package cz.agents.dimap;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

import experiments.psm.ExperimentRunner;
import experiments.psm.PlanningTask;
import experiments.psm.fmap.AgentInfo;
import experiments.psm.fmap.FmapMultiAgentProblem;

public class PsmPlanner {

	private static void printUsage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.setWidth(120);
		formatter.printHelp("psm-plan.sh [OPTIONS] fmap-agent1-name fmap-agent1-domain.pddl fmap-agent1-problem.pddl ...", "", Settings.CMD_OPTIONS_BASIC, "Recommended options: -p PSM -rS");
	}

	private static void printAdvancedUsage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.setWidth(120);
		formatter.printHelp("psm-plan.sh [OPTIONS] fmap-agent1-name fmap-agent1-domain.pddl fmap-agent1-problem.pddl ...", "", Settings.CMD_OPTIONS_ADVANCED, "Recommended options: -p PSM -rS");
	}
	
	private static FmapMultiAgentProblem createFmapMultiAgentProblem(CommandLine cmdLine) throws Exception {
		int agentCount = cmdLine.getArgs().length / 3;
		int argIndex = 0;
		
		AgentInfo[] fmapAgentInfos = new AgentInfo[agentCount];
		for (int i = 0; i < agentCount; i++) {
			fmapAgentInfos[i] = new AgentInfo(
				cmdLine.getArgs()[argIndex++],
				new File(cmdLine.getArgs()[argIndex++]),
				new File(cmdLine.getArgs()[argIndex++])
			);
		}
		
		return new FmapMultiAgentProblem(fmapAgentInfos);
	}
	
	public static void main(String[] args) {
    
		CommandLine cmdLine = Settings.parseCmdLineOptions(args, Settings.CMD_OPTIONS_ADVANCED);
		
		if (cmdLine == null || cmdLine.getArgs().length < 1 || cmdLine.getArgs().length % 3 != 0) {
			if (cmdLine.hasOption("h")) {
				printAdvancedUsage();
			}
			else {
				printUsage();
			}
			System.exit(-1);
		}
		
		Settings.setFromCommandLine(cmdLine);
		
		final FmapMultiAgentProblem fmap;
		try {
			fmap = createFmapMultiAgentProblem(cmdLine);
		} catch (Exception e) {
			System.err.println("ERROR: Error creating multi-agent problem.");
			return;
		}
		
		ExperimentRunner runner = new ExperimentRunner(Settings.TIMEOUT_MINUTES);
		runner.runExperiment("cmd-line-job", new PlanningTask(fmap));
	}
}
