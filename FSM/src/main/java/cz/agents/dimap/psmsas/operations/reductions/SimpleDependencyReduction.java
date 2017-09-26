package cz.agents.dimap.psmsas.operations.reductions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.agents.dimap.psmsas.operations.ReduceOperationImpl;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.SimpleDependencyReductionReverse;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

/**
 * Created by pah on 22.7.15.
 * Simple action dependency reduction.
 */
public class SimpleDependencyReduction extends ReduceOperationImpl {

    private Iterator<Variable> iterator;

    @Override
    public ReverseOperation reduce(SasFile sasFile) {
        setIterator(sasFile.getVariables());
        while (iterator.hasNext()) {
            Variable variable = iterator.next();
            if (variable.isActive()) {
                for (Variable.Value value : variable.getValues()) {
                    boolean notInGoal = !variable.isInGoal() || (variable.getGoalValue().getUpMostParent() != value);
                    if (value.isActive() && variable.getInitValue().getUpMostParent() != value && notInGoal) {
                        List<Operator> eff = filterEffects(sasFile.getOperators().values(), value); // causal link
                        List<Operator> pre = filterPreconditions(sasFile.getOperators().values(), value);

                        if (1 == pre.size() && 1 == eff.size()) {
                            Operator consumer = pre.get(0);
                            Operator producer = eff.get(0);
                            if (1 < consumer.getNumberOfActiveEffectsWithoutAnyValues() || 1 != producer.getNumberOfActiveEffects() || areOpposite(eff.get(0), pre.get(0)))
                                continue;

                            // nemuze to mergovat akci pomoci hodnoty ktera je pouze v prevail PRE, protoze potom by to ten operator byl nekonzistentni, aspon jak je to tam udelane ted, protoze by defacto doslo ke slouceni hodnot

                            // if producer.needs \subseteq consumer.needs -> ok, else continue
                            // if consumer changes variable that producer needs -> continue, else ok
                            if (!canProcessAfterSubsetCheck(value, producer, consumer)) continue;

                            if (Settings.DEBUG) {
                                System.out.println("found");
                                System.out.println("\t" + producer.getCurrentCanonicalString());
                                System.out.println("\t" + consumer.getCurrentCanonicalString());
                            }
                            return shrinkOperators(producer, consumer, sasFile);
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean canProcessAfterSubsetCheck(Variable.Value value, Operator producer, Operator consumer) {

        HashSet<Variable> variableSideEffects = new HashSet<>();
        HashSet<Variable.Value> values = new HashSet<>();
        boolean containsEffect = false;
        for (Operator.Effect effect : consumer.getEffects()) {
            if(effect.isActive() && effect.getVariable() == value.getVariable() && !effect.getOldValue().getUpMostParent().isAnyValue()){
                containsEffect = true;
                break;
            }
        }
        // because the effect in consumer is on different variable
        if(!containsEffect) return false;


        fillConsumer(consumer, variableSideEffects, values, value);

        for (Operator.Precondition precondition : producer.getPreconditions()) {
            if(precondition.isActive()){
                // because consumer changes the variable from -1, but producer has the variable as prevail precondition
                if(variableSideEffects.contains(precondition.getVariable())) return false;
                // because con->pro are not atomic operations
                if(!values.contains(precondition.getValue())) return false;
            }
        }
        return true;
    }

    private void fillConsumer(Operator consumer, HashSet<Variable> variableSideEffects, HashSet<Variable.Value> values, Variable.Value value) {
        for (Operator.Precondition precondition : consumer.getPreconditions()) {
            if (precondition.isActive()) {
                values.add(precondition.getValue().getUpMostParent());
            }
        }

        for (Operator.Effect effect : consumer.getEffects()) {
            if (effect.isActive() && value.getVariable() != effect.getVariable()) {
                variableSideEffects.add(effect.getVariable());
            }
        }
    }

    private boolean areOpposite(Operator first, Operator second) {
        Operator.Effect causal = findEffect(first);
        for (Operator.Effect effect : second.getEffects()) {
            if (effect.isActive() && effect.getVariable() == causal.getVariable()) {
                return effect.isOpposite(causal);
            }
        }
        return false;
    }

    private SimpleDependencyReductionReverse shrinkOperators(Operator first, Operator second, SasFile sasFile) {
        SimpleDependencyReductionReverse reverse = new SimpleDependencyReductionReverse(first, second);
        Operator.Effect firstEffect = findEffect(first);
        updateEffects(second, firstEffect, reverse);
        updatePreconditions(second, first, reverse);
        second.setActive(false);
        reverse.addToTurnOn(second);

        sasFile.storeOperator(first); // caching
        return reverse;
    }

    private void updatePreconditions(Operator operator, Operator first, SimpleDependencyReductionReverse reverse) {
        for (Operator.Precondition precondition : operator.getPreconditions()) {
            if (precondition.isActive()) {
                reverse.addPrecondition(precondition, first);
            }
        }
    }

    private Operator.Effect findEffect(Operator first) {
        for (Operator.Effect effect : first.getEffects()) {
            if (effect.isActive()) {
                return effect;
            }
        }
        return null;
    }

    private void updateEffects(Operator operator, Operator.Effect firstEffect, SimpleDependencyReductionReverse reverse) {
        for (Operator.Effect effect : operator.getEffects()) {
            if (effect.isActive()) {
                reverse.addEffect(effect, firstEffect);
            }
        }
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
        iterator = null;
    }

    public void setIterator(Map<Integer, Variable> variables) {
        if (null == iterator) {
            iterator = variables.values().iterator();
        }
    }
}
