package cz.psmsas.operations.reductions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import cz.agents.dimap.psmsas.operations.reductions.ActionStartMerge;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;
import cz.agents.dimap.tools.sas.Variable.Value;

public class TppProb05Test extends SasTest {

    public TppProb05Test() {
        super("problems", new ActionStartMerge());
    }
    
    @Test
    public void test() throws IOException {
        SasFile sas = runTest("tppProb05.input", null, true);
        assertFalse(sas.operators.get("load goods1 truck1 market1 level0 level1 level0 level1").isActive());
        assertTrue(sas.operators.get("unload goods1 truck1 depot1 level0 level1 level0 level1").isActive());
        assertEquals(2, getActiveValues(sas.variables.get(0)));
        System.out.println("sas.variables.get(1).getInitValue(): " + sas.variables.get(1).getInitValue());
        assertEquals(1, sas.variables.get(1).getInitValue().getRealID());
    }

    private int getActiveValues(Variable var) {
        int count = 0;
        for (Value value : var.getValues()) {
            if (value.isActive()) {
                count++;
            }
        }
        return count;
    }

}
