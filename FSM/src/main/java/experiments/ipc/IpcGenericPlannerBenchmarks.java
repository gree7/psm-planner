package experiments.ipc;

import cz.agents.dimap.tools.GenericPlanner;
import cz.agents.dimap.tools.GenericResult;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class IpcGenericPlannerBenchmarks extends IpcBenchmarksCli {

	private GenericPlanner planner;

	public IpcGenericPlannerBenchmarks(GenericPlanner planner) {
		this.planner = planner;
	}

	@Override
	public GenericResult runExperiment(PddlProblem problem) {
		return planner.plan(problem);
	}
	
}
