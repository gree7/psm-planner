package cz.agents.dimap.data;


import java.util.HashMap;
import java.util.Map;

import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlProblem;
import experiments.psm.fmap.FmapMultiAgentProblem;


public class Logistics01Fmap extends PddlTestProblem {
    public static MultiAgentProblem getProblem() {
        Map<String, PddlProblem> problems = new HashMap<String, PddlProblem>();
        problems.put("apn1", createProblem(domain, problemAirplane1, false));
        problems.put("tru1", createProblem(domain, problemTruck1, false));
        problems.put("tru2", createProblem(domain, problemTruck2, false));
        
        return new FmapMultiAgentProblem(problems);
    }

    private static String domain = "(define (domain logistics)\n" + 
            "(:requirements :typing :equality :fluents)\n" + 
            "(:types city place package agent - object\n" + 
            "        truck airplane - agent\n" + 
            "        airport location - place)\n" + 
            "(:predicates (in-city ?loc - place ?city - city)\n" + 
            "             (myAgent ?a - agent))\n" + 
            "(:functions  (at ?a - agent) - place\n" + 
            "             (in ?pkg - package) - (either place agent))\n" + 
            "(:action LoadTruck\n" + 
            " :parameters    (?pkg - package ?truck - truck ?loc - place)\n" + 
            " :precondition  (and (myAgent ?truck) (= (at ?truck) ?loc) (= (in ?pkg) ?loc))\n" + 
            " :effect        (assign (in ?pkg) ?truck))\n" + 
            "(:action LoadAirplane\n" + 
            " :parameters   (?pkg - package ?airplane - airplane ?loc - place)\n" + 
            " :precondition (and (myAgent ?airplane) (= (in ?pkg) ?loc) (= (at ?airplane) ?loc))\n" + 
            " :effect       (assign (in ?pkg) ?airplane))\n" + 
            "(:action UnloadTruck\n" + 
            " :parameters   (?pkg - package ?truck - truck ?loc - place)\n" + 
            " :precondition (and (myAgent ?truck) (= (at ?truck) ?loc) (= (in ?pkg) ?truck))\n" + 
            " :effect       (assign (in ?pkg) ?loc))\n" + 
            "(:action UnloadAirplane\n" + 
            " :parameters   (?pkg - package ?airplane - airplane ?loc - place)\n" + 
            " :precondition (and (myAgent ?airplane) (= (in ?pkg) ?airplane) (= (at ?airplane) ?loc))\n" + 
            " :effect       (assign (in ?pkg) ?loc))\n" + 
            "(:action DriveTruck\n" + 
            " :parameters (?truck - truck ?loc-from - place ?loc-to - place ?city - city)\n" + 
            " :precondition (and (myAgent ?truck) (= (at ?truck) ?loc-from)\n" + 
            "               (in-city ?loc-from ?city) (in-city ?loc-to ?city))\n" + 
            " :effect (assign (at ?truck) ?loc-to))\n" + 
            "(:action FlyAirplane\n" + 
            " :parameters (?airplane - airplane ?loc-from - airport ?loc-to - airport)\n" + 
            " :precondition (= (at ?airplane) ?loc-from)\n" + 
            " :effect       (assign (at ?airplane) ?loc-to)))\n" + 
            "";

    private static String problemAirplane1 = "(define (problem logistics-4-0)\n" + 
            "(:domain logistics)\n" + 
            "(:objects\n" + 
            " apn1 - airplane\n" + 
            " apt1 apt2 - airport\n" + 
            " pos2 pos1 - location\n" + 
            " cit2 cit1 - city\n" + 
            " tru2 tru1 - truck\n" + 
            " obj23 obj22 obj21 obj13 obj12 obj11 - package\n" + 
            ")\n" + 
            "(:shared-data\n" + 
            "    ((in ?pkg - package) - (either place agent)) - \n" + 
            "(either tru2 tru1)\n" + 
            ")\n" + 
            "(:init (myAgent apn1)\n" + 
            " (= (at apn1) apt2)\n" + 
            " (= (in obj23) pos2)\n" + 
            " (= (in obj22) pos2)\n" + 
            " (= (in obj21) pos2)\n" + 
            " (= (in obj13) pos1)\n" + 
            " (= (in obj12) pos1)\n" + 
            " (= (in obj11) pos1)\n" + 
            " (not (in-city apt1 cit2))\n" + 
            " (in-city apt1 cit1)\n" + 
            " (in-city apt2 cit2)\n" + 
            " (not (in-city apt2 cit1))\n" + 
            " (in-city pos2 cit2)\n" + 
            " (not (in-city pos2 cit1))\n" + 
            " (not (in-city pos1 cit2))\n" + 
            " (in-city pos1 cit1)\n" + 
            ")\n" + 
            "(:global-goal (and\n" + 
            " (= (in obj11) apt1)\n" + 
            " (= (in obj23) pos1)\n" + 
            " (= (in obj13) apt1)\n" + 
            " (= (in obj21) pos1)\n" + 
            "))\n" + 
            "\n" + 
            ")\n" + 
            "";

