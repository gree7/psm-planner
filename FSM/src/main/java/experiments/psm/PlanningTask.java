package experiments.psm;

import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.psm.planner.PlanningResult;
import cz.agents.dimap.psm.planner.PsmPlanner;

final public class PlanningTask {
    private final MultiAgentProblem maProblem;

    public PlanningTask(MultiAgentProblem maProblem) {
        this.maProblem = maProblem;
    }

    public PlanningResult run(long endTime) throws Exception {
        return PsmPlanner.plan(maProblem, endTime);
    }
}