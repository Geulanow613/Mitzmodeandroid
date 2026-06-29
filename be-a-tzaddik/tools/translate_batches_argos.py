#!/usr/bin/env python3
"""Argos-translate specific batch files into shard JSON."""

from __future__ import annotations

import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TOOLS = ROOT / "tools"
BATCHES = ROOT / "data" / "translation-catalog" / "batches"
SHARDS = ROOT / "data" / "translation-catalog" / "shards"
LANGS = ("he", "es", "fr", "ru")

# Reuse argos helpers
sys.path.insert(0, str(TOOLS))
from generate_argos_translations import (  # noqa: E402
    ARGOS_CODES,
    get_translator,
    install_packages,
    translate_text,
)


def main() -> None:
    if len(sys.argv) < 2:
        raise SystemExit("usage: translate_batches_argos.py 24 25 ...")
    install_packages()
    translators = {lang: get_translator(ARGOS_CODES[lang]) for lang in LANGS}

    for arg in sys.argv[1:]:
        idx = int(arg)
        batch_path = BATCHES / f"batch_{idx:03d}.json"
        strings = json.loads(batch_path.read_text(encoding="utf-8"))["strings"]
        shard: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
        for i, s in enumerate(strings):
            for lang in LANGS:
                shard[lang][s] = translate_text(translators[lang], s)
            if (i + 1) % 2 == 0:
                out = SHARDS / f"batch_{idx:03d}.json"
                out.write_text(json.dumps(shard, ensure_ascii=False, indent=2), encoding="utf-8")
                print(f"batch_{idx:03d}: {i+1}/{len(strings)}", flush=True)
        out = SHARDS / f"batch_{idx:03d}.json"
        out.write_text(json.dumps(shard, ensure_ascii=False, indent=2), encoding="utf-8")
        print(f"wrote {out}")


if __name__ == "__main__":
    main()
