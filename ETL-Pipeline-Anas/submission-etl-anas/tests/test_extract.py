from utils import extract

HTML_SAMPLE = """
<div class="product-card">
  <h3>T-shirt 2</h3>
  <div>Price: $99.0</div>
  <div>4.8 / 5</div>
  <div>3 Colors</div>
  <div>Size: M</div>
  <div>Gender: Women</div>
</div>
"""


class FakeResp:
    def __init__(self, text):
        self.text = text

    def raise_for_status(self):
        pass


def test_scrape_page_monkeypatch(monkeypatch):
    def fake_get(url, headers=None, timeout=10):
        return FakeResp(HTML_SAMPLE)

    monkeypatch.setattr(extract.requests, "get", fake_get)
    items = extract.scrape_page("http://x", 1)
    assert len(items) == 1
    it = items[0]
    assert it["Title"] == "T-shirt 2"
    assert it["Price"] == "$99.0"
    assert it["Rating"].startswith("4.8")
    assert it["Colors"] == "3"
    assert it["Size"].startswith("Size:")
    assert it["Gender"].startswith("Gender:")


def test_scrape_main_timestamp(monkeypatch):
    def fake_page(base, p, timeout=10):
        return [
            {
                "Title": "T-shirt 2",
                "Price": "$1",
                "Rating": "4.0 / 5",
                "Colors": "1",
                "Size": "Size: S",
                "Gender": "Gender: Men",
            }
        ]

    monkeypatch.setattr(extract, "scrape_page", fake_page)
    rows = extract.scrape_main("http://x", 1, 2, 0)
    assert rows and len(rows) == 2
    assert "Timestamp" in rows[0]
