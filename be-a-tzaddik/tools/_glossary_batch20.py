"""Human-quality fixes batch 20 — long halacha explainers (ES + FR)."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = json.loads((ROOT / "data" / "translation-catalog" / "strings.json").read_text(encoding="utf-8"))[
    "strings"
]

ASHER_YATZAR_FR = (
    "Après avoir utilisé les toilettes, nous nous lavons les mains et disons la bénédiction Asher Yatzar — "
    "chaque fois que vous avez terminé.\n\n"
    "Quand la dire :\n"
    "• Normalement : dites Asher Yatzar chaque fois que vous utilisez les toilettes, une fois que vous "
    "avez terminé et lavé vos mains.\n"
    "• Si vous êtes malade — par exemple diarrhée ou autre état où vous pourriez devoir y retourner "
    "immédiatement — attendez d'être raisonnablement sûr d'avoir terminé cette fois avant de la dire. "
    "Sinon vous pourriez dire la bénédiction puis devoir y retourner aussitôt, ce qui est peu pratique "
    "et manque le but de la bracha.\n\n"
    "De quoi s'agit-il :\n"
    "Asher Yatzar (אֲשֶׁר יָצַר — « Qui a formé ») est une bénédiction remerciant D. pour la conception "
    "miraculeuse du corps humain. C'est l'une des prières les plus perspicaces jamais composées.\n\n"
    "La bénédiction dit :\n"
    "D. a créé le corps humain avec sagesse — avec ouvertures et fermetures (tubes, valves, sphincters). "
    "Si l'un d'eux devait s'ouvrir quand il devrait être fermé, ou se fermer quand il devrait être ouvert, "
    "il serait impossible de survivre.\n\n"
    "Pourquoi c'est profond :\n"
    "La médecine moderne l'a confirmé exactement. Un uretère bloqué, une valve cardiaque coincée ou un "
    "intestin obstrué — chacun est une urgence vitale. Chaque fois que votre corps fonctionne normalement, "
    "c'est un miracle. Asher Yatzar nous entraîne à le reconnaître comme tel.\n\n"
    "Comment faire :\n"
    "1. Après les toilettes, lavez-vous les mains (sans bénédiction, sauf s'il s'agit de votre netilat "
    "yadayim du matin)\n"
    "2. Dites Asher Yatzar — cela prend environ 20 secondes\n\n"
    "Trouvez le texte dans n'importe quel siddour ou application. Dans Mitz Mode, appuyez sur le menu (⋮) "
    "→ **Bénédictions** pour le texte."
)

BRACHA_FR = (
    "Avant de manger ou de boire quoi que ce soit, nous disons une courte bénédiction (bracha) "
    "remerciant D. pour la nourriture. C'est l'une des mitsvot les plus fréquentes de la vie quotidienne.\n\n"
    "Qu'est-ce qu'une bracha :\n"
    "Bracha (בְּרָכָה — bénédiction ; pluriel : brachot) est une brève formule commençant par "
    "« Baruch Atah Ado-nai Eloheinu Melech ha'olam… » (Béni sois-Tu, Éternel notre D., Roi de l'univers) "
    "suivie d'une conclusion spécifique selon l'aliment.\n\n"
    "Le principe du Talmud :\n"
    "« Il est interdit de profiter de ce monde sans bénédiction. » Manger sans bracha, c'est traiter "
    "le monde de D. comme sans propriétaire — la bracha est notre reconnaissance que tout appartient à D.\n\n"
    "Les six principales bénédictions avant les aliments :\n"
    "• Hamotzi (הַמּוֹצִיא) — pour le pain des cinq céréales (blé, orge, épeautre, avoine, seigle)\n"
    "• Mezonot (מְזוֹנוֹת) — pour les autres produits à base de céréales : gâteau, biscuits, pâtes, céréales\n"
    "• HaGafen (הַגָּפֶן) — pour le vin ou le jus de raisin ; la bénédiction est Borei pri ha'gafen "
    "(בּוֹרֵא פְּרִי הַגָּפֶן)\n"
    "• HaEtz (הָעֵץ) — pour les fruits d'arbre (pommes, oranges, raisins, etc.)\n"
    "• HaAdamah (הָאֲדָמָה) — pour les produits du sol (légumes, bananes, fraises)\n"
    "• Shehakol (שֶׁהַכֹּל) — pour tout le reste : viande, poisson, œufs, lait, fromage, eau, bonbons, etc."
)

CHOL_HAMOED_FR = (
    "Chol HaMoed (חול המועד) — les jours intermédiaires de la fête — n'est pas un Yom Tov complet, "
    "mais ce n'est pas non plus un jour de semaine ordinaire.\n"
    "$hoshanaRabaBlock\n\n"
    "Esprit du jour :\n"
    "• Simchat moed — joie de la fête ; repas plus agréables, temps en famille, étude de la Torah.\n"
    "• Melacha — de nombreux travaux sont restreints (moins strict que Yom Tov). Un travail nécessaire "
    "pour éviter une perte financière peut être permis — demandez à votre rabbin avant de supposer.\n"
    "• Ochel nefesh — la préparation des aliments est permise.\n\n"
    "Que faire :\n"
    "$hallelBlock\n"
    "• Évitez de le traiter comme un jour de travail ordinaire — planifiez sorties, étude et visites "
    "adaptées au moed.\n"
    "$festivalLines\n\n"
    "À éviter (sans guidance halakhique) : grosse lessive, grands travaux à la maison, rasage, "
    "coupe de cheveux et activités qui diminuent l'atmosphère de fête.\n"
    "• Restrictions de toilette : le rasage et la coupe de cheveux sont strictement interdits "
    "pendant Chol HaMoed (Shulchan Arukh O.C. 531) — sauf dans des conditions très spécifiques, "
    "comme un garçon qui atteint trois ans ou une sortie de prison (demandez à votre rav). "
    "Cela s'applique même si vous souhaitez paraître soigné pour les jours restants de la fête."
)

CHOL_HAMOED_ES = (
    "Chol HaMoed (חול המועד) — los días intermedios de la festividad — no es Yom Tov completo, "
    "pero tampoco es un día de semana ordinario.\n"
    "$hoshanaRabaBlock\n\n"
    "Espíritu del día:\n"
    "• Simjat moed — alegría de la festividad; comidas más agradables, tiempo en familia, estudio de Torá.\n"
    "• Melajá — muchas labores están restringidas (no tan estrictas como Yom Tov). Puede permitirse "
    "el trabajo necesario para evitar pérdidas financieras — consulta a tu rabino antes de asumirlo.\n"
    "• Ojel nefesh — la preparación de alimentos está permitida.\n\n"
    "Qué hacer:\n"
    "$hallelBlock\n"
    "• Evita tratarlo como un día laboral normal — planifica salidas, estudio y visitas acordes al moed.\n"
    "$festivalLines\n\n"
    "Evitar (sin orientación halájica): lavado pesado, grandes proyectos en casa, afeitado, cortes de "
    "pelo y actividades que disminuyan el ambiente festivo.\n"
    "• Restricciones de aseo: afeitarse y cortarse el pelo están estrictamente prohibidos en Chol HaMoed "
    "(Shulján Aruj O.C. 531) — salvo en condiciones muy específicas, como un niño que cumple tres años "
    "o al salir de prisión (consulta a tu rav). Esto aplica aunque quieras lucir arreglado "
    "para los días restantes de la festividad."
)

KISUI_ROSH_FR = (
    "Les femmes juives mariées sont tenues de couvrir leurs cheveux chaque fois qu'elles sont en public "
    "ou en présence d'hommes autres que leur mari.\n\n"
    "De quoi s'agit-il :\n"
    "Kisui rosh (כִּיסּוּי רֹאשׁ — littéralement « couverture de la tête ») est l'une des pratiques "
    "fondamentales de la vie juive mariée. C'est une obligation au niveau de la Torah "
    "(Shulchan Aruch, Even HaEzer 115:4).\n\n"
    "Pourquoi :\n"
    "Les cheveux d'une femme mariée sont considérés comme ervah (עֶרְוָה — littéralement « nudité » ; "
    "en termes halakhiques, quelque chose de privé qui ne doit pas être exposé publiquement). "
    "Les couvrir exprime que sa beauté la plus profonde est réservée à l'intimité de son foyer et "
    "de son mari. C'est aussi une expression de tzniut (pudeur) — la valeur juive de la dignité "
    "intérieure plutôt que de l'affichage extérieur.\n\n"
    "Méthodes de couverture :\n"
    "• Tichel (טִיכֶל) — foulard ou enveloppement, couramment utilisé dans de nombreuses communautés\n"
    "• Sheitel (שֵׁייטֶל) — perruque ; largement utilisée dans les communautés ashkénazes et hassidiques\n"
    "• Chapeau ou béret\n"
    "• Mitpachot — enveloppes décoratives et turbans\n"
    "• Combinaison (par exemple petite perruque avec un chapeau par-dessus)\n\n"
    "Normes :\n"
    "La plupart des autorités orthodoxes exigent que l'intégralité des cheveux soit couverte en public. "
    "Les détails varient selon la communauté. Parlez avec votre rabbin ou rebbetzin de ce qui s'applique "
    "dans votre situation."
)

KISUI_ROSH_ES = (
    "Las mujeres judías casadas están obligadas a cubrir su cabello cuando están en público "
    "o en presencia de hombres que no sean su marido.\n\n"
    "Qué es:\n"
    "Kisui rosh (כִּיסּוּי רֹאשׁ — literalmente «cubrir la cabeza») es una de las prácticas "
    "fundamentales de la vida matrimonial judía. Es una obligación a nivel de Torá "
    "(Shulján Aruj, Even HaEzer 115:4).\n\n"
    "Por qué:\n"
    "El cabello de una mujer casada se considera ervah (עֶרְוָה — literalmente «desnudez»; "
    "en términos halájicos, algo privado que no debe mostrarse en público). Cubrirlo expresa "
    "que su belleza más profunda está reservada para la intimidad de su hogar y su marido. "
    "También es expresión de tzniut (modestia) — el valor judío de la dignidad interior "
    "por encima de la exhibición externa.\n\n"
    "Métodos de cobertura:\n"
    "• Tichel (טִיכֶל) — pañuelo o envoltorio, muy usado en muchas comunidades\n"
    "• Sheitel (שֵׁייטֶל) — peluca; ampliamente usada en comunidades ashkenazí y jasidí\n"
    "• Sombrero o boina\n"
    "• Mitpachot — envoltorios decorativos y turbantes\n"
    "• Combinación (por ejemplo, peluca pequeña con sombrero encima)\n\n"
    "Estándares:\n"
    "La mayoría de las autoridades ortodoxas exigen cubrir todo el cabello en público. "
    "Los detalles varían según la comunidad. Consulta con tu rabino o rebetzin "
    "sobre lo que aplica en tu situación."
)

PRU_U_REVU_FR = (
    "La Torah commande : « Soyez féconds et multipliez » — Pru u'Revu (פרו ורבו, Bereishit 1:28). "
    "Les hommes juifs sont obligés de se marier et d'avoir des enfants (Yevamot 61b ; "
    "Shulchan Arukh, Even HaEzer 1:1).\n\n"
    "Accomplir l'obligation :\n"
    "Un homme accomplit pru u'revu lorsqu'il a au moins un fils et une fille qui survivent, "
    "chacun capable en principe d'avoir ses propres enfants (Yevamot 62a).\n\n"
    "Mariage dans l'alliance :\n"
    "L'épouse doit être juive (Shulchan Arukh EH 2:1). Les enfants nés d'une femme non juive "
    "ne sont pas considérés comme les siens selon la halakha ; l'identité juive suit la mère "
    "(EH 4:19 ; Yevamot 17a). Un homme est attendu pour bâtir un foyer juif — et non rester "
    "célibataire par choix (Rambam, Ishut 15:16).\n\n"
    "Un enfant né d'une femme juive hors mariage peut compter en principe pour la mitsvah, "
    "mais les relations hors mariage sont interdites (EH 178 ; Devarim 23:18). "
    "Le mariage est la voie appropriée.\n\n"
    "Au-delà du minimum — la'arev ba'olam (לערב בעולם) :\n"
    "Une fois l'obligation toranique remplie, les Sages enseignent une mitsvah supplémentaire "
    "de continuer à avoir des enfants — pour peupler le monde d'âmes juives "
    "(Yevamot 62b ; EH 1:5–8 ; Rema EH 1:8). Jusqu'où poursuivre dépend de la santé, "
    "des circonstances et du psak personnel.\n\n"
    "Quand les enfants ne sont pas possibles :\n"
    "Celui qui ne peut pas avoir d'enfants malgré des efforts raisonnables peut être exempté — "
    "consulter un rav qualifié (EH 1:5–6). Les poskim discutent traitements de fertilité, "
    "adoption et autres voies ; les décisions diffèrent. L'obligation de chercher le mariage "
    "demeure même lorsque l'accomplissement s'avère difficile."
)

PRU_U_REVU_ES = (
    "La Torá ordena: «Sed fecundos y multiplicaos» — Pru u'Revu (פרו ורבו, Bereishit 1:28). "
    "Los hombres judíos están obligados a casarse y engendrar hijos (Yevamot 61b; "
    "Shulján Aruj, Even HaEzer 1:1).\n\n"
    "Cumplir la obligación:\n"
    "Un hombre cumple pru u'revu cuando tiene al menos un hijo y una hija que sobreviven, "
    "cada uno capaz en principio de tener hijos propios (Yevamot 62a).\n\n"
    "Matrimonio dentro del pacto:\n"
    "La esposa debe ser judía (Shulján Aruj EH 2:1). Los hijos nacidos de una mujer no judía "
    "no se consideran suyos según la halajá; la identidad judía sigue a la madre "
    "(EH 4:19; Yevamot 17a). Se espera que un hombre construya un hogar judío — "
    "y no permanezca soltero por elección (Rambam, Ishut 15:16).\n\n"
    "Un hijo nacido de una mujer judía fuera del matrimonio puede contar en principio "
    "para la mitzvá, pero las relaciones fuera del matrimonio están prohibidas "
    "(EH 178; Devarim 23:18). El matrimonio es el camino apropiado.\n\n"
    "Más allá del mínimo — la'arev ba'olam (לערב בעולם):\n"
    "Tras cumplir la obligación de la Torá, los Sabios enseñan una mitzvá adicional "
    "de seguir teniendo hijos — para poblar el mundo con almas judías "
    "(Yevamot 62b; EH 1:5–8; Rema EH 1:8). Hasta dónde perseguirlo depende de la salud, "
    "las circunstancias y el psak personal.\n\n"
    "Cuando no es posible tener hijos:\n"
    "Quien no puede tener hijos a pesar de esfuerzos razonables puede estar exento — "
    "consulte a un rav calificado (EH 1:5–6). Los poskim discuten tratamientos de fertilidad, "
    "adopción y otros caminos; los fallos difieren. La obligación de buscar matrimonio "
    "permanece incluso cuando el cumplimiento resulta difícil."
)

REVIIT_BODY_ES = (
    "Un revi'it (רביעית) es una unidad estándar de volumen líquido en halajá, definida como "
    "un cuarto de log — comúnmente equivalente al volumen de un huevo y medio. Es el volumen "
    "mínimo requerido para mitzvot rituales con líquidos, como el Kidush. Como los volúmenes "
    "físicos han variado y sido debatidos, existen varias medidas contemporáneas según el posek "
    "seguido: Rav Chaim Na'eh: ~86 ml (aprox. 2,92 fl oz) — ampliamente usado como medida leniente, "
    "especialmente en tradición sefardí y para obligaciones rabínicas como netilat yadayim. "
    "Rab Moshe Feinstein: ~130–150 ml (aprox. 4,4–5,1 fl oz) — muchos lo recomiendan para "
    "obligaciones de nivel Torá como Kidush de Shabat. El Jazon Ish: ~150 ml (aprox. 5,1 fl oz) — "
    "estándar estricto en muchas comunidades ashkenazí y jasidí para mitzvot de nivel Torá. "
    "Además del Kidush, un revi'it también se requiere para Havdalá (mínimo de vino bebido de la copa), "
    "netilat yadayim (mínimo de agua vertida — puede aplicarse la medida menor) y las cuatro copas "
    "del Seder de Pesaj. Tras el Kidush, muchos exigen beber melo lugmav o la mayor parte de la copa; "
    "consulta a tu rav qué medida usar."
)

REVIIT_BODY_FR = (
    "Un revi'it (רביעית) est une unité standard de volume liquide en halakha, définie comme "
    "le quart d'un log — couramment traduit par le volume d'un œuf et demi. C'est le volume "
    "minimum requis pour les mitsvot rituelles impliquant des liquides, comme le Kiddouch. "
    "Comme les volumes physiques ont varié et fait débat, plusieurs mesures contemporaines "
    "existent selon le posek suivi : Rav Chaim Na'eh : ~86 ml (env. 2,92 fl oz) — largement "
    "utilisé comme base clémente, en particulier dans la tradition séfarade et pour les obligations "
    "rabbiniques comme netilat yadayim. Rav Moshe Feinstein : ~130–150 ml (env. 4,4–5,1 fl oz) — "
    "recommandé par beaucoup pour les obligations de niveau Torah comme le Kiddouch de Shabbat. "
    "Le Hazon Ish : ~150 ml (env. 5,1 fl oz) — norme stricte dans de nombreuses communautés "
    "ashkénazes et hassidiques pour les mitsvot de niveau Torah. Outre le Kiddouch, un revi'it "
    "est aussi requis pour la Havdala (minimum de vin bu dans la coupe), netilat yadayim "
    "(minimum d'eau versée — on peut s'appuyer sur la mesure plus petite) et les quatre coupes "
    "du Seder de Pessa'h. Après le Kiddouch, beaucoup exigent de boire melo lugmav ou la majorité "
    "de la coupe ; consultez votre rav sur la mesure à utiliser."
)

GUARD_TONGUE_ES = (
    "¡Guarda tu lengua! 👄 El poder de las palabras es increíble: puede construir mundos o destruirlos. "
    "Algo asombroso: la palabra hebrea para el habla (דיבור) tiene el mismo valor numérico que la palabra "
    "para abeja (דבורה). Como una abeja puede dar miel o picar, nuestras palabras pueden ser dulces o dañinas. "
    "Reto de hoy: antes de hablar de otros, pregúntate: ¿es verdad? ¿es amable? ¿es necesario? "
    "¡Que tus palabras sean dulces como miel!"
)

GAM_ZU_ES = (
    "¡Aprende a ver lo bueno en todo! Cuando la vida te lanza un desafío, intenta decir "
    "«Gam zu l'tovah» (גם זו לטובה) — ¡esto también es para el bien! ✡ Esta frase poderosa "
    "la hizo famosa el sabio Nachum Ish Gam Zu, que siempre encontraba el lado positivo. "
    "Incluso en tiempos difíciles, confía en que D-s tiene un plan perfecto. "
    "Es como un GPS para el alma: a veces la ruta parece rara, pero te lleva exactamente "
    "adonde debes ir. 🗺️"
)

ASHER_YATZAR_KEY = next(k for k in CATALOG if k.startswith("After using the bathroom, we wash our hands"))
BRACHA_KEY = next(k for k in CATALOG if k.startswith("Before eating or drinking anything, we say a short blessing"))
CHOL_HAMOED_KEY = next(k for k in CATALOG if k.startswith("Chol HaMoed (חול המועד)"))
KISUI_ROSH_KEY = next(k for k in CATALOG if k.startswith("Married Jewish women are required to cover their hair"))
PRU_U_REVU_KEY = next(k for k in CATALOG if k.startswith('The Torah commands: "Be fruitful and multiply"'))
REVIIT_PREFIX_KEY = next(k for k in CATALOG if k.startswith("revi'it — A revi'it"))
REVIIT_BODY_KEY = next(k for k in CATALOG if k.startswith("A revi'it (רביעית) is a standard unit"))
GUARD_TONGUE_KEY = next(k for k in CATALOG if k.startswith("Guard your tongue!"))
GAM_ZU_KEY = next(k for k in CATALOG if k.startswith("Learn about seeing the good in everything"))

BATCH20_ES: dict[str, str] = {
    CHOL_HAMOED_KEY: CHOL_HAMOED_ES,
    KISUI_ROSH_KEY: KISUI_ROSH_ES,
    PRU_U_REVU_KEY: PRU_U_REVU_ES,
    REVIIT_PREFIX_KEY: f"revi'it — {REVIIT_BODY_ES}",
    REVIIT_BODY_KEY: REVIIT_BODY_ES,
    GUARD_TONGUE_KEY: GUARD_TONGUE_ES,
    GAM_ZU_KEY: GAM_ZU_ES,
}

BATCH20_FR: dict[str, str] = {
    ASHER_YATZAR_KEY: ASHER_YATZAR_FR,
    BRACHA_KEY: BRACHA_FR,
    CHOL_HAMOED_KEY: CHOL_HAMOED_FR,
    KISUI_ROSH_KEY: KISUI_ROSH_FR,
    PRU_U_REVU_KEY: PRU_U_REVU_FR,
    REVIIT_PREFIX_KEY: f"revi'it — {REVIIT_BODY_FR}",
    REVIIT_BODY_KEY: REVIIT_BODY_FR,
}
