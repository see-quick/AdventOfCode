package advent.of.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class day12 {

    private static final Graph graph = new Graph();

    static class Graph {
        private final List<String> nodes;
        private final Map<String, List<String>> links;

        public Graph() {
            // empty graph
            this.nodes = new ArrayList<>();
            this.links = new HashMap<>();
        }

        public void addNode(String key) {
            if (!this.nodes.contains(key)) {
                this.nodes.add(key);
            }
        }

        public void addLink(String key, String key2) {
            this.links.putIfAbsent(key, new ArrayList<>());
            this.links.putIfAbsent(key2, new ArrayList<>());

            // both ways
            this.links.get(key).add(key2);
            this.links.get(key2).add(key);
        }

        public List<String> getNeighbours(String key) {
            if (this.links.containsKey(key)) {
                return this.links.get(key);
            } else {
                // returning empty list
                return new ArrayList<>();
            }
        }

        public Map<String, List<String>> getAllNeighbours() {
            return links;
        }

        public int numberOfNodes() {
            return this.nodes.size();
        }

        @Override
        public String toString() {
            return "Graph{" +
                "nodes=" + nodes +
                ", links=" + links +
                '}';
        }
    }

    public static int modifiedBfs() {
        Queue<String> nodes = new LinkedList<>();
        // init
        nodes.add(", start");

        int numberOfPaths = 0;
        while (!nodes.isEmpty()) {
            String current = nodes.poll();
            List<String> neighbours = graph.getAllNeighbours().get(current.substring(current.lastIndexOf(',') + 2));

            if (current.contains("end")) {
                numberOfPaths++;
                continue;
            }

            for (String neighbour : neighbours) {
                if (neighbour.toLowerCase().equals(neighbour)) {
                    if (!current.contains(neighbour)) nodes.add(current + ", " + neighbour);
                } else {
                    nodes.add(current + ", " + neighbour);
                }
            }
        }
        return numberOfPaths;
    }

    public static void main(String[] args) {
        // I. parsing and load model part
        String input = Utils.constructStringFromFile("day12.txt");

        System.out.println(input);

        String[] serializedLinks = input.split("\n");

        for (String link : serializedLinks) {
            String[] nodeLink = link.split("-");

            // 1st add all nodes
            graph.addNode(nodeLink[0]);
            graph.addNode(nodeLink[1]);

            // 2nd add all links
            graph.addLink(nodeLink[0], nodeLink[1]);
        }
        System.out.println(graph);

        // II. algorithm part
        // 3rd we gonna use modified BFS for search for all paths... (but DFS could be also applicable :))
        // A,B,C.. (un-limited traverse...)
        // a,b,c...(only once)
        System.out.println("Result is: " + modifiedBfs());
    }
}
