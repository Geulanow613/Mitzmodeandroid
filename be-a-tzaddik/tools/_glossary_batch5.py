"""Human-quality glossary fixes batch 5 — RU glossary glue + long explainer texts."""

from __future__ import annotations

_HALACHA_RU = (
    "Галаха означает «путь ходьбы» — практический путь еврейской жизни, вытекающий из Торы, "
    "Пророков, Писаний, Мишны, Талмуда и кодексов вроде Шулхан Арух. Она охватывает молитву, "
    "Шаббат, кашрут, семейную жизнь, деловую этику и праздники. Галаху определяют квалифицированные "
    "посеким; когда вы не уверены, мицва — спросить раввина, а не гадать."
)

_BESAMIM_RU = (
    "Бесамим — ароматные специи, которыми пахнут во время Хавдалы в субботу вечером — "
    "утешение души, когда дополнительная нешама йетейра возвращается к будничной жизни. "
    "Благословение — Борей миней бесамим. Часто используют гвоздику, мирт или сладкие специи. "
    "Yaknehaz опускает бесамим, когда Шаббат переходит в Йом Тов — радость праздника сама утешает "
    "душу, поэтому специи галахически не нужны."
)

_CHASSIDUT_RU = (
    "Хасидут (хасидская философия) учит служить В-гу с радостью, искренностью и осознанием, "
    "что божественные искры наполняют всю жизнь. Основана Бааль Шем Товом, распространилась через "
    "дворы вроде Хабада, Бреслова и Сатмара. «Таня» — центральный текст хасидут для ежедневного изучения."
)

_LEINING_RU = (
    "Лейнинг — чтение порции Торы из свитка с точными тропами (кантилацией). Бааль корэ тренируется "
    "месяцами. Молящиеся следят в Хумаше. Слушание каждого слова в утро Шаббата выполняет "
    "общинную мицву публичного чтения Торы."
)

_MASHIACH_RU = (
    "Машиах (Мошиах) — праведный помазанный царь из рода Давида, который восстановит Храм, "
    "соберёт изгнанников и принесёт всемирное познание В-га. Вера в его приход — основа еврейской "
    "веры. Мы молимся о геуле ежедневно."
)

_NUSACH_RU = (
    "Нусах — «стиль» молитвы общины: какие слова произносятся, в каком порядке и какие мелодии "
    "используются. Примеры: нусах ашкеназ, нусах сефард (сефардская / иберийская диаспора), "
    "нусах Edot HaMizrach (ближневосточные и североафриканские кехилот), нусах сефард в хасидском "
    "сидуре (ашкенази-хасидский — не сефардский) и нусах ари (Хабад)."
)

_OLAM_HABA_RU = (
    "Олам ха-ба — мир грядущий, вечная близость к В-гу после этой жизни (рай). Мицвот, изучение "
    "Торы и добрые дела создают заслуги, которые мудрецы описывают как непостижимые сокровища "
    "в том мире."
)

_KABBALAH_RU = (
    "Каббала — еврейская мистическая традиция, исследующая отношение В-га к творению, душе и мицвот. "
    "Тексты включают Зоар и более поздние труды вроде «Тани». Подлинная каббала изучается при знании "
    "Торы и с учителем; это не гадание и не магия."
)

_KDEI_ACHILAT_RU = (
    "K'dei achilat pras — галахический предел времени, чтобы съесть определённый объём пищи как "
    "один акт еды — около 4–7 минут по мнению многих посеким. Применяется к еде кезайита мацы "
    "на Седере, марора и к достаточному количеству хлеба или выпечки в этом окне для благословения "
    "после еды (Биркат а-Мазон или ал hа-михья). Если вы съели кезайит хлеба в пределах k'dei "
    "achilat pras, Биркат а-Мазон покрывает всю трапезу."
)

_SUKKAH_RU = (
    "Сукка — временная хижина с крышей из схах, где мы едим (и некоторые спят) на Суккот — "
    "в память о пустынных облаках славы. Стены должны стоять; схах должен давать больше тени, "
    "чем солнца. Украшайте для радости; приглашайте гостей и ушпизин. Дождь может освобождать "
    "от еды в сукке — следуйте галахе для вашей ситуации."
)

