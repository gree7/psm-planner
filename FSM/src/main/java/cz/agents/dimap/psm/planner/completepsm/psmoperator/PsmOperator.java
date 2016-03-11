package cz.agents.dimap.psm.planner.completepsm.psmoperator;

import java.util.Collection;

import cz.agents.dimap.psm.planner.completepsm.PlanStateMachineWithVoidActions;

public interface PsmOperator {

    boolean process(PlanStateMachineWithVoidActions psm, Collection<PlanStateMachineWithVoidActions> psms);
}
