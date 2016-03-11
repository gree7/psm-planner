package experiments.ipc.planners;

import cz.agents.dimap.tools.miniplan.BackwardBfsMiniPlanner;
import experiments.ipc.IpcGenericPlannerBenchmarks;

public class IpcMiniPlannerBenchmarks {
	
	public static void main(String[] args) {
		//new IpcGenericPlannerBenchmarks(new MiniPlanner()).runBenchmarks();
		new IpcGenericPlannerBenchmarks(new BackwardBfsMiniPlanner()).runExperimentCli(args);
	}

}
