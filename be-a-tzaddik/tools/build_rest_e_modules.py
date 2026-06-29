#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Generate rest_e_1.py, rest_e_2.py, rest_e_3.py from translation data."""

from __future__ import annotations

import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent
sys.path.insert(0, str(ROOT))

from rest_e_translations import ENTRIES as EXISTING  # noqa: E402
from rest_e_batch3 import ENTRIES as BATCH3  # noqa: E402

def T(he, es, fr, ru):
    return (he, es, fr, ru)

# Indices 7-143 (first 7 come from EXISTING)
NEW = {
    "mechitza — A mechitza divides men and women's seating in shul during prayer so focus stays on tefillah per traditional practice. Heights and designs vary by community.": T(
        "mechitza — מחיצה מפרידה בין מקומות ישיבה לגברים ונשים בבית כנסת בתפילה כדי שהמיקוד יישאר בתפילה לפי מנהג מסורתי. גובה ועיצוב משתנים לפי קהילה.",
        "mechitza — Una mechitza divide los asientos de hombres y mujeres en la shul durante la oración para mantener el enfoque en la tefillah según la práctica tradicional. Las alturas y diseños varían según la comunidad.",
        "mechitza — Une mechitza sépare les places des hommes et des femmes à la shul pendant la prière afin que l'attention reste sur la tefillah selon la pratique traditionnelle. Hauteurs et designs varient selon la communauté.",
        "mechitza — Mechitza razdelyaet mesta muzhchin i zhenshchin v shul vo vremya molitvy, chtoby fokus ostalsya na tefillah po traditsionnoy praktike. Vysota i dizayn razlichayutsya po obshchinam.",
    ),
    '${yaalehVyavoFemaleAmidahLead("Mincha")}\n\n${yaalehVyavoForgotShacharitOrMincha("Mincha")}\n\nIf you say Birkat Hamazon when you eat bread today, add Yaaleh V\'yavo there too.': T(
        '${yaalehVyavoFemaleAmidahLead("Mincha")}\n\n${yaalehVyavoForgotShacharitOrMincha("Mincha")}\n\nIf you say Birkat Hamazon when you eat bread today, add Yaaleh V\'yavo there too.',
        '${yaalehVyavoFemaleAmidahLead("Mincha")}\n\n${yaalehVyavoForgotShacharitOrMincha("Mincha")}\n\nSi dices Birkat Hamazon cuando comas pan hoy, añade Yaaleh V\'yavo allí también.',
        '${yaalehVyavoFemaleAmidahLead("Mincha")}\n\n${yaalehVyavoForgotShacharitOrMincha("Mincha")}\n\nSi vous dites Birkat HaMazon aujourd\'hui après du pain, ajoutez Yaaleh V\'yavo là aussi.',
        '${yaalehVyavoFemaleAmidahLead("Mincha")}\n\n${yaalehVyavoForgotShacharitOrMincha("Mincha")}\n\nEsli segodnya govorite Birkat Hamazon posle khleba, dobavte Yaaleh V\'yavo i tam.',
    ),
    "Shehecheyanu (Ashkenaz — Rema O.C. 692:1): Recite before the first evening Megillah reading. Ashkenazim also recite Shehecheyanu before the first daytime Megillah reading.": T(
        "Shehecheyanu (Ashkenaz — Rema O.C. 692:1): Omru lifnei kriat Megillah harishona ba'erev. Ashkenazim omrim Shehecheyanu gam lifnei kriat Megillah hayom harishona.",
        "Shehecheyanu (Ashkenaz — Rema O.C. 692:1): Recitar antes de la primera lectura nocturna de la Meguilá. Los ashkenazim también recitan Shehecheyanu antes de la primera lectura diurna de la Meguilá.",
        "Shehecheyanu (Ashkenaz — Rema O.C. 692:1) : Réciter avant la première lecture du soir de la Meguila. Les Ashkenazim récitent aussi Shehecheyanu avant la première lecture de jour de la Meguila.",
        "Shehecheyanu (Ashkenaz — Rema O.C. 692:1): Proiznosite pered pervym vechernim chteniem Megillah. Ashkenazy takzhe proiznosyat Shehecheyanu pered pervym dnevnym chteniem Megillah.",
    ),
    "Tomorrow is erev Purim — and this year is Purim Meshulash in Jerusalem. Shabbat is in the middle, so you need the full plan now (not only tomorrow).\n\n${fullScheduleBlock()}": T(
        "Machar erev Purim — u'b'shanah zo Purim Meshulash biYerushalayim. Shabbat ba'emtza, lachein tzarich et ha'tochnit ha'mele'a achshav (lo rak machar).\n\n${fullScheduleBlock()}",
        "Mañana es erev Purim — y este año es Purim Meshulash en Jerusalén. Shabat está en medio, así que necesitas el plan completo ahora (no solo mañana).\n\n${fullScheduleBlock()}",
        "Demain c'est erev Pourim — et cette année c'est Pourim Mechoulash à Jérusalem. Chabbat est au milieu, il vous faut donc le plan complet maintenant (pas seulement demain).\n\n${fullScheduleBlock()}",
        "Zavtra erev Purim — i v etom godu Purim Meshulash v Ierusalime. Shabbat poseredine, poetomu nuzhen polnyy plan seychas (ne tolko zavtra).\n\n${fullScheduleBlock()}",
    ),
    "minor fast on 10 Tevet from dawn until nightfall; the only fast never postponed when it falls on Friday before Shabbat — in which case we break it with Friday night Kiddush": T(
        "tzom katan be-y' Asara B'Tevet me'alot ha'shemesh ad tzeit ha'kochavim; ha'tzom ha'yechidi she'lo nidchah k'she'hu nofel be'yom shishi lifnei Shabbat — u'va'mikre kach shovrim oto beKiddush leil Shabbat",
        "ayuno menor el 10 de Tevet desde el amanecer hasta la noche; el único ayuno que nunca se pospone cuando cae en viernes antes de Shabat — en cuyo caso lo rompemos con el Kiddush del viernes por la noche",
        "jeûne mineur le 10 Tévet de l'aube à la nuit ; le seul jeûne jamais reporté quand il tombe un vendredi avant Chabbat — auquel cas on le rompt avec le Kiddush du vendredi soir",
        "malyy post 10 Tevet s rassveta do nochi; edinstvennyy post, kotoryy nikogda ne perenositsya, esli padaet v pyatnitsu pered Shabbat — v etom sluchae preryvaem ego pyatnichnym vechernim Kiddush",
    ),
    "Saw a lost object recently? 🔍 Don't just ignore it! Go back and try to return it to its owner (if possible). That's the mitzvah of Hashavat Aveidah (returning lost objects).": T(
        "ראיתם לאחרונה חפץ אבוד? 🔍 אל תתעלמו! חזרו ונסו להחזירו לבעליו (אם אפשר). זו המצווה של השבת אבידה.",
        "¿Viste un objeto perdido recientemente? 🔍 ¡No lo ignores! Regresa e intenta devolverlo a su dueño (si es posible). Esa es la mitzvá de Hashavat Aveidah (devolver objetos perdidos).",
        "Vous avez vu un objet perdu récemment ? 🔍 Ne l'ignorez pas ! Retournez essayer de le rendre à son propriétaire (si possible). C'est la mitzvah de Hashavat Aveidah (restitution des objets perdus).",
        "Nedavno videli poteryannuyu veshch? 🔍 Ne ignoriruyte! Vernites i poprobuyte vernut ee vladeltsu (esli vozmozhno). Eto mitzvah Hashavat Aveidah (vozvrashchenie poteryannogo).",
    ),
    "Before chag — every erev Yom Tov:\n$sunsetLine\n$cookingLine\n• Turn off phones and devices before Yom Tov — this app is for prep, not use on chag.\n$shulServicesLine$simchasBlock": T(
        "Lifnei chag — kol erev Yom Tov:\n$sunsetLine\n$cookingLine\n• Kabe'u telefonim u'machshevim lifnei Yom Tov — ha'app hazeh le'hachana, lo le'shimmush bechag.\n$shulServicesLine$simchasBlock",
        "Antes del chag — cada erev Yom Tov:\n$sunsetLine\n$cookingLine\n• Apaga teléfonos y dispositivos antes de Yom Tov — esta app es para preparación, no para usar en chag.\n$shulServicesLine$simchasBlock",
        "Avant le chag — chaque erev Yom Tov :\n$sunsetLine\n$cookingLine\n• Éteignez téléphones et appareils avant Yom Tov — cette app sert à la préparation, pas à l'usage le jour de fête.\n$shulServicesLine$simchasBlock",
        "Pered chag — kazhdyy erev Yom Tov:\n$sunsetLine\n$cookingLine\n• Vyklyuchite telefony i ustroystva pered Yom Tov — eto prilozhenie dlya podgotovki, ne dlya ispolzovaniya v chag.\n$shulServicesLine$simchasBlock",
    ),
    "Chasidim — a group of Jews who follow a movement that emphasizes deep mystical devotion, joyful worship, and a close-knit community led by a spiritual leader known as a Rebbe.": T(
        "Chasidim — k'vutzat Yehudim ha'okvim acharei tnu'ah ha'medgasheset mesirut mistit amuka, avodah smeicha, ve'kehillah krova bi'rashtut manhig ruchani ha'yadu'a keRebbe.",
        "Jasidim — grupo de judíos que siguen un movimiento que enfatiza devoción mística profunda, culto alegre y comunidad unida liderada por un Rebbe.",
        "Hassidim — groupe de Juifs suivant un mouvement qui met l'accent sur la dévotion mystique profonde, le culte joyeux et une communauté soudée dirigée par un Rebbe.",
        "Hasidim — gruppa evreev, sleduyushchikh dvizheniyu s glubokoy mistической predannostyu, radostnym bogosluzheniem i tesnoy obshchinoy pod rukovodstvom Rebbe.",
    ),
    '${yaalehVyavoFemaleAmidahLead("Shacharit")}\n\n${yaalehVyavoForgotShacharitOrMincha("Shacharit")}\n\nIf you say Birkat Hamazon when you eat bread today, add Yaaleh V\'yavo there too.': T(
        '${yaalehVyavoFemaleAmidahLead("Shacharit")}\n\n${yaalehVyavoForgotShacharitOrMincha("Shacharit")}\n\nIf you say Birkat Hamazon when you eat bread today, add Yaaleh V\'yavo there too.',
        '${yaalehVyavoFemaleAmidahLead("Shacharit")}\n\n${yaalehVyavoForgotShacharitOrMincha("Shacharit")}\n\nSi dices Birkat Hamazon cuando comas pan hoy, añade Yaaleh V\'yavo allí también.',
        '${yaalehVyavoFemaleAmidahLead("Shacharit")}\n\n${yaalehVyavoForgotShacharitOrMincha("Shacharit")}\n\nSi vous dites Birkat HaMazon aujourd\'hui après du pain, ajoutez Yaaleh V\'yavo là aussi.',
        '${yaalehVyavoFemaleAmidahLead("Shacharit")}\n\n${yaalehVyavoForgotShacharitOrMincha("Shacharit")}\n\nEsli segodnya govorite Birkat Hamazon posle khleba, dobavte Yaaleh V\'yavo i tam.',
    ),
    "Beit HaMikdash — The Beit HaMikdash (Holy Temple) stood in Jerusalem as the center of sacrifice, pilgrimage, and divine presence. Its destruction on Tisha B'Av is mourned yearly.": T(
        "Beit HaMikdash — Beit HaMikdash (Mikdash HaKodesh) amad biYerushalayim k'merkaz korban, aliyah l'regel ve'shechinah. Churbano beTisha B'Av ne'evl shanah b'shanah.",
        "Beit HaMikdash — El Beit HaMikdash (Templo Sagrado) estuvo en Jerusalén como centro de sacrificio, peregrinación y presencia divina. Su destrucción en Tisha B'Av se llora anualmente.",
        "Beit HaMikdash — Le Beit HaMikdash (Temple Saint) se dressait à Jérusalem comme centre de sacrifice, pèlerinage et présence divine. Sa destruction à Ticha BeAv est pleurée chaque année.",
        "Beit HaMikdash — Beit HaMikdash (Svyatoy Khram) stoyal v Ierusalime kak tsentr zhertvoprinosheniy, palomnichestva i bozhestvennogo prisutstviya. Ego razrushenie v Tisha B'Av opлакивается ежегодно.",
    ),
    "Bracha acharona is the after-blessing said when you ate or drank enough of a food but did not have a bread meal requiring Birkat Hamazon — for example borei nefashot or al hamichya.": T(
        "Bracha acharona hi ha'bracha achar kvi'at ma'achal o mashke shelo hayta seudat lechem ha'tzricha Birkat Hamazon — lemashal borei nefashot o al hamichya.",
        "Bracha acharona es la bendición posterior cuando comiste o bebiste suficiente de un alimento pero no tuviste una comida con pan que requiera Birkat Hamazon — por ejemplo borei nefashot o al hamichya.",
        "Bracha acharona est la bénédiction après avoir mangé ou bu assez d'un aliment sans repas de pain nécessitant Birkat HaMazon — par exemple borei nefashot ou al hamichya.",
        "Bracha acharona — poslebrachie posle edy ili napitka bez khlebной трапезы, trebuyushchey Birkat Hamazon — naprimer borei nefashot ili al hamichya.",
    ),
    "YaKNeHaZ is a mnemonic for the order when Shabbat flows directly into Yom Tov (for example Saturday night Pesach). Use your siddur's festival night Kiddush section for instructions.": T(
        "YaKNeHaZ hu zikaron l'seder k'sheShabbat over yashir leYom Tov (lemashal motzaei Shabbat Pesach). Hashav siddur le'hanhagot Kiddush leil chag.",
        "YaKNeHaZ es un mnemónico para el orden cuando Shabat pasa directamente a Yom Tov (por ejemplo, noche de Pesaj). Usa la sección de Kiddush festivo nocturno de tu sidur.",
        "YaKNeHaZ est un moyen mnémotechnique pour l'ordre quand Chabbat enchaîne directement sur Yom Tov (par ex. samedi soir de Pessah). Consultez la section Kiddush de fête du soir de votre siddour.",
        "YaKNeHaZ — mnemonika poryadka, kogda Shabbat perekhodit neposredstvenno v Yom Tov (naprimer, subbotu vecherom Pesach). Ispolzuyte razdel prazdnichnogo vechernего Kiddush v siddure.",
    ),
    "Mezonot (borei minei mezonot) is the blessing on grain foods that are not bread — cake, crackers, pasta, cereal. It is shorter than hamotzi but still thanks G-d for grain sustenance.": T(
        "Mezonot (borei minei mezonot) hi ha'bracha al ma'achalei dagan she'einam lechem — ugah, krakers, pasta, dagan. Ktzurah mehamotzi aval modah la'Hashem al mazon dagan.",
        "Mezonot (borei minei mezonot) es la bendición sobre alimentos de grano que no son pan — pastel, galletas, pasta, cereal. Es más corta que hamotzi pero agradece a D-s por el sustento de grano.",
        "Mezonot (borei minei mezonot) est la bénédiction sur les aliments de céréales qui ne sont pas du pain — gâteau, crackers, pâtes, céréales. Plus courte que hamotzi mais remercie D. pour la subsistance de céréales.",
        "Mezonot (borei minei mezonot) — brakha na zernovye produkty, ne yavlyayushchiesya khlebom — keks, krekery, makaron, khlopya. Koroche hamotzi, no blagodarit V-ga za zernovoe propitanie.",
    ),
    "Next time you have guests, keep in mind to escort them on their way as they leave, even for just a few steps. The Talmud (Sotah 46b) says the reward for escorting others has no measure!": T(
        "Ba'pa'am haba'a she'yeish lachem orchim, zekhru le'lave otam baderech k'shehem yotz'im, afilu kama tzadadim. HaTalmud (Sotah 46b) omrim she'sachar ha'levaya ein lo shiur!",
        "La próxima vez que tengas invitados, recuerda escoltarlos al salir, aunque sean solo unos pasos. El Talmud (Sotah 46b) dice que la recompensa por acompañar a otros no tiene medida.",
        "La prochaine fois que vous avez des invités, pensez à les accompagner en partant, même quelques pas. Le Talmud (Sotah 46b) dit que la récompense de l'accompagnement n'a pas de mesure !",
        "V sleduyushchiy raz, kogda u vas gosti, provozhayte ikh pri ukhode, khotya by neskolko shagov. Talmud (Sotah 46b) govorit, chto nagrada za provody ne imeet predela!",
    ),
    'Ruach Ra\'ah ("evil spirit") is a rabbinic term for the impurity resting on hands after sleep. Morning netilat yadayim removes it by washing hands alternating right and left 3 times each.': T(
        "Ruach Ra'ah (\"ruach ra'ah\") hu term rabbani le'tumah ha'nuchach al ha'yadayim achar sheinah. Netilat yadayim baboker me'akher oto bi'khlitat yadayim yemin u'smol shalosh pe'amim kol achat.",
        'Ruach Ra\'ah ("espíritu malo") es un término rabínico para la impureza en las manos tras dormir. El netilat yadayim matutino lo elimina lavando manos alternando derecha e izquierda 3 veces cada una.',
        'Ruach Ra\'ah (« esprit mauvais ») est un terme rabbinique pour l\'impureté sur les mains après le sommeil. Le netilat yadayim du matin l\'enlève en lavant les mains droite et gauche 3 fois chacune.',
        'Ruach Ra\'ah («zloy dukh») — rabbinicheskiy termin dlya tumah na rukakh posle sna. Utrennyaya netilat yadayim ustraняet ego, promyvaya ruki po ocheredi pravyyu i levuyu po 3 raza.',
    ),
    "Asara B'Tevet — minor fast on 10 Tevet from dawn until nightfall; the only fast never postponed when it falls on Friday before Shabbat — in which case we break it with Friday night Kiddush": T(
        "Asara B'Tevet — tzom katan be-y' Tevet me'alot ha'shemesh ad tzeit ha'kochavim; ha'tzom ha'yechidi she'lo nidchah k'she'hu nofel be'yom shishi lifnei Shabbat — u'va'mikre kach shovrim oto beKiddush leil Shabbat",
        "Asara B'Tevet — ayuno menor el 10 de Tevet desde el amanecer hasta la noche; el único ayuno que nunca se pospone cuando cae en viernes antes de Shabat — en cuyo caso lo rompemos con el Kiddush del viernes por la noche",
        "Asara B'Tevet — jeûne mineur le 10 Tévet de l'aube à la nuit ; le seul jeûne jamais reporté quand il tombe un vendredi avant Chabbat — auquel cas on le rompt avec le Kiddush du vendredi soir",
        "Asara B'Tevet — malyy post 10 Tevet s rassveta do nochi; edinstvennyy post, kotoryy nikogda ne perenositsya, esli padaet v pyatnitsu pered Shabbat — v etom sluchae preryvaem ego pyatnichnym vechernim Kiddush",
    ),
    "Kallah means bride; kallah classes teach taharat hamishpacha, mitzvot of the home, and wedding laws before marriage. The kallah and chatan prepare for chuppah and building a Jewish family.": T(
        "Kallah mashma kalah; shiurei kallah melamedim taharat hamishpacha, mitzvot shel habayit ve'chukei nisuin lifnei ha'nisuin. Ha'kallah ve'ha'chatan mitkadshim le'chuppah u'le'binyan mishpacha Yehudit.",
        "Kallah significa novia; las clases de kallah enseñan taharat hamishpacha, mitzvot del hogar y leyes de boda antes del matrimonio. La kallah y el chatan se preparan para la jupá y construir una familia judía.",
        "Kallah signifie mariée ; les cours de kallah enseignent taharat hamishpacha, mitzvot du foyer et lois de mariage avant les noces. La kallah et le hatan se préparent pour la houppa et bâtir une famille juive.",
        "Kallah oznachaet nevestu; zanyatiya kallah uchат taharat hamishpacha, mitzvot doma i svadebnym zakonam do braka. Kallah i chatan gotovyatsya k chuppah i sozdaniyu evreyskoy semyi.",
    ),
    'Vatodi\'enu ("You have made us know") is an insert in the Maariv Amidah on Saturday night when Yom Tov begins after Shabbat. It acknowledges that Shabbat has ended and the festival has begun.': T(
        "Vatodi'enu (\"hoda'atanu\") hu haser baAmidah shel Maariv be'motzaei Shabbat k'sheYom Tov mat'hil achar Shabbat. Hu me'ir sheShabbat nigmer veha'chag hit'hil.",
        'Vatodi\'enu ("Nos has hecho saber") es una inserción en la Amidá de Maariv el sábado por la noche cuando Yom Tov comienza tras Shabat. Reconoce que Shabat terminó y la festividad comenzó.',
        'Vatodi\'enu (« Tu nous as fait savoir ») est une insertion dans l\'Amidah de Maariv le samedi soir quand Yom Tov commence après Chabbat. Elle reconnaît que Chabbat est fini et la fête a commencé.',
        'Vatodi\'enu («Ty sdelal nas znaющimi») — vstavka v Amidah Maariv v subbotu vecherom, kogda Yom Tov nachinaetsya posle Shabbat. Priznaet okonchanie Shabbat i nachalo prazdnika.',
    ),
    "Mezonot — Mezonot (borei minei mezonot) is the blessing on grain foods that are not bread — cake, crackers, pasta, cereal. It is shorter than hamotzi but still thanks G-d for grain sustenance.": T(
        "Mezonot — Mezonot (borei minei mezonot) hi ha'bracha al ma'achalei dagan she'einam lechem — ugah, krakers, pasta, dagan. Ktzurah mehamotzi aval modah la'Hashem al mazon dagan.",
        "Mezonot — Mezonot (borei minei mezonot) es la bendición sobre alimentos de grano que no son pan — pastel, galletas, pasta, cereal. Es más corta que hamotzi pero agradece a D-s por el sustento de grano.",
        "Mezonot — Mezonot (borei minei mezonot) est la bénédiction sur les aliments de céréales qui ne sont pas du pain — gâteau, crackers, pâtes, céréales. Plus courte que hamotzi mais remercie D. pour la subsistance de céréales.",
        "Mezonot — Mezonot (borei minei mezonot) — brakha na zernovye produkty, ne yavlyayushchiesya khlebom — keks, krekery, makaron, khlopya. Koroche hamotzi, no blagodarit V-ga za zernovoe propitanie.",
    ),
    "Yaknehaz — YaKNeHaZ is a mnemonic for the order when Shabbat flows directly into Yom Tov (for example Saturday night Pesach). Use your siddur's festival night Kiddush section for instructions.": T(
        "Yaknehaz — YaKNeHaZ hu zikaron l'seder k'sheShabbat over yashir leYom Tov (lemashal motzaei Shabbat Pesach). Hashav siddur le'hanhagot Kiddush leil chag.",
        "Yaknehaz — YaKNeHaZ es un mnemónico para el orden cuando Shabat pasa directamente a Yom Tov (por ejemplo, noche de Pesaj). Usa la sección de Kiddush festivo nocturno de tu sidur.",
        "Yaknehaz — YaKNeHaZ est un moyen mnémotechnique pour l'ordre quand Chabbat enchaîne directement sur Yom Tov (par ex. samedi soir de Pessah). Consultez la section Kiddush de fête du soir de votre siddour.",
        "Yaknehaz — YaKNeHaZ — mnemonika poryadka, kogda Shabbat perekhodit neposredstvenno v Yom Tov (naprimer, subbotu vecherom Pesach). Ispolzuyte razdel prazdnichnogo vechernего Kiddush v siddure.",
    ),
    "Add Yaaleh V'yavo in the Maariv Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).\n\n${yaalehVyavoForgotMaarivRoshChodesh()}\n\nAlso add Yaaleh V'yavo in bentching if you eat bread tonight.": T(
        "Hosifu Yaaleh V'yavo baAmidah shel Maariv beRosh Chodesh — bi'bracha Retzei (Avodah).\n\n${yaalehVyavoForgotMaarivRoshChodesh()}\n\nHosifu Yaaleh V'yavo gam bebentching im tochlu lechem ha'layla.",
        "Añade Yaaleh V'yavo en la Amidá de Maariv en Rosh Jodesh — en la bendición Retzei (Avodah).\n\n${yaalehVyavoForgotMaarivRoshChodesh()}\n\nTambién añade Yaaleh V'yavo en bentching si comes pan esta noche.",
        "Ajoutez Yaaleh V'yavo dans l'Amidah de Maariv à Roch 'Hodesh — dans la bénédiction Retzei (Avodah).\n\n${yaalehVyavoForgotMaarivRoshChodesh()}\n\nAjoutez aussi Yaaleh V'yavo au bentching si vous mangez du pain ce soir.",
        "Dobavte Yaaleh V'yavo v Amidah Maariv v Rosh Chodesh — v brakhe Retzei (Avodah).\n\n${yaalehVyavoForgotMaarivRoshChodesh()}\n\nTakzhe dobavte Yaaleh V'yavo v bentching, esli vecherom est khleb.",
    ),
    "Purim is tomorrow. Plan all four mitzvot: Megillah (night and morning), matanot la'evyonim, mishloach manot, and tomorrow's festive seudah (afternoon meal). Confirm reading times with your shul.": T(
        "Purim machar. Tichnu et arba hamitzvot: Megillah (layla u'boker), matanot la'evyonim, mishloach manot, ve'seudat Purim machar (seudah ba'tzohorayim). Achru zmanei kriah im ha'shul.",
        "Purim es mañana. Planifica las cuatro mitzvot: Meguilá (noche y mañana), matanot la'evyonim, mishloach manot y la seudá festiva de mañana (comida vespertina). Confirma horarios de lectura con tu shul.",
        "Pourim est demain. Planifiez les quatre mitzvot : Meguila (soir et matin), matanot la'evyonim, mishloach manot et la seudah festive de demain (repas de l'après-midi). Confirmez les horaires de lecture avec votre shul.",
        "Purim zavtra. Zaplaniruyte chetyre mitzvot: Megillah (noch i utro), matanot la'evyonim, mishloach manot i prazdnichnuyu seudah zavtra (obed posle poludnya). Utochnite vremya chteniya v shul.",
    ),
    "Take a minute to pray for someone else's needs before your own. The sages teach that when you pray for others with pure intentions, and you happen to need the same thing, you get answered first!": T(
        "Kachu dakah le'hitpalel al tzorkhei acherim lifnei shelkha. Ha'chachamim melamedim she'k'she'mitpalelim la'acheirim be'kavanah tehorah, ve'gam atah tzarich et oto davar — anuim rishonim!",
        "Tómate un minuto para orar por las necesidades de otros antes que las tuyas. Los sabios enseñan que cuando oras por otros con intenciones puras y tú necesitas lo mismo, ¡te responden primero!",
        "Prenez une minute pour prier pour les besoins d'autrui avant les vôtres. Les sages enseignent que quand vous priez pour les autres avec de bonnes intentions et que vous avez le même besoin, vous êtes exaucé en premier !",
        "Udelyte minutu molitve o nuzhdakh drugikh prezhde svoikh. Mudretsy uchат: kogda molites za drugikh s chistymi namereniyami i vam nuzhno to zhe — vam otvechayut pervym!",
    ),
    "Aggadah is the non-legal storytelling and teaching in the Talmud and midrash — ethics, theology, and narrative. It complements halacha (law). Shabbat table stories and many midrashim are aggadah.": T(
        "Aggadah hi ha'sippur ve'ha'limud she'einam halacha baTalmud u'vamidrash — musar, emunah ve'sippur. Hi meshalemet et ha'halacha. Sippurei shulchan Shabbat ve'rabim midrashim hem aggadah.",
        "Aggadah es la narrativa y enseñanza no legal en el Talmud y midrash — ética, teología y relatos. Complementa la halajá (ley). Las historias de mesa de Shabat y muchos midrashim son aggadah.",
        "Aggadah est le récit et l'enseignement non juridique du Talmud et du midrash — éthique, théologie et narration. Elle complète la halakha (loi). Les histoires de table de Chabbat et beaucoup de midrashim sont aggadah.",
        "Aggadah — nelegalnye rasskazy i ucheniya v Talmude i midrashe — etika, bogoslovie i narrativ. Dopolnayet halakhu (zakon). Shabbat-stol rasskazy i mnogie midrashim — eto aggadah.",
    ),
    "Please put away your device and keep Shabbat. Rest, pray, learn Torah, and enjoy time with family and community. Melacha (forbidden labor) applies on Shabbat, including most phone and device use.": T(
        "אנא הניחu את המכשיר ושמרu על Shabbat. נוחu, התפללu, למdu Torah, והנu מזמן עם משפחה וקהילה. Melacha (avoda asura) chala beShabbat, כולל רוב השימוש בטלפון ובמכשירים.",
        "Por favor guarda tu dispositivo y guarda Shabat. Descansa, ora, aprende Torá y disfruta con familia y comunidad. Melacha (trabajo prohibido) aplica en Shabat, incluyendo la mayoría del uso de teléfono y dispositivos.",
        "Veuillez ranger votre appareil et garder Chabbat. Reposez-vous, priez, étudiez la Torah et profitez de la famille et de la communauté. Melacha (travail interdit) s'applique le Chabbat, y compris la plupart de l'usage du téléphone et des appareils.",
        "Uберите ustroystvo i soblyudayte Shabbat. Otdykhnite, molites, uchites Toru i provodite vremya s semyey i obshchinoy. Melacha (zapreshchennyy trud) primenyaetsya v Shabbat, vklyuchaya bolshinstvo ispolzovaniya telefona i ustroystv.",
    ),
    "Nevi'im (Prophets) is the middle section of Tanach — Joshua, Kings, Isaiah, Jeremiah, and the other prophetic books. They call Israel to justice and loyalty to Hashem. Reading them is Torah study.": T(
        "Nevi'im (Nevi'im) hu helek ha'emtza'i shel Tanach — Yehoshua, Melachim, Yeshayahu, Yirmiyahu ve'sefer nevi'im acherim. Hem kor'im leYisrael le'tzedek u'le'emunah ba'Hashem. Kriatam hi limud Torah.",
        "Nevi'im (Profetas) es la sección central del Tanaj — Josué, Reyes, Isaías, Jeremías y los demás libros proféticos. Llaman a Israel a la justicia y lealtad a Hashem. Leerlos es estudio de Torá.",
        "Nevi'im (Prophètes) est la section centrale du Tanakh — Josué, Rois, Isaïe, Jérémie et les autres livres prophétiques. Ils appellent Israël à la justice et à la fidélité à Hachem. Les lire est étude de Torah.",
        "Nevi'im (Proroki) — srednyaya chast Tanakh — Yehoshua, Melakhim, Yeshayahu, Yirmiyahu i drugie knigi prorokov. Prizyvayut Izrail k spravedlivosti i vernosti Hashemu. Chtenie ikh — izuchenie Tory.",
    ),
    "catch-all blessing on fragrant herbs, spices, flowers, fruit, etc. (often said as the spice blessing in Havdalah after Shabbat, though some will say a more specific blessing on pleasant fragrances)": T(
        "bracha klalit al samim, tavlinim, perachim, pri, ve'ch. (l'rov ne'emra ke'brachat ha'besamim baHavdalah achar Shabbat, af al pi she'yesh ha'omrim bracha meyuchedet yoter al rei'ach naim)",
        "bendición general sobre hierbas aromáticas, especias, flores, fruta, etc. (a menudo dicha como bendición de especias en Havdalah tras Shabat, aunque algunos dicen una bendición más específica sobre fragancias agradables)",
        "bénédiction générale sur herbes parfumées, épices, fleurs, fruits, etc. (souvent dite comme bénédiction des épices à Havdalah après Chabbat, bien que certains disent une bénédiction plus spécifique sur les parfums agréables)",
        "obshchaya brakha na aromatnye travy, specii, tsvety, plody i t.d. (chasto kak brakha na specii v Havdalah posle Shabbat, khotya nekotorye govoryat bolee konkretnuyu brakhu na priyatnye zapakhi)",
    ),
    "kallah — Kallah means bride; kallah classes teach taharat hamishpacha, mitzvot of the home, and wedding laws before marriage. The kallah and chatan prepare for chuppah and building a Jewish family.": T(
        "kallah — Kallah mashma kalah; shiurei kallah melamedim taharat hamishpacha, mitzvot shel habayit ve'chukei nisuin lifnei ha'nisuin. Ha'kallah ve'ha'chatan mitkadshim le'chuppah u'le'binyan mishpacha Yehudit.",
        "kallah — Kallah significa novia; las clases de kallah enseñan taharat hamishpacha, mitzvot del hogar y leyes de boda antes del matrimonio. La kallah y el chatan se preparan para la jupá y construir una familia judía.",
        "kallah — Kallah signifie mariée ; les cours de kallah enseignent taharat hamishpacha, mitzvot du foyer et lois de mariage avant les noces. La kallah et le hatan se préparent pour la houppa et bâtir une famille juive.",
        "kallah — Kallah oznachaet nevestu; zanyatiya kallah uchат taharat hamishpacha, mitzvot doma i svadebnym zakonam do braka. Kallah i chatan gotovyatsya k chuppah i sozdaniyu evreyskoy semyi.",
    ),
    "When (Northern Hemisphere — your location):\nSay it during Nissan, when fruit trees in your area begin to blossom — ideally as early in the month as you first see flowers (Shulchan Arukh O.C. 226:1).": T(
        "Matay (hemisphere tzafoni — hamakom shelkha):\nOmru bechodesh Nissan, k'she'etz pri ba'ezor shelkha mat'hil le'porach — ideally me'urav ba'chodesh k'she'atem ro'im perachim rishonim (Shulchan Arukh O.C. 226:1).",
        "Cuándo (hemisferio norte — tu ubicación):\nDilo durante Nissan, cuando los árboles frutales de tu zona empiecen a florecer — idealmente tan pronto en el mes como veas las primeras flores (Shuljan Aruj O.C. 226:1).",
        "Quand (hémisphère nord — votre lieu) :\nDites-le pendant Nissan, quand les arbres fruitiers de votre région commencent à fleurir — idéalement dès les premières fleurs du mois (Choulhan Aroukh O.C. 226:1).",
        "Kogda (severnoe polusharie — vash region):\nGovorite v Nissan, kogda plodovye derevya v vashem rayone nachinayut tsvet — idealno kak mozhno ranshe v mesyatse, kogda uvidite pervye tsvety (Shulchan Arukh O.C. 226:1).",
    ),
    "Nefesh is the 'animal' part of the soul closest to physical life — appetite, hunger, physical desires, etc. Torah and mitzvot attempt to refine the nefesh toward holiness instead of animal instincts.": T(
        "Nefesh hi helek ha'nefesh ha'karov le'chayim gashmiyim — ta'avah, ra'av, ta'avot gufaniyot ve'ch. Torah u'mitzvot me'ayenim le'zakh et ha'nefesh le'kedushah bimkom instinkt behemi.",
        "Nefesh es la parte 'animal' del alma más cercana a la vida física — apetito, hambre, deseos físicos, etc. Torá y mitzvot intentan refinar el nefesh hacia la santidad en lugar de instintos animales.",
        "Nefesh est la partie « animale » de l'âme la plus proche de la vie physique — appétit, faim, désirs physiques, etc. Torah et mitzvot cherchent à raffiner le nefesh vers la sainteté plutôt que les instincts animaux.",
        "Nefesh — «zhivotnaya» chast dushi, blizhayshaya k fizicheskoy zhizni — appetit, golod, fizicheskie zhelaniya i t.d. Tora i mitzvot stremyatsya ochistit nefesh k kedushah vmesto zhivotnykh instinktov.",
    ),
    "Spread the joy of mitzvot! Share the Mitz Mode app with your friends 📱. Every time they use it to do a mitzvah, you get credit for that mitzvah too! It's like creating an endless chain of good deeds!": T(
        "הפיצu את שמחת המצוות! שתפu את אפליקציית Mitz Mode עם חברים 📱. בכל פעם שהם משתמשים בה לעשות mitzvah, גם אתם מקבלים זcredit לmitzvah הזו! זה כמו ליצור שרשרת אינסופית של מעשים טובים!",
        "¡Difunde la alegría de las mitzvot! Comparte la app Mitz Mode con tus amigos 📱. Cada vez que la usen para hacer una mitzvá, ¡tú también recibes crédito por esa mitzvá! ¡Es como crear una cadena infinita de buenas acciones!",
        "Diffusez la joie des mitzvot ! Partagez l'app Mitz Mode avec vos amis 📱. Chaque fois qu'ils l'utilisent pour une mitzvah, vous recevez aussi le mérite de cette mitzvah ! C'est comme créer une chaîne infinie de bonnes actions !",
        "Rasprostranyayte radost mitzvot! Podelites prilozheniem Mitz Mode s druzyami 📱. Kazhdyy raz, kogda oni delayut mitzvah cherez nego, vy tozhe poluchаете zaslugu za etu mitzvah! Eto kak beskonechnaya tsep dobrodelaniy!",
    ),
    "bracha acharona — Bracha acharona is the after-blessing said when you ate or drank enough of a food but did not have a bread meal requiring Birkat Hamazon — for example borei nefashot or al hamichya.": T(
        "bracha acharona — Bracha acharona hi ha'bracha achar kvi'at ma'achal o mashke shelo hayta seudat lechem ha'tzricha Birkat Hamazon — lemashal borei nefashot o al hamichya.",
        "bracha acharona — Bracha acharona es la bendición posterior cuando comiste o bebiste suficiente de un alimento pero no tuviste una comida con pan que requiera Birkat Hamazon — por ejemplo borei nefashot o al hamichya.",
        "bracha acharona — Bracha acharona est la bénédiction après avoir mangé ou bu assez d'un aliment sans repas de pain nécessitant Birkat HaMazon — par exemple borei nefashot ou al hamichya.",
        "bracha acharona — Bracha acharona — poslebrachie posle edy ili napitka bez khlebной трапезы, trebuyushchey Birkat Hamazon — naprimer borei nefashot ili al hamichya.",
    ),
    'Ruach Ra\'ah — Ruach Ra\'ah ("evil spirit") is a rabbinic term for the impurity resting on hands after sleep. Morning netilat yadayim removes it by washing hands alternating right and left 3 times each.': T(
        'Ruach Ra\'ah — Ruach Ra\'ah ("ruach ra\'ah") hu term rabbani le\'tumah ha\'nuchach al ha\'yadayim achar sheinah. Netilat yadayim baboker me\'akher oto bi\'khlitat yadayim yemin u\'smol shalosh pe\'amim kol achat.',
        'Ruach Ra\'ah — Ruach Ra\'ah ("espíritu malo") es un término rabínico para la impureza en las manos tras dormir. El netilat yadayim matutino lo elimina lavando manos alternando derecha e izquierda 3 veces cada una.',
        'Ruach Ra\'ah — Ruach Ra\'ah (« esprit mauvais ») est un terme rabbinique pour l\'impureté sur les mains après le sommeil. Le netilat yadayim du matin l\'enlève en lavant les mains droite et gauche 3 fois chacune.',
        'Ruach Ra\'ah — Ruach Ra\'ah («zloy dukh») — rabbinicheskiy termin dlya tumah na rukakh posle sna. Utrennyaya netilat yadayim ustraняet ego, promyvaya ruki po ocheredi pravyyu i levuyu po 3 raza.',
    ),
    "Chametz is leavened grain product made from wheat, barley, rye, oats, or spelt. Bread, beer, and many processed foods can be chametz. On Pesach, owning, eating, or benefiting from chametz is forbidden.": T(
        "Chametz hu ma'achal dagan chamutz me'chitah, se'orah, shifon, shibolet shu'al o kusmin. Lechem, shikur ve'rabim ma'achalim me'ukavim yecholim lihyot chametz. BePesach, achizat, achilah o hana'ah mechametz asura.",
        "Chametz es producto de grano fermentado de trigo, cebada, centeno, avena o espelta. Pan, cerveza y muchos alimentos procesados pueden ser chametz. En Pesaj, poseer, comer o beneficiarse de chametz está prohibido.",
        "Chametz est un produit de céréales levé de blé, orge, seigle, avoine ou épeautre. Pain, bière et beaucoup d'aliments transformés peuvent être chametz. À Pessah, posséder, manger ou profiter de chametz est interdit.",
        "Chametz — kislyy zernovoy produkt iz pshenitsy, yachmenya, rzhi, ovsa ili polby. Khleb, pivo i mnogie obrabotannye produkty mogut byt chametz. Na Pesach vladenie, eda ili polza ot chametz zapreshcheny.",
    ),
    'Vatodi\'enu — Vatodi\'enu ("You have made us know") is an insert in the Maariv Amidah on Saturday night when Yom Tov begins after Shabbat. It acknowledges that Shabbat has ended and the festival has begun.': T(
        'Vatodi\'enu — Vatodi\'enu ("hoda\'atanu") hu haser baAmidah shel Maariv be\'motzaei Shabbat k\'sheYom Tov mat\'hil achar Shabbat. Hu me\'ir sheShabbat nigmer veha\'chag hit\'hil.',
        'Vatodi\'enu — Vatodi\'enu ("Nos has hecho saber") es una inserción en la Amidá de Maariv el sábado por la noche cuando Yom Tov comienza tras Shabat. Reconoce que Shabat terminó y la festividad comenzó.',
        'Vatodi\'enu — Vatodi\'enu (« Tu nous as fait savoir ») est une insertion dans l\'Amidah de Maariv le samedi soir quand Yom Tov commence après Chabbat. Elle reconnaît que Chabbat est fini et la fête a commencé.',
        'Vatodi\'enu — Vatodi\'enu («Ty sdelal nas znaющimi») — vstavka v Amidah Maariv v subbotu vecherom, kogda Yom Tov nachinaetsya posle Shabbat. Priznaet okonchanie Shabbat i nachalo prazdnika.',
    ),
    "Get rid of something chametz (leavened) you're not going to eat and have in mind that you're fulfilling the mitzvah of cleaning your home of chametz for Pesach. If it is currently Pesach, eat some matzah!": T(
        "Hishlikhu davar chametz (chamutz) she'einachem ochelin oto u'kavanu she'atem mekayemim mitzvat nikuy habayit mechametz lePesach. Im kach zman Pesach, ochlu ktzat matzah!",
        "Desecha algo chametz (fermentado) que no vayas a comer y ten presente que cumples la mitzvá de limpiar tu hogar de chametz para Pesaj. Si ahora es Pesaj, ¡come un poco de matzá!",
        "Débarrassez-vous d'un chametz (levé) que vous ne mangerez pas en ayant l'intention d'accomplir la mitzvah de nettoyer votre maison du chametz pour Pessah. Si nous sommes déjà à Pessah, mangez de la matza !",
        "Izavites ot chem-to chametz (kislogo), chto ne budete est, s namereniem vypolnit mitzvah ochistki doma ot chametz k Pesach. Esli seychas Pesach — seshite nemnogo matzah!",
    ),
    "Add Yaaleh V'yavo in the Shacharit Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).\n\n${yaalehVyavoForgotShacharitOrMincha(\"Shacharit\")}\n\nAlso add Yaaleh V'yavo in bentching if you eat bread today.": T(
        "Hosifu Yaaleh V'yavo baAmidah shel Shacharit beRosh Chodesh — bi'bracha Retzei (Avodah).\n\n${yaalehVyavoForgotShacharitOrMincha(\"Shacharit\")}\n\nHosifu Yaaleh V'yavo gam bebentching im tochlu lechem hayom.",
        "Añade Yaaleh V'yavo en la Amidá de Shajarit en Rosh Jodesh — en la bendición Retzei (Avodah).\n\n${yaalehVyavoForgotShacharitOrMincha(\"Shacharit\")}\n\nTambién añade Yaaleh V'yavo en bentching si comes pan hoy.",
        "Ajoutez Yaaleh V'yavo dans l'Amidah de Shacharit à Roch 'Hodesh — dans la bénédiction Retzei (Avodah).\n\n${yaalehVyavoForgotShacharitOrMincha(\"Shacharit\")}\n\nAjoutez aussi Yaaleh V'yavo au bentching si vous mangez du pain aujourd'hui.",
        "Dobavte Yaaleh V'yavo v Amidah Shacharit v Rosh Chodesh — v brakhe Retzei (Avodah).\n\n${yaalehVyavoForgotShacharitOrMincha(\"Shacharit\")}\n\nTakzhe dobavte Yaaleh V'yavo v bentching, esli segodnya est khleb.",
    ),
    "aggadah — Aggadah is the non-legal storytelling and teaching in the Talmud and midrash — ethics, theology, and narrative. It complements halacha (law). Shabbat table stories and many midrashim are aggadah.": T(
        "aggadah — Aggadah hi ha'sippur ve'ha'limud she'einam halacha baTalmud u'vamidrash — musar, emunah ve'sippur. Hi meshalemet et ha'halacha. Sippurei shulchan Shabbat ve'rabim midrashim hem aggadah.",
        "aggadah — Aggadah es la narrativa y enseñanza no legal en el Talmud y midrash — ética, teología y relatos. Complementa la halajá (ley). Las historias de mesa de Shabat y muchos midrashim son aggadah.",
        "aggadah — Aggadah est le récit et l'enseignement non juridique du Talmud et du midrash — éthique, théologie et narration. Elle complète la halakha (loi). Les histoires de table de Chabbat et beaucoup de midrashim sont aggadah.",
        "aggadah — Aggadah — nelegalnye rasskazy i ucheniya v Talmude i midrashe — etika, bogoslovie i narrativ. Dopolnayet halakhu (zakon). Shabbat-stol rasskazy i mnogie midrashim — eto aggadah.",
    ),
    "Nevi'im — Nevi'im (Prophets) is the middle section of Tanach — Joshua, Kings, Isaiah, Jeremiah, and the other prophetic books. They call Israel to justice and loyalty to Hashem. Reading them is Torah study.": T(
        "Nevi'im — Nevi'im (Nevi'im) hu helek ha'emtza'i shel Tanach — Yehoshua, Melachim, Yeshayahu, Yirmiyahu ve'sefer nevi'im acherim. Hem kor'im leYisrael le'tzedek u'le'emunah ba'Hashem. Kriatam hi limud Torah.",
        "Nevi'im — Nevi'im (Profetas) es la sección central del Tanaj — Josué, Reyes, Isaías, Jeremías y los demás libros proféticos. Llaman a Israel a la justicia y lealtad a Hashem. Leerlos es estudio de Torá.",
        "Nevi'im — Nevi'im (Prophètes) est la section centrale du Tanakh — Josué, Rois, Isaïe, Jérémie et les autres livres prophétiques. Ils appellent Israël à la justice et à la fidélité à Hachem. Les lire est étude de Torah.",
        "Nevi'im — Nevi'im (Proroki) — srednyaya chast Tanakh — Yehoshua, Melakhim, Yeshayahu, Yirmiyahu i drugie knigi prorokov. Prizyvayut Izrail k spravedlivosti i vernosti Hashemu. Chtenie ikh — izuchenie Tory.",
    ),
    "Thank G-d for giving you the ability to feel textures and temperatures 🤚. From soft bristly grass to cooling water to a fluffy blanket - every sensation is a unique blessing that adds richness to our lives!": T(
        "הודu לה' שנתן לכם את היכולת להרגיש מרקמים וטמפרטורות 🤚. מעשב רך וסמרתי ועד מים מקררים ושמיכה רכה — כל תחושה היא bracha ייחודית שמעשירה את חיינו!",
        "Agradece a D-s por darte la capacidad de sentir texturas y temperaturas 🤚. Desde hierba suave y cerosa hasta agua refrescante y una manta mullida — ¡cada sensación es una bendición única que enriquece nuestras vidas!",
        "Remerciez D. de vous avoir donné la capacité de sentir textures et températures 🤚. De l'herbe douce et rêche à l'eau fraîche et une couverture moelleuse — chaque sensation est une bénédiction unique qui enrichit nos vies !",
        "Blagodarite V-ga za sposobnost chuvstvovat faktury i temperatury 🤚. Ot myagkoy kolюchey travy do prokhladnoy vody i pushenogo odeyala — kazhdoe oshchushchenie unikalnaya brakha, obogashchayushchaya zhizn!",
    ),
    "nefesh — Nefesh is the 'animal' part of the soul closest to physical life — appetite, hunger, physical desires, etc. Torah and mitzvot attempt to refine the nefesh toward holiness instead of animal instincts.": T(
        "nefesh — Nefesh hi helek ha'nefesh ha'karov le'chayim gashmiyim — ta'avah, ra'av, ta'avot gufaniyot ve'ch. Torah u'mitzvot me'ayenim le'zakh et ha'nefesh le'kedushah bimkom instinkt behemi.",
        "nefesh — Nefesh es la parte 'animal' del alma más cercana a la vida física — apetito, hambre, deseos físicos, etc. Torá y mitzvot intentan refinar el nefesh hacia la santidad en lugar de instintos animales.",
        "nefesh — Nefesh est la partie « animale » de l'âme la plus proche de la vie physique — appétit, faim, désirs physiques, etc. Torah et mitzvot cherchent à raffiner le nefesh vers la sainteté plutôt que les instincts animaux.",
        "nefesh — Nefesh — «zhivotnaya» chast dushi, blizhayshaya k fizicheskoy zhizni — appetit, golod, fizicheskie zhelaniya i t.d. Tora i mitzvot stremyatsya ochistit nefesh k kedushah vmesto zhivotnykh instinktov.",
    ),
    "Learn about Tefillin! 👑 Did you know? The head Tefillin contains the same four Torah passages as the arm Tefillin, but arranged differently! Today's mission: Learn about how Tefillin connects our minds to G-d.": T(
        "למdu על Tefillin! 👑 ידעתם? Tefillin shel rosh mekabelim et arba haparshiyot shel Tefillin shel yad, aval be'seder shonе! Ha'mission shel hayom: limud al eich Tefillin mechabrim et moachenu la'Hashem.",
        "¡Aprende sobre Tefilín! 👑 ¿Sabías? El Tefilín de la cabeza contiene los mismos cuatro pasajes de Torá que el del brazo, ¡pero ordenados de forma distinta! Misión de hoy: aprende cómo el Tefilín conecta nuestra mente con D-s.",
        "Apprenez sur les Téfilines ! 👑 Le saviez-vous ? Le Téfiline de la tête contient les mêmes quatre passages de Torah que celui du bras, mais disposés différemment ! Mission du jour : apprendre comment les Téfilines relient notre esprit à D.",
        "Uchites o Tefillin! 👑 Znali li? Tefillin na golove soderzhit te zhe chetyre otryvka Tory, chto i na ruke, no v drugom poryadke! Zadacha dnya: kak Tefillin svyazyvaet um s V-gom.",
    ),
    "Discover the deeper meaning of the beard in Judaism! 🧔 Did you know? Kabbalists say that the 13 attributes of Divine mercy can emanate through the hairs of the beard! Now that's what I call a valuable hairloom.": T(
        "גלו את המשמעות העמוקה של הזקן ביהדות! 🧔 ידעתם? קabbalistim omrim she-13 midot ha'rachamim yecholim le'histalek דרך שערות הזקן! זו באמת ירושה שיערית יקרת ערך.",
        "¡Descubre el significado profundo de la barba en el judaísmo! 🧔 ¿Sabías? Los cabalistas dicen que los 13 atributos de la misericordia divina pueden emanar a través de los pelos de la barba. ¡Eso sí que es un legado valioso!",
        "Découvrez la signification profonde de la barbe dans le judaïsme ! 🧔 Les kabbalistes disent que les 13 attributs de miséricorde divine peuvent émaner par les poils de la barbe ! Voilà un héritage précieux !",
        "Otkroyte glubokiy smysl borody v iudaizme! 🧔 Kabbалisty govoryat, chto 13 svoystv bozhestvennoy milosti mogut izluchatsya cherez volosy borody! Vot eto tsennoye nasledie!",
    ),
    "Maleh lugmov is drinking a cheekful from the Kiddush cup (approx 2 fl oz) after the blessing — from at least a total of a revi'it of wine or grape juice in the cup. Grape juice counts like wine for many poskim.": T(
        "Maleh lugmov hu shtiyah maleh lugma mi'kos haKiddush (b'kiruv 60 ml) achar ha'bracha — mi'klal revi'it yain o mishmash anavim ba'kos. Mishmash anavim nechshav keyayin le'rabim mi'ha'poskim.",
        "Maleh lugmov es beber un puñado de la copa de Kiddush (aprox. 60 ml) tras la bendición — de al menos un revi'it de vino o jugo de uva en la copa. El jugo de uva cuenta como vino para muchos poskim.",
        "Maleh lugmov consiste à boire une joue pleine de la coupe de Kiddush (env. 60 ml) après la bénédiction — d'au moins un revi'it de vin ou jus de raisin dans la coupe. Le jus de raisin compte comme du vin pour beaucoup de poskim.",
        "Maleh lugmov — vypit polnye shcheki iz Kiddush-kubka (okolo 60 ml) posle brakhi — iz ne menee revi'it vina ili vinogradnogo soka. Vinogradnyy sok schitaetsya vinom u mnogikh poskim.",
    ),
    "Chametz — Chametz is leavened grain product made from wheat, barley, rye, oats, or spelt. Bread, beer, and many processed foods can be chametz. On Pesach, owning, eating, or benefiting from chametz is forbidden.": T(
        "Chametz — Chametz hu ma'achal dagan chamutz me'chitah, se'orah, shifon, shibolet shu'al o kusmin. Lechem, shikur ve'rabim ma'achalim me'ukavim yecholim lihyot chametz. BePesach, achizat, achilah o hana'ah mechametz asura.",
        "Chametz — Chametz es producto de grano fermentado de trigo, cebada, centeno, avena o espelta. Pan, cerveza y muchos alimentos procesados pueden ser chametz. En Pesaj, poseer, comer o beneficiarse de chametz está prohibido.",
        "Chametz — Chametz est un produit de céréales levé de blé, orge, seigle, avoine ou épeautre. Pain, bière et beaucoup d'aliments transformés peuvent être chametz. À Pessah, posséder, manger ou profiter de chametz est interdit.",
        "Chametz — Chametz — kislyy zernovoy produkt iz pshenitsy, yachmenya, rzhi, ovsa ili polby. Khleb, pivo i mnogie obrabotannye produkty mogut byt chametz. Na Pesach vladenie, eda ili polza ot chametz zapreshcheny.",
    ),
    "Rosh Chodesh during Chanukah — if you recite Hallel, say Full Hallel at Shacharit (the Chanukah practice), not Half Hallel (Peninei Halakha 12-02-07). Optional for women — follow your minhag.\n\nTachanun is omitted.": T(
        "Rosh Chodesh beChanukah — im omrim Hallel, omru Full Hallel beShacharit (minhag Chanukah), lo Half Hallel (Peninei Halakha 12-02-07). Reshut le'nashim — lefi minhagchem.\n\nTachanun batel.",
        "Rosh Jodesh durante Janucá — si recitas Halel, di Halel completo en Shajarit (práctica de Janucá), no Medio Halel (Peninei Halakha 12-02-07). Opcional para mujeres — sigue tu minhag.\n\nTachanún se omite.",
        "Roch 'Hodesh pendant Hanoucca — si vous récitez Hallel, dites Hallel complet à Shacharit (pratique de Hanoucca), pas Demi-Hallel (Peninei Halakha 12-02-07). Optionnel pour les femmes — suivez votre minhag.\n\nTa'hanoun est omis.",
        "Rosh Chodesh vo vremya Chanukah — esli chitaete Hallel, govorete Polnyy Hallel na Shacharit (obychay Chanukah), ne Polu-Hallel (Peninei Halakha 12-02-07). Po zhelaniyu dlya zhenshchin — po vashemu minhag.\n\nTachanun propuskayetsya.",
    ),
    "Thank G-d for giving you the ability to smell your surroundings. Not only do you know when someone is baking cookies, but it can help you avoid danger. What if everything in the whole world smelled like cardboard?": T(
        "הודu לה' שנתן לכם את היכולת להריח את סביבתכם. לא רק שאתם יודעים כשמישהו אופה עוגיות, אלא שזה יכול לעזור להימנע מסכנה. מה אם הכל בעולם היה מריח כמו קarton?",
        "Agradece a D-s por darte la capacidad de oler tu entorno. No solo sabes cuándo alguien hornea galletas, sino que puede ayudarte a evitar peligros. ¿Y si todo el mundo oliera a cartón?",
        "Remerciez D. de vous avoir donné la capacité de sentir votre environnement. Vous savez quand quelqu'un fait des biscuits, et cela peut vous aider à éviter un danger. Et si tout sentait le carton ?",
        "Blagodarite V-ga za sposobnost chuvstvovat zapakhi vokrug. Vy ne tolko znaete, kogda pekut pechene, no eto pomogaet izbezhat opasnosti. Chto esli by vsyo v mire pakhlo kartonom?",
    ),
    "Tanach (Tanakh) is the Hebrew Bible in three parts: Torah (the five books), Nevi'im (Prophets), and Ketuvim (Writings). Reading Tanach in Hebrew or translation is Torah study; Psalms (Tehillim) are part of Ketuvim.": T(
        "Tanach (Tanakh) hu haMikra be'shalosh chlakim: Torah (chamesh chumashim), Nevi'im veKetuvim. Kriat Tanach be'Ivrit o b'targum hi limud Torah; Tehillim hem helek miKetuvim.",
        "Tanaj (Tanaj) es la Biblia hebrea en tres partes: Torá (los cinco libros), Nevi'im (Profetas) y Ketuvim (Escrituras). Leer Tanaj en hebreo o traducción es estudio de Torá; los Salmos (Tehillim) son parte de Ketuvim.",
        "Tanakh est la Bible hébraïque en trois parties : Torah (cinq livres), Nevi'im (Prophètes) et Ketuvim (Écrits). Lire le Tanakh en hébreu ou traduction est étude de Torah ; les Psaumes (Tehillim) font partie des Ketuvim.",
        "Tanakh — evreyskaya Bibliya v trekh chastyakh: Tora (pyat knig), Nevi'im (Proroki) i Ketuvim (Pisaniya). Chtenie Tanakh na ivrite ili v perevode — izuchenie Tory; Tehillim — chast Ketuvim.",
    ),
    "Tomorrow is erev $tomorrowChag — the Yom Tov–Shabbat rules below apply starting then. Use today (during the day) to prepare so you are not caught tonight or tomorrow without eruv tavshilin, flames, or food in place.": T(
        "Machar erev $tomorrowChag — chukei Yom Tov–Shabbat lemata chalim me'az. Hasivu et hayom (be'or hayom) le'hachana kedei shelo tippalelu ha'layla o machar bli eruv tavshilin, nerot o ochel ba'makom.",
        "Mañana es erev $tomorrowChag — las reglas de Yom Tov–Shabat abajo aplican desde entonces. Usa hoy (durante el día) para prepararte y no quedarte esta noche o mañana sin eruv tavshilin, llamas o comida lista.",
        "Demain c'est erev $tomorrowChag — les règles Yom Tov–Chabbat ci-dessous s'appliquent dès lors. Utilisez aujourd'hui (de jour) pour préparer afin de ne pas être pris ce soir ou demain sans erouv tavshilin, flammes ou nourriture en place.",
        "Zavtra erev $tomorrowChag — pravila Yom Tov–Shabbat nizhe deystvuyut s etogo momenta. Ispolzuyte segodnya (dnyom) dlya podgotovki, chtoby ne okazatsya vecherom ili zavtra bez eruv tavshilin, ognya ili edy.",
    ),
    "Add your own mitzvah! ✡ The Talmud teaches that each person has their own unique way of serving G-d. Think of a mitzvah that can bring more light into the world and submit it in the 'Add a Mitzvah' section of the app!": T(
        "הוסיפu mitzvah משלכם! ✡ התalmud melamed she'le'chol adam derech yechida le'avod et ha'Hashem. חשbu al mitzvah she'yechol le'havi or nוסף la'olam ve'shlichu ba'Add a Mitzvah' ba'app!",
        "¡Añade tu propia mitzvá! ✡ El Talmud enseña que cada persona tiene su forma única de servir a D-s. Piensa en una mitzvá que traiga más luz al mundo y envíala en la sección 'Add a Mitzvah' de la app.",
        "Ajoutez votre propre mitzvah ! ✡ Le Talmud enseigne que chaque personne a sa façon unique de servir D. Pensez à une mitzvah qui apporte plus de lumière au monde et soumettez-la dans la section « Add a Mitzvah » de l'app !",
        "Dobavte svoyu mitzvah! ✡ Talmud uchит, chto u kazhdogo svoy put sluzheniya V-gu. Pridumayte mitzvah, kotoraya prineset bolshe sveta, i otpravte v razdele 'Add a Mitzvah' v prilozhenii!",
    ),
    "Borei Minei Besamim — catch-all blessing on fragrant herbs, spices, flowers, fruit, etc. (often said as the spice blessing in Havdalah after Shabbat, though some will say a more specific blessing on pleasant fragrances)": T(
        "Borei Minei Besamim — bracha klalit al samim, tavlinim, perachim, pri ve'ch. (l'rov ne'emra ke'brachat ha'besamim baHavdalah achar Shabbat, af al pi she'yesh ha'omrim bracha meyuchedet yoter al rei'ach naim)",
        "Borei Minei Besamim — bendición general sobre hierbas aromáticas, especias, flores, fruta, etc. (a menudo dicha como bendición de especias en Havdalah tras Shabat, aunque algunos dicen una bendición más específica sobre fragancias agradables)",
        "Borei Minei Besamim — bénédiction générale sur herbes parfumées, épices, fleurs, fruits, etc. (souvent dite comme bénédiction des épices à Havdalah après Chabbat, bien que certains disent une bénédiction plus spécifique sur les parfums agréables)",
        "Borei Minei Besamim — obshchaya brakha na aromatnye travy, specii, tsvety, plody i t.d. (chasto kak brakha na specii v Havdalah posle Shabbat, khotya nekotorye govoryat bolee konkretnuyu brakhu na priyatnye zapakhi)",
    ),
    "Olam HaBa is the World to Come — the eternal reality of closeness to G-d after this life (Heaven). Mitzvot, Torah study, and acts of kindness build merits that the sages describe as unfathomable treasures in that world.": T(
        "Olam HaBa hu Olam HaBa — ha'metziut ha'nitzchit shel kirvat la'Hashem achar hachayim ha'eleh (Gan Eden). Mitzvot, limud Torah u'ma'asim tovim bonim zechuyot she'ha'chachamim mesaprim aleihen ke'otzrot bli shiur ba'olam hahu.",
        "Olam HaBa es el Mundo Venidero — la realidad eterna de cercanía a D-s tras esta vida (Cielo). Mitzvot, estudio de Torá y actos de bondad acumulan méritos que los sabios describen como tesoros inconmensurables en ese mundo.",
        "Olam HaBa est le Monde à Venir — la réalité éternelle de proximité avec D. après cette vie (Paradis). Mitzvot, étude de Torah et actes de bonté accumulent des mérites que les sages décrivent comme des trésors incommensurables dans ce monde.",
        "Olam HaBa — Mir gрядushchiy — vechnaya blizost k V-gu posle etoy zhizni (Ray). Mitzvot, izuchenie Tory i dobrye dela sozdayut zaslugi, kotorye mudretsy opisyvayut kak nepostizhimye sokrovishcha v tom mire.",
    ),
    "Pshat is the plain, straightforward meaning of a Torah verse — what the text says on its surface before deeper layers. Rashi on Chumash mainly explains pshat. Learning pshat is the foundation before midrash or Kabbalah.": T(
        "Pshat hu pshat ha'pshuta shel pasuk baTorah — mah ha'ktav omair ba'glishah lifnei shichvot amokot. Rashi al haChumash mefareshet ba'ikar pshat. Limud pshat hu ha'yessod lifnei midrash o Kabbalah.",
        "Pshat es el significado llano y directo de un versículo de Torá — lo que el texto dice en superficie antes de capas más profundas. Rashi en Jumash explica principalmente pshat. Aprender pshat es la base antes del midrash o la Cábala.",
        "Pshat est le sens simple et direct d'un verset de Torah — ce que le texte dit en surface avant les couches profondes. Rachi sur le 'Houmash explique surtout le pshat. Apprendre le pshat est la base avant midrash ou Kabbale.",
        "Pshat — prostoy, pryamoy smysl stikha Tory — chto tekst govorit na poverkhnosti do glubokikh sloev. Rashi na Humash obyasnyaet preimushchestvenno pshat. Izuchenie pshat — osnova pered midrashem ili Kabbala.",
    ),
    'Shir HaMaalot is Psalm 126 — "When G-d returns the captivity of Zion" — sung before bentching on Shabbat and festivals. It expresses joy in redemption and hope for complete return. Many know the tune from Shabbat tables.': T(
        'Shir HaMaalot hu Tehillim 126 — "B\'shuv Hashem et shivat Tzion" — sharim lifnei bentching beShabbat u\'vachagim. Hu me\'ir simcha bi\'geulah ve\'tikvah le\'shiva shlema. Rabim makirim et ha\'niggun mi\'shulchanot Shabbat.',
        'Shir HaMaalot es el Salmo 126 — "Cuando D-s haga volver a Sión" — cantado antes del bentching en Shabat y fiestas. Expresa alegría por la redención y esperanza de retorno completo. Muchos conocen la melodía de las mesas de Shabat.',
        'Shir HaMaalot est le Psaume 126 — « Quand D. fera revenir Sion » — chanté avant le bentching le Chabbat et les fêtes. Il exprime la joie de la rédemption et l\'espoir du retour complet. Beaucoup connaissent l\'air des tables de Chabbat.',
        'Shir HaMaalot — Psalom 126 — «Kogda V-s vernet plen Tziona» — poyetsya pered bentching v Shabbat i prazdniki. Vyrazhaet radost geuly i nadezhdu na polnoye vozvrashchenie. Mnogie znayut napev so shabbat-stolov.',
    ),
    "Borei pri ha'adamah is the blessing on produce that grows from the earth — potatoes, cucumbers, melons, and many berries. It covers foods that are not tree fruit. Water and plain beverages have their own blessings or none.": T(
        "Borei pri ha'adamah hi ha'bracha al pri ha'adamah — tapuchim, melafefonim, avatiach ve'rabim perot shatich. Hi mechasa al ma'achalim she'einam pri ha'etz. Mayim u'mashkim pshutim yesh lahem brachot acherot o ein.",
        "Borei pri ha'adamah es la bendición sobre productos que crecen de la tierra — papas, pepinos, melones y muchas bayas. Cubre alimentos que no son fruta de árbol. El agua y bebidas simples tienen sus propias bendiciones o ninguna.",
        "Borei pri ha'adamah est la bénédiction sur les produits qui poussent de la terre — pommes de terre, concombres, melons et beaucoup de baies. Elle couvre les aliments qui ne sont pas des fruits d'arbre. L'eau et les boissons simples ont leurs propres bénédictions ou aucune.",
        "Borei pri ha'adamah — brakha na produkty, rastushchie iz zemli — kartofel, ogurtsy, dyni i mnogie yagody. Pokryvaet produkty, ne yavlyayushchiesya plodami dereva. Voda i prostye napitki imeyut svoi brakhi ili ne imeyut.",
    ),
    "Yom Yerushalayim marks the reunification of Jerusalem in 1967, observed on 28 Iyar.\n\nMany communities recite Hallel and hold festive gatherings. As with Yom Ha'atzmaut, customs and halachic rulings vary — consult your rav.": T(
        "Yom Yerushalayim me'ayen et ichud Yerushalayim bi'1967, nitzav be-28 Iyar.\n\nKehillot rabot omrim Hallel ve'ochazot be'asifot chagigiyot. K'mo Yom Ha'atzmaut, minhagim u'psakim shonim — histakeletz ba'rav.",
        "Yom Yerushalayim conmemora la reunificación de Jerusalén en 1967, observado el 28 de Iyar.\n\nMuchas comunidades recitan Halel y celebran reuniones festivas. Como Yom Ha'atzmaut, las costumbres y decisiones halájicas varían — consulta a tu rav.",
        "Yom Yeroushalayim marque la réunification de Jérusalem en 1967, observé le 28 Iyar.\n\nBeaucoup de communautés récitent Hallel et tiennent des rassemblements festifs. Comme Yom Ha'atsmaout, coutumes et décisions halakhiques varient — consultez votre rav.",
        "Yom Yerushalayim otmechaet vossoedinenie Ierusalima v 1967, nablyudayetsya 28 Iyar.\n\nMnogie obshchiny chitayut Hallel i provodyat prazdnichnye sobraniya. Kak Yom Ha'atzmaut, obychai i psakim razlichayutsya — obratites k ravu.",
    ),
    "Tanach — Tanach (Tanakh) is the Hebrew Bible in three parts: Torah (the five books), Nevi'im (Prophets), and Ketuvim (Writings). Reading Tanach in Hebrew or translation is Torah study; Psalms (Tehillim) are part of Ketuvim.": T(
        "Tanach — Tanach (Tanakh) hu haMikra be'shalosh chlakim: Torah (chamesh chumashim), Nevi'im veKetuvim. Kriat Tanach be'Ivrit o b'targum hi limud Torah; Tehillim hem helek miKetuvim.",
        "Tanaj — Tanaj (Tanaj) es la Biblia hebrea en tres partes: Torá (los cinco libros), Nevi'im (Profetas) y Ketuvim (Escrituras). Leer Tanaj en hebreo o traducción es estudio de Torá; los Salmos (Tehillim) son parte de Ketuvim.",
        "Tanakh — Tanakh est la Bible hébraïque en trois parties : Torah (cinq livres), Nevi'im (Prophètes) et Ketuvim (Écrits). Lire le Tanakh en hébreu ou traduction est étude de Torah ; les Psaumes (Tehillim) font partie des Ketuvim.",
        "Tanakh — Tanakh — evreyskaya Bibliya v trekh chastyakh: Tora (pyat knig), Nevi'im (Proroki) i Ketuvim (Pisaniya). Chtenie Tanakh na ivrite ili v perevode — izuchenie Tory; Tehillim — chast Ketuvim.",
    ),
    "Make every day special! Take a moment to think about the upcoming Shabbat 🌅. Maybe plan a special dish you'll make, or imagine the peaceful atmosphere you'll create. Did you know? It's a mitzvah to remember Shabbat every day!": T(
        "עשu כל יom מיוחד! קחu רגע לחשוב על Shabbat הקרוב 🌅. אולי תכננu מנה מיוחדת או דמיינu את האווירה השלווה. ידעתם? זו mitzvah לזכור את Shabbat בכל יom!",
        "¡Haz especial cada día! Tómate un momento para pensar en el próximo Shabat 🌅. Quizá planifica un plato especial o imagina la atmósfera pacífica que crearás. ¿Sabías? ¡Es mitzvá recordar Shabat cada día!",
        "Rendez chaque jour spécial ! Prenez un moment pour penser au prochain Chabbat 🌅. Planifiez peut-être un plat spécial ou imaginez l'atmosphère paisible que vous créerez. Le saviez-vous ? C'est une mitzvah de se souvenir de Chabbat chaque jour !",
        "Delайte kazhdyy den osobennym! Udelyte minutu predstoyashchemu Shabbat 🌅. Mozhet, zaplaniruyte osobennoe blyudo ili predstavte mirnuyu atmosferu. Znali li? Eto mitzvah pomnit Shabbat kazhdyy den!",
    ),
    "Marvel at the miracle of breath! Notice how G-d designed our bodies to breathe automatically 🫁, giving us life with almost no effort. Each breath is a gift - try taking one deep, mindful breath right now and thank G-d for it!": T(
        "התפעלu מניס הneshimah! שימu לב איך ה' תכנן את גופנו לנשום אוטומטית 🫁, ונותן לנו חיים כמעט ללא מאמץ. כל נשימה היא מתנה — נסu נשימה עמוקה ומודעת עכשיו והודu לה'!",
        "¡Maravíllate con el milagro de la respiración! Observa cómo D-s diseñó nuestros cuerpos para respirar automáticamente 🫁, dándonos vida casi sin esfuerzo. Cada respiración es un regalo — ¡toma una respiración profunda y consciente ahora y agradece a D-s!",
        "Émerveillez-vous du miracle de la respiration ! Remarquez comment D. a conçu nos corps pour respirer automatiquement 🫁, nous donnant la vie presque sans effort. Chaque souffle est un cadeau — prenez une respiration profonde et consciente maintenant et remerciez D. !",
        "Udivites chudu dykhaniya! Zametьte, kak V-s sozdal nashi tela dlya avtomaticheskogo dykhaniya 🫁, daruya zhizn pochti bez usiliy. Kazhdoe dykhanie — dar — sdelayte glubokiy osозnannyy vдох seychas i poblagodarite V-ga!",
    ),
    "The Zohar is the classic work of Kabbalah, attributed to Rabbi Shimon bar Yochai, written in Aramaic. It illuminates the Torah's inner dimensions. Many study a daily snippet; full mastery requires Hebrew/Aramaic and guidance.": T(
        "HaZohar hu sefer haKabbalah ha'klasi, minuy le'Rabbi Shimon bar Yochai, katuv be'Aramit. Hu me'ir et pnimiyut haTorah. Rabim lomdim ktzat yom yom; shleimut doreshet Ivrit/Aramit ve'hadracha.",
        "El Zóhar es la obra clásica de la Cábala, atribuida al Rabí Shimón bar Yojai, escrita en arameo. Ilumina las dimensiones internas de la Torá. Muchos estudian un fragmento diario; el dominio pleno requiere hebreo/aramaico y guía.",
        "Le Zohar est l'œuvre classique de la Kabbale, attribuée au Rabbi Shimon bar Yo'haï, écrite en araméen. Il éclaire les dimensions intérieures de la Torah. Beaucoup étudient un extrait quotidien ; la maîtrise complète exige hébreu/araméen et guidance.",
        "Zohar — klassicheskoe proizvedenie Kabbaly, prpisыvaemoe Rabbenu Shimonu bar Yochai, napisano na arameyskom. Osveshchaet vnutrennie izmereniya Tory. Mnogie uchат ezhednevnyy otryvok; polnoe osvoenie trebuet ivrit/ arameyskogo i nastavnika.",
    ),
    "maleh lugmov — Maleh lugmov is drinking a cheekful from the Kiddush cup (approx 2 fl oz) after the blessing — from at least a total of a revi'it of wine or grape juice in the cup. Grape juice counts like wine for many poskim.": T(
        "maleh lugmov — Maleh lugmov hu shtiyah maleh lugma mi'kos haKiddush (b'kiruv 60 ml) achar ha'bracha — mi'klal revi'it yain o mishmash anavim ba'kos. Mishmash anavim nechshav keyayin le'rabim mi'ha'poskim.",
        "maleh lugmov — Maleh lugmov es beber un puñado de la copa de Kiddush (aprox. 60 ml) tras la bendición — de al menos un revi'it de vino o jugo de uva en la copa. El jugo de uva cuenta como vino para muchos poskim.",
        "maleh lugmov — Maleh lugmov consiste à boire une joue pleine de la coupe de Kiddush (env. 60 ml) après la bénédiction — d'au moins un revi'it de vin ou jus de raisin dans la coupe. Le jus de raisin compte comme du vin pour beaucoup de poskim.",
        "maleh lugmov — Maleh lugmov — vypit polnye shcheki iz Kiddush-kubka (okolo 60 ml) posle brakhi — iz ne menee revi'it vina ili vinogradnogo soka. Vinogradnyy sok schitaetsya vinom u mnogikh poskim.",
    ),
    "Do the powerful mitzvah of praying for others! Think of someone who's looking for their soulmate (shidduch) and say a heartfelt prayer for them 💑. Your sincere prayer could be the key that opens the gates of blessing for them!": T(
        "עשu את mitzvah העוצמתי של התפלל עבור אחרים! חשbu על מישהו שמחפש shidduch והתפללu עבורו בכנות 💑. התפילה הכנה שלכם יכולה להיות המפתח שפותח שערי bracha עבורם!",
        "¡Haz la poderosa mitzvá de orar por otros! Piensa en alguien que busca su alma gemela (shidduj) y ora sinceramente por ellos 💑. ¡Tu oración sincera podría ser la llave que abra las puertas de la bendición para ellos!",
        "Accomplissez la mitzvah puissante de prier pour les autres ! Pensez à quelqu'un qui cherche son âme sœur (shidou'h) et priez sincèrement pour lui 💑. Votre prière sincère pourrait ouvrir les portes de la bénédiction pour lui !",
        "Vypolnite silnuyu mitzvah molitvy za drugikh! Vspomnite kogo-to, kto ishchет zyuzhnuyu polovinku (shidduch), i iskrenne pomolites za nego 💑. Vasha iskrennyaya molitva mozhet otkryt vorota brakhi dlya nego!",
    ),
    "Treif means non-kosher — originally torn flesh of improperly slaughtered animals, now any forbidden food. Mixing treif into a kosher kitchen can require kashering. \"Kosher style\" restaurants without supervision are not kosher.": T(
        "Treif mashma lo kasher — b'mekor nevelah shel behema she'nishchat shelo k'hilchata, hayom kol ma'achal asur. Erev treif ba'mitbach kasher yechol le'drosh hashgacha/kashering. \"Kosher style\" bli hashgacha ein kasher.",
        "Treif significa no kosher — originalmente carne desgarrada de animales mal sacrificados, ahora cualquier alimento prohibido. Mezclar treif en una cocina kosher puede requerir kashering. Restaurantes \"estilo kosher\" sin supervisión no son kosher.",
        "Treif signifie non cacher — à l'origine chair déchirée d'animaux mal abattus, aujourd'hui tout aliment interdit. Mélanger du treif dans une cuisine casher peut exiger un kashering. Les restaurants « style casher » sans supervision ne sont pas casher.",
        "Treif oznachaet ne kosher — iznachalno rvanoye myaso nepravilno zabitykh zhivotnykh, seychas lyubaya zapreshchennaya eda. Smeshivanie treif v koshernoy kukhne mozhet trebovat kashering. Restorany «kosher style» bez nadzora ne kosher.",
    ),
    "pshat — Pshat is the plain, straightforward meaning of a Torah verse — what the text says on its surface before deeper layers. Rashi on Chumash mainly explains pshat. Learning pshat is the foundation before midrash or Kabbalah.": T(
        "pshat — Pshat hu pshat ha'pshuta shel pasuk baTorah — mah ha'ktav omair ba'glishah lifnei shichvot amokot. Rashi al haChumash mefareshet ba'ikar pshat. Limud pshat hu ha'yessod lifnei midrash o Kabbalah.",
        "pshat — Pshat es el significado llano y directo de un versículo de Torá — lo que el texto dice en superficie antes de capas más profundas. Rashi en Jumash explica principalmente pshat. Aprender pshat es la base antes del midrash o la Cábala.",
        "pshat — Pshat est le sens simple et direct d'un verset de Torah — ce que le texte dit en surface avant les couches profondes. Rachi sur le 'Houmash explique surtout le pshat. Apprendre le pshat est la base avant midrash ou Kabbale.",
        "pshat — Pshat — prostoy, pryamoy smysl stikha Tory — chto tekst govorit na poverkhnosti do glubokikh sloev. Rashi na Humash obyasnyaet preimushchestvenno pshat. Izuchenie pshat — osnova pered midrashem ili Kabbala.",
    ),
    'Yom Tov (literally "good day") is a biblical festival such as Pesach, Shavuot, Sukkot, or Rosh Hashana. It resembles Shabbat in joy and in restricting melacha, but cooking and carrying are generally permitted for festival needs.': T(
        "Yom Tov (mashma \"yom tov\") hu chag miKra k'Pesach, Shavuot, Sukkot o Rosh Hashana. Domeh leShabbat ba'simcha u'v'issur melacha, aval bishul ve'hotza'ah mutarim le'tzorkhei chag.",
        'Yom Tov (literalmente "día bueno") es una festividad bíblica como Pesaj, Shavuot, Sucot o Rosh Hashaná. Se parece a Shabat en alegría y restricción de melajá, pero cocinar y transportar generalmente están permitidos para necesidades festivas.',
        'Yom Tov (littéralement « bon jour ») est une fête biblique comme Pessah, Chavouot, Souccot ou Roch Hachana. Il ressemble à Chabbat par la joie et la restriction de melakha, mais cuisiner et porter sont généralement permis pour les besoins de la fête.',
        'Yom Tov (doslovno «horoshiy den») — bibliyskiy prazdnik, kak Pesach, Shavuot, Sukkot ili Rosh Hashana. Pohozh na Shabbat radostyu i zapretom melacha, no gotovka i perenos obychno razreshены dlya nuzhd prazdnika.',
    ),
    "Olam HaBa — Olam HaBa is the World to Come — the eternal reality of closeness to G-d after this life (Heaven). Mitzvot, Torah study, and acts of kindness build merits that the sages describe as unfathomable treasures in that world.": T(
        "Olam HaBa — Olam HaBa hu Olam HaBa — ha'metziut ha'nitzchit shel kirvat la'Hashem achar hachayim ha'eleh (Gan Eden). Mitzvot, limud Torah u'ma'asim tovim bonim zechuyot she'ha'chachamim mesaprim aleihen ke'otzrot bli shiur ba'olam hahu.",
        "Olam HaBa — Olam HaBa es el Mundo Venidero — la realidad eterna de cercanía a D-s tras esta vida (Cielo). Mitzvot, estudio de Torá y actos de bondad acumulan méritos que los sabios describen como tesoros inconmensurables en ese mundo.",
        "Olam HaBa — Olam HaBa est le Monde à Venir — la réalité éternelle de proximité avec D. après cette vie (Paradis). Mitzvot, étude de Torah et actes de bonté accumulent des mérites que les sages décrivent comme des trésors incommensurables dans ce monde.",
        "Olam HaBa — Olam HaBa — Mir gрядushchiy — vechnaya blizost k V-gu posle etoy zhizni (Ray). Mitzvot, izuchenie Tory i dobrye dela sozdayut zaslugi, kotorye mudretsy opisyvayut kak nepostizhimye sokrovishcha v tom mire.",
    ),
    "Ketuvim (Writings) is the third section of Tanach — Psalms (Tehillim), Proverbs, Esther, Ruth, Daniel, and others. It includes poetry, history, and wisdom literature. Tehillim is the most commonly recited book for comfort and praise.": T(
        "Ketuvim (Ketuvim) hu helek ha'shelishi shel Tanach — Tehillim, Mishlei, Esther, Rut, Daniel ve'acharim. Hu koleh shira, historia ve'chochmah. Tehillim hu ha'sefer ha'ne'emar yoter le'nechama u'le'hallel.",
        "Ketuvim (Escrituras) es la tercera sección del Tanaj — Salmos (Tehillim), Proverbios, Ester, Rut, Daniel y otros. Incluye poesía, historia y literatura de sabiduría. Tehillim es el libro más recitado para consuelo y alabanza.",
        "Ketuvim (Écrits) est la troisième section du Tanakh — Psaumes (Tehillim), Proverbes, Esther, Ruth, Daniel et autres. Elle comprend poésie, histoire et sagesse. Tehillim est le livre le plus récité pour réconfort et louange.",
        "Ketuvim (Pisaniya) — tretya chast Tanakh — Tehillim, Mishlei, Ester, Rut, Daniel i drugie. Vklyuchaet poeziyu, istoriyu i mudrost. Tehillim — kniga, chitaemaya chashche vsego dlya utesheniya i hvaly.",
    ),
    "Zohar — The Zohar is the classic work of Kabbalah, attributed to Rabbi Shimon bar Yochai, written in Aramaic. It illuminates the Torah's inner dimensions. Many study a daily snippet; full mastery requires Hebrew/Aramaic and guidance.": T(
        "Zohar — HaZohar hu sefer haKabbalah ha'klasi, minuy le'Rabbi Shimon bar Yochai, katuv be'Aramit. Hu me'ir et pnimiyut haTorah. Rabim lomdim ktzat yom yom; shleimut doreshet Ivrit/Aramit ve'hadracha.",
        "Zóhar — El Zóhar es la obra clásica de la Cábala, atribuida al Rabí Shimón bar Yojai, escrita en arameo. Ilumina las dimensiones internas de la Torá. Muchos estudian un fragmento diario; el dominio pleno requiere hebreo/aramaico y guía.",
        "Zohar — Le Zohar est l'œuvre classique de la Kabbale, attribuée au Rabbi Shimon bar Yo'haï, écrite en araméen. Il éclaire les dimensions intérieures de la Torah. Beaucoup étudient un extrait quotidien ; la maîtrise complète exige hébreu/araméen et guidance.",
        "Zohar — Zohar — klassicheskoe proizvedenie Kabbaly, prpisыvaemoe Rabbenu Shimonu bar Yochai, napisano na arameyskom. Osveshchaet vnutrennie izmereniya Tory. Mnogie uchат ezhednevnyy otryvok; polnoe osvoenie trebuet ivrita/ arameyskogo i nastavnika.",
    ),
    "Peninei Halakha is Rabbi Eliezer Melamed's modern Hebrew halacha series (available in English online), organized by topic — Shabbat, festivals, prayer, family, and Israel. It is clear, sourced, and widely used for practical questions.": T(
        "Peninei Halakha hu seder halacha b'Ivrit moderna shel haRav Eliezer Melamed (zamin gam be'Anglit ba'internet), me'urgan le'fi nusha — Shabbat, chagim, tefillah, mishpacha veYisrael. Barur, me'uyan ve'nifutz le'she'elot ma'asiyot.",
        "Peninei Halakha es la serie moderna de halajá en hebreo del Rabí Eliezer Melamed (disponible en inglés en línea), organizada por tema — Shabat, fiestas, oración, familia e Israel. Es clara, con fuentes y muy usada para preguntas prácticas.",
        "Peninei Halakha est la série moderne de halakha en hébreu du Rav Eliezer Melamed (disponible en anglais en ligne), organisée par sujet — Chabbat, fêtes, prière, famille et Israël. Claire, sourcée et largement utilisée pour les questions pratiques.",
        "Peninei Halakha — sovremennaya ivritskaya seriya halakhi Rava Eliezera Melameda (dostupna na angliyskom onlayn), po temam — Shabbat, prazdniki, molitva, semya i Izrail. Yasnaya, s istochnikami, shiroko ispolzuetsya dlya prakticheskikh voprosov.",
    ),
    "treif — Treif means non-kosher — originally torn flesh of improperly slaughtered animals, now any forbidden food. Mixing treif into a kosher kitchen can require kashering. \"Kosher style\" restaurants without supervision are not kosher.": T(
        "treif — Treif mashma lo kasher — b'mekor nevelah shel behema she'nishchat shelo k'hilchata, hayom kol ma'achal asur. Erev treif ba'mitbach kasher yechol le'drosh hashgacha/kashering. \"Kosher style\" bli hashgacha ein kasher.",
        "treif — Treif significa no kosher — originalmente carne desgarrada de animales mal sacrificados, ahora cualquier alimento prohibido. Mezclar treif en una cocina kosher puede requerir kashering. Restaurantes \"estilo kosher\" sin supervisión no son kosher.",
        "treif — Treif signifie non cacher — à l'origine chair déchirée d'animaux mal abattus, aujourd'hui tout aliment interdit. Mélanger du treif dans une cuisine casher peut exiger un kashering. Les restaurants « style casher » sans supervision ne sont pas casher.",
        "treif — Treif oznachaet ne kosher — iznachalno rvanoye myaso nepravilno zabitykh zhivotnykh, seychas lyubaya zapreshchennaya eda. Smeshivanie treif v koshernoy kukhne mozhet trebovat kashering. Restorany «kosher style» bez nadzora ne kosher.",
    ),
    "The Baal Shem Tov (Rabbi Israel ben Eliezer, c. 1700–1760) founded the Chasidic movement, teaching that every Jew can serve G-d with joy and that divine presence fills all creation. Chabad traces its spiritual lineage to his disciples.": T(
        "HaBaal Shem Tov (Rabbi Yisrael ben Eliezer, c. 1700–1760) yisad et tnu'at haChasidut, melamed she'kol Yehudi yachol la'avod et ha'Hashem be'simcha ve'she'hashra'at haShechinah mamla et kol ha'beriah. Chabad omeket et sharshelta ha'ruchanit le'talmidav.",
        "El Baal Shem Tov (Rabí Israel ben Eliezer, c. 1700–1760) fundó el movimiento jasídico, enseñando que todo judío puede servir a D-s con alegría y que la presencia divina llena toda la creación. Jabad remonta su linaje espiritual a sus discípulos.",
        "Le Baal Shem Tov (Rabbi Israel ben Eliezer, c. 1700–1760) a fondé le mouvement hassidique, enseignant que tout Juif peut servir D. avec joie et que la présence divine remplit toute la création. Habad trace sa lignée spirituelle à ses disciples.",
        "Baal Shem Tov (Rav Yisrael ben Eliezer, ok. 1700–1760) osnoval khasidskoe dvizhenie, uchа ya, chto kazhdyy evrey mozhet sluzhit V-gu s radostyu i chto bozhestvennoe prisutstvie napolnyaet vse tvorenie. Chabad vedet dukhovnuyu liniyu k ego uchenikam.",
    ),
    'Shir HaMaalot — Shir HaMaalot is Psalm 126 — "When G-d returns the captivity of Zion" — sung before bentching on Shabbat and festivals. It expresses joy in redemption and hope for complete return. Many know the tune from Shabbat tables.': T(
        'Shir HaMaalot — Shir HaMaalot hu Tehillim 126 — "B\'shuv Hashem et shivat Tzion" — sharim lifnei bentching beShabbat u\'vachagim. Hu me\'ir simcha bi\'geulah ve\'tikvah le\'shiva shlema. Rabim makirim et ha\'niggun mi\'shulchanot Shabbat.',
        'Shir HaMaalot — Shir HaMaalot es el Salmo 126 — "Cuando D-s haga volver a Sión" — cantado antes del bentching en Shabat y fiestas. Expresa alegría por la redención y esperanza de retorno completo. Muchos conocen la melodía de las mesas de Shabat.',
        'Shir HaMaalot — Shir HaMaalot est le Psaume 126 — « Quand D. fera revenir Sion » — chanté avant le bentching le Chabbat et les fêtes. Il exprime la joie de la rédemption et l\'espoir du retour complet. Beaucoup connaissent l\'air des tables de Chabbat.',
        'Shir HaMaalot — Shir HaMaalot — Psalom 126 — «Kogda V-s vernet plen Tziona» — poyetsya pered bentching v Shabbat i prazdniki. Vyrazhaet radost geuly i nadezhdu na polnoye vozvrashchenie. Mnogie znayut napev so shabbat-stolov.',
    ),
    "Mashiach (Moshiach) is the righteous anointed king from David's line who will rebuild the Temple, gather the exiles, and bring universal knowledge of G-d. Belief in his coming is a foundation of Jewish faith. We pray for redemption daily.": T(
        "Mashiach (Moshiach) hu melech tzadik m'shuch m'zera David she'yivneh et haMikdash, yekabetz et ha'gola ve'yavi da'at et ha'Hashem la'olam kulo. Emunah bi'vo'o hi yesod ha'emunah ha'Yehudit. Mitpalelim le'geulah yom yom.",
        "Mashiach (Moshiaj) es el rey justo ungido del linaje de David que reconstruirá el Templo, reunirá a los exiliados y traerá conocimiento universal de D-s. Creer en su venida es fundamento de la fe judía. Oramos por la redención diariamente.",
        "Machiach (Moshiach) est le roi juste oint de la lignée de David qui reconstruira le Temple, rassemblera les exilés et apportera la connaissance universelle de D. Croire en sa venue est un fondement de la foi juive. Nous prions pour la rédemption chaque jour.",
        "Mashiach (Moshiach) — pravednyy pomazannyy tsar iz roda David, kotoryy vosstanovit Khram, soberet izgnannikov i prineset vsемirnoye poznanie V-ga. Vera v ego prikhod — osnova evreyskoy very. My molimsya o geule ezhednevno.",
    ),
    'Yom Tov — Yom Tov (literally "good day") is a biblical festival such as Pesach, Shavuot, Sukkot, or Rosh Hashana. It resembles Shabbat in joy and in restricting melacha, but cooking and carrying are generally permitted for festival needs.': T(
        'Yom Tov — Yom Tov (mashma "yom tov") hu chag miKra k\'Pesach, Shavuot, Sukkot o Rosh Hashana. Domeh leShabbat ba\'simcha u\'v\'issur melacha, aval bishul ve\'hotza\'ah mutarim le\'tzorkhei chag.',
        'Yom Tov — Yom Tov (literalmente "día bueno") es una festividad bíblica como Pesaj, Shavuot, Sucot o Rosh Hashaná. Se parece a Shabat en alegría y restricción de melajá, pero cocinar y transportar generalmente están permitidos para necesidades festivas.',
        'Yom Tov — Yom Tov (littéralement « bon jour ») est une fête biblique comme Pessah, Chavouot, Souccot ou Roch Hachana. Il ressemble à Chabbat par la joie et la restriction de melakha, mais cuisiner et porter sont généralement permis pour les besoins de la fête.',
        'Yom Tov — Yom Tov (doslovno «horoshiy den») — bibliyskiy prazdnik, kak Pesach, Shavuot, Sukkot ili Rosh Hashana. Pohozh na Shabbat radostyu i zapretom melacha, no gotovka i perenos obychno razreshены dlya nuzhd prazdnika.',
    ),
    "A keli is a vessel, utensil, or tool. It generally refers to any manufactured object or receptacle that holds, processes, or contains something. A keli is used for netilat yadayim — a cup, bottle, or washing cup (usually) with two handles.": T(
        "Keli hu keli, kli o kelim. Hu l'rov me'ayen kol davar meyuchad o makhlit she'machzik, me'ayen o mekabel davar. Keli meshamshim le'netilat yadayim — kos, bakbuk o kos netilah (l'rov) im shnei yadot.",
        "Un keli es un recipiente, utensilio o herramienta. Generalmente se refiere a cualquier objeto fabricado o receptáculo que sostiene, procesa o contiene algo. Un keli se usa para netilat yadayim — una copa, botella o jarrilla (usualmente) con dos asas.",
        "Un keli est un récipient, ustensile ou outil. Il désigne généralement tout objet fabriqué ou réceptacle qui contient ou traite quelque chose. Un keli sert au netilat yadayim — une coupe, bouteille ou gobelet (souvent) à deux anses.",
        "Keli — sosud, prinadlezhnost ili instrument. Obychno lyuboy izgotovlennyy predmet ili emkost, kotoraya derzhit ili soderzhit chto-to. Keli ispolzuetsya dlya netilat yadayim — kubok, butylka ili kovsh (обыchno) s dvumya ruchkami.",
    ),
    "Borei Pri HaAdamah — Borei pri ha'adamah is the blessing on produce that grows from the earth — potatoes, cucumbers, melons, and many berries. It covers foods that are not tree fruit. Water and plain beverages have their own blessings or none.": T(
        "Borei Pri HaAdamah — Borei pri ha'adamah hi ha'bracha al pri ha'adamah — tapuchim, melafefonim, avatiach ve'rabim perot shatich. Hi mechasa al ma'achalim she'einam pri ha'etz. Mayim u'mashkim pshutim yesh lahem brachot acherot o ein.",
        "Borei Pri HaAdamah — Borei pri ha'adamah es la bendición sobre productos que crecen de la tierra — papas, pepinos, melones y muchas bayas. Cubre alimentos que no son fruta de árbol. El agua y bebidas simples tienen sus propias bendiciones o ninguna.",
        "Borei Pri HaAdamah — Borei pri ha'adamah est la bénédiction sur les produits qui poussent de la terre — pommes de terre, concombres, melons et beaucoup de baies. Elle couvre les aliments qui ne sont pas des fruits d'arbre. L'eau et les boissons simples ont leurs propres bénédictions ou aucune.",
        "Borei Pri HaAdamah — Borei pri ha'adamah — brakha na produkty, rastushchie iz zemli — kartofel, ogurtsy, dyni i mnogie yagody. Pokryvaet produkty, ne yavlyayushchiesya plodami dereva. Voda i prostye napitki imeyut svoi brakhi ili ne imeyut.",
    ),
    "Daven (davening) means to pray. You 'daven' the daily services (Shacharit, Mincha, Maariv). The word is Yiddish-derived Ashkenazi slang but widely understood. People often say \"daven Shacharit\" for a service and \"daven\" for praying in general.": T(
        "Daven (davening) mashma le'hitpalel. 'Daven' et hatefillot hayom yom (Shacharit, Mincha, Maariv). Ha'milah hi slang Ashkenazi miYiddish aval me'uvetet. Omrim 'daven Shacharit' le'tefillah u'daven' le'hitpallel b'chlal.",
        "Daven (davening) significa orar. Se 'daven' los servicios diarios (Shajarit, Minjá, Maariv). La palabra es jerga ashkenazí del yiddish pero ampliamente entendida. Se dice \"daven Shacharit\" para un servicio y \"daven\" para orar en general.",
        "Daven (davening) signifie prier. On « daven » les offices quotidiens (Shacharit, Min'ha, Maariv). Le mot est un argot ashkénaze du yiddish mais largement compris. On dit « daven Shacharit » pour un office et « daven » pour prier en général.",
        "Daven (davening) oznachaet molitsya. 'Daven' — ezhednevnye sluzhby (Shacharit, Mincha, Maariv). Slovo iz ashkenazskogo zhargona na idishe, no shiroko ponimaetsya. Govoryat «daven Shacharit» o sluzhbe i «daven» o molitve voobshche.",
    ),
    "Ketuvim — Ketuvim (Writings) is the third section of Tanach — Psalms (Tehillim), Proverbs, Esther, Ruth, Daniel, and others. It includes poetry, history, and wisdom literature. Tehillim is the most commonly recited book for comfort and praise.": T(
        "Ketuvim — Ketuvim (Ketuvim) hu helek ha'shelishi shel Tanach — Tehillim, Mishlei, Esther, Rut, Daniel ve'acharim. Hu koleh shira, historia ve'chochmah. Tehillim hu ha'sefer ha'ne'emar yoter le'nechama u'le'hallel.",
        "Ketuvim — Ketuvim (Escrituras) es la tercera sección del Tanaj — Salmos (Tehillim), Proverbios, Ester, Rut, Daniel y otros. Incluye poesía, historia y literatura de sabiduría. Tehillim es el libro más recitado para consuelo y alabanza.",
        "Ketuvim — Ketuvim (Écrits) est la troisième section du Tanakh — Psaumes (Tehillim), Proverbes, Esther, Ruth, Daniel et autres. Elle comprend poésie, histoire et sagesse. Tehillim est le livre le plus récité pour réconfort et louange.",
        "Ketuvim — Ketuvim (Pisaniya) — tretya chast Tanakh — Tehillim, Mishlei, Ester, Rut, Daniel i drugie. Vklyuchaet poeziyu, istoriyu i mudrost. Tehillim — kniga, chitaemaya chashche vsego dlya utesheniya i hvaly.",
    ),
    "Chazeret is the second bitter herb on the Seder plate in some traditions — often romaine lettuce stalk — alongside maror. Not every community uses a separate chazeret; some use only horseradish for maror. Follow your Haggadah and family custom.": T(
        "Chazeret hu ha'maror ha'sheni ba'ke'arah baSeder b'chlk minhagim — l'rov chazeret chasah — yachad im maror. Lo kol kehillah meshameshet chazeret nifrad; yesh ha'omrim rak chrain le'maror. L'fi haHaggadah u'minhag ha'mishpacha.",
        "Chazeret es la segunda hierba amarga en el plato del Seder en algunas tradiciones — a menudo lechuga romana — junto al maror. No toda comunidad usa chazeret separado; algunos usan solo rábano picante para maror. Sigue tu Hagadá y costumbre familiar.",
        "Chazeret est la deuxième herbe amère sur l'assiette du Seder dans certaines traditions — souvent une tige de laitue romaine — aux côtés du maror. Toutes les communautés n'utilisent pas un chazeret séparé ; certains n'utilisent que du raifort pour le maror. Suivez votre Hagada et coutume familiale.",
        "Chazeret — vtoraya gorkaya trava na seder-tarelke v nekotorykh traditsiyakh — chasto salat romen — ryadom s maror. Ne vse obshchiny ispolzuyut otdelnyy chazeret; nekotorye tolko khren dlya maror. Sleduyte Hagade i semeynomu obychayu.",
    ),
    "Keli — A keli is a vessel, utensil, or tool. It generally refers to any manufactured object or receptacle that holds, processes, or contains something. A keli is used for netilat yadayim — a cup, bottle, or washing cup (usually) with two handles.": T(
        "Keli — Keli hu keli, kli o kelim. Hu l'rov me'ayen kol davar meyuchad o makhlit she'machzik, me'ayen o mekabel davar. Keli meshamshim le'netilat yadayim — kos, bakbuk o kos netilah (l'rov) im shnei yadot.",
        "Keli — Un keli es un recipiente, utensilio o herramienta. Generalmente se refiere a cualquier objeto fabricado o receptáculo que sostiene, procesa o contiene algo. Un keli se usa para netilat yadayim — una copa, botella o jarrilla (usualmente) con dos asas.",
        "Keli — Un keli est un récipient, ustensile ou outil. Il désigne généralement tout objet fabriqué ou réceptacle qui contient ou traite quelque chose. Un keli sert au netilat yadayim — une coupe, bouteille ou gobelet (souvent) à deux anses.",
        "Keli — Keli — sosud, prinadlezhnost ili instrument. Obychno lyuboy izgotovlennyy predmet ili emkost. Keli ispolzuetsya dlya netilat yadayim — kubok, butylka ili kovsh (обыchno) s dvumya ruchkami.",
    ),
    "Leining is chanting the Torah portion from the scroll with precise trop (cantillation). The baal koreh trains for months. Congregants follow in a Chumash. Hearing every word on Shabbat morning fulfills the communal mitzvah of public Torah reading.": T(
        "Leining hu kriat haparsha min haSefer Torah im trop m'duyak. Ha'baal koreh mit'amen chodshim. Ha'tzibur omdim im Chumash. Shmi'at kol milah beShabbat baboker mekayemet mitzvat kriat haTorah ba'tzibur.",
        "Leining es cantar la porción de Torá del rollo con trop preciso (cantilación). El baal kore entrena meses. Los feligreses siguen en un Jumash. Escuchar cada palabra el Shabat por la mañana cumple la mitzvá comunitaria de lectura pública de Torá.",
        "Leining consiste à lire la portion de Torah du rouleau avec un trop précis (cantillation). Le baal koré s'entraîne des mois. Les fidèles suivent dans un 'Houmash. Entendre chaque mot le Chabbat matin accomplit la mitzvah communautaire de lecture publique de Torah.",
        "Leining — chtenie parshi iz svitka s tochnym trop (kantillyatsiey). Baal koreh treniruetsya mesyatsami. Molящiesya sleduyut v Humash. Slushanie kazhdogo slova v Shabbat utrom vypolnyaet obshchinную mitzvah publichnogo chteniya Tory.",
    ),
    "Rosh Hashana is the Jewish New Year and Day of Judgment — shofar, festive meals, and solemn prayer. It begins the Ten Days of Teshuvah leading to Yom Kippur. Customs include apples in honey, new fruit, and tashlich. Work is forbidden like Yom Tov.": T(
        "Rosh Hashana hu Rosh Hashana u'Yom HaDin — shofar, seudot chagigiyot u'tefillah chamurah. Hu matchil et Aseret Yemei Teshuvah ad Yom Kippur. Minhagim: tapuach bidvash, pri chadash ve'tashlich. Melacha asura kmo Yom Tov.",
        "Rosh Hashaná es el Año Nuevo judío y Día del Juicio — shofar, comidas festivas y oración solemne. Comienza los Diez Días de Teshuvá hacia Yom Kipur. Costumbres: manzanas en miel, fruta nueva y tashlij. El trabajo está prohibido como Yom Tov.",
        "Roch Hachana est le Nouvel An juif et le Jour du Jugement — shofar, repas festifs et prière solennelle. Il commence les Dix Jours de Techouva menant à Yom Kippour. Coutumes : pommes au miel, fruit nouveau et tachlich. Le travail est interdit comme Yom Tov.",
        "Rosh Hashana — evreyskiy Novyy god i Sudnyy den — shofar, prazdnichnye трапезы i torzhestvennaya molitva. Nachinaet Desyat dney teshuvy do Yom Kippur. Obychai: yabloki s medom, novyy plod i tashlich. Trud zapreshchen kak Yom Tov.",
    ),
    "A bentcher (birkon) is a small booklet with Birkat Hamazon and sometimes Shabbat songs and benching additions. Keeping one at the table makes bentching after bread meals easy. Many are bilingual; some include zimun instructions and festival inserts.": T(
        "Bentcher (birkon) hu sefer katan im Birkat Hamazon ve'l'f'amim shirim leShabbat vetosafot lebentching. Le'hash'ir oto al ha'shulchan makhel et ha'bentching achar seudat lechem. Rabim dvei lashon; yesh im hanchayot zimun vetosafot lechagim.",
        "Un bentcher (birkon) es un librito con Birkat Hamazon y a veces canciones de Shabat y adiciones de bentching. Tener uno en la mesa facilita el bentching tras comidas con pan. Muchos son bilingües; algunos incluyen instrucciones de zimun e inserciones festivas.",
        "Un bentcher (birkon) est un petit livret avec Birkat HaMazon et parfois des chants de Chabbat et additions de bentching. En garder un à table facilite le bentching après les repas avec pain. Beaucoup sont bilingues ; certains incluent instructions de zimoun et insertions de fêtes.",
        "Bentcher (birkon) — malenkaya knizhka s Birkat Hamazon i inogda shabbat-pesen i dobavleniyami k bentching. Derzhat na stole uproshchaet bentching posle khlebных трапез. Mnogie dvuyazychnye; nekotorye s instruktsiyami zimun i prazdnichnymi vstavkami.",
    ),
    'Chazal (חז"ל) is an acronym for "our Sages of blessed memory" — the rabbis of the Mishnah, Talmud, and midrash who transmitted halacha and values. When a source says "Chazal teach," it refers to this collective tradition rather than a single author.': T(
        'Chazal (חז"ל) hu roshei teivot le"chachameinu zikhronam livracha" — rabbanim shel haMishnah, haTalmud veha\'midrash she\'hעבירu halacha u\'mussar. K\'she\'ha\'mekor omair "Chazal omrim," hu ha\'masoret ha\'klalit ve\'lo mechaber yachid.',
        'Chazal (חז"ל) es un acrónimo de "nuestros Sabios de bendita memoria" — los rabinos del Mishná, Talmud y midrash que transmitieron halajá y valores. Cuando una fuente dice "Chazal enseñan," se refiere a esta tradición colectiva, no a un autor único.',
        'Chazal (חז"ל) est un acronyme pour « nos Sages que leur mémoire soit bénie » — les rabbins de la Michna, du Talmud et du midrash qui ont transmis halakha et valeurs. Quand une source dit « Chazal enseignent, » c\'est cette tradition collective, pas un auteur unique.',
        'Chazal (חז"ל) — akronim «nashi Mudretsy da budet blagoslovena ikh pamyat» — ravviny Mishny, Talmuda i midrasha, peredavshie halakhu i tsennosti. Kogda istochnik govorit «Chazal uchат,» eto kollektivnaya traditsiya, a ne odin avtor.',
    ),
    "Mashiach — Mashiach (Moshiach) is the righteous anointed king from David's line who will rebuild the Temple, gather the exiles, and bring universal knowledge of G-d. Belief in his coming is a foundation of Jewish faith. We pray for redemption daily.": T(
        "Mashiach — Mashiach (Moshiach) hu melech tzadik m'shuch m'zera David she'yivneh et haMikdash, yekabetz et ha'gola ve'yavi da'at et ha'Hashem la'olam kulo. Emunah bi'vo'o hi yesod ha'emunah ha'Yehudit. Mitpalelim le'geulah yom yom.",
        "Mashiach — Mashiach (Moshiaj) es el rey justo ungido del linaje de David que reconstruirá el Templo, reunirá a los exiliados y traerá conocimiento universal de D-s. Creer en su venida es fundamento de la fe judía. Oramos por la redención diariamente.",
        "Machiach — Machiach (Moshiach) est le roi juste oint de la lignée de David qui reconstruira le Temple, rassemblera les exilés et apportera la connaissance universelle de D. Croire en sa venue est un fondement de la foi juive. Nous prions pour la rédemption chaque jour.",
        "Mashiach — Mashiach (Moshiach) — pravednyy pomazannyy tsar iz roda David, kotoryy vosstanovit Khram, soberet izgnannikov i prineset vsемirnoye poznanie V-ga. Vera v ego prikhod — osnova evreyskoy very. My molimsya o geule ezhednevno.",
    ),
    "Psak is a halachic ruling — the answer a qualified posek gives for your real case, not a theoretical debate. Judaism has legitimate range between authorities; your job is to follow your rav's psak consistently rather than shop around for leniencies.": T(
        "Psak hu psak halacha — ha'teshuvah she'posek barur nosen le'mikre shelkha ha'amiti, lo l'vivuch te'oreti. BeYahadut yesh range legitimi bein rabbanim; tachlichem le'kabel psak ha'rav shelkhem be'emtza ve'lo le'chapes kulot.",
        "Psak es una decisión halájica — la respuesta que un posek calificado da a tu caso real, no un debate teórico. El judaísmo tiene rangos legítimos entre autoridades; tu tarea es seguir el psak de tu rav consistentemente en lugar de buscar leniencias.",
        "Psak est une décision halakhique — la réponse qu'un posek qualifié donne pour votre cas réel, pas un débat théorique. Le judaïsme a des écarts légitimes entre autorités ; votre rôle est de suivre le psak de votre rav de façon cohérente plutôt que de chercher des assouplissements.",
        "Psak — halakhicheskoye reshenie — otvet kvalifitsirovannogo poseka na vash realnyy sluchay, a ne teoreticheskiy spor. V iudaizme est dopustimye razlichiya mezhdu autoritetami; vasha zadacha — posledovatelno sledovat psak vashego rava, a ne iskat poslableniya.",
    ),
    'Tzedakah is usually translated "charity," but the root means justice — sharing what G-d entrusted to you with those in need. The sages list it among the pillars on which the world stands; even a small coin given with a full heart is a great mitzvah.': T(
        "Tzedakah meturgemet le\"charity,\" aval ha'shoresh mashma tzedek — le'chalek et mah she'hafkid Hashem etkhem im ha'needyim. Ha'chachamim me'anyenim otah bein amudei ha'olam; afilu pruta she'nitanah be'lev shalem hi mitzvah gedolah.",
        'Tzedaká se traduce usualmente como "caridad," pero la raíz significa justicia — compartir lo que D-s te confió con quienes lo necesitan. Los sabios la incluyen entre los pilares del mundo; incluso una moneda pequeña dada con corazón pleno es una gran mitzvá.',
        'Tsedaka se traduit souvent par « charité, » mais la racine signifie justice — partager ce que D. vous a confié avec les nécessiteux. Les sages la comptent parmi les piliers du monde ; même une petite pièce donnée de bon cœur est une grande mitzvah.',
        'Tzedakah obychno perevodyat kak «blagotvoritelnost,» no koren znachit spravedlivost — delitsya tem, chto V-s vam vveril, s nuzhdayushchimisya. Mudretsy schitayut ee odним iz stolpov mira; dazhe malaya moneta, dannaya ot vsego serdtsa — velikaya mitzvah.',
    ),
    "Baal Shem Tov — The Baal Shem Tov (Rabbi Israel ben Eliezer, c. 1700–1760) founded the Chasidic movement, teaching that every Jew can serve G-d with joy and that divine presence fills all creation. Chabad traces its spiritual lineage to his disciples.": T(
        "Baal Shem Tov — HaBaal Shem Tov (Rabbi Yisrael ben Eliezer, c. 1700–1760) yisad et tnu'at haChasidut, melamed she'kol Yehudi yachol la'avod et ha'Hashem be'simcha ve'she'hashra'at haShechinah mamla et kol ha'beriah. Chabad omeket et sharshelta ha'ruchanit le'talmidav.",
        "Baal Shem Tov — El Baal Shem Tov (Rabí Israel ben Eliezer, c. 1700–1760) fundó el movimiento jasídico, enseñando que todo judío puede servir a D-s con alegría y que la presencia divina llena toda la creación. Jabad remonta su linaje espiritual a sus discípulos.",
        "Baal Shem Tov — Le Baal Shem Tov (Rabbi Israel ben Eliezer, c. 1700–1760) a fondé le mouvement hassidique, enseignant que tout Juif peut servir D. avec joie et que la présence divine remplit toute la création. Habad trace sa lignée spirituelle à ses disciples.",
        "Baal Shem Tov — Baal Shem Tov (Rav Yisrael ben Eliezer, ok. 1700–1760) osnoval khasidskoe dvizhenie, uchа ya, chto kazhdyy evrey mozhet sluzhit V-gu s radostyu i chto bozhestvennoe prisutstvie napolnyaet vse tvorenie. Chabad vedet dukhovnuyu liniyu k ego uchenikam.",
    ),
    "daven — Daven (davening) means to pray. You 'daven' the daily services (Shacharit, Mincha, Maariv). The word is Yiddish-derived Ashkenazi slang but widely understood. People often say \"daven Shacharit\" for a service and \"daven\" for praying in general.": T(
        "daven — Daven (davening) mashma le'hitpalel. 'Daven' et hatefillot hayom yom (Shacharit, Mincha, Maariv). Ha'milah hi slang Ashkenazi miYiddish aval me'uvetet. Omrim 'daven Shacharit' le'tefillah u'daven' le'hitpallel b'chlal.",
        "daven — Daven (davening) significa orar. Se 'daven' los servicios diarios (Shajarit, Minjá, Maariv). La palabra es jerga ashkenazí del yiddish pero ampliamente entendida. Se dice \"daven Shacharit\" para un servicio y \"daven\" para orar en general.",
        "daven — Daven (davening) signifie prier. On « daven » les offices quotidiens (Shacharit, Min'ha, Maariv). Le mot est un argot ashkénaze du yiddish mais largement compris. On dit « daven Shacharit » pour un office et « daven » pour prier en général.",
        "daven — Daven (davening) oznachaet molitsya. 'Daven' — ezhednevnye sluzhby (Shacharit, Mincha, Maariv). Slovo iz ashkenazskogo zhargona na idishe, no shiroko ponimaetsya. Govoryat «daven Shacharit» o sluzhbe i «daven» o molitve voobshche.",
    ),
}

