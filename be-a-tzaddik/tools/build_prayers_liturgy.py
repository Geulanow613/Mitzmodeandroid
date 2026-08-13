#!/usr/bin/env python3
"""Build human/prayers_liturgy.json with full liturgical es/fr/ru translations."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
KEYS = ROOT / "data/translation-catalog/_keys_prayers_liturgy.json"
OUT = ROOT / "data/translation-catalog/human/prayers_liturgy.json"
KOTLIN = ROOT.parent / "app/src/main/java/com/beardytop/mitzmode/data"


def unescape(s: str) -> str:
    return s.replace("\\n", "\n").replace('\\"', '"').replace("\\\\", "\\")


def parse_sections(text: str) -> list[dict]:
    sections = []
    for block in re.finditer(
        r"(?:BirkatSection|TefilatSection|BrachaSection)\(\s*([\s\S]*?)\n\s*\)", text
    ):
        chunk = block.group(1)
        sec: dict = {}
        for field in (
            "title", "hebrew", "english",
            "collapsibleSummary", "collapsibleSummaryEnglish",
        ):
            m = re.search(rf'{field}\s*=\s*"""([\s\S]*?)"""', chunk)
            if m:
                sec[field] = unescape(m.group(1))
                continue
            m = re.search(rf'{field}\s*=\s*"((?:[^"\\]|\\.)*)"', chunk)
            if m:
                sec[field] = unescape(m.group(1))
        if sec:
            sections.append(sec)
    return sections


def load_pairs() -> tuple[dict[str, str], dict[str, str]]:
    en_to_he: dict[str, str] = {}
    he_to_en: dict[str, str] = {}
    for fname in ("BirkatHamazonText.kt", "TefilatHaderechData.kt", "BrachotData.kt"):
        for sec in parse_sections((KOTLIN / fname).read_text(encoding="utf-8")):
            he, en = sec.get("hebrew"), sec.get("english")
            if he and en:
                he_to_en[he] = en
                en_to_he[en] = he
            cs, cse = sec.get("collapsibleSummary"), sec.get("collapsibleSummaryEnglish")
            if cs and cse:
                he_to_en.setdefault(cs, cse)
                en_to_he.setdefault(cse, cs)
    return en_to_he, he_to_en


def is_hebrew_key(key: str) -> bool:
    he = sum(1 for c in key if "\u0590" <= c <= "\u05ff")
    lat = sum(1 for c in key if "A" <= c <= "Z" or "a" <= c <= "z")
    return he > lat


# fmt: off
# Each entry: es, fr, ru — optional he for English-key Hebrew mapping
T: dict[str, dict[str, str]] = {}

def add(key: str, es: str, fr: str, ru: str, he: str | None = None):
    T[key] = {"es": es, "fr": fr, "ru": ru}
    if he:
        T[key]["he"] = he

# --- Titles ---
add("שיר המעלות — מזמור קלז (על נהרות בבל)",
    "Shir HaMaalot — Salmo 137 (Junto a los ríos de Babilonia)",
    "Shir HaMaalot — Psaume 137 (Aux rivières de Babylone)",
    "Шир ha-Ma'alot — Псалом 137 (У рек Вавилонских)")

add("שיר המעלות — מזמור קכו (בשוב יהוה)",
    "Shir HaMaalot — Salmo 126 (Cuando el Señor hizo volver)",
    "Shir HaMaalot — Psaume 126 (Quand l'Éternel ramènera)",
    "Шир ha-Ma'alot — Псалом 126 (Когда Г-сподь вернёт)")

add("זימון", "Zimmun", "Zimmoun", "Цимун")
add("ברכת הזן", "Bendición HaZan (Él que sustenta)", "Bénédiction HaZan (Celui qui sustente)", "Благословение Ha-Zan (Питающий)")
add("ברכת הארץ", "Bendición HaAretz (Sobre la Tierra)", "Bénédiction HaAretz (Sur la Terre)", "Благословение Ha-Aretz (О земле)")
add("על הניסים לחנוכה", "Al HaNissim — Chanuká", "Al HaNissim — Hanoucca", "Al HaNissim — Ханука")
add("על הניסים לפורים", "Al HaNissim — Purim", "Al HaNissim — Pourim", "Al HaNissim — Пурим")
add("בונה ירושלים", "Boneh Yerushalayim (Edificador de Jerusalén)", "Boneh Yerushalayim (Bâtisseur de Jérusalem)", "Boneh Yerushalayim (Строитель Иерусалима)")
add("יעלה ויבוא", "Yaaleh Veyavo", "Yaaleh Veyavo", "Yaaleh Veyavo")
add("הטוב והמטיב", "HaTov VeHaMetiv (El Bueno y el Benefactor)", "HaTov VeHaMetiv (Le Bon et le Bienfaiteur)", "HaTov VeHaMetiv (Добрый и Делающий добро)")
add("הרחמן", "Harachaman (El Misericordioso)", "Harachaman (Le Miséricordieux)", "Harachaman (Милостивый)")
add("הרחמן — בראש חודש ובסוכות", "Harachaman — Rosh Jodesh y Sucot", "Harachaman — Roch 'Hodesh et Souccot", "Harachaman — Рош Ходеш и Суккот")
add("הרחמן (המשך)", "Harachaman (continuación)", "Harachaman (suite)", "Harachaman (продолжение)")
add("פסוקים אחרונים", "Versículos finales", "Versets finaux", "Заключительные стихи")

add("ברכת המזון",
    "Birkat Hamazón (bendición después de las comidas)",
    "Birkat Hamazón (bénédiction après les repas)",
    "Биркат а-Мазон (благословение после еды)")

add("תפילת הדרך",
    "Tefilat HaDerech (oración del camino)",
    "Tefilat HaDerech (prière du voyage)",
    "Тефилат а-Дерех (молитва в пути)")

# --- Bracha titles ---
add("אשר יצר", "Asher Yatzar", "Asher Yatsar", "Asher Yatzar")
add("המוציא", "HaMotzi", "HaMotzi", "HaMotzi")
add("מזונות", "Mezonot", "Mezonot", "Mezonot")
add("הגפן", "HaGafen", "HaGafen", "HaGafen")
add("העץ", "HaEtz", "HaEtz", "HaEtz")
add("האדמה", "HaAdamah", "HaAdamah", "HaAdamah")
add("שהכל", "Shehakol", "Shehakol", "Shehakol")
add("בורא נפשות", "Borei Nefashot", "Borei Nefashot", "Borei Nefashot")
add("בשמים", "Besamim", "Besamim", "Besamim")
add("הברק", "HaBarak (relámpago)", "HaBarak (éclair)", "HaBarak (молния)")
add("הרעם", "HaRa'am (trueno)", "HaRa'am (tonnerre)", "HaRa'am (гром)")

# --- Collapsible summaries ---
add("בחנוכה — על הניסים · לא בכל יום",
    "En Janucá — Al HaNissim · no todos los días",
    "À Hanoucca — Al HaNissim · pas tous les jours",
    "На Ханукe — Al HaNissim · не каждый день")

add("בפורים — על הניסים · לא בכל יום",
    "En Purim — Al HaNissim · no todos los días",
    "À Pourim — Al HaNissim · pas tous les jours",
    "На Пуриме — Al HaNissim · не каждый день")

add("יעלה ויבוא — בראש חודש / פסח / סוכות · לא בכל יום",
    "Yaaleh Veyavo — Rosh Jodesj / Pesaj / Sucot · no todos los días",
    "Yaaleh Veyavo — Roch 'Hodesh / Pessah / Souccot · pas tous les jours",
    "Yaaleh Veyavo — Рош Ходеш / Песах / Суккот · не каждый день")

add("בראש חודש ובסוכות — שורות נוספות (לא בכל יום)",
    "En Rosh Jodesj y Sucot — líneas adicionales (no todos los días)",
    "À Roch 'Hodesh et Souccot — versets supplémentaires (pas tous les jours)",
    "На Рош Ходеш и Суккот — дополнительные строки (не каждый день)")

add("On Chanukah — Al haNissim · not recited on ordinary days",
    "En Janucá — Al HaNissim · no se recita en días ordinarios",
    "À Hanoucca — Al HaNissim · non récité les jours ordinaires",
    "На Ханукe — Al HaNissim · не читается в обычные дни")

add("On Purim — Al haNissim · not recited on ordinary days",
    "En Purim — Al HaNissim · no se recita en días ordinarios",
    "À Pourim — Al HaNissim · non récité les jours ordinaires",
    "На Пуриме — Al HaNissim · не читается в обычные дни")

add("Yaaleh Veyavo — Rosh Chodesh, Passover, or Sukkot (not recited on ordinary weekdays)",
    "Yaaleh Veyavo — Rosh Jodesj, Pesaj o Sucot (no se recita en días laborables ordinarios)",
    "Yaaleh Veyavo — Roch 'Hodesh, Pessah ou Souccot (non récité les jours ordinaires de semaine)",
    "Yaaleh Veyavo — \u0420\u043e\u0448 \u0425\u043e\u0434\u0435\u0448, \u041f\u0435\u0441\u0430\u0445 \u0438\u043b\u0438 \u0421\u0443\u043a\u043a\u043e\u0442 (\u043d\u0435 \u0447\u0438\u0442\u0430\u0435\u0442\u0441\u044f \u0432 \u043e\u0431\u044b\u0447\u043d\u044b\u0435 \u0431\u0443\u0434\u043d\u0438)")

add("On Rosh Chodesh and Sukkot — extra Harachaman lines (not every day)",
    "En Rosh Jodesj y Sucot — líneas Harachaman adicionales (no todos los días)",
    "À Roch 'Hodesh et Souccot — versets Harachaman supplémentaires (pas tous les jours)",
    "На \u0420\u043e\u0448 \u0425\u043e\u0434\u0435\u0448 \u0438 \u0421\u0443\u043a\u043a\u043e\u0442 — \u0434\u043e\u043f\u043e\u043b\u043d\u0438\u0442\u0435\u043b\u044c\u043d\u044b\u0435 \u0441\u0442\u0440\u043e\u043a\u0438 Harachaman (\u043d\u0435 \u043a\u0430\u0436\u0434\u044b\u0439 \u0434\u0435\u043d\u044c)")

# --- Brachot (English keys) ---
add("Blessed are You, Lord our God, King of the universe, Who formed man with wisdom and created within him many openings and many hollow spaces. It is obvious and known before Your Throne of Glory that if even one of them would be opened, or if even one of them would be sealed, it would be impossible to survive and to stand before You even for one hour. Blessed are You, Lord, Who heals all flesh and acts wondrously.",
    "Bendito eres Tú, Hashem, Eloheinu, Rey del universo, que formaste al hombre con sabiduría y creaste en él muchas aberturas y muchas cavidades. Es evidente y conocido ante Tu Trono de Gloria que si aun una de ellas se abriera, o si aun una de ellas se cerrara, sería imposible sobrevivir y permanecer ante Ti siquiera una hora. Bendito eres Tú, Hashem, que sanas toda carne y haces maravillas.",
    "Béni sois-Tu, Éternel notre Dieu, Roi de l'univers, qui as formé l'homme avec sagesse et créé en lui de nombreuses ouvertures et de nombreuses cavités. Il est évident et connu devant Ton Trône de Gloire que si l'une d'elles s'ouvrait, ou si l'une d'elles se fermait, il serait impossible de survivre et de subsister devant Toi ne serait-ce qu'une heure. Béni sois-Tu, Éternel, qui guéris toute chair et opères des merveilles.",
    "Благословен Ты, Г-сподь, Б-г наш, Царь вселенной, сотворивший человека с мудростью и создавший в нём множество отверстий и полостей. Ясно и известно перед Твоим Престолом Славы, что если бы одно из них открылось или одно из них закрылось, невозможно было бы выжить и стоять перед Тобой хотя бы один час. Благословен Ты, Г-сподь, исцеляющий всю плоть и творящий чудеса.")

add("Blessed are You, Lord our God, King of the universe, Who brings forth bread from the earth.",
    "Bendito eres Tú, Hashem, Eloheinu, Rey del universo, que sacas el pan de la tierra.",
    "Béni sois-Tu, Éternel notre Dieu, Roi de l'univers, qui fais sortir le pain de la terre.",
    "Благословен Ты, Г-сподь, Б-г наш, Царь вселенной, изводящий хлеб из земли.")

add("Blessed are You, Lord our God, King of the universe, Who creates various kinds of sustenance.",
    "Bendito eres Tú, Hashem, Eloheinu, Rey del universo, que creas diversas clases de sustento.",
    "Béni sois-Tu, Éternel notre Dieu, Roi de l'univers, qui crées diverses sortes de nourriture.",
    "Благословен Ты, Г-сподь, Б-г наш, Царь вселенной, создающий различные виды пищи.")

add("Blessed are You, Lord our God, King of the universe, Who creates the fruit of the vine.",
    "Bendito eres Tú, Hashem, Eloheinu, Rey del universo, que creas el fruto de la vid.",
    "Béni sois-Tu, Éternel notre Dieu, Roi de l'univers, qui crées le fruit de la vigne.",
    "Благословен Ты, Г-сподь, Б-г наш, Царь вселенной, создающий плод виноградной лозы.")

add("Blessed are You, Lord our God, King of the universe, Who creates the fruit of the tree.",
    "Bendito eres Tú, Hashem, Eloheinu, Rey del universo, que creas el fruto del árbol.",
    "Béni sois-Tu, Éternel notre Dieu, Roi de l'univers, qui crées le fruit de l'arbre.",
    "Благословен Ты, Г-сподь, Б-г наш, Царь вселенной, создающий плод дерева.")

add("Blessed are You, Lord our God, King of the universe, Who creates the fruit of the earth.",
    "Bendito eres Tú, Hashem, Eloheinu, Rey del universo, que creas el fruto de la tierra.",
    "Béni sois-Tu, Éternel notre Dieu, Roi de l'univers, qui crées le fruit de la terre.",
    "Благословен Ты, Г-сподь, Б-г наш, Царь вселенной, создающий плод земли.")

add("Blessed are You, Lord our God, King of the universe, by Whose word all things came to be.",
    "Bendito eres Tú, Hashem, Eloheinu, Rey del universo, por cuya palabra todo fue creado.",
    "Béni sois-Tu, Éternel notre Dieu, Roi de l'univers, par la Parole duquel tout a été créé.",
    "Благословен Ты, Г-сподь, Б-г наш, Царь вселенной, которым всё сотворено словом Его.")

add("Blessed are You, Lord our God, King of the universe, Who creates numerous living things with their deficiencies, for all that You have created with which to sustain the life of every being. Blessed is He who is the Life of the worlds.",
    "Bendito eres Tú, Hashem, Eloheinu, Rey del universo, que creas numerosos seres vivos con sus necesidades, con todo lo que has creado para sustentar la vida de todo ser viviente. Bendito es Él, la Vida de los mundos.",
    "Béni sois-Tu, Éternel notre Dieu, Roi de l'univers, qui crées de nombreux êtres vivants avec leurs besoins, pour tout ce que Tu as créé afin de sustenter la vie de tout être. Béni soit Celui qui est la Vie des mondes.",
    "Благословен Ты, Г-сподь, Б-г наш, Царь вселенной, создающий множество живых существ с их потребностями, всем, что Ты создал для поддержания жизни всякого живого. Благословен Тот, Кто есть Жизнь миров.")

add("Blessed are You, Lord our God, King of the universe, Who creates various kinds of spices.",
    "Bendito eres Tú, Hashem, Eloheinu, Rey del universo, que creas diversas clases de especias.",
    "Béni sois-Tu, Éternel notre Dieu, Roi de l'univers, qui crées diverses sortes d'épices.",
    "Благословен Ты, Г-сподь, Б-г наш, Царь вселенной, создающий различные виды благовоний.")

add("Blessed are You, Lord our God, King of the universe, Who does the works of creation.",
    "Bendito eres Tú, Hashem, Eloheinu, Rey del universo, que haces las obras de la creación.",
    "Béni sois-Tu, Éternel notre Dieu, Roi de l'univers, qui fais les œuvres de la création.",
    "Благословен Ты, Г-сподь, Б-г наш, Царь вселенной, творящий дела творения.")

add("Blessed are You, Lord our God, King of the universe, Whose power and might fill the world.",
    "Bendito eres Tú, Hashem, Eloheinu, Rey del universo, cuyo poder y fuerza llenan el mundo.",
    "Béni sois-Tu, Éternel notre Dieu, Roi de l'univers, dont la puissance et la force remplissent le monde.",
    "Благословен Ты, Г-сподь, Б-г наш, Царь вселенной, сила и могущество Которого наполняют мир.")

# Hebrew brachot with nikud — same translations as English counterparts
HE_BRACHA = {
    "בָּרוּךְ אַתָּה ה׳ אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם אֲשֶׁר יָצַר אֶת הָאָדָם בְּחָכְמָה וּבָרָא בוֹ נְקָבִים נְקָבִים חֲלוּלִים חֲלוּלִים גָּלוּי וְיָדוּעַ לִפְנֵי כִסֵּא כְבוֹדֶךָ שֶׁאִם יִפָּתֵחַ אֶחָד מֵהֶם אוֹ יִסָּתֵם אֶחָד מֵהֶם אִי אֶפְשַׁר לְהִתְקַיֵּם וְלַעֲמוֹד לְפָנֶיךָ אֲפִילוּ שָׁעָה אֶחָת בָּרוּךְ אַתָּה ה׳ רוֹפֵא כָל בָּשָׂר וּמַפְלִיא לַעֲשׂוֹת׃":
        "Blessed are You, Lord our God, King of the universe, Who formed man with wisdom and created within him many openings and many hollow spaces. It is obvious and known before Your Throne of Glory that if even one of them would be opened, or if even one of them would be sealed, it would be impossible to survive and to stand before You even for one hour. Blessed are You, Lord, Who heals all flesh and acts wondrously.",
    "בָּרוּךְ אַתָּה ה׳ אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם הַמּוֹצִיא לֶחֶם מִן הָאָרֶץ׃":
        "Blessed are You, Lord our God, King of the universe, Who brings forth bread from the earth.",
    "בָּרוּךְ אַתָּה ה׳ אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם בּוֹרֵא מִינֵי מְזוֹנוֹת׃":
        "Blessed are You, Lord our God, King of the universe, Who creates various kinds of sustenance.",
    "בָּרוּךְ אַתָּה ה׳ אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם בּוֹרֵא פְּרִי הַגָּפֶן׃":
        "Blessed are You, Lord our God, King of the universe, Who creates the fruit of the vine.",
    "בָּרוּךְ אַתָּה ה׳ אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם בּוֹרֵא פְּרִי הָעֵץ׃":
        "Blessed are You, Lord our God, King of the universe, Who creates the fruit of the tree.",
    "בָּרוּךְ אַתָּה ה׳ אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם בּוֹרֵא פְּרִי הָאֲדָמָה׃":
        "Blessed are You, Lord our God, King of the universe, Who creates the fruit of the earth.",
    "בָּרוּךְ אַתָּה ה׳ אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם שֶׁהַכֹּל נִהְיָה בִּדְבָרוֹ׃":
        "Blessed are You, Lord our God, King of the universe, by Whose word all things came to be.",
    "בָּרוּךְ אַתָּה ה׳ אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם בּוֹרֵא נְפָשׁוֹת רַבּוֹת וְחֶסְרוֹנָן עַל כָּל מַה שֶּׁבָּרָאתָ לְהַחֲיוֹת בָּהֶם נֶפֶשׁ כָּל חָי בָּרוּךְ חֵי הָעוֹלָמִים׃":
        "Blessed are You, Lord our God, King of the universe, Who creates numerous living things with their deficiencies, for all that You have created with which to sustain the life of every being. Blessed is He who is the Life of the worlds.",
    "בָּרוּךְ אַתָּה ה׳ אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם בּוֹרֵא מִינֵי בְשָׂמִים׃":
        "Blessed are You, Lord our God, King of the universe, Who creates various kinds of spices.",
    "בָּרוּךְ אַתָּה ה׳ אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם עוֹשֶׂה מַעֲשֵׂה בְרֵאשִׁית׃":
        "Blessed are You, Lord our God, King of the universe, Who does the works of creation.",
    "בָּרוּךְ אַתָּה ה׳ אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם שֶׁכֹּחוֹ וּגְבוּרָתוֹ מָלֵא עוֹלָם׃":
        "Blessed are You, Lord our God, King of the universe, Whose power and might fill the world.",
}

for he_key, en_key in HE_BRACHA.items():
    tr = T[en_key]
    T[he_key] = {"es": tr["es"], "fr": tr["fr"], "ru": tr["ru"]}

# fmt: on — long texts loaded from companion module
from _prayers_liturgy_long import add_long_texts  # noqa: E402

add_long_texts(add, T)


def lookup(key: str, he_to_en: dict[str, str]) -> dict[str, str]:
    if key in T:
        return T[key]
    if key in he_to_en and he_to_en[key] in T:
        return T[he_to_en[key]]
    raise KeyError(f"Missing translation for: {key[:80]!r}...")


def main() -> None:
    keys: list[str] = json.loads(KEYS.read_text(encoding="utf-8"))
    en_to_he, he_to_en = load_pairs()
    out: dict[str, dict[str, str]] = {lang: {} for lang in ("he", "es", "fr", "ru")}

    for key in keys:
        entry = lookup(key, he_to_en)
        if is_hebrew_key(key):
            out["he"][key] = key
        else:
            out["he"][key] = entry.get("he") or en_to_he.get(key, key)
        for lang in ("es", "fr", "ru"):
            out[lang][key] = entry[lang]

    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps(out, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    for lang in ("he", "es", "fr", "ru"):
        print(f"{lang}: {len(out[lang])} keys")


if __name__ == "__main__":
    main()
