#!/usr/bin/env python3
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
cat = json.load(open(ROOT / "data/translation-catalog/strings.json", encoding="utf-8"))["strings"]
subs = [
    "Get ready to experience the Sukkah",
    "Learn about Shemittah",
    "Learn about the Torah obligation to TEACH",
    "Learn about the Torah's obligations of respect",
    "Learn about the divine command to have the best",
    "Learn about the joy of mitzvot! Take",
    "Learn about the world's first mandatory RSVP",
    "Learn some halacha about the Shema",
    "Practice Hakarat HaTov",
    "Purim joy includes everyone",
    "Ready to launch into Shabbat",
    "Return a lost item",
    "Saw a lost object recently",
    "Say/text something nice",
    "Serve Hashem with Simcha",
    "Strengthen your focus on G-d",
    "Want to be a superstar kid",
    "Oneg Shabbat is delighting",
    "bat mitzvah — Bat mitzvah",
    "seudat mitzvah — festive",
]
for i, s in enumerate(subs):
    k = next(x for x in cat if x.startswith(s))
    (ROOT / "tools" / f"_batch30_en_{i}.txt").write_text(k, encoding="utf-8")
    print(i, len(k))
