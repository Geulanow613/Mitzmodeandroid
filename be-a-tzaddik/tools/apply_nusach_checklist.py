"""Add nusach-specific explanations to main checklist; sync extras defaults."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CHECKLIST = ROOT / "shared/src/commonMain/composeResources/files/checklist-items.json"
DATA_COPY = ROOT / "data/checklist-items.json"

NUSACH_EXPLANATIONS = {
    "shemoneh_esrei_tachanun": {
        "explanationAshkenaz": (
            "Tachanun (Ashkenaz): On most weekdays Tachanun follows Shacharit Amidah — usually sitting with "
            "nefilat apayim. Mondays and Thursdays: longer Tachanun. Other weekdays: shorter form. Omitted on "
            "festive days in your siddur."
        ),
        "explanationSefard": (
            "Tachanun (Sefard): Usually after Shacharit on weekdays; posture varies by community. Longer on "
            "Mondays and Thursdays in many communities. Omitted on festive days."
        ),
        "explanationChabad": (
            "Tachanun (Chabad / Nusach Ari): Amidah from Tehillat Hashem. Tachanun on ordinary weekdays at "
            "Shacharit; omitted on special days in the siddur."
        ),
    },
    "mincha_shemoneh_esrei_tachanun": {
        "explanationAshkenaz": (
            "Tachanun at Mincha (Ashkenaz): Often after Mincha on weekdays. Omitted on festive days."
        ),
        "explanationSefard": (
            "Tachanun at Mincha (Edot HaMizrach): Recited completely normally on weekdays, including the Vidui "
            "(Ashamnu confession) and the Thirteen Attributes of Mercy. However, following the Arizal "
            "(per Kaf HaChaim and Yalkut Yosef), you do not perform nefilat apayim (leaning your head on your arm) "
            "at Mincha — recite the entire prayer sitting completely upright, including Psalm 25 (LeDavid Alecha). "
            "Omitted on Rosh Chodesh, holidays, and standard festive days."
        ),
        "explanationChabad": (
            "Tachanun at Mincha (Chabad): Often shorter Tachanun after Mincha on weekdays; omitted on festive days."
        ),
    },
    "morning_blessings_birchot_hashachar": {
        "explanationChabad": (
            "Follow Tehillat Hashem: Birchot HaShachar, then Korbanot and other morning passages before Pesukei Dezimra."
        ),
    },
    "100_daily_blessings": {
        "explanationAshkenaz": (
            "Ashkenaz: aim for 100 brachot daily — weekdays usually reach the count through prayer and meals; "
            "on Shabbat/Yom Tov add brachot on food and fragrances."
        ),
        "explanationSefard": (
            "Sefard: same obligation of 100 brachot; your siddur may differ in which prayers contain multiple brachot."
        ),
        "explanationChabad": (
            "Chabad: 100 brachot daily; shorter Shabbat davening means planning extra brachot on snacks and besamim."
        ),
    },
}


def main() -> None:
    data = json.loads(CHECKLIST.read_text(encoding="utf-8"))
    for item in data["items"]:
        patch = NUSACH_EXPLANATIONS.get(item["id"])
        if patch:
            item.update(patch)
    data["version"] = 7
    text = json.dumps(data, ensure_ascii=False, indent=2)
    CHECKLIST.write_text(text, encoding="utf-8")
    DATA_COPY.write_text(text, encoding="utf-8")
    print(f"Updated checklist v{data['version']} ({len(data['items'])} items)")


if __name__ == "__main__":
    main()
