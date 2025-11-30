from utils.transform import EXCHANGE_RATE, clean_transform


def test_clean_transform_removes_invalid_and_converts_types():
    raw = [
        {
            "Title": "Unknown Product",
            "Price": "$10",
            "Rating": "4.0 / 5",
            "Colors": "3",
            "Size": "Size: M",
            "Gender": "Gender: Women",
            "Timestamp": "t",
        },
        {
            "Title": "Hoodie 3",
            "Price": "Price Unavailable",
            "Rating": "Not Rated",
            "Colors": "3",
            "Size": "Size: L",
            "Gender": "Gender: Unisex",
            "Timestamp": "t",
        },
        {
            "Title": "Pants 4",
            "Price": "$2.5",
            "Rating": "3.3 / 5",
            "Colors": "3",
            "Size": "Size: XL",
            "Gender": "Gender: Men",
            "Timestamp": "t",
        },
    ]
    cleaned = clean_transform(raw)
    assert len(cleaned) == 1
    row = cleaned.iloc[0]
    assert row["Title"] == "Pants 4"
    assert abs(row["Price"] - 2.5 * EXCHANGE_RATE) < 1e-6
    assert isinstance(row["Rating"].item(), float)
    assert isinstance(row["Colors"].item(), int)
    assert row["Size"] == "XL"
    assert row["Gender"] == "Men"
