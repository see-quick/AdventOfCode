package advent.of.code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;

public class day10 {

    static class Position {
        int x, y, distance;

        public Position(int x, int y, int distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }
    }

    // Directions: N, E, S, W
    private static final int[] dx = {-1, 0, 1, 0};
    private static final int[] dy = {0, 1, 0, -1};

    public static void main(String[] args) {
        String filePath = "_2023/src/advent/of/code/day10.txt";
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            char[][] grid = parseInput(lines.toArray(new String[0]));
            int maxDistance = findFarthestPointDistance(grid);
            System.out.println("Max distance from starting position: " + maxDistance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static char[][] parseInput(String[] lines) {
        char[][] grid = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            grid[i] = lines[i].toCharArray();
        }
        return grid;
    }

    private static int findFarthestPointDistance(char[][] grid) {
        // Find the starting position 'S'
        int startX = -1, startY = -1;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 'S') {
                    startX = i;
                    startY = j;
                    break;
                }
            }
            if (startX != -1) break;
        }

        boolean[][] visited = new boolean[grid.length][grid[0].length];
        Stack<Position> stack = new Stack<>();
        stack.push(new Position(startX, startY, 0));

        int maxDistance = 0;

        while (!stack.isEmpty()) {
            Position current = stack.pop();

            if (visited[current.x][current.y]) {
                continue;
            }

            visited[current.x][current.y] = true;
            maxDistance = Math.max(maxDistance, current.distance);

            for (int dir = 0; dir < 4; dir++) {
                int nx = current.x + dx[dir];
                int ny = current.y + dy[dir];
                if (isValidMove(grid, current.x, current.y, nx, ny) && !visited[nx][ny]) {
                    stack.push(new Position(nx, ny, current.distance + 1));
                }
            }
        }

        return maxDistance;
    }

    private static boolean isValidMove(char[][] grid, int x, int y, int nx, int ny) {
        if (nx < 0 || ny < 0 || nx >= grid.length || ny >= grid[0].length) {
            return false; // Out of bounds
        }

        char current = grid[x][y];
        char next = grid[nx][ny];

        if (next == '.') {
            return false; // Empty space, can't move
        }

        // Directions: N=0, E=1, S=2, W=3
        int direction = getDirection(x, y, nx, ny);

        switch (current) {
            case '|':
                return direction == 0 || direction == 2; // Vertical pipe
            case '-':
                return direction == 1 || direction == 3; // Horizontal pipe
            case 'L':
                return (direction == 0 || direction == 1); // North or East
            case 'J':
                return (direction == 0 || direction == 3); // North or West
            case '7':
                return (direction == 2 || direction == 3); // South or West
            case 'F':
                return (direction == 2 || direction == 1); // South or East
            case 'S':
                // Starting position can connect to any direction,
                // assuming it is a part of the loop.
                return true;
            default:
                return false; // Unknown pipe type
        }
    }

    private static int getDirection(int x, int y, int nx, int ny) {
        if (nx == x - 1) return 0; // North
        if (ny == y + 1) return 1; // East
        if (nx == x + 1) return 2; // South
        if (ny == y - 1) return 3; // West
        return -1; // Invalid direction
    }
}
