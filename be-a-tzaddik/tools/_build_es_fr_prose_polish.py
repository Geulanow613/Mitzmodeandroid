#!/usr/bin/env python3
"""Build human/es_fr_prose_polish.json — targeted ES/FR fixes for worst long-form strings."""
from __future__ import annotations

import json
from pathlib import Path

from _nine_days_es_fr_data import NINE_DAYS_ES_FR_BY_PREFIX
from _three_weeks_ty_data import THREE_WEEKS_INTRO_ES, THREE_WEEKS_INTRO_FR
from _yaaleh_es_fr_data import YAALEH_TEMPLATE_ES, YAALEH_TEMPLATE_FR
from _melacha_prose_data import MELACHA_POLISH, MISC_PROSE_POLISH
from _glossary_batch21 import BATCH21_ES, BATCH21_FR
from _glossary_batch22 import BATCH22_ES, BATCH22_FR
from _glossary_batch33 import BATCH33_ES, BATCH33_FR, BATCH33_FR_PREFIX
from _glossary_batch34 import BATCH34_ES, BATCH34_FR, BATCH34_RU
from _glossary_batch35 import BATCH35_ES, BATCH35_FR
from _glossary_batch36 import BATCH36_ES, BATCH36_FR
from _glossary_batch37 import BATCH37_ES, BATCH37_FR
from _glossary_batch38 import BATCH38_ES, BATCH38_FR
from _glossary_batch39 import BATCH39_ES, BATCH39_FR
from _glossary_batch40 import BATCH40_ES, BATCH40_FR
from _glossary_batch41 import BATCH41_ES, BATCH41_FR
from _glossary_batch42 import BATCH42_ES, BATCH42_FR
from _glossary_batch43 import BATCH43_ES, BATCH43_FR
from _glossary_batch44 import BATCH44_ES, BATCH44_FR
from _glossary_batch45 import BATCH45_ES, BATCH45_FR
from _fr_tu_explainers_data import PREFIX_FR_TU_EXPLAINERS
from _glossary_short_batch_data import GLOSSARY_SHORT_ES_FR, GLOSSARY_SHORT_PREFIX_ES_FR

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "data" / "translation-catalog" / "human" / "es_fr_prose_polish.json"

# Keys must match strings.json exactly — prefix lookup for long keys.
KEY_PREFIXES = [
    "Tefillin are two small black leather boxes",
    "Hamapil is the very last thing said before closing",
    "If you miss a prayer service (tefillah)",
    "Why Shnayim Mikra is worth the effort",
    "Parents are obligated in chinuch",
    "A sofer (scribe) is trained to write STaM",
    "A posek is a rabbi qualified to decide",
    "A parsha (parashah) is the weekly Torah portion",
    "A halachic hour (sha'ah zmanit) is one twelfth",
    "Tashlumin is making up a missed Amidah",
    "Tashlumin — Tashlumin is making up a missed Amidah",
    "Chinuch is training children to do mitzvot",
    "chinuch — Chinuch is training children",
    "Bar mitzvah is when a boy turns thirteen",
    "bar mitzvah — Bar mitzvah is when a boy",
    "bat mitzvah — Bat mitzvah is when a girl",
    "Melave Malka (מְלַוֶּה מַלְכָּה) means",
    "Simchas Yom Tov (שִׂמְחַת יוֹם טוֹב)",
    "mitzvah — At its core, a mitzvah is a command",
    "Besamim are fragrant spices smelled during Havdalah",
    "besamim — Besamim are fragrant spices",
    "A minhag is a binding community",
    "Minhag — A minhag is a binding",
    "Emunah is faith that G-d exists",
    "Emunah — Emunah is faith",
    "Hamotzi is the blessing over bread",
    "Hamotzi — Hamotzi is the blessing",
    "Kaddish sanctifies G-d's Name in Aramaic",
    "Kaddish — Kaddish sanctifies G-d's Name",
    "Retzei asks G-d to be pleased with our rest",
    "Retzei — Retzei asks G-d to be pleased",
    "A machzor is a prayer book for the Jewish festivals",
    "machzor — A machzor is a prayer book",
    "Amen affirms a bracha someone",
    "Amen — Amen affirms a bracha",
    "Chassidut — Chassidut (Chasidic philosophy)",
    "At its core, a mitzvah is a command",
    "A mezuzah is the klaf",
    "Bitachon is trust in G-d",
    "Brit milah (bris) is the covenant",
    "Chilul Hashem — Chilul Hashem is desecrating",
    "Dayeinu (\"it would have been enough\") is the Seder",
    "Dayeinu — Dayeinu (\"it would have been enough\")",
    "L'chatchila means the ideal way",
    "l'chatchila — L'chatchila means",
    "Oneg Shabbat is delighting in Shabbat",
    "Oneg Shabbat — Oneg Shabbat is delighting",
    "Pirsumei nisa means publicizing",
    "pirsumei nisa — Pirsumei nisa means",
    "Please put away your device and keep Shabbat",
    "Today is $holidayName (Yom Tov — a festival day). Please put away your device",
    "Please refrain from using phones, computers, or any electronics during Shabbat",
    "Shabbat is about to begin. Please finish what you are doing",
    "Gebrochts (\"broken\") is matzah",
    "Birchat Hamapil (Hamapil) is the blessing",
    "birchat hamapil — Birchat Hamapil",
    "Bentching is Birkat Hamazon — Grace After Meals",
    "Bentching — Bentching is Birkat Hamazon",
    "A kezayit is the olive-sized portion",
    "chiyuv — Chiyuv means obligated",
    "Borei pri hagafen is the blessing on wine",
    "Tu B'Shvat (15 Shevat) — New Year for Trees",
    "Ne'ilah (\"closing\") is the final prayer service",
    "Ne'ilah — Ne'ilah",
    "Kol Nidrei opens Yom Kippur eve",
    "Kol Nidrei — Kol Nidrei opens",
    "Erev Tisha B'Av (8 Av afternoon): stop learning Torah",
    "Afikoman — The Afikoman is matzah eaten at the end of the Seder",
    "The Afikoman is matzah eaten at the end of the Seder",
    "Seder — The Seder is the ordered Pesach night meal",
    "The Seder is the ordered Pesach night meal",
    "chiyuv — Chiyuv means obligated",
    "halachic hour — A halachic hour (sha'ah zmanit)",
    "Borei Pri HaGafen — Borei pri hagafen is the blessing on wine",
    "K'dei achilat pras is the halachic time limit",
    "k'dei achilat pras — K'dei achilat pras is the halachic time limit",
    "bedikat chametz — Bedikat chametz is the formal search",
    "Shiva is seven days of mourning at home",
    "shiva — Shiva is seven days of mourning",
    "A halachic hour (sha'ah zmanit) is one twelfth",
    "Master the art of loving rebuke",
    "Guard against false prophecy",
    "Discover the letter Tav",
    "Protect G-d's holy Name",
    "Geneivat da'at",
    "Kashrut is the Torah system of permitted food",
    "Speech is one of the most powerful forces in Jewish life",
    "Learn about Dosh — threshing",
    "Learn about the Melacha of Mocheik",
    "Learn about the Melacha of Oreg",
    "baal koreh — The baal koreh",
    "Aliyah means \"going up\"",
    "Trop (ta'amei hamikra) are the cantillation",
    "Purim Meshulash starts tonight in Jerusalem",
    "Purim Meshulash — Purim seudah on Sunday",
    "Purim Meshulash — matanot la'evyonim on Friday",
    "Explore the mystical letter Heh",
    "Al hamichya (Al HaMichya) is the after-blessing",
    "aliyah — Aliyah means",
    "Purim Meshulash — Megillah on Friday",
    "Purim Meshulash is the rare Jerusalem schedule",
    "Learn about Borer — the famous",
    "Shacharit is the morning service",
    "Shacharit — Shacharit is the morning service",
    "Taharat HaMishpacha (Family Purity Laws)",
    "Purim Meshulash — Purim Meshulash is the rare Jerusalem schedule",
    "Bal yera'eh (\"it shall not be seen\") is the Torah prohibition",
    "bal yera'eh — Bal yera'eh",
    "Learn about Losh — kneading",
    "Kiddush b'Makom Seudah means sanctifying",
    "Kiddush b'Makom Seudah — Kiddush b'Makom Seudah",
    "Mikra Megillah (hearing the Book of Esther)",
    "Borer (selecting) forbids sorting",
    "Borer — Borer (selecting)",
    "17 Tammuz — fast marking",
    "Plag HaMincha",
    "Kol Chamira",
    "The Three Weeks from 17 Tammuz to Tisha B'Av mourn",
    "Three Weeks — The Three Weeks from 17 Tammuz",
    "Sefirat HaOmer counts forty-nine days",
    "Sefirat HaOmer — Sefirat HaOmer counts",
    "Maariv is the evening service",
    "Maariv — Maariv is the evening service",
    "Kol Chamira is an Aramaic declaration",
    "Kol Chamira — Kol Chamira is an Aramaic",
    "Chitas (חיט\"ס) is Chabad's daily",
    "Chitas — Chitas (חיט\"ס) is Chabad's",
    "Motzei Yom Kippur meal — after nightfall",
    "Kiddush Levana (קִידּוּשׁ לְבָנָה — Sanctification of the Moon)",
    "Kiddush Levana — Kiddush Levana (קִידּוּשׁ לְבָנָה",
    "Hadlakat Nerot is lighting candles before",
    "Hadlakat Nerot — Hadlakat Nerot is lighting",
    "Lighting candles on Friday afternoon is one",
    "Learn about the mitzvah of Hadlakat Nerot",
    "Hagbah is lifting the open Torah",
    "Hagbah — Hagbah is lifting",
    "Kitniyot are legumes, rice, corn",
    "kitniyot — Kitniyot are legumes",
    "Megillah means scroll; on Purim",
    "Megillah — Megillah means scroll",
    "Selichot are penitential prayers",
    "Selichot — Selichot are penitential",
    "Tzitzit are fringes on four-cornered",
    "tzitzit — Tzitzit are fringes",
    "Kashering makes utensils fit",
    "Kashering — Kashering makes utensils",
    "egg matzah — matzah made with eggs",
    "matzah made with eggs and/or fruit juice",
    "The Torah commands Jewish men to wear fringes",
    "The Torah commands the Shema to be said",
    "Learn about the ultimate spiritual",
    "Shavuot marks Matan Torah",
    "Shavuot — Shavuot marks Matan Torah",
    "Yom HaZikaron (4 Iyar)",
    "Learn about Bishul",
    "Learn about Kotzer",
    "Learn about a surprising ruling: answering Amen",
    "Learn about proper bowing on Rosh Hashanah",
    "Chol HaMoed are the intermediate",
    "After using the bathroom, we wash our hands",
    "Building a sukkah (סֻכָּה) is a mitzvah",
    "Diaspora — The Diaspora (Galut) means",
    "The Diaspora (Galut) means Jewish life",
    "Eruv tavshilin is a symbolic meal",
    "Eruv tavshilin — Eruv tavshilin is",
    "Matana al menat lehachzir",
    "Matanot la'evyonim (מתנות לאביונים) helps",
    "Mishloach manot (משלוח מנות)",
    "Patur means exempt from a particular",
    "Mincha is the afternoon prayer",
    "Misheyakir (מִשֶּׁיַּכִּיר) is when",
    "Melacha is transformative labor",
    "Melacha — Melacha is transformative",
    "The oleh (one who goes up)",
    "oleh — The oleh (one who goes up)",
    'The Shema ("Hear O Israel")',
    'Shema — The Shema ("Hear',
    "Drinking wine on Chol HaMoed",
    "Many poskim and communities hold that women",
    "Ashkenazi Jews trace roots to Central",
    "Ashkenazi — Ashkenazi Jews trace roots",
    "Rosh Chodesh (ראש חודש) — the New Month",
    "Said immediately before sleep, the bedtime Shema",
    "Pesach begins in about a week",
    "After Yom Kippur ends at nightfall",
    "A series of short blessings said at the beginning",
    "Modeh Ani is the first words many Jews",
    "Chametz:\n• All chametz must be completely gone",
    "Chametz:\n• Biur was Friday morning",
    "Borei pri ha'etz is the blessing on fruit",
    "Borei Pri HaEtz — Borei pri ha'etz is",
    "A kezayit is the olive-sized portion in Jewish",
    "A revi'it (רביעית) is a standard unit",
    "Kiddush is the Torah-level commandment",
    "Candles are lit before sunset on Friday",
    "An eruv (especially eruv chatzerot",
    "Chatzos is halachic midnight",
    "halachic chatzos",
    "halachic chatzos — Chatzos is halachic midnight",
    "Ashkenaz custom: mourning from after",
    "Erev Pesach (14 Nissan) is the day",
    "Erev Tisha B'Av (8 Av afternoon)",
    "Arba Minim (ארבעה מינים) — the Four Species",
    "Standing (kima — קִימָה)",
    "A mikveh is a kosher ritual pool",
    "Bedieved describes halachic guidance",
    "Plag hamincha is one and a quarter",
    "Plag HaMincha — Plag hamincha is",
    "Rambam is Rabbi Moses Maimonides",
    "Rambam — Rambam is Rabbi Moses",
    "bedieved — Bedieved describes",
    "lechem mishneh — Lechem mishneh is",
    "The Three Weeks (בין המצרים) from 17 Tammuz",
    "Shloshim is thirty days of reduced",
    "shloshim — Shloshim is thirty days",
    "Tefillin are holy black leather boxes",
    "Tefillin — Tefillin are holy black leather",
    "bitachon — Bitachon is trust in G-d",
    "matana al menat lehachzir — Matana al menat lehachzir",
    "May it be Your will, Lord our God",
    "Tell us about yourself",
    "Why Torah learning matters for you",
    "The Amidah is the central, most important",
    'Amidah — The Amidah ("standing" prayer)',
    "The Shema is the central declaration of Jewish",
    "Daven (davening) means to pray",
    "daven — Daven (davening) means",
    "Nusach Ari is the prayer rite",
    "Nusach Ari — Nusach Ari is the prayer",
    "Chol HaMoed — Chol HaMoed are the intermediate",
    "This app is a learning companion, not a rabbi",
    "Torah learning bonds you with Hashem",
    'The word mitzvah (מִצְוָה) literally means',
    "Musaf is an additional Amidah",
    "Immediately after saying Modeh Ani, wash your hands",
    "Why learn at night — what the sages promise",
    "Aggadah is the non-legal storytelling",
    "aggadah — Aggadah is the non-legal",
    "Tumah is ritual impurity",
    "tumah — Tumah is ritual impurity",
    "Simchat Torah (23 Tishrei in the Diaspora)",
    "Shemini Atzeret (22 Tishrei in the Diaspora)",
    "Today is Erev Yom Kippur — a unique day",
    "Yom Kippur — Day of Atonement. Full 25-hour",
    "Who must fast:",
    "Before you open a book — remember what Torah study is",
    "Shemini Atzeret / Simchat Torah (22 Tishrei in Israel)",
    "The Torah commands Jewish men to wear fringes",
    "Yaaleh V'Yavo is a special prayer paragraph",
    "Purim Meshulash (פורים מְשֻׁלָּשׁ) — Jerusalem when Shushan",
    "Giving charity (tzedakah — צְדָקָה) is a constant",
    "Tomorrow is erev Purim — and this year is Purim Meshulash",
    "Purim Meshulash — mishloach manot on Sunday",
    "Yom Ha'atzmaut (5 Iyar) commemorates",
    "Sephardi custom (Shulchan Arukh O.C. 493",
    "Yom HaZikaron (4 Iyar) is Israel's national",
    "Pikuach nefesh (saving life) overrides",
    "The Days of Awe (Aseret Yemei Teshuvah) span",
    "Days of Awe — The Days of Awe",
    "Aveilut is mourning practice",
    "Elul is the month before Rosh Hashana",
    "During Sefirat HaOmer we keep customs",
    "Sukkot is seven days dwelling",
    "Psalm 27 (לְדָוִד ה' אוֹרִי",
]

strings = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
key_by_prefix = {}
for p in KEY_PREFIXES:
    for k in strings:
        if k.startswith(p):
            key_by_prefix[p] = k
            break
    else:
        raise SystemExit(f"missing key for prefix: {p!r}")

ES: dict[str, str] = {}
FR: dict[str, str] = {}

# --- Tefillin (Argos corruption: phylacteries / תтелителиваный) ---
k = key_by_prefix["Tefillin are two small black leather boxes"]
ES[k] = (
    "Las tefilín son dos pequeñas cajas de cuero negro con pasajes de la Torá, "
    "que se usan en la oración de la mañana. Es una de las mitzvot diarias más importantes para los hombres judíos.\n\n"
    "Qué son:\n"
    "Las tefilín (תְּפִלִּין) son dos cajas de cuero negro, cada una con cuatro pasajes de la Torá escritos a mano en pergamino.\n"
    "• Shel yad (de la mano): se coloca en el brazo, cerca del corazón, con la correa enrollada siete veces en el antebrazo y luego en la mano según el patrón halájico.\n"
    "• Shel rosh (de la cabeza): sobre la cabeza, centrada, por encima de la línea del cabello, con las correas colgando al frente.\n\n"
    "Fuente en la Torá:\n"
    "«Átalos como señal en tu brazo… entre tus ojos» (Deuteronomio 6:8), leído en el Shemá.\n\n"
    "Por qué las usamos:\n"
    "Unen mente (caja de la cabeza), corazón (caja del brazo) y acción (mano envuelta) con D-s en la oración — señal del pacto con el pueblo judío.\n\n"
    "Cuándo ponerlas:\n"
    "La berajá del talit y las tefilín se dice en misheyakir — cuando hay luz suficiente para reconocer a alguien a distancia. "
    "La Mishná Berurá recomienda esperar a misheyakir antes del Shemá y la Amidá. "
    "La Guemará (Berajot 14b) compara decir Shemá sin talit y tefilín con falso testimonio.\n\n"
    "Si debes salir muy temprano al trabajo:\n"
    "• Ideal: poner talit y tefilín después de misheyakir; comenzar la Amidá al amanecer.\n"
    "• Aceptable: Amidá antes del amanecer, tras misheyakir.\n"
    "• Solo si no hay otra opción: Igrot Moshe (O.J. 4:6) permite ponerlas tras alot hashajar sin berajá y recitarla después de misheyakir — consulta a tu rav.\n\n"
    "La app desbloquea este ítem en misheyakir según tus zmanim locales.\n\n"
    "Cuándo NO se usan:\n"
    "• Shabat y Yom Tov\n"
    "• Jol hamoed de Pesaj y Sucot — según el minhag de tu comunidad; pregunta a tu rav.\n\n"
    "Para empezar:\n"
    "Las tefilín kosher son una inversión importante (200–2000 USD o más). No compres baratas — muchas no son válidas. "
    "Pide a tu rav que te ayude a conseguir un par certificado y a aprender a ponértelas."
)
FR[k] = (
    "Les téfilines sont deux petites boîtes de cuir noir contenant des passages de la Torah, "
    "portées pendant la prière du matin. C'est l'une des mitsvot quotidiennes les plus importantes pour les hommes juifs.\n\n"
    "Ce que c'est :\n"
    "Les téfilines (תְּפִלִּין) sont deux boîtes de cuir noir, chacune avec quatre passages de la Torah écrits à la main sur parchemin.\n"
    "• Shel yad (de la main) : sur le bras près du cœur, avec le lien enroulé sept fois autour de l'avant-bras puis sur la main.\n"
    "• Shel rosh (de la tête) : sur le sommet de la tête, centrée au-dessus de la ligne des cheveux, les liens pendant devant.\n\n"
    "Source torahique :\n"
    "« Lie-les comme un signe sur ton bras… entre tes yeux » (Deutéronome 6:8), lu dans le Shema.\n\n"
    "Pourquoi on les porte :\n"
    "Elles unissent l'esprit (boîte de la tête), le cœur (boîte du bras) et l'action (main enroulée) à D.ieu pendant la prière.\n\n"
    "Quand les mettre :\n"
    "La bénédiction du talit et des téfilines se dit à misheyakir — quand il y a assez de lumière pour reconnaître quelqu'un de loin. "
    "La Michna Beroura recommande d'attendre misheyakir avant le Shema et l'Amida.\n\n"
    "Si tu dois partir très tôt au travail :\n"
    "• Idéal : après misheyakir ; Amida au lever du jour.\n"
    "• Sinon : Igrot Moshe (O.H. 4:6) permet un cas particulier avant misheyakir — demande à ton rav.\n\n"
    "L'application débloque cet élément à misheyakir selon tes zmanim locaux.\n\n"
    "Quand on ne les porte pas :\n"
    "• Chabbat et Yom Tov\n"
    "• Hol hamoed de Pessah et Souccot — selon le minhag ; demande à ton rav.\n\n"
    "Pour commencer :\n"
    "Des téfilines casher sont un investissement important. N'achète pas bon marché — beaucoup ne sont pas valides. "
    "Demande à ton rav de t'aider à en acquérir et à apprendre à les poser."
)

# --- Hamapil ES (broken parenthesis / truncated Hebrew gloss) ---
k = key_by_prefix["Hamapil is the very last thing said before closing"]
ES[k] = (
    "Hamapil es lo último que se dice antes de cerrar los ojos para dormir.\n\n"
    "Qué es:\n"
    "Hamapil (הַמַּפִּיל — «quien hace descansar») es una berajá que agradece el don del sueño y pide protección durante la noche. "
    "Es única porque se dice antes de la acción (dormir), no durante ella.\n\n"
    "La berajá pide que:\n"
    "• Nuestros ojos puedan dormir\n"
    "• Despertemos por la mañana en paz\n"
    "• No seamos perturbados por malos sueños\n"
    "• El sueño nos renueve y sane\n\n"
    "Por qué el sueño requiere una berajá:\n"
    "El Talmud enseña que el sueño es «una sesenta parte de la muerte» — una partida parcial del alma cada noche. "
    "Hamapil reconoce esa transición y expresa confianza total en D-s.\n\n"
    "Cómo decirla:\n"
    "Después del Shemá al acostarse, inmediatamente antes de cerrar los ojos.\n\n"
    "Tras Hamapil (Rama, Shulján Aruj O.J. 239:1): no comer, beber ni hablar hasta quedarse dormido. "
    "Si surge una necesidad urgente (un niño, el baño, seguridad), se puede hablar — no anula la berajá ni hay que repetirla.\n\n"
    "Si no estás seguro de dormirte:\n"
    "• Ashkenazim: se dice siempre (Mishná Berurá 239:6) — es alabanza por el don del sueño en general.\n"
    "• Sefardíes: si dudas de dormir pronto, muchos omiten el Shem u-Maljut o lo dicen como meditación — consulta a tu rav."
)
FR[k] = (
    "Hamapil est la dernière chose dite avant de fermer les yeux.\n\n"
    "Qu'est-ce que c'est :\n"
    "Hamapil (הַמַּפִּיל — « qui fait reposer ») remercie D. pour le don du sommeil et demande protection pour la nuit — "
    "dit avant l'action (dormir), pas pendant.\n\n"
    "La bénédiction demande :\n"
    "• que nos yeux puissent dormir\n• un réveil paisible\n• pas de mauvais rêves\n• un sommeil qui restaure\n\n"
    "Le Talmud : le sommeil est « un soixantième de la mort » — départ partiel de l'âme. Hamapil exprime confiance totale en D.\n\n"
    "Comment :\n"
    "Après le Shema du coucher, juste avant de fermer les yeux.\n\n"
    "Après Hamapil (Rama, O.H. 239:1) : ne mange pas, ne bois pas, ne parle pas jusqu'à t'endormir. "
    "Besoin urgent (enfant, toilettes, sécurité) : on peut parler — pas besoin de répéter.\n\n"
    "Si tu n'es pas sûr de t'endormir :\n"
    "• Ashkénazes : on le dit quand même (Michna Beroura 239:6)\n"
    "• Sefarades : si doute, beaucoup omettent le Shem u-Malchut — demande à ton rav."
)

# --- Tashlumin ES (corrupted «nuestros ּ húbitos») ---
k = key_by_prefix["If you miss a prayer service (tefillah)"]
ES[k] = (
    "Si te pierdes un servicio de oración (tefilá), existe un mecanismo rabínico para compensarlo parcialmente — "
    "el tashlumin (תַּשְׁלוּמִין — «completar»). Las reglas son estrictas y la ventana es estrecha.\n\n"
    "Fuente: Talmud Bavli, Berajot 26a. Codificado en Shulján Aruj, Oraj Jaim 108:1.\n\n"
    "Regla central — solo en la oración inmediatamente siguiente:\n"
    "• Shajarit perdida → en Minjá (no en Arvit).\n"
    "• Minjá perdida → en Arvit.\n"
    "• Arvit perdida → en la Shajarit del día siguiente.\n\n"
    "Orden obligatorio:\n"
    "1. Primero la Amidá del servicio actual.\n"
    "2. Pausa breve (cuatro amot).\n"
    "3. Segunda Amidá como tashlumin de la oración perdida.\n\n"
    "Si invertiste el orden por error, la primera Amidá cuenta para la obligación actual y la segunda como tashlumin — no hace falta una tercera.\n\n"
    "Si perdiste la ventana — Tefilat Nedavá:\n"
    "Puedes rezar una Amidá voluntaria con estipulación mental y una petición personal pequeña (Shulján Aruj 107:1).\n\n"
    "Notas:\n"
    "• Tashlumin solo si la oración se perdió sin intención.\n"
    "• Shabat, Rosh Jodesh o Yom Tov perdidos se compensan en Minjá, no en Musaf.\n"
    "• En duda, pregunta a tu rav."
)

# --- Shnayim Mikra FR (spacing disaster in title) ---
k = key_by_prefix["Why Shnayim Mikra is worth the effort"]
FR[k] = (
    "Pourquoi Shnayim Mikra vaut l'effort — enseignements de nos sages :\n\n"
    "La paracha hebdomadaire n'est pas qu'un repère du calendrier. C'est ta part personnelle dans la Torah qui soutient le peuple juif. "
    "Les sages lient la lecture publique de la Torah à la survie du monde (Chabbat 119b). "
    "Quand tu étudies la même paracha que la communauté entendra le Chabbat, tu rejoins une chaîne qui remonte au Sinaï — "
    "et la Chekhina demeure là où passent les paroles de Torah (Avot 3:2).\n\n"
    "L'étude de la Torah protège l'étudiant ; elle fait partie des trois gardiens de la vie (Sotah 21a). "
    "Shnayim Mikra est un rythme hebdomadaire gérable : un peu chaque jour construit un lien avec Hachem qu'aucun podcast ne remplace.\n\n"
    "Chaque semaine, la communauté lit une section désignée de la Torah à la synagogue le matin du Chabbat. "
    "Chaque individu doit personnellement étudier cette même section pendant la semaine.\n\n"
    "De quoi s'agit-il :\n"
    "Shnayim Mikra v'Ejad Targoum (שְׁנַיִם מִקְרָא וְאֶחָד תַּרְגּוּם) — lire chaque verset de la paracha :\n"
    "• Deux fois en hébreu\n"
    "• Une fois en traduction araméenne (Targoum Onkelos) ou le commentaire de Rachi (Shulhan Aroukh et Michna Beroura O.H. 285:2)\n\n"
    "Quand le faire (O.H. 285:4) :\n"
    "• Idéal (le'hathila) : avant le repas du Chabbat, idéalement avant la lecture publique.\n"
    "• Sinon : après le repas, avant Min'ha.\n"
    "• En retard : certains permettent jusqu'au mercredi suivant, d'autres jusqu'à Sim'hat Torah.\n\n"
    "Répartis la paracha sur la semaine. Manquer la fenêtre idéale ne fait pas perdre la mitsva tant que les délais de repli restent valides."
)

# --- Chinuch parents — full native rewrites (fixes mentón/salchicha/English leaks) ---
k = key_by_prefix["Parents are obligated in chinuch"]
ES[k] = (
    "Los padres están obligados al jinuj (חִנּוּךְ — educar a los hijos en las mitzvot). "
    "La Torá ordena: «Enséñales a tus hijos» (Devarim 11:19) — para que aprendan Torá y observen mitzvot "
    "(Peninei Halakha, Leyes de los niños 24:1). Antes del bar/bat mitzvá, el niño no está personalmente obligado "
    "por la Torá en las mitzvot, pero los padres sí tienen obligación toráica de educarlo (Devarim 11:19; Rambam, Chinuj 1:1).\n\n"
    "Principios básicos (de los poskim):\n"
    "• «Educa al joven según su camino» (Mishlei 22:6) — cada niño tiene su ritmo.\n"
    "• Enseña una mitzvá positiva cuando comprenda y pueda cumplirla — kfi daato (Peninei Halakha 24:1).\n"
    "• Enseña los detalles, no solo la idea general — p. ej. un etrog kosher al practicar las Cuatro Especies.\n"
    "• «Lo exterior despierta lo interior» — primero hábito y acción; la comprensión crece con la edad.\n\n"
    "Hoja de ruta clásica — Pirkei Avot 5:21 (Yehudá ben Teima):\n"
    "• 5 años — mikrá (Escritura)\n"
    "• 10 años — Mishná\n"
    "• 13 años — mitzvot (varones a los 13, mujeres a los 12)\n"
    "• 15 años — Guemará / estudio más profundo\n"
    "No es una lista rígida — tu rav y la escuela guían los detalles.\n\n"
    "Etapas según las fuentes:\n"
    "• Al empezar a hablar — «Torá tzivá lanu Moshe» y primer verso del Shemá (Sukká 42a).\n"
    "• ~3 años — alef-bet (Rema YD 245:8); acostumbrar a berajot y tefilá (Bava Batrá 21a; Shulján Aruj YD 245:5). "
    "Varones: muchas comunidades empiezan kipá y tzitzit. Escuchar Kidush y Havdalá cuando comprendan Shabat.\n"
    "• Antes de los 3 — sin obligación de impedir actos prohibidos (Peninei Halakha 24:2).\n"
    "• ~3–4 años — cuando distingue permitido/prohibido: evitar comida no kosher, encender luces en Shabat, etc.\n"
    "• 6–7 años — edad principal de jinuj para mitzvot positivas, berajot y oración (Peninei Halakha 24:1).\n"
    "• Varía por mitzvá: shofar/sucá ~5 años; luto en Tisha BeAv ~7; tzitzit cuando recita la berajá; "
    "tefilín poco antes del bar mitzvá (Sukká 42a; Mishná Berurá 343:3).\n"
    "• Ayuno de niños en Yom Kipur — edades rabínicas específicas (Peninei Halakha, cap. 9).\n\n"
    "Tono: las mitzvot deben ser alegría, no carga; corrige con amor. Ambos padres comparten el jinuj. "
    "Pregunta a tu rav según edad, nusaj y minhag.\n\n"
    "Marca este ítem cuando hoy invertiste activamente en el jinuj de tus hijos — enseñando, modelando o guiando una mitzvá al nivel que la halajá espera para su etapa."
)
FR[k] = (
    "Les parents ont l'obligation du 'hinoukh (חִנּוּךְ — éducation des enfants aux mitsvot). "
    "La Torah ordonne : « Enseignez-les à vos enfants » (Devarim 11:19) — pour qu'ils apprennent la Torah et observent les mitsvot "
    "(Peninei Halakha, Lois des enfants 24:1). Avant le bar/bat mitsvah, l'enfant n'est pas personnellement tenu par la Torah aux mitsvot, "
    "mais les parents ont l'obligation toranique de les former (Devarim 11:19 ; Rambam, Chinoukh 1:1).\n\n"
    "Principes fondamentaux (des poskim) :\n"
    "• « Éduque le jeune selon sa voie » (Mishlei 22:6) — chaque enfant a son rythme.\n"
    "• Former à une mitsva positive quand il comprend et peut l'observer — kfi daato (Peninei Halakha 24:1).\n"
    "• Enseigner les détails, pas seulement l'idée — ex. un étrog casher pour les Quatre Espèces.\n"
    "• « L'extérieur éveille l'intérieur » — l'habitude et l'action d'abord ; la compréhension vient avec l'âge.\n\n"
    "Feuille de route classique — Pirkei Avot 5:21 (Yehouda ben Teima) :\n"
    "• 5 ans — mikra (Écriture)\n"
    "• 10 ans — Mishna\n"
    "• 13 ans — mitsvot (garçons à 13, filles à 12)\n"
    "• 15 ans — Guemara / étude approfondie\n"
    "Structure traditionnelle, pas une checklist rigide — votre rav et l'école guident les détails.\n\n"
    "Étapes selon les sources :\n"
    "• Dès qu'il parle — « Torah tziva lanu Moshe » et premier verset du Shema (Soukka 42a).\n"
    "• ~3 ans — aleph-bet (Rema YD 245:8) ; habituer aux berakhot et à la tefilla (Bava Batra 21a ; Choulhan Aroukh YD 245:5). "
    "Garçons : kippa et tsitsit dans beaucoup de communautés. Écouter Kidouch et Havdala quand il comprend Chabbat.\n"
    "• Avant 3 ans — pas d'obligation d'empêcher les actes interdits (Peninei Halakha 24:2).\n"
    "• ~3–4 ans — distinguer permis/interdit : nourriture non casher, lumières le Chabbat, etc.\n"
    "• 6–7 ans — âge principal du 'hinoukh pour mitsvot positives, berakhot et prière (Peninei Halakha 24:1).\n"
    "• Selon la mitsva : shofar/soucca ~5 ans ; deuil à Tisha BeAv ~7 ; tsitsit quand il récite la berakha ; "
    "téfilines peu avant le bar mitsvah (Soukka 42a ; Michna Beroura 343:3).\n"
    "• Jeûne des enfants à Yom Kippour — âges rabbiniques (Peninei Halakha, ch. 9).\n\n"
    "Ton : les mitsvot doivent être une joie, pas un fardeau ; corrigez avec amour. Les deux parents partagent le 'hinoukh. "
    "Demande à ton rav selon l'âge, le nusach et le minhag.\n\n"
    "Coche cet élément quand tu as investi aujourd'hui dans le 'hinoukh de tes enfants — enseigner, montrer l'exemple ou guider une mitsva au niveau attendu par la halakha."
)

# --- Glossary ES/FR ---
k = key_by_prefix["A sofer (scribe) is trained to write STaM"]
ES[k] = (
    "Un sofer (escriba) está formado para escribir STaM — rollos de Torá, tefilín, mezuzot y megilot — "
    "a mano con tinta especial sobre pergamino. Las letras deben formarse con exactitud; un error puede invalidar el rollo. "
    "Los soferim también revisan klafim existentes. Nunca compres tefilín o mezuzot sin certificación fiable."
)
FR[k] = (
    "Un sofer (scribe) est formé pour écrire le STaM — rouleaux de Torah, téfilines, mezouzot et mégillot — "
    "à la main avec une encre spéciale sur parchemin. Les lettres doivent être formées avec exactitude ; une erreur peut invalider le rouleau. "
    "Les soferim vérifient aussi les klafim existants. N'achète jamais de téfilines ou mezouzot sans certification fiable."
)

k = key_by_prefix["A posek is a rabbi qualified to decide"]
ES[k] = (
    "Un posek es un rabino capacitado para decidir preguntas halájicas difíciles y escribir decisiones autorizadas. "
    "Tu rav puede consultar a poskim en casos nuevos. El término difiere del maguid o maestro que inspira pero no «poskea». "
    "Grandes poskim incluyen las figuras detrás de la Mishná Berurá, Igrot Moshe y obras halájicas contemporáneas que sigue tu comunidad."
)
FR[k] = (
    "Un posek est un rabbin qualifié pour trancher des questions halakhiques difficiles et rédiger des décisions faisant autorité. "
    "Votre rav peut consulter des poskim pour des cas inédits. Le terme diffère du maggid ou enseignant qui inspire sans « posker ». "
    "Parmi les grands poskim : les auteurs de la Michna Beroura, Igrot Moshe et les ouvrages halakhiques que suit votre communauté."
)

k = key_by_prefix["A parsha (parashah) is the weekly Torah portion"]
ES[k] = (
    "Una parashá (parashá) es la porción semanal de la Torá leída en la sinagoga el Shabat por la mañana. "
    "La Torá se divide en 54 porciones para completar los cinco libros cada año (a veces combinadas según el calendario). "
    "Shnayim Mikra significa estudiar la misma parashá durante la semana antes de la lectura pública. "
    "El nombre aparece como «Parashat Bereshit», «Parashat Noaj», etc."
)
FR[k] = (
    "Une paracha (parashah) est la portion hebdomadaire de la Torah lue à la synagogue le matin de Chabbat. "
    "La Torah est divisée en 54 portions pour achever les cinq livres chaque année (parfois combinées selon le calendrier). "
    "Shnayim Mikra signifie étudier la même paracha pendant la semaine avant la lecture publique. "
    "On dit « Parashat Berechit », « Parashat Noah », etc."
)

k = key_by_prefix["A halachic hour (sha'ah zmanit) is one twelfth"]
ES[k] = (
    "Una hora halájica (sha'ah zmanit) es una duodécima parte del período de luz desde el alba hasta el anochecer — "
    "más larga en verano y más corta en invierno, distinta de 60 minutos de reloj. "
    "Plazos como el Shemá de la mañana, la venta de jametz y plag haminjá usan estas horas. "
    "Las apps y calendarios judíos las convierten para tu ubicación."
)
FR[k] = (
    "Une heure halakhique (sha'ah zmanit) est un douzième de la période de jour entre l'aube et le crépuscule — "
    "plus longue en été, plus courte en hiver, contrairement à 60 minutes d'horloge. "
    "Les délais comme le Shema du matin, la vente de hamets et plag hamincha utilisent ces heures. "
    "Les applications et calendriers juifs les calculent pour votre lieu."
)

k = key_by_prefix["Tashlumin is making up a missed Amidah"]
ES[k] = (
    "El tashlumin compensa una Amidá perdida en el servicio inmediatamente siguiente: "
    "Shajarit perdida en Minjá, Minjá en Arvit, Arvit en la Shajarit del día siguiente. "
    "Reza primero la Amidá actual, pausa, luego la de compensación. "
    "Si dijiste la compensación primero por error, esa cuenta como la obligación actual y la segunda como tashlumin — "
    "no hace falta una tercera (Mishná Berurá 108:2). Solo si la oración se perdió sin intención. "
    "Shajarit festiva perdida se compensa en Minjá, no en Musaf (Shulján Aruj O.J. 108:9)."
)
FR[k] = (
    "Le tashloumin rattrape une Amida manquée au service immédiatement suivant : "
    "Shaharit manquée à Min'ha, Min'ha à Arvit, Arvit à la Shaharit du lendemain. "
    "Priez d'abord l'Amida du moment, pause, puis le rattrapage. "
    "Si vous avez dit le rattrapage en premier par erreur, il compte pour l'obligation actuelle et le second pour tashloumin — "
    "pas besoin d'une troisième (Michna Beroura 108:2). Uniquement si la prière a été manquée sans intention. "
    "Shaharit de fête manquée se rattrape à Min'ha, pas à Moussaf (Choulhan Aroukh O.H. 108:9)."
)

