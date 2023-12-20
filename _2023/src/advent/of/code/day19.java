package advent.of.code;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class day19 {

    static class Workflow {
        String name;
        Map<String, String[]> rules = new HashMap<>();
        String defaultAction = "R"; // Default action if no other rule matches

        Workflow(String name) {
            this.name = name;
        }

        void addRule(String condition, String action) {
            if (condition.equals("A") || condition.equals("R")) {
                defaultAction = condition; // Set default action
            } else {
                rules.put(condition, new String[]{condition, action});
            }
        }

        String getNextWorkflow(Map<String, Integer> part) {
            for (String[] rule : rules.values()) {
                if (evaluateCondition(part, rule[0])) {
                    return rule[1];
                }
            }
            return defaultAction; // Return default action if no other rule matches
        }

        boolean evaluateCondition(Map<String, Integer> part, String condition) {
            if (condition.equals("A") || condition.equals("R")) {
                return false; // These are not conditions but default actions
            }
            String[] parts = condition.split("[:<>]");
            int partValue = part.getOrDefault(parts[0], 0);
            int conditionValue = Integer.parseInt(parts[1]);
            return condition.contains(">") ? partValue > conditionValue : partValue < conditionValue;
        }
    }

    public static void main(String[] args) {
        String input = "px{a<2006:qkq,m>2090:A,rfg}\n"
                + "pv{a>1716:R,A}\n"
                + "lnx{m>1548:A,A}\n"
                + "rfg{s<537:gd,x>2440:R,A}\n"
                + "qs{s>3448:A,lnx}\n"
                + "qkq{x<1416:A,crn}\n"
                + "crn{x>2662:A,R}\n"
                + "in{s<1351:px,qqz}\n"
                + "qqz{s>2770:qs,m<1801:hdj,R}\n"
                + "gd{a>3333:R,R}\n"
                + "hdj{m>838:A,pv}\n"
                + "\n"
                + "{x=787,m=2655,a=1222,s=2876}\n"
                + "{x=1679,m=44,a=2067,s=496}\n"
                + "{x=2036,m=264,a=79,s=2244}\n"
                + "{x=2461,m=1339,a=466,s=291}\n"
                + "{x=2127,m=1623,a=2188,s=1013}";

        Map<String, Workflow> workflows = new HashMap<>();
        Pattern workflowPattern = Pattern.compile("(\\w+)\\{([^}]*)\\}");
        Matcher workflowMatcher = workflowPattern.matcher(input);

        while (workflowMatcher.find()) {
            String workflowName = workflowMatcher.group(1);
            Workflow workflow = new Workflow(workflowName);
            workflows.put(workflowName, workflow);

            String[] rules = workflowMatcher.group(2).split(",");
            for (String rule : rules) {
                String[] parts = rule.split(":");
                if (parts.length == 2) {
                    workflow.addRule(parts[0], parts[1]);
                }
            }
        }

        // Parse the parts
        Pattern partPattern = Pattern.compile("\\{(x=[^,]*,m=[^,]*,a=[^,]*,s=[^,]*)\\}");
        Matcher partMatcher = partPattern.matcher(input);

        int totalRatingSum = 0;
        while (partMatcher.find()) {
            String[] ratings = partMatcher.group(1).split(",");
            Map<String, Integer> part = new HashMap<>();
            for (String rating : ratings) {
                String[] parts = rating.split("=");
                if (parts.length == 2) {
                    part.put(parts[0], Integer.parseInt(parts[1]));
                }
            }

            String currentWorkflow = "in";
            while (true) {
                Workflow workflow = workflows.get(currentWorkflow);
                String next = workflow.getNextWorkflow(part);
                if (next.equals("A")) {
                    totalRatingSum += part.values().stream().mapToInt(Integer::intValue).sum();
                    break;
                } else if (next.equals("R")) {
                    break;
                } else {
                    currentWorkflow = next;
                }
            }
        }

        System.out.println("Total rating sum of accepted parts: " + totalRatingSum);
    }
}
