package cz.agents.dimap.planset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.alite.math.CartesianProduct;
import cz.agents.dimap.psm.operator.Operator;
import cz.agents.dimap.psm.planner.causallinks.CausalLink;
import cz.agents.dimap.psm.planner.causallinks.PlanCausalLinks;

public class ConcurrentPlanSet implements PlanSet<Operator> {
    
    List<Plan> plans;

    private ConcurrentPlanSet(List<Plan> plans) {
        this.plans = new ArrayList<Plan>(plans);
    }

    
    public ConcurrentPlanSet() {
        this( new ArrayList<Plan>() );
    }

    @Override
    public void addPlan(List<Operator> plan) {
        Collection<CausalLink> causalLinks = PlanCausalLinks.getCausalLinksForPlan(plan);
        plans.add( new Plan(causalLinks) );
    }
        
    @Override
    public List<String> getRandomPlan() {
        return extractPlan(plans.get(0));
    }

    private List<String> extractPlan(Plan plan) {
        Collection<CausalLink> causalLinks = new LinkedList<>(plan.causalLinks);
        List<String> newPlan = new ArrayList<>(causalLinks.size());
        while (!causalLinks.isEmpty()) {
            CausalLink rootCausalLink = getRootCausalLink(causalLinks);
            newPlan.add(rootCausalLink.action);
            causalLinks.remove(rootCausalLink);
        }
        return newPlan;
    }

    private CausalLink getRootCausalLink(Collection<CausalLink> causalLinks) {
        for (CausalLink causalLink : causalLinks) {
            if (isRootCausalLink(causalLink, causalLinks)) {
                return causalLink;
            }
        }
        throw new IllegalArgumentException("No root causal link -> cyclic graph!");
    }


    private boolean isRootCausalLink(CausalLink causalLink, Collection<CausalLink> causalLinks) {
        for (CausalLink otherCausalLink : causalLinks) {
            if (causalLink.conditionId.contains(otherCausalLink.effectId)) {
                return false;
            }
        }
        return true;
    }


    @Override
    public ConcurrentPlanSet intersectWith(PlanSet<Operator> otherPlanSet) {
        if (!(otherPlanSet instanceof ConcurrentPlanSet)) {
           throw new IllegalArgumentException("Cannot intersect with: " + otherPlanSet.getClass().getName()); 
        }
        List<Plan> intersectedPlans = new ArrayList<>();
        for (Plan plan : ((ConcurrentPlanSet) otherPlanSet).plans) {
            intersectedPlans.addAll( intersectWith(plan) );
        }
        return new ConcurrentPlanSet( intersectedPlans );
    }

    private Collection<? extends Plan> intersectWith(Plan otherPlan) {
        List<Plan> intersectedPlans = new ArrayList<>();
        for (Plan plan : plans) {
            Plan intersectedPlan = intersectPlans(plan, otherPlan);
            if (intersectedPlan != null) {
                intersectedPlans.add( intersectedPlan );
            }
        }
        return intersectedPlans;
    }
    
    static Plan intersectPlans(Plan plan1, Plan plan2) {
        if (plan1.causalLinks.size() != plan2.causalLinks.size()) {
            return null;
        }
        
        Collection<CausalLink> newCausalLinks = new ArrayList<>(plan1.causalLinks);
        
        if (containsCycle(newCausalLinks)) {
            throw new IllegalArgumentException("Causal links contain cycle(s)!");
        }
        
        Map<String, List<Integer>> actionNumbers = extractActionNumbers(plan1.causalLinks);
        
        return renumberActions(newCausalLinks, plan2.causalLinks, actionNumbers);
    }

    private static Map<String, List<Integer>> extractActionNumbers(Collection<CausalLink> causalLinks) {
        Map<String, List<Integer>> actionNumbers = new HashMap<>();
        for (CausalLink causalLink : causalLinks) {
            if (!actionNumbers.containsKey(causalLink.action)) {
                actionNumbers.put(causalLink.action, new ArrayList<Integer>());
            }
            actionNumbers.get(causalLink.action).add(causalLink.effectId);
        }
        return actionNumbers;
    }

