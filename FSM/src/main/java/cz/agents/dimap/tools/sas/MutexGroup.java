package cz.agents.dimap.tools.sas;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.agents.dimap.psmsas.operations.reverse.UniversalReverse;
import cz.agents.dimap.tools.sas.Variable.Value;

/**
 * Created by pah on 8.7.15.
 * <p/>
 * Class representing a mutex block in SAS.
 */
public class MutexGroup implements Active {

    private final List<Mutex> mutexes;
    private boolean active = true;

    public MutexGroup(List<Mutex> mutexes) {
        this.mutexes = mutexes;
    }

    public MutexGroup(Value ... values) {
        this.mutexes = new ArrayList<>();
        for (Value value : values) {
            mutexes.add(new Mutex(value));
        }
    }

    public MutexGroup(BufferedReader br, Map<Integer, Variable> variables) throws IOException {
        this.mutexes = new ArrayList<>();

        int num = Integer.parseInt(br.readLine());
        String[] splitted;
        for (int i = 0; i < num; i++) {
            splitted = br.readLine().split(" ");
            if (splitted.length != 2) {
                throw new IllegalStateException("Mutex is supposed to be in format: 'variable-number value'");
            }
            addMutex(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]), variables);
        }
    }

    private void addMutex(int variableID, int valueId, Map<Integer, Variable> variables) {
        Variable variable = variables.get(variableID);
        addMutex(variable.getValueOriginal(valueId));
    }

    public void addMutex(Variable.Value value) {
        mutexes.add(new Mutex(value));
    }

    public List<Mutex> getMutexes() {
        return mutexes;
    }

    @Override
    public String toString() {
        return "MutexGroupToDel{" +
                "mutexes=" + mutexes +
                '}';
    }

    public boolean isActive() {
        return active;
    }

    public boolean isActiveByContent() {
        for (Mutex mutex : mutexes) {
            if (mutex.getVariable().isActive()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setActive() {
        active = true;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getNumberOfActiveMutexes() {
        // TODO remake this method by caching
        int sum = 0;
        for (Mutex mutex : mutexes) {
            if (mutex.isActive()) {
                sum++;
            }
        }
        return sum;
    }

    public String getSAS() {
        StringBuilder sb = new StringBuilder();
        appendSAS(sb, false);
        return sb.toString();
    }

    /**
     * Appends SAS representation of MutexGroup to sb.
     * If the debug argument is set to true, then XXX is appended instead of unactive mutexes.
     *
     * @param sb
     * @param debug
     */
    public void appendSAS(StringBuilder sb, boolean debug) {
        Set<String> alreadyIn = new HashSet<>();
        alreadyIn.add("");
        StringBuilder sbGroup = new StringBuilder();
        int numberOfMutexes = 0;
        for (Mutex mutex : mutexes) {
            String canonic = mutex.getCanonicString();
            if (mutex.isActive() && !alreadyIn.contains(canonic)) {
                alreadyIn.add(canonic);
                mutex.appendSAS(sbGroup);
                numberOfMutexes++;
            } else if (debug) {
                sbGroup.append("XXX\n");
            }
        }
        if (numberOfMutexes > 0) {
            sb.append("begin_mutex_group\n").append(numberOfMutexes).append("\n");
            sb.append(sbGroup.toString());
            sb.append("end_mutex_group\n");
        } else if (debug) {
            sb.append("begin_mutex_group\n").append(numberOfMutexes).append("\n");
            sb.append(sbGroup.toString());
            sb.append("end_mutex_group\n");
        }
    }

    public String getCanonicString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        // this because of the same output order
        List<Mutex> becauseOfSort = new LinkedList<>();
        becauseOfSort.addAll(mutexes);
        Collections.sort(becauseOfSort);

        for (Mutex mutex : becauseOfSort) {
            if (mutex.isActive() && mutex.getVariable().isActive()) {
                sb.append(mutex.getCanonicString());
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public boolean actualize() {
        if (isActive() && !shouldBeActive()) {
            setActive(false);
            return true;
        } else {
            return false;
        }
    }
    /**
     * Sets active to false if the mutex group consists of only one mutex (tuple).
     */
    public boolean shouldBeActive() {
        Set<String> alreadyIn = new HashSet<>();
        for (Mutex mutex : mutexes) {
            if (mutex.getVariable().isActive()) { // cannot add unactive mutex
                String canonic = mutex.getCanonicString();
                alreadyIn.add(canonic);
            }
        }
        return (alreadyIn.size() > 1 && isActive());
    }

    public boolean containsDifferentVariables() {
        Variable lastVariable = null;
        for (Mutex mutex : mutexes) {
            if (mutex.isActive() && mutex.getVariable().isActive()) {
                if (null == lastVariable) {
                    lastVariable = mutex.getVariable();
                } else if (mutex.getVariable() != lastVariable) {
                    return true;
                }
            }
        }
        return false;
    }

    /*public boolean actualize() {
        boolean change = false;
        Set<String> alreadyIn = new HashSet<>();
        int size = 0;
        for (Mutex mutex : mutexes) {
            if (mutex.isActive()) {
                change |= mutex.actualize();
                size = alreadyIn.size();
                alreadyIn.add(mutex.getCanonicString());
                if (alreadyIn.size() == size) {
                    change = true;
                }
            }
        }
        return change;
    }*/

    public boolean areMutexed(Value value1, Value value2) {
        return mutexes.contains(new Mutex(value1)) && mutexes.contains(new Mutex(value2));
    }

    /**
     * Wrapper for pair.
     */
    public class Mutex implements Active, Comparable<Mutex> {
        private final Variable.Value value;
        private boolean active = true;

        public Mutex(Variable.Value value) {
            this.value = value;
        }

        public Variable getVariable() {
            return value.getVariable();
        }

        public Variable.Value getValue() {
            return value;
        }

        public boolean isActive() {
            return active;
        }

        @Override
        public void setActive() {
            setActive(true);
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String getSAS() {
            return value.getVariable().getRealId() + " " + value.getParentRealID() + "\n";
        }

        public void appendSAS(StringBuilder sb) {
            sb.append(getSAS());
        }


        @Override
        public String toString() {
            return value.getVariable() + "_" + value.getOriginalID();
        }


        @Override
        public int compareTo(Mutex mutex) {
            if (null == mutex) {
                return -1;
            }
            if (getVariable().getRealId() < mutex.getVariable().getRealId()) {
                return -1;
            } else if (mutex.getVariable().getRealId() < getVariable().getRealId()) {
                return 1;
            }

            if (!value.getVariable().isActive()) {
                return 0;
            }

            if (getValue().getParentRealID() < mutex.getValue().getParentRealID()) {
                return -1;
            } else if (mutex.getValue().getParentRealID() < getValue().getParentRealID()) {
                return 1;
            }
            return 0;
        }


        /**
         * Do not call when the variable is unactive.
         *
         * @return
         */
        public String getCanonicString() {
            /*System.out.print(variable.getOriginalId() + "/" + variable.isActive());
            for (Variable.Value value1 : variable.getValues()) {
                System.out.print("\t" + value.getOriginalID() + "/" + value.isActive());
            }
            System.out.println();*/
            if (!value.getVariable().isActive() || (getValue().isTerminal() && !getValue().isActive())) {
                return "";
            }
            return "[" + getVariable().getRealId() + "," + getValue().getParentRealID() + "]";
        }

        private MutexGroup getOuterType() {
            return MutexGroup.this;
        }

        /*public boolean actualize() {
            // tohle se ale opira o to, ze domenu promenne oriznu
            // nedela se tohle nekde jinde jeste, takze jestli nedelam redundantni praci tady
            if (1 >= value.numberOfActiveValues()) {
                variable.setActive(false);
                // mozna i nastavit value na false
                value.setActive(false);
                setActive(false);
            }

            if(!variable.isActive() || value.getParent().isActive()){
                setActive(false);
                return false;
            }
            return true;
        }*/

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + (active ? 1231 : 1237);
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Mutex other = (Mutex) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (active != other.active)
                return false;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }
    }

    public UniversalReverse disableValue(Value value) {
        UniversalReverse reverse = new UniversalReverse("Disable mutex");
        for (Mutex mutex : mutexes) {
            if (mutex.value.equals(value)) {
                mutex.setActive(false);
                reverse.activate(mutex);
            }
        }
        if (getNumberOfActiveMutexes() < 2) {
            setActive(false);
            reverse.activate(this);
        }
        if (reverse.getNumberOfReductions() > 0) {
            return reverse;
        } else {
            return null;
        }
    }
}
