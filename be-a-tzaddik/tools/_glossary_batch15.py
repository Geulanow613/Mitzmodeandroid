"""Human-quality fixes batch 15 — bein kodesh l'kodesh glossary + explainer polish."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = json.loads((ROOT / "data" / "translation-catalog" / "strings.json").read_text(encoding="utf-8"))[
    "strings"
]

BEIN_K_K_KEY = next(k for k in CATALOG if k == "bein kodesh l'kodesh")
BEIN_K_K_MEANS_KEY = next(k for k in CATALOG if k.startswith("Bein kodesh l'kodesh means"))
BEIN_K_K_PREFIX_KEY = next(k for k in CATALOG if k.startswith("bein kodesh l'kodesh — Bein kodesh l'kodesh means"))

_BEIN_K_K_ES = (
    "Bein kodesh l'kodesh significa «entre lo sagrado y lo sagrado». En la Havdalá, cuando el "
    "Shabat conduce al Yom Tov, se declara que un nivel de santidad (Shabat) termina mientras "
    "comienza otro (la festividad), y no bein kodesh l'chol (de lo sagrado a lo profano). "
    "Esa formulación refleja la realidad espiritual: la noche de Shabat puede seguir siendo "
    "sagrada, pero la naturaleza del día cambia. También se dice Baruch hamavdil bein kodesh "
    "l'kodesh después de la caída de la noche para permitir melajá permitida en Yom Tov antes "
    "del Kidush."
)

_BEIN_K_K_FR = (
    "Bein kodesh l'kodesh signifie « entre saint et saint ». Dans la Havdala, lorsque le "
    "Chabbat mène au Yom Tov, l'on déclare qu'un niveau de sainteté (Chabbat) se termine "
    "alors qu'un autre (la fête) commence — et non pas bein kodesh l'chol (du saint au "
    "profane). Cette formulation reflète la réalité spirituelle : le samedi soir peut rester "
    "sacré, mais la nature du jour change. Baruch hamavdil bein kodesh l'kodesh se dit "
    "également après la tombée de la nuit pour permettre les mélakhot autorisées à Yom Tov "
    "avant le Kiddouch."
)

_BEIN_K_K_RU = (
    "Bein kodesh l'kodesh означает «между святым и святым». В хавдале, когда Шаббат "
    "переходит в Йом-Тов, заявляется, что один уровень святости (Шаббат) заканчивается, "
    "когда начинается другой (праздник), а не bein kodesh l'chol (от святого к будню). "
    "Эта формулировка соответствует духовной реальности: субботний вечер может оставаться "
    "священным, но характер дня меняется. Baruch hamavdil bein kodesh l'kodesh также "
    "произносят после наступления темноты, чтобы разрешить мелаху Йом-Това перед Кидушем."
)

BATCH15_ES: dict[str, str] = {
    BEIN_K_K_KEY: "bein kodesh l'kodesh (entre lo sagrado y lo sagrado)",
    BEIN_K_K_MEANS_KEY: _BEIN_K_K_ES,
    BEIN_K_K_PREFIX_KEY: f"bein kodesh l'kodesh — {_BEIN_K_K_ES}",
}

BATCH15_FR: dict[str, str] = {
    BEIN_K_K_KEY: "bein kodesh l'kodesh (entre saint et saint)",
    BEIN_K_K_MEANS_KEY: _BEIN_K_K_FR,
    BEIN_K_K_PREFIX_KEY: f"bein kodesh l'kodesh — {_BEIN_K_K_FR}",
}

BATCH15_RU: dict[str, str] = {
    BEIN_K_K_MEANS_KEY: _BEIN_K_K_RU,
    BEIN_K_K_PREFIX_KEY: f"bein kodesh l'kodesh — {_BEIN_K_K_RU}",
}
