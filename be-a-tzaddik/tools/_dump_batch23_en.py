#!/usr/bin/env python3
import json
from pathlib import Path
ROOT = Path(__file__).resolve().parents[1]
cat = json.load(open(ROOT / "data/translation-catalog/strings.json", encoding="utf-8"))["strings"]
subs = [
    "A minhag is a binding",
    "Amen affirms a bracha",
    "Amen — Amen affirms",
    "Bishul is cooking on Shabbat",
    "Bishul — Bishul is cooking",
    "Choose your friends wisely",
    "Connect to our history",
    "Diaspora — The Diaspora",
    "The Diaspora (Galut) means",
    "Dive into Kabbalah",
    "Dive into wisdom",
    "Double your mitzvah joy",
    "Explore the foundations of faith",
    "Make every day special",
    "Chabad follows strict",
    "The Three Weeks (",
    "A Chumash (from chamesh",
]
for i, s in enumerate(subs):
    k = next(x for x in cat if x.startswith(s))
    (ROOT / "tools" / f"_batch23_en_{i}.txt").write_text(k, encoding="utf-8")
    print(i, s, len(k))
