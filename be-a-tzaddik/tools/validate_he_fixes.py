# -*- coding: utf-8 -*-
import json
import re
from pathlib import Path

H = Path(__file__).resolve().parents[1] / "data" / "translation-catalog" / "human"


def latin(s):
    t = re.sub(r"\$\{[^}]+\}", "", s)
    t = re.sub(r"\$[a-zA-Z_][a-zA-Z0-9_]*", "", t)
    return bool(re.search(r"[A-Za-z]", t))


for fn in ["he_fix_006_he_only.json", "he_fix_008_he_only.json", "he_fix_003_he_only.json"]:
    arr = json.loads((H / fn).read_text(encoding="utf-8"))["he"]
    bad = [i for i, s in enumerate(arr) if latin(s)]
    print(f"{fn}: count={len(arr)}, latin={bad}")

ph = [
    "$tomorrowChag",
    "${shehecheyanuErevLines",
    "${diasporaSecondDayNote",
    "$todaySummary",
    "$day",
    "$tonight",
    "$timePart",
    "$nextNightLine",
    "${omerCountSpeechPhrase",
    "$nusachWhen",
]
arr8 = json.loads((H / "he_fix_008_he_only.json").read_text(encoding="utf-8"))["he"]
for p in ph:
    print(f"  placeholder {p}: {any(p in s for s in arr8)}")
