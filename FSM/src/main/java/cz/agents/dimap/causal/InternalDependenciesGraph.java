package cz.agents.dimap.causal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cz.agents.dimap.Settings;
import cz.agents.dimap.causal.operator.IdGraphOperator;
import cz.agents.dimap.causal.operator.MergeEquivalentNodes;
import cz.agents.dimap.causal.operator.RemoveAlwaysValidTerms;
import cz.agents.dimap.causal.operator.RemoveDeadEnds;
import cz.agents.dimap.causal.operator.RemoveSimpleActionDependencies;
import cz.agents.dimap.causal.operator.RemoveSimpleFactDependencies;
import cz.agents.dimap.causal.operator.RemoveSmallActionCycles;
import cz.agents.dimap.causal.operator.RemoveStatesNotGoingFromInit;
import cz.agents.dimap.fsm.FiniteStateMachine;
import cz.agents.dimap.fsm.FiniteStateMachineTools;
import cz.agents.dimap.fsm.LabelFactory;
import cz.agents.dimap.fsm.Transition;
import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlCondition;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.ma.ActionType;
import cz.agents.dimap.tools.pddl.types.PddlPredicateType;
import cz.agents.dimap.tools.pddl.types.PddlTypeMap;
import experiments.psm.PsmFmapBenchmarks;
import experiments.psm.fmap.MaStripsMultiAgentProblem;

public class InternalDependenciesGraph implements InternalDependencies {

    protected PddlAction INIT_ACTION;
    protected FsmNodeAction INIT_STATE; 
    public PddlProblem problem;
    public Collection<PddlAction> actions;

    public FiniteStateMachine<FsmNode, EpsilonTransition> fsm;

    Collection<PddlTerm> allEffects = new ArrayList<>();

    Map<PddlAction, List<PddlTerm>> newPreconditions = new HashMap<>();
    Map<PddlAction, List<PddlTerm>> newDelEffects = new HashMap<>();
    Map<PddlAction, List<PddlTerm>> newAddEffects = new HashMap<>();
    Collection<PddlTerm> newInitMergeFacts = new HashSet<>();
    static int mergeFactIndex = 0;

    static int imageCounter=0;

    Collection<PddlTerm> newSharedFacts = new ArrayList<>();
    Collection<FsmNodeTerm> ignoredInternalFacts = new ArrayList<>();
    private String agentName;
    private boolean isFullyReduced = false;

    final static List<IdGraphOperator> idGraphOperators = Arrays.<IdGraphOperator>asList(
            new RemoveDeadEnds(),
            new RemoveSmallActionCycles(),
            new RemoveSimpleFactDependencies(),
            new RemoveSimpleActionDependencies(),
            new MergeEquivalentNodes(),
            new RemoveAlwaysValidTerms()
            //BROKEN!!! for logistics-4-0
//            new RemoveStatesNotGoingFromInit()
          //BROKEN!!! - single rovers 4
//            new PropagateAndPlusEdges(),
//            new BackpropagateConsequences()
            );
    
    public InternalDependenciesGraph(String agentName, PddlProblem problem) {
        this.agentName = agentName;
        actions = problem.getDomain().actions;
        this.problem = problem;
    }

    public void init() {
        INIT_ACTION = new PddlAction("INIT", new PddlTypeMap<PddlName>(), new PddlCondition(), new PddlCondition(problem.init), ActionType.PUBLIC);
        INIT_STATE = new FsmNodeAction(INIT_ACTION, "");

        for (PddlAction pddlAction : actions) {
            allEffects.addAll(pddlAction.getEffectFacts());
        }

        fsm = createFsm(problem);
        logFsm("created");
    }

    public void countInternalDependencies() {

        if (INIT_ACTION == null) {
            throw new IllegalStateException("Run init() first!");
        }
        
        logFsm();

        boolean hasChanged = true;
        while (hasChanged) {
            hasChanged = false;

            for (IdGraphOperator operator : idGraphOperators) {
                while ( operator.update(fsm) ) {
//                    logFsm(operator.getClass().getSimpleName());
                    hasChanged = true;
                }
//              logFsm();
            }

//            logFsm();
        }

        while ( publishSimpleDependencies() );
        logFsm();

        checkCompleteReduction();

        prepareTable();
    }

