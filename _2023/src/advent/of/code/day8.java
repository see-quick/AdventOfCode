package advent.of.code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * This class solves the "Haunted Wasteland" puzzle.
 * It reads a series of instructions and node definitions from a file,
 * and computes the number of steps required to reach a specific node ("ZZZ") in a network.
 */
public class day8 {

    /**
     * Represents a node in the network with left and right connections.
     */
    static class Node {
        String left;
        String right;

        Node(String left, String right) {
            this.left = left;
            this.right = right;
        }
    }

    public static void main(String[] args) {
        String filePath = "_2023/src/advent/of/code/day8.txt";
        try {
            String[] fileContent = readFromFile(filePath);
            String instructions = fileContent[0];
            Map<String, Node> nodes = parseNodes(fileContent);
            System.out.println("Steps to reach ZZZ: " + countStepsToZZZ(instructions, nodes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads content from a file and returns it as an array of strings.
     *
     * @param filePath the path to the file.
     * @return an array of strings representing the lines of the file.
     * @throws IOException if an I/O error occurs.
     */
    private static String[] readFromFile(String filePath) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines.toArray(String[]::new);
        }
    }

    /**
     * Parses the node definitions from the file content and creates a map of nodes.
     *
     * @param fileContent the file content as an array of strings.
     * @return a map representing the nodes and their connections.
     */
    private static Map<String, Node> parseNodes(String[] fileContent) {
        Map<String, Node> nodes = new HashMap<>();
        for (int i = 1; i < fileContent.length; i++) {
            if(fileContent[i].isBlank()) {
                continue; // Skip blank lines
            }
            String[] parts = fileContent[i].split(" = |, ");
            if (parts.length != 3) {
                System.out.println("Skipping invalid line: " + fileContent[i]);
                continue;
            }
            nodes.put(parts[0].trim(), new Node(parts[1].replaceAll("[()]", "").trim(),
                    parts[2].replaceAll("[()]", "").trim()));
        }
        return nodes;
    }

    /**
     * Counts the number of steps required to reach the "ZZZ" node by following the instructions.
     *
     * @param instructions the string of instructions for navigating the network.
     * @param nodes        the map of nodes in the network.
     * @return the number of steps required to reach the "ZZZ" node, or -1 if the node is not found.
     */
    private static int countStepsToZZZ(String instructions, Map<String, Node> nodes) {
        String currentNode = "AAA";
        int stepCount = 0;

        while (!currentNode.equals("ZZZ")) {
            char instruction = instructions.charAt(stepCount % instructions.length());
            if (!nodes.containsKey(currentNode)) {
                System.out.println("Node not found: " + currentNode);
                return -1;
            }
            currentNode = instruction == 'L' ? nodes.get(currentNode).left : nodes.get(currentNode).right;
            stepCount++;
        }

        return stepCount;
    }
}
