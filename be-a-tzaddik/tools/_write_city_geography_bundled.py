#!/usr/bin/env python3
"""Generate shared/.../city-geography.json from manual-cities.json."""

from __future__ import annotations

import json
import sys
from pathlib import Path

import pycountry
from babel import Locale

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(Path(__file__).resolve().parent))

if hasattr(sys.stdout, "reconfigure"):
    sys.stdout.reconfigure(encoding="utf-8", errors="replace")

from _city_geo_translit import latin_to_cyrillic, latin_to_hebrew  # noqa: E402

CANADA_PROVINCES = {"ON", "QC", "BC", "AB", "MB", "SK", "NS", "NB", "NL", "PE"}
LANGS = ("he", "es", "fr", "ru")

COUNTRY_ALPHA2: dict[str, str | None] = {
    "USA": "US",
    "United Kingdom": "GB",
    "DR Congo": "CD",
    "Ivory Coast": "CI",
    "Czech Republic": "CZ",
    "Hong Kong": "HK",
    "Palestine": "PS",
    "North Macedonia": "MK",
    "South Korea": "KR",
    "Taiwan": "TW",
    "French Guiana": "GF",
    "Puerto Rico": "PR",
    "New Caledonia": "NC",
    "Turkey": "TR",
    "Unknown": None,
}

LOCALE_BY_LANG = {lang: Locale.parse(lang) for lang in LANGS}

REGION_EN: dict[str, str] = {
    "AL": "Alabama", "AK": "Alaska", "AZ": "Arizona", "AR": "Arkansas",
    "CA": "California", "CO": "Colorado", "CT": "Connecticut", "DE": "Delaware",
    "DC": "District of Columbia", "FL": "Florida", "GA": "Georgia", "HI": "Hawaii",
    "ID": "Idaho", "IL": "Illinois", "IN": "Indiana", "IA": "Iowa", "KS": "Kansas",
    "KY": "Kentucky", "LA": "Louisiana", "ME": "Maine", "MD": "Maryland",
    "MA": "Massachusetts", "MI": "Michigan", "MN": "Minnesota", "MS": "Mississippi",
    "MO": "Missouri", "MT": "Montana", "NE": "Nebraska", "NV": "Nevada",
    "NH": "New Hampshire", "NJ": "New Jersey", "NM": "New Mexico", "NY": "New York",
    "NC": "North Carolina", "ND": "North Dakota", "OH": "Ohio", "OK": "Oklahoma",
    "OR": "Oregon", "PA": "Pennsylvania", "RI": "Rhode Island", "SC": "South Carolina",
    "SD": "South Dakota", "TN": "Tennessee", "TX": "Texas", "UT": "Utah",
    "VT": "Vermont", "VA": "Virginia", "WA": "Washington", "WV": "West Virginia",
    "WI": "Wisconsin", "WY": "Wyoming",
    "AB": "Alberta", "BC": "British Columbia", "MB": "Manitoba", "NB": "New Brunswick",
    "NL": "Newfoundland and Labrador", "NS": "Nova Scotia", "NT": "Northwest Territories",
    "NU": "Nunavut", "ON": "Ontario", "PE": "Prince Edward Island", "QC": "Quebec",
    "SK": "Saskatchewan", "YT": "Yukon",
}

REGION_ES: dict[str, str] = {
    "NY": "Nueva York", "NM": "Nuevo México", "NC": "Carolina del Norte",
    "SC": "Carolina del Sur", "ND": "Dakota del Norte", "SD": "Dakota del Sur",
    "WV": "Virginia Occidental", "DC": "Distrito de Columbia",
    "BC": "Columbia Británica", "NL": "Terranova y Labrador", "NT": "Territorios del Noroeste",
    "PE": "Isla del Príncipe Eduardo", "QC": "Quebec", "YT": "Yukón",
}

REGION_FR: dict[str, str] = {
    "CA": "Californie", "NY": "New York", "NM": "Nouveau-Mexique", "NC": "Caroline du Nord",
    "SC": "Caroline du Sud", "ND": "Dakota du Nord", "SD": "Dakota du Sud",
    "WV": "Virginie-Occidentale", "DC": "District de Columbia",
    "BC": "Colombie-Britannique", "NL": "Terre-Neuve-et-Labrador",
    "NT": "Territoires du Nord-Ouest", "PE": "Île-du-Prince-Édouard", "QC": "Québec", "YT": "Yukon",
}

