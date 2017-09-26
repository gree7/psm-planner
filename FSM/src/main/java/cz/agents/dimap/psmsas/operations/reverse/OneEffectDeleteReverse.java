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
 * Created by pah on 14.8.15.
 */
public class OneEffectDeleteReverse implements ReverseOperation {
    private final List<Active> changes = new LinkedList<>();
    private final Operator operator;

    public OneEffectDeleteReverse(Operator operator) {
        this.operator = operator;
    }

    @Override
    public void extend(Plan plan, SasFile sasFile) {
        undoChanges();
    }

    private void undoChanges() {
        for (Active change : changes) {
            change.setActive();
        }
    }

    @Override
    public String toString() {
        return "OneEffectDelete-"+operator;
    }

    @Override
    public Integer getNumberOfReductions() {
        return 1;
    }

    public void addToTurnOn(Active active) {
        changes.add(active);
    }
}
