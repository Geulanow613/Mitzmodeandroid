#!/usr/bin/env python3
"""Write human/he_short_learn_expansions.json — Hebrew Learn-about alerts at 400+ chars."""

from __future__ import annotations

import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TOOLS = Path(__file__).resolve().parent
sys.path.insert(0, str(TOOLS))

from _he_short_learn_expansions_data import HE_SHORT_LEARN, PREFIX_HE_SHORT_EXPAND  # noqa: E402

CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
OUT = ROOT / "data" / "translation-catalog" / "human" / "he_short_learn_expansions.json"
MIN_LEN = 400


def main() -> int:
    catalog: list[str] = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    out: dict[str, str] = {}

    for key in HE_SHORT_LEARN:
        if key in catalog:
            out[key] = HE_SHORT_LEARN[key]

    prefix_added = 0
    for key in catalog:
        if key in out:
            continue
        if "Learn about" not in key and not key.startswith(
            (
                "Conquer your grudges",
                "Connect to our future",
                "Gather for inspiration",
                "Get creative with Mishloach",
                "Discover the secret of the priestly",
                "Be a judge of merit",
                "Purim joy includes everyone",
                "Step into the story of the Exodus",
                "Light up the night",
                "Ready to launch into Shabbat",
                "Protect the world",
            )
        ):
            continue
        best: str | None = None
        best_len = 0
        for prefix, val in PREFIX_HE_SHORT_EXPAND.items():
            if key.startswith(prefix) and len(val) > best_len:
                best = val
                best_len = len(val)
        if best is not None and len(best) >= MIN_LEN:
            out[key] = best
            prefix_added += 1

    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(
        json.dumps({"he": out}, ensure_ascii=False, indent=2) + "\n",
        encoding="utf-8",
    )
    short = sum(1 for k, v in out.items() if len(v) < MIN_LEN)
    print(
        f"wrote {len(out)} keys to {OUT.name} "
        f"(exact: {len(HE_SHORT_LEARN)}, prefix-added: {prefix_added}, under {MIN_LEN}: {short})"
    )
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
