#!/usr/bin/env python3
"""Emit he_fix_009/011/012_he_only.json — pure Hebrew translations."""
from __future__ import annotations

import json
import re
import sys
from pathlib import Path

_TOOLS = Path(__file__).resolve().parent
if str(_TOOLS) not in sys.path:
    sys.path.insert(0, str(_TOOLS))

from build_he789_final import SELICHOT, SHEMINI_BEGINS  # noqa: E402
from build_he_fix_all import (  # noqa: E402
    build_he_011 as _build_he_011_raw,
    build_he_012 as _build_he_012_raw,
    fix_mixed,
    hb,
)
from make_custom import (  # noqa: E402
    SEPHARDI,
    SEPHARDI_OMER,
    SHAVUOT,
    SHEKHINAH,
    SHEMA_HAMITAH,
    SHEMINI_DIASPORA,
    SHEMINI_ISRAEL,
    SHEMINI_OVERVIEW,
)

ROOT = _TOOLS.parent / "data" / "translation-catalog"
HUMAN = ROOT / "human"

MIXED_RE = re.compile(
    r"[\u0590-\u05FF]+[A-Za-z\u0400-\u04FF]+|[A-Za-z\u0400-\u04FF]+[\u0590-\u05FF]+"
)
PH_SKIP = (
    "HebrewCalendar",
    "profile",
    "tomorrowCal",
    "fullSchedule",
    "alotTomorrow",
    "diasporaSecond",
)

LL = hb(0x05DC, 0x05D5, 0x05DC, 0x05D1)
TKN = hb(
    0x05EA, 0x05D9, 0x05E7, 0x05D5, 0x05DF, 0x0020, 0x05DC, 0x05D9, 0x05DC,
    0x0020, 0x05E9, 0x05D1, 0x05D5, 0x05E2, 0x05D5, 0x05EA,
)
HMPIL = hb(0x05D4, 0x05DE, 0x05D0, 0x05E4, 0x05D9, 0x05DC)
YECHAVEH = hb(0x05D9, 0x05D7, 0x05D5, 0x05D5, 0x05D4, 0x0020, 0x05D3, 0x05E2, 0x05EA)
BAR_MZ = hb(0x05D1, 0x05E8, 0x0020, 0x05DE, 0x05E6, 0x05D5, 0x05D4)
HMITAH = hb(0x05D4, 0x05DE, 0x05D9, 0x05D8, 0x05D4)

SEPHARDI_FULL = (
    SEPHARDI.replace(
        "\u05E2\u05DD \u05E2\u05D3\u05D5\u05EA \u05D4\u05DE\u05D6\u05E8\u05D7.",
        "\u05E2\u05DD \u05E2\u05D3\u05D5\u05EA \u05D4\u05DE\u05D6\u05E8\u05D7 "
        "(\u05E0\u05D5\u05E1\u05D7 \u05DE\u05D6\u05E8\u05D7\u05D9 / \u05E6\u05E4\u05D5\u05DF-\u05D0\u05E4\u05E8\u05D9\u05E7\u05D0\u05D9).",
    )
)


def _strip_title(text: str) -> str:
    for sep in (" — ", " \u2014 "):
        if sep in text:
            return text.split(sep, 1)[1]
    return text


def _load_reuse() -> dict[str, str]:
    he016 = json.loads((HUMAN / "he_fix_016_he_only.json").read_text(encoding="utf-8"))["he"]
    he017 = json.loads((HUMAN / "he_fix_017_he_only.json").read_text(encoding="utf-8"))["he"]
    return {
        "seudah_shlishit": _strip_title(he016[13]),
        "shechita": _strip_title(he017[0]),
        "shloshim": _strip_title(he017[3]),
    }


def check_pure(strings: list[str], label: str) -> None:
    bad = []
    for i, s in enumerate(strings):
        for m in MIXED_RE.finditer(s):
            g = m.group()
            if any(skip in g for skip in PH_SKIP):
                continue
            bad.append((i + 1, g))
    if bad:
        raise SystemExit(f"{label} mixed Latin/Cyrillic in Hebrew: {bad}")


