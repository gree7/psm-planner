package cz.agents.dimap.psm.state;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import cz.agents.dimap.tools.pddl.PddlCondition;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.PddlProblem;

@RunWith(Theories.class)
public class BitSetStateFactoryPddlSharedPredicateNamesTest {

    private static String domainString =
                    ";; logistics domain\n" + 
                    ";;\n" + 
                    ";; logistics-typed-length: strips + simple types\n" + 
                    ";;    based on logistics-strips-length.\n" + 
                    ";; Tue Dec  1 16:10:25 EST 1998 Henry Kautz\n" + 
                    "\n" + 
                    "(define (domain logistics)\n" + 
                    "  (:requirements :strips :typing)\n" + 
                    "  (:types\n" + 
                    "      package location vehicle city - object\n" + 
                    "      truck airplane - vehicle\n" + 
                    "      airport - location)\n" + 
                    "\n" + 
                    "  (:predicates\n" + 
                    "        (at ?vehicle - vehicle  ?location - location)\n" + 
                    "        (pub_att ?package - package  ?location - location)\n" + 
                    "        (in ?package - package ?vehicle - vehicle)\n" + 
                    "        (in-city ?loc-or-truck - (either location truck) ?citys - city))\n" +
                    "  (:shared-predicate-names pub_att)\n" + 
                    "\n" + 
                    "  (:action load-truck\n" + 
                    "    :parameters\n" + 
                    "         (?obj - package\n" + 
                    "          ?truck - truck\n" + 
                    "          ?loc - location)\n" + 
                    "    :precondition\n" + 
                    "        (and    (at ?truck ?loc)\n" + 
                    "            (pub_att ?obj ?loc))\n" + 
                    "    :effect\n" + 
                    "        (and    (not (pub_att ?obj ?loc))\n" + 
                    "            (in ?obj ?truck)))\n" + 
                    "\n" + 
                    "  (:action load-airplane\n" + 
                    "    :parameters\n" + 
                    "        (?obj - package\n" + 
                    "         ?airplane - airplane\n" + 
                    "         ?loc - airport)\n" + 
                    "    :precondition\n" + 
                    "        (and\n" + 
                    "            (pub_att ?obj ?loc)\n" + 
                    "            (at ?airplane ?loc))\n" + 
                    "    :effect\n" + 
                    "           (and     (not (pub_att ?obj ?loc))\n" + 
                    "            (in ?obj ?airplane)))\n" + 
                    "\n" + 
                    "  (:action unload-truck\n" + 
                    "    :parameters\n" + 
                    "        (?obj - package\n" + 
                    "         ?truck - truck\n" + 
                    "         ?loc - location)\n" + 
                    "    :precondition\n" + 
                    "        (and    (at ?truck ?loc)\n" + 
                    "            (in ?obj ?truck))\n" + 
                    "    :effect\n" + 
                    "        (and    (not (in ?obj ?truck))\n" + 
                    "            (pub_att ?obj ?loc)))\n" + 
                    "\n" + 
                    "  (:action unload-airplane\n" + 
                    "    :parameters\n" + 
                    "        (?obj - package\n" + 
                    "         ?airplane - airplane\n" + 
                    "         ?loc - airport)\n" + 
                    "    :precondition\n" + 
                    "        (and    (in ?obj ?airplane)\n" + 
                    "            (at ?airplane ?loc))\n" + 
                    "    :effect\n" + 
                    "        (and\n" + 
                    "            (not (in ?obj ?airplane))\n" + 
                    "            (pub_att ?obj ?loc)))\n" + 
                    "\n" + 
                    "  (:action drive-truck\n" + 
                    "    :parameters\n" + 
                    "        (?truck - truck\n" + 
                    "         ?loc-from - location\n" + 
                    "         ?loc-to - location\n" + 
                    "         ?city - city)\n" + 
                    "    :precondition\n" + 
                    "        (and    (at ?truck ?loc-from)\n" + 
                    "            (in-city ?truck ?city)\n" + 
                    "            (in-city ?loc-from ?city)\n" + 
                    "            (in-city ?loc-to ?city))\n" + 
                    "    :effect\n" + 
                    "        (and    (not (at ?truck ?loc-from))\n" + 
                    "            (at ?truck ?loc-to)))\n" + 
                    "\n" + 
                    "  (:action fly-airplane\n" + 
                    "    :parameters\n" + 
                    "        (?airplane - airplane\n" + 
                    "         ?loc-from - airport\n" + 
                    "         ?loc-to - airport)\n" + 
                    "    :precondition\n" + 
                    "        (at ?airplane ?loc-from)\n" + 
                    "    :effect\n" + 
                    "        (and    (not (at ?airplane ?loc-from))\n" + 
                    "        (at ?airplane ?loc-to)))\n" + 
                    ")\n";
    
