package cz.agents.dimap.data;


import java.util.HashMap;
import java.util.Map;

import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlProblem;
import experiments.psm.fmap.FmapMultiAgentProblem;


public class Depots01Fmap extends PddlTestProblem {
    public static MultiAgentProblem getProblem() {
        Map<String, PddlProblem> problems = new HashMap<String, PddlProblem>();
        problems.put("depot0", createProblem(domainDepot, problemDepot0, false));
        problems.put("distributor0", createProblem(domainDepot, problemDistributor0, false));
        problems.put("distributor1", createProblem(domainDepot, problemDistributor1, false));
        problems.put("truck0", createProblem(domainTruck, problemTruck0, false));
        problems.put("truck1", createProblem(domainTruck, problemTruck1, false));
        
        return new FmapMultiAgentProblem(problems);
    }

    private static String domainDepot = "(define (domain depot)\n" + 
            "(:requirements :typing :equality :fluents)\n" + 
            "(:types place hoist surface - object\n" + 
            "        depot distributor - (either place agent)\n" + 
            "        truck - agent\n" + 
            "        crate pallet - surface)\n" + 
            "(:predicates\n" + 
            "  (myAgent ?a - place)\n" + 
            "  (clear ?x - (either surface hoist)))\n" + 
            "(:functions\n" + 
            "  (located ?h - hoist) - place\n" + 
            "  (at ?t - truck) - place\n" + 
            "  (placed ?p - pallet) - place\n" + 
            "  (pos ?c - crate) - (either place truck)\n" + 
            "  (on ?c - crate) - (either surface hoist truck))\n" + 
            "(:action LiftP\n" + 
            " :parameters (?h - hoist ?c - crate ?z - pallet ?p - place)\n" + 
            " :precondition (and (myAgent ?p) (= (located ?h) ?p)\n" + 
            "                    (= (placed ?z) ?p) (clear ?h) (= (pos ?c) ?p)\n" + 
            "                    (= (on ?c) ?z) (clear ?c))\n" + 
            " :effect (and (assign (on ?c) ?h) (not (clear ?c)) (not (clear ?h))\n" + 
            "              (clear ?z)))\n" + 
            "(:action LiftC\n" + 
            " :parameters (?h - hoist ?c - crate ?z - crate ?p - place)\n" + 
            " :precondition (and (myAgent ?p) (= (located ?h) ?p)\n" + 
            "                    (= (pos ?z) ?p) (clear ?h)\n" + 
            "                    (= (pos ?c) ?p) (= (on ?c) ?z) (clear ?c))\n" + 
            " :effect (and (assign (on ?c) ?h) (not (clear ?c)) (not (clear ?h))\n" + 
            "              (clear ?z)))\n" + 
            "(:action DropP\n" + 
            " :parameters (?h - hoist ?c - crate ?z - pallet ?p - place)\n" + 
            " :precondition (and (myAgent ?p) (= (located ?h) ?p)\n" + 
            "                    (= (placed ?z) ?p) (clear ?z) (= (on ?c) ?h)\n" + 
            "                    (not (clear ?c)) (not (clear ?h)))\n" + 
            " :effect (and (clear ?h) (clear ?c) (not (clear ?z))\n" + 
            "              (assign (on ?c) ?z)))\n" + 
            "(:action DropC\n" + 
            " :parameters (?h - hoist ?c - crate ?z - crate ?p - place)\n" + 
            " :precondition (and (myAgent ?p) (= (located ?h) ?p)\n" + 
            "                    (= (pos ?z) ?p) (clear ?z)\n" + 
            "                    (= (on ?c) ?h) (not (clear ?c))\n" + 
            "                    (not (clear ?h)))\n" + 
            " :effect (and (clear ?h) (clear ?c) (not (clear ?z))\n" + 
            "              (assign (on ?c) ?z))))";

