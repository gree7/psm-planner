package cz.agents.dimap.psmsas.operations.reductions;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.GroundSimpleActionReverse;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.Operator.Effect;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable.Value;

public class GroundSimpleAction extends ReduceOperationImpl {

    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        if (Settings.DEBUG) {
            System.out.println("generalized action reduction");
        }

        MultiReverseOperation multiReverse = new MultiReverseOperation("GeneralizedAction");
        for (Operator operator : sasFile.getOperators().values()) {
            if (operator.isActive()) {
                Effect effect = getOnlyAnyEffect(operator);
                if (effect == null) {
                    continue;
                }

                Value otherValue = getOtherValue(effect.getNewValue().getUpMostParent());
                if (otherValue == null) {
                    continue;
                }
                ReverseOperation reverse = new GroundSimpleActionReverse(effect, effect.getOldValue().getUpMostParent(), operator.getName());
                effect.setOldValue(otherValue.getUpMostParent());
                multiReverse.addReverseOperation(reverse);
            }
        }
        if (multiReverse.isEmpty()) {
            return null;
        }
        return multiReverse;
    }

    private Value getOtherValue(Value value) {
        if (value.getVariable().getNumberOfActiveValues() != 2) {
            return null;
        }
        for (Value otherValue : value.getVariable().getValues()) {
            if (value.isActive() && !otherValue.getUpMostParent().equals(value.getUpMostParent()) ) {
                return otherValue;
            }
        }
        return null;
    }

    private Effect getOnlyAnyEffect(Operator operator) {
        Effect anyEffect = null;
        for (Effect effect : operator.getEffects()) {
            if (effect.isActive()) {
                if (effect.getOldValue().getUpMostParent().isAnyValue() && anyEffect == null) {
                    anyEffect = effect;
                } else {
                    return null;
                }
            }
        }
        return anyEffect;
    }

    @Override
    public void clearCache() {
    }
}
