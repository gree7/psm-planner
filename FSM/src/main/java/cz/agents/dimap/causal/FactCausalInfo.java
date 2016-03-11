package cz.agents.dimap.causal;

import java.util.LinkedHashSet;
import java.util.Set;

import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlTerm;

public class FactCausalInfo {
	
	public final PddlTerm fact;
	
	public boolean hasInternalDependancy;
	
	private Set<PddlAction> dependants; 
	private Set<PddlAction> supporters;
	private Set<PddlAction> disposers;
	
	public FactCausalInfo(PddlTerm fact) {
		this.fact = fact;
		hasInternalDependancy = false;
		dependants = new LinkedHashSet<PddlAction>();
		supporters = new LinkedHashSet<PddlAction>();
		disposers = new LinkedHashSet<PddlAction>();
	}
	
	public void addDependant(PddlAction action) {
		if (action.isInternal()) {
			hasInternalDependancy = true;
		}
		dependants.add(action);
	}

	public void addSupporter(PddlAction action) {
		if (action.isInternal()) {
			hasInternalDependancy = true;
		}
		supporters.add(action);
	}
	
	public void addDisposer(PddlAction action) {
		if (action.isInternal()) {
			hasInternalDependancy = true;
		}
		disposers.add(action);
	}
	
	public Set<PddlAction> getDependants() {
		return dependants;
	}

	public Set<PddlAction> getSupporters() {
		return supporters;
	}

	public Set<PddlAction> getDisposers() {
		return disposers;
	}
	
}
