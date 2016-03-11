package cz.agents.dimap.data;

import java.util.HashMap;
import java.util.Map;

import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.psm.planner.PddlMultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlProblem;

/**
 * Simplified satellites-03-fmap
 */

public class SatellitesIpc03NotGroundedSimple extends PddlTestProblem {
    public static MultiAgentProblem createProblem() {
        Map<String, PddlProblem> problems = new HashMap<String, PddlProblem>();
        problems.put("satellite0", createProblem(domainSatellite, problemSatellite0));
        problems.put("satellite1", createProblem(domainSatellite, problemSatellite1));
        
        return new PddlMultiAgentProblem(problems);
    }

    private static String domainSatellite = "(define (domain satellite)\n" + 
            "(:requirements :typing)\n" + 
            "(:types agent direction instrument mode - object\n" + 
            "        satellite - agent)\n" + 
            "(:predicates\n" + 
            "  (power_avail ?s - satellite)\n" + 
            "  (power_on ?i - instrument)\n" + 
            "  (calibrated ?i - instrument)\n" + 
            "  (have_image ?d - direction ?m - mode)\n" + 
            "  (pointing ?s - satellite ?d - direction)\n" + 
            "  (calibration_target ?i - instrument ?d - direction)\n" + 
            "  (on_board ?s - satellite ?i - instrument)\n" + 
            "  (supports ?i - instrument ?m - mode))\n" + 
            "(:shared-predicate-names have_image)\n" + 
            "(:action turn_to\n" + 
            " :parameters (?s - satellite ?d_new - direction ?d_prev - direction)\n" + 
            " :precondition (and (pointing ?s ?d_prev))\n" + 
            " :effect (and (pointing ?s ?d_new) (not (pointing ?s ?d_prev))))\n" + 
            "(:action switch_on\n" + 
            " :parameters (?i - instrument ?s - satellite)\n" + 
            " :precondition (and (on_board ?s ?i) (power_avail ?s))\n" + 
            " :effect (and (power_on ?i) (not (calibrated ?i)) (not (power_avail ?s))))\n" + 
            "(:action switch_off\n" + 
            " :parameters (?i - instrument ?s - satellite)\n" + 
            " :precondition (and (on_board ?s ?i) (power_on ?i))\n" + 
            " :effect (and (not (power_on ?i)) (power_avail ?s)))\n" + 
            "(:action calibrate\n" + 
            " :parameters (?s - satellite ?i - instrument ?d - direction)\n" + 
            " :precondition (and (on_board ?s ?i) (pointing ?s ?d)\n" + 
            "                 (calibration_target ?i ?d) (power_on ?i))\n" + 
            " :effect (calibrated ?i))\n" + 
            "(:action take_image\n" + 
            " :parameters (?s - satellite ?d - direction ?i - instrument ?m - mode)\n" + 
            " :precondition (and (calibrated ?i) (on_board ?s ?i)\n" + 
            "               (supports ?i ?m) (power_on ?i) (pointing ?s ?d))\n" + 
            " :effect (have_image ?d ?m)))\n" + 
            "";

    private static String problemSatellite0 = "(define (problem satellite0)\n" + 
    		"(:domain satellite)\n" + 
    		"(:objects\n" + 
    		" satellite0 satellite1 - satellite\n" + 
    		" instrument0 instrument1 instrument2 instrument3 - instrument\n" + 
    		" image1 infrared0 spectrograph2 - mode\n" + 
    		" star1 star2 star0 star3 star4 phenomenon5 phenomenon6 phenomenon7 - direction\n" + 
    		")\n" + 
            "(:init\n" + 
    		" (power_avail satellite0)\n" + 
    		" (calibration_target instrument0 star1)\n" + 
    		" (calibration_target instrument1 star2)\n" + 
    		" (calibration_target instrument2 star0)\n" + 
    		" (calibration_target instrument3 star0)\n" + 
    		" (pointing satellite0 star4)\n" + 
            " (pointing satellite1 star0)\n" + 
    		" (on_board satellite0 instrument0)\n" + 
            " (on_board satellite0 instrument1)\n" + 
            " (on_board satellite0 instrument2)\n" + 
            " (supports instrument0 infrared0)\n" + 
    		" (supports instrument0 spectrograph2)\n" + 
    		" (supports instrument1 image1)\n" + 
    		" (supports instrument2 image1)\n" + 
            " (supports instrument2 infrared0)\n" + 
            " (supports instrument3 image1)\n" + 
//            " (supports instrument3 infrared0)\n" + 
    		" (supports instrument3 spectrograph2)\n" + 
    		")\n" + 
    		"(:goal (and\n" + 
//    		" (pointing satellite0 phenomenon5)\n" + 
    		" (have_image star3 infrared0)\n" + 
    		" (have_image star4 spectrograph2)\n" + 
    		" (have_image phenomenon5 spectrograph2)\n" + 
    		" (have_image phenomenon7 spectrograph2)\n" + 
    		"))\n" + 
    		")";  

    private static String problemSatellite1 = "(define (problem satellite1)\n" + 
    		"(:domain satellite)\n" + 
    		"(:objects\n" + 
    		" satellite0 satellite1 - satellite\n" + 
    		" instrument0 instrument1 instrument2 instrument3 - instrument\n" + 
    		" image1 infrared0 spectrograph2 - mode\n" + 
    		" star1 star2 star0 star3 star4 phenomenon5 phenomenon6 phenomenon7 - direction\n" + 
    		")\n" + 
    		"(:init\n" + 
    		" (power_avail satellite1)\n" + 
    		" (calibration_target instrument0 star1)\n" + 
    		" (calibration_target instrument1 star2)\n" + 
    		" (calibration_target instrument2 star0)\n" + 
    		" (calibration_target instrument3 star0)\n" + 
            " (pointing satellite0 star4)\n" + 
    		" (pointing satellite1 star0)\n" + 
    		" (on_board satellite1 instrument3)\n" + 
            " (supports instrument0 infrared0)\n" + 
            " (supports instrument0 spectrograph2)\n" + 
    		" (supports instrument1 image1)\n" + 
    		" (supports instrument2 image1)\n" + 
            " (supports instrument2 infrared0)\n" + 
            " (supports instrument3 image1)\n" + 
//            " (supports instrument3 infrared0)\n" + 
    		" (supports instrument3 spectrograph2)\n" + 
    		")\n" + 
    		"(:goal (and\n" + 
//    		" (pointing satellite0 phenomenon5)\n" + 
    		" (have_image star3 infrared0)\n" + 
    		" (have_image star4 spectrograph2)\n" + 
    		" (have_image phenomenon5 spectrograph2)\n" + 
    		" (have_image phenomenon7 spectrograph2)\n" + 
    		"))\n" + 
    		")"; 
}
