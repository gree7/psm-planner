package cz.agents.dimap.data;


import java.util.HashMap;
import java.util.Map;

import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlProblem;
import experiments.psm.fmap.FmapMultiAgentProblem;


public class BeerFmap extends PddlTestProblem {
    public static MultiAgentProblem getProblem() {
        Map<String, PddlProblem> problems = new HashMap<String, PddlProblem>();
        problems.put("truck", createProblem(domainTruck, problemTruck, false));
        problems.put("plane", createProblem(domainPlane, problemPlane, false));
        
        return new FmapMultiAgentProblem(problems);
    }

    private static String domainTruck = "(define (domain beerproblem)\n" + 
            "    (:requirements :strips :typing)\n" + 
            "    \n" + 
            "    (:types\n" + 
            "        agent city - object\n" + 
            "    )\n" + 
            "\n" + 
            "    (:predicates\n" + 
            "        (at \n" + 
            "            ?veh  - agent   \n" + 
            "            ?city - city \n" + 
            "        )\n" + 
            "        (beer_in ?inveh - agent)\n" + 
            "        (beer_at ?atcity - city)\n" + 
            "        (road ?from ?to - city)" +
            "        (airlink ?from ?to - city)" +
            "    )\n" + 
            "\n" + 
            "    (:action drive_truck\n" + 
            "        :parameters \n" + 
            "        (\n" + 
            "            ?from - city\n" + 
            "            ?to - city\n" + 
            "        )\n" + 
            "        :precondition \n" + 
            "        (and\n" + 
            "            (at truck ?from)\n" + 
            "            (road ?from ?to)" +
            "        )\n" + 
            "        :effect \n" + 
            "        (and\n" + 
            "            (at truck ?to)\n" + 
            "            (not (at truck ?from))\n" + 
            "        )\n" + 
            "    )\n" + 
            "\n" + 
            "    (:action load_truck\n" + 
            "        :parameters \n" + 
            "        (\n" + 
            "            ?city - city\n" + 
            "        )\n" + 
            "        :precondition \n" + 
            "        (and\n" + 
            "            (beer_at ?city)\n" + 
            "            (at truck ?city)\n" + 
            "        )\n" + 
            "        :effect \n" + 
            "        (and\n" + 
            "            (beer_in truck)\n" + 
            "            (not (beer_at ?city))\n" + 
            "        )\n" + 
            "    )\n" + 
            "\n" + 
            "    (:action unload_truck\n" + 
            "        :parameters \n" + 
            "        (\n" + 
            "            ?city - city\n" + 
            "        )\n" + 
            "        :precondition \n" + 
            "        (and\n" + 
            "            (beer_in truck)\n" + 
            "            (at truck ?city)\n" + 
            "        )\n" + 
            "        :effect \n" + 
            "        (and\n" + 
            "            (beer_at ?city)\n" + 
            "            (not (beer_in truck))\n" + 
            "        )\n" + 
            "    )\n" + 
            ")\n" + 
            "";

    private static String problemTruck = "(define (problem beerproblem-truck)\n" + 
            "    (:domain beerproblem)\n" + 
            "    (:requirements :strips :typing)\n" + 
            "    (:objects\n" + 
            "        plane truck - agent\n" + 
            "        prague brno ostrava - city\n" + 
            "    )\n" + 
            "    (:shared-data\n" + 
            "        (beer_at ?atcity - city)\n" + 
            "        - plane\n" + 
            "    )\n" + 
            "    (:init\n" + 
            "        (at plane prague)\n" + 
            "        (at truck brno)\n" + 
            "        (beer_at prague)\n" + 
            "        (road brno ostrava)\n" +
            "        (road ostrava brno)\n" +
            "        (airlink prague brno)\n" +
            "        (airlink brno prague)\n" +
            "    )\n" + 
            "    (:goal (and\n" + 
            "        (beer_at ostrava)\n" + 
            "    ))\n" + 
            ")\n" + 
            "";

    private static String domainPlane = "(define (domain beerproblem)\n" + 
            "    (:requirements :strips :typing)\n" + 
            "    \n" + 
            "    (:types\n" + 
            "        agent city - object\n" + 
            "    )\n" + 
            "\n" + 
            "    (:predicates\n" + 
            "        (at \n" + 
            "            ?veh  - agent   \n" + 
            "            ?city - city \n" + 
            "        )\n" + 
            "        (beer_in ?inveh - agent)\n" + 
            "        (beer_at ?atcity - city)\n" + 
            "        (road ?from ?to - city)" +
            "        (airlink ?from ?to - city)" +
            "    )\n" + 
            "\n" + 
            "    (:action fly_plane\n" + 
            "        :parameters \n" + 
            "        (\n" + 
            "            ?from - city\n" + 
            "            ?to - city)\n" + 
            "        :precondition \n" + 
            "        (and \n" + 
            "            (at plane ?from)\n" + 
            "            (airlink ?from ?to)" +
            "        )\n" + 
            "        :effect \n" + 
            "        (and \n" +
            "            (at plane ?to)\n" + 
            "            (not (at plane ?from))\n" + 
            "        )\n" + 
            "    )\n" + 
            "\n" + 
            "    (:action load_plane\n" + 
            "        :parameters \n" + 
            "        (\n" + 
            "            ?city - city\n" + 
            "        )\n" + 
            "        :precondition \n" + 
            "        (and\n" + 
            "            (beer_at ?city)\n" + 
            "            (at plane ?city)\n" + 
            "        )\n" + 
            "        :effect \n" + 
            "        (and\n" + 
            "            (beer_in plane)\n" + 
            "            (not (beer_at ?city))\n" + 
            "        )\n" + 
            "    )\n" + 
            "\n" + 
            "    (:action unload_plane\n" + 
            "        :parameters \n" + 
            "        (\n" + 
            "            ?city - city\n" + 
            "        )\n" + 
            "        :precondition \n" + 
            "        (and\n" + 
            "            (beer_in plane)\n" + 
            "            (at plane ?city)\n" + 
            "        )\n" + 
            "        :effect \n" + 
            "        (and\n" + 
            "            (beer_at ?city)\n" + 
            "            (not (beer_in plane))\n" + 
            "        )\n" + 
            "    )\n" + 
            ")\n" + 
            "";

    private static String problemPlane = "(define (problem beerproblem-plane)\n" + 
            "    (:domain beerproblem)\n" + 
            "    (:requirements :strips :typing)\n" + 
            "    (:objects\n" + 
            "        plane truck - agent\n" + 
            "        prague brno ostrava - city\n" + 
            "    )\n" + 
            "    (:shared-data\n" + 
            "        (beer_at ?atcity - city)\n" + 
            "        - truck\n" + 
            "    )\n" + 
            "    (:init\n" + 
            "        (at plane prague)\n" + 
            "        (at truck brno)\n" + 
            "        (beer_at prague)\n" + 
            "        (road brno ostrava)\n" +
            "        (road ostrava brno)\n" +
            "        (airlink prague brno)\n" +
            "        (airlink brno prague)\n" +
            "    )\n" + 
            "    (:goal (and\n" + 
            "        (beer_at ostrava)\n" + 
            "    ))\n" + 
            ")\n" + 
            "";
}

