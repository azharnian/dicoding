# ETL Fashion Studio (Modular ETL + Tests) Dicoding Submission

ETL pipeline modular (Extract → Transform → Load) untuk scraping produk dari **https://fashion-studio.dicoding.dev**.  

## Struktur Proyek
```
submission-etl-anas/
├── docker-compose.yml
├── google-sheets-api.json
├── main.py
├── products.csv
├── pyproject.toml
├── README.md
├── requirements.txt
├── src
│   └── submission_etl_anas
│       └── __init__.py
├── tests
│   ├── test_extract.py
│   ├── test_load.py
│   └── test_transform.py
├── utils
│   ├── __init__.py
│   ├── extract.py
│   ├── load.py
│   └── transform.py
└── uv.lock
```

## Setup Cepat

### 1) Clone & inisialisasi
```bash
git clone <repo-url> && cd submission-etl-anas
uv sync
```

### 2) Buat `.env`
```env
PG_CONN=postgresql+psycopg2://etluser:etlpass@localhost:5432/etldb
GSHEET_ID=
GSHEET_NAME=ETL Fashion Studio
GSHEET_SHEET=Products
```

### 3) Postgres di Docker
```yaml
services:
  db:
    image: postgres:16
    container_name: etl_pg
    environment:
      POSTGRES_USER: etluser
      POSTGRES_PASSWORD: etlpass
      POSTGRES_DB: etldb
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data
volumes:
  pgdata:
```

## Menjalankan ETL
```bash
uv run main.py
```

## Unit Test
```bash
uv run pytest -q
uv run pytest --cov=utils --cov=main --cov-report=term-missing
```

## Export GSheet 
**https://docs.google.com/spreadsheets/d/19fhWk343acST8zoqXTuuis0Dx6sXJfZxYd8vVSYWXYw/edit?usp=sharing**