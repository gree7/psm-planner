package cz.agents.dimap.tools.pddl.expr;

public interface SExprTranslator<T> {
	
	public T translate(SExpr expr);

}
