package cz.agents.dimap.tools;

import java.util.HashMap;
import java.util.Map;

import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;

public class RelaxedPGWithHiddenDependencies extends RelaxedPlanningGraph {

    static enum RiskyState { RISKY, POTENTIAL, OK };
    
    Map<PddlTerm, RiskyState> riskyStates = new HashMap<>();
    
    protected RelaxedPGWithHiddenDependencies(PddlProblem problem) {
        super(problem);
        for (PddlTerm fact : problem.init.positives) {
            riskyStates.put(fact, RiskyState.OK);
        }
    }

    public static Stats extractReachableActionsStats(PddlProblem problem) {
        RelaxedPGWithHiddenDependencies graph = new RelaxedPGWithHiddenDependencies(problem);
        graph.applyAllActionsRepeatedly();
        return new Stats(graph);
    }

    @Override
    protected void applyAction(PddlAction action) {
        
        RiskyState riskyState = getRiskyStateForAction(action, riskyStates);
        for (PddlTerm effect : action.effect.positives) {
            if (problem.isPublicPredicate(effect)) {
                if (!riskyStates.containsKey(effect)) {
                    riskyStates.put(effect, RiskyState.OK);
                }
            } else {
                if (action.isPublic()) {
                    if (!riskyStates.containsKey(effect)) {
                        riskyStates.put(effect, RiskyState.RISKY);
                    } else if (riskyStates.get(effect) != RiskyState.RISKY) {
                        riskyStates.put(effect, RiskyState.POTENTIAL);
                    }
                } else {
                    if (!riskyStates.containsKey(effect)) {
                        riskyStates.put(effect, riskyState);
                    } else {
                        switch (riskyState) {
                        case RISKY:
                            if (riskyStates.get(effect) == RiskyState.OK) {
                                riskyStates.put(effect, RiskyState.POTENTIAL);
                            }
                            break;
                        case POTENTIAL:
                            riskyStates.put(effect, RiskyState.POTENTIAL);
                            break;
                        case OK:
                            if (riskyStates.get(effect) == RiskyState.RISKY) {
                                riskyStates.put(effect, RiskyState.POTENTIAL);
                            }
                            break;
                        }
                    }
                }
            }
        }      

        super.applyAction(action);
    }
    
    

    static private RiskyState getRiskyStateForAction(PddlAction action, Map<PddlTerm, RiskyState> riskyStates) {
        RiskyState riskyState = RiskyState.OK;
        
        forPreconditions:
        for (PddlTerm fact : action.precondition.positives) {
            switch (riskyStates.get(fact)) {
            case RISKY:
                riskyState = RiskyState.RISKY;
                break forPreconditions;
            case POTENTIAL:
                riskyState = RiskyState.POTENTIAL;
                break;
            default:
                break;
            }
        }
        return riskyState;
    }

    public static class Stats {
        public int numOfPublicActions = 0;
        public int numOfPotentiallyRiskyPublicActions = 0;
        public int numOfRiskyPublicActions = 0;

        Stats(RelaxedPGWithHiddenDependencies graph) {
            for (PddlAction action : graph.appliedActions) {
                if (action.isPublic() && !action.name.startsWith("goalaction_")) {
                    numOfPublicActions++;
                    RiskyState riskyState = getRiskyStateForAction(action, graph.riskyStates);
                    switch (riskyState) {
                    case RISKY:
                        numOfRiskyPublicActions++;
                        break;
                    case POTENTIAL:
                        numOfPotentiallyRiskyPublicActions++;
                        break;
                    case OK:
                        break;
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "Stats [numOfPublicActions=" + numOfPublicActions
                    + ", numOfPotentiallyRiskyPublicActions="
                    + numOfPotentiallyRiskyPublicActions
                    + ", numOfRiskyPublicActions=" + numOfRiskyPublicActions
                    + "]";
        }
        
        
    }
}
