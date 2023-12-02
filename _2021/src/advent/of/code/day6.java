package advent.of.code;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class day6 {

    public static void main(String[] args) {
        String input = Utils.constructStringFromFile("day6.txt");

        System.out.println(input);

        final int daysCount = 80;
        int howManyEightToAdd = 0;
        List<Integer> numbers = Arrays.stream(input.split(":")[1].trim().split(",")).map(Integer::parseInt).collect(Collectors.toList());

        for (int i = 0; i < daysCount; i++) {
            for (int j = 0; j < numbers.size(); j++) {
                if (numbers.get(j) == 0) {
                    // reset timer -> set to 6
                    numbers.set(j, 6);
                    // spawn another with init 8
                    howManyEightToAdd++;
                } else {
                    // decrement
                    numbers.set(j, numbers.get(j) - 1);
                }
            }
            for (int x = 0; x < howManyEightToAdd; x++) {
                numbers.add(8);
            }
            howManyEightToAdd = 0;
            System.out.println("Day (" + (i+1) + ")");
            System.out.println(numbers);
        }
        // number of fish...
        System.out.println(numbers.size());
    }
}
