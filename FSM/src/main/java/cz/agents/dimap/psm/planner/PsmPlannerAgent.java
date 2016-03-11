package cz.agents.dimap.psm.planner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import cz.agents.dimap.Settings;
import cz.agents.dimap.fsm.FiniteStateMachineTools;
import cz.agents.dimap.fsm.Transition;
import cz.agents.dimap.landmarks.Landmark;
import cz.agents.dimap.landmarks.PddlLandmarkProblem;
import cz.agents.dimap.landmarks.PlanLandmarkCreator;
import cz.agents.dimap.landmarks.PublicPlanHardLandmark;
import cz.agents.dimap.landmarks.RecommendedLandmark;
import cz.agents.dimap.landmarks.RecommendedMultiLandmark;
import cz.agents.dimap.planset.ConcurrentPlanSet;
import cz.agents.dimap.planset.PlanSet;
import cz.agents.dimap.planset.PlanSetType;
import cz.agents.dimap.planset.SimplePlanSet;
import cz.agents.dimap.psm.PlanStateMachine;
import cz.agents.dimap.psm.PlanStateMachineTools;
import cz.agents.dimap.psm.PublicProjection;
import cz.agents.dimap.psm.operator.Operator;
import cz.agents.dimap.psm.operator.PddlOperatorFactory;
import cz.agents.dimap.psm.planner.causallinks.CausalLinks;
import cz.agents.dimap.psm.planner.causallinks.CausalState;
import cz.agents.dimap.psm.state.State;
import cz.agents.dimap.tools.dot.DotTools;
import cz.agents.dimap.tools.fd.Downward;
import cz.agents.dimap.tools.fd.DownwardException;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlActionInstance;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlProblem.PddlProblemStatus;
import cz.agents.dimap.tools.pddl.ma.ActionType;
import cz.agents.dimap.tools.polystar.PolyStarGoalReachebility;

public class PsmPlannerAgent extends PddlPlanner {

	private PlanStateMachine fsm;
	private PlanStateMachine publicFsm = null;
	private Map<String, Landmark> landmarks = new HashMap<>();
	private int iterationNumber;
	public final String agentName;
	public final int agentIndex;
	private File agentDir;
	
	@SuppressWarnings("unused")
	private File problemDir;
	
    public PddlProblem internalProblem;
	public final PddlLandmarkProblem pddlProblem;
	//private CausalGraph causalGraph;

	public Map<String, Operator> actionByName;
	
	//private long lmFreshStateId;
	//private Map<State, Long> lmStateId;
	private Map<String, Long> landmarkUsage;
    private CausalLinks causalLinks = new CausalLinks();
    private PlanSet<Operator> planSet;
    private Collection<Landmark> requiredCausalLandmarks = null;
    private File downwardInputDomainFile;
    private File downwardInputProblemFile;
    
    public long verificationDuration = 0;
    public int communicatedActions = 0;
    private boolean isPubliclySolvable;

	public PsmPlannerAgent(String agentName, PddlProblem problem, int agentIndex, boolean isPubliclySolvable) throws Exception {
		super(problem);
        this.isPubliclySolvable = isPubliclySolvable;

		pddlProblem = new PddlLandmarkProblem(problem);

		this.agentName = agentName;
		this.agentIndex = agentIndex;

		pddlProblem.setDefaultActionCosts();
		//initCausalGraph();

	    agentDir = new File(Files.createTempDirectory(Settings.TMP_FILES_PREFIX + agentName+"-").toUri());

	    downwardInputDomainFile = new File(agentDir, "domain.pddl"); 
        downwardInputProblemFile = new File(agentDir, "problem.pddl");

		actionByName = null;
		fsm = new PlanStateMachine(initState);
		iterationNumber = 0;
		landmarkUsage = new LinkedHashMap<String, Long>();
		
		switch (Settings.PLAN_SET_TYPE) {
        case PSM:
            planSet = new SimplePlanSet();
            break;
        case SIMPLE:
            planSet = new SimplePlanSet();
            break;
        case CONCURRENT:
            planSet = new ConcurrentPlanSet();
            break;
        default:
            throw new IllegalArgumentException("Unknown PLAN_SET_TYPE: " + Settings.PLAN_SET_TYPE);
        }
	}

