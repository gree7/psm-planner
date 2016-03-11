package cz.agents.dimap.tools.fd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cz.agents.dimap.Settings;
import cz.agents.dimap.tools.pddl.PddlActionInstance;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class RelaxedPlanCreator {
    
    private RelaxedPlanCreator() {}

    public static List<String> generateRelaxedPlan(PddlProblem relaxedProblem, Collection<String> agentNames, PddlProblem originalProblem) throws InterruptedException {
        List<String> relaxedPlan = planRelaxedPlan(relaxedProblem);
        List<String> publicRelaxedPlan = projectRelaxedPlanToPublic(relaxedPlan, originalProblem, agentNames);
        return publicRelaxedPlan;
    }

    private static List<String> planRelaxedPlan(PddlProblem relaxedProblem) throws InterruptedException {
        try {

            List<List<String>> createdPlans = Downward.runDownward(relaxedProblem, "relaxed", true);
            List<String> relaxedPlan = createdPlans.get(0);
            if (Settings.VERBOSE) System.out.println("relaxed plan: " + relaxedPlan);
            return relaxedPlan;

         } catch (DownwardException e) {
            System.out.println("No relaxed solution!");
            throw new DownwardException("No relaxed solution!");
        }
    }

    static List<String> projectRelaxedPlanToPublic( List<String> relaxedPlan, PddlProblem problem, Collection<String> agentNames) {
        List<String> publicPlan = new ArrayList<>(relaxedPlan.size());
        for (String actionStr : relaxedPlan) {
            PddlActionInstance bindedAction = PddlActionInstance.parsePlannedPublicAction(actionStr, problem);
            if (bindedAction != null) {
                String[] bindedDesc = bindedAction.getName().split(" ");
                String bindedName = "";
                for (int i=1; i<bindedDesc.length; i++) {
                    if (!agentNames.contains(bindedDesc[i])) {
                        bindedName += " " + bindedDesc[i];
                    }
                }
                String ownerName = null;
                for (String arg : actionStr.split(" ")) {
                    if (agentNames.contains(arg)) {
                        ownerName = arg;
                    }
                }
                
                if (ownerName == null) {
                    boolean isAgentNameInActionName = false;
                    for (String agentName : agentNames) {
                        if (bindedDesc[0].contains("_"+agentName)) {
                            ownerName = agentName;
                            isAgentNameInActionName = true;
                            break;
                        }
                    }
                    if (bindedAction.getOriginalAction().parameters.isEmpty()) {
                        // groundedAction --> use original name
                        publicPlan.add(bindedDesc[0]);
                    } else {
                        if (isAgentNameInActionName) {
                            publicPlan.add(bindedDesc[0] + bindedName);
                        } else {
                            throw new IllegalArgumentException("No owner agent! " + actionStr + " -- " + agentNames);
                        }
                    }
                } else {
                    publicPlan.add(bindedDesc[0] + "_" + ownerName + bindedName);
                }
            }
         }
        return publicPlan;
    }
}
