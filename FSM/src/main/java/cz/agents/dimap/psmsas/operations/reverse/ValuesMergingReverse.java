package cz.agents.dimap.psmsas.operations.reverse;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Active;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pah on 10.7.15.
 * <p/>
 * Class representing reverse operation to merging values.
 */
public class ValuesMergingReverse implements ReverseOperation {
    private final Variable variable;
    private final List<Active> changes = new LinkedList<>();
    private final List<Active> turnOff = new LinkedList<>();
    private Variable.Value oldValue;
    private Variable.Value newValue;
    private final Operator firstOperator;
    private final Operator secondOperator;
    private final Operator.Effect first;
    private final Operator.Effect second;
    private boolean forceConditionalEffect = false;


    public ValuesMergingReverse(Operator.Effect first, Operator.Effect second, Operator firstOperator, Operator secondOperator) {
        variable = first.getVariable();
        this.firstOperator = firstOperator;
        this.secondOperator = secondOperator;
        this.first = first;
        this.second = second;
    }

    public void addToActivize(Active active) {
        changes.add(active);
    }

    public Variable getVariable() {
        return variable;
    }

    public void setOldValue(Variable.Value oldValue) {
        this.oldValue = oldValue;
    }

    public void setNewValue(Variable.Value newValue) {
        this.newValue = newValue;
    }

    public void activizeChanges() {
        first.setActive();
        second.setActive();
        firstOperator.setActive();
        secondOperator.setActive();
        oldValue.setActive();
        for (Active active : changes) {
            active.setActive();
        }
    }

    /**
     * Extends given plan by splitting stored values.
     * @param plan
     * @param sasFile
     */
    @Override
    public void extend(Plan plan, SasFile sasFile) {
        List<Operator> changedPlan = new ArrayList<>();

        activizeChanges();
        undoAdded();
        Variable.Value currentState = variable.getInitValue().getUpMostParent();

        for (Operator operator : plan.getPlan()) {
            Variable.Value possibleChangedState = getConsistentStateWithOperatorPrecondition(currentState, operator, changedPlan);
            if (null == possibleChangedState) {
                possibleChangedState = currentState;
            }
            currentState = processEffect(operator, changedPlan, currentState, possibleChangedState);
            changedPlan.add(operator);
        }

        if (variable.isInGoal()) {
            Variable.Value endState = variable.getGoalValue().getUpMostParent();
            makeConsistent(currentState, changedPlan, endState);
        }
        plan.setPlan(changedPlan);
    }

    private void undoAdded() {
        for (Active active : turnOff) {
            active.setActive(false);
        }
    }

    /**
     * Adds this corresponding operation to the changedPlan if need be, to satisfy precondition of the operator.
     * Returns null if the this operation (firstOperator or secondOperator) does not have to be applied; otherwise returns new value of the variable.
     *
     * @param currentState
     * @param operator
     * @param changedPlan
     * @return
     */
    private Variable.Value getConsistentStateWithOperatorPrecondition(Variable.Value currentState, Operator operator, List<Operator> changedPlan) {
        boolean changed = false;
        Variable.Value resultState = null;
        for (Operator.Precondition precondition : operator.getPreconditions()) {
            if (precondition.isActive() && precondition.getVariable() == variable) {

                Variable.Value expected = precondition.getValue().getUpMostParent();
                boolean check = (currentState == first.getOldValue().getUpMostParent() && expected == first.getNewValue().getUpMostParent()) ||
                        (currentState == second.getOldValue().getUpMostParent() && expected == second.getNewValue().getUpMostParent());

                if (expected != currentState && check) {
                    if (changed) {
                        throw new IllegalStateException("While making plan consistent, value of variable '" + variable.getOriginalId() + "' was changed more than once. So, all preconditions cannot be satisfied simultaneously.");
                    } else {
                        changed = true;
                        resultState = makeConsistent(currentState, changedPlan, expected);
                    }
                }
            }
        }
        return resultState;
    }

