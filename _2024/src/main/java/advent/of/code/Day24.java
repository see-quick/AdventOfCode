package advent.of.code;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Day24 {

    public static void main(String[] args) {
        // algorithm design:
        //  1. parse input (into List<String>)
        //          i.  first part pre-computed circuit points
        //          ii. second part gates to be computed
        //  2. for each gate try to compute if we know both values on the left side
        //          i.  if we know both values -> compute and store such result
        //          ii. if we don't know       -> continue with other gate and store this one as `to be computed`
        //  3. after all gates are computed when we have results of all wires/gates
        //  4. we retrieve ordered (only wires/gates starting with `z`) using TreeSet would be best z00, z01, etc.?
        //  5. we build results from z00=0, z01=1, z02=1 etc. in StringBuilder. In this case `110`. (z00 -> 0 index etc.)
        //  6. this binary we translate to decimal and we print such value.
        final List<String> input = DataLoader.loadDataFromFile("_2024/src/main/java/advent/of/code/day24.txt");

        // Parse input into initial wire values and gates
        final Map<String, Integer> wireValues = new HashMap<>();
        List<String> gates = new ArrayList<>();
        boolean parsingGates = false;

        for (String line : input) {
            if (line.trim().isEmpty()) {
                parsingGates = true;
                continue;
            }
            if (!parsingGates) {
                String[] parts = line.split(": ");
                wireValues.put(parts[0], Integer.parseInt(parts[1]));
            } else {
                gates.add(line);
            }
        }

        // Step 2: Process gates until all are resolved
        while (!gates.isEmpty()) {
            List<String> unresolvedGates = new ArrayList<>();
            for (String gate : gates) {
                if (!processGate(gate, wireValues)) {
                    unresolvedGates.add(gate);
                }
            }
            gates = unresolvedGates;
        }

        // Step 3: Collect results for wires starting with "z" in order
        TreeMap<String, Integer> resultWires = new TreeMap<>(Comparator.reverseOrder());
        // z00 as the and etc.
        wireValues.entrySet().stream()
            .filter(e -> e.getKey().startsWith("z"))
            .forEach(entry -> resultWires.put(entry.getKey(), entry.getValue()));

        // Step 4: Build binary result from z wires
        StringBuilder binaryResult = new StringBuilder();
        for (Integer value : resultWires.values()) {
            binaryResult.append(value);
        }

        // Step 5
        BigInteger decimalResult = new BigInteger(binaryResult.toString(), 2);


        // Output the result
        System.out.println("Decimal Result: " + decimalResult);
    }

    private static boolean processGate(final String gate,
                                       final Map<String, Integer> wireValues) {
        final String[] parts = gate.split(" -> ");
        final String outputWire = parts[1];
        final String[] operationParts = parts[0].split(" ");

        if (operationParts.length == 1) {
            // Direct assignment (e.g., "x00 -> z00")
            String inputWire = operationParts[0];
            if (wireValues.containsKey(inputWire)) {
                wireValues.put(outputWire, wireValues.get(inputWire));
                return true;
            }
        } else if (operationParts.length == 3) {
            // Logical operation (e.g., "x00 AND y00 -> z00")
            String wire1 = operationParts[0];
            String operation = operationParts[1];
            String wire2 = operationParts[2];

            if (wireValues.containsKey(wire1) && wireValues.containsKey(wire2)) {
                int operandLeft = wireValues.get(wire1);
                int operandRight = wireValues.get(wire2);
                int result = switch (operation) {
                    case "AND" -> operandLeft & operandRight;
                    case "OR" -> operandLeft | operandRight;
                    case "XOR" -> operandLeft ^ operandRight;
                    default -> throw new IllegalArgumentException("Unknown operation: " + operation);
                };

                wireValues.put(outputWire, result);
                return true;
            }
        }
        return false; // Could not resolve this gate yet
    }
}
