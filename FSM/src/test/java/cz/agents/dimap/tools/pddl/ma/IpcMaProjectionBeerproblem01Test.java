package cz.agents.dimap.tools.pddl.ma;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

import cz.agents.dimap.Settings;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class IpcMaProjectionBeerproblem01Test {

    @Before
    public void setup() {
        Settings.ALLOW_PRIVATE_GOALS = false;
    }

    private static String domainString =
            "(define (domain beerproblem)\n" + 
            "    (:requirements :strips :typing)\n" + 
            "    \n" + 
            "    (:types\n" + 
            "        vehicle city - object\n" + 
            "    )\n" + 
            "\n" + 
            "    (:predicates\n" + 
            "        (at \n" + 
            "            ?vehicle  - vehicle\n" + 
            "            ?city - city\n" + 
            "        )\n" + 
            "        (beer_in \n" + 
            "            ?in  - object\n" + 
            "        )\n" + 
            "        (path\n" + 
            "            ?vehicle  - vehicle\n" + 
            "            ?src - city\n" + 
            "            ?dst - city\n" + 
            "        )\n" + 
            "    )\n" + 
            "\n" + 
            "    (:action move\n" + 
            "        :parameters \n" + 
            "        (\n" + 
            "            ?veh - vehicle\n" + 
            "            ?src - city\n" + 
            "            ?dst - city\n" + 
            "        )\n" + 
            "        :precondition \n" + 
            "        (and \n" + 
            "            (at ?veh ?src)\n" + 
            "            (path ?veh ?src ?dst)\n" + 
            "        )\n" + 
            "        :effect \n" + 
            "        (and \n" + 
            "            (at ?veh ?dst)\n" + 
            "            (not (at ?veh ?src))\n" + 
            "        )\n" + 
            "    )\n" + 
            "\n" + 
            "    (:action load\n" + 
            "        :parameters \n" + 
            "        (\n" + 
            "            ?vehicle - vehicle\n" + 
            "            ?city - city\n" + 
            "        )\n" + 
            "        :precondition \n" + 
            "        (and\n" + 
            "            (beer_in ?city)\n" + 
            "            (at ?vehicle ?city)\n" + 
            "        )\n" + 
            "        :effect \n" + 
            "        (and\n" + 
            "            (beer_in ?vehicle)\n" + 
            "            (not (beer_in ?city))\n" + 
            "        )\n" + 
            "    )\n" + 
            "\n" + 
            "    (:action unload\n" + 
            "        :parameters \n" + 
            "        (\n" + 
            "            ?vehicle - vehicle\n" + 
            "            ?city - city\n" + 
            "        )\n" + 
            "        :precondition \n" + 
            "        (and\n" + 
            "            (beer_in ?vehicle)\n" + 
            "            (at ?vehicle ?city)\n" + 
            "        )\n" + 
            "        :effect \n" + 
            "        (and\n" + 
            "            (beer_in ?city)\n" + 
            "            (not (beer_in ?vehicle))\n" + 
            "        )\n" + 
            "    )\n" + 
            "\n" + 
            ")\n" + 
            "";

    private static String problemString = 
            "(define (problem beerproblem-01)\n" + 
            "    (:domain beerproblem)\n" + 
            "    (:requirements :strips :typing)\n" + 
            "    (:objects\n" + 
            "        plane - vehicle\n" + 
            "        truck - vehicle\n" + 
            "        pilsen - city\n" + 
            "        prague - city\n" + 
            "        ostrava - city\n" + 
            "    )\n" + 
            "    (:init\n" + 
            "        (at plane prague)\n" + 
            "        (at truck pilsen)\n" + 
            "        (beer_in pilsen)\n" + 
            "        (path plane prague ostrava) \n" + 
            "        (path plane ostrava prague) \n" + 
            "        (path truck prague pilsen) \n" + 
            "        (path truck pilsen prague)  \n" + 
            "    )\n" + 
            "    (:goal (and\n" + 
            "        (beer_in ostrava)\n" + 
            "    ))\n" + 
            ")\n" + 
            "";

    private static String addlString =
            "(define (problem beerproblem-generic)\n" + 
            "    (:domain beerdomain-generic)\n" + 
            "    (:agentTypes vehicle)\n" + 
            ")\n" + 
            "";

    @Test
    public void test() throws IOException {
        PddlDomain domain = new PddlDomain(new StringReader(domainString));
        PddlProblem problem = new PddlProblem(domain, new StringReader(problemString));
        PddlAddl addl = new PddlAddl(new StringReader(addlString));
        
        MaProjection projection = new IpcMaProjection(problem, addl);
        
        assertEquals(2, projection.getAgents().size());
        
        PddlProblem planeProblem = projection.generateAgentProblem(new PddlName("plane"));

        assertEquals("plane-domain", planeProblem.domainName.toString());
        assertEquals("plane-problem", planeProblem.problemName.toString());

        assertEquals(5, planeProblem.objects.getBindings().size());
        assertEquals(7, planeProblem.init.positives.size());
        assertEquals(0, planeProblem.init.negatives.size());
        assertEquals(1, planeProblem.goal.positives.size());
        assertEquals(0, planeProblem.goal.negatives.size());

        PddlDomain planeDomain = planeProblem.domain;
        assertEquals("beerproblem", domain.domainName.toString());
        
        assertTrue(planeDomain.requirements.toString(), planeDomain.requirements.contains(new PddlName(":typing")));
        assertEquals(5, planeDomain.actions.size());
        assertFalse(planeProblem.domainName.name+" contains negative precondition!", hasNegativePrecondition(planeDomain));
        assertEquals(3, planeDomain.predicateTypes.size());
        assertEquals(0, planeDomain.sharedPredicateNames.size());
        assertEquals(2, planeDomain.sharedPredicates.objects.size());

        PddlProblem truckProblem = projection.generateAgentProblem(new PddlName("truck"));

        assertEquals("truck-domain", truckProblem.domainName.toString());
        assertEquals("truck-problem", truckProblem.problemName.toString());

        assertEquals(5, truckProblem.objects.getBindings().size());
        assertEquals(7, truckProblem.init.positives.size());
        assertEquals(0, truckProblem.init.negatives.size());
        assertEquals(1, truckProblem.goal.positives.size());
        assertEquals(0, truckProblem.goal.negatives.size());

        PddlDomain truckDomain = truckProblem.domain;

        assertEquals("beerproblem", domain.domainName.toString());
        
        assertTrue(truckDomain.requirements.toString(), truckDomain.requirements.contains(new PddlName(":typing")));
        assertEquals(5, truckDomain.actions.size());
        assertFalse(truckProblem.domainName.name+" contains negative precondition!", hasNegativePrecondition(truckDomain));
        assertEquals(3, truckDomain.predicateTypes.size());
        assertEquals(0, truckDomain.sharedPredicateNames.size());
        assertEquals(2, planeDomain.sharedPredicates.objects.size());
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
