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
import java.util.Set;

import cz.agents.dimap.Settings;
import cz.agents.dimap.fsm.EpsilonOperator;
import cz.agents.dimap.fsm.FiniteStateMachine;
import cz.agents.dimap.fsm.FiniteStateMachineTools;
import cz.agents.dimap.fsm.LabelFactory;
import cz.agents.dimap.fsm.Transition;
import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlCondition;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.ma.ActionType;
import cz.agents.dimap.tools.pddl.types.PddlPredicateType;
import cz.agents.dimap.tools.pddl.types.PddlTypeMap;
import experiments.psm.PsmFmapBenchmarks;
import experiments.psm.fmap.MaStripsMultiAgentProblem;

public class InternalDependenciesAndOr implements InternalDependencies {

    private final PddlAction INIT_ACTION;
    private final List<FsmNodeAction> INIT_STATES = new ArrayList<>();
    public PddlProblem problem;
    public Collection<PddlAction> actions;

    public FiniteStateMachine<FsmNode, EpsilonTransition> fsm;

    Collection<PddlTerm> allEffects = new ArrayList<>();

    Map<FsmNodeAction, FsmNodeAction> actionMap = new HashMap<>();

    Map<PddlAction, List<PddlTerm>> newPreconditions = new HashMap<>();
    Map<PddlAction, List<PddlTerm>> newDelEffects = new HashMap<>();
    Map<PddlAction, List<PddlTerm>> newAddEffects = new HashMap<>();
    Collection<PddlTerm> newInitMergeFacts = new HashSet<>();
    static int mergeFactIndex = 0;

    static int idFactCounter=0;
    
    static int imageCounter=0;
    
    public InternalDependenciesAndOr(PddlProblem problem) {
        actions = problem.getDomain().actions;
        this.problem = problem;
        
        INIT_ACTION = new PddlAction("INIT", new PddlTypeMap<PddlName>(), new PddlCondition(), new PddlCondition(problem.init), ActionType.PUBLIC);
//        INIT_STATE = new FsmNodeAction(INIT_ACTION, "EFF");

        countInternalDependencies();
    }

    private void countInternalDependencies() {
      
        for (PddlAction pddlAction : actions) {
            allEffects.addAll(pddlAction.getEffectFacts());
        }
        
        FsmNodeAction INIT_STATE = new FsmNodeAction(INIT_ACTION, "EFF");
        fsm = new FiniteStateMachine<FsmNode, EpsilonTransition>(INIT_STATE);
        fsm.removeState(INIT_STATE);
        
        addInitActionToFsm();

        for (PddlAction action : actions) {
            addActionToFsm(action);
        }

        checkIsProblemPubliclySolvable(problem);

        logFsm();

//        removeInternalFactsWithoutPublicPreAction(fsm);

//        logFsm();

//        removeDisconnectedNodes();

//        logFsm();

//        mergeEquivalentFacts();
        
//        logFsm();
        
        solveAndOrDependencies();

        logFsm();

        while(mergeInEquivalentFacts());

        logFsm();
        
//        addLinksFromInit();

//        logFsm();
        
        prepareTable();
    }

    private void addInitActionToFsm() {

        for (PddlTerm term : INIT_ACTION.effect.positives) {
            if (allEffects.contains(term) && !problem.domain.sharedPredicates.objects.contains(term)) {
                FsmNodeAction stateActionEff = new FsmNodeAction(PddlAction.createRenamedAction("INIT-"+term, INIT_ACTION), "");
                fsm.addState(stateActionEff);
                INIT_STATES.add(stateActionEff);
                FsmNodeTerm stateTerm = new FsmNodeTerm(term, problem.domain.sharedPredicates.objects.contains(term));
    
                fsm.addTransition(stateActionEff, EpsilonTransition.createEffectTransition(), stateTerm);
            }
        }
    }

