package cz.agents.dimap.tools.sas;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

import cz.agents.dimap.tools.sas.Variable.Value;


public class SasParser {

    public static void expectToken(BufferedReader br, String token) throws IOException {
        String line = br.readLine();
        if (line == null) {
            throw new IOException("ERROR parsing SAS file: unexpected EOF");
        }
        if (!line.trim().equals(token)) {
            throw new IOException("ERROR parsing SAS file: expected " + token + " but have " + line);
        }
    }

    public static void writeToken(BufferedWriter writer, String token) throws IOException {
        writer.write(token);
        writer.write('\n');
    }

    public static List<String> readPlan(BufferedReader br) throws IOException {
        expectToken(br, "begin_plan");
        int actionCount = Integer.parseInt(br.readLine());
        List<String> plan = new ArrayList<String>(actionCount);
        for (int i = 0; i < actionCount; i++) {
            String line = br.readLine();
            if (line.startsWith("pub_")
                    || line.startsWith("int_")
                    || line.startsWith("ext_")) {
                line = line.substring(4);
            }
            plan.add(line);
        }
        expectToken(br, "end_plan");

        return plan;
    }

    public static void skipState(BufferedReader br) throws IOException {
        expectToken(br, "begin_state");
        while (true) {
            String line = br.readLine();
            if (line == null) {
                throw new IOException("ERROR parsing SAS file: unexpected EOF");
            }
            if (line.trim().equals("end_state")) {
                return;
            }
        }
    }

    public static void copyPlan(BufferedReader reader, BufferedWriter writer) throws IOException {
        expectToken(reader, "begin_plan");
        writeToken(writer, "begin_plan");

        int actionCount = Integer.parseInt(reader.readLine());
        writer.write(actionCount + "\n");
        for (int i = 0; i < actionCount; i++) {
            writer.write(reader.readLine() + "\n");
        }

        expectToken(reader, "end_plan");
        writeToken(writer, "end_plan");
    }

    public static void stripPlanAfterActionIndex(BufferedReader reader, BufferedWriter writer, int actionIndex) throws IOException {
        expectToken(reader, "begin_plan");
        writeToken(writer, "begin_plan");

        int actionCount = Integer.parseInt(reader.readLine());
        writeToken(writer, Integer.toString(actionIndex + 2));
        int index;
        for (index = 0; index <= actionIndex; index++) {
            writer.write(reader.readLine() + "\n");
        }
        writeToken(writer, "*");
        for (; index < actionCount; index++) {
            reader.readLine();
        }

        expectToken(reader, "end_plan");
        writeToken(writer, "end_plan");
    }

    public static int readAxioms(BufferedReader br) throws IOException {
        int numberOfAxioms = Integer.parseInt(br.readLine());
        if (0 != numberOfAxioms) {
            throw new IllegalStateException("AXIOM part is not taken into account, so do not use it.");
        }
        return numberOfAxioms;
    }

    public static int readVersion(BufferedReader br) throws IOException {
        assertBeginToken(br, "version");
        int versionNum = Integer.parseInt(br.readLine());
        assertEndToken(br, "version");
        return versionNum;
    }

    public static int readMetric(BufferedReader br) throws IOException {
        assertBeginToken(br, "metric");
        int metric = Integer.parseInt(br.readLine());
        assertEndToken(br, "metric");
        return metric;
    }

    public static Map<Integer, Variable> readVariables(BufferedReader br) throws IOException {
        Map<Integer, Variable> variables = new LinkedHashMap<>();
        int originalNumberOfVariables = Integer.parseInt(br.readLine());
        for (int i = 0; i < originalNumberOfVariables; i++) {
            assertBeginToken(br, "variable");
            String varName = br.readLine();
            int varNum = i;//varName.substring(3); // "var"
            String layer = br.readLine();
            int varValNum = Integer.parseInt(br.readLine());
            Variable variable = new Variable(varNum, Integer.parseInt((layer)));
            for (int j = 0; j < varValNum; j++) {
                String line = br.readLine();
                variable.addNextValue(line);
            }
            assertEndToken(br, "variable");
            variables.put(variable.getOriginalId(), variable);
        }
        return variables;
    }

    public static List<MutexGroup> readMutexes(BufferedReader br, Map<Integer, Variable> variables) throws IOException {
        List<MutexGroup> mutexes = new ArrayList<>();
        int num = Integer.parseInt(br.readLine());
        for (int i = 0; i < num; i++) {
            assertBeginToken(br, "mutex_group");
            mutexes.add(new MutexGroup(br, variables));
            assertEndToken(br, "mutex_group");
        }
        return mutexes;
    }

