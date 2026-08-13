"""Populate Be a Tzaddik checklist explanations verbatim from MitzMode DailyMitzvotChecklist.kt."""
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CHECKLIST = ROOT / "shared/src/commonMain/composeResources/files/checklist-items.json"
EXTRAS = ROOT / "shared/src/commonMain/composeResources/files/nusach-extras.json"
MITZMODE = Path(r"c:\apps\hehehe\app\src\main\java\com\beardytop\mitzmode\ui\components\DailyMitzvotChecklist.kt")
CONSTANTS = Path(r"c:\apps\hehehe\app\src\main\java\com\beardytop\mitzmode\util\Constants.kt")

# Verbatim from DailyMitzvotChecklist.kt — "Prepare for and observe Shabbat and Festivals"
SHABBAT_PREP_EXPLANATION = (
    "Shabbat and Jewish Festivals are sacred times that elevate us spiritually:\n\n"
    "• Light candles before sunset to welcome these holy days\n"
    "• Prepare food and our home in advance\n"
    "• Refrain from work activities (melacha) such as using electronics, driving, handling money etc.\n"
    "• Enjoy festive meals with family and community\n"
    "• Focus on prayer, Torah study, and spiritual growth\n\n"
    "Proper observance of these days can bring our souls unique feelings of holiness "
    "and closeness with G-d that are unattainable during regular days. These elevated "
    "spiritual states can be experienced through proper observance of the laws."
)

# Verbatim footer note from DailyMitzvotChecklist.kt
ELECTRONICS_NOTE = (
    "Please refrain from using phones, computers, or any electronics during Shabbat "
    "and Festivals, as these are holy days of complete rest."
)

# Be-a-tzaddik-only items: composed only from MitzMode Shabbat text (no invented copy)
BE_A_TZADDIK_ONLY_BY_ID = {
    "shabbat_candles": (
        "Light candles before sunset to welcome Shabbat and Festivals.\n\n" + SHABBAT_PREP_EXPLANATION
    ),
    "kiddush_friday": SHABBAT_PREP_EXPLANATION,
    "electronics_shabbat": SHABBAT_PREP_EXPLANATION + "\n\n" + ELECTRONICS_NOTE,
}


def parse_mitzmode() -> tuple[dict[str, str], dict[str, str], dict[str, list]]:
    text = MITZMODE.read_text(encoding="utf-8")
    female_idx = text.find("private fun FemaleDailyChecklist")
    male: dict[str, str] = {}
    female: dict[str, str] = {}
    links: dict[str, list] = {}

    for m in re.finditer(r"ChecklistItemWithInfo\(", text):
        pos = m.start()
        block = text[pos : pos + 12000]
        tm = re.search(r'text\s*=\s*"([^"]+)"', block)
        if not tm:
            continue
        title = tm.group(1)
        em = re.search(
            r'explanation\s*=\s*("(?:[^"\\]|\\.)*"(?:\s*\+\s*"(?:[^"\\]|\\.)*")*)',
            block,
            re.DOTALL,
        )
        if not em:
            continue
        parts = re.findall(r'"((?:[^"\\]|\\.)*)"', em.group(1))
        expl = "".join(parts).replace("\\n", "\n").strip()
        if pos > female_idx:
            female[title] = expl
        else:
            male[title] = expl

        lm = re.search(
            r'link\s*=\s*Link\(\s*displayText\s*=\s*"([^"]+)"\s*,\s*url\s*=\s*"([^"]+)"',
            block,
        )
        if lm:
            links[title] = [{"displayText": lm.group(1), "url": lm.group(2)}]

    const = CONSTANTS.read_text(encoding="utf-8")
    m = re.search(r'"male"\s+to\s+"""(.*?)"""', const, re.DOTALL)
    if m:
        male["Wear a Kippah (head covering)"] = m.group(1).strip()
    m = re.search(r'"female"\s+to\s+"""(.*?)"""', const, re.DOTALL)
    if m:
        female["Cover hair in public (if married)"] = m.group(1).strip()

    return male, female, links


def patch_checklist(male: dict[str, str], female: dict[str, str], links: dict[str, list]) -> None:
    data = json.loads(CHECKLIST.read_text(encoding="utf-8"))
    unmatched = []
    for item in data["items"]:
        title = item["title"]
        iid = item["id"]

        if iid in BE_A_TZADDIK_ONLY_BY_ID:
            item["explanation"] = BE_A_TZADDIK_ONLY_BY_ID[iid]
        elif title in male:
            item["explanation"] = male[title]
        elif title in female:
            item["explanation"] = female[title]
        else:
            unmatched.append(title)

        # Female-specific wording when both genders share the same checklist row
        if title in female and title in male and female[title] != male[title]:
            item["explanationFemale"] = female[title]
        elif "explanationFemale" in item:
            del item["explanationFemale"]

        if title in links:
            item["links"] = links[title]
        elif item.get("links") and item["links"][0].get("url") == "https://www.sefaria.org":
            if item.get("explanation"):
                item["links"] = []

    CHECKLIST.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    print(f"Wrote {CHECKLIST.name}")
    if unmatched:
        print("No MitzMode match (need manual source):", unmatched)


def patch_extras_unchanged() -> None:
    """Nusach-only rows are not in MitzMode checklist — keep short original JSON text only."""
    pass


def main() -> None:
    male, female, links = parse_mitzmode()
    print(f"From MitzMode: {len(male)} male + {len(female)} female explanations, {len(links)} with links")
    patch_checklist(male, female, links)


if __name__ == "__main__":
    main()
