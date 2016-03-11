package cz.agents.dimap.psm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import cz.agents.dimap.fsm.GoalCondition;
import cz.agents.dimap.fsm.MultiStateGoalCondition;
import cz.agents.dimap.fsm.Transition;
import cz.agents.dimap.psm.operator.Operator;
import cz.agents.dimap.psm.state.IntersectionState;
import cz.agents.dimap.psm.state.State;

/**
 * Created with IntelliJ IDEA.
 * User: durkokar
 * Date: 1/13/14
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlanStateMachineTools {
	
    static PlanStateMachine merge(PlanStateMachine fsm1, PlanStateMachine fsm2) {
        return PlanStateMachineTools.merge(fsm1, fsm2, false);
    }

    static PlanStateMachine merge(PlanStateMachine fsm1, PlanStateMachine fsm2, boolean verbose){
        Map<State, Integer> fsm1Indexing = new HashMap<>();
        Map<State, Integer> fsm2Indexing = new HashMap<>();
        int counter = 0;
        for ( State s : fsm1.getStates() ) {
            fsm1Indexing.put(s,counter++);
        }
        counter = 0;
        for ( State s : fsm2.getStates() ) {
            fsm2Indexing.put(s,counter++);
        }

        IntersectionState init = new IntersectionState(fsm1.getInitState(), fsm2.getInitState(), fsm1, fsm2, fsm1Indexing.get(fsm1.getInitState()), fsm2Indexing.get(fsm2.getInitState()));
        PlanStateMachine intersectedFsm = new PlanStateMachine(init);
        Queue<IntersectionState> openList = new LinkedList<IntersectionState>();
        openList.add(init);
        Set<IntersectionState> closedList = new HashSet<IntersectionState>();

        while ( !openList.isEmpty() ) {
            IntersectionState is = openList.poll();

            if ( verbose ) {
                System.out.print("O=" + openList.size() + ";C="+closedList.size()+";States="+intersectedFsm.getNumberOfStates()+";Transitions="+intersectedFsm.getNumberOfTransitions());
            }

            if (closedList.contains(is)) {
                if (verbose) {
                    System.out.println("already Expanded");
                }
                continue;
            } else {
                if ( verbose ) {
                    System.out.println("");
                }
            }
            closedList.add(is);
            Collection<Transition<State, Operator>> trans = is.expandState(fsm1Indexing, fsm2Indexing);

            for ( Transition<State, Operator> t : trans) {
                intersectedFsm.addTransition(is, t.getOperator(), t.getToState());
                if ( !openList.contains(t.getToState()) && !closedList.contains(t.getToState())) {
                    openList.add((IntersectionState)t.getToState());
                }
            }
        }
        
        intersectedFsm.setGoals( getIntersectedGoalStates(intersectedFsm, fsm1.getGoalStates(), fsm2.getGoalStates()));
        
        return intersectedFsm;
    }

    public static PlanStateMachine intersection( PlanStateMachine fsm1, PlanStateMachine fsm2) {
        return PlanStateMachineTools.intersection(fsm1, fsm2, false);
    }

    public static PlanStateMachine intersection( PlanStateMachine fsm1, PlanStateMachine fsm2, boolean verbose) {
        PlanStateMachine fsm = PlanStateMachineTools.merge(fsm1, fsm2, verbose);
        fsm.removeStatesNotLeadingTo(fsm.getGoalStates());
        return fsm;
    }

    public static Collection<State> getIntersectedGoalStates(PlanStateMachine fsm, Collection<State> goals1, Collection<State> goals2 ){
        Set<State> goals = new HashSet<>();
        for ( State s1 : goals1 ) {
            for ( State s2 : goals2 ) {
                if ( s1.getName().equals(s2.getName())) {
                    State goal = PlanStateMachineTools.getIntersectedGoalState(fsm, s1.getName());
                    if ( goal != null ) {
                        goals.add(goal);
                    }
                }
            }
        }
        return goals;
    }

    public static State getIntersectedGoalState(PlanStateMachine fsm, String name) {
        for ( State s : fsm.getStates()) {
            IntersectionState is = (IntersectionState)s;
            if ( is.getName().equals(name) ) {
                return s;
            }
        }
        return null;
    }

    public static State getIntersectedGoalState(PlanStateMachine fsm, State parent1, State parent2) {
        for ( State s : fsm.getStates()) {
            IntersectionState is = (IntersectionState)s;
            if ( (is.getParent1() == parent1 && is.getParent2() == parent2 ) ||
                    (is.getParent1() == parent2 && is.getParent2() == parent1 ) ) {
                return s;
            }
        }
        return null;
    }

    public static Collection<State> getGoalStates(PlanStateMachine fsm, GoalCondition<State> gc) {
        Collection<State> goalStates = new ArrayList<>();
        for (State state : fsm.getStates()) {
            if (gc.isGoal(state)) {
                goalStates.add(state);
            }
        }

        return goalStates;
    }

	public static List<Operator> getRandomPlan(PlanStateMachine fsm) {
	    GoalCondition<State> goalCondition = new MultiStateGoalCondition<>(fsm.getGoalStates());
	    List<Operator> plan = new ArrayList<>();
		State curState = fsm.getInitState();
		while ( !goalCondition.isGoal(curState) ) {
			Collection<Transition<State, Operator>> transitions = fsm.getTransitions(curState);
			if (transitions == null) {
				System.out.println("!!! curState: " + curState);
			}
			int randNum = (int) ( Math.random() * transitions.size() );
			Iterator<Transition<State, Operator>> iterator = transitions.iterator();
			for (int i = 0; i < randNum; i++) {
				iterator.next();
			}
			if (!iterator.hasNext()) {
				return plan;
			}
			Transition<State, Operator> trans = iterator.next();
			plan.add(trans.getOperator());
			curState = trans.getToState();
		}
		return plan;
	}
		
    public static List<Operator> getShortestPlan(PlanStateMachine fsm) {
        PriorityQueue<HeapElement> open = new PriorityQueue<>();
        open.add(new HeapElement(fsm.getInitState(), new ArrayList<Operator>(), 0));
        Map<State, HeapElement> elements = new HashMap<>();
        Set<State> closed = new HashSet<>();
        while (true) {
            HeapElement openState = open.remove();
            closed.add(openState.state);
            State state = openState.state; 
            List<Operator> plan = openState.bestPlan;
            int price = openState.price + 1;
            if (fsm.getGoalStates().contains(state)) {
                return plan;
            }
            
            Collection<Transition<State, Operator>> transitions = fsm.getTransitions(state);
            
            for (Transition<State, Operator> transition : transitions) {
                State toState = transition.getToState();
                if (closed.contains(toState)) {
                    continue;
                }
                List<Operator> operators = new ArrayList<>(plan);
                operators.add(transition.getOperator());
                HeapElement heapElement = elements.get(toState);
                if (heapElement == null) {
                    heapElement = new HeapElement(toState, operators, price);
                    open.add(heapElement);
                    elements.put(toState, heapElement);
                } else {
                    if (heapElement.price > price) {
                        open.remove(heapElement);
                        heapElement = new HeapElement(toState, operators, price);
                        open.add(heapElement);
                        elements.put(toState, heapElement);
                    }
                }
            }
        }
    }
        
	public static String stripActionPrefix(String name) {
		if (name.startsWith("pub_") || name.startsWith("ext_")) {
			return name.substring(4); 
		}
		if (name.startsWith("lm_")) {
			String[] parts = name.split("_-_");
			return parts[1];
		}
		return name;
	}
}

class HeapElement implements Comparable<HeapElement> {
    State state;
    List<Operator> bestPlan;
    int price;

    public HeapElement(State state, List<Operator> plan, int price) {
        this.state = state;
        bestPlan = plan;
        this.price = price;
    }

    @Override
    public int compareTo(HeapElement o) {
        return price - o.price;
    }
}
