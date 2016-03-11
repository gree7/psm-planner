package cz.agents.dimap.causal;

import cz.agents.dimap.fsm.EpsilonOperator;

public class EpsilonTransition implements EpsilonOperator {

    String name;
    public boolean isEpsilon;

    private EpsilonTransition(String name, boolean isEpsilon) {
        this.name = name;
        this.isEpsilon = isEpsilon;
    }
    
    public static EpsilonTransition createPreconditionTransition(boolean isEpsilon) {
        return new EpsilonTransition("AND", isEpsilon);
    }

    public static EpsilonTransition createEffectTransition() {
        return new EpsilonTransition("OR", false);
    }

    @Override
    public boolean isEpsilon() {
        return isEpsilon;
    }

    @Override
    public void setIsEpsilon(boolean isEpsilon) {
        this.isEpsilon = isEpsilon;
    }
    
    @Override
    public String toString() {
        if (name.equals("AND")) {
            return name + ( isEpsilon ? "+" : "-" );
        } else {
            return name;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isEpsilon ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        EpsilonTransition other = (EpsilonTransition) obj;
        if (isEpsilon != other.isEpsilon)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}