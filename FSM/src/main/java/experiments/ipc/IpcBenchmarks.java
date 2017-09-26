package experiments.ipc;

import cz.agents.dimap.tools.GenericResult;
import cz.agents.dimap.tools.fd.Downward;
import cz.agents.dimap.tools.fd.DownwardException;
import cz.agents.dimap.tools.fd.DownwardOutOfMemoryException;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

public abstract class IpcBenchmarks {

    final long TIMEOUT_MINUTES = 5;

    public static final String[] DOMAINS = new String[]{
//      "beerproblem",
            "airport",
            "barman-sat11-strips",
            "blocks",
            "depot",
            "driverlog",
            "elevators-sat08-strips",
            "elevators-sat11-strips",
            "floortile-sat11-strips",
            "freecell",
            "gripper",
            "logistics00",
            "logistics98",
            "miconic",
            "mystery",
            "no-mprime",
            "no-mystery",
            "nomystery-sat11-strips",
            "openstacks-sat08-strips",
            "openstacks-sat11-strips",
            "openstacks-strips",
            "parcprinter-08-strips",
            "parcprinter-sat11-strips",
            "parking-sat11-strips",
            "pegsol-08-strips",
            "pegsol-sat11-strips",
            "pipesworld-notankage",
            "pipesworld-tankage",
            "psr-small",
            "rovers",
            "satellite",
            "scanalyzer-08-strips",
            "scanalyzer-sat11-strips",
            "sokoban-sat08-strips",
            "sokoban-sat11-strips",
            "storage",
            "tidybot-sat11-strips",
            "tpp",
            "transport-sat08-strips",
            "transport-sat11-strips",
            "trucks-strips",
            "visitall-sat11-strips",
            "woodworking-sat08-strips",
            "woodworking-sat11-strips",
            "zenotravel",

// FIX
//        "movie", 

//FEW PROBLEMS
//        "grid",

//domains with forall / when / exists  
//      "airport-adl", 
//      "assembly", 
//      "miconic-fulladl", 
//      "miconic-simpleadl", 
//      "openstacks", 
//      "openstacks-sat08-adl", 
//      "optical-telegraphs", 
//      "philosophers", 
//      "psr-large", 
//      "psr-middle", 
//      "schedule", 
//      "trucks", 
//      "pathways", 
//      "pathways-noneg", 

//BROKEN domains
//      "mprime",  // FD translate fails 

    };

    public static final String BENCHMARKS_DIR = "./benchmarks/";

    public abstract GenericResult runExperiment(File domain, File problem);

    public void generateBenchmarks() {
        for (File problemDir : IpcBenchmarks.getProblemDirs()) {
            List<File> problemFiles = IpcBenchmarks.getProblemFiles(problemDir);
            //for (int i=0; i<problemFiles.size(); i++) {
            for (int i : selectProblems(problemFiles)) {
                File problemFile = problemFiles.get(i);
                File domainFile = getDomainFile(problemDir, problemFile);
                System.out.println(domainFile + " " + problemFile);
            }
        }
    }

    private Collection<Integer> selectProblems(List<File> problemFiles) {
        final int selectCount = 15;
        List<Integer> selected = new LinkedList<>();
        double delta = (problemFiles.size() - 1) / (selectCount - 1.0);
        double r = 0.0;
        for (int i = 0; i < selectCount; i++, r += delta) {
            selected.add((int) Math.floor(r));
        }
        return selected;
    }

