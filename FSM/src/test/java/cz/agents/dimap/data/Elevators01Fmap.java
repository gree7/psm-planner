package cz.agents.dimap.data;


import java.util.HashMap;
import java.util.Map;

import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlProblem;
import experiments.psm.fmap.FmapMultiAgentProblem;


public class Elevators01Fmap extends PddlTestProblem {
    public static MultiAgentProblem getProblem() {
        Map<String, PddlProblem> problems = new HashMap<String, PddlProblem>();
        problems.put("fast0", createProblem(domain, problemFast0, false));
        problems.put("slow0_0", createProblem(domain, problemSlow0, false));
        problems.put("slow1_0", createProblem(domain, problemSlow1, false));
        
        return new FmapMultiAgentProblem(problems);
    }

    private static String domain = "(define (domain elevators)\n" + 
            "(:requirements :typing :equality :fluents)\n" + 
            "(:types passenger count - object\n" + 
            "        elevator - agent\n" + 
            "        slow-elevator fast-elevator - elevator)\n" + 
            "(:predicates (myAgent ?e - elevator)\n" + 
            "    (reachable-floor ?lift - elevator ?floor - count)\n" + 
            "    (above ?floor1 - count ?floor2 - count)\n" + 
            "    (can-hold ?lift - elevator ?n - count))\n" + 
            "(:functions\n" + 
            "    (at ?person - passenger) - (either count elevator)\n" + 
            "    (lift-at ?lift - elevator) - count\n" + 
            "    (passengers ?lift - elevator) - count\n" + 
            "    (next ?n - count) - count)\n" + 
            "(:action move-up-slow\n" + 
            "  :parameters (?lift - slow-elevator ?f1 - count ?f2 - count)\n" + 
            "  :precondition (and (myAgent ?lift) (= (lift-at ?lift) ?f1) (above ?f1 ?f2) (reachable-floor ?lift ?f2))\n" + 
            "  :effect (and (assign (lift-at ?lift) ?f2)))\n" + 
            "(:action move-down-slow\n" + 
            "  :parameters (?lift - slow-elevator ?f1 - count ?f2 - count)\n" + 
            "  :precondition (and (myAgent ?lift) (= (lift-at ?lift) ?f1) (above ?f2 ?f1) (reachable-floor ?lift ?f2))\n" + 
            "  :effect (and (assign (lift-at ?lift) ?f2)))\n" + 
            "(:action move-up-fast\n" + 
            "  :parameters (?lift - fast-elevator ?f1 - count ?f2 - count)\n" + 
            "  :precondition (and (myAgent ?lift) (= (lift-at ?lift) ?f1) (above ?f1 ?f2) (reachable-floor ?lift ?f2))\n" + 
            "  :effect (and (assign (lift-at ?lift) ?f2)))\n" + 
            "(:action move-down-fast\n" + 
            "  :parameters (?lift - fast-elevator ?f1 - count ?f2 - count)\n" + 
            "  :precondition (and (myAgent ?lift) (= (lift-at ?lift) ?f1) (above ?f2 ?f1) (reachable-floor ?lift ?f2))\n" + 
            "  :effect (and (assign (lift-at ?lift) ?f2)))\n" + 
            "(:action board\n" + 
            "  :parameters (?p - passenger ?lift - elevator ?f - count ?n1 - count ?n2 - count)\n" + 
            "  :precondition (and (myAgent ?lift) (= (lift-at ?lift) ?f) (= (at ?p) ?f) (= (passengers ?lift) ?n1) (= (next ?n1) ?n2) (can-hold ?lift ?n2))\n" + 
            "  :effect (and (assign (at ?p) ?lift) (assign (passengers ?lift) ?n2)))\n" + 
            "(:action leave\n" + 
            "  :parameters (?p - passenger ?lift - elevator ?f - count ?n1 - count ?n2 - count)\n" + 
            "  :precondition (and (myAgent ?lift) (= (lift-at ?lift) ?f) (= (at ?p) ?lift) (= (passengers ?lift) ?n1) (= (next ?n2) ?n1))\n" + 
            "  :effect (and (assign (at ?p) ?f) (assign (passengers ?lift) ?n2))))\n" + 
            "";

