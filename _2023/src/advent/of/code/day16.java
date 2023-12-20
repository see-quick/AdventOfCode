package advent.of.code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class day16 {

    public static void main(String[] args) {
        String fileName = "_2023/src/advent/of/code/day16.txt";
        char[][] grid = readGridFromFile(fileName);

        if (grid != null) {
            Set<String> energizedTiles = simulateBeam(grid, 0, 0, 1, 0);
            System.out.println("Number of energized tiles: " + energizedTiles.size());
        } else {
            System.out.println("Error reading grid from file.");
        }
    }

    private static char[][] readGridFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String[] lines = sb.toString().split("\n");
            char[][] grid = new char[lines.length][lines[0].length()];
            for (int i = 0; i < lines.length; i++) {
                grid[i] = lines[i].toCharArray();
            }
            return grid;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Set<String> simulateBeam(char[][] grid, int x, int y, int dx, int dy) {
        Set<String> energizedTiles = new HashSet<>();
        Set<String> visited = new HashSet<>();

        while (x >= 0 && x < grid[0].length && y >= 0 && y < grid.length) {
            String pos = x + "," + y;
            if (visited.contains(pos)) break;
            visited.add(pos);

            energizedTiles.add(pos);
            char tile = grid[y][x];

            switch (tile) {
                case '/':
                    if (dx != 0) {
                        dy = dx;
                        dx = 0;
                    } else {
                        dx = -dy;
                        dy = 0;
                    }
                    break;
                case '\\':
                    if (dx != 0) {
                        dy = -dx;
                        dx = 0;
                    } else {
                        dx = dy;
                        dy = 0;
                    }
                    break;
                case '|':
                case '-':
                    if ((dx == 0 && tile == '|') || (dy == 0 && tile == '-')) {
                        // Split the beam
                        energizedTiles.addAll(simulateBeam(grid, x, y, 0, (dy == 0) ? 1 : dy));
                        energizedTiles.addAll(simulateBeam(grid, x, y, 0, (dy == 0) ? -1 : -dy));
                        return energizedTiles;
                    }
                    break;
            }

            x += dx;
            y += dy;
        }

        return energizedTiles;
    }
}
