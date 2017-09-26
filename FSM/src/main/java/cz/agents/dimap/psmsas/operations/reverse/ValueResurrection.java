package cz.agents.dimap.psmsas.operations.reverse;

import java.util.LinkedList;
import java.util.List;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.sas.Active;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

/**
 * Created by pah on 17.7.15.
 */
public class ValueResurrection implements ReverseOperation {
    private final List<Active> changes;

    public ValueResurrection(List<Active> changes, Variable.Value value) {
        this.changes = changes;
        changes.add(value);
    }

    public ValueResurrection(Variable.Value value) {
        this.changes = new LinkedList<>();
        changes.add(value);
    }

    public ValueResurrection() {
        this.changes = new LinkedList<>();
    }

    public void addToActivize(Active active) {
        changes.add(active);
    }

    public List<Active> getChanges() {
        return changes;
    }

    public void activizeChanges() {
        for (Active active : changes) {
            active.setActive();
        }
    }

    @Override
    public void extend(Plan plan, SasFile sasFile) {
        activizeChanges();
    }

    @Override
    public String toString() {
        return "ValuePruning: pruned: " + changes;
    }

    @Override
    public Integer getNumberOfReductions() {
        return 1;
    }
}
