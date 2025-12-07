// High -level
//
// Input:
//  .......S.......
//  ...............
//  .......^.......
//  ...............
//  ......^.^......
//  ...............
//  .....^.^.^.....
//  ...............
//  ....^.^...^....
//  ...............
//  ...^.^...^.^...
//  ...............
//  ..^...^.....^..
//  ...............
//  .^.^.^.^.^...^.
//  ...............
//
// The starting point is at S and then we have operation going down (i.e., I).
// and when you face '^', then those lines has to be splittet f.e.,
// ...S...
// ...|...
// ...^...
// ..|.|..
// ......
//
// and so on.
//
//
// Algorithm:
//  1. Parse the input into a 2D grid
//  2. Find the starting position 'S'
//  3. Track active columns using a HashSet (handles path merging automatically)
//  4. Process row by row: if '^' split left/right, otherwise continue down
//  5. Count total splits and print the result... and wualah... :D 
//
// In the above example it was 21 times. i.e.,
//
// .......S.......
// .......|.......
// ......|^|......
// ......|.|......
// .....|^|^|.....
// .....|.|.|.....
// ....|^|^|^|....
// ....|.|.|.|....
// ...|^|^|||^|...
// ...|.|.|||.|...
// ..|^|^|||^|^|..
// ..|.|.|||.|.|..
// .|^|||^||.||^|.
// .|.|||.||.||.|.
// |^|^|^|^|^|||^|
// |.|.|.|.|.|||.|
//
//
//

fn main() {
    let contents = std::fs::read_to_string("src/input/day07.txt")
        .expect("Should have been able to read the file");

    // 1. Parse the input
    let grid: Vec<Vec<char>> = contents
        .lines()
        .map(|line| line.chars().collect())
        .collect();

    if grid.is_empty() {
        println!("Empty input");
        return;
    }

    // 2. Find the position of S
    let mut start_col: Option<usize> = None;
    for (col, &c) in grid[0].iter().enumerate() {
        if c == 'S' {
            start_col = Some(col);
            break;
        }
    }

    let start_col = match start_col {
        Some(col) => col,
        None => {
            println!("No starting position 'S' found");
            return;
        }
    };

    // 3. Track active columns (using a set to handle merging paths) -> this would avoid couting
    //    multiple paths twice or even more:))
    use std::collections::HashSet;
    let mut active_cols: HashSet<usize> = HashSet::new();
    active_cols.insert(start_col);

    let mut total_splits: u64 = 0;

    // 4. Process row by row
    for row in 1..grid.len() {
        let mut next_cols: HashSet<usize> = HashSet::new();

        for &col in &active_cols {
            let c = grid[row][col];

            if c == '^' {
                // Split: this counts as one split, paths go left and right
                total_splits += 1;
                if col > 0 {
                    next_cols.insert(col - 1);
                }
                if col < grid[row].len() - 1 {
                    next_cols.insert(col + 1);
                }
            } else {
                // Continue straight down
                next_cols.insert(col);
            }
        }

        active_cols = next_cols;
    }

    // 6. Print how many times we split
    println!("Total splits: {}", total_splits);
}
