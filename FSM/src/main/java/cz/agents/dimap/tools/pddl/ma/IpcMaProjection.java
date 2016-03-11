package cz.agents.dimap.tools.pddl.ma;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.alite.math.CartesianProduct;
import cz.agents.dimap.Settings;
import cz.agents.dimap.psm.operator.Binding;
import cz.agents.dimap.tools.ReachableFactsExtractor;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlActionInstance;
import cz.agents.dimap.tools.pddl.PddlCondition;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.types.PddlType;
import cz.agents.dimap.tools.pddl.types.PddlTypeMap;

public class IpcMaProjection extends PddlMaProjection implements MaProjection {

    private static final PddlName AGENT_TYPE = new PddlName("agent");
    private final Map<String, List<PddlAction>> agentActions = new HashMap<>();
    private final Map<String, List<PddlAction>> agentActionsInstances = new HashMap<>();
    
    private final Set<PddlTerm> reachableFacts = new HashSet<>();
    private final Set<PddlTerm> publicPredicates = new HashSet<>();
    private final Map<String, List<PddlTerm>> agentsInit = new HashMap<>();
    private final Map<String, List<PddlTerm>> agentsGoal = new HashMap<>();

    public IpcMaProjection(String domainFileName, String problemFileName, String addlFileName) throws IOException {
        this(new PddlProblem(domainFileName, problemFileName), new PddlAddl(addlFileName));
    }

    public IpcMaProjection(PddlProblem problem, PddlAddl addl) {
        super(problem, addl);

        separateActionsByAgent();

        generatePublicPredicates();
        
        groundActionsWithPartiallyPublicParameters();

        setActionPublicness();
    }

    private void generatePublicPredicates() {
        Map<String, Set<PddlTerm>> agentsFacts = new HashMap<>();
        ReachableFactsExtractor reachableFactsExtractor = new ReachableFactsExtractor(originalProblem);
        for (Entry<String, List<PddlAction>> actionsEntry : agentActionsInstances.entrySet()) {
            String agentName = actionsEntry.getKey();
            List<PddlAction> actions = actionsEntry.getValue();
            Set<PddlTerm> agentFacts = reachableFactsExtractor.getUsedFacts(actions);
            reachableFacts.addAll(agentFacts);
            if (!Settings.ALLOW_PRIVATE_GOALS) {
                agentFacts.addAll(originalProblem.goal.getFacts());
            }
            agentsFacts.put(agentName, agentFacts);

            List<PddlTerm> initFacts = new ArrayList<>(originalProblem.init.getFacts());
            initFacts.retainAll(agentFacts);
            agentsInit.put(agentName, initFacts);

            List<PddlTerm> goalFacts = new ArrayList<>(originalProblem.goal.getFacts());
            goalFacts.retainAll(agentFacts);
            agentsGoal.put(agentName, goalFacts);
        }
        
        Set<PddlTerm> publicFacts = getPublicFacts(agentsFacts);
        publicPredicates.addAll(publicFacts);
    }

    public static <T> Set<T> getPublicFacts(Map<?, Set<T>> agentsFacts) {
        Set<T> publicFacts = new HashSet<>();
        for (Entry<?, Set<T>> facts1 : agentsFacts.entrySet()) {
            for (Entry<?, Set<T>> facts2 : agentsFacts.entrySet()) {
                if (!facts1.getKey().equals(facts2.getKey())) {
                    Set<T> intersectionFacts = new HashSet<>(facts1.getValue());
                    intersectionFacts.retainAll(facts2.getValue());
                    publicFacts.addAll(intersectionFacts);
                }
            }
        }
        return publicFacts;
    }

