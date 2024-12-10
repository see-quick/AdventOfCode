package advent.of.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Day10WithBidirectionalSearch {

    public static void main(String[] args) {
        final char[][] mini_input = {
            "89010123".toCharArray(),
            "78121874".toCharArray(),
            "87430965".toCharArray(),
            "96549874".toCharArray(),
            "45678903".toCharArray(),
            "32019012".toCharArray(),
            "01329801".toCharArray(),
            "10456732".toCharArray()
        };

        final char[][] input = {
            "43217654309879876104563234589896761012345656543098901001".toCharArray(),
            "34108903212368123233472105676787851299856567832187812652".toCharArray(),
            "45677814301457014312986345589876940387765678943046543743".toCharArray(),
            "56789325100876525800345276430105432456566589858956769821".toCharArray(),
            "65438456912963436901236187621234501223455410767349874430".toCharArray(),
            "74321067803451107898547093412012982016764323101210143561".toCharArray(),
            "89412210987430210789698892102123673107895214589723652678".toCharArray(),
            "78601521856521345678721743089034543212345605679854781789".toCharArray(),
            "66789430545678934505430654898745672212236765434765690878".toCharArray(),
            "55676545038943213216765989601654981300129870121012345969".toCharArray(),
            "46543216127657804389834808762347690456789983498141456452".toCharArray(),
            "65454307233456934870126712354878765565210012507230987321".toCharArray(),
            "54567898332110125961015645403969234674323215616546576010".toCharArray(),
            "65658983021001876854324106912452128789321308723455678901".toCharArray(),
            "76543212137892965348933217832141089873410419678964987652".toCharArray(),
            "89862901236543501267018363101033210565566566569873788543".toCharArray(),
            "89871876544345652106529854892123521454479877654012699830".toCharArray(),
            "78100703454278743212434763763014672343287778943210581021".toCharArray(),
            "65210212565189858906763212654985785650198961212101432010".toCharArray(),
            "54321056876012567875898708783476698763267890303215678321".toCharArray(),
            "87012567988703489874345679692676543294986725456574329478".toCharArray(),
            "96543498589876530365210189501987650187675216987687610569".toCharArray(),
            "01234567651010921234501076401236501010564307678596789876".toCharArray(),
            "10389830532329854376542345321545692323403018789455430965".toCharArray(),
            "21456721013456765289031234980098783410912129670320121054".toCharArray(),
            "92505432565421010109120345671107654567823451061210120123".toCharArray(),
            "87615443478302341018750134543234541050765962552121234984".toCharArray(),
            "34322342189219650129665234789432132021894873443010965673".toCharArray(),
            "45451056077828743234574343276543032134703210523457876532".toCharArray(),
            "51069987456943104545987650167898749865612309610765454101".toCharArray(),
            "32678678345652210698092156756547056764303458779890363232".toCharArray(),
            "43010589232781306787183045876532178965210569888211274321".toCharArray(),
            "56923432101090458989254234923421369878934678898302989430".toCharArray(),
            "87889211078764567876360143010030450767125986567401276589".toCharArray(),
            "96676305669653478985478652102141341458076803456564345676".toCharArray(),
            "45435434734522780340349760123456232349883712678178737894".toCharArray(),
            "80127821821011091211299854354987101016792103549069016323".toCharArray(),
            "92346940910329654304587121267807652345013401232108925412".toCharArray(),
            "81055432101458765643671010871018947654324589543987432101".toCharArray(),
            "76567789023467010782532346965425638945695678654986540012".toCharArray(),
            "05498654110567821891047897212334721032786014345678901098".toCharArray(),
            "12387013223489932346156598101549889821012823216765212387".toCharArray(),
            "03456323016576542345692367210678710701296954907854323456".toCharArray(),
            "12345465437895431016781450123467623654387867878985401501".toCharArray(),
            "21089870124326528701670101874345634565676541045621032012".toCharArray(),
            "32189210065017019632543210965236730120545632456734548743".toCharArray(),
            "43498349876298903545450143050159821321234012349895699654".toCharArray(),
            "34567658389101232123469052101567634489234510106786789985".toCharArray(),
            "99876501276788943016578769872498105672105621215021058876".toCharArray(),
            "87035432365897654107689898763343234321678789334134567655".toCharArray(),
            "70129641034781089898791099854232145690569245493254321567".toCharArray(),
            "63238701123654178718982387763156056781410126787655010498".toCharArray(),
            "54345652321073265001073456012047189872328901098546710327".toCharArray(),
            "34568543434589874132569895145438976987437812361239891210".toCharArray(),
            "21879654898678013203456701236327805456546521450967890123".toCharArray(),
            "30968745467654320112345210987610112345545430567856543234".toCharArray()
        };

        // algorithm desing:
        //    1 a. locate all trailheads (i.e., `0`) in the map
        //    1 b. locate all ends (i.e., `9`)?
        //
        //    2. for each trailhead find path to the `9`
        //          a) move left, right, up and down (except diagonals)
        //          b) each step control if we are inside map...
        //          c) if we find a path to the 9 then for such trailhead we store +1 score
        //              (i.e., each trailhead has to store also about paths)
        //                  ...0...
        //                  ...1...
        //                  ...2...
        //                  6543456
        //                  7.....7
        //                  8.....8
        //                  9.....9
        //              this trailhead has score of 2.
        //          d) WARNING!: we need to take care of a multiple same paths for instance
        //                 ..90..9
        //                 ...1.98
        //                 ...2..7
        //                 6543456
        //                 765.987
        //                 876....
        //                 987....
        //              this trailhead has score 4 because we only can't go to the `9` on the top-left.
        //              and down-right is accessible with multiple approaches but there is only 1 needed.
        //        e) This is very handy problem for `Bidirectional Search` (because we know starting and goal states)
        //
        // for example this map:
        //  89010123
        //  78121874
        //  87430965
        //  96549874
        //  45678903
        //  32019012
        //  01329801
        //  10456732
        // There are 9 trailheads.
        // Considering the trailheads in reading order, they have scores of 5, 6, 5, 3, 1, 3, 5, 3, and 5.
        // Adding these scores together, the sum of the scores of all trailheads is 36.
        // OKAY tried bi-directional...I have not been succesful... :D

        // 1. Locate all trailheads (cells with '0')
        List<int[]> trailheads = findCells(input, '0');
        List<int[]> peaks = findCells(input, '9');

        // Directions for BFS (up, down, left, right)
        final int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        final int rows = input.length;
        final int cols = input[0].length;
        int totalScore = 0;

        // Assign IDs to each peak for distinct tracking
        Map<String, Integer> peakIdMap = new HashMap<>();
        for (int i = 0; i < peaks.size(); i++) {
            peakIdMap.put(peaks.get(i)[0] + "," + peaks.get(i)[1], i);
        }

        // downwardReachable[r][c] will store a set of peak IDs that can be reached from (r,c)
        // if you move upward (i.e., from lower height to higher), but we compute it by going downward from peaks.
        @SuppressWarnings("unchecked")
        Set<Integer>[][] downwardReachable = new HashSet[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                downwardReachable[r][c] = new HashSet<>();
            }
        }

        // Multi-source BFS from all peaks "downwards" (9→8→7...).
        // Actually, we start from each peak and move to any adjacent cell with height = currentHeight-1.
        Queue<int[]> downQ = new LinkedList<>();
        for (int i = 0; i < peaks.size(); i++) {
            int[] p = peaks.get(i);
            downwardReachable[p[0]][p[1]].add(i);
            downQ.offer(new int[]{p[0], p[1], input[p[0]][p[1]] - '0'});
        }

        boolean[][] visitedDown = new boolean[rows][cols];
        // Initially mark peaks as visited
        for (int[] p : peaks) visitedDown[p[0]][p[1]] = true;

        while (!downQ.isEmpty()) {
            int[] cur = downQ.poll();
            int r = cur[0], c = cur[1], h = cur[2];

            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                int nh = input[nr][nc] - '0';
                // We are going downward in reverse, so look for nh == h-1
                if (nh == h - 1) {
                    // Merge peak sets
                    boolean updated = downwardReachable[nr][nc].addAll(downwardReachable[r][c]);
                    if (updated) {
                        // We found new information, so continue BFS
                        if (!visitedDown[nr][nc]) {
                            visitedDown[nr][nc] = true;
                            downQ.offer(new int[]{nr, nc, nh});
                        }
                    }
                }
            }
        }

        // Now downwardReachable[][] has info about which peaks can be reached from each cell.
        // For each trailhead, run the original BFS upward (0→1→2...) and find reachable peaks by intersecting sets
        for (int[] start : trailheads) {
            boolean[][] visitedUp = new boolean[rows][cols];
            visitedUp[start[0]][start[1]] = true;

            Queue<int[]> upQ = new LinkedList<>();
            upQ.offer(new int[]{start[0], start[1], input[start[0]][start[1]] - '0'});

            Set<Integer> foundPeaks = new HashSet<>();

            while (!upQ.isEmpty()) {
                int[] cur = upQ.poll();
                int r = cur[0], c = cur[1], h = cur[2];

                // Add any peaks known reachable from this cell
                foundPeaks.addAll(downwardReachable[r][c]);

                // If we are at 9, no need to go further
                if (h == 9) continue;

                for (int[] d : dirs) {
                    int nr = r + d[0], nc = c + d[1];
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                    if (!visitedUp[nr][nc]) {
                        int nh = input[nr][nc] - '0';
                        if (nh == h + 1) {
                            visitedUp[nr][nc] = true;
                            upQ.offer(new int[]{nr, nc, nh});
                        }
                    }
                }
            }

            totalScore += foundPeaks.size();
        }

        System.out.println(totalScore);
    }

    public static List<int[]> findCells(char[][] map, char target) {
        List<int[]> cells = new ArrayList<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == target) {
                    cells.add(new int[]{i, j});
                }
            }
        }
        return cells;
    }
}
