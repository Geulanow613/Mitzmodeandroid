# Seasonal & hidden checklist text (full explainers)

Text that does **not** appear on a normal weekday checklist. Sources:

| Source | What |
|--------|------|
| `data/checklist-items.json` | `shabbatEveOnly` items, Musaf, Kiddush Levana, etc. |
| `data/nusach-extras.json` | Nusach-specific prayer/study rows (no Shabbat-use items) |
| `SeasonalChecklistItems.kt` | Calendar-triggered items (Omer, Purim, Pesach, …) |
| `HolyDayPhoneRules.kt` | Full-screen message when checklist is hidden (Shabbat / 2nd-day Yom Tov) |

**Not included here:** Shabbat Guide encyclopedia (`ShabbatGuideData.kt`) — separate reference screen, not checklist rows.

---

## Screen messages (no checklist row)

### Shabbat — checklist hidden all day Saturday

- **Title:** Shabbat Shalom  
- **Message:** Today is Shabbat. Please put away your phone and keep the day holy — pray, learn Torah, enjoy Shabbat meals, and rest. This app is for weekdays and erev Shabbat preparation, not for use during Shabbat.  
- **Footer:** Close this app and enjoy a peaceful, screen-free Shabbat.

### Yom Tov — checklist hidden (e.g. 2nd day in Diaspora)

- **Title:** [Holiday name]  
- **Message:** Today is [Holiday name]. Please put away your device and keep the day holy. Using electronics on Yom Tov is forbidden by halacha (melacha). This app is for weekdays and erev chag preparation, not for use on Yom Tov.  
- **Footer:** Close this app and keep the festival day holy.

---

## Base checklist — Erev Shabbat only (`shabbatEveOnly: true`)

### Prepare for Shabbat (Erev Shabbat checklist)

**ID:** `prepare_for_and_observe_shabbat_and_festivals`  
**When:** Friday (erev Shabbat), before sunset — not shown on ordinary weekdays.

Shabbat is the holiest day of the week — the Jewish Sabbath. It begins at sunset on Friday and ends when three stars appear on Saturday night (approximately 25 hours).

Key terms:
• Shabbat (שַׁבָּת) — the Sabbath; G-d rested on the seventh day of creation and sanctified it (Genesis 2:3); observing Shabbat is one of the Ten Commandments
• Erev Shabbat — the eve of Shabbat; Friday afternoon/evening before candle lighting
• Melacha (מְלָאכָה) — the 39 categories of creative work prohibited on Shabbat, derived from the work done to build the Tabernacle
• Oneg Shabbat — the mitzvah to enjoy and delight in Shabbat (oneg = pleasure)

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

---

### Hadlakat Nerot (Shabbat candle lighting)

**ID:** `shabbat_candles`  
**When:** Friday before sunset (erev Shabbat).

Lighting candles on Friday afternoon is one of the most beloved practices in all of Judaism — it officially welcomes Shabbat into the home.

What it is:
Hadlakat Nerot (הַדְלָקַת נֵרוֹת — lighting of candles) is a rabbinic mitzvah performed weekly before Shabbat. The Shabbat candles bring light and peace into the home, honoring the day's holiness.

Who lights:
Traditionally the woman of the household lights. If there is no woman in the home, a man lights. Each woman or girl in the household may light her own candles.

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

Key terms:
• Nerot — candles
• Zachor / Shamor — the two expressions of the Shabbat command in the two versions of the Ten Commandments
• Kavvanah — intention; candle lighting is an especially auspicious time for personal prayer

Important:
Once you have lit the candles and said the blessing, Shabbat has begun for you. Do not light additional fire or use electricity after this point.

---

## Base checklist — Shabbat / festival days (always in catalog)

### Musaf (Additional prayer on special days)

**ID:** `musaf_only_on_rosh_chodesh_festivals_and_shabbat`  
**When:** Shabbat, Rosh Chodesh, and Yom Tov morning services (not on ordinary weekdays).

Musaf is an additional Amidah prayer added to the morning service on Shabbat, Rosh Chodesh, and all Jewish holidays.

What Musaf is:
Musaf (מוּסָף — literally "additional") is an extra Amidah — the same standing prayer as the rest of the service — but with a special middle blessing describing the Temple offerings (korbanot) that were brought on that day in Temple times, and a prayer for the restoration of the Temple service.