k = key_by_prefix["Tashlumin — Tashlumin is making up a missed Amidah"]
ES[k] = (
    "Tashlumin — el tashlumin compensa una Amidá perdida en el servicio inmediatamente siguiente: "
    "Shajarit perdida en Minjá, Minjá en Arvit, Arvit en la Shajarit del día siguiente. "
    "Reza primero la Amidá actual, pausa, luego la de compensación. "
    "Si dices la compensación primero por error, esa cuenta como la obligación actual y la segunda como tashlumin — "
    "no hace falta una tercera (Mishná Berurá 108:2). Solo si la oración se perdió sin intención o por fuerza mayor. "
    "Shajarit festiva perdida se compensa en Minjá, no en Musaf (Shulján Aruj O.J. 108:9)."
)
FR[k] = (
    "Tashloumin — le tashloumin rattrape une Amida manquée au service immédiatement suivant : "
    "Shaharit manquée à Min'ha, Min'ha à Arvit, Arvit à la Shaharit du lendemain. "
    "Priez d'abord l'Amida du moment, faites une pause, puis le rattrapage. "
    "Si vous dites le rattrapage en premier par erreur, cette Amida compte pour l'obligation actuelle et la seconde pour tashloumin — "
    "pas besoin d'une troisième (Michna Beroura 108:2). Uniquement si la prière a été manquée sans intention ou par empêchement inévitable. "
    "Shaharit de fête manquée se rattrape à Min'ha, pas à Moussaf (Choulhan Aroukh O.H. 108:9)."
)

# --- Chinuch glossary (Argos: entrenando / tímido / Chinch) ---
k = key_by_prefix["Chinuch is training children to do mitzvot"]
ES[k] = (
    "El jinuj (חִנּוּךְ) es educar a los hijos en las mitzvot antes del bar o bat mitzvá — cuando la obligación aún no es personal. "
    "Los padres enseñan gradualmente: berajot, tzitzit, tzedaá, velas de Shabat (según minhag) y comportamiento respetuoso en la sinagoga. "
    "«Educa al joven según su camino» (Mishlei) significa adaptar el ritmo al carácter del niño. El jinuj crea hábito; la comprensión crece con la edad."
)
FR[k] = (
    "Le 'hinoukh (חִנּוּךְ) forme les enfants aux mitsvot avant le bar ou bat mitsvah — quand l'obligation n'est pas encore personnelle. "
    "Les parents enseignent progressivement : berakhot, tsitsit, tsedaka, bougies de Chabbat (selon le minhag) et comportement respectueux à la synagogue. "
    "« Éduque le jeune selon sa voie » (Mishlei) signifie adapter le rythme au tempérament de l'enfant. Le 'hinoukh crée l'habitude ; la compréhension s'approfondit avec l'âge."
)
k = key_by_prefix["chinuch — Chinuch is training children"]
ES[k] = (
    "jinuj — El jinuj educa a los hijos en las mitzvot antes del bar o bat mitzvá. "
    "Enseñanza gradual de berajot, tzitzit, tzedaá, velas de Shabat y respeto en la sinagoga. "
    "«Educa al joven según su camino» (Mishlei). El jinuj crea hábito; la comprensión crece con la edad."
)
FR[k] = (
    "'hinoukh — Le 'hinoukh forme les enfants aux mitsvot avant le bar ou bat mitsvah. "
    "Berakhot, tsitsit, tsedaka, bougies de Chabbat et respect à la synagogue, progressivement. "
    "« Éduque le jeune selon sa voie » (Mishlei). L'habitude d'abord ; la compréhension vient avec l'âge."
)

# --- Bar mitzvah FR (Argos: Barre mitzvah) ---
k = key_by_prefix["Bar mitzvah is when a boy turns thirteen"]
ES[k] = (
    "El bar mitzvá es cuando un niño cumple trece años y queda obligado en mitzvot — tefilín, ayuno, conteo del Omer y toda la ley moral. "
    "La fiesta es alegría de seudat mitzvá, pero la esencia es responsabilidad. "
    "El jinuj de los padres antes de los trece lo prepara. La aliyah a la Torá suele marcar el día en la sinagoga."
)
FR[k] = (
    "Le bar mitsvah, c'est quand un garçon atteint treize ans et devient obligé par les mitsvot — téfilines, jeûne, compte de l'Omer et toute la loi morale. "
    "La fête est une joie de seudat mitsva, mais l'essence est la responsabilité. Le 'hinoukh des parents avant treize ans le prépare. "
    "L'alyah à la Torah marque souvent ce jour à la synagogue."
)
k = key_by_prefix["bar mitzvah — Bar mitzvah is when a boy"]
ES[k] = (
    "bar mitzvá — cuando un niño cumple trece años queda obligado en mitzvot: tefilín, ayuno, conteo del Omer y la ley moral completa. "
    "La celebración es alegría de seudat mitzvá, pero la esencia es responsabilidad. "
    "El jinuj de los padres antes de los trece lo prepara. La aliyah a la Torá suele marcar el día en la sinagoga."
)
FR[k] = (
    "bar mitsvah — Quand un garçon atteint treize ans et devient obligé par les mitsvot : téfilines, jeûne, Omer et loi morale. "
    "Fête de seudat mitsva ; essence : responsabilité. Le 'hinoukh parental le prépare. Alyah à la Torah souvent ce jour-là."
)
k = key_by_prefix["bat mitzvah — Bat mitzvah is when a girl"]
ES[k] = (
    "bat mitzvá — cuando una niña cumple doce años y un día queda obligada en mitzvot no ligadas al tiempo del Templo — "
    "edad estándar de obligación en la halajá tradicional. "
    "Las costumbres de celebración varían: discursos, proyectos de estudio, comida familiar. "
    "Las mitzvot de la mujer incluyen velas de Shabat, kashrut, tzedaá y estudio de Torá — los detalles siguen familia y rav."
)
FR[k] = (
    "bat mitsvah — Quand une fille atteint douze ans et un jour, elle devient obligée par les mitsvot non liées au temps du Temple. "
    "Coutumes de fête variables. Mitsvot des femmes : bougies de Chabbat, cacherout, tsedaka, étude — selon famille et rav."
)

# --- Bentching / kezayit / chiyuv glossary ---
k = key_by_prefix["Bentching is Birkat Hamazon — Grace After Meals"]
ES[k] = (
    "Birkat ha-Mazón — berajá después de las comidas con al menos un kezayit de pan en una misma comida. "
    "Agradecemos a D-s por la comida y la Tierra. En Shabat y festividades, el Salmo 126 precede la segunda berajá. "
    "Yaale ve-Yavo en Rosh Jodesh y hag. Zimun cuando tres o más hombres comieron pan juntos según minhag."
)
FR[k] = (
    "Birkat ha-Mazon — bénédiction après les repas avec au moins un kezayit de pain au cours du même repas. "
    "Remerciements à D.ieu pour la nourriture et la Terre. Chabbat et fêtes : Psaume 126 avant la seconde bénédiction. "
    "Yaale ve-Yavo à Rosh 'Hodesh et aux fêtes. Zimoun quand trois hommes ou plus ont mangé du pain selon le minhag."
)
k = key_by_prefix["Bentching — Bentching is Birkat Hamazon"]
ES[k] = (
    "Birkat ha-Mazón — tras un kezayit de pan; en Shabat y festividades el Salmo 126; Yaale ve-Yavo en Rosh Jodesh y hag; zimun según minhag."
)
FR[k] = (
    "Birkat ha-Mazon — après un kezayit de pain ; Psaume 126 à Chabbat et fêtes ; Yaale ve-Yavo à Rosh 'Hodesh ; zimoun selon minhag."
)
k = key_by_prefix["A kezayit is the olive-sized portion"]
ES[k] = (
    "Un kezayit es la porción del tamaño de una aceituna en la halajá — para medir cuánto pan, matzá o alimentos activan una berajá aharona o una obligación (p. ej. matzá en el Seder)."
)
FR[k] = (
    "Un kezayit est la portion « de la taille d'une olive » en halakha — pour mesurer pain, matsa ou aliments déclenchant une bénédiction finale ou une obligation (ex. matsa au Seder)."
)
k = key_by_prefix["chiyuv — Chiyuv means obligated"]
ES[k] = (
    "jiyuv — Jiyuv significa «obligado»: la mitzvá te aplica y debes cumplirla. El bar mitzvá crea jiyuv para las mitzvot del niño; enfermedad o peligro pueden suspenderla temporalmente. Si dudas entre chayav o patur — consulta a tu rav."
)
FR[k] = (
    "'hiyouv — 'hiyouv signifie « obligé » : la mitsva te concerne et tu dois l'accomplir. Le bar mitsvah crée 'hiyouv pour les mitsvot du garçon ; maladie ou danger peuvent suspendre temporairement l'obligation. En cas de doute chayav ou patour — demande à ton rav."
)

# --- Tu B'Shvat ES/FR ---
k = key_by_prefix["Tu B'Shvat (15 Shevat) — New Year for Trees"]
ES[k] = (
    "Tu bi-Shvat (15 Shevat) — Año Nuevo de los Árboles — día para apreciar los frutos de D-s y la Tierra de Israel.\n\n"
    "Liturgia: no se dice Tachanun hoy ni en la Minjá de ayer (14 Shevat).\n\n"
    "Costumbres: comer frutas — especialmente shivat ha-minim; berajot con cuidado; Shehejiyanu solo en fruta estacional nueva; "
    "algunos hacen seder con cuatro copas de vino; no hay ayuno; trabajo permitido; no es Yom Tov.\n\n"
    "Enfoque espiritual: gratitud, conexión con Eretz Israel y crecimiento."
)
FR[k] = (
    "Tou BiChvat (15 Chevat) — Nouvel An des Arbres — jour pour apprécier les fruits de D.ieu et la Terre d'Israël.\n\n"
    "Liturgie : pas de Tahanoun aujourd'hui ni à la Min'ha d'hier (14 Chevat).\n\n"
    "Coutumes : fruits — surtout shivat ha-minim ; berakhot avec soin ; Shehe'hyanou seulement sur fruit saisonnier nouveau ; "
    "certains font un seder à quatre coupes ; pas de jeûne ; travail permis ; ce n'est pas Yom Tov.\n\n"
    "Focus spirituel : gratitude, lien à Eretz Israël et croissance."
)

for prefix, fr_text in [
    (
        'Ne\'ilah ("closing") is the final prayer service',
        "Neïla (« fermeture ») est le dernier office de Yom Kippour, quand les portes du repentir se ferment. "
        "L'arche reste souvent ouverte ; la communauté supplie intensément. Beaucoup disent ensemble le Shema Israël à la fin. "
        "Après Neïla et la nuit (tzeit), on prie Arvit, fait Havdala et rompt le jeûne. C'est le sommet spirituel des Dix Jours de Téshouva.",
    ),
    (
        "Ne'ilah — Ne'ilah",
        "Neïla (« fermeture ») — dernier office de Yom Kippour quand les portes du repentir se ferment. "
        "Après Neïla et tzeit : Arvit, Havdala, rupture du jeûne. Sommet des Aseret Yemei Teshuvah.",
    ),
    (
        "Kol Nidrei opens Yom Kippur eve",
        "Kol Nidrei ouvre la veille de Yom Kippour — annulation de vœux pour aborder la journée avec une table rase vis-à-vis des promesses envers D.ieu. "
        "La mélodie est célèbre, mais le texte juridique est sérieux. N'annule pas les dettes envers autrui — seulement certains vœux envers D.ieu selon des formules précises. "
        "Arrivez tôt ; la synagogue se remplit vite.",
    ),
    (
        "Kol Nidrei — Kol Nidrei opens",
        "Kol Nidrei — ouvre la veille de Yom Kippour en annulant des vœux pour une table rase envers D.ieu. "
        "Ne concerne pas les dettes envers autrui. Arrivez tôt.",
    ),
    (
        "Erev Tisha B'Av (8 Av afternoon): stop learning Torah",
        "Érev Tisha BeAv (8 Av après-midi) : cesser d'étudier la Torah sauf sujets tristes ; prendre le repas final (seudah hamafseket) avant le jeûne. "
        "Talit et téfilines au Shaharit d'Érev Tisha BeAv — l'interdiction s'applique le jour de Tisha BeAv lui-même.\n\n"
        "Tisha BeAv (9 Av) : jeûne complet de 25 heures ; kinot à Shaharit sans talit ni téfilines ; "
        "on les reprend à Min'ha après 'hatzot halakhique (votre appli zmanim). S'asseoir sur des tabourets bas jusqu'à 'hatzot le 9 Av.\n\n"
        "Chabbat Hazon (le Chabbat avant le 9 Av) : Chabbat normal — viande et vin permis.",
    ),
]:
    k = key_by_prefix[prefix]
    if prefix.startswith("Erev Tisha"):
        ES[k] = (
            "Erev Tisha BeAv (8 Av por la tarde): deja de estudiar Torá salvo temas tristes; come la comida final (seudah hamafseket) antes del ayuno. "
            "Talit y tefilín en Shajarit de Erev Tisha BeAv — la restricción aplica el día de Tisha BeAv.\n\n"
            "Tisha BeAv (9 Av): ayuno completo de 25 horas; kinot en Shajarit sin talit ni tefilín; "
            "póntelos en Minjá tras jatzot halájico (usa tu app de zmanim). Siéntate en taburetes bajos hasta jatzot del 9 Av.\n\n"
            "Shabat Jazón (el Shabat antes del 9 Av): Shabat normal — carne y vino permitidos."
        )
        FR[k] = fr_text
    else:
        FR[k] = fr_text

# --- Afikoman / Seder / halachic hour ---
k = key_by_prefix["Afikoman — The Afikoman is matzah eaten at the end of the Seder"]
ES[k] = (
    "Afikomán — matzá que se come al final del Seder para que no siga otra comida, en memoria del sacrificio pascual. "
    "Los niños a menudo la «roban» por un premio. Necesitas un kezayit antes de jatzot halájico según muchos poskim. "
    "Es el último sabor de matzá que exige el Seder."
)
FR[k] = (
    "Afikoman — matsa mangée à la fin du Seder pour qu'aucun autre aliment ne suive, en souvenir du sacrifice pascal. "
    "Les enfants la « volent » souvent pour une récompense. Il faut un kezayit avant 'hatzot halakhique selon beaucoup de poskim. "
    "C'est le dernier goût de matsa exigé par le Seder."
)
k = key_by_prefix["The Afikoman is matzah eaten at the end of the Seder"]
ES[k] = (
    "El afikomán es matzá al final del Seder para que no siga otra comida — recuerdo del sacrificio pascual. "
    "Los niños suelen «robarla» por un premio. Kezayit antes de jatzot halájico según muchos poskim."
)
FR[k] = (
    "L'afikoman est la matsa à la fin du Seder pour qu'aucun autre aliment ne suive — souvenir du sacrifice pascal. "
    "Les enfants la « volent » souvent. Kezayit avant 'hatzot halakhique selon beaucoup de poskim."
)
k = key_by_prefix["Seder — The Seder is the ordered Pesach night meal"]
ES[k] = (
    "Seder — la comida ordenada de la noche de Pesaj: cuatro copas de vino, matzá, maror y lectura de la Hagadá para que los niños pregunten. "
    "Reclínate a la izquierda en las cuatro copas, matzá, korej y afikomán — no durante maror o jazeret. Recrea la salida de Egipto. "
    "El afikomán debe comerse antes de la medianoche halájica según muchos poskim. Prepara plato, Hagadá y necesidades de los invitados antes de Yom Tov."
)
FR[k] = (
    "Seder — repas ordonné de la nuit de Pessah : quatre coupes de vin, matsa, maror et lecture de la Haggadah pour que les enfants posent des questions. "
    "Accoudé à gauche pour les quatre coupes, matsa, kore'h et afikoman — pas pendant maror ou 'hazeret. Rejoue la sortie d'Égypte. "
    "L'afikoman avant minuit halakhique selon beaucoup de poskim. Préparez assiette, Haggadah et besoins des invités avant Yom Tov."
)
k = key_by_prefix["The Seder is the ordered Pesach night meal"]
ES[k] = ES[key_by_prefix["Seder — The Seder is the ordered Pesach night meal"]].replace("Seder — ", "")
FR[k] = FR[key_by_prefix["Seder — The Seder is the ordered Pesach night meal"]].replace("Seder — ", "")
k = key_by_prefix["halachic hour — A halachic hour (sha'ah zmanit)"]
ES[k] = (
    "sha'á zmanit — hora halájica: un doceavo del día de luz del alba al anochecer; más larga en verano, más corta en invierno. "
    "Plazos como Shemá de la mañana, venta de jametz y plag ha-minjá usan estas horas. Calendarios judíos las convierten para tu ubicación."
)
FR[k] = (
    "sha'ah zmanit — heure halakhique : un douzième du jour lumineux de l'aube au crépuscule ; plus longue en été, plus courte en hiver. "
    "Délais comme Shema du matin, vente de 'hamets et plag ha-min'ha utilisent ces heures. Les calendriers juifs les convertissent pour votre lieu."
)

# --- Borei pri hagafen / k'dei achilat / bedikat / shiva ---
k = key_by_prefix["Borei pri hagafen is the blessing on wine"]
ES[k] = (
    "Borei pri hagafen es la berajá sobre vino y jugo de uva — «Quien crea el fruto de la vid». "
    "Necesaria antes del Kiddush, las cuatro copas del Seder y el vino de simjá. Tras la berajá, bebe una cantidad significativa (reviit según muchos poskim). "
    "Las uvas de mesa usan otra berajá."
)
FR[k] = (
    "Borei pri hagafen est la bénédiction sur le vin et le jus de raisin — « Qui crée le fruit de la vigne ». "
    "Requise avant le Kiddouch, les quatre coupes au Seder et le vin de sim'ha. Après la bénédiction, boire une quantité significative (reviit selon beaucoup de poskim). "
    "Le raisin de table a une autre bénédiction."
)
k = key_by_prefix["Borei Pri HaGafen — Borei pri hagafen is the blessing on wine"]
ES[k] = (
    "Borei pri hagafen — berajá sobre vino y jugo de uva. Antes del Kiddush, las cuatro copas y simjá. "
    "Reviit según muchos poskim. Uvas de mesa — otra berajá."
)
FR[k] = (
    "Borei pri hagafen — bénédiction sur vin et jus de raisin. Avant Kiddouch, quatre coupes et sim'ha. "
    "Reviit selon beaucoup de poskim. Raisin de table — autre bénédiction."
)
k = key_by_prefix["K'dei achilat pras is the halachic time limit"]
ES[k] = (
    "Kedei ajilat pras es el límite halájico para comer un volumen como un solo acto — unos 4–7 minutos según muchos poskim. "
    "Aplica a kezayit de matzá en el Seder, maror y suficiente pan o pastel para berajá aharona. "
    "Si comiste kezayit de pan dentro de kedei ajilat pras, Birkat ha-Mazón cubre toda la comida."
)
FR[k] = (
    "Kedei akhilat pras est la limite halakhique pour manger un volume en un seul acte — environ 4–7 minutes selon beaucoup de poskim. "
    "S'applique au kezayit de matsa au Seder, maror et assez de pain ou gâteau pour une bénédiction finale. "
    "Un kezayit de pain dans ce délai — Birkat ha-Mazon couvre tout le repas."
)
k = key_by_prefix["k'dei achilat pras — K'dei achilat pras is the halachic time limit"]
ES[k] = (
    "kedei ajilat pras — Kedei ajilat pras es el límite halájico para comer un volumen como un solo acto — "
    "unos 4–7 minutos según muchos poskim. Aplica a kezayit de matzá en el Seder, maror y suficiente pan o "
    "pastel para berajá aharona. Si comiste kezayit de pan dentro de kedei ajilat pras, Birkat ha-Mazón cubre toda la comida."
)
FR[k] = (
    "kedei akhilat pras — Kedei akhilat pras est la limite halakhique pour manger un volume en un seul acte — "
    "environ 4–7 minutes selon beaucoup de poskim. S'applique au kezayit de matsa au Seder, maror et assez de "
    "pain ou gâteau pour une bénédiction finale. Un kezayit de pain dans ce délai — Birkat ha-Mazon couvre tout le repas."
)
k = key_by_prefix["bedikat chametz — Bedikat chametz is the formal search"]
ES[k] = (
    "bedikat jametz — búsqueda formal la noche antes de Pesaj, tras la caída de la noche, con vela o linterna según muchos poskim. "
    "A veces se esconden trozos de pan; aunque la casa esté limpia, la halajá exige esta noche de mitzvá."
)
FR[k] = (
    "bedikat 'hamets — recherche formelle la veille de Pessah, après la nuit tombée, à la bougie ou lampe selon beaucoup de poskim. "
    "Parfois on cache des morceaux de pain ; même maison propre — mitzva obligatoire cette nuit."
)
k = key_by_prefix["Shiva is seven days of mourning at home"]
ES[k] = (
    "Shivá — siete días de luto en casa tras el entierro: visitas, taburetes bajos, sin zapatos de cuero según muchos minhagim, "
    "Kadish con minján. Termina la mañana del séptimo día. Consuelo y reintegración gradual."
)
FR[k] = (
    "Shiva — sept jours de deuil à domicile après l'enterrement : visites, tabourets bas, pas de chaussures en cuir selon beaucoup de minhagim, "
    "Kaddish avec minyan. Se termine le matin du septième jour. Réconfort et retour progressif à la vie."
)
k = key_by_prefix["shiva — Shiva is seven days of mourning"]
ES[k] = ES[key_by_prefix["Shiva is seven days of mourning at home"]].replace("Shivá — ", "shivá — ")
FR[k] = FR[key_by_prefix["Shiva is seven days of mourning at home"]].replace("Shiva — ", "shiva — ")
k = key_by_prefix["A halachic hour (sha'ah zmanit) is one twelfth"]
ES[k] = (
    "Una shaá zmanit es un doceavo del día de luz del alba al anochecer — más larga en verano, más corta en invierno. "
    "Plazos como Shemá matutino, venta de jametz y plag ha-minjá."
)
FR[k] = (
    "Une sha'ah zmanit est un douzième du jour lumineux de l'aube au crépuscule — plus longue en été, plus courte en hiver. "
    "Délais comme Shema du matin, vente de 'hamets et plag ha-min'ha."
)

# --- Mitzvah alert tone fixes (mitzvah_alert_tone loads earlier; this shard wins) ---
k = key_by_prefix["Master the art of loving rebuke"]
ES[k] = (
    "¡Domina el arte del reproche amoroso! 💝 La Torá nos da una herramienta: si ves a alguien equivocarse, "
    "no te quedes callado ni chismes — ayúdale a crecer. Habla en privado, con suavidad y desde el amor. "
    "¿Sabías? «Tojajá» (תוכחה) se relaciona con «hojajá» (הוכחה), prueba de un camino mejor. "
    "Reto de hoy: da retroalimentación con bondad y sabiduría."
)
FR[k] = (
    "Maîtrise l'art de la réprimande aimante ! 💝 La Torah nous donne un outil : si tu vois quelqu'un se tromper, "
    "ne reste pas silencieux ni ne colporte — aide-le à grandir. Parle en privé, avec douceur et amour. "
    "Tu savais ? «Tokhaha» (תוכחה) est lié à «hokhaha» (הוכחה), preuve d'une voie meilleure. "
    "Défi du jour : donne un retour avec bonté et sagesse."
)
k = key_by_prefix["Guard against false prophecy"]
ES[k] = (
    "¡Cuídate de la falsa profecía! 🚫 El Rambam enseña (Yesodei haTorá, cap. 9): aunque alguien haga milagros, "
    "si intenta cambiar o anular parte de la Torá, es falso profeta. La profecía verdadera fortalece la Torá. "
    "Reto de hoy: mantente firme en la verdad eterna de la Torá."
)
FR[k] = (
    "Méfie-toi de la fausse prophétie ! 🚫 Le Rambam enseigne (Yesodei haTorah, ch. 9) : même avec des miracles, "
    "celui qui change ou annule une partie de la Torah est un faux prophète. La vraie prophétie fortifie la Torah. "
    "Défi du jour : reste ferme dans la vérité éternelle de la Torah."
)
k = key_by_prefix["Discover the letter Tav"]
ES[k] = (
    "¡Descubre la letra Tav (ת)! 🎯 La última del alef-bet representa la verdad (emet) y la perfección. "
    "«Emet» une la primera (א), la del medio (מ) y la última (ת) — la verdad es coherente de principio a fin. "
    "Reto de hoy: que tus palabras y acciones coincidan con la verdad."
)
FR[k] = (
    "Découvre la lettre Tav (ת) ! 🎯 La dernière de l'alef-bet représente la vérité (emet) et la perfection. "
    "«Emet» unit la première (א), la médiane (מ) et la dernière (ת) — la vérité est cohérente du début à la fin. "
    "Défi du jour : que tes paroles et tes actes s'alignent sur la vérité."
)
k = key_by_prefix["Protect G-d's holy Name"]
ES[k] = (
    "¡Protege el Nombre santo de D-s! 📜 El Rambam (cap. 6) nos enseña a cuidar todo lo que contiene el Nombre. "
    "Si encuentras un libro o papel sagrado, guárdalo con respeto o llévalo a la genizá. "
    "Incluso una letra del Nombre merece reverencia."
)
FR[k] = (
    "Protège le Nom saint de D. ! 📜 Le Rambam (ch. 6) nous enseigne à traiter avec soin tout ce qui contient le Nom. "
    "Si tu trouves un livre ou papier sacré, conserve-le avec respect ou apporte-le à la gueniza. "
    "Même une lettre du Nom mérite révérence."
)
k = key_by_prefix["Geneivat da'at"]
ES[k] = (
    "Geneivat da'at («robar la mente») es el engaño: publicidad falsa, ocultar defectos o falsear credenciales. "
    "Aplica a clientes, empleados y amigos. La Torá exige «balanzas honestas» en espíritu y letra. "
    "La confianza perdida cuesta mucho recuperarla."
)
FR[k] = (
    "Geneivat da'at (« voler l'esprit ») est la tromperie : publicité mensongère, dissimulation de défauts ou fausses qualifications. "
    "Cela vaut pour clients, employés et amis. La Torah exige des « balances honnêtes » dans l'esprit et la lettre. "
    "La confiance perdue est difficile à reconstruire."
)
k = key_by_prefix["Kashrut is the Torah system of permitted food"]
ES[k] = (
    "El kashrut es el sistema de la Torá sobre alimentos permitidos: shejitá, separar carne y lácteos y supervisión. "
    "Santifica la comida. Aprender etiquetas y cocina lleva tiempo — consulta a tu rabino al empezar."
)
FR[k] = (
    "Le kashrout est le système de la Torah sur les aliments permis : shehita, séparation viande-lait et supervision. "
    "Il sanctifie le repas. Apprendre les étiquettes et la cuisine prend du temps — demande à ton rav au départ."
)
k = key_by_prefix["Speech is one of the most powerful forces in Jewish life"]
ES[k] = (
    "El habla puede edificar o destruir en un instante. Shmirat halashon (שְׁמִירַת הַלָּשׁוֹן) es la mitzvá de cuidar lo que decimos.\n\n"
    "Evita: lashon hara (לָשׁוֹן הָרָע), rechilut (רְכִילוּת), ona'at devarim y la mentira.\n\n"
    "El Talmud compara el lashon hara con el asesinato. El Jafetz Jaim dedicó su vida a estas leyes.\n\n"
    "Antes de compartir noticias sobre alguien, pregúntate: ¿es necesario? ¿es bondadoso? No repitas chismes. "
    "Consulta a tu rabino en casos concretos."
)
FR[k] = (
    "La parole peut bâtir ou détruire en un instant. Shmirat halashon (שְׁמִירַת הַלָּשׁוֹן) est la mitzva de surveiller ce que nous disons.\n\n"
    "À éviter : lashon hara (לָשׁוֹן הָרָע), rechilut (רְכִילוּת), ona'at devarim et le mensonge.\n\n"
    "Le Talmud compare le lashon hara au meurtre. Le Hafets Haim a consacré sa vie à ces lois.\n\n"
    "Avant de partager des nouvelles sur quelqu'un, demande-toi : est-ce nécessaire ? est-ce bienveillant ? "
    "Ne répète pas les ragots. Demande à ton rav dans les cas précis."
)
def _apply_polish_batch(batch: list[tuple[str, str, str]]) -> None:
    for prefix, es_text, fr_text in batch:
        for k in strings:
            if k.startswith(prefix):
                ES[k] = es_text
                FR[k] = fr_text
                break
        else:
            raise SystemExit(f"missing key for polish prefix: {prefix!r}")

_apply_polish_batch(MELACHA_POLISH)
_apply_polish_batch(MISC_PROSE_POLISH)

# --- Glossary / Purim Meshulash / baal koreh ---
k = key_by_prefix["baal koreh — The baal koreh"]
ES[k] = (
    "Baal kore — quien canta la Torá del rollo con trop y pronunciación correctos. El entrenamiento toma meses. "
    "Los feligreses siguen en un Jumash. Un error que cambia el sentido puede requerir corrección — el oleh escucha, no lee del rollo."
)
FR[k] = (
    "Baal koré — celui qui lit la Torah du rouleau avec trop et prononciation corrects. L'entraînement prend des mois. "
    "Les fidèles suivent dans un 'Houmash. Une erreur changeant le sens peut exiger correction — l'oleh écoute, ne lit pas du rouleau."
)
k = key_by_prefix["Aliyah means \"going up\""]
ES[k] = (
    "Aliá significa «subir» — ser llamado a bendecir la lectura de la Torá en la sinagoga, o emigrar a Israel. "
    "En la sinagoga el oleh dice las bendiciones; el baal kore canta. El orden de las aliyot lo fija el gabai."
)
FR[k] = (
    "Aliyah signifie « monter » — être appelé pour bénir la lecture de la Torah à la synagogue, ou immigrer en Israël. "
    "À la synagogue l'oleh dit les bénédictions ; le baal koré lit. L'ordre des aliyot est fixé par le gabbaï."
)
k = key_by_prefix["Trop (ta'amei hamikra) are the cantillation"]
ES[k] = (
    "Trop (taaméi hamikrá) son las marcas de cantilación en el Jumash que indican al baal kore cómo cantar la Torá — melodía y pausas. "
    "Aprender trop es una habilidad aparte del significado de las palabras."
)
FR[k] = (
    "Trop (taamé hamikra) sont les signes de cantillation dans le 'Houmash qui indiquent au baal koré comment lire la Torah — mélodie et pauses. "
    "Apprendre le trop est une compétence distincte de la compréhension des mots."
)
k = key_by_prefix["Purim Meshulash starts tonight in Jerusalem"]
ES[k] = (
    "Purim Meshulash comienza esta noche en Jerusalén. Como el Shabat cae en medio de la festividad, lee y guarda este plan ahora — "
    "no podrás usar la app en Shabat para las mitzvot del domingo.\n\n$scheduleBlock\n\n"
    "Esta noche (jueves tras el anochecer): primera Meguilá. Mañana (viernes): segunda lectura y matanot la'evyonim. "
    "Mishloaj y seudá esperan al domingo."
)
FR[k] = (
    "Pourim Mechoulach commence ce soir à Jérusalem. Comme Chabbat tombe au milieu de la fête, lisez et conservez ce plan maintenant — "
    "vous ne pourrez pas compter sur l'app le Chabbat pour les mitzvot du dimanche.\n\n$scheduleBlock\n\n"
    "Ce soir (jeudi après la nuit) : première lecture de Meguila. Demain (vendredi) : deuxième lecture et matanot la'evyonim. "
    "Mishloah et seoudah attendent dimanche."
)
k = key_by_prefix["Purim Meshulash — Purim seudah on Sunday"]
ES[k] = (
    "Purim Meshulash — seudá de Purim el domingo (16 Adar).\n\n"
    "Cuándo: de día antes del anochecer; muchos después del mediodía, tras mishloaj manot.\n"
    "Cómo: comida festiva con pan, carne, vino y alegría; palabras de Torá o gratitud. "
    "El vino es costumbre extendida; celebra con responsabilidad.\n\n"
    "Completa las cuatro mitzvot de Purim en Jerusalén."
)
FR[k] = (
    "Pourim Mechoulach — seouda de Pourim dimanche (16 Adar).\n\n"
    "Quand : en journée avant le coucher du soleil ; beaucoup l'après-midi, après mishloah manot.\n"
    "Comment : repas festif avec pain, viande, vin et joie ; paroles de Torah ou gratitude. "
    "Le vin est une coutume répandue ; célébrez avec responsabilité.\n\n"
    "Achève les quatre mitzvot de Pourim à Jérusalem."
)
k = key_by_prefix["Purim Meshulash — matanot la'evyonim on Friday"]
ES[k] = (
    "Purim Meshulash — matanot la'evyonim solo el viernes (14 Adar).\n\n"
    "Hoy (viernes de día), no en Shabat:\n"
    "• Regalo a dos pobres distintos (mínimo dos destinatarios).\n"
    "• Cada regalo para una comida modesta de Purim; muchos dan tras la Meguilá de día.\n\n"
    "Domingo: mishloaj manot y seudá — prepara antes del Shabat."
)
FR[k] = (
    "Pourim Mechoulach — matanot la'evyonim vendredi seulement (14 Adar).\n\n"
    "Aujourd'hui (vendredi en journée), pas Chabbat :\n"
    "• Don à deux pauvres différents (minimum deux bénéficiaires).\n"
    "• Chaque don pour un repas modeste de Pourim ; beaucoup donnent après la Meguila de jour.\n\n"
    "Dimanche : mishloah manot et seoudah — préparez avant Chabbat."
)
k = key_by_prefix["Explore the mystical letter Heh"]
ES[k] = (
    "¡Descubre la letra Hei (ה)! ✡ Simboliza la presencia de D-s en el mundo: la abertura abajo muestra que, aunque caigamos, podemos volver. "
    "ה aparece dos veces en el Nombre de cuatro letras — D-s en lo espiritual y lo físico. Reto: nota Su presencia hoy."
)
FR[k] = (
    "Découvre la lettre Hé (ה) ! ✡ Elle symbolise la présence de D. dans le monde : l'ouverture en bas montre qu'on peut toujours revenir. "
    "ה apparaît deux fois dans le Nom à quatre lettres — D. dans le spirituel et le physique. Défi : remarque Sa présence aujourd'hui."
)
k = key_by_prefix["Al hamichya (Al HaMichya) is the after-blessing"]
ES[k] = (
    "Al ha-mijá — berajá después de mezonot (pasteles, pasta, cereales, etc.) cuando comiste un kezayit en kedei ajilat pras (4–9 min). "
    "Tras comida con pan, Birkat ha-Mazón cubre todo. Consulta a tu rabino."
)
FR[k] = (
    "Al ha-mihya — bénédiction après mezonot (gâteaux, pâtes, céréales, etc.) quand vous avez mangé un kezayit dans kedei akhilat pras (4–9 min). "
    "Après un repas avec pain, Birkat ha-Mazon couvre tout. Demande à ton rav."
)
k = key_by_prefix["aliyah — Aliyah means"]
ES[k] = ES[key_by_prefix["Aliyah means \"going up\""]].replace("Aliá significa", "aliyá — aliyá significa")
FR[k] = FR[key_by_prefix["Aliyah means \"going up\""]].replace("Aliyah signifie", "aliyah — aliyah signifie")

k = key_by_prefix["Purim Meshulash — Megillah on Friday"]
ES[k] = (
    "Purim Meshulash — Meguilá el viernes (14 Adar).\n\n"
    "Jueves tras el anochecer — primera lectura. Hoy (viernes) de día — segunda, obligatoria.\n\n"
    "Escucha cada palabra del rollo kosher. B'rajot: al mikra megillah, she'asa nissim, shehecheyanu.\n\n"
    "Matanot la'evyonim hoy (viernes). Mishloaj y seudá el domingo — prepara antes del Shabat."
)
FR[k] = (
    "Pourim Mechoulach — Meguila vendredi (14 Adar).\n\n"
    "Jeudi soir après la nuit — première lecture. Aujourd'hui (vendredi) en journée — deuxième, obligatoire.\n\n"
    "Écoute chaque mot du rouleau casher. Bénédictions : al mikra megillah, she'asa nissim, shehecheyanu.\n\n"
    "Matanot la'evyonim aujourd'hui (vendredi). Mishloah et seoudah dimanche — prépare avant Chabbat."
)
k = key_by_prefix["Purim Meshulash is the rare Jerusalem schedule"]
ES[k] = (
    "Purim Meshulash: cuando Shushan Purim (15 Adar) cae en Shabat — Meguilá y matanot la'evyonim el viernes; "
    "mishloaj manot y seudá el domingo. En Shabat: lectura Vayavo Amalek, haftará y Al HaNissim en Amidá y Birkat ha-Mazón."
)
FR[k] = (
    "Pourim Mechoulach : quand Pourim de Chouchan (15 Adar) tombe Chabbat — Meguila et matanot la'evyonim vendredi ; "
    "mishloah manot et seoudah dimanche. Chabbat : Vayavo Amalek, haftara et Al HaNissim dans l'Amida et Birkat ha-Mazon."
)

_SHACHARIT_ES = (
    "Shajarit es el servicio de la mañana: pésukei de-zimrá, Shemá con sus bendiciones y la Amidá; "
    "en Shabat y Yom Tov, lectura de la Torá y musaf. El Shemá debe recitarse antes del final de la tercera hora halájica; "
    "la Amidá, idealmente antes de la cuarta. En días laborables se usan tefilín. "
    "La shajarit diaria ancla el día en la oración antes del ruido del mundo."
)
_SHACHARIT_FR = (
    "Shaharit est le service du matin : pessoukei de-zimra, Chemâ et ses bénédictions, l'Amida ; "
    "le Chabbat et Yom Tov, lecture de la Torah et moussaf. Le Chemâ doit être récité avant la fin de la troisième heure halakhique ; "
    "l'Amida, idéalement avant la quatrième. Les téfilines sont portées en semaine. "
    "La shaharit quotidienne ancre la journée dans la prière avant le bruit du monde."
)
k = key_by_prefix["Shacharit is the morning service"]
ES[k] = _SHACHARIT_ES
FR[k] = _SHACHARIT_FR
k = key_by_prefix["Shacharit — Shacharit is the morning service"]
ES[k] = "shajarit — " + _SHACHARIT_ES
FR[k] = "shaharit — " + _SHACHARIT_FR

k = key_by_prefix["Taharat HaMishpacha (Family Purity Laws)"]
ES[k] = "taharat ha-mishpajá (leyes de pureza familiar)"
FR[k] = "taharat ha-mishpahah (lois de pureté familiale)"

k = key_by_prefix["Purim Meshulash — Purim Meshulash is the rare Jerusalem schedule"]
ES[k] = (
    "Purim Meshulash: cuando Shushan Purim (15 Adar) cae en Shabat — Meguilá y matanot la'evyonim el viernes; "
    "mishloaj manot y seudá el domingo. En Shabat: lectura Vayavo Amalek, haftará y Al HaNissim en Amidá y Birkat ha-Mazón "
    "(no el viernes ni el domingo)."
)
FR[k] = (
    "Pourim Mechoulach : quand Pourim de Chouchan (15 Adar) tombe Chabbat — Meguila et matanot la'evyonim vendredi ; "
    "mishloah manot et seoudah dimanche. Chabbat : Vayavo Amalek, haftara et Al HaNissim dans l'Amida et Birkat ha-Mazon "
    "(pas vendredi ni dimanche)."
)

