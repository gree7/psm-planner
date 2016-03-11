package cz.agents.dimap.causal;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import cz.agents.dimap.Settings;
import cz.agents.dimap.fsm.FiniteStateMachine;
import cz.agents.dimap.psm.operator.Binding;
import cz.agents.dimap.tools.Pair;
import cz.agents.dimap.tools.fd.Downward;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlCondition;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.ma.ActionType;
import cz.agents.dimap.tools.pddl.types.PddlPredicateType;
import cz.agents.dimap.tools.pddl.types.PddlType;
import cz.agents.dimap.tools.pddl.types.PddlTypeMap;

public class InternalDependenciesGraphPddl extends InternalDependenciesGraph {

    final static boolean IGNORE_CONSTANTS = false;
    final static boolean REMOVE_CONSTANTS_FROM_PROBLEM = true;

    private PddlAction goalAction;
    private Collection<PddlTerm> constants;
    private Collection<PddlName> constantPredicates;

    private Collection<PddlTerm> removedInitFacts;

    Set<PddlAction> actions = new HashSet<>();

    private boolean isGrounded;

    PreconditionHandler preconditionHandler;

    public InternalDependenciesGraphPddl(String agentName, PddlProblem problem, boolean isGrounded) {
        super(agentName, problem);
        this.isGrounded = isGrounded;
        preconditionHandler = new PreconditionHandler(problem);
    }

    @Override
    public void init() {
        if (IGNORE_CONSTANTS) {
            constants = extractConstants(problem);
            removedInitFacts = new HashSet<>(constants);
            removedInitFacts.retainAll(constants);
            problem.init.positives.removeAll(constants);
            problem.goal.positives.removeAll(constants);

            for (PddlAction action : problem.domain.actions) {
                PddlAction renamedAction = PddlAction.createRenamedAction(paramsToActionName(action), action);
                for (Iterator<PddlTerm> iterator = renamedAction.precondition.positives.iterator(); iterator.hasNext();) {
                    PddlTerm term = iterator.next();
                    if (constantPredicates.contains(term.name)) {
                        iterator.remove();
                    }
                }
                actions.add(renamedAction);
            }
        } else if (REMOVE_CONSTANTS_FROM_PROBLEM) {
            constants = extractConstants(problem);
            problem.init.positives.removeAll(constants);
            problem.goal.positives.removeAll(constants);

            for (PddlAction pddlAction : problem.domain.actions) {
                PddlAction renamedAction = PddlAction.createRenamedAction(paramsToActionName(pddlAction), pddlAction);
                LinkedList<PddlTerm> relevantConstants = getRelevantConstants(renamedAction);
                Collection<PddlAction> groundedActions = groundAction(renamedAction, relevantConstants);
                if (groundedActions.isEmpty()) {
                    actions.add(renamedAction);
                } else {
                    actions.addAll(groundedActions);
                }
            }
        } else {
            for (PddlAction action : problem.domain.actions) {
                actions.add(PddlAction.createRenamedAction(paramsToActionName(action), action));
            }
        }
        super.init();
    }

    protected FiniteStateMachine<FsmNode, EpsilonTransition> createFsm(
            PddlProblem problem) {

        FiniteStateMachine<FsmNode, EpsilonTransition> fsm = new FiniteStateMachine<FsmNode, EpsilonTransition>(INIT_STATE);

        goalAction = new PddlAction(
                "GOAL", 
                new PddlTypeMap<PddlName>(),
                problem.goal, 
                new PddlCondition(new ArrayList<PddlTerm>(),problem.goal.positives), 
                ActionType.PUBLIC
                );

        Queue<FsmNodeTerm> newTermNodes = new ArrayDeque<>();

        newTermNodes.addAll(addActionToFsm(fsm, INIT_ACTION));

        for (PddlAction action : actions) {
            if (action.getActionType().equals(ActionType.PARSED)) {
                action.setActionType(ActionType.INTERNAL);
            }
            preconditionHandler.addAction(action, action.precondition.positives);
        }
try {

        while (!newTermNodes.isEmpty()) {
            FsmNodeTerm term = newTermNodes.remove();

            Collection<PddlAction> supportedActions = preconditionHandler.addFact(term.term);
            for (PddlAction action : supportedActions) {
                newTermNodes.addAll(addActionToFsm(fsm, action));
            }
            // List<PddlAction> newActions = new ArrayList<>();
            //
            // for (PddlAction action : actions) {
            // Collection<PddlAction> groundedActions = groundAction(action,
            // term.term);
            // for (PddlAction groundedAction : groundedActions) {
            // Collection<PddlTerm> preconditions =
            // getUnsupportedPreconditions(fsm, groundedAction);
            // if (preconditions.isEmpty()) {
            // newTermNodes.addAll(addActionToFsm(fsm, groundedAction));
            // } else {
            // if (groundedAction.isGrounded()) {
            // preconditionHandler.addAction(groundedAction, preconditions);
            // } else {
            // newActions.add(groundedAction);
            // }
            // }
            // }
            // }
            // actions.addAll(newActions);
            // logFsm(fsm, term.toString());
        }

        logFsm(fsm, "without goal");

        addActionToFsm(fsm, goalAction);

} catch(Exception e) {
    e.printStackTrace();
}

        logFsm(fsm, "created");

        return fsm;
    }

