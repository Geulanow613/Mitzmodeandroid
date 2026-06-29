#!/usr/bin/env python3
"""Build shards 014-019 from base translations + derived term-prefix entries."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
LANGS = ("he", "es", "fr", "ru")

HEBREW_RE = re.compile(r"[\u0590-\u05FF]")


def load_all_strings() -> list[str]:
    out: list[str] = []
    for i in range(14, 20):
        batch = json.loads((ROOT / "batches" / f"batch_{i:03d}.json").read_text(encoding="utf-8"))
        out.extend(batch["strings"])
    return out


def derive_term_prefix(term: str, lang: str) -> str:
    """Lightweight term gloss for derived keys like 'karpas — ...'."""
    gloss = {
        "he": {
            "karpas": "כרפס", "levi": "לוי", "machzor": "מחzor", "oleh": "עולה",
            "mussar": "מוסר", "koteiv": "כותב", "kashrut": "כשרות", "sukkot": "סוכות",
            "dayeinu": "דיינו", "hotza'ah": "הוצאה", "bishul": "בישול", "chumash": "חומש",
            "baal koreh": "בaal koreh", "chatzos": "חצות", "gan eden": "גan eden",
            "trop": "טrop", "aliyah": "עלייה", "chuppah": "חuppa", "patur": "פatur",
            "maror": "מרor", "elul": "אלul", "shavuot": "שavuot", "siddur": "סiddur",
        },
    }
    low = term.lower()
    if lang == "he" and low in gloss["he"]:
        return gloss["he"][low]
    return term


def build_lookup(bases: dict[str, dict[str, str]], all_strings: list[str]) -> dict[str, dict[str, str]]:
    lookup = dict(bases)
    all_set = set(all_strings)
    for s in all_strings:
        if s in lookup:
            continue
        if " — " not in s:
            continue
        term, rest = s.split(" — ", 1)
        if rest not in all_set or rest not in lookup:
            continue
        lookup[s] = {
            lang: f"{derive_term_prefix(term, lang)} — {lookup[rest][lang]}"
            for lang in LANGS
        }
    return lookup


def main() -> None:
    from tr_bases import BASES  # noqa: WPS433

    all_strings = load_all_strings()
    lookup = build_lookup(BASES, all_strings)
    missing = [s for s in all_strings if s not in lookup]
    if missing:
        raise SystemExit(f"Missing {len(missing)} translations. First: {missing[0][:120]!r}")

    out_dir = ROOT / "shards"
    out_dir.mkdir(parents=True, exist_ok=True)
    grand = 0
    offset = 0
    for batch_num in range(14, 20):
        batch = json.loads((ROOT / "batches" / f"batch_{batch_num:03d}.json").read_text(encoding="utf-8"))
        shard = {lang: {} for lang in LANGS}
        for s in batch["strings"]:
            for lang in LANGS:
                shard[lang][s] = lookup[s][lang]
        path = out_dir / f"batch_{batch_num:03d}.json"
        path.write_text(json.dumps(shard, ensure_ascii=False, indent=2), encoding="utf-8")
        n = len(shard["he"])
        grand += n
        print(f"batch_{batch_num:03d}.json: {n} keys x 4 langs")
        offset += n
    print(f"TOTAL: {grand} strings")


if __name__ == "__main__":
    main()
