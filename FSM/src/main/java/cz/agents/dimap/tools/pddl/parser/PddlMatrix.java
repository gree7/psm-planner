package cz.agents.dimap.tools.pddl.parser;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import cz.agents.dimap.tools.pddl.expr.SExpr;
import cz.agents.dimap.tools.pddl.expr.SExprTranslator;
import cz.agents.dimap.tools.pddl.types.PddlType;

/*
 * A list of typed objects like in PDDL: (obj1 obj2 - typeA obj3 - typeB ... objN - typeZ).
 * 
 * Objects of the same type can be grouped in one list (obj1 obj2 - type) but semantically it is the same
 * as (obj1 - type obj2 - type).  This structure allows us to preserve the grouping as specified in PDDL.
 * 
 */
public class PddlMatrix<T> extends LinkedList<PddlList<T>> implements Serializable {
	private static final long serialVersionUID = 6382460150258160447L;
	
	public PddlMatrix() {
		super();
	}
	
	public PddlMatrix(SExpr expr, SExprTranslator<T> translator) {
		this();
		while (expr.isNonEmptyList()) {
			this.add(new PddlList<T>(expr, translator));
		}
	}
	
	private PddlMatrix(PddlMatrix<T> other) {
		this();
		for (PddlList<T> list : other) {
			add(list.clone());
		}
	}
	
    public LinkedHashMap<T, PddlType> constructBindings() {
        LinkedHashMap<T, PddlType> bindings = new LinkedHashMap<>();
        for (PddlList<T> list : this) {
            for (T obj : list.objects) {
                if (list.type != null) {
                    bindings.put(obj, list.type);
                } else {
                    bindings.put(obj, new PddlType("object"));
                }
            }
        }
        return bindings;
    }
	
	@Override
	public PddlMatrix<T> clone() {
		return new PddlMatrix<T>(this);
	}
	
}
