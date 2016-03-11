package cz.agents.dimap.planset;

import java.io.Serializable;
import java.util.List;

public interface PlanSet<O> extends Serializable {

    List<String> getRandomPlan();

    PlanSet<O> intersectWith(PlanSet<O> otherPlanSet);

    boolean isEmpty();

    void imgOutput(String fileName);

    void addPlan(List<O> plan);
    
    void clear();
    
    int size();
}
