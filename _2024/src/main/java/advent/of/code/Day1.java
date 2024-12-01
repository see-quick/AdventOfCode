package advent.of.code;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Day1 {
    public static void main(String[] args) {
        final List<String> lines = DataLoader.loadDataFromFile("2024/src/main/java/advent/of/code/day1.txt");

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

            if (left > right) {
                distance += left - right;
            } else if (right > left) {
                distance += right - left;
            } else {
                // numbers are equal not need to do anything more.. ;)
            }
        }


        System.out.println(distance);
    }
}