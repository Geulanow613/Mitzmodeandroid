#!/usr/bin/env python3
"""Write batch_014.json shard — manual halacha-aware translations."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
batch = json.loads((ROOT / "batches" / "batch_014.json").read_text(encoding="utf-8"))
KEYS = batch["strings"]

HE = [
"ביטחון — ביטחון הוא אמון בה' — להאמין שהוא מספק את מה שאתה צריך ושקושי יכול להיות בעל תכלית גם כשאינך רואה זאת. קשור לאמונה אך מדגיש הסתמכות שקטה בדאגות יומיומיות. מוסר וחסידות מלמדים ביטחון מעשי.",
"א-להי נשמה — א-להי נשמה מודה בבוקר לה' על החזרת הנשמה, שהייתה טהורה ותוחזר במות. עוקב אחר ברכות השחר בכמה סידורים. התפילה מלמדת שכל יום הוא הלוואה חדשה של חיים — השתמשו בה למצוות, לא רק לסידורים.",
"כרפס הוא ירק (לעיתים פטרוזיליה או תפוח אדמה) שטובלים במי מלח בתחילת הסeder — לעורר סקרנות לפני הארוחה. מי המלח מזכירים דמעות השעבוד. זו לא מצוות המרור העיקרית; היא פותחת את הלילה בשאלה. הכינו מספיק לכל אורח.",
"אמרו חצי הallel אחר שחרית בראש חודש אם אתם אומרים הallel — מנהג יקר, לא חובה לנשים (פניני הלכה 05-01-12). נשים אשכנזיות רבות מדלגות על הallel; נשים ספרדיות רבות אומרות. עקבו אחר קהילתכם.\n\nתחנון נעדר בראש חודש.",
"עלינו — עלינו היא תפילת סיום המכריזה על מלכות ה' ועל תקוותנו להכרה עולמית במלכותו. מסיימת שחרית ומעריב יומי ומופיעה לקראת סוף תפילות ימים נוראים. הכריעה ב\"וַנִּכְרַע\" היא רגע של כניעה ויראה.",
"דיינו (\"די היה לנו\") הוא שיר הסeder המונה מתנות מיציאת מצרים — כל שלב לבדו היה מצדיק הכרת תודה. שירתו מאמנת ילדים ומבוגרים לשים לב לטוב הה' המצטבר. מופיע אחרי מגיד ולפני הallel.",
"סוכות הוא שבעה ימים של ישיבה בסוכה ונענוע ארba minim — שמחה אחרי ימים נוראים. מזכיר ענני הכבוד במדבר. גשם עלול לפטור מישיבה. שmini atzeret עוקב מיד — חג נפרד של קרבה לה' אחרי החג הציבורי.",
"השulchan aruch (\"שulchan aruch\") הוא קודекс ההalacha הקלאסי מהמאה ה-16 מאת רבי יוסף קארו. אשכנזים לומדים לעיתים עם הגlosses של הרמ\"א; ספרדים בדרך כלל הולכים אחר רבי קארו, אך רבנים חלקו כמעט על הכל לאורך השנים.",
]

# PLACEHOLDER_HE_REST

ES = [
"bitajón — Bitajón es confianza en Dios: creer que Él provee lo que necesitas y que la dificultad puede tener propósito aunque no lo veas. Se relaciona con emuná (fe) pero enfatiza la confianza serena en las preocupaciones diarias. El mussar y la jasidut enseñan bitajón práctico.",
"Elokei Neshama — Elokei Neshama agradece a Dios por la mañana por devolver el alma, que era pura y será devuelta en la muerte. Sigue a Birchot HaShajar en muchos sidurim. La oración enseña que cada día es un nuevo préstamo de vida: úsalo para mitzvot, no solo para recados.",
"Karpas es una verdura (a menudo perejil o papa) sumergida en agua con sal al inicio del Seder, despertando curiosidad antes de la comida. El agua salada recuerda las lágrimas de la esclavitud. No es la mitzvá principal de maror; abre la noche con una pregunta. Prepare suficiente para cada invitado.",
"Recite Medio Hallel después de Shajarit en Rosh Jodesh si dice Hallel — costumbre apreciada, no obligatoria para mujeres (Peninei Halakha 05-01-12). Muchas mujeres ashkenazíes omiten Hallel; muchas sefardíes lo recitan. Siga su comunidad.\n\nTachanun se omite en Rosh Jodesh.",
"Aleinu — Aleinu es una oración de cierre que declara la soberanía de Dios y nuestra esperanza de reconocimiento universal de Su reinado. Cierra Shajarit y Maariv diarios y aparece cerca del final de los servicios de Yamim Noraim. La inclinación en \"nos inclinamos\" es un momento de sumisión y reverencia.",
"Dayeinu (\"habría sido suficiente\") es la canción del Seder que enumera los dones del Éxodo; cada paso por sí solo habría justificado gratitud. Cantarla entrena a niños y adultos a notar la bondad acumulada de Dios. Aparece después del relato de Maguid y antes de Hallel.",
"Sukkot son siete días de morada en la suká y agitar las Cuatro Especies — alegría tras los Yamim Noraim. Recuerda las nubes de gloria en el desierto. La lluvia puede eximir de sentarse. Shemini Atzeret sigue de inmediato — fiesta aparte de intimidad con Dios tras la fiesta pública.",
"El Shulján Aruj («Mesa puesta») es el clásico código de halajá del siglo XVI del rabí Yosef Karo. Los ashkenazíes a menudo lo estudian con las glosas del Rema; los sefardíes siguen generalmente al rabí Karo, aunque los rabinos han debatido casi todo a lo largo de los años.",
]

# PLACEHOLDER_ES_REST

FR = [
"bitachon — Bitachon est la confiance en D. : croire qu'Il pourvoit à vos besoins et que l'épreuve peut avoir un sens même quand on ne le voit pas. Lié à l'emouna (foi), il met l'accent sur la confiance sereine au quotidien. Le moussar et la 'hassidout enseignent un bitachon pratique.",
"Elokei Neshama — Elokei Neshama remercie D. le matin de rendre l'âme, pure, qui lui sera rendue à la mort. Elle suit les Birkhot HaShachar dans beaucoup de siddourim. Cette prière enseigne que chaque jour est un nouvel emprunt de vie — à consacrer aux mitzvot, pas seulement aux courses.",
"Le karpas est un légume (souvent persil ou pomme de terre) trempé dans l'eau salée au début du Seder — éveillant la curiosité avant le repas. L'eau salée rappelle les larmes de l'esclavage. Ce n'est pas la mitzvah principale de maror ; elle ouvre la nuit par une question. Préparez-en assez pour chaque invité.",
"Récitez le Demi-Hallel après Shacharit à Roch 'Hodesh si vous dites Hallel — coutume chérie, non obligatoire pour les femmes (Peninei Halakha 05-01-12). Beaucoup de femmes ashkénazes omettent Hallel ; beaucoup de femmes séfarades le récitent. Suivez votre communauté.\n\nTachanoun est omis à Roch 'Hodesh.",
"Aleinu — Aleinu est une prière de clôture proclamant la souveraineté de D. et notre espérance d'une reconnaissance universelle de Son règne. Elle termine Shacharit et Arvit quotidiens et apparaît vers la fin des offices des Yamim Noraim. S'incliner à « nous fléchissons le genou » est un moment de soumission et d'émerveillement.",
"Dayeinu (« il nous aurait suffi ») est le chant du Seder listant les dons de l'Exode — chaque étape aurait à elle seule justifié la gratitude. Le chanter entraîne enfants et adultes à remarquer la bonté cumulative de D. Il apparaît après le Maguid et avant Hallel.",
"Soukkot, ce sont sept jours de séjour dans la soukka et d'agitation des Quatre Espèces — joie après les Yamim Noraim. Cela rappelle les nuées de gloire dans le désert. La pluie peut dispenser de s'y asseoir. Chemini Atseret suit immédiatement — fête distincte d'intimité avec D. après la fête publique.",
"Le Choulhan Arou'h (« Table dressée ») est le code classique de halakha du XVIe siècle du rabbin Yossef Karo. Les ashkénazes l'étudient souvent avec les gloses du Rema ; les séfarades suivent généralement le rabbin Karo, mais les rabbins ont débattu de presque tout au fil des ans.",
]

# PLACEHOLDER_FR_REST

RU = [
"bitachon — Битахон — доверие к Б-гу: вера в то, что Он даёт необходимое и что трудности могут иметь смысл, даже когда мы его не видим. Связан с emunah (верой), но подчёркивает спокойную опору в повседневных заботах. Мусар и хасидут учат практическому битахону.",
"Elokei Neshama — Elokei Neshama утром благодарит Б-га за возвращение души, которая была чиста и будет возвращена при смерти. Во многих сидурах следует за Birchot HaShachar. Молитва учит, что каждый день — новая «ссуда» жизни; используйте её для mitzvot, а не только для дел.",
"Karpas — овощ (часто петрушка или картофель), которым окунают в солёную воду в начале Seder, пробуждая любопытство до трапезы. Солёная вода напоминает слёзы рабства. Это не основная mitzvah maror; она открывает вечер вопросом. Приготовьте достаточно для каждого гостя.",
"Читайте половину Hallel после Shacharit в Rosh Chodesh, если вы читаетe Hallel — почитаемый обычай, не обязательный для женщин (Peninei Halakha 05-01-12). Многие ashkenazi женщины пропускают Hallel; многие sephardi женщины читают. Следуйте своей общине.\n\nTachanun не читается в Rosh Chodesh.",
"Aleinu — Aleinu — заключительная молитва о власти Б-га и надежде на всеобщее признание Его царства. Завершает ежедневные Shacharit и Maariv, звучит в конце служб Yamim Noraim. Поклон при «мы склоняем колено» — момент смирения и благоговения.",
"Dayeinu («и этого было бы достаточно») — песня Seder со списком даров Исхода; каждый шаг сам по себе оправдал бы благодарность. Пение учит детей и взрослых замечать накопленную доброту Б-га. Звучит после Maggid и перед Hallel.",
"Sukkot — семь дней жить в sukkah и махать Arba Minim — радость после Yamim Noraim. Напоминает облака славы в пустыне. Дождь может освобождать от сидения. Shemini Atzeret следует сразу — отдельный праздник близости с Б-гом после общественного праздника.",
"Shulchan Aruch («Накрытый стол») — классический кодекс halacha XVI века раввина Yosef Karo. Ashkenazim часто изучают его с комментариями Rema; sephardim обычно следуют раввину Karo, но раввины спорили почти обо всём на протяжении веков.",
]

# PLACEHOLDER_RU_REST

def main():
    for name, arr in [("HE", HE), ("ES", ES), ("FR", FR), ("RU", RU)]:
        if len(arr) != len(KEYS):
            raise SystemExit(f"{name}: expected {len(KEYS)}, got {len(arr)}")
    shard = {
        "he": dict(zip(KEYS, HE)),
        "es": dict(zip(KEYS, ES)),
        "fr": dict(zip(KEYS, FR)),
        "ru": dict(zip(KEYS, RU)),
    }
    out = ROOT / "shards" / "batch_014.json"
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text(json.dumps(shard, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Wrote {out.name}: {len(KEYS)} keys x 4 langs")

if __name__ == "__main__":
    main()
