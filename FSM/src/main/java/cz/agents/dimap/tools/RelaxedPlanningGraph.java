package cz.agents.dimap.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cz.agents.alite.math.CartesianProduct;
import cz.agents.dimap.psm.operator.Binding;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlCondition;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.types.PddlType;

public class RelaxedPlanningGraph {

    private Set<PddlTerm> reachableFacts;
    protected Set<PddlAction> appliedActions;
    private Iterable<PddlAction> allActions;
    protected PddlProblem problem;

    public static Set<PddlAction> extractReachableActions(PddlProblem problem) {
        RelaxedPlanningGraph graph = new RelaxedPlanningGraph(problem);
        graph.applyAllActionsRepeatedly();
        return graph.appliedActions;
    }

    public static Set<PddlTerm> extractReachableFacts(PddlProblem problem) {
        RelaxedPlanningGraph graph = new RelaxedPlanningGraph(problem);
        graph.applyAllActionsRepeatedly();
        return graph.reachableFacts;
    }

    protected RelaxedPlanningGraph(PddlProblem problem) {
        this.problem = problem;
        reachableFacts = new HashSet<>( problem.init.positives );
        appliedActions = new HashSet<>();
        this.allActions = problem.domain.actions;
    }
    
    protected void applyAllActionsRepeatedly() {
        int lastNumOfFacts = -1;
        while (lastNumOfFacts != reachableFacts.size()) {
            lastNumOfFacts = reachableFacts.size();
            applyAllActions();
        }
    }

    protected void applyAllActions() {
        for (PddlAction operator : allActions) {
            Set<PddlAction> actions = generateReachableActions(operator);
            for (PddlAction action : actions) {
                if (!appliedActions.contains(action)) {
                    applyAction(action);
                }
            }
        }
    }

    private Set<PddlAction> generateReachableActions(PddlAction action) {
        Set<PddlAction> reachableActions = new LinkedHashSet<PddlAction>();

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
        return reachableActions;
    }
    
    protected void applyAction(PddlAction action) {
        reachableFacts.addAll( action.effect.positives );
        appliedActions.add(action);
    }

    protected boolean isApplicableAction(PddlAction action) {
        for (PddlTerm predicate : action.precondition.positives) {
            if (!reachableFacts.contains(predicate)) {
                return false;
            }
        }
        return true;
    }
}
