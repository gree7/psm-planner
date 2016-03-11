package cz.agents.dimap.fsm;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

public class FiniteStateMachine<S, O> implements Cloneable, Serializable {
    private static final long serialVersionUID = 8916502928813106455L;

    final private Map<S, Collection<Transition<S, O>>> transitions;
	final private Map<S, Collection<Transition<S, O>>> inverseTransitions;
	
	private S initState;
	private Map<S, Collection<Transition<S, O>>>landmarkShortcuts = new LinkedHashMap<>();

	// copy constructor
	protected FiniteStateMachine(FiniteStateMachine<S, O> fsm) {
        initState = fsm.getInitState();
		transitions = new LinkedHashMap<S, Collection<Transition<S, O>>>();
		for (Entry<S, Collection<Transition<S, O>>> entry : fsm.transitions.entrySet()) {
			transitions.put(entry.getKey(), new LinkedHashSet<>(entry.getValue()));
		}
		inverseTransitions = new LinkedHashMap<S, Collection<Transition<S, O>>>();
		for (Entry<S, Collection<Transition<S, O>>> entry : fsm.inverseTransitions.entrySet()) {
			inverseTransitions.put(entry.getKey(), new LinkedHashSet<>(entry.getValue()));
		}
		landmarkShortcuts = new LinkedHashMap<>();
	}
	
    public FiniteStateMachine(S initState) {
		this.initState = initState;
		
		transitions = new HashMap<S, Collection<Transition<S, O>>>();
		transitions.put( initState, new HashSet<Transition<S, O>>() );
		inverseTransitions = new HashMap<S, Collection<Transition<S, O>>>();
		inverseTransitions.put( initState, new HashSet<Transition<S, O>>() );
		landmarkShortcuts = new LinkedHashMap<>();
	}
	
	public boolean addState(S state) {
		if (!transitions.containsKey(state)) {
			transitions.put( state, new HashSet<Transition<S, O>>() );
			inverseTransitions.put( state, new HashSet<Transition<S, O>>() );
			return true;
		} else {
			return false;
		}		
	}
	
	public boolean addTransition(S fromState, O operator, S toState) {
		return addTransition(fromState, operator, toState, false);
	}
	
	public boolean addTransition(S fromState, O operator, S toState, boolean addingShortcut) {
		Transition<S, O> transition = new Transition<S, O>(operator, toState);
		
		//throw NullPointerException if the from state is not present - it's ok
		Collection<Transition<S, O>> trans = transitions.get(fromState);
		if (!trans.contains(transition)) {
			if (addingShortcut) {
				addLandmarkShortcut(fromState, transition);
			}
			else {
				removeLandmarkShortcut(fromState, transition);
			}
			trans.add(transition);
			boolean newState = addState(toState);
			Transition<S, O> invTransition = new Transition<S, O>(operator, fromState);
			inverseTransitions.get(toState).add(invTransition);
			return newState;
		} else {
			return false;
		}
	}

    public Collection<S> getToStates(S state) {
        List<S> states = new ArrayList<>();
        for (Transition<S, O> transition : getTransitions().get(state)) {
            states.add(transition.getToState());
        }
        return states;
    }
    
    public Collection<S> getToStates(S state, O operator) {
        return getOtherStates(getTransitions().get(state), operator);
    }

    public Collection<S> getFromStates(S state, O operator) {
        return getOtherStates(getInverseTransitions().get(state), operator);
    }

    private Collection<S> getOtherStates(Collection<Transition<S, O>> transitions, O operator) {
        List<S> states = new ArrayList<>();
        for (Transition<S, O> transition : transitions) {
            if (transition.getOperator().equals(operator)) {
                states.add(transition.getToState());
            }
        }
        return states;
    }

	private void addLandmarkShortcut(S fromState, Transition<S, O> transition) {
		if (!landmarkShortcuts.containsKey(fromState)) {
			landmarkShortcuts.put(fromState, new HashSet<Transition<S, O>>());
		}
		landmarkShortcuts.get(fromState).add(transition);
	}

