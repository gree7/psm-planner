package cz.agents.dimap.tools.dot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.dimap.causal.CausalGraph;
import cz.agents.dimap.causal.FactCausalInfo;
import cz.agents.dimap.causal.InternalDependenciesGraph;
import cz.agents.dimap.causal.InternalDependenciesAndOr;
import cz.agents.dimap.landmarks.Landmark;
import cz.agents.dimap.tools.pddl.PddlAction;
import cz.agents.dimap.tools.pddl.PddlTerm;

public class DotTools {

	public static void dotLandmarks(Collection<Landmark> landmarks, String filename) {
		try {
			outputDotLandmarks(landmarks, filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void dotCausalGraph(CausalGraph graph, String filename) {
		try {
			outputDotCausalGraph(graph, filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public static void dotInternalDependenciesAndOr(InternalDependenciesAndOr graph, String filename) {
        try {
            outputDotInternalDependenciesAndOr(graph, filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public static void runDotToPng(String filename) throws IOException {
		runDot(filename, "png");
	}

	public static void runDotToSvg(String filename) throws IOException {
		runDot(filename, "svg");
	}

	private static void outputDotLandmarks(Collection<Landmark> landmarks, String filename) throws IOException {
		File file = new File(filename+".dot");
		FileOutputStream fop = new FileOutputStream(file);
		if (!file.exists()) {
			file.createNewFile();
		}
		
		writeToFile(fop, DotFormat.formatHeader());
		for (Landmark landmark : landmarks) {
			writeToFile(fop, DotFormat.formatGenericEdge(landmark.conditionToString(), landmark.effectToString(), landmark.getAction().name));
		}
        writeToFile(fop, DotFormat.formatFooter());

        fop.close();

        runDotToPng(filename);
	}

	private static void outputDotCausalGraph(CausalGraph graph, String filename) throws IOException {
		File file = new File(filename+".dot");
		FileOutputStream fop = new FileOutputStream(file);
		if (!file.exists()) {
			file.createNewFile();
		}
		
		writeToFile(fop, DotFormat.formatHeader());
		
		writeToFile(fop, DotFormat.setNodeShape("Mrecord"));
		
		for (PddlAction action : graph.actions) {
	        writeToFile(fop, DotFormat.formatGenericNode(makeDotId(action.name), action.name));
		}

		writeToFile(fop, DotFormat.setNodeShape("record"));
		for (FactCausalInfo factInfo : graph.facts.values()) {
		    if (graph.problem.init.getFacts().contains(factInfo.fact)) {
                writeToFile(fop, DotFormat.formatGenericNode(makeDotId(termName(factInfo.fact)), termName(factInfo.fact), "green"));
		    } else if (graph.problem.goal.getFacts().contains(factInfo.fact)) {
                writeToFile(fop, DotFormat.formatGenericNode(makeDotId(termName(factInfo.fact)), termName(factInfo.fact), "red"));
		    } else {
		        if (graph.problem.isPublicPredicate(factInfo.fact)) {
		            writeToFile(fop, DotFormat.formatGenericNode(makeDotId(termName(factInfo.fact)), termName(factInfo.fact), "yellow"));
		        } else {
                    writeToFile(fop, DotFormat.formatGenericNode(makeDotId(termName(factInfo.fact)), termName(factInfo.fact)));		            
		        }
		    }
		}

		for (PddlAction action : graph.actions) {
			for (PddlTerm pre : action.precondition.positives) {
				writeToFile(fop, DotFormat.formatColorEdge(makeDotId(termName(pre)), makeDotId(action.name), "blue"));
			}
			for (PddlTerm add : action.effect.positives) {
				writeToFile(fop, DotFormat.formatColorEdge(makeDotId(action.name), makeDotId(termName(add)), "black"));
			}
			for (PddlTerm del : action.effect.negatives) {
				writeToFile(fop, DotFormat.formatColorEdge(makeDotId(action.name), makeDotId(termName(del)), "red"));
			}
		}
		
		writeToFile(fop, DotFormat.formatFooter());
		
		fop.close();
	}

	private static void outputDotInternalDependenciesAndOr(InternalDependenciesAndOr graph, String filename) throws IOException {
	    File file = new File(filename+".dot");
	    FileOutputStream fop = new FileOutputStream(file);
	    if (!file.exists()) {
	        file.createNewFile();
	    }

	    writeToFile(fop, DotFormat.formatHeader());

	    writeToFile(fop, DotFormat.setNodeShape("Mrecord"));

	    for (PddlAction action : graph.actions) {
	        for (PddlTerm fact : action.getEffectFacts()) {
	            if (graph.problem.isPublicPredicate(fact)) {
	                writeToFile(fop, DotFormat.formatGenericNode(makeDotId(action.name), action.name, "yellow"));
	                break;
	            }
	        }
	    }

	    writeToFile(fop, DotFormat.formatFooter());

	    fop.close();
	}

	public static String termName(PddlTerm term) {
		return term.toString().replaceAll(" ", "-");
	}
	
	public static String makeDotId(String name) {
		return name.replace("-", "_");
	}
	
	private static void runDot(String baseName, String imgFormat) throws IOException {
		ProcessBuilder dotProcess = new ProcessBuilder("dot", "-T"+imgFormat, baseName+".dot");
		dotProcess.redirectOutput(new File(baseName+"."+imgFormat));
		dotProcess.start();
		
		//Process p = dotProcess.start();
		//p.waitFor();
	}

	private static  void writeToFile(FileOutputStream fop, String text) throws IOException {
		byte[] contentInBytes = text.getBytes();
		fop.write(contentInBytes);
		fop.flush();
	}

	
}
