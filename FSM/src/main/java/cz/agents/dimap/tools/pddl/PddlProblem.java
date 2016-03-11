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

import cz.agents.dimap.Settings;
import cz.agents.dimap.tools.pddl.expr.SExpr;
import cz.agents.dimap.tools.pddl.expr.SExprParser;
import cz.agents.dimap.tools.pddl.expr.SValue;
import cz.agents.dimap.tools.pddl.ma.ActionType;
import cz.agents.dimap.tools.pddl.parser.PddlParser;
import cz.agents.dimap.tools.pddl.types.PddlType;
import cz.agents.dimap.tools.pddl.types.PddlTypeMap;

public class PddlProblem {

	public enum PddlProblemStatus { SOLVABLE, UNSOLVABLE, UNKNOW }
	
    public PddlDomain domain;

    public PddlName problemName;
    public PddlName domainName;
    public List<PddlName> requirements;
    public PddlCondition init;
    public PddlTypeMap<PddlName> objects;
    public PddlCondition goal;
    
    public SExpr fmapSharedData = null;
    public List<PddlName> fmapSharedNames = new ArrayList<>();

    public List<PddlName> privateObjects;

    private Map<PddlName, HashSet<PddlName>> objectsByTypeMap = null;

    public PddlProblem(PddlDomain domain) {
        this.domain = domain;

        domainName = domain.domainName;
        requirements = new LinkedList<>();
        init = new PddlCondition();
        objects = new PddlTypeMap<PddlName>();
        goal = new PddlCondition();
        privateObjects = new ArrayList<>();

        requirements.add(new PddlName(":strips"));
    }

    public PddlProblem(PddlProblem other) {
        domain = other.domain.clone();

        problemName = other.problemName;
        domainName = other.domainName;
        requirements = new ArrayList<>(other.requirements);
        init = new PddlCondition(other.init);
        objects = new PddlTypeMap<PddlName>(other.objects);
        goal = new PddlCondition(other.goal);
        fmapSharedData = other.fmapSharedData;
        privateObjects = other.privateObjects;
    }

    public PddlProblem(String domainFilename, String problemFilename) throws IOException {
        this(new PddlDomain(domainFilename), new FileReader(problemFilename));
    }

    public PddlProblem(PddlDomain domain, String problemFilename) throws IOException {
        this(domain, new FileReader(problemFilename));
    }

    public PddlProblem(PddlDomain domain, Readable readable) throws IOException {
        this(domain, SExprParser.PARSER.parse(readable));
    }

    public PddlProblem(PddlDomain domain, SExpr expr) {
        if (!expr.isList()) {
            System.err.println("ERROR: Expecting S-List, has "+expr.getValue());
            return;
        }
        Map<String, SExpr> pddlDir = PddlParser.makePddlDictionary(expr);

        this.domain = domain;
        setRequirements(pddlDir);
        setInit(pddlDir);
        setObjects(pddlDir);
        setGoal(pddlDir);
        setCostFunction(pddlDir);
        setProblem(pddlDir);
        setDomain(pddlDir);
        setFmapSharedData(pddlDir);
        if (!pddlDir.isEmpty()) {
            System.err.println("PDDL WARNING: Unrecognized problem file sections: "+pddlDir.keySet());
        }
    }

	public static PddlProblem replaceDomain(PddlProblem problem, PddlDomain domain) {
        PddlProblem replaced = new PddlProblem(problem);
        replaced.domain = domain;
        return replaced;
    }

    public static PddlProblem createPublicProjection(PddlProblem problem) {
        PddlProblem publicProblem = new PddlProblem(problem);
        publicProblem.init = PddlCondition.createProjectedConditionToPredicateNames(problem.init, problem);
        publicProblem.goal = PddlCondition.createProjectedConditionToPredicateNames(problem.goal, problem);
        List<PddlAction> publicActions = new ArrayList<>();
        for (PddlAction action : publicProblem.domain.actions) {
            if (action.isExternal()) {
                publicActions.add(action);
            } else if (action.isPublic()) {
                PddlAction externalAction = PddlAction.createExternalAction(action, problem, Settings.MASTRIPS_PUBLIC_FACT_HAS_TO_BE_IN_EFFECT);
                if (externalAction != null) {
                    publicActions.add(externalAction);
                }
            }
        }
        publicProblem.domain.actions.clear();
        publicProblem.domain.actions.addAll(publicActions);
            
        return publicProblem;
    }

