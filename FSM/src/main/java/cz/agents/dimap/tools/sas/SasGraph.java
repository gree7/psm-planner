package cz.agents.dimap.tools.sas;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SasGraph {
    public Map<Integer, List<Integer>> graph = new LinkedHashMap<>();
    
    public void add(Integer node, List<Integer> toNodes) {
        graph.put(node, toNodes);
    }
}
