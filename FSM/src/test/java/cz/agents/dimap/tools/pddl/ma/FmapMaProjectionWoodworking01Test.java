package cz.agents.dimap.tools.pddl.ma;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class FmapMaProjectionWoodworking01Test {

    private static String domainString =
            "(define (domain woodworking-fmap)\n" + 
            "  (:requirements :typing)\n" + 
            "  (:types\n" + 
            "      acolour awood woodobj machine surface treatmentstatus aboardsize apartsize - object\n" + 
            "      highspeedsaw glazer grinder immersion-varnisher planer saw spray-varnisher - machine\n" + 
            "      board part - woodobj)\n" + 
            "\n" + 
            "  (:predicates \n" + 
            "            (unused ?obj - part)\n" + 
            "            (available ?obj - woodobj)\n" + 
            "\n" + 
            "            (surface_condition ?obj - woodobj ?surface - surface)\n" + 
            "            (treatment ?obj - part ?treatment - treatmentstatus)\n" + 
            "            (colour ?obj - part ?colour - acolour)\n" + 
            "            (wood ?obj - woodobj ?wood - awood)\n" + 
            "            (boardsize ?board - board ?size - aboardsize)\n" + 
            "            (goalsize ?part - part ?size - apartsize)\n" + 
            "            (boardsize_successor ?size1 ?size2 - aboardsize)\n" + 
            "\n" + 
            "            (in_highspeedsaw ?b - board ?m - highspeedsaw)\n" + 
            "            (empty ?m - highspeedsaw)\n" + 
            "            (has_colour ?machine - machine ?colour - acolour)\n" + 
            "            (contains_part ?b - board ?p - part)\n" + 
            "            (grind_treatment_change ?old ?new - treatmentstatus)\n" + 
            "            (is_smooth ?surface - surface))\n" + 
            "      \n" + 
            "  (:action do-immersion-varnish\n" + 
            "    :parameters (?x - part ?m - immersion-varnisher \n" + 
            "                 ?newcolour - acolour ?surface - surface)\n" + 
            "    :precondition (and\n" + 
            "            (available ?x)\n" + 
            "            (has_colour ?m ?newcolour)\n" + 
            "            (surface_condition ?x ?surface)\n" + 
            "            (is_smooth ?surface)\n" + 
            "            (treatment ?x untreated))\n" + 
            "    :effect (and\n" + 
            "            (not (treatment ?x untreated))\n" + 
            "            (treatment ?x varnished)\n" + 
            "            (not (colour ?x natural))\n" + 
            "            (colour ?x ?newcolour)))\n" + 
            "\n" + 
            "  (:action do-spray-varnish\n" + 
            "    :parameters (?x - part ?m - spray-varnisher \n" + 
            "                 ?newcolour - acolour ?surface - surface)\n" + 
            "    :precondition (and\n" + 
            "            (available ?x)\n" + 
            "            (has_colour ?m ?newcolour)\n" + 
            "            (surface_condition ?x ?surface)\n" + 
            "            (is_smooth ?surface)\n" + 
            "            (treatment ?x untreated))\n" + 
            "    :effect (and \n" + 
            "            (not (treatment ?x untreated))\n" + 
            "            (treatment ?x varnished)\n" + 
            "            (not (colour ?x natural))\n" + 
            "            (colour ?x ?newcolour)))\n" + 
            "\n" + 
            "  (:action do-glaze\n" + 
            "    :parameters (?x - part ?m - glazer \n" + 
            "                 ?newcolour - acolour)\n" + 
            "    :precondition (and\n" + 
            "            (available ?x)\n" + 
            "            (has_colour ?m ?newcolour)\n" + 
            "            (treatment ?x untreated))\n" + 
            "    :effect (and \n" + 
            "            (not (treatment ?x untreated))\n" + 
            "            (treatment ?x glazed)\n" + 
            "            (not (colour ?x natural))\n" + 
            "            (colour ?x ?newcolour)))\n" + 
            "\n" + 
            "  (:action do-grind\n" + 
            "    :parameters (?x - part ?m - grinder ?oldsurface - surface\n" + 
            "                 ?oldcolour - acolour \n" + 
            "                 ?oldtreatment ?newtreatment - treatmentstatus) \n" + 
            "    :precondition (and \n" + 
            "            (available ?x)\n" + 
            "            (surface_condition ?x ?oldsurface)\n" + 
            "            (is_smooth ?oldsurface)\n" + 
            "            (colour ?x ?oldcolour)\n" + 
            "            (treatment ?x ?oldtreatment)\n" + 
            "            (grind_treatment_change ?oldtreatment ?newtreatment))\n" + 
            "    :effect (and\n" + 
            "            (not (surface_condition ?x ?oldsurface))\n" + 
            "            (surface_condition ?x verysmooth)\n" + 
            "            (not (treatment ?x ?oldtreatment))\n" + 
            "            (treatment ?x ?newtreatment)\n" + 
            "            (not (colour ?x ?oldcolour))\n" + 
            "            (colour ?x natural)))\n" + 
            "\n" + 
            "  (:action do-plane\n" + 
            "    :parameters (?x - part ?m - planer ?oldsurface - surface\n" + 
            "                 ?oldcolour - acolour ?oldtreatment - treatmentstatus) \n" + 
            "    :precondition (and \n" + 
            "            (available ?x)\n" + 
            "            (surface_condition ?x ?oldsurface)\n" + 
            "            (treatment ?x ?oldtreatment)\n" + 
            "            (colour ?x ?oldcolour))\n" + 
            "    :effect (and\n" + 
            "            (not (surface_condition ?x ?oldsurface))\n" + 
            "            (surface_condition ?x smooth)\n" + 
            "            (not (treatment ?x ?oldtreatment))\n" + 
            "            (treatment ?x untreated)\n" + 
            "            (not (colour ?x ?oldcolour))\n" + 
            "            (colour ?x natural)))\n" + 
            "\n" + 
            "  (:action load-highspeedsaw\n" + 
            "    :parameters (?b - board ?m - highspeedsaw)\n" + 
            "    :precondition (and\n" + 
            "            (empty ?m)\n" + 
            "            (available ?b))\n" + 
            "    :effect (and\n" + 
            "            (not (available ?b))\n" + 
            "            (not (empty ?m))\n" + 
            "            (in_highspeedsaw ?b ?m)))\n" + 
            "            \n" + 
            "  (:action unload-highspeedsaw\n" + 
            "    :parameters (?b - board ?m - highspeedsaw)\n" + 
            "    :precondition (in_highspeedsaw ?b ?m)\n" + 
            "    :effect (and\n" + 
            "            (available ?b)\n" + 
            "            (not (in_highspeedsaw ?b ?m))\n" + 
            "            (empty ?m)))\n" + 
            "            \n" + 
            "  (:action cut-board-small\n" + 
            "    :parameters (?b - board ?p - part ?m - highspeedsaw ?w - awood\n" + 
            "                 ?surface - surface ?size_before ?size_after - aboardsize)\n" + 
            "    :precondition (and\n" + 
            "            (unused ?p)\n" + 
            "            (goalsize ?p small)\n" + 
            "            (in_highspeedsaw ?b ?m)\n" + 
            "            (wood ?b ?w)\n" + 
            "            (surface_condition ?b ?surface)\n" + 
            "            (boardsize ?b ?size_before)\n" + 
            "            (boardsize_successor ?size_after ?size_before))\n" + 
            "    :effect (and\n" + 
            "            (not (unused ?p))\n" + 
            "            (available ?p)\n" + 
            "            (wood ?p ?w)\n" + 
            "            (surface_condition ?p ?surface)\n" + 
            "            (colour ?p natural)\n" + 
            "            (treatment ?p untreated)\n" + 
            "            (boardsize ?b ?size_after)))\n" + 
            "\n" + 
            "  (:action cut-board-medium\n" + 
            "    :parameters (?b - board ?p - part ?m - highspeedsaw ?w - awood\n" + 
            "                 ?surface - surface \n" + 
            "                 ?size_before ?s1 ?size_after - aboardsize)\n" + 
            "    :precondition (and\n" + 
            "            (unused ?p)\n" + 
            "            (goalsize ?p medium)\n" + 
            "            (in_highspeedsaw ?b ?m)\n" + 
            "            (wood ?b ?w)\n" + 
            "            (surface_condition ?b ?surface)\n" + 
            "            (boardsize ?b ?size_before)\n" + 
            "            (boardsize_successor ?size_after ?s1)\n" + 
            "            (boardsize_successor ?s1 ?size_before))\n" + 
            "    :effect (and\n" + 
            "            (not (unused ?p))\n" + 
            "            (available ?p)\n" + 
            "            (wood ?p ?w)\n" + 
            "            (surface_condition ?p ?surface)\n" + 
            "            (colour ?p natural)\n" + 
            "            (treatment ?p untreated)\n" + 
            "            (boardsize ?b ?size_after)))\n" + 
            "\n" + 
            "  (:action cut-board-large\n" + 
            "    :parameters (?b - board ?p - part ?m - highspeedsaw ?w - awood\n" + 
            "                 ?surface - surface \n" + 
            "                 ?size_before ?s1 ?s2 ?size_after - aboardsize)\n" + 
            "    :precondition (and\n" + 
            "            (unused ?p)\n" + 
            "            (goalsize ?p large)\n" + 
            "            (in_highspeedsaw ?b ?m)\n" + 
            "            (wood ?b ?w)\n" + 
            "            (surface_condition ?b ?surface)\n" + 
            "            (boardsize ?b ?size_before)\n" + 
            "            (boardsize_successor ?size_after ?s1)\n" + 
            "            (boardsize_successor ?s1 ?s2)\n" + 
            "            (boardsize_successor ?s2 ?size_before))\n" + 
            "    :effect (and\n" + 
            "            (not (unused ?p))\n" + 
            "            (available ?p)\n" + 
            "            (wood ?p ?w)\n" + 
            "            (surface_condition ?p ?surface)\n" + 
            "            (colour ?p natural)\n" + 
            "            (treatment ?p untreated)\n" + 
            "            (boardsize ?b ?size_after)))\n" + 
            "\n" + 
            "  (:action do-saw-small\n" + 
            "    :parameters (?b - board ?p - part ?m - saw ?w - awood\n" + 
            "                 ?surface - surface ?size_before ?size_after - aboardsize) \n" + 
            "    :precondition (and \n" + 
            "            (unused ?p)\n" + 
            "            (goalsize ?p small)\n" + 
            "            (available ?b)\n" + 
            "            (wood ?b ?w)\n" + 
            "            (surface_condition ?b ?surface)\n" + 
            "            (boardsize ?b ?size_before)\n" + 
            "            (boardsize_successor ?size_after ?size_before))\n" + 
            "    :effect (and\n" + 
            "            (not (unused ?p))\n" + 
            "            (available ?p)\n" + 
            "            (wood ?p ?w)\n" + 
            "            (surface_condition ?p ?surface)\n" + 
            "            (colour ?p natural) \n" + 
            "            (treatment ?p untreated)\n" + 
            "            (boardsize ?b ?size_after)))\n" + 
            "\n" + 
            "  (:action do-saw-medium\n" + 
            "    :parameters (?b - board ?p - part ?m - saw ?w - awood\n" + 
            "                 ?surface - surface \n" + 
            "                 ?size_before ?s1 ?size_after - aboardsize) \n" + 
            "    :precondition (and \n" + 
            "            (unused ?p)\n" + 
            "            (goalsize ?p medium)\n" + 
            "            (available ?b)\n" + 
            "            (wood ?b ?w)\n" + 
            "            (surface_condition ?b ?surface)\n" + 
            "            (boardsize ?b ?size_before)\n" + 
            "            (boardsize_successor ?size_after ?s1)\n" + 
            "            (boardsize_successor ?s1 ?size_before))\n" + 
            "    :effect (and\n" + 
            "            (not (unused ?p))\n" + 
            "            (available ?p)\n" + 
            "            (wood ?p ?w)\n" + 
            "            (surface_condition ?p ?surface)\n" + 
            "            (colour ?p natural) \n" + 
            "            (treatment ?p untreated)\n" + 
            "            (boardsize ?b ?size_after)))\n" + 
            "\n" + 
            "  (:action do-saw-large\n" + 
            "    :parameters (?b - board ?p - part ?m - saw ?w - awood\n" + 
            "                 ?surface - surface \n" + 
            "                 ?size_before ?s1 ?s2 ?size_after - aboardsize) \n" + 
            "    :precondition (and \n" + 
            "            (unused ?p)\n" + 
            "            (goalsize ?p large)\n" + 
            "            (available ?b)\n" + 
            "            (wood ?b ?w)\n" + 
            "            (surface_condition ?b ?surface)\n" + 
            "            (boardsize ?b ?size_before)\n" + 
            "            (boardsize_successor ?size_after ?s1)\n" + 
            "            (boardsize_successor ?s1 ?s2)\n" + 
            "            (boardsize_successor ?s2 ?size_before))\n" + 
            "    :effect (and\n" + 
            "            (not (unused ?p))\n" + 
            "            (available ?p)\n" + 
            "            (wood ?p ?w)\n" + 
            "            (surface_condition ?p ?surface)\n" + 
            "            (colour ?p natural) \n" + 
            "            (treatment ?p untreated)\n" + 
            "            (boardsize ?b ?size_after)))\n" + 
            ")\n" + 
            "";

    private static String problemString = 
            "; woodworking task with 3 parts and 140% wood\n" + 
            "; Machines:\n" + 
            ";   1 grinder\n" + 
            ";   1 glazer\n" + 
            ";   1 immersion-varnisher\n" + 
            ";   1 planer\n" + 
            ";   1 highspeedsaw\n" + 
            ";   1 spray-varnisher\n" + 
            ";   1 saw\n" + 
            "; random seed: 973895\n" + 
            "\n" + 
            "(define (problem woodworking-fmap-p01)\n" + 
            "  (:domain woodworking-fmap)\n" + 
            "  (:requirements :typing )\n" + 
            "  (:objects\n" + 
            "        verysmooth smooth rough - surface\n" + 
            "        varnished glazed untreated colourfragments - treatmentstatus\n" + 
            "        natural - acolour\n" + 
            "        small medium large - apartsize\n" + 
            "    grinder0 - grinder\n" + 
            "    glazer0 - glazer\n" + 
            "    immersion-varnisher0 - immersion-varnisher\n" + 
            "    planer0 - planer\n" + 
            "    highspeedsaw0 - highspeedsaw\n" + 
            "    spray-varnisher0 - spray-varnisher\n" + 
            "    saw0 - saw\n" + 
            "    red black - acolour\n" + 
            "    pine teak - awood\n" + 
            "    p0 p1 p2 - part\n" + 
            "    b0 - board\n" + 
            "    s0 s1 s2 s3 - aboardsize\n" + 
            "  )\n" + 
            "  (:init\n" + 
            "    (grind_treatment_change varnished colourfragments)\n" + 
            "    (grind_treatment_change glazed untreated)\n" + 
            "    (grind_treatment_change untreated untreated)\n" + 
            "    (grind_treatment_change colourfragments untreated)\n" + 
            "    (is_smooth smooth)\n" + 
            "    (is_smooth verysmooth)\n" + 
            "    (boardsize_successor s0 s1)\n" + 
            "    (boardsize_successor s1 s2)\n" + 
            "    (boardsize_successor s2 s3)\n" + 
            "    (has_colour glazer0 natural)\n" + 
            "    (has_colour glazer0 red)\n" + 
            "    (has_colour immersion-varnisher0 natural)\n" + 
            "    (has_colour immersion-varnisher0 red)\n" + 
            "    (empty highspeedsaw0)\n" + 
            "    (has_colour spray-varnisher0 natural)\n" + 
            "    (has_colour spray-varnisher0 red)\n" + 
            "    (available p0)\n" + 
            "    (colour p0 red)\n" + 
            "    (wood p0 pine)\n" + 
            "    (surface_condition p0 smooth)\n" + 
            "    (treatment p0 varnished)\n" + 
            "    (goalsize p0 small)\n" + 
            "    (unused p1)\n" + 
            "    (goalsize p1 medium)\n" + 
            "    (available p2)\n" + 
            "    (colour p2 natural)\n" + 
            "    (wood p2 teak)\n" + 
            "    (surface_condition p2 verysmooth)\n" + 
            "    (treatment p2 varnished)\n" + 
            "    (goalsize p2 large)\n" + 
            "    (boardsize b0 s3)\n" + 
            "    (wood b0 pine)\n" + 
            "    (surface_condition b0 rough)\n" + 
            "    (available b0)\n" + 
            "  )\n" + 
            "  (:goal\n" + 
            "    (and\n" + 
            "      (available p0)\n" + 
            "      (colour p0 natural)\n" + 
            "      (wood p0 pine)\n" + 
            "      (available p1)\n" + 
            "      (colour p1 natural)\n" + 
            "      (wood p1 pine)\n" + 
            "      (surface_condition p1 smooth)\n" + 
            "      (treatment p1 varnished)\n" + 
            "      (available p2)\n" + 
            "      (colour p2 red)\n" + 
            "      (wood p2 teak)\n" + 
            "    )\n" + 
            "  )\n" + 
            ")\n" + 
            "";

    private static String addlString =
            "(define (problem woodworking-fmap)\n" + 
            "    (:domain woodworking-fmap)\n" + 
            "    (:agentTypes machine)\n" + 
            "\n" + 
            "(:shared-data\n" + 
            "            (unused ?obj - part)\n" + 
            "            (available ?obj - woodobj)\n" + 
            "\n" + 
            "            (surface_condition ?obj - woodobj ?surface - surface)\n" + 
            "            (treatment ?obj - part ?treatment - treatmentstatus)\n" + 
            "            (colour ?obj - part ?colour - acolour)\n" + 
            "            (wood ?obj - woodobj ?wood - awood)\n" + 
            "            (boardsize ?board - board ?size - aboardsize)\n" + 
            "            (goalsize ?part - part ?size - apartsize)\n" + 
            "            (boardsize_successor ?size1 ?size2 - aboardsize)\n" + 
            "\n" + 
            "            (in_highspeedsaw ?b - board ?m - highspeedsaw)\n" + 
            "            (empty ?m - highspeedsaw)\n" + 
            "            (has_colour ?machine - machine ?colour - acolour)\n" + 
            "            (grind_treatment_change ?old ?new - treatmentstatus)\n" + 
            "            (is_smooth ?surface - surface))\n" + 
            ")\n" + 
            "      \n" + 
            "\n" + 
            "";

    @Test
    public void test() throws IOException {
        PddlDomain domain = new PddlDomain(new StringReader(domainString));
        PddlProblem problem = new PddlProblem(domain, new StringReader(problemString));
        PddlAddl addl = new PddlAddl(new StringReader(addlString));
        
        MaProjection projection = new FmapMaProjection(problem, addl);
        
        assertEquals(7, projection.getAgents().size());
        
        PddlProblem highspeedsawProblem = projection.generateAgentProblem(new PddlName("highspeedsaw0"));

        assertEquals(30, highspeedsawProblem.objects.getBindings().size());
        assertEquals(34, highspeedsawProblem.init.positives.size());
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
