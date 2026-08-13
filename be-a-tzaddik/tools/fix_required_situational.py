"""All mitzvot are required; situational ones apply only when relevant (not 'optional')."""
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CHECKLIST = ROOT / "shared/src/commonMain/composeResources/files/checklist-items.json"
DATA_COPY = ROOT / "data/checklist-items.json"

SITUATIONAL_IDS = {
    "immerse_food_vessels_in_mikveh",
    "blessing_on_wine_when_drinking",
    "say_amen_to_others_blessings",
    "check_mezuzot_periodically",
    "return_lost_objects",
    "hospitality_to_guests",
    "separate_challah_from_dough",
    "brit_milah_for_sons",
    "torah_study_shavuot",
    "rebuke_with_love_not_hatred",
}

REBuke_ITEM = {
    "id": "rebuke_with_love_not_hatred",
    "title": "Rebuke with love when someone wronged you",
    "section": "Between people",
    "sortOrder": 55,
    "timeOfDay": "any",
    "required": True,
    "situational": True,
    "explanation": (
        "When someone hurts you, Torah teaches to speak to them privately and gently — not to "
        "nurse hatred. This mitzvah applies when the situation arises; it is not a daily checklist "
        "item.\n\n"
        "If you cannot speak safely, ask a rabbi. The goal is to restore peace, not to embarrass."
    ),
    "links": [
        {
            "displayText": "Rebuke your fellow (Vayikra 19:17)",
            "url": "https://www.sefaria.org/Leviticus.19.17",
        },
        {
            "displayText": "Rambam — how to rebuke",
            "url": "https://www.sefaria.org/Mishneh_Torah,_Human_Dispositions.6.7",
        },
    ],
    "hideOnShabbat": False,
    "shabbatOnly": False,
    "persistChecked": False,
}

EXPLANATION_FIXES = [
    (
        "While technically optional according to some opinions, it has become obligatory",
        "While some opinions treated it as non-obligatory, universal Jewish practice treats it as required",
    ),
    (
        "While some consider it optional, many authorities recommend saying it nightly",
        "Many authorities treat it as part of nightly practice; follow your siddur and rabbi",
    ),
    (
        "While technically only required when wearing such a garment",
        "The biblical mitzvah applies when wearing a four-cornered garment; daily wear is standard practice",
    ),
]


def main() -> None:
    data = json.loads(CHECKLIST.read_text(encoding="utf-8"))
    ids = {it["id"] for it in data["items"]}
    if REBuke_ITEM["id"] not in ids:
        data["items"].append(REBuke_ITEM)
        ids.add(REBuke_ITEM["id"])

    for item in data["items"]:
        item["required"] = True
        item["situational"] = item["id"] in SITUATIONAL_IDS

        exp = item.get("explanation", "")
        for old, new in EXPLANATION_FIXES:
            exp = exp.replace(old, new)
        item["explanation"] = exp
        if item.get("explanationFemale"):
            ef = item["explanationFemale"]
            for old, new in EXPLANATION_FIXES:
                ef = ef.replace(old, new)
            item["explanationFemale"] = ef

    data["version"] = 6
    text = json.dumps(data, ensure_ascii=False, indent=2)
    CHECKLIST.write_text(text, encoding="utf-8")
    DATA_COPY.write_text(text, encoding="utf-8")
    situational = sum(1 for it in data["items"] if it.get("situational"))
    print(f"v{data['version']}: {len(data['items'])} items, {situational} situational, all required=true")


if __name__ == "__main__":
    main()
