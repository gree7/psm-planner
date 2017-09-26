package cz.agents.dimap.psmsas;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import cz.agents.dimap.psmsas.operations.ReduceOperation;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reductions.ActionStartMerge;
import cz.agents.dimap.psmsas.operations.reductions.AddMutex;
import cz.agents.dimap.psmsas.operations.reductions.ConstantValue;
import cz.agents.dimap.psmsas.operations.reductions.DeadEnds;
import cz.agents.dimap.psmsas.operations.reductions.GeneralizedAction;
import cz.agents.dimap.psmsas.operations.reductions.GroundAnyValue;
import cz.agents.dimap.psmsas.operations.reductions.GroundSimpleAction;
import cz.agents.dimap.psmsas.operations.reductions.HalfCycleNew;
import cz.agents.dimap.psmsas.operations.reductions.MergeVariables;
import cz.agents.dimap.psmsas.operations.reductions.OneEffectDelete;
import cz.agents.dimap.psmsas.operations.reductions.OneUsage;
import cz.agents.dimap.psmsas.operations.reductions.RemoveDeadEndOperators;
import cz.agents.dimap.psmsas.operations.reductions.ShrinkSimilarOperators;
import cz.agents.dimap.psmsas.operations.reductions.SimpleDependencyReduction;
import cz.agents.dimap.psmsas.operations.reductions.SimpleTunnelMacro;
import cz.agents.dimap.psmsas.operations.reductions.SingleFirstActionMergeWithStart;
import cz.agents.dimap.psmsas.operations.reductions.TunnelMacro;
import cz.agents.dimap.psmsas.operations.reductions.UseApplicableActionInStart;
import cz.agents.dimap.psmsas.operations.reductions.ValuesMerging;
import cz.agents.dimap.psmsas.operations.reductions.ValuesMergingBigOperator;
import cz.agents.dimap.psmsas.operations.reductions.ValuesPruning;
import cz.agents.dimap.psmsas.operations.reductions.VariableDeletion;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.Problem;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.dot.DotCreater;
import cz.agents.dimap.tools.fd.Downward;
import cz.agents.dimap.tools.sas.MutexGroup;
import cz.agents.dimap.tools.sas.MutexGroup.Mutex;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;
import cz.agents.dimap.tools.sas.Variable.Value;

/**
 * Wrapper class that servers as a planner using reduction of given instance, running FD and making extending at the end.
 * <p/>
 * TODO write usage of the class - four steps
 */
public class PsmSasPlanner {

    private File domainFile;
    private File problemFile;
    public SasFile translatedSAS;
    private long translationTime;
    private long reductionTime;
    private long planningTime;
    private long recoveryTime;
    private Stack<ReverseOperation> reverseOperations = new Stack<>();
    private Plan plan;
    private final List<ReduceOperation> operations = new ArrayList<>();
    private String storedInfo = "";
    private boolean checkEnd;

    private Map<ReduceOperation, Integer> reductionCount = new HashMap<>();
    private Map<ReduceOperation, Long> reductionTimesMs = new HashMap<>();
    private long goalCheckTimeMs = 0l;
    private final Map<String, ReduceOperation> reverseOperationMap = makeReverseOperationMap();
    private String problemName;

    private double reductionRatio;
    
    /**
     * Makes new PsmSasPlaner.
     * <p/>
     * TODO actualize parameter secion
     * String reductions can take combination of following values: -mv for using merging values reduction, -dv for using delete variable reduction, -ud for deleting unused values, -sd for merging operators because of simple dependencies, -mp for pruning values with respect to mutex, -mo for merging similar operators, -mw for merging values of possible different variables, -de for pruning dead ends, -hc for half cycle, -ou for one usage reduction, -oe for one effect reduction, -ac for action start merge. Add -ch for checking if goal state is a subset of initial state and stopping reduction when they are not necessary.
     *
     * @param domainFile
     * @param problemFile
     */
    public PsmSasPlanner(File domainFile, File problemFile, String reductions) {
        this.domainFile = domainFile;
        this.problemFile = problemFile;
        setUpReductions(reductions);
    }

