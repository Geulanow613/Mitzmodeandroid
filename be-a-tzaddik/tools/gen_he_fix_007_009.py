#!/usr/bin/env python3
"""Assemble and validate he_fix_007/008/009_he_only.json."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
HUMAN = ROOT / "human"
TOOLS = Path(__file__).resolve().parent

MIXED_RE = re.compile(
    r"[\u0590-\u05FF]+[A-Za-z\u0400-\u04FF]+|[A-Za-z\u0400-\u04FF]+[\u0590-\u05FF]+"
)
PH_SKIP = ("HebrewCalendar", "profile", "tomorrowCal", "java.util", "java.text", "Mitz Mode")


def extract_placeholders(s: str) -> list[str]:
    out: list[str] = []
    i = 0
    while i < len(s):
        if s[i] != "$":
            i += 1
            continue
        if i + 1 < len(s) and s[i + 1] == "{":
            depth = 0
            j = i + 1
            while j < len(s):
                if s[j] == "{":
                    depth += 1
                elif s[j] == "}":
                    depth -= 1
                    if depth == 0:
                        out.append(s[i : j + 1])
                        i = j + 1
                        break
                j += 1
            else:
                i += 1
        else:
            j = i + 1
            while j < len(s) and (s[j].isalnum() or s[j] in "_.$"):
                j += 1
            if j < len(s) and s[j] == "(":
                depth = 1
                j += 1
                while j < len(s) and depth:
                    if s[j] == "(":
                        depth += 1
                    elif s[j] == ")":
                        depth -= 1
                    j += 1
            out.append(s[i:j])
            i = j
    return out


def check_pure(strings: list[str], label: str) -> None:
    bad = []
    for idx, s in enumerate(strings):
        for m in MIXED_RE.finditer(s):
            g = m.group()
            if any(skip in g for skip in PH_SKIP):
                continue
            bad.append((idx + 1, g))
    if bad:
        raise SystemExit(f"{label} mixed: {bad}")


def normalize_placeholders(phs: list[str]) -> list[str]:
    """Kotlin if-blocks may translate inner string literals; code tokens must match."""
    out: list[str] = []
    for ph in phs:
        if ph.startswith("${if "):
            out.append(re.sub(r'"[^"]*"', '"*"', ph))
        else:
            out.append(ph)
    return out


def check_ph(keys: list[str], he: list[str], label: str) -> None:
    for i, (k, t) in enumerate(zip(keys, he)):
        if normalize_placeholders(extract_placeholders(k)) != normalize_placeholders(
            extract_placeholders(t)
        ):
            raise SystemExit(f"{label} [{i+1}] ph mismatch")
    print(f"PLACEHOLDERS OK {label}: {sum(len(extract_placeholders(k)) for k in keys)} tokens")


def write_batch(name: str, strings: list[str]) -> None:
    check_pure(strings, name)
    keys = json.loads((ROOT / f"_keys_{name}.json").read_text(encoding="utf-8"))
    if len(strings) != len(keys):
        raise SystemExit(f"{name}: {len(strings)} != {len(keys)}")
    check_ph(keys, strings, name)
    path = HUMAN / f"{name}_he_only.json"
    path.write_text(json.dumps({"he": strings}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"OK {path.name}: {len(strings)}")


# Unicode-safe constants from build_he789_final
from build_he789_final import (  # noqa: E402
    RECHILUT,
    RAV,
    ROSH_HASHANA,
    SCHACH,
    SEFIRAT_OMER,
    SELICHOT,
    SHEMINI_BEGINS,
)

UTF8 = json.loads((TOOLS / "he789_utf8.json").read_text(encoding="utf-8"))
CUSTOM = json.loads((TOOLS / "he789_custom.json").read_text(encoding="utf-8"))

HE_007 = json.loads((HUMAN / "he_fix_007_he_only.json").read_text(encoding="utf-8"))["he"]

HE_008 = [
    UTF8["purim_meshulash"],
    UTF8["purim_14"],
    UTF8["chofetz_chaim"],
    RAV,
    f"\u05E8\u05D1 \u2014 {RAV}",
    UTF8["friday_chag"],
    RECHILUT,
    ROSH_HASHANA,
    CUSTOM["bedtime_shema"],
    SCHACH,
    CUSTOM["second_seder"],
    CUSTOM["seder"],
    CUSTOM["sefer_torah"],
    SEFIRAT_OMER,
    SELICHOT,
]

HE_009 = [
    f"\u05E1\u05DC\u05D9\u05D7\u05D5\u05EA \u2014 {SELICHOT}",
    CUSTOM["sephardi"],
    CUSTOM["sephardi_omer"],
    f"\u05E1\u05E4\u05E8\u05D3\u05D9 \u2014 {CUSTOM['sephardi']}",
    CUSTOM["seudah_shlishit"],
    CUSTOM["shavuot"],
    CUSTOM["shechita"],
    CUSTOM["shekhinah"],
    CUSTOM["shema_hamitah"],
    f"\u05E9\u05DE\u05E2 \u05E2\u05DC \u05D4\u05DE\u05D9\u05D8\u05D4 \u2014 {CUSTOM['shema_hamitah']}",
    CUSTOM["shemini_diaspora"],
    CUSTOM["shemini_israel"],
    SHEMINI_BEGINS,
    CUSTOM["shemini_overview"],
    CUSTOM["shloshim"],
]

if __name__ == "__main__":
    write_batch("he_fix_007", HE_007)
    write_batch("he_fix_008", HE_008)
    write_batch("he_fix_009", HE_009)
    print("Done.")
