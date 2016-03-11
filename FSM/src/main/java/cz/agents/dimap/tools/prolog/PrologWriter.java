package cz.agents.dimap.tools.prolog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;

public class PrologWriter extends FileWriter {

	public PrologWriter(File file) throws IOException {
		super(file);
	}
	
	public void writeAction(PddlAction action) throws IOException {
		writeFact("action", action.name, toProlog(action.precondition.positives), toProlog(action.effect.positives), toProlog(action.effect.negatives));
	}
	
	public void writeProblem(PddlProblem problem) throws IOException {
		for (PddlAction action : problem.domain.actions) {
			writeAction(action);
		}
		write(System.lineSeparator());
		List<String> inits = new LinkedList<>();
		for (PddlTerm term : problem.init.positives) {
			inits.add(toProlog(term)+"-true");
		}
		writeFact("init", inits.toString());
		writeFact("goal", toProlog(problem.goal.positives));
		write(System.lineSeparator());
	}
	
	public void writeTerm(String name, Object... args) throws IOException {
		write(name);
		if (args != null && args.length > 0) { write('('); }
		boolean comma = false;
		for (Object arg : args) {
			if (comma) { write(','); } else { comma = true; }
			write(arg.toString());
		}
		if (args != null && args.length > 0) { write(')'); }
	}

	public void writeFact(String name, Object... args) throws IOException {
		writeTerm(name, args);
		write('.');
		write(System.lineSeparator());
	}
	
	
	public static String toProlog(List<PddlTerm> terms) {
		List<String> strings = new LinkedList<>();
		for (PddlTerm term : terms) {
			strings.add(toProlog(term));
		}
		return strings.toString();
	}
	
	public static String toProlog(PddlTerm term) {
		return toProlog(term.name.name, term.arguments.toArray());
	}

	public static String toProlog(String name, Object... args) {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		if (args != null && args.length > 0) { builder.append('('); }
		boolean comma = false;
		for (Object arg : args) {
			if (comma) { builder.append(','); } else { comma = true; }
			builder.append(arg.toString());
		}
		if (args != null && args.length > 0) { builder.append(')'); }
		return builder.toString();
	}

	public static void main(String[] args) throws IOException {
		//PddlProblem problem = new PddlProblem(PsmFmapBenchmarks.FMAP_BENCHMARKS_DIR, problemFilename)
		//PddlDomain domain = new PddlDomain("experiments/FMAP-benchmarks/depots/Pfile01/DomainDepotsTruck.pddl");
		//PddlProblem problem = new PddlProblem("experiments/ma-benchmarks/satellites-ipc/satellites-ipc.pddl", "experiments/ma-benchmarks/satellites-ipc/satellites-ipc-01.pddl");
		//PddlProblem problem = new PddlProblem("experiments/ma-benchmarks/beerproblem-ground/beerproblem-grounded.pddl", "experiments/ma-benchmarks/beerproblem-ground/beerproblem-grounded-01.pddl");
		PddlProblem problem = new PddlProblem("experiments/projections/cities-a2/boat-domain.pddl", "experiments/projections/cities-a2/boat-problem.pddl");
		//PddlProblem problem = new PddlProblem("experiments/projections/satellites-ipc-a01/satellite0-domain.pddl", "experiments/projections/satellites-ipc-a01/satellite0-problem.pddl");
		//PddlProblem problem = new PddlProblem("experiments/projections/satellites-a02/satellite0-domain.pddl", "experiments/projections/satellites-a02/satellite0-problem.pddl");
		//PddlProblem problem = new PddlProblem("experiments/projections/woodworking-p01/glazer-domain.pddl", "experiments/projections/woodworking-p01/glazer-problem.pddl");
		problem.applyFluentDecomposition();
		
		PrologWriter writer = new PrologWriter(new File("problem.pl"));
		writer.writeProblem(problem);
		writer.close();
		
	}

	
}
