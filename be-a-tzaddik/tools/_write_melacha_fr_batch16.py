#!/usr/bin/env python3
"""Write human/melacha_fr_batch16.json from inline FR rewrites."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]

# Import translations from content module
from _melacha_fr_batch16_content import MELACHA_FR  # noqa: E402


def resolve() -> dict[str, str]:
    out: dict[str, str] = {}
    for prefix, fr in MELACHA_FR.items():
        matches = [k for k in CATALOG if k.startswith(f"Learn about the Melacha of {prefix}")]
        if not matches:
            raise KeyError(f"no catalog key for Melacha prefix {prefix!r}")
        out[max(matches, key=len)] = fr
    return out


def main() -> None:
    data = resolve()
    path = ROOT / "data/translation-catalog/human/melacha_fr_batch16.json"
    path.write_text(json.dumps(data, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Wrote {len(data)} entries to {path}")


if __name__ == "__main__":
    main()
