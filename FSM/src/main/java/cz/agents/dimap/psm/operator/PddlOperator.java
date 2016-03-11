package cz.agents.dimap.psm.operator;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import cz.agents.dimap.psm.PlanStateMachineTools;
import cz.agents.dimap.psm.state.State;
import cz.agents.dimap.tools.pddl.PddlAction;

class PddlOperator implements Operator, Serializable {
    private static final long serialVersionUID = 4409829675094527135L;
    private final PddlAction action;

    PddlOperator(PddlAction action) {
        this.action = action;
    }

    Collection<Operator> groundInState(@SuppressWarnings("unused") State state) {
        throw new UnsupportedOperationException("Not implemented yet!!!");
    }

    @Override
    public State tryToApply(State state) {
        throw new UnsupportedOperationException("Use grounded operator!");
    }

    @Override
    public String getName() {
        return action.getName();
    }

    @Override
    public String getPublicName() {
        return PlanStateMachineTools.stripActionPrefix(action.getName());
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((action == null) ? 0 : action.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PddlOperator other = (PddlOperator) obj;
        if (action == null) {
            if (other.action != null)
                return false;
        } else if (!action.equals(other.action))
            return false;
        return true;
    }

    @Override
    public boolean isPublic() {
        return action.isPublic();
    }

    @Override
    public Set<Integer> getPreconditions() {
        throw new UnsupportedOperationException("Use grounded operator!");
    }

    @Override
    public Set<Integer> getEffects() {
        throw new UnsupportedOperationException("Use grounded operator!");
    }

    @Override
    public PddlAction getAction() {
        return action;
    }
}