    private static String problemDepot0 = "(define (problem depotprob1818)\n" + 
            "(:domain depot)\n" + 
            "(:objects\n" + 
            " depot0 - depot\n" + 
            " distributor0 distributor1 - distributor\n" + 
            " truck0 truck1 - truck\n" + 
            " crate0 crate1 - crate\n" + 
            " pallet0 pallet1 pallet2 - pallet\n" + 
            " hoist0 hoist1 hoist2 - hoist\n" + 
            ")\n" + 
            "(:shared-data\n" + 
            "  (clear ?x - (either surface hoist))\n" + 
            "  ((at ?t - truck) - place)\n" + 
            "  ((pos ?c - crate) - (either place truck))\n" + 
            "  ((on ?c - crate) - (either surface hoist truck)) - \n" + 
            "(either distributor0 distributor1 truck0 truck1)\n" + 
            ")\n" + 
            "(:init\n" + 
            " (myAgent depot0)\n" + 
            " (= (pos crate0) distributor0)\n" + 
            " (clear crate0)\n" + 
            " (= (on crate0) pallet1)\n" + 
            " (= (pos crate1) depot0)\n" + 
            " (clear crate1)\n" + 
            " (= (on crate1) pallet0)\n" + 
            " (= (at truck0) distributor1)\n" + 
            " (= (at truck1) depot0)\n" + 
            " (= (located hoist0) depot0)\n" + 
            " (clear hoist0)\n" + 
            " (= (located hoist1) distributor0)\n" + 
            " (clear hoist1)\n" + 
            " (= (located hoist2) distributor1)\n" + 
            " (clear hoist2)\n" + 
            " (= (placed pallet0) depot0)\n" + 
            " (not (clear pallet0))\n" + 
            " (= (placed pallet1) distributor0)\n" + 
            " (not (clear pallet1))\n" + 
            " (= (placed pallet2) distributor1)\n" + 
            " (clear pallet2)\n" + 
            ")\n" + 
            "(:global-goal (and\n" + 
            " (= (on crate0) pallet2)\n" + 
            " (= (on crate1) pallet1)\n" + 
            "))\n" + 
            "\n" + 
            ")\n" + 
            "";

    private static String problemDistributor0 = "(define (problem depotprob1818)\n" + 
            "(:domain depot)\n" + 
            "(:objects\n" + 
            " depot0 - depot\n" + 
            " distributor0 distributor1 - distributor\n" + 
            " truck0 truck1 - truck\n" + 
            " crate0 crate1 - crate\n" + 
            " pallet0 pallet1 pallet2 - pallet\n" + 
            " hoist0 hoist1 hoist2 - hoist\n" + 
            ")\n" + 
            "(:shared-data\n" + 
            "  (clear ?x - (either surface hoist))\n" + 
            "  ((at ?t - truck) - place)\n" + 
            "  ((pos ?c - crate) - (either place truck))\n" + 
            "  ((on ?c - crate) - (either surface hoist truck)) - \n" + 
            "(either depot0 distributor1 truck0 truck1)\n" + 
            ")\n" + 
            "(:init\n" + 
            " (myAgent distributor0)\n" + 
            " (= (pos crate0) distributor0)\n" + 
            " (clear crate0)\n" + 
            " (= (on crate0) pallet1)\n" + 
            " (= (pos crate1) depot0)\n" + 
            " (clear crate1)\n" + 
            " (= (on crate1) pallet0)\n" + 
            " (= (at truck0) distributor1)\n" + 
            " (= (at truck1) depot0)\n" + 
            " (= (located hoist0) depot0)\n" + 
            " (clear hoist0)\n" + 
            " (= (located hoist1) distributor0)\n" + 
            " (clear hoist1)\n" + 
            " (= (located hoist2) distributor1)\n" + 
            " (clear hoist2)\n" + 
            " (= (placed pallet0) depot0)\n" + 
            " (not (clear pallet0))\n" + 
            " (= (placed pallet1) distributor0)\n" + 
            " (not (clear pallet1))\n" + 
            " (= (placed pallet2) distributor1)\n" + 
            " (clear pallet2)\n" + 
            ")\n" + 
            "(:global-goal (and\n" + 
            " (= (on crate0) pallet2)\n" + 
            " (= (on crate1) pallet1)\n" + 
            "))\n" + 
            "\n" + 
            ")\n" + 
            "";

