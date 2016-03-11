package cz.agents.dimap.causal.operator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cz.agents.dimap.causal.EpsilonTransition;
import cz.agents.dimap.causal.FsmNode;
import cz.agents.dimap.causal.FsmNodeAction;
import cz.agents.dimap.fsm.FiniteStateMachine;
import cz.agents.dimap.fsm.Transition;
import cz.agents.dimap.tools.Pair;

public class MergeEquivalentNodes implements IdGraphOperator {

    @Override
    public boolean update(FiniteStateMachine<FsmNode, EpsilonTransition> fsm) {
        Map<Pair<Collection<Transition<FsmNode, EpsilonTransition>>,Collection<Transition<FsmNode, EpsilonTransition>>>, FsmNode> processed = new HashMap<>();
        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeAction) {
                if (((FsmNodeAction)node).action.isPublic()) {
                    continue;
                }
            }
            Collection<Transition<FsmNode, EpsilonTransition>> outTrans = fsm.getTransitions(node);
            Collection<Transition<FsmNode, EpsilonTransition>> inTrans = fsm.getInverseTransitions(node);
            Pair<Collection<Transition<FsmNode, EpsilonTransition>>, Collection<Transition<FsmNode, EpsilonTransition>>> transPair = new Pair< Collection<Transition<FsmNode, EpsilonTransition>>,Collection<Transition<FsmNode, EpsilonTransition>>>( inTrans, outTrans);
            FsmNode equivNode = processed.get(transPair);
            if (equivNode != null) {
                for (Transition<FsmNode, EpsilonTransition> trans : outTrans) {
                    if (!fsm.getTransitions(equivNode).contains(trans)) {
                        fsm.addTransition(equivNode, trans.getOperator(), trans.getToState());
                    }
                }
                fsm.removeState(node);
                if (LOGGING) {
                    System.out.println("merged: " + node + " === " + equivNode);
                }
                return true;
            }
            processed.put(transPair, node);
        }
        return false;
    }

}
