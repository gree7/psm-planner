package cz.agents.dimap.psmsas.operations.reverse;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.sas.Active;
import cz.agents.dimap.tools.sas.SasFile;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by pah on 27.7.15.
 */
public class DeadEndsReverse implements ReverseOperation {
    private final List<Active> changes = new LinkedList<>();
    private final List<Active> added = new LinkedList<>();


    @Override
    public void extend(Plan plan, SasFile sasFile) {
        undoTurnOnRemoved();
        undoTurnOffAdded();
    }

    private void undoTurnOffAdded() {
        for (Active active : added) {
            active.setActive(false);
        }
    }

    private void undoTurnOnRemoved() {
        for (Active change : changes) {
            change.setActive();
        }
    }

    public List<Active> getChanges() {
        return changes;
    }

    public void addToSetOn(Active active) {
        changes.add(active);
    }

    public void addToTurnOff(Active active) {
        added.add(active);
    }

    @Override
    public String toString() {
        return "DeadEndsReverse";
    }

    @Override
    public Integer getNumberOfReductions() {
        return 1;
    }
}
