"""Resolve English catalog keys for checklist explainer translations."""
from __future__ import annotations

import json
from pathlib import Path

from _sync_explainer_catalog_keys import NINE_DAYS_SHARED, THREE_WEEKS_INTRO

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"


def catalog_strings() -> list[str]:
    return json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]


def resolve_keys(strings: list[str] | None = None) -> dict[str, str]:
    """Map logical names to exact English catalog keys."""
    strings = strings or catalog_strings()
    halacha = "Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10"

    forgot_sh = next(
        s for s in strings
        if s.startswith("If you forgot:\n") and "repeat only the Shacharit Amidah" in s
    )
    forgot_min = next(
        s for s in strings
        if s.startswith("If you forgot:\n") and "repeat only the Mincha Amidah" in s
    )
    forgot_maariv = next(s for s in strings if s.startswith("If you forgot at Maariv on Rosh Chodesh:"))

    return {
        "halacha": halacha,
        "omer": next(s for s in strings if s.startswith("Sefirat HaOmer links")),
        "chanukah": next(s for s in strings if s.startswith("Chanukah night $day")),
        "ilanot": next(s for s in strings if s.startswith("Birkat Ha'Ilanot (ב")),
        "hachamah": next(s for s in strings if s.startswith("Birkat Hachamah (ב")),
        "kiddush": next(s for s in strings if s.startswith("Kiddush Levana (Sanctification")),
        "tw_intro": THREE_WEEKS_INTRO,
        "nd_shared": NINE_DAYS_SHARED,
        "tw_ash": next(s for s in strings if "Ashkenazi custom observes a longer" in s),
        "tw_sep": next(s for s in strings if "Sephardic and Edot HaMizrach" in s),
        "tw_ch": next(s for s in strings if "Chabad follows strict Ashkenazi mourning" in s),
        "tw_ash_full": next(
            s for s in strings
            if s.startswith("The Three Weeks") and "Ashkenazi custom observes a longer" in s
        ),
        "tw_sep_full": next(
            s for s in strings
            if s.startswith("The Three Weeks") and "Sephardic and Edot HaMizrach" in s
        ),
        "tw_ch_full": next(
            s for s in strings
            if s.startswith("The Three Weeks") and "Chabad follows strict Ashkenazi mourning" in s
        ),
        "nd_ash": next(s for s in strings if s.startswith("The Nine Days (from 1 Av")),
        "nd_sep": next(s for s in strings if "shavuah she'chal bo" in s and "Sephardic communities" in s),
        "nd_ch": next(s for s in strings if "Chabad practice" in s and "Nine Days" in s),
        "when_south": next(s for s in strings if s.startswith("When (Southern Hemisphere")),
        "when_north": next(s for s in strings if s.startswith("When (Northern Hemisphere")),
        "when_unknown": next(s for s in strings if s.startswith("When:\nIn the Northern Hemisphere")),
        "wait_ash": "Recited once a month when the moon is visible, usually beginning on the 3rd night of the Hebrew month (Ashkenaz / Chabad custom; Peninei Halakha 05-01-18).",
        "wait_sef": "Recited once a month when the moon is visible, usually beginning on the 7th night of the Hebrew month (Shulchan Arukh O.C. 426:4; Peninei Halakha 05-01-18).",
        "wait_edot": "Recited once a month when the moon is visible. The majority of Sefardim wait until the 7th of the month (Shulchan Arukh O.C. 426:4). Moroccan and some other North African kehillot begin after 3 days (Peninei Halakha 05-01-18) — follow your community.",
        "brachot_first": "First night: say all three brachot including Shehecheyanu.",
        "brachot_other": "Tonight: two brachot (no Shehecheyanu unless first time lighting this year).",
        "forgot_sh": forgot_sh,
        "forgot_min": forgot_min,
        "forgot_maariv": forgot_maariv,
        "arba_men": next(s for s in strings if s.startswith("Arba Minim") and "Men — Torah obligation" in s),
        "arba_fem": next(s for s in strings if s.startswith("Arba Minim") and "Women — recommended mitzvah" in s),
        "mechirat": next(s for s in strings if s.startswith("Mechirat chametz") and "$scheduleLeadIn" in s),
        "mechirat_urgency": next(
            s for s in strings
            if s.startswith("Mechirat chametz (מְכִירַת") and s.endswith("$urgency") and "$schedule" not in s
        ),
        "mechirat_short": next(s for s in strings if s.startswith("Mechirat chametz is selling")),
        "seder": next(s for s in strings if s.startswith("$intro\n\n$sederNights")),
        "bedikat": next(s for s in strings if s.startswith("Bedikat chametz (בְּדִיקַת") and "$scheduleLeadIn" in s),
        "bedikat_no_sched": next(
            s for s in strings if s.startswith("Bedikat chametz (בְּדִיקַת") and s.endswith("to search.")
        ),
        "bedikat_short": next(s for s in strings if s.startswith("Bedikat chametz is the formal search")),
        "biur": next(s for s in strings if s.startswith("Biur chametz") and "$morningNote" in s),
        "on_shabbat_body": next(s for s in strings if "When Erev Pesach falls on Shabbat, the entire preparatory timeline" in s),
        "fri_shabbat_body": next(
            s for s in strings if "first day of Pesach is Shabbat (15 Nisan)" in s and "Bedikat chametz: Thursday night" in s
        ),
        "fri_13": next(s for s in strings if "Today (Friday, 13 Nisan): biur chametz" in s),
        "days_israel": "In Israel: lulav all 7 days; first day is the primary Torah obligation for men.",
        "days_diaspora": "In the Diaspora: men's primary Torah obligation is the first day of Sukkot (15 Tishrei); the mitzvah continues rabbinically on the second day of Yom Tov and Chol HaMoed.",
        "lead_shabbat": "Today is Shabbat — Erev Pesach. The steps below should already be done; use this as a checklist.",
        "lead_friday": "Today is Erev Pesach (Friday). The calendar below should already be in motion.",
        "lead_bed_thu_tomorrow": "Read this before bedikat chametz — tomorrow (Thursday) night after tzeit is the search, not the usual Erev Pesach night.",
        "lead_bed_thu_tonight": "Read this before bedikat chametz — tonight (Thursday night after tzeit) is bedikat, not tomorrow.",
        "lead_bed_fri_tomorrow": "Read this before bedikat chametz — tomorrow (Thursday) night after tzeit is bedikat, not Friday night.",
        "lead_bed_fri_tonight": "Read this before bedikat chametz — tonight (Thursday night after tzeit) is bedikat. Tomorrow is Erev Pesach (Friday): Taanit Bechorot, biur, and mechirat before Shabbat.",
        "omer_ash": "Many Ashkenazim count after Maariv.",
        "omer_sep": "Many Sephardim count after Maariv.",
        "omer_edot": "Many Edot HaMizrach kehillot count after Maariv.",
        "omer_chabad": "Many in Chabad count after Maariv (Tehillat Hashem).",
    }
