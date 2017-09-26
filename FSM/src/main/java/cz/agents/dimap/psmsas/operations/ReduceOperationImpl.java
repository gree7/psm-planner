package cz.agents.dimap.psmsas.operations;

import cz.agents.dimap.tools.sas.SasFile;

abstract public class ReduceOperationImpl implements ReduceOperation {

    @Override
    public ReverseOperation reduce(SasFile sasFile, boolean alreadyReduced) {
        return reduce(sasFile);
    }
}
