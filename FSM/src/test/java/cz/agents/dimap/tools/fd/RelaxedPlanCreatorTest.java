package cz.agents.dimap.tools.fd;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cz.agents.dimap.data.SatellitesIpc01NotGrounded;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;

public class RelaxedPlanCreatorTest {

    private List<String> relaxedPlan = Arrays.asList(
            "switch_on instrument0 satellite0",
            "turn_to satellite0 star5 phenomenon6",
            "turn_to satellite0 groundstation2 phenomenon6",
            "calibrate satellite0 instrument0 groundstation2",
            "take_image satellite0 star5 instrument0 thermograph0",
            "take_image satellite0 phenomenon6 instrument0 thermograph0",
            "turn_to satellite0 phenomenon4 phenomenon6",
            "take_image satellite0 phenomenon4 instrument0 thermograph0"
            );

    private String[] expectedPublicRelaxedPlan = new String[] {
            "turn_to_satellite0 star5 phenomenon6",
            "turn_to_satellite0 groundstation2 phenomenon6",
            "take_image_satellite0 star5 thermograph0",
            "take_image_satellite0 phenomenon6 thermograph0",
            "turn_to_satellite0 phenomenon4 phenomenon6",
            "take_image_satellite0 phenomenon4 thermograph0"
    };

    @Test
    public void testRelaxedPlanPublicProjection() throws Exception {
        // shared data = take_image, pointing
        PddlProblem problem = SatellitesIpc01NotGrounded.createProblem().getOriginalRelaxedProblem();

        List<String> publicRelaxedPlan = RelaxedPlanCreator.projectRelaxedPlanToPublic(relaxedPlan, problem, Arrays.asList("satellite0"));

        assertEquals(6, publicRelaxedPlan.size());
        
        assertArrayEquals(expectedPublicRelaxedPlan, publicRelaxedPlan.toArray(new String[0]));
    }

    private String[] expectedPublicRelaxedPlanSimple = new String[] {
            "take_image_satellite0 star5 thermograph0",
            "take_image_satellite0 phenomenon6 thermograph0",
            "take_image_satellite0 phenomenon4 thermograph0"
    };

    @Test
    public void testRelaxedPlanPublicProjectionSimpleSharedData() throws Exception {
        // shared data = take_image, pointing
        PddlProblem problem = SatellitesIpc01NotGrounded.createProblem().getOriginalRelaxedProblem();
        
        problem.domain.sharedPredicateNames.remove(new PddlName("pointing"));
        
        List<String> publicRelaxedPlan = RelaxedPlanCreator.projectRelaxedPlanToPublic(relaxedPlan, problem, Arrays.asList("satellite0"));

        assertEquals(3, publicRelaxedPlan.size());
        
        assertArrayEquals(expectedPublicRelaxedPlanSimple, publicRelaxedPlan.toArray(new String[0]));
    }
}
