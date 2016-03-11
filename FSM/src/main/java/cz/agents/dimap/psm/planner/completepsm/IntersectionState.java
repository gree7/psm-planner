package cz.agents.dimap.psm.planner.completepsm;

import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.agents.dimap.Settings;
import cz.agents.dimap.fsm.Transition;
import cz.agents.dimap.psm.operator.Operator;
import cz.agents.dimap.psm.state.State;
import cz.agents.dimap.tools.Pair;

class IntersectionState implements State {
    private static final long serialVersionUID = 3451156106505861422L;

    State parent1, parent2;
    PlanStateMachineWithVoidActions fsm1, fsm2;

    static int globalId = 0;

    final int id;
    
    static private Map<Pair<State, State>, IntersectionState> states = new HashMap<>();
    
    public static IntersectionState create(State s1, State s2, PlanStateMachineWithVoidActions f1, PlanStateMachineWithVoidActions f2) {
        IntersectionState state = states.get(new Pair<State, State>(s1, s2));
        if (state == null) {
            state = new IntersectionState(s1, s2, f1, f2);
            states.put(new Pair<State, State>(s1, s2), state);
        }
        return state;
    }

    public static void clearCreatedStates() {
        states.clear();
    }
    
    private IntersectionState(State s1, State s2, PlanStateMachineWithVoidActions f1, PlanStateMachineWithVoidActions f2) {
        parent1 = s1;
        parent2 = s2;
        fsm1 = f1;
        fsm2 = f2;
        id = globalId++;
    }

    public State getParent2() {
        return parent2;
    }

    public void setParent2(State parent2) {
        this.parent2 = parent2;
    }

    public State getParent1() {
        return parent1;
    }

    public void setParent1(State parent1) {
        this.parent1 = parent1;
    }

    public Collection<Transition<State, Operator>> expandState() {
        Set<Transition<State, Operator>> trans = new HashSet<Transition<State, Operator>>();

        for ( Transition<State, Operator> t1 : this.fsm1.getTransitions(parent1) ) {
            State toState1 = t1.getToState();
            Operator operator = t1.getOperator();
            for ( Transition<State, Operator> t2 : this.fsm2.getTransitions(parent2) ) {
                if ( operator.getPublicName().equals(t2.getOperator().getPublicName()) ) {
                    State toState2 = t2.getToState();
                    addToTrans(trans, toState1, operator, toState2);
                }
            }
        }

        return trans;
    }

    private void addToTrans(Set<Transition<State, Operator>> trans,
            State toState1, Operator operator, State toState2) {
        IntersectionState is = IntersectionState.create(toState1, toState2, fsm1, fsm2);
        Transition<State, Operator> t = new Transition<State, Operator>(operator, is);
        trans.add(t);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        IntersectionState other = (IntersectionState) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BitSet getBitSet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        if (Settings.DOT_CAUSAL_GRAPH) {
            return parent1 + "|" + parent2;
        } else {
            return Integer.toString(id);
        }
    }
}