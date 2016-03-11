package experiments.psm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cz.agents.dimap.Settings;
import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.fd.Downward;
import experiments.psm.fmap.FmapMultiDomainBijectiveInfo;
import experiments.psm.fmap.FmapMultiDomainVariedInfo;
import experiments.psm.fmap.FmapSingleDomainInfo;
import experiments.psm.fmap.MaStripsMultiAgentProblem;

public class PsmFmapBenchmarks extends PsmBenchmarks {
    
	public static final String FMAP_BENCHMARKS_DIR = "experiments/FMAP-benchmarks";
	public static final String FMAP_PROBLEMDIR_PREFIX = "Pfile";
	
	private static final Map<String, String> depotsAgentMap;
    static
    {
        depotsAgentMap = new HashMap<String, String>();
        depotsAgentMap.put("depot", "Depot");
        depotsAgentMap.put("distributor", "Depot");
        depotsAgentMap.put("truck", "Truck");
    }
	
    private static final DomainInfo[] fmapDomainInfos = {
        new FmapSingleDomainInfo("ma-blocksworld", 34, "DomainMaBlocksworld", "ProblemMaBlocks", true),
        new FmapMultiDomainVariedInfo("depots", 20, "DomainDepots", "ProblemDepots", depotsAgentMap, true), 
        new FmapSingleDomainInfo("driverlog", 20, "DomainDriverlog", "ProblemDriverlog"),
        new FmapSingleDomainInfo("elevators", 30, "DomainElevators", "ProblemElevators"),
        new FmapSingleDomainInfo("logistics", 20, "DomainLogistics", "ProblemLog"),
        new FmapMultiDomainBijectiveInfo("openstacks", 30, "DomainOpenstacks", "ProblemOpenstacks", Arrays.asList("Manager", "Manufacturer")),
        new FmapSingleDomainInfo("rovers", 20, "DomainRovers", "ProblemRover"),
        new FmapSingleDomainInfo("satellite", 20, "DomainSatellite", "ProblemSat", true),
        new FmapMultiDomainBijectiveInfo("woodworking", 30, "DomainWoodworking", "ProblemWoodworkingag", Arrays.asList("Grinder", "Planner", "Saw", "Varnisher")),
        new FmapSingleDomainInfo("zenotravel", 20, "DomainZenotravel", "ProblemZenotravel"),
    };

	public PsmFmapBenchmarks() {
	    super("fmap");
    }

    public static void main(String[] args) throws Exception {

        if (Settings.DELETE_TMP_FILES_ON_RESTART) {
            Downward.forceCleanAllTmpFiles();
        }
        
        
	    PsmBenchmarks benchmarks = new PsmFmapBenchmarks();

	    benchmarks.setSettings(args);

	    Map<String, MultiAgentProblem> problems = createBenchmarkTasks();

	    benchmarks.runBenchmarks(problems);
    }

    public static Map<String, MultiAgentProblem> createBenchmarkTasks() {
        Map<String, MultiAgentProblem> problems = new LinkedHashMap<>();
	    
        System.out.print("creating bencharks");
    	for (DomainInfo domainInfo : fmapDomainInfos) {
    		for (int problemNo = 1; problemNo <= domainInfo.getProblemCount(); problemNo++) {
//    		for (int problemNo = 1; problemNo <= 1; problemNo++) {
    			String problemName = domainInfo.getDomainName() + "("+String.format("%02d", problemNo)+")";
    	        try {
                    MultiAgentProblem maProblem = domainInfo.createProblem(problemNo);
                    if (Settings.USE_MASTRIPS) {
                        maProblem = new MaStripsMultiAgentProblem(maProblem);
                    }
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

