package cz.agents.dimap.tools.fd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.agents.dimap.Settings;
import cz.agents.dimap.tools.GenericResult;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.SasParser;

public class Downward {

    public static boolean DEBUG = false;
	
    public static List<List<String>> runDownward(PddlProblem problem, String prefix, boolean notOptimal) throws InterruptedException {
        File tmpDir;
        try {
            tmpDir = new File(Files.createTempDirectory(Settings.TMP_FILES_PREFIX + problem.domain.domainName+"-"+prefix+"-").toUri());
            Files.copy(new ByteArrayInputStream(problem.domain.toString().getBytes()), new File(tmpDir, "domain.pddl").toPath());
            Files.copy(new ByteArrayInputStream(problem.toString().getBytes()), new File(tmpDir, "problem.pddl").toPath());

            List<List<String>> createdPlans = runDownward(tmpDir, notOptimal);

            if (Settings.DELETE_TMP_FILES_ON_EXIT) {
           		deleteTmpDirectory(tmpDir);
            }
            
            return createdPlans;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized static void deleteTmpDirectory(File tmpDir) {
        for (File file : tmpDir.listFiles()) {
            file.delete();
        }
        tmpDir.delete();
    }
    
    public synchronized static void cleanAllTmpFiles() {
        if (Settings.DELETE_TMP_FILES_ON_EXIT) {
            forceCleanAllTmpFiles();
        }
    }

    public synchronized static void forceCleanAllTmpFiles() {
        File sysTmpDir = new File(System.getProperty("java.io.tmpdir"));
        for (File tmpDir : sysTmpDir.listFiles()) {
            if (tmpDir.getName().startsWith("dimap")) {
                deleteTmpDirectory(tmpDir);
            }
            if (tmpDir.getName().startsWith("polystar")
                    || tmpDir.getName().startsWith("tmpFile")) {
                tmpDir.delete();
            }
        }
    }

    public static SasFile runTranslator(File agentDir) throws Exception {
        String downwardDir;
        if (new File("downward").exists()) {
            downwardDir = "/downward/";
        } else {
            downwardDir = "/../downward-src/";
        }
        ProcessBuilder processBuilder = new ProcessBuilder(System.getProperty("user.dir")+downwardDir+"translate/translate.py", "domain.pddl", "problem.pddl");

        processBuilder.directory(agentDir);
        if (Downward.DEBUG) { processBuilder.redirectErrorStream(true); }

        Process process = processBuilder.start();
        process.waitFor();

        File sasFile = new File(agentDir, "output.sas");
        if (!sasFile.exists()) {
            throw new DownwardException("No created SAS!");
        }

        return new SasFile(sasFile);
    }

    public static List<List<String>> runDownward(File agentDir, boolean notOptimal) throws InterruptedException {
        try {
            File plansFile = new File(agentDir, "createdPlans");

            int lastPlanCount = readPlanCount(plansFile);

            ProcessBuilder processBuilder;
            String downwardDir;
            if (new File("downward").exists()) {
                downwardDir = "/downward/";
            } else {
                downwardDir = "/../downward-src/";
            }
            if (notOptimal) {
                processBuilder = new ProcessBuilder(System.getProperty("user.dir")+downwardDir+"plan-dimap", "domain.pddl", "problem.pddl");
            } else {
                processBuilder = new ProcessBuilder(System.getProperty("user.dir")+downwardDir+"plan-lama-limited", "domain.pddl", "problem.pddl");
            }
            processBuilder.directory(agentDir);
            if (Downward.DEBUG) { processBuilder.redirectErrorStream(true); }

            Process process = processBuilder.start();
            process.waitFor();
            
           	processDownwardOutput(process, plansFile);

            return readNewPlans(plansFile, lastPlanCount);

        } catch (IOException e) {
        	System.err.println("Downward IO error:");
            e.printStackTrace();
            throw new DownwardException(e.getMessage());
        } catch (InterruptedException e) {
        	System.err.println("Downward: Interrupted ("+e.getMessage()+").");
        	throw e;
        }
        
    }

	private static void processDownwardOutput(Process process, File plansFile) throws IOException {
		if (Downward.DEBUG) {
			processDownwardOutput(process.getInputStream(), plansFile, true);
		}
		else {
			processDownwardOutput(process.getInputStream(), plansFile, false);
			processDownwardOutput(process.getErrorStream(), plansFile, true);
		}
	}

    private static void processDownwardOutput(InputStream stream, File plansFile, boolean copyToStdErr) throws IOException {
    	String line;
    	BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		while ((line = reader.readLine()) != null) {
			if (line.contains("Killed") && !Downward.DEBUG) {
				if (plansFile.exists()) {
					break; // some plans generated but killed by plan-lama-limited script => not an error
				}
				throw new DownwardException("Killed");
			}
			if (copyToStdErr) {
				System.err.println(line);
			}
		    if (line.contains("MemoryError") || 
		    	line.contains("Memory limit has been reached") || 
                line.contains("Failed to allocate memory") ||
                line.contains("cannot allocate")
		    ) {
		    	throw new DownwardOutOfMemoryException("Out of memory: "+line);
		    }
		}
		return;
	}

	private static int readPlanCount(File plansFile) throws IOException {
        if (!plansFile.exists()) {
            return 0;
        } else {
            BufferedReader br = new BufferedReader(new FileReader(plansFile));
            int planCount = Integer.parseInt(br.readLine());
            br.close();
            return planCount;
        }
    }

    public static List<List<String>> readNewPlans(File plansFile, int lastPlanCount) throws IOException {
        if (!plansFile.exists()) {
            throw new DownwardException("No created plans!");
        }
        BufferedReader br = new BufferedReader(new FileReader(plansFile));

        int newPlanCount = Integer.parseInt(br.readLine());
        int toReadCount = newPlanCount - lastPlanCount;
        List<List<String>> plans = new ArrayList<>(toReadCount);
        for (int i = 0; i < toReadCount; i++) {

            List<String> stringPlan = SasParser.readPlan(br);
            plans.add(stringPlan);
        }
        br.close();

        return plans;
    }
    
    public static void stripPlans(File agentDir, Map<Integer, Integer> planStripInfos) {
    	File oldPlansFile = new File(agentDir, "createdPlans");
    	File newPlansFile = new File(agentDir, "createdPlans.new");
    	try {
			BufferedReader reader = new BufferedReader(new FileReader(oldPlansFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(newPlansFile));
			int planCount = Integer.parseInt(reader.readLine());
			writer.write(planCount+"\n");
			for (int planIndex = 0; planIndex < planCount; planIndex++) {
				if (planStripInfos.containsKey(planIndex)) {
					SasParser.stripPlanAfterActionIndex(reader, writer, planStripInfos.get(planIndex));
				}
				else {
					SasParser.copyPlan(reader, writer);
				}
			}
			reader.close();
			writer.close();
			oldPlansFile.delete();
			newPlansFile.renameTo(oldPlansFile);
		} catch (IOException e) {
			return;
		}
    }

    public static SasFile runTranslator(PddlProblem problem) {
        try {
            File tmpDir = new File(Files.createTempDirectory(Settings.TMP_FILES_PREFIX + problem.domain.domainName+"-translate-").toUri());
            Files.copy(new ByteArrayInputStream(problem.domain.toString().getBytes()), new File(tmpDir, "domain.pddl").toPath());
            Files.copy(new ByteArrayInputStream(problem.toString().getBytes()), new File(tmpDir, "problem.pddl").toPath());

            return runTranslator(tmpDir);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void killDownward() {
    	try {
			Runtime.getRuntime().exec("killall -KILL downward-1");
			Runtime.getRuntime().exec("killall -KILL downward-2");
			Runtime.getRuntime().exec("killall -KILL downward-4");
		} catch (IOException e) {
			System.err.println("killDownward: killall failed: "+e.getMessage());
			e.printStackTrace();
		}
    }
    
    public static GenericResult logDownward(PddlProblem problem, String problemName) {
    	GenericResult result = new GenericResult();
        System.out.print(problemName+" time: ");
        try {
            result.planningTime = measureDownward(problem, problemName);
            result.solved = true;
            System.out.printf("%.02fs%n", result.planningTime/1000.0);
        } catch (DownwardOutOfMemoryException e) {
            System.err.println("Downward - out of memory!");
            result.planningInfo = "out of memory";
        } catch (DownwardException e) {
            System.err.println("Downward ERROR --- " + e.getMessage());
            result.planningInfo = "Downward ERROR: "+e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            result.planningInfo = "ERROR: "+e.getMessage();
        }
        return result;
    }

    private static long measureDownward(PddlProblem problem, String problemName) throws Exception {
        long startTime = System.currentTimeMillis();
        Downward.runDownward(problem, problemName, false);
        return System.currentTimeMillis() - startTime;
    }


}


