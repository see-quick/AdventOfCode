// High level... 2D Bin packing in other words...
//
// We have present shapes that needs to fit into rectangular regions...
// Each shape can be rotated (90*) many times and flipped.
// The goal is to count how many regions can fit all required present...
//
// This problem is straightforward solved by backtracking as I can tell (at first glance)
// We we will try to place pieces one by one at the first available position and exploring all possibly orientations.
// -------
// Note 1:
//  So the brute-force backtracking doesn't work one way how to enchance the processing is to make memoization
//  in a way that we will convert the grid to bitmask...
// Note 2:
//  Memoization didn't worked either...
// ...
// Note 3:
// Eventually I tried backtracking but with more smart approach where
// I checked symmetry breaking (i.e., for identical pieces, place them at non-decreasing positions)
// Plus area pruning where we check and fail early if pieces need more cells than available...
// Also I realised that state space is TOO LARGE for memoization so collision rate is very too low for the benefit)
// That way I eventually was able to run this example under 50ms rather than 10000 hours :D

use std::collections::HashSet;

type Shape = Vec<(i32, i32)>; // List of (row, col) offsets from anchor point

/// Normalize a shape so its minimum row and col are both 0
fn normalize(shape: &Shape) -> Shape {
    if shape.is_empty() {
        return vec![];
    }
    let min_row = shape.iter().map(|&(r, _)| r).min().unwrap();
    let min_col = shape.iter().map(|&(_, c)| c).min().unwrap();
    let mut normalized: Shape = shape.iter().map(|&(r, c)| (r - min_row, c - min_col)).collect();
    normalized.sort();
    normalized
}

fn rotate_90_cw(shape: &Shape) -> Shape {
    // (r, c) -> (c, -r)
    normalize(&shape.iter().map(|&(r, c)| (c, -r)).collect())
}

fn flip_h(shape: &Shape) -> Shape {
    // (r, c) -> (r, -c)
    normalize(&shape.iter().map(|&(r, c)| (r, -c)).collect())
}

/// Generate all unique orientations (up to 8: 4 rotations × 2 flips)
fn all_orientations(shape: &Shape) -> Vec<Shape> {
    let mut seen = HashSet::new();
    let mut result = Vec::new();

    let mut current = normalize(shape);
    for _ in 0..4 {
        if seen.insert(current.clone()) {
            result.push(current.clone());
        }
        current = rotate_90_cw(&current);
    }

    current = flip_h(shape);
    for _ in 0..4 {
        if seen.insert(current.clone()) {
            result.push(current.clone());
        }
        current = rotate_90_cw(&current);
    }

    result
}

fn parse_shape(lines: &[&str]) -> Shape {
    let mut cells = Vec::new();
    for (row, line) in lines.iter().enumerate() {
        for (col, ch) in line.chars().enumerate() {
            if ch == '#' {
                cells.push((row as i32, col as i32));
            }
        }
    }
    normalize(&cells)
}

fn parse_shapes(section: &str) -> Vec<Vec<Shape>> {
    let mut shapes = Vec::new();
    let mut current_lines: Vec<&str> = Vec::new();

    for line in section.lines() {
        let line = line.trim();
        if line.contains(':') && !line.contains('x') {
            // Shape header like "0:"
            if !current_lines.is_empty() {
                let shape = parse_shape(&current_lines);
                shapes.push(all_orientations(&shape));
                current_lines.clear();
            }
        } else if !line.is_empty() && !line.contains('x') {
            current_lines.push(line);
        }
    }

    if !current_lines.is_empty() {
        let shape = parse_shape(&current_lines);
        shapes.push(all_orientations(&shape));
    }

    shapes
}

