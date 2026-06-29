#!/usr/bin/env python3
"""Write long-text bundled translation files for all bundled languages."""

from __future__ import annotations

from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "data" / "bundled-translations"


def write_lang(lang: str, texts: list[str]) -> None:
    folder = OUT / "long" / lang
    folder.mkdir(parents=True, exist_ok=True)
    count = len(list(OUT.glob("long-en-*.txt")))
    if len(texts) != count:
        raise SystemExit(f"{lang}: expected {count} long texts, got {len(texts)}")
    for i, text in enumerate(texts):
        (folder / f"{i:02d}.txt").write_text(text, encoding="utf-8")
    print(f"Wrote {len(texts)} long {lang} files")


# Index order matches long-en-00.txt … long-en-41.txt (synced to translation-strings.json).
# Each list: Hebrew, Spanish, French, Russian for the same English source.

def load_en() -> list[str]:
    files = sorted(OUT.glob("long-en-*.txt"))
    return [p.read_text(encoding="utf-8") for p in files]


def main() -> None:
    from long_translation_content import HE, ES, FR, RU  # noqa: WPS433

    en = load_en()
    for lang, texts in ("he", HE), ("es", ES), ("fr", FR), ("ru", RU):
        if len(texts) != len(en):
            raise SystemExit(f"{lang} length {len(texts)} != en {len(en)}")
    write_lang("he", HE)
    write_lang("es", ES)
    write_lang("fr", FR)
    write_lang("ru", RU)


if __name__ == "__main__":
    main()
