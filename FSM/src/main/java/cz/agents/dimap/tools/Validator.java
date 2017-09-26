package cz.agents.dimap.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by pah on 15.7.15.
 */
public class Validator {


    public static boolean validate(Plan solution, File domain, File problem) throws IOException, InterruptedException {
        String fileName = "output_plan";
        PrintWriter storage = new PrintWriter(fileName);
        storage.print(solution.getPddlPlan());
        storage.flush();
        storage.close();

        ProcessBuilder processBuilder = new ProcessBuilder("VAL" + File.separator + "validate", domain.getAbsolutePath(), problem.getAbsolutePath(), fileName);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
        Process process = processBuilder.start();
        Scanner scanner = new Scanner(process.getInputStream());
        process.waitFor();
        boolean failed = false;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.isEmpty()) {
                System.out.println(line);
            }
            if (line.contains("Bad plan description!")
                    || line.contains("Failed plans")) {
                failed = true;
            }
        }
        scanner.close();

        return !failed && 0 == process.exitValue();
    }

}
