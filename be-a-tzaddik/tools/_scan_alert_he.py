#!/usr/bin/env python3
"""Scan Mitzvah Me alert strings for bad Hebrew."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
HUMAN = ROOT / "data" / "translation-catalog" / "human"
SOURCES = ["local_003.json", "local_005.json", "glossary_polish.json"]
HEBREW_FIXES = ROOT / "data" / "translation-catalog" / "shards" / "hebrew_fixes.json"

ALERT_PAT = re.compile(
    r"(Did you know|Today's mission|Today's challenge|Here's your mission|"
    r"Here's something|Your mission:|Take a moment|Here's the key|Here's your challenge)",
    re.I,
)
EMOJI_PAT = re.compile(r"[\U0001F300-\U0001FAFF\U00002600-\U000027BF]")
HYBRID = re.compile(r"[\u05d0-\u05ea][a-zA-Z]{2,}|[a-zA-Z]{2,}[\u05d0-\u05ea]")
LATIN_IN_HE = re.compile(
    r"[\u0590-\u05ff].{0,8}[A-Za-z]{3,}|[A-Za-z]{3,}.{0,8}[\u0590-\u05ff]"
)

MT_MARKERS = [
    "הידעת", "המשימה של היום", "האתגר של היום", "הנה המשימה שלך",
    "גלה את המכתב", "למד על", "שמור את ה", "המאסטר", "D-s", "Hilchot",
    "Kochavim", " TWO ", "Hakhel", "Vav (Bee)", "feat.", "Everze",
    "t Kochavim", "הילוצ", "קאף (בד)", "דיילט", "Zayin", "Tet (",
    "צ'ומאש", "התנצר", "שם בדוי", "הילכואבאט", "הילוצ'ואט",
    "Kaf (", "Chumash", "Tanach", "Hilcho", "Kochav", "Wi-Fi",
    "Sé un", "Conéctate", "Descubre", "¡Celebra",
]

ALLOWED_LATIN = {
    "Rambam", "Torah", "Talmud", "Shabbat", "Tehillim", "Shema", "Kiddush",
    "Havdalah", "Hallel", "Musaf", "Mincha", "Maariv", "Shacharit", "Ashrei",
    "Birkat", "Hamazon", "Kabbalists", "Chabad", "Sephard", "Ashkenaz",
    "Sefard", "gematria", "mitzvah", "mitzvot", "halacha", "mezuzah",
    "tefillin", "tzitzit", "kashrut", "Amen", "Wi", "Fi",
}


def is_alert_key(k: str) -> bool:
    if not EMOJI_PAT.search(k):
        return False
    if ALERT_PAT.search(k):
        return True
    return "!" in k and len(k) > 80


def is_bad_he(text: str) -> bool:
    if not text:
        return True
    if HYBRID.search(text):
        return True
    if LATIN_IN_HE.search(text):
        for w in re.findall(r"[A-Za-z]{3,}", text):
            if w not in ALLOWED_LATIN:
                return True
    if any(m in text for m in MT_MARKERS):
        return True
    return False


def main() -> None:
    fixes = json.loads(HEBREW_FIXES.read_text(encoding="utf-8")).get("he", {})
    alerts: dict[str, str] = {}
    bad: list[tuple[str, str, str]] = []

    for src in SOURCES:
        data = json.loads((HUMAN / src).read_text(encoding="utf-8"))
        for k, he in data.get("he", {}).items():
            if not is_alert_key(k):
                continue
            alerts[k] = he
            if is_bad_he(he):
                bad.append((src, k, he))

    print(f"Total alert keys: {len(alerts)}")
    print(f"Bad Hebrew: {len(bad)}")
    good_fixes = [k for k in bad if (k[1] in fixes and not is_bad_he(fixes[k[1]]))]
    print(f"Resolvable from hebrew_fixes: {len(good_fixes)}")
    for src, k, he in bad[:20]:
        print(f"\n[{src}] {k[:75]}...")
        print(f"  {he[:120]}...")


if __name__ == "__main__":
    main()
