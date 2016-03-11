package experiments.codmap.distributed.agent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import cz.agents.dimap.Settings;
import cz.agents.dimap.landmarks.Landmark;
import cz.agents.dimap.planset.PlanSetType;
import cz.agents.dimap.psm.PlanStateMachine;
import cz.agents.dimap.psm.planner.AskOtherAgents;
import cz.agents.dimap.psm.planner.ExternalActions;
import cz.agents.dimap.psm.planner.PsmPlanner;
import cz.agents.dimap.psm.planner.PsmPlannerAgent;
import cz.agents.dimap.tools.Pair;
import cz.agents.dimap.tools.fd.Downward;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import experiments.codmap.distributed.Message;
import experiments.codmap.distributed.Message.Command;
import experiments.psm.file.CoDmapMultiAgentProblem;
import experiments.psm.file.FileMultiAgentProblem;

@SuppressWarnings("unchecked")
public class CoDmapAgent {

    public static boolean IS_RUNNING_IN_ONE_VM = false;
    private AgentComm client;
    private PddlProblem problem;
    private String agentName;
    private int agentIndex;
    private PsmPlannerAgent plannerAgent;
    private PddlProblem publicProblem;
    private List<String> relaxedPlan;
    private String outFile;
    private Set<PddlName> constants;
    private List<PddlTerm> inMerges;
    private Set<PddlTerm> reachableFacts;
    private Set<PddlTerm> publicFacts;
    private HashSet<PddlAction> myReachableActions;

    public CoDmapAgent(String agentName, int agentIndex, String domainFileName, String problemFileName, String outFile, String brokerIp, int brokerPort) throws Exception {
        this.agentName = agentName;
        this.agentIndex = agentIndex;
        this.outFile = outFile;
        client = new AgentComm(brokerIp, brokerPort);
        client.send(new Message(Command.LOGIN, agentName));

        File domainFile = FileMultiAgentProblem.fixPrivateAndFunctions(new File(domainFileName));
        File problemFile = FileMultiAgentProblem.fixPrivateAndFunctions(new File(problemFileName));
        
        PddlDomain domain = new PddlDomain(new FileReader(domainFile));
        problem = new PddlProblem(domain, new FileReader(problemFile));
    }

    public void run() {
        sendAtomsWithHyphens();
        extractConstants();

        List<PlanningGraph> subPlanningGraphs = createRelaxedPlan();
        preparePublicFacts();
        extractRelaxedPlan(subPlanningGraphs);

        createPlanner();
        initInitialPlanLandmarks();
        
        startPlanning();
    }
    
    private void preparePublicFacts() {
        Set<PddlTerm> myFacts = getMyFacts();
        Set<PddlTerm> myPublicFacts = removeConstants(myFacts);  
        myPublicFacts.addAll(problem.goal.positives);
        publicFacts = (Set<PddlTerm>) client.sendAndReceiveAnswer(new Message(Command.PUBLIC_FACTS, myPublicFacts));
        CoDmapMultiAgentProblem.groundToFacts(problem, agentName, myFacts, publicFacts);
        CoDmapMultiAgentProblem.setUpSharedPredicates(publicFacts, myFacts, problem);
    }

    private Set<PddlTerm> removeConstants(Set<PddlTerm> facts) {
        Set<PddlTerm> newFacts = new HashSet<>();
        for (PddlTerm term : facts) {
            if (!constants.contains(term.name)) {
                newFacts.add(term);
            }
        }
        return newFacts;
    }

    private Set<PddlTerm> getMyFacts() {
        Set<PddlTerm> myFacts = new HashSet<>(); 
        for (PddlAction action : myReachableActions) {
            myFacts.addAll(action.getPreconditionFacts());
            myFacts.addAll(action.getEffectFacts());
        }
        return myFacts;
    }

    private void extractConstants() {
        constants = new HashSet<>();
        for (PddlName predicate : problem.domain.predicateTypes.keySet()) {
            constants.add(predicate);
        }
        for (PddlAction action : problem.domain.actions) {
            for (PddlTerm effect : action.getEffectFacts()) {
                constants.remove(effect.name);
            }
        }
        Set<PddlName> privateConstants = new HashSet<>(constants);
        privateConstants.retainAll(problem.domain.privatePredicates);
        constants.removeAll(problem.domain.privatePredicates);
        constants = (Set<PddlName>) client.sendAndReceiveAnswer(new Message(Command.CONSTANTS, constants));
        problem.domain.privatePredicates.addAll(constants);
        constants.addAll(privateConstants);
    }

