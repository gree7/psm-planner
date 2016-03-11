package cz.agents.dimap.pddl;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import cz.agents.dimap.tools.pddl.PddlDomain;

public class SharedPredicatesParseTest {

    private static String domainString01 = "(define (domain beerproblem-grounded)\n" + 
    		"    (:requirements :strips :typing)\n" + 
    		"    \n" + 
    		"    (:types\n" + 
    		"        plane truck prague brno ostrava - object\n" + 
    		"    )\n" + 
    		"\n" + 
    		"    (:predicates\n" + 
    		"        (at \n" + 
    		"            ?veh  - (either truck plane)   \n" + 
    		"            ?city - (either prague brno ostrava) \n" + 
    		"        )\n" + 
    		"        (beer_in \n" + 
    		"            ?inn  - (either prague brno ostrava plane truck)\n" + 
    		"        )\n" + 
    		"    )\n" + 
    		"\n" +
    		"    (:shared-predicates " +
    		"        (at truck ?city)" +
    		"        (beer_in prague)" +
    		"        (beer_in ostrava)" +
    		"    )"+
    		"\n" + 
    		"    (:action fly-prague-brno\n" + 
    		"        :parameters \n" + 
    		"        (\n" + 
    		"            ?plane - plane\n" + 
    		"            ?prague - prague\n" + 
    		"            ?brno - brno)\n" + 
    		"        :precondition \n" + 
    		"        (and \n" + 
    		"            (at ?plane ?prague)\n" + 
    		"        )\n" + 
    		"        :effect \n" + 
    		"        (and \n" + 
    		"            (at ?plane ?brno)\n" + 
    		"            (not (at ?plane ?prague))\n" + 
    		"        )\n" + 
    		"    )\n" + 
    		"\n" + 
    		")";

	
	@Test
	public void test() throws IOException {
		PddlDomain domain = new PddlDomain(new StringReader(domainString01));
		System.out.println(domain.sharedPredicates);
	}

}