	public void setActionAccessTypes() {
        for (PddlAction action : domain.actions) {
            PddlCondition projectedEffects = PddlCondition.createProjectedConditionToPredicateNames(action.effect, this);
            if (projectedEffects.isEmpty()) {
                action.setActionType( ActionType.INTERNAL );
            } else {
                action.setActionType( ActionType.PUBLIC );
            }
        }
    }

    private void setRequirements(Map<String, SExpr> pddlDir) {
        requirements = new LinkedList<PddlName>();
        if (!pddlDir.containsKey(":requirements")) {
            return;
        }
        SExpr requirementsExpr = pddlDir.remove(":requirements");
        if (requirementsExpr.isNonEmptyList()) {
            requirementsExpr.getList().remove(0);
            for (SExpr req : requirementsExpr.getList()) {
                if (req.isValue()) {
                    requirements.add(new PddlName(req.getValue()));
                }
                else {
                    System.err.println("PDDL PARSE ERROR: Unrecognized requirment: "+req);
                }
            }
        }
    }

    private void setDomain(Map<String, SExpr> pddlDir) {
        domainName = new PddlName(pddlDir.remove(":domain").e(1).getValue());
    }

    private void setProblem(Map<String, SExpr> pddlDir) {
        problemName = new PddlName(pddlDir.remove("problem").e(1).getValue());
    }

    private void setObjects(Map<String, SExpr> pddlDir) {
		if (pddlDir.containsKey(":objects")) {
			SExpr expr = pddlDir.remove(":objects");
			expr.getList().remove(0);
	        objects = new PddlTypeMap<PddlName>(expr, PddlName.TRANSLATOR, domain.constants);
        } else {
			objects = new PddlTypeMap<PddlName>();
        }

        privateObjects = new ArrayList<>();
        if (pddlDir.containsKey(":private")) {
            SExpr expr = pddlDir.remove(":private");
            expr.getList().remove(0);
            PddlTypeMap<PddlName> tmpObjects = new PddlTypeMap<PddlName>(expr, PddlName.TRANSLATOR);
            for (Entry<PddlName, PddlType> assigment : tmpObjects.getBindings().entrySet()) {
                objects.addAssignment(assigment.getKey(), assigment.getValue());
                privateObjects.add(assigment.getKey());
            }
        }
    }

    public PddlDomain getDomain() {
        return domain;
    }

    public void setDefaultActionCosts() {
		if (!requirements.contains(PddlName.REQUIREMENT_ACTION_COSTS)) {
			requirements.add(PddlName.REQUIREMENT_ACTION_COSTS);
		}
        getDomain().setDefaultActionCosts();
    }
    
    public void removeActionCosts() {
		requirements.remove(PddlName.REQUIREMENT_ACTION_COSTS);
        getDomain().removeActionCosts();
    }

    private void setInit(Map<String, SExpr> pddlDir) {
        if (pddlDir.containsKey(":init") || pddlDir.containsKey(":INIT")) {
            SExpr initExpr = pddlDir.remove(":init");
            if (initExpr == null) {
                initExpr = pddlDir.remove(":INIT");
            }
            initExpr.getList().set(0, new SValue("and"));
            init = new PddlCondition(initExpr);
            
            PddlTerm costInit = new PddlTerm(PddlName.COST_FUNCTION);
            if (init.equalities.containsKey(costInit)) {
            	init.equalities.remove(costInit);
                if (!requirements.contains(PddlName.REQUIREMENT_ACTION_COSTS)) {
                	System.err.println("PDDL WARNING: A cost function in a PDDL problem is initilized but ':action-costs' requirement is not set.  Fixing..");
                	requirements.add(PddlName.REQUIREMENT_ACTION_COSTS);
                }
            }
        }
        else {
            System.out.println("WARNING: Problem defines no initial state.");
        }
    }

    private void setGoal(Map<String, SExpr> pddlDir) {
        if (pddlDir.containsKey(":goal")) {
            goal = new PddlCondition(pddlDir.remove(":goal").e(1));
        }
        else if (pddlDir.containsKey(":global-goal")) {
            goal = new PddlCondition(pddlDir.remove(":global-goal").e(1));
        }
        else {
            System.out.println("WARNING: Problem defines no goals.");
        }
    }

