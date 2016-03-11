package cz.agents.fsmplanner;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import cz.agents.dimap.psm.operator.SimpleOperator;
import cz.agents.dimap.psm.planner.causallinks.CausalLinks;

public class CausalLinksTest {

    class TestOperator extends SimpleOperator {

        private Set<Integer> preconditions;
        private Set<Integer> effects;

        public TestOperator(String name, boolean isPublic, List<Integer> preconditions, List<Integer> effects) {
            super(name, isPublic);
            this.preconditions = new HashSet<>(preconditions);
            this.effects = new HashSet<>(effects);
        }
        
        @Override
        public Set<Integer> getPreconditions() {
            return preconditions;
        }

        @Override
        public Set<Integer> getEffects() {
            return effects;
        }
    }
    
    @Test
    public void testOnePlan() {
        CausalLinks causalLinks = new CausalLinks();
        causalLinks.addPlan(Arrays.asList(
                new TestOperator("pub_op-A", true, Arrays.asList(1), Arrays.asList(2)),
                new TestOperator("pub_op-B", true, Arrays.asList(2), Arrays.asList(3)),
                new TestOperator("pub_op-C", true, Arrays.asList(3), Arrays.asList(4))
                ));
        
        assertEquals(4, causalLinks.getNumberOfStates());
        assertEquals(3, causalLinks.getNumberOfTransitions());
    }

    @Test
    public void testTwoSamePlans() {
        CausalLinks causalLinks = new CausalLinks();
        causalLinks.addPlan(Arrays.asList(
                new TestOperator("pub_op-A", true, Arrays.asList(1), Arrays.asList(2)),
                new TestOperator("pub_op-B", true, Arrays.asList(2), Arrays.asList(3)),
                new TestOperator("pub_op-C", true, Arrays.asList(3), Arrays.asList(4))
                ));
        causalLinks.addPlan(Arrays.asList(
                new TestOperator("pub_op-A", true, Arrays.asList(1), Arrays.asList(2)),
                new TestOperator("pub_op-B", true, Arrays.asList(2), Arrays.asList(3)),
                new TestOperator("pub_op-C", true, Arrays.asList(3), Arrays.asList(4))
                ));
        
        assertEquals(4, causalLinks.getNumberOfStates());
        assertEquals(3, causalLinks.getNumberOfTransitions());
    }

    @Test
    public void testTwoAlmostSameSimilarPlans() {
        CausalLinks causalLinks = new CausalLinks();
        causalLinks.addPlan(Arrays.asList(
                new TestOperator("pub_op-A", true, Arrays.asList(1), Arrays.asList(2)),
                new TestOperator("pub_op-B", true, Arrays.asList(2), Arrays.asList(3)),
                new TestOperator("pub_op-C", true, Arrays.asList(3), Arrays.asList(4))
                ));
        causalLinks.addPlan(Arrays.asList(
                new TestOperator("pub_op-A", true, Arrays.asList(1), Arrays.asList(2)),
                new TestOperator("pub_op-B", true, Arrays.asList(2), Arrays.asList(4)),
                new TestOperator("pub_op-C", true, Arrays.asList(4), Arrays.asList(5))
                ));
        
        assertEquals(4, causalLinks.getNumberOfStates());
        assertEquals(3, causalLinks.getNumberOfTransitions());
    }

    @Test
    public void testTwoSimilarPlans() {
        CausalLinks causalLinks = new CausalLinks();
        causalLinks.addPlan(Arrays.asList(
                new TestOperator("pub_op-A", true, Arrays.asList(1), Arrays.asList(2)),
                new TestOperator("pub_op-B", true, Arrays.asList(2), Arrays.asList(3)),
                new TestOperator("pub_op-C", true, Arrays.asList(3), Arrays.asList(4))
                ));
        causalLinks.addPlan(Arrays.asList(
                new TestOperator("pub_op-A", true, Arrays.asList(1), Arrays.asList(2)),
                new TestOperator("pub_op-D", true, Arrays.asList(2), Arrays.asList(3)),
                new TestOperator("pub_op-C", true, Arrays.asList(3), Arrays.asList(4))
                ));
        
        assertEquals(6, causalLinks.getNumberOfStates());
        assertEquals(5, causalLinks.getNumberOfTransitions());
    }

