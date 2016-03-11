package cz.agents.dimap.tools.pddl;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.dimap.tools.pddl.expr.SExpr;
import cz.agents.dimap.tools.pddl.expr.SExprParser;
import cz.agents.dimap.tools.pddl.parser.PddlList;
import cz.agents.dimap.tools.pddl.parser.PddlParser;
import cz.agents.dimap.tools.pddl.types.PddlFunctionType;
import cz.agents.dimap.tools.pddl.types.PddlTypeMap;
import cz.agents.dimap.tools.pddl.types.PddlPredicateType;
import cz.agents.dimap.tools.pddl.types.PddlType;

public class PddlDomain {

    public static boolean INCLUDE_ACTION_PREFIXES = true;
    
	public PddlName domainName;
	public List<PddlName> requirements;
	public PddlTypeMap<PddlName> types;
	public PddlTypeMap<PddlName> constants;
	public Map<PddlName, PddlPredicateType> predicateTypes;
	public Map<PddlName, PddlFunctionType> functionTypes;
	public PddlList<PddlTerm> sharedPredicates;
	
	public List<PddlName> sharedPredicateNames;

	public List<PddlName> privatePredicates;

	public List<PddlAction> actions;
	
	public PddlDomain() {
		domainName = new PddlName("domain");
		requirements = new LinkedList<>();
		types = new PddlTypeMap<PddlName>();
		constants = new PddlTypeMap<PddlName>();
		predicateTypes = new HashMap<PddlName, PddlPredicateType>();
		functionTypes = new HashMap<PddlName, PddlFunctionType>();
		actions = new LinkedList<>();
		sharedPredicateNames = new ArrayList<>();
        sharedPredicates = new PddlList<>();
        privatePredicates = new ArrayList<>();
		
		requirements.add(new PddlName(":strips"));
	}

    public PddlDomain(PddlDomain other) {
        this(
        	other.domainName, 
            new ArrayList<PddlName>(other.requirements),
            new PddlTypeMap<PddlName>(other.types),
            new PddlTypeMap<PddlName>(other.constants),
            new HashMap<PddlName, PddlPredicateType>(other.predicateTypes),
            new HashMap<PddlName, PddlFunctionType>(other.functionTypes),
            new ArrayList<PddlAction>(other.actions), 
            new ArrayList<PddlName>(other.sharedPredicateNames),
            new PddlList<>(other.sharedPredicates),
            new ArrayList<>(other.privatePredicates)
        );
    }
	
	public PddlDomain(
		PddlName domainName, 
		List<PddlName> requirements, 
		PddlTypeMap<PddlName> types,
        PddlTypeMap<PddlName> constants,
        Map<PddlName, PddlPredicateType> predicateTypes,
        Map<PddlName, PddlFunctionType> functionTypes,
        List<PddlAction> actions, 
        List<PddlName> sharedPredicateNames,
        PddlList<PddlTerm> sharedPredicates,
        List<PddlName> privatePredicates
    ) {
		this.domainName = domainName;
        this.requirements = requirements;
        this.types = types;
        this.constants = constants;
        this.predicateTypes = predicateTypes;
        this.functionTypes = functionTypes;
        this.actions = actions;
        this.sharedPredicateNames = sharedPredicateNames;
        this.sharedPredicates = sharedPredicates;
        this.privatePredicates = privatePredicates;
    }

    public PddlDomain(String domainFilename) throws IOException {
		this(new FileReader(domainFilename));
	}
	
	public PddlDomain(Readable readable) throws IOException {
		this(SExprParser.PARSER.parse(readable));
	}
	
	public PddlDomain(SExpr expr)  {
		if (!expr.isList()) {
			System.err.println("PDDL ERROR: Expecting S-List, has "+expr.getValue());
			return;
		}
		Map<String, SExpr> pddlDir = PddlParser.makePddlDictionary(expr);
		
		setDomainName(pddlDir);
		setRequirements(pddlDir);
		setTypes(pddlDir);
		setConstants(pddlDir);
		setPredicates(pddlDir);
		setFunctions(pddlDir);
        setSharedPredicateNames(pddlDir);
        setSharedPredicates(pddlDir);
		setActions(pddlDir);
		
		if (!pddlDir.isEmpty()) {
			System.err.println("PDDL WARNING: Unrecognized domain file sections: "+pddlDir.keySet());
		}

	}

	public static PddlDomain createGroundedDomain(PddlDomain domain, List<PddlAction> actions) {
	    return new PddlDomain(
	    	new PddlName("domain"), 
	    	domain.requirements, 
	    	new PddlTypeMap<PddlName>(), 
	    	new PddlTypeMap<PddlName>(), 
	    	new HashMap<PddlName, PddlPredicateType>(), 
	    	new HashMap<PddlName, PddlFunctionType>(), 
	    	actions, 
	    	domain.sharedPredicateNames,
            domain.sharedPredicates,
            domain.privatePredicates
	    );
	}

