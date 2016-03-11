package cz.agents.dimap.tools.miniplan.state;

import java.util.List;

import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlProblem;

public interface MiniState {

	List<PddlAction> getApplicableActions(PddlProblem problem);
	
	boolean isApplicable(PddlAction action);
	
	MiniState apply(PddlAction action);

	boolean isSolved();
	
	boolean subsumedBy(MiniState other);
}