    @Test
    public void testTwoDifferentPlans() {
        CausalLinks causalLinks = new CausalLinks();
        causalLinks.addPlan(Arrays.asList(
                new TestOperator("pub_op-A", true, Arrays.asList(1), Arrays.asList(2)),
                new TestOperator("pub_op-B", true, Arrays.asList(2), Arrays.asList(3)),
                new TestOperator("pub_op-C", true, Arrays.asList(3), Arrays.asList(4))
                ));
        causalLinks.addPlan(Arrays.asList(
                new TestOperator("pub_op-D", true, Arrays.asList(11), Arrays.asList(12)),
                new TestOperator("pub_op-E", true, Arrays.asList(12), Arrays.asList(13)),
                new TestOperator("pub_op-F", true, Arrays.asList(13), Arrays.asList(14))
                ));
        
        assertEquals(7, causalLinks.getNumberOfStates());
        assertEquals(6, causalLinks.getNumberOfTransitions());
    }
    
    @Test
    public void testMultiPreconds() {
        CausalLinks causalLinks = new CausalLinks();

        causalLinks.addPlan(Arrays.asList(
                new TestOperator("pub_a", true, Arrays.asList(1), Arrays.asList(2)),
                new TestOperator("pub_b", true, Arrays.asList(2), Arrays.asList(3)),
                new TestOperator("pub_c", true, Arrays.asList(2, 3), Arrays.asList(4))
                ));
        
        assertEquals(4, causalLinks.getNumberOfStates());
        assertEquals(3, causalLinks.getNumberOfTransitions());
    }

    @Test
    public void testMultiPrecondsLonger() {
        CausalLinks causalLinks = new CausalLinks();

        causalLinks.addPlan(Arrays.asList(
                new TestOperator("pub_a", true, Arrays.asList(1), Arrays.asList(2)),
                new TestOperator("pub_b", true, Arrays.asList(2), Arrays.asList(3)),
                new TestOperator("pub_c", true, Arrays.asList(3), Arrays.asList(4)),
                new TestOperator("pub_d", true, Arrays.asList(2, 4), Arrays.asList(5))
                ));
        
        assertEquals(5, causalLinks.getNumberOfStates());
        assertEquals(4, causalLinks.getNumberOfTransitions());
    }

    @Test
    public void testTwoPaths() {
        CausalLinks causalLinks = new CausalLinks();

        causalLinks.addPlan(Arrays.asList(
                new TestOperator("pub_a1", true, Arrays.asList(1), Arrays.asList(10)),
                new TestOperator("pub_a2", true, Arrays.asList(10), Arrays.asList(11)),
                new TestOperator("pub_a3", true, Arrays.asList(11), Arrays.asList(12)),
                new TestOperator("pub_b1", true, Arrays.asList(1), Arrays.asList(20)),
                new TestOperator("pub_b2", true, Arrays.asList(20), Arrays.asList(21)),
                new TestOperator("pub_b3", true, Arrays.asList(21), Arrays.asList(22))
                ));

        assertEquals(7, causalLinks.getNumberOfStates());
        assertEquals(6, causalLinks.getNumberOfTransitions());
    }

    @Test
    public void testTwoPathsAndGoalAction() {
        CausalLinks causalLinks = new CausalLinks();

        causalLinks.addPlan(Arrays.asList(
                new TestOperator("pub_a1", true, Arrays.asList(1), Arrays.asList(10)),
                new TestOperator("pub_a2", true, Arrays.asList(10), Arrays.asList(11)),
                new TestOperator("pub_a3", true, Arrays.asList(11), Arrays.asList(12)),
                new TestOperator("pub_b1", true, Arrays.asList(1), Arrays.asList(20)),
                new TestOperator("pub_b2", true, Arrays.asList(20), Arrays.asList(21)),
                new TestOperator("pub_b3", true, Arrays.asList(21), Arrays.asList(22)),
                new TestOperator("pub_c", true, Arrays.asList(12, 22), Arrays.asList(30))
                ));

        assertEquals(8, causalLinks.getNumberOfStates());
        assertEquals(8, causalLinks.getNumberOfTransitions());
    }
}
