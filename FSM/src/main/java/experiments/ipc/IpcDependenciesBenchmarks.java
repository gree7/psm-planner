package experiments.ipc;

import java.io.File;
import java.util.Date;

import cz.agents.dimap.Settings;
import cz.agents.dimap.causal.InternalDependenciesGraphPddl;
import cz.agents.dimap.tools.GenericResult;
import cz.agents.dimap.tools.fd.Downward;
import cz.agents.dimap.tools.fd.DownwardGrounder;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class IpcDependenciesBenchmarks extends IpcBenchmarks {
    
    static final boolean EVALUATE_PLANNING = true;
    
    final static boolean USE_FD_TRANSLATOR_GROUNDING = false;

	@Override
	public GenericResult runExperiment(PddlProblem problem) {
        double pddlTime = Double.NaN;
        if (EVALUATE_PLANNING) {
            pddlTime = Downward.logDownward(problem, "PDDL FD").planningTime;
        }

        PddlDomain.INCLUDE_ACTION_PREFIXES = false;
        System.out.print("FD translate time: ");
        long groundTime = System.currentTimeMillis();
        
        if (USE_FD_TRANSLATOR_GROUNDING) {
            problem = DownwardGrounder.groundProblem(problem);
        } else {
            DownwardGrounder.groundProblem(problem);
        }
        groundTime = System.currentTimeMillis() - groundTime;
        System.out.printf("%.02fs%n", groundTime/1000.0);

        System.out.print("create time: ");
        long createTime = System.currentTimeMillis();
        InternalDependenciesGraphPddl id = new InternalDependenciesGraphPddl("node1", problem, USE_FD_TRANSLATOR_GROUNDING);
        id.init();
        createTime = System.currentTimeMillis() - createTime;
        System.out.printf("%.02fs%n", createTime/1000.0);

        System.out.println("facts: " + id.getFacts());

        double groundedTime = Double.NaN;
        if (EVALUATE_PLANNING) {
            id.logFsm("grounded");
            problem = id.createConvertedProblem();
            groundedTime = Downward.logDownward(problem, "grounded FD").planningTime;
        }
        
        System.out.print("reduction time: ");

        long convTime = System.currentTimeMillis();
        id.countInternalDependencies();
        convTime = System.currentTimeMillis() - convTime;
        System.out.printf("%.02fs%n", convTime/1000.0);

        System.out.println("reduced facts: " + id.getFacts());
        
        GenericResult reducedResult = new GenericResult();
        if (id.isFullyReduced()) {
            System.out.println("!!! Problem can be reduced !!!" );
            reducedResult.planningTime = 0.0;
        } else {
            System.out.println("@@@ Problem cannot be reduced @@@" );
            
            if (EVALUATE_PLANNING) {
                problem = id.createConvertedProblem();
                reducedResult = Downward.logDownward(problem, "reduced FD");
            }
        }
        
        System.out.printf("planning time: %.02fs -> %.02fs%n", groundedTime/1000.0, reducedResult.planningTime/1000.0);

        if (USE_FD_TRANSLATOR_GROUNDING) {
            createTime += groundTime;
        }                    
        
        groundedTime += createTime;
        
        reducedResult.planningTime += createTime + convTime;
        
        System.out.printf("### result: %.02fs -> %.02fs -> %.02fs%n", pddlTime/1000.0, groundedTime/1000.0, reducedResult.planningTime/1000.0);
        
        return reducedResult;
	}

    private static void deleteAllImages() {
        for (File file : new File(".").listFiles()) {
            if (file.getName().startsWith("fsmRisky")) { 
                file.delete();
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
    	Settings.TIMEOUT_MINUTES = 5;
    	
        deleteAllImages();
        Downward.forceCleanAllTmpFiles();
        
        new IpcDependenciesBenchmarks().runBenchmarks();
        
        System.out.println("Ended: "+new Date());
    }


}

