#!/usr/bin/env python3
"""Generate _ru_batch18_manual.json — Latin-gap long explainers + glossary."""
from __future__ import annotations

import json
from pathlib import Path

KEYS_PATH = Path(__file__).parent / "_batch18_keys.json"
OUT = Path(__file__).parent / "_ru_batch18_manual.json"

# Keys must match _batch18_keys.json exactly.
MANUAL: dict[str, str] = {
    "Learn the Rambam's surprisingly specific health guidelines! 🍽️ Written over 900 years ago, many of the Rambam's (Maimonides) health guidelines in Hilchot De'ot 4 are remarkably validated by modern medicine. He teaches: 'Maintaining a healthy body is part of serving Hashem, because it is impossible to understand or know Hashem if one is ill.' Here's his practical wisdom: Don't eat until you're stuffed! Stop eating when about 75% full. Eat slowly and in a seated position. Avoid drinking liquids excessively during meals. Don't sleep immediately after eating; wait a few hours. Prioritize sleep during the night; daytime napping is generally discouraged. Exercise is essential – the Rambam asserts that lack of physical movement is a primary cause of illness. Avoid foods that are clearly harmful.": (
        "Изучите удивительно конкретные рекомендации Рамбама по здоровью! 🍽️ Написанные более 900 лет назад, многие советы в Илхот деот 4 подтверждены современной медициной. "
        "Он учит: «Поддержание здорового тела — часть служения В-гу, ибо невозможно познать Его, если человек болен». "
        "Практика: не ешьте до отвала; останавливайтесь на ~75% сытости; ешьте медленно, сидя; не пейте много во время еды; "
        "не ложитесь сразу после еды; спите ночью; движение необходимо — без него болезни; избегайте явно вредной пищи."
    ),
    "candles, often referring especially to Shabbat and Yom Tov lights": (
        "свечи, часто особенно шабат- и йом-тов-свет"
    ),
    "Ta'anit — a public or personal fast day": "таанит — публичный или личный день поста",
    "Learn about Losh — kneading and forming a mass from separate ingredients! 🍞 Think dough: flour + water + mixing = one unified blob. In the Mishkan, Losh was a key agricultural step used when mixing water with the ground herb powders to make dyes, as well as kneading the flour to bake the Showbread. On Shabbat, this melacha prohibits combining a finely divided powder or small solid particles with a liquid to form a single, cohesive paste or thick mass. This restriction directly affects how people prepare everyday items like baby cereal, instant oatmeal, mustard, or even certain dense textures of egg salad. To avoid a Torah-level violation, the halacha requires using specific workarounds when a paste must be made, such as changing the normal order of ingredients (for instance, pouring the liquid into the bowl before adding the powder) and mixing the ingredients with an unusual motion (Shinui), like employing a crisscross slicing movement with a knife instead of a standard circular stir. This is why Shabbat challah is baked before the day of rest begins, since the creative act of unifying separate elements into a brand new entity belongs entirely to Friday, leaving us to enjoy the completed creation on Shabbat. Learn more!": (
        "Лош — замешивание и образование единой массы из отдельных ингредиентов! 🍞 В Мишкане — смешение красок и замес хлеба для леhem hа-паним. "
        "В Шаббат запрещено соединять порошок с жидкостью в однородную пасту. Для каши, горчицы и т.п. — меняйте порядок (сначала жидкость) и способ (шинуи). "
        "Хала пекут в пятницу — творческое объединение элементов остаётся для будней."
    ),
    "Plag HaMincha": "плаг hа-минха",
    "Kiddush b'Makom Seudah — Kiddush b'Makom Seudah means sanctifying Shabbat or Yom Tov, typically with blessings over wine, in the same place you eat a meal afterwards. You would not fulfill Kiddush if you recited it and left without eating there. On Shabbat night, the meal should include bread (hamotzi) per most poskim. On Yom Tov or daytime Kiddush, other foods may qualify in some cases; ask your rav. If you make Kiddush in one room, the meal should follow there and not in a different space.": (
        "кидуш бе-маком сеуда — кидуш в том же месте, где едите трапезу. Ушли без еды — кидуш не засчитан. "
        "В пятницу вечером — хлеб (hа-моци) по большинству позеков. В другой комнате — не засчитывается."
    ),
    "Kiddush b'Makom Seudah means sanctifying Shabbat or Yom Tov, typically with blessings over wine, in the same place you eat a meal afterwards. You would not fulfill Kiddush if you recited it and left without eating there. On Shabbat night, the meal should include bread (hamotzi) per most poskim. On Yom Tov or daytime Kiddush, other foods may qualify in some cases; ask your rav. If you make Kiddush in one room, the meal should follow there and not in a different space.": (
        "Кидуш бе-маком сеуда — освящение Шаббата или Йом Тов с вином в том же месте, где едите. "
        "Без трапезы там кидуш не выполнен; в пятницу вечером — хлеб по большинству позеков."
    ),
    "Kinot — elegies read on Tisha B'Av mourning the Temple": "кинот — элегии в Тиша бе-Ав о Храме",
    "halachic hour — A halachic hour (sha'ah zmanit) is one twelfth of the daylight period from dawn to dusk — so it is longer in summer and shorter in winter, unlike a 60-minute clock hour. Deadlines such as latest morning Shema, chametz sale times, and plag hamincha use these hours. Apps and Jewish calendars convert them for your location.": (
        "галахический час (шаа зманит) — одна двенадцатая светового дня от рассвета до заката; летом длиннее, зимой короче. "
        "Им измеряют крайний срок Шма, продажу хамеца, плаг hа-минха."
    ),
    "yichud — Yichud is the prohibition against the seclusion of a man and a woman together in a private area when they are not married to each other and are not immediate blood relatives (parents, children, siblings, etc.). It prevents situations that could lead to improper intimacy. Exceptions and practical details (open doors, shared workplaces, healthcare) vary — ask your rav for real-life guidance.": (
        "йихуд — запрет наедине мужчины и женщины, не состоящих в браке и не близких родственников, в уединённом месте. "
        "Исключения (открытая дверь, работа, медицина) — спросите рава."
    ),
    "shaliach tzibur — A shaliach tzibur is the prayer leader who represents the congregation before G-d — reciting the repetition of the Amidah and guiding pacing. He must be someone the community accepts and who knows the laws of prayer. Women and men have different roles per community in who may lead which parts.": (
        "шлиах цибур — ведущий молитвы, представляющий общину; повторяет Амиду. Должен быть принят общиной и знать галахот тфилы."
    ),
    "teruah — quick staccato shofar blasts": "теруа — короткие отрывистые звуки шофара",
    "Purim joy includes everyone! 🎭💝 Learn about matanot la'evyonim (gifts to the poor): halacha teaches that Purim simcha is incomplete if people in need are left out. It's a requirement of the day to give at least a small sum (preferably enough to buy a meal) to 2 different poor people on Purim. You can also give the money to someone else to distribute it for you, but they need to distribute it on the same day that you celebrate Purim (the 14th or 15th of Adar). Learn one law about this beautiful mitzvah!": (
        "Радость Пурима — для всех! 🎭💝 Матанот ла-эвьоним: симха неполна, если бедные остались без подарка. "
        "Минимум — два бедных, сумма на скромную трапезу, в день вашего Пурима (14 или 15 Адар)."
    ),
    "pareve — neutral foods (neither meat nor dairy) such as fish, eggs, and produce": (
        "парве — нейтральные продукты (не мясо и не молочное): рыба, яйца, овощи"
    ),
    "nusach Ashkenaz — Ashkenazi prayer wording": "нусах ашкеназ — ашкеназская формулировка молитв",
    "Nusach Ari is the prayer rite associated with Rabbi Isaac Luria (the Ari) and used by Chabad and some other communities. It blends Ashkenazi and Sephardi elements. Chabad siddurim such as Tehillat Hashem print this nusach. If you daven Nusach Ari, use that siddur consistently for festival inserts.": (
        "Нусах Ари — обряд рава Ицхака Лурии; у Хабада и других общин. Смешивает ашкеназ и сефард. "
        "Используйте сиддур Тehилат hашем последовательно для праздничных вставок."
    ),
    "Share in another parent's hopes! Think of someone who needs nachat (joy and pride from their children) and say a heartfelt prayer for them 👨‍👩‍👧‍👦. Whether it's for health, success, or happiness - your caring thoughts can make a real difference for both the parents and their children! ❤️": (
        "Разделите надежды другого родителя! Помолитесь за того, кому нужен нахат от детей 👨‍👩‍👧‍👦 — здоровье, успех, радость. ❤️"
    ),
    "challah — Challah is the bread often prepared for Shabbat and festival meals — often braided and covered until Kiddush. Two loaves (lechem mishneh) recall the double portion in the desert. On Friday night, if wine or grape juice is completely unavailable, Kiddush can be recited over the challah loaves, provided one washes hands for bread immediately before reciting Kiddush and replaces the wine blessing with Hamotzi. Bread can never substitute for daytime Shabbat Kiddush on Saturday morning.": (
        "хала — хлеб для Шаббата и праздников; две лепёшки (лехем мишне). Если нет вина в пятницу вечером — кидуш на хале после нетилат ядаим и hа-моци; днём в субботу хлеб не заменяет кидуш."
    ),
    "kippah — skullcap worn as a sign of G-d's presence above": "киппа — головной убор как знак присутствия В-га свыше",
    "niddah — Niddah is the state of ritual separation during and after menstruation until mikveh immersion. Husband and wife avoid physical intimacy and certain affectionate contact. Laws of stains and cycles are complex — a kallah teacher or rav supports real-life questions. Niddah is not a punishment; it is rhythm and holiness in marriage.": (
        "нида — состояние ритуального разделения до погружения в микве; без физической близости. Сложные законы пятен — калла-учитель или рав. Не наказание, а ритм святости в браке."
    ),
    "Niddah is the state of ritual separation during and after menstruation until mikveh immersion. Husband and wife avoid physical intimacy and certain affectionate contact. Laws of stains and cycles are complex — a kallah teacher or rav supports real-life questions. Niddah is not a punishment; it is rhythm and holiness in marriage.": (
        "Нида — ритуальное разделение до микве; без физической близости. Законы пятен сложны — калла-учитель или рав. Ритм святости в браке, не наказание."
    ),
    "Kol Chamira": "коль hамира",
    "biur chametz": "биур хамец",
    "neshama — Neshama is the soul — the divine breath that makes you alive. Modeh Ani thanks G-d for returning it after sleep. Yizkor and Kaddish relate to the soul's journey after death. Jewish thought distinguishes levels of soul (nefesh, ruach, neshama) in mysticism; practically, nurturing the soul means Torah, mitzvot, and character.": (
        "нешама — душа, божественное дыхание жизни. Моде ани — после сна; йизкор и кадиш — путь души. Практически — Тора, мицвот, характер."
    ),
    "Yizkor — Yizkor is the memorial prayer on Yom Tov (and Yom Kippur) for parents and sometimes other relatives. Many light a yahrzeit candle before the day. Pledging charity in their memory is a beautiful custom. Those with both parents living often leave the shul briefly per minhag. The prayer affirms that merit and memory continue beyond death.": (
        "йизкор — поминальная молитва в Йом Тов и Йом Кипур; свеча ярцайт; цдака в память. При живых родителях многие выходят из шула по минхагу."
    ),
    "Yizkor is the memorial prayer on Yom Tov (and Yom Kippur) for parents and sometimes other relatives. Many light a yahrzeit candle before the day. Pledging charity in their memory is a beautiful custom. Those with both parents living often leave the shul briefly per minhag. The prayer affirms that merit and memory continue beyond death.": (
        "Йизкор — поминальная молитва в Йом Тов и Йом Кипур; свеча ярцайт; цдака в память. При живых родителях многие выходят из шула."
    ),
    "Neshama is the soul — the divine breath that makes you alive. Modeh Ani thanks G-d for returning it after sleep. Yizkor and Kaddish relate to the soul's journey after death. Jewish thought distinguishes levels of soul (nefesh, ruach, neshama) in mysticism; practically, nurturing the soul means Torah, mitzvot, and character.": (
        "Нешама — душа, божественное дыхание. Моде ани после сна; йизкор и кадиш — путь души. Практически — Тора, мицвот, характер."
    ),
    "yetzer hara — Yetzer hara is the inclination toward selfishness, laziness, or sin — not a devil, but internal pull. Everyone has it; the battle is lifelong. Torah, mitzvot, and good friends strengthen yetzer hatov. The goal is not to destroy desire but to channel it — appetite for holy things, ambition for good deeds.": (
        "йецер hара — тяга к эгоизму и греху, не дьявол, а внутренний голос. Тора, мицвот и друзья укрепляют йецер hатов; цель — направить желание, а не уничтожить."
    ),
    "Tochen — grinding melacha": "тохен — мелаха помола",
    "maaser kesafim — Maaser kesafim is setting aside about ten percent of net income for tzedakah after expenses. It trains that income is entrusted, not owned absolutely. Priorities: local poor, Torah learning, Israel. If you cannot give ten percent without hardship, give what you can and ask your rav.": (
        "маасер кесафим — откладывать ~10% дохода на цдаку. Приоритеты: местные бедные, Тора, Израиль. При трудностях — сколько можете; спросите рава."
    ),
    "Maaser kesafim is setting aside about ten percent of net income for tzedakah after expenses. It trains that income is entrusted, not owned absolutely. Priorities: local poor, Torah learning, Israel. If you cannot give ten percent without hardship, give what you can and ask your rav.": (
        "Маасер кесафим — ~10% чистого дохода на цдаку. Приоритеты: бедные, Тора, Израиль. При трудностях — по возможности; спросите рава."
    ),
    "charoset — Charoset is the sweet paste (apples, wine, nuts, etc.) recalling mortar between bricks. It is dipped with maror for Korech. Recipes vary by family — Ashkenazi, Sephardi, and Persian charoset all fulfill the mitzvah when made with intent. It is one of the Seder plate's sensory contrasts: bitter and sweet together.": (
        "харосет — сладкая паста (яблоки, вино, орехи), напоминающая раствор кирпичей; с марором для корех."
    ),
    "Taanit Esther — fast day before Purim": "таанит Эстер — пост перед Пуримом",
    "Practice Emunah (faith)! Take a deep breath and remember that everything in your life is orchestrated by G-d with perfect precision 🎯. Each challenge is custom-designed to help you grow, and every joy is a personal gift from Above. When we strengthen our trust in G-d's plan, we find inner peace and purpose.": (
        "Практикуйте эмуну! 🎯 Всё в жизни устроено В-гом с точностью; каждое испытание — для роста, каждая радость — дар свыше."
    ),
    "The Days of Awe (Aseret Yemei Teshuvah) span Rosh Hashana through Yom Kippur — a time of judgment, prayer, and teshuvah. Customs intensify: Selichot, charity, and asking forgiveness. Even Jews who are less observant year-round often attend services. The tone is solemn but hopeful — repentance can change a harsh decree.": (
        "Десять дней покаяния — от Рош hа-Шана до Йом Кипур: суд, молитва, тешува. Селихот, цдака, прощение. Тон торжественный, но с надеждой — покаяние может изменить приговор."
    ),
    "Bentching — Bentching is Birkat Hamazon — Grace After Meals after eating a kezayit of bread within a meal. It thanks G-d for food and the Land. On Shabbat and festivals, Psalm 126 (Shir HaMaalot) precedes the second blessing. Yaaleh V'yavo is added on Rosh Chodesh and chag. Zimun invites others when three or more men ate bread together per minhag.": (
        "бентчинг — биркат а-мазон после кезаита хлеба; в Шаббат и праздники — псалом 126; в Рош Ходеш и хаг — яале вьяво; зимун при трёх и более."
    ),
    "A siddur (from seder, \"order\") is the Jewish prayer book with the fixed texts for daily and Shabbat services — blessings, Shema, Amidah, Birkat Hamazon, and more. Editions follow nusach (Ashkenaz, Sefard, Chabad, etc.), so words and order differ slightly. Your siddur is your map for davening.": (
        "сиддур (от «седер» — порядок) — молитвенник с текстами будней и Шаббата; издания по нусаху (ашкеназ, сефард, хабад)."
    ),
    "Hafrashat challah is separating a small portion of dough when baking a large amount of bread or similar flour-based foods, then burning or discarding in many customs. It recalls the Temple-era gift to the kohen (when they were in a state of ritual purity and able to eat it).": (
        "hафрашат хала — отделение куска теста при выпечке большого количества хлеба; сжигают или выбрасывают; напоминание о дарах коhенам в Храме."
    ),
}