_BAL_YERAEH_ES = (
    "Bal yera'eh («no se verá») es la prohibición de la Torá de que quede jametz visible en tu dominio en Pesaj. "
    "Junto con bal yimatzei («no se encontrará») impulsa bedikat jametz, biur y mejirat jametz. "
    "Hasta las migas bajo tu control cuentan. El jametz vendido en zona cerrada debe permanecer inaccesible."
)
_BAL_YERAEH_FR = (
    "Bal yera'eh («il ne sera pas vu») est l'interdiction de la Torah qu'il reste du hamets visible chez toi à Pessa'h. "
    "Avec bal yimatzei («il ne sera pas trouvé»), cela commande bedikat hamets, biour et mehirat hamets. "
    "Même les miettes sous ton contrôle comptent. Le hamets vendu, zone fermée, doit rester inaccessible."
)
k = key_by_prefix["Bal yera'eh (\"it shall not be seen\") is the Torah prohibition"]
ES[k] = _BAL_YERAEH_ES
FR[k] = _BAL_YERAEH_FR
k = key_by_prefix["bal yera'eh — Bal yera'eh"]
ES[k] = "bal yera'eh — " + _BAL_YERAEH_ES
FR[k] = "bal yera'eh — " + _BAL_YERAEH_FR

_KIDDUSH_MAKOM_ES = (
    "Kidush be-makom seudá significa santificar Shabat o Yom Tov con vino en el mismo lugar donde comerás después. "
    "No cumples el Kidush si lo recitas y te vas sin comer ahí. "
    "Viernes por la noche, la comida debe incluir pan (hamotsi) según la mayoría de poskim. "
    "En Yom Tov o Kidush de día, otros alimentos pueden calificar en algunos casos — pregunta a tu rav. "
    "Si haces Kidush en una habitación, la comida debe seguir ahí y no en otro espacio."
)
_KIDDUSH_MAKOM_FR = (
    "Kidouch be-makom seoudah : sanctifier Chabbat ou Yom Tov avec le vin là où tu prendras le repas. "
    "Partir sans manger sur place invalide le kidouch. Vendredi soir : pain (hamotsi) selon la plupart des poskim. "
    "En Yom Tov ou kidouch de jour, d'autres aliments peuvent suffire selon les cas — demande à ton rav. "
    "Si tu fais kidouch dans une pièce, le repas doit suivre au même endroit."
)
k = key_by_prefix["Kiddush b'Makom Seudah means sanctifying"]
ES[k] = _KIDDUSH_MAKOM_ES
FR[k] = _KIDDUSH_MAKOM_FR
k = key_by_prefix["Kiddush b'Makom Seudah — Kiddush b'Makom Seudah"]
ES[k] = "kidush be-makom seudah — " + _KIDDUSH_MAKOM_ES
FR[k] = "kidouch be-makom seoudah — " + _KIDDUSH_MAKOM_FR

k = key_by_prefix["Mikra Megillah (hearing the Book of Esther)"]
ES[k] = (
    "Mikra Meguila — escuchar el libro de Ester en Purim (Ester 9:28). Hombres y mujeres obligados.\n\n"
    "Cuándo: noche tras el anochecer; de día — mitzvá principal, tras Shajarit.\n\n"
    "Cómo: cada palabra del rollo; berajot: al mikra megillah, she'asa nissim, shehecheyanu.\n\n"
    "Maajazit haShekel — costumbre antes de la Meguilá. Al HaNissim en Amidá y Birkat ha-Mazón todo el día."
)
FR[k] = (
    "Mikra Meguila — écouter le livre d'Esther à Pourim (Esther 9:28). Hommes et femmes obligés.\n\n"
    "Quand : soir après la nuit ; le jour — mitsva principale, après Shaharit.\n\n"
    "Comment : chaque mot du rouleau ; bénédictions : al mikra megillah, she'asa nissim, shehecheyanu.\n\n"
    "Ma'ahazit haShekel — coutume avant la Meguila. Al HaNissim dans l'Amida et Birkat ha-Mazon toute la journée."
)

_BORER_ES = (
    "Borer prohíbe clasificar una mezcla en Shabat separando lo no deseado de lo deseado "
    "salvo tres condiciones: tomar lo bueno de lo malo, con la mano (no un colador dedicado), "
    "para uso inmediato. Ejemplo: quitar huesos del plato justo antes de comer — sí; "
    "clasificar ensalada para más tarde — no."
)
_BORER_FR = (
    "Borer interdit de trier un mélange le Shabbat en séparant le non désiré du désiré "
    "sauf trois conditions : prendre le bon du mauvais, à la main (pas une passoire dédiée), "
    "pour usage immédiat. Exemple : retirer les arêtes de l'assiette juste avant de manger — oui ; "
    "trier une salade pour plus tard — non."
)
k = key_by_prefix["Borer (selecting) forbids sorting"]
ES[k] = _BORER_ES
FR[k] = _BORER_FR
k = key_by_prefix["Borer — Borer (selecting)"]
ES[k] = "borer — " + _BORER_ES
FR[k] = "borer — " + _BORER_FR

k = key_by_prefix["17 Tammuz — fast marking"]
_T17_ES = (
    "17 Tamuz — ayuno menor que conmemora la brecha de las murallas de Jerusalén, inicio de las Tres Semanas. "
    "Sin comer ni beber desde el alba hasta la noche. Algunas comunidades leen Eija y kinot. "
    "No cae en Shabat — consulta tu calendario."
)
_T17_FR = (
    "17 Tamouz — jeûne mineur commémorant la brèche des murailles de Jérusalem, début des Trois Semaines. "
    "Sans manger ni boire de l'aube à la nuit. Certaines communautés lisent Eikhah et kinot. "
    "Ne tombe pas un Chabbat — consulte ton calendrier."
)
ES[k] = _T17_ES
FR[k] = _T17_FR

k = key_by_prefix["Plag HaMincha"]
ES[k] = "plag ha-minjá"
FR[k] = "plag ha-min'ha"

k = key_by_prefix["Kol Chamira"]
ES[k] = "kol jamira"
FR[k] = "kol hamira"

_THREE_WEEKS_SHORT_ES = (
    "Las Tres Semanas, del 17 de Tamuz al 9 de Av, son un período de duelo por la destrucción del Templo. "
    "Los asquenazíes y Jabad prohíben cortes de pelo, bodas y música instrumental durante todo el período; "
    "los sefardíes y Edot HaMizrach suelen ser más flexibles hasta la semana del 9 de Av. "
    "Las restricciones se intensifican en los Nueve Días — los minhagim varían según el nusaj."
)
_THREE_WEEKS_SHORT_FR = (
    "Les Trois Semaines, du 17 Tamouz au 9 Av, sont une période de deuil pour la destruction du Temple. "
    "Les ashkénazes et le Habad interdisent coupes de cheveux, mariages et musique instrumentale pendant toute la période ; "
    "les séfarades et Edot HaMizrach sont en général plus indulgents jusqu'à la semaine du 9 Av. "
    "Les restrictions s'intensifient pendant les Neuf Jours — les coutumes diffèrent selon le nusah."
)
k = key_by_prefix["The Three Weeks from 17 Tammuz to Tisha B'Av mourn"]
ES[k] = _THREE_WEEKS_SHORT_ES
FR[k] = _THREE_WEEKS_SHORT_FR
k = key_by_prefix["Three Weeks — The Three Weeks from 17 Tammuz"]
ES[k] = f"Las Tres Semanas — {_THREE_WEEKS_SHORT_ES}"
FR[k] = f"Les Trois Semaines — {_THREE_WEEKS_SHORT_FR}"

_OMER_GLOSSARY_ES = (
    "Sefirat HaOmer cuenta cuarenta y nueve días desde la segunda noche de Pésaj hasta Shavuot — "
    "uniendo la libertad con la entrega de la Torá. Cada noche, preferentemente después de la caída de la noche (tzeit), "
    "bendices y anuncias el día y la semana del conteo. Si faltó un día completo, puede afectar si puedes seguir diciendo la berajá — consulta a tu rav. "
    "Durante el Omer, muchas comunidades guardan costumbres de luto (sin música, bodas ni cortes de pelo) hasta Lag BaOmer o Shavuot, "
    "según el minhag, en memoria de la plaga que mató a muchos alumnos de Rabí Akiva."
)
_OMER_GLOSSARY_FR = (
    "Sefirat HaOmer compte quarante-neuf jours depuis la deuxième nuit de Pessah jusqu'à Chavouot — "
    "liant la liberté à la réception de la Torah. Chaque nuit, de préférence après la sortie des étoiles (tzeit), "
    "tu bénis et annonces le jour et la semaine du compte. Manquer une journée entière peut affecter la bénédiction des nuits suivantes — demande à ton rav. "
    "Pendant l'Omer, de nombreuses communautés observent des coutumes de deuil (pas de musique, mariages ni coupes de cheveux) jusqu'à Lag BaOmer ou Chavouot, "
    "selon le minhag, en mémoire de la peste qui frappa de nombreux élèves de Rabbi Akiva."
)
k = key_by_prefix["Sefirat HaOmer counts forty-nine days"]
ES[k] = _OMER_GLOSSARY_ES
FR[k] = _OMER_GLOSSARY_FR
k = key_by_prefix["Sefirat HaOmer — Sefirat HaOmer counts"]
ES[k] = f"Sefirat HaOmer — {_OMER_GLOSSARY_ES}"
FR[k] = f"Sefirat HaOmer — {_OMER_GLOSSARY_FR}"

_MAARIV_ES = (
    "Maariv es el servicio vespertino después del anochecer — Shemá, Amidá y, en motzei Shabat, Havdalá o inserciones de festividad. "
    "El Maariv del sábado por la noche puede incluir Vatodi'enu cuando comienza Yom Tov. "
    "En días laborables, muchos judíos que trabajan se conectan con la comunidad al final del día. "
    "La constancia en Maariv crea un ritmo de cerrar el día con D-s."
)
_MAARIV_FR = (
    "Maariv est le service du soir après la sortie des étoiles — Chemâ, Amida et, à motzei Chabbat, Havdala ou insertions de fête. "
    "Le Maariv du samedi soir peut inclure Vatodi'enu quand Yom Tov commence. "
    "En semaine, beaucoup de Juifs actifs se retrouvent en communauté en fin de journée. "
    "La régularité à Maariv ancre la fin de la journée dans la prière."
)
k = key_by_prefix["Maariv is the evening service"]
ES[k] = _MAARIV_ES
FR[k] = _MAARIV_FR
k = key_by_prefix["Maariv — Maariv is the evening service"]
ES[k] = f"Maariv — {_MAARIV_ES}"
FR[k] = f"Maariv — {_MAARIV_FR}"

_KOL_CHAMIRA_ES = (
    "Kol Jamira es una declaración en arameo que anula el jametz que aún está en tu posesión. "
    "La versión nocturna (tras bedikat jametz) anula solo el jametz que no viste ni conoces — aún puedes poseer jametz legalmente para el desayuno. "
    "La versión matutina (tras biur en Erev Pesaj) anula TODO el jametz, visto o no, destruido o no."
)
_KOL_CHAMIRA_FR = (
    "Kol 'Hamira est une déclaration en araméen qui annule le 'hamets encore en votre possession. "
    "La version nocturne (après bedikat 'hamets) n'annule que le 'hamets que vous n'avez pas vu — vous pouvez encore en posséder légalement pour le petit-déjeuner. "
    "La version matinale (après biour à Érev Pessah) annule TOUT le 'hamets, vu ou non, détruit ou non."
)
k = key_by_prefix["Kol Chamira is an Aramaic declaration"]
ES[k] = _KOL_CHAMIRA_ES
FR[k] = _KOL_CHAMIRA_FR
k = key_by_prefix["Kol Chamira — Kol Chamira is an Aramaic"]
ES[k] = f"Kol Jamira — {_KOL_CHAMIRA_ES}"
FR[k] = f"Kol 'Hamira — {_KOL_CHAMIRA_FR}"

_CHITAS_ES = (
    "Jitas (חיט\"ס) es el ciclo de estudio diario de Jabad: Jumash (parashá semanal con Rashí), Tehilim (salmos según el día del mes) y Tania. "
    "Muchos lo completan tras las oraciones de la mañana, pero puedes estudiar las porciones del día en cualquier momento antes del siguiente día hebreo. "
    "Hayom Yom es una lectura diaria breve adicional."
)
_CHITAS_FR = (
    "Hitas (חיט\"ס) est le cycle d'étude quotidien du Habad : 'Houmash (paracha hebdomadaire avec Rachi), Tehilim (psaumes selon le jour du mois) et Tanya. "
    "Beaucoup le terminent après les prières du matin, mais vous pouvez étudier les portions du jour à tout moment avant le jour hébraïque suivant. "
    "Hayom Yom est une courte lecture quotidienne supplémentaire."
)
k = key_by_prefix["Chitas (חיט\"ס) is Chabad's daily"]
ES[k] = _CHITAS_ES
FR[k] = _CHITAS_FR
k = key_by_prefix["Chitas — Chitas (חיט\"ס) is Chabad's"]
ES[k] = f"Jitas — {_CHITAS_ES}"
FR[k] = f"Hitas — {_CHITAS_FR}"

k = key_by_prefix["Motzei Yom Kippur meal — after nightfall"]
ES[k] = "Comida de motzei Yom Kipur — después del anochecer (tzeit)."
FR[k] = "Repas de motzei Yom Kippour — après la sortie des étoiles (tzeit)."

_KIDDUSH_LEVANA_GLOSSARY_ES = (
    "Kidush Levana (קִידּוּשׁ לְבָנָה — santificación de la Luna) es una berajá mensual que se recita una vez "
    "cada mes hebreo cuando la luna nueva es visible de noche. No es adoración de la luna — alaba a D-s por los "
    "ciclos de la creación y el renacimiento de Israel. El Talmud (Sanhedrín 42a) compara quien bendice la luna "
    "correctamente con saludar a la Shejiná. Los hombres están obligados en esta mitzvá positiva ligada al tiempo; "
    "las mujeres están exentas (como la mayoría de mitzvot del tiempo) y el minhag extendido es que las mujeres no "
    "la recitan — autoridades destacadas (Shelá, Magen Avraham O.J. 426:1) desaconsejan también por razones "
    "cabalísticas. Recítala al aire libre bajo el cielo abierto, de pie, después de la caída de la noche (tzeit). "
    "Los asquenazíes y Jabad suelen esperar al menos tres días desde la luna nueva; la mayoría de sefardíes esperan "
    "siete (Shulján Aruj O.J. 426:4); comunidades marroquíes y otras norteafrikanas pueden empezar tras tres días "
    "(Peninei Halakha 05-01-18). Motzei Shabat es costumbre extendida cuando la gente está vestida y la luna es "
    "visible; si esperar arriesga noches nubladas y perder la ventana mensual, dígala en la primera noche despejada "
    "de la semana. La ventana termina en la luna llena (~14,75 días del molad) — en la noche del 15 puede ser "
    "demasiado tarde; consulta Sof Zman Kidush Levana para tu ubicación. También se llama Birkat HaLevanah."
)
_KIDDUSH_LEVANA_GLOSSARY_FR = (
    "Kidouch Levana (קִידּוּשׁ לְבָנָה — sanctification de la Lune) est une bénédiction mensuelle prononcée une fois "
    "chaque mois hébraïque lorsque la nouvelle lune est visible la nuit. Ce n'est pas le culte de la lune — elle "
    "loue D. pour les cycles de la création et le renouveau d'Israël. Le Talmud (Sanhédrin 42a) compare celui qui "
    "bénit correctement la lune à saluer la Chekhina. Les hommes sont obligés par cette mitsva positive liée au temps ; "
    "les femmes en sont exemptées (comme la plupart des mitsvot liées au temps) et la coutume répandue est qu'elles "
    "ne la récitent pas — des autorités (Shelah, Maguen Avraham O.H. 426:1) le déconseillent aussi pour des raisons "
    "cabalistiques. Dites-la dehors sous le ciel ouvert, debout, après la sortie des étoiles (tzeit). Les ashkénazes "
    "et le Habad attendent souvent au moins trois jours après la nouvelle lune ; la plupart des séfarades attendent "
    "sept (Choulhan Aroukh O.H. 426:4) ; des kehillot marocaines et nord-africaines peuvent commencer après trois "
    "jours (Peninei Halakha 05-01-18). Motzei Chabbat est une coutume répandue quand on est encore habillé et que la "
    "lune est visible ; si attendre risque des nuits nuageuses et de manquer la fenêtre mensuelle, dites-la la première "
    "nuit claire de la semaine. La fenêtre se ferme à la pleine lune (~14,75 jours du molad) — la nuit du 15 peut déjà "
    "être trop tard ; vérifiez Sof Zman Kidouch Levana pour votre lieu. On l'appelle aussi Birkat HaLevanah."
)
k = key_by_prefix["Kiddush Levana (קִידּוּשׁ לְבָנָה — Sanctification of the Moon)"]
ES[k] = _KIDDUSH_LEVANA_GLOSSARY_ES
FR[k] = _KIDDUSH_LEVANA_GLOSSARY_FR
k = key_by_prefix["Kiddush Levana — Kiddush Levana (קִידּוּשׁ לְבָנָה"]
ES[k] = f"Kidush Levana — {_KIDDUSH_LEVANA_GLOSSARY_ES}"
FR[k] = f"Kidouch Levana — {_KIDDUSH_LEVANA_GLOSSARY_FR}"

_HADLAKAT_SHORT_ES = (
    "Hadlakat Nerot — encender velas antes de Shabat o Yom Tov para acoger la santidad en el hogar. "
    "Tradicionalmente las encienden las mujeres; si no hay mujer presente, un hombre. "
    "Minhag asquenazí y Jabad: las hijas pueden encender velas propias con berajá. "
    "Minhag sefardí (Shulján Aruj; Rav Ovadia Yosef): las hijas en casa de los padres no recitan berajá en velas "
    "aparte si la madre ya encendió — sería berajá levatalá. "
    "Los asquenazíes suelen encender y luego bendecir con los ojos cubiertos; los sefardíes pueden bendecir primero. "
    "Las velas de Shabat se encienden antes de la puesta del sol (no antes de plag ha-minjá). "
    "Las velas de Erev Yom Tov entre semana también antes del atardecer. "
    "Si Yom Tov comienza en motzei Shabat, se encienden solo después de tzeit desde llama ya encendida. "
    "En otras noches de Yom Tov, antes del inicio puede encender fósforo nuevo; una vez comenzado Yom Tov, "
    "solo desde llama preexistente."
)
_HADLAKAT_SHORT_FR = (
    "Hadlakat Nerot — allumer des bougies avant Chabbat ou Yom Tov pour accueillir la sainteté à la maison. "
    "Traditionnellement les femmes allument ; s'il n'y a pas de femme, un homme. "
    "Minhag ashkénaze et Habad : les filles peuvent allumer leurs propres bougies avec une berakha. "
    "Minhag séfarade (Choulhan Aroukh ; Rav Ovadia Yossef) : les filles chez leurs parents ne récitent pas de berakha "
    "sur des bougies séparées si la mère a déjà allumé — ce serait une berakha levatala. "
    "Les ashkénazes allument souvent puis bénissent les yeux couverts ; les séfarades peuvent bénir d'abord. "
    "Les bougies de Chabbat s'allument avant le coucher du soleil (pas avant plag ha-min'ha). "
    "Les bougies d'Érev Yom Tov en semaine aussi avant le coucher du soleil. "
    "Si Yom Tov commence à motzei Chabbat, on allume seulement après tzeit depuis une flamme déjà allumée. "
    "Les autres nuits de Yom Tov, avant le début on peut frotter une allumette neuve ; une fois Yom Tov commencé, "
    "seulement depuis une flamme préexistante."
)
k = key_by_prefix["Hadlakat Nerot is lighting candles before"]
ES[k] = _HADLAKAT_SHORT_ES
FR[k] = _HADLAKAT_SHORT_FR
k = key_by_prefix["Hadlakat Nerot — Hadlakat Nerot is lighting"]
ES[k] = f"Hadlakat Nerot — {_HADLAKAT_SHORT_ES}"
FR[k] = f"Hadlakat Nerot — {_HADLAKAT_SHORT_FR}"

_LIGHTING_FRIDAY_ES = (
    "Encender velas el viernes por la tarde es una de las prácticas más queridas del judaísmo — acoge oficialmente "
    "a Shabat en el hogar.\n\n"
    "Qué es:\n"
    "Hadlakat Nerot (הַדְלָקַת נֵרוֹת) es una mitzvá rabínica semanal antes de Shabat. Las velas de Shabat traen luz "
    "y paz al hogar y honran la santidad del día.\n\n"
    "Quién enciende:\n"
    "Tradicionalmente la mujer del hogar. Si no hay mujer, enciende un hombre. Minhag asquenazí y Jabad: niñas e "
    "hijas solteras pueden encender velas propias con berajá (a menudo una por niña). Minhag sefardí (Shulján Aruj; "
    "Rav Ovadia Yosef): hijas bajo el techo paterno no recitan berajá en velas aparte si la madre ya encendió — "
    "sería berajá levatalá.\n\n"
    "Cuándo:\n"
    "Unos 18 minutos antes de la puesta del sol el viernes (algunas comunidades asquenazíes 20–22 minutos; muchas "
    "sefardíes más cerca del atardecer). La app muestra la hora local de encendido.\n\n"
    "Cuántas:\n"
    "• Mínimo dos velas: una por «zajór» (Éxodo 20:8) y otra por «shamor» (Deuteronomio 5:12)\n"
    "• Muchas mujeres añaden una vela por cada hijo\n\n"
    "La berajá:\n"
    "• Asquenazí: encender primero, mecer las manos, cubrir los ojos, recitar la berajá y descubrir — al recitar la "
    "berajá se acepta Shabat y no se puede encender después.\n"
    "• Sefardí: berajá primero, luego encender.\n"
    "Sigue el minhag de tu familia; en duda, pregunta a tu rav.\n\n"
    "Berajá: «Baruj Atá Adonai Eloheinu Melej haolam, asher kidshanu bemitzvotav vetzivanu lehadlik ner shel Shabat.»\n\n"
    "Importante:\n"
    "Tras encender y bendecir, Shabat ha comenzado para ti. No enciendas fuego adicional ni uses electricidad después."
)
_LIGHTING_FRIDAY_FR = (
    "Allumer les bougies le vendredi après-midi est l'une des pratiques les plus aimées du judaïsme — elle accueille "
    "officiellement Chabbat à la maison.\n\n"
    "Ce que c'est :\n"
    "Hadlakat Nerot (הַדְלָקַת נֵרוֹת) est une mitsva rabbinique hebdomadaire avant Chabbat. Les bougies apportent "
    "lumière et paix et honorent la sainteté du jour.\n\n"
    "Qui allume :\n"
    "Traditionnellement la femme du foyer. S'il n'y a pas de femme, un homme. Minhag ashkénaze et Habad : filles et "
    "filles célibataires peuvent allumer leurs bougies avec berakha (souvent une par enfant). Minhag séfarade "
    "(Choulhan Aroukh ; Rav Ovadia Yossef) : les filles chez leurs parents ne récitent pas de berakha sur des "
    "bougies séparées si la mère a déjà allumé — berakha levatala.\n\n"
    "Quand :\n"
    "Environ 18 minutes avant le coucher du soleil le vendredi (certaines communautés ashkénazes 20–22 minutes ; "
    "beaucoup de séfarades plus près du coucher). L'application affiche l'heure locale.\n\n"
    "Combien :\n"
    "• Minimum deux bougies : une pour « Zachor » (Exode 20:8) et une pour « Shamor » (Deutéronome 5:12)\n"
    "• Beaucoup de femmes ajoutent une bougie par enfant\n\n"
    "La berakha :\n"
    "• Ashkénaze : allumer d'abord, agiter les mains, couvrir les yeux, réciter la berakha puis découvrir — la "
    "berakha accepte Chabbat et on ne peut plus allumer ensuite.\n"
    "• Séfarade : berakha d'abord, puis allumer.\n"
    "Suivez le minhag familial ; en cas de doute, demande à ton rav.\n\n"
    "Berakha : « Baroukh Ata Adonaï Eloheinou Melekh haolam, asher kidshanu bemitzvotav vetzivanu lehadlik ner "
    "shel Chabbat. »\n\n"
    "Important :\n"
    "Après avoir allumé et béni, Chabbat a commencé pour vous. N'allumez plus de feu ni n'utilisez l'électricité."
)
k = key_by_prefix["Lighting candles on Friday afternoon is one"]
ES[k] = _LIGHTING_FRIDAY_ES
FR[k] = _LIGHTING_FRIDAY_FR

_HADLAKAT_ALERT_ES = (
    "¡Aprende la mitzvá de Hadlakat Nerot — encender velas de Shabat — y un fascinante dilema halájico! 🕯️ "
    "Los Sabios las establecieron para shalom bayit (paz en el hogar), no solo ambiente. Hay que beneficiarse "
    "activamente de la luz. ¿Y si aceptas Shabat temprano con el sol aún brillante? Surge el debate del «¿para qué "
    "una lámpara al mediodía?» (Jullin 60b). Magen Avraham (O.J. 263:11): si enciendes y te vas, debes sentarte y "
    "beneficiarte brevemente de las llamas. Shulján Aruj ha-Rav y Aruj ha-Shulján discrepan: la luz de día no sirve — "
    "usa velas largas que sigan ardiendo al volver de noche. Autoridades modernas como Shemirat Shabat Kehiljata "
    "(43:17) proponen temporizadores en el comedor principal con kavaná explícita de que la berajá cubra velas y "
    "luces eléctricas (Mishná Berurá 263:41). ¡Así la mitzvá queda cumplida cuando anochece!"
)
_HADLAKAT_ALERT_FR = (
    "Découvre la mitsva d'Hadlakat Nerot — bougies de Chabbat — et un casse-tête halakhique fascinant ! 🕯️ "
    "Les Sages les ont instituées pour le shalom bayit, pas seulement l'ambiance : il faut profiter activement de la "
    "lumière. Et si tu accueilles Chabbat tôt alors que le soleil brille encore ? Débat du « à quoi bon une lampe "
    "à midi ? » (Houllin 60b). Maguen Avraham (O.H. 263:11) : si tu allumes et pars, assieds-toi pour en "
    "bénéficier brièvement. Choulhan Aroukh ha-Rav et Aroukh ha-Choulhan divergent : la lumière de jour est inutile — "
    "des bougies longues encore allumées au retour. Des autorités modernes comme Shemirat Shabbat Kehilchata (43:17) "
    "proposent des minuteries dans la salle à manger avec kavana que la berakha couvre bougies et lumières "
    "(Michna Beroura 263:41) — la mitsva est accomplie à la tombée de la nuit !"
)
k = key_by_prefix["Learn about the mitzvah of Hadlakat Nerot"]
ES[k] = _HADLAKAT_ALERT_ES
FR[k] = _HADLAKAT_ALERT_FR

_HAGBAH_ES = (
    "Hagba consiste en levantar el rollo abierto de la Torá para que la congregación vea la escritura "
    "y recite «V'zot HaTorah» (y versículos relacionados). Los asquenazíes (y la mayoría de las comunidades "
    "jabadíes) hacen hagba después de la lectura, antes de gelilá (enrollar y vestir el rollo). Los sefardíes "
    "(nusaj Edot HaMizrach) hacen hagba antes de que comience la lectura — se muestra el rollo y luego se lee. "
    "Sigue el orden de tu sinagoga para saber cuándo ponerte de pie y responder."
)
_HAGBAH_FR = (
    "Hagba consiste à lever le rouleau ouvert de la Torah pour que la congrégation voie l'écriture "
    "et récite « V'zot HaTorah » (et versets connexes). Les ashkénazes (et la plupart des communautés "
    "habad) font hagba après la lecture, avant gelila (enrouler et habiller le rouleau). Les séfarades "
    "(nusah Edot HaMizrach) font hagba avant le début de la lecture — le rouleau est montré, puis lu. "
    "Suivez l'ordre de votre synagogue pour savoir quand vous lever et répondre."
)
k = key_by_prefix["Hagbah is lifting the open Torah"]
ES[k] = _HAGBAH_ES
FR[k] = _HAGBAH_FR
k = key_by_prefix["Hagbah — Hagbah is lifting"]
ES[k] = f"Hagba — {_HAGBAH_ES}"
FR[k] = f"Hagba — {_HAGBAH_FR}"

_KITNIYOT_ES = (
    "Los kitniot son legumbres, arroz, maíz y alimentos similares que los asquenazíes tradicionalmente evitan "
    "en Pesaj aunque no sean jametz de la Torá — es un rigor minhag asquenazí, no una prohibición toráica. "
    "Están permitidos halájicamente para los sefardíes (aunque algunas comunidades tienen costumbres locales "
    "de evitar artículos concretos). La costumbre surgió para evitar confusión con granos de jametz. Aceite de "
    "cacahuete, quinoa y similares siguen psak muy distintos — consulta a tu rav, no debates en redes."
)
_KITNIYOT_FR = (
    "Les kitniot sont légumineuses, riz, maïs et aliments similaires qu'ashkénazes évitent traditionnellement "
    "à Pessah bien qu'ils ne soient pas hamets toranique — rigueur de minhag ashkénaze, pas interdiction toranique. "
    "Ils sont permis halakhiquement pour séfarades (certaines communautés évitent des articles précis). "
    "La coutume évite la confusion avec les grains de hamets. Huile d'arachide, quinoa, etc. — psak très variés ; "
    "demande à ton rav, pas aux réseaux sociaux."
)
k = key_by_prefix["Kitniyot are legumes, rice, corn"]
ES[k] = _KITNIYOT_ES
FR[k] = _KITNIYOT_FR
k = key_by_prefix["kitniyot — Kitniyot are legumes"]
ES[k] = f"kitniot — {_KITNIYOT_ES}"
FR[k] = f"kitniot — {_KITNIYOT_FR}"

_MEGILLAH_ES = (
    "Meguilá significa rollo; en Purim es el libro de Ester, leído dos veces — noche y día. Ambas lecturas son "
    "obligatorias; la de día es la mitzvá principal (mitzvat ha'yom), la nocturna se instituyó después. Escuchar "
    "cada palabra es la mitzvá; el ruido al nombre de Amán es costumbre. Shehejeyanu: los asquenazíes la recitan "
    "antes de la primera lectura nocturna y la primera diurna (Rema O.J. 692:1); los sefardíes solo de noche — "
    "la berajá nocturna cubre el día (Shulján Aruj; Yalkut Yosef). Si faltan palabras, pregunta si debes volver a "
    "escuchar esa sección."
)
_MEGILLAH_FR = (
    "Meguila signifie rouleau ; à Pourim c'est le livre d'Esther, lu deux fois — nuit et jour. Les deux lectures "
    "sont obligatoires ; celle du jour est la mitsva principale (mitzvat ha'yom), celle de nuit fut instituée plus "
    "tard. Entendre chaque mot est la mitsva ; le bruit au nom d'Haman est coutume. Shehe'heyanu : ashkénazes "
    "avant la première lecture du soir et la première du jour (Rema O.H. 692:1) ; séfarades seulement la nuit — "
    "la bénédiction nocturne couvre le jour (Choulhan Aroukh ; Yalkut Yossef). Mots manqués — demande à ton rav."
)
k = key_by_prefix["Megillah means scroll; on Purim"]
ES[k] = _MEGILLAH_ES
FR[k] = _MEGILLAH_FR
k = key_by_prefix["Megillah — Megillah means scroll"]
ES[k] = f"Meguilá — {_MEGILLAH_ES}"
FR[k] = f"Meguila — {_MEGILLAH_FR}"

_SELICHOT_ES = (
    "Las Selihot son oraciones penitenciales recitadas antes de Rosh Hashaná (los asquenazíes a menudo desde el "
    "motzei Shabat anterior; los sefardíes desde Elul). Incluyen súplicas poéticas y los Trece Atributos de la "
    "Misericordia. Madrugar o quedarse tarde para Selihot marca el tono de seriedad antes de los Yamim Noraim. "
    "El nusaj y el horario varían — consulta tu sinagoga."
)
_SELICHOT_FR = (
    "Les Selichot sont des prières pénitentielles avant Rosh Hashana (ashkénazes souvent dès le motzei Chabbat "
    "précédent ; séfarades depuis Eloul). Elles incluent des supplications poétiques et les Treize Attributs de "
    "miséricorde. Se lever tôt ou tard pour Selichot donne le ton des Yamim Noraïm. Nusah et horaires — "
    "renseignez-vous à la synagogue."
)
k = key_by_prefix["Selichot are penitential prayers"]
ES[k] = _SELICHOT_ES
FR[k] = _SELICHOT_FR
k = key_by_prefix["Selichot — Selichot are penitential"]
ES[k] = f"Selihot — {_SELICHOT_ES}"
FR[k] = f"Selichot — {_SELICHOT_FR}"

_TZITZIT_ES = (
    "Los tzitzit son flecos en prendas de cuatro esquinas que nos recuerdan las 613 mitzvot. El talit katan se usa "
    "a diario — por encima o debajo de la camisa según el minhag (muchos jasidim, sefardíes y seguidores del Arizal "
    "lo llevan sobre la camisa, bajo chaleco o chaqueta); el talit gadol en Shajarit. Las cuerdas deben ser kosher "
    "y atadas correctamente. Mirar los tzitzit durante el Shemá cumple «los verás». Algunas mujeres en ciertas "
    "comunidades usan tzitzit; consulta a tu rav."
)
_TZITZIT_FR = (
    "Les tsitsit sont des franges sur vêtements à quatre coins rappelant les 613 mitsvot. Le talit katan se porte "
    "quotidiennement — dessus ou dessous la chemise selon le minhag (beaucoup de hassidim, séfarades et disciples "
    "de l'Arizal le portent sur la chemise, sous gilet ou veste) ; le talit gadol à Shaharit. Les fils doivent être "
    "casher et noués correctement. Regarder les tsitsit pendant le Chemâ accomplit « tu les verras ». Certaines "
    "femmes dans certaines communautés portent tsitsit — demande à ton rav."
)
k = key_by_prefix["Tzitzit are fringes on four-cornered"]
ES[k] = _TZITZIT_ES
FR[k] = _TZITZIT_FR
k = key_by_prefix["tzitzit — Tzitzit are fringes"]
ES[k] = f"tzitzit — {_TZITZIT_ES}"
FR[k] = f"tsitsit — {_TZITZIT_FR}"

_KASHERING_ES = (
    "El kashering hace aptos los utensilios para uso kosher o Pesaj. El principio es kebolo kach polto — como el "
    "utensilio absorbió sabor prohibido, así lo expulsa. Lo usado en agua hirviendo suele necesitar hagalah "
    "(inmersión en agua en ebullición); lo usado directamente al fuego puede necesitar libun (calor intenso hasta "
    "rojo). El plástico y el vidrio tienen psak muy distintos entre asquenazíes y sefardíes — más estricto en "
    "Pesaj. Consulta a tu rav. La cerámica no se puede kasherizar. Muchas familias tienen juego aparte para Pesaj."
)
_KASHERING_FR = (
    "Le kashering rend les ustensiles aptes pour casher ou Pessah. Principe kebolo kach polto — comme l'ustensile "
    "a absorbé un goût interdit, il l'expulse. Usage à l'eau bouillante : hagala souvent ; au feu direct : liboun. "
    "Plastique et verre — psak très divergents ashkénazes/séfarades, plus strict à Pessah. Demande à ton rav. "
    "La céramique ne se kasher pas. Beaucoup gardent un service séparé pour Pessah."
)
k = key_by_prefix["Kashering makes utensils fit"]
ES[k] = _KASHERING_ES
FR[k] = _KASHERING_FR
k = key_by_prefix["Kashering — Kashering makes utensils"]
ES[k] = f"Kashering — {_KASHERING_ES}"
FR[k] = f"Kashering — {_KASHERING_FR}"

_EGG_MATZAH_ES = (
    "Matzá de huevo — matzá hecha con huevos y/o jugo de fruta (matzá ashira); no es lejem oni (pan del pobre) "
    "y no puede usarse en el Seder. Los asquenazíes no la comen en Pesaj salvo enfermos, ancianos o niños pequeños "
    "(Rema O.J. 462:4); los sefardíes la permiten halájicamente, aunque muchas autoridades sefardíes contemporáneas "
    "recomiendan evitarla salvo necesidad."
)
_EGG_MATZAH_FR = (
    "Matsa aux œufs — matsa faite avec œufs et/ou jus de fruit (matsa ashira) ; pas lehem oni et interdite au "
    "Seder. Ashkénazes ne la mangent pas à Pessah sauf malades, personnes âgées ou jeunes enfants (Rema O.H. 462:4) ; "
    "séfarades l'autorisent halakhiquement, bien que beaucoup de poskim séfarades contemporains recommandent de "
    "l'éviter sauf nécessité."
)
k = key_by_prefix["egg matzah — matzah made with eggs"]
ES[k] = _EGG_MATZAH_ES
FR[k] = _EGG_MATZAH_FR
k = key_by_prefix["matzah made with eggs and/or fruit juice"]
ES[k] = _EGG_MATZAH_ES
FR[k] = _EGG_MATZAH_FR

_SHAVUOT_ES = (
    "Shavuot marca Matan Torá — la entrega de la Torá en el Sinaí, siete semanas después de Pesaj. "
    "A diferencia de otros festivales, las velas y el Kidush no pueden comenzar hasta el anochecer completo (tzeit). "
    "Comidas lácteas, estudio nocturno y la lectura de Rut son costumbres. Un día en Israel, dos en la Diáspora."
)
_SHAVUOT_FR = (
    "Shavouot marque Matan Torah — la réception de la Torah au Sinaï, sept semaines après Pessah. "
    "Contrairement aux autres fêtes, les bougies et le Kiddouch ne peuvent commencer qu'après la tombée complète "
    "de la nuit (tzeit). Repas laitiers, étude de nuit et lecture de Ruth sont des coutumes. Un jour en Israël, "
    "deux en diaspora."
)
k = key_by_prefix["Shavuot marks Matan Torah"]
ES[k] = _SHAVUOT_ES
FR[k] = _SHAVUOT_FR
k = key_by_prefix["Shavuot — Shavuot marks Matan Torah"]
ES[k] = f"Shavuot — {_SHAVUOT_ES}"
FR[k] = f"Shavouot — {_SHAVUOT_FR}"

