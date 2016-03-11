package cz.agents.dimap.tools.pddl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import cz.agents.dimap.psm.operator.Binding;

public class PddlActionInstance extends PddlAction {
    private static final long serialVersionUID = 6976381357086376922L;

    private final PddlAction originalAction;

    public PddlActionInstance(PddlAction action, Binding binding, boolean useUnderscoresInName) {
        super( createBindedActionName(action, binding, useUnderscoresInName),
                removeFromParameters(action.parameters, binding.getBindedParameters()),
                PddlCondition.createGroundedCondition(action.precondition, binding),
                PddlCondition.createGroundedCondition(action.effect, binding),
                action.getActionType()
                );
        this.originalAction = action;
    }

    public PddlActionInstance(PddlAction action, Binding binding) {
        this(action, binding, false);
    }

    public static PddlActionInstance createInstanceWithUnderscores(PddlAction action, Binding binding) {
        return new PddlActionInstance(action, binding, true);
    }

    
    private static String createBindedActionName(PddlAction action, Binding binding, boolean useUnderscoresInName) {
        String name = action.name.split(" ")[0];
        for (PddlName parameter : binding.getBindedParameters()) {
            name += useUnderscoresInName ? "_" : ' ';
            name += binding.getParameterValue(parameter).name;
        }

        return name;
    }

    public PddlAction getOriginalAction() {
        return originalAction;
    }
    
    public static PddlActionInstance groundAction(PddlAction action, String[] argStrings) {
        PddlName[] args = new PddlName[argStrings.length - 1];
        for (int i=1; i<argStrings.length; i++) {
            args[i-1] = new PddlName(argStrings[i]);
        }
        
        return PddlActionInstance.groundAction(action, args);
    }

    public static PddlActionInstance groundAction(PddlAction action, PddlName[] arguments) {
        if (action.parameters.getBindings().size() != arguments.length) {
            throw new IllegalArgumentException("Wrong number of parameters! " + action + " vs. " + Arrays.toString(arguments));
        }
        
        Binding binding = new Binding();
        
        int i=0;
        for (PddlName argName : action.parameters.getBindings().keySet()) {
            binding.addBinding(argName, arguments[i]);
            i++;
        }
        
        return new PddlActionInstance(action, binding);
    }

    public static PddlActionInstance parsePlannedPublicAction(String actionStr, PddlProblem problem) {
        String[] actionDesc = actionStr.split(" ");
        PddlAction action = findOriginalAction(actionDesc[0], problem.domain.actions);
        PddlAction projectedAction = PddlAction.parseAndProjectToPublic(actionStr, action, problem);
        if (projectedAction != null) {
            Binding binding = createBinding(actionDesc, action, projectedAction);
            PddlActionInstance bindedAction = new PddlActionInstance(projectedAction, binding);
            for (PddlTerm effect : bindedAction.getEffectFacts()) {
                if (problem.isPublicPredicate(effect)) {
                    return bindedAction;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public static PddlActionInstance parsePlannedAction(String actionStr, PddlProblem problem) {
        String[] actionDesc = actionStr.split(" ");
        PddlAction action = findOriginalAction(actionDesc[0], problem.domain.actions);
        if (action != null) {
            Binding binding = createBinding(actionDesc, action, action);
            return new PddlActionInstance(action, binding, true);
        } else {
            return null;
        }
    }

    static PddlAction findOriginalAction(String actionName, List<PddlAction> actions) {
        PddlAction retAction = null;
        for (PddlAction action : actions) {
            String actName = action.getName();

            if (actName.equals(actionName)) {
                if (retAction != null) {
                    System.out.println("Operator not unique! " + retAction + " -- " + action);
                }
                retAction = action;
            }
        }
        return retAction;
    }

    static Binding createBinding(String[] actionDesc, PddlAction action, PddlAction projectedAction) {
        Binding binding = new Binding();
        int i=1;
        for (PddlName parameter : action.parameters.getBindings().keySet()) {
            if (projectedAction.parameters.getBindings().containsKey(parameter)) {
                binding.addBinding(parameter, new PddlName(actionDesc[i]));
            }
            i++;
        }
        return binding;
    }

    public static PddlActionInstance parsePublicAction(String actionStr, PddlProblem problem) {
        String[] actionDesc = actionStr.split(" ");
        PddlAction action = findOriginalAction(actionDesc[0], problem.domain.actions);
        Binding binding = createBindingForPublicAction(actionDesc, action, problem);
        PddlActionInstance bindedAction = new PddlActionInstance(action, binding);
        return bindedAction;
    }

    static Binding createBindingForPublicAction(String[] actionDesc, PddlAction action, PddlProblem problem) {
        List<PddlName> publicParameters = findPublicParameters(action, problem);

        Binding binding = new Binding();
        if (!publicParameters.isEmpty()) {
            Iterator<PddlName> publicParameterIt = publicParameters.iterator();
            PddlName publicParameter = publicParameterIt.next();
            int index = 1;
            for (PddlName parameterName : action.parameters.getBindings().keySet()) {
                if (publicParameter.equals(parameterName)) {
                    binding.addBinding(parameterName, new PddlName(actionDesc[index]));
                    if (publicParameterIt.hasNext()) {
                        publicParameter = publicParameterIt.next();
                        index++;
                    } else {
                        break;
                    }
                }
            }
        }
        return binding;
    }
}
