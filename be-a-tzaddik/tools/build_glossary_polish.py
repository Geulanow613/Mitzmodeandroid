#!/usr/bin/env python3
"""Build human glossary_polish.json — overrides bad quality_overrides es/fr/ru/he entries."""

from __future__ import annotations

import json
import re
from pathlib import Path

from apply_quality_fixes import (
    HAND_FIXES,
    apply_subs,
    repair_placeholders,
    restore_kotlin_templates,
)
from compile_full_bundled import load_human, load_legacy, load_shards, repair_translation as compile_repair

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
COMPOSE = ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "translations"
OUT = ROOT / "data" / "translation-catalog" / "human" / "glossary_polish.json"
LANGS = ("he", "es", "fr", "ru")

# Extra subs applied after apply_quality_fixes SUBS (glossary / prayer disasters)
EXTRA_SUBS: dict[str, list[tuple[str, str]]] = {
    "es": [
        (r"\bD-s\b", "Dios"),
        (r"\bD-os\b", "Dios"),
        (r"\bOn Shabbat morning\b", "En la mañana de Shabat"),
        (r"\bOn Shabbat mañana\b", "En la mañana de Shabat"),
        (r"\bon Shabbat—", "en Shabat —"),
        (r"\bbishul on Shabbat\b", "bishul en Shabat"),
        (r"\bencendido Rosh Hashana\b", "soplado en Rosh Hashaná"),
        (r"\babierto Shacharit\b", "abren Shajarit"),
        (r"\bOn Pesach, hamotzi\b", "En Pesaj, hamotzi"),
        (r"\bon Shabbat\b", "en Shabat"),
        (r"\bOn Pesach\b", "En Pesaj"),
        (r"\bon Pesach\b", "en Pesaj"),
        (r"\bOn Yom Tov\b", "En Yom Tov"),
        (r"\bdozing\b", "adormilarse"),
        (r"\bpicazón\b", "Birkat Hamazón"),
        (r"\bdoblar Shabbat\b", "del bentching en Shabat"),
        (r"\bde doblar\b", "del bentching"),
        (r"\brotura de repetición\b", "repetir el bentching"),
        (r"\bGracias a D-s para\b", "Agradece a Dios por"),
        (r"\bGracias\.D-s\b", "Agradece a Dios"),
        (r"\bPsalm126\b", "Salmo 126"),
        (r"\bPsalm 126\.\b", "Salmo 126"),
        (r"\bGrace After Meals\b", "Birkat Hamazón"),
        (r"\bGrace After\b", "Birkat Hamazón"),
        (r"\bcourbures\b", "bentching"),
        (r"\bmaquillaje\b", "tintura con pigmento"),
        (r"\bque requiere la picazón\b", "que requiere bentching"),
        (r"\bque requiere des curbures\b", "qui exige le bentching"),
        (r"\bHigh Holiday\b", "Yamim Noraim"),
        (r"\bsemi-holiday\b", "semi-festividad"),
        (r"\bThank G-d\b", "Agradezca a Dios"),
        (r"\bthank G-d\b", "agradezca a Dios"),
        (r"\babout G-d\b", "sobre Dios"),
        (r"\bAbout G-d\b", "Sobre Dios"),
        (r"\bG-d's\b", "de Dios"),
        (r"\bG-d\b", "Dios"),
        (r"\bbless G-d\b", "bendecir a Dios"),
        (r"\bBless G-d\b", "Bendecir a Dios"),
        (r"\bpraising G-d\b", "alabando a Dios"),
        (r"\bdeclaring G-d's sovereignty\b", "declarando la soberanía de Dios"),
        (r"\bsovereignty and our hope for G-d's\b", "soberanía y nuestra esperanza en el reinado de Dios"),
    ],
    "fr": [
        (r"\bThank G-d\b", "Remerciez D."),
        (r"\bthank G-d\b", "remerciez D."),
        (r"\babout G-d\b", "à propos de D."),
        (r"\bG-d's\b", "de D."),
        (r"\bG-d\b", "D."),
        (r"\bbless G-d\b", "bénir D."),
        (r"\bBless G-d\b", "Bénir D."),
        (r"\bmaquillage\b", "teinture au pigment"),
        (r"\bet courbures\b", "et bentching"),
        (r"\ben Amidah et courbures\b", "en Amidah et bentching"),
        (r"\bcourbures\b", "bentching"),
        (r"\bcourbure\b", "bentching"),
        (r"\bcourbure\b", "bentching"),
        (r"\btourbillon\b", "somnolence"),
        (r"\bpendant le tourbillon\b", "en somnolence"),
        (r"\bGrace After Meals\b", "Birkat Hamazón"),
        (r"\bGrace After\b", "Birkat Hamazón"),
        (r"\bJe vous remercie\.D\.\b", "On remercie D."),
        (r"\bPsalm126\b", "Psaume 126"),
        (r"\bHigh Holiday\b", "Yamim Noraïm"),
        (r"\bsemi-holiday\b", "semi-fête"),
        (r"\bThank G-d\b", "Remerciez D."),
        (r"\bthank G-d\b", "remerciez D."),
    ],
    "ru": [
        (r"\bизгиба\b", "бентчинга"),
        (r"\bсогнутия\b", "бентчинга"),
        (r"\bпокорности\b", "бентчинга"),
        (r"\bсогнут\b", "бентчинг"),
        (r"\bперед согнутием\b", "перед бентчингом"),
        (r"умылись\.Б-г", "умылись. Б-г"),
        (r"до того, как умылись\.Б-г", "до Б-га"),
        (r"\bGrace After Meals —\b", "Биркат а-Мазон —"),
        (r"\bGrace After Meals\b", "Биркат а-Мазон"),
        (r"\bGrace After\b", "Биркат а-Мазон"),
        (r"\bГрейс после еды\b", "Биркат а-Мазон"),
        (r"\bmaquillaje\b", "пигментная краска"),
        (r"\bmaquillage\b", "пигментная краска"),
        (r"\bHigh Holiday\b", "Ямим Нораим"),
        (r"\bsemi-holiday\b", "полупраздник"),
        (r"\bThank G-d\b", "Благодарите Б-га"),
        (r"\bthank G-d\b", "благодарите Б-га"),
        (r"\bG-d\b", "Б-г"),
    ],
    "he": [
        (r"\bעל D-s\b", "על ה'"),
        (r"\bD-s\b", "ה'"),
        (r"\bלפני D-s\b", "לפני ה'"),
        (r"\bלשים את D-s\b", "לשים את ה'"),
        (r"\bלפני D-s בחיינו\b", "לפני ה' בחיינו"),
        (r"Hilchot Avoda", "הלכות עבודה זרה"),
        (r"Lechem Wronghneh", "לחם משנה"),
        (r"הדבורה", "הבנטשינג"),
        (r"גירוד חוזר", "חזרה על ברכת המזון"),
        (r"הבובות", "הכיכרות"),
        (r"האוליצי", "המוציא"),
        (r"לאוצי", "להמוציא"),
        (r"בירקכט האמפרי", "בירכת המפיל"),
        (r"שצפה מותרת", "הנמנום מותר"),
        (r"פושקים", "פוסקים"),
    ],
}

