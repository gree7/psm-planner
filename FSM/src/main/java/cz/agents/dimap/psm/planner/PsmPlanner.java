package cz.agents.dimap.psm.planner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cz.agents.dimap.Settings;
import cz.agents.dimap.Settings.InitialLandmarks;
import cz.agents.dimap.landmarks.Landmark;
import cz.agents.dimap.planset.PlanSet;
import cz.agents.dimap.planset.PlanSetType;
import cz.agents.dimap.psm.PlanStateMachine;
import cz.agents.dimap.psm.operator.Binding;
import cz.agents.dimap.psm.operator.Operator;
import cz.agents.dimap.tools.fd.Downward;
import cz.agents.dimap.tools.fd.DownwardException;
import cz.agents.dimap.tools.fd.DownwardOutOfMemoryException;
import cz.agents.dimap.tools.fd.RelaxedPlanCreator;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlActionInstance;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.types.PddlPredicateType;
import cz.agents.dimap.tools.pddl.types.PddlType;

public class PsmPlanner {

    private long parallelTimeMs = 0;
    private long durationMs;
    private int iteration = 0;
    private long startTimeMs;
    private Map<String, PsmPlannerAgent> planners = new LinkedHashMap<>();

	private List<List<String>> initialPlans = null;

    private long iterationMaxTimeMs = 0;
    private PlanSet<Operator> intersection = null;

    private PlanStateMachine agentPubProj;
    private String solutionPngPath;

    private List<Integer> numOfSentActions = new ArrayList<>();
    private List<Long> timeOfVerification = new ArrayList<>();
    private List<Long> timeOfPlanning = new ArrayList<>();
    private Map<String, ExternalActions> externalActions;
    private PddlProblem publicProblem;
    private PddlProblem relaxedProblem;
    private PddlProblem originalProblem;

    long duration = 0;
    Integer round = new Integer(0);
    boolean retValue = true;
    Exception thrownException = null;
    private static boolean isProblemPubliclySolvable;
    

    public PsmPlanner(MultiAgentProblem maProblem) throws Exception {
        createPlanners(maProblem);
        initInitialPlanLandmarks();
        initRequiredCausalLandmarks();
        solutionPngPath = maProblem.getImageOutputPath();
    }

    public Map<String, PsmPlannerAgent> getPlanners() {
		return planners;
	}
    
    public PlanningResult run(boolean runSequentially, long endTime) throws Exception {
        if (isProblemPubliclySolvable) {
            return new PlanningResult(parallelTimeMs, iteration,
                    initialPlans.get(0), planners.size(), numOfSentActions,
                    timeOfVerification, timeOfPlanning);
        }
        while (true) {
            if (Settings.MAX_ITERATIONS > 0
                    && Settings.MAX_ITERATIONS <= iteration) {
                System.out.println("Maximal number of iteration reached!");
                return null;
            }

            initNextIteration();

            if (!planAllAgents(runSequentially, endTime)) {
                return null; // timeout
            }

            if (isIntersectionNonEmpty()) {
                System.out.println("Solution found in iteration " + iteration);
                if (Settings.DOT_OUTPUT_SOLUTION) {
                    intersection.imgOutput(solutionPngPath);
                }
                if (Settings.DELETE_TMP_FILES_ON_EXIT) {
                    for (PsmPlannerAgent planner : planners.values()) {
                        planner.deleteTmpFiles();
                    }
                }
                return new PlanningResult(parallelTimeMs, iteration,
                        intersection.getRandomPlan(), planners.size(), numOfSentActions,
                        timeOfVerification, timeOfPlanning);
            } else {
                System.out.println("--> intersection is empty");
            }

            exchangeLandmarks();
        }
    }

    private void removeAgentsOwnMergeFacts(String agentName, PddlProblem problem) {
        ExternalActions extActions = externalActions.get(agentName);
        extActions.removeMergeFacts(problem);
    }

