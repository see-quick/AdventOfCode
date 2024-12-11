package advent.of.code;

import java.util.ArrayList;
import java.util.List;

public class Day11 {

    public static void main(String[] args) {

        //  If the stone is engraved with the number 0, it is replaced by a stone engraved with the number 1.
        //  If the stone is engraved with a number that has an even number of digits,
        //      it is replaced by two stones.
        //      The left half of the digits are engraved on the new left stone,
        //      and the right half of the digits are engraved on the new right stone.
        //      (The new numbers don't keep extra leading zeroes: 1000 would become stones 10 and 0.)
        //  If none of the other rules apply, the stone is replaced by a new stone; the old stone's number multiplied by 2024 is engraved on the new stone.
        final String input = new String("773 79858 0 71 213357 2937 1 3998391");

        // If you have an arrangement of five stones engraved with the numbers 0 1 10 99 999 and you blink once, the stones transform as follows:
        //
        // The first stone, 0, becomes a stone marked 1.
        // The second stone, 1, is multiplied by 2024 to become 2024.
        // The third stone, 10, is split into a stone marked 1 followed by a stone marked 0.
        // The fourth stone, 99, is split into two stones marked 9.
        // The fifth stone, 999, is replaced by a stone marked 2021976.
        // So, after blinking once, your five stones would become an arrangement of seven stones engraved with the numbers 1 2024 1 0 9 9 2021976.
        //
        // Here is a longer example:
        //
        // Initial arrangement:
        // 125 17
        //
        // After 1 blink:
        // 253000 1 7
        //
        // After 2 blinks:
        // 253 0 2024 14168
        //
        // After 3 blinks:
        // 512072 1 20 24 28676032
        //
        // After 4 blinks:
        // 512 72 2024 2 0 2 4 2867 6032
        //
        // After 5 blinks:
        // 1036288 7 2 20 24 4048 1 4048 8096 28 67 60 32
        //
        // After 6 blinks:
        // 2097446912 14168 4048 2 0 2 4 40 48 2024 40 48 80 96 2 8 6 7 6 0 3 2

        String resultStones = solve(25, input);
        System.out.println(resultStones);
        System.out.println(resultStones.split(" ").length);
    }

    // algorithm:
    //  make this recursive
    public static String solve(int blinks, String input) {
        // base
        if (blinks <= 0) {
            return input;
        }

        String[] stones = input.split(" ");
        List<String> next = new ArrayList<>();

        for (String stone : stones) {
            if (stone.equals("0")) {
                next.add("1");
            } else if (stone.length() % 2 == 0) {
                final int mid = stone.length() / 2;
                String leftPart = stone.substring(0, mid).replaceFirst("^0+", "");
                if (leftPart.isEmpty()) {
                    leftPart = "0";
                }
                String rightPart = stone.substring(mid).replaceFirst("^0+", "");
                if (rightPart.isEmpty()) {
                    rightPart = "0";
                }
                next.add(leftPart);
                next.add(rightPart);
            } else {
                long value = Long.parseLong(stone);
                value = value * 2024;
                next.add(String.valueOf(value));
            }
        }

        return solve(blinks - 1, String.join(" ", next));
    }
}
