#!/usr/bin/env python3
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
cat = json.load(open(ROOT / "data/translation-catalog/strings.json", encoding="utf-8"))["strings"]
subs = [
    "Maaser kesafim is setting",
    "Machatzit HaShekel recalls",
    "Machatzit HaShekel — Machatzit",
    "Niddah is the state",
    "Nusach Ari is the prayer",
    "Nusach Ari — Nusach Ari",
    "Practice the power of positive speech",
    "Rabbi Yisrael Meir Kagan (Chofetz Chaim) wrote",
    "Rechilut is carrying tales",
    "Shekhinah — The Shekhinah",
    "Shema al HaMitah is the bedtime",
    "Shema al HaMitah — Shema al HaMitah",
    "Shema — The Shema",
    "Sukkot is seven days",
    "Sukkot — Sukkot is seven",
    "Tehillim (the Hebrew word",
    'Torah means "teaching"',
    "Torah — Torah means",
    "Yichud is the prohibition",
    "bedikat chametz — Bedikat chametz",
    "bitachon — Bitachon is trust",
]
for i, s in enumerate(subs):
    k = next(x for x in cat if x.startswith(s))
    (ROOT / "tools" / f"_batch25_en_{i}.txt").write_text(k, encoding="utf-8")
    print(i, len(k))
