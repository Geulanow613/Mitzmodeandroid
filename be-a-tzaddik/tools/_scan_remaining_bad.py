#!/usr/bin/env python3
"""Quick scan for remaining glossary/artifact issues in compiled bundles."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
BUNDLE_DIR = ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "translations"
BAD_ES = ROOT / "data" / "translation-catalog" / "_bad_glossary_es_keys.json"

ES_ARTIFACTS = [
    "Hashem Es",
    "específicosbrachot",
    "on Chanukah",
    "gracias Dios",
    "los Amidah",
    "después de bendecirse en el mezono",
    "Did you know",
    "Today's mission",
    "Baruch Atah Hashem",
    "halájico cuestión",
    "Dios.Purim",
    "Yom Tov-permitted",
    "recita over EL",
    "per many Ashkenaz",
    "llam preexistente",
    "C'est",
    "Baruch Atah Hashem C'est",
]

FR_ARTIFACTS = [
    "Baruch Atah Hashem C'est",
    "C'est une",
    "t rav",
    "g rav",
    "ma rav",
    "vacances",
    "maquillage",
    "dessin",
]

RU_ARTIFACTS = [
    "А.",
    "Ashkenaz i",
    "Sephardi",
]


def load_bundle(lang: str) -> dict[str, str]:
    data = json.loads((BUNDLE_DIR / f"{lang}.json").read_text(encoding="utf-8"))
    return data.get("entries", data)


def scan_es() -> None:
    bad_keys = json.loads(BAD_ES.read_text(encoding="utf-8"))
    bundle = load_bundle("es")
    still: list[tuple[str, list[str], str]] = []
    for k in bad_keys:
        v = bundle.get(k, "")
        issues = [p for p in ES_ARTIFACTS if p.lower() in v.lower()]
        en = len(re.findall(r"\b(the|and|with|when|that|this is|it is)\b", v, re.I))
        if issues or en >= 5:
            still.append((k, issues, v[:120]))
    print(f"ES bad glossary keys: {len(still)}/{len(bad_keys)} still problematic")
    for k, iss, v in still[:15]:
        print(f"  {iss or ['en-leak']}: {k[:65]}...")
        print(f"    => {v}...")


def scan_artifacts(lang: str, patterns: list[str]) -> None:
    bundle = load_bundle(lang)
    hits: list[tuple[str, str, str]] = []
    for k, v in bundle.items():
        if not isinstance(v, str):
            continue
        for p in patterns:
            if p.lower() in v.lower():
                hits.append((p, k[:60], v[:100]))
                break
    print(f"\n{lang.upper()} artifact hits: {len(hits)}")
    for p, k, v in hits[:20]:
        print(f"  [{p}] {k}... => {v}...")


def scan_he_english() -> None:
    bundle = load_bundle("he")
    bad: list[tuple[int, str, str]] = []
    for k, v in bundle.items():
        if not isinstance(v, str):
            continue
        he_ratio = len(re.findall(r"[\u0590-\u05ff]", v)) / max(len(v), 1)
        if he_ratio > 0.25:
            continue
        en = len(re.findall(r"\b(the|and|with|when|that|is a|means|This is|It is|G-d)\b", v, re.I))
        if en >= 3:
            bad.append((en, k[:55], v[:90]))
    bad.sort(reverse=True)
    print(f"\nHE mixed English: {len(bad)}")
    for en, k, v in bad[:15]:
        print(f"  [{en}] {k}... => {v}...")


if __name__ == "__main__":
    scan_es()
    scan_artifacts("es", ES_ARTIFACTS)
    scan_artifacts("fr", FR_ARTIFACTS)
    scan_artifacts("ru", RU_ARTIFACTS)
    scan_he_english()