    private void separateActionsByAgent() {
        for (PddlName agentName : agents.keySet()) {
            agentActions.put(agentName.name, new ArrayList<PddlAction>());
            agentActionsInstances.put(agentName.name, new ArrayList<PddlAction>());
        }
        
        for (PddlAction action : originalProblem.domain.actions) {
            for (Entry<PddlName, PddlType> agent : agents.entrySet()) {
                PddlName agentParameterName = containsType(action.parameters.getBindings(), agent.getValue(), originalProblem.domain);
                if (agentParameterName != null) {
                    Binding agentBinding = new Binding();
                    agentBinding.addBinding(agentParameterName, agent.getKey());
                    PddlAction agentAction = PddlActionInstance.createInstanceWithUnderscores(action, agentBinding);
                    agentActions.get(agent.getKey().name).add(PddlAction.changeType(action.clone(), ActionType.PARSED));
                    agentActionsInstances.get(agent.getKey().name).add(agentAction);
                }
            }
        }
    }
    
    private void groundActionsWithPartiallyPublicParameters() {
        Set<String> agentNames = agentActionsInstances.keySet();
        for (String agentName : agentNames) {
            List<PddlAction> actions = agentActionsInstances.get(agentName);
            List<PddlAction> groundedActions = new ArrayList<>();
            
            for (PddlAction action : actions) {
                Map<PddlName, PddlType> parameters = getPartiallyPublicParameters(action);
                if (parameters.isEmpty()) {
                    groundedActions.add(action);
                } else {
                    groundedActions.addAll(groundParameters(action, parameters));
                }
            }
            
            agentActionsInstances.put(agentName, groundedActions);
        }
    }

    private Collection<? extends PddlAction> groundParameters(PddlAction action, Map<PddlName, PddlType> parameters) {
        List<PddlAction> groundedActions = new ArrayList<>();
        
        List<Set<PddlName>> domainsDefinition = new ArrayList<>();
        List<PddlName> parameterNames = new ArrayList<>();
        LinkedHashMap<PddlName, PddlType> remainingParameters = new LinkedHashMap<>(action.parameters.getBindings());
        for (Entry<PddlName, PddlType> parameter : parameters.entrySet()) {
            parameterNames.add(parameter.getKey());
            remainingParameters.remove(parameter.getKey());
            domainsDefinition.add(originalProblem.getObjectsByType(parameter.getValue()));
        }
        
        // grounded precondition and effect
        CartesianProduct<PddlName> cartesianProduct = new CartesianProduct<PddlName>(domainsDefinition);
        for (int i = 0; i < cartesianProduct.size(); i++) {
            String label = action.getName();

            Binding binding = new Binding();
                           
            List<PddlName> element = cartesianProduct.element(i);
            int j = 0;
            for (PddlName constant : element) {
                binding.addBinding(parameterNames.get(j), constant);
                label += "_" + constant;
                ++j;
            }
            
            PddlCondition precondition = PddlCondition.createGroundedCondition(action.precondition, binding);
            PddlCondition effect = PddlCondition.createGroundedCondition(action.effect, binding);
            effect.removeContradictingEffects();
            if (isReachable(precondition) && isReachable(effect) && !effect.isEmpty()) {
                PddlAction newAction = new PddlAction(label, new PddlTypeMap<PddlName>(remainingParameters), precondition, effect, action.getActionType());
                groundedActions.add(newAction);
            }
        }
        
        return groundedActions;
    }

    private boolean isReachable(PddlCondition condition) {
        for (PddlTerm fact : condition.getFacts()) {
            if (!isReachable(fact)) {
                return false;
            }
        }
        return true;
    }

    private boolean isReachable(PddlTerm fact) {
        for (PddlTerm reachableFact : reachableFacts) {
            if (fact.isUnifiable(reachableFact)) {
                return true;
            }
        } 
        return false;
    }

    private Map<PddlName, PddlType> getPartiallyPublicParameters(PddlAction action) {
        Map<PddlName, PddlType> publicParameters = new HashMap<>();
        publicParameters.putAll(getPartiallyPublicParameters(action, action.getPreconditionFacts()));
        publicParameters.putAll(getPartiallyPublicParameters(action, action.getEffectFacts()));
        return publicParameters;
    }

