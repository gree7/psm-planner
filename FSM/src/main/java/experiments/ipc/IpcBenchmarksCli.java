package experiments.ipc;

import cz.agents.dimap.tools.GenericResult;

import java.io.File;

public abstract class IpcBenchmarksCli extends IpcBenchmarks {
	
	public static final double TIMEOUT_TOLERANCE = 0.98;

	public void runExperimentCli(String[] args) {
		if (args.length != 2) {
			System.err.println("Error: Please specify domain and problem PDDL files as command line arguments.");
			System.err.println("usage: <prg> domain.pddl problem.pddl");
			return;
		}

		runExperimentCli(new File(args[0]), new File(args[1]));
	}

	public void runExperimentCli(File domainFile, File problemFile) {
        long startTime = System.currentTimeMillis();

		String domainName = problemFile.getParentFile().getName();
		String problemName = problemFile.getName().replaceFirst("[.][^.]+$", ""); // remove (last) extension ".ext"
		System.out.printf("\n=== Running %s:%s ===\n", domainName, problemName);

		GenericResult result = runExperiment(domainFile, problemFile);
        long durationMs = System.currentTimeMillis() - startTime;
        
        if (durationMs >= TIMEOUT_TOLERANCE * (TIMEOUT_MINUTES * 60 * 1000)) {
        	result.solved = false;
        	result.planningTime = durationMs;
        	result.planningInfo = "timeout";
        }
        if (result.solved && Double.isNaN(result.planningTime)) {
        	result.planningTime = durationMs;
        }
        
		System.out.println(result.toString(domainName, problemName));
		printRuntimeStatistics();
	}

	private void printRuntimeStatistics() {
		System.out.println("JVM total memory: " + Runtime.getRuntime().totalMemory() / (1024.0 * 1024) + " MB");
		System.out.println("JVM free memory: " + Runtime.getRuntime().freeMemory() / (1024.0 * 1024) + " MB");
		System.out.println("JVM max memory: " + Runtime.getRuntime().maxMemory() / (1024.0 * 1024) + " MB");
		System.out.println("JVM available processors: " + Runtime.getRuntime().availableProcessors());
	}
}