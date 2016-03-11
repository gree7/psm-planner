package cz.agents.dimap.causal;

import java.util.Collection;

import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;

public interface InternalDependencies {

    public abstract Collection<PddlTerm> updateProblem(PddlProblem problem);

}