    private static String problemTruck1 = "(define (problem logistics-4-0)\n" + 
            "(:domain logistics)\n" + 
            "(:objects\n" + 
            " apn1 - airplane\n" + 
            " apt1 apt2 - airport\n" + 
            " pos2 pos1 - location\n" + 
            " cit2 cit1 - city\n" + 
            " tru2 tru1 - truck\n" + 
            " obj23 obj22 obj21 obj13 obj12 obj11 - package\n" + 
            ")\n" + 
            "(:shared-data\n" + 
            "    ((in ?pkg - package) - (either place agent)) - \n" + 
            "(either apn1 tru2)\n" + 
            ")\n" + 
            "(:init (myAgent tru1)\n" + 
            " (= (at tru1) pos1)\n" + 
            " (= (in obj23) pos2)\n" + 
            " (= (in obj22) pos2)\n" + 
            " (= (in obj21) pos2)\n" + 
            " (= (in obj13) pos1)\n" + 
            " (= (in obj12) pos1)\n" + 
            " (= (in obj11) pos1)\n" + 
            " (not (in-city apt1 cit2))\n" + 
            " (in-city apt1 cit1)\n" + 
            " (in-city apt2 cit2)\n" + 
            " (not (in-city apt2 cit1))\n" + 
            " (in-city pos2 cit2)\n" + 
            " (not (in-city pos2 cit1))\n" + 
            " (not (in-city pos1 cit2))\n" + 
            " (in-city pos1 cit1)\n" + 
            ")\n" + 
            "(:global-goal (and\n" + 
            " (= (in obj11) apt1)\n" + 
            " (= (in obj23) pos1)\n" + 
            " (= (in obj13) apt1)\n" + 
            " (= (in obj21) pos1)\n" + 
            "))\n" + 
            "\n" + 
            ")\n" + 
            "";

    private static String problemTruck2 = "(define (problem logistics-4-0)\n" + 
            "(:domain logistics)\n" + 
            "(:objects\n" + 
            " apn1 - airplane\n" + 
            " apt1 apt2 - airport\n" + 
            " pos2 pos1 - location\n" + 
            " cit2 cit1 - city\n" + 
            " tru2 tru1 - truck\n" + 
            " obj23 obj22 obj21 obj13 obj12 obj11 - package\n" + 
            ")\n" + 
            "(:shared-data\n" + 
            "    ((in ?pkg - package) - (either place agent)) - \n" + 
            "(either apn1 tru1)\n" + 
            ")\n" + 
            "(:init (myAgent tru2)\n" + 
            " (= (at tru2) pos2)\n" + 
            " (= (in obj23) pos2)\n" + 
            " (= (in obj22) pos2)\n" + 
            " (= (in obj21) pos2)\n" + 
            " (= (in obj13) pos1)\n" + 
            " (= (in obj12) pos1)\n" + 
            " (= (in obj11) pos1)\n" + 
            " (not (in-city apt1 cit2))\n" + 
            " (in-city apt1 cit1)\n" + 
            " (in-city apt2 cit2)\n" + 
            " (not (in-city apt2 cit1))\n" + 
            " (in-city pos2 cit2)\n" + 
            " (not (in-city pos2 cit1))\n" + 
            " (not (in-city pos1 cit2))\n" + 
            " (in-city pos1 cit1)\n" + 
            ")\n" + 
            "(:global-goal (and\n" + 
            " (= (in obj11) apt1)\n" + 
            " (= (in obj23) pos1)\n" + 
            " (= (in obj13) apt1)\n" + 
            " (= (in obj21) pos1)\n" + 
            "))\n" + 
            "\n" + 
            ")\n" + 
            "";

}

