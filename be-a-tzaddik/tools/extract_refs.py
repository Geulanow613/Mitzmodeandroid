#!/usr/bin/env python3
import json
import re
from pathlib import Path

MIXED = re.compile(r"[\u0590-\u05FF]+[A-Za-z]+|[A-Za-z]+[\u0590-\u05FF]+")
ROOT = Path(__file__).resolve().parent.parent / "data" / "translation-catalog"
OUT = Path(__file__).resolve().parent


def check(label, s):
    bad = MIXED.findall(s)
    print(f"{label}: len={len(s)} bad={len(bad)} {bad[:3]}")


raw = json.loads((ROOT / "shards" / "quality_fixes.json").read_text(encoding="utf-8"))
qf = raw.get("he", raw)
keys = [
    "Before eating or drinking",
    "Birkat Ha'Ilanot",
    "Chanukah night",
    "Chilul Hashem is",
    "Chilul Hashem —",
    "Cheshbon hanefesh",
    "Chitas",
    "Chofetz Chaim",
    "Chol HaMoed (",
    "Chol HaMoed Pesach",
    "Chol HaMoed — Chol",
    "Dayeinu",
    "Eating matzah",
    "Edot HaMizrach",
    "Birchot HaShachar",
    "Borei pri hagafen",
    "Chabad (Alter Rebbe",
    "Chabad follows the standard",
    "Chabad is a Chasidic",
    "Chabad — Chabad is",
]
for needle in keys:
    for k, v in qf.items():
        if needle in k and isinstance(v, str):
            check(needle[:30], v)
            (OUT / f"_ref_{needle[:12].replace(' ', '_')}.txt").write_text(v, encoding="utf-8")
            break
    else:
        print(f"MISSING: {needle}")