fn parse_region(line: &str) -> Option<(usize, usize, Vec<usize>)> {
    let parts: Vec<&str> = line.split(':').collect();
    if parts.len() != 2 {
        return None;
    }

    let dims: Vec<&str> = parts[0].trim().split('x').collect();
    if dims.len() != 2 {
        return None;
    }

    let width: usize = dims[0].parse().ok()?;
    let height: usize = dims[1].parse().ok()?;

    let counts: Vec<usize> = parts[1]
        .split_whitespace()
        .filter_map(|s| s.parse().ok())
        .collect();

    Some((width, height, counts))
}

/// Solver state
struct Solver<'a> {
    width: usize,
    height: usize,
    grid: Vec<bool>,                // true = occupied
    remaining: Vec<usize>,          // remaining count for each shape type
    all_shapes: &'a [Vec<Shape>],   // [shape_type][orientation] = Shape
    shape_sizes: Vec<usize>,        // size of each shape type
    total_remaining_cells: usize,   // sum of (remaining[i] * shape_sizes[i])
}

impl<'a> Solver<'a> {
    fn new(width: usize, height: usize, counts: &[usize], all_shapes: &'a [Vec<Shape>]) -> Self {
        let mut remaining = counts.to_vec();
        while remaining.len() < all_shapes.len() {
            remaining.push(0);
        }

        let shape_sizes: Vec<usize> = all_shapes
            .iter()
            .map(|orientations| orientations.first().map_or(0, |s| s.len()))
            .collect();

        let total_remaining_cells: usize = remaining
            .iter()
            .zip(shape_sizes.iter())
            .map(|(&count, &size)| count * size)
            .sum();

        Solver {
            width,
            height,
            grid: vec![false; width * height],
            remaining,
            all_shapes,
            shape_sizes,
            total_remaining_cells,
        }
    }

    #[inline]
    fn idx(&self, row: usize, col: usize) -> usize {
        row * self.width + col
    }

    /// Check if shape can be placed at (row, col)
    fn can_place(&self, shape: &Shape, row: usize, col: usize) -> bool {
        for &(dr, dc) in shape {
            let nr = row as i32 + dr;
            let nc = col as i32 + dc;
            if nr < 0 || nc < 0 || nr >= self.height as i32 || nc >= self.width as i32 {
                return false;
            }
            if self.grid[self.idx(nr as usize, nc as usize)] {
                return false;
            }
        }
        true
    }

    /// Place a shape at (row, col)
    fn place(&mut self, shape: &Shape, row: usize, col: usize) {
        for &(dr, dc) in shape {
            let nr = (row as i32 + dr) as usize;
            let nc = (col as i32 + dc) as usize;
            let idx = nr * self.width + nc;
            self.grid[idx] = true;
        }
    }

    /// Remove a shape from (row, col)
    fn unplace(&mut self, shape: &Shape, row: usize, col: usize) {
        for &(dr, dc) in shape {
            let nr = (row as i32 + dr) as usize;
            let nc = (col as i32 + dc) as usize;
            let idx = nr * self.width + nc;
            self.grid[idx] = false;
        }
    }

    /// Main backtracking solver, which...  places pieces type by type
    /// min_pos is used for symmetry breaking: for identical pieces, require non-decreasing positions
    fn solve_from(&mut self, shape_idx: usize, min_pos: usize) -> bool {
        // Find next shape type with remaining pieces
        let mut current_shape = shape_idx;
        while current_shape < self.all_shapes.len() && self.remaining[current_shape] == 0 {
            current_shape += 1;
        }

        // All pieces placed => so we are good to end... :)
        if current_shape >= self.all_shapes.len() {
            return true;
        }

        let shape_size = self.shape_sizes[current_shape];

        // Determine starting position for symmetry breaking
        let start_pos = if current_shape == shape_idx { min_pos } else { 0 };

        // Try all positions
        for pos in start_pos..(self.width * self.height) {
            let row = pos / self.width;
            let col = pos % self.width;

            // Try each orientation
            for orientation in &self.all_shapes[current_shape] {
                if self.can_place(orientation, row, col) {
                    self.place(orientation, row, col);
                    self.remaining[current_shape] -= 1;
                    self.total_remaining_cells -= shape_size;

                    // For same shape type, next piece must be at >= position (symmetry breaking)
                    let next_min = if self.remaining[current_shape] > 0 { pos } else { 0 };

                    if self.solve_from(current_shape, next_min) {
                        self.unplace(orientation, row, col);
                        self.remaining[current_shape] += 1;
                        self.total_remaining_cells += shape_size;
                        return true;
                    }

                    self.unplace(orientation, row, col);
                    self.remaining[current_shape] += 1;
                    self.total_remaining_cells += shape_size;
                }
            }
        }

        false
    }

