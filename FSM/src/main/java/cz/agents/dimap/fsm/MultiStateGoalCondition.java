package cz.agents.dimap.fsm;

import java.util.Collection;

public class MultiStateGoalCondition<S> implements GoalCondition<S> {

    private final Collection<S> goals;
    
    public MultiStateGoalCondition(Collection<S> goals) {
        this.goals = goals;
    }

    @Override
    public boolean isGoal(S state) {
        return goals.contains(state);
    }
}
