// High-Level
//
// So we have this input:
// 162,817,812
// 57,618,57
// 906,360,560
// 592,479,940
// 352,342,300
// 466,668,158
// 542,29,236
// 431,825,988
// 739,650,466
// 52,470,668
// 216,146,977
// 819,987,18
// 117,168,530
// 805,96,715
// 346,949,466
// 970,615,88
// 941,993,340
// 862,61,35
// 984,92,344
// 425,690,689
//
// which is basically X,Y,Z axis => we can call then junction boxes (one per line).
// In this example, the two junction boxes which are closest together are 162,817,812 and 425,690,689.
//
// When we connect these two junction boxes => they become of part of the same circuit
// After this circuit (i.e., containing two junction boxes) we have remaining 18 junction boxes.
//
// Now, the two junction boxes which are closest together but aren't already directly connected
// are 162,817,812 and 431,825,988. After connecting them, since 162,817,812 is already connected
// to another junction box, there is now a single circuit which contains three junction boxes and an
// additional 17 circuits which contain one junction box each.
//
// The next two junction boxes to connect are 906,360,560 and 805,96,715.
// After connecting them, there is a circuit containing 3 junction boxes, a circuit containing 2 junction boxes,
// and 15 circuits which contain one junction box each.
//
// The next two junction boxes are 431,825,988 and 425,690,689.
// Because these two junction boxes were already in the same circuit, nothing happens!
//
// This process continues for a while, and the Elves are concerned that they don't have enough extension cables for all these circuits.
// They would like to know how big the circuits will be.
//
// ---------
// To simplify this problem let's make som ASCII illustrations + Let's first try with 2D! :)
// ----
// Initial State: 8 junction boxes
//
//       Y
//       ↑
//     10│        A              B
//       │        ○              ○
//       │
//      8│                  C
//       │                  ○
//       │
//      6│   D                       E
//       │   ○                       ○
//       │
//      4│            F
//       │            ○
//       │
//      2│   G                  H
//       │   ○                  ○
//       │
//       └──────────────────────────→ X
//           2    4    6    8   10
//
//       8 separate circuits (each box = 1 circuit)
//
//   Step 1: Connect closest pair (D ↔ G)
//
//       Y
//       ↑
//     10│        A              B
//       │        ○              ○
//       │
//      8│                  C
//       │                  ○
//       │
//      6│   D                       E
//       │   ○                       ○
//       │   │
//      4│   │        F
//       │   │        ○
//       │   │
//      2│   G                  H
//       │   ○                  ○
//       │
//       └──────────────────────────→ X
//
//       Circuit 1: [D-G]  +  6 single boxes = 7 circuits
//
//   Step 2: Connect next closest (A ↔ C)
//
//       Y
//       ↑
//     10│        A              B
//       │        ○              ○
//       │         \
//      8│          \───────C
//       │                  ○
//       │
//      6│   D                       E
//       │   ○                       ○
//       │   │
//      4│   │        F
//       │   │        ○
//       │   │
//      2│   G                  H
//       │   ○                  ○
//       │
//       └──────────────────────────→ X
//
//       Circuit 1: [D-G]    Circuit 2: [A-C]    + 4 singles = 6 circuits
//
//   Step 3: Connect (F ↔ H)
//
//       Y
//       ↑
//     10│        A              B
//       │        ○              ○
//       │         \
//      8│          \───────C
//       │                  ○
//       │
//      6│   D                       E
//       │   ○                       ○
//       │   │
//      4│   │        F
//       │   │        ○
//       │   │         \
//      2│   G          \───────H
//       │   ○                  ○
//       │
//       └──────────────────────────→ X
//
//       [D-G]  [A-C]  [F-H]  + B, E singles = 5 circuits
//
//   Step 4: Connect (D ↔ F) - Circuits merge!
//
//       Y
//       ↑
//     10│        A              B
//       │        ○              ○
//       │         \
//      8│          \───────C
//       │                  ○
//       │
//      6│   D───────────            E
//       │   ○           \           ○
//       │   │            \
//      4│   │        F────┘
//       │   │        ○
//       │   │         \
//      2│   G          \───────H
//       │   ○                  ○
//       │
//       └──────────────────────────→ X
//
//       [D-G-F-H] merged!   [A-C]   + B, E = 4 circuits
//
//   Step 5: Trying to connect (G ↔ F) - Already same circuit!
//
//            D
//            ○
//            │
//            │       ╳ ← G and F already connected
//       G────┤       │   through D! Nothing happens!
//       ○    │       │
//            │       │
//            F───────H
//            ○       ○
//
//       Still 4 circuits - no change!
//
// ... and so on
// it depends on those junction boxes.
//
// In the 3D we have to just extend and compute the distance a bit harder than for 2D :).
// Also we have to store those links so we don't do this twice (and we can detect if such
// junction box is not a part of already created circuit.
// If we have such connections done we must do multiplication of largest 3 circuit.
// So in this 2D we would have in Step 5: 1. [D,G,F,H] (i.e., 4), 2. [A-C] (i.e., 2) 3. B or E => 1 => 8 in total
// and that would be that result.
//
//
// High level algorithm would be:
//      1. Parse the input
//      2. Compute all pair-wise edges and sort them
//      3. then we need to use Union-Find structure
//      4. Now for each connection (in this case it would be 1000)
//              i. try to connect =>
//              ii. if already in same circuit nothing happens (but still counts as one connection attempt)
//      5. Get circuit sizes (sort them)
//      6. First 3 multiply them and print the result.
//
// Junction box in 3D space => we will make a helpful struct to better handle...
#[derive(Debug, Clone, Copy)]
struct JunctionBox {
    x: i64,
    y: i64,
    z: i64,
    id: usize,
}