    private void createPlanners(MultiAgentProblem maProblem) throws Exception {
        System.out.println("Creating planners");

        externalActions = new HashMap<>();
        Map<String, PddlProblem> agentProblems = new HashMap<>();
        computeAgentProblems(maProblem, externalActions, agentProblems);
        
        Map<String, PddlProblem> problems = new HashMap<>();

        int agentIndex = 0;
        for (Entry<String, PddlProblem> problemEntry : agentProblems.entrySet()) {
            String agentName = problemEntry.getKey();
            System.out.println("--> agent planner index: " + agentName + " = "
                    + agentIndex);

            PddlProblem agentProblem = problemEntry.getValue();

            PddlProblem clonedProblem = agentProblem.clone();
            clonedProblem.domain.cloneActions();
            problems.put(agentName, clonedProblem);
            
            extendDomainWithPublicActions(agentName, agentProblem, externalActions);
            extendDomainWithNewInitFacts(agentProblem, externalActions);

            if (publicProblem == null) {
                publicProblem = PddlProblem.createPublicProjection(agentProblem);
            }
            
            if (Settings.REPLACE_INTERNALLY_DEPENDENT_ACTIONS) {
                removeAgentsOwnMergeFacts(agentName, agentProblem);
            }
            
            PsmPlannerAgent plannerAgent = new PsmPlannerAgent(agentName,
                    agentProblem, agentIndex++, externalActions.get(agentName).isPubliclySolvable());
            planners.put(agentName, plannerAgent);
        }
        
        PddlMultiAgentProblem pddlMultiAgentProblem = new PddlMultiAgentProblem(problems);
        originalProblem = pddlMultiAgentProblem.toSingleAgentProblem();
        relaxedProblem = pddlMultiAgentProblem.getOriginalRelaxedProblem();

        for (PsmPlannerAgent planner : planners.values()) {
            planner.initPlanVerifier();
        }
    }

    static public void extendDomainWithNewInitFacts(PddlProblem agentProblem, Map<String, ExternalActions> externalActionsMap) {
        for (ExternalActions externalActions : externalActionsMap.values()) {
            agentProblem.init.positives.addAll(externalActions.getNewInitFacts());
        }
    }

    class IsExtensible {
        boolean isExtensible = true;
    };

