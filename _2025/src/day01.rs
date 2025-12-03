// Top-level design
//
// So we start with number 50, and in the input.txt we have items in each row like:
//    L10
//    R13
//    L1
//    R99
// and we need to count: "How many times we reach concretely `0`".
//
// So, I am thinking about init=50 and then just sequentially go thought numbers and if L (minus),
// else R (plus). But also overall Universum is 99 (circle buffer) => so we need to make sure that
// if we plus 90 + 90 then go 80 etc.
//
// Overall algorithm should be T(N) and S(1).

const UNIVERSE_SIZE: i32 = 100;
const START_POSITION: i32 = 50;

fn main() {
    let contents = std::fs::read_to_string("src/input/day01.txt")
        .expect("Should have been able to read the file");

    let mut position = START_POSITION;
    let mut zero_count = 0;

    for line in contents.lines() {
        let line = line.trim();
        if line.is_empty() {
            continue;
        }

        let direction = &line[0..1];
        let value: i32 = line[1..].parse().expect("Invalid number in input");

        match direction {
            "L" => position -= value,
            "R" => position += value,
            _ => panic!("Unknown direction: {}", direction),
        }

        // Wrap around the circular buffer (modulo that handles negatives)
        position = ((position % UNIVERSE_SIZE) + UNIVERSE_SIZE) % UNIVERSE_SIZE;

        if position == 0 {
            zero_count += 1;
        }
    }

    println!("Times we reached 0: {}", zero_count);
}
