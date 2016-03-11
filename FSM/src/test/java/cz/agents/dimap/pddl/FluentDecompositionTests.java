package cz.agents.dimap.pddl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.expr.SExprParser;
import cz.agents.dimap.tools.pddl.types.PddlFunctionType;
import cz.agents.dimap.tools.pddl.types.PddlType;
import cz.agents.dimap.tools.pddl.types.PddlTypeMap;

public class FluentDecompositionTests {

	private static String simpleAction = "(:action move_up_fast\n" + 
			"	:parameters (\n" + 
			"		?lift - fast_elevator\n" + 
			"		?f1 - count\n" + 
			"		?f2 - count\n" + 
			"	)\n" + 
			"	:precondition (and\n" + 
			"		(reachable_floor ?lift ?f2)\n" + 
			"		(= (lift_at ?lift) ?f1)\n" + 
			"	)\n" + 
			"	:effect (and\n" + 
			"		(assign (lift_at ?lift) ?f2)\n" + 
			"	)\n" + 
			")";
	
	private static String simpleGroundAction = "(:action move_up_fast\n" + 
			"	:parameters (\n" + 
			"		?lift - fast_elevator\n" + 
			"		?f2 - count\n" + 
			"	)\n" + 
			"	:precondition (and\n" + 
			"		(reachable_floor ?lift ?f2)\n" + 
			"		(= (lift_at ?lift) one)\n" + 
			"	)\n" + 
			"	:effect (and\n" + 
			"		(assign (lift_at ?lift) ?f2)\n" + 
			"		(assign (lift_at ground) zero)\n" + 
			"	)\n" + 
			")";
	
	private static String unboundAssignAction = "(:action move_up_fast\n" + 
			"	:parameters (\n" + 
			"		?lift - fast_elevator\n" + 
			"		?f1 - count\n" + 
			"		?f2 - count\n" + 
			"	)\n" + 
			"	:precondition (and\n" + 
			"		(reachable_floor ?lift ?f2)\n" + 
			"		(something ?lift ?f1)\n" + 
			"	)\n" + 
			"	:effect (and\n" + 
			"		(assign (lift_at ?lift) ?f2)\n" + 
			"	)\n" + 
			")";
	
	private static String unboundMultiAssignAction = "(:action move_up_fast\n" + 
			"	:parameters (\n" + 
			"		?lift1 - fast_elevator\n" + 
			"		?lift2 - fast_elevator\n" + 
			"		?f1 - count\n" + 
			"		?f2 - count\n" + 
			"	)\n" + 
			"	:precondition (and\n" + 
			"		(reachable_floor ?lift1 ?f2)\n" + 
			"		(= (lift_at ?lift1) ?f1)\n" + 
			"		(something ?lift2 ?f1)\n" + 
			"	)\n" + 
			"	:effect (and\n" + 
			"		(assign (lift_at ?lift1) ?f2)\n" + 
			"		(assign (lift_at ?lift2) ?f2)\n" + 
			"	)\n" + 
			")";

	private static String twoUnboundAssignsAction = "(:action move_up_fast\n" + 
			"	:parameters (\n" + 
			"		?lift1 - fast_elevator\n" + 
			"		?lift2 - fast_elevator\n" + 
			"		?f1 - count\n" + 
			"		?f2 - count\n" + 
			"	)\n" + 
			"	:precondition (and\n" + 
			"		(something ?lift1)\n" + 
			"	)\n" + 
			"	:effect (and\n" + 
			"		(assign (lift_at ?lift1) ?f1)\n" + 
			"		(assign (lift_at ?lift2) ?f2)\n" + 
			"	)\n" + 
			")";
	
	@Test
	public void simpleAction() {
		PddlAction action = new PddlAction(SExprParser.PARSER.parse(simpleAction));
		//System.out.println(action);
		Map<PddlName, PddlFunctionType> functionTypes = new HashMap<PddlName, PddlFunctionType>();
		action.applyFluentDecomposition(functionTypes);
		//System.out.println("---");
		//System.out.println(action);
		
		assertTrue(action.parameters.size() == 3);
		assertTrue(action.precondition.positives.size() == 2);
		assertTrue(action.precondition.negatives.size() == 0);
		assertTrue(action.precondition.equalities.size() == 0);
		assertTrue(action.precondition.assigns.size() == 0);
		assertTrue(action.effect.positives.size() == 1);
		assertTrue(action.effect.negatives.size() == 1);
		assertTrue(action.effect.equalities.size() == 0);
		assertTrue(action.effect.assigns.size() == 0);
		assertFalse("all parameters have a type", action.parameters.getBindings().containsValue(null));
	}

	
	@Test
	public void unboundAssignAction() {
		PddlAction action = new PddlAction(SExprParser.PARSER.parse(unboundAssignAction));
		//System.out.println(action);
		Map<PddlName, PddlFunctionType> functionTypes = new HashMap<PddlName, PddlFunctionType>();
		PddlFunctionType liftAtType = new PddlFunctionType(new PddlName("lift_at"), new PddlTypeMap<PddlName>(), new PddlType("count"));
		functionTypes.put(liftAtType.functionName, liftAtType);
		action.applyFluentDecomposition(functionTypes);
		//System.out.println("---");
		//System.out.println(action);
		
		assertTrue(action.parameters.size() == 4);
		assertTrue(action.precondition.positives.size() == 3);
		assertTrue(action.precondition.negatives.size() == 0);
		assertTrue(action.precondition.equalities.size() == 0);
		assertTrue(action.precondition.assigns.size() == 0);
		assertTrue(action.effect.positives.size() == 1);
		assertTrue(action.effect.negatives.size() == 1);
		assertTrue(action.effect.equalities.size() == 0);
		assertTrue(action.effect.assigns.size() == 0);
		assertFalse("all parameters have a type", action.parameters.getBindings().containsValue(null));
	}

