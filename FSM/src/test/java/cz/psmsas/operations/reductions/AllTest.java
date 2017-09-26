package cz.psmsas.operations.reductions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import cz.agents.dimap.psmsas.operations.ReduceOperation;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reductions.AddMutex;
import cz.agents.dimap.psmsas.operations.reductions.HalfCycleNew;
import cz.agents.dimap.psmsas.operations.reductions.MergeVariables;
import cz.agents.dimap.psmsas.operations.reductions.RemoveDeadEndOperators;
import cz.agents.dimap.psmsas.operations.reductions.ShrinkSimilarOperators;
import cz.agents.dimap.psmsas.operations.reductions.TunnelMacro;
import cz.agents.dimap.psmsas.operations.reductions.ValuesPruning;
import cz.agents.dimap.psmsas.operations.reductions.VariableDeletion;
import cz.agents.dimap.psmsas.operations.reverse.MultiReverseOperation;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.dot.DotCreater;
import cz.agents.dimap.tools.sas.MutexGroup;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.Operator.Effect;
import cz.agents.dimap.tools.sas.Operator.Precondition;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;
import cz.agents.dimap.tools.sas.Variable.Value;

public class AllTest extends SasTest {

    public AllTest() {
        super("problems", new HalfCycleNew());
    }

    @Test
    public void testOpenstacksReverse() throws IOException {
        testReverse("openstacksHC.input");
    }

    public void testReverse(String input) throws IOException {
        SasFile sas = new SasFile(ClassLoader.getSystemClassLoader().getResourceAsStream(dir + input));

        MultiReverseOperation multiReverse = new MultiReverseOperation("test");
        
        MergeVariables mergeVariables = new MergeVariables();
        List<? extends ReduceOperation> reductions = Arrays.asList(new ReduceOperation[] {
                new HalfCycleNew(),
                new TunnelMacro(),
                new VariableDeletion(),
                new ShrinkSimilarOperators(),
                new RemoveDeadEndOperators(),
                new AddMutex(),
                new ValuesPruning(),
                mergeVariables,
                new ValuesPruning()
        });
        testEffects(sas);
        
        List<String> allSases = new ArrayList<>();
        allSases.add(sas.getFinalSAS());
        boolean changed = true;
        while (changed) {
            changed = false;
            for (ReduceOperation reduceOperation : reductions) {
                ReverseOperation reverse = reduceOperation.reduce(sas, false);
                while (reverse != null) {
                    allSases.add(sas.getFinalSAS());
                    
                    testVariables(sas);
                    testEffects(sas);

                    changed = true;
                    multiReverse.addReverseOperation(reverse);

                    reverse = reduceOperation.reduce(sas, changed);
                }
            }
        }

        SasFile originalSas = new SasFile(ClassLoader.getSystemClassLoader().getResourceAsStream(dir + input));
        Plan plan = new Plan();
        assertTrue(sas.isGoalSubsetOfInit());
        checkPlanConsistency(plan, sas);
        
//        multiReverse.extend(plan, sas);
        int sasIndex = allSases.size() - 2;
        for (ReverseOperation reverse : multiReverse.getReverseOperations()) {
            reverse.extend(plan, sas);
            
            checkPlanConsistency(plan, sas);

            String finalSAS = sas.getFinalSAS();
            assertEquals(allSases.get(sasIndex), finalSAS);
            sasIndex--;
        }
        
        originalSas.getMutexes().clear();
        sas.getMutexes().clear();
        assertEquals(originalSas.getFinalSAS(), sas.getFinalSAS());
    }

    private void checkPlanConsistency(Plan plan, SasFile sas) {
        Map<Variable, Value> currentState = new HashMap<>();
        for (Variable var : sas.getVariables().values()) {
            if (var.isActive()) {
                currentState.put(var, var.getInitValue().getUpMostParent());
            }
        }

        for (Operator operator : plan.getPlan()) {
            for (Precondition precondition : operator.getPreconditions()) {
                if (precondition.isActive()) {
                    assertEquals(precondition.getVariable().toString(), currentState.get(precondition.getVariable()), precondition.getValue().getUpMostParent());
                }
            }
            for (Effect effect : operator.getEffects()) {
                if (effect.isActive()) {
                    assertEquals(operator.toString(), currentState.get(effect.getVariable()), effect.getOldValue().getUpMostParent());
                    currentState.put(effect.getVariable(), effect.getNewValue().getUpMostParent());
                }
            }
        }
        
        for (Entry<Variable, Value> entry : currentState.entrySet()) {
            Variable variable = entry.getKey();
            if (variable.isInGoal()) {
                assertEquals(variable.getGoalValue().getUpMostParent(), entry.getValue().getUpMostParent());
            }
        }
    }

    private void testVariables(SasFile sas) {
        for (Variable variable : sas.getVariables().values()) {
            if (variable.isActive()) {
                assertTrue(variable.getNumberOfActiveValues() > 1);
            }
        }
    }

    private void testEffects(SasFile sas) {
        for (Operator operator : sas.getOperators().values()) {
            if (operator.isActive()) {
                for (Effect effect : operator.getEffects()) {
                    if (effect.isActive()) {
                        assertFalse(operator.getName(), effect.getOldValue().getUpMostParent() == effect.getNewValue().getUpMostParent());
                    }
                }
            }
        }
    }
    
    static public int getNumberOfActiveMutexes(SasFile sasFile) {
        int numOfMutexes = 0;
        for (MutexGroup mutex : sasFile.getMutexes()) {
            if (mutex.isActive()) {
                numOfMutexes++;
            }
        }
        return numOfMutexes;
    }


}
