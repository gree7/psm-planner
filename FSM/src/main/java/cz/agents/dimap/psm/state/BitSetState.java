package cz.agents.dimap.psm.state;

import java.util.BitSet;

public class BitSetState implements State {
    private static final long serialVersionUID = -1600445957859712324L;

    private final BitSet state;
	private String toString = null;  //lazily evaluated
	private String name = null;
	
    public BitSetState(BitSet bitSet) {
		this.state = bitSet;
	}

	public BitSetState(BitSet bitSet, String name) {
		this.state = bitSet;
		this.name = name;
	}


	public BitSetState(BitSetState other) {
        this((BitSet) other.state.clone(), other.name);
    }

    @Override
	public boolean equals(Object obj) {
		if (! (obj instanceof BitSetState)) {
			return false;
		}
		BitSetState bsState = (BitSetState) obj;

		return getName().equals(bsState.getName()) && state.equals(bsState.state);
	}
	
	@Override
	public int hashCode() {
		return state.hashCode()+37*getName().hashCode();
	}
	
	public BitSet getBitSet() {
		return state;
	}

	@Override
	public String getName() {
		if (name == null) {
			name = toString();
		}
		return name;
	}

	@Override
	public String toString() {
		if (toString == null) {
			toString = state.toString();
		}
		return toString;
	}

    public void setName(String name) {
        this.name = name;
    }
}
