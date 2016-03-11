package experiments.psm.fmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cz.agents.dimap.Settings;
import cz.agents.dimap.psm.operator.Binding;
import cz.agents.dimap.psm.planner.PddlMultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlActionInstance;
import cz.agents.dimap.tools.pddl.PddlCondition;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.ma.ActionType;
import cz.agents.dimap.tools.pddl.types.PddlPredicateType;
import cz.agents.dimap.tools.pddl.types.PddlType;
import cz.agents.dimap.tools.pddl.types.PddlTypeMap;

public class FmapMultiAgentProblem extends PddlMultiAgentProblem {

    private static final PddlName depotName = new PddlName("depot");
    private static final PddlName distributorName = new PddlName("distributor");
    private static final PddlName depotPlName = new PddlName("depotpl");
    private static final PddlName distributorPlName = new PddlName("distributorpl");
    private static final PddlType agentPlType = new PddlType("agentpl");
    private static final PddlType placeType = new PddlType("place");

    private static final PddlName plPredicateName = new PddlName("pl");
    private static final PddlName clearName = new PddlName("clear");
    private static final PddlName fullName = new PddlName("full");

    public FmapMultiAgentProblem(AgentInfo... agents) throws Exception {
		super(createProblems(agents));
	}

    public FmapMultiAgentProblem(Map<String, PddlProblem> problems) {
        super(processMaProblem(problems));
    }

	public static Map<String, PddlProblem> createProblems(AgentInfo... agents) throws Exception {
        //DotFormat.elevatorsDomain = agents[0].domainFile.getAbsoluteFile().getPath().contains("Elevators");

	    Map<String, PddlProblem> problems = new HashMap<String, PddlProblem>(agents.length);
	    for (AgentInfo agent : agents) {

	        PddlDomain domain = readDomain(agent);
	        PddlProblem problem = readProblem(agent, domain);

	        problems.put(agent.agentName, problem);
	    }

	    return processMaProblem(problems);
	}

    public static Map<String, PddlProblem> processMaProblem(Map<String, PddlProblem> problems) {
        List<String> agentNames = new ArrayList<>(problems.keySet());
        Collections.sort(agentNames);
        for (Entry<String, PddlProblem> entry : problems.entrySet()) {
		    PddlProblem problem = entry.getValue();
            String agentName = entry.getKey();
		    
            if (Settings.MAKE_CONSTANTS_PUBLIC_IN_FMAP) {
                makeConstantsPublic(problem);
            }
            
		    if (problem.domainName.equals(new PddlName("depot"))) {
	            fixSharedData(problem);
	        }
			problem.translateFmapToMa();

            fixDepots(problem, agentName);
            fixElevators(problem, agentName);

            groundAgentParam(problem, agentName);

            if (Settings.ALLOW_PRIVATE_GOALS) {
                fixPrivateGoals(agentName, problem, agentNames);
            }
            
			problem.setActionAccessTypes();
		}
        
        fixAgentType(problems);

        return problems;
    }

    public static PddlProblem readProblem(AgentInfo agent, PddlDomain domain) throws Exception {
        try {
        	return new PddlProblem(domain, agent.problemFile.getPath());
        }
        catch (Exception e) {
        	System.err.println("ERROR PARSING PROBLEM file: "+agent.problemFile);
        	System.err.println(e.getMessage());
        	System.err.println();
        	throw e;
        }
    }

    public static PddlDomain readDomain(AgentInfo agent) throws Exception {
        try {
        	return new PddlDomain(agent.domainFile.getPath());
        }
        catch (Exception e) {
        	System.err.println("ERROR PARSING DOMAIN file: "+agent.domainFile);
        	System.err.println(e.getMessage());
        	System.err.println();
            throw e;
        }
    }

