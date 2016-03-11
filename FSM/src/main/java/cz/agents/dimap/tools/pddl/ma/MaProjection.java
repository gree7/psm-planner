package cz.agents.dimap.tools.pddl.ma;

import java.util.Map;

import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.types.PddlType;

public interface MaProjection {

    Map<PddlName, PddlType> getAgents();

    PddlName getOriginalProblemName();

    PddlProblem generateAgentProblem(PddlName agentName);

}