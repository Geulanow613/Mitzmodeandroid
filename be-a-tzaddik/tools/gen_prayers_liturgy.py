#!/usr/bin/env python3
"""Generate human/prayers_liturgy.json from Kotlin prayer data + liturgical translations."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
KEYS = ROOT / "data/translation-catalog/_keys_prayers_liturgy.json"
OUT = ROOT / "data/translation-catalog/human/prayers_liturgy.json"
DATA = Path(__file__).resolve().parent / "prayers_liturgy_translations.json"


def unescape(s: str) -> str:
    return s.replace("\\n", "\n").replace('\\"', '"').replace("\\\\", "\\")


def parse_sections(text: str) -> list[dict[str, str | None]]:
    sections: list[dict[str, str | None]] = []
    for block in re.finditer(
        r"(?:BirkatSection|TefilatSection|BrachaSection)\(\s*([\s\S]*?)\n\s*\)",
        text,
    ):
        chunk = block.group(1)
        sec: dict[str, str | None] = {}
        for field in (
            "title",
            "hebrew",
            "english",
            "collapsibleSummary",
            "collapsibleSummaryEnglish",
        ):
            m = re.search(rf'{field}\s*=\s*"""([\s\S]*?)"""', chunk)
            if m:
                sec[field] = unescape(m.group(1))
                continue
            m = re.search(rf'{field}\s*=\s*"((?:[^"\\]|\\.)*)"', chunk)
            if m:
                sec[field] = unescape(m.group(1))
        if sec:
            sections.append(sec)
    return sections


def load_pairs() -> tuple[dict[str, str], dict[str, str]]:
    """en->he and he->en from Kotlin prayer sources."""
    base = ROOT.parent / "app/src/main/java/com/beardytop/mitzmode/data"
    en_to_he: dict[str, str] = {}
    he_to_en: dict[str, str] = {}
    for fname in ("BirkatHamazonText.kt", "TefilatHaderechData.kt", "BrachotData.kt"):
        for sec in parse_sections((base / fname).read_text(encoding="utf-8")):
            he = sec.get("hebrew")
            en = sec.get("english")
            if he and en:
                he_to_en[he] = en
                en_to_he[en] = he
            for field in ("title", "collapsibleSummary", "collapsibleSummaryEnglish"):
                val = sec.get(field)
                if not val:
                    continue
                if field == "collapsibleSummary" and sec.get("collapsibleSummaryEnglish"):
                    he_to_en.setdefault(val, sec["collapsibleSummaryEnglish"])
                    en_to_he.setdefault(sec["collapsibleSummaryEnglish"], val)
                elif field == "collapsibleSummaryEnglish" and sec.get("collapsibleSummary"):
                    en_to_he.setdefault(val, sec["collapsibleSummary"])
                    he_to_en.setdefault(sec["collapsibleSummary"], val)
    return en_to_he, he_to_en


def is_hebrew_key(key: str) -> bool:
    he = sum(1 for c in key if "\u0590" <= c <= "\u05ff")
    lat = sum(1 for c in key if "A" <= c <= "Z" or "a" <= c <= "z")
    return he > lat


def main() -> None:
    keys: list[str] = json.loads(KEYS.read_text(encoding="utf-8"))
    en_to_he, he_to_en = load_pairs()
    catalog: dict[str, dict[str, str]] = json.loads(DATA.read_text(encoding="utf-8"))

    out: dict[str, dict[str, str]] = {lang: {} for lang in ("he", "es", "fr", "ru")}

    for key in keys:
        # Resolve lookup id: prefer English counterpart for Hebrew body text
        lookup = key
        if key in catalog:
            lookup = key
        elif key in he_to_en and he_to_en[key] in catalog:
            lookup = he_to_en[key]
        elif key in en_to_he and key in catalog:
            lookup = key

        entry = catalog.get(lookup) or catalog.get(key)
        if not entry:
            raise KeyError(f"Missing translation entry for key starting: {key[:60]!r}...")

        if is_hebrew_key(key):
            out["he"][key] = key
        else:
            out["he"][key] = en_to_he.get(key, entry.get("he", key))

        for lang in ("es", "fr", "ru"):
            out[lang][key] = entry[lang]

    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps(out, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    for lang in ("he", "es", "fr", "ru"):
        print(f"{lang}: {len(out[lang])} keys")


if __name__ == "__main__":
    main()
