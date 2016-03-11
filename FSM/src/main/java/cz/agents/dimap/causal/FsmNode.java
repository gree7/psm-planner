package cz.agents.dimap.causal;

public interface FsmNode  {
    public FsmNode cloneWithSuffix(String suffix);
}