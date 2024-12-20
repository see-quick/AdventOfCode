package advent.of.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

// Race Condition
public class Day20 {

    // helper class to store point (r, c)
    static class Point {

        private int r;
        private int c;

        Point(int r, int c) {
            this.r = r;
            this.c = c;
        }

        // override equals and hashcode because we use it in the map :))
        public boolean equals(Object o) {
            if(o instanceof Point) {
                Point p = (Point)o;
                return r == p.r && c == p.c;
            }
            return false;
        }
        public int hashCode() { return r * 31 + c;}
    }

    public static void main(String[] args) {
        // Record the start time
        long startTime = System.currentTimeMillis();

        List<String> lines = DataLoader.loadDataFromFile("_2024/src/main/java/advent/of/code/day20.txt");

        char[][] map = new char[lines.size()][];
        for(int i = 0; i < map.length; i++)
            map[i] = lines.get(i).toCharArray();

        // Find S and E
        Point startingPoint = null;
        Point endingPoint = null;
        for(int r=0; r < map.length; r++){
            for(int c=0;c<map[0].length;c++){
                if(map[r][c]=='S') startingPoint = new Point(r,c);
                if(map[r][c]=='E') endingPoint = new Point(r,c);
            }
        }

        // Directions
        final int[][] directions = {
            {1,0},      // down
            {-1,0},     // up
            {0,1},      // right
            {0,-1}      // left
        };

        // BFS to find shortest normal path
        int normalDist = bfs(map, startingPoint, endingPoint, directions);

        // Precompute shortest distances between all track cells and start/end using BFS
        // to avoid recomputing too much.
        // This is large, but we assume the map is small.
        final List<Point> trackCells = new ArrayList<>();
        for(int r = 0; r < map.length; r++){
            for(int c = 0; c < map[0].length; c++){
                if(map[r][c] != '#') trackCells.add(new Point(r,c));
            }
        }

        final Map<Point,Integer> distFromStart = multiBFS(map, directions, startingPoint);
        final Map<Point,Integer> distFromEnd   = multiBFS(map, directions, endingPoint);

        // globalBest: For each cheatStart, store a map of cheatEnd -> minimal steps
        Map<Point, Map<Point,Integer>> globalBest = new HashMap<>();

        for (Point cheatStart : trackCells) {
            Integer distToCheatStart = distFromStart.get(cheatStart);
            if (distToCheatStart == null) continue;

            // Explore and get the best endings for this cheatStart
            Map<Point, Integer> bestEndings = exploreCheatsOptimized(map, directions, cheatStart, distFromEnd, 20);

            // Update globalBest with minimal steps
            Map<Point,Integer> globalForThisStart = globalBest.getOrDefault(cheatStart, new HashMap<>());
            for (Map.Entry<Point, Integer> e : bestEndings.entrySet()) {
                Point cheatEnd = e.getKey();
                int steps = e.getValue();
                int oldBest = globalForThisStart.getOrDefault(cheatEnd, Integer.MAX_VALUE);
                if (steps < oldBest) {
                    globalForThisStart.put(cheatEnd, steps);
                }
            }
            globalBest.put(cheatStart, globalForThisStart);
        }

        // Now calculate savingsCount from globalBest
        Map<Integer,Integer> savingsCount = new HashMap<>();
        for (Map.Entry<Point, Map<Point,Integer>> startEntry : globalBest.entrySet()) {
            Point cheatStart = startEntry.getKey();
            Integer distToCheatStart = distFromStart.get(cheatStart);
            if (distToCheatStart == null) continue; // Should not happen since we checked before

            for (Map.Entry<Point,Integer> endEntry : startEntry.getValue().entrySet()) {
                Point cheatEnd = endEntry.getKey();
                Integer distToEnd = distFromEnd.get(cheatEnd);
                if (distToEnd == null) continue; // unreachable end?

                int cheatSteps = endEntry.getValue();
                int cheatedDist = distToCheatStart + cheatSteps + distToEnd;
                int saving = normalDist - cheatedDist;

                if (saving > 0) {
                    savingsCount.put(saving, savingsCount.getOrDefault(saving, 0) + 1);
                }
            }
        }

        // 269882 - That's not the right answer; your answer is too low
        // 223470 - That's not the right answer; your answer is too low
        // Cheats saving at least 100 picoseconds: 223470
        // Execution time: 347 ms

        // Count how many cheats would save at least 100 picoseconds
        int count = 0;

        for(final Map.Entry<Integer,Integer> e : savingsCount.entrySet()){
            if(e.getKey() >= 100) count += e.getValue();
        }

        System.out.println("Cheats saving at least 100 picoseconds: " + count);

        // Record the end time and calculate execution time
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        // Display execution time
        System.out.println("Execution time: " + executionTime + " ms");
    }

