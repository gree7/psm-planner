package cz.agents.dimap.data;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Assert;

import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class PddlTestProblem {

    public static PddlProblem createProblem(String domainString, String problemString) {
        return createProblem(domainString, problemString, true);
    }
    
    public static PddlProblem createProblem(String domainString, String problemString, boolean setActionPublicness) {
        try {
            PddlDomain domain = new PddlDomain(new StringReader(domainString));
            PddlProblem problem = new PddlProblem(domain, new StringReader(problemString));
            if (setActionPublicness) {
                problem.setActionAccessTypes();
            }
            return problem;
        } catch (IOException e) {
            Assert.fail(e.getMessage());
            return null;
        }
    }

    public PddlTestProblem() {
        super();
    }

}