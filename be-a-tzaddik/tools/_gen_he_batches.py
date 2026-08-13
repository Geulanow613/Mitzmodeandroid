#!/usr/bin/env python3
"""Generate he_fix_009/011/012_he_only.json with validated pure Hebrew."""
import json
import re
from pathlib import Path

OUT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog" / "human"

LATIN_RE = re.compile(r"[A-Za-z]{2,}")


def validate_hebrew(strings: list[str], batch: str) -> None:
    for i, s in enumerate(strings, 1):
        clean = re.sub(r"\$\{[^}]+\}", "", s)
        clean = re.sub(r"\$[a-zA-Z_][a-zA-Z0-9_.?]*", "", clean)
        hits = LATIN_RE.findall(clean)
        if hits:
            raise ValueError(f"{batch} #{i}: Latin fragments {hits[:8]} in: {s[:120]}...")


def write_batch(name: str, strings: list[str]) -> None:
    assert len(strings) == 15, f"{name}: expected 15, got {len(strings)}"
    validate_hebrew(strings, name)
    path = OUT / f"{name}_he_only.json"
    path.write_text(
        json.dumps({"he": strings}, ensure_ascii=False, indent=2) + "\n",
        encoding="utf-8",
    )
    print(f"OK {path.name}")


he_fix_009 = [
    "סליחות — סליחות הן תפילות כפרה הנאמרות לפני ראש השנה (אשכנזים לעיתים קרובות ממוצאי שבת שלפני, ספרדים מאלול). הן כוללות פיוטים של תחינות ושלוש עשרה מידות הרחמים. קימה מוקדמת או השהייה מאוחרת לסליחות מגדירה רצינות לפני ימים נוראים. הנוסח והלוח משתנים — בדקו בבית הכנסת שלכם.",
    "יהודים ספרדים שורשיהם בגלות ספרד והקהילות הים-תיכוניות הקשורות. התפילה בדרך כלל נוסח ספרד (בית יוסף). ההלכה עוקבת בדרך כלל אחר השולחן ערוך ופוסקים כמו הרב עובדיה יוסף זצ\"ל. אורז וקטניות מותרים בהלכה בפסח, בעוד אשכנזים נוהגים איסור קטניות. אל תבלבלו עם «נוסח ספרד» בסידור חסידי (נוסח אשכנזי) או עם עדות המזרח.",
    "מנהג ספרדי (שו\"ע או\"ח תצ\"ג:א–ב; פניני הלכה 05-03-03): אבלות מפסח עד בוקר יום ל\"ד בעומer (לamed-dalet). מוזיקה בל\"ג baomer לכבוד רabbi שimon bar yochai muteret, אך חתunot ותסpurot asurim ad boker lad lefi psak ha-sefardi (ha-rav ovadia yosef, yechaveh daat 3:31). kehillot mesuyamot mesayemot ba-lag baomer — hhalikhu akhar kehilla.\n\nשאelu et ha-rav eizo masoret atem okfim u-matai ha-hagbalot.",
    "ספרדי — יהודים ספרדים שורשיהם בגלות ספרד והקהילות הים-תיכוניות הקשורות. התפילה בדרך כלל נוסח ספרד (בית יוסף). ההלכה עוקבת בדרך כלל אחר השולחן ערוך ופוסקים כמו הרב עובדיה יוסף זצ\"ל. אורז וקטניות מותרים בהלכה בפסח, בעוד אשכנזים נוהגים איסור קטניות. אל תבלבלו עם «נוסח ספרד» בסידור חסידי (נוסח אשכנזי) או עם עדות המזרח.",
]

if __name__ == "__main__":
    write_batch("he_fix_009", he_fix_009)
