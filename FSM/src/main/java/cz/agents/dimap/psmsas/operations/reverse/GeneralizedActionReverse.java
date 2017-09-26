package cz.agents.dimap.psmsas.operations.reverse;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.sas.Active;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

import java.util.*;

/**
 * Created by pah on 25.8.15.
 */
public class GeneralizedActionReverse implements ReverseOperation {
    private Operator operator;
    private final Variable variable;
    private final List<Active> changes = new LinkedList<>();
    private final Map<Variable.Value, Operator> memory = new HashMap<>();

    public GeneralizedActionReverse(Variable variable) {
        this.variable = variable;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    @Override
    public void extend(Plan plan, SasFile sasFile) {
        undoToTurnOn();

        if (null != operator) {
            List<Operator> list = new ArrayList<>();
            Variable.Value state = variable.getInitValue().getUpMostParent();

            for (Operator current : plan.getPlan()) {
                Operator.Precondition precondition = current.getActivePreconditionWithVariable(variable);

                if (null != precondition && precondition.getValue().getUpMostParent() != state.getUpMostParent() && current.getName().equals(this.operator.getName())) {
                    list.add(memory.get(state));
                    continue;
                }

                Operator.Effect effect = current.getActiveEffectWithVariable(variable);
                if (null != effect) {
                    state = effect.getNewValue().getUpMostParent();
                }
                list.add(current);
            }
            plan.setPlan(list);
        }
    }

    private void undoToTurnOn() {
        for (Active change : changes) {
            change.setActive();
        }
    }

    @Override
    public String toString() {
        return "GeneralizedAction-" + operator;
    }
    
    @Override
    public Integer getNumberOfReductions() {
        return 1;
    }

    public void addToTurnOn(Variable.Value value, Operator operator) {
        // this may also make merge of similar operators inside of it, we do not know which of the operators we need (the information is somewhere before this reduction)
        memory.put(value, operator);
        addToTurnOn(operator);
    }

    public void addToTurnOn(Active active) {
        changes.add(active);
    }

    public Operator getOperator() {
        return operator;
    }
}
