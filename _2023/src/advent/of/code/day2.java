package advent.of.code;

import java.io.*;
import java.util.*;
import java.nio.file.*;

public class day2 {
    public static void main(String[] args) throws IOException {
        Path filePath = Paths.get("/Users/morsak/Documents/Work/AdventOfCode/_2023/src/advent/of/code/day2.txt");
        List<String> lines = Files.readAllLines(filePath);

        Map<Integer, List<CubeCount>> gameData = new HashMap<>();
        for (String line : lines) {
            String[] parts = line.split(": ");
            int gameId = Integer.parseInt(parts[0].split(" ")[1]);
            String[] counts = parts[1].split("; ");

            List<CubeCount> cubeCounts = new ArrayList<>();
            for (String count : counts) {
                String[] cubes = count.split(", ");
                for (String cube : cubes) {
                    String[] cubeInfo = cube.split(" ");
                    cubeCounts.add(new CubeCount(cubeInfo[1], Integer.parseInt(cubeInfo[0])));
                }
            }
            gameData.put(gameId, cubeCounts);
        }

        int redLimit = 12, greenLimit = 13, blueLimit = 14;
        int sumOfPossibleGameIds = 0;

        for (Map.Entry<Integer, List<CubeCount>> entry : gameData.entrySet()) {
            if (isGamePossible(entry.getValue(), redLimit, greenLimit, blueLimit)) {
                sumOfPossibleGameIds += entry.getKey();
            }
        }

        System.out.println("Sum of Possible Game IDs: " + sumOfPossibleGameIds);
    }

    private static boolean isGamePossible(List<CubeCount> cubeCounts, int redLimit, int greenLimit, int blueLimit) {
        int maxRed = 0, maxGreen = 0, maxBlue = 0;

        for (CubeCount count : cubeCounts) {
            if (count.color.equals("red")) {
                maxRed = Math.max(maxRed, count.count);
            } else if (count.color.equals("green")) {
                maxGreen = Math.max(maxGreen, count.count);
            } else if (count.color.equals("blue")) {
                maxBlue = Math.max(maxBlue, count.count);
            }
        }

        return maxRed <= redLimit && maxGreen <= greenLimit && maxBlue <= blueLimit;
    }

    static class CubeCount {
        String color;
        int count;

        CubeCount(String color, int count) {
            this.color = color;
            this.count = count;
        }
    }
}

