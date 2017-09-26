package cz.agents.dimap.psmsas.operations.reductions;

import java.util.ArrayList;
import java.util.List;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.ActionStartMergeReverse;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.UseApplicableActionInStartReverse;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;

/**
 * Created by pah on 25.8.15.
 */
public class UseApplicableActionInStart extends ReduceOperationImpl {


    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        if(Settings.DEBUG){
            System.out.println("UseApplicableActionInStart");
        }
        List<Operator> applicableOperators = sasFile.getApplicableOperatorInStart();
        if(applicableOperators.size() < 1){
            return null;
        }
        System.out.println("possible applicableOperators in start " + applicableOperators.size() );
        List<Operator> consistent = filterConsistentOperators(applicableOperators, sasFile);
        if(consistent.size() < 1){
            return null;
        }

        MultiReverseOperation reverse = new MultiReverseOperation("UseApplicableActionInStart-" + consistent.size());
        for (Operator operator : consistent) {
            UseApplicableActionInStartReverse atomicReverse = new UseApplicableActionInStartReverse(operator);
            sasFile.applyOnStart(operator);
            if (ActionStartMergeReverse.isOnlyOnce(operator, sasFile)) {
                operator.setActive(false);
            }
            reverse.addReverseOperation(atomicReverse);
        }
        return reverse;
    }

    private List<Operator> filterConsistentOperators(List<Operator> applicableOperators, SasFile sasFile) {
        List<Operator> list = new ArrayList<>();
        for (Operator operator : applicableOperators) {
            if(operator.isActive() && sasFile.isUsedOnlyOnce(operator) && !sasFile.isDestroyingAnyOtherAction(operator)){
                list.add(operator);
            }
        }
        return list;
    }


    @Override
    public void clearCache() {
    }

}
