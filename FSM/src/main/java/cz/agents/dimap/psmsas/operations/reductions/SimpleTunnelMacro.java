package cz.agents.dimap.psmsas.operations.reductions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.SimpleTunnelMacroReverse;
import cz.agents.dimap.psmsas.operations.reverse.ValueResurrection;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.Operator.Effect;
import cz.agents.dimap.tools.sas.Operator.Precondition;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;
import cz.agents.dimap.tools.sas.Variable.Value;

public class SimpleTunnelMacro extends ReduceOperationImpl {

    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        for (Variable variable : sasFile.getVariables().values()) {
            if (variable.isActive()) {
                for (Value value : variable.getValues()) {
                    boolean notInGoal = !variable.isInGoal() || (variable.getGoalValue().getUpMostParent() != value);
                    if (value.isActive() && variable.getInitValue().getUpMostParent() != value && notInGoal) {
                        if (value.getValue().equals("<none of those>")) {
                            ReverseOperation reverse = tunnelValue(sasFile, value);
                            if (reverse != null) {
                                return reverse;
                            }
                        }
                    }
                }
            }
        }
        
        return null;
    }

    private ReverseOperation tunnelValue(SasFile sasFile, Value value) {
        List<Operator> preOps = filterEffects(sasFile.getOperators().values(), value);
        List<Operator> postOps = filterPreconditions(sasFile.getOperators().values(), value);
        
        if (postOps == null) {
            return null;
        }

        int activeOperatorsBefore = sasFile.getNumberOfActiveOperators();

        MultiReverseOperation revOp = new MultiReverseOperation("SimpleTunnelMacro-"+value.getVariable());
        
        for (Operator preOp : preOps) {
            for (Operator postOp : postOps) {
                SimpleTunnelMacroReverse reverse = shrinkOperators(preOp, postOp, sasFile);
                if (reverse != null) {
                    revOp.addReverseOperation(reverse);
                } else {
                    System.out.println("preOp: " + preOp);
                    System.out.println("postOp: " + postOp);
                }
            }
        }

        int activeOperatorsAfter = sasFile.getNumberOfActiveOperators();

        if (!Settings.ALLOW_TRANSFORMATIONS && activeOperatorsAfter > activeOperatorsBefore) {
            for (ReverseOperation reverse : revOp.getReverseOperations()) {
                ((SimpleTunnelMacroReverse) reverse).undoChanges();
            }
            System.out.println("!!! reverting SimpleTunnelMacro: " + activeOperatorsBefore + " -> " + activeOperatorsAfter);
            return null;
        }
        
        if (!revOp.isEmpty()) {
            value.setActive(false);
            revOp.addReverseOperation(sasFile.removeValueFromMutexes(value));
            revOp.addReverseOperation(new ValueResurrection(value));
            
            return revOp;
        } else {
            return null;
        }
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
                if (!merged.containsActiveEffectProducing(precondition.getValue())) {
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

    private List<Operator> filterEffects(Collection<Operator> operators, Variable.Value value) {
        List<Operator> list = new LinkedList<>();
        boolean added = false;
        for (Operator operator : operators) {
            added = false;
            if (operator.isActive()) {
                for (Operator.Effect effect : operator.getEffects()) {
                    if (effect.isActive()) {
                        if (added) break;
                        if (effect.getNewValue().getUpMostParent() == value) {
                            list.add(operator);
                            added = true;
                        }
                    }
                }
            }
        }
        return list;
    }

    private List<Operator> filterPreconditions(Collection<Operator> operators, Variable.Value value) {
        List<Operator> list = new LinkedList<>();
        boolean added = false;
        for (Operator operator : operators) {
            added = false;
            if (operator.isActive()) {
                for (Operator.Precondition precondition : operator.getPreconditions()) {
                    if (precondition.isActive() && precondition.getVariable() == value.getVariable()) {
                        if (added) break;
                        if (precondition.getValue().getUpMostParent() == value) {
                            list.add(operator);
                            added = true;
                        }
                    }
                }

                for (Operator.Effect effect : operator.getEffects()) {
                    if (effect.isActive() && effect.getVariable() == value.getVariable()) {
                        if (effect.getOldValue().getUpMostParent().isAnyValue()) {
                            return null;
                        }
                        if (operator.getNumberOfActiveEffects() > 2) {
                            return null;
                        }
                        if (added) break;
                        if (effect.getOldValue().getUpMostParent() == value) {
                            list.add(operator);
                            added = true;
                        }
                    }
                }
            }
        }
        return list;
    }


    @Override
    public void clearCache() {
    }
}
