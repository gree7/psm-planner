package cz.agents.dimap.psm;

import cz.agents.dimap.fsm.LabelFactory;
import cz.agents.dimap.psm.operator.Operator;
import cz.agents.dimap.psm.state.State;

public class SimplePublicLabelFactory implements LabelFactory<State, Operator> {

    @Override
    public String createStateLabel(State state) {
        return state.getName();
    }

    @Override
    public String createOperatorLabel(Operator operator) {
        return operator.getPublicName();
    }
}
