package experiments.psm.fmap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cz.agents.dimap.Settings;
import cz.agents.dimap.data.Depots01Fmap;
import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class FmapMultiAgentProblemDepotsTest {

    @Test
    public void testPublicGoals() throws Exception {

        Settings.ALLOW_PRIVATE_GOALS = false;

        MultiAgentProblem maProblem = Depots01Fmap.getProblem();

        assertEquals(5, maProblem.getAgents().size());
        assertTrue(maProblem.getAgents().toString(), maProblem.getAgents().containsAll(Arrays.asList( "depot0", "distributor0", "distributor1", "truck0", "truck1" )));

        PddlProblem depot0Problem = maProblem.getProblems().get("depot0");

        assertEquals(4, depot0Problem.domain.actions.size());
        assertEquals(4, getNumPublicActions(depot0Problem.domain.actions));
        assertEquals(6, depot0Problem.domain.sharedPredicateNames.size());
        
        PddlProblem distributor0Problem = maProblem.getProblems().get("distributor0");

        assertEquals(4, distributor0Problem.domain.actions.size());
        assertEquals(4, getNumPublicActions(distributor0Problem.domain.actions));
        assertEquals(6, distributor0Problem.domain.sharedPredicateNames.size());

        PddlProblem truck0Problem = maProblem.getProblems().get("truck0");

        assertEquals(3, truck0Problem.domain.actions.size());
        assertEquals(3, getNumPublicActions(truck0Problem.domain.actions));
        assertEquals(6, truck0Problem.domain.sharedPredicateNames.size());
    }

    @Test
    public void testPrivateGoals() throws Exception {

        Settings.ALLOW_PRIVATE_GOALS = true;

        MultiAgentProblem maProblem = Depots01Fmap.getProblem();

        assertEquals(5, maProblem.getAgents().size());
        assertTrue(maProblem.getAgents().toString(), maProblem.getAgents().containsAll(Arrays.asList( "depot0", "distributor0", "distributor1", "truck0", "truck1" )));

        PddlProblem depot0Problem = maProblem.getProblems().get("depot0");

        assertEquals(5, depot0Problem.domain.actions.size());
        assertEquals(5, getNumPublicActions(depot0Problem.domain.actions));
        assertEquals(10, depot0Problem.domain.sharedPredicateNames.size());
        
        PddlProblem distributor0Problem = maProblem.getProblems().get("distributor0");

        assertEquals(5, distributor0Problem.domain.actions.size());
        assertEquals(5, getNumPublicActions(distributor0Problem.domain.actions));
        assertEquals(10, distributor0Problem.domain.sharedPredicateNames.size());

        PddlProblem truck0Problem = maProblem.getProblems().get("truck0");

        assertEquals(4, truck0Problem.domain.actions.size());
        assertEquals(3, getNumPublicActions(truck0Problem.domain.actions));
        assertEquals(10, truck0Problem.domain.sharedPredicateNames.size());
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
