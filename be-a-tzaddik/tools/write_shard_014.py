#!/usr/bin/env python3
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
batch = json.loads((ROOT / "batches" / "batch_014.json").read_text(encoding="utf-8"))
LANGS = ("he", "es", "fr", "ru")
D = {}

def a(k, he, es, fr, ru):
    D[k] = {"he": he, "es": es, "fr": fr, "ru": ru}

# 0-19
a("bitachon — Bitachon is trust in G-d — believing that He provides what you need and that hardship can have purpose even when you cannot see it. It is related to emunah (faith) but stresses calm reliance in daily worry. Mussar and Chasidut both teach practical bitachon.",
  "ביטחון — ביטחון הוא אמון בה' — להאמין שהוא מספק את מה שאתה צריך ושקושי יכול להיות בעל תכלית גם כשאינך רואה זאת. קשור לאמונה אך מדגיש הסתמכות שקטה בדאגות יומיומיות. מוסר וחסידות מלמדים ביטחון מעשי.",
  "bitajón — Bitajón es confianza en Dios: creer que Él provee lo que necesitas y que la dificultad puede tener propósito aunque no lo veas. Se relaciona con emuná (fe) pero enfatiza la confianza serena en las preocupaciones diarias. El mussar y la jasidut enseñan bitajón práctico.",
  "bitachon — Bitachon est la confiance en D. : croire qu'Il pourvoit à vos besoins et que l'épreuve peut avoir un sens même quand on ne le voit pas. Lié à l'emouna (foi), il met l'accent sur la confiance sereine au quotidien. Le moussar et la 'hassidout enseignent un bitachon pratique.",
  "bitachon — Битахон — доверие к Б-гу: вера в то, что Он даёт необходимое и что трудности могут иметь смысл, даже когда мы его не видим. Связан с emunah (верой), но подчёркивает спокойную опору в повседневных заботах. Мусар и хасидут учат практическому битахону.")

a("Elokei Neshama — Elokei Neshama thanks G-d in the morning for restoring the soul, which was pure and will be returned at death. It follows Birchot HaShachar in many siddurim. The prayer teaches that each day is a new loan of life — use it for mitzvot, not only errands.",
  "א-להי נשמה — א-להי נשמה מודה בבוקר לה' על החזרת הנשמה, שהייתה טהורה ותוחזר במות. עוקב אחר ברכות השחר בכמה סידורים. התפילה מלמדת שכל יום הוא הלוואה חדשה של חיים — השתמשו בה למצוות, לא רק לסידורים.",
  "Elokei Neshama — Elokei Neshama agradece a Dios por la mañana por devolver el alma, que era pura y será devuelta en la muerte. Sigue a Birchot HaShajar en muchos sidurim. La oración enseña que cada día es un nuevo préstamo de vida: úsalo para mitzvot, no solo para recados.",
  "Elokei Neshama — Elokei Neshama remercie D. le matin de rendre l'âme, pure, qui lui sera rendue à la mort. Elle suit les Birkhot HaShachar dans beaucoup de siddourim. Cette prière enseigne que chaque jour est un nouvel emprunt de vie — à consacrer aux mitzvot, pas seulement aux courses.",
  "Elokei Neshama — Elokei Neshama утром благодарит Б-га за возвращение души, которая была чиста и будет возвращена при смерти. Во многих сидурах следует за Birchot HaShachar. Молитва учит, что каждый день — новая «ссуда» жизни; используйте её для mitzvot, а не только для дел.")

a("Karpas is a vegetable (often parsley or potato) dipped in salt water at the Seder start — awakening curiosity before the meal. The salt water recalls tears of slavery. It is not the main maror mitzvah; it opens the night with a question. Prepare enough for every guest.",
  "כרפס הוא ירק (לעיתים פטרוזיליה או תפוח אדמה) שטובלים במי מלח בתחילת הסeder — לעורר סקרנות לפני הארוחה. מי המלח מזכירים דמעות השעבוד. זו לא מצוות המרור העיקרית; היא פותחת את הלילה בשאלה. הכינו מספיק לכל אורח.",
  "Karpas es una verdura (a menudo perejil o papa) sumergida en agua con sal al inicio del Seder, despertando curiosidad antes de la comida. El agua salada recuerda las lágrimas de la esclavitud. No es la mitzvá principal de maror; abre la noche con una pregunta. Prepare suficiente para cada invitado.",
  "Le karpas est un légume (souvent persil ou pomme de terre) trempé dans l'eau salée au début du Seder — éveillant la curiosité avant le repas. L'eau salée rappelle les larmes de l'esclavage. Ce n'est pas la mitzvah principale de maror ; elle ouvre la nuit par une question. Préparez-en assez pour chaque invité.",
  "Karpas — овощ (часто петрушка или картофель), которым окунают в солёную воду в начале Seder, пробуждая любопытство до трапезы. Солёная вода напоминает слёзы рабства. Это не основная mitzvah maror; она открывает вечер вопросом. Приготовьте достаточно для каждого гостя.")

# PLACEHOLDER_014_REST

def main():
    missing = [s for s in batch["strings"] if s not in D]
    if missing:
        raise SystemExit(f"Missing {len(missing)}: {missing[0][:80]!r}")
    shard = {lang: {s: D[s][lang] for s in batch["strings"]} for lang in LANGS}
    out = ROOT / "shards" / "batch_014.json"
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text(json.dumps(shard, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Wrote {out.name}: {len(shard['he'])} keys x 4 langs")

if __name__ == "__main__":
    main()
