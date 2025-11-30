use clap::{Parser, Subcommand};

#[derive(Parser, Debug)]
#[command(name = "unitconv", version, about = "Konversi satuan sederhana (suhu & panjang)")]
pub struct Cli {
    #[command(subcommand)]
    pub command: Commands,
}

#[derive(Subcommand, Debug)]
pub enum Commands {
    Convert {
        #[arg(long = "from")]
        from: String,
        #[arg(long = "to")]
        to: String,
        #[arg(long = "value")]
        value: f64,
    },
    List,
    History,
}
