package experiments.psm;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.fd.Downward;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.sas.SasFile;

public class PrivatenessAnalyzer extends Analyzer {

    @Override
    int[] getStats(MultiAgentProblem maProblem) {
        PddlProblem problem = maProblem.toSingleAgentProblem();
        SasFile sasFile = Downward.runTranslator(problem);
        SasFile.Stats pubStats = sasFile.getStats(problem);
        return new int[] {pubStats.getPubFactPct(), pubStats.getPubOpsPct()};
    }
    
    @Override
    String[] getDescriptions() {
        return new String[] {"facts", "ops"};
    }

    @Override
    protected void printAverageResults(Map<String, int[][]> results) {
        System.out.println("averageResultsFacts: ");
        for (Entry<String, int[][]> entry : results.entrySet()) {
            System.out.println(entry.getKey() + " " + Arrays.toString(entry.getValue()[0]) );
        }
        System.out.println("averageResultsOps: ");
        for (Entry<String, int[][]> entry : results.entrySet()) {
            System.out.println(entry.getKey() + " " + Arrays.toString(entry.getValue()[1]) );
        }
    }

    public static void main(String[] args) {
        Analyzer analyzer = new PrivatenessAnalyzer();
        analyzer.analyze();
    }

}
