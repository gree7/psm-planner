package cz.agents.dimap.psmsas.operations.reverse;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.sas.Active;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pah on 10.8.15.
 */
public class HalfCycleReverse implements ReverseOperation {
    private List<Active> changes = new LinkedList<>();
    private final List<Active> turnOff = new LinkedList<>();
    private final Operator operator;
    private final Operator.Effect effect;
    private final Variable.Value newValue;
    private final Variable.Value oldValue;

    public HalfCycleReverse(Operator operator, Operator.Effect effect) {
        this.operator = operator;
        this.effect = effect;
        this.newValue = effect.getNewValue().getUpMostParent();
        this.oldValue = effect.getOldValue().getUpMostParent();
    }

    @Override
    public void extend(Plan plan, SasFile sasFile) {
        List<Operator> changedPlan = new ArrayList<>();

        undoTurnedOffThings();
        undoAdded();
        Variable variable = effect.getVariable();
        Variable.Value currentState = variable.getInitValue().getUpMostParent();

        for (Operator operator : plan.getPlan()) {
            Variable.Value wantedState = getWantedState(operator, variable);
            if (null != wantedState) {
                if (wantedState == newValue && currentState == oldValue) {
                    changedPlan.add(this.operator);
                } else if(wantedState != currentState) {
                    System.err.println("Somewhere it went wrong during expanding value dependency. In state '" + currentState.getOriginalID() + "', want to be in state '" + wantedState.getOriginalID() + "', but cannot switch with effect '" + variable.getOriginalId() + "' : '" + oldValue.getOriginalID() + "' -> '" + newValue.getOriginalID() + "'.");// jinak s tim stejnak nemuzu nic delat
                }
                currentState = wantedState;
            }
            currentState = processEffect(operator, currentState, variable);
            changedPlan.add(operator);
        }

        if (variable.isInGoal()) {
            Variable.Value endState = variable.getGoalValue().getUpMostParent();
            makeConsistent(currentState, changedPlan, endState);
        }
        plan.setPlan(changedPlan);
    }

    private void makeConsistent(Variable.Value currentState, List<Operator> changedPlan, Variable.Value endState) {
        if(currentState == oldValue && endState == newValue){
            changedPlan.add(operator);
        }
    }

    private Variable.Value processEffect(Operator operator, Variable.Value currentState, Variable variable) {
        for (Operator.Effect effect : operator.getEffects()) {
            if(effect.isActive() && effect.getVariable() == variable){
                return effect.getNewValue().getUpMostParent();
            }
        }
        return currentState;
    }

    private Variable.Value getWantedState(Operator operator, Variable variable) {
        for (Operator.Precondition precondition : operator.getPreconditions()) {
            if (precondition.isActive() && precondition.getVariable() == variable) {
                return precondition.getValue().getUpMostParent();
            }
        }
        for (Operator.Effect effect : operator.getEffects()) {
            if (effect.isActive() && effect.getVariable() == variable) {
                return effect.getOldValue().getUpMostParent();
            }
        }
        return null;
    }

    private void undoTurnedOffThings() {
        for (Active active : changes) {
            active.setActive();
        }
    }

    private void undoAdded() {
        for (Active active : turnOff) {
            active.setActive(false);
        }
    }

    public void addToTurnOn(Active active) {
        changes.add(active);
    }

    public void addToTurnOff(Active active) {
        turnOff.add(active);
    }

    @Override
    public String toString() {
        return "HalfCycle-"+operator;
    }

    @Override
    public Integer getNumberOfReductions() {
        return 1;
    }
}
