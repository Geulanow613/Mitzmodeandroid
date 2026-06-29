#!/usr/bin/env python3
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
he = json.loads((ROOT / "data/bundled-translations/he.json").read_text(encoding="utf-8"))
MIXED = re.compile(
    r"[\u0590-\u05FF]+[A-Za-z\u0400-\u04FF]+|[A-Za-z\u0400-\u04FF]+[\u0590-\u05FF]+"
)

for batch in ("011", "012"):
    keys = json.loads(
        (ROOT / f"data/translation-catalog/_keys_he_fix_{batch}.json").read_text(
            encoding="utf-8"
        )
    )
    found = []
    for k in keys:
        v = he.get(k)
        if v:
            bad = [
                m.group()
                for m in MIXED.finditer(v)
                if "profile" not in m.group() and "HebrewCalendar" not in m.group()
            ]
            found.append((k, v, bad))
    out = ROOT / "tools" / f"_he_extract_{batch}.json"
    out.write_text(
        json.dumps(
            [{"key": k, "he": v, "bad": b} for k, v, b in found],
            ensure_ascii=False,
            indent=2,
        )
        + "\n",
        encoding="utf-8",
    )
    print(f"{batch}: {len(found)}/{len(keys)} found -> {out.name}")
