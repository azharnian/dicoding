from __future__ import annotations

import logging
import os

import gspread
import pandas as pd
from google.oauth2.service_account import Credentials
from sqlalchemy import create_engine
from utils.config import GSHEET_ID

logger = logging.getLogger(__name__)


def to_csv(df: pd.DataFrame, path: str = "products.csv") -> str:
    try:
        df.to_csv(path, index=False)
        return os.path.abspath(path)
    except Exception as e:
        logger.error("to_csv failed (path=%s): %s", path, e)
        raise


def to_google_sheets(
    df: pd.DataFrame,
    spreadsheet_name: str,
    worksheet_name: str = "Products",
    service_json_path: str = "google-sheets-api.json",
) -> str | None:
    try:
        scopes = [
            "https://www.googleapis.com/auth/spreadsheets",
            "https://www.googleapis.com/auth/drive",
        ]
        creds = Credentials.from_service_account_file(service_json_path, scopes=scopes)
        gc = gspread.authorize(creds)

        gsheet_id = GSHEET_ID
        sh = None
        if gsheet_id:
            sh = gc.open_by_key(gsheet_id)
        else:
            try:
                sh = gc.open(spreadsheet_name)
            except gspread.SpreadsheetNotFound:
                try:
                    sh = gc.create(spreadsheet_name)
                except Exception as e:
                    msg = str(e).lower()
                    if "quota" in msg or "exceeded" in msg:
                        logger.error(
                            "[LOAD][GSHEETS] Quota penuh saat create. "
                            "Buat sheet manual, share ke service account, lalu set GSHEET_ID di .env."
                        )
                        return None
                    raise

        try:
            ws = sh.worksheet(worksheet_name)
        except gspread.WorksheetNotFound:
            ws = sh.add_worksheet(title=worksheet_name, rows="1000", cols="20")

        ws.clear()
        ws.update([df.columns.tolist()] + df.astype(str).to_numpy().tolist())
        return sh.url

    except Exception as e:
        logger.error(f"[LOAD][GSHEETS] gagal: {e}")
        return None


def to_postgres(
    df: pd.DataFrame, conn_str: str, table_name: str = "products", if_exists: str = "replace"
) -> bool:
    try:
        engine = create_engine(conn_str)
        df.to_sql(table_name, engine, index=False, if_exists=if_exists)
        return True
    except Exception as e:
        logger.error(f"[LOAD][POSTGRES] gagal: {e}")
        return False
