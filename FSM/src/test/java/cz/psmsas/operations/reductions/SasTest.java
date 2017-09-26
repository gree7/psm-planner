package cz.psmsas.operations.reductions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import cz.agents.dimap.psmsas.operations.ReduceOperation;
import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.tools.dot.DotCreater;
import cz.agents.dimap.tools.sas.SasFile;

public class SasTest {

    private final static String TEST_DIR = "sas" + File.separator + "reductions" + File.separator;
    private final boolean MAKE_DOT = false;
    private final String DOT_DIR = "." + File.separator + "debug" + File.separator + "test";

    protected String dir;
    private ReduceOperation reduction;
    
    int counter = 0;
    private SasFile sas;
    private String input;
    private String output;
    private ReverseOperation reverse;
    
    protected SasTest(String dir, ReduceOperation reduction) {
        this.reduction = reduction;
        this.dir = TEST_DIR + dir + File.separator;

        File dotDir = new File(DOT_DIR);
        if (!dotDir.exists()) {
            dotDir.mkdirs();
        }
    }

    protected SasFile runTest(String input, String output, boolean isReducible) throws IOException {
        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(dir + input);

        sas = new SasFile(inputStream);

        if (MAKE_DOT) {
            DotCreater.createDotFromFile(sas, DOT_DIR + File.separator + input + "_", true);
            if (output != null) {
                inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(dir + output);
                DotCreater.createDotFromFile(new SasFile(inputStream), DOT_DIR + File.separator + output + "_out", true);
            }
        }
       
        this.input = input;
        this.output = output;
        return runReduction(isReducible);
    }

    protected SasFile runReduction(boolean isReducible) throws IOException {
        reverse = reduction.reduce(sas);
        String finalSasString = sas.getFinalSAS();

        if (MAKE_DOT) {
            DotCreater.createDotFromFile(sas, DOT_DIR + File.separator + input + "_generated_"+(counter++), true);
        }

        if (isReducible) {
            assertNotNull("There is one reduction in this case, so null should not be returned.", reverse);
        } else {
            assertNull("There is no reduction in this case, so null should be returned.", reverse);
        }
        if (output != null) {
            assertSas(finalSasString, dir, output);
        }
        return sas;
    }

    protected ReverseOperation getLastReverseOperation() {
        return reverse;
    }
    
    private static void assertSas(String sas, String dir, String file) throws IOException {

        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(dir + file);

        BufferedReader brGround = new BufferedReader(new InputStreamReader(inputStream));
        BufferedReader brProduced = new BufferedReader(new StringReader(sas));
        String ground, produced;
        while ((ground = brGround.readLine()) != null) {
            produced = brProduced.readLine();
            assertNotNull("Produced SAS is shorter.", produced);

            /*
            System.out.println(ground);
            System.out.println(produced);
            System.out.println("");
            /**/

            assertEquals(ground, produced);
        }
        produced = brProduced.readLine();
        assertNull("Produced SAS is longer.", produced);
    }


}
