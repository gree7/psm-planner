package cz.agents.dimap.causal;

import cz.agents.dimap.tools.pddl.PddlTerm;

public class FsmNodeTerm implements FsmNode {
    public PddlTerm term;
    public boolean isPublic;
    public FsmNodeTerm(PddlTerm term, boolean isPublic) {
        this.term = term;
        this.isPublic = isPublic;
    }
    
    @Override
    public FsmNodeTerm cloneWithSuffix(String suffix) {
        return new FsmNodeTerm(PddlTerm.renamePredicate(term, term.name.name + "'" + suffix), isPublic);
    }
    
    @Override
    public String toString() {
        return term.toString() + (isPublic ? "\" style=\"filled\" color=\"gray" : "");
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((term == null) ? 0 : term.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FsmNodeTerm other = (FsmNodeTerm) obj;
        if (term == null) {
            if (other.term != null)
                return false;
        } else if (!term.equals(other.term))
            return false;
        return true;
    }
}