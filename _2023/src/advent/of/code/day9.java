package advent.of.code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class day9 {

    public static void main(String[] args) {
        String filePath = "_2023/src/advent/of/code/day9.txt";
        try {
            long sum = readAndProcessFile(filePath);
            System.out.println("Sum of extrapolated values: " + sum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long readAndProcessFile(String filePath) throws IOException {
        long sum = 0;
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            for (String line : (Iterable<String>) lines::iterator) {
                int[] series = Arrays.stream(line.split(" "))
                        .mapToInt(Integer::parseInt)
                        .toArray();
                sum += extrapolateNextValue(series);
            }
        }
        return sum;
    }

    /**
     * Extrapolates the next value of a given series.
     *
     * @param series The series of numbers.
     * @return The next value of the series.
     */
    private static int extrapolateNextValue(int[] series) {
        List<List<Integer>> sequences = new ArrayList<>();
        sequences.add(toList(series));

        // Generate sequences of differences
        while (true) {
            List<Integer> lastSequence = sequences.get(sequences.size() - 1);
            List<Integer> newSequence = new ArrayList<>();
            for (int i = 1; i < lastSequence.size(); i++) {
                newSequence.add(lastSequence.get(i) - lastSequence.get(i - 1));
            }
            sequences.add(newSequence);
            if (allZeroes(newSequence)) {
                break;
            }
        }

        // Work backward to find the next value
        for (int i = sequences.size() - 2; i >= 0; i--) {
            List<Integer> sequence = sequences.get(i);
            List<Integer> nextSequence = sequences.get(i + 1);
            int lastValue = sequence.get(sequence.size() - 1);
            int difference = nextSequence.size() > 0 ? nextSequence.get(nextSequence.size() - 1) : 0;
            sequence.add(lastValue + difference);
        }

        return sequences.get(0).get(sequences.get(0).size() - 1);
    }

    /**
     * Converts an array to a list.
     *
     * @param array The array to convert.
     * @return A list containing the elements of the array.
     */
    private static List<Integer> toList(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int value : array) {
            list.add(value);
        }
        return list;
    }

    /**
     * Checks if all elements in the list are zero.
     *
     * @param list The list to check.
     * @return True if all elements are zero, false otherwise.
     */
    private static boolean allZeroes(List<Integer> list) {
        for (int num : list) {
            if (num != 0) {
                return false;
            }
        }
        return true;
    }
}
