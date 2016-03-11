package experiments.codmap.distributed.agent;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlTerm;

public class FactLayer {
    private Map<PddlTerm, Set<PddlAction>> applicableActions = new HashMap<>();

    public void addApplicableAction(PddlTerm fact, PddlAction action) {
        Set<PddlAction> actions = applicableActions.get(fact);
        if (actions == null) {
            actions = new HashSet<PddlAction>();
            applicableActions.put(fact, actions);
        }
        actions.add(action);
    }

    public boolean isEmpty() {
        return applicableActions.isEmpty();
    }

    public Collection<? extends PddlTerm> getFacts() {
        return applicableActions.keySet();
    }

    public boolean containsFact(PddlTerm fact) {
        return applicableActions.containsKey(fact);
    }

    public PddlAction getBestAction(Set<PddlTerm> facts) {
        Map<PddlAction, Integer> coveredGoals = new HashMap<>();
        for (PddlTerm fact : facts) {
            if (applicableActions.containsKey(fact)) {
                for (PddlAction action : applicableActions.get(fact)) {
                    Integer value = coveredGoals.get(action);
                    if (value == null) {
                        value = 0;
                    }
                    value++;
                    coveredGoals.put(action, value);
                }
            }
        }
        
        PddlAction bestAction = null;
        int bestCoverage = -1;
        for (Entry<PddlAction, Integer> entry : coveredGoals.entrySet()) {
            if (entry.getValue() > bestCoverage) {
                bestAction = entry.getKey();
                bestCoverage = entry.getValue();
            }
        }
        return bestAction;
    }
    
    @Override
    public String toString() {
        return applicableActions.toString();
    }
}
