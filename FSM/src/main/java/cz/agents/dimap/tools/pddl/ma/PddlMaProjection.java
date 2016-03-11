package cz.agents.dimap.tools.pddl.ma;

import java.util.HashMap;
import java.util.Map;

import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.types.PddlType;

abstract public class PddlMaProjection implements MaProjection {

    protected Map<PddlName, PddlType> agents;
    protected PddlProblem originalProblem;
    protected PddlAddl addl;

    // for tests
    PddlMaProjection() {
    }
    
    public PddlMaProjection(PddlProblem problem, PddlAddl addl) {
        this.addl = addl;
        
        checkValidity(problem, addl);
        
        agents = extractAgents(problem, addl);
        originalProblem = problem;
    }

    private Map<PddlName, PddlType> extractAgents(PddlProblem problem, PddlAddl addl) {
        Map<PddlName, PddlType> agentNames = new HashMap<>();
        if (addl.agentsList != null) {
            for (PddlName agentName : addl.agentsList) {
                agentNames.put(agentName, problem.objects.getBindings().get(agentName));                
            }
        }
        if (addl.agentTypes != null) {
            for (PddlName agentTypeName : addl.agentTypes) {
                PddlType agentType = new PddlType(agentTypeName);
                for (PddlName agentName : problem.getObjectsByType(agentType)) {
                    PddlType agentSpecificType = problem.objects.getBindings().get(agentName);
                    agentNames.put(agentName, agentSpecificType);
                }
            }
        }
        
        if (agentNames.isEmpty()) {
            System.err.println("PDDL ERROR: Extracted list of agent names is empty!");
        }
        
        return agentNames;
    }

    private void checkValidity(PddlProblem problem, @SuppressWarnings("unused") PddlAddl addl) {
        for (PddlName predicate : problem.domain.predicateTypes.keySet()) {
            if (predicate.name.contains("-")) {
                throw new IllegalArgumentException("Predicate should not contain '-'! "+predicate);
            }
        }
        for (PddlName constant : problem.domain.constants.getBindings().keySet()) {
            if (constant.name.contains("-")) {
                System.out.println("Warning: Constant contains '-'! : " +constant);
            }

        }
        for (PddlAction action : problem.domain.actions) {
            if (action.name.contains("-")) {
                System.out.println("Warning: Action name contains '-'! : " +action.name);
            }
        }
    }

    @Override
    public Map<PddlName, PddlType> getAgents() {
        return agents; 
    }

    @Override
    public PddlName getOriginalProblemName() {
        return originalProblem.problemName; 
    }
}