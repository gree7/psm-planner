package cz.agents.dimap.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.agents.alite.math.CartesianProduct;
import cz.agents.dimap.psm.operator.Binding;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlCondition;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.types.PddlType;

/**
 * Based on relaxed planning graph
 */
public class ReachableFactsExtractor {
    private Set<PddlTerm> reachableFacts;
    private Map<PddlName, Set<PddlTerm>> reachableFactsByPredicate;
    private PddlProblem problem;

    public ReachableFactsExtractor(PddlProblem problem) {
        this.problem = problem;
        reachableFacts = new HashSet<>();
        reachableFactsByPredicate = new HashMap<>();
        addReachableFacts(problem.init.positives);
        applyAllActionsRepeatedly(problem.domain.actions);
    }

    private void addReachableFacts(Collection<PddlTerm> facts) {
        reachableFacts.addAll(facts);
        for (PddlTerm pddlPredicate : facts) {
            Set<PddlTerm> reachable = reachableFactsByPredicate.get(pddlPredicate.name);
            if (reachable == null) {
                reachable = new HashSet<>();
                reachableFactsByPredicate.put(pddlPredicate.name, reachable);
            }
            reachable.add(pddlPredicate);
        }
    }

    private void applyAllActionsRepeatedly(List<PddlAction> actions) {
        int lastNumOfFacts = -1;
        while (lastNumOfFacts != reachableFacts.size()) {
            lastNumOfFacts = reachableFacts.size();
            applyReachableActions(actions);
        }
    }

    private void applyReachableActions(List<PddlAction> actions) {
        Set<PddlAction> reachableActions = generateReachableActions(actions);
        for (PddlAction action : reachableActions) {
            if (isApplicableAction(action)) {
                addReachableFacts(action.effect.positives);
            }
        }
    }

    private Set<PddlAction> generateReachableActions(List<PddlAction> actions) {
        Set<PddlAction> reachableActions = new LinkedHashSet<PddlAction>();

        for (PddlAction action : actions) {
            List<Set<PddlName>> domainsDefinition = new ArrayList<>();
            List<PddlName> parameterNames = new ArrayList<>(action.parameters.getBindings().keySet());
            List<PddlType> parameterTypes = new ArrayList<>(action.parameters.getBindings().values());
            for (PddlType type : parameterTypes) {
                domainsDefinition.add(problem.getObjectsByType(type));
            }

            // grounded precondition and effect
            CartesianProduct<PddlName> cartesianProduct = new CartesianProduct<PddlName>(domainsDefinition);

            boolean isNullary = false;
            int size = cartesianProduct.size();
            if (parameterTypes.isEmpty()) {
                size = 1;
                isNullary = true;
            }
            for (int i = 0; i < size; i++) {
                // label
                String label = action.getName();

                // non-nullary actions has to be grounded by Cartesian product of all possible parameters
                Binding binding = new Binding();
                               
                if (!isNullary) {
                    List<PddlName> element = cartesianProduct.element(i);
                    int j = 0;
                    for (PddlName constant : element) {
                        binding.addBinding(parameterNames.get(j), constant);
                        label += "_" + constant;
                        ++j;
                    }
                }
                
                if (label != null) {
                    PddlCondition precondition = PddlCondition.createGroundedCondition(action.precondition, binding);
                    if (reachableFacts.containsAll(precondition.positives)) {
                        PddlCondition effect = PddlCondition.createGroundedCondition(action.effect, binding);
                        effect.removeContradictingEffects();
                        if ( !effect.isEmpty() ) {
                            PddlAction newAction = PddlAction.createGroundedAction(label, precondition, effect, action.getActionType());
                            reachableActions.add(newAction);
                        }
                    }
                }
            }
        }
        return reachableActions;
    }
    
    private boolean isApplicableAction(PddlAction action) {
        return reachableFacts.containsAll(action.precondition.positives);
    }

    public Set<PddlTerm> getUsedFacts(List<PddlAction> actions) {
        Set<PddlTerm> usedFacts = new HashSet<>();
        for (PddlAction action : generateReachableActions(actions)) {
            if (isApplicableAction(action)) {
                usedFacts.addAll(action.getPreconditionFacts());
                usedFacts.addAll(action.getEffectFacts());
            }            
        }
        return usedFacts;
    }
}
