package cz.agents.dimap.causal.operator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import cz.agents.dimap.causal.EpsilonTransition;
import cz.agents.dimap.causal.FsmNode;
import cz.agents.dimap.causal.FsmNodeAction;
import cz.agents.dimap.causal.FsmNodeTerm;
import cz.agents.dimap.fsm.FiniteStateMachine;
import cz.agents.dimap.fsm.Transition;
import cz.agents.dimap.tools.Pair;

public class BackpropagateConsequences implements IdGraphOperator {

    @Override
    public boolean update(FiniteStateMachine<FsmNode, EpsilonTransition> fsm) {
        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeTerm) {
                Pair<FsmNode, Collection<FsmNode>> consequences = getGoodConsequences(fsm, (FsmNodeTerm)node);
                if (consequences != null) {
                    System.out.println("backpropagating : " + consequences);
                    moveConsequence(fsm, consequences.getLeft(), consequences.getRight());
                    return true;
                }
            }
        }
        return false;
    }

    private void moveConsequence(FiniteStateMachine<FsmNode, EpsilonTransition> fsm, FsmNode termNode, Collection<FsmNode> consequences) {
        
        for (FsmNode consequence : consequences) {
            for (Transition<FsmNode, EpsilonTransition> preTrans : new ArrayList<>(fsm.getInverseTransitions(termNode))) {
                fsm.addTransition(preTrans.getToState(), EpsilonTransition.createEffectTransition(), consequence);               
            }

            for (Transition<FsmNode, EpsilonTransition> inTrans : new ArrayList<>(fsm.getTransitions(termNode))) {
                FsmNode actionNode = inTrans.getToState();

                Collection<Transition<FsmNode, EpsilonTransition>> transitionsBetween = fsm.getTransitionsBetween(actionNode, consequence);
                if (transitionsBetween.size() != 1) {
                    throw new IllegalStateException("!!!" + actionNode + " --- " + consequence + ": " +  transitionsBetween);
                }

                Transition<FsmNode, EpsilonTransition> transition = transitionsBetween.iterator().next();
                fsm.removeTransition(actionNode, transition.getOperator(), consequence);
                fsm.addTransition(consequence, EpsilonTransition.createPreconditionTransition(true), actionNode);
            }
        }
    }

    private Pair<FsmNode, Collection<FsmNode>> getGoodConsequences(FiniteStateMachine<FsmNode, EpsilonTransition> fsm, FsmNodeTerm termNode) {
        Collection<FsmNode> consequences = null;
        boolean containsSomeInternalAction = false;
        for (Transition<FsmNode, EpsilonTransition> trans : fsm.getTransitions(termNode)) {
            FsmNodeAction actionNode = (FsmNodeAction) trans.getToState();
            if (actionNode.action.isInternal()) {
                containsSomeInternalAction = true;
            }
            
            if (trans.getOperator().isEpsilon()) {
                return null;
            } else {
                if (consequences == null) {
                    consequences = new HashSet<>(fsm.getToStates(actionNode));
                } else {
                    consequences.retainAll(fsm.getToStates(actionNode));
                }
            }
            if (consequences.isEmpty()) {
                break;
            }
        }
        if (containsSomeInternalAction) {
            if (!consequences.isEmpty()) {
                System.out.println("found: " + consequences + " trying next");
            }
            for (FsmNode consequence : consequences) {
                Pair<FsmNode, Collection<FsmNode>> nextConsequences = getGoodConsequences(fsm, (FsmNodeTerm) consequence);
                if (nextConsequences != null) {
                    return nextConsequences;
                }
            }
            if (!consequences.isEmpty()) {
                return new Pair<FsmNode, Collection<FsmNode>>(termNode, consequences);
            } else {
                return null; 
            }
        } else {
            return null; 
        }
    }

}