    private static void fixElevators(PddlProblem problem, String agentName) {
        if (!problem.domainName.equals(new PddlName("elevators"))) {
            // not elevator problem => nothing to do
            return;
        }
        
        Collection<PddlAction> toRemove = new ArrayList<>();
        
        for (PddlAction action : problem.domain.actions) {
            if ( (agentName.startsWith("slow") && action.name.contains("fast"))
                || (agentName.startsWith("fast") && action.name.contains("slow")) ) {
                toRemove.add(action);
            }
        }
        
        problem.domain.actions.removeAll(toRemove);
    }


    private static void groundAgentParam(PddlProblem problem, String agentName) {
            
        List<PddlAction> newActions = new ArrayList<>(problem.domain.actions.size());
        for (PddlAction action : problem.domain.actions) {
            Binding binding = getAgentBinding(agentName, problem, action);
            if (!binding.isEmpty()) {
                PddlAction agentAction = PddlActionInstance.createInstanceWithUnderscores(action, binding);
                newActions.add(agentAction);
            } else {
                if (!actionContainsAgentParameter(action, problem)) {
                    PddlAction agentAction = PddlAction.createRenamedAction(action.name+"_"+agentName, action);
                    newActions.add(agentAction);
                }
            }
        }
        problem.domain.actions.clear();
        problem.domain.actions.addAll(newActions);
    }

