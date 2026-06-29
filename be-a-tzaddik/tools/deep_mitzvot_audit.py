#!/usr/bin/env python3
"""Deep audit: runtime mitzvot vs bundle, truncated keys, quality red flags."""
from __future__ import annotations

import json
import re
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8")

ROOT = Path(__file__).resolve().parents[1]
REPO = ROOT.parent
LANGS = ("he", "es", "fr", "ru")

GARBAGE_PATTERNS = [
    (re.compile(r"⟦\d"), "placeholder token"),
    (re.compile(r"\u27e6|\u27e7"), "broken placeholder"),
    (re.compile(r"Argos|argos"), "argos mention"),
    (re.compile(r"Kabb[aа]l"), "kabbaly corruption"),
    (re.compile(r"hu psak|me'ayen", re.I), "latin translit garbage"),
    (re.compile(r"[A-Za-z]{8,}"), "long latin run"),  # applied only to he prose
]


def load_runtime_texts() -> list[str]:
    texts: list[str] = []
    for rel in (
        "be-a-tzaddik/data/mitzvotcloud.json",
        "app/src/main/assets/mitzvotlistfull.json",
    ):
        p = REPO / rel
        if not p.is_file():
            continue
        data = json.loads(p.read_text(encoding="utf-8"))
        for m in data.get("mitzvot", []):
            if isinstance(m, dict) and m.get("text"):
                texts.append(m["text"].strip())
    return list(dict.fromkeys(texts))


def strip_placeholders(t: str) -> str:
    return re.sub(r"\$\{[^}]*\}|\$[a-zA-Z_][a-zA-Z0-9_]*", "", t)


def latin_ratio_he(t: str) -> float:
    stripped = strip_placeholders(t)
    letters = [c for c in stripped if c.isalpha()]
    if not letters:
        return 0.0
    return sum(1 for c in letters if c.isascii()) / len(letters)


def main() -> None:
    runtime = load_runtime_texts()
    catalog = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))[
        "strings"
    ]
    cat_set = set(catalog)
    bundles = {
        lang: json.loads(
            (ROOT / f"shared/src/commonMain/composeResources/files/translations/{lang}.json").read_text(
                encoding="utf-8"
            )
        )["entries"]
        for lang in LANGS
    }

    missing_runtime = [t for t in runtime if t not in cat_set]
    fb = {lang: [t for t in runtime if bundles[lang].get(t, t) == t] for lang in LANGS}

    # truncated catalog keys that are prefix of runtime but not exact
    trunc = []
    for t in runtime:
        if t in cat_set:
            continue
        hits = [s for s in catalog if t.startswith(s) and len(s) < len(t)]
        if hits:
            trunc.append({"runtime": t, "catalog_prefix": max(hits, key=len)})

    # runtime text close but not exact (whitespace/punct)
    near = []
    for t in runtime:
        if t in cat_set:
            continue
        norm = re.sub(r"\s+", " ", t)
        for s in catalog:
            if re.sub(r"\s+", " ", s) == norm:
                near.append({"runtime": t, "catalog": s})
                break

    he_bad = []
    for s in catalog:
        tr = bundles["he"].get(s, s)
        if tr == s or len(tr) < 20:
            continue
        r = latin_ratio_he(tr)
        if r > 0.22:
            he_bad.append({"ratio": round(r, 3), "key": s[:100], "he": tr[:120]})

    garbage = []
    for lang in LANGS:
        for s, tr in bundles[lang].items():
            if tr == s:
                continue
            for pat, label in GARBAGE_PATTERNS[:5]:
                if pat.search(tr):
                    garbage.append({"lang": lang, "issue": label, "key": s[:80], "tr": tr[:100]})
                    break

    # mitzvot with suspiciously short translation vs long English
    short_tr = []
    for t in runtime:
        if t not in bundles["he"]:
            continue
        tr = bundles["he"][t]
        if tr == t:
            continue
        if len(tr) < len(t) * 0.35 and len(t) > 200:
            short_tr.append({"en_len": len(t), "he_len": len(tr), "key": t[:90]})

    report = {
        "runtime_count": len(runtime),
        "catalog_count": len(catalog),
        "missing_from_catalog": missing_runtime,
        "fallback_by_lang": {k: len(v) for k, v in fb.items()},
        "fallback_samples": {k: v[:3] for k, v in fb.items() if v},
        "truncated_prefix_hits": trunc,
        "whitespace_near_misses": near,
        "he_latin_heavy": sorted(he_bad, key=lambda x: -x["ratio"])[:40],
        "garbage_hits": garbage[:60],
        "suspiciously_short_he": short_tr[:30],
    }
    out = ROOT / "data/translation-catalog/deep-mitzvot-audit.json"
    out.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")

    print(f"runtime mitzvot: {len(runtime)}")
    print(f"missing from catalog: {len(missing_runtime)}")
    for lang in LANGS:
        print(f"{lang} fallback at runtime: {len(fb[lang])}")
    print(f"truncated prefix hits: {len(trunc)}")
    print(f"he latin-heavy: {len(he_bad)}")
    print(f"garbage pattern hits: {len(garbage)}")
    print(f"short he translations: {len(short_tr)}")
    print(f"-> {out}")


if __name__ == "__main__":
    main()