	public static PddlDomain replaceActions(PddlDomain domain, List<PddlAction> actions) {
	    return new PddlDomain(
	    	domain.domainName, 
	    	domain.requirements, 
	    	domain.types, 
	    	domain.constants, 
	    	domain.predicateTypes,
	    	domain.functionTypes,
	    	actions, 
	    	domain.sharedPredicateNames,
	    	domain.sharedPredicates,
            domain.privatePredicates
	    );
	}

	private void setDomainName(Map<String, SExpr> pddlDir) {
		domainName = new PddlName(pddlDir.remove("domain").e(1).getValue());
	}
	
	private void setTypes(Map<String, SExpr> pddlDir) {
		if (pddlDir.containsKey(":types")) {
			SExpr expr = pddlDir.remove(":types");
			expr.getList().remove(0);
	        types = new PddlTypeMap<PddlName>(expr, PddlName.TRANSLATOR);
        } else {
			types = new PddlTypeMap<PddlName>();
        }
	}

	private void setConstants(Map<String, SExpr> pddlDir) {
		if (pddlDir.containsKey(":constants")) {
			SExpr expr = pddlDir.remove(":constants");
			expr.getList().remove(0);
	        constants = new PddlTypeMap<PddlName>(expr, PddlName.TRANSLATOR);
        } else {
			constants = new PddlTypeMap<PddlName>();
        }
	}
	
	private void setRequirements(Map<String, SExpr> pddlDir) {
		requirements = new LinkedList<PddlName>();
		if (!pddlDir.containsKey(":requirements")) {
			return;
		}
		SExpr expr = pddlDir.remove(":requirements");
		if (expr.isNonEmptyList()) {
			expr.getList().remove(0);
			for (SExpr req : expr.getList()) {
				if (req.isValue()) {
					requirements.add(PddlName.createFixedName(req.getValue()));
				}
				else {
					System.err.println("PDDL PARSE ERROR: Unrecognized requirement: "+req);
				}
			}
		}
	}
	
	private void setSharedPredicateNames(Map<String, SExpr> pddlDir) {
        sharedPredicateNames = new LinkedList<>();
		if (!pddlDir.containsKey(":shared-predicate-names")) {
			return;
		}
		SExpr sharedPredsExpr = pddlDir.remove(":shared-predicate-names");
		if (!sharedPredsExpr.isNonEmptyList()) {
			System.err.println("PDDL PARSE ERROR: Wrong syntax of shared-preciate-names, non-empty list expected, has: "+sharedPredsExpr);
			return;
		}
		
		sharedPredsExpr.getList().remove(0);
		for (SExpr predicateName : sharedPredsExpr.getList()) {
			if (predicateName.isValue()) {
				sharedPredicateNames.add(PddlName.createFixedName(predicateName.getValue()));
			}
			else {
				System.err.println("PDDL PARSE ERROR: Unrecognized predicate name in shared-predicates: "+predicateName);
			}
		}
	}
	
	private void setSharedPredicates(Map<String, SExpr> pddlDir) {
		if (!pddlDir.containsKey(":shared-predicates")) {
			sharedPredicates = new PddlList<>();
			return;
		}
		SExpr sharedPredsExpr = pddlDir.remove(":shared-predicates");
		if (!sharedPredsExpr.isNonEmptyList()) {
			System.err.println("PDDL PARSE ERROR: Wrong syntax of shared-preciates, non-empty list expected, has: "+sharedPredsExpr);
			return;
		}
		
		sharedPredsExpr.getList().remove(0);
		sharedPredicates = new PddlList<>(sharedPredsExpr, PddlTerm.TRANSLATOR);
	}

	public PddlAction getActionByPublicName(String publicActionName) {
		// TODO: cache the results
		boolean unique = true;
		PddlAction ret = null;
		for (PddlAction action : actions) {
			if (action.getName().equals(publicActionName)) {
				if (unique) {
					ret = action;
					unique = false;
				}
				else {
					System.err.println("FATAL ERROR: Public action name "+publicActionName+" does not uniquely determine an action in the domain! First match used.");
					System.out.println("ret: " + ret + " vs. " + action);
				}
			}
		}
		return ret;
	}
	
	public void setDefaultActionCosts() {
		if (!requirements.contains(PddlName.REQUIREMENT_ACTION_COSTS)) {
			requirements.add(PddlName.REQUIREMENT_ACTION_COSTS);
		}
		for (PddlAction action : actions) {
			action.setDefaultCost();
		}
	}
	
