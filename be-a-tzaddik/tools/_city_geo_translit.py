#!/usr/bin/env python3
"""Latin placename transliteration helpers for city geography bundling."""

from __future__ import annotations

import re

# Digraph-aware Latin → Hebrew (approximate; for unknown city names).
_HE_DIGRAPHS = [
    ("tsch", "טש"),
    ("sch", "ש"),
    ("sh", "ש"),
    ("ch", "צ"),
    ("kh", "ח"),
    ("th", "ת"),
    ("ph", "פ"),
    ("gh", "ג"),
    ("wh", "וו"),
    ("qu", "קו"),
    ("ts", "צ"),
    ("tz", "צ"),
    ("zh", "ז"),
    ("ou", "או"),
    ("ee", "י"),
    ("oo", "ו"),
    ("ea", "י"),
    ("ai", "אי"),
    ("ay", "יי"),
    ("ey", "י"),
    ("ie", "י"),
]
_HE_CHARS = {
    "a": "א",
    "b": "ב",
    "c": "ק",
    "d": "ד",
    "e": "א",
    "f": "פ",
    "g": "ג",
    "h": "ה",
    "i": "י",
    "j": "ג",
    "k": "ק",
    "l": "ל",
    "m": "מ",
    "n": "נ",
    "o": "ו",
    "p": "פ",
    "q": "ק",
    "r": "ר",
    "s": "ס",
    "t": "ת",
    "u": "ו",
    "v": "ב",
    "w": "ו",
    "x": "קס",
    "y": "י",
    "z": "ז",
    "'": "'",
    "-": "-",
    " ": " ",
}


def latin_to_hebrew(name: str) -> str:
    s = name.strip()
    if not s:
        return s
    out: list[str] = []
    i = 0
    lower = s.lower()
    while i < len(lower):
        matched = False
        for src, dst in _HE_DIGRAPHS:
            if lower.startswith(src, i):
                out.append(dst)
                i += len(src)
                matched = True
                break
        if matched:
            continue
        ch = lower[i]
        if ch in _HE_CHARS:
            out.append(_HE_CHARS[ch])
        else:
            out.append(s[i])
        i += 1
    return "".join(out)


# Latin → Cyrillic (BGN/PCGN-ish, simplified).
_RU_DIGRAPHS = [
    ("sch", "щ"),
    ("sh", "ш"),
    ("ch", "ч"),
    ("kh", "х"),
    ("zh", "ж"),
    ("ts", "ц"),
    ("tz", "ц"),
    ("ya", "я"),
    ("yu", "ю"),
    ("yo", "ё"),
    ("ye", "е"),
    ("ay", "ай"),
    ("ey", "ей"),
    ("oy", "ой"),
    ("ou", "у"),
    ("oo", "у"),
    ("ee", "и"),
    ("th", "т"),
    ("ph", "ф"),
    ("wh", "у"),
    ("qu", "кв"),
]
_RU_CHARS = {
    "a": "а",
    "b": "б",
    "c": "к",
    "d": "д",
    "e": "е",
    "f": "ф",
    "g": "г",
    "h": "х",
    "i": "и",
    "j": "дж",
    "k": "к",
    "l": "л",
    "m": "м",
    "n": "н",
    "o": "о",
    "p": "п",
    "q": "к",
    "r": "р",
    "s": "с",
    "t": "т",
    "u": "у",
    "v": "в",
    "w": "в",
    "x": "кс",
    "y": "й",
    "z": "з",
    "'": "'",
    "-": "-",
    " ": " ",
}


def latin_to_cyrillic(name: str) -> str:
    s = name.strip()
    if not s:
        return s
    out: list[str] = []
    i = 0
    lower = s.lower()
    while i < len(lower):
        matched = False
        for src, dst in _RU_DIGRAPHS:
            if lower.startswith(src, i):
                out.append(dst)
                i += len(src)
                matched = True
                break
        if matched:
            continue
        ch = lower[i]
        if ch in _RU_CHARS:
            out.append(_RU_CHARS[ch])
        else:
            out.append(s[i])
        i += 1
    # Capitalize first letter if source was capitalized
    if s and s[0].isupper() and out:
        out[0] = out[0][:1].upper() + out[0][1:]
    return "".join(out)


def normalize_key(text: str) -> str:
    return re.sub(r"\s+", " ", text.strip())
