package cz.agents.dimap.causal.operator;

import cz.agents.dimap.causal.EpsilonTransition;
import cz.agents.dimap.causal.FsmNode;
import cz.agents.dimap.causal.FsmNodeAction;
import cz.agents.dimap.causal.FsmNodeTerm;
import cz.agents.dimap.fsm.FiniteStateMachine;
import cz.agents.dimap.fsm.Transition;

public class RemoveSimpleFactDependencies implements IdGraphOperator {

    @Override
    public boolean update(FiniteStateMachine<FsmNode, EpsilonTransition> fsm) {
        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeTerm) {
                if (fsm.getInverseTransitions(node).size() == 1
                        && fsm.getTransitions(node).size() == 1) {
                    FsmNodeAction preAction = (FsmNodeAction) fsm.getInverseTransitions(node).iterator().next().getToState();
                    Transition<FsmNode, EpsilonTransition> trans = fsm.getTransitions(node).iterator().next();
                    if (!trans.getOperator().isEpsilon) {
                        FsmNodeAction postAction = (FsmNodeAction) trans.getToState();
                        if (fsm.getTransitions(preAction).size() == 1 ) {
                            if (mergeNodes(fsm, preAction, (FsmNodeTerm) node, postAction)) {
//                                System.out.println("Simplified term: " + node);
                                return true;
                            }
                        }
                    }
                } 
            }
        }
        return false;
    }

    private boolean mergeNodes(FiniteStateMachine<FsmNode, EpsilonTransition> fsm, FsmNodeAction preAction, FsmNodeTerm term, FsmNodeAction postAction) {
        for (Transition<FsmNode, EpsilonTransition> trans : fsm.getTransitions(postAction)) {
            for (Transition<FsmNode, EpsilonTransition> trans2 : fsm.getTransitions(trans.getToState())) {
                if (trans2.getToState().equals(preAction)) {
                    // do not allow small cycles
                    return false;
                }
            }
        }
        if (preAction.action.isPublic()) {
            if (!postAction.action.isPublic() && fsm.getInverseTransitions(postAction).size() == 1) {
                for (Transition<FsmNode, EpsilonTransition> trans : fsm.getTransitions(postAction)) {
                    fsm.addTransition(preAction, trans.getOperator(), trans.getToState());
                }
                fsm.removeState(term);
                fsm.removeState(postAction);
                return true;
            }
            return false;
        } else {
            if (!postAction.action.isPublic() || fsm.getTransitions(preAction).size() == 1) {
                for (Transition<FsmNode, EpsilonTransition> trans : fsm.getInverseTransitions(preAction)) {
                    fsm.addTransition(trans.getToState(), trans.getOperator(), postAction);
                }
                for (Transition<FsmNode, EpsilonTransition> trans : fsm.getTransitions(preAction)) {
                    fsm.addTransition(postAction, trans.getOperator(), trans.getToState());
                }
                fsm.removeState(term);
                fsm.removeState(preAction);
                return true;
            }
            return false;
        }
    }
}
