package experiments.codmap.distributed;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;

import cz.agents.dimap.Settings;
import experiments.codmap.distributed.agent.CoDmapAgent;
import experiments.codmap.distributed.broker.CoDmapBroker;

public class CoDmapPsmPlanner {

    static int BROKER_PORT = 30000;// + new Random().nextInt(10);
    
    public static void main(String[] args) throws Exception {
        if (args.length < 5) {
            throw new IllegalArgumentException("usage: [options] <domain-file> <problem-file> <agent-name> <agent-ip-file> <output-file>");
        }
        
        CommandLine cmdLine = Settings.parseCmdLineOptions(args, Settings.CMD_OPTIONS_ADVANCED);
        
        String domainFile = cmdLine.getArgs()[0];
        String problemFile = cmdLine.getArgs()[1];
        String agentName = cmdLine.getArgs()[2];
        String ipFile = cmdLine.getArgs()[3];
        String outFile = cmdLine.getArgs()[4];

        Settings.setFromCommandLine(cmdLine);
        
        Settings.MASTRIPS_PUBLIC_FACT_HAS_TO_BE_IN_EFFECT = false;
        
        System.out.println("Settings: " + Settings.generateCurrentSettingsDescription());
        
        String brokerIp = getBrokerIpAddress(ipFile);
        
        int agentIndex = getAgentIndex(ipFile, agentName);
        
        if (agentIndex == 0) {
            // run broker
            CoDmapBroker broker = new CoDmapBroker(ipFile, BROKER_PORT, domainFile, problemFile);
            new Thread(broker).start();
        }
        
        brokerIp = brokerIp.substring(brokerIp.indexOf('\t')+1, brokerIp.length());
        
        CoDmapAgent agent = new CoDmapAgent(agentName, agentIndex, domainFile, problemFile, outFile, brokerIp, BROKER_PORT);
        agent.run();
    }

    private static int getAgentIndex(String ipFile, String agentName) throws FileNotFoundException {
        Scanner sc = new Scanner(new File (ipFile));
        int index = 0;
        while (true) {
            String line = sc.nextLine();
            if (line.contains(agentName)) {
                sc.close();
                return index;
            }
            index++;
        }
    }

    private static String getBrokerIpAddress(String ipFile) throws FileNotFoundException {
        Scanner sc = new Scanner(new File (ipFile));
        String line = sc.nextLine();
        sc.close();
        return line;
    }
}
