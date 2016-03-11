package cz.agents.dimap.tools.pddl.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.dimap.tools.pddl.expr.SExpr;
import cz.agents.dimap.tools.pddl.expr.SExprTranslator;
import cz.agents.dimap.tools.pddl.parser.PddlList;
import cz.agents.dimap.tools.pddl.parser.PddlMatrix;

/*
 * List of assignments like 
 *   variables to their types, 
 *   constants to their types, 
 *   types to their supertypes, 
 *   ... and so on .... 
 */
public class PddlTypeMap<T> implements Serializable {
    private static final long serialVersionUID = 8224838507829737682L;

    private final LinkedHashMap<T, PddlType> bindings;
	
    private final PddlMatrix<T> assignments;

    public PddlTypeMap() {
    	bindings = new LinkedHashMap<>();
    	assignments = new PddlMatrix<>();
    }

    public PddlTypeMap(PddlMatrix<T> ass) {
        assignments = ass;
        bindings = ass.constructBindings();
    }

    public PddlTypeMap(PddlMatrix<T> ass, PddlTypeMap<T> constants) {
        assignments = ass;
        assignments.addAll(constants.assignments);
        bindings = assignments.constructBindings();
    }


    public PddlTypeMap(LinkedHashMap<T, PddlType> bindings) {
    	this.bindings = bindings;
        assignments = constructMatrix(bindings);
    }

    private static <T> PddlMatrix<T> constructMatrix(Map<T, PddlType> bindings) {
    	PddlMatrix<T> matrix = new PddlMatrix<T>();
    	for (Entry<T, PddlType> entry : bindings.entrySet()) {
    		matrix.add(new PddlList<T>(entry.getKey(), entry.getValue()));
    	}
    	return matrix;
	}

	public PddlTypeMap(PddlTypeMap<T> other) {
        this(other.assignments.clone());
    }

    public PddlTypeMap(SExpr expr, SExprTranslator<T> translator) {
    	this(new PddlMatrix<T>(expr, translator));
    }

    public PddlTypeMap(SExpr expr, SExprTranslator<T> translator, PddlTypeMap<T> constants) {
        this(new PddlMatrix<T>(expr, translator), constants);
    }

    public void addAssignment(T obj, PddlType type) {
    	if (!getBindings().containsKey(obj)) {
    		PddlList<T> as = new PddlList<T>(obj, type);
            assignments.add(as);
            getBindings().put(obj, type);
        }
    }

    public void addAssignment(List<T> objs, PddlType type) {
		PddlList<T> as = new PddlList<T>(objs, type);
        assignments.add(as);
        for (T obj : objs) {
        	getBindings().put(obj, type);
        }
    }
    
    public void removeAssignment(T obj) {
        bindings.remove(obj);
        Collection<PddlList<T>> toRemove = new ArrayList<>(assignments.size());
        for (PddlList<T> assignment : assignments) {
            assignment.objects.remove(obj);
            if (assignment.objects.isEmpty()) {
                toRemove.add(assignment);
            }
        }
        assignments.removeAll(toRemove);
    }

    public boolean isEmpty() {
        return getBindings().isEmpty();
    }

	public Set<T> getObjects() {
		return getBindings().keySet();
	}
	
	public boolean containsObject(T obj) {
		return getBindings().containsKey(obj);
	}
    
	public PddlType getObjectType(T obj) {
		return getBindings().get(obj);
	}
    
    public LinkedHashMap<T, PddlType> getBindings() {
        return bindings;
    }

    public boolean hasAssignments() {
        return assignments != null && !assignments.isEmpty();
    }
    
    public int size() {
    	return getBindings().size();
    }
    
	public String toString(String delim, boolean parensAroundLists) {
		StringBuilder builder = new StringBuilder();
		for (PddlList<T> as : assignments) {
			builder.append(as.toString(parensAroundLists)).append(delim);
		}
		if (builder.length() > 0) { builder.setLength(builder.length() - delim.length()); }
		return builder.toString();
	}

    @Override
    public String toString() {
        return toString(" ", false);
    }

    @Override
    public PddlTypeMap<T> clone() {
        return new PddlTypeMap<T>(this);
    }

    public static <T> PddlTypeMap<T> createUnion(PddlTypeMap<T> map1, PddlTypeMap<T> map2) {
        PddlMatrix<T> union = new PddlMatrix<T>();
        union.addAll(map1.assignments);
        PddlTypeMap<T> list2clone = map2.clone();
        for (T obj : map1.getObjects()) {
        	if (list2clone.containsObject(obj)) {
        		if (!list2clone.getObjectType(obj).equals(map1.getObjectType(obj))) {
        			System.err.println("ERROR: Incompatible union of PddlTypeMap for object "+obj+": map1 type is "+map1.getObjectType(obj)+" while map2 type is"+list2clone.getObjectType(obj));
        		}
        		list2clone.removeAssignment(obj);
        	}
        }
        union.addAll(list2clone.assignments);
        return new PddlTypeMap<T>(union);
    }
    
	public static <T> SExprTranslator<PddlTypeMap<T>> TRANSLATOR(final SExprTranslator<T> innerTranslator) {
		return new SExprTranslator<PddlTypeMap<T>>() {
			@Override
			public PddlTypeMap<T> translate(SExpr expr) {
				return new PddlTypeMap<T>(expr, innerTranslator);
			}
		};
	}

}