	public void removeLandmarkShortcut(S fromState, Transition<S, O> transition) {
		if (!landmarkShortcuts.containsKey(fromState)) {
			return;
		}
		landmarkShortcuts.get(fromState).remove(transition);
	}

	public boolean isLandmarkShortcut(S fromState, Transition<S, O> transition) {
		if (!landmarkShortcuts.containsKey(fromState)) {
			return false;
		}
		return landmarkShortcuts.get(fromState).contains(transition);
	}

	
    public S getInitState() {
        return initState;
    }

    public void removeState(S state) {
        Collection<Transition<S, O>> removed = transitions.remove(state);
        removeInverseTransitions(inverseTransitions, state, removed);
        removed = inverseTransitions.remove(state);        
        removeInverseTransitions(transitions, state, removed);
    }

    public void removeTransition(S fromState, O operator, S toState) {
        transitions.get(fromState).remove(new Transition<S, O>(operator, toState));
        inverseTransitions.get(toState).remove(new Transition<S, O>(operator, fromState));
    }

    private void removeInverseTransitions(Map<S, Collection<Transition<S, O>>> inverseTransitionMap, S fromState, Collection<Transition<S, O>> transitions) {
        for (Transition<S, O> transition : transitions) {
            Collection<Transition<S, O>> toRemove = new ArrayList<>();
            Collection<Transition<S, O>> inverseTrans = inverseTransitionMap.get(transition.getToState());
            for (Transition<S, O> inverseTransition : inverseTrans) {
                if (inverseTransition.getOperator().equals(transition.getOperator())
                        && inverseTransition.getToState().equals(fromState)) {
                    toRemove.add(inverseTransition);
                }
            }
            inverseTrans.removeAll(toRemove);
        }
    }

    public void removeStatesNotLeadingTo(Collection<S> goalStates) {
    	removeStatesNotGoingFromStates(goalStates, inverseTransitions, transitions);
    }

    public void removeStatesNotGoingFromInit() {
    	Collection<S> initS = new ArrayList<>(1);
    	initS.add(initState);
    	removeStatesNotGoingFromStates(initS, transitions, inverseTransitions);
    }

    private void removeStatesNotGoingFromStates(Collection<S> states,
			Map<S, Collection<Transition<S, O>>> edges,
			Map<S, Collection<Transition<S, O>>> inverseEdges) {

    	Set<S> okStates = new HashSet<S>();
		Queue<S> open = new ArrayDeque<S>( states );
		
		while (!open.isEmpty()) {
			S state = open.poll();
			if (!okStates.add(state)) {
				continue;
			}

			if (edges.containsKey(state)) {
				for (Transition<S, O> transition : edges.get(state)) {
					S otherState = transition.getToState();
					if (!okStates.contains(otherState)) {
						open.add( otherState );
					}
				}
			}
		}
		
		Set<S> toRemove = new HashSet<S>( inverseEdges.keySet() );
		toRemove.removeAll( okStates );
		for (S removeState : toRemove) {
			// remove transitions to removed state
			for (Transition<S, O> transition : edges.get( removeState )) {
				S otherState = transition.getToState();
				Collection<Transition<S, O>> fromTrans = inverseEdges.get( otherState );
				if (fromTrans != null) {
					fromTrans.remove(new Transition<S, O>(transition.getOperator(), removeState));
				}
			}

			inverseEdges.remove(removeState);
			edges.remove(removeState);
		}
	}
    
    Map<S, Collection<Transition<S, O>>> getTransitions() {
        return transitions;
    }

    public Collection<Transition<S, O>> getTransitions(S state) {
        return transitions.get(state);
    }

    public void renameTransitions(Map<O, O> operatorMap) {
        renameTransitionSet(transitions, operatorMap);
        renameTransitionSet(inverseTransitions, operatorMap);
    }

