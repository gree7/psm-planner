package cz.agents.dimap.psm.planner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cz.agents.dimap.fsm.GoalCondition;
import cz.agents.dimap.psm.PlanLabelFactory;
import cz.agents.dimap.psm.PlanStateMachine;
import cz.agents.dimap.psm.operator.Operator;
import cz.agents.dimap.psm.operator.PddlOperatorFactory;
import cz.agents.dimap.psm.state.BitSetStateFactory;
import cz.agents.dimap.psm.state.BitSetStateFactoryPddl;
import cz.agents.dimap.psm.state.MultiState;
import cz.agents.dimap.psm.state.State;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class PddlPlanner {

	protected State initState;
	
	protected BitSetStateFactoryPddl bitSetStateFactory;
    protected MultiState goalState;
    protected MultiState publicGoalState;
    protected final GoalCondition<State> goalCondition;
    protected final GoalCondition<State> publicGoalCondition;
	protected PlanLabelFactory labelFactory;

    protected Collection<Operator> operators;

    public GoalCondition<State> getGoalCondition() {
        return goalCondition;
    }

    public PddlPlanner(PddlProblem problem) throws Exception {
        bitSetStateFactory = new BitSetStateFactoryPddl(problem);
        labelFactory = new PlanLabelFactory(bitSetStateFactory);
        
        initState = bitSetStateFactory.createInitState(problem.init);
        goalState = bitSetStateFactory.createMultiState(problem.goal);
        publicGoalState = bitSetStateFactory.createPublicMultiState(problem.goal);

        goalCondition = new GoalCondition<State>() {
            @Override
			public boolean isGoal(State state) {
                return goalState.contains(state);
            }
        };

        publicGoalCondition = new GoalCondition<State>() {
            @Override
            public boolean isGoal(State state) {
                return publicGoalState.contains(state);
            }
        };
        
        operators = actionsToOperators(problem.domain.actions);
	}
	
    public PlanStateMachine plan(boolean dotOutputAllowed, @SuppressWarnings("unused") AskOtherAgents otherAgentPlanners) throws InterruptedException {

    	BfsPsmPlanner planner = new BfsPsmPlanner(initState, operators, dotOutputAllowed);

    	planner.createAllPlans( goalCondition, bitSetStateFactory );

    	planner.fsmToDot("fsm-all", labelFactory, false, goalCondition);

    	planner.publicProjection(bitSetStateFactory);

    	planner.fsmToDot("fsm-public", labelFactory, false, publicGoalCondition);
        return planner.getFsm();
    }

    private Collection<Operator> actionsToOperators(List<PddlAction> actions) {
        Collection<Operator> operators = new ArrayList<>(actions.size());
        for (PddlAction action : actions) {
            operators.add(PddlOperatorFactory.createOperator(action, bitSetStateFactory));
        }
        return operators;
    }

    public State getInitState() {
        return initState;
    }

    public BitSetStateFactory getBitSetStateFactory() {
        return bitSetStateFactory;
    }

    public Collection<Operator> getOperators() {
        return operators;
    }

    public GoalCondition<State> getPublicGoalCondition() {
        return publicGoalCondition;
    }
}
