#!/usr/bin/env python3
"""Find mitzvah texts where catalog key is a truncated prefix of runtime source text."""
from __future__ import annotations

import json
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8")

ROOT = Path(__file__).resolve().parents[1]
REPO = ROOT.parent
CATALOG = ROOT / "data/translation-catalog/strings.json"


def load_texts(path: Path) -> list[str]:
    data = json.loads(path.read_text(encoding="utf-8"))
    items = data.get("mitzvot", [])
    return [m["text"].strip() for m in items if isinstance(m, dict) and m.get("text")]


def main() -> None:
    cloud = load_texts(REPO / "be-a-tzaddik/data/mitzvotcloud.json")
    local = load_texts(REPO / "app/src/main/assets/mitzvotlistfull.json")
    runtime = list(dict.fromkeys(cloud + local))

    cat = json.loads(CATALOG.read_text(encoding="utf-8"))
    strings: list[str] = cat["strings"]
    index = {s: i for i, s in enumerate(strings)}

    fixes: list[tuple[str, str]] = []
    missing: list[str] = []

    for full in runtime:
        if full in index:
            continue
        # truncated catalog key that is a prefix of runtime text
        prefix_hits = [s for s in strings if full.startswith(s) and s != full]
        if len(prefix_hits) == 1:
            fixes.append((prefix_hits[0], full))
        elif len(prefix_hits) > 1:
            # pick longest prefix
            old = max(prefix_hits, key=len)
            fixes.append((old, full))
        else:
            missing.append(full)

    print(f"Runtime unique mitzvot: {len(runtime)}")
    print(f"Prefix replacements needed: {len(fixes)}")
    print(f"No catalog match at all: {len(missing)}")

    for old, new in fixes[:8]:
        print(f"\nOLD ({len(old)}): {old[:80]}...")
        print(f"NEW ({len(new)}): {new[:80]}...")

    if missing:
        print("\n--- no match ---")
        for m in missing[:5]:
            print(m[:100])

    out = ROOT / "data/translation-catalog/mitzvot-key-fixes.json"
    out.write_text(
        json.dumps({"replacements": [{"old": o, "new": n} for o, n in fixes], "missing": missing}, ensure_ascii=False, indent=2),
        encoding="utf-8",
    )
    print(f"\nwrote {out}")


if __name__ == "__main__":
    main()
