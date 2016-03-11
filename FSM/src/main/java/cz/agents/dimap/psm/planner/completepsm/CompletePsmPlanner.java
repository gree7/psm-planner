package cz.agents.dimap.psm.planner.completepsm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cz.agents.dimap.Settings;
import cz.agents.dimap.fsm.FiniteStateMachineTools;
import cz.agents.dimap.fsm.GoalCondition;
import cz.agents.dimap.fsm.LabelFactory;
import cz.agents.dimap.fsm.Transition;
import cz.agents.dimap.psm.PlanStateMachineTools;
import cz.agents.dimap.psm.operator.Operator;
import cz.agents.dimap.psm.operator.SimpleOperator;
import cz.agents.dimap.psm.state.SimpleState;
import cz.agents.dimap.psm.state.State;
import cz.agents.dimap.tools.Pair;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;

public class CompletePsmPlanner {
    
    static boolean ALLOW_VOID_ACTIONS = true;
    static boolean ALLOW_UNPREFFERED_PSMS = true;
    static boolean USE_PSM_REDUCTION = true;

    private static int imageCounter = 0;

    public static List<String> plan(PddlProblem problem) {

        PsmCollection psms = new PsmCollection();

        List<PddlName> facts = new ArrayList<PddlName>(problem.domain.predicateTypes.keySet());
        Collections.sort(facts);
        for (PddlName fact : facts) {
            if (!problem.domain.predicateTypes.get(fact).arguments.isEmpty()) {
                throw new IllegalArgumentException();
            }
            psms.addPsm(createPsm(problem, fact));
        }

        PlanStateMachineWithVoidActions resultPsm = intersectAll(psms);

        List<String> solution = resultPsm.getShortestPlan();

        return solution;
    }

    private static PlanStateMachineWithVoidActions createPsm(PddlProblem problem, final PddlName fact) {

        State factState = new SimpleState(fact.name);
        State nonFactState = new SimpleState("non-"+fact.name);

//        State factState = new State() {
//            @Override
//            public String getName() {
//                return fact.name;
//            }
//
//            @Override
//            public BitSet getBitSet() {
//                return null;
//            }
//            @Override
//            public String toString() {
//                return getName();
//            }};
//            
//        State nonFactState = new State() {
//            @Override
//            public String getName() {
//                return "non-"+fact.name;
//            }
//
//            @Override
//            public BitSet getBitSet() {
//                return null;
//            }
//            @Override
//            public String toString() {
//                return getName();
//            }};

        State initState;
        State nonInitState;
        if (isInTerms(fact, problem.init.positives)) {
            initState = factState;
            nonInitState = nonFactState;
        } else {
            initState = nonFactState;
            nonInitState = factState;
        }
        PlanStateMachineWithVoidActions psm = new PlanStateMachineWithVoidActions(initState);
        psm.addState(nonInitState);

        boolean areStatesConnected = false; 

        Collection<String> voidActions = new HashSet<>(); 
        Collection<String> nonVoidActions = new HashSet<>(); 
        
        for (PddlAction action : problem.domain.actions) {
            Operator operator = new SimpleOperator(action.name); 
            nonVoidActions.add(action.name);
            if (isInTerms(fact, action.precondition.positives)) {
                if (isInTerms(fact, action.effect.negatives)) {
                    psm.addTransition(factState, operator, nonFactState);
                    areStatesConnected = true;
                } else {
                    psm.addTransition(factState, operator, factState);
                }
            } else {
                if (isInTerms(fact, action.effect.positives)) {
                    psm.addTransition(factState, operator, factState);
                    psm.addTransition(nonFactState, operator, factState);
                    areStatesConnected = true;
                } else if (isInTerms(fact, action.effect.negatives)) {
                    psm.addTransition(factState, operator, nonFactState);
                    psm.addTransition(nonFactState, operator, nonFactState);
                    areStatesConnected = true;
                } else {
                    if (ALLOW_VOID_ACTIONS) {
                        voidActions.add(action.name);
                        nonVoidActions.remove(action.name);
                    } else {
                        psm.addTransition(factState, operator, factState);
                        psm.addTransition(nonFactState, operator, nonFactState);                    
                    }
                }
            }
        }

        if (!areStatesConnected) {
            throw new IllegalArgumentException(fact.name);
        }

        psm.addGoal(factState);
        if (!isInTerms(fact, problem.goal.positives)) {
            psm.addGoal(nonFactState);
        }
        
        psm.voidActions = voidActions;
        psm.nonVoidActions = nonVoidActions;
        
        return psm;
    }

