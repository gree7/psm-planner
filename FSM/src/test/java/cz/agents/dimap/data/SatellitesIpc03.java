package cz.agents.dimap.data;

import java.util.HashMap;
import java.util.Map;

import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.psm.planner.PddlMultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlProblem;


public class SatellitesIpc03 extends PddlTestProblem {
    public static MultiAgentProblem getProblem() {
        Map<String, PddlProblem> problems = new HashMap<String, PddlProblem>();
        problems.put("satellite0", createProblem(domainSatellite0, problemSatellite0));
        problems.put("satellite1", createProblem(domainSatellite1, problemSatellite1));
        
        return new PddlMultiAgentProblem(problems);
    }

    private static String domainSatellite0 = "(define (domain satellite0-domain)\n" + 
    		"(:requirements :strips)\n" + 
    		"(:predicates\n" + 
    		"    (int_calibrated_instrument0)\n" + 
    		"    (int_calibrated_instrument1)\n" + 
    		"    (int_calibrated_instrument2)\n" + 
    		"    (int_calibration_target_instrument0_star1)\n" + 
    		"    (int_calibration_target_instrument1_star2)\n" + 
    		"    (int_calibration_target_instrument2_star0)\n" + 
    		"    (int_on_board_instrument0_satellite0)\n" + 
    		"    (int_on_board_instrument1_satellite0)\n" + 
    		"    (int_on_board_instrument2_satellite0)\n" + 
    		"    (int_pointing_satellite0_phenomenon6)\n" + 
    		"    (int_pointing_satellite0_phenomenon7)\n" + 
    		"    (int_pointing_satellite0_star0)\n" + 
    		"    (int_pointing_satellite0_star1)\n" + 
    		"    (int_pointing_satellite0_star2)\n" + 
    		"    (int_pointing_satellite0_star3)\n" + 
    		"    (int_pointing_satellite0_star4)\n" + 
    		"    (int_power_avail_satellite0)\n" + 
    		"    (int_power_on_instrument0)\n" + 
    		"    (int_power_on_instrument1)\n" + 
    		"    (int_power_on_instrument2)\n" + 
    		"    (int_supports_instrument0_infrared0)\n" + 
    		"    (int_supports_instrument0_spectrograph2)\n" + 
    		"    (int_supports_instrument1_image1)\n" + 
    		"    (int_supports_instrument2_image1)\n" + 
    		"    (int_supports_instrument2_infrared0)\n" + 
    		"    (pub_have_image_phenomenon5_image1)\n" + 
    		"    (pub_have_image_phenomenon5_infrared0)\n" + 
    		"    (pub_have_image_phenomenon5_spectrograph2)\n" + 
    		"    (pub_have_image_phenomenon6_image1)\n" + 
    		"    (pub_have_image_phenomenon6_infrared0)\n" + 
    		"    (pub_have_image_phenomenon6_spectrograph2)\n" + 
    		"    (pub_have_image_phenomenon7_image1)\n" + 
    		"    (pub_have_image_phenomenon7_infrared0)\n" + 
    		"    (pub_have_image_phenomenon7_spectrograph2)\n" + 
    		"    (pub_have_image_star0_image1)\n" + 
    		"    (pub_have_image_star0_infrared0)\n" + 
    		"    (pub_have_image_star0_spectrograph2)\n" + 
    		"    (pub_have_image_star1_image1)\n" + 
    		"    (pub_have_image_star1_infrared0)\n" + 
    		"    (pub_have_image_star1_spectrograph2)\n" + 
    		"    (pub_have_image_star2_image1)\n" + 
    		"    (pub_have_image_star2_infrared0)\n" + 
    		"    (pub_have_image_star2_spectrograph2)\n" + 
    		"    (pub_have_image_star3_image1)\n" + 
    		"    (pub_have_image_star3_infrared0)\n" + 
    		"    (pub_have_image_star3_spectrograph2)\n" + 
    		"    (pub_have_image_star4_image1)\n" + 
    		"    (pub_have_image_star4_infrared0)\n" + 
    		"    (pub_have_image_star4_spectrograph2)\n" + 
    		"    (pub_pointing_satellite0_phenomenon5)\n" + 
    		")\n" + 
            "(:shared-predicate-names " +
            "    pub_have_image_phenomenon5_image1\n" + 
            "    pub_have_image_phenomenon5_infrared0\n" + 
            "    pub_have_image_phenomenon5_spectrograph2\n" + 
            "    pub_have_image_phenomenon6_image1\n" + 
            "    pub_have_image_phenomenon6_infrared0\n" + 
            "    pub_have_image_phenomenon6_spectrograph2\n" + 
            "    pub_have_image_phenomenon7_image1\n" + 
            "    pub_have_image_phenomenon7_infrared0\n" + 
            "    pub_have_image_phenomenon7_spectrograph2\n" + 
            "    pub_have_image_star0_image1\n" + 
            "    pub_have_image_star0_infrared0\n" + 
            "    pub_have_image_star0_spectrograph2\n" + 
            "    pub_have_image_star1_image1\n" + 
            "    pub_have_image_star1_infrared0\n" + 
            "    pub_have_image_star1_spectrograph2\n" + 
            "    pub_have_image_star2_image1\n" + 
            "    pub_have_image_star2_infrared0\n" + 
            "    pub_have_image_star2_spectrograph2\n" + 
            "    pub_have_image_star3_image1\n" + 
            "    pub_have_image_star3_infrared0\n" + 
            "    pub_have_image_star3_spectrograph2\n" + 
            "    pub_have_image_star4_image1\n" + 
            "    pub_have_image_star4_infrared0\n" + 
            "    pub_have_image_star4_spectrograph2\n" + 
//            "    pub_pointing_satellite0_phenomenon5\n" + 
            ")\n" + 
    		"\n" + 
    		"(:action int_calibrate_satellite0_instrument0_star1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibration_target_instrument0_star1)\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (int_pointing_satellite0_star1)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"    )\n" + 
    		"    :effect (int_calibrated_instrument0)\n" + 
    		")\n" + 
    		"(:action int_calibrate_satellite0_instrument1_star2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibration_target_instrument1_star2)\n" + 
    		"        (int_on_board_instrument1_satellite0)\n" + 
    		"        (int_pointing_satellite0_star2)\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"    )\n" + 
    		"    :effect (int_calibrated_instrument1)\n" + 
    		")\n" + 
    		"(:action int_calibrate_satellite0_instrument2_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibration_target_instrument2_star0)\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (int_pointing_satellite0_star0)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"    )\n" + 
    		"    :effect (int_calibrated_instrument2)\n" + 
    		")\n" + 
    		"(:action int_switch_off_instrument0_satellite0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"    )\n" + 
    		"    :effect (and\n" + 
    		"        (int_power_avail_satellite0)\n" + 
    		"        (not (int_power_on_instrument0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_switch_off_instrument1_satellite0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_on_board_instrument1_satellite0)\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"    )\n" + 
    		"    :effect (and\n" + 
    		"        (int_power_avail_satellite0)\n" + 
    		"        (not (int_power_on_instrument1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_switch_off_instrument2_satellite0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"    )\n" + 
    		"    :effect (and\n" + 
    		"        (int_power_avail_satellite0)\n" + 
    		"        (not (int_power_on_instrument2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_switch_on_instrument0_satellite0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (int_power_avail_satellite0)\n" + 
    		"    )\n" + 
    		"    :effect (and\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (not (int_calibrated_instrument0))\n" + 
    		"        (not (int_power_avail_satellite0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_switch_on_instrument1_satellite0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_on_board_instrument1_satellite0)\n" + 
    		"        (int_power_avail_satellite0)\n" + 
    		"    )\n" + 
    		"    :effect (and\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"        (not (int_calibrated_instrument1))\n" + 
    		"        (not (int_power_avail_satellite0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_switch_on_instrument2_satellite0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (int_power_avail_satellite0)\n" + 
    		"    )\n" + 
    		"    :effect (and\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (not (int_calibrated_instrument2))\n" + 
    		"        (not (int_power_avail_satellite0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_phenomenon6_phenomenon7\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_phenomenon7)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_phenomenon6)\n" + 
    		"        (not (int_pointing_satellite0_phenomenon7))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_phenomenon6_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_phenomenon6)\n" + 
    		"        (not (int_pointing_satellite0_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_phenomenon6_star1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star1)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_phenomenon6)\n" + 
    		"        (not (int_pointing_satellite0_star1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_phenomenon6_star2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star2)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_phenomenon6)\n" + 
    		"        (not (int_pointing_satellite0_star2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_phenomenon6_star3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star3)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_phenomenon6)\n" + 
    		"        (not (int_pointing_satellite0_star3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_phenomenon6_star4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star4)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_phenomenon6)\n" + 
    		"        (not (int_pointing_satellite0_star4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_phenomenon7_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_phenomenon7)\n" + 
    		"        (not (int_pointing_satellite0_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_phenomenon7_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_phenomenon7)\n" + 
    		"        (not (int_pointing_satellite0_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_phenomenon7_star1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star1)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_phenomenon7)\n" + 
    		"        (not (int_pointing_satellite0_star1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_phenomenon7_star2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star2)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_phenomenon7)\n" + 
    		"        (not (int_pointing_satellite0_star2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_phenomenon7_star3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star3)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_phenomenon7)\n" + 
    		"        (not (int_pointing_satellite0_star3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_phenomenon7_star4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star4)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_phenomenon7)\n" + 
    		"        (not (int_pointing_satellite0_star4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star0_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star0)\n" + 
    		"        (not (int_pointing_satellite0_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star0_phenomenon7\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_phenomenon7)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star0)\n" + 
    		"        (not (int_pointing_satellite0_phenomenon7))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star0_star1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star1)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star0)\n" + 
    		"        (not (int_pointing_satellite0_star1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star0_star2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star2)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star0)\n" + 
    		"        (not (int_pointing_satellite0_star2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star0_star3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star3)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star0)\n" + 
    		"        (not (int_pointing_satellite0_star3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star0_star4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star4)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star0)\n" + 
    		"        (not (int_pointing_satellite0_star4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star1_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star1)\n" + 
    		"        (not (int_pointing_satellite0_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star1_phenomenon7\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_phenomenon7)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star1)\n" + 
    		"        (not (int_pointing_satellite0_phenomenon7))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star1_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star1)\n" + 
    		"        (not (int_pointing_satellite0_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star1_star2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star2)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star1)\n" + 
    		"        (not (int_pointing_satellite0_star2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star1_star3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star3)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star1)\n" + 
    		"        (not (int_pointing_satellite0_star3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star1_star4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star4)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star1)\n" + 
    		"        (not (int_pointing_satellite0_star4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star2_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star2)\n" + 
    		"        (not (int_pointing_satellite0_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star2_phenomenon7\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_phenomenon7)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star2)\n" + 
    		"        (not (int_pointing_satellite0_phenomenon7))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star2_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star2)\n" + 
    		"        (not (int_pointing_satellite0_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star2_star1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star1)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star2)\n" + 
    		"        (not (int_pointing_satellite0_star1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star2_star3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star3)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star2)\n" + 
    		"        (not (int_pointing_satellite0_star3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star2_star4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star4)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star2)\n" + 
    		"        (not (int_pointing_satellite0_star4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star3_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star3)\n" + 
    		"        (not (int_pointing_satellite0_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star3_phenomenon7\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_phenomenon7)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star3)\n" + 
    		"        (not (int_pointing_satellite0_phenomenon7))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star3_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star3)\n" + 
    		"        (not (int_pointing_satellite0_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star3_star1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star1)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star3)\n" + 
    		"        (not (int_pointing_satellite0_star1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star3_star2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star2)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star3)\n" + 
    		"        (not (int_pointing_satellite0_star2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star3_star4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star4)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star3)\n" + 
    		"        (not (int_pointing_satellite0_star4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star4_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star4)\n" + 
    		"        (not (int_pointing_satellite0_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star4_phenomenon7\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_phenomenon7)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star4)\n" + 
    		"        (not (int_pointing_satellite0_phenomenon7))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star4_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star4)\n" + 
    		"        (not (int_pointing_satellite0_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star4_star1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star1)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star4)\n" + 
    		"        (not (int_pointing_satellite0_star1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star4_star2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star2)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star4)\n" + 
    		"        (not (int_pointing_satellite0_star2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite0_star4_star3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star3)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star4)\n" + 
    		"        (not (int_pointing_satellite0_star3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_phenomenon5_instrument0_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument0)\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (pub_pointing_satellite0_phenomenon5)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_supports_instrument0_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon5_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_phenomenon5_instrument0_spectrograph2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument0)\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (pub_pointing_satellite0_phenomenon5)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_supports_instrument0_spectrograph2)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon5_spectrograph2)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_phenomenon5_instrument1_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument1)\n" + 
    		"        (int_on_board_instrument1_satellite0)\n" + 
    		"        (pub_pointing_satellite0_phenomenon5)\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"        (int_supports_instrument1_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon5_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_phenomenon5_instrument2_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument2)\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (pub_pointing_satellite0_phenomenon5)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_supports_instrument2_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon5_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_phenomenon5_instrument2_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument2)\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (pub_pointing_satellite0_phenomenon5)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_supports_instrument2_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon5_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_phenomenon6_instrument0_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument0)\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (int_pointing_satellite0_phenomenon6)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_supports_instrument0_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon6_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_phenomenon6_instrument0_spectrograph2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument0)\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (int_pointing_satellite0_phenomenon6)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_supports_instrument0_spectrograph2)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon6_spectrograph2)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_phenomenon6_instrument1_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument1)\n" + 
    		"        (int_on_board_instrument1_satellite0)\n" + 
    		"        (int_pointing_satellite0_phenomenon6)\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"        (int_supports_instrument1_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon6_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_phenomenon6_instrument2_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument2)\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (int_pointing_satellite0_phenomenon6)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_supports_instrument2_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon6_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_phenomenon6_instrument2_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument2)\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (int_pointing_satellite0_phenomenon6)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_supports_instrument2_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon6_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_phenomenon7_instrument0_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument0)\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (int_pointing_satellite0_phenomenon7)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_supports_instrument0_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon7_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_phenomenon7_instrument0_spectrograph2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument0)\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (int_pointing_satellite0_phenomenon7)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_supports_instrument0_spectrograph2)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon7_spectrograph2)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_phenomenon7_instrument1_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument1)\n" + 
    		"        (int_on_board_instrument1_satellite0)\n" + 
    		"        (int_pointing_satellite0_phenomenon7)\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"        (int_supports_instrument1_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon7_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_phenomenon7_instrument2_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument2)\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (int_pointing_satellite0_phenomenon7)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_supports_instrument2_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon7_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_phenomenon7_instrument2_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument2)\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (int_pointing_satellite0_phenomenon7)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_supports_instrument2_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon7_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star0_instrument0_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument0)\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (int_pointing_satellite0_star0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_supports_instrument0_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star0_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star0_instrument0_spectrograph2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument0)\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (int_pointing_satellite0_star0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_supports_instrument0_spectrograph2)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star0_spectrograph2)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star0_instrument1_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument1)\n" + 
    		"        (int_on_board_instrument1_satellite0)\n" + 
    		"        (int_pointing_satellite0_star0)\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"        (int_supports_instrument1_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star0_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star0_instrument2_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument2)\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (int_pointing_satellite0_star0)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_supports_instrument2_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star0_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star0_instrument2_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument2)\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (int_pointing_satellite0_star0)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_supports_instrument2_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star0_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star1_instrument0_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument0)\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (int_pointing_satellite0_star1)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_supports_instrument0_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star1_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star1_instrument0_spectrograph2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument0)\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (int_pointing_satellite0_star1)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_supports_instrument0_spectrograph2)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star1_spectrograph2)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star1_instrument1_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument1)\n" + 
    		"        (int_on_board_instrument1_satellite0)\n" + 
    		"        (int_pointing_satellite0_star1)\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"        (int_supports_instrument1_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star1_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star1_instrument2_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument2)\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (int_pointing_satellite0_star1)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_supports_instrument2_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star1_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star1_instrument2_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument2)\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (int_pointing_satellite0_star1)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_supports_instrument2_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star1_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star2_instrument0_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument0)\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (int_pointing_satellite0_star2)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_supports_instrument0_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star2_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star2_instrument0_spectrograph2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument0)\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (int_pointing_satellite0_star2)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_supports_instrument0_spectrograph2)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star2_spectrograph2)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star2_instrument1_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument1)\n" + 
    		"        (int_on_board_instrument1_satellite0)\n" + 
    		"        (int_pointing_satellite0_star2)\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"        (int_supports_instrument1_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star2_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star2_instrument2_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument2)\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (int_pointing_satellite0_star2)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_supports_instrument2_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star2_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star2_instrument2_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument2)\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (int_pointing_satellite0_star2)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_supports_instrument2_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star2_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star3_instrument0_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument0)\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (int_pointing_satellite0_star3)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_supports_instrument0_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star3_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star3_instrument0_spectrograph2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument0)\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (int_pointing_satellite0_star3)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_supports_instrument0_spectrograph2)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star3_spectrograph2)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star3_instrument1_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument1)\n" + 
    		"        (int_on_board_instrument1_satellite0)\n" + 
    		"        (int_pointing_satellite0_star3)\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"        (int_supports_instrument1_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star3_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star3_instrument2_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument2)\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (int_pointing_satellite0_star3)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_supports_instrument2_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star3_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star3_instrument2_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument2)\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (int_pointing_satellite0_star3)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_supports_instrument2_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star3_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star4_instrument0_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument0)\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (int_pointing_satellite0_star4)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_supports_instrument0_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star4_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star4_instrument0_spectrograph2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument0)\n" + 
    		"        (int_on_board_instrument0_satellite0)\n" + 
    		"        (int_pointing_satellite0_star4)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_power_on_instrument0)\n" + 
    		"        (int_supports_instrument0_spectrograph2)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star4_spectrograph2)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star4_instrument1_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument1)\n" + 
    		"        (int_on_board_instrument1_satellite0)\n" + 
    		"        (int_pointing_satellite0_star4)\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"        (int_power_on_instrument1)\n" + 
    		"        (int_supports_instrument1_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star4_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star4_instrument2_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument2)\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (int_pointing_satellite0_star4)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_supports_instrument2_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star4_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite0_star4_instrument2_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument2)\n" + 
    		"        (int_on_board_instrument2_satellite0)\n" + 
    		"        (int_pointing_satellite0_star4)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_power_on_instrument2)\n" + 
    		"        (int_supports_instrument2_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star4_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_turn_to_satellite0_phenomenon5_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (pub_pointing_satellite0_phenomenon5)\n" + 
    		"        (not (int_pointing_satellite0_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action pub_turn_to_satellite0_phenomenon5_phenomenon7\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_phenomenon7)\n" + 
    		"    :effect (and\n" + 
    		"        (pub_pointing_satellite0_phenomenon5)\n" + 
    		"        (not (int_pointing_satellite0_phenomenon7))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action pub_turn_to_satellite0_phenomenon5_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (pub_pointing_satellite0_phenomenon5)\n" + 
    		"        (not (int_pointing_satellite0_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action pub_turn_to_satellite0_phenomenon5_star1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star1)\n" + 
    		"    :effect (and\n" + 
    		"        (pub_pointing_satellite0_phenomenon5)\n" + 
    		"        (not (int_pointing_satellite0_star1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action pub_turn_to_satellite0_phenomenon5_star2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star2)\n" + 
    		"    :effect (and\n" + 
    		"        (pub_pointing_satellite0_phenomenon5)\n" + 
    		"        (not (int_pointing_satellite0_star2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action pub_turn_to_satellite0_phenomenon5_star3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star3)\n" + 
    		"    :effect (and\n" + 
    		"        (pub_pointing_satellite0_phenomenon5)\n" + 
    		"        (not (int_pointing_satellite0_star3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action pub_turn_to_satellite0_phenomenon5_star4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite0_star4)\n" + 
    		"    :effect (and\n" + 
    		"        (pub_pointing_satellite0_phenomenon5)\n" + 
    		"        (not (int_pointing_satellite0_star4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action pub_turn_to_satellite0_phenomenon6_phenomenon5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (pub_pointing_satellite0_phenomenon5)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_phenomenon6)\n" + 
    		"        (not (pub_pointing_satellite0_phenomenon5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action pub_turn_to_satellite0_phenomenon7_phenomenon5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (pub_pointing_satellite0_phenomenon5)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_phenomenon7)\n" + 
    		"        (not (pub_pointing_satellite0_phenomenon5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action pub_turn_to_satellite0_star0_phenomenon5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (pub_pointing_satellite0_phenomenon5)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star0)\n" + 
    		"        (not (pub_pointing_satellite0_phenomenon5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action pub_turn_to_satellite0_star1_phenomenon5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (pub_pointing_satellite0_phenomenon5)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star1)\n" + 
    		"        (not (pub_pointing_satellite0_phenomenon5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action pub_turn_to_satellite0_star2_phenomenon5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (pub_pointing_satellite0_phenomenon5)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star2)\n" + 
    		"        (not (pub_pointing_satellite0_phenomenon5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action pub_turn_to_satellite0_star3_phenomenon5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (pub_pointing_satellite0_phenomenon5)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star3)\n" + 
    		"        (not (pub_pointing_satellite0_phenomenon5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action pub_turn_to_satellite0_star4_phenomenon5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (pub_pointing_satellite0_phenomenon5)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite0_star4)\n" + 
    		"        (not (pub_pointing_satellite0_phenomenon5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"\n" + 
    		"\n" + 
    		")\n" + 
    		"";

    private static String problemSatellite0 = "(define (problem satellite0-problem)\n" + 
    		"(:domain satellite0-domain)\n" + 
    		"(:requirements :strips)\n" + 
    		"(:init\n" + 
    		"    (int_calibration_target_instrument0_star1)\n" + 
    		"    (int_calibration_target_instrument1_star2)\n" + 
    		"    (int_calibration_target_instrument2_star0)\n" + 
    		"    (int_on_board_instrument0_satellite0)\n" + 
    		"    (int_on_board_instrument1_satellite0)\n" + 
    		"    (int_on_board_instrument2_satellite0)\n" + 
    		"    (int_pointing_satellite0_star4)\n" + 
    		"    (int_power_avail_satellite0)\n" + 
    		"    (int_supports_instrument0_infrared0)\n" + 
    		"    (int_supports_instrument0_spectrograph2)\n" + 
    		"    (int_supports_instrument1_image1)\n" + 
    		"    (int_supports_instrument2_image1)\n" + 
    		"    (int_supports_instrument2_infrared0)\n" + 
    		")\n" + 
    		"\n" + 
    		"(:goal (and\n" + 
    		"        (pub_have_image_phenomenon5_spectrograph2)\n" + 
    		"        (pub_have_image_phenomenon7_spectrograph2)\n" + 
    		"        (pub_have_image_star3_infrared0)\n" + 
    		"        (pub_have_image_star4_spectrograph2)\n" + 
    		"        (pub_pointing_satellite0_phenomenon5)\n" + 
    		"    )\n" + 
    		")\n" + 
    		"\n" + 
    		")\n" + 
    		"";  

    private static String domainSatellite1 = "(define (domain satellite1-domain)\n" + 
    		"(:requirements :strips)\n" + 
    		"(:predicates\n" + 
    		"    (int_calibrated_instrument3)\n" + 
    		"    (int_calibration_target_instrument3_star0)\n" + 
    		"    (int_on_board_instrument3_satellite1)\n" + 
    		"    (int_pointing_satellite1_phenomenon5)\n" + 
    		"    (int_pointing_satellite1_phenomenon6)\n" + 
    		"    (int_pointing_satellite1_phenomenon7)\n" + 
    		"    (int_pointing_satellite1_star0)\n" + 
    		"    (int_pointing_satellite1_star1)\n" + 
    		"    (int_pointing_satellite1_star2)\n" + 
    		"    (int_pointing_satellite1_star3)\n" + 
    		"    (int_pointing_satellite1_star4)\n" + 
    		"    (int_power_avail_satellite1)\n" + 
    		"    (int_power_on_instrument3)\n" + 
    		"    (int_supports_instrument3_image1)\n" + 
    		"    (int_supports_instrument3_infrared0)\n" + 
    		"    (int_supports_instrument3_spectrograph2)\n" + 
    		"    (pub_have_image_phenomenon5_image1)\n" + 
    		"    (pub_have_image_phenomenon5_infrared0)\n" + 
    		"    (pub_have_image_phenomenon5_spectrograph2)\n" + 
    		"    (pub_have_image_phenomenon6_image1)\n" + 
    		"    (pub_have_image_phenomenon6_infrared0)\n" + 
    		"    (pub_have_image_phenomenon6_spectrograph2)\n" + 
    		"    (pub_have_image_phenomenon7_image1)\n" + 
    		"    (pub_have_image_phenomenon7_infrared0)\n" + 
    		"    (pub_have_image_phenomenon7_spectrograph2)\n" + 
    		"    (pub_have_image_star0_image1)\n" + 
    		"    (pub_have_image_star0_infrared0)\n" + 
    		"    (pub_have_image_star0_spectrograph2)\n" + 
    		"    (pub_have_image_star1_image1)\n" + 
    		"    (pub_have_image_star1_infrared0)\n" + 
    		"    (pub_have_image_star1_spectrograph2)\n" + 
    		"    (pub_have_image_star2_image1)\n" + 
    		"    (pub_have_image_star2_infrared0)\n" + 
    		"    (pub_have_image_star2_spectrograph2)\n" + 
    		"    (pub_have_image_star3_image1)\n" + 
    		"    (pub_have_image_star3_infrared0)\n" + 
    		"    (pub_have_image_star3_spectrograph2)\n" + 
    		"    (pub_have_image_star4_image1)\n" + 
    		"    (pub_have_image_star4_infrared0)\n" + 
    		"    (pub_have_image_star4_spectrograph2)\n" + 
    		"    (pub_pointing_satellite0_phenomenon5)\n" + 
    		")\n" + 
            "(:shared-predicate-names " +
            "    pub_have_image_phenomenon5_image1\n" + 
            "    pub_have_image_phenomenon5_infrared0\n" + 
            "    pub_have_image_phenomenon5_spectrograph2\n" + 
            "    pub_have_image_phenomenon6_image1\n" + 
            "    pub_have_image_phenomenon6_infrared0\n" + 
            "    pub_have_image_phenomenon6_spectrograph2\n" + 
            "    pub_have_image_phenomenon7_image1\n" + 
            "    pub_have_image_phenomenon7_infrared0\n" + 
            "    pub_have_image_phenomenon7_spectrograph2\n" + 
            "    pub_have_image_star0_image1\n" + 
            "    pub_have_image_star0_infrared0\n" + 
            "    pub_have_image_star0_spectrograph2\n" + 
            "    pub_have_image_star1_image1\n" + 
            "    pub_have_image_star1_infrared0\n" + 
            "    pub_have_image_star1_spectrograph2\n" + 
            "    pub_have_image_star2_image1\n" + 
            "    pub_have_image_star2_infrared0\n" + 
            "    pub_have_image_star2_spectrograph2\n" + 
            "    pub_have_image_star3_image1\n" + 
            "    pub_have_image_star3_infrared0\n" + 
            "    pub_have_image_star3_spectrograph2\n" + 
            "    pub_have_image_star4_image1\n" + 
            "    pub_have_image_star4_infrared0\n" + 
            "    pub_have_image_star4_spectrograph2\n" + 
            "    pub_pointing_satellite0_phenomenon5\n" + 
            ")\n" + 
    		"\n" + 
    		"(:action int_calibrate_satellite1_instrument3_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibration_target_instrument3_star0)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_star0)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"    )\n" + 
    		"    :effect (int_calibrated_instrument3)\n" + 
    		")\n" + 
    		"(:action int_switch_off_instrument3_satellite1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"    )\n" + 
    		"    :effect (and\n" + 
    		"        (int_power_avail_satellite1)\n" + 
    		"        (not (int_power_on_instrument3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_switch_on_instrument3_satellite1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_power_avail_satellite1)\n" + 
    		"    )\n" + 
    		"    :effect (and\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (not (int_calibrated_instrument3))\n" + 
    		"        (not (int_power_avail_satellite1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon5_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon5)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon5_phenomenon7\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon7)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon5)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon7))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon5_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon5)\n" + 
    		"        (not (int_pointing_satellite1_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon5_star1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star1)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon5)\n" + 
    		"        (not (int_pointing_satellite1_star1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon5_star2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star2)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon5)\n" + 
    		"        (not (int_pointing_satellite1_star2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon5_star3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star3)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon5)\n" + 
    		"        (not (int_pointing_satellite1_star3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon5_star4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star4)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon5)\n" + 
    		"        (not (int_pointing_satellite1_star4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon6_phenomenon5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon5)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon6)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon6_phenomenon7\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon7)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon6)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon7))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon6_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon6)\n" + 
    		"        (not (int_pointing_satellite1_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon6_star1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star1)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon6)\n" + 
    		"        (not (int_pointing_satellite1_star1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon6_star2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star2)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon6)\n" + 
    		"        (not (int_pointing_satellite1_star2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon6_star3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star3)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon6)\n" + 
    		"        (not (int_pointing_satellite1_star3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon6_star4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star4)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon6)\n" + 
    		"        (not (int_pointing_satellite1_star4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon7_phenomenon5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon5)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon7)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon7_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon7)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon7_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon7)\n" + 
    		"        (not (int_pointing_satellite1_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon7_star1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star1)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon7)\n" + 
    		"        (not (int_pointing_satellite1_star1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon7_star2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star2)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon7)\n" + 
    		"        (not (int_pointing_satellite1_star2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon7_star3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star3)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon7)\n" + 
    		"        (not (int_pointing_satellite1_star3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_phenomenon7_star4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star4)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_phenomenon7)\n" + 
    		"        (not (int_pointing_satellite1_star4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star0_phenomenon5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon5)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star0)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star0_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star0)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star0_phenomenon7\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon7)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star0)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon7))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star0_star1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star1)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star0)\n" + 
    		"        (not (int_pointing_satellite1_star1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star0_star2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star2)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star0)\n" + 
    		"        (not (int_pointing_satellite1_star2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star0_star3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star3)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star0)\n" + 
    		"        (not (int_pointing_satellite1_star3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star0_star4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star4)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star0)\n" + 
    		"        (not (int_pointing_satellite1_star4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star1_phenomenon5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon5)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star1)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star1_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star1)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star1_phenomenon7\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon7)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star1)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon7))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star1_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star1)\n" + 
    		"        (not (int_pointing_satellite1_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star1_star2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star2)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star1)\n" + 
    		"        (not (int_pointing_satellite1_star2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star1_star3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star3)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star1)\n" + 
    		"        (not (int_pointing_satellite1_star3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star1_star4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star4)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star1)\n" + 
    		"        (not (int_pointing_satellite1_star4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star2_phenomenon5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon5)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star2)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star2_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star2)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star2_phenomenon7\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon7)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star2)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon7))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star2_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star2)\n" + 
    		"        (not (int_pointing_satellite1_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star2_star1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star1)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star2)\n" + 
    		"        (not (int_pointing_satellite1_star1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star2_star3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star3)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star2)\n" + 
    		"        (not (int_pointing_satellite1_star3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star2_star4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star4)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star2)\n" + 
    		"        (not (int_pointing_satellite1_star4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star3_phenomenon5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon5)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star3)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star3_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star3)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star3_phenomenon7\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon7)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star3)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon7))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star3_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star3)\n" + 
    		"        (not (int_pointing_satellite1_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star3_star1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star1)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star3)\n" + 
    		"        (not (int_pointing_satellite1_star1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star3_star2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star2)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star3)\n" + 
    		"        (not (int_pointing_satellite1_star2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star3_star4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star4)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star3)\n" + 
    		"        (not (int_pointing_satellite1_star4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star4_phenomenon5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon5)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star4)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star4_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star4)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star4_phenomenon7\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_phenomenon7)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star4)\n" + 
    		"        (not (int_pointing_satellite1_phenomenon7))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star4_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star4)\n" + 
    		"        (not (int_pointing_satellite1_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star4_star1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star1)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star4)\n" + 
    		"        (not (int_pointing_satellite1_star1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star4_star2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star2)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star4)\n" + 
    		"        (not (int_pointing_satellite1_star2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action int_turn_to_satellite1_star4_star3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (int_pointing_satellite1_star3)\n" + 
    		"    :effect (and\n" + 
    		"        (int_pointing_satellite1_star4)\n" + 
    		"        (not (int_pointing_satellite1_star3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_phenomenon5_instrument3_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_phenomenon5)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon5_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_phenomenon5_instrument3_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_phenomenon5)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon5_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_phenomenon5_instrument3_spectrograph2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_phenomenon5)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_spectrograph2)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon5_spectrograph2)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_phenomenon6_instrument3_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_phenomenon6)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon6_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_phenomenon6_instrument3_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_phenomenon6)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon6_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_phenomenon6_instrument3_spectrograph2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_phenomenon6)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_spectrograph2)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon6_spectrograph2)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_phenomenon7_instrument3_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_phenomenon7)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon7_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_phenomenon7_instrument3_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_phenomenon7)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon7_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_phenomenon7_instrument3_spectrograph2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_phenomenon7)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_spectrograph2)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_phenomenon7_spectrograph2)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_star0_instrument3_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_star0)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star0_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_star0_instrument3_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_star0)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star0_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_star0_instrument3_spectrograph2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_star0)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_spectrograph2)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star0_spectrograph2)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_star1_instrument3_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_star1)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star1_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_star1_instrument3_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_star1)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star1_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_star1_instrument3_spectrograph2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_star1)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_spectrograph2)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star1_spectrograph2)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_star2_instrument3_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_star2)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star2_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_star2_instrument3_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_star2)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star2_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_star2_instrument3_spectrograph2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_star2)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_spectrograph2)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star2_spectrograph2)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_star3_instrument3_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_star3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star3_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_star3_instrument3_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_star3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star3_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_star3_instrument3_spectrograph2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_star3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_spectrograph2)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star3_spectrograph2)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_star4_instrument3_image1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_star4)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_image1)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star4_image1)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_star4_instrument3_infrared0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_star4)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_infrared0)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star4_infrared0)\n" + 
    		")\n" + 
    		"(:action pub_take_image_satellite1_star4_instrument3_spectrograph2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (int_calibrated_instrument3)\n" + 
    		"        (int_on_board_instrument3_satellite1)\n" + 
    		"        (int_pointing_satellite1_star4)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_power_on_instrument3)\n" + 
    		"        (int_supports_instrument3_spectrograph2)\n" + 
    		"    )\n" + 
    		"    :effect (pub_have_image_star4_spectrograph2)\n" + 
    		")\n" + 
    		"\n" + 
    		"\n" + 
    		")\n" + 
    		"";

    private static String problemSatellite1 = "(define (problem satellite1-problem)\n" + 
    		"(:domain satellite1-domain)\n" + 
    		"(:requirements :strips)\n" + 
    		"(:init\n" + 
    		"    (int_calibration_target_instrument3_star0)\n" + 
    		"    (int_on_board_instrument3_satellite1)\n" + 
    		"    (int_pointing_satellite1_star0)\n" + 
    		"    (int_power_avail_satellite1)\n" + 
    		"    (int_supports_instrument3_image1)\n" + 
    		"    (int_supports_instrument3_infrared0)\n" + 
    		"    (int_supports_instrument3_spectrograph2)\n" + 
    		")\n" + 
    		"\n" + 
    		"(:goal (and\n" + 
    		"        (pub_have_image_phenomenon5_spectrograph2)\n" + 
    		"        (pub_have_image_phenomenon7_spectrograph2)\n" + 
    		"        (pub_have_image_star3_infrared0)\n" + 
    		"        (pub_have_image_star4_spectrograph2)\n" + 
//    		"        (pub_pointing_satellite0_phenomenon5)\n" + 
    		"    )\n" + 
    		")\n" + 
    		"\n" + 
    		")\n" + 
    		"";  
}
