"""Human-quality fixes batch 11 — FR medium glossary, mitzvah tips, Tachanun, Chitas."""

from __future__ import annotations

_AMEN_FR = (
    "Amen affirme une bracha que quelqu'un d'autre a récitée — « ainsi soit-il ». "
    "Le Talmud loue celui qui répond Amen même plus que celui qui a fait la bénédiction. "
    "Il ne doit pas être précipité ou marmonné ; c'est une petite mais constante occasion de "
    "mitzva à la synagogue et à la maison. (Littéralement, c'est un acronyme pour « Keil Melech "
    "Ne'eman » — D., Roi fidèle.)"
)

_CHITAS_FR = (
    "Chitas est le cycle d'étude quotidien du Chabad : Chumash (parasha hebdomadaire avec Rachi), "
    "Tehillim (psaumes répartis selon le jour du mois) et Tanya. Beaucoup le terminent après les "
    "prières du matin, mais vous pouvez étudier les portions du jour à tout moment avant le "
    "prochain jour hébraïque. Hayom Yom est une lecture quotidienne courte supplémentaire."
)

BATCH11_FR: dict[str, str] = {
    "Amen affirms a bracha someone else recited, \"so may it be.\" The Talmud praises one who answers Amen even more than the one who made the blessing. It should not be rushed or mumbled; it is a small but constant mitzvah opportunity in synagogue and at home. (Literally, it's an acronym for 'G-d, King Who is faithful' — Keil Melech Ne'eman).": _AMEN_FR,
    "Amen — Amen affirms a bracha someone else recited, \"so may it be.\" The Talmud praises one who answers Amen even more than the one who made the blessing. It should not be rushed or mumbled; it is a small but constant mitzvah opportunity in synagogue and at home. (Literally, it's an acronym for 'G-d, King Who is faithful' — Keil Melech Ne'eman).": f"Amen — {_AMEN_FR}",
    "Chitas (חיט\"ס) is Chabad's daily study cycle: Chumash (weekly Torah portion with Rashi), Tehillim (Psalms divided by day of month), and Tanya. Many complete it after morning prayers, but you may learn today's portions anytime before the next Hebrew day. Hayom Yom is an additional short daily reading.": _CHITAS_FR,
    "Chitas — Chitas (חיט\"ס) is Chabad's daily study cycle: Chumash (weekly Torah portion with Rashi), Tehillim (Psalms divided by day of month), and Tanya. Many complete it after morning prayers, but you may learn today's portions anytime before the next Hebrew day. Hayom Yom is an additional short daily reading.": f"Chitas — {_CHITAS_FR}",
    "Learn about the power of Tehillim (Psalms)! Start with Psalm 1, which is all about the joy of someone who delights in Torah 📖. Did you know? King David composed these prayers during both his highest and lowest moments, and they've been a source of comfort and protection for Jews throughout history. When you read them, you're tapping into an ancient power source of divine connection! ✡": (
        "Découvrez le pouvoir de Tehillim (Psaumes) ! Commencez par le Psaume 1, qui parle de la "
        "joie de celui qui prend plaisir à la Torah 📖. Le saviez-vous ? Le roi David a composé "
        "ces prières dans ses moments les plus hauts et les plus bas, et elles ont été une source "
        "de réconfort et de protection pour les Juifs tout au long de l'histoire. En les lisant, "
        "vous puisez dans une ancienne source de connexion divine ! ✡"
    ),
    "Tachanun (Sephardi): On weekdays after Shacharit Amidah — Vidui, the Thirteen Attributes of Mercy, then Psalm 25 (LeDavid). Sephardic poskim including Rav Ovadia Yosef (Yechaveh Daat 6:7) and the Ben Ish Chai rule against physical nefilat apayim — recite sitting upright. Longer form on Mondays and Thursdays; shorter on other weekdays. Omitted on Rosh Chodesh, festivals, Chanukah, and other days in your siddur (Peninei Halakha, Prayer 03-17-05).": (
        "Tachanun (séfarade) : en semaine après l'Amida de Shaharit — Vidui, les treize attributs "
        "de miséricorde, puis le Psaume 25 (LeDavid). Les poskim séfarades, dont le Rav Ovadia "
        "Yossef (Yechaveh Daat 6:7) et le Ben Ish Haï, s'opposent à la nefilat apayim physique — "
        "récitez assis, droit. Forme plus longue le lundi et le jeudi ; plus courte les autres "
        "jours de semaine. Omis à Roch 'Hodech, aux fêtes, à Hanoucca et aux autres jours indiqués "
        "dans votre sidour (Peninei Halakha, Prière 03-17-05)."
    ),
    "Tachanun (Edot HaMizrach): On weekdays after Shacharit — Vidui and the Thirteen Attributes per Sephardic order (Peninei Halakha, Prayer 03-17-05). Longer on Mondays and Thursdays; shorter on other weekdays. Posture and exact wording vary by kehilla (Iraqi, Syrian, Moroccan, etc.) — follow your siddur. Many communities, following kabbalistic guidance cited by the Ben Ish Chai and Rav Ovadia Yosef (Yechaveh Daat 6:7), refrain from physical nefilat apayim and recite Psalm 25 sitting upright. Omitted on Rosh Chodesh, festivals, Chanukah, and other days in your siddur.": (
        "Tachanun (Edot HaMizrach) : en semaine après Shaharit — Vidui et les treize attributs selon "
        "l'ordre séfarade (Peninei Halakha, Prière 03-17-05). Plus long le lundi et le jeudi ; "
        "plus court les autres jours de semaine. Posture et formulation exacte varient selon la "
        "kehilla (irakienne, syrienne, marocaine, etc.) — suivez votre sidour. De nombreuses "
        "communautés, suivant les conseils kabbalistiques cités par le Ben Ish Haï et le Rav "
        "Ovadia Yossef (Yechaveh Daat 6:7), s'abstiennent de nefilat apayim physique et récitent "
        "le Psaume 25 assis, droit. Omis à Roch 'Hodech, aux fêtes, à Hanoucca et aux autres jours "
        "indiqués dans votre sidour."
    ),
}
