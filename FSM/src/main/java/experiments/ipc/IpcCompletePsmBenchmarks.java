package experiments.ipc;

import java.io.File;
import java.util.Date;
import java.util.List;

import cz.agents.dimap.psm.planner.completepsm.CompletePsmPlanner;
import cz.agents.dimap.tools.GenericResult;
import cz.agents.dimap.tools.fd.Downward;
import cz.agents.dimap.tools.fd.DownwardGrounder;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class IpcCompletePsmBenchmarks extends IpcBenchmarksCli {
    
    @Override
	public GenericResult runExperiment(PddlProblem problem) {
//      Settings.DOT_CAUSAL_GRAPH = true;

        PddlDomain.INCLUDE_ACTION_PREFIXES = false;
        System.out.print("FD translate time: ");
        long groundTime = System.currentTimeMillis();
        
        problem = DownwardGrounder.groundProblem(problem);

        groundTime = System.currentTimeMillis() - groundTime;
        System.out.printf("%.02fs%n", groundTime/1000.0);

        long planTime = System.currentTimeMillis();

        List<String> solution = CompletePsmPlanner.plan(problem);
        
        planTime = System.currentTimeMillis() - planTime;

        System.out.printf("planning: %.02fs%n", planTime/1000.0);

        System.out.println("solution ("+solution.size()+": " + solution);
        GenericResult planResult = new GenericResult();
        planResult.planningTime = planTime;
        planResult.solved = true;

        return planResult;
	}

    private static void deleteAllImages() {
        for (File file : new File(".").listFiles()) {
            if (file.getName().startsWith("psm")) { 
                file.delete();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        deleteAllImages();
        Downward.forceCleanAllTmpFiles();
        
        new IpcCompletePsmBenchmarks().runBenchmarks();
        
        System.out.println("Ended: "+new Date());
    }
}
