package cz.agents.dimap.psmsas.operations.reverse;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;
import cz.agents.dimap.tools.sas.Variable.Value;

import java.util.HashMap;
import java.util.Map;

public class UseApplicableActionInStartReverse implements ReverseOperation {
    private final Map<Variable, Variable.Value> initState = new HashMap<>();
    private final Operator operator;

    public UseApplicableActionInStartReverse(Operator operator) {
        this.operator = operator;
        for (Operator.Effect effect : operator.getEffects()) {
            if(effect.isActive()){
                initState.put(effect.getVariable(),effect.getOldValue());
            }
        }
    }

    @Override
    public void extend(Plan plan, SasFile sasFile) {
        this.operator.setActive();
        undoInitChanges();
        plan.getPlan().add(0, this.operator);
    }

    private void undoInitChanges() {
        for (Map.Entry<Variable, Value> entry : initState.entrySet()) {
            Variable variable = entry.getKey();
            Value value = entry.getValue();
            variable.setInitValue(value);
        }
    }

    @Override
    public String toString() {
        return "UseApplicableActionInStart-"+operator;
    }

    @Override
    public Integer getNumberOfReductions() {
        return 1;
    }
}
