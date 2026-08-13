"""Human-reviewed paragraph fixes for Argos-corrupted catalog strings."""

# Russian
RU: dict[str, str] = {
    "\n\nShabbat Erev Tisha B'Av (when 9 Av is Shabbat and the fast is moved to Sunday):\n• Shabbat is celebrated fully until sunset — no mourning restrictions on Shabbat itself.\n• After Shabbat ends (Havdalah), change into non-leather shoes and begin the fast and mourning practices.\n• The seudah hamafseket is not eaten on Shabbat — it is observed after Shabbat ends, before the fast begins.": (
        "\n\nШаббат Эрев Тиша б'Ав (когда 9 Ав выпадает на Шаббат и пост переносят на воскресенье):\n"
        "• Шаббат отмечают полностью до заката — без траурных ограничений в сам Шаббат.\n"
        "• После окончания Шаббата (Хавдала) переобуйтесь в немеховую обувь и начните пост и траурные практики.\n"
        "• Сеуда амафсекет не едят в Шаббат — её соблюдают после окончания Шаббата, до начала поста."
    ),
    "A kezayit is the olive-sized portion in Jewish law used to measure how much food you must consume for mitzvot — eating matzah at the Seder, maror, bentching after bread, and after-blessings. Because ancient volumes are difficult to calculate precisely, rabbis have established modern equivalents. Halachically, a kezayit is tied to the size of an egg — usually calculated as either half or one-third of an egg's volume depending on the authority. Many major organizations (such as Star-K) equate a kezayit to about 1.2 to 1.3 fluid ounces (approx. 35–40 ml), which visually translates to the size of a golf ball or a roll of quarters. By weight, this generally ranges between 25 and 33 grams — the stringent opinion of the Chazon Ish is approx. 33 g; the more lenient opinion of Rabbi Chayim Na'eh is approx. 27 g. (Note: For porous foods like matzah, the required weight is typically lower — around 15–20 grams depending on your rav.) The exact size depends on which authority you follow; ask your rav or use a community chart for the specific food or mitzvah you are measuring. Eating less than a kezayit may mean the mitzvah was not fulfilled.": (
        "Кезаит — порция размером с оливу в еврейском законе, используемая для измерения количества пищи, "
        "которое нужно съесть для мицвот — матца на Седере, марор, бентчинг после хлеба и благословения после еды. "
        "Поскольку древние объёмы трудно точно вычислить, раввины установили современные эквиваленты. "
        "По галахе, кезаит привязан к размеру яйца — обычно половина или треть объёма яйца в зависимости от авторитета. "
        "Многие крупные организации (например Star-K) приравнивают кезаит к примерно 35–40 мл, "
        "что визуально соответствует размеру мячика для гольфа. По весу это обычно 25–33 грамма — "
        "строгое мнение Хазон Иша около 33 г; более мягкое мнение раввина Хаима Наэ — около 27 г. "
        "(Примечание: для пористых продуктов, таких как маца, требуемый вес обычно ниже — около 15–20 грамм по мнению вашего раввина.) "
        "Точный размер зависит от авторитета, которому вы следуете; спросите раввина или используйте общинную таблицу "
        "для конкретного продукта или мицвы. Съесть меньше кезаита может означать, что мицва не была выполнена."
    ),
    "borei nefashot — Borei nefashot is the short after-blessing on many foods and drinks — fruit (aside from dates, grapes, olives, figs, and pomegranates which use the Me'ein Shalosh bracha), vegetables, drinks (other than wine/grape juice which use Me'ein Shalosh), meat, cheese, etc.": (
        "borei nefashot — Борей нефашот — короткое послеблагословение на многие продукты и напитки — "
        "фрукты (кроме фиников, винограда, оливок, инжира и гранатов, для которых — Ме'ейн Шалош), "
        "овощи, напитки (кроме вина и виноградного сока — Ме'ейн Шалош), мясо, сыр и т.д."
    ),
    "kezayit — A kezayit is the olive-sized portion in Jewish law used to measure how much food you must consume for mitzvot — eating matzah at the Seder, maror, bentching after bread, and after-blessings. Because ancient volumes are difficult to calculate precisely, rabbis have established modern equivalents. Halachically, a kezayit is tied to the size of an egg — usually calculated as either half or one-third of an egg's volume depending on the authority. Many major organizations (such as Star-K) equate a kezayit to about 1.2 to 1.3 fluid ounces (approx. 35–40 ml), which visually translates to the size of a golf ball or a roll of quarters. By weight, this generally ranges between 25 and 33 grams — the stringent opinion of the Chazon Ish is approx. 33 g; the more lenient opinion of Rabbi Chayim Na'eh is approx. 27 g. (Note: For porous foods like matzah, the required weight is typically lower — around 15–20 grams depending on your rav.) The exact size depends on which authority you follow; ask your rav or use a community chart for the specific food or mitzvah you are measuring. Eating less than a kezayit may mean the mitzvah was not fulfilled.": (
        "kezayit — Кезаит — порция размером с оливу в еврейском законе, для измерения количества пищи для мицвот — "
        "матца на Седере, марор, бентчинг после хлеба и благословения после еды. "
        "Раввины установили современные эквиваленты. По галахе привязан к размеру яйца — половина или треть. "
        "Обычно ~35–40 мл; по весу 25–33 г (Хазон Иш ~33 г; Хаим Наэ ~27 г). Для мацы — 15–20 г. "
        "Спросите раввина. Меньше кезаита — мицва может не выполниться."
    ),
}

