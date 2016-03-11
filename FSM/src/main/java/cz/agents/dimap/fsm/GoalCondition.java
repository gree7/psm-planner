package cz.agents.dimap.fsm;

public interface GoalCondition<S> {
	boolean isGoal(S state);
}
