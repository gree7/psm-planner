package cz.agents.dimap.psm.operator;

import java.util.HashSet;
import java.util.Set;

import cz.agents.dimap.psm.PlanStateMachineTools;
import cz.agents.dimap.psm.state.BitSetState;
import cz.agents.dimap.psm.state.BitSetStateFactoryPddl;
import cz.agents.dimap.psm.state.MultiState;
import cz.agents.dimap.psm.state.State;
import cz.agents.dimap.tools.pddl.PddlAction;

class PddlOperatorGrounded implements Operator {

	private final PddlAction action;
	private final MultiState precondition;
	private final MultiState effects;
	private final boolean isPublic;
	
	PddlOperatorGrounded(PddlAction action, BitSetStateFactoryPddl stateFactory) {
		this.action = action;
		this.precondition = stateFactory.createMultiState(action.precondition);
		this.effects = stateFactory.createMultiState(action.effect);
		this.isPublic = !stateFactory.projectToPublic(effects).getBitStateMask().getBitSet().isEmpty();
	}
	
	private boolean isApplicable(State state) {
		return precondition.contains(state);
	}

	@Override
	public State tryToApply(State state) {
	    if (isApplicable(state)) {
	        return effects.apply(state);
	    } else {
	        return null;
	    }
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
        PddlOperatorGrounded other = (PddlOperatorGrounded) obj;
        if (action == null) {
            if (other.action != null)
                return false;
        } else if (!action.equals(other.action))
            return false;
        return true;
    }

	@Override
	public boolean isPublic() {
		return isPublic;
	}

    @Override
    public Set<Integer> getPreconditions() {
        return bitStateMaskToSet(precondition.getBitStateMask());
    }

    @Override
    public Set<Integer> getEffects() {
        return bitStateMaskToSet(effects.getBitStateMask());
    }

    private Set<Integer> bitStateMaskToSet(BitSetState bitSetState) {
        Set<Integer> set = new HashSet<>();
        int lastIndex = 0;
        while (true) {
            int index = bitSetState.getBitSet().nextSetBit(lastIndex);
            if (index == -1) {
                break;
            } else {
                set.add(index);
            }
            lastIndex = index + 1;
        }
        return set;
    }

    @Override
    public PddlAction getAction() {
        return action;
    }
}
