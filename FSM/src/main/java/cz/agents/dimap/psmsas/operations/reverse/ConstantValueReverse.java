package cz.agents.dimap.psmsas.operations.reverse;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.sas.Active;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by pah on 26.8.15.
 */
public class ConstantValueReverse implements ReverseOperation{
    private final Variable variable;
    private List<Active> changes = new LinkedList<>();

    public ConstantValueReverse(Variable variable) {
        this.variable = variable;
    }

    @Override
    public void extend(Plan plan, SasFile sasFile) {
        undoChanges();
    }

    private void undoChanges() {
        for (Active change : changes) {
            change.setActive(true);
        }
    }

    public void addToTurnOn(Active active){
        changes.add(active);
    }

    @Override
    public String toString() {
        return "ConstantValue-"+variable.getOriginalId();
    }

    @Override
    public Integer getNumberOfReductions() {
        return 1;
    }
}
