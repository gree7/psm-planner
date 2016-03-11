package experiments.psm.fmap;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import experiments.psm.DomainInfo;
import experiments.psm.PsmFmapBenchmarks;

/*
 * Every agent has its own domain file (one-to-one mapping of problems to domains).
 */
public class FmapMultiDomainBijectiveInfo implements DomainInfo {

	public int problemCount;
	
	public String domainDir;
	private String domainPrefix;
	private String problemPrefix;
	private final List<String> fixedAgents;
	private boolean twoDigitsProblemCount = false;
	
	public FmapMultiDomainBijectiveInfo(String domainDir, int problemCount, String domainPrefix, String problemPrefix, List<String> agents) {
		this.domainDir = domainDir;
		this.problemCount = problemCount;
		this.domainPrefix = domainPrefix;
		this.problemPrefix = problemPrefix;
		this.problemCount = problemCount;
		fixedAgents = agents;
	}
	
	@Override
	public FmapMultiAgentProblem createProblem(int problemNo) throws Exception {
		File problemDir = new File(new File(PsmFmapBenchmarks.FMAP_BENCHMARKS_DIR, domainDir), PsmFmapBenchmarks.FMAP_PROBLEMDIR_PREFIX+(twoDigitsProblemCount && problemNo < 10 ? "0" : "")+problemNo);
		List<AgentInfo> agents = new LinkedList<>();
		for (String agentName : fixedAgents) {
			String domainFilename = domainPrefix+agentName+".pddl";
			String problemFilename = problemPrefix+agentName+".pddl";
			agents.add(new AgentInfo(agentName, new File(problemDir, domainFilename), new File(problemDir, problemFilename)));
		}
		return new FmapMultiAgentProblem(agents.toArray(new AgentInfo[agents.size()]));

	}

	@Override
	public String getDomainName() {
	    return domainDir;
	}

	@Override
	public int getProblemCount() {
		return problemCount;
	}

}
