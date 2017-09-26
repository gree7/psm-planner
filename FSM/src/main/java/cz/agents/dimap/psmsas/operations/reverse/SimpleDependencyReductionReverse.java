package cz.agents.dimap.psmsas.operations.reverse;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.sas.Active;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by pah on 22.7.15.
 */
public class SimpleDependencyReductionReverse implements ReverseOperation{
    private final Operator first;
    private final Operator second;
    private final List<Operator.Effect> addedEffects = new LinkedList();
    private final List<Operator.Precondition> addedPreconditions = new LinkedList();
    private final List<Active> changes = new LinkedList();
    private Variable.Value oldValue, newValue;
    private Operator.Effect firstEffect;

    public SimpleDependencyReductionReverse(Operator first, Operator second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public void extend(Plan plan, SasFile sasFile) {
        List<Operator> changedPlan = new LinkedList<>();
        switchEffects();
        switchPreconditions();
        undoChanges();

        for (Operator operator : plan.getPlan()) {
            changedPlan.add(operator);
            if(operator == first){
                changedPlan.add(second);
            }
        }
        plan.setPlan(changedPlan);
    }

    private void switchPreconditions() {
        for (Operator.Precondition addedPrecondition : addedPreconditions) {
            first.getPreconditions().remove(addedPrecondition);
        }
    }

    private void switchEffects() {
        for (Operator.Effect addedEffect : addedEffects) {
            first.getEffects().remove(addedEffect);
        }
        if(null != oldValue && null != newValue && null != firstEffect){
            firstEffect.setNewValue(oldValue);
        }
    }


    public void addPrecondition(Operator.Precondition precondition, Operator first) {
        addToTurnOn(precondition);
        precondition.setActive(false);
        for (Operator.Precondition current : first.getPreconditions()) {
            if(current.isActive() && current.getValue().getUpMostParent() == precondition.getValue().getUpMostParent()){
                return;
            }
        }
        precondition.setActive(true);
        first.addPrecondition(precondition);
        addedPreconditions.add(precondition);
    }

    public void addEffect(Operator.Effect effect, Operator.Effect firstEffect) {
        if(effect.getVariable() == firstEffect.getVariable()){
            oldValue = firstEffect.getNewValue();
            newValue = effect.getNewValue();
            firstEffect.setNewValue(newValue);
            oldValue.setActive(false);
            addToTurnOn(oldValue);
            this.firstEffect = firstEffect;
        }else{
            first.getEffects().add(effect);
            addedEffects.add(effect);
        }
    }

    public void addToTurnOn(Active second) {
        changes.add(second);
    }

    public void undoChanges() {
        first.setActive();
        second.setActive();
        for (Active active : changes) {
            active.setActive();
        }
    }

    @Override
    public String toString() {
        return "SimpleDependencyReductionReverse-"+this.first+"__"+this.second;
    }

    @Override
    public Integer getNumberOfReductions() {
        return 1;
    }

}
