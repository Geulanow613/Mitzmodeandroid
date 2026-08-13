#!/usr/bin/env python3
"""Extract the 9 static explainer fragments missing from catalog."""
import re
from pathlib import Path

path = Path(__file__).resolve().parents[1] / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/SeasonalMitzvahText.kt"
text = path.read_text(encoding="utf-8")
import json
cat = set(json.load(open(Path(__file__).resolve().parents[1] / "data/translation-catalog/strings.json", encoding="utf-8"))["strings"])

TRIPLE = re.compile(r'"""(.*?)"""', re.DOTALL)
DYNAMIC = re.compile(r'\$\{(?!dateLabel\})[^}]+\}|\$[a-zA-Z_][a-zA-Z0-9_]*')

missing = []
for m in TRIPLE.finditer(text):
    body = m.group(1).strip()
    if len(body) < 40:
        continue
    if DYNAMIC.search(body.replace("$dateLabel", "")):
        continue
    if body not in cat:
        missing.append(body)

print(f"missing: {len(missing)}")
for i, s in enumerate(missing, 1):
    print(f"\n--- {i} ({len(s)} chars) ---")
    print(s[:200] + ("..." if len(s) > 200 else ""))
