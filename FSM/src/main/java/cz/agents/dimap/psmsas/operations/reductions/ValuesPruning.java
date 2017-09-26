package cz.agents.dimap.psmsas.operations.reductions;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.ValueResurrection;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

/**
 * Created by pah on 17.7.15.
 * <p/>
 * Another implementation of PruneUnusedValues. The main idea is the same, so it should give same behaviour as PruneUnusedValues.
 * The main difference is that this class returns all possible prunes in one object instead of returning on pruning action at a time as PruneUnusedValues.
 * <p/>
 * TODO revise this class becase of bad behavior while using before -dv; it unactivizes things differently.
 */
public class ValuesPruning extends ReduceOperationImpl {
    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        if(Settings.DEBUG){
            System.out.println("ValuesPrunning reduction");
        }
        States states = new States(sasFile.getVariables().values());
        ReverseOperation reserve = prune(sasFile, states, sasFile.getOperators().values(), sasFile.getVariables());
        return reserve;
    }

    private ReverseOperation prune(SasFile sasFile, States states, Collection<Operator> operators, Map<Integer, Variable> variables) {
        ValueResurrection reserve = new ValueResurrection();
        Collection<Operator> current = new LinkedList<>();
        for (Operator operator : operators) {
            if (operator.isActive()) {
                current.add(operator);
            }
        }
        Collection<Operator> unreachable = runBFS(states, current);
        appendUnreachableOperators(unreachable, reserve);
        MultiReverseOperation multiReverse = appendUnreachableValues(sasFile, states, variables, reserve);
        if (Settings.DEBUG) System.out.println("unreachable " + reserve.getChanges().size());
        
        if (reserve.getChanges().size() > 0) {
            multiReverse.addReverseOperation(reserve);
        }
        
        multiReverse.addReverseOperation(new VariableDeletion().reduce(sasFile));

        if (multiReverse.isEmpty()) {
            return null;
        } else {
            return multiReverse;
        }
    }

    private MultiReverseOperation appendUnreachableValues(SasFile sasFile, States states, Map<Integer, Variable> variables, ValueResurrection reserve) {
        MultiReverseOperation multiReverse = new MultiReverseOperation("Values Pruning");
        for (Variable variable : variables.values()) {
            if (variable.isActive()) {
                for (Variable.Value value : variable.getValues()) {
                    if (value.isActive() && !value.getUpMostParent().isAnyValue() && !states.get(value)) {
                        reserve.addToActivize(value);
                        value.setActive(false);
                        multiReverse.addReverseOperation(sasFile.removeValueFromMutexes(value));
                        /*// little bit awkward
                        if (variable.getNumberOfActiveValues() <= 1) {
                            variable.setActive(false);
                            reserve.addToSetOn(variable);
                        }*/
                    }
                }
            }
        }
        return multiReverse;
    }

    private void appendUnreachableOperators(Collection<Operator> unreachable, ValueResurrection reserve) {
        for (Operator operator : unreachable) {
            operator.setActive(false);
            reserve.addToActivize(operator);
        }
    }

    private Collection<Operator> runBFS(States states, Collection<Operator> current) {
        Collection<Operator> nextQueue = new LinkedList<>();
        boolean changed = true;

        if (Settings.DEBUG){ states.printSnapshot();}
        while (changed) {
            changed = false;
            for (Operator operator : current) {
                if (canApply(states, operator)) {
                    apply(states, operator);
                    changed = true;
                } else {
                    nextQueue.add(operator);
                }
            }
            current = nextQueue;
            nextQueue = new LinkedList<>();
        }

        if (Settings.DEBUG) states.printSnapshot();
        return current;
    }

    private boolean canApply(States states, Operator operator) {
        if(!operator.isActive()) return false;
        for (Operator.Precondition precondition : operator.getPreconditions()) {
            Variable.Value upmost = precondition.getValue().getUpMostParent();
            if (precondition.isActive() && upmost.isActive() && !upmost.isAnyValue() && !states.get(upmost)) {
                return false;
            }
        }

        for (Operator.Effect effect : operator.getEffects()) {
            if (effect.isActive() && !effect.getOldValue().getUpMostParent().isAnyValue() && !states.get(effect.getOldValue().getUpMostParent())) {
                return false;
            }
        }
        return true;
    }

    private void apply(States states, Operator operator) {
        for (Operator.Effect effect : operator.getEffects()) {
            if (effect.isActive()) {
                states.setReachable(effect);
            }
        }
    }

    @Override
    public void clearCache() {

    }


    private class States {
        private Map<Variable, Map<Integer, Boolean>> state = new LinkedHashMap<>();

        public States(Collection<Variable> variables) {
            for (Variable variable : variables) {
                if (variable.isActive()) {
                    state.put(variable, generateValues(variable));
                }
            }

        }

        private Map<Integer, Boolean> generateValues(Variable variable) {
            Map<Integer, Boolean> map = new HashMap<>();
            for (Variable.Value value : variable.getValues()) {
                if (value.isActive()) {
                    map.put(value.getOriginalID(), false);
                }
            }
            map.put(variable.getInitValue().getUpMostParent().getOriginalID(), true);
            return map;
        }

        private boolean get(Variable.Value value) {
            return state.get(value.getVariable()).get(value.getOriginalID());
        }

        public void setReachable(Operator.Effect effect) {
            state.get(effect.getVariable()).put(effect.getNewValue().getUpMostParent().getOriginalID(), true);
        }

        public void printSnapshot() {
            for (Map.Entry<Variable, Map<Integer, Boolean>> entry : state.entrySet()) {
                System.out.print(entry.getKey().getOriginalId() + ":\t");
                for (Map.Entry<Integer, Boolean> innerEntry : entry.getValue().entrySet()) {
                    if (innerEntry.getValue()) {
                        System.out.print("\t'" + innerEntry.getKey());
                    } else {
                        System.out.print("\t" + innerEntry.getKey());
                    }
                }
                System.out.println("");
            }
            System.out.println("");
        }
    }
}
