package cz.agents.dimap.planset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cz.agents.dimap.data.SatellitesIpc01NotGrounded;
import cz.agents.dimap.psm.operator.Operator;
import cz.agents.dimap.psm.operator.PddlOperatorFactory;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlActionInstance;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class SimplePlanSetTest {

    private static PddlProblem problem = SatellitesIpc01NotGrounded.createProblem().getProblems().get("satellite0");

    private final static List<Operator> plan1 = Arrays.asList(
            createOperator("turn_to satellite1 star4 star0"), 
            createOperator("take_image satellite1 star4 instrument0 spectrograph2") 
            );

    private final static List<Operator> plan1Reverted = Arrays.asList(
            createOperator("take_image satellite1 star4 instrument0 spectrograph2"), 
            createOperator("turn_to satellite1 star4 star0") 
            );

    private final static List<Operator> planLong1 = Arrays.asList(
            createOperator("turn_to satellite1 star4 star0"), 
            createOperator("take_image satellite1 star4 instrument0 spectrograph2"), 
            createOperator("turn_to satellite0 star3 star4"), 
            createOperator("turn_to satellite0 star0 star4"), 
            createOperator("take_image satellite0 star3 instrument0 infrared0"), 
            createOperator("turn_to satellite1 phenomenon7 star0"), 
            createOperator("take_image satellite1 phenomenon7 instrument0 spectrograph2"), 
            createOperator("turn_to satellite1 phenomenon5 star0"),
            createOperator("take_image satellite1 phenomenon5 instrument0 spectrograph2")
            );

    private final static List<Operator> planLong1a = Arrays.asList(
            createOperator("turn_to satellite1 star4 star0"), 
            createOperator("take_image satellite1 star4 instrument0 spectrograph2"), 
            createOperator("turn_to satellite0 star3 star4"), 
            createOperator("turn_to satellite0 star0 star4"), 
            createOperator("take_image satellite0 star3 instrument0 infrared0"), 
            createOperator("turn_to satellite1 phenomenon7 star0"), 
            createOperator("take_image satellite1 phenomenon7 instrument0 spectrograph2"), 
            createOperator("turn_to satellite1 phenomenon5 star0"),
            createOperator("take_image satellite1 phenomenon5 instrument0 spectrograph2")
            );

    @Test
    public void testIdentity() {
        SimplePlanSet set1 = new SimplePlanSet();
        set1.addPlan(planLong1);

        SimplePlanSet set2 = new SimplePlanSet();
        set2.addPlan(planLong1);
        
        PlanSet<Operator> result = set1.intersectWith(set2);
        assertEquals(set1, result);
        assertEquals(set2, result);
    }

    @Test
    public void testIdentityDifferentObjects() {
        SimplePlanSet set1 = new SimplePlanSet();
        set1.addPlan(planLong1);

        SimplePlanSet set2 = new SimplePlanSet();
        set2.addPlan(planLong1a);
        
        PlanSet<Operator> result = set1.intersectWith(set2);
        assertEquals(set1, result);
        assertEquals(set2, result);
    }

    @Test
    public void testDiffPlans() {
        SimplePlanSet set1 = new SimplePlanSet();
        set1.addPlan(plan1);

        SimplePlanSet set2 = new SimplePlanSet();
        set2.addPlan(plan1Reverted);
        
        PlanSet<Operator> result = set1.intersectWith(set2);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSeveralPlans() {
        SimplePlanSet set1 = new SimplePlanSet();
        set1.addPlan(plan1);
        set1.addPlan(plan1Reverted);
        set1.addPlan(planLong1);

        SimplePlanSet set2 = new SimplePlanSet();
        set2.addPlan(plan1Reverted);
        
        PlanSet<Operator> result = set1.intersectWith(set2);
        assertEquals(set2, result);
    }

    @Test
    public void testSeveralPlans2() {
        SimplePlanSet set1 = new SimplePlanSet();
        set1.addPlan(plan1);
        set1.addPlan(plan1Reverted);
        set1.addPlan(planLong1);

        SimplePlanSet set2 = new SimplePlanSet();
        set2.addPlan(plan1Reverted);
        set2.addPlan(planLong1a);
        
        PlanSet<Operator> result = set1.intersectWith(set2);
        assertEquals(set2, result);
    }

    @Test
    public void testSeveralPlans3() {
        SimplePlanSet set1 = new SimplePlanSet();
        set1.addPlan(plan1);
        set1.addPlan(planLong1);

        SimplePlanSet set2 = new SimplePlanSet();
        set2.addPlan(plan1Reverted);
        set2.addPlan(planLong1a);

        SimplePlanSet result = set1.intersectWith(set2);
        assertEquals(1, result.plans.size());
    }

    @Test
    public void testGetRandomPlan() {
        SimplePlanSet set1 = new SimplePlanSet();
        set1.addPlan(plan1);
        set1.addPlan(planLong1);

        SimplePlanSet set2 = new SimplePlanSet();
        set2.addPlan(plan1Reverted);
        set2.addPlan(planLong1a);

        PlanSet<Operator> result = set1.intersectWith(set2);
        List<String> expected = Arrays.asList(
                "turn_to satellite1 star4 star0",
                "take_image satellite1 star4 spectrograph2",
                "turn_to satellite0 star3 star4",
                "turn_to satellite0 star0 star4",
                "take_image satellite0 star3 infrared0",
                "turn_to satellite1 phenomenon7 star0",
                "take_image satellite1 phenomenon7 spectrograph2",
                "turn_to satellite1 phenomenon5 star0",
                "take_image satellite1 phenomenon5 spectrograph2");

        assertEquals(expected, result.getRandomPlan());
    }

    private static Operator createOperator(String string) {
        PddlAction action = PddlActionInstance.parsePlannedPublicAction(string, problem);
        return PddlOperatorFactory.createOperator(action, null);
    }
}
