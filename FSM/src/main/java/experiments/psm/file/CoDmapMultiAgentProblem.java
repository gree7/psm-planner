package experiments.psm.file;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.psm.planner.PddlMultiAgentProblem;
import cz.agents.dimap.tools.GroundedActionsExtractor;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.ma.ActionType;

public class CoDmapMultiAgentProblem extends PddlMultiAgentProblem {

    Map<String, String> atomsWithHyphens = new HashMap<>();
    
    private CoDmapMultiAgentProblem(MultiAgentProblem maProblem, Map<String, String> atomsWithHyphens) {
        super(createProblems(maProblem));
        this.atomsWithHyphens = atomsWithHyphens;
    }
    
    static public CoDmapMultiAgentProblem getInstance(MultiAgentProblem maProblem) {
        return new CoDmapMultiAgentProblem(maProblem, extractAtomsWithHyphens(maProblem));
    }


    private static Map<String, String> extractAtomsWithHyphens(MultiAgentProblem maProblem) {
        Map<String, String> atomsWithHyphens = new HashMap<>();
        for (Entry<String, PddlProblem> entry : maProblem.getProblems().entrySet()) {
            atomsWithHyphens.putAll(extractAtomsWithHyphens(entry.getKey(), entry.getValue()));
        }
        return atomsWithHyphens;
    }

    public static Map<String, String> extractAtomsWithHyphens(String agentName, PddlProblem problem) {
        Map<String, String> atomsWithHyphens = new HashMap<>();

        if (agentName.contains("_")) {
            atomsWithHyphens.put(agentName.replace('_', ' '), agentName);
        }

        for (PddlName object : problem.objects.getBindings().keySet()) {
            if (object.name.contains("_")) {
                atomsWithHyphens.put(object.name.replace('_', ' '), object.name);
            }
        }
        for (PddlAction action : problem.domain.actions) {
            if (action.name.contains("_")) {
                atomsWithHyphens.put(action.name.replace('_', ' '), action.name);
            }
        }
        return atomsWithHyphens;
    }

    public static Map<String, PddlProblem> createProblems(MultiAgentProblem maProblem) {

        Map<String, PddlProblem> ret = new HashMap<String, PddlProblem>();

        Set<PddlTerm> allPublicFacts = new HashSet<>();
        Map<String, Set<PddlTerm>> agentFactsMap = new HashMap<>();

        Set<PddlName> constants = extractConstants(maProblem);

        for (Entry<String, PddlProblem> entry : maProblem.getProblems().entrySet()) {

            PddlProblem problem = entry.getValue();

            String agent = entry.getKey();
            String agentName;
            if (agent.contains(".")) {
                agentName = agent.subSequence(0, agent.indexOf('.')).toString();
            } else {
                agentName = agent;
            }

            Set<PddlTerm> agentFacts = extractReachableFacts(problem, agentName, allPublicFacts, constants);
            agentFactsMap.put(agent, agentFacts);
        }
        
        for (Entry<String, PddlProblem> entry : maProblem.getProblems().entrySet()) {

            PddlProblem problem = entry.getValue();
            
            String agent = entry.getKey();
            
            setUpSharedPredicates(allPublicFacts, agentFactsMap.get(agent), problem);

            String agentName;
            if (agent.contains(".")) {
                agentName = agent.subSequence(0, agent.indexOf('.')).toString();
            } else {
                agentName = agent;
            }
            ret.put(agentName, problem);
        }

        return ret;
    }

    public static void setUpSharedPredicates(Set<PddlTerm> allPublicFacts, Set<PddlTerm> agentFacts, PddlProblem problem) {
        agentFacts.addAll(allPublicFacts);
        
        removeNonReachableActions(problem.domain.actions, agentFacts);

        problem.domain.sharedPredicates.objects.addAll(allPublicFacts);

        problem.setActionAccessTypes();
    }

    public static void groundToFacts(PddlProblem problem, String agentName, Set<PddlTerm> myFacts, Set<PddlTerm> publicFacts) {
        GroundedActionsExtractor extractor = new GroundedActionsExtractor(problem, agentName);
        extractor.addReachableFacts(myFacts);
        extractor.addReachableFacts(publicFacts);
        extractor.applyAllActionsRepeatedly(false, new HashSet<PddlName>());

        Set<PddlAction> agentActions = extractor.getAppliedActions();

        PddlDomain domain = problem.domain;
        domain.actions.clear();
        for (PddlAction action : agentActions) {
            domain.actions.add(PddlAction.changeType(action, ActionType.PARSED));
        }
    }

