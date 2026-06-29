#!/usr/bin/env python3
"""Count Hebrew entries with high Latin-letter ratio (excluding Kotlin placeholders)."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


def latin_ratio(text: str) -> float:
    stripped = re.sub(r"\$\{[^}]*\}|\$[a-zA-Z_][a-zA-Z0-9_]*", "", text)
    letters = [c for c in stripped if c.isalpha()]
    if not letters:
        return 0.0
    return sum(1 for c in letters if c.isascii()) / len(letters)


def is_kotlin_template(text: str) -> bool:
    return bool(re.search(r"\$\{[^}]*\}|\$[a-zA-Z_][a-zA-Z0-9_]*", text))


def main() -> None:
    req = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
    he = json.loads(
        (ROOT / "shared/src/commonMain/composeResources/files/translations/he.json").read_text(encoding="utf-8")
    )["entries"]

    rows = []
    for s in req:
        tr = he.get(s, s)
        if tr == s or len(tr) < 12:
            continue
        ratio = latin_ratio(tr)
        if ratio > 0.25:
            # Kotlin templates: only flag if Hebrew prose outside placeholders is Latin-heavy
            if is_kotlin_template(tr) and latin_ratio(re.sub(r"\$\{[^}]*\}|\$[a-zA-Z_][a-zA-Z0-9_]*", "", tr)) <= 0.25:
                continue
            rows.append({"key": s, "ratio": round(ratio, 3), "he": tr})

    rows.sort(key=lambda x: (-x["ratio"], len(x["key"])))
    out = ROOT / "data/translation-catalog/he-mixed-keys.json"
    out.write_text(json.dumps(rows, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"{len(rows)} mixed keys (ratio>0.25) -> {out}")


if __name__ == "__main__":
    main()