    private List<String> isPlanInternallyExtensibleParalelly(final List<String> plan, long endTime) throws InterruptedException, TimeoutException {
        final IsExtensible isExtensible = new IsExtensible();
        final List<List<String>> agentsPlans = new ArrayList<>(); 
        ExecutorService executor = Executors.newFixedThreadPool(Settings.THREAD_POOL_SIZE);
        for (final PsmPlannerAgent planner : planners.values()) {
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    if (Settings.VERBOSE) {
                        System.out.println("starting...");
                    }

                    try {
                        List<String> planExtension = planner.isPlanInternallyExtensible(plan);
                        if (planExtension == null) {
                            isExtensible.isExtensible = false;
                        } else {
                            agentsPlans.add(planExtension);
                        }
                    } catch (Exception e) {
                        System.err.println("ERROR: " + planner.agentName
                                + ".isPlanInternallyExtensible(...) failed for plan: "
                                + plan);
                        isExtensible.isExtensible = false;
                    }

                    System.out.println("... verification of "+ planner.agentName +" done: " + new Date());
                }});
        }
        executor.shutdown();
        if (!executor.awaitTermination(endTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS)) {
            System.out.println("killing tasks");
            executor.shutdownNow();

            try {
                if (Settings.VERBOSE) {
                    System.out.println("PsmPlanner: timeout: killing all!");
                }
                Runtime.getRuntime().exec("killall -KILL downward-1");
                Runtime.getRuntime().exec("killall -KILL downward-1-debug");
                Runtime.getRuntime().exec("killall -KILL polystar");
            } catch (IOException e) {
                e.printStackTrace();
            }

            throw new TimeoutException();
        }

        try {
            if (Settings.VERBOSE) {
                System.out.println("PsmPlanner: killing all!");
            }
            Runtime.getRuntime().exec("killall -KILL downward-1");
            Runtime.getRuntime().exec("killall -KILL downward-1-debug");
            Runtime.getRuntime().exec("killall -KILL polystar");
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> finalPlan = mergePlans(plan, agentsPlans);
        
        return finalPlan;
    }
    
    static public List<String> mergePlans(List<String> publicPlan, List<List<String>> agentsPlans) {
        List<String> finalPlan = new ArrayList<>();
        
        List<Iterator<String>> planIterators = new ArrayList<>();
        for (List<String> plan : agentsPlans) {
            planIterators.add(plan.iterator());
        }
        
        int pubStepCounter = 1;
        for (String pubAction : publicPlan) {
            int nextPubStepCounter = pubStepCounter;
            for (Iterator<String> iterator : planIterators) {
                int curStepCounter = pubStepCounter;
                while (true) {
                    String action = iterator.next();
                    if (pubAction.equals(action)) {
                        break;
                    }
                    addToFinalPlan(finalPlan, action, curStepCounter);
                    curStepCounter++;
                }
                if (curStepCounter > nextPubStepCounter) {
                    nextPubStepCounter = curStepCounter;
                }
            }

            pubStepCounter = nextPubStepCounter;
            addToFinalPlan(finalPlan, pubAction, pubStepCounter);
            pubStepCounter++;
        }
        return finalPlan;
    }

    static private void addToFinalPlan(List<String> plan, String action, int time) {
        plan.add("(" + action.replace("_", " ") + ")");
    }

    public static void computeAgentProblems(MultiAgentProblem maProblem,
            Map<String, ExternalActions> externalActionsMap,
            Map<String, PddlProblem> agentProblems) {
        List<PddlTerm> inMerges = new ArrayList<>();
        isProblemPubliclySolvable = true;
        for (Entry<String, PddlProblem> entry : maProblem.getProblems().entrySet()) {
            String agentName = entry.getKey();
            PddlProblem agentProblem = groundProblemForAgent(agentName, entry.getValue());
            ExternalActions externalActions = new ExternalActions(agentName, agentProblem);
            if (Settings.REPLACE_INTERNALLY_DEPENDENT_ACTIONS) {
                inMerges.addAll(externalActions.getInMerges());
            }
            if (!externalActions.isPubliclySolvable()) {
                isProblemPubliclySolvable = false;
            }
            agentProblems.put(agentName, agentProblem);
            externalActionsMap.put(entry.getKey(), externalActions);
        }
        
        if (Settings.REPLACE_INTERNALLY_DEPENDENT_ACTIONS) {
            for (PddlProblem problem : agentProblems.values()) {
                addAllInMergesToPublic(inMerges, problem);
            }
        }
    }

    public static void addAllInMergesToPublic(List<PddlTerm> inMerges,
            PddlProblem problem) {
        for (PddlTerm inMerge : inMerges) {
            problem.domain.predicateTypes.put(inMerge.name, new PddlPredicateType(inMerge.name));
            problem.domain.sharedPredicates.objects.add(inMerge);
        }
    }

    public static void extendDomainWithPublicActions(String agentName,
            PddlProblem agentProblem,
            Map<String, ExternalActions> externalActions) {
        for (Entry<String, ExternalActions> externalActionsEntry : externalActions.entrySet()) {
            if (!agentName.equals(externalActionsEntry.getKey())) {
                agentProblem.domain.actions.addAll(externalActionsEntry.getValue().getPublicActions());
            }
        }
    }

    public static PddlProblem groundProblemForAgent(String agentName,
            PddlProblem problem) {
        List<PddlAction> actions = new ArrayList<>(
                problem.domain.actions.size());
        List<PddlName> agentTypes = new ArrayList<>();
        PddlName agentType = new PddlName("agent");
        agentTypes.add(agentType);

        for (Entry<PddlName, PddlType> objectType : problem.domain.types
                .getBindings().entrySet()) {
            if (objectType.getValue().eithers.contains(agentType)) {
                agentTypes.add(objectType.getKey());
            }
        }
        for (PddlAction action : problem.domain.actions) {
            Binding binding = new Binding();
            PddlName agentParameter = getParameterNameAgentTypes(action,
                    agentTypes);
            if (agentParameter != null) {
                binding.addBinding(agentParameter, new PddlName(agentName));
            }
            PddlActionInstance bindedAction = new PddlActionInstance(action,
                    binding);
            actions.add(PddlAction.createRenamedAction(
                    bindedAction.name.replace(' ', '_'), bindedAction));
        }

        PddlDomain groundedDomain = PddlDomain.replaceActions(problem.domain,
                actions);
        return PddlProblem.replaceDomain(problem, groundedDomain);
    }

    private static PddlName getParameterNameAgentTypes(PddlAction action,
            List<PddlName> agentTypes) {
        PddlName parameterName = null;
        for (Entry<PddlName, PddlType> binding : action.parameters
                .getBindings().entrySet()) {
            for (PddlName type : binding.getValue().eithers) {
                if (agentTypes.contains(type)) {
                    if (parameterName != null) {
                        throw new IllegalArgumentException(
                                "Multiple agents in one action! " + action
                                        + " -- " + agentTypes);
                    }
                    parameterName = binding.getKey();
                    break;
                }
            }
        }
        return parameterName;
    }

    private void initInitialPlanLandmarks()
            throws InterruptedException {
        numOfSentActions.add(0);
        timeOfVerification.add(0l);
        timeOfPlanning.add(0l);

        if (!Settings.USE_INITIAL_LANDMARKS.isEmpty()) {
            initialPlans = new ArrayList<>();
            long startTime = System.currentTimeMillis();
            
            if (isProblemPubliclySolvable) {
                System.out.println("Solving using public plan :)");
                initialPlans.add( Downward.runDownward(publicProblem, "public", true).get(0) );
            } else {
            
                for (InitialLandmarks il : Settings.USE_INITIAL_LANDMARKS) {
                    List<String> newInitialPlan;
                    switch (il) {
                    case RELAXED:
                        System.out.println("Creating initial relaxed plan...");
                        newInitialPlan = RelaxedPlanCreator.generateRelaxedPlan(relaxedProblem, planners.keySet(), originalProblem);
                        System.out.println("Relaxed plan: " + newInitialPlan);
                        break;
                    case PUBLIC:
                        System.out.println("Creating initial public plan...");
                        newInitialPlan = Downward.runDownward(publicProblem, "public", true).get(0);
                        break;
                    default:
                        throw new IllegalStateException("Unknown settings: " + Settings.USE_INITIAL_LANDMARKS);
                    }
                   
                    long duration = System.currentTimeMillis() - startTime;
                    long curNum = timeOfPlanning.get(0);
                    timeOfPlanning.set(0, curNum + duration);
    
                    increaseNumOfSentActions(newInitialPlan.size() * planners.size());
    
                    if (Settings.VERBOSE) {
                        System.out.println("public initial plan: " + newInitialPlan);
                    }
    
                    if (Settings.VERIFY_PLANS && Settings.VERIFY_RELAXED_PLAN) {
                        startTime = System.currentTimeMillis();
                        int lastMark = computeLastMarkReachableByAllAgents(newInitialPlan);
                        if (lastMark < newInitialPlan.size()) {
                            newInitialPlan = newInitialPlan.subList(0, lastMark);
                        }
    
                        duration = System.currentTimeMillis() - startTime;
                        timeOfVerification.set(0, duration);
                    }
    
                    initialPlans.add(newInitialPlan);
                }
            }
            addInitialPlansAsLandmarksToPlanners(initialPlans);
        }
    }

    public void increaseNumOfSentActions(int increment) {
        Integer curNum = numOfSentActions.get(iteration);
        numOfSentActions.set(iteration, curNum + increment);
    }

    private int computeLastMarkReachableByAllAgents(List<String> publicPlan)
            throws InterruptedException {
        int minLastReachableMark = Integer.MAX_VALUE;

        for (PsmPlannerAgent planner : planners.values()) {
            minLastReachableMark = Math.min(minLastReachableMark,
                    planner.computeLastReachableMark(publicPlan));
        }
        return minLastReachableMark;
    }

    private void addInitialPlansAsLandmarksToPlanners(List<List<String>> initialPlans) {
        for (PsmPlannerAgent planner : planners.values()) {
            planner.addInitialPlansAsLandmarks(initialPlans);
        }
    }

    private void initRequiredCausalLandmarks() {
        if (!Settings.USE_REQUIRED_CAUSAL_LANDMARKS) {
            return;
        }
        if (Settings.VERBOSE)
            System.out.println("Init required causal landmarks");
        for (PsmPlannerAgent planner1 : planners.values()) {
            Collection<Landmark> landmarks1 = planner1
                    .computeRequiredLandmarks();
            for (PsmPlannerAgent planner2 : planners.values()) {
                if (planner1 == planner2)
                    continue;
                if (Settings.VERBOSE)
                    System.out.println("    " + planner1.agentName + " --> "
                            + planner2.agentName);
                planner2.addLandmarks(landmarks1);
            }
        }
    }

    private void initNextIteration() {
        System.out.println("=== ITERATION " + (++iteration) + " ===");
        iterationMaxTimeMs = 0;
        intersection = null;

        numOfSentActions.add(0);
        timeOfVerification.add(0l);
        timeOfPlanning.add(0l);

        if (Settings.RESET_IN_SECOND_ITERATION && iteration == 2) {
            for (PsmPlannerAgent planner : planners.values()) {
                planner.reset();
            }
        }
    }

    private boolean planAllAgents(boolean runSequentially, long endTime) throws Exception {
        if (runSequentially) {
            return planAllAgentsSequentially();
        } else {
            return planAllAgentsParalelly(endTime);
        }
    }

    private boolean planAllAgentsParalelly(long endTime) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(Settings.THREAD_POOL_SIZE);

        duration = 0;
        retValue = true;
        round = new Integer(0);
        thrownException = null;
        
        for (final Entry<String, PsmPlannerAgent> entry : planners.entrySet()) {
            if (entry.getValue().isPubliclySolvable()) {
                System.out.println("Skipping publicly solvable agent: " + entry.getKey());
                continue;
            }
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    if (Settings.VERBOSE) {
                        System.out.println("starting...");
                    }
                    long startTime = System.currentTimeMillis();
                    boolean isPlanningSuccessful;
                    try {
                        isPlanningSuccessful = planAgent(entry.getKey(), entry.getValue());
                    } catch (InterruptedException e) {
                        thrownException = e;
                        System.out.println("interrupted...finishing");
                        return;
                    } catch (DownwardOutOfMemoryException e) {
                        thrownException = e;
                        System.out.println("out of memory...finishing");
                        return;
                    }
                    synchronized (round) {
                        duration += System.currentTimeMillis() - startTime;
                        if (!isPlanningSuccessful) {
                            retValue = false; // timeout
                            System.out.println("...finishing");
                            return;
                        }

                        if (round == -1) {
                            System.out.println("...finishing");
                            return; // plan only => already has empty intersection
                        }
                        round++;
                        updateIntersectionWithCurrentPsm(entry.getValue());
                        if (isIntersectionEmpty()) {
                            System.out.println("--> intersection empty after " + round + " rounds");
                            round = -1;
                        }
                    }
                    if (Settings.VERBOSE) {
                        System.out.println("...finishing");
                    }
                }
            });
        }

        executor.shutdown();
        if (!executor.awaitTermination(endTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS)) {
            System.out.println("killing tasks");
            executor.shutdownNow();

            try {
                if (Settings.VERBOSE) {
                    System.out.println("PsmPlanner: timeout: killing all!");
                }
                Runtime.getRuntime().exec("killall -KILL downward-1");
                Runtime.getRuntime().exec("killall -KILL downward-1-debug");
                Runtime.getRuntime().exec("killall -KILL polystar");
            } catch (IOException e) {
                e.printStackTrace();
            }
    
            throw new TimeoutException();
        }

        try {
            if (Settings.VERBOSE) {
                System.out.println("PsmPlanner: killing all!");
            }
            Runtime.getRuntime().exec("killall -KILL downward-1");
            Runtime.getRuntime().exec("killall -KILL downward-1-debug");
            Runtime.getRuntime().exec("killall -KILL polystar");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (thrownException != null) {
            throw thrownException;
        }

        timeOfPlanning.set(iteration, duration - timeOfVerification.get(iteration));
        parallelTimeMs += iterationMaxTimeMs;
        
        return retValue;
    }

    private boolean planAllAgentsSequentially() throws InterruptedException {
        long duration = 0;
        int round = 0;
        for (Entry<String, PsmPlannerAgent> entry : planners.entrySet()) {
            long startTime = System.currentTimeMillis();
            boolean isPlanningSuccessful = planAgent(entry.getKey(), entry.getValue());
            duration += System.currentTimeMillis() - startTime;
            if (!isPlanningSuccessful) {
                return false; // timeout
            }
            if (round == -1) {
                continue; // plan only => already has empty intersection
            }
            round++;
            updateIntersectionWithCurrentPsm(entry.getValue());
            if (isIntersectionEmpty()) {
                System.out.println("--> intersection empty after " + round
                        + " rounds");
                round = -1;
                continue;
            }
        }
        timeOfPlanning.set(iteration, duration - timeOfVerification.get(iteration));
        parallelTimeMs += iterationMaxTimeMs;
        return true;
    }

    private boolean isIntersectionNonEmpty() {
        return !isIntersectionEmpty();
    }

    private boolean isIntersectionEmpty() {
        return intersection.isEmpty();
    }

    private void exchangeLandmarks() {
        System.out.println("--> exchanging public landmarks");
        startTimeMs = System.currentTimeMillis();
        for (PsmPlannerAgent planner : planners.values()) {
            planner.resetLandmarks();
            if (!Settings.USE_INITIAL_LANDMARKS.isEmpty()
                    && !Settings.USE_INITIAL_LANDMARKS_IN_FIRST_ITER_ONLY) {
                addInitialPlansAsLandmarksToPlanners(initialPlans);
            }
        }
        for (PsmPlannerAgent planner1 : planners.values()) {
            if (planner1.isPubliclySolvable()) {
                continue;
            }

            Collection<Landmark> landmarks1 = planner1.computePublicLandmarks();
            for (PsmPlannerAgent planner2 : planners.values()) {
                if (planner1 == planner2)
                    continue;
                if (Settings.VERBOSE)
                    System.out.println("    " + planner1.agentName + " --> "
                            + planner2.agentName);
                planner2.addPublicLandmarks(landmarks1);
            }
        }
        durationMs = System.currentTimeMillis() - startTimeMs;
        parallelTimeMs += durationMs;
    }

    private void updateIntersectionWithCurrentPsm(PsmPlannerAgent plannerAgent) {
        PlanSet<Operator> curPlanSet;
        if (Settings.PLAN_SET_TYPE == PlanSetType.PSM) {
            curPlanSet = agentPubProj;
        } else {
            curPlanSet = plannerAgent.getPlanSet();
        }

        increaseNumOfSentActions(curPlanSet.size() * planners.size());

        if (intersection == null) {
            intersection = curPlanSet;
        } else {
            startTimeMs = System.currentTimeMillis();
            intersection = intersection.intersectWith(curPlanSet);
            durationMs = System.currentTimeMillis() - startTimeMs;
            parallelTimeMs += durationMs;
        }
    }

    private boolean planAgent(final String agentName,
            PsmPlannerAgent planner) throws InterruptedException {
        startTimeMs = System.currentTimeMillis();
        planner.verificationDuration = 0;
        planner.communicatedActions = 0;
        agentPubProj = planner.plan(Settings.DOT_OUTPUT_ALLOWED, new AskOtherAgents() {
            @Override
            public int getOtherAgentNum() {
                // TODO Auto-generated method stub
                return planners.size() - 1;
            }

            public int computeLastMarkReachableByAllAgents(List<String> publicPlan) {
                if (getOtherAgentNum() == 0) {
                    System.err.println(agentName+": There are no other agent!");
                    return -1;
                }
                int minLastReachableMark = Integer.MAX_VALUE;
                for (PsmPlannerAgent otherAgentPlanner : planners.values()) {
                    if (!otherAgentPlanner.agentName.equals(agentName)) {
                        try {
                            minLastReachableMark = Math.min(minLastReachableMark, otherAgentPlanner.computeLastReachableMark(publicPlan));
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                return minLastReachableMark;
            }

        });
        timeOfVerification.set(iteration, planner.verificationDuration);
        increaseNumOfSentActions(planner.communicatedActions);
        if (agentPubProj == null) {
            return false; // timeout
        }
        durationMs = System.currentTimeMillis() - startTimeMs;
        iterationMaxTimeMs = Math.max(iterationMaxTimeMs, durationMs);
        return true;
    }

    public static PlanningResult plan(MultiAgentProblem problem, long endTime)
            throws Exception {
        PsmPlanner planner = new PsmPlanner(problem);
        PlanningResult planningResult = planner.run(Settings.RUN_SEQUENTIALLY, endTime);
        if (planningResult != null) {
            System.out.println("Solution found: " + planningResult.plan);
            if (Settings.VERIFY_SOLUTION) {
                System.out.println("Verification started: " + new Date());
                List<String> finalPlan = planner.isPlanInternallyExtensibleParalelly(planningResult.plan, endTime);
                if (finalPlan == null) {
                    throw new DownwardException("ERROR: Invalid solution!");
                } else {
                    System.out.println("finalPlan: " + finalPlan);
                    planningResult.plan = problem.revertPlan(finalPlan);
                    System.out.println("Solution is valid.");
                }
            }
        } else {
            System.out.println("Solution not found!");
        }
        return planningResult;
    }
}
