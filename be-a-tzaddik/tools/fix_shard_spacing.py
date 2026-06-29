#!/usr/bin/env python3
"""Post-process shard files: fix spacing around preserved halacha terms."""
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
LANGS = ("he", "es", "fr", "ru")

TERMS = sorted(
    {
        "G-d", "Hashem", "Shulchan Aruch", "Peninei Halakha", "Birkot HaShachar", "Birchot HaShachar",
        "Beit HaMikdash", "Yom Tov", "Rosh Hashana", "Yom Kippur", "Rosh Chodesh",
        "Shacharit", "Maariv", "Mincha", "Musaf", "Amidah", "Shema", "Hallel", "Tachanun",
        "Bentching", "Birkat Hamazon", "Kiddush", "Havdalah", "Pesach", "Sukkot", "Shavuot",
        "Chanukah", "Purim", "Megillah", "Seder", "Haggadah", "Mishnah", "Talmud", "Gemara",
        "Rambam", "Rashi", "Chabad", "Ashkenaz", "Sephardi", "Ashkenazi", "Sephardim",
        "minyan", "mitzvah", "mitzvot", "halacha", "halachic", "rav", "minhag", "bracha",
        "tefillin", "mezuzah", "tzitzit", "kashrut", "shechita", "chametz", "matzah", "maror",
        "siddur", "machzor", "Chumash", "Tehillim", "Tanya", "Mussar", "Chassidut", "Chasidut",
        "emunah", "bitachon", "teshuvah", "Shabbat", "Motzei Shabbat", "Yamim Noraim", "Omer",
        "kohen", "kohanim", "levi", "leviim", "aliyah", "oleh", "baal koreh", "eruv", "muktzeh",
        "blech", "cholent", "mikveh", "niddah", "Kaddish", "Selichot", "Kol Nidrei", "Ne'ilah",
        "Simchat Torah", "Shemini Atzeret", "Arba Minim", "lulav", "etrog", "sukkah", "schach",
        "shofar", "tekiah", "shevarim", "teruah", "Modeh Ani", "Hamapil", "Yaaleh V'yavo",
        "Retzei", "Al HaNissim", "Shehecheyanu", "Hamotzi", "netilat yadayim", "tashlumin",
        "Shnayim Mikra", "Targum Onkelos", "Mishneh Torah", "Mishnah Berurah", "Chofetz Chaim",
        "Pirkei Avot", "Olam HaBa", "Gan Eden", "Klal Yisrael", "Rema", "Shulchan Arukh",
        "Kitzur Shulchan Arukh", "Edot HaMizrach", "Nusach Ari", "Yalkut Yosef", "Maggid",
        "Korech", "charoset", "zeroa", "beitzah", "karpas", "Dayeinu", "Mah Nishtanah",
        "pirsumei nisa", "shamash", "pikuach nefesh", "geneivat da'at", "ona'at devarim",
        "lashon hara", "rechilut", "chilul Hashem", "kiddush Hashem", "shomer negiah", "yichud",
        "machloket", "l'chatchila", "bedieved", "patur", "chiyuv", "chatzos", "zmanim",
        "yad soledet bo", "bishul", "koteiv", "hotza'ah", "mavir", "borer", "oneg Shabbat",
        "seudah shlishit", "zemiroth", "Krias Shema", "Borei Pri HaEtz", "Borei Pri HaGafen",
        "borei nefashot", "al hamichya", "lechem mishneh", "hafrashat challah", "maaser kesafim",
        "Kol Chamira", "bedikat chametz", "gebrochts", "Chol HaMoed", "Three Weeks", "Nine Days",
        "Tisha B'Av", "Lag BaOmer", "Yom Ha'atzmaut", "Yom HaShoah", "refuah shleimah",
        "parnassa", "nachat", "Hakarat HaTov", "Hagbah", "Shekhinah", "neshama", "neshama yeteira",
        "yetzer hatov", "yetzer hara", "kavannah", "Amen", "Chitas", "Hayom Yom", "gemach",
        "parsha", "Ushpizin", "besamim", "chamar medina", "treif", "kezayit", "revi'it",
        "tumah", "posek", "chazzan", "shaliach tzibur", "trop", "hakafot", "selichot",
        "Vehu Rachum", "Shir HaMaalot", "Bereishit", "Shemot", "Vayikra", "Bamidbar", "Devarim",
        "Sefer Torah", "bar mitzvah", "bat mitzvah", "brit milah", "chuppah", "aveilut",
        "shiva", "shloshim", "yahrzeit", "Yizkor", "Kaparot", "Hoshanot", "Hoshana Raba",
        "Purim Meshulash", "Shushan Purim", "Matan Torah", "chinuch", "tzedakah", "Elokei Neshama",
        "Aleinu", "Karpas", "Levi", "Koteiv", "Hotza'ah", "Pesukei d'Zimra", "Hafrashat challah",
        "Bal yimatzei", "Korbanot", "Bishul", "Chumash", "baal koreh", "Chatzos", "Gan Eden",
        "Kashrut", "Lechem mishneh", "Maror", "Cholent", "Hoshanot", "mayim achronim",
        "Purim", "Shema al HaMitah", "Tehillim", "Tefilat Nedavah", "chesed", "patur",
    },
    key=len,
    reverse=True,
)

WORD = r"[\w\u0590-\u05FF'\-]"


def fix_text(text: str) -> str:
    for term in TERMS:
        text = re.sub(rf"({WORD})({re.escape(term)})", r"\1 \2", text)
        text = re.sub(rf"({re.escape(term)})({WORD})", r"\1 \2", text)
    text = re.sub(r"  +", " ", text)
    return text


def main() -> None:
    for batch_num in range(14, 20):
        path = ROOT / "shards" / f"batch_{batch_num:03d}.json"
        shard = json.loads(path.read_text(encoding="utf-8"))
        for lang in LANGS:
            for key in list(shard[lang]):
                shard[lang][key] = fix_text(shard[lang][key])
        path.write_text(json.dumps(shard, ensure_ascii=False, indent=2), encoding="utf-8")
        print(f"fixed batch_{batch_num:03d}.json")


if __name__ == "__main__":
    main()
