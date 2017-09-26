package cz.agents.dimap.psmsas.operations.reductions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.DeadEndsReverse;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.Operator.Effect;
import cz.agents.dimap.tools.sas.Operator.Precondition;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;
import cz.agents.dimap.tools.sas.Variable.Value;

/**
 * Removes actions changing goal variables to values from which they cannot be changed to goal values
 */
public class RemoveDeadEndOperators extends ReduceOperationImpl {
    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        if (Settings.DEBUG) {
            System.out.println("remove dead end operators reduction");
        }

        Map<Variable.Value, Set<Operator>> consumers = new HashMap<>();
        Map<Variable.Value, Set<Operator>> producers = new HashMap<>();
        
        createMaps(consumers, producers, sasFile.getGoalVariables(), sasFile.getOperators().values());
        
        ReverseOperation reverse = runPruning(sasFile, consumers, producers, sasFile.getGoalVariables());
        return reverse;
    }

    private void addImpossibleOperators(DeadEndsReverse reverse, SasFile sas) {
        nextOp:
        for (Operator operator : sas.getOperators().values()) {
            if (operator.isActive()) {
                for (Precondition pre1 : operator.getPreconditions()) {
                    if (pre1.isActive()) {
                        for (Precondition pre2 : operator.getPreconditions()) {
                            if (pre2.isActive() && pre1 != pre2) {
                                if (sas.areMutexed(pre1.getValue().getUpMostParent(), pre2.getValue().getUpMostParent())) {
                                    operator.setActive(false);
                                    reverse.addToSetOn(operator);
                                    continue nextOp;
                                }
                            }
                        }
                        for (Effect eff : operator.getEffects()) {
                            if (eff.isActive() && sas.areMutexed(pre1.getValue().getUpMostParent(), eff.getOldValue().getUpMostParent())) {
                                operator.setActive(false);
                                reverse.addToSetOn(operator);
                                continue nextOp;
                            }
                        }
                    }
                }
                
                for (Effect eff1 : operator.getEffects()) {
                    if (eff1.isActive()) {
                        for (Effect eff2 : operator.getEffects()) {
                            if (eff2.isActive() && eff1 != eff2) {
                                if (sas.areMutexed(eff1.getOldValue().getUpMostParent(), eff2.getOldValue().getUpMostParent())) {
                                    operator.setActive(false);
                                    reverse.addToSetOn(operator);
                                    continue nextOp;
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private ReverseOperation runPruning(SasFile sasFile, Map<Value, Set<Operator>> consumers, Map<Value, Set<Operator>> producers,
            Collection<Variable> goalVariables) {

        Collection<Value> deadEndValues = new ArrayList<>();
        
        for (Variable variable : goalVariables) {
            if (variable.isActive()) {
                for (Value value : variable.getValues()) {
                    if (value.isActive() && !value.isInGoal()) {
                        if (!consumers.containsKey(value.getUpMostParent())) {
                            deadEndValues.add(value.getUpMostParent());
                        }
                    }
                }
            }
        }

        MultiReverseOperation multiReverse = new MultiReverseOperation("DeadEnds");
        DeadEndsReverse reverse = new DeadEndsReverse();
        for (Value value : deadEndValues) {
            value.setActive(false);
            ReverseOperation reverseMutex = sasFile.removeValueFromMutexes(value);
            multiReverse.addReverseOperation(reverseMutex);

            reverse.addToSetOn(value);
            
            for (Operator operator : producers.get(value)) {
                operator.setActive(false);
                reverse.addToSetOn(operator);
            }
        }

        addImpossibleOperators(reverse, sasFile);
        if (reverse.getChanges().size() < 1) {
            if (multiReverse.isEmpty()) {
                return null;
            } else {
                return multiReverse;
            }
        } else {
            if (multiReverse.isEmpty()) {
                return reverse;
            } else {
                multiReverse.addReverseOperation(reverse);
                return multiReverse;
            }
        }
    }

    private void createMaps(Map<Value, Set<Operator>> consumers, Map<Value, Set<Operator>> producers,
            Collection<Variable> goalVariables, Collection<Operator> operators) {
        for (Operator operator : operators) {
            if (operator.isActive()) {
                for (Operator.Effect effect : operator.getEffects()) {
                    if (effect.isActive()) {
                        if (goalVariables.contains(effect.getVariable())) {
                            Value oldValue = effect.getOldValue().getUpMostParent();
                            if (!oldValue.isAnyValue()) {
                                put(consumers, oldValue, operator);
                            } else {
                                for (Value oldVal : oldValue.getVariable().getValues()) {
                                    if (oldVal.isActive() && !oldVal.isAnyValue()) {
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
    }

    private void put(Map<Variable.Value, Set<Operator>> map, Variable.Value value, Operator operator) {
        if (!map.containsKey(value)) {
            map.put(value, new HashSet<Operator>());
        }
        map.get(value).add(operator);
    }

    public static boolean hasActiveAnyValue(Variable variable, Collection<Operator> operators) {
        for (Operator operator : operators) {
            if (operator.isActive() && operator.usesAnyValue(variable)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clearCache() {
    }
}
