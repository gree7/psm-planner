package experiments.psm.ipc;

import java.io.File;
import java.io.FileReader;
import java.util.Set;

import cz.agents.dimap.Settings;
import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.ma.PddlAddl;
import experiments.psm.PsmIpcBenchmarks;

/* 
 * One domain file for all the agents. 
 */
public class IpcSingleDomainInfo implements IpcDomainInfo {
	
	public int problemCount;
	
	public String domainDir;
	private String domainName;
	private String problemPrefix;
    private String addlName;
	
	public IpcSingleDomainInfo(String domainDir, int problemCount) {
		this(domainDir, problemCount, domainDir, domainDir+"-", domainDir);
	}
	
	public IpcSingleDomainInfo(String domainDir, int problemCount, String domainName, String problemPrefix, String addlName) {
		this.domainDir = domainDir;
		this.problemCount = problemCount;
		this.domainName = domainName;
		this.problemPrefix = problemPrefix;
        this.addlName = addlName;
		this.problemCount = problemCount;
	}

	@Override
	public MultiAgentProblem createProblem(int problemNo) throws Exception {
		if (Settings.VERBOSE) { System.out.println("Translating problem "+domainName+"/"+problemNo+"."); }
		File problemDir = new File(PsmIpcBenchmarks.BENCHMARKS_DIR, domainDir);
		File cacheDir = null;

        if (Settings.USE_IPC_TRANSLATION_CACHE) {
        	cacheDir = new File(problemDir, "_psm_cache_"+String.format("%02d", problemNo));
        	if (cacheDir.exists() && !Settings.FORCE_IPC_TRANSLATION) {
        		return new IpcMultiAgentProblem(cacheDir);
        	}
        }
		
		PddlDomain domain = new PddlDomain(new FileReader(new File(problemDir, domainName+".pddl")));
        PddlProblem problem = new PddlProblem(domain, new FileReader(new File(problemDir, problemPrefix+(problemNo < 10 ? "0" : "")+problemNo+".pddl")));
        PddlAddl addl = new PddlAddl(new FileReader(new File(problemDir, addlName+".addl")));
        IpcMultiAgentProblem maProblem = new IpcMultiAgentProblem(problem, addl);
        if (Settings.USE_IPC_TRANSLATION_CACHE) {
        	maProblem.writeProjections(cacheDir);
        }

        return maProblem;
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
