#!/usr/bin/env python3
"""Human-quality fixes for Argos-corrupted glossary_polish entries (es + he)."""

from __future__ import annotations

import json
from pathlib import Path

from _glossary_batch3 import AFTER_FR, AFTER_RU, BATCH3_FR, BATCH3_HE, BATCH3_RU
from _glossary_batch4 import BATCH4_FR, BATCH4_RU
from _glossary_batch5 import BATCH5_RU
from _glossary_batch6 import BATCH6_RU
from _glossary_batch7 import BATCH7_RU
from _glossary_batch8 import BATCH8_FR, BATCH8_RU
from _glossary_batch9 import resolve_batch9_ru
from _glossary_batch10 import BATCH10_ES, BATCH10_FR
from _glossary_batch11 import BATCH11_FR
from _glossary_batch12 import BATCH12_FR
from _glossary_batch13 import BATCH13_FR, BATCH13_RU
from _glossary_batch14 import BATCH14_ES, BATCH14_FR
from _glossary_batch15 import BATCH15_ES, BATCH15_FR, BATCH15_RU
from _glossary_batch16 import resolve_batch16_fr
from _glossary_batch17 import resolve_batch17_es
from _glossary_batch18 import BATCH18_ES, BATCH18_FR
from _glossary_batch19 import BATCH19_FR
from _glossary_batch20 import BATCH20_ES, BATCH20_FR
from _glossary_batch21 import BATCH21_ES
from _glossary_batch22 import BATCH22_ES, BATCH22_FR, BATCH22_HE, BATCH22_RU
from _glossary_batch23 import BATCH23_RU
from _glossary_batch24 import BATCH24_RU
from _glossary_batch25 import BATCH25_RU
from _glossary_batch26 import BATCH26_RU
from _glossary_batch27 import BATCH27_RU
from _glossary_batch28 import BATCH28_RU
from _glossary_batch29 import BATCH29_RU
from _glossary_batch30 import BATCH30_RU
from _glossary_batch31 import BATCH31_FR
from _glossary_es_batch2 import (
    BATCH2_ES,
    BATCH2_FR,
    BATCH2_HE,
    BATCH2_RU,
    HAVDALAH_WHEN_ES,
    HAVDALAH_WHEN_FR,
    HAVDALAH_WHEN_RU,
    HAVDALAH_YAK_ES,
    HAVDALAH_YAK_FR,
    HAVDALAH_YAK_RU,
    SHAMASH_ES,
    SHAMASH_KEY,
)

ROOT = Path(__file__).resolve().parents[1]
HUMAN = ROOT / "data" / "translation-catalog" / "human"
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
DISASTER = HUMAN / "argos_disaster_fixes.json"
GLOSSARY = HUMAN / "glossary_polish.json"

