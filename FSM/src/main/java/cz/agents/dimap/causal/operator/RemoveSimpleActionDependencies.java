package cz.agents.dimap.causal.operator;

import java.util.Collection;

import cz.agents.dimap.causal.EpsilonTransition;
import cz.agents.dimap.causal.FsmNode;
import cz.agents.dimap.causal.FsmNodeAction;
import cz.agents.dimap.causal.FsmNodeTerm;
import cz.agents.dimap.fsm.FiniteStateMachine;
import cz.agents.dimap.fsm.Transition;

public class RemoveSimpleActionDependencies implements IdGraphOperator {

    @Override
    public boolean update(FiniteStateMachine<FsmNode, EpsilonTransition> fsm) {
        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeAction) {
                if (fsm.getInverseTransitions(node).size() == 1
                        && fsm.getTransitions(node).size() == 1) {
                    Transition<FsmNode, EpsilonTransition> trans = fsm.getInverseTransitions(node).iterator().next();
                    FsmNodeTerm preTerm = (FsmNodeTerm) trans.getToState();
                    FsmNodeTerm postTerm = (FsmNodeTerm) fsm.getTransitions(node).iterator().next().getToState();
                    if (!trans.getOperator().isEpsilon) {
                        if (fsm.getTransitions(preTerm).size() == 1 ) {
                            mergeNodes(fsm, preTerm, (FsmNodeAction) node, postTerm);
//                            System.out.println("Simplified action: " + node);
                            return true;
                        }
                    }
                } 
            }
        }
        return false;
    }

    private void mergeNodes(FiniteStateMachine<FsmNode, EpsilonTransition> fsm, FsmNodeTerm preTerm, FsmNodeAction action, FsmNodeTerm postTerm) {
        for (Transition<FsmNode, EpsilonTransition> trans : fsm.getInverseTransitions(preTerm)) {
            FsmNode preAction = trans.getToState();
            Collection<Transition<FsmNode, EpsilonTransition>> transistions = fsm.getTransitionsBetween(postTerm, preAction);
            if (transistions.isEmpty()) {
                fsm.addTransition(preAction, trans.getOperator(), postTerm);
            } else {
                if (transistions.size() > 1) {
                    throw new UnsupportedOperationException("unexpected");
                }
                Transition<FsmNode, EpsilonTransition> trans1 = transistions.iterator().next();
                trans1.getOperator().isEpsilon = true;
            }
        }
        fsm.removeState(preTerm);
        fsm.removeState(action);
    }

}