	@Test
	public void unboundMultiAssignAction() {
		PddlAction action = new PddlAction(SExprParser.PARSER.parse(unboundMultiAssignAction));
		//System.out.println(action);
		Map<PddlName, PddlFunctionType> functionTypes = new HashMap<PddlName, PddlFunctionType>();
		PddlFunctionType liftAtType = new PddlFunctionType(new PddlName("lift_at"), new PddlTypeMap<PddlName>(), new PddlType("count"));
		functionTypes.put(liftAtType.functionName, liftAtType);
		action.applyFluentDecomposition(functionTypes);
		//System.out.println("---");
		//System.out.println(action);
		
		assertTrue(action.parameters.size() == 5);
		assertTrue(action.precondition.positives.size() == 4);
		assertTrue(action.precondition.negatives.size() == 0);
		assertTrue(action.precondition.equalities.size() == 0);
		assertTrue(action.precondition.assigns.size() == 0);
		assertTrue(action.effect.positives.size() == 2);
		assertTrue(action.effect.negatives.size() == 2);
		assertTrue(action.effect.equalities.size() == 0);
		assertTrue(action.effect.assigns.size() == 0);
		assertFalse("all parameters have a type", action.parameters.getBindings().containsValue(null));
	}

	@Test
	public void twoUnboundAssignsAction() {
		PddlAction action = new PddlAction(SExprParser.PARSER.parse(twoUnboundAssignsAction));
		//System.out.println(action);
		Map<PddlName, PddlFunctionType> functionTypes = new HashMap<PddlName, PddlFunctionType>();
		PddlFunctionType liftAtType = new PddlFunctionType(new PddlName("lift_at"), new PddlTypeMap<PddlName>(), new PddlType("count"));
		functionTypes.put(liftAtType.functionName, liftAtType);
		action.applyFluentDecomposition(functionTypes);
		//System.out.println("---");
		//System.out.println(action);
		
		assertTrue(action.parameters.size() == 6);
		assertTrue(action.precondition.positives.size() == 3);
		assertTrue(action.precondition.negatives.size() == 0);
		assertTrue(action.precondition.equalities.size() == 0);
		assertTrue(action.precondition.assigns.size() == 0);
		assertTrue(action.effect.positives.size() == 2);
		assertTrue(action.effect.negatives.size() == 2);
		assertTrue(action.effect.equalities.size() == 0);
		assertTrue(action.effect.assigns.size() == 0);
		assertFalse("all parameters have a type", action.parameters.getBindings().containsValue(null));
	}

	@Test
	public void simpleGroundAction() {
		PddlAction action = new PddlAction(SExprParser.PARSER.parse(simpleGroundAction));
		//System.out.println(action);
		Map<PddlName, PddlFunctionType> functionTypes = new HashMap<PddlName, PddlFunctionType>();
		PddlFunctionType liftAtType = new PddlFunctionType(new PddlName("lift_at"), new PddlTypeMap<PddlName>(), new PddlType("count"));
		functionTypes.put(liftAtType.functionName, liftAtType);
		action.applyFluentDecomposition(functionTypes);
		//System.out.println("---");
		//System.out.println(action);
		
		assertTrue(action.parameters.size() == 3);
		assertTrue(action.precondition.positives.size() == 3);
		assertTrue(action.precondition.negatives.size() == 0);
		assertTrue(action.precondition.equalities.size() == 0);
		assertTrue(action.precondition.assigns.size() == 0);
		assertTrue(action.effect.positives.size() == 2);
		assertTrue(action.effect.negatives.size() == 2);
		assertTrue(action.effect.equalities.size() == 0);
		assertTrue(action.effect.assigns.size() == 0);
		assertFalse("all parameters have a type", action.parameters.getBindings().containsValue(null));
	}
	
}
