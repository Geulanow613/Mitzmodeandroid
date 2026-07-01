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
from _explainer_keys import resolve_keys  # noqa: E402
from _three_weeks_ty_data import THREE_WEEKS_INTRO_RU, TW_ASH_RU, TW_CH_RU, TW_SEP_RU  # noqa: E402
from _taanit_bechor_ty_data import TAANIT_BECHOR_RU  # noqa: E402
from _ru_deep_fixes_data import MANUAL_RU_DEEP  # noqa: E402
from _ru_modeh_data import modeh_ru_for_key  # noqa: E402
from _ru_nine_days_data import nine_days_ru_for_key  # noqa: E402
from _ru_rosh_chodesh_data import rosh_chodesh_ru_for_key  # noqa: E402
from _ru_melacha_prose_data import melacha_ru_for_key  # noqa: E402
from _glossary_batch21 import BATCH21_RU  # noqa: E402
from _glossary_batch22 import BATCH22_RU  # noqa: E402
from _glossary_batch23 import BATCH23_RU  # noqa: E402
from _glossary_batch24 import BATCH24_RU  # noqa: E402
from _glossary_batch25 import BATCH25_RU  # noqa: E402
from _glossary_batch26 import BATCH26_RU  # noqa: E402
from _glossary_batch27 import BATCH27_RU  # noqa: E402
from _glossary_batch28 import BATCH28_RU  # noqa: E402
from _glossary_batch29 import BATCH29_RU  # noqa: E402
from _glossary_batch30 import BATCH30_RU  # noqa: E402
from _glossary_batch31 import BATCH31_RU  # noqa: E402
from _glossary_batch32 import BATCH32_RU  # noqa: E402
from _ru_glossary_ty_data import glossary_ru_for_key  # noqa: E402
from _yaaleh_ru_data import yaaleh_overview_ru_for_key, yaaleh_ru_for_key  # noqa: E402
from _ru_netilat_data import netilat_ru_for_key  # noqa: E402
from _ru_short_expansions_data import PREFIX_RU_SHORT_EXPAND  # noqa: E402

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
    base = TAANIT_BECHOR_RU
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


