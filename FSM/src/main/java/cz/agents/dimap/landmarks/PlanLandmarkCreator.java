package cz.agents.dimap.landmarks;

import java.util.ArrayList;
import java.util.List;

import cz.agents.dimap.tools.pddl.PddlAction;

public class PlanLandmarkCreator {
    
    private PlanLandmarkCreator() {}

    public static List<Landmark> planToRecommendedLandmarks(List<? extends PddlAction> plan, String agentName, int agentIndex) {
        List<Landmark> landmarks = new ArrayList<>(plan.size());
        long markId = -1;
        for (PddlAction action : plan) {
            landmarks.add(new RecommendedLandmark(agentName, agentIndex, action, markId, markId+1, true));
            markId++;
        }
        return landmarks;
    }
}
