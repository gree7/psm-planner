package cz.agents.dimap.tools;

import java.io.File;

/**
 * Wrapper for an instance consisting of domain and problem.
 */
public class Problem {
    public final File domain;
    public final File problem;
    public final String domainName;

    public Problem(File domain, File problem) {
        this.domain = domain;
        this.problem = problem;

        // ok, technically it is not from domain file
        this.domainName = problem.getName().replaceFirst("[.][^.]+$", ""); // remove (last) extension ".ext"
    }
}