FIXES: dict[str, str] = {
    "Celebrate the gift of joy! Take a moment to thank G-d for your ability to laugh and smile 😊. Whether it's from a good joke, time with friends, or a funny moment - laughter is G-d's special medicine for the soul. Did you know? The Talmud tells us that a person who brings joy to others is rewarded greatly! 🎭": (
        "¡Celebra el regalo de la alegría! Tómate un momento para agradecer a D-s tu capacidad de reír y sonreír 😊. "
        "Ya sea por un buen chiste, tiempo con amigos o un momento divertido — la risa es una medicina especial "
        "de D-s para el alma. ¿Lo sabías? ¡El Talmud nos dice que quien trae alegría a otros es grandemente recompensado! 🎭"
    ),
    "A shaliach tzibur is the prayer leader who represents the congregation before G-d — reciting the repetition of the Amidah and guiding pacing. He must be someone the community accepts and who knows the laws of prayer. Women and men have different roles per community in who may lead which parts.": (
        "Un shaliach tzibur es el chazán o líder de oración que representa a la congregación ante D-s — "
        "recita la repetición de la Amidá y guía el ritmo. Debe ser alguien aceptado por la comunidad "
        "y que conozca las leyes de la oración. Hombres y mujeres tienen roles distintos según la "
        "comunidad en cuanto a qué partes pueden dirigir."
    ),
    "shaliach tzibur — A shaliach tzibur is the prayer leader who represents the congregation before G-d — reciting the repetition of the Amidah and guiding pacing. He must be someone the community accepts and who knows the laws of prayer. Women and men have different roles per community in who may lead which parts.": (
        "shaliach tzibur — Un shaliach tzibur es el chazán o líder de oración que representa a la "
        "congregación ante D-s — recita la repetición de la Amidá y guía el ritmo. Debe ser alguien "
        "aceptado por la comunidad y que conozca las leyes de la oración. Hombres y mujeres tienen "
        "roles distintos según la comunidad en cuanto a qué partes pueden dirigir."
    ),
    "Appreciate life's flavors! Next time you eat something delicious, pause to marvel at how G-d created such variety in tastes 😋. Sweet, salty, sour, spicy - each flavor is a unique gift! Imagine if everything tasted the same... instead, we get to enjoy this wonderful world of tastes!": (
        "¡Aprecia los sabores de la vida! La próxima vez que comas algo delicioso, haz una pausa para "
        "maravillarte de cómo D-s creó tanta variedad de gustos 😋. Dulce, salado, ácido, picante — "
        "cada sabor es un regalo único. Imagina si todo supiera igual... ¡en cambio podemos disfrutar "
        "de este maravilloso mundo de sabores!"
    ),
    "Do the mitzvah of Torah study! Here's a fun way to start - read this tiny but mighty verse: וּבְנֵי־דָ֖ן חֻשִֽׁים (U'vnei Dan Chusheem - Translation: And the sons of Dan: Chusheem). 📜 Fun fact: This is actually the shortest verse in the entire Torah! Even reading just one verse connects you to infinite wisdom. How cool is that? Also, Dan had only one son, Chusheem (and he happened to be deaf), but by the time the tribes were counted later on in the Torah, he had the second largest tribe! When you're blessed by G-d, anything is possible! ✡": (
        "¡Cumple la mitzvá del estudio de la Torá! Aquí tienes una forma divertida de empezar: lee este "
        "versículo pequeño pero poderoso: וּבְנֵי־דָ֖ן חֻשִֽׁים (U'venei Dan Chushim — Traducción: "
        "Y los hijos de Dan: Chushim). 📜 Dato curioso: ¡es el versículo más corto de toda la Torá! "
        "Incluso leer un solo versículo te conecta con sabiduría infinita. ¿Qué genial, verdad? "
        "Además, Dan solo tenía un hijo, Chushim (que era sordo), pero cuando se contaron las tribus "
        "más adelante en la Torá, ¡tenía la segunda tribu más grande! Cuando D-s te bendice, "
        "¡todo es posible! ✡"
    ),
    "Explore the mystical letter Gimmel (ג)! ✡ This amazing letter teaches us about kindness: Its shape looks like someone running - specifically, running to do good deeds! The long 'leg' of the letter reaches down, symbolizing how we should reach out to those in need. Did you know? Gimmel stands for 'Gomel' (bestowing good) - just as G-d constantly bestows goodness on us, we should bestow goodness on others! Take a moment to think: Who can you 'run' to help today? ✡": (
        "¡Explora la letra mística Guímel (ג)! ✡ Esta letra asombrosa nos enseña sobre la bondad: "
        "su forma parece alguien corriendo — ¡corriendo a hacer buenas obras! La pata larga de la "
        "letra se extiende hacia abajo, simbolizando cómo debemos tender la mano a quienes lo necesitan. "
        "¿Lo sabías? Guímel significa «Gomel» (otorgar bien) — así como D-s nos concede bondad "
        "constantemente, debemos otorgar bondad a los demás. Tómate un momento: ¿a quién puedes "
        "«correr» a ayudar hoy? ✡"
    ),
    "Have a heart-to-heart with G-d! Take 5 minutes to talk to Him like you would a close friend 💝. Share your hopes, your worries, or just chat about your day. No formal prayers needed - just genuine conversation! The sages teach us that some angels in Heaven wait thousands of years for their turn to sing praises before G-d. And we have the stupendous gift of being able to speak to Him any time we please! 🙏": (
        "¡Habla de corazón a corazón con D-s! Tómate 5 minutos para hablar con Él como con un amigo "
        "cercano 💝. Comparte tus esperanzas, tus preocupaciones o simplemente charla sobre tu día. "
        "No hacen falta oraciones formales — ¡solo una conversación genuina! Los Sabios enseñan que "
        "algunos ángeles en el Cielo esperan miles de años su turno para cantar alabanzas ante D-s. "
        "¡Y nosotros tenemos el regalo estupendo de poder hablar con Él cuando queramos! 🙏"
    ),
    "Practice mindful gratitude! Take a moment to appreciate the gift of sight 👀. Look around at the colors, shapes, and beauty of the world. From seeing loved ones smile to reading Torah to enjoying a sunset - our eyes let us experience so many wonderful things. Thank G-d for this amazing ability and let Him know you appreciate it! 🌈": (
        "¡Practica la gratitud consciente! Tómate un momento para apreciar el regalo de la vista 👀. "
        "Mira los colores, formas y belleza del mundo. Desde ver sonreír a tus seres queridos hasta "
        "leer Torá o disfrutar de una puesta de sol — nuestros ojos nos permiten experimentar tantas "
        "cosas maravillosas. ¡Agradece a D-s por esta habilidad increíble y hazle saber que la aprecias! 🌈"
    ),
    "Take a moment to think about something wonderful about your parents (whether they're here or in heaven). Maybe it's their patience, their wisdom, or their sense of humor 💭. Here's something amazing: just by thinking positive thoughts about them, you're actively fulfilling the mitzvah of honoring your parents! No action needed - just pure appreciation.": (
        "Tómate un momento para pensar en algo maravilloso de tus padres (estén aquí o en el Cielo). "
        "Tal vez su paciencia, su sabiduría o su sentido del humor 💭. Algo increíble: ¡solo con "
        "pensar positivamente sobre ellos estás cumpliendo activamente la mitzvá de honrar a tus padres! "
        "No hace falta ninguna acción — solo pura gratitud."
    ),
    "mayim achronim — Mayim achronim is washing the fingertips after a bread meal and before bentching — a reminder that we eat as servants before G-d. Not every community emphasizes it today, but many siddurim and bentchers include it. It is separate from netilat yadayim before the meal.": (
        "mayim ajronim — Mayim ajronim es el lavado de las puntas de los dedos después de una comida "
        "con pan y antes del bentching — un recordatorio de que comemos como siervos ante D-s. "
        "No todas las comunidades lo enfatizan hoy, pero muchos sidurim y bentchers lo incluyen. "
        "Es distinto del netilat yadayim antes de la comida."
    ),
    "Shema al HaMitah is the bedtime Shema — declaring faith and entrusting the soul to G-d before sleep. Many add Psalm 91 and other verses. Hamapil is said when actually lying down to sleep. Women are obligated in this protection per halacha. A few minutes of calm prayer ends the day well.": (
        "Shema al HaMitah es la Shemá al acostarse — declarar la fe y confiar el alma a D-s antes de dormir. "
        "Muchos añaden el Salmo 91 y otros versículos. Hamapil se dice al acostarse de verdad. Las mujeres "
        "están obligadas en esta protección según la halajá. Unos minutos de oración tranquila cierran bien el día."
    ),
    "Shema al HaMitah — Shema al HaMitah is the bedtime Shema — declaring faith and entrusting the soul to G-d before sleep. Many add Psalm 91 and other verses. Hamapil is said when actually lying down to sleep. Women are obligated in this protection per halacha. A few minutes of calm prayer ends the day well.": (
        "Shema al HaMitah — Shema al HaMitah es la Shemá al acostarse — declarar la fe y confiar el alma a D-s "
        "antes de dormir. Muchos añaden el Salmo 91 y otros versículos. Hamapil se dice al acostarse de verdad. "
        "Las mujeres están obligadas en esta protección según la halajá. Unos minutos de oración tranquila cierran bien el día."
    ),
    "The Shekhinah is G-d's indwelling presence — especially associated with the Temple, with Israel in exile, and with holiness in marriage and Shabbat. Kiddush Levana tradition says blessing the moon is like greeting the Shekhinah. It is not a separate being; it is how we speak of G-d being near.": (
        "La Shejiná es la presencia moradora de D-s — especialmente asociada al Templo, a Israel en el exilio, "
        "y a la santidad en el matrimonio y Shabat. La tradición de Kiddush Levana dice que bendecir la luna "
        "es como saludar a la Shejiná. No es un ser separado; es cómo hablamos de la cercanía de D-s."
    ),
    "Shekhinah — The Shekhinah is G-d's indwelling presence — especially associated with the Temple, with Israel in exile, and with holiness in marriage and Shabbat. Kiddush Levana tradition says blessing the moon is like greeting the Shekhinah. It is not a separate being; it is how we speak of G-d being near.": (
        "Shejiná — La Shejiná es la presencia moradora de D-s — especialmente asociada al Templo, a Israel en el exilio, "
        "y a la santidad en el matrimonio y Shabat. La tradición de Kiddush Levana dice que bendecir la luna "
        "es como saludar a la Shejiná. No es un ser separado; es cómo hablamos de la cercanía de D-s."
    ),
    "The Shema (\"Hear O Israel\") declares G-d's oneness and our duty to love Him with all our heart, soul, and might. It is recited morning and evening with surrounding blessings. Krias Shema has specific times — especially morning Shema before the third hour of the day halachically. It is the Jewish declaration of faith children learn first.": (
        "El Shemá («Escucha, Israel») proclama la unidad de D-s y nuestro deber de amarle con todo el corazón, "
        "alma y fuerza. Se recita por la mañana y por la noche con las bendiciones que lo acompañan. "
        "Kriat Shemá tiene horarios específicos — especialmente el Shemá de la mañana antes de la tercera "
        "hora halájica del día. Es la declaración de fe judía que los niños aprenden primero."
    ),
    "Shema — The Shema (\"Hear O Israel\") declares G-d's oneness and our duty to love Him with all our heart, soul, and might. It is recited morning and evening with surrounding blessings. Krias Shema has specific times — especially morning Shema before the third hour of the day halachically. It is the Jewish declaration of faith children learn first.": (
        "Shemá — El Shemá («Escucha, Israel») proclama la unidad de D-s y nuestro deber de amarle con todo el corazón, "
        "alma y fuerza. Se recita por la mañana y por la noche con las bendiciones que lo acompañan. "
        "Kriat Shemá tiene horarios específicos — especialmente el Shemá de la mañana antes de la tercera "
        "hora halájica del día. Es la declaración de fe judía que los niños aprenden primero."
    ),
}

