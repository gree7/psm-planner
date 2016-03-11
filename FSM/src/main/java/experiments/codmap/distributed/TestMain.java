package experiments.codmap.distributed;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Random;

import cz.agents.dimap.tools.fd.Downward;
import experiments.codmap.distributed.agent.CoDmapAgent;

public class TestMain {

/*
    public static final String DIR = "experiments/CoDMAP/blocksworld/probBLOCKS-10-0/";
    public static final String DIR = "experiments/CoDMAP/depot/pfile5/";
    public static final String DIR = "experiments/CoDMAP/driverlog/pfile8/";
    public static final String DIR = "experiments/CoDMAP/elevators08/p10/";
    public static final String DIR = "experiments/CoDMAP/logistics00/probLOGISTICS-10-0/";
    public static final String DIR = "experiments/CoDMAP/rovers/p11/";
    public static final String DIR = "experiments/CoDMAP/sokoban/p05/";
    public static final String DIR = "experiments/CoDMAP/woodworking08/p05/";
    public static final String DIR = "experiments/CoDMAP/zenotravel/pfile5/";
 */  

    public static final String DIR = "experiments/CoDMAP/rovers/p12/";
/*
*/
    public static void main(String[] args) throws Exception {
        
        CoDmapPsmPlanner.BROKER_PORT += new Random().nextInt(10);
        
        Downward.forceCleanAllTmpFiles();
        
        
        CoDmapAgent.IS_RUNNING_IN_ONE_VM = true;

// blocksworld
//        runAgent("a1");
//        runAgent("a2");
//        runAgent("a3");
//        runAgent("a4");

// depot
//        runAgent("depot0");
//        runAgent("distributor0");
//        runAgent("distributor1");
//        runAgent("truck0");
//        runAgent("truck1");

// logistics
        Collection<String> agentNames = findAgentNames(new File(DIR));
        File agentList = new File(new File(DIR).getParent(), "agentList");
        BufferedWriter writer = new BufferedWriter(new FileWriter(agentList));
        for (String agent : agentNames) {
            writer.append(agent + "\t127.0.0.1\n");
        }
        writer.close();
        for (String agent : agentNames) {
            runAgent(agent, agentList.getAbsolutePath());
        }
    }

    private static void runAgent(final String agent, final String agentList) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    CoDmapPsmPlanner.main(new String[] {
                            "-C", "-DrS",
//                            "-C", "-rS",
                            DIR + "domain-" + agent + ".pddl",
                            DIR + "problem-" + agent + ".pddl",
                            agent,
                            agentList,
                            "plan-"+agent+".out"
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private static Collection<String> findAgentNames(File problemDir) {
        Collection<String> agentNames = new LinkedHashSet<>();
        
        for (File file : problemDir.listFiles()) {
            if (file.getName().startsWith("problem-")) {
                String agentName = file.getName().substring(8, file.getName().length());
                agentName = agentName.substring(0, agentName.indexOf('.'));
                agentNames.add(agentName);
            }
        }
        return agentNames;
    }

}
