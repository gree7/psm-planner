package experiments.ipc;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import cz.agents.dimap.psmsas.PsmSasPlanner;
import cz.agents.dimap.tools.GenericResult;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.Validator;
import cz.agents.dimap.tools.sas.Operator;

public class IpcPsmSasBenchmarksSas extends IpcBenchmarksCli {


    private static void runAll() {
        
        //-sd  -- !!! very slow
//        String reductions = "-mv -dv -as -mo -mp -de -sf";//udealat check na konec -mo";// -de"; //  -uv -sd -mp -mo
        // -ou broken
//        String reductions = "-sf -bv -dv -uv -mo -mp  -oe -ch -de -hc -as";
//        String reductions = "-bv -dv -uv -mo -oe -ch -de -sf -ga";
//        String reductions = "-bv -hc -dv -uv -mo -oe -ch -de -do -gr -as -sf -ga -am -gv -tm";
//      String reductions = "-bv -dv -uv -mo -sd -oe -ch -de -hc -ga";
//      String reductions = "-bv -dv ";
//      String reductions = "-hc -dv -uv -ga -ch";
        String reductions = "-bv -hc -tn -dv -uv -mo -oe -ch -de -do -gr -as -sf -ga -am -gv"; 
    
//        PsmSasPlanner psmSasPlanner = new PsmSasPlanner(new File(BENCHMARKS_DIR+"zenotravel2/output.sas"), "binadd2-2", reductions);
//        PsmSasPlanner psmSasPlanner = new PsmSasPlanner(new File(BENCHMARKS_DIR+"sas/gripper01.sas"), "gripper01", reductions);
//        PsmSasPlanner psmSasPlanner = new PsmSasPlanner(new File(BENCHMARKS_DIR+"sas/gripper000.sas"), "gripper0", reductions);
//        PsmSasPlanner psmSasPlanner = new PsmSasPlanner(new File(BENCHMARKS_DIR+"sas/rovers02.sas"), "rovers02", reductions);
        PsmSasPlanner psmSasPlanner = new PsmSasPlanner(new File(BENCHMARKS_DIR+"sas/reduced.sas"), "reduced", reductions);
        Plan solution = psmSasPlanner.makeAndGetPlan();

        System.out.println("Solution length: " + solution.getSize());
        System.out.println(solution.getPddlPlan());

        GenericResult planResult = new GenericResult();
        planResult.planningTime = psmSasPlanner.getComputingtime();
        planResult.solved = true;

        planResult.planningInfo = "Run with parameters: '" + reductions + "'." +
                psmSasPlanner.getStoredInfo() +
                psmSasPlanner.getReductionInfo(false) +
                "Plan length: " + solution.getSize() + ". " +
                "Times: translate " + psmSasPlanner.getTranslationTime() + ", " +
                "reduction " + psmSasPlanner.getReductionTime() + ", " +
                "planning " + psmSasPlanner.getPlanningTime() + ", " +
                "recovery " + psmSasPlanner.getRecoveryTime() + ". " +
                "Validation processed: " + "valid" + ".";

        System.out.println(planResult.planningInfo);
    }

    public IpcPsmSasBenchmarksSas() {
    }

    public static void main(String[] args) throws Exception {
        runAll();
    }

    @Override
    public GenericResult runExperiment(File domain, File problem) {
        return null;
    }
}