    private static String problemDistributor1 = "(define (problem depotprob1818)\n" + 
            "(:domain depot)\n" + 
            "(:objects\n" + 
            " depot0 - depot\n" + 
            " distributor0 distributor1 - distributor\n" + 
            " truck0 truck1 - truck\n" + 
            " crate0 crate1 - crate\n" + 
            " pallet0 pallet1 pallet2 - pallet\n" + 
            " hoist0 hoist1 hoist2 - hoist\n" + 
            ")\n" + 
            "(:shared-data\n" + 
            "  (clear ?x - (either surface hoist))\n" + 
            "  ((at ?t - truck) - place)\n" + 
            "  ((pos ?c - crate) - (either place truck))\n" + 
            "  ((on ?c - crate) - (either surface hoist truck)) - \n" + 
            "(either depot0 distributor0 truck0 truck1)\n" + 
            ")\n" + 
            "(:init\n" + 
            " (myAgent distributor1)\n" + 
            " (= (pos crate0) distributor0)\n" + 
            " (clear crate0)\n" + 
            " (= (on crate0) pallet1)\n" + 
            " (= (pos crate1) depot0)\n" + 
            " (clear crate1)\n" + 
            " (= (on crate1) pallet0)\n" + 
            " (= (at truck0) distributor1)\n" + 
            " (= (at truck1) depot0)\n" + 
            " (= (located hoist0) depot0)\n" + 
            " (clear hoist0)\n" + 
            " (= (located hoist1) distributor0)\n" + 
            " (clear hoist1)\n" + 
            " (= (located hoist2) distributor1)\n" + 
            " (clear hoist2)\n" + 
            " (= (placed pallet0) depot0)\n" + 
            " (not (clear pallet0))\n" + 
            " (= (placed pallet1) distributor0)\n" + 
            " (not (clear pallet1))\n" + 
            " (= (placed pallet2) distributor1)\n" + 
            " (clear pallet2)\n" + 
            ")\n" + 
            "(:global-goal (and\n" + 
            " (= (on crate0) pallet2)\n" + 
            " (= (on crate1) pallet1)\n" + 
            "))\n" + 
            "\n" + 
            ")\n" + 
            "";

    private static String domainTruck = "(define (domain depot)\n" + 
            "(:requirements :typing :equality :fluents)\n" + 
            "(:types place hoist surface - object\n" + 
            "      depot distributor - (either place agent)\n" + 
            "      truck - agent\n" + 
            "      crate pallet - surface)\n" + 
            "(:predicates\n" + 
            "  (myAgent ?a - truck)\n" + 
            "  (clear ?x - (either surface hoist)))\n" + 
            "(:functions\n" + 
            "  (located ?h - hoist) - place\n" + 
            "  (at ?t - truck) - place\n" + 
            "  (placed ?p - pallet) - place\n" + 
            "  (pos ?c - crate) - (either place truck)\n" + 
            "  (on ?c - crate) - (either surface hoist truck))\n" + 
            "(:action Drive\n" + 
            " :parameters (?t - truck ?x ?y - place)\n" + 
            " :precondition (and (myAgent ?t) (= (at ?t) ?x))\n" + 
            " :effect (and (assign (at ?t) ?y)))\n" + 
            "(:action Load\n" + 
            " :parameters (?h - hoist ?c - crate ?t - truck ?p - place)\n" + 
            " :precondition (and (myAgent ?t) (= (at ?t) ?p) (= (pos ?c) ?p)\n" + 
            "                    (not (clear ?c)) (not (clear ?h))\n" + 
            "                    (= (on ?c) ?h) (= (located ?h) ?p))\n" + 
            " :effect (and (clear ?h) (clear ?c) (assign (pos ?c) ?t)\n" + 
            "              (assign (on ?c) ?t)))\n" + 
            "(:action Unload\n" + 
            " :parameters (?h - hoist ?c - crate ?t - truck ?p - place)\n" + 
            " :precondition (and (myAgent ?t) (= (located ?h) ?p) (= (at ?t) ?p)\n" + 
            "                   (= (pos ?c) ?t) (= (on ?c) ?t) (clear ?h)\n" + 
            "                   (clear ?c))\n" + 
            " :effect (and (assign (pos ?c) ?p) (assign (on ?c) ?h)\n" + 
            "              (not (clear ?c)) (not (clear ?h))))\n" + 
            ")\n" + 
            "";

