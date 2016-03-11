package cz.agents.dimap.tools.polystar;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.agents.dimap.psm.operator.Binding;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.types.PddlType;

public class PolyStarRuleGrounder {
	
	public static boolean hasUnboundEffectVariable(PddlAction action) {
		return !action.precondition.getUsedVariables().containsAll(action.effect.getUsedVariables());
	}

	public static List<PddlAction> groundUnboundEffectVariable(PddlAction action, PddlProblem problem) {
		PddlName variable = getFirstUnboundEffectVariable(action);
		PddlType type = action.parameters.getBindings().get(variable);
		return computeGroundedInstances(action, variable, problem.getObjectsByType(type));		
	}
	
	private static PddlName getFirstUnboundEffectVariable(PddlAction action) {
		Set<PddlName> unbound = action.effect.getUsedVariables();
		unbound.removeAll(action.precondition.getUsedVariables());
		return unbound.iterator().next();
	}
	
	private static List<PddlAction> computeGroundedInstances(PddlAction action, PddlName variable, Collection<PddlName> varDomain) {
		List<PddlAction> groundedInstances = new LinkedList<>();
		for (PddlName groundedVal : varDomain) {
			PddlAction groundedInstance = action.clone();
			groundedInstance.parameters.removeAssignment(variable);
			groundTermsIn(groundedInstance.effect.positives, variable, groundedVal);
			groundTermsIn(groundedInstance.effect.negatives, variable, groundedVal);
			groundedInstances.add(groundedInstance);
		}
		return groundedInstances;
	}
	
	private static void groundTermsIn(Collection<PddlTerm> terms, PddlName var, PddlName varValue) {
		Binding binding = new Binding();
		binding.addBinding(var, varValue);
		Collection<PddlTerm> groundedTerms = new LinkedList<>();
		for (PddlTerm term : terms) {
			groundedTerms.add(PddlTerm.createGroundedPredicate(term, binding));
		}
		terms.clear();
		terms.addAll(groundedTerms);
	}

}
