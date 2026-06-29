#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Repair Latin/English corruption in mitzvot_003 Hebrew entries 6-25."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
STRINGS = Path(__file__).parent / "_he_003_strings.json"
OUT = Path(__file__).parent / "_clean_he_003.json"

REPLACEMENTS: list[tuple[str, str]] = [
    (r"\bמelech\b", "מלך"),
    (r"\bne'eman\b", "נאמן"),
    (r"\bנe'eman\b", "נאמן"),
    (r"פרashat ויקra", 'פרשת ויקרא'),
    (r"שmonעh esrei", 'שmonעh esrei'),
    (r"שmonעh esrei", 'שmonעh esrei'),
    (r"שmonעh esrei", 'שmonעh esrei'),
]
