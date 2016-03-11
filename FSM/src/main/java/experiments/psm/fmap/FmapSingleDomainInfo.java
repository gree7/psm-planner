package experiments.psm.fmap;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import experiments.psm.DomainInfo;
import experiments.psm.PsmFmapBenchmarks;

/* 
 * One domain file for all the agents. 
 */
public class FmapSingleDomainInfo implements DomainInfo {
	
	public int problemCount;
	
	public String domainDir;
	private String domainPrefix;
	private String problemPrefix;
	private boolean twoDigitsProblemCount = false;
	
	public FmapSingleDomainInfo(String domainDir, int problemCount, String domainPrefix, String problemPrefix) {
		this(domainDir, problemCount, domainPrefix, problemPrefix, false);
	}
	
	public FmapSingleDomainInfo(String domainDir, int problemCount, String domainPrefix, String problemPrefix, boolean twoDigits) {
		this.domainDir = domainDir;
		this.problemCount = problemCount;
		this.domainPrefix = domainPrefix;
		this.problemPrefix = problemPrefix;
		this.problemCount = problemCount;
		this.twoDigitsProblemCount = twoDigits;
	}

	@Override
	public FmapMultiAgentProblem createProblem(int problemNo) throws Exception {
		File problemDir = new File(new File(PsmFmapBenchmarks.FMAP_BENCHMARKS_DIR, domainDir), PsmFmapBenchmarks.FMAP_PROBLEMDIR_PREFIX+(twoDigitsProblemCount && problemNo < 10 ? "0" : "")+problemNo);
		List<AgentInfo> agents = new LinkedList<>();
		File domainFile = new File(problemDir, domainPrefix+".pddl");
		for (File fmapFile : problemDir.listFiles()) {
			if (fmapFile.getName().startsWith(domainPrefix)) {
				continue;
			}
			if (fmapFile.getName().startsWith(problemPrefix) && fmapFile.getName().endsWith(".pddl")) {
				String agentName = fmapFile.getName().substring(problemPrefix.length(), fmapFile.getName().length() - 5);
				agents.add(new AgentInfo(agentName, domainFile, fmapFile));
			}
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