    fn solve(&mut self) -> bool {
        self.solve_from(0, 0)
    }
}

fn can_fit_region(width: usize, height: usize, counts: &[usize], all_shapes: &[Vec<Shape>]) -> bool {
    // Quick check: total cells needed vs available
    let shape_sizes: Vec<usize> = all_shapes
        .iter()
        .map(|orientations| orientations.first().map_or(0, |s| s.len()))
        .collect();

    let total_cells_needed: usize = counts
        .iter()
        .zip(shape_sizes.iter())
        .map(|(&count, &size)| count * size)
        .sum();

    let total_cells_available = width * height;

    if total_cells_needed > total_cells_available {
        return false;
    }

    // No pieces to place?
    if counts.iter().all(|&c| c == 0) {
        return true;
    }

    let mut solver = Solver::new(width, height, counts, all_shapes);
    solver.solve()
}

fn solve(contents: &str) -> usize {
    // Parse shapes and regions
    let mut shapes_section = String::new();
    let mut regions_lines: Vec<&str> = Vec::new();

    for line in contents.lines() {
        let trimmed = line.trim();
        if trimmed.contains('x') && trimmed.contains(':') {
            regions_lines.push(trimmed);
        } else if !trimmed.is_empty() {
            shapes_section.push_str(line);
            shapes_section.push('\n');
        }
    }

    let all_shapes = parse_shapes(&shapes_section);

    let mut count = 0;
    for (i, line) in regions_lines.iter().enumerate() {
        if let Some((width, height, counts)) = parse_region(line) {
            let fits = can_fit_region(width, height, &counts, &all_shapes);
            if fits {
                count += 1;
            }
            println!("Region {}: {}x{} -> {}", i + 1, width, height, if fits { "YES" } else { "NO" });
        }
    }

    count
}

fn main() {
    let start = std::time::Instant::now();

    let contents = std::fs::read_to_string("src/input/day12.txt")
        .expect("Should have been able to read the file");

    // DEBUG FOR me...
    let mut shapes_section = String::new();
    let mut regions_lines: Vec<&str> = Vec::new();

    for line in contents.lines() {
        let trimmed = line.trim();
        if trimmed.contains('x') && trimmed.contains(':') {
            regions_lines.push(trimmed);
        } else if !trimmed.is_empty() {
            shapes_section.push_str(line);
            shapes_section.push('\n');
        }
    }

    let all_shapes = parse_shapes(&shapes_section);
    println!("Parsed {} shape types:", all_shapes.len());
    for (i, orientations) in all_shapes.iter().enumerate() {
        let size = orientations.first().map_or(0, |s| s.len());
        println!("  Shape {}: {} cells, {} orientations", i, size, orientations.len());
        // Print all orientations for shape 4
        if i == 4 {
            for (j, orient) in orientations.iter().enumerate() {
                println!("    Orientation {}: {:?}", j, orient);
            }
        }
    }

    println!("\nRegions:");
    for line in &regions_lines {
        if let Some((w, h, counts)) = parse_region(line) {
            println!("  {}x{}: {:?}", w, h, counts);
        }
    }
    println!();

    let result = solve(&contents);

    let elapsed = start.elapsed();
    println!("Result: {} (in {:.2?})", result, elapsed);
}