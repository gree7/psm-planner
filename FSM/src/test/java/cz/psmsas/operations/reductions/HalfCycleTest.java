package cz.psmsas.operations.reductions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reductions.HalfCycleNew;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.Operator.Effect;
import cz.agents.dimap.tools.sas.SasFile;

public class HalfCycleTest extends SasTest {

    public HalfCycleTest() {
        super("problems", new HalfCycleNew());
    }
    
    @Test
    public void test() throws IOException {
        SasFile sas = runTest("testHC.input", null, true);
        Operator unload = sas.operators.get("unload");
        Operator load = sas.operators.get("load");
        assertFalse(load.isActive());
        assertFalse(unload.isActive());
        assertTrue(sas.isGoalSubsetOfInit());
    }

    @Test
    public void testReverse() throws IOException {
        testReverseOperation("testHC.input");
    }

    private void testReverseOperation(String input) throws IOException {
        SasFile sas = runTest(input, null, true);
        ReverseOperation reverse = getLastReverseOperation();
        SasFile originalSas = new SasFile(ClassLoader.getSystemClassLoader().getResourceAsStream(dir + input));
        reverse.extend(new Plan(), sas);
        originalSas.getMutexes().clear();
        sas.getMutexes().clear();
        assertEquals(originalSas.getFinalSAS(), sas.getFinalSAS());
    }

    @Test
    public void test1() throws IOException {
        SasFile sas = runTest("testHC1.input", null, true);
        Operator unload = sas.operators.get("unload");
        Operator load = sas.operators.get("load");
        assertTrue(load.isActive());
        assertFalse(unload.isActive());
        assertEquals(1, load.getPreconditions().size());
        for (Effect effect : load.getEffects()) {
            assertFalse(effect.isActive());
        }
        runReduction(false);
    }

    @Test
    public void test1Reverse() throws IOException {
        testReverseOperation("testHC1.input");
    }

    @Test
    public void testRovers() throws IOException {
        SasFile sas = runTest("roversHC.input", null, true);
        Operator drop = sas.operators.get("drop rover0 rover0store");
        assertFalse(drop.isActive());
    }
    
    @Test
    public void testRoverseReverse() throws IOException {
        testReverseOperation("roversHC.input");
    }

    @Test
    public void testRoversSimple() throws IOException {
        SasFile sas = runTest("roversHC-simple.input", null, true);
        assertEquals(1, sas.getNumberOfActiveOperators());
        sas = runReduction(true);
        assertEquals(0, sas.getNumberOfActiveOperators());
        assertTrue(sas.isGoalSubsetOfInit());
    }

    @Test
    public void testRoverseSimpleReverse() throws IOException {
        testReverseOperation("roversHC-simple.input");
    }
}
