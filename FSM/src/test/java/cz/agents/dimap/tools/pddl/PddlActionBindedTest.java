package cz.agents.dimap.tools.pddl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import cz.agents.dimap.data.SatellitesIpc01NotGrounded;
import cz.agents.dimap.psm.operator.Binding;

public class PddlActionBindedTest {

    @Test
    public void testFindOriginalAction() throws Exception {
        PddlProblem problem = SatellitesIpc01NotGrounded.createProblem().getProblems().get("satellite0");
        PddlAction action = PddlActionInstance.findOriginalAction("turn_to", problem.domain.actions);
        assertNotNull(action);
        assertEquals("turn_to", action.name);
    }

    @Test
    public void testFindBindingTurnTo() throws Exception {
        PddlProblem problem = SatellitesIpc01NotGrounded.createProblem().getProblems().get("satellite0");

        String[] actionDesc = "turn_to satellite0 groundstation2 phenomenon6".split(" ");
        PddlAction action = PddlActionInstance.findOriginalAction(actionDesc[0], problem.domain.actions);
        Binding binding = PddlActionInstance.createBinding(actionDesc, action, action);

        assertNotNull(binding);
        assertEquals(3, binding.size());
        assertEquals("satellite0", binding.getParameterValue(new PddlName("?s")).name);
        assertEquals("groundstation2", binding.getParameterValue(new PddlName("?d_new")).name);
        assertEquals("phenomenon6", binding.getParameterValue(new PddlName("?d_prev")).name);
    }

    @Test
    public void testFindBindingTakeImage() throws Exception {
        PddlProblem problem = SatellitesIpc01NotGrounded.createProblem().getProblems().get("satellite0");

        String[] actionDesc = "take_image satellite0 phenomenon6 instrument0 thermograph0".split(" ");
        PddlAction action = PddlActionInstance.findOriginalAction(actionDesc[0], problem.domain.actions);
        Binding binding = PddlActionInstance.createBinding(actionDesc, action, action);

        assertNotNull(binding);
        assertEquals(4, binding.size());
        assertEquals("satellite0", binding.getParameterValue(new PddlName("?s")).name);
        assertEquals("phenomenon6", binding.getParameterValue(new PddlName("?d")).name);
        assertEquals("instrument0", binding.getParameterValue(new PddlName("?i")).name);
        assertEquals("thermograph0", binding.getParameterValue(new PddlName("?m")).name);
    }
   
    @Test
    public void testProjectionTakeImage() throws Exception {
        PddlProblem problem = SatellitesIpc01NotGrounded.createProblem().getProblems().get("satellite0");

        String actionStr = "take_image satellite0 phenomenon6 instrument0 thermograph0";
        String[] actionDesc = actionStr.split(" ");
        PddlAction action = PddlActionInstance.findOriginalAction(actionDesc[0], problem.domain.actions);

        PddlAction projectedAction = PddlAction.parseAndProjectToPublic(actionStr, action, problem);

        assertNotNull(projectedAction);
        assertEquals("take_image satellite0 phenomenon6 thermograph0", projectedAction.name);
        assertEquals(3, projectedAction.parameters.getBindings().size());
        assertEquals(1, projectedAction.precondition.positives.size());
        assertEquals(0, projectedAction.precondition.negatives.size());
        assertEquals(1, projectedAction.effect.positives.size());
        assertEquals(0, projectedAction.effect.negatives.size());
    }

    @Test
    public void testBindedProjectionTakeImage() throws Exception {
        PddlProblem problem = SatellitesIpc01NotGrounded.createProblem().getProblems().get("satellite0");

        String actionStr = "take_image satellite0 phenomenon6 instrument0 thermograph0";
        String[] actionDesc = actionStr.split(" ");
        PddlAction action = PddlActionInstance.findOriginalAction(actionDesc[0], problem.domain.actions);

        Binding binding = PddlActionInstance.createBinding(actionDesc, action, action);

        PddlAction projectedAction = PddlAction.parseAndProjectToPublic(actionStr, action, problem);

        PddlActionInstance bindedAction = new PddlActionInstance(projectedAction, binding);

        assertNotNull(bindedAction);
        assertEquals(0, bindedAction.parameters.getBindings().size());
        assertEquals(1, bindedAction.precondition.positives.size());
        assertEquals(0, bindedAction.precondition.negatives.size());
        assertEquals(1, bindedAction.effect.positives.size());
        assertEquals(0, bindedAction.precondition.negatives.size());
    }
}
