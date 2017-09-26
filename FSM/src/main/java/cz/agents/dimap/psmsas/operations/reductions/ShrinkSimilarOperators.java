package cz.agents.dimap.psmsas.operations.reductions;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.ShrinkSimilarOperatorsReverse;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;

/**
 * Created by pah on 23.7.15.
 */
public class ShrinkSimilarOperators extends ReduceOperationImpl {
    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        if(Settings.DEBUG){
            System.out.println("reducing similar operators");
        }

        MultiReverseOperation reverseOp = new MultiReverseOperation("ShrinkSimilarOperators");
        
        Map<String, Set<Operator>> map = makeGroup(sasFile.getOperators().values());
        for (Set<Operator> bag : map.values()) {
            if(bag.size() > 1){
                Operator operator = bag.iterator().next();
                bag.remove(operator);
                for (Operator current : bag) {
                    current.setActive(false);
                }
                reverseOp.addReverseOperation(new ShrinkSimilarOperatorsReverse(operator,bag));
            }
        }
        if (reverseOp.isEmpty()) {
            return null;
        } else {
            return reverseOp;
        }
    }

    private Map<String, Set<Operator>> makeGroup(Collection<Operator> operators) {
        Map<String, Set<Operator>> cached = new HashMap<>();
        for (Operator operator : operators) {
            if (operator.isActive()) {
                String canonic = operator.getCurrentCanonicalString();
                if(!cached.containsKey(canonic)){
                    cached.put(canonic, new HashSet<Operator>());
                }
                cached.get(canonic).add(operator);
            }
        }
        return cached;
    }

    @Override
    public void clearCache() {

    }
}
