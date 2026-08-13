"""Human-quality fixes batch 17 — full ES rewrites for 23 Shabbat Melacha essays."""

from __future__ import annotations

from _melacha_es_batch17_content import MELACHA_ES


def resolve_batch17_es(catalog: list[str]) -> dict[str, str]:
    out: dict[str, str] = {}
    for prefix, es in MELACHA_ES.items():
        matches = [k for k in catalog if k.startswith(f"Learn about the Melacha of {prefix}")]
        if not matches:
            raise KeyError(f"catalog key not found for Melacha prefix {prefix!r}")
        out[max(matches, key=len)] = es
    return out