_TZITZIT_LONG_ES = (
    "La Torá ordena a los hombres judíos llevar tzitzit en las esquinas de prendas de cuatro esquinas — "
    "un recordatorio físico constante de todos los mandamientos de D-s.\n\n"
    "Qué son los tzitzit:\n"
    "Los tzitzit (צִיצִית — flecos o borlas) son cuerdas anudadas en cada esquina de una prenda de cuatro esquinas. "
    "Cada juego tiene 8 hilos y 5 nudos. Un famoso derash (Bamidbar Rabá) vincula la gematría de tzitzit (600) "
    "más 8 hilos y 5 nudos con 613 — recordatorio simbólico de las mitzvot, no un cálculo halájico.\n\n"
    "Fuente en la Torá:\n"
    "«Habla a los hijos de Israel y diles que hagan flecos en las esquinas de sus vestidos… para que los miren "
    "y recuerden todos los mandamientos de D-s» (Números 15:38-39).\n\n"
    "Cómo observarlo:\n"
    "La mayoría de los hombres observantes llevan un talit katan bajo la camisa todo el día. En Shajarit se usa "
    "un talit gadol sobre la ropa.\n\n"
    "Para empezar:\n"
    "• Compra un talit katan en cualquier tienda de judaica o en línea\n"
    "• Pide a tu rav que te muestre cómo ponértelo y decir la berajá\n"
    "• Revisa los tzitzit periódicamente — si se rompe un hilo, pueden quedar pasul"
)
_TZITZIT_LONG_FR = (
    "La Torah ordonne aux hommes juifs de porter des tsitsit aux coins de vêtements à quatre coins — "
    "rappel physique constant de tous les commandements de D.ieu.\n\n"
    "Ce que sont les tsitsit :\n"
    "Les tsitsit (צִיצִית — franges ou glands) sont des cordes nouées à chaque coin d'un vêtement à quatre coins. "
    "Chaque jeu a 8 fils et 5 nœuds. Un célèbre derash (Bamidbar Rabba) relie la guematria de tsitsit (600) "
    "plus 8 fils et 5 nœuds à 613 — rappel symbolique des mitsvot, pas un calcul halakhique.\n\n"
    "Source torahique :\n"
    "« Parle aux enfants d'Israël et dis-leur de faire des franges aux coins de leurs vêtements… afin que vous "
    "les regardiez et vous souveniez de tous les commandements de D.ieu » (Nombres 15:38-39).\n\n"
    "Comment l'observer :\n"
    "La plupart des hommes observants portent un talit katan sous la chemise toute la journée. À Shaharit, "
    "un talit gadol est porté par-dessus les vêtements.\n\n"
    "Pour commencer :\n"
    "• Achetez un talit katan dans une boutique judaïca ou en ligne\n"
    "• Demande à ton rav de vous montrer comment le porter et dire la bénédiction\n"
    "• Vérifiez vos tsitsit régulièrement — un fil cassé peut les invalider"
)
k = key_by_prefix["The Torah commands Jewish men to wear fringes"]
ES[k] = _TZITZIT_LONG_ES
FR[k] = _TZITZIT_LONG_FR

_EVENING_SHEMA_ES = (
    "La Torá ordena recitar el Shemá «cuando te acuestes» — es decir, por la noche. "
    "Es una obligación separada a nivel de Torá, distinta del Shemá de la mañana.\n\n"
    "Qué es:\n"
    "Los mismos tres párrafos de la Torá que en la mañana (véase la sección de oración matutina), "
    "pero de noche con berajot distintas alrededor.\n\n"
    "Las berajot alrededor del Shemá vespertino:\n"
    "• Antes: berajá sobre la alternancia de día y noche (Maariv Aravim) y sobre el amor eterno de D-s "
    "por Israel (Ahavat Olam)\n"
    "• Después: berajá que afirma el Éxodo de Egipto (Emet Ve'Emunah) y oraciones de protección durante la noche\n\n"
    "Momento:\n"
    "Desde el anochecer (tzeit hakojavim — cuando aparecen tres estrellas medias) hasta el amanecer. "
    "Idealmente antes de la medianoche halájica (jatzot halaila). La app muestra la ventana actual.\n\n"
    "Después del atardecer (shkijá):\n"
    "Maariv puede comenzar al atardecer, pero la obligación bíblica de recitar el Shemá de noche aún no ha comenzado. "
    "Si rezas Maariv temprano, deberás repetir el Shemá más tarde (al anochecer / tzeit hakojavim).\n\n"
    "Nota:\n"
    "El Shemá vespertino (como parte de Maariv) es distinto del Shemá al acostarse (Kriat Shema al HaMitá). "
    "Ambos son necesarios."
)
_EVENING_SHEMA_FR = (
    "La Torah ordonne de dire le Chemâ « quand tu te couches » — c'est-à-dire le soir. "
    "C'est une obligation distincte au niveau torahique, séparée du Chemâ du matin.\n\n"
    "Ce que c'est :\n"
    "Les mêmes trois paragraphes torahiques que le matin (voir la section prière du matin), "
    "mais la nuit avec des bénédictions différentes autour.\n\n"
    "Les bénédictions autour du Chemâ du soir :\n"
    "• Avant : bénédiction sur l'alternance du jour et de la nuit (Maariv Aravim) et sur l'amour éternel "
    "de D.ieu pour Israël (Ahavat Olam)\n"
    "• Après : bénédiction affirmant la Sortie d'Égypte (Emet Ve'Emunah) et prières de protection pendant la nuit\n\n"
    "Moment :\n"
    "De la tombée de la nuit (tzeit hakokhavim — quand trois étoiles moyennes apparaissent) jusqu'à l'aube. "
    "Idéalement avant minuit halakhique (hatzot halaila). L'application affiche la fenêtre actuelle.\n\n"
    "Après le coucher du soleil (shkia) :\n"
    "Maariv peut commencer au coucher du soleil, mais l'obligation biblique de réciter le Chemâ de nuit n'a pas encore "
    "commencé. Si vous priez Maariv tôt, vous devrez répéter le Chemâ plus tard (à la tombée de la nuit / tzeit hakokhavim).\n\n"
    "Note :\n"
    "Le Chemâ du soir (dans Maariv) est distinct du Chemâ au coucher (Kriat Shema al HaMitah). Les deux sont requis."
)
k = key_by_prefix["The Torah commands the Shema to be said"]
ES[k] = _EVENING_SHEMA_ES
FR[k] = _EVENING_SHEMA_FR

# Marketing fluff key — same native explainer as the proper Tefillin article.
k = key_by_prefix["Learn about the ultimate spiritual"]
k_tefillin = key_by_prefix["Tefillin are two small black leather boxes"]
ES[k] = ES[k_tefillin]
FR[k] = FR[k_tefillin]

# --- Melave Malka / Simchas Yom Tov / mitzvah glossary ---
k = key_by_prefix["Melave Malka (מְלַוֶּה מַלְכָּה) means"]
ES[k] = (
    "Melave Malka (מְלַוֶּה מַלְכָּה) significa «acompañar a la Reina» — comida del sábado por la noche para despedir a la Reina del Shabat "
    "como a un invitado querido (Talmud Shabat 119b). También se llama Seudat David HaMelech: el rey David celebraba cada Motzei Shabat "
    "tras saber que moriría en Shabat (Shabat 30a).\n\n"
    "Cuándo:\nDespués de Havdalá, desde tzeit hasta el amanecer del domingo. Hombres y mujeres; todos los nusajim.\n\n"
    "Qué hacer:\n"
    "• Extender mantel limpio y poner la mesa — incluso si comes poco\n"
    "• Comer en la mesa. Lo ideal: pan con netilat yadayim y hamotzi. Si no puedes con pan, cumple con pastel u otra porción significativa\n"
    "• Cuando puedas, prepara algo para esta comida, no solo sobras de Shabat\n"
    "• Muchos encienden velas en o cerca de la mesa\n\n"
    "Cuatro velas (opcional — Baal Shem Tov): algunos encienden cuatro velas dedicadas a tzadikim y piden por la semana.\n\n"
    "Si estás muy lleno del Shabat, no estás obligado a forzarte — pero incluso un poco de comida en mesa puesta honra la mitzvá."
)
FR[k] = (
    "Melave Malka (מְלַוֶּה מַלְכָּה) signifie « escorter la Reine » — repas du samedi soir pour accompagner la Reine du Chabbat "
    "(Talmoud Shabbat 119b). Aussi Seoudat David HaMelech.\n\n"
    "Quand : après Havdala, de tzeit à l'aube du dimanche.\n\n"
    "Que faire : nappe propre, repas à table — idéalement pain avec netilat yadayim et hamotzi ; sinon gâteau. "
    "Préparer quelque chose pour ce repas si possible. Bougies optionnelles. "
    "Même un peu de nourriture à table honore la mitsva."
)
k = key_by_prefix["Simchas Yom Tov (שִׂמְחַת יוֹם טוֹב)"]
ES[k] = (
    "Simjat Yom Tov (שִׂמְחַת יוֹם טוֹב) — mitzvá de regocijarse en la festividad:\n"
    "La Torá manda «V'samajta be'jagejá» — «Te regocijarás en tu festividad» (Devar. 16:14). "
    "La alegría en Yom Tov es mitzvá para todos. La halajá reconoce que la felicidad es personal — "
    "el jefe de familia ayuda a cada miembro a regocijarse según lo que le trae alegría (Pesajim 109a; Shuljan Aruj O.J. 529:2).\n\n"
    "Preparar antes del hag:\n"
    "• Esposa — ropa o joyas l'fi mamono: el Shuljan Aruj obliga al marido a comprar ropa o joyas nuevas para Yom Tov "
    "según su capacidad económica — sin endeudarse\n"
    "• Hijos — golosinas que traigan alegría: granos tostados y nueces en el Talmud; hoy dulces y juguetes apropiados\n"
    "• Hombres — carne y vino en comidas festivas de Yom Tov\n\n"
    "Condición del Rambam (Hilchot Yom Tov 6:18):\n"
    "Al festejar en casa, también proveer para pobres, viudas y huérfanos. "
    "Sin ayudar a los necesitados, no es «alegría de mitzvá» sino «alegría del estómago»."
)
FR[k] = (
    "Sim'hat Yom Tov (שִׂמְחַת יוֹם טוֹב) — mitsva de se réjouir à la fête :\n"
    "« V'samakhta be'hagekha » (Devarim 16:14). La joie est personnelle — chaque membre de la famille selon ce qui lui plaît.\n\n"
    "Avant la fête : vêtements ou bijoux pour l'épouse (selon moyens), friandises pour les enfants, viande et vin pour les hommes.\n\n"
    "Rambam : sans aider les pauvres, ce n'est pas « joie de mitsva » mais « joie de l'estomac »."
)
k = key_by_prefix["mitzvah — At its core, a mitzvah is a command"]
ES[k] = (
    "mitzvá — En esencia, una mitzvá es un mandamiento de Hashem que conecta directamente contigo y con Él. "
    "La palabra evoca «atar» — cada mitzvá te une a lo Divino. "
    "Incluye obligaciones de Torá y oración formal, pero también momentos cotidianos: "
    "una berajá sobre el vino o una conversación sincera con Hashem. "
    "Una mitzvá transforma un momento físico ordinario en relación con tu Creador."
)
FR[k] = (
    "mitsva — Au fond, un commandement de Hachem qui vous relie à Lui. "
    "Le mot évoque « lier » — chaque mitsva vous connecte au Divin. "
    "Obligations de Torah, prière, mais aussi bénédiction sur le vin ou parole sincère avec Hachem."
)
_BESAMIM_ES = (
    "Las besamim son especias fragantes que se huelen en Havdalá el sábado por la noche — "
    "consuelan el alma cuando la neshama yeteira extra se retira al volver la vida entre semana. "
    "La berajá es Borei minei besamim. Clavos, mirto o especias dulces son comunes. "
    "Yaknehaz omite besamim cuando Shabat pasa a Yom Tov — la alegría de la festividad ya consuela el alma, "
    "así que las especias son halájicamente innecesarias."
)
_BESAMIM_FR = (
    "Les besamim sont des épices parfumées senties à Havdala le samedi soir — "
    "réconfortent l'âme quand la neshama yeteira supplémentaire se retire. "
    "Bénédiction : Borei minei besamim. Clous de girofle, myrte ou épices douces. "
    "Yaknehaz omet les besamim quand Chabbat enchaîne sur Yom Tov — la joie de la fête suffit."
)
k = key_by_prefix["Besamim are fragrant spices smelled during Havdalah"]
ES[k] = _BESAMIM_ES
FR[k] = _BESAMIM_FR
k = key_by_prefix["besamim — Besamim are fragrant spices"]
ES[k] = "besamim — " + _BESAMIM_ES
FR[k] = "besamim — " + _BESAMIM_FR

_MITZVAH_CORE_ES = (
    "mitzvá — En esencia, una mitzvá es un mandamiento de Hashem que conecta directamente contigo y con Él. "
    "La palabra evoca «atar» — cada mitzvá te une a lo Divino. "
    "Incluye obligaciones de Torá y oración formal, pero también momentos cotidianos: "
    "una berajá sobre el vino o una conversación sincera con Hashem. "
    "Una mitzvá transforma un momento físico ordinario en relación con tu Creador."
)
_MITZVAH_CORE_FR = (
    "mitsva — Au fond, un commandement de Hachem qui vous relie à Lui. "
    "Le mot évoque « lier » — chaque mitsva vous connecte au Divin. "
    "Obligations de Torah, prière, mais aussi bénédiction sur le vin ou parole sincère avec Hachem."
)
k = key_by_prefix["At its core, a mitzvah is a command"]
ES[k] = _MITZVAH_CORE_ES
FR[k] = _MITZVAH_CORE_FR

k = key_by_prefix["A minhag is a binding community"]
ES[k] = (
    "Un minhag es una costumbre vinculante de comunidad o familia arraigada en la vida judía — "
    "no solo «lo que la gente hace». Puede moldear el texto de oración (nusaj), prácticas de luto, "
    "rigores de Pesaj y alegría en festividades. Cuando un minhag tiene aprobación rabínica en una comunidad, "
    "suele tener casi el peso de la ley para esa comunidad. Si eres nuevo o entre comunidades, pregunta a tu rav qué minhagim seguir."
)
FR[k] = (
    "Un minhag est une coutume communautaire ou familiale enracinée dans la vie juive — "
    "pas seulement « ce que les gens font ». Il façonne le nusach, le deuil, Pessah et la joie des fêtes. "
    "Avec approbation rabbinique, il pèse presque comme la loi. Demande à ton rav quels minhagim suivre."
)
k = key_by_prefix["Minhag — A minhag is a binding"]
ES[k] = (
    "Minhag — Costumbre vinculante de comunidad o familia; moldea oración (nusaj), luto, Pesaj y alegría festiva. "
    "Con aprobación rabínica, casi peso de ley. Pregunta a tu rav qué minhagim seguir."
)
FR[k] = (
    "Minhag — Coutume communautaire ou familiale ; nusach, deuil, Pessah, joie des fêtes. "
    "Approbation rabbinique : poids quasi légal. Demande à ton rav quels minhagim suivre."
)
k = key_by_prefix["Emunah is faith that G-d exists"]
ES[k] = (
    "Emuná es la fe de que D-s existe, es Uno y guía la historia y tu vida con propósito. "
    "No es negar la dificultad — muchos salmos muestran justos en lucha — sino compromiso con que hay significado aunque no lo veamos. "
    "Fortalecer la emuná viene del estudio de Torá, mitzvot y recordar bondades recibidas."
)
FR[k] = (
    "Emouna : foi que D.ieu existe, est Un et guide l'histoire et ta vie avec un but. "
    "Pas nier la difficulté — engagement qu'il y a du sens même invisible. "
    "Torah, mitsvot et souvenirs de bontés reçues la renforcent."
)
k = key_by_prefix["Emunah — Emunah is faith"]
ES[k] = (
    "emuná — Fe en que D-s existe, es Uno y guía tu vida con propósito. "
    "Estudio de Torá, mitzvot y gratitud la fortalecen."
)
FR[k] = (
    "emouna — Foi en D.ieu Un qui guide ta vie. Torah, mitsvot et gratitude la renforcent."
)
_HAMOTZI_ES = (
    "Hamotzi es la berajá sobre el pan — «Que saca el pan de la tierra» — que inicia una comida "
    "que requiere bentching. Lávate las manos (netilat yadayim) antes. Si eres anfitrión, corta "
    "el pan para los demás después de bendecir. En Pesaj — sobre matzá; en Shabat — lejem mishné, "
    "dos hogazas enteras."
)
_HAMOTZI_FR = (
    "Hamotzi est la bénédiction sur le pain — « Qui fait sortir le pain de la terre » — qui ouvre "
    "un repas nécessitant le bentching. Lave-toi les mains (netilat yadayim) avant. Si tu es hôte, "
    "coupe le pain pour les autres après ta bénédiction. À Pessah — sur matsa ; à Chabbat — lehem mishneh, "
    "deux pains entiers."
)
_KADDISH_ES = (
    "El Kaddish santifica el Nombre de D-s en arameo — exaltación pública de D-s. Los dolientes lo recitan "
    "once meses por padres y treinta días por otros parientes cercanos. Requiere minyán. Hay medio-Kaddish, "
    "Kaddish completo y Kaddish de los rabinos en la liturgia. Conecta a los vivos con el mérito del difunto."
)
_KADDISH_FR = (
    "Le Kaddish sanctifie le Nom de D. en araméen — exaltation publique de D. Les endeuillés le récitent "
    "onze mois pour les parents et trente jours pour d'autres proches. Il faut un minyan. Demi-Kaddish, "
    "Kaddish entier et Kaddish des rabbins dans la liturgie. Il relie les vivants au mérite du défunt."
)
_RETZEI_ES = (
    "Retzei pide a D-s que se complazca en nuestro descanso y restaure el Templo y la dinastía davídica — "
    "se inserta en la tercera berajá del bentching en Shabat y Yom Tov. Si olvidaste Retzei en las dos "
    "primeras comidas de Shabat, generalmente repites el bentching. En seudá shlishit (tercera comida) "
    "no repites (Shulján Aruj O.J. 188:8). Pregunta a tu rav si tienes dudas."
)
_RETZEI_FR = (
    "Retzei demande à D. de prendre plaisir dans notre repos et de restaurer le Temple et la dynastie davidique — "
    "insert dans la troisième bénédiction du bentching à Chabbat et Yom Tov. Si tu oublies Retzei aux deux "
    "premiers repas de Chabbat, tu répètes généralement le bentching. À la seouda shlishit (troisième repas) "
    "tu ne répètes pas (Choulhan Aroukh O.H. 188:8). Demande à ton rav si tu n'es pas sûr."
)
_MACHZOR_ES = (
    "Un majzor es un sidur para fiestas y Yamim Noraim — Rosh Hashaná, Yom Kipur, Pesaj, Sucot y Shavuot. "
    "Incluye textos que no están en el sidur diario. Para Rosh Hashaná y Yom Kipur necesitas majzor "
    "para el orden correcto de la oración."
)
_MACHZOR_FR = (
    "Un mahzor est un livre de prières pour les fêtes et les Yamim Noraim — Roch Hachana, Yom Kippour, "
    "Pessah, Soukkot et Chavouot. Il contient des textes absents du siddour quotidien. Pour Roch Hachana "
    "et Yom Kippour, le mahzor est nécessaire pour l'ordre correct des prières."
)
k = key_by_prefix["Hamotzi is the blessing over bread"]
ES[k] = _HAMOTZI_ES
FR[k] = _HAMOTZI_FR
k = key_by_prefix["Hamotzi — Hamotzi is the blessing"]
ES[k] = f"Hamotzi — {_HAMOTZI_ES}"
FR[k] = f"Hamotzi — {_HAMOTZI_FR}"
k = key_by_prefix["Kaddish sanctifies G-d's Name in Aramaic"]
ES[k] = _KADDISH_ES
FR[k] = _KADDISH_FR
k = key_by_prefix["Kaddish — Kaddish sanctifies G-d's Name"]
ES[k] = f"Kaddish — {_KADDISH_ES}"
FR[k] = f"Kaddish — {_KADDISH_FR}"
k = key_by_prefix["Retzei asks G-d to be pleased with our rest"]
ES[k] = _RETZEI_ES
FR[k] = _RETZEI_FR
k = key_by_prefix["Retzei — Retzei asks G-d to be pleased"]
ES[k] = f"Retzei — {_RETZEI_ES}"
FR[k] = f"Retzei — {_RETZEI_FR}"
k = key_by_prefix["A machzor is a prayer book for the Jewish festivals"]
ES[k] = _MACHZOR_ES
FR[k] = _MACHZOR_FR
k = key_by_prefix["machzor — A machzor is a prayer book"]
ES[k] = f"machzor — {_MACHZOR_ES}"
FR[k] = f"mahzor — {_MACHZOR_FR}"
_AMEN_ES = (
    "Amen afirma una berajá que otro recitó: «que así sea». "
    "El Talmud elogia más a quien responde Amen que a quien bendijo. "
    "No debe apresurarse ni murmurarse: oportunidad constante de mitzvá en sinagoga y casa. "
    "Literalmente, acrónimo de «D-s, Rey fiel» — Keil Melej Ne'eman."
)
_AMEN_FR = (
    "Amen affirme une bénédiction d'autrui : « qu'il en soit ainsi ». "
    "Le Talmud loue plus celui qui répond Amen. "
    "Acronyme de « D.ieu, Roi fidèle » — Keil Melech Ne'eman."
)
k = key_by_prefix["Amen affirms a bracha someone"]
ES[k] = _AMEN_ES
FR[k] = _AMEN_FR
k = key_by_prefix["Amen — Amen affirms a bracha"]
ES[k] = "Amen — " + _AMEN_ES
FR[k] = "Amen — " + _AMEN_FR
k = key_by_prefix["Chassidut — Chassidut (Chasidic philosophy)"]
ES[k] = (
    "Jasidut — filosofía jasídica: servir a D-s con alegría, sinceridad y conciencia de que chispas divinas llenan la vida. "
    "Fundada por el Baal Shem Tov; se extendió por cortes como Jabad, Breslov y Satmar. "
    "El Tania es texto central para estudio diario."
)
FR[k] = (
    "'Hassidout — philosophie 'hassidique : servir D.ieu avec joie et conscience des étincelles divines. "
    "Baal Shem Tov ; cours comme 'Habad, Breslov, Satmar. Le Tanya est central."
)
k = key_by_prefix["A mezuzah is the klaf"]
ES[k] = (
    "La mezuzá es el klaf — pergamino manuscrito con Shemá y Va'ahavtá — en marcos de puertas de habitaciones habitables. "
    "El estuche protege el rollo; la mitzvá es el pergamino. Revísalo con un sofer cada pocos años. "
    "Tocar y besar la mezuzá al entrar recuerda que D-s cuida el hogar."
)
FR[k] = (
    "La mezouza est le klaf — parchemin manuscrit avec Chemâ et Va'ahavta — sur les montants des pièces habitées. "
    "Le boîtier protège le rouleau. Fais vérifier par un sofer tous les quelques années. "
    "Toucher et embrasser la mezouza rappelle que D.ieu veille sur la maison."
)
k = key_by_prefix["Bitachon is trust in G-d"]
ES[k] = (
    "Bitajón es confianza en D-s — creer que Él provee lo que necesitas y que la dificultad puede tener propósito aunque no lo veas. "
    "Relacionado con emuná pero enfatiza calma en la preocupación diaria. Musar y Jasidut enseñan bitajón práctico."
)
FR[k] = (
    "Bitachon : confiance en D.ieu — Il pourvoit à vos besoins ; l'épreuve peut avoir un sens caché. "
    "Lié à l'emouna ; le moussar et la 'hassidout l'enseignent au quotidien."
)
k = key_by_prefix["Brit milah (bris) is the covenant"]
ES[k] = (
    "Brit milá (bris) es el pacto de circuncisión al octavo día para un niño judío sano, con comida festiva (seudat mitzvá). "
    "Marca la entrada en el pacto judío con D-s. La realiza un mohel entrenado. "
    "Si hay problemas de salud, se pospone — no hay problema si se hace cuando sea posible."
)
FR[k] = (
    "Brit mila (bris) : alliance de la circoncision au huitième jour pour un garçon juif en bonne santé, avec seoudat mitsva. "
    "Entrée dans l'alliance avec D.ieu ; mohel formé. Report possible pour santé."
)
k = key_by_prefix["Chilul Hashem — Chilul Hashem is desecrating"]
ES[k] = (
    "Jilul Hashem — profanar el Nombre de D-s: conducta que hace ver mal la Torá o a los judíos, especialmente en público. "
    "Fraude, ira o hipocresía de alguien visiblemente judío daña a todo el pueblo. "
    "Lo opuesto es kidush Hashem — santificar el Nombre con integridad."
)
FR[k] = (
    "Hilloul Hachem — profanation du Nom de D.ieu ; comportement qui déshonore la Torah ou les Juifs en public. "
    "Opposé : kiddoush Hachem par l'intégrité."
)
_DAYEINU_ES = (
    "Dayeinu («habría sido suficiente») es la canción del Seder que lista los regalos del Éxodo — "
    "cada paso solo habría justificado gratitud. Cantarla enseña a notar la bondad acumulativa de D-s. "
    "Aparece después de Maguid y antes del Hallel."
)
_DAYEINU_FR = (
    "Dayeinu (« il nous aurait suffi ») : chant du Seder listant les dons de la Sortie d'Égypte. "
    "Chaque étape méritait gratitude. Après Maguid, avant Hallel."
)
k = key_by_prefix['Dayeinu ("it would have been enough") is the Seder']
ES[k] = _DAYEINU_ES
FR[k] = _DAYEINU_FR
k = key_by_prefix['Dayeinu — Dayeinu ("it would have been enough")']
ES[k] = "Dayeinu — " + _DAYEINU_ES
FR[k] = "Dayeinu — " + _DAYEINU_FR
k = key_by_prefix["L'chatchila means the ideal way"]
ES[k] = (
    "L'jatchila es la forma ideal de cumplir una mitzvá desde el principio — lo que debes planear hacer. "
    "Bedieved es después del hecho, cuando algo salió mal y la halajá puede ofrecer corrección o leniencia. "
    "Ambos términos ayudan a leer guías: «l'jatchila usa copa; bedieved si olvidaste, algunos permiten…»"
)
FR[k] = (
    "L'hat'hlila, c'est la façon idéale d'accomplir une mitsva dès le départ — ce que tu dois planifier. "
    "Bedieved, c'est après coup, quand quelque chose a mal tourné et que la halakha offre une correction ou une "
    "clémence. Connaître les deux termes t'aide à lire les guides : « l'hat'hlila avec un gobelet ; bedieved si "
    "tu as oublié, certains permettent… »"
)
_lchatchila_key = key_by_prefix["L'chatchila means the ideal way"]
k = key_by_prefix["l'chatchila — L'chatchila means"]
ES[k] = f"l'jatchila — {ES[_lchatchila_key]}"
FR[k] = f"l'chatchila — {FR[_lchatchila_key]}"

_ONEG_ES = (
    "Oneg Shabat es deleitarse en Shabat — buena comida, descanso, estudio de Torá, canto y tiempo con familia o "
    "invitados. Es una mitzvá positiva, no solo evitar melajá. El Talmud critica quien ayuna o se priva en Shabat sin "
    "necesidad. Planifica comidas y ambiente antes de Shabat para que el oneg llegue sin estrés de último momento."
)
_ONEG_FR = (
    "Oneg Chabbat, c'est se réjouir de Chabbat — bon repas, repos, étude de Torah, chant et temps avec famille ou "
    "invités. C'est une mitsva positive, pas seulement éviter la mélakha. Le Talmud critique ceux qui jeûnent ou se "
    "privent Chabbat sans besoin. Planifie repas et ambiance avant Chabbat pour que l'oneg arrive sans stress de "
    "dernière minute."
)
k = key_by_prefix["Oneg Shabbat is delighting in Shabbat"]
ES[k] = _ONEG_ES
FR[k] = _ONEG_FR
k = key_by_prefix["Oneg Shabbat — Oneg Shabbat is delighting"]
ES[k] = f"Oneg Shabat — {_ONEG_ES}"
FR[k] = f"Oneg Chabbat — {_ONEG_FR}"

_PIRSUMEI_ES = (
    "Pirsumei nisa significa publicitar el milagro — coloca la menorá de Janucá donde los transeúntes vean las "
    "luces. El milagro del aceite se proclama a la calle. En casa, muchos encienden junto a la ventana. "
    "No uses las velas de la mitzvá para leer — usa el shamash."
)
_PIRSUMEI_FR = (
    "Pirsoumei nissa, c'est publiciser le miracle — place la menorah de Hanoucca où les passants voient les lumières. "
    "Le miracle de l'huile est proclamé dans la rue. Chez toi, beaucoup allument près de la fenêtre. "
    "N'utilise pas les bougies de mitzvah pour lire — utilise le shamash."
)
k = key_by_prefix["Pirsumei nisa means publicizing"]
ES[k] = _PIRSUMEI_ES
FR[k] = _PIRSUMEI_FR
k = key_by_prefix["pirsumei nisa — Pirsumei nisa means"]
ES[k] = f"pirsumei nisa — {_PIRSUMEI_ES}"
FR[k] = f"pirsumei nisa — {_PIRSUMEI_FR}"

_DEVICE_SHABBAT_FR = (
    "Range ton appareil et garde Chabbat. Repose-toi, prie, étudie la Torah et profite de la famille et de la "
    "communauté. La mélakha (travail interdit) s'applique le Chabbat, y compris la plupart de l'usage du téléphone "
    "et des appareils."
)
_DEVICE_YOMTOV_FR = (
    "Aujourd'hui c'est $holidayName (Yom Tov — jour de fête). Range ton appareil et garde le jour saint. "
    "La mélakha s'applique le Yom Tov comme Chabbat."
)
_DEVICE_SHABBAT_ES = (
    "Guarda tu dispositivo y observa Shabat. Descansa, ora, aprende Torá y disfruta con familia y comunidad. "
    "Melajá (trabajo prohibido) aplica en Shabat, incluido la mayor parte del uso del teléfono y dispositivos."
)
_DEVICE_YOMTOV_ES = (
    "Hoy es $holidayName (Yom Tov — día festivo). Guarda tu dispositivo y mantén el día santo. "
    "Melajá aplica en Yom Tov igual que en Shabat."
)
k = key_by_prefix["Please put away your device and keep Shabbat"]
ES[k] = _DEVICE_SHABBAT_ES
FR[k] = _DEVICE_SHABBAT_FR
k = key_by_prefix["Today is $holidayName (Yom Tov — a festival day). Please put away your device"]
ES[k] = _DEVICE_YOMTOV_ES
FR[k] = _DEVICE_YOMTOV_FR

_SHABBAT_REFRAIN_FR = (
    "Abstiens-toi d'utiliser téléphones, ordinateurs ou tout appareil électronique pendant Chabbat."
)
_SHABBAT_REFRAIN_ES = (
    "Abstente de usar teléfonos, ordenadores o cualquier dispositivo electrónico durante Shabat."
)
_SHABBAT_PREP_FR = (
    "Chabbat va commencer. Termine ce que tu fais, éteins ton téléphone et prépare-toi à accueillir le jour saint.\n\n"
    "Nos Sages enseignent que Chabbat est un goût du monde à venir, et celui qui garde Chabbat selon ses lois reçoit "
    "le pardon et une récompense au-delà de la mesure (Chabbat 25b ; Shabbat 118a)."
)
_SHABBAT_PREP_ES = (
    "Shabat está por comenzar. Termina lo que estás haciendo, apaga tu teléfono y prepárate para recibir el día santo.\n\n"
    "Nuestros Sabios enseñan que Shabat es un anticipo del Mundo Venidero, y quien guarda Shabat según sus leyes recibe "
    "perdón y una recompensa sin medida (Shabat 25b; Shabat 118a)."
)
k = key_by_prefix["Please refrain from using phones, computers, or any electronics during Shabbat"]
ES[k] = _SHABBAT_REFRAIN_ES
FR[k] = _SHABBAT_REFRAIN_FR
k = key_by_prefix["Shabbat is about to begin. Please finish what you are doing"]
ES[k] = _SHABBAT_PREP_ES
FR[k] = _SHABBAT_PREP_FR
k = key_by_prefix['Gebrochts ("broken") is matzah']
ES[k] = (
    "Gebrochts («roto») es matzá que entró en contacto con líquido tras hornear — los kneidlach son el ejemplo clásico. "
    "Algunas comunidades jasídicas no comen gebrochts en Pesaj; otras comen kneidlach libremente. "
    "Como invitado, sigue las reglas del anfitrión; en casa, el psak de tu rav."
)
FR[k] = (
    "Gebrochts (« cassé ») : matsa en contact avec un liquide après cuisson — kneidlach classiques. "
    "Certaines communautés 'hassidiques s'abstiennent à Pessah ; d'autres mangent librement. "
    "Chez l'hôte : ses règles ; chez toi : psak du rav."
)
_HAMAPIL_SHORT_ES = (
    "Birkat Hamapil es la berajá antes de dormir, encomendando el alma a D-s. "
    "Dila como últimas palabras antes de cerrar los ojos. "
    "Tras recitarla, no comas, bebas ni hables hasta dormirte; repetir Shemá en voz baja al adormecer está permitido. "
    "Algunos la omiten si saben que no dormirán pronto. Se complementa con Shemá al acostarse."
)
_HAMAPIL_SHORT_FR = (
    "Birkat Hamapil : bénédiction avant le sommeil, confiant l'âme à D.ieu. "
    "Dites-la en dernier avant de fermer les yeux. Pas manger, boire ni parler jusqu'au sommeil. "
    "Avec le Chemâ au coucher."
)
k = key_by_prefix["Birchat Hamapil (Hamapil) is the blessing"]
ES[k] = _HAMAPIL_SHORT_ES
FR[k] = _HAMAPIL_SHORT_FR
k = key_by_prefix["birchat hamapil — Birchat Hamapil"]
ES[k] = "birchat hamapil — " + _HAMAPIL_SHORT_ES
FR[k] = "birchat hamapil — " + _HAMAPIL_SHORT_FR

_ASHER_YATZAR_ES = (
    "Después del baño, lavamos las manos y decimos Asher Yatzar cada vez que terminas.\n\n"
    "Cuándo:\n"
    "• Normalmente: tras terminar y lavarte las manos.\n"
    "• Si estás enfermo y puede volver a necesitar el baño de inmediato, espera hasta estar razonablemente seguro de haber terminado esa ronda.\n\n"
    "Qué es:\n"
    "Asher Yatzar (אֲשֶׁר יָצַר — «quien formó») agradece a D-s el diseño del cuerpo humano — aberturas y cierres en equilibrio.\n\n"
    "Por qué importa:\n"
    "La medicina confirma: una válvula bloqueada o un conducto obstruido es emergencia. Cada función normal es milagro.\n\n"
    "Cómo:\n"
    "1. Lavar manos (sin berajá salvo netilat yadayim matutina)\n"
    "2. Decir Asher Yatzar (~20 segundos)\n\n"
    "Texto en sidur o app → Menú → Berajot."
)
_ASHER_YATZAR_FR = (
    "Après les toilettes, on se lave les mains et on dit Asher Yatzar à chaque fois.\n\n"
    "Quand : normalement après avoir fini ; si malade et risque de retour immédiat, attendre la fin de la « ronde ».\n\n"
    "Bénédiction de gratitude pour le corps — ouvertures et fermetures en équilibre.\n\n"
    "Comment : se laver les mains, dire Asher Yatzar (~20 s). Texte dans le siddour ou l'app."
)
k = key_by_prefix["After using the bathroom, we wash our hands"]
ES[k] = _ASHER_YATZAR_ES
FR[k] = _ASHER_YATZAR_FR

_SUKKAH_ES = (
    "Construir una sucá (סֻכָּה) es mitzvá — muchos empiezan tras Yom Kipur, de teshuvá a simjá.\n\n"
    "Requisitos básicos (Shulján Aruj O.J. 633–635):\n"
    "• Paredes: al menos dos completas y parte de una tercera. Estables, sin ondear con brisa normal (O.J. 630:10).\n"
    "• Sucot de tela: atar bien o cuerdas horizontales cada menos de 3 tefajim (lavud) — consulta a tu rav.\n"
    "• Tamaño: cabe una mesa y un adulto sentado con cabeza y cuerpo dentro (~7×7 tefajim mínimo).\n"
    "• Sajáj: material vegetal del suelo, separado — no metal ni plástico ni objetos manufacturados con tumá.\n"
    "• Sombra: más sombra que sol en el suelo; se pueden ver estrellas.\n\n"
    "Pasos: elegir lugar sin techo que bloquee; paredes primero; sajáj al final.\n"
    "Primera noche: kezayit de pan en la sucá con leishev ba-sucá (hombres; mujeres según minhag).\n"
    "Lluvia primera noche: askhenazí — esperar hasta una hora; sefardí — según lluvia, consultar rav.\n"
    "Durante Sucot: comer pan y dormir en sucá cuando sea posible."
)
_SUKKAH_FR = (
    "Construire une soucca (סֻכָּה) est une mitsva — beaucoup commencent après Yom Kippour.\n\n"
    "Murs stables (deux entiers + partie du troisième), schakh végétal détaché, plus d'ombre que de soleil.\n"
    "Première nuit : kezayit de pain avec leishev ba-soucca. Pluie : coutumes ashkénaze et séfarade — demande à ton rav."
)
k = key_by_prefix["Building a sukkah (סֻכָּה) is a mitzvah"]
ES[k] = _SUKKAH_ES
FR[k] = _SUKKAH_FR

_DIASPORA_ES = (
    "La Diáspora (Galut) es la vida judía fuera de la Tierra de Israel. "
    "Durante siglos las comunidades desarrollaron minhagim locales permaneciendo fieles a la Torá. "
    "Fuera de Israel los calendarios festivos suelen añadir un día extra de Yom Tov. "
    "La aliyah a Israel es mitzvá para muchos; las comunidades de la Diáspora siguen plenas de vida."
)
_DIASPORA_FR = (
    "La Diaspora (Galout) : vie juive hors d'Israël. Minhagim locaux, jour de fête supplémentaire hors d'Israël. "
    "Alyah vers Israël est une mitsva pour beaucoup ; communautés diasporiques vivantes."
)
k = key_by_prefix["Diaspora — The Diaspora (Galut) means"]
ES[k] = "Diáspora — " + _DIASPORA_ES
FR[k] = "Diaspora — " + _DIASPORA_FR
k = key_by_prefix["The Diaspora (Galut) means Jewish life"]
ES[k] = _DIASPORA_ES
FR[k] = _DIASPORA_FR

_ERUV_TAV_ES = (
    "Eruv tavshilin es comida simbólica en Erev Yom Tov cuando Shabat sigue al día siguiente. "
    "Reservas pan horneado (jalá o matzá) y comida cocida (carne, pescado o huevo duro sin pelar), "
    "recitas la berajá y declaración del sidur en un idioma que entiendes, "
    "y así puedes seguir cocinando en Yom Tov para Shabat — de otro modo estaría prohibido cocinar de un día santo para el siguiente. "
    "Guarda las comidas designadas en lugar seguro y etiquetado; si se comen antes de terminar la cocina del viernes, el eruv queda anulado."
)
_ERUV_TAV_FR = (
    "Erouv tavshilin : repas symbolique à Erev Yom Tov quand Chabbat suit. "
    "Pain cuit + plat cuit, bénédiction et déclaration — permet de cuisiner à Yom Tov pour Chabbat. "
    "Conserver les aliments désignés jusqu'à la fin de la cuisine du vendredi."
)
k = key_by_prefix["Eruv tavshilin is a symbolic meal"]
ES[k] = _ERUV_TAV_ES
FR[k] = _ERUV_TAV_FR
k = key_by_prefix["Eruv tavshilin — Eruv tavshilin is"]
ES[k] = "Eruv tavshilin — " + _ERUV_TAV_ES
FR[k] = "Eruv tavshilin — " + _ERUV_TAV_FR

k = key_by_prefix["Matana al menat lehachzir"]
ES[k] = (
    "Matana al menat lehachzir (מַתָּנָה עַל מְנָת לְהַחְזִיר) es «regalo con condición de devolverlo». "
    "El receptor es dueño legalmente mientras lo tiene en las manos, luego lo devuelve. "
    "En el primer día de Sucot, si no tienes lulav y etrog, alguien puede prestártelos condicionalmente "
    "para cumplir la obligación toráica de propiedad, y luego los devuelves. "
    "No se usa en mejirat jametz — vender jametz requiere venta absoluta."
)
FR[k] = (
    "Matana al menat lehachzir : « don à condition de rendre ». "
    "Propriété temporaire pour la mitsva du loulav le premier jour de Souccot. "
    "Pas pour la vente du hamets — vente absolue requise."
)

