package advent.of.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day8 {

    public static void main(String[] args) {
        // # Algorithm design
        //  1. start with locating all same antennas in the grid? each type move the separate list
        //  2. it would be great that for each antenna compute links i.e., euclidean distance so for instance this is
        //          A..
        //          ...
        //          ..A
        //      those are distanced 2 y and 2 x and try for both of them then expand with antidotes
        // 3. and for each antenna, which consist of several links does expand with antidotes so for instance:
        //  a. 1st antenna expanding
        //          ....
        //          #...
        //          .A..
        //          ..A.
        //          ....
        //          ....
        //  b. 2nd antenna expanding
        //          ....
        //          #...
        //          .A..
        //          ..A.
        //          ...#
        //          ....
        // 4. also when expanding we should cover scenario when we out of the grid (this would be basically x, y <... conditionals
        //      i.e., check before access.
        // 5. lastly we should count how many of these '#' are in the grid after all this computation
        char[][] mini_grid = {
            "............".toCharArray(),
            "........0...".toCharArray(),
            ".....0......".toCharArray(),
            ".......0....".toCharArray(),
            "....0.......".toCharArray(),
            "......A.....".toCharArray(),
            "............".toCharArray(),
            "............".toCharArray(),
            "........A...".toCharArray(),
            ".........A..".toCharArray(),
            "............".toCharArray(),
            "............".toCharArray()
        };

        String[] rawGrid = {
            ".........................p........................",
            "......................h....C............M.........",
            "..............................p....U..............",
            "..5..................p............................",
            "..6z...........................................C..",
            "...............c...........zV.....................",
            "...5.....c........................................",
            ".Z.............h........S...z....9................",
            ".O............................9...z........M..C...",
            "..O....5..............................F..M..C.....",
            "..Z.........5.c...............M....V..............",
            "........ZO................q.......................",
            "s...O................h..Uq.....7V...........4.....",
            ".q.g..............c.............p.......4.........",
            "............hZ.............................4G.....",
            "6s...........................U.Q.....3............",
            ".......6.................9.......Q.............3..",
            "....s..D.........................6................",
            ".............................................FL...",
            "..................................................",
            "..g...D.........q.....f.......Q...F....L......7...",
            "...............2.........f.............V.L...4....",
            "...................2.s...................f3......G",
            "....g...........................v......7P.........",
            "..2..g.............d.....v...........P.......1....",
            "..............u.........f.............L........G..",
            ".........l.D....u...............d........o..P.....",
            "..................8...............9..1......o...7.",
            "............l.....................................",
            "...................l...S...........F.......o..U...",
            ".......................u...S......................",
            "..........l....u...............m...........P....G.",
            "......................................1.8.......o.",
            "..................................................",
            "..................v.......S................0......",
            ".............v........d.....1.....................",
            "..................................................",
            "..........D....................................0..",
            "...................m.............H..........0.....",
            "...................................d......0.......",
            "..................................................",
            "....Q.............................................",
            "................................H.................",
            "........................H....................8....",
            "..................................................",
            "..................................................",
            ".........................................8........",
            ".......................H3.........................",
            "............................m.....................",
            "................................m................."
        };

        int rows = rawGrid.length;
        int cols = rawGrid[0].length();
        char[][] grid = new char[rows][cols];

        // Fill grid with input
        for (int i = 0; i < rows; i++) {
            grid[i] = rawGrid[i].toCharArray();
        }

        // 1st attempt: Unique Antinodes: 289 your answer is too high
        // 2nd attempt: Unique Antinodes: 269 your answer is too low.
        // 3rd attempt: Unique Antinodes: 278 That's the right answer!
        System.out.println("Unique Antinodes: " + findAntinodes(grid));
    }

    public static int findAntinodes(char[][] grid) {
        Map<Character, List<int[]>> antennas = new HashMap<>();
        int rows = grid.length, cols = grid[0].length;

        // Step 1: Collect all antennas
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                char cell = grid[x][y];
                if (Character.isLetterOrDigit(cell)) {
                    antennas.computeIfAbsent(cell, k -> new ArrayList<>()).add(new int[]{x, y});
                }
            }
        }

        // Debugging: Print antenna positions
        System.out.println("Antenna positions:");
        antennas.forEach((key, value) -> System.out.println(key + ": " + Arrays.deepToString(value.toArray())));

        // Step 2: Compute antinodes
        Set<String> antinodes = new HashSet<>();
        for (List<int[]> positions : antennas.values()) {
            antinodes.addAll(getAntinodes(new HashSet<>(positions), rows, cols));
        }

        // Debugging: Print all antinodes
        System.out.println("Final Antinode Positions: " + antinodes);

        // Step 3: Return unique antinodes count
        return antinodes.size();
    }

    /** incorrect!! but at least I tried with tihs :D */
    static Set<String> getAntinodesFromPairs(List<int[]> positions, int rows, int cols) {
        Set<String> antinodes = new HashSet<>();

        for (int i = 0; i < positions.size(); i++) {
            int[] antenna1 = positions.get(i);

            for (int j = i + 1; j < positions.size(); j++) {
                int[] antenna2 = positions.get(j);

                // Compute the difference between the antennas
                int dx = antenna2[0] - antenna1[0];
                int dy = antenna2[1] - antenna1[1];

                // Debugging: Print pair information
                System.out.println("Processing pair: " + Arrays.toString(antenna1) + " -> " + Arrays.toString(antenna2));
                System.out.println("dx: " + dx + ", dy: " + dy);

                // Compute antinodes in both directions
                int ax1 = antenna1[0] - dx;
                int ay1 = antenna1[1] - dy;
                int ax2 = antenna1[0] + dx;
                int ay2 = antenna1[1] + dy;

                // Debugging: Print computed antinodes
                System.out.println("Computed antinodes: [" + ax1 + "," + ay1 + "] and [" + ax2 + "," + ay2 + "]");

                // Add valid antinodes to the set
                if (isValid(ax1, ay1, rows, cols)) {
                    System.out.println("Adding valid antinode: [" + ax1 + "," + ay1 + "]");
                    antinodes.add(ax1 + "," + ay1);
                }
                if (isValid(ax2, ay2, rows, cols)) {
                    System.out.println("Adding valid antinode: [" + ax2 + "," + ay2 + "]");
                    antinodes.add(ax2 + "," + ay2);
                }
            }
        }

        return antinodes;
    }

    static Set<String> getAntinodes(Set<int[]> coords, int rows, int cols) {
        Set<String> antinodes = new HashSet<>();
        List<int[]> coordList = new ArrayList<>(coords);

        for (int i = 0; i < coordList.size(); i++) {
            for (int j = 0; j < coordList.size(); j++) {
                if (i == j) continue;

                int[] antenna1 = coordList.get(i);
                int[] antenna2 = coordList.get(j);

                // geometric symmetry of antennas
                int x = 2 * antenna1[0] - antenna2[0];
                int y = 2 * antenna1[1] - antenna2[1];

                // Check if within bounds
                if (x >= 0 && x < rows && y >= 0 && y < cols) {
                    antinodes.add(x + "," + y); // Use a string representation to ensure uniqueness
                }
            }
        }
        return antinodes;
    }

    // check boundaries...
    private static boolean isValid(int x, int y, int rows, int cols) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }
}
