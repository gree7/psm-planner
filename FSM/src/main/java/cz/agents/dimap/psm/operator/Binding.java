package cz.agents.dimap.psm.operator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.dimap.tools.pddl.PddlName;

public class Binding {

    private final Map<PddlName, PddlName> binding = new LinkedHashMap<>();

    public Binding() {
    }

    public Binding(Binding other) {
        this();
        binding.putAll(other.binding);
    }

    public void addBinding(PddlName parameterName, PddlName parameterValue) {
        binding.put(parameterName, parameterValue);
    }

    public boolean containsParameter(PddlName parameterName) {
        return binding.containsKey(parameterName);
    }

    public PddlName getParameterValue(PddlName parameterName) {
        return binding.get(parameterName);
    }

    @Override
    public String toString() {
        return "Binding [binding=" + binding + "]";
    }

    public int size() {
        return binding.size();
    }

    public boolean isEmpty() {
        return binding.isEmpty();
    }

    public Set<PddlName> getBindedParameters() {
        return binding.keySet();
    }

    public Map<PddlName, PddlName> getBindings() {
        return binding;
    }

    public void add(Binding paramBinding) {
        for (Entry<PddlName, PddlName> entry : paramBinding.binding.entrySet()) {
            PddlName curVal = binding.get(entry.getKey());
            if (curVal==null) {
                addBinding(entry.getKey(), entry.getValue());
            } else if (curVal.equals(entry.getValue())) {
                // ok, same value
            } else {
                throw new IllegalArgumentException("keys are not disjunctive: " + entry + " vs. " + curVal);
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((binding == null) ? 0 : binding.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Binding other = (Binding) obj;
        if (binding == null) {
            if (other.binding != null)
                return false;
        } else if (!binding.equals(other.binding))
            return false;
        return true;
    }
}
