import os
from utils.extract import scrape_main
from utils.load import to_csv, to_google_sheets, to_postgres
from utils.transform import clean_transform
from utils.config import PG_CONN, GSHEET_NAME, GSHEET_SHEET

BASE_URL = "https://fashion-studio.dicoding.dev"


def run_pipeline():
    raw = scrape_main(BASE_URL, 1, 50, 0.1)
    if raw is None:
        print("Extract gagal")
        return 1
    print(f"[EXTRACT] items: {len(raw)}")

    products_df = clean_transform(raw)
    print(f"[TRANSFORM] rows: {len(products_df)}")
    print(products_df.head().to_string(index=False))
    print()
    print(products_df.info())

    csv_path = to_csv(products_df, "products.csv")
    print(f"[LOAD] CSV -> {csv_path}")

    if os.path.exists("google-sheets-api.json"):
        gsheet_name = GSHEET_NAME
        gsheet_sheet = GSHEET_SHEET
        url = to_google_sheets(products_df, gsheet_name, gsheet_sheet)
        if url:
            print(f"[LOAD] Google Sheets -> {url}")
    else:
        print("[LOAD] google-sheets-api.json tidak ditemukan - skip Sheets")

    pg_conn = PG_CONN
    if pg_conn:
        ok = to_postgres(products_df, pg_conn, "products", "replace")
        print(f"[LOAD] PostgreSQL: {ok}")
    else:
        print("[LOAD] Variabel PG_CONN/PGCONN tidak ditemukan - skip Postgres")

    return 0


if __name__ == "__main__":
    raise SystemExit(run_pipeline())
