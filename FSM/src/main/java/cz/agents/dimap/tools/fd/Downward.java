package cz.agents.dimap.tools.fd;

import cz.agents.dimap.tools.GenericResult;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.Problem;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.SasParser;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Downward {

    private static final boolean DELETE_TMP_FILES_ON_EXIT = false;
    public static final String TMP_FILES_PREFIX = "";

    public static List<List<String>> runDownward(Problem problem, String prefix) throws InterruptedException {
        File tmpDir;
        try {
            tmpDir = new File(Files.createTempDirectory(TMP_FILES_PREFIX + problem.domainName + "-" + prefix + "-").toUri());
            Files.copy(problem.domain.toPath(), new File(tmpDir, "domain.pddl").toPath());
            Files.copy(problem.problem.toPath(), new File(tmpDir, "problem.pddl").toPath());

            List<List<String>> createdPlans = runDownward(tmpDir);

            if (DELETE_TMP_FILES_ON_EXIT) {
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
        if (DELETE_TMP_FILES_ON_EXIT) {
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
            downwardDir = File.separator + "downward" + File.separator;
        } else {
            downwardDir = File.separator + ".." + File.separator + "downward" + File.separator;
        }

        ProcessBuilder processBuilder = new ProcessBuilder(System.getProperty("user.dir") + downwardDir + "translate-sas", "domain.pddl", "problem.pddl");

        processBuilder.directory(agentDir);
        if (Settings.DEBUG) {
            processBuilder.redirectErrorStream(true);
        }
        //processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        process.waitFor();

        if (process.exitValue() != 0) {
            throw new IllegalStateException("Translation to SAS failed.");
        }

        File sasFile = new File(agentDir, "output.sas");

        //System.out.println("test now");
        //sasFile = new File(".." + File.separator + "tests" + File.separator + "oneDE");
        //sasFile = new File(".." + File.separator + "tests" + File.separator + "noDE");
        //sasFile = new File(".." + File.separator + "tests" + File.separator + "oneSD");
        //sasFile = new File(".." + File.separator + "tests" + File.separator + "noSD_2");
        //sasFile = new File(".." + File.separator + "tests" + File.separator + "noSD");
        //sasFile = new File(".." + File.separator + "sasExperiment" + File.separator + "testDEmoreLayers.txt");
        //sasFile = new File(".." + File.separator + "sasExperiment" + File.separator + "testDEmultiple.txt");
        //sasFile = new File(".." + File.separator + "sasExperiment" + File.separator + "testMWthreeConsumers.txt");
        //sasFile = new File(".." + File.separator + "sasExperiment" + File.separator + "testMWmultiLayer.txt");
        //sasFile = new File(".." + File.separator + "sasExperiment" + File.separator + "testMWsimple.txt");
        //sasFile = new File(".." + File.separator + "sasExperiment" + File.separator + "testSSO.txt");
        //sasFile = new File(".." + File.separator + "sasExperiment" + File.separator + "testMP.txt");
        //sasFile = new File(".." + File.separator + "sasExperiment" + File.separator + "testSD.txt");
        //sasFile = new File(".." + File.separator + "sasExperiment" + File.separator + "testSDextended.txt");
        //sasFile = new File(".." + File.separator + "sasExperiment" + File.separator + "testUV.txt");

        if (!sasFile.exists()) {
            throw new DownwardException("No created SAS!");
        }

        if(Settings.DEBUG){
            System.out.println(sasFile.getAbsolutePath());
        }

        return new SasFile(sasFile);
    }

    public static List<List<String>> runDownward(File agentDir) throws InterruptedException {
        try {
            File plansFile = new File(agentDir, "createdPlans");

            int lastPlanCount = readPlanCount(plansFile);

            ProcessBuilder processBuilder;
            String downwardDir;
            if (new File("downward").exists()) {
                downwardDir = File.separator + "downward" + File.separator;
            } else {
                downwardDir = File.separator + ".." + File.separator + "downward" + File.separator;
            }

            String planningScript = "plan-lama";
            processBuilder = new ProcessBuilder(System.getProperty("user.dir") + downwardDir + planningScript, "domain.pddl", "problem.pddl");

            if (Settings.DEBUG) {
                processBuilder.redirectErrorStream(true);
            }
            processBuilder.directory(agentDir);
            Process process = processBuilder.start();

            process.waitFor();

            processDownwardOutput(process, plansFile);

            return readNewPlans(plansFile, lastPlanCount);

        } catch (IOException e) {
            System.err.println("Downward IO error:");
            e.printStackTrace();
            throw new DownwardException(e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Downward: Interrupted (" + e.getMessage() + ").");
            throw e;
        }
    }

    private static void processDownwardOutput(Process process, File plansFile) throws IOException {
        if (Settings.DEBUG) {
            processDownwardOutput(process.getInputStream(), plansFile, true);
        } else {
            processDownwardOutput(process.getInputStream(), plansFile, false);
            processDownwardOutput(process.getErrorStream(), plansFile, true);
        }
    }

    private static void processDownwardOutput(InputStream stream, File plansFile, boolean copyToStdErr) throws IOException {
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        while ((line = reader.readLine()) != null) {
            if (line.contains("Killed") && !Settings.DEBUG) {
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
	                line.contains("cannot allocate") ||
	                line.contains("out of memory")
                    ) {
                throw new DownwardOutOfMemoryException("Out of memory: " + line);
            }
            if (line.contains("Usage error occurred")) {
                throw new DownwardException("Usage error occurred.");
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
            writer.write(planCount + "\n");
            for (int planIndex = 0; planIndex < planCount; planIndex++) {
                if (planStripInfos.containsKey(planIndex)) {
                    SasParser.stripPlanAfterActionIndex(reader, writer, planStripInfos.get(planIndex));
                } else {
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

    public static SasFile runTranslator(Problem problem) {
        try {
            File tmpDir = new File(Files.createTempDirectory(TMP_FILES_PREFIX + problem.domainName + "-translate-" + problem.problem.getName()).toUri());
            Files.copy(problem.domain.toPath(), new File(tmpDir, "domain.pddl").toPath());
            Files.copy(problem.problem.toPath(), new File(tmpDir, "problem.pddl").toPath());
            return runTranslator(tmpDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void killDownward() {
        try {
            Runtime.getRuntime().exec("killall -KILL downward-release");
            Runtime.getRuntime().exec("killall -KILL downward-1");
            Runtime.getRuntime().exec("killall -KILL downward-2");
            Runtime.getRuntime().exec("killall -KILL downward-4");
        } catch (IOException e) {
            System.err.println("killDownward: killall failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static GenericResult logDownward(Problem problem, String problemName) {
        GenericResult result = new GenericResult();
        System.out.print(problemName + " time: ");
        try {
            result.planningTime = measureDownward(problem, problemName);
            result.solved = true;
            System.out.printf("%.02fs%n", result.planningTime / 1000.0);
        } catch (DownwardOutOfMemoryException e) {
            System.err.println("Downward - out of memory!");
            result.planningInfo = "out of memory";
        } catch (DownwardException e) {
            System.err.println("Downward ERROR --- " + e.getMessage());
            result.planningInfo = "Downward ERROR: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            result.planningInfo = "ERROR: " + e.getMessage();
        }
        return result;
    }

    private static long measureDownward(Problem problem, String problemName) throws Exception {
        long startTime = System.currentTimeMillis();
        Downward.runDownward(problem, problemName);
        return System.currentTimeMillis() - startTime;
    }

    public static SasFile runPreprocess(SasFile sas) {
        try {
            File tmpDir = new File(Files.createTempDirectory(TMP_FILES_PREFIX + "sas-preprocess").toUri());
            File sasFile = storeSas(tmpDir, sas);
            File preprocessedSas = runPreprocess(tmpDir, sasFile);
            return new SasFile(preprocessedSas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private synchronized static File runPreprocess(File dir, File reducedSas) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(System.getProperty("user.dir") + File.separator + "downward" + File.separator + "preprocess-sas", reducedSas.getName());
            processBuilder.directory(dir);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            process.waitFor();

            if (0 != process.exitValue()) {
                throw new IllegalStateException("Cannot continue since preprocess of SAS failed.");
            }

            String outputFileName = "output";
            File processedFile = new File(dir, outputFileName);
            if (!processedFile.exists()) {
                throw new IOException("The file that should contain processed SAS '" + processedFile.getAbsolutePath() + "' was not found.");
            }

            if (Settings.DEBUG) System.out.println("processed SAS stored in " + processedFile.getAbsolutePath());

            return processedFile;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static File storeSas(File dir, SasFile sas) {
        File outputFile = new File(dir, "reduced.sas");
        PrintWriter storage = null;
        try {
            storage = new PrintWriter(outputFile.getAbsoluteFile());
            storage.print(sas.getFinalSAS());
            storage.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            storage.close();
        }
        return outputFile;
    }


    public static Plan runDownwardWithReducedSas(SasFile sas, String problemName) throws InterruptedException {
        if (Settings.ERASE_TMP) {
            forceCleanAllTmpFiles();
        }

        try {
            File tmpDir = new File(Files.createTempDirectory(TMP_FILES_PREFIX + problemName).toUri());
            File reducedSas = storeSas(tmpDir, sas);
            File planFile = runDownward(tmpDir, reducedSas);

            if (DELETE_TMP_FILES_ON_EXIT) {
                deleteTmpDirectory(tmpDir);
            }

            if (Settings.DEBUG) System.out.println("plan stored in " + planFile.getAbsolutePath());

            return new Plan(sas, planFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static File runDownward(File dir, File sas) throws InterruptedException {
        try {
            if(Settings.DEBUG) System.out.println("planner started");
            final File plansFile = new File(dir, "sas_plan.1");
            final File plansFile1 = new File(dir, "sas_plan");

            ProcessBuilder processBuilder;
            String downwardDir;
            if (new File("downward").exists()) {
                downwardDir = File.separator + "downward" + File.separator;
            } else {
                downwardDir = File.separator + ".." + File.separator + "downward" + File.separator;
            }


            if(Settings.DEBUG){
                System.out.println("tmpDir: " + dir.getAbsolutePath());
            }

            processBuilder = new ProcessBuilder(System.getProperty("user.dir") + downwardDir + "plan-sas", sas.getName());

            if (Settings.DEBUG) {
                processBuilder.redirectErrorStream(true);
            }
            processBuilder.directory(dir);
            final Process process = processBuilder.start();

            if (Settings.KILL_PLANNER_ASAP) {
                Thread watchdog = new Thread() {
                    public void run() {
                        try {
                            while (true) {
                                if (plansFile.exists() || plansFile1.exists()) {
//                                    Thread.sleep(1000);
                                    process.destroy();
                                    killDownward();
                                    return;
                                }
                                // check whether the process is still running
                                try {
                                    process.exitValue(); // throws if the process is still running
                                    if (plansFile.exists() || plansFile1.exists()) {
                                        process.destroy();
                                        killDownward();
                                        return;
                                    }
                                    processDownwardOutput(process.getErrorStream(), plansFile, true);
                                    throw new DownwardException("Downward exited unexpectedly");
                                } catch (IOException ex) {
                                    throw new DownwardException("IOException: " + ex.getMessage());
                                } catch (IllegalThreadStateException ex) {
                                    // nothing to do
                                }
                                Thread.sleep(100);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                watchdog.run();
                watchdog.join();
            }
            process.waitFor();

            if(Settings.DEBUG) System.out.println("planner finished");

            if (!plansFile.exists() && !plansFile1.exists()) {
                throw new IllegalStateException("Cannot continue since plan file was not found '" + plansFile.getAbsolutePath() + "'.");
            }
            return plansFile.exists() ? plansFile : plansFile1;

        } catch (IOException e) {
            System.err.println("Downward IO error:");
            e.printStackTrace();
            throw new DownwardException(e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Downward: Interrupted (" + e.getMessage() + ").");
            throw e;
        }
    }


}


