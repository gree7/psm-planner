package cz.agents.dimap.tools.pddl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import cz.agents.dimap.psm.operator.Binding;
import cz.agents.dimap.tools.pddl.expr.SExpr;

public class PddlCondition implements Serializable {
    private static final long serialVersionUID = -5807221499442617039L;

    public List<PddlTerm> positives;
	public List<PddlTerm> negatives;
	public Map<PddlTerm, PddlName> equalities;
	public Map<PddlTerm, PddlName> assigns;
	
	private long actionCost = 0;
	
	public PddlCondition() {
		positives = new LinkedList<PddlTerm>();
		negatives = new LinkedList<PddlTerm>();
		equalities = new TreeMap<PddlTerm, PddlName>();
		assigns = new TreeMap<PddlTerm, PddlName>();
	}

	public PddlCondition(SExpr expr) {
		this();
		
		if (expr.isAndExpr()) {
			addLiterals(expr);
		}
		else if (expr.isPredicate() || expr.isEquality() || expr.isIncrease() || expr.isAssign()) {
			addLiteral(expr);
		}
		else if (!expr.isEmptyList()){
			System.err.println("PDDL ERROR: Unrecognized condition: "+expr);
		}
		
		Collections.sort(positives);
		Collections.sort(negatives);
	}

	public PddlCondition(PddlCondition other) {
		positives = new LinkedList<>(other.positives);
		negatives = new LinkedList<>(other.negatives);
		equalities = new TreeMap<PddlTerm, PddlName>(other.equalities);
		assigns = new TreeMap<PddlTerm, PddlName>(other.assigns);
		actionCost = other.actionCost;
	}

	public PddlCondition(List<PddlTerm> positives, List<PddlTerm> negatives) {
        this.positives = new ArrayList<>(positives);
        this.negatives = new ArrayList<>(negatives);
        equalities = new TreeMap<PddlTerm, PddlName>();
        assigns = new TreeMap<PddlTerm, PddlName>();
    }

    public static PddlCondition createProjectedCondition(PddlCondition condition, Collection<PddlTerm> facts) {
	    PddlCondition newCondition = new PddlCondition( condition );
	    newCondition.positives.retainAll(facts);
	    newCondition.negatives.retainAll(facts);
	    return newCondition;
	}
    
    public static PddlCondition createProjectedConditionToPredicateNames(PddlCondition condition, PddlProblem problem) {
        PddlCondition newCondition = new PddlCondition();
        for (PddlTerm predicate : condition.positives) {
            if (problem.isPublicPredicate(predicate)) {
                newCondition.positives.add(predicate);
            }
        }
        for (PddlTerm predicate : condition.negatives) {
            if (problem.isPublicPredicate(predicate)) {
                newCondition.negatives.add(predicate);
            }
        }
        return newCondition;
    }

    public static PddlCondition createCondition(List<PddlTerm> positives, List<PddlTerm> negatives) {
        return new PddlCondition(positives, negatives);
    }
    
    public static PddlCondition renamePredicates(PddlCondition condition, Map<PddlTerm, PddlTerm> predicateRenames) {
        return new PddlCondition(
                renamePredicates(condition.positives, predicateRenames),
                renamePredicates(condition.negatives, predicateRenames));
    }

	private static List<PddlTerm> renamePredicates(List<PddlTerm> predicates, Map<PddlTerm, PddlTerm> predicateRenames) {
        List<PddlTerm> newPredicates = new ArrayList<>(predicates.size());
        for (PddlTerm predicate : predicates) {
            newPredicates.add( predicateRenames.get(predicate) );
        }
        return newPredicates;
    }
	
    public static PddlCondition createGroundedCondition(PddlCondition condition, Binding binding) {
        List<PddlTerm> positives = convertGroundedPredicates(condition.positives, binding);
        List<PddlTerm> negatives = convertGroundedPredicates(condition.negatives, binding);
        return PddlCondition.createCondition(positives, negatives);
    }

    public static PddlCondition relax(PddlCondition condition) {
        return PddlCondition.createCondition(condition.positives, new ArrayList<PddlTerm>());
    }

    private static List<PddlTerm> convertGroundedPredicates(List<PddlTerm> predicates, Binding binding) {
        List<PddlTerm> grounded = new ArrayList<>(predicates.size());
        for (PddlTerm predicate : predicates) {
            grounded.add( PddlTerm.createGroundedPredicate(predicate, binding));
        }
        return grounded;
    }
    
    public static PddlCondition createUnion(PddlCondition init1, PddlCondition init2) {
        Set<PddlTerm> positives = new HashSet<>(init1.positives);
        positives.addAll(init2.positives);
        Set<PddlTerm> negatives = new HashSet<>(init1.negatives);
        negatives.addAll(init2.negatives);
        return new PddlCondition(new ArrayList<>(positives), new ArrayList<>(negatives));
    }


    public boolean hasIncreaseEffect() {
		return actionCost > 0;
	}
	
	public void addPositiveCondition(PddlTerm fact) {
		positives.add(fact);
	}

