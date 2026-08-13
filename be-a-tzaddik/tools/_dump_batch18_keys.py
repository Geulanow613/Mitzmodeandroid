#!/usr/bin/env python3
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
cat = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
es = json.loads((ROOT / "shared/src/commonMain/composeResources/files/translations/es.json").read_text(encoding="utf-8"))["entries"]
fr = json.loads((ROOT / "shared/src/commonMain/composeResources/files/translations/fr.json").read_text(encoding="utf-8"))["entries"]

needles = [
    "ona'ah — Overcharging",
    "misheyakir — Misheyakir",
    "Overcharging or underpaying when the other",
    "Arba Minim (ארבעה מינים) — the Four Species — are taken each day of Sukkot (except Shabbat).\n\nThe four:",
    "Learn about the incredible power",
    "Tekiah is a long straight shofar",
    "Get ready for a wake-up call!",
    "Jewish law applies in the marketplace",
]

out = ROOT / "tools" / "_batch18_keys.json"
payload = {}
for sub in needles:
    k = next(x for x in cat if x.startswith(sub) or sub in x)
    payload[sub] = {"key": k, "en": k, "es": es[k], "fr": fr[k]}
out.write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")
print("Wrote", out)
