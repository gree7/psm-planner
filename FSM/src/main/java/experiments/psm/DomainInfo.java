package experiments.psm;

import cz.agents.dimap.psm.planner.MultiAgentProblem;


public interface DomainInfo {

	public MultiAgentProblem createProblem(int problemNo) throws Exception;
	
	public String getDomainName();
	
	public int getProblemCount();
	
}
