package cz.agents.dimap.tools.miniplan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.codehaus.jparsec.util.Strings;

import cz.agents.dimap.tools.GenericPlanner;
import cz.agents.dimap.tools.GenericResult;
import cz.agents.dimap.tools.fd.Downward;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.sas.SasFile;

public class EPlanner implements GenericPlanner {
	
	public static void writeEproverInput(SasFile sas, File out) throws IOException {
		FileWriter writer = new FileWriter(out);
		
		int vars = sas.variables.size();
		
		for (String op : sas.operatorNames) {
			String fle = createPre(vars, sas.operatorPre.get(op), sas.operatorEff.get(op));
			String cnf = "cnf("+normalize(op)+", axiom, (\n\t"+fle+"\n)).\n\n";
			//System.out.println(cnf);
			writer.write(cnf);
		}
		String[] inits = new String[vars];
		int j = 0;
		for (String init : sas.init) {
			inits[j++] = "val"+init; 
		}
		String fle = "s("+Strings.join(",", inits)+")";
		String cnf = "cnf(init, axiom, (\n\t"+fle+"\n)).\n\n";
		writer.write(cnf);
		
		String[] goals = new String[vars];
		for (int i = 0; i < vars; i++) {
			goals[i] = "X"+i;
		}
		for (String goal : sas.goal) {
			String parts[] = goal.split(" ");
			int v = Integer.parseInt(parts[0]);
			int h = Integer.parseInt(parts[1]);
			goals[v] = "val"+h;
		}
		fle = "~s("+Strings.join(",", goals)+")";
		cnf = "cnf(goal, negated_conjecture, (\n\t"+fle+"\n)).\n\n";
		writer.write(cnf);
		
		writer.close();
	}

	private static String normalize(String op) {
		return op.replace(" ", "__").replace("-", "_");
	}

	private static String createPre(int vars, List<String> pres, List<String> effs) {
		String[] argsPre = new String[vars];
		String[] argsEff = new String[vars];
		for (int i=0; i < vars; i++) {
			argsPre[i] = "X"+i;
			argsEff[i] = "X"+i;
		}

		for (String pre : pres) {
			String parts[] = pre.split(" ");
			int v = Integer.parseInt(parts[0]);
			int h = Integer.parseInt(parts[1]);
			argsPre[v] = "val"+h;
		}
		
		for (String eff : effs) {
			String parts[] = eff.split(" ");
			int c = Integer.parseInt(parts[0]);
			int v = Integer.parseInt(parts[1]);
			int f = Integer.parseInt(parts[2]);
			int t = Integer.parseInt(parts[3]);
			if (c != 0) {
				System.err.println("Unsupported SAS effect: "+eff);
			}
			if (f > -1) {
				argsPre[v] = "val"+f;
			} 
			argsEff[v] =  "val"+t;
		}
		
		return "~s("+Strings.join(",", argsPre)+") | s("+Strings.join(",", argsEff)+")";
	}

	public static boolean runEProver(File input) throws IOException, InterruptedException, TimeoutException {
		System.out.println("EPROVER :: Proving "+input);
        //ProcessBuilder processBuilder = new ProcessBuilder(System.getProperty("user.dir")+"/../eprover/eprover", "--cpu-limit=295", "--memory-limit=2048", "--auto", "--tptp3-format", "--silent", input.getName());
		ProcessBuilder processBuilder = new ProcessBuilder(System.getProperty("user.dir")+"/../eprover/eprover", "--memory-limit=2048", "--auto", "--tptp3-format", "--silent", input.getName());
        processBuilder.directory(input.getParentFile());
        Process process = processBuilder.start();
        process.waitFor();

        String line;
    	BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		while ((line = reader.readLine()) != null) {
			//System.out.println(line);
			if (line.contains("Proof found!")) {
				System.out.println(line);
				return true;
			}
		}

    	reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		while ((line = reader.readLine()) != null) {
			System.err.println(line);
			throw new TimeoutException();
		}
		
		return false;		
	}
	
	@Override
	public GenericResult plan(PddlProblem problem) {
		GenericResult result = new GenericResult();
    	long startTime = System.currentTimeMillis();
		System.out.println("EPlanner :: Running FD translator");
		SasFile sasFile = Downward.runTranslator(problem);
		try {
			File eproverInputFile = new File("output.p");
			System.out.println("EPlanner :: Converting SAS file to EPROVER input");
			writeEproverInput(sasFile, eproverInputFile);
			result.solved = runEProver(eproverInputFile);
		} catch (IOException | InterruptedException e) {
			result.planningInfo = "ERROR: "+e.getMessage();
		} catch (TimeoutException e) {
			result.planningInfo = "timeout";
		}
		result.planningTime = System.currentTimeMillis() - startTime;
		return result;
	}
	
    public static void killEProver() {
    	try {
			Runtime.getRuntime().exec("killall -KILL eprover");
		} catch (IOException e) {
			System.err.println("killEProver: killall failed: "+e.getMessage());
			e.printStackTrace();
		}
    }

	
}
