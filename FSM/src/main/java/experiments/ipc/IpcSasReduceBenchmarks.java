package experiments.ipc;

import cz.agents.dimap.psmsas.PsmSasPlanner;
import cz.agents.dimap.tools.GenericResult;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.Settings;
import cz.agents.dimap.tools.Validator;

import java.io.File;
import java.io.IOException;

/**
 * Created by pah on 21.7.15.
 */
public class IpcSasReduceBenchmarks extends IpcBenchmarksCli {
    @Override
    public GenericResult runExperiment(File domain, File problem) {
        //String reductions = "-bv -dv -uv -mo -oe -ch -de -sf -ou -ga";
        //String reductions = "-bv -dv -uv -mo -oe -ch -de -hc -as -sf -ga";
    	//String reductions = "-bv -dv -uv -mo -ch -de -hc -as -sf -ga";
        
//      String reductions = "-bv -hc -dv -uv -mo -oe -ch -de -gr -as -sf -ga"; // AAAI-16
//        String reductions = "-hc -dv -uv -mo -oe -ch -de -do -gr -as -sf -ga -am -gv -tm"; // ICAPS-16
//        String reductions = "-bv -dv -uv -mo -oe -ch -de -do -gr -as -sf -ga -am -gv -tm"; // ICAPS-16
//        String reductions = "-bv -hc -dv -uv -mo -oe -ch -de -do -gr -as -sf -am -gv -tm"; // ICAPS-16
//        String reductions = "-bv -hc -dv -uv -mo -oe -ch -de -do -gr -as -sf -ga -am -gv"; // ICAPS-16
//        String reductions = "-bv -hc -dv -uv -mo -oe -ch -de -do -gr -as -sf -ga -am -gv -tm"; // ICAPS-16
        String reductions = "-bv -hc -dv -uv -mo -oe -ch -de -do -gr -as -sf -ga -am -gv -tn -mg";
        return runExperiment(domain, problem, reductions);
    }
    
    public GenericResult runExperiment(File domain, File problem, String reductions) {
        PsmSasPlanner psmSasPlanner = new PsmSasPlanner(domain, problem, reductions);
        
        if (Settings.COUNT_REDUCTIONS_ONLY) {

            psmSasPlanner.translate();
            psmSasPlanner.reduce();
            
            GenericResult planResult = new GenericResult();
            
            planResult.planningInfo = "Run with parameters: '" + reductions + "'." +
                    "Reduction: " + psmSasPlanner.getReductionRatio()
                    + " " + psmSasPlanner.getStoredInfo();
            
            return planResult;
        }
        
        Plan solution = psmSasPlanner.makeAndGetPlan();

        GenericResult planResult = new GenericResult();
        planResult.planningTime = psmSasPlanner.getComputingtime();

        if (solution.isUnsat()){
            planResult.solved = true;
            planResult.planningInfo = "This instance is unsatisfiable.";
            return planResult;
        }

        boolean validationResult = true;
        if (Settings.VALIDATE) {
            try {
                validationResult = Validator.validate(solution, domain, problem);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Solution length: " + solution.getSize());
        System.out.println(solution.getPddlPlan());

        planResult.solved = validationResult;
        planResult.planningInfo = "Run with parameters: '" + reductions + "'." +
                " Reduction: " + String.format("%.2f", psmSasPlanner.getReductionRatio()) +
                psmSasPlanner.getStoredInfo() +
                psmSasPlanner.getReductionInfo(true) +
                "Plan length: " + solution.getSize() + ". " +
                "Plan cost: " + solution.getCost() + ". " +
                "Times: translate " + psmSasPlanner.getTranslationTime() + ", " +
                "reduction " + psmSasPlanner.getReductionTime() + ", " +
                "planning " + psmSasPlanner.getPlanningTime() + ", " +
                "recovery " + psmSasPlanner.getRecoveryTime() + ", " +
                "computing time: " + psmSasPlanner.getComputingtime() + ". " +
                "Validation processed: " + ( validationResult ? "valid" : "inconsistent" ) + ".";

        return planResult;
    }
    
    public static void main(String[] args) {
		new IpcSasReduceBenchmarks().runExperimentCli(args);
	}
    
}
