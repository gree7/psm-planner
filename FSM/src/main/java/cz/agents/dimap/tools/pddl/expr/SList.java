package cz.agents.dimap.tools.pddl.expr;

import java.util.List;

public class SList extends SExpr {
	public List<SExpr> exprs;
	
	public SList(List<SExpr> exprs) {
		this.exprs = exprs;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		for (SExpr e : exprs) {
			builder.append(e).append(" ");
		}
		if (exprs.size() > 0) {
			builder.setLength(builder.length() - 1);
		}
		builder.append(")");
		return builder.toString();
	}

	@Override
	public String getValue() {
		return null;
	}

	@Override
	public List<SExpr> getList() {
		return exprs;
	}
	
	@Override
	public boolean isValue() {
		return false;
	}

	@Override
	public boolean isList() {
		return true;
	}


}
