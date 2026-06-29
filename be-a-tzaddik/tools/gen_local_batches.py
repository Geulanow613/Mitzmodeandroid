# -*- coding: utf-8 -*-
"""Generate all 8 local_003/004 *_only.json files with human-quality translations."""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
HUMAN = ROOT / "human"

HE = "\u05D4'"
RMBM = '\u05D4\u05E8\u05DE\u05D1"\u05DD'
RCHUM = "\u05E8\u05D7\u05D5\u05DD"
CHNUN = "\u05D7\u05E0\u05D5\u05DF"
KDOSH = "\u05E7\u05D3\u05D5\u05E9"

T003 = {
    "he": [
        f"\u05DC\u05DB\u05D5 \u05D1\u05D3\u05E8\u05DB\u05D9 {HE}! \U0001F463 {RMBM} \u05DE\u05D2\u05DC\u05D4 \u05D1\u05D4\u05DC\u05DB\u05D5\u05EA \u05D3\u05E2\u05D5\u05EA: \u05D1\u05D3\u05D9\u05D5\u05E7 \u05DB\u05E4\u05D9 \u05E9{HE} \u05D4\u05D5\u05D0 {RCHUM} \u2014 \u05D4\u05D9\u05D5 {RCHUM}. \u05D1\u05D3\u05D9\u05D5\u05E7 \u05DB\u05E4\u05D9 \u05E9\u05D4\u05D5\u05D0 {CHNUN} \u2014 \u05D4\u05D9\u05D5 {CHNUN}. \u05D1\u05D3\u05D9\u05D5\u05E7 \u05DB\u05E4\u05D9 \u05E9\u05D4\u05D5\u05D0 {KDOSH} \u2014 \u05D4\u05D9\u05D5 \u05E7\u05D3\u05D5\u05E9\u05D9\u05DD. \u05D4\u05E0\u05D4 \u05D4\u05DE\u05E4\u05EA\u05D7: \u05DE\u05E6\u05D0\u05D5 \u05D0\u05EA \u05D3\u05E8\u05DA \u05D4\u05D0\u05DE\u05E6\u05E2 \u05D1\u05DB\u05DC \u05EA\u05DB\u05D5\u05E0\u05D5\u05EA\u05D9\u05DB\u05DD \u2014 \u05DC\u05D0 \u05E7\u05E9\u05D9\u05DD \u05DE\u05D3\u05D9, \u05DC\u05D0 \u05E8\u05DB\u05D9\u05DD \u05DE\u05D3\u05D9. \u05DE\u05E9\u05D9\u05DE\u05EA \u05D4\u05D9\u05D5\u05DD: \u05D1\u05D7\u05E8\u05D5 \u05EA\u05DB\u05D5\u05E0\u05D4 \u05D0\u05DC\u05D5\u05E7\u05D9\u05EA \u05D0\u05D7\u05EA (\u05D7\u05E1\u05D3, \u05E7\u05D3\u05D5\u05E9\u05D4 \u05D0\u05D5 {RCHUM}\u05D9\u05DD) \u05D5\u05D4\u05EA\u05DE\u05E7\u05D3\u05D5 \u05D1\u05D0\u05DE\u05EA \u05DC\u05E4\u05EA\u05D7 \u05D0\u05D5\u05EA\u05D4!",
        # remaining strings appended below via exec in main
    ],
}

# Full translation tables
DATA: dict[str, dict[str, list[str]]] = {}

def _load_data() -> None:
    global DATA
    import importlib.util
    spec = importlib.util.spec_from_file_location(
        "local_trans", Path(__file__).with_name("local_003_004_strings.py")
    )
    mod = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(mod)
    DATA.update(mod.DATA)

def main() -> None:
    _load_data()
    for batch in ("003", "004"):
        for lang in ("he", "es", "fr", "ru"):
            strings = DATA[batch][lang]
            if len(strings) != 25:
                raise ValueError(f"{batch}/{lang}: expected 25 strings, got {len(strings)}")
            path = HUMAN / f"local_{batch}_{lang}_only.json"
            path.write_text(
                json.dumps({lang: strings}, ensure_ascii=False, indent=2) + "\n",
                encoding="utf-8",
            )
            print(f"wrote {path.name}")

if __name__ == "__main__":
    main()
