use anyhow::{Context, Result};
use chrono::{DateTime, Local};
use serde::{Deserialize, Serialize};
use std::fs;
use std::path::Path;

use crate::units::Unit;

const HISTORY_FILE: &str = "conversion.json";

#[derive(Debug, Serialize, Deserialize)]
pub struct ConversionRecord {
    pub timestamp: DateTime<Local>,
    pub from_unit: String,
    pub to_unit: String,
    pub input: f64,
    pub output: f64,
}

impl ConversionRecord {
    pub fn new(from: Unit, to: Unit, input: f64, output: f64) -> Self {
        Self {
            timestamp: Local::now(),
            from_unit: from.display_name().to_string(),
            to_unit: to.display_name().to_string(),
            input,
            output,
        }
    }
}

pub fn read_history() -> Result<Vec<ConversionRecord>> {
    if !Path::new(HISTORY_FILE).exists() {
        return Ok(Vec::new());
    }
    let data = fs::read_to_string(HISTORY_FILE)
        .with_context(|| format!("Gagal membaca {}", HISTORY_FILE))?;
    let v: Vec<ConversionRecord> = serde_json::from_str(&data)
        .with_context(|| format!("Format JSON tidak valid pada {}", HISTORY_FILE))?;
    Ok(v)
}

pub fn append_history(rec: ConversionRecord) -> Result<()> {
    let mut all = read_history().unwrap_or_default();
    all.push(rec);
    let json = serde_json::to_string_pretty(&all)?;
    fs::write(HISTORY_FILE, json).with_context(|| format!("Gagal menulis {}", HISTORY_FILE))?;
    Ok(())
}
