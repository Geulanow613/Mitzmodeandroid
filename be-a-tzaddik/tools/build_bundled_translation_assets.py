#!/usr/bin/env python3
"""Validate bundled translation JSON and copy into composeResources."""

from __future__ import annotations

import json
import shutil
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
STRINGS = ROOT / "data" / "translation-strings.json"
SRC_DIR = ROOT / "data" / "bundled-translations"
OUT_DIR = ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "translations"
LANGS = ("he", "es", "fr", "ru")


def main() -> None:
    catalog = json.loads(STRINGS.read_text(encoding="utf-8"))
    required = catalog["strings"]
    OUT_DIR.mkdir(parents=True, exist_ok=True)

    for lang in LANGS:
        src = SRC_DIR / f"{lang}.json"
        if not src.exists():
            raise SystemExit(f"Missing {src}")
        data = json.loads(src.read_text(encoding="utf-8"))
        entries = data.get("entries", {})
        missing = [s for s in required if s not in entries]
        extra = [k for k in entries if k not in required]
        if missing:
            print(f"{lang}: missing {len(missing)} / {len(required)}")
            for m in missing[:10]:
                print(f"  - {m[:80]}...")
            raise SystemExit(f"{lang}.json incomplete")
        if extra:
            print(f"{lang}: warning {len(extra)} extra keys (kept)")

        out = {
            "version": 1,
            "language": lang,
            "entries": {s: entries[s] for s in required},
        }
        dest = OUT_DIR / f"{lang}.json"
        dest.write_text(json.dumps(out, ensure_ascii=False, indent=2), encoding="utf-8")
        print(f"Wrote {dest} ({len(required)} entries)")

    print("Done.")


if __name__ == "__main__":
    main()
