package org.example.bfs;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Problem:
 *
 * Given a binary grid (0 for passable, 1 for impassable),
 * find the shortest path from the top-left to the bottom-right.
 */
public class ShortestPathInBinaryMatrix {

    private static final int[][] GRID = {
        {0, 1, 0, 0, 0},
        {0, 1, 0, 1, 0},
        {0, 0, 0, 1, 0},
        {1, 1, 0, 1, 0},
        {0, 0, 0, 0, 0}
    };

    public static void main(String[] args) {
        final Queue<int[]> queue = new LinkedList<>();
        final Set<String> visited = new HashSet<>();
        final int rows = GRID.length;
        final int cols = GRID[0].length;

        // starting point
        queue.add(new int[]{0, 0, 1});
        visited.add(0 + "," + 0);
        final int[][] directions = {
            {1, 0},     // right
            {-1, 0},    // left
            {0, 1},     // down
            {0, -1}     // up
        };

        while (!queue.isEmpty()) {
            int[] position = queue.poll();
            int x = position[0];
            int y = position[1];
            int pathLength = position[2];

            // we reach end of the bottom-right
            if (x == rows - 1 && y == cols -1) {
                System.out.println("Path length is :" + pathLength);
                return;
            }

            // 4 directions
            for (int[] direction : directions) {
                int nextX = x + direction[0];
                int nextY = y + direction[1];

                // check that we are still in the grid
                if (nextX < 0 || nextX >= rows || nextY < 0 || nextY >= cols) continue;
                if (!visited.contains(nextX + "," + nextY)) {
                    int nextVal = GRID[nextX][nextY];

                    if (nextVal == 0) {
                        queue.add(new int[]{nextX, nextY, pathLength + 1});
                        visited.add(nextX + "," + nextY);
                    } else {
                        // we hit the wall so nothing is needed
                    }
                }
            }
        }

        System.out.println("We didn't find Path!");
    }
}
