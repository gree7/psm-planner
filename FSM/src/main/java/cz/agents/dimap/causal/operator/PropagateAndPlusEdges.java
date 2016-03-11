package cz.agents.dimap.causal.operator;

import java.util.ArrayList;
import java.util.List;

import cz.agents.dimap.causal.EpsilonTransition;
import cz.agents.dimap.causal.FsmNode;
import cz.agents.dimap.causal.FsmNodeAction;
import cz.agents.dimap.fsm.FiniteStateMachine;
import cz.agents.dimap.fsm.Transition;

public class PropagateAndPlusEdges implements IdGraphOperator {

    @Override
    public boolean update(FiniteStateMachine<FsmNode, EpsilonTransition> fsm) {
        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeAction) {
                FsmNodeAction actionNode = (FsmNodeAction)node;
                if (actionNode.action.isInternal() && allAndPlusPreconditions(fsm, actionNode)) {
                    List<FsmNode> toRemove = new ArrayList<>();
                    toRemove.add(actionNode);
                    for (Transition<FsmNode, EpsilonTransition> trans : fsm.getTransitions(actionNode)) {
                        FsmNode effect = trans.getToState();
                        if (fsm.getInverseTransitions(effect).size() == 1) {
                            toRemove.add(effect);
                        }
                        for (Transition<FsmNode, EpsilonTransition> trans2 : fsm.getTransitions(effect)) {
                            for (Transition<FsmNode, EpsilonTransition> preTrans : fsm.getInverseTransitions(actionNode)) {
                                fsm.addTransition(preTrans.getToState(), preTrans.getOperator(), trans2.getToState());
                            }
                        }
                    }
                    for (FsmNode remNode : toRemove) {
                        fsm.removeState(remNode);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean allAndPlusPreconditions(FiniteStateMachine<FsmNode, EpsilonTransition> fsm, FsmNodeAction actionNode) {
        for (Transition<FsmNode, EpsilonTransition> trans : fsm.getInverseTransitions(actionNode)) {
            if (!trans.getOperator().isEpsilon) {
                return false;
            }
        }
        return true;
    }

}
