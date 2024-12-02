package advent.of.code;

import java.util.Arrays;
import java.util.List;

public class Day2 {

    public static void main(String[] args) {
        /**
         * So, a report only counts as safe if both of the following are true:
         *
         * The levels are either all increasing or all decreasing.
         * Any two adjacent levels differ by at least one and at most three.
         *
         * In the example above, the reports can be found safe or unsafe by checking those rules:
         *
         * 7 6 4 2 1: Safe because the levels are all decreasing by 1 or 2.
         * 1 2 7 8 9: Unsafe because 2 7 is an increase of 5.
         * 9 7 6 2 1: Unsafe because 6 2 is a decrease of 4.
         * 1 3 2 4 5: Unsafe because 1 3 is increasing but 3 2 is decreasing.
         * 8 6 4 4 1: Unsafe because 4 4 is neither an increase or a decrease.
         * 1 3 6 7 9: Safe because the levels are all increasing by 1, 2, or 3.
         */
        final List<String> reports = DataLoader.loadDataFromFile("_2024/src/main/java/advent/of/code/day2_huge.txt");
        int numberOfSafeReports = reports.size(); // we start with reports.size()

        for (final String report : reports) {
            final Integer[] reportWithLevels = Arrays.stream(report.split(" ")).map(Integer::parseInt).toArray(Integer[]::new);

            // if it's not monotonic then decreasing safe reports and we do not need to traverse more in reports
            if (!isMonotonic(reportWithLevels)) {
                numberOfSafeReports--;
                continue;
            }

            for (int i = 0; i < reportWithLevels.length - 1; i++) {
                int currentLevel = reportWithLevels[i];
                int nextLevel = reportWithLevels[i + 1];

                if (areLevelsSafe(currentLevel, nextLevel)) {
                    // safe we continue going to next levels
                    continue;
                } else {
                    // unsafe we found the first occurrence no need to traverse more
                    // and decrement our counter
                    numberOfSafeReports--;
                    break;
                }
            }
        }

        System.out.println(numberOfSafeReports);
    }

    /**
     *
     *
     * @param currentLevel  current level
     * @param nextLevel     next level
     * @return  true if levels are at most 3 and at least 1 in intervals...
     */
    public static boolean areLevelsSafe(final int currentLevel,
                                        final int nextLevel) {
        return checkValidIntervals(currentLevel, nextLevel);
    }

    public static boolean checkValidIntervals(final int currentLevel,
                                              final int nextLevel) {
        return Math.abs(currentLevel - nextLevel) >= 1 && Math.abs(currentLevel - nextLevel) <= 3;
    }

    public static boolean isMonotonic(Integer[] sequence) {
        if (sequence == null || sequence.length < 2) {
            return true;
        }

        boolean isIncreasing = true;
        boolean isDecreasing = true;

        for (int i = 1; i < sequence.length; i++) {
            if (sequence[i] > sequence[i - 1]) {
                isDecreasing = false; // Not strictly decreasing
            } else if (sequence[i] < sequence[i - 1]) {
                isIncreasing = false; // Not strictly increasing
            }

            if (!isIncreasing && !isDecreasing) {
                return false;
            }
        }
        return true;
    }
}
