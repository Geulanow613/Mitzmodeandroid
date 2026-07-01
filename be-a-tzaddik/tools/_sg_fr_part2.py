# -*- coding: utf-8 -*-
FR_PART2: list[str] = [
    # 51 — Melaben
    (
        "Blanchir, nettoyer ou rafraîchir un tissu. Cela va bien au-delà de la machine à laver — "
        "même enlever une tache fraîche en tamponnant avec de l'eau ou en frottant une tache "
        "avec l'intention de nettoyer peut violer Melaben. Même mouiller un vêtement sale "
        "peut être considéré comme le début du processus de lessive."
    ),
    # 52 — Menapetz
    (
        "Brosser ou peigner agressivement une matière brute et emmêlée pour séparer et aligner les fibres. "
        "Pour les cheveux, une brosse à poils durs qui sépare et arrache les mèches avec force "
        "relève de cette mélakha ou de Gozez. Le Chabbat, utilisez une brosse souple ou un peigne à larges dents "
        "doucement sur des cheveux secs."
    ),
    # 53 — Tzovea
    (
        "Colorer de façon permanente ou améliorer l'apparence d'un matériau à l'aide de pigments. "
        "Cela concerne les cosmétiques : un maquillage qui tache la peau pose problème le Chabbat. "
        "Le principe profond est de ne pas altérer de façon durable l'apparence de votre environnement "
        "selon vos désirs le jour du repos."
    ),
    # 54 — Toveh
    (
        "Prendre des fibres lâches et désordonnées, les tirer et les tordre en un fil continu. "
        "Toute action créant du fil, de la laine ou de la corde à partir de matière brute est interdite. "
        "Tordre plusieurs fils existants ensemble pour former une corde ou une ficelle plus épaisse est également inclus."
    ),
    # 55 — Meisach
    (
        "Organiser et fixer les fils de chaîne verticaux sur un métier à tisser, en préparant son infrastructure pour le tissage. "
        "Cela enseigne que le repos du Chabbat exige de cesser non seulement les actes créateurs finaux, "
        "mais aussi la planification et la structuration en coulisse du travail créateur."
    ),
    # 56 — Oseh Beit Batai Neirin
    (
        "Assembler les lisses et les cadres d'un métier à tisser qui permettent au tisseur de séparer les fils de chaîne. "
        "Comme Meisach, c'est purement préparatoire — cela crée l'infrastructure d'une création future, "
        "que le Chabbat nous oblige à interrompre."
    ),
    # 57 — Oreig
    (
        "Entrelacer deux séries de fils à angle droit pour former un tissu. "
        "Le Talmud indique que tisser seulement deux fils horizontaux parallèles dans une trame verticale "
        "suffit à violer cette interdiction. Certains types de broderie dense, de point de croix "
        "et de vannerie partagent la même mécanique structurelle."
    ),
    # 58 — Potzeia
    (
        "Défaire délibérément ou séparer un matériau tissé ou tressé dans le but de réutiliser ou retisser les fils. "
        "Tirer un fil desserré d'un pull qui défait une rangée de mailles en est un exemple concret. "
        "Le Chabbat interdit à la fois de construire et de déconstruire le monde matériel."
    ),
    # 59 — Koshair
    (
        "Créer un nœud permanent de qualité professionnelle destiné à durer longtemps. "
        "Un nœud à la fois professionnel et permanent est interdit par la Torah. "
        "Un nœud temporaire simple destiné à être défait dans les 24 heures est permis. "
        "Un lacet classique sur une basket convient ; un double nœud serré destiné à rester des semaines — non."
    ),
    # 60 — Matir
    (
        "Défaire délibérément un nœud. Tout nœud interdit à nouer est interdit à défaire. "
        "Si un nœud serré de qualité professionnelle était destiné à être permanent, "
        "il ne peut pas être défait le Chabbat. Un nœud accidentel — jamais voulu — "
        "peut généralement être défait avec indulgence."
    ),
    # 61 — Tofair
    (
        "Joindre deux pièces flexibles séparées en une entité unifiée à l'aide d'un élément de liaison "
        "(fil, colle, agrafes). Utiliser de la colle liquide pour coller des pages ou appliquer du ruban adhésif "
        "pour lier deux surfaces relève de cette catégorie. En urgence vestimentaire, "
        "une épingle de sûreté temporaire utilisée sans cérémonie est permise."
    ),
    # 62 — Koreia
    (
        "Déchirer ou arracher un objet unifié dans l'intention de l'améliorer ou de le préparer "
        "à un usage créateur futur. Déchirer du papier toilette d'un rouleau crée une feuille séparée et utilisable — "
        "c'est pourquoi les foyers observants utilisent des mouchoirs pré-découpés. "
        "Ouvrir un emballage scellé de façon destructive pour accéder à la nourriture (en ruinant l'emballage) "
        "est généralement permis."
    ),
    # 63 — Tzud
    (
        "Restreindre la liberté d'une créature sauvage non domestiquée en la plaçant dans un enclos "
        "d'où elle ne peut pas s'échapper facilement. Cela s'applique aux insectes : couvrir une mouche avec un verre la piège. "
        "Même fermer une porte peut poser problème si un oiseau sauvage s'est introduit. "
        "Exception : si une créature présente un danger réel pour la vie, le pikoua'h nefesh prime."
    ),
    # 64 — Shocheit
    (
        "Prendre la vie de tout être vivant, y compris les insectes. "
        "Le Talmud étend cela à causer une blessure qui fait saigner (havou'ra). "
        "Si le passage du fil dentaire est certain de provoquer un saignement, c'est interdit. "
        "Les cas de danger réel pour la vie lèvent cette interdiction."
    ),
    # 65 — Mafshit
    (
        "Séparer physiquement la peau ou le cuir d'un animal de sa chair. "
        "Éplucher la peau d'un poulet cuit pendant un repas est permis (c'est manger, pas traiter le cuir) ; "
        "séparer délibérément les couches d'un article en cuir — comme décoller des couches qui se délaminent "
        "d'une ceinture ou d'une semelle — relève de cette mélakha."
    ),
    # 66 — Ma'avir (tanning)
    (
        "Traiter une matière organique brute pour la conserver, la durcir ou empêcher sa décomposition. "
        "Sa principale dérivée est Molei'ah (salage) : saler abondamment des légumes crus comme des concombres "
        "pour les mariner ou les ramollir imite le tannage. Pour l'éviter, ajoutez le sel juste avant de servir, "
        "ou mélangez d'abord les légumes avec de l'huile ou du vinaigre."
    ),
    # 67 — Memacheik
    (
        "Lisser, poncer ou gratter une surface solide pour la rendre parfaitement plane. "
        "Sa dérivée quotidienne Memarei'ah couvre l'étalement de substances épaisses et pâteuses : "
        "frotter un savon solide, étaler une pommade sur une plaie ou lisser du dentifrice sur une brosse. "
        "Utilisez du savon liquide, une lotion liquide, et tamponnez (sans étaler) toute pommade nécessaire."
    ),
    # 68 — Mechatech (marking)
    (
        "Marquer, graver ou tracer délibérément des lignes guides sur une surface pour écrire ou couper. "
        "Tracer une ligne préalable sur un gâteau avant de le couper, ou presser une bordure décorative "
        "dans une pâte pour marquer les découpes, est restreint. Couper directement sans ligne guide préalable convient."
    ),
    # 69 — Mechatech (cutting to measure)
    (
        "Couper, déchirer ou tailler un objet à une dimension précise et mesurée dans un but productif. "
        "Déchirer du papier toilette sur les lignes perforées est une violation directe (coupe à une limite mesurée). "
        "Séparer des yaourts multipacks le long d'une couture perforée relève également de cette catégorie."
    ),
    # 70 — Koseiv
    (
        "Former un symbole ou une lettre significatifs sur une surface. "
        "Pour violer l'interdiction toranique, il faut au moins deux lettres ou symboles distincts "
        "écrits avec un moyen permanent sur une surface durable. "
        "Les Sages étendent cela à l'écriture temporaire et à la formation de lettres. "
        "Jouer au Scrabble avec des tuiles qui s'enclenchent dans un cadre fait débat parmi certains décisionnaires."
    ),
    # 71 — Mocheik
    (
        "Effacer, gratter, dissoudre ou oblitérer un symbole ou une lettre significatifs. "
        "Selon la loi toranique, cela s'applique lorsque l'effacement permet une réécriture. "
        "Les Sages interdisent aussi l'effacement destructif. "
        "Concernant la déchirure à travers un texte imprimé sur un emballage alimentaire : "
        "les ashkénazes préfèrent l'éviter dès le départ ; les séfarades le permettent généralement pour accéder à la nourriture."
    ),
    # 72 — Boneh
    (
        "Créer, améliorer ou assembler une structure en combinant des parties en une unité stable. "
        "Installer une grande tonnelle ou une tente pliante crée un ohel (abri) et peut être interdit. "
        "Assembler solidement des meubles modulaires est également restreint. "
        "Les questions d'assemblage mineur dépendent des détails — consultez votre rabbin."
    ),
    # 73 — Soter
    (
        "Démonter ou défaire délibérément une structure stable. "
        "Démonter une grande tonnelle d'extérieur ou démonter une tente à plusieurs panneaux peut violer Soter. "
        "Dévisser complètement une poignée de porte ou séparer de force des meubles modulaires solidement verrouillés "
        "relève de cette restriction."
    ),
    # 74 — Mechabeh
    (
        "Éteindre, atténuer ou amortir une flamme ou une étincelle. "
        "Souffler une bougie, verser de l'eau sur une mèche, éteindre une cuisinière à gaz, "
        "ou même baisser un brûleur allumé pour laisser mijoter est interdit (atténuer est une forme d'extinction partielle). "
        "Les lumières et les bougies doivent être préparées avant le Chabbat pour brûler naturellement "
        "ou s'éteindre via un minuteur."
    ),
    # 75 — Ma'avir (kindling)
    (
        "Créer, étendre ou alimenter une flamme ou une source de chaleur intense. "
        "Frotter une allumette, ajouter du bois au feu ou compléter un circuit électrique "
        "pour allumer une lumière ou un appareil est restreint. "
        "C'est pourquoi les foyers observants utilisent des minuteurs pour les lumières, "
        "préparent la mijoteuse avant le vendredi et s'appuient sur une chaleur préexistante."
    ),
    # 76 — Makeh B'patish
    (
        "Toute action finale qui transforme un objet incomplet ou cassé en objet pleinement fonctionnel. "
        "Couper un fil desserré d'une chemise fraîchement retouchée, remettre une fermeture éclair détachée sur sa glissière, "
        "ou retirer un clou saillant pour rendre une surface sûre — ce sont tous des actes d'achèvement de la création. "
        "Reportez toutes réparations et assemblages après la Havdalah."
    ),
    # 77 — Hotza'ah
    (
        "Déplacer un objet d'un domaine privé vers un domaine public, ou l'inverse, "
        "ou porter un objet sur quatre coudées (~1,8 m) dans un domaine public. "
        "La plupart des rues modernes sont classées rabbiniquement comme zone semi-publique (karmelit), "
        "où le transport reste restreint. Les communautés établissent un érouv — une enceinte symbolique — "
        "pour permettre le transport dans une zone partagée. Sans érouv, une clé de maison ne peut être portée dehors "
        "que si elle est intégrée structurellement par un spécialiste dans un vêtement ou un bijou fonctionnel "
        "(comme une boucle de ceinture ou une épingle décorative). "
        "Suspendre simplement une clé lâche à un cordon autour du cou est une violation directe du transport."
    ),
    # 78 — Comparison intro
    (
        "La liste ci-dessous ne couvre que les différences clairement et directement énoncées dans les sources classiques. "
        "La loi du Yom Tov comporte de nombreuses nuances — consultez toujours votre rabbin pour des situations précises."
    ),
    # 79 — 39 melachot intro
    (
        "Les 39 catégories de travail créateur (méla'hot) interdites le Chabbat sont dérivées des types de travail "
        "effectués lors de la construction du Michkan (Tabernacle) dans le désert. "
        "Le Talmud les énumère dans le traité Chabbat 73a.\n\n"
        "Chaque mélakha a des applications pratiques dans la vie moderne, de la cuisine et l'écriture "
        "au transport et à l'électricité. Appuyez ci-dessous pour parcourir les 39 méla'hot."
    ),
    # 80 — Browse melachot
    "Les 39 catégories de travail créateur interdites le Chabbat. Appuyez sur l'une d'elles pour lire une explication complète.",
    # 81 — Browse holidays
    "Appuyez sur une fête pour en savoir plus — sa signification, ses observances et ses coutumes.",
    # 82 — Cooking & baking (table header)
    "Cuisson et pâtisserie",
    # 83 — Forbidden
    "Interdit",
    # 84 — Permitted for day's needs
    "Permis pour les besoins de ce jour",
    # 85 — Reheating (table header)
    "Réchauffer les aliments secs et maintenir au chaud",
    # 86 — Shabbat reheating row
    (
        "Aliments secs entièrement cuits uniquement — réchauffer avec un shinouï (méthode inhabituelle), "
        "par ex. à l'envers sur la plata ou dans un autre récipient. "
        "La nourriture sur la plata depuis avant le Chabbat peut rester."
    ),
    # 87 — Yom Tov cooking row
    "Toute cuisson est permise pour les besoins de ce jour — y compris les liquides (soupes, café, etc.), pas seulement les aliments secs.",
    # 88 — Kindling (header)
    "Allumage d'une flamme",
    # 89 — Shabbat kindling row
    "Uniquement à partir d'une flamme préexistante — pas de création ex nihilo",
    # 90 — Extinguishing (header)
    "Extinction d'une flamme",
    # 91 — Shabbat extinguishing row
    "Interdit — laisser s'éteindre de lui-même",
    # 92 — Carrying (header)
    "Transport entre domaines",
    # 93 — Shabbat carrying row
    "Interdit (sans érouv)",
    # 94 — Yom Tov carrying row
    "Permis pour les besoins du Yom Tov",
    # 95 — Electricity (header)
    "Électricité / conduite / écriture",
    # 96 — Kiddush (header)
    "Kiddouch",
    # 97 — Required
    "Obligatoire",
    # 98 — Candle lighting (header)
    "Allumage des bougies",
    # 99 — Required before sunset
    "Obligatoire avant le coucher du soleil",
    # 100 — Yom Tov candle lighting row
    (
        "Contrairement au Chabbat, ce n'est pas obligatoire avant le début de la journée. "
        "Deuxième nuit en diaspora ou motsei Chabbat : allumer uniquement après la sortie des étoiles, "
        "à partir d'une flamme préexistante — jamais en avance. "
        "Autres nuits : on peut allumer avant le coucher du soleil à partir d'une flamme préexistante selon la coutume."
    ),
]