    private Collection<PddlTerm> extractConstants(PddlProblem problem) {
        constantPredicates = extractConstantPredicates(problem);
        Collection<PddlTerm> constants = new ArrayList<>();
        for (PddlTerm term : problem.init.positives) {
            if (constantPredicates.contains(term.name)) {
                constants.add(term);
            }
        }
        return constants;
    }

    private static Collection<PddlName> extractConstantPredicates(
            PddlProblem problem) {
        Collection<PddlName> constantPredicates = new HashSet<>(
                problem.domain.predicateTypes.keySet());
        for (PddlAction action : problem.domain.actions) {
            for (PddlTerm effect : action.effect.positives) {
                constantPredicates.remove(effect.name);
            }
            for (PddlTerm effect : action.effect.negatives) {
                constantPredicates.remove(effect.name);
            }
        }
        return constantPredicates;
    }

    // private Collection<PddlTerm> getUnsupportedPreconditions(
    // FiniteStateMachine<FsmNode, EpsilonTransition> fsm, PddlAction action) {
    // Collection<PddlTerm> preconds = new ArrayList<>();
    //
    // for (PddlTerm term : action.precondition.positives) {
    // if (!fsm.containsState(new FsmNodeTerm(term, false))) {
    // preconds.add(term);
    // }
    // }
    // return preconds;
    // }

    private String paramsToActionName(PddlAction action) {
        String name = action.name;
        for (PddlName param : action.parameters.getBindings().keySet()) {
            name += " " + param.name;
        }
        return name;
    }

    private LinkedList<PddlTerm> getRelevantConstants(PddlAction action) {
        LinkedList<PddlTerm> relevantConstants = new LinkedList<>();
        for (PddlTerm constant : constants) {
            for (PddlTerm precond : action.precondition.positives) {
                if (precond.name.equals(constant.name)) {
                    relevantConstants.add(constant);
                }
            }
        }
        return relevantConstants;
    }

    private Collection<PddlAction> groundAction(PddlAction action, LinkedList<PddlTerm> terms) {
        Collection<PddlAction> newActions = new ArrayList<>();
        if (!terms.isEmpty()) {
            PddlTerm term = terms.removeFirst();
            newActions.addAll(groundAction(action, terms));

            Collection<PddlAction> groundedActions = preconditionHandler.groundAction(action, term);
            for (PddlAction groundedAction : groundedActions) {
                newActions.addAll(groundAction(groundedAction, terms));
            }

            terms.addFirst(term);
        } else {
            action.precondition.positives.removeAll(constants);
            if (getRelevantConstants(action).isEmpty()) {
                newActions.add(action);
            }
        }
        return newActions;
    }

    private void checkDeleteEffects(PddlAction action) {
        Collection<PddlTerm> deleteEffects = new HashSet<>(action.effect.negatives);
        deleteEffects.removeAll(action.precondition.positives);
        if (!deleteEffects.isEmpty()) {
            throw new IllegalArgumentException("Action contains delete effects which are not in precondition! " + action + " --> " + deleteEffects);
        }
    }


