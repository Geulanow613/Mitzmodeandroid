"""Concise native ES/FR for Shabbat melacha 'Learn about…' explainers."""
from __future__ import annotations

# (english_key_prefix, es, fr)
MELACHA_POLISH: list[tuple[str, str, str]] = [
    (
        "Learn about the Melacha of Boneh",
        "¡Aprende sobre Boneh — construir o ensamblar en Shabat! 🏗️ En el Mishkán se erguían paneles y encajes del Tabernáculo. "
        "Prohibido crear o mejorar estructuras, ensamblar muebles de forma permanente o reparar piezas sueltas con intención constructiva. "
        "Ejemplos: no montar una tienda permanente, no atornillar un estante que se desprendió, no pegar algo que quedará fijo. "
        "En Shabat descansamos de dar forma estable al mundo físico — disfrutamos lo ya construido.",
        "Boneh — construire ou assembler le Chabbat ! 🏗️ Au Mishkan on élevait les panneaux du Tabernacle. "
        "Interdit de créer ou améliorer des structures, d'assembler des meubles de façon permanente ou de réparer avec intention constructive. "
        "Exemples : pas de tente permanente, pas de revisser une étagère tombée, pas de coller quelque chose de fixe. "
        "Le Chabbat, on cesse de donner une forme stable au monde matériel.",
    ),
    (
        "Learn about the Melacha of Hotza'ah",
        "Hotza'ah — trasladar objetos entre dominios en Shabat. En el Mishkán el pueblo dejó de llevar materiales de las tiendas al campamento común. "
        "Prohibido pasar de reshut hayájid a reshur harabim (o cuatro amot en vía pública) sin eruv. "
        "Es la melajá más visible en la vida comunitaria: llaves, comida, niños — todo depende del eruv y de las reglas locales. "
        "Consulta a tu rav sobre tu barrio. Sin eruv válido, llevar llaves o comida por la calle puede ser problema.",
        "Hotza'ah — transporter des objets entre domaines le Chabbat. Au Mishkan le peuple cessa d'apporter des matériaux des tentes au camp commun. "
        "Interdit de passer de reshout hayahid à reshout harabim (ou quatre amot en domaine public) sans eruv. "
        "Mitsva la plus visible en communauté : clés, nourriture, enfants — tout dépend de l'eruv local. "
        "Demande à ton rav pour ton quartier. Sans eruv valide, porter clés ou nourriture dans la rue peut poser problème.",
    ),
    (
        "Learn about the Melacha of Koreah",
        "Koreah — rasgar con propósito constructivo en Shabat. En el Mishkán se deshacían telas mal tejidas para rehacerlas. "
        "Prohibido abrir sobres a lo largo del sello, rasgar tela para hacer un trapo o romper empaques con intención útil. "
        "Rasgar sin beneficio puede ser rabínico; rasgar para mejorar o preparar — prohibición toráica. "
        "En Shabat no «reformamos» materiales. "
        "Abrir un sobre a lo largo del sello puede estar prohibido — pregunta a tu rav.",
        "Koreah — déchirer avec un but constructif le Chabbat. Au Mishkan on défaisait des tissus mal tissés pour les refaire. "
        "Interdit d'ouvrir une enveloppe le long du sceau, de déchirer un tissu pour faire un chiffon ou de casser un emballage avec intention utile. "
        "Déchirer sans bénéfice peut être rabbinique ; déchirer pour améliorer — interdiction toranique. "
        "Le Chabbat, on ne « remodèle » pas les matériaux. "
        "Ouvrir une enveloppe le long du sceau peut être interdit — demande à ton rav.",
    ),
    (
        "Learn about the Melacha of Koteiv",
        "Koteiv — escribir en Shabat. En el Mishkán se marcaban las vigas con letras para emparejarlas al desmontar. "
        "Prohibido formar dos letras o símbolos con significado, incluso temporalmente. "
        "Los Sabios prohíben también escribir con dedo en el polvo o alinear letras rotas de un libro. "
        "Évite notas, dibujos con letras y apps que «escriben» en pantalla según tu posek. "
        "Borrar letras en el polvo también puede ser problemático — el Shabat protege la escritura.",
        "Koteiv — écrire le Chabbat. Au Mishkan on marquait les poutres avec des lettres pour les réassembler au démontage. "
        "Interdit de former deux lettres ou symboles significatifs, même temporairement. "
        "Les Sages interdisent aussi d'écrire du doigt dans la poussière ou de réaligner des lettres déchirées. "
        "Évite les notes, dessins avec lettres et apps qui « écrivent » à l'écran selon ton posek. "
        "Même effacer des lettres dans la poussière peut poser problème — le Chabbat protège l'écrit.",
    ),
    (
        "Learn about the Melacha of Mafshit",
        "¡Aprende sobre Mafshit — desollar pieles en Shabat! En el Mishkán se separaban cueros de animales para cortinas y cubiertas. "
        "Prohibido desollar, pelar cuero con fines productivos o separar piel de carne de forma industrial. "
        "Pelar fruta para conservar o quitar cáscara con intención de cocinar puede tocar categorías relacionadas — pregunta a tu rav. "
        "En Shabat no procesamos materiales brutos como en un taller — disfrutamos lo ya preparado.",
        "Mafshit — dépouiller des peaux le Chabbat ! Au Mishkan on séparait les cuirs pour tentures et couvertures. "
        "Interdit d'écorcer ou de peler du cuir à des fins productives. "
        "Éplucher pour conserver peut toucher des catégories voisines — demande à ton rav. "
        "Le Chabbat suspend toute chaîne de transformation — on savoure ce qui est déjà prêt, pas un atelier de cuir. "
        "Éplucher pour manger tout de suite a d'autres règles — demande à ton rav.",
    ),
    (
        "Learn about the Melacha of Makeh B'patish",
        "Makeh B'patish — el «último martillazo» que completa un objeto en Shabat. En el Mishkán se daba el golpe final al metal. "
        "Prohibido el acto final que hace usable algo incompleto: cortar un hilo suelante «para terminar», enderezar una cuchara doblada, "
        "encajar un tornillo que estabiliza un mueble, quitar un clavo que sobresale. "
        "En Shabat no damos el toque final de artesanía — dejamos las reparaciones menores para después de Havdalá.",
        "Makeh B'patish — le « dernier coup de marteau » qui achève un objet le Chabbat. Au Mishkan on frappait le métal une dernière fois. "
        "Interdit l'acte final rendant utilisable quelque chose d'incomplet : couper un fil pendant, redresser une cuillère, "
        "visser pour stabiliser un meuble, retirer un clou qui dépasse. "
        "Le Chabbat, pas de touche finale d'artisanat — les petites réparations attendent après la Havdala.",
    ),
    (
        "Learn about the Melacha of Matir",
        "Matir — desatar nudos en Shabat. Contrario de Koshair: en el Mishkán se desataban cuerdas al trasladar el campamento. "
        "Un nudo toráico de atar es toráico de desatar; uno rabínico, igual al desatar. "
        "No deshagas nudos profesionales o pensados para durar; los cordones de zapatos y lazos simples suelen estar permitidos según el caso — pregunta a tu rav. "
        "En Shabat no «reorganizamos» ataduras pensadas para perdurar.",
        "Matir — défaire des nœuds le Chabbat. Contraire de Koshair : au Mishkan on dénouait les cordes au déplacement du camp. "
        "Nœud toranique à faire l'est à défaire ; rabbinique, idem. "
        "Ne défait pas de nœuds professionnels ou durables ; lacets et nœuds simples souvent permis — demande à ton rav. "
        "Le Chabbat, on ne « réorganise » pas des attaches faites pour durer. "
        "Un nœud que tu ne sais pas défaire — règle-le avant Chabbat.",
    ),
    (
        "Learn about the Melacha of Mav'ir",
        "Mav'ir — encender o avivar fuego en Shabat. La Torá lo menciona explícitamente; en el Mishkán se encendían hornos y fraguas. "
        "Prohibido crear llama, chispa o calor intencional: cerillas, mecheros, subir un quemador, encender luces según las reglas eléctricas de tu comunidad. "
        "Deja las luces encendidas antes de Shabat; usa temporizadores y placa caliente según tu rav. "
        "El Shabat enseña que no encendemos fuego nuevo — preparamos antes del anochecer.",
        "Mav'ir — allumer ou attiser le feu le Chabbat. La Torah le mentionne explicitement ; au Mishkan on allumait fours et forges. "
        "Interdit de créer flamme, étincelle ou chaleur intentionnelle : allumettes, briquets, monter un brûleur, allumer selon les règles électriques de ta communauté. "
        "Laisse les lumières allumées avant Chabbat ; minuteurs et plaque chauffante selon ton rav. "
        "Le Chabbat enseigne qu'on n'allume pas de nouveau feu — on prépare avant la nuit.",
    ),
    (
        "Learn about the Melacha of Me'abeid",
        "Me'abeid — curtir y preservar materiales en Shabat. En el Mishkán se trataban pieles con sal y aceites. "
        "Prohibido curtir cuero, ablandar materiales crudos o preservarlos químicamente. "
        "Derivado práctico — Molei'aj (salado): salar en exceso para conservar puede estar prohibido; salar comida para sabor suele estar permitido. "
        "Pregunta en casos de encurtidos y marinados el Shabat. "
        "Incluso ablandar cuero viejo o conservar con sal en exceso puede tocar Me'abeid — el Shabat no es día de curtido.",
        "Me'abeid — tanner et préserver des matériaux le Chabbat. Au Mishkan on traitait les peaux avec sel et huiles. "
        "Interdit de tanner, d'assouplir des matières brutes ou de les conserver chimiquement. "
        "Dérivé pratique — Moleiah (salage) : saler pour conserver peut être interdit ; saler pour le goût est en général permis. "
        "Marinades et conserves à Chabbat — demande à ton rav avant de saler pour garder longtemps.",
    ),
    (
        "Learn about the Melacha of Mechabeh",
        "¡Aprende sobre Mechabeh — apagar fuego en Shabat! 🔥 En el Mishkán los artesanos apagaban brasas para hacer carbón o controlar hornos. "
        "Prohibido apagar, atenuar o sofocar llama con intención — incluso soplar una vela es prohibición rabínica. "
        "Si una vela arde de más o hay peligro, pregunta a tu rav; no apagues por comodidad. "
        "Shabat enseña que no dominamos el fuego como en los días laborables — "
        "preparamos antes del anochecer y dejamos que las llamas sigan su curso según la halajá.",
        "Mechabeh — éteindre le feu à Chabbat ! 🔥 Au Mishkan on éteignait des braises pour le charbon ou les fours. "
        "Interdit d'éteindre, diminuer ou étouffer une flamme intentionnellement — souffler une bougie est rabbinique. "
        "Si une bougie brûle trop ou danger réel, demande à ton rav — pas pour le confort. "
        "Le Chabbat apprend qu'on ne maîtrise pas le feu comme en semaine ; on laisse brûler selon les règles.",
    ),
    (
        "Learn about the Melacha of Mechateich",
        "¡Aprende sobre Mechateich — cortar a medida en Shabat! En el Mishkán se cortaban telas y vigas a dimensiones exactas. "
        "Prohibido recortar un objeto a tamaño o forma específicos con intención productiva: hilo al borde, plástico a medida, madera a tamaño. "
        "Rasgar envoltorio sin medir suele ser otro melajá (Koreah); cortar con regla o tijeras a medida — Mechateich. "
        "Abrir yogur siguiendo la línea de perforación puede ser otro tema — pregunta a tu rav.",
        "Mechateich — couper à mesure le Chabbat. Au Mishkan on coupait tissus et poutres aux dimensions exactes. "
        "Interdit de tailler un objet à taille ou forme précise avec intention productive : fil au bord, plastique à mesure, bois sur mesure. "
        "Déchirer un emballage sans mesurer relève souvent de Koreah ; couper avec règle — Mechateich. "
        "Ouvrir un yaourt le long de la perforation peut poser d'autres questions — demande à ton rav.",
    ),
    (
        "Learn about the Melacha of Melaben",
        "Melaben — lavar y blanquear telas en Shabat. En el Mishkán se lavaba lana y pelo de cabra antes de hilar. "
        "Va mucho más allá de la lavadora: limpiar una mancha fresca frotando con agua e intención de lavar puede violar Melaben. "
        "Incluso mojar una prenda sucia puede iniciar el proceso. En Shabat usamos ropa limpia preparada antes; manchas — consulta a tu rav. "
        "El blanqueado químico o frotar fuerte para quitar manchas cae bajo esta categoría.",
        "Melaben — laver et blanchir des tissus le Chabbat. Au Mishkan on lavait laine et poils de chèvre avant le filage. "
        "Bien au-delà de la machine : enlever une tache fraîche en frottant avec de l'eau et intention de laver peut violer Melaben. "
        "Mouiller un vêtement sale peut commencer le processus. Le Chabbat, vêtements propres préparés avant ; taches — demande à ton rav. "
        "Blanchiment chimique ou frottage fort pour enlever une tache entre dans cette catégorie.",
    ),
    (
        "Learn about the Melacha of Memachek",
        "Memachek — alisar y raspar superficies en Shabat. En el Mishkán se raspaban pieles hasta quedar uniformes. "
        "Prohibido lijar, pulir para mejorar textura o usar jabón/pasta de dientes de forma que «alisas» sólido sobre sólido según muchos poskim. "
        "En Shabat tratamos el cuerpo con suavidad; pregunta sobre cremas espesas y bloques de desodorante. "
        "Raspar hielo o nieve con intención de nivelar también puede tocar Memachek — evita «pulir» superficies el Shabat.",
        "Memachek — lisser et gratter des surfaces le Chabbat. Au Mishkan on raclait les peaux jusqu'à uniformité. "
        "Interdit de poncer, polir pour améliorer la texture ou d'utiliser savon/pâte dentifrice de façon à « lisser » solide sur solide selon beaucoup de poskim. "
        "Le Chabbat, douceur pour le corps ; demande pour crèmes épaisses et déodorant solide. "
        "Gratter glace ou neige pour aplanir peut aussi toucher Memachek — évite de « polir » des surfaces.",
    ),
    (
        "Learn about the Melacha of Menapetz",
        "Menapetz — peinar y desenredar fibras en Shabat. En el Mishkán se cardaban lana y lino antes de hilar. "
        "Prohibido desenredar materia prima industrialmente. En aseo personal: cepillado fuerte que arranca pelo toca Gozez; "
        "usa cepillo suave y desenredante con cuidado. Pasar peine por el cabello para desenredar está permitido con suavidad. "
        "Shabat invita a tratar cuerpo y materiales con gentileza, sin «industrializar» el cuidado personal.",
        "Menapetz — peigner et démêler des fibres le Chabbat. Au Mishkan on cardait laine et lin avant le filage. "
        "Interdit de démêler une matière première de façon industrielle. Pour le corps : brossage dur arrachant des cheveux touche Gozez ; "
        "brosse douce et démêlant avec précaution. Peigner doucement pour démêler est en général permis. "
        "Le Chabbat invite à la douceur envers le corps et les matériaux — "
        "pas de cardage industriel ni de démêlage qui transforme la matière première.",
    ),
    (
        "Learn about the Melacha of Mesarteit",
        "Mesarteit — marcar guías en Shabat. En el Mishkán se trazaban líneas en pieles y telas antes de cortar. "
        "Prohibido hacer marcas que guíen un trabajo futuro: líneas para cortar, puntos de referencia para coser. "
        "Dibujar sin propósito constructivo puede ser rabínico — evita planificar proyectos con trazos el Shabat. "
        "En jamá: una marca minúscula al inicio está permitida; no una línea larga por toda la hogaza.",
        "Mesarteit — marquer des guides le Chabbat. Au Mishkan on traçait des lignes sur peaux et tissus avant la coupe. "
        "Interdit de faire des marques guidant un travail futur : lignes pour couper, repères pour coudre. "
        "Dessiner sans but constructif peut être rabbinique — évite de planifier des projets par des traits le Chabbat. "
        "Pour la 'hallah : une petite marque au début est permise ; pas une longue ligne sur toute la miche.",
    ),
    (
        "Learn about the Melacha of Mocheik",
        "Mocheik — borrar marcas significativas en Shabat. En el Mishkán los escribas corregían errores en las vigas marcadas. "
        "Prohibido borrar letras, tachar texto o destruir registros. Ashkenazim evitan rasgar letras impresas en envases; "
        "sefardíes según Rav Ovadia Yosef permiten para comida y medicinas sin intención de borrar. "
        "Borrar accidentalmente no es lo mismo que corregir con intención — el Shabat protege la permanencia de lo escrito.",
        "Mocheik — effacer des signes significatifs le Chabbat. Au Mishkan les scribes corrigeaient les erreurs sur les poutres marquées. "
        "Interdit d'effacer des lettres, rayer du texte ou détruire des écrits. Ashkénazes évitent de déchirer des lettres imprimées ; "
        "séfarades selon Rav Ovadia Yosef permettent pour nourriture et médicaments sans intention d'effacer. "
        "Effacer par accident n'est pas comme corriger avec intention — le Chabbat protège la permanence de l'écrit.",
    ),
    (
        "Learn about the Melacha of Oreg",
        "¡Aprende sobre Oreg — el arte del tejido en Shabat! 🧵 Entrelazar hilos en el Mishkán para las cortinas sagradas. "
        "Prohibidos telar, zurcir con hilos nuevos que restauren el tejido, o volver a pasar un hilo suelto al patrón. "
        "Incluso dos hilos en una malla pueden violar Oreg (Talmud). "
        "En Shabat apreciamos la tela ya hecha — no creamos nueva tela ni reparamos como en un taller. "
        "Incluso reprisar un agujero con hilos nuevos puede ser Oreg — pregunta a tu rav.",
        "Oreg — tisser le Chabbat ! 🧵 Entrelacer des fils au Mishkan pour les tentures sacrées. "
        "Interdits : métier à tisser, repriser avec de nouveaux fils, réintégrer un fil au motif. "
        "Même deux fils dans une grille — violation (Talmud). "
        "Le Chabbat, on savoure le tissu déjà fait — pas de nouvelle fabrication textile ni d'atelier de réparation. "
        "Même repriser un trou en entrecroisant des fils peut poser problème — apprécie la toile telle quelle.",
    ),
    (
        "Learn about the Melacha of Oseh Batei Nirin",
        "¡Aprende sobre Oseh Batei Nirin — preparar pasadores y guías del telar en Shabat! "
        "En el Mishkán se montaban los nirin del telar para tejer cortinas. "
        "Prohibido armar o ajustar mecanismos de telar, pasar hilos por pasadores o preparar cualquier guía que habilite tejido. "
        "Parte de la cadena textil prohibida junto con Oreg y Menapetz. "
        "Incluso la infraestructura del telar descansa en Shabat — no solo el tejido final.",
        "Oseh Batei Nirin — préparer les lisses et guides du métier à tisser le Chabbat. Au Mishkan on montait les nirin pour tisser les tentures. "
        "Interdit d'assembler ou régler un métier, d'enfiler des fils dans des lisses ou de préparer tout guide permettant le tissage. "
        "Partie de la chaîne textile interdite avec Oreg et Menapetz. "
        "Même l'infrastructure du métier se repose le Chabbat — pas seulement le tissage final.",
    ),
    (
        "Learn about the Melacha of Potze'a",
        "Potze'a — abrir caminos o desbrozar en Shabat. En el Mishkán se allanaba terreno para transportar materiales. "
        "Prohibido abrir sendas nuevas, despejar obstáculos con intención de mejorar un camino o preparar terreno para cultivo. "
        "Arrastrar muebles pesados por tierra blanda puede tocar Choresh — ten cuidado en jardines. "
        "Caminar por el césped está permitido; «mejorar» el terreno con intención productiva no.",
        "Potze'a — ouvrir des chemins ou défricher le Chabbat. Au Mishkan on aplanissait le sol pour transporter des matériaux. "
        "Interdit d'ouvrir de nouveaux sentiers, d'enlever des obstacles pour améliorer un chemin ou de préparer un terrain pour la culture. "
        "Traîner des meubles lourds sur terre meuble peut toucher Choresh — prudence dans les jardins. "
        "Marcher sur l'herbe est permis ; « améliorer » le sol avec intention productive, non.",
    ),
    (
        "Learn about the Melacha of Shochait",
        "¡Aprende sobre Shochait — sacrificar o quitar la vida en Shabat! En el Mishkán se degollaban animales para ofrendas y pieles. "
        "Prohibido matar cualquier criatura viva, incluso insectos, salvo peligro real (pikuaj nefesh). "
        "El Talmud extiende a chavurá — herida que saca sangre de un ser vivo. "
        "No mates moscas por deporte; usa hilo dental con cuidado — pregunta a tu rav en casos concretos. "
        "El Shabat santifica toda vida, incluso la más pequeña criatura — no quitamos vida por costumbre.",
        "Shochait — abattre ou tuer des animaux le Chabbat ! Au Mishkan on égorgeait pour offrandes et peaux. "
        "Interdit de tuer toute créature vivante, même insectes, sauf danger réel (pikuah nefesh). "
        "Le Talmud étend à havourah — blessure qui fait saigner un être vivant. "
        "Ne tue pas les mouches par jeu ; fil dentaire avec prudence — demande à ton rav dans les cas précis. "
        "Le Chabbat sanctifie toute vie, même la plus petite créature.",
    ),
    (
        "Learn about the Melacha of Soter",
        "¡Aprende sobre Soter — demoler o desmontar en Shabat! 🔨 Contrario de Boneh: en el Mishkán se desarmaba el Tabernáculo al viajar. "
        "Prohibido desmontar estructuras con intención constructiva futura, romper algo ensamblado de forma permanente o abrir aparatos sellados. "
        "Quitar tapa de frasco atornillado puede ser Soter — pregunta en cocina y herramientas. "
        "En Shabat no deshacemos lo que está unido de forma duradera.",
        "Soter — démolir ou démonter le Chabbat ! 🔨 Contraire de Boneh : au Mishkan on démontait le Tabernacle en voyageant. "
        "Interdit de démonter pour reconstruction future, de casser un assemblage permanent ou d'ouvrir des appareils scellés. "
        "Ouvrir un bocal vissé peut être Soter — demande à ton rav pour cuisine et outils. "
        "Le Chabbat, on ne défait pas ce qui est uni durablement — pas de démontage « d'atelier ».",
    ),
    (
        "Learn about the Melacha of Tofair",
        "Tofair — coser en Shabat. En el Mishkán se unían piezas de tela y cuero con puntadas duraderas. "
        "Prohibido coser dos piezas de forma permanente, enlazar puntos o reparar ropa con aguja e hilo. "
        "Un hilo suelte puede atarse temporalmente de forma muy limitada según casos — pregunta a tu rav; no hagas «arreglos de verdad». "
        "Un imperdible de seguridad temporal suele estar permitido; coser, pegar o grapar de forma duradera no.",
        "Tofair — coudre le Chabbat. Au Mishkan on joignait tissus et cuirs par des points durables. "
        "Interdit de coudre deux pièces de façon permanente, d'enchaîner des points ou de réparer un vêtement avec aiguille et fil. "
        "Un fil pendant peut parfois se nouer très temporairement — demande à ton rav ; pas de « vraie réparation ». "
        "Une épingle de sûreté temporaire est en général permise ; coudre, coller ou agrafer durablement, non.",
    ),
    (
        "Learn about the Melacha of Tzad",
        "Tzad — atrapar animales vivos en Shabat. En el Mishkán se cazaban animales para pieles y ofrendas. "
        "Prohibido cazar, atrapar peces, matar insectos no peligrosos o cerrar una jaula sobre un pájaro que acaba de entrar. "
        "Mata un animal peligroso solo si hay riesgo real — pikuaj nefesh. Mascotas: no las atrapes de forma lúdica el Shabat. "
        "Cerrar una puerta cuando un animal ya estaba dentro suele estar permitido; atrapar lo que acaba de entrar no.",
        "Tzad — capturer des animaux vivants le Chabbat. Au Mishkan on chassait pour peaux et offrandes. "
        "Interdit de chasser, pêcher, tuer des insectes non dangereux ou de fermer une cage sur un oiseau qui vient d'entrer. "
        "Tuer un animal dangereux seulement s'il y a un vrai risque — pikuah nefesh. Animaux : pas de capture ludique le Chabbat. "
        "Fermer une porte quand l'animal était déjà dedans est en général permis ; attraper ce qui vient d'entrer, non.",
    ),
]

