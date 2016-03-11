package cz.agents.dimap.psm;

import static junit.framework.Assert.assertEquals;

import java.util.BitSet;

import org.junit.Test;

import cz.agents.dimap.fsm.FiniteStateMachineTools;
import cz.agents.dimap.psm.operator.SimpleOperator;
import cz.agents.dimap.psm.state.BitSetState;
import cz.agents.dimap.psm.state.BitSetStateFactory;
import cz.agents.dimap.psm.state.MultiState;
import cz.agents.dimap.psm.state.State;

public class TestPublicProjection2 {

	public static boolean DOT_OUTPUT = false;

	@Test
	public void testSimpleAllPublic() {
		State init = createState();
		State stA = createState(0,2);
		State stC = createState(4);
		
		PlanStateMachine fsm = new PlanStateMachine(init);
		
		fsm.addTransition(init, new SimpleOperator("a-0", true), stA);
		fsm.addTransition(stA, new SimpleOperator("c-4", true), stC);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(2, 4));
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());
		
		assertEquals(3, fsm.getNumberOfStates());
		assertEquals(2, fsm.getNumberOfTransitions());
	}

	@Test
	public void test2InternalStates() {
		State init = createState();
		State stA = createState(0,2);
		State stB = createState(1,2);
		State stC = createState(4);
		
		PlanStateMachine fsm = new PlanStateMachine(init);
		
		fsm.addTransition(init, new SimpleOperator("a-0", true), stA);
		fsm.addTransition(init, new SimpleOperator("b-1", true), stB);
		fsm.addTransition(stA, new SimpleOperator("c-4", true), stC);
		fsm.addTransition(stB, new SimpleOperator("c-4", true), stC);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(2,4));
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());

		assertEquals(3, fsm.getNumberOfStates());
		assertEquals(3, fsm.getNumberOfTransitions());
	}

	@Test
	public void testNondeterminismIntroduction() {
		State init = createState();
		State stA = createState(0,2);
		State stB = createState(1,2);
		State stC = createState(3);
		
		PlanStateMachine fsm = new PlanStateMachine(init);
		
		fsm.addTransition(init, new SimpleOperator("load0", false), stA);
		fsm.addTransition(init, new SimpleOperator("load1", false), stB);
		fsm.addTransition(stA, new SimpleOperator("move1", true), stC);
		fsm.addTransition(stB, new SimpleOperator("move2", true), stC);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(2,3));
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());

		assertEquals(2, fsm.getNumberOfStates());
		assertEquals(2, fsm.getNumberOfTransitions());
	}

	@Test
	public void testNondeterminismLoops() {
		State init = createState();
		State stA = createState(0,2);
		State stB = createState(1,2);
		State stC = createState(0,3);
		State stD = createState(1,3);
		State stE = createState(0,4);
		State stF = createState(1,4);
		
		PlanStateMachine fsm = new PlanStateMachine(init);
		
		fsm.addTransition(init, new SimpleOperator("int1", false), stA);
		fsm.addTransition(init, new SimpleOperator("int2", false), stB);
		fsm.addTransition(stA, new SimpleOperator("pub", true), stC);
		fsm.addTransition(stB, new SimpleOperator("pub", true), stD);
		fsm.addTransition(stC, new SimpleOperator("pub1a", true), stE);
		fsm.addTransition(stD, new SimpleOperator("pub2a", true), stF);
		fsm.addTransition(stE, new SimpleOperator("pub1b", true), stC);
		fsm.addTransition(stF, new SimpleOperator("pub2b", true), stD);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(2,3));
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());

		assertEquals(5, fsm.getNumberOfStates());
		assertEquals(6, fsm.getNumberOfTransitions());
	}
	
	@Test
	public void testPermutationsCase() {
		State init = createState();
		State stA = createState(0,2);
		State stB = createState(1,2);
		State stC = createState(4);
		
		PlanStateMachine fsm = new PlanStateMachine(init);
		
		fsm.addTransition(init, new SimpleOperator("a-0", true), stA);
		fsm.addTransition(init, new SimpleOperator("b-1", true), stB);
		fsm.addTransition(stA, new SimpleOperator("x-pub", true), stB);
		fsm.addTransition(stA, new SimpleOperator("c-4", true), stC);
		fsm.addTransition(stB, new SimpleOperator("c-4", true), stC);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(2,4));
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());

		assertEquals(3, fsm.getNumberOfStates());
		assertEquals(3, fsm.getNumberOfTransitions());
	}

	@Test
	public void testPermutationsCase2() {
		State init = createState();
		State stA = createState(0,2);
		State stB = createState(1,2);
		State stC = createState(4);
		
		PlanStateMachine fsm = new PlanStateMachine(init);
		
		fsm.addTransition(init, new SimpleOperator("a-0", true), stA);
		fsm.addTransition(init, new SimpleOperator("b-1", true), stB);
		fsm.addTransition(stA, new SimpleOperator("x-pub", true), stA);
		fsm.addTransition(stA, new SimpleOperator("x-pub", true), stB);
		fsm.addTransition(stA, new SimpleOperator("c-4", true), stC);
		fsm.addTransition(stB, new SimpleOperator("c-4", true), stC);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(2,4));
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());

		assertEquals(3, fsm.getNumberOfStates());
		assertEquals(3, fsm.getNumberOfTransitions());
	}

	@Test
	public void testPermutationsCase3() {
		State init = createState();
		State stA = createState(0,2);
		State stB = createState(1,2);
		State stC = createState(4);
		
		PlanStateMachine fsm = new PlanStateMachine(init);
		
		fsm.addTransition(init, new SimpleOperator("a-0", true), stA);
		fsm.addTransition(init, new SimpleOperator("b-1", true), stB);
		fsm.addTransition(stB, new SimpleOperator("x-pub", true), stA);
		fsm.addTransition(stA, new SimpleOperator("x-pub", true), stB);
		fsm.addTransition(stA, new SimpleOperator("c-4", true), stC);
		fsm.addTransition(stB, new SimpleOperator("c-4", true), stC);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(2,4));
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());

		assertEquals(3, fsm.getNumberOfStates());
		assertEquals(3, fsm.getNumberOfTransitions());
	}

	@Test
	public void testPermutationsCase4() {
		State init = createState();
		State stA = createState(0,2);
		State stB = createState(1,2);
		State stG4 = createState(4);
		State stG24 = createState(2,4);
		
		PlanStateMachine fsm = new PlanStateMachine(init);
		
		fsm.addTransition(init, new SimpleOperator("a-0", true), stA);
		fsm.addTransition(init, new SimpleOperator("b-1", true), stB);
		fsm.addTransition(stB, new SimpleOperator("x-pub", true), stA);
		fsm.addTransition(stA, new SimpleOperator("x-pub", true), stB);
		fsm.addTransition(stA, new SimpleOperator("c-4", true), stG4);
		fsm.addTransition(stB, new SimpleOperator("c-4", true), stG4);
		fsm.addTransition(stB, new SimpleOperator("c-24", true), stG24);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(2, 4));
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());

		assertEquals(4, fsm.getNumberOfStates());
		assertEquals(4, fsm.getNumberOfTransitions());
	}

	@Test
	public void testPermutationsCase5() {
		State init = createState();
		State stA = createState(0,2);
		State stB = createState(1,2);
		State stG4 = createState(4);
		State stG24 = createState(2,4);
		
		PlanStateMachine fsm = new PlanStateMachine(init);
		
		fsm.addTransition(init, new SimpleOperator("a-0", true), stA);
		fsm.addTransition(init, new SimpleOperator("b-1", true), stB);
		fsm.addTransition(stB, new SimpleOperator("x-pub", true), stA);
		fsm.addTransition(stA, new SimpleOperator("x-pub", true), stB);
		fsm.addTransition(stA, new SimpleOperator("c-4", true), stG4);
		fsm.addTransition(stB, new SimpleOperator("c-4", true), stG4);
		fsm.addTransition(stB, new SimpleOperator("c-24", true), stG24);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(1, 2, 4));
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());

		assertEquals(5, fsm.getNumberOfStates());
		assertEquals(7, fsm.getNumberOfTransitions());
	}

	@Test
	public void testOneInternalAction() {
		State init = createState(0);
		State stA = createState(1);
		State stB = createState(1,2);
		State stC = createState(3);
		
		PlanStateMachine fsm = new PlanStateMachine(init);
		
		fsm.addTransition(init, new SimpleOperator("a-0", true), stA);
		fsm.addTransition(stA, new SimpleOperator("c-4", false), stB);
		fsm.addTransition(stB, new SimpleOperator("c-4", true), stC);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(0,1,3));

		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());

		assertEquals(3, fsm.getNumberOfStates());
		assertEquals(2, fsm.getNumberOfTransitions());
	}

	@Test
	public void testManyInternalActions () {
		State init = createState(0);
		State stA = createState(1);
		State stB = createState(1,2);
		State stC = createState(1,3);
		State stG = createState(4);
		
		PlanStateMachine fsm = new PlanStateMachine(init);
		
		fsm.addTransition(init, new SimpleOperator("a", true), stA);
		fsm.addTransition(init, new SimpleOperator("b", true), stB);
		fsm.addTransition(init, new SimpleOperator("c", true), stC);
		fsm.addTransition(stA, new SimpleOperator("b", false), stB);
		fsm.addTransition(stB, new SimpleOperator("bb", false), stB);
		fsm.addTransition(stA, new SimpleOperator("aa", false), stA);
		fsm.addTransition(stB, new SimpleOperator("d", false), stC);
		fsm.addTransition(stC, new SimpleOperator("cc", false), stC);
		fsm.addTransition(stA, new SimpleOperator("e", true), stG);
		fsm.addTransition(stB, new SimpleOperator("e", true), stG);
		fsm.addTransition(stC, new SimpleOperator("e", true), stG);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());

		PublicProjection.publicProjection(fsm, createBssFactory(0,1,4));

		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());

		assertEquals(3, fsm.getNumberOfStates());
		assertEquals(4, fsm.getNumberOfTransitions());
	}

	@Test
	public void testOneInternalActionAndExtraPublicAction () {
		State init = createState(0);
		State stA = createState(1);
		State stB = createState(1,2);
		State stD = createState(4,2);
		State stC = createState(3);
		
		PlanStateMachine fsm = new PlanStateMachine(init);
		
		fsm.addTransition(init, new SimpleOperator("a", true), stA);
		fsm.addTransition(stA, new SimpleOperator("b", false), stB);
		fsm.addTransition(stB, new SimpleOperator("c", true), stC);
		fsm.addTransition(stA, new SimpleOperator("d", true), stD);
		fsm.addTransition(stD, new SimpleOperator("e", true), stC);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());

		PublicProjection.publicProjection(fsm, createBssFactory(0,1,3,4));

		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());

		assertEquals(4, fsm.getNumberOfStates());
		assertEquals(4, fsm.getNumberOfTransitions());
	}

	@Test
	public void test2InternalStatesDifActions() {
		State init = createState();
		State stA = createState(0,2);
		State stB = createState(1,2);
		State stC = createState(4);
		
		PlanStateMachine fsm = new PlanStateMachine(init);
		
		fsm.addTransition(init, new SimpleOperator("a-0", true), stA);
		fsm.addTransition(init, new SimpleOperator("b-1", true), stB);
		fsm.addTransition(stA, new SimpleOperator("a-4", true), stC);
		fsm.addTransition(stB, new SimpleOperator("b-4", true), stC);

		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());

		PublicProjection.publicProjection(fsm, createBssFactory(2,4));

		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());

		assertEquals(4, fsm.getNumberOfStates());
		assertEquals(4, fsm.getNumberOfTransitions());
	}

	@Test
	public void test2Goals() {
		State init = createState();
		State stA = createState(0);
		State stB = createState(1);
		State stC = createState(0, 1);
		State stD = createState(2);
		State stE = createState(3);
		State stF = createState(2,4);
		State stG = createState(3,4);
		
		PlanStateMachine fsm = new PlanStateMachine(init);
		
		fsm.addTransition(init, new SimpleOperator("a-0", true), stA);
		fsm.addTransition(init, new SimpleOperator("b-1", true), stB);
		fsm.addTransition(stA, new SimpleOperator("a-1", true), stC);
		fsm.addTransition(stB, new SimpleOperator("b-0", true), stC);
		fsm.addTransition(stC, new SimpleOperator("c-2", true), stD);
		fsm.addTransition(stC, new SimpleOperator("c-3", true), stE);
		fsm.addTransition(stD, new SimpleOperator("d-4", true), stF);
		fsm.addTransition(stE, new SimpleOperator("d-4", true), stG);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(0, 1, 4));

		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());
		
		assertEquals(6, fsm.getNumberOfStates());
		assertEquals(7, fsm.getNumberOfTransitions());
	}
	
	@Test
	public void testSimple2Goals() {
		State init = createState();
		State stF = createState(2,4);
		State stG = createState(3,4);
		
		PlanStateMachine fsm = new PlanStateMachine(init);
		
		fsm.addTransition(init, new SimpleOperator("d-4", true), stF);
		fsm.addTransition(init, new SimpleOperator("d-4", true), stG);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(0, 1, 4));

		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());
		
		assertEquals(2, fsm.getNumberOfStates());
		assertEquals(1, fsm.getNumberOfTransitions());
	}
	
	@Test
	public void test2PublicGoals() {
		State init = createState();
		State stA = createState(0);
		State stB = createState(1);
		State stC = createState(0, 1);
		State stD = createState(2);
		State stE = createState(3);
		State stF = createState(0,4);
		State stG = createState(1,4);
		
		PlanStateMachine fsm = new PlanStateMachine(init);
		
		fsm.addTransition(init, new SimpleOperator("a-0", true), stA);
		fsm.addTransition(init, new SimpleOperator("b-1", true), stB);
		fsm.addTransition(stA, new SimpleOperator("a-1", true), stC);
		fsm.addTransition(stB, new SimpleOperator("b-0", true), stC);
		fsm.addTransition(stC, new SimpleOperator("c-2", true), stD);
		fsm.addTransition(stC, new SimpleOperator("c-3", true), stE);
		fsm.addTransition(stD, new SimpleOperator("d-4", true), stF);
		fsm.addTransition(stE, new SimpleOperator("d-4", true), stG);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(0, 1, 4));

		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());
		
		assertEquals(8, fsm.getNumberOfStates());
		assertEquals(8, fsm.getNumberOfTransitions());
	}
	
	@Test
	public void testTwoDiamonds() {
		State init = createState();
		State stA = createState(0);
		State stB = createState(1);
		State stC = createState(0, 1);
		State stD = createState(2);
		State stE = createState(3);
		State stF = createState(3, 4);
		State stG = createState(3, 4);
		
		PlanStateMachine fsm = new PlanStateMachine(init);
		
		fsm.addTransition(init, new SimpleOperator("a-0", true), stA);
		fsm.addTransition(init, new SimpleOperator("b-1", true), stB);
		fsm.addTransition(stA, new SimpleOperator("a-1", true), stC);
		fsm.addTransition(stB, new SimpleOperator("b-0", true), stC);
		fsm.addTransition(stC, new SimpleOperator("c-2", true), stD);
		fsm.addTransition(stC, new SimpleOperator("c-3", true), stE);
		fsm.addTransition(stD, new SimpleOperator("d-4", true), stF);
		fsm.addTransition(stE, new SimpleOperator("d-5", true), stG);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(0, 1, 4));

		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());
		
		assertEquals(7, fsm.getNumberOfStates());
		assertEquals(8, fsm.getNumberOfTransitions());
	}

	@Test
	public void testSmallBackDiamond() {
		
		State stInit = createState(0);
		State stA = createState(1, 4);
		State stC = createState(2);
		State stGoal = createState(3);
		
		PlanStateMachine fsm = new PlanStateMachine(stInit);
		
		fsm.addTransition(stInit, new SimpleOperator("a", true), stA);
		fsm.addTransition(stA, new SimpleOperator("b", true), stC);
		fsm.addTransition(stC, new SimpleOperator("back", true), stA);
		fsm.addTransition(stC, new SimpleOperator("fin", true), stGoal);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(0, 1, 2, 3));

		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());
		
		assertEquals(4, fsm.getNumberOfStates());
		assertEquals(4, fsm.getNumberOfTransitions());
	}

	@Test
	public void testBackDiamond() {
		
		State stInit = createState(0);
		State stA = createState(1, 4);
		State stB = createState(1, 5);
		State stC = createState(2);
		State stGoal = createState(3);
		
		PlanStateMachine fsm = new PlanStateMachine(stInit);
		
		fsm.addTransition(stInit, new SimpleOperator("a", true), stA);
		fsm.addTransition(stInit, new SimpleOperator("c", true), stB);
		fsm.addTransition(stA, new SimpleOperator("b", true), stC);
		fsm.addTransition(stB, new SimpleOperator("d", true), stC);
		fsm.addTransition(stC, new SimpleOperator("back", true), stA);
		fsm.addTransition(stC, new SimpleOperator("fin", true), stGoal);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(0, 1, 2, 3));

		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());
		
		assertEquals(5, fsm.getNumberOfStates());
		assertEquals(6, fsm.getNumberOfTransitions());
	}

	@Test
	public void testBackDiamondOneTrans() {
		
		State stInit = createState(0);
		State stA = createState(1, 4);
		State stB = createState(1, 5);
		State stC = createState(2);
		State stGoal = createState(3);
		
		PlanStateMachine fsm = new PlanStateMachine(stInit);
		
		fsm.addTransition(stInit, new SimpleOperator("a", true), stA);
		fsm.addTransition(stInit, new SimpleOperator("c", true), stB);
		fsm.addTransition(stA, new SimpleOperator("b", true), stC);
		fsm.addTransition(stB, new SimpleOperator("d", true), stC);
		fsm.addTransition(stC, new SimpleOperator("back", true), stA);
		fsm.addTransition(stC, new SimpleOperator("fin", true), stGoal);
		
		fsm.addTransition(stA, new SimpleOperator("trans1", false), stB);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
	
		PublicProjection.publicProjection(fsm, createBssFactory(0, 1, 2, 3));

		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());
		
		assertEquals(5, fsm.getNumberOfStates());
		assertEquals(7, fsm.getNumberOfTransitions());
	}

	@Test
	public void testBackDiamondTwoTrans() {
		
		State stInit = createState(0);
		State stA = createState(1, 4);
		State stB = createState(1, 5);
		State stC = createState(2);
		State stGoal = createState(3);
		
		PlanStateMachine fsm = new PlanStateMachine(stInit);
		
		fsm.addTransition(stInit, new SimpleOperator("a", true), stA);
		fsm.addTransition(stInit, new SimpleOperator("c", true), stB);
		fsm.addTransition(stA, new SimpleOperator("b", true), stC);
		fsm.addTransition(stB, new SimpleOperator("d", true), stC);
		fsm.addTransition(stC, new SimpleOperator("back", true), stA);
		fsm.addTransition(stC, new SimpleOperator("fin", true), stGoal);
		
		fsm.addTransition(stA, new SimpleOperator("trans1", false), stB);
		fsm.addTransition(stB, new SimpleOperator("trans2", false), stA);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());

