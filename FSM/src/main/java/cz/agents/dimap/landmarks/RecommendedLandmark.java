package cz.agents.dimap.landmarks;

import java.io.Serializable;

import cz.agents.dimap.Settings;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlActionInstance;
import cz.agents.dimap.tools.pddl.PddlTerm;

public class RecommendedLandmark implements Landmark, Serializable {
    private static final long serialVersionUID = 7264597967456741053L;

    private final String agentName;
	private final int agentIndex;
	private final PddlAction action;
	private final long conditionId;
	private final long effectId;
	private final boolean ownerVerified;
	private final boolean shortcut;
	
	public RecommendedLandmark(String agentName, int agentIndex, PddlAction action, long conditionId, long effectId, boolean ownerVerified) {
		this(agentName, agentIndex, action, conditionId, effectId, ownerVerified, false);
	}
	
	public RecommendedLandmark(String agentName, int agentIndex, PddlAction action, long conditionId, long effectId, boolean ownerVerified, boolean shortcut) {
		this.agentName = agentName;
		this.agentIndex = agentIndex;
		this.action = PddlAction.createRenamedAction(action.name.replace(' ', '-'), action);
		this.action.setDefaultCost();
		this.conditionId = conditionId;
		this.effectId = effectId;
		this.ownerVerified = ownerVerified;
		this.shortcut = shortcut;
	}
	
	private String getPreconditionFact() {
		return "mark_"+agentName+"_"+(this.conditionId == -1 ? "init": this.conditionId);
	}

	private String getEffectFact() {
		return "mark_"+agentName+"_"+(this.effectId == -1 ? "init" : this.effectId);
	}

	private long getPublicCost() {
		if (shortcut) {
			return 300;
		}
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
        return "Landmark [name=" + action.name
                + ", " + conditionId + " -> " + effectId
                + ", ownerVerified=" + ownerVerified + ", shortcut=" + shortcut
                + "]";
    }

	@Override
	public void addToProblem(PddlLandmarkProblem problem) {
		PddlTerm preconditionFact = new PddlTerm(getPreconditionFact());
		PddlTerm effectFact = new PddlTerm(getEffectFact());
		problem.registerRecommendedLandmarkFact(preconditionFact);
		problem.registerRecommendedLandmarkFact(effectFact);
		
		String actionName = action.name.replace('-', ' ');
        PddlAction landmarkAction = PddlActionInstance.parsePublicAction(actionName, problem).clone();
        landmarkAction = PddlAction.createRenamedAction(action.name, landmarkAction);
		landmarkAction.addPrecondition(preconditionFact);
		landmarkAction.addPositiveEffect(effectFact);
		landmarkAction.addNegativeEffect(preconditionFact);
		landmarkAction.setActionCost(getPublicCost());		
		
		problem.addRecommendedLandmarkAction(landmarkAction);
		if (this.conditionId == -1) {
			problem.addRecommendedLandmarkInitFact(preconditionFact);
		}
	}

    @Override
    public PddlAction getAction() {
        return action;
    }

    @Override
    public String effectToString() {
        return Long.toString(effectId);
    }

    @Override
    public String conditionToString() {
        return Long.toString(conditionId);
    }
}
