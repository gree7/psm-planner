package cz.agents.dimap.psm.planner;

import java.util.List;

public interface AskOtherAgents {
    int getOtherAgentNum();

    int computeLastMarkReachableByAllAgents(List<String> publicPlan);
}
