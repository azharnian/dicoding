from __future__ import annotations
import os
from pathlib import Path
from dotenv import load_dotenv

ROOT = Path(__file__).resolve().parents[1]
load_dotenv(ROOT / ".env")

PG_CONN = os.getenv("PG_CONN") or os.getenv("PGCONN")

GSHEET_ID = os.getenv("GSHEET_ID")
GSHEET_NAME = os.getenv("GSHEET_NAME", "ETL Fashion Studio")
GSHEET_SHEET = os.getenv("GSHEET_SHEET", "Products")

EXCHANGE_RATE = int(os.getenv("EXCHANGE_RATE", "16000"))
