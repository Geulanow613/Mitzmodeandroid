#!/usr/bin/env python3
"""Offline batch translation for shards 020-025 using Argos Translate."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

import argostranslate.translate

ROOT = Path(__file__).resolve().parents[1]
BATCHES = ROOT / "data" / "translation-catalog" / "batches"
SHARDS = ROOT / "data" / "translation-catalog" / "shards"
LANGS = ("he", "es", "fr", "ru")

# Halacha terms to preserve (case-sensitive where noted)
GLOSSARY = [
    ("Shabbat", {"he": "שבת", "es": "Shabat", "fr": "Shabbat", "ru": "Шабbat"}),
    ("Shabbos", {"he": "שבת", "es": "Shabat", "fr": "Shabbat", "ru": "Шабbat"}),
    ("Yom Tov", {"he": "יום טוב", "es": "Yom Tov", "fr": "Yom Tov", "ru": "Yom Tov"}),
    ("melacha", {"he": "מלאכה", "es": "melajá", "fr": "melakha", "ru": "melacha"}),
    ("mitzvah", {"he": "מצווה", "es": "mitzvá", "fr": "mitzvah", "ru": "mitzvah"}),
    ("mitzvot", {"he": "מצוות", "es": "mitzvot", "fr": "mitzvot", "ru": "mitzvot"}),
    ("halacha", {"he": "הלכה", "es": "halajá", "fr": "halakha", "ru": "halacha"}),
    ("halachic", {"he": "הלכתי", "es": "halájico", "fr": "halakhique", "ru": "halachic"}),
    ("poskim", {"he": "פוסקים", "es": "poskim", "fr": "poskim", "ru": "poskim"}),
    ("rav", {"he": "רב", "es": "rav", "fr": "rav", "ru": "rav"}),
    ("Hashem", {"he": "ה'", "es": "Hashem", "fr": "Hashem", "ru": "Hashem"}),
    ("Torah", {"he": "תורה", "es": "Torá", "fr": "Torah", "ru": "Тора"}),
    ("Talmud", {"he": "תלמוד", "es": "Talmud", "fr": "Talmud", "ru": "Тalmud"}),
    ("Kiddush", {"he": "קידוש", "es": "Kidush", "fr": "Kiddouch", "ru": "Kiddush"}),
    ("Havdalah", {"he": "הavdalah", "es": "Havdalá", "fr": "Havdalah", "ru": "Havdalah"}),
    ("tefillin", {"he": "תפילין", "es": "tefilín", "fr": "tefillin", "ru": "tefillin"}),
    ("tzitzit", {"he": "ציצית", "es": "tzitzit", "fr": "tzitzit", "ru": "tzitzit"}),
    ("mezuzah", {"he": "מזוזה", "es": "mezuzá", "fr": "mezouza", "ru": "mezuzah"}),
    ("Seder", {"he": "סeder", "es": "Seder", "fr": "Seder", "ru": "Seder"}),
    ("Haggadah", {"he": "הגדה", "es": "Hagadá", "fr": "Haggadah", "ru": "Haggadah"}),
    ("matzah", {"he": "מצה", "es": "matzá", "fr": "matza", "ru": "matzah"}),
    ("chametz", {"he": "חמץ", "es": "jametz", "fr": "hamets", "ru": "chametz"}),
    ("Purim", {"he": "פורים", "es": "Purim", "fr": "Purim", "ru": "Пурим"}),
    ("Sukkot", {"he": "סוכות", "es": "Sucot", "fr": "Souccot", "ru": "Сukkot"}),
    ("Pesach", {"he": "פesach", "es": "Pésaj", "fr": "Pessah", "ru": "Песах"}),
    ("Shavuot", {"he": "שavuot", "es": "Shavuot", "fr": "Chavouot", "ru": "Shavuot"}),
    ("Rosh Hashana", {"he": "ראש השנה", "es": "Rosh Hashaná", "fr": "Roch Hachana", "ru": "Рosh ha-Shana"}),
    ("Yom Kippur", {"he": "יום כיפur", "es": "Yom Kipur", "fr": "Yom Kippour", "ru": "Yom Kippur"}),
    ("Tisha B'Av", {"he": "תשעה באב", "es": "Tisha BeAv", "fr": "Tisha BeAv", "ru": "Тиша бе-Аv"}),
    ("Chol HaMoed", {"he": "חול המועד", "es": "Jol HaMoed", "fr": "Hol HaMoed", "ru": "Hol HaMoed"}),
    ("eruv tavshilin", {"he": "עירוב תavshilin", "es": "eruv tavshilin", "fr": "eruv tavshilin", "ru": "eruv tavshilin"}),
    ("ochel nefesh", {"he": "אוכל נפש", "es": "ochel nefesh", "fr": "ochel nefesh", "ru": "ochel nefesh"}),
    ("Birkat Hamazon", {"he": "ברכת המזון", "es": "Birkat Hamazón", "fr": "Birkat Hamazon", "ru": "Birkat Hamazon"}),
    ("Amidah", {"he": "עמידה", "es": "Amidá", "fr": "Amidah", "ru": "Amidah"}),
    ("Shema", {"he": "שma", "es": "Shema", "fr": "Shema", "ru": "Shema"}),
    ("Hallel", {"he": "הלל", "es": "Halel", "fr": "Hallel", "ru": "Hallel"}),
    ("Tachanun", {"he": "תחנun", "es": "Tajánun", "fr": "Tachanun", "ru": "Tachanun"}),
    ("Sephardi", {"he": "ספרדי", "es": "sefardí", "fr": "séfarade", "ru": "сефард"}),
    ("Ashkenaz", {"he": "אשכנaz", "es": "ashkenazí", "fr": "ashkénaze", "ru": "ашкеназ"}),
    ("Chabad", {"he": "חabad", "es": "Jabad", "fr": "Habad", "ru": "Хabad"}),
    ("Rambam", {"he": "רמב״ם", "es": "Rambam", "fr": "Rambam", "ru": "Рambam"}),
    ("Shulchan Aruch", {"he": "שulchan Aruch", "es": "Shulján Aruj", "fr": "Choulhan Aroukh", "ru": "Shulchan Aruch"}),
    ("kezayit", {"he": "כzayit", "es": "kezayit", "fr": "kezayit", "ru": "kezayit"}),
    ("tzeit", {"he": "צאת הכוכבים", "es": "tzeit", "fr": "tzeit", "ru": "tzeit"}),
    ("mikveh", {"he": "מikve", "es": "mikve", "fr": "mikvé", "ru": "mikve"}),
    ("tzedakah", {"he": "צדקה", "es": "tzedaká", "fr": "tsedaka", "ru": "tzedakah"}),
    ("lashon hara", {"he": "לשon hara", "es": "lashon hara", "fr": "lashon hara", "ru": "lashon hara"}),
    ("Kiddush Hashem", {"he": "קידוש השם", "es": "Kidush Hashem", "fr": "Kiddouch Hashem", "ru": "Kiddush Hashem"}),
    ("teshuvah", {"he": "תשובה", "es": "teshuvá", "fr": "techouva", "ru": "teshuvah"}),
    ("Mincha", {"he": "מנחה", "es": "Minjá", "fr": "Min'ha", "ru": "Mincha"}),
    ("Maariv", {"he": "maariv", "es": "Maariv", "fr": "Maariv", "ru": "Maariv"}),
    ("Shacharit", {"he": "שacharit", "es": "Shajarit", "fr": "Shaharit", "ru": "Shacharit"}),
    ("Musaf", {"he": "musaf", "es": "Musaf", "fr": "Moussaf", "ru": "Musaf"}),
    ("Chanukah", {"he": "חנוכה", "es": "Janucá", "fr": "Hanoucca", "ru": "Ханука"}),
    ("menorah", {"he": "menorah", "es": "menorá", "fr": "menorah", "ru": "menorah"}),
    ("Al HaNissim", {"he": "על הניסים", "es": "Al HaNissim", "fr": "Al HaNissim", "ru": "Al HaNissim"}),
    ("pirsumei nisa", {"he": "pirsumei nisa", "es": "pirsumei nisa", "fr": "pirsumei nisa", "ru": "pirsumei nisa"}),
    ("bracha", {"he": "ברכה", "es": "berajá", "fr": "berakha", "ru": "bracha"}),
    ("brachot", {"he": "ברכot", "es": "berajot", "fr": "berakhot", "ru": "brachot"}),
    ("kashrut", {"he": "כשרות", "es": "kashrut", "fr": "cacherout", "ru": "kashrut"}),
    ("kosher", {"he": "כשר", "es": "kosher", "fr": "cacher", "ru": "kosher"}),
    ("Kohen", {"he": "כהן", "es": "kohen", "fr": "cohen", "ru": "kohen"}),
    ("Kohanim", {"he": "כohanim", "es": "kohanim", "fr": "kohanim", "ru": "kohanim"}),
    ("Beit Hamikdash", {"he": "בית המקדש", "es": "Beit HaMikdash", "fr": "Beit Hamikdash", "ru": "Beit Hamikdash"}),
    ("Gemara", {"he": "גemara", "es": "Guemará", "fr": "Guemara", "ru": "Gemara"}),
    ("Mishnah", {"he": "משנה", "es": "Mishná", "fr": "Mishna", "ru": "Mishnah"}),
    ("Zimmun", {"he": "זimun", "es": "Zimmun", "fr": "Zimmoun", "ru": "Zimmun"}),
    ("Modeh Ani", {"he": "modeh ani", "es": "Mode Ani", "fr": "Mode Ani", "ru": "Modeh Ani"}),
    ("Hagbah", {"he": "hagbah", "es": "Hagbah", "fr": "Hagbah", "ru": "Hagbah"}),
    ("gelilah", {"he": "gelilah", "es": "gelilah", "fr": "gelilah", "ru": "gelilah"}),
    ("kitniyot", {"he": "kitniyot", "es": "kitniot", "fr": "kitniot", "ru": "kitniyot"}),
    ("muktzeh", {"he": "muktzeh", "es": "muktze", "fr": "muktse", "ru": "muktzeh"}),
    ("Shehecheyanu", {"he": "shehecheyanu", "es": "Shehejeyanu", "fr": "Cheheheyanou", "ru": "Shehecheyanu"}),
    ("Yaknehaz", {"he": "Yaknehaz", "es": "Yaknehaz", "fr": "Yaknehaz", "ru": "Yaknehaz"}),
    ("Yaaleh V'Yavo", {"he": "Yaaleh V'Yavo", "es": "Yaaleh V'Yavo", "fr": "Yaaleh V'Yavo", "ru": "Yaaleh V'Yavo"}),
    ("Tashlumin", {"he": "Tashlumin", "es": "Tashlumin", "fr": "Tashlumin", "ru": "Tashlumin"}),
    ("Sefirat HaOmer", {"he": "Sefirat HaOmer", "es": "Sefirat HaOmer", "fr": "Sefirat HaOmer", "ru": "Sefirat HaOmer"}),
    ("Lag BaOmer", {"he": "Lag BaOmer", "es": "Lag BaOmer", "fr": "Lag BaOmer", "ru": "Lag BaOmer"}),
    ("Simchat Torah", {"he": "Simchat Torah", "es": "Simjat Torá", "fr": "Sim'hat Torah", "ru": "Simchat Torah"}),
    ("Shemini Atzeret", {"he": "Shemini Atzeret", "es": "Shemini Atzeret", "fr": "Chemini Atseret", "ru": "Shemini Atzeret"}),
    ("Hoshana Raba", {"he": "Hoshana Raba", "es": "Hoshana Raba", "fr": "Hoshana Raba", "ru": "Hoshana Raba"}),
    ("Arba Minim", {"he": "Arba Minim", "es": "Arba Minim", "fr": "Arba Minim", "ru": "Arba Minim"}),
    ("lulav", {"he": "lulav", "es": "lulav", "fr": "loulav", "ru": "lulav"}),
    ("etrog", {"he": "etrog", "es": "etrog", "fr": "étrog", "ru": "etrog"}),
    ("schach", {"he": "schach", "es": "sejaj", "fr": "sekhakh", "ru": "schach"}),
    ("sukkah", {"he": "sukkah", "es": "sucá", "fr": "soucca", "ru": "sukkah"}),
    ("tevilat keilim", {"he": "tevilat keilim", "es": "tevilat keilim", "fr": "tevilat keilim", "ru": "tevilat keilim"}),
    ("mechirat chametz", {"he": "mechirat chametz", "es": "mejirat jametz", "fr": "mehirat hamets", "ru": "mechirat chametz"}),
    ("biur chametz", {"he": "biur chametz", "es": "biur jametz", "fr": "biour hamets", "ru": "biur chametz"}),
    ("mishloach manot", {"he": "mishloach manot", "es": "mishloach manot", "fr": "michloa'h manot", "ru": "mishloach manot"}),
    ("matanot la'evyonim", {"he": "matanot la'evyonim", "es": "matanot la'evyonim", "fr": "matanot la'evyonim", "ru": "matanot la'evyonim"}),
    ("Megillah", {"he": "Megillah", "es": "Meguilá", "fr": "Meguila", "ru": "Megillah"}),
    ("Selichot", {"he": "Selichot", "es": "Selihot", "fr": "Seli'hot", "ru": "Selichot"}),
    ("shofar", {"he": "shofar", "es": "shofar", "fr": "chofar", "ru": "shofar"}),
    ("tallit", {"he": "tallit", "es": "talit", "fr": "talit", "ru": "tallit"}),
    ("kippah", {"he": "kippah", "es": "kipá", "fr": "kippa", "ru": "kippah"}),
    ("tzniut", {"he": "tzniut", "es": "tzniut", "fr": "tsniout", "ru": "tzniut"}),
    ("Pirkei Avot", {"he": "Pirkei Avot", "es": "Pirkei Avot", "fr": "Pirkei Avot", "ru": "Pirkei Avot"}),
    ("Hitbodedut", {"he": "Hitbodedut", "es": "Hitbodedut", "fr": "Hitbodedout", "ru": "Hitbodedut"}),
    ("Bikur Cholim", {"he": "Bikur Cholim", "es": "Bikur Jolim", "fr": "Bikour Holim", "ru": "Bikur Cholim"}),
    ("Kibud Av Va'em", {"he": "Kibud Av Va'em", "es": "Kibud Av Va'em", "fr": "Kiboud Av Va'em", "ru": "Kibud Av Va'em"}),
    ("Hadlakat Nerot", {"he": "Hadlakat Nerot", "es": "Hadlakat Nerot", "fr": "Hadlakat Nerot", "ru": "Hadlakat Nerot"}),
    ("Netilat Yadayim", {"he": "Netilat Yadayim", "es": "Netilat Yadayim", "fr": "Netilat Yadayim", "ru": "Netilat Yadayim"}),
    ("Birkat HaTorah", {"he": "Birkat HaTorah", "es": "Birkat HaTorah", "fr": "Birkat HaTorah", "ru": "Birkat HaTorah"}),
    ("revi'it", {"he": "revi'it", "es": "revi'it", "fr": "revi'it", "ru": "revi'it"}),
    ("misheyakir", {"he": "misheyakir", "es": "misheyakir", "fr": "misheyakir", "ru": "misheyakir"}),
    ("Genizah", {"he": "Genizah", "es": "Guenizá", "fr": "Gueniza", "ru": "Genizah"}),
    ("Edot HaMizrach", {"he": "Edot HaMizrach", "es": "Edot HaMizrach", "fr": "Edot HaMizrach", "ru": "Edot HaMizrach"}),
    ("Nusach Sefard", {"he": "Nusach Sefard", "es": "Nusaj Sefard", "fr": "Noussah Sefard", "ru": "Nusach Sefard"}),
    ("Chassidim", {"he": "Chassidim", "es": "jasidim", "fr": "'hassidim", "ru": "Chassidim"}),
    ("Arizal", {"he": "Arizal", "es": "Arizal", "fr": "Arizal", "ru": "Arizal"}),
    ("Kabbalah", {"he": "Kabbalah", "es": "Cábala", "fr": "Kabbale", "ru": "Kabbalah"}),
    ("Gematria", {"he": "Gematria", "es": "Guematria", "fr": "Guematria", "ru": "Gematria"}),
    ("Shechinah", {"he": "Shechinah", "es": "Shejiná", "fr": "Chekhina", "ru": "Shechinah"}),
    ("Olam Haba", {"he": "Olam Haba", "es": "Olam Haba", "fr": "Olam Haba", "ru": "Olam Haba"}),
    ("World to Come", {"he": "World to Come", "es": "Mundo Venidero", "fr": "Monde à venir", "ru": "Мир грядущий"}),
    ("bar mitzvah", {"he": "bar mitzvah", "es": "bar mitzvá", "fr": "bar mitzvah", "ru": "bar mitzvah"}),
    ("bat mitzvah", {"he": "bat mitzvah", "es": "bat mitzvá", "fr": "bat mitzvah", "ru": "bat mitzvah"}),
    ("Pesach Sheni", {"he": "Pesach Sheni", "es": "Pésaj Sheni", "fr": "Pessah Sheni", "ru": "Pesach Sheni"}),
    ("Tu B'Shvat", {"he": "Tu B'Shvat", "es": "Tu B'Shvat", "fr": "Tu Bichvat", "ru": "Tu B'Shvat"}),
    ("Tu B'Av", {"he": "Tu B'Av", "es": "Tu B'Av", "fr": "Tu BeAv", "ru": "Tu B'Av"}),
    ("Yom Ha'atzmaut", {"he": "Yom Ha'atzmaut", "es": "Yom Ha'atzmaut", "fr": "Yom Ha'atsmaout", "ru": "Yom Ha'atzmaut"}),
    ("Yom Yerushalayim", {"he": "Yom Yerushalayim", "es": "Yom Yerushalayim", "fr": "Yom Yeroushalayim", "ru": "Yom Yerushalayim"}),
    ("Melave Malka", {"he": "Melave Malka", "es": "Melave Malka", "fr": "Melave Malka", "ru": "Melave Malka"}),
    ("Hamapil", {"he": "Hamapil", "es": "Hamapil", "fr": "Hamapil", "ru": "Hamapil"}),
    ("Birkat Hachamah", {"he": "Birkat Hachamah", "es": "Birkat Hachamah", "fr": "Birkat Hachama", "ru": "Birkat Hachamah"}),
    ("machatzit hashekel", {"he": "machatzit hashekel", "es": "majatzit hashekel", "fr": "mahatsit hashekel", "ru": "machatzit hashekel"}),
    ("shaatnez", {"he": "shaatnez", "es": "shaatnez", "fr": "shaatnez", "ru": "shaatnez"}),
    ("ribbis", {"he": "ribbis", "es": "ribit", "fr": "ribbit", "ru": "ribbis"}),
    ("Heter Iska", {"he": "Heter Iska", "es": "Heter Iska", "fr": "Heter Iska", "ru": "Heter Iska"}),
    ("Bal Tashchit", {"he": "Bal Tashchit", "es": "Bal Tashjit", "fr": "Bal Tash'hit", "ru": "Bal Tashchit"}),
    ("Parah Adumah", {"he": "Parah Adumah", "es": "Pará Adumá", "fr": "Para Adouma", "ru": "Parah Adumah"}),
    ("Halvayat HaMeit", {"he": "Halvayat HaMeit", "es": "Halvayat HaMeit", "fr": "Halvayat HaMeit", "ru": "Halvayat HaMeit"}),
    ("Kiddush Levana", {"he": "Kiddush Levana", "es": "Kidush Levana", "fr": "Kiddouch Levana", "ru": "Kiddush Levana"}),
    ("Tikkun Leil Shavuot", {"he": "Tikkun Leil Shavuot", "es": "Tikkun Leil Shavuot", "fr": "Tikkun Leil Shavuot", "ru": "Tikkun Leil Shavuot"}),
    ("Simchat Yom Tov", {"he": "Simchat Yom Tov", "es": "Simjat Yom Tov", "fr": "Sim'hat Yom Tov", "ru": "Simchat Yom Tov"}),
    ("Oneg Shabbat", {"he": "Oneg Shabbat", "es": "Oneg Shabat", "fr": "Oneg Shabbat", "ru": "Oneg Shabbat"}),
    ("Pesukei DeZimra", {"he": "Pesukei DeZimra", "es": "Pesukei DeZimra", "fr": "Pessoukei DeZimra", "ru": "Pesukei DeZimra"}),
    ("Birkat Kohanim", {"he": "Birkat Kohanim", "es": "Birkat Kohanim", "fr": "Birkat Kohanim", "ru": "Birkat Kohanim"}),
    ("Yizkor", {"he": "Yizkor", "es": "Yizkor", "fr": "Yizkor", "ru": "Yizkor"}),
    ("Kol Nidrei", {"he": "Kol Nidrei", "es": "Kol Nidrei", "fr": "Kol Nidrei", "ru": "Kol Nidrei"}),
    ("Ne'ilah", {"he": "Ne'ilah", "es": "Ne'ilah", "fr": "Ne'ila", "ru": "Ne'ilah"}),
    ("Kinot", {"he": "Kinot", "es": "Kinot", "fr": "Kinot", "ru": "Kinot"}),
    ("Megillat Ruth", {"he": "Megillat Ruth", "es": "Meguilat Rut", "fr": "Meguila Ruth", "ru": "Megillat Ruth"}),
    ("Talmud Torah", {"he": "Talmud Torah", "es": "Talmud Torá", "fr": "Talmud Torah", "ru": "Talmud Torah"}),
    ("Shnayim Mikra v'Echad Targum", {"he": "Shnayim Mikra v'Echad Targum", "es": "Shnayim Mikra v'Echad Targum", "fr": "Shnayim Mikra v'Echad Targum", "ru": "Shnayim Mikra v'Echad Targum"}),
    ("Taanit Esther", {"he": "Taanit Esther", "es": "Taanit Esther", "fr": "Taanit Esther", "ru": "Taanit Esther"}),
    ("Taanit Bechorot", {"he": "Taanit Bechorot", "es": "Taanit Bejorot", "fr": "Taanit Bechorot", "ru": "Taanit Bechorot"}),
    ("Fast of Gedaliah", {"he": "Fast of Gedaliah", "es": "Ayuno de Gedaliah", "fr": "Jeûne de Gedalia", "ru": "Пост Гедaliah"}),
    ("Three Weeks", {"he": "Three Weeks", "es": "Tres Semanas", "fr": "Trois semaines", "ru": "Три недели"}),
    ("Nine Days", {"he": "Nine Days", "es": "Nueve Días", "fr": "Neuf jours", "ru": "Девять дней"}),
    ("Rosh Chodesh", {"he": "Rosh Chodesh", "es": "Rosh Jodesh", "fr": "Rosh 'Hodesh", "ru": "Rosh Chodesh"}),
    ("machzor", {"he": "machzor", "es": "majzor", "fr": "mahzor", "ru": "machzor"}),
    ("siddur", {"he": "siddur", "es": "sidur", "fr": "sidour", "ru": "siddur"}),
    ("Chumash", {"he": "Chumash", "es": "Jumash", "fr": "'houmach", "ru": "Chumash"}),
    ("Tanach", {"he": "Tanach", "es": "Tanaj", "fr": "Tanakh", "ru": "Tanach"}),
    ("Chofetz Chaim", {"he": "Chofetz Chaim", "es": "Jofetz Jaim", "fr": "Hofets 'Haïm", "ru": "Chofetz Chaim"}),
    ("Mishnah Berurah", {"he": "Mishnah Berurah", "es": "Mishná Berurá", "fr": "Mishna Beroura", "ru": "Mishnah Berurah"}),
    ("Peninei Halakha", {"he": "Peninei Halakha", "es": "Peninei Halakha", "fr": "Peninei Halakha", "ru": "Peninei Halakha"}),
    ("Yalkut Yosef", {"he": "Yalkut Yosef", "es": "Yalkut Yosef", "fr": "Yalkout Yossef", "ru": "Yalkut Yosef"}),
    ("Ben Ish Chai", {"he": "Ben Ish Chai", "es": "Ben Ish Jai", "fr": "Ben Ich 'Haï", "ru": "Ben Ish Chai"}),
    ("Rav Ovadia Yosef", {"he": "Rav Ovadia Yosef", "es": "Rav Ovadia Yosef", "fr": "Rav Ovadia Yossef", "ru": "Rav Ovadia Yosef"}),
    ("Igrot Moshe", {"he": "Igrot Moshe", "es": "Igrot Moshe", "fr": "Igrot Moshe", "ru": "Igrot Moshe"}),
    ("Chazon Ish", {"he": "Chazon Ish", "es": "Jazon Ish", "fr": "Hazon Ish", "ru": "Chazon Ish"}),
    ("Rabbi Nachman", {"he": "Rabbi Nachman", "es": "Rabí Najmán", "fr": "Rabbi Nahman", "ru": "Rabbi Nachman"}),
    ("Breslov", {"he": "Breslov", "es": "Breslov", "fr": "Breslev", "ru": "Breslov"}),
    ("Likutei Moharan", {"he": "Likutei Moharan", "es": "Likutei Moharan", "fr": "Likoutei Moharan", "ru": "Likutei Moharan"}),
    ("Tikkun HaKlali", {"he": "Tikkun HaKlali", "es": "Tikkun HaKlali", "fr": "Tikkun HaKlali", "ru": "Tikkun HaKlali"}),
    ("Tehillim", {"he": "Tehillim", "es": "Tehilim", "fr": "Tehillim", "ru": "Tehillim"}),
    ("Psalm", {"he": "Psalm", "es": "Salmo", "fr": "Psaume", "ru": "Псalm"}),
    ("Amalek", {"he": "Amalek", "es": "Amalek", "fr": "Amalek", "ru": "Amalek"}),
    ("Manna", {"he": "Manna", "es": "Maná", "fr": "Manne", "ru": "Manna"}),
    ("Mount Sinai", {"he": "Mount Sinai", "es": "Monte Sinaí", "fr": "Mont Sinaï", "ru": "Mount Sinai"}),
    ("Exodus", {"he": "Exodus", "es": "Éxodo", "fr": "Exode", "ru": "Exodus"}),
    ("Egypt", {"he": "Egypt", "es": "Egipto", "fr": "Égypte", "ru": "Egypt"}),
    ("Jerusalem", {"he": "Jerusalem", "es": "Jerusalén", "fr": "Jérusalem", "ru": "Jerusalem"}),
    ("Israel", {"he": "Israel", "es": "Israel", "fr": "Israël", "ru": "Israel"}),
    ("Diaspora", {"he": "Diaspora", "es": "Diáspora", "fr": "Diaspora", "ru": "Diaspora"}),
    ("G-d", {"he": "ה'", "es": "D-os", "fr": "D.ieu", "ru": "G-d"}),
    ("G‑d", {"he": "ה'", "es": "D-os", "fr": "D.ieu", "ru": "G-d"}),
    ("Ado-nai", {"he": "Ado-nai", "es": "Ado-nai", "fr": "Ado-naï", "ru": "Ado-nai"}),
    ("Adonai", {"he": "Adonai", "es": "Adonai", "fr": "Adonaï", "ru": "Adonai"}),
    ("Leviticus", {"he": "Leviticus", "es": "Levítico", "fr": "Lévitique", "ru": "Leviticus"}),
    ("Deuteronomy", {"he": "Deuteronomy", "es": "Deuteronomio", "fr": "Deutéronome", "ru": "Deuteronomy"}),
    ("Exodus", {"he": "Exodus", "es": "Éxodo", "fr": "Exode", "ru": "Exodus"}),
    ("Numbers", {"he": "Numbers", "es": "Números", "fr": "Nombres", "ru": "Numbers"}),
    ("Genesis", {"he": "Genesis", "es": "Génesis", "fr": "Genèse", "ru": "Genesis"}),
    ("Devarim", {"he": "Devarim", "es": "Devarim", "fr": "Devarim", "ru": "Devarim"}),
    ("Bereishit", {"he": "Bereishit", "es": "Bereshit", "fr": "Bereshit", "ru": "Bereishit"}),
    ("Micah", {"he": "Micah", "es": "Miqueas", "fr": "Michée", "ru": "Micah"}),
    ("Esther", {"he": "Esther", "es": "Ester", "fr": "Esther", "ru": "Esther"}),
    ("Ruth", {"he": "Ruth", "es": "Rut", "fr": "Ruth", "ru": "Ruth"}),
    ("Job", {"he": "Job", "es": "Job", "fr": "Job", "ru": "Job"}),
    ("Lamentations", {"he": "Lamentations", "es": "Lamentaciones", "fr": "Lamentations", "ru": "Lamentations"}),
    ("Eichah", {"he": "Eichah", "es": "Eijá", "fr": "Eikha", "ru": "Eichah"}),
    ("Iyov", {"he": "Iyov", "es": "Iyov", "fr": "Iyov", "ru": "Iyov"}),
    ("Amen", {"he": "Amen", "es": "Amén", "fr": "Amen", "ru": "Amen"}),
    ("Shalom", {"he": "Shalom", "es": "Shalom", "fr": "Shalom", "ru": "Shalom"}),
    ("Mazel Tov", {"he": "Mazel Tov", "es": "Mazal Tov", "fr": "Mazal Tov", "ru": "Mazel Tov"}),
    ("L'shanah tovah", {"he": "L'shanah tovah", "es": "L'shanah tová", "fr": "L'shanah tova", "ru": "L'shanah tovah"}),
    ("Gam zu l'tovah", {"he": "Gam zu l'tovah", "es": "Gam zu l'tová", "fr": "Gam zu l'tova", "ru": "Gam zu l'tovah"}),
    ("Nachum Ish Gamzu", {"he": "Nachum Ish Gamzu", "es": "Najum Ish Gamzu", "fr": "Nahoum Ish Gamzou", "ru": "Nachum Ish Gamzu"}),
    ("Mitz Mode", {"he": "Mitz Mode", "es": "Mitz Mode", "fr": "Mitz Mode", "ru": "Mitz Mode"}),
]

PLACEHOLDER_RE = re.compile(
    r"(\$\{[^{}]+\}|\$[A-Za-z_][A-Za-z0-9_]*|\[[^\]]+\])"
)
HEBREW_RE = re.compile(r"[\u0590-\u05ff]")


def is_hebrew_source(text: str) -> bool:
    hebrew = sum(1 for c in text if "\u0590" <= c <= "\u05ff")
    latin = sum(1 for c in text if c.isascii() and c.isalpha())
    return hebrew > 30 and latin < max(20, hebrew // 4)


def protect(text: str) -> tuple[str, list[tuple[str, str]]]:
    """Replace placeholders and markdown links with tokens."""
    tokens: list[tuple[str, str]] = []
    counter = 0

    def repl(m: re.Match[str]) -> str:
        nonlocal counter
        token = f"__PH{counter}__"
        tokens.append((token, m.group(0)))
        counter += 1
        return token

    out = PLACEHOLDER_RE.sub(repl, text)
    # protect markdown links
    link_re = re.compile(r"\[([^\]]+)\]\(([^)]+)\)")

    def link_repl(m: re.Match[str]) -> str:
        nonlocal counter
        token = f"__PH{counter}__"
        tokens.append((token, m.group(0)))
        counter += 1
        return token

    out = link_re.sub(link_repl, out)
    return out, tokens


def restore(text: str, tokens: list[tuple[str, str]]) -> str:
    for token, original in tokens:
        text = text.replace(token, original)
    return text


def apply_glossary(text: str, lang: str) -> str:
    for term, trans in GLOSSARY:
        if lang in trans:
            text = re.sub(rf"\b{re.escape(term)}\b", trans[lang], text)
    return text


def get_translator(target: str):
    en = next(l for l in argostranslate.translate.get_installed_languages() if l.code == "en")
    tgt = next(l for l in argostranslate.translate.get_installed_languages() if l.code == target)
    return en.get_translation(tgt)


def translate_text(text: str, lang: str, translator) -> str:
    if is_hebrew_source(text) and lang == "he":
        return text
    protected, tokens = protect(text)
    # Split on newlines to preserve structure
    parts = protected.split("\n")
    translated_parts = []
    for part in parts:
        if not part.strip():
            translated_parts.append(part)
            continue
        # Don't translate lines that are mostly Hebrew
        if sum(1 for c in part if HEBREW_RE.match(c)) > len(part) * 0.4:
            translated_parts.append(part)
            continue
        try:
            tr = translator.translate(part)
        except Exception:
            tr = part
        translated_parts.append(tr)
    result = restore("\n".join(translated_parts), tokens)
    return apply_glossary(result, lang)


def build_shard(batch_num: int, translators: dict) -> dict[str, dict[str, str]]:
    batch_path = BATCHES / f"batch_{batch_num:03d}.json"
    strings = json.loads(batch_path.read_text(encoding="utf-8"))["strings"]
    shard: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    for i, s in enumerate(strings):
        print(f"  batch_{batch_num:03d} [{i+1}/{len(strings)}]", file=sys.stderr)
        for lang in LANGS:
            shard[lang][s] = translate_text(s, lang, translators[lang])
    return shard


def main() -> None:
    SHARDS.mkdir(parents=True, exist_ok=True)
    translators = {lang: get_translator(lang) for lang in LANGS}
    total = 0
    for n in range(20, 26):
        print(f"Translating batch_{n:03d}...", file=sys.stderr)
        shard = build_shard(n, translators)
        out = SHARDS / f"batch_{n:03d}.json"
        out.write_text(json.dumps(shard, ensure_ascii=False, indent=2), encoding="utf-8")
        count = len(json.loads((BATCHES / f"batch_{n:03d}.json").read_text(encoding="utf-8"))["strings"])
        total += count
        print(f"Wrote {out.name}: {count} keys x 4 langs")
    print(f"Grand total: {total} strings ({total * 4} translation entries)")


if __name__ == "__main__":
    main()
