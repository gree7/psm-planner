package experiments.psm.fmap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cz.agents.dimap.Settings;
import cz.agents.dimap.Settings.InitialLandmarks;
import cz.agents.dimap.data.Elevators01Fmap;
import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class MaStripsMultiAgentProblemElevatorsTest {

    @Before
    public void setup() {
        Settings.USE_MASTRIPS = true;
        Settings.USE_INITIAL_LANDMARKS = new HashSet<>();
        Settings.USE_INITIAL_LANDMARKS.add(InitialLandmarks.RELAXED);
        Settings.MASTRIPS_PUBLIC_FACT_HAS_TO_BE_IN_EFFECT = true;
    }
    
    @Test
    public void testPublicGoals() throws Exception {

        Settings.ALLOW_PRIVATE_GOALS = false;

        MultiAgentProblem fmapMaProblem = Elevators01Fmap.getProblem();
        MultiAgentProblem maProblem = new MaStripsMultiAgentProblem(fmapMaProblem);

        assertEquals(3, maProblem.getAgents().size());
        assertTrue(maProblem.getAgents().toString(), maProblem.getAgents().containsAll(Arrays.asList( "fast0", "slow0_0", "slow1_0" )));

        PddlProblem fast0Problem = maProblem.getProblems().get("fast0");

        assertEquals(110, fast0Problem.domain.actions.size());
        assertEquals(90, getNumPublicActions(fast0Problem.domain.actions));
        assertEquals(16, fast0Problem.domain.sharedPredicates.objects.size());
        
        PddlProblem slow0Problem = maProblem.getProblems().get("slow0_0");

        assertEquals(80, slow0Problem.domain.actions.size());
        assertEquals(40, getNumPublicActions(slow0Problem.domain.actions));
        assertEquals(16, slow0Problem.domain.sharedPredicates.objects.size());
    }

    @Test
    public void testPrivateGoals() throws Exception {

        Settings.ALLOW_PRIVATE_GOALS = true;

        MultiAgentProblem fmapMaProblem = Elevators01Fmap.getProblem();
        MultiAgentProblem maProblem = new MaStripsMultiAgentProblem(fmapMaProblem);

        assertEquals(3, maProblem.getAgents().size());
        assertTrue(maProblem.getAgents().toString(), maProblem.getAgents().containsAll(Arrays.asList( "fast0", "slow0_0", "slow1_0" )));

        PddlProblem fast0Problem = maProblem.getProblems().get("fast0");

        assertEquals(111, fast0Problem.domain.actions.size());
        assertEquals(91, getNumPublicActions(fast0Problem.domain.actions));
        assertEquals(20, fast0Problem.domain.sharedPredicates.objects.size());
        
        PddlProblem slow0Problem = maProblem.getProblems().get("slow0_0");

        assertEquals(81, slow0Problem.domain.actions.size());
        assertEquals(41, getNumPublicActions(slow0Problem.domain.actions));
        assertEquals(20, slow0Problem.domain.sharedPredicates.objects.size());
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