    private static PlanStateMachineWithVoidActions intersectAll(PsmCollection psms) {
        while (psms.size() > 1) {
            Collection<PlanStateMachineWithVoidActions> unprefferedPsms = new ArrayList<>();
            Collection<PlanStateMachineWithVoidActions> unprefferedResults = new ArrayList<>();
            boolean goodIntersection = false;
            
            PlanStateMachineWithVoidActions psm = null;
            long intersectionTime = System.currentTimeMillis();
            while (psms.size() > 1) {
                Pair<PlanStateMachineWithVoidActions, PlanStateMachineWithVoidActions> psmPair = psms.getPair(); 
                PlanStateMachineWithVoidActions psm1 = psmPair.getLeft();
                PlanStateMachineWithVoidActions psm2 = psmPair.getRight();
    
                logPsm(psm1, "psm1");
                logPsm(psm2, "psm2");
    
                System.out.println("PSM1 size: " + psm1.getNumberOfStates() + " (void actions: " + psm1.voidActions.size() + ")");
                System.out.println("PSM2 size: " + psm2.getNumberOfStates() + " (void actions: " + psm2.voidActions.size() + ")");
       
                psm = intersect(psm1, psm2, false);

                logPsm(psm, "intersection");

                System.out.println("intersection size: " + psm.getNumberOfStates());
                System.out.println("intersection goals: " + psm.getGoalStates().size());

                if (USE_PSM_REDUCTION && PsmReduction.process(psm, union(psms, unprefferedPsms))) {
                    logPsm(psm, "reduced");
                    System.out.println("reduced size: " + psm.getNumberOfStates());
                    System.out.println("reduced goals: " + psm.getGoalStates().size());
                    goodIntersection = true;
                } else if ( ALLOW_UNPREFFERED_PSMS 
                        && psm.getNumberOfStates() > 32
                        && psm.getNumberOfStates() > (psm1.getNumberOfStates() * psm2.getNumberOfStates()/2)) {
                    unprefferedPsms.add(psm1);
                    unprefferedPsms.add(psm2);
                    unprefferedResults.add(psm);
                    System.out.println("Unpreffered !");
                } else {
                    goodIntersection = true;
                    break;
                }
            }
            
            if (goodIntersection) {
                for (PlanStateMachineWithVoidActions unprefferedPsm : unprefferedPsms) {
                    psms.addPsm(unprefferedPsm);
                }
                psms.addPsm(psm);
            } else {
                for (PlanStateMachineWithVoidActions unprefferedResult : unprefferedResults) {
                    psms.addPsm(unprefferedResult);
                }
                System.out.println("Giving up!!!");
            }

            intersectionTime = System.currentTimeMillis() - intersectionTime;

            System.out.printf("intersection time: %.02fs%n", intersectionTime/1000.0);

            System.out.println("number of PSMs: " + psms.size());
        }
        return psms.findFirst(); 
    }

    private static Collection<PlanStateMachineWithVoidActions> union(PsmCollection psms,
            Collection<PlanStateMachineWithVoidActions> unprefferedPsms) {
        Collection<PlanStateMachineWithVoidActions> all = new ArrayList<>(psms.psms);
        all.addAll(psms.allGoalsPsms);
        all.addAll(unprefferedPsms);
        return all;
    }

    protected static void logPsm(PlanStateMachineWithVoidActions psm, String nameSuffix) {
        if (psm.getNumberOfStates() > 50) {
            return;
        }
        if (!nameSuffix.isEmpty()) {
            nameSuffix = "-" + nameSuffix;
        }
        if (Settings.DOT_CAUSAL_GRAPH) {
            outputFsm(psm, "psm"+(++imageCounter )+nameSuffix);
        }
    }

    private static void outputFsm(final PlanStateMachineWithVoidActions psm, String filename) {
        FiniteStateMachineTools.imgOutput(psm, filename, new LabelFactory<State, Operator>() {
            @Override
            public String createStateLabel(State state) {
                return "{"+state.toString()+"}";
            }

            @Override
            public String createOperatorLabel(Operator operator) {
                return operator.toString();
            }
        }, new GoalCondition<State>() {
            @Override
            public boolean isGoal(State state) {
                return psm.getGoalStates().contains(state);
            }
        });
    }

