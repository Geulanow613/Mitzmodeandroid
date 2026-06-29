import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
qf = json.loads((ROOT / "data/translation-catalog/shards/quality_fixes.json").read_text(encoding="utf-8"))
comp = {
    lang: json.loads((ROOT / f"shared/src/commonMain/composeResources/files/translations/{lang}.json").read_text(encoding="utf-8"))["entries"]
    for lang in ("es", "fr")
}
strings = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
en = next(s for s in strings if s.startswith("The week before Shavuot"))
for lang in ("es", "fr"):
    for label, tr in [("qf", qf.get(lang, {}).get(en, "")), ("compiled", comp[lang].get(en, ""))]:
        print(lang, label, "vac" in tr.lower())
