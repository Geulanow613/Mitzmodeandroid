# -*- coding: utf-8 -*-
"""Inline part files into shabbat_guide_es_fr_ru_polish_data.py."""
from __future__ import annotations

from pathlib import Path

from _sg_es_part1 import ES_PART1
from _sg_es_part2 import ES_PART2
from _sg_fr_part1 import FR_PART1
from _sg_fr_part2 import FR_PART2
from _sg_ru_part1 import RU_PART1
from _sg_ru_part2 import RU_PART2


def fmt_list(name: str, values: list[str]) -> str:
    lines = [f"{name}: list[str] = ["]
    for i, v in enumerate(values, 1):
        lines.append(f"    # {i}")
        lines.append(f"    {v!r},")
    lines.append("]")
    return "\n".join(lines)


def main() -> None:
    es = ES_PART1 + ES_PART2
    fr = FR_PART1 + FR_PART2
    ru = RU_PART1 + RU_PART2
    for name, vals in [("ES", es), ("FR", fr), ("RU", ru)]:
        if len(vals) != 100:
            raise RuntimeError(f"{name}: expected 100, got {len(vals)}")

    header = '''# -*- coding: utf-8 -*-
"""Human-quality es/fr/ru polish for shabbat_guide.json entries."""
from __future__ import annotations

from shabbat_guide_he_polish_data import _load_keys

'''
    body = "\n\n".join([
        fmt_list("_ES_VALUES", es),
        fmt_list("_FR_VALUES", fr),
        fmt_list("_RU_VALUES", ru),
    ])
    footer = """
ES_POLISH: dict[str, str] = dict(zip(_load_keys(), _ES_VALUES))
FR_POLISH: dict[str, str] = dict(zip(_load_keys(), _FR_VALUES))
RU_POLISH: dict[str, str] = dict(zip(_load_keys(), _RU_VALUES))

if len(ES_POLISH) != 100:
    raise RuntimeError(f"expected 100 ES entries, got {len(ES_POLISH)}")
if len(FR_POLISH) != 100:
    raise RuntimeError(f"expected 100 FR entries, got {len(FR_POLISH)}")
if len(RU_POLISH) != 100:
    raise RuntimeError(f"expected 100 RU entries, got {len(RU_POLISH)}")
"""
    out = Path(__file__).parent / "shabbat_guide_es_fr_ru_polish_data.py"
    out.write_text(header + body + footer, encoding="utf-8")
    print(f"wrote {out} ({out.stat().st_size} bytes)")


if __name__ == "__main__":
    main()
