#!/usr/bin/env python3
"""Add Shabbat Guide / 39 Melachot explainer bodies to the translation catalog."""

from __future__ import annotations

import json
import re
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DATA = ROOT / "data" / "translation-catalog"
COMPOSE = ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "translations"
LANGS = ("he", "es", "fr", "ru")


def extract_guide_strings() -> list[str]:
    guide_kt = (ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/ui/screens/ShabbatGuideData.kt").read_text(
        encoding="utf-8"
    )
    screen_kt = (ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/ui/screens/ShabbatGuideScreen.kt").read_text(
        encoding="utf-8"
    )
    strings: list[str] = []
    strings.extend(re.findall(r'body\s*=\s*"""(.*?)"""', guide_kt, re.DOTALL))
    strings.extend(
        m.group(1)
        for m in re.finditer(
            r'GuideTopic\(\s*"[^"]+",\s*"[^"]+",\s*"[^"]*",\s*\n?\s*"([^"]+)"',
            guide_kt,
        )
    )
    for m in re.finditer(r'BodyCard\(\s*\n?\s*"([^"]+)"', screen_kt):
        strings.append(m.group(1))
    for m in re.finditer(r'BodyCard\("([^"]+)"', screen_kt):
        strings.append(m.group(1))
    # Concatenated BodyCard on MelachotListPage
    concat = re.search(
        r'BodyCard\(\s*\n?\s*"([^"]+)"\s*\+\s*\n?\s*"([^"]+)"',
        screen_kt,
    )
    if concat:
        strings.append(concat.group(1) + concat.group(2))
    # Comparison table cells
    for m in re.finditer(r'row\.(activity|shabbat|yomTov)', screen_kt):
        pass
    for block in re.findall(
        r'ComparisonRow\(\s*\n?\s*"([^"]+)",\s*\n?\s*"([^"]+)",\s*\n?\s*"([^"]+)"\s*\)',
        guide_kt,
        re.DOTALL,
    ):
        strings.extend(block)
    seen: set[str] = set()
    out: list[str] = []
    for s in strings:
        s = s.strip()
        if s and s not in seen:
            seen.add(s)
            out.append(s)
    return out


def load_bundle(lang: str) -> dict[str, str]:
    path = COMPOSE / f"{lang}.json"
    if not path.exists():
        return {}
    return json.loads(path.read_text(encoding="utf-8")).get("entries", {})


def make_translator():
    try:
        import argostranslate.translate as argos

        cache: dict[tuple[str, str], object] = {}

        def translate(text: str, lang: str) -> str:
            key = ("en", lang)
            if key not in cache:
                cache[key] = argos.get_translation_from_codes("en", lang)
            return cache[key].translate(text)

        return translate
    except Exception:
        pass
    try:
        from deep_translator import GoogleTranslator

        clients: dict[str, GoogleTranslator] = {}

        def translate(text: str, lang: str) -> str:
            if lang not in clients:
                clients[lang] = GoogleTranslator(source="en", target=lang)
            # deep-translator uses 'iw' for Hebrew sometimes
            target = "iw" if lang == "he" else lang
            if lang not in clients:
                clients[lang] = GoogleTranslator(source="en", target=target)
            return clients[lang].translate(text)

        return translate
    except Exception as exc:
        raise RuntimeError("No translation backend available (argos or deep-translator)") from exc


def main() -> None:
    keys = extract_guide_strings()
    print(f"Extracted {len(keys)} Shabbat guide strings")

    catalog_path = DATA / "strings.json"
    catalog = json.loads(catalog_path.read_text(encoding="utf-8"))
    existing = set(catalog["strings"])
    added = [k for k in keys if k not in existing]
    if added:
        catalog["strings"].extend(added)
        catalog["count"] = len(catalog["strings"])
        catalog_path.write_text(json.dumps(catalog, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
        print(f"Added {len(added)} keys to strings.json")

    bundles = {lang: load_bundle(lang) for lang in LANGS}
    shard: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    translate = make_translator()

    for key in keys:
        for lang in LANGS:
            if key in bundles[lang] and bundles[lang][key] != key:
                shard[lang][key] = bundles[lang][key]
                continue
            if lang == "en":
                continue
            try:
                shard[lang][key] = translate(key, lang)
            except Exception as exc:
                print(f"WARN: failed {lang} for {key[:60]!r}: {exc}", file=sys.stderr)

    out = DATA / "human" / "shabbat_guide.json"
    out.write_text(json.dumps(shard, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    for lang in LANGS:
        print(f"  {lang}: {len(shard[lang])} entries -> shabbat_guide.json")

    subprocess.run([sys.executable, str(ROOT / "tools" / "compile_full_bundled.py")], check=True)


if __name__ == "__main__":
    main()
