package advent.of.code;

import java.math.BigDecimal;
import java.util.List;

public class Day13 {

    // Maximum allowed presses per button
    private static final int MAX_PRESSES = 100;
    private static final int COST_A = 3;
    private static final int COST_B = 1;

    public static void main(String[] args) {
        // it costs 3 tokens to push the A button and 1 token to push the B button.
        // move the claw a specific amount to the:
        //  i. right (along the X axis)
        //  ii. and a specific amount forward (along the Y axis) each time that button is pressed.

        // So, the most prizes you could possibly win is two; the minimum tokens you would have to spend to win all (two) prizes is 480.
        // termination condition: You estimate that each button would need to be pressed no more than 100 times to win a prize.
        List<String> input = DataLoader.loadDataFromFile("_2024/src/main/java/advent/of/code/day13_huge_numbers.txt");

        // algorithm design (recursive or iterative?) -> start with recursive version:
        // UPDATE 1: This ALGORITHM is soooo slow! I can't even find target price for one Puzzle ....
        //       1. parse input as split based on prize metadata -> i.e., each empty line has 3 lines (1. A but, 2. B but, and prize)
        //       2. create a representation of this Puzzle -> Button A, Button B, Prize
        //       3. for each Puzzle:
        //              i. run backtrack algorithm where we would compute the shortest possible
        //                 "path"/cost to reach target positions signature:
        //                  solve(int aButtonPressed, int bButtonPressed, int[] currentPosition, int[] targetPositionPrice, List<Integer> costForSolution)
        //                  or we can possibly use int's for x, y :)
        //              ii. where base condition would be that if we press A and B button more than 100 then
        //                  there is no possible solution. Moreover, if we can't reach targetPositionPrice with A and B button and we
        //                  are over the limit (e.g., currentPosition[60, 100], targetPositionPrice[32, 83]). As we going always up
        //                  then we mark this as not possible to reach target.
        //              iii. otherwise if we reach position we store such solution in the costForSolution list, and we continue to
        //                   explore state space to find most optimal solution if possible.
        //              iv. After whole state space is explored then we compute the best possible ("low cost") path to reach
        //                  target price. That's evaluated as lowest number computed by tokens= (aButtonPressed * 3) + bButtonPressed
        //              v. then we return this number of tokens to win a prize
        //      4. after we compute each Puzzle we now print all tokens to spend to win all possible prizes
        // Update 2:
        //   Algorithm design 2:
        //      1. Since we do not need to track of sequences but only need to minimal cost we can simplify it to
        //      combinations of A's and B's with the respect of 100 presses.

        // Parse input into puzzles
        // Each puzzle consists of 3 lines: Button A, Button B, Prize
        BigDecimal totalTokens = BigDecimal.ZERO;

        for (int i = 0; i < input.size(); i += 4) {
            final BigDecimal[] a = parseButtonLine(input.get(i));
            final BigDecimal[] b = parseButtonLine(input.get(i + 1));
            final BigDecimal[] prize = parsePrizeLine(input.get(i + 2));

            BigDecimal minCost = BigDecimal.valueOf(Long.MAX_VALUE);

            for (int aCount = 0; aCount <= MAX_PRESSES; aCount++) {
                for (int bCount = 0; bCount <= MAX_PRESSES; bCount++) {
                    final BigDecimal x = a[0]
                        .multiply(BigDecimal.valueOf(aCount))
                        .add(b[0]
                            .multiply(BigDecimal.valueOf(bCount)));
                    final BigDecimal y = a[1]
                        .multiply(BigDecimal.valueOf(aCount))
                        .add(b[1]
                            .multiply(BigDecimal.valueOf(bCount)));
                    if (x.equals(prize[0]) && y.equals(prize[1])) {
                        final BigDecimal cost =
                            BigDecimal.valueOf(aCount)
                                .multiply(BigDecimal.valueOf(COST_A))
                                .add(BigDecimal.valueOf(bCount))
                                .multiply(BigDecimal.valueOf(COST_B));
                        if (cost.compareTo(minCost) < 0) minCost = cost;
                    }
                }
            }
            System.out.println("For Puzzle: " + i + " we need " + minCost + " tokens to reach target");

            if (!minCost.equals(Long.MAX_VALUE)) {
                totalTokens.add(minCost);
            }
            // backtrack(0, 0, 0, 0, a[0], a[1], b[0], b[1], prize[0], prize[1]);
        }

        System.out.println("Total tokens to win all prizes: " + totalTokens);
    }

    // we do not need to track sequence ... so no backtracking :)
//    private static void backtrack(int aCount, int bCount, int x, int y,
//                                  int aX, int aY, int bX, int bY,
//                                  int targetX, int targetY) {
//        // Check if reached
//        if (x == targetX && y == targetY) {
//            int cost = aCount * COST_A + bCount * COST_B;
//            if (cost < minCost) {
//                minCost = cost;
//            }
//            return;
//        }
//
//        // Pruning conditions
//        if (aCount > MAX_PRESSES || bCount > MAX_PRESSES) return;
//        if (x > targetX || y > targetY) return;
//        int currentCost = aCount * COST_A + bCount * COST_B;
//        if (currentCost >= minCost) return;
//
//        // Try pressing A
//        backtrack(aCount + 1, bCount, x + aX, y + aY, aX, aY, bX, bY, targetX, targetY);
//
//        // Try pressing B
//        backtrack(aCount, bCount + 1, x + bX, y + bY, aX, aY, bX, bY, targetX, targetY);
//    }

    private static BigDecimal[] parseButtonLine(String line) {
        // e.g. "Button A: X+94, Y+34"
        // Extract increments: After 'X+' until ',' and after 'Y+' until end.
        String[] parts = line.split(",");
        String xPart = parts[0].trim(); // e.g. "Button A: X+94"
        String yPart = parts[1].trim(); // e.g. "Y+34"

        BigDecimal xVal = new BigDecimal(xPart.substring(xPart.indexOf("X+") + 2).trim());
        BigDecimal yVal = new BigDecimal(yPart.substring(yPart.indexOf("Y+") + 2).trim());
        return new BigDecimal[]{xVal, yVal};
    }

    private static BigDecimal[] parsePrizeLine(String line) {
        // e.g. "Prize: X=8400, Y=5400"
        String[] parts = line.split(",");
        String xPart = parts[0].trim();
        String yPart = parts[1].trim();

        BigDecimal xVal = new BigDecimal(xPart.substring(xPart.indexOf("X=") + 2).trim());
        BigDecimal yVal = new BigDecimal(yPart.substring(yPart.indexOf("Y=") + 2).trim());
        return new BigDecimal[]{xVal, yVal};
    }
}