# Authoritative hand-polished glossary / prayer strings (win over pattern fixes)
HAND_POLISH: dict[str, dict[str, str]] = {
    "Bentching is Birkat Hamazon — Grace After Meals after eating a kezayit of bread within a meal. It thanks G-d for food and the Land. On Shabbat and festivals, Psalm 126 (Shir HaMaalot) precedes the second blessing. Yaaleh V'yavo is added on Rosh Chodesh and chag. Zimun invites others when three or more men ate bread together per minhag.": {
        "he": "ברכת המזון — אחרי כזית לחם בסעודה; מודים לה' על המזון והארץ. בשבת וחג — תהילים קכ\"ו לפני הברכה השנייה. יעלה ויבוא בר\"ח וחג. זימון כששלושה גברים אכלו יחד לפי מנהג.",
        "es": "Bentching es Birkat Hamazón — la bendición después de las comidas, tras comer un kezayit de pan en una comida. Se agradece a Dios por el alimento y por la Tierra. En Shabat y festividades, el Salmo 126 (Shir HaMaalot) precede a la segunda bendición. Se añade Yaaleh V'yavo en Rosh Jodesh y en los días de chag. El zimun invita a otros cuando tres o más hombres comieron pan juntos según el minhag.",
        "fr": "Le bentching est le Birkat Hamazón — la Grâce après les repas, après avoir mangé un kezayit de pain au cours d'un repas. On remercie D. pour la nourriture et pour la Terre. Le Chabbat et les fêtes, le Psaume 126 (Shir HaMaalot) précède la deuxième bénédiction. Yaaleh V'yavo s'ajoute à Roch Hodech et aux jours de chag. Le zimoun invite les autres lorsque trois hommes ou plus ont mangé du pain ensemble selon le minhag.",
        "ru": "Бентчинг — это Биркат а-Мазон, благословение после трапезы с хлебом, когда съели хотя бы kezayit хлеба за одной трапезой. Благодарим Б-га за пищу и за Землю. В Шаббат и праздники Псалом 126 (Шир ha-Маалот) предшествует второму благословению. Yaaleh V'yavo добавляют в Рош Ходеш и в дни chag. Зимун приглашает других, когда трое или более мужчин ели хлеб вместе по минhагу.",
    },
    "Bentching — Bentching is Birkat Hamazon — Grace After Meals after eating a kezayit of bread within a meal. It thanks G-d for food and the Land. On Shabbat and festivals, Psalm 126 (Shir HaMaalot) precedes the second blessing. Yaaleh V'yavo is added on Rosh Chodesh and chag. Zimun invites others when three or more men ate bread together per minhag.": {
        "he": "ברכת המזון — אחרי כזית לחם; יעלה ויבוא בר\"ח וחג; זימון לפי מנהג.",
        "es": "Bentching — Birkat Hamazón tras un kezayit de pan; en Shabat y festividades el Salmo 126; Yaaleh V'yavo en Rosh Jodesh y chag; zimun según minhag.",
        "fr": "Bentching — Birkat Hamazón après un kezayit de pain ; le Chabbat et les fêtes, Psaume 126 ; Yaaleh V'yavo à Roch Hodech et chag ; zimoun selon minhag.",
        "ru": "Бентчинг — Биркат а-Мазон после kezayit хлеба; в Шаббат и праздники — Псалом 126; Yaaleh V'yavo в Рош Ходеш и chag; зимун по минhагу.",
    },
    "Hamotzi is the blessing over bread — \"Who brings forth bread from the earth\" — beginning a meal that requires bentching. Wash netilat yadayim before bread meals. Cut bread for others after you bless if you are host. On Pesach, hamotzi is on matzah; on Shabbat, lechem mishneh — two whole loaves.": {
        "he": "המוציא היא הברכה על הלחם — «המוציא לחם מן הארץ» — פותחת סעודה הדורשת ברכת המזון. נטילת ידיים לפני ארוחות לחם. חותכים לחם לאחרים אחרי הברכה אם אתם מארחים. בפסח — על מצה; בשבת — לחם משנה, שתי כיכרות שלמות.",
        "es": "Hamotzi es la bendición sobre el pan — «Que saca el pan de la tierra» — que inicia una comida que requiere bentching. Lávese las manos (netilat yadayim) antes de las comidas con pan. El anfitrión corta el pan para los demás después de bendecir. En Pesaj, el hamotzi es sobre matzá; en Shabat, lejem mishné — dos hogazas enteras.",
        "fr": "Hamotzi est la bénédiction sur le pain — « Qui fait sortir le pain de la terre » — qui commence un repas exigeant le bentching. Lavez-vous les mains (netilat yadayim) avant les repas avec pain. L'hôte coupe le pain pour les autres après la bénédiction. À Pessa'h, hamotzi est sur la matsa ; le Chabbat, lehem mishné — deux pains entiers.",
        "ru": "Хамоци — благословение на хлеб «дающий хлеб из земли», с которого начинается трапеза, требующая бентчинга. Перед хлебной трапезой моют руки (нетилат ядаим). Хозяин режет хлеб для других после благословения. На Песах хамоци на маце; в Шаббат — лехем мишне, две целые буханки.",
    },
    "Hamotzi — Hamotzi is the blessing over bread — \"Who brings forth bread from the earth\" — beginning a meal that requires bentching. Wash netilat yadayim before bread meals. Cut bread for others after you bless if you are host. On Pesach, hamotzi is on matzah; on Shabbat, lechem mishneh — two whole loaves.": {
        "he": "המוציא — ברכה על לחם; נטילת ידיים; בפסח מצה, בשבת לחם משנה.",
        "es": "Hamotzi — bendición sobre el pan; netilat yadayim; en Pesaj matzá, en Shabat lejem mishné.",
        "fr": "Hamotzi — bénédiction sur le pain ; netilat yadayim ; à Pessa'h matsa, le Chabbat lehem mishné.",
        "ru": "Хамоци — благословение на хлеб; нетилат ядаим; на Песах маца, в Шаббат лехем мишне.",
    },
    "Birchat Hamapil (Hamapil) is the blessing before sleep entrusting the soul to G-d. Say it as the last words before closing your eyes. After reciting it, do not eat, drink, or speak until sleep; quietly repeating familiar Shema or protection verses while dozing is permitted, but classical poskim discourage sitting up for new Torah study or lengthy Tehillim (Biur Halachah O.C. 239:1). Some omit it if they know they may not fall asleep for a long time. It pairs with Shema al hamitah for protection.": {
        "he": "בירכת המפיל היא הברכה לפני השינה, מסורת הנפש לה'. אומרים אותה כמילים האחרונות לפני סגירת העיניים. אחרי אמירתה אין לאכול, לשתות או לדבר עד השינה; חזרה שקטה על פסוקי שמע או הגנה בזמן נמנום מותרת, אך פוסקים קלאסיים ממליצים שלא לקום ללימוד תורה חדש או לתהילים ארוכים (ביאור הלכה או\"ח רלט:א). חלק נמנעים אם יודעים שלא יירדמו זמן רב. משלימה את שמע על המיטה להגנה.",
        "es": "Birchat Hamapil (Hamapil) es la bendición antes de dormir, encomendando el alma a Dios. Dígala como últimas palabras antes de cerrar los ojos. Tras recitarla, no coma, beba ni hable hasta dormirse; repetir en voz baja versos conocidos del Shemá o de protección mientras se adormila está permitido, pero los poskim clásicos desaconsejan levantarse para nuevo estudio de Torá o Tehilim largos (Biur Halajá O.J. 239:1). Algunos la omiten si saben que no dormirán pronto. Se complementa con Shemá al hamitá.",
        "fr": "Birchat Hamapil (Hamapil) est la bénédiction avant le sommeil, confiant l'âme à D. Dites-la comme dernières paroles avant de fermer les yeux. Après l'avoir récitée, ne mangez pas, ne buvez pas et ne parlez pas jusqu'au sommeil ; répéter doucement des versets familiers du Shema ou de protection en somnolence est permis, mais les poskim classiques déconseillent de se lever pour un nouvel étude de Torah ou un long Tehilim (Biur Halakha O.H. 239:1). Certains l'omettent s'ils savent qu'ils ne s'endormiront pas longtemps. Elle accompagne le Shema al hamitah.",
        "ru": "Бирхат а-Мапил (Хамапил) — благословение перед сном, вверяющее душу Б-гу. Произносите его последними словами перед закрытием глаз. После него не ешьте, не пейте и не разговаривайте до сна; тихое повторение знакомых стихов Шемы или защитных стихов во время дремоты разрешено, но классические поскими не рекомендуют садиться для нового изучения Торы или длинного Техиллима (Биур алаха О.Х. 239:1). Некоторые пропускают, если знают, что долго не заснут. Сочетается с Шема аль амита.",
    },
    "birchat hamapil — Birchat Hamapil (Hamapil) is the blessing before sleep entrusting the soul to G-d. Say it as the last words before closing your eyes. After reciting it, do not eat, drink, or speak until sleep; quietly repeating familiar Shema or protection verses while dozing is permitted, but classical poskim discourage sitting up for new Torah study or lengthy Tehillim (Biur Halachah O.C. 239:1). Some omit it if they know they may not fall asleep for a long time. It pairs with Shema al hamitah for protection.": {
        "he": "בירכת המפיל — ברכה לפני שינה; אחריה אין אכילה, שתייה או דיבור עד השינה.",
        "es": "birchat hamapil — bendición antes de dormir; tras ella no comer, beber ni hablar hasta dormirse.",
        "fr": "birchat hamapil — bénédiction avant le sommeil ; ensuite pas de nourriture, boisson ni parole jusqu'au sommeil.",
        "ru": "бирхат а-мапил — благословение перед сном; после него не есть, не пить и не говорить до сна.",
    },
    "Retzei asks G-d to be pleased with our rest and to restore the Temple and Davidic kingdom — inserted in the third blessing of bentching on Shabbat and Yom Tov. Forgetting Retzei at the first two Shabbat meals usually requires repeating bentching. However, if you forget it at Seudah Shlishit (the third meal), you do not repeat (SA OC 188:8). Ask your rav if unsure.": {
        "he": "רצה מבקש מה' לרצות במנוחתנו ולשחזר את המקדש ומלכות בית דוד — מוכנס בברכה השלישית של ברכת המזון בשבת ויום טוב. שכחת רצה בשתי הסעודות הראשונות של שבת בדרך כלל מחייבת חזרה על ברכת המזון. אם שכחו בסעודה שלישית — אין חוזרים (שו\"ע או\"ח קפח:ח). שאלו את הרב אם לא בטוחים.",
        "es": "Retzei pide a Dios que se complazca en nuestro descanso y restaure el Templo y el reino davídico — se inserta en la tercera bendición del bentching en Shabat y Yom Tov. Olvidar Retzei en las dos primeras comidas de Shabat generalmente exige repetir el bentching. En Seudá Shlishit (la tercera comida) no se repite (S.A. O.J. 188:8). Consulte a su rav si tiene dudas.",
        "fr": "Retzei demande à D. de se complaire dans notre repos et de restaurer le Temple et le royaume davidique — inséré dans la troisième bénédiction du bentching le Chabbat et Yom Tov. Oublier Retzei aux deux premiers repas de Chabbat exige en général de répéter le bentching. À Seouda Shlishit (le troisième repas), on ne répète pas (Choulhan Aroukh O.H. 188:8). Demandez à votre rav en cas de doute.",
        "ru": "Рецей просит Б-га благоволить к нашему покою и восстановить Храм и царство Давида — вставляется в третье благословение бентчинга в Шаббат и Йом Тов. Забыв Рецей на двух первых трапезах Шаббата, обычно нужно повторить бентчинг. На сеуде шлишит (третья трапеза) не повторяют (Шулхан Арух О.Х. 188:8). Спросите раввина при сомнении.",
    },
    "Retzei — Retzei asks G-d to be pleased with our rest and to restore the Temple and Davidic kingdom — inserted in the third blessing of bentching on Shabbat and Yom Tov. Forgetting Retzei at the first two Shabbat meals usually requires repeating bentching. However, if you forget it at Seudah Shlishit (the third meal), you do not repeat (SA OC 188:8). Ask your rav if unsure.": {
        "he": "רצה — בברכה השלישית של ברכת המזון בשבת ויום טוב; שכחה בשתי הסעודות הראשונות — חוזרים; בסעודה שלישית — לא.",
        "es": "Retzei — tercera bendición del bentching en Shabat y Yom Tov; olvido en las dos primeras comidas — repetir; en la tercera — no.",
        "fr": "Retzei — troisième bénédiction du bentching le Chabbat et Yom Tov ; oubli aux deux premiers repas — répéter ; au troisième — non.",
        "ru": "Рецей — третье благословение бентчинга в Шаббат и Йом Тов; забыли на двух первых трапезах — повторить; на третьей — нет.",
    },
    "Lechem mishneh is two whole loaves at Shabbat and Yom Tov meals — remembering double portion of manna before Shabbat in the desert. Cover the loaves, bless hamotzi, break bread for others. On Pesach, two whole matzot serve the role. The loaves should be whole and worthy of hamotzi.": {
        "he": "לחם משנה — שתי כיכרות שלמות בסעודות שבת ויום טוב, לזכור מנה כפולה של המן לפני שבת במדבר. מכסים את הכיכרות, מברכים המוציא וחותכים לאחרים. בפסח — שתי מצות שלמות ממלאות את התפקיד. הכיכרות צריכות להיות שלמות וראויות להמוציא.",
        "es": "Lejem mishné son dos hogazas enteras en las comidas de Shabat y Yom Tov, en recuerdo de la doble porción de maná antes del Shabat en el desierto. Cubra las hogazas, bendiga hamotzi y parta para los demás. En Pesaj, dos matzot enteras cumplen este papel. Deben estar enteras y ser aptas para hamotzi.",
        "fr": "Lehem mishné, ce sont deux pains entiers aux repas de Chabbat et Yom Tov, en souvenir de la double portion de manne avant Chabbat dans le désert. Couvrez les pains, bénissez hamotzi et partagez. À Pessa'h, deux matsot entières remplissent ce rôle. Les pains doivent être entiers et dignes de hamotzi.",
        "ru": "Лехем мишне — две целые буханки на трапезах Шаббата и Йом Това в память о двойной порции манны перед Шаббатом в пустыне. Накрывают хлеб, благословляют хамоци и делят для других. На Песах роль выполняют две целые мацы. Буханки должны быть целыми и подходящими для хамоци.",
    },
    "lechem mishneh — Lechem mishneh is two whole loaves at Shabbat and Yom Tov meals — remembering double portion of manna before Shabbat in the desert. Cover the loaves, bless hamotzi, break bread for others. On Pesach, two whole matzot serve the role. The loaves should be whole and worthy of hamotzi.": {
        "he": "לחם משנה — שתי כיכרות שלמות בשבת ויום טוב; בפסח שתי מצות.",
        "es": "lechem mishneh — dos hogazas enteras en Shabat y Yom Tov; en Pesaj dos matzot.",
        "fr": "lechem mishné — deux pains entiers le Chabbat et Yom Tov ; à Pessa'h deux matsot.",
        "ru": "лехем мишне — две целые буханки в Шаббат и Йом Тов; на Песах две мацы.",
    },
    "Permanently coloring or enhancing the appearance of a material using pigment. This affects cosmetics: using makeup that stains the skin is a concern on Shabbat. The deeper principle is not permanently altering the appearance of your surroundings to match your desires on the day of rest.": {
        "es": "Teñir permanentemente o realzar la apariencia de un material con pigmento. La melaajá de tzoveya prohíbe teñir telas y materiales de forma duradera. Por extensión, los cosméticos que dejan color permanente en la piel también son motivo de preocupación en Shabat. El principio profundo es no alterar de forma duradera la apariencia del entorno según nuestros deseos en el día de descanso.",
        "fr": "Colorer de façon permanente ou améliorer l'apparence d'un matériau à l'aide de pigments. La mélacha de tzoveya interdit la teinture durable des tissus et matériaux. Par extension, les produits cosmétiques qui laissent une coloration permanente sur la peau posent aussi problème le Chabbat. Le principe profond est de ne pas altérer durablement l'apparence de l'environnement selon nos désirs le jour du repos.",
        "ru": "Постоянное окрашивание или улучшение внешнего вида материала с помощью пигмента. Мелаха цовея запрещает устойчиво красить ткани и материалы. По расширению, косметические средства, оставляющие постоянный след на коже, тоже вызывают опасения в Шаббат. Глубинный принцип — не изменять надолго облик окружающего мира по своему желанию в день отдыха.",
    },
    "Grace After Meals — Birkat Hamazon after eating bread meals": {
        "ru": "Биркат а-Мазон — благословение после трапезы с хлебом",
    },
    "Chametz:\n• Biur was Friday morning (13 Nisan). On Shabbat morning, finish eating chametz by the end of the 4th halachic hour and recite the final Kol Chamira before the end of the 5th halachic hour — do not burn on Shabbat. Bedikat was Thursday night; mechirat chametz should already be authorized.": {
        "es": "Chametz:\n• El biur fue el viernes por la mañana (13 Nisan). En la mañana de Shabat, termine de comer jametz antes del final de la cuarta hora halájica y recite el Kol Chamira final antes del final de la quinta hora halájica — no queme en Shabat. El bedikat fue el jueves por la noche; la mejirat jametz ya debería estar autorizada.",
    },
    "Purim Meshulash is the rare Jerusalem schedule when Shushan Purim (15 Adar) falls on Shabbat. Megillah and matanot l'evyonim move to Friday; mishloach manot and the seudah move to Sunday. Shabbat carries communal Purim obligations — special Torah reading (Vayavo Amalek), Haftarah, and Al HaNissim in Amidah and bentching (not on Friday or Sunday).": {
        "fr": "Pourim Mechoulach est le calendrier rare de Jérusalem lorsque Pourim de Shoushan (15 Adar) tombe un Chabbat. La Meguila et matanot l'evyonim passent au vendredi ; mishloach manot et la seouda passent au dimanche. Chabbat porte les obligations communautaires de Pourim — lecture spéciale de la Torah (Vayavo Amalek), Haftarah, et Al HaNissim dans l'Amidah et le bentching (pas le vendredi ni le dimanche).",
    },
    "Purim Meshulash — Purim Meshulash is the rare Jerusalem schedule when Shushan Purim (15 Adar) falls on Shabbat. Megillah and matanot l'evyonim move to Friday; mishloach manot and the seudah move to Sunday. Shabbat carries communal Purim obligations — special Torah reading (Vayavo Amalek), Haftarah, and Al HaNissim in Amidah and bentching (not on Friday or Sunday).": {
        "fr": "Pourim Mechoulach — calendrier rare de Jérusalem quand Pourim de Shoushan tombe un Chabbat ; Meguila et matanot l'evyonim le vendredi ; mishloach manot et seouda le dimanche ; Al HaNissim dans l'Amidah et le bentching le Chabbat.",
    },
}


