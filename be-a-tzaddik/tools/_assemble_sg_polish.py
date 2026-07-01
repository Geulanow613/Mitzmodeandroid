# -*- coding: utf-8 -*-
"""Assemble shabbat_guide_es_fr_ru_polish_data.py from part files."""
from __future__ import annotations

from pathlib import Path

from _sg_es_part1 import ES_PART1
from _sg_es_part2 import ES_PART2
from _sg_fr_part1 import FR_PART1
from _sg_fr_part2 import FR_PART2
from _sg_ru_part1 import RU_PART1
from _sg_ru_part2 import RU_PART2
from shabbat_guide_he_polish_data import _load_keys

OUT = Path(__file__).parent / "shabbat_guide_es_fr_ru_polish_data.py"

ES_VALUES = ES_PART1 + ES_PART2
FR_VALUES = FR_PART1 + FR_PART2
RU_VALUES = RU_PART1 + RU_PART2

keys = _load_keys()
for name, vals in [("ES", ES_VALUES), ("FR", FR_VALUES), ("RU", RU_VALUES)]:
    if len(vals) != 100:
        raise RuntimeError(f"{name}: expected 100 entries, got {len(vals)}")
    if len(vals) != len(keys):
        raise RuntimeError(f"{name}: key count mismatch {len(vals)} vs {len(keys)}")

header = '''# -*- coding: utf-8 -*-
"""Human-quality es/fr/ru polish for shabbat_guide.json entries."""
from __future__ import annotations

from shabbat_guide_he_polish_data import _load_keys

'''

# Write by importing the lists directly in the output file
body = header
body += "from _sg_es_part1 import ES_PART1\n"
body += "from _sg_es_part2 import ES_PART2\n"
body += "from _sg_fr_part1 import FR_PART1\n"
body += "from _sg_fr_part2 import FR_PART2\n"
body += "from _sg_ru_part1 import RU_PART1\n"
body += "from _sg_ru_part2 import RU_PART2\n\n"
body += "_ES_VALUES: list[str] = ES_PART1 + ES_PART2\n"
body += "_FR_VALUES: list[str] = FR_PART1 + FR_PART2\n"
body += "_RU_VALUES: list[str] = RU_PART1 + RU_PART2\n\n"
body += "ES_POLISH: dict[str, str] = dict(zip(_load_keys(), _ES_VALUES))\n"
body += "FR_POLISH: dict[str, str] = dict(zip(_load_keys(), _FR_VALUES))\n"
body += "RU_POLISH: dict[str, str] = dict(zip(_load_keys(), _RU_VALUES))\n\n"
body += "if len(ES_POLISH) != 100:\n"
body += '    raise RuntimeError(f"expected 100 ES entries, got {len(ES_POLISH)}")\n'
body += "if len(FR_POLISH) != 100:\n"
body += '    raise RuntimeError(f"expected 100 FR entries, got {len(FR_POLISH)}")\n'
body += "if len(RU_POLISH) != 100:\n"
body += '    raise RuntimeError(f"expected 100 RU entries, got {len(RU_POLISH)}")\n'

OUT.write_text(body, encoding="utf-8")
print(f"Wrote {OUT}")
