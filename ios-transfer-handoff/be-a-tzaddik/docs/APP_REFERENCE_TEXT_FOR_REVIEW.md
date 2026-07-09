# Be a Tzaddik — Complete App Text for Halachic Review

Plain-English export of **all user-visible halachic and educational copy** in the app.

**Part 1:** Short + full glossary · **Part 2:** Shabbat Guide · **Part 3:** Seasonal/hidden/festival checklist · **Part 4:** Daily mitzvot checklist · **Part 5:** Disclaimer, About, rest screens

Dynamic placeholders (local times, Hebrew dates, Omer day number, Chanukah night) appear as bracketed notes where the app fills them at runtime.

---

# Part 1 — Glossary


## Short Definitions

One-line and brief glossary entries — from BeginnerHalachaGlossary key-term blocks, HalachicTermsDictionary supplemental entries, checklist explainer key-term bullets, and brief enriched-only terms. When a term also has a longer definition below, the app may show the longer version when you tap the gold underline.

- **17 Tammuz** — fast marking breaching of Jerusalem's walls
- **aggadah** — Aggadah is the non-legal storytelling and teaching in the Talmud and midrash — ethics, theology, and narrative. It complements halacha (law). Shabbat table stories and many midrashim are aggadah.
- **Al Netilat Yadayim** — blessing on ritual handwashing
- **Amidah / Shmoneh Esrei** — the central standing silent prayer
- **aravah** — willow branches used in the Four Species
- **Arba Minim** — four plants waved on Sukkot: palm (lulav), citron (etrog), myrtle (hadas), willow (aravah)
- **Asara B'Tevet** — minor fast on 10 Tevet from dawn until nightfall; the only fast never postponed when it falls on Friday before Shabbat — in which case we break it with Friday night Kiddush
- **Aseret Yemei Teshuvah** — Ten Days of Repentance from Rosh Hashana to Yom Kippur
- **assur bemelacha** — forbidden to do melacha on Shabbat or Yom Tov
- **Barchu** — call to bless G-d that opens communal prayer with a minyan
- **Bedikat chametz** — formal search for chametz the night before Pesach
- **beged** — garment; tzitzit apply to four-cornered beged
- **Beit HaMikdash** — The Beit HaMikdash (Holy Temple) stood in Jerusalem as the center of sacrifice, pilgrimage, and divine presence. Its destruction on Tisha B'Av is mourned yearly.
- **Bentching / Birkat Hamazon** — Grace after Meals
- **Birkat HaMazon** — Grace After Meals after eating bread
- **Birkat Kohanim** — Priestly blessing; recited daily by everyone in Israel and by Sephardim worldwide; usually recited only on festivals by Ashkenazim in the Diaspora
- **bitul** — nullification (e.g. Kol Chamira nullifying chametz)
- **Biur chametz** — destroying chametz the next morning
- **Blech** — metal cover on the stove so food stays warm on Shabbat without cooking
- **block** — explainers show body text only.
 */
object BeginnerHalachaGlossary {

    /** Prepends nothing — callers may still pass [terms] for API stability; body only is shown. */
    fun withKeyTerms(terms: String, body: String): String = body.trim()

    fun block(vararg lines: String): String =
- **Boneh** — building melacha
- **borei me'orei ha'eish** — blessing over fire in Yaknehaz and regular Havdalah
- **Borei Minei Besamim** — catch-all blessing on fragrant herbs, spices, flowers, fruit, etc. (often said as the spice blessing in Havdalah after Shabbat, though some will say a more specific blessing on pleasant fragrances)
- **Bracha** — blessing said before a mitzvah or food
- **bracha acharona** — Bracha acharona is the after-blessing said when you ate or drank enough of a food but did not have a bread meal requiring Birkat Hamazon — for example borei nefashot or al hamichya.
- **candle lighting** — Shabbat candles before sunset; weekday Erev Yom Tov before sunset; Motzei Shabbat going into a Yom Tov at tzeit from a pre-existing flame
- **Chag** — festival (same idea as Yom Tov in everyday speech)
- **Chamar medina** — a prestigious local beverage (such as kosher liquor or coffee, per community standards) used for daytime Kiddush or Havdalah when wine is unavailable — never for Friday night Shabbat Kiddush, which strictly requires wine, grape juice, or washing for bread; not valid for the Seder's four cups per most poskim; never beer on Pesach (beer is chametz)
- **Chametz** — leavened grain (bread, beer, etc.) forbidden on Pesach
- **chametz she'avar** — chametz that was in your possession over Pesach (forbidden to benefit from after)
- **Chasidim** — a group of Jews who follow a movement that emphasizes deep mystical devotion, joyful worship, and a close-knit community led by a spiritual leader known as a Rebbe.
- **chatzos** — halachic midnight or midday depending on context
- **Chol HaMoed** — middle days of Pesach or Sukkot (lighter work rules than full Yom Tov)
- **Chol HaMoed Pesach** — intermediate days of Pesach between the festival days
- **Chol HaMoed Sukkot** — intermediate Sukkot days between the Festival days
- **Choresh** — plowing melacha
- **Daven / davening** — pray; any prayer (blessings, Shema, Tehillim, personal tefillah, or the services Shacharit, Mincha, Maariv, etc.)
- **Davening** — praying; any prayer, often refers to the formal daily prayer services
- **egg matzah** — matzah made with eggs and/or fruit juice (matzah ashira); not lechem oni (poor man's bread) and cannot be used at the Seder. Ashkenazim do not eat it on Pesach except for sick, elderly, or young children (Rema OC 462:4); Sephardim legally permit it, though many contemporary Sephardic authorities recommend avoiding it unless necessary (such as for the elderly or sick)
- **Eretz Yisrael** — the Land of Israel
- **Erev** — the eve before; "Erev Shabbat" is Friday, "Erev Pesach" is the day before Pesach begins at night
- **Eruv chatzerot** — symbolic merging of courtyards allowing carrying on Shabbat
- **Eruv tavshilin** — symbolic food set aside on Erev Yom Tov so you may continue food preparation on the festival for the Shabbat that follows (only in certain calendar years)
- **Eruv techumin** — extends the Shabbat walking boundary in special cases
- **etrog** — citron, one of the Four Species on Sukkot
- **Fast of Gedaliah** — fast day after Rosh Hashana
- **fleishig** — meat status; dairy may not be eaten for a waiting period afterward, usually 6 hours
- **Full Hallel** — complete Hallel psalms on festivals/Chanukah
- **gabbai** — synagogue officer who coordinates aliyot and services
- **Gam zu l'tovah** — this too is for the good; trust that hardship can be for the greater good
- **Gemara** — Talmudic commentary and discussion on the Mishnah
- **gematria** — numerical value of Hebrew letters
- **gemilut chasadim** — acts of loving-kindness
- **Geshem** — Rain. Can refer to the prayer for rain inserted on Shemini Atzeret throughout the winter
- **Gozez** — shearing or cutting hair/nails melacha
- **Grace After Meals** — Birkat Hamazon after eating bread meals
- **hadas** — myrtle branch in the Four Species
- **Hagbah** — lifting the Torah scroll for the congregation to see; Ashkenazim/Chabad after reading, Sephardim before
- **Hakafot** — dancing while circling around the synagogue with Torah scrolls on Simchat Torah
- **hakarat hatov** — recognizing the good; thanking someone for a kindness
- **halachot** — laws or legal details (plural of halacha)
- **Half Hallel** — shortened Hallel psalms on Rosh Chodesh and after the first Yom Tov of Pesach
- **Hallel** — psalms of praise added on festivals and Rosh Chodesh
- **Hamapil** — blessing entrusting the soul to G-d before sleep, part of bedtime Shema
- **HaNeiros halalu** — paragraph sung after lighting Chanukah candles
- **Havdalah** — ceremony separating holy time from weekday (wine, candle, and spices on a normal Motzei Shabbat; spices omitted on Motzei Yom Kippur unless Yom Kippur fell on Shabbat; spices also omitted when Shabbat leads directly into Yom Tov — Yaknehaz — because the joy of the festival replaces the extra soul of Shabbat)
- **Havdalah in Kiddush** — Yaknehaz order when Shabbat leads into Yom Tov
- **hechsher** — in kashrut, a certification that food was produced under reliable kosher supervision
- **honoring your parents** — Kibud Av V'Eim - mitzvah to honor/respect and care for parents
- **Hoshana Raba** — seventh day of Sukkot; Minhag Nevi'im — beating aravot five times on the ground
- **kallah** — Kallah means bride; kallah classes teach taharat hamishpacha, mitzvot of the home, and wedding laws before marriage. The kallah and chatan prepare for chuppah and building a Jewish family.
- **karmelit** — rabbinic semi-public area with carrying restrictions
- **Kashering** — making utensils kosher
- **kehilla** — Jewish community or congregation
- **ketubah** — marriage contract outlining husband's obligations
- **Kezayit** — olive-sized portion in halacha; many authorities measure it as about a golf ball (35–40 ml / 25–33 g depending on poskim; confirm with your rav)
- **Kiddush** — blessing over wine that sanctifies Shabbat or Yom Tov at the meal
- **Kinot** — elegies read on Tisha B'Av mourning the Temple
- **kippah** — skullcap worn as a sign of G-d's presence above
- **kisui rosh** — hair covering for married women in many communities
- **Kitniyot** — legumes/rice; an Ashkenazi customary stringency on Pesach (not Torah chametz); halachically permitted for Sephardim
- **klaf** — The klaf is the special kosher parchment on which scribes write Torah scrolls, mezuzots, tefillin scrolls, etc.
- **kneidlach** — matzah balls; those who observe the stringency of not eating gebrochts do not eat kneidlach on Pesach until the 8th day in the diaspora
- **kohen** — priest descended from Aaron. They perform the service in the Temple when it's standing.
- **Kol Chamira** — declaration nullifying any chametz still in your possession
- **Krias Shema** — recitation of the Shema and its blessings
- **Lag BaOmer** — 33rd day of the Omer; mourning eased in many communities; Chabad keeps haircut restrictions until Shavuot except for a 3 year old boy's first haircut
- **Losh** — kneading melacha
- **loving your fellow Jew** — central Torah principle taught by Rabbi Akiva
- **lulav** — palm branch waved with the etrog, willows, and myrtle branches on Sukkot
- **Makeh B'patish** — final finishing touch melacha
- **maleh lugmov** — Maleh lugmov is drinking a cheekful from the Kiddush cup (approx 2 fl oz) after the blessing — from at least a total of a revi'it of wine or grape juice in the cup. Grape juice counts like wine for many poskim.
- **Maoz Tzur** — "Rock of Ages," Chanukah hymn sung after lighting
- **Matan Torah** — receiving the Torah at Mount Sinai, celebrated on Shavuot
- **matana al menat lehachzir** — gift on condition it is returned; commonly used for borrowing a lulav on the first day of Sukkot
- **Matanot la'evyonim** — gifts to at least two poor people on Purim day
- **Mavir** — creating a new flame; forbidden on Shabbat and Yom Tov; transferring an existing flame is permitted on Yom Tov
- **Mechabeh** — extinguishing fire melacha
- **Mechirat chametz** — selling chametz to a non-Jew through your rabbi so you do not own it on Pesach
- **mechitza** — A mechitza divides men and women's seating in shul during prayer so focus stays on tefillah per traditional practice. Heights and designs vary by community.
- **Megillah** — Book of Esther read on Purim
- **Megillah reading** — hearing the Book of Esther read on Purim
- **Melaben** — laundering melacha
- **Melacha** — transformative labor forbidden on Shabbat (including cooking). On Yom Tov most melachot remain forbidden, but ochel nefesh — food preparation for that day's meals, including cooking from a pre-existing flame — is permitted (Exodus 12:16). Other examples still restricted on Yom Tov: writing, building, kindling a new fire, most phone/electronics per your rav
- **melachot** — the 39 categories of transformative labor forbidden on Shabbat
- **Melave Malka** — escorting the Shabbat Queen — Motzei Shabbat meal (מְלַוֶּה מַלְכָּה) through dawn Sunday
- **melicha** — salting meat to remove blood after shechita
- **menorah** — Chanukah candelabra; candles are lit each night
- **mesorah** — tradition faithfully transmitted generation to generation
- **Mezonot** — Mezonot (borei minei mezonot) is the blessing on grain foods that are not bread — cake, crackers, pasta, cereal. It is shorter than hamotzi but still thanks G-d for grain sustenance.
- **milchig** — dairy status
- **Minhag** — community or family custom (not always identical in every synagogue)
- **Mishloach manot** — sending at least two ready-to-eat foods to one friend on Purim day
- **Modah** — feminine form of Modeh (grateful) in Modeh Ani
- **Motzei Shabbat** — Motzei Shabbat is Saturday night after Shabbat ends.
- **Musaf** — extra Amidah on Shabbat, Yom Tov, and Rosh Chodesh remembering Temple offerings
- **Nachum Ish Gamzu** — sage known for finding good in every situation
- **nefesh** — Nefesh is the 'animal' part of the soul closest to physical life — appetite, hunger, physical desires, etc. Torah and mitzvot attempt to refine the nefesh toward holiness instead of animal instincts.
- **Nerot** — candles, often referring especially to Shabbat and Yom Tov lights
- **Nevi'im** — Nevi'im (Prophets) is the middle section of Tanach — Joshua, Kings, Isaiah, Jeremiah, and the other prophetic books. They call Israel to justice and loyalty to Hashem. Reading them is Torah study.
- **nusach Ashkenaz** — Ashkenazi prayer wording
- **nusach Sefard** — Ashkenazi-Chasidic prayer rite (not the same as Sephardic Nusach Edot HaMizrach)
- **Ochel nefesh** — the halachic allowance to perform certain food preparation tasks on Yom Tov (festival days) for consumption on that same day
- **Olam HaBa** — Olam HaBa is the World to Come — the eternal reality of closeness to G-d after this life (Heaven). Mitzvot, Torah study, and acts of kindness build merits that the sages describe as unfathomable treasures in that world.
- **pareve** — neutral foods (neither meat nor dairy) such as fish, eggs, and produce
- **Parshah** — weekly Torah portion read in synagogue
- **Pesachdik** — kosher for Passover standards
- **Pirsumei nisa** — publicizing the miracle (why the menorah is placed visibly)
- **pitom** — This is the knobby, stem-like protrusion that extends from the top of the etrog (opposite the branch). The shoshanta rests on top of it.
- **Psalms** — Tehillim
- **pshat** — Pshat is the plain, straightforward meaning of a Torah verse — what the text says on its surface before deeper layers. Rashi on Chumash mainly explains pshat. Learning pshat is the foundation before midrash or Kabbalah.
- **Purim day** — 14 Adar (15 in walled cities); Megillah, gifts to friends/poor, and feast
- **Rabbi** — English title for an ordained Jewish spiritual leader (teacher, counselor, or synagogue leader)
- **Rav** — Hebrew title for a senior Torah scholar who rules on halacha (your community's decisor)
- **refuah shleimah** — complete healing; said when praying for someone ill
- **Revi'it** — one-quarter of a log; minimum ritual liquid volume for Kiddush, Havdalah, netilat yadayim, and the four cups (86–150 ml depending on poskim; confirm with your rav)
- **ruach** — wind or spirit; middle soul level in some teachings
- **Ruach Ra'ah** — Ruach Ra'ah ("evil spirit") is a rabbinic term for the impurity resting on hands after sleep. Morning netilat yadayim removes it by washing hands alternating right and left 3 times each.
- **Schach** — plant covering on the sukkah roof
- **Seder** — Pesach night ritual meal with the Haggadah, matzah, four cups of wine, etc.
- **Sefardim** — Jews following Sephardi rites and customs
- **Sefirah** — counting period between 2nd day of Pesach and Shavuot; mourning customs and when they end vary by minhag (Ashkenaz, Sephard, Chabad, etc.)
- **Sefirat HaOmer** — counting 49 days from Pesach to Shavuot
- **Selichot** — extra penitential prayers before Rosh Hashana (timing varies by nusach)
- **Seudah** — festive meal (e.g. Purim afternoon feast)
- **seudat mitzvah** — festive meal tied to a mitzvah (e.g. brit, siyum, wedding, etc.)
- **Shabbat** — the weekly Sabbath from Friday sunset until halachic nightfall (tzeit) on Saturday night, per your community's zmanim
- **Shabbat candles** — candles lit before sunset to welcome Shabbat
- **Shabbat Shalom** — peaceful Sabbath greeting
- **Shabbos** — Shabbat
- **Shalom bayit** — peace in the home; a central Jewish value
- **Shamayim** — Shamayim means Heaven or the skies — in yirat Shamayim (awe of Heaven) it means living with awareness that G-d sees all actions.
- **Shamor** — "guard (Shabbat)" from the Deuteronomy version of the Ten Commandments
- **Shavuot** — festival marking Matan Torah (receiving the Torah at Sinai)
- **Shehecheyanu** — blessing for reaching a new season or mitzvah
- **Shemini Atzeret** — eighth day festival after Sukkot; Simchat Torah celebrations fold into it in Israel
- **Shemoneh Esrei** — the eighteen (now nineteen) blessings of the Amidah
- **shevarim** — broken shofar blast
- **shinui** — unusual change of method that sometimes permits melacha leniency
- **Shir HaMaalot** — Shir HaMaalot is Psalm 126 — "When G-d returns the captivity of Zion" — sung before bentching on Shabbat and festivals. It expresses joy in redemption and hope for complete return. Many know the tune from Shabbat tables.
- **shmirat halashon** — guarding one's speech from lashon hara and harmful words
- **Shmoneh Esrei** — the nineteen blessings of the standing Amidah prayer
- **shmurah matzah** — matzah guarded from moisture from harvest; required by many for the Seder
- **shochet** — trained kosher slaughterer
- **Shomer Daltot Yisrael** — Guardian of the doors of Israel (acronym Shin-Dalet-Yod (one of Hashem's names) on mezuzah)
- **Shul** — synagogue
- **Simchat Torah** — rejoicing with the Torah; in Israel celebrated on the same day as Shemini Atzeret; the second day of the Shemini Atzeret Yom Tov in the Diaspora
- **Simchat Yom Tov** — Torah mitzvah to rejoice on the festival (V'samachta b'chagecha)
- **Siyum** — festive meal (seudat mitzvah) celebrating finishing a section of Torah study; eating at the meal (not merely hearing the siyum) exempts firstborns from Taanit Bechorot
- **Soter** — demolishing melacha
- **Ta'anit** — a public or personal fast day
- **Taanit Bechorot** — Fast of the Firstborn on Erev Pesach (many break or nullify the fast with the seudat mitzvah after a siyum)
- **Taanit Esther** — fast day before Purim
- **Tachanun** — penitential prayers omitted on happy days
- **tallit gadol** — large prayer shawl with tzitzit worn during Shacharit/Musaf and all day on Yom Kippur
- **tallit katan** — small four-cornered garment with tzitzit, worn generally over or under a shirt per community custom
- **Tanach** — Tanach (Tanakh) is the Hebrew Bible in three parts: Torah (the five books), Nevi'im (Prophets), and Ketuvim (Writings). Reading Tanach in Hebrew or translation is Torah study; Psalms (Tehillim) are part of Ketuvim.
- **tefillah** — prayer; from a root meaning to judge oneself
- **teruah** — quick staccato shofar blasts
- **tevilah** — ritual immersion in a mikveh
- **three stars** — colloquial marker for halachic nightfall (tzeit); actual timing varies slightly by community and location
- **Tikkun Leil Shavuot** — all-night Torah study on the first night
- **Tisha B'Av** — fast mourning the destruction of the Temple
- **Tochen** — grinding melacha
- **tzedakah box** — charity box; collecting coins for those in need is itself a mitzvah
- **Tzeit** — halachic nightfall (when three medium stars appear). Required start time for night mitzvot like bedikat chametz, Chanukah candles, Megillah, and counting the Omer. Note: While standard Shabbat and weekday Erev Yom Tov candles must be lit before sunset, whenever a festival begins on a Saturday night (Motzei Shabbat), the Yom Tov candles strictly must be lit after sunset, only once nightfall (tzeit) has arrived, using a pre-existing flame
- **tzniut** — modest dress and conduct
- **Vatodi'enu** — Vatodi'enu ("You have made us know") is an insert in the Maariv Amidah on Saturday night when Yom Tov begins after Shabbat. It acknowledges that Shabbat has ended and the festival has begun.
- **walk in G-d's ways** — imitating divine kindness (clothing the needy, visiting the sick)
- **Yaaleh V'yavo** — paragraph added to Amidah and Grace after Meals on Rosh Chodesh and festivals
- **Yaknehaz** — order when Shabbat leads into Yom Tov: Wine → Kiddush → candle (Ner) → Havdalah text → Shehecheyanu (Zeman); spices are omitted
- **yichud** — prohibition against seclusion of a man and woman in private unless they are married or immediate blood relatives
- **yirat Shamayim** — fearing of Heaven; living with awareness that G-d is present
- **Yom Tov** — a festival day (Pesach, Sukkot, Rosh Hashana, etc.) with work rules similar to Shabbat; some food prep is allowed on the festival itself
- **Zachor** — "remember" from the Exodus version of the Ten Commandments telling the Jews to remember Shabbat
- **Zorea** — planting melacha


## Full Glossary Definitions (HalachicTermsDictionary — tappable in app)

Complete definitions as merged in the app (enriched entries override shorter ones). Listed alphabetically.

### 17 Tammuz

fast marking breaching of Jerusalem's walls

### Afikoman

The Afikoman is matzah eaten at the end of the Seder so no other food follows — remembering the Pesach sacrifice. Children often "steal" it for a prize, adding joy. You need a kezayit-sized piece eaten before chatzos halachic (midpoint of the night) per many poskim. It is the last taste of matzah the Seder requires.

### aggadah

Aggadah is the non-legal storytelling and teaching in the Talmud and midrash — ethics, theology, and narrative. It complements halacha (law). Shabbat table stories and many midrashim are aggadah.

### al hamichya

Al hamichya (Al HaMichya) is the after-blessing on mezonot, grain foods that are not bread — cake, crackers, pasta, cereal, etc. — when you ate a kezayit (approx. golf ball size of food) within k'dei achilat pras (4-9 mins). If you had a bread meal, Birkat Hamazon covers all foods. You may also need to say Birkat Hamazon if you ate a lot of mezonot- depending on the type of food. Ask your rav.

### Al HaNissim

Al HaNissim ("for the miracles") is added to the Amidah and Birkat Hamazon on Chanukah and Purim. It summarizes the salvation — the Maccabees or Esther/Mordecai — and thanks G-d. On Purim Meshulash (Jerusalem when 15 Adar is Shabbat), Al HaNissim is recited only on Shabbat — not on Friday or Sunday when other Purim mitzvot occur.

### Al netilat lulav

Al netilat lulav is the blessing before taking the Four Species on Sukkot — "Who sanctified us with His mitzvot and commanded us to take the lulav." Men say it every day of the festival when taking the lulav (except Shabbat); women follow community custom. Shehecheyanu is said on the first day only. Then wave the lulav in the directions your siddur prescribes.

### Al Netilat Yadayim

blessing on ritual handwashing

### Aleinu

Aleinu is a closing prayer declaring G-d's sovereignty and our hope for universal recognition of His kingship. It ends daily Shacharit and Maariv and appears near the end of High Holiday services. The bow at "we bend the knee" is a moment of submission and awe.

### aliyah

Aliyah means "going up" — being called to bless the Torah reading in shul, or immigrating to Israel. In shul, the oleh says blessings before and after the portion; the baal koreh chants. Honoring aliyot goes to kohanim, leviim, a groom, bar mitzvah, and yahrzeit per order on the gabbai's list.

### alot hashachar

Alot hashachar is halachic dawn — when the sky begins to lighten. Many morning laws start here, such as the beginning of public fast days. It is before sunrise (netz). It is not the normal time for the bracha on tallit and tefillin — see misheyakir. In great need, Igros Moshe (O.C. 4:6) allows putting them on after alot hashachar without a bracha and reciting the brachos after misheyakir — ask your rav.

### Amen

Amen affirms a bracha someone else recited, "so may it be." The Talmud praises one who answers Amen even more than the one who made the blessing. It should not be rushed or mumbled; it is a small but constant mitzvah opportunity in synagogue and at home. (Literally, it's an acronym for 'G-d, King Who is faithful' — Keil Melech Ne'eman).

### Amidah

The Amidah ("standing" prayer) is the core of every service — also called Shmoneh Esrei (eighteen, now nineteen blessings as 1 was added). It is recited silently while standing, facing Jerusalem or the Temple Mount (if inside Jerusalem), with feet together.

### Amidah / Shmoneh Esrei

the central standing silent prayer

### aravah

willow branches used in the Four Species

### Arba Minim

Arba Minim are the Four Species: lulav (palm), etrog (citron), hadas (myrtle), and aravah (willow). They are waved during Hallel on Sukkot (except Shabbat). The etrog must be kosher — not punctured or dried. Many check etrogim before buying. Holding them together unifies the Jewish people's diversity in one mitzvah.

### Asara B'Tevet

minor fast on 10 Tevet from dawn until nightfall; the only fast never postponed when it falls on Friday before Shabbat — in which case we break it with Friday night Kiddush

### Aseret Yemei Teshuvah

Ten Days of Repentance from Rosh Hashana to Yom Kippur

### Ashkenazi

Ashkenazi Jews trace roots to Central and Eastern Europe and follow customs and nusach developed there — Yiddish culture, certain Pesach stringencies (kitniyot), and specific mourning and festival practices. "Ashkenaz" in a siddur means that prayer wording. Many American and Israeli shuls are Ashkenazi; ask your rav which sub-tradition applies.

### assur bemelacha

forbidden to do melacha on Shabbat or Yom Tov

### aveilut

Aveilut is mourning practice after the loss of a close relative. Customs include shiva (seven days), shloshim (thirty days), and for parents, eleven months. During the Omer, aveilut customs overlap with national mourning for Rabbi Akiva's students. During the 3 weeks from 17 Tammuz to 9 Av, we mourn the Temple's destruction.

### baal koreh

The baal koreh (master of reading) chants the Torah from the scroll with correct trop and pronunciation. Training takes months. Congregants follow in Chumash. A mistake that changes meaning may require correction — the oleh listens, not reads aloud from the scroll.

### Baal Shem Tov

The Baal Shem Tov (Rabbi Israel ben Eliezer, c. 1700–1760) founded the Chasidic movement, teaching that every Jew can serve G-d with joy and that divine presence fills all creation. Chabad traces its spiritual lineage to his disciples.

### bal yera'eh

Bal yera'eh ("it shall not be seen") is the Torah prohibition against chametz remaining visible in your domain on Pesach. Together with bal yimatzei ("it shall not be found"), it drives bedikat, biur, and mechirat chametz. Even crumbs in your control matter. Sold chametz in a closed sold area must stay inaccessible.

### bal yimatzei

Bal yimatzei forbids chametz being found in your possession on Pesach. Spiritual nullification (Kol Chamira) complements but does not replace cleaning. Chametz you owned over Pesach without sale may be forbidden to benefit from after Pesach — serious questions go to your rav.

### bar mitzvah

Bar mitzvah is when a boy turns thirteen and becomes obligated in mitzvot — tefillin, fasting, counting the Omer, and the full moral law. The celebration is seudat mitzvah joy, but the essence is responsibility. Parents' chinuch before thirteen prepares him. Aliyah to the Torah often marks the day in shul.

### Barchu

call to bless G-d that opens communal prayer with a minyan

### bat mitzvah

Bat mitzvah is when a girl reaches twelve years and one day and becomes obligated in mitzvot not dependent on the Temple's time — the standard age of obligation in traditional halacha. Customs for celebration vary — speeches, learning projects, family meal. Women's mitzvot include Shabbat candles, kashrut, charity, and Torah study — specifics follow family and rav.

### bedieved

Bedieved describes halachic guidance after the ideal was missed — unintentionally or unavoidably. It is not permission to plan poorly; it is how to recover. Example categories: Amidah repeats, chametz found on Pesach, missed Omer count. Your rav applies bedieved rules to your case; articles in the app summarize common patterns from standard halachic sources.

### bedikat chametz

Bedikat chametz is the formal search for chametz on the night before Pesach, after nightfall, using candlelight (or flashlight per many poskim). Bread is often hidden in rooms so the bracha on the search isn't in vain (if there was no bread found); the next morning it is destroyed in biur chametz. Even if the house was cleaned for weeks, halacha still requires this mitzvah night.

### beged

garment; tzitzit apply to four-cornered beged

### bein kodesh l'chol

Bein kodesh l'chol means "between holy and mundane" — the wording in regular Saturday-night Havdalah when Shabbat ends and weekday begins. People say 'Baruch hamavdil bein kodesh l'chol' before doing melacha if they have not yet heard full Havdalah or recited the maariv amidah with the Havdalah insert. When Shabbat flows into Yom Tov instead, the wording is bein kodesh l'kodesh (holy to holy), not l'chol.

### bein kodesh l'kodesh

Bein kodesh l'kodesh means "between holy and holy." In havdalah when Shabbat leads into Yom Tov, you declare that one level of holiness (Shabbat) ends as another (the festival) begins — not bein kodesh l'chol (holy to weekday). That wording matches the spiritual reality: Saturday night may be still sacred, but the nature of the day changes. Baruch hamavdil bein kodesh l'kodesh is also said after nightfall to permit Yom Tov melacha before Kiddush.

### Beit HaMikdash

The Beit HaMikdash (Holy Temple) stood in Jerusalem as the center of sacrifice, pilgrimage, and divine presence. Its destruction on Tisha B'Av is mourned yearly.

### beitzah

Beitzah is a roasted or boiled egg on the Seder plate — a reminder of festival offerings and of mourning for the destroyed Temple. It is traditionally eaten during the Seder meal (often dipped in salt water at Shulchan Orech), so boiling or roasting it on Yom Tov night is permitted if needed — unlike the zeroa.

### bentcher

A bentcher (birkon) is a small booklet with Birkat Hamazon and sometimes Shabbat songs and benching additions. Keeping one at the table makes bentching after bread meals easy. Many are bilingual; some include zimun instructions and festival inserts.

### Bentching

Bentching is Birkat Hamazon — Grace After Meals after eating a kezayit of bread within a meal. It thanks G-d for food and the Land. On Shabbat and festivals, Psalm 126 (Shir HaMaalot) precedes the second blessing. Yaaleh V'yavo is added on Rosh Chodesh and chag. Zimun invites others when three or more men ate bread together per minhag.

### Bentching / Birkat Hamazon

Grace after Meals

### besamim

Besamim are fragrant spices smelled during Havdalah Saturday night — comforting the soul as the extra neshama yeteira departs back into weekday life. The blessing is Borei minei besamim. Cloves, myrtle, or sweet spices are common. Yaknehaz omits besamim when Shabbat flows into Yom Tov — the joy of the festival itself comforts the soul, so spices are halachically unnecessary.

### birchat hamapil

Birchat Hamapil (Hamapil) is the blessing before sleep entrusting the soul to G-d. Say it as the last words before closing your eyes. After reciting it, do not eat, drink, or speak until sleep; quietly repeating familiar Shema or protection verses while dozing is permitted, but classical poskim discourage sitting up for new Torah study or lengthy Tehillim (Biur Halachah O.C. 239:1). Some omit it if they know they may not fall asleep for a long time. It pairs with Shema al hamitah for protection.

### Birchot HaShachar

Birchot HaShachar are morning blessings thanking G-d for eyesight, clothing, strength, and being created — said at shul or home. They orient the day toward gratitude before requests. Women and men both say them; some women omit certain blessings per custom. They parallel waking gifts we overlook.

### Birkat HaMazon

Grace After Meals after eating bread

### Birkat Kohanim

Birkat Kohanim (the priestly blessing) is when kohanim bless the congregation with the words of Numbers 6:24–26.

### Bishul

Bishul is cooking on Shabbat — heating food until yad soledet bo (hand recoils). Fully cooked dry food may be reheated in some cases; liquids are stricter. A blech or slow cooker set before Shabbat keeps food warm. Yom Tov allows cooking for that day — different rules entirely.

### bitachon

Bitachon is trust in G-d — believing that He provides what you need and that hardship can have purpose even when you cannot see it. It is related to emunah (faith) but stresses calm reliance in daily worry. Mussar and Chasidut both teach practical bitachon.

### bitul

nullification (e.g. Kol Chamira nullifying chametz)

### biur chametz

Biur chametz is destroying chametz the morning after bedikat chametz — usually by burning before the fifth halachic hour of the day. The final Kol Chamira nullification must be completed before that same deadline; a late Kol Chamira after the 5th hour is invalid because chametz is then assur b'hana'ah. Whatever remains must be gone from your ownership. It completes the removal process together with mechirat chametz for sold items. Do not eat breakfast chametz that morning if your custom is to finish biur first.

### Blech

A blech is a metal cover placed on the stove to cover flames and heat food on Shabbat.

### block

explainers show body text only.
 */
object BeginnerHalachaGlossary {

    /** Prepends nothing — callers may still pass [terms] for API stability; body only is shown. */
    fun withKeyTerms(terms: String, body: String): String = body.trim()

    fun block(vararg lines: String): String =

### Boneh

building melacha

### borei me'orei ha'eish

blessing over fire in Yaknehaz and regular Havdalah

### Borei Minei Besamim

catch-all blessing on fragrant herbs, spices, flowers, fruit, etc. (often said as the spice blessing in Havdalah after Shabbat, though some will say a more specific blessing on pleasant fragrances)

### borei nefashot

Borei nefashot is the short after-blessing on many foods and drinks — fruit (aside from dates, grapes, olives, figs, and pomegranates which use the Me'ein Shalosh bracha), vegetables, drinks (other than wine/grape juice which use Me'ein Shalosh), meat, cheese, etc.

### Borei Pri HaAdamah

Borei pri ha'adamah is the blessing on produce that grows from the earth — potatoes, cucumbers, melons, and many berries. It covers foods that are not tree fruit. Water and plain beverages have their own blessings or none.

### Borei Pri HaEtz

Borei pri ha'etz is the blessing on fruit that grows on trees — apples, peaches, grapes (for eating as fruit), and similar. Say it before eating. Fruit from the ground (bananas, strawberries) use borei pri ha'adamah. When in doubt, ask your rav or use a bracha guide.

### Borei Pri HaGafen

Borei pri hagafen is the blessing on wine and grape juice — "Who creates the fruit of the vine." It is required before Kiddush, the four cups at the Seder, and drinking wine for simcha. After the blessing, drink a meaningful amount (revi'it per many poskim). Ordinary table grapes use a different blessing.

### Borer

Borer (selecting) forbids sorting a mixed pile on Shabbat by removing unwanted from wanted unless three conditions are met: take the good from the bad, by hand (not a dedicated strainer), for immediate use. Example: picking bones from your plate right before eating is OK; sorting a salad bowl for later is not.

### Bracha

A bracha is a structured blessing, usually opening with "Baruch Atah Hashem…" It is how we acknowledge G-d as the Source of all blessing before eating, before mitzvot, and at holy moments. Different foods and mitzvot have specific brachot; saying the wrong one or none when required is a halachic issue. Brachot also train gratitude — pausing to recognize that what we enjoy comes from Above.

### bracha acharona

Bracha acharona is the after-blessing said when you ate or drank enough of a food but did not have a bread meal requiring Birkat Hamazon — for example borei nefashot or al hamichya.

### brit milah

Brit milah (bris) is the covenant of circumcision on the eighth day for a healthy Jewish boy, usually with festive meal (seudat mitzvah). It marks entry into the Jewish covenant with G-d. It is performed by a trained mohel. Postponement for health issues is sometimes required, and not a problem as long as it's done once possible.

### candle lighting

Shabbat candles before sunset; weekday Erev Yom Tov before sunset; Motzei Shabbat going into a Yom Tov at tzeit from a pre-existing flame

### Chabad

Chabad is a Chasidic movement founded by the Baal Shem Tov's disciples and led for seven generations by the Lubavitcher Rebbeim. Followers emphasize love of every Jew, joyful mitzvot, and deep study (Chitas: Chumash, Tehillim, Tanya). Prayer follows Nusach Ari. Chabad houses and shluchim welcome beginners without judgment.

### Chag

festival (same idea as Yom Tov in everyday speech)

### challah

Challah is the bread often prepared for Shabbat and festival meals — often braided and covered until Kiddush. Two loaves (lechem mishneh) recall the double portion in the desert. On Friday night, if wine or grape juice is completely unavailable, Kiddush can be recited over the challah loaves, provided one washes hands for bread immediately before reciting Kiddush and replaces the wine blessing with Hamotzi. Bread can never substitute for daytime Shabbat Kiddush on Saturday morning.

### chamar medina

Chamar medina ("drink of the land") is a prestigious local beverage — such as coffee, tea, or kosher liquor — that some authorities allow for daytime Kiddush or Havdalah when wine or grape juice is unavailable or difficult. It can never be used for Friday night Shabbat Kiddush, which strictly requires wine, grape juice, or bread (Shulchan Arukh O.C. 272).

### Chametz

Chametz is leavened grain product made from wheat, barley, rye, oats, or spelt. Bread, beer, and many processed foods can be chametz. On Pesach, owning, eating, or benefiting from chametz is forbidden.

### chametz she'avar

chametz that was in your possession over Pesach (forbidden to benefit from after)

### Chanukah

Chanukah is eight nights lighting menorah for the oil miracle and victory over Greek oppression. Al HaNissim is added in prayer. On Friday, light before Shabbat candles using long candles or extra oil — standard small Chanukah candles burn out before nightfall and invalidate the mitzvah. Gifts are American custom, not core mitzvah. Place menorah for pirsumei nisa. Work is permitted; the focus is light and thanks.

### charoset

Charoset is the sweet paste (apples, wine, nuts, etc.) recalling mortar between bricks. It is dipped with maror for Korech. Recipes vary by family — Ashkenazi, Sephardi, and Persian charoset all fulfill the mitzvah when made with intent. It is one of the Seder plate's sensory contrasts: bitter and sweet together.

### Chasidim

a group of Jews who follow a movement that emphasizes deep mystical devotion, joyful worship, and a close-knit community led by a spiritual leader known as a Rebbe.

### Chassidut

Chassidut (Chasidic philosophy) teaches serving G-d with joy, sincerity, and awareness that divine sparks fill all of life. Founded by the Baal Shem Tov, it spread through courts like Chabad, Breslov, and Satmar. Tanya is a central Chassidut text for daily study.

### chatzos

halachic midnight or midday depending on context

### Chazal

Chazal (חז"ל) is an acronym for "our Sages of blessed memory" — the rabbis of the Mishnah, Talmud, and midrash who transmitted halacha and values. When a source says "Chazal teach," it refers to this collective tradition rather than a single author.

### chazeret

Chazeret is the second bitter herb on the Seder plate in some traditions — often romaine lettuce stalk — alongside maror. Not every community uses a separate chazeret; some use only horseradish for maror. Follow your Haggadah and family custom.

### chazzan

The chazzan (cantor) leads the congregation in prayer — especially when the service is sung or when a minyan needs a skilled voice for repetition of the Amidah. The role is shaliach tzibur (emissary of the community), not a performer. Many weekday services have no professional chazzan; a member leads instead.

### chesed

Chesed is loving-kindness — going beyond the strict requirement to help another person. Gemilut chasadim (bestowing kindness) includes visiting the sick, burying the dead, and free loans. The world is said to stand on Torah, avodah (service), and gemilut chasadim.

### cheshbon hanefesh

Cheshbon hanefesh is an honest spiritual accounting — reviewing your week: speech, honesty, prayer, family. Many do this before bed or during Elul. Name one strength and one fixable weakness. Without cheshbon, the same mistakes repeat; with it, teshuvah becomes concrete.

### Chilul Hashem

Chilul Hashem is desecrating G-d's Name — behavior that makes Torah or Jews look contemptible, especially in public. Fraud, rage, or hypocrisy by a visibly Jewish person harms the whole people. The opposite is kiddush Hashem — sanctifying G-d's Name through integrity. Non-Jews judging Judaism by your conduct is a serious responsibility.

### chinuch

Chinuch is training children to do mitzvot before they are bar or bat mitzvah — when obligation becomes personal. Parents teach gradually: brachot, tzitzit, giving charity, Shabbat candles (per minhag), and respectful shul behavior. "Educate the child according to his way" (Mishlei) means matching pace to the child's temperament. Chinuch builds habit; understanding deepens with age.

### Chitas

Chitas (חיט"ס) is Chabad's daily study cycle: Chumash (weekly Torah portion with Rashi), Tehillim (Psalms divided by day of month), and Tanya. Many complete it after morning prayers, but you may learn today's portions anytime before the next Hebrew day. Hayom Yom is an additional short daily reading.

### chiyuv

Chiyuv means obligated — the mitzvah applies to you and you are expected to fulfill it. Bar mitzvah creates chiyuv for a boy's mitzvot; other exemptions (illness, danger) can remove chiyuv temporarily. Knowing whether you are chayav or patur is why we ask a rav.

### Chofetz Chaim

Rabbi Yisrael Meir Kagan (Chofetz Chaim) wrote the definitive guide to guarding speech and the Mishnah Berurah on daily halacha. His works teach that gossip destroys communities and that ordinary Jews can master Shabbat, kashrut, and prayer law. Studying his speech laws transforms relationships more than any lecture on morality.

### Chol HaMoed

Chol HaMoed are the intermediate days sandwiched between the first and last days of Pesach or Sukkot. They are still festival time, but melacha rules are lighter than on Yom Tov or Shabbat: many forms of work and food preparation are permitted, with limits on certain commercial activity and some melachot. Shaving and haircuts are strictly prohibited (O.C. 531) unless under specific exceptions — ask your rav.

### Chol HaMoed Pesach

intermediate days of Pesach between the festival days

### Chol HaMoed Sukkot

intermediate Sukkot days between the Festival days

### cholent

Cholent (chamin, hamin) is Shabbat stew left on a blech or slow cooker from before Shabbat — hot food without cooking on Shabbat. Every community has recipes: beans, meat, potatoes. It makes oneg Shabbat practical. Start it early Friday so it has time to cook partway before Shabbat.

### Choresh

plowing melacha

### Chumash

A Chumash (from chamesh, "five") is a printed book of the five books of the Torah — Bereishit, Shemot, Vayikra, Bamidbar, and Devarim. Most Chumashim include commentaries such as Rashi. You use a Chumash for Shnayim Mikra, following along in shul, or learning at home.

### chuppah

The chuppah is the wedding canopy representing the couple's new home — open on all sides like Abraham's tent. The ceremony includes kiddushin (betrothal) and nisuin under the chuppah. Breaking the glass recalls the destruction of the Temple. It is a seudat mitzvah with dancing and simcha.

### chutz la'aretz

Chutz la'aretz means outside the Land of Israel. Halacha differs in some areas — second day of Yom Tov, certain agricultural laws, and some prayer texts. Israelis in chutz la'aretz often follow local practice while visiting; ask your rav for long stays.

### daven

Daven (davening) means to pray. You 'daven' the daily services (Shacharit, Mincha, Maariv). The word is Yiddish-derived Ashkenazi slang but widely understood. People often say "daven Shacharit" for a service and "daven" for praying in general.

### Daven / davening

pray; any prayer (blessings, Shema, Tehillim, personal tefillah, or the services Shacharit, Mincha, Maariv, etc.)

### Davening

praying; any prayer, often refers to the formal daily prayer services

### Dayeinu

Dayeinu ("it would have been enough") is the Seder song listing gifts from the Exodus — each step alone would have justified gratitude. Singing it trains children and adults to notice cumulative kindness from G-d. It appears after the Maggid narrative and before Hallel.

### Days of Awe

The Days of Awe (Aseret Yemei Teshuvah) span Rosh Hashana through Yom Kippur — a time of judgment, prayer, and teshuvah. Customs intensify: Selichot, charity, and asking forgiveness. Even Jews who are less observant year-round often attend services. The tone is solemn but hopeful — repentance can change a harsh decree.

### Diaspora

The Diaspora (Galut) means Jewish life outside the Land of Israel. For centuries communities developed local minhagim while staying loyal to Torah. Festival calendars often add an extra day of Yom Tov outside Israel. Aliyah to Israel is a mitzvah for many; Diaspora communities remain fully Jewish and vibrant.

### Edot HaMizrach

Edot HaMizrach (communities of the East) refers to Middle Eastern and North African Jewish kehillot — Iraqi, Syrian, Moroccan, Persian, Yemenite, and related traditions — usually praying Nusach Edot HaMizrach. Halacha generally follows Shulchan Aruch, but customs vary by kehilla. Not the same as Sephardi (Iberian diaspora / Nusach Sefard), though both differ from Ashkenaz.

### egg matzah

matzah made with eggs and/or fruit juice (matzah ashira); not lechem oni (poor man's bread) and cannot be used at the Seder. Ashkenazim do not eat it on Pesach except for sick, elderly, or young children (Rema OC 462:4); Sephardim legally permit it, though many contemporary Sephardic authorities recommend avoiding it unless necessary (such as for the elderly or sick)

### Elokei Neshama

Elokei Neshama thanks G-d in the morning for restoring the soul, which was pure and will be returned at death. It follows Birchot HaShachar in many siddurim. The prayer teaches that each day is a new loan of life — use it for mitzvot, not only errands.

### Elul

Elul is the month before Rosh Hashana — daily selichot (in Ashkenaz from before Rosh Hashana, Sephardim all month), blowing shofar after services, and cheshbon hanefesh. It is the runway for teshuvah. Increase charity, repair relationships, and learn one new halacha before the Days of Awe.

### Emunah

Emunah is faith that G-d exists, is one, and guides history and your personal life with purpose. It is not blind denial of difficulty — many psalms and stories show righteous people struggling — but a commitment that there is meaning even when we cannot see it. Strengthening emunah often comes through Torah study, mitzvot, and reflecting on past kindnesses you received.

### Eretz Yisrael

the Land of Israel

### Erev

the eve before; "Erev Shabbat" is Friday, "Erev Pesach" is the day before Pesach begins at night

### eruv

An eruv (especially eruv chatzerot or city eruv) is a halachic enclosure letting Jews carry within a defined area on Shabbat. It requires kosher boundaries and community upkeep. When the eruv is down, carrying keys and pushing strollers outdoors may be forbidden — shuls often text status Friday. It does not permit driving or phones.

### Eruv chatzerot

symbolic merging of courtyards allowing carrying on Shabbat

### Eruv tavshilin

Eruv tavshilin is a symbolic meal set aside on Erev Yom Tov when Shabbat follows immediately afterward. You take a baked food (challah or matzah) and a cooked food (often meat, fish, or an unpeeled hard-boiled egg), recite the blessing and declaration from your siddur or machzor in a language you understand, and thereby may continue food preparation on Yom Tov for Shabbat — which would otherwise be forbidden because you may not cook on one holy day for the next. Store the designated foods in a safe, labeled spot — if they are eaten before you finish Friday cooking for Shabbat, the eruv is void. It also reminds the home to prepare for Shabbat, not only for the festival.

### Eruv techumin

extends the Shabbat walking boundary in special cases

### etrog

citron, one of the Four Species on Sukkot

### Fast of Gedaliah

fast day after Rosh Hashana

### fleishig

meat status; dairy may not be eaten for a waiting period afterward, usually 6 hours

### Full Hallel

complete Hallel psalms on festivals/Chanukah

### gabbai

synagogue officer who coordinates aliyot and services

### Gam zu l'tovah

this too is for the good; trust that hardship can be for the greater good

### Gan Eden

Gan Eden (Garden of Eden) in Jewish thought often means the soul's reward after death — closeness to G-d, not a physical vacation. It pairs with Olam HaBa. The original Garden in Bereishit is the starting point of human story; tradition teaches souls return to a heavenly Gan Eden.

### gebrochts

Gebrochts ("broken") is matzah that has come into contact with liquid after baking — kneidlach (matzah balls) are the classic example. Some Chasidic communities observe the stringency of not eating gebrochts on Pesach. Others eat kneidlach freely. If you are a guest, follow the host's house rules; at home, follow your rav's psak consistently.

### gemach

A gemach (gemilut chasadim) is a free-loan fund or kindness society — lending items or money without interest, or channeling charity. On Purim, giving through a trustworthy gemach can fulfill matanot la'evyonim if the poor receive funds Purim day (and its the same day you're celebrating Purim). Gemachim also run baby gear, wedding dress, and medical equipment loans in many communities.

### Gemara

Talmudic commentary and discussion on the Mishnah

### gematria

numerical value of Hebrew letters

### gemilut chasadim

acts of loving-kindness

### geneivat da'at

Geneivat da'at ("stealing the mind") is deception — advertising falsely, hiding defects, or misleading about credentials. It applies to customers, employees, and friends. The Torah demands "honest scales" in spirit and letter. Trust lost through geneivat da'at is hard to rebuild.

### Geshem

Rain. Can refer to the prayer for rain inserted on Shemini Atzeret throughout the winter

### Gozez

shearing or cutting hair/nails melacha

### Grace After Meals

Birkat Hamazon after eating bread meals

### hadas

myrtle branch in the Four Species

### Hadlakat Nerot

Hadlakat Nerot is lighting candles before Shabbat or Yom Tov — welcoming holiness into the home. Women traditionally light; men if no woman is present. Ashkenazi and Chabad custom: daughters in the home may light their own candles with a bracha. Sephardic custom (Shulchan Arukh; Rav Ovadia Yosef): daughters under their parents' roof do not recite a bracha on separate candles if the mother already lit — that would be a bracha levatala. Ashkenazim often light then bless with eyes covered; Sephardim may bless first. Shabbat candles must be lit before sunset (not before Plag HaMincha). Weekday Erev Yom Tov candles are also lit before sunset. When Yom Tov begins on Motzei Shabbat, Yom Tov candles are lit only after tzeit, from a pre-existing flame. On other Yom Tov nights, before the holiday begins you may strike a new match; once Yom Tov has started, light only from a pre-existing flame.

### hafrashat challah

Hafrashat challah is separating a small portion of dough when baking a large amount of bread or similar flour-based foods, then burning or discarding in many customs. It recalls the Temple-era gift to the kohen (when they were in a state of ritual purity and able to eat it).

### Hagbah

Hagbah is lifting the open Torah scroll so the congregation can see the writing and recite "V'zot HaTorah" (and related verses). Ashkenazim (and most Chabad communities) perform hagbah after the reading, before gelilah (rolling and dressing the scroll). Sephardim (Nusach Edot HaMizrach) perform hagbah before the reading begins — the scroll is shown, then read. Follow your shul's order so you know when to stand and respond.

### Haggadah

The Haggadah ("telling") is the book guiding the Seder — questions, answers, plagues, Dayeinu, and Hallel. Its core is making the next generation feel they left Egypt. Many editions add commentaries; the mitzvah is participation and understanding, not speed. Mah Nishtanah is the children's opening question.

### Hakafot

dancing while circling around the synagogue with Torah scrolls on Simchat Torah

### hakarat hatov

recognizing the good; thanking someone for a kindness

### halacha

Halacha means "the way of walking" — the practical path of Jewish life drawn from Torah, Prophets, Writings, Mishnah, Talmud, and codes like the Shulchan Aruch. It covers prayer, Shabbat, diet, family life, business ethics, and festivals. Halacha is decided by qualified poskim; when you are unsure, the mitzvah is to ask your rav rather than guess.

### halachic chatzos

Chatzos is halachic midnight or midday — the midpoint of the night or day. Chatzos halayla matters for saying Modeh Ani, starting morning blessings, and some fast practices. Chatzos hayom is the midpoint of daylight. It moves with sunrise and sunset, not with 12:00 on the clock.

### halachic hour

A halachic hour (sha'ah zmanit) is one twelfth of the daylight period from dawn to dusk — so it is longer in summer and shorter in winter, unlike a 60-minute clock hour. Deadlines such as latest morning Shema, chametz sale times, and plag hamincha use these hours. Apps and Jewish calendars convert them for your location.

### halachot

laws or legal details (plural of halacha)

### Half Hallel

shortened Hallel psalms on Rosh Chodesh and after the first Yom Tov of Pesach

### Hallel

Hallel is a set of psalms (113–118, with variations) praising G-d for salvation. Full Hallel is recited on major festivals; Half Hallel (Partial Hallel) on Rosh Chodesh and the last 6 days of Pesach. Hallel is not recited on Rosh Hashana or Yom Kippur (Arachin 10b). It is said standing, with joy.

### Haman

Haman is the Persian official who plotted to destroy the Jews in Esther's time. Mordechai and Esther foiled the plan. When the Megillah reader says Haman's name, many boo, rattle graggers, or stamp — erasing his name symbolically. The mitzvah is hearing the text, not drowning out so much that words are missed.

### Hamapil

blessing entrusting the soul to G-d before sleep, part of bedtime Shema

### Hamotzi

Hamotzi is the blessing over bread — "Who brings forth bread from the earth" — beginning a meal that requires bentching. Wash netilat yadayim before bread meals. Cut bread for others after you bless if you are host. On Pesach, hamotzi is on matzah; on Shabbat, lechem mishneh — two whole loaves.

### HaNeiros halalu

paragraph sung after lighting Chanukah candles

### Havdalah

Havdalah separates holy time from weekday. On a normal Motzei Shabbat it uses wine, a multi-wick candle, and besamim (spices) — comforting the soul as the extra neshama yeteira departs. After Yom Tov there's usually no spices or candle. After Yom Kippur we do use a candle which was lit throughout Yom Kippur.

### Havdalah in Kiddush

Yaknehaz order when Shabbat leads into Yom Tov

### Hayom Yom

Hayom Yom ("Today is…") is a Chabad calendar of brief teachings — a saying, story, or law for each day of the year. It is often studied as part of daily Chitas after Chumash, Tehillim, and Tanya. Entries are short and practical, meant for busy schedules.

### hechsher

in kashrut, a certification that food was produced under reliable kosher supervision

### honoring your parents

Kibud Av V'Eim - mitzvah to honor/respect and care for parents

### Hoshana Raba

Hoshana Raba is the seventh day of Sukkot — the climax of Hoshanot with extra circuits around the bimah. The beating of aravot (willow branches) against the ground is classified as a Minhag Nevi'im — an ancient custom instituted by the final Prophets (Haggai, Zechariah, and Malachi). Because of its prophetic roots, it follows strict parameters that ordinary customs do not — notably striking the branches against the ground exactly five times to sweeten the divine judgments. See also Hoshanot.

### Hoshanot

Hoshanot are prayers of salvation recited while holding the Four Species and circling the synagogue sanctuary platform (Bimah) during Sukkot (except Shabbat). Hoshana Raba (seventh day) has extra circuits. It is the last push of repentance after Yom Kippur — joy and urgency together.

### Hotza'ah

Hotza'ah is carrying from private to public domain (or four amot in public) on Shabbat. Without an eruv, keys in a pin-on pouch, strollers in some areas, and wheeling objects outdoors may be issues. An eruv symbolically encloses a community — check if yours is up this week.

### k'dei achilat pras

K'dei achilat pras is the halachic time limit to consume a specific volume of food so it counts as one act of eating — about 4–7 minutes per many poskim. It applies to eating a kezayit of matzah at the Seder, maror, and eating enough bread or cake within that window to require an after-blessing (Birkat Hamazon or al hamichya). If you ate a kezayit of bread within k'dei achilat pras, Birkat Hamazon covers the whole meal.

### k'fi daato

K'fi daato means training a child according to what they can understand and do reliably — not demanding adult perfection. Chinuch grows mitzvot stepwise: brachot before full Amidah, short Shabbat attendance before long services. Pressure without da'at backfires; joy in mitzvot lasts longer.

### Kabbalah

Kabbalah is the Jewish mystical tradition exploring how G-d relates to creation, the soul, and mitzvot. Texts include the Zohar and later works like Tanya. Authentic Kabbalah is studied with Torah literacy and a teacher; it is not fortune-telling or magic.

### Kaddish

Kaddish sanctifies G-d's Name in Aramaic — exalting G-d publicly. Mourners recite Kaddish for eleven months for parents and thirty days for other close relatives. It requires a minyan. There are half-Kaddish, whole-Kaddish, and rabbinic Kaddish placements in the service. Saying Kaddish connects the living to the deceased's merit.

### kallah

Kallah means bride; kallah classes teach taharat hamishpacha, mitzvot of the home, and wedding laws before marriage. The kallah and chatan prepare for chuppah and building a Jewish family.

### kaparot

Kaparot is a pre-Yom Kippur custom swinging a chicken (or money substituted) over one's head while reciting verses — charity replaces the chicken in many communities today. It is custom, not Torah law. Handle with sensitivity; follow local rabbinic guidance and kashrut if using chickens.

### karmelit

rabbinic semi-public area with carrying restrictions

### karpas

Karpas is a vegetable (often parsley or potato) dipped in salt water at the Seder start — awakening curiosity before the meal. The salt water recalls tears of slavery. It is not the main maror mitzvah; it opens the night with a question. Prepare enough for every guest.

### Kashering

Kashering makes utensils fit for kosher or Pesach use. The core principle is kebolo kach polto — as a vessel absorbed forbidden taste, so it expels it. Items used in boiling water often need hag'alah (immersion in rolling boiling water); items used directly on fire may need libun (intense heat until they turn red hot). Plastic and glass have highly conflicting rulings between Ashkenazim and Sephardim — and it's less lenient on Pesach. Ask your rav. Ceramic cannot be kashered. Many families keep a separate Pesach set.

### Kashrut

Kashrut is the Torah system of permitted food, shechita, separating meat and dairy, and supervising production. It sanctifies eating — turning meals into service of G-d. Learning labels (OU, OK, etc.), hechsherim, and kitchen setup takes time; ask your rav when starting.

### kavannah

Kavannah is focused intention — knowing what mitzvah you are doing and for Whom. Prayer without kavannah is still prayer, but the sages urge preparation: understand words, stand with respect, remove distractions. Candle lighting and Amidah are especially suited to personal requests once the formal text is said.

### kehilla

Jewish community or congregation

### Keli

A keli is a vessel, utensil, or tool. It generally refers to any manufactured object or receptacle that holds, processes, or contains something. A keli is used for netilat yadayim — a cup, bottle, or washing cup (usually) with two handles.

### ketubah

marriage contract outlining husband's obligations

### Ketuvim

Ketuvim (Writings) is the third section of Tanach — Psalms (Tehillim), Proverbs, Esther, Ruth, Daniel, and others. It includes poetry, history, and wisdom literature. Tehillim is the most commonly recited book for comfort and praise.

### kezayit

A kezayit is the olive-sized portion in Jewish law used to measure how much food you must consume for mitzvot — eating matzah at the Seder, maror, bentching after bread, and after-blessings. Because ancient volumes are difficult to calculate precisely, rabbis have established modern equivalents. Halachically, a kezayit is tied to the size of an egg — usually calculated as either half or one-third of an egg's volume depending on the authority. Many major organizations (such as Star-K) equate a kezayit to about 1.2 to 1.3 fluid ounces (approx. 35–40 ml), which visually translates to the size of a golf ball or a roll of quarters. By weight, this generally ranges between 25 and 33 grams — the stringent opinion of the Chazon Ish is approx. 33 g; the more lenient opinion of Rabbi Chayim Na'eh is approx. 27 g. (Note: For porous foods like matzah, the required weight is typically lower — around 15–20 grams depending on your rav.) The exact size depends on which authority you follow; ask your rav or use a community chart for the specific food or mitzvah you are measuring. Eating less than a kezayit may mean the mitzvah was not fulfilled.

### Kiddush

Kiddush sanctifies Shabbat or Yom Tov over wine (or grape juice, or possibly bread/other drinks if in need; ask a rav).

### Kiddush b'Makom Seudah

Kiddush b'Makom Seudah means sanctifying Shabbat or Yom Tov, typically with blessings over wine, in the same place you eat a meal afterwards. You would not fulfill Kiddush if you recited it and left without eating there. On Shabbat night, the meal should include bread (hamotzi) per most poskim. On Yom Tov or daytime Kiddush, other foods may qualify in some cases; ask your rav. If you make Kiddush in one room, the meal should follow there and not in a different space.

### Kiddush Hashem

Kiddush Hashem means sanctifying G-d's Name — living and speaking in a way that makes observers respect Torah and G-d. Examples include returning lost objects, honest dealings, and gracious behavior in public as a visibly Jewish person. Its opposite, chilul Hashem, is among the most serious sins because it pushes people away from holiness.

### Kiddush Levana

Kiddush Levana (קִידּוּשׁ לְבָנָה — Sanctification of the Moon) is a monthly blessing said once each Hebrew month when the new moon is visible at night. It is not worship of the moon — it praises G-d for creation's cycles and Israel's renewal. The Talmud (Sanhedrin 42a) compares one who blesses the moon properly to greeting the Shechinah. Men are obligated in this time-bound positive mitzvah; women are exempt (like most time-bound mitzvot) and the widespread longstanding custom is that women do not recite Kiddush Levana at all — prominent authorities (Shelah, Magen Avraham O.C. 426:1) advise against it for kabbalistic reasons as well. Say it outdoors under the open sky when possible, standing, after nightfall. Ashkenazim and Chabad often wait at least three days after the new moon; most Sephardim wait seven (Shulchan Arukh O.C. 426:4); Moroccan and some North African kehillot may begin after three days (Peninei Halakha 05-01-18). Motzei Shabbat is a widespread preference when people are dressed up and the moon is visible — but if waiting risks a string of cloudy nights and missing the monthly window, say it on the first clear weeknight the mitzvah becomes possible. The window ends at the moment of the full moon (roughly 14.75 days into the month) — saying it on the night of the 15th may already be too late; check Sof Zman Kiddush Levana for your location. Also called Birkat HaLevanah.

### Kinot

elegies read on Tisha B'Av mourning the Temple

### kippah

skullcap worn as a sign of G-d's presence above

### kisui rosh

hair covering for married women in many communities

### kitniyot

Kitniyot are legumes, rice, corn, and similar foods that Ashkenazim traditionally avoid on Pesach though they are not Torah chametz — an Ashkenazi customary stringency, not a Torah prohibition. They are halachically permitted for Sephardim (though some communities have local customs avoiding specific items). The custom began to prevent confusion with chametz grains. Peanut oil, quinoa, and similar items follow widely different psak — follow your rav, not social media debates.

### Kitzur Shulchan Arukh

The Kitzur Shulchan Arukh is a condensed summary of daily halacha — prayer, Shabbat, kashrut, festivals, and family life — written by Rabbi Shlomo Ganzfried. It is a practical first halacha book for beginners and a quick reference for experienced Jews.

### klaf

The klaf is the special kosher parchment on which scribes write Torah scrolls, mezuzots, tefillin scrolls, etc.

### kneidlach

matzah balls; those who observe the stringency of not eating gebrochts do not eat kneidlach on Pesach until the 8th day in the diaspora

### kohen

priest descended from Aaron. They perform the service in the Temple when it's standing.

### Kol Chamira

Kol Chamira is an Aramaic declaration nullifying chametz still in your possession. The night version (after bedikat chametz) nullifies only chametz you have not seen and do not know about — you may still legally own chametz for breakfast. The morning version (after biur on Erev Pesach) is structurally different and nullifies ALL chametz, whether seen or unseen, destroyed or not.

### Kol Nidrei

Kol Nidrei opens Yom Kippur eve — annulling vows so the day can proceed with a clean slate regarding promises to G-d. The haunting melody is famous, but the legal text is serious. It does not cancel interpersonal debts or promises to others — only certain vows to G-d per specific formulas. Arrive early; the shul fills quickly.

### Korbanot

Korbanot are the Temple offerings — animal sacrifices, incense, and libations — brought by the Jewish people in the Beit HaMikdash. Since the Temple's destruction, we cannot offer them; prayer recalls and substitutes for those offerings until the Temple is rebuilt.

### Korech

Korech is the "Hillel sandwich" at the Seder — matzah and maror together, recalling Temple-era practice. You eat a kezayit of matzah and maror combined after dipping maror in charoset. It is one of the Seder's core tastes: slavery's bitterness held with redemption's bread. Follow your Haggadah's order after the main matzah mitzvah.

### kosher

Kosher means fit for use per halacha — especially food, but also valid scrolls and acceptable conduct (e.g. kosher money for mitzvot). Kosher is not a health label; it is spiritual fitness. When in doubt on a product, call the hechsher company or your rav.

### Koteiv

Koteiv is writing two meaningful letters on Shabbat. Typing, texting, and permanent markers violate this rabbinically and often biblically. Avoid Scrabble with locking tiles, signing checks, and filling forms. Children should have Shabbat-appropriate activities prepared.

### Krias Shema

recitation of the Shema and its blessings

### l'chatchila

L'chatchila means the ideal way to perform a mitzvah from the outset — what you should plan to do. Bedieved is after the fact, when something went wrong and halacha may offer a corrective or leniency. Learning both terms helps you read halachic guides: "l'chatchila use a cup; bedieved if you forgot, some allow…"

### Lag BaOmer

33rd day of the Omer; mourning eased in many communities; Chabad keeps haircut restrictions until Shavuot except for a 3 year old boy's first haircut

### lashon hara

Lashon hara is speaking negatively about another Jew even when the information is true and even in casual conversation, unless there's a real useful purpose like protecting someone from a harmful situation. The Torah compares it to murder because it destroys reputations, friendships, and community trust. The Chofetz Chaim systematized its laws: many situations permit speech for protection or constructive need, but for entertainment or no reason it's never permitted. Guarding speech is one of the fastest ways to grow in holiness.

### lechem mishneh

Lechem mishneh is two whole loaves at Shabbat and Yom Tov meals — remembering double portion of manna before Shabbat in the desert. Cover the loaves, bless hamotzi, break bread for others. On Pesach, two whole matzot serve the role. The loaves should be whole and worthy of hamotzi.

### leining

Leining is chanting the Torah portion from the scroll with precise trop (cantillation). The baal koreh trains for months. Congregants follow in a Chumash. Hearing every word on Shabbat morning fulfills the communal mitzvah of public Torah reading.

### leishev baSukkah

Leishev baSukkah is the blessing "to dwell in the sukkah" — said when eating bread meals in the sukkah on Sukkot. Dwelling means spending meaningful time there, not only a quick snack. Rain may exempt you per halacha. Decorate the sukkah and make children feel at home in it — the booth recalls clouds of glory in the desert.

### levi

Levi is the tribe serving in the Temple; today leviim are often called to the Torah after kohanim. Many carry the family name Levi or related traditions. Kohanim and leviim have other halachot (priestly blessing, tumah). If unsure of your status, ask family and your rav.

### Losh

kneading melacha

### loving your fellow Jew

central Torah principle taught by Rabbi Akiva

### lulav

palm branch waved with the etrog, willows, and myrtle branches on Sukkot

### Maariv

Maariv is the evening service after nightfall — Shema, Amidah, and on Motzei Shabbat Havdalah or festival inserts. Saturday night Maariv may include Vatodi'enu when Yom Tov begins. Weekday Maariv is when many working Jews connect with community after the day. Consistency at Maariv builds a rhythm of ending the day with G-d.

### maaser kesafim

Maaser kesafim is setting aside about ten percent of net income for tzedakah after expenses. It trains that income is entrusted, not owned absolutely. Priorities: local poor, Torah learning, Israel. If you cannot give ten percent without hardship, give what you can and ask your rav.

### Machatzit HaShekel

Machatzit HaShekel recalls the half-shekel each Jew gave for Temple upkeep in Adar. Today many give charity before Purim — often three coins half of a local unit. It is a widespread custom, not one of the four Purim mitzvot, but it connects the community to unified redemption. Give through your shul or reputable charity.

### machloket

Machloket is respectful disagreement between Torah authorities — often recorded in the Talmud. Not every machloket is settled; both sides may be valid. The mitzvah is to follow your rav's psak in practice while honoring that another community may follow a different legitimate view. Machloket l'shem shamayim is for truth; argument for ego is destructive.

### machzor

A machzor is a prayer book for the Jewish festivals and High Holidays — Rosh Hashana, Yom Kippur, Pesach, Sukkot, and Shavuot. It includes texts and prayers not found in the daily siddur. For Rosh Hashana and Yom Kippur, a machzor is required to perform the proper prayers.

### Mah Nishtanah

Mah Nishtanah ("Why is this night different?") is the Four Questions — usually asked by the youngest child at the Seder. The Haggadah answers each question with Torah, story, and mitzvot. The point is engagement: children (and adults) should feel the Exodus personally, not only hear a lecture.

### Makeh B'patish

final finishing touch melacha

### maleh lugmov

Maleh lugmov is drinking a cheekful from the Kiddush cup (approx 2 fl oz) after the blessing — from at least a total of a revi'it of wine or grape juice in the cup. Grape juice counts like wine for many poskim.

### Maoz Tzur

"Rock of Ages," Chanukah hymn sung after lighting

### maror

Maror is bitter herb at the Seder — horseradish root or romaine — reminding the bitterness of slavery. A kezayit is required at the Seder. Charoset's sweetness is dipped with maror in Korech (Hillel's sandwich). Prepare enough fresh maror; grated horseradish loses strength quickly.

### Mashiach

Mashiach (Moshiach) is the righteous anointed king from David's line who will rebuild the Temple, gather the exiles, and bring universal knowledge of G-d. Belief in his coming is a foundation of Jewish faith. We pray for redemption daily.

### Matan Torah

receiving the Torah at Mount Sinai, celebrated on Shavuot

### matana al menat lehachzir

Matana al menat lehachzir (מַתָּנָה עַל מְנָת לְהַחְזִיר) is a "gift on condition it is returned." The recipient legally owns the item for the moment it is in their hands, then returns it afterward. On the first day of Sukkot, if you do not own a lulav and etrog, someone may gift you their set conditionally so you fulfill the Torah obligation of ownership for the mitzvah, then you return it. This is not used for mechirat chametz — selling chametz requires an absolute sale.

### matanot la'evyonim

Matanot la'evyonim are gifts sent to the poor on Purim day so they can rejoice too: at least one gift to each of two different poor people (minimum of two recipients total). Money is common; food or vouchers work if they can use them that day. Unlike mishloach manot, this mitzvah focuses on need, not friendship. Many shuls collect on Purim morning; verify distribution happens on Purim itself (and on the same day you are celebrating it, i.e. on Shushan Purim).

### matzah

Matzah is unleavened bread eaten all Pesach and central at the Seder — at least a kezayit when hamotzi is said, again for the afikoman. It recalls haste leaving Egypt. Shmurah matzah is guarded from moisture; many use it at the Seder. Store carefully — broken matzah still counts if kezayit-sized pieces remain.

### Mavir

Mavir (מַבְעִיר) is creating a new flame — striking a match, flipping an electronic switch, or starting a fire from scratch. It is strictly forbidden on both Shabbat and Yom Tov. On Yom Tov, however, you may transfer an existing flame (from a candle lit before Yom Tov) to cook, light Yom Tov candles, or for festival needs (ochel nefesh). That distinction — new fire vs. transferring fire — is central to Yom Tov laws.

### mayim achronim

Mayim achronim is washing the fingertips after a bread meal and before bentching — a reminder that we eat as servants before G-d. Not every community emphasizes it today, but many siddurim and bentchers include it. It is separate from netilat yadayim before the meal.

### Mechabeh

extinguishing fire melacha

### mechirat chametz

Mechirat chametz is selling chametz you want to keep to a non-Jew through your rabbi before Pesach so it is not yours during the holiday. Authorize the sale well before Erev Pesach — most rabbis stop accepting forms by the night before, even though ownership transfers on Erev Pesach morning. It must be an absolute sale, not a conditional gift (matana al menat lehachzir). Chametz sold should be stored away. When including year-round dishes, rabbis typically sell only the absorbed chametz flavor in the vessels — not the physical dishes themselves — to avoid requiring tevilat kelim after Pesach. Shortly after Pesach, ownership reverts per the contract your rabbi uses.

### mechitza

A mechitza divides men and women's seating in shul during prayer so focus stays on tefillah per traditional practice. Heights and designs vary by community.

### Megillah

Megillah means scroll; on Purim it is Esther's book read twice — night and day. Both readings are obligatory; the daytime reading is the primary fulfillment (mitzvat ha'yom), while the night reading was instituted later. Hearing every word is the mitzvah; noise at Haman's name is custom. Shehecheyanu: Ashkenazim recite before the first evening and first daytime readings (Rema O.C. 692:1); Sephardim recite only at night — the night blessing covers the day (Shulchan Arukh; Yalkut Yosef). If you miss words, ask whether you must hear that section again.

### Megillah reading

hearing the Book of Esther read on Purim

### Melaben

laundering melacha

### Melacha

Melacha is transformative labor forbidden on Shabbat — including cooking. On Yom Tov most of the 39 melachot remain forbidden (writing, building, kindling a new fire, etc.), but the Torah permits ochel nefesh — preparing food for that festival day, including cooking transferred from a pre-existing flame (Exodus 12:16). You may not cook on Yom Tov for the next day without eruv tavshilin when Shabbat follows. When unsure, ask your rav before acting.

### melachot

the 39 categories of transformative labor forbidden on Shabbat

### Melave Malka

escorting the Shabbat Queen — Motzei Shabbat meal (מְלַוֶּה מַלְכָּה) through dawn Sunday

### melicha

salting meat to remove blood after shechita

### menorah

Chanukah candelabra; candles are lit each night

### mesorah

tradition faithfully transmitted generation to generation

### Mezonot

Mezonot (borei minei mezonot) is the blessing on grain foods that are not bread — cake, crackers, pasta, cereal. It is shorter than hamotzi but still thanks G-d for grain sustenance.

### mezuzah

A mezuzah is the klaf — handwritten parchment with Shema and V'ahavta passages — affixed to doorposts of rooms used for dwelling. The case is protection for the scroll, not the mitzvah itself. Check scrolls by a sofer every few years. Touching the case and kissing the mezuzah when entering reminds you that G-d watches over the home.

### midrash

Midrash is rabbinic teaching that explores Torah stories, ethics, and values — often answering questions the plain text leaves open. Midrash Rabbah and similar collections are not the same as halacha codes; they inspire and teach. Distinguish midrash from pshat (straightforward meaning) when learning Chumash.

### mikveh

A mikveh is a kosher ritual pool of natural water meeting halachic size and flow rules. It is used for taharat hamishpacha, conversion, tevilat keilim, and some utensil and status changes. Immersion is a powerful transition — the person or vessel emerges spiritually renewed. Community mikvaot are discreet and staffed by trained attendants.

### milchig

dairy status

### Mincha

Mincha is the afternoon service, ideally before sunset. On weekdays it is short; on Shabbat and Yom Tov it includes Torah reading (often) and Musaf on festivals. Friday Mincha is special — it enters Shabbat. Missing Mincha requires tashlumin at Maariv when possible. Working people often pray Mincha at shul or a quiet office corner.

### Minhag

A minhag is a binding community or family custom rooted in Jewish life — not merely "what people happen to do." Minhag can shape prayer text (nusach), mourning practices, Pesach stringencies, and holiday joy. When a minhag is established with rabbinic approval in a community, it often carries nearly the weight of law for that community. If you are new or between communities, ask your rav which minhagim to follow.

### minyan

A minyan is ten Jewish men above bar mitzvah age (13) forming a quorum for public prayer. Certain prayers — including Kaddish, Barchu, and repetition of the Amidah — require a minyan. The Talmud stresses that communal prayer is especially useful for getting prayers accepted by Heaven; joining a minyan is considered the ideal way of prayer.

### misheyakir

Misheyakir (מִשֶּׁיַּכִּיר) is when there is enough natural ambient light to recognize a casual acquaintance from about four amot (~6 feet). The earliest time to recite the bracha on tallit and tefillin is misheyakir — often roughly 35–50 minutes after alot hashachar (varies by season and location). Before misheyakir you do not fulfill the mitzvah, even after dawn. Mishnah Berurah recommends waiting until misheyakir, then putting on tallit and tefillin and reciting Krias Shema and the Amidah; many shuls daven until Yishtabach without them, then wait for misheyakir. The Gemara (Berachot 14b) compares Shema without tallit and tefillin to false testimony — avoid if you can wait. If you must leave very early: Igros Moshe (O.C. 4:6) permits wearing them after alot hashachar without a bracha, davening, then moving them slightly and reciting the brachos after misheyakir. Preference: (1) tallit and tefillin after misheyakir, Amidah at sunrise; (2) after misheyakir, Amidah before sunrise; (3) only if necessary — after alot hashachar without bracha, brachos after misheyakir.

### Mishloach Manot

Mishloach manot are ready-to-eat food gifts sent to at least one Jewish friend on Purim day — minimally two different foods in one package.

### Mishnah

The Mishnah is the first major written compilation of the Oral Torah, edited around 200 CE. It organizes halacha by topic (Shabbat, blessings, damages, and so on) in concise Hebrew. The Gemara and Talmud Yerushalmi explain and debate the Mishnah. Beginners often start Mishnah before full Talmud.

### Mishnah Berurah

Mishnah Berurah is Rabbi Yisrael Meir Kagan's detailed commentary on Orach Chaim — daily halacha for prayer, Shabbat, and festivals. It explains Shulchan Aruch with practical rulings. Many Ashkenazim rely on it for home practice. It pairs with the Chofetz Chaim's other works on speech and mitzvot.

### mitzvah

At its core, a mitzvah is a command from Hashem that acts as a direct connection point between you and Him. The word itself is related to a term for "binding together," meaning every time you do a mitzvah, you are plugging yourself directly into the Divine. It covers the big stuff like Torah obligations and formal prayer, but it also includes the everyday moments—like making a blessing over a glass of wine or just having a candid, heart-to-heart talk with Hashem in your own words. Ultimately, a mitzvah is about taking an ordinary, physical moment and turning it into a real relationship with your Creator by following what He wants you to do.

### Modah

feminine form of Modeh (grateful) in Modeh Ani

### Modeh Ani

Modeh Ani is the first words many Jews say upon waking from primary nighttime sleep: it gives thanks to the living and eternal King for returning the soul with compassion. Say it in bed before getting up, without washing first. It is typically not recited after a daytime nap — then wash hands (negel vasser). You may repeat the words anytime as personal gratitude, but that is not the official morning prayer. Men say Modeh; women often say Modah.

### Motzei Shabbat

Motzei Shabbat is Saturday night after Shabbat ends.

### Muktzeh

Muktzeh means "set aside" — objects not used on Shabbat because they are not needed for Shabbat itself (tools, money, phones). The categories are detailed: some items may not be moved at all; others may be moved for a Shabbat need or to use the place they occupy. Learning basic muktzeh prevents accidental violations when tidying or handling objects on Shabbat.

### Musaf

Musaf is the "additional" Amidah remembering the extra Temple offerings (musaf offerings) brought on Shabbat, Rosh Chodesh, and Yom Tov. Its central blessing describes what was offered and prays for the Temple's restoration. Without the Temple, prayer replaces sacrifice — Musaf is how we still connect to that service.

### Mussar

Mussar is the Jewish discipline of ethical and character development — refining anger, arrogance, laziness, and speech through study and practice. Classic works include Mesillat Yesharim and Ale Shur. It complements halacha (what to do) with work on who you are becoming.

### Nachum Ish Gamzu

sage known for finding good in every situation

### Ne'ilah

Ne'ilah ("closing") is the final prayer service of Yom Kippur as the gates of repentance close. The ark often stays open; the congregation pleads intensely. Many have the custom to say Shema Yisrael together at the end. After Ne'ilah and nightfall (tzeit), one prays Maariv, makes Havdalah, and breaks the fast. It is the spiritual peak of the Ten Days of Teshuvah.

### nefesh

Nefesh is the 'animal' part of the soul closest to physical life — appetite, hunger, physical desires, etc. Torah and mitzvot attempt to refine the nefesh toward holiness instead of animal instincts.

### Nerot

candles, often referring especially to Shabbat and Yom Tov lights

### neshama

Neshama is the soul — the divine breath that makes you alive. Modeh Ani thanks G-d for returning it after sleep. Yizkor and Kaddish relate to the soul's journey after death. Jewish thought distinguishes levels of soul (nefesh, ruach, neshama) in mysticism; practically, nurturing the soul means Torah, mitzvot, and character.

### neshama yeteira

Neshama yeteira ("extra soul") is the tradition that Shabbat brings an added measure of spiritual capacity — more patience, joy, and ability to connect to Torah. It departs at Havdalah, which is why some feel a post-Shabbat dip. Honoring Shabbat meals, rest, and learning helps this soul express itself; rushing and stress diminish it.

### netilat yadayim

Netilat yadayim is ritual handwashing with a cup (keli). Morning washing removes ruach ra'ah after sleep: pour water three times on each hand in alternating sequence (right, left, right, left, right, left). Before bread, pour two or three times consecutively on each hand to purify for eating.

### Nevi'im

Nevi'im (Prophets) is the middle section of Tanach — Joshua, Kings, Isaiah, Jeremiah, and the other prophetic books. They call Israel to justice and loyalty to Hashem. Reading them is Torah study.

### niddah

Niddah is the state of ritual separation during and after menstruation until mikveh immersion. Husband and wife avoid physical intimacy and certain affectionate contact. Laws of stains and cycles are complex — a kallah teacher or rav supports real-life questions. Niddah is not a punishment; it is rhythm and holiness in marriage.

### Nine Days

The Nine Days intensify mourning before Tisha B'Av. Ashkenazim restrict meat, wine, laundry, and bathing from 1 Av; Sephardim and Edot HaMizrach often restrict from the week of Tisha B'Av (some communities from Rosh Chodesh Av). Tisha B'Av is a full fast with kinot; Ashkenazim continue some restrictions until chatzos on 10 Av.

### nusach

Nusach is the prayer "style" of a community: which words are said, in what order, and often which melodies are used. Common examples include Nusach Ashkenaz, Nusach Sefard (Sephardi / Iberian diaspora), Nusach Edot HaMizrach (Middle Eastern and North African kehillot), Nusach Sefard on a Chasidic siddur (Ashkenazi-Chasidic — not Sephardi), and Nusach Ari (Chabad).

### Nusach Ari

Nusach Ari is the prayer rite associated with Rabbi Isaac Luria (the Ari) and used by Chabad and some other communities. It blends Ashkenazi and Sephardi elements. Chabad siddurim such as Tehillat Hashem print this nusach. If you daven Nusach Ari, use that siddur consistently for festival inserts.

### nusach Ashkenaz

Ashkenazi prayer wording

### nusach Sefard

Ashkenazi-Chasidic prayer rite (not the same as Sephardic Nusach Edot HaMizrach)

### ochel nefesh

Ochel nefesh ("food for the soul") is the Torah-based allowance to perform certain food preparation tasks on Yom Tov (festival days) for consumption on that same day — cooking and baking that would otherwise be forbidden melacha. It does not apply on Chol HaMoed, where food preparation is permitted. It does not permit unnecessary cooking. To prepare for Shabbat that falls immediately after Yom Tov, you need an eruv tavshilin.

### Olam HaBa

Olam HaBa is the World to Come — the eternal reality of closeness to G-d after this life (Heaven). Mitzvot, Torah study, and acts of kindness build merits that the sages describe as unfathomable treasures in that world.

### oleh

The oleh (one who goes up) recites blessings before and after the Torah reading at an aliyah. Immigrating to Israel is also called aliyah. Honoring aliyot respects kohanim, leviim, yahrzeits, and life-cycle events. Prepare the Hebrew blessings or follow along in transliteration.

### ona'ah

Overcharging or underpaying when the other party does not know the fair value. A variation of one-sixth or more above or below fair market price triggers the Torah laws of financial fraud (Bava Metzia 49b), requiring the overcharged amount to be fully refunded. Honest business is a mitzvah; "everyone does it" does not excuse geneivat da'at or ona'ah.

### ona'at devarim

Ona'at devarim is verbal wronging — insults, public embarrassment, or pressuring someone to sell. Even "friendly" teasing can be forbidden if it hurts. The Talmud lists it among sins that may not be forgiven until the victim is appeased. Guarding speech includes not only lashon hara but ona'at devarim.

### Oneg Shabbat

Oneg Shabbat is delighting in Shabbat — good food, rest, Torah study, singing, and time with family or guests. It is a positive mitzvah, not only avoiding melacha. The Talmud criticizes those who fast or deprive themselves on Shabbat without need. Planning meals and atmosphere before Shabbat helps oneg happen without last-minute stress.

### pareve

neutral foods (neither meat nor dairy) such as fish, eggs, and produce

### parsha

A parsha (parashah) is the weekly Torah portion read in synagogue on Shabbat morning. The Torah is divided into 54 portions so the whole five books are completed each year (with combined portions sometimes depending on the calendar). Shnayim Mikra means studying the same parsha during the week before it is read publicly. The name appears as "Parshat Bereishit," "Parshat Noach," and so on.

### Parshah

weekly Torah portion read in synagogue

### patur

Patur means exempt from a particular mitzvah in a given situation — for example, someone ill on Yom Kippur from fasting, or a child before bar mitzvah. Patur is not "off the hook" morally; it means the obligation does not apply to you now. A different mitzvah or leniency may apply instead.

### Peninei Halakha

Peninei Halakha is Rabbi Eliezer Melamed's modern Hebrew halacha series (available in English online), organized by topic — Shabbat, festivals, prayer, family, and Israel. It is clear, sourced, and widely used for practical questions.

### Pesach

Pesach commemorates the Exodus — matzah, maror, Seder, and no chametz. It is the festival of freedom and faith. Preparation dominates erev Pesach: bedikat, biur, kashering. The spiritual goal is feeling you left Egypt personally, not only historical memory.

### Pesachdik

kosher for Passover standards

### Pesukei d'Zimra

Pesukei d'Zimra ("verses of song") open Shacharit — psalms and praises warming the heart before Shema. Baruch She'amar and Yishtabach frame the section. Skipping it to "save time" loses the service's emotional arc. On Shabbat, Nishmat and expanded psalms add length and joy.

### Pikuach nefesh

Pikuach nefesh (saving life) overrides Shabbat and almost all mitzvot. Call emergency services, drive to hospital, and carry medicine when life is at risk. Doctors and nurses have detailed halacha for their shifts — but a layperson should never hesitate to save life. After the danger passes, ask your rav about what you did.

### Pirkei Avot

Pirkei Avot (Ethics of the Fathers) is a tractate of the Mishnah with wisdom sayings about character, community, and love of Torah — often studied on Shabbat afternoon between Pesach and Shavuot. It is not a law manual but a moral compass. Familiar lines include "The world stands on Torah, service, and acts of kindness."

### pirsumei nisa

Pirsumei nisa means publicizing the miracle — placing Chanukah menorah where passersby see the lights. The miracle of the oil is proclaimed to the street. Inside homes, many light by the window. Do not use the mitzvah candles for reading — use the shamash.

### pitom

This is the knobby, stem-like protrusion that extends from the top of the etrog (opposite the branch). The shoshanta rests on top of it.

### Plag HaMincha

Plag hamincha is one and a quarter halachic hours before nightfall — used for early Mincha, early Shabbat entry in some communities, and certain Pesach and Chanukah times. It is also the earliest time you may light Shabbat candles; lighting before plag invalidates the mitzvah and makes the blessing unnecessary. It is not identical to sunset; check your calendar.

### posek

A posek is a rabbi qualified to decide difficult halachic questions and write authoritative rulings. Your rav may consult poskim on novel cases. The term differs from a maggid or teacher who inspires but does not pasken. Major poskim include figures behind the Mishnah Berurah, Igrot Moshe, and contemporary halachic works your community follows.

### psak

Psak is a halachic ruling — the answer a qualified posek gives for your real case, not a theoretical debate. Judaism has legitimate range between authorities; your job is to follow your rav's psak consistently rather than shop around for leniencies.

### Psalms

Tehillim

### pshat

Pshat is the plain, straightforward meaning of a Torah verse — what the text says on its surface before deeper layers. Rashi on Chumash mainly explains pshat. Learning pshat is the foundation before midrash or Kabbalah.

### Purim

Purim celebrates salvation in Shushan — Megillah, matanot la'evyonim, mishloach manot, and seudah. Costumes and joy are mitzvah, not frivolity alone. Hearing every word of the Megillah is essential. Drunkenness per "until you cannot tell" is bounded by safety and halacha — ask your rav.

### Purim day

14 Adar (15 in walled cities); Megillah, gifts to friends/poor, and feast

### Purim Meshulash

Purim Meshulash is the rare Jerusalem schedule when Shushan Purim (15 Adar) falls on Shabbat. Megillah and matanot l'evyonim move to Friday; mishloach manot and the seudah move to Sunday. Shabbat carries communal Purim obligations — special Torah reading (Vayavo Amalek), Haftarah, and Al HaNissim in Amidah and bentching (not on Friday or Sunday).

### Purim seudah

The Purim seudah is the festive afternoon meal on Purim day — part of the day's joy alongside Megillah, mishloach manot, and matanot la'evyonim. It requires bread (hamotzi), not matzah. Many begin the meal before sunset and continue into the evening. It is a seudat mitzvah of celebration, not a fast day.

### Rabbi

Rabbi is the English word for a Jewish spiritual leader with rabbinical ordination (semicha). From Hebrew Ribbi ("my master" or "my teacher").

### Rambam

Rambam is Rabbi Moses Maimonides (1138–1204) — physician, philosopher, and halachic giant. His Mishneh Torah codifies halacha in clear Hebrew; his Thirteen Principles summarize Jewish faith. Many beginners meet him through daily halacha summaries or his laws of teshuvah, charity, and Torah study.

### Rashi

Rashi is Rabbi Shlomo Yitzhaki's eleventh-century commentary — the first stop for Chumash and Talmud learners. He explains pshat (plain meaning) in concise Hebrew (or Aramaic in Talmud). Shnayim Mikra often uses Rashi as the "third" reading. His commentary shaped Jewish education for centuries.

### Rav

Rav (Hebrew for "great" or "master") is a title for an experienced Torah scholar who rules on halacha — especially in Orthodox and traditional communities. A rav is a decisor (posek) for real-life questions: kashrut, Shabbat, niddah, business, and festivals. Not every rabbi is knowledgeable enough to be considered a rav.

### rechilut

Rechilut is carrying tales that create hatred — even if true. Example: telling Reuven what Shimon said about him to stir conflict. Lashon hara speaks badly about someone; rechilut spreads words between parties. The Chofetz Chaim devotes extensive chapters to permitted speech for protection or constructive need; casual "did you hear" stories are almost never allowed.

### refuah shleimah

complete healing; said when praying for someone ill

### Retzei

Retzei asks G-d to be pleased with our rest and to restore the Temple and Davidic kingdom — inserted in the third blessing of bentching on Shabbat and Yom Tov. Forgetting Retzei at the first two Shabbat meals usually requires repeating bentching. However, if you forget it at Seudah Shlishit (the third meal), you do not repeat (SA OC 188:8). Ask your rav if unsure.

### revi'it

A revi'it (רביעית) is a standard unit of liquid volume in halacha, defined as one-quarter of a log — commonly translated as the volume of an egg and a half. It is the minimum volume required for ritual mitzvot involving liquids, such as Kiddush. Because physical volumes have shifted and been debated, several contemporary measurements exist depending on the posek followed: Rav Chaim Na'eh: ~86 ml (approx. 2.92 fl oz) — widely used as a lenient baseline, particularly in Sephardic tradition and for rabbinic obligations like netilat yadayim. Rabbi Moshe Feinstein: ~130–150 ml (approx. 4.4–5.1 fl oz) — many recommend this for Torah-level obligations like Shabbat Kiddush. The Chazon Ish: ~150 ml (approx. 5.1 fl oz) — stringent standard in many Ashkenazi and Hasidic communities for Torah-level mitzvot. Besides Kiddush, a revi'it is also required for Havdalah (minimum wine drunk from the cup), netilat yadayim (minimum water poured — may rely on the smaller measure), and the four cups at the Pesach Seder. After Kiddush, many require drinking melo lugmov or the majority of the cup; ask your rav which measure to use.

### Rosh Chodesh

Rosh Chodesh begins the Hebrew month — semi-holiday with Hallel (half), Musaf, Yaaleh V'yavo, and often reduced work for women per custom. Remove tefillin before Musaf — wearing tefillin during Rosh Chodesh Musaf is forbidden. In ancient times the new moon was proclaimed from testimony. Today the calendar is fixed. It is a monthly reset — plan Torah goals and charity.

### Rosh Hashana

Rosh Hashana is the Jewish New Year and Day of Judgment — shofar, festive meals, and solemn prayer. It begins the Ten Days of Teshuvah leading to Yom Kippur. Customs include apples in honey, new fruit, and tashlich. Work is forbidden like Yom Tov.

### ruach

wind or spirit; middle soul level in some teachings

### Ruach Ra'ah

Ruach Ra'ah ("evil spirit") is a rabbinic term for the impurity resting on hands after sleep. Morning netilat yadayim removes it by washing hands alternating right and left 3 times each.

### schach

Schach is the plant covering on the sukkah roof — bamboo, branches, or reeds — thick enough that shade exceeds sun, yet sparse enough to see stars. It must be from material that grew from the ground and is not attached to the ground while on the sukkah. Finished manufactured items (furniture slats, crate boards, processed ropes) are invalid because they are susceptible to ritual impurity (kabalat tumah). Invalid schach invalidates the mitzvah — build with guidance the first time.

### Seder

The Seder is the ordered Pesach night meal — four cups of wine, matzah, maror, and reading the Haggadah so children ask questions. Recline to the left for the four cups, matzah, korech, and afikoman — not during maror or chazeret. It reenacts leaving Egypt. Timing matters: matzah afikoman before halachic midnight per many poskim. Prepare the plate, Hagaddah, and guests' needs before Yom Tov begins so the night runs with joy, not panic.

### Sefardim

Jews following Sephardi rites and customs

### Sefer Torah

A Sefer Torah is a Torah scroll — the entire five books of the Torah handwritten by a sofer on kosher parchment, rolled on two wooden rollers (atzei chayim). It is read in synagogue with special honor; you usually do not study daily from the scroll the way you use a Chumash. Missing or extra letters can invalidate a scroll, so scribes train for years.

### Sefirah

counting period between 2nd day of Pesach and Shavuot; mourning customs and when they end vary by minhag (Ashkenaz, Sephard, Chabad, etc.)

### Sefirat HaOmer

Sefirat HaOmer counts forty-nine days from the second night of Pesach until Shavuot — linking freedom to receiving the Torah. Each night, preferably after nightfall, you bless and announce the day and week count. Missing a full day may affect whether you can say the blessing the remaining nights — ask your rav. During the Omer, many communities observe mourning customs (no music, weddings, haircuts) until Lag BaOmer or Shavuot, depending on minhag to commemorate the plague that killed many of Rabbi Akiva's students during this time.

### Selichot

Selichot are penitential prayers recited before Rosh Hashana (Ashkenazim often from the Saturday night before, Sephardim from Elul). They include poetic pleas and the Thirteen Attributes of Mercy. Waking early or staying late for Selichot sets a tone of seriousness before the Days of Awe. Nusach and schedule vary — check your shul.

### Sephardi

Sephardi Jews trace roots to the Iberian diaspora and related Mediterranean communities. Prayer is usually Nusach Sefard (Bet Yosef). Halacha generally follows the Shulchan Aruch and poskim such as Rav Ovadia Yosef zt"l. Rice and kitniyot are halachically permitted on Pesach where Ashkenazim follow the kitniyot custom. Do not confuse with "Nusach Sefard" on a Chasidic siddur (an Ashkenazi rite) or with Edot HaMizrach (Middle Eastern / North African nusach).

### Seudah

festive meal (e.g. Purim afternoon feast)

### seudah shlishit

Seudah shlishit is the third Shabbat meal, usually late afternoon before sunset. It is lighter than the first two meals — often fish, salad, and songs (zemiroth). Some insert special Torah ideas. Do not skip it — three meals are a key Shabbat mitzvah. Bentching includes Retzei and may extend toward Havdalah time.

### seudat mitzvah

festive meal tied to a mitzvah (e.g. brit, siyum, wedding, etc.)

### Shabbat

Shabbat is the seventh day — a gift of rest and holiness from Friday sunset until halachic nightfall on Saturday night (tzeit), per your community's zmanim. The Torah forbids melacha — transformative work in thirty-nine categories. We light candles, hear Kiddush, eat three meals, study Torah, pray, and refrain from commerce and devices per your rav. Shabbat is called a taste of the World to Come; keeping it faithfully shapes the entire week.

### Shabbat candles

candles lit before sunset to welcome Shabbat

### Shabbat Shalom

peaceful Sabbath greeting

### Shabbos

Shabbat

### Shacharit

Shacharit is the morning service — Pesukei d'Zimra, Shema and its blessings, the Amidah, and on Shabbat and Yom Tov Torah reading and Musaf. The Shema must be recited before the end of the third halachic hour; the Amidah ideally before the end of the fourth. Tefillin are worn on weekdays. Daily Shacharit anchors the day in prayer before the world's noise.

### Shaddai

Shaddai (one of Hashem's names you shouldn't pronounce in vain) on the mezuzah back is a divine name and acronym for Shomer Daltot Yisrael — Guardian of Israel's doors. The letter shin on the case often points outward. It is a reminder of faith that Hashem has the power to guard you at every doorway.

### shaliach tzibur

A shaliach tzibur is the prayer leader who represents the congregation before G-d — reciting the repetition of the Amidah and guiding pacing. He must be someone the community accepts and who knows the laws of prayer. Women and men have different roles per community in who may lead which parts.

### Shalom bayit

peace in the home; a central Jewish value

### shamash

On Chanukah, the shamash is the helper candle used to light the others; its light is not counted among the mitzvah candles, so you may use it for utility light. Do not use the actual mitzvah flames for reading or work — they are holy and forbidden for personal benefit (bizuy mitzvah). In a shul, the shamash often means the caretaker who maintains the building and/or assists the rabbi/services.

### Shamayim

Shamayim means Heaven or the skies — in yirat Shamayim (awe of Heaven) it means living with awareness that G-d sees all actions.

### Shamor

"guard (Shabbat)" from the Deuteronomy version of the Ten Commandments

### Shavuot

Shavuot marks Matan Torah — receiving the Torah at Sinai, seven weeks after Pesach. Unlike other festivals, candles and Kiddush may not begin until full nightfall (tzeit). Dairy meals, all-night learning, and reading Ruth are customs. It is one day in Israel, two in the Diaspora.

### shechita

Shechita is kosher slaughter: a trained shochet cuts the throat swiftly with a perfectly smooth knife so the animal dies with minimal suffering and the meat is permitted. Afterwards, blood is removed through salting (melicha) and soaking. Non-kosher meat is called treif. Shechita is one of the most carefully regulated mitzvot in kashrut.

### Shehecheyanu

Shehecheyanu thanks G-d who "has kept us alive, sustained us, and brought us to this time." It is said on the first night of a festival, and when wearing important new clothes, eating a seasonal fruit for the first time since it came back into season, and many other joyous occasions. Do not recite it on the final day(s) of Pesach (7th in Israel, 8th in the Diaspora) — those days are considered part of the original festival. On Yaknehaz nights it comes at the end of the combined Kiddush–havdalah sequence for the new Yom Tov.

### Shekhinah

The Shekhinah is G-d's indwelling presence — especially associated with the Temple, with Israel in exile, and with holiness in marriage and Shabbat. Kiddush Levana tradition says blessing the moon is like greeting the Shekhinah. It is not a separate being; it is how we speak of G-d being near.

### Shema

The Shema ("Hear O Israel") declares G-d's oneness and our duty to love Him with all our heart, soul, and might. It is recited morning and evening with surrounding blessings. Krias Shema has specific times — especially morning Shema before the third hour of the day halachically. It is the Jewish declaration of faith children learn first.

### Shema al HaMitah

Shema al HaMitah is the bedtime Shema — declaring faith and entrusting the soul to G-d before sleep. Many add Psalm 91 and other verses. Hamapil is said when actually lying down to sleep. Women are obligated in this protection per halacha. A few minutes of calm prayer ends the day well.

### Shemini Atzeret

Shemini Atzeret ("eighth day of assembly") is the biblical festival day immediately after the seven days of Sukkot — a day of special closeness with G-d. In Israel, with one day of Yom Tov, the rabbinic celebrations of Simchat Torah are folded into this biblical day. In the Diaspora, Shemini Atzeret is the first day and Simchat Torah is celebrated separately on the second day.

### Shemoneh Esrei

the eighteen (now nineteen) blessings of the Amidah

### shevarim

broken shofar blast

### shinui

unusual change of method that sometimes permits melacha leniency

### Shir HaMaalot

Shir HaMaalot is Psalm 126 — "When G-d returns the captivity of Zion" — sung before bentching on Shabbat and festivals. It expresses joy in redemption and hope for complete return. Many know the tune from Shabbat tables.

### shiva

Shiva is seven days of mourning at home after burial — community visits, low stools, no leather shoes per many minhagim, and Kaddish with a minyan. Work and entertainment pause. Shiva ends after the morning of the seventh day. The purpose is comfort and gradual re-entry to life, not isolation forever.

### shloshim

Shloshim is thirty days of reduced social celebration after burial (for many relationships). Haircuts and music may be restricted. After shloshim, normal life returns except for parents — Kaddish continues eleven months. Calendar overlaps with the Omer or Three Weeks add extra restrictions — ask your rav.

### shmirat halashon

guarding one's speech from lashon hara and harmful words

### Shmoneh Esrei

the nineteen blessings of the standing Amidah prayer

### shmurah matzah

matzah guarded from moisture from harvest; required by many for the Seder

### Shnayim Mikra v'Echad Targum

Shnayim Mikra v'Echad Targum is completing the weekly Torah portion twice in Hebrew and once in translation — Targum Onkelos or Rashi's commentary, which many use as a substitute to better understand the plain meaning (Shulchan Arukh and Mishnah Berurah O.C. 285:2). Ideally finish before the Shabbat meal; if not, before Mincha on Shabbat; later fallback windows apply per O.C. 285:4 (some until Wednesday of the following week, some until Simchat Torah). Even one aliyah weekly is a start — consistency matters.

### shochet

trained kosher slaughterer

### shofar

The shofar is a ram's horn (or other kosher horn) blown on Rosh Hashana as a wake-up call to teshuvah. Tekiah, shevarim, and teruah patterns follow minhag. It is not blown on Shabbat. Hearing shofar in shul with kavannah fulfills the mitzvah for most men; women's obligation follows community psak. Practice times are posted before the chag.

### Shomer Daltot Yisrael

Guardian of the doors of Israel (acronym Shin-Dalet-Yod (one of Hashem's names) on mezuzah)

### shomer negiah

Shomer negiah is the halachic guarding of touch between men and women (and related laws). It preserves the specialness of physical closeness for marriage and reduces situations that lead to serious sin. Details vary by circumstance; a rav can guide real-life situations like family, healthcare, etc.

### shul

Shul (synagogue) is the community house of prayer, Torah, and chesed. Beyond services, it often hosts classes, youth programs, and charity organizations.

### Shulchan Aruch

The Shulchan Aruch ("Set Table") is the classic 16th-century code of halacha (Jewish Law) by Rabbi Yosef Karo. Ashkenazim often study it with the Rema's glosses; Sephardim generally follow Rabbi Karo, but rabbis have debated about almost everything throughout the years.

### Shushan Purim

Shushan Purim is 15 Adar in walled cities that had walls in Joshua's time (Jerusalem is primary today). When 15 Adar is Shabbat, Jerusalem observes Purim Meshulash — Megillah and matanot Friday, mishloach and seudah Sunday. Elsewhere Purim is 14 Adar. Know your city's calendar.

### siddur

A siddur (from seder, "order") is the Jewish prayer book with the fixed texts for daily and Shabbat services — blessings, Shema, Amidah, Birkat Hamazon, and more. Editions follow nusach (Ashkenaz, Sefard, Chabad, etc.), so words and order differ slightly. Your siddur is your map for davening.

### simanim

Simanim are symbolic Rosh Hashana foods — apple in honey, pomegranate, fish head, dates — each with a pun or prayer for the new year. They are minhag, not Torah law, but beloved for teaching children. Say the short yehi ratzon prayers from the machzor.

### Simchat Torah

Simchat Torah ("rejoicing in the Torah") celebrates completing and restarting the annual Torah cycle with hakafot and joy. Biblically the day is Shemini Atzeret. In Israel, Simchat Torah is celebrated on that single biblical day. In the Diaspora, Shemini Atzeret is the first Yom Tov day and Simchat Torah is the second — separate calendar days with distinct focus.

### Simchat Yom Tov

Simchat Yom Tov (שִׂמְחַת יוֹם טוֹב) is the Torah mitzvah "V'samachta b'chagecha" — rejoice in your festival (Devarim 16:14). The head of household facilitates joy for each family member in the way that brings them happiness (Pesachim 109a; SA OC 529:2): wife — new clothing or jewelry l'fi mamono (within means); children — treats (today candies and toys; Talmud: grains and nuts); men — festive meat and wine meals. Halacha requires including the poor — joy only among yourselves without helping the needy is "joy of one's stomach," not mitzvah joy. At the least it's recommended to provide the poor with tzedaka so they can enjoy the holiday as well.

### Siyum

festive meal (seudat mitzvah) celebrating finishing a section of Torah study; eating at the meal (not merely hearing the siyum) exempts firstborns from Taanit Bechorot

### sofer

A sofer (scribe) is trained to write STaM — Torah scrolls, tefillin, mezuzot, and megillot — by hand with special ink on parchment. Letters must be formed exactly; mistakes can invalidate the scroll. Soferim also check existing klafim. Never buy tefillin or mezuzot without reliable certification.

### Soter

demolishing melacha

### Sukkah

A sukkah is a temporary booth with schach roof where we eat (and some sleep) on Sukkot — recalling desert clouds of glory. Walls must stand; schach must shade more than sun. Decorate for joy; invite guests and ushpizin. Rain may exempt you from eating in the sukkah — follow halacha for your situation.

### Sukkot

Sukkot is seven days dwelling in the sukkah and waving the Four Species — joy after the Days of Awe. It recalls desert clouds of glory. Rain may exempt from sitting. Shemini Atzeret follows immediately — a separate holiday of intimacy with G-d after the public festival.

### Ta'anit

a public or personal fast day

### Taanit Bechorot

Fast of the Firstborn on Erev Pesach (many break or nullify the fast with the seudat mitzvah after a siyum)

### Taanit Esther

fast day before Purim

### Tachanun

Tachanun are supplication prayers after the Amidah on weekdays — omitted on Shabbat, Yom Tov, Rosh Chodesh, and other happy days. Many kneel or lean on the arm. Skipping tachanun on a sad day is a sign the calendar treats the day as joyful. If you are unsure whether tachanun is said, follow the shul or siddur headings.

### taharat hamishpacha

Taharat hamishpacha (family purity) regulates intimacy through the niddah cycle and mikveh immersion. After menstruation, counting seven clean days and immersing in a mikveh permits reuniting. It is a private mitzvah with detailed laws — classes and rabbis guide couples. Healthy observance is associated with marital renewal and sanctity in Jewish tradition.

### tallit gadol

large prayer shawl with tzitzit worn during Shacharit/Musaf and all day on Yom Kippur

### tallit katan

small four-cornered garment with tzitzit, worn generally over or under a shirt per community custom

### Talmud

The Talmud (Gemara + Mishnah) is the central rabbinic discussion of halacha and aggada — developed in Babylonia and Israel. Daf Yomi learners finish the whole Talmud in about seven years. You can start small; even one line with commentary builds Jewish literacy. It is the backbone of how halacha develops.

### Tanach

Tanach (Tanakh) is the Hebrew Bible in three parts: Torah (the five books), Nevi'im (Prophets), and Ketuvim (Writings). Reading Tanach in Hebrew or translation is Torah study; Psalms (Tehillim) are part of Ketuvim.

### Tanya

The Tanya is Rabbi Schneur Zalman of Liadi's foundational Chabad work on the Jewish soul, divine service, and practical mysticism. Its full Hebrew name is Likutei Amarim ("collected sayings"). Chabad's daily Chitas program includes a portion of Tanya each day alongside Chumash and Tehillim. You can read it in Hebrew or English on Sefaria.

### Targum Onkelos

Targum Onkelos is the Aramaic translation of the Torah read in Shnayim Mikra v'Echad Targum. It was taught at Ezra's time to understand Torah. One who finds Onkelos difficult may substitute Rashi's commentary for the third reading (O.C. 285:2). Some use an accurate English translation instead.

### tashlich

Tashlich is a Rosh Hashana custom of casting breadcrumbs or symbolic sins into flowing water, reciting Micah 7:19. It is not in the Talmud like shofar, but is widespread. When the first day of Rosh Hashana falls on Shabbat, tashlich is postponed to Sunday — a structural rule across mainstream Ashkenazi and Sephardic practice, not a local preference. The Sages enacted this to prevent carrying machzorim through a public domain without a kosher eruv. It is symbolic teshuvah — the real work is inward change and apology to people you hurt.

### Tashlumin

Tashlumin is making up a missed Amidah at the very next service: missed Shacharit at Mincha, missed Mincha at Maariv, missed Maariv at tomorrow's Shacharit. Pray the current Amidah first, pause, then the makeup. If you accidentally say the makeup first, that Amidah counts as the current obligation and the second counts as tashlumin — no third Amidah needed (Mishnah Berurah 108:2). This applies only if you missed the prayer by accident or unavoidable delay — if you missed intentionally, there is no tashlumin. Missed holiday Shacharit is made up at Mincha, not at Musaf (SA O.C. 108:9).

### Tefilat Nedavah

Tefilat Nedavah is a voluntary Amidah when tashlumin is impossible — with mental stipulation and a small novelty in the prayer. It does not replace missed obligation like tashlumin but offers a way to pray after multiple misses. Not for daily casual use.

### tefillah

prayer; from a root meaning to judge oneself

### Tefillin

Tefillin are holy black leather boxes containing parchment with Torah verses, bound on the arm and head during weekdays. Men age thirteen and over are required to wear them. Shel yad (arm-tefillin) goes on the upper left arm for right-handed individuals, or the upper right arm if you are left-handed. Recite the bracha at misheyakir (see glossary). They require kosher scrolls and proper placement. They are not worn on Shabbat or festivals.

### Tehillim

Tehillim (the Hebrew word for "praises") is the traditional Jewish name for the Book of Psalms, which is a biblical collection of 150 sacred poems, prayers, and hymns of gratitude compiled primarily by King David and widely recited for comfort, divine protection, and spiritual connection

### tekiah

Tekiah is a long straight shofar blast. The Rosh Hashana sequence combines tekiah with shevarim (broken) and teruah (trembling) sounds per minhag. One hundred blasts are customary Ashkenaz. Practice in shul beforehand so the day is not your first time hearing shofar.

### teruah

quick staccato shofar blasts

### teshuvah

Teshuvah means "return" — coming back to G-d and to your best self after a mistake. The Rambam teaches that sincere teshuvah includes stopping the sin, regretting it, confessing to G-d, and resolving not to repeat it — with a plan for the future. Yom Kippur is the apex of teshuvah season, but repentance is available every day and can transform past actions into merit.

### tevilah

ritual immersion in a mikveh

### tevilat keilim

Tevilat keilim immerses new food utensils manufactured by a non-Jew in a mikveh — recalling the vessels Israel immersed after the Midian war. The obligation follows the manufacturer, not the retailer. Metal and glass usually require a bracha; glazed ceramic often requires immersion without a bracha. Items manufactured by a Jew are generally exempt. Labels "tovel before use" remind you — immersion is quick at a keilim mikveh or community event.

### three stars

colloquial marker for halachic nightfall (tzeit); actual timing varies slightly by community and location

### Three Weeks

The Three Weeks from 17 Tammuz to Tisha B'Av mourn the Temple's destruction. Ashkenazim and Chabad prohibit haircuts, weddings, and instrumental music for the full period; Sephardim and Edot HaMizrach are generally more lenient until the week of Tisha B'Av. Restrictions intensify in the Nine Days — customs differ by nusach.

### Tikkun Leil Shavuot

all-night Torah study on the first night

### Tisha B'Av

fast mourning the destruction of the Temple

### Tochen

grinding melacha

### Torah

Torah means "teaching" or "instruction." In the narrow sense it is the five books of the Torah — Genesis through Deuteronomy in English, Bereishit through Devarim in Hebrew — given at Sinai. In shul, the Torah is read from a handwritten Sefer Torah scroll. In study, it can also mean Mishnah, Talmud, halacha, and other works that explain how to live by G-d's will.

### Torah study

Torah study (Talmud Torah) means setting aside time to learn G-d's teaching — Chumash, Tanach, Mishnah, Talmud, halacha, or musar. The Mishnah says it outweighs all other mitzvot combined (Peah 1:1). Pirkei Avot teaches that the Shechinah dwells when words of Torah are spoken (Avot 3:2–3); the Talmud lists Torah among the three protectors of life (Sotah 21a). Tradition describes words of Torah ascending as merit and advocacy on high. Men and women have different scopes of obligation, but every Jew who learns bonds with Hashem. Even a few focused minutes daily counts.

### treif

Treif means non-kosher — originally torn flesh of improperly slaughtered animals, now any forbidden food. Mixing treif into a kosher kitchen can require kashering. "Kosher style" restaurants without supervision are not kosher.

### trop

Trop (ta'amei hamikra) are the cantillation marks above and below Hebrew in a Chumash that tell the baal koreh how to chant Torah — melody and phrase breaks. Learning trop is a skill separate from understanding the words. Congregants follow the chant in their books during the reading.

### tumah

Tumah is ritual impurity — a halachic state affecting what you may touch or eat (Temple-era laws, niddah, corpse contact, etc.). It is not moral guilt and not the same as being "dirty." Many tumah laws apply today in taharat hamishpacha and kohanim rules; others await the Temple. Morning tumah on hands is removed by washing, not by soap alone.

### tzedakah

Tzedakah is usually translated "charity," but the root means justice — sharing what G-d entrusted to you with those in need. The sages list it among the pillars on which the world stands; even a small coin given with a full heart is a great mitzvah.

### tzedakah box

charity box; collecting coins for those in need is itself a mitzvah

### Tzeit

Tzeit (tzeit hakochavim) is halachic nightfall — when three medium stars appear and the day ends for night mitzvot like counting the Omer, bedikat chametz, Chanukah candles, Purim, and the end of Shabbat. It is not identical to sunset; communities follow different standards — from three medium stars to fixed minutes after sunset, and in some Diaspora communities much later (e.g. Rabbeinu Tam). Note: While standard Shabbat and weekday Erev Yom Tov candles must be lit before sunset, whenever a festival begins on a Saturday night (Motzei Shabbat), the Yom Tov candles strictly must be lit after sunset, only once nightfall (tzeit) has arrived, using a pre-existing flame — lighting them before sunset on Shabbat is a severe violation. Shavuot candles also wait for tzeit. The app shows local zmanim; when in doubt on a borderline mitzvah, ask your rav.

### tzitzit

Tzitzit are fringes on four-cornered garments reminding us of all 613 mitzvot. The tallit katan is worn daily — generally over or under the shirt, depending on community custom (many Chassidim, Sephardim, and followers of the Arizal wear it over the shirt, under a vest or jacket); the tallit gadol at Shacharit. Strings must be kosher and tied correctly. Looking at tzitzit during Shema fulfills "you shall see them." Women in some communities wear tzitzit; ask your rav.

### tzniut

modest dress and conduct

### Ushpizin

Ushpizin are mystical "guests" — Abraham, Isaac, Jacob, and others — welcomed into the sukkah each night of Sukkot in Kabbalistic custom. Some recite an invitation before the meal. The idea is that true hospitality and Torah discussion bring holy presence into the booth. Even without the full kabbalistic text, inviting physical guests fulfills the mitzvah of joy in the sukkah.

### Vatodi'enu

Vatodi'enu ("You have made us know") is an insert in the Maariv Amidah on Saturday night when Yom Tov begins after Shabbat. It acknowledges that Shabbat has ended and the festival has begun.

### walk in G-d's ways

imitating divine kindness (clothing the needy, visiting the sick)

### Yaaleh V'yavo

Paragraph in Amidah and bentching on Rosh Chodesh and festivals. Forgot in Amidah: insert in Retzei if still there; after concluding Retzei return to the beginning of Retzei; after final Yihiyu L'ratzon repeat only that Amidah. Rosh Chodesh Maariv only: no repeat after Retzei (SA O.C. 422:1).

### yad soledet bo

Yad soledet bo ("when the hand recoils") is the heat threshold for bishul on Shabbat — roughly 110–113°F (43–45°C). Food already fully cooked may sometimes be reheated dry on Shabbat; liquids are stricter. This is why a blech keeps food warm without reaching cooking temperature.

### yahrzeit

Yahrzeit is the annual Hebrew-calendar date of a relative's death. Many light a 24-hour candle, visit the grave, give charity, and lead services if they are a kohen or know the customs. Parents' yahrzeits are especially observed. The name means "time of year" in Yiddish — a day to remember and elevate the soul.

### Yaknehaz

YaKNeHaZ is a mnemonic for the order when Shabbat flows directly into Yom Tov (for example Saturday night Pesach). Use your siddur's festival night Kiddush section for instructions.

### yetzer hara

Yetzer hara is the inclination toward selfishness, laziness, or sin — not a devil, but internal pull. Everyone has it; the battle is lifelong. Torah, mitzvot, and good friends strengthen yetzer hatov. The goal is not to destroy desire but to channel it — appetite for holy things, ambition for good deeds.

### yetzer hatov

Yetzer hatov is the inclination toward good — conscience, love of mitzvot, shame from wrongdoing. It grows when you learn Torah and see righteous examples. Jewish education (chinuch) cultivates yetzer hatov in children through habit before full understanding.

### yichud

Yichud is the prohibition against the seclusion of a man and a woman together in a private area when they are not married to each other and are not immediate blood relatives (parents, children, siblings, etc.). It prevents situations that could lead to improper intimacy. Exceptions and practical details (open doors, shared workplaces, healthcare) vary — ask your rav for real-life guidance.

### yirat Shamayim

fearing of Heaven; living with awareness that G-d is present

### Yizkor

Yizkor is the memorial prayer on Yom Tov (and Yom Kippur) for parents and sometimes other relatives. Many light a yahrzeit candle before the day. Pledging charity in their memory is a beautiful custom. Those with both parents living often leave the shul briefly per minhag. The prayer affirms that merit and memory continue beyond death.

### Yom Kippur

Yom Kippur is the Day of Atonement — fasting, confession, and prayer. Biblically called Shabbat Shabbaton, it carries the exact same strict labor (melacha) prohibitions as Shabbat, meaning cooking and kindling fire are strictly forbidden (unlike Yom Tov, which permits ochel nefesh). Kol Nidrei opens the eve; Ne'ilah closes the day. The day atones with teshuvah, but serious sins between people still require apology and restitution. Prepare easy break-fast before the fast begins.

### Yom Tov

Yom Tov (literally "good day") is a biblical festival such as Pesach, Shavuot, Sukkot, or Rosh Hashana. It resembles Shabbat in joy and in restricting melacha, but cooking and carrying are generally permitted for festival needs.

### Zachor

"remember" from the Exodus version of the Ten Commandments telling the Jews to remember Shabbat

### zemiroth

Zemiroth (zemirot) are Shabbat table songs — some biblical phrases, many composed by mystics and poets. Singing zemiroth turns the meal into oneg Shabbat. Common tunes include Menucha v'Simcha and Yah Ribon. No musical instruments on Shabbat per most poskim — voices only. Children learn Shabbat joy through zemiroth.

### zeroa

Zeroa (shankbone) is a roasted bone on the Seder plate reminding us of the Pesach sacrifice in the Temple. Today we do not offer the sacrifice; the zeroa is symbolic only — not eaten at the Seder. Roast it on Erev Pesach before sunset — roasting after Yom Tov begins violates ochel nefesh because it is not eaten that night. Many use a chicken neck or a special roasted bone.

### zimun

Zimun is inviting others to bentch together when three or more men (per common minhag) ate bread as a group. The leader says "let us bless" and others answer. It turns private thanks into communal praise. Women's zimun follows separate customs — ask your rav.

### zmanim

Zmanim are halachic times derived from sunrise, sunset, and seasonal hours — not always clock times. Examples: latest morning Shema, Mincha, Shabbat entry, chatzos, plag hamincha. A "halachic hour" divides daylight into twelve parts, so it lengthens in summer. Jewish calendars and apps translate zmanim for your location.

### Zohar

The Zohar is the classic work of Kabbalah, attributed to Rabbi Shimon bar Yochai, written in Aramaic. It illuminates the Torah's inner dimensions. Many study a daily snippet; full mastery requires Hebrew/Aramaic and guidance.

### Zorea

planting melacha


# Part 2 — Shabbat & Yom Tov Guide (in-app Shabbat Guide screen)


## Key Topics

### What is Shabbat?

Shabbat (the Sabbath) begins at sunset on Friday and ends when three stars are visible on Saturday night. It commemorates G-d resting on the seventh day of Creation and the Exodus from Egypt.

The day is marked by refraining from the 39 categories of creative labor (melachot), as well as by positive observances: candle lighting before sunset, Kiddush over wine, festive meals, prayer, Torah study, and Havdalah at the close of the day.

Shabbat is called a "sign" between G-d and the Jewish people.

### Shabbat Candle Lighting

Candles are lit before sunset on Friday to usher in Shabbat and bring peace and warmth into the home. This is performed by the woman of the household (or a man if no woman is present).

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

Note: There is a strict earliest boundary for this mitzvah. You cannot light candles prior to Plag HaMincha (approximately 1.25 halachic hours before sunset). Lighting before this time invalidates the mitzvah and makes the blessing unnecessary.

### Kiddush

Kiddush is the Torah-level commandment to "remember the Shabbat day to sanctify it" (Exodus 20:8), established by the Sages through a declaration over a cup of wine.

Key rules:
• Kiddush must be recited in the same place where you will eat your meal (Kiddush b'Makom Seudah).
• The cup must hold at least a revi'it (רְבִיעִית — approximately 86ml / ~3 fl oz). The minimum amount to actually drink is maleh lugmov (מְלֹא לֻגְמָיו — a cheekful, approximately 50ml / ~2 fl oz). Ideally, drink the majority of the cup. Exact measurements are debated; consult your rabbi.
• Women are equally obligated in Kiddush.
• Grape juice is a fully valid substitute for wine.
• On Friday night, if wine is unavailable, challah (bread) may substitute.
• On Shabbat morning, if wine is unavailable, beer or whiskey (chamar medinah — a locally significant beverage) may be used. Wine is always preferred. Consult your rabbi for specifics.
• Unlike Friday night, bread can never substitute for Kiddush on Shabbat morning — saying Hamotzi without wine or chamar medina means you have skipped daytime Kiddush entirely.

### Havdalah

Havdalah (separation) is the ceremony performed after Shabbat ends — Saturday night when three stars appear — marking the transition back to the weekdays.

The ceremony has four blessings: wine, fragrant spices (besamim), fire, and the Havdalah blessing (Hamavdil). The spices comfort the soul for the departure of the extra Shabbat soul (neshama yeteira).

Important: you may not perform any melachah (Shabbat-forbidden labor) after Shabbat ends until you have heard Havdalah or said the phrase "Baruch HaMavdil bein Kodesh l'chol" (Blessed is He Who separates the holy from the mundane).

### Hallel

Hallel (praise) is a selection of Psalms 113–118 recited as praise and thanksgiving to G-d on Yom Tov (festivals), Rosh Chodesh (the new month), and Chanukah.

Full Hallel is recited on Shavuot, all seven days of Sukkot (including Chol HaMoed Sukkot), Shemini Atzeret, Simchat Torah, and all eight days of Chanukah.

Pesach — Full vs. Half Hallel:
• Outside Israel (Diaspora): Full Hallel on the first two nights/days of Pesach (Yom Tov); Half Hallel from Chol HaMoed onward through the last days.
• In Israel: Full Hallel only on the first day of Pesach; Half Hallel from the second day onward.
The reason for Half Hallel from Chol HaMoed: we do not fully celebrate while the Egyptians drowned in the sea.

Half Hallel (certain paragraphs omitted) is also recited on Rosh Chodesh.

### Yaaleh V'Yavo

Yaaleh V'Yavo is a special prayer paragraph inserted into the Amidah (the standing silent prayer) and into Birkat HaMazon (Grace After Meals) on Rosh Chodesh (the new month), Yom Tov (festivals), and Chol HaMoed (intermediate festival days).

The text asks G-d to remember us, our fathers, Jerusalem, the Davidic dynasty, and the entire Jewish people for good — for life and peace — on the day being observed.

If forgotten in Shacharit or Mincha (also Chol HaMoed / Yom Tov at any Amidah including Maariv):
• Still in Retzei — insert Yaaleh V'yavo in its place and continue.
• After concluding Retzei — return to the beginning of Retzei, insert Yaaleh V'yavo, and complete the remaining blessings.
• After the final Yihiyu L'ratzon — repeat only that Amidah (Shemoneh Esrei), never the full service.

Rosh Chodesh Maariv only: if forgotten after finishing Retzei, or after the entire Amidah — do not repeat (Berachot 30b; Shulchan Arukh O.C. 422:1). If still in Retzei before God's name at the conclusion, insert it there.

### Rosh Chodesh

Rosh Chodesh (the New Month) is the first day of each Hebrew month, and in some months the 30th day of the previous month as well.

Special observances include: Yaaleh V'Yavo in prayers and Grace After Meals, half Hallel, a Musaf (additional) prayer, and a special Torah reading.

Rosh Chodesh is a semi-holiday — certain fasting and eulogizing are restricted. There is a widespread custom for women to refrain from certain types of work as an extra mark of honor for the day.

### Yom Tov — Jewish Festivals

Yom Tov (literally "good day") refers to the major Jewish festivals: Pesach, Shavuot, Rosh Hashana, Sukkot, and Shemini Atzeret/Simchat Torah. Yom Kippur has its own distinct laws closer to Shabbat.

Yom Tov shares most of Shabbat's restrictions, but three key differences are permitted: cooking and baking for that day's needs, kindling a flame from a pre-existing flame (not striking a new one), and carrying between domains for the sake of Yom Tov needs.

All other creative labors forbidden on Shabbat remain forbidden on Yom Tov. The many details of Yom Tov law are complex and vary by situation. Always consult your rabbi.

### Muktzeh

Muktzeh refers to items that are "set aside" and may not be moved on Shabbat. Unlike the 39 melachot which come from the Mishkan's construction, Muktzeh is a Rabbinic enactment to protect the spirit of Shabbat rest.

The main principle: any object whose primary purpose is forbidden on Shabbat — like a pen, hammer, or smartphone — may generally be moved only to use the space it occupies, or if you need it for a Shabbat-permitted purpose.

Items that are completely non-functional on Shabbat (like money, rocks, or broken objects that lost utility before Shabbat) generally may not be moved at all. To prepare, store wallets, work tools, and electronics out of reach before Shabbat begins.

### Tashlumin — Makeup Prayers

If you miss a prayer service (tefillah), there is a rabbinic mechanism to partially make it up — called tashlumin (תַּשְׁלוּמִין — "completions"). The rules are strict and the window is narrow.

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
• When in doubt about any situation, ask your rabbi.

### Rosh Hashana

Rosh Hashana (the Jewish New Year) falls on 1–2 Tishrei and is observed for two days in the Diaspora (one day in Israel). It is the anniversary of the creation of Adam and Eve and the Day of Judgment — when G-d reviews the deeds of all people and inscribes their fate for the coming year.

Key observances:
• Hearing the shofar (ram's horn) blown in synagogue — the Ashkenazi custom is approximately 100 blasts per day (200 total over the two days in the Diaspora)
• Special prayers and liturgy including the Unetaneh Tokef
• Eating symbolic foods: apples and honey for a sweet new year, pomegranate, fish head, dates, and other simanim (signs)
• Tashlich — a custom to go to a body of water on the afternoon of the first day of Rosh Hashana and recite verses from Micah (7:18–20) symbolizing casting away sins. The custom involves the prayers at the water, not throwing bread crumbs (many authorities including the Vilna Gaon and Chabad discourage or omit the bread crumb practice entirely)
• Rosh Hashana is a full Yom Tov — all Shabbat-like restrictions apply, with the exception of cooking and carrying

### Yom Kippur

Yom Kippur (the Day of Atonement) falls on 10 Tishrei. It is the holiest day of the Jewish year — a full fast from sundown to nightfall (approximately 25 hours) and a day of intensive prayer and introspection.

Five forms of affliction (inuyim) are observed: no eating or drinking, no bathing, no applying oils/creams, no leather shoes, no marital relations.

Key prayers: Kol Nidrei (the night before), Yizkor (Ashkenaz custom on Yom Kippur morning — many Sefard communities omit or observe different memorial customs), the Neilah closing prayer, and the final single shofar blast at the conclusion of the fast.

Unlike a regular Yom Tov, Yom Kippur shares many of Shabbat's additional restrictions — including a strict prohibition on carrying outdoors (unless inside a kosher eruv), exactly like Shabbat. Melachah (labor) is fully forbidden.

### Sukkot

Sukkot begins on 15 Tishrei and lasts seven days (in the Diaspora, the first two days are full Yom Tov, followed by five days of Chol HaMoed). It commemorates the 40 years the Jewish people dwelt in the desert under G-d's protection.

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
• Some Yemenite and other traditions differ on certain days — follow your siddur

### Shemini Atzeret & Simchat Torah

Shemini Atzeret is an independent Yom Tov immediately following Sukkot (22 Tishrei). In the Diaspora, Simchat Torah is observed on 23 Tishrei. In Israel, both are observed on 22 Tishrei.

Shemini Atzeret:
• Tefillat Geshem (prayer for rain) — inserted in Musaf in most Ashkenaz and Sefard communities; some say it at different points — follow your siddur
• Yizkor — memorial prayers; widely observed in Ashkenaz synagogues on Shemini Atzeret (and on Yom Kippur). Many Sefard communities do not recite Yizkor on this day, or observe different memorial customs

Simchat Torah:
• Celebrates the completion and immediate restart of the annual Torah reading cycle
• Ashkenaz / most Sefard: seven hakafot (processional circuits) with the Torah scrolls, singing and dancing; final portion (V'Zot HaBeracha) read, then Bereishit restarted
• Chabad: hakafot with singing; often one aliyah per scroll taken out
• Yemenite and other communities may have distinct hakafah and reading customs — follow your synagogue

### Chanukah

Chanukah (the Festival of Lights) is observed for eight nights beginning on 25 Kislev. It commemorates the miracle of the Temple menorah that burned for eight days on only one day's worth of oil, following the Maccabees' victory over the Greeks and rededication of the Temple.

Key observances:
• Lighting the Chanukiah (menorah) each night — one light on the first night, adding one each successive night
• Lights are placed in a visible location to publicize the miracle
• Hallel and Al HaNisim prayer are added to services
• It is customary to eat foods fried in oil (latkes, sufganiyot) and play dreidel

Chanukah is not a Yom Tov — regular weekday activities including work are permitted.

### Purim

Purim is observed on 14 Adar (15 Adar in walled cities — Shushan Purim). It commemorates the miraculous salvation of the Jewish people in Persia as recorded in Megillat Esther.

Four mitzvot of the day:
• Mikra Megillah — hearing Megillat Esther read aloud twice (night and day)
• Mishloach Manot — sending a food gift of at least two ready-to-eat foods to at least one friend
• Matanot L'evyonim — giving charity to at least two poor people
• Mishteh — having a festive Purim meal

It is also customary to dress in costumes. Purim is not a Yom Tov — work is not formally forbidden, though many abstain.

### Pesach (Passover)

Pesach begins on 15 Nissan. In the Diaspora it lasts eight days; in Israel seven days. It commemorates the Exodus from Egypt.

Chametz (leavened grain products) must be completely removed from one's home before Pesach. There is a Torah-level prohibition on owning or eating chametz during Pesach. Matzah is eaten in its place.

The Seder is held on the first two nights (in the Diaspora). The Haggadah is read, telling the story of the Exodus. Four cups of wine are drunk; matzah, maror (bitter herbs), and other symbolic foods are eaten.

The first two and last two days are full Yom Tov; the middle days (Chol HaMoed) have lighter restrictions. Full Hallel is recited on the first two days of Pesach (Yom Tov); Half Hallel is recited from Chol HaMoed onward and on the final Yom Tov days. In Israel, Half Hallel begins from the second day.

### Shavuot

Shavuot is observed on 6 Sivan (two days in the Diaspora). It celebrates the giving of the Torah at Mount Sinai, which took place seven weeks after the Exodus.

It is customary to:
• Study Torah through the night (Tikkun Leil Shavuot)
• Eat dairy foods — various reasons are given, including that the Jewish people had not yet received the Torah's laws of meat preparation
• Read Megillat Ruth, whose themes of Torah commitment echo the acceptance of the Torah
• Hear the Ten Commandments read in synagogue

Shavuot comes exactly 50 days after the second night of Pesach, at the end of the Sefirat HaOmer (Counting of the Omer). It is a full Yom Tov.

### Hoshana Raba

Hoshana Raba is the 21st of Tishrei — the seventh and final day of Sukkot. While it is technically Chol HaMoed (an intermediate day), it carries special significance as the final "sealing" of the divine judgment begun on Rosh Hashana.

Synagogue service — customs vary by nusach:
• Ashkenaz: extended service with seven hakafot (circuits) with the lulav and aravot; willow branches (hoshanot) beaten on the ground after the circuits; lulav taken for the last time
• Sefard: similar hoshanot circuits in many communities; willow-beating customs vary — follow your siddur
• Chabad: hakafot with hoshanot; aravot may be beaten five times on the ground (not the lulav itself) — see your Chabad machzor
• Yemenite: distinct hoshanot liturgy and customs

Greeting someone with "a gutten kvittel" (a good inscription) is an Ashkenaz custom, referring to the final sealing of one's decree for the year.

### Tu B'Shvat

Tu B'Shvat (the 15th of Shvat) is the New Year for Trees — one of the four "new years" mentioned in the Mishnah. It marks the date used for calculating the age of fruit trees for tithing purposes.

Tu B'Shvat is not a fast day, and work is permitted. It is customary to eat fruits of Israel, especially the seven species: wheat, barley, grapes, figs, pomegranates, olives, and dates. Many communities hold a "Tu B'Shvat seder" with fruits and wine.

In Kabbalistic tradition (particularly from the 16th-century Safed mystics), Tu B'Shvat became associated with the spiritual rectification of the world through eating fruits with intention.

### Pesach Sheni

Pesach Sheni (the Second Passover) falls on 14 Iyar. It originated from a request in the Torah (Numbers 9:6–13) by Jews who were ritually impure and could not bring the Passover offering — G-d granted them a second chance one month later.

Today, Pesach Sheni is a minor holiday. It is not observed with full Yom Tov restrictions. The main custom is to eat matzah on this day. Tachanun (the penitential prayer) is not recited.

A classic Chabad teaching — attributed to the Lubavitcher Rebbeim (Rabbi Yosef Yitzchak Schneersohn, citing his father the Rebbe Rashab) — holds that Pesach Sheni means "ein davar avud": nothing is lost; it is always possible to correct and return.

### Tu B'Av

Tu B'Av is the 15th of Av. It is described in the Talmud (Taanit 26b) as one of the two happiest days in the Jewish calendar (along with Yom Kippur).

Historically, it marked several positive events, including the end of the plague that killed those who accepted the report of the spies in the wilderness. In the era of the Temple, it was a day when unmarried women would dance in the vineyards.

Today, Tu B'Av is observed as a minor holiday of joy. Tachanun is not recited. Many communities treat it as an auspicious day for marriage and love.

### Tisha B'Av

Tisha B'Av (the 9th of Av) is the saddest day in the Jewish calendar. Both the First and Second Temples were destroyed on this date, along with numerous other tragedies throughout Jewish history.

It is a full 25-hour fast (sundown to nightfall), with the same five afflictions as Yom Kippur: no eating, drinking, bathing, anointing, leather shoes, or marital relations.

Melachah (work) is permitted but mourning customs apply: no Torah study (except on sad topics like Lamentations and Job), no greeting others, no music. Megillat Eichah (the Book of Lamentations) is read at night; Kinot (dirges) are recited in the morning.

The three weeks from 17 Tammuz until Tisha B'Av are called "The Three Weeks," a period of increasing mourning.

### Fast of Gedaliah

The Fast of Gedaliah is observed on 3 Tishrei (pushed to 4 Tishrei when 3 Tishrei falls on Shabbat). It commemorates the assassination of Gedaliah ben Ahikam, the Jewish governor appointed by Babylonia to administer the land after the destruction of the First Temple.

His death marked the end of the last vestiges of Jewish autonomy in the Land of Israel following the first exile.

It is a minor fast — from dawn until nightfall (not a full 25-hour fast like Yom Kippur or Tisha B'Av).

### Fast of 10 Tevet

The Fast of 10 Tevet commemorates the day Nebuchadnezzar, king of Babylon, began the siege of Jerusalem — the event that ultimately led to the destruction of the First Temple.

It is a minor fast from dawn until nightfall. It is also observed as a general Kaddish day for Holocaust victims whose date of death is unknown.

Note: This is the only fast in the calendar that is never postponed if it falls on a Friday (because the biblical verse requires observing it "on this very day"). When it lands on a Friday, continue fasting directly into Friday night — welcome Shabbat while fasting — and break the fast only after nightfall following the recitation of Friday night Kiddush.

### Fast of 17 Tammuz

The Fast of 17 Tammuz (Shiva Asar B'Tammuz) marks the day the walls of Jerusalem were breached by the Romans in the first century CE. It begins the "Three Weeks" — a period of mourning culminating in Tisha B'Av.

It is a minor fast from dawn until nightfall. During the Three Weeks, weddings are not held and music is generally avoided. During the final nine days of this period (1–9 Av), additional mourning customs apply.

### Fast of Esther

The Fast of Esther (Ta'anit Esther) is observed on 13 Adar, the day before Purim. It commemorates the three-day fast of Esther and the Jewish people before she approached King Achashverosh to plead on behalf of her people.

It is a minor fast from dawn until nightfall. If 13 Adar falls on Shabbat, the fast is moved to 11 Adar. Machatzit HaShekel (a half-shekel donation to charity) is given on Purim morning, sometimes before Megillah reading on the night before.

### Sefirat HaOmer

Sefirat HaOmer is the mitzvah to count the 49 days from the second night of Pesach until Shavuot — linking the Exodus to receiving the Torah.

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

The Omer period is also a time of mourning in many communities (no haircuts, weddings, or live music) until Lag BaOmer or Shavuot, depending on custom.

### Yom HaShoah

Yom HaShoah (Holocaust Remembrance Day) is observed in Israel on 27 Nisan. It commemorates the six million Jews murdered in the Holocaust and honors survivors.

In Israel, a siren brings the country to a standstill. Many communities hold memorial programs, recite Tehillim, and learn about the Shoah. It is not a Torah-mandated fast or Yom Tov; customs vary by community.

### Yom HaZikaron

Yom HaZikaron (Memorial Day) falls on 4 Iyar in Israel, immediately before Yom Ha'atzmaut. It honors fallen soldiers and victims of terror.

The day is marked by memorial ceremonies and sirens. Many observe it as a solemn day before the celebration of independence.

### Yom Ha'atzmaut

Yom Ha'atzmaut (Israeli Independence Day) is observed on 5 Iyar, celebrating the establishment of the State of Israel in 1948.

In Israel and many diaspora communities, it is marked with Hallel (per local custom), festive meals, and programs.

### Yom Yerushalayim

Yom Yerushalayim marks the reunification of Jerusalem in 1967, observed on 28 Iyar.

Many communities recite Hallel and hold festive gatherings. As with Yom Ha'atzmaut, customs and halachic rulings vary — consult your rav.

### Chol HaMoed Pesach

Chol HaMoed Pesach — the intermediate days of Passover — is not full Yom Tov, but it is not ordinary weekdays either.

Key points:
• No chametz; eat kosher-for-Passover food only. Matzah is eaten on many days.
• Half Hallel at Shacharit (not Full Hallel — Ashkenazic custom permits a bracha on Partial Hallel; Sephardic custom often does not).
• Yaaleh V'yavo in Amidah and bentching; Tachanun is omitted.
• Melacha is restricted but less than Yom Tov — work needed to avoid loss may be permitted; ask your rav.
• Simchat moed — nicer meals, family time, Torah learning; avoid treating the day like a regular workweek.
• Shaving and haircuts are generally prohibited on Chol HaMoed unless under specific exceptions — ask your rav.

### Chol HaMoed Sukkot

Chol HaMoed Sukkot — the intermediate days of Sukkot — is not full Yom Tov, but it is not ordinary weekdays either.

Key points:
• Full Hallel each day of Sukkot including Chol HaMoed.
• Yaaleh V'yavo in Amidah and bentching; Tachanun is omitted.
• Lulav and etrog each weekday (not on Shabbat); meals in the sukkah when applicable.
• Melacha is restricted but less than Yom Tov — work needed to avoid loss may be permitted; ask your rav.
• Simchat moed — nicer meals, time in the sukkah, Torah learning.
• Shaving and haircuts are generally prohibited on Chol HaMoed unless under specific exceptions — ask your rav.
• Hoshana Raba (21 Tishrei) is the seventh day of Sukkot with special hoshanot customs.

### Simchat Torah

Simchat Torah celebrates the completion and immediate restart of the annual Torah reading cycle.

In the Diaspora it falls on 23 Tishrei (day after Shemini Atzeret). In Israel it is observed together with Shemini Atzeret on 22 Tishrei.

Customs:
• Hakafot — joyous processional circuits with Torah scrolls, singing and dancing
• Final parsha V'Zot HaBeracha read; Bereishit begun again
• Festive Kiddush and meals; Yizkor in many Ashkenaz communities on Shemini Atzeret/Simchat Torah

It is a full Yom Tov in the Diaspora; in Israel it shares the day with Shemini Atzeret.

### Purim Katan

In a Jewish leap year, 14 Adar I is Purim Katan ("little Purim"). The full Purim mitzvot — Megillah, mishloach manot, matanot l'evyonim, and the festive meal — apply only on regular Purim (14 Adar, or 15 Adar in walled cities).

Purim Katan is marked by a slightly more festive tone; some communities add a special meal or omit Tachanun. It is not a substitute for Purim observance.

### Erev Pesach

Erev Pesach (14 Nissan) is the day of final Pesach preparation before the Seder.

Key preparations:
• Finish eating chametz by the end of the morning (check your local zmanim).
• Bedikat chametz — search for chametz by candlelight after nightfall the night before (or according to your community's schedule).
• Biur chametz — destroy remaining chametz the next morning.
• Sell chametz through your rabbi if needed.
• Prepare the Seder plate, matzah, wine, and Haggadot for the first night(s).

The Seder is held after nightfall on 15 Nissan (and the second night in the Diaspora). No chametz may be eaten after the morning deadline on Erev Pesach.


## Shabbat vs Yom Tov Comparison

| Activity | Shabbat | Yom Tov |
|----------|---------|--------|
| Cooking & baking | Forbidden | Permitted for the sake of that day's needs |
| Reheating dry food & keeping food warm | Dry, fully cooked food only — reheat with a shinui (unusual method), e.g. upside-down on blech or inside another vessel. Food on blech from before Shabbat may stay. | All cooking is permitted for that day's needs — including liquids (soups, coffee, etc.), not only dry food. |
| Kindling a flame | Forbidden | May kindle from a pre-existing flame only — not from scratch |
| Extinguishing a flame | Forbidden | Forbidden — let it burn out on its own |
| Carrying between domains | Forbidden (without eruv) | Permitted for the sake of Yom Tov needs |
| Electricity / driving / writing | Forbidden | Forbidden |
| Kiddush | Required | Required |
| Candle lighting | Required before sunset | Unlike Shabbat, not required before the day begins. Second Diaspora night or Motzei Shabbat: light after nightfall only, from a pre-existing flame — never early. Other nights: may light before sunset from pre-existing flame per custom |


## The 39 Melachot

### Zorea — Planting

Any action that promotes or sustains plant growth is forbidden: planting seeds, grafting branches, or watering your garden. Because watering actively fosters growth, even indoor houseplants may not be watered on Shabbat, no matter how dry the soil looks.

### Choresh — Plowing

Loosening, leveling, or improving the earth in any way — plowing, digging, aerating — is forbidden. The prohibition extends to dragging heavy objects: dragging a massive iron or wooden lawn bench across soft dirt can easily carve a deep furrow and must be avoided. Standard baby strollers and wheelchairs, however, are fully permitted to be pushed across dirt or grass, as normal wheel tracking is not considered an act of plowing.

### Kotzer — Reaping

Picking fruit, plucking a flower, or detaching anything from its source of growth is forbidden. This includes herbs from a kitchen windowsill pot. The Sages further forbade making direct use of a tree on Shabbat — climbing, leaning, or hanging hammocks is out.

### Me'ameir — Gathering

Taking scattered items that grew from the ground and collecting them into a single pile or unit. If apples fall from your tree, you may not gather them into a basket. However, the prohibition of gathering applies strictly to the place where the items naturally grow (like a field or orchard). Gathering items that spill on a kitchen floor does not violate Me'ameir under any circumstances.

### Dosh — Threshing

Separating a food product from its outer shell or casing. This applies today primarily to squeezing: squeezing grapes or olives for juice is a direct Torah-level violation. The Sages extended this to other fruits — freshly squeezing oranges or grapefruits is forbidden.

### Zoreh — Winnowing

Separating food from non-food using the wind or your breath. Shaking a tablecloth outdoors when the wind will scatter the crumbs, or blowing dust off a book cover, may fall under this prohibition according to the Jerusalem Talmud's broader understanding.

### Borer — Selecting

Separating a mixture by removing the unwanted from the wanted. For selection to be permitted, three conditions must all be met simultaneously: take the good from the bad (not bad from good), by hand or normal eating utensil (not a sorting tool like a strainer), and for immediate use only — not to prepare food for later. These rules apply to food and non-food alike (clothes, toys, etc.).

### Tochen — Grinding

Reducing something that grew from the ground to small particles. Dicing vegetables into very fine pieces (like for Israeli salad) may violate this — deliberately cut larger pieces than usual. Crushing dried mud off your shoes is also forbidden. 'There is no grinding after grinding' — crushing already-baked items like matzah is generally permitted.

### Meraked — Sifting

Using a dedicated utensil (sifter, strainer, French press filter) to separate food or liquids from unwanted mixtures. If the liquid is so full of sediment that no one would drink it unfiltered, straining is a Torah-level violation. Using a strainer on a drinkable liquid that just has pulp may be more lenient, but best to avoid dedicated separating utensils entirely.

### Losh — Kneading

Combining a powder or small solid particles with a liquid to form a single cohesive paste or thick mass. This applies to baby cereal, instant oatmeal, mustard, and certain textures of egg salad. When mixing is unavoidable, workarounds exist: reverse the order of ingredients and use an unusual stirring motion (shinui). This is why Shabbat challah is baked before Shabbat.

### Bishul — Cooking

Using fire-generated heat to improve any item — cooking, baking, frying, or boiling. The threshold is 'yad soledet bo' (hot enough to pull your hand away). 'There is no cooking after cooking' — re-warming dry, solid foods already fully cooked before Shabbat is generally permitted. Reheating cold liquids is forbidden. These laws are complex — consult your rabbi.

### Gozez — Shearing

Detaching any natural growth from the body of a living creature. This includes cutting hair, trimming fingernails, shaving, plucking, or pulling loose feathers from a pillow. Even biting fingernails or pulling loose skin is restricted. Preparing these beforehand is itself a mitzvah honoring Shabbat.

### Melaben — Laundering

Whitening, cleaning, or refreshing fabric. This goes well beyond the washing machine — even spot-cleaning a fresh spill by dabbing with water or rubbing a stain with cleaning intent can violate Melaben. Even wetting a dirty garment may be considered the beginning of the laundering process.

### Menapetz — Combing Fibers

Aggressively brushing or combing raw, tangled material to separate and align fibers. For hair, this means a hard-bristled brush that forcefully separates and tears strands falls under this or Gozez. On Shabbat, use a soft brush or wide-toothed comb gently on dry hair.

### Tzoveya — Dyeing

Permanently coloring or enhancing the appearance of a material using pigment. This affects cosmetics: using makeup that stains the skin is a concern on Shabbat. The deeper principle is not permanently altering the appearance of your surroundings to match your desires on the day of rest.

### Toveh — Spinning

Taking loose, disorganized fibers and drawing them out while twisting them into a continuous strand. Any action that creates thread, yarn, or cord from raw material is prohibited. Twisting multiple existing threads together to create a thicker rope or twine is also included.

### Maisach — Warping a Loom

Organizing and anchoring the vertical base threads on a loom, preparing its infrastructure for weaving. This teaches that Shabbat rest requires cessation not just of final creative acts but even the behind-the-scenes planning and structuring of creative work.

### Oseh Batei Nirin — Setting Heddles

Assembling the guiding loops and harnesses of a loom that allow the weaver to separate warp threads for weaving. Like Maisach, this is purely preparatory — it creates the infrastructure for future creation, which Shabbat requires us to cease.

### Oreg — Weaving

Interlacing two sets of threads at right angles to form fabric. The Talmud states that weaving even two parallel horizontal threads into a vertical grid is sufficient to violate this prohibition. Certain types of dense embroidery, cross-stitching, and basketry share the same structural mechanics.

### Potze'a — Unraveling

Deliberately unraveling or separating woven or braided material with the goal of re-using or re-weaving the strands. Pulling a loose thread from a sweater that unravels a row of stitches is a practical example. Shabbat forbids both constructing and deconstructing the material world.

### Koshair — Tying

Creating a permanent, professional-grade knot intended to remain long-term. A knot that is both professionally crafted and permanent is forbidden by the Torah. A simple temporary knot meant to be undone within 24 hours is permitted. A standard bow on a sneaker is fine; a tight double-knot meant to stay tied for weeks is not.

### Matir — Untying

Deliberately untying a knot. Any knot that is forbidden to tie is forbidden to untie. If a tight professional knot was intended to be permanent, it cannot be untied on Shabbat. An accidental knot — one never intended to be tied — may generally be untied leniently.

### Tofair — Sewing

Joining two separate pieces of flexible material into a unified entity using a connecting element (thread, glue, staples). Using liquid glue to paste pages together or applying duct tape to bind two surfaces falls under this category. In a fashion emergency, a temporary safety pin used casually is permitted.

### Koreah — Tearing

Tearing or ripping any unified object with the intent to improve it or prepare it for future creative use. Tearing toilet paper from a roll creates a separate, usable sheet — this is why observant homes use pre-cut tissues. Opening sealed packaging destructively to access food (ruining the packaging) is generally permitted.

### Tzad — Trapping

Restricting the freedom of a wild, un-domesticated creature by placing it in an enclosure from which it cannot easily escape. This applies to insects: covering a fly with a cup traps it. Even closing a door can be an issue if a wild bird wandered in. Exception: if a creature poses a genuine danger to life, Pikuach Nefesh overrides.

### Shochait — Slaughtering

Taking the life of any living creature, including insects. The Talmud extends this to causing a wound that draws blood (Chavurah). If flossing is certain to cause bleeding, it is forbidden. Cases of genuine danger to life override this prohibition.

### Mafshit — Skinning

Physically separating the skin or hide of any animal from its meat. While peeling the skin off a cooked chicken during a meal is permitted (it's eating, not processing leather), deliberately splitting apart layers of a leather item — like peeling delaminating layers of a leather belt or shoe sole — falls under this melacha.

### Me'abeid — Tanning

Processing raw organic material to preserve, toughen, or prevent it from decaying. Its key derivative is Molei'ach (salting): heavily salting raw vegetables like cucumbers to marinate or soften them mimics tanning. To avoid this, add salt right before serving, or mix vegetables with oil or vinegar first.

### Memachek — Smoothing

Smoothing, sanding, or scraping a solid surface to make it perfectly even. Its daily-life derivative Memarei'ach covers spreading thick pasty substances: rubbing a bar of solid soap, spreading ointment over a wound, or smoothing toothpaste on a brush. Use liquid soap, liquid lotion, and dab (don't spread) any necessary ointment.

### Mesarteit — Scoring Lines

Intentionally marking, etching, or scratching guide lines on a surface for writing or cutting. Drawing a preliminary guide line across a cake before cutting, or pressing a decorative border into dough to mark cuts, is restricted. Slicing directly through without a prior guide line is fine.

### Mechateich — Cutting to Size

Cutting, tearing, or trimming any object to a specific, measured dimension for a productive purpose. Tearing toilet paper on perforated lines is a direct violation (cutting at a measured boundary). Snapping apart multi-pack yogurt containers along a perforated seam also falls here.

### Koteiv — Writing

Forming any meaningful symbol or letter on a surface. To violate the Torah prohibition, at least two distinct letters or symbols must be written with a permanent medium on a lasting surface. The Sages extend this to temporary writing and shaping letters. Playing Scrabble with tiles that lock into a frame is questioned by some authorities.

### Mocheik — Erasing

Wiping, scraping, dissolving, or obliterating any meaningful symbol or letter. Under Torah law this applies when erasing enables rewriting. The Sages also forbid destructive erasing. Regarding tearing through printed text on food packaging: Ashkenazim prefer to avoid it from the outset; Sephardim generally permit it when accessing food.

### Boneh — Building

Creating, improving, or assembling any structure by combining parts into a stable unit. Setting up a large pop-up canopy or tent creates an Ohel (covering) and may be forbidden. Snapping together modular furniture tightly is also restricted. Minor assembly questions depend on specifics — ask your rabbi.

### Soter — Demolishing

Intentionally dismantling or breaking down any stable structure. Dismantling a large outdoor shade canopy or taking down a multi-panel tent can violate Soter. Unscrewing a door handle completely or forcefully separating tightly locked modular furniture falls under this restriction.

### Mechabeh — Extinguishing

Putting out, dimming, or dampening any flame or spark. Blowing out a candle, pouring water on a wick, turning off a gas stove, or even turning down a live burner to simmer is forbidden (dimming is a form of partial extinguishing). Lights and candles must be set up before Shabbat to burn naturally or turn off via timer.

### Mav'ir — Kindling

Creating, expanding, or fueling any flame or intense heat source. Striking a match, adding wood to a fire, or completing an electrical circuit to turn on a light or appliance is restricted. This is why observant homes use timers for lights, set slow-cookers before Friday, and rely on pre-existing warmth.

### Makeh B'patish — Final Blow

Any final action that takes an incomplete or broken object and renders it fully functional. Snipping a loose thread from a freshly tailored shirt, snapping a detached zipper back onto its track, or removing a protruding nail to make a surface safe — all are finishing acts of creation. Leave all repairs and assembly until after Havdalah.

### Hotza'ah — Carrying

Moving an object from a private domain to a public domain, or vice versa, or carrying an object four cubits (~6 feet) within a public domain. Most modern streets are rabbinically classified as a semi-public area (karmelit), where carrying is still restricted. Communities establish an eruv — a symbolic enclosure — to permit carrying within a shared area. Without an eruv, a house key can only be worn outdoors if it is structurally integrated by a specialist into a functional piece of clothing or jewelry (such as a belt clasp or decorative pin). Simply hanging a loose key on a string around your neck is a direct violation of carrying.


## Jewish Holidays (Guide summaries)

### What is Shabbat?

Shabbat (the Sabbath) begins at sunset on Friday and ends when three stars are visible on Saturday night. It commemorates G-d resting on the seventh day of Creation and the Exodus from Egypt.

The day is marked by refraining from the 39 categories of creative labor (melachot), as well as by positive observances: candle lighting before sunset, Kiddush over wine, festive meals, prayer, Torah study, and Havdalah at the close of the day.

Shabbat is called a "sign" between G-d and the Jewish people.

### Shabbat Candle Lighting

Candles are lit before sunset on Friday to usher in Shabbat and bring peace and warmth into the home. This is performed by the woman of the household (or a man if no woman is present).

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

Note: There is a strict earliest boundary for this mitzvah. You cannot light candles prior to Plag HaMincha (approximately 1.25 halachic hours before sunset). Lighting before this time invalidates the mitzvah and makes the blessing unnecessary.

### Kiddush

Kiddush is the Torah-level commandment to "remember the Shabbat day to sanctify it" (Exodus 20:8), established by the Sages through a declaration over a cup of wine.

Key rules:
• Kiddush must be recited in the same place where you will eat your meal (Kiddush b'Makom Seudah).
• The cup must hold at least a revi'it (רְבִיעִית — approximately 86ml / ~3 fl oz). The minimum amount to actually drink is maleh lugmov (מְלֹא לֻגְמָיו — a cheekful, approximately 50ml / ~2 fl oz). Ideally, drink the majority of the cup. Exact measurements are debated; consult your rabbi.
• Women are equally obligated in Kiddush.
• Grape juice is a fully valid substitute for wine.
• On Friday night, if wine is unavailable, challah (bread) may substitute.
• On Shabbat morning, if wine is unavailable, beer or whiskey (chamar medinah — a locally significant beverage) may be used. Wine is always preferred. Consult your rabbi for specifics.
• Unlike Friday night, bread can never substitute for Kiddush on Shabbat morning — saying Hamotzi without wine or chamar medina means you have skipped daytime Kiddush entirely.

### Havdalah

Havdalah (separation) is the ceremony performed after Shabbat ends — Saturday night when three stars appear — marking the transition back to the weekdays.

The ceremony has four blessings: wine, fragrant spices (besamim), fire, and the Havdalah blessing (Hamavdil). The spices comfort the soul for the departure of the extra Shabbat soul (neshama yeteira).

Important: you may not perform any melachah (Shabbat-forbidden labor) after Shabbat ends until you have heard Havdalah or said the phrase "Baruch HaMavdil bein Kodesh l'chol" (Blessed is He Who separates the holy from the mundane).

### Hallel

Hallel (praise) is a selection of Psalms 113–118 recited as praise and thanksgiving to G-d on Yom Tov (festivals), Rosh Chodesh (the new month), and Chanukah.

Full Hallel is recited on Shavuot, all seven days of Sukkot (including Chol HaMoed Sukkot), Shemini Atzeret, Simchat Torah, and all eight days of Chanukah.

Pesach — Full vs. Half Hallel:
• Outside Israel (Diaspora): Full Hallel on the first two nights/days of Pesach (Yom Tov); Half Hallel from Chol HaMoed onward through the last days.
• In Israel: Full Hallel only on the first day of Pesach; Half Hallel from the second day onward.
The reason for Half Hallel from Chol HaMoed: we do not fully celebrate while the Egyptians drowned in the sea.

Half Hallel (certain paragraphs omitted) is also recited on Rosh Chodesh.

### Yaaleh V'Yavo

Yaaleh V'Yavo is a special prayer paragraph inserted into the Amidah (the standing silent prayer) and into Birkat HaMazon (Grace After Meals) on Rosh Chodesh (the new month), Yom Tov (festivals), and Chol HaMoed (intermediate festival days).

The text asks G-d to remember us, our fathers, Jerusalem, the Davidic dynasty, and the entire Jewish people for good — for life and peace — on the day being observed.

If forgotten in Shacharit or Mincha (also Chol HaMoed / Yom Tov at any Amidah including Maariv):
• Still in Retzei — insert Yaaleh V'yavo in its place and continue.
• After concluding Retzei — return to the beginning of Retzei, insert Yaaleh V'yavo, and complete the remaining blessings.
• After the final Yihiyu L'ratzon — repeat only that Amidah (Shemoneh Esrei), never the full service.

Rosh Chodesh Maariv only: if forgotten after finishing Retzei, or after the entire Amidah — do not repeat (Berachot 30b; Shulchan Arukh O.C. 422:1). If still in Retzei before God's name at the conclusion, insert it there.

### Rosh Chodesh

Rosh Chodesh (the New Month) is the first day of each Hebrew month, and in some months the 30th day of the previous month as well.

Special observances include: Yaaleh V'Yavo in prayers and Grace After Meals, half Hallel, a Musaf (additional) prayer, and a special Torah reading.

Rosh Chodesh is a semi-holiday — certain fasting and eulogizing are restricted. There is a widespread custom for women to refrain from certain types of work as an extra mark of honor for the day.

### Yom Tov — Jewish Festivals

Yom Tov (literally "good day") refers to the major Jewish festivals: Pesach, Shavuot, Rosh Hashana, Sukkot, and Shemini Atzeret/Simchat Torah. Yom Kippur has its own distinct laws closer to Shabbat.

Yom Tov shares most of Shabbat's restrictions, but three key differences are permitted: cooking and baking for that day's needs, kindling a flame from a pre-existing flame (not striking a new one), and carrying between domains for the sake of Yom Tov needs.

All other creative labors forbidden on Shabbat remain forbidden on Yom Tov. The many details of Yom Tov law are complex and vary by situation. Always consult your rabbi.

### Muktzeh

Muktzeh refers to items that are "set aside" and may not be moved on Shabbat. Unlike the 39 melachot which come from the Mishkan's construction, Muktzeh is a Rabbinic enactment to protect the spirit of Shabbat rest.

The main principle: any object whose primary purpose is forbidden on Shabbat — like a pen, hammer, or smartphone — may generally be moved only to use the space it occupies, or if you need it for a Shabbat-permitted purpose.

Items that are completely non-functional on Shabbat (like money, rocks, or broken objects that lost utility before Shabbat) generally may not be moved at all. To prepare, store wallets, work tools, and electronics out of reach before Shabbat begins.

### Tashlumin — Makeup Prayers

If you miss a prayer service (tefillah), there is a rabbinic mechanism to partially make it up — called tashlumin (תַּשְׁלוּמִין — "completions"). The rules are strict and the window is narrow.

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
• When in doubt about any situation, ask your rabbi.

### Rosh Hashana

Rosh Hashana (the Jewish New Year) falls on 1–2 Tishrei and is observed for two days in the Diaspora (one day in Israel). It is the anniversary of the creation of Adam and Eve and the Day of Judgment — when G-d reviews the deeds of all people and inscribes their fate for the coming year.

Key observances:
• Hearing the shofar (ram's horn) blown in synagogue — the Ashkenazi custom is approximately 100 blasts per day (200 total over the two days in the Diaspora)
• Special prayers and liturgy including the Unetaneh Tokef
• Eating symbolic foods: apples and honey for a sweet new year, pomegranate, fish head, dates, and other simanim (signs)
• Tashlich — a custom to go to a body of water on the afternoon of the first day of Rosh Hashana and recite verses from Micah (7:18–20) symbolizing casting away sins. The custom involves the prayers at the water, not throwing bread crumbs (many authorities including the Vilna Gaon and Chabad discourage or omit the bread crumb practice entirely)
• Rosh Hashana is a full Yom Tov — all Shabbat-like restrictions apply, with the exception of cooking and carrying

### Yom Kippur

Yom Kippur (the Day of Atonement) falls on 10 Tishrei. It is the holiest day of the Jewish year — a full fast from sundown to nightfall (approximately 25 hours) and a day of intensive prayer and introspection.

Five forms of affliction (inuyim) are observed: no eating or drinking, no bathing, no applying oils/creams, no leather shoes, no marital relations.

Key prayers: Kol Nidrei (the night before), Yizkor (Ashkenaz custom on Yom Kippur morning — many Sefard communities omit or observe different memorial customs), the Neilah closing prayer, and the final single shofar blast at the conclusion of the fast.

Unlike a regular Yom Tov, Yom Kippur shares many of Shabbat's additional restrictions — including a strict prohibition on carrying outdoors (unless inside a kosher eruv), exactly like Shabbat. Melachah (labor) is fully forbidden.

### Sukkot

Sukkot begins on 15 Tishrei and lasts seven days (in the Diaspora, the first two days are full Yom Tov, followed by five days of Chol HaMoed). It commemorates the 40 years the Jewish people dwelt in the desert under G-d's protection.

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
• Some Yemenite and other traditions differ on certain days — follow your siddur

### Shemini Atzeret & Simchat Torah

Shemini Atzeret is an independent Yom Tov immediately following Sukkot (22 Tishrei). In the Diaspora, Simchat Torah is observed on 23 Tishrei. In Israel, both are observed on 22 Tishrei.

Shemini Atzeret:
• Tefillat Geshem (prayer for rain) — inserted in Musaf in most Ashkenaz and Sefard communities; some say it at different points — follow your siddur
• Yizkor — memorial prayers; widely observed in Ashkenaz synagogues on Shemini Atzeret (and on Yom Kippur). Many Sefard communities do not recite Yizkor on this day, or observe different memorial customs

Simchat Torah:
• Celebrates the completion and immediate restart of the annual Torah reading cycle
• Ashkenaz / most Sefard: seven hakafot (processional circuits) with the Torah scrolls, singing and dancing; final portion (V'Zot HaBeracha) read, then Bereishit restarted
• Chabad: hakafot with singing; often one aliyah per scroll taken out
• Yemenite and other communities may have distinct hakafah and reading customs — follow your synagogue

### Chanukah

Chanukah (the Festival of Lights) is observed for eight nights beginning on 25 Kislev. It commemorates the miracle of the Temple menorah that burned for eight days on only one day's worth of oil, following the Maccabees' victory over the Greeks and rededication of the Temple.

Key observances:
• Lighting the Chanukiah (menorah) each night — one light on the first night, adding one each successive night
• Lights are placed in a visible location to publicize the miracle
• Hallel and Al HaNisim prayer are added to services
• It is customary to eat foods fried in oil (latkes, sufganiyot) and play dreidel

Chanukah is not a Yom Tov — regular weekday activities including work are permitted.

### Purim

Purim is observed on 14 Adar (15 Adar in walled cities — Shushan Purim). It commemorates the miraculous salvation of the Jewish people in Persia as recorded in Megillat Esther.

Four mitzvot of the day:
• Mikra Megillah — hearing Megillat Esther read aloud twice (night and day)
• Mishloach Manot — sending a food gift of at least two ready-to-eat foods to at least one friend
• Matanot L'evyonim — giving charity to at least two poor people
• Mishteh — having a festive Purim meal

It is also customary to dress in costumes. Purim is not a Yom Tov — work is not formally forbidden, though many abstain.

### Pesach (Passover)

Pesach begins on 15 Nissan. In the Diaspora it lasts eight days; in Israel seven days. It commemorates the Exodus from Egypt.

Chametz (leavened grain products) must be completely removed from one's home before Pesach. There is a Torah-level prohibition on owning or eating chametz during Pesach. Matzah is eaten in its place.

The Seder is held on the first two nights (in the Diaspora). The Haggadah is read, telling the story of the Exodus. Four cups of wine are drunk; matzah, maror (bitter herbs), and other symbolic foods are eaten.

The first two and last two days are full Yom Tov; the middle days (Chol HaMoed) have lighter restrictions. Full Hallel is recited on the first two days of Pesach (Yom Tov); Half Hallel is recited from Chol HaMoed onward and on the final Yom Tov days. In Israel, Half Hallel begins from the second day.

### Shavuot

Shavuot is observed on 6 Sivan (two days in the Diaspora). It celebrates the giving of the Torah at Mount Sinai, which took place seven weeks after the Exodus.

It is customary to:
• Study Torah through the night (Tikkun Leil Shavuot)
• Eat dairy foods — various reasons are given, including that the Jewish people had not yet received the Torah's laws of meat preparation
• Read Megillat Ruth, whose themes of Torah commitment echo the acceptance of the Torah
• Hear the Ten Commandments read in synagogue

Shavuot comes exactly 50 days after the second night of Pesach, at the end of the Sefirat HaOmer (Counting of the Omer). It is a full Yom Tov.

### Hoshana Raba

Hoshana Raba is the 21st of Tishrei — the seventh and final day of Sukkot. While it is technically Chol HaMoed (an intermediate day), it carries special significance as the final "sealing" of the divine judgment begun on Rosh Hashana.

Synagogue service — customs vary by nusach:
• Ashkenaz: extended service with seven hakafot (circuits) with the lulav and aravot; willow branches (hoshanot) beaten on the ground after the circuits; lulav taken for the last time
• Sefard: similar hoshanot circuits in many communities; willow-beating customs vary — follow your siddur
• Chabad: hakafot with hoshanot; aravot may be beaten five times on the ground (not the lulav itself) — see your Chabad machzor
• Yemenite: distinct hoshanot liturgy and customs

Greeting someone with "a gutten kvittel" (a good inscription) is an Ashkenaz custom, referring to the final sealing of one's decree for the year.

### Tu B'Shvat

Tu B'Shvat (the 15th of Shvat) is the New Year for Trees — one of the four "new years" mentioned in the Mishnah. It marks the date used for calculating the age of fruit trees for tithing purposes.

Tu B'Shvat is not a fast day, and work is permitted. It is customary to eat fruits of Israel, especially the seven species: wheat, barley, grapes, figs, pomegranates, olives, and dates. Many communities hold a "Tu B'Shvat seder" with fruits and wine.

In Kabbalistic tradition (particularly from the 16th-century Safed mystics), Tu B'Shvat became associated with the spiritual rectification of the world through eating fruits with intention.

### Pesach Sheni

Pesach Sheni (the Second Passover) falls on 14 Iyar. It originated from a request in the Torah (Numbers 9:6–13) by Jews who were ritually impure and could not bring the Passover offering — G-d granted them a second chance one month later.

Today, Pesach Sheni is a minor holiday. It is not observed with full Yom Tov restrictions. The main custom is to eat matzah on this day. Tachanun (the penitential prayer) is not recited.

A classic Chabad teaching — attributed to the Lubavitcher Rebbeim (Rabbi Yosef Yitzchak Schneersohn, citing his father the Rebbe Rashab) — holds that Pesach Sheni means "ein davar avud": nothing is lost; it is always possible to correct and return.

### Tu B'Av

Tu B'Av is the 15th of Av. It is described in the Talmud (Taanit 26b) as one of the two happiest days in the Jewish calendar (along with Yom Kippur).

Historically, it marked several positive events, including the end of the plague that killed those who accepted the report of the spies in the wilderness. In the era of the Temple, it was a day when unmarried women would dance in the vineyards.

Today, Tu B'Av is observed as a minor holiday of joy. Tachanun is not recited. Many communities treat it as an auspicious day for marriage and love.

### Tisha B'Av

Tisha B'Av (the 9th of Av) is the saddest day in the Jewish calendar. Both the First and Second Temples were destroyed on this date, along with numerous other tragedies throughout Jewish history.

It is a full 25-hour fast (sundown to nightfall), with the same five afflictions as Yom Kippur: no eating, drinking, bathing, anointing, leather shoes, or marital relations.

Melachah (work) is permitted but mourning customs apply: no Torah study (except on sad topics like Lamentations and Job), no greeting others, no music. Megillat Eichah (the Book of Lamentations) is read at night; Kinot (dirges) are recited in the morning.

The three weeks from 17 Tammuz until Tisha B'Av are called "The Three Weeks," a period of increasing mourning.

### Fast of Gedaliah

The Fast of Gedaliah is observed on 3 Tishrei (pushed to 4 Tishrei when 3 Tishrei falls on Shabbat). It commemorates the assassination of Gedaliah ben Ahikam, the Jewish governor appointed by Babylonia to administer the land after the destruction of the First Temple.

His death marked the end of the last vestiges of Jewish autonomy in the Land of Israel following the first exile.

It is a minor fast — from dawn until nightfall (not a full 25-hour fast like Yom Kippur or Tisha B'Av).

### Fast of 10 Tevet

The Fast of 10 Tevet commemorates the day Nebuchadnezzar, king of Babylon, began the siege of Jerusalem — the event that ultimately led to the destruction of the First Temple.

It is a minor fast from dawn until nightfall. It is also observed as a general Kaddish day for Holocaust victims whose date of death is unknown.

Note: This is the only fast in the calendar that is never postponed if it falls on a Friday (because the biblical verse requires observing it "on this very day"). When it lands on a Friday, continue fasting directly into Friday night — welcome Shabbat while fasting — and break the fast only after nightfall following the recitation of Friday night Kiddush.

### Fast of 17 Tammuz

The Fast of 17 Tammuz (Shiva Asar B'Tammuz) marks the day the walls of Jerusalem were breached by the Romans in the first century CE. It begins the "Three Weeks" — a period of mourning culminating in Tisha B'Av.

It is a minor fast from dawn until nightfall. During the Three Weeks, weddings are not held and music is generally avoided. During the final nine days of this period (1–9 Av), additional mourning customs apply.

### Fast of Esther

The Fast of Esther (Ta'anit Esther) is observed on 13 Adar, the day before Purim. It commemorates the three-day fast of Esther and the Jewish people before she approached King Achashverosh to plead on behalf of her people.

It is a minor fast from dawn until nightfall. If 13 Adar falls on Shabbat, the fast is moved to 11 Adar. Machatzit HaShekel (a half-shekel donation to charity) is given on Purim morning, sometimes before Megillah reading on the night before.

### Sefirat HaOmer

Sefirat HaOmer is the mitzvah to count the 49 days from the second night of Pesach until Shavuot — linking the Exodus to receiving the Torah.

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

The Omer period is also a time of mourning in many communities (no haircuts, weddings, or live music) until Lag BaOmer or Shavuot, depending on custom.

### Yom HaShoah

Yom HaShoah (Holocaust Remembrance Day) is observed in Israel on 27 Nisan. It commemorates the six million Jews murdered in the Holocaust and honors survivors.

In Israel, a siren brings the country to a standstill. Many communities hold memorial programs, recite Tehillim, and learn about the Shoah. It is not a Torah-mandated fast or Yom Tov; customs vary by community.

### Yom HaZikaron

Yom HaZikaron (Memorial Day) falls on 4 Iyar in Israel, immediately before Yom Ha'atzmaut. It honors fallen soldiers and victims of terror.

The day is marked by memorial ceremonies and sirens. Many observe it as a solemn day before the celebration of independence.

### Yom Ha'atzmaut

Yom Ha'atzmaut (Israeli Independence Day) is observed on 5 Iyar, celebrating the establishment of the State of Israel in 1948.

In Israel and many diaspora communities, it is marked with Hallel (per local custom), festive meals, and programs.

### Yom Yerushalayim

Yom Yerushalayim marks the reunification of Jerusalem in 1967, observed on 28 Iyar.

Many communities recite Hallel and hold festive gatherings. As with Yom Ha'atzmaut, customs and halachic rulings vary — consult your rav.

### Chol HaMoed Pesach

Chol HaMoed Pesach — the intermediate days of Passover — is not full Yom Tov, but it is not ordinary weekdays either.

Key points:
• No chametz; eat kosher-for-Passover food only. Matzah is eaten on many days.
• Half Hallel at Shacharit (not Full Hallel — Ashkenazic custom permits a bracha on Partial Hallel; Sephardic custom often does not).
• Yaaleh V'yavo in Amidah and bentching; Tachanun is omitted.
• Melacha is restricted but less than Yom Tov — work needed to avoid loss may be permitted; ask your rav.
• Simchat moed — nicer meals, family time, Torah learning; avoid treating the day like a regular workweek.
• Shaving and haircuts are generally prohibited on Chol HaMoed unless under specific exceptions — ask your rav.

### Chol HaMoed Sukkot

Chol HaMoed Sukkot — the intermediate days of Sukkot — is not full Yom Tov, but it is not ordinary weekdays either.

Key points:
• Full Hallel each day of Sukkot including Chol HaMoed.
• Yaaleh V'yavo in Amidah and bentching; Tachanun is omitted.
• Lulav and etrog each weekday (not on Shabbat); meals in the sukkah when applicable.
• Melacha is restricted but less than Yom Tov — work needed to avoid loss may be permitted; ask your rav.
• Simchat moed — nicer meals, time in the sukkah, Torah learning.
• Shaving and haircuts are generally prohibited on Chol HaMoed unless under specific exceptions — ask your rav.
• Hoshana Raba (21 Tishrei) is the seventh day of Sukkot with special hoshanot customs.

### Simchat Torah

Simchat Torah celebrates the completion and immediate restart of the annual Torah reading cycle.

In the Diaspora it falls on 23 Tishrei (day after Shemini Atzeret). In Israel it is observed together with Shemini Atzeret on 22 Tishrei.

Customs:
• Hakafot — joyous processional circuits with Torah scrolls, singing and dancing
• Final parsha V'Zot HaBeracha read; Bereishit begun again
• Festive Kiddush and meals; Yizkor in many Ashkenaz communities on Shemini Atzeret/Simchat Torah

It is a full Yom Tov in the Diaspora; in Israel it shares the day with Shemini Atzeret.

### Purim Katan

In a Jewish leap year, 14 Adar I is Purim Katan ("little Purim"). The full Purim mitzvot — Megillah, mishloach manot, matanot l'evyonim, and the festive meal — apply only on regular Purim (14 Adar, or 15 Adar in walled cities).

Purim Katan is marked by a slightly more festive tone; some communities add a special meal or omit Tachanun. It is not a substitute for Purim observance.

### Erev Pesach

Erev Pesach (14 Nissan) is the day of final Pesach preparation before the Seder.

Key preparations:
• Finish eating chametz by the end of the morning (check your local zmanim).
• Bedikat chametz — search for chametz by candlelight after nightfall the night before (or according to your community's schedule).
• Biur chametz — destroy remaining chametz the next morning.
• Sell chametz through your rabbi if needed.
• Prepare the Seder plate, matzah, wine, and Haggadot for the first night(s).

The Seder is held after nightfall on 15 Nissan (and the second night in the Diaspora). No chametz may be eaten after the morning deadline on Erev Pesach.


# Part 3 — Seasonal, Hidden & Festival Checklist Explainers

All seasonal and situational checklist copy: hidden Shabbat/Yom Tov screens, Erev Shabbat prep (see Part 4), Musaf, Omer, holidays, Pesach prep, mourning periods, etc. Extracted live from Kotlin sources.

Titles and explanation text as shown in the app. Dynamic parts (Omer day number, sunset/tzeit times, which Yom Tov–Shabbat blocks appear, holiday name, parsha name) depend on your calendar, location, and profile.

*Auto-generated from Kotlin sources by `tools/export_app_reference_text.py`.*


## When the checklist is hidden
### Shabbat Shalom

**Title:** Shabbat Shalom

Today is Shabbat. Please put away your phone and keep the day holy — pray, learn Torah, enjoy Shabbat meals, and rest. Melacha (forbidden labor) applies on Shabbat, including most phone and device use. This app is for weekdays and erev Shabbat preparation, not for use during Shabbat.

Put away your phone and enjoy a peaceful, screen-free Shabbat.

### [Holiday name] (Yom Tov)

**Title:** [Holiday name]

Today is [holiday name] (Yom Tov — a festival day). Please put away your device and keep the day holy. Melacha (forbidden labor) applies on Yom Tov similar to Shabbat. This app is for weekdays and erev chag preparation, not for use on Yom Tov.

Put away your phone and keep the festival day holy.


## Seasonal checklist items (SeasonalChecklistItems.kt)
### Yaaleh V'yavo — Rosh Chodesh (in Mincha Amidah)

*Section: Afternoon Prayer* · ID: `yaaleh_vyavo_rosh_chodesh_mincha`


*(No static explanation text — may be assembled at runtime.)*


### Erev Chanukah prep — set up menorah, candles, and brachot

*Section: Chanukah* · ID: `erev_chanukah_prep`


*(No static explanation text — may be assembled at runtime.)*


### Light Chanukah candles — Night $day

*Section: Chanukah* · ID: `chanukah_lighting_day_$day`


*(No static explanation text — may be assembled at runtime.)*


### Eat matzah on Pesach (optional on Chol HaMoed)

*Section: Chol HaMoed* · ID: `chol_hamoed_pesach_matzah`


*(No static explanation text — may be assembled at runtime.)*


### Honor Chol HaMoed with a festive day

*Section: Chol HaMoed* · ID: `chol_hamoed_honor`


*(No static explanation text — may be assembled at runtime.)*


### Revi'it of wine — daily mitzvah on Chol HaMoed (recommended)

*Section: Chol HaMoed* · ID: `chol_hamoed_wine_reviit`


*(No static explanation text — may be assembled at runtime.)*


### Wear nicer clothes in honor of the moed

*Section: Chol HaMoed* · ID: `chol_hamoed_nicer_clothes`


*(No static explanation text — may be assembled at runtime.)*


### Yaaleh V'yavo — Rosh Chodesh (in Maariv Amidah)

*Section: Evening Prayer* · ID: `yaaleh_vyavo_rosh_chodesh`


*(No static explanation text — may be assembled at runtime.)*


### erev_public_fast_prep

*Section: Fasts* · ID: `erev_public_fast_prep`


*(No static explanation text — may be assembled at runtime.)*


### erev_tisha_beav_prep

*Section: Fasts* · ID: `erev_tisha_beav_prep`


*(No static explanation text — may be assembled at runtime.)*


### erev_yom_kippur_eat

*Section: Fasts* · ID: `erev_yom_kippur_eat`


*(No static explanation text — may be assembled at runtime.)*


### motzei_yom_kippur_meal

*Section: Fasts* · ID: `motzei_yom_kippur_meal`


*(No static explanation text — may be assembled at runtime.)*


### public_fast_day

*Section: Fasts* · ID: `public_fast_day`


*(No static explanation text — may be assembled at runtime.)*


### Kiddush Levana — Sanctification of the Moon (once per Hebrew month)

*Section: Monthly* · ID: `kiddush_levana`


*(No static explanation text — may be assembled at runtime.)*


### Rosh Chodesh — the New Month

*Section: Monthly* · ID: `rosh_chodesh_observances`


*(No static explanation text — may be assembled at runtime.)*


### Full Hallel — Rosh Chodesh during Chanukah

*Section: Morning Prayer (Shacharit)* · ID: `rosh_chodesh_full_hallel_chanukah`


*(No static explanation text — may be assembled at runtime.)*


### Half Hallel — Rosh Chodesh

*Section: Morning Prayer (Shacharit)* · ID: `rosh_chodesh_half_hallel`


*(No static explanation text — may be assembled at runtime.)*


### Psalm 27 — L'Dovid Hashem Ori

*Section: Morning Prayer (Shacharit)* · ID: `ldovid_hashem_ori`


*(No static explanation text — may be assembled at runtime.)*


### Yaaleh V'yavo — Rosh Chodesh (in Shacharit Amidah)

*Section: Morning Prayer (Shacharit)* · ID: `yaaleh_vyavo_rosh_chodesh_shacharit`


*(No static explanation text — may be assembled at runtime.)*


### Nine Days: observe stricter mourning customs

*Section: Mourning customs* · ID: `nine_days_mourning_customs`


*(No static explanation text — may be assembled at runtime.)*


### Observe Three Weeks mourning customs

*Section: Mourning customs* · ID: `three_weeks_mourning_customs`


*(No static explanation text — may be assembled at runtime.)*


### Sefirah: mourning customs (music, weddings, haircuts)

*Section: Mourning customs* · ID: `sefirah_mourning_music`


During Sefirat HaOmer we keep customs of mourning (aveilut) because Rabbi Akiva's 24,000 students died in a plague during this period between Pesach and Shavuot (Talmud, Yevamot 62b). Their deaths ceased on Lag BaOmer — which is why many communities ease some restrictions then, while others continue until Shavuot or the morning of the 33rd day of the Omer.

Why we mourn: The Omer is the path from physical freedom (Pesach) to spiritual receiving of Torah (Shavuot). The plague cut short Torah transmission — so we temper joy with restraint until we reach Matan Torah.

Common customs (timing varies — ask your rav):
• No live music (recordings and a cappella rules vary by posek)
• No weddings
• No haircuts for part or all of the Omer

Follow your community's start and end dates for these practices.

**Ashkenaz:**

Ashkenaz custom: mourning from after Pesach until Lag BaOmer (33rd day of the Omer, 18 Iyar) or until the morning of Lag BaOmer (per your shul). Some continue haircuts/music restrictions until Shavuot or the Three Weeks.

No weddings, no live music, and no haircuts during your community's Sefirah period. Lag BaOmer is a break for many Ashkenazim; ask your rabbi about music and haircuts after that date.

**Sefard:**

Sephardi custom (Shulchan Arukh O.C. 493:1-2; Peninei Halakha 05-03-03): mourning from Pesach until the morning of the 34th day of the Omer (Lamed-Dalet). Music on Lag BaOmer in honor of R. Shimon bar Yochai is permitted, but weddings and haircuts remain restricted until the 34th morning per prevalent Sephardi psak (Rav Ovadia Yosef, Yechaveh Daat 3:31). Some communities (e.g. Turkey, Egypt) end mourning on Lag BaOmer — follow your kehilla.

Ask your rav which tradition you follow and when restrictions begin and end.

**Edot HaMizrach:**

Edot HaMizrach communities follow different Omer traditions (Peninei Halakha 05-03-03):
• Many follow Shulchan Arukh O.C. 493:1-2 — mourning until the morning of the 34th day of the Omer.
• Many who follow the Ari act strictly and refrain from haircuts until the day before Shavuot (Kaf HaChaim 493:13, cited in Peninei Halakha 05-03-03).
• Some North African kehillot end mourning on Lag BaOmer.

Music, weddings, and haircuts follow your kehilla's psak — ask your rav.

**Chabad:**

Chabad (Alter Rebbe / Arizal): haircut and shaving restrictions continue the entire 49 days through Erev Shavuot — adults do not take haircuts on Lag BaOmer (the sole exception is upsherin for a 3-year-old boy). Lag BaOmer is a day of intense joy with music, bonfires, and celebration, but haircut restrictions remain until Shavuot.

Music is generally avoided through Shavuot per Chabad practice, with Lag BaOmer as a day without music restrictions. Weddings follow your Chabad rabbi's guidance.

Ask your Chabad rabbi for details on your community.


### erev_chag_prep

*Section: Prepare for the festival* · ID: `erev_chag_prep`


*(No static explanation text — may be assembled at runtime.)*


### prepare_for_festival_${prep.name.lowercase()}

*Section: Prepare for the festival* · ID: `prepare_for_festival_${prep.name.lowercase()}`


*(No static explanation text — may be assembled at runtime.)*


### yom_tov_shabbat_advance_prep

*Section: Prepare for the festival* · ID: `yom_tov_shabbat_advance_prep`


*(No static explanation text — may be assembled at runtime.)*


### Hear the Megillah (Purim)

*Section: Purim* · ID: `purim_megillah`


Mikra Megillah (hearing the Book of Esther) is a rabbinic mitzvah instituted for Purim (Esther 9:28; Megillah 19a). Men and women are equally obligated.

When to hear it:
• Once on Purim evening — after nightfall (tzeit)
• Once on Purim day — you are halachically obligated to hear the Megillah during the daytime (usually right after morning Shacharit and before sunset). The daytime reading is the primary fulfillment of the mitzvah (mitzvat ha'yom); the night reading was instituted later — both are required.

How to fulfill:
• Hear every word read from a kosher megillah scroll by someone who can discharge your obligation (Shulchan Arukh O.C. 690)
• Stand for the blessings; customs when Haman's name is read vary by shul (many make noise; sitting vs standing — follow your community)
• Listen without talking — missed words may require hearing that passage again; ask your rabbi if unsure

Blessings before reading:
• Al mikra megillah
• She'asa nissim
• Shehecheyanu (recited on the first night; Ashkenazim recite it by day as well)

Machatzit haShekel: A widespread pre-Purim custom (not one of the four Purim mitzvot in the same way); many give before Megillah — follow your community. Confirm local reading times with your shul.

Prayers & meals:
• Insert Al HaNissim into every Amidah and into Birkat Hamazon (bentching) all day long on Purim.


### Matanot la'evyonim — gifts to the poor

*Section: Purim* · ID: `purim_matanot_laevyonim`


Matanot la'evyonim (מתנות לאביונים) helps every Jew celebrate Purim with food and joy (Esther 9:22).

The mitzvah (Peninei Halakha 05-16-03):
• Give at least one gift to each of two different poor people (minimum of two recipients total) during Purim daytime.
• Each gift should enable a modest Purim meal — money is common (Peninei Halakha: roughly enough for about three slices of bread or your community's minimum; amounts vary).

How to do it:
• Give during Purim daytime only (not at night); many give after the daytime Megillah reading (follow your minhag).
• Ideally before your Purim seudah so recipients can use it for the meal.
• You may give through a trustworthy messenger, gemach, or organization that distributes on Purim day — verify funds reach the poor that day
• If you cannot find recipients, ask your rabbi or shul — many collect on Purim morning

Who qualifies: Someone who lacks resources for Purim — your rav can guide you if unsure.


### Mishloach manot — food gifts to friends

*Section: Purim* · ID: `purim_mishloach_manot`


Mishloach manot (משלוח מנות) — sending portions of food — increases friendship and joy on Purim (Esther 9:19).

The mitzvah (Rambam, Shulchan Arukh 695:4; Aish; Peninei Halakha 05-16-04):
• Send at least two different ready-to-eat foods or drinks to one friend on Purim day — one mishloach manot package.
• Women are equally obligated; many send to a woman friend and men to a man (Rema). Sending to additional friends is praiseworthy.
• Examples: wine and cookies, fruit and pastry — clearly two types, not one combined dish.

How to do it:
• Deliver on Purim day before sunset — by you or a messenger (shul lists, kids, or neighbors are fine)
• Food should be ready to eat without cooking
• Label sender and recipient; mishloach manot should be identifiable

Tips: You need not reciprocate every package you receive the same day. Plan ahead on Erev Purim so deliveries are not rushed at the last minute.


### Purim Meshulash (Friday): Hear the Megillah

*Section: Purim* · ID: `purim_meshulash_friday_megillah`


*(No static explanation text — may be assembled at runtime.)*


### Purim Meshulash (Friday): Matanot la'evyonim

*Section: Purim* · ID: `purim_meshulash_friday_matanot`


*(No static explanation text — may be assembled at runtime.)*


### Purim Meshulash (Sunday): Mishloach manot

*Section: Purim* · ID: `purim_meshulash_sunday_mishloach`


*(No static explanation text — may be assembled at runtime.)*


### Purim Meshulash (Sunday): Purim seudah

*Section: Purim* · ID: `purim_meshulash_sunday_seudah`


*(No static explanation text — may be assembled at runtime.)*


### Purim Meshulash — read full plan before Shabbat

*Section: Purim* · ID: `purim_meshulash_advance_prep`


*(No static explanation text — may be assembled at runtime.)*


### Purim seudah — festive afternoon meal

*Section: Purim* · ID: `purim_seudah`


The Purim seudah (סעודת פורים) is one of the four Purim mitzvot (Esther 9:22; Peninei Halakha 05-16-02).

When:
• During Purim day — before sunset (many hold the meal in the afternoon after mitzvot are underway).

How:
• A festive meal with bread (hamotzi — many use two rolls), meat, wine, and joy. Do not use matzah — there is no Purim custom for matzah, and many avoid matzah in the weeks before Pesach so the Seder taste stays distinct (Rema O.C. 471:2).
• Include words of Torah or thanks to Hashem — the meal is a mitzvah, not only a party.
• Drinking wine is a widespread custom but not required to excess; celebrate responsibly.

Plan the menu and timing so matanot la'evyonim and mishloach manot are handled earlier in the day when possible.


### erev_purim_prep

*Section: Purim* · ID: `erev_purim_prep`


*(No static explanation text — may be assembled at runtime.)*


### Birkat Ha'Ilanot — Blessing on Fruit Trees

*Section: Seasonal* · ID: `birkat_hailanot`


*(No static explanation text — may be assembled at runtime.)*


### Birkat Hachamah — Blessing the Sun

*Section: Seasonal* · ID: `birkat_hachamah`


*(No static explanation text — may be assembled at runtime.)*


### Motzei Yom Kippur: begin building the sukkah (men)

*Section: Seasonal* · ID: `motzei_yk_build_sukkah`


*(No static explanation text — may be assembled at runtime.)*


### Say Selichot (Ashkenaz timing)

*Section: Seasonal* · ID: `selichot_elul_ashkenaz`


*(No static explanation text — may be assembled at runtime.)*


### Say Selichot (Chabad — Nusach Ari)

*Section: Seasonal* · ID: `selichot_elul_chabad`


*(No static explanation text — may be assembled at runtime.)*


### Simchat Torah — hakafot & Torah joy

*Section: Seasonal* · ID: `simchat_torah_focus`


*(No static explanation text — may be assembled at runtime.)*


### Tu B'Shvat Seder (optional)

*Section: Seasonal* · ID: `tu_bshvat_seder_optional`


*(No static explanation text — may be assembled at runtime.)*


### Yom Ha'atzmaut — Israeli Independence Day

*Section: Seasonal* · ID: `yom_haatzmaut`


Yom Ha'atzmaut (5 Iyar) commemorates Israeli independence in 1948.

Customs vary significantly by community:

Religious Zionist / Modern Orthodox: Hallel is recited (instituted by the Chief Rabbinate of Israel). Whether Hallel is said with a bracha is disputed — the Chief Rabbinate instructed with a bracha; many Ashkenazi poskim outside Israel say without a bracha. Tachanun is omitted. Some communities add special festive prayers (Hallel u'Maariv).

Sephardic (Rav Ovadia Yosef / Yalkut Yosef): Rav Ovadia Yosef ruled that Hallel should not be recited (concern of bracha levatala since the day was not established by Chazal). Tachanun omission is also disputed in these communities.

Chabad: The Rebbe did not institute any special observance. Most Chabad communities do not say Hallel and recite Tachanun as usual.

Charedi communities (Agudah, Litvish): Generally do not observe the day as a religious holiday. Tachanun is said as usual.

The Omer continues to be counted normally. There is no Al HaNissim addition to davening.

Ask your rav which custom your community follows.


### Yom HaShoah — Holocaust Remembrance Day

*Section: Seasonal* · ID: `yom_hashoah`


Yom HaShoah V'HaGevurah (27 Nisan) is the national day of remembrance for the six million Jews murdered in the Holocaust. It was established by the Israeli Knesset in 1953.

Date adjustment: If 27 Nisan falls on Friday, the day is observed on Thursday (26 Nisan). If it falls on Sunday, it is observed on Monday (28 Nisan), to avoid disruption of Shabbat.

Customs by community:

In Israel: Two-minute siren sounds at 10:00 AM; most Israelis stop and stand in silence. Memorial ceremonies are held at Yad Vashem and throughout the country.

Prayers: Standard weekday davening — Yom HaShoah does not add or remove any siddur insertions. It is a Knesset civil memorial, not a rabbinically instituted liturgical day. (27 Nisan falls in Nisan — Tachanun is omitted throughout the entire month of Nisan per Shulchan Arukh O.C. 429:2, the universal standard for Ashkenazim and Sephardim alike; that is a separate rule of the joyous month, not this observance.) Some communities hold memorial learning or ceremonies.

Charedi communities: Many do not observe this date as a religious memorial, preferring 10 Tevet (designated by the Chief Rabbinate in 1949 as Yom Kaddish HaKlali for those whose date of death is unknown) or Tisha B'Av as the appropriate day of mourning for all Jewish tragedies. This is a matter of minhag and communal leadership.

Chabad: No official communal observance is instituted, though the memory of the kedoshim (holy martyrs) is honored.


### Yom HaZikaron — Fallen Soldiers Memorial Day

*Section: Seasonal* · ID: `yom_hazikaron`


Yom HaZikaron (4 Iyar) is Israel's national day of remembrance for soldiers of the Israel Defense Forces and victims of terrorism who gave their lives for the State of Israel. It was established by the Knesset in 1963 and always falls the day before Yom Ha'atzmaut.
In Israel: Memorial sirens sound at 8:00 PM (start of the day, at nightfall) and again at 11:00 AM the following morning. Ceremonies are held at military cemeteries across the country. Flags fly at half-mast.

Prayers (Religious Zionist / Dati Leumi): Tachanun is recited fully at Shacharit — the day is one of solemn national memory. Tachanun is omitted only at Mincha as the calendar transitions into Yom Ha'atzmaut celebrations (Peninei Halakha 5:4:11; Koren Yom Ha'atzmaut mahzor — Mincha ketana before nightfall). Communities that do not treat Yom Ha'atzmaut as a religious day say Tachanun at Mincha too.

Most Charedi and Chabad communities: Do not observe as a religious day; regular weekday davening with Tachanun throughout the day.

The day ends at nightfall with the transition into Yom Ha'atzmaut celebrations.


### Yom Yerushalayim — Jerusalem Day

*Section: Seasonal* · ID: `yom_yerushalayim`


Yom Yerushalayim (28 Iyar) marks the reunification of Jerusalem during the Six-Day War in 1967 (5727).

Customs vary by community:

Religious Zionist / Dati Leumi: Hallel is recited (with or without a bracha, depending on the posek and community). Tachanun is omitted. Some communities recite special tefillot.

Sephardic (Rav Ovadia Yosef): Hallel is not recited for the same reason as Yom Ha'atzmaut — not established by Chazal. Practice varies.

Chabad and Charedi communities: Generally no special observance. Tachanun is said as usual.

Yom Yerushalayim is observed by fewer communities than Yom Ha'atzmaut, and there is no universally accepted halachic obligation. Ask your rav about your community's custom.


### shemini_atzeret_focus

*Section: Seasonal* · ID: `shemini_atzeret_focus`


*(No static explanation text — may be assembled at runtime.)*


### sukkot_arba_minim

*Section: Seasonal* · ID: `sukkot_arba_minim`


*(No static explanation text — may be assembled at runtime.)*


### sefirat_haomer_day_$day

*Section: Sefirat HaOmer* · ID: `sefirat_haomer_day_$day`


*(No static explanation text — may be assembled at runtime.)*



## Erev Yom Tov holiday-specific blocks (ErevChagPrepText.kt)
### Erev Rosh Hashana prep

Rosh Hashana — Jewish New Year; Day of Judgment. No melacha from tonight.

If this year Rosh Hashana meets Shabbat, a detailed section follows (eruv tavshilin, Yaknehaz Kiddush–havdalah, etc.) — only in years when the calendar requires it. Use your Machzor for the exact wording.

Tonight & tomorrow:
• Light Yom Tov candles before sunset.
${shehecheyanuErevLines(HebrewCalendarEngine.ROSH_HASHANA, tomorrowCal, profile)}
• Festive meals with Kiddush, challah dipped in honey, and symbolic foods (apple & honey, pomegranate, etc.).
• Hear the shofar blown during daytime services tomorrow (not tonight).
• Add Yaaleh V'yavo in Amidah and bentching; Tachanun is omitted.

Customs:
• Greet others with wishes for a good year (L'shanah tovah).
• Many avoid nuts, vinegar, and sharp foods on Rosh Hashana (minhag).
• Tashlich (casting sins into water) is on the first afternoon when Rosh Hashana is not Shabbat; if the first day is Shabbat, tashlich is postponed to Sunday.

${diasporaSecondDayNote(profile, "Rosh Hashana")}


### Erev Pesach prep — Yom Tov & seder

$pesachBegins

$chametzNote

Seder (first night):
• $sederWhen
• Matzah, maror, four cups of wine, reading the Haggadah, afikoman.
• Reclining (hasebha): Recline to the left when drinking the four cups and eating matzah, korech, and afikoman — do not recline while eating maror or chazeret (they symbolize slavery).
• Kiddush, festive meal, Hallel, Nirtzah.

Tomorrow by day:
• Yom Tov davening with Full Hallel and Musaf; no chametz or kitniyot (per your custom).
• Only eat food prepared for Pesach in kosher-for-Passover utensils.

${diasporaSecondDayNote(profile, "Pesach")}$shabbatBlock


### Erev Shavuot prep

Shavuot — receiving the Torah at Sinai. Yom Tov from tonight.

Tonight & tomorrow:
• Do not light candles, begin Maariv, or make Kiddush until full nightfall (tzeit) — see the timing note above. Shavuot is the only festival that may not begin before tzeit.
• Light Yom Tov candles at tzeit.
${shehecheyanuErevLines(HebrewCalendarEngine.SHAVUOS, tomorrowCal, profile)}
• Dairy is a cherished Shavuot minhag (cheesecake, blintzes). A festive meat meal with wine fulfills the primary mitzvah of Simchat Yom Tov (O.C. 529:2); many families have dairy first, then a full meat Yom Tov meal.
• All-night Torah learning (Tikkun Leil Shavuot) is a widespread custom tonight.
• Staying up all night: If you remain awake the entire night studying, standard Ashkenazi practice is to hear the morning blessings and Torah blessings from someone who slept, to avoid halachic doubts. However, according to Chabad custom (and several Sephardic authorities), you personally recite the entire sequence of morning and Torah blessings yourself after dawn (alot hashachar), even with zero sleep.
• Read Megillat Rut in many communities (tomorrow).
• Full Yom Tov davening with Full Hallel and Musaf; Akdamut/Megillat Rut per minhag.

No melacha; treat meals and prayer with joy and Torah focus.


### Erev Sukkot prep — Yom Tov tonight

Sukkot (first day) begins tonight — Zman Simchateinu.

Before sunset:
• Avoid eating a formal meal inside the sukkah today (Rama O.C. 639:1) so that your entry tonight is distinctly dedicated to the start of the mitzvah.
• Have arba minim ready: lulav, etrog, hadasim, aravot (per your rabbi's kashrut standards).

Tonight & tomorrow:
• Light Yom Tov candles in the sukkah (per custom) or home.
${shehecheyanuErevLines(HebrewCalendarEngine.SUCCOS, tomorrowCal, profile)}
• ${if (profile.isInIsrael) "Shake lulav and etrog with bracha tomorrow (first day of Sukkot)." else "Shake lulav and etrog with bracha on the first and second days of Yom Tov (Diaspora); continue on Chol HaMoed per custom."}
• Festive meals in the sukkah; Ushpizin welcome guests.
• No melacha; Full Hallel and Musaf in davening.

${diasporaSecondDayNote(profile, "Sukkot")}


### Erev Shemini Atzeret prep

Shemini Atzeret begins tonight${if (profile.isInIsrael) " — in Israel this is also Simchat Torah." else "."}

Tonight & tomorrow:
• Light Yom Tov candles.
${shehecheyanuErevLines(HebrewCalendarEngine.SHEMINI_ATZERES, tomorrowCal, profile)}
• No lulav on Shemini Atzeret — the mitzvah ended with the seventh day of Sukkot.
${if (profile.isInIsrael)


### Erev Simchat Torah prep

Simchat Torah begins tonight (Diaspora) — rejoicing with the Torah.

Tonight & tomorrow:
• Light Yom Tov candles.
${shehecheyanuErevLines(HebrewCalendarEngine.SIMCHAS_TORAH, tomorrowCal, profile)}
• Hakafot — dancing with Torah scrolls; finish the annual cycle and begin Bereshit.
• Festive meals and drinking (responsibly) in many communities.
• Synagogue note: Because drinking often occurs during daytime hakafot, many synagogues move the Priestly Blessing (Birkat Kohanim) up to the early morning Shacharit service instead of keeping it in Musaf, so Kohanim are completely sober for the blessing.
• Full Yom Tov — no melacha; Full Hallel and Musaf.
• In Israel, Simchat Torah coincides with Shemini Atzeret (one day).


### commonErevBlock

Before chag — every erev Yom Tov:
$sunsetLine
$cookingLine
• Turn off phones and devices before Yom Tov — this app is for prep, not use on chag.
$shulServicesLine$simchasBlock


### holidayBlock

Rosh Hashana — Jewish New Year; Day of Judgment. No melacha from tonight.

If this year Rosh Hashana meets Shabbat, a detailed section follows (eruv tavshilin, Yaknehaz Kiddush–havdalah, etc.) — only in years when the calendar requires it. Use your Machzor for the exact wording.

Tonight & tomorrow:
• Light Yom Tov candles before sunset.
${shehecheyanuErevLines(HebrewCalendarEngine.ROSH_HASHANA, tomorrowCal, profile)}
• Festive meals with Kiddush, challah dipped in honey, and symbolic foods (apple & honey, pomegranate, etc.).
• Hear the shofar blown during daytime services tomorrow (not tonight).
• Add Yaaleh V'yavo in Amidah and bentching; Tachanun is omitted.

Customs:
• Greet others with wishes for a good year (L'shanah tovah).
• Many avoid nuts, vinegar, and sharp foods on Rosh Hashana (minhag).
• Tashlich (casting sins into water) is on the first afternoon when Rosh Hashana is not Shabbat; if the first day is Shabbat, tashlich is postponed to Sunday.

${diasporaSecondDayNote(profile, "Rosh Hashana")}

Yom Kippur — Day of Atonement. Full 25-hour fast; five afflictions from sunset tonight until nightfall tomorrow.

Today before the fast:
• The mitzvah of eating: There is a unique halachic obligation to eat and drink throughout the day on Erev Yom Kippur (Berachot 8b). Halacha considers eating today and fasting tomorrow as two halves of the same complete mitzvah — eat regular meals during the day, not only the final pre-fast meal.
• Eat a festive pre-fast meal (seudah hamafseket) before sunset — finish eating and drinking in time.
• Light candles before Kol Nidre with the bracha (neir shel Yom Hakippurim per your siddur) — all flames must be lit before sunset. Yom Kippur has the same strict fire restrictions as Shabbat: unlike regular Yom Tov, you cannot transfer a flame once the fast begins.
• Crucial timing note: Once you light candles and say the blessing, Yom Kippur has fully begun for you — you cannot drive or ride in a vehicle after that point. If you plan to drive to synagogue for Kol Nidre, either arrive early and light at shul, or explicitly make a mental halachic condition (tnai) before lighting at home that you are not accepting the holiday until you arrive at synagogue (ask your rav for guidance).
• Give tzedakah and ask forgiveness from others.
• Kaparot (if your custom) is done before Yom Kippur.

On Yom Kippur (no eating, drinking, washing for pleasure, anointing, leather shoes, or marital relations):
• Spend the day in prayer at shul (Kol Nidre tonight, full day of services tomorrow).
• Clothing & shoes: There is a widespread custom to wear white clothing to look like angels. Separately, it is a strict halachic prohibition for everyone to wear leather shoes or leather footwear of any kind on Yom Kippur (one of the five mandatory inuyim).
• Ne'ilah at the end; after nightfall pray Maariv, then Havdalah over wine and a ner she-shavat (a flame that burned throughout Yom Kippur, such as a 48-hour candle lit before the fast). Do not use besamim (spices) — Yom Kippur has no neshama yeteira; spices are omitted unless Yom Kippur itself fell on Shabbat. Then break the fast.

Chametz:
• Biur was Friday morning (13 Nisan). On Shabbat morning, finish eating chametz by the end of the 4th halachic hour and recite the final Kol Chamira before the end of the 5th halachic hour — do not burn on Shabbat. Bedikat was Thursday night; mechirat chametz should already be authorized.

Chametz:
• All chametz must be completely gone, destroyed, or sold, and the final Kol Chamira recited, before the end of the 5th halachic hour this morning (midday threshold) — NOT sunset. Stop eating chametz by the end of the 4th halachic hour. Bedikat chametz was last night; mechirat chametz should already be authorized with your rabbi — use today's biur chametz checklist item if still on your list.

$pesachBegins

$chametzNote

Seder (first night):
• $sederWhen
• Matzah, maror, four cups of wine, reading the Haggadah, afikoman.
• Reclining (hasebha): Recline to the left when drinking the four cups and eating matzah, korech, and afikoman — do not recline while eating maror or chazeret (they symbolize slavery).
• Kiddush, festive meal, Hallel, Nirtzah.

Tomorrow by day:
• Yom Tov davening with Full Hallel and Musaf; no chametz or kitniyot (per your custom).
• Only eat food prepared for Pesach in kosher-for-Passover utensils.

${diasporaSecondDayNote(profile, "Pesach")}$shabbatBlock

Another Yom Tov day of Pesach begins after sunset today — no melacha from then.

Tomorrow:
• Yom Tov davening with Half Hallel and Musaf; no chametz or kitniyot (per your custom).
• Festive meals in kosher-for-Passover utensils only.
• Light Yom Tov candles before sunset.
${shehecheyanuErevLines(HebrewCalendarEngine.PESACH, tomorrowCal, profile)}${diasporaFinalPesachAdvanceNote(tomorrowCal, profile)}

Shavuot — receiving the Torah at Sinai. Yom Tov from tonight.

Tonight & tomorrow:
• Do not light candles, begin Maariv, or make Kiddush until full nightfall (tzeit) — see the timing note above. Shavuot is the only festival that may not begin before tzeit.
• Light Yom Tov candles at tzeit.
${shehecheyanuErevLines(HebrewCalendarEngine.SHAVUOS, tomorrowCal, profile)}
• Dairy is a cherished Shavuot minhag (cheesecake, blintzes). A festive meat meal with wine fulfills the primary mitzvah of Simchat Yom Tov (O.C. 529:2); many families have dairy first, then a full meat Yom Tov meal.
• All-night Torah learning (Tikkun Leil Shavuot) is a widespread custom tonight.
• Staying up all night: If you remain awake the entire night studying, standard Ashkenazi practice is to hear the morning blessings and Torah blessings from someone who slept, to avoid halachic doubts. However, according to Chabad custom (and several Sephardic authorities), you personally recite the entire sequence of morning and Torah blessings yourself after dawn (alot hashachar), even with zero sleep.
• Read Megillat Rut in many communities (tomorrow).
• Full Yom Tov davening with Full Hallel and Musaf; Akdamut/Megillat Rut per minhag.

No melacha; treat meals and prayer with joy and Torah focus.

Sukkot (first day) begins tonight — Zman Simchateinu.

Before sunset:
• Avoid eating a formal meal inside the sukkah today (Rama O.C. 639:1) so that your entry tonight is distinctly dedicated to the start of the mitzvah.
• Have arba minim ready: lulav, etrog, hadasim, aravot (per your rabbi's kashrut standards).

Tonight & tomorrow:
• Light Yom Tov candles in the sukkah (per custom) or home.
${shehecheyanuErevLines(HebrewCalendarEngine.SUCCOS, tomorrowCal, profile)}
• ${if (profile.isInIsrael) "Shake lulav and etrog with bracha tomorrow (first day of Sukkot)." else "Shake lulav and etrog with bracha on the first and second days of Yom Tov (Diaspora); continue on Chol HaMoed per custom."}
• Festive meals in the sukkah; Ushpizin welcome guests.
• No melacha; Full Hallel and Musaf in davening.

${diasporaSecondDayNote(profile, "Sukkot")}

Shemini Atzeret begins tonight${if (profile.isInIsrael) " — in Israel this is also Simchat Torah." else "."}

Tonight & tomorrow:
• Light Yom Tov candles.
${shehecheyanuErevLines(HebrewCalendarEngine.SHEMINI_ATZERES, tomorrowCal, profile)}
• No lulav on Shemini Atzeret — the mitzvah ended with the seventh day of Sukkot.
${if (profile.isInIsrael)

else

}
• Festive Yom Tov meals.

Simchat Torah begins tonight (Diaspora) — rejoicing with the Torah.

Tonight & tomorrow:
• Light Yom Tov candles.
${shehecheyanuErevLines(HebrewCalendarEngine.SIMCHAS_TORAH, tomorrowCal, profile)}
• Hakafot — dancing with Torah scrolls; finish the annual cycle and begin Bereshit.
• Festive meals and drinking (responsibly) in many communities.
• Synagogue note: Because drinking often occurs during daytime hakafot, many synagogues move the Priestly Blessing (Birkat Kohanim) up to the early morning Shacharit service instead of keeping it in Musaf, so Kohanim are completely sober for the blessing.
• Full Yom Tov — no melacha; Full Hallel and Musaf.
• In Israel, Simchat Torah coincides with Shemini Atzeret (one day).



## Seasonal text modules (Kotlin)

### SeasonalMitzvahText.kt
#### arbaMinimExplanation

${arbaMinimSharedBody(profile)}

Men — Torah obligation:
• The first day of Sukkot worldwide (15 Tishrei) is the Torah-level day for men. In the Diaspora, the mitzvah continues rabbinically on the second day of Yom Tov and Chol HaMoed.
• Bracha: Men say Al netilat lulav every day when taking the lulav (except Shabbat). Shehecheyanu is on the first day only.
• ${arbaMinimMenWave(profile)}

The ownership rule (lakhem — u'lekachtem lakhem):
• In Israel: the strict requirement to fully own your lulav and etrog set applies on the first day of Sukkot (15 Tishrei; Shulchan Arukh O.C. 658:3). If you do not own a set, ask the owner for matana al menat lehachzir (gift on condition of return) before you wave.
• In the Diaspora: because the second day of Yom Tov is kept as a safek de'oraita, the same lakhem ownership requirement applies on both Day 1 and Day 2 — you cannot simply borrow a synagogue set; it must be given to you as matana al menat lehachzir.
• In the Diaspora from the third day onward (the first day of Chol HaMoed): you may borrow a shared or synagogue set without a formal gift — the rabbinic continuation no longer carries the Torah ownership requirement (Peninei Halakha, Laws of Sukkot 13:13).
• In Israel on Chol HaMoed: borrowing without a gift is permitted — ownership was required only on the first day.


#### birkatHaIlanotChabadNote

Chabad in the Southern Hemisphere:
Some Chabad communities look for rare late-blooming fruit trees during Nissan (local autumn) to align with the traditional month. If no suitable blossoms are found in Nissan, rely on the general rule and recite the blessing in Elul–Tishrei when local trees flower — as above.


#### birkatHaIlanotExplanation

When (Southern Hemisphere — your location):
The Shulchan Arukh places this blessing in Nissan, when spring arrives in Israel. Mainstream poskim rule that the blessing follows the natural blossoming of fruit trees in your locale — not the calendar month alone (Yalkut Yosef; Peninei Halakha). In Australia, South America, southern Africa, and similar regions, local spring falls in Elul and Tishrei (typically September–October). Say it once during that window, as soon as you see the first blossoms.

When (Northern Hemisphere — your location):
Say it during Nissan, when fruit trees in your area begin to blossom — ideally as early in the month as you first see flowers (Shulchan Arukh O.C. 226:1).

When:
In the Northern Hemisphere, say it during Nissan when fruit trees blossom (Shulchan Arukh O.C. 226:1). In the Southern Hemisphere, say it when local fruit trees blossom in Elul–Tishrei. Set your city or GPS in Settings so the app shows this item in the correct season for you.

Birkat Ha'Ilanot (בִּרְכַּת הָאִילָנוֹת — Blessing of the Trees) is recited once each Jewish year upon seeing fruit trees in bloom — thanking Hashem for the beauty and renewal of creation.

This checklist appears during the likely season for your hemisphere — a reminder to go look for blossoms, not an obligation to recite before you see them.

$whenBlock

The blessing (said once per year):
Baruch Atah Ado-nai Eloheinu Melech ha'olam shelo chiser be'olamo kelum u'vara vo beriyot tovot v'ilanot tovim lehanot bahem benei adam.
(בָּרוּךְ אַתָּה ה' אֱלֹקֵינוּ מֶלֶךְ הָעוֹלָם שֶׁלֹּא חִסֵּר בְּעוֹלָמוֹ כְּלוּם וּבָרָא בוֹ בְּרִיּוֹת טוֹבוֹת וְאִילָנוֹת טוֹבִים לֵהָנוֹת בָּהֶם בְּנֵי אָדָם)
"Blessed are You, L-rd our G-d, King of the universe, Who has withheld nothing from His world and created in it good creatures and good trees for people to enjoy."

How to fulfill it:
• Go outdoors to fruit-bearing trees — at least one tree; preferably two or more of the same or different species.
• Recite the blessing once while viewing the blossoms. Many add Tehillim 104 or other verses; follow your siddur or community custom.
• Once a year: if you said it in your local spring, you do not repeat it when traveling to another hemisphere later in the same Jewish year.
• Many communities avoid saying it on Shabbat — go during the week when you first see suitable blossoms.

What stage must the tree be in (melav'lave — מְלַבְלְבֵי)?
The rabbis require the tree to be actively flowering — not merely sprouting leaves. Shulchan Arukh O.C. 226:1 defines the Talmudic stage of melav'lave as when trees "put forth flowers" (shemotzi'in perach): you need open, visible blossoms that you can recognize as flowers. The blessing praises Hashem for creating "good trees for people to enjoy" — that response is meant to come from seeing beautiful, open blooms.

L'chatchilah (ideal): the tree has moved past the leaf-only stage and shows open flowers, but the petals have not yet fallen and fruit has not begun to form.

What does not work:
• Green leaf buds alone — invalid; do not recite the blessing on leaves without open flowers.
• After petals have dropped and fruit is forming — too late for this year's blessing.

Bedieved (after the fact): some contemporary poskim allow reciting on swollen, tightly closed flower buds that are clearly about to open when no properly blooming trees are available — ask your rav if that is your only option.

Spiritual note:
Nissan is the month of redemption; blossoming trees recall that creation itself waits to praise Hashem. Even a brief walk among flowering orchards can be a mitzvah.


#### cholHamoedClothesExplanation

Wearing nicer clothing on Chol HaMoed honors the festival (kavod ha'moed).

Practice:
• Wear clothes you would not wear for dirty chores — clean, pressed, or festive attire.
• Some avoid new clothing that would require a Shehecheyanu on Chol HaMoed (ask your rav).
• Men: many wear a hat and jacket for shul even if weekday dress is casual.

This applies each day of Chol HaMoed — it is a simple daily way to mark the moed apart from ordinary weekdays.


#### cholHamoedMatzahExplanation

Eating matzah on Pesach after the Seder night(s) is a mitzvah many observe, though not with the same strict obligation as the first Seder night(s).

Levels:
• First Seder night: Torah obligation (k'zayit) for men and women.
• Rest of Pesach including Chol HaMoed: Rabbinic command (if you have a second seder) and optional mitzvah to eat matzah every day of Pesach. Most will eat matzah at all Yom Tov and Shabbat meals.

How:
• Use shmurah matzah for the seder if available.
• A kezayit per meal is sufficient.
• No chametz or kitniyot (per your custom) the entire Pesach.


#### kiddushLevanaExplanation

Kiddush Levana (Sanctification of the New Moon) — Birkat HaLevanah. Men are obligated in this time-bound positive mitzvah; women are exempt and the widespread custom is that women do not recite it at all (see Deracheha link below).

$waitLine

Deadline: The window ends at the moment of the full moon (roughly 14 days, 18 hours, and 22 minutes from the molad — about 14.75 days into the month). Saying it on the night of the 15th may already be too late depending on the month. This app uses the Hebrew calendar day as a rough guide only — always check Sof Zman Kiddush Levana for your location before the month ends.

When:
• After nightfall (tzeit), standing outdoors under the open sky.
• Ideally on Motzei Shabbat while still in nice clothes — a widespread custom because you are already dressed up.
• In Av, many wait until after Tisha B'Av; in Tishrei, many wait until after Yom Kippur — but say sooner on Motzei Shabbat if waiting risks cloudy nights.
• Not on Shabbat or Yom Tov itself.

How:
• The moon must be clearly visible — if clouds block it, wait for a clear night within the window.
• Use your siddur; it praises G-d for creation — not worship of the moon.

Window closes at the full moon (~14.75 days from the molad). Check Sof Zman Kiddush Levana for your location.


#### ldovidExplanation

Psalm 27 (לְדָוִד ה' אוֹרִי וְיִשְׁעִי) is added to Shacharit during Elul and Tishrei — a season of drawing close to G-d before and through the Days of Awe.

When:
• Said after the morning service (often after Shacharit Amidah, before concluding prayers — follow your siddur).
• Start and end dates vary by minhag — this item follows your nusach setting.

Why:
• "The L-rd is my light and my salvation — whom shall I fear?" (Tehillim 27:1) — trust in divine protection through judgment season.
• A widespread custom in Elul–Tishrei; widely adopted across Ashkenaz, Sephard, and Edot HaMizrach with different calendars.


#### nineDaysAshkenazExplanation

The Nine Days (from 1 Av until after Tisha B'Av) are the strictest part of summer mourning in Ashkenazi custom.

From 1 Av:
• Meat & wine: forbidden entirely, except on Shabbat or at a seudat mitzvah (e.g. brit milah, siyum — ask your rav).
• Laundry: washing, ironing, and wearing freshly laundered outer clothing are prohibited.
• Bathing: bathing or showering for pleasure is prohibited.
• Home & shopping: buying new clothes or beginning major home improvements or repairs is forbidden.

${nineDaysSharedHalacha()}

After the fast (10 Av): Ashkenazi custom continues meat, wine, music, laundry, and bathing for pleasure until chatzos (halachic midday) on 10 Av.

Nine Days Havdalah: On Motzei Shabbat during the Nine Days, use wine or grape juice for Havdalah. Ashkenazi custom (Rema O.C. 551:10): ideally a child (ages 6–9) drinks the cup; if none is present, the one reciting Havdalah drinks it — the mitzvah of Havdalah overrides the custom of restraint.


#### nineDaysChabadExplanation

The Nine Days (from Rosh Chodesh Av until after Tisha B'Av) follow strict Ashkenazi mourning in Chabad practice.

From Rosh Chodesh Av:
• Meat & wine: prohibited entirely, except on Shabbat or at a seudat mitzvah (e.g. brit milah, siyum).
• Laundry, bathing & home: traditional Ashkenazi prohibitions against laundering, bathing for pleasure, home improvements, and buying new garments are observed.

${nineDaysSharedHalacha()}

After the fast (10 Av): follow accepted Chabad psak on meat, wine, music, laundry, and bathing until chatzos on 10 Av — ask your rav if unsure.

Nine Days Havdalah: use wine or grape juice; Ashkenazi-style custom often gives the cup to a child (ages 6–9) when possible.


#### nineDaysSephardicExplanation

The Nine Days and the week of Tisha B'Av (shavuah she'chal bo) are the strictest part of summer mourning for Sephardic communities following Shulchan Arukh — generally more lenient than Ashkenazim until the week of Tisha B'Av.

From Rosh Chodesh Av (1 Av):
• Haircuts: usually prohibited from Rosh Chodesh Av, or only during the actual week of Tisha B'Av — follow your rav.

From the week in which Tisha B'Av falls (shavuah she'chal bo):
• Meat & wine: prohibited from the start of that week (not necessarily the full Nine Days). Some communities (e.g. Syrian, Mashadi) are strict from Rosh Chodesh Av.
• Laundry & bathing: restrictions on washing clothes and bathing for pleasure apply during the week of Tisha B'Av.

${nineDaysSharedHalacha()}

After the fast: many Sephardim follow similar post-fast restrictions until chatzos on 10 Av — confirm with your rav before resuming meat and wine.


#### pesachWeekPrepExplanation

Pesach begins in about a week — use this time for practical preparation (not spring cleaning every closet, but removing chametz where it matters).

Focus areas:
• Kitchen and dining — where chametz is eaten: counters, stove, microwave, toaster, fridge, pantry.
• Cars, offices, bags, coat pockets — crumbs and snack wrappers.
• Sell or use up chametz food you will not keep; plan Pesach menus and shopping.
• Haggadahs, matzah, wine, seder plate — order before prices rise and stores sell out.

Kashering vessels for Pesach:
• Year-round pots, pans, dishes, and utensils absorb chametz from hot foods and long use. For Pesach you need either kashered vessels or a separate Pesach set.
• Common methods (each item has rules — ask your rabbi): hagalah (immersion in boiling water), irui (pouring boiling water on surfaces), libun (heat for metal that cannot be kashered by hagalah).
• Start early — kashering the whole kitchen takes time. Many families use only their Pesach dishes and keep year-round dishes sealed away for the week.

Pesach dishes vs selling chametz dishes:
• Unpack or buy a dedicated Pesach set; label milchig/fleishig if needed.
• Many people include the chametz residue and absorbed flavor within their year-round dishes and pots in the sale, locking them away securely. The physical dishes themselves are not sold, avoiding the halachic requirement to re-immerse them in a Mikveh (tevilat kelim) after Pesach (Shulchan Arukh Y.D. 120). Rabbis structure the contract accordingly — list items clearly and do not use sealed dishes until buy-back.

Mechirat chametz (plan this week):
• Authorize the sale with your rabbi or community several days before Erev Pesach — most rabbis stop accepting sale forms by the night before Erev Pesach, even though the sale takes effect on Erev Pesach morning. Many accept online forms through your local rabbi or kashrut council. The sale must be valid halachically; follow your rabbi's instructions on what to include and when it takes effect.
• Selling transfers ownership so chametz you are not destroying can sit sealed in your home without belonging to you during Pesach.

After Pesach — chametz owned by a Jew during Pesach:
• Chametz that belonged to a Jew over Pesach becomes forbidden (chametz she'avar alav haPesach) — you may not own it, eat it, or otherwise benefit from it even after Pesach ends.
• That is why the sale must be real, and why you must not use sold chametz during Pesach. Ask your rav about repurchase timing and about store-bought chametz after Pesach if you are unsure.

Halachic mindset:
• You need not make the home sterile — remove edible chametz and clean places where chametz was brought.
• Bedikat chametz and biur happen on Erev Pesach — this week sets you up to succeed.

Children & family: assign zones, check school bags, plan chametz finish-up meals before Pesach.


#### roshChodeshExplanation

Chanukah night $day of 8 — lighting the menorah.

When:
• Light after tzeit (nightfall) — not before sunset. On Friday, light Chanukah candles before Shabbat candles (approx. 20–25 minutes before nightfall).
• Friday candle size warning: Because Chanukah candles must burn for at least 30 minutes after nightfall (tzeit), standard small Chanukah candles cannot be used on Friday afternoon — they will burn out before nightfall and invalidate the mitzvah. Use larger candles (like standard Shabbat candles) or pour enough oil to burn for at least ~90 minutes from lighting (often longer if you light well before sunset) so they survive well past dark.
• On Motzei Shabbat, light Chanukah before or after Havdalah per minhag.
• Motzei Shabbat lighting: If your custom is to light Chanukah candles before formal Havdalah over wine, you must terminate Shabbat first — recite Atah Chonantanu in the Maariv Amidah, or say "Baruch hamavdil bein kodesh l'chol" aloud before striking a match. Melacha remains forbidden until Shabbat has ended.

How:
• Pirsumei nisa — publicize the miracle; place menorah by a window or doorway where passersby can see.
• Placement: Insert candles from right to left in the menorah (universal custom per Shulchan Arukh O.C. 676:5 — Ashkenaz, Sephard, and Chabad).
• Lighting: Light from left to right, always starting with the newest candle (the leftmost one) and moving toward the right. Use the shamash to light each candle.
• Oil or candles must burn at least 30 minutes after nightfall.
• Brachot (first night all three; other nights two): lehadlik ner shel Chanukah, she'asa nissim; Shehecheyanu on first night.
• Do not use the Chanukah lights for work — shamash is for utility light.

Prayers & meals:
• Insert Al HaNissim into every Amidah and into Birkat Hamazon (bentching) all day long.

${if (day == 1) "First night: say all three brachot including Shehecheyanu." else "Tonight: two brachot (no Shehecheyanu unless first time lighting this year)."}

After lighting: sing HaNeiros halalu and Maoz Tzur (custom).


#### roshChodeshFullHallelAshkenazNote

Ashkenaz — Full Hallel (Chanukah):
Recite the blessings before Full Hallel as on Chanukah (Mishnah Berurah 422:8).


#### roshChodeshFullHallelChabadNote

Chabad — Full Hallel (Chanukah):
Follow Ashkenazic custom — recite the blessings before Full Hallel (Alter Rebbe's Siddur).


#### roshChodeshFullHallelChanukahExplanation

Rosh Chodesh falls during Chanukah — recite Full Hallel at Shacharit (the Chanukah obligation), not the usual Half Hallel of Rosh Chodesh (Peninei Halakha 12-02-07).

Before Musaf:
Remove tefillin before Musaf — do not wear tefillin during the Musaf Amidah.

Tachanun is omitted.


#### roshChodeshFullHallelChanukahExplanationFemale

Rosh Chodesh during Chanukah — if you recite Hallel, say Full Hallel at Shacharit (the Chanukah practice), not Half Hallel (Peninei Halakha 12-02-07). Optional for women — follow your minhag.

Tachanun is omitted.


#### roshChodeshFullHallelSephardNote

Sephardi — Full Hallel (Chanukah):
Recite the blessings before Full Hallel as on Chanukah (Shulchan Arukh O.C. 422:2).


#### roshChodeshHalfHallelAshkenazNote

Ashkenaz — Half Hallel:
Recite the blessings before Half Hallel on Rosh Chodesh (Mishnah Berurah 422:8).


#### roshChodeshHalfHallelChabadNote

Chabad — Half Hallel:
Follow Ashkenazic custom — recite the blessings before Half Hallel (Alter Rebbe's Siddur; ask your rav if unsure).


#### roshChodeshHalfHallelExplanation

Recite Half Hallel after the Shacharit Amidah on Rosh Chodesh (a cherished custom; not a Torah obligation — Peninei Halakha 05-01-12).

Before Musaf:
Remove tefillin before Musaf — do not wear tefillin during the Musaf Amidah.

Tachanun is omitted on Rosh Chodesh.


#### roshChodeshHalfHallelExplanationFemale

Recite Half Hallel after Shacharit on Rosh Chodesh if you say Hallel — a cherished custom, not obligatory for women (Peninei Halakha 05-01-12). Many Ashkenazi women omit Hallel; many Sephardi women recite it. Follow your community.

Tachanun is omitted on Rosh Chodesh.


#### roshChodeshHalfHallelSephardNote

Sephardi — Half Hallel:
Do not recite a blessing over Partial (Half) Hallel — say the psalms without a bracha (Shulchan Arukh O.C. 422:2; Peninei Halakha 05-01-12).


#### roshChodeshObservancesExplanation

Rosh Chodesh (ראש חודש) — the New Month — is a semi-holiday with extra prayer and customs.

Festive meal (mitzvah):
• It is a mitzvah to increase your meal on Rosh Chodesh — at minimum add an extra dish or special food in honor of the day (Shulchan Arukh O.C. 419:1).
• Have the meal during the day. Poskim write this commemorates the feast the Sanhedrin held at Beit Ya'zek for witnesses who came to testify they saw the new moon (Mishnah Rosh Hashanah 2:5; Orchos Chaim and Kol Bo, cited on O.C. 419).
• Money spent on Rosh Chodesh meals — like Shabbat and Yom Tov — is not deducted from the income allotted to you on Rosh Hashanah; if you spend more for these mitzvos, Heaven adds to your allotment (Pesikta de-Rav Kahana, cited in Tur O.C. 419 and Magen Avraham 419:1).

Davening today (listed in your Morning, Afternoon, and Evening Prayer sections):
• Yaaleh V'yavo in Shacharit, Mincha, and Maariv Amidah — and in bentching when you eat bread
• Half Hallel at Shacharit (Full Hallel if Rosh Chodesh falls during Chanukah; no Hallel on Rosh Chodesh Tishrei / Rosh Hashanah)
• Musaf after Shacharit — remove tefillin before Musaf
• Tachanun is omitted all day

Other customs:
• Fasting and eulogies are generally not done on Rosh Chodesh
• Widespread custom: married women refrain from certain melacha (needlework, laundry, etc.) as an extra mark of honor — ask your rav for details

When Rosh Chodesh spans two days (30th of the previous month and 1st), observances apply to both days.


#### roshChodeshObservancesExplanationFemale

Rosh Chodesh (ראש חודש) — the New Month — is a semi-holiday with extra prayer and customs.

Festive meal (mitzvah):
• It is a mitzvah to increase your meal on Rosh Chodesh — at minimum add an extra dish or special food in honor of the day (Shulchan Arukh O.C. 419:1).
• Have the meal during the day. Poskim write this commemorates the feast the Sanhedrin held at Beit Ya'zek for witnesses who came to testify they saw the new moon (Mishnah Rosh Hashanah 2:5; Orchos Chaim and Kol Bo, cited on O.C. 419).
• Money spent on Rosh Chodesh meals — like Shabbat and Yom Tov — is not deducted from the income allotted to you on Rosh Hashanah; if you spend more for these mitzvos, Heaven adds to your allotment (Pesikta de-Rav Kahana, cited in Tur O.C. 419 and Magen Avraham 419:1).

Davening today (listed in your Morning, Afternoon, and Evening Prayer sections):
• If you recite Shacharit, Mincha, or Maariv Amidah — add Yaaleh V'yavo in Retzei. Shacharit/Mincha: correct per timing (insert in Retzei, return to the beginning of Retzei if already concluded, or repeat only that Amidah if finished). Maariv on Rosh Chodesh only: do not repeat if forgotten after Retzei (Berachot 30b; SA O.C. 422:1)
• If you say Birkat Hamazon when you eat bread — add Yaaleh V'yavo there too
• Ashkenazi custom — most authorities obligate Shacharit and Mincha Amidah on these days
• Sephardic custom — many women fulfill the daily obligation with one prayer; if you daven an extra Amidah and forget Yaaleh V'yavo, ask your rabbi about a voluntary (nedavah) stipulation
• Half Hallel at Shacharit if you say Hallel (optional — follow your minhag; Full Hallel if Rosh Chodesh falls during Chanukah)
• Tachanun is omitted all day

Other customs:
• Fasting and eulogies are generally not done on Rosh Chodesh
• Widespread custom: married women refrain from certain melacha (needlework, laundry, etc.) as an extra mark of honor — ask your rav for details

When Rosh Chodesh spans two days (30th of the previous month and 1st), observances apply to both days.


#### shavuotWeekPrepExplanation

The week before Shavuot is for practical preparation — Shavuot celebrates Matan Torah (receiving the Torah at Sinai).

Food & home:
• Dairy is a cherished Shavuot minhag (cheesecake, blintzes, lasagna, ice cream) — plan menus and shop early. A festive meat meal with wine remains standard practice to fulfill the primary mitzvah of Simchat Yom Tov (Shulchan Arukh O.C. 529:2); many families have a dairy kiddush or snack, then a full meat Yom Tov meal.
• Some decorate with flowers and greenery (minhag of a garden around Sinai).
• Stock wine, grape juice, and Yom Tov staples for festive meals${if (!profile.isInIsrael) " — in the Diaspora, prepare for two days of Yom Tov" else ""}.

Torah & tefillah:
• Confirm shul times for Maariv, Shacharit, and Musaf; many communities have all-night learning (Tikkun Leil Shavuot) — find a program or plan a home study session.
• Choose texts to learn: Ruth (read on Shavuot), Megillat Rut customs, Pirkei Avot, or a topic your family enjoys.
• Review Akdamut / Yizkor customs if your community observes them on the second day (Diaspora).

Joy & family (simchat Yom Tov):
• Plan festive meat meals with wine — the core Simchat Yom Tov obligation — alongside any dairy minhag.
• Gifts for wife and children (clothes, treats) l'fi mamono — associate the day with joy.

Practical:
• On Erev Shavuot: daytime preparations can continue up until nightfall (tzeit), as the holiday begins later than usual. Reheating and cooking for the night meal may also be done on the holiday itself after tzeit, strictly from a pre-existing flame. Set up a blech or hot plate if needed. You cannot light candles or make Kiddush until tzeit.
• Turn off devices before Yom Tov — this app is for prep, not use on chag.


#### sheminiAtzeretExplanation

Shemini Atzeret / Simchat Torah (22 Tishrei in Israel) — one day combining both.

Yom Tov:
• Full Yom Tov — no melacha; festive meals with Kiddush and Shehecheyanu (if not said already).
• Sukkot has ended — in Israel, do not eat or sleep in the sukkah today; festive meals are indoors (not in the sukkah).

Davening highlights:
• Liturgical shift: During Musaf today, the entire Jewish world officially transitions to the winter prayer cycle, universally inserting "Mashiv HaRuach U'Morid HaGeshem" into the second blessing of the Amidah. Tefillat Geshem (the formal prayer for rain) is recited in Musaf.
• Yizkor — memorial prayer in many Ashkenaz communities.
• Hakafot — dancing with Torah scrolls; finish the annual Torah reading and begin Bereshit again.
• Synagogue note: Because drinking often occurs during daytime hakafot, many synagogues move the Priestly Blessing (Birkat Kohanim) up to the early morning Shacharit service instead of keeping it in Musaf, so Kohanim are completely sober for the blessing.
• Full Hallel and Musaf; Yom Tov Amidah.

Simchat Torah joy:
• Everyone receives an aliyah in many shuls; Kol HaNearim (children's aliyah) with flags — an adult (or a boy over bar mitzvah) stands with the group and recites the Torah blessing aloud so the aliya is halachically valid.
• Singing, dancing — honor the day with Torah celebration.

Evening: Candle lighting and Yom Tov; morning services are long — plan accordingly.

Shemini Atzeret (22 Tishrei in the Diaspora) — eighth day of Sukkot.

Yom Tov:
• Full Yom Tov — no melacha; Kiddush and festive meals.
• Sukkah in the Diaspora: Due to safek dyoma (halachic doubt which day is which), Diaspora Ashkenazim are required to eat all major meals in the sukkah on Shemini Atzeret, though leishev basukkah is omitted entirely. Sephardic and Chabad customs vary — confirm with your rav.

Davening:
• Liturgical shift: During Musaf today, the entire Jewish world officially transitions to the winter prayer cycle, universally inserting "Mashiv HaRuach U'Morid HaGeshem" into the second blessing of the Amidah. Tefillat Geshem is recited in Musaf.
• Yizkor — Ashkenaz communities recite memorial prayers.
• Full Hallel and Musaf; Yom Tov Amidah.

Simchat Torah is tomorrow (23 Tishrei) in the Diaspora — see that day's checklist item for hakafot and Torah joy.

Tonight: light Yom Tov candles; no lulav on Shemini Atzeret.

Simchat Torah (23 Tishrei in the Diaspora) — rejoicing with the Torah.

Yom Tov:
• Full Yom Tov — no melacha; Kiddush, festive meals, joy.
• Sukkah: Most Ashkenazim do not eat in the sukkah on Simchat Torah (it is no longer Sukkot) — Sefardic practice varies; follow your rav.
• Davening: Full Hallel and Musaf (like other Yom Tov days).

The mitzvah of the day — simcha and Torah:
• Hakafot — circling the bimah with Torah scrolls, singing and dancing (often many circuits).
• Synagogue note: Because drinking often occurs during daytime hakafot, many synagogues move the Priestly Blessing (Birkat Kohanim) up to the early morning Shacharit service instead of keeping it in Musaf, so Kohanim are completely sober for the blessing.
• Complete the last parsha of Devarim and begin Bereshit — the Torah never ends.
• Aliyot — Kol HaNearim (all children together; an adult or bar mitzvah boy recites the blessing with them), Chatan Torah, Chatan Bereshit, and honors for members.
• Many shuls extend Hakafot to the evening or multiple sessions.

Practical tips:
• Dress festively; arrive early for a good spot.
• If you have a Torah honor, prepare your brachot.
• Celebrate responsibly — the day is about love of Torah, not excess.

Candle lighting last night of Yom Tov; havdalah when Yom Tov ends (often with Sukkot/Yom Tov additions in havdalah — use your siddur).

Hoshana Raba prep (tomorrow — 21 Tishrei):
• Buy or prepare a dedicated bundle of five fresh willow branches (aravot) for tomorrow morning — separate from your daily lulav set.
• At the conclusion of synagogue services tomorrow, the community performs Chagizat Aravah — striking these branches against the ground five times (Minhag Nevi'im). Hoshana Raba is the final sealing of judgment from Yom Kippur.

Chol HaMoed (חול המועד) — the intermediate festival days — is not full Yom Tov, but it is not ordinary weekdays either.
$hoshanaRabaBlock

Spirit of the day:
• Simchat moed — joy of the festival; nicer meals, family time, Torah learning.
• Melacha — many labors are restricted (not as strict as Yom Tov). Work needed to avoid financial loss may be permitted — ask your rabbi before assuming.
• Ochel nefesh — food preparation is permitted.

What to do:
$hallelBlock
• Avoid treating it like a regular workday — schedule outings, learning, and visits that fit the moed.
$festivalLines

Avoid (without halachic guidance): heavy laundry, major home projects, shaving, haircuts, and activities that diminish the festival atmosphere.
• Grooming restrictions: Shaving and getting a haircut are strictly prohibited on Chol HaMoed (Shulchan Arukh O.C. 531) — unless under highly specific conditions, such as a boy turning three or leaving prison (ask your rav). This applies even if you want to look clean for the remaining days of the festival.


#### simchasYomTovPrepBlock

Simchas Yom Tov (שִׂמְחַת יוֹם טוֹב) — the mitzvah to rejoice on the festival:
The Torah commands "V'samachta b'chagecha" — "And you shall rejoice in your festival" (Devarim 16:14). Joy on Yom Tov is a mitzvah for everyone. Halacha recognizes that happiness is personal — the head of household helps each family member rejoice in the way that naturally brings them joy (Pesachim 109a; Shulchan Arukh O.C. 529:2).

Prepare before chag begins:
• Wife — clothing or jewelry l'fi mamono (within your financial ability): the Shulchan Arukh obligates a husband to buy his wife new clothes or jewelry for Yom Tov. You are not expected to go into debt — an inexpensive but pleasant garment, modest jewelry, or nice shoes fulfills the mitzvah. The goal is her actual joy; if she prefers something else that genuinely makes her happy (a book, houseware, experience), many modern poskim agree that fulfills the spirit of the law.
• Children — treats that bring joy: the Talmud mentions roasted grains and nuts; today candies, chocolates, and age-appropriate toys. The purpose is to associate Yom Tov with excitement and a break from everyday routine — not merely to give sugar.
• Men — meat and wine: in Temple times this meant holiday sacrifices; today festive Yom Tov meals with wine and meat (beef is classic; poultry is often acceptable) fulfill personal simchas Yom Tov.

Rambam's crucial condition (Hilchos Yom Tov 6:18):
When you feed your family, buy your wife clothes, and give your children treats, you must also provide for the poor, the widow, and the orphan. If a household feasts only behind closed doors without helping the needy, the Rambam writes this is not "the joy of a mitzvah" but merely "the joy of one's stomach."


#### simchatTorahExplanation

Simchat Torah (23 Tishrei in the Diaspora) — rejoicing with the Torah.

Yom Tov:
• Full Yom Tov — no melacha; Kiddush, festive meals, joy.
• Sukkah: Most Ashkenazim do not eat in the sukkah on Simchat Torah (it is no longer Sukkot) — Sefardic practice varies; follow your rav.
• Davening: Full Hallel and Musaf (like other Yom Tov days).

The mitzvah of the day — simcha and Torah:
• Hakafot — circling the bimah with Torah scrolls, singing and dancing (often many circuits).
• Synagogue note: Because drinking often occurs during daytime hakafot, many synagogues move the Priestly Blessing (Birkat Kohanim) up to the early morning Shacharit service instead of keeping it in Musaf, so Kohanim are completely sober for the blessing.
• Complete the last parsha of Devarim and begin Bereshit — the Torah never ends.
• Aliyot — Kol HaNearim (all children together; an adult or bar mitzvah boy recites the blessing with them), Chatan Torah, Chatan Bereshit, and honors for members.
• Many shuls extend Hakafot to the evening or multiple sessions.

Practical tips:
• Dress festively; arrive early for a good spot.
• If you have a Torah honor, prepare your brachot.
• Celebrate responsibly — the day is about love of Torah, not excess.

Candle lighting last night of Yom Tov; havdalah when Yom Tov ends (often with Sukkot/Yom Tov additions in havdalah — use your siddur).


#### sukkahBuildExplanation

Building a sukkah (סֻכָּה) is a mitzvah — many begin right after Yom Kippur to show we move from teshuvah to simcha.

Basic requirements (Shulchan Arukh O.C. 633–635):
• Walls: At least two full walls plus part of a third (or three full walls). Walls can be wood, fabric on a frame, or existing walls — they must be stationary and not flap in a normal breeze (Shulchan Arukh O.C. 630:10).
• Fabric / canvas sukkahs: Tie fabric walls down tightly so they do not move or flap in a normal wind, or reinforce with horizontal ropes/straps around the frame spaced less than 3 tefachim apart (lavud) to create valid stationary partitions — ask your rav. Pop-up kits that billow are a common beginner pitfall.
• Size: Large enough to fit a table and for an adult to sit with head and most of body inside (roughly 7×7 handbreadths minimum; build larger for comfort).
• Schach (covering): Must be plant material that grew from the ground and is detached (tree branches, bamboo mats certified for schach, etc.) — not metal, plastic, or a solid roof. Schach rules: Material must be detached plant life that has NOT been manufactured into a functional tool, vessel, or finished item (such as old furniture parts or wooden slats from crates), as finished objects are susceptible to ritual impurity (kabalat tumah) and are completely invalid for schach.
• Shade: More shade than sun on the floor under the schach. You may see some sky through gaps — that is fine.
• Height: Walls at least ~3 ft (10 tefachim); schach can be high, but very high sukkot still need a valid structure below.

Practical steps:
1. Choose a spot — backyard, porch, balcony (check building rules). Avoid placing schach directly under a tree or house roof overhang that blocks the mitzvah.
2. Build the frame and walls first; lay schach last so it stays dry and valid.
3. Arrange schach loosely with gaps — not a solid rainproof roof (use a removable slatted cover on top only if rain is expected, per halachic guidance).
4. Set up table, chairs, and decorations (fruit, lights — electric on Yom Tov/Chol HaMoed per your rav).
5. Before the first night of Sukkot, eat at least a kezayit of bread in the sukkah with the bracha leishev basukkah (first night is Torah-level for men; women per minhag).

First night rain protocol (if it rains on the first night):
• Ashkenaz (Rema O.C. 639:5): Wait up to one hour. If rain does not stop, enter the sukkah, make Kiddush, eat a kezayit of bread without leishev basukkah, then finish the rest of the meal indoors.
• Sephardic (Shulchan Arukh): If rain is heavy enough to spoil the meal, you are exempt from eating the full meal in the sukkah — make Kiddush and eat the full meal indoors from the start, without leishev basukkah. Nevertheless, many Sephardic decisors (including Yalkut Yosef) rule that it remains proper and praiseworthy at the very end of the meal to go out to the sukkah and eat a single kezayit of bread without reciting leishev basukkah — honoring the first night's unique obligation in part.

First night: Kiddush and Yom Tov meal in the sukkah. Men: leishev basukkah (sit in the sukkah — a blessing said before eating bread in the sukkah). Throughout Sukkot: eat bread and sleep in sukkah when possible (rain and illness have exemptions — ask your rabbi).


#### sukkotWeekPrepExplanation

The week before Sukkot (Tishrei 8–13) is for building joy and getting the mitzvot ready — many begin the sukkah right after Yom Kippur.

Sukkah:
• Build or repair your sukkah — walls, frame, and schach (plant covering, not metal or solid roof).
• Choose a spot with more shade than sun under the schach; avoid blocking by trees or porch roofs.
• Set up table, chairs, and decorations; plan to eat (and sleep, when possible) in the sukkah all seven days.

Arba Minim (Four Species):
• Order or buy a kosher set: lulav, etrog, hadassim (myrtle), aravot (willow).
• Check the etrog (pitom intact if present) and that leaves are fresh — buy early before stores sell out.
• Get a lulav holder (hoshanah holder / koisan) and an etrog box.

Meals & Yom Tov:
• Plan menus for seven days of festive meals in and out of the sukkah.
• Wine, grape juice, challah, and Yom Tov groceries — ${if (profile.isInIsrael) "one day of Yom Tov at the start" else "two days of Yom Tov at the start in the Diaspora"}.
• First night: Kiddush and bread in the sukkah; men say leishev basukkah before eating bread (women per minhag). If it rains on the first night: Ashkenaz — wait up to one hour, then eat a kezayit in the sukkah without leishev basukkah and finish indoors (Rema O.C. 639:5); Sephardic — if rain spoils the meal, eat the full meal indoors from the start (Shulchan Arukh), but many Sephardic decisors (including Yalkut Yosef) hold it is proper at the end of dinner to walk out to the sukkah and eat a kezayit of bread without leishev basukkah.

Joy & family:
• Simchat Yom Tov — plan treats for children, festive clothing, and meals that bring household joy.

Practical:
• Hoshana Raba prep (21 Tishrei): Buy or prepare a dedicated bundle of five fresh willow branches (aravot) for Hoshana Raba morning — separate from your daily lulav set. At the conclusion of synagogue services, the community performs Chagizat Aravah, striking these branches against the ground five times (Minhag Nevi'im).
• Confirm shul times for Hallel and Hoshanot throughout Sukkot, and for hakafot on Hoshana Raba.
• Turn off devices before Yom Tov — this app is for prep, not use on chag.


#### threeWeeksAshkenazExplanation

${threeWeeksIntro()}

Ashkenazi custom observes a longer, stricter mourning throughout the Three Weeks, intensifying during the Nine Days.

From 17 Tammuz (general Three Weeks):
• Haircuts & shaving: prohibited for the entire Three Weeks.
• Music: instrumental music is not listened to throughout the period.
• Weddings: not held.
• Shehecheyanu: traditionally not recited on new clothes or new fruits; permitted on Shabbat.

From 1 Av (Nine Days): restrictions intensify — see the Nine Days checklist item for meat, wine, laundry, bathing, and home practices.


#### threeWeeksChabadExplanation

${threeWeeksIntro()}

Chabad follows strict Ashkenazi mourning customs, with specific emphasis from the Lubavitcher Rebbe on spiritual growth during this period.

From 17 Tammuz (general Three Weeks):
• Haircuts, music & weddings: prohibited throughout the entire Three Weeks.
• Shehecheyanu: avoided entirely, except on Shabbat or when required for a mitzvah (e.g. brit milah).
• Torah & charity: increase Torah study — especially subjects about the Holy Temple's layout and construction — and give extra tzedakah during this time.

From Rosh Chodesh Av (Nine Days): restrictions intensify — see the Nine Days checklist item for meat, wine, laundry, bathing, and home practices.


#### threeWeeksSephardicExplanation

${threeWeeksIntro()}

Sephardic and Edot HaMizrach communities, following Shulchan Arukh, generally take a more lenient approach than Ashkenazim during the early Three Weeks.

From 17 Tammuz (general Three Weeks):
• Haircuts & shaving: permitted during most of the Three Weeks; shaving is usually prohibited only during the week in which Tisha B'Av falls (shavuah she'chal bo).
• Music: live or recorded music is avoided.
• Weddings: some communities avoid weddings from 17 Tammuz; others are lenient and avoid them only from Rosh Chodesh Av — follow your kehilla.
• Shehecheyanu: avoided on new items for the duration of the period.

From Rosh Chodesh Av or the week of Tisha B'Av: additional restrictions apply — see the Nine Days checklist item. Some communities (e.g. Syrian, Mashadi) are stricter on meat and wine from Rosh Chodesh Av.


#### tuBshvatExplanation

Tu B'Shvat (15 Shevat) — New Year for Trees — is a day to appreciate Hashem's fruit and Land of Israel.

Liturgical note:
• Tachanun is completely omitted from standard weekday prayers today — as well as during yesterday afternoon's Mincha service (14 Shevat).

Customs:
• Eat fruit — especially the seven species of Eretz Yisrael: wheat, barley, grapes, figs, pomegranates, olives, dates.
• Say brachot and after-brachot carefully.
• Shehecheyanu on fruit: You may say Shehecheyanu only on a seasonal fruit that is genuinely new to the market this season (like fresh pomegranates or figs). Do not say this blessing on fruits grown and sold year-round (like bananas, pineapples, or apples), even if you personally have not eaten one recently.
• Some hold a Tu B'Shvat Seder with four cups of wine (white to red) and themed fruit — follow a guide if hosting.
• No fasting; work is permitted; it is not Yom Tov.

Spiritual focus: gratitude for creation, connection to Eretz Yisrael, and growth (trees blossom in Israel around this season).


#### yaalehVyavoMaarivExplanation

Add Yaaleh V'yavo in the Maariv Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).

${yaalehVyavoForgotMaarivRoshChodesh()}

Also add Yaaleh V'yavo in bentching if you eat bread tonight.


#### yaalehVyavoMaarivExplanationFemale

${yaalehVyavoFemaleAmidahLead("Maariv")}

${yaalehVyavoForgotMaarivRoshChodesh()}

If you say Birkat Hamazon when you eat bread tonight, add Yaaleh V'yavo there too.


#### yaalehVyavoMinchaExplanation

Add Yaaleh V'yavo in the Mincha Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).

${yaalehVyavoForgotShacharitOrMincha("Mincha")}


#### yaalehVyavoMinchaExplanationFemale

${yaalehVyavoFemaleAmidahLead("Mincha")}

${yaalehVyavoForgotShacharitOrMincha("Mincha")}

If you say Birkat Hamazon when you eat bread today, add Yaaleh V'yavo there too.


#### yaalehVyavoShacharitExplanation

Add Yaaleh V'yavo in the Shacharit Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).

${yaalehVyavoForgotShacharitOrMincha("Shacharit")}

Also add Yaaleh V'yavo in bentching if you eat bread today.


#### yaalehVyavoShacharitExplanationFemale

${yaalehVyavoFemaleAmidahLead("Shacharit")}

${yaalehVyavoForgotShacharitOrMincha("Shacharit")}

If you say Birkat Hamazon when you eat bread today, add Yaaleh V'yavo there too.



### ErevPesachPrepText.kt
#### bedikatExplanation

Bedikat chametz (בְּדִיקַת חָמֵץ) — the formal search for chametz — is a rabbinic mitzvah on the night **before** Erev Pesach day (after tzeit when the Hebrew date becomes 14 Nisan).

$whenLine

How to search:
• Use a candle (or flashlight per many poskim), a wooden spoon, and a feather (or paper bag) to gather crumbs.
• Search every room where chametz may have been brought during the year — especially kitchen, dining areas, living room, car, office, children's bags, and coat pockets.
• Check under furniture, cushions, car seats, and appliances where crumbs collect.
• Place ten pieces of bread (wrapped) in rooms before searching if your custom includes finding known pieces (optional minhag). If you hide them, write down every location and verify all 10 are recovered — a missed piece means known chametz remains in your home.

After the search:
• Recite the blessing Al bi'ur chametz and the Kol chamira nullification (bitul) — many siddurim print the text.
• Text difference: Use the night version of Kol Chamira from your siddur. It nullifies only chametz you have not seen and do not know about — because you may still legally own chametz for breakfast tomorrow morning.
• When Erev Pesach is Shabbat, this first bitul is Thursday night after bedikat; the final Kol Chamira is on Shabbat morning before the end of the 5th halachic hour — not at Friday's burning.
• Gather found chametz in a bag to destroy the next morning at biur chametz.
• Eating restrictions: You may not eat a meal or start work after nightfall until you complete the search. Once the search is finished, you may eat normally. Tomorrow morning is biur chametz — avoid a heavy meal from midday (chatzos) onward to preserve your appetite for the Seder.

If you are traveling or staying elsewhere, your host or rabbi can guide which rooms you are responsible to search.


#### biurExplanation

Biur chametz (בִּעוּר חָמֵץ) — destroying chametz — completes the mitzvah of removing leaven before Pesach.

$morningNote
• Burn or destroy all chametz found last night and any remaining chametz you are not selling.
• Many burn chametz in a safe outdoor fire; flushing crumbs in the toilet or similar is acceptable for small amounts per many poskim — ask your rav.
• $zmanNote
• Timeline guardrail: ${if (dow == PesachErevDow.SHABBAT) {
            "On this year's schedule, do not recite the final Kol Chamira at today's burning — keep chametz for Shabbat meals. The final Kol Chamira is recited on Shabbat morning before the end of the 5th halachic hour."
        } else {
            "Both the physical destruction of chametz and the final recitation of Kol Chamira must be fully completed before the end of the 5th halachic hour. Once the 5th hour ends, chametz becomes assur b'hana'ah — you can no longer nullify ownership, and a late Kol Chamira is invalid."
        }}
• Text difference: Use the morning version of Kol Chamira from your siddur — it is structurally different from the night text and completely disowns ALL chametz in your possession, whether you have seen it or not and whether you have destroyed it or not.
• Recite the final Kol Chamira immediately after destruction while still before that deadline${if (dow == PesachErevDow.SHABBAT) " (on Shabbat morning, not at today's burning)" else ""}.

Mechirat chametz:
• Any chametz included in the rabbi's sale should already be sealed and not eaten — only unsold chametz is burned.

After biur:
• Eat only kosher-for-Passover food until Pesach ends.
• Firstborns: Taanit Bechorot earlier per schedule; Seder ${
            when (dow) {
                PesachErevDow.SHABBAT -> "after Shabbat (Motzei Shabbat)"
                PesachErevDow.FRIDAY -> "tonight (first Seder); second Seder Saturday night in the Diaspora (Yaknehaz — Shabbat into second day of Yom Tov)"
                else -> "tonight"
            }
        }.


#### erevPesachFridayBeforeShabbatFullSchedule

$bedikatLeadIn

This year, Erev Pesach is on Friday (14 Nisan) and the first day of Pesach is Shabbat (15 Nisan):

• Bedikat chametz: Thursday night (night of 14 Nisan, after tzeit) — not Friday night. Recite the first bitul (Kol Chamira).
• Taanit Bechorot: Friday daytime (14 Nisan) — fast or attend a siyum.
• Biur chametz: Friday morning (14 Nisan) by the 5th halachic hour deadline. Both physical destruction and the final Kol Chamira nullification must be finished before this time.
• Mechirat chametz: must be entirely completed before Shabbat/Yom Tov candle lighting on Friday evening.
• First Seder: Friday night (commencing 15 Nisan).
• Second Seder (Diaspora only): Saturday night — transitioning directly from Shabbat into the second day of Yom Tov (not a regular weekend Motzei Shabbat). Kiddush includes Yaknehaz (integrated Havdalah: wine, Kiddush, candle, Havdalah text, Shehecheyanu — no spices/besamim).
• Preparation warning: you may NOT do any prep work (chopping, cooking, table setting) on Shabbat day for the second Seder. All preparations must wait until Shabbat ends at nightfall. Eruv tavshilin does not apply when Yom Tov falls on Shabbat — it is only for when Yom Tov immediately precedes Shabbat.

Confirm candle lighting, Yom Tov, and Seder times with your siddur and rav.


#### erevPesachOnShabbatFullSchedule

$bedikatLeadIn

This year, Erev Pesach is on Shabbat (14 Nisan). When Erev Pesach falls on Shabbat, the entire preparatory timeline shifts early (Peninei Halakha ch. 14):

• Taanit Bechorot: Thursday (12 Nisan) — moved early; not on Shabbat or Friday. Ashkenazim fast or attend a siyum. Many Sephardic authorities rule the moved fast is nullified entirely; many still attend a siyum out of custom (ask your rav).
• Bedikat chametz: Thursday night (night of 13 Nisan, after tzeit) with bracha — not the usual night of 14 Nisan, and not on Shabbat. Recite the first bitul (Kol Chamira) immediately after the search.
• Biur chametz: Friday morning (13 Nisan) — burn the chametz found. Do not recite the final Kol Chamira at the burning — you will still eat chametz over Shabbat.
• Mechirat chametz: complete and finalize the sale before Shabbat begins Friday evening.
• Matzah on Friday (13 Nisan): many authorities extend the prohibition of eating regular matzah to Friday as well when Erev Pesach falls on Shabbat, so matzah is eaten with a prime appetite at the Seder (ask your rav).
• Seder prep: all physical cooking, roasting the zeroa (shankbone), checking lettuce/maror, and making charoset must be finished on Friday before Shabbat — you cannot prepare on Shabbat for Motzei Shabbat.
• Shabbat (14 Nisan) — eating deadline: finish eating chametz by the end of the 4th halachic hour on Shabbat morning.
• Shabbat (14 Nisan) — disposal: flush leftover crumbs down the toilet or nullify them chemically (e.g. pour liquid soap over them) before the 5th hour. Do not burn on Shabbat.
• Shabbat (14 Nisan) — final nullification: recite the final Kol Chamira before the end of the 5th halachic hour on Shabbat morning.
• Shabbat meals — lechem mishneh: Sephardim may use egg matzah (matzah ashira). Ashkenazim do not eat egg matzah on Pesach (Rema O.C. 462:4); use small challah rolls with extreme caution over disposable plates, shake out garments completely, and flush all crumbs before the 4th-hour deadline. Seudah shlishit: meat, fish, or fruit — not regular matzah on Erev Pesach.
• First Seder: Saturday night after Shabbat fully ends (tzeit). Kiddush includes the full Yaknehaz sequence (Wine, Yom Tov Kiddush, Ner/candle, Havdalah text, and Shehecheyanu — no spices/besamim).

Plan with your rav and local zmanim — many communities publish a Pesach-on-Shabbat timetable.


#### includePesachScheduleOnBedikatItem

Mechirat chametz (מְכִירַת חָמֵץ) — selling your chametz to a non-Jew through your rabbi before Pesach — lets you keep chametz products locked away without owning them during Pesach (Shulchan Arukh O.C. 448).

Why:
• On Pesach, owning chametz is forbidden (bal yera'eh / bal yimatzei). Selling transfers ownership so sealed chametz in your home does not belong to you during the festival.

How to do it:
• Sign or authorize a sale with your rabbi or community (online forms are common). Deadline warning: While the sale takes effect on Erev Pesach morning, you must authorize your rabbi to sell your chametz several days in advance — most rabbis stop accepting sale forms by the night before Erev Pesach. The rabbi needs time to organize contracts and complete the kinyan (legal transfer) before the halachic deadline.
• Mark cabinets or rooms included in the sale; keep sold chametz separate from what you will burn or discard.
• Do not eat or use sold chametz after the sale takes effect — it belongs to the buyer until buy-back after Pesach.
• Dishes used with chametz: Many people include the chametz residue and absorbed flavor within their year-round dishes and pots in the sale, locking them away securely. The physical dishes themselves are not sold, avoiding the halachic requirement to re-immerse them in a Mikveh (tevilat kelim) after Pesach (Shulchan Arukh Y.D. 120). Rabbis structure the contract accordingly — follow your rabbi's sale form.
• Store the contract or confirmation; many communities sell through a central rabbi (e.g. local kashrut council).

After Pesach: chametz is repurchased per the terms of the sale — follow your rabbi's instructions on when you may use it again.
$urgency


#### taanitBechorExplanation

Second Seder prep (Diaspora this year):
• Saturday daytime is the first day of Yom Tov (Shabbat). Saturday night begins the second day of Yom Tov — not a regular Motzei Shabbat weekend.
• Kiddush at the second Seder includes Yaknehaz (integrated Havdalah: wine, Kiddush, candle, Havdalah text, Shehecheyanu — no spices).
• You may NOT cook, chop, or prepare on Shabbat day for the second Seder. Hachana from weekday across Shabbat to a later Yom Tov is forbidden. Eruv tavshilin does not apply when Yom Tov falls on Shabbat — it is only when Yom Tov immediately precedes Shabbat. All food prep, cooking, and table setting for the second night must wait until Shabbat fully ends Saturday night.

• Omer count trigger: The night after the first Seder (16 Nisan) officially begins Sefirat HaOmer. Before leaving the table on that night, count Day 1 of the Omer — if you miss a full day-and-night cycle, ask your rav about continuing with a bracha (see the Omer checklist item).

• Omer count trigger: The second night of Pesach (second Seder) officially begins Sefirat HaOmer. Before leaving the Seder table on the second night, count Day 1 of the Omer — if you miss a full day-and-night cycle, ask your rav about continuing with a bracha (see the Omer checklist item).

$intro

$sederNights
$secondSederHachana
$omerTrigger

Set up before Yom Tov:
• Matzah — shmurah matzah for motzi/matza mitzvah (three matzot on the plate per custom)
• Maror — romaine, horseradish, or bitter herbs per your minhag
• Four cups of wine per participant (grape juice is widely used if needed — ask your rav; not the same debate as Chol HaMoed wine)
• Haggadah for each person (or shared)
• Seder plate: zeroa (shankbone), beitzah (egg), karpas, charoset, maror, chazeret
• Seder plate prep: You should ideally roast your zeroa (shankbone) on Erev Pesach day before sunset. Because the shankbone is not eaten on Seder night, roasting it after the holiday begins violates Yom Tov cooking laws. The egg (beitzah), however, is traditionally eaten during the meal, so it may legally be boiled or roasted on Yom Tov night if needed.
• Reclining (hasebha): Recline to the left when drinking the four cups and eating matzah, korech, and afikoman — do not recline while eating maror or chazeret (they symbolize slavery).
• Festive table; candles for Yom Tov

Kitchen:
• Only kosher-for-Passover food and utensils from this point
• Warm food on a blech or pre-set timer for Yom Tov meals

At the Seder: follow your Haggadah step by step — Kiddush, the order of the night, brachot, and the four cups are all laid out there. ${if (profile.isInIsrael) "One" else "Two"} Seder night(s) this Pesach.



### PublicFastDayText.kt
#### commonFastLawsBlock

Who must fast:
Jewish adults from bar/bat mitzvah age who are healthy enough to fast. Children are trained gradually — ask your rav. Women who are pregnant, nursing, or recently gave birth, and anyone who is ill, should ask a posek — many eat in measured amounts (shiurim) or are fully exempt.

If you ate by mistake:
If you forgot and ate or drank unintentionally, you may continue fasting once you remember — the fast remains valid (Shulchan Arukh O.C. 568:1).

If you cannot fast:
Do not endanger your health. Ask a rav about shiurim (small amounts at intervals), postponing the fast, or exemption. Pikuach nefesh overrides fasting.


#### erevMinorFastPrepTitle

Tomorrow is $fastName — a public fast from dawn (alot hashachar) until nightfall (tzeit).

If you plan to eat before the fast begins:
• Set a mental condition (tanai) the night before: "If I wake up hungry before dawn, I will eat." Without this condition, waking early and eating may prohibit you from eating again until the fast officially begins at dawn (Shulchan Arukh O.C. 564:1).
• If you wake before dawn and want to eat, you may drink water and eat foods that are not normally cooked for a meal — e.g. a piece of cake, fruit, or cereal. A full hot meal is disputed; many avoid a formal cooked meal once they have decided to fast (Mishnah Berurah 564:8–9).
• Stop all eating and drinking at alot hashachar${alotTomorrow?.let { " (approx. $it — enable location for your exact zman)" } ?: " — enable location in Settings for your dawn time"}.

Practical prep tonight:
• Hydrate well and eat a balanced dinner.
• Plan a lighter morning if you will not eat before dawn.
• Know your synagogue schedule if you plan to attend special prayers.

Who must fast: Jewish adults (bar/bat mitzvah age and older) in good health. Children below bar/bat mitzvah are not required to fast — train them gradually per your rav.$fridayNote



### YomTovShabbatPrepText.kt
#### advanceBlock

Read this today (Friday) before Shabbat candles — the app is not for use on Shabbat.

Tomorrow is Shabbat and erev $tomorrowChag. $tomorrowChag begins tomorrow night at nightfall (Motzei Shabbat), not tonight. Finish Yaknehaz prep, Yom Tov candles from a pre-existing flame, wine, and festive food before Shabbat ends.

Tomorrow is erev $tomorrowChag — the Yom Tov–Shabbat rules below apply starting then. Use today (during the day) to prepare so you are not caught tonight or tomorrow without eruv tavshilin, flames, or food in place.

Pesach meets Shabbat this year — some steps happen today or tonight (before tomorrow's erev Pesach checklist):

$body


#### genericEruvBlock

This year, $chagName meets Shabbat on the calendar — you need eruv tavshilin. This doesn't happen every year.

Why (Peninei Halakha 12:8):
• On Yom Tov, cooking is generally allowed only for that calendar day of Yom Tov, not for the next day.
• When Yom Tov is immediately followed by Shabbat, the Sages required eruv tavshilin so you may cook on Yom Tov for Shabbat, and so Shabbat is not forgotten amid festival preparations.

$whenLine

How (Peninei Halakha 12:8:2):
• Set aside a baked food (challah or matzah) and a cooked food (meat, fish, or unpeeled hard-boiled egg are common examples).
• Recite the blessing and declaration from your Machzor or siddur in any language you understand — use the printed wording; do not paraphrase from memory.
• One eruv per household is sufficient.
• Food must be appropriate to eat with bread; at least a kezayit of cooked food should remain until Shabbat prep is done.
• Storage warning: Put the designated eruv foods in a safe, clearly labeled spot. If the eruv foods are eaten or destroyed before you finish cooking for Shabbat on Friday afternoon, your permission to prepare food is canceled (ask your rav if this happens).

Limits:
• Permits cooking and food prep on Yom Tov for Shabbat only — not cooking on one day of Yom Tov for the next festival day.
• Food should be ready early enough Friday afternoon that it could theoretically be eaten before Shabbat.
• Does not permit non-food Shabbat prep (e.g. certain laundry) — ask your rav.


#### genericHavdalahInKiddushBlock

This year, tomorrow (Shabbat) is Erev $chagName — $chagName begins tomorrow night at nightfall (Motzei Shabbat). This doesn't happen every year.

This year, Shabbat is Erev $chagName — $chagName begins tonight at nightfall (Motzei Shabbat). This doesn't happen every year.

$opener

Havdalah when Shabbat leads into Yom Tov:
• Havdalah is recited when entering a day of lesser holiness. Shabbat is holier than Yom Tov, so when Shabbat leads into a festival, havdalah is included in that night's Kiddush — not as a full separate havdalah with spices before Kiddush.
• Order (mnemonic YaKNeHaZ per many Ashkenaz poskim): Yayin (borei pri hagafen) → Kiddush for Yom Tov → Ner (borei me'orei ha'eish — recite over the Yom Tov candles already lit on the table; do NOT pick up, move, or touch them — they are muktzeh once lit; gaze at the flames from where they stand) → Havdalah (holiday text ending bein kodesh l'kodesh, not bein kodesh l'chol) → Zeman (Shehecheyanu on the first festival night when applicable).
• Spices (besamim) are omitted for this transition.
• Before Kiddush, melacha permitted on Yom Tov but not on Shabbat: many say Baruch hamavdil bein kodesh l'kodesh, or rely on the Vatodi'enu insert in Maariv — follow your Machzor.

$prepWhen: Yom Tov candles from a pre-existing flame; wine; festive meal ready; 48-hour candle or pilot light per your rav.


#### roshHashanaEruvBlock

This year, Rosh Hashana leads into Shabbat on the calendar — you need eruv tavshilin. This doesn't happen every year.

Why (Peninei Halakha 12:8):
• When Rosh Hashana (or its second day in the Diaspora) falls on Friday, Shabbat follows immediately. Without an eruv, you may not cook on Yom Tov for Shabbat.
• The eruv reminds the household to prepare for Shabbat, not only for the Days of Awe.

$whenLine

How to make eruv tavshilin (Peninei Halakha 12:8:2):
• Foods: challah (or matzah) plus cooked food — commonly fish, meat, or an unpeeled hard-boiled egg.
• Blessing: Asher kid'shanu b'mitzvotav v'tzivanu al mitzvat eruv — use your siddur text.
• Declaration: recite the eruv declaration from your Machzor or siddur in any language you understand (traditionally Aramaic; many editions include translation) — use the printed wording; it permits baking, cooking, lighting, and food prep on Yom Tov for Shabbat.
• Storage warning: Put the designated eruv foods in a safe, clearly labeled, visible spot before the Friday afternoon rush — they are easily thrown out by mistake. If the eruv foods are eaten or destroyed before you finish cooking for Shabbat on Friday afternoon, your permission to prepare food is canceled (ask your rav if this happens). Many eat the eruv foods at a Shabbat meal once Shabbat prep is done (lechem mishneh / oneg Shabbat).

Rosh Hashana notes:
• Eruv allows Shabbat **food** prep on Friday Yom Tov — honey cake, challah, fish, soup, etc. — not melacha forbidden on Yom Tov itself.
• If the first day(s) were Thursday–Friday in the Diaspora, only **Friday** Yom Tov cooking for Shabbat uses this eruv — the eruv does not permit Thursday cooking for Friday Yom Tov.
• Confirm communal eruv from your rabbi does not replace your household eruv for personal cooking — many poskim require each home to make its own (ask your rav).

Shofar & davening: eruv does not change shofar rules — shofar is blown on Yom Tov days of Rosh Hashana when not Shabbat, per your minhag and calendar.


#### roshHashanaHavdalahInKiddushBlock

This year, tomorrow (Shabbat) is Erev Rosh Hashana — Rosh Hashana begins tomorrow night at nightfall (Motzei Shabbat). This doesn't happen every year.

This year, Shabbat is Erev Rosh Hashana — Rosh Hashana begins tonight at nightfall (Motzei Shabbat). This doesn't happen every year.

$opener

Havdalah inside Kiddush (Yaknehaz):
• When a biblical holiday begins Saturday night (including Rosh Hashana), that night's Kiddush incorporates havdalah for Shabbat. The order is: (1) wine blessing, (2) holiday Kiddush, (3) candle blessing, (4) havdalah blessing — with the Yom Tov text concluding bein kodesh l'kodesh (not bein kodesh l'chol), (5) Shehecheyanu for the festival.
• This is the YaKNeHaZ order (Yayin, Kiddush, Ner, Havdalah, Zeman) codified in Shulchan Arukh for Motzei Shabbat into Yom Tov.
• Spices are not used.
• Use the Machzor or siddur nusach for Rosh Hashana — do not rely on memory for the long havdalah text.

Before or at Maariv:
• You may say Baruch hamavdil bein kodesh l'kodesh to begin Yom Tov-permitted activities before Kiddush, or rely on Vatodi'enu in the Amidah of Maariv — follow your community.

$prepLead:
• Have round challah, honey, apples, and symbolic foods ready for the Yom Tov meals after Shabbat (minhag).
• Confirm shofar and Musaf times for the first day(s) of Rosh Hashana after Shabbat — shofar is not blown on Shabbat itself.
• Tashlich: when the first day of Rosh Hashana is Shabbat, it is postponed to Sunday (structural rule — avoiding carrying machzorim in public without an eruv).

Candles: after Shabbat ends, light Yom Tov candles from a flame lit before Shabbat began (pre-existing flame).


#### yomTovFridayCookingBlock

This year, today is Rosh Hashana on Friday and Shabbat begins tonight — the Yom Tov–Shabbat sequence is in progress.

If you made eruv tavshilin before Rosh Hashana began:
• You may cook and otherwise prepare food on Yom Tov today for Shabbat meals, within the limits above (food ready with time before Shabbat; no cooking for weekday or for the previous Yom Tov day).
• Many prepare honey challah, fish, soup, and other Shabbat/Rosh Hashana dishes today.
• Eruv check: Double-check that your eruv tavshilin foods are in a safe, visible, labeled spot — they are easily discarded during a frantic Friday afternoon prep rush.

Shofar: blown today (if today is a Yom Tov day of Rosh Hashana and not Shabbat) per shul schedule — not on Shabbat itself.

If you did not make eruv tavshilin: ask your rabbi immediately what you may still prepare for Shabbat.

Shabbat tonight: when Yom Tov ends Friday before Shabbat, light Shabbat candles from a pre-existing flame and follow your siddur for the transition — this is not the same as Motzei Shabbat into Yom Tov (no Yaknehaz tonight).

This year, today is $chagName on Friday and Shabbat begins tonight — the Yom Tov–Shabbat sequence is in progress.

Today on Yom Tov (if you made eruv tavshilin before the festival began):
• You may cook and prepare for Shabbat meals today — within halachic limits (food ready with time to spare before Shabbat).
• Finish preparations before Shabbat; set timers/blech as guided by your rav.

If you have not made eruv tavshilin, ask your rabbi immediately.

Shabbat tonight: light candles from a pre-existing flame after Yom Tov ends; follow your siddur for when Yom Tov ends and Shabbat begins.



### PurimMeshulashText.kt
#### advancePrepExplanation

Tomorrow is erev Purim — and this year is Purim Meshulash in Jerusalem. Shabbat is in the middle, so you need the full plan now (not only tomorrow).

${fullScheduleBlock()}


#### erevPrepExplanation

Purim Meshulash starts tonight in Jerusalem. Because Shabbat falls in the middle of the festival, read and save this plan now — you will not be able to rely on the app on Shabbat for Sunday's mitzvot.

${fullScheduleBlock()}

Tonight (Thursday night after tzeit): first Megillah reading. Tomorrow (Friday): second Megillah reading and matanot la'evyonim. Mishloach and seudah wait until Sunday.


#### fridayMatanotExplanation

Purim Meshulash — matanot la'evyonim on Friday only (14 Adar)

Today (Friday daytime) — not on Shabbat:
• Give at least one gift to each of two different poor people (minimum of two recipients total).
• Each gift should enable a modest Purim meal (money is common; amounts vary by community).
• Many give after the daytime Megillah reading.

You may use a trustworthy messenger or organization that distributes today. If you cannot find recipients, ask your rabbi or shul Friday morning.

Sunday is for mishloach manot and the seudah — those should already be prepared before Shabbat.


#### fridayMegillahExplanation

Purim Meshulash — Megillah on Friday (14 Adar)

When this year:
• Thursday night after tzeit — first reading (you should have heard it then).
• Today (Friday) — second reading during the daytime (usually after Shacharit, before sunset) — a universal obligation, not optional.

How (same laws as regular Purim):
• Hear every word from a kosher megillah scroll; men and women are equally obligated.
• Stand for the blessings; customs at Haman's name vary by shul.

${PurimBrachotText.MEGILLAH_BLESSINGS_COMMON}

Reminder: matanot la'evyonim are also today (Friday), not Shabbat. Mishloach manot and the seudah are Sunday — prepare packages before Shabbat if you have not already.


#### fullScheduleBlock

Purim Meshulash (פורים מְשֻׁלָּשׁ) — Jerusalem when Shushan Purim (15 Adar) falls on Shabbat

Why the calendar splits:
• Your Purim observance (walled city / Jerusalem) would normally be 15 Adar, but Purim mitzvot are not done on Shabbat.
• Megillah and matanot la'evyonim move to Friday (14 Adar). Mishloach manot and the seudah move to Sunday (16 Adar). Shabbat (15 Adar): individual home mitzvot (Megillah, matanot, mishloach, seudah) do not happen today, but Shabbat carries communal Purim obligations — see below.

Read this entire guide before Shabbat — your phone will be off on Shabbat, so you will not see Sunday's checklist until after Havdalah.

The four mitzvot — when they happen this year:
1. Megillah — Thursday night after tzeit (start of Friday / 14 Adar) AND Friday daytime (before sunset). Two readings, like a normal Purim. Confirm times with your shul; write them down or print them.
2. Matanot la'evyonim — Friday daytime only (not Thursday night, not Shabbat). Give at least one gift to each of two different poor people (minimum of two recipients total); money is common. Prepare cash, envelopes, or charity contacts before Friday.
3. Mishloach manot — Sunday (16 Adar) before sunset. At least two ready-to-eat foods to one friend. Prepare and label packages before Shabbat; plan who delivers on Sunday (you, family, or shul list).
4. Purim seudah — Sunday afternoon (16 Adar), before sunset. Festive meal with bread, joy, and Torah words — not on Shabbat.

What to finish before Shabbat candles (Friday):
• Megillah: attend Thursday night and Friday morning readings (or know your shul's schedule).
• Matanot: complete on Friday — have funds ready Friday morning.
• Mishloach: packages packed, labeled, and stored; delivery list written (Sunday only).
• Seudah: Sunday menu and timing planned; invite guests if needed.
• Machatzit haShekel: if your custom, many give before Megillah — handle before or with Thursday/Friday readings.

Shabbat (15 Adar):
• While individual home mitzvot like Megillah, matanot, mishloach, and the seudah do not happen today, Shabbat carries the communal Purim obligations.
• Synagogue: Attend for the special Purim Torah reading (Parashat Vayavo Amalek, Exodus 17:8–16) and the unique Haftarah for Purim.
• Davening & meals: Insert Al HaNissim into all Shabbat prayers (Amidah) and Birkat Hamazon today. Do not say Al HaNissim on Friday (14 Adar) or Sunday (16 Adar), even though other Purim mitzvot happen those days.
• No melacha for Purim prep on Shabbat — everything for Sunday must already be prepared.

Sunday (16 Adar):
• Send mishloach manot and celebrate the Purim seudah. The app will show today's items after Shabbat ends.

Ask your rav about edge cases (travel, illness, minhag).


#### sundayMishloachExplanation

Purim Meshulash — mishloach manot on Sunday (16 Adar)

Deferred from Shabbat because Purim mitzvot are not performed on Shabbat this year.

The mitzvah:
• Send at least two different ready-to-eat foods or drinks to one friend today — one package.
• Deliver before sunset; a messenger is fine.
• Food should be ready to eat without cooking; label sender and recipient.

You should have prepared packages before Shabbat. If not, ask your rav what you may still do today.


#### sundaySeudahExplanation

Purim Meshulash — Purim seudah on Sunday (16 Adar)

The festive Purim meal is today (not Friday or Shabbat this year).

When:
• Sunday daytime before sunset — many hold the meal in the afternoon after mishloach manot.

How:
• Festive meal with bread, meat, wine, and joy; include words of Torah or thanks to Hashem.
• Drinking wine is a widespread custom; celebrate responsibly.

This completes the four Purim mitzvot for Purim Meshulash in Jerusalem.


# Part 4 — Daily Mitzvot Checklist Explainers

Full explanation text for every item in the daily checklist (`checklist-items.json`). Includes default text plus Ashkenaz, Sefard, Edot HaMizrach, Chabad, and female variants where present. Shnayim Mikra prepends "This week's parsha: Parshat [name]" at runtime.

### Mincha (Afternoon Prayer)

*Section: Afternoon Prayer · ID: `mincha_shemoneh_esrei_tachanun`*

Mincha is the afternoon prayer — brief, powerful, and often said in the middle of the working day.

What Mincha is:
Mincha (מִנְחָה — originally the name of the afternoon grain offering in the Temple) is the afternoon prayer service. It was instituted by our patriarch Yitzchak (Isaac), who went to the field to pray in the afternoon (Genesis 24:63, per the Talmud).

What the Mincha service contains:
• Ashrei (Psalm 145) — see morning prayer notes
• Amidah / Shemoneh Esrei — the same 19-blessing standing prayer (see above)
• Tachanun — on most weekdays (see below)
• Aleinu — a closing prayer affirming G-d's sovereignty over the world

Tachanun (weekdays):
Tachanun (תַּחֲנוּן — supplication) is a penitential prayer after the Amidah on most ordinary weekdays. It is omitted on Rosh Chodesh, Yom Tov, Chanukah, Purim, and other days of joy.

At Shacharit:
• Mondays and Thursdays: longer Tachanun (often opening with "Vehu Rachum" / וְהוּא רַחוּם).
• Other weekdays: shorter form (exact text varies by nusach).

At Mincha:
Usually the shorter version — often beginning with "Vayomer David el Gad" ("And David said to Gad..."). If you already said Tachanun at Shacharit, Mincha may still include Tachanun depending on your custom — follow your siddur or congregation.

Timing:
Mincha begins from about 30 minutes after midday and must finish before sunset. The app shows the current window. If you miss Mincha, it can be made up at Maariv only — say the Maariv Amidah first, then immediately say a second Amidah as the tashlumin. This window closes when Maariv ends; you cannot make it up the following morning. See the guide entry on Tashlumin for full rules.

**Ashkenaz:**

Tachanun (Ashkenaz): At Shacharit on weekdays — longer on Mondays and Thursdays (Vehu Rachum), shorter on other days. At Mincha, often the shorter form (sometimes with nefilat apayim). Omitted on Rosh Chodesh, Yom Tov, Chanukah, and other days of celebration — Mincha itself is still said.

**Sefard:**

Mincha (Sephardi): The service opens with Korbanot (Parshat HaTamid), then Ashrei, the Amidah, Tachanun on weekdays, and Aleinu (Shulchan Arukh O.C. 232; follow your siddur).

Tachanun (Sephardi): At Shacharit on weekdays — longer on Mondays and Thursdays; shorter on other days (Vidui, Thirteen Attributes, Psalm 25). At Mincha, recited after the Amidah where applicable. Rav Ovadia Yosef (Yechaveh Daat 6:7) and the Ben Ish Chai rule against physical nefilat apayim — recite sitting upright. Omitted on Rosh Chodesh, holidays, and standard festive days.

**Edot HaMizrach:**

Mincha (Edot HaMizrach): The service opens with Korbanot (Parshat HaTamid), then Ashrei, the Amidah, Tachanun on weekdays, and Aleinu — per your kehilla's siddur.

Tachanun (Edot HaMizrach): At Shacharit on weekdays — longer on Mondays and Thursdays; shorter on other days (Vidui and the Thirteen Attributes per your siddur). At Mincha, recited after the Amidah including Vidui (Ashamnu) and the Thirteen Attributes. Following the Arizal (per Kaf HaChaim and Yalkut Yosef), do not perform nefilat apayim at Mincha — recite sitting upright, including Psalm 25 (LeDavid Alecha). Omitted on Rosh Chodesh, holidays, and standard festive days.

**Chabad:**

Mincha (Chabad / Nusach Ari): The communal service begins with Ashrei — not Korbanot (Sefer HaMinhagim, Mincha). The gabbai or chazan opens the minyan with Psalm 145; the congregation does not wait for or jointly recite Korbanot at Mincha.

Korbanot privately (optional): The Alter Rebbe (Shulchan Arukh HaRav, O.C. 234, Kuntres Acharon 2) writes that after Mincha Gedolah an individual who is meticulous may read the Tamid and Ketoret passages on his own — a personal stringency (midat chasidut), not part of the communal service. Hayom Yom (11 Nissan) treats this as private preparation before joining the minyan.

Tachanun (Chabad): At Shacharit on ordinary weekdays — longer on Mondays and Thursdays (Vehu Rachum), shorter on other days. At Mincha, typically the shorter Tachanun (Vayomer David el Gad). Omitted on Rosh Chodesh, Yom Tov, Chanukah, Purim, and other special occasions — Mincha itself is still said.

### Daily prayer (at least once)

*Section: Daily Prayer · ID: `at_least_one_prayer_daily_typically_morning`*

While men are obligated in three structured prayer times, women are required to pray at least once a day.

What prayer (tefillah) is:
Tefillah (תְּפִלָּה) comes from the Hebrew root meaning to judge oneself or reflect. Prayer is both a mitzvah and our most direct channel of communication with G-d. The rabbis derived from the phrase "to serve G-d with all your heart" (Deuteronomy 11:13) that this refers to prayer — the service of the heart.

What to say:
The minimum for women includes praise of G-d, personal requests, and thanks. Many women say the full Amidah (Shemoneh Esrei). Others fulfill the obligation with a heartfelt personal prayer in any language.

For newcomers:
Start with what you can manage consistently. Even a few sincere sentences every morning — thanking G-d, asking for what you need — is a genuine fulfillment. Over time, learn more from a siddur (prayer book).

### Brachot before eating or drinking

*Section: Eating & Blessings · ID: `blessings_before_food`*

Before eating or drinking anything, we say a short blessing (bracha) thanking G-d for the food. This is one of the most frequently-performed mitzvot in daily life.

What a bracha is:
Bracha (בְּרָכָה — blessing, plural: brachot) is a brief formula beginning "Baruch Atah Ado-nai Eloheinu Melech ha'olam..." (Blessed are You, L-rd our G-d, King of the universe) followed by a specific ending based on the food.

The Talmud's principle:
"It is forbidden to benefit from this world without a blessing." Eating without a bracha treats G-d's world as ownerless — the bracha is our acknowledgment that everything belongs to G-d.

The six main blessings before food:
• Hamotzi (הַמּוֹצִיא) — for bread made from any of the five grains (wheat, barley, spelt, oat, rye)
• Mezonot (מְזוֹנוֹת) — for other grain-based foods: cake, cookies, crackers, pasta, cereal
• HaGafen (הַגָּפֶן) — for wine or grape juice; the blessing is Borei pri ha'gafen (בּוֹרֵא פְּרִי הַגָּפֶן)
• HaEtz (הָעֵץ) — for fruit from a tree (apples, oranges, grapes, etc.)
• HaAdamah (הָאֲדָמָה) — for produce from the ground (vegetables, bananas, strawberries)
• Shehakol (שֶׁהַכֹּל) — for everything else: meat, fish, eggs, milk, cheese, water, candy, etc.

### Brachot after eating or drinking

*Section: Eating & Blessings · ID: `blessings_after_food`*

After eating or drinking, we thank G-d with an after-blessing (bracha acharona). Which blessing you say depends on what you ate or drank — and whether you ate or drank enough, within the required time.

The after-blessings:

Birkat Hamazon (בִּרְכַּת הַמָּזוֹן — Grace After Meals):
• After eating bread (Hamotzi)
• A full multi-paragraph prayer of thanks; a Torah-level commandment (Deuteronomy 8:10)
• When three or more adult Jewish men eat bread together, Zimmun (זִמּוּן — a formal invitation) is recited together before Birkat Hamazon
• A bentcher (בֶּנְטְשֶׁר) is a small booklet containing the text — commonly found at Shabbat tables
• After a bread meal: if you ate at least a kezayit of bread within k'dei achilat pras, Birkat Hamazon (bentching) covers everything else you ate at that same meal — wine, meat, fruit, dessert, etc. You do not say separate after-blessings for those other foods.

Al HaMichya (עַל הַמִּחְיָה — Bracha Achat Me'ein Shalosh):
• After Mezonot — grain foods that are not bread: cake, crackers, pasta, cereal, etc. (wheat, barley, spelt, oat, rye)
• A condensed “one blessing like three” derived from Birkat Hamazon
• At the same sitting, if you also had enough wine/grape juice or enough shivat ha-minim tree fruits (below), use one Al HaMichya text with the inserted phrases (v'al hagafen v'al pri hagafen, v'al ha'etz v'al pri ha'etz) in your siddur

Al HaGafen (עַל הַגָּפֶן וְעַל פְּרִי הַגָּפֶן):
• After wine or grape juice — the full after-blessing is על הגפן ועל פרי הגפן (the vine and the fruit of the vine), not על הגפן alone
• At least a revi'it within k'dei shtiyat revi'it (not Borei Nefashot)
• Same category as the before-blessing Borei pri ha'gafen (בּוֹרֵא פְּרִי הַגָּפֶן)
• Wine or grape juice only — fresh grapes eaten as fruit take Al HaEtz (see below), not Al HaGafen

Al HaEtz (עַל הָעֵץ וְעַל פְּרִי הָעֵץ — Bracha Achat Me'ein Shalosh):
• After shivat ha-minim (Seven Species) tree fruits: dates, figs, pomegranates, olives — and fresh grapes eaten as fruit (not grape juice)
• Fresh grapes: Because grapes are one of the Seven Species, eating a kezayit of fresh grapes always requires Al HaEtz (Me'ein Shalosh), not Borei Nefashot — whereas regular tree fruits like apples or oranges take Borei Nefashot only
• The full phrase is על העץ ועל פרי העץ (the tree and the fruit of the tree) throughout the blessing — opening, thanks, and closing — not על העץ alone
• Not for apples, oranges, bananas, berries, or other non–shivat ha-minim tree fruits — those use Borei Nefashot only
• A kezayit within k'dei achilat pras

Borei Nefashot (בּוֹרֵא נְפָשׁוֹת — "Who creates souls"):
• After most other foods: meat, fish, eggs, dairy, vegetables, and fruits that are not shivat ha-minim
• After most drinks other than wine/grape juice, if you drank a revi'it within k'dei shtiyat revi'it
• The shortest after-blessing; can be memorized in minutes

How much food — k'dei achilat pras (eating):
• Generally need at least a kezayit (כְּזַיִת — about olive-sized; many use roughly 1 oz / 30 g)
• Eaten within k'dei achilat pras (כְּדֵי אֲכִילַת פְּרַס — the time to eat about half a loaf): lechatchila about 4 minutes; many poskim allow up to about 6–9 minutes bedi'eved (Shulchan Aruch / Mishnah Berurah)
• Less than a kezayit, or spread over longer than k'dei achilat pras: no after-blessing on that food

How much drink — k'dei shtiyat revi'it (drinking) — not the same as k'dei achilat pras:
Halacha often requires an action within a specific continuous timeframe. For eating a kezayit, that is k'dei achilat pras (minutes — see above). For drinking, it is much faster.

K'dei shtiyat revi'it (כְּדֵי שְׁתִיַּת רְבִיעִית) literally means the time it takes to drink a revi'it (about 3–5 fl oz / 86–150 ml depending on custom). It is a very short span — a few continuous seconds — used as a legal measurement for drinking.

For bracha acharona on a beverage:
• You need a revi'it drunk within k'dei shtiyat revi'it
• If you sip coffee slowly over 20 minutes, you did not drink the required volume within the required time — no after-blessing on that drink
• How many seconds? Poskim differ:
  — Sephardic / Rambam / Rav Ovadia Yosef: one continuous drink without removing the cup from your lips — the strictest view; halacha defines this qualitatively (not as a fixed second count), but it is only a brief uninterrupted span — often estimated at just a few seconds for a full revi'it
  — Ashkenaz / Mishnah Berurah: often estimated as a continuous sequence of normal gulps within 5–9 seconds max. To guarantee your obligation for an after-blessing, drink the beverage in a single, uninterrupted sequence.
• Cold drinks (water, juice): to be obligated in Borei Nefashot or Al HaGafen, swallow a revi'it quickly and continuously in those few seconds rather than nursing it slowly
• Hot coffee, tea, or soup: drinking that fast is difficult, so common practice is no after-blessing when you only sip slowly — unless you set aside at least a revi'it and drink it at once when cool enough

Drinks only — which after-blessing?
• Wine or grape juice: Al HaGafen (עַל הַגָּפֶן וְעַל פְּרִי הַגָּפֶן) if a revi'it within k'dei shtiyat revi'it
• Water, milk, soda, etc.: Borei Nefashot if a revi'it within k'dei shtiyat revi'it
• Mezonot snack plus wine at the same sitting: one Al HaMichya including the wine phrase if both meet their thresholds

Where to find the exact text:
Open your siddur or bentcher to the bracha acharona section. In Mitz Mode, tap the menu (⋮) → **Blessings** for before- and after-blessing texts.

### Asher Yatzar (After using the bathroom)

*Section: Eating & Blessings · ID: `asher_yatzar_after_using_bathroom`*

After using the bathroom, we wash our hands and say the Asher Yatzar blessing — every time you finish.

When to say it:
• Normally: say Asher Yatzar each time you use the bathroom, once you are done and have washed your hands.
• If you are ill — for example diarrhea or another condition where you may need the bathroom again right away — wait until you are reasonably sure you have finished for that round before saying it. Otherwise you might say the blessing and then need the bathroom again immediately, which is impractical and not the point of the bracha.

What it is:
Asher Yatzar (אֲשֶׁר יָצַר — "Who fashioned") is a blessing thanking G-d for the miraculous design of the human body. It is one of the most medically insightful prayers ever composed.

The blessing says:
G-d created the human body with wisdom — with openings and closings (tubes, valves, sphincters). If any one of them were to open when it should be closed, or close when it should be open, it would be impossible to survive.

Why this is profound:
Modern medicine has confirmed exactly this. A blocked ureter, a stuck heart valve, or a blocked bowel — any of these is a life-threatening emergency. Every time your body functions normally, it is a miracle. Asher Yatzar trains us to recognize it as one.

How to do it:
1. After using the bathroom, wash your hands (without a blessing, unless this is your morning washing)
2. Say Asher Yatzar — it takes about 20 seconds

Find the text in any Jewish prayer book (siddur) or app. In Mitz Mode, tap the menu (⋮) → **Blessings** for the text.

### Shema with its blessings (Evening)

*Section: Evening Prayer · ID: `evening_shema_with_its_blessings`*

The Torah commands the Shema to be said "when you lie down" — meaning in the evening. This is a separate Torah-level obligation from the morning Shema.

What it is:
The same three Torah paragraphs as the morning Shema (see the morning prayer section), but said at night with different surrounding blessings.

The blessings around the evening Shema:
• Before: A blessing about G-d alternating day and night (Ma'ariv Aravim) and a blessing about G-d's eternal love for Israel (Ahavat Olam)
• After: A blessing affirming the Exodus from Egypt (Emet Ve'Emunah) and prayers for protection through the night

Timing:
From nightfall (tzeit hakochavim — when three medium stars appear) until dawn. Ideally said before halachic midnight (chatzos halayla). The app shows the current window.

After sunset (shkiyah):
While Maariv (the evening prayer) can begin at sunset, the biblical obligation to recite Shema at night has not yet begun. If you pray early Maariv, you will need to repeat the Shema later (at nightfall / tzeit hakochavim).

Note:
The evening Shema (as part of Maariv) is distinct from the bedtime Shema (Kriat Shema al HaMitah). Both are required.

### Maariv (Evening Prayer — Amidah)

*Section: Evening Prayer · ID: `maariv_shemoneh_esrei`*

Maariv is the evening prayer service, instituted by our patriarch Yaakov (Jacob) and observed every night.

What it is:
Maariv (מַעֲרִיב — also called Arvit, עַרְבִית) is the evening prayer service. After the Shema and its blessings, we say the Amidah (Shemoneh Esrei) — the same standing prayer as in Shacharit and Mincha, though the evening version has some differences.

Structure of Maariv:
1. Ashrei and a short passage (in some traditions)
2. Barchu — the call to prayer (בָּרְכוּ — "Bless G-d"), said when praying with a minyan of 10 men
3. Evening Shema with its two blessings before and two after
4. Amidah / Shemoneh Esrei
5. Aleinu
6. Mourner's Kaddish (said by mourners or those observing yahrzeit)

Timing:
From nightfall until dawn. If you miss Maariv, make it up at the following morning's Shacharit only — say the regular Shacharit Amidah first, then immediately say a second Amidah as the tashlumin for Maariv. If you miss that window too, the opportunity is permanently gone. See the guide entry on Tashlumin for full rules including the Tefilat Nedavah option.

### Kriat Shema al HaMitah (Bedtime Shema)

*Section: Evening Prayer · ID: `bedtime_shema_first_paragraph_though_recommended_to_say_enti`*

Said immediately before sleep, the bedtime Shema is a separate requirement from the Shema said during Maariv.

What it is:
Kriat Shema al HaMitah (קְרִיאַת שְׁמַע עַל הַמִּטָּה — "reading of the Shema on the bed") is a short nighttime prayer said as the final spiritual act before sleep. The Talmud teaches it protects the person through the night.

What to say:
• At minimum: the first paragraph of Shema ("Shema Yisrael... uvshochbecha uvkumecha")
• Recommended: the full three paragraphs of Shema
• Also included: Hamapil blessing (next item), several psalms (including Psalm 91 — a psalm of protection), and other brief prayers

Why a second Shema?
Even if you already said the Shema as part of Maariv, the bedtime Shema is a separate mitzvah — different time, different purpose. The Maariv Shema fulfills the evening commandment; the bedtime Shema is specifically for protection during sleep.

Timing:
Say it as close to the actual time of sleep as possible. Ideally before halachic midnight (chatzos halayla). If you go to bed later, say it then.

### Kriat Shema al HaMitah (Bedtime Shema)

*Section: Evening Prayer · ID: `bedtime_shema_women`*

Women are obligated in Kriat Shema al HaMitah (קְרִיאַת שְׁמַע עַל הַמִּטָּה — the Bedtime Shema), even though women are generally exempt from most time-bound positive commandments (mitzvot aseh shehazman graman, like the daytime Shema at a specific hour). Bedtime Shema is treated differently for several reasons:

Spiritual protection (shmirah — שְׁמִירָה):
The Bedtime Shema is not only a timed recitation. The Talmud teaches that these words guard a person while the soul is partially withdrawn during sleep. Women need this protection during sleep just as men do.

Hamapil (הַמַּפִּיל):
The blessing recited just before sleep — "Who casts the bands of sleep upon my eyes..." — is a blessing of praise and a request for protection. Many women say it with the full bedtime order; Ashkenazi custom is to recite it when you lie down intending to sleep (Mishnah Berurah 239:6). If you are genuinely unsure you will sleep soon — per Sephardi and kabbalistic guidance — you may omit the closing Baruch Atah… or say the words as meditation without G-d's name (see the Hamapil checklist item).

The core verse:
Even beyond the full bedtime order, reciting the first verse of Shema (Shema Yisrael) is an acknowledgment of G-d's sovereignty that applies to everyone.

What to say:
• At minimum: the first paragraph of Shema ("Shema Yisrael..." through "uvshochbecha uvkumecha")
• Recommended: all three paragraphs of Shema, as printed in your siddur's bedtime section
• Then Hamapil, as the last blessing before closing your eyes
• Many siddurim also include psalms (such as Psalm 91) and brief prayers of trust in G-d

Customs:
Some traditions omit certain prayers in the full bedtime order (for example, some are cautious about saying Hamapil if they are unsure they will fall asleep immediately). The core Shema and the protective blessings remain universal for women. Ask your rabbi or rebbetzin about your community's practice.

Timing:
Say it right before getting into bed to go to sleep for the night — at your actual bedtime, not when taking a short daytime nap. If you go to bed later than halachic midnight, say it then. If you forgot and have not yet slept, say it when you remember.

Distinct from Maariv:
If you pray Maariv or say the evening Shema separately, the Bedtime Shema at sleep is still its own mitzvah — different time and purpose (protection through the night).

Use a siddur or the guides below for the exact text step by step.

### Hamapil (Blessing before sleep)

*Section: Evening Prayer · ID: `hamapil_blessing_according_to_many_opinions`*

Hamapil is the very last thing said before closing your eyes to sleep.

What it is:
Hamapil (הַמַּפִּיל — "who casts") is a blessing thanking G-d for the gift of sleep and asking for protection through the night. It is unique because it is said before an action you are about to do (sleep) rather than while doing it.

The blessing asks that:
• Our eyes be allowed to sleep
• We wake in the morning in peace
• We not be disturbed by bad dreams
• Our sleep refresh and heal us

Why sleep requires a blessing:
The Talmud teaches that sleep is "one-sixtieth of death" — a nightly partial departure of the soul from the body. Saying Hamapil acknowledges this transition and expresses total trust in G-d.

How to say it:
Say it after the bedtime Shema, immediately before closing your eyes.

After saying Hamapil (Rama, Shulchan Arukh O.C. 239:1): do not eat, drink, or speak until you fall asleep. Hamapil is meant to be the final spiritual anchor directly adjacent to losing consciousness — not followed by extended activity.

If an urgent need arises (a child crying, needing the restroom, a security matter), you may speak — this does not cancel the blessing and you do not need to repeat it (Biur Halachah O.C. 239:1).

What may you do while waiting to fall asleep?
• Repeating verses of Shema, or quietly murmuring familiar protective verses you already know to help yourself doze off — this is universally permitted and is not considered an interruption (Biur Halachah O.C. 239:1).
• Sitting up to deliberately begin new Torah study, or reading lengthy chapters of Tehillim after the final blessing of Hamapil — classical codifiers discourage this; the sages wanted Hamapil to lead straight into sleep, not into a study session.

If you find yourself wide awake for hours and simply cannot sleep, some poskim permit leniently relying on the view that learning Torah is allowed rather than lying idle — but this is a fallback for genuine inability to sleep, not an open standard permission from the outset.

What if you're not sure you'll fall asleep?
There are two main positions:

1. Ashkenazi ruling — always say it (Mishnah Berurah 239:6; Chazon Ish, OC 37:11):
Hamapil is a blessing of praise for G-d's creation of sleep in general, not a personal guarantee that you will fall asleep at once. Even if you suffer from insomnia, say it. As long as you lie down with the intent to sleep, the blessing is valid — any sleep you eventually get that night validates it retroactively.

2. Sephardic and Kabbalistic ruling — exercise caution (Ben Ish Chai, Year 1, Parashat Pekudei 12; Kaf HaChaim OC 239:18):
If you are genuinely uncertain you will sleep soon — due to illness, severe anxiety, or acute insomnia — do not say the full blessing with G-d's name (Shem u'Malchut), to avoid a blessing said in vain (bracha levatala). Instead, say the words as a meditative prayer while thinking G-d's name silently, or omit the closing signature (Baruch Atah...) entirely. The principle applied is: safek brachot l'hakel — when in doubt about a blessing, be lenient and omit it.

### Tzniut (Modest dress and conduct)

*Section: Important Lifestyle Mitzvot · ID: `modesty_tznius`*

Tzniut is one of the defining values of Jewish life — it governs how we dress and how we carry ourselves in the world.

What tzniut is:
Tzniut (צְנִיעוּת — modesty) comes from the verse "Walk humbly (hatznea lechet) with your G-d" (Micah 6:8). It is not just a dress code — it is a philosophy of life. Tzniut means reserving what is most precious and intimate for those who truly know you, rather than displaying it to the world.

In dress (for women):
• Skirts or dresses that cover the knees
• Tops that cover the elbows and collarbone
• Avoid tight-fitting or sheer clothing
• Stockings / tights as required by community custom

In conduct (for everyone):
• Speak with dignity and thoughtfulness
• Do not seek unnecessary attention
• Act in a way that reflects inner character over outer appearance
• Lower the eyes when appropriate

For men:
Tzniut applies to men too — in dress, speech, and conduct. Men should not wear revealing clothing and should conduct themselves with dignity.

Standards vary by community. The key is sincere progress and consistency, guided by your local rabbi and rebbetzin.

### Kisui Rosh (Hair covering for married women)

*Section: Married women's mitzvot · ID: `cover_hair_in_public_if_married`*

Married Jewish women are required to cover their hair whenever they are in public or in the presence of men other than their husband.

What it is:
Kisui rosh (כִּיסּוּי רֹאשׁ — literally "covering of the head") is one of the fundamental practices of Jewish married life. It is a Torah-level obligation (Shulchan Aruch, Even HaEzer 115:4).

Why:
A married woman's hair is considered ervah (עֶרְוָה — literally "nakedness"; in halachic terms, something private that should not be publicly displayed). Covering it expresses that her deepest beauty is reserved for the privacy of her home and her husband. It is also an expression of tzniut (modesty) — the Jewish value of inner dignity over external display.

Methods of covering:
• Tichel (טִיכֶל) — a headscarf or wrap, commonly used across many communities
• Sheitel (שֵׁייטֶל) — a wig; widely used in Ashkenazic and Chassidic communities
• Hat or beret
• Mitpachot — decorative wraps and turbans
• Combination (e.g., small wig with a hat over it)

Standards:
Most Orthodox authorities require the entire hair to be covered in public. Details vary by community. Speak with your rabbi or rebbetzin about what applies in your situation.

### Taharat HaMishpacha (Family Purity Laws)

*Section: Married women's mitzvot · ID: `family_purity_laws_if_married`*

The laws of family purity govern the intimate relationship between husband and wife and are among the most important mitzvot in the Torah.

What they are:
Taharat HaMishpacha (טָהֳרַת הַמִּשְׁפָּחָה — literally "family purity") is a cycle of physical separation and mikveh immersion that structures the intimacy of Jewish marriage.

The basic cycle:
• During niddah (נִדָּה — menstrual period and its aftermath), the husband and wife refrain from physical contact
• After the period ends, the wife counts seven "clean days" (shivah nekiyim) during which she checks that bleeding has fully stopped
• After the seven clean days, she immerses in a mikveh (ritual pool)
• After immersion, the couple resumes their relationship

Why these laws:
Jewish tradition teaches that these laws bring holiness, freshness, and blessing to marriage. Couples who practice them often describe the monthly rhythm of separation and reunion as one of the most surprisingly positive aspects of Jewish life.

How to learn:
This system is complex and must be learned properly before marriage — or as soon as you begin observance. Take a Taharat HaMishpacha course from a qualified teacher (rebbetzin, kallah teacher, or through organizations like Nishmat or local Jewish community programs).

### Birkat HaTorah (Blessings before Torah study)

*Section: Morning Prayer (Shacharit) · ID: `torah_blessings_minimal_torah_study`*

Before you open a book — remember what Torah study is:

The blessings you are about to say exist because Torah is how we cleave to Hashem. The sages teach that learning Torah outweighs every other mitzvah combined (Peah 1:1), that the Shechinah joins those who speak words of Torah (Avot 3:2–3), and that Torah study protects your life in this world and the next (Sotah 21a). Birkat HaTorah is the doorway into that reality — asking Hashem to make Torah sweet on your lips, not heavy.

Every day, before learning Torah for the first time, we say Birkat HaTorah — the blessings over Torah study. For men, this is widely held as a Torah-level commandment; women's obligation and its level are debated among poskim — all agree women who learn Torah should say these blessings.

What these blessings are:
Birkat HaTorah (בִּרְכַּת הַתּוֹרָה — blessings of the Torah) consists of three short blessings:
1. A blessing thanking G-d for commanding us to engage in Torah study
2. A blessing asking G-d to make the words of Torah sweet in our mouths and the mouths of our children
3. A blessing praising G-d for giving us the Torah

After saying these blessings, we immediately read short Torah passages (which also fulfills a minimum daily Torah study obligation).

Why they matter:
The Talmud teaches that many troubles in Jewish history came from failing to say the blessings before Torah study. These blessings express that Torah is not just intellectual — it is a divine gift and our most sacred inheritance.

Timing:
Said once in the morning; they cover all Torah learning for the entire day. They can be said from after halachic midnight (chatzos halayla — the midpoint of the night) onward — so even if you wake up very early, say them.

**Female:**

Torah learning bonds you with Hashem — the sages teach that the Shechinah dwells wherever words of Torah are spoken (Avot 3:2) and that Torah study protects your life (Sotah 21a). Birkat HaTorah opens that door each day.

Before learning Torah for the first time each day, women say Birkat HaTorah — the blessings over Torah study. This applies when you learn halacha or any Torah text. Women's obligation in Birkat HaTorah is widely accepted among poskim; the precise level (Torah vs rabbinic) is debated — ask your rav.

Say the three blessings once in the morning (from after halachic midnight / chatzos halayla onward). They cover all Torah learning for the rest of the day.

After the blessings, many siddurim include short Torah passages. Even a few minutes of halacha learning afterward fulfills your daily study obligation (see Torah Study).

### Birchot HaShachar (Morning Blessings)

*Section: Morning Prayer (Shacharit) · ID: `morning_blessings_birchot_hashachar`*

A series of short blessings said at the beginning of the morning prayer service, thanking G-d for things we tend to take completely for granted.

What they are:
Birchot HaShachar (בִּרְכוֹת הַשַּׁחַר — Blessings of the Dawn) is a sequence of ~15 blessings at the start of Shacharit. They originated as blessings said at each stage of waking up and preparing for the day — as you open your eyes, stand up, put on clothes, and step outside.

What you thank G-d for:
• Giving intelligence to the rooster to distinguish day from night
• Not making you a non-Jew, a slave, or a woman (men's version; women say "for making me according to His will")
• Opening the eyes of the blind
• Clothing the naked
• Straightening those who are bent
• Spreading the earth over the waters
• Giving strength to the weary
• And more...

What comes before Birchot HaShachar:
In the full morning service, Korbanot (passages about Temple offerings) typically come before or after these blessings. While these may be skipped if time is short, saying them is an important part of the complete service.

Timing:
• The earliest time to say Birchot HaShachar is from chatzos (halachic midnight / chatzos halayla).
• L'chatchila (ideally), say the first blessing — HaNoten LaSechvi Vinah (who gives the rooster understanding to distinguish day from night) — after alot hashachar (dawn). If you said it after chatzos, you have still fulfilled your obligation.
• The other blessings in the sequence may be said from chatzos onward if you wake up very early.

**Female:**

Many poskim and communities hold that women should say Birchot HaShachar upon waking — the same sequence of morning blessings men recite at the start of Shacharit, thanking G-d for the gifts of each new day.

What they are:
Birchot HaShachar (בִּרְכוֹת הַשַּׁחַר — Blessings of the Dawn) is a sequence of ~15 short blessings. They originated as blessings said at each stage of waking — opening your eyes, standing up, getting dressed, and stepping outside.

Women's wording:
Use your siddur's women's version. Instead of "who has not made me a woman," women say "who has made me according to His will." The other blessings are the same.

When to say them:
• From chatzos (halachic midnight) onward.
• Ideally say HaNoten LaSechvi Vinah (the rooster blessing) after alot hashachar; if you said it after chatzos, you have still fulfilled your obligation.
• The other blessings may be said from chatzos if you wake up very early. Ideally say them upon waking.

Even if you do not daven full Shacharit, many say these blessings are an important way to start the day with gratitude.

Ask your rabbi or rebbetzin about your community's practice.

### Tefillin (Phylacteries) during morning prayers

*Section: Morning Prayer (Shacharit) · ID: `put_on_tefillin_during_morning_prayers_except_shabbat_festiv`*

Tefillin are two small black leather boxes containing Torah passages, worn during morning prayers. This is one of the most important daily mitzvot for Jewish men.

What they are:
Tefillin (תְּפִלִּין — phylacteries) consist of two black leather boxes, each containing four hand-written Torah passages on parchment.
• Shel yad (of the hand): strapped to the upper left arm for right-handed individuals, or the upper right arm if you are left-handed, near the heart, with the strap wound seven times around the forearm and then around the hand and fingers in a specific pattern
• Shel rosh (of the head): placed on top of the head, centered, above the hairline, with the straps hanging down in front

The Torah source:
"Bind them as a sign on your arm, and they shall be frontlets between your eyes" (Deuteronomy 6:8). This verse is read in the Shema.

Why we wear them:
Tefillin bind our mind (head-box), heart (arm-box, worn near the heart), and hands (wrapped around the arm) to G-d during prayer. They are a sign of the covenant between G-d and the Jewish people.

When to put them on:
The bracha on tallit and tefillin is recited at misheyakir — when there is enough daylight to recognize a person from a distance (see glossary). Mishnah Berurah recommends waiting until misheyakir, then putting on tallit and tefillin and reciting Krias Shema and the Amidah. The Gemara (Berachot 14b) compares reciting Shema without tallit and tefillin to false testimony — avoid saying Shema before misheyakir if you can wait.

If you must leave for work very early:
• Best: put on tallit and tefillin after misheyakir; ideally begin the Amidah at sunrise.
• Next: put on tallit and tefillin after misheyakir; Amidah before sunrise is acceptable.
• Only if you have no other choice: Igros Moshe (O.C. 4:6) permits putting on tallit and tefillin after alot hashachar without a bracha, davening Krias Shema and the Amidah, then — after misheyakir — moving them slightly and reciting the brachos. You do not fulfill the mitzvah at the earlier time; this is so you do not get accustomed to davening without tallit and tefillin. Ask your rav.

The app unlocks this item at misheyakir in your local zmanim.

When NOT worn:
• Shabbat (the Sabbath) and Yom Tov (Jewish holidays)
• Chol HaMoed (the intermediate days of Passover and Sukkot) — this depends on community custom; ask your rabbi

Getting started:
Kosher tefillin are a significant investment ($200 to $2000+). Do not buy cheap tefillin — many are not kosher. Ask your rabbi to help you acquire a certified pair and to teach you how to put them on correctly.

### Pesukei DeZimra (Verses of Praise)

*Section: Morning Prayer (Shacharit) · ID: `minimum_pesukei_d_zimra`*

Psalms and poetic passages recited before the Shema and Amidah to prepare the heart and mind for prayer.

What they are:
Pesukei DeZimra (פְּסוּקֵי דְזִמְרָא — Verses of Song) is a collection of psalms and passages from the Hebrew Bible said before the central prayers each morning. The word zimra means song or melody.

Why we say them:
The Talmud compares jumping straight into the Amidah without preparation to barging into a king's presence without first requesting an audience. Pesukei DeZimra is our way of gradually elevating our focus, shifting from ordinary thought to spiritual awareness, before standing before G-d in prayer.

The minimum for when you are short on time:
• Baruch She'amar (opening blessing)
• Ashrei (Psalm 145)
• Yishtabach (closing blessing)

The full version also includes Psalms 146-150 and other hymns. If you arrive at synagogue late, skip Pesukei DeZimra to say the Shema and Amidah with the congregation on time.

### Shema with its blessings (Morning)

*Section: Morning Prayer (Shacharit) · ID: `morning_shema_with_its_blessings`*

The Shema is the central declaration of Jewish faith, recited twice daily — morning and evening. Saying it each day is a Torah-level commandment.

What the Shema is:
Shema (שְׁמַע — "Hear" or "Listen") is three Torah passages:
• Paragraph 1 (Deuteronomy 6:4-9): Declares G-d's oneness and the obligation to love Him with all your heart, soul, and resources
• Paragraph 2 (Deuteronomy 11:13-21): The reward for observing the Torah and the consequence for abandoning it
• Paragraph 3 (Numbers 15:37-41): The commandment of tzitzit and remembering the Exodus from Egypt

The first line:
"Shema Yisrael, Ado-nai Eloheinu, Ado-nai Echad."
"Hear O Israel: the L-rd is our G-d, the L-rd is One."
This is the most fundamental statement in all of Judaism.

The blessings that surround it:
• The morning blessings before and after Shema (Yotzer Or, Ahavat Olam / Ahavah Rabbah, Emet VeYatziv) belong in Shacharit — not when you recite the three Shema paragraphs alone to catch sof zman Shema before davening.
• Before the Shema in Shacharit: two blessings — one about G-d creating light and day (Yotzer Or) and one about G-d giving the Torah to Israel (Ahavat Olam / Ahavah Rabbah)
• After the Shema: a blessing about redemption from Egypt (Emet VeYatziv), affirming that G-d saved our ancestors and continues to save us

How to say it:
Recite the first line slowly with full intention. Covering your eyes during the first verse of the Shema is a universally accepted custom, but it is not a strict halachic obligation. The rest can be said at a normal pace.

Timing:
• The biblical morning Shema must be recited within the first three shaot zmaniot (halachic hours) of the day — until sof zman Shema. That is the latest time to fulfill the Torah obligation.
• This app grays out the item after sof zman Shema because the biblical obligation can no longer be fulfilled.
• If you will not daven Shacharit before sof zman Shema, say the three Shema paragraphs by themselves before that deadline — without the blessings before and after.
• If you will daven Shacharit before sof zman Shema, say the Shema with its blessings as part of the full service.
• Bedi'eved: if you missed sof zman Shema but are still davening Shacharit until chatzos (halachic midday), say the morning Shema with its blessings as part of that service. Doing so does not fulfill the biblical obligation, but it is how you complete Shacharit properly.

### Amidah / Shemoneh Esrei (Morning prayer)

*Section: Morning Prayer (Shacharit) · ID: `shemoneh_esrei_tachanun`*

The Amidah is the central, most important prayer of every service — the moment we stand directly before G-d in silent prayer.

What it is:
The Amidah (עֲמִידָה — "standing") is also called the Shemoneh Esrei (שְׁמוֹנֶה עֶשְׂרֵה — "eighteen") after the original 18 blessings; a 19th was added later. On weekdays it contains 19 blessings; on Shabbat and holidays, it is shorter.

The three sections:
• Shevach (praise): The first three blessings praise G-d — His greatness, His power over life and death, and His holiness
• Bakasha (requests): The middle 13 blessings ask for wisdom, repentance, forgiveness, redemption, health, livelihood, ingathering of exiles, restoration of justice, and the coming of the Messiah
• Hodayah (thanks): The last three blessings give thanks and ask for peace

How to say it:
• Stand with your feet together (as angels stand)
• Face Jerusalem (roughly east in most places)
• Bow at four specific points in the prayer
• Say it quietly — lips move but only you can hear
• Do not stop or interrupt once you begin

Tachanun — what is it?:
Tachanun (תַּחֲנוּן — supplication) is a penitential prayer added after the Amidah on most weekdays. It is a confession and plea for mercy. Tachanun is usually said while sitting, leaning the forehead on the arm (nefilat apayim — a posture of submission).

At Shacharit:
• Mondays and Thursdays: the longer Tachanun is said — the full section (often opening with "Vehu Rachum" / וְהוּא רַחוּם) with nefilat apayim.
• Other weekdays: a shorter Tachanun is used (exact text varies by nusach and community).

Tachanun is omitted on Shabbat, Yom Tov (holidays), Rosh Chodesh (the new month), and other days of joy — the Amidah is still said.

Rosh Chodesh means "head of the month" — the first day of the Hebrew calendar month, a minor holiday.

If you are praying in a synagogue, follow the congregation's practice for Tachanun.

Timing:
The morning Amidah becomes available at dawn (alot hashachar — shown in the app). Ideally say it at sunrise or later, when the full Shacharit service is ordinarily prayed. If you are in a hurry, you may say the Amidah after dawn even before sunrise.

Ideal deadline: before the end of the fourth halachic hour (sof zman tefillah — visible in the app). Bedi'eved you may still daven Shacharit until halachic midday (chatzos). If you miss it entirely, at Mincha pray two Amidot — Mincha first, then tashlumin for the missed Shacharit.

**Ashkenaz:**

Tachanun (Ashkenaz): On most weekdays Tachanun follows Shacharit Amidah — usually sitting with nefilat apayim. Mondays and Thursdays: longer Tachanun (Vehu Rachum and the full penitential section). Other weekdays: shorter form. Omitted on Rosh Chodesh, festivals, Chanukah, and other days listed in your siddur.

**Sefard:**

Tachanun (Sephardi): On weekdays after Shacharit Amidah — Vidui, the Thirteen Attributes of Mercy, then Psalm 25 (LeDavid). Sephardic poskim including Rav Ovadia Yosef (Yechaveh Daat 6:7) and the Ben Ish Chai rule against physical nefilat apayim — recite sitting upright. Longer form on Mondays and Thursdays; shorter on other weekdays. Omitted on Rosh Chodesh, festivals, Chanukah, and other days in your siddur (Peninei Halakha, Prayer 03-17-05).

**Edot HaMizrach:**

Tachanun (Edot HaMizrach): On weekdays after Shacharit — Vidui and the Thirteen Attributes per Sephardic order (Peninei Halakha, Prayer 03-17-05). Longer on Mondays and Thursdays; shorter on other weekdays. Posture and exact wording vary by kehilla (Iraqi, Syrian, Moroccan, etc.) — follow your siddur. Many communities, following kabbalistic guidance cited by the Ben Ish Chai and Rav Ovadia Yosef (Yechaveh Daat 6:7), refrain from physical nefilat apayim and recite Psalm 25 sitting upright. Omitted on Rosh Chodesh, festivals, Chanukah, and other days in your siddur.

**Chabad:**

Tachanun (Chabad / Nusach Ari): Recite the Amidah from Tehillat Hashem. Tachanun on ordinary weekdays at Shacharit — longer on Mondays and Thursdays (Vehu Rachum), shorter on other days. Omitted on Rosh Chodesh, Yom Tov, Chanukah, and other special days in the siddur.

### Musaf (Additional prayer on special days)

*Section: Morning Prayer (Shacharit) · ID: `musaf_only_on_rosh_chodesh_festivals_and_shabbat`*

Musaf is an additional Amidah prayer added to the morning service on Shabbat, Rosh Chodesh, and all Jewish holidays.

What Musaf is:
Musaf (מוּסָף — literally "additional") is an extra Amidah — the same standing prayer as the rest of the service — but with a special middle blessing describing the Temple offerings (korbanot) that were brought on that day in Temple times, and a prayer for the restoration of the Temple service.

When Musaf is said:
• Every Shabbat
• Rosh Chodesh (new month)
• All Yom Tov holidays
• Chol HaMoed (intermediate days of Pesach and Sukkot)

Why we say it:
In Temple times, special extra offerings (musaf korbanot) were brought on these holy days. Since the Temple was destroyed, we replace the physical offerings with prayer — reciting what was offered and praying for the Temple to be rebuilt.

Timing:
Said after Shacharit (the main morning service), ideally before the end of the 7th halachic hour; bedi'eved valid until sunset (Shulchan Arukh O.C. 286:1).

### Melave Malka (festival meal after Shabbat)

*Section: Motzei Shabbat · ID: `melave_malkah`*

Melave Malka (מְלַוֶּה מַלְכָּה) means "escorting the queen" — a meal Saturday night to escort the Shabbat Queen as she departs, the way one would honor a beloved guest at the door (Talmud Shabbat 119b). It is also called Seudat David HaMelech: King David held a Motzei Shabbat feast each week after learning he would die on Shabbat (Shabbat 30a).

When:
After Havdalah, from tzeit (nightfall) until dawn Sunday. Men and women; all nusachim.

What to do:
• Spread a clean tablecloth and set the table — the sages say to set the table even if you will eat only a small amount
• Sit and eat at the table. The ideal is a meal with bread: wash hands (netilat yadayim), say hamotzi, and eat. If you are too full for bread, you can still fulfill the mitzvah with cake or another meaningful portion of food — netilat yadayim applies only when you eat bread
• When you can, prepare something for this meal rather than only finishing Shabbat leftovers
• Many light candles on or near the table

Four candles (optional — Baal Shem Tov, Siddur Tzelota D'Yisrael; Sefer Amrei Esh):
Some light four candles at Melave Malka. Chassidic tradition teaches that King David is spiritually present at this meal. A common practice (Rabbi Aaron Roth, Shulchan HaTahor): dedicate one candle each to King David, Eliyahu HaNavi, Rabbi Meir Baal HaNess, and the Baal Shem Tov — or choose any four tzaddikim. Light them at the table, name each dedication, and ask then for your needs for the week ahead in the merit of the tzaddikim.

If you are truly too full from Shabbat, you are not required to force yourself — but even a little food at a set table honors the mitzvah.

### Kippah (Head covering for men)

*Section: Ongoing mitzvot · ID: `wear_a_kippah_head_covering`*

Jewish men cover their heads throughout the entire day as a sign that G-d is above us.

What it is:
A kippah (כִּפָּה — also called a yarmulke, from the Yiddish) is a small head covering — a skullcap worn on top of the head. The word kippah means "dome" or "vault," and the word yarmulke comes from an Aramaic phrase meaning "awe of the King."

Why we wear it:
The Talmud records that covering the head cultivates yirat Shamayim — literally "fear of Heaven," which in Jewish thought means a constant awareness that G-d is present and that our actions matter. Wearing a kippah is a visible, ongoing reminder of that awareness.

Practical notes:
• Wear it all day, every day — not just during prayer or Torah study
• There is no need to wear a kippah while bathing or swimming
• Any size, color, or style is acceptable; follow your community's custom
• Many men wear a hat or a larger covering over the kippah during prayer as an added sign of respect

For women:
Married women are obligated in hair covering — see the modesty section.

### Tzitzit (Fringed garment)

*Section: Ongoing mitzvot · ID: `wear_tzitzit_recommended_for_divine_protection`*

The Torah commands Jewish men to wear fringes on the corners of four-cornered garments — a constant physical reminder of all G-d's commandments.

What tzitzit are:
Tzitzit (צִיצִית — fringes or tassels) are knotted strings attached to each corner of a four-cornered garment. Each set of tzitzit has 8 threads and 5 knots. A famous derash (Bamidbar Rabbah) links the gematria of tzitzit (600) plus 8 threads and 5 knots to 613 — a symbolic reminder of all the mitzvot, not a halachic calculation.

The Torah source:
"Speak to the Children of Israel and tell them to make fringes on the corners of their garments... so that you may look upon them and remember all the commandments of G-d." (Numbers 15:38-39)

How to observe it:
Most observant Jewish men wear a tallit katan (small garment) under their shirt all day. During morning prayers (Shacharit), a tallit gadol (large prayer shawl) is worn over the clothing.

Getting started:
• Buy a tallit katan at any Judaica store or online
• Ask your rabbi to show you how to wear it and say the blessing
• Check your tzitzit periodically — if a string breaks, it may invalidate them

### Keep Kosher (Kashrut)

*Section: Ongoing mitzvot · ID: `keep_kosher`*

Kashrut is the system of Jewish dietary laws that governs what we eat, how food is prepared, and how meat and dairy are kept separate.

What kosher means:
Kosher (כָּשֵׁר) literally means "fit" or "proper." Food that meets all the Torah's dietary requirements is called kosher. Food that does not is called treif (טְרֵיף — literally "torn," referring to torn-apart animals).

The main rules:

Permitted and forbidden animals:
• Land animals must have split hooves AND chew their cud: beef, lamb, and goat are kosher; pork is not
• Fish must have fins AND scales: salmon, tuna, and carp are kosher; shrimp, lobster, and catfish are not
• Chicken, turkey, duck, and other domestic fowl are kosher

Shechita (kosher slaughter):
• All meat must be slaughtered by a shochet — a specially trained and G-d-fearing slaughterer — using a swift, painless cut. This is called shechita (שְׁחִיטָה)
• After slaughter, the meat is salted to remove blood (melicha), since consuming blood is forbidden

Separating meat and dairy (basar b'chalav):
• Meat and dairy may never be eaten together, cooked together, or served on the same dishes
• After eating meat, you wait a period of time before dairy. Six hours is standard in many communities, but customs vary (some wait one or three hours) — follow your rabbi and community
• Separate sets of dishes, pots, and utensils are kept for meat and dairy

Hechsher:
• A hechsher (הֶכְשֵׁר) is a certification mark on packaged foods indicating rabbinical supervision. Look for a recognized symbol on labels.

Why these laws:
The Torah gives these laws as divine commands. Beyond the reason, observing them elevates every meal into an act of service to G-d and builds discipline and consciousness in our daily lives.

Getting started:
Consult your local Orthodox rabbi before setting up a kosher kitchen. Many people begin by removing non-kosher meat and seafood, then work toward full observance.

### Shmirat HaLashon (Guard your speech)

*Section: Ongoing mitzvot · ID: `shmirat_halashon`*

Speech is one of the most powerful forces in Jewish life — it can build people up or destroy them in an instant. Shmirat halashon (שְׁמִירַת הַלָּשׁוֹן — guarding the tongue) is the ongoing mitzvah to watch what we say.

What to avoid:
• Lashon hara (לָשׁוֹן הָרָע — "evil tongue"): saying true or untrue derogatory words about another person — their character, their business, their children, and the like — even without intent to harm. If the information would lower the listener's opinion of that person, it is generally forbidden. Speaking lashon hara about the Land of Israel (Eretz Yisrael) is also forbidden.
• Rechilut (רְכִילוּת): tale-bearing — passing words from one person to another in a way that creates conflict.
• Ona'at devarim: hurtful or humiliating speech, insults, mockery, and needless argument.
• Falsehood and misleading others in speech.

Why it matters:
The Talmud compares lashon hara to murder — reputations and relationships can be killed without raising a hand. The Chafetz Chaim (Rabbi Yisrael Meir Kagan, 19th century) devoted his life to teaching these laws because they protect the dignity of every Jew.

How to practice:
• Before sharing news about someone, ask: Is it necessary? Is it kind? Would I want this said about me?
• When you hear gossip, do not repeat it; you may change the subject or gently defend the person spoken about.
• Use speech for encouragement, truth, prayer, and Torah.

This checklist item is a daily reminder to be mindful — not a scorecard of perfection. Ask your rabbi when a specific situation is permitted (e.g., warning someone of harm).

### Business ethics (Honesty in work and money)

*Section: Ongoing mitzvot · ID: `business_ethics`*

Jewish law applies in the marketplace and office just as in the synagogue. Honest weights, fair deals, and trustworthy conduct are Torah obligations — not optional "extra credit."

What this includes:
• Geneivat da'at (גְּנֵבַת דַּעַת — deception): misleading a customer or employer about price, quality, terms, or your qualifications.
• Ona'ah (אוֹנָאָה): overcharging or underpaying when the other party does not know the fair value. A variation of one-sixth or more above or below fair market price triggers the Torah laws of financial fraud, requiring the overcharged amount to be fully refunded.
• Keeping your word: honoring agreements, deadlines, and commitments; not making promises you cannot keep.
• Paying workers on time: wages owed must be paid when due (Leviticus 19:13).
• Not stealing in any form — including time theft, expense fraud, or taking what is not yours.
• Taxes and debts: fulfilling legal obligations honestly; not cheating the government or private parties.

Key idea:
"In righteousness you shall judge your fellow" (Leviticus 19:15) — business dealings are judged by the same moral standard as ritual mitzvot. A Jew's integrity (yosher — יֹשֶׁר) is a sanctification of G-d's name in the world.

How to practice:
• Be transparent in sales, billing, and negotiations.
• When in doubt, ask a rabbi or trusted advisor about a specific contract or industry practice.
• Treat employees, clients, and employers with the dignity the Torah demands.

Check this off as a daily intention to act with honesty in all financial and professional interactions.

### Tzedakah (Charity) — Maaser

*Section: Ongoing mitzvot · ID: `tzedakah_maaser`*

Giving charity (tzedakah — צְדָקָה) is a constant mitzvah. A foundational custom is maaser kesafim (מַעֲשֵׂר כְּסָפִים) — setting aside about ten percent of one's income for charity.

How to calculate:
Maaser is generally figured from net earnings, not gross: start with what you actually receive, then deduct taxes, necessary work-related expenses, and reasonable business overhead before taking ten percent. Ask your rabbi for guidance on your specific situation (self-employment, benefits, one-time windfalls, etc.).

Where to give:
Halachic priorities place local poor individuals, Torah institutions, and the needy of Israel at the top of the list. Excellent choices often include local family relief organizations, community food pantries, and trusted charities that serve those in need in your area and in Israel.

If money is tight:
Jewish law teaches that your own livelihood and basic needs come first. If you cannot comfortably afford the full ten percent without real hardship, you are not expected to compromise your financial stability. Give a minimal, symbolic amount when you can so you still take part in this vital mitzvah — and increase when circumstances allow.

This checklist item is a daily reminder to plan your giving with intention. Confirm amounts and recipients with your rabbi.

### Honor your father and mother

*Section: Ongoing mitzvot · ID: `honor_father_and_mother`*

The Torah commands: "Honor your father and your mother" (Shemot 20:12) — among the Ten Commandments, and equal in weight to honoring G-d Himself (Kiddushin 30b). Kibbud av va'em (כִּבּוּד אָב וָאֵם) is a lifelong mitzvah for men and women alike.

Two dimensions:
• Kibbud (כִּבּוּד — honor): active care — food, clothing, escort, respectful speech, and financial support when parents need it (Shulchan Arukh Y.D. 240; Rambam, Mamrim 6).
• Morah (מוֹרָא — reverence): not sitting in a parent's place, not contradicting them sharply, and not calling them by name — guarding their dignity even in disagreement (Y.D. 240:2–3).

Daily practice while parents are alive:
• Speak with respect — tone and words matter as much as deeds.
• Help with their needs: meals, errands, medical appointments, household tasks, especially as they age.
• Stand to honor them when they enter — the details of how often differ by minhag (see your nusach note below).
• Share good news; shield them from unnecessary worry or embarrassment.
• If a parent asks you to violate Torah law, decline gently — obedience to Hashem comes first (Y.D. 240:15).

Limits:
You are not required to impoverish yourself or your own household (Y.D. 240:5). A parent who behaves abusively or endangers a child may require halachic guidance — the mitzvah does not mean submitting to harm.

When parents are no longer alive:
Honor continues. Many fulfill it through:
• Reciting Kaddish — especially for a parent (eleven months for a father or mother).
• Torah study and good deeds l'ilui nishmatam (for the elevation of their soul).
• Charity given in their memory.
• Visiting the grave, particularly on yahrzeit and before the High Holidays.
• Carrying forward the values and good name they left you — speaking of them with respect.

In-laws:
Honoring your spouse's parents is a great mitzvah and promotes shalom bayit, but the Torah-level obligation centers on your own father and mother (Y.D. 240:24).

This item is a daily intention: one concrete act of kibbud or morah — a call, a kind word, help with a need, or learning done in a parent's memory.

**Ashkenaz:**

Standing (kima — קִימָה): Ashkenaz custom (Rama Y.D. 240:4): rise once in each 24-hour period for each parent when you see them — that fulfills the daily obligation of standing. If you are in their presence throughout the day, stand when they enter the room. When unsure, standing more often is praiseworthy.

**Sefard:**

Standing (kima): Sephardi custom (Shulchan Arukh Y.D. 240:4; Yalkut Yosef): stand each time a parent enters within your presence — roughly within four amot (about six to eight feet) — and remain standing until they sit or pass on.

**Edot HaMizrach:**

Standing (kima): Edot HaMizrach follow Shulchan Arukh (Y.D. 240:4): stand when a parent enters your presence and remain standing until they are seated or leave — each time they approach, not only once per day.

**Chabad:**

Standing (kima): Chabad follows the Rama (Y.D. 240:4): standing once per day for each parent when you see them fulfills the daily obligation. If you share a home or spend the day together, stand when they enter your space.

### Chinuch (Educating children)

*Section: Ongoing mitzvot · ID: `chinuch_educating_children`*

Parents are obligated in chinuch (חִנּוּךְ — training children in mitzvot). The Torah commands: "Teach them to your children" (Devarim 11:19) — so they learn Torah and observe mitzvot (Peninei Halakha, Laws of Children 24:1). Before bar/bat mitzvah, a child is not personally Torah-obligated in mitzvot, but parents have a Torah obligation to train them (Devarim 11:19; Rambam, Chinuch 1:1; Peninei Halakha, Laws of Children 24:1).

Core principles (from the poskim):
• "Educate the youth according to his way" (Mishlei 22:6) — each child has his or her own pace.
• Train a child in a positive mitzva when he understands what it involves and can observe it properly — k'fi daato (Peninei Halakha 24:1).
• Teach the proper details, not just the general idea — e.g. use a kosher etrog when training for the Four Species.
• "The external awakens the internal" — action and habit come first; understanding deepens with age.

Classical roadmap — Pirkei Avot 5:21 (Yehudah ben Teima), cited throughout Jewish education:
• Age 5 — mikra (Scripture)
• Age 10 — Mishnah
• Age 13 — mitzvot (boys at 13, girls at 12 become obligated)
• Age 15 — Gemara / deeper Torah study
This is the traditional structure of Jewish learning, not a rigid checklist for every family — your rabbi and school guide the details.

What major sources say about specific stages:

When a child begins to speak — teach "Torah tziva lanu Moshe" and the first verse of Shema (Sukka 42a; Mishnah Berurah).

Around age 3 — begin teaching Aleph-Bet (Rema YD 245:8). Habituate children to brachot and tefillah, as Torah learning begins then (Bava Batra 21a; Sukka 42a; Shulchan Aruch YD 245:5 — Peninei Halakha 24:1). Boys: many communities begin kippah and tzitzit (Shaarei Teshuva 17:2). Encourage listening to Kiddush and Havdalah; when the child understands Shabbat, ensure he or she hears them properly (Peninei Halakha 24:1).

Before age 3 — no obligation to stop a child from prohibited acts (Peninei Halakha 24:2).

When a child understands permitted vs. forbidden — usually around age 3 — stop him from eating non-kosher food, turning lights on/off on Shabbat, etc. (Peninei Halakha 24:2; many poskim note a similar stage around age 4 for comprehension of "forbidden").

Age 6 or 7 — the main age of chinuch for most positive mitzvot, including brachot and prayer, when children study Torah in earnest (Peninei Halakha 24:1 — general rule from age 6, varying by mitzvah). Poskim teach brachot of Kriyat Shema, Shema, and Shemoneh Esrei around this age (see Children in Halacha / Mishnah Berurah).

Varies by mitzvah (Peninei Halakha):
• Simple action mitzvot (shofar, sukkah) — often from about age 5.
• Mitzvot requiring intent (e.g. mourning on Tisha B'Av) — closer to age 7.
• Tzitzit — when the boy knows how to wear them correctly and recite the bracha (Peninei Halakha 24:1).
• Tefillin — only shortly before bar mitzvah, when he can maintain proper body and concentration (Sukka 42a; Mishnah Berurah 343:3; Aish).

Yom Kippur fasting for children follows specific rabbinic ages (partial fast from 9, etc.) — see Peninei Halakha, Laws of Yom Kippur ch. 9.

Tone: mitzvot should be a joy, not a burden; correct with love. Both parents share in chinuch; ask your rabbi for your children's ages, nusach, and community minhag.

This checklist item: mark it when you actively invested in your children's chinuch today — teaching, modeling, or guiding a mitzvah at the level halacha expects for their stage.

### Mezuzot on your doorposts

*Section: Ongoing mitzvot · ID: `have_mezuzot_on_your_doorposts`*

The Torah commands us to write G-d's words "on the doorposts of your house and your gates" (Deuteronomy 6:9). This is fulfilled by affixing a mezuzah to each doorpost.

What a mezuzah is:
A mezuzah (מְזוּזָה — literally "doorpost") is first and foremost the klaf — a small parchment scroll on which a trained scribe (sofer) has hand-written two passages from the Torah (Deuteronomy 6:4-9 and 11:13-21). On the back, the name "Shaddai" is written — one of G-d's names, and an acronym for Shomer Daltot Yisrael (Guardian of Israel's doors).
• The mitzvah is the kosher klaf affixed to the doorpost. A decorative case is optional (you may affix the klaf directly), but most people use one to shield the parchment from dust, damage, and weather.

Protection and benefits:
Jewish tradition teaches that mezuzot guard the home and those who dwell in it — the scroll at the threshold is a sign that G-d watches over your doors. Each time you pass a mezuzah, you are reminded of the Shema, G-d's oneness, and the mitzvot; that steady reminder builds Jewish awareness through ordinary comings and goings. Our Sages link careful mezuzah observance with wellbeing in the home. Many families find mezuzot mark the house as unmistakably Jewish, help teach children, and bring a sense of peace and kedushah (holiness) to daily life.

Where to put one:
• Every regularly-used doorway in your home requires a mezuzah — front door, bedroom, living room, kitchen, office, etc.
• Not required on: bathrooms, small closets under a certain size, storage rooms
• Affixed on the right doorpost as you enter, in the upper third of the post, at a slight inward angle (top tilted inward)

Very important:
The klaf (parchment) must be kosher. Many decorative mezuzah cases are sold empty or with non-kosher scrolls. Always buy your klaf from a reliable Judaica store or rabbi, and have it checked by a sofer every few years.

The blessing when affixing:
"Baruch Atah Ado-nai Eloheinu Melech ha'olam, asher kid'shanu b'mitzvotav v'tzivanu lik'boa mezuzah."

### Tevilat Keilim (Immersing new vessels)

*Section: Ongoing mitzvot · ID: `immerse_food_vessels_in_mikveh`*

When you acquire new metal or glass dishes, pots, or utensils that were manufactured by a non-Jew, they require ritual immersion (Tevilat Keilim) before they can touch food. Glazed ceramic (such as china or porcelain with a glaze) and similar materials often require immersion as well — usually without a bracha.

Who triggers the obligation: the manufacturer who made the item — not the store that sold it. A pot made by a non-Jewish company still requires tevilah even if you bought it from a Jewish-owned shop; a pot made by a Jew does not require tevilah even if you bought it from a non-Jewish retailer. Ask your rav about borderline cases.

What this is:
Tevilat keilim (טְבִילַת כֵּלִים — "immersion of vessels") is a mitzvah derived from Numbers 31:23. Most poskim treat it as rabbinic today; some view it as Torah-level — ask your rav. Just as the human body requires immersion for certain transitions, so too do our vessels when entering the Jewish home.

Which vessels need immersion:
• With a blessing: metal and glass vessels used for food
• Without a blessing (rabbinic requirement): glazed ceramic and some other materials — ask your rabbi for unusual items
• No immersion needed: plastic, wooden items, disposables, and items manufactured by a Jew

How to do it:
Recite the blessing, then fully submerge the vessel. Let go for a moment so the water surrounds the entire item — a brief release ensures nothing is still touching your hand above the waterline. Use a kosher mikveh; if there is no mikveh nearby, you may immerse in the ocean, a lake, or a river — whether a particular body of water qualifies depends on several factors, so ask your rabbi. Your local mikveh or rabbi can advise on times and setup.

Ovens and appliances:
For ovens and similar items, usually only the removable racks (or parts that directly touch food) are immersed — not the entire appliance. Some items do require immersing the whole unit; halacha depends on the type. If you are worried about damage — digital displays, electronics, or fragile equipment — ask your rabbi; sometimes one may be lenient and not immerse.

This is a situational mitzvah — check it off when you have completed immersing the required vessels.

### 100 Blessings every day

*Section: Ongoing mitzvot · ID: `100_daily_blessings`*

The rabbis instituted a practice of saying 100 blessings (brachot) every day, keeping us perpetually aware of G-d's gifts.

Who is obligated:
The Talmudic practice was framed for men (Menachot 43b). Many poskim hold that women should also aim for 100 brachot daily; others treat it as optional for women — ask your rav.

What a bracha is:
A bracha (בְּרָכָה — blessing, plural: brachot) is a short formula said before or after performing a mitzvah or enjoying something from the world. Every bracha begins: "Baruch Atah Ado-nai Eloheinu Melech ha'olam..." — Blessed are You, L-rd our G-d, King of the universe...

Where the 100 come from on a weekday:
• Morning blessings (Birchot HaShachar): ~15
• Morning prayer service (Shacharit): ~40
• Afternoon prayer (Mincha): ~20
• Evening prayer (Maariv): ~18
• Blessings before and after food: ~10
Total: easily 100 on a regular weekday

On Shabbat and Yom Tov (Jewish holidays):
The prayer services are shorter and the Amidah has fewer blessings, so you will fall short of 100. To make up the count:
• Eat additional snacks and say brachot before and after each
• Smell fragrant spices or fruit and say the blessing on fragrances (besamim — בְּשָׂמִים)

Ein Kelokeinu (Ashkenaz custom on Shabbat & Yom Tov):
Ashkenazim worldwide recite Ein Kelokeinu (אֵין כֵאלֹקֵינו) at the end of Shacharit on Shabbat and festivals — in Israel and the Diaspora alike. (On weekdays, Ashkenazim omit it; Sephardim and Chabad recite it daily.) Ashkenaz recite it on Shabbat and Yom Tov especially because the weekday Amidah has nineteen blessings while the Shabbat Amidah has only seven, so you fall further short of one hundred on those days. Following Rashi (Menachot 43b), some rabbis hold that the praises in this hymn count toward the daily hundred when you are lacking — not as full brachot with Shem Umalchus (G-d's name and kingship in the usual formula), but as intentional recognition of G-d. The Taz (OC 642) cites this as a reason for the custom. Many count twenty lines (five stanzas of four); some count twenty-one when the opening line is repeated at the end, as in many siddurim. An older reckoning (Shibbolei Haleket) counts twelve, parallel to the twelve weekday Amidah blessings omitted on Shabbat. Note on Yom Kippur: Unlike the minor fasts, Ein Kelokeinu is universally chanted at the conclusion of the Musaf service (or Ne'ilah) on Yom Kippur across mainstream Ashkenazic, Sephardic, and Chassidic traditions. The Mishnah Berurah prefers making up the count with real brachot — food, fragrances, or answering Amen to aliyot — when you can, rather than relying only on these praises.

Tzaddik (צדיק) — a framework some rabbis recommend:
Some teachers suggest using the four letters of the word Tzaddik as a daily checklist alongside the 100 brachot:
• צ (Tzadi = 90): Answer 90 Amens each day (responding "Amen" when you hear blessings in prayer or from others).
• ד (Dalet = 4): Respond to the 4 Kedushot (קְדֻשּׁוֹת — sanctification prayers) in the daily services: (1) Kedushah in Yotzer Or (the blessing before the morning Shema), (2) Kedushah in the Shacharit Amidah repetition, (3) Kedushah D'Sidra / Uva L'Tzion (recited at the end of Shacharit), and (4) Kedushah in the Mincha Amidah repetition.
• י (Yud = 10): Say 10 Amens to "Yehei Shemei Rabbah" (יְהֵא שְׁמֵהּ רַבָּא) during Kaddish throughout the day.
• ק (Kuf = 100): Recite 100 brachot (blessings) each day — the obligation described above.

### Be Fruitful and Multiply

*Section: Ongoing mitzvot · ID: `be_fruitful_and_multiply`*

The Torah commands: "Be fruitful and multiply" — Pru u'Revu (פרו ורבו, Bereishit 1:28). Jewish men are obligated to marry and father children (Yevamot 61b; Shulchan Arukh, Even HaEzer 1:1).

Fulfilling the obligation:
A man fulfills pru u'revu once he has at least one son and one daughter who survive, each capable in principle of having children of their own (Yevamot 62a).

Marriage within the covenant:
The wife must be Jewish (Shulchan Arukh EH 2:1). Children born to a non-Jewish woman are not considered his under halacha; Jewish identity follows the mother (EH 4:19; Yevamot 17a). A man is expected to build a Jewish home — not to remain unmarried by choice (Rambam, Ishut 15:16).

A child born to a Jewish woman outside marriage may count toward the mitzvah in principle, but relations outside marriage are forbidden (EH 178; Devarim 23:18). Marriage is the proper path.

Beyond the minimum — la'arev ba'olam (לערב בעולם):
After the Torah obligation is met, the Sages teach a further mitzvah to continue having children — to populate the world with Jewish souls (Yevamot 62b; EH 1:5–8; Rema EH 1:8). How far to pursue this depends on health, circumstances, and personal psak.

When children are not possible:
One who cannot have children despite reasonable effort may be exempt — consult a qualified rav (EH 1:5–6). Poskim discuss infertility treatment, adoption, and other paths; rulings differ. The obligation to seek marriage remains even when fulfillment proves difficult.

### Prepare for Shabbat (Erev Shabbat checklist)

*Section: Prepare for Shabbat · ID: `prepare_for_and_observe_shabbat_and_festivals`*

Shabbat is the holiest day of the week — the Jewish Sabbath. It begins at sunset on Friday and ends when three stars appear on Saturday night (approximately 25 hours).

What to prepare before Shabbat:
1. Cooking: All cooking must be done before Shabbat begins. A blech (a metal sheet covering the stove flame) or a pre-set hot plate keeps food warm on Shabbat without cooking.
2. Timers: Electric lights, the hot plate, and other appliances can be left on or set to turn on/off using a Shabbos timer (pre-set before Shabbat).
3. Lay out what you need: siddur (prayer book), Chumash (Torah book), clothes, children's items — anything you will want.
4. Candles: Light Shabbat candles 18 minutes before sunset (see next item).

On Shabbat — things we do NOT do:
• Write, draw, or erase
• Cook, bake, or light fire
• Turn electricity on or off (including phones, lights, and appliances)
• Drive or ride in a motorized vehicle
• Handle money or engage in commerce

On Shabbat — what we DO:
• Attend synagogue for Friday night and Saturday morning services
• Say Kiddush — a blessing over wine sanctifying Shabbat
• Eat three festive meals (Friday night, Saturday afternoon, and a third before nightfall)
• Study Torah, sing Shabbat songs (zemiroth), rest, and spend time with family

### Hadlakat Nerot (Shabbat candle lighting)

*Section: Prepare for Shabbat · ID: `shabbat_candles`*

Lighting candles on Friday afternoon is one of the most beloved practices in all of Judaism — it officially welcomes Shabbat into the home.

What it is:
Hadlakat Nerot (הַדְלָקַת נֵרוֹת — lighting of candles) is a rabbinic mitzvah performed weekly before Shabbat. The Shabbat candles bring light and peace into the home, honoring the day's holiness.

Who lights:
Traditionally the woman of the household lights. If there is no woman in the home, a man lights. Ashkenazi and Chabad custom: young girls and single daughters in the home may light their own candles with a bracha (often one candle per child). Sephardic custom (Shulchan Arukh; Rav Ovadia Yosef): daughters living under their parents' roof do not recite a bracha on their own candles if the mother has already lit — the mother's candles establish light for the home; a duplicate bracha would be a bracha levatala.

When to light:
Exactly 18 minutes before sunset on Friday (some Ashkenazic communities light 20-22 minutes before; Sephardic communities often light closer to sunset). The app shows the candle-lighting time for your location.

How many candles:
• The minimum is two candles — one for Zachor ("remember" Shabbat, from Exodus 20:8) and one for Shamor ("guard" Shabbat, from Deuteronomy 5:12)
• Many women add one candle per child

The blessing:
There are two customs:
• Ashkenazi custom: Light the candles first, then wave hands over the flames, cover the eyes, recite the blessing, and uncover. Lighting is done first because reciting the blessing is considered accepting Shabbat — after which relighting would be forbidden. Covering the eyes allows the blessing to precede benefiting from the light.
• Sephardi custom: Recite the blessing first, then light the candles.
Follow your family's custom. When in doubt, ask your rabbi.

The blessing: "Baruch Atah Ado-nai Eloheinu Melech ha'olam, asher kid'shanu b'mitzvotav v'tzivanu l'hadlik ner shel Shabbat."

Important:
Once you have lit the candles and said the blessing, Shabbat has begun for you. Do not light additional fire or use electricity after this point.

### Shnayim Mikra (Weekly Torah portion)

*Section: Torah Study · ID: `weekly_parsha_shnayim_mikra`*

Why Shnayim Mikra is worth the effort — teachings of our rabbis:

The weekly Torah portion is not only a calendar item. It is your personal share in the Torah that sustains the Jewish people. The sages tie public Torah reading to the survival of the world itself (Shabbat 119b). When you learn the same parsha the community will hear on Shabbat, you join a chain stretching back to Sinai — and the Shechinah dwells wherever words of Torah are spoken (Avot 3:2).

Torah study protects the learner; it is among the three guardians of life in the Talmud (Sotah 21a). Tradition describes words of Torah ascending and bringing merit. Shnayim Mikra is a manageable weekly rhythm: a little each day builds a bond with Hashem that no podcast can replace.

Each week, the Jewish community reads a designated section of the Torah in synagogue on Shabbat morning. Every individual is personally required to study that same section during the week.

What this is:
Shnayim Mikra v'Echad Targum (שְׁנַיִם מִקְרָא וְאֶחָד תַּרְגּוּם — "twice the text and once the translation") is the requirement to read each verse of the weekly Torah portion:
• Twice in the original Hebrew
• Once in Aramaic translation (Targum Onkelos) or Rashi's commentary, which many use as a substitute for the translation to better understand the plain meaning (Shulchan Arukh and Mishnah Berurah O.C. 285:2)

For those learning Hebrew:
If you cannot yet read the Hebrew and Aramaic fluently, you can read from a transliterated Chumash, and many authorities allow an accurate English translation in place of Targum Onkelos. However, learning to read the Hebrew text is one of the most rewarding investments a Jew can make.

When to do it (Shulchan Arukh O.C. 285:4):
• L'chatchila (ideal): finish before you eat on Shabbat — before the Shabbat meal and ideally before the public Torah reading.
• If not finished before the meal: complete it after the meal, before Mincha on Shabbat.
• If you still fell behind: some poskim allow until Wednesday of the following week; others until Simchat Torah, when the congregation completes the annual cycle (see commentaries on O.C. 285:4).

Most people spread the parsha across the week — a few verses or an aliyah each day. Missing the ideal pre-Shabbat morning window does not mean the mitzvah is lost; these fallback times remain valid until they pass.

How this checklist tracks it: the item stays available through Shabbat (not marked failed at Friday night). A new parsha appears when the calendar week turns; if you fell behind on the prior portion, you may still make it up per O.C. 285:4 even after the new parsha shows here.

### Torah study — halachot for daily life

*Section: Torah Study · ID: `torah_study_women`*

Why Torah learning matters for you — what the sages teach every Jew:

Torah is not only men's work in the spiritual sense. Whenever you learn — halacha for your home, stories of our matriarchs, or the prayers you say — you bond with Hashem. Pirkei Avot promises that when words of Torah pass between people, the Shechinah dwells among them (Avot 3:2–3). That applies to a woman at her desk with a Kitzur Shulchan Arukh just as it applies in a beit midrash.

The sages list Torah study among the three things that protect a person's life (Sotah 21a). Tradition teaches that words of Torah rise on high and bring merit and advocacy for the learner and her family. Learning the laws of Shabbat, kashrut, and taharat hamishpacha is not bureaucracy — it is how you bring holiness into the rooms where your family lives.

Torah is an etz chaim, a tree of life (Mishlei 3:18). Even a few minutes of focused learning sweetens the day and connects mitzvot into a whole picture instead of isolated rules.

What applies to women in Torah Study:

Women are exempt from the formal, continuous mitzvah of Talmud Torah (תַּלְמוּד תּוֹרָה) incumbent on men — including the men's day-and-night study schedule on this checklist. You are also not required to do Shnayim Mikra (weekly parsha reading) or Chabad's daily Hayom Yom / Chitas program, though you may learn them voluntarily.

Your obligation:
You are obligated to study the practical halachot (הֲלָכוֹת — laws) that apply to your daily life so you can observe them properly: blessings, kashrut, Shabbat, prayer you say, tzniut, taharat hamishpacha (if married), and other mitzvot relevant to you.

When to learn:
This item is for any time of day or night. Even a few minutes of focused halacha learning counts. There is no upper limit if you wish to learn more.

Evening prayer (separate section):
Women say Kriat Shema al HaMitah (Bedtime Shema) before sleep. You do not need Shema with its Maariv blessings as a separate obligation.

Good starting points:
• [Kitzur Shulchan Arukh](https://www.sefaria.org/Kitzur_Shulchan_Arukh) or a women's halacha book from your community
• Daily halacha — [DailyHalacha.com](https://www.dailyhalacha.com/) or [Halachipedia](https://www.halachipedia.com/)
• [Sefaria.org](https://www.sefaria.org/) — search topics you need for your home and family

Ask your rabbi or rebbetzin which areas to prioritize as you grow in observance.

### Talmud Torah — daytime (Daily Torah Study)

*Section: Torah Study · ID: `torah_study_day`*

Why the sages urge us to learn — and why it is worth starting today:

Torah study is not only an obligation. It is the deepest everyday way a Jew bonds with Hashem. The Mishnah teaches that Talmud Torah k'neged kulam — learning Torah outweighs every other mitzvah combined (Peah 1:1). When you learn, you are cleaving to the G-d who gave us life — not escaping the world, but filling it with meaning.

What our rabbis say happens when you learn:

• Hashem draws near — Pirkei Avot teaches that when even two people sit together and words of Torah pass between them, the Shechinah (Divine Presence) dwells among them (Avot 3:2–3). Alone at your kitchen table with a siddur or Chumash, you are still in that company.

• Protection in this world and the next — The Talmud lists Torah study among the three things that guard a person's life (alongside fear of sin and acts of kindness; Sotah 21a). Tradition describes words of Torah ascending on high and becoming angels that advocate for the learner. However you understand that image, the sages are clear: Torah surrounds your home, your children, and your decisions with merit.

• Sweetness, not drudgery — Birkat HaTorah asks Hashem to make Torah "sweet in our mouths." Torah is called an etz chaim, a tree of life (Mishlei 3:18). The more you return to it, the more mitzvot feel connected instead of scattered.

• You help hold up the world — The Talmud teaches that the world endures because of the breath of children learning Torah, and that Jerusalem was lost in part when Torah study was neglected (Shabbat 119b). Your learning tips the scale toward blessing for the Jewish people.

You do not need to be a scholar. A few focused minutes count. The reward is real even before you feel it.

What Torah study is:
Talmud Torah (תַּלְמוּד תּוֹרָה — study of Torah) means engaging with any part of the Jewish textual tradition: the five books of the Torah, the Prophets and Writings (Tanakh), Mishnah, Talmud, halachic (legal) works, Kabbalah (Jewish mysticism), Chassidut (Chassidic teachings), Mussar (ethical literature), or any authentic Torah book.

The obligation:
Every Jew should learn at least a little Torah during the day. There is no upper limit — learn as much as you are able. Set aside fixed times (keviyut — קְבִיעוּת) when possible; even a small amount done consistently and purposefully is enormously valuable.

This checklist item is for daytime study. There is a separate item for Torah study at night.

Good starting points for newcomers:
• Daily halacha: [DailyHalacha.com](https://www.dailyhalacha.com/) and [Halachipedia](https://www.halachipedia.com/) for looking up laws
• [Chabad.org](https://www.chabad.org/) or [Sefaria.org](https://www.sefaria.org/) for structured texts with translation
• A weekly Parsha class (in person or on [TorahAnytime.com](https://www.torahanytime.com/))
• [Daf Yomi](https://www.sefaria.org/daf-yomi) — one page of Talmud per day (completes the entire Talmud in 7.5 years)
• The [Kitzur Shulchan Arukh](https://www.sefaria.org/Kitzur_Shulchan_Arukh) — a concise summary of practical Jewish law for daily life

### Talmud Torah — nighttime (Daily Torah Study)

*Section: Torah Study · ID: `torah_study_night`*

Why learn at night — what the sages promise:

Nighttime Torah study has a special place in Jewish tradition. The Talmud praises one who establishes words of Torah at night — the verse "I rise at midnight to thank You" applies to him (Tehillim 119:62; Shabbat 119a). After the day's noise, learning at night is quiet cleaving to Hashem: the Shechinah dwells wherever words of Torah are spoken (Avot 3:2–3).

Our rabbis teach that Torah study protects the learner — it is counted among the three guardians of a person's life (Sotah 21a). Words of Torah are described in Kabbalistic tradition as ascending and forming advocates on high. Even a short night seder before sleep can settle the mind, sanctify the end of the day, and surround your household with merit.

Torah study is equal in weight to all other mitzvot combined (Peah 1:1). You do not need hours — consistency matters more than length.

The obligation:
Every Jew should learn at least a little Torah during the day and also at night. There is no upper limit to how much you may learn — the more Torah you study, the greater the mitzvah.

This checklist item is for nighttime study (from nightfall until the end of the night's obligations). Daytime study is tracked separately.

What counts:
Any authentic Torah learning — Chumash, Tanakh, Mishnah, Talmud, halacha, Mussar, Chassidut, or a structured daily program (such as Chitas for those who follow that custom).

Even a few minutes of focused learning at night fulfills this mitzvah. Consistency matters more than length.

### Modeh Ani (Thank G-d upon waking)

*Section: Upon waking · ID: `modeh_ani_upon_waking`*

The very first act of each day:

What it is:
Modeh Ani (מוֹדֶה אֲנִי — literally "I am grateful") is a two-line prayer said the moment you open your eyes. It is the most basic expression of Jewish gratitude — thanking G-d for returning your soul after sleep.

The tradition:
Jewish thought teaches that sleep is a nightly experience of near-death — our soul partially leaves the body each night and is returned in the morning. Modeh Ani acknowledges that G-d chose to give you another day.

The Hebrew text and translation:
"Modeh ani lifanecha, Melech chai v'kayam, shehechezarta bi nishmati b'chemla — raba emunatecha."
"I thank You, living and eternal King, for returning my soul to me with compassion — how great is Your faithfulness."

How to do it:
Say it while still in bed, before getting up, immediately upon waking from your primary nighttime sleep — before doing anything else. Modeh Ani is one of the only parts of davening you can say without washing your hands first — then proceed to Netilat Yadayim (negel vasser; see the next item).

Nighttime sleep vs daytime naps:
You may repeat these words anytime as personal gratitude or mindfulness, but that is not the official morning prayer and does not carry the same halachic status.

Traditional practice reserves Modeh Ani for waking from major nighttime sleep. After a daytime nap, it is not required and is typically not recited — simply wash your hands (negel vasser) per the rules in the next item.

### Netilat Yadayim (Ritual morning hand washing)

*Section: Upon waking · ID: `ritual_hand_washing`*

Immediately after saying Modeh Ani, wash your hands in a specific ritual way before doing anything else.

What it is:
Netilat Yadayim (נְטִילַת יָדַיִם — literally "lifting of the hands") is a ritual washing — not just hygiene, but a spiritual act. During deep sleep, the soul partially ascends and a spiritual impurity called Ruach Ra'ah (evil spirit) settles on the fingers and nails. This washing removes it and prepares us for prayer, Torah, and the day ahead.

Different minhagim disagree whether morning washing is mainly to remove ruach ra'ah upon waking or mainly as preparation for prayer — so when you say Al Netilat Yadayim differs between Ashkenaz, Sephardi, Edot HaMizrach, and Chabad.

How to pour (all traditions):
1. Ideally have a cup, bottle, or washcup filled with water and a basin ready at your bedside before you sleep
2. Pour over your right hand, then left, alternating three times: right, left, right, left, right, left
3. Dry your hands when your minhag calls for it (see blessing timing below)

Blessing timing overview (set nusach in Settings for your full text):
• Ashkenaz: usually no blessing at bedside; blessing after second washing (bathroom + dress), unless you had no bathroom need.
• Sephardi / many Edot HaMizrach: blessing after washing, before drying (Shulchan Aruch); bathroom first only if urgent.
• Some Edot HaMizrach kehillot (e.g. Baladi Yemenite): wash after bathroom — follow your rav.
• Chabad: no blessing on first washing; blessing only after second washing at the sink.

The blessing text:
"Baruch Atah Ado-nai Eloheinu Melech ha'olam, asher kid'shanu b'mitzvotav v'tzivanu al netilat yadayim"

The 4 amot rule:
Before washing, do not walk 4 amot (about 6–8 feet) continuously without stopping — this is considered a full "journey" that allows the impurity to expand. If the sink is farther than 4 amot away:
• Strict / Kabbalistic approach (Zohar, Shulchan Aruch HaRav): walk in segments shorter than 4 amot, pause fully between each, and repeat until you reach the sink
• Mainstream approach (Mishnah Berurah): walking directly to the sink to remove the impurity quickly is also permitted, though the stop-and-go method is praiseworthy
• Lenient approach (Aruch HaShulchan): the entire modern house is considered one domain; walk normally to the sink
Follow your rabbi's guidance. In all opinions, washing normally at the sink still fully removes the impurity even if you walked further first.

If you need to say a blessing before you can wash (e.g. urgent bathroom need and no water nearby):
Rub your hands firmly on a clean, dry surface — a wooden headboard, wall, or clothing. This is called Nikuy (ניקוי — cleaning by friction) and permits saying holy words, but does NOT remove the spiritual impurity. You must still wash with water as soon as possible (Shulchan Aruch OC 4:22, Mishnah Berurah 4:61).

After naps (Modeh Ani is not recited):
When waking from a daytime nap, Modeh Ani is typically omitted — wash your hands (negel vasser) per the rules below:
• Nap under ~30 minutes: the severe Ruach Ra'ah does not apply; wash for cleanliness before prayer but no bracha is required and the 4-amot rule does not apply (Mishnah Berurah 4:34)
• Long daytime nap: mainstream opinion — wash three times alternating but no bracha; strict opinion — treat as morning washing. Ask your rabbi

Other important notes:
• Do not touch your eyes, mouth, or face before washing
• Do not touch food before washing
• The same alternating triple-pour without a blessing is done after: using the bathroom, leaving a cemetery, and before eating bread
• Once you have said Al Netilat Yadayim in the morning, all traditions agree you do not repeat the blessing when you wash again at shul before davening

**Ashkenaz:**

Immediately after saying Modeh Ani, wash your hands in a specific ritual way before doing anything else.

What it is:
Netilat Yadayim (נְטִילַת יָדַיִם — literally "lifting of the hands") is a ritual washing — not just hygiene, but a spiritual act. During deep sleep, the soul partially ascends and a spiritual impurity called Ruach Ra'ah (evil spirit) settles on the fingers and nails. This washing removes it and prepares us for prayer, Torah, and the day ahead.

Different minhagim disagree whether morning washing is mainly to remove ruach ra'ah upon waking or mainly as preparation for prayer — so when you say Al Netilat Yadayim differs between Ashkenaz, Sefard, and Chabad.

How to pour (all traditions):
1. Ideally have a cup, bottle, or washcup filled with water and a basin ready at your bedside before you sleep
2. Pour over your right hand, then left, alternating three times: right, left, right, left, right, left
3. Dry your hands when your minhag calls for it (see blessing timing below)

Ashkenaz minhag — when to say Al Netilat Yadayim:
• Routine: wash upon waking (often pouring into a basin by the bed) without a blessing first.
• Blessing: because most people need the bathroom shortly after waking — and one should not recite blessings while needing to relieve oneself — the custom is to say Al Netilat Yadayim only after the second washing: once you have used the bathroom, gotten dressed, washed again, and are about to dry your hands; then say Asher Yatzar as well.
• If you woke with no urge to use the bathroom: say the blessing on the first morning washing instead.

The blessing text:
"Baruch Atah Ado-nai Eloheinu Melech ha'olam, asher kid'shanu b'mitzvotav v'tzivanu al netilat yadayim"

The 4 amot rule:
Before washing, do not walk 4 amot (about 6–8 feet) continuously without stopping — this is considered a full "journey" that allows the impurity to expand. If the sink is farther than 4 amot away:
• Strict / Kabbalistic approach (Zohar, Shulchan Aruch HaRav): walk in segments shorter than 4 amot, pause fully between each, and repeat until you reach the sink
• Mainstream approach (Mishnah Berurah): walking directly to the sink to remove the impurity quickly is also permitted, though the stop-and-go method is praiseworthy
• Lenient approach (Aruch HaShulchan): the entire modern house is considered one domain; walk normally to the sink
Follow your rabbi's guidance. In all opinions, washing normally at the sink still fully removes the impurity even if you walked further first.

If you need to say a blessing before you can wash (e.g. urgent bathroom need and no water nearby):
Rub your hands firmly on a clean, dry surface — a wooden headboard, wall, or clothing. This is called Nikuy (ניקוי — cleaning by friction) and permits saying holy words, but does NOT remove the spiritual impurity. You must still wash with water as soon as possible (Shulchan Aruch OC 4:22, Mishnah Berurah 4:61).

After naps (Modeh Ani is not recited):
When waking from a daytime nap, Modeh Ani is typically omitted — wash your hands (negel vasser) per the rules below:
• Nap under ~30 minutes: the severe Ruach Ra'ah does not apply; wash for cleanliness before prayer but no bracha is required and the 4-amot rule does not apply (Mishnah Berurah 4:34)
• Long daytime nap: mainstream opinion — wash three times alternating but no bracha; strict opinion — treat as morning washing. Ask your rabbi

Other important notes:
• Do not touch your eyes, mouth, or face before washing
• Do not touch food before washing
• The same alternating triple-pour without a blessing is done after: using the bathroom, leaving a cemetery, and before eating bread
• Once you have said Al Netilat Yadayim in the morning, all traditions agree you do not repeat the blessing when you wash again at shul before davening

**Sefard:**

Immediately after saying Modeh Ani, wash your hands in a specific ritual way before doing anything else.

What it is:
Netilat Yadayim (נְטִילַת יָדַיִם — literally "lifting of the hands") is a ritual washing — not just hygiene, but a spiritual act. During deep sleep, the soul partially ascends and a spiritual impurity called Ruach Ra'ah (evil spirit) settles on the fingers and nails. This washing removes it and prepares us for prayer, Torah, and the day ahead.

Different minhagim disagree whether morning washing is mainly to remove ruach ra'ah upon waking or mainly as preparation for prayer — so when you say Al Netilat Yadayim differs between Ashkenaz, Sefard, and Chabad.

How to pour (all traditions):
1. Ideally have a cup, bottle, or washcup filled with water and a basin ready at your bedside before you sleep
2. Pour over your right hand, then left, alternating three times: right, left, right, left, right, left
3. Dry your hands when your minhag calls for it (see blessing timing below)

Sefard minhag — when to say Al Netilat Yadayim:
• Following Shulchan Aruch and the Rashba: morning washing primarily removes ruach ra'ah from sleep.
• Say the blessing right after the first morning washing when you can — ideally immediately, even if you will use the bathroom afterward.
• If you have an urgent, pressing need to use the bathroom, go first, then wash and recite the blessing.

The blessing text:
"Baruch Atah Ado-nai Eloheinu Melech ha'olam, asher kid'shanu b'mitzvotav v'tzivanu al netilat yadayim"

The 4 amot rule:
Before washing, do not walk 4 amot (about 6–8 feet) continuously without stopping — this is considered a full "journey" that allows the impurity to expand. If the sink is farther than 4 amot away:
• Strict / Kabbalistic approach (Zohar, Shulchan Aruch HaRav): walk in segments shorter than 4 amot, pause fully between each, and repeat until you reach the sink
• Mainstream approach (Mishnah Berurah): walking directly to the sink to remove the impurity quickly is also permitted, though the stop-and-go method is praiseworthy
• Lenient approach (Aruch HaShulchan): the entire modern house is considered one domain; walk normally to the sink
Follow your rabbi's guidance. In all opinions, washing normally at the sink still fully removes the impurity even if you walked further first.

If you need to say a blessing before you can wash (e.g. urgent bathroom need and no water nearby):
Rub your hands firmly on a clean, dry surface — a wooden headboard, wall, or clothing. This is called Nikuy (ניקוי — cleaning by friction) and permits saying holy words, but does NOT remove the spiritual impurity. You must still wash with water as soon as possible (Shulchan Aruch OC 4:22, Mishnah Berurah 4:61).

After naps (Modeh Ani is not recited):
When waking from a daytime nap, Modeh Ani is typically omitted — wash your hands (negel vasser) per the rules below:
• Nap under ~30 minutes: the severe Ruach Ra'ah does not apply; wash for cleanliness before prayer but no bracha is required and the 4-amot rule does not apply (Mishnah Berurah 4:34)
• Long daytime nap: mainstream opinion — wash three times alternating but no bracha; strict opinion — treat as morning washing. Ask your rabbi

Other important notes:
• Do not touch your eyes, mouth, or face before washing
• Do not touch food before washing
• The same alternating triple-pour without a blessing is done after: using the bathroom, leaving a cemetery, and before eating bread
• Once you have said Al Netilat Yadayim in the morning, all traditions agree you do not repeat the blessing when you wash again at shul before davening

**Edot HaMizrach:**

Immediately after saying Modeh Ani, wash your hands in a specific ritual way before doing anything else.

What it is:
Netilat Yadayim (נְטִילַת יָדַיִם — literally "lifting of the hands") is a ritual washing — not just hygiene, but a spiritual act. During deep sleep, the soul partially ascends and a spiritual impurity called Ruach Ra'ah (evil spirit) settles on the fingers and nails. This washing removes it and prepares us for prayer, Torah, and the day ahead.

Different minhagim disagree whether morning washing is mainly to remove ruach ra'ah upon waking or mainly as preparation for prayer — so when you say Al Netilat Yadayim differs between Ashkenaz, Sephardi, Edot HaMizrach, and Chabad.

How to pour (all traditions):
1. Ideally have a cup, bottle, or washcup filled with water and a basin ready at your bedside before you sleep
2. Pour over your right hand, then left, alternating three times: right, left, right, left, right, left
3. Dry your hands when your minhag calls for it (see blessing timing below)

Edot HaMizrach minhag (many kehillot) — when to say Al Netilat Yadayim:
• Following Shulchan Aruch and the Rashba: morning washing primarily removes ruach ra'ah from sleep.
• Say the blessing right after the first morning washing when you can — ideally immediately, even if you will use the bathroom afterward.
• If you have an urgent, pressing need to use the bathroom, go first, then wash and recite the blessing.

The blessing text:
"Baruch Atah Ado-nai Eloheinu Melech ha'olam, asher kid'shanu b'mitzvotav v'tzivanu al netilat yadayim"

The 4 amot rule:
Before washing, do not walk 4 amot (about 6–8 feet) continuously without stopping — this is considered a full "journey" that allows the impurity to expand. If the sink is farther than 4 amot away:
• Strict / Kabbalistic approach (Zohar, Shulchan Aruch HaRav): walk in segments shorter than 4 amot, pause fully between each, and repeat until you reach the sink
• Mainstream approach (Mishnah Berurah): walking directly to the sink to remove the impurity quickly is also permitted, though the stop-and-go method is praiseworthy
• Lenient approach (Aruch HaShulchan): the entire modern house is considered one domain; walk normally to the sink
Follow your rabbi's guidance. In all opinions, washing normally at the sink still fully removes the impurity even if you walked further first.

If you need to say a blessing before you can wash (e.g. urgent bathroom need and no water nearby):
Rub your hands firmly on a clean, dry surface — a wooden headboard, wall, or clothing. This is called Nikuy (ניקוי — cleaning by friction) and permits saying holy words, but does NOT remove the spiritual impurity. You must still wash with water as soon as possible (Shulchan Aruch OC 4:22, Mishnah Berurah 4:61).

After naps (Modeh Ani is not recited):
When waking from a daytime nap, Modeh Ani is typically omitted — wash your hands (negel vasser) per the rules below:
• Nap under ~30 minutes: the severe Ruach Ra'ah does not apply; wash for cleanliness before prayer but no bracha is required and the 4-amot rule does not apply (Mishnah Berurah 4:34)
• Long daytime nap: mainstream opinion — wash three times alternating but no bracha; strict opinion — treat as morning washing. Ask your rabbi

Other important notes:
• Do not touch your eyes, mouth, or face before washing
• Do not touch food before washing
• The same alternating triple-pour without a blessing is done after: using the bathroom, leaving a cemetery, and before eating bread
• Once you have said Al Netilat Yadayim in the morning, all traditions agree you do not repeat the blessing when you wash again at shul before davening

Baladi Yemenite and some kehillot wash after the bathroom before reciting the blessing — follow your community's psak (Shulchan Aruch O.C. 4 and your rav).

**Chabad:**

Immediately after saying Modeh Ani, wash your hands in a specific ritual way before doing anything else.

What it is:
Netilat Yadayim (נְטִילַת יָדַיִם — literally "lifting of the hands") is a ritual washing — not just hygiene, but a spiritual act. During deep sleep, the soul partially ascends and a spiritual impurity called Ruach Ra'ah (evil spirit) settles on the fingers and nails. This washing removes it and prepares us for prayer, Torah, and the day ahead.

Different minhagim disagree whether morning washing is mainly to remove ruach ra'ah upon waking or mainly as preparation for prayer — so when you say Al Netilat Yadayim differs between Ashkenaz, Sefard, and Chabad.

How to pour (all traditions):
1. Ideally have a cup, bottle, or washcup filled with water and a basin ready at your bedside before you sleep
2. Pour over your right hand, then left, alternating three times: right, left, right, left, right, left
3. Dry your hands when your minhag calls for it (see blessing timing below)

Chabad minhag — when to say Al Netilat Yadayim (Alter Rebbe's Shulchan Aruch):
• First washing: upon waking, before touching things or letting your feet touch the floor without washing — three alternating pours, no blessing.
• Then: restroom, rinse your mouth, and personal grooming.
• Second washing: at the sink — now recite Al Netilat Yadayim, with Asher Yatzar and Elokai Neshama, in a clean state of body and mind.

The blessing text:
"Baruch Atah Ado-nai Eloheinu Melech ha'olam, asher kid'shanu b'mitzvotav v'tzivanu al netilat yadayim"

The 4 amot rule:
Before washing, do not walk 4 amot (about 6–8 feet) continuously without stopping — this is considered a full "journey" that allows the impurity to expand. If the sink is farther than 4 amot away:
• Strict / Kabbalistic approach (Zohar, Shulchan Aruch HaRav): walk in segments shorter than 4 amot, pause fully between each, and repeat until you reach the sink
• Mainstream approach (Mishnah Berurah): walking directly to the sink to remove the impurity quickly is also permitted, though the stop-and-go method is praiseworthy
• Lenient approach (Aruch HaShulchan): the entire modern house is considered one domain; walk normally to the sink
Follow your rabbi's guidance. In all opinions, washing normally at the sink still fully removes the impurity even if you walked further first.

If you need to say a blessing before you can wash (e.g. urgent bathroom need and no water nearby):
Rub your hands firmly on a clean, dry surface — a wooden headboard, wall, or clothing. This is called Nikuy (ניקוי — cleaning by friction) and permits saying holy words, but does NOT remove the spiritual impurity. You must still wash with water as soon as possible (Shulchan Aruch OC 4:22, Mishnah Berurah 4:61).

After naps (Modeh Ani is not recited):
When waking from a daytime nap, Modeh Ani is typically omitted — wash your hands (negel vasser) per the rules below:
• Nap under ~30 minutes: the severe Ruach Ra'ah does not apply; wash for cleanliness before prayer but no bracha is required and the 4-amot rule does not apply (Mishnah Berurah 4:34)
• Long daytime nap: mainstream opinion — wash three times alternating but no bracha; strict opinion — treat as morning washing. Ask your rabbi

Other important notes:
• Do not touch your eyes, mouth, or face before washing
• Do not touch food before washing
• The same alternating triple-pour without a blessing is done after: using the bathroom, leaving a cemetery, and before eating bread
• Once you have said Al Netilat Yadayim in the morning, all traditions agree you do not repeat the blessing when you wash again at shul before davening


# Part 5 — App UI Copy (Disclaimer, About, Rest Screens)

### Welcome Headline

A companion for your day

### Welcome Intro (standalone app)

Be a Tzaddik helps you track standard daily mitzvot in a Torah-observant routine.

### Welcome Intro (embedded in MitzMode)

The Daily Mitzvot Checklist helps you track standard daily mitzvot in a Torah-observant routine.

### Disclaimer / About Body

This app is a learning companion, not a rabbi — it does not give halachic rulings.

This checklist does not contain all the mitzvot in the entire Torah. It covers standard daily mitzvot that observant Jews commonly practice — waking, prayer, blessings, meals, Torah study, Shabbat preparation, and similar foundations.

With your permission, the app uses GPS or a city you choose to calculate Jewish calendar times and when you can fulfill different mitzvot throughout the day (for example morning, afternoon, and evening prayer windows, candle lighting, and Shabbat-related times). Location is kept on your device only for zmanim and the calendar.

If you are new to Judaism, take it slow and do what you can. Build steady habits without overwhelm, and always ask an Orthodox rabbi you trust when something is unclear or when your situation needs personal guidance.

**Brought to you by** Beardy Top Productions

Website: www.beardy.top

### Rest Screen Title (Shabbat active)

It's Shabbat Now

### Rest Screen Title (Shabbat approaching)

Shabbat is about to begin

### Rest Screen Title (Yom Tov active)

[Holiday name] (Yom Tov)

### Rest Screen Title (Yom Tov approaching)

[Holiday name] is about to begin

### Rest Screen Messages

**Shabbat (active):**

Please put away your device and keep Shabbat. Rest, pray, learn Torah, and enjoy time with family and community. Melacha (forbidden labor) applies on Shabbat, including most phone and device use.

**Shabbat (approaching):**

Shabbat is about to begin. Please finish what you are doing, turn off your phone, and prepare to welcome the holy day.

Our Sages teach that Shabbat is a taste of the World to Come, and one who guards Shabbat according to its laws receives forgiveness and a reward beyond measure (Shabbat 25b; Shabbat 118a).

**Yom Tov (active):**

Today is [holiday name] (Yom Tov — a festival day). Please put away your device and keep the day holy. Melacha (forbidden labor) applies on Yom Tov similar to Shabbat.

**Yom Tov (approaching):**

[holiday name] is about to begin. Please finish what you are doing, turn off your phone, and prepare to welcome this Yom Tov with joy.

Our Sages teach that rejoicing on Yom Tov is itself a mitzvah — may your observance bring blessing and divine reward to you and your household.


## Omer Count Text Template (OmerCountText.kt)

*(Could not extract OmerCountText.buildExplanation.)*


---

*End of export. Re-run `python tools/export_app_reference_text.py` to regenerate from source.*