# Hand-tuned where phonetic transliteration is misleading (e.g. Ohio → והיו).
REGION_HE: dict[str, str] = {
    "CA": "קליפורניה", "NY": "ניו יורק", "TX": "טקסס", "FL": "פלורידה", "IL": "אילינוי",
    "PA": "פנסילבניה", "OH": "אוהיו", "MI": "מישיגן", "GA": "ג'ורג'יה", "NC": "קרוליינה הצפונית",
    "NJ": "ניו ג'רזי", "VA": "וירג'יניה", "WA": "וושינגטון", "MA": "מסצ'וסטס", "AZ": "אריזונה",
    "ON": "אונטריו", "QC": "קוויבק", "BC": "קולומביה הבריטית",
}

REGION_RU: dict[str, str] = {code: latin_to_cyrillic(name) for code, name in REGION_EN.items()}

# Hebrew city names (Israel + common exonyms)
CITY_HE_BY_ID: dict[str, str] = {
    "jlm": "ירושלים",
    "tlv": "תל אביב",
    "haifa": "חיפה",
    "ashdod": "אשדוד",
    "ashkelon": "אשקלון",
    "beersheba": "באר שבע",
    "bnei_brak": "בני ברק",
    "efrat": "אפרת",
    "eilat": "אילת",
    "herzliya": "הרצליה",
    "holon": "חולון",
    "kfar_saba": "כפר סבא",
    "modiin": "מודיעין",
    "netanya": "נתניה",
    "petah_tikva": "פתח תקווה",
    "raanana": "רעננה",
    "ramat_gan": "רמת גן",
    "rishon_lezion": "ראשון לציון",
    "safed": "צפת",
    "tiberias": "טבריה",
    "gaza_city": "עזה",
    "ramallah": "רמאללה",
    "new_york": "ניו יורק",
    "london": "לונדון",
    "paris": "פריז",
    "moscow": "מוסקבה",
    "rome": "רומא",
    "madrid": "מדריד",
    "berlin": "ברלין",
    "amsterdam": "אמסטרדם",
    "vienna": "וינה",
    "prague": "פראג",
    "warsaw": "ורשה",
    "budapest": "בודפשט",
    "athens": "אתונה",
    "istanbul": "איסטנבול",
    "cairo": "קהיר",
    "beirut": "ביירות",
    "amman": "עמאן",
    "tokyo": "טוקיו",
    "beijing": "בייג'ינג",
    "shanghai": "שאנגחאי",
    "sydney": "סידני",
    "melbourne": "מלבורן",
    "toronto": "טורונטו",
    "montreal": "מונטריאול",
}