    private static String problemFast0 = "(define (problem elevators-sequencedstrips-p8_3_1)\n" + 
            "(:domain elevators)\n" + 
            "(:objects\n" + 
            " n0 n1 n2 n3 n4 n5 n6 n7 n8 - count\n" + 
            " p0 p1 p2 - passenger\n" + 
            " fast0 - fast-elevator\n" + 
            " slow0-0 slow1-0 - slow-elevator\n" + 
            ")\n" + 
            "(:shared-data\n" + 
            "  ((at ?person - passenger) - (either count elevator)) - (either slow0-0 slow1-0)\n" + 
            ")\n" + 
            "(:init (myAgent fast0)\n" + 
            " (= (next n0) n1)\n" + 
            " (= (next n1) n2)\n" + 
            " (= (next n2) n3)\n" + 
            " (= (next n3) n4)\n" + 
            " (= (next n4) n5)\n" + 
            " (= (next n5) n6)\n" + 
            " (= (next n6) n7)\n" + 
            " (= (next n7) n8)\n" + 
            " (above n0 n1)\n" + 
            " (above n0 n2)\n" + 
            " (above n0 n3)\n" + 
            " (above n0 n4)\n" + 
            " (above n0 n5)\n" + 
            " (above n0 n6)\n" + 
            " (above n0 n7)\n" + 
            " (above n0 n8)\n" + 
            " (above n1 n2)\n" + 
            " (above n1 n3)\n" + 
            " (above n1 n4)\n" + 
            " (above n1 n5)\n" + 
            " (above n1 n6)\n" + 
            " (above n1 n7)\n" + 
            " (above n1 n8)\n" + 
            " (above n2 n3)\n" + 
            " (above n2 n4)\n" + 
            " (above n2 n5)\n" + 
            " (above n2 n6)\n" + 
            " (above n2 n7)\n" + 
            " (above n2 n8)\n" + 
            " (above n3 n4)\n" + 
            " (above n3 n5)\n" + 
            " (above n3 n6)\n" + 
            " (above n3 n7)\n" + 
            " (above n3 n8)\n" + 
            " (above n4 n5)\n" + 
            " (above n4 n6)\n" + 
            " (above n4 n7)\n" + 
            " (above n4 n8)\n" + 
            " (above n5 n6)\n" + 
            " (above n5 n7)\n" + 
            " (above n5 n8)\n" + 
            " (above n6 n7)\n" + 
            " (above n6 n8)\n" + 
            " (above n7 n8)\n" + 
            " (= (lift-at fast0) n0)\n" + 
            " (= (passengers fast0) n0)\n" + 
            " (reachable-floor fast0 n0)\n" + 
            " (can-hold fast0 n1)\n" + 
            " (can-hold fast0 n2)\n" + 
            " (reachable-floor fast0 n2)\n" + 
            " (can-hold fast0 n3)\n" + 
            " (reachable-floor fast0 n4)\n" + 
            " (reachable-floor fast0 n6)\n" + 
            " (reachable-floor fast0 n8)\n" + 
            " (= (at p0) n8)\n" + 
            " (= (at p1) n3)\n" + 
            " (= (at p2) n2)\n" + 
            ")\n" + 
            "(:global-goal (and\n" + 
            " (= (at p0) n4)\n" + 
            " (= (at p1) n6)\n" + 
            " (= (at p2) n1)\n" + 
            "))\n" + 
            ")\n" + 
            "";

    private static String problemSlow0 = "(define (problem elevators-sequencedstrips-p8_3_1)\n" + 
            "(:domain elevators)\n" + 
            "(:objects\n" + 
            " n0 n1 n2 n3 n4 n5 n6 n7 n8 - count\n" + 
            " p0 p1 p2 - passenger\n" + 
            " fast0 - fast-elevator\n" + 
            " slow0-0 slow1-0 - slow-elevator\n" + 
            ")\n" + 
            "(:shared-data\n" + 
            "  ((at ?person - passenger) - (either count elevator)) - (either fast0 slow1-0)\n" + 
            ")\n" + 
            "(:init (myAgent slow0-0)\n" + 
            " (= (next n0) n1)\n" + 
            " (= (next n1) n2)\n" + 
            " (= (next n2) n3)\n" + 
            " (= (next n3) n4)\n" + 
            " (= (next n4) n5)\n" + 
            " (= (next n5) n6)\n" + 
            " (= (next n6) n7)\n" + 
            " (= (next n7) n8)\n" + 
            " (above n0 n1)\n" + 
            " (above n0 n2)\n" + 
            " (above n0 n3)\n" + 
            " (above n0 n4)\n" + 
            " (above n0 n5)\n" + 
            " (above n0 n6)\n" + 
            " (above n0 n7)\n" + 
            " (above n0 n8)\n" + 
            " (above n1 n2)\n" + 
            " (above n1 n3)\n" + 
            " (above n1 n4)\n" + 
            " (above n1 n5)\n" + 
            " (above n1 n6)\n" + 
            " (above n1 n7)\n" + 
            " (above n1 n8)\n" + 
            " (above n2 n3)\n" + 
            " (above n2 n4)\n" + 
            " (above n2 n5)\n" + 
            " (above n2 n6)\n" + 
            " (above n2 n7)\n" + 
            " (above n2 n8)\n" + 
            " (above n3 n4)\n" + 
            " (above n3 n5)\n" + 
            " (above n3 n6)\n" + 
            " (above n3 n7)\n" + 
            " (above n3 n8)\n" + 
            " (above n4 n5)\n" + 
            " (above n4 n6)\n" + 
            " (above n4 n7)\n" + 
            " (above n4 n8)\n" + 
            " (above n5 n6)\n" + 
            " (above n5 n7)\n" + 
            " (above n5 n8)\n" + 
            " (above n6 n7)\n" + 
            " (above n6 n8)\n" + 
            " (above n7 n8)\n" + 
            " (= (lift-at slow0-0) n2)\n" + 
            " (= (passengers slow0-0) n0)\n" + 
            " (reachable-floor slow0-0 n0)\n" + 
            " (can-hold slow0-0 n1)\n" + 
            " (reachable-floor slow0-0 n1)\n" + 
            " (can-hold slow0-0 n2)\n" + 
            " (reachable-floor slow0-0 n2)\n" + 
            " (reachable-floor slow0-0 n3)\n" + 
            " (reachable-floor slow0-0 n4)\n" + 
            " (= (at p0) n8)\n" + 
            " (= (at p1) n3)\n" + 
            " (= (at p2) n2)\n" + 
            ")\n" + 
            "(:global-goal (and\n" + 
            " (= (at p0) n4)\n" + 
            " (= (at p1) n6)\n" + 
            " (= (at p2) n1)\n" + 
            "))\n" + 
            ")\n" + 
            "";

