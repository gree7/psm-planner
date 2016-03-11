package cz.agents.dimap.landmarks;

import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlCondition;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;

public class PddlLandmarkProblem extends PddlProblem {

	public PddlCondition recommendedInitFacts;
	public PddlCondition requiredInitFacts;
	
	public PddlLandmarkProblem(PddlProblem problem) {
		super(problem);
		domain = new PddlLandmarkDomain(problem.domain);
		recommendedInitFacts = new PddlCondition();
		requiredInitFacts = new PddlCondition();
	}

	@Override
	public PddlLandmarkDomain getDomain() {
		return (PddlLandmarkDomain)domain;
	}
	
	public void addRecommendedLandmarkAction(PddlAction landmarkAction) {
		getDomain().addRecommendedLandmarkAction(landmarkAction);
	}
	
	public void addRecommendedLandmarkInitFact(PddlTerm landmarkInitFact) {
		recommendedInitFacts.addPositiveCondition(landmarkInitFact);
	}

	public void addRequiredLandmarkInitFact(PddlTerm landmarkInitFact) {
		requiredInitFacts.addPositiveCondition(landmarkInitFact);
	}

	public void registerRecommendedLandmarkFact(PddlTerm fact) {
		getDomain().registerRecommendedLandmarkFact(fact);
	}
	
	public void resetRecommendedLandmarks() {
		recommendedInitFacts.clear();
		getDomain().resetRecommendedLandmarks();
	}

	public void resetRequiredLandmarks() {
		System.err.println("Required landmarks can not be reseted at the moment. But it can be implemented.");
		throw new RuntimeException("Required landmarks can not be reseted at the moment. But it can be implemented.");
	}
	
	@Override
	protected void appendInit(StringBuilder builder) {
		super.appendInit(builder);
		builder.append(recommendedInitFacts.toStringAsPositiveList());
		builder.append(requiredInitFacts.toStringAsPositiveList());
	}
	
}
