package cz.agents.dimap.tools.sas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SasParser {

	public static void expectToken(BufferedReader br, String token) throws IOException {
		String line = br.readLine();
		if (line == null) {
			throw new IOException("ERROR parsing SAS file: unexpected EOF");
		}
		if (!line.trim().equals(token)) {
			throw new IOException("ERROR parsing SAS file: expected "+token+" but have "+line);
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
		writer.write(actionCount+"\n");
		for (int i = 0; i < actionCount; i++) {
			writer.write(reader.readLine()+"\n");
		}
		
		expectToken(reader, "end_plan");
		writeToken(writer, "end_plan");
	}

	public static void stripPlanAfterActionIndex(BufferedReader reader, BufferedWriter writer, int actionIndex) throws IOException {
		expectToken(reader, "begin_plan");
		writeToken(writer, "begin_plan");
		
		int actionCount = Integer.parseInt(reader.readLine());
		writeToken(writer, Integer.toString(actionIndex+2));
		int index;
		for (index = 0; index <= actionIndex; index++) {
			writer.write(reader.readLine()+"\n");
		}
		writeToken(writer, "*");
		for (; index < actionCount; index++) {
			reader.readLine();
		}
		
		expectToken(reader, "end_plan");
		writeToken(writer, "end_plan");
	}

}
