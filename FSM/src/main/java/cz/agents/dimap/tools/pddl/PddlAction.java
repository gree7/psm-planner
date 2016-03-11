package cz.agents.dimap.tools.pddl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.dimap.Settings;
import cz.agents.dimap.tools.pddl.expr.SExpr;
import cz.agents.dimap.tools.pddl.ma.ActionType;
import cz.agents.dimap.tools.pddl.parser.PddlParser;
import cz.agents.dimap.tools.pddl.types.PddlFunctionType;
import cz.agents.dimap.tools.pddl.types.PddlType;
import cz.agents.dimap.tools.pddl.types.PddlTypeMap;

public class PddlAction implements Comparable<PddlAction>, Serializable {
    private static final long serialVersionUID = 1389968187997575347L;

    public final String name;
	public final PddlTypeMap<PddlName> parameters;
	public final PddlCondition precondition;
	public final PddlCondition effect;
	private ActionType actionType;
	
	private static final Set<String> doNotAddParametersToTheseActions = new HashSet<>(Arrays.asList(
            "cut_board_small",
            "cut_board_medium",
            "cut_board_large",
            "do_saw_small",
            "do_saw_medium",
            "do_saw_large"
	        ));
	
    private static final Map<String, String> useTheseDefaultValuesInsteadOfParameters = new HashMap<>();
    {
        useTheseDefaultValuesInsteadOfParameters.put("stack:?on_prev0", "nob");
        useTheseDefaultValuesInsteadOfParameters.put("do_glaze:?colour_prev0", "natural");
        useTheseDefaultValuesInsteadOfParameters.put("do_spray_varnish:?colour_prev0", "natural");
    }
	
	public PddlAction(SExpr value) {
	    this(value, PddlParser.makeActionDictionary(value));
	}

	private PddlAction(SExpr value, Map<String, SExpr> actionDictionary) {
	    this(
	    	getName(value, actionDictionary), 
	    	getParameters(value, actionDictionary),
	        getPreconditions(value, actionDictionary), 
	        getEffects(value, actionDictionary), 
	        ActionType.PARSED
	    );
	}
	
	private PddlAction(PddlAction other) {
        this(other.name, other.parameters, other.precondition, other.effect, other.getActionType());
    }

    public PddlAction(String name, PddlTypeMap<PddlName> parameters, PddlCondition precondition, PddlCondition effect, ActionType actionType) {
        this.name = name;
        this.precondition = precondition.clone();
        this.effect = effect.clone();
        this.parameters = parameters.clone();
        this.actionType = actionType;
    }

    public static PddlAction createRenamedAction(String name, PddlAction action) {
        return new PddlAction(name, action.parameters, action.precondition, action.effect, action.getActionType());
    }

    public static PddlAction createProjectedAction(String name, PddlAction action, Collection<PddlTerm> facts) {
        return new PddlAction(
                name, 
                action.parameters,
                PddlCondition.createProjectedCondition(action.precondition, facts),
                PddlCondition.createProjectedCondition(action.effect, facts),
                ActionType.PROJECTED
                );
    }

    public static PddlAction createExternalAction(PddlAction action, PddlProblem problem, boolean publicFactHasToBeInEffect) {
        PddlCondition projectedEffects = PddlCondition.createProjectedConditionToPredicateNames(action.effect, problem);
        PddlCondition projectedPreconds = PddlCondition.createProjectedConditionToPredicateNames(action.precondition, problem);
        if (projectedEffects.isEmpty() && projectedPreconds.isEmpty()) {
            return null;
        } else if (publicFactHasToBeInEffect && projectedEffects.isEmpty()) {
            return null;
        } else {
            List<PddlName> publicParameters = findPublicParameters(action, problem);
            return new PddlAction(
                    action.name, 
                    getRemainingParameters(action.parameters, publicParameters),
                    PddlCondition.createProjectedConditionToPredicateNames(action.precondition, problem),
                    projectedEffects,
                    ActionType.EXTERNAL
                    );
        }
    }

    public static PddlAction parseAndProjectToPublic(String name, PddlAction action, PddlProblem problem) {
        List<PddlName> publicParameters = findPublicParameters(action, problem);
        PddlCondition projectedEffects = PddlCondition.createProjectedConditionToPredicateNames(action.effect, problem);
        if (projectedEffects.isEmpty()) {
            return null;
        } else {
            return new PddlAction(
                    removedUnusedParametersFromName(name, publicParameters, action.parameters), 
                    getRemainingParameters(action.parameters, publicParameters),
                    PddlCondition.createProjectedConditionToPredicateNames(action.precondition, problem),
                    projectedEffects,
                    ActionType.PROJECTED
                    );
        }
    }

