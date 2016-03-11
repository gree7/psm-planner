package cz.agents.dimap.causal.operator;

import cz.agents.dimap.causal.EpsilonTransition;
import cz.agents.dimap.causal.FsmNode;
import cz.agents.dimap.fsm.FiniteStateMachine;

public class RemoveStatesNotGoingFromInit implements IdGraphOperator {

    @Override
    public boolean update(FiniteStateMachine<FsmNode, EpsilonTransition> fsm) {
        int states = fsm.getNumberOfStates();
        fsm.removeStatesNotGoingFromInit();
        return states != fsm.getNumberOfStates();
    }

}
