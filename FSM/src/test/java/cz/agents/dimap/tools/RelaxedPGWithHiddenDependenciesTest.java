package cz.agents.dimap.tools;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import cz.agents.dimap.Settings;
import cz.agents.dimap.data.BeerFmap;
import cz.agents.dimap.data.Elevators01Fmap;
import cz.agents.dimap.data.PddlTestProblem;
import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.RelaxedPGWithHiddenDependencies.Stats;
import cz.agents.dimap.tools.pddl.PddlProblem;
import experiments.psm.fmap.MaStripsMultiAgentProblem;

public class RelaxedPGWithHiddenDependenciesTest {

    @Before
    public void setup() {
        Settings.USE_MASTRIPS = true;
        Settings.MASTRIPS_PUBLIC_FACT_HAS_TO_BE_IN_EFFECT = true;
        Settings.ALLOW_PRIVATE_GOALS = false;
    }
    
    @Test
    public void testElevatorsMaStrips() throws Exception {
        MultiAgentProblem fmapMaProblem = Elevators01Fmap.getProblem();
        MultiAgentProblem maProblem = new MaStripsMultiAgentProblem(fmapMaProblem);
        PddlProblem problem = maProblem.toSingleAgentProblem();

        Stats stats = RelaxedPGWithHiddenDependencies.extractReachableActionsStats(problem); 
        
        assertEquals(166, stats.numOfPublicActions);
        assertEquals(116, stats.numOfRiskyPublicActions);
        assertEquals(50, stats.numOfPotentiallyRiskyPublicActions);
    }

    @Test
    public void testBeerMaStrips() throws Exception {
        MultiAgentProblem fmapMaProblem = BeerFmap.getProblem();
        MultiAgentProblem maProblem = new MaStripsMultiAgentProblem(fmapMaProblem);

        PddlProblem problem = maProblem.toSingleAgentProblem();

        Stats stats = RelaxedPGWithHiddenDependencies.extractReachableActionsStats(problem); 

        assertEquals(6, stats.numOfPublicActions);
        assertEquals(2, stats.numOfRiskyPublicActions);
        assertEquals(1, stats.numOfPotentiallyRiskyPublicActions);
    }

    @Test
    public void testBeerFmap() throws Exception {
        MultiAgentProblem maProblem = BeerFmap.getProblem();

        PddlProblem problem = maProblem.toSingleAgentProblem();

        Stats stats = RelaxedPGWithHiddenDependencies.extractReachableActionsStats(problem); 

        assertEquals(8, stats.numOfPublicActions);
        assertEquals(4, stats.numOfRiskyPublicActions);
        assertEquals(0, stats.numOfPotentiallyRiskyPublicActions);
    }


    @Test
    public void testSimpleRiskyProblem() throws Exception {
        PddlProblem problem = SimpleRiskyProblem.getProblem();

        Stats stats = RelaxedPGWithHiddenDependencies.extractReachableActionsStats(problem); 
        
        assertEquals(1, stats.numOfRiskyPublicActions);
        assertEquals(0, stats.numOfPotentiallyRiskyPublicActions);
    }

    @Test
    public void SimplePotentiallyRiskyProblem() throws Exception {
        PddlProblem problem = SimplePotentiallyRiskyProblem.getProblem();

        Stats stats = RelaxedPGWithHiddenDependencies.extractReachableActionsStats(problem); 
        
        assertEquals(0, stats.numOfRiskyPublicActions);
        assertEquals(1, stats.numOfPotentiallyRiskyPublicActions);
    }

    @Test
    public void testOneInternalActionProblem() throws Exception {
        PddlProblem problem = OneInternalActionProblem.getProblem();

        Stats stats = RelaxedPGWithHiddenDependencies.extractReachableActionsStats(problem); 
        
        assertEquals(1, stats.numOfRiskyPublicActions);
        assertEquals(0, stats.numOfPotentiallyRiskyPublicActions);
    }
}

class SimpleRiskyProblem {
    public static PddlProblem getProblem() {
        return PddlTestProblem.createProblem(domain, problem, true);
    }

    private static String domain = "(define (domain testproblem)\n" + 
            "    (:requirements :strips :typing)\n" + 
            "    \n" + 
            "\n" + 
            "    (:predicates\n" +
            "        (start)\n" + 
            "        (public_1)\n" + 
            "        (priv_1)\n" + 
            "        (public_2)\n" + 
            "    )\n" + 
            "\n" + 
            "    (:shared-predicate-names public_1 public_2 )" + 
            "\n" + 
            "    (:action public_action1\n" + 
            "        :parameters ()\n" + 
            "        :precondition (start)\n" + 
            "        :effect \n" + 
            "        (and\n" + 
            "            (priv_1)\n" + 
            "            (public_1)\n" + 
            "            (not (start))\n" + 
            "        )\n" + 
            "    )\n" + 
            "\n" + 
            "    (:action public_action2\n" + 
            "        :parameters ()\n" + 
            "        :precondition (priv_1)\n" + 
            "        :effect \n" + 
            "        (and\n" + 
            "            (public_2)\n" + 
            "            (not (priv_1))\n" + 
            "        )\n" + 
            "    )\n" + 
            "\n" +
            ")\n" + 
            "";

