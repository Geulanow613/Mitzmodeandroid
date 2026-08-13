#!/usr/bin/env python3
"""Find FR glued words in catalog entries (exclude $ and { template vars)."""
import json, re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
cat = set(json.load(open(ROOT / "data/translation-catalog/strings.json", encoding="utf-8"))["strings"])
fr = json.load(open(ROOT / "shared/src/commonMain/composeResources/files/translations/fr.json", encoding="utf-8"))["entries"]

# French word glued to capitalized Hebrew/Latin term
pat = re.compile(
    r"(?<![$\{])(?:pas|est|les|des|une|par|sur|dans|avec|sans|pour|sont|cette|cette|tout|tous|toute|"
    r"plus|moins|après|avant|entre|sous|vers|chez|dont|mais|donc|ainsi|comme|très|bien|"
    r"viennent|catégorie|interdit|permis|dire|réciter|faire|être|avoir|peut|doit)"
    r"([A-Z][a-z]{2,}|[A-Z]{2,}[a-z])"
)

seen = {}
for k, v in fr.items():
    if k not in cat:
        continue
    for m in pat.finditer(v):
        frag = m.group(0)
        if any(x in frag for x in ["DeZimra", "LaSechvi", "Shekel", "Pesach)", "VeYatziv)", "Kosher", "Shabbat", "Yom Tov"]):
            continue
        seen.setdefault(frag, []).append(k[:65])

print(f"unique glues: {len(seen)}")
for frag, keys in sorted(seen.items(), key=lambda x: -len(x[1]))[:40]:
    print(f"{frag!r} ({len(keys)}) e.g. {keys[0]}")
