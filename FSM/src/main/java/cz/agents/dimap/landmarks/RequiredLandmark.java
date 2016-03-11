package cz.agents.dimap.landmarks;

import cz.agents.dimap.causal.FactCausalInfo;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.types.PddlPredicateType;

public class RequiredLandmark implements Landmark {
	
	FactCausalInfo factInfo;
	String agentName;
	String id;
	boolean init;

	public RequiredLandmark(FactCausalInfo factInfo, String agentName, boolean init, String id) {
		this.factInfo = factInfo; 
		this.agentName = agentName;
		this.init = init;
		this.id = id;
	}

	@Override
	public void addToProblem(PddlLandmarkProblem problem) {
		
		PddlTerm unlocked = new PddlTerm(new PddlName("unlock-"+agentName+"-"+id));
		
		if (init) {
			problem.addRequiredLandmarkInitFact(unlocked);
		}
		
		if (!problem.getDomain().predicateTypes.containsKey(unlocked)) {
			problem.getDomain().predicateTypes.put(unlocked.name, new PddlPredicateType(unlocked.name));
		}
		
		PddlAction act;
		for (PddlAction dep : factInfo.getDependants()) {
			act = problem.getDomain().getActionByPublicName(dep.getName());
			if (act == null) {
				System.err.println("FATAL ERROR: Action "+dep.getName()+" of agent "+this.agentName+" should be public but I can not find it among my actions.");
			}
			else {
				act.addPrecondition(unlocked);
			}
		}
		for (PddlAction sup : factInfo.getSupporters()) {
			act = problem.getDomain().getActionByPublicName(sup.getName());
			if (act == null) {
				System.err.println("FATAL ERROR: Action "+sup.getName()+" of agent "+this.agentName+" should be public but I can not find it among my actions.");
			}
			else {
				act.addPositiveEffect(unlocked);
			}
		}
		for (PddlAction dis : factInfo.getDisposers()) {
			act = problem.getDomain().getActionByPublicName(dis.getName());
			if (act == null) {
				System.err.println("FATAL ERROR: Action "+dis.getName()+" of agent "+this.agentName+" should be public but I can not find it among my actions.");
			}
			else {
				act.addNegativeEffect(unlocked);
			}
		}
	}
	
	@Override
	public String toString() {
		return factInfo.fact.toString();
	}

    @Override
    public PddlAction getAction() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String effectToString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String conditionToString() {
        throw new UnsupportedOperationException();
    }
}