BATCH5_RU: dict[str, str] = {
    "Halacha means \"the way of walking\" — the practical path of Jewish life drawn from Torah, Prophets, Writings, Mishnah, Talmud, and codes like the Shulchan Aruch. It covers prayer, Shabbat, diet, family life, business ethics, and festivals. Halacha is decided by qualified poskim; when you are unsure, the mitzvah is to ask your rav rather than guess.": _HALACHA_RU,
    "halacha — Halacha means \"the way of walking\" — the practical path of Jewish life drawn from Torah, Prophets, Writings, Mishnah, Talmud, and codes like the Shulchan Aruch. It covers prayer, Shabbat, diet, family life, business ethics, and festivals. Halacha is decided by qualified poskim; when you are unsure, the mitzvah is to ask your rav rather than guess.": f"галаха — {_HALACHA_RU}",
    "Besamim are fragrant spices smelled during Havdalah Saturday night — comforting the soul as the extra neshama yeteira departs back into weekday life. The blessing is Borei minei besamim. Cloves, myrtle, or sweet spices are common. Yaknehaz omits besamim when Shabbat flows into Yom Tov — the joy of the festival itself comforts the soul, so spices are halachically unnecessary.": _BESAMIM_RU,
    "besamim — Besamim are fragrant spices smelled during Havdalah Saturday night — comforting the soul as the extra neshama yeteira departs back into weekday life. The blessing is Borei minei besamim. Cloves, myrtle, or sweet spices are common. Yaknehaz omits besamim when Shabbat flows into Yom Tov — the joy of the festival itself comforts the soul, so spices are halachically unnecessary.": f"бесамим — {_BESAMIM_RU}",
    "Chassidut (Chasidic philosophy) teaches serving G-d with joy, sincerity, and awareness that divine sparks fill all of life. Founded by the Baal Shem Tov, it spread through courts like Chabad, Breslov, and Satmar. Tanya is a central Chassidut text for daily study.": _CHASSIDUT_RU,
    "Chassidut — Chassidut (Chasidic philosophy) teaches serving G-d with joy, sincerity, and awareness that divine sparks fill all of life. Founded by the Baal Shem Tov, it spread through courts like Chabad, Breslov, and Satmar. Tanya is a central Chassidut text for daily study.": f"Хасидут — {_CHASSIDUT_RU}",
    "Leining is chanting the Torah portion from the scroll with precise trop (cantillation). The baal koreh trains for months. Congregants follow in a Chumash. Hearing every word on Shabbat morning fulfills the communal mitzvah of public Torah reading.": _LEINING_RU,
    "leining — Leining is chanting the Torah portion from the scroll with precise trop (cantillation). The baal koreh trains for months. Congregants follow in a Chumash. Hearing every word on Shabbat morning fulfills the communal mitzvah of public Torah reading.": f"лейнинг — {_LEINING_RU}",
    "Mashiach (Moshiach) is the righteous anointed king from David's line who will rebuild the Temple, gather the exiles, and bring universal knowledge of G-d. Belief in his coming is a foundation of Jewish faith. We pray for redemption daily.": _MASHIACH_RU,
    "Mashiach — Mashiach (Moshiach) is the righteous anointed king from David's line who will rebuild the Temple, gather the exiles, and bring universal knowledge of G-d. Belief in his coming is a foundation of Jewish faith. We pray for redemption daily.": f"Машиах — {_MASHIACH_RU}",
    'Nusach is the prayer "style" of a community: which words are said, in what order, and often which melodies are used. Common examples include Nusach Ashkenaz, Nusach Sefard (Sephardi / Iberian diaspora), Nusach Edot HaMizrach (Middle Eastern and North African kehillot), Nusach Sefard on a Chasidic siddur (Ashkenazi-Chasidic — not Sephardi), and Nusach Ari (Chabad).': _NUSACH_RU,
    'nusach — Nusach is the prayer "style" of a community: which words are said, in what order, and often which melodies are used. Common examples include Nusach Ashkenaz, Nusach Sefard (Sephardi / Iberian diaspora), Nusach Edot HaMizrach (Middle Eastern and North African kehillot), Nusach Sefard on a Chasidic siddur (Ashkenazi-Chasidic — not Sephardi), and Nusach Ari (Chabad).': f"нусах — {_NUSACH_RU}",
    "Olam HaBa is the World to Come — the eternal reality of closeness to G-d after this life (Heaven). Mitzvot, Torah study, and acts of kindness build merits that the sages describe as unfathomable treasures in that world.": _OLAM_HABA_RU,
    "Olam HaBa — Olam HaBa is the World to Come — the eternal reality of closeness to G-d after this life (Heaven). Mitzvot, Torah study, and acts of kindness build merits that the sages describe as unfathomable treasures in that world.": f"Олам ха-ба — {_OLAM_HABA_RU}",
    "Kabbalah is the Jewish mystical tradition exploring how G-d relates to creation, the soul, and mitzvot. Texts include the Zohar and later works like Tanya. Authentic Kabbalah is studied with Torah literacy and a teacher; it is not fortune-telling or magic.": _KABBALAH_RU,
    "Kabbalah — Kabbalah is the Jewish mystical tradition exploring how G-d relates to creation, the soul, and mitzvot. Texts include the Zohar and later works like Tanya. Authentic Kabbalah is studied with Torah literacy and a teacher; it is not fortune-telling or magic.": f"Каббала — {_KABBALAH_RU}",
    "K'dei achilat pras is the halachic time limit to consume a specific volume of food so it counts as one act of eating — about 4–7 minutes per many poskim. It applies to eating a kezayit of matzah at the Seder, maror, and eating enough bread or cake within that window to require an after-blessing (Birkat Hamazon or al hamichya). If you ate a kezayit of bread within k'dei achilat pras, Birkat Hamazon covers the whole meal.": _KDEI_ACHILAT_RU,
    "Sukkah — A sukkah is a temporary booth with schach roof where we eat (and some sleep) on Sukkot — recalling desert clouds of glory. Walls must stand; schach must shade more than sun. Decorate for joy; invite guests and ushpizin. Rain may exempt you from eating in the sukkah — follow halacha for your situation.": f"Сукка — {_SUKKAH_RU}",
    "Ashkenazi": "Ашкенази",
    "Gemara": "Гемара",
    "Tehillim": "Техиллим",
    "Rosh Chodesh": "Рош Ходеш",
    "Have Mezuzot on your doorposts": "Повесить мезузот на дверные косяки",
    "The klaf is the special kosher parchment on which scribes write Torah scrolls, mezuzots, tefillin scrolls, etc.": (
        "Клаф — особый кошерный пергамент, на котором соферим пишут свитки Торы, мезузот, свитки тфилин и т.д."
    ),
    "Bracha acharona is the after-blessing said when you ate or drank enough of a food but did not have a bread meal requiring Birkat Hamazon — for example borei nefashot or al hamichya.": (
        "Браха ахрона — благословение после еды или питья, когда вы съели или выпили достаточно, "
        "но не было трапезы с хлебом, требующей Биркат а-Мазон — например Борей нефашот или ал hа-михья."
    ),
    "Ruach Ra'ah (\"evil spirit\") is a rabbinic term for the impurity resting on hands after sleep. Morning netilat yadayim removes it by washing hands alternating right and left 3 times each.": (
        "Руах раа («злой дух») — раввинистический термин для нечистоты на руках после сна. "
        "Утреннее нетилат ядаим устраняет её, омывая руки поочерёдно правую и левую по три раза."
    ),
    "Ruach Ra'ah — Ruach Ra'ah (\"evil spirit\") is a rabbinic term for the impurity resting on hands after sleep. Morning netilat yadayim removes it by washing hands alternating right and left 3 times each.": (
        "руах раа — Руах раа («злой дух») — раввинистический термин для нечистоты на руках после сна. "
        "Утреннее нетилат ядаим устраняет её омовением правой и левой руки по три раза."
    ),
    "Nerot — candles, often referring especially to Shabbat and Yom Tov lights": (
        "Нерот — свечи, часто особенно шабатные и йом-товские огни"
    ),
    "YaKNeHaZ is a mnemonic for the order when Shabbat flows directly into Yom Tov (for example Saturday night Pesach). Use your siddur's festival night Kiddush section for instructions.": (
        "YaKNeHaZ — мнемоника порядка, когда Шаббат переходит непосредственно в Йом Тов "
        "(например субботний вечер Песаха). Используйте раздел киддуша праздничной ночи в вашем сидуре."
    ),
    "Yaknehaz — YaKNeHaZ is a mnemonic for the order when Shabbat flows directly into Yom Tov (for example Saturday night Pesach). Use your siddur's festival night Kiddush section for instructions.": (
        "Yaknehaz — YaKNeHaZ — мнемоника порядка, когда Шаббат переходит непосредственно в Йом Тов. "
        "Смотрите раздел киддуша праздничной ночи в сидуре."
    ),
    "Chasidim — a group of Jews who follow a movement that emphasizes deep mystical devotion, joyful worship, and a close-knit community led by a spiritual leader known as a Rebbe.": (
        "Хасидим — евреи, следующие движению с глубокой мистической преданностью, радостной молитвой "
        "и тесной общиной под духовным лидером — ребе."
    ),
    "a group of Jews who follow a movement that emphasizes deep mystical devotion, joyful worship, and a close-knit community led by a spiritual leader known as a Rebbe.": (
        "группа евреев, следующих движению с глубокой мистической преданностью, радостной молитвой "
        "и тесной общиной под духовным лидером — ребе."
    ),
    "klaf — The klaf is the special kosher parchment on which scribes write Torah scrolls, mezuzots, tefillin scrolls, etc.": (
        "клаф — особый кошерный пергамент, на котором соферим пишут свитки Торы, мезузот, свитки тфилин и т.д."
    ),
    "palm branch waved with the etrog, willows, and myrtle branches on Sukkot": (
        "пальмовая ветвь, которую машут с этрогом, ивой и ветками мирта на Суккот"
    ),
    "lulav — palm branch waved with the etrog, willows, and myrtle branches on Sukkot": (
        "лулав — пальмовая ветвь с этрогом, ивой и миртом на Суккот"
    ),
    "The time to fulfill the mitzvah of tefillin has passed.": "Время мицвы тфилин прошло.",
    "The time to fulfill the mitzvah of tefillin has passed. Tefillin are still valid to wear until sunset ({time}).": (
        "Время мицвы тфилин прошло. Их ещё можно надевать до заката ({time})."
    ),
    "The time to fulfill the mitzvah of Shema has passed ({time}). Still say Shema with its blessings during Shacharit until midday — even if you missed it, say it during prayer.": (
        "Время мицвы чтения Шма прошло ({time}). Всё равно читайте Шма с благословениями на Шахарите до полудня — "
        "даже если пропустили, произнесите во время молитвы."
    ),
    "The time to fulfill Shacharit has passed": "Время для Шахарита прошло",
    "The time to fulfill Shacharit has passed ({time}). You can still daven until midday.": (
        "Время для Шахарита прошло ({time}). Всё ещё можно молиться до полудня."
    ),
}

