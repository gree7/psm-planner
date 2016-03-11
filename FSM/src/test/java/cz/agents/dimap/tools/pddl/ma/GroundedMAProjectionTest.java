package cz.agents.dimap.tools.pddl.ma;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class GroundedMAProjectionTest {

    private static PddlDomain agentPlane;
    private static PddlDomain agentTruck;
    private static PddlProblem problem;

    private static HashSet<PddlAction> planeActions;
    private static HashSet<PddlAction> truckActions;

    @BeforeClass
    public static void init() throws IOException {
        agentPlane = new PddlDomain(
            new StringReader(
                    "(define (domain plane-domain)\n" + 
                    "(:requirements :strips :typing)\n" + 
                    "(:predicates\n" + 
                    "        (at-plane-brno-T)\n" + 
                    "        (beer-in-prague-T)\n" + 
                    "        (at-plane-prague-T)\n" + 
                    "        (beer-in-ostrava-T)\n" + 
                    "        (beer-in-brno-T)\n" + 
                    "        (beer-in-plane-T)\n" + 
                    ")\n" + 
                    "\n" + 
                    "(:action fly-prague-brno-plane-prague-brno\n" + 
                    "    :parameters ()\n" + 
                    "    :precondition       (at-plane-prague-T)\n" + 
                    "    :effect (and        (at-plane-brno-T)\n" + 
                    "        (not (at-plane-prague-T))\n" + 
                    "    )\n" + 
                    ")\n" + 
                    "(:action load-plane-prague-plane-prague\n" + 
                    "    :parameters ()\n" + 
                    "    :precondition (and      (beer-in-prague-T)\n" + 
                    "        (at-plane-prague-T)\n" + 
                    "    )\n" + 
                    "    :effect (and        (not (beer-in-prague-T))\n" + 
                    "        (beer-in-plane-T)\n" + 
                    "    )\n" + 
                    ")\n" + 
                    "(:action unload-plane-prague-plane-prague\n" + 
                    "    :parameters ()\n" + 
                    "    :precondition (and      (at-plane-prague-T)\n" + 
                    "        (beer-in-plane-T)\n" + 
                    "    )\n" + 
                    "    :effect (and        (beer-in-prague-T)\n" + 
                    "        (not (beer-in-plane-T))\n" + 
                    "    )\n" + 
                    ")\n" + 
                    "(:action fly-brno-prague-plane-brno-prague\n" + 
                    "    :parameters ()\n" + 
                    "    :precondition       (at-plane-brno-T)\n" + 
                    "    :effect (and        (not (at-plane-brno-T))\n" + 
                    "        (at-plane-prague-T)\n" + 
                    "    )\n" + 
                    ")\n" + 
                    "(:action unload-plane-brno-plane-brno\n" + 
                    "    :parameters ()\n" + 
                    "    :precondition (and      (at-plane-brno-T)\n" + 
                    "        (beer-in-plane-T)\n" + 
                    "    )\n" + 
                    "    :effect (and        (beer-in-brno-T)\n" + 
                    "        (not (beer-in-plane-T))\n" + 
                    "    )\n" + 
                    ")\n" + 
                    "(:action load-plane-brno-plane-brno\n" + 
                    "    :parameters ()\n" + 
                    "    :precondition (and      (at-plane-brno-T)\n" + 
                    "        (beer-in-brno-T)\n" + 
                    "    )\n" + 
                    "    :effect (and        (beer-in-plane-T)\n" + 
                    "        (not (beer-in-brno-T))\n" + 
                    "    )\n" + 
                    ")\n" + 
                    ")"
                    ));


    agentTruck = new PddlDomain(
            new StringReader(
                    "(define (domain truck-domain)\n" + 
                    "(:requirements :strips :typing)\n" + 
                    "(:predicates\n" + 
                    "        (at-truck-ostrava-T)\n" + 
                    "        (at-truck-brno-T)\n" + 
                    "        (beer-in-ostrava-T)\n" + 
                    "        (beer-in-truck-T)\n" + 
                    "        (beer-in-brno-T)\n" + 
                    ")\n" + 
                    "\n" + 
                    "(:action drive-ostrava-brno-truck-ostrava-brno\n" + 
                    "    :parameters ()\n" + 
                    "    :precondition       (at-truck-ostrava-T)\n" + 
                    "    :effect (and        (at-truck-brno-T)\n" + 
                    "        (not (at-truck-ostrava-T))\n" + 
                    "    )\n" + 
                    ")\n" + 
                    "(:action drive-brno-ostrava-truck-brno-ostrava\n" + 
                    "    :parameters ()\n" + 
                    "    :precondition       (at-truck-brno-T)\n" + 
                    "    :effect (and        (at-truck-ostrava-T)\n" + 
                    "        (not (at-truck-brno-T))\n" + 
                    "    )\n" + 
                    ")\n" + 
                    "(:action load-truck-brno-truck-brno\n" + 
                    "    :parameters ()\n" + 
                    "    :precondition (and      (at-truck-brno-T)\n" + 
                    "        (beer-in-brno-T)\n" + 
                    "    )\n" + 
                    "    :effect (and        (beer-in-truck-T)\n" + 
                    "        (not (beer-in-brno-T))\n" + 
                    "    )\n" + 
                    ")\n" + 
                    "(:action unload-truck-brno-truck-brno\n" + 
                    "    :parameters ()\n" + 
                    "    :precondition (and      (at-truck-brno-T)\n" + 
                    "        (beer-in-truck-T)\n" + 
                    "    )\n" + 
                    "    :effect (and        (not (beer-in-truck-T))\n" + 
                    "        (beer-in-brno-T)\n" + 
                    "    )\n" + 
                    ")\n" + 
                    "(:action load-truck-ostrava-truck-ostrava\n" + 
                    "    :parameters ()\n" + 
                    "    :precondition (and      (at-truck-ostrava-T)\n" + 
                    "        (beer-in-ostrava-T)\n" + 
                    "    )\n" + 
                    "    :effect (and        (not (beer-in-ostrava-T))\n" + 
                    "        (beer-in-truck-T)\n" + 
                    "    )\n" + 
                    ")\n" + 
                    "(:action unload-truck-ostrava-truck-ostrava\n" + 
                    "    :parameters ()\n" + 
                    "    :precondition (and      (at-truck-ostrava-T)\n" + 
                    "        (beer-in-truck-T)\n" + 
                    "    )\n" + 
                    "    :effect (and        (not (beer-in-truck-T))\n" + 
                    "        (beer-in-ostrava-T)\n" + 
                    "    )\n" + 
                    ")\n" + 
                    ")"
                    ));
    
        problem = new PddlProblem(agentPlane, new StringReader(
                "(define (problem beerproblem-grounded-a2)\n" + 
                "(:domain truck-domain)\n" + 
                "(:requirements :strips :typing)\n" + 
                "(:init\n" + 
                "        (at-truck-brno-T)\n" + 
                ")\n" + 
                "\n" + 
                "(:goal\n" + 
                "        (beer-in-ostrava-T)\n" + 
                ")\n" + 
                "\n" + 
                ")\n" 
                ));

        planeActions = new HashSet<PddlAction>(agentPlane.actions);
        truckActions = new HashSet<PddlAction>(agentTruck.actions);
    }

    @Test
    public void testNoPublicAction() {
        
        Map<String, Set<PddlAction>> agentActions = new HashMap<>();
        agentActions.put("plane", planeActions);
        agentActions.put("truck", selectActions(truckActions, "drive_brno_ostrava_truck_brno_ostrava"));

        assertEquals(6, agentActions.get("plane").size());
        assertEquals(1, agentActions.get("truck").size());

        Map<String, Set<PddlAction>> projection = new GroundedMaProjection(agentActions, problem.goal).getAgentProjectedActions();
        
        assertEquals(2, projection.size());
        assertEquals(""+projection.get("plane"), 6, projection.get("plane").size());
        assertEquals(""+projection.get("truck"), 1, projection.get("truck").size());
    }

    @Test
    public void testOnePublicAction() {
        
        Map<String, Set<PddlAction>> agentActions = new HashMap<>();
        agentActions.put("plane", planeActions);
        agentActions.put("truck", selectActions(truckActions, "unload_truck_ostrava_truck_ostrava"));

        assertEquals(6, agentActions.get("plane").size());
        assertEquals(1, agentActions.get("truck").size());

        Map<String, Set<PddlAction>> projection = new GroundedMaProjection(agentActions, problem.goal).getAgentProjectedActions();
        
        assertEquals(2, projection.size());
        assertEquals(""+projection.get("plane"), 7, projection.get("plane").size());
        assertEquals(""+projection.get("truck"), 1, projection.get("truck").size());
    }

    @Test
    public void testOnePublicOnePrivateAction() {
        Map<String, Set<PddlAction>> agentActions = new HashMap<>();
        agentActions.put("plane", planeActions);
        agentActions.put("truck", selectActions(truckActions, "unload_truck_ostrava_truck_ostrava", "drive_brno_ostrava_truck_brno_ostrava"));

        assertEquals(6, agentActions.get("plane").size());
        assertEquals(2, agentActions.get("truck").size());

        Map<String, Set<PddlAction>> projection = new GroundedMaProjection(agentActions, problem.goal).getAgentProjectedActions();
        
        assertEquals(2, projection.size());
        assertEquals(""+projection.get("plane"), 7, projection.get("plane").size());
        assertEquals(""+projection.get("truck"), 2, projection.get("truck").size());
    }

    private Set<PddlAction> selectActions(HashSet<PddlAction> actions, String... names) {
        Set<PddlAction> selected = new HashSet<>();
        for (PddlAction action : actions) {
            for (String name : names) {
                if (name.equals(action.name)) {
                    selected.add(action);
                    break;
                }
            }
        }
        return selected;
    }

    @Test
    public void testAllActions() {
        
        Map<String, Set<PddlAction>> agentActions = new HashMap<>();
        agentActions.put("plane", planeActions);
        agentActions.put("truck", truckActions);

        assertEquals(6, agentActions.get("plane").size());
        assertEquals(6, agentActions.get("truck").size());

        Map<String, Set<PddlAction>> projection = new GroundedMaProjection(agentActions, problem.goal).getAgentProjectedActions();
        
        assertEquals(2, projection.size());
        assertEquals(""+projection.get("plane"), 10, projection.get("plane").size());
        assertEquals(""+projection.get("truck"), 8, projection.get("truck").size());
    }
}