CITY_I18N: dict[str, dict[str, str]] = {
    "jlm": {"es": "Jerusalén", "fr": "Jérusalem", "ru": "Иерусалим"},
    "tlv": {"es": "Tel Aviv", "fr": "Tel Aviv", "ru": "Тель-Авив"},
    "haifa": {"es": "Haifa", "fr": "Haïfa", "ru": "Хайфа"},
    "beersheba": {"es": "Beersheba", "fr": "Beersheba", "ru": "Беэр-Шева"},
    "new_york": {"es": "Nueva York", "fr": "New York", "ru": "Нью-Йорк"},
    "los_angeles": {"es": "Los Ángeles", "fr": "Los Angeles", "ru": "Лос-Анджелес"},
    "london": {"es": "Londres", "fr": "Londres", "ru": "Лондон"},
    "paris": {"es": "París", "fr": "Paris", "ru": "Париж"},
    "moscow": {"es": "Moscú", "fr": "Moscou", "ru": "Москва"},
    "rome": {"es": "Roma", "fr": "Rome", "ru": "Рим"},
    "madrid": {"es": "Madrid", "fr": "Madrid", "ru": "Мадрид"},
    "berlin": {"es": "Berlín", "fr": "Berlin", "ru": "Берлин"},
    "amsterdam": {"es": "Ámsterdam", "fr": "Amsterdam", "ru": "Амстердам"},
    "vienna": {"es": "Viena", "fr": "Vienne", "ru": "Вена"},
    "prague": {"es": "Praga", "fr": "Prague", "ru": "Прага"},
    "warsaw": {"es": "Varsovia", "fr": "Varsovie", "ru": "Варшава"},
    "budapest": {"es": "Budapest", "fr": "Budapest", "ru": "Будапешт"},
    "athens": {"es": "Atenas", "fr": "Athènes", "ru": "Афины"},
    "istanbul": {"es": "Estambul", "fr": "Istanbul", "ru": "Стамбул"},
    "cairo": {"es": "El Cairo", "fr": "Le Caire", "ru": "Каир"},
    "beirut": {"es": "Beirut", "fr": "Beyrouth", "ru": "Бейрут"},
    "amman": {"es": "Amán", "fr": "Amman", "ru": "Амман"},
    "dubai": {"es": "Dubái", "fr": "Dubaï", "ru": "Дубай"},
    "mumbai": {"es": "Bombay", "fr": "Bombay", "ru": "Мумбаи"},
    "delhi": {"es": "Delhi", "fr": "Delhi", "ru": "Дели"},
    "beijing": {"es": "Pekín", "fr": "Pékin", "ru": "Пекин"},
    "shanghai": {"es": "Shanghái", "fr": "Shanghai", "ru": "Шанхай"},
    "tokyo": {"es": "Tokio", "fr": "Tokyo", "ru": "Токио"},
    "seoul": {"es": "Seúl", "fr": "Séoul", "ru": "Сеул"},
    "sydney": {"es": "Sídney", "fr": "Sydney", "ru": "Сидней"},
    "melbourne": {"es": "Melbourne", "fr": "Melbourne", "ru": "Мельбурн"},
    "toronto": {"es": "Toronto", "fr": "Toronto", "ru": "Торонто"},
    "montreal": {"es": "Montreal", "fr": "Montréal", "ru": "Монреаль"},
    "mexico_city": {"es": "Ciudad de México", "fr": "Mexico", "ru": "Мехико"},
    "buenos_aires": {"es": "Buenos Aires", "fr": "Buenos Aires", "ru": "Буэнос-Айрес"},
    "sao_paulo": {"es": "São Paulo", "fr": "São Paulo", "ru": "Сан-Паулу"},
    "rio_de_janeiro": {"es": "Río de Janeiro", "fr": "Rio de Janeiro", "ru": "Рио-де-Жанейро"},
    "gaza_city": {"es": "Gaza", "fr": "Gaza", "ru": "Газа"},
    "ramallah": {"es": "Ramala", "fr": "Ramallah", "ru": "Рамаллах"},
    "chicago": {"es": "Chicago", "fr": "Chicago", "ru": "Чикаго"},
    "san_francisco": {"es": "San Francisco", "fr": "San Francisco", "ru": "Сан-Франциско"},
    "boston": {"es": "Boston", "fr": "Boston", "ru": "Бостон"},
    "miami": {"es": "Miami", "fr": "Miami", "ru": "Майами"},
    "washington": {"es": "Washington", "fr": "Washington", "ru": "Вашингтон"},
}

