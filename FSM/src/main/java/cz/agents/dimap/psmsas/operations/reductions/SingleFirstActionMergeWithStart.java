package cz.agents.dimap.psmsas.operations.reductions;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.ActionStartMergeReverse;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.Operator.Effect;
import cz.agents.dimap.tools.sas.Operator.Precondition;
import cz.agents.dimap.tools.sas.SasFile;

/**
 * Created by pah on 14.8.15.
 */
public class SingleFirstActionMergeWithStart extends ReduceOperationImpl {
    
    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        if (Settings.DEBUG) {
            System.out.println("SingleFirstActionMergeWithStart");
        }
        Operator applicableOperator = null;
        for (Operator operator : sasFile.getOperators().values()) {
            if (operator.isActive() && isApplicableInInit(operator)) {
                if (applicableOperator == null) {
                    applicableOperator = operator;
                } else {
                    // multiple applicable operators in init
                    return null;
                }
            }
        }
        if (applicableOperator != null) {
            if (Settings.ALLOW_TRANSFORMATIONS || ActionStartMergeReverse.isOnlyOnce(applicableOperator, sasFile)) {
                return new ActionStartMergeReverse(applicableOperator, sasFile, "SingleFirstActionMergeWithStart");
            } else {
                return null;
            }
        } else {
            for (Operator operator : sasFile.getOperators().values()) {
                //TODO replace by for (Operator operator : sasFile.getActiveOperators()) {
                if (operator.isActive()) {
                    // no applicable operator => no solution!!!
                    throw new IllegalStateException();
                }
            }
            // no active operator - that's ok, maybe the problem is already completly reduced
            return null;
        }
    }

    private boolean isApplicableInInit(Operator operator) {
        for (Precondition precondition : operator.getPreconditions()) {
            if (precondition.isActive() && !precondition.getValue().isInitValue()) {
                return false;
            }
        }
        for (Effect effect : operator.getEffects()) {
            if (effect.isActive() && !effect.getOldValue().isInitValue()) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void clearCache() {
    }

}
