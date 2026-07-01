"""Catalog key for Taanit Bechor explainer template (ErevPesachExplainerTemplates.kt)."""

TAANIT_BECHOR_BASE = (
    "Taanit Bechorot (תַּעֲנִית בְּכוֹרוֹת) — Fast of the Firstborn — is observed on Erev Pesach day "
    "by firstborn males (and some communities include firstborn females — ask your rav).\n\n"
    "Why:\n• Commemorates the plague of the firstborn in Egypt, when Jewish firstborn were spared.\n\n"
    "The fast:\n• When Erev Pesach is Shabbat (14 Nisan), Taanit Bechorot is moved to Thursday "
    "(12 Nisan) per Rama and Peninei Halakha — not Friday or Shabbat. Attend a siyum that day "
    "if that is your minhag.\n• Many firstborns avoid fasting entirely by attending a siyum "
    "(completion of a Talmud tractate or similar Torah work) followed by a seudat mitzvah "
    "(festive meal). The exemption: To be exempt from the fast, you must not only hear the "
    "conclusion of the siyum but also participate in the seudat mitzvah by eating at least a "
    "kezayit (approx. 1 oz) of bread or cake. Leaving after the siyum without eating does not "
    "exempt you — you must still fast.\n• If you fast: the fast runs from dawn (alot hashachar) "
    "until full nightfall (tzeit) — no eating or drinking in between.\n\n"
    "Father of a firstborn son under bar mitzvah:\n• If you are a man with a firstborn son below "
    "bar mitzvah age, the widespread custom is that you fast or attend a siyum yourself on his "
    "behalf — the young child does not observe the fast.\n\n"
    "Plan ahead: locate a community siyum in advance if that is your minhag, or confirm fasting "
    "rules with your rav."
)

TAANIT_BECHOR_TEMPLATE = (
    TAANIT_BECHOR_BASE + "\n$scheduleLeadIn$scheduleBody$scheduleYomTov"
)

TAANIT_CATALOG_KEYS = [TAANIT_BECHOR_TEMPLATE]
