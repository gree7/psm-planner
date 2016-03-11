package cz.agents.dimap.psm.planner.causallinks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CausalLink {

    public String action;
    public List<Integer> conditionId;
    public Integer effectId;

    public CausalLink(String action, Collection<Integer> conditionId, Integer effectId) {
        this.action = action;
        if (conditionId.isEmpty()) {
            this.conditionId = Arrays.asList(-1);
        } else {
            this.conditionId = new ArrayList<>(conditionId);
            Collections.sort(this.conditionId, Collections.reverseOrder());
        }
        this.effectId = effectId;
    }

    @Override
    public String toString() {
        return action + " ( "+conditionId +" -> " +effectId+ " )";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((action == null) ? 0 : action.hashCode());
        result = prime * result
                + ((conditionId == null) ? 0 : conditionId.hashCode());
        result = prime * result
                + ((effectId == null) ? 0 : effectId.hashCode());
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
        CausalLink other = (CausalLink) obj;
        if (action == null) {
            if (other.action != null)
                return false;
        } else if (!action.equals(other.action))
            return false;
        if (conditionId == null) {
            if (other.conditionId != null)
                return false;
        } else if (!conditionId.equals(other.conditionId))
            return false;
        if (effectId == null) {
            if (other.effectId != null)
                return false;
        } else if (!effectId.equals(other.effectId))
            return false;
        return true;
    }
}