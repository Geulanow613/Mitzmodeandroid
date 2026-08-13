"""Add zman template strings + remaining UI strings to translation catalog."""
import json

NEW = {
    # Settings / onboarding gaps
    "Uses 1-day Yom Tov customs and the Israel parsha cycle. Select a city above or enable GPS for automatic detection.": {
        "he": "משתמש במנהגי יום טוב של יום אחד ובמחזור פרשות ישראל. בחרו עיר למעלה או הפעילו GPS לזיהוי אוטומטי.",
        "es": "Usa costumbres de Yom Tov de 1 día y el ciclo de parashot de Israel. Elija una ciudad arriba o active GPS para detección automática.",
        "fr": "Utilise les coutumes de Yom Tov d'un jour et le cycle de parachot d'Israël. Choisissez une ville ci-dessus ou activez le GPS.",
        "ru": "Использует однодневный Йом Тов и израильский цикл паршот. Выберите город выше или включите GPS.",
    },
    "Location (for prayer times)": {"he": "מיקום (לזמני תפילה)", "es": "Ubicación (horarios de oración)", "fr": "Emplacement (horaires de prière)", "ru": "Местоположение (время молитв)"},
    "Prayer times are off": {"he": "זמני תפילה כבויים", "es": "Horarios de oración desactivados", "fr": "Horaires de prière désactivés", "ru": "Время молитв отключено"},
    "To enable zmanim, set your location in Settings.": {
        "he": "כדי להפעיל זמנים, הגדירו מיקום בהגדרות.",
        "es": "Para activar zmanim, configure su ubicación en Ajustes.",
        "fr": "Pour activer les zmanim, définissez votre emplacement dans Réglages.",
        "ru": "Чтобы включить зманим, укажите местоположение в Настройках.",
    },
    "Choose city now (no GPS)": {"he": "בחרו עיר עכשיו (ללא GPS)", "es": "Elegir ciudad ahora (sin GPS)", "fr": "Choisir une ville (sans GPS)", "ru": "Выбрать город (без GPS)"},
    "My prayer tradition (Nusach)": {"he": "מסורת התפילה שלי (נוסח)", "es": "Mi tradición de oración (Nusaj)", "fr": "Ma tradition de prière (Nousa'h)", "ru": "Моя молитвенная традиция (нусах)"},
    "I am": {"he": "אני", "es": "Soy", "fr": "Je suis", "ru": "Я"},
    "Chabad — Nusach Ari": {"he": "חב״ד — נוסח האר״י", "es": "Jabad — Nusaj Ari", "fr": "Habad — Nousa'h Ari", "ru": "Хабад — нусах Ари"},
    "Used by Chabad-Lubavitch. Siddur: Tehillat Hashem. Not the same as Sephardi or Edot HaMizrach.": {
        "he": "בשימוש בחב״ד-לובביץ׳. סידור: תהילת ה׳. אינו זהה לספרדי או לעדות המזרח.",
        "es": "Usado por Jabad-Lubavitch. Sidur: Tehillat Hashem. No es lo mismo que sefardí o Edot HaMizrach.",
        "fr": "Utilisé par Habad-Loubavitch. Siddour : Tehillat Hashem. Différent du séfarade ou Edot HaMizrach.",
        "ru": "Используется в Хабад-Любавич. Сидур: Техилат Ашем. Не то же, что сефардский или Эдот а-Мизрах.",
    },
    "Your daily companion\nfor Torah-observant living": {
        "he": "הלווה היומי שלכם\nלחיים שומרי תורה",
        "es": "Su compañero diario\npara una vida observante de la Torá",
        "fr": "Votre compagnon quotidien\npour une vie observante de la Torah",
        "ru": "Ваш ежедневный спутник\nдля жизни по Торе",
    },

    # Zman — static hints
    "Tefillin are not worn on Shabbat or Festivals.": {
        "he": "אין מניחים תפילין בשבת וביום טוב.",
        "es": "No se usan tefilín en Shabat ni en festivos.",
        "fr": "On ne met pas les téfilines le Shabbat ni les fêtes.",
        "ru": "Тфилин не надевают в Шаббат и праздники.",
    },
    "Musaf is only on Shabbat, Festivals, and Rosh Chodesh — not a regular weekday.": {
        "he": "מוסף רק בשבת, ביום טוב ובראש חודש — לא ביום חול רגיל.",
        "es": "Musaf solo en Shabat, festivos y Rosh Jodesh — no en un día laborable.",
        "fr": "Moussaf seulement le Shabbat, les fêtes et Roch 'Hodech — pas un jour ordinaire.",
        "ru": "Мусаф только в Шаббат, праздники и Рош Ходеш — не в обычный будний день.",
    },
    "Set location in Settings for tzeit hakochavim times.": {
        "he": "הגדירו מיקום בהגדרות לזמני צאת הכוכבים.",
        "es": "Configure ubicación en Ajustes para horarios de tzeit hakojavim.",
        "fr": "Définissez l'emplacement dans Réglages pour les heures de tzeit hakokhavim.",
        "ru": "Укажите местоположение в Настройках для времени выхода звёзд.",
    },
    "Set location in Settings for dawn (alot hashachar) times.": {
        "he": "הגדירו מיקום בהגדרות לזמני עלות השחר.",
        "es": "Configure ubicación en Ajustes para horarios del alba (alot hashajar).",
        "fr": "Définissez l'emplacement pour les heures de l'aube (alot hashahar).",
        "ru": "Укажите местоположение для времени рассвета (алот а-шахар).",
    },
    "Break-fast meal — available after nightfall when Yom Kippur ends.": {
        "he": "ארוחת פרישת הצום — זמינה אחרי צאת הכוכבים בסיום יום כיפור.",
        "es": "Comida de ruptura del ayuno — disponible tras el anochecer al terminar Yom Kippur.",
        "fr": "Repas de rupture du jeûne — disponible après la tombée de la nuit à la fin de Yom Kippour.",
        "ru": "Приём пищи после поста — после выхода звёзд в конце Йом Кипура.",
    },
    "Set location for Hebrew date and timing this month.": {
        "he": "הגדירו מיקום לתאריך העברי ולזמנים החודש.",
        "es": "Configure ubicación para la fecha hebrea y horarios este mes.",
        "fr": "Définissez l'emplacement pour la date hébraïque et le calendrier ce mois-ci.",
        "ru": "Укажите местоположение для еврейской даты и времени в этом месяце.",
    },
    "Window closed for this month — reopens after the next new moon.": {
        "he": "החלון נסגר החודש — נפתח שוב אחרי ראש חודש הבא.",
        "es": "Ventana cerrada este mes — se reabre tras la próxima luna nueva.",
        "fr": "Fenêtre fermée ce mois-ci — rouvre après la prochaine nouvelle lune.",
        "ru": "Окно закрыто в этом месяце — откроется после следующего новолуния.",
    },
    "Last chance may be tonight — say if the moon is clear and still within Sof Zman.": {
        "he": "אולי ההזדמנות האחרונה הלילה — אמרו אם הירח ברור ועדיין בתוך סוף זמן.",
        "es": "Última oportunidad puede ser esta noche — diga si la luna está clara y aún dentro del Sof Zman.",
        "fr": "Dernière chance peut-être ce soir — dites si la lune est claire et encore dans le Sof Zman.",
        "ru": "Последний шанс может быть сегодня ночью — если луна видна и ещё в пределах соф зман.",
    },
    "Eat a proper meal after Maariv and Havdalah.": {
        "he": "אכלו ארוחה כראוי אחרי מעריב והבדלה.",
        "es": "Coma una comida adecuada después de Maariv y Havdalá.",
        "fr": "Prenez un repas convenable après Maariv et Havdalah.",
        "ru": "Поешьте полноценно после Маарива и Авдаллы.",
    },
    "Recite Birkat Hachamah outdoors at sunrise while viewing the sun.": {
        "he": "אמרו ברכת החמה בחוץ בזריחה תוך כדי ראיית השמש.",
        "es": "Recite Birkat Hachamah al aire libre al amanecer viendo el sol.",
        "fr": "Récitez Birkat Hachamah dehors au lever du soleil en regardant le soleil.",
        "ru": "Читайте Биркат а-Хаму на улице на восходе, глядя на солнце.",
    },
    " On Shabbat and Festivals, fulfill this without using your phone.": {
        "he": " בשבת וביום טוב — קיימו זאת בלי להשתמש בטלפון.",
        "es": " En Shabat y festivos, cúmplalo sin usar el teléfono.",
        "fr": " Le Shabbat et les fêtes, accomplissez ceci sans utiliser votre téléphone.",
        "ru": " В Шаббат и праздники выполняйте это без телефона.",
    },
    "It is a mitzvah to have a festive meal during the day in honor of Rosh Chodesh.": {
        "he": "מצווה לאכול סעודה חגיגית ביום לכבוד ראש חודש.",
        "es": "Es mitzvá tener una comida festiva de día en honor de Rosh Jodesh.",
        "fr": "C'est une mitzvah de prendre un repas festif le jour en l'honneur de Roch 'Hodech.",
        "ru": "Мицва — праздничная трапеза днём в честь Рош Ходеш.",
    },

    # Zman — templates
    "Available after halachic midnight (chatzos halayla) — {time}. Most say {label} when they wake, even after a nap.": {
        "he": "זמין אחרי חצות הלילה (חצות הלילה) — {time}. רוב האנשים אומרים {label} כשהם מתעוררים, גם אחרי תנומה.",
        "es": "Disponible tras la medianoche halájica (chatzot halaila) — {time}. La mayoría dice {label} al despertar, incluso tras una siesta.",
        "fr": "Disponible après minuit halakhique (chatzot halaila) — {time}. La plupart disent {label} au réveil, même après une sieste.",
        "ru": "Доступно после полуночи (хацот халаила) — {time}. Большинство читает {label} при пробуждении, даже после дневного сна.",
    },
    "{label} can be said any time during the day.": {
        "he": "ניתן לאמר {label} בכל שעה במהלך היום.",
        "es": "{label} puede decirse en cualquier momento del día.",
        "fr": "{label} peut être dit à tout moment de la journée.",
        "ru": "{label} можно читать в любое время дня.",
    },
    "{label} is part of the morning Shacharit service, from dawn ({time}).": {
        "he": "{label} הוא חלק מתפילת שחרית בבוקר, משחר ({time}).",
        "es": "{label} forma parte del Shajarit matutino, desde el alba ({time}).",
        "fr": "{label} fait partie du Shacharit du matin, dès l'aube ({time}).",
        "ru": "{label} — часть утреннего Шахарита, с рассвета ({time}).",
    },
    "Morning Shema opens at dawn ({time}).": {
        "he": "קריאת שמע של שחר פותחת בשחר ({time}).",
        "es": "El Shemá matutino abre al alba ({time}).",
        "fr": "Le Shema du matin commence à l'aube ({time}).",
        "ru": "Утреннее Шма начинается на рассвете ({time}).",
    },
    "{label} is available from dawn ({startTime}). Ideal at sunrise ({sunriseTime}) or later.": {
        "he": "{label} זמין משחר ({startTime}). מומלץ בזריחה ({sunriseTime}) או אחר כך.",
        "es": "{label} disponible desde el alba ({startTime}). Ideal al amanecer ({sunriseTime}) o después.",
        "fr": "{label} disponible dès l'aube ({startTime}). Idéal au lever ({sunriseTime}) ou après.",
        "ru": "{label} доступен с рассвета ({startTime}). Лучше на восходе ({sunriseTime}) или позже.",
    },
    "{label} is available from dawn ({startTime}). Ideal from sunrise or later.": {
        "he": "{label} זמין משחר ({startTime}). מומלץ מזריחה ואילך.",
        "es": "{label} disponible desde el alba ({startTime}). Ideal desde el amanecer.",
        "fr": "{label} disponible dès l'aube ({startTime}). Idéal dès le lever du soleil.",
        "ru": "{label} доступен с рассвета ({startTime}). Лучше с восхода солнца.",
    },
    "Recite brachos on tallit and tefillin at misheyakir ({time}) — ideally during Shacharit. See explainer if you must leave earlier.": {
        "he": "אמרו ברכות על טלית ותפילין במישעכיר ({time}) — במיטב במהלך שחרית. ראו הסבר אם אתם חייבים לצאת מוקדם.",
        "es": "Recite las bendiciones del talit y tefilín en misheyakir ({time}) — idealmente en Shajarit. Vea la explicación si debe salir antes.",
        "fr": "Récitez les bénédictions du tallit et des téfilines à misheyakir ({time}) — idéalement au Shacharit. Voir l'explication si vous devez partir plus tôt.",
        "ru": "Читайте благословения на талит и тфилин в мишейакир ({time}) — лучше во время Шахарита. См. пояснение, если нужно уйти раньше.",
    },
    "Musaf ({label}) — after the morning Amidah, from {time}.": {
        "he": "מוסף ({label}) — אחרי עמידת שחרית, מ-{time}.",
        "es": "Musaf ({label}) — después del Amidá matutino, desde {time}.",
        "fr": "Moussaf ({label}) — après l'Amidah du matin, à partir de {time}.",
        "ru": "Мусаф ({label}) — после утренней Амиды, с {time}.",
    },
    "Musaf window has closed (sunset).{shabbatNote}": {
        "he": "חלון המוסף נסגר (שקיעה).{shabbatNote}",
        "es": "La ventana de Musaf cerró (atardecer).{shabbatNote}",
        "fr": "La fenêtre du Moussaf est fermée (coucher du soleil).{shabbatNote}",
        "ru": "Окно Мусафа закрыто (закат).{shabbatNote}",
    },
    "The time to fulfill Musaf has passed ({time} — end of the 7th halachic hour). You can still fulfill it until sunset — if Mincha time has arrived, pray Mincha first (SA OC 286:4).{shabbatNote}": {
        "he": "זמן קיום מוסף עבר ({time} — סוף השעה השביעית). עדיין אפשר לקיים עד השקיעה — אם הגיע זמן מנחה, התפללו מנחה קודם (שו״ע או״ח רפו:ד).{shabbatNote}",
        "es": "El tiempo de Musaf pasó ({time} — fin de la 7.ª hora halájica). Aún puede cumplirlo hasta el atardecer — si llegó Minjá, rece primero Minjá (SA OC 286:4).{shabbatNote}",
        "fr": "Le délai du Moussaf est passé ({time} — fin de la 7e heure halakhique). Vous pouvez encore l'accomplir jusqu'au coucher — si l'heure de Min'ha est arrivée, priez Min'ha d'abord (SA OC 286:4).{shabbatNote}",
        "ru": "Время Мусафа прошло ({time} — конец 7-го часа). Ещё можно до заката — если наступила Минха, сначала Минха (ША ОХ 286:4).{shabbatNote}",
    },
    "{label} appears {time} — Mincha Gedolah.": {
        "he": "{label} מופיע {time} — מנחה גדולה.",
        "es": "{label} aparece {time} — Minjá Gedolá.",
        "fr": "{label} apparaît {time} — Min'ha Gedola.",
        "ru": "{label} появляется {time} — Минха Гедола.",
    },
    "Today's {label} window ended {time}.": {
        "he": "חלון {label} של היום נסגר {time}.",
        "es": "La ventana de {label} de hoy terminó {time}.",
        "fr": "La fenêtre de {label} d'aujourd'hui s'est terminée {time}.",
        "ru": "Окно {label} сегодня закончилось {time}.",
    },
    "Evening Shema — biblical obligation {time}.": {
        "he": "קריאת שמע בערב — חובה מן התורה {time}.",
        "es": "Shemá vespertino — obligación bíblica {time}.",
        "fr": "Shema du soir — obligation biblique {time}.",
        "ru": "Вечернее Шма — библейская обязанность {time}.",
    },
    "Evening Shema — biblical obligation {time}. Maariv may begin at sunset, but biblical evening Shema begins {tzeitTime}. If you daven Maariv early, repeat Shema then.": {
        "he": "קריאת שמע בערב — חובה מן התורה {time}. מעריב יכול להתחיל בשקיעה, אך שמע ערבית מן התורה מתחיל {tzeitTime}. אם התפללתם מעריב מוקדם, חזרו על שמע אז.",
        "es": "Shemá vespertino — obligación bíblica {time}. Maariv puede empezar al atardecer, pero el Shemá bíblico vespertino comienza {tzeitTime}. Si reza Maariv temprano, repita el Shemá entonces.",
        "fr": "Shema du soir — obligation biblique {time}. Maariv peut commencer au coucher, mais le Shema biblique du soir commence {tzeitTime}. Si vous priez Maariv tôt, répétez le Shema alors.",
        "ru": "Вечернее Шма — библейская обязанность {time}. Маарив может начаться на закате, но библейское вечернее Шма — {tzeitTime}. Если молитесь Маарив рано, повторите Шма тогда.",
    },
    "Yaaleh V'yavo at Maariv — available {time}.": {
        "he": "יעלה ויבוא במעריב — זמין {time}.",
        "es": "Yaaleh V'yavo en Maariv — disponible {time}.",
        "fr": "Yaaleh V'yavo à Maariv — disponible {time}.",
        "ru": "Яале ве-Яво на Маариве — доступно {time}.",
    },
    "Yaaleh V'yavo at Maariv — available {time}. Many daven Maariv ideally {tzeitTime}.": {
        "he": "יעלה ויבוא במעריב — זמין {time}. רבים מתפללים מעריב במיטב {tzeitTime}.",
        "es": "Yaaleh V'yavo en Maariv — disponible {time}. Muchos rezan Maariv idealmente {tzeitTime}.",
        "fr": "Yaaleh V'yavo à Maariv — disponible {time}. Beaucoup prient Maariv idéalement {tzeitTime}.",
        "ru": "Яале ве-Яво на Маариве — {time}. Многие молят Маарив лучше {tzeitTime}.",
    },
    "Maariv — available {time}.": {
        "he": "מעריב — זמין {time}.",
        "es": "Maariv — disponible {time}.",
        "fr": "Maariv — disponible {time}.",
        "ru": "Маарив — доступен {time}.",
    },
    "Maariv — available {time}. Many daven Maariv ideally {tzeitTime} — ask your rabbi.": {
        "he": "מעריב — זמין {time}. רבים מתפללים מעריב במיטב {tzeitTime} — שאלו את הרב.",
        "es": "Maariv — disponible {time}. Muchos rezan Maariv idealmente {tzeitTime} — consulte a su rabino.",
        "fr": "Maariv — disponible {time}. Beaucoup prient Maariv idéalement {tzeitTime} — demandez à votre rabbin.",
        "ru": "Маарив — {time}. Многие молят лучше {tzeitTime} — спросите раввина.",
    },
    "Melave Malka — after Havdalah, from {time} through dawn.": {
        "he": "מלווה מלכה — אחרי הבדלה, מ-{time} עד השחר.",
        "es": "Melave Malka — después de Havdalá, desde {time} hasta el alba.",
        "fr": "Melave Malka — après Havdalah, de {time} jusqu'à l'aube.",
        "ru": "Мелаве Малка — после Авдаллы, с {time} до рассвета.",
    },
    "{label} — when you are ready for sleep ({time}).": {
        "he": "{label} — כשאתם מוכנים לשינה ({time}).",
        "es": "{label} — cuando esté listo para dormir ({time}).",
        "fr": "{label} — quand vous êtes prêt à dormir ({time}).",
        "ru": "{label} — когда готовы ко сну ({time}).",
    },
    "Ideal bedtime Shema time is before dawn ({time}).": {
        "he": "זמן שמע על המיטה האידיאלי הוא לפני השחר ({time}).",
        "es": "El momento ideal del Shemá al acostarse es antes del alba ({time}).",
        "fr": "L'heure idéale du Shema au coucher est avant l'aube ({time}).",
        "ru": "Идеальное время Шма перед сном — до рассвета ({time}).",
    },
    "Rosh Chodesh observances — from dawn ({time}) through nightfall tonight.": {
        "he": "מנהגי ראש חודש — משחר ({time}) עד צאת הכוכבים הלילה.",
        "es": "Observancias de Rosh Jodesh — desde el alba ({time}) hasta el anochecer esta noche.",
        "fr": "Observances de Roch 'Hodech — dès l'aube ({time}) jusqu'à la tombée de la nuit.",
        "ru": "Обряды Рош Ходеш — с рассвета ({time}) до выхода звёзд сегодня ночью.",
    },
    "{subtitle} — fast begins at sunset.": {
        "he": "{subtitle} — הצום מתחיל בשקיעה.",
        "es": "{subtitle} — el ayuno comienza al atardecer.",
        "fr": "{subtitle} — le jeûne commence au coucher du soleil.",
        "ru": "{subtitle} — пост начинается на закате.",
    },
    "{subtitle} — begins at dawn ({time}).": {
        "he": "{subtitle} — מתחיל בשחר ({time}).",
        "es": "{subtitle} — comienza al alba ({time}).",
        "fr": "{subtitle} — commence à l'aube ({time}).",
        "ru": "{subtitle} — начинается на рассвете ({time}).",
    },
    "Motzei Yom Kippur meal — after nightfall ({time}).": {
        "he": "ארוחת מוצאי יום כיפור — אחרי צאת הכוכבים ({time}).",
        "es": "Comida de Motzei Yom Kippur — tras el anochecer ({time}).",
        "fr": "Repas de Motzei Yom Kippour — après la tombée de la nuit ({time}).",
        "ru": "Трапеза моцэ йом-кипура — после выхода звёзд ({time}).",
    },
    "Today — Birkat Hachamah at sunrise ({date}). Recite outdoors while viewing the sun.": {
        "he": "היום — ברכת החמה בזריחה ({date}). אמרו בחוץ תוך כדי ראיית השמש.",
        "es": "Hoy — Birkat Hachamah al amanecer ({date}). Recite al aire libre viendo el sol.",
        "fr": "Aujourd'hui — Birkat Hachamah au lever ({date}). Récitez dehors en regardant le soleil.",
        "ru": "Сегодня — Биркат а-Хама на восходе ({date}). Читайте на улице, глядя на солнце.",
    },
    "The time to fulfill Birkat Hachamah has passed ({time}). According to some opinions, you may still recite it until chatzos (halachic midday).": {
        "he": "זמן ברכת החמה עבר ({time}). לפי חלק מהדעות, עדיין אפשר לאמר עד חצות.",
        "es": "El tiempo de Birkat Hachamah pasó ({time}). Según algunas opiniones, aún puede recitarlo hasta el mediodía halájico.",
        "fr": "Le délai de Birkat Hachamah est passé ({time}). Selon certaines opinions, vous pouvez encore le réciter jusqu'à midi halakhique.",
        "ru": "Время Биркат а-Хамы прошло ({time}). По некоторым мнениям, ещё можно до полудня.",
    },
    "Birkat Hachamah tomorrow — {date}. Plan to recite outdoors at sunrise.": {
        "he": "ברכת החמה מחר — {date}. תכננו לאמר בחוץ בזריחה.",
        "es": "Birkat Hachamah mañana — {date}. Planee recitar al aire libre al amanecer.",
        "fr": "Birkat Hachamah demain — {date}. Prévoyez de réciter dehors au lever du soleil.",
        "ru": "Биркат а-Хама завтра — {date}. Планируйте читать на улице на восходе.",
    },
    "Birkat Hachamah in {count} days — {date}. Plan to recite outdoors at sunrise.": {
        "he": "ברכת החמה בעוד {count} ימים — {date}. תכננו לאמר בחוץ בזריחה.",
        "es": "Birkat Hachamah en {count} días — {date}. Planee recitar al aire libre al amanecer.",
        "fr": "Birkat Hachamah dans {count} jours — {date}. Prévoyez de réciter dehors au lever.",
        "ru": "Биркат а-Хама через {count} дн. — {date}. Планируйте читать на улице на восходе.",
    },
    "Opens around {day} this month.": {
        "he": "נפתח בסביבות {day} החודש.",
        "es": "Abre alrededor del {day} de este mes.",
        "fr": "Ouvre vers le {day} de ce mois.",
        "ru": "Открывается около {day} этого месяца.",
    },
    "Last chance this month — {timing}": {
        "he": "הזדמנות אחרונה החודש — {timing}",
        "es": "Última oportunidad este mes — {timing}",
        "fr": "Dernière chance ce mois-ci — {timing}",
        "ru": "Последний шанс в этом месяце — {timing}",
    },
    "{timing} Window closes at the full moon.": {
        "he": "{timing} החלון נסגר בירח המלא.",
        "es": "{timing} La ventana cierra en la luna llena.",
        "fr": "{timing} La fenêtre se ferme à la pleine lune.",
        "ru": "{timing} Окно закрывается в полнолуние.",
    },
    "Many wait until after Tisha B'Av; Motzei Shabbat sooner if the moon is clear.": {
        "he": "רבים ממתינים עד אחרי תשעה באב; מוצאי שבת מוקדם יותר אם הירח ברור.",
        "es": "Muchos esperan hasta después de Tisha B'Av; Motzei Shabat antes si la luna está clara.",
        "fr": "Beaucoup attendent après Tisha B'Av ; Motzei Shabbat plus tôt si la lune est claire.",
        "ru": "Многие ждут после Тиша бе-Ава; моцэ шабата раньше, если луна видна.",
    },
    "Many wait until after Yom Kippur; Motzei Shabbat sooner if the moon is clear.": {
        "he": "רבים ממתינים עד אחרי יום כיפור; מוצאי שבת מוקדם יותר אם הירח ברור.",
        "es": "Muchos esperan hasta después de Yom Kippur; Motzei Shabat antes si la luna está clara.",
        "fr": "Beaucoup attendent après Yom Kippour ; Motzei Shabbat plus tôt si la lune est claire.",
        "ru": "Многие ждут после Йом Кипура; моцэ шабата раньше, если луна видна.",
    },
    "Tonight after Shabbat — ideally still in Shabbat clothes.": {
        "he": "הלילה אחרי שבת — במיטב עדיין בבגדי שבת.",
        "es": "Esta noche después de Shabat — idealmente aún con ropa de Shabat.",
        "fr": "Ce soir après le Shabbat — idéalement encore en habits de Shabbat.",
        "ru": "Сегодня после Шаббата — лучше ещё в шаббатней одежде.",
    },
    "Ideally Motzei Shabbat in nice clothes; say when the moon is clear.": {
        "he": "במיטב במוצאי שבת בבגדים נאים; אמרו כשהירח ברור.",
        "es": "Idealmente Motzei Shabat con ropa elegante; diga cuando la luna esté clara.",
        "fr": "Idéalement Motzei Shabbat en beaux habits ; dites quand la lune est claire.",
        "ru": "Лучше на моцэ шабата в нарядной одежде; читайте, когда луна видна.",
    },
    "the 3rd": {"he": "ג׳", "es": "el 3.º", "fr": "le 3e", "ru": "3-го"},
    "the 7th": {"he": "ז׳", "es": "el 7.º", "fr": "le 7e", "ru": "7-го"},
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

strings_data["strings"] = sorted(strings_set)
with open(strings_path, "w", encoding="utf-8") as f:
    json.dump(strings_data, f, ensure_ascii=False, indent=2)
    f.write("\n")

with open(overrides_path, "w", encoding="utf-8") as f:
    json.dump(overrides, f, ensure_ascii=False, indent=2)
    f.write("\n")

print(f"Added {added} new keys, total {len(strings_set)}")
