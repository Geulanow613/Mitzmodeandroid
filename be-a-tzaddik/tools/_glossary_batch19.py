"""Human-quality fixes batch 19 — FR letter essays with stripped Hebrew glyphs."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = json.loads((ROOT / "data" / "translation-catalog" / "strings.json").read_text(encoding="utf-8"))[
    "strings"
]

BATCH19_FR: dict[str, str] = {
    next(k for k in CATALOG if k.startswith("Discover the letter Nun (נ)")): (
        "Découvrez la lettre Noun (נ) ! 🌊 La lettre Noun nous enseigne la loyauté et la confiance (Ne'emanut). "
        "Le saviez-vous ? Sa forme courbée (נ) représente un serviteur fidèle, tandis que sa forme finale (ן) "
        "représente un dévouement inébranlable. Le Talmud révèle que Noun a été laissée hors d'Ashrei (Psaume 145) "
        "parce qu'elle représente une « chute » (Nofilah), mais le verset suivant parle du soutien de D. — "
        "nous enseignant que même dans nos chutes, D. nous soutient !"
    ),
    next(k for k in CATALOG if k.startswith("Discover the letter Tav (ת)")): (
        "Découvrez la lettre Tav (ת) ! 🎯 La dernière lettre de l'Alef-Bet représente la vérité (Emet) et la perfection ! "
        "Le saviez-vous ? Le mot Emet (vérité) est composé de la première lettre (א), de la lettre médiane (מ) "
        "et de la dernière lettre (ת) de l'Alef-Bet, enseignant que la vérité doit être cohérente du début à la fin ! "
        "Mission d'aujourd'hui : veillez à ce que vos paroles et vos actions s'accordent avec la vérité."
    ),
    next(k for k in CATALOG if k.startswith("Discover the letter Tet (ט)")): (
        "Découvrez la lettre Tet (ט) ! ✡ Cette lettre représente la bonté — c'est la première lettre qui apparaît "
        "dans la Torah dans le mot « Tov » (bon) ! Le saviez-vous ? Sa forme est courbée vers l'intérieur, "
        "nous enseignant que la vraie bonté est souvent cachée et modeste. Sa valeur numérique est 9, "
        "représentant les neuf mois de grossesse — un temps où la bonté cachée grandit ! "
        "Prenez un moment aujourd'hui pour remarquer les bénédictions cachées dans votre vie."
    ),
    next(k for k in CATALOG if k.startswith("Discover the letter Zayin (ז)")): (
        "Découvrez la lettre Zayin (ז) ! ✡ Cette lettre puissante ressemble à une couronne avec une épée pointée vers le bas ! "
        "Le saviez-vous ? Zayin signifie « arme » en hébreu, nous apprenant que la Torah est notre arme spirituelle "
        "contre la négativité ! Sa valeur numérique est 7, liée au Chabbat, le septième jour. "
        "La forme du Zayin nous enseigne à couronner nos actions physiques d'une intention spirituelle ! "
        "Prenez un moment aujourd'hui pour élever une activité physique avec une sainte intention."
    ),
    next(k for k in CATALOG if k.startswith("Discover the power of Resh (ר)")): (
        "Découvrez la puissance de Resh (ר) ! 👑 Cette lettre nous enseigne le leadership — elle représente « Rosh » (tête). "
        "Le saviez-vous ? Sa forme montre quelqu'un incliné dans l'humilité, enseignant que le vrai leadership "
        "vient du service aux autres. L'ouverture en haut montre que même un leader doit rester ouvert "
        "à apprendre des autres. Mission d'aujourd'hui : pratiquez à la fois le leadership et le suivisme."
    ),
    next(k for k in CATALOG if k.startswith("Discover the righteous Tzadik (צ)")): (
        "Découvrez le juste Tzadik (צ) ! ⚖️ Cette lettre représente le tsaddik (personne juste) dont les actions "
        "sont parfaitement équilibrées. Le saviez-vous ? Sa forme montre un Noun (נ) avec un Yud (י) au-dessus — "
        "enseignant qu'un tsaddik doit combiner l'humilité (Noun courbé) avec la connexion à D. (Yud) ! "
        "La forme finale (ץ) s'étend sous la ligne, montrant comment l'influence d'un tsaddik atteint "
        "même les endroits les plus bas. Défi d'aujourd'hui : équilibrer la gentillesse et les limites "
        "dans vos interactions."
    ),
    next(k for k in CATALOG if k.startswith("Learn about seeing the good in everything")): (
        "Apprenez à voir le bien en tout ! Quand la vie vous envoie une épreuve, essayez de dire "
        "« Gam zu l'tovah » (גם זו לטובה) — ceci aussi est pour le bien ! ✡ Cette phrase puissante "
        "a été rendue célèbre par le sage Nahoum Ish Gam Zu, qui trouvait toujours une lumière cachée. "
        "Même dans les moments difficiles, ayez confiance que D. a un plan parfait. "
        "C'est comme un GPS pour l'âme — parfois l'itinéraire semble étrange, mais il vous mène exactement "
        "où vous devez aller ! 🗺️"
    ),
    next(k for k in CATALOG if k.startswith("Uncover the mysteries of Kaf (כ)")): (
        "Découvrez les mystères de Kaf (כ) ! 👑 Dans le mysticisme juif, Kaf représente Keter (couronne) — "
        "mais pas la couronne de l'orgueil ! Le saviez-vous ? La forme courbée du Kaf (כ) représente "
        "l'inclinaison dans la soumission, tandis que sa valeur numérique (20) représente l'âge où "
        "commence la poursuite de la sagesse. Le Kaf final (ך) enseigne que la vraie réussite vient de l'humilité."
    ),
}
