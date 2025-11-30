mod cli;
mod convert;
mod format;
mod history;
mod units;

use anyhow::Result;
use clap::Parser;

use cli::{Cli, Commands};
use convert::convert_value;
use format::{print_length_dst, print_length_src, print_temperature_dst, print_temperature_src};
use history::{append_history, read_history, ConversionRecord};
use units::{parse_unit, Unit};

fn main() {
    if let Err(e) = run() {
        eprintln!("Error: {}", e);
        std::process::exit(1);
    }
}

fn run() -> Result<()> {
    let cli = Cli::parse();
    match cli.command {
        Commands::Convert { from, to, value } => {
            let from_u = parse_unit(&from, "asal")?;
            let to_u = parse_unit(&to, "tujuan")?;
            let out = convert_value(value, from_u, to_u)?;

            match (from_u, to_u) {
                (Unit::T(fu), Unit::T(tu)) => {
                    println!(
                        "{} = {}",
                        print_temperature_src(value, fu),
                        print_temperature_dst(out, tu)
                    );
                }
                (Unit::L(fu), Unit::L(tu)) => {
                    println!("{} = {}", print_length_src(value, fu), print_length_dst(out, tu));
                }
                _ => unreachable!(),
            }

            append_history(ConversionRecord::new(from_u, to_u, value, out))?;
        }

        Commands::List => {
            println!("\nSatuan yang didukung:");
            println!("1. [suhu] celsius");
            println!("2. [suhu] fahrenheit");
            println!("3. [suhu] kelvin");
            println!("4. [panjang] cm");
            println!("5. [panjang] inch");
            println!("6. [panjang] km");
            println!("7. [panjang] miles");
        }

        Commands::History => {
            let hist = read_history()?;
            println!("\nRiwayat Konversi:");
            if hist.is_empty() {
                println!("(kosong)");
            } else {
                for (i, r) in hist.iter().enumerate() {
                    if let (Ok(fu), Ok(tu)) =
                        (parse_unit(&r.from_unit, "asal"), parse_unit(&r.to_unit, "tujuan"))
                    {
                        match (fu, tu) {
                            (Unit::T(fu), Unit::T(tu)) => {
                                println!(
                                    "{}. {} = {}",
                                    i + 1,
                                    print_temperature_src(r.input, fu),
                                    print_temperature_dst(r.output, tu)
                                );
                            }
                            (Unit::L(fu), Unit::L(tu)) => {
                                println!(
                                    "{}. {} = {}",
                                    i + 1,
                                    print_length_src(r.input, fu),
                                    print_length_dst(r.output, tu)
                                );
                            }
                            _ => println!(
                                "{}. {} {} = {} {}",
                                i + 1,
                                r.input,
                                r.from_unit,
                                r.output,
                                r.to_unit
                            ),
                        }
                    } else {
                        println!(
                            "{}. {} {} = {} {}",
                            i + 1,
                            r.input,
                            r.from_unit,
                            r.output,
                            r.to_unit
                        );
                    }
                }
            }
        }
    }
    Ok(())
}
