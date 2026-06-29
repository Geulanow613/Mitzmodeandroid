#!/usr/bin/env python3
"""Export actionable translation fixes needed."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
scan = json.loads((ROOT / "data/translation-catalog/deep-scan.json").read_text(encoding="utf-8"))
entries = {
    lang: json.loads(
        (ROOT / f"shared/src/commonMain/composeResources/files/translations/{lang}.json").read_text(encoding="utf-8")
    )["entries"]
    for lang in ("he", "es", "fr", "ru")
}

def is_hebrew_source(text: str) -> bool:
    return any("\u0590" <= c <= "\u05ff" for c in text) and not any(
        c.isascii() and c.isalpha() for c in text[:20]
    )

# English in he bundle (should be Hebrew)
he_english = [k for k in scan["english_fallback_keys"]["he"] if not is_hebrew_source(k)]
out = ROOT / "data/translation-catalog/actionable-fixes.json"
actionable = {
    "he_english_seasonal": he_english,
    "short_untranslated": scan["short_untranslated_keys"],
    "rapid_fast": [i for i in scan["pattern_issues"] if i["reason"] == "rapid-fast"],
    "holiday_word": [i for i in scan["pattern_issues"] if i["reason"] == "holiday-word"],
    "drawing": [i for i in scan["pattern_issues"] if i["reason"] == "drawing"],
}
out.write_text(json.dumps(actionable, ensure_ascii=False, indent=2), encoding="utf-8")
print("he english seasonal:", len(he_english))
for k in he_english:
    print("-", k[:90])
print("short:", actionable["short_untranslated"])
print("rapid-fast:", len(actionable["rapid_fast"]))
print("holiday-word:", len(actionable["holiday_word"]))
print("drawing:", len(actionable["drawing"]))
