#!/usr/bin/env python3
"""Merge human translation shards into shipped he.json — no broken quality-batch import."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
LEGACY = ROOT / "data" / "bundled-translations" / "he.json"
HUMAN = ROOT / "data" / "translation-catalog" / "human"
OUT = (
    ROOT.parent
    / "sharedmodule"
    / "shared"
    / "src"
    / "commonMain"
    / "composeResources"
    / "files"
    / "translations"
    / "he.json"
)

SKIP_NAMES = {
    "quality_overrides.json",
    "shabbat_guide.json",
}


def should_skip_human_file(name: str) -> bool:
    if name in SKIP_NAMES:
        return True
    if name.endswith("_only.json") or name.endswith("_src.json"):
        return True
    if name.startswith("_"):
        return True
    if name.startswith("es_fr_") or name.startswith("ru_"):
        return True
    return False

PRIORITY_LAST = [
    "shabbat_guide_he_polish.json",
    "shabbat_guide_polish.json",
    "ui_gap_and_guide.json",
    "zman_and_upcoming_strings.json",
    "checklist_daily_explainers.json",
    "checklist_explainers.json",
    "seasonal_explainer_fragments.json",
    "purim_meshulash_explainers.json",
    "he_quality_hotfixes.json",
    "he_zman_megillah_fixes.json",
    "he_melachot_ui_fixes.json",
    "he_seasonal_purim_batch.json",
    "he_checklist_explainers_batch.json",
    "he_ui_gaps_batch.json",
    "he_kashrut_mitzvot_batch.json",
    "he_checklist_comprehensive.json",
    "he_seasonal_edge_comprehensive.json",
    "he_zman_ui_comprehensive.json",
    "he_quality_native_fixes.json",
    "he_checklist_remaining_fixes.json",
    "he_grok_ui_leftovers.json",
    "he_grok_quality_polish.json",
    "he_grok_stub_fixes.json",
    "he_grok_checklist_long.json",
    "he_grok_pass2.json",
    "he_grok_siddur_cues.json",
    "he_grok_app_tour.json",
    "he_grok_ui_sweep.json",
    "he_grok_learn_upcoming.json",
    "he_grok_shabbat_guide.json",
    "he_native_ui_polish.json",
    "he_native_holidays_explainers.json",
    "he_native_halacha_glossary.json",
    "he_native_learn_and_mixed.json",
    "he_native_short_ui_and_strings.json",
    "he_native_checklist_pass.json",
    "he_native_master_polish.json",
    "he_native_guide_batch_a.json",
    "he_native_guide_batch_b.json",
    "he_native_guide_batch_c.json",
    "he_native_guide_fluency.json",
]

LATIN_IN_HE = re.compile(r"(?<![\u0590-\u05FF\{])([A-Za-z]{4,})(?![\u0590-\u05FF\}])")
ALLOW_LATIN = {
    "iOS", "Maps",
    "http", "https", "www", "beardy", "top", "html", "json", "PurimBrachotText",
    "Metsudah", "Avrohom", "Davis", "Miqra", "Masorah", "Shmuel", "Gonzales",
    "Sefaria", "malkeinu",
    "MEGILLAH", "BLESSINGS", "COMMON", "label", "time", "count", "nusach", "day",
}


def load_json(path: Path) -> dict:
    raw = path.read_text(encoding="utf-8")
    try:
        return json.loads(raw)
    except json.JSONDecodeError:
        return json.loads(raw, strict=False)


def he_entries_from_file(path: Path) -> dict[str, str]:
    data = load_json(path)
    if isinstance(data.get("he"), dict):
        return {k: v for k, v in data["he"].items() if isinstance(k, str) and isinstance(v, str)}
    if all(isinstance(k, str) and isinstance(v, str) for k, v in data.items()):
        return dict(data)
    return {}


def is_suspicious_hebrew(value: str) -> bool:
    for m in LATIN_IN_HE.finditer(value):
        word = m.group(1)
        if word not in ALLOW_LATIN and not word.startswith("$"):
            return True
    return False


def bundle_stats(required: list[str], entries: dict[str, str]) -> dict[str, int]:
    translated = sum(1 for key in required if entries.get(key, key) != key)
    fallback = len(required) - translated
    suspicious = sum(
        1 for key in required
        if entries.get(key, key) != key and is_suspicious_hebrew(entries[key])
    )
    return {
        "total": len(required),
        "translated": translated,
        "fallback": fallback,
        "suspicious": suspicious,
    }


def print_stats(label: str, stats: dict[str, int]) -> None:
    print(
        f"{label}: total={stats['total']} translated={stats['translated']} "
        f"fallback={stats['fallback']} suspicious={stats['suspicious']}"
    )


def top_missing_short_ui(required: list[str], entries: dict[str, str], limit: int = 10) -> list[str]:
    missing = [key for key in required if entries.get(key, key) == key and len(key) > 2]
    return sorted(missing, key=len)[:limit]


def main() -> int:
    required = load_json(CATALOG)["strings"]
    before_entries: dict[str, str] = {}
    if OUT.exists():
        before_entries = load_json(OUT).get("entries", {})
        print_stats("Before", bundle_stats(required, before_entries))

    merged: dict[str, str] = {}

    if LEGACY.exists():
        merged.update(load_json(LEGACY).get("entries", {}))

    paths = sorted(
        p
        for p in HUMAN.glob("*.json")
        if not should_skip_human_file(p.name)
        and p.name not in PRIORITY_LAST
        and not p.name.startswith("he_fluency_")
    )
    paths += [HUMAN / n for n in PRIORITY_LAST if (HUMAN / n).is_file()]
    paths += sorted(HUMAN.glob("he_fluency_*.json"))

    loaded = 0
    for path in paths:
        try:
            merged.update(he_entries_from_file(path))
            loaded += 1
        except Exception as exc:
            print(f"WARN skip {path.name}: {exc}", file=sys.stderr)

    entries: dict[str, str] = {key: merged.get(key, key) for key in required}
    # Keep shipped keys that are not in the extractor catalog (siddur labels, etc.).
    for key, value in before_entries.items():
        if key not in entries:
            entries[key] = merged.get(key, value)
    stats = bundle_stats(required, entries)
    suspicious = [k for k, v in entries.items() if v != k and is_suspicious_hebrew(v)]
    missing_short = top_missing_short_ui(required, entries)

    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(
        json.dumps({"version": 2, "language": "he", "entries": entries}, ensure_ascii=False, indent=2),
        encoding="utf-8",
    )

    print(f"Output: {OUT}")
    print(f"Human files merged: {loaded}")
    print_stats("After", stats)
    if suspicious[:10]:
        print("Sample suspicious:")
        for k in suspicious[:10]:
            safe = k[:70].encode("ascii", errors="replace").decode("ascii")
            print(f"  - {safe}...")
    if missing_short:
        print("Top missing short UI strings:")
        for key in missing_short:
            safe = key.encode("ascii", errors="replace").decode("ascii")
            print(f"  - {safe}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