    private static String problemString= 
                "(define (problem logistics-a3)\n" + 
                "    (:domain logistics)\n" + 
                "    (:requirements :strips :typing)\n" + 
                "    (:objects\n" + 
                "        package1 - PACKAGE\n" + 
                "\n" + 
                "        airplane1 - AIRPLANE\n" + 
                "\n" + 
                "        pgh - CITY\n" + 
                "        ny - CITY\n" + 
                "\n" + 
                "        pgh-truck - TRUCK\n" + 
                "        ny-truck - TRUCK\n" + 
                "\n" + 
                "        pgh-po - LOCATION\n" + 
                "        ny-po - LOCATION\n" + 
                "\n" + 
                "        pgh-airport - AIRPORT\n" + 
                "        ny-airport - AIRPORT\n" + 
                "    )\n" + 
                "    (:init\n" + 
                "        (in-city pgh-po pgh)\n" + 
                "        (in-city pgh-airport pgh)\n" + 
                "\n" + 
                "        (in-city ny-po ny)\n" + 
                "        (in-city ny-airport ny)\n" + 
                "\n" + 
                "        (pub_att package1 pgh-po)\n" + 
                "\n" + 
                "        (at airplane1 pgh-airport)\n" + 
                "\n" + 
                "        (at pgh-truck pgh-po)\n" + 
                "        (in-city pgh-truck pgh)\n" + 
                "        (at ny-truck ny-po)\n" + 
                "        (in-city ny-truck ny)\n" + 
                "    )\n" + 
                "    (:goal (and\n" + 
                "        (pub_att package1 ny-po)\n" + 
                "    ))\n" + 
                ")\n";

    
    @DataPoints
    public static String[] predicates = new String[] {
        "in_city pgh_po pgh",
        "in_city pgh_po ny",
        "in_city pgh_airport pgh",
        "pub_att package1 ny_po",
        "at ny_truck pgh_po"
    };

    @Theory
    public void testToStringSimple(String predicate) throws IOException {
        String result = getToString(predicate);
        assertEquals("{"+predicate+"}", result);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testToStringSimpleWrong() throws IOException {
        getToString("in-city pgh-po pgh");
    }

    private String getToString(String predicate) throws IOException {
        PddlDomain domain = new PddlDomain( new StringReader( domainString ) );
        PddlProblem problem = new PddlProblem( domain, new StringReader( problemString ) );

        BitSetStateFactoryPddl bssFactory = new BitSetStateFactoryPddl(problem);
        
        String[] splitPredicate = predicate.split(" ");
        
        List<PddlName> args = new ArrayList<>(splitPredicate.length);
        for (int i = 1; i < splitPredicate.length; i++) {
            args.add(new PddlName(splitPredicate[i]));
        }
        
        PddlCondition condition = new PddlCondition(
                Arrays.asList(
                        new PddlTerm(
                                new PddlName(splitPredicate[0]),
                                args)),
                Collections.<PddlTerm>emptyList());
                                        
        BitSetState init = bssFactory.createInitState(condition);
        return bssFactory.toString(init);
    }

    @Test
    public void testToString() throws IOException {

        PddlDomain domain = new PddlDomain( new StringReader( domainString ) );
        PddlProblem problem = new PddlProblem( domain, new StringReader( problemString ) );

        BitSetStateFactoryPddl bssFactory = new BitSetStateFactoryPddl(problem);
        
        BitSetState init = bssFactory.createInitState(problem.init);
        assertEquals(10, bssFactory.toString(init).split("\\|").length);
        BitSetState projectedInit = bssFactory.projectToPublic(init);
        assertEquals("{pub_att package1 pgh_po}", bssFactory.toString(projectedInit));
    }

    @Test
    public void testProjection() throws IOException {

        PddlDomain domain = new PddlDomain( new StringReader( domainString ) );
        PddlProblem problem = new PddlProblem( domain, new StringReader( problemString ) );

        BitSetStateFactoryPddl bssFactory = new BitSetStateFactoryPddl(problem);
        
        BitSetState init = bssFactory.createInitState(problem.init);
        BitSetState projectedInit = bssFactory.projectToPublic(init);
        assertEquals(1, projectedInit.getBitSet().cardinality());
    }
}
