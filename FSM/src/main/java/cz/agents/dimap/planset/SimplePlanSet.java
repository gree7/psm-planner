package cz.agents.dimap.planset;

import java.util.ArrayList;
import java.util.List;

import cz.agents.dimap.psm.operator.Operator;

public class SimplePlanSet implements PlanSet<Operator> {
    
    List<Plan> plans;

    private SimplePlanSet(List<Plan> plans) {
        this.plans = plans;
    }

    
    public SimplePlanSet() {
        this( new ArrayList<Plan>() );
    }

    public void addPlan(List<Operator> plan) {
        plans.add( new Plan(plan) );
    }
        
    @Override
    public List<String> getRandomPlan() {
        List<Operator> plan = plans.get(0).plan;
        List<String> newPlan = new ArrayList<>(plan.size());
        for (Operator op : plan) {
            newPlan.add(op.getName());
        }
        return newPlan;
    }

    @Override
    public SimplePlanSet intersectWith(PlanSet<Operator> otherPlanSet) {
        if (!(otherPlanSet instanceof SimplePlanSet)) {
           throw new IllegalArgumentException("Cannot intersect with: " + otherPlanSet.getClass().getName()); 
        }
        List<Plan> intersectedPlans = new ArrayList<>(plans);
        intersectedPlans.retainAll(((SimplePlanSet) otherPlanSet).plans);
        return new SimplePlanSet( intersectedPlans );
    }

    @Override
    public boolean isEmpty() {
        return plans.isEmpty();
    }

    @Override
    public void imgOutput(String fileName) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public void clear() {
        plans.clear();
    }

    static class Plan {
        private List<Operator> plan;

        public Plan(List<Operator> plan) {
            this.plan = plan;
        }

        @Override
        public int hashCode() {
            return plan.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            return plan.toString().equals(obj.toString());
        }

        @Override
        public String toString() {
            String retStr = null;
            for (Operator op : plan) {
                if (retStr == null) {
                    retStr = "["; 
                } else {
                    retStr += ", ";
                }
                retStr += op.getName();
            }
            return retStr + "]";
        }
    }
    
    @Override
    public String toString() {
        return plans.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((plans == null) ? 0 : plans.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SimplePlanSet other = (SimplePlanSet) obj;
        if (plans == null) {
            if (other.plans != null)
                return false;
        } else if (!plans.equals(other.plans))
            return false;
        return true;
    }


    @Override
    public int size() {
        int size = 0;
        for (Plan plan : plans) {
            size += plan.plan.size();
        }
        return size;
    }
}
