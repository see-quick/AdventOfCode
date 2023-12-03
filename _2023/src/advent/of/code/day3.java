package advent.of.code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * day3: The EngineSchematic class processes a schematic diagram of an engine
 * and calculates the sum of all part numbers that are adjacent to special symbols.
 */
public class day3 {

    /**
     * The main method to run the engine schematic calculation.
     *
     * @param args              Command line arguments (not used).
     * @throws IOException      If an error occurs during file reading.
     */
    public static void main(String[] args) throws IOException {
        List<String> lines = readSchematicFromFile("_2023/src/advent/of/code/day3.txt");
        int sum = calculateSumOfPartNumbers(lines);
        System.out.println("Sum of part numbers: " + sum);
    }

    /**
     * Reads the engine schematic from a file.
     *
     * @param fileName          The name of the file containing the schematic.
     * @return                  A list of strings, each representing a line of the schematic.
     * @throws IOException      If an error occurs during file reading.
     */
    private static List<String> readSchematicFromFile(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * Calculates the sum of part numbers in the schematic.
     * Part numbers are numbers adjacent to certain symbols in the schematic.
     *
     * @param schematic         The engine schematic represented as a list of strings.
     * @return                  The sum of all valid part numbers.
     */
    private static int calculateSumOfPartNumbers(List<String> schematic) {
        int sum = 0;
        for (int i = 0; i < schematic.size(); i++) {
            for (int j = 0; j < schematic.get(i).length(); j++) {
                if (Character.isDigit(schematic.get(i).charAt(j))) {
                    String number = extractNumber(schematic.get(i), j);
                    if (isAdjacentToSymbol(schematic, i, j, number.length())) {
                        sum += Integer.parseInt(number);
                    }
                    j += number.length() - 1; // Skip past the number
                }
            }
        }
        return sum;
    }

    /**
     * Extracts a number starting at a specific position in a string.
     *
     * @param line      The string containing the number.
     * @param start     The starting index of the number in the string.
     * @return          The extracted number as a string.
     */
    private static String extractNumber(String line, int start) {
        StringBuilder number = new StringBuilder();
        while (start < line.length() && Character.isDigit(line.charAt(start))) {
            number.append(line.charAt(start));
            start++;
        }
        return number.toString();
    }

    /**
     * Checks if a number in the schematic is adjacent to a special symbol.
     * Symbols include any non-digit and non-period characters.
     *
     * @param schematic         The engine schematic.
     * @param row               The row index of the starting digit of the number.
     * @param col               The column index of the starting digit of the number.
     * @param length            The length of the number.
     * @return                  true if the number is adjacent to a symbol; false otherwise.
     */
    private static boolean isAdjacentToSymbol(List<String> schematic, int row, int col, int length) {
        int[] dx = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] dy = {-1, 0, 1, 1, 1, 0, -1, -1};
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < length; j++) {
                int newRow = row + dx[i];
                int newCol = col + j + dy[i];
                if (newRow >= 0 && newRow < schematic.size() && newCol >= 0 && newCol < schematic.get(newRow).length()) {
                    char adjacentChar = schematic.get(newRow).charAt(newCol);
                    if (adjacentChar != '.' && !Character.isDigit(adjacentChar)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
