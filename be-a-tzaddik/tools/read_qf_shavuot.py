import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
qf = json.loads((ROOT / "data/translation-catalog/shards/quality_fixes.json").read_text(encoding="utf-8"))
strings = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
for en in strings:
    if en.startswith("The week before Shavuot"):
        tr = qf.get("fr", {}).get(en, "<missing>")
        print("vacances in qf", "vacances" in tr.lower())
        i = tr.lower().find("vacan")
        if i >= 0:
            print(tr[max(0, i - 40) : i + 60])
        break