    private void setCostFunction(Map<String, SExpr> pddlDir) {
        if (pddlDir.containsKey(":metric")) {
            SExpr expr = pddlDir.remove(":metric");
            if (!(expr.isListOfSize(3) && 
            	 expr.e(1).isValue("minimize") && 
            	 expr.e(2).isPositiveFact() &&
            	 expr.e(2).e(0).getValue().equals(PddlName.COST_FUNCTION.name))) 
            {
            	System.err.println("PDDL WARNING: Unsupported metric: "+expr+". Only '(:metric minimize (total-cost))' is supported.");
            	return;
            }
            if (!requirements.contains(PddlName.REQUIREMENT_ACTION_COSTS)) {
            	System.err.println("PDDL WARNING: A minimize metric is used but ':action-costs' requirement is not set.  Fixing..");
            	requirements.add(PddlName.REQUIREMENT_ACTION_COSTS);
            }
        }
    }

    private void setFmapSharedData(Map<String, SExpr> pddlDir) {
        if (pddlDir.containsKey(":shared-data")) {
        	fmapSharedData = pddlDir.remove(":shared-data");
        	fmapSharedNames = new LinkedList<PddlName>();
        	for (int i = 1; i < fmapSharedData.getList().size(); i++) {
        		if (fmapSharedData.e(i).isValue("-")) {
        			break;        			
        		}
        		if (!fmapSharedData.e(i).isList() || fmapSharedData.e(i).isEmptyList()) {
        			System.err.println("PDDL PARSE ERROR: While parsing FMAP shared-data, list expected, has: "+fmapSharedData.e(i));
        			break;
        		}
        		if (fmapSharedData.e(i).e(0).isValue()) {
        			fmapSharedNames.add(PddlName.createFixedName(fmapSharedData.e(i).e(0).getValue()));
        		}
        		else
        		if (fmapSharedData.e(i).e(0).isNonEmptyList() && fmapSharedData.e(i).e(0).e(0).isValue()) {
        			fmapSharedNames.add(PddlName.createFixedName(fmapSharedData.e(i).e(0).e(0).getValue()));
        		}
        		else {
        			System.err.println("PDDL PARSE ERROR: While parsing FMAP shared-data, predicate or function type expected, has: "+fmapSharedData.e(i).e(0));
        		}
        		        		
        	}
        }
        else {
        	fmapSharedData = null;
        	fmapSharedNames = new ArrayList<>();
        }
	}

    public Set<PddlName> getObjectsByType(PddlType type) {
        if (objectsByTypeMap == null) {
            extractObjectsByType();
        }
        
        Set<PddlName> objects = new HashSet<>();
        for (PddlName typeName : type.eithers) {
            HashSet<PddlName> typeObjects = objectsByTypeMap.get(typeName);
            if (typeObjects != null) {
                objects.addAll(typeObjects);
            } else {
                if (Settings.DEBUG) {
                    System.err.println("PDDL WARNING: used type has no instances: " + type);
                }
            }
        }
        
        return objects;
    }

    private void extractObjectsByType() {

        Map<PddlName, HashSet<PddlName>> typeHierarchyTree = new HashMap<>();
                
        for (Entry<PddlName, PddlType> entry : domain.types.getBindings().entrySet()) {
            if (entry.getValue().eithers.size() > 1) {
                throw new UnsupportedOperationException();
            }

            PddlName type = entry.getValue().eithers.get(0);

            addToListInMap(typeHierarchyTree, type, entry.getKey());           
        }

        objectsByTypeMap = new HashMap<>();

        for (Entry<PddlName, PddlType> entry : objects.getBindings().entrySet()) {
            if (entry.getValue().eithers.size() > 1) {
                throw new UnsupportedOperationException();
            }

            if (entry.getValue().eithers.isEmpty()) {
                continue;
            }
            
            PddlName type = entry.getValue().eithers.get(0);
            PddlName object = entry.getKey();

            addToListInMap(typeHierarchyTree, type, object);           
            addToListInMap(objectsByTypeMap, object, object);           
        }
        
        for (PddlName type : typeHierarchyTree.keySet()) {
            extractObjectsOfType(typeHierarchyTree, type);
        }
    }

    private <K, V> void addToListInMap(Map<K, HashSet<V>> map, K key, V value) {
        HashSet<V> collectionValue = map.get(key);
        if (collectionValue == null) {
            collectionValue = new HashSet<V>();
            map.put(key, collectionValue);
        }
        collectionValue.add(value);
    }
    
