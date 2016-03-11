package experiments.psm.file;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Scanner;

import cz.agents.dimap.psm.planner.PddlMultiAgentProblem;
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
        
        Collection<String> agentSuffixes = findAgentSuffixes(problemDir);
        
        for (String agentSuffix : agentSuffixes) {
            // Creates an instance of the java pddl compiler
            File domainFile = new File(problemDir, "domain-" + agentSuffix);
            domainFile = fixPrivateAndFunctions(domainFile);
            File problemFile = new File(problemDir, "problem-" + agentSuffix);
            problemFile = fixPrivateAndFunctions(problemFile);
            
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
                PddlDomain domain = new PddlDomain(new FileReader(domainFile));
                
                PddlProblem problem = new PddlProblem(domain, new FileReader(problemFile));
                agentSuffix = agentSuffix.replace('-', '_');
                agentSuffix = agentSuffix.replace(".pddl", "");
                problems.put(agentSuffix, problem);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }

        }
        return problems;
    }

    public static File fixPrivateAndFunctions(File file) {
        try {
            File retFile = File.createTempFile("tmpFile-"+file.getName(), "");
            Writer writer = new FileWriter(retFile);
            Scanner sc = new Scanner(file);
            boolean counting = false;
            int counter = 0;
            boolean warningWritten = false;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.contains("(=") || line.contains("total-cost")) {
                    if (!warningWritten) {
                        System.err.println("Skipping functions...");
                    }
                    warningWritten = true;
                    continue;
                }
                if (counting) {
                    counter += countBraces(line);
                    if (counter < 0) {
                        counting = false;
                        continue;
                    }
                }
                if (line.contains(":private")) {
                    writer.append("  )\n");
                    counting = true;
                    counter = 0;
                }
                writer.append(line+"\n");
            }
            writer.close();
            sc.close();
            return retFile;

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    private static int countBraces(String line) {
        int counter = 0;
        for (int i=0; i<line.length(); i++) {
            switch (line.charAt(i)) {
            case ')':
                counter--;
                break;
            case '(':
                counter++;
                break;

            default:
                break;
            }
        }
        return counter;
    }

    @Override
    public String getImageOutputPath() {
        return new File(problemDir, "fsm-solution-PUB").getPath();
    }

    private static Collection<String> findAgentSuffixes(File problemDir) {
        Collection<String> agentSuffixes = new LinkedHashSet<>();
        
        for (File file : problemDir.listFiles()) {
            if (file.getName().startsWith("problem-")) {
                String agentName = file.getName().substring(8, file.getName().length());
                agentSuffixes.add(agentName);
            }
        }
        return agentSuffixes;
    }
}
