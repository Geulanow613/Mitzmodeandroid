#!/usr/bin/env python3
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
req = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
ALLOW = {
    "\\s*/\\s*",
    "(?i)(?<![A-Za-z0-9'])${Regex.escape(word)}(?![A-Za-z0-9'])",
    "$mitzvotCount",
    "www.beardy.top",
    "https://www.beardy.top",
    "$translatedSwitchedTo $languageName.\n$translatedPleaseNote",
    "Rav",
}

def is_hebrew(s):
    return any("\u0590" <= c <= "\u05ff" for c in s) and not any(c.isascii() and c.isalpha() for c in s[:20])

lines = []
for lang in ("he", "es", "fr", "ru"):
    e = json.loads(
        (ROOT / f"shared/src/commonMain/composeResources/files/translations/{lang}.json").read_text(encoding="utf-8")
    )["entries"]
    fb = [s for s in req if e.get(s, s) == s and len(s) > 2 and s not in ALLOW]
    lines.append(f"\n{lang} fallbacks ({len(fb)}):")
    for s in fb:
        tag = "hebrew-liturgy" if is_hebrew(s) else "ENGLISH"
        lines.append(f"  [{tag}] {s[:70]}")
(ROOT / "data/translation-catalog/fallbacks-report.txt").write_text("\n".join(lines), encoding="utf-8")
