package cz.agents.dimap.tools.pddl.ma;

import java.io.File;
import java.io.IOException;

import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class ProjectionCreator {

    public static boolean ALLOW_ONLY_PUBLIC_PRECONDITION = false;

    public static void main(String[] args) {
        if (ALLOW_ONLY_PUBLIC_PRECONDITION) {
            System.out.println("INFO: Actions with no public effect but with some public precondition are public!");
        } else {
            System.out.println("INFO: Actions with no public effect are internal!");
        }
        if (args.length == 4 && (args[0].equals("--grounded-multi-agent-pddl") || args[0].equals("-gmap"))) {
        	try {
				createGroundedProjections(args);
			} catch (IOException e) {
				System.err.println("IO Error occured while creating projections:\n"+e.getMessage());
			}
        }
        else if (args.length == 4 && (args[0].equals("--fmap-multi-agent-pddl") || args[0].equals("-gmap"))) {
            try {
                createFmapProjections(args);
            } catch (IOException e) {
                System.err.println("IO Error occured while creating projections:\n"+e.getMessage());
            }
        } else {
        	System.out.println(usage());
        	System.exit(1);
        }
    }

    private static void createGroundedProjections(String[] args) throws IOException {
        createProjections(new GroundedMaProjection(args[1], args[2], args[3]));
	}

    private static void createFmapProjections(String[] args) throws IOException {
        createProjections(new IpcMaProjection(args[1], args[2], args[3]));
    }
    
    private static void createProjections(MaProjection projections) throws IOException {
        for (PddlName agentName : projections.getAgents().keySet()) {
            PddlProblem agentProjection = projections.generateAgentProblem(agentName);
            
            File projectionDir = new File(projections.getOriginalProblemName().name);
            if (!projectionDir.exists()) {
                projectionDir.mkdir();
            }
            File domainFile = new File(projectionDir, agentProjection.domainName.name+".pddl");
            File problemFile = new File(projectionDir, agentProjection.problemName.name+".pddl");

            agentProjection.writeToFilesMaPddl(domainFile.getPath(), problemFile.getPath());
        }
    }
    
    public static String usage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usage: java -cp FSM.jar cz.agents.dimap.tools.pddl.ma.ProjectionCreator\n");
        sb.append("\t--grounded-multi-agent-pddl domain.pddl problem.pddl domain.addl\n");
        sb.append("\t--fmap-multi-agent-pddl domain.pddl problem.pddl domain.addl\n");

        return sb.toString();
    }
}
