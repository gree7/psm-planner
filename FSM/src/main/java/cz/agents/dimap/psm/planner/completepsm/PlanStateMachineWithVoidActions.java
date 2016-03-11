package cz.agents.dimap.psm.planner.completepsm;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import cz.agents.dimap.psm.PlanStateMachine;
import cz.agents.dimap.psm.operator.Operator;
import cz.agents.dimap.psm.state.State;

public class PlanStateMachineWithVoidActions extends PlanStateMachine {
    private static final long serialVersionUID = 2933257895511832073L;

    public Collection<String> voidActions = new HashSet<>();
    public Collection<String> nonVoidActions = new HashSet<>();
    
    Map<State, Collection<Operator>> stateVoidActions = new HashMap<>();
    
    public PlanStateMachineWithVoidActions(State initState) {
        super(initState);
    }
}
