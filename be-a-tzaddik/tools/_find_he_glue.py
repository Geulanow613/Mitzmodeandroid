#!/usr/bin/env python3
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
catalog = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
he = json.loads(
    (ROOT / "shared/src/commonMain/composeResources/files/translations/he.json").read_text(encoding="utf-8")
)["entries"]

for k in catalog:
    v = he.get(k, "")
    if not v or v == k:
        continue
    stripped = re.sub(r"\$\{[^}]*\}", "", v)
    if re.search(r"[\u0590-\u05ff]\$", stripped) or re.search(r"\$[a-zA-Z]+[\u0590-\u05ff]", stripped):
        print("GLUE placeholder:", k[:70])
        print(v[:220])
        print()
    if re.search(r"[\u0590-\u05ff][A-Za-z]{3,}", stripped) and len(v) > 60:
        print("GLUE latin:", k[:70])
        print(v[:220])
        print()