// we need to compute distance squared for two junction boxes :))
impl JunctionBox {
    fn distance_squared(&self, other: &JunctionBox) -> i64 {
        let dx = self.x - other.x;
        let dy = self.y - other.y;
        let dz = self.z - other.z;
        dx * dx + dy * dy + dz * dz
    }
}

// Union-Find data structure to track circuits
struct UnionFind {
    parent: Vec<usize>,
    rank: Vec<usize>,
    size: Vec<usize>,
}

impl UnionFind {
    fn new(n: usize) -> Self {
        UnionFind {
            parent: (0..n).collect(),
            rank: vec![0; n],
            size: vec![1; n],
        }
    }

    fn find(&mut self, x: usize) -> usize {
        if self.parent[x] != x {
            self.parent[x] = self.find(self.parent[x]);
        }
        self.parent[x]
    }

    fn union(&mut self, x: usize, y: usize) -> bool {
        let root_x = self.find(x);
        let root_y = self.find(y);

        if root_x == root_y {
            return false; // already in same circuit (we reject to add same!)
        }

        // union by rank
        if self.rank[root_x] < self.rank[root_y] {
            self.parent[root_x] = root_y;
            self.size[root_y] += self.size[root_x];
        } else if self.rank[root_x] > self.rank[root_y] {
            self.parent[root_y] = root_x;
            self.size[root_x] += self.size[root_y];
        } else {
            self.parent[root_y] = root_x;
            self.size[root_x] += self.size[root_y];
            self.rank[root_x] += 1;
        }
        true
    }

    fn get_circuit_sizes(&mut self) -> Vec<usize> {
        let n = self.parent.len();
        let mut sizes = Vec::new();
        for i in 0..n {
            if self.find(i) == i {
                sizes.push(self.size[i]);
            }
        }
        sizes.sort_by(|a, b| b.cmp(a)); // descending order
        sizes
    }
}

// Represents a potential connection between two junction boxes
#[derive(Debug)]
struct Edge {
    box_a: usize,
    box_b: usize,
    distance_sq: i64,
}

fn parse_junction_boxes(contents: &str) -> Vec<JunctionBox> {
    contents
        .lines()
        .enumerate()
        .filter(|(_, line)| !line.trim().is_empty())
        .map(|(id, line)| {
            let parts: Vec<i64> = line
                .split(',')
                .map(|s| s.trim().parse().unwrap())
                .collect();
            JunctionBox {
                x: parts[0],
                y: parts[1],
                z: parts[2],
                id,
            }
        })
        .collect()
}

fn compute_all_edges(boxes: &[JunctionBox]) -> Vec<Edge> {
    let mut edges = Vec::new();
    for i in 0..boxes.len() {
        for j in (i + 1)..boxes.len() {
            edges.push(Edge {
                box_a: boxes[i].id,
                box_b: boxes[j].id,
                distance_sq: boxes[i].distance_squared(&boxes[j]),
            });
        }
    }
    // Sort by distance (ascending)
    edges.sort_by(|a, b| a.distance_sq.cmp(&b.distance_sq));
    edges
}

fn solve(contents: &str, num_connections: usize) -> usize {
    let boxes = parse_junction_boxes(contents);
    let n = boxes.len();

    // Compute all pairwise distances and sort them
    let edges = compute_all_edges(&boxes);

    // Initialize Union-Find: each box is its own circuit
    let mut uf = UnionFind::new(n);

    // Process only the first num_connections edges (shortest first)
    let mut connections_made = 0;
    for edge in edges {
        if connections_made >= num_connections {
            break;
        }
        uf.union(edge.box_a, edge.box_b);
        connections_made += 1;
    }

    let sizes = uf.get_circuit_sizes();
    let result: usize = sizes.iter().take(3).product();

    println!("Circuit sizes (largest first): {:?}", sizes);
    println!("Top 3 sizes: {:?}", sizes.iter().take(3).collect::<Vec<_>>());
    println!("Result (product of top 3): {}", result);

    result
}

fn main() {
    let contents = std::fs::read_to_string("src/input/day08.txt")
        .expect("Should have been able to read the file");

    // Make 1000 shortest connections as per the problem
    solve(&contents, 1000);
}