    private static String removedUnusedParametersFromName(String actionName, List<PddlName> publicParameters, PddlTypeMap<PddlName> parameters) {
        String[] actionNameStr = actionName.split(" ");
        String retName = actionNameStr[0];
        if (!publicParameters.isEmpty()) {
            Iterator<PddlName> publicParameterIt = publicParameters.iterator();
            PddlName publicParameter = publicParameterIt.next();
            int index = 1;
            for (PddlName parameterName : parameters.getBindings().keySet()) {
                if (publicParameter.equals(parameterName)) {
                    retName += ' ' + actionNameStr[index];
                    if (publicParameterIt.hasNext()) {
                        publicParameter = publicParameterIt.next();
                    } else {
                        break;
                    }
                }
                index++;
            }
        }
        return retName;
    }

    protected static PddlTypeMap<PddlName> removeFromParameters(PddlTypeMap<PddlName> parameters, Collection<PddlName> usedParameters) {
        Collection<PddlName> toRemove = new HashSet<>(parameters.getBindings().keySet());
        for (Entry<PddlName, PddlType> parameter : parameters.getBindings().entrySet()) {
            if (!usedParameters.contains(parameter.getKey())) {
                toRemove.remove(parameter.getKey());
            }
        }
        PddlTypeMap<PddlName> remaining = new PddlTypeMap<>(parameters);
        for (PddlName parameterName : toRemove) {
            remaining.removeAssignment(parameterName);
        }
        return remaining;
    }

    protected static PddlTypeMap<PddlName> getRemainingParameters(PddlTypeMap<PddlName> parameters, Collection<PddlName> usedParameters) {
        Collection<PddlName> toRemove = new HashSet<>();
        for (Entry<PddlName, PddlType> parameter : parameters.getBindings().entrySet()) {
            if (!usedParameters.contains(parameter.getKey())) {
                toRemove.add(parameter.getKey());
            }
        }
        PddlTypeMap<PddlName> remaining = new PddlTypeMap<>(parameters);
        for (PddlName parameterName : toRemove) {
            remaining.removeAssignment(parameterName);
        }
        return remaining;
    }

    public static List<PddlName> findPublicParameters(PddlAction action, PddlProblem agentProblem) {
        Set<PddlName> publicParameters = new HashSet<>();

        publicParameters.addAll( findPublicParameters(action.effect, agentProblem) );

        if (publicParameters.isEmpty()) {
            // no public effect => internal action
            return Collections.emptyList();
        } else {
            publicParameters.addAll( findPublicParameters(action.precondition, agentProblem) );

            List<PddlName> sortedPublicParameters = new ArrayList<>();
            for (PddlName parameterName : action.parameters.getBindings().keySet()) {
                if (publicParameters.contains(parameterName)) {
                    sortedPublicParameters.add(parameterName);
                }
            }

            return sortedPublicParameters;
        }
    }
    
    public static PddlAction removeInternalGroundedParameters(PddlAction action, PddlProblem problem) {
        String[] params = action.name.split(" ");
        if (params.length == 1) {
            return new PddlAction(
                    action.name, 
                    new PddlTypeMap<PddlName>(),
                    PddlCondition.createProjectedConditionToPredicateNames(action.precondition, problem),
                    PddlCondition.createProjectedConditionToPredicateNames(action.effect, problem),
                    ActionType.EXTERNAL
                    );
        } else { 
            PddlActionInstance bindedAction = (PddlActionInstance) action; 
            PddlAction originalAction = bindedAction.getOriginalAction();
            List<PddlName> publicParametersNames = findPublicParameters(originalAction, problem);
    
            if (publicParametersNames.size() == originalAction.parameters.getBindings().size()) {
                return PddlAction.changeType(action, ActionType.EXTERNAL);
            } else {
                int paramIndex = 1;
                String newName = params[0];
                for (PddlName paramName : originalAction.parameters.getBindings().keySet()) {
                    if (publicParametersNames.contains(paramName)) {
                        newName += ' ' + params[paramIndex];
                    }
                    paramIndex++;
                }
                return new PddlAction(
                        newName, 
                        new PddlTypeMap<PddlName>(),
                        PddlCondition.createProjectedConditionToPredicateNames(action.precondition, problem),
                        PddlCondition.createProjectedConditionToPredicateNames(action.effect, problem),
                        ActionType.EXTERNAL
                        );
            }
        }
    }