def apply_three_weeks_ru_fixes(fixes: dict[str, str]) -> int:
    """Authoritative ty RU for Three Weeks intro + nusach fragment and composite keys."""
    keys = resolve_keys()
    intro = THREE_WEEKS_INTRO_RU
    added = 0
    for frag in ("tw_ash", "tw_sep", "tw_ch"):
        body = {"tw_ash": TW_ASH_RU, "tw_sep": TW_SEP_RU, "tw_ch": TW_CH_RU}[frag]
        fixes[keys[frag]] = body
        fixes[keys[f"{frag}_full"]] = intro + "\n\n" + body
        added += 2
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

    # Expanded PREFIX_RU_DEEP prose wins over shorter manual/batch stubs.
    expanded = 0
    for key in catalog:
        best_val: str | None = None
        best_len = len(fixes.get(key, ""))
        for prefix, val in PREFIX_RU_DEEP.items():
            if key.startswith(prefix) and len(val) > best_len:
                best_val = val
                best_len = len(val)
        if best_val is not None:
            fixes[key] = best_val
            expanded += 1

    # Final pass: Learn-about missions still under 400 chars after compile repairs.
    short_expanded = 0
    for key in catalog:
        best_val: str | None = None
        best_len = len(fixes.get(key, ""))
        for prefix, val in PREFIX_RU_SHORT_EXPAND.items():
            if key.startswith(prefix) and len(val) > best_len:
                best_val = val
                best_len = len(val)
        if best_val is not None:
            fixes[key] = best_val
            short_expanded += 1

    netilat_added = 0
    for key in catalog:
        body = netilat_ru_for_key(key)
        if body:
            fixes[key] = body
            netilat_added += 1

    modeh_added = 0
    for key in catalog:
        body = modeh_ru_for_key(key)
        if body:
            fixes[key] = body
            modeh_added += 1

    nine_days_added = 0
    for key in catalog:
        body = nine_days_ru_for_key(key)
        if body:
            fixes[key] = body
            nine_days_added += 1

    yaaleh_added = 0
    for key in catalog:
        body = yaaleh_ru_for_key(key)
        if body:
            fixes[key] = body
            yaaleh_added += 1

    yaaleh_overview_added = 0
    for key in catalog:
        body = yaaleh_overview_ru_for_key(key)
        if body:
            fixes[key] = body
            yaaleh_overview_added += 1

    rosh_chodesh_added = 0
    for key in catalog:
        body = rosh_chodesh_ru_for_key(key)
        if body:
            fixes[key] = body
            rosh_chodesh_added += 1

    glossary_added = 0
    for key in catalog:
        body = glossary_ru_for_key(key)
        if body:
            fixes[key] = body
            glossary_added += 1

    melacha_added = 0
    for key in catalog:
        body = melacha_ru_for_key(key)
        if body:
            fixes[key] = body
            melacha_added += 1

    batch21_ru_added = 0
    for key, body in BATCH21_RU.items():
        if key in catalog:
            fixes[key] = body
            batch21_ru_added += 1

    batch22_ru_added = 0
    for key, body in BATCH22_RU.items():
        if key in catalog:
            fixes[key] = body
            batch22_ru_added += 1

    batch23_ru_added = 0
    for key, body in BATCH23_RU.items():
        if key in catalog:
            fixes[key] = body
            batch23_ru_added += 1

    batch24_ru_added = 0
    for key, body in BATCH24_RU.items():
        if key in catalog:
            fixes[key] = body
            batch24_ru_added += 1

    batch25_ru_added = 0
    for key, body in BATCH25_RU.items():
        if key in catalog:
            fixes[key] = body
            batch25_ru_added += 1

    batch26_ru_added = 0
    for key, body in BATCH26_RU.items():
        if key in catalog:
            fixes[key] = body
            batch26_ru_added += 1

    batch27_ru_added = 0
    for key, body in BATCH27_RU.items():
        if key in catalog:
            fixes[key] = body
            batch27_ru_added += 1

    batch28_ru_added = 0
    for key, body in BATCH28_RU.items():
        if key in catalog:
            fixes[key] = body
            batch28_ru_added += 1

    batch29_ru_added = 0
    for key, body in BATCH29_RU.items():
        if key in catalog:
            fixes[key] = body
            batch29_ru_added += 1

    batch30_ru_added = 0
    for key, body in BATCH30_RU.items():
        if key in catalog:
            fixes[key] = body
            batch30_ru_added += 1

    batch31_ru_added = 0
    for key, body in BATCH31_RU.items():
        if key in catalog:
            fixes[key] = body
            batch31_ru_added += 1

    batch32_ru_added = 0
    for key, body in BATCH32_RU.items():
        if key in catalog:
            fixes[key] = body
            batch32_ru_added += 1

    three_weeks_added = apply_three_weeks_ru_fixes(fixes)

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
        f"batch15: {batch15_added}, batch16: {batch16_added}, batch17: {batch17_added}, batch18: {batch18_added}, batch19: {batch19_added}, batch20: {batch20_added}, batch21: {batch21_added}, batch22: {batch22_ru_added}, batch23: {batch23_ru_added}, batch24: {batch24_ru_added}, batch25: {batch25_ru_added}, batch26: {batch26_ru_added}, batch27: {batch27_ru_added}, batch28: {batch28_ru_added}, batch29: {batch29_ru_added}, batch30: {batch30_ru_added}, batch31: {batch31_ru_added}, batch32: {batch32_ru_added}, expanded: {expanded}, short-expanded: {short_expanded}, netilat: {netilat_added}, modeh: {modeh_added}, nine_days: {nine_days_added}, yaaleh: {yaaleh_added}, yaaleh_overview: {yaaleh_overview_added}, rosh_chodesh: {rosh_chodesh_added}, glossary: {glossary_added}, melacha: {melacha_added}, batch21_ru: {batch21_ru_added})"
    )
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
