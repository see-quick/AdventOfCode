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
//

fn main() {
    let contents = std::fs::read_to_string("src/input/day04.txt")
        .expect("Should have been able to read the file");

    // 1. Parse input into a 2D grid
    let grid: Vec<Vec<char>> = contents
        .lines()
        .filter(|line| !line.trim().is_empty())
        .map(|line| line.chars().collect())
        .collect();

    let rows = grid.len();
    let cols = if rows > 0 { grid[0].len() } else { 0 };

    // 8 adjacent directions: up, down, left, right, and 4 diagonals
    let directions: [(i32, i32); 8] = [
        (-1, -1), (-1, 0), (-1, 1),  // top-left, top, top-right
        (0, -1),           (0, 1),   // left, right
        (1, -1),  (1, 0),  (1, 1),   // bottom-left, bottom, bottom-right
    ];

    let mut accessible_count = 0;

    // 2. Go through each position in the grid
    for row in 0..rows {
        for col in 0..cols {
            // Only check positions with rolls of paper '@'
            if grid[row][col] != '@' {
                continue;
            }

            // Count adjacent rolls of paper
            let mut adjacent_rolls = 0;

            for (dr, dc) in directions.iter() {
                let new_row = row as i32 + dr;
                let new_col = col as i32 + dc;

                // Check bounds
                if new_row >= 0 && new_row < rows as i32 && new_col >= 0 && new_col < cols as i32 {
                    if grid[new_row as usize][new_col as usize] == '@' {
                        adjacent_rolls += 1;
                        // Early break: if we already have 4+ adjacent rolls, no need to check more
                        if adjacent_rolls >= 4 {
                            break;
                        }
                    }
                }
            }

            // If fewer than 4 adjacent rolls, forklift can access this roll
            if adjacent_rolls < 4 {
                accessible_count += 1;
            }
        }
    }

    // 3. Print result
    println!("Rolls of paper accessible by forklift: {}", accessible_count);
}