_RC_INTRO_RU = (
    "Рош Ходеш (ראש חודש) — новый месяц — полупраздник с дополнительными молитвами и обычаями.\n\n"
    "Праздничная трапеза (мицва):\n"
    "• Мицва увеличить трапезу в Рош Ходеш — как минимум добавить блюдо или особую еду в честь дня "
    "(Шулхан Арух O.C. 419:1).\n"
    "• Трапеза днём. Посеким пишут, что это воспоминает пир Синедриона в Бейт Я'зек для свидетелей "
    "новолуния (Мишна Рош а-Шана 2:5; Орхот Хаим и Кол Бо, цит. на O.C. 419).\n"
    "• Деньги на трапезы Рош Ходеш — как на Шаббат и Йом Тов — не вычитаются из дохода, "
    "отведённого на Рош а-Шана; если тратите больше на эти мицвот, Небеса добавляют к вашей доле "
    "(Песикта де-Рав Кахана, цит. в Тур O.C. 419 и Маген Авраам 419:1).\n\n"
)

_RC_DAVENING_DETAIL_RU = (
    "Молитва сегодня (см. разделы Утро, День и Вечер):\n"
    "• Если читаете Шахарит, Минху или Маарив Амиду — добавьте Яале ве-яво в Рецей. "
    "Шахарит/Минха: исправляйте по моменту (вставьте в Рецей, вернитесь к началу Рецей если уже "
    "закончили, или повторите только эту Амиду если завершили). Маарив только в Рош Ходеш: "
    "не повторяйте, если забыли после Рецей (Берахот 30b; SA O.C. 422:1)\n"
    "• Если говорите Биркат а-Мазон с хлебом — добавьте Яале ве-яво и там\n"
    "• Обычай ашкеназов — большинство авторитетов обязывают Амиду Шахарит и Минхи в эти дни\n"
    "• Обычай сефардов — многие женщины выполняют дневную обязанность одной молитвой; "
    "если молитесь дополнительную Амиду и забыли Яале ве-яво, спросите раввина о добровольной "
    "оговорке (недава)\n"
    "• Полу-Галель на Шахарите, если читаете Галель (по желанию — следуйте минхагу; полный Галель "
    "если Рош Ходеш в Хануку)\n"
    "• Таханун не читают весь день\n\n"
)

