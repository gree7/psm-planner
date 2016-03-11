package cz.agents.dimap.tools.pddl.expr;

import java.util.List;


public class SValue extends SExpr {
	public String val;

	public SValue(String val) {
		this.val = val;
	}
	
	@Override
	public String toString() {
		return val;
	}

	@Override
	public String getValue() {
		return val;
	}

	@Override
	public List<SExpr> getList() {
		return null;
	}

	@Override
	public boolean isValue() {
		return true;
	}

	@Override
	public boolean isList() {
		return false;
	}

}
