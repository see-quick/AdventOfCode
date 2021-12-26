package advent.of.code;

import java.util.HashMap;
import java.util.Map;

public class day14 {

    private static Map<String, String> insertionRules = new HashMap<>();
    private static Map<Character, Integer> symbolCount = new HashMap<>();

    public static void main(String[] args) {
        // I.   parse input
        String input = Utils.constructStringFromFile("day14.txt");
//        System.out.println(input);

        String[] lines = input.split("\n");

        for (int i = 2; i < lines.length; i++) {
            String[] rule = lines[i].split("->");

            insertionRules.put(rule[0].trim(), rule[1].trim());
        }
//        System.out.println(insertionRules);

        // II. algorithm... generate
        String polymerTemplate = lines[0];
        StringBuilder generativePolymer = null;

        for (int i = 0; i < 10; i++) {
            generativePolymer = new StringBuilder();
//            System.out.println(polymerTemplate);

            for (int j = 0; j < polymerTemplate.length() - 1; j++) {
                final String polymer = polymerTemplate.substring(j, j + 2);
                final String insertInMid = insertionRules.get(polymer);

                // also include the first otherwise not...
                if (j == 0) {
                    generativePolymer.append(polymer.charAt(0));    // first
                }
                generativePolymer
                    .append(insertInMid)            // mid
                    .append(polymer.charAt(1));     // last
            }
            polymerTemplate = generativePolymer.toString();
        }
//        System.out.println(generativePolymer.toString());

        for (int i = 0; i < generativePolymer.toString().length(); i++) {
            Character c = generativePolymer.toString().charAt(i);
            if (symbolCount.containsKey(c)) {
                int number = symbolCount.get(c);
                symbolCount.put(c, ++number);
            } else {
                symbolCount.put(generativePolymer.toString().charAt(i), 1);
            }
        }

//        System.out.println(symbolCount);

        int[] max = new int[]{Integer.MIN_VALUE};
        int[] min = new int[]{Integer.MAX_VALUE};

        symbolCount.forEach((key, value) -> {
            if (value > max[0]) {
                max[0] = value;
            }
            if (value < min[0]) {
                min[0] = value;
            }
        });

        System.out.println("Max: " + max[0]);
        System.out.println("Min: " + min[0]);
        System.out.println("Result: " + (max[0] - min[0]));
    }
}
