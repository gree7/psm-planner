package cz.agents.fsmplanner;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import cz.agents.dimap.psm.operator.SimpleOperator;
import cz.agents.dimap.psm.planner.causallinks.CausalLink;
import cz.agents.dimap.psm.planner.causallinks.PlanCausalLinks;

public class PlanCausalLinksTest {

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
    public void testNoLandmarks() {
        List<TestOperator> plan = Arrays.asList(
                new TestOperator("pub_op-A", true, Arrays.asList(1), Arrays.asList(2)),
                new TestOperator("pub_op-B", true, Arrays.asList(3), Arrays.asList(4)),
                new TestOperator("pub_op-C", true, Arrays.asList(5), Arrays.asList(6))
                );
        Collection<CausalLink> causalLinks = PlanCausalLinks.getCausalLinksForPlan(plan);
        
        assertEquals(3, causalLinks.size());
    }
 
    @Test
    public void testSimplePlan() {
        List<TestOperator> plan = Arrays.asList(
                new TestOperator("pub_op-A", true, Arrays.asList(1), Arrays.asList(2)),
                new TestOperator("pub_op-B", true, Arrays.asList(2), Arrays.asList(3)),
                new TestOperator("pub_op-C", true, Arrays.asList(3), Arrays.asList(4))
                );
        Collection<CausalLink> causalLinks = PlanCausalLinks.getCausalLinksForPlan(plan);
        
        assertEquals(3, causalLinks.size());
    }
   
    @Test
    public void testTwoPrecond() {
        List<TestOperator> plan = Arrays.asList(
                new TestOperator("pub_op-A", true, Arrays.asList(1), Arrays.asList(2)),
                new TestOperator("pub_op-B", true, Arrays.asList(1), Arrays.asList(3)),
                new TestOperator("pub_op-C", true, Arrays.asList(3,2), Arrays.asList(4))
                );
        Collection<CausalLink> causalLinks = PlanCausalLinks.getCausalLinksForPlan(plan);

        assertEquals(3, causalLinks.size());
    }

    @Test
    public void testComplexOnePlan1() {
        List<TestOperator> plan = Arrays.asList(
                new TestOperator("pub_op-A", true, Arrays.asList(1), Arrays.asList(2)),
                new TestOperator("pub_op-B", true, Arrays.asList(2), Arrays.asList(3)),
                new TestOperator("pub_op-C", true, Arrays.asList(3,2), Arrays.asList(4))
                );
        Collection<CausalLink> causalLinks = PlanCausalLinks.getCausalLinksForPlan(plan);
        
        assertEquals(3, causalLinks.size());
    }

    @Test
    public void testComplexOnePlan2() {
        TestOperator opA = new TestOperator("pub_op-A", true, Arrays.asList(1), Arrays.asList(2));
        List<TestOperator> plan = Arrays.asList(
                opA,
                new TestOperator("pub_op-B", true, Arrays.asList(2), Arrays.asList(3,2)),
                new TestOperator("pub_op-C", true, Arrays.asList(3), Arrays.asList(4)),
                new TestOperator("pub_op-D", true, Arrays.asList(1), Arrays.asList(5,6)),
                new TestOperator("pub_op-E", true, Arrays.asList(5), Arrays.asList(1,5,7)),
                opA,
                new TestOperator("pub_op-F", true, Arrays.asList(4,6,2), Arrays.asList(8))
                );
        Collection<CausalLink> causalLinks = PlanCausalLinks.getCausalLinksForPlan(plan);
        
        assertEquals(7, causalLinks.size());
    }

    @Test
    public void testSimplePlanIntAct() {
        List<TestOperator> plan = Arrays.asList(
                new TestOperator("pub_op-A", true, Arrays.asList(1), Arrays.asList(2)),
                new TestOperator("int_op-B", false, Arrays.asList(2), Arrays.asList(3)),
                new TestOperator("pub_op-C", true, Arrays.asList(3), Arrays.asList(4))
                );
        Collection<CausalLink> causalLinks = PlanCausalLinks.getCausalLinksForPlan(plan);
        
        assertEquals(2, causalLinks.size());
    }

    @Test
    public void testComplexOnePlan2IntAct() {
        TestOperator opA = new TestOperator("pub_op-A", true, Arrays.asList(1), Arrays.asList(2));
        List<TestOperator> plan = Arrays.asList(
                opA,
                new TestOperator("int_op-B", false, Arrays.asList(2), Arrays.asList(3,2)),
                new TestOperator("pub_op-C", true, Arrays.asList(3), Arrays.asList(4)),
                new TestOperator("pub_op-D", true, Arrays.asList(1), Arrays.asList(5,6)),
                new TestOperator("int_op-E", false, Arrays.asList(5), Arrays.asList(1,5,7)),
                opA,
                new TestOperator("pub_op-F", true, Arrays.asList(4,6,2), Arrays.asList(8))
                );
        Collection<CausalLink> causalLinks = PlanCausalLinks.getCausalLinksForPlan(plan);

        assertEquals(5, causalLinks.size());
    }

    @Test
    public void testMultiPreconds() {
        List<TestOperator> plan = Arrays.asList(
                new TestOperator("pub_op-A", true, Arrays.asList(1), Arrays.asList(2)),
                new TestOperator("pub_op-B", true, Arrays.asList(2), Arrays.asList(3)),
                new TestOperator("pub_op-C", true, Arrays.asList(2, 3), Arrays.asList(4))
                );
        Collection<CausalLink> causalLinks = PlanCausalLinks.getCausalLinksForPlan(plan);
        
        assertEquals(3, causalLinks.size());
    }
}
