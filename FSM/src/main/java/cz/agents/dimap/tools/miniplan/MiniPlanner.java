package cz.agents.dimap.tools.miniplan;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import cz.agents.dimap.tools.GenericPlanner;
import cz.agents.dimap.tools.GenericResult;
import cz.agents.dimap.tools.fd.DownwardGrounder;
import cz.agents.dimap.tools.miniplan.state.MiniState;
import cz.agents.dimap.tools.miniplan.state.MiniStateSet;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class MiniPlanner implements GenericPlanner {
	
	public PddlProblem problem;
	public Stack<PddlAction> plan = new Stack<>();
	public Set<MiniState> visited;
	
	public MiniPlanner() {
	}
	
	public boolean plan() {
		plan.clear();
		//MiniState startState = new MiniStateList(problem);
		MiniState startState = new MiniStateSet(problem);
		System.out.println(startState);
		
		int LIMIT = 11;
		while (true) {
			visited = new LinkedHashSet<>();
			System.out.printf("RUNNING SEARCH WITH LIMIT = %d\n", LIMIT);
			if (plan(startState, LIMIT)) {
				return true;
			}
			LIMIT += 5;
			return false;
		}
		
	}
	
	public boolean plan(MiniState state, int LIMIT) {
		if (state.isSolved()) {
			System.out.printf("SOLVED: ");
			printPlan();
			return true;
		}
		if (plan.size() > LIMIT) {
			return false;
		}
		//if (visited.contains(state)) {
		//	return false;
		//}
		for (MiniState vis : visited) {
			if (state.subsumedBy(vis)) {
			//if (state.equals(vis)) {
				return false;
			}
		}
		visited.add(state);
		
		printPlan();
		System.out.println(state);
		
		List<PddlAction> applicables = state.getApplicableActions(problem);
		for (PddlAction action : applicables) {
			System.out.println(action.name);
		}
		
		for (PddlAction action : applicables) {
			plan.push(action);
			MiniState nextState = state.apply(action);
			if (plan(nextState, LIMIT)) {
				return true;
			}
			plan.pop();
		}
		
		return false;
	}

	private static int count = 0; 
	private void printPlan() {
		System.out.printf("%d", count++);
		for (int i = plan.size() - 1; i >= 0; i--) { 
			System.out.printf(" %s, ", plan.get(i).name);
		}
		System.out.printf("\n");
	}

	@Override
	public GenericResult plan(PddlProblem problem) {
		GenericResult result = new GenericResult();
		double startTime = System.currentTimeMillis();
		System.out.println("MiniPlanner :: Running FD grounder");
		this.problem = DownwardGrounder.groundProblem(problem);
		System.out.println("MiniPlanner :: Planning");
		result.solved = plan();
		result.planningTime = System.currentTimeMillis() - startTime;
		return result;
	}

	public static void main(String[] args) throws IOException {
		new MiniPlanner().plan(new PddlProblem("experiments/ma-benchmarks/beerproblem/beerproblem.pddl", "experiments/ma-benchmarks/beerproblem/beerproblem-01.pddl"));
	}
	
}
