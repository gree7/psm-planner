package cz.psmsas.operations.reductions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reductions.AddMutex;
import cz.agents.dimap.psmsas.operations.reductions.GroundAnyValue;
import cz.agents.dimap.psmsas.operations.reductions.SimpleTunnelMacro;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.Operator.Effect;
import cz.agents.dimap.tools.sas.Operator.Precondition;
import cz.agents.dimap.tools.sas.SasFile;

public class GroundAnyValueTest extends SasTest {

    public GroundAnyValueTest() {
        super("problems", new GroundAnyValue());
    }

    @Test
    public void test() throws IOException {
        SasFile sas = runTest("testMacrosGripper-1.input", null, true);
        assertEquals(4, sas.getNumberOfActiveOperators());
        Operator opA = sas.operators.get("drop ball1 rooma left");
        assertTrue(opA.isActive());
        boolean tested = false;
        for (Effect effect : opA.getEffects()) {
            if (effect.getVariable().getOriginalId() == 0) {
                assertEquals(2, effect.getOldValue().getUpMostParent().getRealID());
                tested = true;
            }
        }
        assertTrue(tested);

        Operator opB = sas.operators.get("drop ball1 roomb left");
        tested = false;
        for (Effect effect : opB.getEffects()) {
            if (effect.getVariable().getOriginalId() == 0) {
                assertEquals(2, effect.getOldValue().getUpMostParent().getRealID());
                tested = true;
            }
        }
        assertTrue(tested);
    }

    @Test
    public void testGripper01() throws IOException {
        SasFile sas = runTest("gripper01.sas", null, true);
        assertEquals(60, sas.getNumberOfActiveOperators());
        Operator opA = sas.operators.get("drop ball1 rooma left");
        assertTrue(opA.isActive());
        boolean tested = false;
        for (Effect effect : opA.getEffects()) {
            if (effect.getVariable().getOriginalId() == 0) {
                assertEquals(3, effect.getOldValue().getUpMostParent().getRealID());
                tested = true;
            }
        }
        assertTrue(tested);

        Operator opB = sas.operators.get("drop ball1 roomb left");
        tested = false;
        for (Effect effect : opB.getEffects()) {
            if (effect.getVariable().getOriginalId() == 0) {
                assertEquals(3, effect.getOldValue().getUpMostParent().getRealID());
                tested = true;
            }
        }
        assertTrue(tested);
    }

    @Test
    public void testGripper01Combined() throws IOException {
        SasFile sas = runTest("gripper01.sas", null, true);
        ReverseOperation reverse = new SimpleTunnelMacro().reduce(sas);
        assertNotNull(reverse);
        reverse = new SimpleTunnelMacro().reduce(sas);
        assertNotNull(reverse);
        reverse = new SimpleTunnelMacro().reduce(sas);
        assertNotNull(reverse);
        reverse = new SimpleTunnelMacro().reduce(sas);
        assertNotNull(reverse);
        reverse = new SimpleTunnelMacro().reduce(sas);
        assertNotNull(reverse);
        reverse = new SimpleTunnelMacro().reduce(sas);
        assertNull(reverse);
        reverse = new GroundAnyValue().reduce(sas);
        assertNull(reverse);
        reverse = new SimpleTunnelMacro().reduce(sas);
        assertNull(reverse);
    }

    @Test
    public void testGripper00() throws IOException {
        SasFile sas = runTest("gripper00.sas", null, true);

        Operator opA = sas.operators.get("drop ball1 rooma left");
        assertTrue(opA.isActive());
        boolean tested = false;
        for (Effect effect : opA.getEffects()) {
            if (effect.getVariable().getOriginalId() == 0) {
                assertEquals(2, effect.getOldValue().getUpMostParent().getRealID());
                tested = true;
            }
        }
        assertTrue(tested);
    }
    
    @Test
    public void testGripper00Combined() throws IOException {
        SasFile sas = runTest("gripper00.sas", null, true);
        ReverseOperation reverse = new SimpleTunnelMacro().reduce(sas);
        assertNotNull(reverse);
        reverse = new SimpleTunnelMacro().reduce(sas);
        assertNull(reverse);

        runReduction(false);
        
        Operator op = sas.operators.get("drop ball1 roomb left");
        assertFalse(op.isActive());

        op = sas.operators.get("pick ball1 roomb left-drop ball1 roomb left");
        assertNull(op);

        op = sas.operators.get("pick ball1 roomb left-drop ball1 roomb right");
        assertNull(op);

        op = sas.operators.get("pick ball1 rooma right-drop ball1 roomb right");
        assertNotNull(op);
        assertTrue(op.isActive());
    }

    @Test
    public void testAirport00_1() throws IOException {
        SasFile sas = runTest("airport00-1.sas", null, true);
        Operator op = sas.operators.get("move_seg_twe2_0_50_seg_twe3_0_50_south_south_medium airplane_cfbeg");
        
        boolean tested = false;
        for (Effect effect : op.getEffects()) {
            if (effect.getVariable().getOriginalId() == 4) {
                assertTrue(effect.toString(), effect.getOldValue().getUpMostParent().isAnyValue());
                tested = true;
            }
        }
        assertTrue(tested);
        
        op = sas.operators.get("move_seg_ppdoor_0_40_seg_pp_0_60_south_south_medium airplane_cfbeg");
        tested = false;
        for (Effect effect : op.getEffects()) {
            if (effect.getVariable().getOriginalId() == 4) {
                System.out.println("effect: " + effect);
                assertTrue(effect.getOldValue().getUpMostParent().isAnyValue());
                tested = true;
            }
        }
        assertTrue(tested);
        
    }
}
