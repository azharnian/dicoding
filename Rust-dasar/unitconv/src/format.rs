use crate::units::{LenUnit, TempUnit};

pub fn print_temperature_src(value: f64, unit: TempUnit) -> String {
    format!("{} {}", fmt_num_src(value), unit.symbol())
}
pub fn print_temperature_dst(value: f64, unit: TempUnit) -> String {
    format!("{} {}", fmt_num_dst(value), unit.symbol())
}

pub fn print_length_src(value: f64, unit: LenUnit) -> String {
    format!("{} {}", fmt_num_src(value), unit.name())
}
pub fn print_length_dst(value: f64, unit: LenUnit) -> String {
    format!("{} {}", fmt_num_dst(value), unit.name())
}

fn fmt_num_src(value: f64) -> String {
    let mut s = format!("{:.4}", value);
    if s.find('.').is_some() {
        while s.ends_with('0') {
            s.pop();
        }
        if s.ends_with('.') {
            s.pop();
        }
    }
    s
}

fn fmt_num_dst(value: f64) -> String {
    let mut s = format!("{:.4}", value);
    if s.find('.').is_some() {
        while s.ends_with('0') {
            s.pop();
        }
        if s.ends_with('.') {
            s.push('0');
        }
    } else {
        s.push_str(".0");
    }
    s
}
