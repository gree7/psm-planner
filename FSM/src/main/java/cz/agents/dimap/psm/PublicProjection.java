package cz.agents.dimap.psm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import cz.agents.dimap.fsm.Transition;
import cz.agents.dimap.psm.operator.Operator;
import cz.agents.dimap.psm.state.BitSetState;
import cz.agents.dimap.psm.state.BitSetStateFactory;
import cz.agents.dimap.psm.state.State;

public final class PublicProjection {

    public static void publicProjection(PlanStateMachine fsm, BitSetStateFactory bssFactory) {
    	computePrivateClosure(fsm, bssFactory);
    	
    	fsm.removeStatesNotGoingFromInit();

		mergeEquivalentStates(fsm, bssFactory);

		projectStates(fsm, bssFactory);
    }

    public static boolean isEpsilonTransition(Transition<State, Operator> trans, State state, BitSetStateFactory publicBss) {
    	if (!trans.getOperator().isPublic()) {
    		return true;
    	}
    	
    	// return true to keep all public actions
    	return publicBss.projectToPublic(state).equals(publicBss.projectToPublic(trans.getToState()));
    }
    
	public static void computePrivateClosure(PlanStateMachine fsm, BitSetStateFactory bssFactory) {
		EpsilonClosures<State> epsilonClosures = computeEpsilonClosures(fsm, bssFactory);
		removeEpsilonTransitions(fsm, bssFactory);

		for (State state : fsm.getStates()) {
			for (State middle : epsilonClosures.get(state)) {
				for (Transition<State, Operator> trans : fsm.getTransitions(middle)) {
					fsm.addTransition(state, trans.getOperator(), trans.getToState());
				}
			}
		}
	}
	
	private static EpsilonClosures<State> computeEpsilonClosures(PlanStateMachine fsm, BitSetStateFactory publicBss) {
		EpsilonClosures<State> closures = new EpsilonClosures<State>();
		
		for (State state : fsm.getStates()) {
			closures.put(state, computeStateClosure(fsm, state, publicBss));
		}
		
		return closures;
	}
	
	private static void removeEpsilonTransitions(PlanStateMachine fsm, BitSetStateFactory publicBss) {
		// remove private operators from fsm.transitions
		for (State state : fsm.getStates()) {
			Collection<Transition<State, Operator>> toRemove = new HashSet<Transition<State, Operator>>();
			for (Transition<State, Operator> trans : fsm.getTransitions(state)) {
				if (isEpsilonTransition(trans, state, publicBss)) {
					// remove all private actions and ...
					toRemove.add(trans);
				}
			}
			
			fsm.getTransitions(state).removeAll(toRemove);
		}

		// remove private operators from fsm.inverseTransitions
        for (State state : fsm.getStates()) {
			Collection<Transition<State, Operator>> toRemove = new HashSet<Transition<State, Operator>>();
			for (Transition<State, Operator> trans : fsm.getInverseTransitions(state)) {
				if (isEpsilonTransition(trans, state, publicBss)) {
					toRemove.add(trans);
				}
			}
			fsm.getInverseTransitions(state).removeAll(toRemove);
		}
	}
	
	private static Set<State> computeStateClosure(PlanStateMachine fsm, State startState, BitSetStateFactory publicBss) {
		Stack<State> open = new Stack<>();
		Set<State> closed = new HashSet<>();
		
		open.add(startState);
		
		while (!open.isEmpty()) {
			State fromState = open.pop();
			if (!closed.add(fromState)) {
				continue;
			}
			for (Transition<State, Operator> trans : fsm.getTransitions(fromState)) {
				if (!isEpsilonTransition(trans, fromState, publicBss)) {
					continue;
				}
				State toState = trans.getToState();
				if (!closed.contains(toState)) {
					open.push(toState);
				}
			}
		}
		
		return closed;
	}

	public static void mergeEquivalentStates(PlanStateMachine fsm, BitSetStateFactory bssFactory) {
		
		Collection<State> allStates = new ArrayList<>(fsm.getStates());
		
		for (State curState : allStates) {

	        Queue<State> states = new ArrayDeque<>();
	        states.add(curState);

			while (!states.isEmpty()) {

				State state = states.remove();
				
				if (!fsm.containsState(state) || state == fsm.getInitState()) {
					continue;
				}
				
				State pubState = null; // lazy initialization
				Collection<Transition<State, Operator>> stateTrans = fsm.getTransitions(state);

				for (State otherState : fsm.getStates()) {
					if (otherState.equals(state)) {
						continue;
					}
					if (stateTrans.equals(fsm.getTransitions(otherState))) {
						// states have equal outgoing edges
						State otherPubState = bssFactory.projectToPublic(otherState);
						if (pubState == null) {
							// initialize if not initialized yet
							pubState = bssFactory.projectToPublic(state);
						}
						if (otherPubState.getBitSet().equals(pubState.getBitSet())) {
							// states have equal public projections

							for (Transition<State, Operator> transition : fsm.getInverseTransitions(state)) {
								states.add(transition.getToState());
							}

							moveTransitions(fsm, state, otherState);
							
							fsm.removeState(state);
							
							break;
						} 
					}
				}
				
			}			
		}
    }

	private static void moveTransitions(PlanStateMachine fsm, State source, State target) {
		Collection<Transition<State, Operator>> inverseTrans = fsm.getInverseTransitions(source);
		
		for (Transition<State, Operator> transition : inverseTrans) {
			if (!transition.getToState().equals(source)) {
				fsm.getTransitions(transition.getToState()).remove(new Transition<State, Operator>(transition.getOperator(), source));
				fsm.addTransition(transition.getToState(), transition.getOperator(), target);
			}
		}
		
		for (Transition<State, Operator> transition : fsm.getTransitions(source)) {
			fsm.getInverseTransitions(transition.getToState()).remove(new Transition<State, Operator>(transition.getOperator(), source));
		}
	}

	public static void projectStates(PlanStateMachine fsm, BitSetStateFactory bssFactory) {
        Map<State, Integer> subStates = new HashMap<>();
        Map<State, State> statesMap = new HashMap<>();
        for (State state : fsm.getStates()) {
			BitSetState pubState = bssFactory.projectToPublic(state);
			pubState.setName(bssFactory.toString(pubState));
        	if (!subStates.containsKey(pubState)) {
        		subStates.put(pubState, 0);
        	}
        	int index = subStates.get(pubState);
			statesMap.put(state, new SubState(pubState, index));
			subStates.put(pubState, index + 1);
		}
        
        fsm.renameStates(statesMap);
	}

	public static class SubState implements State {
        private static final long serialVersionUID = -2014977483524395017L;

        private int subIndex;
		private State state;
		public SubState(State state, int subIndex) {
			this.state = state;
			this.subIndex = subIndex;
		}
		@Override
		public String getName() {
			return state.getName();
		}
		@Override
		public int hashCode() {
			return subIndex + 37*state.hashCode();
		}
	    @Override
	    public boolean equals(Object obj) {
			if (! (obj instanceof SubState)) {
				return false;
			}
			SubState other = (SubState) obj;
			return (subIndex == other.subIndex) && state.equals(other.state);
	    }
		@Override
		public String toString() {
			return state.toString()+"-"+subIndex;
		}
		@Override
		public BitSet getBitSet() {
			return state.getBitSet();
		}
	}
}
