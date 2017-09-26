package cz.agents.dimap.psmsas.operations.reverse;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import cz.agents.dimap.psmsas.operations.ReverseOperation;
import cz.agents.dimap.tools.Plan;
import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;

/**
 * Created by pah on 23.7.15.
 */
public class ShrinkSimilarOperatorsReverse implements ReverseOperation {
    private Operator first;
    private final List<Operator> rest = new LinkedList<>();

    public ShrinkSimilarOperatorsReverse(Operator operator, Collection<Operator> bag) {
        first = operator;
        rest.addAll(bag);
    }

    @Override
    public void extend(Plan plan, SasFile sasFile) {
        for (Operator operator : rest) {
            operator.setActive();
        }
    }

    @Override
    public String toString() {
        return "ShrinkSimilarOperators-"+first;
    }

    @Override
    public Integer getNumberOfReductions() {
        return 1;
    }
}
