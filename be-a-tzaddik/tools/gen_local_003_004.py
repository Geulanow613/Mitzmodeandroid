"""Generate human-quality local_003/004 *_only.json files."""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
HUMAN = ROOT / "human"

# Translations indexed by batch -> lang -> list of 25 strings (key order in _keys_local_00N.json)
T: dict[str, dict[str, list[str]]] = {}

def _load():
    global T
    data_path = Path(__file__).with_name("local_003_004_translations.json")
    T.update(json.loads(data_path.read_text(encoding="utf-8")))

def main() -> None:
    _load()
    for batch in ("003", "004"):
        for lang in ("he", "es", "fr", "ru"):
            out = {lang: T[batch][lang]}
            path = HUMAN / f"local_{batch}_{lang}_only.json"
            path.write_text(
                json.dumps(out, ensure_ascii=False, indent=2) + "\n",
                encoding="utf-8",
            )
            print(f"wrote {path.name} ({len(out[lang])} strings)")

if __name__ == "__main__":
    main()
