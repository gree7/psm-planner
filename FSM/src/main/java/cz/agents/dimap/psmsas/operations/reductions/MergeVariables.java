package cz.agents.dimap.psmsas.operations.reductions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.dimap.psmsas.operations.ReduceOperationLazyImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.UniversalReverse;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.Operator.Effect;
import cz.agents.dimap.tools.sas.Operator.Precondition;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;
import cz.agents.dimap.tools.sas.Variable.Value;

public class MergeVariables extends ReduceOperationLazyImpl {

    private static final String GOAL_ACTION_PREFIX = "goal-action";

    private static final String IN_GOAL_VALUE = "Atom in-goal()";

    private static final int MAX_VALUES = 2;

    protected boolean isGoalMoved = false;
    
    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        ReverseOperation reverse = checkGoal(sasFile);
        if (reverse != null) {
            return reverse;
        }

        Set<Variable> variables = selectVariablesForMerge(sasFile);
        if (variables == null) {
            return null;
        }
        
        reverse = addGoalAction(sasFile, variables);
        if (reverse != null) {
            return reverse;
        }
        
        return mergeVariables(sasFile, new ArrayList<>(variables));
    }

    private ReverseOperation checkGoal(SasFile sasFile) {
        if (isGoalMoved) {
            for (Operator operator : sasFile.getOperators().values()) {
                if (operator.isActive()) {
                    if (operator.getName().startsWith(GOAL_ACTION_PREFIX)) {
                        if (isApplicableInInit(operator)) {
                            ReverseOperation reverse = applyOperatorInInit(operator);
                            if (reverse != null) {
                                ReverseOperation reversePruning = new ValuesPruning().reduce(sasFile);
                                if (reversePruning != null) {
                                    MultiReverseOperation multiReverse = new MultiReverseOperation("MergeVariables: Apply goal action in init.");
                                    multiReverse.addReverseOperation(reverse);
                                    multiReverse.addReverseOperation(reversePruning);
                                    return multiReverse;
                                }
                            }
                            return reverse;
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean isApplicableInInit(Operator operator) {
        for (Precondition precondition : operator.getPreconditions()) {
            if (precondition.isActive() && !precondition.getValue().isInitValue()) {
                return false;
            }
        }
        for (Effect effect : operator.getEffects()) {
            if (effect.isActive() && !effect.getOldValue().isInitValue()) {
                return false;
            }
        }
        return true;
    }

    private ReverseOperation applyOperatorInInit(final Operator operator) {
        UniversalReverse reverse = new UniversalReverse("Apply goal " + operator + " in init.") {
            public void extend(Plan plan, SasFile sasFile) {
                super.extend(plan, sasFile);
                if (plan.getSize() != 0) {
                    throw new IllegalStateException("Plan should be empty!");
                }
                plan.getPlan().add(operator);
            };
        };
        for (Effect effect : operator.getEffects()) {
            if (effect.isActive()) {
                Variable.Value originalValue = effect.getOldValue().getUpMostParent();
                if (originalValue.isAnyValue()) {
                    originalValue = originalValue.getVariable().getInitValue().getUpMostParent();
                }
                Variable.Value newState = effect.getNewValue().getUpMostParent();
                if (newState != originalValue) {
                    originalValue.getVariable().setInitValue(newState);
                    reverse.addToInit(originalValue);
                }
            }
        }
        operator.setActive(false);
        reverse.activate(operator);
        return reverse;
    }

    
    public int getNumberOfGoalValues(SasFile sasFile) {
        int sum = 0;
        for (Variable variable : sasFile.getVariables().values()) {
            if (variable.isActive()) {
                for (Variable.Value value : variable.getValues()) {
                    if (value.isActive() && !value.getUpMostParent().isAnyValue()) {
                        String valueString = value.toString();
                        if (valueString.contains(IN_GOAL_VALUE) || valueString.contains("goal-fact")) {
                            sum++;
                        }
                    }
                }
            }
        }
        return sum;
    }

    public int getNumberOfGoalOperators(SasFile sasFile) {
        // may be some of that could be done by caching efficiently
        int sum = 0;
        for (Operator operator : sasFile.getOperators().values()) {
            if (operator.isActive()) {
                if (operator.getName().startsWith(GOAL_ACTION_PREFIX)) {
                    sum++;
                }
            }
        }
        return sum;
    }

    private Set<Variable> selectVariablesForMerge(SasFile sasFile) {
        Map <Set<Variable>, Integer> variableCounts = new HashMap<>();  
        for (Operator operator : sasFile.operators.values()) {
            if (operator.isActive()) {
                if (operator.getName().startsWith(GOAL_ACTION_PREFIX)) {
                    continue;
                }
                Set<Variable> variables = getAffectedVariables(operator); 
                if ((variables.size() > 1) && (variables.size() <= MAX_VALUES) ) {
                    Integer count = variableCounts.get(variables);
                    if (count == null) {
                        count = 0;
                    }
                    variableCounts.put(variables, count + 1 );
                }
            }
        }
        int max = 0;
        Set<Variable> maxVariables = null;
        for (Entry<Set<Variable>, Integer> entry : variableCounts.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                maxVariables = entry.getKey();
            }
        }
        if (max > 1) {
            return maxVariables;
        } else {
            return null;
        }
    }

    private Set<Variable> getAffectedVariables(Operator operator) {
        Set<Variable> variables = new HashSet<>();
        for (Precondition precondition : operator.getPreconditions()) {
            if (precondition.isActive()) {
                variables.add(precondition.getVariable());
            }
        }
        for (Effect effect : operator.getEffects()) {
            if (effect.isActive()) {
                variables.add(effect.getVariable());
            }
        }
        return variables;
    }

    private ReverseOperation mergeVariables(SasFile sasFile, List<Variable> variables) {
        MultiReverseOperation multiReverse = new MultiReverseOperation("MergeVariables-"+variables, 1);

        UniversalReverse reverse = new UniversalReverse("mv:");

        Map<Set<Value>, Value> newValues = new HashMap<>();
        Variable newVariable = new Variable(sasFile.getVariables().size(), -1);
        newVariable.setActive();
        reverse.deactivate(newVariable);
        
        Map<Variable, Value> curInitValues = new HashMap<>();
        for (Variable variable : variables) {
            curInitValues.put(variable, variable.getInitValue().getUpMostParent());
        }
        Value newInitValue = getNewValue(newValues, newVariable, curInitValues);
        newVariable.setInitValue(newInitValue.getOriginalID());
        sasFile.variables.put(newVariable.getOriginalId(), newVariable);

        Collection<Operator> newOperators = new ArrayList<>();
        for (Operator operator : sasFile.getOperators().values()) {
            if (operator.isActive()) {
                Collection<Operator> operators = updateOperatorWithAnyValues(operator, variables, newValues, newVariable);
                if (operators != null) {
                    newOperators.addAll(operators);
                    operator.setActive(false);
                    reverse.activate(operator);
                    reverse.renameOperators(operators, operator);
                }
            }
        }

        for (Operator operator : newOperators) {
            sasFile.getOperators().put(operator.getName(), operator);
            sasFile.storeOperator(operator);
            reverse.deactivate(operator);
        }
        
        for (Variable variable : variables) {
            for (Value value : variable.getValues()) {
                if (value.isActive()) {
                    value.setActive(false);
                    reverse.activate(value);
                    ReverseOperation reverseMutex = sasFile.removeValueFromMutexes(value);
                    multiReverse.addReverseOperation(reverseMutex);
                }
            }
            variable.setActive(false);
            reverse.activate(variable);
        }

        multiReverse.addReverseOperation(reverse);
        
        multiReverse.addReverseOperation(new ValuesPruning().reduce(sasFile));

        return multiReverse;
    }

    private ReverseOperation addGoalAction(SasFile sasFile, Set<Variable> variables) {
        for (Variable variable : variables) {
            if (variable.isInGoal()) {
                if (isGoalMoved) {
                    return null;
                }
                isGoalMoved = true;

                UniversalReverse reverse = new UniversalReverse("MergeVariables: Add goal action.") {
                    public void extend(Plan plan, SasFile sasFile) {
                        super.extend(plan, sasFile);
                        boolean afterGoalOp = false;
                        for (Iterator<Operator> iterator = plan.getPlan().iterator(); iterator .hasNext();) {
                            Operator operator = iterator.next();
                            if (afterGoalOp) {
                                iterator.remove();
                            } else if (operator.getName().startsWith(GOAL_ACTION_PREFIX)) {
                                iterator.remove();
                                afterGoalOp = true;
                            }
                        }
                    };
                };

                // add goal action
                Operator goalOperator = new Operator(GOAL_ACTION_PREFIX, new ArrayList<Precondition>(), new ArrayList<Effect>());
                for (Variable goalVar : sasFile.getGoalVariables()) {
                    Value newGoalVal = goalVar.addNextValue(IN_GOAL_VALUE);
                    reverse.deactivate(newGoalVal);
                    goalOperator.addEffectStartingWithZero(goalVar, goalVar.getGoalValue().getUpMostParent(), newGoalVal);
                    goalVar.setInGoal(false);
                    reverse.addToGoal(goalVar);
                }
                sasFile.getGoalVariables().clear();

                Variable newVariable = new Variable(sasFile.getVariables().size(), -1);
                Value initVal = newVariable.addNextValue("Atom not-goal-fact()");
                reverse.deactivate(initVal);
                Value goalVal = newVariable.addNextValue("Atom goal-fact()");
                reverse.deactivate(goalVal);

                goalOperator.addEffectStartingWithZero(newVariable, initVal, goalVal);
                
                newVariable.setInitValue(initVal);
                newVariable.setInGoal(true);
                newVariable.setGoalValue(goalVal);
                sasFile.getGoalVariables().add(newVariable);
                reverse.removeFromGoal(newVariable);

                sasFile.variables.put(newVariable.getOriginalId(), newVariable);
                reverse.deactivate(newVariable);

                sasFile.getOperators().put(goalOperator.getName(), goalOperator);
                sasFile.storeOperator(goalOperator);
                reverse.deactivate(goalOperator);
                return reverse;
            }
        }
        return null;
    }

    private Collection<Operator> updateOperatorWithAnyValues(Operator operator, List<Variable> variables, Map<Set<Value>, Value> newValues, Variable newVariable) {
        Operator tmpOperator = new Operator(operator, operator.getName());
        for (Iterator<Effect> iterator = tmpOperator.getEffects().iterator(); iterator.hasNext();) {
            Effect effect = iterator.next();
            if (effect.isActive() && effect.getOldValue().getUpMostParent().isAnyValue()) {
                iterator.remove();
                Collection<Operator> newOperators = new ArrayList<>();
                for (Value oldValue : effect.getVariable().getValues()) {
                    if (oldValue.isActive()) {
                        Operator newOperator = new Operator(tmpOperator, tmpOperator.getName()+"-"+oldValue.getVariable()+"_"+oldValue);
                        newOperator.addEffectStartingWithZero(oldValue.getVariable(), oldValue, effect.getNewValue());
                        newOperators.addAll(updateOperatorWithAnyValues(newOperator, variables, newValues, newVariable));
                    }                    
                }
                return newOperators;
            }
        }
        return updateOperator(operator, variables, newValues, newVariable);
    }
    
    private Collection<Operator> updateOperator(Operator operator, List<Variable> variables, Map<Set<Value>, Value> newValues, Variable newVariable) {

        if (variables.size() > 2) throw new RuntimeException("Not implemented yet!!!");

        Operator newOperator = new Operator(operator, operator.getName()+"-m");

        Map<Variable, Value> curVariables = new HashMap<>();
        
        for (Iterator<Precondition> iterator = newOperator.getPreconditions().iterator(); iterator.hasNext();) {
            Precondition precondition = iterator.next();
            Variable variable = precondition.getVariable();
            if (!variables.contains(variable)) {
                continue;
            }
            curVariables.put(variable, precondition.getValue().getUpMostParent());
            
            iterator.remove();
        }

        Map<Variable, Value> curOldVariables = new HashMap<>();
        Map<Variable, Value> curNewVariables = new HashMap<>();

        if (curVariables.isEmpty()) {
            // ok, nothing to do
        } else if (curVariables.size() == variables.size()) {
            Value newValue = getNewValue(newValues, newVariable, curVariables);
            newOperator.addPrecondition(newVariable, newValue);
            return Arrays.asList(newOperator);
        } else {
            // nothing to do - curVariables will be added to curOldVariables and to curNewVariables later
        }

        for (Iterator<Effect> iterator = newOperator.getEffects().iterator(); iterator.hasNext();) {
            Effect effect = iterator.next();
            Variable variable = effect.getVariable();
            if (!variables.contains(variable)) {
                continue;
            }
            if (effect.getOldValue().getUpMostParent().isAnyValue()) {
                throw new UnsupportedOperationException("Not implemented yet!!!");
            }
            curOldVariables.put(variable, effect.getOldValue().getUpMostParent());
            curNewVariables.put(variable, effect.getNewValue().getUpMostParent());
            
            iterator.remove();
        }

        if (curOldVariables.isEmpty()) {
            if (curVariables.isEmpty()) {
                // ok, nothing to do
            } else {
                Set<Variable> vars = new HashSet<>(variables);
                vars.removeAll(curVariables.keySet());
                if (vars.size() != 1) {
                    throw new IllegalStateException("Something wrong since MAX_VALUES should be 2. It is: " + MAX_VALUES);
                }
                Collection<Operator> newOperators = new ArrayList<>();
                Variable missingVariable = vars.iterator().next();
                for (Value value : missingVariable.getValues()) {
                    if (value.isActive() && !value.toString().contains(IN_GOAL_VALUE)) {
                        Operator newOp = new Operator(newOperator, newOperator.getName()+"-"+value);
                        curVariables.put(missingVariable, value);
                        Value newValue = getNewValue(newValues, newVariable, curVariables);
                        curVariables.remove(missingVariable);
                        newOp.addPrecondition(newVariable, newValue);
                        newOperators.add(newOp);
                    }
                }
                return newOperators;
            }
        } else {
            // add curVariables (from prevail preconditions)
            for (Entry<Variable, Value> curVar : curVariables.entrySet()) {
                curOldVariables.put(curVar.getKey(), curVar.getValue());
                curNewVariables.put(curVar.getKey(), curVar.getValue());
            }
            if (curOldVariables.size() == variables.size()) {
        
                Value newOldValue = getNewValue(newValues, newVariable, curOldVariables);
                Value newNewValue = getNewValue(newValues, newVariable, curNewVariables);
                newOperator.addEffectStartingWithZero(newVariable, newOldValue, newNewValue);
                return Arrays.asList(newOperator);
            } else {
                Set<Variable> vars = new HashSet<>(variables);
                vars.removeAll(curOldVariables.keySet());
                if (vars.size() != 1) {
                    throw new IllegalStateException("Something wrong since MAX_VALUES should be 2. It is: " + MAX_VALUES);
                }
                Collection<Operator> newOperators = new ArrayList<>();
                Variable missingVariable = vars.iterator().next();
                for (Value value : missingVariable.getValues()) {
                    if (value.isActive() && !value.toString().contains(IN_GOAL_VALUE)) {
    
                        Operator newOp = new Operator(newOperator, newOperator.getName()+"-"+value);
                        curOldVariables.put(missingVariable, value);
                        curNewVariables.put(missingVariable, value);
                        Value newOldValue = getNewValue(newValues, newVariable, curOldVariables);
                        Value newNewValue = getNewValue(newValues, newVariable, curNewVariables);
                        curOldVariables.remove(missingVariable);
                        curNewVariables.remove(missingVariable);
                        newOp.addEffectStartingWithZero(newVariable, newOldValue, newNewValue);
                        newOperators.add(newOp);
                    }
                }
                return newOperators;
            }
        }

        return null;
    }

    private Value getNewValue(Map<Set<Value>, Value> newValues, Variable newVariable, Map<Variable, Value> curVariables) {
        Set<Value> curValues = new HashSet<>(curVariables.values());
        Value newValue = newValues.get(curValues);
        if (newValue == null) {
            String label = "Atom " + curVariables.toString().replace("{", "").replace("}", "").replaceAll("=","").replaceAll("Atom ","").replaceAll("var\\d*","");
            newValue = newVariable.addNextValue(label);
            newValues.put(curValues, newValue);
        }
        return newValue;
    }

    @Override
    public void clearCache() {
    }
}
