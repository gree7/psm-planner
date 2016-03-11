package cz.agents.dimap.landmarks;

import java.util.List;

import cz.agents.dimap.Settings;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlTerm;

public class RecommendedMultiLandmark implements Landmark {
	
	private final String agentName;
	private final int agentIndex;
	private final String publicActionName;
	private final List<String> conditionIds;
	private final List<String> effectIds;
	private final boolean ownerVerified;
	
	public RecommendedMultiLandmark(String agentName, int agentIndex, String publicActionName, List<String> conditionId, List<String> effectId, boolean ownerVerified) {
		this.agentName = agentName;
		this.agentIndex = agentIndex;
		this.publicActionName = publicActionName;
		this.conditionIds = conditionId;
		this.effectIds = effectId;
		this.ownerVerified = ownerVerified;
	}
	
	private String getFact(String factId) {
		return "mark_"+agentName+"_"+factId;
	}

	private long getPublicCost() {
		if (ownerVerified) {
			return 1;
		}
		if (Settings.USE_AGENT_INDEXES) {
			return 11+agentIndex;
		}
		else {
			return 11;
		}
	}

    @Override
    public String toString() {
        return "RecommendedMultiLandmark [publicActionName=" + publicActionName
                + ", " + conditionIds + " -> " + effectIds
                + ", ownerVerified=" + ownerVerified
                + "]";
    }

	@Override
	public void addToProblem(PddlLandmarkProblem problem) {
		PddlAction originAction = problem.getDomain().getActionByPublicName(publicActionName);
		PddlAction landmarkAction = originAction.clone();
		for (String condId : conditionIds) {
	        PddlTerm preconditionFact = new PddlTerm(getFact(condId));
	        problem.registerRecommendedLandmarkFact(preconditionFact);
	        landmarkAction.addPrecondition(preconditionFact);
            landmarkAction.addNegativeEffect(preconditionFact);

            if (condId.startsWith("init-")) {
                problem.addRecommendedLandmarkInitFact(preconditionFact);
            }
		}

        for (String effectId : effectIds) {
            PddlTerm effectFact = new PddlTerm(getFact(effectId));
            problem.registerRecommendedLandmarkFact(effectFact);
            landmarkAction.addPositiveEffect(effectFact);
        }
		landmarkAction.setActionCost(getPublicCost());		
		
		problem.addRecommendedLandmarkAction(landmarkAction);
	}

    @Override
    public PddlAction getAction() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String effectToString() {
        return effectIds.toString();
    }

    @Override
    public String conditionToString() {
        return conditionIds.toString();
    }
}
