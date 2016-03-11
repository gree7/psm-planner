package cz.agents.dimap.landmarks;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlTerm;

public class PddlLandmarkDomain extends PddlDomain {

	private List<PddlAction> recommendedActions = new LinkedList<>();
	private Set<PddlTerm> recommendedPredicates = new LinkedHashSet<>();
	
	public PddlLandmarkDomain(PddlDomain domain) {
		super(domain);
	}

	public void addRecommendedLandmarkAction(PddlAction landmarkAction) {
		recommendedActions.add(landmarkAction);
	}
	
	public void registerRecommendedLandmarkFact(PddlTerm fact) {
		recommendedPredicates.add(fact);
	}

	public void resetRecommendedLandmarks() {
		recommendedActions.clear();
	}
	
	@Override
	protected void appendActions(StringBuilder builder) {
		super.appendActions(builder);
		for (PddlAction a : recommendedActions) {
			builder.append(a);
		}
	}
	
	@Override
	protected void appendPredicates(StringBuilder builder) {
		super.appendPredicates(builder);
		for (PddlTerm f : recommendedPredicates) {
			builder.append("\t(").append(f).append(")\n");
		}
	}
	
	/*
	public void addRequiredLandmark() {
	}

	public void resetRequiredLandmarks() {
		System.err.println("Required landmarks can not be reseted at the moment. But it can be implemented.");
		throw new RuntimeException("Required landmarks can not be reseted at the moment. But it can be implemented.");
	}
	*/
}
