package cz.agents.dimap.causal;

import cz.agents.dimap.tools.pddl.PddlAction;

public class FsmNodeAction implements FsmNode {
    public PddlAction action;
    private String suffix;
    FsmNodeAction(PddlAction action, String suffix) {
        this.action = action;
        this.suffix = suffix;
    }
    
    @Override
    public FsmNodeAction cloneWithSuffix(String newSuffix) {
        return new FsmNodeAction(PddlAction.createRenamedAction(action.name+"'"+suffix, action), newSuffix);
    }
    
    @Override
    public String toString() {
        return action.name + "'" + suffix + (action.isPublic() ? "\" color=\"red" : "");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((action == null) ? 0 : action.hashCode());
        result = prime * result
                + ((suffix == null) ? 0 : suffix.hashCode());
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
        FsmNodeAction other = (FsmNodeAction) obj;
        if (action == null) {
            if (other.action != null)
                return false;
        } else if (!action.equals(other.action))
            return false;
        if (suffix == null) {
            if (other.suffix != null)
                return false;
        } else if (!suffix.equals(other.suffix))
            return false;
        return true;
    }
}