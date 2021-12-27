package advent.of.code;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class day15 {

    // Below arrays detail all four possible movements from a cell
    private static int[] row = { 0, 1 };
    private static int[] col = { 1, 0 };

    static class Node {
        // (x, y) represents coordinates of a cell in the matrix
        int x, y;

        // maintain a parent node for printing the final path
        Node parent;

        Node(int x, int y, Node parent)
        {
            this.x = x;
            this.y = y;
            this.parent = parent;
        }

        @Override
        public String toString() {
            return x + ", " + y;
        }
    }

    public static void printMatrix(int[][] m) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int[] ints : m) {
            for (int anInt : ints) {
                stringBuilder.append(anInt);
            }
            stringBuilder.append("\n");
        }

        System.out.println(stringBuilder.toString());
    }

    /**
     * THIS WORKS BUT SLOW :D
     * Recursive greedy algorithm for find short sum path in 2d matrix @code{matrix}.
     * But this is not a good for matrix more than 10x100
     */
    static int minCost(int[][] matrix, int m,
                       int n)
    {
        if (n < 0 || m < 0)
            // already visited...
            return Integer.MAX_VALUE;
        else if (m == 0 && n == 0)
            return matrix[m][n];
        else
            return matrix[m][n] +
                Math.min(
                    minCost(matrix, m, n-1),
                    minCost(matrix, m-1, n));
    }

    // The function returns false if (x, y) is not a valid position
    private static boolean isValid(int x, int y, int N) {
        return (x >= 0 && x < N) && (y >= 0 && y < N);
    }

    // Utility function to find path from source to destination
    private static void findPath(Node node, List<String> path)
    {
        if (node != null) {
            findPath(node.parent, path);
            path.add(node.toString());
        }
    }

    // Find the shortest route in a matrix from source cell (x, y) to
    // destination cell (N-1, N-1)
    /**
     * BFS based algorithm for finding shortest route in a matrix from (@code{x},@code{y}) to (@code{matrix.length - 1},
     * @code{matrix[0].length)}.
     * @param matrix input 2d matrix
     * @param x size
     * @param y size
     * @return list path
     */
    public static List<String> findPath(int[][] matrix, int x, int y)
    {
        // list to store shortest path
        List<String> path = new ArrayList<>();

        // base case
        if (matrix == null || matrix.length == 0) {
            return path;
        }

        // `N × N` matrix
        int N = matrix.length;

        // create a queue and enqueue the first node
        Queue<Node> q = new ArrayDeque<>();
        Node src = new Node(x, y, null);
        q.add(src);

        // set to check if the matrix cell is visited before or not
        Set<String> visited = new HashSet<>();

        String key = src.x + "," + src.y;
        visited.add(key);

        // loop till queue is empty
        while (!q.isEmpty())
        {
            // dequeue front node and process it
            Node curr = q.poll();
            int i = curr.x, j = curr.y;

            // return if the destination is found
            if (i == N - 1 && j == N - 1) {
                findPath(curr, path);
                return path;
            }

            // value of the current cell
            int n = matrix[i][j];

            // check all four possible movements from the current cell
            // and recur for each valid movement
            for (int k = 0; k < row.length; k++)
            {
                // get next position coordinates using the value of the current cell
                x = i + row[k] * n;
                y = j + col[k] * n;

                // check if it is possible to go to the next position
                // from the current position
                if (isValid(x, y, N))
                {
                    // construct the next cell node
                    Node next = new Node(x, y, curr);

                    key = next.x + "," + next.y;

                    // if it isn't visited yet
                    if (!visited.contains(key))
                    {
                        // enqueue it and mark it as visited
                        q.add(next);
                        visited.add(key);
                    }
                }
            }
        }
        // we reach here if the path is not possible
        return path;
    }

    public static void main(String[] args) {
        // I. parse input
        final String input = Utils.constructStringFromFile("day15.txt");
        final String[] lines = input.split("\n");

        System.out.println(input);

        // II. represent game
        int[][] matrix = new int[lines.length][lines[0].length()];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = Character.getNumericValue(lines[i].charAt(j));
            }
        }
        printMatrix(matrix);

        // III. run algorithm (A* or BFS.. and any Graph base algorithm)
//        System.out.println(minCost(matrix, matrix.length - 1, matrix[0].length - 1) - matrix[0][0]);
        List<String> bestPath = findPath(matrix, 0, 0);

        System.out.println(bestPath);

        int[] minSum = new int[]{0};

        bestPath.forEach(axis -> {
            final int x = Integer.parseInt(axis.split(",")[0].trim());
            final int y = Integer.parseInt(axis.split(",")[1].trim());

            minSum[0] += matrix[x][y];
        });

        System.out.println(minSum[0]);
    }
}
