package cz.psmsas.operations.reductions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import cz.agents.dimap.psmsas.operations.reductions.SingleFirstActionMergeWithStart;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.sas.SasFile;

public class SingleFirstActionMergeWithStartTest extends SasTest {

    public SingleFirstActionMergeWithStartTest() {
        super("problems", new SingleFirstActionMergeWithStart());
    }

    @Test
    public void testWithoutTransformations() throws IOException {
        Settings.ALLOW_TRANSFORMATIONS = false;
        runTest("testSF.input", null, false);
    }
    
    @Test
    public void test() throws IOException {
        Settings.ALLOW_TRANSFORMATIONS = true;
        SasFile sas = runTest("testSF.input", null, true);
        assertTrue(sas.operators.get("load").isActive());
        assertEquals("Atom unloaded", sas.variables.get(0).getInitValue().getValue());

        runReduction(true);

        assertFalse(sas.operators.get("unload").isActive());
        assertEquals("Atom loaded", sas.variables.get(0).getInitValue().getValue());

        runReduction(true);

        assertFalse(sas.operators.get("load").isActive());
        assertEquals("Atom unloaded", sas.variables.get(0).getInitValue().getValue());

        runReduction(true);

        assertFalse(sas.operators.get("finish").isActive());
        assertEquals("Atom done", sas.variables.get(2).getInitValue().getValue());
    }
}
