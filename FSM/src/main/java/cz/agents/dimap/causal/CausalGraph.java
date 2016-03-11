package cz.agents.dimap.causal;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import cz.agents.dimap.landmarks.Landmark;
import cz.agents.dimap.landmarks.RequiredLandmark;
import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.dot.DotTools;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.PddlProblem;
import experiments.psm.PsmBenchmarks;
import experiments.psm.PsmFmapBenchmarks;
import experiments.psm.fmap.FmapMultiAgentProblem;
import experiments.psm.fmap.FmapSingleDomainInfo;
import experiments.psm.fmap.MaStripsMultiAgentProblem;

@Deprecated // needs correction for parametric predicates
public class CausalGraph {

	public PddlProblem problem;
	public Map<PddlTerm, FactCausalInfo> facts;
	public Collection<PddlAction> actions;
	
	public CausalGraph(PddlProblem problem) {
		actions = problem.getDomain().actions;
		facts = new LinkedHashMap<PddlTerm, FactCausalInfo>();
		this.problem = problem;
		for (PddlAction action : actions) {
			for (PddlTerm f : action.precondition.positives) {
				getFactInfo(f).addDependant(action);
			}
			for (PddlTerm f : action.effect.positives) {
				getFactInfo(f).addSupporter(action);
			}
			for (PddlTerm f : action.effect.negatives) {
				getFactInfo(f).addDisposer(action);
			}
		}
	}
	
	private FactCausalInfo getFactInfo(PddlTerm fact) {
		if (!facts.containsKey(fact)) {
			facts.put(fact, new FactCausalInfo(fact));
		}
		return facts.get(fact);
	}

	public Collection<Landmark> computeRequiredCausalLandmarks(String agentName) {
		/*
		long id = 0;
		Collection<Landmark> ret = new LinkedList<Landmark>();
		for (FactCausalInfo factInfo : facts.values()) {
			if (!factInfo.hasInternalDependancy && factInfo.fact.isInternal()) {
				boolean init = problem.init.positives.contains(factInfo.fact);
				ret.add(new RequiredLandmark(factInfo, agentName, init, Long.toString(id++)));
			}
		}
		return ret;
		*/
		return null; // TODO: fix me
	}
	
	
	
	public static void main(String[] args) throws Exception {
		FmapSingleDomainInfo di = new FmapSingleDomainInfo("logistics", 20, "DomainLogistics", "ProblemLog");
		FmapMultiAgentProblem fmapProblem = di.createProblem(1);
		MultiAgentProblem maProblem = new MaStripsMultiAgentProblem(fmapProblem);
		PddlProblem problem = maProblem.toSingleAgentProblem();
		System.out.println(problem.domain);
		CausalGraph cg = new CausalGraph(problem);
		DotTools.dotCausalGraph(cg, "logistics-cg");
	}
	
}
