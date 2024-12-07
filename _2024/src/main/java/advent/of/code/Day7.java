package advent.of.code;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Day7 {

    enum Operator {
        ADD("+"),
        MULTIPLY("*");

        private final String symbol;

        Operator(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    public static void main(String[] args) {
        final List<String> input = DataLoader.loadDataFromFile("_2024/src/main/java/advent/of/code/day7_huge.txt");
        final List<BigDecimal> sumOfCorrectEquations = new ArrayList<>();

        // try the easiest algorithm (starting with all + then changing to *)
        for (final String line : input) {
            final String[] parts = line.split(":");

            final BigDecimal leftPart = new BigDecimal(parts[0]);
            final String rightPart = parts[1];
            String[] numberTokens = rightPart.split(" ");

            List<BigDecimal> numbers = new ArrayList<>();
            for (final String numStr : numberTokens) {
                if (numStr.isEmpty()) continue;
                numbers.add(new BigDecimal(numStr));
            }

            // i want that algorithm would do this
            // 10 19

            // 1. 10 + 19 => 19  X
            // 2. 10 * 19 => 190 A
            // 3. add to list of numbers to sum (190)

            // 81 40 27
            //
            // 1. 81 + 40 + 27 => 148   X
            // 2. 81 + 40 * 27 => 3267  A
            // add to list of numbers to sum (3267)

            //  83: 17 5
            //
            // 1. 17 + 5 => 22 X
            // 2. 17 * 5 => 85 X
            //
            // ignore...
            // Use backtracking to find if any combination matches the target
            if (backtrack(numbers, leftPart, 0, numbers.get(0), new ArrayList<>())) {
                sumOfCorrectEquations.add(leftPart);
            }

            BigDecimal finalSum = sumOfCorrectEquations.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            System.out.println("Sum of Correct Equations: " + finalSum.toPlainString());
        }
    }

    /**
     * Backtracking method to determine if any operator combination results in the target.
     *
     * @param numbers List of numbers.
     * @param target  The target value to achieve.
     * @param index   Current index in the numbers list.
     * @param current The current computed value.
     * @return True if a combination matches the target, else False.
     */
    private static boolean backtrack(List<BigDecimal> numbers, BigDecimal target, int index, BigDecimal current, List<Operator> operatorsUsed) {
        // Base case: If we've reached the last number, check if current value matches the target
        if (index == numbers.size() - 1) {
            return current.compareTo(target) == 0;
        }

        // Move to the next index
        BigDecimal nextNumber = numbers.get(index + 1);

        operatorsUsed.add(Operator.ADD);
        BigDecimal newSum = current.add(nextNumber);
        boolean addResult = backtrack(numbers, target, index + 1, newSum, operatorsUsed);
        operatorsUsed.remove(operatorsUsed.size() - 1); // Backtrack: remove the last operator
        if (addResult) {
            return true;
        }

        operatorsUsed.add(Operator.MULTIPLY);
        BigDecimal newProduct = current.multiply(nextNumber);
        boolean multiplyResult = backtrack(numbers, target, index + 1, newProduct, operatorsUsed);
        operatorsUsed.remove(operatorsUsed.size() - 1); // Backtrack: remove the last operator
        if (multiplyResult) {
            return true;
        }

        // If neither addition nor multiplication leads to the target, backtrack
        return false;
    }

    // TODO: here it would be great to make backtracking algorithm ... :)
    //      or let's start with to enumerate all possible options
    //          for 2 numbers -> 2 options (+, *)
    //          for 3 numbers -> + +, + *, *, +, *, * -> 4 options
    //          for 4 numbers -> 8 options
    //              + + +,
    //              * * *,
    //              -----
    //              * * +
    //              + * *
    //              * + *
    //              + + *
    //              * + +
    //              + * +
    //              -----
    //
}