    private void renameTransitionSet(Map<S, Collection<Transition<S, O>>> trans, Map<O, O> operatorMap) {
        Map<S, Collection<Transition<S, O>>> newTransitions = new HashMap<S, Collection<Transition<S, O>>>();
        for (Entry<S, Collection<Transition<S, O>>> entry : trans.entrySet()) {
            Collection<Transition<S, O>> newTrans = new HashSet<>();
            for (Transition<S, O> transition : entry.getValue()) {
                newTrans.add(new Transition<S, O>(operatorMap.get(transition.getOperator()), transition.getToState()));
            }
            newTransitions.put(entry.getKey(), newTrans);
        }
        trans.clear();
        trans.putAll(newTransitions);
    }
    
    Map<S, Collection<Transition<S, O>>> getInverseTransitions() {
        return inverseTransitions;
    }

    public Collection<Transition<S, O>> getInverseTransitions(S state) {
        return inverseTransitions.get(state);
    }

    public void renameStates(Map<S, S> statesMap) {
    	renameSet(transitions, statesMap);
    	renameSet(inverseTransitions, statesMap);
    	if (statesMap.containsKey(initState)) {
    		initState = statesMap.get(initState);
    	}
    }

	private void renameSet(Map<S, Collection<Transition<S, O>>> trans, Map<S, S> statesMap) {
		Map<S, Collection<Transition<S, O>>> newTransitions = new HashMap<S, Collection<Transition<S, O>>>();
		for (Entry<S, Collection<Transition<S, O>>> entry : trans.entrySet()) {
			Collection<Transition<S, O>> newTrans = new HashSet<>();
    		for (Transition<S, O> transition : entry.getValue()) {
				newTrans.add(new Transition<S, O>(transition.getOperator(), statesMap.get(transition.getToState())));
			}
    		newTransitions.put(statesMap.get(entry.getKey()), newTrans);
		}
		trans.clear();
		trans.putAll(newTransitions);
	}

	@Override
	public String toString() {
		return "states: " + transitions.size() +", transitions:"+getNumberOfTransitions()+" init:" + initState;
	}

    public int getNumberOfTransitions() {
        int transSize = 0;
        for (Collection<Transition<S, O>> trans : transitions.values()) {
            transSize += trans.size();
        }
        return transSize;
    }

    public int getNumberOfStates() {
        return transitions.size();
    }

    public Collection<S> getStates() {
        return transitions.keySet();
    }

	public boolean containsState(S state) {
		return transitions.containsKey(state);
	}
	
	@Override
	public FiniteStateMachine<S, O> clone() {
		return new FiniteStateMachine<S, O>(this);
	}

    public void checkConsistency() {
        if (getInverseTransitions().size() != getTransitions().size()) {
            throw new Error("!!!");
        }
        
        for (Entry<S, Collection<Transition<S, O>>> entry : getTransitions().entrySet()) {
            for (Transition<S, O> transition : entry.getValue()) {
                if (!getFromStates(transition.getToState(), transition.getOperator()).contains( entry.getKey() )) {
                    System.out.println("transition: " + transition);
                    System.out.println("fsm.getTransitions(): " + getTransitions());
                    System.out.println("fsm.getInverseTransitions(): " + getInverseTransitions());
                    throw new Error("!!!");
                }
            }
        }

        for (Entry<S, Collection<Transition<S, O>>> entry : getInverseTransitions().entrySet()) {
            for (Transition<S, O> transition : entry.getValue()) {
                if (!getToStates(transition.getToState(), transition.getOperator()).contains( entry.getKey() )) {
                    System.out.println("transition: " + transition);
                    System.out.println("fsm.getTransitions(): " + getTransitions());
                    System.out.println("fsm.getInverseTransitions(): " + getInverseTransitions());
                    throw new Error("!!!");
                }
            }
        }
    }

    public boolean isEmpty() {
        return transitions.isEmpty();
    }

    public Collection<Transition<S, O>> getTransitionsBetween(S state1, S state2) {
        Collection<Transition<S, O>> retTrans = new ArrayList<>();
        for (Transition<S, O> trans : transitions.get(state1)) {
            if (trans.getToState().equals(state2)) {
                retTrans.add(trans);
            }
        }
        return retTrans;
    }
}