RU_FIXES: dict[str, str] = {
    "The Mishnah is the first major written compilation of the Oral Torah, edited around 200 CE. It organizes halacha by topic (Shabbat, blessings, damages, and so on) in concise Hebrew. The Gemara and Talmud Yerushalmi explain and debate the Mishnah. Beginners often start Mishnah before full Talmud.": (
        "Мишна — первая крупная письменная сводка Устной Торы, отредактированная около 200 года н.э. "
        "Она организует галаху по темам (Шаббат, благословения, ущерб и т.д.) на лаконичном иврите. "
        "Гемара и Талмуд Йерушалми объясняют и обсуждают Мишну. Начинающие часто начинают с Мишны, прежде чем переходить к полному Талмуду."
    ),
    "The Shekhinah is G-d's indwelling presence — especially associated with the Temple, with Israel in exile, and with holiness in marriage and Shabbat. Kiddush Levana tradition says blessing the moon is like greeting the Shekhinah. It is not a separate being; it is how we speak of G-d being near.": (
        "Шхина — это божественное присутствие, пребывающее в мире — особенно связанное с Храмом, "
        "с Израилем в изгнании и со святостью в браке и на Шаббате. Традиция Кидуш Левана говорит, "
        "что благословение луны подобно приветствию Шхины. Это не отдельное существо; так мы говорим о близости В-его."
    ),
    "The Shema (\"Hear O Israel\") declares G-d's oneness and our duty to love Him with all our heart, soul, and might. It is recited morning and evening with surrounding blessings. Krias Shema has specific times — especially morning Shema before the third hour of the day halachically. It is the Jewish declaration of faith children learn first.": (
        "Шма («Слушай, Израиль») провозглашает единство В-его и нашу обязанность любить Его всем сердцем, "
        "душой и могуществом. Читается утром и вечером с окружающими благословениями. У Kriat Shema есть "
        "определённые времена — особенно утренняя Шма до третьего часа дня по галахе. "
        "Это еврейская декларация веры, которую дети учат первой."
    ),
}

HE_FIXES: dict[str, str] = {}

HAMAPIL_KEY = (
    "Hamapil is the very last thing said before closing your eyes to sleep.\n\nWhat it is:\n"
    "Hamapil (הַמַּפִּיל — \"who casts\") is a blessing thanking G-d for the gift of sleep and asking "
    "for protection through the night. It is unique because it is said before an action you are about "
    "to do (sleep) rather than while doing it.\n\nThe blessing asks that:\n• Our eyes be allowed to sleep\n"
    "• We wake in the morning in peace\n• We not be disturbed by bad dreams\n• Our sleep refresh and heal us\n\n"
    "Why sleep requires a blessing:\nThe Talmud teaches that sleep is \"one-sixtieth of death\" — a nightly "
    "partial departure of the soul from the body. Saying Hamapil acknowledges this transition and expresses "
    "total trust in G-d.\n\nHow to say it:\nSay it after the bedtime Shema, immediately before closing your eyes.\n\n"
    "After saying Hamapil (Rama, Shulchan Arukh O.C. 239:1): do not eat, drink, or speak until you fall asleep. "
    "Hamapil is meant to be the final spiritual anchor directly adjacent to losing consciousness — not followed "
    "by extended activity.\n\nIf an urgent need arises (a child crying, needing the restroom, a security matter), "
    "you may speak — this does not cancel the blessing and you do not need to repeat it (Biur Halachah O.C. 239:1).\n\n"
    "What may you do while waiting to fall asleep?\n• Repeating verses of Shema, or quietly murmuring familiar "
    "protective verses you already know to help yourself doze off — this is universally permitted and is not "
    "considered an interruption (Biur Halachah O.C. 239:1).\n• Sitting up to deliberately begin new Torah study, "
    "or reading lengthy chapters of Tehillim after the final blessing of Hamapil — classical codifiers discourage "
    "this; the sages wanted Hamapil to lead straight into sleep, not into a study session.\n\n"
    "If you find yourself wide awake for hours and simply cannot sleep, some poskim permit leniently relying on "
    "the view that learning Torah is allowed rather than lying idle — but this is a fallback for genuine inability "
    "to sleep, not an open standard permission from the outset.\n\nWhat if you're not sure you'll fall asleep?\n"
    "There are two main positions:\n\n1. Ashkenazi ruling — always say it (Mishnah Berurah 239:6; Chazon Ish, OC 37:11):\n"
    "Hamapil is a blessing of praise for G-d's creation of sleep in general, not a personal guarantee that you will "
    "fall asleep at once. Even if you suffer from insomnia, say it. As long as you lie down with the intent to sleep, "
    "the blessing is valid — any sleep you eventually get that night validates it retroactively.\n\n"
    "2. Sephardic and Kabbalistic ruling — exercise caution (Ben Ish Chai, Year 1, Parashat Pekudei 12; Kaf HaChaim OC 239:18):\n"
    "If you are genuinely uncertain you will sleep soon — due to illness, severe anxiety, or acute insomnia — do not "
    "say the full blessing with G-d's name (Shem u'Malchut), to avoid a blessing said in vain (bracha levatala). "
    "Instead, say the words as a meditative prayer while thinking G-d's name silently, or omit the closing signature "
    "(Baruch Atah...) entirely. The principle applied is: safek brachot l'hakel — when in doubt about a blessing, "
    "be lenient and omit it."
)

