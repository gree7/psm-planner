package cz.agents.dimap.psm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cz.agents.dimap.fsm.FiniteStateMachine;
import cz.agents.dimap.fsm.FiniteStateMachineTools;
import cz.agents.dimap.fsm.MultiStateGoalCondition;
import cz.agents.dimap.planset.PlanSet;
import cz.agents.dimap.psm.operator.Operator;
import cz.agents.dimap.psm.state.State;


public class PlanStateMachine extends FiniteStateMachine<State, Operator> implements PlanSet <Operator> {
    private static final long serialVersionUID = -3438299357551351500L;

    private final Collection<State> goalStates;

	// copy constructor
	private PlanStateMachine(PlanStateMachine fsm) {
	    super(fsm);
	    goalStates = new ArrayList<>(fsm.goalStates);
	}
	
    public PlanStateMachine(State initState) {
        super(initState);
        goalStates = new ArrayList<>();
	}
	
	@Override
	public PlanStateMachine clone() {
		return new PlanStateMachine(this);
	}
	
    @Override
    public void imgOutput(String fileName) {
        FiniteStateMachineTools.imgOutput(this, fileName, new SimplePublicLabelFactory(), new MultiStateGoalCondition<>(goalStates));
    }

    public Collection<State> getGoalStates() {
        return goalStates;
    }

    @Override
    public List<String> getRandomPlan() {
        List<Operator> plan = PlanStateMachineTools.getRandomPlan(this);
        List<String> newPlan = new ArrayList<>(plan.size());
        for (Operator op : plan) {
            newPlan.add(op.getName());
        }
        return newPlan;
    }

    public List<String> getShortestPlan() {
        List<Operator> plan = PlanStateMachineTools.getShortestPlan(this);
        List<String> newPlan = new ArrayList<>(plan.size());
        for (Operator op : plan) {
            newPlan.add(op.getName());
        }
        return newPlan;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public PlanSet<Operator> intersectWith(PlanSet<Operator> psm) {
        if (!(psm instanceof PlanStateMachine)) {
            throw new IllegalArgumentException("Wrong class: " + psm.getClass().getName());
        }
        return PlanStateMachineTools.intersection(this, (PlanStateMachine) psm);
    }

    public void setGoals(Collection<State> goalStates) {
        this.goalStates.clear();
        this.goalStates.addAll(goalStates);
    }

    public void addGoal(State goalState) {
        this.goalStates.add(goalState);
    }

    @Override
    public void addPlan(List<Operator> plan) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return getNumberOfTransitions();
    }
}