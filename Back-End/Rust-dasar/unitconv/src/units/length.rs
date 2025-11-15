#[derive(Debug, Clone, Copy, PartialEq)]
pub enum LenUnit {
    Cm,
    Inch,
    Km,
    Miles,
}

impl LenUnit {
    pub fn name(&self) -> &'static str {
        match self {
            LenUnit::Cm => "cm",
            LenUnit::Inch => "inch",
            LenUnit::Km => "km",
            LenUnit::Miles => "miles",
        }
    }
}
