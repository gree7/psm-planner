package experiments.psm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

import cz.agents.dimap.Settings;
import cz.agents.dimap.psm.planner.MultiAgentProblem;

public abstract class PsmBenchmarks {

    private String experimentName;
    private final String experimentType;

    public PsmBenchmarks(String experimentType) {
        this.experimentType = experimentType;
    }

    public void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(120);
        formatter.printHelp(experimentType+"-benchmarks.sh [OPTIONS] experiment-id", Settings.CMD_OPTIONS_ADVANCED);
    }

    protected void saveResults(String experimentName, ExperimentRunner experimentRunner) throws IOException {
        File resultsDir = new File("experiments", "results");
        if (!resultsDir.exists()) {
            resultsDir.mkdir();
        }
        String resultFile = "results-"+experimentType+"-"+experimentName+".txt";
        BufferedWriter resultOut = new BufferedWriter(new FileWriter(new File(resultsDir, resultFile)));
        resultOut.write(experimentRunner.getResults());
        resultOut.close();
    }

    public void setSettings(String[] args) throws Exception {
        CommandLine cmdLine = Settings.parseCmdLineOptions(args, Settings.CMD_OPTIONS_ADVANCED);

        if (cmdLine == null || cmdLine.getArgs().length != 1) {
            printUsage();
            System.exit(-1);
        }
        experimentName = cmdLine.getArgs()[0];

        Settings.setFromCommandLine(cmdLine);
    }

    public void runBenchmarks(Map<String, MultiAgentProblem> problems) throws Exception {

        ExperimentRunner experimentRunner = new ExperimentRunner(Settings.TIMEOUT_MINUTES);
        for (Entry<String, MultiAgentProblem> problem : problems.entrySet()) {
            System.out.println("Running problem: " + problem.getKey());
            experimentRunner.runExperiment(problem.getKey(), new PlanningTask(problem.getValue()));
        }

        saveResults(experimentName, experimentRunner);
        System.out.println("Current configuration: "+Settings.generateCurrentSettingsDescription());
        System.out.println("Results: \n" + experimentRunner.getResults());
    }
}

