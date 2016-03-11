package cz.agents.dimap.tools.dot;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class DotFormat {
	
	public static boolean deconflictionDomain3x3 = false;
	public static boolean elevatorsDomain = false;
	
	private DotFormat() {}
	
	///
	/// Graph formatters
	///
	
	public static String formatHeader() {
		return "digraph G {\n" +
		        "    graph [\n" +
		        "        color = \"white\",\n" +
		        //"        concentrate = \"true\",\n" +
		        "        overlap = \"false\",\n" +
		        "        splines = \"spline\",\n" +
		        "    ];\n" +
		        "    node  [ shape = \"Mrecord\" ];\n" +
		        "    edge  [\n" + 
		        "        penwidth = \"0.8\" ];\n\n";
	}

	public static String formatFooter() {
		return "\n}\n";
	}

	///
	/// Node formatters
	///

	public static String formatGenericNode(String nodeName) {
		return "    " + nodeName + ";\n";
	}

	public static String formatGenericNode(String nodeName, String nodeLabel) {
		return "    " + nodeName + " [ label = \"" + nodeLabel + "\" ];\n";
	}

	public static String formatGenericNode(String nodeName, String nodeLabel, String bgColor) {
		return "    " + nodeName + " [ label = \"" + nodeLabel + "\", style=filled, fillcolor="+bgColor+" ];\n";
	}

    public static String formatNode(String nodeName, String nodeLabel) {
		return formatGenericNode(nodeName, formaNodeLabel(nodeLabel));
	}

	public static String formatNode(String nodeName, String nodeLabel, String bgColor) {
		return formatGenericNode(nodeName, formaNodeLabel(nodeLabel), bgColor);
	}

	public static String setNodeShape(String shape) {
		return "    node  [ shape = \"" + shape + "\" ];\n";
	}
	
	///
	/// Edge formatters
	///
	
	public static String formatGenericEdge(String from, String to) {
		return "" + from + " -> " + to + ";\n";
	}
	
	public static String formatGenericEdge(String from, String to, String label) {
		return "" + from + " -> " + to + " [ label = \"" + label + "\" ];\n";
	}
	
	public static String formatColorEdge(String from, String to, String color) {
		return "    " + from + " -> " + to + " [ color = \""+color+"\" ];\n";
	}
	
	public static String formatEdge(String from, String to, String label) {
		label = formatEdgeLabel(label);
		return "" + from + " -> " + to + " [ label = \"" + label + "\" ];\n";
	}

	///
	/// Label formatters
	///
	
	private static <S> String formaNodeLabel(String nodeLabel) {
		if (deconflictionDomain3x3) {
			return formatDeconflictionLabel(nodeLabel);
 		}
		if (elevatorsDomain) {
			return filterElevatorLabel(nodeLabel);
		}
		return nodeLabel;
	}

	private static int elevatorExtractLastNumber(String fact) {
		String[] parts = fact.split("-");
		return Integer.parseInt(parts[parts.length - 2].substring(1));
	}

	private static int elevatorExtractPrevNumber(String fact) {
		String[] parts = fact.split("-");
		return Integer.parseInt(parts[parts.length - 3].substring(1));
	}
	
	private static String filterElevatorLabel(String nodeLabel) {
		String parts[] = nodeLabel.split("\\{|\\||\\}");
		
		int lift_at = -1;
		List<Integer> liftPassangers = new LinkedList<>();
		List<Integer> reachables = new LinkedList<>();
		Map<Integer, List<Integer>> passangers = new LinkedHashMap<>();
		int max_level = 0;
		
		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];
			if (part.isEmpty()) {
				continue;
			}
			if (part.startsWith("int_lift-at")) {
				int level = elevatorExtractLastNumber(part);
				max_level = Math.max(level, max_level);
				lift_at = level;
			}
			else
			if (part.startsWith("int_passengers")) {
				liftPassangers.add(elevatorExtractLastNumber(part));			
			}
			else
			if (part.startsWith("pub_passenger")) {
				Integer level = elevatorExtractLastNumber(part);
				max_level = Math.max(level, max_level);
				if (!passangers.containsKey(level)) {
					passangers.put(level, new LinkedList<Integer>());
				}
				passangers.get(level).add(elevatorExtractPrevNumber(part));			
			}
			else
			if (part.startsWith("int_reachable-floor")) {
				Integer level = elevatorExtractLastNumber(part);
				max_level = Math.max(level, max_level);
				reachables.add(level);
			}
		}
		
		String legend = "{";
		String lift = "{";
		String lvls = "{";
		for (int level = max_level; level >= 0; level--) {
			legend += level;
			if (lift_at == level) {
				lift += liftPassangers.toString();
			}
			else
			if (reachables.contains(level)) {
				lift += "."; 
			}
			if (passangers.containsKey(level)) {
				lvls += passangers.get(level).toString();
			}
			if (level != 0) {
				lift += "|";
				lvls += "|";
				legend += "|";
			}
		}
		legend += "}";
		lift += "}";
		lvls += "}";
		return legend+"|"+lift+"|"+lvls;
		//return "{A||}|{A||}";
		//return nodeLabel;
	}

	
	@SuppressWarnings("unused")
	private static String filterLabelExcludePrefix(String nodeLabel, String[] excludedPrefixes) {
		StringBuilder label = new StringBuilder();
		label.append("{");
		String parts[] = nodeLabel.split("\\{|\\||\\}");
		PART: for (int i = 0; i < parts.length; i++) {
			String part = parts[i];
			if (part.isEmpty()) {
				continue;
			}
			for (String excludedPrefix : excludedPrefixes) {
				if (part.startsWith(excludedPrefix)) {
					continue PART;
				}
			}
			label.append(part);
			if (i != parts.length - 1) { label.append("|"); }
		}
		label.append("}");
		return label.toString();
	}

	protected static String formatDeconflictionLabel(String nodeLabel) {
		String[][] board = new String[3][3];
		for (int i = 0; i < 3; i++) 
			for (int j= 0; j < 3; j++)
				board[i][j] = "";
		
		String parts[] = nodeLabel.split("\\{|\\||\\}");
		for (String part : parts) {
			if (part.isEmpty()) continue;
			if (part.startsWith("pub_conn")) continue;
			if (part.startsWith("pub_empty")) {
				int i = Integer.parseInt(part.substring(11, 11+1));
				int j = Integer.parseInt(part.substring(13, 13+1));
				board[i][j] += '~';
			}
			if (part.startsWith("priv_at")) {
				int i = Integer.parseInt(part.substring(16, 16+1));
				int j = Integer.parseInt(part.substring(18, 18+1));
				board[i][j] += part.charAt(13);
			}
			if (part.startsWith("pub_at")) {
				int i = Integer.parseInt(part.substring(15, 15+1));
				int j = Integer.parseInt(part.substring(17, 17+1));
				board[i][j] += part.charAt(12);
			}
		}

		for (int i = 0; i < 3; i++) 
			for (int j= 0; j < 3; j++)
				if (board[i][j].isEmpty()) board[i][j] = "?";

		
		return "{"+board[0][0]+"|"+board[0][1]+"|"+board[0][2]+"}|{"+board[1][0]+"|"+board[1][1]+"|"+board[1][2]+"}|{"+board[2][0]+"|"+board[2][1]+"|"+board[2][2]+"}";
	}
	
	private static String formatEdgeLabel(String label) {
		if (deconflictionDomain3x3) {
			String parts[] = label.split("-");
			return parts[1];
		}
		return label;
	}
	
}