    private Collection<FsmNodeTerm> addActionToFsm(
            FiniteStateMachine<FsmNode, EpsilonTransition> fsm,
            PddlAction action) {

        checkDeleteEffects(action);
        
        Collection<FsmNodeTerm> newTermNodes = new ArrayList<>();
        FsmNodeAction stateAction;

        stateAction = new FsmNodeAction(action, "");

        fsm.addState(stateAction);

        for (PddlTerm term : action.precondition.positives) {
            FsmNodeTerm stateTerm = getUnifiedTerm(fsm, term, true);

            boolean isEpsilon = !action.effect.negatives.contains(term);

            fsm.addTransition(
                    stateTerm, 
                    EpsilonTransition.createPreconditionTransition(isEpsilon), 
                    stateAction);
        }

        for (PddlTerm term : action.effect.positives) {
            FsmNodeTerm stateTerm = getUnifiedTerm(fsm, term, false);

            if (!fsm.containsState(stateTerm)) {
                fsm.addState(stateTerm);
                newTermNodes.add(stateTerm);
            }

            fsm.addTransition(stateAction, EpsilonTransition.createEffectTransition(), stateTerm);
        }
        return newTermNodes;
    }

    private FsmNodeTerm getUnifiedTerm(FiniteStateMachine<FsmNode,EpsilonTransition> fsm, PddlTerm term, boolean throwIfNotFound) {
        FsmNodeTerm newTermNode = new FsmNodeTerm(term, false);
        if (fsm.containsState(newTermNode)) {
            return newTermNode;
        } else {
            for (FsmNode node : fsm.getStates()) {
                if (node instanceof FsmNodeTerm) {
                    FsmNodeTerm termNode = (FsmNodeTerm) node;
                    if (preconditionHandler.areTermsEquivalent(termNode.term, term)) {
                        return termNode;
                    }
                }
            }
        }
        if (throwIfNotFound) {
            throw new IllegalArgumentException("Should not happen! Unknown term: " + term);
        } else {
            return newTermNode;
        }
    }

    public PddlProblem createConvertedProblem() {
        PddlProblem convertedProblem = super.createConvertedProblem(goalAction);
        if (!isGrounded) {
            convertedProblem.domain.predicateTypes = new HashMap<PddlName, PddlPredicateType>(problem.domain.predicateTypes);
        }

        List<PddlAction> newActions = new ArrayList<>();
        Collection<PddlType> relevantTypes = new ArrayList<>();
        for (Iterator<PddlAction> iterator = convertedProblem.domain.actions.iterator(); iterator.hasNext();) {
            PddlAction action = iterator.next();
            Pair<PddlAction, Collection<PddlType>> converted = convertFreeParameters(action, problem.domain.actions);
            newActions.add(converted.getLeft());
            relevantTypes.addAll(converted.getRight());
            iterator.remove();
        }
        convertedProblem.domain.actions.addAll(newActions);

        for (PddlType type : relevantTypes) {
            Set<PddlName> objects = problem.getObjectsByType(type);
            for (PddlName object : objects) {
                convertedProblem.objects.addAssignment(object, type);
            }
        }

        if (!relevantTypes.isEmpty()) {
            convertedProblem.domain.types = new PddlTypeMap<>(problem.domain.types);
        }

        return convertedProblem;
    }

    private Pair<PddlAction, Collection<PddlType>> convertFreeParameters(PddlAction action, List<PddlAction> actions) {
        PddlAction fixedAction = action;
        Collection<PddlType> relevantTypes = new ArrayList<>();
        for (PddlAction originalAction : actions) {
            if (action.name.startsWith(originalAction.name)) {
                while (fixedAction.name.contains("?")) {
                    Pair<PddlAction, PddlType> converted = convertFirstFreeParameter(fixedAction, originalAction);
                    fixedAction = converted.getLeft();
                    relevantTypes.add(converted.getRight());
                }
                return new Pair<PddlAction, Collection<PddlType>>(fixedAction, relevantTypes);
            }
        }
        throw new IllegalStateException("Should not happen! " + action.name + " --- " + actions);
    }

    private Pair<PddlAction, PddlType> convertFirstFreeParameter(PddlAction action, PddlAction originalAction) {
        int startIndex = action.name.indexOf('?');
        int endIndex = action.name.indexOf(' ', startIndex);
        if (endIndex < 0) {
            endIndex = action.name.length();
        }
        PddlName paramName = new PddlName(action.name.substring(startIndex, endIndex));
        PddlType paramType = originalAction.parameters.getBindings().get(paramName);

        String newName = action.name.replace('?', '-');

        PddlTypeMap<PddlName> parameters = new PddlTypeMap<>(action.parameters);
        parameters.addAssignment(paramName, paramType);
        return new Pair<PddlAction, PddlType>(
                new PddlAction(
                    newName,
                    parameters, 
                    action.precondition, 
                    action.effect,
                    action.getActionType()), 
                paramType);
    }

