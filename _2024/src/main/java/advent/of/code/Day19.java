package advent.of.code;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This program determines how many towel designs can be constructed
 * using a given set of towel patterns. The designs are represented
 * as strings, and each string can be built by concatenating the given
 * patterns in order.
 */
public class Day19 {

    // TowelDesigner
    public static void main(String[] args) {
        final List<String> lines = DataLoader.loadDataFromFile("_2024/src/main/java/advent/of/code/day19.txt");
        // algorithm design:
        //      1. parse input (i.e., first line possible towel patterns to use after than blank line and then on each line
        //          we would have towels desings, which we would need to build from towel patterns.)
        //              example:
        //                  r, wr, b, g, bwu, rb, gb, br
        //
        //                  brwrr
        //                  bggr
        //                  gbbr
        //                  rrbgbr
        //                  ubwu
        //                  bwurrg
        //                  brgr
        //                  bbrgwb
        //      2. for each towel design:
        //          i.) run backtracking to searh space starting from first towel pattern etc.
        //          ii.) during backtracking we should count which towel desings are feasible to do with current towel pattrerns
        //      3. when done return number of towel designs which are possible to design.
        //
        // so For instance:
        //  In the above example, the designs are possible or impossible as follows:
        //
        //      brwrr can be made with a br towel, then a wr towel, and then finally an r towel.
        //      bggr can be made with a b towel, two g towels, and then an r towel.
        //      gbbr can be made with a gb towel and then a br towel.
        //      rrbgbr can be made with r, rb, g, and br.
        //      ubwu is impossible.
        //      bwurrg can be made with bwu, r, r, and g.
        //      brgr can be made with br, g, and r.
        //      bbrgwb is impossible.
        //
        //      In this example, 6 of the eight designs are possible with the available towel patterns.

        // first non-empty line: patterns
        List<String> patterns = Arrays.stream(lines.get(0).split(","))
            .map(String::trim)
            .collect(Collectors.toList());

        // skip blank line(s), then process designs
        int i = 1;
        while (i < lines.size() && lines.get(i).trim().isEmpty()) i++;

        int count = 0;
        for (; i < lines.size(); i++) {
            String design = lines.get(i).trim();
            if (canFormDesign(design, patterns, new HashMap<>())) count++;
        }

        System.out.println(count);
    }

    /**
     * Determines if a given design can be formed using the available patterns.
     * It uses recursion with memoization technique for optimized solution :).
     *
     * @param design  the towel design string to be checked
     * @param patterns the list of towel patterns
     * @param memo    a map used for memoization to avoid redundant computations
     * @return true if the design can be formed, false otherwise
     */
    static boolean canFormDesign(String design, List<String> patterns, Map<String, Boolean> memo) {
        if (design.isEmpty()) return true;
        if (memo.containsKey(design)) return memo.get(design);

        for (String p : patterns) {
            if (design.startsWith(p) && canFormDesign(design.substring(p.length()), patterns, memo)) {
                memo.put(design, true);
                return true;
            }
        }
        memo.put(design, false);
        return false;
    }

}
