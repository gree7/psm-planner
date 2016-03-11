package cz.agents.dimap.psm.planner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.agents.dimap.Settings;
import cz.agents.dimap.causal.InternalDependencies;
import cz.agents.dimap.causal.InternalDependenciesAndOr;
import cz.agents.dimap.causal.InternalDependenciesGraph;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;

public class ExternalActions implements Serializable {
    private static final long serialVersionUID = -11086931945451789L;

    private Set<PddlAction> publicActions = new HashSet<>();

    private final List<PddlTerm> inMerges = new ArrayList<>();
    private final List<PddlTerm> newInitFacts = new ArrayList<>();
    
    boolean isPubliclySolvable;

    private final String agentName;
    
    public ExternalActions(String agentName, PddlProblem problem) {
        this.agentName = agentName;
        if (Settings.VERBOSE) System.out.println("original actions: " + problem.domain.actions.size());
        isPubliclySolvable = createReplacementActions(problem);
        if (Settings.VERBOSE) System.out.println("actions after risky actions replacements: " + problem.domain.actions.size());
        for (PddlAction action : problem.domain.actions) {
            PddlAction projectedAction = PddlAction.createExternalAction(action, problem, !isPubliclySolvable);
            if (projectedAction != null) {
                publicActions.add(projectedAction);
            }
        }
    }

    private boolean createReplacementActions(PddlProblem problem) {
        if (Settings.REPLACE_INTERNALLY_DEPENDENT_ACTIONS) {
            try {
                
                InternalDependenciesGraph inDeps;
                if (Settings.USE_INTERNAL_DEPENDENCIES_GRAPH) {
                    inDeps = new InternalDependenciesGraph(agentName, problem);
                    inDeps.init();
                    inDeps.countInternalDependencies();
                } else {
//                    inDeps = new InternalDependenciesAndOr(problem);
                }
                inMerges.addAll( inDeps.updateProblem(problem) );
                newInitFacts.addAll(inMerges);
                newInitFacts.retainAll(problem.init.positives);
                return inDeps.isFullyReduced();
            } catch (UnsupportedOperationException ex) {
                System.err.println("Publishing internal dependencies failed.");
                return false;
            }
        } else {
            return false;
        }
    }

    public List<PddlAction> getPublicActions() {
        return new ArrayList<PddlAction>(publicActions);
    }
   
    @Override
    public String toString() {
        return publicActions.toString();
    }

    public List<PddlTerm> getInMerges() {
        return inMerges;
    }

    public void removeMergeFacts(PddlProblem problem) {
        problem.domain.sharedPredicates.objects.removeAll(inMerges);
        for (PddlTerm inMerge : inMerges) {
            problem.domain.predicateTypes.remove(inMerge.name);
            problem.domain.sharedPredicates.objects.remove(inMerge);
        }
        problem.init.positives.removeAll(inMerges);

        for (PddlAction action : problem.domain.actions) {
            action.precondition.positives.removeAll(inMerges);
            action.effect.positives.removeAll(inMerges);
            action.effect.negatives.removeAll(inMerges);
        }
    }

    public boolean isPubliclySolvable() {
        return isPubliclySolvable;
    }

    public String getAgentName() {
        return agentName;
    }

    public Collection<? extends PddlTerm> getNewInitFacts() {
        return newInitFacts;
    }
}