    public void runBenchmarks() {
        System.out.println("java.io.tmpdir: " + System.getProperty("java.io.tmpdir"));

        ExecutorService executor = Executors.newSingleThreadExecutor();

        for (File problemDir : IpcBenchmarks.getProblemDirs()) {
            List<File> problemFiles = IpcBenchmarks.getProblemFiles(problemDir);

            System.out.println("cahnge of benchamrks");
            //int start = 0;
            //int end = (problemFiles.size() < 5) ? problemFiles.size() : 5;

            //for (int i = start; i < end; i++) {
            for (int i = 0; i < problemFiles.size(); i++) {
//            for (int i : new int[]{1}) {
                GenericResult result = new GenericResult();
                double startPlanningTime = System.currentTimeMillis();

                File problemFile = problemFiles.get(i);
                String domainName = problemFile.getParentFile().getName();
                String problemName = problemFile.getName().replaceFirst("[.][^.]+$", ""); // remove (last) extension ".ext"
                File domainFile = getDomainFile(problemDir, problemFile);

                try {
                    System.out.printf("\n=== Running %s:%s(%d) ===\n", domainName, problemName, i);
                    if (TIMEOUT_MINUTES > 0) {
                        result = executor.invokeAny(Arrays.asList(makeTask(domainFile, problemFile)), TIMEOUT_MINUTES, TimeUnit.MINUTES);
                    } else {
                        result = executor.invokeAny(Arrays.asList(makeTask(domainFile, problemFile)));
                    }
                    //} catch (IOException ex) {
                    //    result.planningInfo = "IO ERROR: " + ex.getMessage();
                    //System.out.println("Skipping -- cannot read: " + ex.getMessage());
                } catch (UnsupportedOperationException ex) {
                    result.planningInfo = "UNSUPPORTED ERROR: " + ex.getMessage();
                    System.out.println("Error: " + ex.getMessage());
                } catch (TimeoutException e) {
                    result.planningInfo = "timeout";
                    Downward.cleanAllTmpFiles();
                } catch (InterruptedException e) {
                    result.planningInfo = "INTERRUPTED: " + e.getMessage();
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    try {
                        throw e.getCause();
                    } catch (TimeoutException e1) {
                        result.planningInfo = "timeout";
                        Downward.cleanAllTmpFiles();
                    } catch (DownwardOutOfMemoryException e1) {
                        result.planningInfo = "out of memory";
                        Downward.cleanAllTmpFiles();
                    } catch (DownwardException e1) {
                        e1.printStackTrace();
                        result.planningInfo = e.getCause().getMessage();
                    } catch (Throwable e1) {
                        result.planningInfo = "UNKNOWN ERROR: " + e.getCause().getMessage();
                        e1.printStackTrace();
                    }
                } catch (Exception ex) {
                    result.planningInfo = "ERROR: " + ex.getMessage();
                    ex.printStackTrace();
                }

                if (Double.isNaN(result.planningTime)) {
                    result.planningTime = System.currentTimeMillis() - startPlanningTime;
                }
                System.out.println(result.toString(domainName, problemName, i));
                System.out.println("JVM used memory: " + Runtime.getRuntime().totalMemory() / (1024.0 * 1024) + " MB");
            }
        }

        executor.shutdownNow();
        Downward.killDownward();
    }

    public Callable<GenericResult> makeTask(final File domainFile, final File problemFile) {
        return new Callable<GenericResult>() {
            @Override
            public GenericResult call() throws Exception {
                return runExperiment(domainFile, problemFile);
            }
        };
    }

    public static List<File> getProblemDirs() {
        List<File> subdirs = new ArrayList<>();
        for (String domain : IpcBenchmarks.DOMAINS) {
            subdirs.add(new File(IpcBenchmarks.BENCHMARKS_DIR, domain));
        }
        return subdirs;
    }

    public static List<File> getProblemFiles(File problemDir) {
        List<File> problemFiles = new ArrayList<>();
        for (File file : problemDir.listFiles()) {
            if (!file.getName().contains("domain")) {
                problemFiles.add(file);
            }
        }
        Collections.sort(problemFiles, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return (int) (o1.length() - o2.length());
            }
        });
        return problemFiles;
    }

    protected static File getDomainFile(File problemDir, File problemFile) {
        File file = new File(problemDir, "domain.pddl");
        if (!file.exists()) {
            String problemName = problemFile.getName().substring(0, 3);
            file = new File(problemDir, problemName + "-domain.pddl");
            if (!file.exists()) {
                file = new File(problemDir, "domain_" + problemName + ".pddl");
            }
        }
        return file;
    }

    public static void main(String[] args) throws Exception {
        new IpcBenchmarks() {
            @Override
            public GenericResult runExperiment(File domain, File problem) {
                return null;
            }
        }.generateBenchmarks();
    }
}
