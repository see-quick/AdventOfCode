package advent.of.code;

import java.util.ArrayList;
import java.util.List;

public class day9 {

    static class Matrix {

        int[][] matrix;

        public static Matrix load(final String input) {
            final String[] lines = input.split("\n");
            final Matrix matrix = new Matrix(lines.length, lines[0].length());

            for (int i = 0; i < lines.length; i++) {
                for (int j = 0; j < lines[i].length(); j++) {
                    matrix.matrix[i][j] = Character.getNumericValue(lines[i].charAt(j));
                }
            }
            return matrix;
        }

        public Matrix(int rows, int columns) {
            this.matrix = new int[rows][columns];
        }

        public boolean checkIfSmaller(int row, int column) {
            int currentNumber = this.matrix[row][column];

            // 1.check left
            if (column != 0) {
                if (currentNumber >= this.matrix[row][column - 1]) {
                    return false;
                }
            }

            // 2. check up
            if (row != 0) {
                if (currentNumber >= this.matrix[row - 1][column]) {
                    return false;
                }
            }

            // 3. check right
            if (column != this.matrix[0].length - 1) {
                if (currentNumber >= this.matrix[row][column + 1]) {
                    return false;
                }
            }

            // 4. check down
            if (row != this.matrix.length - 1) {
                if (currentNumber >= this.matrix[row + 1][column]) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for (int[] ints : matrix) {
                for (int anInt : ints) {
                    stringBuilder.append(anInt);
                }
                stringBuilder.append("\n");
            }

            return stringBuilder.toString();
        }
    }

    public static void main(String[] args) {
        String input = Utils.constructStringFromFile("day9.txt");
        Matrix matrix = Matrix.load(input);

        List<Integer> riskLevels = new ArrayList<>();

        for (int i = 0; i < matrix.matrix.length; i++) {
            for (int j = 0; j < matrix.matrix[i].length; j++) {
                if (matrix.checkIfSmaller(i, j)) {
                    System.out.println("Smaller is on (i:" + i + ") and (j:" + j + ") - " + matrix.matrix[i][j]);
                    riskLevels.add(matrix.matrix[i][j]);
                }
            }
        }

        for (int i = 0; i < riskLevels.size(); i++) {
            riskLevels.set(i, riskLevels.get(i) + 1);
        }
        System.out.println(riskLevels);

        final int sum = riskLevels.stream().reduce(0, Integer::sum);

        System.out.println(sum);
    }
}
