package cz.agents.dimap.tools.pddl.parser;

import java.util.LinkedList;
import java.util.List;

import cz.agents.dimap.tools.pddl.expr.SExpr;
import cz.agents.dimap.tools.pddl.expr.SExprTranslator;
import cz.agents.dimap.tools.pddl.types.PddlType;

/*
 * A list of objects of a _single_ type (type can be possibly null).
 */
public class PddlList<T> {
	
	public List<T> objects;
	public PddlType type;
	
	public PddlList() {
		objects = new LinkedList<T>();
		type = null;
	}
	
	public PddlList(PddlList<T> other) {
		this();
		this.type = other.type;
		objects.addAll(other.objects);
	}
	
	public PddlList(SExpr expr, SExprTranslator<T> translator) {
		this();
		
		if (expr.isValue()) {
			System.err.println("PDDL ERROR: A list expected, has value: "+expr);
			return;
		}
		
		SExpr e;
		while (expr.isNonEmptyList()) {
			e = expr.shift();
			if (e.isValue("-")) {
				type = new PddlType(expr.shift());
				return;
			}
			objects.add(translator.translate(e));
		}
	}

	public PddlList(T obj, PddlType type) {
		this();
		objects.add(obj);
		this.type = type;
	}

	public PddlList(List<T> objs, PddlType type) {
		this();
		objects.addAll(objs);
		this.type = type;
	}
	
	@Override
	public String toString() {
		return toString(false);
	}
	
	public String toString(boolean parens) {
		StringBuilder builder = new StringBuilder();
		if (parens) { builder.append("("); }
		for (T obj : objects) {
			builder.append(obj).append(" ");
		}
		if (builder.length() > 0) { builder.setLength(builder.length() - 1); }
		if (parens) { builder.append(")"); }
		if (type != null) {
			builder.append(" - ").append(type);
		}
		return builder.toString();
	}
	
	@Override
	protected PddlList<T> clone()  {
		return new PddlList<T>(this);
	}

}
