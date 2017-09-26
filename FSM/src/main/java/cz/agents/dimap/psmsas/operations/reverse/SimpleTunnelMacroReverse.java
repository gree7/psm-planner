package cz.agents.dimap.psmsas.operations.reverse;

import java.util.LinkedList;
import java.util.List;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;

public class SimpleTunnelMacroReverse implements ReverseOperation {
    private final Operator merged;
    private final Operator first;
    private final Operator second;

    public SimpleTunnelMacroReverse(Operator merged, Operator first, Operator second) {
        this.merged = merged;
        this.first = first;
        this.second = second;
    }

    @Override
    public void extend(Plan plan, SasFile sasFile) {
        undoChanges();

        if (merged != null) {
            List<Operator> changedPlan = new LinkedList<>();
            for (Operator operator : plan.getPlan()) {
                if(operator == merged){
                    changedPlan.add(first);
                    changedPlan.add(second);
                } else {
                    changedPlan.add(operator);
                }
            }
            plan.setPlan(changedPlan);
        }
    }

    public void undoChanges() {
        first.setActive();
        second.setActive();
        if (merged != null) {
            merged.setActive(false);
        }
    }

    @Override
    public String toString() {
        return "SimpleTunnelMacro-"+this.first+"__"+this.second;
    }

    @Override
    public Integer getNumberOfReductions() {
        return 1;
    }

}
