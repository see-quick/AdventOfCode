package advent.of.code;

import java.util.*;

public class Day21 {

    // Point class to represent positions on keypads
    static class Point {
        int row;
        int col;

        Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        // Override equals and hashCode for proper comparison in HashSets and HashMaps
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point)) return false;
            Point point = (Point) o;
            return row == point.row && col == point.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    // Keypad class to model each keypad's layout
    static class Keypad {
        int rows;
        int cols;
        Map<Character, Point> charToPos;
        Map<Point, Character> posToChar;

        Keypad(int rows, int cols, Map<Character, Point> charToPos) {
            this.rows = rows;
            this.cols = cols;
            this.charToPos = new HashMap<>(charToPos);
            this.posToChar = new HashMap<>();
            for (Map.Entry<Character, Point> entry : charToPos.entrySet()) {
                this.posToChar.put(entry.getValue(), entry.getKey());
            }
        }

        // Get position of a button
        Point getPosition(char button) {
            return charToPos.get(button);
        }

        // Get button at a position
        Character getButton(Point p) {
            return posToChar.get(p);
        }

        // Check if a move in a direction is possible from a given position
        Point move(Point current, String direction) {
            int newRow = current.row;
            int newCol = current.col;
            switch (direction) {
                case "^":
                    newRow -= 1;
                    break;
                case "v":
                    newRow += 1;
                    break;
                case "<":
                    newCol -= 1;
                    break;
                case ">":
                    newCol += 1;
                    break;
                default:
                    return null; // Invalid direction
            }
            // Check bounds
            if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols)
                return null;
            // Check if there's a button at the new position
            Character btn = getButton(new Point(newRow, newCol));
            if (btn == null || btn == ' ') // ' ' represents a gap
                return null;
            return new Point(newRow, newCol);
        }
    }

    // State class for BFS
    static class State {
        Point userPos;
        Point robot1Pos;
        Point robot2Pos;
        Point numericPos;
        int codeIndex;

        State(Point userPos, Point robot1Pos, Point robot2Pos, Point numericPos, int codeIndex) {
            this.userPos = userPos;
            this.robot1Pos = robot1Pos;
            this.robot2Pos = robot2Pos;
            this.numericPos = numericPos;
            this.codeIndex = codeIndex;
        }

        // Override equals and hashCode for proper comparison in HashSets and HashMaps
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof State)) return false;
            State state = (State) o;
            return codeIndex == state.codeIndex &&
                Objects.equals(userPos, state.userPos) &&
                Objects.equals(robot1Pos, state.robot1Pos) &&
                Objects.equals(robot2Pos, state.robot2Pos) &&
                Objects.equals(numericPos, state.numericPos);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userPos, robot1Pos, robot2Pos, numericPos, codeIndex);
        }
    }

    // BFS to find minimal sequence length for a given code
    static int bfs(Keypad userKeypad, Keypad robot1Keypad, Keypad robot2Keypad, Keypad numericKeypad, String code) {
        // Initialize starting positions (all at 'A')
        Point startUser = userKeypad.getPosition('A');
        Point startRobot1 = robot1Keypad.getPosition('A');
        Point startRobot2 = robot2Keypad.getPosition('A');
        Point startNumeric = numericKeypad.getPosition('A');

        // Initialize BFS
        Queue<State> queue = new LinkedList<>();
        Set<State> visited = new HashSet<>();
        State initialState = new State(startUser, startRobot1, startRobot2, startNumeric, 0);
        queue.add(initialState);
        visited.add(initialState);

        // Define possible directional moves
        String[] directions = {"^", "v", "<", ">"};

        while (!queue.isEmpty()) {
            State current = queue.poll();

            // If all characters have been pressed, return the sequence length
            if (current.codeIndex == code.length()) {
                // The number of button presses is the number of 'A's pressed so far plus the directional moves
                // Since each move increments the sequence length by 1
                // To track the sequence length, we need to keep it as a separate parameter
                // However, for simplicity, we'll modify the State to include sequence length
                // Alternatively, we can track it with a separate BFS level
                // To keep it simple, assume sequence length equals the number of steps taken
                // Thus, we need to track sequence length per state

                // To implement this properly, let's adjust the State class to include sequence length
                // But for now, proceed and assume we can track it externally
                // Since we need to return the sequence length, let's redefine the BFS accordingly
                // Thus, break here and handle the sequence length
                // To do this properly, redefine the BFS to track sequence length with each state

                // This requires a small adjustment: include sequence length in the State
                // Alternatively, use a parallel queue to track sequence lengths
                // Let's implement it with a parallel queue

                // Modify BFS to include sequence length
                // To avoid confusion, let's restart the BFS with proper tracking

                // This is an oversight; to correct it, implement BFS with sequence length

                break; // Placeholder
            }

            // Implementing BFS with proper sequence length tracking
        }

        // Placeholder return
        return Integer.MAX_VALUE;
    }

    // Revised BFS with sequence length tracking
    static int bfsWithSequence(Keypad userKeypad, Keypad robot1Keypad, Keypad robot2Keypad, Keypad numericKeypad, String code) {
        // Initialize starting positions (all at 'A')
        Point startUser = userKeypad.getPosition('A');
        Point startRobot1 = robot1Keypad.getPosition('A');
        Point startRobot2 = robot2Keypad.getPosition('A');
        Point startNumeric = numericKeypad.getPosition('A');

        // Initialize BFS
        Queue<State> queue = new LinkedList<>();
        Set<State> visited = new HashSet<>();
        State initialState = new State(startUser, startRobot1, startRobot2, startNumeric, 0);
        queue.add(initialState);
        visited.add(initialState);

        // Initialize sequence length tracking
        int sequenceLength = 0;

        // Define possible directional moves
        String[] directions = {"^", "v", "<", ">"};

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                State current = queue.poll();

                // If all characters have been pressed, return the sequence length
                if (current.codeIndex == code.length()) {
                    return sequenceLength;
                }

                // Try all directional moves
                for (String dir : directions) {
                    // Attempt to move in this direction on all directional keypads
                    Point newUserPos = userKeypad.move(current.userPos, dir);
                    Point newRobot1Pos = robot1Keypad.move(current.robot1Pos, dir);
                    Point newRobot2Pos = robot2Keypad.move(current.robot2Pos, dir);
                    // Attempting to move numeric keypad's arm is not needed, as it's controlled by Robot 2
                    // Thus, we ignore numeric keypad's movement on directional presses

                    // If any move is invalid (null), skip this directional press
                    if (newUserPos == null || newRobot1Pos == null || newRobot2Pos == null) {
                        continue;
                    }

                    // The numeric keypad's arm remains unchanged on directional presses
                    Point newNumericPos = current.numericPos;

                    // Create new state
                    State newState = new State(newUserPos, newRobot1Pos, newRobot2Pos, newNumericPos, current.codeIndex);

                    // If not visited, add to queue
                    if (!visited.contains(newState)) {
                        visited.add(newState);
                        queue.add(newState);
                    }
                }

                // Try pressing 'A'
                // Pressing 'A' on the user's keypad causes the numeric keypad to press its current button
                // So, check if the numeric keypad's current button matches the desired character
                Character currentButton = numericKeypad.getButton(current.numericPos);
                if (currentButton != null && currentButton == code.charAt(current.codeIndex)) {
                    // Press 'A' to press this button
                    // After pressing, the numeric keypad's arm remains the same
                    State newState = new State(current.userPos, current.robot1Pos, current.robot2Pos, current.numericPos, current.codeIndex + 1);
                    if (!visited.contains(newState)) {
                        visited.add(newState);
                        queue.add(newState);
                    }
                }
            }
            // Increment sequence length after processing all states at the current level
            sequenceLength++;
        }

        // If the code cannot be typed, return a large number
        return Integer.MAX_VALUE;
    }

    public static void main(String[] args) {
        // Define Numeric Keypad
        Map<Character, Point> numericButtons = new HashMap<>();
        numericButtons.put('7', new Point(0, 0));
        numericButtons.put('8', new Point(0, 1));
        numericButtons.put('9', new Point(0, 2));
        numericButtons.put('4', new Point(1, 0));
        numericButtons.put('5', new Point(1, 1));
        numericButtons.put('6', new Point(1, 2));
        numericButtons.put('1', new Point(2, 0));
        numericButtons.put('2', new Point(2, 1));
        numericButtons.put('3', new Point(2, 2));
        numericButtons.put('0', new Point(3, 1));
        numericButtons.put('A', new Point(3, 2));
        // Note: (3,0) is a gap represented implicitly by absence in the map

        Keypad numericKeypad = new Keypad(4, 3, numericButtons);

        // Define Directional Keypad (User's, Robot1's, Robot2's)
        Map<Character, Point> directionalButtons = new HashMap<>();
        directionalButtons.put('^', new Point(0, 0));
        directionalButtons.put('A', new Point(0, 1));
        directionalButtons.put('<', new Point(1, 0));
        directionalButtons.put('v', new Point(1, 1));
        directionalButtons.put('>', new Point(1, 2));
        // Note: (0,2) is a gap represented implicitly by absence in the map

        Keypad userKeypad = new Keypad(2, 3, directionalButtons);
        Keypad robot1Keypad = new Keypad(2, 3, directionalButtons);
        Keypad robot2Keypad = new Keypad(2, 3, directionalButtons);

        // Define the list of codes
        List<String> codes = Arrays.asList("029A", "980A", "179A", "456A", "379A");

        // Initialize a map to store precomputed minimal sequence lengths
        // This avoids recomputing for duplicate codes
        Map<String, Integer> codeToSequenceLength = new HashMap<>();

        // Compute complexities
        long totalComplexity = 0;

        for (String code : codes) {
            // Extract numeric part by removing leading zeros and non-numeric characters
            String numericPartStr = code.replaceAll("^0+", "").replaceAll("[^0-9]", "");
            long numericPart = numericPartStr.isEmpty() ? 0 : Long.parseLong(numericPartStr);

            // Check if sequence length is already computed
            int sequenceLength;
            if (codeToSequenceLength.containsKey(code)) {
                sequenceLength = codeToSequenceLength.get(code);
            } else {
                // Compute minimal sequence length using BFS
                sequenceLength = bfsWithSequence(userKeypad, robot1Keypad, robot2Keypad, numericKeypad, code);
                // Store in the map
                codeToSequenceLength.put(code, sequenceLength);
            }

            // Calculate complexity
            long complexity = (long) sequenceLength * numericPart;
            totalComplexity += complexity;

            // Debugging output
            System.out.println("Code: " + code + ", Numeric Part: " + numericPart + ", Sequence Length: " + sequenceLength + ", Complexity: " + complexity);
        }

        // Output the total complexity
        System.out.println("Total Complexity: " + totalComplexity);
    }
}