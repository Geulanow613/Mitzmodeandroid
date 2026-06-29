#!/usr/bin/env python3
"""Repair missing keys in shard files."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

import argostranslate.translate

ROOT = Path(__file__).resolve().parents[1]
BATCHES = ROOT / "data/translation-catalog/batches"
SHARDS = ROOT / "data/translation-catalog/shards"
LANGS = ("he", "es", "fr", "ru")
PH_RE = re.compile(r"(\$\{[^{}]+\}|\$[A-Za-z_][A-Za-z0-9_]*)")


def is_hebrew_source(text: str) -> bool:
    hebrew = sum(1 for c in text if "\u0590" <= c <= "\u05ff")
    latin = sum(1 for c in text if c.isascii() and c.isalpha())
    return hebrew > 30 and latin < max(20, hebrew // 4)


def split_placeholders(text: str) -> list[tuple[str, bool]]:
    parts: list[tuple[str, bool]] = []
    last = 0
    for m in PH_RE.finditer(text):
        if m.start() > last:
            parts.append((text[last : m.start()], False))
        parts.append((m.group(0), True))
        last = m.end()
    if last < len(text):
        parts.append((text[last:], False))
    return parts if parts else [(text, False)]


def get_translator(target: str):
    en = next(l for l in argostranslate.translate.get_installed_languages() if l.code == "en")
    tgt = next(l for l in argostranslate.translate.get_installed_languages() if l.code == target)
    return en.get_translation(tgt)


def translate_segment(text: str, translator) -> str:
    if not text.strip():
        return text
    if sum(1 for c in text if "\u0590" <= c <= "\u05ff") > len(text) * 0.4:
        return text
    try:
        return translator.translate(text)
    except Exception:
        return text


def translate_text(text: str, lang: str, translator) -> str:
    if is_hebrew_source(text) and lang == "he":
        return text
    parts = split_placeholders(text)
    if not any(is_ph for _, is_ph in parts):
        return translate_segment(text, translator)
    out = []
    for seg, is_ph in parts:
        if is_ph:
            out.append(seg)
        else:
            lines = seg.split("\n")
            out.append("\n".join(translate_segment(line, translator) for line in lines))
    return "".join(out)


def main() -> None:
    translators = {lang: get_translator(lang) for lang in LANGS}
    added = 0
    for n in range(20, 26):
        batch_path = BATCHES / f"batch_{n:03d}.json"
        shard_path = SHARDS / f"batch_{n:03d}.json"
        strings = json.loads(batch_path.read_text(encoding="utf-8"))["strings"]
        shard = json.loads(shard_path.read_text(encoding="utf-8"))
        for lang in LANGS:
            shard.setdefault(lang, {})
        missing = [s for s in strings if s not in shard["he"]]
        if not missing:
            continue
        print(f"batch_{n:03d}: repairing {len(missing)} missing keys", file=sys.stderr)
        for i, s in enumerate(missing):
            print(f"  [{i+1}/{len(missing)}] len={len(s)}", file=sys.stderr)
            for lang in LANGS:
                shard[lang][s] = translate_text(s, lang, translators[lang])
            added += 1
        shard_path.write_text(json.dumps(shard, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Added {added} missing string translations")


if __name__ == "__main__":
    main()
