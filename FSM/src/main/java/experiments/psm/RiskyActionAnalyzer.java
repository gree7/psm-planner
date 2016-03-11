package experiments.psm;

import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.RelaxedPGWithHiddenDependencies;
import cz.agents.dimap.tools.RelaxedPGWithHiddenDependencies.Stats;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class RiskyActionAnalyzer extends Analyzer {

    @Override
    int[] getStats(MultiAgentProblem maProblem) {
        PddlProblem problem = maProblem.toSingleAgentProblem();
        Stats stats = RelaxedPGWithHiddenDependencies.extractReachableActionsStats(problem); 
        
        return new int[] {stats.numOfPublicActions, stats.numOfPotentiallyRiskyPublicActions, stats.numOfRiskyPublicActions, stats.numOfPotentiallyRiskyPublicActions + stats.numOfRiskyPublicActions};
    }
    
    
    @Override
    String[] getDescriptions() {
        return new String[] {"total public actions", "potentially", "risky", "total"};
    }

    public static void main(String[] args) {
        Analyzer analyzer = new RiskyActionAnalyzer();
        analyzer.analyze();
    }
}
