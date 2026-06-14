#!/usr/bin/env python3
"""Apply batch halachic copy corrections across app source files."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

REPLACEMENTS = [
    (
        "This is called kuach and permits saying holy words, but does NOT remove the spiritual impurity.",
        "This is called Nikuy (ניקוי — cleaning by friction) and permits saying holy words, but does NOT remove the spiritual impurity.",
    ),
    (
        "• On days with Musaf (Shabbat, Rosh Chodesh, Yom Tov), the tashlumin for missed Shacharit is said after Musaf.",
        "• If you missed Shacharit on Shabbat, Rosh Chodesh, or Yom Tov, make it up at Mincha — not at Musaf. Musaf is a separate holiday Amidah, not a standard daily boundary prayer. Pray Mincha first, pause, then a second Amidah as tashlumin (SA O.C. 108:9).",
    ),
    (
        "However, if they have already been gathered once and spill on your kitchen floor, picking them up is permitted — the melacha of gathering is considered complete.",
        "However, the prohibition of gathering applies strictly to the place where the items naturally grow (like a field or orchard). Gathering items that spill on a kitchen floor does not violate Me'ameir under any circumstances.",
    ),
    (
        'The Baal Shem Tov taught that Pesach Sheni represents the idea that "it\'s never too late" — that there is always an opportunity for a second chance.',
        'A classic Chabad teaching — attributed to the Lubavitcher Rebbeim (Rabbi Yosef Yitzchak Schneersohn, citing his father the Rebbe Rashab) — holds that Pesach Sheni means "ein davar avud": nothing is lost; it is always possible to correct and return.',
    ),
    (
        "Ashkenazim outside Israel often recite Ein Kelokeinu (אֵין כֵאלֹקֵינו) at the end of Shacharit on Shabbat and festivals because the weekday Amidah has nineteen blessings while the Shabbat Amidah has only seven, so you fall further short of one hundred.",
        "Ashkenazim worldwide recite Ein Kelokeinu (אֵין כֵאלֹקֵינו) at the end of Shacharit on Shabbat and festivals — in Israel and the Diaspora alike. (On weekdays, Ashkenazim omit it; Sephardim and Chabad recite it daily.) Ashkenaz recite it on Shabbat and Yom Tov especially because the weekday Amidah has nineteen blessings while the Shabbat Amidah has only seven, so you fall further short of one hundred on those days.",
    ),
    (
        "Birkat Kohanim — priestly blessing; Sephardim daily worldwide; Diaspora Ashkenazim on festivals",
        "Birkat Kohanim — Priestly blessing; recited daily by everyone in Israel and by Sephardim worldwide; recited strictly on festivals by Ashkenazim in the Diaspora",
    ),
    (
        "Musaf adds complexity; ask your rav.",
        "Missed holiday Shacharit is made up at Mincha, not at Musaf (SA O.C. 108:9).",
    ),
    (
        "• Eating fresh grapes as fruit (not drinking juice): many say Al HaEtz instead — see shivat ha-minim below\n\nAl HaEtz (עַל הָעֵץ וְעַל פְּרִי הָעֵץ — Bracha Achat Me'ein Shalosh):\n• Only after shivat ha-minim (seven species) fruits that grow on trees: grapes (as fruit, not juice), dates, figs, pomegranates, olives\n• The full phrase is על העץ ועל פרי העץ (the tree and the fruit of the tree) throughout the blessing — opening, thanks, and closing — not על העץ alone\n• Not for apples, oranges, bananas, berries, or other tree fruits — those use Borei Nefashot only",
        "• Wine or grape juice only — fresh grapes eaten as fruit take Al HaEtz (see below), not Al HaGafen\n\nAl HaEtz (עַל הָעֵץ וְעַל פְּרִי הָעֵץ — Bracha Achat Me'ein Shalosh):\n• After shivat ha-minim (Seven Species) tree fruits: dates, figs, pomegranates, olives — and fresh grapes eaten as fruit (not grape juice)\n• Fresh grapes: Because grapes are one of the Seven Species, eating a kezayit of fresh grapes always requires Al HaEtz (Me'ein Shalosh), not Borei Nefashot — whereas regular tree fruits like apples or oranges take Borei Nefashot only\n• The full phrase is על העץ ועל פרי העץ (the tree and the fruit of the tree) throughout the blessing — opening, thanks, and closing — not על העץ alone\n• Not for apples, oranges, bananas, berries, or other non–shivat ha-minim tree fruits — those use Borei Nefashot only",
    ),
    (
        "Without an eruv, house keys may be worn as functional jewelry to avoid the prohibition.",
        "Without an eruv, a house key can only be worn outdoors if it is structurally integrated by a specialist into a functional piece of clothing or jewelry (such as a belt clasp or decorative pin). Simply hanging a loose key on a string around your neck is a direct violation of carrying.",
    ),
    (
        "Tachanun at Mincha (Sefard / Mizrahi): In many communities Tachanun is not said at Mincha (only at Shacharit). Follow your community; if your minyan says Tachanun at Mincha, do as they do.",
        "Tachanun at Mincha (Edot HaMizrach): Recited completely normally on weekdays, including the Vidui (Ashamnu confession) and the Thirteen Attributes of Mercy. However, following the Arizal (per Kaf HaChaim and Yalkut Yosef), you do not perform nefilat apayim (leaning your head on your arm) at Mincha — recite the entire prayer sitting completely upright, including Psalm 25 (LeDavid Alecha). Omitted on Rosh Chodesh, holidays, and standard festive days.",
    ),
    (
        "All chametz must be gone before Shabbat or Yom Tov begins (sold, burned, or removed).",
        "All chametz must be completely gone, destroyed, or sold, and the final Kol Chamira recited, before the end of the 5th halachic hour this morning (midday threshold) — NOT sunset. Stop eating chametz by the end of the 4th halachic hour.",
    ),
    (
        "All chametz must be gone before sunset (sold, burned, or removed).",
        "All chametz must be completely gone, destroyed, or sold, and the final Kol Chamira recited, before the end of the 5th halachic hour this morning (midday threshold) — NOT sunset. Stop eating chametz by the end of the 4th halachic hour.",
    ),
    (
        "Literally it's a acronym for G-d, King who is faithful)",
        "(Literally, it's an acronym for 'G-d, King Who is faithful' — Kel Melech Ne'eman).",
    ),
    (
        "you shouldn't pronouce in vain",
        "you shouldn't pronounce in vain",
    ),
    (
        "1.25 hours before sunset",
        "1.25 halachic hours before sunset",
    ),
    (
        "  — Ashkenaz / Mishnah Berurah: two normal gulps with one brief pause between them — often estimated about 9–12 seconds",
        "  — Ashkenaz / Mishnah Berurah: often estimated as a continuous sequence of normal gulps within 5–9 seconds max. To guarantee your obligation for an after-blessing, drink the beverage in a single, uninterrupted sequence.",
    ),
    (
        "about 9–12 seconds per Mishnah Berurah / many Ashkenazim — not comparable to k'dei achilat pras",
        "about 5–9 seconds max per Mishnah Berurah / many Ashkenazim — not comparable to k'dei achilat pras",
    ),
]

CHECKLIST_PATHS = [
    ROOT / "shared/src/commonMain/composeResources/files/checklist-items.json",
    ROOT / "data/checklist-items.json",
    ROOT.parent / "ios-transfer-handoff/be-a-tzaddik/shared/src/commonMain/composeResources/files/checklist-items.json",
    ROOT.parent / "ios-transfer-handoff/be-a-tzaddik/data/checklist-items.json",
]

OTHER_TEXT_FILES = [
    ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/ui/screens/ShabbatGuideData.kt",
    ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/HalachicTermsDictionary.kt",
    ROOT / "tools/update_netilat_nusach.py",
    ROOT.parent / "ios-transfer-handoff/be-a-tzaddik/shared/src/commonMain/kotlin/com/beardytop/beatzaddik/ui/screens/ShabbatGuideData.kt",
    ROOT.parent / "ios-transfer-handoff/be-a-tzaddik/shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/HalachicTermsDictionary.kt",
]


def apply_replacements(text: str) -> tuple[str, int]:
    count = 0
    for old, new in REPLACEMENTS:
        if old in text:
            n = text.count(old)
            text = text.replace(old, new)
            count += n
    return text, count


def fix_checklist(path: Path) -> int:
    if not path.exists():
        return 0
    data = json.loads(path.read_text(encoding="utf-8"))
    total = 0
    for item in data.get("items", []):
        for field in (
            "explanation",
            "explanationAshkenaz",
            "explanationSefard",
            "explanationChabad",
            "explanationFemale",
        ):
            if field in item and item[field]:
                new, n = apply_replacements(item[field])
                item[field] = new
                total += n
    path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    return total


def fix_text_file(path: Path) -> int:
    if not path.exists():
        return 0
    text = path.read_text(encoding="utf-8")
    new, n = apply_replacements(text)
    if n:
        path.write_text(new, encoding="utf-8")
    return n


def main() -> None:
    total = 0
    for p in CHECKLIST_PATHS:
        c = fix_checklist(p)
        if c:
            print(f"{p.relative_to(ROOT.parent)}: {c} replacements")
        total += c
    for p in OTHER_TEXT_FILES:
        c = fix_text_file(p)
        if c:
            print(f"{p.relative_to(ROOT.parent)}: {c} replacements")
        total += c
    print(f"Total replacements: {total}")


if __name__ == "__main__":
    main()