    private Map<PddlName, PddlType> getPartiallyPublicParameters(PddlAction action, Set<PddlTerm> terms) {
        Map<PddlName, PddlType> publicParameters = new HashMap<>();
        for (PddlTerm term : terms) {
            if (isPartiallyPublic(term)) {
                for (PddlName arg : term.arguments) {
                    if (arg.name.startsWith("?")) {
                        publicParameters.put(arg, action.parameters.getBindings().get(arg));
                    }
                }
            }
        }
        return publicParameters;
    }

    private boolean isPartiallyPublic(PddlTerm term) {
        for (PddlTerm publicTerm : publicPredicates) {
            if (term.isUnifiable(publicTerm)) {
                return true;
            }
        }
        return false;
    }

    private void setActionPublicness() {
        for (List<PddlAction> actions : agentActionsInstances.values()) {
            for (PddlAction action : actions) {
                if (isPublicAction(action)) {
                    action.setActionType(ActionType.PUBLIC);
                } else {
                    action.setActionType(ActionType.INTERNAL);
                }
            }
        }
    }

    private boolean isPublicAction(PddlAction action) {
        List<PddlTerm> effects = new ArrayList<>(action.effect.positives);
        effects.addAll(action.effect.negatives);
        for (PddlTerm effect : effects) {
            for (PddlTerm publicTerm : publicPredicates) {
                if (publicTerm.isUnifiable(effect)) {
                    return true;
                }
            }
        }
        return false;
    }

    private PddlName containsType(Map<PddlName, PddlType> parameters, PddlType type, PddlDomain domain) {
        for (Entry<PddlName, PddlType> parameter : parameters.entrySet()) {
            if (parameter.getValue().equals(type)) {
                return parameter.getKey();
            } 
        }
        
        if ( type != null ) {
            // try super types
            for (PddlName typeName : type.eithers) {
                PddlName parameterName = containsType(parameters, domain.types.getBindings().get(typeName), domain);
                if (parameterName != null) {
                    return parameterName;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public PddlProblem generateAgentProblem(PddlName agentName) {
        PddlDomain agentDomain = generateAgentDomain(agentName);
        PddlProblem agentProblem = new PddlProblem(agentDomain);

        agentProblem.problemName = new PddlName(agentName.name+"-problem");
        agentProblem.objects = new PddlTypeMap<>(originalProblem.objects);
        agentProblem.init = originalProblem.init; //new PddlCondition(agentsInit.get(agentName.name), Collections.<PddlTerm>emptyList());
        agentProblem.goal = originalProblem.goal; //new PddlCondition(agentsGoal.get(agentName.name), Collections.<PddlTerm>emptyList());

        return agentProblem;
    }

    private PddlDomain generateAgentDomain(PddlName agentName) {
        PddlDomain agentDomain = new PddlDomain(originalProblem.domain);

        agentDomain.domainName = new PddlName(agentName.name+"-domain");
        agentDomain.actions.clear();
        agentDomain.actions.addAll(agentActionsInstances.get(agentName.name));
        agentDomain.sharedPredicates.objects.clear();
        agentDomain.sharedPredicates.objects.addAll(publicPredicates);
        
        addAgentTypeToTypes(agentDomain.types);

        return agentDomain;
    }

    private void addAgentTypeToTypes(PddlTypeMap<PddlName> types) {
        
        if (types.getBindings().containsKey(AGENT_TYPE)) {
            System.err.println("WARNING!!! Domain already contains type '"+AGENT_TYPE+"'!");
            return;
        }
        
        Set<PddlName> processedTypes = new HashSet<>();
        List<PddlName> typesNames = new ArrayList<>(agents.values().size());
        for (PddlName type : addl.agentTypes) {
            if (processedTypes.contains(type)) {
                continue;
            } else {
                processedTypes.add(type);
            }
            if (types.getBindings().get(type).eithers.size() != 1) {
                throw new IllegalArgumentException("Wrong type of agent!" + type + " -- " + types);
            }
            PddlName superType = types.getBindings().get(type).eithers.get(0);

            types.addAssignment(AGENT_TYPE, new PddlType(superType));
            types.removeAssignment(type);
            typesNames.add(type);
        }

        types.addAssignment(typesNames, new PddlType(AGENT_TYPE));
    }
}
