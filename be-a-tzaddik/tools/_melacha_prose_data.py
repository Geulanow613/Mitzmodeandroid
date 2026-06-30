"""Concise native ES/FR for Shabbat melacha 'Learn about…' explainers."""
from __future__ import annotations

# (english_key_prefix, es, fr)
MELACHA_POLISH: list[tuple[str, str, str]] = [
    (
        "Learn about the Melacha of Boneh",
        "Boneh — construir o ensamblar en Shabat. En el Mishkán se erguían paneles y encajes del Tabernáculo. "
        "Prohibido crear o mejorar estructuras, ensamblar muebles de forma permanente o reparar piezas sueltas con intención constructiva. "
        "Ejemplos prácticos: no montar una tienda de campaña permanente, no atornillar un estante que se desprendió. "
        "En Shabat descansamos de dar forma estable al mundo físico.",
        "Boneh — construire ou assembler le Chabbat. Au Mishkan on élevait les panneaux et les emboîtements du Tabernacle. "
        "Interdit de créer ou améliorer des structures, d'assembler des meubles de façon permanente ou de réparer des pièces détachées avec intention constructive. "
        "Exemples : ne pas monter une tente permanente, ne pas revisser une étagère tombée. "
        "Le Chabbat, on cesse de donner une forme stable au monde matériel.",
    ),
    (
        "Learn about the Melacha of Hotza'ah",
        "Hotza'ah — trasladar objetos entre dominios en Shabat. En el Mishkán el pueblo dejó de llevar materiales de las tiendas al campamento común. "
        "Prohibido pasar de reshut hayájid a reshur harabim (o cuatro amot en vía pública) sin eruv. "
        "Es la melajá más visible en la vida comunitaria: llaves, comida, niños — todo depende del eruv y de las reglas locales. "
        "Consulta a tu rav sobre tu barrio.",
        "Hotza'ah — transporter des objets entre domaines le Chabbat. Au Mishkan le peuple cessa d'apporter des matériaux des tentes au camp commun. "
        "Interdit de passer de reshout hayahid à reshout harabim (ou quatre amot en domaine public) sans eruv. "
        "Mitsva la plus visible en communauté : clés, nourriture, enfants — tout dépend de l'eruv local. "
        "Demandez à votre rav pour votre quartier.",
    ),
    (
        "Learn about the Melacha of Koreah",
        "Koreah — rasgar con propósito constructivo en Shabat. En el Mishkán se deshacían telas mal tejidas para rehacerlas. "
        "Prohibido abrir sobres a lo largo del sello, rasgar tela para hacer un trapo o romper empaques con intención útil. "
        "Rasgar sin beneficio puede ser rabínico; rasgar para mejorar o preparar — prohibición toráica. "
        "En Shabat no «reformamos» materiales.",
        "Koreah — déchirer avec un but constructif le Chabbat. Au Mishkan on défaisait des tissus mal tissés pour les refaire. "
        "Interdit d'ouvrir une enveloppe le long du sceau, de déchirer un tissu pour faire un chiffon ou de casser un emballage avec intention utile. "
        "Déchirer sans bénéfice peut être rabbinique ; déchirer pour améliorer — interdiction toranique. "
        "Le Chabbat, on ne « remodèle » pas les matériaux.",
    ),
    (
        "Learn about the Melacha of Koteiv",
        "Koteiv — escribir en Shabat. En el Mishkán se marcaban las vigas con letras para emparejarlas al desmontar. "
        "Prohibido formar dos letras o símbolos con significado, incluso temporalmente. "
        "Los Sabios prohíben también escribir con dedo en el polvo o alinear letras rotas de un libro. "
        "Evita notas, dibujos con letras y apps que «escriben» en pantalla según tu posek.",
        "Koteiv — écrire le Chabbat. Au Mishkan on marquait les poutres avec des lettres pour les réassembler au démontage. "
        "Interdit de former deux lettres ou symboles significatifs, même temporairement. "
        "Les Sages interdisent aussi d'écrire du doigt dans la poussière ou de réaligner des lettres déchirées. "
        "Évitez notes, dessins avec lettres et apps qui « écrivent » à l'écran selon votre posek.",
    ),
    (
        "Learn about the Melacha of Mafshit",
        "Mafshit — desollar pieles en Shabat. En el Mishkán se separaban cueros de animales para las cortinas. "
        "Prohibido desollar, pelar cuero con fines productivos o separar piel de carne de forma industrial. "
        "En la vida diaria es poco común, pero recuerda: Shabat pausa toda cadena de procesamiento de materiales.",
        "Mafshit — dépouiller des peaux le Chabbat. Au Mishkan on séparait les cuirs des animaux pour les tentures. "
        "Interdit d'écorcer, de peler du cuir à des fins productives ou de séparer peau et chair de façon industrielle. "
        "Rare au quotidien, mais le Chabbat suspend toute chaîne de transformation des matériaux.",
    ),
    (
        "Learn about the Melacha of Makeh B'patish",
        "Makeh B'patish — el «último martillazo» que completa un objeto en Shabat. En el Mishkán se daba el golpe final al metal. "
        "Prohibido el acto final que hace usable algo incompleto: cortar un hilo suelante «para terminar», enderezar una cuchara doblada, "
        "encajar un tornillo que estabiliza un mueble. En Shabat no damos el toque final de artesanía.",
        "Makeh B'patish — le « dernier coup de marteau » qui achève un objet le Chabbat. Au Mishkan on frappait le métal une dernière fois. "
        "Interdit l'acte final rendant utilisable quelque chose d'incomplet : couper un fil pendant, redresser une cuillère, "
        "visser pour stabiliser un meuble. Le Chabbat, pas de touche finale d'artisanat.",
    ),
    (
        "Learn about the Melacha of Matir",
        "Matir — desatar nudos en Shabat. Contrario de Koshair: en el Mishkán se desataban cuerdas al trasladar el campamento. "
        "Un nudo toráico de atar es toráico de desatar; uno rabínico, igual al desatar. "
        "No deshagas nudos profesionales o pensados para durar; los corbatas y lazos simples suelen estar permitidos según el caso — pregunta a tu rav.",
        "Matir — défaire des nœuds le Chabbat. Contraire de Koshair : au Mishkan on dénouait les cordes au déplacement du camp. "
        "Nœud toranique à faire l'est à défaire ; rabbinique, idem. "
        "Ne défaire pas de nœuds professionnels ou durables ; cravates et lacets simples souvent permis — demandez à votre rav.",
    ),
    (
        "Learn about the Melacha of Mav'ir",
        "Mav'ir — encender o avivar fuego en Shabat. La Torá lo menciona explícitamente; en el Mishkán se encendían hornos y fraguas. "
        "Prohibido crear llama, chispa o calor intencional: cerillas, mecheros, subir un quemador, encender luces según las reglas eléctricas de tu comunidad. "
        "Deja las luces encendidas antes de Shabat; usa temporizadores y placa caliente según tu rav.",
        "Mav'ir — allumer ou attiser le feu le Chabbat. La Torah le mentionne explicitement ; au Mishkan on allumait fours et forges. "
        "Interdit de créer flamme, étincelle ou chaleur intentionnelle : allumettes, briquets, monter un brûleur, allumer selon les règles électriques de votre communauté. "
        "Laissez les lumières allumées avant Chabbat ; minuteurs et plaque chauffante selon votre rav.",
    ),
    (
        "Learn about the Melacha of Me'abeid",
        "Me'abeid — curtir y preservar materiales en Shabat. En el Mishkán se trataban pieles con sal y aceites. "
        "Prohibido curtir cuero, ablandar materiales crudos o preservarlos químicamente. "
        "Derivado práctico — Molei'aj (salado): salar en exceso para conservar puede estar prohibido; salar comida para sabor suele estar permitido. "
        "Pregunta en casos de encurtidos y marinados.",
        "Me'abeid — tanner et préserver des matériaux le Chabbat. Au Mishkan on traitait les peaux avec sel et huiles. "
        "Interdit de tanner, d'assouplir des matières brutes ou de les conserver chimiquement. "
        "Dérivé pratique — Moleiah (salage) : saler pour conserver peut être interdit ; saler pour le goût est en général permis. "
        "Demandez pour marinades et conserves.",
    ),
    (
        "Learn about the Melacha of Mechabeh",
        "Mechabeh — apagar fuego en Shabat. En el Mishkán se apagaban brasas para hacer carbón o controlar hornos. "
        "Prohibido apagar, atenuar o sofocar llama con propósito — incluso soplar una vela rabínicamente. "
        "Si una vela arde de más, consulta a tu rav; no la apagues tú mismo sin necesidad.",
        "Mechabeh — éteindre le feu le Chabbat. Au Mishkan on éteignait des braises pour faire du charbon ou régler les fours. "
        "Interdit d'éteindre, diminuer ou étouffer une flamme intentionnellement — même souffler une bougie est rabbinique. "
        "Si une bougie brûle trop, demandez à votre rav ; ne l'éteignez pas vous-même sans nécessité.",
    ),
    (
        "Learn about the Melacha of Mechateich",
        "Mechateich — cortar a medida en Shabat. En el Mishkán se cortaban telas y vigas a dimensiones exactas. "
        "Prohibido recortar un objeto a tamaño o forma específicos con intención productiva: hilo al borde, plástico a medida, madera a tamaño. "
        "Rasgar envoltorio sin medir suele ser otro melajá (Koreah); cortar con regla — Mechateich.",
        "Mechateich — couper à mesure le Chabbat. Au Mishkan on coupait tissus et poutres aux dimensions exactes. "
        "Interdit de tailler un objet à taille ou forme précise avec intention productive : fil au bord, plastique à mesure, bois sur mesure. "
        "Déchirer un emballage sans mesurer relève souvent de Koreah ; couper avec règle — Mechateich.",
    ),
    (
        "Learn about the Melacha of Melaben",
        "Melaben — lavar y blanquear telas en Shabat. En el Mishkán se lavaba lana y pelo de cabra antes de hilar. "
        "Va mucho más allá de la lavadora: limpiar una mancha fresca frotando con agua e intención de lavar puede violar Melaben. "
        "Incluso mojar una prenda sucia puede iniciar el proceso. En Shabat usamos ropa limpia preparada antes; manchas — consulta a tu rav.",
        "Melaben — laver et blanchir des tissus le Chabbat. Au Mishkan on lavait laine et poils de chèvre avant le filage. "
        "Bien au-delà de la machine : enlever une tache fraîche en frottant avec de l'eau et intention de laver peut violer Melaben. "
        "Mouiller un vêtement sale peut commencer le processus. Le Chabbat, vêtements propres préparés avant ; taches — demandez à votre rav.",
    ),
    (
        "Learn about the Melacha of Memachek",
        "Memachek — alisar y raspar superficies en Shabat. En el Mishkán se raspaban pieles hasta quedar uniformes. "
        "Prohibido lijar, pulir para mejorar textura o usar jabón/pasta de dientes de forma que «alisas» sólido sobre sólido según muchos poskim. "
        "En Shabat tratamos el cuerpo con suavidad; pregunta sobre cremas espesas y bloques de desodorante.",
        "Memachek — lisser et gratter des surfaces le Chabbat. Au Mishkan on raclait les peaux jusqu'à uniformité. "
        "Interdit de poncer, polir pour améliorer la texture ou d'utiliser savon/pâte dentifrice de façon à « lisser » solide sur solide selon beaucoup de poskim. "
        "Le Chabbat, douceur pour le corps ; demandez pour crèmes épaisses et déodorant solide.",
    ),
    (
        "Learn about the Melacha of Menapetz",
        "Menapetz — peinar y desenredar fibras en Shabat. En el Mishkán se cardaban lana y lino antes de hilar. "
        "Prohibido desenredar materia prima industrialmente. En aseo personal: cepillado fuerte que arranca pelo toca Gozez; "
        "usa cepillo suave y desenredante con cuidado. Shabat invita a tratar cuerpo y materiales con gentileza.",
        "Menapetz — peigner et démêler des fibres le Chabbat. Au Mishkan on cardait laine et lin avant le filage. "
        "Interdit de démêler une matière première de façon industrielle. Pour le corps : brossage dur arrachant des cheveux touche Gozez ; "
        "brosse douce et démêlant avec précaution. Le Chabbat invite à la douceur envers le corps et les matériaux.",
    ),
    (
        "Learn about the Melacha of Mesarteit",
        "Mesarteit — marcar guías en Shabat. En el Mishkán se trazaban líneas en pieles y telas antes de cortar. "
        "Prohibido hacer marcas que guíen un trabajo futuro: líneas para cortar, puntos de referencia para coser. "
        "Dibujar sin propósito constructivo puede ser rabínico — evita planificar proyectos con trazos el Shabat.",
        "Mesarteit — marquer des guides le Chabbat. Au Mishkan on traçait des lignes sur peaux et tissus avant la coupe. "
        "Interdit de faire des marques guidant un travail futur : lignes pour couper, repères pour coudre. "
        "Dessiner sans but constructif peut être rabbinique — évitez de planifier des projets par des traits le Chabbat.",
    ),
    (
        "Learn about the Melacha of Mocheik",
        "Mocheik — borrar marcas significativas en Shabat. En el Mishkán los escribas corregían errores en las vigas marcadas. "
        "Prohibido borrar letras, tachar texto o destruir registros. Ashkenazim evitan rasgar letras impresas en envases; "
        "sefardíes según Rav Ovadia Yosef permiten para comida y medicinas sin intención de borrar.",
        "Mocheik — effacer des signes significatifs le Chabbat. Au Mishkan les scribes corrigeaient les erreurs sur les poutres marquées. "
        "Interdit d'effacer des lettres, rayer du texte ou détruire des écrits. Ashkénazes évitent de déchirer des lettres imprimées ; "
        "séfarades selon Rav Ovadia Yosef permettent pour nourriture et médicaments sans intention d'effacer.",
    ),
    (
        "Learn about the Melacha of Oreg",
        "Oreg — tejer en Shabat. Entrelazar hilos en el Mishkán. Prohibidos telar, zurcir con hilos nuevos, "
        "retirar un hilo al tejido. Incluso dos hilos en una malla — violación (Talmud). En Shabat no creamos tela — "
        "disfrutamos lo ya hecho.",
        "Oreg — tisser le Chabbat. Entrelacer des fils au Mishkan. Interdits : métier à tisser, repriser avec de nouveaux fils, "
        "réintégrer un fil au tissu. Même deux fils dans une grille — violation (Talmud). Le Chabbat, on ne fabrique pas le tissu — "
        "on savoure ce qui est déjà fait.",
    ),
    (
        "Learn about the Melacha of Oseh Batei Nirin",
        "Oseh Batei Nirin — preparar pasadores y guías del telar en Shabat. En el Mishkán se montaban los nirin del telar para tejer cortinas. "
        "Prohibido armar o ajustar mecanismos de telar, pasar hilos por pasadores o preparar cualquier guía que habilite tejido. "
        "Parte de la cadena textil prohibida junto con Oreg y Menapetz.",
        "Oseh Batei Nirin — préparer les lisses et guides du métier à tisser le Chabbat. Au Mishkan on montait les nirin pour tisser les tentures. "
        "Interdit d'assembler ou régler un métier, d'enfiler des fils dans des lisses ou de préparer tout guide permettant le tissage. "
        "Partie de la chaîne textile interdite avec Oreg et Menapetz.",
    ),
    (
        "Learn about the Melacha of Potze'a",
        "Potze'a — abrir caminos o desbrozar en Shabat. En el Mishkán se allanaba terreno para transportar materiales. "
        "Prohibido abrir sendas nuevas, despejar obstáculos con intención de mejorar un camino o preparar terreno para cultivo. "
        "Arrastrar muebles pesados por tierra blanda puede tocar Choresh — ten cuidado en jardines.",
        "Potze'a — ouvrir des chemins ou défricher le Chabbat. Au Mishkan on aplanissait le sol pour transporter des matériaux. "
        "Interdit d'ouvrir de nouveaux sentiers, d'enlever des obstacles pour améliorer un chemin ou de préparer un terrain pour la culture. "
        "Traîner des meubles lourds sur terre meuble peut toucher Choresh — prudence dans les jardins.",
    ),
    (
        "Learn about the Melacha of Shochait",
        "Shochait — sacrificar o matar animales en Shabat. En el Mishkán se degollaban animales para ofrendas y pieles. "
        "Prohibido matar cualquier criatura viva, incluso insectos, salvo peligro real según halajá. "
        "No mates moscas ni mosquitos por deporte; en caso de picadura peligrosa — consulta a tu rav.",
        "Shochait — abattre ou tuer des animaux le Chabbat. Au Mishkan on égorgeait des animaux pour offrandes et peaux. "
        "Interdit de tuer toute créature vivante, même insectes, sauf danger réel selon la halakha. "
        "Ne tuez pas mouches ou moustiques par jeu ; en cas de piqûre dangereuse — demandez à votre rav.",
    ),
    (
        "Learn about the Melacha of Soter",
        "Soter — demoler o desmontar en Shabat. Contrario de Boneh: en el Mishkán se desarmaba el Tabernáculo al viajar. "
        "Prohibido desmontar estructuras con intención constructiva futura, romper algo ensamblado de forma permanente o abrir aparatos sellados. "
        "Quitar tapa de frasco atornillado puede ser Soter — pregunta en cocina y herramientas.",
        "Soter — démolir ou démonter le Chabbat. Contraire de Boneh : au Mishkan on démontait le Tabernacle en voyageant. "
        "Interdit de démonter des structures pour reconstruction future, de casser un assemblage permanent ou d'ouvrir des appareils scellés. "
        "Ouvrir un bocal vissé peut être Soter — demandez pour cuisine et outils.",
    ),
    (
        "Learn about the Melacha of Tofair",
        "Tofair — coser en Shabat. En el Mishkán se unían piezas de tela y cuero con puntadas duraderas. "
        "Prohibido coser dos piezas de forma permanente, enlazar puntos o reparar ropa con aguja e hilo. "
        "Un hilo suelte puede atarse temporalmente de forma muy limitada según casos — pregunta a tu rav; no hagas «arreglos de verdad».",
        "Tofair — coudre le Chabbat. Au Mishkan on joignait tissus et cuirs par des points durables. "
        "Interdit de coudre deux pièces de façon permanente, d'enchaîner des points ou de réparer un vêtement avec aiguille et fil. "
        "Un fil pendant peut parfois se nouer très temporairement — demandez à votre rav ; pas de « vraie réparation ».",
    ),
    (
        "Learn about the Melacha of Tzad",
        "Tzad — atrapar animales vivos en Shabat. En el Mishkán se cazaban animales para pieles y ofrendas. "
        "Prohibido cazar, atrapar peces, matar insectos no peligrosos o cerrar una jaula sobre un pájaro que acaba de entrar. "
        "Mata un animal peligroso solo si hay riesgo real — pikuaj nefesh. Mascotas: no las atrapes de forma lúdica el Shabat.",
        "Tzad — capturer des animaux vivants le Chabbat. Au Mishkan on chassait pour peaux et offrandes. "
        "Interdit de chasser, pêcher, tuer des insectes non dangereux ou de fermer une cage sur un oiseau qui vient d'entrer. "
        "Tuer un animal dangereux seulement s'il y a un vrai risque — pikuah nefesh. Animaux : pas de capture ludique le Chabbat.",
    ),
]