    private void solveAndOrDependencies() {
        Map<FsmNodeAction, AndOrTree> trees = new HashMap<>();
        logFsm();
        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeAction) {
                FsmNodeAction actionNode = (FsmNodeAction)node;
                if ( actionNode.action.isPublic() ) {
                    try {
                        FiniteStateMachine<FsmNode, EpsilonTransition> fsmCopy = fsm.clone();
                        fsmCopy.removeStatesNotLeadingTo(Arrays.asList(node));
//                        logFsm(fsmCopy, node.toString());
                        AndOrTree tree = new AndOrTree(fsmCopy, (FsmNodeAction)node);
//                        System.out.println("actionNode.action.name: " + actionNode.action.name);
//                        System.out.println("tree: " + tree);
                        tree.toCnf();
//                        System.out.println("tree cnf: " + tree);
                        trees.put(actionNode, tree);
                    } catch (IllegalStateException e) {
                        // ok - no dependencies
                    }
                }
            }
        }
        FsmNodeAction INIT_STATE = new FsmNodeAction(INIT_ACTION, "EFF");
        fsm = new FiniteStateMachine<FsmNode, EpsilonTransition>(INIT_STATE);
        fsm.removeState(INIT_STATE);
        for (Entry<FsmNodeAction, AndOrTree> entry : trees.entrySet()) {
            FsmNodeAction actionNode = entry.getKey();
            if (!actionNode.action.isPublic()) {
                continue;
            }
            AndOrTree tree = entry.getValue();
            fsm.addState(actionNode);
            for (int i=0; i<tree.root.children.size(); i++) {
                Node node = tree.root.children.get(i);
                boolean isEpsilon = tree.root.isEpsilon.get(i);
                PddlTerm idTerm = new PddlTerm(new PddlName("idf_"+(idFactCounter++)));
                FsmNodeTerm idNode = new FsmNodeTerm(idTerm, problem.domain.sharedPredicates.objects.contains(idTerm));
                fsm.addState(idNode);
                fsm.addTransition(idNode, EpsilonTransition.createPreconditionTransition(isEpsilon), actionNode);
                
                for (Node child : node.children) {
                    FsmNodeAction depAction = ((Leaf)child).label;
                    fsm.addState(depAction);
                    fsm.addTransition(depAction, EpsilonTransition.createEffectTransition(), idNode);
                }
            }
        }
    }

    private void removeAllVirtualEffectsButOne(FiniteStateMachine<FsmNode, EpsilonTransition> fsm, PddlAction action) {
        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeAction && fsm.getInverseTransitions(node).isEmpty()) {
                PddlAction nodeAction = ((FsmNodeAction) node).action;
                if (!nodeAction.equals(action)) {
                    List<Transition<FsmNode, EpsilonTransition>> removeTrans = new ArrayList<>();
                    for (Transition<FsmNode, EpsilonTransition> trans : fsm.getTransitions(node)) {
                        if (trans.getOperator().isEpsilon()) {
                            removeTrans.add(trans);
                        }
                    }
                    for (Transition<FsmNode, EpsilonTransition> transition : removeTrans) {
                        fsm.getTransitions(node).remove(transition);
                        fsm.getInverseTransitions(transition.getToState()).remove(new Transition<FsmNode, EpsilonTransition>(transition.getOperator(), node));
                    }
                }
            }
        }
    }

    public Collection<PddlTerm> updateProblem(PddlProblem problem) {
        Collection<PddlTerm> newTerms = new HashSet<>();
        for (PddlAction action : problem.domain.actions) {
//            action.effect.negatives.removeAll(commonTerms);
//            action.effect.positives.removeAll(commonTerms);
            
//            System.out.println("action - remove: " + action);

            
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
            
            
//            System.out.println("action: " + action);
            
            List<PddlTerm> commonTerms = new LinkedList<>(action.effect.negatives);
            commonTerms.retainAll(action.effect.positives);
            for (PddlTerm term : commonTerms) {
                if (term.toString().startsWith("merge_")) {
                    action.effect.negatives.remove(term);
                    action.effect.positives.remove(term);
                }
            }
//            if (!commonTerms.isEmpty()) {
//                System.out.println("precondition without delete effect: " + action + " -- " + commonTerms); 
//                action.effect.negatives.removeAll(commonTerms);
//                action.effect.positives.removeAll(commonTerms);
//                System.out.println("action: " + action);
//            }
            
//            System.out.println("action - remove: " + action);
        }
        problem.init.positives.addAll(newInitMergeFacts);
        for (PddlTerm term : newTerms) {
            problem.domain.predicateTypes.put(term.name, new PddlPredicateType(term.name));
        }
        problem.domain.sharedPredicates.objects.addAll(newTerms);
        return newTerms;
    }

    private void prepareTable() {
        Map<PddlTerm, PddlTerm> newMergeFacts = new HashMap<>();

        for (Entry<FsmNodeAction, FsmNodeAction> entry : actionMap.entrySet()) {
            FsmNodeAction preAction = entry.getKey();
            FsmNodeAction effAction = entry.getValue();

            if (fsm.containsState(preAction)) {
                List<PddlTerm> newPreconds = new ArrayList<>();
                List<PddlTerm> newDelEff = new ArrayList<>();
                for (Transition<FsmNode, EpsilonTransition> trans : fsm.getInverseTransitions(preAction)) {
                    PddlTerm mergeFact = getMergeFact(newMergeFacts, trans.getToState());
                    newPreconds.add(mergeFact);
                    if (!trans.getOperator().isEpsilon) {
                        newDelEff.add(mergeFact);
                    }
                }
                newPreconditions.put(preAction.action, newPreconds);
                newDelEffects.put(effAction.action, newDelEff);
            }

            if (fsm.containsState(effAction)) {
                List<PddlTerm> newEff = new ArrayList<>();
                for (Transition<FsmNode, EpsilonTransition> trans : fsm.getTransitions(effAction)) {
                    PddlTerm mergeFact = getMergeFact(newMergeFacts, trans.getToState());
                    newEff.add(mergeFact);
                }
                newAddEffects.put(effAction.action, newEff);
            }
        }

        for (FsmNodeAction initState : INIT_STATES) {
            if (fsm.containsState(initState)) {
                for (Transition<FsmNode, EpsilonTransition> trans : fsm.getTransitions(initState)) {
                    PddlTerm term = getMergeFact(newMergeFacts, trans.getToState());
                    newInitMergeFacts.add(term);
                }
            }
        }
    }

    private PddlTerm getMergeFact(Map<PddlTerm, PddlTerm> newMergeFacts, FsmNode node) {
        FsmNodeTerm termNode = (FsmNodeTerm) node;
        PddlTerm mergeFact = newMergeFacts.get(termNode.term);
        if (mergeFact == null) {
            mergeFact = new PddlTerm(new PddlName("merge_"+(mergeFactIndex++)));
            newMergeFacts.put(termNode.term, mergeFact);
        }
        return mergeFact;
    }

    private void mergeEquivalentFacts() {
        while(mergeInEquivalentFacts());
    }
    
    private boolean mergeInEquivalentFacts() {
        Collection<FsmNode> processed = new ArrayList<>();
        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeTerm) {
                FsmNode equivNode = getEquivIn(node, processed);
                if (equivNode != null) {
                    for (Transition<FsmNode, EpsilonTransition> trans : fsm.getTransitions(node)) {
                        if (!fsm.getTransitions(equivNode).contains(trans)) {
                            fsm.addTransition(equivNode, trans.getOperator(), trans.getToState());
                        }
                    }
                    fsm.removeState(node);
                    return true;
                }
                processed.add(node);
            }
        }
        return false;
    }

    private FsmNode getEquivIn(FsmNode node, Collection<FsmNode> candidates) {
        for (FsmNode fsmNode : candidates) {
            if (equalSets(fsm.getInverseTransitions(node), fsm.getInverseTransitions(fsmNode))) {
                return fsmNode;
            }
        }
        return null;
    }

    private static boolean equalSets(
            Collection<Transition<FsmNode, EpsilonTransition>> trans1,
            Collection<Transition<FsmNode, EpsilonTransition>> trans2) {
        if (trans1.size() != trans2.size()) {
            return false;
        }
        return trans1.containsAll(trans2);
    }