    private List<PlanningGraph> createRelaxedPlan() {
        List<PlanningGraph> subPlanningGraphs = new ArrayList<>();

        reachableFacts = new HashSet<>(problem.init.positives);
        myReachableActions = new HashSet<>();
        boolean isGoalReached = false;

        while (true) {
            PlanningGraph planningGraph = new PlanningGraph(reachableFacts, agentName, problem, constants);
            planningGraph.applyAllActionsRepeatedly();
            myReachableActions.addAll(planningGraph.getAppliedActions());
            Set<PddlTerm> newReachableFacts = planningGraph.getReachableFacts();
            newReachableFacts.removeAll(reachableFacts);
            reachableFacts.addAll(newReachableFacts);
            Set<PddlTerm> newPublicFacts = getPublicFacts(newReachableFacts); 
            
            if (!isGoalReached) {
                subPlanningGraphs.add(planningGraph);
            }

            Message message = client.sendAndReceiveAny(new Message(Command.CREATE_RELAXED_PLAN_REACHABLE_FACTS, getPublicFacts(newPublicFacts)));
            if (message.command.equals(Command.CREATE_RELAXED_PLAN_GOAL_REACHED)) {
                isGoalReached = true;
                message = client.receive();
            } 

            Set<PddlTerm> receivedPublicFacts = (Set<PddlTerm>) message.content;
            if (receivedPublicFacts != null) {
                reachableFacts.addAll(receivedPublicFacts);
            } else {
                break;
            }
        }
        if (!isGoalReached) {
            throw new AssertionError("Goal not reached by relaxed planning graph!");
        }
        return subPlanningGraphs;
    }

    public void extractRelaxedPlan(List<PlanningGraph> subPlanningGraphs) {
        ListIterator<PlanningGraph> planningGraphIt = subPlanningGraphs.listIterator(subPlanningGraphs.size());
        Set<PddlTerm> factsToSupport = new HashSet<>();
        PlanningGraph planningGraph = planningGraphIt.previous();
        while (true) {
            Message message = client.receive();
            if (message.command.equals(Command.RELAXED_PLAN)) {
                relaxedPlan = (List<String>) message.content;
                return;
            } else if (message.command.equals(Command.CREATE_RELAXED_PLAN_NEXT_LAYER)) {
                planningGraph = planningGraphIt.previous();
            } else if (message.command.equals(Command.CREATE_RELAXED_PLAN_EXRTACT_PLAN)) {
                Set<PddlTerm> receivedFacts = (Set<PddlTerm>) message.content;
                if (receivedFacts == null) {
                    factsToSupport.addAll(problem.goal.positives);
                } else {
                    factsToSupport.addAll(receivedFacts);
                }
                
                Set<PddlTerm> factsToBeSupportedLater = new HashSet<>();
                factsToBeSupportedLater = new HashSet<>(factsToSupport);
                factsToBeSupportedLater.retainAll(planningGraph.initFacts);
                factsToSupport.removeAll(factsToBeSupportedLater);

                Pair<Set<PddlTerm>, List<PddlAction>> relaxedSolution = planningGraph.getInvertedPlan(factsToSupport, Settings.LAZY_RELAXED_AGENTS);
                
                List<String> invertedPlan = getRelaxedPublicPlan(relaxedSolution.getRight());
                
                factsToSupport = relaxedSolution.getLeft();

                Set<PddlTerm> publicFacts = getPublicFacts(factsToSupport);
                publicFacts.addAll(factsToBeSupportedLater);
                publicFacts.removeAll(problem.init.positives);
                factsToBeSupportedLater.clear();
                
                factsToSupport.removeAll(publicFacts);
                
                client.send(new Message(Command.CREATE_RELAXED_PLAN_EXRTACT_PLAN, invertedPlan));
                client.send(new Message(Command.CREATE_RELAXED_PLAN_EXRTACT_PLAN, publicFacts));
            }
        }
    }