HAMAPIL_HE = (
    "המפיל הוא הדבר האחרון שאומרים לפני סגירת העיניים לשינה.\n\nמה זה:\n"
    "המפיל (הַמַּפִּיל — «המטיל») היא ברכה שמודה לה' על מתנת השינה ומבקשת הגנה במשך הלילה. "
    "היא ייחודית כי נאמרת לפני מעשה שעומדים לעשות (שינה) ולא במהלכו.\n\nהברכה מבקשת ש:\n"
    "• עינינו יורשו לישון\n• נקום בבוקר לשלום\n• לא ניווכח בחלומות רעים\n• שינתנו תרענן ותרפא אותנו\n\n"
    "למה שינה דורשת ברכה:\nהתלמוד מלמד ששינה היא «אחד משישים במיתה» — ירידה חלקית לילית של הנשמה מהגוף. "
    "אמירת המפיל מכירה במעבר הזה ומבטאת אמון מוחלט בה'.\n\nאיך אומרים:\n"
    "אומרים אותה אחרי קריאת שמע על המיטה, ממש לפני סגירת העיניים.\n\n"
    "אחרי אמירת המפיל (רמ״א, שולחן ערוך או״ח רל״ט:א): לא לאכול, לא לשתות ולא לדבר עד שנרדמים. "
    "המפיל נועדה להיות העוגן הרוחני האחרון ממש לפני איבוד ההכרה — לא אחרי פעילות ממושכת.\n\n"
    "אם מתעורר צורך דחוף (ילד בוכה, צורך בשירותים, עניין ביטחוני) — מותר לדבר; זה לא מבטל את הברכה "
    "ואין צורך לחזור עליה (ביאור הלכה או״ח רל״ט:א).\n\nמה מותר בזמן ההמתנה לשינה?\n"
    "• חזרה על פסוקי שמע, או מלמול בשקט פסוקי הגנה מוכרים כדי להירדם — מותר לחלוטין ואינו הפסקה "
    "(ביאור הלכה או״ח רל״ט:א).\n• לקום ללימוד תורה חדש במכוון, או לקרוא פרקים ארוכים מתהילים אחרי חתימת "
    "המפיל — הפוסקים הקלאסיים מתנגדים; החכמים רצו שהמפיל תוביל ישר לשינה.\n\n"
    "אם אתם ערים שעות ואינכם יכולים לישון, יש מקילים שמסתמכים על דעה שמותר ללמוד תורה במקום לשכב בטל — "
    "אך זה פתרון לחוסר יכולת אמיתי לישון, לא היתר כללי מראש.\n\nמה אם אינכם בטוחים שתירדמו?\n"
    "שתי עמדות עיקריות:\n\n1. פסיקת אשכנז — תמיד אומרים (משנה ברורה רל״ט:ו; חזון איש או״ח ל״ז:י״א):\n"
    "המפיל היא ברכת שבח על בריאת השינה בכלל, לא הבטחה אישית שתירדמו מיד. גם עם נדודי שינה — אומרים. "
    "כל עוד שוכבים בכוונה לישון, הברכה תקפה; כל שינה שתבוא באותו לילה מקיימת אותה בדיעבד.\n\n"
    "2. פסיקת ספרדים וקבלה — זהירות (בן איש חי, שנה א', פרשת פקודי י״ב; כף החיים או״ח רל״ט:י״ח):\n"
    "אם באמת אינכם בטוחים שתירדמו בקרוב — מחלה, חרדה חמורה או נדודי שינה חריפים — אל תאמרו את הברכה "
    "המלאה בשם ומלכות, כדי שלא תצא ברכה לבטלה. אפשר לומר את המילים כתפילה מדיטטיבית בלב, "
    "או לדלג על החתימה. העיקרון: ספק ברכות להקל."
)

THREE_WEEKS_SHORT_KEY = (
    "The Three Weeks (בין המצרים) from 17 Tammuz until Tisha B'Av commemorate the destruction of the Temple "
    "and Jewish tragedies.\n\nWhy we mourn:\n• On 17 Tammuz the walls of Jerusalem were breached; on 9 Av both "
    "Temples were destroyed, among other national calamities.\n\nShabbat during the Three Weeks: mourning practices "
    "do not apply on Shabbat itself — observe Shabbat fully."
)

THREE_WEEKS_SHORT_HE = (
    "שלושת השבועות (בין המצרים) מי״ז בתמוז עד תשעה באב מנציחים את חורבן המקדש ואת הטרגדיות היהודיות.\n\n"
    "למה מתאבלים:\n• בי״ז בתמוז נפרצו חומות ירושלים; בט׳ באב נחרבו שני המקדשים, בין שאר האסונות הלאומיים.\n\n"
    "שבת במהלך שלושת השבועות: אבלות אינה חלה בשבת עצמה — שמרו שבת כראוי."
)

THREE_WEEKS_KEY = (
    "The Three Weeks (בין המצרים) from 17 Tammuz until Tisha B'Av commemorate the destruction of the Temple "
    "and Jewish tragedies.\n\nWhy we mourn:\n• On 17 Tammuz the walls of Jerusalem were breached; on 9 Av both "
    "Temples were destroyed, among other national calamities.\n\nShabbat during the Three Weeks: mourning practices "
    "do not apply on Shabbat itself — observe Shabbat fully.\n\nSephardic and Edot HaMizrach communities, following "
    "Shulchan Arukh, generally take a more lenient approach than Ashkenazim during the early Three Weeks.\n\n"
    "From 17 Tammuz (general Three Weeks):\n• Haircuts & shaving: permitted during most of the Three Weeks; "
    "shaving is usually prohibited only during the week in which Tisha B'Av falls (shavuah she'chal bo).\n"
    "• Music: live or recorded music is avoided.\n• Weddings: some communities avoid weddings from 17 Tammuz; "
    "others are lenient and avoid them only from Rosh Chodesh Av — follow your kehilla.\n"
    "• Shehecheyanu: avoided on new items for the duration of the period.\n\n"
    "From Rosh Chodesh Av or the week of Tisha B'Av: additional restrictions apply — see the Nine Days checklist item. "
    "Some communities (e.g. Syrian, Mashadi) are stricter on meat and wine from Rosh Chodesh Av."
)

