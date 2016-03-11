package experiments.codmap.distributed.agent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import cz.agents.dimap.tools.GroundedActionsExtractor;
import cz.agents.dimap.tools.Pair;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;

/**
 * @author honza
 *
 */
public class PlanningGraph {
    private Set<PddlTerm> reachableFacts;
    Collection<PddlTerm> initFacts;
    private Set<PddlAction> appliedActions = new HashSet<>();
    private PddlProblem problem;
    private String agentName;

    private List<FactLayer> factLayers = new ArrayList<>();
    private Set<PddlName> constants;

    public PlanningGraph(Collection<PddlTerm> initFacts, String agentName, PddlProblem problem, Set<PddlName> constants) {
        this.constants = constants;
        this.initFacts = new HashSet<PddlTerm>(initFacts);
        this.agentName = agentName;
        this.problem = problem;
        reachableFacts = new HashSet<>(initFacts);
    }

    public void applyAllActionsRepeatedly() {
        while (true) {
            Set<PddlAction> applicableActions = GroundedActionsExtractor.generateReachableActions(agentName, problem, reachableFacts, false, constants);
            applicableActions.removeAll(appliedActions);

            appliedActions.addAll(applicableActions);
            
            FactLayer newFactLayer = new FactLayer();
            for (PddlAction action : applicableActions) {
                for (PddlTerm fact : action.effect.positives) {
                    if (!reachableFacts.contains(fact)) {
                        newFactLayer.addApplicableAction(fact, action);
                    }
                }
            }

            if (newFactLayer.isEmpty()) {
                break;
            } else {
                reachableFacts.addAll(newFactLayer.getFacts());
                factLayers.add(newFactLayer);
            }
        }
    }
    
    public Pair<Set<PddlTerm>, List<PddlAction>> getInvertedPlan(Set<PddlTerm> goals, boolean isLazy) {
        goals = new HashSet<>(goals);
        goals.removeAll(initFacts);

        List<PddlAction> plan = new ArrayList<>();
        ListIterator<FactLayer> factLayerIt = factLayers.listIterator(factLayers.size());

        Set<PddlTerm> newGoals = new HashSet<>(goals);
        Set<PddlTerm> coveredGoals = new HashSet<>();
        while (factLayerIt.hasPrevious()) {
            FactLayer factLayer = factLayerIt.previous();

            boolean hasChanged = true;
            while (hasChanged) {
                hasChanged = false;
                
                PddlAction action = factLayer.getBestAction(newGoals);

                if (action != null) {
                    Set<PddlTerm> newEffects = new HashSet<>(action.effect.positives);
                    newEffects.removeAll(coveredGoals);
        
                    if (!newEffects.isEmpty()) {
                        coveredGoals.addAll(action.effect.positives);
                        plan.add(action);
    
                        newGoals.addAll(action.precondition.positives);
                        newGoals.removeAll(coveredGoals);
                        newGoals.removeAll(initFacts);
                        hasChanged = true;
                    }

                    if (isLazy) {
                        newGoals.removeAll(goals);
                        isLazy = false;
                        break;
                    }
                }
            }
        }
        newGoals.addAll(goals);
        newGoals.removeAll(coveredGoals);
        return new Pair<Set<PddlTerm>, List<PddlAction>>(newGoals, plan);
    }

    public Set<PddlTerm> getReachableFacts() {
        return reachableFacts;
    }

    public Set<PddlAction> getAppliedActions() {
        return appliedActions;
    }
}
