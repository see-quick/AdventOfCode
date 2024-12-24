package advent.of.code;

import java.util.*;
import java.util.stream.Collectors;

public class Day21 {

    private static final Coord KEY_PAD_START = new Coord(2, 3);
    private static final Coord DIR_PAD_START = new Coord(2, 0);

    private static final char[][] KEY_PAD = {
        {'7', '8', '9'},
        {'4', '5', '6'},
        {'1', '2', '3'},
        {'#', '0', 'A'}
    };

    private static final char[][] DIR_PAD = {
        {'#', '^', 'A'},
        {'<', 'v', '>'}
    };

    private static final Map<Character, Coord> DIR_PAD_COORDS = Map.of(
        '^', new Coord(1, 0),
        'A', new Coord(2, 0),
        '<', new Coord(0, 1),
        'v', new Coord(1, 1),
        '>', new Coord(2, 1)
    );

    private static final Map<RoadDepth, Long> MEMOIZED_ROAD_DEPTH = new HashMap<>();

    public static void main(String[] args) {
        List<String> lines = DataLoader.loadDataFromFile("_2024/src/main/java/advent/of/code/day21.txt");
        long result = calculateTotalComplexity(lines, 2);
        System.out.println(result);
    }

    public static long calculateTotalComplexity(List<String> codes, int depth) {
        long totalComplexity = 0;
        for (String code : codes) {
            long bestComplexity = findBestRobotPathComplexity(code, depth);
            totalComplexity += bestComplexity * extractNumericCode(code);
        }
        return totalComplexity;
    }

    private static long findBestRobotPathComplexity(String code, int depth) {
        List<String> robot1Moves = findShortestPaths(code, KEY_PAD_START, KEY_PAD);
        long bestComplexity = Long.MAX_VALUE;
        for (String robot1Move : robot1Moves) {
            long complexity = calculatePathComplexity(robot1Move, depth);
            bestComplexity = Math.min(bestComplexity, complexity);
        }
        return bestComplexity;
    }

    private static int extractNumericCode(String code) {
        return Integer.parseInt(code.substring(0, code.length() - 1));
    }

    private static List<String> findShortestPaths(String code, Coord start, char[][] pad) {
        if (code.isEmpty()) {
            return List.of("");
        }

        char target = code.charAt(0);
        List<String> allPaths = new ArrayList<>();

        for (Path path : calculateShortestPaths(start, target, pad)) {
            List<String> subPaths = findShortestPaths(code.substring(1), path.getEnd(), pad);
            for (String subPath : subPaths) {
                allPaths.add(path.getSteps() + "A" + subPath);
            }
        }

        int minPathLength = allPaths.stream().mapToInt(String::length).min().orElseThrow();
        return allPaths.stream()
            .filter(path -> path.length() == minPathLength)
            .distinct()
            .collect(Collectors.toList());
    }

    private static List<Path> calculateShortestPaths(Coord start, char goal, char[][] pad) {
        if (isButtonValid(start, pad) && getButton(start, pad) == goal) {
            return List.of(new Path(start, ""));
        }

        Queue<Path> queue = new ArrayDeque<>(List.of(new Path(start, "")));
        List<Path> shortestPaths = new ArrayList<>();
        int shortestLength = Integer.MAX_VALUE;

        while (!queue.isEmpty()) {
            Path currentPath = queue.poll();
            for (Coord neighbor : currentPath.getEnd().getNeighbors()) {
                if (isButtonValid(neighbor, pad)) {
                    Path nextPath = new Path(neighbor, currentPath.getSteps() + currentPath.getEnd().getDirection(neighbor));
                    if (getButton(neighbor, pad) == goal) {
                        if (nextPath.getSteps().length() < shortestLength) {
                            shortestLength = nextPath.getSteps().length();
                            shortestPaths.clear();
                            shortestPaths.add(nextPath);
                        } else if (nextPath.getSteps().length() == shortestLength) {
                            shortestPaths.add(nextPath);
                        }
                    } else if (nextPath.getSteps().length() < shortestLength) {
                        queue.add(nextPath);
                    }
                }
            }
        }

        return shortestPaths;
    }

    private static long calculatePathComplexity(String robotMove, int depth) {
        if (depth == 0) {
            return robotMove.length();
        }

        RoadDepth key = new RoadDepth(robotMove, depth);
        if (MEMOIZED_ROAD_DEPTH.containsKey(key)) {
            return MEMOIZED_ROAD_DEPTH.get(key);
        }

        Coord currentPosition = DIR_PAD_START;
        long totalComplexity = 0;

        for (char move : robotMove.toCharArray()) {
            long minComplexity = Long.MAX_VALUE;
            for (String subPath : findRoads(move, currentPosition)) {
                minComplexity = Math.min(minComplexity, calculatePathComplexity(subPath, depth - 1));
            }
            totalComplexity += minComplexity;
            currentPosition = DIR_PAD_COORDS.get(move);
        }

        MEMOIZED_ROAD_DEPTH.put(key, totalComplexity);
        return totalComplexity;
    }

    private static List<String> findRoads(char target, Coord current) {
        return calculateShortestPaths(current, target, DIR_PAD).stream()
            .map(path -> path.getSteps() + "A")
            .toList();
    }

    private static boolean isButtonValid(Coord coord, char[][] pad) {
        return coord.getY() >= 0 && coord.getY() < pad.length && coord.getX() >= 0 && coord.getX() < pad[0].length && pad[coord.getY()][coord.getX()] != '#';
    }

    private static char getButton(Coord coord, char[][] pad) {
        return pad[coord.getY()][coord.getX()];
    }

    static class Coord {
        private final int x;
        private final int y;

        Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int getX() {
            return x;
        }

        int getY() {
            return y;
        }

        List<Coord> getNeighbors() {
            return List.of(
                new Coord(x + 1, y),
                new Coord(x, y + 1),
                new Coord(x - 1, y),
                new Coord(x, y - 1)
            );
        }

        char getDirection(Coord other) {
            if (x < other.getX()) return '>';
            if (x > other.getX()) return '<';
            if (y < other.getY()) return 'v';
            if (y > other.getY()) return '^';
            throw new IllegalArgumentException("Invalid direction: " + this + " to " + other);
        }
    }

    static class Path {
        private final Coord end;
        private final String steps;

        Path(Coord end, String steps) {
            this.end = end;
            this.steps = steps;
        }

        Coord getEnd() {
            return end;
        }

        String getSteps() {
            return steps;
        }
    }

    static class RoadDepth {
        private final String road;
        private final int depth;

        RoadDepth(String road, int depth) {
            this.road = road;
            this.depth = depth;
        }

        @Override
        public int hashCode() {
            return Objects.hash(road, depth);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            RoadDepth other = (RoadDepth) obj;
            return depth == other.depth && Objects.equals(road, other.road);
        }
    }
}