THREE_WEEKS_HE = (
    "שלושת השבועות (בין המצרים) מי״ז בתמוז עד תשעה באב מנציחים את חורבן המקדש ואת הטרגדיות היהודיות.\n\n"
    "למה מתאבלים:\n• בי״ז בתמוז נפרצו חומות ירושלים; בט׳ באב נחרבו שני המקדשים, בין שאר האסונות הלאומיים.\n\n"
    "שבת במהלך שלושת השבועות: אבלות אינה חלה בשבת עצמה — שמרו שבת כראוי.\n\n"
    "קהילות ספרדיות ועדות המזרח, לפי השולחן ערוך, נוהגות בדרך כלל בקולות יותר מאשכנזים בתחילת שלושת השבועות.\n\n"
    "מי״ז בתמוז (שלושת השבועות בכלל):\n• תספורת וגילוח: מותרים ברוב שלושת השבועות; גילוח אסור בדרך כלל רק "
    "בשבוע שחל בו תשעה באב (שבוע שחל בו).\n• מוזיקה: נמנעים ממוזיקה חיה או מוקלטת.\n• חתונות: יש קהילות "
    "שנמנעות מחתונות מי״ז בתמוז; אחרות מקילות ונמנעות רק מראש חודש אב — לפי מנהג הקהילה.\n"
    "• שהחיינו: נמנעים על פריטים חדשים לכל התקופה.\n\n"
    "מראש חודש אב או משבוע תשעה באב: מגבלות נוספות — ראו פריט רשימת תשעת הימים. "
    "יש קהילות (סוריות, משאדיות וכו׳) שמחמירות בבשר ויין מראש חודש אב."
)

HE_FIXES.update(
    {
        HAMAPIL_KEY: HAMAPIL_HE,
        THREE_WEEKS_SHORT_KEY: THREE_WEEKS_SHORT_HE,
        THREE_WEEKS_KEY: THREE_WEEKS_HE,
        "The Shekhinah is G-d's indwelling presence — especially associated with the Temple, with Israel in exile, and with holiness in marriage and Shabbat. Kiddush Levana tradition says blessing the moon is like greeting the Shekhinah. It is not a separate being; it is how we speak of G-d being near.": (
            "השכינה היא נוכחותו השורה של ה' — קשורה במיוחד למקדש, לישראל בגלות, ולקדושה בנישואין ובשבת. "
            "מסורת קידוש לבנה אומרת שברכת הירח דומה לקבלת פני השכינה. אינה ישות נפרדת; כך מדברים על קרבת ה'."
        ),
        "The Shulchan Aruch (\"Set Table\") is the classic 16th-century code of halacha (Jewish Law) by Rabbi Yosef Karo. Ashkenazim often study it with the Rema's glosses; Sephardim generally follow Rabbi Karo, but rabbis have debated about almost everything throughout the years.": (
            "השולחן ערוך («שולחן מוכן») הוא קודקס הלכה הקלאסי מהמאה ה-16 מאת הרב יוסף קארו. "
            "אשכנזים לומדים אותו לעיתים עם הגהות הרמ״א; ספרדים בדרך כלל הולכים אחרי הרב קארו, "
            "אך רבנים חלקו כמעט על הכל לאורך השנים."
        ),
    }
)

BIRCHOT_KEY = (
    "A series of short blessings said at the beginning of the morning prayer service, thanking G-d "
    "for things we tend to take completely for granted.\n\nWhat they are:\nBirchot HaShachar "
    "(בִּרְכוֹת הַשַּׁחַר — Blessings of the Dawn) is a sequence of ~15 blessings at the start of "
    "Shacharit. They originated as blessings said at each stage of waking up and preparing for the "
    "day — as you open your eyes, stand up, put on clothes, and step outside.\n\nWhat you thank G-d "
    "for:\n• Giving intelligence to the rooster to distinguish day from night\n• Not making you a "
    "non-Jew, a slave, or a woman (men's version; women say \"for making me according to His will\")\n"
    "• Opening the eyes of the blind\n• Clothing the naked\n• Straightening those who are bent\n"
    "• Spreading the earth over the waters\n• Giving strength to the weary\n• And more...\n\n"
    "What comes before Birchot HaShachar:\nIn the full morning service, Korbanot (passages about "
    "Temple offerings) typically come before or after these blessings. While these may be skipped "
    "if time is short, saying them is an important part of the complete service.\n\nTiming:\n"
    "• The earliest time to say Birchot HaShachar is from chatzos (halachic midnight / chatzos "
    "halayla).\n• L'chatchila (ideally), say the first blessing — HaNoten LaSechvi Vinah (who gives "
    "the rooster understanding to distinguish day from night) — after alot hashachar (dawn). If you "
    "said it after chatzos, you have still fulfilled your obligation.\n• The other blessings in the "
    "sequence may be said from chatzos onward if you wake up very early."
)

BIRCHOT_ES = (
    "Una serie de breves bendiciones al inicio del servicio de oración matutino, agradeciendo a D-s "
    "por cosas que solemos dar por sentadas.\n\nQué son:\nBirchot HaShachar (בִּרְכוֹת הַשַּׁחַר — "
    "Bendiciones del Alba) es una secuencia de ~15 bendiciones al inicio de Shajarit. Se originaron "
    "como bendiciones dichas en cada etapa de despertar y prepararse para el día — al abrir los ojos, "
    "levantarse, vestirse y salir.\n\nPor qué agradeces a D-s:\n• Dar inteligencia al gallo para "
    "distinguir el día de la noche\n• No hacerme gentil, esclavo ni mujer (versión masculina; las "
    "mujeres dicen «por hacerme según Su voluntad»)\n• Abrir los ojos de los ciegos\n"
    "• Vestir a los desnudos\n• Enderezar a los encorvados\n• Extender la tierra sobre las aguas\n"
    "• Dar fuerza al fatigado\n• Y más...\n\nQué viene antes de Birchot HaShachar:\nEn el servicio "
    "matutino completo, Korbanot (pasajes sobre ofrendas del Templo) suelen ir antes o después de "
    "estas bendiciones. Aunque pueden omitirse si falta tiempo, recitarlas es parte importante del "
    "servicio completo.\n\nMomento:\n• La hora más temprana para decir Birchot HaShachar es desde "
    "jotzot (medianoche halájica / jotzot halaila).\n• Lejatjila (idealmente), diga la primera "
    "bendición — HaNoten LaSechvi Binah (que da al gallo entendimiento para distinguir el día de la "
    "noche) — después de alot hashajar (el alba). Si la dijo después de jotzot, igual cumplió la "
    "obligación.\n• Las demás bendiciones de la secuencia pueden decirse desde jotzot en adelante si "
    "te despiertas muy temprano."
)

