package advent.of.code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * This class is designed to calculate the total points from a set of scratchcards.
 * The calculation is based on matching numbers between a set of winning numbers and the user's numbers.
 * The first match on a card is worth one point, and each subsequent match doubles the point value of that card.
 */
public class day4 {

    /**
     * The main method to run the scratch card calculator.
     * It reads the input from a file and sums up the points from each scratchcard.
     *
     * @param args          Command line arguments (not used here).
     */
    public static void main(String[] args) {
        String filePath = "_2023/src/advent/of/code/day4.txt"; // path to your file
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            int totalPoints = 0;

            while (scanner.hasNextLine()) {
                totalPoints += calculateCardPoints(scanner.nextLine());
            }

            System.out.println("Total points: " + totalPoints);
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    /**
     * Calculates the points for a single scratchcard.
     * Points are calculated based on the number of matches between the winning numbers and the user's numbers.
     *
     * @param card          The string representation of a scratchcard, containing winning numbers and user's numbers.
     * @return              The calculated points for the given scratchcard.
     */
    private static int calculateCardPoints(String card) {
        String[] parts = card.split(" \\| ");
        Set<String> winningNumbers = new HashSet<>(Arrays.asList(parts[0].trim().split("\\s+")));
        String[] yourNumbers = parts[1].trim().split("\\s+");
        int points = 0;

        for (String number : yourNumbers) {
            if (winningNumbers.contains(number)) {
                if (points == 0) {
                    points = 1;
                } else {
                    points *= 2;
                }
            }
        }

        return points;
    }
}
