#!/usr/bin/env python3
"""Build human/es_fr_prose_polish.json — targeted ES/FR fixes for worst long-form strings."""
from __future__ import annotations

import json
from pathlib import Path

from _melacha_prose_data import MELACHA_POLISH, MISC_PROSE_POLISH

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
    "Si vous devez partir très tôt au travail :\n"
    "• Idéal : après misheyakir ; Amida au lever du jour.\n"
    "• Sinon : Igrot Moshe (O.H. 4:6) permet un cas particulier avant misheyakir — demandez à votre rav.\n\n"
    "L'application débloque cet élément à misheyakir selon vos zmanim locaux.\n\n"
    "Quand on ne les porte pas :\n"
    "• Chabbat et Yom Tov\n"
    "• Hol hamoed de Pessah et Souccot — selon le minhag ; demandez à votre rav.\n\n"
    "Pour commencer :\n"
    "Des téfilines casher sont un investissement important. N'achetez pas bon marché — beaucoup ne sont pas valides. "
    "Demandez à votre rav de vous aider à en acquérir et à apprendre à les poser."
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
    "La paracha hebdomadaire n'est pas qu'un repère du calendrier. C'est votre part personnelle dans la Torah qui soutient le peuple juif. "
    "Les sages lient la lecture publique de la Torah à la survie du monde (Chabbat 119b). "
    "Quand vous étudiez la même paracha que la communauté entendra le Chabbat, vous rejoignez une chaîne qui remonte au Sinaï — "
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
    "Répartissez la paracha sur la semaine. Manquer la fenêtre idéale ne fait pas perdre la mitsva tant que les délais de repli restent valides."
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
    "Demandez à votre rav selon l'âge, le nusach et le minhag.\n\n"
    "Cochez cet élément quand vous avez investi aujourd'hui dans le 'hinoukh de vos enfants — enseigner, montrer l'exemple ou guider une mitsva au niveau attendu par la halakha."
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
    "Les soferim vérifient aussi les klafim existants. N'achetez jamais de téfilines ou mezouzot sans certification fiable."
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
FR[k] = (
    "Le bar mitsvah, c'est quand un garçon atteint treize ans et devient obligé par les mitsvot — téfilines, jeûne, compte de l'Omer et toute la loi morale. "
    "La fête est une joie de seudat mitsva, mais l'essence est la responsabilité. Le 'hinoukh des parents avant treize ans le prépare. "
    "L'alyah à la Torah marque souvent ce jour à la synagogue."
)
k = key_by_prefix["bar mitzvah — Bar mitzvah is when a boy"]
FR[k] = (
    "bar mitsvah — Quand un garçon atteint treize ans et devient obligé par les mitsvot : téfilines, jeûne, Omer et loi morale. "
    "Fête de seudat mitsva ; essence : responsabilité. Le 'hinoukh parental le prépare. Alyah à la Torah souvent ce jour-là."
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
    "'hiyouv — 'hiyouv signifie « obligé » : la mitsva vous concerne et vous devez l'accomplir. Le bar mitsvah crée 'hiyouv pour les mitsvot du garçon ; maladie ou danger peuvent suspendre temporairement l'obligation. En cas de doute chayav ou patour — demandez à votre rav."
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
    "kedei ajilat pras — unos 4–7 minutos según muchos poskim para contar como una sola comida; "
    "matzá, maror y berajá aharona."
)
FR[k] = (
    "kedei akhilat pras — environ 4–7 minutes selon beaucoup de poskim pour un seul acte de manger ; "
    "matsa, maror et bénédiction finale."
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
    "Maîtrisez l'art de la réprimande aimante ! 💝 La Torah nous donne un outil : si vous voyez quelqu'un se tromper, "
    "ne restez pas silencieux ni ne colportez — aidez-le à grandir. Parlez en privé, avec douceur et amour. "
    "Tu savais ? «Tokhaha» (תוכחה) est lié à «hokhaha» (הוכחה), preuve d'une voie meilleure. "
    "Défi du jour : donnez un retour avec bonté et sagesse."
)
k = key_by_prefix["Guard against false prophecy"]
ES[k] = (
    "¡Cuídate de la falsa profecía! 🚫 El Rambam enseña (Yesodei haTorá, cap. 9): aunque alguien haga milagros, "
    "si intenta cambiar o anular parte de la Torá, es falso profeta. La profecía verdadera fortalece la Torá. "
    "Reto de hoy: mantente firme en la verdad eterna de la Torá."
)
FR[k] = (
    "Méfiez-vous de la fausse prophétie ! 🚫 Le Rambam enseigne (Yesodei haTorah, ch. 9) : même avec des miracles, "
    "celui qui change ou annule une partie de la Torah est un faux prophète. La vraie prophétie fortifie la Torah. "
    "Défi du jour : restez fermes dans la vérité éternelle de la Torah."
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
    "Si vous trouvez un livre ou papier sacré, conservez-le avec respect ou apportez-le à la gueniza. "
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
    "Il sanctifie le repas. Apprendre les étiquettes et la cuisine prend du temps — demandez à votre rav au départ."
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
    "Avant de partager des nouvelles sur quelqu'un, demandez-vous : est-ce nécessaire ? est-ce bienveillant ? "
    "Ne répétez pas les ragots. Demandez à votre rav dans les cas précis."
)
k = key_by_prefix["Learn about Dosh — threshing"]
ES[k] = (
    "¡Aprende sobre Dosh — trillar y extraer en Shabat! 🌾 En el Mishkán, separar grano de paja. "
    "Hoy: exprimir uvas u olivas es prohibición de Torá; los Sabios extendieron a otros frutos. "
    "En Shabat no «procesamos» la naturaleza — la disfrutamos tal cual."
)
FR[k] = (
    "Découvre Dosh — battage et extraction le Shabbat ! 🌾 Au Mishkan, séparer le grain de la paille. "
    "Aujourd'hui : presser raisins ou olives est une interdiction toranique ; les Sages l'ont étendue à d'autres fruits. "
    "Le Shabbat, on ne « transforme » pas la nature — on la savoure telle quelle."
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
    "Après un repas avec pain, Birkat ha-Mazon couvre tout. Demandez à votre rav."
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
k = key_by_prefix["Learn about Borer — the famous"]
ES[k] = (
    "Borer en Shabat 🥗 — tres condiciones: tomar lo bueno de lo malo, con la mano, para uso inmediato. "
    "No clasifiques por la mañana para el almuerzo. Aplica también fuera de la comida."
)
FR[k] = (
    "Borer le Shabbat 🥗 — trois conditions : prendre le bon du mauvais, à la main, pour usage immédiat. "
    "Ne triez pas le matin pour le déjeuner. S'applique aussi hors nourriture."
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
    "Bal yera'eh («il ne sera pas vu») est l'interdiction de la Torah qu'il reste du hamets visible chez vous à Pessa'h. "
    "Avec bal yimatzei («il ne sera pas trouvé»), cela commande bedikat hamets, biour et mehirat hamets. "
    "Même les miettes sous votre contrôle comptent. Le hamets vendu, zone fermée, doit rester inaccessible."
)
k = key_by_prefix["Bal yera'eh (\"it shall not be seen\") is the Torah prohibition"]
ES[k] = _BAL_YERAEH_ES
FR[k] = _BAL_YERAEH_FR
k = key_by_prefix["bal yera'eh — Bal yera'eh"]
ES[k] = "bal yera'eh — " + _BAL_YERAEH_ES
FR[k] = "bal yera'eh — " + _BAL_YERAEH_FR

k = key_by_prefix["Learn about Losh — kneading"]
ES[k] = (
    "Losh — amasar y formar una masa única 🍞 En el Mishkan: mezclar tintes y amasar el pan de la proposición. "
    "En Shabat está prohibido unir polvo fino con líquido en pasta homogénea. Para cereales o mostaza: cambie el orden "
    "(líquido primero) y el movimiento (shinui). La jalá se hornea el viernes."
)
FR[k] = (
    "Losh — pétrir et former une masse unique 🍞 Dans le Mishkan : teintures et pain de proposition. "
    "Chabbat interdit de lier poudre fine et liquide en pâte homogène. Pour céréales ou moutarde : ordre inversé "
    "(liquide d'abord) et geste inhabituel (shinui). La hallah se cuit vendredi."
)

_KIDDUSH_MAKOM_ES = (
    "Kidush be-makom seudah : sanctifier Chabbat ou Yom Tov avec le vin au même endroit où vous mangerez. "
    "Partir sans manger là-bas : kidush non accompli. Vendredi soir : pain (hamotsi) selon la plupart des poskim."
)
_KIDDUSH_MAKOM_FR = (
    "Kidouch be-makom seoudah : sanctifier Chabbat ou Yom Tov avec le vin où vous prendrez le repas. "
    "Partir sans manger sur place : kidouch non valide. Vendredi soir : pain (hamotsi) selon la plupart des poskim."
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
ES[k] = "17 Tamuz — ayuno por la brecha de las murallas de Jerusalén"
FR[k] = "17 Tamouz — jeûne pour la brèche des murailles de Jérusalem"

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
    "vous bénissez et annoncez le jour et la semaine du compte. Manquer une journée entière peut affecter la bénédiction des nuits suivantes — demandez à votre rav. "
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
    "Suivez le minhag familial ; en cas de doute, demandez à votre rav.\n\n"
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
    "Découvrez la mitsva d'Hadlakat Nerot — bougies de Chabbat — et un casse-tête halakhique fascinant ! 🕯️ "
    "Les Sages les ont instituées pour le shalom bayit, pas seulement l'ambiance : il faut profiter activement de la "
    "lumière. Et si vous accueillez Chabbat tôt alors que le soleil brille encore ? Débat du « à quoi bon une lampe "
    "à midi ? » (Houllin 60b). Maguen Avraham (O.H. 263:11) : si vous allumez et partez, asseyez-vous pour en "
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
    "demandez à votre rav, pas aux réseaux sociaux."
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
    "la bénédiction nocturne couvre le jour (Choulhan Aroukh ; Yalkut Yossef). Mots manqués — demandez à votre rav."
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
    "casher et noués correctement. Regarder les tsitsit pendant le Chemâ accomplit « vous les verrez ». Certaines "
    "femmes dans certaines communautés portent tsitsit — demandez à votre rav."
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
    "Plastique et verre — psak très divergents ashkénazes/séfarades, plus strict à Pessah. Demandez à votre rav. "
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
    "• Demandez à votre rav de vous montrer comment le porter et dire la bénédiction\n"
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

shard = {"es": ES, "fr": FR}
OUT.write_text(json.dumps(shard, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
print(f"Wrote {len(ES)} es + {len(FR)} fr entries to {OUT.name}")
