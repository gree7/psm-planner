package cz.agents.dimap.tools;

public class GenericResult {
	public boolean solved = false;
	
	public double planningTime = Double.NaN;
	public String planningInfo = "";

	//public Object genericResult = null;
	
	public String toString(String domainName, String problemName) {
       	return String.format("###RESULT### %s %s:%s @ %.02f [%s]", solved ? "SOLVED" : "FAILED", domainName, problemName, planningTime/1000.0, planningInfo);
	}
	
	public String toString(String domainName, String problemName, int problemNumber) {
		return toString(domainName, String.format("%s[%d]", problemName, problemNumber));
	}
	
}
