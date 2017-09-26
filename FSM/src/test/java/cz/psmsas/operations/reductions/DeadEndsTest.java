package cz.psmsas.operations.reductions;

import java.io.IOException;

import org.junit.Test;

import cz.agents.dimap.psmsas.operations.reductions.DeadEnds;

/**
 * Created by pah on 29.7.15.
 */
public class DeadEndsTest extends SasTest {

    public DeadEndsTest() {
        super("deadEnds", new DeadEnds());
    }
    
    @Test
    public void relaxedDelete() throws IOException {
        runTest("woodworking.input", "woodworking.output", false);
    }

    @Test
    public void removeOperatorLeadingToDeadEnd() throws IOException {
        runTest("removeDeadPathAction.input", "removeDeadPathAction.output", false);
    }

    @Test
    public void removeTwoOperators() throws IOException {
        runTest("oneDEWithoutSideEffectWithoutAnyValueWithMultipleActions.input", "oneDEWithoutSideEffectWithoutAnyValueWithMultipleActions.output", true);
    }

    @Test
    public void removeOneOperator() throws IOException {
        runTest("sharedUnusedValueOnePruneWithAnyValue.input", "sharedUnusedValueOnePruneWithAnyValue.output", true);
    }

    @Test
    public void redundantPrune() throws IOException {
        runTest("anyValueWithoutSideEffect.input", "anyValueWithoutSideEffect.output", true);
    }


    @Test
    public void soFarNotReverserable() throws IOException {
        // zatim nevim presne jak to invertovat, proto to ted neredukuju, pozdeji s vymyslenim redukce to chce napsat novy test
        // reseni je trivialni - akce zvednout, akce presun do pozice A
        // pripad any value with side effect
        runTest("tuzkaZvednuto.input", "tuzkaZvednuto.output", false);
    }

    @Test
    public void tuzkaAStul() throws IOException {
        runTest("tuzkaAStul.input", "tuzkaAStul.output", false);
    }

    @Test
    public void oneWithoutSideEffectWithoutAnyValue() throws IOException {
        runTest("oneDEWithoutSideEffectWithoutAnyValue.input", "oneDEWithoutSideEffectWithoutAnyValue.output", true);
    }

    @Test
    public void noDeadEnds() throws IOException {
        runTest("noDE.input", "noDE.output", false);
    }
}
