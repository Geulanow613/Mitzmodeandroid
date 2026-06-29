#!/usr/bin/env python3
"""Import all legacy bundled translation sources into shard files."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DATA = ROOT / "data" / "bundled-translations"
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
SHARDS = ROOT / "data" / "translation-catalog" / "shards"
LANGS = ("he", "es", "fr", "ru")


def load_maps() -> dict[str, dict[str, str]]:
    out: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    for lang in LANGS:
        path = DATA / "maps" / f"{lang}.json"
        if path.exists():
            out[lang].update(json.loads(path.read_text(encoding="utf-8")))
    return out


def load_long_pairs() -> dict[str, dict[str, str]]:
    out: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    en_files = sorted(DATA.glob("long-en-*.txt"))
    for i, en_path in enumerate(en_files):
        en_text = en_path.read_text(encoding="utf-8")
        for lang in LANGS:
            tr_path = DATA / "long" / lang / f"{i:02d}.txt"
            if tr_path.exists():
                tr_text = tr_path.read_text(encoding="utf-8")
                if tr_text.strip() and tr_text != en_text:
                    out[lang][en_text] = tr_text
    return out


def load_long_i18n() -> dict[str, dict[str, str]]:
    path = DATA / "long-i18n.json"
    if not path.exists():
        return {lang: {} for lang in LANGS}
    i18n = json.loads(path.read_text(encoding="utf-8"))
    en_list = json.loads((DATA / "long-en-all.json").read_text(encoding="utf-8"))
    out: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    for lang in LANGS:
        texts = i18n.get(lang, [])
        for en, tr in zip(en_list, texts):
            if tr and tr != en:
                out[lang][en] = tr
    return out


def load_locale_ui() -> dict[str, dict[str, str]]:
    keys_path = DATA / "maps" / "ui-keys.json"
    if not keys_path.exists():
        return {lang: {} for lang in LANGS}
    keys = json.loads(keys_path.read_text(encoding="utf-8"))
    out: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    for lang in ("es", "fr", "ru"):
        path = DATA / "locale" / f"{lang}-ui.json"
        if path.exists():
            vals = json.loads(path.read_text(encoding="utf-8"))
            out[lang].update(dict(zip(keys, vals)))
    return out


def load_existing_bundled() -> dict[str, dict[str, str]]:
    out: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    for lang in LANGS:
        path = DATA / f"{lang}.json"
        if path.exists():
            entries = json.loads(path.read_text(encoding="utf-8")).get("entries", {})
            for en, tr in entries.items():
                if tr and tr != en:
                    out[lang][en] = tr
    return out


def main() -> None:
    required = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    merged: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}

    for source in (load_maps, load_locale_ui, load_long_pairs, load_long_i18n, load_existing_bundled):
        chunk = source()
        for lang in LANGS:
            merged[lang].update(chunk[lang])

    SHARDS.mkdir(parents=True, exist_ok=True)
    shard = {lang: merged[lang] for lang in LANGS}
    (SHARDS / "legacy_import.json").write_text(
        json.dumps(shard, ensure_ascii=False, indent=2), encoding="utf-8"
    )

    for lang in LANGS:
        covered = sum(1 for s in required if s in merged[lang] and merged[lang][s] != s)
        print(f"{lang}: {covered}/{len(required)} translated via legacy import")


if __name__ == "__main__":
    main()
