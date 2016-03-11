package experiments.codmap.distributed.broker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import experiments.codmap.distributed.Message;
import experiments.codmap.distributed.Message.Command;

public class LastReachableProcessor {
    private int numOfAgents;

    private Map<List<String>, Desc> descMap = new HashMap<>();
    private Map<List<String>, List<BrokerComm>> commMap = new HashMap<>();
    
    public LastReachableProcessor(int numOfAgents) {
        this.numOfAgents = numOfAgents;
    }
    
    public boolean addPlan(BrokerComm comm, List<String> plan) {
        if (descMap.containsKey(plan)) {
            commMap.get(plan).add(comm);
            return false;
        } else {
            descMap.put(plan, new Desc());
            commMap.put(plan, new ArrayList<BrokerComm>(Arrays.asList(comm)));
            return true;
        }
    }

    public void processAnswer(List<String> plan, int value) {
        Desc desc = descMap.get(plan);
        desc.value = Math.min(desc.value, value);
        desc.numOfAnswers++;
        if (desc.numOfAnswers == numOfAgents - 1) {
            for (BrokerComm comm : commMap.get(plan)) {
                comm.sendMessage(new Message(Command.COMPUTE_LAST_REACHABLE_BY_ALL_OTHER_AGENTS, new Integer(desc.value)));
            }
            descMap.remove(plan);
            commMap.remove(plan);
        }
    }
    
    @Override
    public String toString() {
        return descMap.toString();
    }

    static class Desc {
        int value = Integer.MAX_VALUE;
        int numOfAnswers = 0;
        
        @Override
        public String toString() {
            return "desc: " + value + "%" + numOfAnswers;
        }
    }
}
