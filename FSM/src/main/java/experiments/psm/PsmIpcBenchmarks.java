package experiments.psm;

import java.util.LinkedHashMap;
import java.util.Map;

import cz.agents.dimap.psm.planner.MultiAgentProblem;
import experiments.psm.ipc.IpcDomainInfo;
import experiments.psm.ipc.IpcSingleDomainInfo;

public class PsmIpcBenchmarks extends PsmBenchmarks {

    public static final String BENCHMARKS_DIR = "experiments/ma-benchmarks";

    private static final IpcDomainInfo[] domainInfos = {
    	new IpcSingleDomainInfo("beerproblem", 1),
//    	new IpcSingleDomainInfo("beerproblem-generic", 1),
//        new IpcSingleDomainInfo("elevators", 30),
//        new IpcSingleDomainInfo("woodworking", 30),
//        new IpcSingleDomainInfo("satellites-ipc", 20),
//        new IpcSingleDomainInfo("logistics-ipc", 28),
//        new IpcSingleDomainInfo("rovers-ipc", 40),
    };

    public PsmIpcBenchmarks() {
        super("fmap");
    }

    public static void main(String[] args) throws Exception {
        PsmBenchmarks benchmarks = new PsmIpcBenchmarks();

//        benchmarks.setSettings(args);

        Map<String, MultiAgentProblem> problems = createBenchmarkTasks();

        benchmarks.runBenchmarks(problems);
    }

    private static Map<String, MultiAgentProblem> createBenchmarkTasks() {
        Map<String, MultiAgentProblem> problems = new LinkedHashMap<>();

        System.out.print("creating bencharks:");
        for (IpcDomainInfo domainInfo : domainInfos) {
            System.out.print(" " + domainInfo.getDomainName());
            for (int problemNo = 1; problemNo <= 1/*domainInfo.getProblemCount()*/; problemNo++) {
                String problemName = domainInfo.getDomainName() + "("+String.format("%02d", problemNo)+")";
                try {
                    problems.put(problemName, domainInfo.createProblem(problemNo));
                } catch (Exception e) {
                    System.err.println("ERROR CREATING MA PROBLEM "+domainInfo.getDomainName()+"/"+problemNo+": "+e.getMessage());
                    e.printStackTrace();
                }
                System.out.print('.');
            }
        }
        System.out.println();
        
        System.out.println("problems: " + problems);
        return problems;
    }
}

