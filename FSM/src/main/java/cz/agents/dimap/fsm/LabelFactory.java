package cz.agents.dimap.fsm;

public interface LabelFactory<S, O> {
    
    public String createStateLabel(S state);
    public String createOperatorLabel(O operator);
}
