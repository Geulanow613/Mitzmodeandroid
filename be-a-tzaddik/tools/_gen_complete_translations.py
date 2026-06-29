#!/usr/bin/env python3
"""Generate complete_translations.json and update placeholder_fixes RU entries."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG_PATH = ROOT / "data" / "translation-catalog" / "strings.json"
OUT = ROOT / "data" / "translation-catalog" / "human" / "complete_translations.json"
PLACEHOLDER_FIXES_PY = Path(__file__).resolve().parent / "build_placeholder_fixes.py"

PLACEHOLDER_RE = [
    re.compile(r"\$\{[^}]+\}"),
    re.compile(r"\$[A-Za-z_][A-Za-z0-9_]*"),
]


def placeholders(text: str) -> list[str]:
    found: list[str] = []
    for pat in PLACEHOLDER_RE:
        found.extend(pat.findall(text))
    return found


def assert_placeholders(en: str, tr: str, label: str = "") -> None:
    en_p = placeholders(en)
    tr_p = placeholders(tr)
    if en_p != tr_p:
        raise ValueError(f"Placeholder mismatch {label}: en={en_p} tr={tr_p}")


def kotlin_blocks(text: str) -> list[str]:
    blocks, i = [], 0
    while i < len(text):
        if text.startswith("${", i):
            depth, j = 1, i + 2
            while j < len(text) and depth > 0:
                if text[j] == "{":
                    depth += 1
                elif text[j] == "}":
                    depth -= 1
                j += 1
            blocks.append(text[i:j])
            i = j
        else:
            i += 1
    return blocks


def force_kotlin_from_en(en: str, tr: str) -> str:
    en_b = kotlin_blocks(en)
    tr_b = kotlin_blocks(tr)
    if len(en_b) == len(tr_b):
        for eb, tb in zip(en_b, tr_b):
            if eb != tb:
                tr = tr.replace(tb, eb, 1)
    else:
        for eb in en_b:
            if eb not in tr:
                idx = tr.find("${")
                if idx >= 0:
                    depth, j = 1, idx + 2
                    while j < len(tr) and depth > 0:
                        if tr[j] == "{":
                            depth += 1
                        elif tr[j] == "}":
                            depth -= 1
                        j += 1
                    tr = tr[:idx] + eb + tr[j:]
    return tr


# --- RU CRITICAL (15 keys) ---
RU_CRITICAL: dict[str, str] = {}

RU_CRITICAL["Today is $holidayName (Yom Tov — a festival day). Please put away your device and keep the day holy. Melacha (forbidden labor) applies on Yom Tov similar to Shabbat."] = (
    "Сегодня $holidayName (Йом Тов — праздничный день). Пожалуйста, уберите устройство и храните день святым. "
    "Мелаха (запретный труд) действует в Йом Тов аналогично Шабbatу."
)

RU_CRITICAL["$holidayName is about to begin. Please finish what you are doing, turn off your phone, and prepare to welcome this Yom Tov with joy.\n\nOur Sages teach that rejoicing on Yom Tov is itself a mitzvah — may your observance bring blessing and divine reward to you and your household."] = (
    "$holidayName вот-вот начнётся. Закончите то, что делаете, выключите телефон и приготовьтесь встретить этот Йом Тов с радостью.\n\n"
    "Наши мудрецы учат: радоваться в Йом Тов — сама по себе мицва. Пусть ваше соблюдение принесёт благословение и божественную награду вам и вашему дому."
)

RU_CRITICAL["Today is $name — $meaning\n\nMinor fast rules:\n• No eating or drinking from dawn until nightfall.\n• Washing, music, and showering are generally permitted (unlike Tisha B'Av and Yom Kippur).\n• Special selichot and prayers may be recited in synagogue — check your community schedule."] = (
    "Сегодня $name — $meaning\n\nПравила малого поста:\n"
    "• Не есть и не пить с рассвета до наступления ночи.\n"
    "• Мытьё, музыка и душ, как правило, разрешены (в отличие от Тиша б'Ав и Йом Кипура).\n"
    "• В синагоге могут читаться особые селихот и молитвы — проверьте расписание вашей общины."
)

RU_CRITICAL["Read this today (Friday) before Shabbat candles — the app is not for use on Shabbat.\n\nTomorrow is Shabbat and erev $tomorrowChag. $tomorrowChag begins tomorrow night at nightfall (Motzei Shabbat), not tonight. Finish Yaknehaz prep, Yom Tov candles from a pre-existing flame, wine, and festive food before Shabbat ends."] = (
    "Прочитайте это сегодня (в пятницу) перед зажжением шаббатных свечей — приложение не для использования в Шабbat.\n\n"
    "Завтра Шабbat и эрев $tomorrowChag. $tomorrowChag начинается завтра вечером с наступлением ночи (Моцэи Шабbat), а не сегодня. "
    "Завершите подготовку Якнехаз, свечи Йом Тов от уже существующего пламени, вино и праздничную еду до окончания Шабbата."
)

SHEMINI_KEY = (
    "Shemini Atzeret begins tonight${if (profile.isInIsrael) \" — in Israel this is also Simchat Torah.\" else \".\"}\n\n"
    "Tonight & tomorrow:\n• Light Yom Tov candles.\n"
    "${shehecheyanuErevLines(HebrewCalendarEngine.SHEMINI_ATZERES, tomorrowCal, profile)}\n"
    "• No lulav on Shemini Atzeret — the mitzvah ended with the seventh day of Sukkot.\n${if (profile.isInIsrael)"
)
RU_CRITICAL[SHEMINI_KEY] = (
    "Шемини Ацерет начинается сегодня вечером${if (profile.isInIsrael) \" — in Israel this is also Simchat Torah.\" else \".\"}\n\n"
    "Сегодня вечером и завтра:\n• Зажигите свечи Йом Тов.\n"
    "${shehecheyanuErevLines(HebrewCalendarEngine.SHEMINI_ATZERES, tomorrowCal, profile)}\n"
    "• Без лулава в Шемини Ацерет — мицва завершилась с седьмым днём Сукkot.\n${if (profile.isInIsrael)"
)

RU_CRITICAL["New mitzvah submission from the Mitz Mode app:\n            \n            Submitted by: $submitterName\n            Mitzvah text: $mitzvahText\n            \n            Submitted on: ${java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\", java.util.Locale.getDefault()).format(java.util.Date())}\n            \n            Please review and consider adding to the mitzvah database."] = (
    "Новая заявка mitzvah из приложения Mitz Mode:\n            \n            "
    "Отправитель: $submitterName\n            Текст мицвы: $mitzvahText\n            \n            "
    "Отправлено: ${java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\", java.util.Locale.getDefault()).format(java.util.Date())}\n            \n            "
    "Пожалуйста, просмотрите и рассмотрите добавление в базу данных мицвot."
)

RU_CRITICAL["If you forgot at Maariv on Rosh Chodesh:\n• Still in Retzei before God's name at the conclusion — insert Yaaleh V'yavo there and continue ($YAALEH_VYAVO_HALACHA_SOURCE).\n• Once you finished Retzei, or after the entire Amidah — do not go back and do not repeat. Beit Din sanctified the new month by day, not at night (Berachot 30b; $YAALEH_VYAVO_HALACHA_SOURCE). Continue davening."] = (
    "Если вы забыли в Маариве в Рош Ходеш:\n"
    "• Ещё в Ретцее перед Именем Б-га в заключении — вставьте Яале Вьяво там и продолжите ($YAALEH_VYAVO_HALACHA_SOURCE).\n"
    "• После завершения Ретцee или всей Амиды — не возвращайтесь и не повторяйте. Бейт Дин освящал новый месяц днём, а не ночью (Berachot 30b; $YAALEH_VYAVO_HALACHA_SOURCE). Продолжайте молитву."
)

RU_CRITICAL["If you forgot:\n• Still in Retzei (before concluding the blessing) — insert Yaaleh V'yavo in its place and continue ($YAALEH_VYAVO_HALACHA_SOURCE).\n• After concluding Retzei — return to the beginning of Retzei, insert Yaaleh V'yavo, and complete the remaining blessings ($YAALEH_VYAVO_HALACHA_SOURCE).\n• Finished the entire Amidah (after the final Yihiyu L'ratzon) — repeat only the $service Amidah (Shemoneh Esrei), never the full service, even if you already davened Musaf, Maariv, or anything else afterward ($YAALEH_VYAVO_HALACHA_SOURCE)."] = (
    "Если вы забыли:\n"
    "• Ещё в Ретцее (до завершения благословения) — вставьте Яале Вьяво на его место и продолжите ($YAALEH_VYAVO_HALACHA_SOURCE).\n"
    "• После завершения Ретцee — вернитесь к началу Ретцee, вставьте Яале Вьяво и завершите оставшиеся благословения ($YAALEH_VYAVO_HALACHA_SOURCE).\n"
    "• Завершили всю Амиду (после финального Йихию л'рацон) — повторите только Амиду $service (Шмоне Эсре), никогда полный порядок, даже если уже молились Мусаф, Маарив или что-то ещё после ($YAALEH_VYAVO_HALACHA_SOURCE)."
)

PESACH_KEY = (
    "$pesachBegins\n\n$chametzNote\n\nSeder (first night):\n• $sederWhen\n"
    "• Matzah, maror, four cups of wine, reading the Haggadah, afikoman.\n"
    "• Reclining (hasebha): Recline to the left when drinking the four cups and eating matzah, korech, and afikoman — do not recline while eating maror or chazeret (they symbolize slavery).\n"
    "• Kiddush, festive meal, Hallel, Nirtzah.\n\nTomorrow by day:\n"
    "• Yom Tov davening with Full Hallel and Musaf; no chametz or kitniyot (per your custom).\n"
    "• Only eat food prepared for Pesach in kosher-for-Passover utensils.\n\n"
    "${diasporaSecondDayNote(profile, \"Pesach\")}$shabbatBlock"
)
RU_CRITICAL[PESACH_KEY] = (
    "$pesachBegins\n\n$chametzNote\n\nСедер (первая ночь):\n• $sederWhen\n"
    "• Маца, марор, четыре чашки вина, чтение Агады, афикоман.\n"
    "• Возлежание (хасеба): наклонитесь влево при питье четырёх чашек и еде мацы, кореха и афикомана — не возлеживайте при еде марора или хазерета (они символизируют рабство).\n"
    "• Киддуш, праздничная трапеза, Халел, Нирца.\n\nЗавтра днём:\n"
    "• Молитва Йом Тов с полным Халелом и Мусaf; без хамеца и китниот (по вашему минхагу).\n"
    "• Ешьте только еду, приготовленную к Песаху, в кошерной пасхальной посуде.\n\n"
    "${diasporaSecondDayNote(profile, \"Pesach\")}$shabbatBlock"
)

OMER_KEY = (
    "Sefirat HaOmer links Pesach to Shavuot — counting each day from the Exodus toward receiving the Torah.\n\n"
    "Today in the Omer: $todaySummary (day $day of 49).\n\nTonight's count:\n"
    "• $tonight night — count $todaySummary after nightfall$timePart.\n$nextNightLine\n\nHow to count:\n"
    "• Stand and recite the blessing before counting if you are still saying it with a blessing (if you missed a day, ask your rabbi before continuing with a bracha).\n"
    "• Say: \"${omerCountSpeechPhrase(day)}\"\n"
    "• Count after nightfall (tzeit); complete before dawn. If you forgot at night, count the next day during the daytime without a bracha. "
    "If you do this before sunset, you can continue counting on subsequent nights with a bracha. "
    "You only lose the blessing permanently if you miss an entire 24-hour cycle (both night and the following day) — ask your rav.\n\n$nusachWhen"
)
RU_CRITICAL[OMER_KEY] = (
    "Сефират ha-Omer связывает Песах со Шavuot — считая каждый день от Исхода к получению Торы.\n\n"
    "Сегодня в Омере: $todaySummary (день $day из 49).\n\nВечерний счёт:\n"
    "• Ночь $tonight — считайте $todaySummary после наступления ночи$timePart.\n$nextNightLine\n\nКак считать:\n"
    "• Встаньте и произнесите благословение перед счётом, если ещё говорите его с брaхой (если пропустили день, спросите раввина перед продолжением с брaхой).\n"
    "• Скажите: \"${omerCountSpeechPhrase(day)}\"\n"
    "• Считайте после наступления ночи (tzeit); завершите до рассвета. Если забыли ночью, считайте на следующий день днём без брaхи. "
    "Если сделаете это до заката, можете продолжать последующие ночи с брaхой. "
    "Брaху теряете навсегда только если пропустите полный 24-часовой цикл (и ночь, и следующий день) — спросите раввина.\n\n$nusachWhen"
)

SUKKOT_KEY = (
    "Sukkot (first day) begins tonight — Zman Simchateinu.\n\nBefore sunset:\n"
    "• Avoid eating a formal meal inside the sukkah today (Rama O.C. 639:1) so that your entry tonight is distinctly dedicated to the start of the mitzvah.\n"
    "• Have arba minim ready: lulav, etrog, hadasim, aravot (per your rabbi's kashrut standards).\n\nTonight & tomorrow:\n"
    "• Light Yom Tov candles in the sukkah (per custom) or home.\n"
    "${shehecheyanuErevLines(HebrewCalendarEngine.SUCCOS, tomorrowCal, profile)}\n"
    "• ${if (profile.isInIsrael) \"Shake lulav and etrog with bracha tomorrow (first day of Sukkot).\" else \"Shake lulav and etrog with bracha on the first and second days of Yom Tov (Diaspora); continue on Chol HaMoed per custom.\"}\n"
    "• Festive meals in the sukkah; Ushpizin welcome guests.\n• No melacha; Full Hallel and Musaf in davening.\n\n"
    "${diasporaSecondDayNote(profile, \"Sukkot\")}"
)
RU_CRITICAL[SUKKOT_KEY] = (
    "Суккот (первый день) начинается сегодня вечером — Зман Симхатеinu.\n\nПеред закатом:\n"
    "• Избегайте формальной трапезы в сукке сегодня (Рама О.Х. 639:1), чтобы ваш вход сегодня вечером был посвящён началу мицвы.\n"
    "• Приготовьте arba minim: lulav, etrog, hadassim, aravot (по стандартам кашрута вашего раввина).\n\nСегодня вечером и завтра:\n"
    "• Зажигите свечи Йом Тов в сукке (по минхагу) или дома.\n"
    "${shehecheyanuErevLines(HebrewCalendarEngine.SUCCOS, tomorrowCal, profile)}\n"
    "• ${if (profile.isInIsrael) \"Shake lulav and etrog with bracha tomorrow (first day of Sukkot).\" else \"Shake lulav and etrog with bracha on the first and second days of Yom Tov (Diaspora); continue on Chol HaMoed per custom.\"}\n"
    "• Праздничные трапезы в сукке; Ushpizin приветствуют гостей.\n• Без мелахи; полный Халел и Мусaf в молитве.\n\n"
    "${diasporaSecondDayNote(profile, \"Sukkot\")}"
)

HAV_YT_KEY = (
    "$opener\n\nHavdalah when Shabbat leads into Yom Tov:\n"
    "• Havdalah is recited when entering a day of lesser holiness. Shabbat is holier than Yom Tov, so when Shabbat leads into a festival, havdalah is included in that night's Kiddush — not as a full separate havdalah with spices before Kiddush.\n"
    "• Order (mnemonic YaKNeHaZ per many Ashkenaz poskim): Yayin (borei pri hagafen) → Kiddush for Yom Tov → Ner (borei me'orei ha'eish — recite over the Yom Tov candles already lit on the table; do NOT pick up, move, or touch them — they are muktzeh once lit; gaze at the flames from where they stand) → Havdalah (holiday text ending bein kodesh l'kodesh, not bein kodesh l'chol) → Zeman (Shehecheyanu on the first festival night when applicable).\n"
    "• Spices (besamim) are omitted for this transition.\n"
    "• Before Kiddush, melacha permitted on Yom Tov but not on Shabbat: many say Baruch hamavdil bein kodesh l'kodesh, or rely on the Vatodi'enu insert in Maariv — follow your Machzor.\n\n"
    "$prepWhen: Yom Tov candles from a pre-existing flame; wine; festive meal ready; 48-hour candle or pilot light per your rav."
)
RU_CRITICAL[HAV_YT_KEY] = (
    "$opener\n\nХавдала, когда Шабbat переходит в Йом Тов:\n"
    "• Хавдала читается при входе в день меньшей святости. Шабbat святее Йом Тов, поэтому когда Шабbat переходит в праздник, хавдала включается в киддуш той ночи — не как отдельная полная хавдала со специями перед киддушем.\n"
    "• Порядок (мнемоника YaKNeHaZ у многих ашкеназских позким): Yayin (borei pri hagafen) → Kiddush для Йом Тов → Ner (borei me'orei ha'eish — над уже зажжёнными на столе свечами Йом Тов; НЕ поднимать, не двигать и не касаться — они мукце после зажигания; смотреть на пламя там, где они стоят) → Havdalah (праздничный текст, завершающийся bein kodesh l'kodesh, не bein kodesh l'chol) → Zeman (Shehecheyanu в первую праздничную ночь, когда применимо).\n"
    "• Специи (besamim) опускаются при этом переходе.\n"
    "• Перед киддушем, мелаха разрешена в Йом Тов, но не в Шабbat: многие говорят Baruch hamavdil bein kodesh l'kodesh или полагаются на вставку Vatodi'enu в Маариве — следуйте вашему махзору.\n\n"
    "$prepWhen: свечи Йом Тов от уже существующего пламени; вино; праздничная трапеза готова; 48-часовая свеча или pilot light по указанию раввина."
)

HAV_YK_KEY = (
    "$opener\n\nHavdalah inside Kiddush (Yaknehaz):\n"
    "• When a biblical holiday begins Saturday night (including Rosh Hashana), that night's Kiddush incorporates havdalah for Shabbat. The order is: (1) wine blessing, (2) holiday Kiddush, (3) candle blessing, (4) havdalah blessing — with the Yom Tov text concluding bein kodesh l'kodesh (not bein kodesh l'chol), (5) Shehecheyanu for the festival.\n"
    "• This is the YaKNeHaZ order (Yayin, Kiddush, Ner, Havdalah, Zeman) codified in Shulchan Arukh for Motzei Shabbat into Yom Tov.\n"
    "• Spices are not used.\n"
    "• Use the Machzor or siddur nusach for Rosh Hashana — do not rely on memory for the long havdalah text.\n\nBefore or at Maariv:\n"
    "• You may say Baruch hamavdil bein kodesh l'kodesh to begin Yom Tov-permitted activities before Kiddush, or rely on Vatodi'enu in the Amidah of Maariv — follow your community.\n\n$prepLead:\n"
    "• Have round challah, honey, apples, and symbolic foods ready for the Yom Tov meals after Shabbat (minhag).\n"
    "• Confirm shofar and Musaf times for the first day(s) of Rosh Hashana after Shabbat — shofar is not blown on Shabbat itself.\n"
    "• Tashlich: when the first day of Rosh Hashana is Shabbat, it is postponed to Sunday (structural rule — avoiding carrying machzorim in public without an eruv).\n\n"
    "Candles: after Shabbat ends, light Yom Tov candles from a flame lit before Shabbat began (pre-existing flame)."
)
RU_CRITICAL[HAV_YK_KEY] = (
    "$opener\n\nХавдала внутри киддуша (Yaknehaz):\n"
    "• Когда библейский праздник начинается в субботу вечером (включая Рош ha-Shana), кидdуш той ночи включает хавдалу для Шабbата. Порядок: (1) благословение вина, (2) праздничный кидdуш, (3) благословение свечи, (4) благословение хавdалы — с текстом Йом Тов, завершающимся bein kodesh l'kodesh (не bein kodesh l'chol), (5) Shehecheyanu для праздника.\n"
    "• Это порядок YaKNeHaZ (Yayin, Kiddush, Ner, Havdalah, Zeman), закреплённый в Шулхан Арух для Моцэi Шабbat в Йом Тов.\n"
    "• Специи не используются.\n"
    "• Используйте nusach махзора или сиддура для Рosh ha-Shana — не полагайтесь на память для длинного текста хавdалы.\n\nПеред или во время Маариva:\n"
    "• Можно сказать Baruch hamavdil bein kodesh l'kodesh, чтобы начать дела, разрешённые в Йом Тов, до кидdуша, или полагаться на Vatodi'enu в Амиде Маариva — следуйте вашей общине.\n\n$prepLead:\n"
    "• Приготовьте круглую халу, мёд, яблоки и символические блюда для трапез Йом Тов после Шабbата (минhag).\n"
    "• Уточните время шofара и Мусaf на первый день(дни) Рosh ha-Shana после Шабbата — шofар не трубят в сам Шабbat.\n"
    "• Ташlich: когда первый день Рosh ha-Shana — Шабbat, переносится на воскресенье (структурное правило — избегание ношения махзоров в общественном месте без эрува).\n\n"
    "Свечи: после окончания Шабbата зажигайте свечи Йом Тов от пламени, зажжённого до начала Шабbата (pre-existing flame)."
)

SEDER_PREP_KEY = (
    "$intro\n\n$sederNights\n$secondSederHachana\n$omerTrigger\n\nSet up before Yom Tov:\n"
    "• Matzah — shmurah matzah for motzi/matza mitzvah (three matzot on the plate per custom)\n"
    "• Maror — romaine, horseradish, or bitter herbs per your minhag\n"
    "• Four cups of wine per participant (grape juice is widely used if needed — ask your rav; not the same debate as Chol HaMoed wine)\n"
    "• Haggadah for each person (or shared)\n"
    "• Seder plate: zeroa (shankbone), beitzah (egg), karpas, charoset, maror, chazeret\n"
    "• Seder plate prep: You should ideally roast your zeroa (shankbone) on Erev Pesach day before sunset. Because the shankbone is not eaten on Seder night, roasting it after the holiday begins violates Yom Tov cooking laws. The egg (beitzah), however, is traditionally eaten during the meal, so it may legally be boiled or roasted on Yom Tov night if needed.\n"
    "• Reclining (hasebha): Recline to the left when drinking the four cups and eating matzah, korech, and afikoman — do not recline while eating maror or chazeret (they symbolize slavery).\n"
    "• Festive table; candles for Yom Tov\n\nKitchen:\n• Only kosher-for-Passover food and utensils from this point\n"
    "• Warm food on a blech or pre-set timer for Yom Tov meals\n\n"
    "At the Seder: follow your Haggadah step by step — Kiddush, the order of the night, brachot, and the four cups are all laid out there. "
    "${if (profile.isInIsrael) \"One\" else \"Two\"} Seder night(s) this Pesach."
)
RU_CRITICAL[SEDER_PREP_KEY] = (
    "$intro\n\n$sederNights\n$secondSederHachana\n$omerTrigger\n\nПодготовка до Йом Тов:\n"
    "• Маца — shmurah matzah для motzi/matza mitzvah (три мацы на тарелке по минhagу)\n"
    "• Марор — romaine, хрен или горькие травы по вашему минhagу\n"
    "• Четыре чашки вина на участника (виноградный сок широко используется при необходимости — спросите равvина; не тот же спор, что о вине в Хol ha-Moed)\n"
    "• Агада на каждого (или общая)\n"
    "• Тарелка седера: zeroa (shankbone), beitzah (яйцо), karpas, charoset, maror, chazeret\n"
    "• Подготовка тарелки: zeroa (shankbone) желательно жарить в Эреv Песаха до заката. Поскольку shankbone не едят в ночь седера, жарка после начала праздника нарушает законы готовки в Йом Тов. Яйцо (beitzah), однако, традиционно едят во время трапезы, поэтому его можно варить или жарить в ночь Йом Тов при необходимости.\n"
    "• Возлежание (hasebha): наклонитесь влево при питье четырёх чашек и еде мацы, кореха и афикомана — не возлеживайте при еде марора или хazерeta (они символизируют рабство).\n"
    "• Праздничный стол; свечи для Йом Тов\n\nКухня:\n• Только кошерная пасхальная еда и посуда с этого момента\n"
    "• Подогревайте еду на blech или по таймеру для трапез Йом Тов\n\n"
    "На седере: следуйте Аgаде шаг за шагом — кидdуш, порядок ночи, брaхot и четыре чашки указаны там. "
    "${if (profile.isInIsrael) \"One\" else \"Two\"} ночь(и) седера в этом Песахе."
)

ILANOT_KEY = (
    "Birkat Ha'Ilanot (בִּרְכַּת הָאִילָנוֹת — Blessing of the Trees) is recited once each Jewish year upon seeing fruit trees in bloom — thanking Hashem for the beauty and renewal of creation.\n\n"
    "This checklist appears during the likely season for your hemisphere — a reminder to go look for blossoms, not an obligation to recite before you see them.\n\n$whenBlock\n\n"
    "The blessing (said once per year):\nBaruch Atah Ado-nai Eloheinu Melech ha'olam shelo chiser be'olamo kelum u'vara vo beriyot tovot v'ilanot tovim lehanot bahem benei adam.\n"
    "(בָּרוּךְ אַתָּה ה' אֱלֹקֵינוּ מֶלֶךְ הָעוֹלָם שֶׁלֹּא חִסֵּר בְּעוֹלָמוֹ כְּלוּם וּבָרָא בוֹ בְּרִיּוֹת טוֹבוֹת וְאִילָנוֹת טוֹבִים לֵהָנוֹת בָּהֶם בְּנֵי אָדָם)\n"
    "\"Blessed are You, L-rd our G-d, King of the universe, Who has withheld nothing from His world and created in it good creatures and good trees for people to enjoy.\"\n\n"
    "How to fulfill it:\n• Go outdoors to fruit-bearing trees — at least one tree; preferably two or more of the same or different species.\n"
    "• Recite the blessing once while viewing the blossoms. Many add Tehillim 104 or other verses; follow your siddur or community custom.\n"
    "• Once a year: if you said it in your local spring, you do not repeat it when traveling to another hemisphere later in the same Jewish year.\n"
    "• Many communities avoid saying it on Shabbat — go during the week when you first see suitable blossoms.\n\n"
    "What stage must the tree be in (melav'lave — מְלַבְלְבֵי)?\nThe rabbis require the tree to be actively flowering — not merely sprouting leaves. Shulchan Arukh O.C. 226:1 defines the Talmudic stage of melav'lave as when trees \"put forth flowers\" (shemotzi'in perach): you need open, visible blossoms that you can recognize as flowers. The blessing praises Hashem for creating \"good trees for people to enjoy\" — that response is meant to come from seeing beautiful, open blooms.\n\n"
    "L'chatchilah (ideal): the tree has moved past the leaf-only stage and shows open flowers, but the petals have not yet fallen and fruit has not begun to form.\n\nWhat does not work:\n"
    "• Green leaf buds alone — invalid; do not recite the blessing on leaves without open flowers.\n"
    "• After petals have dropped and fruit is forming — too late for this year's blessing.\n\n"
    "Bedieved (after the fact): some contemporary poskim allow reciting on swollen, tightly closed flower buds that are clearly about to open when no properly blooming trees are available — ask your rav if that is your only option.\n\n"
    "Spiritual note:\nNissan is the month of redemption; blossoming trees recall that creation itself waits to praise Hashem. Even a brief walk among flowering orchards can be a mitzvah."
)
RU_CRITICAL[ILANOT_KEY] = (
    "Birkat Ha'Ilanot (בִּרְכַּת הָאִילָנוֹת — Благословение деревьев) читается один раз в еврейский год, увидев цветущие плодовые деревья — благодаря В-у за красоту и обновление творения.\n\n"
    "Этот чеклист появляется в вероятный сезон для вашего полушария — напоминание пойти искать цветы, а не обязанность читать до того, как увидите их.\n\n$whenBlock\n\n"
    "Благословение (один раз в год):\nBaruch Atah Ado-nai Eloheinu Melech ha'olam shelo chiser be'olamo kelum u'vara vo beriyot tovot v'ilanot tovim lehanot bahem benei adam.\n"
    "(בָּרוּךְ אַתָּה ה' אֱלֹקֵינוּ מֶלֶךְ הָעוֹלָם שֶׁלֹּא חִסֵּר בְּעוֹלָמוֹ כְּלוּם וּבָרָא בוֹ בְּרִיּוֹת טוֹבוֹת וְאִילָנוֹת טוֹבִים לֵהָנוֹת בָּהֶם בְּנֵי אָדָם)\n"
    "«Благословен Ты, Г-споди Б-г наш, Царь вселенной, Который не лишил Своего мира ничего и создал в нём добрые творения и добрые деревья, чтобы люди наслаждались ими.»\n\n"
    "Как исполнить:\n• Выйдите к плодоносящим деревьям — хотя бы одно; лучше два или более одного или разных видов.\n"
    "• Произнесите благословение один раз, глядя на цветы. Многие добавляют Тehillim 104 или другие стихи; следуйте сидdуру или минhagу общины.\n"
    "• Раз в год: если сказали в местной весне, не повторяйте при поездке в другое полушарие позже в том же еврейском году.\n"
    "• Многие общины избегают чтения в Шабbat — идите в будний день, когда впервые увидите подходящие цветы.\n\n"
    "На какой стадии должно быть дерево (melav'lave — מְלַבְלְבֵי)?\n"
    "Мудрецы требуют активного цветения — не только листьев. Шулхан Арух О.Х. 226:1 определяет talmudическую стадию melav'lave как когда деревья «дают цветы» (shemotzi'in perach): нужны открытые, видимые цветы. Благословение хвалит В-а за «добрые деревья для наслаждения людей» — ответ должен идти от красивых открытых цветов.\n\n"
    "L'chatchilah (идеально): дерево прошло стадию только листьев и показывает открытые цветы, но лепестки ещё не опали и плоды не начали формироваться.\n\nЧто не подходит:\n"
    "• Только зелёные почки листьев — недействительно; не читайте благословение на листья без открытых цветов.\n"
    "• После опадания лепестков и начала формирования плодов — слишком поздно для благословения этого года.\n\n"
    "Bedieved (post factum): некоторые современные позkim разрешают читать на опухших, плотно закрытых бутонах, явно готовых раскрыться, когда нет должным образом цветущих деревьев — спросите раvвина, если это единственный вариант.\n\n"
    "Духовная заметка:\nНissan — месяц искупления; цветущие деревья напоминают, что само творение ждёт, чтобы восхвалить В-а. Даже короткая прогулка среди цветущих садов может быть мицвой."
)