_RC_DAVENING_SUMMARY_RU = (
    "Молитва сегодня (см. разделы Утро, День и Вечер):\n"
    "• Яале ве-яво в Амиде Шахарит, Минхи и Маарив — и при бентчинге с хлебом\n"
    "• Полу-Галель на Шахарите (полный Галель если Рош Ходеш в Хануку; без Галеля в Рош Ходеш "
    "Тишри / Рош а-Шана)\n"
    "• Мусаф после Шахарита — снимите тфилин перед Мусафом\n"
    "• Таханун не читают весь день\n\n"
)

_RC_OTHER_RU = (
    "Другие обычаи:\n"
    "• Пост и надгробные речи обычно не делают в Рош Ходеш\n"
    "• Распространённый обычай: замужние женщины воздерживаются от некоторых мелахот "
    "(рукоделие, стирка и т.д.) как дополнительный знак почёта — уточните у раввина\n\n"
    "Когда Рош Ходеш длится два дня (30-й предыдущего месяца и 1-й), обычаи применяются к обоим дням."
)

_MODEH_ANI_RU = (
    "Первое действие каждого дня:\n\n"
    "Что это:\n"
    "Моде Ани (מוֹדֶה אֲנִי — буквально «Я благодарен») — двухстрочная молитва в момент, "
    "когда открываете глаза. Самое простое выражение еврейской благодарности — благодарность В-гу "
    "за возвращение души после сна.\n\n"
    "Традиция:\n"
    "Еврейская мысль учит, что сон — ночной опыт близкий к смерти: душа частично покидает тело "
    "и возвращается утром. Моде Ани признаёт, что В-г дал вам ещё один день.\n\n"
    "Текст и перевод:\n"
    "«Modeh ani lifanecha, Melech chai v'kayam, shehechezarta bi nishmati b'chemla — raba emunatecha.»\n"
    "«Благодарю Тебя, живой и вечный Царь, что с милостью вернул мне душу — велика Твоя верность.»\n\n"
    "Как делать:\n"
    "Произнесите, ещё в постели, до вставания, сразу после основного ночного сна — до всего остального. "
    "Моде Ани — одна из немногих частей молитвы, которую можно сказать до омовения рук; затем — "
    "нетилат ядаим (негель вассер; см. следующий пункт).\n\n"
    "Ночной сон и дневной сон:\n"
    "Эти слова можно повторять как личную благодарность, но это не официальная утренняя молитва "
    "и не имеет того же галахического статуса.\n\n"
    "Традиция сохраняет Моде Ани для пробуждения после основного ночного сна. После дневного сна "
    "не требуется и обычно не читается — просто омойте руки (негель вассер) по правилам следующего пункта."
)

