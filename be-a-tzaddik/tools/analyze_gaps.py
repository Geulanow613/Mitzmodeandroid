#!/usr/bin/env python3
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
req = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
ALLOW = {
    r"\s*/\s*",
    "(?i)(?<![A-Za-z0-9'])${Regex.escape(word)}(?![A-Za-z0-9'])",
    "$mitzvotCount",
    "www.beardy.top",
    "https://www.beardy.top",
    "$translatedSwitchedTo $languageName.\n$translatedPleaseNote",
    "Rav",
}


def is_hebrew(s: str) -> bool:
    return any("\u0590" <= c <= "\u05ff" for c in s) and not any(c.isascii() and c.isalpha() for c in s[:20])


for lang in ("he", "es", "fr", "ru"):
    e = json.loads(
        (ROOT / f"shared/src/commonMain/composeResources/files/translations/{lang}.json").read_text(
            encoding="utf-8"
        )
    )["entries"]
    fb = [s for s in req if e.get(s, s) == s and len(s) > 2 and s not in ALLOW]
    real = [s for s in fb if not is_hebrew(s)]
    print(f"{lang}: {len(real)} non-hebrew fallbacks")
    for s in real:
        print(f"  {s[:90]}")

qr = json.loads((ROOT / "data/translation-catalog/quality-report.json").read_text(encoding="utf-8"))
print("\nQuality issues:", len(qr["issues"]))

audit = json.loads((ROOT / "data/translation-catalog/deep-mitzvot-audit.json").read_text(encoding="utf-8"))
print("Garbage hits:", len(audit.get("garbage_hits", [])))
