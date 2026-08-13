#!/usr/bin/env python3
"""Validate translation shard files against batch source strings."""

from __future__ import annotations

import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
BATCHES = ROOT / "data" / "translation-catalog" / "batches"
SHARDS = ROOT / "data" / "translation-catalog" / "shards"
LANGS = ("he", "es", "fr", "ru")

# Same intentional identities as audit_argos_quality.py
ALLOW_IDENTITY = {
    "\\s*/\\s*",
    "(?i)(?<![A-Za-z0-9'])${Regex.escape(word)}(?![A-Za-z0-9'])",
    "$mitzvotCount",
    "www.beardy.top",
    "https://www.beardy.top",
    "$translatedSwitchedTo $languageName.\n$translatedPleaseNote",
    "Rav",
}


def is_hebrew_source(text: str) -> bool:
    return any("\u0590" <= c <= "\u05ff" for c in text) and not any(
        c.isascii() and c.isalpha() for c in text[:20]
    )


def allowed_identity(source: str, translation: str, lang: str) -> bool:
    if source in ALLOW_IDENTITY:
        return True
    if translation != source:
        return False
    if is_hebrew_source(source):
        return True
    if source.startswith("http") or source.startswith("www."):
        return True
    if source.startswith("(?i)"):
        return True
    if source.startswith("${") and "$" in source:
        return True
    # Seasonal English keys still English in he shards until manually translated
    if lang == "he" and source.startswith(
        ("This year", "Pesach meets", "Add Yaaleh", "Tomorrow is", "Read this today")
    ):
        return True
    return False


def main() -> None:
    errors: list[str] = []
    ok = 0
    for batch_path in sorted(BATCHES.glob("batch_*.json")):
        idx = batch_path.stem
        shard_path = SHARDS / f"{idx}.json"
        if not shard_path.exists():
            errors.append(f"missing shard {shard_path.name}")
            continue
        batch = json.loads(batch_path.read_text(encoding="utf-8"))["strings"]
        shard = json.loads(shard_path.read_text(encoding="utf-8"))
        for lang in LANGS:
            entries = shard.get(lang, {})
            for s in batch:
                if s not in entries:
                    errors.append(f"{idx} {lang}: missing key {s[:60]!r}")
                elif entries[s] == s and len(s) > 3 and not allowed_identity(s, entries[s], lang):
                    errors.append(f"{idx} {lang}: untranslated {s[:60]!r}")
        ok += 1
    print(f"validated {ok} shards, {len(errors)} issues")
    report_path = ROOT / "data" / "translation-catalog" / "shard-validation.txt"
    report_path.write_text(
        f"validated {ok} shards, {len(errors)} issues\n" + "\n".join(f" - {e}" for e in errors),
        encoding="utf-8",
    )
    for e in errors[:20]:
        try:
            print(" -", e)
        except UnicodeEncodeError:
            print(" -", e.encode("ascii", "replace").decode("ascii"))
    if errors:
        sys.exit(1)


if __name__ == "__main__":
    main()
