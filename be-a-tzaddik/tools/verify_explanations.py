"""Verify checklist explanations match MitzMode source."""
import json
import re
from pathlib import Path

MITZMODE = Path(r"c:\apps\hehehe\app\src\main\java\com\beardytop\mitzmode\ui\components\DailyMitzvotChecklist.kt")
CONSTANTS = Path(r"c:\apps\hehehe\app\src\main\java\com\beardytop\mitzmode\util\Constants.kt")
CHECKLIST = Path(__file__).resolve().parents[1] / "shared/src/commonMain/composeResources/files/checklist-items.json"

SHABBAT_PREP = (
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

ELECTRONICS_NOTE = (
    "Please refrain from using phones, computers, or any electronics during Shabbat "
    "and Festivals, as these are holy days of complete rest."
)


def parse_explanations(text: str) -> tuple[dict[str, str], dict[str, str], dict[str, list]]:
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
        target = female if pos > female_idx else male
        target[title] = expl

        lm = re.search(
            r'link\s*=\s*Link\(\s*displayText\s*=\s*"([^"]+)"\s*,\s*url\s*=\s*"([^"]+)"',
            block,
        )
        if lm:
            links[title] = [{"displayText": lm.group(1), "url": lm.group(2)}]

    const = CONSTANTS.read_text(encoding="utf-8")
    for key, title in [("male", "Wear a Kippah (head covering)"), ("female", "Cover hair in public (if married)")]:
        m = re.search(rf'"{key}"\s+to\s+"""(.*?)"""', const, re.DOTALL)
        if m:
            (male if key == "male" else female)[title] = m.group(1).strip()

    return male, female, links


def main() -> None:
    text = MITZMODE.read_text(encoding="utf-8")
    male, female, links = parse_explanations(text)
    data = json.loads(CHECKLIST.read_text(encoding="utf-8"))

    missing = []
    wrong = []
    for item in data["items"]:
        title = item["title"]
        iid = item["id"]
        expected = male.get(title) or female.get(title)
        if iid == "shabbat_candles":
            expected = (
                "Light candles before sunset to welcome Shabbat and Festivals.\n\n"
                + SHABBAT_PREP
            )
        elif iid == "electronics_shabbat":
            expected = SHABBAT_PREP + "\n\n" + ELECTRONICS_NOTE
        elif iid == "kiddush_friday":
            expected = SHABBAT_PREP
        if not expected:
            missing.append(title)
            continue
        actual = item.get("explanation", "")
        if actual != expected:
            wrong.append((title, len(actual), len(expected)))

    print(f"MitzMode male entries: {len(male)}")
    print(f"MitzMode female entries: {len(female)}")
    print(f"Missing from MitzMode: {missing}")
    print(f"Text mismatch count: {len(wrong)}")
    for t, a, e in wrong[:10]:
        print(f"  mismatch: {t} (actual {a} vs expected {e} chars)")


if __name__ == "__main__":
    main()
