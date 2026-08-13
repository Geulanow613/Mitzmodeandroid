#!/usr/bin/env python3
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
cat = json.load(open(ROOT / "data/translation-catalog/strings.json", encoding="utf-8"))["strings"]
subs = [
    "Learn about shechitah",
    "Oneg Shabbat is delighting",
    "Oneg Shabbat — Oneg Shabbat",
    "Plag hamincha is one and a quarter",
    "Plag HaMincha — Plag hamincha",
    "Purim celebrates salvation",
    "Purim — Purim celebrates",
    "Today is $holidayName (Yom Tov",
    'Tap the\n"Mitzvah Me"',
    "honoring your parents — Kibud",
    "seudat mitzvah — festive",
    "Get ready for a wake-up call",
    "Get ready to experience the Sukkah",
    "Ready to launch into Shabbat",
    "Learn about Shemittah",
    "Spread some smiles",
    "Get a Chumash and open",
    "Tzedakah is usually translated",
]
for i, s in enumerate(subs):
    k = next(x for x in cat if x.startswith(s) or s in x[:80])
    (ROOT / "tools" / f"_batch29_en_{i}.txt").write_text(k, encoding="utf-8")
    print(i, len(k), k[:70])
