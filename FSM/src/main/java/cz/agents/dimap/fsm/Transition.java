package cz.agents.dimap.fsm;

import java.io.Serializable;

public class Transition<S, O> implements Serializable {
    private static final long serialVersionUID = -9013193171735734990L;

    private final O operator;
	private final S toState;
	
	private int hashCode = 0;

	public Transition(O operator, S toState) {
		if (toState == null) {
			throw new Error("toState == null");
		}
		this.operator = operator;
		this.toState = toState;
	}

	public O getOperator() {
		return operator;
	}

	public S getToState() {
		return toState;
	}
	
   @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transition<?, ?> other = (Transition<?, ?>) obj;
        if (operator == null) {
            if (other.operator != null)
                return false;
        } else if (!operator.equals(other.operator))
            return false;
        if (toState == null) {
            if (other.toState != null)
                return false;
        } else if (!toState.equals(other.toState))
            return false;
        return true;
    }

	@Override
	public int hashCode() {
		if (hashCode == 0) {
			hashCode = operator.hashCode() +
						37 * toState.hashCode() ; 
		}
		return hashCode;
	}
	
	@Override
	public String toString() {
		return "<" + operator+"-->"+ toState + ">";
	}
}
