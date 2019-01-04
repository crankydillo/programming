use std::io::{BufReader, BufWriter};
use std::fs::File;
use std::io::prelude::*;

fn main() -> std::io::Result<()> {

    let input = File::open("../A-large-practice.in")?;
    let reader = BufReader::new(input);
    let mut output = File::create("./results")?;
    let lines = reader.lines().map(|l| l.unwrap()).collect::<Vec<_>>(); // I don't really want to do this, but time is short

    let results = lines.iter().enumerate().skip(1).map(|(ctr, line)| {
        let (pancakes, flip_size) = parse(line);
        let num_flips = cook(pancakes, flip_size);
        let result = num_flips.map_or("IMPOSSIBLE".to_string(), |flips| flips.to_string());
        let output_line = format!("Case #{}: {}", ctr, result);
        output_line
    });

    let data = results.into_iter().map(|l| l + "\n").collect::<String>();
    println!("{}", data);
    output.write_all(data.as_bytes())?;
    Ok(())
}

fn test() {
    // Test scenarios (not fooling with unit tests)
    // pancakes, flip_size, expected value
    let test_collateral = vec!(
        (vec!('-', '-', '-', '+', '-', '+', '+', '-'), 3, Some(3)),
        (vec!('-', '+', '-', '+', '-')               , 4, None),
        (vec!('+', '+', '+', '+', '+')               , 4, Some(0))
    );

    for (pancakes, flip_size, expected_result) in test_collateral {
        let num_flips = flip(to_bool_vec(&pancakes), 0, pancakes.len() - 1, flip_size, 0);
        println!("{:?}", num_flips == expected_result);
    }
}

fn parse(line: &String) -> (Vec<char>, usize) {
    let vec = line.split(" ").collect::<Vec<_>>();
    let pancake_str = vec[0];
    let flip_size_str = vec[1];
    (pancake_str.chars().collect::<Vec<_>>(), flip_size_str.parse().unwrap())
}

fn to_bool_vec(pancakes: &Vec<char>) -> Vec<bool> {
    pancakes.iter().map(|&c| c == '+').collect::<Vec<_>>()
}

fn cook(pancakes: Vec<char>, flip_size: usize) -> Option<usize> {
    flip(to_bool_vec(&pancakes), flip_size, 0, pancakes.len() - 1, 0)
}

fn flip(mut pancakes: Vec<bool>,
        flip_size: usize,
        left_idx: usize,
        right_idx: usize,
        num_flips: usize) -> Option<usize> {

    if pancakes.iter().all(|&p| p) {
        Some(num_flips)
    } else if left_idx >= right_idx {
        None
    } else {
        let mut flip_ctr = 0;
        if !pancakes[left_idx] {
            flip_ctr += 1;
            for i in left_idx..(left_idx + flip_size) {
                if i < pancakes.len() {
                    pancakes[i] = !pancakes[i];
                }
            }
        }
        if !pancakes[right_idx] {
            flip_ctr += 1;
            for i in 0..flip_size {
                if i <= right_idx {
                    pancakes[right_idx - i] = !pancakes[right_idx - i];
                }
            }
        }
        flip(pancakes, flip_size, left_idx + 1, right_idx - 1, num_flips + flip_ctr)
    }
}
