package cz.agents.dimap.psmsas.operations;

import cz.agents.dimap.tools.sas.SasFile;

abstract public class ReduceOperationLazyImpl implements ReduceOperation {

    @Override
    public ReverseOperation reduce(SasFile sasFile, boolean alreadyReduced) {
        if (!alreadyReduced) {
            return reduce(sasFile);
        } else {
            return null;
        }
    }
}
