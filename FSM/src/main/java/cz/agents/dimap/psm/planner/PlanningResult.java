package cz.agents.dimap.psm.planner;

import java.util.List;

public class PlanningResult {
    public long parallelTimeMs;
    public int iterationCount; 
    public List<String> plan;
    
    public List<Integer> sentActions;
    public List<Long> verificationTime;
    private List<Long> planningTime;
    private int numberOfAgents;

    public PlanningResult(long parallelTimeMs, int iterationCount, List<String> plan, int numberOfAgents, List<Integer> numOfSentActions, List<Long> verificationTime, List<Long> planningTime) {
        this.parallelTimeMs = parallelTimeMs;
        this.iterationCount = iterationCount;
        this.plan = plan;
        this.numberOfAgents = numberOfAgents;
        this.sentActions = numOfSentActions;
        this.verificationTime = verificationTime;
        this.planningTime = planningTime;
    }

    @Override
    public String toString() {
        return plan.toString();
    }
    
    public String getDetails() {
        return "agents: " + numberOfAgents + " communication: " + sentActions + " verification[ms]: " + verificationTime  + " planning[ms]: " + planningTime;
    }

}
