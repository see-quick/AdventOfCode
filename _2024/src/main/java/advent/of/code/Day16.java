package advent.of.code;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * The ReindeerMaze `Day16` solves the problem of finding the lowest score
 * a reindeer can achieve to traverse a maze from the Start ('S') tile
 * to the End ('E') tile. The score is calculated based on movement
 * (1 point per step) and rotation (1000 points per 90-degree turn).
 *
 * The algorithm uses Breadth-First Search (BFS) with a priority queue
 * (Dijkstra's algorithm) to minimize the score.
 */
public class Day16 {

    // Movement vectors for North, East, South, and West directions.
    private static final int[] DR = {-1,0,1,0};  // Change in row for N, E, S, W
    private static final int[] DC = {0,1,0,-1};  // Change in column for N, E, S, W
    private static final int TURN_COST = 1000;
    private static final int MOVE_COST = 1;

    public static void main(String[] args) {
        final List<String> lines = DataLoader.loadDataFromFile("_2024/src/main/java/advent/of/code/day16.txt");

        int H = lines.size();           // Height of the maze
        int W = lines.get(0).length();  // Width of the maze

        // Locate the start ('S') and end ('E') positions
        int sr = 0, sc = 0, er = 0, ec = 0; // Start and end coordinates
        for (int r = 0; r < H; r++) {
            for (int c = 0; c < W; c++) {
                char ch = lines.get(r).charAt(c);
                if (ch == 'S') {
                    sr = r;
                    sc = c;
                }
                if (ch == 'E') {
                    er = r;
                    ec = c;
                }
            }
        }

        // 3D array to store the minimum cost to reach a specific cell
        // with a specific direction (0=N, 1=E, 2=S, 3=W).
        long[][][] dist = new long[H][W][4];
        for (int r = 0; r < H; r++) {
            for (int c = 0; c < W; c++) {
                Arrays.fill(dist[r][c], Long.MAX_VALUE);
            }
        }

        // Initialize the start position, facing East (direction 1)
        dist[sr][sc][1]=0;

        // Priority queue for Dijkstra's algorithm (min-heap)
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a->a[0]));
        pq.add(new long[]{0,sr,sc,1});

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long cd = cur[0]; // Current distance (score)
            int r = (int) cur[1]; // Current row
            int c = (int) cur[2]; // Current column
            int d = (int) cur[3]; // Current direction

            // If this state has already been processed, skip it
            if (cd > dist[r][c][d]) continue;

            // If we reach the end tile ('E'), output the score and terminate
            if (r == er && c == ec) {
                System.out.println(cd);
                return;
            }

            // Explore rotations (left and right turns)
            for (int ndir = -1; ndir <= 1; ndir += 2) {
                int dd = (d + ndir + 4) % 4; // New direction (wrap around 0-3)
                long nd = cd + TURN_COST; // Increment score by turn cost
                if (nd < dist[r][c][dd]) {
                    dist[r][c][dd] = nd;
                    pq.add(new long[]{nd, r, c, dd});
                }
            }

            // Explore moving forward in the current direction
            int nr = r + DR[d]; // New row
            int nc = c + DC[d]; // New column
            // Check boundaries and avoid walls
            if (nr < 0 || nr >= H || nc < 0 || nc >= W || lines.get(nr).charAt(nc) == '#') continue;

            long nd = cd + MOVE_COST; // Increment score by move cost
            if (nd < dist[nr][nc][d]) {
                dist[nr][nc][d] = nd;
                pq.add(new long[]{nd, nr, nc, d});
            }
        }
    }
}
