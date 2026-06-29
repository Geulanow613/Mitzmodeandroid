#!/usr/bin/env python3
"""Re-translate missing batch_025 strings and merge into shard."""

from __future__ import annotations

import json
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from auto_translate_shards import LANGS, get_translator, translate_text
from fix_embedded_hebrew_shards import fix_translation

ROOT = Path(__file__).resolve().parents[1]
BATCHES = ROOT / "data" / "translation-catalog" / "batches"
SHARDS = ROOT / "data" / "translation-catalog" / "shards"


def main() -> None:
    strings = json.loads((BATCHES / "batch_025.json").read_text(encoding="utf-8"))["strings"]
    path = SHARDS / "batch_025.json"
    shard = json.loads(path.read_text(encoding="utf-8"))
    missing = [s for s in strings if s not in shard["he"]]
    print(f"Missing {len(missing)} strings", file=sys.stderr)
    translators = {lang: get_translator(lang) for lang in LANGS}
    for i, s in enumerate(missing):
        print(f"  [{i + 1}/{len(missing)}]", file=sys.stderr)
        for lang in LANGS:
            tr = translate_text(s, lang, translators[lang])
            tr = fix_translation(s, tr)
            shard[lang][s] = tr
    if len(shard["he"]) != len(strings):
        raise SystemExit(f"Still incomplete: {len(shard['he'])}/{len(strings)}")
    path.write_text(json.dumps(shard, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {path.name}: {len(strings)} keys x 4 langs")


if __name__ == "__main__":
    main()
