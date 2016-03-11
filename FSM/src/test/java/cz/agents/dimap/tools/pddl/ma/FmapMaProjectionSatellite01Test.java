package cz.agents.dimap.tools.pddl.ma;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import org.junit.Test;

import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class FmapMaProjectionSatellite01Test {

    private static String domainString =
            "(define (domain satellites-ipc-fmap)\n" + 
            "  (:requirements :strips :typing)\n" + 
            "  (:types \n" + 
            "   satellite - object\n" + 
            "   direction - object\n" + 
            "   instrument - object\n" + 
            "   mode - object)\n" + 
            " (:predicates \n" + 
            "               (on_board ?i - instrument ?s - satellite)\n" + 
            "           (supports ?i - instrument ?m - mode)\n" + 
            "           (pointing ?s - satellite ?d - direction)\n" + 
            "           (power_avail ?s - satellite)\n" + 
            "           (power_on ?i - instrument)\n" + 
            "           (calibrated ?i - instrument)\n" + 
            "           (have_image ?d - direction ?m - mode)\n" + 
            "           (calibration_target ?i - instrument ?d - direction))\n" + 
            " \n" + 
            " \n" + 
            "\n" + 
            "  (:action turn_to\n" + 
            "   :parameters (?s - satellite ?d_new - direction ?d_prev - direction)\n" + 
            "   :precondition (and (pointing ?s ?d_prev))\n" + 
            "   :effect (and  (pointing ?s ?d_new)\n" + 
            "                 (not (pointing ?s ?d_prev))\n" + 
            "           )\n" + 
            "  )\n" + 
            "\n" + 
            " \n" + 
            "  (:action switch_on\n" + 
            "   :parameters (?i - instrument ?s - satellite)\n" + 
            " \n" + 
            "   :precondition (and (on_board ?i ?s) \n" + 
            "                      (power_avail ?s)\n" + 
            "                 )\n" + 
            "   :effect (and (power_on ?i)\n" + 
            "                (not (calibrated ?i))\n" + 
            "                (not (power_avail ?s))\n" + 
            "           )\n" + 
            "          \n" + 
            "  )\n" + 
            "\n" + 
            " \n" + 
            "  (:action switch_off\n" + 
            "   :parameters (?i - instrument ?s - satellite)\n" + 
            " \n" + 
            "   :precondition (and (on_board ?i ?s)\n" + 
            "                      (power_on ?i) \n" + 
            "                  )\n" + 
            "   :effect (and (not (power_on ?i))\n" + 
            "                (power_avail ?s)\n" + 
            "           )\n" + 
            "  )\n" + 
            "\n" + 
            "  (:action calibrate\n" + 
            "   :parameters (?s - satellite ?i - instrument ?d - direction)\n" + 
            "   :precondition (and (on_board ?i ?s)\n" + 
            "              (calibration_target ?i ?d)\n" + 
            "                      (pointing ?s ?d)\n" + 
            "                      (power_on ?i)\n" + 
            "                  )\n" + 
            "   :effect (calibrated ?i)\n" + 
            "  )\n" + 
            "\n" + 
            "\n" + 
            "  (:action take_image\n" + 
            "   :parameters (?s - satellite ?d - direction ?i - instrument ?m - mode)\n" + 
            "   :precondition (and (calibrated ?i)\n" + 
            "                      (on_board ?i ?s)\n" + 
            "                      (supports ?i ?m)\n" + 
            "                      (power_on ?i)\n" + 
            "                      (pointing ?s ?d)\n" + 
            "                      (power_on ?i)\n" + 
            "               )\n" + 
            "   :effect (have_image ?d ?m)\n" + 
            "  )\n" + 
            ")\n" + 
            "\n" + 
            "";

    private static String problemString = 
            "(define (problem satellites-ipc-fmap-a01)\n" + 
            "(:domain satellites-ipc-fmap)\n" + 
            "(:requirements :strips :typing)\n" + 
            "(:objects\n" + 
            "    satellite0 - satellite\n" + 
            "    instrument0 - instrument\n" + 
            "    image1 spectrograph2 thermograph0 - mode\n" + 
            "    Star0 GroundStation1 GroundStation2 Phenomenon3 Phenomenon4 Star5 Phenomenon6 - direction\n" + 
            ")\n" + 
            "(:init\n" + 
            "    (supports instrument0 thermograph0)\n" + 
            "    (calibration_target instrument0 GroundStation2)\n" + 
            "    (on_board instrument0 satellite0)\n" + 
            "    (power_avail satellite0)\n" + 
            "    (pointing satellite0 Phenomenon6)\n" + 
            ")\n" + 
            "(:goal (and\n" + 
            "    (have_image Phenomenon4 thermograph0)\n" + 
            "    (have_image Star5 thermograph0)\n" + 
            "    (have_image Phenomenon6 thermograph0)\n" + 
            "))\n" + 
            "\n" + 
            ")\n" + 
            "";

    private static String addlString =
            "(define (problem satellites-ipc-fmap)\n" + 
            "    (:domain satellites-ipc-fmap)\n" + 
            "    (:agentTypes satellite)\n" + 
            "    (:shared-data\n" + 
            "    (pointing ?s - satellite ?d - direction)\n" + 
            "    (have_image ?d - direction ?m - mode))\n" + 
            ")\n" + 
            "";

    @Test
    public void test() throws IOException {
        PddlDomain domain = new PddlDomain(new StringReader(domainString));
        PddlProblem problem = new PddlProblem(domain, new StringReader(problemString));
        PddlAddl addl = new PddlAddl(new StringReader(addlString));
        
        MaProjection projection = new FmapMaProjection(problem, addl);
        
        assertEquals(1, projection.getAgents().size());
        
        PddlProblem satellite0Problem = projection.generateAgentProblem(new PddlName("satellite0"));

        assertEquals("satellite0-domain", satellite0Problem.domainName.toString());
        assertEquals("satellite0-problem", satellite0Problem.problemName.toString());

        assertEquals(12, satellite0Problem.objects.getBindings().size());
        assertEquals(5, satellite0Problem.init.positives.size());
        assertEquals(0, satellite0Problem.init.negatives.size());
        assertEquals(3, satellite0Problem.goal.positives.size());
        assertEquals(0, satellite0Problem.goal.negatives.size());

        PddlDomain satellite0Domain = satellite0Problem.domain;

        assertEquals("satellites-ipc-fmap", domain.domainName.toString());
        
        assertTrue(satellite0Domain.requirements.toString(), satellite0Domain.requirements.contains(new PddlName(":typing")));
        assertEquals(5, satellite0Domain.actions.size());
        assertFalse(satellite0Problem.domainName.name+" contains negative precondition!", hasNegativePrecondition(satellite0Domain));
        assertEquals(8, satellite0Domain.predicateTypes.size());
        assertEquals(2, satellite0Domain.sharedPredicateNames.size());
        assertTrue(satellite0Domain.types.getBindings().toString(), satellite0Domain.types.getBindings().get(new PddlName("agent")).eithers.equals(Arrays.asList(new PddlName("object"))));
        assertTrue(satellite0Domain.types.getBindings().toString(), satellite0Domain.types.getBindings().get(new PddlName("satellite")).eithers.equals(Arrays.asList(new PddlName("agent"))));
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
