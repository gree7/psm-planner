package cz.agents.dimap.causal.operator;

import cz.agents.dimap.causal.EpsilonTransition;
import cz.agents.dimap.causal.FsmNode;
import cz.agents.dimap.fsm.FiniteStateMachine;

public interface IdGraphOperator {
    
    static final boolean LOGGING = false;
    
    boolean update(FiniteStateMachine<FsmNode, EpsilonTransition> fsm);
}
