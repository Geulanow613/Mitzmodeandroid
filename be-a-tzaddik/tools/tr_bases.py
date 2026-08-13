# Base translations for batches 014-019 (313 unique strings)
BASES: dict[str, dict[str, str]] = {}

def _b(k, he, es, fr, ru):
    BASES[k] = {"he": he, "es": es, "fr": fr, "ru": ru}

# --- batch 014 bases (partial; run build_all_shards to see missing) ---
_b("bitachon — Bitachon is trust in G-d — believing that He provides what you need and that hardship can have purpose even when you cannot see it. It is related to emunah (faith) but stresses calm reliance in daily worry. Mussar and Chasidut both teach practical bitachon.",
   "ביטחון — ביטחון הוא אמון בה' — להאמין שהוא מספק את מה שאתה צריך ושקושי יכול להיות בעל תכלית גם כשאינך רואה זאת. קשור לאמונה אך מדגיש הסתמכות שקטה בדאגות יומיומיות. מוסר וחסידות מלמדים ביטחון מעשי.",
   "bitajón — Bitajón es confianza en Dios: creer que Él provee lo que necesitas y que la dificultad puede tener propósito aunque no lo veas. Se relaciona con emuná (fe) pero enfatiza la confianza serena en las preocupaciones diarias. El mussar y la jasidut enseñan bitajón práctico.",
   "bitachon — Bitachon est la confiance en D. : croire qu'Il pourvoit à vos besoins et que l'épreuve peut avoir un sens même quand on ne le voit pas. Lié à l'emouna (foi), il met l'accent sur la confiance sereine au quotidien. Le moussar et la 'hassidout enseignent un bitachon pratique.",
   "bitachon — Битахон — доверие к Б-гу: вера в то, что Он даёт необходимое и что трудности могут иметь смысл, даже когда мы его не видим. Связан с emunah (верой), но подчёркивает спокойную опору в повседневных заботах. Мусар и хасидут учат практическому битахону.")

_b("Elokei Neshama — Elokei Neshama thanks G-d in the morning for restoring the soul, which was pure and will be returned at death. It follows Birchot HaShachar in many siddurim. The prayer teaches that each day is a new loan of life — use it for mitzvot, not only errands.",
   "א-להי נשמה — א-להי נשמה מודה בבוקר לה' על החזרת הנשמה, שהייתה טהורה ותוחזר במות. עוקב אחר ברכות השחר בכמה סידורים. התפילה מלמדת שכל יום הוא הלוואה חדשה של חיים — השתמשו בה למצוות, לא רק לסידורים.",
   "Elokei Neshama — Elokei Neshama agradece a Dios por la mañana por devolver el alma, que era pura y será devuelta en la muerte. Sigue a Birchot HaShajar en muchos sidurim. La oración enseña que cada día es un nuevo préstamo de vida: úsalo para mitzvot, no solo para recados.",
   "Elokei Neshama — Elokei Neshama remercie D. le matin de rendre l'âme, pure, qui lui sera rendue à la mort. Elle suit les Birkhot HaShachar dans beaucoup de siddourim. Cette prière enseigne que chaque jour est un nouvel emprunt de vie — à consacrer aux mitzvot, pas seulement aux courses.",
   "Elokei Neshama — Elokei Neshama утром благодарит Б-га за возвращение души, которая была чиста и будет возвращена при смерти. Во многих сидурах следует за Birchot HaShachar. Молитва учит, что каждый день — новая «ссуда» жизни; используйте её для mitzvot, а не только для дел.")

# __MORE_BASES__
