package advent.of.code;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class day18 {

    // TODO: gg wp...hard pathfinding

    public static void main(String[] args) {
        String filePath = "_2023/src/advent/of/code/day18.txt";

        Set<Point> trench;
        try {
            trench = createTrench(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int volume = calculateVolume(trench);
        System.out.println("Lagoon Volume: " + volume + " cubic meters");
    }

    private static Set<Point> createTrench(String filePath) throws IOException {
        Set<Point> trench = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int x = 0, y = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                char direction = parts[0].charAt(0);
                int distance = Integer.parseInt(parts[1]);
                for (int i = 0; i < distance; i++) {
                    switch (direction) {
                        case 'R': x++; break;
                        case 'L': x--; break;
                        case 'U': y--; break;
                        case 'D': y++; break;
                    }
                    trench.add(new Point(x, y));
                }
            }
        }
        return trench;
    }

    private static int calculateVolume(Set<Point> trench) {
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        for (Point p : trench) {
            minX = Math.min(minX, p.x);
            maxX = Math.max(maxX, p.x);
            minY = Math.min(minY, p.y);
            maxY = Math.max(maxY, p.y);
        }

        System.out.println("Boundary minX: " + minX + ", maxX: " + maxX);
        System.out.println("Boundary minY: " + minY + ", maxY: " + maxY);

        boolean[][] grid = new boolean[maxY - minY + 3][maxX - minX + 3];
        for (Point p : trench) {
            grid[p.y - minY + 1][p.x - minX + 1] = true;
        }

        printGrid(grid);

        floodFill(grid, 0, 0);

//        printGrid(grid);

        int count = 0;
        for (int y = 1; y < grid.length - 1; y++) {
            for (int x = 1; x < grid[y].length - 1; x++) {
                if (!grid[y][x]) {
                    count++;
                }
            }
        }

        return count;
    }

    private static void floodFill(boolean[][] grid, int startY, int startX) {
        Stack<Point> stack = new Stack<>();
        stack.push(new Point(startX, startY));

        while (!stack.isEmpty()) {
            Point p = stack.pop();
            if (p.x < 0 || p.x >= grid[0].length || p.y < 0 || p.y >= grid.length || grid[p.y][p.x]) {
                continue;
            }
            grid[p.y][p.x] = true;
            stack.push(new Point(p.x + 1, p.y));
            stack.push(new Point(p.x - 1, p.y));
            stack.push(new Point(p.x, p.y + 1));
            stack.push(new Point(p.x, p.y - 1));
        }
    }

    private static void printGrid(boolean[][] grid) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                System.out.print(grid[y][x] ? "#" : ".");
            }
            System.out.println();
        }
        System.out.println();
    }
}
