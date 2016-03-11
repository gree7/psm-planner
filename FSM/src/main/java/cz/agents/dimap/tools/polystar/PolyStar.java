package cz.agents.dimap.tools.polystar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

import cz.agents.dimap.Settings;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlProblem.PddlProblemStatus;


public class PolyStar {

	//public static String POLYSTAR_BIN = "../polystar-bin/polystar";
	public static String POLYSTAR_BIN;
	static {
	    if (new File("polystar").exists()) {
	        POLYSTAR_BIN = System.getProperty("user.dir") + "/polystar";
	    } else {
	        POLYSTAR_BIN = "/usr/local/bin/polystar";
	    }
	}
	
	public static String computeType(String polyStarInstance) throws IOException, InterruptedException {
        ProcessBuilder processBuilder;
        File file = Files.createTempFile("polystar-", ".pst").toFile();
        //File file = new File("gr.pst");
        FileWriter writer = new FileWriter(file);
        writer.write(polyStarInstance);
        writer.close();

        processBuilder = new ProcessBuilder(new File(POLYSTAR_BIN).getAbsolutePath(), file.getPath());
        Process process = processBuilder.start();
        process.waitFor();
        
        BufferedReader reader;
        String line;
        StringBuilder builder = new StringBuilder();
        
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		if (Settings.DEBUG) { System.out.println("\nPOLY*: Type inferred:\n"); }
        while ((line = reader.readLine()) != null) {
        	if (Settings.DEBUG) { System.out.println(line); }
        	builder.append(line).append(System.getProperty("line.separator"));
        }
        
        reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        while ((line = reader.readLine()) != null) {
            System.err.println(line);
        }
        
        if (Settings.DELETE_TMP_FILES_ON_EXIT) {
        	file.delete();
        }

        return builder.toString();
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		File dir = new File("experiments/ma-benchmarks/", "woodworking");
		File d = new File(dir, "woodworking.pddl");
		//File p = new File(dir, "woodworking-a2.pddl");
		File p = new File(dir, "woodworking-p01.pddl");
		
		Settings.DEBUG = true;
		PddlProblem problem = new PddlProblem(d.getPath(), p.getPath());
		System.out.println(problem.domain);
		PddlProblemStatus status = PolyStarGoalReachebility.computeGoalReachebilityStatus(problem);
		System.out.println(status);
	}

	public static void killPolyStar() {
		try {
			Runtime.getRuntime().exec("killall -KILL polystar");
		} catch (IOException e) {
			System.err.println("killPolyStar: killall failed: "+e.getMessage());
			e.printStackTrace();
		}
	}
	
}
