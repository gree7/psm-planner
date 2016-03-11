package experiments.psm.fmap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cz.agents.dimap.Settings;
import cz.agents.dimap.Settings.InitialLandmarks;
import cz.agents.dimap.data.Logistics01Fmap;
import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class MaStripsMultiAgentProblemLogisticsTest {

    @Before
    public void setup() {
        Settings.USE_INITIAL_LANDMARKS = new HashSet<>();
        Settings.USE_INITIAL_LANDMARKS.add(InitialLandmarks.RELAXED);
        Settings.MASTRIPS_PUBLIC_FACT_HAS_TO_BE_IN_EFFECT = true;
    }
    
    @Test(timeout=100000)
    public void testPublicGoals() throws Exception {

        Settings.ALLOW_PRIVATE_GOALS = false;

        MultiAgentProblem fmapMaProblem = Logistics01Fmap.getProblem();
        MultiAgentProblem maProblem = new MaStripsMultiAgentProblem(fmapMaProblem);

        assertEquals(3, maProblem.getAgents().size());
        assertTrue(maProblem.getAgents().toString(), maProblem.getAgents().containsAll(Arrays.asList( "apn1", "tru1", "tru2" )));

        PddlProblem apn1Problem = maProblem.getProblems().get("apn1");

        for (PddlAction action : apn1Problem.domain.actions) {
            assertFalse(action.name, action.name.startsWith("loadtruck"));
            assertFalse(action.name, action.name.startsWith("drive"));
            assertFalse(action.name, action.name.startsWith("unloadtruck"));
        }

        assertEquals(26, apn1Problem.domain.actions.size());
        assertEquals(24, getNumPublicActions(apn1Problem.domain.actions));
        assertEquals(14, apn1Problem.domain.sharedPredicates.objects.size());
        
        PddlProblem tru1Problem = maProblem.getProblems().get("tru1");

        for (PddlAction action : tru1Problem.domain.actions) {
            assertFalse(action.name, action.name.startsWith("loadairplane"));
            assertFalse(action.name, action.name.startsWith("fly"));
            assertFalse(action.name, action.name.startsWith("unloadairplane"));
        }

        assertEquals(26, tru1Problem.domain.actions.size());
        assertEquals(16, getNumPublicActions(tru1Problem.domain.actions));
        assertEquals(14, tru1Problem.domain.sharedPredicates.objects.size());
    }

    @Test(timeout=100000)
    public void testPrivateGoals() throws Exception {

        Settings.ALLOW_PRIVATE_GOALS = true;

        MultiAgentProblem fmapMaProblem = Logistics01Fmap.getProblem();
        MultiAgentProblem maProblem = new MaStripsMultiAgentProblem(fmapMaProblem);

        assertEquals(3, maProblem.getAgents().size());
        assertTrue(maProblem.getAgents().toString(), maProblem.getAgents().containsAll(Arrays.asList( "apn1", "tru1", "tru2" )));

        PddlProblem apn1Problem = maProblem.getProblems().get("apn1");

        for (PddlAction action : apn1Problem.domain.actions) {
            assertFalse(action.name, action.name.startsWith("loadtruck"));
            assertFalse(action.name, action.name.startsWith("drive"));
            assertFalse(action.name, action.name.startsWith("unloadtruck"));
        }
        
        assertEquals(27, apn1Problem.domain.actions.size());
        assertEquals(25, getNumPublicActions(apn1Problem.domain.actions));
        assertEquals(18, apn1Problem.domain.sharedPredicates.objects.size());
        
        PddlProblem tru1Problem = maProblem.getProblems().get("tru1");

        for (PddlAction action : tru1Problem.domain.actions) {
            assertFalse(action.name, action.name.startsWith("loadairplane"));
            assertFalse(action.name, action.name.startsWith("fly"));
            assertFalse(action.name, action.name.startsWith("unloadairplane"));
        }

        assertEquals(27, tru1Problem.domain.actions.size());
        assertEquals(17, getNumPublicActions(tru1Problem.domain.actions));
        assertEquals(18, tru1Problem.domain.sharedPredicates.objects.size());
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
