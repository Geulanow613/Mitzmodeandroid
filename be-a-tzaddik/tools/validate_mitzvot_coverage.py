#!/usr/bin/env python3
"""Verify all mitzvah texts from cloud + local sources have human translations in bundles."""
from __future__ import annotations

import json
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8")

ROOT = Path(__file__).resolve().parents[1]
REPO = ROOT.parent
LANGS = ("he", "es", "fr", "ru")


def load_mitzvah_texts(path: Path, field: str = "text") -> list[str]:
    if not path.is_file():
        return []
    data = json.loads(path.read_text(encoding="utf-8"))
    items = data.get("mitzvot", data if isinstance(data, list) else [])
    out: list[str] = []
    for item in items:
        if isinstance(item, dict):
            t = item.get(field) or item.get("Text") or item.get("mitzvah")
            if t:
                out.append(t.strip())
        elif isinstance(item, str):
            out.append(item.strip())
    return out


def main() -> None:
    cloud = load_mitzvah_texts(REPO / "be-a-tzaddik/data/mitzvotcloud.json")
    local = load_mitzvah_texts(REPO / "app/src/main/assets/mitzvotlistfull.json")
    # Also check be-a-tzaddik copy if present
    alt = REPO / "be-a-tzaddik/data/mitzvotlistfull.json"
    if alt.is_file():
        local = load_mitzvah_texts(alt) or local

    unique = list(dict.fromkeys(cloud + local))
    bundles = {
        lang: json.loads(
            (ROOT / f"shared/src/commonMain/composeResources/files/translations/{lang}.json").read_text(
                encoding="utf-8"
            )
        )["entries"]
        for lang in LANGS
    }

    print(f"Cloud mitzvot: {len(cloud)}")
    print(f"Local mitzvot: {len(local)}")
    print(f"Unique texts:  {len(unique)}")

    for lang in LANGS:
        fb = [t for t in unique if bundles[lang].get(t, t) == t]
        ok = len(unique) - len(fb)
        print(f"\n{lang}: {ok}/{len(unique)} translated ({len(fb)} English fallback)")
        for t in fb[:5]:
            print(f"  - {t[:90]}...")
        if len(fb) > 5:
            print(f"  ... and {len(fb) - 5} more")


if __name__ == "__main__":
    main()