_KIDDUSH_LEVANA_RU = (
    "Кидуш левана (קִידּוּשׁ לְבָנָה — освящение Луны) — ежемесячное благословение, произносимое "
    "раз в еврейский месяц, когда новая луна видна ночью. Это не поклонение луне — хвала В-гу "
    "за циклы творения и обновление Израиля. Талмуд (Санхедрин 42a) сравнивает правильно "
    "благословившего луну с встречей Шехины. Мужчины обязаны в этой мицве, связанной со временем; "
    "женщины освобождены (как от большинства таких мицв), и широкий давний обычай — что женщины "
    "вообще не читают Кидуш левана (Шела, Маген Авраам O.C. 426:1; также каббалистические причины). "
    "Читайте на улице под открытым небом, стоя, после наступления ночи. Ашкеназы и Хабад часто "
    "ждут минимум три дня после новолуния; большинство сефардов — семь (Шулхан Арух O.C. 426:4); "
    "марокканские и некоторые североафриканские кехилот могут начинать через три дня "
    "(Пениней а-Алаха 05-01-18). Моцэи Шаббат — распространённое предпочтение, когда люди "
    "одеты празднично и луна видна; но если ожидание грозит пропустить окно из-за облачности, "
    "читайте в первую ясную буднюю ночь, когда мицва становится возможной. Окно закрывается "
    "в момент полнолуния (примерно 14,75 дня месяца) — в ночь 15-го может быть уже поздно; "
    "проверьте соф зман Кидуш левана для вашего места. Также называется Биркат hа-левана."
)


def _register_long_keys() -> None:
    import json
    from pathlib import Path

    catalog = Path(__file__).resolve().parents[1] / "data" / "translation-catalog" / "strings.json"
    for key in json.loads(catalog.read_text(encoding="utf-8"))["strings"]:
        if key.startswith("Rosh Chodesh (") and len(key) > 500:
            body = _RC_DAVENING_DETAIL_RU if "If you recite" in key else _RC_DAVENING_SUMMARY_RU
            BATCH5_RU[key] = _RC_INTRO_RU + body + _RC_OTHER_RU
        elif key.startswith("The very first act of each day"):
            BATCH5_RU[key] = _MODEH_ANI_RU
        elif key.startswith("Kiddush Levana (קִידּוּשׁ"):
            BATCH5_RU[key] = _KIDDUSH_LEVANA_RU
        elif key.startswith("Kiddush Levana — Kiddush Levana (קִידּוּשׁ"):
            BATCH5_RU[key] = f"Кидуш левана — {_KIDDUSH_LEVANA_RU}"


_register_long_keys()
