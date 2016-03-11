package cz.agents.dimap.tools.polystar;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.agents.dimap.Settings;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlCondition;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlProblem.PddlProblemStatus;
import cz.agents.dimap.tools.pddl.PddlTerm;

public class PolyStarGoalReachebility {

	public static PddlProblemStatus computeGoalReachebilityStatus(PddlProblem problem) throws InterruptedException {
		String polyStarType = computeType(problem);
		if (polyStarType == null) {
			return PddlProblemStatus.UNKNOW;
		}
		if (!polyStarType.isEmpty() && !polyStarType.contains("GOAL")) {
			return PddlProblemStatus.UNSOLVABLE;
		}
		return PddlProblemStatus.UNKNOW;
	}

	public static int computeLastReachableMark(PddlProblem problem) throws InterruptedException {
		String polyStarType = computeType(problem);
		if (polyStarType == null) {
			return 0;
		}
		return extractLastMark(polyStarType);
	}

	private static String computeType(PddlProblem problem) throws InterruptedException {
		String polyStarInstance = PolyStarGoalReachebility.generatePolyStarInstance(problem);
		if (Settings.DEBUG) {
			System.out.println("\nPOLY*: Goal Reachability instance generated:\n");
			System.out.println(polyStarInstance);
			System.err.println();
		}
		String polyStarType;
		try {
			polyStarType = PolyStar.computeType(polyStarInstance);
		} catch (IOException e) {
			System.err.println("Poly* ERROR: "+e.getMessage());
			if (Settings.DEBUG) { e.printStackTrace(); }
			return null;
		}
		catch (InterruptedException e) {
			System.err.println("Poly*: Interrupted");
			throw e;
		}
		return polyStarType;
	}
	
	private static int extractLastMark(String polyStarType) {
		int lastMark = 0;
		Pattern markPat = Pattern.compile("pp_mark_(\\d+)");
		Matcher matcher = markPat.matcher(polyStarType);
		while (matcher.find()) {
			 lastMark = Math.max(lastMark, Integer.parseInt(matcher.group(1)));
		}
		return lastMark;
	}

	public static String generatePolyStarInstance(PddlProblem problem) {
		StringBuilder builder = new StringBuilder();
		builder.append(generateOptions());
		builder.append(generateActionRewritingRules(problem));
		builder.append(generateGoalMarkerRule(problem.goal));
		builder.append(generateInitProcess(problem));
		return builder.toString();
	}

	// privates
	private static String generateOptions() {
		return "option { usemarks off }\n\n";
	}

	private static String generateActionRewritingRules(PddlProblem problem) {
		StringBuilder builder = new StringBuilder();
		for (PddlAction action : problem.domain.actions) {
			builder.append(generateRewritingRule(action, problem)).append("\n");
		}
		return builder.append("\n").toString();
	}

	private static String generateGoalMarkerRule(PddlCondition goal) {
		StringBuilder builder = new StringBuilder();
		builder.append("reduce { \n").append("\t");
		for (PddlTerm goalCond : goal.positives) {
			builder.append(generateProcessTemplate(goalCond)).append(" | ");	
		}
		builder.append("P -->").append("\n");
		builder.append("\t").append("\"GOAL\".0").append("\n");
		builder.append("}\n\n");
		return builder.toString();
	}
	
	private static String generateRewritingRule(PddlAction action, PddlProblem problem) {
		if (PolyStarRuleGrounder.hasUnboundEffectVariable(action)) {
			return generatePartiallyGroundedRewritingRule(action, problem);
		}
		else {
			return generateParametricRewritingRule(action);
		}
	}

	private static String generatePartiallyGroundedRewritingRule(PddlAction action, PddlProblem problem) {
		StringBuilder builder = new StringBuilder();
		for (PddlAction groundedAction : PolyStarRuleGrounder.groundUnboundEffectVariable(action, problem)) {
			builder.append(generateRewritingRule(groundedAction, problem));
		}
		return builder.toString();
	}

	private static String generateParametricRewritingRule(PddlAction action) {
		StringBuilder builder = new StringBuilder();
		builder.append("reduce { \n");
		builder.append("\t").append(generateLhs(action.precondition)).append("\n");
		builder.append("-->").append("\n");
		builder.append("\t").append(generateRhs(action)).append("\n");
		builder.append("}");
		return builder.toString();
	}

	private static String generateLhs(PddlCondition precondition) {
		StringBuilder builder = new StringBuilder();
		for (PddlTerm pre : precondition.positives) {
			builder.append(generateProcessTemplate(pre)).append(" | ");
		}
		builder.append("P");
		return builder.toString();
	}

	private static String generateRhs(PddlAction action) {
		StringBuilder builder = new StringBuilder();
		for (PddlTerm pre : action.precondition.positives) {
			if (!action.effect.negatives.contains(pre)) {
				builder.append(generateProcessTemplate(pre)).append(" | ");
			}
		}
		for (PddlTerm eff : action.effect.positives) {
			builder.append(generateProcessTemplate(eff)).append(" | ");
		}
		if (Settings.VERBOSE || Settings.DEBUG) {
			for (PddlTerm eff : action.effect.negatives) {
				if (!action.precondition.positives.contains(eff)) {
					System.err.println("Poly* TRANSLATION WARNING: Action has negative effect ("+eff+") which is not a positive precondition:"+(Settings.DEBUG ? "\n"+action : " "+action.name));
				}
			}
		}
		builder.append("P");
		return builder.toString();
	}
	
	private static String generateProcessTemplate(PddlTerm pre) {
		StringBuilder builder = new StringBuilder();
		builder.append(generateTemplateName(pre.name));
		for (PddlName arg : pre.arguments) {
			builder.append(".").append(arg.isVariable() ? generateTemplateVariable(arg) : generateTemplateName(arg));
		}
		builder.append(".0");
		return builder.toString();
	}

	private static Object generateTemplateVariable(PddlName arg) {
		return "var__"+arg.name.substring(1);
	}
	
	private static Object generateTemplateName(PddlName arg) {
		return '"'+arg.name+'"';
	}
	
	private static Object generateInitProcess(PddlProblem problem) {
		StringBuilder builder = new StringBuilder();
		for (PddlTerm init : problem.init.positives) {
			builder.append(generateProcess(init));
			builder.append(" | \n");
		}
		if (builder.length() > 0) { builder.setLength(builder.length() - 4); }
		return builder.toString();
	}

	private static String generateProcess(PddlTerm pre) {
		StringBuilder builder = new StringBuilder();
		builder.append(pre.name);
		for (PddlName arg : pre.arguments) {
			builder.append(".").append(arg);
		}
		builder.append(".0");
		return builder.toString();
	}

	
	
}
