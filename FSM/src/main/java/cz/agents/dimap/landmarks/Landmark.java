package cz.agents.dimap.landmarks;

import cz.agents.dimap.tools.pddl.PddlAction;


public interface Landmark {

	void addToProblem(PddlLandmarkProblem problem);
	
    String effectToString();

    String conditionToString();

    PddlAction getAction();
	
}
