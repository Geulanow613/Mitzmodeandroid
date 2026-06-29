import json
from pathlib import Path

from apply_quality_fixes import (
    HAND_FIXES,
    apply_subs,
    load_compiled,
    repair_placeholders,
    restore_kotlin_templates,
)

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data/translation-catalog/strings.json"
required = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
en = next(s for s in required if s.startswith("The week before Shavuot"))
compiled = load_compiled()

for lang in ("es", "fr"):
    if en in HAND_FIXES and lang in HAND_FIXES[en]:
        print(lang, "HAND_FIXES skip")
        continue
    tr = compiled[lang].get(en, en)
    print(lang, "tr==en", tr == en, "vac", "vacac" in tr.lower())
    if tr == en and lang != "he":
        print(lang, "skipped tr==en")
        continue
    new_tr = apply_subs(lang, tr)
    new_tr = restore_kotlin_templates(en, new_tr)
    new_tr = repair_placeholders(en, new_tr)
    print(lang, "new!=tr", new_tr != tr, "vac after", "vacac" in new_tr.lower())
