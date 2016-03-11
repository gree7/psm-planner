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

public class IpcMaProjectionSatellite03Test {

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
            "(define (problem satellites-ipc-fmap-a03)\n" + 
            "(:domain satellites-ipc-fmap)\n" + 
            "(:requirements :strips :typing)\n" + 
            "(:objects\n" + 
            "    satellite0 satellite1 - satellite\n" + 
            "    instrument0 instrument1 instrument2 instrument3 - instrument\n" + 
            "    image1 infrared0 spectrograph2 - mode\n" + 
            "    Star1 Star2 Star0 Star3 Star4 Phenomenon5 Phenomenon6 Phenomenon7 - direction\n" + 
            ")\n" + 
            "(:init\n" + 
            "    (supports instrument0 spectrograph2)\n" + 
            "    (supports instrument0 infrared0)\n" + 
            "    (calibration_target instrument0 Star1)\n" + 
            "    (supports instrument1 image1)\n" + 
            "    (calibration_target instrument1 Star2)\n" + 
            "    (supports instrument2 infrared0)\n" + 
            "    (supports instrument2 image1)\n" + 
            "    (calibration_target instrument2 Star0)\n" + 
            "    (on_board instrument0 satellite0)\n" + 
            "    (on_board instrument1 satellite0)\n" + 
            "    (on_board instrument2 satellite0)\n" + 
            "    (power_avail satellite0)\n" + 
            "    (pointing satellite0 Star4)\n" + 
            "    (supports instrument3 spectrograph2)\n" + 
            "    (supports instrument3 infrared0)\n" + 
            "    (supports instrument3 image1)\n" + 
            "    (calibration_target instrument3 Star0)\n" + 
            "    (on_board instrument3 satellite1)\n" + 
            "    (power_avail satellite1)\n" + 
            "    (pointing satellite1 Star0)\n" + 
            ")\n" + 
            "(:goal (and\n" + 
            "    (pointing satellite0 Phenomenon5)\n" + 
            "    (have_image Star3 infrared0)\n" + 
            "    (have_image Star4 spectrograph2)\n" + 
            "    (have_image Phenomenon5 spectrograph2)\n" + 
            "    (have_image Phenomenon7 spectrograph2)\n" + 
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
        
        MaProjection projection = new IpcMaProjection(problem, addl);
        
        assertEquals(2, projection.getAgents().size());
        
        PddlProblem satellite0Problem = projection.generateAgentProblem(new PddlName("satellite0"));

        assertEquals("satellite0-domain", satellite0Problem.domainName.toString());
        assertEquals("satellite0-problem", satellite0Problem.problemName.toString());

        assertEquals(17, satellite0Problem.objects.getBindings().size());
        assertEquals(20, satellite0Problem.init.positives.size());
        assertEquals(0, satellite0Problem.init.negatives.size());
        assertEquals(5, satellite0Problem.goal.positives.size());
        assertEquals(0, satellite0Problem.goal.negatives.size());

        PddlDomain satellite0Domain = satellite0Problem.domain;

        assertEquals("satellites-ipc-fmap", domain.domainName.toString());
        
        assertTrue(satellite0Domain.requirements.toString(), satellite0Domain.requirements.contains(new PddlName(":typing")));
        assertEquals(85, satellite0Domain.actions.size());
        assertFalse(satellite0Problem.domainName.name+" contains negative precondition!", hasNegativePrecondition(satellite0Domain));
        assertEquals(8, satellite0Domain.predicateTypes.size());
        assertEquals(0, satellite0Domain.sharedPredicateNames.size());
        assertEquals(25, satellite0Domain.sharedPredicates.objects.size());
        assertTrue(satellite0Domain.types.getBindings().toString(), satellite0Domain.types.getBindings().get(new PddlName("agent")).eithers.equals(Arrays.asList(new PddlName("object"))));
        assertTrue(satellite0Domain.types.getBindings().toString(), satellite0Domain.types.getBindings().get(new PddlName("satellite")).eithers.equals(Arrays.asList(new PddlName("agent"))));

        PddlProblem satellite1Problem = projection.generateAgentProblem(new PddlName("satellite1"));

        assertEquals("satellite1-domain", satellite1Problem.domainName.toString());
        assertEquals("satellite1-problem", satellite1Problem.problemName.toString());

        assertEquals(17, satellite1Problem.objects.getBindings().size());
        assertEquals(20, satellite1Problem.init.positives.size());
        assertEquals(0, satellite1Problem.init.negatives.size());
        assertEquals(5, satellite1Problem.goal.positives.size());
        assertEquals(0, satellite1Problem.goal.negatives.size());

        PddlDomain satellite1Domain = satellite1Problem.domain;

        assertEquals("satellites-ipc-fmap", domain.domainName.toString());
        
        assertTrue(satellite1Domain.requirements.toString(), satellite1Domain.requirements.contains(new PddlName(":typing")));
        assertEquals(28, satellite1Domain.actions.size());
        assertFalse(satellite1Problem.domainName.name+" contains negative precondition!", hasNegativePrecondition(satellite1Domain));
        assertEquals(8, satellite1Domain.predicateTypes.size());
        assertEquals(0, satellite1Domain.sharedPredicateNames.size());
        assertEquals(25, satellite0Domain.sharedPredicates.objects.size());
        assertTrue(satellite1Domain.types.getBindings().toString(), satellite1Domain.types.getBindings().get(new PddlName("agent")).eithers.equals(Arrays.asList(new PddlName("object"))));
        assertTrue(satellite1Domain.types.getBindings().toString(), satellite1Domain.types.getBindings().get(new PddlName("satellite")).eithers.equals(Arrays.asList(new PddlName("agent"))));
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
