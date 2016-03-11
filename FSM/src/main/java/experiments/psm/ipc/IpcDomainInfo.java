package experiments.psm.ipc;

import cz.agents.dimap.psm.planner.MultiAgentProblem;

public interface IpcDomainInfo {

	public MultiAgentProblem createProblem(int problemNo) throws Exception;
	
	public String getDomainName();
	
	public int getProblemCount();
	
}
