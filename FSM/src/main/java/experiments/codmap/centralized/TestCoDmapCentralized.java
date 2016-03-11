package experiments.codmap.centralized;

import java.util.Date;

import cz.agents.dimap.tools.fd.Downward;

public class TestCoDmapCentralized {

    public static final String DIR = "experiments/CoDMAP/depot/pfile5/";
//    public static final String DIR = "experiments/CoDMAP/elevators08/p05/";
//    public static final String DIR = "experiments/CoDMAP/zenotravel/pfile5/";
/*
    public static final String DIR = "experiments/CoDMAP/blocksworld/probBLOCKS-10-0/";
    public static final String DIR = "experiments/CoDMAP/depot/pfile5/";
    public static final String DIR = "experiments/CoDMAP/driverlog/pfile8/";
    public static final String DIR = "experiments/CoDMAP/elevators08/p10/";
    public static final String DIR = "experiments/CoDMAP/logistics00/probLOGISTICS-4-0/";
    public static final String DIR = "experiments/CoDMAP/rovers/p11/";
    public static final String DIR = "experiments/CoDMAP/satellites/p07-pfile7/";
    public static final String DIR = "experiments/CoDMAP/sokoban/p05/";
    public static final String DIR = "experiments/CoDMAP/woodworking08/p04/";
    public static final String DIR = "experiments/CoDMAP/zenotravel/pfile5/";
*/  

//////////////  DOUBLE-CHECK     /////////////
//    public static final String DIR = "experiments/CoDMAP/rovers/p17/";

/*
*/
//////////////     BROKEN     /////////////

//    public static final String DIR = "experiments/CoDMAP/depot/pfile1/";
/*
    public static final String DIR = "experiments/CoDMAP/depot/pfile1/";
*/
    public static void main(String[] args) throws Exception {
        
        Downward.forceCleanAllTmpFiles();
    
        System.out.println("start: " + new Date());

        CoDmapPsmPlanner.main(new String[] {
                            "-C", "-DrS",
//                            "-C", "-rS",
                            DIR,
                            "plan.out"
                            });
        
        System.out.println("end: " + new Date());
    }
}
