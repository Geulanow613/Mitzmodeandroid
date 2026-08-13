#!/usr/bin/env python3
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
path = ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/SeasonalMitzvahText.kt"
text = path.read_text(encoding="utf-8")
TRIPLE = re.compile(r'"""(.*?)"""', re.DOTALL)
DYNAMIC = re.compile(r'\$\{(?!dateLabel\})[^}]+\}|\$[a-zA-Z_][a-zA-Z0-9_]*')
cat = set(json.load(open(ROOT / "data/translation-catalog/strings.json", encoding="utf-8"))["strings"])

missing = []
for m in TRIPLE.finditer(text):
    body = m.group(1).strip()
    if len(body) < 40 or DYNAMIC.search(body.replace("$dateLabel", "")):
        continue
    if body not in cat:
        missing.append(body)

out = ROOT / "tools" / "_seasonal_fragments_en.json"
out.write_text(json.dumps(missing, ensure_ascii=False, indent=2), encoding="utf-8")
print(f"wrote {len(missing)} fragments to {out.name}")
