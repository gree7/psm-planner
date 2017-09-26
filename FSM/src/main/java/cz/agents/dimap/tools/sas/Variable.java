package cz.agents.dimap.tools.sas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pah on 8.7.15.
 * <p/>
 * Class representing variable of SAS.
 */
public class Variable implements Active {
    private final int originalId;
    private int realId;
    private boolean active = true;
    private boolean inGoal = false;
    private Value goalValue;
    private Value initValue;
    private final List<Value> values;
    private final int layer;
    private Value anyValue;

    public Variable(int originalId, int layer) {
        this.originalId = originalId;
        this.realId = this.originalId;
        this.values = new ArrayList<>();
        this.layer = layer;
    }

    public Variable(int originalId, int layer, List<Value> values) {
        this.originalId = originalId;
        this.values = values;
        this.layer = layer;
    }

    public boolean hasAnyvalue(){
        return null != anyValue;
    }

    public Value getAnyValue() {
        if (null == anyValue) {
            anyValue = new Value(-1, "-1", this, true);
        }
        return anyValue;
    }

    public int getRealId() {
        return realId;
    }

    public void setRealId(int realId) {
        this.realId = realId;
    }

    public boolean isActive() {
        return active;// && (getNumberOfActiveValues() > 1);
    }

    @Override
    public void setActive() {        active = true;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getOriginalId() {
        return originalId;
    }

    public List<Value> getValues() {
        return values;
    }

    public Value getGoalValue() {
        return goalValue;
    }

    public void setGoalValue(Value goalValue) {
        this.inGoal = true;
        this.goalValue = goalValue;
    }

    public void setGoalValue(Integer goalValueID) {
        setGoalValue(values.get(goalValueID));
    }

    public Value getInitValue() {
        return initValue;
    }

    public void setInitValue(Value initValue) {
        this.initValue = initValue;
    }

    public void setInitValue(Integer id) {
        this.initValue = values.get(id);
    }

    public boolean isInGoal() {
        return inGoal;
    }

    public void setInGoal(boolean inGoal) {
        this.inGoal = inGoal;
    }

    public int getLayer() {
        return layer;
    }

    public Variable.Value addNextValue(String string) {
        Variable.Value value = new Value(values.size(), string, this);
        values.add(value);
        return value;
    }

    public Value getValueOriginal(Integer valueID) {
        return values.get(valueID);
    }

    /**
     * Appends SAS representation of variable to sb.
     * If debug is set to true, then XXX is added instead of unactive values.
     *
     * @param sb
     */
    public void appendSAS(StringBuilder sb, boolean debug) {
        sb.append("begin_variable\n");
        sb.append("var").append(getRealId()).append("\n");
        sb.append(getLayer()).append("\n");
        int activeValues = getNumberOfActiveValues();
        sb.append(activeValues).append("\n");

        for (Value value : values) {
            if (value.isActive()) {
                sb.append(value.getValue()).append("\n");
            } else if (debug) {
                sb.append("XXX\n");
            }
        }
        sb.append("end_variable\n");
    }

    public String getSAS() {
        StringBuilder sb = new StringBuilder();
        appendSAS(sb, false);
        return sb.toString();
    }

    public int getNumberOfActiveValues() {
        // TODO remake this by caching
        int sum = 0;
        for (Value value : values) {
            if (value.isActive()) {
                sum++;
            }
        }
        return sum;
    }

    public String getSASInit() {
        return getInitValue().getParentRealID() + "\n";
    }

    public String getSASGoal() {
        return getRealId() + " " + getGoalValue().getParentRealID() + "\n";
    }

    public void consistentRename() {
        int idx = 0;
        for (Value value : values) {
            if (value.isActive()) {
                value.setRealID(idx);
                idx++;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Variable variable = (Variable) o;

        if (originalId != variable.originalId) return false;
        if (layer != variable.layer) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = originalId;
        result = 31 * result + layer;
        return result;
    }

    public class Value implements Active {
        private boolean active;
        private int realID;
        private final int originalID;
        private Value parent;
        private final String value;
        private final boolean isAnyValue;
        private final Variable variable;
        private Value parentUpMost;

        public Value(int originalID, String value, Variable variable) {
            this.originalID = originalID;
            this.realID = originalID;
            this.active = true;
            this.parent = this;
            this.value = value;
            this.isAnyValue = false;
            this.variable = variable;
            this.parentUpMost = this;
        }

        public Value(int originalID, String value, Variable variable, boolean isAnyValue) {
            this.originalID = originalID;
            this.realID = originalID;
            this.active = true;
            this.parent = this;
            this.value = value;
            this.isAnyValue = isAnyValue;
            this.variable = variable;
            this.parentUpMost = this;
        }

        public boolean isAnyValue() {
            return isAnyValue;
        }

        public boolean isActive() {
            return active;
        }

        @Override
        public void setActive() {
            this.parent = this;
            this.parentUpMost = this;
            active = true;
        }

        public void setActive(boolean active) {
            this.active = active;
            if (active) {
                setActive();
            }
        }

        public int getRealID() {
            return realID;
        }

        public void setRealID(int realID) {
            this.realID = realID;
        }

        public Value getParent() {
            return parent;
        }

        public Value getUpMostParent() {
            if (isTerminal()) {
                return this;
            }
            return parent.getUpMostParent();
        }

        /**
         * Use this method only in the reduction part. While expanding, the caching does not reflect the right upmost parent.
         * @return
         */
        public Value getCachedUpMostParent() {
            if (isTerminal()) {
                return this;
            }
            if(null == parentUpMost) parentUpMost = parent.getUpMostParent();
            if(!parentUpMost.isTerminal()) parentUpMost = parentUpMost.getUpMostParent();
            return parentUpMost;
        }

        public void setParent(Value parent) {
            if (isTerminal()) {
                this.active = false;
                this.parent = parent;
            } else {
                getParent().setParent(parent);
            }
            this.parentUpMost = parent.getUpMostParent();
        }


        public int getOriginalID() {
            return originalID;
        }

        public boolean isTerminal() {
            return this == this.parent;
        }

        public String getValue() {
            return value;
        }

        public String getParentValue() {
            if (this.isTerminal()) {
                return value;
            }
            return parent.getParentValue();
        }

        public int getParentRealID() {
            return getUpMostParent().getRealID();
            /*if (this.isTerminal()) {
                return realID;
            }
            Value ancester = parent;
            while (true) {
                //if(ancester.isActive()){
                if (ancester.parent == ancester) { // not consistent
                    return ancester.getRealID();
                }

                ancester = ancester.getParent();
            }*/
        }

        public int numberOfActiveValues() {
            // TODO remake this method by caching
            int sum = 0;
            for (Value value : values) {
                if (value.isActive()) {
                    sum++;
                }
            }
            return sum;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Value value1 = (Value) o;

            if (originalID != value1.originalID) return false;
            if (isAnyValue != value1.isAnyValue) return false;
            if (!value.equals(value1.value)) return false;
            return variable.equals(value1.variable);

        }

        @Override
        public int hashCode() {
            int result = originalID;
            result = 31 * result + value.hashCode();
            result = 31 * result + (isAnyValue ? 1 : 0);
            result = 31 * result + variable.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return value;
        }
        
        public Variable getVariable() {
            return variable;
        }

        public boolean isInitValue() {
            return !variable.isActive() || isAnyValue() || getUpMostParent() == variable.getInitValue().getUpMostParent();
        }

        public boolean isInGoal() {
            if(!variable.isInGoal()){
                return false;
            }
            return getUpMostParent() == variable.getGoalValue().getUpMostParent();
        }
    }

    @Override
    public String toString() {
        return "var"+getRealId();
    }
}
