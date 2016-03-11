package cz.agents.dimap.tools.pddl.types;

import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.expr.SExpr;
import cz.agents.dimap.tools.pddl.expr.SExprTranslator;

public class PddlPredicateType {
	
	public final PddlName predicateName;
	public final PddlTypeMap<PddlName> arguments;
	
	public PddlPredicateType(PddlName functionName, PddlTypeMap<PddlName> argumentTypes)
	{
		this.predicateName = functionName;
		this.arguments = argumentTypes;
	}
	
	public PddlPredicateType(SExpr expr) {
		predicateName = PddlName.createFixedName(expr.shift().getValue());
		arguments = new PddlTypeMap<PddlName>(expr, PddlName.TRANSLATOR);
	}
	
	public PddlPredicateType(PddlName name) {
		this(name, new PddlTypeMap<PddlName>());
	}

	public static final SExprTranslator<PddlPredicateType> TRANSLATOR = new SExprTranslator<PddlPredicateType>() {
		@Override
		public PddlPredicateType translate(SExpr expr) {
			return new PddlPredicateType(expr);
		}
	}; 
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		builder.append(predicateName).append(" ");
		builder.append(arguments.toString(" ", false));
		builder.append(")");
		return builder.toString();
	}

}
