package advent.of.code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Utils {

    public static String constructStringFromFile(final String fileName, final String path) {
        String result = null;

        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String constructStringFromFile(final String fileName) {
        return constructStringFromFile("src/advent/of/code/" + fileName);
    }
}