    private static void exploreCheats(
        final char[][] map,
        final int[][] directions,
        final Point cheatStart,
        final int distToCheatStart,
        final Map<Point, Integer> distFromEnd,
        final int normalDist,
        final Map<Integer, Integer> savingsCount,
        final int maxSteps) {

        // BFS to explore up to maxSteps from the cheat start point
        Queue<Point> queue = new LinkedList<>();
        Queue<Integer> stepsQueue = new LinkedList<>();
        queue.add(cheatStart);
        stepsQueue.add(0);

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            int steps = stepsQueue.poll();

            // If we've used up all allowed cheat steps, stop exploring further
            if (steps >= maxSteps) continue;

            for (int[] dir : directions) {
                Point next = new Point(current.r + dir[0], current.c + dir[1]);
                if (!inBounds(map, next)) continue;

                // After cheating, we must land on track
                if (map[next.r][next.c] == '#') {
                    queue.add(next);
                    stepsQueue.add(steps + 1);
                    continue;
                }

                // Valid cheat end point
                Integer distToEnd = distFromEnd.get(next);
                if (distToEnd != null) {
                    int cheatedDist = distToCheatStart + steps + 1 + distToEnd;
                    int saving = normalDist - cheatedDist;

                    if (saving > 0) {
                        savingsCount.put(saving, savingsCount.getOrDefault(saving, 0) + 1);
                    }
                }
            }
        }
    }

    /**
     * Explore cheats up to maxSteps and return a map of best endings.
     * bestEndings maps the end cell to the minimal steps used to reach it.
     */
    private static Map<Point, Integer> exploreCheatsOptimized(
        final char[][] map,
        final int[][] directions,
        final Point cheatStart,
        final Map<Point, Integer> distFromEnd,
        final int maxSteps) {

        Queue<PointState> queue = new LinkedList<>();
        Map<Point, Integer> visited = new HashMap<>();
        // bestEndings: records minimal cheat steps needed to end on track at a given cell
        Map<Point, Integer> bestEndings = new HashMap<>();

        queue.add(new PointState(cheatStart, 0));
        visited.put(cheatStart, 0);

        while (!queue.isEmpty()) {
            PointState currentState = queue.poll();
            Point current = currentState.point;
            int steps = currentState.steps;

            if (steps >= maxSteps) continue;

            for (int[] dir : directions) {
                Point next = new Point(current.r + dir[0], current.c + dir[1]);
                if (!inBounds(map, next)) continue;

                int newSteps = steps + 1;
                if (map[next.r][next.c] == '#') {
                    // Continue through a wall
                    if (newSteps <= maxSteps && visited.getOrDefault(next, Integer.MAX_VALUE) > newSteps) {
                        visited.put(next, newSteps);
                        queue.add(new PointState(next, newSteps));
                    }
                } else {
                    // End on track
                    Integer distToEnd = distFromEnd.get(next);
                    if (distToEnd != null) {
                        // Check if this is a better (fewer steps) route to this end cell
                        if (bestEndings.getOrDefault(next, Integer.MAX_VALUE) > newSteps) {
                            bestEndings.put(next, newSteps);
                        }
                    }
                }
            }
        }

        return bestEndings;
    }

    static class PointState {
        Point point;
        int steps;

        PointState(Point point, int steps) {
            this.point = point;
            this.steps = steps;
        }
    }


    private static boolean inBounds(char[][]map, Point p){
        return p.r >= 0 && p.c >= 0 && p.r < map.length && p.c < map[0].length;
    }

    /**
     * Performs a Breadth-First Search (BFS) to find the shortest path from the start point to the end point on the given map.
     * The BFS considers only valid moves on track cells ('.') and ignores walls ('#').
     *
     * @param map       The 2D character array representing the map, where '.' denotes track, '#' denotes walls,
     *                  'S' is the start, and 'E' is the end.
     * @param start     The starting point of the search.
     * @param end       The target point to reach.
     * @param dirs      A 2D array representing the allowed movement directions (e.g., up, down, left, right).
     * @return          The shortest distance from the start point to the end point.
     *                  Returns {@code Integer.MAX_VALUE} if the end point is unreachable.
     */
    private static int bfs(char[][] map, Point start, Point end, int[][]dirs){
        // Track visited cells
        boolean[][] visited = new boolean[map.length][map[0].length];
        // Queue for BFS, stores points and their distances from the start
        Queue<Point> q = new LinkedList<>();
        Queue<Integer> d = new LinkedList<>();

        // Initialize BFS with the starting point
        q.add(start);
        d.add(0);
        visited[start.r][start.c] = true;

        // Perform BFS
        while (!q.isEmpty()) {
            Point cur = q.poll();
            int dist = d.poll();

            // If the current point is the end point, return the distance
            if (cur.equals(end)) return dist;

            // Explore all valid neighboring cells
            for (int[] dir : dirs) {
                int nr = cur.r + dir[0], nc = cur.c + dir[1];
                if (nr < 0 || nc < 0 || nr >= map.length || nc >= map[0].length) continue; // Out of bounds
                if (map[nr][nc] == '#') continue; // Skip walls
                if (!visited[nr][nc]) {
                    visited[nr][nc] = true;
                    q.add(new Point(nr, nc));
                    d.add(dist + 1);
                }
            }
        }

        // If the end point is unreachable, return a very large value
        return Integer.MAX_VALUE;
    }

    /**
     * Performs a multi-source Breadth-First Search (BFS) to compute the shortest distances
     * from the given start point to all reachable points on the map. The BFS considers only
     * valid moves on track cells ('.') and ignores walls ('#').
     *
     * @param map           The 2D character array representing the map, where '.' denotes track, '#' denotes walls,
     *                      'S' is the start, and 'E' is the end.
     * @param directions    A 2D array representing the allowed movement directions (e.g., up, down, left, right).
     * @param start         The starting point of the BFS.
     * @return              A map where the keys are points on the map and the values are the shortest distances
     *                      from the start point to those points. If a point is unreachable, it is not included in the map.
     */
    static Map<Point,Integer> multiBFS(char[][] map, int[][] directions, Point start){
        // Map to store shortest distances from the start point
        Map<Point, Integer> distances = new HashMap<>();
        // Track visited cells
        boolean[][] visited = new boolean[map.length][map[0].length];
        // Queues for BFS: one for points and one for distances
        Queue<Point> queue = new LinkedList<>();
        Queue<Integer> queueDistances = new LinkedList<>();

        // Initialize BFS with the starting point
        queue.add(start);
        queueDistances.add(0);
        visited[start.r][start.c] = true;
        distances.put(start, 0);

        // Perform BFS
        while (!queue.isEmpty()) {
            Point cur = queue.poll();
            int cd = queueDistances.poll();

            // Explore all valid neighboring cells (i.e., up, down, left and right)...
            for (int[] direction : directions) {
                int nr = cur.r + direction[0], nc = cur.c + direction[1];
                if (nr < 0 || nc < 0 || nr >= map.length || nc >= map[0].length) continue; // Out of bounds
                if (map[nr][nc] == '#') continue; // Skip walls
                if (!visited[nr][nc]) {
                    visited[nr][nc] = true;
                    Point np = new Point(nr, nc);
                    distances.put(np, cd + 1); // Update distance
                    queue.add(np);
                    queueDistances.add(cd + 1);
                }
            }
        }

        return distances;
    }
}
