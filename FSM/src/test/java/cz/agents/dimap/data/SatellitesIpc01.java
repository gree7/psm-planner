package cz.agents.dimap.data;

import java.util.HashMap;
import java.util.Map;

import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.psm.planner.PddlMultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlProblem;


public class SatellitesIpc01 extends PddlTestProblem {
    public static MultiAgentProblem getProblem() {
        Map<String, PddlProblem> problems = new HashMap<String, PddlProblem>();
        problems.put("satellite0", createProblem(domainSatellite0, problemSatellite0));
        
        return new PddlMultiAgentProblem(problems);
    }

    private static String domainSatellite0 = "(define (domain satellite0-domain)\n" + 
    		"(:requirements :strips)\n" + 
    		"(:predicates\n" + 
    		"    (in_calibrated_instrument0)\n" + 
    		"    (in_calibration_target_instrument0_groundstation2)\n" + 
    		"    (in_have_image_groundstation1_thermograph0)\n" + 
    		"    (in_have_image_groundstation2_thermograph0)\n" + 
    		"    (in_have_image_phenomenon3_thermograph0)\n" + 
    		"    (in_have_image_star0_thermograph0)\n" + 
    		"    (in_on_board_instrument0_satellite0)\n" + 
    		"    (in_pointing_satellite0_groundstation1)\n" + 
    		"    (in_pointing_satellite0_groundstation2)\n" + 
    		"    (in_pointing_satellite0_phenomenon3)\n" + 
    		"    (in_pointing_satellite0_phenomenon4)\n" + 
    		"    (in_pointing_satellite0_phenomenon6)\n" + 
    		"    (in_pointing_satellite0_star0)\n" + 
    		"    (in_pointing_satellite0_star5)\n" + 
    		"    (in_power_avail_satellite0)\n" + 
    		"    (in_power_on_instrument0)\n" + 
    		"    (in_supports_instrument0_thermograph0)\n" + 
    		"    (pu_have_image_phenomenon4_thermograph0)\n" + 
    		"    (pu_have_image_phenomenon6_thermograph0)\n" + 
    		"    (pu_have_image_star5_thermograph0)\n" + 
    		")\n" + 
            "(:shared-predicate-names " +
            "    pu_have_image_phenomenon4_thermograph0\n" + 
            "    pu_have_image_phenomenon6_thermograph0\n" + 
            "    pu_have_image_star5_thermograph0\n" + 
            ")\n" + 
    		"\n" + 
    		"(:action in_calibrate_satellite0_instrument0_groundstation2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (in_calibration_target_instrument0_groundstation2)\n" + 
    		"        (in_on_board_instrument0_satellite0)\n" + 
    		"        (in_pointing_satellite0_groundstation2)\n" + 
    		"        (in_power_on_instrument0)\n" + 
    		"    )\n" + 
    		"    :effect (in_calibrated_instrument0)\n" + 
    		")\n" + 
    		"(:action in_switch_off_instrument0_satellite0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (in_on_board_instrument0_satellite0)\n" + 
    		"        (in_power_on_instrument0)\n" + 
    		"    )\n" + 
    		"    :effect (and\n" + 
    		"        (in_power_avail_satellite0)\n" + 
    		"        (not (in_power_on_instrument0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_switch_on_instrument0_satellite0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (in_on_board_instrument0_satellite0)\n" + 
    		"        (in_power_avail_satellite0)\n" + 
    		"    )\n" + 
    		"    :effect (and\n" + 
    		"        (in_power_on_instrument0)\n" + 
    		"        (not (in_calibrated_instrument0))\n" + 
    		"        (not (in_power_avail_satellite0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_take_image_satellite0_groundstation1_instrument0_thermograph0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (in_calibrated_instrument0)\n" + 
    		"        (in_on_board_instrument0_satellite0)\n" + 
    		"        (in_pointing_satellite0_groundstation1)\n" + 
    		"        (in_power_on_instrument0)\n" + 
    		"        (in_power_on_instrument0)\n" + 
    		"        (in_supports_instrument0_thermograph0)\n" + 
    		"    )\n" + 
    		"    :effect (in_have_image_groundstation1_thermograph0)\n" + 
    		")\n" + 
    		"(:action in_take_image_satellite0_groundstation2_instrument0_thermograph0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (in_calibrated_instrument0)\n" + 
    		"        (in_on_board_instrument0_satellite0)\n" + 
    		"        (in_pointing_satellite0_groundstation2)\n" + 
    		"        (in_power_on_instrument0)\n" + 
    		"        (in_power_on_instrument0)\n" + 
    		"        (in_supports_instrument0_thermograph0)\n" + 
    		"    )\n" + 
    		"    :effect (in_have_image_groundstation2_thermograph0)\n" + 
    		")\n" + 
    		"(:action in_take_image_satellite0_phenomenon3_instrument0_thermograph0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (in_calibrated_instrument0)\n" + 
    		"        (in_on_board_instrument0_satellite0)\n" + 
    		"        (in_pointing_satellite0_phenomenon3)\n" + 
    		"        (in_power_on_instrument0)\n" + 
    		"        (in_power_on_instrument0)\n" + 
    		"        (in_supports_instrument0_thermograph0)\n" + 
    		"    )\n" + 
    		"    :effect (in_have_image_phenomenon3_thermograph0)\n" + 
    		")\n" + 
    		"(:action in_take_image_satellite0_star0_instrument0_thermograph0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (in_calibrated_instrument0)\n" + 
    		"        (in_on_board_instrument0_satellite0)\n" + 
    		"        (in_pointing_satellite0_star0)\n" + 
    		"        (in_power_on_instrument0)\n" + 
    		"        (in_power_on_instrument0)\n" + 
    		"        (in_supports_instrument0_thermograph0)\n" + 
    		"    )\n" + 
    		"    :effect (in_have_image_star0_thermograph0)\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_groundstation1_groundstation2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_groundstation2)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_groundstation1)\n" + 
    		"        (not (in_pointing_satellite0_groundstation2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_groundstation1_phenomenon3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_phenomenon3)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_groundstation1)\n" + 
    		"        (not (in_pointing_satellite0_phenomenon3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_groundstation1_phenomenon4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_phenomenon4)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_groundstation1)\n" + 
    		"        (not (in_pointing_satellite0_phenomenon4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_groundstation1_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_groundstation1)\n" + 
    		"        (not (in_pointing_satellite0_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_groundstation1_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_groundstation1)\n" + 
    		"        (not (in_pointing_satellite0_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_groundstation1_star5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_star5)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_groundstation1)\n" + 
    		"        (not (in_pointing_satellite0_star5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_groundstation2_groundstation1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_groundstation1)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_groundstation2)\n" + 
    		"        (not (in_pointing_satellite0_groundstation1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_groundstation2_phenomenon3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_phenomenon3)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_groundstation2)\n" + 
    		"        (not (in_pointing_satellite0_phenomenon3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_groundstation2_phenomenon4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_phenomenon4)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_groundstation2)\n" + 
    		"        (not (in_pointing_satellite0_phenomenon4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_groundstation2_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_groundstation2)\n" + 
    		"        (not (in_pointing_satellite0_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_groundstation2_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_groundstation2)\n" + 
    		"        (not (in_pointing_satellite0_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_groundstation2_star5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_star5)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_groundstation2)\n" + 
    		"        (not (in_pointing_satellite0_star5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_phenomenon3_groundstation1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_groundstation1)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_phenomenon3)\n" + 
    		"        (not (in_pointing_satellite0_groundstation1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_phenomenon3_groundstation2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_groundstation2)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_phenomenon3)\n" + 
    		"        (not (in_pointing_satellite0_groundstation2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_phenomenon3_phenomenon4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_phenomenon4)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_phenomenon3)\n" + 
    		"        (not (in_pointing_satellite0_phenomenon4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_phenomenon3_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_phenomenon3)\n" + 
    		"        (not (in_pointing_satellite0_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_phenomenon3_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_phenomenon3)\n" + 
    		"        (not (in_pointing_satellite0_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_phenomenon3_star5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_star5)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_phenomenon3)\n" + 
    		"        (not (in_pointing_satellite0_star5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_phenomenon4_groundstation1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_groundstation1)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_phenomenon4)\n" + 
    		"        (not (in_pointing_satellite0_groundstation1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_phenomenon4_groundstation2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_groundstation2)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_phenomenon4)\n" + 
    		"        (not (in_pointing_satellite0_groundstation2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_phenomenon4_phenomenon3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_phenomenon3)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_phenomenon4)\n" + 
    		"        (not (in_pointing_satellite0_phenomenon3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_phenomenon4_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_phenomenon4)\n" + 
    		"        (not (in_pointing_satellite0_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_phenomenon4_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_phenomenon4)\n" + 
    		"        (not (in_pointing_satellite0_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_phenomenon4_star5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_star5)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_phenomenon4)\n" + 
    		"        (not (in_pointing_satellite0_star5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_phenomenon6_groundstation1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_groundstation1)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_phenomenon6)\n" + 
    		"        (not (in_pointing_satellite0_groundstation1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_phenomenon6_groundstation2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_groundstation2)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_phenomenon6)\n" + 
    		"        (not (in_pointing_satellite0_groundstation2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_phenomenon6_phenomenon3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_phenomenon3)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_phenomenon6)\n" + 
    		"        (not (in_pointing_satellite0_phenomenon3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_phenomenon6_phenomenon4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_phenomenon4)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_phenomenon6)\n" + 
    		"        (not (in_pointing_satellite0_phenomenon4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_phenomenon6_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_phenomenon6)\n" + 
    		"        (not (in_pointing_satellite0_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_phenomenon6_star5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_star5)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_phenomenon6)\n" + 
    		"        (not (in_pointing_satellite0_star5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_star0_groundstation1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_groundstation1)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_star0)\n" + 
    		"        (not (in_pointing_satellite0_groundstation1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_star0_groundstation2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_groundstation2)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_star0)\n" + 
    		"        (not (in_pointing_satellite0_groundstation2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_star0_phenomenon3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_phenomenon3)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_star0)\n" + 
    		"        (not (in_pointing_satellite0_phenomenon3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_star0_phenomenon4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_phenomenon4)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_star0)\n" + 
    		"        (not (in_pointing_satellite0_phenomenon4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_star0_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_star0)\n" + 
    		"        (not (in_pointing_satellite0_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_star0_star5\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_star5)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_star0)\n" + 
    		"        (not (in_pointing_satellite0_star5))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_star5_groundstation1\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_groundstation1)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_star5)\n" + 
    		"        (not (in_pointing_satellite0_groundstation1))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_star5_groundstation2\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_groundstation2)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_star5)\n" + 
    		"        (not (in_pointing_satellite0_groundstation2))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_star5_phenomenon3\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_phenomenon3)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_star5)\n" + 
    		"        (not (in_pointing_satellite0_phenomenon3))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_star5_phenomenon4\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_phenomenon4)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_star5)\n" + 
    		"        (not (in_pointing_satellite0_phenomenon4))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_star5_phenomenon6\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_phenomenon6)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_star5)\n" + 
    		"        (not (in_pointing_satellite0_phenomenon6))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action in_turn_to_satellite0_star5_star0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (in_pointing_satellite0_star0)\n" + 
    		"    :effect (and\n" + 
    		"        (in_pointing_satellite0_star5)\n" + 
    		"        (not (in_pointing_satellite0_star0))\n" + 
    		"    )\n" + 
    		")\n" + 
    		"(:action pu_take_image_satellite0_phenomenon4_instrument0_thermograph0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (in_calibrated_instrument0)\n" + 
    		"        (in_on_board_instrument0_satellite0)\n" + 
    		"        (in_pointing_satellite0_phenomenon4)\n" + 
    		"        (in_power_on_instrument0)\n" + 
    		"        (in_power_on_instrument0)\n" + 
    		"        (in_supports_instrument0_thermograph0)\n" + 
    		"    )\n" + 
    		"    :effect (pu_have_image_phenomenon4_thermograph0)\n" + 
    		")\n" + 
    		"(:action pu_take_image_satellite0_phenomenon6_instrument0_thermograph0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (in_calibrated_instrument0)\n" + 
    		"        (in_on_board_instrument0_satellite0)\n" + 
    		"        (in_pointing_satellite0_phenomenon6)\n" + 
    		"        (in_power_on_instrument0)\n" + 
    		"        (in_power_on_instrument0)\n" + 
    		"        (in_supports_instrument0_thermograph0)\n" + 
    		"    )\n" + 
    		"    :effect (pu_have_image_phenomenon6_thermograph0)\n" + 
    		")\n" + 
    		"(:action pu_take_image_satellite0_star5_instrument0_thermograph0\n" + 
    		"    :parameters ()\n" + 
    		"    :precondition (and\n" + 
    		"        (in_calibrated_instrument0)\n" + 
    		"        (in_on_board_instrument0_satellite0)\n" + 
    		"        (in_pointing_satellite0_star5)\n" + 
    		"        (in_power_on_instrument0)\n" + 
    		"        (in_power_on_instrument0)\n" + 
    		"        (in_supports_instrument0_thermograph0)\n" + 
    		"    )\n" + 
    		"    :effect (pu_have_image_star5_thermograph0)\n" + 
    		")\n" + 
    		"\n" + 
    		"\n" + 
    		")\n" + 
    		"";

    private static String problemSatellite0 = "(define (problem satellite0-problem)\n" + 
    		"(:domain satellite0-domain)\n" + 
    		"(:requirements :strips)\n" + 
    		"(:init\n" + 
    		"    (in_calibration_target_instrument0_groundstation2)\n" + 
    		"    (in_on_board_instrument0_satellite0)\n" + 
    		"    (in_pointing_satellite0_phenomenon6)\n" + 
    		"    (in_power_avail_satellite0)\n" + 
    		"    (in_supports_instrument0_thermograph0)\n" + 
    		")\n" + 
    		"\n" + 
    		"(:goal (and\n" + 
    		"        (pu_have_image_phenomenon4_thermograph0)\n" + 
    		"        (pu_have_image_phenomenon6_thermograph0)\n" + 
    		"        (pu_have_image_star5_thermograph0)\n" + 
    		"    )\n" + 
    		")\n" + 
    		"\n" + 
    		")\n" + 
    		"";  

}
