package cz.agents.dimap.tools.sas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.psmsas.operations.reverse.UniversalReverse;
import cz.agents.dimap.tools.Pair;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.MutexGroup.Mutex;
import cz.agents.dimap.tools.sas.Variable.Value;

public class SasFile {

    public Map<Integer, Variable> variables;
    public Map<String, Operator> operators;
    private List<MutexGroup> mutexes;
    private long versionNum;
    private int metric;
    private int numberOfAxioms;
    private int originalNumberOfVariables;
    private Map<Variable.Value, Set<Operator>> vmCache = new HashMap<>();
    private Map<Variable.Value, Set<Operator>> valueCache = new HashMap<>();
    private Map<Variable, Set<Operator>> variableOperators = new HashMap<>();
    public Map<Variable, SasGraph> dtgs = new HashMap<>();
    public SasGraph causalGraph;;

    private Set<Pair<Value, Value>> mutexMap = null;

    public SasFile(File file) throws IOException {
        this(new FileInputStream(file));
    }

    public SasFile(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        versionNum = SasParser.readVersion(br);
        metric = SasParser.readMetric(br);
        variables = SasParser.readVariables(br);
        originalNumberOfVariables = variables.values().size();
        mutexes = SasParser.readMutexes(br, variables);
        SasParser.readInit(br, variables);
        SasParser.readGoal(br, variables);
        operators = SasParser.readOperators(br, variables, this);
        numberOfAxioms = SasParser.readAxioms(br);
        SasParser.skipSg(br);
        dtgs = SasParser.readDtgs(br, variables);
        causalGraph = SasParser.readCg(br, variables.size());
        br.close();
    }

    @Override
    public String toString() {
        return "SasFile{" +
                "variables=" + variables +
                ", operators=" + operators +
                ", mutexes=" + mutexes +
                ", versionNum=" + versionNum +
                ", metric=" + metric +
                ", numberOfAxioms=" + numberOfAxioms +
                ", originalNumberOfVariables=" + originalNumberOfVariables +
                '}';
    }

    public boolean isMetric() {
        return 0 == metric;
    }

    public String getFinalSAS() {
        return getCurrentSAS(false);
    }

    public String getCurrentSAS(boolean debug) {
        StringBuilder builder = new StringBuilder();
        appendBlock(builder, "version", versionNum);
        appendBlock(builder, "metric", metric);
        if (!debug) {
            consistentRenaming();
        }
        appendVariables(builder, debug);
        appendMutexes(builder, debug);
        appendInit(builder, debug);
        appendGoal(builder, debug);
        appendOperators(builder, debug);
        appendAxioms(builder);

        return builder.toString();
    }

    public String createInfo() {
        return (isGoalSubsetOfInit() ? " REDUCED !!!" : "") +
                " Variables: " + getNumberOfActiveVariables() + "/" + getOriginalNumberOfVariables() + ". " +
                "Goals: " + getNumberOfActiveGoals() + "/" + getOriginalNumberOfGoals() + ". " +
                "Operators: " + getNumberOfActiveOperators() + "/" + getOriginalNumberOfOperators() + ". " +
                "Values: " + getNumberOfActiveValues() + "/" + getOriginalNumberOfValues() + ". " +
                "Is goal subset of init: " + isGoalSubsetOfInit() + ". ";
    }

    private void consistentRenaming() {
        if (Settings.DEBUG) System.out.println("consistent renaming");
        int idx = 0;
        for (Variable variable : variables.values()) {
            if (variable.isActive()) {
                variable.setRealId(idx);
                variable.consistentRename();
                idx++;
            }
        }
    }

