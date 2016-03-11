package cz.agents.dimap.tools;

import cz.agents.dimap.tools.pddl.PddlProblem;

public interface GenericPlanner {
	
	GenericResult plan(PddlProblem problem);

}
