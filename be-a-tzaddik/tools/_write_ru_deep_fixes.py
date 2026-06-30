#!/usr/bin/env python3
"""Emit human/ru_deep_fixes.json — authoritative Cyrillic rewrites for worst RU gaps."""

from __future__ import annotations

import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TOOLS = Path(__file__).resolve().parent
sys.path.insert(0, str(TOOLS))

from _ru_deep_fixes_batch2_data import PREFIX_RU_DEEP, TAANIT_BECHOROT_PREFIX  # noqa: E402
from _ru_deep_fixes_data import MANUAL_RU_DEEP  # noqa: E402

CATALOG_PATH = ROOT / "data" / "translation-catalog" / "strings.json"
OUT = ROOT / "data" / "translation-catalog" / "human" / "ru_deep_fixes.json"


def load_catalog() -> list[str]:
    return json.loads(CATALOG_PATH.read_text(encoding="utf-8"))["strings"]


def apply_prefix_fixes(catalog: list[str], fixes: dict[str, str]) -> int:
    added = 0
    for key in catalog:
        if key in fixes:
            continue
        for prefix, val in PREFIX_RU_DEEP.items():
            if key.startswith(prefix):
                fixes[key] = val
                added += 1
                break
    return added


def apply_taanit_fixes(catalog: list[str], fixes: dict[str, str]) -> int:
    base = PREFIX_RU_DEEP.get(
        TAANIT_BECHOROT_PREFIX,
        "",
    )
    if not base:
        # Fallback: find any taanit prefix value from PREFIX_RU_DEEP
        for prefix, val in PREFIX_RU_DEEP.items():
            if prefix.startswith("Taanit Bechorot"):
                base = val
                break
    if not base:
        return 0
    added = 0
    for key in catalog:
        if not key.startswith(TAANIT_BECHOROT_PREFIX):
            continue
        suffix = ""
        if "$scheduleLeadIn" in key:
            suffix = "$scheduleLeadIn$scheduleBody$scheduleYomTov"
        fixes[key] = base + suffix
        added += 1
    return added


def apply_polish_regression_fixes(fixes: dict[str, str]) -> int:
    """Re-apply top50 winners when ru_polish_all regressed them to guillemets."""
    human = ROOT / "data" / "translation-catalog" / "human"
    top50_path = human / "ru_cyrillic_top50.json"
    polish_path = human / "ru_polish_all.json"
    if not top50_path.exists() or not polish_path.exists():
        return 0
    top50 = json.loads(top50_path.read_text(encoding="utf-8")).get("ru", {})
    polish = json.loads(polish_path.read_text(encoding="utf-8")).get("ru", {})
    added = 0
    for key, good in top50.items():
        bad = polish.get(key, "")
        if not (bad.startswith("«") and bad.endswith("»")):
            continue
        if fixes.get(key) != good:
            fixes[key] = good
            added += 1
    return added


def apply_cyrillic_fixes_regression(fixes: dict[str, str]) -> int:
    """Re-apply ru_cyrillic_fixes winners when ru_polish_all overwrote them."""
    human = ROOT / "data/translation-catalog/human"
    cf_path = human / "ru_cyrillic_fixes.json"
    polish_path = human / "ru_polish_all.json"
    if not cf_path.exists():
        return 0
    cf = json.loads(cf_path.read_text(encoding="utf-8")).get("ru", {})
    polish = json.loads(polish_path.read_text(encoding="utf-8")).get("ru", {}) if polish_path.exists() else {}
    added = 0
    for key, good in cf.items():
        bad = polish.get(key, "")
        if bad and bad != good:
            if fixes.get(key) != good:
                fixes[key] = good
                added += 1
    return added


