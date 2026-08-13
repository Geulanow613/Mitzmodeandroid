#!/usr/bin/env python3
"""Fix broken explainer catalog keys that contain Kotlin source instead of placeholders."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG_PATH = ROOT / "data" / "translation-catalog" / "strings.json"

from _mourning_period_translations import (
    NINE_DAYS_SHARED_EN as NINE_DAYS_SHARED,
    THREE_WEEKS_INTRO_EN as THREE_WEEKS_INTRO,
)

YAALEH_HALACHA = "Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10"

CHANUKAH_BRACHOT_OLD = (
    '${if (day == 1) "First night: say all three brachot including Shehecheyanu." '
    'else "Tonight: two brachot (no Shehecheyanu unless first time lighting this year)."}'
)
CHANUKAH_BRACHOT_NEW = "$brachotNote"

OMER_SPEECH_OLD = '"${omerCountSpeechPhrase(day)}"'
OMER_SPEECH_NEW = '"$speechPhrase"'

KOTLIN_ARTIFACT = re.compile(r"\$\{[^}]+\}")


def forgot_amidah(service: str) -> str:
    return f"""If you forgot:
• Still in Retzei (before concluding the blessing) — insert Yaaleh V'yavo in its place and continue ({YAALEH_HALACHA}).
• After concluding Retzei — return to the beginning of Retzei, insert Yaaleh V'yavo, and complete the remaining blessings ({YAALEH_HALACHA}).
• Finished the entire Amidah (after the final Yihiyu L'ratzon) — repeat only the {service} Amidah (Shemoneh Esrei), never the full service, even if you already davened Musaf, Maariv, or anything else afterward ({YAALEH_HALACHA})."""


FORGOT_MAARIV = f"""If you forgot at Maariv on Rosh Chodesh:
• Still in Retzei before God's name at the conclusion — insert Yaaleh V'yavo there and continue ({YAALEH_HALACHA}).
• Once you finished Retzei, or after the entire Amidah — do not go back and do not repeat. Beit Din sanctified the new month by day, not at night (Berachot 30b; {YAALEH_HALACHA}). Continue davening."""

YAALEH_EXPLAINERS: dict[str, str] = {
    "shacharit": f"""Add Yaaleh V'yavo in the Shacharit Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).

{forgot_amidah("Shacharit")}

Also add Yaaleh V'yavo in bentching if you eat bread today.""",
    "shacharit_female": f"""If you recite the Shacharit Amidah on Rosh Chodesh, add Yaaleh V'yavo in the blessing Retzei (Avodah).

{forgot_amidah("Shacharit")}

If you say Birkat Hamazon when you eat bread today, add Yaaleh V'yavo there too.""",
    "mincha": f"""Add Yaaleh V'yavo in the Mincha Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).

{forgot_amidah("Mincha")}""",
    "mincha_female": f"""If you recite the Mincha Amidah on Rosh Chodesh, add Yaaleh V'yavo in the blessing Retzei (Avodah).

{forgot_amidah("Mincha")}

If you say Birkat Hamazon when you eat bread today, add Yaaleh V'yavo there too.""",
    "maariv": f"""Add Yaaleh V'yavo in the Maariv Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).

{FORGOT_MAARIV}

Also add Yaaleh V'yavo in bentching if you eat bread tonight.""",
    "maariv_female": f"""If you recite the Maariv Amidah on Rosh Chodesh, add Yaaleh V'yavo in the blessing Retzei (Avodah).

{FORGOT_MAARIV}

If you say Birkat Hamazon when you eat bread tonight, add Yaaleh V'yavo there too.""",
}

# Kotlin template keys from ExplainerTemplateSupport (must stay in sync).
ARBA_MINIM_MEN = """Arba Minim (ארבעה מינים) — the Four Species — are taken each day of Sukkot (except Shabbat).

The four:
• Lulav — closed palm branch (at least 3 tefachim)
• Etrog — citron, beautiful and mostly intact (pitom if present should be intact)
• Hadasim — three myrtle branches
• Aravot — two willow branches

How to observe (everyone):
• Assemble the lulav: Bind the lulav, hadassim, and aravot together (per your hoshanah holder / koisan). Ensure the central spine of the lulav extends upward at least one handbreadth (tefach — around 4 inches) higher than the tops of the myrtle and willow branches (Shulchan Arukh O.C. 650:1).
• Check kashrut before Yom Tov — especially etrog and that leaves are fresh enough.
• When: Daytime; many do it before Shacharit at home or in shul. Not on Shabbat.
• $daysNote

If a species is missing or invalid, ask your rabbi — there are limited substitutions in pressing cases.

Men — Torah obligation:
• The first day of Sukkot worldwide (15 Tishrei) is the Torah-level day for men. In the Diaspora, the mitzvah continues rabbinically on the second day of Yom Tov and Chol HaMoed.
• Bracha: Men say Al netilat lulav every day when taking the lulav (except Shabbat). Shehecheyanu is on the first day only.
• $menWave

The ownership rule (lakhem — u'lekachtem lakhem):
• In Israel: the strict requirement to fully own your lulav and etrog set applies on the first day of Sukkot (15 Tishrei; Shulchan Arukh O.C. 658:3). If you do not own a set, ask the owner for matana al menat lehachzir (gift on condition of return) before you wave.
• In the Diaspora: because the second day of Yom Tov is kept as a safek de'oraita, the same lakhem ownership requirement applies on both Day 1 and Day 2 — you cannot simply borrow a synagogue set; it must be given to you as matana al menat lehachzir.
• In the Diaspora from the third day onward (the first day of Chol HaMoed): you may borrow a shared or synagogue set without a formal gift — the rabbinic continuation no longer carries the Torah ownership requirement (Peninei Halakha, Laws of Sukkot 13:13).
• In Israel on Chol HaMoed: borrowing without a gift is permitted — ownership was required only on the first day."""

ARBA_MINIM_FEMALE = """Arba Minim (ארבעה מינים) — the Four Species — are taken each day of Sukkot (except Shabbat).

The four:
• Lulav — closed palm branch (at least 3 tefachim)
• Etrog — citron, beautiful and mostly intact (pitom if present should be intact)
• Hadasim — three myrtle branches
• Aravot — two willow branches

How to observe (everyone):
• Assemble the lulav: Bind the lulav, hadassim, and aravot together (per your hoshanah holder / koisan). Ensure the central spine of the lulav extends upward at least one handbreadth (tefach — around 4 inches) higher than the tops of the myrtle and willow branches (Shulchan Arukh O.C. 650:1).
• Check kashrut before Yom Tov — especially etrog and that leaves are fresh enough.
• When: Daytime; many do it before Shacharit at home or in shul. Not on Shabbat.
• $daysNote

If a species is missing or invalid, ask your rabbi — there are limited substitutions in pressing cases.

Women — recommended mitzvah (not obligatory):
• Women are exempt from this time-bound commandment, but participating is highly popular in both Ashkenazi and Sephardi communities. This checklist marks it as recommended — not a strict daily obligation like men's first-day mitzvah.

Do women need their own set?
• No. Most women use a shared family set (usually owned by a husband or father) or a synagogue set.

The ownership rule (for men):
• In Israel: on the first day of Sukkot only, a man must own the set he waves (matana al menat lehachzir if needed). In the Diaspora, lakhem applies on Day 1 and Day 2; borrowing without a gift is permitted from the first day of Chol HaMoed (Day 3) onward.

Bracha:
• $brachaLine

How to wave:
• $waveLine
• Hold etrog in left, lulav (with hadassim and aravot) in right — or together per your siddur.

Each day of Sukkot (except Shabbat) you may fulfill this recommended mitzvah again."""

MECHIRAT_TEMPLATE = """Mechirat chametz (מְכִירַת חָמֵץ) — selling your chametz to a non-Jew through your rabbi before Pesach — lets you keep chametz products locked away without owning them during Pesach (Shulchan Arukh O.C. 448).

Why:
• On Pesach, owning chametz is forbidden (bal yera'eh / bal yimatzei). Selling transfers ownership so sealed chametz in your home does not belong to you during the festival.

How to do it:
• Sign or authorize a sale with your rabbi or community (online forms are common). Deadline warning: While the sale takes effect on Erev Pesach morning, you must authorize your rabbi to sell your chametz several days in advance — most rabbis stop accepting sale forms by the night before Erev Pesach. The rabbi needs time to organize contracts and complete the kinyan (legal transfer) before the halachic deadline.
• Mark cabinets or rooms included in the sale; keep sold chametz separate from what you will burn or discard.
• Do not eat or use sold chametz after the sale takes effect — it belongs to the buyer until buy-back after Pesach.
• Dishes used with chametz: Many people include the chametz residue and absorbed flavor within their year-round dishes and pots in the sale, locking them away securely. The physical dishes themselves are not sold, avoiding the halachic requirement to re-immerse them in a Mikveh (tevilat kelim) after Pesach (Shulchan Arukh Y.D. 120). Rabbis structure the contract accordingly — follow your rabbi's sale form.
• Store the contract or confirmation; many communities sell through a central rabbi (e.g. local kashrut council).

After Pesach: chametz is repurchased per the terms of the sale — follow your rabbi's instructions on when you may use it again.
$urgency
$scheduleLeadIn$scheduleBody$scheduleYomTov"""

SEDER_PREP_TEMPLATE = """$intro

$sederNights
$secondSederHachana
$omerTrigger

Set up before Yom Tov:
• Matzah — shmurah matzah for motzi/matza mitzvah (three matzot on the plate per custom)
• Maror — romaine, horseradish, or bitter herbs per your minhag
• Four cups of wine per participant (grape juice is widely used if needed — ask your rav; not the same debate as Chol HaMoed wine)
• Haggadah for each person (or shared)
• Seder plate: zeroa (shankbone), beitzah (egg), karpas, charoset, maror, chazeret
• Seder plate prep: You should ideally roast your zeroa (shankbone) on Erev Pesach day before sunset. Because the shankbone is not eaten on Seder night, roasting it after the holiday begins violates Yom Tov cooking laws. The egg (beitzah), however, is traditionally eaten during the meal, so it may legally be boiled or roasted on Yom Tov night if needed.
• Reclining (hasebha): Recline to the left when drinking the four cups and eating matzah, korech, and afikoman — do not recline while eating maror or chazeret (they symbolize slavery).
• Festive table; candles for Yom Tov

Kitchen:
• Only kosher-for-Passover food and utensils from this point
• Warm food on a blech or pre-set timer for Yom Tov meals

At the Seder: follow your Haggadah step by step — Kiddush, the order of the night, brachot, and the four cups are all laid out there. $sederNightCount
$scheduleLeadIn$scheduleBody$scheduleYomTov"""

BEDIKAT_TEMPLATE = """Bedikat chametz (בְּדִיקַת חָמֵץ) — the formal search for chametz — is a rabbinic mitzvah on the night **before** Erev Pesach day (after tzeit when the Hebrew date becomes 14 Nisan).

$whenLine

How to search:
• Use a candle (or flashlight per many poskim), a wooden spoon, and a feather (or paper bag) to gather crumbs.
• Search every room where chametz may have been brought during the year — especially kitchen, dining areas, living room, car, office, children's bags, and coat pockets.
• Check under furniture, cushions, car seats, and appliances where crumbs collect.
• Place ten pieces of bread (wrapped) in rooms before searching if your custom includes finding known pieces (optional minhag). If you hide them, write down every location and verify all 10 are recovered — a missed piece means known chametz remains in your home.

After the search:
• Recite the blessing Al bi'ur chametz and the Kol chamira nullification (bitul) — many siddurim print the text.
• Text difference: Use the night version of Kol Chamira from your siddur. It nullifies only chametz you have not seen and do not know about — because you may still legally own chametz for breakfast tomorrow morning.
• When Erev Pesach is Shabbat, this first bitul is Thursday night after bedikat; the final Kol Chamira is on Shabbat morning before the end of the 5th halachic hour — not at Friday's burning.
• Gather found chametz in a bag to destroy the next morning at biur chametz.
• Eating restrictions: You may not eat a meal or start work after nightfall until you complete the search. Once the search is finished, you may eat normally. Tomorrow morning is biur chametz — avoid a heavy meal from midday (chatzos) onward to preserve your appetite for the Seder.

If you are traveling or staying elsewhere, your host or rabbi can guide which rooms you are responsible to search.
$scheduleLeadIn$scheduleBody$scheduleYomTov"""

BIUR_TEMPLATE = """Biur chametz (בִּעוּר חָמֵץ) — destroying chametz — completes the mitzvah of removing leaven before Pesach.

$morningNote
• Burn or destroy all chametz found last night and any remaining chametz you are not selling.
• Many burn chametz in a safe outdoor fire; flushing crumbs in the toilet or similar is acceptable for small amounts per many poskim — ask your rav.
• $zmanNote
• Timeline guardrail: $timelineGuardrail
• Text difference: Use the morning version of Kol Chamira from your siddur — it is structurally different from the night text and completely disowns ALL chametz in your possession, whether you have seen it or not and whether you have destroyed it or not.
• Recite the final Kol Chamira immediately after destruction while still before that deadline$kolChamiraShabbatNote

Mechirat chametz:
• Any chametz included in the rabbi's sale should already be sealed and not eaten — only unsold chametz is burned.

After biur:
• Eat only kosher-for-Passover food until Pesach ends.
• Firstborns: Taanit Bechorot earlier per schedule; Seder $sederTiming.
$scheduleLeadIn$scheduleBody$scheduleYomTov"""

EREV_PESACH_ON_SHABBAT_SCHEDULE_BODY = """

This year, Erev Pesach is on Shabbat (14 Nisan). When Erev Pesach falls on Shabbat, the entire preparatory timeline shifts early (Peninei Halakha ch. 14):

• Taanit Bechorot: Thursday (12 Nisan) — moved early; not on Shabbat or Friday. Ashkenazim fast or attend a siyum. Many Sephardic authorities rule the moved fast is nullified entirely; many still attend a siyum out of custom (ask your rav).
• Bedikat chametz: Thursday night (night of 13 Nisan, after tzeit) with bracha — not the usual night of 14 Nisan, and not on Shabbat. Recite the first bitul (Kol Chamira) immediately after the search.
• Biur chametz: Friday morning (13 Nisan) — burn the chametz found. Do not recite the final Kol Chamira at the burning — you will still eat chametz over Shabbat.
• Mechirat chametz: complete and finalize the sale before Shabbat begins Friday evening.
• Matzah on Friday (13 Nisan): many authorities extend the prohibition of eating regular matzah to Friday as well when Erev Pesach falls on Shabbat, so matzah is eaten with a prime appetite at the Seder (ask your rav).
• Seder prep: all physical cooking, roasting the zeroa (shankbone), checking lettuce/maror, and making charoset must be finished on Friday before Shabbat — you cannot prepare on Shabbat for Motzei Shabbat.
• Shabbat (14 Nisan) — eating deadline: finish eating chametz by the end of the 4th halachic hour on Shabbat morning.
• Shabbat (14 Nisan) — disposal: flush leftover crumbs down the toilet or nullify them chemically (e.g. pour liquid soap over them) before the 5th hour. Do not burn on Shabbat.
• Shabbat (14 Nisan) — final nullification: recite the final Kol Chamira before the end of the 5th halachic hour on Shabbat morning.
• Shabbat meals — lechem mishneh: Sephardim may use egg matzah (matzah ashira). Ashkenazim do not eat egg matzah on Pesach (Rema O.C. 462:4); use small challah rolls with extreme caution over disposable plates, shake out garments completely, and flush all crumbs before the 4th-hour deadline. Seudah shlishit: meat, fish, or fruit — not regular matzah on Erev Pesach.
• First Seder: Saturday night after Shabbat fully ends (tzeit). Kiddush includes the full Yaknehaz sequence (Wine, Yom Tov Kiddush, Ner/candle, Havdalah text, and Shehecheyanu — no spices/besamim).

Plan with your rav and local zmanim — many communities publish a Pesach-on-Shabbat timetable."""

EREV_PESACH_FRIDAY_BEFORE_SHABBAT_SCHEDULE_BODY = """

This year, Erev Pesach is on Friday (14 Nisan) and the first day of Pesach is Shabbat (15 Nisan):

• Bedikat chametz: Thursday night (night of 14 Nisan, after tzeit) — not Friday night. Recite the first bitul (Kol Chamira).
• Taanit Bechorot: Friday daytime (14 Nisan) — fast or attend a siyum.
• Biur chametz: Friday morning (14 Nisan) by the 5th halachic hour deadline. Both physical destruction and the final Kol Chamira nullification must be finished before this time.
• Mechirat chametz: must be entirely completed before Shabbat/Yom Tov candle lighting on Friday evening.
• First Seder: Friday night (commencing 15 Nisan).
• Second Seder (Diaspora only): Saturday night — transitioning directly from Shabbat into the second day of Yom Tov (not a regular weekend Motzei Shabbat). Kiddush includes Yaknehaz (integrated Havdalah: wine, Kiddush, candle, Havdalah text, Shehecheyanu — no spices/besamim).
• Preparation warning: you may NOT do any prep work (chopping, cooking, table setting) on Shabbat day for the second Seder. All preparations must wait until Shabbat ends at nightfall. Eruv tavshilin does not apply when Yom Tov falls on Shabbat — it is only for when Yom Tov immediately precedes Shabbat.

Confirm candle lighting, Yom Tov, and Seder times with your siddur and rav."""

EREV_PESACH_FRIDAY_13_NISAN_SCHEDULE = """This year, Erev Pesach is Shabbat (14 Nisan). Today (Friday, 13 Nisan): biur chametz this morning (no final Kol Chamira at burning — save chametz for Shabbat meals), and finish mechirat chametz before Shabbat candles. Taanit Bechorot was Thursday (12 Nisan). Bedikat was last night (Thursday). Tomorrow is Shabbat — finish chametz by the 4th halachic hour, final bitul by the 5th halachic hour; first Seder is Saturday night (Yaknehaz, then Seder)."""

PESACH_SCHEDULE_LEAD_INS = [
    "Today is Shabbat — Erev Pesach. The steps below should already be done; use this as a checklist.",
    "Today is Erev Pesach (Friday). The calendar below should already be in motion.",
    "Read this before bedikat chametz — tomorrow (Thursday) night after tzeit is the search, not the usual Erev Pesach night.",
    "Read this before bedikat chametz — tonight (Thursday night after tzeit) is bedikat, not tomorrow.",
    "Read this before bedikat chametz — tomorrow (Thursday) night after tzeit is bedikat, not Friday night.",
    "Read this before bedikat chametz — tonight (Thursday night after tzeit) is bedikat. Tomorrow is Erev Pesach (Friday): Taanit Bechorot, biur, and mechirat before Shabbat.",
]

REQUIRED_TEMPLATE_KEYS = [
    *YAALEH_EXPLAINERS.values(),
    ARBA_MINIM_MEN,
    ARBA_MINIM_FEMALE,
    FORGOT_MAARIV,
    forgot_amidah("Shacharit"),
    forgot_amidah("Mincha"),
    MECHIRAT_TEMPLATE,
    SEDER_PREP_TEMPLATE,
    BEDIKAT_TEMPLATE,
    BIUR_TEMPLATE,
    EREV_PESACH_ON_SHABBAT_SCHEDULE_BODY,
    EREV_PESACH_FRIDAY_BEFORE_SHABBAT_SCHEDULE_BODY,
    EREV_PESACH_FRIDAY_13_NISAN_SCHEDULE,
    *PESACH_SCHEDULE_LEAD_INS,
]

SEASONAL_FRAGMENTS_PATH = Path(__file__).resolve().parent / "_seasonal_fragments_en.json"
if SEASONAL_FRAGMENTS_PATH.exists():
    REQUIRED_TEMPLATE_KEYS.extend(json.loads(SEASONAL_FRAGMENTS_PATH.read_text(encoding="utf-8")))

from _public_fast_template_data import PUBLIC_FAST_CATALOG_KEYS  # noqa: E402
from _purim_meshulash_template_data import PURIM_MESHULASH_CATALOG_KEYS  # noqa: E402
from _write_seasonal_arg_explainers import SEASONAL_ARG_CATALOG_KEYS  # noqa: E402
from _kiddush_levana_wait_data import KIDDUSH_WAIT_CATALOG_KEYS  # noqa: E402
from _omer_explainer_arg_data import OMER_ARG_CATALOG_KEYS  # noqa: E402
from _update_chol_hamoed_copy import HONOR_KEY  # noqa: E402
from _yaaleh_template_data import YAALEH_TEMPLATE_CATALOG_KEYS  # noqa: E402
from _taanit_template_data import TAANIT_CATALOG_KEYS  # noqa: E402

REQUIRED_TEMPLATE_KEYS.extend(PUBLIC_FAST_CATALOG_KEYS)
REQUIRED_TEMPLATE_KEYS.extend(PURIM_MESHULASH_CATALOG_KEYS)
REQUIRED_TEMPLATE_KEYS.extend(SEASONAL_ARG_CATALOG_KEYS)
REQUIRED_TEMPLATE_KEYS.extend(KIDDUSH_WAIT_CATALOG_KEYS)
REQUIRED_TEMPLATE_KEYS.extend(OMER_ARG_CATALOG_KEYS)
REQUIRED_TEMPLATE_KEYS.append(HONOR_KEY)
REQUIRED_TEMPLATE_KEYS.extend(YAALEH_TEMPLATE_CATALOG_KEYS)
REQUIRED_TEMPLATE_KEYS.extend(TAANIT_CATALOG_KEYS)


def patch_string(s: str) -> str:
    out = s
    if "${threeWeeksIntro()}" in out:
        out = out.replace("${threeWeeksIntro()}", THREE_WEEKS_INTRO)
    if "${nineDaysSharedHalacha()}" in out:
        out = out.replace("${nineDaysSharedHalacha()}", NINE_DAYS_SHARED)
    if CHANUKAH_BRACHOT_OLD in out:
        out = out.replace(CHANUKAH_BRACHOT_OLD, CHANUKAH_BRACHOT_NEW)
    if OMER_SPEECH_OLD in out:
        out = out.replace(OMER_SPEECH_OLD, OMER_SPEECH_NEW)
    if "$THREE_WEEKS_INTRO" in out:
        out = out.replace("$THREE_WEEKS_INTRO", THREE_WEEKS_INTRO)
    if "$NINE_DAYS_SHARED_HALACHA" in out:
        out = out.replace("$NINE_DAYS_SHARED_HALACHA", NINE_DAYS_SHARED)
    if "$YAALEH_VYAVO_HALACHA_SOURCE" in out:
        out = out.replace("$YAALEH_VYAVO_HALACHA_SOURCE", YAALEH_HALACHA)
    if "$service" in out and "repeat only the $service Amidah" in out:
        # Orphan fragment — drop by returning empty (caller filters)
        return ""
    if "$bedikatLeadIn" in out:
        return ""
    return out


def main() -> None:
    data = json.loads(CATALOG_PATH.read_text(encoding="utf-8"))
    strings: list[str] = data["strings"]
    updated: list[str] = []
    removed = 0
    changed = 0
    seen: set[str] = set()

    for s in strings:
        if KOTLIN_ARTIFACT.search(s):
            removed += 1
            continue
        if "$scheduleBlock" in s and "$scheduleLeadIn" not in s:
            removed += 1
            continue
        out = patch_string(s)
        if not out:
            removed += 1
            continue
        if out != s:
            changed += 1
        if out not in seen:
            updated.append(out)
            seen.add(out)

    added = 0
    for key in REQUIRED_TEMPLATE_KEYS:
        if key not in seen:
            updated.append(key)
            seen.add(key)
            added += 1

    updated.sort()
    data["strings"] = updated
    CATALOG_PATH.write_text(json.dumps(data, ensure_ascii=False, indent=2), encoding="utf-8")
    print(
        f"Catalog sync: removed {removed} kotlin-artifact keys, "
        f"patched {changed} placeholders, added {added} template keys "
        f"({len(updated)} total)"
    )


if __name__ == "__main__":
    main()
