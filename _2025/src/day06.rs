// High level...
//
//
//  Input (note this is small input! the normal one would consist more values....):
// 123 328  51 64
// 45 64  387 23
// 6 98  215 314
// *   +   *   +
//
//
// So basically we need to make vertical stuff and each column at the end has operation which has
// to done by x numbers in that specific column.
//
// In each column, we get one number which has to be then sum up with all others in the column.
//
// Algorighm:
//
//  1. Parse the input ... and init the sum etc.
//  2. Each row store the number in different list (those lists then would be used for parallel
//     calculation)
//  3. If the reach end of row we then extract operation which has to be done on those numbers.
//  4. Store Each number in the list
//  5. Make sum of such list
//
//  Complexity Analysis:
//      Time:  O(R * C) where R = number of rows, C = number of columns
//             - Column calculations: O(R * C) total across all columns
//               (each column has R elements, and we have C columns)
//             - Total: O(R * C) => O(N) - (still linear)
//
//      Space: O(R * C)
//             - We store all numbers in column vectors: O(R * C)
//             - Operators vector: O(C)
//             - Other variables (indices, results): O(1)
//             - Total: O(R * C) => O(N) - (still linear)
//
fn main() {
    let contents = std::fs::read_to_string("src/input/day06.txt")
        .expect("Should have been able to read the file");

    let lines: Vec<&str> = contents.lines().collect();

    if lines.is_empty() {
        println!("Empty input");
        return;
    }

    // The last line contains the operators
    let operator_line = lines.last().unwrap();
    let data_lines = &lines[..lines.len() - 1];

    // Parse the columns by splitting on double spaces (column groups)
    // Each column group has numbers separated by single spaces
    let mut columns: Vec<Vec<u64>> = Vec::new();
    let mut operators: Vec<char> = Vec::new();

    // Parse operator line to get operations for each column
    // Operators are separated by spaces, we need to find * and +
    for c in operator_line.chars() {
        if c == '*' || c == '+' {
            operators.push(c);
        }
    }

    // Initialize columns based on number of operators
    for _ in 0..operators.len() {
        columns.push(Vec::new());
    }

    // Parse data lines
    // Split by double space to get column groups
    for line in data_lines {
        // Split by double space to get column groups
        let groups: Vec<&str> = line.split("  ").collect();

        let mut col_idx = 0;
        for group in groups {
            // Each group can have one or more numbers separated by single space
            let nums: Vec<&str> = group.split_whitespace().collect();
            for num_str in nums {
                if let Ok(num) = num_str.parse::<u64>() {
                    if col_idx < columns.len() {
                        columns[col_idx].push(num);
                    }
                    col_idx += 1;
                }
            }
        }
    }

    // Calculate result for each column
    let mut total_sum: u64 = 0;

    for (i, column) in columns.iter().enumerate() {
        let op = operators[i];
        let result = match op {
            '*' => column.iter().product::<u64>(),
            '+' => column.iter().sum::<u64>(),
            _ => 0,
        };

        println!("Column {}: {:?} {} = {}", i, column, op, result);
        total_sum += result;
    }

    println!("Total sum: {}", total_sum);
}
