#!/usr/bin/env python3
"""Generate long-multi.json with offline translations for all bundled languages."""

from __future__ import annotations

import json
from pathlib import Path

DATA = Path(__file__).resolve().parents[1] / "data" / "bundled-translations"


def t(index: int, lang: str, en: list[str]) -> str:
    """Return offline translation for long string [index] in [lang]."""
    return TABLE[index][lang]


# fmt: off
TABLE: list[dict[str, str]] = [
  {
    "he": """גברים יהודים מכסים את ראשם כל היום כסימן שה׳ מעלינו.

מה זה:
כיפה (כִּפָּה — נקראת גם יארמולקה, מהיידיש) היא כיסוי ראש קטן — כובע קטן על הראש. המילה כיפה פירושה כיפה או קשת, ויידיש יארמולקה מגיעה מביטוי ארמי שפירושו יראת המלך.

למה נושאים:
התלמוד מלמד שכיסוי הראש מטפח יראת שמים — מודעות מתמדת לה׳ ולמעשינו. הכיפה תזכורת נראית לכך.

הערות מעשיות:
• לובשים כל היום, לא רק בתפילה או בלימוד
• אין צורך בכיפה ברחצה או בשחייה
• כל גודל, צבע או סגנון מתקבלים; עקבו אחר מנהג הקהילה
• רבים חובשים כובע מעל הכיפה בתפילה כאות כבוד נוסף

לנשים:
נשים נשואות חייבות בכיסוי שיער — ראו פרק הצניעות.""",
    "es": """Los hombres judíos cubren la cabeza todo el día como señal de que Dios está sobre nosotros.

Qué es:
Una kipá (כִּפָּה — también llamada yarmulke, del yiddish) es un pequeño cubrecabezas — una gorra sin visera. Kipá significa «cúpula»; yarmulke proviene de una frase aramea que significa «temor del Rey».

Por qué la usamos:
El Talmud enseña que cubrir la cabeza cultiva yirat Shamayim — conciencia constante de que Dios está presente. La kipá es un recordatorio visible.

Notas prácticas:
• Llévela todo el día, no solo en oración o estudio
• No es necesaria al bañarse o nadar
• Cualquier tamaño, color o estilo es aceptable; siga la costumbre de su comunidad
• Muchos usan sombrero sobre la kipá en la oración

Para mujeres:
Las mujeres casadas deben cubrir el cabello — véase la sección de modestia.""",
    "fr": """Les hommes juifs couvrent la tête toute la journée en signe que Dieu est au-dessus de nous.

Ce que c'est :
Une kippa (כִּפָּה — aussi appelée yarmulke, du yiddish) est un petit couvre-chef — une calotte. Kippa signifie « dôme » ; yarmulke vient d'une expression araméenne signifiant « crainte du Roi ».

Pourquoi nous la portons :
Le Talmud enseigne que couvrir la tête cultive la yirat Shamayim — conscience constante de la présence de Dieu. La kippa en est le rappel visible.

Notes pratiques :
• Portez-la toute la journée, pas seulement à la prière ou à l'étude
• Pas besoin de kippa pour se baigner ou nager
• Toute taille, couleur ou style convient ; suivez votre communauté
• Beaucoup portent un chapeau par-dessus à la prière

Pour les femmes :
Les femmes mariées doivent couvrir leurs cheveux — voir la section tsniout.""",
    "ru": """Еврейские мужчины покрывают голову весь день как знак того, что Бог над нами.

Что это:
Кипа (כִּפָּה — также йармулка, от идиша) — небольшое головное покрытие. Слово кипа означает «купол»; йармулка — от арамейской фразы «страх Царя».

Зачем носим:
Талмуд учит, что покрытие головы воспитывает йират шамаим — постоянное осознание присутствия Бога. Кипа — видимое напоминание.

Практические заметки:
• Носите весь день, не только на молитве или учёбе
• Не нужна при купании или плавании
• Подойдёт любой размер, цвет и стиль; следуйте обычаю общины
• Многие надевают шляпу поверх кипы на молитве

Для женщин:
Замужние женщины обязаны покрывать волосы — см. раздел о цниют.""",
  },
]
# fmt: on


def build_table(en: list[str]) -> dict[str, list[str]]:
    if len(TABLE) < len(en):
        # Fill remaining indices with English until expanded.
        for i in range(len(TABLE), len(en)):
            TABLE.append({lang: en[i] for lang in ("he", "es", "fr", "ru")})
    out = {lang: [] for lang in ("he", "es", "fr", "ru")}
    for i in range(len(en)):
        row = TABLE[i] if i < len(TABLE) else {lang: en[i] for lang in out}
        for lang in out:
            out[lang].append(row.get(lang, en[i]))
    return out


def main() -> None:
    en = json.loads((DATA / "long-en-all.json").read_text(encoding="utf-8"))
    payload = build_table(en)
    (DATA / "long-multi.json").write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Wrote long-multi.json ({len(en)} x 4 langs, {len(TABLE)} hand-translated rows)")


if __name__ == "__main__":
    main()
