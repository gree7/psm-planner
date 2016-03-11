package cz.agents.dimap.psm.operator;

import java.util.Collection;
import java.util.Set;

import cz.agents.dimap.psm.state.State;
import cz.agents.dimap.tools.pddl.PddlAction;

public class ConcurrentOperator implements Operator {

    Collection<Operator> operators;
    
    public ConcurrentOperator(Collection<Operator> operators) {
        this.operators = operators;
    }

    @Override
    public String getName() {
        StringBuilder sb = new StringBuilder("@[");
        boolean first = true;
        for (Operator operator : operators) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(operator.getName());
            first = false;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public String getPublicName() {
        StringBuilder sb = new StringBuilder("@[");
        boolean first = true;
        for (Operator operator : operators) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(operator.getPublicName());
            first = false;
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public PddlAction getAction() {
        throw new UnsupportedOperationException();
    }

    @Override
    public State tryToApply(State state) {
        Operator[] copy = operators.toArray(new Operator[0]);
        return tryToApply(state, copy);
    }

    private State tryToApply(State state, Operator[] ops) {
        for (int i=0; i<ops.length; i++) {
            Operator operator = ops[i];
            State newState = operator.tryToApply(state);
            if (newState != null) {
                ops[i] = null;
                State finalState = tryToApply(newState, ops);
                ops[i] = operator;
                if (finalState != null) {
                    return finalState;
                }
            }
        }
        return null;
    }

    @Override
    public boolean isPublic() {
        for (Operator operator : operators) {
            if (operator.isPublic()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<? extends Object> getPreconditions() {
        throw new UnsupportedOperationException("Use grounded operator!");
    }

    @Override
    public Set<? extends Object> getEffects() {
        throw new UnsupportedOperationException("Use grounded operator!");
    }

}
