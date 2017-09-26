package cz.agents.dimap.psmsas.operations.reverse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.sas.Active;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

public class UniversalReverse implements ReverseOperation {
    private final String description;
    private final List<Active> toActivate = new ArrayList<>();
    private final List<Active> toDeactivate = new ArrayList<>();
    private final List<Variable.Value> initChanges = new ArrayList<>();
    private final List<Variable> addToGoal = new ArrayList<>();
    private final List<Variable> removeFromGoal = new ArrayList<>();

    private final Map<Operator, Operator> operatorRenames = new HashMap<>();

    public UniversalReverse(String description) {
        this.description = description;
    }

    public void activate(Active active) {
        toActivate.add(active);
    }

    public void deactivate(Active active) {
        toDeactivate.add(active);
    }

    public void addToInit(Variable.Value value) {
        initChanges.add(value);
    }

    @Override
    public void extend(Plan plan, SasFile sasFile) {
        for (Active active : toActivate) {
            active.setActive();
        }
        for (Active active : toDeactivate) {
            active.setActive(false);
        }
        for (Variable.Value value : initChanges) {
            value.getVariable().setInitValue(value);
        }
        for (Variable variable : removeFromGoal) {
            sasFile.getGoalVariables().remove(variable);
            variable.setInGoal(false);
        }
        for (Variable variable : addToGoal) {
            sasFile.getGoalVariables().add(variable);
            variable.setInGoal(true);
        }
        if (!operatorRenames.isEmpty()) {
            List<Operator> newPlan = new ArrayList<>();
            for (Operator operator : plan.getPlan()) {
                if (operatorRenames.containsKey(operator)) {
                    newPlan.add(operatorRenames.get(operator));
                } else {
                    newPlan.add(operator);
                }
            }
            plan.setPlan(newPlan);
        }
    }

    @Override
    public Integer getNumberOfReductions() {
        return toActivate.size() + toDeactivate.size() + initChanges.size() + removeFromGoal.size() + addToGoal.size() + operatorRenames.size();
    }
    
    @Override
    public String toString() {
        return description  ;
    }

    public void addToGoal(Variable variable) {
        addToGoal.add(variable);
    }

    public void removeFromGoal(Variable variable) {
        removeFromGoal.add(variable);
    }

    public void renameOperators(Collection<Operator> newOperators, Operator originalOperator) {
        for (Operator newOperator : newOperators) {
            renameOperator(newOperator, originalOperator);
        }
    }

    public void renameOperator(Operator newOperator, Operator originalOperator) {
        operatorRenames.put(newOperator, originalOperator);
    }
}