    private Map<String, ReduceOperation> makeReverseOperationMap() {
        Map<String, ReduceOperation> map = new HashMap<>();
        map.put("-mo", new ShrinkSimilarOperators());
        map.put("-mv", new ValuesMerging());
        map.put("-bv", new ValuesMergingBigOperator());
        map.put("-hc", new HalfCycleNew());
        map.put("-mo", new ShrinkSimilarOperators());
        map.put("-dv", new VariableDeletion());

        // Values Prunning has to be called after -dv because it otherwise set to unactive states something that ready to be unactivized be -dv.
        map.put("-uv", new ValuesPruning());
        map.put("-sd", new SimpleDependencyReduction());
        map.put("-de", new DeadEnds());
        map.put("-do", new RemoveDeadEndOperators());
        map.put("-ou", new OneUsage());
        map.put("-oe", new OneEffectDelete());
        map.put("-as", new ActionStartMerge());
        map.put("-dv", new VariableDeletion());
        map.put("-sf", new SingleFirstActionMergeWithStart());
        map.put("-ai", new UseApplicableActionInStart());
        map.put("-ga", new GeneralizedAction());
        map.put("-cv", new ConstantValue());
        map.put("-gr", new GroundSimpleAction());

        map.put("-am", new AddMutex());
        
        map.put("-gv", new GroundAnyValue());
        map.put("-tm", new SimpleTunnelMacro());

        map.put("-tn", new TunnelMacro());

        map.put("-mg", new MergeVariables());
        return map;
    }

