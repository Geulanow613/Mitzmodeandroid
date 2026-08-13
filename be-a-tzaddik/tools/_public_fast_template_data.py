"""English catalog keys for PublicFastDay dynamic explainers (must match PublicFastDayText.kt)."""

from __future__ import annotations

COMMON_FAST_LAWS = """Who must fast:
Jewish adults from bar/bat mitzvah age who are healthy enough to fast. Children are trained gradually — ask your rav. Women who are pregnant, nursing, or recently gave birth, and anyone who is ill, should ask a posek — many eat in measured amounts (shiurim) or are fully exempt.

If you ate by mistake:
If you forgot and ate or drank unintentionally, you may continue fasting once you remember — the fast remains valid (Shulchan Arukh O.C. 568:1).

If you cannot fast:
Do not endanger your health. Ask a rav about shiurim (small amounts at intervals), postponing the fast, or exemption. Pikuach nefesh overrides fasting."""

MINOR_FAST_DAY = """Today is $name — $meaning

Minor fast rules:
• No eating or drinking from dawn until nightfall.
• Washing, music, and showering are generally permitted (unlike Tisha B'Av and Yom Kippur).
• Special selichot and prayers may be recited in synagogue — check your community schedule.

$common"""

ESTHER_FAST_DAY = """Today is the Fast of Esther (Taanit Esther) — commemorating the fasting Esther called before approaching the king (Esther 4:16).

Minor fast rules with Purim context:
• No eating or drinking from dawn until nightfall.
• Music, showering for pleasure, and leather shoes are permitted — this is a minor fast, not like Tisha B'Av.
• Purim mitzvot begin tonight/tomorrow per your calendar (14 Adar, or 15 in walled cities). This fast is the spiritual preparation before the joy of Purim.

$common"""

YOM_KIPPUR_FAST_DAY = """Today is Yom Kippur — the Day of Atonement.

The fast:
• Began at sunset last night (many communities accepted it earlier with tosefet — adding time from erev onto Yom Kippur) and ends at nightfall tonight$tzeitSuffix.
• Five afflictions: no eating/drinking, no leather shoes, no bathing for pleasure, no anointing, no marital relations.
• Many wear white; married men who normally wear a kittel do so; tallit is worn all day.
• The day is spent in prayer — Kol Nidrei was last night before sunset (hatarat nedarim cannot be done on Yom Kippur itself), then Shacharit, Musaf, Mincha, Neilah, then Maariv and Havdalah after nightfall.

After nightfall: eat the Motzei Yom Kippur meal (see your checklist). Chabad tradition connects this meal to parnassa for the year.

$common"""

TISHA_FAST_DAY = """Today is Tisha B'Av — mourning the destruction of both Temples and our exile.

The fast:
• Began at sunset last night and ends at nightfall tonight$tzeitSuffix — not a dawn-to-dusk fast.

What is forbidden today (in addition to eating and drinking):
• Leather shoes; bathing for pleasure; anointing with cream or cologne; marital relations.
• Torah study except for mournful passages (Eichah, Iyov, parts of Yirmiyahu, halachot of mourning).
• Greeting people with "hello" during the fast day — even after chatzos, do not greet or reply to greetings until nightfall when the fast ends.
• Idle conversation; work is discouraged (follow your community).
• Tefillin are not worn in the morning (like the day of burial); tallit and tefillin are worn at Mincha after halachic chatzos.
• Until chatzos (halachic midday): maintain a mournful mindset; sit on the floor or a low stool (no seat higher than about 12 inches / 30 cm); kinot (elegies) are recited at Shacharit.
• Hand washing: only to remove ritual impurity — wash only to the knuckle where fingers join the hand; or to remove actual dirt on the hand.
• Do not brush teeth with water; a dry toothbrush is permitted. Flossing is permitted.
• Do not fly on Tisha B'Av, even to Israel to make aliyah.

What is permitted (unlike Yom Kippur in some communities):
• Music prohibitions are mourning-related — live joyful music is avoided as part of the Three Weeks/Nine Days spirit, but the fast's core prohibitions are the five afflictions above.

After the fast day is over, after nightfall when the fast ends: some mourning restrictions of the Nine Days begin to lift — but Ashkenazi custom continues meat, wine, music, laundry, and bathing for pleasure until chatzos (halachic midday) on 10 Av (not at nightfall of 9 Av itself). When 9 Av was Shabbat and the fast was Sunday, laundry, bathing, and haircuts may be permitted Motzei fast; meat and wine often wait until Monday morning. When 10 Av is Friday, haircuts, laundry, and bathing may be permitted Friday morning for Shabbat prep; meat and wine for Ashkenazim often until Friday chatzos. Ask your rav before resuming.

$common"""