    private static Collection<? extends PddlName> findPublicParameters(PddlCondition condition, PddlProblem agentProblem) {
        Set<PddlName> publicParameters = new HashSet<>();
        publicParameters.addAll( findPublicParameters(condition.positives, agentProblem) );
        publicParameters.addAll( findPublicParameters(condition.negatives, agentProblem) );
        return publicParameters;
    }

    private static Collection<? extends PddlName> findPublicParameters(List<PddlTerm> predicates, PddlProblem agentProblem) {
        Set<PddlName> publicParameters = new HashSet<>();
        for (PddlTerm predicate : predicates) {
            if (agentProblem.isPublicPredicate(predicate)) {
                publicParameters.addAll(predicate.arguments);
            }

        }
        return publicParameters;
    }

    public static PddlAction createGroundedAction(String name, PddlCondition precondition, PddlCondition effect, ActionType actionType) {
        return new PddlAction(
                name, 
                new PddlTypeMap<PddlName>(),
                precondition,
                effect,
                actionType
                );
    }

    public static PddlAction changeType(PddlAction action, ActionType type) {
        return new PddlAction(
                action.name, 
                action.parameters,
                action.precondition,
                action.effect,
                type
                );
    }

    public static PddlAction renamePredicates(PddlAction action, Map<PddlTerm, PddlTerm> predicateRenames) {
        return new PddlAction(
                action.name, 
                action.parameters,
                PddlCondition.renamePredicates(action.precondition, predicateRenames),
                PddlCondition.renamePredicates(action.effect, predicateRenames),
                action.getActionType()
                );
    }


    public static PddlAction createRelaxedAction(PddlAction action) {
        return new PddlAction(
                action.name, 
                action.parameters,
                action.precondition,
                PddlCondition.relax(action.effect),
                action.getActionType()
                );
    }

	public boolean isPublic() {
	    checkValidPublicness();
		return getActionType() == ActionType.PUBLIC;
	}

	public boolean isInternal() {
        checkValidPublicness();
		return getActionType() == ActionType.INTERNAL;
	}

	public boolean isExternal() {
        checkValidPublicness();
		return getActionType() == ActionType.EXTERNAL;
	}
	
	private void checkValidPublicness() {
	    if (getActionType() == ActionType.PARSED || getActionType() == ActionType.PROJECTED) {
	        throw new IllegalStateException("Action should not be in state: " + getActionType());
	    }
    }

    public boolean isOwn() {
		return isPublic() || isInternal();
	}
	
	public String getName() {
		return name;
	}

	public void setDefaultCost() {
		if (isInternal()) {
			effect.setActionCost( 10 );
		}
		else if (isPublic()) {
			effect.setActionCost( 100 );
		}
		else if (isExternal()) {
			effect.setActionCost( 1000 );
		}
	}
	
	public void addPositivePrecondition(PddlTerm unlocked) {
		precondition.positives.add(unlocked);
	}

	public void addNegativePrecondition(PddlTerm fact) {
		precondition.negatives.add(fact);
	}
	
	public void addPrecondition(PddlTerm unlocked) {
		addPositivePrecondition(unlocked);
	}
	
	public void addPositiveEffect(PddlTerm fact) {
		effect.positives.add(fact);
	}

	public void addNegativeEffect(PddlTerm fact) {
		effect.negatives.add(fact);
	}

	public void setActionCost(long costValue) {
	    effect.setActionCost(costValue);
	}
	
	public void removeActionCost() {
		effect.removeActionCost();
	}

	private static PddlCondition getPreconditions(SExpr value, Map<String, SExpr> actionDict) {
		if (actionDict.containsKey(":precondition")) {
			return new PddlCondition(actionDict.get(":precondition"));
		}
		else {
			System.out.println("WARNING: No preconditions: "+value);
			return null;
		}
	}

	private static PddlCondition getEffects(SExpr value, Map<String, SExpr> actionDict) {
		if (actionDict.containsKey(":effect")) {
			return new PddlCondition(actionDict.get(":effect"));
		}
		else {
			System.out.println("WARNING: No effects: "+value);
			return null;
		}
	}

	private static String getName(SExpr value, Map<String, SExpr> actionDict) {
		if (actionDict.containsKey(":action")) {
			SExpr act = actionDict.get(":action");
			if (!act.isValue()) { System.out.println("PDDL WARNING: Action name is not a value: "+act); }
			return PddlName.fixName(act.getValue());
		}
		else {
			System.out.println("WARNING: No-name action: "+value);
			return "noname";
		}
	}

