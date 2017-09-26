package cz.agents.dimap.psmsas.operations.reductions;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.GroundSimpleActionReverse;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.Operator.Effect;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable.Value;

/**
 * Ground AnyValue precondition if its value is implied by some other precondition 
 */
public class GroundAnyValue extends ReduceOperationImpl {

    @Override
    public ReverseOperation reduce(SasFile sasFile) {

        MultiReverseOperation revOp = new MultiReverseOperation("GroundAnyValue");

        for (Operator operator : sasFile.getOperators().values()) {
            if (operator.isActive()) {
                
                for (Operator.Effect effect : operator.getEffects()) {
                    if (effect.isActive() && effect.getOldValue().getUpMostParent().isAnyValue()) {
                        ReverseOperation reverse = tryGroundAnyValue(sasFile, operator, effect);
                        if (reverse != null) {
                            revOp.addReverseOperation(reverse);
                        }
                    }
                }
            }
        }

        return revOp.isEmpty() ? null : revOp;
    }

    private ReverseOperation tryGroundAnyValue(SasFile sasFile, Operator operator, Effect effect) {
        Value effectOldValue = effect.getOldValue().getUpMostParent();
        Value possibleValue = null;
        for (Value possibleEffectOldValue : effectOldValue.getVariable().getValues()) {
            if (isPossibleValue(sasFile, operator, effect, possibleEffectOldValue)) {
                if (possibleValue == null) {
                    possibleValue = possibleEffectOldValue;
                } else {
                    return null;
                }
            }
        }
        if (possibleValue != null) {
            return ground(effect, possibleValue);
        } else {
            return null;
        }
    }

    private boolean isPossibleValue(SasFile sasFile, Operator operator, Effect effect, Value possibleEffectOldValue) {
        for (Operator.Effect otherEffect : operator.getEffects()) {
            if (otherEffect != effect && otherEffect.isActive() && !otherEffect.getOldValue().getUpMostParent().isAnyValue()) {
                if (sasFile.areMutexed(possibleEffectOldValue.getUpMostParent(), otherEffect.getOldValue().getUpMostParent())) {
                    return false;
                }
            }
        }
        return true;
    }

    private ReverseOperation ground(Effect effect, Value oldValue) {
        ReverseOperation reverse = new GroundSimpleActionReverse(effect, effect.getOldValue().getUpMostParent(), "AnyValue");
        effect.setOldValue(oldValue);
        return reverse;
    }

    @Override
    public void clearCache() {
    }
}
