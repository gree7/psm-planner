package cz.agents.dimap.tools.dot;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cz.agents.dimap.tools.sas.MutexGroup;
import cz.agents.dimap.tools.sas.MutexGroup.Mutex;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.Operator.Effect;
import cz.agents.dimap.tools.sas.SasFile;
import cz.agents.dimap.tools.sas.SasGraph;
import cz.agents.dimap.tools.sas.Variable;
import cz.agents.dimap.tools.sas.Variable.Value;

/**
 * Created by pah on 22.7.15.
 */
public class DotCreater {


    /**
     * Generates dot file of given file with name/path as sasFileSource. Returns file where the dot is stored. DotFileName is name of file in which the dot will be written.
     *
     * SVG is generated if generate is set to true.
     *
     * @param sasFileSource
     * @param dotFileName
     * @param generate
     * @return
     * @throws IOException
     */
    public static File createDotFromFile(String sasFileSource, String dotFileName, boolean generate) throws IOException {
        SasFile sasFile = new SasFile(new File(sasFileSource));
        return createDotFromFile(sasFile, dotFileName, generate);
    }
    
    public static File createDotFromFile(SasFile sas, String dotFileName, boolean generate) throws IOException {
        return createDotAndStore(sas, dotFileName, generate);
    }


    /**
     * Generates dot file of given file which contains SAS. Returns file where the dot is stored. DotFileName is name of file in which the dot will be written.
     *
     * SVG is generated if generate is set to true.
     *
     * @param sas
     * @param dotFileName
     * @param generate
     * @return
     * @throws IOException
     */
    public static File createDotFromFile(File sas, String dotFileName, boolean generate) throws IOException {
        SasFile sasFile = new SasFile(sas);
        return createDotAndStore(sasFile, dotFileName, generate);
    }


    /**
     * Generates dot file of given sasFile. Returns file where the dot is stored. DotFileName is name of file in which the dot will be written.
     *
     * SVG is generated if generate is set to true.
     *
     * @param sas
     * @param dotFileName
     * @param generate
     * @return
     * @throws IOException
     */
    public static File createDotAndStore(SasFile sas, String dotFileName, boolean generate) throws IOException {
        if (dotFileName.length() > 200) {
            System.out.println("Shortened file name: " + dotFileName);
        }
        DotWriter dot = new DotWriter(dotFileName.substring(0, Math.min(dotFileName.length(), 200)));
        generateStateNodes(sas.getVariables(), dot);
        generateActionNodes(sas.getOperators(), dot);
        generateMutexes(sas.getMutexes(), dot);
        dot.writeNodeStyle(DotAttr.fillcolor("green"), DotAttr.style("filled"));
        addInit(sas.getVariables(), dot);
        addGoal(sas.getVariables(), dot);
        dot.flush();
        dot.close();
        if (generate) dot.generate();
        return new File(dotFileName);
    }

