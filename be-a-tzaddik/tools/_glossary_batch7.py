"""Human-quality glossary fixes batch 7 — transliterated RU glossary → Cyrillic."""

from __future__ import annotations

from _glossary_batch6 import _CHESED_RU

BATCH7_RU: dict[str, str] = {
    "Chesed is loving-kindness — going beyond the strict requirement to help another person. Gemilut chasadim (bestowing kindness) includes visiting the sick, burying the dead, and free loans. The world is said to stand on Torah, avodah (service), and gemilut chasadim.": _CHESED_RU,
    "Baal Shem Tov — The Baal Shem Tov (Rabbi Israel ben Eliezer, c. 1700–1760) founded the Chasidic movement, teaching that every Jew can serve G-d with joy and that divine presence fills all creation. Chabad traces its spiritual lineage to his disciples.": (
        "Бааль Шем Тов — Бааль Шем Тов (рав Исраэль бен Элиэзер, ок. 1700–1760) основал хасидское "
        "движение, учившее, что каждый еврей может служить В-гу с радостью и что божественное "
        "присутствие наполняет всё творение. Хабад прослеживает духовную линию к его ученикам."
    ),
    "The Baal Shem Tov (Rabbi Israel ben Eliezer, c. 1700–1760) founded the Chasidic movement, teaching that every Jew can serve G-d with joy and that divine presence fills all creation. Chabad traces its spiritual lineage to his disciples.": (
        "Бааль Шем Тов (рав Исраэль бен Элиэзер, ок. 1700–1760) основал хасидское движение, "
        "учившее служить В-гу с радостью; божественное присутствие наполняет всё творение. "
        "Хабад прослеживает духовную линию к его ученикам."
    ),
    "Beit HaMikdash — The Beit HaMikdash (Holy Temple) stood in Jerusalem as the center of sacrifice, pilgrimage, and divine presence. Its destruction on Tisha B'Av is mourned yearly.": (
        "Бейт а-Микдаш — Святой Храм стоял в Иерусалиме как центр жертвоприношений, паломничества "
        "и божественного присутствия. Его разрушение в Тиша бе-Ав оплакивается ежегодно."
    ),
    "The Beit HaMikdash (Holy Temple) stood in Jerusalem as the center of sacrifice, pilgrimage, and divine presence. Its destruction on Tisha B'Av is mourned yearly.": (
        "Бейт а-Микдаш (Святой Храм) стоял в Иерусалиме как центр жертвоприношений, паломничества "
        "и божественного присутствия. Его разрушение в Тиша бе-Ав оплакивается ежегодно."
    ),
    "Daven (davening) means to pray. You 'daven' the daily services (Shacharit, Mincha, Maariv). The word is Yiddish-derived Ashkenazi slang but widely understood. People often say \"daven Shacharit\" for a service and \"daven\" for praying in general.": (
        "Давен (молитва) означает молиться. Вы «даавените» ежедневные службы (Шахарит, Минха, Маарив). "
        "Слово из идиша, но широко понятно. Говорят «даавенить Шахарит» о службе и «даавенить» о молитве вообще."
    ),
    "daven — Daven (davening) means to pray. You 'daven' the daily services (Shacharit, Mincha, Maariv). The word is Yiddish-derived Ashkenazi slang but widely understood. People often say \"daven Shacharit\" for a service and \"daven\" for praying in general.": (
        "давен — Давен означает молиться. Вы «даавените» ежедневные службы (Шахарит, Минха, Маарив). "
        "Слово из идиша, но широко понятно."
    ),
    "Elokei Neshama thanks G-d in the morning for restoring the soul, which was pure and will be returned at death. It follows Birchot HaShachar in many siddurim. The prayer teaches that each day is a new loan of life — use it for mitzvot, not only errands.": (
        "Элокей Нешама благодарит В-га утром за возвращение души, чистой и которую вернут при смерти. "
        "Следует за Биркот а-Шахар во многих сидурах. Молитва учит: каждый день — новый заём жизни; "
        "используйте его для мицвот, а не только для дел."
    ),
    "Elokei Neshama — Elokei Neshama thanks G-d in the morning for restoring the soul, which was pure and will be returned at death. It follows Birchot HaShachar in many siddurim. The prayer teaches that each day is a new loan of life — use it for mitzvot, not only errands.": (
        "Элокей Нешама — благодарит В-га утром за возвращение души, чистой и которую вернут при смерти. "
        "Следует за Биркот а-Шахар. Каждый день — новый заём жизни для мицвот."
    ),
    "Mayim achronim is washing the fingertips after a bread meal and before bentching — a reminder that we eat as servants before G-d. Not every community emphasizes it today, but many siddurim and bentchers include it. It is separate from netilat yadayim before the meal.": (
        "Маим ахроним — омовение кончиков пальцев после трапезы с хлебом и перед бентчингом — "
        "напоминание, что мы едим как слуги перед В-гом. Не каждая община подчёркивает это сегодня, "
        "но многие сидуры и бентчеры включают. Отдельно от нетилат ядаим перед едой."
    ),
    "Pirsumei nisa means publicizing the miracle — placing Chanukah menorah where passersby see the lights. The miracle of the oil is proclaimed to the street. Inside homes, many light by the window. Do not use the mitzvah candles for reading — use the shamash.": (
        "Пирсумей ниса означает оглашение чуда — поставить ханукальную менору там, где прохожие "
        "видят огни. Чудо с маслом провозглашается улице. Дома многие зажигают у окна. "
        "Не используйте свечи мицвы для чтения — используйте шамаш."
    ),
    "Alot hashachar is halachic dawn — when the sky begins to lighten. Many morning laws start here, such as the beginning of public fast days. It is before sunrise (netz). It is not the normal time for the bracha on tallit and tefillin — see misheyakir. In great need, Igros Moshe (O.C. 4:6) allows putting them on after alot hashachar without a bracha and reciting the brachos after misheyakir — ask your rav.": (
        "Алот а-шахар — галахический рассвет, когда небо начинает светлеть. Многие утренние законы "
        "начинаются здесь, например публичные посты. Это до восхода (нец). Не обычное время для "
        "брахи на талит и тфилин — см. мишейакир. В крайней нужде Игрот Моше (O.C. 4:6) разрешает "
        "надеть после алот а-шахар без брахи и произнести брахот после мишейакир — спросите раввина."
    ),
    "bracha acharona — Bracha acharona is the after-blessing said when you ate or drank enough of a food but did not have a bread meal requiring Birkat Hamazon — for example borei nefashot or al hamichya.": (
        "браха ахрона — благословение после еды или питья, когда вы съели или выпили достаточно, "
        "но не было трапезы с хлебом, требующей Биркат а-Мазон — например Борей нефашот или ал hа-михья."
    ),
    "bitul — nullification (e.g. Kol Chamira nullifying chametz)": (
        "битуль — аннулирование (напр. Кол Хамира, аннулирующий хамец)"
    ),
    "nullification (e.g. Kol Chamira nullifying chametz)": (
        "аннулирование (напр. Кол Хамира, аннулирующий хамец)"
    ),
    "simanim — Simanim are symbolic Rosh Hashana foods — apple in honey, pomegranate, fish head, dates — each with a pun or prayer for the new year. They are minhag, not Torah law, but beloved for teaching children. Say the short yehi ratzon prayers from the machzor.": (
        "симаним — символические новогодние продукты: яблоко в мёде, гранат, рыбья голова, финики — "
        "каждый с игрой слов или молитвой на новый год. Это минхаг, не закон Торы, но любим для "
        "обучения детей. Произнесите короткие «йеhi рацон» из махзора."
    ),
    "Simanim are symbolic Rosh Hashana foods — apple in honey, pomegranate, fish head, dates — each with a pun or prayer for the new year. They are minhag, not Torah law, but beloved for teaching children. Say the short yehi ratzon prayers from the machzor.": (
        "Симаним — символические новогодние продукты: яблоко в мёде, гранат, рыбья голова, финики — "
        "каждый с игрой слов или молитвой. Минхаг, любимый для обучения детей."
    ),
    "Tekiah is a long straight shofar blast. The Rosh Hashana sequence combines tekiah with shevarim (broken) and teruah (trembling) sounds per minhag. One hundred blasts are customary Ashkenaz. Practice in shul beforehand so the day is not your first time hearing shofar.": (
        "Текия — длинный ровный звук шофара. Последовательность Рош а-Шана сочетает текию с шеварим "
        "(прерывистым) и труа (дрожащим) по минхагу. Сто звуков — обычай ашкеназов. Потренируйтесь "
        "в синагоге заранее."
    ),
    "Shamor — \"guard (Shabbat)\" from the Deuteronomy version of the Ten Commandments": (
        "Шамор — «храни (Шаббат)» из версии Десяти заповедей в Дварим"
    ),
    "Rosh Hashana is the Jewish New Year and Day of Judgment — shofar, festive meals, and solemn prayer. It begins the Ten Days of Teshuvah leading to Yom Kippur. Customs include apples in honey, new fruit, and tashlich. Work is forbidden like Yom Tov.": (
        "Рош а-Шана — еврейский Новый год и Судный день: шофар, праздничные трапезы и торжественная "
        "молитва. Начинаются Десять дней покаяния до Йом Кипура. Обычаи: яблоко в мёде, новый плод, "
        "ташлих. Работа запрещена как в Йом Тов."
    ),
    "Rosh Hashana — Rosh Hashana is the Jewish New Year and Day of Judgment — shofar, festive meals, and solemn prayer. It begins the Ten Days of Teshuvah leading to Yom Kippur. Customs include apples in honey, new fruit, and tashlich. Work is forbidden like Yom Tov.": (
        "Рош а-Шана — еврейский Новый год и Судный день: шофар, праздничные трапезы и торжественная "
        "молитва. Начинаются Десять дней покаяния до Йом Кипура."
    ),
    "Vatodi'enu — Vatodi'enu (\"You have made us know\") is an insert in the Maariv Amidah on Saturday night when Yom Tov begins after Shabbat. It acknowledges that Shabbat has ended and the festival has begun.": (
        "Ватоди'ену («Ты дал нам знать») — вставка в Амиду Маарива в субботу вечером, когда Йом Тов "
        "начинается после Шаббата. Признаёт, что Шаббат закончился и праздник начался."
    ),
    "Vatodi'enu (\"You have made us know\") is an insert in the Maariv Amidah on Saturday night when Yom Tov begins after Shabbat. It acknowledges that Shabbat has ended and the festival has begun.": (
        "Ватоди'ену («Ты дал нам знать») — вставка в Амиду Маарива в субботу вечером, когда Йом Тов "
        "начинается после Шаббата."
    ),
    "Borei Minei Besamim — catch-all blessing on fragrant herbs, spices, flowers, fruit, etc. (often said as the spice blessing in Havdalah after Shabbat, though some will say a more specific blessing on pleasant fragrances)": (
        "Борей миней бесамим — общая браха на ароматные травы, специи, цветы, плоды и т.д. "
        "(часто как благословение на специи при Хавдале после Шаббата; некоторые произносят более "
        "специфическую браху на приятные ароматы)"
    ),
    "catch-all blessing on fragrant herbs, spices, flowers, fruit, etc. (often said as the spice blessing in Havdalah after Shabbat, though some will say a more specific blessing on pleasant fragrances)": (
        "общая браха на ароматные травы, специи, цветы, плоды и т.д. (часто на Хавдале после Шаббата)"
    ),
    "Add your own mitzvah! ✡ The Talmud teaches that each person has their own unique way of serving G-d. Think of a mitzvah that can bring more light into the world and submit it in the 'Add a Mitzvah' section of the app!": (
        "Добавьте свою мицву! ✡ Талмуд учит, что у каждого свой путь служения В-гу. "
        "Придумайте мицву, которая принесёт больше света в мир, и отправьте её в разделе "
        "«Добавить мицву» в приложении!"
    ),
    "Got some spare time? Visit someone who's sick. The Talmud (Nedarim 39b) says this removes 1/60th of their illness!": (
        "Есть свободное время? Навестите больного. Талмуд (Недарим 39b) говорит, что это убирает "
        "1/60 болезни!"
    ),
    "Got some spare time? Visit someone who's sick. Our sages teach that visiting the sick removes one-sixtieth of their illness! The mitzvah of bikkur cholim — visiting the sick — is one of the ways we walk in G-d's ways, just as He visited Abraham when he was recovering. Jewish law teaches that we should sit with the ill, comfort them, and help with whatever they need — even if it's just keeping them company for a few minutes. Your presence can lift someone's spirits as much as any medicine.": (
        "Есть свободное время? Навестите больного. Мудрецы учат, что навещение больных убирает "
        "одну шестидесятую болезни! Мицва биккур холим — один из путей идти путями В-га, "
        "как Он навещал Авраама при выздоровлении. Сидите с больным, утешайте и помогайте — "
        "даже несколько минут общения могут поднять дух не хуже лекарства."
    ),
    "Discover the power of Peh (פ)! 👄 The Peh means 'mouth' and teaches us about the power of speech. Its shape includes a Bet (ב) on its inside (the empty space within it when written in a Torah scrollforms a bet), reminding us that what's inside us affects what we say.": (
        "Откройте силу Пе (פ)! 👄 Пе означает «рот» и учит о силе речи. Его форма включает Бет (ב) "
        "внутри (пустое пространство в свитке Торы образует бет), напоминая, что внутреннее "
        "влияет на то, что мы говорим."
    ),
}
