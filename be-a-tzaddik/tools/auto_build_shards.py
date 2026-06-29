#!/usr/bin/env python3
"""Build translation shards 014-019 — local Argos, segment-based term preservation."""
from __future__ import annotations

import json
import re
from pathlib import Path

import argostranslate.translate

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
LANGS = ("he", "es", "fr", "ru")
HEBREW_RE = re.compile(r"[\u0590-\u05FF]")
VAR_RE = re.compile(r"\$\{[^}]+\}|\$[A-Za-z_][A-Za-z0-9_]*")

PRESERVE = sorted(
    {
        "Shulchan Aruch", "Peninei Halakha", "Birkot HaShachar", "Birchot HaShachar",
        "Beit HaMikdash", "Yom Tov", "Rosh Hashana", "Yom Kippur", "Rosh Chodesh",
        "Shacharit", "Maariv", "Mincha", "Musaf", "Amidah", "Shema", "Hallel",
        "Tachanun", "Bentching", "Birkat Hamazon", "Kiddush", "Havdalah",
        "Pesach", "Sukkot", "Shavuot", "Chanukah", "Purim", "Megillah", "Seder",
        "Haggadah", "Mishnah", "Talmud", "Gemara", "Rambam", "Rashi", "Chabad",
        "Ashkenaz", "Sephardi", "Ashkenazi", "Sephardim", "minyan", "mitzvah", "mitzvot",
        "halacha", "halachic", "rav", "poskim", "minhag", "bracha", "brachot",
        "tefillin", "mezuzah", "tzitzit", "kashrut", "shechita", "chametz", "matzah",
        "maror", "afikoman", "siddur", "machzor", "Chumash", "Tehillim", "Tanya",
        "Mussar", "Chassidut", "Chasidut", "emunah", "bitachon", "teshuvah",
        "Shabbat", "Motzei Shabbat", "Yamim Noraim", "Omer", "Sefirat HaOmer",
        "kohen", "kohanim", "levi", "leviim", "aliyah", "oleh", "baal koreh",
        "eruv", "muktzeh", "blech", "cholent", "mikveh", "niddah", "Kaddish",
        "Selichot", "Kol Nidrei", "Ne'ilah", "Simchat Torah", "Shemini Atzeret",
        "Arba Minim", "lulav", "etrog", "sukkah", "schach", "shofar", "tekiah",
        "shevarim", "teruah", "Modeh Ani", "Hamapil", "Yaaleh V'yavo", "Retzei",
        "Al HaNissim", "Shehecheyanu", "Hamotzi", "netilat yadayim", "tashlumin",
        "Shnayim Mikra", "Targum Onkelos", "Mishneh Torah", "Mishnah Berurah",
        "Chofetz Chaim", "Pirkei Avot", "Igrot Moshe", "Olam HaBa", "Gan Eden",
        "Klal Yisrael", "IDF", "Sefaria", "OU", "OK", "Rema", "Rama", "SA O.C.",
        "Shulchan Arukh", "Kitzur Shulchan Arukh", "Edot HaMizrach", "Nusach Ari",
        "Nusach Ashkenaz", "Yalkut Yosef", "Bava Metzia", "Arachin", "Berachot",
        "Vayikra Rabbah", "Yesodei HaTorah", "Hilchot Deot", "Hilchot Avodat Kochavim",
        "Gemilut chasadim", "tzedakah", "chinuch", "bar mitzvah", "bat mitzvah",
        "brit milah", "chuppah", "kiddushin", "nisuin", "aveilut", "shiva", "shloshim",
        "yahrzeit", "Yizkor", "Kaparot", "Hoshanot", "Hoshana Raba", "Purim Meshulash",
        "Shushan Purim", "Matan Torah", "Maggid", "Korech", "charoset", "zeroa",
        "beitzah", "karpas", "Dayeinu", "Mah Nishtanah", "pirsumei nisa", "shamash",
        "pikuach nefesh", "geneivat da'at", "ona'at devarim", "lashon hara", "rechilut",
        "chilul Hashem", "kiddush Hashem", "shomer negiah", "yichud", "taharat hamishpacha",
        "machloket", "l'chatchila", "bedieved", "patur", "chiyuv", "chatzos",
        "zmanim", "sha'ah zmanit", "plag hamincha", "alot hashachar", "misheyakir",
        "yad soledet bo", "bishul", "koteiv", "hotza'ah", "mavir", "borer",
        "oneg Shabbat", "seudah shlishit", "zemiroth", "Krias Shema", "Borei Pri HaEtz",
        "Borei Pri HaGafen", "borei nefashot", "al hamichya", "Me'ein Shalosh",
        "lechem mishneh", "hafrashat challah", "maaser kesafim", "Kol Chamira",
        "bedikat chametz", "mechirat chametz", "biur chametz", "gebrochts", "kitniyot",
        "Chol HaMoed", "Three Weeks", "Nine Days", "Tisha B'Av", "Lag BaOmer",
        "Yom Ha'atzmaut", "Yom HaShoah", "refuah shleimah", "parnassa", "nachat",
        "Hakarat HaTov", "Hagbah", "gelilah", "STaM", "sofer", "klaf", "Shaddai",
        "Shekhinah", "neshama", "neshama yeteira", "yetzer hatov", "yetzer hara",
        "kavannah", "Amen", "Keil Melech Ne'eman", "Chitas", "Hayom Yom", "Daf Yomi",
        "gemach", "parsha", "parashah", "Ushpizin", "ushpizin", "besamim", "Yaknehaz",
        "chamar medina", "treif", "melicha", "kezayit", "revi'it", "k'dei achilat pras",
        "ruach ra'ah", "tumah", "taharah", "posek", "magid", "chazzan", "shaliach tzibur",
        "gabbai", "trop", "ta'amei hamikra", "hakafot", "kinot", "selichot",
        "Vehu Rachum", "nefilat apayim", "Shir HaMaalot", "Psalm", "Bereishit", "Shemot",
        "Vayikra", "Bamidbar", "Devarim", "Moriah", "atzei chayim", "Sefer Torah",
        "Shabbat 25b", "Shabbat 118a", "G-d", "Hashem",
    },
    key=len,
    reverse=True,
)