MISC_PROSE_POLISH: list[tuple[str, str, str]] = [
    (
        "Learn about Bishul",
        "Bishul — cocinar en Shabat. En el Mishkán se hervían hierbas para tintes y se horneaba el pan de la proposición. "
        "Prohibido cocer, hornear o freír alimentos crudos con calor de yad soledet bo (donde la mano se retira). "
        "Ein bishul ajar bishul: recalentar sólidos ya cocidos suele estar permitido; líquidos fríos — prohibido. "
        "Leyes muy detalladas — aprende con tu rav.",
        "Bishul — cuisiner le Chabbat. Au Mishkan on faisait bouillir des herbes pour teintures et on cuisait le pain de proposition. "
        "Interdit de cuire, griller ou frire des aliments crus avec une chaleur de yad soledet bo (où la main se retire). "
        "Ein bishul ahar bishul : réchauffer des solides déjà cuits est en général permis ; liquides froids — interdit. "
        "Lois très détaillées — étudiez avec votre rav.",
    ),
    (
        "Learn about Kotzer",
        "Kotzer — cosechar y separar del crecimiento en Shabat. En el Mishkán se cortaba trigo y hierbas. "
        "Prohibido arrancar fruta, flores o hierbas — incluso de una maceta en la ventana. "
        "Los Sabios prohíben usar un árbol directamente (no trepar, no colgar hamaca). "
        "Shabat enseña a disfrutar la creación sin arrancarla constantemente.",
        "Kotzer — moissonner et détacher de la croissance le Chabbat. Au Mishkan on coupait blé et herbes. "
        "Interdit de cueillir fruits, fleurs ou herbes — même d'un pot sur le rebord. "
        "Les Sages interdisent d'utiliser un arbre directement (pas grimper, pas de hamac). "
        "Le Chabbat apprend à savourer la création sans la cueillir sans cesse.",
    ),
    (
        "Chol HaMoed are the intermediate",
        "Jol hamoed son los días intermedios entre el primer y último día de Pesaj o Sucot. Siguen siendo fiesta, "
        "pero las reglas de melajá son más ligeras que en Yom Tov o Shabat: mucho trabajo y cocina están permitidos, "
        "con límites en comercio y algunas melajot. Afeitado y cortes de pelo están prohibidos (O.J. 531) salvo excepciones — consulta a tu rav.",
        "Hol hamoed sont les jours intermédiaires entre le premier et le dernier jour de Pessah ou Souccot. "
        "Ce reste une fête, mais les règles de melacha sont plus légères qu'à Yom Tov ou Chabbat : beaucoup de travail et cuisine permis, "
        "avec limites commerciales et certaines melachot. Rasage et coupe interdits (O.H. 531) sauf exceptions — demandez à votre rav.",
    ),
    (
        "Chol HaMoed — Chol HaMoed are the intermediate",
        "Jol hamoed — jol hamoed son los días intermedios entre el primer y último día de Pesaj o Sucot. Siguen siendo fiesta, "
        "pero las reglas de melajá son más ligeras que en Yom Tov o Shabat: mucho trabajo y cocina están permitidos, "
        "con límites en comercio y algunas melajot. Afeitado y cortes de pelo están prohibidos (O.J. 531) salvo excepciones — consulta a tu rav.",
        "Hol hamoed — hol hamoed sont les jours intermédiaires entre le premier et le dernier jour de Pessah ou Souccot. "
        "Ce reste une fête, mais les règles de melacha sont plus légères qu'à Yom Tov ou Chabbat : beaucoup de travail et cuisine permis, "
        "avec limites commerciales et certaines melachot. Rasage et coupe interdits (O.H. 531) sauf exceptions — demandez à votre rav.",
    ),
    (
        "Learn about a surprising ruling: answering Amen",
        "Un fallo sorprendente: responder Amén puede ser más importante que quien recita la berajá (Berajot 53b). "
        "Rabí Yose enseñó que quien responde Amén es mayor — Amén afirma que lo dicho es verdad. "
        "El Talmud compara al que bendice con el que responde: el segundo refuerza la victoria. "
        "Amén = El Melej Ne'eman — D-s, Rey fiel. Di Amén con intención.",
        "Un arrêt surprenant : répondre Amen peut être plus important que celui qui récite la bénédiction (Berakhot 53b). "
        "Rabbi Yosse enseigne que celui qui répond Amen est plus grand — Amen affirme que ce qui est dit est vrai. "
        "Le Talmud compare celui qui bénit à celui qui répond : le second renforce la victoire. "
        "Amen = El Melekh Ne'eman — D.ieu, Roi fidèle. Dites Amen avec intention.",
    ),
    (
        "Learn about proper bowing on Rosh Hashanah",
        "Inclinación adecuada en Rosh Hashaná y Yom Kipur: no ponemos la cara directamente en suelo de piedra. "
        "Colocamos papel, tela o estera entre rostro y suelo. El Rambam explica: postración completa en piedra "
        "se asociaba con idolatría fuera del Templo. Incluso al inclinarnos ante D-s, somos conscientes del cómo.",
        "Inclinaison correcte à Rosh Hashana et Yom Kippour : on ne pose pas le visage directement sur pierre. "
        "On place papier, tissu ou natte entre le visage et le sol. Le Rambam explique : la prosternation complète sur pierre "
        "était associée à l'idolâtrie hors du Temple. Même en s'inclinant devant D.ieu, on reste attentif au comment.",
    ),
    (
        "Yom HaZikaron (4 Iyar)",
        "Yom HaZikaron (4 Iyar) es el día nacional de recuerdo de Israel por los soldados caídos y víctimas del terrorismo. "
        "Instituido por la Knéset en 1963; siempre precede a Yom HaAtzmaut. No es Yom Tov — la melajá está permitida; es observancia civil. "
        "La aplicación ajusta la fecha cuando 4 Iyar cae cerca de Shabat para mantener Yom HaZikaron y Yom HaAtzmaut en días consecutivos. "
        "En Israel: sirenas a las 20:00 (inicio del día, al anochecer) y a las 11:00; banderas a media asta. "
        "Datí leumi: Tachanun completo en Shajarit; omitido en Minjá al pasar a Yom HaAtzmaut (Peninei Halajá). "
        "Jaredí y Jabad: día civil, Tachanun todo el día. Termina al anochecer con el inicio de Yom HaAtzmaut.",
        "Yom HaZikaron (4 Iyar) est la journée nationale du souvenir en Israël pour les soldats tombés et les victimes du terrorisme. "
        "Instituée par la Knesset en 1963 ; précède toujours Yom HaAtsmaout. Ce n'est pas Yom Tov — melacha permise ; observance civile. "
        "L'application ajuste la date quand le 4 Iyar est proche de Chabbat pour garder Yom HaZikaron et Yom HaAtsmaout consécutifs. "
        "En Israël : sirènes à 20h (début du jour, à la tombée) et à 11h ; drapeaux en berne. "
        "Dati leumi : Tahanoun complet à Shaharit ; omis à Min'ha vers Yom HaAtsmaout (Peninei Halakha). "
        "Haredi et Habad : jour civil, Tahanoun toute la journée. Se termine à la tombée de la nuit avec Yom HaAtsmaout.",
    ),
    (
        "Learn about Choresh —",
        "Choresh — arar y preparar la tierra en Shabat. En el Mishkán se labraba para cultivar hierbas y trigo. "
        "Prohibido aflojar, nivelar o mejorar la tierra: arar, cavar en el jardín o preparar para plantar. "
        "No arrastres bancos o carriolas pesados por tierra blanda — puede abrir surcos. "
        "En Shabat no remodelamos la faz de la tierra.",
        "Choresh — labourer et préparer la terre le Chabbat. Au Mishkan on labourait pour herbes et blé. "
        "Interdit d'ameublir, niveler ou améliorer la terre : labourer, creuser le jardin ou préparer pour planter. "
        "Ne traînez pas bancs ou poussettes sur terre meuble — peut ouvrir des sillons. "
        "Le Chabbat, on ne remodèle pas la surface de la terre.",
    ),
    (
        "Learn about Gozez —",
        "Gozez — esquilar lana y pelo en Shabat. En el Mishkán se esquilaban ovejas para tapices. "
        "Prohibido cortar pelo humano, uñas, arrancar plumas firmemente unidas o desprender crecimiento vivo del cuerpo. "
        "Por eso peluquería y corte de uñas son entre semana (mitzvá prepararse antes de Shabat). "
        "Incluso morder uñas o arrancar piel suelta puede estar restringido.",
        "Gozez — tondre laine et poils le Chabbat. Au Mishkan on tondait les brebis pour tentures. "
        "Interdit de couper cheveux humains, ongles, arracher plumes fermement attachées ou détacher une croissance vivante du corps. "
        "D'où coiffure et ongles en semaine (mitzva de se préparer avant Chabbat). "
        "Même se ronger les ongles ou arracher peau peut être restreint.",
    ),
    (
        "Learn about Zoreh —",
        "Zoreh — aventar y dispersar en Shabat. En el Mishkán se lanzaba grano al aire para separar la paja. "
        "Prohibido usar viento o aliento para separar mezclas; no sacudir manteles fuera si el viento dispersa migas, "
        "ni soplar polvo de un libro. En Shabat no usamos el aire para clasificar ni esparcir.",
        "Zoreh — vanner et disperser le Chabbat. Au Mishkan on jetait le grain en l'air pour séparer la paille. "
        "Interdit d'utiliser vent ou souffle pour séparer des mélanges ; ne pas secouer nappes dehors si le vent disperse les miettes, "
        "ni souffler la poussière d'un livre. Le Chabbat, on n'utilise pas l'air pour trier ni disperser.",
    ),
    (
        "Learn about Oneg Shabbat!",
        "Oneg Shabat — disfrutar del Shabat es mitzvá, no solo costumbre agradable. "
        "Incluye comida festiva, descanso, estudio de Torá, oración y tiempo en familia. "
        "El Talmud (Shabat 118a) promete bendición a quien se deleita en Shabat. "
        "Cada gozo con intención de honrar el día es mitzvá.",
        "Oneg Chabbat — prendre plaisir au Chabbat est une mitsva, pas seulement une agréable coutume. "
        "Repas festifs, repos, étude de Torah, prière et temps en famille. "
        "Le Talmud (Chabbat 118a) promet bénédiction à qui se délecte du Chabbat. "
        "Chaque plaisir avec intention d'honorer le jour est mitsva.",
    ),
    (
        "Learn about Birkat Kohanim —",
        "Birkat Kohanim — la bendición sacerdotal (Bamidbar 6:24-26). Ashkenazim fuera de Israel: generalmente solo en Yom Tov; "
        "en Israel y sefardíes: a menudo cada día en Shajarit. Los kohanim suben al ducán, se cubren con el talit, "
        "extienden los dedos en el patrón tradicional y cantan los tres versículos; la congregación mira hacia abajo "
        "(Sotá 39a — la Presencia descansa sobre sus manos).",
        "Birkat Kohanim — la bénédiction sacerdotale (Bamidbar 6:24-26). Ashkénazes hors d'Israël : généralement seulement à Yom Tov ; "
        "en Israël et séfarades : souvent chaque jour à Shaharit. Les kohanim montent au duchan, se couvrent du talit, "
        "étendent les doigts selon la tradition et chantent les trois versets ; la congrégation baisse les yeux "
        "(Sotah 39a — la Présence repose sur leurs mains).",
    ),
    (
        "Learn about Kiddush Hashem and Chillul Hashem",
        "Kiddush Hashem — santificar el Nombre de D-s con integridad, paciencia y honradez; la gente dice "
        "«qué hermosa Torá enseña su maestro» (Rambam). Chillul Hashem — lo opuesto: conducta vergonzosa de alguien claramente religioso. "
        "Es de las prohibiciones más graves — pregunta a tu rav en casos concretos.",
        "Kiddush Hashem — sanctifier le Nom de D.ieu par intégrité, patience et honnêteté ; on dit "
        "« quelle belle Torah son maître enseigne » (Rambam). Hilloul Hashem — l'opposé : conduite honteuse d'une personne clairement religieuse. "
        "Parmi les interdictions les plus graves — demandez à votre rav dans les cas concrets.",
    ),
    (
        "Gather for inspiration!",
        "Hakhel — cada siete años, tras el shemitá, todo Israel se reunía para escuchar al rey leer la Torá (Devarim 31:12). "
        "Hombres, mujeres y niños — incluso bebés, para que los padres reciban mérito. "
        "Hoy: únete a una clase de Torá o reunión comunitaria, incluso en línea.",
        "Hakhel — tous les sept ans, après le shemita, tout Israël se rassemblait pour entendre le roi lire la Torah (Devarim 31:12). "
        "Hommes, femmes et enfants — même les bébés, pour que les parents reçoivent du mérite. "
        "Aujourd'hui : rejoignez un cours de Torah ou une assemblée, même en ligne.",
    ),
    (
        "Get creative with Mishloach Manot",
        "Mishloach manot — en Purim debes enviar al menos a un adulto judío dos tipos distintos de comida lista para comer "
        "(p. ej. galleta y zumo). Debe enviarse el día de Purim, idealmente por mensajero (mishloach). "
        "Piensa a quién alegrar el próximo Purim.",
        "Mishloach manot — à Pourim vous devez envoyer à au moins un adulte juif deux sortes distinctes de nourriture prête "
        "(ex. biscuit et jus). Doit être envoyé le jour de Pourim, idéalement par messager (mishloah). "
        "Pensez à qui réjouir au prochain Pourim.",
    ),
    (
        "Learn about Tefillin!",
        "Las tefilín — la caja de la cabeza contiene los mismos cuatro pasajes que la del brazo, pero dispuestos distinto. "
        "Unen mente, corazón y acción con D-s en la oración matutina. Aprende a ponértelas con tu rav.",
        "Les téfilines — la boîte de la tête contient les mêmes quatre passages que celle du bras, mais disposés différemment. "
        "Elles unissent esprit, cœur et action à D.ieu à la prière du matin. Apprenez à les poser avec votre rav.",
    ),
    (
        "Connect to our future! Learn about the Beit HaMikdash",
        "El Beit HaMikdash — cada detalle de su estructura y servicio tiene significado espiritual profundo. "
        "Estudiar sus leyes y servicios es como cumplirlos en cierta medida (Talmud). "
        "Ese estudio aclara muchas otras partes de la Torá.",
        "Le Beit HaMikdash — chaque détail de sa structure et de son service a un sens spirituel profond. "
        "Étudier ses lois et services équivaut en partie à les accomplir (Talmud). "
        "Cette étude éclaire bien d'autres parties de la Torah.",
    ),
    (
        "Embrace the power of the Shema!",
        "El Shemá — oración central, dos veces al día: proclamar la unidad de D-s y nuestro amor por Él. "
        "Contiene principios fundamentales de la fe en pocos versículos; los Sabios enseñan su poder protector. "
        "Reto: recítalo con intención, palabra por palabra.",
        "Le Shema — prière centrale, deux fois par jour : proclamer l'unité de D.ieu et notre amour pour Lui. "
        "Principes fondamentaux de la foi en quelques versets ; les Sages enseignent son pouvoir protecteur. "
        "Défi : récitez-le avec intention, mot à mot.",
    ),
    (
        "Experience the power of community prayer!",
        "Orar con minyan (diez hombres adultos) amplifica la oración. "
        "El Talmud enseña que D-s no rechaza la oración de la comunidad. "
        "Únete a un minyan cuando puedas.",
        "Prier avec un minyan (dix hommes adultes) amplifie la prière. "
        "Le Talmud enseigne que D.ieu ne rejette pas la prière de la communauté. "
        "Rejoignez un minyan quand vous le pouvez.",
    ),
]
