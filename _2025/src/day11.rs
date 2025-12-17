// High-Level
//
// We have a directed graph of devices where each device has outputs leading to other devices.
// Input format: "aaa: bbb ccc" means device "aaa" has outputs to devices "bbb" and "ccc".
// We have to `Count` all distinct paths from device "you" to device "out".
// =======
// This is essentially a path counting problem in a DAG (directed acyclic graph).
// Moreover, it is analogous to count derivations in a context-free grammar, where "you" is the start symbol and "out" is the terminal
// So we can either use DFS or recursion with memoization (or maybe even without memoization): for each node, count = sum of counts from all its children.
// Base case should be ("out" node has exactly 1 path (itself).

use std::collections::HashMap;

fn parse_graph(contents: &str) -> HashMap<String, Vec<String>> {
    let mut graph: HashMap<String, Vec<String>> = HashMap::new();

    for line in contents.lines() {
        let line = line.trim();
        if line.is_empty() {
            continue;
        }

        let parts: Vec<&str> = line.split(':').collect();
        let device = parts[0].trim().to_string();
        let outputs: Vec<String> = parts[1]
            .split_whitespace()
            .map(|s| s.to_string())
            .collect();

        graph.insert(device, outputs);
    }

    graph
}

fn count_paths(
    node: &str,
    graph: &HashMap<String, Vec<String>>,
    memo: &mut HashMap<String, u64>,
) -> u64 {
    // Base case: reached "out"
    if node == "out" {
        return 1;
    }

    // Check memoization
    if let Some(&count) = memo.get(node) {
        return count;
    }

    // Get children, if node has no outputs it's a dead end
    let children = match graph.get(node) {
        Some(c) => c,
        None => return 0,
    };

    // Sum paths through all children
    let count: u64 = children
        .iter()
        .map(|child| count_paths(child, graph, memo))
        .sum();

    memo.insert(node.to_string(), count);
    count
}

fn solve(contents: &str) -> u64 {
    let graph = parse_graph(contents);
    let mut memo: HashMap<String, u64> = HashMap::new();
    count_paths("you", &graph, &mut memo)
}

fn main() {
    let contents = std::fs::read_to_string("src/input/day11.txt")
        .expect("Should have been able to read the file");

    let result = solve(&contents);
    println!("Result: {}", result);
}

// this is for day11_mini.txt (to avoid basically re-naming the input from main function)
#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_example() {
        let input = "aaa: you hhh
you: bbb ccc
bbb: ddd eee
ccc: ddd eee fff
ddd: ggg
eee: out
fff: out
ggg: out
hhh: ccc fff iii
iii: out";
        assert_eq!(solve(input), 5);
    }
}
