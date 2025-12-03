// Top Level Design
//
// We have single line input where there are numbers i.e., 11-22,95-115 ...
// Each ranges are separated by comma (i.e., ',') and specific range is divided by dash (i.e., `-`).
//
// We have to find all invalid IDs in those ranges.
// Invalid ID is when number 11 (is repeated twice) or 22, even 123123 or 1212.
//
// We don't need to care about leading zeroes!
// ---
//
// So I am thinking that we should read the input as a String possibly?? Hopefully that's gonna be
// not so big :D (if not than we can load by parts...).
//
// When we would have input then we have to start from <start-id> -> <end-id> (iterating).
// Note: The important thing that sizes would differentiate (i.e., 998-1012) so we can't make
// assumption with 1000-1012 (length 4 and try just 1010) :).
//
// Each iteration we should compare number by its length for instance even length ==> can't repeat
// twice only `odd` length.
//
// So basically having 115-999 => there is nothing :). But 4-20 => there is f.e., 11.
//
// So, at higher point now:
//  1. read input
//  2. for each ID range
//      i. trim those which are even and start with odd lowest number (i.e., 998-1012) start with
//      1000.
//  3. resolve number if there is two numbers twice (I.e., 1010)....
//      i. if so then add such number to a sum (which would be result)
//  4. print SUM!
//
// The complexity of the algorithm I think should be number of ID ranges * <interval of ranges>
//
// T(N^2) and S(1) I assume?

fn is_repeated_id(n: u64) -> bool {
    let s = n.to_string();
    let len = s.len();

    // Only even-length numbers can have the "repeated twice" pattern
    if len % 2 != 0 {
        return false;
    }

    let half = len / 2;
    let first_half = &s[..half];
    let second_half = &s[half..];

    first_half == second_half
}

fn main() {
    let contents = std::fs::read_to_string("src/input/day02.txt")
        .expect("Should have been able to read the file");

    let line = contents.lines().next().unwrap_or("").trim();

    let mut sum: u64 = 0;

    for range_str in line.split(',') {
        let parts: Vec<&str> = range_str.split('-').collect();
        if parts.len() != 2 {
            // ignoring parts not having dash
            continue;
        }

        let start: u64 = parts[0].trim().parse().expect("Invalid start number");
        let end: u64 = parts[1].trim().parse().expect("Invalid end number");

        for id in start..=end {
            if is_repeated_id(id) {
                sum += id;
            }
        }
    }

    println!("Sum of invalid IDs: {}", sum);
} 
