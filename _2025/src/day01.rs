use _2025::{Day, run};
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

#[derive(Clone, Copy)]
enum Direction {
    Left,
    Right,
}

struct Rotation {
    direction: Direction,
    steps: i32,
}

pub struct Day01 {
    rotations: Vec<Rotation>,
}

impl Day for Day01 {
    type Output1 = i32;
    type Output2 = i32;

    fn parse(input: &str) -> Self {
        let rotations = input
            .lines()
            .filter_map(|line| {
                let line = line.trim();
                if line.is_empty() {
                    return None;
                }

                let direction = match &line[0..1] {
                    "L" => Direction::Left,
                    "R" => Direction::Right,
                    d => panic!("Unknown direction: {}", d),
                };
                let steps: i32 = line[1..].parse().expect("Invalid number in input");

                Some(Rotation { direction, steps })
            })
            .collect();

        Self { rotations }
    }

    /// Part 1: Count how many times the dial ends at 0 after each rotation
    fn part1(&self) -> Self::Output1 {
        let mut position = START_POSITION;
        let mut zero_count = 0;

        for rot in &self.rotations {
            match rot.direction {
                Direction::Left => position -= rot.steps,
                Direction::Right => position += rot.steps,
            }

            position = ((position % UNIVERSE_SIZE) + UNIVERSE_SIZE) % UNIVERSE_SIZE;

            if position == 0 {
                zero_count += 1;
            }
        }

        zero_count
    }

    /// Part 2: Count how many times the dial passes through 0 during any rotation
    fn part2(&self) -> Self::Output2 {
        let mut position = START_POSITION;
        let mut zero_count = 0;

        for rot in &self.rotations {
            zero_count += count_zero_crossings(position, rot.direction, rot.steps);

            match rot.direction {
                Direction::Left => position -= rot.steps,
                Direction::Right => position += rot.steps,
            }
            position = ((position % UNIVERSE_SIZE) + UNIVERSE_SIZE) % UNIVERSE_SIZE;
        }

        zero_count
    }
}

fn main() {
    run::<Day01>("src/input/day01.txt");
}

fn count_zero_crossings(position: i32, direction: Direction, steps: i32) -> i32 {
    if steps <= 0 {
        return 0;
    }

    let first_k = match direction {
        Direction::Right => {
            if position == 0 { UNIVERSE_SIZE } else { UNIVERSE_SIZE - position }
        }
        Direction::Left => {
            if position == 0 { UNIVERSE_SIZE } else { position }
        }
    };

    if steps >= first_k {
        (steps - first_k) / UNIVERSE_SIZE + 1
    } else {
        0
    }
}
