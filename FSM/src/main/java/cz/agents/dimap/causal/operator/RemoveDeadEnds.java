package cz.agents.dimap.causal.operator;

import cz.agents.dimap.causal.EpsilonTransition;
import cz.agents.dimap.causal.FsmNode;
import cz.agents.dimap.causal.FsmNodeAction;
import cz.agents.dimap.fsm.FiniteStateMachine;

public class RemoveDeadEnds implements IdGraphOperator {

    @Override
    public boolean update(FiniteStateMachine<FsmNode, EpsilonTransition> fsm) {
        for (FsmNode node : fsm.getStates()) {
            if (! (node instanceof FsmNodeAction) || !(((FsmNodeAction)node).action.isPublic()) ) {
                if (fsm.getTransitions(node).isEmpty()) {
                    fsm.removeState(node);
                    return true;
                }
            }
        }
        return false;
    }

}