    private void appendMutexes(StringBuilder bd, boolean debug) {
        StringBuilder sbMutex = new StringBuilder();
        Set<String> alreadyIn = new HashSet<>();
        alreadyIn.add("");
        int numberOfMutexes = 0;
        int size = 0;
        for (MutexGroup mutexGroup : mutexes) {
            if (mutexGroup.shouldBeActive() || debug) {
                String canonic = mutexGroup.getCanonicString();
                size = alreadyIn.size();
                alreadyIn.add(canonic);
                if (alreadyIn.size() != size) {
                    mutexGroup.appendSAS(sbMutex, debug);
                    numberOfMutexes++;
                } else if (debug) {
                    sbMutex.append("XXXXXXX");
                    mutexGroup.appendSAS(sbMutex, debug);
                }
            }
        }
        bd.append(numberOfMutexes).append("\n");
        bd.append(sbMutex.toString());
    }
    
    public List<MutexGroup> getMutexes() {
        return mutexes;
    }

    private void appendInit(StringBuilder bd, boolean debug) {
        bd.append("begin_state\n");
        for (Variable variable : variables.values()) {
            if (variable.isActive()) {
                bd.append(variable.getSASInit());
            } else if (debug) {
                bd.append("XXX\n");
            }
        }
        bd.append("end_state\n");
    }

    private void appendGoal(StringBuilder bd, boolean debug) {
        bd.append("begin_goal\n");
        bd.append(getNumberOfActiveGoals()).append("\n");
        for (Variable variable : variables.values()) {
            if (variable.isActive() && variable.isInGoal()) {
                bd.append(variable.getSASGoal());
            } else if (debug) {
                bd.append("XXX\n");
            }
        }
        bd.append("end_goal\n");
    }

    private void appendOperators(StringBuilder bd, boolean debug) {
        bd.append(getNumberOfActive(operators.values())).append("\n");
        for (Operator operator : operators.values()) {
            if (operator.isActive() || debug) {
                operator.appendSAS(bd, debug);
            }
        }
    }

    private void appendAxioms(StringBuilder bd) {
        // axioms are not parsed, nor used
        bd.append(numberOfAxioms);
    }

    public int getNumberOfActiveOperators() {
        return getNumberOfActive(operators.values());
    }

    private int getNumberOfActive(Collection<? extends Active> list) {
        // may be some of that could be done by caching efficiently
        int sum = 0;
        for (Active active : list) {
            if (active.isActive()) {
                sum++;
            }
        }
        return sum;
    }

    private void appendVariables(StringBuilder bd, boolean debug) {
        bd.append(getNumberOfActive(variables.values())).append("\n");
        for (int i = 0; i < variables.size(); i++) {
            Variable variable = variables.get(i);
            if (variable.isActive() || debug) {
                variable.appendSAS(bd, debug);
            }
        }
    }

    private void appendBlock(StringBuilder builder, String name, long value) {
        builder.append("begin_").append(name).append("\n").append(value);
        builder.append("\nend_").append(name).append("\n");
    }

    public Map<Integer, Variable> getVariables() {
        return variables;
    }

    public Map<String, Operator> getOperators() {
        return operators;
    }

    public long getOriginalNumberOfVariables() {
        return originalNumberOfVariables;
    }

    public int getNumberOfActiveGoals() {
        // TODo remake by caching
        int sum = 0;
        for (Variable variable : variables.values()) {
            if (variable.isActive() && variable.isInGoal()) {
                sum++;
            }
        }
        return sum;
    }

    public int getOriginalNumberOfOperators() {
        return operators.values().size();
    }

    public Map<Variable.Value, Set<Operator>> getVmCache() {
        return vmCache;
    }

    public Map<Variable.Value, Set<Operator>> getValueCache() {
        return valueCache;
    }

    public int getOriginalNumberOfGoals() {
        int sum = 0;
        for (Variable variable : variables.values()) {
            if (variable.isInGoal()) {
                sum++;
            }
        }
        return sum;
    }

    public int getNumberOfActiveVariables() {
        return getNumberOfActive(variables.values());
    }

    //TODO cache
    public Collection<Variable> getGoalVariables() {
        LinkedList<Variable> list = new LinkedList<>();
        for (Map.Entry<Integer, Variable> entry : variables.entrySet()) {
            Variable variable = entry.getValue();
            if (variable.isActive() && variable.isInGoal()) {
                list.add(variable);
            }
        }
        return list;
    }

