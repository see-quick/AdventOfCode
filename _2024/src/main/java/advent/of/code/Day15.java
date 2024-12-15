package advent.of.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day15 {

    public static void main(String[] args) {
        // algorithm design
        //      1. parse the grid
        //      2. locate robot (i.e., @), locate all walls (i.e., #) and all moves rocks... (i.e., O)
        //      3. parse the moves (i.e., <,^,v,>)
        //      4. for move : moves
        //          i. compute robot nextMove there a few conditions to check
        //              ########
        //              #..O.O.#
        //              ##@.O..#
        //              #...O..#
        //              #.#.O..#
        //              #...O..#
        //              #......#
        //              ########
        //
        //              <^^>>>vv<v>>v<<
        //          ii. if `<` and next item is '#' then can't move and we stay (same for other 3 directions)
        //          iii. if `<` and next item is `.` then we simply move robot in that direction (same for other 3 directions)
        //          iv. if `<` and next item is '0' but next next item is '#' we can't move it (same for other 3 directions)
        //          v. if `<` and next time is '0' and also next next item is `.` then we can move both (i.e., rock and itself)
        //          vi. CAREFULL: we also need to check and find for this `"#..O.O.#"` case where we move more rocks in same direction
        //    5. when done with all moves we now compute of a box is equal to 100 times its distance from the top
        //          edge of the map plus its distance from the left edge of the map.
        //          for instance:
        //              So, the box shown below has a distance of 1 from the top edge of the map and
        //              4 from the left edge of the map, resulting in a GPS coordinate of 100 * 1 + 4 = 104.
        //                      #######
        //                      #...O..
        //                      #......
        List<String> lines = DataLoader.loadDataFromFile("_2024/src/main/java/advent/of/code/day15.txt");
        // Parse the grid and moves
        List<String> gridLines = new ArrayList<>();
        StringBuilder movesBuilder = new StringBuilder();

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            if (line.matches("[<^>v]+")) { // Match moves line
                movesBuilder.append(line.trim());
            } else {
                gridLines.add(line.trim());
            }
        }
        final int height = gridLines.size();
        final int width = gridLines.get(0).length();
        char[][] grid = new char[height][width];
        for (int i = 0; i < height; i++) {
            grid[i] = gridLines.get(i).toCharArray();
        }

        // locate robot
        int robotPositionRow = 0, robotPositionColumn = 0;
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                if(grid[i][j]=='@'){
                    robotPositionRow = i;
                    robotPositionColumn = j;
                }
            }
        }

        Map<Character,int[]> dirMap = new HashMap<>();
        dirMap.put('<', new int[]{0,-1});   // -1, 0
        dirMap.put('>', new int[]{0,1});    // 1, 0
        dirMap.put('^', new int[]{-1,0});   // 0, -1
        dirMap.put('v', new int[]{1,0});    // 0, 1

        for(char move : movesBuilder.toString().toCharArray()){
            final int[] direction = dirMap.get(move);
            final int nextRow = robotPositionRow + direction[0];
            final int nextColumn = robotPositionColumn + direction[1];

            if(nextRow < 0 || nextRow >= height || nextColumn < 0 || nextColumn >= width) {
                continue; // out of bounds
            }
            if(grid[nextRow][nextColumn] == '#') {
                continue; // wall
            }
            if(grid[nextRow][nextColumn] == '.'){
                grid[robotPositionRow][robotPositionColumn] = '.';
                grid[nextRow][nextColumn]='@';
                robotPositionRow = nextRow;
                robotPositionColumn = nextColumn;
            } else if (grid[nextRow][nextColumn] == 'O'){
                if(pushRocks(grid, nextRow, nextColumn, direction[0], direction[1], height, width)){
                    // move robot into the first rock position
                    grid[robotPositionRow][robotPositionColumn]='.';
                    grid[nextRow][nextColumn]='@';
                    robotPositionRow = nextRow;
                    robotPositionColumn = nextColumn;
                }
            }

            // for big we do not need to print
//            System.out.println("After `" + move + "` move the grid loooks like:");
//            printGrid(H, W, grid);
        }



        // compute coordinates of boxes
        // 100 * row_distance + column_distance
        List<Integer> coords = new ArrayList<>();
        for(int i=0; i< height; i++){
            for(int j=0; j< width; j++){
                if(grid[i][j] == 'O') coords.add(i*100+j);
            }
        }

        System.out.println("Sum of chors:" + coords.stream().mapToInt(i -> i).sum());
    }

    // Attempt to push a chain of rocks in direction dr,dc.
    // If all can be pushed into '.' cells, do so and return true.
    // If not, return false (no changes).
    private static boolean pushRocks(char[][] grid,
                                     int startRow,
                                     int startColumn,
                                     int directionRow,
                                     int directionColumn,
                                     int height,
                                     int width) {
        // find the chain of consecutive rocks in line
        int r = startRow;
        int c = startColumn;
        List<int[]> chain = new ArrayList<>();
        while (r >= 0 && r < height && c >= 0 && c < width && grid[r][c] == 'O') {
            chain.add(new int[]{r, c});
            r += directionRow;
            c += directionColumn;
        }
        // now r,c is either out of bounds or not 'O'
        // we need that last cell to be '.'
        if (r < 0 || r >= height || c < 0 || c >= width) {
            return false; // can't push out of bounds
        }
        if (grid[r][c] != '.') {
            return false; // must have a '.' to push into
        }

        // if we got here, we can push all rocks forward by one cell
        // push in reverse order to not overwrite
        grid[r][c] = 'O';
        for (int i = chain.size() - 1; i > 0; i--) {
            int[] curr = chain.get(i);
            int[] prev = chain.get(i - 1);
            grid[curr[0]][curr[1]] = grid[prev[0]][prev[1]];
        }
        // the start cell (first rock pos) now becomes '.'
        int[] first = chain.get(0);
        grid[first[0]][first[1]] = '.';
        return true;
    }

    public static void printGrid(int H, int W, char[][] grid) {
        // print final grid
        for(int i=0;i<H;i++){
            for(int j=0;j<W;j++) System.out.print(grid[i][j]);
            System.out.println();
        }
    }
}
