"""Assign verified learn-more URLs per checklist item id."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CHECKLIST = ROOT / "shared/src/commonMain/composeResources/files/checklist-items.json"
DATA_COPY = ROOT / "data/checklist-items.json"
EXTRAS = ROOT / "shared/src/commonMain/composeResources/files/nusach-extras.json"

# URLs verified with verify_checklist_links.py (HTTP 200)
ITEM_LINKS: dict[str, list[dict]] = {
    "weekly_parsha_hebrew_first": [
        {
            "displayText": "Shulchan Arukh — Shnayim Mikra",
            "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.285",
        }
    ],
    "weekly_parsha_hebrew_second": [
        {
            "displayText": "Shulchan Arukh — Shnayim Mikra",
            "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.285",
        }
    ],
    "weekly_parsha_onkelos_once": [
        {
            "displayText": "Shulchan Arukh — Shnayim Mikra",
            "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.285",
        }
    ],
    "wear_a_kippah_head_covering": [
        {
            "displayText": "Rambam — head covering",
            "url": "https://www.sefaria.org/Mishneh_Torah,_Prayer_and_the_Priestly_Blessing.5.6",
        }
    ],
    "put_on_tefillin_during_morning_prayers_except_shabbat_festiv": [
        {
            "displayText": "Shulchan Arukh — tefillin",
            "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.25",
        },
        {
            "displayText": "Tefillin (Devarim 6:8)",
            "url": "https://www.sefaria.org/Deuteronomy.6.8",
        },
    ],
    "wear_tzitzit_recommended_for_divine_protection": [
        {
            "displayText": "Tzitzit (Bamidbar 15:38)",
            "url": "https://www.sefaria.org/Numbers.15.38",
        }
    ],
    "keep_kosher": [
        {
            "displayText": "Rambam — forbidden foods",
            "url": "https://www.sefaria.org/Mishneh_Torah,_Forbidden_Foods",
        }
    ],
    "immerse_food_vessels_in_mikveh": [
        {
            "displayText": "Shulchan Arukh — tevilat keilim",
            "url": "https://www.sefaria.org/Shulchan_Arukh,_Yoreh_De'ah.120",
        }
    ],
    "minimum_pesukei_d_zimra": [
        {
            "displayText": "Shulchan Arukh — Pesukei Dezimra",
            "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.51",
        }
    ],
    "shemoneh_esrei_tachanun": [
        {
            "displayText": "Shulchan Arukh — Amidah",
            "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.94",
        },
        {
            "displayText": "Shulchan Arukh — Tachanun",
            "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.131",
        },
    ],
    "musaf_only_on_rosh_chodesh_festivals_and_shabbat": [
        {
            "displayText": "Shulchan Arukh — Musaf",
            "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.286",
        }
    ],
    "mincha_shemoneh_esrei_tachanun": [
        {
            "displayText": "Shulchan Arukh — Mincha",
            "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.233",
        }
    ],
    "maariv_shemoneh_esrei": [
        {
            "displayText": "Shulchan Arukh — Maariv",
            "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.237",
        }
    ],
    "torah_study_women": [
        {
            "displayText": "Kitzur Shulchan Arukh",
            "url": "https://www.sefaria.org/Kitzur_Shulchan_Arukh",
        },
        {
            "displayText": "DailyHalacha.com",
            "url": "https://www.dailyhalacha.com/",
        },
        {
            "displayText": "Halachipedia",
            "url": "https://www.halachipedia.com/",
        },
        {
            "displayText": "Sefaria.org",
            "url": "https://www.sefaria.org/",
        },
    ],
    "torah_study": [
        {
            "displayText": "Rambam — Torah study",
            "url": "https://www.sefaria.org/Mishneh_Torah,_Torah_Study.1",
        }
    ],
    "shabbat_candles": [
        {
            "displayText": "Shulchan Arukh — Shabbat candles",
            "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.263",
        }
    ],
    "kiddush_friday": [
        {
            "displayText": "Shulchan Arukh — Kiddush",
            "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.271",
        }
    ],
    "prepare_for_and_observe_shabbat_and_festivals": [
        {
            "displayText": "Mishnah — Shabbat",
            "url": "https://www.sefaria.org/Shabbat.2",
        }
    ],
    "electronics_shabbat": [
        {
            "displayText": "Mishnah — Shabbat",
            "url": "https://www.sefaria.org/Shabbat.2",
        }
    ],
    "at_least_one_prayer_daily_typically_morning": [
        {
            "displayText": "Shulchan Arukh — women's prayer",
            "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.106",
        }
    ],
}

EXTRAS_LINKS: dict[str, list[dict]] = {
    "hayom_yom": [
        {
            "displayText": "Sefaria — Tanya (daily Chitas)",
            "url": "https://www.sefaria.org/Tanya",
            "nusach": "chabad",
        }
    ],
    "tachanun_extra": [
        {
            "displayText": "Shulchan Arukh — Tachanun",
            "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.131",
            "nusach": "default",
        }
    ],
    "birkat_kohanim": [
        {
            "displayText": "Birkat Kohanim (Bamidbar 6)",
            "url": "https://www.sefaria.org/Numbers.6.24",
            "nusach": "default",
        }
    ],
    "kiddush_chabad": [
        {
            "displayText": "Shulchan Arukh — Kiddush",
            "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.271",
            "nusach": "chabad",
        }
    ],
    "kiddush_default": [
        {
            "displayText": "Shulchan Arukh — Kiddush",
            "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.271",
            "nusach": "default",
        }
    ],
}

# Map nusach-extras by url pattern -> replacement
EXTRAS_URL_FIX = {
    "https://www.chabad.org/hayomyom": EXTRAS_LINKS["hayom_yom"],
    "https://www.sefaria.org/Hayom_Yom": EXTRAS_LINKS["hayom_yom"],
    "https://www.sefaria.org/Tachanun": EXTRAS_LINKS["tachanun_extra"],
    "https://www.sefaria.org/Birkat_Kohanim": EXTRAS_LINKS["birkat_kohanim"],
    "https://www.chabad.org/library/article_cdo/aid/3622/jewish/Kiddush.htm": EXTRAS_LINKS[
        "kiddush_chabad"
    ],
    "https://www.sefaria.org/Kiddush": EXTRAS_LINKS["kiddush_default"],
}

WRONG_HAND = "https://www.chabad.org/library/article_cdo/aid/7230155/jewish/What-You-Need-to-Know-About-Washing-Hands-Not-for-Bread.htm"
WRONG_MEZUZAH = "https://www.chabad.org/library/article_cdo/aid/278476/jewish/Mezuzah.htm"
WRONG_AISH = "https://aish.com/43-100-blessings-each-day/"
WRONG_DAILY = "https://www.sefaria.org/Daily_Life?tab=sources"
WRONG_SHABBAT = "https://www.chabad.org/shabbat"


def strip_wrong_links(item: dict) -> None:
    links = item.get("links", [])
    if not links:
        return
    url = links[0].get("url", "")
    if url in (WRONG_HAND, WRONG_MEZUZAH, WRONG_AISH, WRONG_DAILY, WRONG_SHABBAT):
        if item["id"] not in ITEM_LINKS and item["id"] != "ritual_hand_washing":
            if url == WRONG_HAND and item["id"] == "ritual_hand_washing":
                pass
            elif item["id"] != "100_daily_blessings":
                item["links"] = []


def main() -> None:
    data = json.loads(CHECKLIST.read_text(encoding="utf-8"))
    for item in data["items"]:
        iid = item["id"]
        if iid in ITEM_LINKS:
            item["links"] = ITEM_LINKS[iid]
        else:
            strip_wrong_links(item)
    text = json.dumps(data, ensure_ascii=False, indent=2)
    CHECKLIST.write_text(text, encoding="utf-8")
    DATA_COPY.write_text(text, encoding="utf-8")

    extras = json.loads(EXTRAS.read_text(encoding="utf-8"))
    for entry in extras.get("items", []):
        for link in entry.get("links", []):
            url = link.get("url", "")
            if url in EXTRAS_URL_FIX:
                replacement = EXTRAS_URL_FIX[url][0]
                link["displayText"] = replacement["displayText"]
                link["url"] = replacement["url"]
                if "nusach" in replacement and "nusach" not in link:
                    link["nusach"] = replacement["nusach"]
    EXTRAS.write_text(json.dumps(extras, ensure_ascii=False, indent=2), encoding="utf-8")
    print("Fixed checklist and nusach-extras links")


if __name__ == "__main__":
    main()
