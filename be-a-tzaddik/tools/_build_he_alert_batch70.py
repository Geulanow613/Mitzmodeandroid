#!/usr/bin/env python3
"""Build _he_alert_tone_batch70.py — tone-polish coverage for OK-but-missing alerts."""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SRC = ROOT / "_ok70_keys.json"
OUT = Path(__file__).parent / "_he_alert_tone_batch70.py"

# Full rewrites for entries whose mitzvah_alert_tone Hebrew is stubby or corrupted.
OVERRIDES: dict[str, str] = {
    "Learn about the Melacha of Boneh—the deliberate act of building or assembling a structure—and discover how this manufacturing boundary directly shapes how we handle household items, furniture, and temporary shelters on Shabbat! This fundamental category of creative labor traces its origins straight to the construction of the Mishkan, where skilled artisans erected the massive wooden wall panels and fit the heavy foundation sockets together to create a unified, sturdy Tabernacle. The core definition of Boneh is the creation, improvement, or assembly of any structure, vessel, or functional space by combining parts together into a stable unit. On Shabbat, this means that heavy construction, installing permanent shelving, or repairing a loose door handle that fell out of its socket is strictly forbidden. While a person is unlikely to be pouring concrete on Saturday, Boneh introduces highly practical guidelines for routine household adjustments. A classic example is setting up a pop-up canopy or a large, multi-panel tent for shade; because you are assembling a covered shelter that shields a significant area from the sun or rain, it can constitute the creation of an Ohel (tent/canopy), which falls under the restrictions of building. Similarly, snapping together modular plastic furniture or attaching/screwing parts that form a rigid, tight unit are all problems. Learn more!":
        "למדו על מלאכת בונה — בנייה והרכבה! 🏗️ במשכן הקימו קירות עץ ויסודות. בונה = יצירה או הרכבה של מבנה, כלי או מרחב ליחידה יציבה. בשבת אסור בנייה כבדה, מדפים קבועים או תיקון ידית שנפלה. דוגמה: הקמת סוכה/אוהל גדול — עשיית אוהל (עול). רהיטים מודולריים הדוקים — בעייתי. למדו עוד!",
    "Learn about the Melacha of Melaben—the art of laundering and bleaching—and the fascinating reasons behind why we can't do laundry or scrub out stains on Shabbat! This category of creative labor traces directly back to the construction of the Mishkan, where workers had to wash and bleach the raw goat hair and sheep wool before spinning them into the beautiful tapestries that draped the sacred structure. On Shabbat, the prohibition of Melaben means we pause all forms of whitening, cleaning, and refreshing fabrics, and it goes way beyond just avoiding the washing machine. In fact, even spot-cleaning a fresh spill on a tablecloth or shirt can be a serious violation. For instance, if you drop a piece of food on your shirt and immediately dab it with water or try to rub the stain away with cleaning intent, you are actively performing the Melacha of Melaben by forcing the dirt out of the fabric fibers. The rules are so sensitive that even wetting a dirty garment is sometimes considered the beginning of the laundering process. Learn more!":
        "למדו על מלאכת מלבן — כביסה והלבנה! 👕 במשכן כבסו צמר לפני טוויה. מלבן = הלבנה, ניקוי ורענון בדים. בשבת אסורה כביסה — וגם ניגוב כתם עם מים בכוונת ניקוי. אפילו הרטבת בגד מלוכלך יכולה להתחיל תהליך כביסה. למדו עוד!",
    "Learn about the power of forgiveness! Take a moment to forgive someone who may have upset you - big or small. Remember: everything that happens is orchestrated by G-d for a reason. Here's the amazing part: when you show mercy to others, G-d shows extra mercy to you too! It's like a spiritual boomerang of kindness.":
        "למדו על כוח הסליחה! קחו רגע לסלוח למי שהרגיז אתכם — קטן או גדול. זכרו: הכל מנוהל מה'. כשמראים רחמים לאחרים — ה' מרחם גם עליכם! כמו בומרנג רוחני של חסד.",
    "Practice judging favorably! Spotted someone doing something that seems not-so-great? Here's your chance to be a spiritual superhero 🦸‍♂️! The Torah teaches us to give others the benefit of the doubt. Maybe they're having a rough day, dealing with something we can't see, or just made a mistake. When we judge others kindly, G-d judges us kindly too! ⚖️":
        "תרגלו שיפוט לכף זכות! מישהו עושה משהו שנראה לא טוב? הזדמנות להיות גיבור-על רוחני! 🦸‍♂️ תנו את היתרון של הספק — אולי יום קשה, משהו נסתר, או טעות. כששופטים בחום — גם ה' שופט אתכם בחום. ⚖️",
}

items = json.loads(SRC.read_text(encoding="utf-8"))
manual: dict[str, str] = {}
for item in items:
    k = item["key"]
    manual[k] = OVERRIDES.get(k, item["he"])

lines = [
    '"""Batch 70 — tone-polish coverage for mechanically OK alerts missing from he_alert_tone."""',
    "",
    "from __future__ import annotations",
    "",
    "MANUAL_HE_BATCH70: dict[str, str] = {",
]
for k, v in manual.items():
    lines.append(f"    {json.dumps(k, ensure_ascii=False)}: {json.dumps(v, ensure_ascii=False)},")
lines.append("}")
lines.append("")

OUT.write_text("\n".join(lines) + "\n", encoding="utf-8")
print(f"Wrote {len(manual)} entries to {OUT.name}")
