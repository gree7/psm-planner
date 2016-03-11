package cz.agents.dimap.causal.operator;

import cz.agents.dimap.Settings;
import cz.agents.dimap.causal.EpsilonTransition;
import cz.agents.dimap.causal.FsmNode;
import cz.agents.dimap.causal.FsmNodeAction;
import cz.agents.dimap.fsm.FiniteStateMachine;
import cz.agents.dimap.fsm.Transition;

public class RemoveSmallActionCycles implements IdGraphOperator {

    @Override
    public boolean update(FiniteStateMachine<FsmNode, EpsilonTransition> fsm) {
        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeAction) {
                FsmNodeAction action = (FsmNodeAction) node;
                if (!action.action.isPublic() && fsm.getTransitions(node).size() == 1 && fsm.getInverseTransitions(node).size() == 1) {
                    Transition<FsmNode, EpsilonTransition> preTrans = fsm.getInverseTransitions(node).iterator().next();
                    Transition<FsmNode, EpsilonTransition> effTrans = fsm.getTransitions(node).iterator().next();
                    if (!preTrans.getOperator().isEpsilon()) {
                        FsmNode effNode = effTrans.getToState();
                        FsmNode preNode = preTrans.getToState();
                        FsmNode dualAction = getNodeBetween(fsm, effNode, preNode);
                        if (dualAction != null) {
                            fsm.removeState(action);
                            fsm.removeState(dualAction);
                            for (Transition<FsmNode, EpsilonTransition> trans : fsm.getTransitions(effNode)) {
                                fsm.addTransition(preNode, trans.getOperator(), trans.getToState());
                            }
                            for (Transition<FsmNode, EpsilonTransition> trans : fsm.getInverseTransitions(effNode)) {
                                fsm.addTransition(trans.getToState(), trans.getOperator(), preNode);
                            }
                            fsm.removeState(effNode);
                            if (LOGGING) {
                                System.out.println("small action cycle: " + preNode + " and " + effNode + "(" + action + " -- " + dualAction + " ) ");
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private FsmNode getNodeBetween(FiniteStateMachine<FsmNode, EpsilonTransition> fsm, FsmNode fromNode, FsmNode toNode) {
        FsmNode retVal = null;
        for (Transition<FsmNode, EpsilonTransition> fromTrans : fsm.getTransitions(fromNode)) {
            FsmNode candidateAction = fromTrans.getToState();
            if (fsm.getTransitions(candidateAction).size() == 1 && !fromTrans.getOperator().isEpsilon) {
                if (fsm.getTransitions(candidateAction).iterator().next().getToState().equals(toNode)) {
                    if (retVal == null) {
                        retVal = candidateAction;
                    } else {
                        return null;
                    }
                }
            }
        }
        return retVal;
    }

}
