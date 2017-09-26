package cz.agents.dimap.psmsas.operations.reverse;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.sas.Operator.Effect;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable.Value;

public class GroundSimpleActionReverse implements ReverseOperation {

    private Effect effect;
    private String operatorName;
    private Value value;

    public GroundSimpleActionReverse(Effect effect, Value value, String operatorName) {
        this.effect = effect;
        this.value = value;
        this.operatorName = operatorName;
    }

    @Override
    public void extend(Plan plan, SasFile sasFile) {
        effect.setOldValue(value);
    }

    @Override
    public Integer getNumberOfReductions() {
        return 1;
    }

    @Override
    public String toString() {
        return "GroundSimpleAction-"+operatorName;
    }
}