CITY_I18N_BY_EN: dict[str, dict[str, str]] = {
    "Jerusalem": {"es": "Jerusalén", "fr": "Jérusalem", "ru": "Иерусалим"},
    "Tel Aviv": {"es": "Tel Aviv", "fr": "Tel Aviv", "ru": "Тель-Авив"},
    "Haifa": {"es": "Haifa", "fr": "Haïfa", "ru": "Хайфа"},
    "New York": {"es": "Nueva York", "fr": "New York", "ru": "Нью-Йорк"},
    "Los Angeles": {"es": "Los Ángeles", "fr": "Los Angeles", "ru": "Лос-Анджелес"},
    "London": {"es": "Londres", "fr": "Londres", "ru": "Лондон"},
    "Paris": {"es": "París", "fr": "Paris", "ru": "Париж"},
    "Moscow": {"es": "Moscú", "fr": "Moscou", "ru": "Москва"},
    "Rome": {"es": "Roma", "fr": "Rome", "ru": "Рим"},
    "Madrid": {"es": "Madrid", "fr": "Madrid", "ru": "Мадрид"},
    "Berlin": {"es": "Berlín", "fr": "Berlin", "ru": "Берлин"},
    "Vienna": {"es": "Viena", "fr": "Vienne", "ru": "Вена"},
    "Prague": {"es": "Praga", "fr": "Prague", "ru": "Прага"},
    "Warsaw": {"es": "Varsovia", "fr": "Varsovie", "ru": "Варшава"},
    "Budapest": {"es": "Budapest", "fr": "Budapest", "ru": "Будапешт"},
    "Athens": {"es": "Atenas", "fr": "Athènes", "ru": "Афины"},
    "Istanbul": {"es": "Estambul", "fr": "Istanbul", "ru": "Стамбул"},
    "Cairo": {"es": "El Cairo", "fr": "Le Caire", "ru": "Каир"},
    "Beirut": {"es": "Beirut", "fr": "Beyrouth", "ru": "Бейрут"},
    "Amman": {"es": "Amán", "fr": "Amman", "ru": "Амман"},
    "Beijing": {"es": "Pekín", "fr": "Pékin", "ru": "Пекин"},
    "Shanghai": {"es": "Shanghái", "fr": "Shanghai", "ru": "Шанхай"},
    "Tokyo": {"es": "Tokio", "fr": "Tokyo", "ru": "Токио"},
    "Seoul": {"es": "Seúl", "fr": "Séoul", "ru": "Сеул"},
    "Sydney": {"es": "Sídney", "fr": "Sydney", "ru": "Сидней"},
    "Melbourne": {"es": "Melbourne", "fr": "Melbourne", "ru": "Мельбурн"},
    "Toronto": {"es": "Toronto", "fr": "Toronto", "ru": "Торонто"},
    "Montreal": {"es": "Montreal", "fr": "Montréal", "ru": "Монреаль"},
    "Mexico City": {"es": "Ciudad de México", "fr": "Mexico", "ru": "Мехико"},
    "Buenos Aires": {"es": "Buenos Aires", "fr": "Buenos Aires", "ru": "Буэнос-Айрес"},
    "São Paulo": {"es": "São Paulo", "fr": "São Paulo", "ru": "Сан-Паулу"},
    "Rio de Janeiro": {"es": "Río de Janeiro", "fr": "Rio de Janeiro", "ru": "Рио-де-Жанейро"},
    "Gaza City": {"es": "Gaza", "fr": "Gaza", "ru": "Газа"},
    "Ramallah": {"es": "Ramala", "fr": "Ramallah", "ru": "Рамаллах"},
    "Chicago": {"es": "Chicago", "fr": "Chicago", "ru": "Чикаго"},
    "San Francisco": {"es": "San Francisco", "fr": "San Francisco", "ru": "Сан-Франциско"},
    "Boston": {"es": "Boston", "fr": "Boston", "ru": "Бостон"},
    "Miami": {"es": "Miami", "fr": "Miami", "ru": "Майами"},
    "Washington": {"es": "Washington", "fr": "Washington", "ru": "Вашингтон"},
    "Dubai": {"es": "Dubái", "fr": "Dubaï", "ru": "Дубай"},
    "Mumbai": {"es": "Bombay", "fr": "Bombay", "ru": "Мумбаи"},
    "Delhi": {"es": "Delhi", "fr": "Delhi", "ru": "Дели"},
}


def country_alpha2(en: str) -> str | None:
    if en in COUNTRY_ALPHA2:
        return COUNTRY_ALPHA2[en]
    for country in pycountry.countries:
        if country.name == en:
            return country.alpha_2
    try:
        return pycountry.countries.search_fuzzy(en)[0].alpha_2
    except LookupError:
        return None


def country_name(en: str, lang: str) -> str:
    if en == "Unknown":
        return {"he": "לא ידוע", "es": "Desconocido", "fr": "Inconnu", "ru": "Неизвестно"}[lang]
    code = country_alpha2(en)
    if code:
        localized = LOCALE_BY_LANG[lang].territories.get(code)
        if localized:
            return localized
    if lang == "he":
        return latin_to_hebrew(en)
    if lang == "ru":
        return latin_to_cyrillic(en)
    return en


