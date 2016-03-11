package cz.agents.dimap.tools.miniplan.state;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;

public class MiniStateList implements MiniState {

	public final List<PddlTerm> have, wanted, blocked;

	public MiniStateList(PddlProblem problem) {
		have = problem.init.positives;
		wanted = problem.goal.positives;
		blocked = new LinkedList<>();
	}

	
	private MiniStateList(MiniStateList other) {
		this.have = new LinkedList<>(other.have);
		this.wanted = new LinkedList<>(other.wanted);
		this.blocked = new LinkedList<>(other.blocked);
	}
	
	// apply constructor
	private MiniStateList(MiniStateList state, PddlAction action) {
		this(state);
		
		for (PddlTerm pre : action.precondition.positives) {
			if (have.contains(pre)) {
				have.remove(pre);
				blocked.add(pre);
			}
			else 
			if (blocked.contains(pre)) {
				// keep in block
			}
			else {
				if (!wanted.contains(pre)) {
					wanted.add(pre);
				}
			}
		}
		
		for (PddlTerm add : action.effect.positives) {
			if (wanted.contains(add)) {
				wanted.remove(add);
			}
			if (blocked.contains(add)) {
				blocked.remove(add);
				have.add(add);
			}
		}
		
		wanted.removeAll(action.effect.positives);
		blocked.removeAll(action.effect.positives);
	}
	
	@Override
	public MiniStateList apply(PddlAction action) {
		return new MiniStateList(this, action);
	}

	@Override
	public boolean isApplicable(PddlAction action) {
		return !isBlockedAction(action) && shouldApply(action);
	}

	@Override
	public List<PddlAction> getApplicableActions(PddlProblem problem) {
		List<PddlAction> acts = new LinkedList<PddlAction>();
		
		// the first "for" is just to prefer actions for which we have all the preconditions
		/*
		for (PddlAction action : problem.domain.actions) {
			if (isBlockedAction(action)) { 
				//System.out.println("BLOCKED: "+action.name);
			 	continue; 
			}
			//if (!shouldApply(action)) { System.out.println("NOT DESIRED: "+action.name);  }
			
			if (canDoRightNow(action) && shouldApply(action)) {
				acts.add(action);
			}
		}
		*/

		for (PddlAction action : problem.domain.actions) {
			//if (acts.contains(action)) continue;
			if (isApplicable(action)) {
				acts.add(action);
			}
		}
		
//		if (acts.size() == 0) {
//			System.out.println("DEAD STATE: "+this);
//		}
		
		return acts;
	}
	
//	private boolean canDoRightNow(PddlAction action) {
//		return have.containsAll(action.precondition.positives);
//	}
	
	private boolean shouldApply(PddlAction action) {
		//return intersects(wanted, action.effect.positives) || intersects(blocked, action.effect.positives);
		return intersects(wanted, action.effect.positives);
	}
	
	private boolean isBlockedAction(PddlAction action) {
		return intersects(action.effect.negatives, blocked) || intersects(action.effect.negatives, wanted);
	}

	@Override
	public boolean isSolved() {
		return wanted.isEmpty();
	}
	
	private static <T> boolean intersects(Collection<T> as, Collection<T> bs) {
		for (T a : as) {
			if (bs.contains(a)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public MiniStateList clone() {
		return new MiniStateList(this);
	}

	@Override
	public String toString() {
		return String.format("HAVE: %s; BLOCKED: %s; WANTED: %s\n", have, blocked, wanted);
	}

	@Override
	public boolean subsumedBy(MiniState other) {
		return this.equals(other);
	}

}
