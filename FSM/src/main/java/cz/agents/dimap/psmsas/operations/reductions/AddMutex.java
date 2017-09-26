package cz.agents.dimap.psmsas.operations.reductions;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.UniversalReverse;
import cz.agents.dimap.tools.sas.MutexGroup;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;
import cz.agents.dimap.tools.sas.Variable.Value;

public class AddMutex extends ReduceOperationImpl {

    boolean alreadyRun = false;
    
    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        if (alreadyRun) {
            return null;
        }
        Map<Variable.Value, Set<Operator>> consumers = new HashMap<>();
        Map<Variable.Value, Set<Operator>> producers = new HashMap<>();
        createMaps(consumers, producers, sasFile.getOperators().values());
        ReverseOperation reverse = processConsumersProducers(consumers, producers, sasFile);
        if (reverse != null) {
            alreadyRun = true;
        }
        return reverse;
    }

    private ReverseOperation processConsumersProducers(Map<Value, Set<Operator>> consumers, Map<Value, Set<Operator>> producers, SasFile sasFile) {
        UniversalReverse reverse = new UniversalReverse("AddMutexes");
        for (Map.Entry<Value, Set<Operator>> consumerEntry : consumers.entrySet()) {
            Value consumerValue = consumerEntry.getKey();
            for (Map.Entry<Value, Set<Operator>> producerEntry : producers.entrySet()) {
                Value producerValue = producerEntry.getKey();

                if (consumerValue.getVariable().getOriginalId() <= producerValue.getVariable().getOriginalId()) {
                    continue;
                }

                if (consumerValue.isInitValue() && producerValue.isInitValue()) {
                    continue;
                }

                if (areProducersSubsetOfConsumers(consumers, producers, consumerValue, producerValue)) {

                    if (sasFile.areMutexed(consumerValue, producerValue)) {
                        continue;
                    }

                    MutexGroup mutex = sasFile.addMutex(consumerValue, producerValue);
                    reverse.deactivate(mutex);
                }
            }
        }
        if (reverse.getNumberOfReductions() == 0) {
            return null;
        } else {
            return reverse;
        }
    }

    private boolean areProducersSubsetOfConsumers(
            Map<Value, Set<Operator>> consumers,
            Map<Value, Set<Operator>> producers,
            Value value1,
            Value value2) {
        return isSubSet(producers.get(value1), consumers.get(value2)) 
                && isSubSet(producers.get(value2), consumers.get(value1));
    }

    private boolean isSubSet(Set<Operator> subSet, Set<Operator> superSet) {
        if (subSet == null) {
            return true;
        }
        if (superSet == null) {
            return false;
        }
        for (Operator operator : subSet) {
            if (!superSet.contains(operator)) {
                return false;
            }
        }
        return true;
    }

    private void createMaps(Map<Value, Set<Operator>> consumers, Map<Value, Set<Operator>> producers,
            Collection<Operator> operators) {
        for (Operator operator : operators) {
            if (operator.isActive()) {
                for (Operator.Effect effect : operator.getEffects()) {
                    if (effect.isActive()) {
                        Value oldValue = effect.getOldValue().getUpMostParent();
                        if (!oldValue.isAnyValue()) {
                            put(consumers, oldValue, operator);
                        } else {
                            for (Value oldV : oldValue.getVariable().getValues()) {
                                Value oldVal = oldV.getUpMostParent();
                                if (oldVal.isActive() && !oldVal.isAnyValue() && !oldVal.equals(effect.getNewValue().getUpMostParent())) {
                                    put(consumers, oldVal, operator);
                                }
                            }
                        }
                        Variable.Value newValue = effect.getNewValue().getUpMostParent();
                        put(producers, newValue, operator);
                    }
                }
            }
        }
    }

    private void put(Map<Variable.Value, Set<Operator>> map, Variable.Value value, Operator operator) {
        if (!map.containsKey(value)) {
            map.put(value, new HashSet<Operator>());
        }
        map.get(value).add(operator);
    }

    @Override
    public void clearCache() {
        alreadyRun = false;
    }
}
