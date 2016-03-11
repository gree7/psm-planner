package experiments.codmap.centralized;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.cli.CommandLine;

import cz.agents.dimap.Settings;
import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.psm.planner.PlanningResult;
import cz.agents.dimap.psm.planner.PsmPlanner;
import experiments.psm.file.CoDmapMultiAgentProblemMaStrips;
import experiments.psm.file.FileMultiAgentProblem;

public class CoDmapPsmPlanner {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("usage: [options] <problem-dir> <output-file>");
        }
        
        CommandLine cmdLine = Settings.parseCmdLineOptions(args, Settings.CMD_OPTIONS_ADVANCED);
        
        String problemDir = cmdLine.getArgs()[0];
        String outFile = cmdLine.getArgs()[1];

        Settings.setFromCommandLine(cmdLine);
        
//        Settings.MASTRIPS_PUBLIC_FACT_HAS_TO_BE_IN_EFFECT = false;
        
        System.out.println("Settings: " + Settings.generateCurrentSettingsDescription() + " (centralized)");

        System.out.println("creating MA problem: " + new Date());
        MultiAgentProblem maProblem = new FileMultiAgentProblem( new File(problemDir) );
        System.out.println("creating CoDMAP MA problem: " + new Date());
//        maProblem = new MaStripsMultiAgentProblem(maProblem);
        maProblem = CoDmapMultiAgentProblemMaStrips.getInstance(maProblem);

        System.out.println("planning: " + new Date());
        PlanningResult result = PsmPlanner.plan(maProblem, Long.MAX_VALUE);
        
        System.out.println("result: " + result);
        
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outFile)));
            for (String action : result.plan) {
                writer.append(action);
                writer.append('\n');
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
