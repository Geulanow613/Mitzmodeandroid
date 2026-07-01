#!/usr/bin/env python3
"""Targeted audit: show ONLY real artifacts (PH0 and HTML entities), no false positives."""
import json, re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
COMPOSE = ROOT / "shared/src/commonMain/composeResources/files/translations"
CATALOG = ROOT / "data/translation-catalog/strings.json"
required = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]

ARTIFACT_PATTERNS = [
    (r"PH\d+", "placeholder PH0/PH1/…"),
    (r"&(?:amp|quot|lt|gt|apos);", "HTML entity"),
    (r"⟦\d+⟧", "argos bracket ⟦N⟧"),
]

for lang in ("he", "es", "fr", "ru"):
    path = COMPOSE / f"{lang}.json"
    entries = json.loads(path.read_text(encoding="utf-8"))["entries"]
    found = []
    for en_key in required:
        tr = entries.get(en_key, "")
        if not tr:
            continue
        for pattern, label in ARTIFACT_PATTERNS:
            m = re.search(pattern, tr)
            if m:
                found.append((label, m.group(), en_key[:80], tr[:100]))
                break
    print(f"\n=== {lang.upper()} — {len(found)} real artifact(s) ===")
    for label, match, en, tr in found:
        print(f"  [{label}: {match!r}]")
        print(f"    EN: {en}")
        print(f"    TR: {tr}")
