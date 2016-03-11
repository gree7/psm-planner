package cz.agents.dimap.psm.planner.completepsm;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import cz.agents.dimap.psm.planner.completepsm.psmoperator.InverseActionReduction;
import cz.agents.dimap.psm.planner.completepsm.psmoperator.PsmOperator;

public class PsmReduction {

    final static List<PsmOperator> idGraphOperators = Arrays.<PsmOperator>asList(
            new InverseActionReduction()
            );

    public static boolean process(PlanStateMachineWithVoidActions psm, Collection<PlanStateMachineWithVoidActions> psms) {

        boolean hasChangedOnce = false;
        boolean hasChanged = true;
        while (hasChanged) {
            hasChanged = false;

            for (PsmOperator operator : idGraphOperators) {
                while ( operator.process(psm, psms) ) {
                    hasChanged = true;
                    hasChangedOnce = true;
                }
            }

        }
        return hasChangedOnce;
    }

}
