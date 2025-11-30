#[derive(Debug, Clone, Copy, PartialEq)]
pub enum TempUnit {
    Celsius,
    Fahrenheit,
    Kelvin,
}

impl TempUnit {
    pub fn symbol(&self) -> &'static str {
        match self {
            TempUnit::Celsius => "°C",
            TempUnit::Fahrenheit => "°F",
            TempUnit::Kelvin => "K",
        }
    }
    pub fn name(&self) -> &'static str {
        match self {
            TempUnit::Celsius => "celsius",
            TempUnit::Fahrenheit => "fahrenheit",
            TempUnit::Kelvin => "kelvin",
        }
    }
}
