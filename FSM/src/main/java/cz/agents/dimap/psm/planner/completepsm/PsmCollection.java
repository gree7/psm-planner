package cz.agents.dimap.psm.planner.completepsm;

import java.util.Collection;
import java.util.LinkedList;

import cz.agents.dimap.tools.Pair;

public class PsmCollection {
    LinkedList<PlanStateMachineWithVoidActions> psms = new LinkedList<>();
    LinkedList<PlanStateMachineWithVoidActions> allGoalsPsms = new LinkedList<>();

    public void addPsm(PlanStateMachineWithVoidActions psm) {
        if (psm.getNumberOfStates() > psm.getGoalStates().size()) {
            psms.addLast(psm);
        } else {
            allGoalsPsms.addLast(psm);
        }
    }

    public int size() {
        return psms.size()+allGoalsPsms.size();
    }

    public Pair<PlanStateMachineWithVoidActions, PlanStateMachineWithVoidActions> getPair() {
        PlanStateMachineWithVoidActions psm = findFirst();
//        PlanStateMachineWithVoidActions psm = leastGoalStates();
        return new Pair<PlanStateMachineWithVoidActions, PlanStateMachineWithVoidActions>(psm, findOther(psm));
//        return new Pair<PlanStateMachineWithVoidActions, PlanStateMachineWithVoidActions>(leastGoalStates(), leastGoalStates());
    }

    private PlanStateMachineWithVoidActions leastGoalStates() {
        int bestPsmValue = Integer.MAX_VALUE;
        PlanStateMachineWithVoidActions bestPsm = null;
        LinkedList<PlanStateMachineWithVoidActions> list;
        if (psms.isEmpty()) {
            list = allGoalsPsms;
        } else {
            list = psms;
        }
        for (PlanStateMachineWithVoidActions otherPsm : list) {
            int goals = otherPsm.getGoalStates().size();
            if (goals < bestPsmValue) {
                bestPsmValue = goals;
                bestPsm = otherPsm;
            }
        }
        list.remove(bestPsm);
        return bestPsm;
    }

    PlanStateMachineWithVoidActions findFirst() {
//        for (Iterator<PlanStateMachineWithVoidActions> iterator = psms.iterator(); iterator.hasNext();) {
//            PlanStateMachineWithVoidActions psm = iterator.next();
//            if (psm.getNumberOfStates() > psm.getGoalStates().size()) {
//                iterator.remove();
//                return psm;
//            }
//        }
        if (allGoalsPsms.isEmpty() || (psms.size() > 5 && size() > 10)) {
            return psms.remove();
        } else {
            return allGoalsPsms.remove();
        }
    }

    private PlanStateMachineWithVoidActions findOther(PlanStateMachineWithVoidActions psm) {
        int maxIntersection = -1;
        PlanStateMachineWithVoidActions bestPsm = null;
        LinkedList<PlanStateMachineWithVoidActions> list;
        if (psms.isEmpty()) {
            list = allGoalsPsms;
        } else {
            list = psms;
        }
        for (PlanStateMachineWithVoidActions otherPsm : list) {
            int commonActions = 0;
            for (String action : psm.voidActions) {
                if (otherPsm.voidActions.contains(action)) {
                    commonActions++;
                }
            }
            for (String action : psm.nonVoidActions) {
                if (otherPsm.nonVoidActions.contains(action)) {
                    commonActions++;
                }
            }
            if (commonActions > maxIntersection) {
                maxIntersection = commonActions;
                bestPsm = otherPsm;
            }
        }
        list.remove(bestPsm);
        return bestPsm;
    }

    public Collection<PlanStateMachineWithVoidActions> getPsms() {
        return psms;
    }

    public Collection<PlanStateMachineWithVoidActions> getAllGoalsPsms() {
        return allGoalsPsms;
    }
}