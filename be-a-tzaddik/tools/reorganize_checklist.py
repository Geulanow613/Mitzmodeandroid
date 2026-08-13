"""Reassign sections, sortOrder, persistChecked; add Modeh Ani + split parsha items."""
import json
from copy import deepcopy
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CHECKLIST = ROOT / "shared/src/commonMain/composeResources/files/checklist-items.json"
DATA_COPY = ROOT / "data/checklist-items.json"

MODEH_ANI = {
    "id": "modeh_ani_upon_waking",
    "title": "Modeh Ani upon waking",
    "section": "Upon waking",
    "sortOrder": 10,
    "timeOfDay": "day",
    "required": True,
    "explanation": (
        "The first prayer upon waking, thanking G-d for restoring your soul. "
        "Many say it immediately, even while still in bed, before getting up."
    ),
    "links": [],
    "hideOnShabbat": False,
    "shabbatOnly": False,
}

PARSHA_SPLIT = [
    {
        "id": "weekly_parsha_hebrew_first",
        "title": "Weekly parsha — Hebrew (1st reading)",
        "sortOrder": 20,
        "explanation": (
            "Shnayim Mikra v'Echad Targum: read the weekly Torah portion in Hebrew. "
            "This is the first of two Hebrew readings for the week."
        ),
    },
    {
        "id": "weekly_parsha_hebrew_second",
        "title": "Weekly parsha — Hebrew (2nd reading)",
        "sortOrder": 30,
        "explanation": (
            "Second Hebrew reading of the weekly parsha. Many people divide the parsha "
            "across the week and complete both Hebrew readings before Shabbat."
        ),
    },
    {
        "id": "weekly_parsha_onkelos_once",
        "title": "Weekly parsha — Onkelos / Targum (once)",
        "sortOrder": 40,
        "explanation": (
            "Read the weekly portion once in Targum Onkelos (Aramaic translation). "
            "If you cannot read Aramaic, some authorities allow a faithful translation "
            "in a language you understand—ask your rabbi."
        ),
    },
]

# id -> (section, sortOrder, persistChecked)
LAYOUT = {
    # Upon waking
    "modeh_ani_upon_waking": ("Upon waking", 10, False),
    "ritual_hand_washing": ("Upon waking", 20, False),
    # Morning prayer
    "morning_blessings_birchot_hashachar": ("Morning Prayer (Shacharit)", 10, False),
    "torah_blessings_minimal_torah_study": ("Morning Prayer (Shacharit)", 20, False),
    "minimum_pesukei_d_zimra": ("Morning Prayer (Shacharit)", 30, False),
    "morning_shema_with_its_blessings": ("Morning Prayer (Shacharit)", 40, False),
    "shemoneh_esrei_tachanun": ("Morning Prayer (Shacharit)", 50, False),
    "put_on_tefillin_during_morning_prayers_except_shabbat_festiv": (
        "Morning Prayer (Shacharit)",
        60,
        False,
    ),
    "musaf_only_on_rosh_chodesh_festivals_and_shabbat": ("Morning Prayer (Shacharit)", 70, False),
    # Afternoon / evening
    "mincha_shemoneh_esrei_tachanun": ("Afternoon Prayer", 10, False),
    "evening_shema_with_its_blessings": ("Evening Prayer", 10, False),
    "maariv_shemoneh_esrei": ("Evening Prayer", 20, False),
    "bedtime_shema_first_paragraph_though_recommended_to_say_enti": ("Evening Prayer", 30, False),
    "hamapil_blessing_according_to_many_opinions": ("Evening Prayer", 40, False),
    # Torah study (men)
    "torah_study": ("Torah Study", 10, False),
    "weekly_parsha_hebrew_first": ("Torah Study", 20, False),
    "weekly_parsha_hebrew_second": ("Torah Study", 30, False),
    "weekly_parsha_onkelos_once": ("Torah Study", 40, False),
    # Eating
    "blessings_before_food": ("Eating & Blessings", 10, False),
    "blessings_after_food": ("Eating & Blessings", 20, False),
    "asher_yatzar_after_using_bathroom": ("Eating & Blessings", 30, False),
    "100_daily_blessings": ("Eating & Blessings", 40, False),
    # Ongoing
    "wear_a_kippah_head_covering": ("Ongoing mitzvot", 10, False),
    "wear_tzitzit_recommended_for_divine_protection": ("Ongoing mitzvot", 20, False),
    "keep_kosher": ("Ongoing mitzvot", 30, True),
    "have_mezuzot_on_your_doorposts": ("Ongoing mitzvot", 40, True),
    "immerse_food_vessels_in_mikveh": ("Ongoing mitzvot", 50, True),
    # Shabbat
    "prepare_for_and_observe_shabbat_and_festivals": ("Shabbat & Festivals", 10, False),
    "shabbat_candles": ("Shabbat & Festivals", 20, False),
    "kiddush_friday": ("Shabbat & Festivals", 30, False),
    "electronics_shabbat": ("Shabbat & Festivals", 40, False),
    # Female
    "at_least_one_prayer_daily_typically_morning": ("Daily Prayer", 30, False),
    "cover_hair_in_public_if_married": ("Important Lifestyle Mitzvot", 20, True),
    "modesty_tznius": ("Important Lifestyle Mitzvot", 30, True),
    "family_purity_laws_if_married": ("Important Lifestyle Mitzvot", 40, True),
}

