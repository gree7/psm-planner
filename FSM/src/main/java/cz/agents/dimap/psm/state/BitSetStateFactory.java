package cz.agents.dimap.psm.state;

public interface BitSetStateFactory {

	String toString(State state);

	BitSetState projectToPublic(State state);
    MultiState projectToPublic(MultiState state);
}
