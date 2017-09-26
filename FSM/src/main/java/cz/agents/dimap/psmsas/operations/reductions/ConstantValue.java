package cz.agents.dimap.psmsas.operations.reductions;

import java.util.Set;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.ConstantValueReverse;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

/**
 * Created by pah on 26.8.15.
 */
public class ConstantValue extends ReduceOperationImpl {

    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        if(Settings.DEBUG){
            System.out.println("constant value reduction");
        }
        MultiReverseOperation reverse = new MultiReverseOperation("ConstantValueMulti");
        for (Variable variable : sasFile.getVariables().values()) {
            //System.out.println("considering variable\t" + variable.getOriginalId());
            if (variable.isActive()) {
                ConstantValueReverse miniReverse = findAndProcessReduction(variable, sasFile);
                if (null != miniReverse) {
                    reverse.addReverseOperation(miniReverse);
                }
            }
        }
        if (reverse.isEmpty()) {
            return null;
        }
        return reverse;
    }

    private ConstantValueReverse findAndProcessReduction(Variable variable, SasFile sasFile) {
        Variable.Value initValue = variable.getInitValue().getCachedUpMostParent();
        Set<Operator> consumers = sasFile.getConsumers(initValue);

        // value from init is in delete effect of some operator
        if (consumers.size() > 0) {
            return null;
        }
        //System.out.println(consumers.size() + "\tprocessing variable\t" + variable.getOriginalId());
        ConstantValueReverse reverse = new ConstantValueReverse(variable);

        if (!sasFile.getVariableOperators().containsKey(variable)) {
            for (Operator operator : sasFile.getVariableOperators().get(variable)) {
                Operator.Precondition precondition = operator.getActivePreconditionWithVariable(variable);

                // remove only operators having different value in precondition than value from init
                if (precondition.getValue().getCachedUpMostParent() != initValue) {
                    operator.setActive(false);
                    reverse.addToTurnOn(operator);
                }
            }
        }

        variable.setActive(false);
        reverse.addToTurnOn(variable);
        return reverse;
    }

    @Override
    public void clearCache() {

    }
}
