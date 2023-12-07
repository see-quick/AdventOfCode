package advent.of.code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class day5 {

    public static void main(String[] args) {
        String filePath = "_2023/src/advent/of/code/day5.txt";
        try {
            Scanner scanner = new Scanner(new File(filePath));
            long[] seeds = parseSeeds(scanner.nextLine());
            long[][][] maps = new long[6][][]; // Assuming 6 maps in total

            for (int i = 0; i < maps.length; i++) {
                scanner.nextLine(); // Skip the map title line
                maps[i] = parseMap(scanner);
            }

            long lowestLocation = Long.MAX_VALUE;
            for (long seed : seeds) {
                long location = seed;
                for (long[][] map : maps) {
                    location = mapNumber(location, map);
                }
                lowestLocation = Math.min(lowestLocation, location);
            }

            System.out.println("Lowest Location Number: " + lowestLocation);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    private static long[] parseSeeds(String line) {
        return Arrays.stream(line.substring(7).split(" ")).mapToLong(Long::parseLong).toArray();
    }

    private static long[][] parseMap(Scanner scanner) {
        long[][] map = new long[100][]; // Adjust the size based on expected number of lines
        int count = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty() || line.endsWith("map:")) {
                break;
            }
            String[] parts = line.split(" ");
            map[count] = new long[]{Long.parseLong(parts[0]), Long.parseLong(parts[1]), Long.parseLong(parts[2])};
            count++;
        }
        return Arrays.copyOf(map, count);
    }

    private static long mapNumber(long number, long[][] map) {
        for (long[] mapping : map) {
            long destStart = mapping[0];
            long sourceStart = mapping[1];
            long rangeLength = mapping[2];

            if (number >= sourceStart && number < sourceStart + rangeLength) {
                return destStart + (number - sourceStart);
            }
        }
        return number; // If not mapped, return the same number
    }
}

