package cz.agents.dimap.tools.miniplan.state;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;

public class MiniStateSetOld implements MiniState {

	public final Set<PddlTerm> have, wanted, blocked;
	
	public MiniStateSetOld(PddlProblem problem) {
		have = new HashSet<>(problem.init.positives);
		wanted = new HashSet<>(problem.goal.positives);
		blocked = new HashSet<>();
	}

	// apply constructor
	private MiniStateSetOld(MiniStateSetOld state, PddlAction action) {
		have = state.have;
		wanted = new HashSet<>(state.wanted);
		wanted.removeAll(action.effect.positives);
		wanted.addAll(action.precondition.positives);
		wanted.removeAll(have);
		blocked = new HashSet<>(state.blocked);
		blocked.removeAll(action.effect.positives);
		Set<PddlTerm> blocking = new HashSet<>(action.precondition.positives);
		blocking.retainAll(have);
		blocked.addAll(blocking);
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
		return new MiniStateSetOld(this, action);
	}

	@Override
	public boolean isSolved() {
		return wanted.isEmpty();
	}

	@Override
	public boolean isApplicable(PddlAction action) {
		return 
			intersects(action.effect.positives, wanted) &&
			!intersects(action.effect.negatives, blocked) &&
			!intersects(action.effect.negatives, wanted);
	}
	
	private static <T> boolean intersects(Collection<T> s, Collection<T> t) {
		Set<T> intersection = new HashSet<>(s);
		intersection.retainAll(t);
		return !intersection.isEmpty();
	}

	@Override
	public String toString() {
		String wantedStr = wanted.toString().replace('[', '{').replace(']', '}').replace(", ", "|");
		String blockedStr = blocked.toString().replace('[', '{').replace(']', '}').replace(", ", "|");
		return String.format("%s|%s", wantedStr, blockedStr);
	}
	
	
	
	@Override
	public boolean equals(Object obj) {
		MiniStateSetOld other = (MiniStateSetOld)obj;
		return (this.have.equals(other.have) && this.wanted.equals(other.wanted) && this.blocked.equals(other.blocked));
	}
	
	@Override
	public int hashCode() {
		return have.hashCode() ^ wanted.hashCode() ^ blocked.hashCode();
	}

	@Override
	public boolean subsumedBy(MiniState other) {
		MiniStateSetOld otherSet = (MiniStateSetOld)other;
		//return this.wanted.containsAll(otherSet.wanted) && otherSet.blocked.containsAll(this.blocked);
		return this.wanted.containsAll(otherSet.wanted) && this.blocked.containsAll(otherSet.blocked);
		
		//// not implemented
		//return this.equals(other);
	}
	
}
