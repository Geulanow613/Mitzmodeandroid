#!/usr/bin/env python3
"""Apply pattern-based quality fixes; write quality_fixes.json for compile override."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
COMPOSE = ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "translations"
PLACEHOLDER_FIXES = ROOT / "data" / "translation-catalog" / "placeholder_fixes.json"
MANUAL = ROOT / "data" / "translation-catalog" / "shards" / "manual_fixes.json"
OUT = ROOT / "data" / "translation-catalog" / "shards" / "quality_fixes.json"
LANGS = ("he", "es", "fr", "ru")

PLACEHOLDER_RE = [
    re.compile(r"\$\{[^}]+\}"),
    re.compile(r"\$[A-Za-z_][A-Za-z0-9_]*"),
]

# Per-language substring replacements (order matters)
SUBS: dict[str, list[tuple[str, str]]] = {
    "es": [
        (r"\bvacaciones altas\b", "Yamim Noraim (Alta Festividad)"),
        (r"\bunas vacaciones separadas\b", "una fiesta separada"),
        (r"\bdurante las vacaciones\b", "durante la festividad"),
        (r"\bantes de las vacaciones\b", "antes de la festividad"),
        (r"\bpara las vacaciones\b", "para la festividad"),
        (r"\balegría de vacaciones\b", "alegría de la festividad"),
        (r"\bun viaje de placer\b", "unas vacaciones físicas"),
        (r"\bNo unas vacaciones físicas\b", "no unas vacaciones físicas"),
        (r"\bHigh Holidays\b", "Yamim Noraim"),
        (r"\bHigh Holiday\b", "Yamim Noraim"),
        (r"\bMakeup prayer\b", "Oración tashlumin"),
        (r"\bmakeup prayer\b", "oración tashlumin"),
        (r"\bmakeup\b", "tashlumin"),
        (r"\bmaquillaje\b", "tashlumin"),
        (r"\boraciones de compensación\b", "oraciones tashlumin"),
        (r"\boración de compensación\b", "oración tashlumin"),
        (r"\bcompensación\b", "tashlumin"),
        (r"\bservicios móviles\b", "servicios en movimiento"),
        (r"\bG-d\b", "D-s"),
        (r"\bGod\b", "Dios"),
        (r"\bOWN\b", "PROPIO"),
        (r"\bBELOW\b", "ABAJO"),
        (r"\bTHE\b", "EL"),
        (r"\bFAST\b", "AYUNO"),
        (r"\bdrawing close\b", "acercándose"),
        (r"\bTap below\b", "Toque abajo"),
        (r"\bAdd a Mitzvah\b", "Añadir una mitzvá"),
        (r"\bAwe\b", "Temor"),
        (r"\bDías del Awe\b", "Yamim Noraim"),
        (r"\brapidamente\b", "con prontitud"),
        (r"\brápidamente\b", "con prontitud"),
        (r"\bRápido de Ester\b", "Ayuno de Ester"),
        (r"\bEl Rápido de Ester\b", "El ayuno de Ester"),
        (r"\bHoy es el Rápido de Ester\b", "Hoy es el ayuno de Ester"),
        (r"\b25 horas rápido\b", "ayuno de 25 horas"),
        (r"\bLleno de 25 horas rápido\b", "Ayuno completo de 25 horas"),
        (r"\bPrepárate rápido antes de que empiece el rápido\b", "Prepárese antes de que comience el ayuno"),
        (r"\bposponer el rápido\b", "posponer el ayuno"),
        (r"\bpre-rápida final\b", "comida previa al ayuno"),
        (r"\bpre-rápida\b", "previa al ayuno"),
        (r"\bAshkenazim rápido\b", "los ashkenazim ayunan"),
        (r"\bVacaciones perdidas\b", "Oraciones perdidas"),
        (r"\bTodas las vacaciones de Yom Tov\b", "Todos los días de Yom Tov"),
        (r"\bmenú de vacaciones\b", "menú festivo"),
        (r"\bEn Shabat y vacaciones\b", "En Shabat y fiestas"),
        (r"\ben Shabat y vacaciones\b", "en Shabat y fiestas"),
        (r"\bvacaciones judías\b", "fiestas judías"),
        (r"\btodas las vacaciones judías\b", "todas las fiestas judías"),
        (r"\bdisfrutar de las vacaciones\b", "disfrutar de la festividad"),
        (r"\bdisfrute de las vacaciones\b", "disfrute de la festividad"),
        (r"\ben las vacaciones\b", "en la festividad"),
        (r"\bde las vacaciones\b", "de la festividad"),
        (r"\(2\) vacaciones Kiddush", "(2) Kiddush de la festividad"),
        (r"holiday Kiddush", "Kiddush de la festividad"),
        (r"después de que las vacaciones comiencen", "después de que comience la festividad"),
        (r"cuando las vacaciones comienzan más tarde", "cuando la festividad comienza más tarde"),
        (r"\blas vacaciones comienzan\b", "la festividad comienza"),
        (r"\blas Altas Vacaciones\b", "Yamim Noraim"),
        (r"\bAltas Vacaciones\b", "Yamim Noraim"),
        (r"vacaciones; hoy comidas", "festividad; hoy comidas"),
        (r"^Vacaciones\.", "Festividades."),
        (r"\nVacaciones\.", "\nFestividades."),
        (r"two days of Yom Tov at the start in the Diaspora", "two days of Yom Tov at the start in the Diaspora"),
        (r"dos días de Yom Tov al inicio en la Diaspora", "dos días de Yom Tov al inicio en la Diáspora"),
        (r"vacaciones hasta que llegue", "festividad hasta que llegue"),
        (r"vacaciones menores", "fiestas menores"),
        (r" —Rosh", " — Rosh"),
        (r"—Rosh", " — Rosh"),
        (r"A machzor es", "Un machzor es"),
        (r"machzor A machzor", "machzor — Un machzor"),
        (r"inventando una", "compensando una"),
        (r"está inventando", "compensa"),
        (r"\bEl Rápido de\b", "El ayuno de"),
        (r"\bTorá-mandated rápido\b", "ayuno prescrito por la Torá"),
        (r"\bTorah-mandated rápido\b", "ayuno prescrito por la Torá"),
        (r"\brápido lleno\b", "ayuno completo"),
        (r"\bun rápido lleno\b", "un ayuno completo"),
        (r"\bes un rápido lleno\b", "es un ayuno completo"),
        (r"\brápido menor\b", "ayuno menor"),
        (r"\bun rápido menor\b", "un ayuno menor"),
        (r"\bdías rápidos públicos\b", "días de ayuno público"),
        (r"\bno un día rápido\b", "no es un día de ayuno"),
        (r"\bsemi-holiday\b", "semi-festividad"),
        (r"\bdibujo cerca\b", "acercarse"),
        (r"\bMorir significa\b", "Habitar significa"),
        (r"\bAshkenaz i m\b", "Los ashkenazim"),
        (r"\bSephardi m\b", "Los sefardíes"),
        (r"\bconkinot\b", "con kinot"),
        (r"\bplan Torah goles\b", "planifique metas de Torá"),
        (r"\bGrace After Meals\b", "Birkat Hamazón"),
        (r"\bropa tefillin\b", "— quitar el tefillin"),
        (r"\bRetirar tefillin antes Musaf —\b", "Quitar el tefillin antes de Musaf —"),
        (r"\bpost-bendición\b", "bendición posterior"),
        (r"\bpublicado a 4\b", "pospuesto al 4"),
        (r"\bReglas mínimas menores\b", "Reglas de ayuno menor"),
        (r"\bno es un día rápido\b", "no es un día de ayuno"),
        (r"\bdía rápido difieren\b", "los días de ayuno difieren"),
        (r"\bhorarios de día rápido\b", "horarios de días de ayuno"),
        (r"\bTiempo rápido Halachic\b", "Tiempo de ayuno halájico"),
        (r"\bfast Halachic\b", "ayuno halájico"),
        (r"\bantes de que el rápido comience\b", "antes de que comience el ayuno"),
        (r"\bla velocidad termina\b", "el ayuno termina"),
        (r"\bla velocidad es\b", "el ayuno es"),
        (r"\bel rápido comienza\b", "el ayuno comienza"),
        (r"\bdespués de que la velocidad\b", "después de que el ayuno"),
        (r"D-s 's soberanía", "la soberanía de D-s"),
        (r"D-s 's presencia", "la presencia de D-s"),
        (r"D-s 's plan", "el plan de D-s"),
        (r"D-s 's Nombre", "el Nombre de D-s"),
        (r"D-s 's names", "los nombres de D-s"),
        (r"D-s 's unicidad", "la unicidad de D-s"),
        (r"D-s 's apoyo", "el apoyo de D-s"),
        (r"santificados D-s 's Nombre", "santifica el Nombre de D-s"),
        (r"santificación D-s 's Nombre", "santificación del Nombre de D-s"),
        (r"a t rav és", "a través"),
        (r"t rav és", "través"),
        (r"g rav e\b", "grave"),
        (r"g rav es\b", "graves"),
        (r"ma rav illa", "maravilla"),
        (r"Gracias\.D-s", "Gracias a D-s"),
        (r"desapareciendo D-s", "profanando el Nombre de D-s"),
        (r"Mourners recite", "Los dolientes recitan"),
        (r"diario siddur", "sidur diario"),
        (r"a machzor se requiere", "se requiere un machzor"),
        (r"\bAshkenaz i\b", "Los ashkenazim"),
        (r"\bSephardi m\b", "Los sefardíes"),
        (r"\bRecite la mitad Hallel\b", "Reciten la mitad del Hallel"),
        (r"\bRecite the\b", "Reciten"),
        (r"\bSigue a tu comunidad\b", "Sigan el minhag de su comunidad"),
        (r"\bHigh Holiday services\b", "servicios de Yamim Noraim"),
        (r"no unas vacaciones físicas", "no un placer físico"),
        (r"unas vacaciones físicas", "un placer físico"),
        (r"Оригинальное название:", ""),
        (r"El Nine Days\b", "Los Nueve Días"),
        (r"Purim día", "día de Purim"),
        (r"El Purim Seudah es la comida de la tarde festiva Purim día", "La seudá de Purim es la comida festiva de la tarde del día de Purim"),
    ],
    "fr": [
        (r"\bvacances\b(?!\s+physiques)", "fête"),
        (r"\bHigh Holidays\b", "Yamim Noraim"),
        (r"\b — in the Diaspora\b", " — en Diaspora"),
        (r"\bMakeup prayer\b", "Prière de rattrapage (tashlumin)"),
        (r"\bmakeup\b", "tashlumin"),
        (r"\bmaquillage\b", "tashlumin"),
        (r"\bG-d\b", "D."),
        (r"\bGod\b", "D."),
        (r"\bOWN\b", "PROPRE"),
        (r"\bBELOW\b", "CI-DESSOUS"),
        (r"\bdessin\b", "rapprochement"),
        (r"\bTap below\b", "Appuyez ci-dessous"),
        (r"\bAdd a Mitzvah\b", "Ajouter une mitzvah"),
        (r"\bAwe\b", "Crainte"),
        (r"\bsemi-holiday\b", "semi-fête"),
        (r"\ble t rav ail\b", "le travail"),
        (r"\bréinitialisation mensuelle\b", "réinitialisation mensuelle"),
        (r"D\. La souveraineté", "la souveraineté de D."),
        (r"D\. Le plan", "le plan de D."),
        (r"D\. La présence", "la présence de D."),
        (r"services de Haute Fête", "offices des Yamim Noraïm"),
        (r"d'Crainte", "de crainte"),
        (r"d'admiration", "de crainte"),
        (r"dansPsalm2", "dans le Psaume 2"),
        (r"\bt rav ail\b", "travail"),
        (r"t rav ers", "travers"),
        (r"t rav aux", "travaux"),
        (r"rav issant", "ravissant"),
        (r"Ung Shabbat", "Oneg Shabbat"),
        (r"à t rav ", "à travers "),
        (r"\best ravissant dans Shabbat\b", "se réjouir de Chabbat"),
        (r"\best ravissant\b", "se réjouir"),
        (r"\baide ong à se produire\b", "favorise l'oneg"),
        (r"\bPas seulement pour éviter\b", "Pas seulement éviter"),
        (r"vacances physiques", "plaisir physique"),
        (r"\bHigh Holiday\b", "Yamim Noraïm"),
    ],
    "ru": [
        (r"\bотпуск\b", "праздник"),
        (r"\bHigh Holidays\b", "Ямим Нораим"),
        (r"\bHigh Holiday\b", "Ямим Нораим"),
        (r"\bMakeup prayer\b", "Молитва догоняния (tashlumin)"),
        (r"\bmakeup\b", "tashlumin"),
        (r"\bG-d\b", "Б-г"),
        (r"\bGod\b", "Б-г"),
        (r"\bOWN\b", "СВОЙ"),
        (r"\bBELOW\b", "НИЖЕ"),
        (r"\bTap below\b", "Нажмите ниже"),
        (r"\bAdd a Mitzvah\b", "Добавить мицву"),
        (r"\bsemi-holiday\b", "полупраздник"),
        (r"\bполупраздник с Hallel наполовину\b", "полупраздник с половинным Hallel"),
        (r"А\.machzor", "Махзор"),
        (r"Алейну - Алейну -", "Алейну —"),
        (r"Б-г суверенитет", "суверенитет Б-га"),
        (r"повседневной жизни\.siddur", "ежедневном сидуре"),
        (r"Практикуйте отпускать месть", "Практикуйте отказываться от мести"),
        (r"отпустить месть", "отказаться от мести"),
        (r"заповедь от Б-г Кроме", "заповедь от Б-га. Кроме"),
        (r"\bminyan\b", "миньян"),
        (r"\bMinyan\b", "Миньян"),
        (r"bar mitzvah Возраст", "возраста бар-мицвы"),
        (r"bar mitzvah age", "возраста бар-мицвы"),
        (r"\bThe chazzan\b", "Хazan"),
        (r"\bTalmud\b", "Талмуд"),
        (r"Оригинальное название:", ""),
        (r"Никогда не отвергай", "никогда не отвергает"),
        (r"Join миньян", "Присоединяйтесь к миньяну"),
        (r"\bHigh Holiday\b", "Ямим Нораим"),
        (r"с minyan", "с миньяном"),
        (r"с одним minyan", "с миньяном"),
        (r"на многих minhag", "по многим минhagim"),
    ],
    "he": [
        (r"\bG-d\b", "ה'"),
        (r"\bGod\b", "ה'"),
        (r"\bA A A ", ""),
        (r"\bEturn\b", "ערב"),
        (r"\bOn On\b", "ב"),
        (r"\bThe Talmud\b", "התלמוד"),
        (r"\bFestival calendars\b", "לוחות חגים"),
        (r"\bמהיר תורה\b", "צום מצווה בתורה"),
        (r"\bמהיר\b", "צום"),
        (r"\bTimers/blech\b", "טיימרים/פלטה"),
        (r"\buv tavshilin\b", "עירוב תבשילין"),
        (r"\bהשבר שלך\b", "הרב שלך"),
        (r"\bאומנים\b", "בניסן"),
        (r"\bA rav הוא עבריין\b", "רב הוא פוסק"),
        (r"\bהכריך של היטלר\b", "כורך של הלל"),
        (r"\bידי טיהור\b", "יד סולדת בו"),
        (r"\bסוסי אומן\b", "חזרת"),
        (r"\bסוסי צלע\b", "חזרת"),
        (r" - - - ", ""),
        (r"An An An ", ""),
        (r"פסטיבלים", "חגים"),
        (r"פסטיבל", "חג"),
        (r"Hurtin", "hamin"),
        (r"אורורה", "מנורה"),
        (r"אולוגיזציה", "הספדים"),
        (r"ראש השנה \(חודש חדש\)", "ראש חודש"),
        (r"רוש צ'וש", "ראש חודש"),
        (r"יום שישי עם Hallel", "חצי חג עם הלל"),
        (r"Grace לאחר מאל", "ברכת המזון"),
        (r"Vayavo Aנקבהk", "ויבוא עמלק"),
        (r"שם הסרטון: ", ""),
        (r"בתי משפט כמו", "חסידויות כמו"),
        (r"ארבעה מניעים", "ארבע אמות"),
        (r"רעידות לעתים קרובות מעמד טקסט יום שישי", "בתי כנסת מודיעים לעתים קרובות בסטטוס ביום שישי"),
        (r"eruv צ'אט אפס", "עירוב חצרות"),
        (r"eruv\(1\)", "עירוב"),
        (r"halachic ", "הלכתי "),
        (r"Paragraph Amidah", "פסקה בעמידה"),
        (r"Enter Retzei", "הכניסו בִּרְצֵה"),
        (r"מישול ומרד", "משלוח מנות וסעודה"),
        (r"ראשית היום", "העיקרית היום"),
        (r"חופשה נפרדת", "חג נפרד"),
        (r"צ'ילה", "צ'ולנט"),
        (r"Yaknehazom", "יקנה\"ז מדלג על"),
        (r"חוזר לחיי יום שישי", "חוזר לחיי יום חול"),
        (r" הן מכס", " הן מנהג"),
        (r"chatzos halachic", "חצות הלכתית"),
        (r"Matters Modeh", "רלוונטי למודה"),
        (r"ברקצ'ו אטה", "ברוך אתה"),
        (r"בראוצ'ט", "ברכות"),
        (r"מיןאג", "מנהג"),
        (r"כרום לתפילה", "קוורום לתפילה"),
        (r"לדהור", "לתפילה"),
        (r"handwritten parchment", "קלף בכתב יד"),
        (r"asofer", "סופר"),
        (r"The The ", ""),
        (r"poskimזהו", "פוסקים: זהו"),
        (r"שם מקור: ", ""),
        (r"עקשנות טקסית", "טומאה טקסית"),
        (r"המדינה המשפיעה", "המצב המשפיע"),
        (r"בוקר בוקר בוקר", "טומאת ידיים בבוקר"),
    ],
}

# Hand-reviewed UI + high-traffic strings
HAND_FIXES: dict[str, dict[str, str]] = {
    "Makeup prayer": {
        "he": "תפילת תשלומין",
        "es": "Oración tashlumin",
        "fr": "Prière tashlumin",
        "ru": "Молитва tashlumin",
    },
    "Tashlumin — Makeup Prayers": {
        "he": "תשלומין — תפילות תשלומין",
        "es": "Tashlumin — oraciones tashlumin",
        "fr": "Tashlumin — prières de rattrapage",
        "ru": "Tashlumin — молитвы догоняния",
    },
    "Translation Notice": {
        "he": "הודעת תרגום",
        "es": "Aviso de traducción",
        "fr": "Avis de traduction",
        "ru": "Уведомление о переводе",
    },
    "You've switched to": {
        "he": "עברת ל",
        "es": "Has cambiado a",
        "fr": "Vous avez basculé vers",
        "ru": "Вы переключились на",
    },
    "Please note:": {
        "he": "שימו לב:",
        "es": "Tenga en cuenta:",
        "fr": "Veuillez noter :",
        "ru": "Обратите внимание:",
    },
    "• This language uses offline translations built into the app": {
        "he": "• שפה זו משתמשת בתרגומים לא מקוונים המובנים באפליקציה",
        "es": "• Este idioma usa traducciones sin conexión integradas en la app",
        "fr": "• Cette langue utilise des traductions hors ligne intégrées à l'application",
        "ru": "• Этот язык использует встроенные офлайн-переводы",
    },
    "• Translations may not be perfect or culturally accurate": {
        "he": "• התרגומים עלולים להיות לא מדויקים; להלכה יש להתייעץ עם רב",
        "es": "• Las traducciones pueden no ser perfectas; para halajá consulte a un rabino",
        "fr": "• Les traductions peuvent être imparfaites ; pour la halakha, consultez un rav",
        "ru": "• Переводы могут быть неточными; по галахе обращайтесь к раввину",
    },
    "• All website links lead to English-only content": {
        "he": "• כל הקישורים מובילים לתוכן באנגלית",
        "es": "• Todos los enlaces web llevan a contenido solo en inglés",
        "fr": "• Tous les liens mènent à du contenu en anglais uniquement",
        "ru": "• Все ссылки ведут на контент только на английском",
    },
    "• For the best experience, consider using English": {
        "he": "• לחוויה הטובה ביותר, שקלו להשתמש באנגלית",
        "es": "• Para la mejor experiencia, considere usar inglés",
        "fr": "• Pour une meilleure expérience, envisagez l'anglais",
        "ru": "• Для лучшего опыта рассмотрите использование английского",
    },
    "Got it": {
        "he": "הבנתי",
        "es": "Entendido",
        "fr": "Compris",
        "ru": "Понятно",
    },
    "• Translations are machine-assisted and may contain errors; consult a rav for halacha": {
        "he": "• התרגומים מסייעי מכונה ועלולים לכלול שגיאות; להלכה יש להתייעץ עם רב",
        "es": "• Las traducciones son asistidas por máquina y pueden contener errores; para halajá consulte a un rabino",
        "fr": "• Les traductions sont assistées par machine et peuvent contenir des erreurs ; pour la halakha, consultez un rav",
        "ru": "• Переводы машинные и могут содержать ошибки; по галахе обращайтесь к раввину",
    },
    "Recite Half Hallel after Shacharit on Rosh Chodesh if you say Hallel — a cherished custom, not obligatory for women (Peninei Halakha 05-01-12). Many Ashkenazi women omit Hallel; many Sephardi women recite it. Follow your community.\n\nTachanun is omitted on Rosh Chodesh.": {
        "es": "Reciten la mitad del Hallel después de Shacharit en Rosh Jodesh si recitan Hallel — costumbre apreciada, no obligatoria para mujeres (Peninei Halakha 05-01-12). Muchas ashkenazim omiten Hallel; muchas sefardíes lo recitan. Sigan el minhag de su comunidad.\n\nNo se dice Tachanun en Rosh Jodesh.",
        "fr": "Récitez la demi-Hallel après Shacharit à Roch Hodech si vous dites Hallel — coutume chérie, non obligatoire pour les femmes (Peninei Halakha 05-01-12). Beaucoup de femmes ashkénazes omettent Hallel ; beaucoup de femmes séfarades le récitent. Suivez votre communauté.\n\nTachanun est omis à Roch Hodech.",
        "ru": "Произнесите половину Hallel после Шахарит в Рош Ходеш, если читаете Hallel — почитаемый обычай, не обязательный для женщин (Peninei Halakha 05-01-12). Многие ashkenazim не читают Hallel; многие sefardim читают. Следуйте своей общине.\n\nТаханун не читают в Рош Ходеш.",
    },
    "Oneg Shabbat is delighting in Shabbat — good food, rest, Torah study, singing, and time with family or guests. It is a positive mitzvah, not only avoiding melacha. The Talmud criticizes those who fast or deprive themselves on Shabbat without need. Planning meals and atmosphere before Shabbat helps oneg happen without last-minute stress.": {
        "es": "Oneg Shabbat es deleitarse en Shabat — buena comida, descanso, estudio de Torá, canto y tiempo con familia o invitados. Es una mitzvá positiva, no solo evitar melajá. El Talmud critica a quienes ayunan o se privan en Shabat sin necesidad. Planificar comidas y ambiente antes de Shabat favorece el oneg sin estrés de último momento.",
        "fr": "Oneg Shabbat, c'est se réjouir de Chabbat — bonne nourriture, repos, étude de Torah, chant et temps en famille ou avec des invités. C'est une mitzvah positive, pas seulement éviter la mélacha. Le Talmud critique ceux qui jeûnent ou se privent de Chabbat sans raison. Planifier repas et ambiance avant Chabbat favorise l'oneg sans stress de dernière minute.",
        "ru": "Oneg Shabbat — радоваться Шаббату: хорошая еда, отдых, изучение Торы, пение и время с семьёй или гостями. Это позитивная мицва, не только избегание мелахи. Тalmud порицает тех, кто постится или лишает себя в Шабbat без нужды. Планирование трапез и атмосферы до Шаббата помогает oneg без спешки.",
    },
    "A minyan is ten Jewish men above bar mitzvah age (13) forming a quorum for public prayer. Certain prayers — including Kaddish, Barchu, and repetition of the Amidah — require a minyan. The Talmud stresses that communal prayer is especially useful for getting prayers accepted by Heaven; joining a minyan is considered the ideal way of prayer.": {
        "es": "Un minyan son diez hombres judíos mayores de bar mitzvá (13) que forman quórum para la oración pública. Ciertas oraciones — Kaddish, Barchu y la repetición de la Amidá — requieren minyan. El Talmud enfatiza que la oración comunitaria es especialmente eficaz; unirse a un minyan es la forma ideal de orar.",
        "fr": "Un minyan, c'est dix hommes juifs majeurs (bar mitzvah, 13 ans) formant un quorum pour la prière publique. Certaines prières — Kaddish, Barchu et répétition de l'Amidah — exigent un minyan. Le Talmud insiste sur l'efficacité de la prière communautaire ; rejoindre un minyan est la voie idéale.",
        "ru": "Миньян — десять еврейских мужчин старше бар-мицвы (13 лет), образующих кворум для общественной молитвы. Некоторые молитвы — Кадиш, Бarchu и повторение амиды — требуют миньяна. Тalmud подчёркивает силу общественной молитвы; присоединение к миньяну — идеальный способ молиться.",
    },
    "minyan — A minyan is ten Jewish men above bar mitzvah age (13) forming a quorum for public prayer. Certain prayers — including Kaddish, Barchu, and repetition of the Amidah — require a minyan. The Talmud stresses that communal prayer is especially useful for getting prayers accepted by Heaven; joining a minyan is considered the ideal way of prayer.": {
        "es": "minyan — Diez hombres judíos mayores de bar mitzvá (13) forman quórum para oración pública; Kaddish, Barchu y repetición de Amidá requieren minyan.",
        "fr": "minyan — Dix hommes juifs majeurs (13 ans) forment un quorum ; Kaddish, Barchu et Amidah répétée exigent un minyan.",
        "ru": "minyan — десять мужчин старше бар-мицвы (13) для кворума; Кadish, Бarchu и повтор амиды требуют миньяна.",
    },
    "Wait times reflect your family custom (minhag). Tap below to adjust.": {
        "he": "זמני ההמתנה משקפים את מנהג המשפחה (מנהג). הקישו למטה לכוונון.",
        "es": "Los tiempos de espera reflejan su minhag familiar. Toque abajo para ajustar.",
        "fr": "Les délais reflètent votre minhag familial. Appuyez ci-dessous pour ajuster.",
        "ru": "Время ожидания отражает семейный минхаг. Нажмите ниже, чтобы настроить.",
    },
    "$holidayName is about to begin. Please finish what you are doing, turn off your phone, and prepare for the holiday.": {
        "he": "$holidayName עומד להתחיל. סיימו מה שאתם עושים, כבו את הטלפון והתכוננו לחג.",
        "es": "$holidayName está por comenzar. Termine lo que hace, apague el teléfono y prepárese para la festividad.",
        "fr": "$holidayName va bientôt commencer. Terminez ce que vous faites, éteignez le téléphone et préparez-vous pour la fête.",
        "ru": "$holidayName скоро начнётся. Завершите дела, выключите телефон и приготовьтесь к празднику.",
    },
    "Bat mitzvah is when a girl reaches twelve years and one day and becomes obligated in mitzvot not dependent on the Temple's time — the standard age of obligation in traditional halacha. Customs for celebration vary — speeches, learning projects, family meal. Women's mitzvot include Shabbat candles, kashrut, charity, and Torah study — specifics follow family and rav.": {
        "fr": "La bat mitzvah, c'est quand une fille atteint douze ans et un jour et devient obligée dans les mitzvot qui ne dépendent pas du temps du Temple — l'âge standard d'obligation dans la halakha traditionnelle. Les coutumes de célébration varient — discours, projets d'étude, repas de famille. Les mitzvot des femmes incluent les bougies de Chabbat, la cacherout, la tsedaka et l'étude de la Torah — les détails suivent la famille et le rav.",
    },
    "bat mitzvah — Bat mitzvah is when a girl reaches twelve years and one day and becomes obligated in mitzvot not dependent on the Temple's time — the standard age of obligation in traditional halacha. Customs for celebration vary — speeches, learning projects, family meal. Women's mitzvot include Shabbat candles, kashrut, charity, and Torah study — specifics follow family and rav.": {
        "fr": "bat mitzvah — la bat mitzvah, c'est quand une fille atteint douze ans et un jour et devient obligée dans les mitzvot qui ne dépendent pas du temps du Temple — l'âge standard d'obligation dans la halakha traditionnelle. Les coutumes de célébration varient — discours, projets d'étude, repas de famille. Les mitzvot des femmes incluent les bougies de Chabbat, la cacherout, la tsedaka et l'étude de la Torah — les détails suivent la famille et le rav.",
    },
    'The Shulchan Aruch ("Set Table") is the classic 16th-century code of halacha (Jewish Law) by Rabbi Yosef Karo. Ashkenazim often study it with the Rema\'s glosses; Sephardim generally follow Rabbi Karo, but rabbis have debated about almost everything throughout the years.': {
        "ru": "Шулхан Арух («Покрытый стол») — классический кодекс галахи XVI века, составленный раввином Иосифом Каро. Ашкеназы часто изучают его с комментариями Рема; сефарды обычно следуют решениям рава Каро, хотя раввины спорили о многом на протяжении веков.",
    },
    'Shulchan Aruch — The Shulchan Aruch ("Set Table") is the classic 16th-century code of halacha (Jewish Law) by Rabbi Yosef Karo. Ashkenazim often study it with the Rema\'s glosses; Sephardim generally follow Rabbi Karo, but rabbis have debated about almost everything throughout the years.': {
        "ru": "Шулхан Арух — классический кодекс галахи XVI века («Покрытый стол»), составленный раввином Иосифом Каро. Ашкеназы часто изучают его с комментариями Рема; сефарды обычно следуют решениям рава Каро, хотя раввины спорили о многом на протяжении веков.",
    },
    "The Three Weeks from 17 Tammuz to Tisha B'Av mourn the Temple's destruction. Ashkenazim and Chabad prohibit haircuts, weddings, and instrumental music for the full period; Sephardim and Edot HaMizrach are generally more lenient until the week of Tisha B'Av. Restrictions intensify in the Nine Days — customs differ by nusach.": {
        "ru": "Три недели — с 17 Таммуза до Тиша б'Ав — скорбят о разрушении Храма. Ашкеназы и Хabad запрещают стрижки, свадьбы и инструментальную музыку на весь период; сефарды и Edot HaMizrach обычно более снисходительны до недели Тиша б'Ав. Ограничения усиливаются в Девять дней — обычаи различаются по нusach.",
    },
    "Three Weeks — The Three Weeks from 17 Tammuz to Tisha B'Av mourn the Temple's destruction. Ashkenazim and Chabad prohibit haircuts, weddings, and instrumental music for the full period; Sephardim and Edot HaMizrach are generally more lenient until the week of Tisha B'Av. Restrictions intensify in the Nine Days — customs differ by nusach.": {
        "ru": "Три недели — с 17 Таммуза до Тиша б'Ав скорбят о разрушении Храма. Ашкеназы и Хabad запрещают стрижки, свадьбы и инструментальную музыку на весь период; сефарды и Edot HaMizrach обычно более снисходительны до недели Тиша б'Ав. Ограничения усиливаются в Девять дней — обычаи различаются по нusach.",
    },
    "The chazzan (cantor) leads the congregation in prayer — especially when the service is sung or when a minyan needs a skilled voice for repetition of the Amidah. The role is shaliach tzibur (emissary of the community), not a performer. Many weekday services have no professional chazzan; a member leads instead.": {
        "ru": "Хазан (кантор) ведёт общину в молитве — особенно когда служба поётся или когда миньяну нужен опытный голос для повторения Амиды. Это роль шлиах цибур (посланника общины), а не исполнителя. В будни часто нет профессионального хазана; вместо него ведёт член общины.",
    },
    "chazzan — The chazzan (cantor) leads the congregation in prayer — especially when the service is sung or when a minyan needs a skilled voice for repetition of the Amidah. The role is shaliach tzibur (emissary of the community), not a performer. Many weekday services have no professional chazzan; a member leads instead.": {
        "ru": "хазан — кантор, который ведёт общину в молитве; роль шлиах цибур, а не исполнителя.",
    },
    "Experience the power of community prayer! 👥 Did you know? Praying with a minyan (group of 10) amplifies the power of our prayers! Here's something amazing: The Talmud teaches that G-d never rejects the prayers of a community. Today's mission: Join a minyan for prayer if possible.": {
        "ru": "Почувствуйте силу общественной молитвы! Знаете ли? Молитва с миньяном (группой из 10) усиливает силу наших молитв! Удивительно: Тalmud учит, что Б-г никогда не отвергает молитвы общины. Задача на сегодня: присоединитесь к миньяну для молитвы, если возможно.",
    },
}


def extract_placeholders(text: str) -> list[str]:
    found: list[str] = []
    for pat in PLACEHOLDER_RE:
        found.extend(m.group(0) for m in pat.finditer(text))
    return found


def repair_placeholders(en: str, tr: str) -> str:
    for ph in extract_placeholders(en):
        if ph in tr:
            continue
        if ph.startswith("${"):
            # Restore full Kotlin template blocks from English
            for km in re.finditer(r"\$\{[^}]+\}", en):
                block = km.group(0)
                if block not in tr:
                    # Replace mangled ${...} in translation
                    tr = re.sub(r"\$\{[^}]*\}", block, tr, count=1)
            continue
        var = ph[1:]
        # $var glued to next word: $holidayNameestá -> $holidayName está
        tr = re.sub(
            r"(?i)\$" + re.escape(var) + r"(?=[a-záéíóúüñàâäèêëïîôùûç])",
            ph + " ",
            tr,
        )
        # $var + spurious suffix token
        tr = re.sub(
            r"(?i)\$" + re.escape(var) + r"[A-Za-zÀ-ÿ]{1,16}",
            ph,
            tr,
            count=1,
        )
    return tr


def kotlin_template_blocks(text: str) -> list[str]:
    blocks: list[str] = []
    i = 0
    while i < len(text):
        if text.startswith("${", i):
            depth = 1
            j = i + 2
            while j < len(text) and depth > 0:
                if text[j] == "{":
                    depth += 1
                elif text[j] == "}":
                    depth -= 1
                j += 1
            blocks.append(text[i:j])
            i = j
        else:
            i += 1
    return blocks


def restore_kotlin_templates(en: str, tr: str) -> str:
    en_blocks = kotlin_template_blocks(en)
    if not en_blocks:
        return tr
    tr_blocks = kotlin_template_blocks(tr)
    if len(tr_blocks) == len(en_blocks):
        for eb, tb in zip(en_blocks, tr_blocks):
            if eb != tb:
                tr = tr.replace(tb, eb, 1)
    else:
        for eb in en_blocks:
            if eb not in tr:
                tr = re.sub(r"\$\{[^}]*\}", eb, tr, count=1)
    return tr


def apply_subs(lang: str, text: str) -> str:
    from translation_repairs import repair_translation

    def _subs(chunk: str) -> str:
        for pattern, repl in SUBS.get(lang, []):
            chunk = re.sub(pattern, repl, chunk, flags=re.IGNORECASE)
        return repair_translation(lang, chunk)

    if "${" not in text:
        return _subs(text)

    out: list[str] = []
    i = 0
    while i < len(text):
        if text.startswith("${", i):
            depth = 1
            j = i + 2
            while j < len(text) and depth > 0:
                if text[j] == "{":
                    depth += 1
                elif text[j] == "}":
                    depth -= 1
                j += 1
            out.append(text[i:j])
            i = j
        else:
            nxt = text.find("${", i)
            if nxt == -1:
                nxt = len(text)
            chunk = text[i:nxt]
            out.append(_subs(chunk))
            i = nxt
    return "".join(out)


def load_compiled() -> dict[str, dict[str, str]]:
    return {
        lang: json.loads((COMPOSE / f"{lang}.json").read_text(encoding="utf-8"))["entries"]
        for lang in LANGS
    }


def main() -> None:
    required = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    compiled = load_compiled()
    fixes: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}

    if OUT.exists():
        existing = json.loads(OUT.read_text(encoding="utf-8"))
        for lang in LANGS:
            fixes[lang].update(existing.get(lang, {}))

    # Merge manual + placeholder overrides (win over prior quality fixes)
    if MANUAL.exists():
        manual = json.loads(MANUAL.read_text(encoding="utf-8"))
        for lang in LANGS:
            fixes[lang].update(manual.get(lang, {}))

    if PLACEHOLDER_FIXES.exists():
        ph = json.loads(PLACEHOLDER_FIXES.read_text(encoding="utf-8"))
        for lang in LANGS:
            for en, tr in ph.get(lang, {}).items():
                fixes[lang][en] = repair_placeholders(
                    en, restore_kotlin_templates(en, apply_subs(lang, tr))
                )

    for en in required:
        for lang in LANGS:
            if en in HAND_FIXES and lang in HAND_FIXES[en]:
                fixes[lang][en] = HAND_FIXES[en][lang]
                continue

            tr = compiled[lang].get(en, en)
            if tr == en and lang != "he":
                continue
            new_tr = apply_subs(lang, tr)
            new_tr = restore_kotlin_templates(en, new_tr)
            new_tr = repair_placeholders(en, new_tr)
            if new_tr != tr:
                fixes[lang][en] = new_tr

    OUT.write_text(json.dumps(fixes, ensure_ascii=False, indent=2), encoding="utf-8")
    changed = sum(len(v) for v in fixes.values())
    print(f"wrote {OUT} ({changed} override entries)")


if __name__ == "__main__":
    main()
