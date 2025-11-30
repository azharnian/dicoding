mod category;
mod length;
mod temperature;

pub use category::Category;
pub use length::LenUnit;
pub use temperature::TempUnit;

use anyhow::{bail, Result};

#[derive(Debug, Clone, Copy, PartialEq)]
pub enum Unit {
    T(TempUnit),
    L(LenUnit),
}

impl Unit {
    pub fn category(&self) -> Category {
        match self {
            Unit::T(_) => Category::Temperature,
            Unit::L(_) => Category::Length,
        }
    }
    pub fn display_name(&self) -> &'static str {
        match self {
            Unit::T(t) => t.name(),
            Unit::L(l) => l.name(),
        }
    }
}

pub fn parse_unit(s: &str, role: &str) -> Result<Unit> {
    let lower = s.to_ascii_lowercase();
    let u = match lower.as_str() {
        "c" | "celsius" | "celcius" => Some(Unit::T(TempUnit::Celsius)), // toleransi ejaan "celcius"
        "f" | "fahrenheit" => Some(Unit::T(TempUnit::Fahrenheit)),
        "k" | "kelvin" => Some(Unit::T(TempUnit::Kelvin)),
        "cm" => Some(Unit::L(LenUnit::Cm)),
        "inch" | "in" => Some(Unit::L(LenUnit::Inch)),
        "km" => Some(Unit::L(LenUnit::Km)),
        "mile" | "miles" => Some(Unit::L(LenUnit::Miles)),
        _ => None,
    };

    match u {
        Some(u) => Ok(u),
        None => bail!("[ERROR] Satuan {role} '{}' tidak dikenali.", s),
    }
}
