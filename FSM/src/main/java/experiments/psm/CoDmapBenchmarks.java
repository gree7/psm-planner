package experiments.psm;

import java.util.LinkedHashMap;
import java.util.Map;

import cz.agents.dimap.Settings;
import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.fd.Downward;
import experiments.psm.codmap.CoDmapDomainInfo;
import experiments.psm.file.CoDmapMultiAgentProblem;

public class CoDmapBenchmarks extends PsmBenchmarks {
    public static final String DIR = "experiments/CoDMAP/";

    private static final DomainInfo[] domainInfos = {
        new CoDmapDomainInfo("Blocksworld", DIR + "blocksworld-factored"),
        new CoDmapDomainInfo("Depots", DIR + "depot-factored"),
        new CoDmapDomainInfo("Driverlog", DIR + "driverlog-factored"),
        new CoDmapDomainInfo("Elevators", DIR + "elevators08-factored"),
        new CoDmapDomainInfo("Logistics", DIR + "logistics00-factored"),
        new CoDmapDomainInfo("Rovers", DIR + "rovers-factored"),
        new CoDmapDomainInfo("Satellites", DIR + "satellites-factored"),
        new CoDmapDomainInfo("Sokoban", DIR + "sokoban-factored"),
        new CoDmapDomainInfo("Woodworking", DIR + "woodworking08-factored"),
        new CoDmapDomainInfo("Zenotravel", DIR + "zenotravel-factored"),
    };

    public CoDmapBenchmarks() {
        super("codmap");
    }

    public static void main(String[] args) throws Exception {

        if (Settings.DELETE_TMP_FILES_ON_RESTART) {
            Downward.forceCleanAllTmpFiles();
        }

        PsmBenchmarks benchmarks = new CoDmapBenchmarks();

        benchmarks.setSettings(args);

        Map<String, MultiAgentProblem> problems = createBenchmarkTasks();

        benchmarks.runBenchmarks(problems);
    }

    public static Map<String, MultiAgentProblem> createBenchmarkTasks() {
        Map<String, MultiAgentProblem> problems = new LinkedHashMap<>();

        System.out.print("creating bencharks");
        for (DomainInfo domainInfo : domainInfos) {
            for (int problemNo = 0; problemNo < domainInfo.getProblemCount(); problemNo++) {
//            for (int problemNo : new int[] {19, 26, 27}) {//, 29, 30, 31, 32, 33, 34}) {
//            for (int problemNo = 5; problemNo <= 10; problemNo++) {
                String problemName = domainInfo.getDomainName() + "("+String.format("%02d", problemNo)+")";
                try {
                    MultiAgentProblem maProblem = domainInfo.createProblem(problemNo);
                    maProblem = CoDmapMultiAgentProblem.getInstance(maProblem);
                    problems.put(problemName, maProblem);
                } catch (Exception e) {
                    System.err.println("ERROR CREATING MA PROBLEM "+domainInfo.getDomainName()+"/"+problemNo+": "+e.getMessage());
                    e.printStackTrace();
                    System.exit(-1);
                }
                System.out.print('.');
            }
        }
        System.out.println();
        return problems;
    }
}