def main() -> None:
    keys: list[str] = json.loads(KEYS_PATH.read_text(encoding="utf-8"))
    # Long keys loaded from strings.json for entries not inlined above.
    strings = json.loads((Path(__file__).resolve().parents[1] / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
    long_fixes = _load_long_fixes(strings)
    manual = dict(MANUAL)
    manual.update(long_fixes)
    missing = [k for k in keys if k not in manual]
    extra = [k for k in manual if k not in keys]
    if missing:
        raise SystemExit(f"missing ({len(missing)}): {missing[0][:80]}...")
    if extra:
        raise SystemExit(f"extra ({len(extra)}): {extra[0][:80]}...")
    ordered = {k: manual[k] for k in keys}
    OUT.write_text(json.dumps(ordered, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {len(ordered)} entries to {OUT.name}")


def _load_long_fixes(strings: list[str]) -> dict[str, str]:
    """Long explainer bodies — keyed by exact catalog strings."""
    by_key = {s: s for s in strings}
    fixes: dict[str, str] = {}

    k = by_key.get
    # Shnayim Mikra — use prefix match
    for s in strings:
        if s.startswith("Why Shnayim Mikra is worth the effort"):
            fixes[s] = (
                "Почему шнайим микра стоит усилий — учения мудрецов:\n\n"
                "Еженедельная парша — ваша личная доля в Торе. Мудрецы связывают публичное чтение с устойчивостью мира (Шаббат 119б). "
                "Изучая ту же паршу, что услышите в Шаббат, вы в цепи от Синая; Шехина — там, где звучат слова Торы (Авот 3:2).\n\n"
                "Шнайим микра в'эхад таргум (שְׁנַיִם מִקְרָא וְאֶחָד תַּרְגּוּם) — каждый стих парши:\n"
                "• дважды на иврите\n• раз на Таргум Онкелос или Раши (ША О.Х. 285:2)\n\n"
                "Сроки (ША О.Х. 285:4): идеально до трапезы в Шаббат; иначе до Минхи; некоторые — до среды или Симхат Торы."
            )
            break

    for s in strings:
        if s.startswith("Learn about the Nazirite vow"):
            fixes[s] = (
                "Обет назира! 🍇✂️💀 Три воздержания минимум 30 дней: (1) ничего от виноградной лозы; "
                "(2) без стрижки; (3) без контакта с мёртвыми. Осквернение — счёт сначала. Тора называет назира «кадош». "
                "Сегодня без Храма обет редок, но учит дисциплине. Источник: Бемидбар 6; Рамбам, Илхот Незирут 1."
            )
            break

    for s in strings:
        if s.startswith("Learn about one of the most surprising"):
            fixes[s] = (
                "Давар хариф — «острая» еда (лук, лимон, перец)! 🧅 В холоде вкус обычно не переходит, "
                "но острая еда «выжимает» мясной вкус с ножа без огня. Острый лук может «разбудить» старый вкус в стали. "
                "Поэтому многие держат парве-нож для острого."
            )
            break

    for s in strings:
        if s.startswith("Learn about the Temple Mount"):
            fixes[s] = (
                "Размеры Храмовой горы! 🏛️ 500×500 локтей со стеной; землю под ней выдолбили и поддержали сводами, "
                "чтобы тума от могил не поднималась."
            )
            break

    for s in strings:
        if s.startswith("Learn about Kotzer"):
            fixes[s] = (
                "Коцер — отделение роста от источника! ✂️ Срывать плоды, цветы, траву в горшке — мелаха. "
                "Мудрецы запретили пользоваться деревом в Шаббат — не лазить и не вешать гамаки."
            )
            break

    for s in strings:
        if s.startswith("Level up your character: Study Pirkei Avot"):
            fixes[s] = (
                "Прокачайте характер: изучайте Пиркей Авот! 📚 Сокровищница мудрости для мидот. "
                "Еженедельная привычка: один урок и применение сегодня. Источник: Мишна, Пиркей Авот; Бава Кама 30а."
            )
            break

    for s in strings:
        if s.startswith("Tzniut is one of the defining"):
            fixes[s] = (
                "Цниют — скромность в одежде и поведении (Миха 6:8). Не только дресс-код, а философия: "
                "сокровенное — для близких. Для женщин: колени, локти, декольте; для мужчин — достоинство в речи и одежде. "
                "Стандарты по общине — с равом и ребецин."
            )
            break

    for s in strings:
        if s.startswith("When you acquire new metal or glass"):
            fixes[s] = (
                "Твилат кеилим — погружение новой посуды из металла и стекла, сделанной неевреем, перед контактом с едой. "
                "Обязанность от производителя, не от магазина. Металл и стекло — с брахой; глазурованная керамика часто без. "
                "Полное погружение в микве; духовки — обычно только решётки. Спросите рава о пограничных случаях."
            )
            break

    for s in strings:
        if s.startswith("Mikra Megillah (hearing the Book of Esther)"):
            fixes[s] = (
                "Микра мегилла — слушать Книгу Эстер в Пурим (Эстер 9:28; Мегилла 19а). Мужчины и женщины обязаны.\n\n"
                "Когда: вечером после цейт; днём — основная мицва (митцват hа-йом), обычно после Шахарита.\n\n"
                "Как: каждое слово из кошерного свитка; брахот: аль микра мегилла, шеаса ниссим, шехехияну (ночь; ашкеназы и днём).\n\n"
                "Маахазит hа-шекель — обычай перед Мегиллой. Аль hа-ниссим в Амиде и бентчинге весь день."
            )
            break

    for s in strings:
        if s.startswith("Learn the Rambam's precise definition of teshuvah"):
            fixes[s] = (
                "Точное определение тешувы Рамбама! 🔄 Илхот Тешува 1:1 — позитивная заповедь при любом грехе. "
                "Четыре шага: (1) азиват hа-хейт — прекратить; (2) харата — искреннее сожаление; "
                "(3) кабалат ле-атид — решение не повторять; (4) видуй вслух — сердца мало! "
                "Между людьми — сначала загладить вину перед человеком."
            )
            break

    for s in strings:
        if s.startswith("A kezayit is the olive-sized"):
            fixes[s] = (
                "Кезаит — порция размером с оливу для мицвот (маца, марор, бентчинг). "
                "Современные оценки: ~25–33 г по весу, ~35–40 мл по объёму; для мацы часто 15–20 г. "
                "Уточните у рава для конкретной мицвы."
            )
            break

    for s in strings:
        if s.startswith("Tomorrow is $fastName"):
            fixes[s] = (
                "Завтра $fastName — публичный пост от рассвета (алот hашахар) до ночи (цейт).\n\n"
                "Если планируете есть до поста:\n"
                "• Условие (танай) накануне: «Если проснусь голодным до рассвета — поем». Без условия ранний приём пищи может запретить есть снова до рассвета (ША О.Х. 564:1).\n"
                "• До рассвета — вода и лёгкая еда; полноценный горячий приём спорен (МБ 564:8–9).\n"
                "• Прекратить еду и питьё в alot hashachar$alotLine.\n\n"
                "Подготовка сегодня: пейте, ужинайте сбалансированно, знайте расписание синагоги.\n\n"
                "Кто постится: взрослые евреи в здравии. Дети до бар/бат-мицва — постепенно по раву.$fridayNote"
            )
            break

    return fixes


if __name__ == "__main__":
    main()
