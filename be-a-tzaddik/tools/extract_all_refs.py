#!/usr/bin/env python3
import json
import re
from pathlib import Path

M = re.compile(r"[\u0590-\u05FF]+[A-Za-z]+|[A-Za-z]+[\u0590-\u05FF]+")
M2 = re.compile(r"[A-Za-z]{2,}")
ROOT = Path(__file__).resolve().parent.parent / "data" / "translation-catalog"
qf = json.loads((ROOT / "shards" / "quality_fixes.json").read_text(encoding="utf-8"))["he"]
needles = {
    "bracha": "Before eating or drinking anything",
    "ilanot": "Birkat Ha'Ilanot",
    "chanukah": "Chanukah night $day",
    "chilul": "Chilul Hashem is desecrating",
    "chilul2": "Chilul Hashem \u2014 Chilul",
    "chitas": "Chitas (",
    "chofetz": "Chofetz Chaim",
    "chol": "Chol HaMoed (\u05d7",
    "chol2": "Chol HaMoed Pesach",
    "chol3": "Chol HaMoed \u2014 Chol",
    "dayeinu": "Dayeinu",
    "matzah": "Eating matzah on Pesach",
    "edot": "Edot HaMizrach communities",
    "birchot": "Birchot HaShachar \u2014",
    "borei": "Borei pri hagafen is",
    "chabad_omer": "Chabad (Alter Rebbe",
    "chabad_sel": "Chabad follows the standard",
    "chabad": "Chabad is a Chasidic",
    "chabad2": "Chabad \u2014 Chabad is",
}
outdir = Path(__file__).resolve().parent / "refs"
outdir.mkdir(exist_ok=True)
for name, needle in needles.items():
    for k, v in qf.items():
        if needle in k:
            (outdir / f"{name}.txt").write_text(v, encoding="utf-8")
            bad1 = M.findall(v)
            latin = [w for w in M2.findall(v) if w not in ("day", "if", "else")]
            print(name, "len", len(v), "mixed", len(bad1), "latin", latin[:8])
            break
    else:
        print(name, "MISSING")
