package experiments.psm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cz.agents.dimap.Settings;
import cz.agents.dimap.psm.planner.MultiAgentProblem;

public abstract class Analyzer {

    final List<Map<String, MultiAgentProblem>> maProblems;

    public Analyzer() {
        this(true, true, true, true);
    }
    
    public Analyzer(boolean fmap, boolean fmapPrivGoal, boolean maStrips, boolean maStripsPrivGoal) {
        maProblems = new ArrayList<>();
        Settings.USE_MASTRIPS = false;
        Settings.ALLOW_PRIVATE_GOALS = false;
        if (fmap) {
            maProblems.add( PsmFmapBenchmarks.createBenchmarkTasks() );
        }
        Settings.ALLOW_PRIVATE_GOALS = true;
        if (fmapPrivGoal) {
            maProblems.add( PsmFmapBenchmarks.createBenchmarkTasks() );
        }
        
        Settings.USE_MASTRIPS = true;
        Settings.ALLOW_PRIVATE_GOALS = false;
        if (maStrips) {
            maProblems.add( PsmFmapBenchmarks.createBenchmarkTasks() );
        }
        Settings.ALLOW_PRIVATE_GOALS = true;
        if (maStripsPrivGoal) {
            maProblems.add( PsmFmapBenchmarks.createBenchmarkTasks() );
        }
    }
    
    public void analyze() {
    
        Map<String, int[][]> results = new LinkedHashMap<>();
    
        for (String problemName : maProblems.get(0).keySet()) {
            
            if (maProblems.get(0).get(problemName).getAgents().size() == 1) {
                // publicness has no meaning for problem with one agent
                continue;
            }
            
            List<int[]> statistics = new ArrayList<>();
            int length = 0;
            for (Map<String, MultiAgentProblem> maProblem : maProblems) {
                int[] stats = getStats( maProblem.get(problemName));
                statistics.add( stats );
                length = stats.length;
            }
    
            int[][] stats = new int[length][maProblems.size()];
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < maProblems.size(); j++) {
                    stats[i][j] = statistics.get(j)[i];
                }
            }
 
            log(problemName, stats);

            results.put(problemName, stats);
        }
    
        printAverageResults(averageResults(results));
    }

    protected void printAverageResults(Map<String, int[][]> results) {

        System.out.println();

        System.out.printf("%15s   ", "Domain");
        
        String[] descriptions = getDescriptions();
        for (int i = 0; i < descriptions.length; i++) {
            System.out.printf("| %12s   ", descriptions[i]);
        }
        System.out.println();

        for (Entry<String, int[][]> entry : results.entrySet()) {
            System.out.printf("%15s   ", entry.getKey());
            for (int i = 0; i < descriptions.length; i++) {
                System.out.printf("| %12s   ", Arrays.toString(entry.getValue()[i]));
            }
            System.out.println();
        }
    }

    void log(String problemName, int[][] stats) {
        String[] descriptions = getDescriptions();
        System.out.print(problemName + ":");
        for (int i=0; i<descriptions.length; ++i) {
            System.out.print(" " + descriptions[i] + ": " + Arrays.toString(stats[i]));
        }
        System.out.println();
    }

    private static Map<String, int[][]> averageResults(Map<String, int[][]> results) {
        Map<String, Integer> domainCount = new LinkedHashMap<>();
        Map<String, int[][]> averageResults = new LinkedHashMap<>();
    
        for (Entry<String, int[][]> result : results.entrySet()) {
            String domainName = result.getKey().substring(0, result.getKey().indexOf('('));
    
    
            addToMap(averageResults, domainName, result.getValue());
            addToMap(domainCount, domainName, 1);            
        }
        
        for (Entry<String, int[][]> entry : averageResults.entrySet()) {
            int count = domainCount.get(entry.getKey());
            int[][] result = entry.getValue();
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result[i].length; j++) {
                    result[i][j] /= count;
                }
            }
        }
    
        return averageResults;
    }

    private static void addToMap(Map<String, int[][]> map, String key, int [][] increment) {
        int [][] curValue;
        if (map.containsKey(key)) {
            curValue = map.get(key);
        } else {
            curValue = new int[increment.length][increment[0].length];
        }
        for (int i = 0; i < curValue.length; i++) {
            for (int j = 0; j < curValue[i].length; j++) {
                curValue[i][j] += increment[i][j];
            }
        }
        map.put(key, curValue);
    }

    private static void addToMap(Map<String, Integer> map, String key, int increment) {
        int curValue;
        if (map.containsKey(key)) {
            curValue = map.get(key);
        } else {
            curValue = 0;
        }
        map.put(key, curValue+increment);
    }

    public static int getImprovement(int oldValue, int newValue) {
        if (oldValue == 0) {
            return 0;
        } else {
            return 100 - (newValue*100) / oldValue;
        }
    }

    abstract int[] getStats(MultiAgentProblem maProblem);

    abstract String[] getDescriptions();
}