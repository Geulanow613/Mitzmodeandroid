"""Build human-quality local_003/004 *_only.json files from polished overrides."""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
HUMAN = ROOT / "human"
LEGACY = json.loads((ROOT / "shards" / "legacy_import.json").read_text(encoding="utf-8"))

KEYS = {
    "003": json.loads((ROOT / "_keys_local_003.json").read_text(encoding="utf-8")),
    "004": json.loads((ROOT / "_keys_local_004.json").read_text(encoding="utf-8")),
}

# Polished human-quality overrides keyed by exact English source string.
# Loaded from companion JSON to keep this script readable.
OVERRIDES: dict[str, dict[str, str]] = json.loads(
    Path(__file__).with_name("local_003_004_overrides.json").read_text(encoding="utf-8")
)


def translate(key: str, lang: str) -> str:
    if key in OVERRIDES and lang in OVERRIDES[key]:
        return OVERRIDES[key][lang]
    if key in LEGACY.get(lang, {}):
        return LEGACY[lang][key]
    raise KeyError(f"Missing translation: {lang!r} for {key[:80]!r}...")


def main() -> None:
    for batch, keys in KEYS.items():
        for lang in ("he", "es", "fr", "ru"):
            strings = [translate(k, lang) for k in keys]
            assert len(strings) == 25, f"{batch}/{lang}: expected 25, got {len(strings)}"
            out = {lang: strings}
            path = HUMAN / f"local_{batch}_{lang}_only.json"
            path.write_text(json.dumps(out, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
            print(f"wrote {path.name}")


if __name__ == "__main__":
    main()
