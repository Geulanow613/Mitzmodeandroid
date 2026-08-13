"""Human-quality fixes batch 13 — Three Weeks intro, shloshim, FR glossary titles."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = json.loads((ROOT / "data" / "translation-catalog" / "strings.json").read_text(encoding="utf-8"))[
    "strings"
]

THREE_WEEKS_INTRO_KEY = next(
    k
    for k in CATALOG
    if k.startswith("The Three Weeks (בין המצרים) from 17 Tammuz")
    and "Ashkenazi custom observes" not in k
    and "Chabad follows" not in k
    and "Sephardic and Edot" not in k
)

SHLOSHIM_KEY = next(k for k in CATALOG if k.startswith("Shloshim is thirty days"))
SHLOSHIM_PREFIX_KEY = next(k for k in CATALOG if k.startswith("shloshim — Shloshim is thirty"))

THREE_WEEKS_INTRO_FR = (
    "Les Trois Semaines (Bein HaMetzarim / בין המצרים), du 17 Tamouz au 9 Av, commémorent "
    "la destruction du Temple et les tragédies du peuple juif.\n\n"
    "Pourquoi nous pleurons :\n"
    "• Le 17 Tamouz, les murailles de Jérusalem ont été percées ; le 9 Av, les deux Temples "
    "ont été détruits, ainsi que d'autres calamités nationales.\n\n"
    "Chabbat pendant les Trois Semaines : les pratiques de deuil ne s'appliquent pas à "
    "Chabbat lui-même — observez Chabbat pleinement."
)

THREE_WEEKS_INTRO_RU = (
    "Три недели (Бейн а-Мецарим / בין המצרים) — с 17 Тамуза до 9 Ава — в память о "
    "разрушении Храма и еврейских трагедиях.\n\n"
    "Почему мы скорбим:\n"
    "• 17 Тамуза прорваны стены Иерусалима; 9 Ава разрушены оба Храма, среди прочих "
    "национальных бедствий.\n\n"
    "Шаббат в Три недели: обычаи траура не применяются в сам Шаббат — полностью "
    "соблюдайте Шаббат."
)

_SHLOSHIM_BODY_FR = (
    "Shloshim est une période de trente jours de célébration sociale réduite après "
    "l'enterrement (pour de nombreux liens de parenté). Les coupes de cheveux et la musique "
    "peuvent être limitées. Après le shloshim, la vie normale reprend, sauf pour les parents — "
    "le Kaddish continue onze mois. Un chevauchement avec le Omer ou les Trois Semaines ajoute "
    "des restrictions — consultez votre rav."
)

_SHLOSHIM_BODY_RU = (
    "Шлошим — тридцать дней сокращённых общественных радостей после погребения "
    "(для многих степеней родства). Стрижка и музыка могут быть ограничены. После шлошима "
    "обычная жизнь возвращается, кроме для родителей — Кадиш продолжается одиннадцать месяцев. "
    "Пересечение календаря с Сефирой или Тремя неделями добавляет ограничения — спросите раввина."
)

BATCH13_FR: dict[str, str] = {
    THREE_WEEKS_INTRO_KEY: THREE_WEEKS_INTRO_FR,
    SHLOSHIM_KEY: _SHLOSHIM_BODY_FR,
    SHLOSHIM_PREFIX_KEY: f"shloshim — {_SHLOSHIM_BODY_FR}",
    # Glossary short titles — distinct from English keys for bundle lookup
    "Al HaNissim": "Al HaNissim (על הניסים)",
    "Arba Minim": "Arba Minim (ארבעה מינים)",
    "Birkat HaMazon": "Birkat HaMazon (bénédiction après le repas)",
    "Birkat Kohanim": "Birkat Kohanim (bénédiction sacerdotale)",
    "Hoshana Raba": "Hoshana Raba (הושענא רבה)",
    "Matan Torah": "Matan Torah (don de la Torah)",
    "Normal": "Ordinaire",
    "Olam HaBa": "Olam HaBa (monde à venir)",
    "Shalom bayit": "Shalom bayit (paix du foyer)",
    "Shir HaMaalot": "Shir HaMaalot (שיר המעלות)",
    "Yom HaShoah": "Yom HaShoah (jour de la Shoah)",
    "Yom HaZikaron": "Yom HaZikaron (jour du souvenir)",
    "hakarat hatov": "hakarat hatov (reconnaissance)",
    "k'fi daato": "k'fi daato (selon son avis)",
    "kisui rosh": "kisui rosh (couverture de la tête)",
    "lashon hara": "lashon hara (médisance)",
    "shmirat halashon": "shmirat halashon (garde de la langue)",
    "yirat Shamayim": "yirat Shamayim (crainte du Ciel)",
}

BATCH13_RU: dict[str, str] = {
    THREE_WEEKS_INTRO_KEY: THREE_WEEKS_INTRO_RU,
    SHLOSHIM_KEY: _SHLOSHIM_BODY_RU,
    SHLOSHIM_PREFIX_KEY: f"shloshim — {_SHLOSHIM_BODY_RU}",
}