    private List<String> getRelaxedPublicPlan(List<PddlAction> actions) {
        List<String> plan = new ArrayList<>();
        for (PddlAction action : actions) {

            if (!getRelaxedFacts(action.getEffectFacts()).isEmpty()
                    || !getRelaxedFacts(action.getPreconditionFacts()).isEmpty()) {
                plan.add(action.name);
            }
        }
        return plan;
    }

    private Set<PddlTerm> getRelaxedFacts(Collection<PddlTerm> facts) {
        Set<PddlTerm> newFacts = new HashSet<>(facts);
        newFacts.retainAll(publicFacts);
        return newFacts;
    }

    private Set<PddlTerm> getPublicFacts(Collection<PddlTerm> facts) {
        Set<PddlTerm> publicFacts = new HashSet<>();
        
        nextEffect:
        for (PddlTerm fact : facts) {
            for (PddlName name : problem.privateObjects) {
                if (fact.arguments.contains(name)) {
                    continue nextEffect;
                }
            }
            for (PddlName name : problem.domain.privatePredicates) {
                if (fact.name.name.startsWith(name.name)) {
                    continue nextEffect;
                }
            }
            publicFacts.add(fact);
        }
        return publicFacts;
    }

    private void startPlanning() {
        while (true) {
            Message serverCommand = client.receive();
            switch (serverCommand.command) {
            case CREATE_PUBLIC_PLAN:
                try {
                    List<String> publicPlan = Downward.runDownward(publicProblem, "public", true).get(0);
                    client.send(new Message(Command.CREATE_PUBLIC_PLAN, publicPlan));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case RESET:
                plannerAgent.reset();
                if (!Settings.USE_INITIAL_LANDMARKS.isEmpty() && !Settings.USE_INITIAL_LANDMARKS_IN_FIRST_ITER_ONLY) {
                    initInitialPlanLandmarks();
                }
                break;
            case KILL_ALL:
                killall();
                break;
            case PLAN:
                plan();
                break;
            case COMPUTE_LAST_REACHABLE:
                handleComputeLastRechable(serverCommand);
                break;
            case RESET_LANDMARKS:
                plannerAgent.resetLandmarks();
                break;
            case COMPUTE_PUBLIC_LANDMARKS:
                client.send(new Message(Command.COMPUTE_PUBLIC_LANDMARKS, plannerAgent.computePublicLandmarks()));
                break;
            case ADD_PUBLIC_LANDMARKS:
                Collection<Landmark> landmarks = (Collection<Landmark>) serverCommand.content;
                plannerAgent.addPublicLandmarks(landmarks);
                break;
            case INTERNAL_EXTENSION:
                List<String> plan = (List<String>) serverCommand.content;
                List<String> planExtension = null;
                try {
                    removeAllMergesForVerification();
                    planExtension = plannerAgent.isPlanInternallyExtensible(plan);
                    System.out.println(agentName + ": extension found");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                client.send(new Message(Command.INTERNAL_EXTENSION, planExtension));
                break;
            case END:
                if (Settings.DELETE_TMP_FILES_ON_EXIT) {
                    plannerAgent.deleteTmpFiles();
                }
                writePlan((List<String>) serverCommand.content);
                return;
            default:
                throw new IllegalArgumentException("Unexpected command: " + serverCommand.command);
            }
        }
    }

    private void removeAllMergesForVerification() {
        if (Settings.REPLACE_INTERNALLY_DEPENDENT_ACTIONS) {
            for (PddlAction action : plannerAgent.pddlProblem.domain.actions) {
                action.precondition.positives.removeAll(inMerges);
                action.effect.negatives.removeAll(inMerges);
                action.effect.positives.removeAll(inMerges);
            }
        }
    }

    private void writePlan(List<String> plan) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outFile)));
            for (String action : plan) {
                writer.append(action);
                writer.append('\n');
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void killall() {
        try {
            Runtime.getRuntime().exec("killall -KILL downward-1");
            Runtime.getRuntime().exec("killall -KILL downward-1-debug");
            Runtime.getRuntime().exec("killall -KILL polystar");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        client.send(Message.OK);
    }
    
    public void plan() {
        try {
            PlanStateMachine agentPubProj = plannerAgent.plan(false, new AskOtherAgents() {
                
                @Override
                public int getOtherAgentNum() {
                    return 1; // wrong number but fine for distributed version
                }
                
                @Override
                public int computeLastMarkReachableByAllAgents(List<String> publicPlan) {
                    return (Integer) CoDmapAgent.this.handleComputeLastReachable(publicPlan);
                }
            });
            if (Settings.PLAN_SET_TYPE == PlanSetType.PSM) {
                client.send(new Message(Command.PLAN, agentPubProj));
            } else {
                throw new IllegalArgumentException("Plan set not supported! : " + Settings.PLAN_SET_TYPE);
//                        client.send(new Message(Command.PLAN, plannerAgent.getPlanSet()));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected Object handleComputeLastReachable(List<String> publicPlan) {
        Message message = client.sendAndReceiveAny(new Message(Command.COMPUTE_LAST_REACHABLE_BY_ALL_OTHER_AGENTS, publicPlan));

        while (true) {
            if (message.command.equals(Command.COMPUTE_LAST_REACHABLE_BY_ALL_OTHER_AGENTS)) {
                return message.content;
            } else if (message.command.equals(Command.COMPUTE_LAST_REACHABLE)) {
                handleComputeLastRechable(message);
                message = client.receive();
            } else {
                throw new IllegalArgumentException("Unexpected command: " + message.command);
            }
        }
    }

    public void handleComputeLastRechable(Message message) {
        List<String> plan = (List<String>) message.content;
        try {
            int lastMark = plannerAgent.computeLastReachableMark(plan);
            client.send(new Message(Command.COMPUTE_LAST_REACHABLE, new Pair<List<String>, Integer>(plan, lastMark)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initInitialPlanLandmarks() {
        if (relaxedPlan != null) {
            plannerAgent.addInitialPlansAsLandmarks(Arrays.asList(relaxedPlan));
        }
    }

    private void sendAtomsWithHyphens() {
        Map<String, String> extractedAtoms = CoDmapMultiAgentProblem.extractAtomsWithHyphens(agentName, problem);
        client.sendAndReceiveAnswer(new Message(Command.ATOMS_WITH_HYPHENS, extractedAtoms));
        Message message = new Message(Command.TRANSLATION_TABLE, new HashMap<String, String>(PddlName.translationTable));
        client.sendAndReceiveAnswer(message);
    }

    private void createPlanner() {
        System.out.println(agentName + ": Creating planner");

        Map<String, ExternalActions> externalActionsMap = computeAgentProblem();

        PsmPlanner.extendDomainWithPublicActions(agentName, problem, externalActionsMap);
        PsmPlanner.extendDomainWithNewInitFacts(problem, externalActionsMap);

        publicProblem = problem.clone();
        publicProblem.domain.clone();
        publicProblem = PddlProblem.createPublicProjection(publicProblem);
        ExternalActions externalActions = externalActionsMap.get(agentName);
        try {
            plannerAgent = new PsmPlannerAgent(agentName, problem, agentIndex, externalActions.isPubliclySolvable());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        plannerAgent.initPlanVerifier();

//      
//      PddlMultiAgentProblem pddlMultiAgentProblem = new PddlMultiAgentProblem(problems);
//      originalProblem = pddlMultiAgentProblem.toSingleAgentProblem();
//      relaxedProblem = pddlMultiAgentProblem.getOriginalRelaxedProblem();
//
//      for (PsmPlannerAgent planner : planners.values()) {
//          Collection<PsmPlannerAgent> otherPlanners = new HashSet<>(
//                  planners.values());
//          otherPlanners.remove(planner);
//          planner.initPlanVerifier(otherPlanners);
//      }
    }

    private Map<String, ExternalActions> computeAgentProblem() {
        problem = PsmPlanner.groundProblemForAgent(agentName, problem);
        ExternalActions externalActions = new ExternalActions(agentName, problem);

        Map<String, ExternalActions> externalActionsMap = (Map<String, ExternalActions>) client.sendAndReceiveAnswer(new Message(Command.EXTERNAL_ACTIONS, externalActions));
        
//        System.out.println(agentName + ": externalActionsMap: " + externalActionsMap);
        
        if (Settings.REPLACE_INTERNALLY_DEPENDENT_ACTIONS) {
            inMerges = new ArrayList<>();
            for (ExternalActions extActions : externalActionsMap.values()) {
                inMerges.addAll(extActions.getInMerges());
            }
            PsmPlanner.addAllInMergesToPublic(inMerges, problem);
        }
        return externalActionsMap;
    }
}
