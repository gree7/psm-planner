package cz.agents.dimap.psmsas.operations.reductions;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.DeadEndsReverse;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

/**
 * Created by pah on 27.7.15.
 */
public class DeadEnds extends ReduceOperationImpl {
    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        if (Settings.DEBUG) {
            System.out.println("dead ends reduction");
        }

        Map<Variable.Value, Set<Operator>> map = createMap(sasFile.getOperators().values());
        ReverseOperation reverse = runPruning(sasFile, map, sasFile.getOperators().values());
        return reverse;
    }


    private ReverseOperation runPruning(SasFile sasFile, Map<Variable.Value, Set<Operator>> map, Collection<Operator> operators) {

        MultiReverseOperation multiReverseOperation = new MultiReverseOperation("DeadEnds");
        
        DeadEndsReverse reverse = new DeadEndsReverse();
        boolean changed = true;
        while (changed) {
            int size = reverse.getChanges().size();
            for (Map.Entry<Variable.Value, Set<Operator>> valueEntry : map.entrySet()) {
                Variable.Value value = valueEntry.getKey().getUpMostParent();
                boolean inGoal = value.getVariable().isInGoal() && value.getVariable().getGoalValue().getUpMostParent() == value;
                boolean inInit = value.getVariable().getInitValue().getUpMostParent() == value;

                // TODO tuhle silenost predelat do lepsi podoby a hlavne rychlejsi, az potom co se zjisti jestli to opravdu funguje jak ma
                // navic se tahle cast kodu vicekrat opakuje; a v reverse operaci potom potreba pridelat co se kdy vypina

                if (valueEntry.getValue().size() < 1 && !inGoal && !inInit && !hasSomeOperatorWithSideEffect(value, map.values())) {//  (!hasSomeOperatorWithSideEffect(value, map.values()) || !hasActiveAnyValue(value.getVariable(), operators))) {
                    //if (valueEntry.getValue().size() < 1 && !inGoal && !inInit && (!hasSomeOperatorWithSideEffect(value, map.values()) || !hasActiveAnyValue(value.getVariable(), operators))) {
                    // System.out.println("unact " + value.getVariable().getOriginalId() + ":" + value.getOriginalID());
                    reverse.addToSetOn(value);
                    value.setActive(false);
                    ReverseOperation reverseMutex = sasFile.removeValueFromMutexes(value);
                    if (reverseMutex != null) {
                        multiReverseOperation.addReverseOperation(reverseMutex);
                    }
                }

            }
            map = update(map, operators, reverse);
            changed = reverse.getChanges().size() > size;

        }
        
        if (reverse.getChanges().size() > 0) {
            if (multiReverseOperation.isEmpty()) {
                return reverse;
            } else {
                multiReverseOperation.addReverseOperation(reverse);
                return multiReverseOperation;
            }
        } else {
            if (multiReverseOperation.isEmpty()) {
                return null;
            } else {
                return multiReverseOperation;
            }
        }        
    }

    public static boolean hasActiveAnyValue(Variable variable, Collection<Operator> operators) {
        for (Operator operator : operators) {
            if (operator.isActive() && operator.usesAnyValue(variable)) {
                return true;
            }
        }
        return false;
    }

    private Map<Variable.Value, Set<Operator>> update(Map<Variable.Value, Set<Operator>> map, Collection<Operator> operators, DeadEndsReverse reverse) {

        actualizeOperators(operators, map, reverse);
        List<Variable.Value> delete = new LinkedList<>();
        for (Map.Entry<Variable.Value, Set<Operator>> entry : map.entrySet()) {
            Variable.Value value = entry.getKey();
            boolean inGoal = value.getVariable().isInGoal() && value.getVariable().getGoalValue().getUpMostParent() == value;
            boolean inInit = value.getVariable().getInitValue().getUpMostParent() == value;

            if (entry.getValue().size() < 1 && !inGoal && !inInit && !hasSomeOperatorWithSideEffect(value, map.values())) { //  (!hasSomeOperatorWithSideEffect(value, map.values()) || !hasActiveAnyValue(value.getVariable(), operators))) {
                //if (entry.getValue().size() < 1 && !inGoal && !inInit && (!hasSomeOperatorWithSideEffect(value, map.values()) || !hasActiveAnyValue(value.getVariable(), operators))) {
                //System.out.println("deleting " + value.getVariable().getOriginalId() + ":" + value.getOriginalID());
                delete.add(entry.getKey());

            } else {
                actualizeEntry(entry, reverse);
            }
        }
        for (Variable.Value value : delete) {
            map.remove(value);
        }
        checkOperators(operators, reverse);
        return map;
    }

    private void checkOperators(Collection<Operator> operators, DeadEndsReverse reverse) {
        for (Operator operator : operators) {
            if (operator.isActive() && operator.actualize()) {
                operator.setActive(false);
                reverse.addToSetOn(operator);
            }
        }
    }


    private void actualizeOperators(Collection<Operator> operators, Map<Variable.Value, Set<Operator>> map, DeadEndsReverse reverse) {
        for (Operator operator : operators) {
            if (operator.isActive()) {
                operator.update(map, operators, reverse);
            }
        }
    }

    private boolean hasSomeOperatorWithSideEffect(Variable.Value value, Collection<Set<Operator>> operatorSet) {
        for (Set<Operator> operators : operatorSet) {
            for (Operator operator : operators) {
                if (operator.isActive()) {
                    if (operator.containsActiveEffectProducing(value) && operator.getNumberOfActiveEffects() > 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void actualizeEntry(Map.Entry<Variable.Value, Set<Operator>> entry, DeadEndsReverse reverse) {
        List<Operator> delete = new LinkedList<>();
        for (Operator operator : entry.getValue()) {
            if (operator.isActive() && operator.actualize()) {
                delete.add(operator);
                operator.setActive(false);
                reverse.addToSetOn(operator);
            }
        }
        entry.getValue().removeAll(delete);
    }

    private Map<Variable.Value, Set<Operator>> createMap(Collection<Operator> operators) {
        HashMap<Variable.Value, Set<Operator>> map = new HashMap<>();
        for (Operator operator : operators) {
            if (operator.isActive()) {
                processPreconditions(map, operator);
                processEffects(map, operator);
            }
        }
        return map;
    }

    private void processEffects(HashMap<Variable.Value, Set<Operator>> map, Operator operator) {
        for (Operator.Effect effect : operator.getEffects()) {
            if (effect.isActive()) {
                if (!effect.getOldValue().getUpMostParent().isAnyValue()) {
                    put(map, effect.getOldValue().getUpMostParent(), operator);
                }
                Variable.Value value = effect.getNewValue().getUpMostParent();
                if (!map.containsKey(value)) {
                    map.put(value, new HashSet<Operator>());
                }
            }
        }
    }

    private void processPreconditions(HashMap<Variable.Value, Set<Operator>> map, Operator operator) {
        for (Operator.Precondition precondition : operator.getPreconditions()) {
            if (precondition.isActive()) {
                put(map, precondition.getValue().getUpMostParent(), operator);
            }
        }
    }

    private void put(HashMap<Variable.Value, Set<Operator>> map, Variable.Value value, Operator operator) {
        if (!map.containsKey(value)) {
            map.put(value, new HashSet<Operator>());
        }
        map.get(value).add(operator);
    }

    @Override
    public void clearCache() {

    }
}
