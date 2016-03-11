package cz.agents.dimap.causal.operator;

import cz.agents.dimap.causal.EpsilonTransition;
import cz.agents.dimap.causal.FsmNode;
import cz.agents.dimap.causal.FsmNodeTerm;
import cz.agents.dimap.fsm.FiniteStateMachine;
import cz.agents.dimap.fsm.Transition;

public class RemoveAlwaysValidTerms implements IdGraphOperator {

    @Override
    public boolean update(FiniteStateMachine<FsmNode, EpsilonTransition> fsm) {
        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeTerm) {
                if (isAlwaysValid(fsm, node)) {
                    fsm.removeState(node);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAlwaysValid(FiniteStateMachine<FsmNode, EpsilonTransition> fsm, FsmNode node) {
        // has 1 incoming edge
        if (fsm.getInverseTransitions(node).size() != 1) {
            return false;
        }
        // ...  and it is from init
        if (!fsm.getInitState().equals(fsm.getInverseTransitions(node).iterator().next().getToState())) {
            return false;
        }
        // ... and this fact is never deleted  (unless goal)
        for (Transition<FsmNode, EpsilonTransition> trans : fsm.getTransitions(node)) {
            if (!trans.getOperator().isEpsilon() && !trans.getToState().toString().contains("GOAL")) {
                return false;
            }
        }
        return true;
    }

}