_MATANOT_ES = (
    "Matanot la'evyonim (מתנות לאביונים) ayuda a cada judío a celebrar Purim con comida y alegría (Ester 9:22).\n\n"
    "La mitzvá:\n"
    "• Al menos un regalo a cada uno de dos pobres distintos durante el día de Purim.\n"
    "• Cada regalo debe permitir una comida modesta de Purim — dinero es común.\n\n"
    "Cómo:\n"
    "• Solo de día (no de noche); muchos dan tras la Meguilá diurna.\n"
    "• Idealmente antes de tu seudá de Purim.\n"
    "• Puedes enviar por mensajero o organización de confianza — que llegue ese día.\n"
    "• Si no encuentras receptores, pregunta en tu sinagoga."
)
_MATANOT_FR = (
    "Matanot la'evyonim : cadeaux aux pauvres pour Purim (Esther 9:22).\n"
    "Au moins un don à chacun de deux pauvres distincts le jour de Purim. "
    "De jour seulement ; idéalement avant la seouda. Par messager ou collecte de la synagogue."
)
k = key_by_prefix["Matanot la'evyonim (מתנות לאביונים) helps"]
ES[k] = _MATANOT_ES
FR[k] = _MATANOT_FR

_MISHLOACH_ES = (
    "Mishloach manot (משלוח מנות) — enviar porciones de comida — aumenta amistad y alegría en Purim (Ester 9:19).\n\n"
    "La mitzvá:\n"
    "• Enviar al menos dos alimentos o bebidas distintos listos para comer a un amigo en Purim — un paquete.\n"
    "• Mujeres también obligadas; muchos envían mujer a mujer y hombre a hombre (Rema).\n"
    "• Ejemplos: vino y galletas, fruta y pastel — dos tipos claros.\n\n"
    "Cómo:\n"
    "• Entregar de día antes del atardecer — tú o un mensajero.\n"
    "• Comida lista sin cocinar; identificar remitente y destinatario.\n"
    "• Planifica en Erev Purim para no apurar al final."
)
_MISHLOACH_FR = (
    "Mishloach manot : envoyer au moins deux mets ou boissons prêts à un ami le jour de Pourim. "
    "Femmes aussi obligées. Livrer de jour ; nourriture prête ; étiqueter expéditeur et destinataire."
)
k = key_by_prefix["Mishloach manot (משלוח מנות)"]
ES[k] = _MISHLOACH_ES
FR[k] = _MISHLOACH_FR

k = key_by_prefix["Patur means exempt from a particular"]
ES[k] = (
    "Patur significa exento de una mitzvá en una situación concreta — por ejemplo, enfermo en Yom Kipur del ayuno, "
    "o un niño antes del bar mitzvá. No es «liberado de obligación» moralmente; significa que la obligación no te aplica ahora. "
    "Puede aplicarse otra mitzvá o leniencia en su lugar."
)
FR[k] = (
    "Patur : exempté d'une mitsva dans une situation — malade à Yom Kippour, enfant avant bar mitsva. "
    "L'obligation ne s'applique pas maintenant ; une autre règle ou indulgence peut s'appliquer."
)

_MINCHA_ES = (
    "Minjá es la oración de la tarde — breve y poderosa, a menudo en medio de la jornada laboral.\n\n"
    "Minjá (מִנְחָה) recuerda la ofrenda de grano vespertina del Templo; instituida por Itzjak (Bereshit 24:63 según el Talmud).\n\n"
    "Contenido:\n"
    "• Ashrei (Salmo 145)\n"
    "• Amidá / Shemoneh Esrei\n"
    "• Tachanun en días laborables (omitido en Rosh Jodesh, Yom Tov, Janucá, Purim)\n"
    "• Aleinu\n\n"
    "Idealmente antes del atardecer. Minjá del viernes entra en Shabat. Si falta, tashlumim en Arvit cuando sea posible."
)
_MINCHA_FR = (
    "Min'ha : prière de l'après-midi — brève et puissante, souvent au milieu de la journée.\n\n"
    "Min'ha (מִנְחָה) rappelle l'offrande végétale du Temple ; instituée par Yitshak (Bereshit 24:63 selon le Talmud).\n\n"
    "Contenu :\n"
    "• Ashrei (Psaume 145)\n"
    "• Amida / Shemoneh Esrei\n"
    "• Tachanun en semaine (omis au Rosh 'Hodesh, Yom Tov, 'Hanukah, Pourim et jours de joie)\n"
    "• Aleinu\n\n"
    "Tachanun : lundi et jeudi plus long au Shaharit ; plus court les autres jours. "
    "Idéalement avant le coucher du soleil. Min'ha du vendredi entre dans Chabbat. "
    "Si tu manques Min'ha, tashloumin à Arvit quand c'est possible."
)
k = key_by_prefix["Mincha is the afternoon prayer"]
ES[k] = _MINCHA_ES
FR[k] = _MINCHA_FR

_MISHEYAKIR_ES = (
    "Misheyakir (מִשֶּׁיַּכִּיר) es cuando hay luz natural suficiente para reconocer a un conocido casual a unos dos metros. "
    "Es el momento más temprano para la berajá del talit y las tefilín — a menudo 35–50 minutos tras alot hashajar. "
    "Antes de misheyakir no se cumple la mitzvá aunque haya amanecido. "
    "La Mishná Berurá recomienda esperar, poner talit y tefilín, y luego Shemá y Amidá. "
    "La Guemará (Berajot 14b) compara Shemá sin talit y tefilín con falso testimonio. "
    "Si debes salir muy temprano: Igrot Moshe (O.J. 4:6) permite caso especial — consulta a tu rav."
)
_MISHEYAKIR_FR = (
    "Misheyakir : assez de lumière pour reconnaître un connu à ~2 m. "
    "Heure la plus tôt pour talit et téfilines — souvent 35–50 min après alot hashachar. "
    "Avant misheyakir, la mitsva n'est pas accomplie même après l'aube. "
    "Michna Beroura : attendre, mettre talit et téfilines, puis Shema et Amida. "
    "Guemara (Berakhot 14b) : Shema sans talit et téfilines = faux témoignage. "
    "Cas particulier très tôt : Igrot Moshe O.H. 4:6 — demande à ton rav."
)
k = key_by_prefix["Misheyakir (מִשֶּׁיַּכִּיר) is when"]
ES[k] = _MISHEYAKIR_ES
FR[k] = _MISHEYAKIR_FR

_MELACHA_ES = (
    "Melajá es trabajo transformador prohibido en Shabat — incluye cocinar. "
    "En Yom Tov la mayoría de las 39 melajot siguen prohibidas (escribir, construir, encender fuego nuevo, etc.), "
    "pero la Torá permite ochel nefesh — preparar comida para ese día festivo desde llama preexistente (Shemot 12:16). "
    "No cocines en Yom Tov para el día siguiente sin eruv tavshilin cuando Shabat sigue. En duda, pregunta a tu rav antes de actuar."
)
_MELACHA_FR = (
    "Méla'ha : travail transformateur interdit le Chabbat. À Yom Tov, la plupart des 39 méla'hot restent interdites ; "
    "ochel nefesh permet de cuisiner ce jour-là depuis une flamme existante. "
    "Pas cuisiner pour le lendemain sans erouv tavshilin quand Chabbat suit."
)
k = key_by_prefix["Melacha is transformative labor"]
ES[k] = _MELACHA_ES
FR[k] = _MELACHA_FR
k = key_by_prefix["Melacha — Melacha is transformative"]
ES[k] = "Melajá — " + _MELACHA_ES
FR[k] = "Méla'ha — " + _MELACHA_FR

_OLEH_ES = (
    "El oleh (quien sube) recita las berajot antes y después de la lectura de la Torá en una aliyah. "
    "Inmigrar a Israel también se llama aliyah. "
    "Honrar aliyot respeta a kohanim, leviím, yahrzeit y eventos del ciclo de vida. "
    "Prepara las berajot en hebreo o sigue la transliteración."
)
_OLEH_FR = (
    "L'oleh (celui qui monte) récite les bénédictions avant et après la lecture à l'alyah. "
    "L'immigration en Israël s'appelle aussi alyah. "
    "Préparez les bénédictions en hébreu ou suivez la translittération."
)
k = key_by_prefix["The oleh (one who goes up)"]
ES[k] = _OLEH_ES
FR[k] = _OLEH_FR
k = key_by_prefix["oleh — The oleh (one who goes up)"]
ES[k] = "oleh — " + _OLEH_ES
FR[k] = "oleh — " + _OLEH_FR

_SHEMA_GLOSS_ES = (
    "El Shemá («Escucha, Israel») proclama la unicidad de D-s y nuestro deber de amarlo con todo el corazón, alma y fuerza. "
    "Se recita mañana y noche con berajot. Kriat Shemá tiene horarios específicos — especialmente el Shemá de la mañana antes de la tercera hora halájica. "
    "Es la declaración de fe judía que los niños aprenden primero."
)
_SHEMA_GLOSS_FR = (
    "Le Shema (« Écoute, Israël ») proclame l'unité de D.ieu et notre devoir de L'aimer de tout notre cœur, âme et force. "
    "Récité matin et soir avec les bénédictions. Kriat Shema a des horaires — surtout le matin avant la 3e heure halakhique. "
    "Déclaration de foi que les enfants apprennent en premier."
)
k = key_by_prefix['The Shema ("Hear O Israel")']
ES[k] = _SHEMA_GLOSS_ES
FR[k] = _SHEMA_GLOSS_FR
k = key_by_prefix['Shema — The Shema ("Hear']
ES[k] = "Shema — " + _SHEMA_GLOSS_ES
FR[k] = "Shema — " + _SHEMA_GLOSS_FR

_CHOL_WINE_ES = (
    "Beber vino en Jol hamoed es parte de simjat ha-moed — regocijarse en la festividad (Peninei Halajá 12-10-03). "
    "El Talmud enseña que no hay alegría sin vino; los hombres marcan especialmente simjat Yom Tov con vino.\n\n"
    "No es como las cuatro copas del Seder o Kiddush de Shabat — obligaciones toráicas con revi'it más estricto. "
    "Para esta costumbre opcional de Jol hamoed, muchos poskim sugieren al menos un revi'it leniente (~86 ml según Rav Chaim Naé) en comida festiva. Confirma con tu rav.\n\n"
    "Vino vs jugo de uva: debate real — muchos exigen la cualidad intoxicante del vino; otros son más lenientes. Pregunta a tu rav."
)
_CHOL_WINE_FR = (
    "Boire du vin à Hol hamoed fait partie de la sim'ha — pas comme les quatre coupes du Seder. "
    "Revi'it allégé souvent suggéré ; vin vs jus de raisin : débat — demande à ton rav."
)
k = key_by_prefix["Drinking wine on Chol HaMoed"]
ES[k] = _CHOL_WINE_ES
FR[k] = _CHOL_WINE_FR

_BIRCHOT_WOMEN_ES = (
    "Muchos poskim sostienen que las mujeres deben decir Birchot HaShajar al despertar — "
    "la misma secuencia de berajot matutinas que los hombres al inicio de Shaharit.\n\n"
    "Qué son:\n"
    "Birchot HaShajar (בִּרְכוֹת הַשַּׁחַר) — ~15 berajot cortas de agradecimiento por cada etapa del despertar.\n\n"
    "Redacción para mujeres:\n"
    "Usa la versión femenina del sidur — en lugar de «que no me hizo mujer», «que me hizo según Su voluntad».\n\n"
    "Cuándo:\n"
    "Desde jatzot (medianoche halájica). Idealmente HaNoten LaSechvi Binah tras alot hashajar; las demás al despertar."
)
_BIRCHOT_WOMEN_FR = (
    "Beaucoup de poskim : les femmes disent Birchot HaShachar au réveil — comme les hommes avant Shaharit. "
    "Version féminine du siddour. Depuis 'hatzot ; idéalement au réveil."
)
k = key_by_prefix["Many poskim and communities hold that women"]
ES[k] = _BIRCHOT_WOMEN_ES
FR[k] = _BIRCHOT_WOMEN_FR

_ASHKENAZI_ES = (
    "Los judíos ashkenazíes tienen raíces en Europa central y oriental y siguen costumbres y nusaj desarrollados allí — "
    "cultura yiddish, rigores de kitniyot en Pesaj, y prácticas de luto y festividades. "
    "«Ashkenaz» en un sidur indica ese texto de oración. "
    "Muchas sinagogas en EE.UU. e Israel son ashkenazíes; pregunta a tu rav qué subtradición te aplica."
)
_ASHKENAZI_FR = (
    "Les Juifs ashkénazes : racines en Europe centrale et orientale, nusach, kitniot à Pessah, deuil et fêtes. "
    "« Ashkenaz » dans un siddour = ce texte de prière. Demande à ton rav quelle sous-tradition."
)
k = key_by_prefix["Ashkenazi Jews trace roots to Central"]
ES[k] = _ASHKENAZI_ES
FR[k] = _ASHKENAZI_FR
k = key_by_prefix["Ashkenazi — Ashkenazi Jews trace roots"]
ES[k] = "Ashkenazí — " + _ASHKENAZI_ES
FR[k] = "Ashkenazi — " + _ASHKENAZI_FR

_ROSH_CHODESH_ES = (
    "Rosh Jodesh (ראש חודש) — el Mes Nuevo — semi-festividad con oración y costumbres extra.\n\n"
    "Comida festiva (mitzvá):\n"
    "• Es mitzvá aumentar la comida — al menos un plato especial (Shulján Aruj O.J. 419:1).\n"
    "• Comer de día; conmemora el banquete del Sanedrín en Beit Ya'zek para testigos de la luna nueva.\n"
    "• Lo gastado en comidas de Rosh Jodesh no se descuenta del sustento del año (Tur O.J. 419).\n\n"
    "Oración hoy (en tus secciones de oración matutina, vespertina y nocturna):\n"
    "• Yaale ve-Yavo en Retzei de Shaharit, Minjá y Arvit — y en bentching si comes pan\n"
    "• Shaharit/Minjá: insertar en Retzei; si ya terminaste Retzei, volver al inicio; si terminaste la Amidá, repetir solo esa Amidá\n"
    "• Arvit de Rosh Jodesh solo: si olvidaste tras Retzei, no repitas (Berajot 30b; Shulján Aruj O.J. 422:1)\n"
    "• Hallel reducido en Shaharit (según minhag; Hallel completo si cae en Janucá)\n"
    "• Se omite Tachanun todo el día\n\n"
    "Otras costumbres:\n"
    "• Generalmente no se ayuna ni se hacen eulogias en Rosh Jodesh\n"
    "• Muchas mujeres casadas evitan cierta melajá (costura, lavado, etc.) — pregunta a tu rav\n"
    "• Si Rosh Jodesh abarca dos días (30 y 1), las observancias aplican a ambos."
)
_ROSH_CHODESH_FR = (
    "Rosh 'Hodesh (ראש חודש) — le Nouveau Mois — semi-fête avec prières et coutumes supplémentaires.\n\n"
    "Repas festif (mitsva) :\n"
    "• Mitsva d'augmenter le repas — au minimum un plat spécial (Choulhan Aroukh O.H. 419:1).\n"
    "• Mange de jour ; commémore le banquet du Sanhédrin à Beit Ya'zek pour les témoins de la nouvelle lune.\n"
    "• Les dépenses pour les repas de Rosh 'Hodesh ne sont pas déduites du parnassa annuel (Tur O.H. 419).\n\n"
    "Prière aujourd'hui (dans tes sections de prière matin, après-midi et soir) :\n"
    "• Yaale v'yavo dans Retzei à Shaharit, Min'ha et Maariv — et au bentching si tu manges du pain\n"
    "• Shaharit/Min'ha : insère dans Retzei ; si tu as déjà conclu Retzei, reviens au début ; "
    "si tu as fini l'Amida, répète seulement cette Amida\n"
    "• Maariv de Rosh 'Hodesh seul : si tu as oublié après Retzei, ne répète pas (Berakhot 30b ; Choulhan Aroukh O.H. 422:1)\n"
    "• Demi-Hallel à Shaharit (selon minhag ; Hallel complet si Rosh 'Hodesh tombe à 'Hanukah)\n"
    "• Pas de Tahanoun toute la journée\n\n"
    "Autres coutumes :\n"
    "• Pas de jeûne ni d'éloges funèbres en Rosh 'Hodesh\n"
    "• Coutume répandue : femmes mariées s'abstiennent de certaines melachot — demande à ton rav\n"
    "• Si Rosh 'Hodesh dure deux jours (30 et 1), les observances s'appliquent aux deux."
)
k = key_by_prefix["Rosh Chodesh (ראש חודש) — the New Month"]
ES[k] = _ROSH_CHODESH_ES
FR[k] = _ROSH_CHODESH_FR

_BEDTIME_SHEMA_ES = (
    "Se recita inmediatamente antes de dormir — requisito separado del Shemá de Arvit.\n\n"
    "Kriat Shemá al HaMitá (קְרִיאַת שְׁמַע עַל הַמִּטָּה) es oración nocturna final que protege durante el sueño.\n\n"
    "Qué decir:\n"
    "• Mínimo: primer párrafo del Shemá\n"
    "• Recomendado: tres párrafos completos + Hamapil + Salmo 91 y otras breves\n\n"
    "Aunque ya dijiste Shemá en Arvit, el de acostarse es mitzvá distinta — protección al dormir.\n\n"
    "Momento: lo más cerca posible de quedarte dormido."
)
_BEDTIME_SHEMA_FR = (
    "Chemâ au coucher — distinct du Chemâ de Maariv. Kriat Shema al HaMitah protège la nuit. "
    "Minimum : premier paragraphe ; idéal : trois + Hamapil + Psaume 91. "
    "Le plus près possible du sommeil."
)
k = key_by_prefix["Said immediately before sleep, the bedtime Shema"]
ES[k] = _BEDTIME_SHEMA_ES
FR[k] = _BEDTIME_SHEMA_FR

_PESACH_PREP_ES = (
    "Pésaj comienza en aproximadamente una semana — usa este tiempo para preparación práctica "
    "(no limpiar cada armario, sino quitar jametz donde importa).\n\n"
    "Enfócate en:\n"
    "• Cocina y comedor: encimeras, horno, microondas, nevera, despensa\n"
    "• Autos, oficinas, bolsos, bolsillos\n"
    "• Vender o usar jametz que no guardarás; planear menús y compras\n"
    "• Hagadot, matzá, vino, plato del Seder — pedir antes de que suban precios\n\n"
    "Kasherización:\n"
    "• Ollas y utensilios absorben jametz — necesitas juego de Pésaj o kasherizar (hagalá, iruí, libun). "
    "Empieza temprano; pregunta a tu rav."
)
_PESACH_PREP_FR = (
    "Pessah dans environ une semaine — préparez : cuisine, voiture, bureaux, vendre le hamets, "
    "commander matsa et vin. Kasherisation des ustensiles — commencez tôt, demande à ton rav."
)
k = key_by_prefix["Pesach begins in about a week"]
ES[k] = _PESACH_PREP_ES
FR[k] = _PESACH_PREP_FR

_YK_BREAKFAST_ES = (
    "Tras el fin de Yom Kipur al anochecer$tzeitLine, es mitzvá comer una comida adecuada — no solo un bocado.\n\n"
    "Qué hacer:\n"
    "• Completa Arvit y Havdalá (vela encendida todo Yom Kipur, vino, sin especias)\n"
    "• Come una comida festiva tras el ayuno — muchos preparan comida antes porque las restricciones terminan al anochecer\n"
    "• Tradición jabadí (Baal HaTania): el sustento del año se conecta especialmente a esta comida — come con alegría e intención espiritual"
)
_YK_BREAKFAST_FR = (
    "Après Yom Kippour à la tombée de la nuit$tzeitLine, mitsva de manger un vrai repas. "
    "Arvit, Havdala, repas festif. Tradition 'Habad : lié au parnassa de l'année."
)
k = key_by_prefix["After Yom Kippur ends at nightfall"]
ES[k] = _YK_BREAKFAST_ES
FR[k] = _YK_BREAKFAST_FR

_BIRCHOT_HASHACHAR_ES = (
    "Serie de berajot cortas al inicio de Shaharit, agradeciendo lo que damos por sentado.\n\n"
    "Birchot HaShajar (בִּרְכוֹת הַשַּׁחַר) — ~15 berajot al despertar: abrir los ojos, vestirse, salir.\n\n"
    "Agradeces por: el gallo que distingue día y noche, vestir a los desnudos, enderezar al encorvado, "
    "extender la tierra sobre las aguas, dar fuerza al cansado, y más.\n\n"
    "Antes vienen Korbanot en el servicio completo. Di las berajot al despertar o al inicio de Shaharit."
)
_BIRCHOT_HASHACHAR_FR = (
    "Birchot HaShachar : ~15 bénédictions au réveil au début de Shaharit — "
    "remerciements pour chaque étape du matin."
)
k = key_by_prefix["A series of short blessings said at the beginning"]
ES[k] = _BIRCHOT_HASHACHAR_ES
FR[k] = _BIRCHOT_HASHACHAR_FR

_MODEH_ANI_ES = (
    "Modeh Ani son las primeras palabras que muchos judíos dicen al despertar del sueño nocturno: "
    "agradece al Rey vivo y eterno por devolver el alma con compasión. "
    "Dilo en la cama antes de levantarte, sin lavarte primero. "
    "Normalmente no se recita tras una siesta diurna — entonces lávate las manos (negel vasser). "
    "Puedes repetir las palabras como gratitud personal, pero no es la oración matutina oficial. "
    "Hombres dicen Modeh; mujeres suelen decir Modá."
)
_MODEH_ANI_FR = (
    "Modeh Ani : premiers mots au réveil du sommeil nocturne — remercier le Roi vivant et éternel "
    "de rendre l'âme avec compassion. Dis-le au lit avant de te lever, sans te laver d'abord. "
    "Pas après une sieste de jour — alors lave les mains (negel vasser). "
    "Tu peux répéter les mots comme gratitude personnelle, mais ce n'est pas la prière officielle du matin. "
    "Hommes : Modeh ; femmes : Modah."
)
k = key_by_prefix["Modeh Ani is the first words many Jews"]
ES[k] = _MODEH_ANI_ES
FR[k] = _MODEH_ANI_FR

_CHAMETZ_EREV_ES = (
    "Jametz:\n"
    "• Todo el jametz debe desaparecer, destruirse o venderse, y recitar Kol Chamira final, "
    "antes del final de la 5.ª hora halájica esta mañana (mediodía) — NO al atardecer.\n"
    "• Deja de comer jametz al final de la 4.ª hora halájica.\n"
    "• Bedikat jametz fue anoche; mejirat jametz ya autorizada con tu rav — usa el ítem biur jametz de hoy si sigue en tu lista."
)
_CHAMETZ_EREV_FR = (
    "Hamets : tout doit être éliminé avant la 5e heure halakhique ce matin ; "
    "arrêter de manger à la 4e heure. Bedikat et mechirat déjà faits."
)
k = key_by_prefix["Chametz:\n• All chametz must be completely gone"]
ES[k] = _CHAMETZ_EREV_ES
FR[k] = _CHAMETZ_EREV_FR

_CHAMETZ_SHABBAT_ES = (
    "Jametz:\n"
    "• El biur fue el viernes por la mañana (13 Nisan). "
    "En Shabat por la mañana, termina de comer jametz antes de la 4.ª hora halájica "
    "y recita Kol Chamira final antes de la 5.ª — no quemes en Shabat.\n"
    "• Bedikat fue el jueves por la noche; mejirat jametz ya debería estar autorizada."
)
_CHAMETZ_SHABBAT_FR = (
    "Hamets : biur vendredi matin (13 Nisan). Chabbat matin — finir de manger avant la 4e heure, "
    "Kol Chamira avant la 5e, pas de brûlage le Chabbat."
)
k = key_by_prefix["Chametz:\n• Biur was Friday morning"]
ES[k] = _CHAMETZ_SHABBAT_ES
FR[k] = _CHAMETZ_SHABBAT_FR

_BOREI_ETZ_ES = (
    "Borei pri ha'etz es la berajá sobre fruta que crece en árboles — manzanas, duraznos, uvas (como fruta) y similares. "
    "Dila antes de comer. Fruta del suelo (bananas, fresas) usa borei pri ha'adamá. "
    "En duda, pregunta a tu rav o usa una guía de berajot."
)
_BOREI_ETZ_FR = (
    "Borei pri ha'etz : bénédiction sur les fruits d'arbre. Fruits du sol : borei pri ha'adamah. "
    "En cas de doute, demande à ton rav."
)
k = key_by_prefix["Borei pri ha'etz is the blessing on fruit"]
ES[k] = _BOREI_ETZ_ES
FR[k] = _BOREI_ETZ_FR
k = key_by_prefix["Borei Pri HaEtz — Borei pri ha'etz is"]
ES[k] = "Borei Pri HaEtz — " + _BOREI_ETZ_ES
FR[k] = "Borei Pri HaEtz — " + _BOREI_ETZ_FR

_KEZAYIT_ES = (
    "Un kezayit es la porción del tamaño de una aceituna en la halajá — para medir cuánto comer "
    "en mitzvot como matzá en el Seder, maror, birkat ha-mazón y berajot aharonot.\n\n"
    "Equivale al volumen de medio o un tercio de huevo según la autoridad. "
    "Muchas organizaciones usan ~35–40 ml (tamaño de pelota de golf). "
    "En peso: ~27 g (Rav Chaim Naé) a ~33 g (Jazon Ish). "
    "Matzá porosa puede ser menos en gramos — pregunta a tu rav."
)
_KEZAYIT_FR = (
    "Kezayit : portion « taille d'olive » — matsa au Seder, bentching, etc. "
    "~27–33 g selon les autorités ; matsa poreuse souvent moins — demande à ton rav."
)
k = key_by_prefix["A kezayit is the olive-sized portion in Jewish"]
ES[k] = _KEZAYIT_ES
FR[k] = _KEZAYIT_FR

_REVIIT_ES = (
    "Un revi'it (רביעית) es un cuarto de log — volumen de huevo y medio en halajá. "
    "Mínimo para mitzvot con líquidos como Kiddush.\n\n"
    "Medidas contemporáneas según posek:\n"
    "• Rav Chaim Naé: ~86 ml (2,9 fl oz) — línea leniente, mucho sefardí\n"
    "• Rav Moshe Feinstein / Jazon Ish: ~130–150 ml — estricto para obligaciones toráicas como Kiddush de Shabat\n\n"
    "También Havdalá, netilat yadayim, etc. — pregunta a tu rav."
)
_REVIIT_FR = (
    "Revi'it (רביעית) : quart de log — Kiddush, Havdala. "
    "~86 ml (lenient) à ~150 ml (strict) selon le posek."
)
k = key_by_prefix["A revi'it (רביעית) is a standard unit"]
ES[k] = _REVIIT_ES
FR[k] = _REVIIT_FR

# Duplicate-prefix keys (same opening line, different tail).
for _k in strings:
    if _k.startswith("Rosh Chodesh (ראש חודש) — the New Month"):
        ES[_k] = _ROSH_CHODESH_ES
        FR[_k] = _ROSH_CHODESH_FR

_KIDDUSH_ES = (
    "Kiddush es la mitzvá toráica de «acordarse del día de Shabat para santificarlo» (Shemot 20:8), "
    "cumplida con una declaración sobre una copa de vino.\n\n"
    "Reglas clave:\n"
    "• Kiddush b'Makom Seudá — en el mismo lugar donde comerás\n"
    "• Copa con al menos un revi'it (~86 ml leniente; más estricto para obligación toráica)\n"
    "• Beber maleh lugmav (bocado generoso) — idealmente la mayor parte de la copa\n"
    "• Mujeres también obligadas; jugo de uva válido\n"
    "• Viernes noche sin vino: jalá puede sustituir; mañana de Shabat: chamar mediná según rav\n"
    "• Consulta a tu rav sobre medidas y sustitutos"
)
_KIDDUSH_FR = (
    "Kiddoush : mitsva toranique de « se souvenir du jour du Chabbat pour le sanctifier » (Shemot 20:8), "
    "accomplie avec une déclaration sur une coupe de vin.\n\n"
    "Règles clés :\n"
    "• Kiddoush b'makom seoudah — même lieu que le repas\n"
    "• Coupe d'au moins un revi'it (~86 ml selon poskim)\n"
    "• Boire maleh lougmav — idéalement la plus grande partie de la coupe\n"
    "• Femmes aussi obligées ; jus de raisin valide\n"
    "• Vendredi soir sans vin : 'hallah peut remplacer ; matin de Chabbat : hamar medina selon ton rav\n"
    "• Demande à ton rav pour mesures et substituts"
)
k = key_by_prefix["Kiddush is the Torah-level commandment"]
ES[k] = _KIDDUSH_ES
FR[k] = _KIDDUSH_FR

_CANDLES_ES = (
    "Las velas se encienden antes del atardecer del viernes para recibir Shabat con paz y calidez. "
    "Las enciende la mujer de la casa (o el hombre si no hay mujer).\n\n"
    "Dos costumbres de berajá:\n"
    "• Ashkenazí: encender, taparse los ojos, berajá, destapar (aceptar Shabat al bendecir)\n"
    "• Sefardí: berajá primero, luego encender\n\n"
    "Sigue el minhag de tu familia. Horarios típicos antes del atardecer: "
    "18 min (muchos ashkenazim fuera de Israel), 40 min (Jerusalén), 20–22 min (otras comunidades). "
    "Consulta el calendario local. No encender antes de plag ha-minjá."
)
_CANDLES_FR = (
    "Bougies avant le coucher du soleil vendredi — femme de la maison (ou l'homme). "
    "Ashkénaze : allumer puis bénédiction les yeux couverts ; séfarade : bénédiction d'abord. "
    "Calendrier local ; pas avant plag ha-min'ha."
)
k = key_by_prefix["Candles are lit before sunset on Friday"]
ES[k] = _CANDLES_ES
FR[k] = _CANDLES_FR

_ERUV_ES = (
    "Un eruv (especialmente eruv chatzerot o eruv de ciudad) es un recinto halájico que permite "
    "llevar objetos dentro de un área definida en Shabat. Requiere límites kosher y mantenimiento comunitario. "
    "Si el eruv está caído, llevar llaves o empujar carriolas fuera puede estar prohibido — "
    "las sinagogas suelen informar por mensaje el viernes. No permite conducir ni usar el teléfono."
)
_ERUV_FR = (
    "Erouv : enceinte halakhique permettant de transporter dans une zone définie le Chabbat. "
    "Vérifier le statut le vendredi. Ne permet pas de conduire ni le téléphone."
)
k = key_by_prefix["An eruv (especially eruv chatzerot"]
ES[k] = _ERUV_ES
FR[k] = _ERUV_FR

_CHATZOS_ES = (
    "Chatzos es la medianoche halájica o el mediodía halájico — punto medio de la noche o del día. "
    "Chatzos halaila importa para Mode Ani, berajot matutinas y algunos ayunos. "
    "Chatzos hayom es el punto medio de la luz del día. "
    "Se mueve con amanecer y atardecer, no con las 12:00 del reloj."
)
_CHATZOS_FR = (
    "Chatzos est le milieu halakhique de la nuit ou du jour — point médian entre lever et coucher du soleil. "
    "Chatzos halaila compte pour Mode Ani, les bénédictions matinales et certains jeûnes. "
    "Chatzos hayom est le milieu de la lumière du jour. "
    "Ça bouge avec lever/coucher, pas avec 12 h sur l'horloge."
)
k = key_by_prefix["Chatzos is halachic midnight"]
ES[k] = _CHATZOS_ES
FR[k] = _CHATZOS_FR
k = key_by_prefix["halachic chatzos — Chatzos is halachic midnight"]
ES[k] = f"halachic chatzos — {_CHATZOS_ES}"
FR[k] = f"chatzos halakhique — {_CHATZOS_FR}"
k = key_by_prefix["halachic chatzos"]
ES[k] = "chatzos halájico"
FR[k] = "chatzos halakhique"

_SEFIRAH_ES = (
    "Minhag ashkenazí: luto desde después de Pesaj hasta Lag BaOmer (33.º día del Omer, 18 Iyar) "
    "o hasta la mañana de Lag BaOmer (según tu sinagoga). "
    "Algunos continúan restricciones de pelo/música hasta Shavuot o las Tres Semanas.\n\n"
    "Sin bodas, música en vivo ni cortes de pelo durante el período de Sefirá de tu comunidad. "
    "Lag BaOmer es pausa para muchos ashkenazim; pregunta a tu rav sobre música y pelo después."
)
_SEFIRAH_FR = (
    "Minhag ashkénaze : deuil de Pessah à Lag ba-Omer — pas de mariages, musique live, coupes de cheveux. "
    "Lag ba-Omer : pause pour beaucoup ; demande à ton rav."
)
k = key_by_prefix["Ashkenaz custom: mourning from after"]
ES[k] = _SEFIRAH_ES
FR[k] = _SEFIRAH_FR

_EREV_PESACH_ES = (
    "Erev Pésaj (14 Nisan) es el día de preparación final antes del Seder.\n\n"
    "• Terminar de comer jametz antes del final de la mañana (consulta zmanim locales)\n"
    "• Bedikat jametz — búsqueda con vela tras el anochecer (según comunidad)\n"
    "• Biur jametz — destruir jametz restante por la mañana\n"
    "• Vender jametz con tu rav si hace falta\n"
    "• Preparar plato del Seder, matzá, vino y hagadot\n\n"
    "El Seder es tras el anochecer del 15 Nisan (y segunda noche en la Diáspora)."
)
_EREV_PESACH_FR = (
    "Erev Pessah (14 Nisan) : fin du hamets le matin, bedikat, biour, vente, préparer le Seder."
)
k = key_by_prefix["Erev Pesach (14 Nissan) is the day"]
ES[k] = _EREV_PESACH_ES
FR[k] = _EREV_PESACH_FR

_EREV_TISHA_ES = (
    "Erev Tisha BeAv (tarde del 8 Av): dejar de estudiar Torá salvo temas tristes; "
    "comida final (seudah hamafseket) antes del ayuno. "
    "Talit y tefilín en Shaharit de Erev — la restricción es el día de Tisha BeAv.\n\n"
    "Tisha BeAv (9 Av): ayuno de 25 horas; kinot en Shaharit sin talit ni tefilín; "
    "ponérselos en Minjá tras jatzot halájica. Sentarse en taburetes bajos hasta jatzot del 9 Av.\n\n"
    "Shabat Jazón: Shabat normal — carne y vino permitidos."
)
_EREV_TISHA_FR = (
    "Erev Tisha BeAv : arrêt d'étude sauf sujets tristes ; dernier repas. "
    "9 Av : jeûne 25 h ; kinot ; téfilines à Min'ha après 'hatstot."
)
k = key_by_prefix["Erev Tisha B'Av (8 Av afternoon)"]
ES[k] = _EREV_TISHA_ES
FR[k] = _EREV_TISHA_FR

_ARBA_MINIM_ES = (
    "Arba Minim (ארבעה מינים) — las Cuatro Especies — cada día de Sucot (excepto Shabat).\n\n"
    "Las cuatro: lulav, etrog, hadasim (3), aravot (2).\n\n"
    "Cómo:\n"
    "• Atar lulav, hadasim y aravot (según portador/koisan). "
    "El lulav debe sobresalir un tefaj sobre myrtle y sauce (Shulján Aruj O.J. 650:1)\n"
    "• Revisar kashrut antes de Yom Tov — especialmente etrog\n"
    "• De día; muchos antes de Shaharit. No en Shabat\n"
    "• Si falta una especie, pregunta a tu rav\n\n"
    "Hombres: obligación toráica el primer día (15 Tishrei en Israel; 15 y 16 en Diáspora según minhag). "
    "Mujeres: mitzvá recomendada según costumbre."
)
_ARBA_MINIM_FR = (
    "Arba Minim (ארבעה מינים) — les quatre espèces — chaque jour de Souccot sauf Chabbat.\n\n"
    "Les quatre : loulav, etrog, hadassim (3), aravot (2).\n\n"
    "Comment faire :\n"
    "• Lie loulav, hadassim et aravot (selon ton porte-loulav / koisan). "
    "Le loulav doit dépasser d'un tefah myrte et saule (Choulhan Aroukh O.H. 650:1)\n"
    "• Vérifie le casher avant Yom Tov — surtout l'etrog\n"
    "• De jour ; beaucoup avant Shaharit. Pas Chabbat\n"
    "• $daysNote\n"
    "• Si une espèce manque, demande à ton rav\n\n"
    "Hommes : obligation torahique le premier jour (15 Tishri en Israël ; 15–16 en diaspora selon minhag). "
    "Femmes : mitsva recommandée selon la coutume."
)
for _k in strings:
    if _k.startswith("Arba Minim (ארבעה מינים) — the Four Species"):
        ES[_k] = _ARBA_MINIM_ES
        FR[_k] = _ARBA_MINIM_FR

_STANDING_KIMA_ES = (
    "De pie (kima — קִימָה): minhag ashkenazí (Rama Y.D. 240:4): levántate una vez cada 24 horas "
    "por cada padre cuando los ves — cumple la obligación diaria. "
    "Si están contigo todo el día, levántate al entrar. En duda, levantarse más a menudo es loable."
)
_STANDING_KIMA_FR = (
    "Kima (קִימָה) : se lever pour les parents — ashkénaze : une fois par jour (Rama Y.D. 240:4)."
)
for _k in strings:
    if _k.startswith("Standing (kima — קִימָה)"):
        ES[_k] = _STANDING_KIMA_ES
        FR[_k] = _STANDING_KIMA_FR

_STANDING_KIMA_CHABAD_ES = (
    "De pie (kima): Jabad sigue al Rama (Y.D. 240:4): levantarse una vez al día por cada padre al verlos "
    "cumple la obligación diaria. Si compartes hogar o pasas el día juntos, levántate al entrar a tu espacio."
)
_STANDING_KIMA_CHABAD_FR = (
    "Kima : 'Habad suit le Rama — une fois par jour par parent quand on les voit."
)
_STANDING_KIMA_MIZRACH_ES = (
    "De pie (kima): Edot HaMizraj siguen Shulján Aruj (Y.D. 240:4): levántate cuando un padre entra en tu presencia "
    "y permanece de pie hasta que se sienten o se van — cada vez que se acercan, no solo una vez al día."
)
_STANDING_KIMA_MIZRACH_FR = (
    "Kima : Edot HaMizrah — se lever à chaque fois qu'un parent entre, jusqu'à ce qu'il s'assoie."
)
_STANDING_KIMA_SEPHARDI_ES = (
    "De pie (kima): costumbre sefardí (Shulján Aruj Y.D. 240:4; Yalkut Yosef): levántate cada vez que un padre "
    "entra en tu presencia — dentro de unas cuatro amot (unos dos metros) — y permanece de pie hasta que se sienta o pase."
)
_STANDING_KIMA_SEPHARDI_FR = (
    "Kima séfarade : se lever à chaque approche d'un parent dans les quatre amot."
)
for _k in strings:
    if _k.startswith("Standing (kima): Chabad"):
        ES[_k] = _STANDING_KIMA_CHABAD_ES
        FR[_k] = _STANDING_KIMA_CHABAD_FR
    elif _k.startswith("Standing (kima): Edot HaMizrach"):
        ES[_k] = _STANDING_KIMA_MIZRACH_ES
        FR[_k] = _STANDING_KIMA_MIZRACH_FR
    elif _k.startswith("Standing (kima): Sephardi"):
        ES[_k] = _STANDING_KIMA_SEPHARDI_ES
        FR[_k] = _STANDING_KIMA_SEPHARDI_FR

