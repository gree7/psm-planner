package experiments.ipc.planners;

import cz.agents.dimap.tools.GenericResult;
import cz.agents.dimap.tools.fd.Downward;
import cz.agents.dimap.tools.fd.DownwardGrounder;
import cz.agents.dimap.tools.pddl.PddlProblem;
import experiments.ipc.IpcBenchmarksCli;

public class IpcDownwardPlannerBenchmarks extends IpcBenchmarksCli {

    static boolean RUN_FD_WITH_GROUNDED_PROBLEM = false;
	
	@Override
	public GenericResult runExperiment(PddlProblem problem) {
		PddlProblem fdProblem = problem;
		
		long groundTime = System.currentTimeMillis();
		if (RUN_FD_WITH_GROUNDED_PROBLEM) {
			System.out.print("FD translate time: ");
			
			fdProblem = DownwardGrounder.groundProblem(fdProblem);
	        
			groundTime = System.currentTimeMillis() - groundTime;
	        System.out.printf("%.02fs%n", groundTime/1000.0);
		}
        
		GenericResult result = Downward.logDownward(fdProblem, RUN_FD_WITH_GROUNDED_PROBLEM ? "FD grounded PDDL" : "FD PDDL");
        
		if (RUN_FD_WITH_GROUNDED_PROBLEM) {
			result.planningInfo = String.format("*=%.2f+%.2f", groundTime/1000.0, result.planningTime/1000.0);
			result.planningTime += groundTime;
		}
        return result;
	}

	public static void main(String[] args) {
		RUN_FD_WITH_GROUNDED_PROBLEM = false;
		if (args.length == 3) {
			if (args[0].equals("--run-grounded")) {
				RUN_FD_WITH_GROUNDED_PROBLEM = true;
				args = new String[] { args[1], args[2] };
			}
			else {
				System.err.println("usage: <prg> [--run-grounded] domain.pddl problem.pddl");
				return;
			}
		}
		new IpcDownwardPlannerBenchmarks().runExperimentCli(args);
	}
	
}