HEBREW_GLOSS: dict[str, str] = {
    "בימים שאין בהם תחנון אומרים:\n\nשיר המעלות בשוב יהוה את שיבת ציון היינו כחלמים. אז ימלא שחוק פינו ולשוננו רנה אז יאמרו בגוים הגדיל יהוה לעשות עם אלה. הגדיל יהוה לעשות עמנו היינו שמחים. שובה יהוה את שביתנו כאפיקים בנגב. הזרעים בדמעה ברנה יקצרו. הלוך ילך ובכה נשא משך הזרע בא יבוא ברנה נשא אלמתיו":
        "On days when Tachanun is omitted, we say Shir HaMa'alot (Psalm 126): When the Lord returns the captivity of Zion we were like dreamers...",
    "בימי מרדכי ואסתר בשושן הבירה כשעמד עליהם המן הרשע בקש להשמיד להרוג לאבד את-כל-היהודים מנער ועד זקן טף ונשים ביום אחד בשלשה עשר לחדש שנים עשר הוא חדש אדר וללם לבוז ואתה ברחמיך הרבים הפרת את עצתו וקלקלת את מחשבתו והשבות-לו גמולו בראשו ותלו אותו ואת בניו על העץ ועשית עמהם נסים ונפלאות ונודה לשמך הגדול סלה.":
        "In the days of Mordechai and Esther in Shushan, wicked Haman sought to destroy all Jews; You in Your great mercy foiled his plan. We thank Your great Name, Selah.",
    "ברוך אתה יי אלהינו, מלך העולם, האל אבינו, מלכנו, אדירנו, בוראנו, גאלנו, יוצרנו, קדושנו קדוש יעקב, רוענו רועה ישראל, המלך הטוב והמיטיב לכל, שבכל יום ויום הוא היטיב, הוא מיטיב, הוא ייטיב לנו, הוא גמלנו, הוא גומלנו, הוא יגמלנו לעד, לחן ולחסד ולרחמים ולרוח הצלה והצלחה, ברכה וישועה, נחמה פרנסה וכלכלה ורחמים וחיים ושלום, וכל טוב; ומכל טוב לעולם אל יחסרנו.":
        "Blessed are You, Lord our God — our Father, King, Creator, Redeemer... Who daily bestows good; grant grace, kindness, mercy, salvation, blessing, livelihood, life and peace.",
    "ברוך אתה יהוה אלהינו מלך העולם הזן את העולם כלו בטובו בחן בחסד וברחמים, הוא נתן לחם לכל-בשר כי לעולם חסדו ובטובו הגדול תמיד לא חסר לנו ואל יחסר לנו מזון (תמיד) לעולם ועד בעבור שמו הגדול כי הוא אל זן ומפרנס לכל ומטיב לכל ומכין מזון לכל-בריותיו אשר ברא ברוך אתה יי הזן את הכל.":
        "Blessed are You, Lord our God, who feeds the entire world in goodness, grace, kindness and mercy... Blessed are You, Lord, who feeds all.",
    "יראו את יי קדשיו, כי אין מחסור ליראיו. כפירים רשו ורעבו, ודרשי יי לא יחסרו כל טוב. הודו ליי כי טוב, כי לעולם חסדו. פותח את ידך, ומשביע לכל חי רצון. ברוך הגבר אשר יבטח ביי, והיה יי מבטחו. נער הייתי גם זקנתי, ולא ראיתי צדיק נעזב, וזרעו מבקש לחם. יי עז לעמו יתן, יי יברך את עמו בשלום.":
        "Fear the Lord, you His holy ones... The Lord gives strength to His people; the Lord blesses His people with peace (Psalm 34).",
}