Key terms:
• Musaf — additional / extra
• Korbanot — Temple offerings; sacrifices brought by priests in the Holy Temple in Jerusalem
• Beit HaMikdash — the Holy Temple in Jerusalem (literally "the House of Holiness")
• Rosh Chodesh — the first day of the Hebrew calendar month; a minor holiday
• Yom Tov — a Jewish holiday (literally "good day"): Rosh Hashanah, Yom Kippur, Passover (Pesach), Shavuot, Sukkot, Shemini Atzeret/Simchat Torah

When Musaf is said:
• Every Shabbat
• Rosh Chodesh (new month)
• All Yom Tov holidays
• Not said on ordinary weekdays

---

### Tefillin — hidden ON Shabbat/Yom Tov

**ID:** `put_on_tefillin_during_morning_prayers_except_shabbat_festiv`  
**Flag:** `hideOnShabbat: true` — item is **hidden** on Shabbat and festivals (not a separate seasonal explainer).

---

## Calendar-seasonal items (`SeasonalChecklistItems.kt`)

Each block below is added only when the Hebrew calendar / `activeSeasons` matches.

---

### Sefirat HaOmer — Count the Omer (days 1–49, not Lag BaOmer)

**When:** Each night after tzeit; dynamic copy includes local nightfall time when zmanim are available.

**Title:** e.g. `Count the Omer — day 43 of 49 (6 weeks and 1 day)`.

**Explanation:** Built at runtime in `OmerCountText.kt` — day + weeks/days summary, tonight's count, tomorrow night's count, and `after nightfall at [time]`. Header chip shows `Omer day 43 — 6 weeks and 1 day`.

---

### Sefirah: mourning customs (music, weddings, haircuts)

**When:** During Sefirah (except Lag BaOmer per calendar).

**Explanation:** Mourning for Rabbi Akiva's students (Yevamot 62b); music, weddings, haircuts — timing varies by community. See `SeasonalChecklistItems.kt` for full Ashkenaz / Sefard / Chabad text.

---

### Chanukah — Light candles (each night)

**When:** Each night of Chanukah after tzais.

**Explanation:** Light after tzais. [Chabad: shamash then rightmost new candle / Sefard: left to right newest first / Ashkenaz: right to left] Recite brachot and HaNeiros halalu.

---

### Erev Chanukah prep

**When:** Day before Chanukah starts.

