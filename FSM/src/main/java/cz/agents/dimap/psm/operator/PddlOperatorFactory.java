package cz.agents.dimap.psm.operator;

import java.util.Arrays;
import java.util.Collection;

import cz.agents.dimap.psm.state.BitSetStateFactoryPddl;
import cz.agents.dimap.psm.state.State;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlActionInstance;

public class PddlOperatorFactory {

    public static Operator createOperator(PddlAction action, BitSetStateFactoryPddl stateFactory) {
        if (stateFactory != null && action.isGrounded()) {
            return new PddlOperatorGrounded(action, stateFactory);
        } else {
            return new PddlOperator(action);
        }
    }
    
    public static Collection<Operator> groundOperatorInState(Operator operator, State state) {
        if (operator instanceof PddlOperatorGrounded) {
            return Arrays.asList(operator);
        } else if (operator instanceof PddlOperator) {
            return ((PddlOperator) operator).groundInState(state);
        } else {
            throw new IllegalArgumentException("Unknown type of operator: "+operator.getClass());
        }
    }

    public static Operator groundOperator(Operator operator, String[] argStrings, BitSetStateFactoryPddl stateFactory) {
        PddlAction action = operator.getAction();
        PddlAction groundedAction = PddlActionInstance.groundAction(action, argStrings);
        
        return new PddlOperatorGrounded(groundedAction, stateFactory);
    }
}
