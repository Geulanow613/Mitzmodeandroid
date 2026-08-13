#!/usr/bin/env python3
"""Generate index page linking all mitzvot review HTML files."""
from __future__ import annotations

from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data/translation-catalog"

pages = sorted(CATALOG.glob("*_review.html"))
# drop typo duplicate
pages = [p for p in pages if p.name != "mitxvot_001_review.html"]

lines = [
    """<!DOCTYPE html>
<html lang="en"><head><meta charset="utf-8">
<title>Mitz Mode — translation review index</title>
<style>
body{font-family:system-ui,sans-serif;max-width:720px;margin:2rem auto;padding:0 1rem;line-height:1.6}
h1{margin-bottom:.25rem}
p.meta{color:#555}
ul{padding-left:1.25rem}
li{margin:.35rem 0}
a{color:#0645ad}
section{margin:1.5rem 0}
</style></head><body>
<h1>Human translation review</h1>
<p class="meta">All mitzvot batches (cloud + local). Open any page to compare EN / HE / ES / FR / RU side by side.</p>
""",
]

cloud = [p for p in pages if p.name.startswith("mitzvot_")]
local = [p for p in pages if p.name.startswith("local_")]

for title, group in [("Cloud mitzvot (mitzvotcloud.json)", cloud), ("Local mitzvot (mitzvotlistfull.json)", local)]:
    lines.append(f"<section><h2>{title}</h2><ul>")
    for p in group:
        label = p.stem.replace("_review", "").replace("_", " ")
        lines.append(f'  <li><a href="{p.name}">{label}</a></li>')
    lines.append("</ul></section>")

lines.append("</body></html>")
out = CATALOG / "review_index.html"
out.write_text("\n".join(lines), encoding="utf-8")
print(f"wrote {out} ({len(pages)} pages)")
