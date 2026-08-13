#!/usr/bin/env python3
"""Export human mitzvot batch to a readable HTML review page."""
import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
batch = sys.argv[1] if len(sys.argv) > 1 else "001"
kind = "cloud"
if batch.startswith("local_"):
    kind = "local"
    batch_id = batch.replace("local_", "")
    keys_path = ROOT / f"data/translation-catalog/_keys_{batch}.json"
    data_path = ROOT / f"data/translation-catalog/human/{batch}.json"
    start = 26  # local_001 keys are gratitude/shabbat prep (after original local batch)
    if batch_id != "001":
        start = 26 + (int(batch_id) - 2) * 25
else:
    batch_id = batch
    keys_path = ROOT / f"data/translation-catalog/_keys_{batch_id}.json"
    data_path = ROOT / f"data/translation-catalog/human/mitzvot_{batch_id}.json"
    start = int(batch_id) * 25 - 24  # 001->1, 002->26

data = json.loads(data_path.read_text(encoding="utf-8"))
keys = json.loads(keys_path.read_text(encoding="utf-8"))

parts = [
    f"""<!DOCTYPE html>
<html lang="en"><head><meta charset="utf-8">
<title>Mitzvot {kind} {batch_id} — human translations</title>
<style>
body{{font-family:system-ui,sans-serif;max-width:900px;margin:2rem auto;padding:0 1rem;line-height:1.5}}
.mitzvah{{border:1px solid #ccc;border-radius:8px;padding:1rem;margin:1.5rem 0}}
.mitzvah h2{{margin:0 0 .5rem;font-size:1.1rem}}
.lang{{margin:.75rem 0}}
.lang b{{display:inline-block;width:2rem}}
.en{{background:#f5f5f5;padding:.75rem;border-radius:4px;font-size:.9rem;white-space:pre-wrap}}
</style></head><body>
<h1>Human translations: {kind} batch {batch_id} ({len(keys)} entries)</h1>
<p>Source: <code>{data_path.relative_to(ROOT).as_posix()}</code></p>
"""
]

for i, key in enumerate(keys):
    num = start + i if kind == "cloud" else i + 1
    label = f"cloud{num}" if kind == "cloud" else f"local #{num}"
    title = key.split("!")[0].split(".")[0][:80]
    parts.append(f'<div class="mitzvah"><h2>#{num} {label} — {title}</h2>')
    parts.append(f'<div class="en"><b>EN</b> {json.dumps(key)[1:-1]}</div>')
    for lang, label in [("he", "HE"), ("es", "ES"), ("fr", "FR"), ("ru", "RU")]:
        tr = data[lang].get(key, "MISSING")
        parts.append(f'<div class="lang"><b>{label}</b> {json.dumps(tr)[1:-1]}</div>')
    parts.append("</div>")

parts.append("</body></html>")
out = ROOT / f"data/translation-catalog/{batch}_review.html" if kind == "local" else ROOT / f"data/translation-catalog/mitzvot_{batch_id}_review.html"
out.write_text("\n".join(parts), encoding="utf-8")
print(f"wrote {out}")
