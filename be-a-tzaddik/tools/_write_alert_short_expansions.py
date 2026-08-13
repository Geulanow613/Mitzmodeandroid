#!/usr/bin/env python3
"""Write human/alert_short_expansions.json from _alert_short_expansions_data."""

from __future__ import annotations

import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TOOLS = Path(__file__).resolve().parent
sys.path.insert(0, str(TOOLS))

from _alert_short_expansions_data import ALERT_SHORT_EXPAND  # noqa: E402

CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
OUT = ROOT / "data" / "translation-catalog" / "human" / "alert_short_expansions.json"
MIN_LEN = 300


def main() -> int:
    catalog: set[str] = set(json.loads(CATALOG.read_text(encoding="utf-8"))["strings"])
    out: dict[str, dict[str, str]] = {lang: {} for lang in ("he", "es", "fr", "ru")}

    for key, langs in ALERT_SHORT_EXPAND.items():
        if key not in catalog:
            continue
        for lang, val in langs.items():
            if len(val) >= MIN_LEN:
                out[lang][key] = val

    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps(out, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    for lang in out:
        short = sum(1 for v in out[lang].values() if len(v) < MIN_LEN)
        print(f"{lang}: {len(out[lang])} keys, under {MIN_LEN}: {short}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
