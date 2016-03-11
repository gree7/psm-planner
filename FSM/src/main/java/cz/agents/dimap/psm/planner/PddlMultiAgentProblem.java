package cz.agents.dimap.psm.planner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlCondition;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.parser.PddlList;
import cz.agents.dimap.tools.pddl.types.PddlTypeMap;

public class PddlMultiAgentProblem implements MultiAgentProblem {
    private final Map<String, PddlProblem> problems;
    private final PddlProblem relaxedProblem;
    private String imageOutputPath = null;

    public PddlMultiAgentProblem(Map<String, PddlProblem> problems) {
        this.problems = problems;
        this.relaxedProblem = extractSingleAgentProblem(true);
    }

    public PddlProblem toSingleAgentProblem() {
        return extractSingleAgentProblem(false);
    }
    
    private PddlProblem extractSingleAgentProblem(boolean relax) {
        PddlDomain domain = new PddlDomain();
        PddlProblem problem = new PddlProblem(domain);
        for (PddlProblem agentProblem : problems.values()) {
            PddlDomain agentDomain = agentProblem.domain;

            domain.domainName = new PddlName(agentDomain.domainName.name + "-relaxed");
            domain.requirements = new ArrayList<>(agentDomain.requirements);
            domain.types = PddlTypeMap.createUnion(domain.types, agentDomain.types);
            domain.constants = PddlTypeMap.createUnion(domain.constants, agentDomain.constants);
            domain.predicateTypes.putAll(agentDomain.predicateTypes);
            domain.sharedPredicateNames = new ArrayList<>(agentDomain.sharedPredicateNames);
            domain.sharedPredicates = new PddlList<>(agentDomain.sharedPredicates);
            
            if (relax) {
                domain.actions = unionActions(domain.actions, relaxActions(agentDomain.actions));
            } else {
                domain.actions = unionActions(domain.actions, agentDomain.actions);
            }
            
            problem.problemName = domain.domainName;
            problem.domainName = domain.domainName;
            problem.requirements = new ArrayList<>(agentProblem.requirements);
            problem.objects = PddlTypeMap.createUnion(problem.objects, agentProblem.objects);
            problem.init = PddlCondition.createUnion(problem.init, agentProblem.init);
            problem.goal = PddlCondition.createUnion(problem.goal, agentProblem.goal);
        }

        if (relax) {
            problem.problemName = new PddlName(problem.problemName.name + "-relaxed");
        }

        return problem;
    }

    private List<PddlAction> relaxActions(List<PddlAction> actions) {
        List<PddlAction> relaxedActions = new ArrayList<PddlAction>(actions.size());
        for (PddlAction action : actions) {
            relaxedActions.add(PddlAction.createRelaxedAction(action));
        }
        return relaxedActions;
    }

    private List<PddlAction> unionActions(List<PddlAction> actions1, List<PddlAction> actions2) {
        Set<PddlAction> actions = new HashSet<>(actions1);
        actions.addAll(actions2);
        return new ArrayList<>(actions);
    }

    public void setImageOutputPath(String path) {
        imageOutputPath = path;
    }

    @Override
    public String getImageOutputPath() {
        return imageOutputPath;
    }

    @Override
    public Collection<String> getAgents() {
        return problems.keySet(); 
    }

    @Override
    public Map<String, PddlProblem> getProblems() {
        return problems;
    }

    @Override
    public PddlProblem getOriginalRelaxedProblem() {
        return relaxedProblem;
    }
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (Entry<String, PddlProblem> problem : problems.entrySet()) {
            sb.append(problem.getKey() + ": \n");
            sb.append(problem.getValue().domain +"\n");
            sb.append(problem.getValue() +"\n");
            sb.append("Shared predicate names: " + problem.getValue().domain.sharedPredicateNames +"\n");
            sb.append("Shared predicates: " + problem.getValue().domain.sharedPredicates.objects +"\n");
        }
        return sb.toString();
    }

    @Override
    public List<String> revertPlan(List<String> finalPlan) {
        return finalPlan;
    }
}
