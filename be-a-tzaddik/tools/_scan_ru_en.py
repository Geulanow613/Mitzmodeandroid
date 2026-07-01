#!/usr/bin/env python3
"""List RU entries where value closely matches English source."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
cat = json.load(open(ROOT / "data/translation-catalog/strings.json", encoding="utf-8"))["strings"]
ru = json.load(open(ROOT / "shared/src/commonMain/composeResources/files/translations/ru.json", encoding="utf-8"))["entries"]

hits = []
for k in cat:
    v = ru.get(k, "")
    if not v or v == k:
        hits.append(("missing/same", k[:70]))
    elif v[:60] == k[:60]:
        hits.append(("prefix_match", k[:70]))

print(f"hits: {len(hits)}")
for kind, k in hits:
    print(kind, k)