    public long getNumberOfActiveValues() {
        long sum = 0;
        for (Variable variable : variables.values()) {
            if (variable.isActive()) {
                for (Variable.Value value : variable.getValues()) {
                    if (value.isActive() && !value.getUpMostParent().isAnyValue()) {
                        sum++;
                    }
                }
            }
        }
        return sum;
    }

    public long getOriginalNumberOfValues() {
        long sum = 0;
        for (Variable variable : variables.values()) {
            for (Variable.Value value : variable.getValues()) {
                if (!value.getUpMostParent().isAnyValue()) {
                    sum++;
                }
            }
        }
        return sum;
    }

    public void cacheOperator(Operator operator) {
        if (!operator.isActive()) return;
        if (operator.getNumberOfActiveEffects() == 1 && operator.getNumberOfActivePreconditions() == 0) {
            cacheOperatorFor(operator, vmCache);
        }
        cacheOperatorFor(operator, valueCache);
    }


    public void storeOperator(Operator operator) {
        for (Operator.Precondition precondition : operator.getPreconditions()) {
            addToMap(precondition.getVariable(), operator, variableOperators);
        }
        for (Operator.Effect effect : operator.getEffects()) {
            addToMap(effect.getVariable(), operator, variableOperators);
        }
    }

    private void cacheOperatorFor(Operator operator, Map<Variable.Value, Set<Operator>> cache) {
        Operator.Effect effect = operator.getFirstActiveEffect();
        if (effect.getOldValue().getUpMostParent().isAnyValue()) {
            for (Variable.Value value : effect.getVariable().getValues()) {
                if (!value.getUpMostParent().isAnyValue()) { // mozna redundantni dotaz, nevim
                    addToMap(value, operator, cache);
                }
            }
        } else {
            addToMap(effect.getOldValue().getUpMostParent(), operator, cache);
        }
        addToMap(effect.getNewValue().getUpMostParent(), operator, cache);
    }

    // assume that value is upMostParent
    private static void addToMap(Variable.Value value, Operator operator, Map<Variable.Value, Set<Operator>> map) {
        if (!map.containsKey(value)) {
            map.put(value, new HashSet<Operator>());
        }
        map.get(value).add(operator);
    }

    private static void addToMap(Variable variable, Operator operator, Map<Variable, Set<Operator>> map) {
        if (!map.containsKey(variable)) {
            map.put(variable, new HashSet<Operator>());
        }
        map.get(variable).add(operator);
    }

    public Map<Variable, Set<Operator>> getVariableOperators() {
        return variableOperators;
    }

    public List<Operator> getApplicableOperatorInStart() {
        List<Operator> list = new ArrayList<>();
        for (Operator operator : operators.values()) {
            if(operator.isActive() && isApplicableInInit(operator)){
                list.add(operator);
            }
        }
        return list;
    }


    public boolean isInInit(Variable.Value value){
        return value.isInitValue();
    }

    public boolean isApplicableInInit(Operator operator) {
        for (Operator.Precondition precondition : operator.getPreconditions()) {
            if (precondition.isActive() && !precondition.getValue().isInitValue()) {
                return false;
            }
        }
        for (Operator.Effect effect : operator.getEffects()) {
            if (effect.isActive() && !effect.getOldValue().isInitValue()) {
                return false;
            }
        }
        return true;
    }

    public boolean isUsedOnlyOnce(Operator operator) {
        for (Operator.Effect effect : operator.getEffects()) {
            if(effect.isActive() && getProducers(effect.getOldValue().getCachedUpMostParent()).size() > 0){
                return false;
            }
        }

        return true;
    }

    private List<Operator> getProducers(Variable.Value value) {
        List<Operator> list = new ArrayList<>();
        Variable.Value current = value.getCachedUpMostParent();
        if(!variableOperators.containsKey(current)){
         return list;
        }
        for (Operator operator : variableOperators.get(current)) {
            if(operator.isActive() && operator.isProducingValue(current)){
                list.add(operator);
            }
        }
        return list;
    }