EREV_MINOR_FAST = """Tomorrow is $fastName — a public fast from dawn (alot hashachar) until nightfall (tzeit).

If you plan to eat before the fast begins:
• Set a mental condition (tanai) the night before: "If I wake up hungry before dawn, I will eat." Without this condition, waking early and eating may prohibit you from eating again until the fast officially begins at dawn (Shulchan Arukh O.C. 564:1).
• If you wake before dawn and want to eat, you may drink water and eat foods that are not normally cooked for a meal — e.g. a piece of cake, fruit, or cereal. A full hot meal is disputed; many avoid a formal cooked meal once they have decided to fast (Mishnah Berurah 564:8–9).
• Stop all eating and drinking at alot hashachar$alotLine.

Practical prep tonight:
• Hydrate well and eat a balanced dinner.
• Plan a lighter morning if you will not eat before dawn.
• Know your synagogue schedule if you plan to attend special prayers.

Who must fast: Jewish adults (bar/bat mitzvah age and older) in good health. Children below bar/bat mitzvah are not required to fast — train them gradually per your rav.$fridayNote"""

EREV_YOM_KIPPUR = """Today is Erev Yom Kippur — a unique day of eating before the holiest fast of the year.

Mitzvah to eat today:
• It is a mitzvah to eat and drink generously all day Erev Yom Kippur (Shulchan Arukh O.C. 604:1; Mishnah Berurah).
• Have a festive Yom Tov-style meal — many begin with candle lighting and Kiddush as for Yom Tov (ask your rav for your minhag).
• The Talmud compares eating on Erev Yom Kippur to fasting on Yom Kippur itself for merit (Rosh Hashana 9a).

Before the fast begins:
• Halachic fast time: Yom Kippur runs from sunset tonight$sunsetLine until nightfall tomorrow ($tzeitTomorrow).
• Tosefet (adding from the weekday): Many communities begin abstaining from food and drink somewhat before sunset — accepting the fast early, as we "add from the weekday onto the holy day" (Yoma 81b; Shulchan Arukh O.C. 608:1). How many minutes early varies by community; ask your rav. The sunset time in this app is when the fast is definitely in effect, not necessarily when your synagogue begins Kol Nidrei.
• Kol Nidrei before sunset: Kol Nidrei is the annulment of vows (hatarat nedarim) before the Day of Atonement. A beit din cannot convene on Yom Kippur itself, so Kol Nidrei must be completed before sunset while it is still weekday (Rosh Hashana 9b; Shulchan Arukh O.C. 619:1). That is why services often start well before sunset — so the congregation can finish Kol Nidrei and begin the fast with tosefet while it is still erev.
• Ask forgiveness from those you may have hurt; give tzedakah; immerse in a mikveh if that is your custom.
• Light Yom Kippur candles before sunset (married women traditionally light; others follow community — ask your rav).

Motzei Yom Kippur meal:
• After the fast ends tomorrow night, it is a mitzvah to eat a proper meal — not only a snack. Many have a festive break-fast.
• Chabad tradition (Baal HaTanya) teaches that one's livelihood (parnassa) for the year is especially connected to this post-Yom Kippur meal — eat with joy and intention after davening and Havdalah.

Who fasts tomorrow: Healthy Jewish adults from bar/bat mitzvah age. Those who are ill, pregnant, nursing, or recently gave birth should ask a rav — often they eat in smaller amounts (shiurim) or are exempt."""

