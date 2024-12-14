package advent.of.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day14 {

    static class Robot {
        int x, y, vx, vy;

        Robot(int x, int y, int vx, int vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        void move(int w, int h){
            this.x = (this.x + this.vx) % w;
            if(x < 0) {
                x += w;
            }
            this.y = (this.y + this.vy) % h;
            if (y < 0)
                y += h;
        }

        @Override
        public String toString() {
            return "Robot{" +
                "x=" + x +
                ", y=" + y +
                ", vx=" + vx +
                ", vy=" + vy +
                '}';
        }
    }


    public static void main(String[] args) {
        // algorithm design
        //     1. parse robots starting position and velocity (i.e., p=0,4 v=3,-3), each line one robot
        //     2. set grid width x, y length (e.g., only 11 tiles wide and 7 tiles tall but for real example 101 tiles wide and 103 tiles tall)
        //          (note: in this we do not need to allocate for grid i.e., 2D array and simply can just hold those axis of robot in class)
        //     3. Init the state of simulation (assign position of robots to grid)
        //     4. Start simulation (while 100 seconds is reached in simulation):
        //            i.) for each robot move based on defined velocity
        //            ii.) note: for each move we have to do numerous checks:
        //                  1. check if nextMove is horizontally/vertically/diagonally
        //                  (e.g., if grid width is 7 long and our robot position is [6,0] and velocity is [3,0]
        //                          then next position is [2,0]. Same applies for vertical and diagonal.
        //                 Initial state:
        //                  ...........
        //                  ...........
        //                  ...........
        //                  ...........
        //                  ..1........
        //                  ...........
        //                  ...........
        //
        //                  After 1 second:
        //                  ...........
        //                  ....1......
        //                  ...........
        //                  ...........
        //                  ...........
        //                  ...........
        //                  ...........
        //
        //                  After 2 seconds:
        //                  ...........
        //                  ...........
        //                  ...........
        //                  ...........
        //                  ...........
        //                  ......1....
        //                  ...........
        //
        //                  After 3 seconds:
        //                  ...........
        //                  ...........
        //                  ........1..
        //                  ...........
        //                  ...........
        //                  ...........
        //                  ...........
        //
        //                  After 4 seconds:
        //                  ...........
        //                  ...........
        //                  ...........
        //                  ...........
        //                  ...........
        //                  ...........
        //                  ..........1
        //
        //                  After 5 seconds:
        //                  ...........
        //                  ...........
        //                  ...........
        //                  .1.........
        //                  ...........
        //                  ...........
        //                  ...........
        //            iii.) increment one second in simulation after we move all robots
        //     5. end of simulation, and we cut the grid middle of X and Y axis.
        //     6. count each quadrant for the robots f.e.,
        //          ..... 2..1.
        //          ..... .....
        //          1.... .....
        //
        //          ..... .....
        //          ...12 .....
        //          .1... 1....
        //        the quadrants contain 1, 3, 4, and 1 robot.
        //    7. Lastly we compute safety factor of these quadrants by multiplication of each quadrant's number.
        //    8. Print out the safety factor!.

        // Sample input
        List<String> lines = DataLoader.loadDataFromFile("_2024/src/main/java/advent/of/code/day14.txt");

        // load lines into POJOs
        List<Robot> robots = new ArrayList<>();
        for (final String line : lines) {
            final String[] arr = line.split(" ");
            final String positions = arr[0].split("=")[1];
            final String velocity = arr[1].split("=")[1];

            final String[] positionNumbers = positions.split(",");
            final String[] velocityNumbers = velocity.split(",");

            final Robot robot = new Robot(
                Integer.parseInt(positionNumbers[0]),   // x
                Integer.parseInt(positionNumbers[1]),   // y
                Integer.parseInt(velocityNumbers[0]),   // velocity x
                Integer.parseInt(velocityNumbers[1]));  // velocity y

            robots.add(robot);
        }

        System.out.println("Number of robots:\n" + robots.size());
        System.out.println(Arrays.toString(robots.toArray()));

//      (this is for day14_mini.txt  final int width=11, height=7, time=100;
        final int width=101, height=103, time=100;

        // Run simulation
        for(int t = 0; t < time; t++){
            for(Robot r: robots) r.move(width, height);
        }

        // Divide into quadrants
        int midX= width / 2, midY= height / 2;
        int[] counts=new int[4];
        for(Robot r: robots){
            // skip robots which are in the middle
            if (r.x == midX || r.y == midY) continue;

            boolean top = r.y < midY, left = r.x < midX;
            if (top && left) {
                counts[0]++;
            } else if (top && !left) {
                counts[1]++;
            }
            else if (!top && left) {
                counts[2]++;
            } else {
                counts[3]++;
            }
        }

        // Compute safety factor
        long safety = 1;
        for (int c : counts) {
            System.out.println("Count for each quadrant: " + c);
            safety *= c; // Remove the zero-to-one substitution
        }
        System.out.println(safety);
    }
}
