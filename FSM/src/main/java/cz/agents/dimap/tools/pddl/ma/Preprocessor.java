package cz.agents.dimap.tools.pddl.ma;

import java.util.ArrayList;
import java.util.List;

import cz.agents.dimap.tools.GroundedActionsExtractor;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlCondition;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class Preprocessor {

    private final List<PddlAction> allReachableActions;
    private final PddlCondition init;
    private final PddlCondition goal;
    private final PddlDomain groundedDomain;

    public Preprocessor(PddlDomain domain, PddlProblem problem) {
        System.out.println("\n--- INVARIANTS ---");

        System.out.println("\n--- INIT & GOAL ---");
        init = problem.init;
        goal = problem.goal;

        System.out.println("\n--- GLOBAL REACHABILITY & FACTORIZATION ---");
        
        throw new UnsupportedOperationException("not implemented any more");
//        allReachableActions = new ArrayList<>(GroundedActionsExtractor.extractReachableActions(problem));
//        groundedDomain = PddlDomain.createGroundedDomain(domain, allReachableActions);
    }
    
    public PddlDomain getDomain() {
        return groundedDomain;
    }

    public PddlCondition getInit() {
        return init;
    }

    public PddlCondition getGoal() {
        return goal;
    }
}
