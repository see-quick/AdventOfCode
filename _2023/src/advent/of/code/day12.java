package advent.of.code;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class day12 {

    static class State {
        int index;
        int groupIndex;
        int currentGroupSize;

        State(int index, int groupIndex, int currentGroupSize) {
            this.index = index;
            this.groupIndex = groupIndex;
            this.currentGroupSize = currentGroupSize;
        }
    }

    public static void main(String[] args) {
        String[] input = {
                "???.### 1,1,3",
                ".??..??...?##. 1,1,3",
                "?#?#?#?#?#?#?#? 1,3,1,6",
                "????.#...#... 4,1,1",
                "????.######..#####. 1,6,5",
                "?###???????? 3,2,1"
        };

        int totalArrangements = Arrays.stream(input)
                .mapToInt(day12::countArrangements)
                .sum();

        System.out.println("Total arrangements: " + totalArrangements);
    }

    private static int countArrangements(String row) {
        String[] parts = row.split(" ");
        char[] springs = parts[0].toCharArray();
        int[] groupSizes = Arrays.stream(parts[1].split(","))
                .mapToInt(Integer::parseInt)
                .toArray();

        Queue<State> queue = new LinkedList<>();
        queue.add(new State(0, 0, 0));

        int count = 0;

        while (!queue.isEmpty()) {
            State currentState = queue.poll();

            if (currentState.index == springs.length) {
                if (currentState.groupIndex == groupSizes.length && (currentState.groupIndex == 0 || currentState.currentGroupSize == 0)) {
                    count++;
                }
                continue;
            }

            if (currentState.groupIndex >= groupSizes.length) {
                continue;
            }

            char current = springs[currentState.index];

            if (current == '?') {
                // Replace '?' with operational spring '.'
                if (currentState.currentGroupSize == groupSizes[currentState.groupIndex]) {
                    queue.add(new State(currentState.index + 1, currentState.groupIndex + 1, 0));
                } else {
                    queue.add(new State(currentState.index + 1, currentState.groupIndex, 0));
                }

                // Replace '?' with broken spring '#'
                if (currentState.currentGroupSize < groupSizes[currentState.groupIndex]) {
                    queue.add(new State(currentState.index + 1, currentState.groupIndex, currentState.currentGroupSize + 1));
                }
            } else if (current == '#') {
                // Broken spring
                if (currentState.currentGroupSize < groupSizes[currentState.groupIndex]) {
                    queue.add(new State(currentState.index + 1, currentState.groupIndex, currentState.currentGroupSize + 1));
                } else if (currentState.currentGroupSize == groupSizes[currentState.groupIndex]) {
                    queue.add(new State(currentState.index + 1, currentState.groupIndex + 1, 0));
                }
            } else {
                // Operational spring
                if (currentState.currentGroupSize == groupSizes[currentState.groupIndex]) {
                    queue.add(new State(currentState.index + 1, currentState.groupIndex + 1, 0));
                } else {
                    queue.add(new State(currentState.index + 1, currentState.groupIndex, 0));
                }
            }
        }

        return count;
    }
}
