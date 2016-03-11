package experiments.psm;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.dimap.causal.InternalDependencies;
import cz.agents.dimap.causal.InternalDependenciesGraph;
import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;

public class InternalDependenciesAnalyzer extends Analyzer {

    public InternalDependenciesAnalyzer() {
        super(false, false, true, false);
    }
    
    @Override
    int[] getStats(MultiAgentProblem maProblem) {
        int success = 0;
        int factCount = 0;
        int sharedFacts = 0; 
        int mergeFacts = 0;
        int actions = 0;
        int pubActions = 0;
        for (Entry<String, PddlProblem> entry : maProblem.getProblems().entrySet()) {
            PddlProblem problem = entry.getValue();
            factCount += countFacts(problem);
            int curSharedFacts = countPubFacts(problem);
            sharedFacts += curSharedFacts;
            actions += problem.domain.actions.size();
            pubActions += countPubActions(problem);
            try {
                InternalDependencies id = new InternalDependenciesGraph(entry.getKey(), problem);
                id.updateProblem(problem);
                mergeFacts += countPubFacts(problem) - curSharedFacts;
                success += 1;
            } catch (Exception e) {
            }
        }
        return new int[] { 
                factCount, 
                sharedFacts,
                mergeFacts,
                (mergeFacts * 100) / (factCount - sharedFacts),
                actions,
                pubActions,
                (success * 100) / maProblem.getProblems().size()};
    }
    
    private int countPubActions(PddlProblem problem) {
        int counter = 0;
        for (PddlAction action : problem.domain.actions) {
            if (action.isPublic()) {
                counter++;
            }
        }
        return counter;
    }

    private int countFacts(PddlProblem problem) {
        Set<PddlTerm> facts = new HashSet<>();
        for (PddlAction action : problem.domain.actions) {
            facts.addAll(action.getPreconditionFacts());
            facts.addAll(action.getEffectFacts());
        }
        return facts.size();
    }

    private int countPubFacts(PddlProblem problem) {
        Set<PddlTerm> facts = new HashSet<>();
        for (PddlAction action : problem.domain.actions) {
            for (PddlTerm fact : action.getPreconditionFacts()) {
                if (problem.isPublicPredicate(fact)) {
                    facts.add(fact);
                }
            }
            for (PddlTerm fact : action.getEffectFacts()) {
                if (problem.isPublicPredicate(fact)) {
                    facts.add(fact);
                }
            }
        }
        return facts.size();
    }

    @Override
    String[] getDescriptions() {
        return new String [] {
            "Facts", 
            "Public facts", 
            "Merge facts", 
            "Disclosure %",
            "Actions", 
            "Pub. actions",
            "Success %"
        };
    }

    public static void main(String[] args) {
        Analyzer analyzer = new InternalDependenciesAnalyzer();
        analyzer.analyze();
    }
}
