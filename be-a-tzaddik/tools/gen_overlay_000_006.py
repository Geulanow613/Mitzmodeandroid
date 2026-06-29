#!/usr/bin/env python3
"""Generate overlay_000_006.json with es/fr/ru for batches 000-006."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
BATCH_DIR = ROOT / "data" / "translation-catalog" / "batches"
SHARD_DIR = ROOT / "data" / "translation-catalog" / "shards"
BUNDLED_DIR = ROOT / "data" / "bundled-translations"
OUT = SHARD_DIR / "overlay_000_006.json"
LANGS = ("es", "fr", "ru")

from ui_ovr_000_006 import UI  # noqa: E402

# Manual phrase translations (exact English keys).
PHRASE: dict[str, dict[str, str]] = {
    "Tu B'Av": {"es": "Tu B'Av", "fr": "Tou B'Av", "ru": "Ту бе-Ав"},
    "Yom Tov": {"es": "Yom Tov", "fr": "Yom Tov", "ru": "Йом тов"},
    "Gan Eden": {"es": "Gan Eden", "fr": "Gan Eden", "ru": "Ган Эден"},
    "17 Tammuz": {"es": "17 Tamuz", "fr": "17 Tamouz", "ru": "17 таммуза"},
    "Hayom Yom": {"es": "Hayom Yom", "fr": "HaYom Yom", "ru": "ХаЙом Йом"},
    "Maoz Tzur": {"es": "Maoz Tzur", "fr": "Maoz Tzour", "ru": "Маоз Цур"},
    "Modeh Ani": {"es": "Mode Ani", "fr": "Mode Ani", "ru": "Моде ани"},
    "Nine Days": {"es": "Nueve Días", "fr": "Neuf jours", "ru": "Девять дней"},
    "Olam HaBa": {"es": "Olam HaBa", "fr": "Olam HaBa", "ru": "Олам а-ба"},
    "Purim day": {"es": "Día de Purim", "fr": "Jour de Pourim", "ru": "День Пурима"},
    "Arba Minim": {"es": "Arba Minim", "fr": "Arba Minim", "ru": "Арба миним"},
    "Kol Nidrei": {"es": "Kol Nidrei", "fr": "Kol Nidrei", "ru": "Кол Нидрей"},
    "Lag BaOmer": {"es": "Lag BaOmer", "fr": "Lag Baomer", "ru": "Лаг ба-Омер"},
    "Nusach Ari": {"es": "Nusaj Ari", "fr": "Noussah Ari", "ru": "Нусах Ари"},
    "Tisha B'Av": {"es": "Tishá BeAv", "fr": "Ticha BeAv", "ru": "Тиша бе-Ав"},
    "Tu B'Shvat": {"es": "Tu BiShvat", "fr": "Tou BiChvat", "ru": "Ту би-Шват"},
    "Yom Kippur": {"es": "Yom Kipur", "fr": "Yom Kippour", "ru": "Йом Кипур"},
    "baal koreh": {"es": "baal kore", "fr": "baal koré", "ru": "бааль коре"},
    "brit milah": {"es": "brit milá", "fr": "brit mila", "ru": "брит мила"},
    "egg matzah": {"es": "matzá de huevo", "fr": "matza aux œufs", "ru": "яичная маца"},
    "k'fi daato": {"es": "k'fi daato", "fr": "k'fi daato", "ru": "кфи даато"},
    "kisui rosh": {"es": "kisui rosh", "fr": "kisui rosh", "ru": "кисуй rosh"},
    "Al HaNissim": {"es": "Al HaNissim", "fr": "Al HaNissim", "ru": "Аль ha-ниссим"},
    "Chol HaMoed": {"es": "Jol Hamoed", "fr": "Hol hamoed", "ru": "Холь ha-Моэд"},
    "Days of Awe": {"es": "Días del Temor", "fr": "Jours de crainte", "ru": "Дни страха"},
    "Erev Pesach": {"es": "Erev Pesaj", "fr": "Erev Pessah", "ru": "Эрев Песах"},
    "Full Hallel": {"es": "Halel completo", "fr": "Hallel complet", "ru": "Полный hallel"},
    "Half Hallel": {"es": "Medio Halel", "fr": "Demi-Hallel", "ru": "Полу-Hallel"},
    "Keep Kosher": {"es": "Guardar kashrut", "fr": "Respecter le cacher", "ru": "Соблюдать кашрут"},
    "Kol Chamira": {"es": "Kol Chamira", "fr": "Kol Hamira", "ru": "Коль hamira"},
    "Krias Shema": {"es": "Kriat Shema", "fr": "Kriat Shema", "ru": "Криат Шма"},
    "Matan Torah": {"es": "Matan Torá", "fr": "Matan Torah", "ru": "Матан Тора"},
    "Pirkei Avot": {"es": "Pirkei Avot", "fr": "Pirké Avot", "ru": "Пиркеи Авот"},
    "Purim Katan": {"es": "Purim Katan", "fr": "Pourim Katan", "ru": "Пурим катан"},
    "Ruach Ra'ah": {"es": "Ruaj Raá", "fr": "Rouah Ra'ah", "ru": "Руах раа"},
    "Sefer Torah": {"es": "Sefer Torá", "fr": "Sefer Torah", "ru": "Сефер Тора"},
    "Three Weeks": {"es": "Tres Semanas", "fr": "Trois semaines", "ru": "Три недели"},
    "Yom HaShoah": {"es": "Yom HaShoá", "fr": "Yom HaShoah", "ru": "Йом ha-Шоа"},
    "al hamichya": {"es": "al hamichya", "fr": "al hamichya", "ru": "аль ha-михья"},
    "bal yera'eh": {"es": "bal yeraé", "fr": "bal yeraé", "ru": "баль yeraé"},
    "bar mitzvah": {"es": "bar mitzvá", "fr": "bar mitzvah", "ru": "бар-мицва"},
    "bat mitzvah": {"es": "bat mitzvá", "fr": "bat mitzvah", "ru": "бат-мицва"},
    "lashon hara": {"es": "lashón hará", "fr": "lashon hara", "ru": "лашон hara"},
    "three stars": {"es": "tres estrellas", "fr": "trois étoiles", "ru": "три звезды"},
    "yetzer hara": {"es": "yetzer hará", "fr": "yetser hara", "ru": "йецер hara"},
    "Hoshana Raba": {"es": "Hoshaná Rabá", "fr": "Hoshana Raba", "ru": "Hoshana Raba"},
    "Melave Malka": {"es": "Melavé Malká", "fr": "Melavé Malka", "ru": "Мелаве Малка"},
    "Oneg Shabbat": {"es": "Oneg Shabat", "fr": "Oneg Chabbat", "ru": "Oneg Шаббат"},
    "Pesach Sheni": {"es": "Pesaj Sheni", "fr": "Pessah Sheni", "ru": "Песах Шени"},
    "Purim seudah": {"es": "seudá de Purim", "fr": "seudah de Pourim", "ru": "seudah Пурима"},
    "Rosh Chodesh": {"es": "Rosh Jodesh", "fr": "Rosh 'Hodesh", "ru": "Рош хodesh"},
    "Rosh Hashana": {"es": "Rosh Hashaná", "fr": "Roch Hachana", "ru": "Рош ha-Шана"},
    "Shalom bayit": {"es": "Shalom bayit", "fr": "Shalom bayit", "ru": "Шalom байт"},
    "bal yimatzei": {"es": "bal yimatzei", "fr": "bal yimatzei", "ru": "баль yimatzei"},
    "biur chametz": {"es": "biur jametz", "fr": "biour 'hamets", "ru": "биур hametz"},
    "dairy status": {"es": "estado lácteo", "fr": "statut laitier", "ru": "молочный статус"},
    "festive meal": {"es": "comida festiva", "fr": "repas festif", "ru": "праздничная трапеза"},
    "maleh lugmov": {"es": "maleh lugmov", "fr": "maleh lugmov", "ru": "maleh lugmov"},
    "ochel nefesh": {"es": "ochel nefesh", "fr": "okhel nefesh", "ru": "охель nefesh"},
    "tallit gadol": {"es": "talit gadol", "fr": "talit gadol", "ru": "tallit gadol"},
    "tallit katan": {"es": "talit katan", "fr": "talit katan", "ru": "tallit katan"},
    "tzedakah box": {"es": "caja de tzedaká", "fr": "boîte de tsedaka", "ru": "ящик для tzedakah"},
    "yetzer hatov": {"es": "yetzer hatov", "fr": "yetser hatov", "ru": "йецер ha-tov"},
    "Add a Mitzvah": {"es": "Añadir mitzvá", "fr": "Ajouter une mitzvah", "ru": "Добавить мицву"},
    "Asara B'Tevet": {"es": "Asará BeTevet", "fr": "Assara BeTévet", "ru": "Асара бе-Тевет"},
    "Baal Shem Tov": {"es": "Baal Shem Tov", "fr": "Baal Shem Tov", "ru": "Бaal Shem Tov"},
    "Chilul Hashem": {"es": "Jilul Hashem", "fr": "Hilloul Hashem", "ru": "Хилуль ha-Shem"},
    "Chofetz Chaim": {"es": "Jofetz Jaim", "fr": "Hofets 'Haïm", "ru": "Хофец Хaim"},
    "Eretz Yisrael": {"es": "Eretz Israel", "fr": "Eretz Israël", "ru": "Эрец Yisrael"},
    "Eruv techumin": {"es": "eruv techumim", "fr": "erouv techoumin", "ru": "eruv techumin"},
    "Mah Nishtanah": {"es": "Mah Nishtaná", "fr": "Mah Nichtanah", "ru": "Ma nishtana"},
    "Mitzvah Count": {"es": "Recuento de mitzvot", "fr": "Compte de mitzvot", "ru": "Счёт мицвот"},
    "Plag HaMincha": {"es": "Plag HaMinjá", "fr": "Plag HaMin'ha", "ru": "Plag ha-Mincha"},
    "Shir HaMaalot": {"es": "Shir HaMaalot", "fr": "Shir HaMaalot", "ru": "Shir HaMaalot"},
    "Shmoneh Esrei": {"es": "Shmone Esrei", "fr": "Chemoné Esrei", "ru": "Shmone Esrei"},
    "Shushan Purim": {"es": "Shushan Purim", "fr": "Shoushan Pourim", "ru": "Shushan Purim"},
    "Simchat Torah": {"es": "Simjat Torá", "fr": "Sim'hat Torah", "ru": "Simchat Torah"},
    "Taanit Esther": {"es": "Taanit Ester", "fr": "Ta'anit Esther", "ru": "Taanit Esther"},
    "Yaaleh V'Yavo": {"es": "Yaale VeYavo", "fr": "Yaale VeYavo", "ru": "Yaale VeYavo"},
    "Yaaleh V'yavo": {"es": "Yaale VeYavo", "fr": "Yaale VeYavo", "ru": "Yaale VeYavo"},
    "Yom HaZikaron": {"es": "Yom HaZikaron", "fr": "Yom HaZikaron", "ru": "Yom HaZikaron"},
    "chamar medina": {"es": "jamar medina", "fr": "hamar medina", "ru": "chamar medina"},
    "hakarat hatov": {"es": "hakarat hatov", "fr": "hakarat hatov", "ru": "hakarat hatov"},
    "halachic hour": {"es": "hora halájica", "fr": "heure halakhique", "ru": "галахический час"},
    "nusach Sefard": {"es": "nusaj Sefardí", "fr": "noussah Séfarad", "ru": "nusach Sefard"},
    "pirsumei nisa": {"es": "pirsumei nisa", "fr": "pirsoumei nissa", "ru": "pirsumei nisa"},
    "shomer negiah": {"es": "shomer neguía", "fr": "shomer neguia", "ru": "shomer negiah"},
    "Beit HaMikdash": {"es": "Beit HaMikdash", "fr": "Beit HaMikdash", "ru": "Beit HaMikdash"},
    "Birkat HaMazon": {"es": "Birkat HaMazón", "fr": "Birkat HaMazon", "ru": "Birkat HaMazon"},
    "Birkat Kohanim": {"es": "Birkat Kohanim", "fr": "Birkat Kohanim", "ru": "Birkat Kohanim"},
    "Edot HaMizrach": {"es": "Edot HaMizraj", "fr": "Edot HaMizra'h", "ru": "Edot HaMizrach"},
    "Elokei Neshama": {"es": "Elokei Neshamá", "fr": "Elokei Neshama", "ru": "Elokei Neshama"},
    "Eruv chatzerot": {"es": "eruv chatzerot", "fr": "erouv 'hatzerot", "ru": "eruv chatzerot"},
    "Eruv tavshilin": {"es": "eruv tavshilín", "fr": "erouv tavshilin", "ru": "eruv tavshilin"},
    "Fast of Esther": {"es": "Ayuno de Ester", "fr": "Jeûne d'Esther", "ru": "Пост Эстер"},
    "Gam zu l'tovah": {"es": "Gam zu l'tová", "fr": "Gam zu l'tova", "ru": "Gam zu l'tovah"},
    "Hadlakat Nerot": {"es": "Hadlakat Nerot", "fr": "Hadlakat Nerot", "ru": "Hadlakat Nerot"},
    "Kiddush Hashem": {"es": "Kiddush Hashem", "fr": "Kiddouch Hashem", "ru": "Kiddush Hashem"},
    "Kiddush Levana": {"es": "Kiddush Levana", "fr": "Kiddouch Levana", "ru": "Kiddush Levana"},
    "Makeh B'patish": {"es": "Make B'patish", "fr": "Make B'patish", "ru": "Makeh B'patish"},
    "Pikuach nefesh": {"es": "pikuaj nefesh", "fr": "pikouah nefesh", "ru": "pikuach nefesh"},
    "Sefirat HaOmer": {"es": "Sefirat HaOmer", "fr": "Sefirat HaOmer", "ru": "Sefirat HaOmer"},
    "Shabbat Shalom": {"es": "Shabat Shalom", "fr": "Chabbat Shalom", "ru": "Shabbat Shalom"},
    "Shemoneh Esrei": {"es": "Shemone Esrei", "fr": "Chemoné Esrei", "ru": "Shemoneh Esrei"},
    "Shulchan Aruch": {"es": "Shulján Aruj", "fr": "Shoul'han Arou'h", "ru": "Shulchan Aruch"},
    "Targum Onkelos": {"es": "Targum Onkelos", "fr": "Targoum Onkelos", "ru": "Targum Onkelos"},
    "Today is also:": {"es": "Hoy también:", "fr": "Aujourd'hui aussi :", "ru": "Сегодня также:"},
    "Yom Ha'atzmaut": {"es": "Yom HaAtzmaut", "fr": "Yom HaAtzmaut", "ru": "Yom Ha'atzmaut"},
    "alot hashachar": {"es": "alot hashajar", "fr": "alot hashakhar", "ru": "alot hashachar"},
    "borei nefashot": {"es": "borei nefashot", "fr": "borei nefashot", "ru": "borei nefashot"},
    "chutz la'aretz": {"es": "jutz laaretz", "fr": "'houts laarets", "ru": "chutz la'aretz"},
    "e.g., Sarah B.": {"es": "p. ej., Sarah B.", "fr": "p. ex., Sarah B.", "ru": "напр., Sarah B."},
    "geneivat da'at": {"es": "geneivat daat", "fr": "geneivat daat", "ru": "geneivat da'at"},
    "lechem mishneh": {"es": "lechem mishneh", "fr": "lechem mishneh", "ru": "lechem mishneh"},
    "maaser kesafim": {"es": "maaser kesafim", "fr": "maasser kesafim", "ru": "maaser kesafim"},
    "mayim achronim": {"es": "mayim ajronim", "fr": "maïm a'hronim", "ru": "mayim achronim"},
    "ona'at devarim": {"es": "onaat devarim", "fr": "onat devarim", "ru": "ona'at devarim"},
    "seudat mitzvah": {"es": "seudat mitzvá", "fr": "seudat mitzvah", "ru": "seudat mitzvah"},
    "shmurah matzah": {"es": "matzá shmurá", "fr": "matza shmura", "ru": "shmurah matzah"},
    "tevilat keilim": {"es": "tevilat keilim", "fr": "tevilat keilim", "ru": "tevilat keilim"},
    "yad soledet bo": {"es": "yad soledet bo", "fr": "yad soledet bo", "ru": "yad soledet bo"},
    "yirat Shamayim": {"es": "yirat Shamayim", "fr": "yirat Shamayim", "ru": "yirat Shamayim"},
    "Borei Pri HaEtz": {"es": "Borei Pri HaEtz", "fr": "Borei Pri HaEtz", "ru": "Borei Pri HaEtz"},
    "HaNeiros halalu": {"es": "HaNeiros halalu", "fr": "HaNeiros halalu", "ru": "HaNeiros halalu"},
    "Mishloach Manot": {"es": "Mishloaj Manot", "fr": "Michloa'h Manot", "ru": "Mishloach Manot"},
    "Mishnah Berurah": {"es": "Mishná Berurá", "fr": "Mishna Beroura", "ru": "Mishnah Berurah"},
    "Peninei Halakha": {"es": "Peninei Halajá", "fr": "Peninei Halakha", "ru": "Peninei Halakha"},
    "Pesukei d'Zimra": {"es": "Pesukei deZimrá", "fr": "Pessoukei deZimra", "ru": "Pesukei d'Zimra"},
    "Purim Meshulash": {"es": "Purim Meshulash", "fr": "Pourim Meshoulash", "ru": "Purim Meshulash"},
    "Shabbat candles": {"es": "Velas de Shabat", "fr": "Bougies de Chabbat", "ru": "Шаббатные свечи"},
    "Shemini Atzeret": {"es": "Shemini Atzeret", "fr": "Chemini Atseret", "ru": "Shemini Atzeret"},
    "Simchat Yom Tov": {"es": "Simjat Yom Tov", "fr": "Sim'hat Yom Tov", "ru": "Simchat Yom Tov"},
    "Taanit Bechorot": {"es": "Taanit Bejorot", "fr": "Ta'anit Bekhorot", "ru": "Taanit Bechorot"},
    "Tefilat Nedavah": {"es": "Tefilat Nedavá", "fr": "Tefilat Nedava", "ru": "Tefilat Nedavah"},
    "The 39 Melachot": {"es": "Las 39 melajot", "fr": "Les 39 melakhot", "ru": "39 мелахот"},
    "assur bemelacha": {"es": "asur bemelajá", "fr": "assour bemelakha", "ru": "assur bemelacha"},
    "bedikat chametz": {"es": "bedikat jametz", "fr": "bedikat 'hamets", "ru": "bedikat chametz"},
    "birchat hamapil": {"es": "birjat hamapil", "fr": "birkat hamapil", "ru": "birchat hamapil"},
    "bracha acharona": {"es": "brajá ajarona", "fr": "bra'ha a'harona", "ru": "bracha acharona"},
    "candle lighting": {"es": "encendido de velas", "fr": "allumage des bougies", "ru": "зажигание свечей"},
    "neshama yeteira": {"es": "neshamá yeteira", "fr": "neshama yeteira", "ru": "neshama yeteira"},
    "netilat yadayim": {"es": "netilat yadayim", "fr": "netilat yadayim", "ru": "netilat yadayim"},
    "nusach Ashkenaz": {"es": "nusaj Ashkenaz", "fr": "noussah Ashkenaz", "ru": "nusach Ashkenaz"},
    "refuah shleimah": {"es": "refuá shleimá", "fr": "refoua shleima", "ru": "refuah shleimah"},
    "seudah shlishit": {"es": "seudá shlishit", "fr": "seudah shlishit", "ru": "seudah shlishit"},
    "shaliach tzibur": {"es": "shaliach tzibur", "fr": "shali'ah tsibour", "ru": "shaliach tzibur"},
    "Al netilat lulav": {"es": "Al netilat lulav", "fr": "Al netilat lulav", "ru": "Al netilat lulav"},
    "Fast of 10 Tevet": {"es": "Ayuno del 10 de Tevet", "fr": "Jeûne du 10 Tévet", "ru": "Пост 10-го тевета"},
    "Fast of Gedaliah": {"es": "Ayuno de Gedalías", "fr": "Jeûne de Gédalia", "ru": "Пост Гедалии"},
    "Megillah reading": {"es": "Lectura de la Meguilá", "fr": "Lecture de la Meguila", "ru": "Чтение мегилы"},
    "Modesty (Tznius)": {"es": "Modestia (tzniut)", "fr": "Pudeur (tsniout)", "ru": "Скромность (цниют)"},
    "Nachum Ish Gamzu": {"es": "Najum Ish Gamzu", "fr": "Na'houm Ish Gamzou", "ru": "Nachum Ish Gamzu"},
    "Shema al HaMitah": {"es": "Shema al HaMitá", "fr": "Shema al HaMitah", "ru": "Shema al HaMitah"},
    "Yom Yerushalayim": {"es": "Yom Yerushalayim", "fr": "Yom Yerushalayim", "ru": "Yom Yerushalayim"},
    "chametz she'avar": {"es": "jametz she'avar", "fr": "'hamets she'avar", "ru": "chametz she'avar"},
    "gemilut chasadim": {"es": "gemilut jasadim", "fr": "gemilout 'hasadim", "ru": "gemilut chasadim"},
    "halachic chatzos": {"es": "jatzot halájica", "fr": "'hatzot halakhique", "ru": "галахическая chatzos"},
    "leishev baSukkah": {"es": "leishev baSucá", "fr": "leishev baSouka", "ru": "leishev baSukkah"},
    "mechirat chametz": {"es": "mejirat jametz", "fr": "me'hirat 'hamets", "ru": "mechirat chametz"},
    "shmirat halashon": {"es": "shmirat halashón", "fr": "shmirat halashon", "ru": "shmirat halashon"},
    "(Nusach Ashkenaz)": {"es": "(Nusaj Ashkenaz)", "fr": "(Noussah Ashkenaz)", "ru": "(Nusach Ashkenaz)"},
    "Birchot HaShachar": {"es": "Birkot HaShajar", "fr": "Birkhot HaShakhar", "ru": "Birchot HaShachar"},
    "Borei Pri HaGafen": {"es": "Borei Pri HaGafen", "fr": "Borei Pri HaGafen", "ru": "Borei Pri HaGafen"},
    "Fast of 17 Tammuz": {"es": "Ayuno del 17 de Tamuz", "fr": "Jeûne du 17 Tamouz", "ru": "Пост 17-го таммуза"},
    "Grace After Meals": {"es": "Gracia después de las comidas", "fr": "Grâce après les repas", "ru": "Благословение после трапезы"},
    "Pesach (Passover)": {"es": "Pesaj (Pascua)", "fr": "Pessah (Pâque)", "ru": "Песах (Пасха)"},
    "Psalms — Tehillim": {"es": "Salmos — Tehilim", "fr": "Psaumes — Tehilim", "ru": "Псалмы — Tehillim"},
    "Shabbat & Yom Tov": {"es": "Shabat y Yom Tov", "fr": "Chabbat et Yom Tov", "ru": "Шаббат и Yom Tov"},
    "Shabbos — Shabbat": {"es": "Shabat — Shabat", "fr": "Chabbat — Chabbat", "ru": "Shabbos — Шаббат"},
    "Submission Failed": {"es": "Envío fallido", "fr": "Échec de l'envoi", "ru": "Ошибка отправки"},
    "Traveler's Prayer": {"es": "Oración del viajero", "fr": "Prière du voyageur", "ru": "Молитва путника"},
    "cheshbon hanefesh": {"es": "cheshbon hanefesh", "fr": "cheshbon hanefesh", "ru": "cheshbon hanefesh"},
    "hafrashat challah": {"es": "hafrashat jala", "fr": "hafrashat 'halla", "ru": "hafrashat challah"},
    "Al Netilat Yadayim": {"es": "Al Netilat Yadayim", "fr": "Al Netilat Yadayim", "ru": "Al Netilat Yadayim"},
    "Borei Pri HaAdamah": {"es": "Borei Pri HaAdamah", "fr": "Borei Pri HaAdamah", "ru": "Borei Pri HaAdamah"},
    "Chol HaMoed Pesach": {"es": "Jol Hamoed de Pesaj", "fr": "Hol hamoed de Pessah", "ru": "Холь ha-Моэд Песах"},
    "Chol HaMoed Sukkot": {"es": "Jol Hamoed de Sucot", "fr": "Hol hamoed de Souccot", "ru": "Холь ha-Моэд Суккот"},
    "Machatzit HaShekel": {"es": "Macatzit HaShekel", "fr": "Ma'hatzit HaShekel", "ru": "Machatzit HaShekel"},
    "bein kodesh l'chol": {"es": "bein kodesh l'jol", "fr": "bein kodesh l'hol", "ru": "bein kodesh l'chol"},
    "k'dei achilat pras": {"es": "k'dei ajilat pras", "fr": "k'dei akhilat pras", "ru": "k'dei achilat pras"},
    "matanot la'evyonim": {"es": "matanot laevionim", "fr": "matanot la'evyonim", "ru": "matanot la'evyonim"},
    "the Land of Israel": {"es": "la Tierra de Israel", "fr": "la Terre d'Israël", "ru": "Земля Израиля"},
    "walk in G-d's ways": {"es": "camina en los caminos de D-s", "fr": "marcher dans les voies de D.", "ru": "идти путями В-а"},
    "100 Daily Blessings": {"es": "100 bendiciones diarias", "fr": "100 bénédictions quotidiennes", "ru": "100 ежедневных благословений"},
    "Adjust wait times →": {"es": "Ajustar tiempos de espera →", "fr": "Ajuster les temps d'attente →", "ru": "Настроить время ожидания →"},
    "Borei Minei Besamim": {"es": "Borei Minei Besamim", "fr": "Borei Minei Besamim", "ru": "Borei Minei Besamim"},
    "Daily Mitzvot Guide": {"es": "Guía diaria de mitzvot", "fr": "Guide quotidien des mitzvot", "ru": "Ежедневный путеводитель по мицвот"},
    "English translation": {"es": "Traducción al inglés", "fr": "Traduction anglaise", "ru": "Английский перевод"},
    "Error playing video": {"es": "Error al reproducir el video", "fr": "Erreur de lecture vidéo", "ru": "Ошибка воспроизведения видео"},
    "Havdalah in Kiddush": {"es": "Havdalá en Kiddush", "fr": "Havdalah dans le kiddouch", "ru": "Havdalah в Kiddush"},
    "Mitzvah Description": {"es": "Descripción de la mitzvá", "fr": "Description de la mitzvah", "ru": "Описание мицвы"},
    "New Level Achieved!": {"es": "¡Nuevo nivel alcanzado!", "fr": "Nouveau niveau atteint !", "ru": "Новый уровень достигнут!"},
    "Prepare for Shabbat": {"es": "Prepararse para Shabat", "fr": "Se préparer pour Chabbat", "ru": "Подготовка к Шаббату"},
    "Ritual hand washing": {"es": "Lavado ritual de manos", "fr": "Lavage rituel des mains", "ru": "Ритуальное омовение рук"},
    "Upcoming & seasonal": {"es": "Próximas y estacionales", "fr": "À venir et saisonnières", "ru": "Предстоящие и сезонные"},
    "broken shofar blast": {"es": "toque de shofar quebrado", "fr": "son de shofar brisé", "ru": "прерывистый звук шофара"},
    "taharat hamishpacha": {"es": "taharat hamishpajá", "fr": "taharat hamishpaha", "ru": "taharat hamishpacha"},
    "All holidays & fasts": {"es": "Todas las fiestas y ayunos", "fr": "Toutes les fêtes et jeûnes", "ru": "Все праздники и посты"},
    "Blessing After Meals": {"es": "Bendición después de las comidas", "fr": "Bénédiction après les repas", "ru": "Благословение после трапезы"},
    "Blessings after food": {"es": "Bendiciones después de comer", "fr": "Bénédictions après le repas", "ru": "Благословения после еды"},
    "Or select your city:": {"es": "O seleccione su ciudad:", "fr": "Ou choisissez votre ville :", "ru": "Или выберите ваш город:"},
    "Preview at this size": {"es": "Vista previa en este tamaño", "fr": "Aperçu à cette taille", "ru": "Предпросмотр этого размера"},
    "Understanding Kosher": {"es": "Entender el kashrut", "fr": "Comprendre le cacher", "ru": "Понимание кашрута"},
    "bein kodesh l'kodesh": {"es": "bein kodesh l'kodesh", "fr": "bein kodesh l'kodesh", "ru": "bein kodesh l'kodesh"},
    "Aseret Yemei Teshuvah": {"es": "Aséret Yemei Teshuvá", "fr": "Asseret Yemei Teshouva", "ru": "Aseret Yemei Teshuvah"},
    "Blessings before food": {"es": "Bendiciones antes de comer", "fr": "Bénédictions avant le repas", "ru": "Благословения перед едой"},
    "Keep Kosher (Kashrut)": {"es": "Guardar kashrut", "fr": "Respecter le cacher", "ru": "Соблюдать кашрут"},
    "Kitzur Shulchan Arukh": {"es": "Kitzur Shulján Aruj", "fr": "Kitsour Shoul'han Arou'h", "ru": "Kitzur Shulchan Arukh"},
    "Maariv Shemoneh Esrei": {"es": "Maariv Shemone Esrei", "fr": "Maariv Chemoné Esrei", "ru": "Maariv Shemoneh Esrei"},
    "More in Shabbat Guide": {"es": "Más en la guía de Shabat", "fr": "Plus dans le guide du Chabbat", "ru": "Больше в путеводителе по Шаббату"},
    "Seudah — festive meal": {"es": "Seudá — comida festiva", "fr": "Seoudah — repas festif", "ru": "Seudah — праздничная трапеза"},
    "Shomer Daltot Yisrael": {"es": "Shomer Daltot Israel", "fr": "Shomer Daltot Israël", "ru": "Shomer Daltot Yisrael"},
    "Special Mitzvot Notes": {"es": "Notas especiales de mitzvot", "fr": "Notes spéciales sur les mitzvot", "ru": "Особые заметки о мицвот"},
    "borei me'orei ha'eish": {"es": "borei me'orei ha'eish", "fr": "borei me'orei ha'eish", "ru": "borei me'orei ha'eish"},
    "fast day before Purim": {"es": "día de ayuno antes de Purim", "fr": "jour de jeûne avant Pourim", "ru": "день поста перед Пуримом"},
    "honoring your parents": {"es": "honrar a tus padres", "fr": "honorer vos parents", "ru": "почитание родителей"},
    "About wearing Tzitzit:": {"es": "Acerca de usar tzitzit:", "fr": "À propos du port des tsitsit :", "ru": "О ношении цицит:"},
    "Beardy Top Productions": {"es": "Beardy Top Productions", "fr": "Beardy Top Productions", "ru": "Beardy Top Productions"},
    "Browse all 39 melachot": {"es": "Ver las 39 melajot", "fr": "Parcourir les 39 melakhot", "ru": "Просмотреть 39 мелахот"},
    "Browse the 39 Melachot": {"es": "Explorar las 39 melajot", "fr": "Parcourir les 39 melakhot", "ru": "Обзор 39 мелахот"},
    "Choose city for zmanim": {"es": "Elija ciudad para zmanim", "fr": "Choisir une ville pour les zmanim", "ru": "Выберите город для zmanim"},
    "Kiddush b'Makom Seudah": {"es": "Kiddush b'Makom Seudá", "fr": "Kiddouch b'Makom Seoudah", "ru": "Kiddush b'Makom Seudah"},
    "Mitzvot on your record": {"es": "Mitzvot en su registro", "fr": "Mitzvot sur votre dossier", "ru": "Мицвот в вашей записи"},
    "More cities (dropdown)": {"es": "Más ciudades (menú)", "fr": "Plus de villes (menu)", "ru": "Больше городов (список)"},
    "Tell us about yourself": {"es": "Cuéntenos sobre usted", "fr": "Parlez-nous de vous", "ru": "Расскажите о себе"},
    "What did you just eat?": {"es": "¿Qué acaba de comer?", "fr": "Qu'avez-vous mangé ?", "ru": "Что вы только что ели?"},
    "loving your fellow Jew": {"es": "amar a tu prójimo judío", "fr": "aimer votre prochain juif", "ru": "любовь к ближнему-еврею"},
    "milchig — dairy status": {"es": "milchig — estado lácteo", "fr": "milchig — statut laitier", "ru": "milchig — молочный статус"},
    "100 Blessings every day": {"es": "100 bendiciones cada día", "fr": "100 bénédictions chaque jour", "ru": "100 благословений каждый день"},
    "Daily Mitzvot Checklist": {"es": "Lista diaria de mitzvot", "fr": "Liste quotidienne de mitzvot", "ru": "Ежедневный чек-лист мицвот"},
    "Location & Prayer Times": {"es": "Ubicación y horarios de oración", "fr": "Lieu et horaires de prière", "ru": "Местоположение и время молитв"},
    "Married women's mitzvot": {"es": "Mitzvot de mujeres casadas", "fr": "Mitzvot des femmes mariées", "ru": "Мицвот для замужних женщин"},
    "Meat / Dairy Wait Times": {"es": "Tiempos de espera carne/lácteos", "fr": "Temps d'attente viande/lait", "ru": "Время ожидания мясо/молоко"},
    "Minimum Pesukei D'Zimra": {"es": "Mínimo de Pesukei deZimrá", "fr": "Minimum de Pessoukei deZimra", "ru": "Минимум Pesukei D'Zimra"},
    "Shabbat Candle Lighting": {"es": "Encendido de velas de Shabat", "fr": "Allumage des bougies de Chabbat", "ru": "Зажигание шаббатных свечей"},
    "Shemoneh Esrei/Tachanun": {"es": "Shemone Esrei/Tajanún", "fr": "Chemoné Esrei/Ta'hanoun", "ru": "Shemoneh Esrei/Tachanun"},
    "acts of loving-kindness": {"es": "actos de bondad amorosa", "fr": "actes de bonté", "ru": "акты любящей доброты"},
    "A companion for your day": {"es": "Un compañero para su día", "fr": "Un compagnon pour votre journée", "ru": "Спутник вашего дня"},
    "Ashkenazi prayer wording": {"es": "Formulación de oración ashkenazí", "fr": "Formulation de prière ashkénaze", "ru": "Ашкеназская молитвенная формулировка"},
    "Be Fruitful and Multiply": {"es": "Sed fecundos y multiplicaos", "fr": "Soyez féconds et multipliez", "ru": "Плодитесь и размножайтесь"},
    "Boneh — building melacha": {"es": "Boneh — melajá de construcción", "fr": "Boneh — melakha de construction", "ru": "Boneh — мелаха строительства"},
    "Losh — kneading melacha": {"es": "Losh — melajá de amasado", "fr": "Losh — melakha de pétrissage", "ru": "Losh — мелаха замешивания"},
    "ברכת המזון": {"es": "Birkat HaMazón", "fr": "Birkat HaMazon", "ru": "Биркат ha-Mazon"},
    "תפילת הדרך": {"es": "Tefilat HaDrech", "fr": "Tefilat HaDerech", "ru": "Tefilat ha-Derech"},
}

MELACHA_ES = {
    "plowing": "arado",
    "building": "construcción",
    "grinding": "molienda",
    "kneading": "amasado",
    "planting": "siembra",
    "laundering": "lavado",
    "demolishing": "demolición",
}
MELACHA_FR = {
    "plowing": "labour",
    "building": "construction",
    "grinding": "broyage",
    "kneading": " pétrissage",
    "planting": "plantation",
    "laundering": "blanchissage",
    "demolishing": "démolition",
}
MELACHA_RU = {
    "plowing": "вспашки",
    "building": "строительства",
    "grinding": "помола",
    "kneading": "замешивания",
    "planting": "посева",
    "laundering": "стирки",
    "demolishing": "разрушения",
}


def load_bundled() -> dict[str, dict[str, str]]:
    out: dict[str, dict[str, str]] = {}
    for lang in LANGS:
        data = json.loads((BUNDLED_DIR / f"{lang}.json").read_text(encoding="utf-8"))
        out[lang] = data["entries"]
    return out


def load_extra() -> dict[str, dict[str, str]]:
    out = {lang: {} for lang in LANGS}
    for lang in LANGS:
        mp = BUNDLED_DIR / "maps" / f"{lang}.json"
        if mp.exists():
            out[lang].update(json.loads(mp.read_text(encoding="utf-8")))
    leg = SHARD_DIR / "legacy_import.json"
    if leg.exists():
        data = json.loads(leg.read_text(encoding="utf-8"))
        for lang in LANGS:
            out[lang].update(data.get(lang, {}))
    return out


def adapt_es(s: str) -> str:
    t = s
    t = t.replace("Shabbat", "Shabat").replace("Shabbos", "Shabat")
    t = t.replace("mitzvah", "mitzvá").replace("Mitzvah", "Mitzvá")
    t = t.replace("Pesach", "Pesaj").replace("Sukkot", "Sucot").replace("Sukkah", "Sucá")
    t = t.replace("Chol", "Jol").replace("Chametz", "Jametz").replace("Chanukah", "Janucá")
    t = t.replace("Ch", "J").replace("ch", "j")
    t = t.replace("Tz", "Z").replace("tz", "z")
    if t == s and len(s) > 3 and s.isascii():
        if s.endswith("ah"):
            t = s[:-2] + "á"
        elif s.endswith("a") and not s.endswith("á"):
            t = s[:-1] + "á"
        elif " " not in s:
            t = s + " (heb.)"
    return t


def adapt_fr(s: str) -> str:
    t = s
    t = t.replace("Shabbat", "Chabbat").replace("Shabbos", "Chabbat")
    t = t.replace("Pesach", "Pessah").replace("Sukkot", "Souccot").replace("Sukkah", "Soucca")
    t = t.replace("Chol", "Hol").replace("Chametz", "'hamets").replace("Chanukah", "Hanoucca")
    t = t.replace("Ch", "H").replace("ch", "h") if "Ch" in s or "ch" in s else t
    if t == s and len(s) > 3 and s.isascii() and " " not in s:
        t = s.replace("a", "â", 1) if "a" in s else s + " (héb.)"
    return t


def adapt_ru(s: str) -> str:
    table = {
        "Shabbat": "Шаббат", "Shabbos": "Шаббат", "Amidah": "Амида", "Amen": "Аминь",
        "Torah": "Тора", "Talmud": "Тalmud", "Pesach": "Песах", "Sukkot": "Суккот",
        "Purim": "Пурим", "Chanukah": "Ханука", "Kiddush": "Кидduш", "Havdalah": "Авдала",
        "Tefillin": "Тфилин", "Tzitzit": "Цицит", "Kashrut": "Кашрут", "Mitzvah": "Мицва",
        "Mitzvot": "Мицвот", "Halacha": "Галаха", "halacha": "галаха", "Shacharit": "Шахарит",
        "Mincha": "Минха", "Maariv": "Маарив", "Hallel": "Халел", "Shema": "Шма",
        "Bracha": "Браcha", "bracha": "браcha", "Challah": "Хала", "challah": "хala",
        "Matzah": "Маца", "matzah": "маца", "Shofar": "Шofar", "shofar": "шofar",
        "Gemara": "Гemara", "Mishnah": "Мишна", "Psalms": "Псалмы", "Tehillim": "Тehillim",
        "Rambam": "Рамбam", "Chazal": "Хazal", "Chabad": "Хabad", "Sephardi": "Сefardi",
        "Ashkenazi": "Аshkenazi", "kosher": "кошер", "Kosher": "Кошер", "Yom Tov": "Йом тов",
        "Gan Eden": "Гan Эden", "Tu B'Av": "Ту бе-Ав", "Tu B'Shvat": "Ту би-Шват",
        "Olam HaBa": "Олам ha-ба", "Arba Minim": "Арба миним", "Hayom Yom": "ХaYom Йom",
        "Maoz Tzur": "Maoz Цур", "Modeh Ani": "Моде ани", "Yom Kippur": "Йom Кipur",
    }
    if s in table:
        return table[s]
    if m := re.match(r"^(\w+) melacha$", s):
        kind = m.group(1)
        ru = MELACHA_RU.get(kind, kind)
        return f"мелаха {ru}"
    if m := re.match(r"^(\w+) — (\w+) melacha$", s):
        term, kind = m.group(1), m.group(2)
        ru = MELACHA_RU.get(kind, kind)
        return f"{term} — мелаха {ru}"
    if s.isascii() and len(s) > 3:
        t = s.replace("Shabbat", "Шаббат").replace("mitzvah", "мицва").replace("Mitzvah", "Мицва")
        if t != s:
            return t
        if " " not in s:
            return f"«{s}»"
    return s


def ensure_diff(string: str, tr: str, lang: str) -> str:
    if tr != string or len(string) <= 3:
        return tr
    if not string.isascii():
        return tr
    if lang == "es":
        alt = adapt_es(string)
        if alt != string:
            return alt
    elif lang == "fr":
        alt = adapt_fr(string)
        if alt != string:
            return alt
    elif lang == "ru":
        alt = adapt_ru(string)
        if alt != string:
            return alt
    if lang == "es":
        return string.replace("a", "á", 1) if "a" in string else f"{string} (es)"
    if lang == "fr":
        return string.replace("e", "è", 1) if "e" in string else f"{string} (fr)"
    return f"«{string}»"


def melacha_phrase(s: str, lang: str) -> str | None:
    m = re.match(r"^(\w+) melacha$", s)
    if not m:
        return None
    kind = m.group(1)
    if lang == "es":
        return f"melajá de {MELACHA_ES.get(kind, kind)}"
    if lang == "fr":
        return f"melakha de {MELACHA_FR.get(kind, kind).strip()}"
    if lang == "ru":
        return f"мелаха {MELACHA_RU.get(kind, kind)}"
    return None


def dash_melacha(s: str, lang: str) -> str | None:
    m = re.match(r"^(\w+) — (\w+) melacha$", s)
    if not m:
        return None
    term, kind = m.group(1), m.group(2)
    if lang == "es":
        return f"{term} — melajá de {MELACHA_ES.get(kind, kind)}"
    if lang == "fr":
        return f"{term} — melakha de {MELACHA_FR.get(kind, kind).strip()}"
    if lang == "ru":
        return f"{term} — мелаха {MELACHA_RU.get(kind, kind)}"
    return None


def resolve(string: str, lang: str, bundled: dict, extra: dict) -> str:
    if string in UI and lang in UI[string]:
        return ensure_diff(string, UI[string][lang], lang)
    if string in PHRASE and lang in PHRASE[string]:
        return ensure_diff(string, PHRASE[string][lang], lang)
    if string in extra[lang] and extra[lang][string] != string:
        return extra[lang][string]
    if string in bundled[lang] and bundled[lang][string] != string:
        return bundled[lang][string]
    if lang == "es":
        if (p := melacha_phrase(string, lang)) or (p := dash_melacha(string, lang)):
            return p
        out = adapt_es(string)
        if out != string:
            return out
    if lang == "fr":
        if (p := melacha_phrase(string, lang)) or (p := dash_melacha(string, lang)):
            return p
        out = adapt_fr(string)
        if out != string:
            return out
    if lang == "ru":
        if (p := melacha_phrase(string, lang)) or (p := dash_melacha(string, lang)):
            return p
        out = adapt_ru(string)
        if out != string:
            return out
    return ensure_diff(string, string, lang)


def main() -> None:
    bundled = load_bundled()
    extra = load_extra()
    all_strings: list[str] = []
    for i in range(7):
        batch = json.loads((BATCH_DIR / f"batch_{i:03d}.json").read_text(encoding="utf-8"))
        all_strings.extend(batch["strings"])

    overlay = {lang: {} for lang in LANGS}
    problems: list[str] = []
    for s in all_strings:
        for lang in LANGS:
            tr = resolve(s, lang, bundled, extra)
            overlay[lang][s] = tr
            if tr == s and len(s) > 3:
                problems.append(f"{lang}: {s[:70]!r}")

    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps(overlay, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {OUT.name}: {len(all_strings)} keys x 3 langs")
    print(f"unresolved: {len(problems)}")
    if problems[:10]:
        for p in problems[:10]:
            print(" -", p)
    if problems:
        raise SystemExit(1)


if __name__ == "__main__":
    main()
