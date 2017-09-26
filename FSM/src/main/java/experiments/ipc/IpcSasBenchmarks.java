package experiments.ipc;

import java.io.File;

import cz.agents.dimap.tools.GenericResult;

public class IpcSasBenchmarks extends IpcSasReduceBenchmarks {
    @Override
    public GenericResult runExperiment(File domain, File problem) {
        return runExperiment(domain, problem, "");
    }
    
    public static void main(String[] args) {
		new IpcSasBenchmarks().runExperimentCli(args);
	}
}
