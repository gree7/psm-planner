package cz.agents.dimap.tools.miniplan.state;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;

public class MiniStateSet implements MiniState {

	public final Set<PddlTerm> have, wanted;
	
	public MiniStateSet(PddlProblem problem) {
		have = new HashSet<>(problem.init.positives);
		wanted = new HashSet<>(problem.goal.positives);
	}

	// apply constructor
	private MiniStateSet(MiniStateSet state, PddlAction action) {
		have = state.have;
		wanted = new HashSet<>(state.wanted);
		wanted.removeAll(action.effect.positives);
		wanted.addAll(action.precondition.positives);
	}
	
	@Override
	public List<PddlAction> getApplicableActions(PddlProblem problem) {
		List<PddlAction> acts = new LinkedList<PddlAction>();
		for (PddlAction action : problem.domain.actions) {
			if (isApplicable(action)) {
				acts.add(action);
			}
		}
		return acts;
	}

	@Override
	public MiniState apply(PddlAction action) {
		return new MiniStateSet(this, action);
	}

	@Override
	public boolean isSolved() {
		Set<PddlTerm> suffering = new HashSet<>(wanted);
		suffering.removeAll(have);
		return suffering.isEmpty();
	}

	@Override
	public boolean isApplicable(PddlAction action) {
		return 
			intersects(action.effect.positives, wanted) &&
			!intersects(action.effect.negatives, wanted);
	}
	
	private static <T> boolean intersects(Collection<T> s, Collection<T> t) {
		Set<T> intersection = new HashSet<>(s);
		intersection.retainAll(t);
		return !intersection.isEmpty();
	}

	@Override
	public String toString() {
		return wanted.toString().replace('[', '{').replace(']', '}').replace(", ", "|");
	}
	
	
	
	@Override
	public boolean equals(Object obj) {
		MiniStateSet other = (MiniStateSet)obj;
		return (this.have.equals(other.have) && this.wanted.equals(other.wanted));
	}
	
	@Override
	public int hashCode() {
		return have.hashCode() ^ wanted.hashCode();
	}

	@Override
	public boolean subsumedBy(MiniState other) {
		MiniStateSet otherSet = (MiniStateSet)other;
		//return this.wanted.containsAll(otherSet.wanted) && otherSet.blocked.containsAll(this.blocked);
		return this.wanted.containsAll(otherSet.wanted); // && this.blocked.containsAll(otherSet.blocked);
		
		//// not implemented
		//return this.equals(other);
	}
	
}
