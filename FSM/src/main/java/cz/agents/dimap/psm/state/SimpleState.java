package cz.agents.dimap.psm.state;

import java.util.BitSet;


/**
 * Created with IntelliJ IDEA.
 * User: durkokar
 * Date: 1/13/14
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleState implements State {
    private static final long serialVersionUID = -4303021164032308606L;

    final private String publicName;
    final private int hashCode;
    public SimpleState(String publicName) {
        this.publicName = publicName;
        hashCode = publicName.hashCode();
    }

    @Override
    public String getName() {
        return publicName;
    }

	@Override
	public BitSet getBitSet() {
		throw new UnsupportedOperationException();
	}

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SimpleState other = (SimpleState) obj;
        if (publicName == null) {
            if (other.publicName != null)
                return false;
        } else if (!publicName.equals(other.publicName))
            return false;
        return true;
    }

    
    @Override
    public String toString() {
        return publicName;
    }
}
