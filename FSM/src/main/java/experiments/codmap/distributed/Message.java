package experiments.codmap.distributed;

import java.io.Serializable;

public class Message implements Serializable {
    
    private static final long serialVersionUID = 5697233746337243388L;

    public static final Message OK = new Message(Command.OK, null);
    
    public static enum Command {
        OK,
        LOGIN,
        // preprocessing
        ATOMS_WITH_HYPHENS,
        TRANSLATION_TABLE,
        CONSTANTS,
        
        // relaxed plan & reachable facts exchange
        CREATE_RELAXED_PLAN_REACHABLE_FACTS,
        PUBLIC_FACTS,
        
        RELAXED_PLAN,
        CREATE_RELAXED_PLAN_GOAL_REACHED,
        CREATE_RELAXED_PLAN_EXRTACT_PLAN,
        CREATE_RELAXED_PLAN_NEXT_LAYER,
        CREATE_PUBLIC_PLAN,
        
        // external actions exchange
        EXTERNAL_ACTIONS,

        // planning
        RESET, 
        PLAN,
        COMPUTE_LAST_REACHABLE_BY_ALL_OTHER_AGENTS, // agent queries broker
        COMPUTE_LAST_REACHABLE,  // broker queries one agent
        KILL_ALL,

        RESET_LANDMARKS,
        COMPUTE_PUBLIC_LANDMARKS,
        ADD_PUBLIC_LANDMARKS,
        
        INTERNAL_EXTENSION,

        END,
    }

    public Command command;
    public Object content;

    public Message(Command command, Object content) {
        this.command = command;
        this.content = content;
    }
}
