package cz.agents.dimap.tools;

import cz.agents.dimap.tools.sas.Operator;
import cz.agents.dimap.tools.sas.SasFile;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pah on 14.7.15.
 * <p/>
 * Class representing plan.
 */
public class Plan {
    private final boolean isUnsat;
    private List<Operator> plan = new LinkedList<>();

    public Plan() {
        this.isUnsat = false;
        this.plan = new ArrayList<>();
    }

    public Plan(boolean isUnsat) {
        this.isUnsat = isUnsat;
    }

    public Plan(SasFile sas, File planFile) {
        this.isUnsat = false;
        plan = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(planFile));
            fillPlan(br, sas);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillPlan(BufferedReader br, SasFile sas) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.length() == 0 || line.charAt(0) == ';') {
                continue;
            }
            String operatorName = line.substring(1, line.length() - 1);
            Operator operator = sas.getOperators().get(operatorName.trim());
            plan.add(operator);
        }
        br.close();
    }

    public String getSasPlan() {
        return planToString(false);
    }

    private String planToString(boolean makeBracket) {
        StringBuilder sb = new StringBuilder();
        String prefix = makeBracket ? "(" : "";
        String suffix = makeBracket ? ")" : "";
        for (Operator operator : plan) {
            sb.append(prefix).append(operator.getName()).append(suffix).append("\n");
        }
        return sb.toString();
    }

    public String getPddlPlan() {
        return planToString(true);
    }

    public List<Operator> getPlan() {
        return plan;
    }

    public void setPlan(List<Operator> plan) {
        this.plan = plan;
    }

    public int getSize() {
        return plan.size();
    }

    public int getCost() {
        int cost = 0;
        for (Operator operator : plan) {
            cost += operator.getCost();
        }
        return cost;
    }

    public boolean isUnsat() {
        return isUnsat;
    }
    
    @Override
    public String toString() {
        return plan.toString();
    }
}
