# -*- coding: utf-8 -*-
"""Build local_003..006 *_only.json from keys, legacy import, and polished overrides."""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
HUMAN = ROOT / "human"
LEGACY = json.loads((ROOT / "shards" / "legacy_import.json").read_text(encoding="utf-8"))
OVERRIDES: dict[str, dict[str, str]] = json.loads(
    Path(__file__).with_name("local_missing_overrides.json").read_text(encoding="utf-8")
)

BATCHES = {
    "003": (25, ("es", "fr", "ru")),  # he already done separately
    "004": (25, ("he", "es", "fr", "ru")),
    "005": (25, ("es", "fr", "ru")),  # he already done
    "006": (18, ("he", "es", "fr", "ru")),
}


def translate(key: str, lang: str) -> str:
    if key in OVERRIDES and lang in OVERRIDES[key]:
        return OVERRIDES[key][lang]
    if key in LEGACY.get(lang, {}):
        return LEGACY[lang][key]
    raise KeyError(f"Missing {lang} for: {key[:72]!r}...")


def main() -> None:
    created: list[tuple[str, int]] = []
    for batch, (expected, langs) in BATCHES.items():
        keys = json.loads((ROOT / f"_keys_local_{batch}.json").read_text(encoding="utf-8"))
        if len(keys) != expected:
            raise ValueError(f"batch {batch}: expected {expected} keys, got {len(keys)}")
        for lang in langs:
            strings = [translate(k, lang) for k in keys]
            path = HUMAN / f"local_{batch}_{lang}_only.json"
            path.write_text(
                json.dumps({lang: strings}, ensure_ascii=False, indent=2) + "\n",
                encoding="utf-8",
            )
            created.append((path.name, len(strings)))
            print(f"wrote {path.name} ({len(strings)} strings)")
    print("\nSummary:")
    for name, count in created:
        print(f"  {name}: {count}")


if __name__ == "__main__":
    main()