    private Variable.Value makeConsistent(Variable.Value currentState, List<Operator> changedPlan, Variable.Value expected) {
        if (currentState != expected) {
            if ((currentState == first.getOldValue().getUpMostParent() || first.getOldValue().getUpMostParent().isAnyValue()) && expected == first.getNewValue().getUpMostParent()) {
                changedPlan.add(firstOperator);
            } else if ((currentState == second.getOldValue().getUpMostParent() || second.getOldValue().getUpMostParent().isAnyValue())  && expected == second.getNewValue().getUpMostParent()) {
                changedPlan.add(secondOperator);
            } else {
                throw new IllegalStateException("Something is horribly wrong because by this reverse operation we cannot, even by forcing the conditional effects to happen, make the plan satisfiable.");
            }
            currentState = expected;
        }
        return currentState;
    }


    /**
     * This method takes care of dealing with effect of given operator. It forces to switch to corresponding value (and adds corresponding operation to changedPlan).
     * In the case that there change of state was forced before this (in order to setReachable precondition satisfiable), then it does corresponding effect of the operator.
     *
     * In case of more possible outcomes, messages to std.err should be written.
     *
     * @param operator
     * @param changedPlan
     * @param currentState
     * @param possibleChange
     * @return
     */
    private Variable.Value processEffect(Operator operator, List<Operator> changedPlan, Variable.Value currentState, Variable.Value possibleChange) {
        boolean allowedToMakeConsistent = currentState == possibleChange;
        Operator.Effect activeEffect = null;

        if (possibleChange == currentState) {
            // either it finds an effect that starts in this component (reverse op.) and force it,
            // in case that there was no changing state because of precondition of given operator
            activeEffect = retrieveEffect(operator, currentState);
        }
        if (null == activeEffect) {
            // or there were change of state because of precondition and so its look for effect starting in that state only
            allowedToMakeConsistent = false;
            if (possibleChange != null) { // because we start in possibleChange
                currentState = possibleChange;
            }
            activeEffect = retrieveEffectWithSameBase(operator, currentState);
        } else {
            currentState = possibleChange;
        }

        if (null != activeEffect) {
            if (!forceConditionalEffect && allowedToMakeConsistent) {
                forceConditionalEffect = true;
                if(Settings.DEBUG) System.out.println("forcing state, so that conditional effect may be used");
            }


            Variable.Value expected = activeEffect.getOldValue().getUpMostParent();
            Variable.Value next = activeEffect.getNewValue().getUpMostParent();
            if (allowedToMakeConsistent && !expected.isAnyValue()) {
                currentState = makeConsistent(currentState, changedPlan, expected);
            }

            if (currentState == expected || expected.isAnyValue()) {
                currentState = next;
            }
        }
        return currentState;
    }

    /**
     * Returns effect from given operator that is active and its condition is the same as given state.
     *
     * In case there is no such effect, null is returned.
     *
     * @param operator
     * @param state
     * @return
     */
    private Operator.Effect retrieveEffectWithSameBase(Operator operator, Variable.Value state) {
        Operator.Effect effect = null;
        for (Operator.Effect current : operator.getEffects()) {
            if (current.isActive() && current.getVariable() == variable) {
                boolean check = state == current.getOldValue().getUpMostParent();
                if (null != effect && check) {
                    System.err.println("WARNING: operator '" + operator.getName() + "' contains more conditional active effect on variable " + variable.getOriginalId() + ". Picking up the first.");
                } else if (check) {
                    effect = current;
                }
            }
        }
        return effect;
    }

    /**
     * Returns first active effect which condition is reachable from given state by applying one action inside the component; this is the force of effect to happen.
     *
     * In case there is no such effect, null is returned.
     *
     * @param operator
     * @param state
     * @return
     */
    private Operator.Effect retrieveEffect(Operator operator, Variable.Value state) {
        Operator.Effect effect = null;
        Variable.Value expected;
        if (state == oldValue.getUpMostParent()) {
            expected = newValue.getUpMostParent();
        } else if (state == newValue.getUpMostParent()) {
            expected = oldValue.getUpMostParent();
        } else {
            return null;
        }

        for (Operator.Effect current : operator.getEffects()) {
            if (current.isActive() && current.getVariable() == variable) {
                Variable.Value possible = current.getOldValue().getUpMostParent();
                boolean check = possible == expected;
                if (null != effect && check) {
                    System.err.println("WARNING: operator '" + operator.getName() + "' contains more conditional active effect on variable " + variable.getOriginalId() + ". Picking up the first.");
                } else if (check) {
                    // here is the force
                    effect = current;
                }
            }
        }
        return effect;
    }

