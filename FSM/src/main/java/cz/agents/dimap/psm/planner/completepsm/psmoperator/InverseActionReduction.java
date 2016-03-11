package cz.agents.dimap.psm.planner.completepsm.psmoperator;

import java.util.Collection;

import cz.agents.dimap.fsm.Transition;
import cz.agents.dimap.psm.operator.Operator;
import cz.agents.dimap.psm.planner.completepsm.PlanStateMachineWithVoidActions;
import cz.agents.dimap.psm.planner.completepsm.PsmCollection;
import cz.agents.dimap.psm.state.State;

public class InverseActionReduction implements PsmOperator {

    @Override
    public boolean process(PlanStateMachineWithVoidActions psm, Collection<PlanStateMachineWithVoidActions> psms) {
        for (State state : psm.getStates()) {
            for (Transition<State, Operator> trans : psm.getTransitions(state)) {

                State otherState = trans.getToState();

                if (!state.equals(otherState)
                        && !psm.getInitState().equals(otherState)
                        && isReducable(trans.getOperator().getName(), psms)) {
                    Collection<Transition<State, Operator>> otherTransitions = psm.getTransitionsBetween(otherState, state);
                    for (Transition<State, Operator> otherTrans : otherTransitions) {
                        if (state.equals(otherTrans.getToState()) && isReducable(otherTrans.getOperator().getName(), psms)) {
                            
                            mergeStates(psm, state, trans, otherState, otherTrans);

                            if (psm.getGoalStates().contains(otherState) && !psm.getGoalStates().contains(state)) {
                                psm.addGoal(state);
                            }
                            psm.removeState(otherState);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void mergeStates(PlanStateMachineWithVoidActions psm, 
            State state, Transition<State, Operator> trans, State otherState, Transition<State, Operator> otherTrans) {
        for (Transition<State, Operator> otherTransition : psm.getTransitions(otherState)) {
            if (!otherTransition.equals(otherTrans)) {
                psm.addTransition(state, otherTransition.getOperator(), otherTransition.getToState());
            }
        }
        for (Transition<State, Operator> otherTransition : psm.getInverseTransitions(otherState)) {
            if (!otherTransition.equals(trans)) {
                psm.addTransition(otherTransition.getToState(), otherTransition.getOperator(), state);
            }
        }
    }

    private boolean isReducable(String action, Collection<PlanStateMachineWithVoidActions> psms) {
        for (PlanStateMachineWithVoidActions psm : psms) {
            if (psm.nonVoidActions.contains(action)) {
                return false;
            }
        }
        return true;
    }
}
