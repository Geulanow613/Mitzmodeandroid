# -*- coding: utf-8 -*-
"""Merge all rest_data modules into overlay_parts/rest.py DATA."""

import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))

from rest_data import ENTRIES as E1  # noqa: E402
from rest_data_b import ENTRIES as E2  # noqa: E402
from rest_data_c import ENTRIES as E3  # noqa: E402
from rest_data_d import ENTRIES as E4  # noqa: E402
from rest_e_translations import ENTRIES as E5  # noqa: E402

ALL = {**E1, **E2, **E3, **E4, **E5}
DATA: dict[str, dict[str, str]] = {
    k: {"he": v[0], "es": v[1], "fr": v[2], "ru": v[3]} for k, v in ALL.items()
}