    public static void main(String[] args) throws IOException {

        deleteAllImages();
        Downward.forceCleanAllTmpFiles();

        Settings.DOT_CAUSAL_GRAPH = true;
        PddlDomain domain = new PddlDomain(new FileReader(new File("experiments/ma-benchmarks/beerproblem/beerproblem.pddl")));
        PddlProblem problem = new PddlProblem(domain, new FileReader(new File("experiments/ma-benchmarks/beerproblem/beerproblem-01.pddl")));
        InternalDependenciesGraph id = new InternalDependenciesGraphPddl("node1", problem, false);
        id.init();
        id.countInternalDependencies();
        id.updateProblem(problem);
    }

    private static void deleteAllImages() {
        for (File file : new File(".").listFiles()) {
            if (file.getName().startsWith("fsmRisky")) {
                file.delete();
            }
        }
    }

    public int getFacts() {
        return getFacts(fsm);
    }
}

class PreconditionHandler {
    /**
     * contains actions and their preconditions that are not met yet if new fact
     * is added, appropriate preconditions are covered and actions are grounded
     * if necessary if all preconditions of some actions are covered, these
     * actions are removed and returned. New actions together with their unmet
     * preconditions can be added anytime.
     */

    Map<PddlTerm, List<PddlAction>> groundedPreconditionMap = new HashMap<>();
    Map<PddlAction, Collection<PddlTerm>> actionMap = new HashMap<>();
    Map<PddlName, Set<PddlTerm>> preconditionMap = new HashMap<>();

    List<PddlTerm> supportedFacts = new ArrayList<>();
    private PddlProblem problem;

    public PreconditionHandler(PddlProblem problem) {
        this.problem = problem;
    }

    void addAction(PddlAction action, Collection<PddlTerm> preconditions) {
        if (actionMap.containsKey(action)) {
            return;
        }
        HashSet<PddlTerm> preconds = new HashSet<>();
        for (PddlTerm term : preconditions) {
            boolean isSupported = false;
            for (PddlTerm supported : supportedFacts) {
                if (areTermsEquivalent(term, supported)) {
                    isSupported = true;
                    break;
                }
            }
            if (!isSupported) {
                preconds.add(term);
            }
        }
        actionMap.put(action, preconds);
        for (PddlTerm term : preconds) {
            addToMapList(groundedPreconditionMap, term, action);
            if (!term.isGrounded()) {
                addToMapSet(preconditionMap, term.name, term);
            }
        }
    }

    boolean areTermsEquivalent(PddlTerm term1, PddlTerm term2) {
        if (!term1.name.equals(term2.name)
                || term1.arguments.size() != term2.arguments.size()) {
            return false;
        } else {
            for (int i=0; i<term1.arguments.size(); i++) {
                PddlName arg1 = term1.arguments.get(i);
                PddlName arg2 = term2.arguments.get(i);
                if (arg1.isVariable()) {
                    if (!arg2.isVariable()) {
                        return false;
                    }
                } else { // term1 is not variable
                    if (arg2.isVariable()) {
                        return false;
                    } else { // both terms are constant
                        if (!arg1.equals(arg2)) {
                            return false;
                        }
                    }
                    
                }
            }
            return true;
        }
    }

    Collection<PddlAction> addFact(PddlTerm term) {
        groundActionsToFact(term);

        List<PddlAction> readyActions = new ArrayList<>();
        List<PddlAction> actions = groundedPreconditionMap.remove(term);
        if (actions != null) {
            for (PddlAction action : actions) {
                Collection<PddlTerm> preconditions = actionMap.get(action);
                if (preconditions == null) {
                    System.err.println("action.name: " + action.name);
                }
                preconditions.remove(term);
                if (preconditions.isEmpty()) {
                    readyActions.add(action);
                    actionMap.remove(action);
                }
            }
            if (!term.isGrounded()) {
                preconditionMap.get(term.name).remove(term);
            }
        }
        supportedFacts.add(term);
        return readyActions;
    }

