package cz.agents.dimap.psmsas.operations.reverse;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.sas.Active;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by pah on 10.7.15.
 */
public class VariableDeletionReverse implements ReverseOperation {
    private final List<Active> changes = new LinkedList<>();
    private final Variable variable;

    public VariableDeletionReverse(Variable variable) {
        this.variable = variable;
        addToActivize(variable);
    }

    public void addToActivize(Active active) {
        changes.add(active);
    }

    public void activizeChanges() {
        for (Active active : changes) {
            active.setActive();
        }
    }

    /**
     * Extends given plan by activating stored variable.
     *
     * @param plan
     * @param sasFile
     */
    @Override
    public void extend(Plan plan, SasFile sasFile) {
        activizeChanges();
    }

    @Override
    public String toString() {
        return "VariableDeletion-"+variable.getOriginalId();
    }

    @Override
    public Integer getNumberOfReductions() {
        return 1;
    }
}
