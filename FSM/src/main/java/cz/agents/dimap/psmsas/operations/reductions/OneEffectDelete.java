package cz.agents.dimap.psmsas.operations.reductions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.OneEffectDeleteReverse;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

/**
 * Created by pah on 14.8.15.
 */
public class OneEffectDelete extends ReduceOperationImpl {
    //TODO fix mutexes !!! 
    
    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        if (Settings.DEBUG) {
            System.out.println("one effect reduction");
        }
        for (Map.Entry<Variable, Set<Operator>> entry : sasFile.getVariableOperators().entrySet()) {
            Variable variable = entry.getKey();
            if (variable.isActive() && !variable.isInGoal()) {
                ReverseOperation reverse = process(variable, entry.getValue(), sasFile);
                if (null != reverse) {
                    return reverse;
                }
            }
        }
        return null;
    }

    private ReverseOperation process(Variable variable, Set<Operator> operators, SasFile sasFile) {
        Operator operator = null;
        for (Operator current : operators) {
            if (current.isActive() && null != current.getActiveEffectWithVariable(variable)) {
                if (null == operator) {
                    operator = current;
                } else {
                    return null;
                }
            }
        }
        if (null != operator) {
            Operator.Effect effect = operator.getActiveEffectWithVariable(variable);
            if (hasZeroConsumers(effect.getNewValue().getCachedUpMostParent(), sasFile) && isApplicableOnlyOnceWithoutDependency(operator, variable, sasFile)) {
                return executeReduction(sasFile, operator,effect);
            }
        }
        return null;
    }

    private ReverseOperation executeReduction(SasFile sasFile, Operator operator, Operator.Effect effect) {
        MultiReverseOperation multiReverse = new MultiReverseOperation("OneEffect");
        OneEffectDeleteReverse reverse = new OneEffectDeleteReverse(operator);
        effect.setActive(false);
        reverse.addToTurnOn(effect);
        Variable.Value removedValue = effect.getNewValue().getCachedUpMostParent();
        removedValue.setActive(false);
        ReverseOperation reverseMutex = sasFile.removeValueFromMutexes(removedValue);
        multiReverse.addReverseOperation(reverseMutex);
        reverse.addToTurnOn(removedValue);
        if (operator.actualize()) {
            operator.setActive(false);
            reverse.addToTurnOn(operator);
        }
        if (multiReverse.isEmpty()) {
            return reverse;
        } else {
            multiReverse.addReverseOperation(reverse);
            return multiReverse;
        }
    }

    // the dependency only means that value produced by the effect has not to be used anywhere
    private boolean isApplicableOnlyOnceWithoutDependency(Operator operator, Variable variable, SasFile sasFile) {
        for (Operator.Effect effect : operator.getEffects()) {
            if (effect.isActive() && effect.getVariable() != variable && hasZeroProducers(effect.getOldValue(), sasFile)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasZeroProducers(Variable.Value value, SasFile sasFile) {
        if (value.getUpMostParent().isAnyValue()) { // a co pripad kdy jsou pouze dve hodnoty u promenne???
            return false;
        }
        Set<Operator> producers = findProducers(value, sasFile);
        // kdyz by to produkovalo vic jak jeden operator tak jak zjistit ze se ten operator nemuze pouzit vickrat?
        if (producers.size() == 0 && value.getCachedUpMostParent() == value.getVariable().getInitValue().getCachedUpMostParent()) {
            return true;
        }
        return false;
    }

    private boolean hasZeroConsumers(Variable.Value value, SasFile sasFile) {
        if (value.getUpMostParent().isAnyValue()) { // a co pripad kdy jsou pouze dve hodnoty u promenne???
            return false;
        }
        Set<Operator> consumers = findRequirers(value, sasFile);
        if (consumers.size() == 0) {
            return true;
        }
        return false;
    }

    private Set<Operator> findRequirers(Variable.Value value, SasFile sasFile) {
        Set<Operator> set = new HashSet<>();
        if (!sasFile.getVariableOperators().containsKey(value.getVariable())) {
            return set;
        }
        for (Operator operator : sasFile.getVariableOperators().get(value.getVariable())) {
            if (operator.isActive() && operator.isNeedingValue(value.getCachedUpMostParent())) {
                set.add(operator);
            }
        }
        return set;
    }

    private Set<Operator> findProducers(Variable.Value value, SasFile sasFile) {
        Set<Operator> set = new HashSet<>();
        if (!sasFile.getVariableOperators().containsKey(value.getVariable())) {
            return set;
        }
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