TRANSLATORS: dict[str, object] = {}


def keep_spans(text: str) -> list[tuple[int, int, str]]:
    candidates: list[tuple[int, int, str]] = []
    for term in PRESERVE:
        start = 0
        while True:
            idx = text.find(term, start)
            if idx == -1:
                break
            candidates.append((idx, idx + len(term), term))
            start = idx + len(term)
    for m in VAR_RE.finditer(text):
        candidates.append((m.start(), m.end(), m.group(0)))
    candidates.sort(key=lambda x: (-(x[1] - x[0]), x[0]))
    used: set[int] = set()
    merged: list[tuple[int, int, str]] = []
    for s, e, val in candidates:
        if any(i in used for i in range(s, e)):
            continue
        merged.append((s, e, val))
        used.update(range(s, e))
    merged.sort(key=lambda x: x[0])
    return merged


def translate_preserving(text: str, lang: str, cache: dict) -> str:
    key = (text, lang)
    if key in cache:
        return cache[key]
    spans = keep_spans(text)
    if not spans:
        out = translate_chunk(text, lang, cache)
        cache[key] = out
        return out
    parts: list[str] = []
    pos = 0
    for s, e, keep in spans:
        if s > pos:
            parts.append(translate_preserving(text[pos:s], lang, cache))
        parts.append(keep)
        pos = e
    if pos < len(text):
        parts.append(translate_preserving(text[pos:], lang, cache))
    out = "".join(parts)
    cache[key] = out
    return out


def get_tr(lang: str):
    if lang not in TRANSLATORS:
        TRANSLATORS[lang] = argostranslate.translate.get_translation_from_codes("en", lang)
    return TRANSLATORS[lang]


def translate_chunk(chunk: str, lang: str, cache: dict) -> str:
    if not chunk or not chunk.strip():
        return chunk
    key = (chunk, lang)
    if key in cache:
        return cache[key]
    out = get_tr(lang).translate(chunk)
    if lang == "he":
        out = out.replace("G-d", "ה'")
    elif lang == "fr":
        out = out.replace("G-d", "D.")
    elif lang == "es":
        out = out.replace("G-d", "Dios")
    elif lang == "ru":
        out = out.replace("G-d", "Б-г")
    cache[key] = out
    return out


    stripped = s.lstrip()
    return bool(stripped) and bool(HEBREW_RE.match(stripped[0]))


def is_hebrew_key(s: str) -> bool:
    stripped = s.lstrip()
    return bool(stripped) and bool(HEBREW_RE.match(stripped[0]))


def main() -> None:
    cache: dict = {}
    out_dir = ROOT / "shards"
    out_dir.mkdir(parents=True, exist_ok=True)
    grand = 0

    for batch_num in range(14, 20):
        batch = json.loads((ROOT / "batches" / f"batch_{batch_num:03d}.json").read_text(encoding="utf-8"))
        shard = {lang: {} for lang in LANGS}
        print(f"batch_{batch_num:03d}...", flush=True)

        for idx, s in enumerate(batch["strings"], 1):
            if is_hebrew_key(s):
                shard["he"][s] = s
                gloss = HEBREW_GLOSS.get(s, "Sacred Hebrew liturgical text (recited in Hebrew).")
                for lang in ("es", "fr", "ru"):
                    shard[lang][s] = translate_preserving(gloss, lang, cache)
            else:
                for lang in LANGS:
                    shard[lang][s] = translate_preserving(s, lang, cache)
            if idx % 20 == 0:
                print(f"  {idx}/80", flush=True)

        path = out_dir / f"batch_{batch_num:03d}.json"
        path.write_text(json.dumps(shard, ensure_ascii=False, indent=2), encoding="utf-8")
        n = len(shard["he"])
        grand += n
        print(f"batch_{batch_num:03d}.json: {n} keys x 4 langs", flush=True)

    print(f"TOTAL: {grand} strings", flush=True)


if __name__ == "__main__":
    main()