	@SuppressWarnings("unused")
	@Deprecated
	private void initCausalGraph() {
		//causalGraph = new CausalGraph(pddlProblem);
		//requiredCausalLandmarks = causalGraph.computeRequiredCausalLandmarks(agentName);
		//if (Settings.VERBOSE) System.out.println("    internal facts with public deps: "+requiredCausalLandmarks);
		//if (Settings.DOT_CAUSAL_GRAPH) DotTools.dotCausalGraph(causalGraph, new File(agentDir, "causal_graph").getPath());
	}
	
	@Override
	public PlanStateMachine plan(boolean dotOutputAllowed, AskOtherAgents otherAgentPlanners) throws InterruptedException {
		this.iterationNumber++;
		List<List<String>> newPlans = generateNewPlan();
        if (newPlans.isEmpty()) {
            System.err.println(agentName+": ERROR: no new plan generated!");
            if (!Settings.ALLOW_PRIVATE_GOALS) {
                // it's an error in FD in most cases, so throw...
                throw new DownwardException("no new plan generated! (iteration "+iterationNumber+")");
            } else {
                // it can happen when we allow private goals
                return publicFsm;
            }
        }

        addNewPlans(newPlans, otherAgentPlanners);

        if (dotOutputAllowed) FiniteStateMachineTools.imgOutput(fsm, new File(agentDir, "iter-"+String.format("%03d" ,iterationNumber)+"-fsm-1INT").getPath(), labelFactory, goalCondition);
		
		PlanStateMachine pubFsm = fsm.clone();
		PublicProjection.publicProjection(pubFsm, bitSetStateFactory);
	    projectOperators(pubFsm, pddlProblem);
		if (dotOutputAllowed) FiniteStateMachineTools.imgOutput(pubFsm, new File(agentDir, "iter-"+String.format("%03d" ,iterationNumber)+"-fsm-2PUB").getPath(), labelFactory, publicGoalCondition);
		
	    pubFsm.setGoals( PlanStateMachineTools.getGoalStates(pubFsm, publicGoalCondition) );

		publicFsm = pubFsm;
		
		return pubFsm;
	}
	
    private static void projectOperators(PlanStateMachine fsm, PddlProblem problem) {
        Map<Operator, Operator> renameMap = new HashMap<>();
        for (State state : fsm.getStates()) {
            for (Transition<State, Operator> transition : fsm.getTransitions(state)) {
                Operator operator = transition.getOperator();
                PddlAction projectedAction = PddlAction.removeInternalGroundedParameters(operator.getAction(), problem);
                
                renameMap.put(operator, PddlOperatorFactory.createOperator(projectedAction, null));
            }
        }
        fsm.renameTransitions(renameMap);
    }
	
	public void reset() {
		fsm = new PlanStateMachine(initState);
		landmarkUsage.clear();
		resetLandmarks();
		causalLinks = new CausalLinks();
		planSet.clear();
	}

	private void addNewPlans(List<List<String>> newPlans, AskOtherAgents otherAgentPlanners) throws InterruptedException {
		Map<Integer, Integer> planStripInfos = new LinkedHashMap<>();
		int acceptedPlansCount = 0;
		int rejectedPlansCount = 0;
		int partiallyAcceptedPlansCount = 0;
		
	    for (int planIndex = 0; planIndex < newPlans.size(); planIndex++) {
	    	List<String> stringPlan = newPlans.get(planIndex);
			if (Settings.VERBOSE) System.out.println(agentName+": new plan generated");
			List<Operator> plan = parsePlan(stringPlan);
			if (Settings.DEBUG) { System.out.println(agentName+": new string plan "+stringPlan); System.out.println(agentName+": new plan "+plan); }
			if (Settings.VERIFY_PLANS && otherAgentPlanners.getOtherAgentNum() > 0) {
			    List<Operator> publicPlan = projectPlanToPublic(plan);

			    communicatedActions  += publicPlan.size() * otherAgentPlanners.getOtherAgentNum();

			    long startTime = System.currentTimeMillis();

			    int lastReachableMark = otherAgentPlanners.computeLastMarkReachableByAllAgents(PublicPlanHardLandmark.operatorsToNames(publicPlan));
			    verificationDuration += System.currentTimeMillis() - startTime;

			    if (lastReachableMark == publicPlan.size() || lastReachableMark == -1) {
			        addPlan(plan);
			        acceptedPlansCount++;
			    }
			    else {
			        if (lastReachableMark == 0) {
			            rejectedPlansCount++;
			        }
			        else {
			            List<Operator> partPlan = stripPlanAtMark(plan, lastReachableMark);
			            //propagated to PSM - but could be used to create landmarks only
			            addPlanToPsm(partPlan); 
			            partiallyAcceptedPlansCount++;
			        }
			        if (Settings.VERIFY_PLANS_STAR) {
			            int firstUnreachableIndex = getRealPublicActionIndex(plan, lastReachableMark+1);
			            if (firstUnreachableIndex != -1) {
			                planStripInfos.put(planIndex, firstUnreachableIndex);
			            }
			        }
			    }
			}
			else {
				addPlan(plan);
				acceptedPlansCount++;
			}
		}
	    
	    if (Settings.VERIFY_PLANS_STAR && !planStripInfos.isEmpty()) {
	    	Downward.stripPlans(agentDir, planStripInfos);	    	
	    }
	    
	    System.out.println(agentName+": planning done (accepted = "+acceptedPlansCount+"; part-accepted = "+partiallyAcceptedPlansCount+"; rejected = "+rejectedPlansCount+")");
	}

