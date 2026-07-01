"""Catalog keys for Yaaleh V'yavo explainer templates (SeasonalMitzvahText.kt)."""

YAALEH_HALACHA = "Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10"


def forgot_amidah(service: str) -> str:
    return f"""If you forgot:
• Still in Retzei (before concluding the blessing) — insert Yaaleh V'yavo in its place and continue ({YAALEH_HALACHA}).
• After concluding Retzei — return to the beginning of Retzei, insert Yaaleh V'yavo, and complete the remaining blessings ({YAALEH_HALACHA}).
• Finished the entire Amidah (after the final Yihiyu L'ratzon) — repeat only the {service} Amidah (Shemoneh Esrei), never the full service, even if you already davened Musaf, Maariv, or anything else afterward ({YAALEH_HALACHA})."""


FORGOT_MAARIV = f"""If you forgot at Maariv on Rosh Chodesh:
• Still in Retzei before God's name at the conclusion — insert Yaaleh V'yavo there and continue ({YAALEH_HALACHA}).
• Once you finished Retzei, or after the entire Amidah — do not go back and do not repeat. Beit Din sanctified the new month by day, not at night (Berachot 30b; {YAALEH_HALACHA}). Continue davening."""

YAALEH_FORGOT_SHACHARIT = forgot_amidah("Shacharit")
YAALEH_FORGOT_MINCHA = forgot_amidah("Mincha")

YAALEH_SHACHARIT_TEMPLATE = f"""Add Yaaleh V'yavo in the Shacharit Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).

$forgotBlock

Also add Yaaleh V'yavo in bentching if you eat bread today."""

YAALEH_SHACHARIT_FEMALE_TEMPLATE = f"""If you recite the Shacharit Amidah on Rosh Chodesh, add Yaaleh V'yavo in the blessing Retzei (Avodah).

$forgotBlock

If you say Birkat Hamazon when you eat bread today, add Yaaleh V'yavo there too."""

YAALEH_MINCHA_TEMPLATE = f"""Add Yaaleh V'yavo in the Mincha Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).

$forgotBlock"""

YAALEH_MINCHA_FEMALE_TEMPLATE = f"""If you recite the Mincha Amidah on Rosh Chodesh, add Yaaleh V'yavo in the blessing Retzei (Avodah).

$forgotBlock

If you say Birkat Hamazon when you eat bread today, add Yaaleh V'yavo there too."""

YAALEH_MAARIV_TEMPLATE = f"""Add Yaaleh V'yavo in the Maariv Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).

$forgotBlock

Also add Yaaleh V'yavo in bentching if you eat bread tonight."""

YAALEH_MAARIV_FEMALE_TEMPLATE = f"""If you recite the Maariv Amidah on Rosh Chodesh, add Yaaleh V'yavo in the blessing Retzei (Avodah).

$forgotBlock

If you say Birkat Hamazon when you eat bread tonight, add Yaaleh V'yavo there too."""

YAALEH_TEMPLATE_CATALOG_KEYS = [
    YAALEH_FORGOT_SHACHARIT,
    YAALEH_FORGOT_MINCHA,
    FORGOT_MAARIV,
    YAALEH_SHACHARIT_TEMPLATE,
    YAALEH_SHACHARIT_FEMALE_TEMPLATE,
    YAALEH_MINCHA_TEMPLATE,
    YAALEH_MINCHA_FEMALE_TEMPLATE,
    YAALEH_MAARIV_TEMPLATE,
    YAALEH_MAARIV_FEMALE_TEMPLATE,
]

# Full explainer bodies (for deriving template translations).
YAALEH_FULL_EXPLAINERS = {
    "shacharit": f"""Add Yaaleh V'yavo in the Shacharit Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).

{YAALEH_FORGOT_SHACHARIT}

Also add Yaaleh V'yavo in bentching if you eat bread today.""",
    "shacharit_female": f"""If you recite the Shacharit Amidah on Rosh Chodesh, add Yaaleh V'yavo in the blessing Retzei (Avodah).

{YAALEH_FORGOT_SHACHARIT}

If you say Birkat Hamazon when you eat bread today, add Yaaleh V'yavo there too.""",
    "mincha": f"""Add Yaaleh V'yavo in the Mincha Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).

{YAALEH_FORGOT_MINCHA}""",
    "mincha_female": f"""If you recite the Mincha Amidah on Rosh Chodesh, add Yaaleh V'yavo in the blessing Retzei (Avodah).

{YAALEH_FORGOT_MINCHA}

If you say Birkat Hamazon when you eat bread today, add Yaaleh V'yavo there too.""",
    "maariv": f"""Add Yaaleh V'yavo in the Maariv Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).

{FORGOT_MAARIV}

Also add Yaaleh V'yavo in bentching if you eat bread tonight.""",
    "maariv_female": f"""If you recite the Maariv Amidah on Rosh Chodesh, add Yaaleh V'yavo in the blessing Retzei (Avodah).

{FORGOT_MAARIV}

If you say Birkat Hamazon when you eat bread tonight, add Yaaleh V'yavo there too.""",
}
