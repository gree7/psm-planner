package cz.agents.dimap.psmsas.operations.reductions;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.HalfCycleReverse;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.Operator.Effect;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;
import cz.agents.dimap.tools.sas.Variable.Value;

public class HalfCycleNew extends ReduceOperationImpl {
    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        if (Settings.DEBUG){
            System.out.println("generalized half cycle reduction");
        }

        // assume that -mv was called before this
        Map<Variable.Value, Set<Operator>> consumers = new HashMap<>();
        Map<Variable.Value, Set<Operator>> producers = new HashMap<>();

        // precondition is both 'producer' and 'consumer' for hasExtraProducer check            
        Map<Variable.Value, Set<Operator>> preconditionProducers = new HashMap<>();
        fillIn(sasFile.getOperators().values(), consumers, producers, preconditionProducers);

        for (Operator operator : sasFile.getOperators().values()) {
            if (!operator.isActive()) {
                continue;
            }
            
            if (operator.getNumberOfActivePreconditions() > 0
                    || operator.getNumberOfActiveEffects() != 1) {
                continue;
            }
            Effect effect = operator.getFirstActiveEffect();
            Variable.Value oldValue = effect.getOldValue().getUpMostParent();
            if (oldValue.isAnyValue()) {
                continue;
            }

            Variable.Value newValue = effect.getNewValue().getUpMostParent();

            boolean hasExtraConsumer = hasExtraConsumer(consumers, oldValue);
            boolean hasExtraProducer = hasExtraProducer(producers, newValue);
            hasExtraProducer |= preconditionProducers.containsKey(newValue);
            if (hasExtraConsumer && hasExtraProducer) {
                continue;
            }
            
            HalfCycleReverse reverseOp = processValueDependencyReduction(operator, effect);

            fixActionFromTo(oldValue, newValue, producers, consumers, reverseOp);
            
            ReverseOperation reverseMutexes = sasFile.actualizeMutexes();

            MultiReverseOperation multiReverse = new MultiReverseOperation("HalfCycle-"+operator.getName());
            multiReverse.addReverseOperation(reverseOp);
            multiReverse.addReverseOperation(reverseMutexes);
            multiReverse.addReverseOperation(new VariableDeletion().reduce(sasFile));
            
            return multiReverse;
        }
        return null;
    }

    private boolean hasExtraConsumer(Map<Value, Set<Operator>> consumers, Value value) {
        Set<Operator> valueConsumers = consumers.get(value);
        if (valueConsumers.size() != 1) {
            return true;
        }

        boolean inGoal = value.getVariable().isInGoal() && value == value.getVariable().getGoalValue().getUpMostParent();

        return inGoal;
    }

    private boolean hasExtraProducer(Map<Value, Set<Operator>> producers, Value value) {
        Set<Operator> valueProducers = producers.get(value);
        if (valueProducers.size() != 1) {
            return true;
        }

        boolean inInit = value.getVariable().getInitValue().getUpMostParent() == value;

        return inInit;
    }

    private HalfCycleReverse processValueDependencyReduction(Operator operator, Operator.Effect effect) {
        HalfCycleReverse reverse = new HalfCycleReverse(operator, effect);
        Variable.Value oldValue = effect.getOldValue().getUpMostParent();
        Variable.Value newValue = effect.getNewValue().getUpMostParent();
        newValue.setParent(oldValue);
        reverse.addToTurnOn(newValue);
        operator.setActive(false);
        reverse.addToTurnOn(operator);
        return reverse;
    }

    private void fixActionFromTo(Value fromValue, Value toValue,
            Map<Value, Set<Operator>> producers,
            Map<Value, Set<Operator>> consumers, HalfCycleReverse reverseOp) {
        if (!producers.containsKey(fromValue) || !consumers.containsKey(toValue)) {
            return;
        }

        Set<Operator> operators = new HashSet<Operator>(producers.get(fromValue));
        operators.retainAll(consumers.get(toValue));
        for (Operator operator : operators) {
            Effect effect = operator.getActiveEffectWithVariable(fromValue.getVariable());
            effect.setActive(false);
            reverseOp.addToTurnOn(effect);

            Operator.Precondition precondition = operator.addPrecondition(effect.getVariable(), effect.getOldValue());
            reverseOp.addToTurnOff(precondition);
        }
    }

    private void fillIn(Collection<Operator> operators, Map<Variable.Value, Set<Operator>> consumers, Map<Variable.Value, Set<Operator>> producers, Map<Value, Set<Operator>> preconditionProducers) {
        for (Operator operator : operators) {
            if (operator.isActive()) {
                fillInOperator(operator, consumers, producers, preconditionProducers);
            }
        }
    }

    private void fillInOperator(Operator operator, Map<Variable.Value, Set<Operator>> consumers, Map<Variable.Value, Set<Operator>> producers, Map<Value, Set<Operator>> preconditionProducers) {
        for (Operator.Precondition precondition : operator.getPreconditions()) {
            if (precondition.isActive()) {
                addToMap(precondition.getValue(), operator, consumers);
                addToMap(precondition.getValue(), operator, preconditionProducers);
            }
        }
        for (Operator.Effect effect : operator.getEffects()) {
            if (effect.isActive()) {
                addToMap(effect.getNewValue(), operator, producers);
                if (!effect.getOldValue().getUpMostParent().isAnyValue()) {
                    addToMap(effect.getOldValue(), operator, consumers);
                }
            }
        }
    }

    private void addToMap(Variable.Value value, Operator operator, Map<Variable.Value, Set<Operator>> map) {
        Variable.Value upMost = value.getUpMostParent();
        if (!map.containsKey(upMost)) {
            map.put(upMost, new HashSet<Operator>());
        }
        map.get(upMost).add(operator);
    }


    @Override
    public void clearCache() {
    }
}
