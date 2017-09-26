package cz.agents.dimap.psmsas.operations.reductions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.UniversalReverse;
import cz.agents.dimap.psmsas.operations.reverse.VariableDeletionReverse;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;
import cz.agents.dimap.tools.sas.Variable.Value;

/**
 * Created by pah on 10.7.15.
 * <p/>
 * Class representing operation that reduces variables. Each instance is a statefull object, so read method reduce and clearCache carefully.
 */
public class VariableDeletion extends ReduceOperationImpl {
    private final Set<String> cached = new HashSet<>();
    private Iterator<Variable> iterator;

    public VariableDeletion() {
    }

    /**
     * Reduces the SAS by erasing variable with only one value. If it is possible, object representing reverse operation is returned; otherwise null is returned.
     * This method is supposed to be called on the same SAS repeatedly until possible. But before calling it on a different SAS, call clearCache to reset the instance.
     *
     * @param sasFile
     * @return
     */
    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        if (Settings.DEBUG) {
            System.out.println("VariableDeletion reduction");
        }
        MultiReverseOperation multiReverse = new MultiReverseOperation("VariableDeletion");
        
        while (true) {
            ReverseOperation varDelReverse = findVariableAndProcess(sasFile, sasFile.getVariables(), sasFile.getOperators().values());
            if (varDelReverse == null) {
                break;
            }
            multiReverse.addReverseOperation(varDelReverse);
        }

        if (multiReverse.isEmpty()) {
            return null;
        } else {
            return multiReverse;
        }
    }

    /**
     * Finds a variable that can be erased and returns corresponding reverse operation.
     * If there is no variable that can be erased, it returns null.
     *
     * @param variables
     * @param values
     * @return
     */
    private ReverseOperation findVariableAndProcess(SasFile sasFile, Map<Integer, Variable> variables, Collection<Operator> values) {
        setIterator(variables);
        while (iterator.hasNext()) {
            Variable variable = iterator.next();
            if (variable.isActive() && variable.getNumberOfActiveValues() == 1) {
                MultiReverseOperation multiReverse = new MultiReverseOperation("VariableDeletion");
                UniversalReverse reverse = new UniversalReverse("Deactivate Values");
                variable.setActive(false);
                for (Value value : variable.getValues()) {
                    if (value.isActive()) {
                        value.setActive(false);
                        reverse.activate(value);
                        multiReverse.addReverseOperation(sasFile.removeValueFromMutexes(value));
                        break;
                    }
                }
                multiReverse.addReverseOperation(reverse);
                multiReverse.addReverseOperation(completeResurrection(variable, values));
                return multiReverse;
            }
        }
        return null;
    }

    /**
     * Stores information needed for reverse operation of deleting given variable and returns the reverse operation.
     *
     * @param variable
     * @param operators
     * @return
     */
    private ReverseOperation completeResurrection(Variable variable, Collection<Operator> operators) {
        VariableDeletionReverse reverse = new VariableDeletionReverse(variable);
        if (Settings.DEBUG) {
            System.out.println("variable " + variable.getOriginalId() + "/" + variable.getRealId() + " set to unactive state");
        }
        for (Operator operator : operators) {
            if (operator.isActive()) {
                completeOperatorResurrection(variable, reverse, operator);
            }
        }
        return reverse;
    }

    private void completeOperatorResurrection(Variable variable, VariableDeletionReverse reverse, Operator operator) {
        updatePreconditions(operator, variable, reverse);
        updateEffects(operator, variable, reverse);
        if (operator.actualize()) {
            reverse.addToActivize(operator);
            if (Settings.DEBUG) {
                System.out.println("DEBUG: operator actualization 1: '" + operator.getName() + "' because of setting to unactive state.");
            }
        }
    }

    private void updateEffects(Operator operator, Variable variable, VariableDeletionReverse reverse) {
        Set<Long> alreadyIn = new HashSet<>();
        for (Operator.Effect effect : operator.getEffects()) {
            if (effect.isActive() && effect.getVariable() == variable) {
                if (!effect.getVariable().isActive()) {
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
                        if (Settings.DEBUG) {
                            System.out.println("effect set to unactive state because of duplicates; operator: '" + operator.getName() + "', effect: '" + effect.getSAS() + "'");
                        }
                    }
                }
            }
        }
    }

    private void updatePreconditions(Operator operator, Variable variable, VariableDeletionReverse reverse) {
        for (Operator.Precondition precondition : operator.getPreconditions()) {
            if (precondition.isActive() && precondition.getVariable() == variable) {
                reverse.addToActivize(precondition);
                precondition.setActive(false);
                if (Settings.DEBUG) {
                    System.out.println("precondition set to unactive because of unactive variable in operator: '" + operator.getName() + "', precondition: " + precondition.getSAS());
                }
            }
        }
    }

    /**
     * Use this method before reduce on different SAS.
     */
    @Override
    public void clearCache() {
        cached.clear();
        iterator = null;
    }

    public void setIterator(Map<Integer, Variable> variables) {
        if (null == iterator) {
            iterator = variables.values().iterator();
        }
    }
}
