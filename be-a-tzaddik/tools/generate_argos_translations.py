#!/usr/bin/env python3
"""Generate offline bundled translation shards using Argos Translate (no Google API)."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
SHARDS = ROOT / "data" / "translation-catalog" / "shards"
LEGACY_SHARD = SHARDS / "legacy_import.json"
LANGS = ("he", "es", "fr", "ru")
ARGOS_CODES = {"he": "he", "es": "es", "fr": "fr", "ru": "ru"}

# Protect tokens from translation
PLACEHOLDER_RE = re.compile(
    r"(\$\w+|\[[^\]]+\]\([^)]+\)|https?://\S+|www\.\S+|\bBaruch Atah\b[^\"]*\"[^\"]*\"|"
    r"[\u0590-\u05FF\u05F0-\u05FF]+)"
)


def install_packages() -> None:
    import argostranslate.package

    argostranslate.package.update_package_index()
    available = argostranslate.package.get_available_packages()
    for lang in LANGS:
        code = ARGOS_CODES[lang]
        pkg = next(
            (p for p in available if p.from_code == "en" and p.to_code == code),
            None,
        )
        if pkg is None:
            print(f"warning: no argos package en->{code}")
            continue
        if not any(
            ip.from_code == "en" and ip.to_code == code
            for ip in argostranslate.package.get_installed_packages()
        ):
            print(f"installing en->{code}...")
            argostranslate.package.install_from_path(pkg.download())


def get_translator(to_code: str):
    import argostranslate.translate

    for t in argostranslate.translate.get_installed_languages():
        if t.code == "en":
            en = t
            break
    else:
        raise RuntimeError("English not installed")
    for t in argostranslate.translate.get_installed_languages():
        if t.code == to_code:
            return en.get_translation(t)
    raise RuntimeError(f"No translator en->{to_code}")


def protect(text: str) -> tuple[str, list[str]]:
    tokens: list[str] = []

    def repl(m: re.Match[str]) -> str:
        tokens.append(m.group(0))
        return f"⟦{len(tokens)-1}⟧"

    return PLACEHOLDER_RE.sub(repl, text), tokens


def restore(text: str, tokens: list[str]) -> str:
    for i, tok in enumerate(tokens):
        text = text.replace(f"⟦{i}⟧", tok)
    return text


def _translate_chunk(translator, text: str) -> str:
    protected, tokens = protect(text)
    try:
        translated = translator.translate(protected)
    except Exception as e:
        print(f"translate error: {e!r} for {text[:60]!r}", flush=True)
        return text
    return restore(translated, tokens)


def translate_text(translator, text: str) -> str:
    if not text.strip():
        return text
    max_chunk = 450
    if len(text) <= max_chunk:
        return _translate_chunk(translator, text)

    parts: list[str] = []
    i = 0
    while i < len(text):
        end = min(i + max_chunk, len(text))
        if end < len(text):
            nl = text.rfind("\n", i, end)
            if nl > i + max_chunk // 3:
                end = nl + 1
        parts.append(_translate_chunk(translator, text[i:end]))
        i = end
    return "".join(parts)


def load_existing() -> dict[str, dict[str, str]]:
    out: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    if not SHARDS.exists():
        return out
    for path in sorted(SHARDS.glob("*.json")):
        if path.name == "argos_full.json":
            continue
        data = json.loads(path.read_text(encoding="utf-8"))
        for lang in LANGS:
            for en, tr in data.get(lang, {}).items():
                if tr and tr != en:
                    out[lang][en] = tr
    argos_shard = SHARDS / "argos_full.json"
    if argos_shard.exists():
        data = json.loads(argos_shard.read_text(encoding="utf-8"))
        for lang in LANGS:
            out[lang].update(data.get(lang, {}))
    return out


def main() -> None:
    gap_path: Path | None = None
    if len(sys.argv) >= 3 and sys.argv[1] == "--gap":
        gap_path = Path(sys.argv[2])

    install_packages()
    if gap_path and gap_path.exists():
        strings = json.loads(gap_path.read_text(encoding="utf-8"))["strings"]
    else:
        strings = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    existing = load_existing()
    result: dict[str, dict[str, str]] = {lang: dict(existing[lang]) for lang in LANGS}

    translators = {lang: get_translator(ARGOS_CODES[lang]) for lang in LANGS}

    total = len(strings)
    for i, s in enumerate(strings):
        for lang in LANGS:
            if s in result[lang] and result[lang][s] != s:
                continue
            result[lang][s] = translate_text(translators[lang], s)
        if (i + 1) % 10 == 0:
            print(f"progress {i+1}/{total}")
            SHARDS.mkdir(parents=True, exist_ok=True)
            (SHARDS / "argos_full.json").write_text(
                json.dumps(result, ensure_ascii=False), encoding="utf-8"
            )

    SHARDS.mkdir(parents=True, exist_ok=True)
    (SHARDS / "argos_full.json").write_text(
        json.dumps(result, ensure_ascii=False, indent=2), encoding="utf-8"
    )
    for lang in LANGS:
        done = sum(1 for s in strings if result[lang].get(s, s) != s)
        print(f"{lang}: {done}/{total} in this run")


if __name__ == "__main__":
    main()