    public static void readInit(BufferedReader br, Map<Integer, Variable> variables) throws IOException {
        assertBeginToken(br, "state");
        int timeMark = 0;
        for (String line = br.readLine(); !line.equals("end_state"); timeMark++) {
            variables.get(timeMark).setInitValue(Integer.parseInt(line));
            line = br.readLine();
        }
    }

    public static void readGoal(BufferedReader br, Map<Integer, Variable> variables) throws IOException {
        assertBeginToken(br, "goal");
        String line = br.readLine();
        int num = Integer.parseInt(line);
        for (int i = 0; i < num; i++) {
            line = br.readLine();
            String[] splitted = line.split(" ");
            if (2 != splitted.length) {
                throw new IllegalStateException("");
            }
            int key = Integer.parseInt(splitted[0]);
            int value = Integer.parseInt(splitted[1]);
            variables.get(key).setGoalValue(value);
        }
        assertEndToken(br, "goal");
    }

    public static Map<String,Operator> readOperators(BufferedReader br, Map<Integer, Variable> variables, SasFile sasFile) throws IOException {
        Map<String,Operator> operators = new LinkedHashMap<>();
        int num = Integer.parseInt(br.readLine());
        for (int i = 0; i < num; i++) {
            assertBeginToken(br, "operator");
            String opName = br.readLine().trim();
            Operator operator = new Operator(opName, br, variables);

            // TODO tady vyhodit jeste operatory ktery tam jsou od zacatku redundantni

            operators.put(opName, operator);
            sasFile.cacheOperator(operator);
            sasFile.storeOperator(operator);
            assertEndToken(br, "operator");
        }
        return operators;
    }

    public static void assertBeginToken(BufferedReader br, String blockName) throws IOException {
        assertToken(br, "begin_" + blockName);
    }

    public static void assertEndToken(BufferedReader br, String blockName) throws IOException {
        assertToken(br, "end_" + blockName);
    }

    public static void assertToken(BufferedReader br, String blockName) throws IOException {
        String line = br.readLine();
        if (line == null) {
            throw new IOException("End of file!");
        }
        if (!line.equals(blockName)) {
            throw new IllegalArgumentException("Wrong block: " + line + " (expected: " + blockName + ")");
        }
    }

    public static Map<Variable, SasGraph> readDtgs(BufferedReader br, Map<Integer, Variable> variables) {
        try {
            Map<Variable, SasGraph> dtgs = new LinkedHashMap<Variable, SasGraph>();
            for (int i=0; i<variables.size(); i++) {
                Variable variable = variables.get(i);
                SasGraph dtg = readDtg(br, variable.getValues().size());
                dtgs.put(variable, dtg);
            }
            return dtgs;
        } catch (IOException e) {
            return null;
        }
    }

    private static SasGraph readDtg(BufferedReader br, int numOfNodes) throws IOException {
        assertBeginToken(br, "DTG");
        SasGraph graph = new SasGraph();
        for (int i=0; i<numOfNodes; i++) {
            List<Integer> toNodes = readDtgToNodes(br);
            graph.add(i, toNodes);
        }
        assertEndToken(br, "DTG");
        return graph;
    }

    private static List<Integer> readDtgToNodes(BufferedReader br) throws IOException {
        List<Integer> toNodes = new ArrayList<>();
        int num = Integer.parseInt(br.readLine());
        for (int i = 0; i < num; i++) {
            int toNode = Integer.parseInt(br.readLine().split(" ")[0]);
            toNodes.add(toNode);
            
            // skip other information
            br.readLine();
            int skipNum = Integer.parseInt(br.readLine());
            for (int j = 0; j < skipNum; j++) {
                br.readLine();
            }
        }
        return toNodes;
    }

    public static SasGraph readCg(BufferedReader br, int numOfNodes) throws IOException {
        try {
            assertBeginToken(br, "CG");
            SasGraph graph = new SasGraph();
            for (int i=0; i<numOfNodes; i++) {
                List<Integer> toNodes = readCgToNodes(br);
                graph.add(i, toNodes);
            }
            assertEndToken(br, "CG");
            return graph;
        } catch (IOException e) {
            return null;
        }
    }

    private static List<Integer> readCgToNodes(BufferedReader br) throws IOException {
        List<Integer> toNodes = new ArrayList<>();
        int num = Integer.parseInt(br.readLine());
        for (int i = 0; i < num; i++) {
            int toNode = Integer.parseInt(br.readLine().split(" ")[0]);
            toNodes.add(toNode);
        }
        return toNodes;
    }

    public static void skipSg(BufferedReader br) throws IOException {
        try {
            expectToken(br, "begin_SG");
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    throw new IOException("ERROR parsing SAS file: unexpected EOF");
                }
                if (line.trim().equals("end_SG")) {
                    return;
                }
            }
        } catch (IOException e) {
            return;
        }
    }

}
