#!/usr/bin/env python3
"""Dump EN sources for batch 27 RU mitzvah-leak fixes."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
cat = json.load(open(ROOT / "data/translation-catalog/strings.json", encoding="utf-8"))["strings"]
subs = [
    "Bat mitzvah is when",
    "L'chatchila means",
    "Charoset is the sweet",
    "After the fast (10 Av)",
    "Add some sweetness to Shabbat",
    "Be a comfort to others",
    "Choose your words wisely",
    "Contemplate #5 of the Constant Mitzvot",
    "Discover #2 of the Constant Mitzvot",
    "Do the mitzvah of experiencing both love",
    "Experience #4 of the Constant Mitzvot",
    "Explore #3 of the Constant Mitzvot",
    "Learn the #1 Constant Mitzvah",
    "Learn about the joy of mitzvot",
    "Guard your body, mind, and soul",
    "Learn how to light Shabbat candles",
    "Fulfill the beautiful mitzvah of Mezuzot",
    "Learn about the mitzvah of Halvayat HaMeit",
]
for i, s in enumerate(subs):
    k = next(x for x in cat if x.startswith(s))
    (ROOT / "tools" / f"_batch27_en_{i}.txt").write_text(k, encoding="utf-8")
    print(i, len(k))
