package cz.agents.dimap.tools.dot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DotWriter extends FileWriter {

	private String baseName;
	
	public DotWriter(String baseName) throws IOException {
		super(baseName+".dot");
		this.baseName = baseName;
	}

	public DotWriter(File file) throws IOException {
		this(file.getPath());
	}

	/*
	 * Top level, Graphs
	 */
	public void writeHeader() throws IOException {
		write("digraph {\n\n");
	}

	public void writeFooter() throws IOException {
		write("\n}\n");
	}
	
	public void writeGraphStyle(DotAttr... attrs) throws IOException {
		writeStatementMultiLine("graph", attrs);
	}

	/*
	 * Edges
	 */
	public void writeEdge(String idFrom, String idTo, DotAttr... attrs) throws IOException {
		writeStatement(idFrom+" -> "+idTo, attrs);
	}

	public void writeEdge(String idFrom, String idTo) throws IOException {
		writeEdge(idFrom, idTo, (DotAttr[])null);
	}

	
	public void writeEdgeStyle(DotAttr... attrs) throws IOException {
		writeStatement("edge", attrs);
	}

	/*
	 * Nodes
	 */
	public void writeNode(String nodeId, DotAttr... attrs) throws IOException {
		writeStatement(nodeId, attrs);
	}

	public void writeNodeStyle(DotAttr... attrs) throws IOException {
		writeStatement("node", attrs);
	}

	/*
	 * Generic statements
	 */
	public void writeStatement(String decl, DotAttr... attrs) throws IOException {
		writeStatement(decl, false, attrs);
	}
	
	public void writeStatementMultiLine(String decl, DotAttr... attrs) throws IOException {
		writeStatement(decl, true, attrs);
	}
	
	public void writeStatement(String name, boolean multiLine, DotAttr... attrs) throws IOException {
		String delim = multiLine ? "\n" : " ";
		write("\t"); write(name);
		if (attrs != null && attrs.length > 0) {
			write(" ["); write(delim);
			for (int i = 0; i < attrs.length; i++) {
				if (multiLine) { write("\t\t"); }
				write(attrs[i].toString());
				if (i != attrs.length - 1) { write(","); }
				write(delim);
			}
			if (multiLine) { write("\t"); }
			write("]");
		}
		write(";\n");
	}

	public void generate() throws IOException {
		DotTools.runDotToSvg(baseName);
	}
	
}
