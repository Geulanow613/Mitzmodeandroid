#!/usr/bin/env python3
"""Translate only catalog strings not yet covered by existing shards."""

from __future__ import annotations

import json
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TOOLS = ROOT / "tools"
CATALOG = ROOT / "data" / "translation-catalog"
SHARDS = CATALOG / "shards"
LANGS = ("he", "es", "fr", "ru")


def merged() -> dict[str, dict[str, str]]:
    out: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    for path in sorted(SHARDS.glob("*.json")):
        data = json.loads(path.read_text(encoding="utf-8"))
        for lang in LANGS:
            out[lang].update(data.get(lang, {}))
    return out


def main() -> None:
    required = json.loads((CATALOG / "strings.json").read_text(encoding="utf-8"))["strings"]
    have = merged()
    missing = [
        s
        for s in required
        if any(have[lang].get(s, s) == s for lang in LANGS)
    ]
    gap_path = CATALOG / "missing-strings.json"
    gap_path.write_text(json.dumps({"strings": missing}, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"missing {len(missing)} / {len(required)}")
    if not missing:
        return
    subprocess.check_call([sys.executable, str(TOOLS / "generate_argos_translations.py"), "--gap", str(gap_path)])


if __name__ == "__main__":
    main()
