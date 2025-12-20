use _2025::{Day, run};

// High Level Design
//
// So we have this input:
//  ..@@.@@@@.
//  @@@.@.@.@@
//  @@@@@.@.@@
//  @.@@@@..@.
//  @@.@@@@.@@
//  .@@@@@@@.@
//  .@.@.@.@@@
//  @.@@@.@@@@
//  .@@@@@@@@.
//  @.@.@@@.@.
//
//
// legend:
//      1. roll of paper = @
//      2. forklifts => multiple machines...
//
// The forklifts can only access a roll of paper if there are fewer than four rolls of paper in the eight adjacent positions.
// If you can figure out which rolls of paper the forklifts can access, they'll spend less time looking and more time
// breaking down the wall to the cafeteria.
//
// We need to optimize...
//
//  High level algorithm:
//      1. parse input
//      2. go through each line
//          i. go through each character of that line either ('.' or '@').
//              v. check `eighth` adjacent positions
//              vi. if there are fewer than four rolls of paper (i.e., '@') then mark is as 'x'
//              (optionally: we can have counter for 'x') so that we know result.
//              vii. else continue and let that symbol as it is...
//      3. print How many rolls of paper can be accessed by a forklift :).
//
//  Complexity Analysis:
//      Time:  O(R * C) where R = number of rows, C = number of columns
//             - We iterate through each cell once: O(R * C)
//             - For each '@' cell, we check at most 8 neighbors: O(8) = O(1)
//             - With early break optimization: we exit as soon as adjacent_rolls >= 4
//             - Total: O(R * C * 1) = O(R * C)
//
//      Space: O(R * C)
//             - We store the entire grid in memory: O(R * C)
//             - Direction array is constant: O(8) = O(1)
//             - Other variables (counters, indices): O(1)
//             - Total: O(R * C)
//
// In Part 2, iteratively remove all accessible rolls until no more can be removed...so each iteration
//  i find all rolls with < 4 adjacent neighbours => remove them all at once. And we do it untul no more
//  rolls can be removed. Finally we need to return total count of removed rolls

// 8 adjacent directions: up, down, left, right, and 4 diagonals
const DIRECTIONS: [(i32, i32); 8] = [
    (-1, -1), (-1, 0), (-1, 1),  // top-left, top, top-right
    (0, -1),           (0, 1),   // left, right
    (1, -1),  (1, 0),  (1, 1),   // bottom-left, bottom, bottom-right
];

pub struct Day04 {
    grid: Vec<Vec<char>>,
}

impl Day04 {
    fn count_adjacent_rolls(&self, grid: &[Vec<char>], row: usize, col: usize) -> usize {
        let rows = grid.len();
        let cols = if rows > 0 { grid[0].len() } else { 0 };
        let mut count = 0;

        for (dr, dc) in DIRECTIONS.iter() {
            let new_row = row as i32 + dr;
            let new_col = col as i32 + dc;

            if new_row >= 0 && new_row < rows as i32 && new_col >= 0 && new_col < cols as i32 {
                if grid[new_row as usize][new_col as usize] == '@' {
                    count += 1;
                    if count >= 4 {
                        break;
                    }
                }
            }
        }
        count
    }

    fn find_accessible_rolls(&self, grid: &[Vec<char>]) -> Vec<(usize, usize)> {
        let rows = grid.len();
        let cols = if rows > 0 { grid[0].len() } else { 0 };
        let mut accessible = Vec::new();

        for row in 0..rows {
            for col in 0..cols {
                if grid[row][col] == '@' {
                    let adjacent = self.count_adjacent_rolls(grid, row, col);
                    if adjacent < 4 {
                        accessible.push((row, col));
                    }
                }
            }
        }
        accessible
    }
}

impl Day for Day04 {
    type Output1 = usize;
    type Output2 = usize;

    fn parse(input: &str) -> Self {
        let grid: Vec<Vec<char>> = input
            .lines()
            .filter(|line| !line.trim().is_empty())
            .map(|line| line.chars().collect())
            .collect();

        Self { grid }
    }

    fn part1(&self) -> Self::Output1 {
        self.find_accessible_rolls(&self.grid).len()
    }

    fn part2(&self) -> Self::Output2 {
        let mut grid = self.grid.clone();
        let mut total_removed = 0;

        loop {
            let accessible = self.find_accessible_rolls(&grid);
            if accessible.is_empty() {
                break;
            }

            // Remove all accessible rolls at once
            for (row, col) in &accessible {
                grid[*row][*col] = '.';
            }

            total_removed += accessible.len();
        }

        total_removed
    }
}

fn main() {
    run::<Day04>("src/input/day04.txt");
}