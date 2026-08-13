import json
import re
from pathlib import Path

SRC = Path(r"c:\apps\hehehe\app\src\main\java\com\beardytop\mitzmode\ui\components\DailyMitzvotChecklist.kt")
OUT = Path(r"c:\apps\hehehe\be-a-tzaddik\data\checklist-items.json")

MALE_REQUIRED = {
    "Ritual hand washing", "Have Mezuzot on your doorposts", "Wear a Kippah (head covering)",
    "Put on Tefillin during morning prayers (except Shabbat/Festivals)",
    "Wear Tzitzit (recommended for divine protection)", "Keep Kosher",
    "Morning Blessings (Birchot HaShachar)", "Torah Blessings + minimal Torah study",
    "Weekly Parsha reading (twice in Hebrew, once in Targum)",
    "Minimum Pesukei D'Zimra", "Morning Shema with its blessings", "Shemoneh Esrei",
    "Mincha - Shemoneh Esrei", "Evening Shema with its blessings", "Maariv Shemoneh Esrei",
    "Bedtime Shema (first paragraph - though recommended to say entire Shema for spiritual protection)",
    "Hamapil blessing (according to many opinions)", "Prepare for and observe Shabbat and Festivals",
    "Say 100 brachot (blessings) today", "100 Daily Blessings",
}
FEMALE_REQUIRED = {
    "Ritual hand washing", "Have Mezuzot on your doorposts", "At least one prayer daily (typically morning)",
    "Cover hair in public (if married)", "Modesty (Tznius)", "Blessings before food", "Blessings after food",
    "Asher Yatzar after using bathroom", "Keep Kosher", "Family Purity Laws (if married)",
    "Prepare for and observe Shabbat and Festivals", "Torah Study",
}

IMPORTANT_DAILY = {
    "Ritual hand washing", "Wear a Kippah (head covering)",
    "Put on Tefillin during morning prayers (except Shabbat/Festivals)",
    "Wear Tzitzit (recommended for divine protection)", "Keep Kosher",
    "Have Mezuzot on your doorposts", "Immerse food vessels in mikveh", "100 Daily Blessings",
}

MORNING_PRAYER = {
    "Morning Blessings (Birchot HaShachar)", "Torah Blessings + minimal Torah study",
    "Minimum Pesukei D'Zimra", "Morning Shema with its blessings", "Shemoneh Esrei",
    "Musaf (only on Rosh Chodesh, Festivals, and Shabbat)", "Weekly Parsha reading (twice in Hebrew, once in Targum)",
}

SHABBAT_SECTION = {
    "Prepare for and observe Shabbat and Festivals",
}


def slug(t: str) -> str:
    s = re.sub(r"[^a-z0-9]+", "_", t.lower()).strip("_")[:60]
    return s or "item"


def time_for(t: str) -> str:
    if any(x in t for x in ("Evening", "Maariv", "Bedtime", "Hamapil")):
        return "night"
    if any(x in t for x in ("Morning", "Mincha", "Pesukei", "Tefillin", "Parsha", "Birchot HaShachar", "Musaf")):
        return "day"
    return "any"


def gender_for(t: str) -> str | None:
    if t in ("Cover hair in public (if married)", "Family Purity Laws (if married)", "Modesty (Tznius)",
             "At least one prayer daily (typically morning)"):
        return "female"
    if any(x in t for x in ("Tefillin", "Kippah", "Tzitzit", "Musaf", "100 Daily", "100 brachot",
                             "Shemoneh Esrei/Tachanun", "Maariv Shemoneh", "Weekly Parsha",
                             "Torah Blessings + minimal", "Minimum Pesukei")):
        return "male"
    return None


def section_for(t: str) -> str:
    if t in IMPORTANT_DAILY:
        return "Important Daily Mitzvot"
    if t in MORNING_PRAYER:
        return "Morning prayer (Shacharit)"
    if t in SHABBAT_SECTION:
        return "Shabbat & Festivals"
    return "Daily"


text = SRC.read_text(encoding="utf-8")
titles = []
for m in re.finditer(r'ChecklistItemWithInfo\(\s*text\s*=\s*"([^"]+)"', text):
    t = m.group(1)
    if t not in titles:
        titles.append(t)

items = []
for t in titles:
    g = gender_for(t)
    item = {
        "id": slug(t),
        "title": t,
        "section": section_for(t),
        "timeOfDay": time_for(t),
        "required": t in MALE_REQUIRED or t in FEMALE_REQUIRED,
        "explanation": "",
        "links": [{"displayText": "Learn more", "url": "https://www.sefaria.org"}],
        "hideOnShabbat": "Tefillin" in t or t in SHABBAT_SECTION,
        "shabbatOnly": "Musaf" in t,
    }
    if g:
        item["gender"] = g
    if "if married" in t.lower():
        item["married"] = True
    items.append(item)

for eid, title, section, tod, req, shabbat_only, shabbat_eve_only in [
    ("shabbat_candles", "Light Shabbat candles before sunset", "Shabbat & Festivals", "day", True, False, True),
    ("kiddush_friday", "Friday night Kiddush", "Shabbat & Festivals", "night", False, True, False),
    ("electronics_shabbat", "Avoid electronics on Shabbat and Festivals", "Shabbat & Festivals", "any", False, True, False),
]:
    items.append({
        "id": eid, "title": title, "section": section, "timeOfDay": tod, "required": req,
        "explanation": "", "links": [{"displayText": "Learn more", "url": "https://www.chabad.org/shabbat"}],
        "hideOnShabbat": False, "shabbatOnly": shabbat_only, "shabbatEveOnly": shabbat_eve_only,
    })

OUT.parent.mkdir(parents=True, exist_ok=True)
OUT.write_text(json.dumps({"version": 1, "items": items}, ensure_ascii=False, indent=2), encoding="utf-8")
print(f"Wrote {len(items)} items to {OUT}")
