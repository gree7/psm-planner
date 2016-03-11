package cz.agents.dimap.tools.pddl.ma;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class FmapMaProjectionLogistics01Test {

    private static String domainString =
            ";; logistics domain\n" + 
            ";;\n" + 
            ";; logistics-typed-length: strips + simple types\n" + 
            ";;    based on logistics-strips-length.\n" + 
            ";; Tue Dec  1 16:10:25 EST 1998 Henry Kautz\n" + 
            "\n" + 
            "(define (domain logistics-ipc-fmap)\n" + 
            "  (:requirements :strips :typing)\n" + 
            "  (:types\n" + 
            "      package location vehicle city - object\n" + 
            "      truck airplane - vehicle\n" + 
            "      airport - location)\n" + 
            "\n" + 
            "  (:predicates\n" + 
            "        (at ?vehicle - vehicle  ?location - location)\n" + 
            "        (att ?package - package  ?location - location)\n" + 
            "        (in ?package - package ?vehicle - vehicle)\n" + 
            "        (in_city ?loc-or-truck - (either location truck) ?citys - city))\n" + 
            "\n" + 
            "  (:action load-truck\n" + 
            "    :parameters\n" + 
            "         (?obj - package\n" + 
            "          ?truck - truck\n" + 
            "          ?loc - location)\n" + 
            "    :precondition\n" + 
            "        (and    (at ?truck ?loc)\n" + 
            "            (att ?obj ?loc))\n" + 
            "    :effect\n" + 
            "        (and    (not (att ?obj ?loc))\n" + 
            "            (in ?obj ?truck)))\n" + 
            "\n" + 
            "  (:action load-airplane\n" + 
            "    :parameters\n" + 
            "        (?obj - package\n" + 
            "         ?airplane - airplane\n" + 
            "         ?loc - airport)\n" + 
            "    :precondition\n" + 
            "        (and\n" + 
            "            (att ?obj ?loc)\n" + 
            "            (at ?airplane ?loc))\n" + 
            "    :effect\n" + 
            "           (and     (not (att ?obj ?loc))\n" + 
            "            (in ?obj ?airplane)))\n" + 
            "\n" + 
            "  (:action unload-truck\n" + 
            "    :parameters\n" + 
            "        (?obj - package\n" + 
            "         ?truck - truck\n" + 
            "         ?loc - location)\n" + 
            "    :precondition\n" + 
            "        (and    (at ?truck ?loc)\n" + 
            "            (in ?obj ?truck))\n" + 
            "    :effect\n" + 
            "        (and    (not (in ?obj ?truck))\n" + 
            "            (att ?obj ?loc)))\n" + 
            "\n" + 
            "  (:action unload-airplane\n" + 
            "    :parameters\n" + 
            "        (?obj - package\n" + 
            "         ?airplane - airplane\n" + 
            "         ?loc - airport)\n" + 
            "    :precondition\n" + 
            "        (and    (in ?obj ?airplane)\n" + 
            "            (at ?airplane ?loc))\n" + 
            "    :effect\n" + 
            "        (and\n" + 
            "            (not (in ?obj ?airplane))\n" + 
            "            (att ?obj ?loc)))\n" + 
            "\n" + 
            "  (:action drive-truck\n" + 
            "    :parameters\n" + 
            "        (?truck - truck\n" + 
            "         ?loc-from - location\n" + 
            "         ?loc-to - location\n" + 
            "         ?city - city)\n" + 
            "    :precondition\n" + 
            "        (and    (at ?truck ?loc-from)\n" + 
            "            (in_city ?loc-from ?city)\n" + 
            "            (in_city ?loc-to ?city))\n" + 
            "    :effect\n" + 
            "        (and    (not (at ?truck ?loc-from))\n" + 
            "            (at ?truck ?loc-to)))\n" + 
            "\n" + 
            "  (:action fly-airplane\n" + 
            "    :parameters\n" + 
            "        (?airplane - airplane\n" + 
            "         ?loc-from - airport\n" + 
            "         ?loc-to - airport)\n" + 
            "    :precondition\n" + 
            "        (at ?airplane ?loc-from)\n" + 
            "    :effect\n" + 
            "        (and    (not (at ?airplane ?loc-from))\n" + 
            "        (at ?airplane ?loc-to)))\n" + 
            ")\n" + 
            "";

    private static String problemString = 
            "(define (problem logistics-ipc-fmap-01)\n" + 
            "(:domain logistics-ipc-fmap)\n" + 
            "(:requirements :strips :typing)\n" + 
            "(:objects \n" + 
            "    apn1 - airplane\n" + 
            "    tru1 tru2 - truck\n" + 
            "    apt1 apt2 - airport\n" + 
            "    pos1 pos2 - location\n" + 
            "    cit1 cit2 - city\n" + 
            "    obj23 obj22 obj21 obj13 obj12 obj11 - package)\n" + 
            "(:init (at apn1 apt2) (at tru1 pos1) (att obj11 pos1)\n" + 
            " (att obj12 pos1) (att obj13 pos1) (at tru2 pos2) (att obj21 pos2) (att obj22 pos2)\n" + 
            " (att obj23 pos2) (in_city pos1 cit1) (in_city apt1 cit1) (in_city pos2 cit2)\n" + 
            " (in_city apt2 cit2))\n" + 
            "(:goal (and (att obj11 apt1) (att obj23 pos1) (att obj13 apt1) (att obj21 pos1)))\n" + 
            ")\n" + 
            "";

    private static String addlString =
            "(define (problem logisitcs-ipc-fmap)\n" + 
            "    (:domain logistics-ipc-fmap)\n" + 
            "    (:agentTypes vehicle)\n" + 
            "    (:shared-data\n" + 
            "        (att ?package - package  ?location - location)\n" + 
            "        (in ?package - package ?vehicle - vehicle))\n" + 
            ")\n" + 
            "\n" + 
            "";

    @Test
    public void test() throws IOException {
        PddlDomain domain = new PddlDomain(new StringReader(domainString));
        PddlProblem problem = new PddlProblem(domain, new StringReader(problemString));
        PddlAddl addl = new PddlAddl(new StringReader(addlString));
        
        MaProjection projection = new FmapMaProjection(problem, addl);
        
        assertEquals(3, projection.getAgents().size());
        
        PddlProblem tru1Problem = projection.generateAgentProblem(new PddlName("tru1"));

        assertEquals(3, tru1Problem.domain.actions.size());
    }
    
    public static boolean hasNegativePrecondition(PddlDomain domain) {
    	for (PddlAction action : domain.actions) {
    		if (!action.precondition.negatives.isEmpty()) {
    			return true;
    		}
    	}
    	return false;
    }

}