def main() -> int:
    catalog = load_catalog()
    fixes: dict[str, str] = {}
    fixes.update(MANUAL_RU_DEEP)
    prefix_added = apply_prefix_fixes(catalog, fixes)
    taanit_added = apply_taanit_fixes(catalog, fixes)
    polish_reg_added = apply_polish_regression_fixes(fixes)
    cyrillic_reg_added = apply_cyrillic_fixes_regression(fixes)

    batch11 = TOOLS / "_ru_batch11_manual.json"
    batch11_added = 0
    if batch11.exists():
        batch11_manual = json.loads(batch11.read_text(encoding="utf-8"))
        fixes.update(batch11_manual)
        batch11_added = len(batch11_manual)

    batch12 = TOOLS / "_ru_batch12_manual.json"
    batch12_added = 0
    if batch12.exists():
        batch12_manual = json.loads(batch12.read_text(encoding="utf-8"))
        fixes.update(batch12_manual)
        batch12_added = len(batch12_manual)

    batch13 = TOOLS / "_ru_batch13_manual.json"
    batch13_added = 0
    if batch13.exists():
        batch13_manual = json.loads(batch13.read_text(encoding="utf-8"))
        fixes.update(batch13_manual)
        batch13_added = len(batch13_manual)

    batch14 = TOOLS / "_ru_batch14_manual.json"
    batch14_added = 0
    if batch14.exists():
        batch14_manual = json.loads(batch14.read_text(encoding="utf-8"))
        fixes.update(batch14_manual)
        batch14_added = len(batch14_manual)

    batch15 = TOOLS / "_ru_batch15_manual.json"
    batch15_added = 0
    if batch15.exists():
        batch15_manual = json.loads(batch15.read_text(encoding="utf-8"))
        fixes.update(batch15_manual)
        batch15_added = len(batch15_manual)

    batch16 = TOOLS / "_ru_batch16_manual.json"
    batch16_added = 0
    if batch16.exists():
        batch16_manual = json.loads(batch16.read_text(encoding="utf-8"))
        fixes.update(batch16_manual)
        batch16_added = len(batch16_manual)

    batch17 = TOOLS / "_ru_batch17_manual.json"
    batch17_added = 0
    if batch17.exists():
        batch17_manual = json.loads(batch17.read_text(encoding="utf-8"))
        fixes.update(batch17_manual)
        batch17_added = len(batch17_manual)

    batch18 = TOOLS / "_ru_batch18_manual.json"
    batch18_added = 0
    if batch18.exists():
        batch18_manual = json.loads(batch18.read_text(encoding="utf-8"))
        fixes.update(batch18_manual)
        batch18_added = len(batch18_manual)

    batch19 = TOOLS / "_ru_batch19_manual.json"
    batch19_added = 0
    if batch19.exists():
        batch19_manual = json.loads(batch19.read_text(encoding="utf-8"))
        fixes.update(batch19_manual)
        batch19_added = len(batch19_manual)

    batch20 = TOOLS / "_ru_batch20_manual.json"
    batch20_added = 0
    if batch20.exists():
        batch20_manual = json.loads(batch20.read_text(encoding="utf-8"))
        fixes.update(batch20_manual)
        batch20_added = len(batch20_manual)

    batch21 = TOOLS / "_ru_batch21_manual.json"
    batch21_added = 0
    if batch21.exists():
        batch21_manual = json.loads(batch21.read_text(encoding="utf-8"))
        fixes.update(batch21_manual)
        batch21_added = len(batch21_manual)

    # Drop keys not in catalog (stale).
    fixes = {k: v for k, v in fixes.items() if k in catalog}

    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(
        json.dumps({"ru": fixes}, ensure_ascii=False, indent=2) + "\n",
        encoding="utf-8",
    )
    print(
        f"wrote {len(fixes)} keys to {OUT.name} "
        f"(prefix-added: {prefix_added}, taanit: {taanit_added}, polish-reg: {polish_reg_added}, cyrillic-reg: {cyrillic_reg_added}, "
        f"batch11: {batch11_added}, batch12: {batch12_added}, batch13: {batch13_added}, batch14: {batch14_added}, "
        f"batch15: {batch15_added}, batch16: {batch16_added}, batch17: {batch17_added}, batch18: {batch18_added}, batch19: {batch19_added}, batch20: {batch20_added}, batch21: {batch21_added})"
    )
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
