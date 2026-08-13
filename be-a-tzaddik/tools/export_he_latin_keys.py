#!/usr/bin/env python3
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
req = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
he = json.loads(
    (ROOT / "shared/src/commonMain/composeResources/files/translations/he.json").read_text(encoding="utf-8")
)["entries"]

rows = []
for s in req:
    tr = he.get(s, s)
    if tr == s or len(tr) < 20:
        continue
    letters = [c for c in tr if c.isalpha()]
    if not letters:
        continue
    ratio = sum(1 for c in letters if c.isascii()) / len(letters)
    if ratio > 0.85:
        rows.append((ratio, len(s), s, tr[:120]))

rows.sort(key=lambda x: (-x[0], x[1]))
out = ROOT / "data" / "translation-catalog" / "he-latin-heavy-keys.json"
out.write_text(
    json.dumps([{"key": s, "preview": tr} for _, _, s, tr in rows], ensure_ascii=False, indent=2),
    encoding="utf-8",
)
print(len(rows), "keys ->", out)