_TEFILLIN_GLOSSARY_ES = (
    "Las tefilín son cajas de cuero negro con versos de la Torá, usadas en brazo y cabeza entre semana. "
    "Los hombres desde los trece años están obligados. Shel yad va en el brazo izquierdo superior (derecho si eres zurdo). "
    "Recita la berajá en misheyakir (ver glosario). Requieren pergaminos kosher y colocación correcta. "
    "No se usan en Shabat ni festivales."
)
_TEFILLIN_GLOSSARY_FR = (
    "Les téfilines sont des boîtes de cuir noir avec des versets de Torah, portées au bras et à la tête en semaine. "
    "Obligatoires pour les hommes dès treize ans. Shel yad sur le bras gauche (droit si tu es gaucher). "
    "Bénédiction à misheyakir (voir glossaire). Parchemins casher et placement corrects requis. "
    "Pas le Shabbat ni les fêtes."
)
k = key_by_prefix["Tefillin are holy black leather boxes"]
ES[k] = _TEFILLIN_GLOSSARY_ES
FR[k] = _TEFILLIN_GLOSSARY_FR
k = key_by_prefix["Tefillin — Tefillin are holy black leather"]
ES[k] = "Tefillin — " + _TEFILLIN_GLOSSARY_ES
FR[k] = "Tefillin — " + _TEFILLIN_GLOSSARY_FR

k = key_by_prefix["bitachon — Bitachon is trust in G-d"]
ES[k] = (
    "bitajón — confianza en D-s: creer que Él provee lo que necesitas y que la dificultad puede tener propósito "
    "aunque no lo veas. Relacionado con emuná pero enfatiza calma en la preocupación diaria. "
    "Musar y Jasidut enseñan bitajón práctico."
)
FR[k] = (
    "bitachon — confiance en D.ieu : Il pourvoit à vos besoins ; l'épreuve peut avoir un sens caché. "
    "Lié à l'emouna ; le moussar et la 'hassidout l'enseignent au quotidien."
)

_MATANA_ES = (
    "Matana al menat lehachzir — regalo con condición de devolverlo. "
    "El receptor es dueño mientras lo tiene y luego lo devuelve. "
    "En el primer día de Sucot, si no tienes lulav y etrog, alguien puede prestártelos con esta fórmula "
    "para que cumplas la obligación de propiedad y luego los devuelves. "
    "No se usa en mejirat jametz — la venta debe ser absoluta."
)
_MATANA_FR = (
    "Matana al menat lehachzir — don à condition de le rendre. "
    "Le destinataire en est propriétaire le temps de l'avoir en main, puis le rend. "
    "Au premier jour de Souccot, si vous n'avez pas de loulav et etrog, quelqu'un peut vous les prêter ainsi "
    "pour l'obligation de propriété, puis vous les rendez. "
    "Pas pour me'hirat 'hamets — la vente doit être absolue."
)
k = key_by_prefix["matana al menat lehachzir — Matana al menat lehachzir"]
ES[k] = _MATANA_ES
FR[k] = _MATANA_FR

k = key_by_prefix["Tell us about yourself"]
ES[k] = "Cuéntanos sobre ti"
FR[k] = "Parlez-nous de vous"

_HADERECH_ES = (
    "Que sea Tu voluntad, Señor nuestro D-s y D-s de nuestros antepasados, "
    "que nos conduzcas hacia la paz, guíes nuestros pasos hacia la paz "
    "y nos hagas llegar a nuestro destino deseado para vida, alegría y paz.\n"
    "Que nos salves de todo enemigo, emboscada en el camino y de todo castigo que venga a la tierra.\n"
    "Que envíes bendición en nuestra obra y nos concedas gracia, bondad y misericordia ante Ti y ante quienes nos ven.\n"
    "Escucha la voz de nuestra súplica, porque Tú eres D-s que escucha la oración y la súplica.\n"
    "Bendito eres Tú, Señor, que escuchas la oración."
)
_HADERECH_FR = (
    "Qu'il soit Ta volonté, Éternel notre D.ieu et D.ieu de nos ancêtres, "
    "de nous conduire vers la paix, guider nos pas vers la paix "
    "et nous faire atteindre notre destination pour la vie, la joie et la paix.\n"
    "Délivre-nous de tout ennemi, embuscade sur le chemin et de toute punition venue sur terre.\n"
    "Envoie la bénédiction dans notre travail et accorde-nous grâce, bonté et miséricorde devant Toi et devant tous.\n"
    "Écoute la voix de notre supplication, car Tu es D.ieu qui entends la prière.\n"
    "Béni sois-Tu, Éternel, qui entends la prière."
)
k = key_by_prefix["May it be Your will, Lord our God"]
ES[k] = _HADERECH_ES
FR[k] = _HADERECH_FR

_TORAH_WOMEN_ES = (
    "Por qué importa el estudio de la Torá para ti — lo que enseñan los sabios:\n\n"
    "La Torá no es solo «trabajo de hombres» en el sentido espiritual. Cada vez que aprendes — halajá para tu hogar, "
    "historias de nuestras matriarcas o las oraciones que recitas — te unes a Hashem. "
    "Pirkei Avot promete que cuando pasan palabras de Torá entre personas, la Shejiná mora entre ellas (Avot 3:2–3). "
    "Aplica a una mujer con un Kitzur Shulján Aruj en su escritorio como en un beit midrash.\n\n"
    "Los sabios cuentan el estudio de la Torá entre las tres cosas que protegen la vida (Sotá 21a). "
    "Aprender las leyes de Shabat, kashrut y taharat ha-mishpajá no es burocracia — "
    "es cómo traes santidad a las habitaciones donde vive tu familia.\n\n"
    "La Torá es etz jaim, árbol de vida (Mishlei 3:18). Unos minutos de estudio enfocado endulzan el día "
    "y conectan mitzvot en un panorama completo.\n\n"
    "Qué aplica a las mujeres en el estudio de la Torá:\n\n"
    "Las mujeres están exentas de la mitzvá formal y continua de Talmud Torá que incumbe a los hombres — "
    "incluido el horario día y noche de este checklist. Tampoco estás obligada a Shnayim Mikra ni al programa "
    "Hayom Yom / Chitas de Jabad, aunque puedes aprenderlo voluntariamente.\n\n"
    "Tu obligación:\n"
    "Estás obligada a estudiar las halajot prácticas que aplican a tu vida diaria para observarlas bien: "
    "berajot, kashrut, Shabat, oración que recitas, tzniut, taharat ha-mishpajá (si estás casada) "
    "y otras mitzvot relevantes para ti.\n\n"
    "Cuándo aprender:\n"
    "Cualquier momento del día o la noche. Unos minutos de halajá enfocada cuentan. "
    "No hay límite superior si quieres aprender más.\n\n"
    "Oración vespertina (sección aparte):\n"
    "Las mujeres recitan Kriat Shemá al HaMitá antes de dormir. "
    "No necesitas el Shemá con sus berajot de Arvit como obligación separada.\n\n"
    "Buenos puntos de partida:\n"
    "• Kitzur Shulján Aruj o un libro de halajá para mujeres de tu comunidad\n"
    "• Halajá diaria en DailyHalacha.com o Halachipedia\n"
    "• Sefaria.org — busca temas para tu hogar y familia\n\n"
    "Pregunta a tu rav o rebbetzin qué áreas priorizar según creces en la observancia."
)
_TORAH_WOMEN_FR = (
    "Pourquoi l'étude de la Torah compte pour vous — ce qu'enseignent les sages :\n\n"
    "La Torah n'est pas seulement « l'affaire des hommes » au sens spirituel. Chaque fois que vous apprenez — "
    "halakha pour la maison, histoires de nos matriarches ou prières que vous dites — vous vous liez à Hachem. "
    "Pirke Avot promet que quand des paroles de Torah passent entre les gens, la Chekhina demeure parmi eux (Avot 3:2–3).\n\n"
    "Les sages comptent l'étude de la Torah parmi les trois choses qui protègent la vie (Sotah 21a). "
    "Apprendre Chabbat, kashrout et taharat hamishpacha n'est pas de la bureaucratie — "
    "c'est apporter la sainteté dans les pièces où vit votre famille.\n\n"
    "Votre obligation :\n"
    "Vous devez étudier les halakhot pratiques de votre vie quotidienne : berakhot, kashrout, Chabbat, "
    "prière, tsniout, taharat hamishpacha (si mariée) et autres mitsvot qui vous concernent.\n\n"
    "Quand apprendre : à tout moment. Quelques minutes de halakha comptent.\n\n"
    "Prières du soir : les femmes disent le Kriat Shema al HaMitah avant de dormir — "
    "pas besoin du Shema avec les berakhot de Maariv séparément.\n\n"
    "Demande à ton rav ou rebbetzin quelles priorités selon votre chemin."
)
k = key_by_prefix["Why Torah learning matters for you"]
ES[k] = _TORAH_WOMEN_ES
FR[k] = _TORAH_WOMEN_FR

_AMIDAH_LONG_ES = (
    "La Amidá es la oración central de cada servicio — el momento en que nos ponemos de pie ante D-s en oración silenciosa.\n\n"
    "Qué es:\n"
    "La Amidá (עֲמִידָה — «de pie») también se llama Shemoneh Esrei (שְׁמוֹנֶה עֶשְׂרֵה — «dieciocho») por las 18 bendiciones originales; "
    "se añadió una 19.ª. Entre semana tiene 19 berajot; en Shabat y festivales es más corta.\n\n"
    "Las tres secciones:\n"
    "• Sheváj (alabanza): las tres primeras berajot alaban a D-s — Su grandeza, poder sobre vida y muerte, y santidad\n"
    "• Bakashá (peticiones): las 13 del medio piden sabiduría, arrepentimiento, perdón, redención, salud, sustento, "
    "reunión de exiliados, justicia y la venida del Mesías\n"
    "• Hodáá (gracias): las tres finales dan gracias y piden paz\n\n"
    "Cómo rezarla:\n"
    "• De pie con los pies juntos (como los ángeles)\n"
    "• Hacia Jerusalén (orientación al este en la mayoría de lugares)\n"
    "• Inclinarse en cuatro puntos específicos\n"
    "• En voz baja — los labios se mueven pero solo tú la escuchas\n"
    "• No interrumpir una vez empezada\n\n"
    "Tajanún:\n"
    "Tajanún (תַּחֲנוּן — súplica) es oración penitencial tras la Amidá la mayoría de los días de semana. "
    "Suele recitarse sentado, inclinando la frente sobre el brazo (nefilat apayim).\n\n"
    "En Shaharit:\n"
    "• Lunes y jueves: Tajanún largo con nefilat apayim\n"
    "• Otros días: versión más corta (varía según nusaj)\n\n"
    "Se omite Tajanún en Shabat, Yom Tov, Rosh Jodesh y otros días de alegría — la Amidá sí se dice.\n\n"
    "Rosh Jodesh es «cabeza del mes» — primer día del mes hebreo, festividad menor.\n\n"
    "Si rezas en sinagoga, sigue la práctica congregacional para Tajanún.\n\n"
    "Horarios:\n"
    "La Amidá de la mañana está disponible desde el alba (alot hashajar). Idealmente al amanecer o después. "
    "Plazo ideal: antes del final de la cuarta hora halájica (sof zman tefilá). "
    "Bedieved puedes rezar Shaharit hasta jatzot (mediodía halájico). "
    "Si la pierdes por completo, en Minjá reza dos Amidot — Minjá primero, luego tashlumin por Shaharit perdida."
)
_AMIDAH_LONG_FR = (
    "L'Amida est la prière centrale de chaque office — le moment où l'on se tient debout devant D.ieu en prière silencieuse.\n\n"
    "C'est quoi :\n"
    "L'Amida (עֲמִידָה — « debout ») s'appelle aussi Chemoneh Esre (שְׁמוֹנֶה עֶשְׂרֵה — « dix-huit ») ; une 19e bénédiction a été ajoutée. "
    "En semaine : 19 berakhot ; Chabbat et fêtes : plus court.\n\n"
    "Trois sections : Sheva'h (louange), Bakashah (demandes), Hodah (remerciement).\n\n"
    "Comment : debout, pieds joints, face à Jérusalem, inclinaisons à quatre moments, à voix basse, sans interruption.\n\n"
    "Tahanoun : supplication après l'Amida en semaine, souvent assis (nefilat apayim). Omis Chabbat, Yom Tov, Rosh 'Hodesh.\n\n"
    "Horaires : dès alot hashahar ; idéal avant sof zman tefila ; bédieved jusqu'à 'hatzot ; tashloumin à Min'ha si manquée."
)
_AMIDAH_SHORT_ES = (
    "Amidá — La Amidá (oración «de pie») es el núcleo de cada servicio — también llamada Shemoneh Esrei "
    "(dieciocho, ahora diecinueve berajot). Se recita en silencio de pie, hacia Jerusalén o el Monte del Templo "
    "(en Jerusalén), con los pies juntos."
)
_AMIDAH_SHORT_FR = (
    "Amida — prière « debout », cœur de chaque office ; Chemoneh Esre (dix-huit, maintenant dix-neuf berakhot). "
    "Silencieusement debout, face à Jérusalem, pieds joints."
)
k = key_by_prefix["The Amidah is the central, most important"]
ES[k] = _AMIDAH_LONG_ES
FR[k] = _AMIDAH_LONG_FR
k = key_by_prefix['Amidah — The Amidah ("standing" prayer)']
ES[k] = _AMIDAH_SHORT_ES
FR[k] = _AMIDAH_SHORT_FR

_SHEMA_LONG_ES = (
    "El Shemá es la declaración central de la fe judía, recitada dos veces al día — mañana y noche. "
    "Decirlo cada día es mitzvá de nivel toráico.\n\n"
    "Qué es el Shemá:\n"
    "Shema (שְׁמַע — «Escucha») son tres pasajes de la Torá:\n"
    "• Párrafo 1 (Deuteronomio 6:4-9): unicidad de D-s y amarle con todo tu corazón, alma y recursos\n"
    "• Párrafo 2 (Deuteronomio 11:13-21): recompensa por observar la Torá y consecuencia de abandonarla\n"
    "• Párrafo 3 (Números 15:37-41): mitzvá de tzitzit y recordar el Éxodo de Egipto\n\n"
    "La primera línea:\n"
    "«Shema Israel, Ado-nai Eloheinu, Ado-nai Ejad.»\n"
    "«Escucha, Israel: el Señor es nuestro D-s, el Señor es Uno.»\n"
    "Es la afirmación más fundamental del judaísmo.\n\n"
    "Las berajot que lo rodean:\n"
    "• Las berajot matutinas antes y después del Shemá (Yotzer Or, Ahavat Olam / Ahavá Rabá, Emet VeYatziv) pertenecen a Shaharit — "
    "no cuando recitas solo los tres párrafos para alcanzar sof zman Shemá antes de rezar Shaharit.\n"
    "• Antes del Shemá en Shaharit: dos berajot — luz del día (Yotzer Or) y Torá a Israel (Ahavat Olam / Ahavá Rabá)\n"
    "• Después del Shemá: berajá sobre la redención de Egipto (Emet VeYatziv)\n\n"
    "Cómo decirlo:\n"
    "Recita la primera línea despacio con intención. Cubrir los ojos en el primer verso es costumbre universal, no obligación estricta. "
    "El resto a ritmo normal.\n\n"
    "Horarios:\n"
    "• El Shemá matutino bíblico debe recitarse dentro de las primeras tres shaot zmaniot — hasta sof zman Shemá. "
    "Es el último momento para la obligación toráica.\n"
    "• La app atenúa el ítem tras sof zman Shemá porque ya no se cumple la obligación bíblica.\n"
    "• Si no rezarás Shaharit antes de sof zman Shemá, di los tres párrafos solos antes de ese plazo — sin berajot.\n"
    "• Si rezarás Shaharit a tiempo, di el Shemá con sus berajot en el servicio completo.\n"
    "• Bedieved: si perdiste sof zman Shemá pero aún rezas Shaharit hasta jatzot, di el Shemá matutino con berajot en ese servicio. "
    "No cumple la obligación bíblica, pero completa Shaharit correctamente."
)
_SHEMA_LONG_FR = (
    "Le Shema est la déclaration centrale de la foi juive, récitée deux fois par jour — matin et soir — mitsva de niveau Torah.\n\n"
    "Trois passages : Deutéronome 6, 11 ; Nombres 15 (tsitsit et sortie d'Égypte).\n\n"
    "« Shema Israel, Ado-nai Eloheinu, Ado-nai E'had. » — l'affirmation la plus fondamentale de l'unité divine.\n\n"
    "Berakhot autour : Yotzer Or, Ahavat Olam, Emet VeYatziv dans Shaharit — pas quand tu dis seulement les trois paragraphes avant sof zman Shema.\n\n"
    "Horaires :\n"
    "• Le Shema matinal biblique doit être récité dans les trois premières shaot zmaniot — jusqu'à sof zman Shema.\n"
    "• L'app atténue l'élément après sof zman Shema car l'obligation biblique est passée.\n"
    "• Si tu ne pries pas Shaharit avant sof zman Shema, dis les trois paragraphes seuls avant ce délai — sans berakhot.\n"
    "• Si tu pries Shaharit à temps, dis le Shema avec ses berakhot dans le service complet.\n"
    "• Bédieved : si tu as manqué sof zman Shema mais pries encore Shaharit avant 'hatzot, dis le Shema matinal avec berakhot — "
    "cela ne remplit pas l'obligation biblique mais complète Shaharit correctement."
)
k = key_by_prefix["The Shema is the central declaration of Jewish"]
ES[k] = _SHEMA_LONG_ES
FR[k] = _SHEMA_LONG_FR

_CHOL_HAMOED_ES = (
    "Jol hamoed son los días intermedios entre el primer y último día de Pesaj o Sucot. "
    "Siguen siendo tiempo festivo, pero las reglas de melajá son más ligeras que en Yom Tov o Shabat: "
    "muchos trabajos y preparación de comida están permitidos, con límites en cierta actividad comercial y algunas melajot. "
    "Afeitado y corte de pelo están prohibidos (O.J. 531) salvo excepciones — pregunta a tu rav."
)
_CHOL_HAMOED_FR = (
    "Hol hamoed : jours intermédiaires de Pessa'h ou Souccot — encore fête, melakha allégée vs Yom Tov/Chabbat. "
    "Rasage et coupe interdits (O.H. 531) sauf exceptions — demande à ton rav."
)
k = key_by_prefix["Chol HaMoed are the intermediate"]
ES[k] = _CHOL_HAMOED_ES
FR[k] = _CHOL_HAMOED_FR
k = key_by_prefix["Chol HaMoed — Chol HaMoed are the intermediate"]
ES[k] = "Jol hamoed — " + _CHOL_HAMOED_ES
FR[k] = "Hol hamoed — " + _CHOL_HAMOED_FR

_DAVEN_ES = (
    "Daven (tefilá) significa orar — palabra yiddish ashkenazí muy extendida. "
    "«Rezar Shaharit», «ir a daven» = ir a rezar. "
    "Se habla de rezar los servicios diarios (Shajarit, Minjá, Arvit) o de orar en general."
)
_DAVEN_FR = (
    "Daven (tefila) : prier — argot yiddish ashkenaze très répandu. « Daven Shacharit » = prier l'office du matin."
)
k = key_by_prefix["Daven (davening) means to pray"]
ES[k] = _DAVEN_ES
FR[k] = _DAVEN_FR
k = key_by_prefix["daven — Daven (davening) means"]
ES[k] = "daven — " + _DAVEN_ES
FR[k] = "daven — " + _DAVEN_FR

_NUSACH_ARI_ES = (
    "Nusaj Ari es el rito de oración asociado al rabí Isaac Luria (el Ari), usado por Jabad y otras comunidades. "
    "Mezcla elementos ashkenazíes y sefardíes. Los sidurim de Jabad como Tehilat Hashem imprimen este nusaj. "
    "Si rezas Nusaj Ari, usa ese sidur de forma consistente para los insertos festivos."
)
_NUSACH_ARI_FR = (
    "Nusach Ari : rite de prière du Ari, utilisé par 'Habad et d'autres — mélange ashkénaze et séfarade. "
    "Siddour Tehilat Hashem ; restez cohérent pour les insertions de fête."
)
k = key_by_prefix["Nusach Ari is the prayer rite"]
ES[k] = _NUSACH_ARI_ES
FR[k] = _NUSACH_ARI_FR
k = key_by_prefix["Nusach Ari — Nusach Ari is the prayer"]
ES[k] = "Nusaj Ari — " + _NUSACH_ARI_ES
FR[k] = "Nusach Ari — " + _NUSACH_ARI_FR

_APP_DISCLAIMER_ES = (
    "Esta aplicación es un compañero de aprendizaje, no un rabino — no da reglas halájicas.\n\n"
    "Esta lista no contiene todas las mitzvot de la Torá entera. Cubre mitzvot diarias estándar que practican los judíos observantes — "
    "despertar, oración, berajot, comidas, estudio de Torá, preparación de Shabat y cimientos similares.\n\n"
    "Con tu permiso, la aplicación usa GPS o una ciudad que eliges para calcular tiempos del calendario judío "
    "y cuándo puedes cumplir distintas mitzvot (ventanas de oración, encendido de velas, tiempos de Shabat). "
    "La ubicación queda solo en tu dispositivo para zmanim y calendario.\n\n"
    "Si eres nuevo en el judaísmo, ve despacio y haz lo que puedas. Construye hábitos firmes sin agobio, "
    "y pregunta siempre a un rabino ortodoxo de confianza cuando algo no esté claro o tu situación requiera guía personal."
)
_APP_DISCLAIMER_FR = (
    "Cette application est un compagnon d'apprentissage, pas un rabbin — pas de décisions halakhiques.\n\n"
    "La liste couvre les mitsvot quotidiennes courantes, pas toute la Torah.\n\n"
    "Avec votre permission : GPS ou ville choisie pour les zmanim — données sur l'appareil seulement.\n\n"
    "Nouveau dans le judaïsme ? Allez doucement ; demandez à un rabbin orthodoxe de confiance."
)
k = key_by_prefix["This app is a learning companion, not a rabbi"]
ES[k] = _APP_DISCLAIMER_ES
FR[k] = _APP_DISCLAIMER_FR

_TORAH_BIRKAT_ES = (
    "El estudio de la Torá te une con Hashem — los sabios enseñan que la Shejiná mora donde se hablan palabras de Torá (Avot 3:2) "
    "y que el estudio protege la vida (Sotá 21a). Birkat HaTorá abre esa puerta cada día.\n\n"
    "Antes de aprender Torá por primera vez cada día, las mujeres dicen Birkat HaTorá — las berajot sobre el estudio. "
    "Aplica cuando aprendes halajá o cualquier texto de Torá. La obligación de las mujeres es ampliamente aceptada entre poskim; "
    "el nivel exacto (toráico vs rabínico) se debate — pregunta a tu rav.\n\n"
    "Di las tres berajot una vez por la mañana (desde después de jatzot halayla / medianoche halájica). "
    "Cubren todo el estudio del resto del día.\n\n"
    "Tras las berajot, muchos sidurim incluyen pasajes cortos de Torá. "
    "Incluso unos minutos de halajá después cumplen tu obligación diaria de estudio (ver Estudio de Torá)."
)
_TORAH_BIRKAT_FR = (
    "L'étude de la Torah vous lie à Hachem — la Chekhina demeure où l'on parle Torah (Avot 3:2). "
    "Birkat HaTorah chaque matin pour les femmes qui apprennent — trois berakhot après 'hatzot halayla. "
    "Demande à ton rav sur le niveau d'obligation."
)
k = key_by_prefix["Torah learning bonds you with Hashem"]
ES[k] = _TORAH_BIRKAT_ES
FR[k] = _TORAH_BIRKAT_FR

_MITZVAH_WORD_ES = (
    "La palabra mitzvá (מִצְוָה) significa literalmente «mandamiento». En el judaísmo, las mitzvot son los 613 mandamientos "
    "que D-s dio por la Torá, más algunas que los rabinos añadieron con inspiración divina.\n\n"
    "Mitzvá también significa «conexión». Al cumplir una mitzvá, realizas la voluntad de D-s y te conectas con lo Divino — "
    "como seguir las indicaciones de alguien querido fortalece el vínculo.\n\n"
    "D-s envía constantemente Luz celestial que sostiene el mundo. Ir contra Su voluntad es como poner un paraguas entre ti y esa Luz. "
    "Pero con las mitzvot te conectas a la Bondad celestial que es D-s mismo — y puedes experimentar algo de «cielo en la tierra», "
    "un estado que podrías llamar «Mitz Mode». Quizá no sea de inmediato, pero haz unas mitzvot y observa cómo te sientes. "
    "Esto es solo el comienzo..."
)
_MITZVAH_WORD_FR = (
    "Mitsva (מִצְוָה) : commandement — les 613 de la Torah et d'autres d'origine rabbinique.\n\n"
    "Aussi « connexion » : accomplir une mitsva, c'est se lier au Divin.\n\n"
    "Quelques mitsvot et voyez comment vous vous sentez — c'est le début du « Mitz Mode »."
)
k = key_by_prefix['The word mitzvah (מִצְוָה) literally means']
ES[k] = _MITZVAH_WORD_ES
FR[k] = _MITZVAH_WORD_FR

_MUSAF_ES = (
    "Musaf es una Amidá adicional en el servicio de la mañana en Shabat, Rosh Jodesh y todas las festividades judías.\n\n"
    "Qué es:\n"
    "Musaf (מוּסָף — «adicional») es una Amidá extra con una berajá central especial que describe las ofrendas del Templo (korbanot) "
    "de ese día y pide la restauración del servicio del Templo.\n\n"
    "Cuándo:\n"
    "• Cada Shabat\n"
    "• Rosh Jodesh\n"
    "• Todos los Yom Tov\n"
    "• Jol hamoed de Pesaj y Sucot\n\n"
    "Por qué:\n"
    "En tiempos del Templo se traían korbanot musaf en estos días santos. Tras la destrucción, la oración sustituye las ofrendas físicas.\n\n"
    "Horario:\n"
    "Tras Shaharit, idealmente antes del final de la 7.ª hora halájica; bedieved válido hasta el atardecer (Shulján Aruj O.J. 286:1)."
)
_MUSAF_FR = (
    "Moussaf : Amida supplémentaire le matin Chabbat, Rosh 'Hodesh et fêtes — berakha centrale sur les korbanot du jour "
    "et restauration du Temple. Après Shaharit, idéal avant la 7e heure halakhique (Choulhan Aroukh O.H. 286:1)."
)
k = key_by_prefix["Musaf is an additional Amidah"]
ES[k] = _MUSAF_ES
FR[k] = _MUSAF_FR

_NETILAT_ES = (
    "Inmediatamente después de Modeh Ani, lava las manos ritualmente antes de cualquier otra cosa.\n\n"
    "Qué es:\n"
    "Netilat Yadayim (נְטִילַת יָדַיִם — «levantamiento de las manos») es lavado ritual — no solo higiene. "
    "En el sueño profundo el alma asciende parcialmente y ruach ra'ah se asienta en dedos y uñas. "
    "Este lavado la quita y nos prepara para oración, Torá y el día.\n\n"
    "Los minhagim difieren si el lavado matutino es sobre todo para quitar ruach ra'ah o preparar la oración — "
    "el momento de Al Netilat Yadayim varía (ashkenazí, sefardí, jabad, edot ha-mizraj).\n\n"
    "Cómo verter (todas las tradiciones):\n"
    "1. Idealmente ten un vaso con agua y un recipiente junto a la cama\n"
    "2. Vierte sobre la mano derecha, luego la izquierda, alternando tres veces\n"
    "3. Sécalas cuando indique tu minhag\n\n"
    "Minhag ashkenazí:\n"
    "• Rutina: lavar al despertar sin berajá\n"
    "• Berajá: tras ir al baño, vestirte, lavar de nuevo y secar — entonces Al Netilat Yadayim y Asher Yatzar\n"
    "• Si no necesitas baño al despertar: berajá en el primer lavado\n\n"
    "Texto de la berajá:\n"
    "«Baruj Ata Ado-nai Eloheinu Melej ha-olam, asher kidshanu be-mitzvotav ve-tzivanu al netilat yadayim»\n\n"
    "Regla de 4 amot:\n"
    "Antes de lavar, no camines 4 amot (~2 m) seguidos sin parar. Si el lavabo está lejos, consulta a tu rav "
    "(Zohar: tramos cortos; Mishná Berurá: ir directo también permitido).\n\n"
    "Sin agua cercana: frota las manos en superficie limpia (nikuy) — permite palabras sagradas pero NO quita la impureza.\n\n"
    "Siestas diurnas:\n"
    "• Menos de ~30 min: ruach ra'ah severa no aplica; lavar sin berajá\n"
    "• Siesta larga: opiniones varían — pregunta a tu rav\n\n"
    "Notas:\n"
    "• No toques ojos, boca o cara antes de lavar\n"
    "• No toques comida antes de lavar\n"
    "• El mismo triple vertido sin berajá: tras baño, cementerio, antes de pan\n"
    "• Tras Al Netilat Yadayim por la mañana, no repites la berajá al lavar en la sinagoga"
)
_NETILAT_ES_HEAD = (
    "Inmediatamente después de Modeh Ani, lava las manos ritualmente antes de cualquier otra cosa.\n\n"
    "Qué es:\n"
    "Netilat Yadayim (נְטִילַת יָדַיִם) es lavado ritual — no solo higiene. En el sueño profundo el alma "
    "asciende parcialmente y ruach ra'ah se asienta en dedos y uñas. Este lavado la quita y te prepara "
    "para oración, Torá y el día.\n\n"
    "Los minhagim difieren en el momento de la berajá Al Netilat Yadayim.\n\n"
    "Cómo verter (todas las tradiciones):\n"
    "1. Idealmente ten un vaso con agua y un recipiente junto a la cama\n"
    "2. Vierte sobre la mano derecha, luego la izquierda, alternando tres veces\n"
    "3. Sécalas cuando indique tu minhag (ver abajo)\n\n"
)
_NETILAT_ES_TAIL = (
    "\n\nTexto de la berajá:\n"
    "«Baruj Ata Ado-nai Eloheinu Melej ha-olam, asher kidshanu be-mitzvotav ve-tzivanu al netilat yadayim»\n\n"
    "Regla de 4 amot:\n"
    "Antes de lavar, no camines 4 amot (~2 m) seguidos sin parar. Si el lavabo está lejos, consulta a tu rav "
    "(Zohar: tramos cortos; Mishná Berurá: ir directo también permitido).\n\n"
    "Nikuy (sin agua cerca):\n"
    "Frota las manos en superficie limpia — permite palabras sagradas pero NO quita la impureza.\n\n"
    "Siesta diurna (sin Modeh Ani):\n"
    "• Menos de ~30 min: lavar sin berajá\n"
    "• Siesta larga: opiniones varían — pregunta a tu rav\n\n"
    "Notas:\n"
    "• No toques ojos, boca o cara antes de lavar\n"
    "• No toques comida antes de lavar\n"
    "• Mismo triple vertido sin berajá: tras baño, cementerio, antes de pan\n"
    "• Tras la berajá matinal, no la repites en la sinagoga"
)
_NETILAT_ES_MINHAG = {
    "Ashkenaz minhag — when": (
        "Minhag ashkenazí — cuándo decir Al Netilat Yadayim:\n"
        "• Al despertar: lavado sin berajá (a menudo junto a la cama)\n"
        "• Berajá tras el segundo lavado (baño, vestirse), con Asher Yatzar\n"
        "• Si no necesitas baño al despertar: berajá en el primer lavado"
    ),
    "Chabad minhag — when": (
        "Minhag jabad — cuándo decir Al Netilat Yadayim:\n"
        "• Primer lavado al despertar, antes de tocar el suelo — tres vertidos, sin berajá\n"
        "• Luego: baño, enjuague bucal, aseo personal\n"
        "• Segundo lavado en el lavabo: Al Netilat Yadayim, Asher Yatzar y Elokai Neshama"
    ),
    "Sefard minhag — when": (
        "Minhag sefardí — cuándo decir Al Netilat Yadayim:\n"
        "• Lavado para quitar ruach ra'ah del sueño (Shulján Aruj, Rasha)\n"
        "• Berajá justo después del primer lavado matutino, idealmente de inmediato\n"
        "• Urgencia de baño: primero el baño, luego lavado y berajá"
    ),
    "Blessing timing overview": (
        "Resumen del minhag (elige nusaj en Ajustes para el texto completo):\n"
        "• Ashkenaz: en general sin berajá junto a la cama; berajá tras el segundo lavado\n"
        "• Sefaradí / muchas edot ha-mizraj: berajá tras lavar, antes de secar\n"
        "• Algunas kehillot yemenitas (Baladi): lavado tras baño — consulta a tu rav\n"
        "• Jabad: sin berajá en el primer lavado; berajá en el segundo en el lavabo"
    ),
    "Edot HaMizrach minhag (many kehillot)": (
        "Minhag edot ha-mizraj (muchas kehillot) — cuándo decir Al Netilat Yadayim:\n"
        "• Lavado para quitar ruach ra'ah del sueño (Shulján Aruj, Rasha)\n"
        "• Berajá justo después del primer lavado matutino, idealmente de inmediato\n"
        "• Urgencia de baño: primero el baño, luego lavado y berajá\n\n"
        "Baladi yemenita y algunas kehillot lavan tras el baño antes de la berajá — sigue el psak de tu comunidad."
    ),
}
for _nk in strings:
    if not _nk.startswith("Immediately after saying Modeh Ani, wash your hands"):
        continue
    for _marker, _section in _NETILAT_ES_MINHAG.items():
        if _marker in _nk:
            ES[_nk] = _NETILAT_ES_HEAD + _section + _NETILAT_ES_TAIL
            break
    else:
        ES[_nk] = _NETILAT_ES

_WHY_NIGHT_ES = (
    "Por qué aprender de noche — lo que prometen los sabios:\n\n"
    "El estudio de Torá nocturno tiene lugar especial. El Talmud alaba a quien establece palabras de Torá de noche — "
    "«Me levanto a medianoche para agradecerte» le aplica (Tehilim 119:62; Shabat 119a). "
    "Tras el ruido del día, aprender de noche es unión silenciosa con Hashem: la Shejiná mora donde se habla Torá (Avot 3:2–3).\n\n"
    "El estudio protege al que aprende — está entre las tres guardianas de la vida (Sotá 21a). "
    "Incluso un seder corto antes de dormir puede calmar la mente, santificar el fin del día y rodear el hogar de mérito.\n\n"
    "El estudio de Torá pesa como todas las demás mitzvot juntas (Peá 1:1). No necesitas horas — la constancia importa más que la duración.\n\n"
    "La obligación:\n"
    "Todo judío debe aprender al menos un poco de Torá durante el día. La noche es un momento excelente si el día fue ocupado.\n\n"
    "Consejo práctico:\n"
    "Unos minutos de halajá o un pasaje de Torá antes de dormir — sin pantallas si puedes — endulza el sueño y cierra el día con santidad."
)
_WHY_NIGHT_FR = (
    "Pourquoi apprendre la nuit — les sages promettent un lieu spécial à l'étude nocturne (Shabbat 119a). "
    "La Chekhina demeure où l'on parle Torah. Quelques minutes avant de dormir sanctifient la fin de la journée. "
    "La constance compte plus que la durée (Peah 1:1)."
)
k = key_by_prefix["Why learn at night — what the sages promise"]
ES[k] = _WHY_NIGHT_ES
FR[k] = _WHY_NIGHT_FR

_AGGADAH_ES = (
    "Aggadá es la narrativa y enseñanza no legal en el Talmud y midrash — ética, teología y relatos. "
    "Complementa la halajá (ley). Las historias de mesa de Shabat y muchos midrashim son aggadá."
)
_AGGADAH_FR = (
    "Aggadah : récits et enseignements non juridiques du Talmud et midrash — éthique, théologie, narrative. "
    "Complète la halakha. Histoires de table de Chabbat et midrashim."
)
k = key_by_prefix["Aggadah is the non-legal storytelling"]
ES[k] = _AGGADAH_ES
FR[k] = _AGGADAH_FR
k = key_by_prefix["aggadah — Aggadah is the non-legal"]
ES[k] = "aggadá — " + _AGGADAH_ES
FR[k] = "aggadah — " + _AGGADAH_FR

_TUMAH_ES = (
    "Tumah es impureza ritual — un estado halájico que afecta lo que puedes tocar o comer "
    "(leyes de la era del Templo, niddah, contacto con cadáver, etc.). "
    "No es culpa moral ni lo mismo que estar «sucio». "
    "Muchas leyes de tumah aplican hoy en taharat ha-mishpajá y reglas de kohanim; otras esperan el Templo. "
    "La tumah matutina en las manos se quita con lavado ritual, no solo con jabón."
)
_TUMAH_FR = (
    "Toumah : impureté rituelle — état halakhique (Temple, niddah, contact avec un mort). "
    "Pas une faute morale. Toumah matinale des mains : enlèvement par lavage rituel, pas seulement savon."
)
k = key_by_prefix["Tumah is ritual impurity"]
ES[k] = _TUMAH_ES
FR[k] = _TUMAH_FR
k = key_by_prefix["tumah — Tumah is ritual impurity"]
ES[k] = "tumah — " + _TUMAH_ES
FR[k] = "tumah — " + _TUMAH_FR

_SIMCHAT_TORAH_ES = (
    "Simjat Torá (23 Tishrei en la Diáspora) — regocijarse con la Torá.\n\n"
    "Yom Tov:\n"
    "• Yom Tov completo — sin melajá; Kidush, comidas festivas, alegría\n"
    "• Sucá: la mayoría de ashkenazíes no comen en la sucá en Simjat Torá (ya no es Sucot) — la práctica sefardí varía; pregunta a tu rav\n"
    "• Oración: Hallel completo y Musaf (como otros días de Yom Tov)\n\n"
    "La mitzvá del día — simjá y Torá:\n"
    "• Hakafot — rodear la bimá con rollos de Torá, cantando y bailando\n"
    "• Nota: como suele haber bebida en hakafot diurnas, muchas sinagogas mueven Birkat Kohanim a Shaharit temprano "
    "para que los kohanim estén sobrios\n"
    "• Completar la última parashá de Devarim y empezar Bereshit — la Torá no termina\n"
    "• Aliyot — Kol HaNearim, Jatán Torá, Jatán Bereshit y honores\n\n"
    "Consejos:\n"
    "• Vístete festivo; llega temprano\n"
    "• Si tienes un honor de la Torá, prepara tus berajot\n"
    "• Celebra con responsabilidad — amor a la Torá, no exceso\n\n"
    "Velas anoche (última noche de Yom Tov); havdalá al terminar (con textos de Sucot/Yom Tov según tu sidur)."
)
_SIMCHAT_TORAH_FR = (
    "Sim'hat Torah (23 Tichri en Diaspora) — se réjouir avec la Torah.\n\n"
    "Yom Tov :\n"
    "• Yom Tov complet — pas de melakha ; Kiddoush, repas festifs, joie\n"
    "• Soucca : la plupart des ashkénazes ne mangent pas dans la soucca — pratique séfarade variable ; demande à ton rav\n"
    "• Prière : Hallel complet et Moussaf\n\n"
    "Mitsva du jour — sim'ha et Torah :\n"
    "• Hakafot — tourner la bimah avec les rouleaux, chanter et danser\n"
    "• Fin de Devarim et début de Bereshit — la Torah ne finit jamais\n"
    "• Aliyot — Kol HaNearim, 'Hatan Torah, 'Hatan Bereshit\n\n"
    "Arrivez tôt, préparez vos bénédictions ; célébrez avec responsabilité. "
    "Bougies hier soir ; Havdala en fin de Yom Tov (textes Souccot selon siddour)."
)
k = key_by_prefix["Simchat Torah (23 Tishrei in the Diaspora)"]
ES[k] = _SIMCHAT_TORAH_ES
FR[k] = _SIMCHAT_TORAH_FR

