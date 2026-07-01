#!/usr/bin/env python3
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
catalog = json.loads((ROOT / "strings.json").read_text(encoding="utf-8"))
hints = json.loads((ROOT / "human" / "checklist_zman_hints.json").read_text(encoding="utf-8"))
keys = list(hints["he"].keys())
existing = set(catalog["strings"])
added = [k for k in keys if k not in existing]
catalog["strings"].extend(added)
catalog["strings"].sort()
catalog["count"] = len(catalog["strings"])
(ROOT / "strings.json").write_text(
    json.dumps(catalog, ensure_ascii=False, indent=2) + "\n",
    encoding="utf-8",
)
print(f"Added {len(added)} keys; total {catalog['count']}")
