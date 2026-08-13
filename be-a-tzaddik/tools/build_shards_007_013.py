#!/usr/bin/env python3
"""Build translation shards for batches 007-013."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
BATCH_DIR = ROOT / "data" / "translation-catalog" / "batches"
SHARD_DIR = ROOT / "data" / "translation-catalog" / "shards"
BUNDLED_DIR = ROOT / "data" / "bundled-translations"
OVERLAY_PATH = SHARD_DIR / "overlay_007_013.json"
LANGS = ("he", "es", "fr", "ru")


def load_bundled() -> dict[str, dict[str, str]]:
    out: dict[str, dict[str, str]] = {}
    for lang in LANGS:
        data = json.loads((BUNDLED_DIR / f"{lang}.json").read_text(encoding="utf-8"))
        out[lang] = data["entries"]
    return out


def load_overlay() -> dict[str, dict[str, str]]:
    if not OVERLAY_PATH.exists():
        return {lang: {} for lang in LANGS}
    data = json.loads(OVERLAY_PATH.read_text(encoding="utf-8"))
    return {lang: data.get(lang, {}) for lang in LANGS}


def translate(string: str, bundled: dict[str, dict[str, str]], overlay: dict[str, dict[str, str]]) -> dict[str, str]:
    result: dict[str, str] = {}
    for lang in LANGS:
        if string in overlay[lang]:
            result[lang] = overlay[lang][string]
        elif string in bundled[lang] and bundled[lang][string] != string:
            result[lang] = bundled[lang][string]
        else:
            raise KeyError(f"Missing {lang} translation for: {string[:80]!r}")
    return result


def main() -> None:
    bundled = load_bundled()
    overlay = load_overlay()
    SHARD_DIR.mkdir(parents=True, exist_ok=True)

    for batch_idx in range(7, 14):
        batch_path = BATCH_DIR / f"batch_{batch_idx:03d}.json"
        batch = json.loads(batch_path.read_text(encoding="utf-8"))
        shard: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
        for s in batch["strings"]:
            tr = translate(s, bundled, overlay)
            for lang in LANGS:
                shard[lang][s] = tr[lang]
        out_path = SHARD_DIR / f"batch_{batch_idx:03d}.json"
        out_path.write_text(json.dumps(shard, ensure_ascii=False, indent=2), encoding="utf-8")
        print(f"Wrote {out_path.name}: {len(batch['strings'])} strings")


if __name__ == "__main__":
    main()
