package cz.agents.dimap.psm.planner;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.Queue;

import cz.agents.dimap.fsm.FiniteStateMachineTools;
import cz.agents.dimap.fsm.GoalCondition;
import cz.agents.dimap.fsm.LabelFactory;
import cz.agents.dimap.psm.PlanLabelFactory;
import cz.agents.dimap.psm.PlanStateMachine;
import cz.agents.dimap.psm.PublicProjection;
import cz.agents.dimap.psm.operator.Operator;
import cz.agents.dimap.psm.state.BitSetStateFactory;
import cz.agents.dimap.psm.state.State;

public class BfsPsmPlanner {

	private PlanStateMachine fsm;
	private State initState;
	private Collection<Operator> operators;
	private boolean dotOutputAllowed;
	
	public BfsPsmPlanner(State initState, Collection<Operator> operators) {
		this(initState, operators, false);
	}

	public BfsPsmPlanner(State initState, Collection<Operator> operators, boolean dotOutputAllowed) {
		this.initState = initState;
		this.operators = operators;
		this.dotOutputAllowed = dotOutputAllowed;
		
		fsm = new PlanStateMachine(initState);
	}

	public void createAllPlans(GoalCondition<State> goalCondition, BitSetStateFactory bssFactory) {
		Queue<State> open = new ArrayDeque<State>();
		open.add(initState);
		
		Collection<State> reachedGoals = new HashSet<State>();
		
		while (!open.isEmpty()) {
			State state = open.poll();
			if ( goalCondition.isGoal(state) ) {
				reachedGoals.add(state);
                continue;
			}
			
			for (Operator operator : operators) {
				State toState = operator.tryToApply(state);
                if (toState != null) {
					if (fsm.addTransition(state, operator, toState)) {
						if (!open.contains(toState)) {
							open.add( toState );
						}
					}
				}
			}
		}
		
		PlanLabelFactory labelFactory = new PlanLabelFactory(bssFactory);
		
		fsmToDot("fsm", labelFactory, goalCondition);

		fsm.removeStatesNotLeadingTo( reachedGoals );
		
		fsmToDot("fsm-reduced", labelFactory, goalCondition);
	}

	public void fsmToDot(String fileName, LabelFactory<State, Operator> labelFactory, GoalCondition<State> goalCondition) {
		fsmToDot(fileName, labelFactory, false, goalCondition);
	}
	
	public void fsmToDot(String fileName, LabelFactory<State, Operator> labelFactory, boolean forceDot, GoalCondition<State> goalCondition) {
		if (forceDot || dotOutputAllowed) {
			FiniteStateMachineTools.imgOutput(fsm, fileName, labelFactory, goalCondition);
		}
	}
	
	public void publicProjection(BitSetStateFactory bssFactory) {
		PublicProjection.publicProjection(fsm, bssFactory);
	}

    public PlanStateMachine getFsm() {
        return fsm;
    }
}
