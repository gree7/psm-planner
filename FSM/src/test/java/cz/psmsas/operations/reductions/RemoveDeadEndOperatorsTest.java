package cz.psmsas.operations.reductions;

import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;

import cz.agents.dimap.psmsas.operations.reductions.RemoveDeadEndOperators;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;

public class RemoveDeadEndOperatorsTest extends SasTest {

    public RemoveDeadEndOperatorsTest() {
        super("removeDeadEndOperators", new RemoveDeadEndOperators());
    }
    
    @Test
    public void testAirport01() throws IOException {
        SasFile sas = runTest("airport01.sas", null, true);
        
        Operator op = sas.operators.get("takeoff_seg_rwe_0_50_south airplane_cfbeg");
        assertFalse(op.isActive());
    }
}
