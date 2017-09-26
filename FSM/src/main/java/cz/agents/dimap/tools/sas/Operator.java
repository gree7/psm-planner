package cz.agents.dimap.tools.sas;

import cz.agents.dimap.psmsas.operations.reductions.DeadEnds;
import cz.agents.dimap.psmsas.operations.reverse.DeadEndsReverse;
import cz.agents.dimap.tools.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by pah on 8.7.15.
 * <p/>
 * Class representing operator of SAS.
 */
public class Operator implements Active {
    private final String name;
    private final List<Precondition> preconditions;
    private final List<Effect> effects;
    private long cost;
    private boolean active = true;

    public Operator(String name, List<Precondition> preconditions, List<Effect> effects) {
        this.name = name;
        this.preconditions = preconditions;
        this.effects = effects;
        this.cost = 1;
    }
    
    public Operator(Operator other, String name) {
        this.name = name;
        preconditions = new ArrayList<>();
        for (Precondition precondition : other.preconditions) {
            if (precondition.isActive()) {
                preconditions.add(new Precondition(precondition));
            }
        }
        effects = new ArrayList<>();
        for (Effect effect : other.effects) {
            if (effect.isActive()) {
                effects.add(new Effect(effect));
            }
        }
        cost = other.cost;
        active = other.active;
    }
    
