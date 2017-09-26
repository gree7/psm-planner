package cz.psmsas.operations.reductions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import cz.agents.dimap.psmsas.operations.reductions.SimpleTunnelMacro;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;

public class SimpleTunnelMacroTest extends SasTest {

    public SimpleTunnelMacroTest() {
        super("problems", new SimpleTunnelMacro());
    }

    @Test
    public void test() throws IOException {
        SasFile sas = runTest("testMacros.input", null, true);
        assertEquals(2, sas.getNumberOfActiveOperators());
        Operator opAB = sas.operators.get("pick room-A-drop room-B");
        assertTrue(opAB.isActive());
        Operator opBA = sas.operators.get("pick room-B-drop room-A");
        assertTrue(opBA.isActive());
        assertEquals(2, sas.variables.get(0).getNumberOfActiveValues());
    }

    @Test
    public void testGripper0() throws IOException {
        SasFile sas = runTest("testMacrosGripper0.input", null, true);
        assertEquals(2, sas.getNumberOfActiveOperators());
        assertEquals(2, sas.variables.get(0).getNumberOfActiveValues());
    }

    @Test
    public void testGripperAnyValue() throws IOException {
        runTest("testMacrosGripper-1.input", null, false);
    }

    @Test
    public void testGripper01() throws IOException {
        runTest("gripper01-grounded.sas", null, true);
        runReduction(true);
        runReduction(true);
        runReduction(true);
        runReduction(true);
        runReduction(false);
    }

    @Test
    public void testGripper00() throws IOException {
        runTest("gripper00-grounded.sas", null, true);
        runReduction(false);
    }

    public void testFloortile00() throws IOException {
        runTest("floortile00.sas", null, false);
    }
}