    private static boolean isInTerms(PddlName fact, List<PddlTerm> terms) {
        for (PddlTerm term : terms) {
            if (term.name.equals(fact)) {
                return true;
            }
        }
        return false;
    }

    static PlanStateMachineWithVoidActions intersect(PlanStateMachineWithVoidActions fsm1, PlanStateMachineWithVoidActions fsm2, boolean verbose){
        addVoidActions(fsm1, fsm2);
        addVoidActions(fsm2, fsm1);
        IntersectionState.clearCreatedStates();
        IntersectionState init = IntersectionState.create(fsm1.getInitState(), fsm2.getInitState(), fsm1, fsm2);
        PlanStateMachineWithVoidActions intersectedFsm = new PlanStateMachineWithVoidActions(init);
        Set<IntersectionState> openList = new HashSet<IntersectionState>();
        openList.add(init);
        Set<IntersectionState> closedList = new HashSet<IntersectionState>();

        HashSet<State> newGoals = new HashSet<State>();

        if (fsm1.getGoalStates().contains(fsm1.getInitState())
                && fsm2.getGoalStates().contains(fsm2.getInitState())) {
            newGoals.add(init);
        }
        
        while ( !openList.isEmpty() ) {
            Iterator<IntersectionState> tmpIterator = openList.iterator();
            IntersectionState is = tmpIterator.next();
            tmpIterator.remove();
            
            if ( verbose ) {
                System.out.print("O=" + openList.size() + ";C="+closedList.size()+";States="+intersectedFsm.getNumberOfStates()+";Transitions="+intersectedFsm.getNumberOfTransitions());
            }

            if (closedList.contains(is)) {
                if (verbose) {
                    System.out.println("already Expanded");
                }
                continue;
            } else {
                if ( verbose ) {
                    System.out.println("");
                }
            }
            closedList.add(is);
            Collection<Transition<State, Operator>> trans = is.expandState();

            for ( Transition<State, Operator> t : trans) {
                IntersectionState toState = (IntersectionState) t.getToState();
                intersectedFsm.addTransition(is, t.getOperator(), toState);

                if ( !openList.contains(toState) && !closedList.contains(toState)) {
                    openList.add(toState);

                    if (toState.fsm1.getGoalStates().contains(toState.parent1) 
                            && toState.fsm2.getGoalStates().contains(toState.parent2)) {
                        newGoals.add(toState);
                    }
                }
            }
            
            if (!Settings.DOT_CAUSAL_GRAPH) {
                is.parent1 = null;
                is.parent2 = null;
            }
        }

        IntersectionState.clearCreatedStates();

        intersectedFsm.setGoals(newGoals);
        //    intersectedFsm.setGoals( getIntersectedGoalStates(intersectedFsm, fsm1.getGoalStates(), fsm2.getGoalStates()));

        intersectedFsm.voidActions.addAll(fsm1.voidActions);

        intersectedFsm.nonVoidActions.addAll(fsm1.nonVoidActions);
        intersectedFsm.nonVoidActions.addAll(fsm2.nonVoidActions);

        return intersectedFsm;
    }

    private static void addVoidActions(PlanStateMachineWithVoidActions fsm1, PlanStateMachineWithVoidActions fsm2) {
        for (Iterator<String> iterator = fsm1.voidActions.iterator(); iterator.hasNext();) {
            String voidAction = iterator.next();
            if (!fsm2.voidActions.contains(voidAction)) {
                for (State state : fsm1.getStates()) {
                    fsm1.addTransition(state, new SimpleOperator(voidAction), state);
                }
                iterator.remove();
                fsm1.nonVoidActions.add(voidAction);
            }
        }
    }

    public static Collection<State> getIntersectedGoalStates(PlanStateMachineWithVoidActions fsm, Collection<State> goals1, Collection<State> goals2 ){
        Set<State> goals = new HashSet<>();
        for ( State s1 : goals1 ) {
            for ( State s2 : goals2 ) {
                State goal = PlanStateMachineTools.getIntersectedGoalState(fsm, s1.getName() + "-" + s2.getName());
                if ( goal != null ) {
                    goals.add(goal);
                }
            }
        }
        return goals;
    }
}
