package advent.of.code;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {

    public static void main(String[] args) {
        final List<String> memory = DataLoader.loadDataFromFile("_2024/src/main/java/advent/of/code/day3_huge.txt");

        System.out.println(getSumOfCorrectMultiInstructions(memory));
    }

    public static int getSumOfCorrectMultiInstructions(List<String> memory) {
        final String regex = "mul\\(\\d+,\\d+\\)";
        final Pattern pattern = Pattern.compile(regex);
        int sumOfValidInstr = 0;

        for (final String eachLineOfMemory : memory) {
            final Matcher matcher = pattern.matcher(eachLineOfMemory);

            while (matcher.find()) {
                final String mulInstruction = matcher.group();
                final String fineTunedInstr = mulInstruction.replaceAll("mul|\\(|\\)", "");
                final String[] operands = fineTunedInstr.split(",");

                sumOfValidInstr += Integer.parseInt(operands[0]) * Integer.parseInt(operands[1]);
            }
        }

        return sumOfValidInstr;
    }
}
