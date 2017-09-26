package cz.agents.dimap.psmsas.operations.reverse;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by pah on 14.8.15.
 */
public class OneUsageReverse implements ReverseOperation {
    private final List<Operator.Effect> effects = new LinkedList<>();
    private final Variable.Value replacedValue;

    public OneUsageReverse(Variable.Value replacedValue) {
        this.replacedValue = replacedValue;
    }

    @Override
    public void extend(Plan plan, SasFile sasFile) {
        // we do not care about effect.getNewValue because we do not need final SAS after extensions
        for (Operator.Effect effect : effects) {
            effect.setNewValue(replacedValue);
        }
    }

    @Override
    public String toString() {
        return "OneUsage-"+replacedValue.getVariable()+"-"+replacedValue.getValue();
    }

    @Override
    public Integer getNumberOfReductions() {
        return 1;
    }

    public void addChangedEffect(Operator.Effect effect) {
        effects.add(effect);
    }
}