    private Set<PddlName> extractObjectsOfType(Map<PddlName, HashSet<PddlName>> typeHierarchyTree, PddlName type) {
        if (objectsByTypeMap.containsKey(type)) {
            return objectsByTypeMap.get(type);
        }
        
        Set<PddlName> subTypes = typeHierarchyTree.get(type);

        if (subTypes == null) {
            return Collections.emptySet();
        }
        
        HashSet<PddlName> newSubTypes = new HashSet<>();

        for (PddlName subType : subTypes) {
            newSubTypes.addAll(extractObjectsOfType(typeHierarchyTree, subType));
        }
        
        objectsByTypeMap.put(type, newSubTypes);
        return newSubTypes;
    }

    public void writeProblemToFile(String problemFileName) throws IOException {
        FileWriter writer = new FileWriter(problemFileName);
        writer.write(this.toString());
        writer.close();
    }

    public void writeToFiles(String domainFileName, String problemFileName) throws IOException {
        getDomain().writeToFile(domainFileName);
        writeProblemToFile(problemFileName);
    }

    public void writeToFilesMaPddl(String domainFileName, String problemFileName) throws IOException {
        getDomain().writeToFileMaPddl(domainFileName);
        writeProblemToFile(problemFileName);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(define (problem ").append(problemName).append(")\n");
        builder.append("(:domain ").append(domainName).append(")\n");
        appendRequirements(builder);

        if (!objects.isEmpty()) {
            builder.append("(:objects\n\t");
            appendObjects(builder);
            builder.append("\n)\n\n");
        }

        if (fmapSharedData != null) {
        	builder.append(fmapSharedData);
        }

        builder.append("(:init\n");
        appendInit(builder);
        builder.append(")\n\n");
        
        builder.append("(:goal ");
        appendGoal(builder);
        builder.append("\n)\n\n");

        if (requirements.contains(PddlName.REQUIREMENT_ACTION_COSTS)) {
            builder.append("(:metric minimize (").append(PddlName.COST_FUNCTION).append("))\n\n");
        }

        builder.append(")\n");

        return builder.toString();
    }

    private void appendRequirements(StringBuilder builder) {
        builder.append("(:requirements");
        for (PddlName req : requirements) {
            builder.append(" ").append(req);
        }
        builder.append(")\n");
    }

    protected void appendObjects(StringBuilder builder) {
        builder.append(objects.toString("\t\n\t", false));
    }

    protected void appendGoal(StringBuilder builder) {
        builder.append(goal);
    }

    protected void appendInit(StringBuilder builder) {
        builder.append(init.toStringAsPositiveList());
        if (requirements.contains(PddlName.REQUIREMENT_ACTION_COSTS)) {
        	builder.append("\t(= ("+PddlName.COST_FUNCTION.name+") 0)\n");
        }
    }

    public void writeToFiles() throws IOException {
        writeToFiles(domain.domainName.name+".pddl", problemName.name+".pddl");
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("usage: pddl-problem-reformat problem.pddl");
        }

        try {
            System.out.println(new PddlProblem((PddlDomain)null, args[0]).toString());
        } catch (IOException e) {
            System.err.println("Reading problem file failed: "+e.getMessage());
        }
    }

    public void applyFluentDecomposition() {
    	init.applyEqualitiesFluentDecomposition();
    	goal.applyEqualitiesFluentDecomposition();
    	domain.applyFluentDecomposition();
    	requirements.remove(PddlName.REQUIREMENT_FLUENTS);
    }
    
    public void translateFmapToMa() {
    	applyFluentDecomposition();
    	translateFmapSharedDataToMa();
    }

	private void translateFmapSharedDataToMa() {
		if (fmapSharedNames != null) {
			domain.trySetSharedPredicates(fmapSharedNames);
		}
    	fmapSharedData = null;
	}
	
	@Override
	public PddlProblem clone() {
		return new PddlProblem(this);
	}

    public boolean isPublicPredicate(PddlName predicateName) {
        return domain.sharedPredicateNames.contains(predicateName);
    }

    public boolean isPublicPredicate(PddlTerm predicate) {
        if (isPublicPredicate(predicate.name)) {
            return true;
        } else {
            for (PddlTerm sharedTerm : domain.sharedPredicates.objects) {
                if (predicate.isUnifiable(sharedTerm)) {
                    return true;
                }
            }
            return false;
        }
    }
}
