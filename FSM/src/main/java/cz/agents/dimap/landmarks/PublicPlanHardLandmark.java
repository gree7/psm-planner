package cz.agents.dimap.landmarks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import cz.agents.dimap.psm.operator.Binding;
import cz.agents.dimap.psm.operator.Operator;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlActionInstance;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.types.PddlPredicateType;

public class PublicPlanHardLandmark implements Landmark {

	List<PddlAction> landmark;
	
	public PublicPlanHardLandmark(List<Operator> publicPlan, PddlProblem agentProblem) {
	    this(agentProblem, operatorsToNames(publicPlan));
	}
	
    public static List<String> operatorsToNames(List<Operator> publicPlan) {
        List<String> opNames = new ArrayList<>(publicPlan.size());
        for (Operator operator : publicPlan) {
            opNames.add(operator.getName());
        }
        return opNames;
    }
    
    public PublicPlanHardLandmark(PddlProblem agentProblem, List<String> publicPlan) {
		landmark = new LinkedList<>();
		for (String operator : publicPlan) {
			String[] parts = operator.split(" ");
			String operatorName = parts[0];
			
			PddlAction agentAction = agentProblem.domain.getActionByPublicName(operatorName);
			Binding binding = createBinding(agentProblem, agentAction, parts);
			PddlActionInstance instance = new PddlActionInstance(agentAction, binding);
			
			landmark.add(instance);
		}
		
	}

	private Binding createBinding(PddlProblem agentProblem, PddlAction agentAction, String[] values) {
		Collection<PddlName> parameters;
		if (agentAction.isPublic()) {
			parameters = PddlAction.findPublicParameters(agentAction, agentProblem);
		}
		else {
			parameters = agentAction.parameters.getBindings().keySet();
		}
		
		if (parameters.size() != values.length - 1) {
			System.err.println("Invalid parameters size: "+parameters+" != "+values);
		}
		Binding binding = new Binding();
		int i = 1;
		for (PddlName param : parameters) {
			binding.addBinding(param, new PddlName(values[i++]));
		}
		return binding;
	}
	
	@Override
	public void addToProblem(PddlLandmarkProblem problem) {
		int markIndex = 0;
		PddlTerm currentMark = makeMark(markIndex);
		PddlTerm nextMark;
		
		problem.init.positives.add(currentMark);
		for (PddlAction action : landmark) {
	        addMarkToDomain(problem.domain, currentMark);
			nextMark = makeMark(++markIndex);
			problem.domain.actions.add(addMarkToAction(action, currentMark, nextMark));
			currentMark = nextMark;
		}

		addMarkToDomain(problem.domain, currentMark);
		
        problem.goal.positives.add(currentMark);
	}

    private void addMarkToDomain(PddlDomain domain, PddlTerm currentMark) {
        PddlName markName = new PddlName(currentMark.toString());
        domain.predicateTypes.put(markName, new PddlPredicateType(markName));
    }
	
	public static PddlAction addMarkToAction(PddlAction action, PddlTerm curr, PddlTerm next) {
		PddlAction marked = action.clone();
		marked.precondition.positives.add(curr);
		marked.effect.positives.add(next);
		marked.effect.negatives.add(curr);
		return marked;
	}
	
	public List<PddlAction> getLandmark() {
		return landmark;
	}
	
	private PddlTerm makeMark(int i) {
		return new PddlTerm(new PddlName("pp_mark_"+i));
	}

	@Override
	public String effectToString() {
		throw new RuntimeException("PlanHardLandmark.effectToString is not implemented.");
	}

	@Override
	public String conditionToString() {
		throw new RuntimeException("PlanHardLandmark.conditionToString is not implemented.");
	}

	@Override
	public PddlAction getAction() {
		throw new RuntimeException("PlanHardLandmark.getAction is not implemented.");
	}

}
