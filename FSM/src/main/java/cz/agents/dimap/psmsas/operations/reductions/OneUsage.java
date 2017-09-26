package cz.agents.dimap.psmsas.operations.reductions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.OneUsageReverse;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

/**
 * Created by pah on 14.8.15.
 */
public class OneUsage extends ReduceOperationImpl {
    private final Map<Variable.Value, Variable.Value> cache = new HashMap<>();

    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        cache.clear();
        if (Settings.DEBUG) {
            System.out.println("one usage reduction");
        }

        MultiReverseOperation multiReverse = new MultiReverseOperation("OneUsageWrapper");
        for (Variable variable : sasFile.getVariables().values()) {
            MultiReverseOperation reverse = process(variable, sasFile);
            if (null != reverse) {
                multiReverse.addReverseOperation(reverse);
            }
        }
        if (multiReverse.isEmpty()) {
            return null;
        }
        return multiReverse;
    }

    private MultiReverseOperation process(Variable variable, SasFile sasFile) {
        Variable.Value value = variable.getInitValue().getCachedUpMostParent();
        Set<Operator> consumers = new HashSet<>();
        Set<Operator> producers = new HashSet<>();
        fillInConsumersAndProducers(value, sasFile, consumers, producers);
        if (producers.size() == 0 && consumers.size() == 0) {
            return null;
        }
        if (producers.size() != 0) {
            return null;
        }
        MultiReverseOperation multiReverse = new MultiReverseOperation("OneUsage: " + value.getVariable().getOriginalId() + ":" + value.getOriginalID());

        for (Operator consumer : consumers) {
            MultiReverseOperation reverse = processEffect(consumer, sasFile);
            if (null != reverse) {
                multiReverse.addReverseOperation(reverse);
            }
        }
        if (multiReverse.isEmpty()) {
            return null;
        }
        return multiReverse;
    }

    private MultiReverseOperation processEffect(Operator onceUsedOperator, SasFile sasFile) {
        MultiReverseOperation reverse = new MultiReverseOperation("OneUsage: " + onceUsedOperator);
        for (Operator.Effect effect : onceUsedOperator.getEffects()) {
            if (effect.isActive()) {
                if (effect.getNewValue().getCachedUpMostParent() == effect.getVariable().getInitValue().getCachedUpMostParent()) {
                    processEffectLeadingToInit(onceUsedOperator, sasFile, reverse, effect);
                } else {
                    processEffectLeadingFromInit(sasFile, reverse, effect);
                }
            }
        }
        if (reverse.isEmpty()) {
            return null;
        }
        return reverse;
    }

    private void processEffectLeadingFromInit(SasFile sasFile, MultiReverseOperation reverse, Operator.Effect effect) {
        Set<Operator> opposite = findOppositeOperator(effect, sasFile);
        Set<Operator> producers = findProducers(effect.getNewValue(), sasFile);
        if (opposite.size() > 0 && opposite.size() == producers.size()) {
            Set<Operator> requires = findRequirers(effect.getNewValue().getUpMostParent(), sasFile);
            if (requires.size() == 1) {
                OneUsageReverse addNewValue = processReduction(effect.getOldValue().getCachedUpMostParent(), opposite);
                reverse.addReverseOperation(addNewValue);
            }
        }
    }

    private void processEffectLeadingToInit(Operator onceUsedOperator, SasFile sasFile, MultiReverseOperation reverse, Operator.Effect effect) {
        Set<Operator> opposite = findOppositeOperator(effect, sasFile);
        Set<Operator> producers = findProducers(effect.getOldValue(), sasFile);
        Set<Operator> requirers = findRequirers(effect.getNewValue(), sasFile);
        if(opposite.size() > 0 && opposite.size() == producers.size() && opposite.size() == requirers.size()){
            Set<Operator> set = new HashSet<>();
            set.add(onceUsedOperator);
            OneUsageReverse addNewValue = processReduction(effect.getNewValue().getCachedUpMostParent(), set);
            reverse.addReverseOperation(addNewValue);
        }
    }

    private OneUsageReverse processReduction(Variable.Value value, Set<Operator> opposite) {
        Variable.Value newValue;
        if (cache.containsKey(value)) {
            newValue = cache.get(value);
        } else {
            newValue = value.getVariable().addNextValue(value.getValue());
        }
        OneUsageReverse reverse = new OneUsageReverse(newValue);
        for (Operator operator : opposite) {
            Operator.Effect currentEffect = operator.getActiveEffectWithVariable(value.getVariable());
            currentEffect.setNewValue(newValue);
            reverse.addChangedEffect(currentEffect);
        }
        return reverse;
    }

    private Set<Operator> findRequirers(Variable.Value value, SasFile sasFile) {
        Set<Operator> set = new HashSet<>();
        if (!sasFile.getVariableOperators().containsKey(value.getVariable())) {
            return set;
        }
        for (Operator operator : sasFile.getVariableOperators().get(value.getVariable())) {
            if (operator.isActive() && operator.isNeedingValue(value)) {
                set.add(operator);
            }
        }
        return set;
    }

    private Set<Operator> findOppositeOperator(Operator.Effect effect, SasFile sasFile) {
        Set<Operator> set = new HashSet<>();
        if (!sasFile.getVariableOperators().containsKey(effect.getVariable())) {
            return set;
        }
        for (Operator operator : sasFile.getVariableOperators().get(effect.getVariable())) {
            if (operator.isActive()) {
                Operator.Effect currentEffect = operator.getActiveEffectWithVariable(effect.getVariable());
                if (effect.isOpposite(currentEffect)) {
                    set.add(operator);
                }
            }
        }
        return set;
    }

    private void fillInConsumersAndProducers(Variable.Value value, SasFile sasFile, Set<Operator> consumers, Set<Operator> producers) {
        if (!sasFile.getVariableOperators().containsKey(value.getVariable())) {
            return;
        }
        for (Operator operator : sasFile.getVariableOperators().get(value.getVariable())) {
            if (operator.isActive()) {
                if (operator.isConsumingValue(value)) {
                    consumers.add(operator);
                }
                if (operator.isProducingValue(value)) {
                    producers.add(operator);
                }
            }
        }
    }

    private Set<Operator> findProducers(Variable.Value value, SasFile sasFile) {
        if (!sasFile.getVariableOperators().containsKey(value.getVariable())) {
            return new HashSet<>();
        }
        HashSet<Operator> set = new HashSet<>();
        for (Operator operator : sasFile.getVariableOperators().get(value.getVariable())) {
            if (operator.isActive() && operator.isProducingValue(value)) {
                set.add(operator);
            }
        }
        return set;
    }

    @Override
    public void clearCache() {

    }
}