    /**
     * This method tries all possible renumbering of actions and return a plan from first possible match
     * 
     * @param newCausalLinks
     * @param causalLinks
     * @param actionNumbers
     * @return
     */
    private static Plan renumberActions(Collection<CausalLink> newCausalLinks, Collection<CausalLink> causalLinks, Map<String, List<Integer>> actionNumbers) {
       
        List<Set<List<Integer>>> definitionsOfDomains = new ArrayList<>();
        List<String> actionNumbersNames = new ArrayList<>();
        for (Entry<String, List<Integer>> entry : actionNumbers.entrySet()) {
            definitionsOfDomains.add(generatePerm(entry.getValue()));
            actionNumbersNames.add(entry.getKey());
        }

        CartesianProduct<List<Integer>> cartesianProduct= new CartesianProduct<>(definitionsOfDomains);

        nextAssignment:
        for ( int i = 0; i < cartesianProduct.size(); i++ ) {

            Map<String, List<Integer>> newActionNumbers = new HashMap<>();

            List<List<Integer>> product = cartesianProduct.element(i);
            for (int j = 0; j < actionNumbersNames.size(); j++) {
                String actionName = actionNumbersNames.get(j);
                newActionNumbers.put(actionName, product.get(j));
            }

        
            Map<Integer, Integer> idMapping = new HashMap<>();
    
            idMapping.put(-1, -1);
    
            for (CausalLink causalLink : causalLinks) {
                List<Integer> curActionNumbers = newActionNumbers.get(causalLink.action);
                if (curActionNumbers == null || curActionNumbers.isEmpty()) {
                    continue nextAssignment;
                }
                Integer newActionNumber = curActionNumbers.remove(0);
                idMapping.put(causalLink.effectId, newActionNumber);
            }
    
            Collection<CausalLink> renumberedCausalLinks = new ArrayList<>(causalLinks.size());
            for (CausalLink causalLink : causalLinks) {
                Collection<Integer> newConditionId = new ArrayList<>(causalLink.conditionId.size());
                for (Integer id : causalLink.conditionId) {
                    newConditionId.add(idMapping.get(id));
                }
                Integer newEffectId = idMapping.get(causalLink.effectId);
                renumberedCausalLinks.add(new CausalLink(causalLink.action, newConditionId, newEffectId));
            }
    
            return process(newCausalLinks, renumberedCausalLinks);
        }
        return null;
    }

    static <E> Set<List<E>> generatePerm(List<E> original) {
        if (original.size() == 0) { 
            Set<List<E>> result = new HashSet<List<E>>();
            result.add(new ArrayList<E>());
            return result;
        }
        E firstElement = original.remove(0);
        Set<List<E>> returnValue = new HashSet<List<E>>();
        Set<List<E>> permutations = generatePerm(original);
        for (List<E> smallerPermutated : permutations) {
            for (int index=0; index <= smallerPermutated.size(); index++) {
                List<E> temp = new ArrayList<E>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        return returnValue;
    }

    static Plan process(Collection<CausalLink> newCausalLinks, Collection<CausalLink> plan2CausalLinks) {
        for (CausalLink causalLink2 : plan2CausalLinks) {
            CausalLink newCausalLink = null;
            for (CausalLink causalLink : newCausalLinks) {
                if (causalLink.action.equals(causalLink2.action)) {
                    newCausalLink = causalLink;
                    break;
                }
            }

            if (newCausalLink != null) {               
                if (newCausalLink.effectId != causalLink2.effectId) {
                    return null;
                }
                
                Set<Integer> newConditionId = new HashSet<>(newCausalLink.conditionId);
                newConditionId.addAll(causalLink2.conditionId);
                newCausalLinks.remove(newCausalLink);
                newCausalLinks.add(new CausalLink(newCausalLink.action, newConditionId, newCausalLink.effectId));
            }

            if (containsCycle(newCausalLinks)) {
                return null;
            }
        }
        return new Plan(newCausalLinks);
    }

    private static boolean containsCycle(Collection<CausalLink> causalLinks) {
        
        try {
            findCoveredDfs(causalLinks, -1, new ArrayList<Integer>(causalLinks.size()));
        } catch (IllegalStateException e) {
            return true;
        }
        return false;
    }


    private static void findCoveredDfs(Collection<CausalLink> causalLinks, int currentId, List<Integer> closedIds) {
        if (closedIds.contains(currentId)) {
            throw new IllegalStateException("Contains cycle!");
        }
            
        closedIds.add(currentId);
        for (CausalLink causalLink : causalLinks) {
            if (causalLink.conditionId.contains(currentId)) {
                findCoveredDfs(causalLinks, causalLink.effectId, closedIds);
            }
        }
        closedIds.remove((Integer) currentId);
    }

    @Override
    public boolean isEmpty() {
        return plans.isEmpty();
    }

    @Override
    public void clear() {
        plans.clear();
    }

    @Override
    public void imgOutput(String fileName) {
        throw new UnsupportedOperationException("Not implemented yet!");
//        FiniteStateMachineTools.imgOutput(planCausalLinks, fileName, PlanCausalLinks.getLabelFactory());
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
        ConcurrentPlanSet other = (ConcurrentPlanSet) obj;
        if (plans == null) {
            if (other.plans != null)
                return false;
        } else if (!plans.equals(other.plans))
            return false;
        return true;
    }

    static class Plan {
        Collection<CausalLink> causalLinks;

        Plan(Collection<CausalLink> causalLinks) {
            this.causalLinks = causalLinks;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((causalLinks == null) ? 0 : causalLinks.hashCode());
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
            Plan other = (Plan) obj;
            if (causalLinks == null) {
                if (other.causalLinks != null)
                    return false;
            } else if (!causalLinks.equals(other.causalLinks))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Plan [causalLinks=" + causalLinks + "]";
        }
    }

    @Override
    public int size() {
        int size = 0;
        for (Plan plan : plans) {
            size += plan.causalLinks.size();
        }
        return size;
    }
}
