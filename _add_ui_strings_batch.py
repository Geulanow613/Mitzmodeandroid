"""Add Settings/UI/zman template strings to translation catalog."""
import json

NEW = {
    # Text size presets
    "Small": {"he": "קטן", "es": "Pequeño", "fr": "Petit", "ru": "Мелкий"},
    "Normal": {"he": "רגיל", "es": "Normal", "fr": "Normal", "ru": "Обычный"},
    "Large": {"he": "גדול", "es": "Grande", "fr": "Grand", "ru": "Крупный"},
    "XL": {"he": "XL", "es": "XL", "fr": "XL", "ru": "XL"},
    "Huge": {"he": "ענק", "es": "Enorme", "fr": "Très grand", "ru": "Огромный"},

    # Settings — concatenated strings (must match Kotlin exactly)
    "After eating meat, you wait before eating dairy, and vice versa. The wait time is a matter of your family tradition — ask your rabbi if unsure.": {
        "he": "אחרי אכילת בשר ממתינים לפני אכילת חלב, ולהפך. זמן ההמתנה הוא עניין של מסורת המשפחה — שאלו את הרב אם אינכם בטוחים.",
        "es": "Después de comer carne se espera antes de comer lácteos, y viceversa. El tiempo de espera depende de la tradición familiar — consulte a su rabino si tiene dudas.",
        "fr": "Après la viande, on attend avant les produits laitiers, et inversement. Le délai dépend de la tradition familiale — demandez à votre rabbin en cas de doute.",
        "ru": "После мяса ждут перед молочным, и наоборот. Время ожидания — вопрос семейной традиции; при сомнении спросите раввина.",
    },
    "Prayer times (zmanim) — Shema deadline, Mincha window, candle lighting — are calculated from your location. Your location is stored on this device only.": {
        "he": "זמני תפילה (זמנים) — סוף זמן קריאת שמע, חלון מנחה, הדלקת נרות — מחושבים לפי מיקומכם. המיקום נשמר רק במכשיר זה.",
        "es": "Los horarios de oración (zmanim) — plazo del Shemá, ventana de Minjá, encendido de velas — se calculan según su ubicación. La ubicación se guarda solo en este dispositivo.",
        "fr": "Les horaires de prière (zmanim) — heure limite du Shema, fenêtre de Min'ha, allumage des bougies — sont calculés selon votre emplacement. Votre position n'est stockée que sur cet appareil.",
        "ru": "Время молитв (зманим) — крайний срок Шма, окно Минхи, зажигание свечей — рассчитывается по вашему местоположению. Оно хранится только на этом устройстве.",
    },
    "Currently using: {nusach}": {
        "he": "בשימוש כרגע: {nusach}",
        "es": "Usando actualmente: {nusach}",
        "fr": "Actuellement utilisé : {nusach}",
        "ru": "Сейчас используется: {nusach}",
    },
    "🇮🇱 Israel customs active — detected from your GPS location (1-day Yom Tov, Israel parsha cycle).": {
        "he": "🇮🇱 מנהגי ישראל פעילים — זוהה לפי מיקום ה-GPS (יום טוב יום אחד, מחזור פרשות ישראל).",
        "es": "🇮🇱 Costumbres de Israel activas — detectadas por GPS (Yom Tov de 1 día, ciclo de parashot de Israel).",
        "fr": "🇮🇱 Coutumes d'Israël actives — détectées via GPS (Yom Tov d'un jour, cycle de parachot d'Israël).",
        "ru": "🇮🇱 Обычаи Израиля активны — определено по GPS (однодневный Йом Тов, израильский цикл паршот).",
    },
    "🇮🇱 Israel customs active — detected from your city selection (1-day Yom Tov, Israel parsha cycle).": {
        "he": "🇮🇱 מנהגי ישראל פעילים — זוהה לפי בחירת העיר (יום טוב יום אחד, מחזור פרשות ישראל).",
        "es": "🇮🇱 Costumbres de Israel activas — detectadas por la ciudad elegida (Yom Tov de 1 día, ciclo de parashot de Israel).",
        "fr": "🇮🇱 Coutumes d'Israël actives — détectées via la ville choisie (Yom Tov d'un jour, cycle de parachot d'Israël).",
        "ru": "🇮🇱 Обычаи Израиля активны — по выбранному городу (однодневный Йом Тов, израильский цикл паршот).",
    },
    "Current location: {place} (GPS)": {
        "he": "מיקום נוכחי: {place} (GPS)",
        "es": "Ubicación actual: {place} (GPS)",
        "fr": "Emplacement actuel : {place} (GPS)",
        "ru": "Текущее местоположение: {place} (GPS)",
    },
    "Current location: {place} (city)": {
        "he": "מיקום נוכחי: {place} (עיר)",
        "es": "Ubicación actual: {place} (ciudad)",
        "fr": "Emplacement actuel : {place} (ville)",
        "ru": "Текущее местоположение: {place} (город)",
    },
    "Current location: waiting for GPS…": {
        "he": "מיקום נוכחי: ממתין ל-GPS…",
        "es": "Ubicación actual: esperando GPS…",
        "fr": "Emplacement actuel : en attente du GPS…",
        "ru": "Текущее местоположение: ожидание GPS…",
    },
    "Current location: {place}": {
        "he": "מיקום נוכחי: {place}",
        "es": "Ubicación actual: {place}",
        "fr": "Emplacement actuel : {place}",
        "ru": "Текущее местоположение: {place}",
    },
    "No location set — turn on GPS or choose a city below": {
        "he": "לא הוגדר מיקום — הפעילו GPS או בחרו עיר למטה",
        "es": "Sin ubicación — active GPS o elija una ciudad abajo",
        "fr": "Aucun emplacement — activez le GPS ou choisissez une ville ci-dessous",
        "ru": "Местоположение не задано — включите GPS или выберите город ниже",
    },

    # Wait time unit labels
    "30 min": {"he": "30 דק׳", "es": "30 min", "fr": "30 min", "ru": "30 мин"},
    "1h": {"he": "שעה", "es": "1 h", "fr": "1 h", "ru": "1 ч"},

    # Language settings (from prior work)
    "Language": {"he": "שפה", "es": "Idioma", "fr": "Langue", "ru": "Язык"},
    "Offline languages": {"he": "שפות לא מקוונות", "es": "Idiomas sin conexión", "fr": "Langues hors ligne", "ru": "Офлайн-языки"},
    "No internet required": {"he": "ללא צורך באינטרנט", "es": "Sin internet", "fr": "Sans Internet", "ru": "Интернет не нужен"},
    "All languages (online) →": {"he": "כל השפות (מקוון) →", "es": "Todos los idiomas (en línea) →", "fr": "Toutes les langues (en ligne) →", "ru": "Все языки (онлайн) →"},
    "Online languages use Google Translate. Quality varies.": {
        "he": "שפות מקוונות משתמשות ב-Google Translate. האיכות משתנה.",
        "es": "Los idiomas en línea usan Google Translate. La calidad varía.",
        "fr": "Les langues en ligne utilisent Google Translate. La qualité varie.",
        "ru": "Онлайн-языки используют Google Translate. Качество различается.",
    },

    # Zman templates
    "The time to fulfill {label} has passed ({time}). You can still pray until midday.": {
        "he": "זמן קיום {label} עבר ({time}). עדיין אפשר להתפלל עד חצות.",
        "es": "El tiempo para cumplir {label} pasó ({time}). Aún puede rezar hasta el mediodía.",
        "fr": "Le délai pour {label} est passé ({time}). Vous pouvez encore prier jusqu'à midi.",
        "ru": "Время для {label} прошло ({time}). Ещё можно молиться до полудня.",
    },
    "The time to fulfill the mitzvah of Shema has passed ({time}). Still say Shema with its blessings during Shacharit until midday — even if you missed it, say it during prayer.": {
        "he": "זמן מצוות קריאת שמע עבר ({time}). עדיין אמרו שמע עם ברכותיה בשחרית עד חצות — גם אם פספסתם, אמרו בתפילה.",
        "es": "El tiempo de la mitzvá del Shemá pasó ({time}). Diga aún el Shemá con sus bendiciones en Shajarit hasta el mediodía — aunque lo haya perdido, dígalo en la oración.",
        "fr": "Le délai de la mitzvah du Shema est passé ({time}). Récitez encore le Shema avec ses bénédictions au Shacharit jusqu'à midi — même si vous l'avez manqué, dites-le pendant la prière.",
        "ru": "Время мitzvah чтения Шма прошло ({time}). Всё равно читайте Шма с благословениями на Шахарите до полудня — даже если пропустили, скажите в молитве.",
    },
    "The time to fulfill Shacharit has passed ({time}). You can still daven until midday.": {
        "he": "זמן קיום שחרית עבר ({time}). עדיין אפשר להתפלל עד חצות.",
        "es": "El tiempo de Shajarit pasó ({time}). Aún puede rezar hasta el mediodía.",
        "fr": "Le délai du Shacharit est passé ({time}). Vous pouvez encore prier jusqu'à midi.",
        "ru": "Время Шахарита прошло ({time}). Ещё можно молиться до полудня.",
    },
    "The time to fulfill the mitzvah of tefillin has passed. Tefillin are still valid to wear until sunset ({time}).": {
        "he": "זמן מצוות התפילין עבר. עדיין מותר להניח תפילין עד השקיעה ({time}).",
        "es": "El tiempo de la mitzvá de tefilín pasó. Aún puede usar tefilín hasta la puesta del sol ({time}).",
        "fr": "Le délai de la mitzvah des téfilines est passé. On peut encore les porter jusqu'au coucher du soleil ({time}).",
        "ru": "Время мitzvah тфилин прошло. Их ещё можно надевать до заката ({time}).",
    },
    "Available from dawn. Ideal at sunrise ({time}) or later — if you're in a hurry you may say it now.": {
        "he": "זמין משחר. מומלץ בזריחה ({time}) או אחר כך — אם אתם ממהרים אפשר עכשיו.",
        "es": "Disponible desde el alba. Ideal al amanecer ({time}) o después — si tiene prisa puede decirlo ahora.",
        "fr": "Disponible dès l'aube. Idéal au lever du soleil ({time}) ou après — si vous êtes pressé, vous pouvez le dire maintenant.",
        "ru": "Доступно с рассвета. Лучше на восходе ({time}) или позже — если спешите, можно сейчас.",
    },
    "Available in {countdown}{at}": {
        "he": "זמין בעוד {countdown}{at}",
        "es": "Disponible en {countdown}{at}",
        "fr": "Disponible dans {countdown}{at}",
        "ru": "Доступно через {countdown}{at}",
    },
    " · at dawn": {"he": " · בזריחה", "es": " · al alba", "fr": " · à l'aube", "ru": " · на рассвете"},
    " · at midday": {"he": " · בחצות", "es": " · al mediodía", "fr": " · à midi", "ru": " · в полдень"},
    " · at sunset": {"he": " · בשקיעה", "es": " · al atardecer", "fr": " · au coucher du soleil", "ru": " · на закате"},
    " · at nightfall": {"he": " · בצאת הכוכבים", "es": " · al anochecer", "fr": " · à la tombée de la nuit", "ru": " · с наступлением ночи"},
    " · at halachic midnight": {"he": " · בחצות הלילה", "es": " · a medianoche halájica", "fr": " · à minuit halakhique", "ru": " · в полночь (халахическую)"},
    " · at misheyakir": {"he": " · במישעכיר", "es": " · en misheyakir", "fr": " · à misheyakir", "ru": " · в мишейакир"},
    " · at Mincha Gedola": {"he": " · במנחה גדולה", "es": " · en Minjá Gedolá", "fr": " · à Min'ha Gedola", "ru": " · в Минхе Гедола"},
    "Available during the day · tap to read": {"he": "זמין ביום · הקישו לפרטים", "es": "Disponible de día · toque para leer", "fr": "Disponible le jour · appuyez pour lire", "ru": "Доступно днём · нажмите для подробностей"},
    "Only available during the day · tap to read": {"he": "זמין רק ביום · הקישו לפרטים", "es": "Solo de día · toque para leer", "fr": "Uniquement le jour · appuyez pour lire", "ru": "Только днём · нажмите для подробностей"},
    "Available from midday on · tap to read": {"he": "זמין מחצות · הקישו לפרטים", "es": "Disponible desde el mediodía · toque para leer", "fr": "Disponible dès midi · appuyez pour lire", "ru": "Доступно с полудня · нажмите"},
    "Only available from midday on · tap to read": {"he": "זמין רק מחצות · הקישו לפרטים", "es": "Solo desde el mediodía · toque para leer", "fr": "Uniquement dès midi · appuyez pour lire", "ru": "Только с полудня · нажмите"},
    "Available after sunset · tap to read": {"he": "זמין אחרי השקיעה · הקישו לפרטים", "es": "Disponible tras el atardecer · toque para leer", "fr": "Disponible après le coucher · appuyez pour lire", "ru": "Доступно после заката · нажмите"},
    "Only available after sunset · tap to read": {"he": "זמין רק אחרי השקיעה · הקישו לפרטים", "es": "Solo tras el atardecer · toque para leer", "fr": "Uniquement après le coucher · appuyez pour lire", "ru": "Только после заката · нажмите"},
    "Available at night · tap to read": {"he": "זמין בלילה · הקישו לפרטים", "es": "Disponible de noche · toque para leer", "fr": "Disponible la nuit · appuyez pour lire", "ru": "Доступно ночью · нажмите"},
    "Only available at night · tap to read": {"he": "זמין רק בלילה · הקישו לפרטים", "es": "Solo de noche · toque para leer", "fr": "Uniquement la nuit · appuyez pour lire", "ru": "Только ночью · нажмите"},
    "Coming up later · tap to read": {"he": "מגיע בהמשך · הקישו לפרטים", "es": "Próximamente · toque para leer", "fr": "Bientôt · appuyez pour lire", "ru": "Скоро · нажмите"},
    "Tap to read when it's time": {"he": "הקישו לפרטים כשיגיע הזמן", "es": "Toque para leer cuando sea el momento", "fr": "Appuyez pour lire au bon moment", "ru": "Нажмите, когда придёт время"},
    " — Parshat {parsha}": {
        "he": " — פרשת {parsha}",
        "es": " — Parashat {parsha}",
        "fr": " — Parachat {parsha}",
        "ru": " — Парашат {parsha}",
    },
}

# Add hour labels 2h-12h
for h in range(2, 13):
    key = f"{h}h"
    NEW[key] = {
        "he": f"{h} שע׳",
        "es": f"{h} h",
        "fr": f"{h} h",
        "ru": f"{h} ч",
    }

strings_path = "be-a-tzaddik/data/translation-catalog/strings.json"
overrides_path = "be-a-tzaddik/data/translation-catalog/human/quality_overrides.json"

with open(strings_path, encoding="utf-8") as f:
    strings_data = json.load(f)
strings_list = strings_data["strings"]
strings_set = set(strings_list)

with open(overrides_path, encoding="utf-8") as f:
    overrides = json.load(f)

added = 0
for en, langs in NEW.items():
    if en not in strings_set:
        strings_list.append(en)
        strings_set.add(en)
        added += 1
    for lang, tr in langs.items():
        overrides.setdefault(lang, {})[en] = tr

strings_data["count"] = len(strings_list)
with open(strings_path, "w", encoding="utf-8") as f:
    json.dump(strings_data, f, ensure_ascii=False, indent=2)
with open(overrides_path, "w", encoding="utf-8") as f:
    json.dump(overrides, f, ensure_ascii=False, indent=2)

print(f"Added {added} new keys, total {len(strings_list)}")