    private static String problemSlow1 = "(define (problem elevators-sequencedstrips-p8_3_1)\n" + 
            "(:domain elevators)\n" + 
            "(:objects\n" + 
            " n0 n1 n2 n3 n4 n5 n6 n7 n8 - count\n" + 
            " p0 p1 p2 - passenger\n" + 
            " fast0 - fast-elevator\n" + 
            " slow0-0 slow1-0 - slow-elevator\n" + 
            ")\n" + 
            "(:shared-data\n" + 
            "  ((at ?person - passenger) - (either count elevator)) - (either fast0 slow0-0)\n" + 
            ")\n" + 
            "(:init (myAgent slow1-0)\n" + 
            " (= (next n0) n1)\n" + 
            " (= (next n1) n2)\n" + 
            " (= (next n2) n3)\n" + 
            " (= (next n3) n4)\n" + 
            " (= (next n4) n5)\n" + 
            " (= (next n5) n6)\n" + 
            " (= (next n6) n7)\n" + 
            " (= (next n7) n8)\n" + 
            " (above n0 n1)\n" + 
            " (above n0 n2)\n" + 
            " (above n0 n3)\n" + 
            " (above n0 n4)\n" + 
            " (above n0 n5)\n" + 
            " (above n0 n6)\n" + 
            " (above n0 n7)\n" + 
            " (above n0 n8)\n" + 
            " (above n1 n2)\n" + 
            " (above n1 n3)\n" + 
            " (above n1 n4)\n" + 
            " (above n1 n5)\n" + 
            " (above n1 n6)\n" + 
            " (above n1 n7)\n" + 
            " (above n1 n8)\n" + 
            " (above n2 n3)\n" + 
            " (above n2 n4)\n" + 
            " (above n2 n5)\n" + 
            " (above n2 n6)\n" + 
            " (above n2 n7)\n" + 
            " (above n2 n8)\n" + 
            " (above n3 n4)\n" + 
            " (above n3 n5)\n" + 
            " (above n3 n6)\n" + 
            " (above n3 n7)\n" + 
            " (above n3 n8)\n" + 
            " (above n4 n5)\n" + 
            " (above n4 n6)\n" + 
            " (above n4 n7)\n" + 
            " (above n4 n8)\n" + 
            " (above n5 n6)\n" + 
            " (above n5 n7)\n" + 
            " (above n5 n8)\n" + 
            " (above n6 n7)\n" + 
            " (above n6 n8)\n" + 
            " (above n7 n8)\n" + 
            " (= (lift-at slow1-0) n4)\n" + 
            " (= (passengers slow1-0) n0)\n" + 
            " (can-hold slow1-0 n1)\n" + 
            " (can-hold slow1-0 n2)\n" + 
            " (reachable-floor slow1-0 n4)\n" + 
            " (reachable-floor slow1-0 n5)\n" + 
            " (reachable-floor slow1-0 n6)\n" + 
            " (reachable-floor slow1-0 n7)\n" + 
            " (reachable-floor slow1-0 n8)\n" + 
            " (= (at p0) n8)\n" + 
            " (= (at p1) n3)\n" + 
            " (= (at p2) n2)\n" + 
            ")\n" + 
            "(:global-goal (and\n" + 
            " (= (at p0) n4)\n" + 
            " (= (at p1) n6)\n" + 
            " (= (at p2) n1)\n" + 
            "))\n" + 
            ")\n" + 
            "";

}

