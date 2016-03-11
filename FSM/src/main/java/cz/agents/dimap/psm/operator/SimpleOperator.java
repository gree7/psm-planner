package cz.agents.dimap.psm.operator;

import java.util.Set;

import cz.agents.dimap.psm.PlanStateMachineTools;
import cz.agents.dimap.psm.state.State;
import cz.agents.dimap.tools.pddl.PddlAction;

/**
 * Created with IntelliJ IDEA.
 * User: durkokar
 * Date: 1/13/14
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleOperator implements Operator {

    private final String name;
    private final boolean isPublic;

    public SimpleOperator(String name) {
        this.name = name;
    	this.isPublic = true;
    }
    
    public SimpleOperator(String name, boolean isPublic) {
        this.name = name;
        this.isPublic = isPublic;
    }

    @Override
    public String getName() {
        return name;
    }

	@Override
	public String getPublicName() {
		return PlanStateMachineTools.stripActionPrefix(name);
	}

    @Override
    public State tryToApply(State state) {
        return null;
    }
    
    @Override
    public int hashCode() {
    	return name.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
		if (! (obj instanceof SimpleOperator)) {
			return false;
		}
		SimpleOperator op = (SimpleOperator) obj;
		return name.equals(op.name);
    }
    
    @Override
    public String toString() {
    	return getName();
    }

	@Override
	public boolean isPublic() {
		return this.isPublic;
	}

    @Override
    public Set<? extends Object> getPreconditions() {
        return null;
    }

    @Override
    public Set<? extends Object> getEffects() {
        return null;
    }
    
    @Override
    public PddlAction getAction() {
        return null;
    }

}
