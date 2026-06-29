import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
COMPOSE = ROOT / "shared/src/commonMain/composeResources/files/translations"
OUT = ROOT / "data/translation-catalog/shards/quality_fixes.json"
CATALOG = ROOT / "data/translation-catalog/strings.json"

SUB = (r"\bvacances\b(?!\s+physiques)", "fête")

required = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
fr = json.loads((COMPOSE / "fr.json").read_text(encoding="utf-8"))["entries"]
qf = json.loads(OUT.read_text(encoding="utf-8"))

for en in required:
    if en.startswith("The week before Shavuot"):
        tr = fr.get(en, "")
        print("in compiled", en[:40], "vacances" in tr.lower())
        if en in qf.get("fr", {}):
            print("in quality_fixes vacances", "vacances" in qf["fr"][en].lower())
        new = re.sub(SUB[0], SUB[1], tr, flags=re.IGNORECASE)
        print("after sub vacances", "vacances" in new.lower())
        print("changed", new != tr)
        break
