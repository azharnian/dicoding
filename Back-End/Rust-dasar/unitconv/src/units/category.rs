use std::fmt::{self, Display};

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum Category {
    Temperature,
    Length,
}

impl Display for Category {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            Category::Temperature => write!(f, "suhu"),
            Category::Length => write!(f, "panjang"),
        }
    }
}
