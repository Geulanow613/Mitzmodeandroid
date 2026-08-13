"""Add 'Between people' checklist section and repair known-bad learn-more URLs."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CHECKLIST = ROOT / "shared/src/commonMain/composeResources/files/checklist-items.json"
DATA_COPY = ROOT / "data/checklist-items.json"

# Verified GET 200 (Mar 2026)
LINKS = {
    "lashon_hara": [
        {
            "displayText": "Rambam — lashon hara (Mishneh Torah)",
            "url": "https://www.sefaria.org/Mishneh_Torah,_Human_Dispositions.7",
        },
        {
            "displayText": "OU — The prohibition of lashon hara",
            "url": "https://ou.org/torah/machshava/tzarich-iyun/tzarich_iyun_the_prohibition_of_lashon_hara/",
        },
    ],
    "rechilut": [
        {
            "displayText": "Rambam — gossip (rechilut)",
            "url": "https://www.sefaria.org/Mishneh_Torah,_Human_Dispositions.7.1",
        },
        {
            "displayText": "Halashon series (Chofetz Chaim)",
            "url": "https://torah.org/series/halashon/",
        },
    ],
    "honest_business": [
        {
            "displayText": "Rambam — honesty in sales",
            "url": "https://www.sefaria.org/Mishneh_Torah,_Sales.18",
        },
        {
            "displayText": "Proverbs on honest scales",
            "url": "https://www.sefaria.org/Proverbs.11.1",
        },
    ],
    "love_fellow": [
        {
            "displayText": "Love your fellow (Vayikra 19:18)",
            "url": "https://www.sefaria.org/Leviticus.19.18",
        },
        {
            "displayText": "Rambam — loving others",
            "url": "https://www.sefaria.org/Mishneh_Torah,_Human_Dispositions.6.3",
        },
    ],
    "rebuke": [
        {
            "displayText": "Rebuke your fellow (Vayikra 19:17)",
            "url": "https://www.sefaria.org/Leviticus.19.17",
        },
        {
            "displayText": "Rambam — how to rebuke",
            "url": "https://www.sefaria.org/Mishneh_Torah,_Human_Dispositions.6.7",
        },
    ],
    "forgive": [
        {
            "displayText": "Rambam — no revenge or grudges",
            "url": "https://www.sefaria.org/Mishneh_Torah,_Human_Dispositions.7.7",
        },
        {
            "displayText": "Vayikra 19:18 — love and forgiveness",
            "url": "https://www.sefaria.org/Leviticus.19.18",
        },
    ],
    "hand_washing": [
        {
            "displayText": "Chabad — ritual hand washing",
            "url": "https://www.chabad.org/library/article_cdo/aid/7230155/jewish/What-You-Need-to-Know-About-Washing-Hands-Not-for-Bread.htm",
        }
    ],
    "mezuzah": [
        {
            "displayText": "Chabad — mezuzah",
            "url": "https://www.chabad.org/library/article_cdo/aid/278476/jewish/Mezuzah.htm",
        }
    ],
    "brachot_100": [
        {
            "displayText": "Aish — 100 blessings a day",
            "url": "https://aish.com/43-100-blessings-each-day/",
        }
    ],
    "shabbat": [
        {"displayText": "Chabad — Shabbat", "url": "https://www.chabad.org/shabbat"}
    ],
}

WRONG_URL = "https://www.chabad.org/library/article_cdo/aid/7230155/jewish/What-You-Need-to-Know-About-Washing-Hands-Not-for-Bread.htm"
WRONG_MEZUZAH = "https://www.chabad.org/library/article_cdo/aid/278476/jewish/Mezuzah.htm"

SOCIAL_ITEMS = [
    {
        "id": "avoid_lashon_hara",
        "title": "Avoid lashon hara (harmful speech about others)",
        "section": "Between people",
        "sortOrder": 10,
        "timeOfDay": "any",
        "required": True,
        "explanation": (
            "Lashon hara is speaking negatively about another person even when what you say is true. "
            "The Torah warns that harmful speech destroys relationships and communities — our Sages "
            "compare its severity to idol worship, forbidden relations, and murder.\n\n"
            "Practical steps: pause before sharing a story about someone; ask if it is necessary and "
            "kind; change the subject when others gossip; if you heard something damaging, do not pass it on."
        ),
        "links": LINKS["lashon_hara"],
    },
    {
        "id": "avoid_rechilut_tale_bearing",
        "title": "Avoid rechilus (carrying tales between people)",
        "section": "Between people",
        "sortOrder": 20,
        "timeOfDay": "any",
        "required": True,
        "explanation": (
            "Rechilut is passing words from one person to another in a way that creates ill will — "
            "even if the report is true. It often leads to quarrels and broken friendships.\n\n"
            "If someone tells you what another person said about you, do not rush to confront them "
            "angrily; verify calmly and privately when appropriate. Do not be the messenger who spreads "
            "conflict."
        ),
        "links": LINKS["rechilut"],
    },
    {
        "id": "be_honest_in_business",
        "title": "Be honest in business and keep your word",
        "section": "Between people",
        "sortOrder": 30,
        "timeOfDay": "any",
        "required": True,
        "explanation": (
            "Jewish law requires honesty in buying and selling: disclose defects, do not deceive customers, "
            "and keep agreements. Honest weights and measures are a mitzvah; trickery in the marketplace "
            "is forbidden for Jew and non-Jew alike.\n\n"
            "This includes fair pricing, truthful advertising, honoring quotes, and returning money that "
            "is not rightfully yours. When in doubt, ask a rabbi."
        ),
        "links": LINKS["honest_business"],
    },
    {
        "id": "love_your_fellow_as_yourself",
        "title": "Love your fellow Jew as yourself",
        "section": "Between people",
        "sortOrder": 40,
        "timeOfDay": "any",
        "required": True,
        "explanation": (
            "\"Love your fellow as yourself\" (Vayikra 19:18) is a foundation of Jewish social life. "
            "It means wanting good for others, greeting them warmly, helping when you can, and "
            "speaking to them with respect.\n\n"
            "You need not agree with everyone — but you can still treat them with dignity and seek "
            "peace whenever possible."
        ),
        "links": LINKS["love_fellow"],
    },
]


def fix_item_links(item: dict) -> None:
    iid = item["id"]
    links = item.get("links", [])
    if not links:
        return
    url = links[0].get("url", "")
    if url == WRONG_URL:
        if iid == "ritual_hand_washing":
            item["links"] = LINKS["hand_washing"]
        elif "shema" in iid or "hamapil" in iid or "asher_yatzar" in iid or "blessings" in iid:
            item["links"] = []
        elif "mincha" in iid or "shemoneh" in iid or "musaf" in iid:
            item["links"] = [
                {
                    "displayText": "Sefaria — daily prayer",
                    "url": "https://www.sefaria.org/Daily_Life?tab=sources",
                }
            ]
    if url == WRONG_MEZUZAH and "tefillin" in iid:
        item["links"] = [
            {
                "displayText": "Chabad — tefillin",
                "url": "https://www.chabad.org/library/article_cdo/aid/98/jewish/Tefillin.htm",
            }
        ]
    if url == WRONG_MEZUZAH and "tzitzit" in iid:
        item["links"] = [
            {
                "displayText": "Chabad — tzitzit",
                "url": "https://www.chabad.org/library/article_cdo/aid/100/jewish/Tzitzit.htm",
            }
        ]
    if "have_mezuzot" in iid:
        item["links"] = LINKS["mezuzah"]
    if iid == "100_daily_blessings":
        item["links"] = LINKS["brachot_100"]
    if iid in ("shabbat_candles", "kiddush_friday", "electronics_shabbat", "prepare_for_and_observe_shabbat_and_festivals"):
        if url.endswith("/shabbat") or url == "https://www.chabad.org/shabbat":
            item["links"] = LINKS["shabbat"]


def main() -> None:
    data = json.loads(CHECKLIST.read_text(encoding="utf-8"))
    existing_ids = {it["id"] for it in data["items"]}
    for item in SOCIAL_ITEMS:
        if item["id"] not in existing_ids:
            data["items"].append(item)
            existing_ids.add(item["id"])
    for item in data["items"]:
        fix_item_links(item)
        if "persistChecked" not in item:
            item["persistChecked"] = False
        if "hideOnShabbat" not in item:
            item["hideOnShabbat"] = False
        if "shabbatOnly" not in item:
            item["shabbatOnly"] = False
    data["version"] = 3
    text = json.dumps(data, ensure_ascii=False, indent=2)
    CHECKLIST.write_text(text, encoding="utf-8")
    DATA_COPY.write_text(text, encoding="utf-8")
    print(f"Updated {CHECKLIST} ({len(data['items'])} items)")


if __name__ == "__main__":
    main()
