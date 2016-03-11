package experiments.ipc;

import java.io.File;
import java.io.IOException;

import cz.agents.dimap.tools.GenericResult;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlProblem;

public abstract class IpcBenchmarksCli extends IpcBenchmarks {

	public void runExperimentCli(String[] args) {
		if (args.length != 2) {
			System.err.println("Error: Please specify domain and problem PDDL files as command line arguments.");
			System.err.println("usage: <prg> domain.pddl problem.pddl");
			return;
		}
		
		PddlDomain.INCLUDE_ACTION_PREFIXES = false;

		File domainFile = new File(args[0]);
		File problemFile = new File(args[1]);
        String domainName = problemFile.getParentFile().getName();
        String problemName = problemFile.getName().replaceFirst("[.][^.]+$", ""); // remove (last) extension ".ext"
        System.out.printf("\n=== Running %s:%s ===\n", domainName, problemName);
		
        PddlProblem problem;
		GenericResult result = new GenericResult();
		try {
			problem = new PddlProblem(domainFile.getPath(), problemFile.getPath());
		} catch (IOException e) {
			result.planningInfo = "PARSE ERROR: "+e.getMessage();
			System.out.println(result.toString(domainName, problemName));
			e.printStackTrace();
			return;
		}
        
        result = runExperiment(problem);
        
		System.out.println(result.toString(domainName, problemName));
		printRuntimeStatistics();
	}

	private void printRuntimeStatistics() {
		System.out.println("JVM total memory: "+Runtime.getRuntime().totalMemory()/(1024.0*1024)+" MB" );
		System.out.println("JVM free memory: "+Runtime.getRuntime().freeMemory()/(1024.0*1024)+" MB" );
		System.out.println("JVM max memory: "+Runtime.getRuntime().maxMemory()/(1024.0*1024)+" MB" );
		System.out.println("JVM available processors: "+Runtime.getRuntime().availableProcessors());
	}
	
}
