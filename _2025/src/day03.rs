// High Level Design
//
// So basically the problem is that we have this input:
//      987654321111111
//      811111111111119
//      234234234234278
//      818181911112111
// where each line is basically single bank.
// We need to find exactly two numbers in each line to form the largest two-digit number.
//
// Key insight: For 811111111111119, we can make 89 (largest possible)
//   - The second digit should be the LAST occurrence of the maximum digit
//   - The first digit should be the largest digit BEFORE that position
//
// Algorithm:
//  1. read input and parse it properly
//  2. prepare and init sum and other auxiliary variables
//  3. for each row (line)
//      i. find the maximum digit value
//      ii. Option A: use max as FIRST digit (first occurrence), find best second digit after it
//      iii. Option B: use max as SECOND digit (last occurrence), find best first digit before it
//      iv. pick the larger of the two options and add to sum
//  4. print sum! :)
//
//  Complexity: T(N*M) where N = lines, M = digits per line. S(M) for storing digits.
// 
//
// Sum: 15639 => too low (hmmm...)
// ... ah 
//
fn main() {
    let contents = std::fs::read_to_string("src/input/day03.txt")
        .expect("Should have been able to read the file");

    let mut sum: u64 = 0;

    for line in contents.lines() {
        let line = line.trim();
        if line.is_empty() {
            continue;
        }

        let digits: Vec<u8> = line
            .chars()
            .filter_map(|c| c.to_digit(10).map(|d| d as u8))
            .collect();

        if digits.is_empty() {
            continue;
        }

        // Find the maximum digit value
        let max_digit = *digits.iter().max().unwrap();

        // Option A: Use max digit as FIRST digit (first occurrence), find best second after it
        let first_max_pos = digits.iter().position(|&d| d == max_digit).unwrap();
        let option_a = if first_max_pos < digits.len() - 1 {
            let second = digits.iter().skip(first_max_pos + 1).max().copied().unwrap_or(0);
            (max_digit as u64) * 10 + (second as u64)
        } else {
            0 // Can't use max as first if it's the last digit
        };

        // Option B: Use max digit as SECOND digit (last occurrence), find best first before it
        let last_max_pos = digits.iter().rposition(|&d| d == max_digit).unwrap();
        let option_b = if last_max_pos > 0 {
            let first = digits.iter().take(last_max_pos).max().copied().unwrap_or(0);
            (first as u64) * 10 + (max_digit as u64)
        } else {
            0 // Can't use max as second if it's the first digit
        };

        // Pick the larger option
        let two_digit_number = option_a.max(option_b);

        println!("Option A (max first): {}, Option B (max second): {} => {}", option_a, option_b, two_digit_number);

        println!("Two digit number: {}", two_digit_number);

        sum += two_digit_number;
    }

    println!("Sum: {}", sum);
}