    private static String problem = "(define (problem testproblem)\n" + 
            "    (:domain testproblem)\n" + 
            "    (:requirements :strips :typing)\n" + 
            "    (:init\n" + 
            "        (start)\n" + 
            "    )\n" + 
            "    (:goal (and\n" + 
            "        (public_2)\n" + 
            "    ))\n" + 
            ")\n" + 
            "";

}

class SimplePotentiallyRiskyProblem {
    public static PddlProblem getProblem() {
        return PddlTestProblem.createProblem(domain, problem, true);
    }

    private static String domain = "(define (domain testproblem)\n" + 
            "    (:requirements :strips :typing)\n" + 
            "    \n" + 
            "\n" + 
            "    (:predicates\n" +
            "        (start)\n" + 
            "        (public_1)\n" + 
            "        (priv_1)\n" + 
            "        (public_2)\n" + 
            "    )\n" + 
            "\n" + 
            "    (:shared-predicate-names public_1 public_2 )" + 
            "\n" + 
            "    (:action public_action1\n" + 
            "        :parameters ()\n" + 
            "        :precondition (start)\n" + 
            "        :effect \n" + 
            "        (and\n" + 
            "            (priv_1)\n" + 
            "            (public_1)\n" + 
            "            (not (start))\n" + 
            "        )\n" + 
            "    )\n" + 
            "\n" + 
            "    (:action priv_action1\n" + 
            "        :parameters ()\n" + 
            "        :precondition ()\n" + 
            "        :effect \n" + 
            "        (and\n" + 
            "            (priv_1)\n" + 
            "        )\n" + 
            "    )\n" + 
            "\n" + 
            "    (:action public_action2\n" + 
            "        :parameters ()\n" + 
            "        :precondition (priv_1)\n" + 
            "        :effect \n" + 
            "        (and\n" + 
            "            (public_2)\n" + 
            "            (not (priv_1))\n" + 
            "        )\n" + 
            "    )\n" + 
            "\n" +
            ")\n" + 
            "";

    private static String problem = "(define (problem testproblem)\n" + 
            "    (:domain testproblem)\n" + 
            "    (:requirements :strips :typing)\n" + 
            "    (:init\n" + 
            "        (start)\n" + 
            "    )\n" + 
            "    (:goal (and\n" + 
            "        (public_2)\n" + 
            "    ))\n" + 
            ")\n" + 
            "";

}

class OneInternalActionProblem {
    public static PddlProblem getProblem() {
        return PddlTestProblem.createProblem(domain, problem, true);
    }

    private static String domain = "(define (domain testproblem)\n" + 
            "    (:requirements :strips :typing)\n" + 
            "    \n" + 
            "\n" + 
            "    (:predicates\n" +
            "        (start)\n" + 
            "        (public_1)\n" + 
            "        (priv_1)\n" + 
            "        (priv_2)\n" + 
            "        (public_2)\n" + 
            "    )\n" + 
            "\n" + 
            "    (:shared-predicate-names public_1 public_2 )" + 
            "    (:action public_action1\n" + 
            "        :parameters ()\n" + 
            "        :precondition (start)\n" + 
            "        :effect \n" + 
            "        (and\n" + 
            "            (public_1)\n" + 
            "            (priv_1)\n" + 
            "            (not (start))\n" + 
            "        )\n" + 
            "    )\n" + 
            "\n" + 
            "    (:action priv_action1\n" + 
            "        :parameters ()\n" + 
            "        :precondition (priv_1)\n" + 
            "        :effect \n" + 
            "        (and\n" + 
            "            (priv_2)\n" + 
            "        )\n" + 
            "    )\n" + 
            "\n" + 
            "    (:action public_action2\n" + 
            "        :parameters ()\n" + 
            "        :precondition " +
            "        (and\n" + 
            "            (public_1)\n" + 
            "            (priv_2)\n" + 
            "        )\n" + 
            "        :effect \n" + 
            "        (and\n" + 
            "            (public_2)\n" + 
            "        )\n" + 
            "    )\n" + 
            "\n" +
            ")\n" + 
            "";

    private static String problem = "(define (problem testproblem)\n" + 
            "    (:domain testproblem)\n" + 
            "    (:requirements :strips :typing)\n" + 
            "    (:init\n" + 
            "        (start)\n" + 
            "    )\n" + 
            "    (:goal (and\n" + 
            "        (public_2)\n" + 
            "    ))\n" + 
            ")\n" + 
            "";

}

