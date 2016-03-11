package cz.agents.dimap.tools.pddl.ma;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import cz.agents.dimap.Settings;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class IpcMaProjectionRovers03Test {

    private static String domainString =
            "(define (domain Rovers-ipc)\n" + 
            "(:requirements :typing)\n" + 
            "(:types rover waypoint store camera mode lander objective)\n" + 
            "\n" + 
            "(:predicates (at ?x - rover ?y - waypoint) \n" + 
            "             (at_lander ?x - lander ?y - waypoint)\n" + 
            "             (can_traverse ?r - rover ?x - waypoint ?y - waypoint)\n" + 
            "         (equipped_for_soil_analysis ?r - rover)\n" + 
            "             (equipped_for_rock_analysis ?r - rover)\n" + 
            "             (equipped_for_imaging ?r - rover)\n" + 
            "             (empty ?s - store)\n" + 
            "             (have_rock_analysis ?r - rover ?w - waypoint)\n" + 
            "             (have_soil_analysis ?r - rover ?w - waypoint)\n" + 
            "             (full ?s - store)\n" + 
            "         (calibrated ?c - camera ?r - rover) \n" + 
            "         (supports ?c - camera ?m - mode)\n" + 
            "             (available ?r - rover)\n" + 
            "             (visible ?w - waypoint ?p - waypoint)\n" + 
            "             (have_image ?r - rover ?o - objective ?m - mode)\n" + 
            "             (communicated_soil_data ?w - waypoint)\n" + 
            "             (communicated_rock_data ?w - waypoint)\n" + 
            "             (communicated_image_data ?o - objective ?m - mode)\n" + 
            "         (at_soil_sample ?w - waypoint)\n" + 
            "         (at_rock_sample ?w - waypoint)\n" + 
            "             (visible_from ?o - objective ?w - waypoint)\n" + 
            "         (store_of ?s - store ?r - rover)\n" + 
            "         (calibration_target ?i - camera ?o - objective)\n" + 
            "         (on_board ?i - camera ?r - rover)\n" + 
            "         (channel_free ?l - lander)\n" + 
            "\n" + 
            ")\n" + 
            "\n" + 
            "    \n" + 
            "(:action navigate\n" + 
            ":parameters (?x - rover ?y - waypoint ?z - waypoint) \n" + 
            ":precondition (and (can_traverse ?x ?y ?z) (available ?x) (at ?x ?y) \n" + 
            "                (visible ?y ?z)\n" + 
            "        )\n" + 
            ":effect (and (not (at ?x ?y)) (at ?x ?z)\n" + 
            "        )\n" + 
            ")\n" + 
            "\n" + 
            "(:action sample_soil\n" + 
            ":parameters (?x - rover ?s - store ?p - waypoint)\n" + 
            ":precondition (and (at ?x ?p) (at_soil_sample ?p) (equipped_for_soil_analysis ?x) (store_of ?s ?x) (empty ?s)\n" + 
            "        )\n" + 
            ":effect (and (not (empty ?s)) (full ?s) (have_soil_analysis ?x ?p) (not (at_soil_sample ?p))\n" + 
            "        )\n" + 
            ")\n" + 
            "\n" + 
            "(:action sample_rock\n" + 
            ":parameters (?x - rover ?s - store ?p - waypoint)\n" + 
            ":precondition (and (at ?x ?p) (at_rock_sample ?p) (equipped_for_rock_analysis ?x) (store_of ?s ?x)(empty ?s)\n" + 
            "        )\n" + 
            ":effect (and (not (empty ?s)) (full ?s) (have_rock_analysis ?x ?p) (not (at_rock_sample ?p))\n" + 
            "        )\n" + 
            ")\n" + 
            "\n" + 
            "(:action drop\n" + 
            ":parameters (?x - rover ?y - store)\n" + 
            ":precondition (and (store_of ?y ?x) (full ?y)\n" + 
            "        )\n" + 
            ":effect (and (not (full ?y)) (empty ?y)\n" + 
            "    )\n" + 
            ")\n" + 
            "\n" + 
            "(:action calibrate\n" + 
            " :parameters (?r - rover ?i - camera ?t - objective ?w - waypoint)\n" + 
            " :precondition (and (equipped_for_imaging ?r) (calibration_target ?i ?t) (at ?r ?w) (visible_from ?t ?w)(on_board ?i ?r)\n" + 
            "        )\n" + 
            " :effect (calibrated ?i ?r) \n" + 
            ")\n" + 
            "\n" + 
            "\n" + 
            "\n" + 
            "\n" + 
            "(:action take_image\n" + 
            " :parameters (?r - rover ?p - waypoint ?o - objective ?i - camera ?m - mode)\n" + 
            " :precondition (and (calibrated ?i ?r)\n" + 
            "             (on_board ?i ?r)\n" + 
            "                      (equipped_for_imaging ?r)\n" + 
            "                      (supports ?i ?m)\n" + 
            "              (visible_from ?o ?p)\n" + 
            "                     (at ?r ?p)\n" + 
            "               )\n" + 
            " :effect (and (have_image ?r ?o ?m)(not (calibrated ?i ?r))\n" + 
            "        )\n" + 
            ")\n" + 
            "\n" + 
            "\n" + 
            "(:action communicate_soil_data\n" + 
            " :parameters (?r - rover ?l - lander ?p - waypoint ?x - waypoint ?y - waypoint)\n" + 
            " :precondition (and (at ?r ?x)(at_lander ?l ?y)(have_soil_analysis ?r ?p) \n" + 
            "                   (visible ?x ?y)(available ?r)(channel_free ?l)\n" + 
            "            )\n" + 
            " :effect (and (not (available ?r))(not (channel_free ?l))(channel_free ?l)\n" + 
            "        (communicated_soil_data ?p)(available ?r)\n" + 
            "    )\n" + 
            ")\n" + 
            "\n" + 
            "(:action communicate_rock_data\n" + 
            " :parameters (?r - rover ?l - lander ?p - waypoint ?x - waypoint ?y - waypoint)\n" + 
            " :precondition (and (at ?r ?x)(at_lander ?l ?y)(have_rock_analysis ?r ?p)\n" + 
            "                   (visible ?x ?y)(available ?r)(channel_free ?l)\n" + 
            "            )\n" + 
            " :effect (and (not (available ?r))(not (channel_free ?l))(channel_free ?l)(communicated_rock_data ?p)(available ?r)\n" + 
            "          )\n" + 
            ")\n" + 
            "\n" + 
            "\n" + 
            "(:action communicate_image_data\n" + 
            " :parameters (?r - rover ?l - lander ?o - objective ?m - mode ?x - waypoint ?y - waypoint)\n" + 
            " :precondition (and (at ?r ?x)(at_lander ?l ?y)(have_image ?r ?o ?m)(visible ?x ?y)(available ?r)(channel_free ?l)\n" + 
            "            )\n" + 
            " :effect (and (not (available ?r))(not (channel_free ?l))(channel_free ?l)(communicated_image_data ?o ?m)(available ?r)\n" + 
            "          )\n" + 
            ")\n" + 
            "\n" + 
            ")\n" + 
            "";

    private static String problemString = 
            "(define (problem rovers-ipc-p03)\n" + 
            "(:domain Rovers-ipc)\n" + 
            "(:requirements :typing)\n" + 
            "(:objects\n" + 
            "    general - Lander\n" + 
            "    colour high_res low_res - Mode\n" + 
            "    rover0 rover1 - Rover\n" + 
            "    rover0store rover1store - Store\n" + 
            "    waypoint0 waypoint1 waypoint2 waypoint3 - Waypoint\n" + 
            "    camera0 camera1 - Camera\n" + 
            "    objective0 objective1 - Objective\n" + 
            "    )\n" + 
            "(:init\n" + 
            "    (visible waypoint0 waypoint1)\n" + 
            "    (visible waypoint1 waypoint0)\n" + 
            "    (visible waypoint0 waypoint3)\n" + 
            "    (visible waypoint3 waypoint0)\n" + 
            "    (visible waypoint1 waypoint2)\n" + 
            "    (visible waypoint2 waypoint1)\n" + 
            "    (visible waypoint1 waypoint3)\n" + 
            "    (visible waypoint3 waypoint1)\n" + 
            "    (visible waypoint2 waypoint0)\n" + 
            "    (visible waypoint0 waypoint2)\n" + 
            "    (visible waypoint3 waypoint2)\n" + 
            "    (visible waypoint2 waypoint3)\n" + 
            "    (at_rock_sample waypoint0)\n" + 
            "    (at_rock_sample waypoint1)\n" + 
            "    (at_soil_sample waypoint2)\n" + 
            "    (at_rock_sample waypoint2)\n" + 
            "    (at_lander general waypoint0)\n" + 
            "    (channel_free general)\n" + 
            "    (at rover0 waypoint1)\n" + 
            "    (available rover0)\n" + 
            "    (store_of rover0store rover0)\n" + 
            "    (empty rover0store)\n" + 
            "    (equipped_for_soil_analysis rover0)\n" + 
            "    (equipped_for_rock_analysis rover0)\n" + 
            "    (equipped_for_imaging rover0)\n" + 
            "    (can_traverse rover0 waypoint1 waypoint0)\n" + 
            "    (can_traverse rover0 waypoint0 waypoint1)\n" + 
            "    (can_traverse rover0 waypoint1 waypoint3)\n" + 
            "    (can_traverse rover0 waypoint3 waypoint1)\n" + 
            "    (at rover1 waypoint3)\n" + 
            "    (available rover1)\n" + 
            "    (store_of rover1store rover1)\n" + 
            "    (empty rover1store)\n" + 
            "    (equipped_for_soil_analysis rover1)\n" + 
            "    (equipped_for_rock_analysis rover1)\n" + 
            "    (equipped_for_imaging rover1)\n" + 
            "    (can_traverse rover1 waypoint3 waypoint0)\n" + 
            "    (can_traverse rover1 waypoint0 waypoint3)\n" + 
            "    (can_traverse rover1 waypoint3 waypoint2)\n" + 
            "    (can_traverse rover1 waypoint2 waypoint3)\n" + 
            "    (can_traverse rover1 waypoint0 waypoint1)\n" + 
            "    (can_traverse rover1 waypoint1 waypoint0)\n" + 
            "    (on_board camera0 rover0)\n" + 
            "    (calibration_target camera0 objective1)\n" + 
            "    (supports camera0 low_res)\n" + 
            "    (on_board camera1 rover1)\n" + 
            "    (calibration_target camera1 objective0)\n" + 
            "    (supports camera1 colour)\n" + 
            "    (supports camera1 high_res)\n" + 
            "    (supports camera1 low_res)\n" + 
            "    (visible_from objective0 waypoint0)\n" + 
            "    (visible_from objective0 waypoint1)\n" + 
            "    (visible_from objective1 waypoint0)\n" + 
            "    (visible_from objective1 waypoint1)\n" + 
            ")\n" + 
            "\n" + 
            "(:goal (and\n" + 
            "(communicated_soil_data waypoint2)\n" + 
            "(communicated_rock_data waypoint0)\n" + 
            "(communicated_image_data objective0 colour)\n" + 
            "    )\n" + 
            ")\n" + 
            ")\n" + 
            "";

    private static String addlString =
            "(define (problem rovers-ipc)\n" + 
            "    (:domain Rovers-ipc)\n" + 
            "    (:agentTypes Rover)\n" + 
            ")\n" + 
            "";
    
    @Before
    public void setup() {
        Settings.ALLOW_PRIVATE_GOALS = false;
        Settings.MASTRIPS_PUBLIC_FACT_HAS_TO_BE_IN_EFFECT = false;
    }

    @Test
    public void test() throws IOException {
        PddlDomain domain = new PddlDomain(new StringReader(domainString));
        PddlProblem problem = new PddlProblem(domain, new StringReader(problemString));
        PddlAddl addl = new PddlAddl(new StringReader(addlString));
        
        MaProjection projection = new IpcMaProjection(problem, addl);
        
        assertEquals(2, projection.getAgents().size());
        
        PddlProblem rover0Problem = projection.generateAgentProblem(new PddlName("rover0"));
        assertEquals("rover0-domain", rover0Problem.domainName.toString());
        assertEquals("rover0-problem", rover0Problem.problemName.toString());

        assertEquals(16, rover0Problem.objects.getBindings().size());
        String rover0initString = rover0Problem.init.toString();
        assertTrue(rover0initString.contains("(at rover0 waypoint1)"));
//        assertFalse(rover0initString.contains("(at rover1 waypoint3)"));
        assertTrue(rover0initString.contains("(empty rover0store)"));
//        assertFalse(rover0initString.contains("(empty rover1store)"));
        assertEquals(54, rover0Problem.init.positives.size());
        assertEquals(0, rover0Problem.init.negatives.size());
        assertEquals(3, rover0Problem.goal.positives.size());
        assertEquals(0, rover0Problem.goal.negatives.size());

        PddlDomain rover0Domain = rover0Problem.domain;

        assertEquals("rovers-ipc", domain.domainName.toString());
        assertTrue(rover0Domain.requirements.toString(), rover0Domain.requirements.contains(new PddlName(":typing")));
        assertEquals(24, rover0Domain.actions.size());
        assertFalse(rover0Problem.domainName.name+" contains negative precondition!", hasNegativePrecondition(rover0Domain));
        assertEquals(25, rover0Domain.predicateTypes.size());
        assertEquals(0, rover0Domain.sharedPredicateNames.size());
        assertEquals(17, rover0Domain.sharedPredicates.objects.size());
        assertTrue(rover0Domain.types.getBindings().toString(), rover0Domain.types.getBindings().get(new PddlName("agent")).eithers.equals(Arrays.asList(new PddlName("object"))));
        assertTrue(rover0Domain.types.getBindings().toString(), rover0Domain.types.getBindings().get(new PddlName("rover")).eithers.equals(Arrays.asList(new PddlName("agent"))));

        PddlProblem rover1Problem = projection.generateAgentProblem(new PddlName("rover1"));

        assertEquals("rover1-domain", rover1Problem.domainName.toString());
        assertEquals("rover1-problem", rover1Problem.problemName.toString());

        assertEquals(16, rover1Problem.objects.getBindings().size());
        String rover1initString = rover1Problem.init.toString();
//        assertFalse(rover1initString.contains("(at rover0 waypoint1)"));
        assertTrue(rover1initString.contains("(at rover1 waypoint3)"));
        assertEquals(54, rover1Problem.init.positives.size());
        assertEquals(0, rover1Problem.init.negatives.size());
        assertEquals(3, rover1Problem.goal.positives.size());
        assertEquals(0, rover1Problem.goal.negatives.size());

        PddlDomain rover1Domain = rover1Problem.domain;

        assertEquals("rovers-ipc", domain.domainName.toString());
        
        assertTrue(rover1Domain.requirements.toString(), rover1Domain.requirements.contains(new PddlName(":typing")));
        assertEquals(49, rover1Domain.actions.size());
        assertFalse(rover1Problem.domainName.name+" contains negative precondition!", hasNegativePrecondition(rover1Domain));
        assertEquals(25, rover1Domain.predicateTypes.size());
        assertEquals(0, rover1Domain.sharedPredicateNames.size());
        assertEquals(17, rover0Domain.sharedPredicates.objects.size());
        assertTrue(rover1Domain.types.getBindings().toString(), rover1Domain.types.getBindings().get(new PddlName("agent")).eithers.equals(Arrays.asList(new PddlName("object"))));
        assertTrue(rover1Domain.types.getBindings().toString(), rover1Domain.types.getBindings().get(new PddlName("rover")).eithers.equals(Arrays.asList(new PddlName("agent"))));
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
