package experiments.ipc.planners;

import cz.agents.dimap.causal.InternalDependenciesGraphPddl;
import cz.agents.dimap.tools.GenericResult;
import cz.agents.dimap.tools.fd.Downward;
import cz.agents.dimap.tools.fd.DownwardGrounder;
import cz.agents.dimap.tools.pddl.PddlProblem;
import experiments.ipc.IpcBenchmarksCli;

public class IpcDGPlannerBenchmarks extends IpcBenchmarksCli {

    static boolean USE_FD_TRANSLATOR_GROUNDING = true;
	
	@Override
	public GenericResult runExperiment(PddlProblem problem) {
        long startTime = System.currentTimeMillis();
        
        // ground problem if required
        long groundTime = startTime;
        if (USE_FD_TRANSLATOR_GROUNDING) {
            System.out.print("FD translate time: ");
            problem = DownwardGrounder.groundProblem(problem);
            groundTime = System.currentTimeMillis() - groundTime;
            System.out.printf("%.02fs%n", groundTime/1000.0);
        }

        // create DG
        System.out.print("create time: ");
        long createTime = System.currentTimeMillis();
        InternalDependenciesGraphPddl id = new InternalDependenciesGraphPddl("node1", problem, USE_FD_TRANSLATOR_GROUNDING);
        id.init();
        createTime = System.currentTimeMillis() - createTime;
        System.out.printf("%.02fs%n", createTime/1000.0);
        System.out.println("facts: " + id.getFacts());

        // reduce DG
        System.out.print("reduction time: ");
        long convTime = System.currentTimeMillis();
        id.countInternalDependencies();
        convTime = System.currentTimeMillis() - convTime;
        System.out.printf("%.02fs%n", convTime/1000.0);
        System.out.println("reduced facts: " + id.getFacts());
        
        // solve reduced DG
        GenericResult result = new GenericResult();
        if (id.isFullyReduced()) {
            System.out.println("!!! Problem can be reduced !!!" );
            result.solved = true;
        } else {
            System.out.println("@@@ Problem cannot be reduced @@@" );
            problem = id.createConvertedProblem();
            result = Downward.logDownward(problem, "FD reduced PDDL");
        }

        if (result.solved) {
        	if (USE_FD_TRANSLATOR_GROUNDING) {
        		result.planningInfo = String.format("*=%.2f+%.2f+%.2f+%.2f", groundTime/1000.0, createTime/1000.0, convTime/1000.0, result.planningTime/1000.0);
        	}
        	else {
        		result.planningInfo = String.format("*=%.2f+%.2f+%.2f", createTime/1000.0, convTime/1000.0, result.planningTime/1000.0);
        	}
        }
		result.planningTime = System.currentTimeMillis() - startTime;
        return result;
	}
	
	public static void main(String[] args) {
		USE_FD_TRANSLATOR_GROUNDING = false;
		if (args.length == 3) {
			if (args[0].equals("--run-grounded")) {
				USE_FD_TRANSLATOR_GROUNDING = true;
				args = new String[] { args[1], args[2] };
			}
			else {
				System.err.println("usage: <prg> [--run-grounded] domain.pddl problem.pddl");
				return;
			}
		}

		new IpcDGPlannerBenchmarks().runExperimentCli(args);
	}
	
}
