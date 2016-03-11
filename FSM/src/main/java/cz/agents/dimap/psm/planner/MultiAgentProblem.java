package cz.agents.dimap.psm.planner;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import cz.agents.dimap.tools.pddl.PddlProblem;

public interface MultiAgentProblem {

    Map<String, PddlProblem> getProblems();
    
    PddlProblem getOriginalRelaxedProblem();
    
    String getImageOutputPath();

    Collection<String> getAgents();

    PddlProblem toSingleAgentProblem();

    List<String> revertPlan(List<String> finalPlan);

}
