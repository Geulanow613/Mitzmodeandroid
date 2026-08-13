#!/usr/bin/env python3
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
cat = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
fr = json.loads((ROOT / "shared/src/commonMain/composeResources/files/translations/fr.json").read_text(encoding="utf-8"))["entries"]
es = json.loads((ROOT / "shared/src/commonMain/composeResources/files/translations/es.json").read_text(encoding="utf-8"))["entries"]

needles = [
    "After using the bathroom, we wash our hands",
    "Before eating or drinking anything, we say a short blessing",
    "Chol HaMoed (חול המועד)",
    "Married Jewish women are required to cover their hair",
    "The Torah commands: \"Be fruitful and multiply\"",
    "revi'it — A revi'it (רביעית)",
]

payload = {}
for sub in needles:
    k = next(x for x in cat if x.startswith(sub) or sub in x)
    payload[sub] = {"key_len": len(k), "en": k, "fr": fr[k], "es": es[k]}

(ROOT / "tools" / "_batch20_keys.json").write_text(
    json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8"
)
print("Wrote batch20 keys", len(payload))