_SHEMINI_ATZERET_ES = (
    "Shemini Atzeret (22 Tishrei en la Diáspora) — octavo día de Sukkot.\n\n"
    "Yom Tov:\n"
    "• Yom Tov completo — sin melajá; Kidush y comidas festivas.\n"
    "• Sucá en la Diáspora: por safek yom tov (duda halájica de qué día es cuál), los ashkenazíes deben comer "
    "las comidas principales en la sucá en Shemini Atzeret, aunque se omite leishev ba-sucá. Costumbres sefardíes y jabadíes varían — confirma con tu rav.\n\n"
    "Oración:\n"
    "• Cambio litúrgico: en Musaf hoy el mundo judío pasa al ciclo de invierno, insertando «Mashiv ha-ruaj u-morid ha-geshem» "
    "en la segunda berajá de la Amidá. Tefilat Geshem en Musaf.\n"
    "• Yizkor — comunidades ashkenazíes recitan memorias.\n"
    "• Hallel completo y Musaf; Amidá de Yom Tov.\n\n"
    "Simjat Torah es mañana (23 Tishrei) en la Diáspora — ver el ítem de ese día para hakafot.\n\n"
    "Esta noche: velas de Yom Tov; sin lulav en Shemini Atzeret."
)
_SHEMINI_ATZERET_FR = (
    "Chemini Atseret (22 Tichri en Diaspora) — huitième jour de Souccot.\n\n"
    "Yom Tov :\n"
    "• Yom Tov complet — pas de melakha ; Kiddoush et repas festifs.\n"
    "• Soucca en Diaspora : safek yom tov — ashkénazes mangent les repas principaux dans la soucca, "
    "sans leishev ba-soucca ; coutumes séfarades et 'Habad varient — demande à ton rav.\n\n"
    "Prière :\n"
    "• En Moussaf, passage au cycle d'hiver : « Mashiv haroua'h oumorid haguéshem » dans la 2e bénédiction de l'Amida. Tefilat Guéshem.\n"
    "• Yizkor dans les communautés ashkénazes ; Hallel complet et Moussaf.\n\n"
    "Sim'hat Torah demain (23 Tichri) en Diaspora.\n\n"
    "Ce soir : bougies de Yom Tov ; pas de loulav."
)
k = key_by_prefix["Shemini Atzeret (22 Tishrei in the Diaspora)"]
ES[k] = _SHEMINI_ATZERET_ES
FR[k] = _SHEMINI_ATZERET_FR

_EREV_YK_ES = (
    "Hoy es Erev Yom Kipur — un día único de comer antes del ayuno más sagrado del año.\n\n"
    "Mitzvá de comer hoy:\n"
    "• Es mitzvá comer y beber generosamente todo Erev Yom Kipur (Shulján Aruj O.J. 604:1; Mishná Berurá).\n"
    "• Haz una comida festiva estilo Yom Tov — muchos empiezan con velas y Kidush (pregunta a tu rav tu minhag).\n"
    "• El Talmud compara comer en Erev Yom Kipur con ayunar en Yom Kipur para el mérito (Rosh Hashaná 9a).\n\n"
    "Antes de que empiece el ayuno:\n"
    "• Tiempo halájico del ayuno: Yom Kipur va del atardecer de esta noche$sunsetLine hasta la caída de la noche de mañana ($tzeitTomorrow).\n"
    "• Tosefet (añadir del día de semana): muchas comunidades dejan de comer y beber un poco antes del atardecer — "
    "aceptar el ayuno temprano (Yoma 81b; Shulján Aruj O.J. 608:1). Los minutos varían; pregunta a tu rav. "
    "La hora del atardecer en esta app es cuando el ayuno ya está en vigor, no necesariamente cuando tu sinagoga empieza Kol Nidré.\n"
    "• Kol Nidré antes del atardecer: anulación de votos (hatarat nedarim) antes del Día del Perdón. "
    "Un beit din no puede reunirse en Yom Kipur, así que Kol Nidré debe terminar antes del atardecer (Rosh Hashaná 9b; Shulján Aruj O.J. 619:1). "
    "Por eso los servicios suelen empezar mucho antes del atardecer.\n"
    "• Pide perdón a quien hayas lastimado; da tzedaqá; tevilá en mikvé si es tu costumbre.\n"
    "• Enciende velas de Yom Kipur antes del atardecer (las mujeres casadas suelen encenderlas; otros siguen la comunidad — pregunta a tu rav).\n\n"
    "Comida de Motzei Yom Kipur:\n"
    "• Tras el ayuno mañana por la noche, es mitzvá comer una comida adecuada — no solo un bocadillo. Muchos hacen un break-fast festivo.\n"
    "• La tradición jabadí (Baal HaTanya) conecta el sustento del año (parnasá) con esta comida — come con alegría tras oración y Havdalá.\n\n"
    "Quién ayuna mañana: adultos judíos sanos desde bar/bat mitzvá. Enfermos, embarazadas, lactantes o recién paridas deben preguntar a un rav — "
    "a menudo comen en shiurim (cantidades medidas) o están exentos."
)
_EREV_YK_FR = (
    "Aujourd'hui c'est Erev Yom Kippour — jour unique de manger avant le jeûne le plus saint de l'année.\n\n"
    "Mitzva de manger aujourd'hui :\n"
    "• Mitzva de manger et boire généreusement tout Erev Yom Kippour (Choulhan Aroukh O.H. 604:1 ; Michna Beroura).\n"
    "• Repas festif style Yom Tov — beaucoup commencent par bougies et Kiddoush (demande à ton rav votre minhag).\n"
    "• Le Talmud compare manger à Erev Yom Kippour à jeûner à Yom Kippour pour le mérite (Rosh Hashana 9a).\n\n"
    "Avant le jeûne :\n"
    "• Temps halakhique : Yom Kippour du coucher du soleil ce soir$sunsetLine jusqu'à la nuit de demain ($tzeitTomorrow).\n"
    "• Tosefet : beaucoup s'abstiennent un peu avant le coucher du soleil (Yoma 81b ; Choulhan Aroukh O.H. 608:1) — demande à ton rav.\n"
    "• Kol Nidre avant le coucher du soleil : annulation des vœux ; un beit din ne peut siéger à Yom Kippour (Rosh Hashana 9b ; Choulhan Aroukh O.H. 619:1).\n"
    "• Demandez pardon ; tsedaka ; tévila au mikvé si coutume.\n"
    "• Allumez les bougies de Yom Kippour avant le coucher du soleil.\n\n"
    "Repas de Motzei Yom Kippour : mitzva de manger un vrai repas après la prière et la Havdala.\n\n"
    "Qui jeûne demain : adultes juifs en bonne santé dès bar/bat mitzva ; malades, femmes enceintes ou allaitantes — demandez à un rav."
)
k = key_by_prefix["Today is Erev Yom Kippur — a unique day"]
ES[k] = _EREV_YK_ES
FR[k] = _EREV_YK_FR

_YOM_KIPPUR_LONG_ES = (
    "Yom Kipur — Día del Perdón. Ayuno completo de 25 horas; cinco privaciones desde el atardecer de hoy "
    "hasta la caída de la noche de mañana.\n\n"
    "Antes del ayuno:\n"
    "• Mitzvá de comer: obligación halájica única de comer y beber todo Erev Yom Kipur (Berajot 8b). "
    "Comer hoy y ayunar mañana son dos mitades de la misma mitzvá — comidas normales, no solo la comida previa al ayuno.\n"
    "• Seudá hamafseket festiva antes del atardecer — termina a tiempo.\n"
    "• Enciende velas antes de Kol Nidré (neir shel Yom HaKipurim según tu sidur); todas las llamas antes del atardecer. "
    "Yom Kipur tiene las mismas restricciones de fuego que Shabat.\n"
    "• Tras encender y la berajá, Yom Kipur ha comenzado — no conduzcas. Tnai con tu rav si enciendes en casa e irás en coche.\n"
    "• Tzedaqá y pedir perdón. Kaparot (si es tu costumbre) antes de Yom Kipur.\n\n"
    "En Yom Kipur (sin comer, beber, lavarse por placer, ungirse, zapatos de cuero ni relaciones maritales):\n"
    "• Oración en la sinagoga (Kol Nidré esta noche, servicios todo el día).\n"
    "• Ropa blanca es costumbre extendida; prohibición estricta de calzado de cuero (uno de los cinco inuyim).\n"
    "• Ne'ilá al final; tras la noche Maariv y Havdalá sobre vino y ner she-shavat (vela de 48 h encendida antes del ayuno). "
    "Sin besamim salvo que Yom Kipur caiga en Shabat. Luego rompe el ayuno."
)
_YOM_KIPPUR_LONG_FR = (
    "Yom Kippour — Jour du Pardon. Jeûne complet de 25 heures ; cinq privations du coucher du soleil ce soir "
    "jusqu'à la nuit de demain.\n\n"
    "Avant le jeûne :\n"
    "• Mitzva de manger : obligation halakhique unique de manger et boire tout Erev Yom Kippour (Berakhot 8b).\n"
    "• Seouda hamafseket festive avant le coucher du soleil.\n"
    "• Bougies avant Kol Nidre (neir shel Yom Hakippourim) ; toutes les flammes avant le coucher du soleil.\n"
    "• Une fois allumées et bénies, Yom Kippour a commencé — pas de conduite ; tnaï avec votre rav si besoin.\n"
    "• Tsedaka et demander pardon.\n\n"
    "Pendant Yom Kippour (pas manger, boire, se laver pour plaisir, s'oindre, chaussures en cuir, relations conjugales) :\n"
    "• Prière à la synagogue ; vêtements blancs coutumiers ; interdiction stricte du cuir.\n"
    "• Neïla ; après la nuit Maariv et Havdala sur vin et ner she-shavat. Pas de besamim sauf si Yom Kippour tombe un Chabbat."
)
k = key_by_prefix["Yom Kippur — Day of Atonement. Full 25-hour"]
ES[k] = _YOM_KIPPUR_LONG_ES
FR[k] = _YOM_KIPPUR_LONG_FR

_WHO_MUST_FAST_ES = (
    "Quién debe ayunar:\n"
    "Adultos judíos desde bar/bat mitzvá con salud suficiente. Los niños se educan gradualmente — pregunta a tu rav. "
    "Embarazadas, lactantes, recién paridas o enfermos deben consultar a un posek — muchos comen en shiurim (cantidades medidas) o están exentos.\n\n"
    "Si comiste por error:\n"
    "Si olvidaste y comiste o bebiste sin querer, puedes seguir ayunando al recordarlo — el ayuno sigue válido (Shulján Aruj O.J. 568:1).\n\n"
    "Si no puedes ayunar:\n"
    "No pongas en riesgo tu salud. Pregunta a un rav sobre shiurim, posponer el ayuno o exención. Pikuaj nefesh prevalece sobre el ayuno."
)
_WHO_MUST_FAST_FR = (
    "Qui doit jeûner :\n"
    "Adultes juifs dès bar/bat mitzva en bonne santé. Les enfants s'habituent progressivement — demande à ton rav. "
    "Femmes enceintes, allaitantes, récemment accouchées ou malades : consultez un posek — beaucoup mangent en shiourim ou sont exemptés.\n\n"
    "Si vous avez mangé par erreur :\n"
    "Oubli involontaire : vous pouvez continuer à jeûner — le jeûne reste valide (Choulhan Aroukh O.H. 568:1).\n\n"
    "Si vous ne pouvez pas jeûner :\n"
    "Ne mettez pas votre santé en danger. Demandez à un rav pour shiourim, report ou exemption. Pikuah nefesh prime."
)
k = key_by_prefix["Who must fast:"]
ES[k] = _WHO_MUST_FAST_ES
FR[k] = _WHO_MUST_FAST_FR

_BIRKAT_HATORAH_ES = (
    "Antes de abrir un libro — recuerda qué es el estudio de la Torá.\n\n"
    "Las berajot que vas a decir existen porque la Torá es cómo nos unimos a Hashem. "
    "Los Sabios enseñan que aprender Torá supera a todas las demás mitzvot juntas (Peá 1:1); "
    "la Shejiná se une a quien habla palabras de Torá (Avot 3:2-3); "
    "el estudio protege tu vida en este mundo y en el próximo (Sotá 21a). "
    "Birkat HaTorá es la puerta a esa realidad — pedir que la Torá sea dulce en tus labios, no pesada.\n\n"
    "Cada día, antes de aprender Torá por primera vez, decimos Birkat HaTorá. "
    "Para los hombres es ampliamente obligación de nivel toráico; el nivel para mujeres se debate entre poskim — "
    "todos coinciden en que quien aprende Torá debe decir estas berajot.\n\n"
    "Tres berajot cortas:\n"
    "1. Agradecer a D-s por mandarnos estudiar Torá\n"
    "2. Pedir que las palabras de Torá sean dulces en nuestra boca y la de nuestros hijos\n"
    "3. Alabar a D-s por darnos la Torá\n\n"
    "Tras las berajot, leemos pasajes breves de Torá (cumple también un mínimo diario de estudio).\n\n"
    "Por qué importan: el Talmud enseña que muchas calamidades vinieron de no decir las berajot antes de estudiar. "
    "La Torá no es solo intelecto — es regalo divino y herencia sagrada.\n\n"
    "Momento: una vez por la mañana; cubren todo el estudio del día. "
    "Desde después de jamot ha-lailá (media noche halájica) — si te despiertas muy temprano, dilas."
)
_BIRKAT_HATORAH_FR = (
    "Avant d'ouvrir un livre — rappelez-vous ce qu'est l'étude de la Torah.\n\n"
    "Les bénédictions existent parce que la Torah est notre lien avec Hashem (Peah 1:1 ; Avot 3:2-3 ; Sotah 21a). "
    "Birkat HaTorah — trois brachot : commander d'étudier, douceur des paroles, don de la Torah. "
    "Puis courts passages bibliques. Une fois le matin, dès 'hatzot halaila. "
    "Obligation toranique pour les hommes ; femmes qui étudient — tous s'accordent à les dire."
)
k = key_by_prefix["Before you open a book — remember what Torah study is"]
ES[k] = _BIRKAT_HATORAH_ES
FR[k] = _BIRKAT_HATORAH_FR

_SHEMINI_ISRAEL_ES = (
    "Shemini Atzeret / Simjat Torá (22 Tishrei en Israel) — un día que combina ambos.\n\n"
    "Yom Tov:\n"
    "• Yom Tov completo — sin melajá; comidas festivas con Kidush y Shehejejanu (si no lo dijiste ya).\n"
    "• Sucot ha terminado — en Israel no comas ni duermas en la sucá hoy; las comidas son en interiores.\n\n"
    "Oración:\n"
    "• Cambio litúrgico en Musaf: «Mashiv ha-ruaj u-morid ha-geshem» en la segunda berajá de la Amidá; Tefilat Geshem.\n"
    "• Yizkor en muchas comunidades ashkenazíes.\n"
    "• Hakafot — bailar con rollos de Torá; terminar Devarim y empezar Bereshit.\n"
    "• Nota: muchas sinagogas mueven Birkat Kohanim a Shaharit temprano por hakafot diurnas.\n"
    "• Hallel completo y Musaf; Amidá de Yom Tov.\n\n"
    "Alegría de Simjat Torá:\n"
    "• Todos reciben aliyá en muchas sinagogas; Kol HaNearim con banderas — un adulto recita la berajá en voz alta.\n"
    "• Canto y baile — honra el día con celebración de la Torá.\n\n"
    "Noche: velas de Yom Tov; los servicios matutinos son largos — planifica."
)
_SHEMINI_ISRAEL_FR = (
    "Chemini Atseret / Sim'hat Torah (22 Tichri en Israël) — un jour réunissant les deux.\n\n"
    "Yom Tov complet ; repas en intérieur (la Soucca est terminée en Israël).\n\n"
    "Prière : Mashiv haroua'h oumorid haguéshem en Moussaf ; Yizkor ; Hakafot ; fin de Devarim et début de Bereshit. "
    "Birkat Kohanim souvent déplacé à Shaharit tôt. Kol HaNearim, joie et aliyot pour tous.\n\n"
    "Bougies ce soir ; services longs le matin — prévoyez."
)
k = key_by_prefix["Shemini Atzeret / Simchat Torah (22 Tishrei in Israel)"]
ES[k] = _SHEMINI_ISRAEL_ES
FR[k] = _SHEMINI_ISRAEL_FR

_TZITZIT_ES = (
    "La Torá manda a los hombres judíos llevar flecos en las esquinas de prendas de cuatro puntas — "
    "recordatorio físico constante de todos los mandamientos de D-s.\n\n"
    "Qué son: tzitzit (צִיצִית) — hilos anudados en cada esquina; 8 hilos y 5 nudos por juego. "
    "Una derashá (Bamidbar Rabá) vincula gematría 600 + 8 + 5 con 613 — símbolo, no cálculo halájico.\n\n"
    "Fuente: «Hazte flecos en las esquinas de tus vestidos… para que los miren y recuerden todos los mandamientos de D-s» (Bamidbar 15:38-39).\n\n"
    "Cómo observarlo: tallit katán bajo la camisa todo el día; tallit gadol en Shaharit.\n\n"
    "Para empezar: compra en judaica o en línea; pide a tu rav que te enseñe a ponértelo y la berajá; "
    "revisa los hilos periódicamente — si se rompe uno, puede invalidarlos."
)
_TZITZIT_FR = (
    "La Torah commande aux hommes juifs des franges (tzitzit) aux coins des vêtements à quatre bords — "
    "rappel physique des mitzvot (Bamidbar 15:38-39). Tallit katan toute la journée ; tallit gadol à Shaharit. "
    "Achetez en judaica ; demande à ton rav la bénédiction et vérifiez les fils."
)
k = key_by_prefix["The Torah commands Jewish men to wear fringes"]
ES[k] = _TZITZIT_ES
FR[k] = _TZITZIT_FR

_YAALEH_ES = (
    "Yaale ve-Yavo es un párrafo especial en la Amidá y en Birkat HaMazón en Rosh Jodesh, Yom Tov y Jol HaMoed.\n\n"
    "Pide a D-s que nos recuerde a nosotros, a nuestros padres, a Jerusalén, la dinastía davídica y todo Israel "
    "para bien — vida y paz — en el día que se observa.\n\n"
    "Si lo olvidaste en Shaharit o Minjá (también Jol HaMoed / Yom Tov en cualquier Amidá incluyendo Arvit):\n"
    "• Aún en Retzei — inserta Yaale ve-Yavo y continúa.\n"
    "• Tras concluir Retzei — vuelve al inicio de Retzei, inserta y completa las berajot restantes.\n"
    "• Tras el Yihiyu le-ratzon final — repite solo esa Amidá (Shemone Esre), nunca el servicio completo.\n\n"
    "Solo Arvit de Rosh Jodesh: si olvidaste tras terminar Retzei o toda la Amidá — no repitas (Berajot 30b; Shulján Aruj O.J. 422:1). "
    "Si aún estás en Retzei antes del Nombre, insértalo ahí."
)
_YAALEH_FR = (
    "Yaale ve-Yavo — paragraphe spécial dans l'Amida et le Birkat HaMazon à Rosh 'Hodesh, Yom Tov et hol hamoed.\n\n"
    "Demande à D.ieu de se souvenir de nous, de nos pères, de Jérusalem, de la dynastie davidique et de tout Israël "
    "pour le bien — vie et paix — le jour observé.\n\n"
    "Si tu l'as oublié à Shaharit ou Min'ha (aussi hol hamoed / Yom Tov à toute Amida y compris Maariv) :\n"
    "• Encore dans Retzei — insère Yaale ve-Yavo et continue.\n"
    "• Après avoir conclu Retzei — reviens au début de Retzei, insère et achève les bénédictions restantes.\n"
    "• Après le Yihiyou le-ratson final — répète seulement cette Amida (Shemoneh Esrei), jamais tout le service.\n\n"
    "Maariv de Rosh 'Hodesh seul : oubli après Retzei ou toute l'Amida — ne répète pas (Berakhot 30b; "
    "Choulhan 'Aroukh O.C. 422:1). Si tu es encore dans Retzei avant le Nom, insère-le là."
)
k = key_by_prefix["Yaaleh V'Yavo is a special prayer paragraph"]
ES[k] = _YAALEH_ES
FR[k] = _YAALEH_FR

_PURIM_FULL_ES = (
    "Purim Meshulash (פורים מְשֻׁלָּשׁ) — Jerusalén cuando Purim de Shushan (15 Adar) cae en Shabat\n\n"
    "Por qué el calendario se divide:\n"
    "• Tu observancia de Purim (ciudad amurallada / Jerusalén) sería normalmente 15 Adar, "
    "pero las mitzvot de Purim no se hacen en Shabat.\n"
    "• Meguilá y matanot la'evyonim pasan al viernes (14 Adar). Mishloaj manot y la seudá al domingo (16 Adar). "
    "Shabat (15 Adar): las mitzvot del hogar no ocurren hoy, pero Shabat lleva obligaciones comunitarias de Purim — véase abajo.\n\n"
    "Lee esta guía completa antes de Shabat — tu teléfono estará apagado en Shabat, "
    "así que no verás la lista del domingo hasta después de Havdalá.\n\n"
    "Las cuatro mitzvot — cuándo ocurren este año:\n"
    "1. Meguilá — jueves por la noche tras el anochecer (inicio del viernes / 14 Adar) Y viernes de día (antes del atardecer). "
    "Dos lecturas, como Purim normal. Confirma horarios con tu sinagoga; anótalos o imprímelos.\n"
    "2. Matanot la'evyonim — solo el viernes de día (no jueves por la noche, no Shabat). "
    "Al menos un regalo a cada uno de dos pobres distintos (mínimo dos destinatarios); el dinero es común. "
    "Prepara efectivo, sobres o contactos de caridad antes del viernes.\n"
    "3. Mishloaj manot — domingo (16 Adar) antes del atardecer. Al menos dos comidas listas para un amigo. "
    "Prepara y etiqueta paquetes antes de Shabat; planifica quién entrega el domingo.\n"
    "4. Seudá de Purim — domingo por la tarde (16 Adar), antes del atardecer. Comida festiva con pan, alegría y palabras de Torá — no en Shabat.\n\n"
    "Qué terminar antes de las velas de Shabat (viernes):\n"
    "• Meguilá: asiste jueves por la noche y viernes por la mañana (o conoce el horario de tu sinagoga).\n"
    "• Matanot: completa el viernes — ten fondos listos el viernes por la mañana.\n"
    "• Mishloaj: paquetes empacados, etiquetados y guardados; lista de entrega escrita (solo domingo).\n"
    "• Seudá: menú y horario del domingo planificados; invita si hace falta.\n"
    "• Majatzit ha-shekel: si es tu costumbre, muchos dan antes de la Meguilá — hazlo antes o con las lecturas de jueves/viernes.\n\n"
    "Shabat (15 Adar):\n"
    "• Las mitzvot del hogar (Meguilá, matanot, mishloaj, seudá) no ocurren hoy, pero Shabat lleva obligaciones comunitarias de Purim.\n"
    "• Sinagoga: asiste a la lectura especial de Purim (Parashat Vayavo Amalek, Shemot 17:8-16) y la haftará única de Purim.\n"
    "• Oración y comidas: inserta Al HaNissim en todas las oraciones de Shabat (Amidá) y Birkat HaMazón hoy. "
    "No digas Al HaNissim el viernes (14 Adar) ni el domingo (16 Adar), aunque otras mitzvot de Purim ocurran esos días.\n"
    "• Sin melajá para preparar Purim en Shabat — todo para el domingo debe estar listo.\n\n"
    "Domingo (16 Adar):\n"
    "• Envía mishloaj manot y celebra la seudá de Purim. La app mostrará los ítems de hoy tras Shabat.\n\n"
    "Pregunta a tu rav sobre casos límite (viaje, enfermedad, minhag)."
)
_PURIM_FULL_FR = (
    "Pourim Mechoulach (פורים מְשֻׁלָּשׁ) — Jérusalem quand Pourim de Shoushan (15 Adar) tombe un Chabbat\n\n"
    "Pourquoi le calendrier se divise :\n"
    "• Votre Pourim (ville murée / Jérusalem) serait normalement le 15 Adar, mais les mitzvot de Pourim ne se font pas le Chabbat.\n"
    "• Meguila et matanot la'evyonim le vendredi (14 Adar). Mishloah manot et seoudah le dimanche (16 Adar).\n\n"
    "Lisez ce guide entier avant Chabbat — votre téléphone sera éteint ; vous ne verrez la checklist du dimanche qu'après la Havdala.\n\n"
    "Les quatre mitzvot :\n"
    "1. Meguila — jeudi soir après la nuit et vendredi en journée. Confirmez les horaires avec votre synagogue.\n"
    "2. Matanot la'evyonim — vendredi en journée seulement.\n"
    "3. Mishloah manot — dimanche avant le coucher du soleil.\n"
    "4. Seouda de Pourim — dimanche après-midi.\n\n"
    "Chabbat (15 Adar) : lecture Vayavo Amalek, haftara de Pourim, Al HaNissim dans l'Amida et le bentching — pas vendredi ni dimanche.\n"
    "Dimanche : mishloah et seoudah. Demande à ton rav pour les cas limites."
)
k = key_by_prefix["Purim Meshulash (פורים מְשֻׁלָּשׁ) — Jerusalem when Shushan"]
ES[k] = _PURIM_FULL_ES
FR[k] = _PURIM_FULL_FR

_TZEDAKAH_ES = (
    "Dar caridad (tzedaqá — צְדָקָה) es una mitzvá constante. Una costumbre fundamental es maaser kesafim (מַעֲשֵׂר כְּסָפִים) — "
    "apartar alrededor del diez por ciento de los ingresos para caridad.\n\n"
    "Cómo calcular:\n"
    "El maaser suele calcularse sobre ingresos netos, no brutos: empieza con lo que realmente recibes, "
    "deduce impuestos, gastos laborales necesarios y gastos razonables del negocio antes del diez por ciento. "
    "Pregunta a tu rav sobre tu situación (autónomo, beneficios, ingresos únicos, etc.).\n\n"
    "Dónde dar:\n"
    "Prioridades halájicas: pobres locales, instituciones de Torá y necesitados de Israel. "
    "Buenas opciones: organizaciones locales de ayuda familiar, despensas comunitarias y caridades de confianza en tu zona e Israel.\n\n"
    "Si el dinero aprieta:\n"
    "La ley judía enseña que tu sustento y necesidades básicas van primero. "
    "Si no puedes dar el diez por ciento completo sin dificultad real, no se espera que comprometas tu estabilidad. "
    "Da una cantidad mínima simbólica cuando puedas y aumenta cuando las circunstancias lo permitan.\n\n"
    "Este ítem es un recordatorio diario para planificar tu dar con intención. Confirma montos y destinatarios con tu rav."
)
_TZEDAKAH_FR = (
    "La tsedaka (צְדָקָה) est une mitsva constante. Maasser kesafim — environ dix pour cent des revenus pour la charité.\n\n"
    "Calcul sur le net : impôts et frais déduits avant les dix pour cent — demande à ton rav.\n"
    "Priorités : pauvres locaux, institutions de Torah, nécessiteux d'Israël.\n"
    "Si l'argent manque : vos besoins passent d'abord ; don symbolique minimal si possible."
)
k = key_by_prefix["Giving charity (tzedakah — צְדָקָה) is a constant"]
ES[k] = _TZEDAKAH_ES
FR[k] = _TZEDAKAH_FR

_EREV_PURIM_MESH_ES = (
    "Mañana es erev Purim — y este año es Purim Meshulash en Jerusalén. Shabat está en medio, "
    "así que necesitas el plan completo ahora (no solo mañana).\n\n$scheduleBlock"
)
_EREV_PURIM_MESH_FR = (
    "Demain c'est erev Pourim — et cette année c'est Pourim Mechoulach à Jérusalem. Chabbat est au milieu — "
    "il vous faut le plan complet maintenant (pas seulement demain).\n\n$scheduleBlock"
)
k = key_by_prefix["Tomorrow is erev Purim — and this year is Purim Meshulash"]
ES[k] = _EREV_PURIM_MESH_ES
FR[k] = _EREV_PURIM_MESH_FR

_MISHLOACH_SUN_ES = (
    "Purim Meshulash — mishloaj manot el domingo (16 Adar)\n\n"
    "Aplazado desde Shabat porque las mitzvot de Purim no se realizan en Shabat este año.\n\n"
    "La mitzvá:\n"
    "• Envía al menos dos comidas distintas listas para comer a un amigo hoy — un paquete.\n"
    "• Entrega antes del atardecer; un mensajero vale.\n"
    "• La comida debe estar lista sin cocinar; etiqueta remitente y destinatario.\n\n"
    "Deberías haber preparado paquetes antes de Shabat. Si no, pregunta a tu rav qué puedes hacer hoy."
)
_MISHLOACH_SUN_FR = (
    "Pourim Mechoulach — mishloah manot dimanche (16 Adar)\n\n"
    "Reporté depuis Chabbat — les mitzvot de Pourim ne se font pas le Chabbat.\n\n"
    "Mitsva : au moins deux aliments prêts à manger à un ami — livrer avant le coucher du soleil. "
    "Préparez avant Chabbat ; sinon demande à ton rav."
)
k = key_by_prefix["Purim Meshulash — mishloach manot on Sunday"]
ES[k] = _MISHLOACH_SUN_ES
FR[k] = _MISHLOACH_SUN_FR

_YOM_ATZMAUT_ES = (
    "Yom HaAtzmaut (5 Iyar) conmemora la independencia de Israel en 1948. No es Yom Tov bíblico ni rabínico — la melajá está totalmente permitida.\n\n"
    "Las costumbres varían mucho según la comunidad:\n\n"
    "Sionista religioso / ortodoxo moderno: se recita Hallel (instituido por el Rabinado de Israel). "
    "Si se dice con berajá está en disputa — el Rabinado instruyó con berajá; muchos poskim ashkenazíes fuera de Israel sin berajá. "
    "Se omite Tachanun. Algunas comunidades añaden oraciones festivas (Hallel u-Maariv).\n\n"
    "Sefardíes (Rav Ovadia Yosef / Yalkut Yosef): Rav Ovadia Yosef dictaminó no recitar Hallel (temor de berajá levatala — el día no fue establecido por Jazal). "
    "La omisión de Tachanun también se discute.\n\n"
    "Jabad: el Rebe no instituyó observancia especial. La mayoría no dice Hallel y recita Tachanun como siempre.\n\n"
    "Comunidades jaredíes (Agudá, litvakes): generalmente no observan el día como fiesta religiosa. Tachanun como siempre.\n\n"
    "El Omer se cuenta con normalidad. No hay Al HaNissim en la oración.\n\n"
    "Pregunta a tu rav qué costumbre sigue tu comunidad."
)
_YOM_ATZMAUT_FR = (
    "Yom HaAtsmaout (5 Iyar) — indépendance d'Israël en 1948. Pas Yom Tov biblique ou rabbinique — melacha permise.\n\n"
    "Religieux sionistes : Hallel (Rabbinat) — avec ou sans bénédiction selon poskim. Pas de Tahanoun.\n"
    "Séfarades (Rav Ovadia Yosef) : pas de Hallel. 'Habad et haredim : pas d'observance religieuse spéciale.\n"
    "Omer compté normalement ; pas d'Al HaNissim. Demande à ton rav."
)
k = key_by_prefix["Yom Ha'atzmaut (5 Iyar) commemorates"]
ES[k] = _YOM_ATZMAUT_ES
FR[k] = _YOM_ATZMAUT_FR

_SEPHARDI_OMER_ES = (
    "Costumbre sefardí (Shulján Aruj O.J. 493:1-2; Peninei Halajá 05-03-03): luto desde Pesaj hasta la mañana del día 34 del Omer (lamed-dalet). "
    "Música en Lag BaOmer en honor de R. Shimón bar Yojai está permitida, pero bodas y cortes de pelo siguen restringidos hasta la mañana del 34 "
    "según el psak sefardí prevalente (Rav Ovadia Yosef, Yechave Daat 3:31). "
    "Algunas comunidades (p. ej. Turquía, Egipto) terminan el luto en Lag BaOmer — sigue a tu kehilá.\n\n"
    "Pregunta a tu rav qué tradición sigues y cuándo empiezan y terminan las restricciones."
)
_SEPHARDI_OMER_FR = (
    "Coutume séfarade (Choulhan Aroukh O.H. 493) : deuil de Pessah jusqu'au matin du 34e jour de l'Omer. "
    "Musique à Lag BaOmer permise ; mariages et coupes jusqu'au 34e matin (Rav Ovadia Yosef). "
    "Certaines kehillot finissent à Lag BaOmer — suis la tienne. Demande à ton rav."
)
k = key_by_prefix["Sephardi custom (Shulchan Arukh O.C. 493"]
ES[k] = _SEPHARDI_OMER_ES
FR[k] = _SEPHARDI_OMER_FR

_YOM_HAZIKARON_LONG_ES = (
    "Yom HaZikaron (4 Iyar) es el día nacional de recuerdo de Israel por los soldados de las FDI y víctimas del terrorismo "
    "que dieron la vida por el Estado. Instituido por la Knéset en 1963; siempre precede a Yom HaAtzmaut.\n\n"
    "No es Yom Tov — melajá permitida; observancia civil nacional.\n\n"
    "Ajuste de fecha: la app ajusta cuando 4 Iyar cae cerca de Shabat para mantener Yom HaZikaron y Yom HaAtzmaut en días consecutivos.\n\n"
    "En Israel: sirenas a las 20:00 (inicio del día) y a las 11:00; ceremonias en cementerios militares; banderas a media asta.\n\n"
    "Oración (sionista religioso / datí leumi): Tachanun completo en Shaharit — día de memoria solemne. "
    "Tachanun omitido solo en Minjá al pasar a Yom HaAtzmaut (Peninei Halajá 5:4:11). "
    "Comunidades que no tratan Yom HaAtzmaut como día religioso dicen Tachanun también en Minjá.\n\n"
    "Mayoría jaredí y jabadí: no como día religioso; oración de días de semana con Tachanun todo el día.\n\n"
    "El día termina al anochecer con el paso a las celebraciones de Yom HaAtzmaut."
)
_YOM_HAZIKARON_LONG_FR = (
    "Yom HaZikaron (4 Iyar) — souvenir national des soldats et victimes du terrorisme. Pas Yom Tov ; melacha permise.\n"
    "Sirènes à 20h et 11h en Israël. Dati leumi : Tahanoun complet à Shaharit ; omis à Min'ha vers Yom HaAtsmaout. "
    "Haredi et 'Habad : jour civil, Tahanoun toute la journée."
)
k = key_by_prefix["Yom HaZikaron (4 Iyar) is Israel's national"]
ES[k] = _YOM_HAZIKARON_LONG_ES
FR[k] = _YOM_HAZIKARON_LONG_FR

_PIKUACH_ES = (
    "Pikuaj nefesh (salvar la vida) anula Shabat y casi todas las mitzvot. Llama a emergencias, conduce al hospital "
    "y lleva medicamentos cuando hay riesgo vital. Médicos y enfermeras tienen halajá detallada para sus turnos — "
    "pero un laico nunca debe dudar en salvar una vida. Tras el peligro, pregunta a tu rav sobre lo que hiciste."
)
_PIKUACH_FR = (
    "Pikuah nefesh (sauver la vie) annule Chabbat et presque toutes les mitsvot. Appelez les secours, conduisez à l'hôpital, "
    "portez des médicaments en cas de danger vital. Après le danger, demande à ton rav."
)
k = key_by_prefix["Pikuach nefesh (saving life) overrides"]
ES[k] = _PIKUACH_ES
FR[k] = _PIKUACH_FR
for _k in strings:
    if _k.startswith("Pikuach nefesh — Pikuach nefesh"):
        ES[_k] = "Pikuaj nefesh — " + _PIKUACH_ES
        FR[_k] = "Pikuah nefesh — " + _PIKUACH_FR

_DAYS_OF_AWE_ES = (
    "Los Días del Temor (Aseret Yemei Teshuvá) abarcan desde Rosh Hashaná hasta Yom Kipur — "
    "tiempo de juicio, oración y teshuvá. Las costumbres se intensifican: selijot, tzedaqá y pedir perdón. "
    "Incluso judíos menos observantes durante el año suelen asistir a los servicios. "
    "El tono es solemne pero esperanzador: el arrepentimiento puede cambiar un decreto severo."
)
_DAYS_OF_AWE_FR = (
    "Les Jours de Crainte (Aseret Yemei Teshouva) — de Rosh Hashana à Yom Kippour : jugement, prière et teshouva. "
    "Selihot, tsedaka, demander pardon. Ton solennel mais plein d'espoir."
)
k = key_by_prefix["The Days of Awe (Aseret Yemei Teshuvah) span"]
ES[k] = _DAYS_OF_AWE_ES
FR[k] = _DAYS_OF_AWE_FR
for _k in strings:
    if _k.startswith("Days of Awe — The Days of Awe") or (
        _k.startswith("The Days of Awe (Aseret") and _k != k
    ):
        ES[_k] = _DAYS_OF_AWE_ES if not _k.startswith("Days of Awe —") else "Días del Temor — " + _DAYS_OF_AWE_ES
        FR[_k] = _DAYS_OF_AWE_FR if not _k.startswith("Days of Awe —") else "Jours de Crainte — " + _DAYS_OF_AWE_FR

_AVEILUT_ES = (
    "Aveilut es la práctica de luto tras la pérdida de un pariente cercano. "
    "Las costumbres incluyen shiva (siete días), shloshim (treinta días) y, para los padres, once meses. "
    "Durante el Omer, las costumbres de aveilut se solapan con el luto nacional por los estudiantes de Rabí Akiva. "
    "Durante las tres semanas del 17 Tamuz al 9 Av, lamentamos la destrucción del Templo."
)
_AVEILUT_FR = (
    "Aveilut — deuil après la perte d'un proche : shiva (7 jours), shloshim (30 jours), onze mois pour les parents. "
    "Pendant l'Omer, chevauchement avec le deuil des élèves de Rabbi Akiva. Trois semaines du 17 Tamouz au 9 Av."
)
k = key_by_prefix["Aveilut is mourning practice"]
ES[k] = _AVEILUT_ES
FR[k] = _AVEILUT_FR
for _k in strings:
    if _k.startswith("aveilut — Aveilut is mourning"):
        ES[_k] = "aveilut — " + _AVEILUT_ES
        FR[_k] = "aveilut — " + _AVEILUT_FR

