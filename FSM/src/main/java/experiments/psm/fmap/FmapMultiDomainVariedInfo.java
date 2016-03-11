package experiments.psm.fmap;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import experiments.psm.DomainInfo;
import experiments.psm.PsmFmapBenchmarks;

/*
 * Multiple domains with varied mapping of problems to domains.
 */
public class FmapMultiDomainVariedInfo implements DomainInfo {

	public int problemCount;
	
	public String domainDir;
	private String domainPrefix;
	private String problemPrefix;
	private final Map<String, String> problemDomainMap;
	private boolean twoDigitsProblemCount = false;

	public FmapMultiDomainVariedInfo(String domainDir, int problemCount, String domainPrefix, String problemPrefix, Map<String, String> agentDomainMap, boolean twoDigits) {
		this.domainDir = domainDir;
		this.problemCount = problemCount;
		this.domainPrefix = domainPrefix;
		this.problemPrefix = problemPrefix;
		this.problemCount = problemCount;
		this.problemDomainMap = agentDomainMap;
		this.twoDigitsProblemCount = twoDigits;
	}
	
	@Override
	public FmapMultiAgentProblem createProblem(int problemNo) throws Exception {
		File problemDir = new File(new File(PsmFmapBenchmarks.FMAP_BENCHMARKS_DIR, domainDir), PsmFmapBenchmarks.FMAP_PROBLEMDIR_PREFIX+(twoDigitsProblemCount && problemNo < 10 ? "0" : "")+problemNo);
		List<AgentInfo> agents = new LinkedList<>();
		for (File fmapFile : problemDir.listFiles()) {
			if (fmapFile.getName().startsWith(problemPrefix) && fmapFile.getName().endsWith(".pddl")) {
				String agentName = fmapFile.getName().substring(problemPrefix.length(), fmapFile.getName().length() - 5);
				File domainFile = new File(problemDir, domainPrefix+findDomainName(agentName)+".pddl");
				agents.add(new AgentInfo(agentName, domainFile, fmapFile));
			}
		}
		return new FmapMultiAgentProblem(agents.toArray(new AgentInfo[agents.size()]));
	}
	
	private String findDomainName(String agentFileName) {
		for (String agentName : problemDomainMap.keySet()) {
			if (agentFileName.contains(agentName)) {
				return problemDomainMap.get(agentName);
			}
		}
		System.err.println("ERROR: Domain info does not contain information about the domain of agent: "+agentFileName);
		return null;
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
