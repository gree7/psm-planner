package cz.agents.dimap.psm.planner;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import cz.agents.dimap.tools.dot.DotFormat;
import cz.agents.dimap.tools.pddl.PddlDomain;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class FileMultiAgentProblem extends PddlMultiAgentProblem {
    private final File problemDir;

    public FileMultiAgentProblem(File problemDir) {
        super(createProblems(problemDir));
        this.problemDir = problemDir;
    }

    private static Map<String, PddlProblem> createProblems(File problemDir) {
        Map<String, PddlProblem> problems = new HashMap<>();
        
        Collection<String> agentNames = findAgentNames(problemDir);
        
        for (String agentName : agentNames) {
            // Creates an instance of the java pddl compiler
            File domainFile = new File(problemDir, agentName+"-domain.pddl");
            File problemFile = new File(problemDir, agentName+"-problem.pddl");
            
            // Creates an instance of the java pddl compiler
            if (!domainFile.exists()) {
                System.out.println("domain file " + domainFile + " does not exist");
                System.exit(-1);
            }
            if (!problemFile.exists()) {
                System.out.println("problem file " + problemFile + " does not exist");
                System.exit(-1);
            }
            
            try {
                PddlDomain domain= new PddlDomain(new FileReader(domainFile));
                PddlProblem problem = new PddlProblem(domain, new FileReader(problemFile));
                problems.put(agentName, problem);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }

        }
        return problems;
    }

    @Override
    public String getImageOutputPath() {
        return new File(problemDir, "fsm-solution-PUB").getPath();
    }

    private static Collection<String> findAgentNames(File problemDir) {
        Collection<String> agentNames = new LinkedHashSet<>();
        System.out.println("Detecting agents in problem "+problemDir.getName()+":");
        
        DotFormat.deconflictionDomain3x3 = problemDir.getName().contains("deconfliction");
        DotFormat.elevatorsDomain = problemDir.getName().contains("elevator");
        
        for (File file : problemDir.listFiles()) {
            if (file.getName().endsWith("-problem.pddl")) {
                String agentName = file.getName().substring(0, file.getName().length() - 13);
                agentNames.add(agentName);
                System.out.println("--> found agent: "+agentName);
            }
        }
        return agentNames;
    }
}
