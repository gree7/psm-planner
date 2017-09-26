package cz.agents.dimap.psmsas.operations.reductions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.ValuesMergingReverse;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

/**
 * Created by pah on 10.7.15.
 * <p/>
 * Class merging two values of one variable if possible. Each instance of this class is statefull, so read reduce and clearCache method carefully.
 */
public class ValuesMerging extends ReduceOperationImpl {
    private int idx;
    private List<Operator> list;

    public ValuesMerging() {
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
        if (Settings.DEBUG) System.out.println("reducing effects");

        List<Operator> operators = getList(sasFile.getOperators().values());
        return findCoupleAndProcess(operators);
    }

    private List<Operator> getList(Collection<Operator> values) {
        if(0 == idx){
            list = new ArrayList<>();
            list.addAll(values);
        }
        return list;
    }

    /**
     * Use this method before reduce on different SAS.
     */
    @Override
    public void clearCache() {
        idx = 0;
    }

    /**
     * If possible, finds two values that can be merged and returns corresponding reverse operation; otherwise returns null.
     *
     * @param operators
     * @return
     */
    private ReverseOperation findCoupleAndProcess(List<Operator> operators) {
        Operator first, second;
        for (; idx < operators.size() - 1; idx++) {
            first = operators.get(idx);
            if (!isCandidate(first)) {
                continue;
            }
            for (int j = idx + 1; j < operators.size(); j++) {
                second = operators.get(j);
                if (isCandidate(second)) {
                    ReverseOperation reverse = mergeValues(first, second, operators);
                    if (null != reverse) {
                        idx++;
                        return reverse;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns reverse operation only if both operator can be merged; otherwise returns null. If the two operators can be merged, they are merged to one by to makeMerge method.
     *
     * @param first
     * @param second
     * @param operators
     * @return
     */
    private ReverseOperation mergeValues(Operator first, Operator second, List<Operator> operators) {
        Operator.Effect firstEffect = first.getFirstActiveEffect();
        Operator.Effect secondEffect = second.getFirstActiveEffect();

        if (!firstEffect.isOpposite(secondEffect)) {
            return null;
        }

        /*if (!isUsedOnlyOnce(first, firstEffect, operators)) {
            System.err.println("WARNING: merging effect that was found to be in more than one operator: '" + first.getSAS() + "'");
        }
        if (!isUsedOnlyOnce(second, secondEffect, operators)) {
            System.err.println("WARNING: merging effect that was found to be in more than one operator: '" + first.getSAS() + "'");
        }*/

        if (Settings.DEBUG) {
            System.out.println("merging effects:");
            System.out.println("\t" + first.getName() + "\t" + first.getCanonicalString());
            System.out.println("\t\t" + firstEffect.getCanonic());
            System.out.println("\t" + second.getName() + "\t" + second.getCanonicalString());
            System.out.println("\t\t" + secondEffect.getCanonic());
        }

        ValuesMergingReverse reverse = mergeEffects(first, firstEffect, second, secondEffect);


        firstEffect.actualize();
        secondEffect.actualize();

        return processOperationChanges(operators, reverse);
    }

    /**
     * Takes care of changes that should be stored in reverse action.
     *
     * @param operators
     * @param reverse
     * @return
     */
    private ValuesMergingReverse processOperationChanges(List<Operator> operators, ValuesMergingReverse reverse) {
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

    private void updateEffects(Operator operator, Variable variable, ValuesMergingReverse reverse) {
        Set<Long> alreadyIn = new HashSet<>();
        for (Operator.Effect effect : operator.getEffects()) {
            if (effect.isActive() && effect.getVariable() == variable) {
                if (effect.getOldValue().getUpMostParent() == effect.getNewValue().getUpMostParent()) {
                    effect.setActive(false);
                    reverse.addToActivize(effect);
                    if (Settings.DEBUG) {
                        System.out.println("effect set to unactive state because of same old and new value, value: '" + effect.getVariable().getRealId() + "', operator: '" + operator.getName() + "', effect: '" + effect.getSAS() + "', precondition added");
                    }

                    Operator.Precondition precondition = operator.addPrecondition(effect.getVariable(), effect.getOldValue());
                    reverse.addToTurnOff(precondition);

                } else if (!effect.getVariable().isActive()) {
                    effect.setActive(false);
                    reverse.addToActivize(effect);
                    if (Settings.DEBUG) {
                        System.out.println("effect set to unactive state because of unactive variable: '" + effect.getVariable().getRealId() + "', operator: '" + operator.getName() + "', effect: '" + effect.getSAS() + "'");
                    }

                } else {
                    int inSize = alreadyIn.size();
                    alreadyIn.add(effect.getCanonic());
                    if (alreadyIn.size() == inSize) {
                        effect.setActive(false);
                        reverse.addToActivize(effect);
                        System.err.println("WARNING: effect become duplicated");
                        if (Settings.DEBUG) {
                            System.out.println("effect set to unactive state because of duplicates; operator: '" + operator.getName() + "', effect: '" + effect.getSAS() + "'");
                        }

                    }
                }
            }
        }
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
            if(first.getOldValue().getUpMostParent().isAnyValue() && second.getOldValue().getUpMostParent().isAnyValue()){
                if(first.getNewValue().getParentRealID() < second.getNewValue().getParentRealID()){
                    mergeSortedEffects(first, second, operation);
                }else{
                    mergeSortedEffects(second, first, operation);
                }
            }else if(first.getOldValue().getUpMostParent().isAnyValue()){
                mergeSortedEffects(first, second, operation);
            }else{
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
        second.getNewValue().setParent(first.getNewValue());
    }

    private boolean isUsedOnlyOnce(Operator operator, Operator.Effect effect, List<Operator> operators) {
        for (Operator current : operators) {
            if (!current.areSimilarlyEqual(operator)) {
                if (operator.containsSimilarEffect(effect)) {
                    return false;
                }
            }
        }
        return true;
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
