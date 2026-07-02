"""Catalog keys for Sefirat HaOmer explainer template args (OmerCountText.kt)."""

from __future__ import annotations


def weeks_and_days(day: int) -> str:
    weeks = day // 7
    rem = day % 7
    if weeks == 0:
        return "1 day" if rem == 1 else f"{rem} days"
    if rem == 0:
        return "1 week" if weeks == 1 else f"{weeks} weeks"
    if weeks == 1 and rem == 1:
        return "1 week and 1 day"
    if weeks == 1:
        return f"1 week and {rem} days"
    if rem == 1:
        return f"{weeks} weeks and 1 day"
    return f"{weeks} weeks and {rem} days"


def omer_day_summary(day: int) -> str:
    if day < 7:
        word = "day" if day == 1 else "days"
        return f"{day} {word} of the Omer"
    return f"{day} days, which is {weeks_and_days(day)} of the Omer"


def omer_day_summary_he(day: int, suffix: str = "לעומר") -> str:
    if day < 7:
        return f"יום אחד {suffix}" if day == 1 else f"{day} ימים {suffix}"
    weeks = day // 7
    rem = day % 7
    if rem == 0:
        if weeks == 1:
            w = "שבוע אחד"
        elif weeks == 2:
            w = "שבועיים"
        else:
            w = f"{weeks} שבועות"
        return f"{day} ימים, שהם {w} {suffix}"
    if weeks == 1 and rem == 1:
        wd = "שבוע ויום אחד"
    elif weeks == 1:
        wd = f"שבוע ו-{rem} ימים"
    elif rem == 1:
        wd = f"{weeks} שבועות ויום אחד"
    else:
        wd = f"{weeks} שבועות ו-{rem} ימים"
    return f"{day} ימים, שהם {wd} {suffix}"


OMER_TODAY_SUMMARIES = [omer_day_summary(d) for d in range(1, 50)]
OMER_SPEECH_PHRASES = [f"Today is {s}." for s in OMER_TODAY_SUMMARIES]

OMER_NEXT_NIGHT_LINE = "\n• $tomorrowNight night: you will count $nextDaySummary."
OMER_TIME_PART = " at $time"

OMER_EXPLANATION_TEMPLATE = (
    "Sefirat HaOmer links Pesach to Shavuot — counting each day from the Exodus toward receiving the Torah.\n\n"
    "Today in the Omer: $todaySummary (day $day of 49).\n\n"
    "Tonight's count:\n"
    "• $tonight night — count $tonightSummary after nightfall$timePart.\n"
    "$nextNightLine\n\n"
    "How to count:\n"
    "• Stand and recite the blessing before counting if you are still saying it with a blessing "
    "(if you missed a day, ask your rabbi before continuing with a bracha).\n"
    '• Say: "$speechPhrase"\n'
    "• Count after nightfall (tzeit); complete before dawn. If you forgot at night, count the next day during the "
    "daytime without a bracha. If you do this before sunset, you can continue counting on subsequent nights with a "
    "bracha. You only lose the blessing permanently if you miss an entire 24-hour cycle (both night and the "
    "following day) — ask your rav.\n\n"
    "$nusachWhen"
)

NUSACH_WHEN = {
    "Many in Chabad count after Maariv (Tehillat Hashem).": "chabad",
    "Many Sephardim count after Maariv.": "sefard",
    "Many Edot HaMizrach kehillot count after Maariv.": "edot",
    "Many Ashkenazim count after Maariv.": "ashkenaz",
}

OMER_ARG_CATALOG_KEYS = [
    OMER_EXPLANATION_TEMPLATE,
    *OMER_TODAY_SUMMARIES,
    *OMER_SPEECH_PHRASES,
    OMER_NEXT_NIGHT_LINE,
    OMER_TIME_PART,
    *NUSACH_WHEN.keys(),
]
