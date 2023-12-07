package advent.of.code;

public class day6 {

    public static void main(String[] args) {
        // Updated race data
        int[][] raceData = {
                {45, 305},
                {97, 1062},
                {72, 1110},
                {95, 1695}
        };
        long totalWays = 1;

        for (int[] race : raceData) {
            int time = race[0];
            int recordDistance = race[1];
            int waysToWin = calculateWaysToWin(time, recordDistance);
            totalWays *= waysToWin;
        }

        System.out.println("Total ways to beat the records: " + totalWays);
    }

    private static int calculateWaysToWin(int totalTime, int recordDistance) {
        int ways = 0;
        for (int holdTime = 0; holdTime < totalTime; holdTime++) {
            int speed = holdTime;
            int travelTime = totalTime - holdTime;
            int distance = speed * travelTime;
            if (distance > recordDistance) {
                ways++;
            }
        }
        return ways;
    }
}
