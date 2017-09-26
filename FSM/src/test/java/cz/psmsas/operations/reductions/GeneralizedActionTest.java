package cz.psmsas.operations.reductions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import cz.agents.dimap.psmsas.operations.reductions.GeneralizedAction;
import cz.agents.dimap.tools.sas.SasFile;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by pah on 25.8.15.
 */
public class GeneralizedActionTest extends SasTest {

    public GeneralizedActionTest() {
        super("decompileOperators", new GeneralizedAction());
    }

    @Test
    public void threeOperatorsToOne() throws IOException {
        SasFile sas = runTest("test1.input", null, true);
        assertEquals(1, sas.getNumberOfActiveOperators());
    }

    @Test
    public void noReductionOneProjectionMissing() throws IOException {
        runTest("noreduction.input", "noreduction.output", false);
    }

    @Test
    public void noReductionOnMissingOnDoubled() throws IOException {
        runTest("noreduction2.input", "noreduction2.output", false);
    }

    @Test
    public void generalizedOperatorAlreadyInside() throws IOException {
        runTest("reductionRedundant.input", "reductionRedundant.output", true);
    }

}
