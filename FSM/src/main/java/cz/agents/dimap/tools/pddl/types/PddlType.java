package cz.agents.dimap.tools.pddl.types;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.expr.SExpr;
import cz.agents.dimap.tools.pddl.expr.SExprTranslator;

public class PddlType implements Serializable {
    private static final long serialVersionUID = 1910927318239225159L;

    public List<PddlName> eithers;
	
	private PddlType() {
		eithers = new LinkedList<PddlName>();
	}
	
	public PddlType(String typeName) {
		this();
		addType(typeName);
	}
	
	public PddlType(PddlName type) {
		this();
		eithers.add(type);
	}
	
	public PddlType(List<String> rights) {
		this();
		for (String name : rights) {
			eithers.add(PddlName.createFixedName(name));
		}
	}

	public PddlType(SExpr expr) {
		this();
		if (expr.isValue()) {
			addType(expr.getValue());
			return;
		}
		else if (expr.isNonEmptyList() && expr.e(0).isValue("either")) {
			for (int i = 1; i < expr.getList().size(); i++) {
				addType(expr.e(i).getValue());
			}
		}
		else {
			System.err.println("WARNING: S-Expression does not represent a PDDL type: "+expr);
		}
	}
	
	public static final SExprTranslator<PddlType> TRANSLATOR = new SExprTranslator<PddlType>() {
		@Override
		public PddlType translate(SExpr expr) {
			return new PddlType(expr);
		}
	}; 
			
	private void addType(String typeName) {
		eithers.add(PddlName.createFixedName(typeName));
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eithers == null) ? 0 : eithers.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PddlType other = (PddlType) obj;
        if (eithers == null) {
            if (other.eithers != null)
                return false;
        } else if (!eithers.equals(other.eithers))
            return false;
        return true;
    }

    @Override
	public String toString() {
		if (eithers == null) { 
			return ""; 
		}
		else if (eithers.size() == 1) {
			return eithers.get(0).name;
		}
		else if (eithers.isEmpty()) {
			return "<any-type>";
		}
		StringBuilder builder = new StringBuilder();
		builder.append("(either");
		for (PddlName either : eithers) {
			builder.append(" ").append(either.name);
		}
		builder.append(")");
		
		return builder.toString();
	}
	
}
