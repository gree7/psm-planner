package experiments.psm.file;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.dimap.Settings;
import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.psm.planner.PddlMultiAgentProblem;
import cz.agents.dimap.tools.fd.Downward;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlActionInstance;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.ma.ActionType;
import cz.agents.dimap.tools.pddl.ma.IpcMaProjection;
import cz.agents.dimap.tools.sas.SasFile;

public class CoDmapMultiAgentProblemMaStrips extends PddlMultiAgentProblem {

    Map<String, String> atomsWithHyphens = new HashMap<>();
    
    private CoDmapMultiAgentProblemMaStrips(MultiAgentProblem maProblem, Map<String, String> atomsWithHyphens) {
        super(createProblems(maProblem));
        this.atomsWithHyphens = atomsWithHyphens;
    }
    
    static public CoDmapMultiAgentProblemMaStrips getInstance(MultiAgentProblem maProblem) {
        return new CoDmapMultiAgentProblemMaStrips(maProblem, extractAtomsWithHyphens(maProblem));
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
        
        PddlProblem singleProblem = maProblem.toSingleAgentProblem();
        SasFile sasFile = Downward.runTranslator(singleProblem);

        Map<String, PddlProblem> ret = new HashMap<String, PddlProblem>();
        Map<String, Set<PddlAction>> agentActions = extractAgentActions(singleProblem, sasFile.operatorNames, maProblem.getAgents());
        Map<String, Set<PddlTerm>> agentFacts = extractAgentFacts(agentActions);

        Set<PddlTerm> publicFacts = IpcMaProjection.getPublicFacts(agentFacts);

        if (Settings.MASTRIPS_PUBLIC_FACT_HAS_TO_BE_IN_EFFECT) {
            retainEffectFactsOnly(publicFacts, agentActions);
        }
        
        publicFacts.addAll( singleProblem.goal.getFacts() );

        for (String agent : maProblem.getAgents()) {

            PddlProblem problem = maProblem.getProblems().get(agent);
            PddlDomain domain = problem.domain;
            
            domain.sharedPredicateNames.clear();
            domain.sharedPredicates.objects.clear();
            domain.sharedPredicates.objects.addAll(publicFacts);
            
            domain.actions.clear();
            for (PddlAction action : agentActions.get(agent)) {
                domain.actions.add(PddlAction.changeType(action, ActionType.PARSED));
            }
            problem.setActionAccessTypes();
            ret.put(agent, problem);
        }
        
        return ret;
    }

    private static void retainEffectFactsOnly(Set<PddlTerm> publicFacts, Map<String, Set<PddlAction>> agentActions) {
        Set<PddlTerm> effectFacts = new HashSet<>();
        
        for (Set<PddlAction> actions : agentActions.values()) {
            for (PddlAction action : actions) {
                effectFacts.addAll(action.getEffectFacts());
            }
        }

        publicFacts.retainAll(effectFacts);
    }

    private static Map<String, Set<PddlTerm>> extractAgentFacts(Map<String, Set<PddlAction>> agentActions) {
        Map<String, Set<PddlTerm>> agentFacts = new HashMap<>();
        
        for (Entry<String, Set<PddlAction>> entry : agentActions.entrySet()) {
            Set<PddlTerm> facts = new HashSet<>();
            for (PddlAction action : entry.getValue()) {
                facts.addAll(action.getEffectFacts());
                facts.addAll(action.getPreconditionFacts());
            }
            agentFacts.put(entry.getKey(), facts);
        }
        
        return agentFacts;
    }

    private static Map<String, Set<PddlAction>> extractAgentActions(PddlProblem problem, List<String> sasOperators, Collection<String> agentNames) {
        Map<String, Set<PddlAction>> agentActions = new HashMap<>();

        for (String agent : agentNames) {
            agentActions.put(agent, new HashSet<PddlAction>());
        }
        
        for (String operator : sasOperators) {
//            operator = operator.substring(4);
            String agentName = getAgentName(operator, agentNames);

            PddlAction action = PddlActionInstance.parsePlannedAction(operator, problem);
            if (action != null) {
                agentActions.get(agentName).add(action);
            } else {
                System.out.println("operator: " + operator);
            }
        }
        return agentActions;
    }

    private static String getAgentName(String operator, Collection<String> agentNames) {
        String agentName = null;
        int index = operator.length();
        for (String curAgentName : agentNames) {
            if (operator.contains(" "+curAgentName)) {
                if (agentName == null) {
                    agentName = curAgentName;
                    index = operator.indexOf(curAgentName);
                } else {
                    if (Settings.IS_CODMAP) {
                        int curIndex = operator.indexOf(curAgentName);
                        if (curIndex < index) {
                            agentName = curAgentName;
                            index = curIndex;
                        }
                    } else {
                        throw new IllegalArgumentException("Multiple agents in operator name: " + operator + " -- " + agentNames);
                    }
                }
            }
        }
        if (agentName == null) {
            throw new IllegalArgumentException("No agents in operator name: " + operator + " -- " + agentNames);
        }
        return agentName;
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
