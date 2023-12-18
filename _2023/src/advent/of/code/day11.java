package advent.of.code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * The CosmicExpansion class solves the puzzle of calculating the sum of the shortest paths
 * between galaxies in a universe that undergoes cosmic expansion.
 * It reads a grid representing the universe from a file, expands the universe according to the
 * given rules, labels each galaxy, and then calculates the total sum of the shortest paths
 * between all pairs of galaxies.
 */
public class day11 {

    private static final char GALAXY = '#';

    /**
     * The main method reads the universe grid from a file, processes it, and prints out the sum
     * of the shortest paths between all pairs of galaxies.
     *
     * @param args Command line arguments (not used).
     * @throws IOException If an error occurs while reading the file.
     */
    public static void main(String[] args) throws IOException {
        List<String> lines = readLinesFromFile("_2023/src/advent/of/code/day11.txt");
        char[][] universe = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            universe[i] = lines.get(i).toCharArray();
        }

        char[][] expandedUniverse = expandUniverse(universe);
        int[][] labeledUniverse = labelGalaxies(expandedUniverse);
        int sumOfPaths = calculateSumOfShortestPaths(labeledUniverse);

        System.out.println("Sum of the shortest paths between all pairs of galaxies: " + sumOfPaths);
    }

    /**
     * Reads the lines from a given file and returns them as a list of strings.
     *
     * @param fileName The name of the file to read from.
     * @return A list of strings, each representing a line in the file.
     * @throws IOException If an error occurs while reading the file.
     */
    private static List<String> readLinesFromFile(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }


    /**
     * Expands the universe by doubling the size of rows and columns that do not contain any galaxies.
     *
     * @param universe The original universe grid.
     * @return The expanded universe grid.
     */
    private static char[][] expandUniverse(char[][] universe) {
        boolean[] emptyRows = new boolean[universe.length];
        boolean[] emptyCols = new boolean[universe[0].length];

        // Identify empty rows and columns
        for (int i = 0; i < universe.length; i++) {
            for (int j = 0; j < universe[i].length; j++) {
                if (universe[i][j] == GALAXY) {
                    emptyRows[i] = true;
                    emptyCols[j] = true;
                }
            }
        }

        // Count new size
        int newRows = 0, newCols = 0;
        for (boolean row : emptyRows) if (!row) newRows += 2; else newRows++;
        for (boolean col : emptyCols) if (!col) newCols += 2; else newCols++;

        // Expand the universe
        char[][] expanded = new char[newRows][newCols];
        int rowIdx = 0;
        for (int i = 0; i < universe.length; i++) {
            int colIdx = 0;
            for (int j = 0; j < universe[i].length; j++) {
                expanded[rowIdx][colIdx] = universe[i][j];
                if (!emptyCols[j]) expanded[rowIdx][colIdx + 1] = universe[i][j];
                colIdx += emptyCols[j] ? 1 : 2;
            }
            if (!emptyRows[i]) System.arraycopy(expanded[rowIdx], 0, expanded[rowIdx + 1], 0, newCols);
            rowIdx += emptyRows[i] ? 1 : 2;
        }
        return expanded;
    }

    /**
     * Labels each galaxy in the expanded universe with a unique number.
     *
     * @param expandedUniverse The expanded universe grid.
     * @return An integer grid with each galaxy labeled with a unique number.
     */
    private static int[][] labelGalaxies(char[][] expandedUniverse) {
        int[][] labeled = new int[expandedUniverse.length][expandedUniverse[0].length];
        int label = 1;
        for (int i = 0; i < expandedUniverse.length; i++) {
            for (int j = 0; j < expandedUniverse[i].length; j++) {
                if (expandedUniverse[i][j] == GALAXY) {
                    labeled[i][j] = label++;
                } else {
                    labeled[i][j] = 0; // 0 for empty space
                }
            }
        }
        return labeled;
    }

    /**
     * Calculates the sum of the shortest paths between every pair of galaxies in the labeled universe.
     *
     * @param labeledUniverse The labeled universe grid.
     * @return The sum of the shortest paths between all pairs of galaxies.
     */
    private static int calculateSumOfShortestPaths(int[][] labeledUniverse) {
        int sum = 0;
        List<int[]> galaxies = new ArrayList<>();

        // Collect coordinates of all galaxies
        for (int i = 0; i < labeledUniverse.length; i++) {
            for (int j = 0; j < labeledUniverse[i].length; j++) {
                if (labeledUniverse[i][j] > 0) {
                    galaxies.add(new int[]{i, j});
                }
            }
        }

        // Calculate shortest paths between each pair of galaxies
        for (int i = 0; i < galaxies.size(); i++) {
            for (int j = i + 1; j < galaxies.size(); j++) {
                sum += bfsShortestPath(labeledUniverse, galaxies.get(i), galaxies.get(j));
            }
        }

        return sum;
    }

    private static int bfsShortestPath(int[][] universe, int[] start, int[] end) {
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        boolean[][] visited = new boolean[universe.length][universe[0].length];
        Queue<int[]> queue = new LinkedList<>();

        queue.add(new int[]{start[0], start[1], 0}); // Include distance in the queue
        visited[start[0]][start[1]] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0], y = current[1], dist = current[2];

            if (x == end[0] && y == end[1]) {
                return dist;
            }

            for (int[] dir : directions) {
                int newX = x + dir[0], newY = y + dir[1];
                if (newX >= 0 && newX < universe.length && newY >= 0 && newY < universe[0].length && !visited[newX][newY]) {
                    visited[newX][newY] = true;
                    queue.add(new int[]{newX, newY, dist + 1});
                }
            }
        }
        return -1; // Should never reach here if path exists
    }
}
