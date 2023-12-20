package advent.of.code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class day14 {

    public static void main(String[] args) {
        List<String> inputLines = new ArrayList<>();
        String fileName = "_2023/src/advent/of/code/day14.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                inputLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int rows = inputLines.size();
        int cols = inputLines.get(0).length();
        char[][] platform = new char[rows][cols];

        // Parse the input
        for (int i = 0; i < rows; i++) {
            platform[i] = inputLines.get(i).toCharArray();
        }

        // Tilt the platform north
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                if (platform[row][col] == 'O') {
                    int moveRow = row;
                    while (moveRow > 0 && platform[moveRow - 1][col] == '.') {
                        moveRow--;
                    }
                    if (moveRow != row) {
                        platform[moveRow][col] = 'O';
                        platform[row][col] = '.';
                    }
                }
            }
        }

        // Calculate the total load
        int totalLoad = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (platform[row][col] == 'O') {
                    totalLoad += (rows - row);
                }
            }
        }

        System.out.println("Total Load: " + totalLoad);
    }
}
