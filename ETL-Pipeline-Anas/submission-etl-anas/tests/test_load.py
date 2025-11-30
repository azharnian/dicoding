import os

import pandas as pd

from utils.load import to_csv


def test_to_csv_tmp(tmp_path):
    frame = pd.DataFrame(
        [
            {
                "Title": "A",
                "Price": 1.0,
                "Rating": 4.0,
                "Colors": 1,
                "Size": "M",
                "Gender": "Men",
                "Timestamp": "t",
            }
        ]
    )
    out = tmp_path / "x.csv"
    path = to_csv(frame, str(out))
    assert os.path.exists(path)
