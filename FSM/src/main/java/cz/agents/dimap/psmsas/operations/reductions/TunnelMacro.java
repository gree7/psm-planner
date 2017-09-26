package cz.agents.dimap.psmsas.operations.reductions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.agents.dimap.psmsas.operations.ReduceOperationLazyImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.SimpleTunnelMacroReverse;
import cz.agents.dimap.tools.Pair;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.Operator.Effect;
import cz.agents.dimap.tools.sas.Operator.Precondition;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;
import cz.agents.dimap.tools.sas.Variable.Value;

public class TunnelMacro extends ReduceOperationLazyImpl {

    private static final int MAX_VALUES = 2;
    
    private Set<Set<Value>> possibleTunnelValues;
    
    Map<Variable.Value, Set<Operator>> consumers = new HashMap<>();
    Map<Variable.Value, Set<Operator>> producers = new HashMap<>();

    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        prepareMaps(sasFile);

        return tunnelMultipleVariables(sasFile);
    }

    private void prepareMaps(SasFile sasFile) {
        consumers.clear();
        producers.clear();
        possibleTunnelValues = new HashSet<>();
        Set<Set<Value>> possibleOldValues = new HashSet<>();
        for (Operator operator : sasFile.operators.values()) {
            if (operator.isActive()) {
                fillInOperator(operator, consumers, producers);

                if (operator.hasNoActivePreconditions()) { 
                    Pair<Set<Value>, Set<Value>> pairValues = getEffectValues(operator);
                    Set<Value> oldValues = pairValues.getKey(); 
                    Set<Value> newValues = pairValues.getValue(); 

                    if (newValues.size() <= MAX_VALUES) {
                        possibleTunnelValues.add(newValues);
                    }
                    if (oldValues.size() <= MAX_VALUES) {
                        possibleOldValues.add(oldValues);
                    }
                }
            }
        }
        
        possibleTunnelValues.retainAll(possibleOldValues);
    }

    private void fillInOperator(Operator operator, Map<Variable.Value, Set<Operator>> consumers, Map<Variable.Value, Set<Operator>> producers) {
        for (Operator.Precondition precondition : operator.getPreconditions()) {
            if (precondition.isActive()) {
                addToMap(precondition.getValue().getUpMostParent(), operator, consumers);
            }
        }
        for (Operator.Effect effect : operator.getEffects()) {
            if (effect.isActive()) {
                addToMap(effect.getNewValue().getUpMostParent(), operator, producers);
                addToMap(effect.getOldValue().getUpMostParent(), operator, consumers);
            }
        }
    }

    private void addToMap(Variable.Value value, Operator operator, Map<Variable.Value, Set<Operator>> map) {
        Variable.Value upMost = value.getUpMostParent();
        if (!map.containsKey(upMost)) {
            map.put(upMost, new HashSet<Operator>());
        }
        map.get(upMost).add(operator);
    }

    private ReverseOperation tunnelMultipleVariables(SasFile sasFile) {
        nextOp:
        for (Set<Value> tunnelValues : possibleTunnelValues) {
            for (Value value : tunnelValues) {
                boolean inInit = value.getVariable().getInitValue().getUpMostParent() == value;
                boolean inGoal = value.getVariable().isInGoal() && (value.getVariable().getGoalValue().getUpMostParent() == value);
                if (inInit || inGoal) {
                    continue nextOp;
                }
            }

            Pair<Collection<Operator>, Collection<Operator>> preOpsPair = filterEffects(tunnelValues);
            Collection<Operator> preOps = preOpsPair.getKey();
            Collection<Operator> partialPreOps = preOpsPair.getValue();
            if (preOps.isEmpty()) {
                continue;
            }

            Collection<Operator> postOps = filterPreconditions(sasFile, tunnelValues, preOps, partialPreOps);
            if (postOps == null || postOps.isEmpty()) {
                continue;
            }

            ReverseOperation reverse = tunnelOps(sasFile, preOps, postOps, tunnelValues);
            if (reverse != null) {
                System.out.println("tunnelValues: " + tunnelValues);
                System.out.println("preOps: " + preOps);
                System.out.println("postOps: " + postOps);
                System.out.println("!!!: ");
                return reverse;
            }
        }
        return null;
    }

    private boolean isMergeable(SasFile sasFile, Collection<Operator> preOps, Operator postOp) {
        for (Operator preOp : preOps) {
            if (areMergeable(sasFile, preOp, postOp)) {
                return true;
            }
        }
        return false;
    }

    private boolean areMergeable(SasFile sasFile, Operator preOp, Operator postOp) {
        return merge(sasFile, preOp, postOp) != null;
    }

    private ReverseOperation tunnelOps(SasFile sasFile, Collection<Operator> preOps, Collection<Operator> postOps, Set<Value> tunnelValues) {
        int activeOperatorsBefore = sasFile.getNumberOfActiveOperators();

        MultiReverseOperation revOp = new MultiReverseOperation("TunnelMacro-"+tunnelValues);
        
        for (Operator preOp : preOps) {
            for (Operator postOp : postOps) {
                SimpleTunnelMacroReverse reverse = shrinkOperators(preOp, postOp, sasFile);
                revOp.addReverseOperation(reverse);
            }
        }

        int activeOperatorsAfter = sasFile.getNumberOfActiveOperators();

        if (!Settings.ALLOW_TRANSFORMATIONS && activeOperatorsAfter > activeOperatorsBefore) {
            for (ReverseOperation reverse : revOp.getReverseOperations()) {
                ((SimpleTunnelMacroReverse) reverse).undoChanges();
            }
            System.out.println("!!! reverting TunnelMacro: " + activeOperatorsBefore + " -> " + activeOperatorsAfter);
            return null;
        }
        
        
        if (!revOp.isEmpty()) {
            ReverseOperation reduce = new ValuesPruning().reduce(sasFile);
            if (reduce != null) {
                revOp.addReverseOperation(reduce);
            }
            return revOp;
        } else {
            return null;
        }
    }

    private Pair<Set<Value>, Set<Value>> getEffectValues(Operator operator) {
        Set<Value> oldValues = new HashSet<>();
        Set<Value> newValues = new HashSet<>();
        for (Effect effect : operator.getEffects()) {
            if (effect.isActive()) {
                oldValues.add(effect.getOldValue().getUpMostParent());
                newValues.add(effect.getNewValue().getUpMostParent());
            }
        }
        return new Pair<>(oldValues, newValues);
    }

    private Pair<Collection<Operator>, Collection<Operator>> filterEffects(Set<Value> values) {
        Collection<Operator> equalEffects = new LinkedList<>();
        Collection<Operator> partialOverlap = new LinkedList<>();
        
        Set<Operator> skipOperators = new HashSet<>();
        for (Value value : values) {
            for (Operator operator : producers.get(value)) {
                if (skipOperators.contains(operator)) {
                    continue;
                }
                skipOperators.add(operator);
                int curValues = 0;
                int curSize = 0;
                for (Effect effect : operator.getEffects()) {
                    if (effect.isActive()) {
                        Value newValue = effect.getNewValue().getUpMostParent();
                        if (!values.contains(newValue)) {
                            curValues++;
                        }
                        curSize++;
                    }
                }
                if (curValues == 0 && curSize == values.size()) {
                    // can have other effects
                    equalEffects.add(operator);
                } else if (curValues < curSize) {
                    // there is some intersection
                    partialOverlap.add(operator);
                }
            }
        }
        return new Pair<Collection<Operator>, Collection<Operator>>(equalEffects, partialOverlap);
    }

    private Collection<Operator> filterPreconditions(SasFile sasFile, Set<Value> values, Collection<Operator> preOps, Collection<Operator> partialPreOps) {
        Collection<Operator> equalEffects = new LinkedList<>();
        
        Set<Operator> skipOperators = new HashSet<>();

        for (Value val : values) {
            Set<Operator> consOps = consumers.get(val);
            if (consOps == null) {
                return null;
            }
            List<Operator> ops = new ArrayList<>(consOps);
            if (consumers.containsKey(val.getVariable().getAnyValue())) {
                ops.addAll(consumers.get(val.getVariable().getAnyValue()));
            }
            nextOp:
            for (Operator operator : ops) {
                if (skipOperators.contains(operator)) {
                    continue;
                }
                skipOperators.add(operator);
                for (Precondition precond : operator.getPreconditions()) {
                    if (precond.isActive()) {
                        for (Value value : values) {
                            if (precond.getValue().getUpMostParent() == value) {
                                if (isMergeable(sasFile, preOps, operator)) {
                                    // all partialOps have to be mutually mutexed with other ops
                                    return null;
                                } else {
                                    continue nextOp;
                                }
                            }
                        }
                    }
                }

                int contains = 0;
                for (Effect effect : operator.getEffects()) {
                    if (effect.isActive()) {
                        for (Value value : values) {
                            if (effect.getVariable() == value.getVariable()) {
                                if (effect.getOldValue().getUpMostParent().isAnyValue()) {
                                    return null;
                                }
                                if (effect.getOldValue().getUpMostParent() == value) {
                                    contains++;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (values.size() == contains && values.size() == operator.getNumberOfActiveEffects()) {
                    // cannot have other effects
                    if (isMergeable(sasFile, partialPreOps, operator)) {
                        // all partialOps have to be mutually mutexed with other ops
                        return null;
                    } else {
                        equalEffects.add(operator);
                    }
                } else if (contains > 0) {
                    // there is some intersection
                    if (isMergeable(sasFile, preOps, operator)) {
                        // all partialOps have to be mutually mutexed with other ops
                        return null;
                    }
                }
            }
        }
        return equalEffects;
    }

    private SimpleTunnelMacroReverse shrinkOperators(Operator first, Operator second, SasFile sasFile) {
        Operator merged = merge(sasFile, first, second);
        if (merged != null) {

            merged.setActive();
    
            sasFile.operators.put(merged.getName(), merged);
            sasFile.storeOperator(merged); // caching
        }

        first.setActive(false);
        second.setActive(false);

        SimpleTunnelMacroReverse reverse = new SimpleTunnelMacroReverse(merged, first, second);
        return reverse;
    }

    private Operator merge(SasFile sas, Operator first, Operator second) {
        String name = first.getName() + "-" + second.getName();
        Operator merged = new Operator(first, name);
        for (Operator.Precondition precondition : second.getPreconditions()) {
            if (precondition.isActive()) {
                if (!merged.containsActiveEffectProducing(precondition.getValue().getUpMostParent())) {
                    if (!merged.getPreconditions().contains(precondition)) {
                        merged.addPrecondition(precondition);
                    }
                }
            }
        }

        for (Effect effect : second.getEffects()) {
            if (effect.isActive()) {
                Effect preEffect = merged.getActiveEffectWithVariable(effect.getVariable());
                if (preEffect != null) {
                    if (preEffect.getNewValue().getUpMostParent().equals(effect.getOldValue().getUpMostParent())) {
                        if (preEffect.getOldValue().getUpMostParent().equals(effect.getNewValue().getUpMostParent())) {
                            merged.getEffects().remove(preEffect);
                            merged.addPrecondition(preEffect.getVariable(), preEffect.getOldValue().getUpMostParent());
                        } else {
                            preEffect.setNewValue(effect.getNewValue().getUpMostParent());
                        }
                    } else {
                        return null;
                    }
                } else {
                    merged.getEffects().add(effect);
                }
            }
        }
        
        if (merged.getNumberOfActiveEffects() == 0) {
            return null;
        }
        
        if (hasMutexedPreconditions(sas, merged)) {
            return null;
        }

        return merged;
    }

    private boolean hasMutexedPreconditions(SasFile sas, Operator op) {
        for (Precondition precondition : op.getPreconditions()) {
            if (precondition.isActive()) {
                for (Precondition otherPre : op.getPreconditions()) {
                    if (otherPre.isActive() && precondition != otherPre) {
                        if (sas.areMutexed(precondition.getValue().getUpMostParent(), otherPre.getValue().getUpMostParent())) {
                            return true;
                        }
                    }                    
                }

                for (Effect effect : op.getEffects()) {
                    if (effect.isActive()) {
                        if (sas.areMutexed(precondition.getValue().getUpMostParent(), effect.getOldValue().getUpMostParent())) {
                            return true;
                        }
                    }                    
                }
            }
            
        }
        for (Effect effect : op.getEffects()) {
            if (effect.isActive()) {
                for (Effect otherEff : op.getEffects()) {
                    if (otherEff.isActive() && effect != otherEff) {
                        if (sas.areMutexed(effect.getOldValue().getUpMostParent(), otherEff.getOldValue().getUpMostParent())) {
                            return true;
                        }
                    }                    
                }
            }
        }
        return false;
    }

    @Override
    public void clearCache() {
    }
}
