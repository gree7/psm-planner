package cz.agents.dimap.psm;

import cz.agents.dimap.fsm.LabelFactory;
import cz.agents.dimap.psm.operator.Operator;
import cz.agents.dimap.psm.state.BitSetStateFactory;
import cz.agents.dimap.psm.state.State;

public class PlanLabelFactory implements LabelFactory<State, Operator> {

    private BitSetStateFactory bssFactory;

    public PlanLabelFactory(BitSetStateFactory bssFactory) {
        this.bssFactory = bssFactory;
    }
    
    @Override
    public String createStateLabel(State state) {
        return bssFactory.toString(state);
    }

    @Override
    public String createOperatorLabel(Operator operator) {
        return operator.getName();
    }
}
