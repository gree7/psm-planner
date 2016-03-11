package cz.agents.dimap.fsm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class EpsilonClosure {
    
    public static class EpsilonClosures<S, O extends EpsilonOperator> extends HashMap<S, Set<Transition<S, O>>> {
        private static final long serialVersionUID = 1L;
    }
    
    public static <S, O extends EpsilonOperator> void computeEpsilonClosure(FiniteStateMachine<S, O> fsm) {
        computeEpsilonClosure(fsm, false);
    }

    public static <S, O extends EpsilonOperator> void computeEpsilonClosure(FiniteStateMachine<S, O> fsm, boolean keepEpsilonTranstionNames ) {
        EpsilonClosures<S, O> epsilonClosures = computeEpsilonClosures(fsm);
        removeEpsilonTransitions(fsm);

        for (S state : fsm.getStates()) {
            for (Transition<S, O> middle : epsilonClosures.get(state)) {
                for (Transition<S, O> trans : fsm.getTransitions(middle.getToState())) {
                    fsm.addTransition(state, trans.getOperator(), trans.getToState());
                }
            }
            Collection<Transition<S, O>> transitions = new ArrayList<>(fsm.getTransitions(state));
            for (Transition<S, O> trans : transitions) {
                for (Transition<S, O> epsilonTransition : epsilonClosures.get(trans.getToState())) {
                    if (!epsilonTransition.getToState().equals(state)) {
                        if (keepEpsilonTranstionNames) {
                            O operator = epsilonTransition.getOperator();
                            operator.setIsEpsilon(false);
                            fsm.addTransition(state, operator, epsilonTransition.getToState());
                        } else {
                            fsm.addTransition(state, trans.getOperator(), epsilonTransition.getToState());
                        }
                    }
                }
            }
        }
    }
    
    private static <S, O extends EpsilonOperator> EpsilonClosures<S, O> computeEpsilonClosures(FiniteStateMachine<S, O> fsm) {
        EpsilonClosures<S, O> closures = new EpsilonClosures<>();
        
        for (S state : fsm.getStates()) {
            closures.put(state, computeStateClosure(fsm, state));
        }
        
        return closures;
    }
    
    private static <S, O extends EpsilonOperator> void removeEpsilonTransitions(FiniteStateMachine<S, O> fsm) {
        // remove private operators from fsm.transitions
        for (S state : fsm.getStates()) {
            Collection<Transition<S, O>> toRemove = new HashSet<>();
            for (Transition<S, O> trans : fsm.getTransitions(state)) {
                if (trans.getOperator().isEpsilon()) {
                    // remove all private actions and ...
                    toRemove.add(trans);
                }
            }
            
            fsm.getTransitions(state).removeAll(toRemove);
        }

        // remove private operators from fsm.inverseTransitions
        for (S state : fsm.getStates()) {
            Collection<Transition<S, O>> toRemove = new HashSet<>();
            for (Transition<S, O> trans : fsm.getInverseTransitions(state)) {
                if (trans.getOperator().isEpsilon()) {
                    toRemove.add(trans);
                }
            }
            fsm.getInverseTransitions(state).removeAll(toRemove);
        }
    }
    
    private static <S, O extends EpsilonOperator> Set<Transition<S, O>> computeStateClosure(FiniteStateMachine<S, O> fsm, S startState) {
        Stack<Transition<S, O>> open = new Stack<>();
        Set<Transition<S, O>> closed = new HashSet<>();
        
        open.add(new Transition<S, O>(null, startState));
        
        while (!open.isEmpty()) {
            Transition<S, O> fromState = open.pop();
            if (fromState.getOperator()!=null && !closed.add(fromState)) {
                continue;
            }
            for (Transition<S, O> trans : fsm.getTransitions(fromState.getToState())) {
                if (!trans.getOperator().isEpsilon()) {
                    continue;
                }
                if (!closed.contains(trans)) {
                    open.push(trans);
                }
            }
        }
        
        return closed;
    }

}
