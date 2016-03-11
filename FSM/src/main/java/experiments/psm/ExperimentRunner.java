package experiments.psm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cz.agents.dimap.Settings;
import cz.agents.dimap.psm.planner.PlanningResult;
import cz.agents.dimap.tools.fd.Downward;
import cz.agents.dimap.tools.fd.DownwardException;
import cz.agents.dimap.tools.fd.DownwardOutOfMemoryException;

public class ExperimentRunner {

    StringBuffer result = new StringBuffer();
    long timeoutMinutes;
    
    public ExperimentRunner(long timeoutMinutes) {
        this.timeoutMinutes = timeoutMinutes;
    }
    
    public void runExperiment(String problemName, final PlanningTask experimentTask) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            long start = System.currentTimeMillis();
            long expectedEndTime = timeoutMinutes > 0 ? System.currentTimeMillis() + timeoutMinutes * 60 * 1000 : -1;
            expectedEndTime -= 500; // It is better to end threads started by the task bit earlier 
            Collection<Callable<PlanningResult>> tasks = makeCallableTasks(experimentTask, expectedEndTime);
            PlanningResult planningResult;
            if (timeoutMinutes > 0) {
            	planningResult = executor.invokeAny(tasks, timeoutMinutes, TimeUnit.MINUTES);
            }
            else {
            	planningResult = executor.invokeAny(tasks);
            }
            long dur = System.currentTimeMillis() - start;
            if (planningResult != null) {
                result.append( problemName + " @ " + planningResult.iterationCount + " - sequential: " + String.format("%.2f", dur/1000.0) + "s - parallel: " + String.format("%.2f", planningResult.parallelTimeMs/1000.0) + "s | " + planningResult.getDetails() + "\n" );
            } else {
                result.append( problemName + " @ no solution found\n" );
            }
        } catch (TimeoutException e) {
            result.append( problemName + " @ timeout\n" );
            Downward.cleanAllTmpFiles();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            try {
                throw e.getCause();
            } catch (TimeoutException e1) {
                result.append( problemName + " @ timeout\n" );
                Downward.cleanAllTmpFiles();
            } catch (DownwardOutOfMemoryException e1) {
                result.append( problemName + " @ out of memory\n" );
                Downward.cleanAllTmpFiles();
            } catch (DownwardException e1) {
                e1.printStackTrace();
                result.append( problemName + " " +
                		" (" + e.getCause().getMessage() + ")\n" );
            } catch (Throwable e1) {
                result.append( problemName + " @ UNKNOWN ERROR (" + e.getMessage() + ")\n" );
                e1.printStackTrace();
            }
        }
        System.out.println(result);
        
        executor.shutdownNow();
        try {
            if (Settings.VERBOSE) {            
                System.out.println("ExperimentRunner: killing all!");
            }
            Runtime.getRuntime().exec("killall -KILL downward-1");
            Runtime.getRuntime().exec("killall -KILL downward-1-debug");
            Runtime.getRuntime().exec("killall -KILL polystar");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Collection<Callable<PlanningResult>> makeCallableTasks(final PlanningTask experimentTask, final long endTime) {
        Collection<Callable<PlanningResult>> callableTasks = new ArrayList<>();
        callableTasks.add(new Callable<PlanningResult>() {
            @Override
            public PlanningResult call() throws Exception {
                return experimentTask.run(endTime);
            }
        });
        return callableTasks;
    }

    public String getResults() {
        return result.toString();
    }
}
