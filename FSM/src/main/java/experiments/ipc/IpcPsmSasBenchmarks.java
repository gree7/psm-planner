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

public class IpcPsmSasBenchmarks extends IpcBenchmarksCli {


    public GenericResult runExperiment(File domain, File problem) {
        
        //-sd  -- !!! very slow
//        String reductions = "-mv -dv -as -mo -mp -de -sf";//udealat check na konec -mo";// -de"; //  -uv -sd -mp -mo
        // -ou broken
//        String reductions = "-sf -bv -dv -uv -mo -mp  -oe -ch -de -hc -as";
//        String reductions = "-bv -dv -uv -mo -oe -ch -de -sf -ga";
//        String reductions = "-bv -hc -dv -uv -mo -oe -ch -de -do -gr -as -sf -ga -gv -tm";
//      String reductions = "-bv -dv -uv -mo -sd -oe -ch -de -hc -ga";
//      String reductions = "-hc";
//      String reductions = "-bv -dv -ga";
//      String reductions = "-bv -hc -dv -uv -mo -oe -ch -de -do -gr -as -sf -ga -am -gv -tm"; 
        // -hc is needed when - merging with init; when partial merging with free DTG action
//      String reductions = "-am";
        String reductions = "-bv -hc -tn -dv -uv -mo -oe -ch -de -do -gr -as -sf -ga -am -gv -mg";
//        String reductions = "-bv -hc -tn -dv -uv -mo -oe -ch -de -do -gr -as -sf -ga -am -gv -mg";
//        String reductions = "-bv -dv -mg";
      
        PsmSasPlanner psmSasPlanner = new PsmSasPlanner(domain, problem, reductions);
        
        if (Settings.COUNT_REDUCTIONS_ONLY) {

            psmSasPlanner.translate();
            psmSasPlanner.reduce();
            
            GenericResult planResult = new GenericResult();
            
            planResult.planningInfo = "Run with parameters: '" + reductions + "'." +
                    "Reduction: " + psmSasPlanner.getReductionRatio()
                    + " " + psmSasPlanner.getStoredInfo() + psmSasPlanner.getReductionInfo(false);
            
            return planResult;
        }

        Plan solution = psmSasPlanner.makeAndGetPlan();

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
        System.out.println("Solution cost: " + solution.getCost());
        System.out.println(solution.getPddlPlan());

        GenericResult planResult = new GenericResult();
        planResult.planningTime = psmSasPlanner.getComputingtime();
        planResult.solved = validationResult;

        planResult.planningInfo = "Run with parameters: '" + reductions + "'." +
                " Reduction: " + String.format("%.2f", psmSasPlanner.getReductionRatio()) +
                psmSasPlanner.getStoredInfo() +
                psmSasPlanner.getReductionInfo(false) +
                "Plan length: " + solution.getSize() + ". " +
                "Plan cost: " + solution.getCost() + ". " +
                "Times: translate " + psmSasPlanner.getTranslationTime() + ", " +
                "reduction " + psmSasPlanner.getReductionTime() + ", " +
                "planning " + psmSasPlanner.getPlanningTime() + ", " +
                "recovery " + psmSasPlanner.getRecoveryTime() + ". " +
                "Validation processed: " + ( validationResult ? "valid" : "inconsistent" ) + ".";

        System.out.println(planResult.planningInfo);

        return planResult;
        //return new GenericResult();
    }

    public IpcPsmSasBenchmarks() {
    }

    public static void main(String[] args) throws Exception {
        runAll();
//        IpcPsmSasBenchmarks benchmarsk = new IpcPsmSasBenchmarks();
//        benchmarsk.runExperimentCli(new String[]{"./benchmarks/depot/domain.pddl", "./benchmarks/depot/pfile1"});
        //benchmarsk.runExperimentCli(new String[]{"./benchmarks/beerproblem/domain.pddl", "./benchmarks/beerproblem/beerproblem-04.pddl"});
        //benchmarsk.runExperimentCli(new String[]{"./benchmarks/logistics00/domain.pddl", "./benchmarks/logistics00/probLOGISTICS-14-1.pddl"});
        //benchmarsk.runExperimentCli(new String[]{"./benchmarks/rovers/domain.pddl", "./benchmarks/rovers/p02.pddl"});


        // trivialne stejne akce uz od zacatku
        //benchmarsk.runExperimentCli(new String[]{"./benchmarks/parcprinter-08-strips/p11-domain.pddl", "./benchmarks/parcprinter-08-strips/p11.pddl"});

        // airport:p02-airport1-p1
        //benchmarsk.runExperimentCli(new String[]{"./benchmarks/airport/p02-domain.pddl", "./benchmarks/airport/p02-airport1-p1.pddl"});
        //airport:p03-airport1-p2
        //benchmarsk.runExperimentCli(new String[]{"./benchmarks/airport/p03-domain.pddl", "./benchmarks/airport/p03-airport1-p2.pddl"});

        /* mozny merge akci
        airport:p05-airport2-p1 - tam jsou dve stejnojmenne akce?
        barman-sat11-strips:pfile06-022
        barman-sat11-strips:pfile06-023
        barman-sat11-strips:pfile06-021
        barman-sat11-strips:pfile06-024
        barman-sat11-strips:pfile07-028


        */

        /*String dir = "airport";
        String problem = "p04-airport2-p1.pddl";
        benchmarsk.runExperimentCli(new String[]{"./benchmarks/"+dir+"/domain.pddl", "./benchmarks/"+dir+"/" + problem});
        */


        // loop na -DE
//        benchmarsk.runExperimentCli(new String[]{"./benchmarks/parcprinter-08-strips/p01-domain.pddl", "./benchmarks/parcprinter-08-strips/p01.pddl"});


        /*Downward.forceCleanAllTmpFiles();

        new IpcPsmSasBenchmarks().runBenchmarks();
        //new IpcSasReduceBenchmarks().runBenchmarks();

        System.out.println("Ended: " + new Date());
        /**/
    }

    private static void runAll() {
        IpcPsmSasBenchmarks benchmarsk = new IpcPsmSasBenchmarks();

        for (File problemDir : IpcBenchmarks.getProblemDirs()) {
            List<File> problemFiles = IpcBenchmarks.getProblemFiles(problemDir);

            //int start = 0;
            //int end = (problemFiles.size() < 5) ? problemFiles.size() : 5;

//            for (int i = start; i < end; i++) {
//            for (int i = 0; i < problemFiles.size(); i++) {
//                System.out.println(i+ "problemFiles.get(index): " + problemFiles.get(i).getName());
//            }
//          for (int i : new int[]{2}) {//18,20,24,22,29,26}) { //11,15,17,18,19,20
          for (int i : new int[]{0}) {//,5,20,10,11,14,18,17,7}) {//2,4,7,9,10,12,15,16,17,19
                File problemFile = problemFiles.get(i);
                File domainFile = IpcBenchmarks.getDomainFile(problemDir, problemFile);
                benchmarsk.runExperimentCli(domainFile, problemFile);
            }
        }
    }
}
