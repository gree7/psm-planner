package cz.agents.dimap.psm.operator;

import java.util.Set;

import cz.agents.dimap.psm.state.State;
import cz.agents.dimap.tools.pddl.PddlAction;

public interface Operator {

	String getName();
	
	String getPublicName();

	PddlAction getAction();
	
	State tryToApply(State state);

	boolean isPublic();
	
    Set<? extends Object> getPreconditions();
    Set<? extends Object> getEffects();
}
