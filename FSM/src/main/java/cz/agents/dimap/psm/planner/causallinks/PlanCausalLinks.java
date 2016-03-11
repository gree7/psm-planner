package cz.agents.dimap.psm.planner.causallinks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.dimap.psm.operator.Operator;

public class PlanCausalLinks {

    Map<Integer, List<Integer>> dependsOn = new LinkedHashMap<>();

    Map<Integer, Operator> actions = new HashMap<>();


    private PlanCausalLinks(List<? extends Operator> plan) {
        addPlan(plan);
    }
    
    static public Collection<CausalLink> getCausalLinksForPlan(List<? extends Operator> plan) {
        PlanCausalLinks planCausalLinks = new PlanCausalLinks(plan);
        return planCausalLinks.getLandmarksForPlan();
    }
    
    private Collection<CausalLink> getLandmarksForPlan() {
        Collection<CausalLink> landmarks = new ArrayList<>();
        
        for (Entry<Integer, List<Integer>> dependency : dependsOn.entrySet()) {
            Integer action = dependency.getKey();
            Operator operator = actions.get(action);
            if (operator.isPublic()) {
                Collection<Integer> depAction = dependency.getValue();
                CausalLink landmark = new CausalLink(operator.getName(), depAction, action);
                landmarks.add(landmark);
            }
        }
        return landmarks;
    }

    private void addPlan(List<? extends Operator> plan) {
        List<Operator> prevOperators = new ArrayList<>();
        Operator[] arrayPlan = plan.toArray(new Operator[0]);
        
        for (int operatorIndex = 0; operatorIndex < arrayPlan.length; operatorIndex++) {
            Operator operator = arrayPlan[operatorIndex];

            actions.put(operatorIndex, operator);

            List<Integer> reqOperators = getRequiredOperators(operator, prevOperators);
            
            dependsOn.put(operatorIndex, reqOperators);
                
            prevOperators.add(operator);
        }
    }

    private List<Integer> getRequiredOperators(Operator operator, List<Operator> prevOperators) {
        List<Integer> reqOperators = new ArrayList<>();

        for (Object precondition : operator.getPreconditions()) {
            int requiredOperatorIndex = findRequiredOperator(precondition, prevOperators);
            if (requiredOperatorIndex != -1) {
                // found some required operator and it hasn't been processed yet
                Operator requiredOperator = prevOperators.get(requiredOperatorIndex);
                if ( requiredOperator.isPublic() ) {
                    reqOperators.add(requiredOperatorIndex);
                } else {
                    reqOperators.addAll(dependsOn.get(requiredOperatorIndex));
                }
            }
        }
        return reqOperators;
    }

    private int findRequiredOperator(Object precondition, List<Operator> prevOperators) {
        ListIterator<Operator> iterator = prevOperators.listIterator(prevOperators.size());
        while (iterator.hasPrevious()) {
            Operator op = iterator.previous();
            Set<? extends Object> effects = op.getEffects();
            if (effects.contains(precondition)) {
                return iterator.previousIndex()+1;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return "PlanCausalLinks [dependsOn=" + dependsOn + "]";
    }
}
