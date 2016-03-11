package cz.agents.dimap.tools.miniplan;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.agents.dimap.tools.GenericPlanner;
import cz.agents.dimap.tools.GenericResult;
import cz.agents.dimap.tools.dot.DotAttr;
import cz.agents.dimap.tools.dot.DotWriter;
import cz.agents.dimap.tools.fd.DownwardGrounder;
import cz.agents.dimap.tools.miniplan.state.MiniState;
import cz.agents.dimap.tools.miniplan.state.MiniStateSet;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class BackwardBfsMiniPlanner implements GenericPlanner {

	private static final boolean DOT_SEARCH = false;
	private static final boolean DOT_SUBSUMPTION = false; 
	
	private PddlProblem problem;
	private List<MiniState> unproc;
	private DotWriter writer;
	private Map<MiniState, String> stateIds;
	
	@Override
	public GenericResult plan(PddlProblem problem) {
		GenericResult result = new GenericResult();
		double startTime = System.currentTimeMillis();
		System.out.println("MiniPlanner :: Running FD grounder");
		this.problem = DownwardGrounder.groundProblem(problem);
		System.out.println("MiniPlanner :: Planning");
		try {
			result.solved = plan();
		} catch (IOException e) {
			result.solved = false;
		}
		result.planningTime = System.currentTimeMillis() - startTime;
		System.out.println((result.solved ? "SOLVED" : "UNSOLVABLE")+" in "+result.planningTime/1000.0);
		return result;
	}

	private boolean plan() throws IOException {
		stateIds = new LinkedHashMap<>();
		if (DOT_SEARCH) {
			try {
				writer = new DotWriter("backward-search");
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			writer.writeHeader();
			writer.writeGraphStyle(DotAttr.attr("overlap", "false"), DotAttr.attr("sep", "+20;20"));
		}
		
		MiniState startState = new MiniStateSet(problem);
		writeInit(startState);
		
		unproc = new LinkedList<>();
		unproc.add(startState);
		
		MiniState levelMarker = startState;
		Set<MiniState> seen = new HashSet<>();
		seen.add(startState);
		
		while (!unproc.isEmpty()) {
			//System.out.println("UNPROC: "+unproc);
			MiniState selected = unproc.remove(0);
			for (PddlAction action : selected.getApplicableActions(problem)) {
				MiniState next = selected.apply(action);
				if (next.isSolved()) {
					writeGoal(next);
					if (DOT_SEARCH) {
						writer.writeEdge(getStateId(selected), getStateId(next), DotAttr.label(action.name));
						writer.writeFooter();
						writer.close();
					}
					return true;
				}
				if (!seen.contains(next)) {
					MiniState better = better(seen, next);
					if (better != null) {
						if (DOT_SUBSUMPTION) { writer.writeEdge(getStateId(selected), getStateId(better), DotAttr.label(action.name), DotAttr.color("red"), DotAttr.style("dotted"), DotAttr.fontcolor("red")); }
						continue;
					}
					seen.add(next);
					unproc.add(0, next);
					if (DOT_SEARCH) { writer.writeEdge(getStateId(selected), getStateId(next), DotAttr.label(action.name)); }
				}
				else {
					if (DOT_SUBSUMPTION) { writer.writeEdge(getStateId(selected), getStateId(next), DotAttr.label(action.name), DotAttr.color("red"), DotAttr.fontcolor("red")); }
				}
			}

			/*if (selected == levelMarker) {
				if (!unproc.isEmpty()) { levelMarker = unproc.get(unproc.size()-1); }
				System.out.println("next level: "+seen.size());
			}*/

		}
		
		if (DOT_SEARCH) { writer.writeFooter(); writer.close(); }
		
		System.out.println("UNSOLVABLE!");

		return false;
	}
	
	private MiniState better(Set<MiniState> seen, MiniState next) {
		for (MiniState pred : seen) {
			if (next.subsumedBy(pred)) {
				System.out.println(pred+" < "+next);
				return pred;
			}
		}
		return null;
	}

	private void writeGoal(MiniState next) throws IOException {
		String id = Integer.toString(stateIds.size());
		stateIds.put(next, id);
		if (DOT_SEARCH) { writer.writeNode(id, DotAttr.label(getStateLabel(next)), DotAttr.fillcolor("green"), DotAttr.style("filled"), DotAttr.shape("Mrecord")); }
	}

	private void writeInit(MiniState startState) throws IOException {
		String id = Integer.toString(stateIds.size());
		stateIds.put(startState, id);
		if (DOT_SEARCH) { writer.writeNode(id, DotAttr.label(getStateLabel(startState)), DotAttr.fillcolor("lightblue"), DotAttr.style("filled"), DotAttr.shape("Mrecord")); }
	}

	private String getStateId(MiniState state) throws IOException {
		if (stateIds.containsKey(state)) {
			return stateIds.get(state);
		}
		
		String id = Integer.toString(stateIds.size());
		stateIds.put(state, id);
		if (DOT_SEARCH) { writer.writeNode(id, DotAttr.label(getStateLabel(state)), DotAttr.shape("Mrecord")); }
		return id;
	}

	private String getStateLabel(MiniState state) {
		return ((MiniStateSet)state).toString();
	}

	public static void main(String[] args) throws IOException {
		//new BackwardBfsMiniPlanner().plan(new PddlProblem("experiments/ma-benchmarks/beerproblem/beerproblem.pddl", "experiments/ma-benchmarks/beerproblem/beerproblem-01.pddl"));
		new BackwardBfsMiniPlanner().plan(new PddlProblem("../benchmarks/satellite/domain.pddl", "../benchmarks/satellite/p01-pfile1.pddl"));
	}
	
}
