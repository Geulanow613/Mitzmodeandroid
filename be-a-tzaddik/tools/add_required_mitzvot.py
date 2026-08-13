"""Append halachically central checklist items with verified Sefaria learn-more links."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CHECKLIST = ROOT / "shared/src/commonMain/composeResources/files/checklist-items.json"
DATA_COPY = ROOT / "data/checklist-items.json"

DEFAULTS = {
    "hideOnShabbat": False,
    "shabbatOnly": False,
    "persistChecked": False,
}

NEW_ITEMS = [
    # --- Eating & Blessings ---
    {
        "id": "netilat_yadayim_and_hamotzi_on_bread",
        "title": "Wash hands and say HaMotzi on bread",
        "section": "Eating & Blessings",
        "sortOrder": 15,
        "timeOfDay": "any",
        "required": True,
        "explanation": (
            "Before eating bread (about the size of an olive or more), pour water over each hand "
            "twice (or once per Ashkenaz custom for clean hands) and recite the blessing for "
            "washing hands. Then recite HaMotzi over the bread.\n\n"
            "This applies to meals with bread, not to crackers or breakfast cereal unless they "
            "are true bread according to halacha — ask your rabbi when unsure."
        ),
        "links": [
            {
                "displayText": "Shulchan Arukh — washing before bread",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.161",
            },
            {
                "displayText": "Shulchan Arukh — HaMotzi",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.167",
            },
        ],
    },
    {
        "id": "birkat_hamazon_after_bread_meal",
        "title": "Birkat HaMazon after a bread meal",
        "section": "Eating & Blessings",
        "sortOrder": 25,
        "timeOfDay": "any",
        "required": True,
        "explanation": (
            "After eating bread and becoming satisfied, men and women recite Grace After Meals "
            "(Birkat HaMazon). If you ate a kezayit of bread but are unsure about satisfaction, "
            "consult your rabbi — many still recite the blessing.\n\n"
            "At minimum, learn the text from a siddur and say it with understanding when possible."
        ),
        "links": [
            {
                "displayText": "Shulchan Arukh — Birkat HaMazon",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.184",
            }
        ],
    },
    {
        "id": "bracha_achrona_after_other_foods",
        "title": "Bracha acharona after other foods (when required)",
        "section": "Eating & Blessings",
        "sortOrder": 35,
        "timeOfDay": "any",
        "required": True,
        "explanation": (
            "After eating enough of certain foods (not bread), we thank G-d with the appropriate "
            "after-blessing: Borei Nefashot for many foods, or the longer Me'ein Shalosh for wine, "
            "grapes, grains, and fruits of Israel.\n\n"
            "If you are unsure whether you ate enough to require a bracha acharona, ask your rabbi."
        ),
        "links": [
            {
                "displayText": "Shulchan Arukh — after blessings",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.207",
            }
        ],
    },
    {
        "id": "blessing_on_wine_when_drinking",
        "title": "Blessing on wine or grape juice (HaGafen)",
        "section": "Eating & Blessings",
        "sortOrder": 45,
        "timeOfDay": "any",
        "required": False,
        "explanation": (
            "When drinking wine or grape juice (not mixed as a minor ingredient), recite the "
            "appropriate blessing — usually HaGafen. Kiddush on Shabbat and festivals uses this "
            "blessing as part of sanctifying the day."
        ),
        "links": [
            {
                "displayText": "Shulchan Arukh — wine blessings",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.272",
            }
        ],
    },
    {
        "id": "separate_meat_and_dairy",
        "title": "Keep meat and dairy separate",
        "section": "Eating & Blessings",
        "sortOrder": 50,
        "timeOfDay": "any",
        "required": True,
        "explanation": (
            "The Torah forbids cooking meat and milk together and eating such mixtures. Jewish "
            "practice extends this to separate dishes, utensils, and waiting times between meat "
            "and dairy meals.\n\n"
            "Use your app's kashrut timer settings for waiting times according to your minhag."
        ),
        "links": [
            {
                "displayText": "Rambam — meat and milk",
                "url": "https://www.sefaria.org/Mishneh_Torah,_Forbidden_Foods.9",
            }
        ],
    },
    # --- Ongoing mitzvot ---
    {
        "id": "give_tzedakah_charity_daily",
        "title": "Give tzedakah (charity) each day",
        "section": "Ongoing mitzvot",
        "sortOrder": 15,
        "timeOfDay": "any",
        "required": True,
        "explanation": (
            "Giving to those in need is a constant Torah obligation. Even a small daily coin or "
            "online donation fulfills the spirit of the mitzvah. The Rambam teaches that "
            "tzedakah brings redemption closer.\n\n"
            "Set aside a pushke (charity box) or regular automatic giving if that helps you remember."
        ),
        "links": [
            {
                "displayText": "Rambam — gifts to the poor",
                "url": "https://www.sefaria.org/Mishneh_Torah,_Gifts_to_the_Poor.7",
            },
            {
                "displayText": "Tzedakah (Devarim 15:8)",
                "url": "https://www.sefaria.org/Deuteronomy.15.8",
            },
        ],
    },
    {
        "id": "honor_father_and_mother",
        "title": "Honor your father and mother",
        "section": "Ongoing mitzvot",
        "sortOrder": 25,
        "timeOfDay": "any",
        "required": True,
        "explanation": (
            "Honoring parents is one of the Ten Commandments. It includes speaking respectfully, "
            "helping with their needs, and not causing them pain — even when you disagree.\n\n"
            "If a parent asks you to violate Torah law, you must decline respectfully. When parents "
            "are no longer alive, honor continues through prayer, learning, and good deeds in their memory."
        ),
        "links": [
            {
                "displayText": "Honor parents (Shemot 20:12)",
                "url": "https://www.sefaria.org/Exodus.20.12",
            },
            {
                "displayText": "Rambam — honoring parents",
                "url": "https://www.sefaria.org/Mishneh_Torah,_Mourning.6",
            },
        ],
    },
    {
        "id": "sanctify_gods_name_in_daily_life",
        "title": "Sanctify G-d's name in how you live",
        "section": "Ongoing mitzvot",
        "sortOrder": 35,
        "timeOfDay": "any",
        "required": True,
        "explanation": (
            "Jews are called to make G-d's holiness visible through honest, kind, and courageous "
            "behavior — and to avoid chillul Hashem (desecrating His name) through hypocrisy, "
            "public sin, or cruelty attributed to Judaism.\n\n"
            "Your conduct in business, online, and with neighbors is part of this mitzvah."
        ),
        "links": [
            {
                "displayText": "Rambam — sanctifying G-d's name",
                "url": "https://www.sefaria.org/Mishneh_Torah,_Foundations_of_the_Torah.5",
            }
        ],
    },
    {
        "id": "modest_dress_and_conduct_men",
        "title": "Modest dress and conduct (men)",
        "section": "Ongoing mitzvot",
        "sortOrder": 55,
        "timeOfDay": "any",
        "required": True,
        "gender": "male",
        "explanation": (
            "Modesty (tzniut) applies to men as well as women: dress and speech should not be "
            "deliberately provocative. Guard your eyes and thoughts; avoid inappropriate media.\n\n"
            "Standards vary by community — follow your rabbi's guidance."
        ),
        "links": [
            {
                "displayText": "Rambam — proper conduct",
                "url": "https://www.sefaria.org/Mishneh_Torah,_Human_Dispositions.5",
            }
        ],
    },
    {
        "id": "say_amen_to_others_blessings",
        "title": "Answer Amen to others' blessings",
        "section": "Ongoing mitzvot",
        "sortOrder": 75,
        "timeOfDay": "any",
        "required": False,
        "explanation": (
            "When someone recites a blessing aloud, answering Amen connects you to their praise of "
            "G-d. It is a small act that builds community at meals and in synagogue."
        ),
        "links": [
            {
                "displayText": "Shulchan Arukh — answering Amen",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.124",
            }
        ],
    },
    {
        "id": "check_mezuzot_periodically",
        "title": "Check mezuzot scrolls periodically",
        "section": "Ongoing mitzvot",
        "sortOrder": 45,
        "timeOfDay": "any",
        "required": False,
        "persistChecked": True,
        "explanation": (
            "Mezuzah scrolls can fade or crack over time. Many check them twice in seven years "
            "by a sofer (scribe). A kosher mezuzah on each required doorway is a biblical mitzvah."
        ),
        "links": [
            {
                "displayText": "Shulchan Arukh — mezuzah",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Yoreh_De'ah.285",
            },
            {
                "displayText": "Mezuzah (Devarim 6:9)",
                "url": "https://www.sefaria.org/Deuteronomy.6.9",
            },
        ],
    },
    # --- Between people (more) ---
    {
        "id": "judge_others_favorably",
        "title": "Judge others favorably (dan l'chaf zechut)",
        "section": "Between people",
        "sortOrder": 15,
        "timeOfDay": "any",
        "required": True,
        "explanation": (
            "When someone's action could be interpreted positively or negatively, Torah teaches us "
            "to give the benefit of the doubt when reasonably possible — as you would want others "
            "to do for you."
        ),
        "links": [
            {
                "displayText": "Rambam — judging favorably",
                "url": "https://www.sefaria.org/Mishneh_Torah,_Human_Dispositions.6.6",
            }
        ],
    },
    {
        "id": "help_someone_in_need",
        "title": "Help someone in need when you can",
        "section": "Between people",
        "sortOrder": 25,
        "timeOfDay": "any",
        "required": True,
        "explanation": (
            "Visiting the sick, comforting mourners, helping carry a load, or offering a meal are "
            "forms of gemilut chasadim (loving-kindness). If you cannot help yourself, facilitating "
            "help from others still counts."
        ),
        "links": [
            {
                "displayText": "Rambam — loving-kindness",
                "url": "https://www.sefaria.org/Mishneh_Torah,_Mourning.14",
            }
        ],
    },
    {
        "id": "return_lost_objects",
        "title": "Return lost objects when possible",
        "section": "Between people",
        "sortOrder": 35,
        "timeOfDay": "any",
        "required": False,
        "explanation": (
            "If you find a lost item that likely has an owner, you must try to return it. This "
            "builds trust in the community and fulfills hashavat aveidah."
        ),
        "links": [
            {
                "displayText": "Rambam — returning lost property",
                "url": "https://www.sefaria.org/Mishneh_Torah,_Robbery_and_Lost_Property.1",
            }
        ],
    },
    {
        "id": "pay_workers_and_debts_on_time",
        "title": "Pay workers and debts on time",
        "section": "Between people",
        "sortOrder": 45,
        "timeOfDay": "any",
        "required": True,
        "explanation": (
            "The Torah commands paying a day laborer promptly and honoring financial obligations. "
            "Delayed wages cause real hardship — treat others' time and trust with seriousness."
        ),
        "links": [
            {
                "displayText": "Pay wages on time (Devarim 24:15)",
                "url": "https://www.sefaria.org/Deuteronomy.24.15",
            },
            {
                "displayText": "Rambam — hiring and wages",
                "url": "https://www.sefaria.org/Mishneh_Torah,_Hiring.11",
            },
        ],
    },
    {
        "id": "hospitality_to_guests",
        "title": "Show hospitality to guests",
        "section": "Between people",
        "sortOrder": 65,
        "timeOfDay": "any",
        "required": False,
        "explanation": (
            "Inviting guests for Shabbat or meals, helping travelers, and making people feel welcome "
            "in your home continue Abraham's example of hachnasat orchim."
        ),
        "links": [
            {
                "displayText": "Avraham's hospitality (Bereishit 18)",
                "url": "https://www.sefaria.org/Genesis.18.1",
            }
        ],
    },
    {
        "id": "pursue_peace_in_relationships",
        "title": "Pursue peace in relationships",
        "section": "Between people",
        "sortOrder": 70,
        "timeOfDay": "any",
        "required": True,
        "explanation": (
            "Be a rodef shalom — seek peace between spouses, family, friends, and community. "
            "Sometimes that means apologizing first, even when you feel partly right."
        ),
        "links": [
            {
                "displayText": "Rambam — peace",
                "url": "https://www.sefaria.org/Mishneh_Torah,_Human_Dispositions.6.4",
            }
        ],
    },
    # --- Important Lifestyle ---
    {
        "id": "separate_challah_from_dough",
        "title": "Separate challah from dough (hafrashat challah)",
        "section": "Important Lifestyle Mitzvot",
        "sortOrder": 15,
        "timeOfDay": "any",
        "required": False,
        "explanation": (
            "When baking a sufficient amount of dough (about 1.2 kg / 2.6 lb of flour — confirm "
            "with your rabbi), a portion is separated and declared with a blessing. In Israel it "
            "is burned; outside Israel it is often discarded respectfully after the blessing.\n\n"
            "Many women make this blessing when baking challah for Shabbat."
        ),
        "links": [
            {
                "displayText": "Shulchan Arukh — challah separation",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Yoreh_De'ah.322",
            }
        ],
    },
    {
        "id": "shalom_bayit_if_married",
        "title": "Work for shalom bayit (peace in the home)",
        "section": "Important Lifestyle Mitzvot",
        "sortOrder": 25,
        "timeOfDay": "any",
        "required": True,
        "married": True,
        "explanation": (
            "Peace between husband and wife is a value the Talmud compares to G-d's name. Speak "
            "gently, avoid public quarrels, and seek rabbinic counsel when conflicts persist."
        ),
        "links": [
            {
                "displayText": "Shalom bayit (Vayikra 19:18)",
                "url": "https://www.sefaria.org/Leviticus.19.18",
            }
        ],
    },
    {
        "id": "educate_children_in_torah",
        "title": "Educate your children in Torah and mitzvot",
        "section": "Important Lifestyle Mitzvot",
        "sortOrder": 35,
        "timeOfDay": "any",
        "required": True,
        "explanation": (
            "Parents are obligated to teach children Torah, mitzvot, and moral behavior — both by "
            "instruction and example. This includes Shabbat, blessings, honest speech, and age-appropriate "
            "halacha.\n\n"
            "If you do not yet have children, prepare by learning what you will need to teach."
        ),
        "links": [
            {
                "displayText": "Teach your children (Devarim 6:7)",
                "url": "https://www.sefaria.org/Deuteronomy.6.7",
            },
            {
                "displayText": "Rambam — teaching children",
                "url": "https://www.sefaria.org/Mishneh_Torah,_Torah_Study.1.6",
            },
        ],
    },
    {
        "id": "brit_milah_for_sons",
        "title": "Brit milah for newborn sons (when applicable)",
        "section": "Important Lifestyle Mitzvot",
        "sortOrder": 45,
        "timeOfDay": "any",
        "required": False,
        "persistChecked": True,
        "explanation": (
            "Jewish boys are circumcised on the eighth day (unless health requires delay). This "
            "covenant mitzvah is performed by a trained mohel. Plan with your rabbi before birth."
        ),
        "links": [
            {
                "displayText": "Brit milah (Bereishit 17:12)",
                "url": "https://www.sefaria.org/Genesis.17.12",
            },
            {
                "displayText": "Rambam — circumcision",
                "url": "https://www.sefaria.org/Mishneh_Torah,_Circumcision.1",
            },
        ],
    },
    # --- Shabbat & Festivals ---
    {
        "id": "havdalah_after_shabbat",
        "title": "Havdalah after Shabbat (and Yom Tov when applicable)",
        "section": "Shabbat & Festivals",
        "sortOrder": 50,
        "timeOfDay": "night",
        "required": True,
        "shabbatOnly": False,
        "explanation": (
            "Havdalah separates Shabbat (or Yom Tov) from the weekday using wine, spices, and flame "
            "(per custom). It is recited after nightfall when three stars are visible — follow "
            "your community's timing."
        ),
        "links": [
            {
                "displayText": "Shulchan Arukh — Havdalah",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.296",
            }
        ],
    },
    {
        "id": "three_meals_on_shabbat",
        "title": "Eat Shabbat meals (at least two with bread)",
        "section": "Shabbat & Festivals",
        "sortOrder": 55,
        "timeOfDay": "any",
        "required": True,
        "shabbatOnly": True,
        "explanation": (
            "Shabbat is celebrated with festive meals including bread — traditionally three meals "
            "(Friday night, Shabbat day, and late afternoon). Each meal is an opportunity for Torah "
            "words, singing, and family connection."
        ),
        "links": [
            {
                "displayText": "Shulchan Arukh — Shabbat meals",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.291",
            }
        ],
    },
    {
        "id": "oneg_shabbat_torah_and_rest",
        "title": "Delight in Shabbat — Torah, rest, and joy",
        "section": "Shabbat & Festivals",
        "sortOrder": 60,
        "timeOfDay": "any",
        "required": False,
        "shabbatOnly": True,
        "explanation": (
            "Oneg Shabbat means enjoying the day through Torah study, rest, good food, and wholesome "
            "pleasure — not weekday work or worry. Dress nicely and set aside time for what elevates "
            "your soul."
        ),
        "links": [
            {
                "displayText": "Shulchan Arukh — honoring Shabbat",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.242",
            }
        ],
    },
    # --- Seasonal (shown when calendar activeSeasons match) ---
    {
        "id": "fast_yom_kippur",
        "title": "Fast on Yom Kippur",
        "section": "Seasonal",
        "sortOrder": 10,
        "timeOfDay": "any",
        "required": True,
        "seasons": ["yom_kippur"],
        "explanation": (
            "Yom Kippur is a 25-hour fast from food and drink (and other restrictions) for adults "
            "who are healthy enough. It is the day of atonement — spent in prayer and repentance."
        ),
        "links": [
            {
                "displayText": "Yom Kippur (Vayikra 16)",
                "url": "https://www.sefaria.org/Leviticus.16.29",
            }
        ],
    },
    {
        "id": "hear_shofar_rosh_hashana",
        "title": "Hear the shofar on Rosh Hashanah",
        "section": "Seasonal",
        "sortOrder": 20,
        "timeOfDay": "day",
        "required": True,
        "seasons": ["rosh_hashana"],
        "explanation": (
            "Hearing the shofar blasts on Rosh Hashanah is a central mitzvah of the day, usually "
            "fulfilled in synagogue. If you cannot attend, ask your rabbi about hearing blasts nearby."
        ),
        "links": [
            {
                "displayText": "Shulchan Arukh — shofar",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.585",
            }
        ],
    },
    {
        "id": "dwell_in_sukkah",
        "title": "Dwell in the sukkah on Sukkot",
        "section": "Seasonal",
        "sortOrder": 30,
        "timeOfDay": "any",
        "required": True,
        "seasons": ["sukkot"],
        "explanation": (
            "Eat (and according to many, sleep) in a kosher sukkah during Sukkot. The sukkah "
            "reminds us of G-d's protection in the desert and dependence on Him."
        ),
        "links": [
            {
                "displayText": "Shulchan Arukh — sukkah",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.625",
            }
        ],
    },
    {
        "id": "lulav_and_etrog_sukkot",
        "title": "Wave lulav and etrog on Sukkot",
        "section": "Seasonal",
        "sortOrder": 40,
        "timeOfDay": "day",
        "required": True,
        "seasons": ["sukkot"],
        "explanation": (
            "On each day of Sukkot (except Shabbat per most customs), we take the four species — "
            "lulav, etrog, hadas, and aravah — and wave them with a blessing."
        ),
        "links": [
            {
                "displayText": "Shulchan Arukh — lulav",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.645",
            }
        ],
    },
    {
        "id": "eat_matzah_pesach",
        "title": "Eat matzah at the Pesach Seder",
        "section": "Seasonal",
        "sortOrder": 50,
        "timeOfDay": "night",
        "required": True,
        "seasons": ["pesach"],
        "explanation": (
            "On the first two nights of Pesach (in Israel, the first night), we eat a kezayit of "
            "matzah at the Seder after saying the blessings. Prepare a Seder with hagaddah, wine, "
            "and maror per halacha."
        ),
        "links": [
            {
                "displayText": "Shulchan Arukh — matzah",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.475",
            }
        ],
    },
    {
        "id": "no_chametz_pesach",
        "title": "Do not eat or own chametz on Pesach",
        "section": "Pesach prep",
        "sortOrder": 20,
        "timeOfDay": "any",
        "required": True,
        "seasons": ["pesach", "erev_pesach"],
        "explanation": (
            "During Pesach, chametz (leaven from the five grains) is forbidden to eat or benefit from. "
            "Homes are cleaned and sold or destroyed before the holiday. Check product labels for "
            "kosher-for-Passover certification."
        ),
        "links": [
            {
                "displayText": "Shulchan Arukh — chametz on Pesach",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.431",
            }
        ],
    },
    {
        "id": "torah_study_shavuot",
        "title": "Torah study on Shavuot night",
        "section": "Seasonal",
        "sortOrder": 60,
        "timeOfDay": "night",
        "required": False,
        "seasons": ["shavuot"],
        "explanation": (
            "Many stay up learning Torah on Shavuot night to celebrate receiving the Torah. "
            "Even an hour of study fulfills the spirit of the custom."
        ),
        "links": [
            {
                "displayText": "Shavuot — receiving the Torah",
                "url": "https://www.sefaria.org/Exodus.19.1",
            }
        ],
    },
    # --- Daily Prayer (women) ---
    {
        "id": "morning_blessings_women",
        "title": "Morning blessings (Birchot HaShachar)",
        "section": "Daily Prayer",
        "sortOrder": 10,
        "timeOfDay": "day",
        "required": True,
        "gender": "female",
        "explanation": (
            "Women recite the morning blessings to thank G-d for the gifts of each day — the soul, "
            "body, clothing, and strength. Many say the full series from the siddur; ask your rabbi "
            "if your minhag differs."
        ),
        "links": [
            {
                "displayText": "Shulchan Arukh — morning blessings",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.46",
            }
        ],
    },
    {
        "id": "evening_shema_women",
        "title": "Evening Shema",
        "section": "Daily Prayer",
        "sortOrder": 20,
        "timeOfDay": "night",
        "required": True,
        "gender": "female",
        "explanation": (
            "Many authorities hold that women are obligated in the evening Shema (or at least its "
            "first paragraph) like men. Recite after nightfall with the blessings in your siddur."
        ),
        "links": [
            {
                "displayText": "Shulchan Arukh — Shema at night",
                "url": "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.235",
            }
        ],
    },
]

# Mark core male prayers as required (halachically obligatory)
REQUIRED_TRUE = {
    "shemoneh_esrei_tachanun",
    "mincha_shemoneh_esrei_tachanun",
}

MEZUZAH_LINKS = [
    {
        "displayText": "Shulchan Arukh — mezuzah",
        "url": "https://www.sefaria.org/Shulchan_Arukh,_Yoreh_De'ah.285",
    },
    {
        "displayText": "Mezuzah (Devarim 6:9)",
        "url": "https://www.sefaria.org/Deuteronomy.6.9",
    },
]


def main() -> None:
    data = json.loads(CHECKLIST.read_text(encoding="utf-8"))
    existing = {it["id"] for it in data["items"]}
    added = 0
    for item in NEW_ITEMS:
        if item["id"] in existing:
            continue
        full = {**DEFAULTS, **item}
        data["items"].append(full)
        existing.add(item["id"])
        added += 1

    for item in data["items"]:
        if item["id"] in REQUIRED_TRUE:
            item["required"] = True
        if item["id"] == "have_mezuzot_on_your_doorposts" and not item.get("links"):
            item["links"] = MEZUZAH_LINKS

    data["version"] = 4
    text = json.dumps(data, ensure_ascii=False, indent=2)
    CHECKLIST.write_text(text, encoding="utf-8")
    DATA_COPY.write_text(text, encoding="utf-8")
    print(f"Added {added} items; total {len(data['items'])}; version {data['version']}")


if __name__ == "__main__":
    main()
