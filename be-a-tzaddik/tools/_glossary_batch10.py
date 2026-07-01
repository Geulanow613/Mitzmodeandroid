"""Human-quality fixes batch 10 — FR short titles, glued glossary, ES title placeholders."""

from __future__ import annotations

_SHEMA_MITAH_FR = (
    "Shema al HaMitah est le Shema du coucher — déclarer la foi et confier l'âme à D. avant "
    "de dormir. Beaucoup ajoutent le Psaume 91 et d'autres versets. Hamapil se dit en se "
    "couchant réellement. Les femmes sont tenues à cette protection selon la halakha. "
    "Quelques minutes de prière calme terminent bien la journée."
)

BATCH10_FR: dict[str, str] = {
    # Short glossary titles (were literal "… (fr)" placeholders)
    "Al HaNissim": "Al HaNissim",
    "Arba Minim": "Arba Minim",
    "Birkat HaMazon": "Birkat HaMazon",
    "Hoshana Raba": "Hoshana Raba",
    "Matan Torah": "Matan Torah",
    "Olam HaBa": "Olam HaBa",
    "Shalom bayit": "Shalom bayit",
    "Shir HaMaalot": "Shir HaMaalot",
    "Yom HaShoah": "Yom HaShoah",
    "Yom HaZikaron": "Yom HaZikaron",
    "bar mitzvah": "bar-mitsva",
    "bat mitzvah": "bat-mitsva",
    "hakarat hatov": "hakarat hatov",
    "k'fi daato": "k'fi daato",
    "kisui rosh": "kisui rosh",
    "lashon hara": "lashon hara",
    "shmirat halashon": "shmirat halashon",
    "yirat Shamayim": "yirat Shamayim",
    # Glued glossary / educational shorts
    "Afikoman — The Afikoman is matzah eaten at the end of the Seder so no other food follows — remembering the Pesach sacrifice. Children often \"steal\" it for a prize, adding joy. You need a kezayit-sized piece eaten before chatzos halachic (midpoint of the night) per many poskim. It is the last taste of matzah the Seder requires.": (
        "Afikoman — L'afikoman est la matsa mangée à la fin du Seder, sans autre aliment après — "
        "en souvenir du sacrifice de Pessa'h. Les enfants la « volent » souvent pour un prix, "
        "ajoutant de la joie. Il faut un morceau de taille kezayit mangé avant chatzot halakhique "
        "(milieu de la nuit) selon beaucoup de poskim. C'est le dernier goût de matsa que le Seder "
        "exige."
    ),
    "Bedieved describes halachic guidance after the ideal was missed — unintentionally or unavoidably. It is not permission to plan poorly; it is how to recover. Example categories: Amidah repeats, chametz found on Pesach, missed Omer count. Your rav applies bedieved rules to your case; articles in the app summarize common patterns from standard halachic sources.": (
        "Bédieved décrit les conseils halakhiques après que l'idéal a été manqué — involontairement "
        "ou inévitablement. Ce n'est pas la permission de mal planifier ; c'est comment récupérer. "
        "Exemples : Amidah répétée, chametz trouvé à Pessa'h, compte de l'Omer manqué. Votre rav "
        "applique les règles de bédieved à votre cas ; les articles de l'application résument les "
        "modèles courants des sources halakhiques standard."
    ),
    "Chanukah is eight nights lighting menorah for the oil miracle and victory over Greek oppression. Al HaNissim is added in prayer. On Friday, light before Shabbat candles using long candles or extra oil — standard small Chanukah candles burn out before nightfall and invalidate the mitzvah. Gifts are American custom, not core mitzvah. Place menorah for pirsumei nisa. Work is permitted; the focus is light and thanks.": (
        "Hanoucca, ce sont huit nuits d'allumage de la menorah pour le miracle de l'huile et la "
        "victoire sur l'oppression grecque. Al HaNissim s'ajoute dans la prière. Le vendredi, "
        "allumez avant les bougies de Chabbat avec de longues bougies ou de l'huile supplémentaire — "
        "les petites bougies de Hanoucca standard s'éteignent avant la nuit et invalident la mitzva. "
        "Les cadeaux sont une coutume américaine, pas une mitzva centrale. Placez la menorah pour "
        "pirsumei nisa. Le travail est permis ; l'accent est sur la lumière et la gratitude."
    ),
    "Chanukah — Chanukah is eight nights lighting menorah for the oil miracle and victory over Greek oppression. Al HaNissim is added in prayer. On Friday, light before Shabbat candles using long candles or extra oil — standard small Chanukah candles burn out before nightfall and invalidate the mitzvah. Gifts are American custom, not core mitzvah. Place menorah for pirsumei nisa. Work is permitted; the focus is light and thanks.": (
        "Hanoucca — Hanoucca, ce sont huit nuits d'allumage de la menorah pour le miracle de "
        "l'huile et la victoire sur l'oppression grecque. Al HaNissim s'ajoute dans la prière. "
        "Le vendredi, allumez avant les bougies de Chabbat avec de longues bougies ou de l'huile "
        "supplémentaire — les petites bougies de Hanoucca standard s'éteignent avant la nuit et "
        "invalident la mitzva. Les cadeaux sont une coutume américaine, pas une mitzva centrale. "
        "Placez la menorah pour pirsumei nisa. Le travail est permis ; l'accent est sur la lumière "
        "et la gratitude."
    ),
    "Shema al HaMitah is the bedtime Shema — declaring faith and entrusting the soul to G-d before sleep. Many add Psalm 91 and other verses. Hamapil is said when actually lying down to sleep. Women are obligated in this protection per halacha. A few minutes of calm prayer ends the day well.": _SHEMA_MITAH_FR,
    "Shema al HaMitah — Shema al HaMitah is the bedtime Shema — declaring faith and entrusting the soul to G-d before sleep. Many add Psalm 91 and other verses. Hamapil is said when actually lying down to sleep. Women are obligated in this protection per halacha. A few minutes of calm prayer ends the day well.": (
        f"Shema al HaMitah — {_SHEMA_MITAH_FR}"
    ),
    "bedieved — Bedieved describes halachic guidance after the ideal was missed — unintentionally or unavoidably. It is not permission to plan poorly; it is how to recover. Example categories: Amidah repeats, chametz found on Pesach, missed Omer count. Your rav applies bedieved rules to your case; articles in the app summarize common patterns from standard halachic sources.": (
        f"bédieved — Bédieved décrit les conseils halakhiques après que l'idéal a été manqué — "
        "involontairement ou inévitablement. Ce n'est pas la permission de mal planifier ; c'est comment "
        "récupérer. Exemples : Amidah répétée, chametz trouvé à Pessa'h, compte de l'Omer manqué. "
        "Votre rav applique les règles de bédieved à votre cas ; les articles de l'application résument "
        "les modèles courants des sources halakhiques standard."
    ),
}

BATCH10_ES: dict[str, str] = {
    "Kol Nidrei": "Kol Nidré",
    "Pirkei Avot": "Pirkei Avot",
    "Tu B'Av": "Tu BeAv",
    "bein kodesh l'kodesh": "bein kodesh l'kodesh",
    "kisui rosh": "kisui rosh",
}
