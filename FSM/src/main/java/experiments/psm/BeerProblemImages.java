package experiments.psm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cz.agents.dimap.fsm.FiniteStateMachineTools;
import cz.agents.dimap.fsm.GoalCondition;
import cz.agents.dimap.fsm.LabelFactory;
import cz.agents.dimap.psm.PlanStateMachine;
import cz.agents.dimap.psm.PlanStateMachineTools;
import cz.agents.dimap.psm.operator.Operator;
import cz.agents.dimap.psm.planner.BfsPsmPlanner;
import cz.agents.dimap.psm.planner.ExternalActions;
import cz.agents.dimap.psm.planner.MultiAgentProblem;
import cz.agents.dimap.psm.planner.PddlPlanner;
import cz.agents.dimap.psm.planner.PsmPlanner;
import cz.agents.dimap.psm.state.State;
import cz.agents.dimap.tools.pddl.PddlProblem;
import experiments.psm.fmap.FmapSingleDomainInfo;
import experiments.psm.fmap.MaStripsMultiAgentProblem;

public class BeerProblemImages {

    static DomainInfo domainInfo = new FmapSingleDomainInfo("beerproblem", 1, "domain", "problem");

    static boolean allExternal = false;
    
    static LabelFactory<State, Operator> labelFactory = new LabelFactory<State, Operator>() {
        @Override
        public String createStateLabel(State state) {
            return "";
        }

        @Override
        public String createOperatorLabel(Operator operator) {
            String text = operator.getName().replace("_", "");
            if (operator.getAction().isExternal() || allExternal) {
            } else if (operator.getAction().isPublic() ) {
                text += "\" style=\"dashed";
            } else {
                text += "\" style=\"dotted";
            }
            
            text = text.replace("truck", "p");
            text = text.replace("plane", "t");
            text = text.replace("prague", "b");
            text = text.replace("ostrava", "o");
            text = text.replace("pilsen", "p");

            text = text.replace("unload", "u");
            text = text.replace("load", "l");
            text = text.replace("move", "m");

            text = text.replace("mt", "d");
            text = text.replace("mp", "f");
            
            return text;
        }
    };

    static PlanStateMachine createPublicPsm(PddlProblem problem, final String agentName) throws Exception {

        final PddlPlanner pddlPlanner = new PddlPlanner(problem);
        
        final BfsPsmPlanner planner = new BfsPsmPlanner(pddlPlanner.getInitState(), pddlPlanner.getOperators(), false);

        planner.createAllPlans(pddlPlanner.getGoalCondition(), pddlPlanner.getBitSetStateFactory());
        
        System.out.println(agentName + ": " + planner.getFsm());

        planner.fsmToDot("psm-" + agentName+"-int", labelFactory, true, pddlPlanner.getPublicGoalCondition());

        planner.publicProjection(pddlPlanner.getBitSetStateFactory());

        PlanStateMachine psm = planner.getFsm();
        Collection<State> goals = new ArrayList<>();
        for (State state : psm.getStates()) {
            if (pddlPlanner.getGoalCondition().isGoal(state)) {
                goals.add(state);
            }
        }
        psm.setGoals(goals);

        return psm;
    }  

    private static PlanStateMachine createPublicPsm(
            HashMap<String, ExternalActions> externalActions,
            Map<String, PddlProblem> agentProblems, String agentName) throws Exception {
        PddlProblem problem = agentProblems.get(agentName);
        
        PsmPlanner.extendDomainWithPublicActions(agentName, problem, externalActions);
        PsmPlanner.extendDomainWithNewInitFacts(problem, externalActions);

        PlanStateMachine psm = createPublicPsm(problem, agentName);
        return psm;
    }

    public static void main(String[] args) throws Exception {
        MultiAgentProblem maProblem = domainInfo.createProblem(1);
        maProblem = new MaStripsMultiAgentProblem(maProblem);
        

        HashMap<String, ExternalActions> externalActions = new HashMap<>();
        Map<String, PddlProblem> agentProblems = new HashMap<>();
        PsmPlanner.computeAgentProblems(maProblem, externalActions, agentProblems);

        final PlanStateMachine planePsm = createPublicPsm(externalActions, agentProblems, "plane");
        final PlanStateMachine truckPsm = createPublicPsm(externalActions, agentProblems, "truck");
        final PlanStateMachine intersection = PlanStateMachineTools.intersection(planePsm, truckPsm);

        GoalCondition<State> goalCondition = new GoalCondition<State>() {
            @Override
            public boolean isGoal(State state) {
                return (planePsm.getGoalStates().contains(state))
                        || (truckPsm.getGoalStates().contains(state))
                        || (intersection.getGoalStates().contains(state));
            }
        };

        FiniteStateMachineTools.imgOutput(planePsm, "psm-plane", labelFactory, goalCondition); 
        FiniteStateMachineTools.imgOutput(truckPsm, "psm-truck", labelFactory, goalCondition);
        
        allExternal = true;
        FiniteStateMachineTools.imgOutput(intersection, "psm-intersection", labelFactory, goalCondition);
    }

}
