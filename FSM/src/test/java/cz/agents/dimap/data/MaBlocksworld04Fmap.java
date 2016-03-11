package cz.agents.dimap.data;


import java.util.HashMap;
import java.util.Map;

import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlProblem;
import experiments.psm.fmap.FmapMultiAgentProblem;


public class MaBlocksworld04Fmap extends PddlTestProblem {
    public static MultiAgentProblem getProblem() {
        Map<String, PddlProblem> problems = new HashMap<String, PddlProblem>();
        problems.put("r0", createProblem(domain, problemR0, false));
        problems.put("r1", createProblem(domain, problemR1, false));
        problems.put("r2", createProblem(domain, problemR2, false));
        problems.put("r3", createProblem(domain, problemR3, false));
        
        return new FmapMultiAgentProblem(problems);
    }

    private static String domain = "(define (domain ma-blocksworld)\n" + 
            "(:requirements :typing :equality :fluents)\n" + 
            "(:types\n" + 
            "    robot - agent\n" + 
            "    block - object)\n" + 
            "(:constants\n" + 
            "  nob - block)\n" + 
            "(:predicates\n" + 
            "  (myAgent ?x - robot)\n" + 
            "  (clear ?x - block)\n" + 
            "  (ontable ?x - block))\n" + 
            "(:functions\n" + 
            "  (on ?x - block) - block\n" + 
            "  (holding ?x - robot) - block)\n" + 
            "(:action pickup\n" + 
            ":parameters (?ob - block ?r - robot)\n" + 
            ":precondition (and (myAgent ?r) (= (on ?ob) nob) (ontable ?ob) (clear ?ob)(= (holding ?r) nob))\n" + 
            ":effect (and (assign (holding ?r) ?ob) (not (ontable ?ob))(not (clear ?ob))))\n" + 
            "(:action putdown\n" + 
            ":parameters (?ob - block ?r - robot)\n" + 
            ":precondition (and (myAgent ?r) (= (holding ?r) ?ob))\n" + 
            ":effect (and (assign (holding ?r) nob)(ontable ?ob)(clear ?ob)))\n" + 
            "(:action stack\n" + 
            ":parameters (?ob - block ?uob - block ?r - robot)\n" + 
            ":precondition (and (myAgent ?r) (clear ?uob) (= (holding ?r) ?ob))\n" + 
            ":effect (and (assign (holding ?r) nob) (clear ?ob) (assign (on ?ob) ?uob) (not (clear ?uob))))\n" + 
            "(:action unstack\n" + 
            ":parameters (?ob - block ?uob - block ?r - robot)\n" + 
            ":precondition (and (myAgent ?r) (= (on ?ob) ?uob) (clear ?ob) (= (holding ?r) nob))\n" + 
            ":effect (and (assign (holding ?r) ?ob) (clear ?uob) (assign (on ?ob) nob) (not (clear ?ob)))))\n" + 
            "";

    private static String problemR0 = "(define (problem BLOCKS-4-0)\n" + 
            "(:domain ma-blocksworld)\n" + 
            "(:objects\n" + 
            " r0 r1 r2 r3 - robot\n" + 
            " d b a c - block\n" + 
            ")\n" + 
            "(:shared-data\n" + 
            "  ((on ?b - block) - block)\n" + 
            "  (ontable ?b - block)\n" + 
            "  (clear ?b - block)\n" + 
            "  ((holding ?r - robot) - block) - \n" + 
            "(either r1 r2 r3)\n" + 
            ")\n" + 
            "(:init\n" + 
            " (myAgent r0)\n" + 
            " (= (holding r0) nob)\n" + 
            " (= (holding r1) nob)\n" + 
            " (= (holding r2) nob)\n" + 
            " (= (holding r3) nob)\n" + 
            " (clear d)\n" + 
            " (ontable d)\n" + 
            " (= (on d) nob)\n" + 
            " (clear b)\n" + 
            " (ontable b)\n" + 
            " (= (on b) nob)\n" + 
            " (clear a)\n" + 
            " (ontable a)\n" + 
            " (= (on a) nob)\n" + 
            " (clear c)\n" + 
            " (ontable c)\n" + 
            " (= (on c) nob)\n" + 
            ")\n" + 
            "(:global-goal (and\n" + 
            " (= (on d) c)\n" + 
            " (= (on c) b)\n" + 
            " (= (on b) a)\n" + 
            ")))\n" + 
            "";

