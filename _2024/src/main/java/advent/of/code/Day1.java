package advent.of.code;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Day1 {
    public static void main(String[] args) {
        final List<String> lines = DataLoader.loadDataFromFile("_2024/src/main/java/advent/of/code/day1.txt");

        List<String> leftSide = new LinkedList<>();
        List<String> rightSide = new LinkedList<>();

        // make two lists
        for (String line : lines) {
            String[] separated = line.split("   ");
            leftSide.add(separated[0]);
            rightSide.add(separated[1]);
        }

        Collections.sort(leftSide);
        Collections.sort(rightSide);

        int distance = 0;

        for (int i = 0; i < 1000; i++) {
            int left = Integer.parseInt(leftSide.get(i));
            int right = Integer.parseInt(rightSide.get(i));

            distance += Math.abs(left - right);
        }

        // 1223326
        System.out.println(distance);
    }
}