    private static boolean actionContainsAgentParameter(PddlAction action, PddlProblem problem) {
        for (PddlType type : action.parameters.getBindings().values()) {
            for (PddlName typeName : type.eithers) {
                PddlType pddlType = problem.domain.types.getBindings().get(typeName);
                if (type.equals(new PddlType("agent")) 
                    || (pddlType!=null && pddlType.equals(new PddlType("agent")))
                    ) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Binding getAgentBinding(String agent, PddlProblem problem, PddlAction action) {
        Binding binding = new Binding();
        PddlName agentName = new PddlName(agent);
        for (Entry<PddlName, PddlType> parameter : action.parameters.getBindings().entrySet()) {
            if (problem.getObjectsByType(parameter.getValue()).contains(agentName)) {
                binding.addBinding(parameter.getKey(), agentName);
            }
            
        }
        return binding;
    }

    private static void fixDepots(PddlProblem problem, String agentName) {
        if (!problem.domainName.equals(new PddlName("depot"))) {
            // not depot problem => nothing to do
            return;
        }
        
        if (agentName.startsWith("depot") || agentName.startsWith("distributor")) {
            fixDepotProblem(problem);
        } else if (agentName.startsWith("truck")) {
            fixTruckProblem(problem);
        } else {
            throw new IllegalArgumentException("Invalid agent name: " + agentName);
        }
    }

    private static void fixDepotProblem(PddlProblem problem) {
        fixDepotDomain(problem.domain);
        
        addPlObjects(problem);
        fixInit(problem, clearName, fullName);
    }

    private static void fixTruckProblem(PddlProblem problem) {
        fixTruckDomain(problem.domain);

        addPlObjects(problem);
        fixInit(problem, clearName, fullName);
    }

    private static void fixDepotDomain(PddlDomain domain) {
        fixEitherTypes(domain);
        addPredicates(domain);
        addOwnerAgentToActions(domain);
        fixActionsForPredicate(domain, clearName, fullName);
    }

    private static void fixEitherTypes(PddlDomain domain) {
        domain.types.removeAssignment(depotName);
        domain.types.removeAssignment(distributorName);
        domain.types.addAssignment(new PddlName("agentpl"), new PddlType("agent"));
        domain.types.addAssignment(Arrays.asList(depotName, distributorName), agentPlType);
        domain.types.addAssignment(Arrays.asList(depotPlName, distributorPlName), placeType);
    }

    private static void addPredicates(PddlDomain domain) {
        PddlPredicateType plPredicate = createPlPredicate();
        domain.predicateTypes.put(plPredicate.predicateName, plPredicate);
        PddlPredicateType fullPredicate = new PddlPredicateType(fullName, new PddlTypeMap<>( domain.predicateTypes.get(clearName).arguments ));
        domain.predicateTypes.put(fullPredicate.predicateName, fullPredicate);
    }

    private static PddlPredicateType createPlPredicate() {
        PddlTypeMap<PddlName> arguments = new PddlTypeMap<>();
        arguments.addAssignment(new PddlName("?a"), agentPlType);
        arguments.addAssignment(new PddlName("?p"), placeType);
        return new PddlPredicateType(plPredicateName, arguments);
    }

    private static void addOwnerAgentToActions(PddlDomain domain) {
        for (PddlAction action : domain.actions) {
            action.parameters.addAssignment(new PddlName("?a"), agentPlType);
            PddlTerm plPrecondition = new PddlTerm(plPredicateName);
            plPrecondition.arguments.add(new PddlName("?a"));
            plPrecondition.arguments.add(new PddlName("?p"));
            action.precondition.positives.add(plPrecondition);
        }
    }

    private static void fixActionsForPredicate(PddlDomain domain, PddlName negativePredicate, PddlName inversePredicate) {
        for (PddlAction action : domain.actions) {
            for (PddlTerm negativePrecondition : action.precondition.negatives) {
                if (negativePrecondition.name.equals(negativePredicate)) {
                    action.precondition.addPositiveCondition(PddlTerm.renamePredicate(negativePrecondition, inversePredicate.name));
                } else {
                    throw new IllegalArgumentException("Unexpected negative precondition: " + negativePrecondition);
                }
            }
            action.precondition.negatives.clear();

            for (PddlTerm effect : action.effect.positives) {
                if (effect.name.equals(negativePredicate)) {
                    PddlTerm convertedPredicate = PddlTerm.renamePredicate(effect, inversePredicate.name);
                    action.effect.addNegativeCondition(convertedPredicate);                
                    action.precondition.addPositiveCondition(convertedPredicate.clone());
                }
            }
            for (PddlTerm effect : action.effect.negatives) {
                if (effect.name.equals(negativePredicate)) {
                    action.effect.addPositiveCondition(PddlTerm.renamePredicate(effect, inversePredicate.name));
                }
            }
        }
    }

    private static void addPlObjects(PddlProblem problem) {
        Map<PddlName, PddlType> newObjects = new HashMap<>();
        List<PddlTerm> initFacts = new ArrayList<>();
        for (Entry<PddlName, PddlType> object : problem.objects.getBindings().entrySet()) {
            PddlName objectName = object.getKey();
            PddlName objectPlName = new PddlName(objectName.name+"pl");
            if (object.getValue().equals(new PddlType("distributor"))) {
                newObjects.put(objectPlName, new PddlType("distributorpl"));
                initFacts.add(new PddlTerm(plPredicateName, Arrays.asList(objectName, objectPlName)));
            } else if (object.getValue().equals(new PddlType("depot"))) {
                newObjects.put(objectPlName, new PddlType("depotpl"));
                initFacts.add(new PddlTerm(plPredicateName, Arrays.asList(objectName, objectPlName)));
            }
        }
        for (Entry<PddlName, PddlType> entry : newObjects.entrySet()) {
            problem.objects.addAssignment(entry.getKey(), entry.getValue());
        }

        for (PddlTerm term : problem.init.positives) {
            List<PddlName> newArgs = new ArrayList<>();
            boolean isRenamed = false;
            for (PddlName arg : term.arguments) {
                if (arg.name.startsWith("depot") || arg.name.startsWith("distributor")) {
                    newArgs.add(new PddlName(arg.name+"pl"));
                    isRenamed = true;
                } else {
                    newArgs.add(arg);
                }
            }
            if (isRenamed) {
                initFacts.add(new PddlTerm(term.name, newArgs));
            } else {
                initFacts.add(term);
            }
        }

        problem.init.positives.clear();
        problem.init.positives.addAll(initFacts);
    }

    private static void fixInit(PddlProblem problem, PddlName negativePredicate, PddlName inversePredicate) {
        for (PddlTerm term : problem.init.negatives) {
            if (term.name.equals(negativePredicate)) {
                problem.init.addPositiveCondition(PddlTerm.renamePredicate(term, inversePredicate.name));
            }
        }
    }

    private static void fixSharedData(PddlProblem problem) {
        problem.fmapSharedNames.add(plPredicateName);
        problem.fmapSharedNames.add(fullName);
    }

    private static void fixTruckDomain(PddlDomain domain) {
        fixEitherTypes(domain);
        addPredicates(domain);
        fixActionsForPredicate(domain, clearName, fullName);
    }

    private static void fixAgentType(Map<String, PddlProblem> agentProblems) {
		if (agentProblems.isEmpty()) { return; }
	    PddlProblem someProblem = agentProblems.values().iterator().next();
        PddlName agentName = new PddlName("agent");
        PddlType agentType = new PddlType("agent");
        if (!someProblem.domain.types.getBindings().values().contains(agentType)) {
	        // for example Openstacks problem - every file is for single agent only
            List<PddlName> agentTypes = new ArrayList<>(agentProblems.size());
            for (String agent : agentProblems.keySet()) {
                agentTypes.add(new PddlName(agent+"agent"));
            }

            for (Entry<String, PddlProblem> entry : agentProblems.entrySet()) {
                PddlProblem problem = entry.getValue();
                problem.domain.types.addAssignment(agentName, new PddlType("object"));
                problem.domain.types.addAssignment(agentTypes, new PddlType("agent"));
                
                for (PddlAction action : problem.domain.actions) {
                    action.parameters.addAssignment(new PddlName("?ag"), new PddlType(entry.getKey()+"agent"));
                }
                
                for (String agent : agentProblems.keySet()) {
                    problem.objects.removeAssignment(new PddlName(agent));
                    problem.objects.addAssignment(new PddlName(agent), new PddlType(agent+"agent"));
                }
            }

	    }
    }

    private static void makeConstantsPublic(PddlProblem problem) {
        switch (problem.domainName.name) {
        case "ma-blocksworld":
            break;
        case "depot":
            addSharedPredicateNames(problem, "located", "placed");
            break;
        case "driverlog":
            addSharedPredicateNames(problem, "path");
            break;
        case "elevators":
            addSharedPredicateNames(problem, "above");
            break;
        case "logistics":
            addSharedPredicateNames(problem, "in-city");
            break;
        case "openstacks":
            break;
        case "rover":
            addSharedPredicateNames(problem, "visible_from");
            break;
        case "satellite":
            break;
        case "woodworking":
            break;
        case "zeno-travel":
            addSharedPredicateNames(problem, "next");
            break;

        //test domains
        case "beerproblem":
            break;
        default:
            throw new IllegalArgumentException("Unknown domain: " + problem.domainName);
        }
    }

    private static void fixPrivateGoals(String agentName, PddlProblem problem, Collection<String> agentNames) {
        switch (problem.domainName.name) {
        case "ma-blocksworld":
            removeSharedPredicateNames(problem, agentName, true, "holding");
            break;
        case "depot":
            removeSharedPredicateNames(problem, agentName, true, "at", "pl", "placed");
            break;
        case "driverlog":
            removeSharedPredicateNames(problem, agentName, true, "at");
            break;
        case "elevators":
            break;
        case "logistics":
            break;
        case "openstacks":
            if (agentName.equals("manufacturer")) {
                removeSharedPredicateNames(problem, agentName, true, "waiting", "shipped", "next_count");
            } else if (agentName.equals("manager")) {
                removeSharedPredicateNames(problem, agentName, false, "waiting", "shipped", "next_count");
            }
            break;
        case "rover":
            break;
        case "satellite":
            removeSharedPredicateNames(problem, agentName, true, "pointing");
            break;
        case "woodworking":
            removeSharedPredicateNames(problem, agentName, true, "empty", "has_colour", "in_highspeed_saw", "grind_treatment_change");
            break;
        case "zeno-travel":
            removeSharedPredicateNames(problem, agentName, true, "at");
            break;
        //test domains
        case "beerproblem":
            if (agentName.equals("plane")) {
                problem.goal.clear();
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown domain: " + problem.domainName);
        }
        
        addVirtualPublicGoals(problem, agentName, agentNames);
    }

    /* 
     * Fixes problem when PSM is used and some goal is empty 
     * (or more generally some private goal of one agent requires public action of another agent).
     * 
     * We add a virtual public goal for each agent that needs to be fulfilled. 
     */
    private static void addVirtualPublicGoals(PddlProblem problem, String agentName, Collection<String> agentNames) {
        if (Settings.VERBOSE) {
            System.out.println("Adding virtual goal to problem " + problem.problemName);
        }

        PddlTerm planningPhaseTerm = new PddlTerm("planningphase");
        problem.domain.predicateTypes.put(planningPhaseTerm.name, new PddlPredicateType(planningPhaseTerm.name, new PddlTypeMap<PddlName>()));
        problem.init.addPositiveCondition(planningPhaseTerm);
        for (PddlAction action : problem.domain.actions) {
            action.precondition.addPositiveCondition(planningPhaseTerm);
        }
        problem.fmapSharedNames.add(planningPhaseTerm.name);
        problem.domain.sharedPredicateNames.add(planningPhaseTerm.name);
        
        List<PddlTerm> allGoalTerms = new ArrayList<>();
        PddlTerm agentGoalTerm = null;
        PddlTerm prevGoalTerm = null;
        PddlCondition oldGoal = problem.goal.clone();
        PddlTerm lastGoal = null;
        problem.goal.clear();
        for (String agent : agentNames) {
            PddlTerm goalTerm = new PddlTerm("virtualgoal_"+agent);
            if (agent.equals(agentName)) {
                agentGoalTerm = goalTerm;
                prevGoalTerm = lastGoal;
            }
            allGoalTerms.add(goalTerm);

            problem.domain.predicateTypes.put(goalTerm.name, new PddlPredicateType(goalTerm.name, new PddlTypeMap<PddlName>()));
            
            problem.fmapSharedNames.add(goalTerm.name);
            problem.domain.sharedPredicateNames.add(goalTerm.name);
            
            lastGoal = goalTerm;
        }

        problem.goal.addPositiveCondition(lastGoal);

        PddlCondition effectCondition = new PddlCondition();
        effectCondition.addPositiveCondition(agentGoalTerm);

        if (prevGoalTerm != null) { 
            oldGoal.addPositiveCondition(prevGoalTerm);
            effectCondition.addNegativeCondition(prevGoalTerm);
        } else {
            oldGoal.addPositiveCondition(planningPhaseTerm);
            effectCondition.addNegativeCondition(planningPhaseTerm);
        }
        
        PddlAction goalAction = new PddlAction("goalaction_"+agentName, new PddlTypeMap<PddlName>(), oldGoal, effectCondition, ActionType.PARSED);

        problem.domain.actions.add(goalAction);  
    }

    private static void addSharedPredicateNames(PddlProblem problem, String... predicates) {
        for (String predicate : predicates) {
            PddlName predicateName = new PddlName(predicate);
            problem.domain.sharedPredicateNames.remove(predicateName);
        }
    }
    
    private static void removeSharedPredicateNames(PddlProblem problem, String agentName, boolean removeGoals, String... predicates) {
        for (String predicate : predicates) {
            PddlName predicateName = new PddlName(predicate);
            problem.domain.sharedPredicateNames.remove(predicateName);

            if (removeGoals) {
                List<PddlTerm> goalsToRemove = new ArrayList<>();
                for (PddlTerm goal : problem.goal.positives) {
                    if (goal.name.equals(predicateName) && !goal.toString().contains(agentName)) {
                        goalsToRemove.add(goal);
                    }
                }
                problem.goal.positives.removeAll(goalsToRemove);
            }
        }
    }
}
