# -*- coding: utf-8 -*-
"""Helper: convert UTF-8 Hebrew lines to Python unicode-escape literals."""
import json
import sys
from pathlib import Path

text = Path(sys.argv[1]).read_text(encoding="utf-8").strip()
print(json.dumps(text, ensure_ascii=True))
