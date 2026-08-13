#!/usr/bin/env python3
"""Build quality_overrides.json — wins over all human shards including content_long."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog"
SHARDS = CATALOG / "shards"
HUMAN = CATALOG / "human"
OUT = HUMAN / "quality_overrides.json"
LANGS = ("he", "es", "fr", "ru")

GARBAGE = [
    re.compile(r"⟦\d"),
    re.compile(r"\u27e6|\u27e7"),
    re.compile(r"איריירי"),
    re.compile(r"prostoy|klassicheskoe|prpis|uchат|mistическая"),
    re.compile(r"גedalya|מודה אאני|צום גedalya"),
    re.compile(r"צילום מסך"),  # screenshot corruption in HE
]


def is_garbage(text: str) -> bool:
    return any(p.search(text) for p in GARBAGE)


def load_json(path: Path) -> dict:
    if not path.exists():
        return {}
    return json.loads(path.read_text(encoding="utf-8"))


def merge_shard(out: dict[str, dict[str, str]], path: Path) -> None:
    data = load_json(path)
    for lang in LANGS:
        block = data.get(lang)
        if isinstance(block, dict):
            out[lang].update(block)


def load_mitzvot_human(out: dict[str, dict[str, str]]) -> None:
    for path in sorted(HUMAN.glob("mitzvot_*.json")):
        if any(x in path.name for x in ("_only", "_src", "_keys")):
            continue
        merge_shard(out, path)
    for path in sorted(HUMAN.glob("local_*.json")):
        if any(x in path.name for x in ("_only", "_src", "_keys")):
            continue
        merge_shard(out, path)


def load_clean_content_long(out: dict[str, dict[str, str]]) -> None:
    """Keep content_long/medium entries when not garbage; quality_fixes already merged."""
    for pattern in ("content_long_*.json", "content_medium_*.json"):
        for path in sorted(HUMAN.glob(pattern)):
            data = load_json(path)
            for lang in LANGS:
                block = data.get(lang)
                if not isinstance(block, dict):
                    continue
                for key, tr in block.items():
                    if key in out[lang]:
                        continue
                    if not isinstance(tr, str) or tr == key or is_garbage(tr):
                        continue
                    if lang == "he" and len(key) > 150:
                        letters = [c for c in tr if c.isalpha()]
                        if letters and sum(1 for c in letters if c.isascii()) / len(letters) > 0.12:
                            continue
                    out[lang][key] = tr


def manual_ru() -> dict[str, str]:
    pshat = (
        "Pshat is the plain, straightforward meaning of a Torah verse — what the text says on its surface "
        "before deeper layers. Rashi on Chumash mainly explains pshat. Learning pshat is the foundation "
        "before midrash or Kabbalah."
    )
    zohar = (
        "The Zohar is the classic work of Kabbalah, attributed to Rabbi Shimon bar Yochai, written in Aramaic. "
        "It illuminates the Torah's inner dimensions. Many study a daily snippet; full mastery requires "
        "Hebrew/Aramaic and guidance."
    )
    kab = (
        "Kabbalah is the Jewish mystical tradition exploring how G-d relates to creation, the soul, and mitzvot. "
        "Texts include the Zohar and later works like Tanya. Authentic Kabbalah is studied with Torah literacy "
        "and a teacher; it is not fortune-telling or magic."
    )
    ru_pshat = (
        "Пшат — простой, прямой смысл стиха Торы: то, что текст говорит на поверхности, до более глубоких "
        "уровней. Раши на Хумаш в основном объясняет пшат. Изучение пшата — основа перед мидрашем и Каббалой."
    )
    ru_zohar = (
        "Зоар — классическое произведение Каббалы, приписываемое раввину Шимону бар Йохаю, написано на арамейском. "
        "Оно освещает внутренние измерения Торы. Многие изучают ежедневный отрывок; полное освоение требует "
        "иврита/арамейского и наставника."
    )
    ru_kab = (
        "Каббала — еврейская мистическая традиция, исследующая отношение В-а к творению, душе и мitzvot. "
        "Тексты включают Зоар и более поздние работы, например «Таню». Подлинная Каббала изучается при знании "
        "Торы и с учителем; это не гадание и не магия."
    )
    gedaliah_long = (
        "The Fast of Gedaliah is observed on 3 Tishrei (pushed to 4 Tishrei when 3 Tishrei falls on Shabbat). "
        "It commemorates the assassination of Gedaliah ben Ahikam, the Jewish governor appointed by Babylonia "
        "to administer the land after the destruction of the First Temple.\n\nHis death marked the end of the "
        "last vestiges of Jewish autonomy in the Land of Israel following the first exile.\n\nIt is a minor fast "
        "— from dawn until nightfall (not a full 25-hour fast like Yom Kippur or Tisha B'Av)."
    )
    return {
        pshat: ru_pshat,
        f"pshat — {pshat}": f"pshat — {ru_pshat}",
        zohar: ru_zohar,
        f"Zohar — {zohar}": f"Zohar — {ru_zohar}",
        kab: ru_kab,
        f"Kabbalah — {kab}": f"Каббала — {ru_kab}",
        "Kabbalah": "Каббала",
        gedaliah_long: (
            "Пост Гедалии — 3 тишрея (4-го, если 3-е выпадает на шабbat). Память об убийстве Гедалии "
            "бен Ахикама, еврейского наместника, назначенного Вавилонией после разрушения Первого Храма.\n\n"
            "Его смерть ознаменовала конец последних остатков еврейской автономии в Земле Израиля после "
            "первого изгнания.\n\nЭто малый пост — от рассвета до наступления ночи (не полный 25-часовой пост, "
            "как в Yom Kippur или Tisha B'Av)."
        ),
    }


def manual_he() -> dict[str, str]:
    special = (
        "**Special Mitzvah**: Light an oil candle (such as a floating wick in a small cup of oil) for the "
        "elevation of the souls of the \"Neshamot Hagalmudot\" (הנשמות הגלמודות) -the forlorn departed souls "
        "in Heaven who need just a small merit to get into the Garden of Eden (Gan Eden). Rabbis say these poor "
        "souls are just missing a small merit to be let into the eternal pleasure of Gan Eden,  and if you light "
        "a candle in their merit, you could help them get in! Then these souls are extremely grateful, and once "
        "they're in they can pray for you for all your wishes!"
    )
    super_special = special.replace("**Special Mitzvah**", "**Super-Special Mitzvah**")
    local = load_json(HUMAN / "local_002.json")
    he_special = local.get("he", {}).get(special, "")
    fixes: dict[str, str] = {}
    if he_special:
        fixes[super_special] = he_special.replace("**Special Mitzvah**", "**Super-Special Mitzvah**")
    fixes["Fast of Gedaliah — fast day after Rosh Hashana"] = "תענית גדליה — יום צום לאחר ראש השנה"
    gedaliah_long = (
        "The Fast of Gedaliah is observed on 3 Tishrei (pushed to 4 Tishrei when 3 Tishrei falls on Shabbat). "
        "It commemorates the assassination of Gedaliah ben Ahikam, the Jewish governor appointed by Babylonia "
        "to administer the land after the destruction of the First Temple.\n\nHis death marked the end of the "
        "last vestiges of Jewish autonomy in the Land of Israel following the first exile.\n\nIt is a minor fast "
        "— from dawn until nightfall (not a full 25-hour fast like Yom Kippur or Tisha B'Av)."
    )
    fixes[gedaliah_long] = (
        "צום גדליה נצפה ב-3 בתשרי (נדחה ל-4 בתשרי כש-3 בתשרי חל בשבת). הוא מנציח את רצח גדליה בן אחיקם, "
        "המושל היהודי שמינתה בבל לנהל את הארץ לאחר חורבן בית המקדש הראשון.\n\n"
        "מותו סימן את סוף שרידי האוטונומיה היהודית בארץ ישראל לאחר הגלות הראשונה.\n\n"
        "זהו צום קטן — משחר עד צאת הכוכבים (לא צום של 25 שעות כמו יום כיפור או תשעה באב)."
    )
    return fixes


MODEH_NETILAT_HE = (
    "מיד לאחר אמירת \"מודה אני\", שוטפים את הידיים בנטילת ידיים לפני כל דבר אחר.\n\n"
    "מה זה:\n"
    "נטילת ידיים (נְטִילַת יָדַיִם — \"הרמת הידיים\") היא שטיפה טקסית — לא רק היגיינה אלא מעשה רוחני. "
    "בשינה עמוקה הנשמה מתעלה חלקית ורוח רעה שורה על האצבעות והציפורניים. "
    "השטיפה מסירה זאת ומכינה לתפילה, תורה והיום.\n\n"
    "מנהגים שונים חולקים אם נטילת הבוקר עיקרה להסיר רוח רעה או להכין לתפילה — "
    "לכן מתי אומרים \"על נטילת ידיים\" שונה בין אשכנז, ספרד וחב\"ד.\n\n"
    "איך לשפוך (כל המנהגים):\n"
    "1. עדיף כוס/בקבוק מים וכלי ליד המיטה לפני השינה\n"
    "2. שופכים על ימין, אחר כך שמאל, שלוש פעמים לסירוגין\n"
    "3. מנגבים כשהמנהג קובע\n\n"
    "ברכת \"על נטילת ידיים\" — לפי מנהגכם, בבוקר או לפני תפילה."
)

BRACHA_HE = (
    "לפני אכילה או שתייה אומרים ברכה קצרה ומודים לה' על המזון. "
    "זו אחת המצוות הנפוצות ביותר בחיי היומיום.\n\n"
    "מהי ברכה:\n"
    "ברכה (בְּרָכָה — ברכה, רבים: ברכות) היא נוסח קצר שמתחיל "
    "\"ברוך אתה ה' אלוקינו מלך העולם...\" וממשיך בסיום המתאים למין המזון.\n\n"
    "עקרון התלמוד:\n"
    "\"אסור ליהנות מן העולם הזה בלא ברכה.\" אכילה בלא ברכה כאילו העולם הפך להפקר — "
    "הברכה מודה שהכל שייך לה'.\n\n"
    "שש ברכות עיקריות לפני אוכל:\n"
    "• המוציא — על לחם מחמשת מיני דגן\n"
    "• מזונות — על מאכלי דגן אחרים\n"
    "• הגפן — על יין או מיץ ענבים\n"
    "• העץ — על פרי מעץ\n"
    "• האדמה — על פרי מהאדמה\n"
    "• שהכל — על כל מה שלא בקטגוריה אחרת"
)

MODEH_ANI_SHORT_HE = (
    "מודה אני הוא בין המילים הראשונות שיהודים רבים אומרים עם ההקיצה.\n\n"
    "הנוסח: \"מודה אני לפניך מלך חי וקיים, שהחזרת בי נשמתי בחמלה — רבה אמונתך.\"\n\n"
    "מודים לה' על החזרת הנשמה לפני שקמים מהמיטה."
)

TZEDAKAH_HE = (
    "מתן צדקה (צְדָקָה) הוא מצווה מתמדת. מנהג יסוד הוא מעשר כספים — להפריש כעשרה אחוז מההכנסה לצדקה.\n\n"
    "חישוב: בדרך כלל מנכים מסים והוצאות הכרחיות, ואז מפרישים עשרה אחוז. שאלו רב לגבי מקרה ספציפי.\n\n"
    "עדיפויות: עניים מקומיים, מוסדות תורה, נזקקים בארץ ישראל.\n\n"
    "אם קשה כלכלית: פרנסה וצרכים בסיסיים קודמים; אפשר לתת פחות מעשר כשיש קושי אמיתי."
)

ASHER_YATZAR_HE = (
    "לאחר יציאה לשירותים שוטפים ידיים ואומרים ברכת \"אשר יצר\" — "
    "מודים לה' על גוף הפועל כראוי.\n\n"
    "הברכה מזכירה את מערכות הגוף המורכבות; אומרים אותה בכל פעם אחרי שימוש בשירותים."
)

MARKETPLACE_HE = (
    "הדין היהודי חל בשוק ובמשרד כמו בבית הכנסת — משקלות כנות, עסקאות הוגנות והתנהלות אמינה הם חובות תורה.\n\n"
    "כולל: הטעיה במסחר, אונאה (מחיר לא הוגן), כיבוד דברים, תשלום שכר בזמן, איסור גזל בכל צורה."
)

LASHON_HE = (
    "הדיבור הוא מן הכוחות החזקים ביותר בחיים היהודיים — יכול לבנות או להרוס.\n\n"
    "שמירת הלשון: להימנע מלשון הרע, רכילות, עלבונות בדיבור ושקר.\n\n"
    "חכמים משווים לשון הרע לרצח — שמירה על הלשון היא מצווה יומיומית."
)


def apply_prefix_overrides(out: dict[str, dict[str, str]], catalog: list[str]) -> None:
    prefix_he = {
        "Immediately after saying Modeh Ani, wash your hands": MODEH_NETILAT_HE,
        "Giving charity (tzedakah": TZEDAKAH_HE,
        "After using the bathroom, we wash our hands": ASHER_YATZAR_HE,
        "Jewish law applies in the marketplace": MARKETPLACE_HE,
        "Speech is one of the most powerful forces": LASHON_HE,
        "Modeh Ani is the first words": MODEH_ANI_SHORT_HE,
        "Modeh Ani — Modeh Ani is the first words": MODEH_ANI_SHORT_HE,
        "Before eating or drinking anything, we say a short blessing": BRACHA_HE,
    }
    for prefix, tr in prefix_he.items():
        for s in catalog:
            if s.startswith(prefix):
                out["he"][s] = tr

    prefix_ru = {
        "Chazal (חז\"ל) is an acronym": (
            "Хазал (חז\"ל) — аббревиатура «наши мудрецы, да будет благословенна их память» — "
            "раввины Мишны, Тalmuda и midrasha, передавшие halakhu и ценности."
        ),
        "Chazal — Chazal (חז\"ל)": (
            "Chazal — Хазал (חז\"ל) — аббревиатура «наши мудрецы, да будет благословенна их память»."
        ),
        "Bitachon is trust in G-d": (
            "Битахон — доверие к В-у: вера, что Он даёт необходимое и что трудности могут иметь смысл."
        ),
        "Bitachon — Bitachon is trust": (
            "Bitachon — Битахон — доверие к В-у и спокойствие в повседневных заботах."
        ),
        "Kallah means bride": (
            "Кала означает невесту; занятия для невест учат taharat hamishpacha и подготовке ל браку."
        ),
        "kallah — Kallah means bride": (
            "kallah — Кала означает невесту; занятия для невест учат taharat hamishpacha."
        ),
        "Take a minute to pray for someone else's needs": (
            "Уделите минуту молитве о нуждах других прежде своих — мудрецы учат, что так часто исполняются и ваши просьбы."
        ),
        "Tzedakah is usually translated \"charity,\"": (
            "Слово «tzedakah» часто переводят как «благотворительность», но корень означает справедливость — "
            "делиться тем, что В-а вам доверил, с нуждающимися. Мудрецы считают это одним из столпов мира; "
            "даже малая монета, данная от всего сердца, — великая mitzvah."
        ),
        "tzedakah — Tzedakah is usually translated": (
            "tzedakah — Слово «tzedakah» часто переводят как «благотворительность», но корень означает справедливость — "
            "делиться тем, что В-а вам доверил, с нуждающимися."
        ),
    }
    for prefix, tr in prefix_ru.items():
        for s in catalog:
            if s.startswith(prefix):
                out["ru"][s] = tr


def main() -> None:
    out: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}

    for name in (
        "quality_fixes.json",
        "hebrew_fixes.json",
        "hebrew_glossary_batch.json",
        "targeted_fixes.json",
        "manual_fixes.json",
    ):
        merge_shard(out, SHARDS / name)

    qf_keys = set(out["he"].keys())
    load_clean_content_long(out)
    load_mitzvot_human(out)

    patches_path = HUMAN / "mitzvot_004_he_patches.json"
    if patches_path.exists():
        merge_shard(out, patches_path)
    halacha_path = HUMAN / "he_halacha_overrides.json"
    if halacha_path.exists():
        merge_shard(out, halacha_path)
    glossary_path = HUMAN / "glossary_overrides.json"
    if glossary_path.exists():
        merge_shard(out, glossary_path)

    # Expansions/educational win over patches and human shards (before manual_he)
    for name in ("mitzvot_004_he_expansions.json", "educational_he_overrides.json"):
        path = HUMAN / name
        if path.exists():
            merge_shard(out, path)

    complete_path = HUMAN / "complete_translations.json"
    if complete_path.exists():
        merge_shard(out, complete_path)

    for key, tr in manual_ru().items():
        out["ru"][key] = tr
    for key, tr in manual_he().items():
        out["he"][key] = tr

    catalog = json.loads((CATALOG / "strings.json").read_text(encoding="utf-8"))["strings"]
    apply_prefix_overrides(out, catalog)

    # ES/FR Super-Special from local_002
    special = (
        "**Special Mitzvah**: Light an oil candle (such as a floating wick in a small cup of oil) for the "
        "elevation of the souls of the \"Neshamot Hagalmudot\" (הנשמות הגלמודות) -the forlorn departed souls "
        "in Heaven who need just a small merit to get into the Garden of Eden (Gan Eden). Rabbis say these poor "
        "souls are just missing a small merit to be let into the eternal pleasure of Gan Eden,  and if you light "
        "a candle in their merit, you could help them get in! Then these souls are extremely grateful, and once "
        "they're in they can pray for you for all your wishes!"
    )
    super_special = special.replace("**Special Mitzvah**", "**Super-Special Mitzvah**")
    local = load_json(HUMAN / "local_002.json")
    for lang in ("es", "fr", "ru"):
        tr = local.get(lang, {}).get(special)
        if tr:
            out[lang][super_special] = tr.replace("**Special Mitzvah**", "**Super-Special Mitzvah**")

    OUT.write_text(json.dumps(out, ensure_ascii=False, indent=2), encoding="utf-8")
    counts = {lang: len(out[lang]) for lang in LANGS}
    print(f"Wrote {OUT.name}: {counts}")


if __name__ == "__main__":
    main()
