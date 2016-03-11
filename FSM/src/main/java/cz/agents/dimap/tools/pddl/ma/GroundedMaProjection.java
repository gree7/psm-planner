package cz.agents.dimap.tools.pddl.ma;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlCondition;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.types.PddlPredicateType;

/**
 * Created with IntelliJ IDEA.
 * User: durkokar
 * Date: 1/7/14
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroundedMaProjection extends PddlMaProjection implements MaProjection {

    private Set<PddlTerm> allFacts = new HashSet<>();
    private Set<PddlTerm> publicFacts = new HashSet<>();
    private Map<String, Set<PddlAction>> agentActions = new HashMap<>();
    private Map<String, Set<PddlTerm>> agentFacts = new HashMap<>();
    private Map<PddlTerm, PddlTerm> factNewNames = new HashMap<>();

    private Map<String, Set<PddlAction>> agentPublicActions = new HashMap<>();
    private Map<String, Set<PddlAction>> agentInternalActions = new HashMap<>();
    private Map<String, Set<PddlAction>> externalActions = new HashMap<>();
    private Map<String, Set<PddlAction>> agentProjectedActions = new HashMap<>();

    private PddlCondition goalFacts;
    private PddlCondition initFactsGlobal;
    public GroundedMaProjection(String domainFileName, String problemFileName, String addlFileName) throws IOException {
        this(new PddlProblem(domainFileName, problemFileName), new PddlAddl(addlFileName));
    }

    public GroundedMaProjection(PddlProblem problem, PddlAddl addl) {
        super(problem, addl);
        
        Preprocessor preprocessor = new Preprocessor(problem.domain, problem);

        List<PddlAction> allActions = preprocessor.getDomain().actions;
        agentActions = separateActionsByAgent(allActions);
        goalFacts = preprocessor.getGoal();
        initFactsGlobal = preprocessor.getInit();

        runSeparationProcess();
    }

    private void runSeparationProcess() {
        extractFacts();
        createFactNewNames();
        extractAgentsActions();       
        createExternalProjectedActions();
        extractAgentsProjections();
    }

    /* (non-Javadoc)
     * @see cz.agents.dimap.tools.pddl.ma.MaProjection#generateAgentProblem(cz.agents.dimap.tools.pddl.PddlName)
     */
    @Override
    public PddlProblem generateAgentProblem(PddlName agentName) {
        PddlDomain agentDomain = generateAgentDomain(agentName);
        PddlProblem agentProblem = new PddlProblem(agentDomain);

        agentProblem.problemName = new PddlName(agentName.name+"-problem");
        Set<PddlTerm> facts = agentFacts.get(agentName.name);
        facts.addAll(publicFacts);
        PddlCondition projectedInit = PddlCondition.createProjectedCondition(initFactsGlobal, facts);
        agentProblem.init = PddlCondition.renamePredicates(projectedInit, factNewNames);
        agentProblem.goal = PddlCondition.renamePredicates(goalFacts.clone(), factNewNames);

        return agentProblem;
    }

    private PddlDomain generateAgentDomain(PddlName agentName) {
        PddlDomain agentDomain = new PddlDomain();

        agentDomain.domainName = new PddlName(agentName.name+"-domain");
        agentDomain.actions.addAll(agentProjectedActions.get(agentName.name));
        Set<PddlTerm> agentRelevantFacts = new HashSet<>(agentFacts.get(agentName.name));
        agentRelevantFacts.addAll(publicFacts);
        for (PddlTerm fact : agentRelevantFacts) {
        	PddlPredicateType predicateType = new PddlPredicateType(factNewNames.get(fact).name);
            agentDomain.predicateTypes.put(predicateType.predicateName, predicateType);
        }

        return agentDomain;
    }

    private Map<String, Set<PddlAction>> separateActionsByAgent(List<PddlAction> actions) {
        Map<String, Set<PddlAction>> agentActions = new HashMap<>();
        for (PddlName agentName : agents.keySet()) {
            agentActions.put(agentName.name, new HashSet<PddlAction>());
        }

        for (PddlAction action : actions) {
            for (PddlName agentName : agents.keySet()) {
                if (action.name.contains("_" + agentName.name)) {
                    agentActions.get(agentName.name).add(action);
                }
            }
        }
        
        return agentActions;
    }

    private void extractFacts() {
        // extract absolutely all facts
        for ( Entry<String, Set<PddlAction>> agent : agentActions.entrySet() ) {
            Set<PddlTerm> facts = new HashSet<>();
            for (PddlAction action : agent.getValue()) {
                facts.addAll(action.getPreconditionFacts());
                facts.addAll(action.getEffectFacts());
            }
            agentFacts.put(agent.getKey(), facts);
            allFacts.addAll(facts);
        }

        allFacts.addAll(goalFacts.getFacts());
        
        if ( addl!=null && addl.hasSharedData()) {
            extractPublicFactsBySharedData();
        } else {
            extractPublicFactsByDomshlak();
        }
    }

    private void extractPublicFactsBySharedData() {
        publicFacts.addAll(goalFacts.getFacts());

        for (PddlTerm fact : allFacts) {
            if (addl.sharedData.containsKey( getFactName(fact))) {
                publicFacts.add( fact );
            }
        }
    }

    private PddlName getFactName(PddlTerm fact) {
        PddlName name = fact.name;
        return new PddlName(name.name.split("-")[0]);
    }

    private void extractPublicFactsByDomshlak() {
        publicFacts.addAll(goalFacts.getFacts());

        Set<PddlTerm> processedFacts = new HashSet<>(goalFacts.getFacts());

        for (Set<PddlTerm> facts : agentFacts.values()) {
            Set<PddlTerm> curAgentFacts = new HashSet<>();

            for (PddlTerm fact : facts) {
                if ( processedFacts.contains(fact) ) {
                    publicFacts.add( fact );
                }
                curAgentFacts.add(fact);
            }

            processedFacts.addAll(curAgentFacts);
        }
    }

    private void createFactNewNames() {
        for (PddlTerm fact : allFacts) {
            PddlTerm newFact;
            if (publicFacts.contains(fact)) {
                newFact = PddlTerm.renamePredicate(fact, "pub_"+fact.name);
            } else {
                newFact = PddlTerm.renamePredicate(fact, "int_"+fact.name);
            }
            factNewNames.put(fact, newFact);
        }

    }

    private void extractAgentsActions() {
        for ( Entry<String, Set<PddlAction>> agent : agentActions.entrySet() ) {
            extractOwnActions(agent.getKey(), agent.getValue());
        }
    }

    private void extractOwnActions(String agentName, Set<PddlAction> actions) {
        Set<PddlAction> publicActions = new HashSet<>();
        Set<PddlAction> internalActions = new HashSet<>();
        for ( PddlAction act : actions ) {
            if ( isActPublic(act) ) {
                publicActions.add(act);
            } else {
                internalActions.add(act);
            }
        }

        agentPublicActions.put(agentName, publicActions);
        agentInternalActions.put(agentName, internalActions);
    }

    private boolean isActPublic(PddlAction act) {
        for ( PddlTerm fact : publicFacts ) {
            if ( ProjectionCreator.ALLOW_ONLY_PUBLIC_PRECONDITION && act.getPreconditionFacts().contains( fact )) {
                return true;
            } else {
                if ( act.getEffectFacts().contains( fact ) ) {
                    return true;
                }
            }
        }
        return false;
    }

    private void extractAgentsProjections() {

        for ( String agent : agentActions.keySet() ) {
            HashSet<PddlAction> projectedActions = new HashSet<PddlAction>();

            for (PddlAction action :  agentInternalActions.get( agent )) {
                projectedActions.add( renameToInternal(action) );
            }

            for (PddlAction action :  agentPublicActions.get( agent )) {
                projectedActions.add( renameToPublic(action) );
            }

            for (Entry<String, Set<PddlAction>> agentExternal : externalActions.entrySet()) {
                if ( !agent.equals(agentExternal.getKey()) ) {
                    projectedActions.addAll(agentExternal.getValue());
                }
            }

            agentProjectedActions.put(agent, projectedActions);
        }
    }

    private void createExternalProjectedActions() {

        for ( Entry<String, Set<PddlAction>> agent : agentPublicActions.entrySet() ) {
            HashSet<PddlAction> projectedActions = new HashSet<PddlAction>();

            for (PddlAction action : agent.getValue()) {
                PddlAction externalAction = createExternalProjectedAction(action);
                if (!externalAction.effect.isEmpty()) {
                	projectedActions.add( externalAction );
                }
            }

            externalActions.put(agent.getKey(), projectedActions);
        }
    }

    private PddlAction createExternalProjectedAction(PddlAction action) {
        PddlAction projectedAction = PddlAction.createProjectedAction("ext_"+action.getName(), action, publicFacts);
        return PddlAction.renamePredicates(projectedAction, factNewNames);
    }

    private PddlAction renameToInternal(PddlAction action) {
        PddlAction internalAction = PddlAction.createRenamedAction("int_"+action.getName(), action);
        return PddlAction.renamePredicates(internalAction, factNewNames); 
    }

    private PddlAction renameToPublic(PddlAction action) {
        PddlAction publicAction = PddlAction.createRenamedAction("pub_"+action.getName(), action);
        return PddlAction.renamePredicates(publicAction, factNewNames); 
    }

    // for tests 
    GroundedMaProjection(Map<String, Set<PddlAction>> agentActions, PddlCondition goal) {
        this.agentActions = agentActions;
        this.goalFacts = goal;
        runSeparationProcess();
    }

    Map<String, Set<PddlAction>> getAgentProjectedActions() {
        return agentProjectedActions;
    }

}
