from __future__ import annotations

import logging
import re

import pandas as pd
from utils.config import EXCHANGE_RATE

logger = logging.getLogger(__name__)

DIRTY_PATTERNS: dict[str, list] = {
    "Title": ["Unknown Product"],
    "Rating": ["Invalid Rating / 5", "Not Rated"],
    "Price": ["Price Unavailable", None],
}


def _to_float_dollar(v: object) -> float | None:
    try:
        if v is None:
            return None
        s = str(v).replace(",", "")
        m = re.search(r"([0-9][0-9]*\.?\d*)", s)
        return float(m.group(1)) if m else None
    except Exception as e:
        logger.warning("_to_float_dollar failed for %r: %s", v, e)
        return None


def _to_rating(v: object) -> float | None:
    try:
        if v is None:
            return None
        m = re.search(r"(\d+(?:\.\d+)?)", str(v))
        return float(m.group(1)) if m else None
    except Exception as e:
        logger.warning("_to_rating failed for %r: %s", v, e)
        return None


def _to_int(v: object) -> int | None:
    try:
        if v is None:
            return None
        m = re.search(r"(\d+)", str(v))
        return int(m.group(1)) if m else None
    except Exception as e:
        logger.warning("_to_int failed for %r: %s", v, e)
        return None


def _strip_prefix(s: object, prefix: str) -> str:
    try:
        return ("" if s is None else str(s)).replace(prefix, "").strip()
    except Exception as e:
        logger.warning("_strip_prefix failed for %r: %s", s, e)
        return ""


def clean_transform(rows) -> pd.DataFrame:
    try:
        if rows is None:
            return pd.DataFrame(
                columns=["Title", "Price", "Rating", "Colors", "Size", "Gender", "Timestamp"]
            )

        products_df = pd.DataFrame(rows)

        for col, bads in DIRTY_PATTERNS.items():
            if col in products_df.columns:
                products_df = products_df[~products_df[col].isin(bads)]

        products_df["Price"] = products_df["Price"].apply(_to_float_dollar)
        products_df["Rating"] = products_df["Rating"].apply(_to_rating)
        products_df["Colors"] = products_df["Colors"].apply(_to_int)
        products_df["Size"] = products_df["Size"].apply(lambda s: _strip_prefix(s, "Size:"))
        products_df["Gender"] = products_df["Gender"].apply(lambda s: _strip_prefix(s, "Gender:"))
        products_df["Title"] = products_df["Title"].astype(str).str.strip()

        products_df = products_df.dropna(
            subset=["Price", "Rating", "Colors", "Size", "Gender", "Title"]
        )

        products_df["Price"] = products_df["Price"].astype("float64") * EXCHANGE_RATE
        products_df["Rating"] = products_df["Rating"].astype("float64")
        products_df["Colors"] = products_df["Colors"].astype("int64")

        if "Timestamp" not in products_df.columns:
            products_df["Timestamp"] = pd.NA

        products_df = products_df.drop_duplicates(subset=["Title", "Price", "Size", "Gender"])

        products_df = products_df[
            ["Title", "Price", "Rating", "Colors", "Size", "Gender", "Timestamp"]
        ]

        products_df["Title"] = products_df["Title"].astype("object")
        products_df["Size"] = products_df["Size"].astype("object")
        products_df["Gender"] = products_df["Gender"].astype("object")
        return products_df

    except Exception as e:
        logger.error("clean_transform failed: %s", e)
        # fallback aman: kembalikan DataFrame kosong dengan skema yang benar
        return pd.DataFrame(
            columns=["Title", "Price", "Rating", "Colors", "Size", "Gender", "Timestamp"]
        )
