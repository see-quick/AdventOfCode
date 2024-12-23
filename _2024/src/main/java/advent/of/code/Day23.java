package advent.of.code;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Day23 {

    public static void main(String[] args) {
        // algorithm design:
        //  0.  Parse input...where each line would be <edge-edge>
        //  1.  Make graph-representation using Map
        //  2.  Find all triangles (k = 3), which is clique NP-complete problem
        //  3.  Then we filter all triangles which contains at least one `t`

        // 0. Parse input
        final List<String> connections = DataLoader.loadDataFromFile("_2024/src/main/java/advent/of/code/day23.txt");

        // 1. Build adjacency list
        final Map<String, Set<String>> graph = new HashMap<>();

        for (final String connection : connections) {
            // <first_edge-second_edge>
            //  f.e., qp-kh
            final String[] parts = connection.split("-");
            final String firstPoint = parts[0];
            final String secondPoint = parts[1];

            // we include both directions
            // 1. ---->
            // 2. <----
            graph.computeIfAbsent(firstPoint, k -> new HashSet<>()).add(secondPoint);
            graph.computeIfAbsent(secondPoint, k -> new HashSet<>()).add(firstPoint);
        }

        // 2. Finding all triangles
        Set<Set<String>> triangles = new HashSet<>();

        for (final String node : graph.keySet()) {
            final Set<String> neighbors = graph.get(node);

            for (final String neighbor : neighbors) {
                final Set<String> common = new HashSet<>(graph.get(neighbor));
                // Find common neighbors
                common.retainAll(neighbors);
                for (final String third : common) {
                    final Set<String> triangle = new TreeSet<>(Arrays.asList(node, neighbor, third));
                    triangles.add(triangle); // Store unique triangle
                }
            }
        }

        // 3. Filter out those, which contains at least one `t` vertex
        int count = 0;
        for (Set<String> triangle : triangles) {
            if (triangle.stream().anyMatch(v -> v.startsWith("t"))) {
                count++;
                System.out.println(triangle);
            }
        }

        System.out.println("Total triangles with at least one 't': " + count);
    }
}