AFTER_KEY = (
    "After eating or drinking, we thank G-d with an after-blessing (bracha acharona). Which blessing "
    "you say depends on what you ate or drank — and whether you ate or drank enough, within the "
    "required time.\n\nThe after-blessings:\n\nBirkat Hamazon (בִּרְכַּת הַמָּזוֹן — Grace After Meals):\n"
    "• After eating bread (Hamotzi)\n• A full multi-paragraph prayer of thanks; a Torah-level "
    "commandment (Deuteronomy 8:10)\n• When three or more adult Jewish men eat bread together, "
    "Zimmun (זִמּוּן — a formal invitation) is recited together before Birkat Hamazon\n"
    "• A bentcher (בֶּנְטְשֶׁר) is a small booklet containing the text — commonly found at Shabbat "
    "tables\n• After a bread meal: if you ate at least a kezayit of bread within k'dei achilat pras, "
    "Birkat Hamazon (bentching) covers everything else you ate at that same meal — wine, meat, fruit, "
    "dessert, etc. You do not say separate after-blessings for those other foods.\n\n"
    "Al HaMichya (עַל הַמִּחְיָה — Bracha Achat Me'ein Shalosh):\n• After Mezonot — grain foods that "
    "are not bread: cake, crackers, pasta, cereal, etc. (wheat, barley, spelt, oat, rye)\n"
    "• A condensed “one blessing like three” derived from Birkat Hamazon\n• At the same sitting, if "
    "you also had enough wine/grape juice or enough shivat ha-minim tree fruits (below), use one Al "
    "HaMichya text with the inserted phrases (v'al hagafen v'al pri hagafen, v'al ha'etz v'al pri "
    "ha'etz) in your siddur\n\nAl HaGafen (עַל הַגָּפֶן וְעַל פְּרִי הַגָּפֶן):\n• After wine or grape "
    "juice — the full after-blessing is על הגפן ועל פרי הגפן (the vine and the fruit of the vine), "
    "not על הגפן alone\n• At least a revi'it within k'dei shtiyat revi'it (not Borei Nefashot)\n"
    "• Same category as the before-blessing Borei pri ha'gafen (בּוֹרֵא פְּרִי הַגָּפֶן)\n"
    "• Wine or grape juice only — fresh grapes eaten as fruit take Al HaEtz (see below), not Al "
    "HaGafen\n\nAl HaEtz (עַל הָעֵץ וְעַל פְּרִי הָעֵץ — Bracha Achat Me'ein Shalosh):\n"
    "• After shivat ha-minim (Seven Species) tree fruits: dates, figs, pomegranates, olives — and "
    "fresh grapes eaten as fruit (not grape juice)\n• Fresh grapes: Because grapes are one of the "
    "Seven Species, eating a kezayit of fresh grapes always requires Al HaEtz (Me'ein Shalosh), not "
    "Borei Nefashot — whereas regular tree fruits like apples or oranges take Borei Nefashot only\n"
    "• The full phrase is על העץ ועל פרי העץ (the tree and the fruit of the tree) throughout the "
    "blessing — opening, thanks, and closing — not על העץ alone\n• Not for apples, oranges, bananas, "
    "berries, or other non–shivat ha-minim tree fruits — those use Borei Nefashot only\n"
    "• A kezayit within k'dei achilat pras\n\nBorei Nefashot (בּוֹרֵא נְפָשׁוֹת — \"Who creates "
    "souls\"):\n• After most other foods: meat, fish, eggs, dairy, vegetables, and fruits that are "
    "not shivat ha-minim\n• After most drinks other than wine/grape juice, if you drank a revi'it "
    "within k'dei shtiyat revi'it\n• The shortest after-blessing; can be memorized in minutes\n\n"
    "How much food — k'dei achilat pras (eating):\n• Generally need at least a kezayit (כְּזַיִת — "
    "about olive-sized; many use roughly 1 oz / 30 g)\n• Eaten within k'dei achilat pras (כְּדֵי "
    "אֲכִילַת פְּרַס — the time to eat about half a loaf): lechatchila about 4 minutes; many poskim "
    "allow up to about 6–9 minutes bedi'eved (Shulchan Aruch / Mishnah Berurah)\n• Less than a "
    "kezayit, or spread over longer than k'dei achilat pras: no after-blessing on that food\n\n"
    "How much drink — k'dei shtiyat revi'it (drinking) — not the same as k'dei achilat pras:\n"
    "Halacha often requires an action within a specific continuous timeframe. For eating a kezayit, "
    "that is k'dei achilat pras (minutes — see above). For drinking, it is much faster.\n\n"
    "K'dei shtiyat revi'it (כְּדֵי שְׁתִיַּת רְבִיעִית) literally means the time it takes to drink a "
    "revi'it (about 3–5 fl oz / 86–150 ml depending on custom). It is a very short span — a few "
    "continuous seconds — used as a legal measurement for drinking.\n\nFor bracha acharona on a "
    "beverage:\n• You need a revi'it drunk within k'dei shtiyat revi'it\n• If you sip coffee slowly "
    "over 20 minutes, you did not drink the required volume within the required time — no "
    "after-blessing on that drink\n• How many seconds? Poskim differ:\n  — Sephardic / Rambam / Rav "
    "Ovadia Yosef: one continuous drink without removing the cup from your lips — the strictest view; "
    "halacha defines this qualitatively (not as a fixed second count), but it is only a brief "
    "uninterrupted span — often estimated at just a few seconds for a full revi'it\n  — Ashkenaz / "
    "Mishnah Berurah: often estimated as a continuous sequence of normal gulps within 5–9 seconds "
    "max. To guarantee your obligation for an after-blessing, drink the beverage in a single, "
    "uninterrupted sequence.\n• Cold drinks (water, juice): to be obligated in Borei Nefashot or Al "
    "HaGafen, swallow a revi'it quickly and continuously in those few seconds rather than nursing "
    "it slowly\n• Hot coffee, tea, or soup: drinking that fast is difficult, so common practice is "
    "no after-blessing when you only sip slowly — unless you set aside at least a revi'it and drink "
    "it at once when cool enough\n\nDrinks only — which after-blessing?\n• Wine or grape juice: Al "
    "HaGafen (עַל הַגָּפֶן וְעַל פְּרִי הַגָּפֶן) if a revi'it within k'dei shtiyat revi'it\n"
    "• Water, milk, soda, etc.: Borei Nefashot if a revi'it within k'dei shtiyat revi'it\n"
    "• Mezonot snack plus wine at the same sitting: one Al HaMichya including the wine phrase if "
    "both meet their thresholds\n\nWhere to find the exact text:\nOpen your siddur or bentcher to "
    "the bracha acharona section. In Mitz Mode, tap the menu (⋮) → **Blessings** for before- and "
    "after-blessing texts."
)

