package cz.agents.dimap.psmsas.operations;

import cz.agents.dimap.tools.sas.SasFile;

/**
 * Created by pah on 10.7.15.
 *
 *
 */
public interface ReduceOperation {

    /**
     *  Takes the sasFile and performs an reduce operator on it. It returns ReverseOperating representing the backward transformation.
     *
     * @param sasFile
     * @return
     */
    ReverseOperation reduce(SasFile sasFile);

    ReverseOperation reduce(SasFile sasFile, boolean alreadyReduced);

    /**
     * Should be called before each time reduce is called on different SAS, since the object stateful.
     */
    void clearCache();


}
