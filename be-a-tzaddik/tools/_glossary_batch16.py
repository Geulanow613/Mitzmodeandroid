"""Human-quality fixes batch 16 — full FR rewrites for 23 Shabbat Melacha essays."""

from __future__ import annotations

from _melacha_fr_batch16_content import MELACHA_FR


def resolve_batch16_fr(catalog: list[str]) -> dict[str, str]:
    out: dict[str, str] = {}
    for prefix, fr in MELACHA_FR.items():
        matches = [k for k in catalog if k.startswith(f"Learn about the Melacha of {prefix}")]
        if not matches:
            raise KeyError(f"catalog key not found for Melacha prefix {prefix!r}")
        out[max(matches, key=len)] = fr
    return out
