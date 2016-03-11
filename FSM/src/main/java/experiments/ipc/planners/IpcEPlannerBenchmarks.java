package experiments.ipc.planners;

import cz.agents.dimap.tools.miniplan.EPlanner;
import experiments.ipc.IpcGenericPlannerBenchmarks;

public class IpcEPlannerBenchmarks {

	public static void main(String[] args) {
		new IpcGenericPlannerBenchmarks(new EPlanner()).runExperimentCli(args);
	}
	
}