	private static PddlTypeMap<PddlName> getParameters(SExpr value, Map<String, SExpr> actionDict) {
		if (actionDict.containsKey(":parameters")) {
			SExpr params = actionDict.get(":parameters");
			if (!params.isList()) { System.out.println("PDDL WARNING: Paramaters are not a list: "+params); }
			return new PddlTypeMap<PddlName>(params, PddlName.TRANSLATOR); // TODO: size check
		}
		else {
            System.out.println("WARNING: No-parameter action: "+value);
		    return new PddlTypeMap<PddlName>();
		}
	}
	
	@Override
	public String toString() {
	    return toString(false);
	}
	
	public String toString(boolean includeActionPrefix) {
	    StringBuilder builder = new StringBuilder();
		builder.append("(:action ");
		if (includeActionPrefix) {
		    builder.append(getDownwardNamePrefix());
		}
		builder.append(name.replace(" ", "_")).append("\n");
		if (parameters.isEmpty()) {
			builder.append("\t:parameters ()\n");
		}
		else {
			builder.append("\t:parameters (\n\t\t").append(parameters.toString("\n\t\t", false)).append("\n\t)\n");
		}
		builder.append("\t:precondition ").append(precondition).append("\n");
		builder.append("\t:effect ").append(effect).append("\n");
		builder.append(")\n");
		return builder.toString();
	}
	
	private String getDownwardNamePrefix() {
	    switch (actionType) {
        case INTERNAL : return "int_";
        case PUBLIC : return "pub_";
        case EXTERNAL : return "ext_";
        default:
            return "";
	    }
    }

    public Set<PddlTerm> getPreconditionFacts() {
        return precondition.getFacts();
    }

    public Set<PddlTerm> getEffectFacts() {
        return effect.getFacts();
    }
    
	public void applyFluentDecomposition(Map<PddlName, PddlFunctionType> functionTypes) {
		// needs to be done in this order! (because effect decomposition can add a precondition) 
		applyEffectFluentDecomposition(functionTypes); 
		applyPreconditionFluentDecomposition();
	}

	private void applyPreconditionFluentDecomposition() {
		precondition.applyEqualitiesFluentDecomposition();
	}

	private void applyEffectFluentDecomposition(Map<PddlName, PddlFunctionType> functionTypes) {
		int uniqueId = 0;
		for (Entry<PddlTerm, PddlName> assign : effect.assigns.entrySet()) {
			PddlTerm assignTerm = assign.getKey();
			PddlName previousValue;
			if (precondition.equalities.containsKey(assignTerm)) {
				previousValue = precondition.equalities.get(assignTerm);
			}
			else {
				PddlFunctionType functionType = functionTypes.get(assignTerm.name);
				PddlType resultType;
				if (functionType == null) {
					System.err.println("PDDL ERROR: Undefined fluent "+assignTerm.name+".  Fluent decomposition might be wrong.");
					resultType = null;
				}
				else {
					resultType = functionType.resultType;
				}
				
				if (!doNotAddParametersToTheseActions.contains(name)) {
    				previousValue = new PddlName("?"+assignTerm.name+"_prev"+(uniqueId++));
    				if (!useTheseDefaultValuesInsteadOfParameters.containsKey(name+":"+previousValue)) {
    				    if (Settings.VERBOSE) {
    				        System.out.println("Adding parameter " + previousValue + " to action " + name);
    				    }
        				parameters.addAssignment(previousValue, resultType);
        				precondition.equalities.put(assignTerm.clone(), previousValue);
    				} else {
                        if (Settings.VERBOSE) {
                            System.out.println("Using default value instead of parameter " + previousValue + " in action " + name);
                        }
                        previousValue = new PddlName(useTheseDefaultValuesInsteadOfParameters.get(name+":"+previousValue));
    				}
				} else {
                    if (Settings.VERBOSE) {
                        System.out.println("Skipping adding parameter to action " + name);
                    }
	                previousValue = null;
				}
			}
			if (previousValue != null) {
    			PddlTerm unassignTerm = assignTerm.clone();
    			unassignTerm.arguments.add(previousValue);
    			effect.negatives.add(unassignTerm);
			}
			assignTerm.arguments.add(assign.getValue());
			effect.positives.add(assignTerm);
		}
		effect.assigns.clear();
	}
    
    @Override
	public PddlAction clone() {
		return new PddlAction(this);
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PddlAction other = (PddlAction) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

	@Override
	public int compareTo(PddlAction other) {
		return this.name.compareTo(other.name);
	}

    public boolean isGrounded() {
        return parameters.isEmpty();
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        if (this.actionType != ActionType.PARSED) {
            throw new IllegalStateException("Cannot change publicness of existing action! " + this.actionType);
        }
        this.actionType = actionType;
    }

}