AFTER_ES = (
    "Después de comer o beber, agradecemos a D-s con una bendición posterior (berajá ajrona). "
    "Cuál bendición se dice depende de lo que comiste o bebiste — y si comiste o bebiste "
    "suficiente, dentro del tiempo requerido.\n\nLas bendiciones posteriores:\n\n"
    "Birkat Hamazon (בִּרְכַּת הַמָּזוֹן — Gracia después de las comidas):\n"
    "• Después de comer pan (Hamotzi)\n"
    "• Una oración completa de agradecimiento de varios párrafos; mandamiento a nivel de Torá "
    "(Deuteronomio 8:10)\n"
    "• Cuando tres o más hombres judíos adultos comen pan juntos, se recita Zimmun (זִמּוּן — "
    "invitación formal) antes de Birkat Hamazon\n"
    "• Un bentcher (בֶּנְטְשֶׁר) es un librito con el texto — común en las mesas de Shabat\n"
    "• Después de una comida con pan: si comiste al menos un kezayit de pan dentro de k'dei "
    "achilat pras, Birkat Hamazon (bentching) cubre todo lo demás que comiste en esa misma comida — "
    "vino, carne, fruta, postre, etc. No se dicen bendiciones posteriores separadas para esos "
    "otros alimentos.\n\n"
    "Al HaMichya (עַל הַמִּחְיָה — Berajá Ajat Me'ein Shalosh):\n"
    "• Después de Mezonot — alimentos de grano que no son pan: pastel, galletas, pasta, cereal, etc. "
    "(trigo, cebada, espelta, avena, centeno)\n"
    "• Una bendición condensada «una como tres», derivada de Birkat Hamazon\n"
    "• En la misma sesión, si también tomaste suficiente vino/jugo de uva o suficientes frutas de "
    "árbol de shivat ha-minim (abajo), usa un texto de Al HaMichya con las frases insertadas "
    "(v'al hagafen v'al pri hagafen, v'al ha'etz v'al pri ha'etz) en tu sidur\n\n"
    "Al HaGafen (עַל הַגָּפֶן וְעַל פְּרִי הַגָּפֶן):\n"
    "• Después de vino o jugo de uva — la bendición posterior completa es על הגפן ועל פרי הגפן "
    "(la vid y el fruto de la vid), no על הגפן sola\n"
    "• Al menos un revi'it dentro de k'dei shtiyat revi'it (no Borei Nefashot)\n"
    "• Misma categoría que la bendición previa Borei pri ha'gafen (בּוֹרֵא פְּרִי הַגָּפֶן)\n"
    "• Solo vino o jugo de uva — las uvas frescas comidas como fruta requieren Al HaEtz (ver abajo), "
    "no Al HaGafen\n\n"
    "Al HaEtz (עַל הָעֵץ וְעַל פְּרִי הָעֵץ — Berajá Ajat Me'ein Shalosh):\n"
    "• Después de frutas de árbol de shivat ha-minim (Siete Especies): dátiles, higos, granadas, "
    "aceitunas — y uvas frescas comidas como fruta (no jugo de uva)\n"
    "• Uvas frescas: como las uvas son una de las Siete Especies, comer un kezayit de uvas frescas "
    "siempre requiere Al HaEtz (Me'ein Shalosh), no Borei Nefashot — mientras que frutas de árbol "
    "comunes como manzanas o naranjas solo requieren Borei Nefashot\n"
    "• La frase completa es על העץ ועל פרי העץ (el árbol y el fruto del árbol) a lo largo de la "
    "bendición — apertura, agradecimiento y cierre — no על העץ sola\n"
    "• No para manzanas, naranjas, plátanos, bayas u otras frutas de árbol que no sean shivat "
    "ha-minim — esas usan solo Borei Nefashot\n"
    "• Un kezayit dentro de k'dei achilat pras\n\n"
    "Borei Nefashot (בּוֹרֵא נְפָשׁוֹת — «Quien crea almas»):\n"
    "• Después de la mayoría de otros alimentos: carne, pescado, huevos, lácteos, verduras y frutas "
    "que no son shivat ha-minim\n"
    "• Después de la mayoría de bebidas que no sean vino/jugo de uva, si bebiste un revi'it dentro "
    "de k'dei shtiyat revi'it\n"
    "• La bendición posterior más corta; se puede memorizar en minutos\n\n"
    "Cuánta comida — k'dei achilat pras (comer):\n"
    "• Generalmente se necesita al menos un kezayit (כְּזַיִת — del tamaño de una aceituna; muchos "
    "usan aproximadamente 30 g)\n"
    "• Comido dentro de k'dei achilat pras (כְּדֵי אֲכִילַת פְּרַס — el tiempo de comer media hogaza): "
    "lejatjila unos 4 minutos; muchos poskim permiten hasta unos 6–9 minutos bedi'eved (Shulján "
    "Aruj / Mishná Berurá)\n"
    "• Menos de un kezayit, o extendido más allá de k'dei achilat pras: no hay bendición posterior "
    "por ese alimento\n\n"
    "Cuánta bebida — k'dei shtiyat revi'it (beber) — no es lo mismo que k'dei achilat pras:\n"
    "La halajá a menudo exige una acción dentro de un marco continuo específico. Para comer un "
    "kezayit, eso es k'dei achilat pras (minutos — ver arriba). Para beber, es mucho más rápido.\n\n"
    "K'dei shtiyat revi'it (כְּדֵי שְׁתִיַּת רְבִיעִית) significa literalmente el tiempo que toma "
    "beber un revi'it (unos 86–150 ml según la costumbre). Es un lapso muy breve — unos pocos "
    "segundos continuos — usado como medida legal para beber.\n\n"
    "Para berajá ajrona en una bebida:\n"
    "• Necesitas un revi'it bebido dentro de k'dei shtiyat revi'it\n"
    "• Si sorbeas café lentamente durante 20 minutos, no bebiste el volumen requerido en el tiempo "
    "requerido — no hay bendición posterior por esa bebida\n"
    "• ¿Cuántos segundos? Los poskim difieren:\n"
    "  — Sefardíes / Rambam / Rav Ovadia Yosef: un trago continuo sin quitar la copa de los labios — "
    "la postura más estricta; la halajá lo define cualitativamente (no como un conteo fijo de "
    "segundos), pero es solo un breve lapso ininterrumpido — a menudo estimado en unos pocos "
    "segundos para un revi'it completo\n"
    "  — Ashkenazí / Mishná Berurá: a menudo estimado como una secuencia continua de tragos "
    "normales dentro de 5–9 segundos como máximo. Para garantizar la obligación de una bendición "
    "posterior, bebe la bebida en una sola secuencia ininterrumpida.\n"
    "• Bebidas frías (agua, jugo): para estar obligado en Borei Nefashot o Al HaGafen, traga un "
    "revi'it rápida y continuamente en esos pocos segundos en lugar de sorberlo lentamente\n"
    "• Café caliente, té o sopa: beber tan rápido es difícil, así que la práctica común es no decir "
    "bendición posterior cuando solo sorbeas lentamente — a menos que reserves al menos un revi'it y "
    "lo bebas de una vez cuando esté lo suficientemente frío\n\n"
    "Solo bebidas — ¿qué bendición posterior?\n"
    "• Vino o jugo de uva: Al HaGafen (עַל הַגָּפֶן וְעַל פְּרִי הַגָּפֶן) si un revi'it dentro de "
    "k'dei shtiyat revi'it\n"
    "• Agua, leche, refresco, etc.: Borei Nefashot si un revi'it dentro de k'dei shtiyat revi'it\n"
    "• Merienda Mezonot más vino en la misma sesión: un Al HaMichya incluyendo la frase del vino si "
    "ambos alcanzan sus umbrales\n\n"
    "Dónde encontrar el texto exacto:\nAbre tu sidur o bentcher en la sección de berajá ajrona. "
    "En Mitz Mode, toca el menú (⋮) → **Bendiciones** para los textos de bendiciones anteriores "
    "y posteriores."
)