	private List<Operator> stripPlanAtMark(List<Operator> plan, int publicIndex) {
		int i = getRealPublicActionIndex(plan, publicIndex);
		if (i == -1) {
			System.err.println(agentName+": ERROR: Mark "+publicIndex+" was not found; not enough public actions in the plan: "+plan);
			return plan;
		}
		return plan.subList(0, i+1);
	}
	
	private int getRealPublicActionIndex(List<Operator> plan, int publicIndex) {
		int publicActionsCount = 0;
		for (int i = 0; i < plan.size(); i++) {
			Operator operator = plan.get(i);
			//if (operator.isPublic()) { 
			if (operator.getAction().getActionType() != ActionType.INTERNAL) {
				publicActionsCount++;
				if (publicActionsCount == publicIndex) {
					return i;
				}
			}
		}
		return -1;
	}

	public List<String> isPlanInternallyExtensible(List<String> plan) throws InterruptedException {
	    PddlProblem internalProblemNoCosts = internalProblem.clone();
	    internalProblemNoCosts.removeActionCosts();
	    PddlLandmarkProblem planVerifyProblem = new PddlLandmarkProblem(internalProblemNoCosts);
	    PublicPlanHardLandmark planHardLandmark = new PublicPlanHardLandmark(pddlProblem, plan);
	    planHardLandmark.addToProblem(planVerifyProblem);
        List<List<String>> result = Downward.runDownward(planVerifyProblem, "verify-"+agentName, true);
        return (result != null) ? result.get(0) : null;
    }

	public boolean isInternallyExtensible(String senderAgentName, List<Operator> publicPlan) throws InterruptedException {
		PddlLandmarkProblem planVerifyProblem = new PddlLandmarkProblem(internalProblem.clone());
		PublicPlanHardLandmark planHardLandmark = new PublicPlanHardLandmark(publicPlan, pddlProblem);
		planHardLandmark.addToProblem(planVerifyProblem);

		if (Settings.VERBOSE) { System.out.println(agentName+": int-extensibility check requested by "+senderAgentName+": "+publicPlan); }
		if (Settings.DEBUG) { System.out.println(agentName+": Running internal extensibility test for agent "+senderAgentName+":"); System.out.println(":: problem init ::"); System.out.println(planVerifyProblem.init); System.out.println(":: problem goal ::"); System.out.println(planVerifyProblem.goal); System.out.println(":: problem actions ::"); }
		if (Settings.DEBUG) { System.out.println(planVerifyProblem.domain.actions); System.out.println(); }
		
		PddlProblemStatus status = PolyStarGoalReachebility.computeGoalReachebilityStatus(planVerifyProblem);
		
		if (Settings.VERBOSE) { System.out.println(agentName+": int-extensibility check for "+senderAgentName+" finished: "+status); }
		
		return (status != PddlProblemStatus.UNSOLVABLE);
	}

