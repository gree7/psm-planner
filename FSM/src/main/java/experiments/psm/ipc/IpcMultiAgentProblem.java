package experiments.psm.ipc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.dimap.psm.planner.PddlMultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.ma.IpcMaProjection;
import cz.agents.dimap.tools.pddl.ma.MaProjection;
import cz.agents.dimap.tools.pddl.ma.PddlAddl;

public class IpcMultiAgentProblem extends PddlMultiAgentProblem {

    public IpcMultiAgentProblem(PddlProblem problem, PddlAddl addl) {
        super(createProjections(new IpcMaProjection(problem, addl)));
    }

    public IpcMultiAgentProblem(File inputDir) {
        super(readProjections(inputDir));
    }
    
    private static Map<String, PddlProblem> createProjections(MaProjection projections) {
        Map<String, PddlProblem> problemProjections = new HashMap<>();
        for (PddlName agentName : projections.getAgents().keySet()) {
            PddlProblem agentProjection = projections.generateAgentProblem(agentName);
            problemProjections.put(agentName.name, agentProjection);
        }
        return problemProjections;
    }
    
    private static Map<String, PddlProblem> readProjections(File inputDir) {
    	Map<String, PddlProblem> problemProjections = new HashMap<>();
    	
    	Set<String> agentNames = extractAgentNames(inputDir);
    	
    	for (String agentName : agentNames) {
    		File domainFile = new File(inputDir, agentName+"-domain.pddl");
			File problemFile = new File(inputDir, agentName+"-problem.pddl");
			try {
				PddlProblem problem = new PddlProblem(domainFile.getPath(), problemFile.getPath());
				problem.setActionAccessTypes();
				problemProjections.put(agentName, problem);
			} catch (IOException e) {
				System.err.println("ERROR: Error while reading cached translation result PDDL:");
				e.printStackTrace();
			}
    	}
    	
    	return problemProjections;
    }

	private static Set<String> extractAgentNames(File inputDir) {
		Set<String> agentNames = new HashSet<>();
    	for (File file : inputDir.listFiles()) {
    		String agentName = file.getName().split("-")[0];
    		agentNames.add(agentName);
    	}
		return agentNames;
	}

	public void writeProjections(File outputDir) {
		if (!outputDir.exists()) { 
			outputDir.mkdir(); 
		}
        for (File file : outputDir.listFiles()) {
            file.delete();
        }
		
		for (Entry<String, PddlProblem> entry : getProblems().entrySet()) {
			String agentName = entry.getKey();
			PddlProblem ipcMaProblem = entry.getValue();
			File domainFile = new File(outputDir, agentName+"-domain.pddl");
			File problemFile = new File(outputDir, agentName+"-problem.pddl");
			try {
				ipcMaProblem.writeToFilesMaPddl(domainFile.getPath(), problemFile.getPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}   
}