    private static String problemR1 = "(define (problem BLOCKS-4-0)\n" + 
            "(:domain ma-blocksworld)\n" + 
            "(:objects\n" + 
            " r0 r1 r2 r3 - robot\n" + 
            " d b a c - block\n" + 
            ")\n" + 
            "(:shared-data\n" + 
            "  ((on ?b - block) - block)\n" + 
            "  (ontable ?b - block)\n" + 
            "  (clear ?b - block)\n" + 
            "  ((holding ?r - robot) - block) - \n" + 
            "(either r0 r2 r3)\n" + 
            ")\n" + 
            "(:init\n" + 
            " (myAgent r1)\n" + 
            " (= (holding r0) nob)\n" + 
            " (= (holding r1) nob)\n" + 
            " (= (holding r2) nob)\n" + 
            " (= (holding r3) nob)\n" + 
            " (clear d)\n" + 
            " (ontable d)\n" + 
            " (= (on d) nob)\n" + 
            " (clear b)\n" + 
            " (ontable b)\n" + 
            " (= (on b) nob)\n" + 
            " (clear a)\n" + 
            " (ontable a)\n" + 
            " (= (on a) nob)\n" + 
            " (clear c)\n" + 
            " (ontable c)\n" + 
            " (= (on c) nob)\n" + 
            ")\n" + 
            "(:global-goal (and\n" + 
            " (= (on d) c)\n" + 
            " (= (on c) b)\n" + 
            " (= (on b) a)\n" + 
            ")))\n" + 
            "";

    private static String problemR2 = "(define (problem BLOCKS-4-0)\n" + 
            "(:domain ma-blocksworld)\n" + 
            "(:objects\n" + 
            " r0 r1 r2 r3 - robot\n" + 
            " d b a c - block\n" + 
            ")\n" + 
            "(:shared-data\n" + 
            "  ((on ?b - block) - block)\n" + 
            "  (ontable ?b - block)\n" + 
            "  (clear ?b - block)\n" + 
            "  ((holding ?r - robot) - block) - \n" + 
            "(either r0 r1 r3)\n" + 
            ")\n" + 
            "(:init\n" + 
            " (myAgent r2)\n" + 
            " (= (holding r0) nob)\n" + 
            " (= (holding r1) nob)\n" + 
            " (= (holding r2) nob)\n" + 
            " (= (holding r3) nob)\n" + 
            " (clear d)\n" + 
            " (ontable d)\n" + 
            " (= (on d) nob)\n" + 
            " (clear b)\n" + 
            " (ontable b)\n" + 
            " (= (on b) nob)\n" + 
            " (clear a)\n" + 
            " (ontable a)\n" + 
            " (= (on a) nob)\n" + 
            " (clear c)\n" + 
            " (ontable c)\n" + 
            " (= (on c) nob)\n" + 
            ")\n" + 
            "(:global-goal (and\n" + 
            " (= (on d) c)\n" + 
            " (= (on c) b)\n" + 
            " (= (on b) a)\n" + 
            ")))\n" + 
            "";

    private static String problemR3 = "(define (problem BLOCKS-4-0)\n" + 
            "(:domain ma-blocksworld)\n" + 
            "(:objects\n" + 
            " r0 r1 r2 r3 - robot\n" + 
            " d b a c - block\n" + 
            ")\n" + 
            "(:shared-data\n" + 
            "  ((on ?b - block) - block)\n" + 
            "  (ontable ?b - block)\n" + 
            "  (clear ?b - block)\n" + 
            "  ((holding ?r - robot) - block) - \n" + 
            "(either r0 r1 r2)\n" + 
            ")\n" + 
            "(:init\n" + 
            " (myAgent r3)\n" + 
            " (= (holding r0) nob)\n" + 
            " (= (holding r1) nob)\n" + 
            " (= (holding r2) nob)\n" + 
            " (= (holding r3) nob)\n" + 
            " (clear d)\n" + 
            " (ontable d)\n" + 
            " (= (on d) nob)\n" + 
            " (clear b)\n" + 
            " (ontable b)\n" + 
            " (= (on b) nob)\n" + 
            " (clear a)\n" + 
            " (ontable a)\n" + 
            " (= (on a) nob)\n" + 
            " (clear c)\n" + 
            " (ontable c)\n" + 
            " (= (on c) nob)\n" + 
            ")\n" + 
            "(:global-goal (and\n" + 
            " (= (on d) c)\n" + 
            " (= (on c) b)\n" + 
            " (= (on b) a)\n" + 
            ")))\n" + 
            "";
}

