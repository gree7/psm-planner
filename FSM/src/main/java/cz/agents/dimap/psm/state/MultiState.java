package cz.agents.dimap.psm.state;

import java.io.Serializable;
import java.util.BitSet;

public class MultiState implements Serializable {
    private static final long serialVersionUID = 7126067451105203003L;

    private final BitSetState bitState;
	private final BitSetState bitStateMask;
	
	public MultiState(BitSetState bitState, BitSetState bitStateMask) {
        this.bitState = new BitSetState(bitState);
        this.bitStateMask = new BitSetState(bitStateMask);
    }



    public boolean contains(State state) {
		BitSet bitSet = (BitSet) state.getBitSet().clone();
		bitSet.and(bitStateMask.getBitSet());
		return bitSet.equals(bitState.getBitSet());
	}

	public State apply(State state) {
		BitSet bitSet = (BitSet) ((BitSetState) state).getBitSet().clone();

		bitSet.andNot(bitStateMask.getBitSet());
		
		bitSet.or(bitState.getBitSet());
		
		return new BitSetState(bitSet);
	}

	@Override
	public String toString() {
		return bitState + "@" + bitStateMask;
	}

	public BitSetState getBitStateMask() {
		return bitStateMask;
	}

    public BitSetState getBitState() {
        return bitState;
    }
}
