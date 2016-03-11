package cz.agents.dimap.pddl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.ma.PddlAddl;
import cz.agents.dimap.tools.pddl.types.PddlType;

public class AddlTest {

	private static final String addlTest1 = 
        "(define (problem elevators)\n" + 
        "    (:domain elevators)\n" + 
        "    (:agentTypes elevator)\n" + 
        "    (:shared-data\n" + 
        "        (in ?pkg - package)\n" + 
        "    )\n" + 
        ")\n" + 
        "\n" + 
        "";
	
	@Test
	public void addlTest() throws IOException {
		PddlAddl addl = new PddlAddl(new StringReader(addlTest1));
		assertTrue(addl.addlName.name.equals("elevators"));
		assertTrue(addl.domainName.name.equals("elevators"));
		assertTrue(addl.agentsList == null);
		assertTrue(addl.agentTypes != null);
		assertTrue(addl.agentTypes.size() == 1 && addl.agentTypes.get(0).name.equals("elevator"));
		assertTrue(addl.sharedData.size() == 1 && addl.sharedData.containsKey(new PddlName("in")));
		assertTrue(addl.sharedData.get(new PddlName("in")).arguments.size() == 1);
		assertTrue(addl.sharedData.get(new PddlName("in")).arguments.getBindings().containsKey(new PddlName("?pkg")));
		assertTrue(addl.sharedData.get(new PddlName("in")).arguments.getBindings().get(new PddlName("?pkg")).equals(new PddlType("package")));
	}

}