_ELUL_ES = (
    "Elul es el mes anterior a Rosh Hashaná — selijot diarias (ashkenazíes desde antes de Rosh Hashaná; sefardíes todo el mes), "
    "shofar tras los servicios y ješbon ha-nefesh. Es la pista de aterrizaje para la teshuvá. "
    "Aumenta la tzedaqá, repara relaciones y aprende una halajá nueva antes de los Días del Temor."
)
_ELUL_FR = (
    "Eloul — mois avant Rosh Hashana : selihot quotidiennes, shofar après les offices, heshbon hanefesh. "
    "Piste vers la teshouva : tsedaka, réparer les liens, une nouvelle halakha avant les Yamim Noraïm."
)
k = key_by_prefix["Elul is the month before Rosh Hashana"]
ES[k] = _ELUL_ES
FR[k] = _ELUL_FR
for _k in strings:
    if _k.startswith("Elul — Elul is the month"):
        ES[_k] = "Elul — " + _ELUL_ES
        FR[_k] = "Elul — " + _ELUL_FR

_OMER_MOURNING_ES = (
    "Durante Sefirat HaOmer guardamos costumbres de luto (aveilut) porque los 24.000 estudiantes de Rabí Akiva murieron en una plaga "
    "entre Pesaj y Shavuot (Talmud, Yevamot 62b). Las muertes cesaron en Lag BaOmer — por eso muchas comunidades alivian entonces; "
    "otras continúan hasta Shavuot o la mañana del día 33.\n\n"
    "Por qué lamentamos: el Omer une la libertad física (Pesaj) con recibir la Torá (Shavuot). "
    "La plaga interrumpió la transmisión de la Torá — moderamos la alegría hasta Matan Torá.\n\n"
    "Costumbres comunes (el calendario varía — pregunta a tu rav):\n"
    "• Sin música en vivo (grabaciones y a cappella — según posek)\n"
    "• Sin bodas\n"
    "• Sin cortes de pelo parte o todo el Omer\n\n"
    "Sigue las fechas de inicio y fin de tu comunidad."
)
_OMER_MOURNING_FR = (
    "Pendant Sefirat HaOmer : deuil (aveilut) pour les 24 000 élèves de Rabbi Akiva (Yevamot 62b). "
    "Lag BaOmer soulage pour beaucoup ; d'autres jusqu'à Chavouot. Pas de musique live, mariages, coupes — selon communauté."
)
k = key_by_prefix["During Sefirat HaOmer we keep customs"]
ES[k] = _OMER_MOURNING_ES
FR[k] = _OMER_MOURNING_FR

_SUKKOT_ES = (
    "Sucot son siete días de morada en la sucá y agitar las Cuatro Especies — alegría tras los Días del Temor. "
    "Recuerda las nubes de gloria en el desierto. La lluvia puede eximir de sentarse en la sucá. "
    "Shemini Atzeret sigue de inmediato — fiesta aparte de intimidad con D-s tras la festividad pública."
)
_SUKKOT_FR = (
    "Souccot : sept jours dans la soucca et les Quatre Espèces — joie après les Yamim Noraïm. "
    "Nuées de gloire dans le désert. La pluie peut dispenser de s'asseoir. Chemini Atseret suit aussitôt."
)
k = key_by_prefix["Sukkot is seven days dwelling"]
ES[k] = _SUKKOT_ES
FR[k] = _SUKKOT_FR
for _k in strings:
    if _k.startswith("Sukkot — Sukkot is seven days"):
        ES[_k] = "Sucot — " + _SUKKOT_ES
        FR[_k] = "Souccot — " + _SUKKOT_FR

_PSALM27_ES = (
    "El salmo 27 (לְדָוִד ה' אוֹרִי וְיִשְׁעִי) se añade a Shajarit durante Elul y Tishrei — "
    "temporada de acercarse a D-s antes y durante los Yamim Noraim.\n\n"
    "Cuándo:\n"
    "• Después del servicio matutino (a menudo tras la Amidá de Shajarit — sigue tu sidur).\n"
    "• Las fechas de inicio y fin varían según el minhag — este ítem sigue tu configuración de nusaj.\n\n"
    "Por qué:\n"
    "• «D-s es mi luz y mi salvación — ¿a quién temeré?» (Tehilim 27:1) — confianza en la protección divina en temporada de juicio.\n"
    "• Costumbre extendida en Elul–Tishrei; adoptada en ashkenaz, sefardí y edot ha-mizraj con calendarios distintos."
)
_PSALM27_FR = (
    "Psaume 27 (לְדָוִד ה' אוֹרִי וְיִשְׁעִי) s'ajoute à Shaharit pendant Eloul et Tichri — "
    "saison de rapprochement avant et pendant les Yamim Noraïm.\n\n"
    "Quand :\n"
    "• Après le service du matin (souvent après l'Amida de Shaharit — suis ton sidour).\n"
    "• Les dates de début et de fin varient selon le minhag — cet élément suit votre réglage de nusah.\n\n"
    "Pourquoi :\n"
    "• « D.ieu est ma lumière et mon salut — de qui aurais-je peur ? » (Tehilim 27:1) — confiance dans la protection divine en saison de jugement.\n"
    "• Coutume répandue en Eloul–Tichri ; adoptée chez ashkénazes, séfarades et edot ha-mizrah avec calendriers différents."
)
k = key_by_prefix["Psalm 27 (לְדָוִד ה' אוֹרִי"]
ES[k] = _PSALM27_ES
FR[k] = _PSALM27_FR

_MIKVEH_ES = (
    "Un mikvé es una piscina ritual kosher de agua natural con tamaño halájico y flujo adecuados. "
    "Se usa para taharat ha-mishpajá, conversión, tevilat keilim y otros cambios de estado. "
    "La inmersión es transición poderosa. Los mikvaot comunitarios son discretos con asistentes capacitados."
)
_MIKVEH_FR = (
    "Mikvé : bassin rituel d'eau naturelle — taharat hamishpacha, conversion, tévila des ustensiles."
)
k = key_by_prefix["A mikveh is a kosher ritual pool"]
ES[k] = _MIKVEH_ES
FR[k] = _MIKVEH_FR

_BEDIEVED_ES = (
    "Bedieved describe orientación halájica tras perder el ideal — sin querer o por fuerza mayor. "
    "No es permiso para planificar mal; es cómo recuperarse. "
    "Ejemplos: repetición de Amidá, jametz en Pesaj, omisión del Omer. Tu rav aplica bedieved a tu caso."
)
_BEDIEVED_FR = (
    "Bedieved décrit l'orientation halakhique quand l'idéal a été manqué — sans intention ou par force majeure. "
    "Ce n'est pas une permission pour mal planifier ; c'est comment te remettre sur les rails. "
    "Exemples : répétition de l'Amida, hamets trouvé à Pessa'h, compte du Omer manqué. "
    "Ton rav applique les règles bedieved à ton cas ; les articles de l'app résument les schémas courants."
)
k = key_by_prefix["Bedieved describes halachic guidance"]
ES[k] = _BEDIEVED_ES
FR[k] = _BEDIEVED_FR
k = key_by_prefix["bedieved — Bedieved describes"]
ES[k] = f"bedieved — {_BEDIEVED_ES}"
FR[k] = f"bedieved — {_BEDIEVED_FR}"

_PLAG_ES = (
    "Plag ha-minjá es una hora y cuarto halájica antes del anochecer — "
    "para Minjá temprana, entrada temprana a Shabat en algunas comunidades, y ciertos tiempos de Pesaj y Janucá. "
    "También es lo más temprano para encender velas de Shabat; antes de plag invalida la mitzvá. "
    "No es igual al atardecer — consulta tu calendario."
)
_PLAG_FR = (
    "Plag ha-min'ha : 1¼ heure halakhique avant la nuit — bougies Chabbat au plus tôt ; vérifier le calendrier."
)
k = key_by_prefix["Plag hamincha is one and a quarter"]
ES[k] = _PLAG_ES
FR[k] = _PLAG_FR
k = key_by_prefix["Plag HaMincha — Plag hamincha is"]
ES[k] = "Plag HaMincha — " + _PLAG_ES
FR[k] = "Plag HaMincha — " + _PLAG_FR

_RAMBAM_ES = (
    "Rambam — Rabí Mosés Maimónides (1138–1204): médico, filósofo y gigante halájico. "
    "Su Mishné Torá codifica la halajá en hebreo claro; sus Trece Principios resumen la fe judía. "
    "Muchos lo conocen por resúmenes diarios de halajá o sus leyes de teshuvá, tzedaá y estudio de Torá."
)
_RAMBAM_FR = (
    "Rambam — Rabbin Moïse Maïmonide (1138–1204) : médecin, philosophe et géant halakhique. "
    "Sa Méchné Torah codifie la halakha en hébreu clair ; ses Treize Principes résument la foi juive. "
    "Beaucoup le découvrent par des résumés quotidiens de halakha ou ses lois de techouva, tsedaka et étude de Torah."
)
k = key_by_prefix["Rambam is Rabbi Moses Maimonides"]
ES[k] = _RAMBAM_ES
FR[k] = _RAMBAM_FR
k = key_by_prefix["Rambam — Rambam is Rabbi Moses"]
ES[k] = "Rambam — " + _RAMBAM_ES
FR[k] = "Rambam — " + _RAMBAM_FR

_LECHEM_MISHNEH_ES = (
    "Lechem mishneh son dos panes enteros en las comidas de Shabat y Yom Tov — recuerdan la doble porción de maná "
    "antes del Shabat en el desierto. Cubre los panes, bendice hamotzi y parte para los demás. En Pesaj, dos matzot "
    "enteras cumplen el papel. Los panes deben estar enteros y ser aptos para hamotzi."
)
_LECHEM_MISHNEH_FR = (
    "Le lechem mishneh, ce sont deux pains entiers aux repas de Chabbat et Yom Tov — en souvenir de la double "
    "portion de manne avant Chabbat dans le désert. Couvre les pains, bénis hamotzi et coupe pour les autres. "
    "À Pessa'h, deux matzot entières remplissent ce rôle. Les pains doivent être entiers et dignes de hamotzi."
)
k = key_by_prefix["lechem mishneh — Lechem mishneh is"]
ES[k] = f"lechem mishneh — {_LECHEM_MISHNEH_ES}"
FR[k] = f"lechem mishneh — {_LECHEM_MISHNEH_FR}"
if "lechem mishneh" in strings:
    ES["lechem mishneh"] = "lechem mishneh"
    FR["lechem mishneh"] = "lechem mishneh"

k = key_by_prefix["The Three Weeks (בין המצרים) from 17 Tammuz"]
ES[k] = THREE_WEEKS_INTRO_ES
FR[k] = THREE_WEEKS_INTRO_FR

_SHLOSHIM_ES = (
    "Shloshim son treinta días de celebración social reducida tras el entierro (para muchos grados de parentesco). "
    "Los cortes de pelo y la música pueden estar restringidos. Tras el shloshim, la vida normal vuelve, excepto para "
    "los padres — el Kaddish continúa once meses. Si el calendario se solapa con el Omer o las Tres Semanas, hay "
    "restricciones adicionales — pregunta a tu rav."
)
_SHLOSHIM_FR = (
    "Shloshim, c'est trente jours de fêtes sociales réduites après l'enterrement (pour de nombreux liens de parenté). "
    "Les coupes de cheveux et la musique peuvent être limitées. Après le shloshim, la vie normale reprend, sauf pour "
    "les parents — le Kaddish continue onze mois. Un chevauchement avec le Omer ou les Trois Semaines ajoute des "
    "restrictions — demande à ton rav."
)
k = key_by_prefix["Shloshim is thirty days of reduced"]
ES[k] = _SHLOSHIM_ES
FR[k] = _SHLOSHIM_FR
k = key_by_prefix["shloshim — Shloshim is thirty days"]
ES[k] = f"shloshim — {_SHLOSHIM_ES}"
FR[k] = f"shloshim — {_SHLOSHIM_FR}"

# --- FR/ES prose disaster fixes (garbled machine output in bundle) ---
def _k(pfx: str) -> str | None:
    for s in strings:
        if s.startswith(pfx):
            return s
    return None

k = _k("Appreciate life's flavors")
if k:
    ES[k] = (
        "¡Aprecia los sabores de la vida! La próxima vez que comas algo delicioso, "
        "haz una pausa para maravillarte de la variedad de gustos que D-s creó 😋. "
        "Dulce, salado, ácido, picante — cada sabor es un regalo. "
        "Imagina si todo supiera igual... En cambio, disfrutamos de un mundo maravilloso de sabores."
    )
    FR[k] = (
        "Apprécie les saveurs de la vie ! La prochaine fois que tu mangeras quelque chose de délicieux, "
        "fais une pause pour t'émerveiller de la variété des goûts que D. a créée 😋. "
        "Sucré, salé, acide, épicé — chaque saveur est un cadeau unique ! "
        "Imagine si tout avait le même goût... Au lieu de cela, tu profites d'un monde merveilleux de saveurs."
    )

_NESHAMA_FR = (
    "Neshama yeteira (« âme supplémentaire ») : tradition selon laquelle Chabbat apporte une mesure "
    "supplémentaire de capacité spirituelle — plus de patience, de joie et de lien à la Torah. "
    "Elle part à Havdalah ; c'est pourquoi certains ressentent une baisse après Chabbat. "
    "Honorer les repas, le repos et l'étude de Chabbat aide cette âme à s'exprimer ; "
    "la précipitation et le stress la diminuent."
)
for _pfx in ("Neshama yeteira (\"extra soul\")", "neshama yeteira — Neshama yeteira"):
    k = _k(_pfx)
    if k:
        FR[k] = _NESHAMA_FR if _pfx.startswith("Neshama yeteira (\"") else (
            "neshama yeteira — " + _NESHAMA_FR
        )

k = _k("Study the incredible Showbread table")
if k:
    ES[k] = (
        "¡Estudia la increíble mesa de los Panes de la Proposición (Shulján)! 🍞 "
        "Tómate un momento para conocer esta mesa de oro con doce panes especiales. "
        "El milagro: el pan permaneció fresco y caliente toda la semana. "
        "En las fiestas, los kohanim levantaban la mesa y decían: "
        "«¡Mirad cuánto sois amados ante D-s!» — prueba del amor constante de D-s. ❤️"
    )
    FR[k] = (
        "Étudie l'incroyable table des pains de proposition (Shulhan) ! 🍞 "
        "Prends un moment pour découvrir cette table d'or qui contenait douze pains spéciaux. "
        "Le miracle : le pain resta frais et chaud toute une semaine jusqu'à son remplacement. "
        "Aux fêtes, les kohanim soulevaient la table et disaient : "
        "« Regarde combien tu es aimé devant D. ! » — preuve de l'amour constant de D. ! ❤️"
    )

k = _k("e.g., Call your grandmother")
if k:
    ES[k] = "p. ej., llama a tu abuela para preguntar cómo está..."
    FR[k] = "p. ex., appelle ta grand-mère pour prendre de ses nouvelles..."

k = _k('The Torah commands: "Honor your father')
if k:
    FR[k] = (
        "La Torah ordonne : « Honore ton père et ta mère » (Shemot 20:12) — parmi les Dix Commandements, "
        "d'un poids égal à l'honneur dû à D. (Kiddouchin 30b). Kibbud av va'em (כִּבּוּד אָב וָאֵם) est une mitsva à vie "
        "pour hommes et femmes.\n\n"
        "Deux dimensions :\n"
        "• Kibbud (כִּבּוּד — honneur) : soins actifs — nourriture, vêtements, accompagnement, parole respectueuse "
        "et soutien financier si besoin (Choulhan Aroukh Y.D. 240 ; Rambam, Mamrim 6).\n"
        "• Morah (מוֹרָא — crainte) : ne pas s'asseoir à leur place, ne pas les contredire violemment "
        "ni les appeler par le prénom — préserver leur dignité (Y.D. 240:2–3).\n\n"
        "Pratique quotidienne tant qu'ils vivent :\n"
        "• Parle avec respect — ton et mots comptent autant que les actes.\n"
        "• Aide dans leurs besoins : repas, courses, rendez-vous médicaux, tâches ménagères, surtout en vieillissant.\n"
        "• Lève-toi à leur entrée — la fréquence varie selon le minhag.\n"
        "• Partage les bonnes nouvelles ; épargne-leur souci ou embarras inutiles.\n"
        "• Si un parent demande de transgresser la Torah, refuse avec douceur — l'obéissance à Hachem d'abord (Y.D. 240:15).\n\n"
        "Limites :\n"
        "Tu n'es pas tenu de t'appauvrir ni de nuire à ton foyer (Y.D. 240:5). Un parent abusif peut nécessiter "
        "un guide halakhique — la mitsva n'est pas se soumettre au mal.\n\n"
        "Quand ils ne sont plus :\n"
        "L'honneur continue — Kaddish, étude de Torah et bonnes actions l'ilui nishmatam, tsedakah en leur mémoire, "
        "visite au cimetière à yahrzeit et avant les Yamim Noraïm, porter leurs valeurs et leur bon nom.\n\n"
        "Beaux-parents :\n"
        "Honorer les parents du conjoint est une grande mitsva et favorise le shalom bayit, mais l'obligation toranique "
        "porte sur ton propre père et ta mère (Y.D. 240:24).\n\n"
        "Intention du jour : un acte concret de kibbud ou morah — un appel, un mot gentil, de l'aide ou de l'étude "
        "en mémoire d'un parent."
    )

k = _k("The rabbis instituted a practice of saying 100 blessings")
if k:
    FR[k] = (
        "Les Sages ont institué de réciter cent bénédictions (berakhot) chaque jour — "
        "pour rester conscient des dons de D.\n\n"
        "Qui est concerné :\n"
        "La pratique talmudique vise d'abord les hommes (Menahot 43b). Beaucoup de poskim estiment que les femmes "
        "devraient aussi viser cent berakhot ; d'autres le voient comme facultatif — demande à ton rav.\n\n"
        "Qu'est-ce qu'une berakha :\n"
        "Formule courte avant ou après une mitsva ou un plaisir du monde. "
        "Elle commence : « Baroukh Atah Ado-naï Eloheinou Melekh ha'olam... »\n\n"
        "D'où viennent les cent en semaine :\n"
        "• Birchot HaShahar : ~15\n• Shaharit : ~40\n• Min'ha : ~20\n• Maariv : ~18\n"
        "• Avant et après les repas : ~10\n"
        "Total : facilement cent un jour ordinaire.\n\n"
        "Chabbat et Yom Tov :\n"
        "Les offices sont plus courts — tu seras en dessous de cent. Pour compléter :\n"
        "• Collations supplémentaires avec berakhot\n"
        "• Épices ou fruits parfumés (besamim)\n\n"
        "Ein Kelokeinu (minhag ashkénaze à Chabbat et fêtes) :\n"
        "Récité à la fin de Shaharit — certains rabbins comptent ses louanges vers le quota quand il manque des berakhot "
        "complètes. La Michna Beroura préfère compléter avec de vraies berakhot quand c'est possible.\n\n"
        "Tzaddik (צדיק) — cadre mnémotechnique :\n"
        "• צ (90) : répondre Amen\n• ד (4) : les quatre Kedushot\n"
        "• י (10) : dix « Yehei Shemei Rabbah » au Kaddish\n"
        "• ק (100) : cent berakhot"
    )

k = _k("Clean your bathroom extra spick and span")
if k:
    ES[k] = (
        "¡Limpia el baño a fondo en honor de Shabat! — Enviado por A.S. Nuestros Sabios enseñan que preparar Shabat "
        "es en sí misma una mitzvá — y quien se esfuerza el viernes disfruta las recompensas en Shabat. "
        "Un hermoso midrash describe dos ángeles que acompañan a la persona desde la sinagoga el viernes por la noche: "
        "uno bueno, uno malo. Si encuentran el hogar listo — velas encendidas, mesa puesta — el ángel bueno bendice "
        "que el próximo Shabat sea igual y el malo se ve obligado a decir «amén». Honrar Shabat es hacer que toda "
        "la casa se sienta como un palacio para el Rey."
    )
    FR[k] = (
        "Nettoie la salle de bain à fond en l'honneur de Chabbat ! — Proposé par A.S. Nos Sages enseignent que "
        "préparer Chabbat est en soi une mitsva — et que celui qui peine le vendredi en profite le Chabbat. "
        "Un beau midrash : deux anges accompagnent la personne du synagogue le vendredi soir — un bon, un mauvais. "
        "S'ils trouvent la maison prête — bougies allumées, table dressée — le bon ange bénit le prochain Chabbat "
        "pareil et le mauvais est forcé de dire « amen ». Honorer Chabbat, c'est faire de toute la maison un palais pour le Roi."
    )

_CHUPPAH_FR = (
    "La houppa est le dais de mariage qui représente le nouveau foyer du couple — ouvert de tous côtés "
    "comme la tente d'Abraham. La cérémonie comprend kiddoushin (fiançailles) et nissouine sous la houppa. "
    "Briser le verre rappelle la destruction du Temple. C'est une seoudat mitsva avec danse et sim'ha."
)
k = _k("The chuppah is the wedding")
if k:
    FR[k] = _CHUPPAH_FR
k = _k("chuppah — The chuppah")
if k:
    FR[k] = f"chuppah — {_CHUPPAH_FR}"
if "chuppah" in strings:
    FR["chuppah"] = "houppa"

k = _k("The shofar is a ram's horn")
if k:
    FR[k] = (
        "Le shofar est une corne de bélier (ou autre corne casher) soufflée à Roch Hachana comme appel au techouva. "
        "Les séquences tekia, chevarim et teroua suivent le minhag. On ne le souffle pas à Chabbat. "
        "Entendre le shofar à la synagogue avec kavana accomplit la mitsva pour la plupart des hommes ; "
        "l'obligation des femmes suit le psak communautaire. Les horaires de répétition sont affichés avant la fête."
    )

_NESHAMA_GLOSS_FR = (
    "Neshama est l'âme — le souffle divin qui te fait vivre. Modeh Ani remercie D. de la rendre après le sommeil. "
    "Yizkor et Kaddish concernent le voyage de l'âme après la mort. La pensée juive distingue nefesh, roua'h et neshama "
    "dans le mysticisme ; en pratique, nourrir l'âme, c'est Torah, mitsvot et caractère."
)
for _pfx in ("Neshama is the soul", "neshama — Neshama is"):
    k = _k(_pfx)
    if k:
        FR[k] = _NESHAMA_GLOSS_FR if _pfx.startswith("Neshama is") else f"neshama — {_NESHAMA_GLOSS_FR}"

k = _k("The very first act of each day")
if k:
    FR[k] = (
        "Le premier acte de chaque jour :\n\n"
        "Qu'est-ce que c'est :\n"
        "Modeh Ani est une prière de deux lignes dite au moment où tu ouvres les yeux — "
        "l'expression la plus simple de la gratitude juive, remercier D. de rendre ton âme après le sommeil.\n\n"
        "La tradition :\n"
        "Le sommeil est une expérience nocturne proche de la mort — l'âme quitte partiellement le corps "
        "chaque nuit et revient le matin. Modeh Ani reconnaît que D. t'a donné un jour de plus.\n\n"
        "Texte hébreu :\n"
        "« Modeh ani lifanecha, Melech chai v'kayam, shehechezarta bi nishmati b'chemla — raba emunatecha. »\n"
        "« Je Te remercie, Roi vivant et éternel, de m'avoir rendu mon âme avec compassion — quelle est Ta fidélité ! »\n\n"
        "Comment faire :\n"
        "Dis-la encore au lit, avant de te lever, dès le réveil du sommeil nocturne principal — avant toute autre chose. "
        "Modeh Ani est l'une des rares parties du davening que tu peux dire sans te laver les mains d'abord — "
        "puis passe à Netilat Yadayim (negel vasser ; voir l'item suivant).\n\n"
        "Sommeil nocturne vs sieste :\n"
        "Tu peux répéter ces mots comme gratitude personnelle, mais ce n'est pas la prière officielle du matin. "
        "Après une sieste de jour, on ne la récite généralement pas — lave simplement les mains selon les règles du point suivant."
    )

k = _k("The Torah commands us to write G-d's words")
if k:
    FR[k] = (
        "La Torah nous ordonne d'inscrire les paroles de D. « sur les poteaux de ta maison et de tes portes » "
        "(Deutéronome 6:9) — accomplie en fixant une mezouza à chaque montant de porte.\n\n"
        "Qu'est-ce qu'une mezouza :\n"
        "D'abord le klaf — petit parchemin où un sofer a écrit à la main Deutéronome 6:4-9 et 11:13-21. "
        "Au dos : « Shaddaï » — acronyme de Shomer Daltot Yisrael.\n"
        "• La mitsva est le klaf casher fixé au montant. Un boîtier décoratif est facultatif.\n\n"
        "Protection et sens :\n"
        "La tradition enseigne que les mezouzot protègent la maison. À chaque passage, tu te rappelles le Shema "
        "et les mitsvot. Nos Sages lient une observance soigneuse au bien-être du foyer.\n\n"
        "Où la placer :\n"
        "• Chaque porte régulièrement utilisée — entrée, chambres, salon, cuisine, bureau.\n"
        "• Pas requis : salles de bain, petits placards, débarras.\n"
        "• Montant droit en entrant, tiers supérieur, légèrement incliné vers l'intérieur.\n\n"
        "Très important :\n"
        "Le klaf doit être casher. Achète ton klaf dans une boutique fiable et fais-le vérifier par un sofer tous les quelques ans.\n\n"
        "Bénédiction à la pose :\n"
        "« Baroukh Atah Ado-naï Eloheinou Melekh ha'olam, asher kid'shanou b'mitsvotav v'tsivanou lik'boa mezouza. »"
    )

# --- Netilat Yadayim (5 nusach variants; bundle had 323-char stubs) ---
_NETILAT_HEAD = (
    "Après Modeh Ani, lave tes mains rituellement (netilat yadayim) avant toute autre chose.\n\n"
    "Qu'est-ce que c'est :\n"
    "Netilat Yadayim est un lavage rituel — pas seulement hygiénique. Pendant le sommeil profond, "
    "l'âme s'élève partiellement et un rouah ra'ah s'attache aux doigts et ongles. "
    "Ce lavage l'enlève et te prépare à la prière, la Torah et la journée.\n\n"
    "Les minhagim divergent sur le moment de la bénédiction Al Netilat Yadayim.\n\n"
    "Comment verser (toutes traditions) :\n"
    "1. Idéalement : coupe d'eau et bassin prêts au chevet\n"
    "2. Verser sur la main droite puis gauche, trois fois en alternant\n"
    "3. Sécher selon ton minhag (voir ci-dessous)\n\n"
)
_NETILAT_TAIL = (
    "\n\nTexte de la bénédiction :\n"
    "« Baroukh Atah Ado-naï Eloheinou Melekh ha'olam, asher kid'shanou b'mitsvotav v'tzivanou al netilat yadayim »\n\n"
    "Règle des 4 amot :\n"
    "Avant de laver, ne marche pas 4 amot (~2 m) sans t'arrêter complètement. Si le lavabo est loin : "
    "approche stricte (Zohar), courante (Michna Beroura) ou souple (Aroukh HaShoulhan). Demande à ton rav.\n\n"
    "Nikuy (sans eau à portée) :\n"
    "Frotte tes mains sur une surface sèche propre — permet de dire des mots saints, "
    "mais ne remplace pas le lavage à l'eau (Choulhan Aroukh O.H. 4:22).\n\n"
    "Après une sieste (pas de Modeh Ani) :\n"
    "• Sieste courte (<30 min) : propreté avant la prière, pas de bénédiction\n"
    "• Longue sieste : avis divergents — demande à ton rav\n\n"
    "Notes importantes :\n"
    "• Ne touche pas yeux, bouche ou visage avant de laver\n"
    "• Ne touche pas la nourriture avant de laver\n"
    "• Même triple lavage sans bénédiction après toilettes, cimetière, avant le pain\n"
    "• Une fois la bénédiction matinale dite, on ne la répète pas à la synagogue"
)
_NETILAT_MINHAG = {
    "Ashkenaz minhag — when": (
        "Minhag ashkénaze — quand dire Al Netilat Yadayim :\n"
        "• Au réveil : lavage sans bénédiction (souvent au chevet)\n"
        "• Bénédiction après le deuxième lavage (toilettes, habillage), avec Asher Yatsar\n"
        "• Si pas besoin des toilettes au réveil : bénédiction dès le premier lavage"
    ),
    "Chabad minhag — when": (
        "Minhag 'Habad — quand dire Al Netilat Yadayim :\n"
        "• Premier lavage au réveil, avant de toucher le sol — trois versées, sans bénédiction\n"
        "• Puis : toilettes, rinçage de bouche, toilette personnelle\n"
        "• Deuxième lavage au lavabo : Al Netilat Yadayim, Asher Yatsar et Elokaï Neshama"
    ),
    "Sefard minhag — when": (
        "Minhag séfarade — quand dire Al Netilat Yadayim :\n"
        "• Lavage pour enlever le rouah ra'ah du sommeil (Choulhan Aroukh, Racha)\n"
        "• Bénédiction juste après le premier lavage du matin, idéalement tout de suite\n"
        "• Besoin urgent des toilettes : d'abord les toilettes, puis lavage et bénédiction"
    ),
    "Blessing timing overview": (
        "Aperçu du minhag (règle le nusach dans les réglages pour le texte complet) :\n"
        "• Ashkenaz : en général pas de bénédiction au chevet ; bénédiction après le deuxième lavage\n"
        "• Sefarade / beaucoup d'Edot HaMizra'h : bénédiction après le lavage, avant de sécher\n"
        "• Certaines kehillot yéménites (Baladi) : lavage après les toilettes — suis ton rav\n"
        "• 'Habad : pas de bénédiction au premier lavage ; bénédiction au deuxième lavage au lavabo"
    ),
    "Edot HaMizrach minhag (many kehillot)": (
        "Minhag Edot HaMizra'h (beaucoup de kehillot) — quand dire Al Netilat Yadayim :\n"
        "• Lavage pour enlever le rouah ra'ah du sommeil (Choulhan Aroukh, Racha)\n"
        "• Bénédiction juste après le premier lavage du matin, idéalement tout de suite\n"
        "• Besoin urgent des toilettes : d'abord les toilettes, puis lavage et bénédiction\n\n"
        "Baladi yéménite et certaines kehillot lavent après les toilettes avant la bénédiction — suis le psak de ta communauté."
    ),
}
for _nk in strings:
    if not _nk.startswith("Immediately after saying Modeh Ani"):
        continue
    for _marker, _section in _NETILAT_MINHAG.items():
        if _marker in _nk:
            FR[_nk] = _NETILAT_HEAD + _section + _NETILAT_TAIL
            break

_CHAZZAN_FR = (
    "Le hazan (chantre) guide la congrégation dans la prière — surtout quand l'office est chanté "
    "ou quand un minyan a besoin d'une voix pour la répétition de l'Amida. "
    "C'est un cheli'ah tsibour (émissaire de la communauté), pas un artiste. "
    "Beaucoup d'offices en semaine n'ont pas de hazan professionnel ; un membre conduit."
)
for _pfx in ("The chazzan (cantor)", "chazzan — The chazzan"):
    k = _k(_pfx)
    if k:
        FR[k] = _CHAZZAN_FR if _pfx.startswith("The chazzan") else f"chazzan — {_CHAZZAN_FR}"

if "Modeh Ani" in strings:
    FR["Modeh Ani"] = "Modeh Ani"
    ES["Modeh Ani"] = "Modeh Ani"
k = _k("Modeh Ani — Modeh Ani is the first words")
if k:
    FR[k] = (
        "Modeh Ani — premiers mots au réveil du sommeil nocturne : remercier le Roi vivant "
        "de rendre l'âme avec compassion. Dis-le au lit avant de te lever, sans te laver d'abord. "
        "Pas après une sieste de jour — alors lave les mains (negel vasser). "
        "Tu peux répéter les mots comme gratitude personnelle, mais ce n'est pas la prière officielle du matin. "
        "Hommes : Modeh ; femmes : Modah."
    )
    ES[k] = (
        "Modeh Ani — primeras palabras al despertar del sueño nocturno: agradecer al Rey viviente "
        "por devolver el alma con compasión. Dilo en la cama antes de levantarte, sin lavarte antes. "
        "No tras siesta diurna — entonces lávate las manos (negel vasser). "
        "Puedes repetir las palabras como gratitud personal, pero no es la oración oficial del mañana. "
        "Hombres: Modeh; mujeres: Modá."
    )

k = _k("Guard your speech about G-d")
if k:
    FR[k] = (
        "Garde ton discours sur D. ! 🗣️ Le Rambam enseigne dans Hilchot Avodat Kokhavim "
        "d'être extrêmement prudent — ne jamais parler avec irrespect de D. ni nier Son existence. "
        "Même les serments légers ou bénédictions inutiles sont interdits. "
        "Chaque mot sur D. doit être avec révérence. Défi du jour : parle des choses saintes avec soin et respect."
    )

k = _k("This app is brought to you by")
if k:
    FR[k] = "Cette application t'est proposée par"

k = _k("This app is a learning companion, not a rabbi")
if k:
    FR[k] = (
        "Cette application est un compagnon d'apprentissage, pas un rabbin — pas de décisions halakhiques.\n\n"
        "La liste couvre les mitsvot quotidiennes courantes, pas toute la Torah.\n\n"
        "Avec ta permission : GPS ou ville choisie pour les zmanim — données sur l'appareil seulement.\n\n"
        "Nouveau dans le judaïsme ? Va doucement ; demande à un rabbin orthodoxe de confiance."
    )

k = _k('The word mitzvah (מִצְוָה) literally means "commandment."')
if k:
    FR[k] = (
        "Mitsva (מִצְוָה) : commandement — les 613 de la Torah et d'autres d'origine rabbinique.\n\n"
        "Aussi « connexion » : accomplir une mitsva, c'est te lier au Divin.\n\n"
        "Fais quelques mitsvot et vois comment tu te sens — c'est le début du « Mitz Mode »."
    )

k = _k("Find your 'golden middle path'")
if k:
    FR[k] = (
        "Trouve ton « juste milieu doré » ! 🎯 Le Rambam enseigne l'équilibre dans la plupart des traits de caractère — "
        "ni trop extrême, ni trop passif. Choisis un domaine où tu as des difficultés et cherche le milieu aujourd'hui."
    )

k = _k("Guard your heart against hatred")
if k:
    FR[k] = (
        "Protège ton cœur contre la haine ! ❤️ La Torah nous enseigne : même si quelqu'un t'a fait du tort, "
        "ne garde pas la haine dans ton cœur. Parle-lui plutôt de ce qui t'a blessé — la réconciliation vaut mieux que la rancune."
    )

k = _k("Guard your home from forbidden things")
if k:
    FR[k] = (
        "Protège ton foyer des images interdites ! 🎨 Le Rambam enseigne dans Hilchot Avodat Kokhavim "
        "de faire attention à certains types d'images et statues. Demande à ton rav ce qui convient chez toi."
    )

k = _k("Let go of grudges")
if k:
    FR[k] = (
        "Lâche la rancune ! 🕊️ Même sans te venger, ne garde pas la blessure. Exemple torahique : "
        "si quelqu'un emprunte, ne dis pas « Comme toi qui m'as refusé » — prête de bon cœur."
    )

k = _k("Practice letting go of revenge")
if k:
    FR[k] = (
        "Entraîne-toi à lâcher la vengeance ! Tu sens l'envie de te venger ? "
        "La Torah enseigne que la vengeance appartient à D. — choisis aujourd'hui un acte de lâcher-prise."
    )

k = _k("Show extra care to those who need it most")
if k:
    FR[k] = (
        "Soin particulier pour veuves et orphelins 🤗 — mitsva de douceur. "
        "Le Talmud : D. est leur gardien ; en les aidant tu t'associes à Lui."
    )

k = _k("Still say Shema with its blessings during Shacharit")
if k:
    FR[k] = (
        "Récite quand même le Chema avec ses bénédictions au Shaharit jusqu'à midi — "
        "même si tu as manqué le moment, dis-le pendant la prière."
    )

k = _k("The time to fulfill the mitzvah of Shema has passed ({time}). Still say Shema")
if k:
    FR[k] = (
        "Le délai de la mitsva du Shema est passé ({time}). Récite encore le Shema avec ses bénédictions "
        "au Shaharit jusqu'à midi — même si tu l'as manqué, dis-le pendant la prière."
    )

for _nk in strings:
    for _prefix, _langs in NINE_DAYS_ES_FR_BY_PREFIX.items():
        if _nk.startswith(_prefix):
            if "es" in _langs:
                ES[_nk] = _langs["es"]
            if "fr" in _langs:
                FR[_nk] = _langs["fr"]
            break

k = _k("Make an appointment for a private audience with the King of Kings")
if k:
    FR[k] = (
        "Prends rendez-vous pour une audience privée avec le Roi des rois ! 👑 "
        "Le Shemoneh Esrei (Amida) est le cœur de chaque office — composé avec la Rouah HaKodesh, "
        "chaque mot est une clé qui ouvre les portes célestes 🔓. "
        "Protocole royal : debout, pieds joints (comme les anges), le cœur tourné vers Jérusalem 🏛️. "
        "Trois sections — louange, treize demandes, gratitude. "
        "Chabbat et fêtes : lis la bonne Amida dans ton siddour 📜. "
        "Kavanah cruciale dès « Avot » — les ashkénazes ne répètent pas, les séfarades oui 🕊️. "
        "Plonge dans une autre dimension et fais descendre les bénédictions que D. veut t'accorder ! "
        "En savoir plus 🎊🙏"
    )

for _k, _v in YAALEH_TEMPLATE_ES.items():
    ES[_k] = _v
for _k, _v in YAALEH_TEMPLATE_FR.items():
    FR[_k] = _v

for _k in strings:
    for _prefix, _fr_tu in PREFIX_FR_TU_EXPLAINERS.items():
        if _k.startswith(_prefix):
            FR[_k] = _fr_tu
            break

for _gk, (_ges, _gfr) in GLOSSARY_SHORT_ES_FR.items():
    ES[_gk] = _ges
    FR[_gk] = _gfr
for _k in strings:
    for _prefix, (_pes, _pfr) in GLOSSARY_SHORT_PREFIX_ES_FR.items():
        if _k.startswith(_prefix):
            ES[_k] = _pes
            FR[_k] = _pfr
            break

ES.update(BATCH21_ES)
FR.update(BATCH21_FR)
ES.update(BATCH22_ES)
FR.update(BATCH22_FR)
ES.update(BATCH33_ES)
FR.update(BATCH33_FR)
ES.update(BATCH34_ES)
FR.update(BATCH34_FR)
ES.update(BATCH35_ES)
FR.update(BATCH35_FR)
ES.update(BATCH36_ES)
FR.update(BATCH36_FR)
ES.update(BATCH37_ES)
FR.update(BATCH37_FR)
ES.update(BATCH38_ES)
FR.update(BATCH38_FR)
ES.update(BATCH39_ES)
FR.update(BATCH39_FR)
ES.update(BATCH40_ES)
FR.update(BATCH40_FR)
ES.update(BATCH41_ES)
FR.update(BATCH41_FR)
ES.update(BATCH42_ES)
FR.update(BATCH42_FR)
ES.update(BATCH43_ES)
FR.update(BATCH43_FR)
ES.update(BATCH44_ES)
FR.update(BATCH44_FR)
ES.update(BATCH45_ES)
FR.update(BATCH45_FR)

for _k in strings:
    for _prefix, _fr_tu in BATCH33_FR_PREFIX.items():
        if _k.startswith(_prefix):
            FR[_k] = _fr_tu
            break

shard = {"es": ES, "fr": FR}
OUT.write_text(json.dumps(shard, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
print(f"Wrote {len(ES)} es + {len(FR)} fr entries to {OUT.name}")
