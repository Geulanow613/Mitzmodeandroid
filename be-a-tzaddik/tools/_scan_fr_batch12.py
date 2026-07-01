#!/usr/bin/env python3
import json, re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
cat = set(json.load(open(ROOT / "data/translation-catalog/strings.json", encoding="utf-8"))["strings"])
fr = json.load(open(ROOT / "shared/src/commonMain/composeResources/files/translations/fr.json", encoding="utf-8"))["entries"]

# Real glue: lowercase letter immediately after French word without space/punct (exclude $vars)
glue_re = re.compile(r"(?<![$\w])([a-zàâäéèêëïîôùûüç]{3,})([A-Z][a-z]{2,})")

hits = []
for k, v in fr.items():
    if k not in cat:
        continue
    for m in glue_re.finditer(v):
        frag = m.group(0)
        if frag.startswith("$") or "DeZimra" in frag or "LaSechvi" in frag or "haShekel" in frag:
            continue
        hits.append((k[:70], frag, v[max(0, m.start() - 30) : m.end() + 30]))

print(f"glue hits: {len(hits)}")
for h in hits[:35]:
    print("---", h[0])
    print(" ", h[1], "|", repr(h[2]))

for needle in ["KosherJava", "Kli SheMelakhto", "haPesach)", "desDes", "simplementL", "force mCette"]:
    n = sum(1 for k, v in fr.items() if k in cat and needle in v)
    print(f"{needle}: {n}")
