package cz.agents.dimap.tools.pddl.types;

import java.util.Map.Entry;

import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.expr.SExpr;
import cz.agents.dimap.tools.pddl.expr.SExprTranslator;

public class PddlFunctionType {
	
	public final PddlName functionName;
	public final PddlTypeMap<PddlName> arguments;
	public final PddlType resultType;
	
	public PddlFunctionType(PddlName functionName, PddlTypeMap<PddlName> argumentTypes, PddlType resultType)
	{
		this.functionName = functionName;
		this.arguments = argumentTypes;
		this.resultType = resultType;
	}
	
	public static final SExprTranslator<PddlFunctionType> TRANSLATOR = new SExprTranslator<PddlFunctionType>() {
		@Override
		public PddlFunctionType translate(SExpr expr) {
			PddlTypeMap<PddlPredicateType> tmp = new PddlTypeMap<>(expr, PddlPredicateType.TRANSLATOR);
			
			if (tmp.getBindings().size() != 1) {
				System.err.println("PDDL PARSE ERROR: Unsupported function type (more than one function in signature?): "+expr);
				return null;
			}
			
			for (Entry<PddlPredicateType, PddlType> entry : tmp.getBindings().entrySet()) {
				return new PddlFunctionType(entry.getKey().predicateName, entry.getKey().arguments, entry.getValue());
			}
			
			return null; // can never get there but the Java compiler can not infer that ;-) 
		}
	}; 
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		builder.append(functionName).append(" ");
		builder.append(arguments.toString(" ", false));
		builder.append(")");
		if (resultType != null) {
			builder.append(" - ");
			builder.append(resultType.toString());
		}
		return builder.toString();
	}

}
