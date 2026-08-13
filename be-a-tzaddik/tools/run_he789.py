#!/usr/bin/env python3
"""Generate and validate he_fix_007/008/009_he_only.json."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
HUMAN = ROOT / "human"
TOOLS = Path(__file__).resolve().parent

sys.path.insert(0, str(TOOLS))
import gen_he_fix_007_009 as gen  # noqa: E402

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
        raise SystemExit(f"{label} mixed Latin/Cyrillic in Hebrew: {bad}")


def check_placeholders(keys: list[str], he: list[str], label: str) -> None:
    issues = []
    for i, (key, tr) in enumerate(zip(keys, he)):
        kp = extract_placeholders(key)
        tp = extract_placeholders(tr)
        if kp != tp:
            issues.append(f"  [{i + 1}] key={kp!r} he={tp!r}")
    if issues:
        raise SystemExit(f"PLACEHOLDER MISMATCH {label}:\n" + "\n".join(issues))
    n = sum(len(extract_placeholders(k)) for k in keys)
    print(f"PLACEHOLDERS OK {label}: {n} tokens")


def write_batch(batch: str) -> None:
    name = f"he_fix_{batch}"
    strings = gen.TRANSLATIONS[batch]
    keys = json.loads((ROOT / f"_keys_{name}.json").read_text(encoding="utf-8"))
    if len(strings) != 15:
        raise SystemExit(f"{name}: expected 15 strings, got {len(strings)}")
    if len(strings) != len(keys):
        raise SystemExit(f"{name}: {len(strings)} strings != {len(keys)} keys")
    check_pure(strings, name)
    check_placeholders(keys, strings, name)
    path = HUMAN / f"{name}_he_only.json"
    path.write_text(json.dumps({"he": strings}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"OK {path.name}: {len(strings)} strings")


def main() -> None:
    for batch in ("007", "008", "009"):
        write_batch(batch)
    print("Done.")


if __name__ == "__main__":
    main()
