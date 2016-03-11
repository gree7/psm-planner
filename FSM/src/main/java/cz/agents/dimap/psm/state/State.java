package cz.agents.dimap.psm.state;

import java.io.Serializable;
import java.util.BitSet;

public interface State extends Serializable {

	String getName();

	BitSet getBitSet();
}
