package cz.agents.dimap.psmsas.operations;

import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.sas.SasFile;

public interface ReverseOperation {

    /**
     * Extends given plan by this operation.
     *
     * @param plan
     * @param sasFile
     * @return
     */
    void extend(Plan plan, SasFile sasFile);

    Integer getNumberOfReductions();
}
