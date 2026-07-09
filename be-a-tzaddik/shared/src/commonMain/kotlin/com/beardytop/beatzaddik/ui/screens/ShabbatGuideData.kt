package com.beardytop.beatzaddik.ui.screens

import com.beardytop.beatzaddik.domain.HalachicTerm

data class GuideTopic(
    val id: String,
    val title: String,
    val hebrewTitle: String = "",
    val body: String,
    val learnMoreUrl: String? = null
)

object ShabbatGuideData {

    // ── Key terms ───────────────────────────────────────────────────────────

    val glossary = listOf(
        GuideTopic(
            id = "shabbat_overview",
            title = "What is Shabbat?",
            hebrewTitle = "שַׁבָּת",
            body = """Shabbat (the Sabbath) begins at sunset on Friday and ends when three stars are visible on Saturday night. It commemorates G-d resting on the seventh day of Creation and the Exodus from Egypt.

The day is marked by refraining from the 39 categories of creative labor (melachot), as well as by positive observances: candle lighting before sunset, Kiddush over wine, festive meals, prayer, Torah study, and Havdalah at the close of the day.

Shabbat is called a "sign" between G-d and the Jewish people.""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/4687/jewish/Shabbat.htm"
        ),
        GuideTopic(
            id = "candle_lighting",
            title = "Shabbat Candle Lighting",
            hebrewTitle = "הַדְלָקַת נֵרוֹת",
            // Source: Peninei Halakha ph.yhb.org.il/en/01-04-04, OU Torah outorah.org/p/35448
            body = """Candles are lit before sunset on Friday to usher in Shabbat and bring peace and warmth into the home. This is performed by the woman of the household (or a man if no woman is present).

There are two customs regarding the blessing:
• Ashkenazi custom: Light the candles first, then cover the eyes, recite the blessing, and uncover. This is done because reciting the blessing is considered accepting Shabbat — after which lighting would be forbidden. Covering the eyes allows the blessing to precede benefiting from the light.
• Sephardi custom: Recite the blessing first, then light the candles.

Every woman should follow the custom of her family. When in doubt, ask your rabbi.

Timing:
Candles are lit before Shabbat begins (before sunset). The amount of time before sunset varies by community:
• Most Ashkenazi communities outside Israel: 18 minutes before sunset
• Jerusalem: 40 minutes before sunset
• Many other communities: 20–22 minutes, or other local customs
Always check your local community Shabbat calendar for the exact time in your area.

Note: There is a strict earliest boundary for this mitzvah. You cannot light candles prior to Plag HaMincha (approximately 1.25 halachic hours before sunset). Lighting before this time invalidates the mitzvah and makes the blessing unnecessary.""",
            learnMoreUrl = "https://halachipedia.com/index.php?title=Shabbat_Candles"
        ),
        GuideTopic(
            id = "kiddush",
            title = "Kiddush",
            hebrewTitle = "קִידּוּשׁ",
            body = """Kiddush is the Torah-level commandment to "remember the Shabbat day to sanctify it" (Exodus 20:8), established by the Sages through a declaration over a cup of wine.

Key rules:
• Kiddush must be recited in the same place where you will eat your meal (Kiddush b'Makom Seudah).
• The cup must hold at least a revi'it (רְבִיעִית — approximately 86ml / ~3 fl oz). The minimum amount to actually drink is maleh lugmov (מְלֹא לֻגְמָיו — a cheekful, approximately 60ml / ~2 fl oz). Ideally, drink the majority of the cup. Exact measurements are debated; consult your rabbi.
• Women are equally obligated in Kiddush.
• Grape juice is a fully valid substitute for wine.
• On Friday night, if wine is unavailable, challah (bread) may substitute.
• On Shabbat morning, if wine is unavailable, beer or whiskey (chamar medinah — a locally significant beverage) may be used. Wine is always preferred. Consult your rabbi for specifics.
• Unlike Friday night, bread can never substitute for Kiddush on Shabbat morning — saying Hamotzi without wine or chamar medina means you have skipped daytime Kiddush entirely.""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/610626/jewish/Kiddush.htm"
        ),
        GuideTopic(
            id = "havdalah",
            title = "Havdalah",
            hebrewTitle = "הַבְדָּלָה",
            body = """Havdalah (separation) is the ceremony performed after Shabbat ends — Saturday night when three stars appear — marking the transition back to the weekdays.

The ceremony has four blessings: wine, fragrant spices (besamim), fire, and the Havdalah blessing (Hamavdil). The spices comfort the soul for the departure of the extra Shabbat soul (neshama yeteira).

Important: you may not perform any melachah (Shabbat-forbidden labor) after Shabbat ends until you have heard Havdalah or said the phrase "Baruch HaMavdil bein Kodesh l'chol" (Blessed is He Who separates the holy from the mundane).""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/4198870/jewish/Havdalah.htm"
        ),
        GuideTopic(
            id = "hallel",
            title = "Hallel",
            hebrewTitle = "הַלֵּל",
            // Source: chabad.org/library/article_cdo/aid/4685156 (Shulchan Aruch ch. 490)
            //         chabad.org/library/article_cdo/aid/4909907 (12 Rosh Chodesh Facts)
            // Source: Peninei Halakha ph.yhb.org.il/en/12-02-07
            //         chabad.org/library/article_cdo/aid/4685156 (Shulchan Aruch ch. 490)
            //         chabad.org/library/article_cdo/aid/4909907 (12 Rosh Chodesh Facts)
            body = """Hallel (praise) is a selection of Psalms 113–118 recited as praise and thanksgiving to G-d on Yom Tov (festivals), Rosh Chodesh (the new month), and Chanukah.

Full Hallel is recited on Shavuot, all seven days of Sukkot (including Chol HaMoed Sukkot), Shemini Atzeret, Simchat Torah, and all eight days of Chanukah.

Pesach — Full vs. Half Hallel:
• Outside Israel (Diaspora): Full Hallel on the first two nights/days of Pesach (Yom Tov); Half Hallel from Chol HaMoed onward through the last days.
• In Israel: Full Hallel only on the first day of Pesach; Half Hallel from the second day onward.
The reason for Half Hallel from Chol HaMoed: we do not fully celebrate while the Egyptians drowned in the sea.

Half Hallel (certain paragraphs omitted) is also recited on Rosh Chodesh."""
        ),
        GuideTopic(
            id = "yaaleh_vyavo",
            title = "Yaaleh V'Yavo",
            hebrewTitle = "יַעֲלֶה וְיָבֹא",
            // Source: chabad.org/library/article_cdo/aid/4685156 (Shulchan Aruch ch. 490, sec. 3)
            //         chabad.org/library/article_cdo/aid/4909907 (12 Rosh Chodesh Facts)
            body = """Yaaleh V'Yavo is a special prayer paragraph inserted into the Amidah (the standing silent prayer) and into Birkat HaMazon (Grace After Meals) on Rosh Chodesh (the new month), Yom Tov (festivals), and Chol HaMoed (intermediate festival days).

The text asks G-d to remember us, our fathers, Jerusalem, the Davidic dynasty, and the entire Jewish people for good — for life and peace — on the day being observed.

If forgotten in Shacharit or Mincha (also Chol HaMoed / Yom Tov at any Amidah including Maariv):
• Still in Retzei — insert Yaaleh V'yavo in its place and continue.
• After concluding Retzei — return to the beginning of Retzei, insert Yaaleh V'yavo, and complete the remaining blessings.
• After the final Yihiyu L'ratzon — repeat only that Amidah (Shemoneh Esrei), never the full service.

Rosh Chodesh Maariv only: if forgotten after finishing Retzei, or after the entire Amidah — do not repeat (Berachot 30b; Shulchan Arukh O.C. 422:1). If still in Retzei before God's name at the conclusion, insert it there."""
        ),
        GuideTopic(
            id = "rosh_chodesh",
            title = "Rosh Chodesh",
            hebrewTitle = "רֹאשׁ חֹדֶשׁ",
            // Source: chabad.org/library/article_cdo/aid/4909907 (12 Rosh Chodesh Facts)
            body = """Rosh Chodesh (the New Month) is the first day of each Hebrew month, and in some months the 30th day of the previous month as well.

Special observances include: Yaaleh V'Yavo in prayers and Grace After Meals, half Hallel, a Musaf (additional) prayer, and a special Torah reading.

Rosh Chodesh is a semi-holiday — certain fasting and eulogizing are restricted. There is a widespread custom for women to refrain from certain types of work as an extra mark of honor for the day."""
        ),
        GuideTopic(
            id = "yom_tov",
            title = "Yom Tov — Jewish Festivals",
            hebrewTitle = "יוֹם טוֹב",
            // Source: chabad.org/library/article_cdo/aid/708510/jewish/Laws-of-Yom-Tov.htm
            body = """Yom Tov (literally "good day") refers to the major Jewish festivals: Pesach, Shavuot, Rosh Hashana, Sukkot, and Shemini Atzeret/Simchat Torah. Yom Kippur has its own distinct laws closer to Shabbat.

Yom Tov shares most of Shabbat's restrictions, but three key differences are permitted: cooking and baking for that day's needs, kindling a flame from a pre-existing flame (not striking a new one), and carrying between domains for the sake of Yom Tov needs.

All other creative labors forbidden on Shabbat remain forbidden on Yom Tov. The many details of Yom Tov law are complex and vary by situation. Always consult your rabbi."""
        ),
        GuideTopic(
            id = "muktzeh",
            title = "Muktzeh",
            hebrewTitle = "מֻקְצֶה",
            body = """Muktzeh refers to items that are "set aside" and may not be moved on Shabbat. Unlike the 39 melachot which come from the Mishkan's construction, Muktzeh is a Rabbinic enactment to protect the spirit of Shabbat rest.

The main principle: any object whose primary purpose is forbidden on Shabbat — like a pen, hammer, or smartphone — may generally be moved only to use the space it occupies, or if you need it for a Shabbat-permitted purpose.

Items that are completely non-functional on Shabbat (like money, rocks, or broken objects that lost utility before Shabbat) generally may not be moved at all. To prepare, store wallets, work tools, and electronics out of reach before Shabbat begins.""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/253229/jewish/Muktzeh.htm"
        ),
        GuideTopic(
            id = "tashlumin",
            title = "Tashlumin — Makeup Prayers",
            hebrewTitle = "תַּשְׁלוּמִין",
            body = """If you miss a prayer service (tefillah), there is a rabbinic mechanism to partially make it up — called tashlumin (תַּשְׁלוּמִין — "completions"). The rules are strict and the window is narrow.

Source: Talmud Bavli, Berachot 26a. Codified in Shulchan Aruch, Orach Chaim 108:1:
"If one erred or was prevented from praying a service, they may make it up by praying twice during the service that immediately follows it."

The Core Rule — Immediate Succession Only:
The makeup can only happen at the very next scheduled prayer. Once that window closes, the opportunity is permanently gone (Mishnah Berurah 108:3 — "Avar zmano, batel korbano" — its time has expired, the obligation is cancelled).

Missed Prayer → When to do Tashlumin:
• Shacharit (morning) → at Mincha. Cannot be made up at Maariv.
• Mincha (afternoon) → at Maariv. Cannot be made up the next morning.
• Maariv (evening) → at the next morning's Shacharit. Cannot be made up at Mincha.

How to do it (order is mandatory — SA OC 108:1):
1. First: pray the Amidah of the current service (the prayer whose time it actually is now).
2. Pause briefly (enough time to walk four cubits).
3. Second: pray the Amidah again as the tashlumin for the missed prayer.

Penalty for reversing the order: If you accidentally say the makeup first intending it as the makeup, halacha automatically applies that first Amidah to your current obligation instead (Mishnah Berurah 108:2). The second Amidah you pray then immediately counts as your valid makeup. You do not need to pray a third time.

If you missed the window entirely — Tefilat Nedavah:
If Shacharit was missed, Mincha was also missed, and it is now Maariv — the formal tashlumin is gone forever. However, you may pray a second voluntary Amidah as a gift to G-d (Tefilat Nedavah — prayer of donation), subject to two conditions (SA OC 107:1):
• Make a mental stipulation: "If I am permitted to pray a voluntary prayer right now, let this be one. If not, let it not count for anything."
• Include a small personal request or textual innovation not in the standard Amidah, to distinguish it as a genuinely spontaneous gift rather than a mistaken obligation.

Additional notes:
• Tashlumin applies only when the prayer was missed unintentionally (forgetfulness or unavoidable circumstances). Deliberate skipping has no makeup.
• If you missed Shacharit on Shabbat, Rosh Chodesh, or Yom Tov, make it up at Mincha — not at Musaf. Musaf is a separate holiday Amidah, not a standard daily boundary prayer. Pray Mincha first, pause, then a second Amidah as tashlumin (SA O.C. 108:9).
• When in doubt about any situation, ask your rabbi.""",
            learnMoreUrl = "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.108"
        )
    )

    // ── Shabbat vs Yom Tov key differences ─────────────────────────────────
    // Only includes points directly stated in the source material (mitzvotcloud.json).
    // For anything beyond this, consult your rabbi.

    data class ComparisonRow(val activity: String, val shabbat: String, val yomTov: String)

    // Source: chabad.org/library/article_cdo/aid/708510/jewish/Laws-of-Yom-Tov.htm
    val shabbatYomTovComparison = listOf(
        ComparisonRow(
            "Cooking & baking",
            "Forbidden",
            "Permitted for the sake of that day's needs"
        ),
        ComparisonRow(
            "Reheating dry food & keeping food warm",
            "Dry, fully cooked food only — reheat with a shinui (unusual method), e.g. upside-down on blech or inside another vessel. Food on blech from before Shabbat may stay.",
            "All cooking is permitted for that day's needs — including liquids (soups, coffee, etc.), not only dry food."
        ),
        ComparisonRow(
            "Kindling a flame",
            "Forbidden",
            "May kindle from a pre-existing flame only — not from scratch"
        ),
        ComparisonRow(
            "Extinguishing a flame",
            "Forbidden",
            "Forbidden — let it burn out on its own"
        ),
        ComparisonRow(
            "Carrying between domains",
            "Forbidden (without eruv)",
            "Permitted for the sake of Yom Tov needs"
        ),
        ComparisonRow(
            "Electricity / driving / writing",
            "Forbidden",
            "Forbidden"
        ),
        ComparisonRow(
            "Kiddush",
            "Required",
            "Required"
        ),
        ComparisonRow(
            "Candle lighting",
            "Required before sunset",
            "Unlike Shabbat, not required before the day begins. Second Diaspora night or Motzei Shabbat: light after nightfall only, from a pre-existing flame — never early. Other nights: may light before sunset from pre-existing flame per custom"
        )
    )

    // ── 39 Melachot ────────────────────────────────────────────────────────
    // Content based on mitzvotcloud.json entries cloud113–cloud151 + cloud153

    val melachot = listOf(
        GuideTopic("zorea", "Zorea — Planting", "זוֹרֵעַ",
            "Any action that promotes or sustains plant growth is forbidden: planting seeds, grafting branches, or watering your garden. Because watering actively fosters growth, even indoor houseplants may not be watered on Shabbat, no matter how dry the soil looks.",
            "https://www.chabad.org/library/article_cdo/aid/4739388/jewish/Zoraya-Planting.htm"),
        GuideTopic("choresh", "Choresh — Plowing", "חוֹרֵשׁ",
            "Loosening, leveling, or improving the earth in any way — plowing, digging, aerating — is forbidden. The prohibition extends to dragging heavy objects: dragging a massive iron or wooden lawn bench across soft dirt can easily carve a deep furrow and must be avoided. Standard baby strollers and wheelchairs, however, are fully permitted to be pushed across dirt or grass, as normal wheel tracking is not considered an act of plowing.",
            "https://www.chabad.org/library/article_cdo/aid/4739406/jewish/Choresh-Plowing.htm"),
        GuideTopic("kotzer", "Kotzer — Reaping", "קוֹצֵר",
            "Picking fruit, plucking a flower, or detaching anything from its source of growth is forbidden. This includes herbs from a kitchen windowsill pot. The Sages further forbade making direct use of a tree on Shabbat — climbing, leaning, or hanging hammocks is out.",
            "https://www.chabad.org/library/article_cdo/aid/4740235/jewish/Kotzeir-Reaping.htm"),
        GuideTopic("meameir", "Me'ameir — Gathering", "מְעַמֵּר",
            "Taking scattered items that grew from the ground and collecting them into a single pile or unit. If apples fall from your tree, you may not gather them into a basket. However, the prohibition of gathering applies strictly to the place where the items naturally grow (like a field or orchard). Gathering items that spill on a kitchen floor does not violate Me'ameir under any circumstances.",
            "https://www.chabad.org/library/article_cdo/aid/4740248/jewish/Meamer-Gathering.htm"),
        GuideTopic("dosh", "Dosh — Threshing", "דָּשׁ",
            "Separating a food product from its outer shell or casing. This applies today primarily to squeezing: squeezing grapes or olives for juice is a direct Torah-level violation. The Sages extended this to other fruits — freshly squeezing oranges or grapefruits is forbidden.",
            "https://www.chabad.org/library/article_cdo/aid/4786919/jewish/Dosh-Threshing.htm"),
        GuideTopic("zoreh", "Zoreh — Winnowing", "זוֹרֶה",
            "Separating food from non-food using the wind or your breath. Shaking a tablecloth outdoors when the wind will scatter the crumbs, or blowing dust off a book cover, may fall under this prohibition according to the Jerusalem Talmud's broader understanding.",
            "https://www.chabad.org/library/article_cdo/aid/4788763/jewish/Zoreh-Winnowing.htm"),
        GuideTopic("borer", "Borer — Selecting", "בּוֹרֵר",
            "Separating a mixture by removing the unwanted from the wanted. For selection to be permitted, three conditions must all be met simultaneously: take the good from the bad (not bad from good), by hand or normal eating utensil (not a sorting tool like a strainer), and for immediate use only — not to prepare food for later. These rules apply to food and non-food alike (clothes, toys, etc.).",
            "https://www.chabad.org/library/article_cdo/aid/4788764/jewish/Borer-Selecting.htm"),
        GuideTopic("tochen", "Tochen — Grinding", "טוֹחֵן",
            "Reducing something that grew from the ground to small particles. Dicing vegetables into very fine pieces (like for Israeli salad) may violate this — deliberately cut larger pieces than usual. Crushing dried mud off your shoes is also forbidden. 'There is no grinding after grinding' — crushing already-baked items like matzah is generally permitted.",
            "https://www.chabad.org/library/article_cdo/aid/4740282/jewish/Tochen-Grinding.htm"),
        GuideTopic("meraked", "Meraked — Sifting", "מְרַקֵּד",
            "Using a dedicated utensil (sifter, strainer, French press filter) to separate food or liquids from unwanted mixtures. If the liquid is so full of sediment that no one would drink it unfiltered, straining is a Torah-level violation. Using a strainer on a drinkable liquid that just has pulp may be more lenient, but best to avoid dedicated separating utensils entirely.",
            "https://www.chabad.org/library/article_cdo/aid/4817714/jewish/Meraked-Sifting.htm"),
        GuideTopic("losh", "Losh — Kneading", "לָשׁ",
            "Combining a powder or small solid particles with a liquid to form a single cohesive paste or thick mass. This applies to baby cereal, instant oatmeal, mustard, and certain textures of egg salad. When mixing is unavoidable, workarounds exist: reverse the order of ingredients and use an unusual stirring motion (shinui). This is why Shabbat challah is baked before Shabbat.",
            "https://www.chabad.org/library/article_cdo/aid/4819033/jewish/Losh-Kneading.htm"),
        GuideTopic("bishul", "Bishul — Cooking", "בִּישּׁוּל",
            "Using fire-generated heat to improve any item — cooking, baking, frying, or boiling. The threshold is 'yad soledet bo' (hot enough to pull your hand away). 'There is no cooking after cooking' — re-warming dry, solid foods already fully cooked before Shabbat is generally permitted. Reheating cold liquids is forbidden. These laws are complex — consult your rabbi.",
            "https://halachipedia.com/index.php?title=Bishul"),
        GuideTopic("gozez", "Gozez — Shearing", "גּוֹזֵז",
            "Detaching any natural growth from the body of a living creature. This includes cutting hair, trimming fingernails, shaving, plucking, or pulling loose feathers from a pillow. Even biting fingernails or pulling loose skin is restricted. Preparing these beforehand is itself a mitzvah honoring Shabbat.",
            "https://www.chabad.org/library/article_cdo/aid/4844108/jewish/Gozez-Shearing.htm"),
        GuideTopic("melaben", "Melaben — Laundering", "מְלַבֵּן",
            "Whitening, cleaning, or refreshing fabric. This goes well beyond the washing machine — even spot-cleaning a fresh spill by dabbing with water or rubbing a stain with cleaning intent can violate Melaben. Even wetting a dirty garment may be considered the beginning of the laundering process.",
            "https://www.chabad.org/library/article_cdo/aid/4853580/jewish/Melaben-Bleaching-Laundering.htm"),
        GuideTopic("menapetz", "Menapetz — Combing Fibers", "מְנַפֵּץ",
            "Aggressively brushing or combing raw, tangled material to separate and align fibers. For hair, this means a hard-bristled brush that forcefully separates and tears strands falls under this or Gozez. On Shabbat, use a soft brush or wide-toothed comb gently on dry hair.",
            "https://www.chabad.org/library/article_cdo/aid/4861029/jewish/Menapetz-Combing.htm"),
        GuideTopic("tzoveya", "Tzoveya — Dyeing", "צוֹבֵעַ",
            "Permanently coloring or enhancing the appearance of a material using pigment. This affects cosmetics: using makeup that stains the skin is a concern on Shabbat. The deeper principle is not permanently altering the appearance of your surroundings to match your desires on the day of rest.",
            "https://www.chabad.org/library/article_cdo/aid/4904572/jewish/Tzoveya-Dyeing.htm"),
        GuideTopic("toveh", "Toveh — Spinning", "טוֹוֶה",
            "Taking loose, disorganized fibers and drawing them out while twisting them into a continuous strand. Any action that creates thread, yarn, or cord from raw material is prohibited. Twisting multiple existing threads together to create a thicker rope or twine is also included.",
            "https://www.chabad.org/library/article_cdo/aid/4917849/jewish/Toveh-Spinning.htm"),
        GuideTopic("maisach", "Maisach — Warping a Loom", "מֵסַךְ",
            "Organizing and anchoring the vertical base threads on a loom, preparing its infrastructure for weaving. This teaches that Shabbat rest requires cessation not just of final creative acts but even the behind-the-scenes planning and structuring of creative work.",
            "https://halachipedia.com/index.php?title=Maisach"),
        GuideTopic("oseh_batei_nirin", "Oseh Batei Nirin — Setting Heddles", "עוֹשֶׂה שְׁתֵּי בָּתֵּי נִירִין",
            "Assembling the guiding loops and harnesses of a loom that allow the weaver to separate warp threads for weaving. Like Maisach, this is purely preparatory — it creates the infrastructure for future creation, which Shabbat requires us to cease.",
            "https://halachipedia.com/index.php?title=Oseh_Shtei_Batei_Nirin"),
        GuideTopic("oreg", "Oreg — Weaving", "אוֹרֵג",
            "Interlacing two sets of threads at right angles to form fabric. The Talmud states that weaving even two parallel horizontal threads into a vertical grid is sufficient to violate this prohibition. Certain types of dense embroidery, cross-stitching, and basketry share the same structural mechanics.",
            "https://halachipedia.com/index.php?title=Oraig"),
        GuideTopic("pofzea", "Potze'a — Unraveling", "פּוֹצֵעַ",
            "Deliberately unraveling or separating woven or braided material with the goal of re-using or re-weaving the strands. Pulling a loose thread from a sweater that unravels a row of stitches is a practical example. Shabbat forbids both constructing and deconstructing the material world.",
            "https://www.halachipedia.com/index.php?title=Potzeya"),
        GuideTopic("koshair", "Koshair — Tying", "קוֹשֵׁר",
            "Creating a permanent, professional-grade knot intended to remain long-term. A knot that is both professionally crafted and permanent is forbidden by the Torah. A simple temporary knot meant to be undone within 24 hours is permitted. A standard bow on a sneaker is fine; a tight double-knot meant to stay tied for weeks is not.",
            "https://www.chabad.org/library/article_cdo/aid/4937160/jewish/Koshair-Tying.htm"),
        GuideTopic("matir", "Matir — Untying", "מַתִּיר",
            "Deliberately untying a knot. Any knot that is forbidden to tie is forbidden to untie. If a tight professional knot was intended to be permanent, it cannot be untied on Shabbat. An accidental knot — one never intended to be tied — may generally be untied leniently.",
            "https://www.chabad.org/library/article_cdo/aid/4943184/jewish/Matir-Untying.htm"),
        GuideTopic("tofair", "Tofair — Sewing", "תּוֹפֵר",
            "Joining two separate pieces of flexible material into a unified entity using a connecting element (thread, glue, staples). Using liquid glue to paste pages together or applying duct tape to bind two surfaces falls under this category. In a fashion emergency, a temporary safety pin used casually is permitted.",
            "https://www.halachipedia.com/index.php?title=Tofer"),
        GuideTopic("koreah", "Koreah — Tearing", "קוֹרֵעַ",
            "Tearing or ripping any unified object with the intent to improve it or prepare it for future creative use. Tearing toilet paper from a roll creates a separate, usable sheet — this is why observant homes use pre-cut tissues. Opening sealed packaging destructively to access food (ruining the packaging) is generally permitted.",
            "https://www.halachipedia.com/index.php?title=Koreah"),
        GuideTopic("tzad", "Tzad — Trapping", "צָד",
            "Restricting the freedom of a wild, un-domesticated creature by placing it in an enclosure from which it cannot easily escape. This applies to insects: covering a fly with a cup traps it. Even closing a door can be an issue if a wild bird wandered in. Exception: if a creature poses a genuine danger to life, Pikuach Nefesh overrides.",
            "https://www.chabad.org/library/article_cdo/aid/5009500/jewish/Tzod-Trapping.htm"),
        GuideTopic("shochait", "Shochait — Slaughtering", "שׁוֹחֵט",
            "Taking the life of any living creature, including insects. The Talmud extends this to causing a wound that draws blood (Chavurah). If flossing is certain to cause bleeding, it is forbidden. Cases of genuine danger to life override this prohibition.",
            "https://www.chabad.org/library/article_cdo/aid/5028250/jewish/Shochait-Slaughtering.htm"),
        GuideTopic("mafshit", "Mafshit — Skinning", "מַפְשִׁיט",
            "Physically separating the skin or hide of any animal from its meat. While peeling the skin off a cooked chicken during a meal is permitted (it's eating, not processing leather), deliberately splitting apart layers of a leather item — like peeling delaminating layers of a leather belt or shoe sole — falls under this melacha.",
            "https://www.halachipedia.com/index.php?title=Mafshit"),
        GuideTopic("meabeid", "Me'abeid — Tanning", "מְעַבֵּד",
            "Processing raw organic material to preserve, toughen, or prevent it from decaying. Its key derivative is Molei'ach (salting): heavily salting raw vegetables like cucumbers to marinate or soften them mimics tanning. To avoid this, add salt right before serving, or mix vegetables with oil or vinegar first.",
            "https://www.chabad.org/library/article_cdo/aid/5106334/jewish/Meabeid-Tanning-Hides.htm"),
        GuideTopic("memachek", "Memachek — Smoothing", "מְמַחֵק",
            "Smoothing, sanding, or scraping a solid surface to make it perfectly even. Its daily-life derivative Memarei'ach covers spreading thick pasty substances: rubbing a bar of solid soap, spreading ointment over a wound, or smoothing toothpaste on a brush. Use liquid soap, liquid lotion, and dab (don't spread) any necessary ointment.",
            "https://www.chabad.org/library/article_cdo/aid/5128235/jewish/Memachek-Smoothing.htm"),
        GuideTopic("mesarteit", "Mesarteit — Scoring Lines", "מְשַׂרְטֵט",
            "Intentionally marking, etching, or scratching guide lines on a surface for writing or cutting. Drawing a preliminary guide line across a cake before cutting, or pressing a decorative border into dough to mark cuts, is restricted. Slicing directly through without a prior guide line is fine.",
            "https://www.halachipedia.com/index.php?title=Mesartait"),
        GuideTopic("mechateich", "Mechateich — Cutting to Size", "מְחַתֵּךְ",
            "Cutting, tearing, or trimming any object to a specific, measured dimension for a productive purpose. Tearing toilet paper on perforated lines is a direct violation (cutting at a measured boundary). Snapping apart multi-pack yogurt containers along a perforated seam also falls here.",
            "https://www.chabad.org/library/article_cdo/aid/5139146/jewish/Mechateich-Cutting.htm"),
        GuideTopic("koteiv", "Koteiv — Writing", "כּוֹתֵב",
            "Forming any meaningful symbol or letter on a surface. To violate the Torah prohibition, at least two distinct letters or symbols must be written with a permanent medium on a lasting surface. The Sages extend this to temporary writing and shaping letters. Playing Scrabble with tiles that lock into a frame is questioned by some authorities.",
            "https://www.chabad.org/library/article_cdo/aid/5152314/jewish/Koteiv-Writing.htm"),
        GuideTopic("mochek", "Mocheik — Erasing", "מוֹחֵק",
            "Wiping, scraping, dissolving, or obliterating any meaningful symbol or letter. Under Torah law this applies when erasing enables rewriting. The Sages also forbid destructive erasing. Regarding tearing through printed text on food packaging: Ashkenazim prefer to avoid it from the outset; Sephardim generally permit it when accessing food.",
            "https://www.chabad.org/library/article_cdo/aid/5154283/jewish/Mochek-Erasing.htm"),
        GuideTopic("boneh", "Boneh — Building", "בּוֹנֶה",
            "Creating, improving, or assembling any structure by combining parts into a stable unit. Setting up a large pop-up canopy or tent creates an Ohel (covering) and may be forbidden. Snapping together modular furniture tightly is also restricted. Minor assembly questions depend on specifics — ask your rabbi.",
            "https://www.chabad.org/library/article_cdo/aid/5163573/jewish/Boneh-Building.htm"),
        GuideTopic("soter", "Soter — Demolishing", "סוֹתֵר",
            "Intentionally dismantling or breaking down any stable structure. Dismantling a large outdoor shade canopy or taking down a multi-panel tent can violate Soter. Unscrewing a door handle completely or forcefully separating tightly locked modular furniture falls under this restriction.",
            "https://www.chabad.org/library/article_cdo/aid/5344896/jewish/Sotair-Demolishing.htm"),
        GuideTopic("mechabeh", "Mechabeh — Extinguishing", "מְכַבֶּה",
            "Putting out, dimming, or dampening any flame or spark. Blowing out a candle, pouring water on a wick, turning off a gas stove, or even turning down a live burner to simmer is forbidden (dimming is a form of partial extinguishing). Lights and candles must be set up before Shabbat to burn naturally or turn off via timer.",
            "https://www.chabad.org/library/article_cdo/aid/5580653/jewish/Mechabeh-Extinguishing.htm"),
        GuideTopic("mavir", "Mav'ir — Kindling", "מַבְעִיר",
            "Creating, expanding, or fueling any flame or intense heat source. Striking a match, adding wood to a fire, or completing an electrical circuit to turn on a light or appliance is restricted. This is why observant homes use timers for lights, set slow-cookers before Friday, and rely on pre-existing warmth.",
            "https://www.chabad.org/library/article_cdo/aid/6897613/jewish/Mavir-Kindling-a-Fire.htm"),
        GuideTopic("makeh_b_patish", "Makeh B'patish — Final Blow", "מַכֶּה בַּפַּטִּישׁ",
            "Any final action that takes an incomplete or broken object and renders it fully functional. Snipping a loose thread from a freshly tailored shirt, snapping a detached zipper back onto its track, or removing a protruding nail to make a surface safe — all are finishing acts of creation. Leave all repairs and assembly until after Havdalah.",
            "https://www.chabad.org/library/article_cdo/aid/5358316/jewish/Makeh-Bepatish-Completing.htm"),
        GuideTopic("hotza_ah", "Hotza'ah — Carrying", "הוֹצָאָה",
            "Moving an object from a private domain to a public domain, or vice versa, or carrying an object four cubits (~6 feet) within a public domain. Most modern streets are rabbinically classified as a semi-public area (karmelit), where carrying is still restricted. Communities establish an eruv — a symbolic enclosure — to permit carrying within a shared area. Without an eruv, a house key can only be worn outdoors if it is structurally integrated by a specialist into a functional piece of clothing or jewelry (such as a belt clasp or decorative pin). Simply hanging a loose key on a string around your neck is a direct violation of carrying.",
            "https://www.chabad.org/library/article_cdo/aid/6897566/jewish/Hotzaah-Transferring.htm")
    )

    // ── Jewish Holidays ─────────────────────────────────────────────────────
    // Major holidays (Yom Tov + prominent days)
    val majorHolidayIds = setOf(
        "rosh_hashana", "yom_kippur", "sukkot", "shemini_atzeret",
        "chanukah", "purim", "pesach", "shavuot"
    )

    val holidays = listOf(
        // ── Major Yamim Tovim ──────────────────────────────────────────────
        GuideTopic(
            id = "rosh_hashana",
            title = "Rosh Hashana",
            hebrewTitle = "רֹאשׁ הַשָּׁנָה",
            body = """Rosh Hashana (the Jewish New Year) falls on 1–2 Tishrei and is observed for two days in the Diaspora (one day in Israel). It is the anniversary of the creation of Adam and Eve and the Day of Judgment — when G-d reviews the deeds of all people and inscribes their fate for the coming year.

Key observances:
• Hearing the shofar (ram's horn) blown in synagogue — the Ashkenazi custom is approximately 100 blasts per day (200 total over the two days in the Diaspora)
• Special prayers and liturgy including the Unetaneh Tokef
• Eating symbolic foods: apples and honey for a sweet new year, pomegranate, fish head, dates, and other simanim (signs)
• Tashlich — a custom to go to a body of water on the afternoon of the first day of Rosh Hashana and recite verses from Micah (7:18–20) symbolizing casting away sins. The custom involves the prayers at the water, not throwing bread crumbs (many authorities including the Vilna Gaon and Chabad discourage or omit the bread crumb practice entirely)
• Rosh Hashana is a full Yom Tov — all Shabbat-like restrictions apply, with the exception of cooking and carrying""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/4762/jewish/What-Is-Rosh-Hashanah.htm"
        ),
        GuideTopic(
            id = "yom_kippur",
            title = "Yom Kippur",
            hebrewTitle = "יוֹם כִּיפּוּר",
            body = """Yom Kippur (the Day of Atonement) falls on 10 Tishrei. It is the holiest day of the Jewish year — a full fast from sundown to nightfall (approximately 25 hours) and a day of intensive prayer and introspection.

Five forms of affliction (inuyim) are observed: no eating or drinking, no bathing, no applying oils/creams, no leather shoes, no marital relations.

Key prayers: Kol Nidrei (the night before), Yizkor (Ashkenaz custom on Yom Kippur morning — many Sefard communities omit or observe different memorial customs), the Neilah closing prayer, and the final single shofar blast at the conclusion of the fast.

Unlike a regular Yom Tov, Yom Kippur shares many of Shabbat's additional restrictions — including a strict prohibition on carrying outdoors (unless inside a kosher eruv), exactly like Shabbat. Melachah (labor) is fully forbidden.""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/4688/jewish/What-Is-Yom-Kippur.htm"
        ),
        GuideTopic(
            id = "sukkot",
            title = "Sukkot",
            hebrewTitle = "סֻכּוֹת",
            body = """Sukkot begins on 15 Tishrei and lasts seven days (in the Diaspora, the first two days are full Yom Tov, followed by five days of Chol HaMoed). It commemorates the 40 years the Jewish people dwelt in the desert under G-d's protection.

Key observances:
• Dwelling in a sukkah (a temporary outdoor booth with a roof of natural plant material) — at minimum, eating meals there
• Taking the Arba Minim (Four Species): lulav (palm branch), etrog (citron), hadassim (myrtle), aravot (willow)

Arba Minim — by nusach:
• Ashkenaz / most Sefard: waved in six directions (east, south, west, north, up, down) during Hallel; bracha on the first day (and in Chutz LaAretz, also on the second day of Yom Tov)
• Chabad: hold lulav in right hand, etrog in left; wave in the same six directions plus some add an extra motion; follow your Chabad siddur
• Yemenite (Baladi): may bind aravot differently and follow distinct waving customs — ask your community
• Not taken on Shabbat; in Israel, the first day is the primary Torah obligation; in Chutz LaAretz, the first two Yom Tov days

Hallel:
• Full Hallel every day of Sukkot in most communities
• Some Yemenite and other traditions differ on certain days — follow your siddur""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/4784/jewish/What-Is-Sukkot.htm"
        ),
        GuideTopic(
            id = "shemini_atzeret",
            title = "Shemini Atzeret & Simchat Torah",
            hebrewTitle = "שְׁמִינִי עֲצֶרֶת וְשִׂמְחַת תּוֹרָה",
            body = """Shemini Atzeret is an independent Yom Tov immediately following Sukkot (22 Tishrei). In the Diaspora, Simchat Torah is observed on 23 Tishrei. In Israel, both are observed on 22 Tishrei.

Shemini Atzeret:
• Tefillat Geshem (prayer for rain) — inserted in Musaf in most Ashkenaz and Sefard communities; some say it at different points — follow your siddur
• Yizkor — memorial prayers; widely observed in Ashkenaz synagogues on Shemini Atzeret (and on Yom Kippur). Many Sefard communities do not recite Yizkor on this day, or observe different memorial customs

Simchat Torah:
• Celebrates the completion and immediate restart of the annual Torah reading cycle
• Ashkenaz / most Sefard: seven hakafot (processional circuits) with the Torah scrolls, singing and dancing; final portion (V'Zot HaBeracha) read, then Bereishit restarted
• Chabad: hakafot with singing; often one aliyah per scroll taken out
• Yemenite and other communities may have distinct hakafah and reading customs — follow your synagogue""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/4464/jewish/What-Is-Shemini-Atzeret-Simchat-Torah.htm"
        ),
        GuideTopic(
            id = "chanukah",
            title = "Chanukah",
            hebrewTitle = "חֲנֻכָּה",
            body = """Chanukah (the Festival of Lights) is observed for eight nights beginning on 25 Kislev. It commemorates the miracle of the Temple menorah that burned for eight days on only one day's worth of oil, following the Maccabees' victory over the Greeks and rededication of the Temple.

Key observances:
• Lighting the Chanukiah (menorah) each night — one light on the first night, adding one each successive night
• Lights are placed in a visible location to publicize the miracle
• Hallel and Al HaNisim prayer are added to services
• It is customary to eat foods fried in oil (latkes, sufganiyot) and play dreidel

Chanukah is not a Yom Tov — regular weekday activities including work are permitted.""",
            learnMoreUrl = "https://www.chabad.org/holidays/chanukah/default_cdo/jewish/Chanukah.htm"
        ),
        GuideTopic(
            id = "purim",
            title = "Purim",
            hebrewTitle = "פּוּרִים",
            body = """Purim is observed on 14 Adar (15 Adar in walled cities — Shushan Purim). It commemorates the miraculous salvation of the Jewish people in Persia as recorded in Megillat Esther.

Four mitzvot of the day:
• Mikra Megillah — hearing Megillat Esther read aloud twice (night and day)
• Mishloach Manot — sending a food gift of at least two ready-to-eat foods to at least one friend
• Matanot L'evyonim — giving charity to at least two poor people
• Mishteh — having a festive Purim meal

It is also customary to dress in costumes. Purim is not a Yom Tov — work is not formally forbidden, though many abstain.""",
            learnMoreUrl = "https://www.chabad.org/holidays/purim/default_cdo/jewish/Purim.htm"
        ),
        GuideTopic(
            id = "pesach",
            title = "Pesach (Passover)",
            hebrewTitle = "פֶּסַח",
            body = """Pesach begins on 15 Nissan. In the Diaspora it lasts eight days; in Israel seven days. It commemorates the Exodus from Egypt.

Chametz (leavened grain products) must be completely removed from one's home before Pesach. There is a Torah-level prohibition on owning or eating chametz during Pesach. Matzah is eaten in its place.

The Seder is held on the first two nights (in the Diaspora). The Haggadah is read, telling the story of the Exodus. Four cups of wine are drunk; matzah, maror (bitter herbs), and other symbolic foods are eaten.

The first two and last two days are full Yom Tov; the middle days (Chol HaMoed) have lighter restrictions. Full Hallel is recited on the first two days of Pesach (Yom Tov); Half Hallel is recited from Chol HaMoed onward and on the final Yom Tov days. In Israel, Half Hallel begins from the second day.""",
            learnMoreUrl = "https://www.chabad.org/holidays/passover/default_cdo/jewish/Passover.htm"
        ),
        GuideTopic(
            id = "shavuot",
            title = "Shavuot",
            hebrewTitle = "שָׁבוּעוֹת",
            body = """Shavuot is observed on 6 Sivan (two days in the Diaspora). It celebrates the giving of the Torah at Mount Sinai, which took place seven weeks after the Exodus.

It is customary to:
• Study Torah through the night (Tikkun Leil Shavuot)
• Eat dairy foods — various reasons are given, including that the Jewish people had not yet received the Torah's laws of meat preparation
• Read Megillat Ruth, whose themes of Torah commitment echo the acceptance of the Torah
• Hear the Ten Commandments read in synagogue

Shavuot comes exactly 50 days after the second night of Pesach, at the end of the Sefirat HaOmer (Counting of the Omer). It is a full Yom Tov.""",
            learnMoreUrl = "https://www.chabad.org/holidays/shavuot/default_cdo/jewish/Shavuot.htm"
        ),

        // ── Minor Holidays ─────────────────────────────────────────────────
        GuideTopic(
            id = "hoshana_raba",
            title = "Hoshana Raba",
            hebrewTitle = "הוֹשַׁעְנָא רַבָּה",
            body = """Hoshana Raba is the 21st of Tishrei — the seventh and final day of Sukkot. While it is technically Chol HaMoed (an intermediate day), it carries special significance as the final "sealing" of the divine judgment begun on Rosh Hashana.

Synagogue service — customs vary by nusach:
• Ashkenaz: extended service with seven hakafot (circuits) with the lulav and aravot; willow branches (hoshanot) beaten on the ground after the circuits; lulav taken for the last time
• Sefard: similar hoshanot circuits in many communities; willow-beating customs vary — follow your siddur
• Chabad: hakafot with hoshanot; aravot may be beaten five times on the ground (not the lulav itself) — see your Chabad machzor
• Yemenite: distinct hoshanot liturgy and customs

Greeting someone with "a gutten kvittel" (a good inscription) is an Ashkenaz custom, referring to the final sealing of one's decree for the year.""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/757453/jewish/Hoshana-Rabbah.htm"
        ),
        GuideTopic(
            id = "tu_bshvat",
            title = "Tu B'Shvat",
            hebrewTitle = "טוּ בִּשְׁבָט",
            body = """Tu B'Shvat (the 15th day of the Hebrew month of Shvat) is the New Year for Trees (Rosh Hashanah La'Ilanot). It usually falls in late January or early February.

Tu B'Shvat is the holiday of environmental mindfulness and deep-rooted potential. It marks the exact cosmic and agricultural turning point when the deepest winter chill begins to break, and the sap starts rising within the trees to form the fruit of the coming year.

1. The tax day origin (the Mishnah)
Tu B'Shvat does not appear in the Bible. It begins as a legal tax deadline in the Mishnah (Rosh Hashanah 1:1).

In ancient Israel, farmers were obligated to tithe (give a percentage of) their crops to the Priests, the Levites, and the poor. However, biblical law forbids tithing fruit grown in one year to fulfill the tax obligation of a different year. The Rabbis needed a hard cutoff date to calculate a tree's "age" and separate the old crop from the new crop.

They chose the 15th of Shvat because, by mid-winter, most of the winter rains have already fallen in Israel, the soil is saturated, and the trees are officially starting their new growth cycle.

2. The mystical renaissance (the Kabbalistic seder)
For centuries after the destruction of the Temple, Tu B'Shvat was a minor day where fasting was forbidden and people ate a little extra fruit. But in the 16th century, the Kabbalists (mystics) of Safed transformed it into a more spiritual semi-festival.

Led by Rabbi Isaac Luria (the Ari), they created the Tu B'Shvat Seder, modeled loosely on the Passover Seder. They viewed eating fruit not just as a physical act, but as a way to release holy "sparks" trapped in the physical world. The seder revolves around drinking four cups of wine — gradually transitioning from pure white wine to dark red wine to mimic the changing of the seasons — and eating specific fruits categorized by how they are peeled and structured.

3. The Seven Species (Shivat HaMinim)
The central culinary custom of the day is eating fruits native to the Land of Israel, specifically the Seven Species praised in the Book of Deuteronomy (Deuteronomy 8:8):

• Wheat and barley — representing physical nourishment and foundational sustenance.

• Grapes — representing joy and transformation (as wine).

• Figs — representing the sweetness of continuous Torah study (fig trees yield fruit over a long period rather than all at once).

• Pomegranates — representing action and mitzvot (traditionally said to contain 613 seeds, matching the biblical commandments).

• Olives — representing resilience under pressure (olives must be crushed to produce oil) and light.

• Dates (honey) — representing peace, tranquility, and long-term growth.

4. Modern ecology and connection to the Land
With the return of the Jewish people to Israel in the late 19th and 20th centuries, Tu B'Shvat took on a powerful Zionist and ecological meaning. Schoolchildren across Israel traditionally go out to plant trees, a practice that helped reforest a landscape that had been stripped bare over centuries of neglect. Today, it functions globally as a Jewish Earth Day, focusing on environmental conservation, sustainability, and our responsibility to protect the planet.

The psychological lesson: Jewish philosophy frequently compares a human being to a "tree of the field" (Deuteronomy 20:19). Tu B'Shvat teaches that real growth starts completely out of sight. When you look at a tree in mid-Shvat, it looks completely dead, frozen, and bare. Yet the sap is moving hidden underground. The holiday is a reminder that our most profound personal breakthroughs often happen quietly in the dark long before anyone else can see the fruit.

Tu B'Shvat seder (optional)
The Tu B'Shvat Seder is a mystical, multi-sensory feast created by the 16th-century Kabbalists of Safed to celebrate the "New Year for Trees." Modeled loosely after the Passover Seder, this evening ritual transitions participants through four worlds of spiritual consciousness.

The experience revolves around drinking four cups of wine — gradually shifting from pure white to deep red to mirror the changing seasons — and eating at least 15 different fruits and nuts, with a special focus on the Seven Species of Israel. Each fruit is categorized by its physical structure (such as those with hard shells, those with internal pits, or those eaten whole), serving as a tangible meditation on human character, environmental stewardship, and the hidden potential waiting to bloom within us all.

If you want the text of a Tu B'Shvat seder, [here](https://aish.com/48965616/) is a good one (Aish.com).

Liturgical note: Tachanun is omitted at Shacharit and Mincha on Tu B'Shvat, and at Mincha on the preceding afternoon (14 Shevat). Work is permitted; it is not Yom Tov.""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/468738/jewish/Tu-BiShvat-What-and-How.htm"
        ),
        GuideTopic(
            id = "pesach_sheni",
            title = "Pesach Sheni",
            hebrewTitle = "פֶּסַח שֵׁנִי",
            body = """Pesach Sheni (the "Second Passover") falls on the 14th day of the Hebrew month of Iyar — exactly one month after the eve of Passover (Pesach).

While it is not a traditional rest day (Yom Tov) where work is forbidden, it holds a deeply unique, empowering status in Jewish tradition. If Tu B'Av is the holiday of transformation, Pesach Sheni is the ultimate holiday of the second chance. It stands as the only biblical commandment that offers a built-in makeup date for those who missed it the first time around.

The origin story: the power of protesting
The origin of Pesach Sheni, recorded in the Book of Numbers (Numbers 9:1–14), is one of the few instances in the Torah where a major law was initiated not from the top down by God or Moses, but from the bottom up by regular people.

One year after the Exodus from Egypt, God commanded the Israelites to bring the Passover offering on the 14th of Nisan. However, a specific group of men had become ritually impure (tamei) because they had been carrying a human corpse (traditionally identified as the coffin of Joseph, or the remains of Nadav and Avihu).

Because of their impurity, they were forbidden from entering the Tabernacle area to bring the sacrifice. Instead of accepting their exclusion, they approached Moses and Aaron with a raw, passionate protest: "Why should we be diminished by not being allowed to bring God's offering at its appointed time among the children of Israel?"

Moses did not have an immediate answer. He told them to wait while he consulted God. God responded by validating their plea completely, establishing a permanent decree: anyone who was ritually impure, or away on a "distant journey" during the first Passover, could bring the offering exactly one month later.

How it differed from the first Passover
Pesach Sheni was a streamlined version of the holiday, offering a fascinating legal compromise between the strict rules of the original Passover and the practicalities of daily life:

• No chametz ban — Unlike the main Passover festival, where owning or seeing leavened bread (chametz) is strictly forbidden for seven days, on Pesach Sheni you could have chametz and matzah in your house at the exact same time.

• The menu — The individual celebrating would simply eat the meat of the late sacrifice along with matzah and bitter herbs (maror) on the evening of the 14th of Iyar.

• Duration — It lasted only one evening, rather than a full seven-day festival.

Modern observance and themes
Because the Temple in Jerusalem is no longer standing, the animal sacrifice cannot be brought today. However, the day is still actively marked in Jewish practice and thought:

• Eating matzah — It is a widespread custom to eat a piece of matzah on Pesach Sheni (often during lunch or dinner) to conceptually connect with the essence of the day.

• Dropping Tachanun — The daily prayers of penitence and mourning (Tachanun) are omitted, signaling that the day retains a festive, joyful status.

• The spiritual lesson — In Hasidic and contemporary Jewish philosophy, Pesach Sheni has become a powerful psychological symbol. It carries the foundational message that "it is never too late." No matter how spiritually distant a person feels, or how many mistakes or roadblocks ("impurities") have gotten in their way, there is always a mechanism to make amends and move forward.

Liturgical note: Tachanun is omitted. It is not Yom Tov and work is permitted.""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/4377624/jewish/What-Is-Pesach-Sheni-the-Second-Passover.htm"
        ),
        GuideTopic(
            id = "lag_baomer",
            title = "Lag BaOmer",
            hebrewTitle = "לַ\"ג בָּעֹמֶר",
            body = """Lag BaOmer falls on the 18th day of the Hebrew month of Iyar. The name itself is a shorthand for its date: the Hebrew letters Lamed (ל) and Gimmel (ג) have a combined numerical value of 33, meaning the holiday takes place on the 33rd day of the Omer — the 49-day countdown connecting Passover to Shavuot.

While Passover and Shavuot are highly structured biblical festivals, Lag BaOmer is a fiery, mystical, grassroots celebration. It functions as a sudden, 24-hour pause button on a multi-week period of semi-mourning.

The two historic pillars of the day
Lag BaOmer is anchored by two entirely different historical events that occurred centuries apart, combining a devastating tragedy with profound spiritual revelation.

• The pause of the plague (Rabbi Akiva's students) — The weeks counting the Omer are traditionally a solemn time. The Talmud records that during the 2nd century CE, a catastrophic plague swept through the 24,000 students of the great sage Rabbi Akiva, killing them all. The sages teach that this tragedy occurred because "they did not show proper respect to one another." On the 33rd day of the count, the plague miraculously stopped. Because the dying ceased, Lag BaOmer became a celebration of survival and a reminder of the vital importance of radical interpersonal respect.

• The passing of Rabbi Shimon bar Yochai — The second pillar centers on Rabbi Akiva's greatest surviving student: Rabbi Shimon bar Yochai (known as the Rashbi), who went on to become the premier master of Jewish mysticism and the traditional author of the Zohar (the foundational text of Kabbalah). Rabbi Shimon passed away on the 33rd day of the Omer. Before his death, he explicitly commanded his students to celebrate his passing not with weeping, but as a day of joy (Yom Hillula), because his death marked the moment his soul achieved perfect unity with God and his hidden spiritual teachings were fully revealed to the world.

Why the fire? Traditions and observance
Lag BaOmer is famous for its distinct, high-energy customs, each deeply tied to its historical themes:

• Bonfires — Massive fires are lit all over Israel and Jewish communities worldwide. The fire serves a dual purpose: it symbolizes the immense "spiritual light" that Rabbi Shimon brought into the world through the Zohar, and it recalls the light of a miraculous fire that columned over his bed on the day he passed.

• Lifting the mourning restrictions — Because the plague paused, all the traditional mourning practices observed during the Omer are suspended for the day. People blast music, dance around the bonfires, and schedule weddings that were delayed for weeks. (Whether your community ends all Omer mourning at Lag BaOmer or only on Shavuot varies by minhag — follow your community.)

• The first haircut (upsherin) — It is a widespread tradition to hold off on cutting a boy's hair until he turns three years old. On Lag BaOmer, thousands of families travel to Meron (the burial site of Rabbi Shimon in northern Israel) to give their three-year-old boys their very first haircuts, a ceremony called an upsherin.

• Bows and arrows — Children traditionally play with toy bows and arrows on this day. This recalls the midrashic tradition that no rainbow appeared in the sky during Rabbi Shimon's entire lifetime; his personal spiritual merit was so immense that the world never required a physical reminder of God's covenant not to destroy the earth.

Liturgical note: Tachanun is omitted. Work is permitted; it is not Yom Tov.""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/679300/jewish/What-Is-Lag-BaOmer.htm"
        ),
        GuideTopic(
            id = "tu_beav",
            title = "Tu B'Av",
            hebrewTitle = "טוּ בְּאָב",
            body = """Tu B'Av (the 15th day of the Hebrew month of Av) holds a unique spot in Jewish tradition. The Mishnah (Taanit 4:8) famously states: "There were no greater days of joy for Israel than the 15th of Av and Yom Kippur."

Because it sits precisely six days after Tisha B'Av (the darkest day of mourning on the calendar), Tu B'Av represents a dramatic transformation from grief to joy, celebrated under the light of a mid-summer full moon.

The Talmud (Taanit 30b–31a) lists six distinct historical and agricultural reasons why this day became a major festival:

• Matchmaking in the vineyards — The most famous reason, and why the day is celebrated as a "Day of Love" in modern Israel. In ancient times, the unmarried daughters of Jerusalem would dress in white garments and go out to dance in the vineyards. To ensure no one was humiliated due to poverty, all the dresses were borrowed — a wealthy girl would borrow from a poor girl, leveling the social playing field so suitors would focus on character and family rather than wealth.

• Intermarriage between the tribes was permitted — When the Israelites first settled the Land of Israel, women who inherited land (such as the daughters of Zelophehad) were restricted to marrying only within their own tribe to prevent ancestral land from shifting boundaries. On a later Tu B'Av, this restriction was lifted, allowing the twelve tribes to fully intermarry and fostering national unity.

• The rescue of the tribe of Benjamin — Following a devastating civil war described in the Book of Judges (the incident of the Concubine of Gibeah), the other tribes had sworn never to let their daughters marry anyone from the surviving remnant of Benjamin. On Tu B'Av, this ban was lifted, allowing Benjamin to re-integrate into the nation and avoid extinction.

• The "desert generation" ceased to die — After the Sin of the Spies, God decreed that the generation that left Egypt would wander for 40 years and die out before entering the Promised Land. Every year on Tisha B'Av, a portion of that generation passed away. In the 40th year, the remaining people lay down expecting the decree to claim them, but woke up alive. They waited until the full moon of Tu B'Av to ensure they had not miscalculated the calendar; when they saw the full moon, they realized the decree of death had finally ended.

• The blockade to Jerusalem was removed — When King Jeroboam split the kingdom, he set up armed guards and roadblocks to prevent citizens from making holiday pilgrimages to the Holy Temple in Jerusalem. Centuries later, King Hoshea ben Elah removed these blockades on the 15th of Av, restoring spiritual access and religious unity to the entire nation.

• The breaking of the hatchets — The fires on the Temple altar required an immense amount of wood, which had to be perfectly dry and free of worms. By the 15th of Av, the summer sun began to lose its intense heat, meaning freshly cut wood would no longer dry quickly enough to guarantee it was worm-free. On this day they celebrated the successful completion of harvesting wood for the altar, and the axes were symbolically broken and put away for the year — a transition toward a season focused more intensely on Torah study during the longer autumn nights.

Bonus — The burial of the martyrs of Betar: A later reason relates to the Bar Kochba revolt against the Romans. When the fortress city of Betar fell, the Romans slaughtered thousands of Jews and refused to let the bodies be buried. Miraculously, the bodies did not decompose, and on Tu B'Av permission was finally granted to give the martyrs a proper Jewish burial.

Liturgical note: Tachanun is omitted. Work is permitted; it is not a fast day. Many communities treat it as an auspicious day for marriage and joy.""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/717167/jewish/7-Joyous-Events-That-Happened-on-the-15th-of-Av.htm"
        ),
        GuideTopic(
            id = "birkat_hachamah",
            title = "Birkat Hachamah — Blessing the Sun",
            hebrewTitle = "בִּרְכַּת הַחַמָּה",
            body = """Birkat Hachamah (בִּרְכַּת הַחַמָּה — Blessing of the Sun) is recited once every 28 years, when the sun completes the machzor gadol (מחזור גדול) — the great 28-year solar cycle described in the Talmud (Berakhot 59b).

The cycle:
Jewish tradition calculates the solar year as 365¼ days (Samuel; Eruvin 56a). Each year the vernal equinox shifts forward six hours; after 28 years it returns to the same day of the week and time — Tuesday at nightfall, when the sun was created (Bereishit 1:14–19). Because the blessing requires seeing the sun, recite it on Wednesday morning at sunrise.

When:
• Recite outdoors at sunrise on the morning of the occurrence, while viewing the sun (not through a window, per most poskim).
• Ideal time: from sunrise through the third halachic hour of the day (sof zman tefillah). According to some opinions, you may still recite it until chatzos (halachic midday).

The blessing:
Baruch Atah Ado-nai Eloheinu Melech ha'olam oseh ma'aseh bereishit.
(בָּרוּךְ אַתָּה ה' אֱלֹקֵינוּ מֶלֶךְ הָעוֹלָם עוֹשֵׂה מַעֲשֵׂה בְרֵאשִׁית)

Notes:
• This mitzvah comes only once every 28 years.
• Check your local synagogue or Chabad center — large communal gatherings are common.""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/3731/jewish/Birkat-Hachama.htm"
        ),
        GuideTopic(
            id = "tisha_beav",
            title = "Tisha B'Av",
            hebrewTitle = "תִּשְׁעָה בְּאָב",
            body = """Tisha B'Av (the 9th of Av) is the saddest day in the Jewish calendar. Both the First and Second Temples were destroyed on this date, along with numerous other tragedies throughout Jewish history.

It is a full 25-hour fast (sundown to nightfall), with the same five afflictions as Yom Kippur: no eating, drinking, bathing, anointing, leather shoes, or marital relations.

Melachah (work) is permitted but mourning customs apply: no Torah study (except on sad topics like Lamentations and Job), no greeting others, no music. Megillat Eichah (the Book of Lamentations) is read at night; Kinot (dirges) are recited in the morning.

The three weeks from 17 Tammuz until Tisha B'Av are called the Three Weeks — a period of mourning. Mourning intensifies during the Nine Days leading up to Tisha B'Av; customs differ by nusach (Ashkenaz, Sephard, Chabad, and others).""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/144575/jewish/What-Is-Tisha-BAv.htm"
        ),
        GuideTopic(
            id = "fast_gedaliah",
            title = "Fast of Gedaliah",
            hebrewTitle = "צוֹם גְּדַלְיָה",
            body = """The Fast of Gedaliah is observed on 3 Tishrei (pushed to 4 Tishrei when 3 Tishrei falls on Shabbat). It commemorates the assassination of Gedaliah ben Ahikam, the Jewish governor appointed by Babylonia to administer the land after the destruction of the First Temple.

His death marked the end of the last vestiges of Jewish autonomy in the Land of Israel following the first exile.

It is a minor fast — from dawn until nightfall (not a full 25-hour fast like Yom Kippur or Tisha B'Av).""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/2316462/jewish/Tzom-Gedaliah-Fast-Day.htm"
        ),
        GuideTopic(
            id = "fast_10_tevet",
            title = "Fast of 10 Tevet",
            hebrewTitle = "עֲשָׂרָה בְּטֵבֵת",
            body = """The Fast of 10 Tevet commemorates the day Nebuchadnezzar, king of Babylon, began the siege of Jerusalem — the event that ultimately led to the destruction of the First Temple.

It is a minor fast from dawn until nightfall. It is also observed as a general Kaddish day for Holocaust victims whose date of death is unknown.

Note: This is the only fast in the calendar that is never postponed if it falls on a Friday (because the biblical verse requires observing it "on this very day"). When it lands on a Friday, continue fasting directly into Friday night — welcome Shabbat while fasting — and break the fast only after nightfall following the recitation of Friday night Kiddush.""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/3170662/jewish/What-Is-Asarah-BTevet-Tevet-10.htm"
        ),
        GuideTopic(
            id = "fast_17_tammuz",
            title = "Fast of 17 Tammuz",
            hebrewTitle = "שִׁבְעָה עָשָׂר בְּתַמּוּז",
            body = """The Fast of 17 Tammuz (Shiva Asar B'Tammuz) marks the day the walls of Jerusalem were breached by the Romans in the first century CE. It begins the "Three Weeks" — a period of mourning culminating in Tisha B'Av.

It is a minor fast from dawn until nightfall. During the Three Weeks, weddings are not held and music is generally avoided. During the final nine days of this period (1–9 Av), additional mourning customs apply.""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/479885/jewish/The-17th-of-Tammuz.htm"
        ),
        GuideTopic(
            id = "fast_esther",
            title = "Fast of Esther",
            hebrewTitle = "תַּעֲנִית אֶסְתֵּר",
            body = """The Fast of Esther (Ta'anit Esther) is observed on 13 Adar, the day before Purim. It commemorates the three-day fast of Esther and the Jewish people before she approached King Achashverosh to plead on behalf of her people.

It is a minor fast from dawn until nightfall. If 13 Adar falls on Shabbat, the fast is moved to 11 Adar. Machatzit HaShekel (a half-shekel donation to charity) is given on Purim morning, sometimes before Megillah reading on the night before.""",
            learnMoreUrl = "https://www.chabad.org/holidays/purim/article_cdo/aid/1473/jewish/Fast-of-Esther.htm"
        ),
        GuideTopic(
            id = "sefirat_haomer",
            title = "Sefirat HaOmer",
            hebrewTitle = "סְפִירַת הָעֹמֶר",
            body = """Sefirat HaOmer is the mitzvah to count the 49 days from the second night of Pesach until Shavuot — linking the Exodus to receiving the Torah.

When to count:
• Count each night after tzeit (nightfall), before dawn.
• Many Ashkenazim count after Maariv; many Sephardim and Chabad also count after Maariv. Follow your community.

The blessing:
• Before counting on the first night, recite the blessing "…al sefirat ha'omer."
• Then say the count for that night (e.g. "Today is one day of the Omer").

If you forgot at night:
• Count the next day during the daytime without a bracha.
• If you do this before sunset, you can continue counting on subsequent nights with a bracha.
• You only lose the blessing permanently if you miss an entire 24-hour cycle (both night and the following day) — ask your rav.

The Omer period is also a time of mourning in many communities (no haircuts, weddings, or live music) until Lag BaOmer or Shavuot, depending on custom.""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/4886/jewish/Counting-the-Omer.htm"
        ),
        GuideTopic(
            id = "yom_hashoah",
            title = "Yom HaShoah",
            hebrewTitle = "יוֹם הַשּׁוֹאָה",
            body = """Yom HaShoah (Holocaust Remembrance Day) is observed in Israel on 27 Nisan. It commemorates the six million Jews murdered in the Holocaust and honors survivors.

In Israel, a siren brings the country to a standstill. Many communities hold memorial programs, recite Tehillim, and learn about the Shoah. It is not a Torah-mandated fast or Yom Tov; customs vary by community.""",
        ),
        GuideTopic(
            id = "yom_hazikaron",
            title = "Yom HaZikaron",
            hebrewTitle = "יוֹם הַזִּכָּרוֹן",
            body = """Yom HaZikaron (Memorial Day) falls on 4 Iyar in Israel, immediately before Yom Ha'atzmaut. It honors fallen soldiers and victims of terror.

The day is marked by memorial ceremonies and sirens. Many observe it as a solemn day before the celebration of independence.""",
        ),
        GuideTopic(
            id = "yom_haatzmaut",
            title = "Yom Ha'atzmaut",
            hebrewTitle = "יוֹם הָעַצְמָאוּת",
            body = """Yom Ha'atzmaut (Israeli Independence Day) is observed on 5 Iyar, celebrating the establishment of the State of Israel in 1948.

In Israel and many diaspora communities, it is marked with Hallel (per local custom), festive meals, and programs.""",
        ),
        GuideTopic(
            id = "yom_yerushalayim",
            title = "Yom Yerushalayim",
            hebrewTitle = "יוֹם יְרוּשָׁלַיִם",
            body = """Yom Yerushalayim marks the reunification of Jerusalem in 1967, observed on 28 Iyar.

Many communities recite Hallel and hold festive gatherings. As with Yom Ha'atzmaut, customs and halachic rulings vary — consult your rav.""",
        ),
        GuideTopic(
            id = "chol_hamoed_pesach",
            title = "Chol HaMoed Pesach",
            hebrewTitle = "חול המועד פסח",
            body = """Chol HaMoed Pesach — the intermediate days of Passover — is not full Yom Tov, but it is not ordinary weekdays either.

Key points:
• No chametz; eat kosher-for-Passover food only. Matzah is eaten on many days.
• Half Hallel at Shacharit (not Full Hallel — Ashkenazic custom permits a bracha on Partial Hallel; Sephardic custom often does not).
• Yaaleh V'yavo in Amidah and bentching; Tachanun is omitted.
• Melacha is restricted but less than Yom Tov — work needed to avoid loss may be permitted; ask your rav.
• Simchat moed — nicer meals, family time, Torah learning; avoid treating the day like a regular workweek.
• Shaving and haircuts are generally prohibited on Chol HaMoed unless under specific exceptions — ask your rav.""",
        ),
        GuideTopic(
            id = "chol_hamoed_sukkot",
            title = "Chol HaMoed Sukkot",
            hebrewTitle = "חול המועד סוכות",
            body = """Chol HaMoed Sukkot — the intermediate days of Sukkot — is not full Yom Tov, but it is not ordinary weekdays either.

Key points:
• Full Hallel each day of Sukkot including Chol HaMoed.
• Yaaleh V'yavo in Amidah and bentching; Tachanun is omitted.
• Lulav and etrog each weekday (not on Shabbat); meals in the sukkah when applicable.
• Melacha is restricted but less than Yom Tov — work needed to avoid loss may be permitted; ask your rav.
• Simchat moed — nicer meals, time in the sukkah, Torah learning.
• Shaving and haircuts are generally prohibited on Chol HaMoed unless under specific exceptions — ask your rav.
• Hoshana Raba (21 Tishrei) is the seventh day of Sukkot with special hoshanot customs.""",
        ),
        GuideTopic(
            id = "simchat_torah",
            title = "Simchat Torah",
            hebrewTitle = "שִׂמְחַת תּוֹרָה",
            body = """Simchat Torah celebrates the completion and immediate restart of the annual Torah reading cycle.

In the Diaspora it falls on 23 Tishrei (day after Shemini Atzeret). In Israel it is observed together with Shemini Atzeret on 22 Tishrei.

Customs:
• Hakafot — joyous processional circuits with Torah scrolls, singing and dancing
• Final parsha V'Zot HaBeracha read; Bereishit begun again
• Festive Kiddush and meals; Yizkor in many Ashkenaz communities on Shemini Atzeret/Simchat Torah

It is a full Yom Tov in the Diaspora; in Israel it shares the day with Shemini Atzeret.""",
            learnMoreUrl = "https://www.chabad.org/library/article_cdo/aid/4464/jewish/What-Is-Shemini-Atzeret-Simchat-Torah.htm"
        ),
        GuideTopic(
            id = "purim_katan",
            title = "Purim Katan",
            hebrewTitle = "פורים קטן",
            body = """In a Jewish leap year, 14 Adar I is Purim Katan ("little Purim"). The full Purim mitzvot — Megillah, mishloach manot, matanot l'evyonim, and the festive meal — apply only on regular Purim in Adar II (14 Adar, or 15 Adar in walled cities).

Purim Katan is a minor festive day: Tachanun is omitted. Some communities add a slightly more festive meal or learning; it is not a substitute for Purim observance.""",
        ),
        GuideTopic(
            id = "shushan_purim_katan",
            title = "Shushan Purim Katan",
            hebrewTitle = "שושן פורים קטן",
            body = """In a leap year, 15 Adar I is Shushan Purim Katan — the "walled-city" counterpart to Purim Katan, parallel to Shushan Purim on 15 Adar.

Like Purim Katan, it is a minor day: Tachanun is omitted. The full Purim mitzvot are observed only on regular Purim in Adar II.""",
        ),
        GuideTopic(
            id = "erev_pesach",
            title = "Erev Pesach",
            hebrewTitle = "עֶרֶב פֶּסַח",
            body = """Erev Pesach (14 Nissan) is the day of final Pesach preparation before the Seder.

Key preparations:
• Finish eating chametz by the end of the morning (check your local zmanim).
• Bedikat chametz — search for chametz by candlelight after nightfall the night before (or according to your community's schedule).
• Biur chametz — destroy remaining chametz the next morning.
• Sell chametz through your rabbi if needed.
• Prepare the Seder plate, matzah, wine, and Haggadot for the first night(s).

The Seder is held after nightfall on 15 Nissan (and the second night in the Diaspora). No chametz may be eaten after the morning deadline on Erev Pesach.""",
            learnMoreUrl = "https://www.chabad.org/holidays/passover/default_cdo/jewish/Passover.htm"
        ),
    )

    // Terms that appear in the upcoming/seasonal block and need an explanation anchor
    val termAnchorMap = mapOf(
        "Shabbat" to "shabbat_overview",
        "Shabbos" to "shabbat_overview",
        "Rosh Chodesh" to "rosh_chodesh",
        "Hallel" to "hallel",
        "Yaaleh V'yavo" to "yaaleh_vyavo",
        "Kiddush" to "kiddush",
        "Havdalah" to "havdalah",
        "Yom Tov" to "yom_tov",
        "Candles" to "candle_lighting",
        "Muktzeh" to "muktzeh",
        // Holidays
        "Rosh Hashana" to "rosh_hashana",
        "Yom Kippur" to "yom_kippur",
        "Sukkot" to "sukkot",
        "Shemini Atzeret" to "shemini_atzeret",
        "Simchat Torah" to "simchat_torah",
        "Chanukah" to "chanukah",
        "Purim" to "purim",
        "Purim Katan" to "purim_katan",
        "Shushan Purim Katan" to "shushan_purim_katan",
        "Pesach" to "pesach",
        "Erev Pesach" to "erev_pesach",
        "Passover" to "pesach",
        "Chol HaMoed Pesach" to "chol_hamoed_pesach",
        "Chol HaMoed Sukkot" to "chol_hamoed_sukkot",
        "Chol HaMoed" to "pesach",
        "Shavuot" to "shavuot",
        "Hoshana Raba" to "hoshana_raba",
        "Tu B'Shvat" to "tu_bshvat",
        "Pesach Sheni" to "pesach_sheni",
        "Lag BaOmer" to "lag_baomer",
        "Tu B'Av" to "tu_beav",
        "Tisha B'Av" to "tisha_beav",
        "Fast of Gedaliah" to "fast_gedaliah",
        "Fast of 10 Tevet" to "fast_10_tevet",
        "Fast of 17 Tammuz" to "fast_17_tammuz",
        "Fast of Esther" to "fast_esther",
        "Fast of Esther (Taanit Esther)" to "fast_esther",
        "Yom HaShoah" to "yom_hashoah",
        "Yom HaZikaron" to "yom_hazikaron",
        "Yom Ha'atzmaut" to "yom_haatzmaut",
        "Yom Yerushalayim" to "yom_yerushalayim",
        "Sefirat HaOmer" to "sefirat_haomer",
        "Omer" to "sefirat_haomer",
    )

    /** Glossary terms that share a word with a guide topic but are a different concept. */
    private val glossaryOnlyTermIds = setOf(
        "kiddush_levana",
        "kiddush_hashem",
        "motzei_shabbat",
        "shema_al_hamitah",
        "torah_study",
        "havdalah_in_kiddush",
        "yaknehaz",
        "holiday_havdalah",
        "oneg_shabbat",
        "ruach_ra'ah",
        "bar_mitzvah",
        "bat_mitzvah",
        "megillah_reading",
        "half_hallel",
        "full_hallel",
        "seudah_shlishit",
        "shabbat_candles",
        "eruv_tavshilin",
        "eruv_chatzerot",
        "eruv_techumin",
        "shabbat_shalom",
        "mishnah_berurah",
        "pesukei_d'zimra",
        "chamar_medina",
    )

    /** Exact display names from [UpcomingHolidayPlanner] and calendar backends. */
    private val upcomingHolidayTopicIds = mapOf(
        "Shabbat" to "shabbat_overview",
        "Rosh Chodesh" to "rosh_chodesh",
        "Chanukah" to "chanukah",
        "Purim" to "purim",
        "Purim Katan" to "purim_katan",
        "Shushan Purim Katan" to "shushan_purim_katan",
        "Pesach" to "pesach",
        "Shevi'i shel Pesach" to "pesach",
        "Acharon shel Pesach" to "pesach",
        "Erev Pesach" to "erev_pesach",
        "Chol HaMoed Pesach" to "chol_hamoed_pesach",
        "Shavuot" to "shavuot",
        "Sukkot" to "sukkot",
        "Chol HaMoed Sukkot" to "chol_hamoed_sukkot",
        "Hoshana Raba" to "hoshana_raba",
        "Shemini Atzeret" to "shemini_atzeret",
        "Simchat Torah" to "simchat_torah",
        "Rosh Hashana" to "rosh_hashana",
        "Yom Kippur" to "yom_kippur",
        "Yom Tov" to "yom_tov",
        "Tu B'Shvat" to "tu_bshvat",
        "Pesach Sheni" to "pesach_sheni",
        "Lag BaOmer" to "lag_baomer",
        "Tu B'Av" to "tu_beav",
        "Tisha B'Av" to "tisha_beav",
        "Fast of Gedaliah" to "fast_gedaliah",
        "Fast of 10 Tevet" to "fast_10_tevet",
        "Fast of Esther" to "fast_esther",
        "Fast of 17 Tammuz" to "fast_17_tammuz",
        "Yom HaShoah" to "yom_hashoah",
        "Yom HaZikaron" to "yom_hazikaron",
        "Yom Ha'atzmaut" to "yom_haatzmaut",
        "Yom Yerushalayim" to "yom_yerushalayim",
        "Birkat Hachamah" to "birkat_hachamah",
    )

    private fun normalizeHolidayLabel(label: String): String =
        label.replace('\u2019', '\'').trim()

    /** Longest matching label wins (e.g. "Rosh Hashana" before "Rosh"). */
    fun anchorForLabel(label: String): String? {
        val normalized = normalizeHolidayLabel(label)
        upcomingHolidayTopicIds[normalized]?.let { return it }
        return termAnchorMap.entries
            .sortedByDescending { it.key.length }
            .firstOrNull { (term, _) -> normalized.contains(term, ignoreCase = true) }
            ?.value
    }

    fun topicForAnchor(anchor: String): GuideTopic? =
        com.beardytop.beatzaddik.domain.SeasonalChecklistItems.israeliCivilHolidayExplainerForAnchor(anchor)
            ?.let { explainer ->
                GuideTopic(
                    id = explainer.id,
                    title = explainer.title,
                    body = explainer.body,
                )
            }
            ?: glossary.firstOrNull { it.id == anchor }
            ?: holidays.firstOrNull { it.id == anchor }

    fun topicForLabel(label: String): GuideTopic? =
        anchorForLabel(label)?.let(::topicForAnchor)

    /** Resolves a guide topic for upcoming-holiday rows and header occasions. */
    fun topicForUpcomingHoliday(name: String): GuideTopic? =
        com.beardytop.beatzaddik.domain.SeasonalChecklistItems.israeliCivilHolidayExplainerForName(name)
            ?.let { explainer ->
                GuideTopic(
                    id = explainer.id,
                    title = explainer.title,
                    body = explainer.body,
                )
            }
            ?: topicForLabel(name)

    /** Always returns a topic for an upcoming row — full explainer when mapped, hint-only last resort. */
    fun guideTopicForUpcoming(name: String, hint: String = ""): GuideTopic =
        com.beardytop.beatzaddik.domain.SeasonalChecklistItems.israeliCivilHolidayExplainerForName(name)
            ?.let { explainer ->
                GuideTopic(
                    id = explainer.id,
                    title = explainer.title,
                    body = explainer.body,
                )
            }
            ?: topicForUpcomingHoliday(name)
            ?: GuideTopic(
                id = "upcoming_${name.lowercase().replace(' ', '_').replace('\'', '_')}",
                title = name,
                body = hint.trim().ifBlank {
                    "Open “Shabbat guide” above for holiday explainers and related halachic topics."
                },
            )

    fun anchorForTerm(term: HalachicTerm): String? {
        if (term.id in glossaryOnlyTermIds) return null
        // Exact guide keys (Rosh Hashana, Yom Tov) — not substring matches on longer phrases.
        term.matchLabels
            .firstOrNull { label -> termAnchorMap.keys.any { it.equals(label, ignoreCase = true) } }
            ?.let { label ->
                return termAnchorMap.entries.first { it.key.equals(label, ignoreCase = true) }.value
            }
        if (!term.title.contains(' ')) {
            term.matchLabels.firstNotNullOfOrNull(::anchorForLabel)?.let { return it }
        }
        return if (term.id.startsWith("guide_")) term.id.removePrefix("guide_") else null
    }
}