def apply_extra_subs(lang: str, text: str) -> str:
    for pattern, repl in EXTRA_SUBS.get(lang, []):
        text = re.sub(pattern, repl, text, flags=re.IGNORECASE)
    return compile_repair(lang, text) if lang in ("es", "fr", "ru") else text


def load_base_entries(required: list[str]) -> dict[str, dict[str, str]]:
    """Compiled bundles without glossary_polish.json (the layer we are replacing)."""
    shards = load_shards()
    legacy = load_legacy()
    human = load_human()
    # Remove glossary_polish overrides from human merge
    gp = ROOT / "data" / "translation-catalog" / "human" / "glossary_polish.json"
    if gp.exists():
        gp_data = json.loads(gp.read_text(encoding="utf-8"))
        for lang in LANGS:
            for k in gp_data.get(lang, {}):
                human[lang].pop(k, None)
    entries: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    for lang in LANGS:
        merged: dict[str, str] = {}
        merged.update(legacy.get(lang, {}))
        merged.update(shards.get(lang, {}))
        merged.update(human.get(lang, {}))
        if lang in ("es", "fr", "ru"):
            merged = {k: compile_repair(lang, v) for k, v in merged.items()}
        entries[lang] = {s: merged.get(s, s) for s in required}
    return entries


def polish_text(lang: str, en: str, tr: str) -> str:
    if en in HAND_POLISH and lang in HAND_POLISH[en]:
        return HAND_POLISH[en][lang]
    if en in HAND_FIXES and lang in HAND_FIXES[en]:
        return HAND_FIXES[en][lang]
    out = apply_subs(lang, tr)
    out = apply_extra_subs(lang, out)
    out = restore_kotlin_templates(en, out)
    out = repair_placeholders(en, out)
    return out


def load_compiled() -> dict[str, dict[str, str]]:
    required = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    return load_base_entries(required)


def main() -> None:
    required = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    compiled = load_compiled()
    keys_path = ROOT / "data" / "translation-catalog" / "_glossary_polish_keys.json"
    target_keys: set[str] = set(required)
    if keys_path.exists():
        target_keys |= set(json.loads(keys_path.read_text(encoding="utf-8")))

    out: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}

    for en in required:
        for lang in LANGS:
            if en in HAND_POLISH and lang in HAND_POLISH[en]:
                out[lang][en] = HAND_POLISH[en][lang]
                continue
            tr = compiled[lang].get(en, en)
            if tr == en and lang != "he":
                continue
            new_tr = polish_text(lang, en, tr)
            if new_tr != tr:
                out[lang][en] = new_tr

    # Always include HAND_POLISH keys
    for en, langs in HAND_POLISH.items():
        for lang, text in langs.items():
            out[lang][en] = text

    OUT.write_text(json.dumps(out, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    counts = {lang: len(out[lang]) for lang in LANGS}
    print(f"wrote {OUT}: {counts}")


if __name__ == "__main__":
    main()