# Spanish
ES: dict[str, str] = {
    "borei nefashot — Borei nefashot is the short after-blessing on many foods and drinks — fruit (aside from dates, grapes, olives, figs, and pomegranates which use the Me'ein Shalosh bracha), vegetables, drinks (other than wine/grape juice which use Me'ein Shalosh), meat, cheese, etc.": (
        "borei nefashot — Borei nefashot es la bendición breve posterior sobre muchos alimentos y bebidas — "
        "fruta (excepto dátiles, uvas, aceitunas, higos y granadas, que usan la bracha Me'ein Shalosh), "
        "verduras, bebidas (excepto vino o jugo de uva, que usan Me'ein Shalosh), carne, queso, etc."
    ),
    "A kezayit is the olive-sized portion in Jewish law used to measure how much food you must consume for mitzvot — eating matzah at the Seder, maror, bentching after bread, and after-blessings. Because ancient volumes are difficult to calculate precisely, rabbis have established modern equivalents. Halachically, a kezayit is tied to the size of an egg — usually calculated as either half or one-third of an egg's volume depending on the authority. Many major organizations (such as Star-K) equate a kezayit to about 1.2 to 1.3 fluid ounces (approx. 35–40 ml), which visually translates to the size of a golf ball or a roll of quarters. By weight, this generally ranges between 25 and 33 grams — the stringent opinion of the Chazon Ish is approx. 33 g; the more lenient opinion of Rabbi Chayim Na'eh is approx. 27 g. (Note: For porous foods like matzah, the required weight is typically lower — around 15–20 grams depending on your rav.) The exact size depends on which authority you follow; ask your rav or use a community chart for the specific food or mitzvah you are measuring. Eating less than a kezayit may mean the mitzvah was not fulfilled.": (
        "Un kezayit es la porción del tamaño de una aceituna en la ley judía, usada para medir cuánto alimento "
        "debes consumir para mitzvot — comer matzá en el Seder, maror, bentching después del pan y bendiciones posteriores. "
        "Como los volúmenes antiguos son difíciles de calcular con precisión, los rabinos han establecido equivalentes modernos. "
        "Halájicamente, un kezayit se vincula al tamaño de un huevo — normalmente la mitad o un tercio del volumen del huevo "
        "según la autoridad. Muchas organizaciones importantes (como Star-K) equiparan un kezayit a unos 35–40 ml, "
        "lo que visualmente equivale al tamaño de una pelota de golf. En peso, suele oscilar entre 25 y 33 gramos — "
        "la opinión estricta del Jazón Ish es aprox. 33 g; la más indulgente del rabino Jaim Naé, aprox. 27 g. "
        "(Nota: para alimentos porosos como el matzá, el peso requerido suele ser menor — unos 15–20 gramos según tu rav.) "
        "El tamaño exacto depende de la autoridad que sigas; pregunta a tu rav o usa una tabla comunitaria "
        "para el alimento o mitzvá específicos. Comer menos de un kezayit puede significar que la mitzvá no se cumplió."
    ),
    "kezayit — A kezayit is the olive-sized portion in Jewish law used to measure how much food you must consume for mitzvot — eating matzah at the Seder, maror, bentching after bread, and after-blessings. Because ancient volumes are difficult to calculate precisely, rabbis have established modern equivalents. Halachically, a kezayit is tied to the size of an egg — usually calculated as either half or one-third of an egg's volume depending on the authority. Many major organizations (such as Star-K) equate a kezayit to about 1.2 to 1.3 fluid ounces (approx. 35–40 ml), which visually translates to the size of a golf ball or a roll of quarters. By weight, this generally ranges between 25 and 33 grams — the stringent opinion of the Chazon Ish is approx. 33 g; the more lenient opinion of Rabbi Chayim Na'eh is approx. 27 g. (Note: For porous foods like matzah, the required weight is typically lower — around 15–20 grams depending on your rav.) The exact size depends on which authority you follow; ask your rav or use a community chart for the specific food or mitzvah you are measuring. Eating less than a kezayit may mean the mitzvah was not fulfilled.": (
        "kezayit — Un kezayit es la porción del tamaño de una aceituna en la ley judía, usada para medir cuánto alimento "
        "debes consumir para mitzvot — matzá en el Seder, maror, bentching después del pan y bendiciones posteriores. "
        "Los rabinos han establecido equivalentes modernos. Halájicamente se vincula al tamaño de un huevo — "
        "mitad o un tercio según la autoridad. Equivalencia habitual: unos 35–40 ml; en peso, 25–33 g "
        "(Jazón Ish ~33 g; Jaim Naé ~27 g). Para matzá poroso — unos 15–20 g. "
        "Pregunta a tu rav. Comer menos de un kezayit puede invalidar la mitzvá."
    ),
}

# French
FR: dict[str, str] = {
    "borei nefashot — Borei nefashot is the short after-blessing on many foods and drinks — fruit (aside from dates, grapes, olives, figs, and pomegranates which use the Me'ein Shalosh bracha), vegetables, drinks (other than wine/grape juice which use Me'ein Shalosh), meat, cheese, etc.": (
        "borei nefashot — Borei nefashot est la courte bénédiction après de nombreux aliments et boissons — "
        "fruits (sauf dattes, raisins, olives, figues et grenades, qui utilisent la bracha Me'ein Shalosh), "
        "légumes, boissons (sauf vin ou jus de raisin, qui utilisent Me'ein Shalosh), viande, fromage, etc."
    ),
}
