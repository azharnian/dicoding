use anyhow::{bail, Result};

use crate::units::{LenUnit, TempUnit, Unit};

pub fn convert_value(value: f64, from: Unit, to: Unit) -> Result<f64> {
    if from.category() != to.category() {
        bail!(
            "[ERROR] Tidak dapat mengonversi satuan yang berbeda kategori: [{}] {} → [{}] {}",
            from.category(),
            from.display_name(),
            to.category(),
            to.display_name()
        );
    }

    match (from, to) {
        (Unit::T(TempUnit::Celsius), Unit::T(TempUnit::Fahrenheit)) => Ok(value * 9.0 / 5.0 + 32.0),
        (Unit::T(TempUnit::Fahrenheit), Unit::T(TempUnit::Celsius)) => {
            Ok((value - 32.0) * 5.0 / 9.0)
        }
        (Unit::T(TempUnit::Celsius), Unit::T(TempUnit::Kelvin)) => Ok(value + 273.15),
        (Unit::T(TempUnit::Kelvin), Unit::T(TempUnit::Celsius)) => Ok(value - 273.15),
        (Unit::T(TempUnit::Fahrenheit), Unit::T(TempUnit::Kelvin)) => {
            Ok((value - 32.0) * 5.0 / 9.0 + 273.15)
        }
        (Unit::T(TempUnit::Kelvin), Unit::T(TempUnit::Fahrenheit)) => {
            Ok((value - 273.15) * 9.0 / 5.0 + 32.0)
        }

        (Unit::L(f), Unit::L(t)) => {
            let to_cm = |v: f64, u: LenUnit| -> f64 {
                match u {
                    LenUnit::Cm => v,
                    LenUnit::Inch => v * 2.54,       // 1 inch = 2.54 cm
                    LenUnit::Km => v * 100_000.0,    // 1 km = 100000 cm
                    LenUnit::Miles => v * 160_934.4, // 1 mile ≈ 160934.4 cm
                }
            };
            let from_cm = |v_cm: f64, u: LenUnit| -> f64 {
                match u {
                    LenUnit::Cm => v_cm,
                    LenUnit::Inch => v_cm / 2.54,
                    LenUnit::Km => v_cm / 100_000.0,
                    LenUnit::Miles => v_cm / 160_934.4,
                }
            };

            let cm_val = to_cm(value, f);
            Ok(from_cm(cm_val, t))
        }

        _ if from == to => Ok(value),

        _ => unreachable!("Kasus konversi tidak terduga"),
    }
}
