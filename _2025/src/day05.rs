// High level...
//
//
// There is a database, which operates on IDs, there are two types:
//  1. ID ranges
//  2. available IDs
//
//
//  f.e.,
//      3-5
//      10-14
//      16-20
//      12-18

//      1
//      5
//      8
//      11
//      17
//      32
//
// Note: ID-ranges are inclusive
//
// We want to know how many IDs are "fresh" => i.e., which can be contained in any ranges...
//
// High-level algorithm:
//          1. parse input => list of ID-ranges + available ID
//          2. for each available ID try each range
//          3. if any ID is container in any ID-ranges
//                 i. then increment counter
//          4. else continue with other IDs until we discover everything
//          5. print how many IDs are contained in those ranges
//------ (this is very brute force algorithm => and for sure there are many improvements such as
//having some cache (to know if there has been already such number.. etc.)
//

fn main() {
    let contents = std::fs::read_to_string("src/input/day05.txt")
        .expect("Should have been able to read the file");

    // 1. Parse input => list of ID-ranges + available IDs
    // The input format: ranges first (e.g., "3-5"), then empty line, then individual IDs
    let mut ranges: Vec<(u64, u64)> = Vec::new();
    let mut available_ids: Vec<u64> = Vec::new();
    let mut parsing_ranges = true;

    for line in contents.lines() {
        let trimmed = line.trim();

        // Empty line separates ranges from IDs
        if trimmed.is_empty() {
            parsing_ranges = false;
            continue;
        }

        if parsing_ranges {
            // Parse range format: "start-end"
            if let Some((start_str, end_str)) = trimmed.split_once('-') {
                let start: u64 = start_str.trim().parse().expect("Invalid range start");
                let end: u64 = end_str.trim().parse().expect("Invalid range end");
                ranges.push((start, end));
            }
        } else {
            // Parse individual ID
            let id: u64 = trimmed.parse().expect("Invalid ID");
            available_ids.push(id);
        }
    }

    // 2. For each available ID, check if it's contained in any range
    let mut fresh_count = 0;

    for id in &available_ids {
        // 3. Check if ID is contained in any ID-range
        let mut is_fresh = false;

        for (start, end) in &ranges {
            // Ranges are inclusive
            if id >= start && id <= end {
                is_fresh = true;
                break; // No need to check other ranges
            }
        }

        // 4. Increment counter if ID is in any range
        if is_fresh {
            fresh_count += 1;
        }
    }

    // 5. Print how many IDs are contained in those ranges
    println!("Fresh IDs (contained in ranges): {}", fresh_count);
}
