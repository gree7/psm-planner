package cz.agents.dimap.tools.pddl.ma;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.expr.SExpr;
import cz.agents.dimap.tools.pddl.expr.SExprParser;
import cz.agents.dimap.tools.pddl.parser.PddlParser;
import cz.agents.dimap.tools.pddl.types.PddlTypeMap;
import cz.agents.dimap.tools.pddl.types.PddlPredicateType;

public class PddlAddl {

	public PddlName addlName;
	public PddlName domainName;
	public List<PddlName> agentsList = null;
	public List<PddlName> agentTypes = null;
	public Map<PddlName, PddlPredicateType> sharedData = null;
	
	public PddlAddl(List<String> agentNames) {
		addlName = new PddlName("unknown");
		domainName = new PddlName("unknown");
		agentsList = new LinkedList<>();
		for (String agentName : agentNames) {
			agentsList.add(PddlName.createFixedName(agentName));
		}
	}

	public PddlAddl(String addlFileName) throws IOException {
		this(new FileReader(addlFileName));
	}
	
	/*
	 * Construction from S-Expression
	 */
	public PddlAddl(Readable readable) throws IOException {
		this(SExprParser.PARSER.parse(readable));
	}
	
	public PddlAddl(SExpr expr) {
		if (!expr.isList()) {
			System.err.println("ERROR: Expecting S-List, has "+expr.getValue());
			return;
		}
		Map<String, SExpr> pddlDir = PddlParser.makePddlDictionary(expr);
		
		setAddlName(pddlDir);
		setDomainName(pddlDir);
		setAgentsList(pddlDir);
		setAgentTypes(pddlDir);
		setSharedData(pddlDir);
		
		if (!pddlDir.isEmpty()) {
			System.err.println("PDDL WARNING: Unrecognized ADDL file sections: "+pddlDir.keySet());
		}
		if ((agentsList == null && agentTypes == null)) {
			System.err.println("PDDL WARNING: ADDL file does not define agent collection !");
		}

	}

	private void setAddlName(Map<String, SExpr> pddlDir) {
		if (pddlDir.containsKey("problem")) {
			addlName = new PddlName(pddlDir.remove("problem").e(1).getValue());
		}
	}

	private void setDomainName(Map<String, SExpr> pddlDir) {
		if (pddlDir.containsKey(":domain")) {
			domainName = new PddlName(pddlDir.remove(":domain").e(1).getValue());
		}
	}

	private void setAgentsList(Map<String, SExpr> pddlDir) {
		if (pddlDir.containsKey(":agents")) {
			agentsList = new LinkedList<>();
			SExpr agentsExpr = pddlDir.remove(":agents");
			for (int i = 1; i < agentsExpr.getList().size(); i++) {
				agentsList.add(PddlName.createFixedName(agentsExpr.e(i).getValue()));
			}
			if (agentsList.isEmpty()) {
				System.err.println("PDDL WARNING: An ADDL file defines empty collection of agents!");
			}
		}
	}

	private void setAgentTypes(Map<String, SExpr> pddlDir) {
		if (pddlDir.containsKey(":agentTypes")) {
			agentTypes = new LinkedList<>();
			SExpr agentsExpr = pddlDir.remove(":agentTypes");
			for (int i = 1; i < agentsExpr.getList().size(); i++) {
				agentTypes.add(PddlName.createFixedName(agentsExpr.e(i).getValue()));
			}
			if (agentTypes.isEmpty()) {
				System.err.println("PDDL WARNING: An ADDL file defines empty collection of agent types!");
			}
		}
	}
	
	private void setSharedData(Map<String, SExpr> pddlDir) {
		sharedData = new HashMap<PddlName, PddlPredicateType>();
		if (pddlDir.containsKey(":shared-data")) {
			SExpr expr = pddlDir.remove(":shared-data");
			expr.getList().remove(0);
			PddlTypeMap<PddlPredicateType> tmp = new PddlTypeMap<>(expr, PddlPredicateType.TRANSLATOR);
			for (PddlPredicateType e : tmp.getBindings().keySet()) {
				sharedData.put(e.predicateName, e);
			}
		}
	}

    public boolean hasSharedData() {
        return sharedData != null && !sharedData.isEmpty();
    }
}
