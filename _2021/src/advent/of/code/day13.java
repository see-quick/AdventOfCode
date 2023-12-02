package advent.of.code;

public class day13 {

    private static int width = Integer.MIN_VALUE;
    private static int height = Integer.MIN_VALUE;

    public static void printMatrix(Character[][] m, int height, int width) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i <= height; i++) {
            stringBuilder.append("[");
            for (int j = 0; j <= width; j++) {
                stringBuilder.append(m[i][j]);
            }
            stringBuilder.append("]");
            stringBuilder.append("\n");
        }
        System.out.println(stringBuilder.toString());
    }

    public static void fillMatrix(Character[][] m, int height, int width) {
        for (int i = 0; i <= height; i++) {
            for (int j = 0; j <= width; j++) {
                m[i][j] = '.';
            }
        }
    }

    public static void main(String[] args) {
        // I.   parse input
        String input = Utils.constructStringFromFile("day13.txt");
        String[] lines = input.split("\n");

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].isEmpty()) break;
            String[] numbers = lines[i].split(",");
            int left = Integer.parseInt(numbers[0]);
            int right = Integer.parseInt(numbers[1]);

            if (width < left) {
                width = left;
            }

            if (height < right) {
                height = right;
            }
        }

        System.out.println(height);
        System.out.println(width);
        // II.  create transcript paper

        // 1st build and fill with default values
        Character[][] transcript = new Character[height + 1][width + 1];

        /*
            [...........]
            [...........]
            [...........]
            [...........]
            [...........]
            [...........]
            [...........]
            [...........]
            [...........]
            [...........]
            [...........]
            [...........]
            [...........]
            [...........]
            [...........]
         */
        fillMatrix(transcript, height, width);

        printMatrix(transcript, height, width);

        // 2nd override some of values
        /*
         * [...#..#..#.]
         * [....#......]
         * [...........]
         * [#..........]
         * [...#....#.#]
         * [...........]
         * [...........]
         * [...........]
         * [...........]
         * [...........]
         * [.#....#.##.]
         * [....#......]
         * [......#...#]
         * [#..........]
         * [#.#........]
         */
        int counterFromFolds = 0;
        for (String line : lines) {
            if (line == null || line.isEmpty()) break;

            String[] numbers = line.split(",");
            int x = Integer.parseInt(numbers[0]);
            int y = Integer.parseInt(numbers[1]);

            // override . -> #
            transcript[y][x] = '#';

            counterFromFolds++;
        }

        printMatrix(transcript, height, width);


        // 3rd algorithm...
        //  i) make '-'

        Character[][] newArray = new Character[0][0];

        for (int i = counterFromFolds; i < lines.length; i++) {
            if (lines[i] == null || lines[i].isEmpty()) continue;

            String axis = lines[i].split(" ")[2];
            System.out.println(axis);

            // split horizontally
            if (axis.split("=")[0].equals("y")) {
                int y = Integer.parseInt(axis.split("=")[1]);

                for (int j = 0; j < transcript[0].length; j++) {
                    transcript[y][j] = '-';
                }

                // ii) make `fold`
                newArray = new Character[(height / 2) + 1][width + 1];

                fillMatrix(newArray, height / 2, width);

                for (int x = 0; x < height; x++) {
                    for (int z = 0; z < width; z++) {
                        if (x > height / 2) {
                            int tempX = height - x;

                            if (transcript[x][z] == '#') {
                                newArray[tempX][z] = '#';
                            }
                        } else {
                            if (transcript[x][z] == '#') {
                                newArray[x][z] = '#';
                            }
                        }
                    }

                }
            }
        }

        // TODO: do it in more recursive way would be great...

        printMatrix(transcript, height, width);
        printMatrix(newArray,height / 2, width);
    }
}
