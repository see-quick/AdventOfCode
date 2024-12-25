package advent.of.code;

import java.util.ArrayList;
import java.util.List;

public class Day25 {

    public static void main(String[] args) {
        final List<String> input = DataLoader.loadDataFromFile("_2024/src/main/java/advent/of/code/day25.txt");

        // Parse locks and keys
        final List<int[]> locks = new ArrayList<>();
        final List<int[]> keys = new ArrayList<>();

        for (int i = 0; i < input.size(); i += 8) {
            final String[] block = new String[7];

            for (int j = 0; j < 7; j++) {
                block[j] = input.get(i + j);
            }
            // it's lock
            if (block[0].charAt(0) == '#') {
                locks.add(toHeights(block, true));
            } else {
                // it's key
                keys.add(toHeights(block, false));
            }
        }

        long uniquePairs = 0;
        for (int[] lock : locks) {
            for (int[] key : keys) {
                if (fits(lock, key)) uniquePairs++;
            }
        }

        System.out.println("Number of unique lock/key pairs: " + uniquePairs);
    }

    // Checks if lock and key fit (no column overlap)
    static boolean fits(int[] lock, int[] key) {
        // If sum of lock + key in any column > 7, they overlap
        for(int i = 0; i < 5; i++){
            if(lock[i] + key[i] > 7) return false;
        }
        return true;
    }

    /**
     * Converts a 7-row schematic into an array of 5 column heights.
     * For locks, heights are counted from top to bottom until a '.' is found.
     * For keys, heights are counted from bottom to top until a '.' is found.
     *
     * Example:
     *   Lock schematic (top row fully '#', bottom row fully '.'):
     *      #####
     *      .####
     *      .####
     *      .####
     *      .#.#.
     *      .#...
     *      .....
     *   This yields pin heights [0,5,3,4,3].
     *
     *   Key schematic (top row fully '.', bottom row fully '#'):
     *      .....
     *      #....
     *      #....
     *      #...#
     *      #.#.#
     *      #.###
     *      #####
     *  This yields key heights [5,0,2,1,3].
     *
     * @param schematic the 7-line schematic of a lock or key
     * @param isLock true if the schematic is for a lock, false if it is for a key
     * @return an array of 5 column heights
     */
    static int[] toHeights(String[] schematic, boolean isLock) {
        // For locks, '#' columns count from top down; for keys, '#' columns count from bottom up
        int[] heights = new int[5];
        for (int col = 0; col < 5; col++){
            int count = 0;
            if (isLock) {
                for (int row = 0; row < 7; row++){
                    if(schematic[row].charAt(col) == '#') {
                        count++;
                    } else {
                        break; // once '.' is found, stop counting
                    }
                }
            } else {
                for (int row = 6; row >= 0; row--){
                    if (schematic[row].charAt(col) == '#') {
                        count++;
                    } else {
                        break;
                    }
                }
            }
            heights[col] = count;
        }
        return heights;
    }
}