//		PublicProjection.computePrivateClosure(fsm);
//    	fsm.removeStatesNotGoingFromInit();

		PublicProjection.publicProjection(fsm, createBssFactory(0, 1, 2, 3));

		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());
		
		assertEquals(4, fsm.getNumberOfStates());
		assertEquals(6, fsm.getNumberOfTransitions());
	}
	
	@Test
	public void testSimplePublicLoop() {
		
		State stInit = createState(0);
		State stA = createState(1);
		State stGoal = createState(2);
		
		PlanStateMachine fsm = new PlanStateMachine(stInit);
		
		fsm.addTransition(stInit, new SimpleOperator("a", true), stA);
		fsm.addTransition(stA, new SimpleOperator("back", true), stInit);
		fsm.addTransition(stA, new SimpleOperator("b", true), stGoal);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(0, 1, 2));

		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());
		
		assertEquals(3, fsm.getNumberOfStates());
		assertEquals(3, fsm.getNumberOfTransitions());
	}

	@Test
	public void testSimpleLoop() {
		
		State stInit = createState(0, 2);
		State stA = createState(0, 3);
		State stGoal = createState(1);
		
		PlanStateMachine fsm = new PlanStateMachine(stInit);
		
		fsm.addTransition(stInit, new SimpleOperator("a", false), stA);
		fsm.addTransition(stA, new SimpleOperator("back", false), stInit);
		fsm.addTransition(stA, new SimpleOperator("b", true), stGoal);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(0, 1));

		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());
		
		assertEquals(2, fsm.getNumberOfStates());
		assertEquals(1, fsm.getNumberOfTransitions());
	}
	
	@Test
	public void testSinglePrivateAction() {
		
		State stInit = createState(0);
		State stA = createState(2);
		State stB = createState(3);
		State stGoal = createState(1);
		
		PlanStateMachine fsm = new PlanStateMachine(stInit);
		
		fsm.addTransition(stInit, new SimpleOperator("a", true), stA);
		fsm.addTransition(stA, new SimpleOperator("priv", false), stB);
		fsm.addTransition(stB, new SimpleOperator("b", true), stGoal);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(0, 1));

		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());
		
		assertEquals(3, fsm.getNumberOfStates());
		assertEquals(2, fsm.getNumberOfTransitions());
	}

	@Test
	public void testSinglePublicAction() {
		
		State stInit = createState(0);
		State stA = createState(2,3);
		State stB = createState(2,4);
		State stGoal = createState(1);
		
		PlanStateMachine fsm = new PlanStateMachine(stInit);
		
		fsm.addTransition(stInit, new SimpleOperator("a", true), stA);
		fsm.addTransition(stA, new SimpleOperator("pub {eff: +2, +4, -3}", true), stB);
		fsm.addTransition(stB, new SimpleOperator("b", true), stGoal);
		
		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(0, 1, 2));

		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());
		
		assertEquals(3, fsm.getNumberOfStates());
		assertEquals(2, fsm.getNumberOfTransitions());
	}

	@Test
	public void testPublicLoop() {
		
		State stInit = createState(0);
		State stA = createState(1,4);
		State stB = createState(2);
		State stC = createState(1,5);
		State stGoal = createState(3);
		
		PlanStateMachine fsm = new PlanStateMachine(stInit);
		
		fsm.addTransition(stInit, new SimpleOperator("a", true), stA);
		fsm.addTransition(stA, new SimpleOperator("b", true), stB);
		fsm.addTransition(stB, new SimpleOperator("c", true), stC);
		fsm.addTransition(stC, new SimpleOperator("d", true), stGoal);
		
		if (DOT_OUTPUT ) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
		
		PublicProjection.publicProjection(fsm, createBssFactory(0, 1, 2, 3));

		if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());
		
		assertEquals(5, fsm.getNumberOfStates());
		assertEquals(4, fsm.getNumberOfTransitions());
	}

    @Test
    public void testSatellites() {
        
        State stInit = createState(0, 5, 9, 12, 13);
        State st1 = createState(0, 5, 9, 13, 18);
        State st2 = createState(0, 5, 7, 13, 18);
        State st3 = createState(0, 4, 5, 7, 13, 18);
        State st4 = createState(0, 4, 5, 13, 14, 18);
        State st5 = createState(0, 3, 4, 5, 13, 14, 18);
        State st6 = createState(0, 3, 4, 5, 9, 13, 18);
        State st7 = createState(0, 2, 3, 4, 5, 9, 13, 18);
        State st8 = createState(0, 2, 3, 4, 5, 8, 13, 18);
        State stGoal = createState(0, 2, 3, 4, 5, 8, 13, 17, 18);
        
        PlanStateMachine fsm = new PlanStateMachine(stInit);
        
        fsm.addTransition(stInit, new SimpleOperator("int_a", false), st1);
        fsm.addTransition(st1, new SimpleOperator("int_b", false), st2);
        fsm.addTransition(st2, new SimpleOperator("int_c", false), st3);
        fsm.addTransition(st3, new SimpleOperator("ini_d", false), st4);
        fsm.addTransition(st4, new SimpleOperator("pub_e", true), st5);
        fsm.addTransition(st5, new SimpleOperator("int_f", false), st6);
        fsm.addTransition(st6, new SimpleOperator("int_g", false), st7);
        fsm.addTransition(st7, new SimpleOperator("int_h", false), st8);
        fsm.addTransition(st8, new SimpleOperator("pub_i", true), stGoal);
        
        if (DOT_OUTPUT ) FiniteStateMachineTools.imgOutput(fsm, "test", new SimplePublicLabelFactory());
        
        PublicProjection.publicProjection(fsm, createBssFactory(3, 17));

        if (DOT_OUTPUT) FiniteStateMachineTools.imgOutput(fsm, "test-pub", new SimplePublicLabelFactory());
        
        assertEquals(3, fsm.getNumberOfStates());
        assertEquals(2, fsm.getNumberOfTransitions());
    }
	
	private BitSetStateFactory createBssFactory(final int ... value) {
		return new BitSetStateFactory() {
			
			@Override
			public String toString(State state) {
				return state.toString();
			}

            @Override
            public BitSetState projectToPublic(State state) {
                BitSet bitSet = (BitSet) ((BitSetState) state).getBitSet().clone();
                bitSet.and(createState(value).getBitSet());
                BitSetState pubState = new BitSetState(bitSet);
                return pubState;
            }

            @Override
            public MultiState projectToPublic(MultiState state) {
                return null;
            }
		};
	}
	
	private BitSetState createState(int ... value) {
		BitSet bitSet = new BitSet();
		for (int i : value) {
			bitSet.set(i);
		}
		return new BitSetState(bitSet);
	}
}