FEMALE_OVERRIDES = {
    "ritual_hand_washing": ("Daily Prayer", 10, False),
    "torah_study": ("Torah Study", 10, False),
    "have_mezuzot_on_your_doorposts": ("Important Lifestyle Mitzvot", 10, True),
    "keep_kosher": ("Important Lifestyle Mitzvot", 50, True),
    "immerse_food_vessels_in_mikveh": ("Important Lifestyle Mitzvot", 60, True),
}


def main():
    data = json.loads(CHECKLIST.read_text(encoding="utf-8"))
    by_id = {it["id"]: it for it in data["items"]}
    old_parsha = by_id.pop("weekly_parsha_reading_twice_in_hebrew_once_in_targum", None)

    new_items = []
    # Modeh Ani
    new_items.append(MODEH_ANI)

    # Parsha split from old entry
    base_links = old_parsha.get("links", []) if old_parsha else []
    base_gender = old_parsha.get("gender") if old_parsha else "male"
    for spec in PARSHA_SPLIT:
        new_items.append(
            {
                "id": spec["id"],
                "title": spec["title"],
                "section": "Torah Study",
                "sortOrder": spec["sortOrder"],
                "timeOfDay": "day",
                "required": old_parsha.get("required", True) if old_parsha else True,
                "explanation": spec["explanation"],
                "links": base_links,
                "hideOnShabbat": False,
                "shabbatOnly": False,
                "gender": base_gender,
            }
        )

    for item in data["items"]:
        if item["id"] == "weekly_parsha_reading_twice_in_hebrew_once_in_targum":
            continue
        it = deepcopy(item)
        iid = it["id"]
        if iid in LAYOUT:
            section, order, persist = LAYOUT[iid]
            it["section"] = section
            it["sortOrder"] = order
            it["persistChecked"] = persist
        if it.get("gender") == "female" and iid in FEMALE_OVERRIDES:
            section, order, persist = FEMALE_OVERRIDES[iid]
            it["section"] = section
            it["sortOrder"] = order
            it["persistChecked"] = persist
        if "sortOrder" not in it:
            it["sortOrder"] = 500
        if "persistChecked" not in it:
            it["persistChecked"] = False
        new_items.append(it)

    # Dedupe by id (modeh added first, also in loop? modeh not in original)
    seen = set()
    deduped = []
    for it in new_items:
        if it["id"] in seen:
            continue
        seen.add(it["id"])
        deduped.append(it)

    data["version"] = 2
    data["items"] = deduped
    text = json.dumps(data, ensure_ascii=False, indent=2)
    CHECKLIST.write_text(text, encoding="utf-8")
    DATA_COPY.write_text(text, encoding="utf-8")
    print(f"Wrote {len(deduped)} items to {CHECKLIST}")


if __name__ == "__main__":
    main()
