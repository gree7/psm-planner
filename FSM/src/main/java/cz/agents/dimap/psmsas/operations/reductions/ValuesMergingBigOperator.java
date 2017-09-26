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
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.ValuesMergingReverse;
import cz.agents.dimap.tools.Pair;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

/**
 * Created by pah on 10.7.15.
 * <p/>
 * Class merging two values of one variable if possible. Each instance of this class is statefull, so read reduce and clearCache method carefully.
 */
public class ValuesMergingBigOperator extends ReduceOperationImpl {
    private final Map<Variable.Value, Set<Pair<Variable.Value, Variable.Value>>> cache = new HashMap<>();

    public ValuesMergingBigOperator() {
    }

    /**
     * Reduces the SAS by merging two values of one variable. If it is possible, object representing reverse operation is returned; otherwise null is returned.
     * This method is supposed to be called on the same SAS repeatedly until possible. But before calling it on a different SAS, call clearCache to reset the instance.
     *
     * @param sasFile
     * @return
     */
    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        if (Settings.DEBUG) {
            System.out.println("reducing effects with caching");
        }
        cache.clear();
        MultiReverseOperation reverse = process(filterOperators(sasFile.getOperators().values()), sasFile);
        return (reverse.isEmpty()) ? null : reverse;
    }

    private Collection<Operator> filterOperators(Collection<Operator> operators) {
        List<Operator> filtered = new LinkedList<>();
        for (Operator operator : operators) {
            if (operator.isActive() && operator.getNumberOfActivePreconditions() < 1) {
                filtered.add(operator);
            }
        }
        return filtered;
    }

    private void addAndMerge(Operator operator, Map<Pair<Variable.Value, Variable.Value>, Set<Operator>> map, MultiReverseOperation reverse, SasFile sasFile) {
        Operator.Effect effect = operator.getFirstActiveEffect();

        if (effect.getOldValue().getUpMostParent().isAnyValue()) {
            for (Variable.Value value : effect.getVariable().getValues()) {
                if (!value.getUpMostParent().isAnyValue()) {
                    Pair<Variable.Value, Variable.Value> opposite = new Pair<>(effect.getNewValue().getUpMostParent(), value.getUpMostParent());
                    if (map.containsKey(opposite)) {
                        merge(operator, opposite, map, reverse, sasFile);
                        return;
                    }
                }
            }
        }

        Pair<Variable.Value, Variable.Value> opposite = new Pair<>(effect.getNewValue().getUpMostParent(), effect.getOldValue().getUpMostParent());
        if (map.containsKey(opposite)) {
            merge(operator, opposite, map, reverse, sasFile);
        } else {
            add(effect, operator, map);
        }
    }

    private MultiReverseOperation process(Collection<Operator> operators, SasFile sasFile) {
        MultiReverseOperation reverse = new MultiReverseOperation("BigValueMerge");
        Map<Pair<Variable.Value, Variable.Value>, Set<Operator>> map = new HashMap<>();
        for (Operator operator : operators) {
            if (operator.getNumberOfActiveEffects() == 1) {
                addAndMerge(operator, map, reverse, sasFile);
            }
        }
        return reverse;
    }

    private void add(Operator.Effect effect, Operator operator, Map<Pair<Variable.Value, Variable.Value>, Set<Operator>> map) {
        if (effect.getOldValue().getUpMostParent().isAnyValue()) {
            for (Variable.Value value : effect.getOldValue().getVariable().getValues()) {
                if (!value.getUpMostParent().isAnyValue()) {
                    addToMap(value.getUpMostParent(), effect, operator, map);
                }
            }
        } else {
            addToMap(effect, operator, map);
        }
    }

    private void addToMap(Variable.Value oldValue, Operator.Effect effect, Operator operator, Map<Pair<Variable.Value, Variable.Value>, Set<Operator>> map) {
        Pair<Variable.Value, Variable.Value> pair = new Pair<>(oldValue.getUpMostParent(), effect.getNewValue().getUpMostParent());
        if (!map.containsKey(pair)) {
            map.put(pair, new HashSet<Operator>());
        }
        map.get(pair).add(operator);
        addToCache(pair.getValue(), pair);
        addToCache(pair.getKey(), pair);
    }

    private void addToCache(Variable.Value key, Pair<Variable.Value, Variable.Value> pair) {
        if (!cache.containsKey(key)) {
            cache.put(key, new HashSet<Pair<Variable.Value, Variable.Value>>());
        }
        //Pair<Variable.Value, Variable.Value> opposite = new Pair<>(pair.getValue(), pair.getKey());
        //System.out.println("adding\t" + pair.getKey().getVariable().getOriginalId() + ":" + pair.getKey().getUpMostParent().getOriginalID() + " -> " + pair.getValue().getUpMostParent().getOriginalID() + "\t" + pair.hashCode() + " HC\t" + pair.getKey().hashCode() + "_" + pair.getValue().hashCode() + "\t opHC " + opposite.hashCode());
        cache.get(key).add(pair);
    }

    private void addToMap(Operator.Effect effect, Operator operator, Map<Pair<Variable.Value, Variable.Value>, Set<Operator>> map) {
        addToMap(effect.getOldValue().getUpMostParent(), effect, operator, map);
    }

    private void merge(Operator operator, Pair<Variable.Value, Variable.Value> opposite, Map<Pair<Variable.Value, Variable.Value>, Set<Operator>> map, MultiReverseOperation reverse, SasFile sasFile) {
        Operator first = findOperator(operator, map.get(opposite));
        Operator second = operator;
        if (null == first) {
            return;
        }
        ReverseOperation rev = mergeValuesUpdate(first, second, map, reverse, sasFile);
        if (null != rev) {
            reverse.addReverseOperation(rev);
        }
    }

    private Operator findOperator(Operator second, Set<Operator> operators) {
        Operator result = null;
        Operator.Effect effect = second.getFirstActiveEffect();
        for (Operator operator : operators) {
            Operator.Effect currentEffect = operator.getFirstActiveEffect();
            if ((currentEffect.getOldValue().getUpMostParent().isAnyValue() || effect.getNewValue().getUpMostParent() == currentEffect.getOldValue().getUpMostParent())) {// && currentEffect.getOldValue().isTerminal()) {
                result = operator;
            }
        }
        return result;
    }


    /**
     * Use this method before reduce on different SAS.
     */
    @Override
    public void clearCache() {

    }


    /**
     * Returns reverse operation only if both operator can be merged; otherwise returns null. If the two operators can be merged, they are merged to one by to makeMerge method.
     *
     * @param first
     * @param second
     * @param map
     * @param @return
     */
    private ReverseOperation mergeValuesUpdate(Operator first, Operator second, Map<Pair<Variable.Value, Variable.Value>, Set<Operator>> map, MultiReverseOperation bigReverse, SasFile sasFile) {
        Operator.Effect firstEffect = first.getFirstActiveEffect();
        Operator.Effect secondEffect = second.getFirstActiveEffect();

        if (!firstEffect.isOpposite(secondEffect)) {// tohle je asi redundatni uz, radsi check
            return null;
        }

        Variable.Value valueFirst = firstEffect.getNewValue().getUpMostParent();
        Variable.Value valueSecond = secondEffect.getNewValue().getUpMostParent();

        if (Settings.DEBUG) {
            System.out.println("merging effects:");
            System.out.println("\t" + first.getName() + "\t" + first.getCanonicalString());
            System.out.println("\t\t" + firstEffect.getCanonic() + "\t" + first.isActive());
            System.out.println("\t" + second.getName() + "\t" + second.getCanonicalString());
            System.out.println("\t\t" + secondEffect.getCanonic() + "\t" + second.isActive());
            Pair<Variable.Value, Variable.Value> pair = new Pair<>(valueFirst, valueSecond);
            System.out.println("\t\t" + pair.hashCode());
            pair = new Pair<>(valueSecond, valueFirst);
            System.out.println("\t\t" + pair.hashCode());
        }

        ValuesMergingReverse reverse = mergeEffects(first, firstEffect, second, secondEffect);

        firstEffect.actualize();
        secondEffect.actualize();

        if (valueFirst.isActive()) {
            update(valueSecond, valueFirst, map, bigReverse, sasFile);
        } else {
            update(valueFirst, valueSecond, map, bigReverse, sasFile);
        }

        return processOperationChanges(sasFile.getVariableOperators().get(firstEffect.getVariable()), reverse);
    }

    private void update(Variable.Value erased, Variable.Value parent, Map<Pair<Variable.Value, Variable.Value>, Set<Operator>> map, MultiReverseOperation bigReverse, SasFile sasFile) {
        for (Pair<Variable.Value, Variable.Value> pair : cache.get(erased)) {
            Pair<Variable.Value, Variable.Value> newPair;
            if (pair.getKey() == erased) {
                newPair = new Pair<>(parent.getUpMostParent(), pair.getValue().getUpMostParent());
            } else {
                newPair = new Pair<>(pair.getKey().getUpMostParent(), parent.getUpMostParent());
            }
            if (pair.getKey() == pair.getValue()) {
                continue;
            }

            addToCache(parent.getUpMostParent(), newPair);
            if (!map.containsKey(newPair)) {
                map.put(newPair, new HashSet<Operator>());
            }
            if(map.containsKey(pair) && null != map.get(pair)) {
                map.get(newPair).addAll(map.get(pair));
            }

            /*
            // tahle tovarna tu je kvuli rekurzi, ale kdyz se to predtim sesortuje, tak by to tu rekurzi potrebovat nemelo
            Pair<Variable.Value, Variable.Value> opposite = new Pair<>(newPair.getValue(), newPair.getKey());
            if (map.containsKey(opposite)) {
                Operator operator = map.get(newPair).iterator().next();
                merge(operator, opposite, map, bigReverse, sasFile);
            }*/

        }
    }

    /**
     * Takes care of changes that should be stored in reverse action.
     *
     * @param operators
     * @param reverse
     * @return
     */
    private ValuesMergingReverse processOperationChanges(Set<Operator> operators, ValuesMergingReverse reverse) {
        for (Operator operator : operators) {
            if (operator.isActive()) {
                updateEffects(operator, reverse.getVariable(), reverse);
                if (operator.actualize()) {
                    reverse.addToActivize(operator);
                }
            }
        }
        return reverse;
    }

    private boolean updateEffects(Operator operator, Variable variable, ValuesMergingReverse reverse) {
        boolean change = false;
        Set<Long> alreadyIn = new HashSet<>();
        for (Operator.Effect effect : operator.getEffects()) {
            if (effect.isActive() && effect.getVariable() == variable) {
                if (effect.getOldValue().getUpMostParent() == effect.getNewValue().getUpMostParent()) {
                    effect.setActive(false);
                    reverse.addToActivize(effect);
                    change = true;
                    if (Settings.DEBUG) {
                        System.out.println("effect set to unactive state because of same old and new value, value: '" + effect.getVariable().getRealId() + "', operator: '" + operator.getName() + "', effect: '" + effect.getSAS() + "', precondition added");
                    }

                    Operator.Precondition precondition = operator.addPrecondition(effect.getVariable(), effect.getOldValue().getUpMostParent());
                    reverse.addToTurnOff(precondition);

                } else if (!effect.getVariable().isActive()) {
                    effect.setActive(false);
                    reverse.addToActivize(effect);
                    change = true;
                    if (Settings.DEBUG) {
                            System.out.println("effect set to unactive state because of unactive variable: '" + effect.getVariable().getRealId() + "', operator: '" + operator.getName() + "', effect: '" + effect.getSAS() + "'");
                    }

                } else {
                    if (!alreadyIn.add(effect.getCanonic())) {
                        effect.setActive(false);
                        reverse.addToActivize(effect);
                        change = true;
                        System.err.println("WARNING: effect become duplicated");
                        if (Settings.DEBUG) {
                            System.out.println("effect set to unactive state because of duplicates; operator: '" + operator.getName() + "', effect: '" + effect.getSAS() + "'");
                        }

                    }
                }
            }
        }
        return change;
    }

    /**
     * Makes merge of two effect. Note that it does not checks whether the effects can be merged.
     * For merging two operators with checking of the merge possibility please use merge method.
     *
     * @param firstOperator
     * @param first
     * @param secondOperator
     * @param second
     * @return
     */
    private ValuesMergingReverse mergeEffects(Operator firstOperator, Operator.Effect first, Operator secondOperator, Operator.Effect second) {
        ValuesMergingReverse operation = new ValuesMergingReverse(first, second, firstOperator, secondOperator);

        if (first.getOldValue().getUpMostParent().isAnyValue() || second.getOldValue().getUpMostParent().isAnyValue()) {
            if (first.getOldValue().getUpMostParent().isAnyValue() && second.getOldValue().getUpMostParent().isAnyValue()) {
                if (first.getNewValue().getParentRealID() < second.getNewValue().getParentRealID()) {
                    mergeSortedEffects(first, second, operation);
                } else {
                    mergeSortedEffects(second, first, operation);
                }
            } else if (first.getOldValue().getUpMostParent().isAnyValue()) {
                mergeSortedEffects(first, second, operation);
            } else {
                mergeSortedEffects(second, first, operation);
            }

        } else {
            if (first.getNewValue().getParentRealID() < second.getNewValue().getParentRealID()) {
                mergeSortedEffects(first, second, operation);
            } else {
                mergeSortedEffects(second, first, operation);
            }
        }
        return operation;
    }

    private void mergeSortedEffects(Operator.Effect first, Operator.Effect second, ValuesMergingReverse operation) {
        operation.setOldValue(second.getNewValue().getUpMostParent());
        operation.setNewValue(first.getNewValue().getUpMostParent());
        second.getNewValue().setParent(first.getNewValue().getUpMostParent());
    }

    /**
     * Returns true only if operator has exactly 0 active precondition and 1 active effect; otherwise returns false.
     *
     * @param operator
     * @return
     */
    private boolean isCandidate(Operator operator) {
        return operator.getNumberOfActivePreconditions() == 0 && operator.getNumberOfActiveEffects() == 1;
    }

}