    public Operator(String name, BufferedReader br, Map<Integer, Variable> variables) throws IOException {
        this.name = name;
        preconditions = new ArrayList<>();
        effects = new ArrayList<>();

        long num = Long.parseLong(br.readLine());
        String[] splitted;
        for (int i = 0; i < num; i++) {
            splitted = br.readLine().split(" ");
            if (2 != splitted.length) {
                throw new IllegalStateException("Preconditions should look like 'variable-number-id value'");
            }
            addPrecondition(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]), variables);
        }

        num = Long.parseLong(br.readLine());
        String line;
        for (int i = 0; i < num; i++) {
            line = br.readLine();
            splitted = line.split(" ");
            if (splitted.length > 4 || 0 != Long.parseLong(splitted[0])) {
                // if one would implement starting with others than 0, so he has to implement also output parsing - inside method SasFile.appendOperator
                throw new IllegalStateException("Probably some effect, we do not know how to parse: " + line);
            } else if (4 != splitted.length) {
                throw new IllegalStateException("Effect should look like 'variable-number-id oldValue newValue'");
            }
            addEffectStartingWithZero(Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]), Integer.parseInt(splitted[3]), variables);
        }
        cost = Long.parseLong(br.readLine());
    }

    public String getName() {
        return name;
    }

    public List<Precondition> getPreconditions() {
        return preconditions;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive() {
        active = true;
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
    }

    public void addEffectStartingWithZero(Variable variable, Variable.Value oldValue, Variable.Value newValue) {
        addEffectStartingWithZero(new Effect(variable, oldValue, newValue));
    }


    public Effect addEffectStartingWithZero(Variable variable, Variable.Value newValue) {
        Effect effect = new Effect(variable, newValue);
        addEffectStartingWithZero(effect);
        return effect;
    }

    private void addEffectStartingWithZero(Effect effect) {
        effects.add(effect);
    }


    private void addEffectStartingWithZero(int variableID, int oldValueID, int newValueID, Map<Integer, Variable> variables) {
        Variable variable = variables.get(variableID);
        if (-1 == oldValueID) {
            addEffectStartingWithZero(variable, variable.getValueOriginal(newValueID));
        } else {
            addEffectStartingWithZero(variable, variable.getValueOriginal(oldValueID), variable.getValueOriginal(newValueID));
        }
    }


    public Precondition addPrecondition(Integer variableID, Integer valueID, Map<Integer, Variable> variables) {
        return addPrecondition(variables.get(variableID), variables.get(variableID).getValueOriginal(valueID));
    }

    public Precondition addPrecondition(Variable variable, Variable.Value value) {
        return addPrecondition(new Precondition(variable, value));
    }

    public Precondition addPrecondition(Precondition precondition) {
        preconditions.add(precondition);
        return precondition;
    }


    public int getNumberOfActivePreconditions() {
        // TODO remake this to cached value
        int num = 0;
        for (Precondition precondition : preconditions) {
            if (precondition.isActive()) {
                num++;
            }
        }
        return num;
    }

    public int getNumberOfActiveEffects() {
        // TODO remake this to cached value
        int num = 0;
        for (Effect effect : effects) {
            if (effect.isActive()) {
                num++;
            }
        }
        return num;
    }

    public String getSAS() {
        StringBuilder sb = new StringBuilder();
        appendSAS(sb, false);
        return sb.toString();
    }

    /**
     * Appends operator in SAS.
     * If the debug argument is set to true, then instead of active preconditions XXX is appended.
     *
     * @param sb
     * @param debug
     */
    public void appendSAS(StringBuilder sb, boolean debug) {
        sb.append("begin_operator\n").append(name).append("\n");
        sb.append(getNumberOfActivePreconditions()).append("\n");
        for (Precondition precondition : preconditions) {
            if (precondition.isActive()) {
                sb.append(precondition.getSAS());
            } else if (debug) {
                sb.append("XXX\n");
            }
        }

        sb.append(getNumberOfActiveEffects()).append("\n");
        for (Effect effect : effects) {
            if (effect.isActive()) {
                sb.append(effect.getSAS());
            } else if (debug) {
                sb.append("XXX\n");
            }
        }

        sb.append(cost).append("\n");
        sb.append("end_operator\n");
    }

    public Effect getFirstActiveEffect() {
        for (int i = 0; i < effects.size(); i++) {
            Effect effect = effects.get(i);
            if (effect.isActive()) {
                return effect;
            }
        }
        return null;
    }

    public boolean hasNoActiveEffects() {
        return 0 == getNumberOfActiveEffects();
    }

    public boolean hasNoActivePreconditions() {
        return 0 == getNumberOfActivePreconditions();
    }

    /**
     * Changes activity to false if there are zero active effect and zero active preconditions.
     * Returns true if change was performed; otherwise false.
     *
     * @return
     */
    public boolean actualize() {
        if (hasNoActiveEffects()) {
            setActive(false);
            return true;
        }
        return false;
    }



    /*public boolean actualize() {
        boolean change = actualizePreconditions();
        change |= actualizeEffects();
        return change;
    }*/

    /*private boolean actualizeEffects() {
        Set<String> alreadyIn = new HashSet<>();
        boolean change = false;
        for (Effect effect : effects) {
            if (effect.isActive()) {
                effect.actualize();
                if (effect.isActive()) {
                    if (alreadyIn.contains(effect.getCanonicString())) {
                        effect.setActive(false);
                        change = true;
                    } else {
                        alreadyIn.add(effect.getCanonicString());
                    }
                }
            }
        }
        return change;
    }*/

    /*private boolean actualizePreconditions() {
        boolean change = false;
        for (Precondition precondition : preconditions) {
            if (precondition.isActive()) {
                change |= precondition.checkActive();
            }
        }
        return change;
    }*/

    public String getCanonicalString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Collections.sort(preconditions);
        for (Precondition precondition : preconditions) {
            if (precondition.isActive()) {
                sb.append(precondition.getCanonic());
            }
        }

        sb.append("][");
        Collections.sort(effects);
        for (Effect effect : effects) {
            if (effect.isActive()) {
                sb.append(effect.getCanonic());
            }
        }

        sb.append("]");
        return sb.toString();
    }

    /**
     * Returns true if both operators has the same preconditions given by realID and same effects given by realID.
     *
     * @param operator
     * @return
     */
    public boolean areSimilarlyEqual(Operator operator) {
        return getCanonicalString().equals(operator.getCanonicalString());
    }

    public boolean containsSimilarEffect(Effect effect) {
        long canonic = effect.getCanonic();
        for (Effect current : effects) {
            if (canonic == current.getCanonic()) {
                return false;
            }
        }
        return false;
    }

    public int getNumberOfActiveEffectsWithoutAnyValues() {
        // TODO remake this to cached value
        int num = 0;
        for (Effect effect : effects) {
            if (effect.isActive() && !effect.getOldValue().getUpMostParent().isAnyValue()) {
                num++;
            }
        }
        return num;
    }

    public boolean contains(Variable.Value value) {
        for (Precondition precondition : preconditions) {
            if (precondition.isActive()) {
                if (precondition.getValue().getUpMostParent() == value) {
                    return true;
                }
            }
        }

        for (Effect effect : effects) {
            if (effect.isActive()) {
                if (effect.getOldValue().getUpMostParent() == value || effect.getNewValue().getUpMostParent() == value) {
                    return true;
                }
            }
        }

        return false;
    }

    public String getCurrentCanonicalString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Collections.sort(preconditions);
        for (Precondition precondition : preconditions) {
            if (precondition.isActive()){// && precondition.getVariable().isActive() && precondition.getValue().getUpMostParent().isActive()) { tahle kontrola by mela byt redundantni
                sb.append(precondition.getInputCanonicString());
            }
        }

        sb.append("][");
        Collections.sort(effects);
        for (Effect effect : effects) {
            if (effect.isActive()) {
                sb.append(effect.getInputCanonicString());
            }
        }
        sb.append("][");
        for (Effect effect : effects) {
            if (effect.isActive()) {
                sb.append(effect.getOutputCanonicString());
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public void update(Map<Variable.Value, Set<Operator>> map, Collection<Operator> operators, DeadEndsReverse reverse) {
        Collection<Variable.Value> changed = unactivizeEffectsFromDeadEnds(map, operators, reverse);
        appendPreconditionFromDeadEnds(changed, reverse);
    }

    private void appendPreconditionFromDeadEnds(Collection<Variable.Value> changed, DeadEndsReverse reverse) {
        for (Variable.Value value : changed) {
            boolean contains = false;
            for (Precondition precondition : preconditions) {
                if (precondition.isActive()) {
                    if (precondition.getVariable() == value.getVariable()) {
                        contains = true;
                        if (precondition.getValue().getUpMostParent() != value) {
                            throw new IllegalStateException("Probably inconsistency in prevail and unconditional preconditions of an effect. Thus this operator cannot be used.");
                        }
                    }
                }
            }

            if (!contains) {
                Precondition addedPrecondition = addPrecondition(value.getVariable(), value);
                reverse.addToTurnOff(addedPrecondition);
            }
        }
    }

    private Collection<Variable.Value> unactivizeEffectsFromDeadEnds(Map<Variable.Value, Set<Operator>> map, Collection<Operator> operators, DeadEndsReverse reverse) {
        Collection<Variable.Value> set = new HashSet<>();
        boolean maxOneEffect = getNumberOfActiveEffects() < 2;
        for (Effect effect : effects) {
            Variable.Value value = effect.getNewValue().getUpMostParent();
            boolean inGoal = value.getVariable().isInGoal() && value.getVariable().getGoalValue().getUpMostParent() == value;
            boolean inInit = value.getVariable().getInitValue().getUpMostParent() == value;

            boolean check = (maxOneEffect && map.containsKey(value) && map.get(value).size() < 1 && !inGoal && !inInit) || !value.isActive();
            if (effect.isActive() && !value.isActive() && value.getVariable().isInGoal() && !DeadEnds.hasActiveAnyValue(value.getVariable(), operators)) {
                setActive(false);
                reverse.addToSetOn(this);
                if (Settings.DEBUG) {
                    System.out.println("prune this operator\t" + getName());
                }
            } else if (effect.isActive() && check) {
                effect.setActive(false);
                reverse.addToSetOn(effect);
                if (!effect.getOldValue().getUpMostParent().isAnyValue()) {
                    set.add(effect.getOldValue().getUpMostParent());
                }
            }
        }
        return set;
    }

    public boolean containsActiveEffectProducing(Variable.Value value) {
        for (Effect effect : effects) {
            if (effect.isActive()) {
                if (effect.getNewValue().getUpMostParent() == value) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean usesAnyValue(Variable variable) {
        for (Effect effect : effects) {
            if (effect.isActive() && effect.getOldValue().getVariable() == variable && effect.getOldValue().getUpMostParent().isAnyValue()) {
                return true;
            }
        }
        return false;
    }

    public boolean isProducingValue(Variable.Value value) {
        for (Effect effect : effects) {
            if (effect.isActive() && effect.getVariable() == value.getVariable() && effect.getNewValue().getCachedUpMostParent() == value.getCachedUpMostParent()) {
                return true;
            }
        }
        return false;
    }

    public boolean isConsumingValue(Variable.Value value) {
        for (Effect effect : effects) {
            if (effect.isActive() && effect.getVariable() == value.getVariable() && (effect.getOldValue().getUpMostParent().isAnyValue() || effect.getOldValue().getCachedUpMostParent() == value.getCachedUpMostParent())) {
                return true;
            }
        }
        return false;
    }

    // assumes that the operator is feasible, meaning that it has only one effect affecting the given variable
    public Effect getActiveEffectWithVariable(Variable variable){
        for (Effect effect : getEffects()) {
            if(effect.isActive() && effect.getVariable() == variable){
                return effect;
            }
        }
        return null;
    }

    public boolean isNeedingValue(Variable.Value value) {
        for (Precondition precondition : preconditions) {
            if(precondition.isActive() && precondition.getVariable() == value.getVariable() && precondition.getValue().getCachedUpMostParent() == value.getCachedUpMostParent()){
                return true;
            }
        }
        for (Effect effect : effects) {
            if (effect.isActive() && effect.getVariable() == value.getVariable() && (effect.getOldValue().getUpMostParent().isAnyValue() || effect.getOldValue().getCachedUpMostParent() == value.getCachedUpMostParent())) {
                return true;
            }
        }
        return false;
    }

    public Precondition getActivePreconditionWithVariable(Variable variable) {
        for (Precondition precondition : preconditions) {
            if(precondition.isActive() && variable == precondition.getVariable()){
                return precondition;
            }
        }
        return null;
    }


    public class Precondition implements Active, Comparable {
        private final Variable variable;
        private Variable.Value value;
        private boolean active = true;

        public Precondition(Precondition precondition) {
            this(precondition.variable, precondition.value);
        }
        
        public Precondition(Variable variable, Variable.Value value) {
            this.variable = variable;
            this.value = value;
        }

        public Variable getVariable() {
            return variable;
        }

        public Variable.Value getValue() {
            return value;
        }

        public void setValue(Variable.Value value) {
            this.value = value;
        }

        public boolean isActive() {
            return active && variable.isActive();
        }

        public void setActive(boolean active) {
            this.active = active;
        }


        @Override
        public String toString() {
            return "Precondition{" +
                    "variable=" + variable +
                    ", value=" + value +
                    ", active=" + active +
                    '}';
        }

        public String getSAS() {
            return variable.getRealId() + " " + value.getParentRealID() + "\n";
        }

        public String getCanonic() {
            return "[" + variable.getRealId() + "," + value.getParentRealID() + "]";
        }

        @Override
        public int compareTo(Object o) {
            if (null == o) {
                return -1;
            }
            if (o.getClass() != getClass()) {
                return -1;
            }
            Precondition precondition = (Precondition) o;
            if (variable.getRealId() < precondition.getVariable().getRealId()) {
                return -1;
            } else if (precondition.getVariable().getRealId() < variable.getRealId()) {
                return 1;
            }
            if (value.getParentRealID() < precondition.getValue().getParentRealID()) {
                return -1;
            } else if (precondition.getValue().getParentRealID() < value.getParentRealID()) {
                return 1;
            }
            return 0;
        }

        @Override
        public void setActive() {
            active = true;
        }

        public String getInputCanonicString() {
            return variable.getOriginalId() + ":" + value.getUpMostParent().getOriginalID() + ",";
        }

    }

    public class Effect implements Active, Comparable {
        private final Variable variable;
        private Variable.Value oldValue;
        private Variable.Value newValue;
        private boolean active = true;
        private long canonic = -1;
        private Variable.Value oldValueParent;
        private Variable.Value newValueParent;

        public Effect(Effect effect) {
            this.variable = effect.variable;
            this.oldValue = effect.oldValue;
            this.newValue = effect.newValue;
        }

        public Effect(Variable variable, Variable.Value oldValue, Variable.Value newValue) {
            this.variable = variable;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public Effect(Variable variable, Variable.Value newValue) {
            this.variable = variable;
            this.newValue = newValue;
            this.oldValue = variable.getAnyValue();
        }

        public Variable getVariable() {
            return this.variable;
        }

        public Variable.Value getOldValue() {
            return oldValue;
        }

        public void setOldValue(Variable.Value oldValue) {
            this.oldValue = oldValue;
        }

        public Variable.Value getNewValue() {
            return newValue;
        }

        public void setNewValue(Variable.Value newValue) {
            this.newValue = newValue;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }


        public boolean areOldNewValuesEqual() {
            return oldValue.getUpMostParent() == newValue.getUpMostParent();
        }

        @Override
        public String toString() {
            return "Effect{" +
                    "variable=" + variable +
                    ", oldValue=" + oldValue.getUpMostParent() +
                    ", newValue=" + newValue.getUpMostParent() +
                    '}';
        }

        public String getSAS() {
            return "0 " + variable.getRealId() + " " + oldValue.getParentRealID() + " " + newValue.getParentRealID() + "\n";
        }

        /**
         * Returns true only if given effect do precisely opposite thing than this effect; otherwise returns false.
         *
         * @param effect
         * @return
         */
        public boolean isOpposite(Effect effect) {
            if(null == effect){
                return false;
            }
            if (effect.getVariable().getRealId() != this.getVariable().getRealId()) {
                return false;
            }

            if (isEqual(effect)) {
                return false;
            }


            if (oldValue.getUpMostParent().isAnyValue() || effect.getOldValue().getUpMostParent().isAnyValue()) {
                return solveAnyValue(effect);
            }
            if ((effect.getNewValue().getParentRealID() != getOldValue().getParentRealID()) ||
                    (effect.getOldValue().getParentRealID() != getNewValue().getParentRealID())) {
                return false;
            }
            return true;
        }

        private boolean isEqual(Effect effect) {
            if (variable == effect.getVariable() &&
                    oldValue.getUpMostParent() == effect.getOldValue().getUpMostParent() &&
                    newValue.getUpMostParent() == effect.getNewValue().getUpMostParent()) {
                return true;
            }
            return false;
        }

        private boolean solveAnyValue(Effect effect) {
            if (effect.getOldValue().getUpMostParent().isAnyValue() && effect.getOldValue().getUpMostParent().isAnyValue()) {
                return true;
            }
            return oldValue.getUpMostParent().isAnyValue() ? isMergeable(effect) : effect.isMergeable(this);
        }

        private boolean isMergeable(Effect effect) {
            if (!oldValue.getUpMostParent().isAnyValue()) {
                return false;
            }
            return effect.getOldValue().getUpMostParent() == getNewValue().getUpMostParent();
        }

        /**
         * Sets active to false if effect's old value and new value are equal.
         */
        public void actualize() {
            if (oldValue.getParentRealID() == newValue.getParentRealID()) {
                setActive(false);
            }
        }

        public long getCanonic() {
            // tohle zapamatovavani zrychlilo -MV
            if (-1 == canonic) {
                oldValueParent = oldValue;
                newValueParent = newValue;
                canonic = makeCanonic();
                return canonic;
            }
            if (!oldValueParent.isActive() || !newValueParent.isActive()) {
                oldValueParent = oldValueParent.getUpMostParent();
                newValueParent = newValueParent.getUpMostParent();
                canonic = makeCanonic();
            }
            return canonic;
        }

        private long makeCanonic() {
            return (variable.getRealId() 
                    * oldValueParent.getVariable().getValues().size() + oldValueParent.getOriginalID())
                    * newValueParent.getVariable().getValues().size() + newValueParent.getOriginalID();
        }

        @Override
        public int compareTo(Object o) {
            if (null == o) {
                return -1;
            }
            if (o.getClass() != getClass()) {
                return -1;
            }
            Effect effect = (Effect) o;
            if (getVariable().getRealId() < effect.getVariable().getRealId()) {
                return -1;
            } else if (effect.getVariable().getRealId() < getVariable().getRealId()) {
                return 1;
            }
            if (getOldValue().getParentRealID() < effect.getOldValue().getParentRealID()) {
                return -1;
            } else if (getOldValue().getParentRealID() < effect.getOldValue().getParentRealID()) {
                return 1;
            }
            if (getNewValue().getParentRealID() < effect.getNewValue().getParentRealID()) {
                return -1;
            } else if (getNewValue().getParentRealID() < effect.getNewValue().getParentRealID()) {
                return 1;
            }
            return 0;
        }

        @Override
        public void setActive() {
            active = true;
        }

        public String getInputCanonicString() {
            return variable.getOriginalId() + ":" + oldValue.getUpMostParent().getOriginalID() + ",";
        }

        public String getOutputCanonicString() {
            return variable.getOriginalId() + ":" + newValue.getUpMostParent().getOriginalID() + ",";
        }
    }
    
    @Override
    public String toString() {
        return name;
    }
}
