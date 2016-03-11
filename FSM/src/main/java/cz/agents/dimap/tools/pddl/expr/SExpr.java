package cz.agents.dimap.tools.pddl.expr;

import java.util.List;

import org.codehaus.jparsec.functors.Map;

/*
 * An S-Expression is either a value or a list of S-Expressions:
 * 
 *     E   ::=   V  |  (E1 E2 ... En)
 *     
 */
public abstract class SExpr {

	public abstract boolean isValue();
	public abstract boolean isList();
	
	public abstract String getValue();
	public abstract List<SExpr> getList();
	
	public SExpr shift() {
		if (isValue()) { System.err.println("S-EXPR ERROR: Can not shift a value."); return null; }
		if (isEmptyList()) { System.err.println("S-EXPR ERROR: Can not shift an empty list."); return null; }
		return getList().remove(0);
	}

	public boolean isValue(String val) {
		return isValue() && getValue().equals(val);
	}
	
	private boolean isNumericValue() {
		return isValue() && getValue().matches("\\d+");
	}
	
	public boolean isListOfSize(int size) {
		return (isList() && getList().size() == size);
	}
	
	public boolean isNonEmptyList() {
		return (isList() && getList().size() > 0);
	}

	public boolean isEmptyList() {
		return (isList() && getList().size() == 0);
	}
	
	public boolean isListOfValues() {
		if (!isList()) {
			return false;
		}
		for (SExpr e : getList()) {
			if (!e.isValue()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isPositivePredicate() {
		return (isNonEmptyList() & isListOfValues());
	}

	public boolean isNegativePredicate() {
		return (isListOfSize(2) && e(0).isValue("not") && e(1).isPositivePredicate());
	}
	
	public boolean isLiteral() {
		return (isPositiveFact() || isNegativeFact());
	}
	
	public boolean isFExpr() {
		if (isValue()) return true;
		if (!e(0).isValue()) return false;
		for (SExpr e : getList()) { // could skip the first item e(0)
			if (!e.isFExpr()) return false;			
		}
		return true;
	}
	
	public SExpr e(int n) {
		return getList().get(n);
	}

	public boolean isPositiveFact() {
		return (isListOfSize(1) && e(0).isValue());
	}
	
	public boolean isNegativeFact() {
		return (isListOfSize(2) && e(0).isValue("not") && e(1).isPositiveFact());
	}
	public boolean isIncrease() {
		return (isListOfSize(3) && e(0).isValue("increase") && e(1).isPositiveFact() && e(2).isNumericValue());
	}

	public boolean isEquality() {
		return (isListOfSize(3) && e(0).isValue("=") && e(1).isFExpr() && e(2).isValue());
	}
	
	public boolean isAssign() {
		return (isListOfSize(3) && e(0).isValue("assign") && e(1).isFExpr() && e(2).isValue());
	}
	
	public boolean isAndExpr() {
		return (isNonEmptyList() && (e(0).isValue("and") || e(0).isValue("AND")));
	}
	
	public static Map<String, SExpr> fromToken = new Map<String, SExpr>() {
		@Override
		public SExpr map(String from) {
			return new SValue(from);
		}
	};
	
	public static Map<List<SExpr>, SExpr> fromList = new Map<List<SExpr>, SExpr>() {
		@Override
		public SExpr map(List<SExpr> from) {
			return new SList(from);
		}
	};

	public boolean isPredicate() {
		return (isPositivePredicate() || isNegativePredicate());
	}
	
}