def write_batch(name: str, strings: list[str]) -> None:
    strings = [fix_mixed(s) for s in strings]
    check_pure(strings, name)
    keys = json.loads((ROOT / f"_keys_{name}.json").read_text(encoding="utf-8"))
    if len(strings) != len(keys):
        raise SystemExit(f"{name}: {len(strings)} strings != {len(keys)} keys")
    path = HUMAN / f"{name}_he_only.json"
    path.write_text(
        json.dumps({"he": strings}, ensure_ascii=False, indent=2) + "\n",
        encoding="utf-8",
    )
    print(f"OK {path.name}: {len(strings)} strings")


def build_he_011() -> list[str]:
    strings = _build_he_011_raw()
    strings[6] = strings[6].replace("קודекс", "ספר")
    return strings


def build_he_012() -> list[str]:
    return _build_he_012_raw()


def _fix_sephardi_omer(s: str) -> str:
    return (
        s.replace("Yechaveh Daat", YECHAVEH)
        .replace(
            "\u05E0\u05E8\u05D5\u05E5",
            "\u05E0\u05E4\u05D5\u05E5",
        )
    )


def _fix_shavuot(s: str) -> str:
    return s.replace("(Tikkun Leil Shavuot)", f"({TKN})")


def _fix_shema(s: str) -> str:
    return s.replace("\u00ABHamapil\u00BB", f"\u00AB{HMPIL}\u00BB")


def _fix_shemini_diaspora(s: str) -> str:
    return (
        s.replace(
            "\u05DB\u05D9\u05E1\u05D0 \u05D4\u05E2\u05D5\u05DC\u05DD",
            "\u05DB\u05DC \u05D4\u05E2\u05D5\u05DC\u05DD",
        )
        .replace(" lulav ", f" {LL} ")
        .replace("\u05D0\u05D9\u05DF lulav ", f"\u05D0\u05D9\u05DF {LL} ")
        .replace(" lulav ", f" {LL} ")
    )


def _fix_shemini_israel(s: str) -> str:
    return (
        s.replace(
            "\u05DB\u05D9\u05E1\u05D0 \u05D4\u05E2\u05D5\u05DC\u05DD",
            "\u05DB\u05DC \u05D4\u05E2\u05D5\u05DC\u05DD",
        )
        .replace(
            "\u05D5\u05E9\u05D4\u05D7\u05D9\u05E0\u05D5",
            "\u05D5" + hb(0x05E9, 0x05D4, 0x05D7, 0x05D9, 0x05E0, 0x05D5),
        )
        .replace("bar mitzvah", BAR_MZ)
    )


def build_he_009() -> list[str]:
    reuse = _load_reuse()
    shema = _fix_shema(SHEMA_HAMITAH)
    return [
        f"\u05E1\u05DC\u05D9\u05D7\u05D5\u05EA \u2014 {SELICHOT}",
        SEPHARDI_FULL,
        _fix_sephardi_omer(SEPHARDI_OMER),
        f"\u05E1\u05E4\u05E8\u05D3\u05D9 \u2014 {SEPHARDI_FULL}",
        reuse["seudah_shlishit"],
        _fix_shavuot(SHAVUOT),
        reuse["shechita"],
        SHEKHINAH,
        shema,
        f"\u05E9\u05DE\u05E2 \u05E2\u05DC {HMITAH} \u2014 {shema}",
        _fix_shemini_diaspora(SHEMINI_DIASPORA),
        _fix_shemini_israel(SHEMINI_ISRAEL),
        SHEMINI_BEGINS.replace(" lulav ", f" {LL} ").replace(
            "\u05D0\u05D9\u05DF lulav ", f"\u05D0\u05D9\u05DF {LL} "
        ),
        SHEMINI_OVERVIEW,
        reuse["shloshim"],
    ]


if __name__ == "__main__":
    write_batch("he_fix_009", build_he_009())
    write_batch("he_fix_011", build_he_011())
    write_batch("he_fix_012", build_he_012())
    print("All 3 batches written.")
