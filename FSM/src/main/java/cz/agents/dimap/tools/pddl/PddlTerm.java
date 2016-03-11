package cz.agents.dimap.tools.pddl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cz.agents.dimap.psm.operator.Binding;
import cz.agents.dimap.tools.pddl.expr.SExpr;
import cz.agents.dimap.tools.pddl.expr.SExprTranslator;

public class PddlTerm implements Comparable<PddlTerm>, Serializable {

    private static final long serialVersionUID = 2920855699224686543L;

    public final PddlName name;
	public final List<PddlName> arguments;

	public PddlTerm(String name) {
		this(PddlName.createFixedName(name));
	}
	
	public PddlTerm(PddlName name) {
		this(name, new LinkedList<PddlName>());
	}
	
	private PddlTerm(PddlTerm other) {
		this(other.name);
		arguments.addAll(other.arguments);
	}

	public PddlTerm(SExpr e) {
		if (!e.isNonEmptyList()) {
			System.err.println("PDDL ERROR: Not a predicate: "+e);
		}
		name = PddlName.createFixedName(e.e(0).getValue());
		arguments = new LinkedList<PddlName>();
		for (int i = 1; i < e.getList().size(); i++) {
			arguments.add(PddlName.createFixedName(e.e(i).getValue()));
		}
	}
	
    public PddlTerm(PddlName name, List<PddlName> arguments) {
        this.name = name;
        this.arguments = new ArrayList<>(arguments);
    }

    // format: at(truck1, depot0pl)
    public static PddlTerm parse(String str) {
        String [] split = str.split("[(, )]");
        List<PddlName> argNames = new ArrayList<>();
        for (int i = 1; i < split.length; i++) {
            if (!split[i].isEmpty()) {
                argNames.add( new PddlName(split[i]) );
            }
        }
        return new PddlTerm(new PddlName(split[0]), argNames);
    }

    public static PddlTerm renamePredicate(PddlTerm predicate, String name) {
        return new PddlTerm(PddlName.createFixedName(name), predicate.arguments);
    }

    public static PddlTerm createGroundedPredicate( PddlTerm predicate, Binding binding) {
        List<PddlName> arguments = new ArrayList<>();
        for (PddlName arg : predicate.arguments) {
            arguments.add((arg.isVariable() &&  binding.containsParameter(arg)) ? binding.getParameterValue( arg ) : arg);
        }
        
        return new PddlTerm(predicate.name, arguments);
    }
    
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		for (PddlName arg : arguments) {
			builder.append(" ").append(arg);
		}
		return builder.toString();
	}

	@Override
	public int compareTo(PddlTerm other) {
		int comparison = this.name.compareTo(other.name);
		if (comparison != 0) {
			return comparison;
		}
		if (arguments != null && other.arguments != null && arguments.size() == other.arguments.size())
		{
			for (int i = 0; i < arguments.size(); i++) {
				comparison = arguments.get(i).compareTo(other.arguments.get(i));
				if (comparison != 0) {
					return comparison;
				}
			}
	        return 0;
		}
		else if (arguments == null && other.arguments != null) { return -1; }
		else if (arguments != null && other.arguments == null) { return +1; }
		else { return Integer.compare(arguments.size(), other.arguments.size()); }
	}

	public boolean isPublicInProblem(PddlProblem problem) {
		return problem.isPublicPredicate(this);
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		if (arguments != null) {
			for (PddlName arg : arguments) {
				result ^= arg.hashCode();
			}
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (!(obj instanceof PddlTerm)) { return false; }
		
		PddlTerm other = (PddlTerm)obj;
		return (compareTo(other) == 0);
	}
	
	@Override
	public PddlTerm clone() {
		return new PddlTerm(this);
	}
	
	public static final SExprTranslator<PddlTerm> TRANSLATOR = new SExprTranslator<PddlTerm>() {
		@Override
		public PddlTerm translate(SExpr expr) {
			return new PddlTerm(expr);
		}
	};

    public boolean isUnifiable(PddlTerm sharedTerm) {
        if (equals(sharedTerm)) {
            return true;
        } else if (!name.equals(sharedTerm.name)
                || arguments.size() != sharedTerm.arguments.size()) {
            return false;
        } else {
            for (int i=0; i<arguments.size(); i++) {
                if (!arguments.get(i).isVariable()
                        && !sharedTerm.arguments.get(i).isVariable()
                        && !arguments.get(i).equals(sharedTerm.arguments.get(i))) {
                    return false;
                }
            }
            return true;
        }
    }
    
    public boolean isGrounded() {
        for (PddlName arg : arguments) {
            if (arg.isVariable()) {
                return false;
            }
        }
        return true;
    }
}
