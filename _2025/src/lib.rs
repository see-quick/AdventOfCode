use std::fmt::Display;
use std::fs;
use std::path::Path;

/// Trait defining the contract for each Advent of Code day solution.
///
/// Each day must implement:
/// - `parse`: Convert raw input string into the day's data structure
/// - `part1`: Solve part 1 of the puzzle
/// - `part2`: Solve part 2 of the puzzle
pub trait Day: Sized {
    type Output1: Display;
    type Output2: Display;

    fn parse(input: &str) -> Self;
    fn part1(&self) -> Self::Output1;
    fn part2(&self) -> Self::Output2;
}

/// Run a day's solution with the given input file path
pub fn run<D: Day>(input_path: impl AsRef<Path>) {
    let contents = fs::read_to_string(input_path.as_ref())
        .expect("Should have been able to read the file");

    let day = D::parse(&contents);

    println!("Part 1: {}", day.part1());
    println!("Part 2: {}", day.part2());
}