    @Override
    public String toString() {
        return "ValuesMerging("+oldValue+", "+newValue+")";
    }

    @Override
    public Integer getNumberOfReductions() {
        return 1;
    }

    public void addToTurnOff(Active active) {
        turnOff.add(active);
    }



    /*tahle tovarna nize zde neni potreba, nemazu ale protoze by strukturou mohla by byt pouzitelna v

    private List<Operator> getConsistentPlan(Map<Integer, Variable.Value> currentState, Operator operator) {
        List<Operator> expansionPlan = new LinkedList();
        for (Operator.Precondition precondition : operator.getPreconditions()) {
            if(precondition.isActive()){
                satisfyPrecondition(currentState, precondition, expansionPlan);
            }
        }
        if(expansionPlan.size() > 1){
            throw new IllegalStateException("While making plan consistent, value of variable '"+variable.getOriginalId()+"' was changed more than once. So, all preconditions cannot be satisfied simultaneously.");
        }
        return expansionPlan;
    }*/

    /**
     * Appends to the expansionPlan operation reduced by this class in the case that the given precondition has to be satisfy and is not satisfied.
     * @param currentState
     * @param precondition
     * @param expansionPlan
     */
    /*private void satisfyPrecondition(Map<Integer, Variable.Value> currentState, Operator.Precondition precondition, List<Operator> expansionPlan) {
        if(precondition.getVariable() != variable){
            // does not have to consider this case, because this merging operation cannot effect this

            if(currentState.setReachable(variable.getOriginalId()) != precondition.getValue()){ // only chech, should not occure
               throw new UnsupportedOperationException("Trying to use operator with precondition that is not satisfied: precondition '"+precondition.getSAS()+"', value neede: '" + precondition.getValue().getUpMostParent().getOriginalID()+"/"+precondition.getValue().getOriginalID() +"'");
            }
            return;
        }

        Variable.Value current = currentState.setReachable(variable.getOriginalId()).getUpMostParent();
        Variable.Value expected = precondition.getValue().getUpMostParent();
        if(current != expected){
            if(oldValue.getUpMostParent() == current  && newValue.getUpMostParent() == expected ){
                apply(currentState,firstOperator);
                expansionPlan.add(firstOperator);
            }else if(newValue.getUpMostParent() == current && oldValue.getUpMostParent() == expected){
                apply(currentState,secondOperator);
                expansionPlan.add(secondOperator);
            }else{
                throw new IllegalStateException("Cannot setReachable the precondition '" + precondition.getSAS() + "', because this merging effect does not cause this situation.");
            }
        }
    }
    */

    /**
     * Modifies the state by applying the operator. Does not check whether the operator is applicable.
     * @param state
     * @param operator
     */
    /*private void apply(Map<Integer, Variable.Value> state, Operator operator) {
        for (Operator.Effect effect : operator.getEffects()) {
            if(effect.isActive()){
                Variable effectVariable = effect.getVariable();
                Variable.Value expected = effect.getOldValue().getUpMostParent();
                Variable.Value current = state.setReachable(effectVariable.getOriginalId()).getUpMostParent();
                if(expected == current){
                    state.put(effectVariable.getOriginalId(),effect.getNewValue().getUpMostParent());
                }
            }
        }
    }
    */

    /**
     * Fills init state in the state variable if init set to true; otherwise fills in goal state.
     */
    /*public static void fillState(Collection<Variable> variables, boolean init, Map<Integer, Variable.Value> state) {
        for (Variable variable : variables) {
            if (init) {
                state.put(variable.getOriginalId(), variable.getInitValue());
            } else if (variable.isInGoal()) {
                state.put(variable.getOriginalId(), variable.getGoalValue());
            }
        }
    }
    */
    /*
    public Map<Integer, Variable.Value> getEndState(Map<Integer, Variable> variables) {
        Map<Integer, Variable.Value> state = new HashMap<>();
        fillState(variables.values(), false, state);
        return state;
    }

    public Map<Integer, Variable.Value> getInitState(Map<Integer, Variable> variables) {
        Map<Integer, Variable.Value> state = new HashMap<>();
        fillState(variables.values(), true, state);
        return state;
    }
    */
}