    private void groundActionsToFact(PddlTerm fact) {
        Set<PddlTerm> terms = preconditionMap.get(fact.name);
        if (terms != null) {
            List<Pair<PddlAction, Collection<PddlTerm>>> newActions = new ArrayList<>();
            for (PddlTerm term : terms) {
                if (term.isUnifiable(fact)) {
                    for (PddlAction action : groundedPreconditionMap.get(term)) {
                        Collection<PddlAction> groundedActions = groundAction(action, fact);
                        for (PddlAction groundedAction : groundedActions) {
                            newActions.add(new Pair<PddlAction, Collection<PddlTerm>>(
                                            groundedAction,
                                            groundedAction.precondition.positives));
                        }
                    }
                }
            }
            for (Pair<PddlAction, Collection<PddlTerm>> newAction : newActions) {
                addAction(newAction.getLeft(), newAction.getRight());
            }
        }
    }

    Collection<PddlAction> groundAction(PddlAction action, PddlTerm term) {
        Collection<PddlAction> newActions = new ArrayList<>();
        Collection<Binding> bindings = getBindings(action, term);
        for (Binding binding : bindings) {
            PddlAction bindAction = bindAction(action, binding);
            if (bindAction != null) {
                newActions.add(bindAction);
            }
        }
        return newActions;
    }

    private PddlAction bindAction(PddlAction action, Binding binding) {
        PddlCondition precondition = PddlCondition.createGroundedCondition(action.precondition, binding);
        PddlCondition effect = PddlCondition.createGroundedCondition(action.effect, binding);

        effect.removeContradictingEffects();
        if (!effect.isEmpty()) {
            return new PddlAction(
                    createActionName(action, binding),
                    getNonGroundedParameters(action, binding), 
                    precondition,
                    effect, 
                    ActionType.INTERNAL);
        } else {
            return null;
        }
    }

    private PddlTypeMap<PddlName> getNonGroundedParameters(PddlAction action, Binding binding) {
        PddlTypeMap<PddlName> params = new PddlTypeMap<>(action.parameters);
        for (PddlName param : binding.getBindedParameters()) {
            params.removeAssignment(param);
        }
        return params;
    }

    private String createActionName(PddlAction action, Binding binding) {
        String actionName = action.name;
        for (PddlName par : binding.getBindedParameters()) {
            actionName = actionName.replace(par.name, binding.getParameterValue(par).name);
        }
        return actionName;
    }

    private Collection<Binding> getBindings(PddlAction action, PddlTerm term) {
        Collection<Binding> bindings = new HashSet<>();
        for (PddlTerm actionTerm : action.precondition.positives) {
            Binding binding = createBinding(actionTerm, term);
            if (binding != null && isTypeCompatible(action.parameters.getBindings(), binding)) {
                bindings.add(binding);
            }
        }
        return bindings;
    }

    private boolean isTypeCompatible(LinkedHashMap<PddlName, PddlType> types, Binding binding) {
        for (Entry<PddlName, PddlName> entry : binding.getBindings().entrySet()) {
            PddlType type = types.get(entry.getKey());
            if (!problem.getObjectsByType(type).contains(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    private Binding createBinding(PddlTerm actionTerm, PddlTerm term) {
        if (actionTerm.name.equals(term.name) && actionTerm.arguments.size() == term.arguments.size()) {
            Binding binding = new Binding();
            for (int i = 0; i < term.arguments.size(); i++) {
                PddlName actionArg = actionTerm.arguments.get(i);
                PddlName termArg = term.arguments.get(i);
                if (actionArg.isVariable()) {
                    binding.addBinding(actionArg, termArg);
                } else if (!actionArg.equals(termArg)) {
                    return null;
                }
            }
            return binding;
        }
        return null;
    }

    <K, V> void addToMapList(Map<K, List<V>> map, K key, V value) {
        List<V> values = map.get(key);
        if (values == null) {
            values = new ArrayList<>();
            map.put(key, values);
        }
        values.add(value);
    }

    <K, V> void addToMapSet(Map<K, Set<V>> map, K key, V value) {
        Set<V> values = map.get(key);
        if (values == null) {
            values = new HashSet<>();
            map.put(key, values);
        }
        values.add(value);
    }

    @Override
    public String toString() {
        return "PreconditionHandler: actionMap=" + actionMap;
    }
}
