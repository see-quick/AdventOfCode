package advent.of.code;

import java.util.PriorityQueue;

public class day17 {

    static class Node implements Comparable<Node> {
        int row, col, direction, steps, heatLoss;

        public Node(int row, int col, int direction, int steps, int heatLoss) {
            this.row = row;
            this.col = col;
            this.direction = direction;
            this.steps = steps;
            this.heatLoss = heatLoss;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.heatLoss, other.heatLoss);
        }
    }

    public static int findMinimumHeatLoss(int[][] grid) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(0, 0, -1, 0, 0));

        int minHeatLoss = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // Check if we have reached the destination
            if (current.row == grid.length - 1 && current.col == grid[0].length - 1) {
                minHeatLoss = Math.min(minHeatLoss, current.heatLoss);
                continue;
            }

            // Explore next moves (straight, turn left, turn right)
            // Add valid moves to the queue
        }

        // TODO: this is kinda hard... i don't know :D

        return minHeatLoss;
    }

    public static void main(String[] args) {
        int[][] grid = {
                // Initialize the grid with puzzle input
        };

        int result = findMinimumHeatLoss(grid);
        System.out.println("Minimum Heat Loss: " + result);
    }
}
