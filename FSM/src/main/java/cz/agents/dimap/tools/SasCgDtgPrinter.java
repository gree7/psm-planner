package cz.agents.dimap.tools;

import java.io.File;
import java.io.IOException;

import cz.agents.dimap.tools.dot.DotCreater;
import cz.agents.dimap.tools.fd.Downward;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.Variable;

public class SasCgDtgPrinter {

//    private static final String SAS_FILE = "benchmarks/sas/gripper-01.sas";
    private static final String SAS_FILE = "benchmarks/sas/output";

    public static void main(String[] args) throws IOException {
        SasFile sas = new SasFile(new File(SAS_FILE));
        if (sas.dtgs == null) {
            sas = Downward.runPreprocess(sas);
        }
        DotCreater.createDtgDots(sas, SAS_FILE + "-dtg", true);
        DotCreater.createCgDot(sas, SAS_FILE + "-cg", true);
        DotCreater.createCgDtgDots(sas, SAS_FILE + "-cg-dtg", true);
        DotCreater.createFreeDtgDot(sas, SAS_FILE + "-free-dtg", true);
    }
}
