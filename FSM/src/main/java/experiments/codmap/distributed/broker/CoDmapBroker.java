package experiments.codmap.distributed.broker;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import cz.agents.dimap.Settings;
import cz.agents.dimap.landmarks.Landmark;
import cz.agents.dimap.planset.PlanSet;
import cz.agents.dimap.psm.planner.ExternalActions;
import cz.agents.dimap.tools.Pair;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.ma.IpcMaProjection;
import experiments.codmap.distributed.Message;
import experiments.codmap.distributed.Message.Command;
import experiments.psm.file.FileMultiAgentProblem;

@SuppressWarnings("unchecked")
public class CoDmapBroker implements Runnable {

    private int basePort;
    private int numberOfAgents;
    
    List<BrokerComm> agentComms = new ArrayList<>();
    
    private int iteration = 0;
    private Map<String, String> extractedAtoms;
    private Map<String, String> translationTable;
    private Map<String, ExternalActions> externalActionsMap;
    private PddlProblem problem;

    boolean isProblemPubliclySolvable = false;
    Set<BrokerComm> agentsWithPubliclySolvableProblems;
    
    public CoDmapBroker(String ipFile, int basePort, String domainFileName, String problemFileName) {
        try {
            File domainFile = FileMultiAgentProblem.fixPrivateAndFunctions(new File(domainFileName));
            File problemFile = FileMultiAgentProblem.fixPrivateAndFunctions(new File(problemFileName));
            
            PddlDomain domain = new PddlDomain(new FileReader(domainFile));
            problem = new PddlProblem(domain, new FileReader(problemFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        numberOfAgents = getNumberOfAgents(ipFile);
        this.basePort = basePort;
    }
    
    private int getNumberOfAgents(String ipFile) {
        try {
            Scanner sc = new Scanner(new File (ipFile));
            int number = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                number++;
            }
            sc.close();
            return number;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("cannot count number of agents!");
        }
    }

    @Override
    public void run() {
        connectToAgents();
        System.out.println("BROKER: All agents connected");
        recieveAtomsWithHyphens();
        exchangeConstants();
        
        System.out.println("BROKER: Creating relaxed plan...");
        
        createRelaxedPlan();

        System.out.println("BROKER: Grounding problems and exchanging external actions");
        exchangeExternalActions();
        
        List<String> plan = null;
        if (isProblemPubliclySolvable) {
            System.out.println("BROKER: Creating public plan");
            plan = createPublicPlan();
        } else {
            System.out.println("BROKER: Going to plan");
            plan = startPlanning();
        }
        
        System.out.println("BROKER: plan: " + plan);

        Map<BrokerComm, List<String>> solution = createSolution(plan);
        
        System.out.println("BROKER: solution: " + solution);

        for (BrokerComm agentComm : agentComms) {
            agentComm.sendMessage(new Message(Command.END, solution.get(agentComm)));
        }
        
        System.out.println("BROKER: end");
    }

    private void exchangeConstants() {
        Set<PddlName> constants = null;
        for (BrokerComm agentComm : agentComms) {
            Set<PddlName> newConstants = (Set<PddlName>) agentComm.receiveMessage(Command.CONSTANTS);
            if (constants == null) {
                constants = newConstants;
            } else {
                constants.retainAll(newConstants);
            }
        }
        
        for (BrokerComm agentComm : agentComms) {
            agentComm.sendMessage(new Message(Command.CONSTANTS, constants));
        }
        System.out.println("BROKER: constants: " + constants);
    }

    private void createRelaxedPlan() {
        createRelaxedPlanningGraph();
        extractMaStripsPublicFacts();
        extractRelaxedPlan();
    }

    private void createRelaxedPlanningGraph() throws AssertionError {
        Set<PddlTerm> goalsToCover = new HashSet<>(problem.goal.positives);
        goalsToCover.removeAll(problem.init.positives);
        
        boolean isGoalReached = false;
        while (true) {
            Set<PddlTerm> newPublicFacts  = new HashSet<>();
            for (BrokerComm agentComm : agentComms) {
                newPublicFacts.addAll((Collection<? extends PddlTerm>) agentComm.receiveMessage(Command.CREATE_RELAXED_PLAN_REACHABLE_FACTS));
            }

            goalsToCover.removeAll(newPublicFacts);

            if (goalsToCover.isEmpty() && !isGoalReached) {
                isGoalReached = true;
                for (BrokerComm agentComm : agentComms) {
                    agentComm.sendMessage(new Message(Command.CREATE_RELAXED_PLAN_GOAL_REACHED, null));
                }
            }
            
            if (newPublicFacts.isEmpty()) {
                for (BrokerComm agentComm : agentComms) {
                    agentComm.sendMessage(new Message(Command.CREATE_RELAXED_PLAN_REACHABLE_FACTS, null));
                }
                break;
            } else {
                for (BrokerComm agentComm : agentComms) {
                    agentComm.sendMessage(new Message(Command.CREATE_RELAXED_PLAN_REACHABLE_FACTS, newPublicFacts));
                }
            }
            System.out.println("newPublicFacts: " + newPublicFacts);
        }
        
        if (!isGoalReached) {
            throw new AssertionError("Goal not reached by relaxed planning graph! " + goalsToCover);
        }
    }

    private void extractMaStripsPublicFacts() {
        Map<BrokerComm, Set<PddlTerm>> agentFactsMap = new HashMap<>();
        
        for (BrokerComm agentComm : agentComms) {
            Set<PddlTerm> agentFacts = (Set<PddlTerm>) agentComm.receiveMessage(Command.PUBLIC_FACTS);
            agentFactsMap.put(agentComm, agentFacts);
        }

        Set<PddlTerm> publicFacts = IpcMaProjection.getPublicFacts(agentFactsMap);

        System.out.println("BROKER: Public facts: " + publicFacts);

        for (BrokerComm agentComm : agentComms) {
            agentComm.sendMessage(new Message(Command.PUBLIC_FACTS, publicFacts));
        }
    }

    private void extractRelaxedPlan() {
        List<String> relaxedPlan = new ArrayList<>();
        Set<PddlTerm> factsToSupport = null;
        nextRound:
        while (factsToSupport == null || !factsToSupport.isEmpty()) {
            Set<PddlTerm> lastFactsToSupport = new HashSet<>();
            if (factsToSupport != null) {
                lastFactsToSupport.addAll(factsToSupport);
            }
            for (BrokerComm agentComm : agentComms) {
                agentComm.sendMessage(new Message(Command.CREATE_RELAXED_PLAN_EXRTACT_PLAN, factsToSupport));
                
                relaxedPlan.addAll((List<String>) agentComm.receiveMessage(Command.CREATE_RELAXED_PLAN_EXRTACT_PLAN));
                factsToSupport = (Set<PddlTerm>) agentComm.receiveMessage(Command.CREATE_RELAXED_PLAN_EXRTACT_PLAN);
                if (factsToSupport.isEmpty()) {
                    break nextRound;
                }
            }
            if (lastFactsToSupport.equals(factsToSupport)) {
                for (BrokerComm agentComm : agentComms) {
                    agentComm.sendMessage(new Message(Command.CREATE_RELAXED_PLAN_NEXT_LAYER, null));
                }
            }
        }

        Collections.reverse(relaxedPlan);
        
        System.out.println("Relaxed plan: " + relaxedPlan);
        
        for (BrokerComm comm : agentComms) {
            comm.sendMessage(new Message(Command.RELAXED_PLAN, relaxedPlan));
        }
    }

    private List<String> createPublicPlan() {
        
        BrokerComm agentComm = agentComms.get(0);
        agentComm.sendMessage(new Message(Command.CREATE_PUBLIC_PLAN, null));
        List<String> publicPlan = (List<String>) agentComm.receiveMessage(Command.CREATE_PUBLIC_PLAN);
        return publicPlan;
    }

    public Map<BrokerComm, List<String>> createSolution(List<String> plan) {
        final Map<BrokerComm, List<String>> agentsPlans = new HashMap<>(); 

        for (BrokerComm agentComm : agentComms) {
            agentComm.sendMessage(new Message(Command.INTERNAL_EXTENSION, plan));
        }
        for (BrokerComm agentComm : agentComms) {
            List<String> planExtension = (List<String>) agentComm.receiveMessage(Command.INTERNAL_EXTENSION);
            agentsPlans.put(agentComm, planExtension);
        }
        
        Map<BrokerComm, List<String>> mergedPlan = mergePlans(plan, agentsPlans);
        Map<BrokerComm, List<String>> solutions = new HashMap<>();
        for (Entry<BrokerComm, List<String>> entry : mergedPlan.entrySet()) {
            List<String> solution = new ArrayList<>(); 
            for (String action : entry.getValue()) {
                String replaced = action;
                for (Entry<String, String> atomEntry : extractedAtoms.entrySet()) {
                    String name = atomEntry.getValue();
                    if (translationTable.containsKey(name)) {
                        name = translationTable.get(name);
                    }
                    replaced = replaced.replaceAll(" " + atomEntry.getKey(), " " + name);
                    replaced = replaced.replaceAll("\\(" + atomEntry.getKey(), "(" + name);
                }
                solution.add(replaced);
            }
            solutions.put(entry.getKey(), solution);
        }
        return solutions;
    }

    static public Map<BrokerComm, List<String>> mergePlans(List<String> publicPlan, Map<BrokerComm, List<String>> agentsPlans) {
        Map<BrokerComm, List<String>> finalPlans = new HashMap<>();
        
        Map<BrokerComm, Iterator<String>> planIterators = new HashMap<>();
        for (Entry<BrokerComm, List<String>> entry : agentsPlans.entrySet()) {
            planIterators.put(entry.getKey(), entry.getValue().iterator());
            finalPlans.put(entry.getKey(), new ArrayList<String>());
        }
        
        int pubStepCounter = 1;
        for (String pubAction : publicPlan) {
            int nextPubStepCounter = pubStepCounter;
            for (Entry<BrokerComm, Iterator<String>> entry : planIterators.entrySet()) {
                Iterator<String> iterator = entry.getValue();
                int curStepCounter = pubStepCounter;
                while (true) {
                    String action = iterator.next();
                    if (pubAction.equals(action)) {
                        break;
                    }
                    addToFinalPlan(finalPlans.get(entry.getKey()), action, curStepCounter);
                    curStepCounter++;
                }
                if (curStepCounter > nextPubStepCounter) {
                    nextPubStepCounter = curStepCounter;
                }
            }
            
            pubStepCounter = nextPubStepCounter;
            for (Entry<BrokerComm, List<String>> entry : finalPlans.entrySet()) {
                String curAgentName = "_"+entry.getKey().agentName.replace('-', '_');
                if (pubAction.contains(curAgentName)) {
                    addToFinalPlan(entry.getValue(), pubAction, pubStepCounter);
                    break;
                }
            }
            pubStepCounter++;
        }
        return finalPlans;
    }

    static private void addToFinalPlan(List<String> plan, String action, int time) {
        plan.add(time + ": (" + action.replace("_", " ") + ")");
    }


    
    private List<String> startPlanning() {
        while (true) {
            System.out.println("=== ITERATION " + (++iteration) + " ===");

            if (Settings.RESET_IN_SECOND_ITERATION && iteration == 2) {
                for (BrokerComm agentComm : agentComms) {
                    agentComm.sendMessage(new Message(Command.RESET, null));
                }
            }

            PlanSet<String> intersection = planAllAgents();

            if (!intersection.isEmpty()) {
                System.out.println("Solution found in iteration " + iteration);
                return intersection.getRandomPlan();
            } else {
                System.out.println("--> intersection is empty");
            }

            exchangeLandmarks();
        }
    }

    private void exchangeLandmarks() {
        System.out.println("--> exchanging public landmarks");
        for (BrokerComm agentComm : agentComms) {
            if (!agentsWithPubliclySolvableProblems.contains(agentComm)) {
                agentComm.sendMessage(new Message(Command.RESET_LANDMARKS, null));
            }
        }

        for (BrokerComm agentComm : agentComms) {
            if (agentsWithPubliclySolvableProblems.contains(agentComm)) {
                continue;
            }
            agentComm.sendMessage(new Message(Command.COMPUTE_PUBLIC_LANDMARKS, null));

            Collection<Landmark> landmarks = (Collection<Landmark>) agentComm.receiveMessage(Command.COMPUTE_PUBLIC_LANDMARKS);
            for (BrokerComm comm : agentComms) {
                if (!agentComm.equals(comm)) {
                    if (!agentsWithPubliclySolvableProblems.contains(comm)) {
                        comm.sendMessage(new Message(Command.ADD_PUBLIC_LANDMARKS, landmarks));
                    }
                }
            }
        }
    }

    private PlanSet<String> planAllAgents() {
        int round = 1;

        PlanSet<String> intersection = null;

        for (BrokerComm agentComm : agentComms) {
            if (!agentsWithPubliclySolvableProblems.contains(agentComm)) {
                agentComm.sendMessage(new Message(Command.PLAN, null));
            }
        }

        List<PlanSet<String>> planSets = new ArrayList<>();
        int readyAgents = agentsWithPubliclySolvableProblems.size();
        
        LastReachableProcessor lastReachableMap = new LastReachableProcessor(numberOfAgents);
        
        while (readyAgents < agentComms.size()) {
            BrokerComm agentComm = getAvailableComm();
            Message message = agentComm.receiveMessage();
            switch (message.command) {
            case PLAN:
                PlanSet<String> curPlanSet = (PlanSet<String>) message.content;
                planSets.add(curPlanSet);
                readyAgents++;
                break;
            case COMPUTE_LAST_REACHABLE_BY_ALL_OTHER_AGENTS:
                List<String> publicPlan = (List<String>) message.content;

                if (lastReachableMap.addPlan(agentComm, publicPlan)) {
                    for (BrokerComm comm : agentComms) {
                        if (!comm.equals(agentComm)) {
                            comm.sendMessage(new Message(Command.COMPUTE_LAST_REACHABLE, publicPlan));
                        }
                    }
                }

                break;
            case COMPUTE_LAST_REACHABLE:
                Pair<List<String>, Integer> answer = (Pair<List<String>, Integer>) message.content;
                lastReachableMap.processAnswer(answer.getLeft(), answer.getRight());
                break;
            default:
                throw new IllegalArgumentException("Unexpected command: " + message.command);
            }
        }

        for (PlanSet<String> planSet : planSets) {
            if (intersection == null) {
                intersection = planSet;
            } else {
                if (!intersection.isEmpty()) {
                    intersection = intersection.intersectWith(planSet);
                    round++;
                }
            }
        }

        if (intersection.isEmpty()) {
            System.out.println("--> intersection empty after " + round + " rounds");
        }
        
        for (BrokerComm comm : agentComms) {
            comm.sendMessage(new Message(Command.KILL_ALL, null));
            comm.receiveMessage(Command.OK);
        }

        return intersection;
    }

    private BrokerComm getAvailableComm() {
        while (true) {
            for (BrokerComm comm : agentComms) {
                if (comm.isAvailable()) {
                    return comm;
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // that's OK
            }
        }
    }

    private void exchangeExternalActions() {
        externalActionsMap = new HashMap<>();
        
        isProblemPubliclySolvable = true;
        agentsWithPubliclySolvableProblems = new HashSet<>();
        for (BrokerComm agentComm : agentComms) {
            ExternalActions externalActions = (ExternalActions) agentComm.receiveMessage(Command.EXTERNAL_ACTIONS);
            externalActionsMap.put(externalActions.getAgentName(), externalActions);
            if (externalActions.isPubliclySolvable()) {
                agentsWithPubliclySolvableProblems.add(agentComm);
            } else {
                isProblemPubliclySolvable = false;
            }
        }
        for (BrokerComm agentComm : agentComms) {
            agentComm.sendMessage(new Message(Command.EXTERNAL_ACTIONS, externalActionsMap));
        }
    }

    private void recieveAtomsWithHyphens() {
        extractedAtoms = new HashMap<>();
        for (BrokerComm agentComm : agentComms) {
            extractedAtoms.putAll((Map<String, String>) agentComm.receiveMessage(Command.ATOMS_WITH_HYPHENS));
            agentComm.sendMessage(Message.OK);
        }
        
        translationTable = new HashMap<>();
        for (BrokerComm agentComm : agentComms) {
            translationTable.putAll((Map<String, String>) agentComm.receiveMessage(Command.TRANSLATION_TABLE));
            agentComm.sendMessage(Message.OK);
        }
    }

    public void connectToAgents() {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(basePort);
            System.out.println("Connection Socket Created");
            try {
                for (int i=0; i<numberOfAgents; i++) {
                    System.out.println("Waiting for Connection");
                    BrokerComm agentComm = new BrokerComm(serverSocket.accept());
                    agentComm.connect();
                    agentComms.add(agentComm);
                }
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + basePort);
            System.exit(1);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Could not close port: " + basePort);
                System.exit(1);
            }
        }
    }
}
