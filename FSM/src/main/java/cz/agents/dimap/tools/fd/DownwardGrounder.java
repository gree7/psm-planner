package cz.agents.dimap.tools.fd;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.agents.dimap.psm.operator.Binding;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlActionInstance;
import cz.agents.dimap.tools.pddl.PddlCondition;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.types.PddlPredicateType;
import cz.agents.dimap.tools.pddl.types.PddlTypeMap;
import cz.agents.dimap.tools.prolog.PrologWriter;
import cz.agents.dimap.tools.sas.SasFile;

public class DownwardGrounder {

	public static PddlProblem groundProblem(PddlProblem problem) {
		PddlProblem grounded = problem.clone();
		grounded.domain.actions.clear();
		SasFile sas = Downward.runTranslator(problem);
		for (String op : sas.operatorNames) {
			String parts[] = op.split(" ");
			PddlAction action = problem.domain.getActionByName(parts[0]);
			Binding subst = createSubstitution(parts, action);
			PddlAction groundedAction = new PddlActionInstance(action, subst, true);
			//System.out.println(groundedAction);
			grounded.domain.actions.add(groundedAction);
		}
		atomize(grounded);
		removeContradictingEffects(grounded);
		removePermanentFacts(grounded);
		return grounded;
	}
	
    private static void removeContradictingEffects(PddlProblem problem) {
        for (PddlAction action : problem.domain.actions) {
            Collection<PddlTerm> badEffects = new HashSet<>(action.effect.positives);
            badEffects.retainAll(action.effect.negatives);
            action.effect.positives.removeAll(badEffects);
            action.effect.negatives.removeAll(badEffects);
        }
    }

	private static void removePermanentFacts(PddlProblem grounded) {
		// detect static facts
		Set<PddlTerm> staticFacts = new LinkedHashSet<>(grounded.init.positives);
		for (PddlAction action : grounded.domain.actions) {
			staticFacts.removeAll(action.effect.negatives);
		}
		// remove them from init, goal, and actions
		grounded.init.positives.removeAll(staticFacts);
		grounded.goal.positives.removeAll(staticFacts);
		for (PddlAction action : grounded.domain.actions) {
			action.precondition.positives.removeAll(staticFacts);
			action.effect.positives.removeAll(staticFacts);
			staticFacts.removeAll(action.effect.negatives);
		}
		for (PddlTerm fact : staticFacts) {
	        grounded.domain.predicateTypes.remove(fact.name);
        }
	}

	private static void atomize(PddlProblem grounded) {
		atomize(grounded.init);
		atomize(grounded.goal);
		atomize(grounded.domain);
		grounded.objects = new PddlTypeMap<PddlName>();
	}

	private static void atomize(PddlDomain domain) {
	    Set<PddlTerm> predicates = new HashSet<>();
		for (PddlAction action : domain.actions) {
			atomize(action);
            predicates.addAll(action.precondition.positives);
            predicates.addAll(action.effect.getFacts());
			//action.name = action.name.replace(' ', '_');
		}
	    domain.predicateTypes.clear();
	    for (PddlTerm term : predicates) {
	        PddlPredicateType type = new PddlPredicateType(term.name);
	        domain.predicateTypes.put(type.predicateName, type);
        }
	}

	private static void atomize(PddlAction action) {
		atomize(action.precondition);
		atomize(action.effect);
	}

	private static void atomize(PddlCondition cond) {
		cond.positives = atomizedClone(cond.positives); 
		cond.negatives = atomizedClone(cond.negatives); 
	}

	private static List<PddlTerm> atomizedClone(List<PddlTerm> terms) {
		List<PddlTerm> atoms = new LinkedList<>();
		for (PddlTerm term : terms) {
			atoms.add(new PddlTerm(term.toString().replace(' ', '_')));
		}
		return atoms;
	}

	private static Binding createSubstitution(String[] parts, PddlAction action) {
		Binding subst = new Binding();
		int i = 1;
		for (PddlName arg : action.parameters.getObjects()) {
			subst.addBinding(arg, new PddlName(parts[i++]));
		}
		return subst;
	}

	public static void main(String[] args) throws IOException {
		PddlProblem problem = new PddlProblem(
			"/home/yan/tmp/ipc/demo-instances/sequential/openstacks-strips/p01-domain.pddl", 
			"/home/yan/tmp/ipc/demo-instances/sequential/openstacks-strips/p01.pddl"
		);
		PddlProblem grounded = groundProblem(problem);

		PrologWriter writer = new PrologWriter(new File("problem.pl"));
		writer.writeProblem(grounded);
		writer.close();

	}
	
}