	public int computeLastReachableMark(List<String> publicPlan) throws InterruptedException {
		if (Settings.VERBOSE) { System.out.println(agentName+": int-extensibility check requested: "+publicPlan); }
		
		PddlLandmarkProblem planVerifyProblem = new PddlLandmarkProblem(internalProblem.clone());
		PublicPlanHardLandmark planHardLandmark = new PublicPlanHardLandmark(pddlProblem, publicPlan);
		planHardLandmark.addToProblem(planVerifyProblem);
		int mark = PolyStarGoalReachebility.computeLastReachableMark(planVerifyProblem);
		
		if (Settings.VERBOSE) { System.out.println(agentName+":int-extensibility check finished; last reachable mark: "+mark+" of "+publicPlan.size()); }
		
		return mark;
	}

	
	public void resetLandmarks() {
		landmarks.clear();
		pddlProblem.resetRecommendedLandmarks();
	}
	
	public void addPublicLandmarks(Collection<Landmark> landmarks) {
		for (Landmark landmark : landmarks) {
			landmark.addToProblem(pddlProblem);
			this.landmarks.put(landmark.getAction().name, landmark);
		}
	}
	
	public void addLandmarks(Collection<Landmark> landmarks) {
		for (Landmark landmark : landmarks) {
			landmark.addToProblem(pddlProblem);
		}
	}

	public Collection<Landmark> computeRequiredLandmarks() {
		return requiredCausalLandmarks;		
	}

	static Collection<Landmark> computeCausalLinksLandmarks(CausalLinks causalLinks, String agentName, int agentIndex, File agentDir, int iterationNumber) {
        if (Settings.VERBOSE) {
            System.out.println("causalLinks: " + causalLinks);
        }

        Collection<Landmark> landmarks = new LinkedHashSet<>();
        for (CausalState state : causalLinks.getStates()) {
            if (state.equals( causalLinks.getInitState() )) {
                continue;
            }
            String operator = null;
            List<String> condIds = new ArrayList<>();
            for (Transition<CausalState, String> cond : causalLinks.getInverseTransitions(state)) {
                int condId = cond.getToState().id;
                if (condId == -1) {
                    condIds.add("init-"+state.id);
                } else {
                    condIds.add(condId+"-"+state.id);
                }
                operator = cond.getOperator();
            }
            List<String> effectIds = new ArrayList<>();
            for (Transition<CausalState, String> effect : causalLinks.getTransitions(state)) {
                effectIds.add(state.id+"-"+effect.getToState().id);
            }
            
            boolean myOp = operator.startsWith("pub_");
            landmarks.add(new RecommendedMultiLandmark(agentName, agentIndex, operator.substring(4), condIds, effectIds, myOp));
        }
        
        if (Settings.VERBOSE) {
            System.out.println("landmarks: " + landmarks);
        }
        if (Settings.DOT_LANDMARKS) {
            FiniteStateMachineTools.imgOutput(causalLinks, agentDir+"/iter-"+String.format("%03d", iterationNumber)+"-causalLinks", CausalLinks.getLabelFactory());
            DotTools.dotLandmarks(landmarks, new File(agentDir, "iter-"+String.format("%03d", iterationNumber)+"-landmarks").getPath()); 
        }
        return landmarks;
	}
    public Collection<Landmark> computePublicLandmarks() {
	    if (Settings.USE_CAUSAL_LINKS) {
	        return computeCausalLinksLandmarks(causalLinks, agentName, agentIndex, agentDir, iterationNumber);
	    } else {
    		if (publicFsm == null) {
    			throw new RuntimeException("There is no public automata projection.  Have you called method plan?");
    		}
    		
    		PlanStateMachine lmFsm = publicFsm.clone();
    		
    		if (Settings.DEBUG_LANDMARKS) {
    			FiniteStateMachineTools.imgOutput(lmFsm, new File(agentDir, "iter-"+String.format("%03d" ,iterationNumber)+"-fsm-3MARKS").getPath(), labelFactory, publicGoalCondition);
    		}
    		Map<State, Long> lmStateId = updateLmStateId(lmFsm);
    		
    		Collection<Landmark> landmarks = new LinkedHashSet<>();
    		for (State state : lmFsm.getStates()) {
                long condId = state == lmFsm.getInitState() ? -1 : lmStateId.get(state);
    		    for (Transition<State, Operator> transition : lmFsm.getTransitions(state)) {
    				boolean myOp = transition.getOperator().getName().startsWith("pub_");
    				boolean shortcut = lmFsm.isLandmarkShortcut(state, transition);
    				landmarks.add(new RecommendedLandmark(agentName, agentIndex, transition.getOperator().getAction(), condId, lmStateId.get(transition.getToState()), myOp, shortcut));
    		    }
		    }
            if (Settings.DOT_LANDMARKS) { DotTools.dotLandmarks(landmarks, new File(agentDir, "iter-"+String.format("%03d", iterationNumber)+"-landmarks").getPath()); }
            return landmarks;
	    }		
	}
	
