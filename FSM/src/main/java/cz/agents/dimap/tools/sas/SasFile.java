package cz.agents.dimap.tools.sas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;

public class SasFile {
    
    public Map<String, List<String>> variables = new HashMap<>(); 
    public List<String> operatorNames = new ArrayList<>();
    public Map<String, List<String>> operatorPre = new HashMap<>();
    public Map<String, List<String>> operatorEff = new HashMap<>();
    public List<String> init;
    public List<String> goal;

    public File file;

    public SasFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        skipBlock(br, "version");
        skipBlock(br, "metric");
        
        readVariables(br);
        
        skipMutexes(br);
        readInit(br);
        readGoal(br);
        
        readOperators(br);
        
        br.close();

    	this.file = file;
    }
    
	public String getContent() throws IOException {
    	return new String(Files.readAllBytes(file.toPath()));
    }

    private void skipMutexes(BufferedReader br) throws IOException {
        int num = Integer.parseInt( br.readLine() );
        for (int i=0; i<num; i++) {
            skipBlock(br, "mutex_group");
        }
    }

    private void readVariables(BufferedReader br) throws IOException {
        int num = Integer.parseInt( br.readLine() );
        for (int i=0; i<num; i++) {
            assertBeginToken(br, "variable");
            String varName = br.readLine();
            br.readLine();
            int varValNum = Integer.parseInt( br.readLine() );
            List<String> values = new ArrayList<>();
            for (int j=0; j<varValNum; j++) {
                String line = br.readLine();
                line = line.substring(line.indexOf(' ')+1);
                values.add(line);
            }
            assertEndToken(br, "variable");
            variables.put(varName,values);
        }
    }

    private void readOperators(BufferedReader br) throws IOException {
        int num = Integer.parseInt( br.readLine() );
        for (int i=0; i<num; i++) {
            assertBeginToken(br, "operator");
            String opName = br.readLine();
            operatorNames.add(opName);
            operatorPre.put(opName, readBlock(br));
            operatorEff.put(opName, readBlock(br));
            br.readLine();
            assertEndToken(br, "operator");
        }
    }
    
    public static List<String> readBlock(BufferedReader br) throws IOException {
        int count = Integer.parseInt( br.readLine() );
        List<String> list = new LinkedList<>();
        for (int j=0; j<count; j++) {
        	list.add(br.readLine());
        }
        return list;
    }

    private void skipBlock(BufferedReader br, String blockName) throws IOException {
        assertBeginToken(br, blockName);
        for (String line = br.readLine(); !line.equals("end_"+blockName); line = br.readLine()) {
        }
    }

    private void readInit(BufferedReader br) throws IOException {
        assertBeginToken(br, "state");
        init = new LinkedList<>();
        for (String line = br.readLine(); !line.equals("end_state"); ) {
        	init.add(line);
        	line = br.readLine();
        }
    }
    
    private void readGoal(BufferedReader br) throws IOException {
        assertBeginToken(br, "goal");
		goal = readBlock(br);
        assertEndToken(br, "goal");
	}

    
    private void assertBeginToken(BufferedReader br, String blockName) throws IOException {
        assertToken(br, "begin_"+blockName);
    }

    private void assertEndToken(BufferedReader br, String blockName) throws IOException {
        assertToken(br, "end_"+blockName);
    }

    private void assertToken(BufferedReader br, String blockName) throws IOException {
        String line = br.readLine();
        if (!line.equals(blockName)) {
            throw new IllegalArgumentException("Wrong block: " + line + " (expected: " + blockName + ")");
        }
    }

    @Override
    public String toString() {
        return "SasFile [variables=" + variables + ", operators=" + operatorNames + "]";
    }

    public Stats getStats(PddlProblem problem) {
        return new Stats(variables.size(), countFacts(), countPubFacts(problem), countOperators(), countPubOperators());
    }

    private int countPubFacts(PddlProblem problem) {
        int result = 0;
        for (List<String> facts : variables.values()) {
            for (String fact : facts) {
                if (!fact.startsWith("virtualgoal_")
                        && !fact.startsWith("planningphase")
                        && fact.indexOf('(') != -1) {
                    PddlTerm term = PddlTerm.parse(fact);
                    if (problem.isPublicPredicate(term)) {
                        result++;
                    } 
                }
            }
        }
        return result;
    }

    private int countFacts() {
        int result = 0;
        for (List<String> facts : variables.values()) {
            for (String fact : facts) {
                if (!fact.startsWith("virtualgoal_") && !fact.startsWith("planningphase") ) {
                    result++;
                }
            }
        }
        return result;
    }
    
    private int countOperators() {
        int result = 0;
        for (String op : operatorNames) {
            if (!op.startsWith("pub_goalaction_")) {
                result++;
            }
        }
        return result;
    }

    private int countPubOperators() {
        int result = 0;
        for (String op : operatorNames) {
            if (op.startsWith("pub_") && !op.startsWith("pub_goalaction_")) {
                result++;
            }
        }
        return result;
    }

    public static class Stats {
        public int pubOps;
        public int variables;
        public int facts;
        public int pubFacts;
        public int ops;

        public Stats(int variables, int facts, int pubFacts, int ops, int pubOps) {
            this.variables = variables;
            this.facts = facts;
            this.pubFacts = pubFacts;
            this.ops = ops;
            this.pubOps = pubOps;
        }

        @Override
        public String toString() {
            String ret = "";
            ret += "vars: ";
            ret += variables;
            ret += ", facts: ";
            ret += facts;
            ret += " (pub: ";
            ret += pubFacts;
            ret += " -> ";
            ret += getPubFactPct();
            ret += "%)";
            ret += ", ops:";
            ret += ops;
            ret += " (pub: ";
            ret += pubOps;
            ret += " -> ";
            ret += getPubOpsPct();
            ret += "%)";
            return ret;
        }

        public int getPubOpsPct() {
            return (pubOps*100) / ops;
        }

        public int getPubFactPct() {
            return (pubFacts*100) / facts;
        }
    }
    
    public static void main(String[] args) throws IOException {
    	SasFile sas = new SasFile(new File("/home/yan/projects/uni/planning/eplanner/output.sas"));
		System.out.println(sas.operatorEff);
		System.out.println(sas.init);
		System.out.println(sas.goal);
	}
    
}
