package cz.agents.dimap.tools.pddl.ma;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import cz.agents.dimap.Settings;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class IpcMaProjectionElevators01Test {

    private static String domainString =
            "(define (domain elevators)\n" + 
            "  (:requirements :typing)\n" + 
            "  (:types   elevator - object \n" + 
            "            slow-elevator fast-elevator - elevator\n" + 
            "            passenger - object\n" + 
            "            count - object\n" + 
            "         )\n" + 
            "\n" + 
            "(:predicates \n" + 
            "    (passenger_at ?person - passenger ?floor - count)\n" + 
            "    (boarded ?person - passenger ?lift - elevator)\n" + 
            "    (lift_at ?lift - elevator ?floor - count)\n" + 
            "    (reachable_floor ?lift - elevator ?floor - count)\n" + 
            "    (above ?floor1 - count ?floor2 - count)\n" + 
            "    (passengers ?lift - elevator ?n - count)\n" + 
            "    (can_hold ?lift - elevator ?n - count)\n" + 
            "    (next ?n1 - count ?n2 - count)\n" + 
            ")\n" + 
            "\n" + 
            "(:action move-up-slow\n" + 
            "  :parameters (?lift - slow-elevator ?f1 - count ?f2 - count )\n" + 
            "  :precondition (and (lift_at ?lift ?f1) (above ?f1 ?f2 ) (reachable_floor ?lift ?f2) )\n" + 
            "  :effect (and (lift_at ?lift ?f2) (not (lift_at ?lift ?f1)) ))\n" + 
            "\n" + 
            "(:action move-down-slow\n" + 
            "  :parameters (?lift - slow-elevator ?f1 - count ?f2 - count )\n" + 
            "  :precondition (and (lift_at ?lift ?f1) (above ?f2 ?f1 ) (reachable_floor ?lift ?f2) )\n" + 
            "  :effect (and (lift_at ?lift ?f2) (not (lift_at ?lift ?f1)) ))\n" + 
            "\n" + 
            "(:action move-up-fast\n" + 
            "  :parameters (?lift - fast-elevator ?f1 - count ?f2 - count )\n" + 
            "  :precondition (and (lift_at ?lift ?f1) (above ?f1 ?f2 ) (reachable_floor ?lift ?f2) )\n" + 
            "  :effect (and (lift_at ?lift ?f2) (not (lift_at ?lift ?f1)) ))\n" + 
            "\n" + 
            "(:action move-down-fast\n" + 
            "  :parameters (?lift - fast-elevator ?f1 - count ?f2 - count )\n" + 
            "  :precondition (and (lift_at ?lift ?f1) (above ?f2 ?f1 ) (reachable_floor ?lift ?f2) )\n" + 
            "  :effect (and (lift_at ?lift ?f2) (not (lift_at ?lift ?f1)) ))\n" + 
            "\n" + 
            "(:action board\n" + 
            "  :parameters (?p - passenger ?lift - elevator ?f - count ?n1 - count ?n2 - count)\n" + 
            "  :precondition (and  (lift_at ?lift ?f) (passenger_at ?p ?f) (passengers ?lift ?n1) (next ?n1 ?n2) (can_hold ?lift ?n2) )\n" + 
            "  :effect (and (not (passenger_at ?p ?f)) (boarded ?p ?lift) (not (passengers ?lift ?n1)) (passengers ?lift ?n2) ))\n" + 
            "\n" + 
            "(:action leave \n" + 
            "  :parameters (?p - passenger ?lift - elevator ?f - count ?n1 - count ?n2 - count)\n" + 
            "  :precondition (and  (lift_at ?lift ?f) (boarded ?p ?lift) (passengers ?lift ?n1) (next ?n2 ?n1) )\n" + 
            "  :effect (and (passenger_at ?p ?f) (not (boarded ?p ?lift)) (not (passengers ?lift ?n1)) (passengers ?lift ?n2) ))\n" + 
            "  \n" + 
            ")\n" + 
            "";

    private static String problemString = 
            "(define (problem elevators-p01)\n" + 
            "(:domain elevators)\n" + 
            "(:requirements :typing)\n" + 
            "\n" + 
            "(:objects \n" + 
            "n0 n1 n2 n3 n4 n5 n6 n7 n8  - count\n" + 
            "p0 p1 p2 p3  - passenger\n" + 
            "fast0 fast1  - fast-elevator\n" + 
            "slow0 slow1 - slow-elevator\n" + 
            ")\n" + 
            "\n" + 
            "(:init\n" + 
            "(next n0 n1) (next n1 n2) (next n2 n3) (next n3 n4) (next n4 n5) (next n5 n6) (next n6 n7) (next n7 n8) \n" + 
            "\n" + 
            "(above n0 n1) (above n0 n2) (above n0 n3) (above n0 n4) (above n0 n5) (above n0 n6) (above n0 n7) (above n0 n8) \n" + 
            "(above n1 n2) (above n1 n3) (above n1 n4) (above n1 n5) (above n1 n6) (above n1 n7) (above n1 n8) \n" + 
            "(above n2 n3) (above n2 n4) (above n2 n5) (above n2 n6) (above n2 n7) (above n2 n8) \n" + 
            "(above n3 n4) (above n3 n5) (above n3 n6) (above n3 n7) (above n3 n8) \n" + 
            "(above n4 n5) (above n4 n6) (above n4 n7) (above n4 n8) \n" + 
            "(above n5 n6) (above n5 n7) (above n5 n8) \n" + 
            "(above n6 n7) (above n6 n8) \n" + 
            "(above n7 n8) \n" + 
            "\n" + 
            "(lift_at fast0 n8)\n" + 
            "(passengers fast0 n0)\n" + 
            "(can_hold fast0 n1) (can_hold fast0 n2) (can_hold fast0 n3) \n" + 
            "(reachable_floor fast0 n0)(reachable_floor fast0 n2)(reachable_floor fast0 n4)(reachable_floor fast0 n6)(reachable_floor fast0 n8)\n" + 
            "\n" + 
            "(lift_at fast1 n6)\n" + 
            "(passengers fast1 n0)\n" + 
            "(can_hold fast1 n1) (can_hold fast1 n2) (can_hold fast1 n3) \n" + 
            "(reachable_floor fast1 n0)(reachable_floor fast1 n2)(reachable_floor fast1 n4)(reachable_floor fast1 n6)(reachable_floor fast1 n8)\n" + 
            "\n" + 
            "(lift_at slow0 n4)\n" + 
            "(passengers slow0 n0)\n" + 
            "(can_hold slow0 n1) (can_hold slow0 n2) \n" + 
            "(reachable_floor slow0 n0)(reachable_floor slow0 n1)(reachable_floor slow0 n2)(reachable_floor slow0 n3)(reachable_floor slow0 n4)\n" + 
            "\n" + 
            "(lift_at slow1 n4)\n" + 
            "(passengers slow1 n0)\n" + 
            "(can_hold slow1 n1) (can_hold slow1 n2) \n" + 
            "(reachable_floor slow1 n4)(reachable_floor slow1 n5)(reachable_floor slow1 n6)(reachable_floor slow1 n7)(reachable_floor slow1 n8)\n" + 
            "\n" + 
            "(passenger_at p0 n3)\n" + 
            "(passenger_at p1 n1)\n" + 
            "(passenger_at p2 n7)\n" + 
            "(passenger_at p3 n0)\n" + 
            "\n" + 
            ")\n" + 
            "\n" + 
            "(:goal\n" + 
            "(and\n" + 
            "(passenger_at p0 n4)\n" + 
            "(passenger_at p1 n5)\n" + 
            "(passenger_at p2 n6)\n" + 
            "(passenger_at p3 n2)\n" + 
            "))\n" + 
            ")\n" + 
            "";

    private static String addlString =
            "(define (problem elevators)\n" + 
            "    (:domain elevators)\n" + 
            "    (:agentTypes elevator)\n" + 
            ")\n" + 
            "";

    @Test
    public void test() throws IOException {
        Settings.ALLOW_PRIVATE_GOALS = false;
        Settings.USE_MASTRIPS = false;
        
        PddlDomain domain = new PddlDomain(new StringReader(domainString));
        PddlProblem problem = new PddlProblem(domain, new StringReader(problemString));
        PddlAddl addl = new PddlAddl(new StringReader(addlString));
        
        MaProjection projection = new IpcMaProjection(problem, addl);
        
        assertEquals(4, projection.getAgents().size());
        
        PddlProblem fast0Problem = projection.generateAgentProblem(new PddlName("fast0"));

        assertEquals("fast0-domain", fast0Problem.domainName.toString());
        assertEquals("fast0-problem", fast0Problem.problemName.toString());

        assertEquals(17, fast0Problem.objects.getBindings().size());
        assertEquals(86, fast0Problem.init.positives.size());
        assertEquals(0, fast0Problem.init.negatives.size());
        assertEquals(4, fast0Problem.goal.positives.size());
        assertEquals(0, fast0Problem.goal.negatives.size());

        PddlDomain fast0Domain = fast0Problem.domain;

        assertEquals("elevators", domain.domainName.toString());
        
        assertTrue(fast0Domain.requirements.toString(), fast0Domain.requirements.contains(new PddlName(":typing")));
        assertEquals(140, fast0Domain.actions.size());
        assertFalse(fast0Problem.domainName.name+" contains negative precondition!", hasNegativePrecondition(fast0Domain));
        assertEquals(8, fast0Domain.predicateTypes.size());
        assertEquals(0, fast0Domain.sharedPredicateNames.size());
        assertEquals(34, fast0Domain.sharedPredicates.objects.size());
        PddlProblem slow0Problem = projection.generateAgentProblem(new PddlName("slow0"));

        assertEquals("slow0-domain", slow0Problem.domainName.toString());
        assertEquals("slow0-problem", slow0Problem.problemName.toString());

        assertEquals(17, slow0Problem.objects.getBindings().size());
        assertEquals(86, slow0Problem.init.positives.size());
        assertEquals(0, slow0Problem.init.negatives.size());
        assertEquals(4, slow0Problem.goal.positives.size());
        assertEquals(0, slow0Problem.goal.negatives.size());

        PddlDomain slow0Domain = slow0Problem.domain;

        assertEquals("elevators", domain.domainName.toString());
        
        assertTrue(slow0Domain.requirements.toString(), slow0Domain.requirements.contains(new PddlName(":typing")));
        assertEquals(100, slow0Domain.actions.size());
        assertFalse(slow0Problem.domainName.name+" contains negative precondition!", hasNegativePrecondition(slow0Domain));
        assertEquals(8, slow0Domain.predicateTypes.size());
        assertEquals(0, slow0Domain.sharedPredicateNames.size());
        assertEquals(34, fast0Domain.sharedPredicates.objects.size());
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
