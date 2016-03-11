package experiments.psm.fmap;

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
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.ma.ActionType;
import cz.agents.dimap.tools.pddl.ma.IpcMaProjection;
import cz.agents.dimap.tools.sas.SasFile;

public class MaStripsMultiAgentProblem extends PddlMultiAgentProblem {

    public MaStripsMultiAgentProblem(MultiAgentProblem fmapProblem) {
		super(createProblems(fmapProblem));
	}
	
	public static Map<String, PddlProblem> createProblems(MultiAgentProblem fmapProblem) {
	    
        PddlProblem singleProblem = fmapProblem.toSingleAgentProblem();
        SasFile sasFile = Downward.runTranslator(singleProblem);

        Map<String, PddlProblem> ret = new HashMap<String, PddlProblem>();
        Map<String, Set<PddlAction>> agentActions = extractAgentActions(singleProblem, sasFile.operatorNames, fmapProblem.getAgents());
        Map<String, Set<PddlTerm>> agentFacts = extractAgentFacts(agentActions);
        Set<PddlTerm> publicFacts = IpcMaProjection.getPublicFacts(agentFacts);
        
        if (Settings.MASTRIPS_PUBLIC_FACT_HAS_TO_BE_IN_EFFECT) {
            retainEffectFactsOnly(publicFacts, agentActions);
        }
        
        publicFacts.addAll( singleProblem.goal.getFacts() );

		for (String agent : fmapProblem.getAgents()) {

		    PddlProblem problem = fmapProblem.getProblems().get(agent);
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
            operator = operator.substring(4);
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
        for (String curAgentName : agentNames) {
            if (operator.endsWith("_"+curAgentName) || operator.contains("_"+curAgentName+" ") || operator.contains("_"+curAgentName+"_")) {
                if (agentName == null) {
                    agentName = curAgentName;
                } else {
                    throw new IllegalArgumentException("Multiple agents in operator name: " + operator + " -- " + agentNames);
                }
            }
        }
        if (agentName == null) {
            throw new IllegalArgumentException("No agents in operator name: " + operator + " -- " + agentNames);
        }
        return agentName;
    }
}
