#!/usr/bin/env python3
import json
from collections import defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
r = json.loads((ROOT / "data/translation-catalog/quality-report.json").read_text(encoding="utf-8"))
crit = [i for i in r["issues"] if i["severity"] == "CRITICAL"]
by = defaultdict(list)
for i in crit:
    by[i["reason"]].append(i)
lines = []
for reason, items in sorted(by.items()):
    lines.append(f"=== {reason} {len(items)}")
    for i in items:
        lines.append(f"{i['lang']} | {i.get('key_full', i['key'])[:120]}")
        lines.append(f"  current: {i.get('current', '')[:200]}")
        lines.append(f"  detail: {i.get('detail', '')}")
(ROOT / "data/translation-catalog/critical_remaining2.txt").write_text("\n".join(lines), encoding="utf-8")
print(len(crit))