    public boolean isDestroyingAnyOtherAction(Operator operator) {
        for (Operator.Effect effect : operator.getEffects()) {
            // because at least one action destroys it
            if(effect.isActive()){
                if(effect.getOldValue().getUpMostParent().isAnyValue() || effect.getOldValue().isInGoal() || getRequirers(effect.getOldValue().getUpMostParent()).size() > 1){
                    return true;
                }
            }
        }
        return false;
    }

    private List<Operator> getRequirers(Variable.Value value) {
        List<Operator> list = new ArrayList<>();
        Variable.Value current = value.getCachedUpMostParent();
        if(!variableOperators.containsKey(current.getVariable())){
            return list;
        }
        for (Operator operator : variableOperators.get(current.getVariable())) {
            if(operator.isActive() && operator.isNeedingValue(current)){
                list.add(operator);
            }
        }
        return list;
    }

    public void applyOnStart(Operator operator) {
        for (Operator.Effect effect : operator.getEffects()) {
            if(effect.isActive()){
                Variable variable = effect.getVariable();
                variable.setInitValue(effect.getNewValue());
            }
        }
    }

    public boolean containsOperator(String canonic) {
        for (Operator operator : operators.values()) {
            if(operator.isActive() && (operator.getCurrentCanonicalString().equals(canonic) || operator.getCanonicalString().equals(canonic))){
                return true;
            }
        }
        return false;
    }

    public Set<Operator> getConsumers(Variable.Value value) {
        if(!variableOperators.containsKey(value.getVariable())){
            return new HashSet<>();
        }
        Variable.Value currentValue = value.getCachedUpMostParent();
        Set<Operator> set = new HashSet<>();
        for (Operator operator : variableOperators.get(value.getVariable())) {
            if(operator.isActive() && operator.isConsumingValue(currentValue)){
                set.add(operator);
            }
        }
        return set;
    }

    public boolean isGoalSubsetOfInit() {
        for (Variable variable : getGoalVariables()) {
            if (variable.isActive() && variable.getInitValue().getUpMostParent() != variable.getGoalValue().getUpMostParent()) {
                return false;
            }
        }
        return true;
    }

    public boolean areMutexed(Value value1, Value value2) {
        
        buildMutexMap();

        return mutexMap.contains(new Pair<>(value1, value2));
    }

    private void buildMutexMap() {
        if (mutexMap == null) {
            mutexMap = new HashSet<>();
            for (MutexGroup mutexGroup : mutexes) {
                for (Mutex mutex1 : mutexGroup.getMutexes()) {
                    for (Mutex mutex2 : mutexGroup.getMutexes()) {
                        if (mutex1 != mutex2) {
                            mutexMap.add(new Pair<>(mutex1.getValue(), mutex2.getValue()));
                        }
                    }
                }
            }
        }
    }

    public MutexGroup addMutex(Value value1, Value value2) {
        if (!areMutexed(value1, value2)) {
            MutexGroup mutex = new MutexGroup(value1, value2);
            mutexes.add(mutex);
            mutexMap.add(new Pair<>(value1, value2));
            mutexMap.add(new Pair<>(value2, value1));
            return mutex;
        }
        return null;
    }

    public ReverseOperation removeValueFromMutexes(Value value) {
        MultiReverseOperation multiReverseOperation = new MultiReverseOperation("Remove value from Mutex");
        for (MutexGroup mutex : mutexes) {
            
            UniversalReverse reverse = mutex.disableValue(value);
            if (reverse != null) {
                multiReverseOperation.addReverseOperation(reverse);
            }
        }
        if (multiReverseOperation.isEmpty()) {
            return null;
        } else {
            return multiReverseOperation;
        }
    }
    

    public ReverseOperation actualizeMutexes() {
        UniversalReverse reverse = null;
        for (MutexGroup mutex : mutexes) {
            if (mutex.isActive() && mutex.actualize()) {
                if (reverse == null) {
                    reverse = new UniversalReverse("Actualize mutexes");
                }
                reverse.activate(mutex);
            }
        }
        return reverse;
    }
}
