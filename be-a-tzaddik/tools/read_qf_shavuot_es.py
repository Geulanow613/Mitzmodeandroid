import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
qf = json.loads((ROOT / "data/translation-catalog/shards/quality_fixes.json").read_text(encoding="utf-8"))
es_comp = json.loads((ROOT / "shared/src/commonMain/composeResources/files/translations/es.json").read_text(encoding="utf-8"))["entries"]
strings = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
for en in strings:
    if en.startswith("The week before Shavuot"):
        for label, tr in [("compiled", es_comp.get(en, "")), ("qf", qf.get("es", {}).get(en, ""))]:
            i = tr.lower().find("vacac")
            print(label, "vacaciones" in tr.lower(), tr[max(0, i - 40) : i + 60] if i >= 0 else "")
        break
