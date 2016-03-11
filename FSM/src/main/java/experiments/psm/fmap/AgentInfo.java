package experiments.psm.fmap;

import java.io.File;

import cz.agents.dimap.tools.pddl.PddlName;

public class AgentInfo {
	
	public String agentName;
	public File domainFile;
	public File problemFile;
	
	public AgentInfo(String agentName, File domainFile, File problemFile) {
		this.agentName = PddlName.fixName(agentName);
		this.domainFile = domainFile;
		this.problemFile = problemFile;
	}
	
}