MISC_PROSE_POLISH: list[tuple[str, str, str]] = [
    (
        "Rosh Chodesh falls during Chanukah — recite Full Hallel at Shacharit (the Chanukah obligation), not the usual Half Hallel of Rosh Chodesh (Peninei Halakha 12-02-07).\n\nBefore Musaf:\nRemove tefillin before Musaf — do not wear tefillin during the Musaf Amidah.\n\nTachanun is omitted.",
        "Cuando Rosh Jodesh cae en Janucá — recita Hallel completo en Shaharit (obligación de Janucá), no el medio Hallel habitual de Rosh Jodesh (Peninei Halakha 12-02-07).\n\n"
        "Antes de Musaf:\n"
        "Quítate el tefilín antes de Musaf — no uses tefilín durante la Amidá de Musaf.\n\n"
        "Tachanún se omite.",
        "Quand Rosh 'Hodesh tombe pendant Hanoucca — récite le Hallel complet à Shaharit (obligation de Hanoucca), pas le demi-Hallel habituel de Rosh 'Hodesh (Peninei Halakha 12-02-07).\n\n"
        "Avant Moussaf :\n"
        "Retire les téfilines avant Moussaf — ne porte pas de téfilines pendant l'Amida de Moussaf.\n\n"
        "Ta'hanoun est omis.",
    ),
    (
        "Learn about Bishul",
        "¡Aprende sobre Bishul — la famosa melajá de «cocinar» en Shabat! 🍳 "
        "En el Mishkán se hervían hierbas para tintes y se horneaba el pan de la proposición. "
        "Prohibido cocer, hornear o freír alimentos crudos con calor de yad soledet bo (donde la mano se retira). "
        "Ein bishul ajar bishul: recalentar sólidos ya cocidos suele estar permitido; líquidos fríos — prohibido. "
        "Leyes muy detalladas — aprende con tu rav.",
        "Découvrez Bishul — la melakha de « cuisiner » à Chabbat ! 🍳 "
        "Au Mishkan on faisait bouillir des herbes pour teintures et on cuisait le pain de proposition. "
        "Interdit de cuire, rôtir ou frire des aliments crus à yad soledet bo (où la main se retire). "
        "Ein bishul ahar bishul : réchauffer des solides déjà cuits est en général permis ; liquides froids — interdit. "
        "Lois très détaillées — étudie avec ton rav.",
    ),
    (
        "Learn about Kotzer",
        "¡Aprende sobre Kotzer — cosechar y separar del crecimiento en Shabat! ✂️ "
        "Una de las melajot más cotidianas: arrancar fruta, flores o cualquier cosa de su fuente de crecimiento. "
        "En el Mishkán se cortaba trigo y hierbas. La regla es absoluta: si crece, no puedes separarlo — "
        "ni hierbas de una maceta en la ventana. Los Sabios prohíben usar un árbol directamente "
        "(no trepar, no apoyarse, no colgar hamaca). Shabat enseña a disfrutar la creación sin arrancarla constantemente.",
        "Découvrez Kotzer — moissonner et détacher de la croissance à Chabbat ! ✂️ "
        "Cueillir fruits, fleurs ou herbes — même d'un pot sur le rebord de fenêtre. "
        "Au Mishkan on coupait blé et herbes. La règle est absolue : si ça pousse, on ne le détache pas. "
        "Interdit de grimper aux arbres ou de s'appuyer sur eux pour un hamac. "
        "Le Chabbat apprend à savourer la création sans la cueillir sans cesse — "
        "même une feuille d'herbe aromatique sur le rebord ne se détache pas ce jour-là.",
    ),
    (
        "Chol HaMoed are the intermediate",
        "Jol hamoed son los días intermedios entre el primer y último día de Pesaj o Sucot. Siguen siendo fiesta, "
        "pero las reglas de melajá son más ligeras que en Yom Tov o Shabat: mucho trabajo y cocina están permitidos, "
        "con límites en comercio y algunas melajot. Afeitado y cortes de pelo están prohibidos (O.J. 531) salvo excepciones — consulta a tu rav.",
        "Hol hamoed sont les jours intermédiaires entre le premier et le dernier jour de Pessah ou Souccot. "
        "Ce reste une fête, mais les règles de melacha sont plus légères qu'à Yom Tov ou Chabbat : beaucoup de travail et cuisine permis, "
        "avec limites commerciales et certaines melachot. Rasage et coupe interdits (O.H. 531) sauf exceptions — demande à ton rav.",
    ),
    (
        "Chol HaMoed — Chol HaMoed are the intermediate",
        "Jol hamoed — jol hamoed son los días intermedios entre el primer y último día de Pesaj o Sucot. Siguen siendo fiesta, "
        "pero las reglas de melajá son más ligeras que en Yom Tov o Shabat: mucho trabajo y cocina están permitidos, "
        "con límites en comercio y algunas melajot. Afeitado y cortes de pelo están prohibidos (O.J. 531) salvo excepciones — consulta a tu rav.",
        "Hol hamoed — hol hamoed sont les jours intermédiaires entre le premier et le dernier jour de Pessah ou Souccot. "
        "Ce reste une fête, mais les règles de melacha sont plus légères qu'à Yom Tov ou Chabbat : beaucoup de travail et cuisine permis, "
        "avec limites commerciales et certaines melachot. Rasage et coupe interdits (O.H. 531) sauf exceptions — demande à ton rav.",
    ),
    (
        "Learn about a surprising ruling: answering Amen",
        "Un fallo sorprendente: responder Amén puede ser más importante que quien recita la berajá (Berajot 53b). "
        "Rabí Yose enseñó que quien responde Amén es mayor — Amén afirma que lo dicho es verdad. "
        "El Talmud compara al que bendice con el que responde: el segundo refuerza la victoria. "
        "Amén = El Melej Ne'eman — D-s, Rey fiel. Di Amén con intención y sin prisa — "
        "cada respuesta afirma la verdad de lo que se acaba de bendecir.",
        "Un arrêt surprenant : répondre Amen peut être plus important que celui qui récite la bénédiction (Berakhot 53b). "
        "Rabbi Yosse enseigne que celui qui répond Amen est plus grand — Amen affirme que ce qui est dit est vrai. "
        "Le Talmud compare celui qui bénit à celui qui répond : le second renforce la victoire. "
        "Amen = El Melekh Ne'eman — D.ieu, Roi fidèle. Dis Amen avec intention et sans hâte — "
        "chaque réponse affirme la vérité de ce qui vient d'être béni.",
    ),
    (
        "Learn about proper bowing on Rosh Hashanah",
        "¡Inclinación adecuada en Rosh Hashaná y Yom Kipur! 🙇‍♂️ No ponemos la cara directamente en suelo de piedra — "
        "colocamos papel, tela o estera entre rostro y suelo (prostración completa). "
        "El Rambam explica: postrarse en piedra se asociaba con idolatría fuera del Templo. "
        "Incluso al inclinarnos ante D-s, somos conscientes del cómo — reverencia con límites halájicos. "
        "Durante Musaf de estos días, esta postración forma parte del servicio litúrgico.",
        "Inclinaison correcte à Rosh Hashana et Yom Kippour ! 🙇‍♂️ On ne pose pas le visage directement sur pierre — "
        "on place papier, tissu ou natte entre le visage et le sol (prosternation complète). "
        "Le Rambam : se prosterner sur pierre était associé à l'idolâtrie hors du Temple. "
        "Même devant D.ieu, on reste attentif au comment — révérence avec limites halakhiques. "
        "Pendant Musaf de ces jours, cette prosternation fait partie du service liturgique.",
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
        "¡Aprende sobre Choresh — arar y preparar la tierra en Shabat! 🚜 En el Mishkán se labraba para cultivar hierbas y trigo. "
        "Prohibido aflojar, nivelar o mejorar la tierra: arar, cavar en el jardín o preparar para plantar. "
        "La regla va más allá del jardín: no arrastres bancos o carriolas pesados por tierra blanda — puede abrir surcos. "
        "En Shabat no remodelamos la faz de la tierra — caminamos sobre ella con respeto.",
        "Choresh — labourer et préparer la terre le Chabbat ! 🚜 Au Mishkan on labourait pour herbes et blé. "
        "Interdit d'ameublir, niveler ou améliorer la terre : labourer, creuser le jardin ou préparer pour planter. "
        "Ne traînez pas bancs ou poussettes sur terre meuble — peut ouvrir des sillons. "
        "Le Chabbat, on ne remodèle pas la face de la terre — on la traverse avec respect, sans labour ni plantation. "
        "Même creuser légèrement dans le jardin pour « arranger » peut poser question — demande à ton rav.",
    ),
    (
        "Learn about Gozez —",
        "¡Aprende sobre Gozez — esquilar lana y pelo en Shabat! 🐑 En el Mishkán se esquilaban ovejas para tapices y cubiertas. "
        "Prohibido cortar pelo humano, uñas, arrancar plumas firmemente unidas o desprender crecimiento vivo del cuerpo. "
        "Peluquería y corte de uñas son entre semana — mitzvá prepararse antes de Shabat. "
        "Incluso morder uñas o arrancar piel suelta puede estar restringido bajo esta categoría. "
        "El Shabat nos recuerda que nuestro cuerpo es parte de la creación — no lo «cosechamos» ese día.",
        "Gozez — tondre laine et poils le Chabbat ! 🐑 Au Mishkan on tondait les moutons pour tentures et couvertures. "
        "Interdit cheveux, ongles, plumes attachées ou tout détachement de croissance vivante du corps. "
        "Coiffure et ongles en semaine — mitsva de se préparer avant Chabbat. "
        "Même mordre les ongles ou arracher peau morte peut être restreint. "
        "Le Chabbat nous rappelle que notre corps fait partie de la création — on ne le « récolte » pas ce jour-là.",
    ),
    (
        "Learn about Zoreh —",
        "¡Aprende sobre Zoreh — aventar y dispersar en Shabat! 💨 En el Mishkán se lanzaba grano al aire para separar la paja del trigo. "
        "Prohibido usar viento o aliento para separar mezclas deseadas de no deseadas; no sacudir manteles fuera si el viento dispersa migas, "
        "ni soplar polvo de un libro para limpiarlo. Incluso ventilador dirigido a clasificar puede ser problema. "
        "En Shabat no usamos el aire como herramienta de producción.",
        "Zoreh — vanner et disperser le Chabbat ! 💨 Au Mishkan on lançait le grain en l'air pour séparer la paille. "
        "Interdit d'utiliser vent ou souffle pour trier des mélanges ; pas secouer nappes dehors si le vent disperse les miettes, "
        "pas souffler la poussière d'un livre pour le nettoyer. Même un ventilateur pour trier peut poser problème. "
        "Le Chabbat, on n'utilise pas l'air comme outil de production — on savoure sans trier par le vent.",
    ),
    (
        "Learn about Oneg Shabbat!",
        "¡Aprende sobre Oneg Shabat! 🍽️ Disfrutar del Shabat no es solo costumbre agradable — es parte esencial de su santidad y mitzvá en sí. "
        "Incluye comida festiva, descanso, estudio de Torá, oración y tiempo de calidad en familia. "
        "El Talmud (Shabat 118a) promete bendición abundante a quien se deleita en Shabat. "
        "Saborea un manjar, toma la siesta de Shabat y relájate de verdad: cada gozo con intención de honrar el día es mitzvá. "
        "Aprende una halajá sobre las comidas de Shabat y eleva tu mesa la próxima semana.",
        "Oneg Chabbat ! 🍽️ Prendre plaisir au Chabbat n'est pas qu'une agréable coutume — c'est une mitsva et partie de sa sainteté. "
        "Repas festifs, repos, étude de Torah, prière et temps en famille. "
        "Le Talmud (Chabbat 118a) promet d'abondantes bénédictions à qui se délecte du Chabbat. "
        "Savourez, faites la sieste du Chabbat, détendez-vous : chaque plaisir pour honorer le jour est mitsva. "
        "Apprends une halakha sur les repas de Chabbat pour enrichir ta table.",
    ),
    (
        "Learn about Birkat Kohanim —",
        "¡Aprende sobre Birkat Kohanim — la bendición sacerdotal! 🙏 Bamidbar 6:24-26. "
        "Ashkenazíes fuera de Israel: generalmente solo en Yom Tov; en Israel y sefardíes: a menudo cada día en Shaharit. "
        "Los kohanim suben al ducán, se cubren con el talit, extienden los dedos en el patrón tradicional y cantan los tres versículos; "
        "la congregación mira hacia abajo (Sotá 39a — la Presencia descansa sobre sus manos). "
        "Es uno de los momentos más emotivos del servicio en la sinagoga.",
        "Découvrez Birkat Kohanim — la bénédiction sacerdotale ! 🙏 Bamidbar 6:24-26. "
        "Ashkénazes hors d'Israël : souvent seulement à Yom Tov ; en Israël et séfarades : souvent chaque Shaharit. "
        "Les kohanim montent au duchan, se couvrent du talit, étendent les doigts selon la tradition et chantent les trois versets ; "
        "la congrégation baisse les yeux (Sotah 39a — la Présence repose sur leurs mains). "
        "C'est l'un des moments les plus émouvants du service à la synagogue.",
    ),
    (
        "Learn about Kiddush Hashem and Chillul Hashem",
        "¡Aprende Kiddush Hashem y Chillul Hashem! El Rambam define ambos: KIDDUSH HASHEM — un erudito o judío claramente observante "
        "que actúa con integridad, paciencia y honradez; la gente dice «qué hermosa Torá enseña su maestro». "
        "CHILLUL HASHEM — lo opuesto: conducta vergonzosa de alguien reconociblemente religioso. "
        "Es de las prohibiciones más graves; algunos dicen que solo se expía con la muerte — pregunta a tu rav en casos concretos.",
        "Kiddush Hashem et Hilloul Hashem ! Le Rambam : KIDDUSH — un Juif clairement observant qui agit avec intégrité, patience et honnêteté ; "
        "on dit « quelle belle Torah son maître enseigne ». HILLOUL — l'opposé : conduite honteuse d'une personne reconnaissablement religieuse. "
        "Parmi les interdictions les plus graves — certains cas n'expient qu'à la mort ; demande à ton rav dans les cas précis. "
        "Chaque acte public d'un Juif observant peut sanctifier ou profaner le Nom de D.ieu — choisis avec soin.",
    ),
    (
        "Gather for inspiration!",
        "Hakhel — cada siete años, tras el shemitá, todo Israel se reunía para escuchar al rey leer la Torá (Devarim 31:12). "
        "Hombres, mujeres y niños — incluso bebés, para que los padres reciban mérito. "
        "Hoy la mitsvá no se cumple igual, pero el espíritu de Hakhel sigue vivo: únete a una clase de Torá o reunión comunitaria, incluso en línea. "
        "Leer Torá en familia en Sucot o tras el shemitá en Israel recuerda esta tradición. "
        "Reunirse por inspiración continúa la antigua mitsvá de unidad del pueblo.",
        "Hakhel — tous les sept ans, après le shemita, tout Israël se rassemblait pour entendre le roi lire la Torah (Devarim 31:12). "
        "Hommes, femmes et enfants — même les bébés, pour que les parents reçoivent du mérite. "
        "Aujourd'hui la mitsva ne s'accomplit plus de la même façon, mais l'esprit de Hakhel vit : rejoins un cours de Torah ou une assemblée, même en ligne. "
        "Lire la Torah en famille à Souccot ou après le shemita en Israël rappelle cette tradition. "
        "Se rassembler pour l'inspiration poursuit l'ancienne mitsva d'unité du peuple.",
    ),
    (
        "Get creative with Mishloach Manot",
        "Mishloach manot — en Purim debes enviar al menos a un adulto judío dos tipos distintos de comida lista para comer "
        "(p. ej. galleta y zumo). Debe enviarse el día de Purim, idealmente por mensajero (mishloach). "
        "La Meguilá enseña: fortalecer la amistad y la unidad del pueblo con generosidad. "
        "Un empaque bonito es parte de la simjá, pero el contenido importa más. "
        "Piensa ya a quién alegrar en el próximo Purim — vecino, maestro o amigo.",
        "Mishloach manot — à Pourim tu dois envoyer à au moins un adulte juif deux sortes distinctes de nourriture prête "
        "(ex. biscuit et jus). Doit être envoyé le jour de Pourim, idéalement par messager (mishloah). "
        "La Meguila enseigne : renforcer l'amitié et l'unité du peuple par la générosité. "
        "Un bel emballage fait partie de la simha, mais le contenu compte davantage. "
        "Pense déjà à qui réjouir au prochain Pourim — voisin, enseignant ou ami.",
    ),
    (
        "Learn about Tefillin!",
        "¡Aprende sobre las tefilín! 👑 ¿Sabías? La caja de la cabeza contiene los mismos cuatro pasajes de Torá que la del brazo, "
        "pero dispuestos de forma distinta — mente y acción unidas a D-s. "
        "Se ponen en la oración matutina de días laborables (no Shabat ni festividades). "
        "Misión de hoy: descubre cómo las tefilín conectan tu mente con D-s — pregunta a tu rav cómo colocarlas correctamente. "
        "Shel yad en el bíceps del brazo débil, shel rosh sobre la línea del cabello entre los ojos.",
        "Les téfilines ! 👑 La boîte de la tête contient les mêmes quatre passages que celle du bras, mais disposés différemment — "
        "esprit et action unis à D.ieu. Elles se posent à la prière du matin en semaine (pas Chabbat ni fêtes). "
        "Mission : découvre comment les téfilines relient ton esprit à D.ieu — demande à ton rav comment les poser. "
        "Shel yad sur le biceps de ton bras faible, shel rosh au-dessus de la ligne des cheveux entre les yeux. "
        "Les téfilines relient l'esprit, le cœur et l'action — une mitzva quotidienne pour les hommes en semaine.",
    ),
    (
        "Connect to our future! Learn about the Beit HaMikdash",
        "El Beit HaMikdash — puente entre pasado y futuro. Cada detalle de su estructura y servicio tiene significado espiritual profundo. "
        "Estudiar sus leyes y servicios es como cumplirlos en cierta medida (Talmud); aclara muchas otras partes de la Torá. "
        "Las oraciones por el Tercer Templo no son nostalgia, sino fe activa en la geulá. "
        "Hoy: lee un pasaje sobre los utensilios sagrados o reflexiona sobre lo que el Templo simboliza en tu vida.",
        "Le Beit HaMikdash — pont entre passé et avenir. Chaque détail de sa structure et de son service a un sens spirituel profond. "
        "Étudier ses lois et services équivaut en partie à les accomplir (Talmud) ; cela éclaire bien d'autres parties de la Torah. "
        "Les prières pour le Troisième Temple ne sont pas de la nostalgie, mais une foi active dans la geoula. "
        "Aujourd'hui : lis un passage sur les ustensiles sacrés ou réfléchis à ce que le Temple symbolise dans ta vie.",
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
        "Rejoignez un minyan quand tu le peux.",
    ),
    (
        "Learn about Me'ilah —",
        "¡Aprende sobre Me'ilah — apropiación indebida de bienes del Templo! ⚠️ "
        "Cuando algo es hekdesh (consagrado a D-s), está prohibido usarlo en beneficio personal. "
        "Ejemplos: comer del pan de la proposición, usar un animal consagrado para transporte. "
        "Consecuencias: devolver el valor completo más un quinto adicional y traer korban asham. "
        "La violación comienza solo con beneficio real.",
        "Découvrez Me'ilah — détournement des biens du Temple ! ⚠️ "
        "Quand un objet est hekdesh (consacré à D.ieu), il est interdit de l'utiliser pour un bénéfice personnel. "
        "Exemples : manger du pain de proposition, utiliser un animal consacré pour le transport. "
        "Conséquences : rembourser la valeur complète plus un cinquième et apporter un korban asham. "
        "La violation commence seulement avec un bénéfice réel. "
        "Le sacré n'est pas pour usage personnel — même un « petit » profit sur un objet du Temple a un prix.",
    ),
    (
        "Learn about Tochen —",
        "¡Aprende sobre Tochen — moler y reducir a partículas en Shabat! ⚙️ "
        "En el Mishkán se trituraban hierbas para tintes e incienso. "
        "Aplica principalmente a gedulei karka (lo que crece de la tierra): cubos muy finos de ensalada pueden ser problema; "
        "raspar barro seco del calzado también. Carne cocida o queso — sin restricción de moler. "
        "El Talmud relaciona Tochen con preparar incienso — Shabat protege la materia prima de la tierra.",
        "Découvrez Tochen — broyer à Chabbat ! ⚙️ "
        "Au Mishkan on écrasait herbes pour teintures et encens. "
        "Surtout gedulei karka (ce qui pousse de la terre) : dés très fins de salade israélienne, boue sèche sur les chaussures. "
        "Viande cuite ou fromage — pas de restriction de broyage. "
        "Le Talmud relie Tochen à la préparation des encens — le Chabbat protège la matière première de la terre. "
        "Même râper du fromage très fin ou un saladier israélien peut poser question — demande à ton rav.",
    ),
    (
        "Learn about Meraked —",
        "¡Aprende sobre Meraked — cernir y colar en Shabat! 🫖 En el Mishkán se tamizaba harina para el pan de la proposición y se colaban hierbas. "
        "Implica utensilio dedicado (tamiz, filtro, colador). Meshamer (colar líquidos): prohibición toráica si el líquido "
        "tiene tanto sedimento que nadie lo bebería sin filtrar; zumo ya bebible con pulpa — generalmente permitido. "
        "Similar a Borer y Zoreh pero con herramienta específica — aprende los casos con tu rav.",
        "Meraked — tamiser et filtrer le Chabbat ! 🫖 Au Mishkan on tamisait la farine et filtrait les herbes. "
        "Utensile dédié interdit (tamis, filtre). Meshamer : interdiction toranique si liquide imbuvable sans filtration ; "
        "jus déjà buvable avec pulpe — en général permis. "
        "Proche de Borer et Zoreh mais avec outil spécifique — étudie avec ton rav. "
        "Le Chabbat, on ne transforme pas la cuisine en usine de filtration.",
    ),
    (
        "Learn about Shemittah —",
        "¡Aprende sobre Shemitá — el séptimo año de descanso de la tierra de Israel! 🌾 Sin arar, plantar, podar ni cosecha comercial; "
        "solo lo básico para mantener plantas vivas. Lo que crece solo tiene kedushat shevi'it y no se vende normalmente. "
        "En Israel hoy: heter mejirá (venta temporal de tierras a no judíos) — el Rabinato lo permite; muchas autoridades ortodoxas no. "
        "Otzar beit din — la corte rabínica distribuye productos cobrando solo mano de obra. Aprende con tu rav.",
        "Shemita — la septième année de repos de la terre d'Israël ! 🌾 Pas de labour, plantation, taille ni récolte commerciale ; "
        "seulement le minimum pour garder les plantes vivantes. "
        "Ce qui pousse seul a kedouchat chevi'it et ne se vend pas normalement — on le traite avec sainteté. "
        "En Israël : hété mekhira (Rabbinate) ou otzar beit din — "
        "beaucoup de communautés orthodoxes rejettent la mekhira. Étudie avec ton rav avant d'acheter des fruits.",
    ),
    (
        "Explore the majesty of Psalm 8!",
        "¡Explora la majestad del Salmo 8! 🌠 Celebra la maravilla de la creación de D-s y el lugar especial del ser humano. "
        "Cuando David miró las estrellas, escribió sobre la dignidad humana y nuestro propósito divino. "
        "Reto del día: sal afuera, mira el cielo nocturno y recita este salmo edificante.",
        "Explorez la majesté du Psaume 8 ! 🌠 Il célèbre la merveille de la création de D.ieu et le rôle unique de l'humanité. "
        "Quand David contempla les étoiles, il écrit sur la dignité humaine et notre vocation divine. "
        "Défi du jour : sortez, regardez le ciel nocturne et récitez ce psaume édifiant.",
    ),
    (
        "Explore the mighty Shin (ש)!",
        "¡Explora el poderoso Shin (ש)! 🔥 Tres puntas hacia arriba — los tres patriarcas. "
        "Aparece en cada caja de mezuzá y en las tefilín, recordando el nombre Sha-dai. "
        "Su forma recuerda una llama: acércate a la Torá con entusiasmo ardiente. "
        "Reto del día: aprende Torá con pasión y alegría.",
        "Explorez le puissant Shin (ש) ! 🔥 Trois pointes vers le haut — les trois patriarches. "
        "Il figure sur chaque boîte de mezouza et sur les téfilines, rappelant le nom Cha-daï. "
        "Sa forme évoque une flamme : approchez la Torah avec un enthousiasme ardent. "
        "Défi du jour : étudiez la Torah avec passion et joie.",
    ),
    (
        "Connect to our history!",
        "¡Conéctate con nuestra historia! Recuerda un momento el Éxodo de Egipto 🌊. "
        "Imagina los milagros — cómo D-s transformó una nación de esclavos en un pueblo libre. "
        "Esa memoria de libertad es tan importante que es mitzvá recordarla cada día y cada noche.",
        "Connectez-vous à notre histoire ! Prenez un moment pour vous souvenir de la Sortie d'Égypte 🌊. "
        "Imaginez les miracles — comment D.ieu a transformé une nation d'esclaves en peuple libre. "
        "Ce souvenir de liberté est si important qu'il est mitsva de le rappeler chaque jour et chaque nuit.",
    ),
    (
        "Connect to the Holy Land!",
        "¡Conéctate a la Tierra Santa! Recuerda (uno de los diez recuerdos diarios) que D-s dio Israel al pueblo judío "
        "como herencia eterna. El aire de Israel es tan santo que el Talmud dice que hace sabio a quien lo respira. "
        "Cuando D-s nos dio la tierra, también nos dio fuerza para guardar Sus mitzvot. "
        "¿Te sientes lejos? Cada judío tiene una porción en la Tierra Santa — es tu hogar espiritual.",
        "Connectez-vous à la Terre sainte ! Rappelez (un des dix souvenirs quotidiens) que D.ieu a donné Israël au peuple juif "
        "en héritage éternel. L'air d'Israël est si saint que le Talmud dit qu'il rend sage. "
        "Loin d'Israël ? Chaque Juif a une part dans la Terre sainte — votre foyer spirituel.",
    ),
    (
        "Appreciate the miracle of healing!",
        "¡Aprecia el milagro de la sanación! Agradece a D-s por darnos la asombrosa capacidad de reparar y recuperarnos 🌱. "
        "Desde un pequeño corte que cicatriza hasta combatir una enfermedad — nuestro cuerpo trabaja por nuestra salud. "
        "Cada recuperación recuerda el cuidado constante de D-s ❤️‍🩹.",
        "Appréciez le miracle de la guérison ! Remerciez D.ieu de nous donner la capacité étonnante de réparer et de guérir 🌱. "
        "D'une petite coupure à la lutte contre la maladie — notre corps veille sur notre santé. "
        "Chaque guérison rappelle les soins constants de D.ieu ❤️‍🩹.",
    ),
    (
        "Embrace the gift of Torah study!",
        "¡Abraza el regalo del estudio de la Torá! 📚 El Rambam revela: aprender Torá no es solo ganar conocimiento — "
        "es conectar con la sabiduría de D-s. ¿Sabías que? Cada palabra de Torá que aprendes crea un ángel que te acompaña. "
        "Reto: reserva hoy cinco minutos para aprender algo nuevo — cada momento de estudio es invaluable.",
        "Embrassez le don de l'étude de la Torah ! 📚 Le Rambam révèle : apprendre la Torah, ce n'est pas seulement acquérir "
        "des connaissances — c'est se relier à la sagesse de D.ieu. Chaque mot appris crée un ange qui vous accompagne. "
        "Défi : réservez cinq minutes aujourd'hui pour apprendre quelque chose de nouveau.",
    ),
    (
        "Dive into Kabbalah's depths!",
        "¡Sumérgete en las profundidades de la Cábala! 🌊 Estudiarla correctamente puede transformar tu práctica judía — "
        "con madurez en Torá y temor a D-s. El Arizal dijo que en estas generaciones es mitzvá revelar estas enseñanzas. "
        "La Cábala ayuda a entender las razones profundas de las mitzvot. "
        "Reto del día: aprende un insight místico sobre una mitzvá que haces a menudo.",
        "Plongez dans les profondeurs de la Kabbale ! 🌊 L'étudier correctement peut transformer votre pratique — "
        "avec maturité en Torah et crainte de D.ieu. L'Arizal dit qu'il est mitsva de révéler ces enseignements aujourd'hui. "
        "Défi : apprenez un aperçu mystique sur une mitsva que vous accomplissez souvent.",
    ),
    (
        "Ever wonder about some wild Torah laws?",
        "¿Te preguntas por leyes «salvajes» de la Torá? 🔮 La Torá habla del «Ov»: quemaban incienso, agitaban ramas de mirto "
        "e intentaban escuchar voces de... cráneos. El Rambam explica voces que parecían salir del suelo o de las axilas. "
        "Muestra cómo la Torá nos protege de prácticas espirituales dañinas. "
        "Reto del día: aprecia cómo la Torá guía nuestra conexión con D-s.",
        "Vous êtes-vous demandé pourquoi la Torah parle de lois si étranges ? 🔮 Le « Ov » : encens, branches de myrte, "
        "voix prétendument venues de crânes. Le Rambam décrit des voix semblant sortir du sol ou des aisselles. "
        "La Torah nous protège de pratiques spirituelles nuisibles. "
        "Défi : appréciez comment la Torah guide notre lien avec D.ieu.",
    ),
    (
        "Learn about the power of forgiveness!",
        "¡Aprende sobre el poder del perdón! Tómate un momento para perdonar a quien te haya molestado, grande o pequeño. "
        "Todo está orquestado por D-s con un propósito. Cuando muestras misericordia a otros, D-s muestra misericordia extra contigo — "
        "como un bumerán espiritual de bondad. Soltar el resentimiento libera espacio para la alegría y la paz. "
        "Antes de Yom Kipur, perdonar a otros abre la puerta al perdón divino.",
        "Le pouvoir du pardon ! Prends un moment pour pardonner à qui t'a blessé, peu ou beaucoup. "
        "Tout est orchestré par D.ieu avec un dessein. Quand tu fais miséricorde, D.ieu t'en fait davantage — "
        "comme un boomerang spirituel de bonté. Lâcher la rancune libère de la place pour la joie et la paix. "
        "Avant Yom Kippour, le pardon envers autrui ouvre la porte au pardon divin. "
        "Prends un moment aujourd'hui pour lâcher une rancune — c'est un cadeau pour toi aussi.",
    ),
    (
        "Learn about appreciating G-d's creations!",
        "¿Algo huele maravilloso? Conviértelo en momento espiritual: recita «Baruj Ata Adonai Eloheinu Melej ha-Olam, Borei minei besamim» 🌺 "
        "— bendición sobre especias, hierbas aromáticas y flores. Luego disfruta la fragancia con intención. "
        "Cada aroma agradable revela la maestría divina en lo cotidiano; la berajá entrena a ver santidad en lo sensorial. "
        "Un momento de fragancia puede convertirse en oración breve y silenciosa de gratitud.",
        "Quelque chose sent merveilleusement bon ? Transformez-le en moment spirituel : récitez "
        "« Barouh Ata Adonaï Eloheinou Mele'h ha-Olam, Borei minei besamim » 🌺 — sur épices, herbes et fleurs parfumées. "
        "Savourez ensuite le parfum avec intention. Chaque agréable senteur révèle la maîtrise divine dans le quotidien. "
        "La bénédiction entraîne à voir la sainteté dans l'expérience sensorielle. "
        "Un parfum agréable peut devenir une prière silencieuse de gratitude.",
    ),
    (
        "Learn about the Holy Temple's recycling system!",
        "¡El «reciclaje» del Templo! ♻️ Las vestiduras sacerdotales desgastadas no se tiraban — "
        "se convertían en mechas para las lámparas del candelabro. Incluso lo santo que ya no servía para su fin original "
        "encontraba nuevo servicio a D-s en lugar de desperdiciarse. "
        "Reto: reflexiona sobre dar nuevo propósito a lo que parece agotado en tu vida — nada sagrado se pierde del todo. "
        "Los kohanim enseñan que incluso lo usado puede alumbrar de nuevo.",
        "Le « recyclage » du Temple ! ♻️ Les vêtements sacerdotaux usés devenaient mèches pour le chandelier de la menorah. "
        "Même le sacré épuisé trouvait un nouveau service à D.ieu au lieu d'être jeté — rien de saint ne se perd totalement. "
        "Les kohanim nous enseignent que même l'usé peut encore éclairer. "
        "Défi : redonne un sens à ce qui semble fini dans ta vie — comme ces vêtements devenus lumière. "
        "Rien de saint ne se perd totalement quand on le réoriente vers un nouveau service.",
    ),
    (
        "Learn about the mitzvah of writing a Torah scroll!",
        "¡Aprende la mitzvá de escribir un rollo de Torá! ✍️ Cada hombre judío debe escribir (o participar en escribir) un Sefer Torá. "
        "Hoy muchos cumplen comprando un Jumash, Tanaj u otros libros sagrados con intención de la mitzvá. "
        "Cada letra corresponde a un alma judía; falta una letra e invalida todo el rollo — "
        "así de importante es cada judío en la comunidad. Un escriba (sofer) entrena años en leyes y caligrafía sagrada.",
        "La mitsva d'écrire un rouleau de Torah ! ✍️ Chaque homme juif doit écrire (ou y participer) un Sefer Torah. "
        "Aujourd'hui beaucoup l'accomplissent en achetant 'Humash, Tanakh et livres saints avec cette intention. "
        "Chaque lettre correspond à une âme juive ; une lettre manquante invalide tout le rouleau — "
        "chaque Juif compte dans la communauté. Un sofer s'entraîne des années en lois et calligraphie sacrée.",
    ),
    (
        "Learn about the prohibition of idol worship!",
        "¡Aprende sobre la prohibición de idolatría! 🛡️ El Rambam explica que no es solo estatuas — "
        "es cualquier cosa que pongamos antes de D-s: dinero, fama, poder o falsos dioses. "
        "Resistir la idolatría moderna testifica que solo D-s es el poder verdadero. "
        "El Rambam enumera categorías de idolatría prohibida — desde adorar estrellas hasta tratar un objeto como intermediario divino. "
        "Reto: identifica algo demasiado importante en tu vida y da un paso concreto para poner a D-s primero.",
        "L'interdiction d'idolâtrie ! 🛡️ Le Rambam explique que ce n'est pas que des statues — "
        "c'est tout ce qu'on place avant D.ieu : argent, gloire, pouvoir ou faux dieux. "
        "Résister à l'idolâtrie moderne témoigne que seul D.ieu est le vrai pouvoir. "
        "Le Rambam énumère des catégories d'idolâtrie interdite — des étoiles aux objets traités comme intermédiaires divins. "
        "Défi : identifie ce qui est trop important et fais un pas concret pour remettre D.ieu en premier.",
    ),
    (
        "Learn from history's mistakes!",
        "Recuerda (uno de los diez recuerdos diarios) que incluso tras milagros en el desierto, "
        "a veces nuestros antepasados perdieron la fe — el becerro de oro a los cuarenta días de oír a D-s. "
        "Lección: aunque falles, D-s siempre da oportunidad de volver. Nunca es tarde para empezar de nuevo.",
        "Rappelez (un des dix souvenirs quotidiens) qu'après les miracles du désert, "
        "nos ancêtres ont parfois faibli — le veau d'or quarante jours après entendre D.ieu. "
        "Leçon : même après une chute, D.ieu offre un nouveau départ.",
    ),
    (
        "Protect the world: Learn about Bal Tashchit!",
        "Bal tashjit (Devarim 20:19) prohíbe destrucción innecesaria. Los Sabios lo ampliaron: "
        "no desperdiciar comida, romper utensilios útiles ni talar árboles frutales sin motivo constructivo. "
        "No es destrucción productiva (p. ej. construir una casa) — es el derroche sin propósito. "
        "Cada bocado y cada gota son un depósito de D-s; somos guardianes de la creación, no destructores. "
        "Reto de hoy: tira menos, usa más tiempo lo que tienes o dona lo que aún sirve a otros.",
        "Bal tachchit (Devarim 20:19) interdit la destruction inutile. Les Sages l'ont étendu : "
        "ne pas gaspiller nourriture, casser des objets utiles ni abattre des arbres fruitiers sans but constructif. "
        "Chaque bouchée et chaque goutte sont un dépôt de D.ieu — nous sommes gardiens de la création, pas destructeurs. "
        "Défi du jour : jette moins, utilise plus longtemps ce que tu as ou donne ce qui sert encore aux autres.",
    ),
    (
        "Learn the Rambam's stunning ruling on free will",
        "El Rambam sobre libre albedrío: cada persona puede elegir ser justo como Moshé o malvado como Yerobam — "
        "pilar fundamental de la Torá. Si D-s decretara el resultado, ¿cómo existiría el teshuvá? "
        "D-s conoce el futuro, pero cada uno es libre; esa aparente contradicción supera nuestra inteligencia, "
        "como preguntar cómo existe D-s sin tiempo.",
        "Le Rambam sur le libre arbitre : chacun peut choisir d'être juste comme Moïse ou méchant comme Jéroboam — "
        "pilier de la Torah. Si D.ieu décrétait le résultat, comment le teshouva existerait-il ? "
        "D.ieu connaît l'avenir, mais chacun est libre ; cette contradiction dépasse notre intelligence.",
    ),
    (
        "Learn the critical distinction that most people miss about Yom Kippur",
        "Distinción crucial de Yom Kipur: la Mishná enseña que Yom Kipur expía entre la persona y D-s, "
        "pero NO entre persona y persona hasta que el ofensor pida perdón a la víctima. "
        "Debes ir, reconocer, disculparte sinceramente y reparar si aplica. "
        "Intenta hasta tres veces; si rechazan el perdón tras eso, la culpa queda en ellos. "
        "El Cielo no puede perdonar el daño hecho a otra persona.",
        "Distinction cruciale de Yom Kippour : la Michna enseigne que Yom Kippour expie entre l'homme et D.ieu, "
        "mais PAS entre personnes tant que l'auteur n'a pas demandé pardon à la victime. "
        "Allez, reconnaissez, excusez-vous et réparez. Trois tentatives ; ensuite la faute peut être sur eux.",
    ),
    (
        "Learn about 'avak lashon hara'",
        "Avak lashon hará — el «polvo» del habla negativa: patrones limítrofes que el Jafetz Jaim prohíbe. "
        "(1) Elogio ambiguo que implica lo contrario. (2) «¡No me preguntes por él!» — insinuación. "
        "(3) Contexto que deshonra indirectamente. (4) Alabar ante enemigos sabiendo que responderán mal. "
        "(5) Recordar el pasado vergonzoso de quien ya hizo teshuvá. El tono y la intención cuentan — "
        "el Jafetz Jaim enseña que incluso el «polvo» del habla negativa es peligroso.",
        "Avak lashon hara — la « poussière » de la parole négative : motifs limites interdits par le Hafets Haim. "
        "(1) Louange ambiguë. (2) « Ne me demande pas pour lui ! » (3) Contexte qui déshonore indirectement. "
        "(4) Louer devant des ennemis. (5) Rappeler le passé honteux de quelqu'un qui a fait teshouva. "
        "Le ton et l'intention comptent — même la « poussière » est dangereuse. "
        "Le Hafets Haim enseigne : mieux vaut se taire que risquer une parole ambiguë.",
    ),
    (
        "Learn about shechitah — kosher slaughter",
        "¡Aprende shejitá — sacrificio kosher! Los cinco factores invalidantes (mnemotécnico ShaCHaT NaDaH): "
        "(1) Shehiyá — pausa a mitad del corte. (2) Jaladá — cuchillo oculto bajo la tráquea. "
        "(3) Drashá — presionar en vez de deslizar suavemente. (4) Hagramá — cortar en lugar incorrecto del cuello. "
        "(5) Ikur — arrancar antes de completar. Todos deben cumplirse; de lo contrario la carne puede ser treif. "
        "Solo un shojet entrenado puede realizar shejitá.",
        "Shehita — abattage casher ! Les cinq facteurs invalidants (ShaCHaT NaDaH) : "
        "(1) Shehiya — pause. (2) Halada — couteau caché. (3) Drasha — pression au lieu de glisser. "
        "(4) Hagrama — mauvais endroit. (5) Ikour — arrachement prématuré. "
        "Tous doivent être respectés ; sinon la viande peut être treif. Seul un shochet formé peut abattre — "
        "la shehita exige un couteau parfaitement lisse et un geste continu sans pression.",
    ),
    (
        "Learn about the prohibition of blood — and the specific salting process",
        "Prohibición de sangre: la Torá dice «la sangre es la vida». Melijá — (1) enjuagar; (2) remojar 30 min; "
        "(3) sal gruesa en todas las superficies; (4) una hora en tabla perforada; (5) enjuagar tres veces. "
        "Excepción: el hígado no se hace kosher solo con sal — debe asarse a la llama primero. "
        "Carne no salada en 72 h tras el shejitá suele requerir asado, no solo sal. "
        "La melijá protege la santidad de lo que comemos — cada paso importa.",
        "Interdiction du sang : la Torah dit « le sang c'est la vie ». Meliha — (1) rincer ; (2) tremper 30 min ; "
        "(3) gros sel ; (4) une heure sur planche perforée ; (5) rincer trois fois. "
        "Exception : le foie doit être grillé à la flamme avant cuisson — trop de sang pour le salage seul. "
        "Viande non salée dans les 72 h après abattage requiert souvent grillade, pas seulement sel. "
        "La meliha protège la sainteté de ce que nous mangeons — chaque étape compte.",
    ),
    (
        "Learn about the fascinating laws of Jews benefiting from meat and milk cooked together!",
        "¡Leyes fascinantes de carne con leche cocida junta! 🥩🥛 Prohibición total de beneficio — no solo comer: "
        "incluso mil quesoburgers regalados están prohibidos para aprovechar. "
        "El pollo con leche es decreto rabínico; por eso sí se permite alimentar con pollo cocinado con leche "
        "a quien no es judío, a diferencia de carne de res. La halajá protege la santidad de la mesa judía. "
        "Esta separación recuerda que algunas cosas siguen siendo sagradas aunque no las comamos.",
        "Viande et lait cuits ensemble ! 🥩🥛 Interdiction totale de bénéfice — pas seulement manger : "
        "même mille burgers offerts sont interdits à utiliser. "
        "Poulet-lait est rabbinique ; on peut donc nourrir un non-Juif avec poulet-lait, contrairement au bœuf-lait. "
        "La halakha protège la sainteté de la table juive — même un cadeau interdit profiter si c'est viande-lait cuite ensemble. "
        "Cette séparation rappelle que certaines choses restent sacrées même hors de la bouche.",
    ),
    (
        "Learn about the fascinating symbolism of the kosher signs",
        "¡Aprende los signos kosher de animales terrestres! 🐄 Pezuña hendida y rumiante — ambos obligatorios. "
        "Cerdo: pezuña sí, rumia no; camello: rumia sí, pezuña no — por eso ninguno es kosher. "
        "Los Sabios ven simbolismo: pezuña hendida — apertura y generosidad hacia otros; rumiar — meditar sobre lo vivido antes de actuar. "
        "Lo que comemos nos forma; elegir kosher es elegir un estilo de vida consciente y conectado a D-s. "
        "Los signos no son arbitrarios — recuerdan apertura y reflexión antes de actuar.",
        "Découvrez les signes casher des animaux terrestres ! 🐄 Sabot fendu et rumination — les deux requis. "
        "Porc : sabot oui, rumination non ; chameau : l'inverse — aucun n'est casher. "
        "Symbolisme : sabot fendu — ouverture et générosité ; ruminer — méditer avant d'agir. "
        "Ce que nous mangeons nous forme ; choisir casher, c'est un mode de vie conscient et connecté à D.ieu. "
        "Les signes ne sont pas arbitraires — ils rappellent ouverture et réflexion avant d'agir.",
    ),
    (
        "Learn a fascinating halachic debate about whether raisins",
        "Debate sobre pasas: ¿hay que revisar bichos? Muchos poskim exigen revisar pasas secas "
        "(como otros frutos secos) porque pueden albergar insectos. Otros son más lenientes según el origen y la época. "
        "Consulta a tu rav — en la práctica muchas comunidades revisan pasas antes de Pesaj y en general.",
        "Débat sur les raisins secs : faut-il vérifier les insectes ? Beaucoup de poskim exigent de contrôler "
        "les raisins secs ; d'autres sont plus indulgents selon l'origine. Demande à ton rav.",
    ),
    (
        "Learn about the fascinating laws of Nedarim",
        "Nedarim — votos: las palabras pueden transformar un objeto en prohibido para ti. "
        "La Torá prevé hatarat nedarim — anulación por un sabio. "
        "El Rambam (Hilchot Nedarim 4:13) describe enredos hilarantes cuando alguien jura no beneficiarse "
        "de toda una ciudad y luego necesita agua de un pozo comunitario. Las palabras importan — pide a tu rav antes de jurar. "
        "Un voto mal formulado puede complicar la vida — la Torá prevé anulación por un sabio.",
        "Nedarim — vœux : les paroles peuvent rendre un objet interdit pour vous. "
        "La Torah prévoit hatarat nedarim — annulation par un sage. "
        "Le Rambam (Hilkhot Nedarim 4:13) décrit des cas où quelqu'un jure de ne pas profiter d'une ville entière "
        "puis a besoin d'eau d'un puits communal. Les mots comptent — demande à ton rav avant de jurer. "
        "Un vœu mal formulé peut compliquer la vie — la Torah prévoit l'annulation par un sage.",
    ),
    (
        'Learn about the ultimate "handle with care" policy for Hashem',
        "¡La política «manejar con cuidado» para los Nombres de Hashem! ✍️ La Torá prohíbe borrar o destruir Su Nombre sagrado — "
        "no es solo precaución, es el más alto honor hacia lo divino. "
        "Siete Nombres específicos nunca pueden borrarse; error en un rollo — cortar la sección y genizá. "
        "Por eso escribimos «D-s» con guión y evitamos Nombres en papeles desechables o tratados sin respeto. "
        "Los libros sagrados merecen genizá, no el cubo de basura — honrar el Nombre es honrar a Quien lo escribió.",
        "La politique « manipuler avec soin » pour les Noms de Hachem ! ✍️ La Torah interdit d'effacer ou détruire Son Nom sacré — "
        "pas seulement prudence, mais le plus haut honneur envers le divin. "
        "Sept Noms précis ne peuvent jamais être effacés ; erreur sur un rouleau — couper la section et geniza. "
        "D'où « D.ieu » avec trait d'union et éviter les Noms sur papier jetable. "
        "Traiter les livres saints avec respect, c'est honorer Celui dont le Nom y est écrit.",
    ),
    (
        "Learn about the rabbinic boundary of Muktzeh",
        "¡Aprende Muktzeh — objetos «apartados» en Shabat! Lo que mentalmente dejaste fuera del uso antes del anochecer "
        "no se mueve ese día. Cerca rabínica para evitar melajá accidental y marcar el día como distinto del laborable. "
        "Categorías prácticas: muktzeh machmat guisa (herramientas), machmat mius (basura), machmat hessron kis (objetos valiosos que no quieres usar), etc. "
        "Un martillo en la mesa del comedor — probablemente muktzeh; un libro de Torá — no. Aprende con tu rav.",
        "Mouktsé — objets « mis de côté » le Chabbat ! Ce que tu as écarté mentalement avant la nuit "
        "ne se déplace pas ce jour-là. Clôture rabbinique pour éviter melacha accidentelle et marquer le jour. "
        "Catégories : mouktsé machmat guisa (outils), machmat mius (déchets), machmat hessron kis (objets précieux), etc. "
        "Un marteau sur la table — probablement mouktsé ; un livre de Torah — non. Étudie avec ton rav.",
    ),
    (
        "Learn about the melacha of Koshair",
        "¡Aprende sobre Koshair — el arte preciso de atar nudos en Shabat! En el Mishkán: nudos de camelleros y redes de pesca. "
        "Prohibido el nudo profesional y duradero que se pretende dejar atado; lazos simples de corbata o zapato suelen estar permitidos según el caso. "
        "Contrario de Matir (desatar). Un nudo que no sabes cómo deshacer — pregunta a tu rav antes del Shabat. "
        "Un nudo de cordón de zapato o corbata simple suele estar permitido; un nudo de marinero, no.",
        "Koshair — faire des nœuds permanents le Chabbat ! Au Mishkan : nœuds de chameliers et filets. "
        "Nœud professionnel durable interdit ; lacets simples de cravate ou chaussure souvent permis selon le cas. "
        "Contraire de Matir (dénouer). Nœud que tu ne sais pas défaire — demande à ton rav avant Chabbat. "
        "Le Chabbat n'est pas le moment de nouer des attaches faites pour durer. "
        "Un nœud de cordon de chaussure ou de cravate simple est souvent permis — un nœud de marin, non.",
    ),
    (
        "Learn about the melacha of Me'ameir",
        "¡Aprende Me'ameir — recoger y agrupar en Shabat! En el Mishkán: espigas caídas y hierbas para tintes. "
        "Juntar lo disperso que creció de la tierra en un montón — p. ej. manzanas caídas en el jardín en una cesta para guardar — "
        "puede ser problema. Recoger para comer inmediatamente en la mano tiene reglas distintas (ochel mitoch psolet). "
        "Consulta a tu rav antes de «ordenar» el jardín el Shabat. "
        "Incluso juntar hojas secas en montón para tirar puede plantear dudas — el Shabat no es día de jardinería.",
        "Me'ameir — rassembler le Chabbat ! Au Mishkan : épis tombés et herbes pour teintures. "
        "Grouper ce qui pousse de la terre en un tas pour conserver peut être problématique — "
        "par ex. pommes tombées dans le jardin dans un panier pour garder. "
        "Ramasser pour manger tout de suite à la main — règles différentes (ochel mitoch psolet). "
        "Demande à ton rav avant de « ranger » le jardin le Chabbat. "
        "Même ramasser des feuilles mortes en tas pour les jeter peut poser question — le Chabbat n'est pas jour de jardinage.",
    ),
    (
        "Learn about the melacha of Toveh",
        "¡Aprende sobre Toveh — hilar y torcer fibras en Shabat! 🧶 Puente entre materia prima y textil en el Mishkán: "
        "las mujeres hilaban incluso lana aún en el animal. "
        "Prohibido crear hilo, cordón o mecha a partir de fibras sueltas — rueda, husillo o entre las palmas. "
        "También torcer varios hilos en uno más grueso. En Shabat dejamos reposar las ruecas — disfrutamos la ropa ya hecha. "
        "Torcer varios hilos sueltos en uno más grueso también puede ser Toveh — el Shabat descansa la producción textil.",
        "Toveh — filer et tordre des fibres le Chabbat ! 🧶 Au Mishkan : filage de laine même sur l'animal vivant. "
        "Interdit de créer fil, corde ou mèche à partir de fibres lâches — rouet, fuseau ou entre les paumes. "
        "Aussi tordre plusieurs fils en un plus épais. Le Chabbat, les rouets se reposent — "
        "on savoure les vêtements déjà faits sans filer ni tordre de nouvelles fibres. "
        "Tordre plusieurs fils lâches en un seul plus épais peut aussi être Toveh — le Chabbat repose la production textile.",
    ),
    (
        "Learn about the melacha of Tzoveya",
        "¡Aprende Tzoveya — teñir en Shabat! 🎨 En el Mishkán: lana y pieles en azul, púrpura y rojo para las cortinas. "
        "Prohibido colorear permanentemente un material con pigmento; cosméticos que tiñen piel o uñas son preocupantes. "
        "Colorar comida solo para comer — generalmente permitido; teñir tela, papel o uñas para «fabricar» color — problema. "
        "En Shabat no creamos nuevos colores duraderos — disfrutamos los que ya están en la creación.",
        "Tzoveya — teindre le Chabbat ! 🎨 Au Mishkan : laine et peaux en bleu, pourpre et rouge pour les tentures. "
        "Interdit de colorer durablement un matériau ; produits qui colorent peau ou ongles sont problématiques. "
        "Colorer nourriture pour manger — en général permis ; teindre tissu, papier ou ongles — problème. "
        "Le Chabbat, on ne crée pas de nouvelles couleurs durables — on apprécie celles déjà là, sans teindre peau ou tissu.",
    ),
    (
        "Learn about one of the most surprising 'superpowers'",
        "¡El caso de la cebolla picante en kashrut! 🧅 El sabor suele transferirse con calor — pero davar jarif (cebolla, limón, ají) "
        "es tan «agresivo» que un cuchillo de carne frío que corta cebolla puede hacerla «carne» — sin fogón. "
        "Ojo al mezclar con lácteos después. Los Sabios clasifican estos alimentos aparte por su intensidad. "
        "Reto: revisa tus tablas de corte en la cocina kosher — y pregunta a tu rav si un cuchillo de carne tocó cebolla.",
        "Le cas de l'oignon piquant en cacherout ! 🧅 Le goût se transfère surtout à la chaleur — mais davar harif (oignon, citron, piment) "
        "est si « agressif » qu'un couteau viande froid qui coupe un oignon peut le rendre « viande » — sans feu. "
        "Attention aux produits laitiers ensuite. Les Sages classent ces aliments à part pour leur intensité. "
        "Défi : vérifiez vos planches à découper casher — et demande à ton rav si un couteau « viande » a touché un oignon.",
    ),
    (
        "Learn about the gid ha-nasheh — the forbidden sciatic nerve",
        "Gid hanáshe — el nervio ciático prohibido; una de las pocas mitzvot con razón explícita en la Torá "
        "(Bereshit 32:33): Yaakov luchó toda la noche con el ángel y quedó cojeando al amanecer. "
        "En kashrut se extrae de la cadera del animal; muchos comen solo «glatt» donde se revisa a fondo. "
        "Recuerda la lucha de Yaakov y la debilidad humana incluso tras la victoria espiritual. "
        "El gid hanáshe nos recuerda que incluso los grandes pueden cojear tras una noche de lucha.",
        "Gid hanashe — le nerf sciatique interdit ; une des rares mitsvot avec raison explicite dans la Torah "
        "(Berechit 32:33) : Yaakov lutta toute la nuit avec l'ange et boita à l'aube. "
        "En cacheroute on l'extrait de la hanche ; beaucoup ne mangent que « glatt » avec contrôle approfondi. "
        "Souvenez-vous de la lutte de Yaakov et de la fragilité humaine même après la victoire spirituelle. "
        "Le gid hanashe rappelle que même les grands peuvent boiter après une nuit de lutte.",
    ),
    (
        "Learn about the little known melacha of Maisach",
        "¡Aprende sobre Maisach — urdir el telar en Shabat! 🪡 Tensa los hilos verticales (urdimbre) antes de tejer — "
        "en el Mishkán preparaban la base de las cortinas monumentales. "
        "Prohibido montar el telar, enhebrar el bastidor de urdimbre o estirar cuerdas paralelas listas para el tejido. "
        "Incluso la infraestructura de la producción descansa en Shabat — no solo el tejido final (Oreg). "
        "Montar un telar, aunque no tejas, sigue siendo un paso prohibido en la cadena textil.",
        "Maisach — mettre en place la chaîne verticale le Chabbat ! 🪡 Tendre les fils de chaîne avant le tissage — "
        "au Mishkan on préparait la base des tentures monumentales. "
        "Interdit de monter le métier, enfiler le cadre de chaîne ou tendre des fils parallèles pour tisser ensuite. "
        "Même l'infrastructure de production se repose le Chabbat — pas seulement le tissage final (Oreg). "
        "Préparer un métier à tisser, même sans filer, reste une étape interdite de la chaîne textile.",
    ),
    (
        "Learn about treifot — internal defects",
        "Treifot — defectos internos que vuelven treif un animal aunque el shejitá fuera correcta. "
        "Foco principal: pulmones (bodek tras cada sacrificio). Pulmón perforado es causa frecuente de descarte. "
        "«Glatt» (pulmones lisos) es estándar más estricto que kosher regular con adhesiones revisables. "
        "Muestra cuán seria es la halajá de lo que entra al cuerpo — no basta matar bien si el animal estaba enfermo. "
        "El bodek revisa cada pulmón — la kashrut exige salud interior, no solo un buen corte.",
        "Treifot — défauts internes rendant l'animal treif malgré une shehita correcte. "
        "Focus : poumons (bodek après chaque abattage). Poumon perforé est cause fréquente de rejet. "
        "« Glatt » (poumons lisses) est plus strict que casher régulier avec adhérences révisables. "
        "Montre la rigueur halakhique de ce qui entre dans le corps — une bonne abattage ne suffit pas si l'animal était malade. "
        "Le bodek vérifie chaque poumon — la kashrout exige santé intérieure, pas seulement un bon coup de couteau.",
    ),
    (
        "Learn the Rambam's precise definition of teshuvah",
        "Teshuvá según el Rambam (Hilchot Teshuvá 1:1) — mitzvá positiva al pecar. Cuatro pasos: "
        "(1) Azivat hajait — dejar el pecado. (2) Jarata — arrepentimiento genuino. "
        "(3) Kabbalat leatid — compromiso de no repetir. (4) Vidui — confesión verbal en voz alta; "
        "el remordimiento en silencio no basta. Nombra el pecado, lamenta y te comprometes a cambiar.",
        "Teshouva selon le Rambam (Hilchot Teshouva 1:1) — mitsva positive en cas de faute. Quatre étapes : "
        "(1) Azivat hahait — cesser le péché. (2) Harata — regret sincère. "
        "(3) Kabbalat leatid — ne pas répéter. (4) Vidui — confession à voix haute ; "
        "le regret silencieux ne suffit pas.",
    ),
    (
        "Learn the step-by-step process of bringing a korban",
        "Ofrecer un korban — cuatro actos en orden: (1) Shejitá — sacrificio (cualquier israelita adulto). "
        "(2) Kabbalá — el kohen recoge la sangre en vaso sagrado. (3) Jolajá — lleva la sangre al altar. "
        "(4) Zeriká — rocía la sangre. Fuera de orden — pasul. La kavaná importa (p. ej. pigul).",
        "Offrir un korban — quatre actes dans l'ordre : (1) Shehita — abattage (tout Israélite adulte). "
        "(2) Kabbalah — le kohen reçoit le sang. (3) Holahah — porte au autel. (4) Zerikah — aspersion. "
        "Hors ordre — pasoul. La kavana compte (p. ex. pigoul).",
    ),
    (
        "Discover the righteous Tzadik (צ)!",
        "¡Descubre la letra Tzadik (צ)! Representa al tzadik — justo con acciones equilibradas. "
        "Su forma: Nun (נ) con Yud (י) encima — humildad (nun inclinada) unida a D-s. "
        "La forma final (ץ) baja de la línea: su influencia llega hasta lo más bajo. "
        "Reto: equilibra bondad y límites en tus relaciones.",
        "Découvrez la lettre Tsadik (צ) ! Elle représente le juste aux actes équilibrés. "
        "Forme : Noun (נ) avec Yod (י) au-dessus — humilité liée à D.ieu. "
        "La forme finale (ץ) descend sous la ligne. "
        "Défi : équilibrez bonté et limites.",
    ),
    (
        "Explore the power of Mem (מ)!",
        "¡Explora la letra Mem (מ)! 💧 Representa el agua (mayim) — fuente de vida. "
        "Como el agua, la sabiduría de la Torá fluye de lo alto a lo humilde. "
        "Mem abierto (מ) y mem sofit cerrado (ם): parte de la Torá revelada, parte oculta.",
        "Explorez la lettre Mem (מ) ! 💧 Elle représente l'eau (mayim). "
        "Comme l'eau, la sagesse de la Torah coule du haut vers l'humble. "
        "Mem ouvert (מ) et mem final fermé (ם) : Torah révélée et cachée.",
    ),
    (
        "Explore the power of Yud (י)!",
        "¡Explora la letra Yud (י)! La más pequeña — humildad — y primera letra del Nombre santo. "
        "Un punto: incluso el acto bueno más pequeño puede tener impacto infinito. Valor 10 — los Diez Mandamientos.",
        "Explorez la lettre Yod (י) ! La plus petite — humilité — et première lettre du Nom saint. "
        "Un point : le plus petit acte de bonté peut avoir un impact infini. Valeur 10 — les Dix Commandements.",
    ),
    (
        "Discover the power of Divine protection!",
        "Recuerda (uno de los diez recuerdos diarios) cómo D-s convirtió las maldiciones de Bilam en bendiciones. "
        "Contrataron a un hechicero para maldecir a Israel — y D-s lo hizo proclamar «¡Cuán buenas son tus tiendas, Yaakov!» "
        "que decimos cada mañana. Lección: la negatividad ajena puede transformarse en bendición.",
        "Rappelez (un des dix souvenirs) comment D.ieu transforma les malédictions de Bilam en bénédictions. "
        "On engagea un sorcier — et D.ieu fit proclamer « Que tes tentes sont bonnes, Yaakov ! » "
        "que nous disons chaque matin.",
    ),
    (
        "Practice the power of positive speech!",
        "Practica el habla positiva: comprométete — sin lashon hará innecesaria — durante 20 minutos con amigos 🤐. "
        "Cada vez que retienes algo negativo es una mitzvá aparte. ¡Muchas mitzvot en poco tiempo!",
        "Pratiquez la parole positive : pas de lashon hara inutile pendant 20 minutes avec des amis 🤐. "
        "Chaque fois que vous retenez une parole négative est une mitsva à part.",
    ),
    (
        "Strengthen your trust in G-d!",
        "Fortalece la confianza en D-s: recuerda la maná en el desierto — alimento del cielo cada día. "
        "Cada uno recibía exactamente un omer; lo guardado se echaba a perder. "
        "El viernes doble por Shabat. Lección: D-s provee hoy también, aunque de formas menos obvias.",
        "Renforcez la confiance en D.ieu : rappelez la manne au désert — nourriture du ciel chaque jour. "
        "Chacun recevait un omer exact ; ce qu'on gardait pourriait. "
        "Le vendredi double portion pour Chabbat.",
    ),
    (
        "Experience the power of Psalm 5!",
        "¡Experimenta el poder del Salmo 5! 🌅 Oración matutina de David — empezar el día con fe. "
        "El salmo enseña que D-s escucha nuestra voz al amanecer. "
        "Reto: léelo esta mañana con esperanza.",
        "Expérimentez la puissance du Psaume 5 ! 🌅 Prière matinale de David — commencer la journée avec foi. "
        "D.ieu écoute notre voix à l'aube. "
        "Défi : lisez-le ce matin avec espérance.",
    ),
    (
        "Guard your unique identity!",
        "¡Guarda tu identidad única! 👕 El Rambam (Hilchot Avodat Kochavim cap. 11): no imitar costumbres no judías en vestido o conducta. "
        "No es superioridad — es mantener identidad espiritual. Costumbres con origen idólatra pueden estar prohibidas. "
        "Reto: enorgullece de tu distinción judía.",
        "Gardez votre identité unique ! 👕 Le Rambam (Hilchot Avodat Kochavim ch. 11) : ne pas imiter les coutumes non juives. "
        "Ce n'est pas la supériorité — c'est préserver l'identité spirituelle. "
        "Défi : soyez fiers de votre distinction juive.",
    ),
    (
        "Keep the Temple courtyard holy!",
        "¡Guarda santo el patio del Templo! 🌳 El Rambam (cap. 6): no plantar árboles en el patio del Templo, "
        "aunque sea por belleza — los idólatras plantaban junto a sus altares. "
        "Servir a D-s es a Su manera, no a la nuestra aunque parezca bonito.",
        "Gardez saint le parvis du Temple ! 🌳 Le Rambam (ch. 6) : ne pas planter d'arbres dans le parvis — "
        "les idolâtres plantaient près de leurs autels. "
        "Servir D.ieu, c'est à Sa manière.",
    ),
    (
        "Learn from the Ramban's wisdom!",
        "¡Aprende de la sabiduría del Rambán! 📜 Carta poderosa a su hijo — leerla a diario ayuda a que las tefilot sean escuchadas. "
        "Centra en humildad y hablar con suavidad. Desafío: lee un consejo y practícalo hoy.",
        "Apprenez de la sagesse du Ramban ! 📜 Lettre puissante à son fils — la lire chaque jour aide les tefilot. "
        "Humilité et parole douce. Défi : lisez un conseil et pratiquez-le.",
    ),
    (
        "Protect others from spiritual harm!",
        "¡Protege a otros del daño espiritual! 🛡️ No animar a nadie a idolatría o falsas creencias. "
        "Como no darías consejo físico dañino, cuida el consejo espiritual — "
        "el Rambam: desviar a alguien espiritualmente es peor que daño físico. "
        "Reto: comparte algo que eleve espiritualmente.",
        "Protégez autrui du tort spirituel ! 🛡️ N'encouragez pas l'idolâtrie. "
        "Le Rambam : égarer spirituellement est pire qu'un tort physique. "
        "Défi : partagez quelque chose d'élevant.",
    ),
    (
        "Relive the most epic moment in history!",
        "¡Revive el momento del Sinaí! 🗻 Recuerda (uno de los diez recuerdos): montaña en llamas, truenos — "
        "y silencio al escuchar la voz de D-s. Las almas salieron de los cuerpos; pedimos a Moshé como intermediario. "
        "Los Sabios enseñan: tu alma estuvo allí; recordarlo reaviva esa conexión.",
        "Revivez le moment du Sinaï ! 🗻 L'un des dix souvenirs : montagne en feu, tonnerre — "
        "et silence en entendant la voix de D.ieu. Votre âme y était ; s'en souvenir réveille ce lien.",
    ),
    (
        "Show extra care to those who need it most!",
        "Más cuidado con viudas y huérfanos 🤗 — mitzvá de gentileza especial. "
        "Han sufrido pérdida y pueden sentirse vulnerables. El Talmud: D-s mismo es su guardián; "
        "al ayudarlos te asocias con Él. Reto: bondad extra con quien se sienta solo o vulnerable.",
        "Soin particulier pour veuves et orphelins 🤗 — mitsva de douceur. "
        "Le Talmud : D.ieu est leur gardien ; en les aidant vous vous associez à Lui.",
    ),
    (
        "Strengthen your focus on G-d!",
        "Fortalece el enfoque en D-s 🎯 — el Rambam: incluso pensar brevemente en otros «poderes» está prohibido. "
        "No solo idolatría: TODO viene de D-s. Cuando algo bueno pase, no digas «mi suerte» — "
        "recuerda que solo D-s es el poder verdadero.",
        "Renforcez votre focus sur D.ieu 🎯 — le Rambam : penser à d'autres « pouvoirs » est interdit. "
        "Tout vient de D.ieu ; pas « ma chance ».",
    ),
    (
        "Understand true prophecy!",
        "¡Aprende sobre profetas verdaderos! 🔍 Rambam cap. 7: el profeta debe ser sabio, de carácter fuerte y dueño de sus deseos. "
        "La profecía llega en estado de alegría; por eso músicos antes de profetizar. "
        "Servir a D-s con alegría abre canales espirituales.",
        "Prophétie vraie 👥 — Rambam ch. 7 : sage, caractère fort, maître de ses désirs. "
        "Elle vient dans la joie ; d'où les musiciens avant de prophétiser.",
    ),
    (
        "Learn about true prophets!",
        "¡Aprende sobre profetas verdaderos! 🔍 El Rambam enseña: una vez establecido un profeta genuino (como Moshé o Isaías), "
        "¡está prohibido ponerlo a prueba constantemente! Es como probar una y otra vez a un amigo de confianza — falta de fe. "
        "En cambio, haz lo que dice y confía en que es la voluntad de D-s. "
        "La profecía auténtica guía a Israel — no la pongas a prueba sin necesidad. "
        "Servir a D-s con alegría abre los canales de la profecía, enseña el Rambam.",
        "Découvrez les vrais prophètes ! 🔍 Le Rambam enseigne : une fois un prophète authentique établi (Moïse, Ésaïe), "
        "il est interdit de le mettre sans cesse à l'épreuve — comme tester sans fin un ami de confiance, manque de foi. "
        "Fais plutôt ce qu'il dit et crois que c'est la volonté de D.ieu. "
        "La prophétie authentique guide Israël — ne la mets pas à l'épreuve sans raison. "
        "Servir D.ieu avec joie ouvre les canaux de la prophétie, dit le Rambam.",
    ),
    (
        "Let go of grudges!",
        "¡Suelta rencores! 🕊️ Hay una mitzvá increíble: aunque no puedas vengarte, tampoco te quedes con el dolor. "
        "Ejemplo de la Torá: si alguien pide prestado, no digas «Claro, no soy como tú que me negaste» — presta de corazón. "
        "Guardar rencor es beber veneno esperando que el otro se enferme. "
        "Reto: reemplaza un rencor con una bendición.",
        "Lâchez rancune ! 🕊️ Même sans vous venger, ne gardez pas la blessure. "
        "Exemple torahique : si quelqu'un emprunte, ne dites pas « Comme toi qui m'as refusé » — prêtez de bon cœur. "
        "Garder rancune, c'est boire du poison. "
        "Défi : remplacez une rancune par une bénédiction.",
    ),
    (
        "Experience the beauty of creation!",
        "¡Experimenta la belleza de la creación! Busca un paisaje o mira una imagen de la naturaleza 🏔️. "
        "Desde montañas hasta flores pequeñas, cada parte del mundo de D-s está perfectamente diseñada. "
        "Tómate un momento para notar la increíble maestría a tu alrededor y agradece a D-s por crearlo todo. 🌎",
        "Expérimentez la beauté de la création ! Trouvez un paysage ou une image de la nature 🏔️. "
        "Chaque partie du monde de D.ieu est parfaitement conçue. "
        "Remarquez la maîtrise divine autour de vous et remerciez D.ieu. 🌎",
    ),
    (
        "Experience the joy of freedom!",
        "¡Experimenta la alegría de la libertad! 🌅 Recuerda el Éxodo de Egipto (uno de los diez recuerdos diarios): "
        "éramos esclavos con trabajo agotador, pero D-s lo cambió todo con milagros. "
        "Los egipcios que no nos pagaron terminaron dándonos su riqueza al salir. "
        "D-s puede transformar cualquier situación — de oscuridad a luz, de esclavitud a libertad. "
        "Cuando todo parezca difícil, recuerda: la salvación puede llegar en un instante.",
        "Expérimentez la joie de la liberté ! 🌅 Souvenez-vous de la Sortie d'Égypte : "
        "esclaves épuisés, D.ieu a tout renversé par des miracles. "
        "Le salut peut venir en un clin d'œil.",
    ),
    (
        "Walk in Hashem's ways",
        "Camina en los caminos de Hashem: V'Halachta B'Drachav! 👣 La Torá ordena: «Camina en todos Sus caminos» (Devar. 11:22). "
        "¿Cómo imitar a D-s, que es «fuego consumidor»? El Talmud aclara: imitamos Sus actos, no Su esencia. "
        "Como Hashem vistió a los desnudos, tú vistes a los desnudos ayudando a los pobres. "
        "Como visitó a los enfermos, consoló a los dolientes y enterró a los muertos — tú haces lo mismo. "
        "El Rambam añade: si D-s es misericordioso y santo — sé misericordioso y santo. "
        "«Halajá» comparte raíz con «caminar»: cultivar el carácter también es halajá.",
        "Marchez dans les voies de Hachem : V'Halachta B'Drachav ! 👣 « Marchez dans toutes Ses voies » (Devarim 11:22). "
        "On imite Ses actes, pas Son essence. Vêtir les nus, visiter les malades, consoler, enterrer — "
        "ce sont des mitsvot de 'hessed. Le Rambam : soyez miséricordieux et saint comme D.ieu.",
    ),
    (
        "Honor Torah teachers!",
        "¡Honra a los maestros de Torá! 👨‍🏫 El Rambam enseña: un maestro de Torá es como un padre que nos da vida eterna. "
        "El respeto es tan grande que si tu padre y tu maestro cargan peso, ayudas primero al maestro "
        "(salvo que tu padre también sea erudito en Torá). "
        "Reto de hoy: muestra aprecio a quien te enseñó Torá — mensaje, llamada o pequeño regalo.",
        "Honorez les maîtres de Torah ! 👨‍🏫 Le Rambam : un maître de Torah est comme un père qui donne la vie éternelle. "
        "Si père et maître portent un fardeau, aidez d'abord le maître (sauf si le père est aussi savant). "
        "Défi : remerciez quelqu'un qui vous a enseigné la Torah.",
    ),
    (
        "Learn about the Torah's obligations of respect for Torah scholars",
        "Conoce el respeto a eruditos y ancianos 🙌 «Delante de las canas te levantarás» (Vaikrá 19:32). "
        "Dos mitzvot: ancianos (70+) y eruditos en Torá. "
        "Ancianos: levántate (o al menos un gesto) cuando se acerquen a unos dos metros. "
        "Eruditos: levántate aunque sean jóvenes si tienen sabiduría significativa. "
        "Ante un gadol, levántate al entrar y no te sientes hasta que llegue a su lugar. "
        "No avergüences ni te burles de un erudito — el Talmud lo compara con destruir el Templo.",
        "Respect des savants et des anciens 🙌 « Devant les cheveux blancs, lève-toi » (Vayikra 19:32). "
        "Deux mitsvot : personnes âgées (70+) et talmid 'hakham. "
        "Levez-vous quand ils approchent à environ deux mètres ; pour un gadol, dès l'entrée dans la pièce — "
        "ne vous asseyez pas avant qu'il soit à sa place. "
        "Ne ridiculisez pas un savant — le Talmud compare cela à la destruction du Temple ; "
        "l'honneur des sages soutient toute la communauté.",
    ),
    (
        "Learn an interesting kashrut detail",
        "Detalle curioso de kashrut: carne y leche solo prohíbe cocinar carne kosher con leche — "
        "no carne no kosher como cerdo (ya prohibida por especie). "
        "El cerdo sigue prohibido comer, pero halájicamente podrías cocinarlo en leche. "
        "Aprende más sobre kashrut.",
        "Détail de cacherout : viande et lait interdisent de cuire viande casher avec du lait — "
        "pas le porc (déjà interdit). Le porc reste interdit à manger, mais on pourrait le cuire au lait halakhiquement.",
    ),
    (
        "Learn about the melacha of Zorea",
        "¡Aprende Zorea — sembrar y promover crecimiento en Shabat! 🌱 No solo semillas en tierra: injertos, regar el jardín activamente, "
        "plantar bulbos o esquejes. En el Mishkán: granos y plantas para tintes. "
        "Regar activa el crecimiento — prohibido en Shabat, incluso plantas de interior que se marchitan sin agua. "
        "Regamos antes del anochecer y nos apartamos. Shabat es dejar que el mundo crezca sin forzar producción.",
        "Zorea — semer et favoriser la croissance le Chabbat ! 🌱 Pas seulement des graines : greffes, arroser activement le jardin, "
        "planter bulbes ou boutures. Au Mishkan : grains et plantes à teinture. "
        "Arroser active la croissance — interdit le Chabbat, même plantes d'intérieur. "
        "On arrose avant la nuit et on s'écarte. Le Chabbat, on laisse le monde croître sans forcer la production — "
        "même les plantes d'intérieur ne doivent pas être arrosées activement ce jour-là.",
    ),
    (
        "$holidayName is about to begin",
        "$holidayName está a punto de comenzar. Termina lo que estás haciendo, apaga el teléfono y prepárate para recibir este Yom Tov con alegría.\n\n"
        "Nuestros Sabios enseñan que regocijarse en Yom Tov es en sí misma una mitzvá — "
        "que tu observancia traiga bendición y recompensa divina a ti y a tu hogar.",
        "$holidayName va commencer. Terminez ce que vous faites, éteignez le téléphone et accueillez ce Yom Tov avec joie.\n\n"
        "Se réjouir à Yom Tov est une mitsva — que votre observance apporte bénédiction à vous et votre foyer.",
    ),
    (
        "Add your own mitzvah!",
        "¡Añade tu propia mitzvá! ✡ El Talmud enseña que cada persona tiene su forma única de servir a D-s. "
        "Piensa en una mitzvá que traiga más luz al mundo y envíala en «Añadir una mitzvá» en la app.",
        "Ajoutez votre propre mitsva ! ✡ Le Talmud : chacun sert D.ieu à sa façon. "
        "Pensez à une mitsva qui apporte plus de lumière et soumettez-la dans l'app.",
    ),
    (
        "Contemplate #5 of the Constant Mitzvot",
        "Contempla la #5 de las Mitzvot Constantes: reverencia a Hashem (Yirat Hashem) 🤩 "
        "No es solo miedo — es reconocer el inmenso poder, majestad y trascendencia de D-s. "
        "Imagina el Mar Rojo partido o estar en el Sinaí: esa grandeza abrumadora es Yirat Hashem. "
        "Esta conciencia ayuda a valorar cada mitzvá y que cada acción importe. "
        "Fuente: Devar. 6:13; Rambam, Sefer HaMitzvot, mitzvá positiva 4.",
        "Contemplez la #5 des mitsvot constantes : crainte de Hachem (Yirat Hashem) 🤩 "
        "Reconnaître la puissance et la majesté de D.ieu — pas seulement la peur. "
        "Source : Devarim 6:13 ; Rambam, mitsva positive 4.",
    ),
    (
        "Discover G-d's unique unity!",
        "¡Descubre la unidad única de D-s! ✡ El Rambam revela en halajá 7: la unidad de D-s no es como la nuestra. "
        "Una persona es «una» pero tiene partes; un grupo es «uno» pero de muchos. "
        "La unidad de D-s es absoluta — sin partes ni divisiones. "
        "Reto: contempla una unidad tan completa que no hay nada fuera de Ella.",
        "Découvrez l'unité unique de D.ieu ! ✡ Le Rambam : l'unité divine n'a pas de parties. "
        "Défi : imaginez une unité si complète qu'il n'y a rien en dehors.",
    ),
    (
        "Elevate your physical self!",
        "¡Eleva tu ser físico! 🚿 Toma una ducha o baño con la intención de honrar a D-s con un cuerpo limpio. "
        "Hillel enseñó que cuidar el cuerpo es mitzvá. "
        "Con intención santa, incluso lavarse se vuelve acto espiritual. Fuente: Vayikrá Rabbá 34:3.",
        "Élevez votre corps ! 🚿 Douche ou bain en l'honneur de D.ieu — Hillel : soigner le corps est une mitsva.",
    ),
    (
        "Do the mitzvah of experiencing both love and awe of G-d!",
        "¡Haz la mitzvá de amor y reverencia a D-s! ❤️ Mira un atardecer, sube una montaña, siente la lluvia. "
        "Te harás pequeño ante el Creador — como amar un león majestuoso: belleza y respeto. "
        "Amor y temor sano ante el poder de D-s.",
        "Faites la mitsva d'amour et de crainte de D.ieu ! ❤️ Coucher de soleil, montagne, pluie — "
        "petitesse devant le Créateur et respect sain.",
    ),
    (
        "Buy yourself something special to wear for Shabbat",
        "Cómprate algo especial para Shabat — camisa, bufanda o calcetines elegantes 👔 "
        "Los Sabios enseñan que lo gastado en honrar Shabat D-s lo repaga de formas misteriosas. "
        "Vístelo viernes o sábado y añade honor al día santo.",
        "Achetez-vous quelque chose de spécial pour Chabbat 👔 Les Sages : D.ieu rembourse ce qu'on dépense pour honorer Chabbat.",
    ),
    (
        "Learn some halacha - When doing the mitzvah of saying the Shema",
        "Detalle halájico del Shemá: al recitar los versos que proclaman la unicidad de D-s, "
        "si caminas, detente un momento para el primer versículo — merece atención plena. "
        "Luego puedes seguir caminando con el resto.",
        "Détail halakhique du Shema : à pied, arrêtez-vous pour le premier verset, puis continuez en marchant.",
    ),
    (
        "Appreciate the gift of rest!",
        "¡Aprecia el regalo del descanso! Agradece a D-s por dormir y despertar renovado 😴. "
        "D-s nos dio este reinicio diario que renueva cuerpo y mente 🌅",
        "Appréciez le don du repos ! Remerciez D.ieu de dormir et vous réveiller renouvelé 😴",
    ),
    (
        "Dive into the deep end and learn the kosher fish signs",
        "¡Descubre las señales del pescado kosher! 🐟 Para ser kosher necesita aletas y escamas removibles. "
        "El Talmud enseña: quien tiene escamas válidas tiene aletas también. "
        "Deben verse a simple vista y quitarse sin dañar la piel.\n\n"
        "🛡️ Símbolo: escamas como armadura espiritual; aletas, fuerza para nadar contra la corriente.\n\n"
        "⚔️ Debates: pez espada (escamas de joven, las pierde al crecer); esturión (escamas ganoides fijas — "
        "muchos lo consideran no kosher; el caviar kosher suele ser de otros peces).\n\n"
        "¿Sabías? La sangre de pescado está permitida (a diferencia de carne y aves), "
        "aunque se dejan escamas visibles para que no parezca sangre prohibida.",
        "Découvrez les signes du poisson casher ! 🐟 Nageoires et écailles amovibles. "
        "Le Talmud : écailles valides ⇒ nageoires aussi. Débats : espadon, esturgeon. "
        "Le sang du poisson est permis (contrairement à viande et volaille).",
    ),
    (
        "Learn about the mezuzah — and the surprising halachic criteria",
        "¡Aprende sobre la mezuzá! 🚪 La Torá dice «escríbelas en los postes de tu casa» (Deuteronomio 6:9). "
        "No toda puerta califica: el Rambam exige espacio mínimo (4×4 codos), techo, dos postes, dintel y uso habitacional. "
        "Un vestidor puede requerirla; un baño no (por dignidad); un garaje quizá.\n\n"
        "Colocación: lado derecho al entrar, tercio superior del poste, inclinada hacia adentro. "
        "Ashkenazíes a ~45° (compromiso Rambam/Rashi); sefardíes vertical. "
        "Altura: tercio superior o altura de hombro. El klaf debe estar escrito por un sofer calificado — impreso no vale. "
        "La mezuzá protege quien la tiene en las puertas requeridas, incluso cuando no está en casa.",
        "Découvrez la mezouza ! 🚪 Deutéronome 6:9 — pas toute porte ne qualifie (Rambam : 4×4 coudées, toit, montants, linteau). "
        "Côté droit en entrant, tiers supérieur du montant, inclinée vers l'intérieur. "
        "Ashkénazes ~45° (compromis Rambam/Rachi) ; séfarades verticale. "
        "Le klaf doit être écrit par un sofer qualifié — imprimé ne vaut pas. "
        "La mezouza protège celui qui l'a aux portes requises, même en son absence.",
    ),
    (
        "Light up the night: Learn about Chanukah candles and Pirsumei Nisa",
        "¡Ilumina la noche: velas de Janucá y Pirsumei Nisá! 🕎 No es solo disfrute personal — proclamar públicamente el milagro del aceite.\n\n"
        "Ubicación: idealmente en la puerta, lado izquierdo (mezuzá a la derecha — rodeado de mitzvot). "
        "En piso alto, ventana visible desde la calle. En peligro, se encendía en el interior sobre la mesa.\n\n"
        "Horario: al anochecer (tzet ha-kojavim), ardiendo al menos 30 minutos en la noche. "
        "Bendiciones: primera noche tres, luego dos; añade una vela cada noche según tu minhag. "
        "Que la luz de Janucá recuerde: lo pequeño puede vencer a lo grande.",
        "Illumine la nuit : bougies de 'Hanoucca et Pirsoumei Nissa ! 🕎 Proclamer le miracle de l'huile — pas seulement pour toi.\n\n"
        "À la porte, côté gauche (mezouza à droite) ; fenêtre visible depuis la rue si tu es en étage ; sur la table si besoin.\n\n"
        "Allumage au crépuscule (tzeit), minimum 30 minutes après la nuit. "
        "Bénédictions : trois la première nuit, deux ensuite ; ajoute une bougie chaque soir selon ton minhag. "
        "Que la lumière de 'Hanoucca rappelle : le petit peut vaincre le grand.",
    ),
    (
        "Learn about Borer — the famous 'selecting' melacha",
        "¡Aprende sobre Borer — la melajá de «seleccionar» que cambia cómo comes en Shabat! 🥗 "
        "Borer prohíbe separar una mezcla eligiendo lo que no quieres de lo que sí. "
        "Para que sea permitido en Shabat deben cumplirse tres condiciones: elegir lo bueno del malo (ochel mitoj psolet), "
        "hacerlo a mano (b'yad) y para uso inmediato (m'yad). "
        "No uses colador, tamiz o pelador dedicado — eso convierte el acto en melajá prohibida.",
        "Découvrez Borer — la melakha de « trier » qui change comment tu manges à Chabbat ! 🥗 "
        "Borer interdit de séparer un mélange en choisissant ce que tu ne veux pas de ce que tu veux. "
        "Trois conditions pour que ce soit permis : bon du mauvais (ochel mitokh psolet), "
        "à la main (b'yad) et pour usage immédiat (m'yad). "
        "Pas de passoire, tamis ou éplucheur dédié — cela rend l'acte interdit. "
        "Le Chabbat transforme même un repas simple en occasion d'apprendre ces règles subtiles.",
    ),
    (
        "Be a judge of merit: Learn about Dan L'Kaf Zechut",
        "Sé juez a favor del mérito: Dan L'Kaf Zechut (juzgar favorablemente) ⚖️ "
        "La Torá dice «B'tzedek tishpot amiteja» — juzga a tu prójimo con justicia (Levítico 19:15). "
        "El Rambam lo define como obligación positiva. El Talmud (Shabat 127b): «Con cada persona, inclina la balanza hacia el mérito». "
        "Si alguien de buen carácter hace algo dudoso, asume que te falta contexto. "
        "Solo ante un patrón claro de mal debes asumir lo peor. El Jafetz Jaim advierte que pensar mal sin motivo también viola esto. "
        "Pirkei Avot (2:4): no juzgues hasta llegar a su lugar.",
        "Juge favorablement : Dan L'Kaf Zekhout ! ⚖️ Lévitique 19:15 — obligation positive selon le Rambam. "
        "Talmud (Chabbat 127b) : incline la balance vers le mérite. Pirkei Avot (2:4) : ne juge pas avant d'être à sa place. "
        "Si quelqu'un de bon caractère fait quelque chose d'ambigu, suppose qu'il te manque du contexte. "
        "Seul un schéma clair de faute justifie la pire interprétation. Le Hafets Haim : même une pensée négative sans raison peut violer cela.",
    ),
    (
        "Learn about the Temple Mount's dimensions!",
        "¡Aprende las dimensiones del Monte del Templo! 🏛️ El Monte Moriah, donde estuvo el Beit HaMikdash, medía 500 codos por 500, rodeado de muralla. "
        "¿Sabías? Excavaron la tierra debajo y la sostuvieron con arcos para evitar tumat meit (impureza ritual) de tumbas bajo el suelo. "
        "Cada detalle del Templo tenía significado espiritual — estudiar sus medidas es estudiar la presencia de D-s en el mundo. "
        "El Rambam dedica capítulos enteros a las dimensiones sagradas del Monte.",
        "Découvrez les dimensions du Mont du Temple ! 🏛️ Le mont Moriah, où se dressait le Beit HaMikdash, mesurait 500 coudées sur 500, entouré d'un mur. "
        "On a creusé la terre en dessous et soutenu par des arches pour éviter toumat meit des tombes sous le sol. "
        "Chaque détail du Temple avait un sens spirituel — étudier ses mesures, c'est étudier la présence de D.ieu dans le monde. "
        "Le Rambam consacre des chapitres entiers aux dimensions sacrées du Mont.",
    ),
    (
        "Conquer your grudges: Learn about Nekamah",
        "Vence tus rencores: Nekamá (venganza) y Netirá (guardar rencor) 🕊️ "
        "La Torá prohíbe ambos: «No te vengarás ni guardarás rencor… amarás a tu prójimo como a ti mismo» (Levítico 19:17-18). "
        "El Rambam (Hiljot De'ot 7:7-8) da ejemplos vivos de la diferencia sutil: venganza es responder al mismo golpe; "
        "guardar rencor es albergar la ofensa en el corazón aunque no respondas. "
        "El perdón libera el alma más que la ira justa. Reto: si alguien te ofendió, suelta hoy una vieja herida — "
        "no hace falta decirlo en voz alta, pero con sinceridad ante D-s.",
        "Vaincs tes rancunes : Nekama (vengeance) et Netira (rancune) ! 🕊️ Lévitique 19:17-18 interdit les deux. "
        "Le Rambam (Hilkhot De'ot 7:7-8) illustre la nuance : vengeance — répondre au même coup ; rancune — garder l'offense au cœur. "
        "Le pardon libère l'âme plus que la colère juste. Défi : si quelqu'un t'a blessé, lâche aujourd'hui une vieille blessure — "
        "pas forcément à voix haute, mais sincèrement devant D.ieu.",
    ),
    (
        "Learn about Dosh — threshing",
        "¡Aprende sobre Dosh — trillar y extraer en Shabat! 🌾 "
        "En el Mishkán se separaba grano de paja para el pan de la proposición. "
        "Hoy aparece al extraer líquido de sólidos: exprimir uvas u olivas es prohibición de Torá; "
        "los Sabios extendieron a otros frutos — por eso no exprimes naranjas frescas en Shabat. "
        "En Shabat no «procesamos» la naturaleza; la disfrutamos tal cual. "
        "Incluso exprimir un limón sobre pescado puede plantear dudas — pregunta a tu rav.",
        "Découvrez Dosh — battre et extraire à Chabbat ! 🌾 "
        "Au Mishkan on séparait le grain de la paille pour le pain de proposition. "
        "Aujourd'hui : presser raisin ou olive = Torah ; les Sages ont étendu à d'autres fruits — "
        "pas de jus d'orange fraîchement pressé à Chabbat. "
        "Le Chabbat, on ne « traite » pas la nature — on la savoure telle quelle, sans extraire comme en semaine. "
        "Même presser un citron sur du poisson peut poser question — demande à ton rav.",
    ),
    (
        "Learn about Losh — kneading",
        "¡Aprende sobre Losh — amasar y formar una masa en Shabat! 🍞 "
        "Harina + agua + mezcla = una masa unificada. En el Mishkán se mezclaban polvos de hierbas con agua para tintes "
        "y se amasaba para el pan de la proposición. "
        "Prohibido combinar polvo fino con líquido en pasta cohesiva — afecta cereal infantil, avena instantánea, mostaza. "
        "Para evitar violación de Torá: cambiar el orden (líquido primero) y mezclar con shinui (movimiento inusual).",
        "Découvrez Losh — pétrir à Chabbat ! 🍞 "
        "Farine + eau + mélange = pâte unifiée. Au Mishkan on mélangeait poudres d'herbes et eau pour teintures "
        "et on pétrissait pour le pain de proposition. "
        "Interdit de combiner poudre fine et liquide en pâte cohésive — céréales pour bébé, avoine instantanée, moutarde. "
        "Pour éviter une violation toranique : ordre inversé (liquide d'abord) et shinui (geste inhabituel). "
        "Même mélanger avoine instantanée ou céréales pour bébé demande attention — le Chabbat protège la « masse » unifiée.",
    ),
    (
        "Learn about Musaf — the additional prayer",
        "¡Aprende sobre Musaf — la oración adicional en Shabat, Rosh Jodesh y Yom Tov! 🎺 "
        "La Amidá de Musaf («adicional») corresponde al korban extra del Templo en estos días. "
        "En un día laborable solo el Tamid; en Shabat dos corderos adicionales; en Rosh Hashaná, Rosh Jodesh y fiestas, muchos korbanot más. "
        "Hoy lo conmemoramos con la Amidá de Musaf tras Shaharit — cada ocasión tiene su texto propio en el sidur. "
        "Aprende el significado de las berajot de Musaf para profundizar tu oración festiva.",
        "Moussaf — prière additionnelle à Chabbat, Rosh 'Hodesh et Yom Tov ! 🎺 "
        "L'Amida de Moussaf correspond au korban supplémentaire du Temple — deux agneaux à Chabbat, bien plus aux fêtes. "
        "Aujourd'hui on la récite après Shaharit ; chaque occasion a son texte propre dans le sidour. "
        "Les bénédictions évoquent les korbanot du Temple et prient pour le rétablissement du service — "
        "apprends leur sens pour approfondir ta prière festive.",
    ),
    (
        "Learn about Kiddush Yadayim V'Raglayim",
        "¡Aprende sobre Kidush Yadayim ve-Raglaim — la santificación obligatoria de manos y pies del kohen antes del servicio en el Beit HaMikdash! 🌊 "
        "Mano derecha sobre pie derecho y mano izquierda sobre pie izquierdo; agua fresca simultánea del Kiyor de cobre u otro vaso sagrado, "
        "con fuerza humana directa (koaj gavra). Sin esto, el servicio del kohen es inválido. "
        "Se anula con chatzitzá (suciedad, vendajes), si amaneció tras velar toda la noche, "
        "si salió del patio del Templo, contrajo tumá o usó el baño. "
        "Nos recuerda que el servicio divino exige santidad sin compromisos.",
        "Découvrez Kiddoush Yadayim veraglayim — sanctification des mains et pieds du kohen au Beit HaMikdash ! 🌊 "
        "Main droite sur pied droit, main gauche sur pied gauche ; eau fraîche simultanée du Kiyor de cuivre ou autre vase sacré, "
        "avec koah gavra (force humaine directe). Sans cela, le service est invalide. "
        "Annulé par chatzitsa (saleté, pansements), si l'aube suit une veille entière, "
        "si le kohen sort du parvis, contracte toumah ou utilise les toilettes — il doit se relaver.",
    ),
    (
        "Get ready for a hunt! Bedikat Chametz",
        "¡Prepárate para la búsqueda! Bedikat Jametz (búsqueda nocturna de levadura) 🔦 "
        "La noche antes de la víspera de Pesaj (noche del 13 de Nisán), tras el anochecer, buscamos jametz formalmente a la luz de una vela "
        "(cera de abeja tradicional; linterna también vale). Es asunto familiar: pluma, cuchara de madera, bolsa de papel. "
        "Berajá antes de buscar. ¿Diez pedazos de pan escondidos? Asegura que la berajá no sea en vano si ya limpiaste todo. "
        "Tras la búsqueda, Kol Chamira en arameo anula el jametz no visto. "
        "Al día siguiente, biur jametz por la mañana. ¡Que tu búsqueda sea fructífera!",
        "Préparez la chasse ! Bedikat 'Hamets 🔦 "
        "La veille de Pessah (13 Nissan), après la nuit, recherche à la bougie. Bénédiction, plume, Kol Chamira, biour le lendemain.",
    ),
    (
        "Learn about the power of Tehillim (Psalms)!",
        "¡Aprende el poder de Tehillim (Salmos)! Empieza con el Salmo 1 — la alegría de quien se deleita en la Torá 📖. "
        "El rey David compuso estas oraciones en sus momentos más altos y más bajos; han sido consuelo y protección para el pueblo judío a lo largo de la historia. "
        "Al leerlos, te conectas a una fuente antigua de vínculo con D-s. ✡ "
        "Misión: lee hoy un verso con intención — incluso un minuto de Tehillim cambia el día.",
        "Découvre le pouvoir de Tehillim (Psaumes) ! Commence par le Psaume 1 — la joie de celui qui prend plaisir à la Torah 📖. "
        "Le roi David a composé ces prières dans ses moments les plus hauts et les plus bas ; elles ont réconforté et protégé les Juifs tout au long de l'histoire. "
        "En les lisant, tu puises dans une source ancienne de lien avec D.ieu ! ✡ "
        "Mission : lis aujourd'hui un verset avec intention — même une minute de Tehillim change la journée.",
    ),
]
