package cz.agents.dimap.tools.pddl.ma;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.types.PddlTypeMap;
import cz.agents.dimap.tools.pddl.types.PddlType;

public class FmapMaProjection extends PddlMaProjection implements MaProjection {

    private static final PddlName AGENT_TYPE = new PddlName("agent");
    private Set<PddlName> publicPredicates = new HashSet<>();
    private Map<String, Set<PddlAction>> agentActions = new HashMap<>();

    public FmapMaProjection(String domainFileName, String problemFileName, String addlFileName) throws IOException {
        this(new PddlProblem(domainFileName, problemFileName), new PddlAddl(addlFileName));
    }

    public FmapMaProjection(PddlProblem problem, PddlAddl addl) {
        super(problem, addl);

        publicPredicates.addAll(addl.sharedData.keySet());
        
        separateActionsByAgent();
    }

    private void separateActionsByAgent() {
        for (PddlName agentName : agents.keySet()) {
            agentActions.put(agentName.name, new HashSet<PddlAction>());
        }
        
        for (PddlAction action : originalProblem.domain.actions) {
            for (Entry<PddlName, PddlType> agent : agents.entrySet()) {
                if (containsType(action.parameters.getBindings().values(), agent.getValue(), originalProblem.domain)) {
                    agentActions.get(agent.getKey().name).add(action);
                }
            }
        }
    }

    private boolean containsType(Collection<PddlType> values, PddlType type, PddlDomain domain) {
        if (values.contains(type)) {
            return true;
        } else  if ( type != null ) {
            // try super types
            for (PddlName typeName : type.eithers) {
                if (containsType(values, domain.types.getBindings().get(typeName), domain)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public PddlProblem generateAgentProblem(PddlName agentName) {
        PddlDomain agentDomain = generateAgentDomain(agentName);
        PddlProblem agentProblem = new PddlProblem(agentDomain);

        agentProblem.problemName = new PddlName(agentName.name+"-problem");
        agentProblem.objects = new PddlTypeMap<>(originalProblem.objects);
        agentProblem.init = removeOtherAgentsInternalFacts(originalProblem.init, agentDomain);
        agentProblem.goal = originalProblem.goal.clone();

        return agentProblem;
    }

    private PddlCondition removeOtherAgentsInternalFacts(PddlCondition condition, PddlDomain domain) {
        List<PddlTerm> positives = new ArrayList<>(condition.positives.size());
        for (PddlTerm predicate : condition.positives) {
            if (domain.sharedPredicateNames.contains(predicate.name)
                    || isOwnInternalPredicate(predicate.name, domain.actions)) {
                positives.add(predicate);
            }
        }
        return new PddlCondition(positives, Collections.<PddlTerm>emptyList());
    }

    private boolean isOwnInternalPredicate(PddlName name, List<PddlAction> actions) {
        for (PddlAction action : actions) {
            if (containsPredicate(action.precondition, name) || containsPredicate(action.effect, name)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsPredicate(PddlCondition condition, PddlName name) {
        return containsPredicate(condition.positives, name);
    }

    private boolean containsPredicate(List<PddlTerm> predicates, PddlName name) {
        for (PddlTerm predicate : predicates) {
            if (predicate.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    private PddlDomain generateAgentDomain(PddlName agentName) {
        PddlDomain agentDomain = new PddlDomain(originalProblem.domain);

        agentDomain.domainName = new PddlName(agentName.name+"-domain");
        agentDomain.actions.clear();
        agentDomain.actions.addAll(agentActions.get(agentName.name));
        agentDomain.sharedPredicateNames.addAll(publicPredicates);
        
        addAgentTypeToTypes(agentDomain.types);

        return agentDomain;
    }

    private void addAgentTypeToTypes(PddlTypeMap<PddlName> types) {
        
        if (types.getBindings().containsKey(AGENT_TYPE)) {
            throw new IllegalArgumentException("Domain already contains type '"+AGENT_TYPE+"'!");
        }
        
        Set<PddlType> processedTypes = new HashSet<>();
        for (PddlType agentType : agents.values()) {
            if (processedTypes.contains(agentType)) {
                continue;
            } else {
                processedTypes.add(agentType);
            }
            if (agentType.eithers.size() != 1) {
                throw new IllegalArgumentException("Wrong type of agent!" + agentType + " -- " + types);
            }
            PddlName type = agentType.eithers.get(0);

            if (types.getBindings().get(type).eithers.size() != 1) {
                throw new IllegalArgumentException("Wrong type of agent!" + agentType + " -- " + types);
            }
            PddlName superType = types.getBindings().get(type).eithers.get(0);

            types.addAssignment(AGENT_TYPE, new PddlType(superType));
            types.removeAssignment(type);
        }

        List<PddlName> typesNames = new ArrayList<>(agents.values().size());
        for (PddlType type : agents.values()) {
            typesNames.add(type.eithers.get(0));
        }

        types.addAssignment(typesNames, new PddlType(AGENT_TYPE));
    }
}
