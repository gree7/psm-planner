package cz.agents.dimap.psmsas.operations.reductions;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.ActionStartMergeReverse;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.Operator.Precondition;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

/**
 * Created by pah on 14.8.15.
 */
public class ActionStartMerge extends ReduceOperationImpl {
    private Iterator<Operator> iterator;

    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        if (Settings.DEBUG) {
            System.out.println("action start merge reduction");
        }
        setIterator(sasFile);
        while (iterator.hasNext()) {
            Operator operator = iterator.next();
            if (operator.isActive() && operator.getNumberOfActivePreconditions() == 0) {
                ActionStartMergeReverse reverse = process(operator, sasFile);
                if (null != reverse) {
                    return reverse;
                }
            }
        }
        return null;
    }

    private ActionStartMergeReverse process(Operator operator, SasFile sasFile) {
        Set<Operator.Effect> mergeableWithStart = new HashSet<>();
        for (Precondition precondition : operator.getPreconditions()) {
            if (precondition.isActive() && precondition.getVariable().getInitValue().equals(precondition.getValue())) {
                if (sasFile.getVariableOperators().get(precondition.getVariable()).size() != 1) {
                    return null;
                }
            }
        }
        for (Operator.Effect effect : operator.getEffects()) {
            if (effect.isActive()) {
                if (!effect.getVariable().getInitValue().equals(effect.getOldValue()) || !processEffect(effect, sasFile, mergeableWithStart, operator)) {
                    return null;
                }
            }
        }
        
        //System.out.println("mergable With");
        //if (mergeableWithStart.size() > 0) { // mozna jeste nejaky podminky
        // tohle odstraneno ve specifikaci, je to trochu vic obecnejsi
        if (Settings.ALLOW_TRANSFORMATIONS || ActionStartMergeReverse.isOnlyOnce(operator, sasFile)) {
            return new ActionStartMergeReverse(operator, sasFile, "ActionStartMerge");
        } else {
            return null;
        }
    }

    // returns false if the operator cannot be merged with start because of the effect
    private boolean processEffect(Operator.Effect effect, SasFile sasFile, Set<Operator.Effect> mergeableWithStart, Operator operator) {
// why that? - brokes nomystery00
//        if (isUnused(effect.getNewValue().getCachedUpMostParent(), sasFile)) {
//            mergeableWithStart.add(effect);
//            return true;
//        }

        if (existsOnlyOppositeEffects(effect, sasFile, operator)) {
            return true;
        }
        return false;
    }

    private boolean isUnused(Variable.Value value, SasFile sasFile) {
        if (!sasFile.getVariableOperators().containsKey(value.getVariable())) {
            return true;
        }
        for (Operator operator : sasFile.getVariableOperators().get(value.getVariable())) {
            if (operator.isActive() && operator.isConsumingValue(value.getCachedUpMostParent())) {
                return false;
            }
        }
        return true;
    }

    private boolean existsOnlyOppositeEffects(Operator.Effect effect, SasFile sasFile, Operator processedOperator) {
        if (!sasFile.getVariableOperators().containsKey(effect.getVariable())) {
            return false;
        }
        if (sasFile.getVariableOperators().get(effect.getVariable()).size() < 1) {
            return false;
        }
        for (Operator operator : sasFile.getVariableOperators().get(effect.getVariable())) {
            if(!operator.isActive()){
                continue;
            }
            if(processedOperator == operator){
                continue;
            }
            Operator.Effect currentEffect = operator.getActiveEffectWithVariable(effect.getVariable());
            if (!areOpposite(effect, currentEffect)) {
                return false;
            }
        }
        return true;
    }

    private boolean areOpposite(Operator.Effect first, Operator.Effect second) {
        if (null == first || null == second) {
            return false;
        }
        if (first.getOldValue().getCachedUpMostParent() == second.getNewValue().getCachedUpMostParent() &&
                first.getNewValue().getCachedUpMostParent() == second.getOldValue().getCachedUpMostParent()) {
            return true;
        }

        return false;
    }

    @Override
    public void clearCache() {
        iterator = null;
    }

    public void setIterator(SasFile sasFile) {
        if (null == iterator) {
            this.iterator = sasFile.getOperators().values().iterator();
        }
    }
}
