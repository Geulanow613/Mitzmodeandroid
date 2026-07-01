#!/usr/bin/env python3
import json, re
from pathlib import Path
ROOT = Path(__file__).resolve().parents[1]
ALERT = re.compile(
    r"Did you know|Today's mission|Today's challenge|Discover the letter|Walk in G-d|Be an Amen|"
    r"Celebrate the gift|Connect through|Crown yourself|Gather for inspiration|Guard your|"
    r"Learn about the|Rise above|Embrace the|Honor Torah|Experience the|Explore the|Dive into",
    re.I,
)
strings = json.loads((ROOT/"data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
alert_keys = [k for k in strings if len(k)>40 and ALERT.search(k)]
he = json.loads((ROOT/"shared/src/commonMain/composeResources/files/translations/he.json").read_text(encoding="utf-8"))["entries"]
he_tone = json.loads((ROOT/"data/translation-catalog/human/he_alert_tone.json").read_text(encoding="utf-8")).get("he",{})
mt = json.loads((ROOT/"data/translation-catalog/human/mitzvah_alert_tone.json").read_text(encoding="utf-8"))
HYBRID = re.compile(r"[\u05d0-\u05ea][a-zA-Z]{2,}|[a-zA-Z]{2,}[\u05d0-\u05ea]")
bad_he_alerts = sum(1 for k in alert_keys if HYBRID.search(he.get(k,"")))
print(f"alert keys: {len(alert_keys)}")
print(f"he_alert_tone: {len(he_tone)}")
print(f"mitzvah_alert_tone: he={len(mt.get('he',{}))} es={len(mt.get('es',{}))} fr={len(mt.get('fr',{}))} ru={len(mt.get('ru',{}))}")
print(f"he alerts with hybrid in bundle: {bad_he_alerts}")
# es disasters sample
es = json.loads((ROOT/"shared/src/commonMain/composeResources/files/translations/es.json").read_text(encoding="utf-8"))["entries"]
es_en = sum(1 for k,v in es.items() if re.search(r"\b(the|and|with|your|this)\b", v) and len(v)>80)
print(f"es long values with common English words: {es_en}")
