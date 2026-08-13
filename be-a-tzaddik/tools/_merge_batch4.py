"""Merge batch4 parts and assemble final hebrew_paragraph_batch4.py."""
from __future__ import annotations

import importlib.util
import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent


def main() -> None:
    fixes: dict[str, str] = {}
    for i in range(1, 5):
        p = ROOT / f"_batch4_part{i}.py"
        spec = importlib.util.spec_from_file_location(f"batch4_part{i}", p)
        assert spec and spec.loader
        mod = importlib.util.module_from_spec(spec)
        spec.loader.exec_module(mod)
        part_dict = getattr(mod, f"PART{i}")
        fixes.update(part_dict)

    trans_path = ROOT / "_batch4_translations.json"
    trans_path.write_text(json.dumps(fixes, ensure_ascii=False, indent=2), encoding="utf-8")

    catalog = json.loads(
        (ROOT.parent / "data/translation-catalog/he-mixed-need-fix.json").read_text(
            encoding="utf-8"
        )
    )
    required = [item["key"] for item in catalog]
    missing = [k for k in required if k not in fixes]
    extra = [k for k in fixes if k not in required]
    if missing:
        raise SystemExit(f"Missing {len(missing)}: {missing[0][:80]!r}...")
    if extra:
        raise SystemExit(f"Extra {len(extra)} keys")

    lines = [
        '"""Fourth batch — fix all remaining Hebrew mixed-Latin entries."""',
        "from __future__ import annotations",
        "",
        "FIXES: dict[str, str] = {",
    ]
    for key in required:
        val = fixes[key]
        lines.append(f"    {key!r}: (")
        lines.append(f"        {val!r}")
        lines.append("    ),")
    lines.append("}")
    lines.append("")

    out = ROOT / "hebrew_paragraph_batch4.py"
    out.write_text("\n".join(lines), encoding="utf-8")
    print(f"OK: {len(fixes)} keys -> {out.name}")


if __name__ == "__main__":
    main()