def fmt_entries(entries: dict) -> str:
    lines = ["ENTRIES = {"]
    for key, val in entries.items():
        he, es, fr, ru = val
        lines.append(f"    {json.dumps(key, ensure_ascii=False)}: T(")
        lines.append(f"        {json.dumps(he, ensure_ascii=False)},")
        lines.append(f"        {json.dumps(es, ensure_ascii=False)},")
        lines.append(f"        {json.dumps(fr, ensure_ascii=False)},")
        lines.append(f"        {json.dumps(ru, ensure_ascii=False)},")
        lines.append("    ),")
    lines.append("}")
    return "\n".join(lines)

def write_module(path: Path, entries: dict) -> None:
    content = (
        "# -*- coding: utf-8 -*-\n"
        "def T(he, es, fr, ru):\n"
        "    return (he, es, fr, ru)\n\n"
        + fmt_entries(entries)
        + "\n"
    )
    path.write_text(content, encoding="utf-8")

def main() -> None:
    keys = json.loads((ROOT / "_overlay_missing.json").read_text(encoding="utf-8"))
    all_entries = {**EXISTING, **NEW, **BATCH3}
    missing = [k for k in keys if k not in all_entries]
    if missing:
        print(f"Still missing {len(missing)}: {missing[0]!r}")
        sys.exit(1)
    for i, name in enumerate(["rest_e_1", "rest_e_2", "rest_e_3"]):
        batch = {k: all_entries[k] for k in keys[i * 48 : (i + 1) * 48]}
        write_module(ROOT / f"{name}.py", batch)
        print(f"Wrote {name}.py: {len(batch)} entries")

if __name__ == "__main__":
    main()