	public void buildActionDicts() {
	    actionByName = new LinkedHashMap<String, Operator>(operators.size());
	    for (Operator op : operators) {
			actionByName.put(op.getName(), op);
		}
	}
	
	private Map<State, Long> updateLmStateId(PlanStateMachine newLmFsm) {
		Map<State, Long> lmStateId = new HashMap<>();
		lmStateId.clear();
		long lmFreshStateId = 0;

		for (State state : newLmFsm.getStates()) {
			if (!lmStateId.containsKey(state)) {
				lmStateId.put(state, lmFreshStateId++);
			}
		}
		
		if (newLmFsm.getStates().size() != lmFreshStateId) {
			System.err.println("ERROR: lmFreshStateId check failed");
		}
		
		return lmStateId;
	}

	private List<List<String>> generateNewPlan() throws InterruptedException {
		createDownwardInputFiles();
		return Downward.runDownward(agentDir, false);
	}
	
	private void createDownwardInputFiles() {
		try {
			File curDomainFile = new File(agentDir, "iter-"+String.format("%03d" ,iterationNumber)+"-domain.pddl");
			File curProblemFile = new File(agentDir, "iter-"+String.format("%03d" ,iterationNumber)+"-problem.pddl");
			
			if (downwardInputDomainFile.exists()) { Files.delete(downwardInputDomainFile.toPath()); }
			if (downwardInputProblemFile.exists()) { Files.delete(downwardInputProblemFile.toPath()); }
			if (curDomainFile.exists()) { Files.delete(curDomainFile.toPath()); }
			if (curProblemFile.exists()) { Files.delete(curProblemFile.toPath()); }
			
			pddlProblem.writeToFiles(downwardInputDomainFile.getPath(), downwardInputProblemFile.getPath());
			
			//PddlInstatiator.instatiateDomainFile(new File(problemDir, agentName+"-domain-costs.pddl"), downwardInputDomainFile, landmarks, cheepLandmarks, landmarkUsage);
			Files.copy(downwardInputDomainFile.toPath(), curDomainFile.toPath());
			//PddlInstatiator.instatiateProblemFile(new File(problemDir, agentName+"-problem-costs.pddl"), downwardInputProblemFile, landmarks, cheepLandmarks, landmarkUsage);
			Files.copy(downwardInputProblemFile.toPath(), curProblemFile.toPath());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addPlan(List<Operator> plan) {
	    addPlanToPsm(plan);

	    if (Settings.USE_CAUSAL_LINKS) {
	        causalLinks.addPlan( plan );
	    }

	    if (Settings.PLAN_SET_TYPE != PlanSetType.PSM) {
	        List<Operator> publicPlan = projectPlanToPublic(plan);
	        planSet.addPlan( publicPlan );
	    }
	}

	private void addPlanToPsm(List<Operator> plan) {
		State state = initState;
	    for (Operator operator : plan) {
	        State nextState = operator.tryToApply(state);
	        if (nextState == null) {
	            System.out.println("operator: " + operator);
	            System.out.println("nextState: " + nextState);
	            throw new IllegalArgumentException("Invalid plan!" + plan);
	        }
	        fsm.addTransition(state, operator, nextState);
	        state = nextState;
	    }
	}
	
    private List<Operator> projectPlanToPublic(List<Operator> plan) {
        List<Operator> publicPlan = new ArrayList<>(plan.size());
        for (Operator operator : plan) {
            PddlAction projectedAction = PddlAction.removeInternalGroundedParameters(operator.getAction(), pddlProblem);
            if (!projectedAction.effect.isEmpty()) {
                publicPlan.add( PddlOperatorFactory.createOperator(projectedAction, bitSetStateFactory) );
            }
        }
        return publicPlan;
    }



	private List<Operator> parsePlan(List<String> stringPlan) {
		List<Operator> plan = new ArrayList<Operator>(stringPlan.size());
		
		for (String opString : stringPlan) {
			Operator op = parseAction(opString);
			plan.add(op);
		}
		
		return plan;
	}

	private Operator parseAction(String opStr) {
		if (opStr == null) {
			throw new IllegalArgumentException("ERROR parsing action: null! ");
		}
		
		if (actionByName == null) {
			buildActionDicts();
		}

		String[] splitOpStr = opStr.split(" ");

        String opName;
        if (actionByName.containsKey(splitOpStr[0])) {
            opName = splitOpStr[0];
        } else if  (landmarks.containsKey(splitOpStr[0])) {
            String[] landmarkStr = splitOpStr[0].split("-");
            opName = landmarkStr[0];
            if (splitOpStr.length == 1) {
                splitOpStr = landmarkStr;
            } else {
                PddlAction action = pddlProblem.domain.getActionByPublicName(opName);
                splitOpStr = createParamsOfLandmark(splitOpStr, landmarkStr, action);
            }
        } else {
            throw new IllegalArgumentException(agentName+": ERROR parsing action: Unknown action "+opStr);
        }
        Operator operator = actionByName.get(opName);
        if (splitOpStr.length != 1) {
            operator = PddlOperatorFactory.groundOperator(operator, splitOpStr, bitSetStateFactory);
        }
		if (Settings.VERBOSE) System.out.println(agentName+": ["+(operator.isPublic() ? '*' : '!')+"] '"+opStr+"' ["+operator.getAction().getActionType()+"] ("+operator.getAction().name+")");
        return operator;
	}

    String[] createParamsOfLandmark(String[] actionDesc, String[] landmarkDesc, PddlAction action) {
        String[] params = new String[actionDesc.length + landmarkDesc.length - 1];
        params[0] = landmarkDesc[0];
        
        List<PddlName> publicParameters = PddlAction.findPublicParameters(action, pddlProblem);

        int landmarkIndex = 1;
        int actionParamIndex = 1;
        int curIndex = 1;
        for (PddlName parameter : action.parameters.getBindings().keySet()) {
            if (publicParameters.contains(parameter)) {
                params[curIndex] = landmarkDesc[landmarkIndex];
                landmarkIndex++;
            } else { 
                params[curIndex] = actionDesc[actionParamIndex];
                actionParamIndex++;
            }
            curIndex++;
        }
        return params;
    }

    public void addInitialPlansAsLandmarks(List<List<String>> initialPlans) {
        resetLandmarks();
        int index = 0;
        for (List<String> initialPlan : initialPlans) {
            addPublicPlanAsLandmarks(initialPlan, "relax"+index, 0);
            index++;
        }
    }
    
    public void addPublicPlanAsLandmarks(List<String> publicPlanStr, String agentName, int agentIndex) {
        List<PddlActionInstance> publicPlan = parsePublicPlan(publicPlanStr);
        List<Landmark> planLandmarks = PlanLandmarkCreator.planToRecommendedLandmarks(publicPlan, agentName, agentIndex);
        addPublicLandmarks(planLandmarks);
    }

    private List<PddlActionInstance> parsePublicPlan(List<String> stringPlan) {
        List<PddlActionInstance> plan = new ArrayList<>(stringPlan.size());
        for (String actionStr : stringPlan) {
            PddlActionInstance action = PddlActionInstance.parsePublicAction(actionStr, pddlProblem);
            plan.add(action);
        }
        
        return plan;
    }
    
    public void deleteTmpFiles() {
        Downward.deleteTmpDirectory(agentDir);
    }

    public CausalLinks getCausalLinks() {
        return causalLinks;
    }

    public PlanSet<Operator> getPlanSet() {
        return planSet;
    }

	public void initPlanVerifier() {
		if (Settings.DEBUG) { System.out.println(agentName+": problem "); System.out.println(pddlProblem); System.out.println(pddlProblem.domain); }
		internalProblem = pddlProblem.clone();
		Collection<PddlAction> toRemove = new HashSet<>();
		for (PddlAction action : internalProblem.domain.actions) {
			if (Settings.VERBOSE) System.out.println(agentName+": action: "+action.name+" ["+action.getActionType()+"]");
			if (!action.isInternal()) {
				toRemove.add(action);
			}
		}
		internalProblem.domain.actions.removeAll(toRemove);
	}

    public boolean isPubliclySolvable() {
        return isPubliclySolvable;
    }
}
