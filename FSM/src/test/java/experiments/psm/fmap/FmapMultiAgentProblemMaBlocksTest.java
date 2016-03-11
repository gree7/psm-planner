package experiments.psm.fmap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cz.agents.dimap.Settings;
import cz.agents.dimap.data.MaBlocksworld04Fmap;
import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class FmapMultiAgentProblemMaBlocksTest {

    @Test
    public void testPublicGoals() throws Exception {

        Settings.ALLOW_PRIVATE_GOALS = false;

        MultiAgentProblem maProblem = MaBlocksworld04Fmap.getProblem();

        assertEquals(4, maProblem.getAgents().size());
        assertTrue(maProblem.getAgents().toString(), maProblem.getAgents().containsAll(Arrays.asList( "r0", "r1", "r2", "r3" )));

        PddlProblem r0Problem = maProblem.getProblems().get("r0");

        assertEquals(4, r0Problem.domain.actions.size());
        assertEquals(4, getNumPublicActions(r0Problem.domain.actions));
        assertEquals(4, r0Problem.domain.sharedPredicateNames.size());
        
        PddlProblem r1Problem = maProblem.getProblems().get("r1");

        assertEquals(4, r1Problem.domain.actions.size());
        assertEquals(4, getNumPublicActions(r1Problem.domain.actions));
        assertEquals(4, r1Problem.domain.sharedPredicateNames.size());
    }

    @Test
    public void testPrivateGoals() throws Exception {

        Settings.ALLOW_PRIVATE_GOALS = true;

        MultiAgentProblem maProblem = MaBlocksworld04Fmap.getProblem();

        assertEquals(4, maProblem.getAgents().size());
        assertTrue(maProblem.getAgents().toString(), maProblem.getAgents().containsAll(Arrays.asList( "r0", "r1", "r2", "r3" )));

        PddlProblem r0Problem = maProblem.getProblems().get("r0");

        assertEquals(5, r0Problem.domain.actions.size());
        assertEquals(5, getNumPublicActions(r0Problem.domain.actions));
        assertEquals(8, r0Problem.domain.sharedPredicateNames.size());
        
        PddlProblem r1Problem = maProblem.getProblems().get("r1");

        assertEquals(5, r1Problem.domain.actions.size());
        assertEquals(5, getNumPublicActions(r1Problem.domain.actions));
        assertEquals(8, r1Problem.domain.sharedPredicateNames.size());
    }

    private int getNumPublicActions(List<PddlAction> actions) {
        int publicNum = 0;
        for (PddlAction action : actions) {
            if (action.isPublic()) {
                publicNum++;
            }
        }
        return publicNum;
    }
}
