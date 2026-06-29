"""Assemble hebrew_paragraph_batch4.py from translations JSON."""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parent
TRANS = ROOT / "_batch4_translations.json"
OUT = ROOT / "hebrew_paragraph_batch4.py"


def main() -> None:
    fixes: dict[str, str] = json.loads(TRANS.read_text(encoding="utf-8"))
    catalog = json.loads(
        (ROOT.parent / "data/translation-catalog/he-mixed-need-fix.json").read_text(
            encoding="utf-8"
        )
    )
    required = [item["key"] for item in catalog]
    missing = [k for k in required if k not in fixes]
    extra = [k for k in fixes if k not in required]
    if missing:
        raise SystemExit(f"Missing {len(missing)} keys: {missing[:3]}...")
    if extra:
        raise SystemExit(f"Extra {len(extra)} keys not in catalog")

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

    OUT.write_text("\n".join(lines), encoding="utf-8")
    print(f"Wrote {len(fixes)} keys to {OUT.name}")


if __name__ == "__main__":
    main()