    private static String problemTruck0 = "(define (problem depotprob1818)\n" + 
            "(:domain depot)\n" + 
            "(:objects\n" + 
            " depot0 - depot\n" + 
            " distributor0 distributor1 - distributor\n" + 
            " truck0 truck1 - truck\n" + 
            " crate0 crate1 - crate\n" + 
            " pallet0 pallet1 pallet2 - pallet\n" + 
            " hoist0 hoist1 hoist2 - hoist\n" + 
            ")\n" + 
            "(:shared-data\n" + 
            "  (clear ?x - (either surface hoist))\n" + 
            "  ((at ?t - truck) - place)\n" + 
            "  ((pos ?c - crate) - (either place truck))\n" + 
            "  ((on ?c - crate) - (either surface hoist truck)) - \n" + 
            "(either depot0 distributor0 distributor1 truck1)\n" + 
            ")\n" + 
            "(:init\n" + 
            " (myAgent truck0)\n" + 
            " (= (pos crate0) distributor0)\n" + 
            " (clear crate0)\n" + 
            " (= (on crate0) pallet1)\n" + 
            " (= (pos crate1) depot0)\n" + 
            " (clear crate1)\n" + 
            " (= (on crate1) pallet0)\n" + 
            " (= (at truck0) distributor1)\n" + 
            " (= (at truck1) depot0)\n" + 
            " (= (located hoist0) depot0)\n" + 
            " (clear hoist0)\n" + 
            " (= (located hoist1) distributor0)\n" + 
            " (clear hoist1)\n" + 
            " (= (located hoist2) distributor1)\n" + 
            " (clear hoist2)\n" + 
            " (= (placed pallet0) depot0)\n" + 
            " (not (clear pallet0))\n" + 
            " (= (placed pallet1) distributor0)\n" + 
            " (not (clear pallet1))\n" + 
            " (= (placed pallet2) distributor1)\n" + 
            " (clear pallet2)\n" + 
            ")\n" + 
            "(:global-goal (and\n" + 
            " (= (on crate0) pallet2)\n" + 
            " (= (on crate1) pallet1)\n" + 
            "))\n" + 
            "\n" + 
            ")\n" + 
            "";

    private static String problemTruck1 = "(define (problem depotprob1818)\n" + 
            "(:domain depot)\n" + 
            "(:objects\n" + 
            " depot0 - depot\n" + 
            " distributor0 distributor1 - distributor\n" + 
            " truck0 truck1 - truck\n" + 
            " crate0 crate1 - crate\n" + 
            " pallet0 pallet1 pallet2 - pallet\n" + 
            " hoist0 hoist1 hoist2 - hoist\n" + 
            ")\n" + 
            "(:shared-data\n" + 
            "  (clear ?x - (either surface hoist))\n" + 
            "  ((at ?t - truck) - place)\n" + 
            "  ((pos ?c - crate) - (either place truck))\n" + 
            "  ((on ?c - crate) - (either surface hoist truck)) - \n" + 
            "(either depot0 distributor0 distributor1 truck0)\n" + 
            ")\n" + 
            "(:init\n" + 
            " (myAgent truck1)\n" + 
            " (= (pos crate0) distributor0)\n" + 
            " (clear crate0)\n" + 
            " (= (on crate0) pallet1)\n" + 
            " (= (pos crate1) depot0)\n" + 
            " (clear crate1)\n" + 
            " (= (on crate1) pallet0)\n" + 
            " (= (at truck0) distributor1)\n" + 
            " (= (at truck1) depot0)\n" + 
            " (= (located hoist0) depot0)\n" + 
            " (clear hoist0)\n" + 
            " (= (located hoist1) distributor0)\n" + 
            " (clear hoist1)\n" + 
            " (= (located hoist2) distributor1)\n" + 
            " (clear hoist2)\n" + 
            " (= (placed pallet0) depot0)\n" + 
            " (not (clear pallet0))\n" + 
            " (= (placed pallet1) distributor0)\n" + 
            " (not (clear pallet1))\n" + 
            " (= (placed pallet2) distributor1)\n" + 
            " (clear pallet2)\n" + 
            ")\n" + 
            "(:global-goal (and\n" + 
            " (= (on crate0) pallet2)\n" + 
            " (= (on crate1) pallet1)\n" + 
            "))\n" + 
            "\n" + 
            ")\n" + 
            "";
}

