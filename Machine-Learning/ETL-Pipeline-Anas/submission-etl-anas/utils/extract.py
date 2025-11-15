from __future__ import annotations

import re
import time
from collections.abc import Iterable
from datetime import UTC, datetime

import requests
from bs4 import BeautifulSoup, Tag

HEADERS = {
    "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124 Safari/537.36",
    "Accept-Language": "en-US,en;q=0.9,id;q=0.8",
}

PRODUCT_CARD_SELECTORS: Iterable[str] = (
    ".product-card",
    ".card.product",
    ".product",
    "article.product",
    "li.product-item",
    "div[data-role='product']",
)


def _parse_card(card: Tag) -> dict | None:
    text = " ".join(card.stripped_strings)
    title = None
    for sel in ("h3", "h2", ".title", "a[title]", "a"):
        el = card.select_one(sel)
        if el and el.get_text(strip=True):
            title = el.get_text(strip=True)
            break
    if not title:
        m = re.search(r"(?:T-?shirt|Hoodie|Outerwear|Jacket|Pants|Crewneck)\s*\d+", text, re.I)
        title = m.group(0) if m else None
    price_raw = None
    m = re.search(r"\$\s*([0-9][0-9,]*\.?\d*)", text)
    if m:
        price_raw = f"${m.group(1)}"
    elif "Price Unavailable" in text:
        price_raw = "Price Unavailable"
    rating_raw = None
    m = re.search(r"(\d+(?:\.\d+)?)\s*/\s*5", text)
    if m:
        rating_raw = f"{m.group(1)} / 5"
    if "Not Rated" in text:
        rating_raw = "Not Rated"
    if "Invalid Rating" in text:
        rating_raw = "Invalid Rating / 5"
    colors_raw = None
    m = re.search(r"(\d+)\s*Colors?", text, re.I)
    if m:
        colors_raw = m.group(1)
    size_raw = None
    m = re.search(r"Size:\s*([XSML]{1,3}|XL|XXL|XXXL)", text, re.I)
    if m:
        size_raw = f"Size: {m.group(1)}"
    gender_raw = None
    m = re.search(r"Gender:\s*(Women|Men|Unisex)", text, re.I)
    if m:
        gender_raw = f"Gender: {m.group(1)}"
    return {
        "Title": title or "Unknown Product",
        "Price": price_raw or "Price Unavailable",
        "Rating": rating_raw or "Not Rated",
        "Colors": colors_raw or "0",
        "Size": size_raw or "Size: -",
        "Gender": gender_raw or "Gender: -",
    }


def _find_cards(soup: BeautifulSoup) -> list[Tag]:
    for sel in PRODUCT_CARD_SELECTORS:
        cards = soup.select(sel)
        if cards:
            return cards
    candidates = []
    for blk in soup.find_all(text=re.compile(r"\$")):
        if isinstance(blk, Tag):
            candidates.append(blk)
        elif blk.parent:
            candidates.append(blk.parent)
    cards = []
    for c in candidates:
        node = c
        for _ in range(3):
            if hasattr(node, "get_text") and any(
                k in node.get_text(" ") for k in ("Size:", "Gender:", "Colors")
            ):
                cards.append(node)
                break
            if node.parent:
                node = node.parent
    return cards


def _request_page(url: str, timeout: int = 20) -> BeautifulSoup:
    resp = requests.get(url, headers=HEADERS, timeout=timeout)
    resp.raise_for_status()
    return BeautifulSoup(resp.text, "html.parser")


def scrape_page(base_url: str, page: int, timeout: int = 20) -> list[dict]:
    base = base_url.rstrip("/")
    url = f"{base}" if page == 1 else f"{base}/page{page}"
    try:
        soup = _request_page(url, timeout=timeout)
        cards = _find_cards(soup)
        if not cards:
            items = []
            page_text = soup.get_text(" ", strip=True)
            chunks = re.split(r"(?=Size:\s*)", page_text)
            for ch in chunks:
                if "$" in ch and ("Gender:" in ch or "Colors" in ch):
                    fake_tag = soup.new_tag("div")
                    fake_tag.string = ch
                    parsed = _parse_card(fake_tag)
                    if parsed:
                        items.append(parsed)
            return items
        results = []
        for card in cards:
            try:
                item = _parse_card(card)
                if item:
                    results.append(item)
            except Exception:
                continue
        return results
    except requests.exceptions.RequestException as e:
        print(f"[scrape_page] {e}")
        return []


def scrape_main(
    base_url: str, start_page: int = 1, end_page: int = 50, delay_sec: float = 0.2
) -> list[dict] | None:
    products: list[dict] = []
    try:
        ts = datetime.now(UTC).isoformat()
        for p in range(start_page, end_page + 1):
            items = scrape_page(base_url, p)
            if not items:
                if p > start_page and not products:
                    print(f"[WARN] page {p} juga kosong. Cek selector/HTML situs.")
            for it in items:
                it["Timestamp"] = ts
            products.extend(items)
            time.sleep(delay_sec)
        return products
    except Exception as e:
        print(f"An error occurred during scraping: {e}")
        return None


def debug_fetch(base_url: str = "https://fashion-studio.dicoding.dev", page: int = 1):
    base = base_url.rstrip("/")
    url = f"{base}" if page == 1 else f"{base}/page{page}"
    try:
        soup = _request_page(url)
        html_len = len(soup.prettify())
        cards = _find_cards(soup)
        print(f"[DEBUG] URL={url}\n  html_len={html_len}\n  cards_found={len(cards)}")
        if cards[:1]:
            print("  sample_text:", " ".join(cards[0].stripped_strings)[:180], "...")
    except Exception as e:
        print(f"[DEBUG] URL={url} error: {e}")
