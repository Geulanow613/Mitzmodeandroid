#!/usr/bin/env python3
import json
import re
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from _write_he_alert_tone import ALLOWED_LATIN, HYBRID, LATIN_IN_HE, is_bad_he

items = json.loads((Path(__file__).parent / "_remaining_alerts.json").read_text(encoding="utf-8"))


def score(item: dict) -> int:
    he = item["he"]
    s = 0
    if is_bad_he(he):
        s += 100
    if HYBRID.search(he):
        s += 50
    if LATIN_IN_HE.search(he):
        s += 40
    for w in re.findall(r"[A-Za-z]{3,}", he):
        if w not in ALLOWED_LATIN:
            s += 30
    for m in ("משימה להיום", "הנה משהו", "אתה ", "למד עוד", "ה-Rambam", "ב-ה'", "tzitzit"):
        if m in he:
            s += 10
    return s


ranked = sorted(items, key=score, reverse=True)
out = Path(__file__).parent / "_remaining_ranked.json"
out.write_text(json.dumps(ranked[:110], ensure_ascii=False, indent=2), encoding="utf-8")
print(f"wrote {len(ranked[:110])} ranked items")
