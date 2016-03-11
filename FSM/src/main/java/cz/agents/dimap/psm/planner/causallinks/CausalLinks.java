package cz.agents.dimap.psm.planner.causallinks;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.agents.dimap.fsm.FiniteStateMachine;
import cz.agents.dimap.fsm.LabelFactory;
import cz.agents.dimap.psm.operator.Operator;

public class CausalLinks extends FiniteStateMachine<CausalState, String> {
    
    private final static CausalState initState = new CausalState(-1); 

    private int curId = 0;

    public CausalLinks() {
        super(initState);
    }

    public CausalLinks(CausalLinks other) {
        super(other);
        curId = other.curId;
    }

    public void addPlan(List<? extends Operator> plan) {

        Collection<CausalLink> causalLinks = PlanCausalLinks.getCausalLinksForPlan(plan);
        
        Map<Integer, CausalState> actionStateById = new HashMap<>();
        actionStateById.put(-1, initState);
        
        Map<CausalState, Set<CausalState>> coveredStatesMap = new HashMap<>();
        coveredStatesMap.put(initState, new HashSet<CausalState>());
        for (CausalLink causalLink : causalLinks) {
            for (Integer conditionId : causalLink.conditionId) {
                CausalState requiredAction = actionStateById.get(conditionId);
                String action = causalLink.action;
                
                CausalState newState = actionStateById.get(causalLink.effectId);
                if (newState == null) {
                    Collection<CausalState> toStates = getToStates(requiredAction, action);
                    if (toStates.size() == 1) {
                        newState = toStates.iterator().next();
                    } else if (toStates.isEmpty()) {
                        newState = null;
                    } else {
                        throw new UnsupportedOperationException("Not implemented yet!!!");
                    }
                }
                
                if (newState == null) {
                    newState = new CausalState(curId++);
                }
                
                Set<CausalState> coveredStates = coveredStatesMap.get(newState);

                if (coveredStates == null) {
                    coveredStates = new HashSet<CausalState>();
                    coveredStatesMap.put(newState, coveredStates);
                }
                
                if (!coveredStates.contains(requiredAction)) {
                    addTransition(requiredAction, action, newState);
                    coveredStates.addAll(coveredStatesMap.get(requiredAction));
                    coveredStates.add(requiredAction);
                }

                CausalState state = newState;
                actionStateById.put(causalLink.effectId, state);
            }
        }
    }
    
    @Override
    public String toString() {
        return "CausalLinks [" + super.toString() + "]";
    }
    
    public static LabelFactory<CausalState, String> getLabelFactory() {
        return new LabelFactory<CausalState, String>() {

            @Override
            public String createStateLabel(CausalState state) {
                return Integer.toString( state.id );
            }

            @Override
            public String createOperatorLabel(String operator) {
                return operator;
            }
            
        };
    }
}
