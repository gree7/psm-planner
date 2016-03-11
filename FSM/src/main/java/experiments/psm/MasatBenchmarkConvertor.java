package experiments.psm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import cz.agents.dimap.Settings;
import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.psm.planner.PsmPlanner;
import cz.agents.dimap.psm.planner.PsmPlannerAgent;
import cz.agents.dimap.tools.fd.Downward;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.sas.SasFile;

public class MasatBenchmarkConvertor {
	
	//private static final File MASAT_BENCHMARK_PATH = new File("../masat-benchmarks/fmap");
	private static final File MASAT_BENCHMARK_PATH = new File("../masat-benchmarks/mastrips");

	public MasatBenchmarkConvertor() {
	}
	
	public void convertBenchmarks(Map<String, MultiAgentProblem> problems) throws Exception {
        for (Entry<String, MultiAgentProblem> problem : problems.entrySet()) {
        	System.out.println("Processing "+problem.getKey());
        	PsmPlanner psm = new PsmPlanner(problem.getValue());
        	for (Entry<String, PsmPlannerAgent> entry : psm.getPlanners().entrySet()) {
        		convertBenchmark(problem.getKey(), entry.getKey(), entry.getValue().pddlProblem);
        	}
        }
	}

	private void convertBenchmark(String benchmarkName, String agentName, PddlProblem pddlProblem) throws IOException {
		String domainName = benchmarkName.substring(0, benchmarkName.indexOf("("));
		String problemName = benchmarkName.substring(benchmarkName.indexOf("(")+1, benchmarkName.indexOf(")"));
		System.out.println("--> generating: "+domainName+"/"+problemName+"/"+agentName+".sas");
		
		File problemDir = new File(new File(MASAT_BENCHMARK_PATH, domainName), problemName);
		problemDir.mkdirs();
		
		SasFile agentSas = Downward.runTranslator(pddlProblem);
		String sasContent = agentSas.getContent();
		sasContent = sasContent.replaceAll("pub_", "+");
		sasContent = sasContent.replaceAll("ext_", "+");
		sasContent = sasContent.replaceAll("int_", "-");
		
		String agentSasFilename = agentName+".sas";
		BufferedWriter sasOut = new BufferedWriter(new FileWriter(new File(problemDir, agentSasFilename)));
		sasOut.write(sasContent);
		sasOut.close();
		
		String tableFilename = problemName+".table";
		BufferedWriter tableOut = new BufferedWriter(new FileWriter(new File(problemDir, tableFilename), true));
		tableOut.write(agentSasFilename+" "+agentName);
		tableOut.newLine();
		tableOut.close();
		
		Downward.cleanAllTmpFiles();
	}
	
    public static void main(String[] args) throws Exception {
    	Settings.USE_INITIAL_LANDMARKS.clear();
    	Settings.ALLOW_PRIVATE_GOALS = false;
    	Settings.REPLACE_INTERNALLY_DEPENDENT_ACTIONS = false;
    	Settings.USE_MASTRIPS = true;
    	
	    MasatBenchmarkConvertor benchmarks = new MasatBenchmarkConvertor();
	    Map<String, MultiAgentProblem> problems = PsmFmapBenchmarks.createBenchmarkTasks();
	    benchmarks.convertBenchmarks(problems);
    }

}