def region_name(code: str, lang: str) -> str:
    en = REGION_EN.get(code, code)
    if lang == "es":
        return REGION_ES.get(code, en)
    if lang == "fr":
        return REGION_FR.get(code, en)
    if lang == "he":
        return REGION_HE.get(code, latin_to_hebrew(en))
    if lang == "ru":
        return REGION_RU.get(code, latin_to_cyrillic(en))
    return en


def city_name(city_id: str, en_name: str, lang: str) -> str:
    if lang == "he" and city_id in CITY_HE_BY_ID:
        return CITY_HE_BY_ID[city_id]
    if city_id in CITY_I18N and lang in CITY_I18N[city_id]:
        return CITY_I18N[city_id][lang]
    if en_name in CITY_I18N_BY_EN and lang in CITY_I18N_BY_EN[en_name]:
        return CITY_I18N_BY_EN[en_name][lang]
    if lang == "he":
        return latin_to_hebrew(en_name)
    if lang == "ru":
        return latin_to_cyrillic(en_name)
    return en_name


def english_display_label(city: dict) -> str:
    label = city["label"]
    suffix = label.rsplit(", ", 1)[1].strip() if ", " in label else ""
    if len(suffix) == 2 and suffix.isupper():
        country = "Canada" if suffix in CANADA_PROVINCES else "USA"
    elif suffix:
        country = suffix
    elif city["timezoneId"] == "Asia/Jerusalem":
        country = "Israel"
    elif city["timezoneId"] == "Asia/Singapore":
        country = "Singapore"
    elif city["timezoneId"] == "Asia/Hong_Kong":
        country = "Hong Kong"
    elif city["timezoneId"].startswith("Australia/"):
        country = "Australia"
    elif city["timezoneId"] == "Pacific/Auckland":
        country = "New Zealand"
    else:
        country = "Unknown"
    if label.endswith(", " + country):
        return label
    return f"{label}, {country}"


def parse_parts(display: str) -> tuple[str, str | None, str]:
    country = display.rsplit(", ", 1)[-1]
    rest = display[: -len(country) - 2]
    if ", " in rest:
        city_en, region = rest.rsplit(", ", 1)
        return city_en.strip(), region.strip(), country
    return rest.strip(), None, country


def localized_label(city: dict, lang: str) -> str:
    display = english_display_label(city)
    city_en, region_code, country_en = parse_parts(display)
    c_name = city_name(city["id"], city_en, lang)
    c_country = country_name(country_en, lang)
    if region_code and region_code in REGION_EN:
        if REGION_EN[region_code].casefold() != city_en.casefold():
            c_region = region_name(region_code, lang)
            return f"{c_name}, {c_region}, {c_country}"
    return f"{c_name}, {c_country}"


def main() -> None:
    cities_path = ROOT / "shared/src/commonMain/composeResources/files/manual-cities.json"
    out_path = ROOT / "shared/src/commonMain/composeResources/files/city-geography.json"
    cities = json.loads(cities_path.read_text(encoding="utf-8"))["cities"]

    labels: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    search_aliases: dict[str, list[str]] = {}

    for city in cities:
        cid = city["id"]
        en_label = english_display_label(city)
        aliases = {en_label.lower(), city["label"].lower(), cid.lower()}
        for lang in LANGS:
            loc = localized_label(city, lang)
            labels[lang][cid] = loc
            aliases.add(loc.lower())
            city_en, _, _ = parse_parts(en_label)
            aliases.add(city_en.lower())
        search_aliases[cid] = sorted(aliases)

    payload = {"version": 1, "labels": labels, "searchAliases": search_aliases}
    out_path.write_text(json.dumps(payload, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")

    print(f"Wrote {out_path} — {len(cities)} cities × {len(LANGS)} langs")
    for lang in LANGS:
        print(f"  {lang} jlm: {labels[lang]['jlm']}")
        print(f"  {lang} new_york: {labels[lang]['new_york']}")
        print(f"  {lang} akron: {labels[lang]['akron']}")


if __name__ == "__main__":
    main()