    private static void generateMutexes(List<MutexGroup> mutexes, DotWriter dot) throws IOException {
        for (MutexGroup mutexGroup : mutexes) {
            if (mutexGroup.isActive()) {
                for (Mutex mutexA : mutexGroup.getMutexes()) {
                    if (mutexA.isActive()) {
                        String labelA = createValueLabel(mutexA.getValue());
                        for (Mutex mutexB : mutexGroup.getMutexes()) {
                            if (mutexB.isActive()) {
                                String labelB = createValueLabel(mutexB.getValue());
                                if (labelA.compareTo(labelB) > 0 && !mutexA.getVariable().equals(mutexB.getVariable())) {
                                    dot.writeEdge(labelA, labelB, DotAttr.color("RED"), DotAttr.dir("none"));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void addGoal(Map<Integer, Variable> variables, DotWriter dot) throws IOException {
        String goal = "goal";
        dot.writeNode(goal, DotAttr.label("GOAL"));
        for (Variable variable : variables.values()) {
            if (variable.isActive() && variable.isInGoal()) {
                String source = "var" + variable.getRealId() + "_" + variable.getGoalValue().getUpMostParent().getRealID();
                dot.writeEdge(source, goal);
            }
        }
    }

    private static void addInit(Map<Integer, Variable> variables, DotWriter dot) throws IOException {
        String init = "init";
        dot.writeNode(init, DotAttr.label("INIT"));
        for (Variable variable : variables.values()) {
            if (variable.isActive()) {
                String target = "var" + variable.getRealId() + "_" + variable.getInitValue().getUpMostParent().getRealID();
                dot.writeEdge(init, target);
            }
        }
    }

    private static void generateActionNodes(Map<String, Operator> operators, DotWriter dot) throws IOException {
        long marker = 0;
        dot.writeNodeStyle(DotAttr.fillcolor("skyblue"), DotAttr.style("filled"));
        for (Operator operator : operators.values()) {
            if (operator.isActive()) {
                appendOperator(operator, marker, dot);
                marker++;
            }
        }
    }

    private static void appendOperator(Operator operator, long idx, DotWriter dot) throws IOException {
        String operatorNode = appendOperatorNode(operator, idx, dot);
        appendPreconditions(operator, operatorNode, dot);
        appendEffect(operator, operatorNode, dot);
    }

    private static void appendEffect(Operator operator, String operatorNode, DotWriter dot) throws IOException {
        dot.writeEdgeStyle(DotAttr.style("solid"));
        for (Operator.Effect effect : operator.getEffects()) {
            if (effect.isActive()) {
                if (effect.getOldValue().getUpMostParent().isAnyValue()) {
                    dot.writeEdgeStyle(DotAttr.style("dashed"));
                    for (Variable.Value value : effect.getVariable().getValues()) {
                        if (value.isActive()) {
                            String oldValueNode = createValueLabel(value);
                            dot.writeEdge(oldValueNode, operatorNode);
                        }
                    }
                    
                    dot.writeEdgeStyle(DotAttr.style("solid"));
                } else {
                    String oldValueNode = "var" + effect.getVariable().getRealId() + "_" + effect.getOldValue().getUpMostParent().getRealID();
                    dot.writeEdge(oldValueNode, operatorNode);
                }

                String newValueNode = "var" + effect.getVariable().getRealId() + "_" + effect.getNewValue().getUpMostParent().getRealID();
                dot.writeEdge(operatorNode, newValueNode);
            }
        }
    }

    private static String createValueLabel(Variable.Value value) {
        return "var" + value.getVariable().getRealId() + "_" + value.getUpMostParent().getRealID();
    }

    private static void appendPreconditions(Operator operator, String operatorNode, DotWriter dot) throws IOException {
        dot.writeEdgeStyle(DotAttr.style("bold"));
        for (Operator.Precondition precondition : operator.getPreconditions()) {
            if (precondition.isActive()) {
                String valueNode = "var" + precondition.getVariable().getRealId() + "_" + precondition.getValue().getUpMostParent().getRealID();
                dot.writeEdge(valueNode, operatorNode);
            }
        }
    }

    private static String appendOperatorNode(Operator operator, long idx, DotWriter dot) throws IOException {
        String operatorNode = "op_" + idx;
        dot.writeNode(operatorNode, DotAttr.label(operator.getName()));
        return operatorNode;
    }

    private static void generateStateNodes(Map<Integer, Variable> variables, DotWriter dot) throws IOException {
        dot.writeNodeStyle(DotAttr.shape("Mrecord"));
        for (Variable variable : variables.values()) {
            if (variable.isActive()) {
                generatedValues(variable, dot);
            }
        }
    }

    private static void generatedValues(Variable variable, DotWriter dot) throws IOException {
        String varName = "var" + variable.getRealId() + "_";
        for (Variable.Value value : variable.getValues()) {
            if (value.isActive()) {
                int valueId = value.getUpMostParent().getRealID();
                int variableId = value.getUpMostParent().getVariable().getRealId();
                String valueName = value.getValue();
                if (valueName.startsWith("NegatedAtom")) {
                    valueName = "!" + valueName.substring(11);
                } else if (valueName.startsWith("Atom")) {
                    valueName = valueName.substring(4);
                }
                dot.writeNode(varName + valueId, DotAttr.label(variableId + "|" + valueName));
            }
        }
    }

    public static void createDtgDots(SasFile sas, String outputPrefix, boolean generate) throws IOException {
        for (Variable variable : sas.variables.values()) {
            createDtgDot(sas.dtgs.get(variable), variable.getValues(), outputPrefix+"-var"+variable.getRealId(), generate);
        }
    }

    private static void createDtgDot(SasGraph sasGraph, List<Value> values, String outputFile, boolean generate) throws IOException {
        DotWriter dot = new DotWriter(outputFile);
        writeDtgDot(sasGraph, values, dot);
        dot.flush();
        dot.close();
        if (generate) dot.generate();
    }

    private static void writeDtgDot(SasGraph sasGraph, List<Value> values, DotWriter dot) throws IOException {
        String prefix = "val_"+values.get(0).getVariable().getRealId()+"_";
        for (Value value : values) {
            String label = value.getValue();
            if (label.equals("<none of those>")) {
                label = "--";
            }
            dot.writeNode(prefix+value.getRealID(), DotAttr.label(label));
        }
        for (Entry<Integer, List<Integer>> entry : sasGraph.graph.entrySet()) {
            String fromNode = prefix+entry.getKey();
            for (Integer toNodeInt : new HashSet<>(entry.getValue())) {
                String toNode = prefix+toNodeInt;
                dot.writeEdge(fromNode, toNode);
            }
        }
    }

    public static void createCgDot(SasFile sas, String outputFile, boolean generate) throws IOException {
        DotWriter dot = new DotWriter(outputFile);
        writeCgDot(sas, dot);
        dot.flush();
        dot.close();
        if (generate) dot.generate();
    }

    private static void writeCgDot(SasFile sas, DotWriter dot) throws IOException {
        dot.writeNodeStyle(DotAttr.shape("Mrecord"));
        for (Variable var : sas.variables.values()) {
            StringBuilder sb = null;
            for (Value value : var.getValues()) {
                if (sb == null) {
                    sb = new StringBuilder();
                    sb.append('{');
                } else {
                    sb.append('|');
                }
                if (value.getValue().equals("<none of those>")) {
                    sb.append("--");
                } else {
                    sb.append(value.getValue());
                }
            }
            sb.append('}');
            dot.writeNode("var_"+var.getRealId(), DotAttr.label(sb.toString()));
        }
        for (Entry<Integer, List<Integer>> entry : sas.causalGraph.graph.entrySet()) {
            String fromNode = "var_"+entry.getKey();
            for (Integer toNodeInt : entry.getValue()) {
                String toNode = "var_"+toNodeInt;
                dot.writeEdge(fromNode, toNode);
            }
        }
    }

    public static void createCgDtgDots(SasFile sas, String outputFile, boolean generate) throws IOException {
        DotWriter dot = new DotWriter(outputFile, DotAttr.attr("overlap", "false"), DotAttr.attr("sep", "+20;20"), DotAttr.attr("splines", "spline"), DotAttr.attr("concentrate", "true"));
//        DotWriter dot = new DotWriter(outputFile);
        writeCgDot(sas, dot);
        for (Variable variable : sas.variables.values()) {
            writeDtgDot(sas.dtgs.get(variable), variable.getValues(), dot);
        }
        dot.flush();
        dot.close();
        if (generate) dot.generate();
        
    }

    public static void createFreeDtgDot(SasFile sas, String outputFile, boolean generate) throws IOException {
        DotWriter dot = new DotWriter(outputFile, DotAttr.attr("overlap", "false"), DotAttr.attr("sep", "+20;20"), DotAttr.attr("splines", "spline"), DotAttr.attr("concentrate", "true"));
        HashSet<String> nodes = new HashSet<>();
        for (Entry<String, Operator> entry : sas.getOperators().entrySet()) {
            Operator operator = entry.getValue();
            if (operator.isActive() && operator.hasNoActivePreconditions() && operator.getNumberOfActiveEffects() == 1) {
                Effect effect = operator.getFirstActiveEffect();
                String fromNode = '\"'+effect.getOldValue().getUpMostParent().toString()+'\"';
                String toNode = '\"'+effect.getNewValue().getUpMostParent().toString()+'\"';
                if (!nodes.contains(fromNode)) {
                    dot.writeNode(fromNode);
                    nodes.add(fromNode);
                }
                if (!nodes.contains(toNode)) {
                    dot.writeNode(toNode);
                    nodes.add(toNode);
                }
                dot.writeEdge(fromNode, toNode, DotAttr.label(operator.getName()));
            }
        }
        dot.flush();
        dot.close();
        if (generate && !nodes.isEmpty()) dot.generate();

    }

}
