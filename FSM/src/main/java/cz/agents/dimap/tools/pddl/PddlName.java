package cz.agents.dimap.tools.pddl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.agents.dimap.Settings;
import cz.agents.dimap.tools.pddl.expr.SExpr;
import cz.agents.dimap.tools.pddl.expr.SExprTranslator;

/*
 * A PPDL identifier represents
 *   a predicate/fact symbol name like "at", or
 *   a type, or
 *   a constant, or
 *   a variable like "?var", or
 *   a PDDL keyword like ":strips"
 *   ... and possibly other PDDL atomic values
 */
public class PddlName implements Comparable<PddlName>, Serializable {

    private static final long serialVersionUID = 8590789958890703999L;

    public static final PddlName REQUIREMENT_STRIPS = new PddlName(":strips");
	public static final PddlName REQUIREMENT_ACTION_COSTS = new PddlName(":action-costs");
	public static final PddlName REQUIREMENT_FLUENTS = new PddlName(":fluents");

	public static final PddlName COST_FUNCTION = new PddlName("total-cost");

    static final Set<String> KEY_WORDS = new HashSet<>(Arrays.asList(":action-costs", ":shared-predicates", ":shared-data", "total-cost"));

    public static final Map<String, String> translationTable = new HashMap<>();
    
    public final String name;

    public PddlName(String name) {
        this.name = name.toLowerCase();
    }
    
    public static SExprTranslator<PddlName> TRANSLATOR = new SExprTranslator<PddlName>() {
		@Override
		public PddlName translate(SExpr expr) {
			return createFixedName(expr.getValue());
		}
	};

    public static PddlName createFixedName(String name) {
        return new PddlName(fixName(name));
    }

    public static String fixName(String name) {
        String newName = name.toLowerCase();
        if (KEY_WORDS.contains(newName)) {
            return newName; 
        } else if (newName.contains("-")) {
        	if (Settings.DEBUG) { System.out.println("PddlName: Replacing '-' by '_' in name: " + newName); }
            newName = newName.replace('-', '_');
            if (Settings.IS_CODMAP) {
                translationTable.put(newName, name);
            }
            return newName;
        } else {
            return newName;
        }
    }

    public boolean isVariable() {
        return name.charAt(0) == '?';
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof PddlName)) {
            return false;
        }
        else if (this == other) {
            return true;
        }
        else {
            return this.name.equals(((PddlName)other).name);
        }
    }

    @Override
    public int compareTo(PddlName other) {
        return this.name.compareTo(other.name);
    }
}
