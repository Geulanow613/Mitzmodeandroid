"""Human-quality fixes batch 18 — ES/FR ona'ah, misheyakir, Arba Minim, business ethics, shofar."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = json.loads((ROOT / "data" / "translation-catalog" / "strings.json").read_text(encoding="utf-8"))[
    "strings"
]

ONA_AH_BODY_ES = (
    "Cobrar de más o pagar de menos cuando la otra parte no conoce el valor justo. "
    "Una variación de un sexto o más por encima o por debajo del precio justo de mercado "
    "activa las leyes de la Torá sobre fraude financiero (Bava Metzia 49b), que exigen "
    "la devolución íntegra del monto cobrado en exceso. Un negocio honesto es una mitzvá; "
    "el «todos lo hacen» no excusa geneivat da'at ni ona'ah."
)

ONA_AH_BODY_FR = (
    "Surfacturation ou sous-paiement lorsque l'autre partie ne connaît pas la juste valeur. "
    "Une variation d'un sixième ou plus au-dessus ou en dessous du juste prix du marché "
    "déclenche les lois de la Torah sur la fraude financière (Bava Metzia 49b), "
    "exigeant le remboursement intégral du montant facturé en trop. "
    "L'honnêteté en affaires est une mitsvah ; « tout le monde le fait » n'excuse ni "
    "geneivat da'at ni ona'ah."
)

MISHEYAKIR_ES = (
    "misheyakir — Misheyakir (מִשֶּׁיַּכִּיר) es cuando hay suficiente luz ambiental natural "
    "para reconocer a un conocido ocasional desde unos cuatro amot (~1,8 m). "
    "El momento más temprano para recitar la berajá del talit y los tefilín es misheyakir — "
    "a menudo unos 35–50 minutos después de alot hashajar (varía según estación y ubicación). "
    "Antes de misheyakir no se cumple la mitzvá, incluso después del amanecer. "
    "La Mishná Berurá recomienda esperar hasta misheyakir, ponerse el talit y los tefilín "
    "y recitar Keriat Shemá y la Amidá; muchas shul rezan hasta Yishtabaj sin ellos y "
    "esperan misheyakir. La Guemará (Berajot 14b) compara el Shemá sin talit y tefilín "
    "con falso testimonio — evítalo si puedes esperar. Si debes salir muy temprano: "
    "Igrot Moshe (O.C. 4:6) permite usarlas después de alot hashajar sin berajá, orar, "
    "moverlas ligeramente y recitar las berajot después de misheyakir. "
    "Preferencia: (1) talit y tefilín después de misheyakir, Amidá al amanecer; "
    "(2) después de misheyakir, Amidá antes del amanecer; "
    "(3) solo si es necesario — después de alot hashajar sin berajá, berajot tras misheyakir."
)

MISHEYAKIR_FR = (
    "misheyakir — Misheyakir (מִשֶּׁיַּכִּיר) correspond à l'instant où il y a assez "
    "de lumière ambiante naturelle pour reconnaître une connaissance occasionnelle "
    "à environ quatre amot (~1,8 m). Le moment le plus tôt pour réciter la berakha "
    "sur le talit et le tefillin est le misheyakir — souvent environ 35 à 50 minutes "
    "après alot hashachar (selon la saison et le lieu). Avant le misheyakir, la mitsvah "
    "n'est pas accomplie, même après l'aube. La Mishna Berurah recommande d'attendre "
    "le misheyakir, puis de mettre talit et tefillin et de réciter la lecture du Shema "
    "et l'Amida ; beaucoup de shuls prient jusqu'à Yishtabah sans eux, puis attendent "
    "le misheyakir. La Guemara (Berakhot 14b) compare le Shema sans talit et tefillin "
    "à un faux témoignage — évitez si vous pouvez attendre. Si vous devez partir très tôt : "
    "Igrot Moshe (O.C. 4:6) permet de les porter après alot hashachar sans berakha, "
    "de prier, de les déplacer légèrement puis de réciter les berachot après le misheyakir. "
    "Préférence : (1) talit et tefillin après misheyakir, Amida au lever du soleil ; "
    "(2) après misheyakir, Amida avant le lever du soleil ; "
    "(3) seulement si nécessaire — après alot hashachar sans berakha, berachot après misheyakir."
)

ARBA_MINIM_ES = (
    "Arba Minim (ארבעה מינים) — las Cuatro Especies — se toman cada día de Sucot (excepto Shabat).\n\n"
    "Los cuatro:\n"
    "• Lulav — rama de palma cerrada (al menos 3 tefajim)\n"
    "• Etrog — etrog, hermoso y mayormente intacto (el pitom, si está presente, debe estar intacto)\n"
    "• Hadasim — tres ramas de mirto\n"
    "• Aravot — dos ramas de sauce\n\n"
    "Cómo observarlo (todos):\n"
    "• Armar el lulav: ata el lulav, los hadasim y los aravot (según tu portador de hoshanot / koisan). "
    "La columna central del lulav debe extenderse hacia arriba al menos un tefaj (aprox. 10 cm) "
    "por encima de las ramas de mirto y sauce (Shulján Aruj O.C. 650:1).\n"
    "• Revisa la kashrut antes del Yom Tov — especialmente el etrog y que las hojas estén frescas.\n"
    "• Cuándo: de día; muchos lo hacen antes de Shajarit en casa o en la shul. No en Shabat.\n"
    "• $daysNote\n\n"
    "Si falta una especie o no es válida, consulta a tu rabino — hay sustituciones limitadas en casos urgentes."
)

ARBA_MINIM_FR = (
    "Arba Minim (ארבעה מינים) — les Quatre Espèces — sont prises chaque jour de Soukot (sauf le Chabbat).\n\n"
    "Les quatre :\n"
    "• Lulav — branche de palmier fermée (au moins 3 tefahim)\n"
    "• Etrog — citron, beau et pour l'essentiel intact (le pitom, s'il est présent, doit être intact)\n"
    "• Hadasim — trois branches de myrte\n"
    "• Aravot — deux branches de saule\n\n"
    "Comment l'observer (tous) :\n"
    "• Assembler le lulav : liez le lulav, les hadassim et les aravot (selon votre porte-hoshanot / koisan). "
    "L'axe central du lulav doit dépasser d'au moins un tefah (environ 10 cm) le sommet des branches "
    "de myrte et de saule (Shoulhan Aroukh O.C. 650:1).\n"
    "• Vérifiez la cacherout avant Yom Tov — surtout l'étrog et que les feuilles soient assez fraîches.\n"
    "• Quand : le jour ; beaucoup le font avant Shaharit à la maison ou à la shul. Pas le Chabbat.\n"
    "• $daysNote\n\n"
    "Si une espèce manque ou n'est pas valide, consultez votre rabbin — il existe des substitutions "
    "limitées en cas de nécessité."
)

JEWISH_LAW_ES = (
    "La ley judía se aplica en el mercado y la oficina como en la sinagoga. Los pesos honestos, "
    "las ofertas justas y la conducta confiable son obligaciones de la Torá, no un «crédito extra» opcional.\n\n"
    "Lo que esto incluye:\n"
    "• Geneivat da'at (גְּנֵבַת דַּעַת — engaño): engañar a un cliente o empleador "
    "sobre el precio, la calidad, los términos o sus calificaciones.\n"
    "• Ona'ah (אוֹנָאָה): cobrar de más o pagar de menos cuando la otra parte no conoce el valor justo. "
    "Una variación de un sexto o más por encima o por debajo del precio justo de mercado activa "
    "las leyes de la Torá sobre fraude financiero, exigiendo la devolución íntegra del monto cobrado en exceso.\n"
    "• Cumplir la palabra: honrar acuerdos, plazos y compromisos; no prometer lo que no puedes cumplir.\n"
    "• Pagar a los trabajadores a tiempo: los salarios adeudados deben pagarse cuando corresponda (Levítico 19:13).\n"
    "• No robar en ninguna forma — incluido robo de tiempo, fraude de gastos o tomar lo que no es tuyo.\n"
    "• Impuestos y deudas: cumplir las obligaciones legales con honestidad; no engañar al gobierno ni a terceros.\n\n"
    "Idea clave:\n"
    "«Con justicia juzgarás a tu prójimo» (Levítico 19:15) — los negocios se juzgan con el mismo "
    "estándar moral que las mitzvot rituales. La integridad de un judío (yosher — יֹשֶׁר) es una "
    "sanificación del Nombre de D-s en el mundo.\n\n"
    "Cómo practicarlo:\n"
    "• Sé transparente en ventas, facturación y negociaciones.\n"
    "• En caso de duda, consulta a un rabino o asesor de confianza sobre un contrato o práctica concreta.\n"
    "• Trata a empleados, clientes y empleadores con la dignidad que exige la Torá.\n\n"
    "Marca esto como intención diaria de actuar con honestidad en todas tus interacciones "
    "financieras y profesionales."
)

JEWISH_LAW_FR = (
    "La loi juive s'applique sur le marché et au bureau, tout comme dans la synagogue. "
    "Les poids honnêtes, les accords équitables et une conduite digne de confiance sont "
    "des obligations de la Torah, et non des « crédits supplémentaires » facultatifs.\n\n"
    "Ce que cela inclut :\n"
    "• Geneivat da'at (גְּנֵבַת דַּעַת — tromperie) : induire en erreur un client ou un employeur "
    "sur le prix, la qualité, les conditions ou vos qualifications.\n"
    "• Ona'ah (אוֹנָאָה) : surfacturation ou sous-paiement lorsque l'autre partie ne connaît pas "
    "la juste valeur. Une variation d'un sixième ou plus au-dessus ou en dessous du juste prix "
    "du marché déclenche les lois de la Torah sur la fraude financière, exigeant le remboursement "
    "intégral du montant facturé en trop.\n"
    "• Tenir parole : honorer accords, délais et engagements ; ne pas promettre ce que l'on ne peut tenir.\n"
    "• Payer les travailleurs à temps : les salaires dus doivent être payés à l'échéance (Lévitique 19:13).\n"
    "• Ne pas voler sous aucune forme — y compris vol de temps, fraude de dépenses ou appropriation "
    "de ce qui n'est pas à vous.\n"
    "• Impôts et dettes : remplir honnêtement les obligations légales ; ne pas tromper l'État ni des tiers.\n\n"
    "Idée clé :\n"
    "« Tu jugeras ton prochain avec justice » (Lévitique 19:15) — les affaires sont jugées selon "
    "le même critère moral que les mitsvot rituelles. L'intégrité d'un Juif (yosher — יֹשֶׁר) "
    "est une sanctification du Nom de D. dans le monde.\n\n"
    "Comment pratiquer :\n"
    "• Soyez transparent dans les ventes, la facturation et les négociations.\n"
    "• En cas de doute, consultez un rabbin ou un conseiller de confiance sur un contrat ou une pratique.\n"
    "• Traitez employés, clients et employeurs avec la dignité exigée par la Torah.\n\n"
    "Cochez ceci comme intention quotidienne d'agir avec honnêteté dans toutes vos interactions "
    "financières et professionnelles."
)

PRAYING_ES = (
    "¡Descubre el increíble poder de orar por todos! 🙏 Cuando te diriges a Hashem en tefilá (oración) "
    "pidiendo bendiciones, salud, parnasá (sustento) o cualquier cosa buena para todo el pueblo de Israel "
    "(Am Israel) —y no solo para ti— los Sabios enseñan que tu oración tiene más probabilidades de ser escuchada. "
    "Es como un ganar-ganar espiritual: te ayudas a ti mismo al ayudar a todos. Además, ¿quién no querría "
    "vivir en un mundo donde nadie está estresado? La próxima vez que necesites bendiciones, amplía tu enfoque "
    "y ora por toda la comunidad."
)

PRAYING_FR = (
    "Découvrez l'incroyable pouvoir de prier pour tout le monde ! 🙏 Quand vous priez (daven) D. pour des "
    "bénédictions, la santé et la parnassa (subsistance), ou pour tout bien pour tout Am Israël "
    "(le peuple juif) — et pas seulement pour vous — les Sages enseignent que votre prière a plus de chances "
    "d'être exaucée ! C'est comme un gagnant-gagnant spirituel : vous vous aidez en aidant tout le monde. "
    "Qui ne voudrait pas vivre dans un monde où personne n'est stressé ? La prochaine fois que vous avez "
    "besoin de bénédictions, élargissez votre champ de prière et priez pour toute la communauté !"
)

TEKIAH_ES = (
    "Tekiah es un toque largo y recto de shofar. La secuencia de Rosh Hashaná combina tekiah con shevarim "
    "(quebrado) y teruah (tembloroso) según el minhag. Cien toques es costumbre ashkenazí. "
    "Practica en la shul antes para que Rosh Hashaná no sea tu primera vez escuchando el shofar."
)

SHOFAR_ES = (
    "¡Prepárate para un despertar! 🎺 Explora la increíble mitzvá de tocar el shofar en Rosh Hashaná. "
    "No es solo hacer ruido: es una poderosa alarma espiritual para el alma, que nos impulsa a la "
    "introspección y al teshuvá (arrepentimiento). Cada sonido tiene un significado profundo: Tekiá es un "
    "toque largo e ininterrumpido, que simboliza claridad, certeza y nuestra conexión unificada con Hashem. "
    "Shevarim son tres toques cortos y quebrados, como un suspiro o llanto, que expresan nuestra fragilidad "
    "y arrepentimiento sincero. Teruá es una serie de al menos nueve toques rápidos, como sollozos, que "
    "significan una súplica desesperada por misericordia divina. La secuencia (Tekiá-Shevarim-Teruá-Tekiá, "
    "etc.) es crucial. Imagina estos sonidos antiguos en el Día del Juicio, despertando tu alma para volver "
    "a tu Creador. Fuente: Números 29:1; Talmud, Rosh Hashaná 33b; Shulján Aruj, Oraj Jaim 590."
)

ONA_AH_PREFIX_KEY = next(k for k in CATALOG if k.startswith("ona'ah — Overcharging"))
ONA_AH_BODY_KEY = next(k for k in CATALOG if k.startswith("Overcharging or underpaying when the other"))
MISHEYAKIR_KEY = next(k for k in CATALOG if k.startswith("misheyakir — Misheyakir"))
ARBA_MINIM_KEY = next(
    (
        k for k in CATALOG
        if k.startswith("Arba Minim (ארבעה מינים) — the Four Species")
        and "Men —" not in k
        and "Women —" not in k
    ),
    None,
)
JEWISH_LAW_KEY = next(k for k in CATALOG if k.startswith("Jewish law applies in the marketplace"))
PRAYING_KEY = next(k for k in CATALOG if k.startswith("Learn about the incredible power of praying"))
TEKIAH_KEY = next(k for k in CATALOG if k.startswith("Tekiah is a long straight shofar"))
SHOFAR_KEY = next(k for k in CATALOG if k.startswith("Get ready for a wake-up call!"))

BATCH18_ES: dict[str, str] = {
    k: v
    for k, v in {
        ONA_AH_PREFIX_KEY: f"ona'ah — {ONA_AH_BODY_ES}",
        ONA_AH_BODY_KEY: ONA_AH_BODY_ES,
        MISHEYAKIR_KEY: MISHEYAKIR_ES,
        ARBA_MINIM_KEY: ARBA_MINIM_ES,
        JEWISH_LAW_KEY: JEWISH_LAW_ES,
        PRAYING_KEY: PRAYING_ES,
        TEKIAH_KEY: TEKIAH_ES,
        SHOFAR_KEY: SHOFAR_ES,
    }.items()
    if k is not None
}

BATCH18_FR: dict[str, str] = {
    k: v
    for k, v in {
        ONA_AH_PREFIX_KEY: f"Ona'ah — {ONA_AH_BODY_FR}",
        ONA_AH_BODY_KEY: ONA_AH_BODY_FR,
        MISHEYAKIR_KEY: MISHEYAKIR_FR,
        ARBA_MINIM_KEY: ARBA_MINIM_FR,
        JEWISH_LAW_KEY: JEWISH_LAW_FR,
        PRAYING_KEY: PRAYING_FR,
    }.items()
    if k is not None
}
