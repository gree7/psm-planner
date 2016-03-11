package cz.agents.dimap.psm.state;

import java.util.BitSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.agents.dimap.fsm.Transition;
import cz.agents.dimap.psm.PlanStateMachine;
import cz.agents.dimap.psm.operator.Operator;

/**
 * Created with IntelliJ IDEA.
 * User: durkokar
 * Date: 1/13/14
 * Time: 10:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class IntersectionState implements State {
    private static final long serialVersionUID = 3451156106505861422L;

    private State parent1, parent2;
    private final String publicName;
    private String subPublicName;
    private PlanStateMachine fsm1, fsm2;
    private boolean expanded = false;

    public IntersectionState(State s1, State s2, PlanStateMachine f1, PlanStateMachine f2, int name1, int name2) {
        parent1 = s1;
        parent2 = s2;
        publicName = s1.getName();
//        subPublicName = s1.getSubPublicName() + "_" + s2.getSubPublicName();
        subPublicName = name1+"_"+name2;
        fsm1 = f1;
        fsm2 = f2;
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

        for ( Transition<State, Operator> t1 : fsm1.getTransitions(parent1) ) {
            for ( Transition<State, Operator> t2 : fsm2.getTransitions(parent2) ) {
                if ( t1.getOperator().getPublicName().equals(t2.getOperator().getPublicName()) ) {
                    State toState1 = t1.getToState();
                    State toState2 = t2.getToState();
                    if ( toState1.getName().equals(toState2.getName()) ) {
                        IntersectionState is = new IntersectionState(toState1, toState2, fsm1, fsm2, toState1.hashCode(), toState2.hashCode());
                        // TODO: here I add operator from t1(since I'm interested only in the name of the operator, doesnt matter if I add t1's or t2's)
                        Transition<State, Operator> t = new Transition<State, Operator>(t1.getOperator(), is);
                        trans.add(t);
                    }
                }
            }
        }
        return trans;
    }


    public Collection<Transition<State, Operator>> expandState(Map<State, Integer> fsm1Index, Map<State, Integer> fsm2Index) {
        Set<Transition<State, Operator>> trans = new HashSet<Transition<State, Operator>>();

        for ( Transition<State, Operator> t1 : this.fsm1.getTransitions(parent1) ) {
            for ( Transition<State, Operator> t2 : this.fsm2.getTransitions(parent2) ) {
                if ( t1.getOperator().getPublicName().equals(t2.getOperator().getPublicName()) ) {
                    State toState1 = t1.getToState();
                    State toState2 = t2.getToState();
                    if ( toState1.getName().equals(toState2.getName()) ) {
                        IntersectionState is = new IntersectionState(toState1, toState2, fsm1, fsm2, fsm1Index.get(toState1), fsm2Index.get(toState2));
                        // TODO: here I add operator from t1(since I'm interested only in the name of the operator, doesnt matter if I add t1's or t2's)
                        Transition<State, Operator> t = new Transition<State, Operator>(t1.getOperator(), is);
                        trans.add(t);
                    }
                }
            }
        }
        return trans;
    }

    public Collection<Transition<State, Operator>> expandState(List<State> name1, List<State> name2) {
        Set<Transition<State, Operator>> trans = new HashSet<Transition<State, Operator>>();

        for ( Transition<State, Operator> t1 : fsm1.getTransitions(parent1) ) {
            for ( Transition<State, Operator> t2 : fsm2.getTransitions(parent2) ) {
                if ( t1.getOperator().getPublicName().equals(t2.getOperator().getPublicName()) ) {
                    State toState1 = t1.getToState();
                    State toState2 = t2.getToState();
                    if ( toState1.getName().equals(toState2.getName()) ) {
                        IntersectionState is = new IntersectionState(toState1, toState2, fsm1, fsm2, name1.indexOf(toState1), name2.indexOf(toState2));
                        // TODO: here I add operator from t1(since I'm interested only in the name of the operator, doesnt matter if I add t1's or t2's)
                        Transition<State, Operator> t = new Transition<State, Operator>(t1.getOperator(), is);
                        trans.add(t);
                    }
                }
            }
        }
        return trans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntersectionState that = (IntersectionState) o;

        if (publicName != null ? !publicName.equals(that.publicName) : that.publicName != null) return false;
        if (subPublicName != null ? !subPublicName.equals(that.subPublicName) : that.subPublicName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = publicName != null ? publicName.hashCode() : 0;
        result = 31 * result + (subPublicName != null ? subPublicName.hashCode() : 0);
        return result;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public String getName() {
        return publicName;
    }

	@Override
	public BitSet getBitSet() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