    protected int getFacts(FiniteStateMachine<FsmNode, EpsilonTransition> fsm) {
        int facts = 0;
        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeTerm) {
                facts++;
            }
        }
        return facts;
    }

    private void checkCompleteReduction() {
        isFullyReduced = true;
        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeTerm) {
                if (!ignoredInternalFacts.contains(node)) {
                    removePublishableFacts();
                    fsm.removeStatesNotGoingFromInit();

//                    Settings.DOT_CAUSAL_GRAPH = true;
                    logFsm("fail");
//                    Settings.DOT_CAUSAL_GRAPH = false;
                    isFullyReduced = false;
                    return;
                }
            }
        }        
    }

    private void removePublishableFacts() {
        List<FsmNode> toRemove = new ArrayList<>();
        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeTerm) {
                if (ignoredInternalFacts.contains(node)) {
                    toRemove.add(node);
                }
            }
        }
        for (FsmNode node : toRemove) {
            fsm.removeState(node);
        }
    }

    private boolean publishSimpleDependencies() {
        boolean hasChanged = false;
        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeTerm) {
                hasChanged |= tryPublishTerm((FsmNodeTerm)node);
            }
        }
        return hasChanged;
    }

    private boolean tryPublishTerm(FsmNodeTerm term) {
        if (isPublicTerm(term.term) || ignoredInternalFacts.contains(term)) {
            // already public
            return false;
        }
        for (Transition<FsmNode, EpsilonTransition> node : fsm.getInverseTransitions(term)) {
            if (!((FsmNodeAction)node.getToState()).action.isPublic()) {
                return false;
            }
        }
        for (Transition<FsmNode, EpsilonTransition> node : fsm.getTransitions(term)) {
            if (!((FsmNodeAction)node.getToState()).action.isPublic()) {
                return false;
            }
        }

        term.isPublic = true;
        ignoredInternalFacts.add(term);
        return true;
    }

    private boolean isPublicTerm(PddlTerm term) {
        return problem.domain.sharedPredicates.objects.contains(term)
                || newSharedFacts.contains(term);
    }

    protected FiniteStateMachine<FsmNode, EpsilonTransition> createFsm(PddlProblem problem) {
        FiniteStateMachine<FsmNode, EpsilonTransition> fsm = new FiniteStateMachine<FsmNode, EpsilonTransition>(INIT_STATE);
        fsm.removeState(INIT_STATE);

        addInitActionToFsm(fsm);

        for (PddlAction action : problem.domain.actions) {
            if (action.getActionType().equals(ActionType.PARSED)) {
                action.setActionType(ActionType.INTERNAL);
            }
            addActionToFsm(fsm, action);
        }
        return fsm;
    }

    private void addInitActionToFsm(FiniteStateMachine<FsmNode, EpsilonTransition> fsm) {

        fsm.addState(INIT_STATE);
        for (PddlTerm term : INIT_ACTION.effect.positives) {
            if (allEffects.contains(term) && !isPublicTerm(term)) {
                FsmNodeTerm stateTerm = new FsmNodeTerm(term, false);
                fsm.addTransition(INIT_STATE, EpsilonTransition.createEffectTransition(), stateTerm);
            }
        }
    }

    @Override
    public Collection<PddlTerm> updateProblem(PddlProblem problem) {
        
        if (INIT_ACTION == null) {
            throw new IllegalStateException("Run init() and countInternalDependencies() first!");
        }

        Collection<PddlTerm> newTerms = new HashSet<>();
        for (PddlAction action : problem.domain.actions) {

            if (newPreconditions.containsKey(action)) {
                List<PddlTerm> preconditions = newPreconditions.get(action);
                action.precondition.positives.addAll(preconditions);
                action.effect.negatives.addAll(newDelEffects.get(action));
                newTerms.addAll(preconditions);
            }

            if (newAddEffects.containsKey(action)) {
                List<PddlTerm> effects = newAddEffects.get(action);
                action.effect.positives.addAll(effects);
                newTerms.addAll(effects);
            }

            List<PddlTerm> commonTerms = new LinkedList<>(action.effect.negatives);
            commonTerms.retainAll(action.effect.positives);
            for (PddlTerm term : commonTerms) {
                if (term.toString().startsWith("merge_")) {
                    action.effect.negatives.remove(term);
                    action.effect.positives.remove(term);
                }
            }
        }
        problem.init.positives.addAll(newInitMergeFacts);
        for (PddlTerm term : newTerms) {
            problem.domain.predicateTypes.put(term.name, new PddlPredicateType(term.name));
        }

        problem.domain.sharedPredicates.objects.addAll(newSharedFacts);
        return newTerms;
    }

    private void prepareTable() {
        Map<PddlTerm, PddlTerm> newMergeFacts = new HashMap<>();

        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeAction) {
                FsmNodeAction action = (FsmNodeAction) node;

                List<PddlTerm> newPreconds = new ArrayList<>();
                List<PddlTerm> newDelEff = new ArrayList<>();
                for (Transition<FsmNode, EpsilonTransition> trans : fsm.getInverseTransitions(action)) {
                    PddlTerm mergeFact = getMergeFact(newMergeFacts, trans.getToState());
                    newPreconds.add(mergeFact);
                    if (!trans.getOperator().isEpsilon) {
                        newDelEff.add(mergeFact);
                    }
                }
                newPreconditions.put(action.action, newPreconds);
                newDelEffects.put(action.action, newDelEff);

                List<PddlTerm> newEff = new ArrayList<>();
                for (Transition<FsmNode, EpsilonTransition> trans : fsm.getTransitions(action)) {
                    PddlTerm mergeFact = getMergeFact(newMergeFacts, trans.getToState());
                    newEff.add(mergeFact);
                }
                newAddEffects.put(action.action, newEff);
            }
        }

        if (fsm.containsState(INIT_STATE)) {
            for (Transition<FsmNode, EpsilonTransition> trans : fsm.getTransitions(INIT_STATE)) {
                PddlTerm term = getMergeFact(newMergeFacts, trans.getToState());
                newInitMergeFacts.add(term);
            }
        }

        newSharedFacts.addAll(newMergeFacts.values());
    }

    private PddlTerm getMergeFact(Map<PddlTerm, PddlTerm> newMergeFacts, FsmNode node) {
        FsmNodeTerm termNode = (FsmNodeTerm) node;
        PddlTerm mergeFact = newMergeFacts.get(termNode.term);
        if (mergeFact == null) {
            mergeFact = new PddlTerm(new PddlName("merge_"+agentName+"_"+(mergeFactIndex++)));
            newMergeFacts.put(termNode.term, mergeFact);
        }
        return mergeFact;
    }

    private void addActionToFsm(FiniteStateMachine<FsmNode, EpsilonTransition> fsm, PddlAction action) {
        FsmNodeAction stateAction;

        stateAction = new FsmNodeAction(action, "");

        fsm.addState(stateAction);

        for (PddlTerm term : action.precondition.positives) {
            if (allEffects.contains(term) && !isPublicTerm(term)) {

                FsmNodeTerm stateTerm = new FsmNodeTerm(term, false);
                if (!fsm.containsState(stateTerm)) {
                    fsm.addState(stateTerm);
                }

                boolean isEpsilon = !action.effect.negatives.contains(term);

                fsm.addTransition(stateTerm, EpsilonTransition.createPreconditionTransition(isEpsilon), stateAction);
            }
        }

        for (PddlTerm term : action.effect.positives) {
            if (allEffects.contains(term) && !isPublicTerm(term)) {

                FsmNodeTerm stateTerm = new FsmNodeTerm(term, false);

                fsm.addTransition(stateAction, EpsilonTransition.createEffectTransition(), stateTerm);
            }
        }

        if (fsm.getInverseTransitions(stateAction).isEmpty() && fsm.getTransitions(stateAction).isEmpty()) {
            fsm.removeState(stateAction);
        }
    }

    protected void logFsm(FiniteStateMachine<FsmNode, EpsilonTransition> fsm, String nameSuffix) {
        if (!nameSuffix.isEmpty()) {
            nameSuffix = "-" + nameSuffix;
        }
        if (Settings.DOT_CAUSAL_GRAPH) {
            outputFsm(fsm, "fsmRisky"+(++imageCounter)+nameSuffix);
        }
    }

    public void logFsm(String nameSuffix) {
        logFsm(fsm, nameSuffix);
    }

    private void logFsm() {
        logFsm(fsm, "");
    }

    private static void outputFsm(FiniteStateMachine<FsmNode, EpsilonTransition> fsm, String filename) {
        FiniteStateMachineTools.imgOutput(fsm, filename, new LabelFactory<FsmNode, EpsilonTransition>() {

            @Override
            public String createStateLabel(FsmNode state) {
                if (state instanceof FsmNodeAction) {
                    return state.toString() + "\" shape =\"box";
                } else {
                    return state.toString();
                }
            }

            @Override
            public String createOperatorLabel(EpsilonTransition operator) {
                return operator.toString().replace("OR", "ADD").replace("AND", "PRE");
            }
        });
    }

    private static final Map<String, String> depotsAgentMap;
    static
    {
        depotsAgentMap = new HashMap<String, String>();
        depotsAgentMap.put("depot", "Depot");
        depotsAgentMap.put("distributor", "Depot");
        depotsAgentMap.put("truck", "Truck");
    }

    public static void main(String[] args) throws Exception {

        Settings.DOT_CAUSAL_GRAPH = false;
        Map<String, MultiAgentProblem> tasks = PsmFmapBenchmarks.createBenchmarkTasks();
        for (Entry<String, MultiAgentProblem> problemEntry : tasks.entrySet()) {
//            System.out.println(problemEntry.getKey());
            MultiAgentProblem maProblem = new MaStripsMultiAgentProblem(problemEntry.getValue());
          for (String agent : Arrays.asList("driver2")) {
//            for (String agent : maProblem.getAgents()) {

//                String agent = "slow0_0";
//                String agent = "rover1";
                PddlProblem problem = maProblem.getProblems().get(agent);
//        PddlDomain domain = new PddlDomain(new FileReader(new File("experiments/1/iter-001-domain.pddl")));
//        PddlProblem problem = new PddlProblem(domain, new FileReader(new File("experiments/1/iter-001-problem.pddl")));
//        for (PddlAction action : domain.actions) {
//            if (action.name.startsWith("pub")) {
//                action.setActionType(ActionType.PUBLIC);
//            } else {
//                action.setActionType(ActionType.INTERNAL);
//            }
//        }
          System.out.println("=== " + problemEntry.getKey() + " ===");
                try {
                    InternalDependenciesGraph id = new InternalDependenciesGraph("node1", problem);
                    id.init();
//                  System.out.println(problemEntry.getKey() + ": " + agent + ": Problem can be converted ####" );
                    id.countInternalDependencies();
                    id.updateProblem(problem);

                    if (id.isFullyReduced()) {
                        System.out.println(": Problem can be converted ####" );
                    } else {
                        System.out.println(": Problem cannot be converted @@@@" );
                    }


                } catch (Exception ex) {
//                    ex.printStackTrace();
                  System.out.println(": " + ex.getMessage() );
//                    System.out.println(problemEntry.getKey() + ": " + agent + ": " + ex.getMessage() );
                }
            }
//            System.out.println("#################");
//            System.out.println("imageCounter: " + imageCounter);
        }        
    }

    public boolean isFullyReduced() {
        return isFullyReduced;
    }

    public PddlProblem createConvertedProblem(PddlAction goalAction) {
        PddlDomain domain = new PddlDomain();
        PddlProblem problem = new PddlProblem(domain);
        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeTerm) {
                PddlPredicateType type = new PddlPredicateType(((FsmNodeTerm)node).term.name);
                domain.predicateTypes.put(type.predicateName, type);
            } else {
                FsmNodeAction actionNode = ((FsmNodeAction)node);
                if (actionNode.action.equals(INIT_ACTION)) {
                    problem.init.positives.addAll(getPositiveEffectTerms(actionNode));
                } else if (actionNode.action.equals(goalAction)) {
                    problem.goal.positives.addAll(getPreconditionTerms(actionNode));
                } else {
                    PddlAction action = new PddlAction(
                            actionNode.action.name,
                            new PddlTypeMap<PddlName>(),
                            new PddlCondition(getPreconditionTerms(actionNode), new ArrayList<PddlTerm>()),
                            new PddlCondition(getPositiveEffectTerms(actionNode), getNegativeEffectTerms(actionNode)),
                            ActionType.PARSED
                            );
                    domain.actions.add(action);
                }
            }
        }

        return problem;
    }

    private List<PddlTerm> getPreconditionTerms(FsmNodeAction actionNode) {
        List<PddlTerm> terms = new ArrayList<>();
        for (Transition<FsmNode, EpsilonTransition> trans : fsm.getInverseTransitions(actionNode)) {
            terms.add(((FsmNodeTerm)trans.getToState()).term);
        }
        return terms;
    }

    private List<PddlTerm> getPositiveEffectTerms(FsmNodeAction actionNode) {
        List<PddlTerm> terms = new ArrayList<>();
        for (Transition<FsmNode, EpsilonTransition> trans : fsm.getTransitions(actionNode)) {
            terms.add(((FsmNodeTerm)trans.getToState()).term);
        }
        return terms;
    }

    private List<PddlTerm> getNegativeEffectTerms(FsmNodeAction actionNode) {
        List<PddlTerm> terms = new ArrayList<>();
        for (Transition<FsmNode, EpsilonTransition> trans : fsm.getInverseTransitions(actionNode)) {
            if (!trans.getOperator().isEpsilon) {
                terms.add(((FsmNodeTerm)trans.getToState()).term);
            }
        }
        return terms;
    }
}
