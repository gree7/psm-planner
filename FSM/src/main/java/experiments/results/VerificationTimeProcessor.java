package experiments.results;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

public class VerificationTimeProcessor {

    final static int COLUMN_WIDTH = 20;
    
    enum Type {VER_TIME, COMMUNICATION, ITERATIONS}
    
    static Type type = Type.VER_TIME;

    static String FILE_PREFIX = "fmap-results-comm-new-";
    
    static File psm = new File("experiments/results/2014-journal/fmap-results-comm-new-PSM-30m.txt");
    static File psmC = new File("experiments/results/2014-journal/fmap-results-comm-new-PSM-c-30m.txt");
    static File psmS = new File("experiments/results/2014-journal/fmap-results-comm-new-PSM-S-30m.txt");
    static File psmR = new File("experiments/results/2014-journal/fmap-results-comm-new-PSM-r-30m.txt");
//    static File psmSR = new File("experiments/results/2014-journal/fmap-results-comm-new-PSM-Sr-30m.txt");

    static File psmSR = new File("experiments/results/2014-journal/fmap-results-comm-mastrips-PSM-Sr-30m.txt");

    public static void main(String[] args) throws IOException {
        switch (type) {
        case VER_TIME:
            System.out.println("PSM-C");
            processVerTime(psmC);
            System.out.println("PSM-S");
            processVerTime(psmS);
            System.out.println("PSM-Sr");
            processVerTime(psmSR);
            break;
        
        case COMMUNICATION:
            processFiles("communication:", psmS, psmSR);
            break;

        case ITERATIONS:
            processFiles("@", psmS, psmSR);
            break;
        }
    }

    static void processVerTime(File psmFile) throws IOException {
        Scanner scanner = new Scanner(psmFile);
        System.out.printf("%"+COLUMN_WIDTH+"s %"+COLUMN_WIDTH+"s %"+COLUMN_WIDTH+"s %"+COLUMN_WIDTH+"s %"+COLUMN_WIDTH+"s%n", "Problem", "Seq. Time", "Par. Time", "Verification", "Planning");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (!line.contains("sequential")) {
                continue;
            }
            
            String problemName = line.split(" ")[0];

            double seqTime = getValue(line, "sequential:");
            double parTime = getValue(line, "parallel:");
            double agents = getValue(line, "agents:");
            double iters = getValue(line, "@");

            double verTime = getSumOfArray(line, "verification\\[ms\\]:") / 1000.0;
            double planTime = getSumOfArray(line, "planning\\[ms\\]:") / 1000.0;

            double relativeVerTime = verTime / agents / (parTime-iters);
            
            System.out.printf("%-"+COLUMN_WIDTH+"s %"+COLUMN_WIDTH+".2f %"+COLUMN_WIDTH+".2f %"+COLUMN_WIDTH+".2f %"+COLUMN_WIDTH+".3f%n", problemName, seqTime, parTime, verTime, relativeVerTime);
        }
        scanner.close();
    }

    static void processFiles(String valueName, File ... files) throws IOException {
        System.out.printf("%-"+COLUMN_WIDTH+"s", "Problem");
        for (File file : files) {
            System.out.printf("%"+COLUMN_WIDTH+"s ", file.getName().substring(FILE_PREFIX.length()));
        }
        System.out.printf("%n");

        List<Map<String, Integer>> psmProblems = new ArrayList<>();
        for (File file : files) {
            psmProblems.add(extractSumOfValues(file, valueName));
        }

        Set<String> intersectedProblems = getIntersectedProblems(psmProblems); 

        for (Map<String,Integer> psmProblem : psmProblems) {
            psmProblem.keySet().retainAll(intersectedProblems);
        }
        
        List<Map<String, Double>> averages = new ArrayList<Map<String,Double>>();
        for (Map<String, Integer> psmProblem : psmProblems) {
            averages.add(averageOverDomain(psmProblem));
        }      
        
        for (String domain : averages.get(0).keySet()) {
            System.out.printf("%-"+COLUMN_WIDTH+"."+COLUMN_WIDTH+"s ", fixDomainName(domain));
            for (Map<String, Double> averageMap : averages) {
                System.out.printf("%"+COLUMN_WIDTH+".2f ", averageMap.get(domain));
            }
            System.out.printf("%n");
        }
    }

    private static Set<String> getIntersectedProblems(List<Map<String, Integer>> problems) {
        Set<String> intersectedProblems = new HashSet<>(problems.get(0).keySet());
        for (Map<String,Integer> problem : problems) {
            intersectedProblems.retainAll(problem.keySet());
        }
        return intersectedProblems;
    }

    public static String fixDomainName(String domain) {
        domain = domain.replaceAll("ma-", "");
        
        domain = Character.toUpperCase(domain.charAt(0)) + domain.substring(1);
        return "\""+domain+"\"";
    }

    public static Map<String, Integer> extractValues(File file, String prefix) throws IOException {
        Map<String, Integer> iters = new LinkedHashMap<>(); 
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (!line.contains("sequential")) {
                continue;
            }
            
            String problemName = line.split(" ")[0];
            int iterations = (int) getValue(line, prefix);

            iters.put(problemName, iterations);
        }
        scanner.close();
        return iters;
    }

    public static Map<String, Integer> extractSumOfValues(File file, String prefix) throws IOException {
        Map<String, Integer> iters = new LinkedHashMap<>(); 
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (!line.contains("sequential")) {
                continue;
            }
            
            String problemName = line.split(" ")[0];
            int iterations = (int) getSumOfArray(line, prefix);

            iters.put(problemName, iterations);
        }
        scanner.close();
        return iters;
    }

    private static Map<String, Double> averageOverDomain(Map<String, Integer> values) {
        Map<String, Integer> sums = new LinkedHashMap<>(); 
        Map<String, Integer> counts = new LinkedHashMap<>();
        
        for (Entry<String, Integer> entry : values.entrySet()) {
            String domain = entry.getKey().split("\\(")[0];
            if (!sums.containsKey(domain)) {
                sums.put(domain, entry.getValue());
                counts.put(domain, 1);
            } else {
                sums.put(domain, sums.get(domain) + entry.getValue());
                counts.put(domain, counts.get(domain) + 1);
            }
        }
        
        Map<String, Double> averages = new LinkedHashMap<>();
        for (Entry<String, Integer> entry : sums.entrySet()) {
            String domain = entry.getKey();
            int count = counts.get(domain);
            String domainWithCount = domain + " (" + count + ")";
            averages.put(domainWithCount, sums.get(domain) / (double) count);
        }
        return averages;
    }

    private static double getValue(String line, String prefix) {
        String fieldStr = line.replaceFirst(".*"+prefix +" ", "");
        fieldStr = fieldStr.replaceFirst("[s ].*", "");
        return Double.parseDouble(fieldStr);
    }

    private static int getSumOfArray(String line, String prefix) {
        String fieldArray = line.replaceFirst(".*"+prefix +" ", "");
        if (fieldArray.charAt(0) == '[') {
            fieldArray = fieldArray.substring(1);
            fieldArray = fieldArray.replaceFirst("\\].*", "");
            String[] fieldSplit = fieldArray.split("[, ]");
            int sum = 0;
            for (String field : fieldSplit) {
                if (!field.isEmpty()) {
                    int number = Integer.parseInt(field);
                    sum += number;
                }
            }
            return sum;
        } else {
            fieldArray = fieldArray.replaceFirst("[s ].*", "");
            return Integer.parseInt(fieldArray);
        }
    }

}
