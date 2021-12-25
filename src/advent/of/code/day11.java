package advent.of.code;

public class day11 {

    static class ConstraintInteger {

        private int number;
        private boolean blushed;

        public ConstraintInteger(int number) {
            this.number = number;
            this.blushed = false;
        }

        public void increment() {
            if (this.number == 9) {
                this.number = 0;
                this.blushed = true;
            } else {
                this.number++;
            }
        }

        public boolean isBlushed() {
            return blushed;
        }

        @Override
        public String toString() {
            return String.valueOf(number);
        }
    }

    public static void printMatrix(ConstraintInteger[][] m) {
        StringBuilder stringBuilder = new StringBuilder();

        for (ConstraintInteger[] constraintIntegers : m) {
            stringBuilder.append("[");
            for (ConstraintInteger constraintInteger : constraintIntegers) {
                stringBuilder
                    .append("(")
                    .append(constraintInteger.number)
                    .append(",")
                    .append(constraintInteger.blushed)
                    .append(")");
            }
            stringBuilder.append("]");
            stringBuilder.append("\n");
        }

        System.out.println(stringBuilder.toString());
    }

    public static void main(String[] args) {
        String input = Utils.constructStringFromFile("day11.txt");

        System.out.println(input);

        final String[] lines = input.split("\n");

        ConstraintInteger[][] matrix = new ConstraintInteger[lines.length][lines[0].length()];

        // 1st store matrix...
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                matrix[i][j] = new ConstraintInteger(Character.getNumericValue(lines[i].charAt(j)));
            }
        }

        // 2nd algorithm...
        final int steps = 50;
        int numberOfFlashes = 0;

        for (int x = 0; x < steps; x++) {
            for (int i = 0; i < lines.length; i++) {
                for (int j = 0; j < lines[i].length(); j++) {
                    // 4   2   3
                    //  \  |  /
                    // 3  |0|   1
                    //  /  |  \
                    // 2   1   5
                    // -----------
                    // 5     3     4
                    //  \    |    /
                    // 4  -  0  -  2
                    //  /    |    \
                    // 3     2     6
                    // flash magic...
                    if (matrix[i][j].isBlushed()) {
                        if (matrix[i][j].number == 0) {
                            // a) up
                            if (i != 0) {
                                //      i)      up left     \
                                if (j != 0) {
                                    // already zero...
                                    if (matrix[i - 1][j - 1].number != 0) {
                                        matrix[i - 1][j - 1].increment();
                                    }
                                }
                                //      ii)     up up       |
                                if (matrix[i - 1][j].number != 0) {
                                    matrix[i - 1][j].increment();
                                }
                                //      iii)    up right    /
                                if (j != lines[i].length() - 1) {
                                    if (matrix[i - 1][j + 1].number != 0) {
                                        matrix[i - 1][j + 1].increment();
                                    }
                                }
                            }
                            // b) middle
                            //      i)  middle left
                            if (j != 0) {
                                if (matrix[i][j - 1].number != 0) {
                                    matrix[i][j - 1].increment();
                                }
                            }
                            //      ii) middle right
                            if (j != lines[i].length() - 1) {
                                if (matrix[i][j + 1].number != 0) {
                                    matrix[i][j + 1].increment();
                                }
                            }
                            // c) down
                            if (i != lines.length - 1) {
                                //      i)  down left    \
                                if (j != 0) {
                                    if (matrix[i + 1][j - 1].number != 0) {
                                        matrix[i + 1][j - 1].increment();
                                    }
                                }
                                //      ii) down down    |
                                if (matrix[i + 1][j].number != 0) {
                                    matrix[i + 1][j].increment();
                                }
                                //      iii) down right    /
                                if (j != lines[i].length() - 1) {
                                    if (matrix[i + 1][j + 1].number != 0) {
                                        matrix[i + 1][j + 1].increment();
                                    }
                                }
                            }
                        }
                    } else {
                        // also zero
                        // if (matrix[i][j].number <= 9) {
                        matrix[i][j].increment();
                    }
                }
            }
            printMatrix(matrix);
            // compute flashes
            for (int i = 0; i < lines.length; i++) {
                for (int j = 0; j < lines[i].length(); j++) {
                    if (matrix[i][j].number == 0) {
                        numberOfFlashes++;
                    }
                    // reset blushing
                    matrix[i][j].blushed = false;
                }
            }
        }
        // 3rd result
        System.out.println("Result: " + numberOfFlashes);
    }
}
