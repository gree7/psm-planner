package cz.agents.dimap.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.alite.math.CartesianProduct;
import cz.agents.dimap.psm.operator.Binding;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlCondition;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.types.PddlType;

/**
 * Based on relaxed planning graph
 */
public class GroundedActionsExtractor {
    private Set<PddlTerm> reachableFacts;
    private Set<PddlAction> appliedActions;
    private Map<PddlName, Set<PddlTerm>> reachableFactsByPredicate;
    private PddlProblem problem;
    private String agentName;

    public GroundedActionsExtractor(PddlProblem problem, String agentName) {
        this.problem = problem;
        this.agentName = agentName;
        reachableFacts = new HashSet<>();
        appliedActions = new HashSet<>();
        reachableFactsByPredicate = new HashMap<>();
        addReachableFacts(problem.init.getFacts());
    }

    public void addReachableFacts(Collection<PddlTerm> facts) {
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

    public static Set<PddlAction> extractReachableActions(PddlProblem problem, String agentName) {
        GroundedActionsExtractor actionExtractor = new GroundedActionsExtractor(problem, agentName);
        actionExtractor.applyAllActionsRepeatedly(false, new HashSet<PddlName>());
        return actionExtractor.appliedActions;
    }

    public void applyAllActionsRepeatedly(boolean useOnlyInternalFacts, Set<PddlName> constants) {
        int lastNumOfFacts = -1;
        while (lastNumOfFacts != reachableFacts.size()) {
            lastNumOfFacts = reachableFacts.size();
            applyReachableActions(useOnlyInternalFacts, constants);
        }
    }

    private void applyReachableActions(boolean useOnlyInternalFacts, Set<PddlName> constants) {
        Set<PddlAction> reachableActions = generateReachableActions(agentName, problem, reachableFacts, useOnlyInternalFacts, constants);
//        reachableActions.removeAll(appliedActions);
        for (PddlAction action : reachableActions) {
            if (!appliedActions.contains(action)) {
                if (isApplicablePrecondition(problem, action.precondition.positives, reachableFacts, useOnlyInternalFacts)) {
                    addReachableFacts(action.effect.positives);
                    appliedActions.add(action);
                }
            }
        }
    }

    public static Set<PddlAction> generateReachableActions(String agentName, PddlProblem problem, Set<PddlTerm> reachableFacts, boolean useOnlyInternalFacts, Set<PddlName> constantPredicates) {
        Set<PddlAction> actions = new LinkedHashSet<PddlAction>();
        PddlDomain domain = problem.domain;
        
        nextAction:
        for (PddlAction action : domain.actions) {
            
            List<Set<Binding>> domainsDefinition = new ArrayList<>();
            List<PddlName> parameterNames = new ArrayList<>(action.parameters.getBindings().keySet());
            List<PddlType> parameterTypes = new ArrayList<>(action.parameters.getBindings().values());
            
            Set<PddlName> parametersUsedInConstants = new HashSet<>();
            Set<PddlTerm> usedConstantPredicates = new HashSet<>();
            for (PddlTerm precondition : action.precondition.positives) {
                if (constantPredicates.contains(precondition.name)) {
                    parametersUsedInConstants.addAll(precondition.arguments);
                    usedConstantPredicates.add(precondition);
                }
            }

            Set<Entry<PddlName, PddlType>> constantParameters = new HashSet<>();
            boolean isFirst = true;
            for (Entry<PddlName, PddlType> type : action.parameters.getBindings().entrySet()) {
                if (parametersUsedInConstants.contains(type.getKey())) {
                    constantParameters.add(type);
                    isFirst = false;
                } else {
                    Set<PddlName> objectsByType = problem.getObjectsByType(type.getValue());
                    PddlName agentPddlName = new PddlName(agentName);
                    if (objectsByType.contains(agentPddlName) && isFirst) {
                        objectsByType.clear();
                        objectsByType.add(agentPddlName);
                    }
                    isFirst = false;
                    Set<Binding> objects = new HashSet<>();
                    for (PddlName object : objectsByType) {
                        Binding binding = new Binding();
                        binding.addBinding(type.getKey(), object);
                        objects.add(binding);
                    }
                    if (objects.isEmpty()) {
                        continue nextAction;
                    }
                    domainsDefinition.add(objects);
                }
            }
            if (!constantParameters.isEmpty()) {
                domainsDefinition.add(getAllInitBindings(problem, usedConstantPredicates, new Binding()));
            }
            
            // grounded precondition and effect
            CartesianProduct<Binding> cartesianProduct = new CartesianProduct<Binding>(domainsDefinition);

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
                    for (Binding paramBinding : cartesianProduct.element(i)) {
                        binding.add(paramBinding);
                    }
                    for (int j = 0; j<parameterNames.size(); j++) {
                        label += "_" + binding.getParameterValue(parameterNames.get(j));
                    }
                }

                if (label != null) {
                    PddlCondition precondition = PddlCondition.createGroundedCondition(action.precondition, binding);
                    if (isApplicablePrecondition(problem, precondition.positives, reachableFacts, useOnlyInternalFacts)) {
                        PddlCondition effect = PddlCondition.createGroundedCondition(action.effect, binding);
                        effect.removeContradictingEffects();
                        if ( !effect.isEmpty() ) {
                            PddlAction newAction = PddlAction.createGroundedAction(label, precondition, effect, action.getActionType());
                            actions.add(newAction);
                        }
                    }
                }
            }
        }
        return actions;
    }
    
    private static Set<Binding> getAllInitBindings(PddlProblem problem, Set<PddlTerm> usedConstantPredicates, Binding oldBinding) {
        Set<Binding> bindings = new HashSet<>();
        if (usedConstantPredicates.isEmpty()) {
            bindings.add(oldBinding);
        } else {
            Set<PddlTerm> tmpUsedConstantPredicates = new HashSet<>(usedConstantPredicates);
            Iterator<PddlTerm> iterator = tmpUsedConstantPredicates.iterator();
            PddlTerm precond = iterator.next();
            iterator.remove();
    
            for (Binding newBinding : getInitBindings(problem, precond)) {
                try {
                    Binding binding = new Binding(oldBinding);
                    binding.add(newBinding);
                    bindings.addAll(getAllInitBindings(problem, tmpUsedConstantPredicates, binding));
                } catch (IllegalArgumentException e) {
                    // ok, try next binding
                }
            }
        }
        return bindings;
    }

    private static Collection<Binding> getInitBindings(PddlProblem problem, PddlTerm precond) {
        List<Binding> bindings = new ArrayList<>();
        for (PddlTerm fact : problem.init.positives) {
            if (fact.name.equals(precond.name)) {
                bindings.add( createBinding(precond, fact) );
            }
        }
        return bindings;
    }

    private static Binding createBinding(PddlTerm predicate, PddlTerm fact) {
        Binding binding = new Binding();
        int i=0;
        for (PddlName arg : predicate.arguments) {
            binding.addBinding(arg, fact.arguments.get(i));
            i++;
        }
        return binding;
    }

    private static boolean isApplicablePrecondition(PddlProblem problem, List<PddlTerm> facts, Set<PddlTerm> reachableFacts, boolean useOnlyInternalFacts) {
        if (useOnlyInternalFacts) {
            for (PddlTerm fact : facts) {
                if (isInternal(problem, fact)) {
                    if (!reachableFacts.contains(fact)) {
                        System.out.println("fact: " + fact);
                        return false;
                    }
                }
            }
            return true;
        } else {
            return reachableFacts.containsAll(facts);
        }
    }

    private static boolean isInternal(PddlProblem problem, PddlTerm fact) {
        for (PddlName predicate : problem.domain.privatePredicates) {
            if (fact.name.name.startsWith(predicate.name)) {
                return true;
            }
        }
        for (PddlName object : problem.privateObjects) {
            if (fact.arguments.contains(object)) {
                return true;
            }
        }
        return false;
    }

    public Set<PddlTerm> getReachableFacts() {
        return reachableFacts;
    }

    public Set<PddlAction> getAppliedActions() {
        return appliedActions;
    }
}
