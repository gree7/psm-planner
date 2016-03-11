package cz.agents.dimap.data;

import java.util.HashMap;
import java.util.Map;

import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.psm.planner.PddlMultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlProblem;


public class SatellitesIpc01NotGrounded extends PddlTestProblem {
    public static MultiAgentProblem createProblem() {
        Map<String, PddlProblem> problems = new HashMap<String, PddlProblem>();
        problems.put("satellite0", createProblem(domainSatellite0, problemSatellite0));
        
        return new PddlMultiAgentProblem(problems);
    }

    private static String domainSatellite0 = "(define (domain satellite)\n" + 
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
            "(:shared-predicate-names have_image pointing)\n" + 
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

    private static String problemSatellite0 = "(define (problem strips-sat-x-1)\n" + 
    		"(:domain satellite)\n" + 
    		"(:objects\n" + 
    		" satellite0 - satellite\n" + 
    		" instrument0 - instrument\n" + 
    		" image1 spectrograph2 thermograph0 - mode\n" + 
    		" star0 groundstation1 groundstation2 phenomenon3 phenomenon4 star5 phenomenon6 - direction\n" + 
    		")\n" + 
    		"(:init\n" + 
    		" (power_avail satellite0)\n" + 
    		" (calibration_target instrument0 groundstation2)\n" + 
    		" (pointing satellite0 phenomenon6)\n" + 
    		" (on_board satellite0 instrument0)\n" + 
    		" (supports instrument0 thermograph0)\n" + 
    		")\n" + 
    		"(:goal (and\n" + 
    		" (have_image phenomenon4 thermograph0)\n" + 
    		" (have_image star5 thermograph0)\n" + 
    		" (have_image phenomenon6 thermograph0)\n" + 
    		"))\n" + 
    		")";  
}
