package cz.agents.dimap.psmsas.operations.reductions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.GeneralizedActionReverse;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

/**
 * Created by pah on 25.8.15.
 */
public class GeneralizedAction extends ReduceOperationImpl {

    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        if (Settings.DEBUG) {
            System.out.println("generalized action reduction");
        }

        MultiReverseOperation multiReverse = new MultiReverseOperation("GeneralizedAction");
        for (Variable variable : sasFile.getVariables().values()) {
            if (variable.isActive()) {
                Map<String, Map<Variable.Value, Set<Operator>>> map = makeProjection(variable, sasFile);
                ReverseOperation reverse = checkAndMakeReduction(map, variable, sasFile);
                if (null != reverse) {
                    multiReverse.addReverseOperation(reverse);
                }
            }
        }
        if (multiReverse.isEmpty()) {
            return null;
        }
        return multiReverse;
    }

    private ReverseOperation checkAndMakeReduction(Map<String, Map<Variable.Value, Set<Operator>>> map, Variable variable, SasFile sasFile) {
        MultiReverseOperation multiReverse = new MultiReverseOperation("GeneralizedAction on variable projection " + variable.getOriginalId());
        for (Map.Entry<String, Map<Variable.Value, Set<Operator>>> entry : map.entrySet()) {
            if (!isReductionPossible(entry, variable)) {
                continue;
            }
            if (sasFile.containsOperator(entry.getKey())) {
                // case when there exists action that is same as projection of actions, therefore we may use the first actions and forget the actions before projection
                multiReverse.addReverseOperation(processRedundantOperatorReduction(variable, entry));
            } else {
                multiReverse.addReverseOperation(processDecompileReduction(variable, entry));
            }
        }
        if (multiReverse.isEmpty()) {
            return null;
        }
        return multiReverse;
    }

    private ReverseOperation processDecompileReduction(Variable variable, Map.Entry<String, Map<Variable.Value, Set<Operator>>> entry) {
        GeneralizedActionReverse reverse = new GeneralizedActionReverse(variable);
        for (Map.Entry<Variable.Value, Set<Operator>> currentEntry : entry.getValue().entrySet()) {
            for (Operator operator : currentEntry.getValue()) {
                reverse.setOperator(operator);
                operator.setActive(false);
                reverse.addToTurnOn(currentEntry.getKey(), operator);
            }
        }
        Operator operator = reverse.getOperator();
        operator.setActive(true);
        Operator.Precondition precondition = operator.getActivePreconditionWithVariable(variable);
        precondition.setActive(false);
        reverse.addToTurnOn(precondition);
        return reverse;
    }

    private ReverseOperation processRedundantOperatorReduction(Variable variable, Map.Entry<String, Map<Variable.Value, Set<Operator>>> entry) {
        GeneralizedActionReverse reverse = new GeneralizedActionReverse(variable);
        for (Set<Operator> operators : entry.getValue().values()) {
            for (Operator operator : operators) {
                operator.setActive(false);
                reverse.addToTurnOn(operator);
            }
        }
        return reverse;
    }

    private boolean isReductionPossible(Map.Entry<String, Map<Variable.Value, Set<Operator>>> entry, Variable variable) {
        int size = variable.getNumberOfActiveValues();
        return entry.getValue().keySet().size() == size;
    }

    private Map<String, Map<Variable.Value, Set<Operator>>> makeProjection(Variable variable, SasFile sasFile) {
        Map<String, Map<Variable.Value, Set<Operator>>> map = new HashMap<>();
        if (!sasFile.getVariableOperators().containsKey(variable)) {
            return map;
        }
        for (Operator operator : sasFile.getVariableOperators().get(variable)) {
            if (operator.isActive()) {
                Operator.Precondition precondition = operator.getActivePreconditionWithVariable(variable);
                if (null != precondition) {
                    precondition.setActive(false);
                    Variable.Value value = precondition.getValue().getCachedUpMostParent();
                    addProjection(operator, value, map);
                    precondition.setActive();
                }
            }
        }
        return map;
    }

    private void addProjection(Operator operator, Variable.Value value, Map<String, Map<Variable.Value, Set<Operator>>> map) {
        String canonic = operator.getCurrentCanonicalString();
        if (!map.containsKey(canonic)) {
            map.put(canonic, new HashMap<Variable.Value, Set<Operator>>());
        }
        if (!map.get(canonic).containsKey(value.getCachedUpMostParent())) {
            map.get(canonic).put(value.getCachedUpMostParent(), new HashSet<Operator>());
        }
        map.get(canonic).get(value.getCachedUpMostParent()).add(operator);
    }

    @Override
    public void clearCache() {

    }
}