//    private void addLinksFromInit() {
//        //      remove links from init to acitons that are not internally relax. reachable
//        PddlProblem internalProblem = problem.clone();
//        removePublicActions(internalProblem);
//        Set<PddlTerm> reachableFacts = RelaxedPlanningGraph.extractReachableFacts(internalProblem);
//        fsm.addState(INIT_STATE);
//        for (FsmNode node : fsm.getStates()) {
//            if (node instanceof FsmNodeAction) {
//                FsmNodeAction actionNode = (FsmNodeAction)node;
//                PddlAction action = actionNode.action;
//                if (actionNode.suffix.equals("PRE")) {
//                    if (containsAllInternalPreconditions(reachableFacts, action)) {
//                        fsm.addTransition(INIT_STATE, new EpsilonTransition("SUP-INIT", false), node);
//                    }
//                }
//            }
//        }
//    }

  private boolean containsAllInternalPreconditions(Set<PddlTerm> reachableFacts, PddlAction action) {
      for (PddlTerm term : action.precondition.positives) {
          if (!problem.domain.sharedPredicates.objects.contains(term)) {
              if (!reachableFacts.contains(term)) {
                  return false;
              }
          }
      }
      return true;
  }

    
    private void removeInternalFactsWithoutPublicPreAction(FiniteStateMachine<FsmNode, EpsilonTransition> fsm) {
        Collection<FsmNode> toRemove = new HashSet<>();

        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeTerm) {
                if (!hasPublicPreActions(node, new HashSet<FsmNode>())) {
                    toRemove.add(node);
                }
            }
        }

        for (FsmNode node : toRemove) {
            fsm.removeState(node);
        }
    }

    private boolean hasPublicPreActions(FsmNode node, HashSet<FsmNode> visited) {
        if (visited.contains(node)) {
            return false;
        }
        visited.add(node);
        
        for (Transition<FsmNode, EpsilonTransition> invTrans : fsm.getInverseTransitions(node)) {
            FsmNode preNode = invTrans.getToState();
            if ((preNode instanceof FsmNodeAction) && (((FsmNodeAction)preNode).action.isPublic())) {
                return true;
            }
            if (hasPublicPreActions(preNode, visited)) {
                return true;
            }
        }
        return false;
    }

    private void removeDisconnectedNodes() {
        Collection<FsmNode> toRemove = new HashSet<>();

        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeAction) {
                if (((FsmNodeAction)node).action.isPublic()) {
                    if (fsm.getTransitions(node).isEmpty() && fsm.getInverseTransitions(node).isEmpty()) {
                        toRemove.add(node);
                    }
                } else {
                    if (fsm.getTransitions(node).isEmpty()) {
                        toRemove.add(node);
                    }
                }
            }
            
        }
        for (FsmNode node : toRemove) {
            fsm.removeState(node);
        }
    }

    private void removePublicActions(PddlProblem internalProblem) {
        List<PddlAction> toRemove = new ArrayList<PddlAction>();
        for (PddlAction action : internalProblem.domain.actions) {
            if (action.isPublic()) {
                toRemove.add(action);
            }
        }
        internalProblem.domain.actions.removeAll(toRemove);
    }

    private void addActionToFsm(PddlAction action) {
        FsmNodeAction stateActionPre;
        FsmNodeAction stateActionEff;

        if (action.isPublic()) {
            stateActionPre = new FsmNodeAction(action, "PRE");
            stateActionEff = new FsmNodeAction(action, "EFF");
            actionMap.put(stateActionPre, stateActionEff);
        } else {
            stateActionPre = new FsmNodeAction(action, "");
            stateActionEff = stateActionPre;
        }

        fsm.addState(stateActionPre);
        fsm.addState(stateActionEff);
        
        for (PddlTerm term : action.precondition.positives) {
            if (allEffects.contains(term) && !problem.domain.sharedPredicates.objects.contains(term)) {

                FsmNodeTerm stateTerm = new FsmNodeTerm(term, problem.domain.sharedPredicates.objects.contains(term));
                if (!fsm.containsState(stateTerm)) {
                    fsm.addState(stateTerm);
                }
                
                boolean isEpsilon = !action.effect.negatives.contains(term);
                
                fsm.addTransition(stateTerm, EpsilonTransition.createPreconditionTransition(isEpsilon), stateActionPre);
                
//                if (!action.effect.negatives.contains(term)) {
//                    if (action.isPublic()) {
//                        fsm.addTransition(stateActionEff, new EpsilonTransition("OR", true), stateTerm);
//                    } else {
//                        System.err.println("precondition without delete effect: " + action.name + " -- " + term);
//                    }
//                }
            }
        }

        for (PddlTerm term : action.effect.positives) {
            if (allEffects.contains(term) && !problem.domain.sharedPredicates.objects.contains(term)) {

                FsmNodeTerm stateTerm = new FsmNodeTerm(term, problem.domain.sharedPredicates.objects.contains(term));
    
                fsm.addTransition(stateActionEff, EpsilonTransition.createEffectTransition(), stateTerm);
            }
        }

        if (fsm.getInverseTransitions(stateActionPre).isEmpty()) {
            fsm.removeState(stateActionPre);
        }

        if (fsm.getTransitions(stateActionEff).isEmpty()) {
            fsm.removeState(stateActionEff);
        }
    }

    public int getNumberOfIdActions() {
        int numberOfActions = 0;
        for (FsmNode node : fsm.getStates()) {
            if (!fsm.getInverseTransitions(node).isEmpty()) {
                numberOfActions++;
            }
        }
        return numberOfActions;
    }

    public boolean containsPotentiallyRiskyActions() {
//        for (Transition<FsmNode, EpsilonTransition> node : fsm.getTransitions(INIT_STATE)) {
//            if (fsm.getInverseTransitions(node.getToState()).size() > 1) {
//                return true;
//            }
//        }
        return false;
    }

    public boolean containsComplicatedInternalDependencies() {
        for (FsmNode node : fsm.getStates()) {
            if (!fsm.getInverseTransitions(node).isEmpty()) {
                for (Transition<FsmNode, EpsilonTransition> trans : fsm.getInverseTransitions(node)) {
                    if (!fsm.getInverseTransitions(trans.getToState()).isEmpty() || !fsm.getTransitions(node).isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void logFsm(FiniteStateMachine<FsmNode, EpsilonTransition> fsm, String nameSuffix) {
        if (Settings.DOT_CAUSAL_GRAPH) {
            outputFsm(fsm, "fsmRisky"+(++imageCounter)+nameSuffix);
        }
    }

    public void logFsm() {
        logFsm(fsm, "");
    }

    public static void outputFsm(FiniteStateMachine<FsmNode, EpsilonTransition> fsm, String filename) {
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
                return operator.toString();
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
        
//        Settings.DOT_CAUSAL_GRAPH = true;
        Map<String, MultiAgentProblem> tasks = PsmFmapBenchmarks.createBenchmarkTasks();
        for (Entry<String, MultiAgentProblem> problemEntry : tasks.entrySet()) {
            System.out.println(problemEntry.getKey());
//        MultiAgentProblem mp =
//                new FmapMultiDomainVariedInfo("depots", 20, "DomainDepots", "ProblemDepots", depotsAgentMap, true)
//                        .createProblem(1);
//              new FmapMultiDomainBijectiveInfo("woodworking", 30, "DomainWoodworking", "ProblemWoodworkingag", Arrays.asList("Grinder", "Planner", "Saw", "Varnisher")).createProblem(1);

//                new FmapSingleDomainInfo("logistics", 20, "DomainLogistics", "ProblemLog").createProblem(5); 
            MultiAgentProblem maProblem = new MaStripsMultiAgentProblem(problemEntry.getValue());
//          PddlProblem problem = maProblem.toSingleAgentProblem();
//            PddlProblem problem = maProblem.getProblems().get("grinder");
//            System.out.println("problem.domain.sharedPredicates: " + problem.domain.sharedPredicates);
//          System.out.println("shared predicates: " + problem.domain.sharedPredicates.objects);
//          System.out.println("problem: " + problem.domain);
          for (String agent : maProblem.getAgents()) {
              PddlProblem problem = maProblem.getProblems().get(agent);
            try {
                InternalDependenciesAndOr id = new InternalDependenciesAndOr(problem);
                System.out.println(problemEntry.getKey() + ": " + agent + ": Problem can be converted ####" );

            } catch (Exception ex) {
                System.out.println(problemEntry.getKey() + ": " + agent + ": " + ex.getMessage() );
            }
          }
//            DotTools.dotInternalDependenciesAndOr(id, "logistics-cg");

//            outputFsm(id.fsm, "fsmRiskyA");

//            id.updateProblem(problem);
//            System.out.println("domain: " + problem.domain);
//            System.out.println("problem.init: " + problem.init);
            
//            InternalDependenciesAndOr id2 = new InternalDependenciesAndOr(problem);
            
//            outputFsm(id2.fsm, "fsmRiskyB");

//            System.out.println("id.fsm: actions: " + id.fsm.getNumberOfStates() + " dependencies: " + id.fsm.getNumberOfTransitions());
/*            
            if (id.containsPotentiallyRiskyActions()) {
                System.out.println("Problem contains potentially risky actions!");
            }
            for (FsmNode node : id.fsm.getStates()) {
                PddlAction action = ((FsmNodeAction) node).action;
                List<PddlAction> replActions = id.getReplacement(action);
                System.out.print(action.name + " -> [");
                if (replActions != null) {
                    for (PddlAction replAction : replActions) {
                        System.out.print(replAction.name + ", ");
                    }
                } else {
                    System.out.print("---");
                }
                System.out.println("]");
            }
*/    
            System.out.println("#################");
        }        

    }

    private void checkIsProblemPubliclySolvable(PddlProblem problem) {
        for (PddlAction pddlAction : problem.domain.actions) {
            if (!pddlAction.isPublic()) {
                boolean isFalse = false;
                for (PddlTerm term : pddlAction.precondition.positives) {
                    if (problem.domain.sharedPredicates.objects.contains(term)
                            || !allEffects.contains(term)) {
                        continue;
                    }
                    if (!pddlAction.effect.negatives.contains(term)) {
                        isFalse = true;
//                        System.err.println( "  !!! " + pddlAction.getName() + " -- " + term);
                    }
                }
                if (isFalse) {
                    throw new UnsupportedOperationException("Problem cannot be converted (internal action without deleted precondition).");
                }
            }
        }

        for (FsmNode node : fsm.getStates()) {
            if (node instanceof FsmNodeTerm) {
                PddlTerm term = ((FsmNodeTerm)node).term; 
                boolean hasInternalConsequence = false;
                boolean hasPublicConsequenceWithoutDelete = false;
                for (Transition<FsmNode, EpsilonTransition> trans : fsm.getTransitions(node)) {
                    PddlAction action = ((FsmNodeAction)trans.getToState()).action;
                    boolean isDeleted = action.effect.negatives.contains(term);
                    boolean isPublic = action.isPublic();
                    if (isPublic) {
                        if (!isDeleted) {
//                            System.err.println( "  !p " + action.getName() + " -- " + term);
                            hasPublicConsequenceWithoutDelete = true;
                        }
                    } else {
                        hasInternalConsequence = true;
                    }
                }
                if (hasInternalConsequence && hasPublicConsequenceWithoutDelete) {
                    throw new UnsupportedOperationException("Problem cannot be converted (internal fact with both internal delete and public non-delete actions): " + node);
                }
            }
        }
    }

}