EREV_TISHA_BEAV = """Erev Tisha B'Av prepares us for mourning the destruction of the Temple.

When the fast begins:
• Tisha B'Av starts at sunset tonight$sunsetLine and continues until nightfall tomorrow night — not dawn to dusk like minor fasts.

Restrictions from chatzos (halachic midday) today:
• From chatzos onward: do not eat meat or drink wine (some extend other restrictions — follow your minhag).
• Reduce pleasurable activities; avoid laundry, haircuts, and swimming per mourning customs.

Seudah hamafseket — the final meal before the fast:
• Eat a simple meal alone, sitting on the floor or a low stool, with only one cooked dish (e.g. a hard-boiled egg dipped in ashes, or bread with cold water).
• Do not eat two cooked dishes together; do not recline; do not greet others with "hello."
• Finish before sunset; afterward, only water is permitted until the fast begins.
• Many recite Birkat HaMazon and then change into non-leather shoes before sunset.

After sunset tonight until nightfall tomorrow:
• Five afflictions apply (like Yom Kippur): no eating/drinking, no leather shoes, no bathing for pleasure, no anointing, no marital relations.
• Additionally: no Torah study except sad topics (Eichah, Job, mourning laws); many sit on the floor until chatzos tomorrow; kinot are recited.$shabbatNote

Ask your rav for details if you are ill, pregnant, or nursing."""

MOTZEI_YOM_KIPPUR_MEAL = """After Yom Kippur ends at nightfall$tzeitLine, it is a mitzvah to eat a proper meal — not only a quick bite.

What to do:
• Complete Maariv and Havdalah (Havdalah includes a candle that was lit throughout Yom Kippur, wine, and no spices).
• Eat a festive break-fast — many prepare food in advance because cooking restrictions on Yom Kippur end at nightfall.
• Chabad tradition (Baal HaTanya) teaches that one's livelihood (parnassa) for the year is especially connected to this meal — eat with joy and spiritual intention.

This item becomes available at nightfall after the fast ends."""

PREP_TITLE = "Prepare for tomorrow's fast — $fastName"
FAST_DAY_TITLE = "Fast — $name"

ALOT_LINE_FALLBACK = " — enable location in Settings for your dawn time"

APPROX_TIME_SUFFIX = " (approx. $time)"
ALOT_LINE_WITH_TIME = " (approx. $time — enable location for your exact zman)"
TZET_TOMORROW_TIME = "approx. $time tomorrow night"
TZET_TOMORROW_FALLBACK = "nightfall tomorrow night"
TZET_LINE = " (approx. $time tonight)"
FAST_DAY_SUBTITLE_TEMPLATE = "{name} · {timing}{endSuffix}"
FAST_TIMING_SUNSET = "from sunset until nightfall the following night"
FAST_TIMING_DAWN = "from dawn (alot hashachar) until nightfall (tzeit)"

FRIDAY_NOTE_10_TEVET = """

10 Tevet on Friday: This is the only public fast that is never postponed. If it falls on Friday, you still fast until nightfall, then break the fast with Shabbat Kiddush and your Friday night meal (Shulchan Arukh O.C. 249:4; Mishnah Berurah)."""

FRIDAY_NOTE_ESTHER = """

Fast of Esther on Friday: Many communities still fast until shortly before Shabbat; break in time for Shabbat preparations — ask your rav for local practice."""

SHABBAT_TISHA_NOTE = """

Shabbat Erev Tisha B'Av (when 9 Av is Shabbat and the fast is moved to Sunday):
• Shabbat is celebrated fully until sunset — no mourning restrictions on Shabbat itself.
• After Shabbat ends (Havdalah), change into non-leather shoes and begin the fast and mourning practices.
• The seudah hamafseket is not eaten on Shabbat — it is observed after Shabbat ends, before the fast begins."""

PUBLIC_FAST_CATALOG_KEYS: list[str] = [
    COMMON_FAST_LAWS,
    MINOR_FAST_DAY,
    ESTHER_FAST_DAY,
    YOM_KIPPUR_FAST_DAY,
    TISHA_FAST_DAY,
    EREV_MINOR_FAST,
    EREV_YOM_KIPPUR,
    EREV_TISHA_BEAV,
    MOTZEI_YOM_KIPPUR_MEAL,
    PREP_TITLE,
    FAST_DAY_TITLE,
    ALOT_LINE_FALLBACK,
    APPROX_TIME_SUFFIX,
    ALOT_LINE_WITH_TIME,
    TZET_TOMORROW_TIME,
    TZET_TOMORROW_FALLBACK,
    TZET_LINE,
    FAST_DAY_SUBTITLE_TEMPLATE,
    FAST_TIMING_SUNSET,
    FAST_TIMING_DAWN,
    FRIDAY_NOTE_10_TEVET,
    FRIDAY_NOTE_ESTHER,
    SHABBAT_TISHA_NOTE,
]