    public static Set<PddlTerm> extractReachableFacts(PddlProblem problem, String agentName, Set<PddlTerm> allPublicFacts, Set<PddlName> constants) {
        GroundedActionsExtractor extractor = new GroundedActionsExtractor(problem, agentName);
        extractor.applyAllActionsRepeatedly(true, constants);
        Set<PddlAction> agentActions = extractor.getAppliedActions();

        PddlDomain domain = problem.domain;
        domain.actions.clear();
        for (PddlAction action : agentActions) {
            domain.actions.add(PddlAction.changeType(action, ActionType.PARSED));
        }

        Set<PddlTerm> agentFacts = extractor.getReachableFacts();
        allPublicFacts.addAll(getPublicFacts(problem, agentFacts));
        return agentFacts;
    }

    public static Set<PddlTerm> extractReachableFacts(PddlProblem problem,
            String agentName, Set<PddlTerm> allPublicFacts) {
        GroundedActionsExtractor extractor = new GroundedActionsExtractor(problem, agentName);
        extractor.applyAllActionsRepeatedly(true, new HashSet<PddlName>());

        Set<PddlAction> agentActions = extractor.getAppliedActions();

        PddlDomain domain = problem.domain;
        domain.actions.clear();
        for (PddlAction action : agentActions) {
            domain.actions.add(PddlAction.changeType(action, ActionType.PARSED));
        }

        Set<PddlTerm> agentFacts = extractor.getReachableFacts();
        allPublicFacts.addAll(getPublicFacts(problem, agentFacts));
        return agentFacts;
    }

    private static Set<PddlName> extractConstants(MultiAgentProblem maProblem) {
        Set<PddlName> constants = null;
        for (PddlProblem problem : maProblem.getProblems().values()) {
            if (constants == null) {
                constants = extractConstants(problem);
            } else {
                constants.retainAll(extractConstants(problem));
            }
        }
        return constants;
    }

    private static Set<PddlName> extractConstants(PddlProblem problem) {
        Set<PddlName> constants = new HashSet<>();
        for (PddlName predicate : problem.domain.predicateTypes.keySet()) {
            constants.add(predicate);
        }
        for (PddlAction action : problem.domain.actions) {
            for (PddlTerm effect : action.getEffectFacts()) {
                constants.remove(effect.name);
            }
        }
        return constants;
    }

    private static void removeNonReachableActions(List<PddlAction> actions, Set<PddlTerm> reachableFacts) {
        for (Iterator<PddlAction> iterator = actions.iterator(); iterator.hasNext();) {
            PddlAction action = iterator.next();
            
            for (PddlTerm fact : action.getPreconditionFacts()) {
                if (!reachableFacts.contains(fact)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    private static Collection<PddlTerm> getPublicFacts(PddlProblem problem, Set<PddlTerm> facts) {
        HashSet<PddlTerm> publicFacts = new HashSet<>();
        nextFact:
            for (PddlTerm fact : facts) {
                for (PddlName type : problem.domain.privatePredicates) {
                    if (fact.name.name.startsWith(type.name)) {
                        continue nextFact;
                    }
                }
                for (PddlName object : problem.privateObjects) {
                    if (fact.arguments.contains(object)) {
                        continue nextFact;
                    }
                }
                publicFacts.add(fact);
            }
        return publicFacts;
    }
    
    @Override
    public List<String> revertPlan(List<String> finalPlan) {
        return revertPlan(finalPlan, atomsWithHyphens, PddlName.translationTable);
    }

    public static List<String> revertPlan(List<String> finalPlan, Map<String, String> atomsWithHyphens, Map<String, String> translationTable ) {
        List<String> solution = new ArrayList<>(); 
        for (String action : finalPlan) {
            String replaced = action;
            for (Entry<String, String> atomEntry : atomsWithHyphens.entrySet()) {
                String name = atomEntry.getValue();
                if (translationTable.containsKey(name)) {
                    name = translationTable.get(name);
                }
                replaced = replaced.replaceAll(" " + atomEntry.getKey(), " " + name);
                replaced = replaced.replaceAll("\\(" + atomEntry.getKey(), "(" + name);
            }
            solution.add(replaced);
        }
        return solution;
    }
}
