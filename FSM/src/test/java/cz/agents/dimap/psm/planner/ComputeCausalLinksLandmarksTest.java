package cz.agents.dimap.psm.planner;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import cz.agents.dimap.Settings;
import cz.agents.dimap.landmarks.Landmark;
import cz.agents.dimap.psm.operator.SimpleOperator;
import cz.agents.dimap.psm.planner.causallinks.CausalLinks;

public class ComputeCausalLinksLandmarksTest {

    @BeforeClass
    public static void setup() {
        Settings.DOT_LANDMARKS = false;
    }
    
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

    Collection<Landmark> computeCausalLinksLandmarks(CausalLinks causalLinks) {
        return PsmPlannerAgent.computeCausalLinksLandmarks(causalLinks, "agent", 1, new File("."), 1);
    }
    
    @Test
    public void testOnePlan() {
        CausalLinks causalLinks = new CausalLinks();
        causalLinks.addPlan(Arrays.asList(
                new TestOperator("pub_op-A", true, Arrays.asList(1), Arrays.asList(2)),
                new TestOperator("pub_op-B", true, Arrays.asList(2), Arrays.asList(3)),
                new TestOperator("pub_op-C", true, Arrays.asList(3), Arrays.asList(4))
                ));

        Collection<Landmark> landmarks = computeCausalLinksLandmarks(causalLinks);
        assertEquals(3, landmarks.size());
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

        Collection<Landmark> landmarks = computeCausalLinksLandmarks(causalLinks);
        assertEquals(3, landmarks.size());
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

        Collection<Landmark> landmarks = computeCausalLinksLandmarks(causalLinks);
        assertEquals(3, landmarks.size());
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

        Collection<Landmark> landmarks = computeCausalLinksLandmarks(causalLinks);
        assertEquals(5, landmarks.size());
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

        Collection<Landmark> landmarks = computeCausalLinksLandmarks(causalLinks);
        
        assertEquals(6, landmarks.size());
    }

    @Test
    public void testMultiPreconds() {
        CausalLinks causalLinks = new CausalLinks();

        causalLinks.addPlan(Arrays.asList(
                new TestOperator("pub_a", true, Arrays.asList(1), Arrays.asList(2)),
                new TestOperator("pub_b", true, Arrays.asList(2), Arrays.asList(3)),
                new TestOperator("pub_c", true, Arrays.asList(2, 3), Arrays.asList(4))
                ));

        Collection<Landmark> landmarks = computeCausalLinksLandmarks(causalLinks);
        assertEquals(3, landmarks.size());
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

        Collection<Landmark> landmarks = computeCausalLinksLandmarks(causalLinks);
        assertEquals(4, landmarks.size());
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

        Collection<Landmark> landmarks = computeCausalLinksLandmarks(causalLinks);
        assertEquals(6, landmarks.size());
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

        Collection<Landmark> landmarks = computeCausalLinksLandmarks(causalLinks);
        assertEquals(7, landmarks.size());
    }
}
