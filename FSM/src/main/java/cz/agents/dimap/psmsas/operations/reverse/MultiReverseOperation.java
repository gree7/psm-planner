package cz.agents.dimap.psmsas.operations.reverse;


import java.util.LinkedList;
import java.util.List;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.sas.SasFile;

public class MultiReverseOperation implements ReverseOperation {
    private final LinkedList<ReverseOperation> reverseOperations = new LinkedList<>();
    private String description;
    private int numberOfReductions;

    public MultiReverseOperation(String description) {
        this(description, 0);
    }

    public MultiReverseOperation(String description, int numberOfReductions) {
        this.description = description;
        this.numberOfReductions = numberOfReductions;
    }

    public void addReverseOperation(ReverseOperation reverseOperation) {
        if (reverseOperation != null) {
            reverseOperations.addFirst(reverseOperation);
            numberOfReductions += reverseOperation.getNumberOfReductions();
        }
    }

    @Override
    public void extend(Plan plan, SasFile sasFile) {
        for (ReverseOperation reverseOperation : reverseOperations) {
            reverseOperation.extend(plan, sasFile);
        }
    }

    @Override
    public Integer getNumberOfReductions() {
        return numberOfReductions;
    }

    public boolean isEmpty() {
        return reverseOperations.isEmpty();
    }
    
    @Override
    public String toString() {
        return description + ": " + reverseOperations.toString();
    }

    public List<ReverseOperation> getReverseOperations() {
        return reverseOperations;
    }
}
