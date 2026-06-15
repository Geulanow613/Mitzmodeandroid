"""Rebuild checklist-items.json to match MitzMode daily core (no bloat)."""
from __future__ import annotations

import json
from copy import deepcopy
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SRC_PATHS = [
    ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "checklist-items.json",
    ROOT / "data" / "checklist-items.json",
]

KEEP_IDS = {
    "modeh_ani_upon_waking",
    "ritual_hand_washing",
    "wear_a_kippah_head_covering",
    "wear_tzitzit_recommended_for_divine_protection",
    "keep_kosher",
    "have_mezuzot_on_your_doorposts",
    "immerse_food_vessels_in_mikveh",
    "100_daily_blessings",
    "torah_blessings_minimal_torah_study",
    "morning_blessings_birchot_hashachar",
    "minimum_pesukei_d_zimra",
    "morning_shema_with_its_blessings",
    "shemoneh_esrei_tachanun",
    "put_on_tefillin_during_morning_prayers_except_shabbat_festiv",
    "musaf_only_on_rosh_chodesh_festivals_and_shabbat",
    "mincha_shemoneh_esrei_tachanun",
    "evening_shema_with_its_blessings",
    "maariv_shemoneh_esrei",
    "torah_study",
    "bedtime_shema_first_paragraph_though_recommended_to_say_enti",
    "hamapil_blessing_according_to_many_opinions",
    "blessings_before_food",
    "blessings_after_food",
    "asher_yatzar_after_using_bathroom",
    "prepare_for_and_observe_shabbat_and_festivals",
    "shabbat_candles",
    "at_least_one_prayer_daily_typically_morning",
    "cover_hair_in_public_if_married",
    "modesty_tznius",
    "family_purity_laws_if_married",
}

REMOVE_IDS = {
    "weekly_parsha_hebrew_first",
    "weekly_parsha_hebrew_second",
    "weekly_parsha_onkelos_once",
}

SHNAYIM_MIKRA_EXPLANATION = (
    "There is a mitzvah to read the weekly Torah portion (Parsha) twice in Hebrew and once in "
    "Targum (Aramaic translation) each week, completing it before or on Shabbat (except on certain "
    "Festival days when the weekly parsha is overridden). This practice is called "
    "'Shnayim Mikra v'Echad Targum.' The goal is to become familiar with the Torah portion that "
    "will be read in synagogue on Shabbat.\n\n"
    "Some rabbis say that if you have no ability to read Hebrew, you can read the parsha twice and "
    "the translation of the targum once in the language you speak (instead of Hebrew/Aramaic — but "
    "this is not a universally accepted leniency). Many people spread this out throughout the week, "
    "reading a small portion each day."
)

SECTION_OVERRIDES = {
    "100_daily_blessings": "Ongoing mitzvot",
}

SORT_OVERRIDES = {
    "modeh_ani_upon_waking": 10,
    "ritual_hand_washing": 20,
    "wear_a_kippah_head_covering": 10,
    "wear_tzitzit_recommended_for_divine_protection": 20,
    "keep_kosher": 30,
    "have_mezuzot_on_your_doorposts": 40,
    "immerse_food_vessels_in_mikveh": 50,
    "100_daily_blessings": 60,
    "torah_blessings_minimal_torah_study": 10,
    "morning_blessings_birchot_hashachar": 20,
    "minimum_pesukei_d_zimra": 30,
    "morning_shema_with_its_blessings": 40,
    "shemoneh_esrei_tachanun": 50,
    "put_on_tefillin_during_morning_prayers_except_shabbat_festiv": 25,
    "musaf_only_on_rosh_chodesh_festivals_and_shabbat": 60,
    "mincha_shemoneh_esrei_tachanun": 10,
    "evening_shema_with_its_blessings": 10,
    "maariv_shemoneh_esrei": 20,
    "torah_study": 30,
    "bedtime_shema_first_paragraph_though_recommended_to_say_enti": 40,
    "hamapil_blessing_according_to_many_opinions": 50,
    "weekly_parsha_shnayim_mikra": 10,
    "blessings_before_food": 10,
    "blessings_after_food": 20,
    "asher_yatzar_after_using_bathroom": 30,
    "prepare_for_and_observe_shabbat_and_festivals": 10,
    "shabbat_candles": 20,
    "at_least_one_prayer_daily_typically_morning": 10,
    "cover_hair_in_public_if_married": 10,
    "modesty_tznius": 20,
    "family_purity_laws_if_married": 30,
}


def new_parsha_item() -> dict:
    return {
        "id": "weekly_parsha_shnayim_mikra",
        "title": "Weekly Parsha reading (twice in Hebrew, once in Targum)",
        "section": "Torah Study",
        "sortOrder": 10,
        "timeOfDay": "day",
        "required": True,
        "explanation": SHNAYIM_MIKRA_EXPLANATION,
        "links": [
            {
                "displayText": "Shulchan Arukh — Shnayim Mikra",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.285",
            }
        ],
        "hideOnShabbat": False,
        "shabbatOnly": False,
        "gender": "male",
        "persistChecked": False,
        "situational": False,
    }


def main() -> None:
    src = SRC_PATHS[0]
    data = json.loads(src.read_text(encoding="utf-8"))
    by_id = {item["id"]: item for item in data["items"]}

    items: list[dict] = []
    for item_id in KEEP_IDS:
        if item_id not in by_id:
            raise SystemExit(f"Missing expected item: {item_id}")
        item = deepcopy(by_id[item_id])
        if item_id in SECTION_OVERRIDES:
            item["section"] = SECTION_OVERRIDES[item_id]
        if item_id in SORT_OVERRIDES:
            item["sortOrder"] = SORT_OVERRIDES[item_id]
        items.append(item)

    items.append(new_parsha_item())

    # Stable section + sort order for UI
    section_rank = {
        "Upon waking": 0,
        "Ongoing mitzvot": 1,
        "Daily Prayer": 2,
        "Morning Prayer (Shacharit)": 3,
        "Afternoon Prayer": 4,
        "Evening Prayer": 5,
        "Torah Study": 6,
        "Eating & Blessings": 7,
        "Important Lifestyle Mitzvot": 8,
        "Prepare for Shabbat": 9,
    }

    def sort_key(it: dict) -> tuple:
        return (
            section_rank.get(it.get("section", ""), 99),
            it.get("sortOrder", 0),
            it.get("title", ""),
        )

    items.sort(key=sort_key)

    out = {"version": 9, "items": items}
    text = json.dumps(out, indent=2, ensure_ascii=False) + "\n"
    for path in SRC_PATHS:
        path.write_text(text, encoding="utf-8")
        print(f"Wrote {path} ({len(items)} items)")


if __name__ == "__main__":
    main()
