package cz.agents.dimap.tools.pddl.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.expr.SExpr;

public class PddlParser {

	public static java.util.Map<String, SExpr> makePddlDictionary(SExpr expr) {
		java.util.Map<String, SExpr> dict = new HashMap<String, SExpr>();
		int uniqId = 0;
		if (expr.getValue() != null) {
			return dict;
		}
		for (SExpr e : expr.getList()) {
			if (!e.isNonEmptyList()) {
				continue;
			}
			if (!e.e(0).isValue()) {
				continue;
			}
			String key = e.e(0).getValue();
			if (key.equals(":action")) {
				key = "act:"+(++uniqId); 
			}
			dict.put(key, e);
		}
		return dict;
	}

	public static java.util.Map<String, SExpr> makeActionDictionary(SExpr expr) {
		java.util.Map<String, SExpr> dict = new HashMap<String, SExpr>();
		if (!expr.isList()) {
			System.err.println("PDDL ERROR: An S-Expression does not represent action: "+expr);
			return dict;
		}
		
		Iterator<SExpr> iter = expr.getList().iterator(); 
		while (iter.hasNext()) {
			SExpr key = iter.next();
			if (!iter.hasNext()) {
				System.out.println("WARNING: Not enough expressions");
				break;
			}
			SExpr val = iter.next();
			if (!key.isValue()) {
				System.out.println("WARNING: Key is not a value: "+key);
				continue;
			}
			dict.put(key.getValue(), val);
		}
		
		return dict;
	}

	/*
	public static List<PddlAssignment> makeAssignmentsList(SExpr expr) {
		return makeAssignmentsList(expr, true);
	}
	
	public static List<PddlAssignment> makeAssignmentsList(SExpr expr, boolean skipFirst) {
		if (!expr.isList()) {
			System.err.println("PDDL ERROR: An S-Expression does not represent a list of assignments: "+expr);
		}
		
		List<PddlAssignment> ret = new LinkedList<>();
		List<String> lefts = new LinkedList<String>();
		for (int i = skipFirst ? 1 : 0; i < expr.getList().size(); i++) {
			if (!expr.e(i).isValue()) {
				System.err.println("PDDL ERROR: Invalid left side assignemt value. Value expected, has "+expr.e(i));
				continue;
			}
			if (expr.e(i).isValue("-")) {
				ret.add(new PddlAssignment(lefts, expr.e(i+1)));
				lefts = new LinkedList<String>();
				i++;
				continue;
			}
			lefts.add(PddlName.fixName(expr.e(i).getValue()));
		}
		if (lefts.size() > 0) {
			ret.add(new PddlAssignment(lefts));
		}
		
		return ret;				
	}
	*/

	public static void main(String[] args) {
		try {
			//PddlDomain domain = new PddlDomain("/home/yan/projects/uni/planning/src/lpgfd-planner/FSM/experiments/ma-benchmarks/beerproblem-grounded/beerproblem-grounded.pddl");
			//PddlDomain domain = new PddlDomain("/home/yan/projects/uni/planning/src/lpgfd-planner/FSM/experiments/projections/rovers-ipc-p01/_ma-domain-orig.pddl");
			//PddlProblem problem = new PddlProblem(domain, "/home/yan/projects/uni/planning/src/lpgfd-planner/FSM/experiments/projections/deconfliction-a2/robot1-problem-costs.pddl");
			//PddlDomain domain = new PddlDomain("/home/yan/projects/uni/planning/src/lpgfd-planner/FSM/experiments/ma-benchmarks/deconfliction/deconfliction.pddl");
			//PddlProblem problem = new PddlProblem(domain, "/home/yan/projects/uni/planning/src/lpgfd-planner/FSM/experiments/ma-benchmarks/deconfliction/deconfliction-a2.pddl");
			//PddlProblem problem = new PddlProblem(domain, "/home/yan/projects/uni/planning/src/lpgfd-planner/FSM/experiments/projections/elevators-p01/fast0-problem.pddl");
			PddlDomain domain = new PddlDomain("/home/yan/projects/planning/fmap/benchmarks/elevators/Pfile1/DomainElevators.pddl");
			//PddlDomain domain = new PddlDomain("/home/yan/projects/planning/src/lpgfd-planner/FSM/experiments/projections/beerproblem-grounded-a2/plane/iter-001-domain.pddl");
			PddlProblem problem = new PddlProblem(domain, "/home/yan/projects/planning/fmap/benchmarks/elevators/Pfile1/ProblemElevatorsfast0.pddl");
			//domain.applyFluentDecomposition();
			problem.applyFluentDecomposition();
			problem.translateFmapToMa();
			System.out.println(problem);
			//System.out.println(problem);
			//PddlAddl addl = new PddlAddl("/home/yan/projects/planning/src/lpgfd-planner/FSM/experiments/ma-benchmarks/elevators/elevators.addl");
			//System.out.println(addl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
