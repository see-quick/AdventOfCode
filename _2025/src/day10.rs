// High-leve...
//
// Each machine has indicator lights where we initially set to `off`.
// Since pressing a button twice is equivalent to not pressing it (basically XOR toggle) => each button is pressed 0 or 1 times
// Each button toggles specific lights.
// Find minimum total button presses across all machines.
// =======
// We Brute force is 2^n combinations as we already know that pressing a button twice = not pressing it (XOR)

fn parse_line(line: &str) -> (Vec<bool>, Vec<Vec<usize>>) {
    let bracket_start = line.find('[').unwrap();
    let bracket_end = line.find(']').unwrap();
    let target_str = &line[bracket_start + 1..bracket_end];
    let target: Vec<bool> = target_str.chars().map(|c| c == '#').collect();

    let mut buttons: Vec<Vec<usize>> = Vec::new();
    let mut rest = &line[bracket_end + 1..];

    while let Some(paren_start) = rest.find('(') {
        let paren_end = rest.find(')').unwrap();
        let button_str = &rest[paren_start + 1..paren_end];

        let indices: Vec<usize> = button_str
            .split(',')
            .map(|s| s.trim().parse().unwrap())
            .collect();
        buttons.push(indices);

        rest = &rest[paren_end + 1..];
    }

    (target, buttons)
}

fn solve_machine(target: &[bool], buttons: &[Vec<usize>]) -> u64 {
    let num_lights = target.len();
    let num_buttons = buttons.len();

    // 1st Brute force all combinations of button presses
    let mut min_presses = u64::MAX;

    for mask in 0..(1u64 << num_buttons) {
        // 2nd Start with all lights off
        let mut lights = vec![false; num_lights];

        // 3rd Apply each button if its bit is set
        let mut presses = 0u64;
        for (i, button) in buttons.iter().enumerate() {
            if (mask >> i) & 1 == 1 {
                presses += 1;
                for &idx in button {
                    lights[idx] = !lights[idx];
                }
            }
        }

        // 4th Check if we reached the target
        if lights == target {
            min_presses = min_presses.min(presses);
        }
    }

    min_presses
}

fn solve(contents: &str) -> u64 {
    let mut total = 0;

    for line in contents.lines() {
        let line = line.trim();
        if line.is_empty() {
            continue;
        }

        let (target, buttons) = parse_line(line);
        let min_presses = solve_machine(&target, &buttons);
        total += min_presses;
    }

    total
}

fn main() {
    let contents = std::fs::read_to_string("src/input/day10.txt")
        .expect("Should have been able to read the file");

    let result = solve(&contents);
    println!("Result: {}", result);
}