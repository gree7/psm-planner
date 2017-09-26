package cz.psmsas.operations.reductions;

import java.io.IOException;

import org.junit.Test;

import cz.agents.dimap.psmsas.operations.reductions.GroundSimpleAction;
import cz.agents.dimap.tools.sas.SasFile;

/**
 * Created by pah on 25.8.15.
 */
public class GroundSimpleActionTest extends SasTest {

    public GroundSimpleActionTest() {
        super("groundSimpleAction", new GroundSimpleAction());
    }

    @Test
    public void testSimple() throws IOException {
        runTest("groundSimpleAction.input", "groundSimpleAction.output", true);
        runReduction(false);
    }
}
