// High-Level
//
// Basically is about to finding the largest rectangle that can be formed using two red tiles as
// oposite corners. For tiles  (x1, y1) and (x2, y2) and the rectangle area is (|x2-x1|+1) * (|y2-y1|+1)

fn parse_tiles(contents: &str) -> Vec<(i64, i64)> {
    contents
        .lines()
        .filter(|line| !line.trim().is_empty())
        .map(|line| {
            let parts: Vec<i64> = line
                .split(',')
                .map(|s| s.trim().parse().unwrap())
                .collect();
            (parts[0], parts[1])
        })
        .collect()
}

fn rectangle_area(p1: (i64, i64), p2: (i64, i64)) -> i64 {
    let width = (p2.0 - p1.0).abs() + 1;
    let height = (p2.1 - p1.1).abs() + 1;
    width * height
}

fn solve(contents: &str) -> i64 {
    let tiles = parse_tiles(contents);
    let mut max_area = 0;

    // Check all pairs of tiles
    for i in 0..tiles.len() {
        for j in (i + 1)..tiles.len() {
            let area = rectangle_area(tiles[i], tiles[j]);
            if area > max_area {
                max_area = area;
            }
        }
    }

    max_area
}

fn main() {
    let contents = std::fs::read_to_string("src/input/day09.txt")
        .expect("Should have been able to read the file");

    let result = solve(&contents);
    println!("Result: {}", result);
}