	public void removeActionCosts() {
		requirements.remove(PddlName.REQUIREMENT_ACTION_COSTS);
		for (PddlAction action : actions) {
			action.removeActionCost();
		}
	}

	
	private void setActions(Map<String, SExpr> pddlDir) {
		actions = new LinkedList<PddlAction>();
		Set<String> removeUs = new HashSet<>();
		for (Entry<String, SExpr> entry : pddlDir.entrySet()) {
			if (!entry.getKey().startsWith("act:")) {
				continue;
			}
			removeUs.add(entry.getKey());
			actions.add(new PddlAction(entry.getValue()));
		}
		pddlDir.keySet().removeAll(removeUs);
	}

	private void setPredicates(Map<String, SExpr> pddlDir) {
		predicateTypes = new HashMap<PddlName, PddlPredicateType>();
		if (pddlDir.containsKey(":predicates")) {
			SExpr expr = pddlDir.remove(":predicates");
			expr.getList().remove(0);
			PddlTypeMap<PddlPredicateType> tmp = new PddlTypeMap<>(expr, PddlPredicateType.TRANSLATOR);
			for (PddlPredicateType e : tmp.getBindings().keySet()) {
				predicateTypes.put(e.predicateName, e);
			}
		}
		else {
			System.err.println("PDDL WARNING: Domain defines no predicates.");
		}
        privatePredicates = new ArrayList<>();
        if (pddlDir.containsKey(":private")) {
            SExpr expr = pddlDir.remove(":private");
            expr.getList().remove(0);
            PddlTypeMap<PddlPredicateType> tmp = new PddlTypeMap<>(expr, PddlPredicateType.TRANSLATOR);
            for (PddlPredicateType e : tmp.getBindings().keySet()) {
                predicateTypes.put(e.predicateName, e);
                privatePredicates.add(e.predicateName);
            }
        }
	}
	
	private void setFunctions(Map<String, SExpr> pddlDir) {
		functionTypes = new HashMap<PddlName, PddlFunctionType>();
		if (pddlDir.containsKey(":functions")) {
			SExpr expr = pddlDir.remove(":functions");
			expr.getList().remove(0);
			PddlTypeMap<PddlPredicateType> tmp = new PddlTypeMap<>(expr, PddlPredicateType.TRANSLATOR);
			for (Entry<PddlPredicateType, PddlType> t : tmp.getBindings().entrySet()) {
				if (t.getKey().predicateName.equals(PddlName.COST_FUNCTION)) {
					if (!requirements.contains(PddlName.REQUIREMENT_ACTION_COSTS)) {
						System.err.println("PDDL WARNING: 'total-cost' function is used but ':action-costs' requirement is not set. Fixing..");
						requirements.add(PddlName.REQUIREMENT_ACTION_COSTS);
					}
					continue;
				}
				functionTypes.put(t.getKey().predicateName, new PddlFunctionType(t.getKey().predicateName, t.getKey().arguments, t.getValue()));
			}
		}
	}
	
	public void applyDeleteRelaxation() {
		for (PddlAction action : actions) {
			action.effect.negatives.clear();
		}
	}
	
	public void applyFluentDecomposition() {
		for (PddlAction action : actions) {
			action.applyFluentDecomposition(functionTypes);
		}
		translateFunctionsToPredicates();
		requirements.remove(PddlName.REQUIREMENT_FLUENTS);
	}

	private void translateFunctionsToPredicates() {
		for (PddlFunctionType functionType : functionTypes.values()) {
			PddlTypeMap<PddlName> arguments = new PddlTypeMap<>(functionType.arguments);
			arguments.addAssignment(new PddlName("?"+functionType.functionName.name+"_value"), functionType.resultType);
			PddlPredicateType predicateType = new PddlPredicateType(functionType.functionName, arguments);
			predicateTypes.put(predicateType.predicateName, predicateType);
		}
		functionTypes.clear();
	}
	
	public void writeToFile(String domainFileName) throws IOException {
		FileWriter writer = new FileWriter(domainFileName);
		writer.write(this.toString());
		writer.close();
	}

	public void writeToFileMaPddl(String domainFileName) throws IOException {
        FileWriter writer = new FileWriter(domainFileName);
        writer.write(this.toString(true));
        writer.close();
    }
	
	@Override
	public String toString() {
	    return toString(false);
	}
	