**Explanation:** Chanukah starts tomorrow night. Prepare the menorah, enough candles/oil, and [review lighting time and brachot](https://ohr.edu/1304) before tomorrow night.

**Link:** [Ohr Somayach — The Laws of Chanukah](https://ohr.edu/1304)

---

### Purim — Hear the Megillah (`purim_megillah`)

**When:** Purim — night and day.

**Explanation:** Full laws in `SeasonalChecklistItems.kt` (twice, standing, brachot, listening, machatzit haShekel).

---

### Purim — Matanot la'evyonim (`purim_matanot_laevyonim`)

**Explanation:** Two gifts to two poor people on Purim day; how, when, and via organization — see source.

---

### Purim — Mishloach manot (`purim_mishloach_manot`)

**Explanation:** Two ready-to-eat foods to a friend before sunset — see source.

---

### Erev Purim prep

**Explanation:** Purim is tomorrow. Confirm Megillah reading times and prepare matanot la'evyonim and mishloach manot.

---

### Purim Meshulash (Jerusalem) — Friday

**Explanation:** In Jerusalem Purim Meshulash years, Megillah reading and gifts to the poor are done on Friday (14 Adar).

---

### Purim Meshulash (Jerusalem) — Sunday

**Explanation:** In Jerusalem Purim Meshulash years, the festive meal and mishloach manot are observed on Sunday (16 Adar).

---

### Erev Chag prep (holiday-specific)

**When:** Day before a Torah-level Yom Tov (melacha prohibited).

**Title & explanation:** Built at runtime in `ErevChagPrepText.kt` from `DayInfo.upcomingChagName` / `upcomingChagYomTovIndex` — covers Rosh Hashana, Yom Kippur, Pesach (+ seder), Shavuot, Sukkot, Shemini Atzeret, Simchat Torah, with sunset time, eruv tavshilin on Friday, Israel vs Diaspora notes, and Chabad/Aish/Peninei/Ohr Somayach links.

---

### Rosh Chodesh — Yaaleh V'yavo & Hallel

**When:** Rosh Chodesh.

**Explanation:** On Rosh Chodesh, Hallel and Musaf are added (per your minhag).

---

### Week before Pesach prep

**When:** Nissan 8–13.

**Explanation:** In the week before Pesach, focus cleaning on places where chametz is actually brought/eaten, and prepare Pesach kitchen/table logistics in advance.

---

### Erev Pesach items (`ErevPesachPrepText.kt`)

| ID | Title | Notes |
|----|-------|-------|
| `erev_pesach_mechirat_chametz` | Sell chametz | Why, how, storage, buy-back |
| `erev_pesach_taanit_bechorot` | Fast of the Firstborn | Siyum exemption, who fasts |
| `erev_pesach_prepare_seder` | Seder prep | Plate, 4 cups, Israel vs Diaspora |
| `bedikat_chametz` | Search tonight | Candle, rooms, bracha, bitul, local tzeit |
| `erev_pesach_biur_chametz` | Burn next morning | 4th/5th hour zmanim when available |

Full text in `ErevPesachPrepText.kt`; links: Chabad, Peninei Halacha, Aish, Ohr Somayach.

---

### Chol HaMoed, Sukkot, Shemini Atzeret, Simchat Torah, etc.

Full how-to text for Chol HaMoed (honor, wine, clothes, matzah), **sukkah building**, **arba minim**, **Shemini Atzeret**, **Simchat Torah** (Diaspora), Rosh Chodesh, Chanukah lighting, Pesach week prep, Selichot, Three Weeks / Nine Days, and Tu B'Shvat — see `SeasonalMitzvahText.kt` with Chabad / Peninei / Aish / Ohr Somayach links per item.

---

### Shemini Atzeret focus

**When:** Shemini Atzeret.

**Explanation:** Shemini Atzeret is a distinct Yom Tov after Sukkot. Focus on tefillah and simcha. Ashkenaz: Tefillat Geshem in Musaf and Yizkor are common. Sefard: Geshem customs vary; Yizkor is often not said on this day. In Chutz LaAretz, Simchat Torah is the following day; in Israel, both are on 22 Tishrei.

---

### Motzei Yom Kippur — Begin building sukkah (men)

**When:** 11 Tishrei night (situational).

**Explanation:** If you are able and have space, it is a mitzvah to begin sukkah building right after Yom Kippur. Basics: valid walls, kosher schach from detached plant material, and enough shade for dwelling.

---

### Three Weeks mourning customs

**When:** 17 Tammuz through 9 Av.

**Explanation:** From 17 Tammuz through Tisha B'Av, keep Three Weeks aveilut practices for your nusach and community.

**Ashkenaz / Sefard / Chabad:** (see `SeasonalChecklistItems.kt` for full nusach-specific paragraphs)

---

### Nine Days mourning customs

**When:** 1–9 Av.

**Explanation:** Keep Nine Days mourning practice as observed in your nusach and community. (Full Ashkenaz/Sefard/Chabad variants in source file.)

---

### Selichot (Elul)

**When:** Elul; Ashkenaz from Motzei Shabbat before Rosh Hashanah (≥4 days).

**Sefard:** Sefard custom begins Selichot from Elul. Say with a minyan when possible according to your local schedule.

**Chabad:** Chabad custom begins Selichot in Elul. Follow your local Chabad minyan timing and nusach.

**Ashkenaz:** Ashkenaz custom begins Selichot Motzei Shabbat before Rosh Hashana (with at least four days of Selichot). Follow local shul schedule.

---

### Tu B'Shvat Seder (optional)

**When:** 15 Shevat.

**Explanation:** Some communities have a custom to hold a Tu B'Shvat Seder: eating varieties of fruits (especially from Eretz Yisrael), saying brachot mindfully, and reflecting on growth and gratitude.

---

### Yom HaShoah (27 Nisan)

**When:** Israeli civil observance (date may shift near Shabbat).

**Explanation:**

Yom HaShoah V'HaGevurah (27 Nisan) is the national day of remembrance for the six million Jews murdered in the Holocaust. It was established by the Israeli Knesset in 1953.

This is not a Yom Tov — melacha is fully permitted. It is a national civil observance, not a halachically mandated fast or prayer day.

Date adjustment: If 27 Nisan falls on Friday, the day is observed on Thursday (26 Nisan). If it falls on Sunday, it is observed on Monday (28 Nisan), to avoid disruption of Shabbat.

Customs by community:

In Israel: Two-minute siren sounds at 10:00 AM; most Israelis stop and stand in silence. Memorial ceremonies are held at Yad Vashem and throughout the country.

Religious Zionist / Modern Orthodox: Many observe a moment of silence and omit Tachanun. Some communities hold special memorial prayers or learning.

Charedi communities: Many do not observe this date as a religious memorial, preferring 10 Tevet (designated by the Chief Rabbinate in 1949 as Yom Kaddish HaKlali for those whose date of death is unknown) or Tisha B'Av as the appropriate day of mourning for all Jewish tragedies. This is a matter of minhag and communal leadership.

Chabad: No official communal observance is instituted, though the memory of the kedoshim (holy martyrs) is honored.

---

### Yom HaZikaron (4 Iyar)

**When:** Day before Yom Ha'atzmaut.

**Explanation:**

Yom HaZikaron (4 Iyar) is Israel's national day of remembrance for soldiers of the Israel Defense Forces and victims of terrorism who gave their lives for the State of Israel. It was established by the Knesset in 1963 and always falls the day before Yom Ha'atzmaut.

This is not a Yom Tov — melacha is fully permitted. It is a national civil observance.

Date adjustment: KosherJava adjusts the date when 4 Iyar falls near Shabbat, to keep Yom HaZikaron and Yom Ha'atzmaut together on consecutive days and not adjacent to Shabbat.

In Israel: Memorial sirens sound at 8:00 PM (start of the day, at nightfall) and again at 11:00 AM the following morning. Ceremonies are held at military cemeteries across the country. Flags fly at half-mast.

Religious Zionist / Dati Leumi communities: Observe solemnly; Tachanun is sometimes omitted (varies by community and rav). Memorial prayers may be added.

Most Charedi and Chabad communities: Do not observe as a religious day; regular weekday davening with Tachanun.

The day ends at nightfall with the transition into Yom Ha'atzmaut celebrations.

---

### Yom Ha'atzmaut (5 Iyar)

**When:** After Yom HaZikaron.

**Explanation:**

Yom Ha'atzmaut (5 Iyar) commemorates Israeli independence in 1948. It is not a Biblical or Rabbinic Yom Tov — melacha (work) is fully permitted.

Customs vary significantly by community:

Religious Zionist / Modern Orthodox: Hallel is recited (instituted by the Chief Rabbinate of Israel). Whether Hallel is said with a bracha is disputed — the Chief Rabbinate instructed with a bracha; many Ashkenazi poskim outside Israel say without a bracha. Tachanun is omitted. Some communities add special festive prayers (Hallel u'Maariv).

Sephardic (Rav Ovadia Yosef / Yalkut Yosef): Rav Ovadia Yosef ruled that Hallel should not be recited (concern of bracha levatala since the day was not established by Chazal). Tachanun omission is also disputed in these communities.

Chabad: The Rebbe did not institute any special observance. Most Chabad communities do not say Hallel and recite Tachanun as usual.

Charedi communities (Agudah, Litvish): Generally do not observe the day as a religious holiday. Tachanun is said as usual.

The Omer continues to be counted normally. There is no Al HaNissim addition to davening.

Ask your rav which custom your community follows.

---

### Yom Yerushalayim (28 Iyar)

**When:** 28 Iyar.

**Explanation:**

Yom Yerushalayim (28 Iyar) marks the reunification of Jerusalem during the Six-Day War in 1967 (5727). It is not a Yom Tov — melacha is fully permitted.

Customs vary by community:

Religious Zionist / Dati Leumi: Hallel is recited (with or without a bracha, depending on the posek and community). Tachanun is omitted. Some communities recite special tefillot.

Sephardic (Rav Ovadia Yosef): Hallel is not recited for the same reason as Yom Ha'atzmaut — not established by Chazal. Practice varies.

Chabad and Charedi communities: Generally no special observance. Tachanun is said as usual.

Yom Yerushalayim is observed by fewer communities than Yom Ha'atzmaut, and there is no universally accepted halachic obligation. Ask your rav about your community's custom.

---

## Upcoming holidays banner (Today screen)

From `holidays-overlay.json` + calendar engine — short **prep hints** (not full explainers), e.g. linking to `shabbat_candles`, `shabbat_prep` item titles. Full explainer text is in the checklist items above when those days arrive.

---

*Generated for editorial review. Edit source files: `data/checklist-items.json`, `data/nusach-extras.json`, `SeasonalChecklistItems.kt`.*
