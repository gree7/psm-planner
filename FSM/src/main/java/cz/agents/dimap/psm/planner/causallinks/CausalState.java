package cz.agents.dimap.psm.planner.causallinks;

public class CausalState {
    public final int id;
    public CausalState(int id) {
        this.id = id;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CausalState other = (CausalState) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CausalState [id=" + id + "]";
    } 
}