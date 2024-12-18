package advent.of.code;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Day18 {

    public static void main(String[] args) {
        List<String> lines = DataLoader.loadDataFromFile("_2024/src/main/java/advent/of/code/day18.txt");

        // Grid size
        // int n = 7; example part
        int n = 71;
        boolean[][] corrupted = new boolean[n][n];

        // int iterationUntil = 12; example part
        int iterationUntil = 1024;
        for (int i = 0; i < iterationUntil && i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            corrupted[y][x] = true;
        }

        // BFS to find shortest path from (0,0) to (6,6)
        int[][] dirs = {
            {1, 0},   // down
            {-1, 0},  // up
            {0, 1},   // right
            {0, -1}   // left
        };
        boolean[][] visited = new boolean[n][n];
        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{0,0,0});
        visited[0][0] = true;

        while(!q.isEmpty()){
            int[] cur = q.poll();
            int cy = cur[0], cx = cur[1], steps = cur[2];

            if (cy == n - 1 && cx == n - 1) {
                System.out.println(steps);
                return;
            }

            for (int[] d : dirs) {
                int ny = cy + d[0];
                int nx = cx + d[1];
                if (ny >= 0 && ny < n && nx >= 0 && nx < n &&
                    !visited[ny][nx] && !corrupted[ny][nx]) {
                    visited[ny][nx] = true;
                    q.offer(new int[] {ny, nx, steps + 1});
                }
            }
        }

        // If no path found
        System.out.println(-1);
        // 1st try - 146 - That's not the right answer; your answer is too low.
        // 2nd... -> was rigght !!!
    }
}
