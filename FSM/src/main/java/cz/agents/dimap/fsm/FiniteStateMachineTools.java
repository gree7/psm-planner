package cz.agents.dimap.fsm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.dimap.Settings;
import cz.agents.dimap.tools.dot.DotFormat;

public class FiniteStateMachineTools {

    public static <S, O> void imgOutput(FiniteStateMachine<S, O> fsm, String fileName, LabelFactory<S, O> labelFactory) {
        Set<S> emptyGoals = Collections.emptySet();
        imgOutput(fsm, fileName, labelFactory, emptyGoals);
    }
    
    public static <S, O> void imgOutput(FiniteStateMachine<S, O> fsm, String fileName, LabelFactory<S, O> labelFactory, final Collection<S> goals) {
        imgOutput(fsm, fileName, labelFactory, new MultiStateGoalCondition<>(goals));
    }

    public static <S, O> void imgOutput(FiniteStateMachine<S, O> fsm, String fileName, LabelFactory<S, O> labelFactory, GoalCondition<S> goalCondition) {
        File file = new File(fileName+".dot");
        try {
            FileOutputStream fop = new FileOutputStream(file);
        
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
    
            writeToFile(fop, DotFormat.formatHeader());
            
            Map<S, String> varNames = new HashMap<>();
            int varNum = 0; 
            for (S state : fsm.getStates()) {
                String varName = "s" + varNum++;
                varNames.put(state, varName);
                String stateLabel;
                if (labelFactory != null) {
                    stateLabel = labelFactory.createStateLabel(state);
                } else {
                    if (state == null) {
                        stateLabel = "!!! null\" fontcolor =\"red";
                    } else {
                        stateLabel = state.toString();
                    }
                }
                if ( state == fsm.getInitState() ) {
                    if (Settings.DOT_BLACKWHITE) {
                        writeToFile(fop, DotFormat.formatNode(varName, stateLabel, "black") );
                    } else {
                        writeToFile(fop, DotFormat.formatNode(varName, stateLabel, "blue") );
                    }
                } 
                else if ( goalCondition != null && goalCondition.isGoal(state)) {
                    if (Settings.DOT_BLACKWHITE) {
                        writeToFile(fop, DotFormat.formatNode(varName, stateLabel, "lightgray") );
                    } else {
                        writeToFile(fop, DotFormat.formatNode(varName, stateLabel, "red") );
                    }
                } 
                else {
                    writeToFile(fop, DotFormat.formatNode(varName, stateLabel) );
                }
                
            }
    
            for (Entry<S, Collection<Transition<S, O>>> entry : fsm.getTransitions().entrySet()) {
                for (Transition<S, O> transition : entry.getValue()) {
                    String opName;
                    if (labelFactory != null) {
                        opName = labelFactory.createOperatorLabel(transition.getOperator());
                    } else {
                        opName = transition.getOperator().toString();
                    }
                    writeToFile(fop, DotFormat.formatEdge(varNames.get(entry.getKey()), varNames.get(transition.getToState()), opName));
                }
            }
            writeToFile(fop, DotFormat.formatFooter());
    
            fop.close();
            
//            ProcessBuilder dotProcess = new ProcessBuilder("dot", "-Tpdf", fileName+".dot");
//            dotProcess.redirectOutput(new File(fileName+".pdf"));
            ProcessBuilder dotProcess = new ProcessBuilder("dot", "-Tpng", fileName+".dot");
            dotProcess.redirectOutput(new File(fileName+".png"));
            Process p = dotProcess.start();

            if (!Settings.DOT_OUTPUT_ASYNCHRONOUS) {
                p.waitFor();
            }
        } catch (InterruptedException e) {
            System.err.println("Dot output interrupted!");
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static  void writeToFile(FileOutputStream fop, String text) throws IOException {
        // get the content in bytes
        byte[] contentInBytes = text.getBytes();
    
        fop.write(contentInBytes);
        fop.flush();
    }
}
