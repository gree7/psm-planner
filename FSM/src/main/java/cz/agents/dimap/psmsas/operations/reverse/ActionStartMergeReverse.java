package cz.agents.dimap.psmsas.operations.reverse;

import java.util.LinkedList;
import java.util.List;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.sas.Active;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.Operator.Effect;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;
import cz.agents.dimap.tools.sas.Variable.Value;

public class ActionStartMergeReverse implements ReverseOperation {
    private final List<Active> changes = new LinkedList<>();
    private final List<Variable.Value> initChanges = new LinkedList<>();
    private final List<Active> unactivize = new LinkedList<>();
    private final Operator operator;
    private final String name;

    public ActionStartMergeReverse(Operator operator, SasFile sasFile, String name) {
        this.name = name;
        this.operator = operator;
        if (isOnlyOnce(operator, sasFile)) {
            operator.setActive(false);
        }
        changes.add(operator);
        for (Operator.Effect effect : operator.getEffects()) {
            if (effect.isActive()) {
                processEffect(effect);
            }
        }
    }

    public static boolean isOnlyOnce(Operator operator, SasFile sasFile) {
        for (Effect effect : operator.getEffects()) {
            if (effect.isActive() && !hasProducingOperator(effect.getOldValue(), sasFile)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasProducingOperator(Value value, SasFile sasFile) {
        if(!sasFile.getVariableOperators().containsKey(value.getVariable())){
            return false;
        }
        for (Operator operator : sasFile.getVariableOperators().get(value.getVariable())) {
            if (operator.isActive() && operator.isProducingValue(value)) {
                return true;
            }
        }
        return false;
    }

    private void processEffect(Operator.Effect effect) {
        Variable.Value originalValue = effect.getOldValue().getCachedUpMostParent();
        if (originalValue.isAnyValue()) {
            originalValue = originalValue.getVariable().getInitValue().getCachedUpMostParent();
        }
        Variable.Value newState = effect.getNewValue().getCachedUpMostParent();
        if (newState != originalValue) {
            originalValue.getVariable().setInitValue(newState);
            initChanges.add(originalValue);
        }
    }

    private void processOperators(Operator.Effect effect, SasFile sasFile) {
        if (!sasFile.getVariableOperators().containsKey(effect.getVariable())) {
            return;
        }
        for (Operator currentOperator : sasFile.getVariableOperators().get(effect.getVariable())) {
            if (!currentOperator.isActive()) {
                continue;
            }
            Operator.Effect currentEffect = currentOperator.getActiveEffectWithVariable(effect.getVariable());
            if (null == currentEffect) {
                continue;
            }
            currentEffect.actualize();
            if (!currentEffect.isActive()) {
                changes.add(currentEffect);
                Operator.Precondition precondition = currentOperator.addPrecondition(effect.getVariable(), effect.getNewValue().getCachedUpMostParent());
                unactivize.add(precondition);
            }
        }
    }

    @Override
    public void extend(Plan plan, SasFile sasFile) {
        activizeChanges();
        unactivizeChanges();
        undoInitChanges();
        plan.getPlan().add(0, this.operator);
    }

    private void undoInitChanges() {
        for (Variable.Value originalValue : initChanges) {
            originalValue.getVariable().setInitValue(originalValue);
        }
    }

    private void unactivizeChanges() {
        for (Active change : unactivize) {
            change.setActive(false);
        }
    }

    private void activizeChanges() {
        for (Active change : changes) {
            change.setActive();
        }
    }

    @Override
    public String toString() {
        return this.name+"-"+operator;
    }

    @Override
    public Integer getNumberOfReductions() {
        return 1;
    }

}
