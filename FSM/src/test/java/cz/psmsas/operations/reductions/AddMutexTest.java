package cz.psmsas.operations.reductions;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.psmsas.operations.reductions.AddMutex;
import cz.agents.dimap.psmsas.operations.reductions.GroundAnyValue;
import cz.agents.dimap.psmsas.operations.reductions.SimpleTunnelMacro;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;
import cz.agents.dimap.tools.sas.Variable.Value;

public class AddMutexTest extends SasTest {

    public AddMutexTest() {
        super("addMutex", new AddMutex());
    }
    
    @Test
    public void testAirport00() throws IOException {
        SasFile sas = runTest("airport00.sas", null, true);
        areMutexed(sas, 0, 0, 12, 1, true);
        areMutexed(sas, 1, 1, 7, 0, false);
        areMutexed(sas, 15, 0, 7, 0, false);
    }

    @Test
    public void testAirport00_1() throws IOException {
        SasFile sas = runTest("airport00-1.sas", null, true);
        areMutexed(sas, 0, 0, 12, 1, true);
        areMutexed(sas, 1, 1, 7, 0, false);
        areMutexed(sas, 15, 0, 7, 0, false);

    }

    private void areMutexed(SasFile sas, int var1, int val1, int var2, int val2, boolean areMutexed) throws IOException {
        Value value1 = sas.variables.get(var1).getValueOriginal(val1);
        Value value2 = sas.variables.get(var2).getValueOriginal(val2);
        assertEquals(areMutexed, sas.areMutexed(value1, value2));
        assertEquals(areMutexed, sas.areMutexed(value2, value1));
    }

    @Test
    public void testAirport00InitMutexes() throws IOException {
        SasFile sas = runTest("airport00.sas", null, true);
        for (Variable var1 : sas.getVariables().values()) {
            for (Variable var2 : sas.getVariables().values()) {
                if (var1 != var2) {
                    if (sas.areMutexed(var1.getInitValue(), var2.getInitValue())) {
                        fail(var1.getInitValue() + " - " + var2.getInitValue());
                    }
                }
            }
        }
    }

    @Test
    public void testAirport00Again() throws IOException {
        runTest("airport00.sas", null, true);
        runReduction(false);
    }
}