def merge_into(path: Path, lang: str, fixes: dict[str, str]) -> int:
    data = json.loads(path.read_text(encoding="utf-8")) if path.exists() else {}
    block = data.setdefault(lang, {})
    n = 0
    for key, tr in fixes.items():
        if block.get(key) != tr:
            block[key] = tr
            n += 1
    path.write_text(json.dumps(data, ensure_ascii=False, indent=2), encoding="utf-8")
    return n


def catalog_key(needle: str) -> str:
    for k in json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]:
        if needle in k:
            return k
    raise KeyError(f"catalog key not found: {needle!r}")


def main() -> None:
    es_fixes = dict(FIXES)
    es_fixes.update(BATCH2_ES)
    es_fixes.update(BATCH10_ES)
    es_fixes.update(BATCH14_ES)
    es_fixes.update(BATCH15_ES)
    es_fixes.update(resolve_batch17_es(json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]))
    es_fixes.update(BATCH18_ES)
    es_fixes.update(BATCH20_ES)
    es_fixes.update(BATCH21_ES)
    es_fixes.update(BATCH22_ES)
    es_fixes[BIRCHOT_KEY] = BIRCHOT_ES
    es_fixes[AFTER_KEY] = AFTER_ES
    es_fixes[SHAMASH_KEY] = SHAMASH_ES
    es_fixes[catalog_key("Havdalah inside Kiddush")] = HAVDALAH_YAK_ES
    es_fixes[catalog_key("Havdalah when Shabbat leads")] = HAVDALAH_WHEN_ES

    fr_fixes = dict(BATCH2_FR)
    fr_fixes.update(BATCH3_FR)
    fr_fixes.update(BATCH4_FR)
    fr_fixes.update(BATCH8_FR)
    fr_fixes.update(BATCH10_FR)
    fr_fixes.update(BATCH11_FR)
    fr_fixes.update(BATCH12_FR)
    fr_fixes.update(BATCH13_FR)
    fr_fixes.update(BATCH14_FR)
    fr_fixes.update(BATCH15_FR)
    fr_fixes.update(resolve_batch16_fr(json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]))
    fr_fixes.update(BATCH18_FR)
    fr_fixes.update(BATCH19_FR)
    fr_fixes.update(BATCH20_FR)
    fr_fixes.update(BATCH22_FR)
    fr_fixes.update(BATCH31_FR)
    fr_fixes[catalog_key("Havdalah inside Kiddush")] = HAVDALAH_YAK_FR
    fr_fixes[catalog_key("Havdalah when Shabbat leads")] = HAVDALAH_WHEN_FR
    fr_fixes[AFTER_KEY] = AFTER_FR

    ru_fixes = dict(RU_FIXES)
    ru_fixes.update(BATCH2_RU)
    ru_fixes.update(BATCH3_RU)
    ru_fixes.update(BATCH4_RU)
    ru_fixes.update(BATCH5_RU)
    ru_fixes.update(BATCH6_RU)
    ru_fixes.update(BATCH7_RU)
    ru_fixes.update(BATCH8_RU)
    ru_fixes.update(resolve_batch9_ru(json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]))
    ru_fixes.update(BATCH13_RU)
    ru_fixes.update(BATCH15_RU)
    ru_fixes.update(BATCH22_RU)
    ru_fixes.update(BATCH23_RU)
    ru_fixes.update(BATCH24_RU)
    ru_fixes.update(BATCH25_RU)
    ru_fixes.update(BATCH26_RU)
    ru_fixes.update(BATCH27_RU)
    ru_fixes.update(BATCH28_RU)
    ru_fixes.update(BATCH29_RU)
    ru_fixes.update(BATCH30_RU)
    ru_fixes[catalog_key("Havdalah inside Kiddush")] = HAVDALAH_YAK_RU
    ru_fixes[catalog_key("Havdalah when Shabbat leads")] = HAVDALAH_WHEN_RU
    ru_fixes[AFTER_KEY] = AFTER_RU

    HE_FIXES.update(BATCH2_HE)
    HE_FIXES.update(BATCH3_HE)
    HE_FIXES.update(BATCH22_HE)

    n1_es = merge_into(DISASTER, "es", es_fixes)
    n1_fr = merge_into(DISASTER, "fr", fr_fixes)
    n1_he = merge_into(DISASTER, "he", HE_FIXES)
    n1_ru = merge_into(DISASTER, "ru", ru_fixes)
    n2_es = merge_into(GLOSSARY, "es", es_fixes)
    n2_fr = merge_into(GLOSSARY, "fr", fr_fixes)
    n2_he = merge_into(GLOSSARY, "he", HE_FIXES)
    n2_ru = merge_into(GLOSSARY, "ru", ru_fixes)
    print(f"Updated {DISASTER.name}: {n1_es} es, {n1_fr} fr, {n1_he} he, {n1_ru} ru entries")
    print(f"Updated {GLOSSARY.name}: {n2_es} es, {n2_fr} fr, {n2_he} he, {n2_ru} ru entries")
    print(
        f"Total keys fixed: {len(es_fixes)} es, {len(fr_fixes)} fr, "
        f"{len(HE_FIXES)} he, {len(ru_fixes)} ru"
    )


if __name__ == "__main__":
    main()