    public PsmSasPlanner(File sasFile, String problemName, String reductions) {
        try {
            this.translatedSAS = new SasFile(sasFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setUpReductions(reductions);
    }

    public long getTranslationTime() {
        return translationTime;
    }

    public long getReductionTime() {
        return reductionTime;
    }

    public long getPlanningTime() {
        return planningTime;
    }

    public long getRecoveryTime() {
        return recoveryTime;
    }

    public long getComputingtime() {
        return translationTime + reductionTime + planningTime + recoveryTime;
    }


    /**
     * 1st phase
     */
    public void translate() {
        long groundTime = System.currentTimeMillis();
        Problem problem = new Problem(domainFile, problemFile);
        this.translatedSAS = Downward.runTranslator(problem);
        problemName = problem.domainName + "-" + problem.problem.getName();
        translationTime = System.currentTimeMillis() - groundTime;
    }


    /**
     * 2nd phase
     */
    public void reduce() {
        long groundTime = System.currentTimeMillis();
        if (null == translatedSAS) {
            throw new IllegalStateException("Method translate() has to be executed before calling reduce().");
        }
        makeReductions();
        reductionTime = System.currentTimeMillis() - groundTime;
        storedInfo = translatedSAS.createInfo();
        if (Settings.DOT_FREE_DTG) {
            try {
                DotCreater.createFreeDtgDot(translatedSAS, "debug/freedtg/" + domainFile.getParentFile().getName() + "-free-dtg", true);
                SasFile sas = Downward.runPreprocess(translatedSAS);
                DotCreater.createCgDtgDots(sas, "debug/freedtg/" + domainFile.getParentFile().getName() + "-cg-dtg", true);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        reductionRatio = countReductionRatio();
    }
    
    public double getReductionRatio() {
        return reductionRatio;
    }

    private double countReductionRatio() {
        
        if (translatedSAS.isGoalSubsetOfInit()) {
            return 1;
        } else {
            double sizeBefore = translatedSAS.getOriginalNumberOfOperators()
                    + translatedSAS.getOriginalNumberOfValues();
            double sizeAfter = translatedSAS.getNumberOfActiveOperators()
                    + translatedSAS.getNumberOfActiveValues();
            return 1 - (sizeAfter / sizeBefore);
        }
    }


    
    /**
     * This method is called from reduce() to make reduction given while constructing this class or set by setUpReductions.
     */
    private void makeReductions() {
        if (Settings.DEBUG) {
            storeContentToFile("debug", "origin.text", translatedSAS.getCurrentSAS(true));
        }
        long timeMark = 0;
        boolean changed = true;

        if (Settings.DEBUG_DOT) {
            try {
                DotCreater.createDotAndStore(translatedSAS, "debug" + File.separator + timeMark + "_" + reverseOperations.size() + "_", Settings.DOT_GENERATE_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        while (changed) {
            changed = false;
            for (ReduceOperation operation : operations) {
                ReverseOperation reverse;
                operation.clearCache();

                long start = System.currentTimeMillis();
                reverse = operation.reduce(translatedSAS, changed);
                long time = System.currentTimeMillis() - start;
                updateReduceOperationTime(operation, time);
                while (null != reverse) {
                    changed = true;
                    updateReduceOperationCount(operation, reverse);

                    if (Settings.DEBUG_DOT) {
                        try {
                            DotCreater.createDotAndStore(translatedSAS, "debug" + File.separator + timeMark + "_" + reverseOperations.size() + "_" + reverse, Settings.DOT_GENERATE_FILE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    reverseOperations.push(reverse);
                    if (goalCheckSetAndSuccessful()) {
                        break;
                    }

                    start = System.currentTimeMillis();
                    reverse = operation.reduce(translatedSAS, changed);
                    time = System.currentTimeMillis() - start;
                    updateReduceOperationTime(operation, time);
                }
                if (Settings.DEBUG) {
                    storeContentToFile("debug", timeMark + "_" + operation.getClass() + ".txt", translatedSAS.getCurrentSAS(true));
                }

                if (goalCheckSetAndSuccessful()) {
                    break;
                }
            }

            if (goalCheckSetAndSuccessful()) {
                break;
            }
            /*if (Settings.DEBUG) {
                System.out.println("\ndebug\t" + timeMark);
                if (timeMark > 50) {
                    System.out.println("DEBUG WATCHDOG");
                    break;
                }
            }*/
            timeMark++;
        }

        if (Settings.DEBUG_DOT_FINAL) {
            try {
                DotCreater.createDotAndStore(translatedSAS, "debug" + File.separator + timeMark + "_" + reverseOperations.size() + "_", Settings.DOT_GENERATE_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*System.out.println();
        System.out.println("active ones");

        for (Operator operator : translatedSAS.getOperators().values()) {
            if (operator.isActive()) {
                System.out.println(operator.getName() + "\t" + operator.getCurrentCanonicalString());
            }
        }*/


        if (Settings.DEBUG) {
            storeContentToFile("debug", "final.txt", translatedSAS.getCurrentSAS(true));
            storeContentToFile("debug", "reduced_final.txt", translatedSAS.getFinalSAS());
            System.out.println("complete in " + timeMark + " iterations");
        }
    }

    private boolean goalCheckSetAndSuccessful() {
        long start = System.currentTimeMillis();
        boolean isGoal = translatedSAS.isGoalSubsetOfInit();
        goalCheckTimeMs += System.currentTimeMillis() - start;
        if (checkEnd && isGoal) {
            return true;
        }
        return false;
    }

    private void updateReduceOperationCount(ReduceOperation operation, ReverseOperation reverse) {
        Integer count = reductionCount.get(operation);
        if (count != null) {
            reductionCount.put(operation, count + reverse.getNumberOfReductions());
        } else {
            reductionCount.put(operation, reverse.getNumberOfReductions());
        }
    }

    private void updateReduceOperationTime(ReduceOperation operation, long time) {
        Long oldTime = reductionTimesMs.get(operation);
        if (oldTime != null) {
            reductionTimesMs.put(operation, oldTime + time);
        } else {
            reductionTimesMs.put(operation, time);
        }
    }

    private void storeContentToFile(String dirName, String fileName, String sasContent) {
        PrintWriter storage = null;
        try {
            File file = new File(dirName, fileName);
            storage = new PrintWriter(file, "UTF-8");
            storage.print(sasContent);
            storage.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            storage.close();
        }
    }


    /**
     * 3rd phase
     */
    public void executePlanner() {
        long groundTime = System.currentTimeMillis();
        makePlanning();
        planningTime = System.currentTimeMillis() - groundTime;
    }

    private void makePlanning() {
        if (translatedSAS.isGoalSubsetOfInit() || Settings.REDUCTION_WITHOUT_PLANNING) {
            plan = new Plan();
        } else {
            if (Settings.DEBUG) {
                System.out.println("active goals\t\t" + translatedSAS.getNumberOfActiveGoals() + " vs " + translatedSAS.getOriginalNumberOfGoals());
                System.out.println("active operators\t" + translatedSAS.getNumberOfActiveOperators() + " vs " + translatedSAS.getOriginalNumberOfOperators());
                System.out.println("active variables\t" + translatedSAS.getNumberOfActiveVariables() + " vs " + translatedSAS.getOriginalNumberOfVariables());
            }

            try {
                plan = Downward.runDownwardWithReducedSas(translatedSAS, problemName);
            } catch (InterruptedException e) {
                System.err.println("Downward: Interrupted (" + e.getMessage() + ").");
                e.printStackTrace();
            }
        }
    }

    /**
     * 4th phase
     *
     * @return
     */
    public Plan recoverPlan() {
        long groundTime = System.currentTimeMillis();
        makePlanConsistent();
        recoveryTime = System.currentTimeMillis() - groundTime;
        return plan;
    }

    public void makePlanConsistent() {
        while (!reverseOperations.empty()) {
            ReverseOperation reverse = reverseOperations.pop();
            if (Settings.DEBUG) {
                System.out.println(reverse.getClass());
            }
            if (!Settings.REDUCTION_WITHOUT_PLANNING) {
                reverse.extend(plan, translatedSAS);
            }
        }
    }

    /**
     *
     * TODO actualize parameter section
     * Sets up setting of PsmSasPlanner, so that given reduction will be used.
     * String reductions can take combination of following values: -mv for using merging values reduction, -dv for using delete variable reduction, -ud for deleting unused values, -sd for merging operators because of simple dependencies, -mp for pruning values with respect to mutex, -mo for merging similar operators, -mw for merging values of possible different variables, -de for pruning dead ends, -hc for half cycle, -ou for one usage reduction, -oe for one effect reduction, -ac for action start merge, -sf for application of action in start, -ai for execution of independent actions in start. Add -ch for checking if goal state is a subset of initial state and stopping reduction when they are not necessary.
     *
     * @param upReductions
     */
    public void setUpReductions(String upReductions) {
        List<ReduceOperation> list = new LinkedList<>();

        String[] splitted = upReductions.split(" ");
        for (String parameter : splitted) {
            if (parameter.trim().equals("-ch")) {
                checkEnd = true;
            }else{
                if(reverseOperationMap.containsKey(parameter.trim())){
                    list.add(reverseOperationMap.get(parameter.trim()));
                }else{
                    System.out.println("unknow parameter '"+parameter+"'");
                }
            }
        }

        operations.clear();
        operations.addAll(list);
    }

    /**
     * Execute all 4 phases of the planner.
     *
     * @return
     */
    public Plan makeAndGetPlan() {
        if (translatedSAS == null) {
            translate();
        }
        if (Settings.DEBUG) System.out.printf("FD translate time:\t%.02fs%n", getTranslationTime() / 1000.0);

        if (translatedSAS.getOperators().values().size() < 1) {
            return new Plan(true);
        }

        reduce();
        if (Settings.DEBUG) System.out.printf("reduction time:\t%.02fs%n", getReductionTime() / 1000.0);

        executePlanner();
        if (Settings.DEBUG) System.out.printf("FD planning time:\t%.02fs%n", getPlanningTime() / 1000.0);

        Plan solution = recoverPlan();
        if (Settings.DEBUG) System.out.printf("recovery time:\t%.02fs%n", getRecoveryTime() / 1000.0);

        return solution;
        //return null;
    }

    public String getStoredInfo() {
        return storedInfo;
    }

    public String getReductionInfo(boolean htmlFormat) {
        StringBuilder info = new StringBuilder("Reductions executed:");
        if (htmlFormat) {
            info.append("<table border=1 style=\"width:100%\">");
        }
        for (ReduceOperation reduceOperation : operations) {
            // replace by reduceOperation.getShortcut()
            Integer count = reductionCount.get(reduceOperation);
            if (count == null) {
                count = 0;
            }
            Long time = reductionTimesMs.get(reduceOperation);
            if (time == null) {
                time = 0l;
            }
            if (htmlFormat) {
                info.append("<tr><td>");
            }
            info.append(reduceOperation.getClass().getSimpleName());
            if (htmlFormat) {
                info.append(String.format("</td><td>%d</td><td>%.02fs</td></tr>", count, time / 1000.0));
            } else {
                info.append(String.format(" %d (%.02fs) / ", count, time / 1000.0));
            }
        }
        if (htmlFormat) {
            info.append(String.format("<tr><td>GoalCheck</td><td></td><td>%.02fs</td></rd>", goalCheckTimeMs / 1000.0));
            info.append("</table>");
        } else {
            info.append(String.format("GoalCheck (%.02fs) ", goalCheckTimeMs / 1000.0));
        }


        return info.toString();
//        return "Reductions executed: -mv " + valueSplit + " / " + bigValueSplit + ", -dv " + variableResurrection + ", -uv " + valueResurrection + ", -sd " + operatorsExpand + ", -mo " + mergedOperators + ", -ou " + splitUsage + ", -de " + deadEnds + ", -hc " + halfCycle + ", -oe " + addEffect + ", -as " + actionStart + ". ";
    }
}