    public String toString(boolean isMaPddlFormat) {

		StringBuilder builder = new StringBuilder();
		builder.append("(define (domain ").append(domainName).append(")\n");
		appendRequirements(builder);
		
		if (types.hasAssignments()) {
			builder.append("(:types\n\t").append(types.toString("\n\t", false)).append("\n)\n\n");
		}

		/*
		if (constants.hasAssignments()) {
			builder.append("(:constants\n\t").append(constants.toString("\n\t")).append("\n)\n\n");
		}
		*/
		
		builder.append("(:predicates").append("\n");
		appendPredicates(builder);
		builder.append(")\n\n");
		
		if (!functionTypes.isEmpty() || requirements.contains(PddlName.REQUIREMENT_ACTION_COSTS)) {
			builder.append("(:functions").append("\n");
			appendFunctions(builder);
			if (requirements.contains(PddlName.REQUIREMENT_ACTION_COSTS)) {
				builder.append("\t(total-cost)\n");
			}
			builder.append(")\n\n");
		}
		
        if (isMaPddlFormat) {
            if (sharedPredicates != null && sharedPredicates.objects.size() > 0) {
                builder.append("(:shared-predicates").append("\n");
                appendSharedPredicates(builder);
                builder.append(")\n\n");
            }
        }
		
		if (isMaPddlFormat) {
		    appendActions(builder, !isMaPddlFormat);
		} else {
		    appendActions(builder);
		}
				
		builder.append("\n)\n");
		
		return builder.toString();
	}

	private void appendRequirements(StringBuilder builder) {
		builder.append("(:requirements");
		for (PddlName req : requirements) {
		    if (req.name.contains("privacy")) {
		        continue;
		    }
			builder.append(" ").append(req);
		}
		builder.append(")\n");
	}

	protected void appendActions(StringBuilder builder) {
	    appendActions(builder, INCLUDE_ACTION_PREFIXES);
	}

	protected void appendActions(StringBuilder builder, boolean includeActionPrefixes) {
		Collections.sort(actions);
		for (PddlAction a : actions) {
			builder.append(a.toString(includeActionPrefixes));
		}
	}

	protected void appendSharedPredicateNames(StringBuilder builder) {
		for (PddlName sharedPredName : sharedPredicateNames) {
			builder.append("\t").append(sharedPredName.name).append("\n");
		}
	}

	protected void appendSharedPredicates(StringBuilder builder) {
		for (PddlTerm sharedTerm : sharedPredicates.objects) {
			builder.append("\t(").append(sharedTerm).append(")\n");
		}
	}

	protected void appendPredicates(StringBuilder builder) {
		List<PddlName> predicates = new LinkedList<>(predicateTypes.keySet());
		Collections.sort(predicates);
		for (PddlName predicate : predicates) {
			builder.append("\t").append(predicateTypes.get(predicate)).append("\n");
		}
	}
	
	private void appendFunctions(StringBuilder builder) {
		List<PddlName> functions = new LinkedList<>(functionTypes.keySet());
		Collections.sort(functions);
		for (PddlName function : functions) {
			builder.append("\t").append(functionTypes.get(function)).append("\n");
		}
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("usage: pddl-domain-reformat domain.pddl");
		}
		
		try {
			System.out.println(new PddlDomain(args[0]).toString());
		} catch (IOException e) {
			System.err.println("Reading domain file failed: "+e.getMessage());
		}
	}

	public void trySetSharedPredicates(List<PddlName> newSharedPredicateNames) {
		if (sharedPredicateNames.isEmpty()) {
			sharedPredicateNames.addAll(newSharedPredicateNames);
		}
		else {
			checkSharedPredicatesConsistency(newSharedPredicateNames);
		}
	}

	private void checkSharedPredicatesConsistency(List<PddlName> newSharedPredicateNames) {
		// check (sharedPredicateNames == sharedPredicates)
		boolean namesAreEqual = false; 
		Collections.sort(newSharedPredicateNames);
		Collections.sort(sharedPredicateNames);
		if (newSharedPredicateNames.size() == sharedPredicateNames.size()) {
			namesAreEqual = true;
			for (int i = 0; i < newSharedPredicateNames.size(); i++) {
				if (!newSharedPredicateNames.get(i).equals(sharedPredicateNames.get(i))) {
					namesAreEqual = false;
					break;
				}
			}
		}
		if (!namesAreEqual) {
			System.err.println("FMAP TRANSLATION ERROR: Shared data from one FMAP problem do not match data from another FMAP problem!");
			System.err.println("  Previously added: "+sharedPredicateNames);
			System.err.println("  Now adding      : "+newSharedPredicateNames);
		}
	}
	
	public PddlAction getActionByName(String actionName) {
		for (PddlAction action : actions) {
			if (action.name.equals(actionName)) {
				return action;
			}
		}
		return null;
	}
	
	@Override
	public PddlDomain clone() {
		return new PddlDomain(this);
	}

    public void cloneActions() {
        List<PddlAction> oldActions = actions;
        actions = new ArrayList<>(oldActions.size());
        for (PddlAction action : oldActions) {
            actions.add(action.clone());
        }
    }

}