	public void addNegativeCondition(PddlTerm fact) {
		negatives.add(fact);
	}
	
	public void clear() {
		positives.clear();
		negatives.clear();
		equalities.clear();
		assigns.clear();
		actionCost = 0;
	}
	
	private void addLiterals(SExpr expr) {
		expr.getList().remove(0);
		for (SExpr e : expr.getList()) {
			addLiteral(e);
		}
	}
	
	private void addLiteral(SExpr e) {
		if (e.isPositivePredicate()) {
			positives.add(new PddlTerm(e));
		}
		else if (e.isNegativePredicate()) {
			negatives.add(new PddlTerm(e.e(1)));
		}
		else if (e.isEquality()) {
			equalities.put(new PddlTerm(e.e(1)), PddlName.createFixedName(e.e(2).getValue()));
		}
		else if (e.isAssign()) {
			assigns.put(new PddlTerm(e.e(1)), PddlName.createFixedName(e.e(2).getValue()));
		}
		else if (e.isIncrease()) {
			setIncrease(e);
		}
		else {
			System.out.println("WARNING: Unrecognized literal: "+e);
		}
	}
	
	public void removeContradictingEffects() {
	    List<PddlTerm> contradictingEffects = new ArrayList<>(positives);
	    contradictingEffects.retainAll(negatives);
	    positives.removeAll(contradictingEffects);
	    negatives.removeAll(contradictingEffects);
	}

	private void setIncrease(SExpr expr) {
		actionCost = Integer.parseInt(expr.e(2).getValue());
	}

	@Override
	public String toString() {
		if (size() == 0) {
			return "()";
		}
		if (size() == 1 && !(positives.isEmpty() && negatives.isEmpty()) ) {
			String one = positives.isEmpty() ? "(not ("+negatives.get(0)+"))" : "("+positives.get(0)+")";
			return one;
		}
		StringBuilder builder = new StringBuilder();
		builder.append("(and\n");
		for (PddlTerm f : positives) {
			builder.append("\t\t(").append(f).append(")\n");
		}
		for (PddlTerm f : negatives) {
			builder.append("\t\t(not (").append(f).append("))\n");
		}
		for (Entry<PddlTerm, PddlName> entry : equalities.entrySet()) {
			builder.append("\t\t(= (").append(entry.getKey()).append(") ").append(entry.getValue()).append(")\n");
		}
		for (Entry<PddlTerm, PddlName> entry : assigns.entrySet()) {
			builder.append("\t\t(assign (").append(entry.getKey()).append(") ").append(entry.getValue()).append(")\n");
		}
		if (hasIncreaseEffect()) {
			builder.append("\t\t(increase (total-cost) ").append(actionCost).append(")\n");
		}
		builder.append("\t)");
		return builder.toString();
	}

	public String toStringAsPositiveList() {
		StringBuilder builder = new StringBuilder();
		for (PddlTerm f : positives) {
			builder.append("\t(").append(f).append(")\n");
		}
		for (Entry<PddlTerm, PddlName> entry : equalities.entrySet()) {
			builder.append("\t(= (").append(entry.getKey()).append(") ").append(entry.getValue()).append(")\n");
		}
		return builder.toString();
	}

	public int size() {
		return positives.size() + negatives.size() + equalities.size() + assigns.size() + (actionCost > 0 ? 1 : 0);
	}
	
	@Override
	public PddlCondition clone() {
		return new PddlCondition(this);
	}

	public Set<PddlTerm> getFacts() {
	    Set<PddlTerm> facts = new HashSet<>();
        facts.addAll(positives);
        facts.addAll(negatives);
        return facts;
	}

    public boolean isEmpty() {
        return positives.isEmpty() && negatives.isEmpty() && !hasIncreaseEffect();
    }

    public void setActionCost(long costValue) {
        actionCost = costValue;
    }
    
	public void removeActionCost() {
		actionCost = 0;
	}
    
    public void applyEqualitiesFluentDecomposition() {
		for (Entry<PddlTerm, PddlName> equality : equalities.entrySet()) {
			PddlTerm equalityTerm = equality.getKey();
			equalityTerm.arguments.add(equality.getValue());
			positives.add(equalityTerm);	
		}
		equalities.clear();
    }
    
    public Set<PddlName> getUsedVariables() {
    	Set<PddlName> ret = new HashSet<>();
    	ret.addAll(getUsedVariablesIn(positives));
    	ret.addAll(getUsedVariablesIn(negatives));
    	ret.addAll(getUsedVariablesIn(assigns.keySet()));
    	ret.addAll(getUsedVariablesIn(equalities.keySet()));
    	return ret;
    }

	private Set<PddlName> getUsedVariablesIn(Collection<PddlTerm> terms) {
    	Set<PddlName> ret = new HashSet<>();
    	for (PddlTerm term : terms) {
    		for (PddlName arg : term.arguments) {
    			if (arg.isVariable()) {
    				ret.add(arg);
    			}
    		}
    	}
    	return ret;
	}

}
