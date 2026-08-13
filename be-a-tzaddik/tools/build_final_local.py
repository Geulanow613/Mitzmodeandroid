# -*- coding: utf-8 -*-
"""Build polished local_003/004 *_only.json from extracted legacy + overrides."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
HUMAN = ROOT / "human"

EXTRACTED = {
    b: json.loads((ROOT / f"_extracted_{b}.json").read_text(encoding="utf-8"))
    for b in ("003", "004")
}

O: dict[tuple[str, str, int], str] = {}


def o(batch: str, lang: str, idx: int, text: str) -> None:
    O[(batch, lang, idx)] = text


# Batch 004 — previously empty legacy entries
o("004", "he", 11,
  'למדו את הלכות לשון הרע מהחפץ חיים. — הוגש על ידי משה. חכמינו משווים דיבור מזיק '
  'לעבירות החמורות ביותר — ומלמדים שרכילות הרגלית יכולה להרוס מערכות יחסים, קהילות '
  'והמצב הרוחני של האדם. החפץ חיים הקדיש את חייו להראות כמה התורה מזהירה על דיבור שוב '
  'ושוב, כי מילים יכולות לבנות עולמות או לקרוע אותם. אפילו יום אחד של שמירה על הלשון '
  'יכול לשנות את הדרך שבה אתם רואים אחרים — ואיך הם רואים אתכם.')
o("004", "es", 11,
  "Aprende las leyes del lashon hara (mala lengua) del Jofetz Jaim. — Enviado por Moshe. "
  "Nuestros sabios comparan el habla dañina con los pecados más graves — y enseñan que el chisme habitual "
  "puede destruir relaciones, comunidades y la posición espiritual de una persona. El Jofetz Jaim dedicó su vida "
  "a mostrar cuánto advierte la Torá sobre el habla una y otra vez, porque las palabras pueden construir mundos "
  "o derribarlos. Incluso un solo día guardando la lengua puede transformar cómo ves a otras personas — y cómo te ven a ti.")
o("004", "fr", 11,
  "Apprenez les lois du lashon hara (mauvaise parole) du Hofets 'Haïm. — Soumis par Moshe. "
  "Nos sages comparent la parole nuisible aux fautes les plus graves — et enseignent que les commérages habituels "
  "peuvent détruire les relations, les communautés et la vie spirituelle d'une personne. Le Hofets 'Haïm a consacré "
  "sa vie à montrer combien la Torah met en garde encore et encore contre la parole, car les mots peuvent bâtir "
  "des mondes ou les détruire. Même une seule journée de garde de la langue peut transformer votre regard sur les "
  "autres — et le leur sur vous.")
o("004", "ru", 11,
  "Изучите законы лашон hara (злой речи) из «Хофец Хаим». — Предложено Моше. "
  "Наши мудрецы сравнивают вредную речь с самыми серьёзными грехами — и учат, что привычные сплетни "
  "могут разрушить отношения, общины и духовное положение человека. Хофец Хаим посвятил жизнь тому, "
  "чтобы показать, как часто Тора предупреждает о речи, ведь слова могут строить миры или разрушать их. "
  "Даже один день бережного отношения к языку может изменить то, как вы видите других — и как они видят вас.")

o("004", "he", 12,
  "אכלו קצת חמץ (מאכל חמץ) וכוונו שאתם מקיימים את מצוות ניקוי הבית מחמץ לקראת פסח. "
  "אם עכשיו פסח — אכלו מצה! בליל שלפני פסח, משפחות יהודיות מחפשות בכל פינה בבית לאור נר "
  "כל פירור חמץ אחרון — כי לא יכול להישאר חמץ בבית במשך שבעת ימי החג. יצאנו ממצרים בחיפזון "
  "כך שלחם לא הספיק להחמיץ, וניקוי החמץ מדי שנה מחבר אותנו לרגע החירות ההוא. מה שאתם "
  "לועסים היום הוא חלק מהסיפור העתיק הזה, אם יש לכם בכוונה שאתם עושים זאת לכבוד החג.")
o("004", "es", 12,
  "Come un poco de jametz (alimento fermentado) teniendo en mente que cumples la mitzvá de limpiar "
  "tu hogar del jametz para Pesaj. Si ahora es Pesaj, ¡come matzá! La noche antes de Pesaj, las familias "
  "judías buscan en cada rincón del hogar a la luz de una vela la última miga de jametz — porque no puede "
  "quedar levadura en la casa durante los siete días del festival. Salimos de Egipto con tanta prisa que el pan "
  "no tuvo tiempo de levantarse, y sacar el jametz cada año nos reconecta con ese momento de libertad. "
  "Lo que masticas hoy es parte de esa historia antigua, si lo haces con la intención de honrar la festividad.")
o("004", "fr", 12,
  "Mangez un peu de 'hamets (aliment levé) en ayant à l'esprit que vous accomplissez la mitzvah de nettoyer "
  "votre maison du 'hamets pour Pessah. Si nous sommes actuellement à Pessah, mangez de la matsah ! "
  "La veille de Pessah, les familles juives cherchent dans chaque recoin de la maison à la lumière d'une bougie "
  "la dernière miette de 'hamets — car aucune levure ne doit rester dans la maison pendant les sept jours de la fête. "
  "Nous sommes sortis d'Égypte si vite que le pain n'a pas eu le temps de lever, et éliminer le 'hamets chaque année "
  "nous reconnecte à ce moment de liberté. Ce que vous mâchez aujourd'hui fait partie de cette histoire ancienne, "
  "si vous l'avez à l'esprit en l'honneur de la fête.")
o("004", "ru", 12,
  "Съешьте немного хамеца (кислого) с намерением исполнить mitzvah очистки дома от хамеца к Песаху. "
  "Если сейчас Песах — ешьте мацу! В канун Песаха еврейские семьи при свете свечи ищут в каждом уголке дома "
  "последнюю крошку хамеца — ведь в течение семи дней праздника в доме не должно оставаться закваски. "
  "Мы вышли из Египта так поспешно, что хлеб не успел подняться, и ежегодная очистка от хамеца связывает нас "
  "с тем моментом свободы. То, что вы жуёте сегодня, — часть этой древней истории, если делаете это "
  "с намерением в честь праздника.")

o("004", "he", 13,
  "תנו ברכה ליהודי הבא שתפגשו. אם אין לכם מה לומר — פשוט ברכו אותו שיהיה לו חיים ארוכים ובריאים! "
  "בבית המקדש, הכוהנים היו מרימים ידיים ומברכים את כל העם במילים של שלום והגנה שאנחנו עדיין שומעים "
  "בבית הכנסת. לא צריך להיות כוהן כדי לאחל לישראל — ברכה מלב אל לב מיהודי רגיל היא מעשה אהבה יפה. "
  "כשאתם מברכים אדם אחר, אתם הופכים לערוץ של טוב בעולם.")
o("004", "es", 13,
  "Da una bendición al próximo judío que veas. Si no se te ocurre nada, simplemente bendícelo para que tenga "
  "una vida larga y saludable. En el Templo, los Kohanim alzaban las manos y bendecían a toda la nación con "
  "palabras de paz y protección que aún escuchamos en la sinagoga hoy. No tienes que ser Kohen para desearle "
  "bien a alguien — una bendición sincera de un judío común es un hermoso acto de amor. Cuando bendices a "
  "otra persona, te conviertes en un canal de bien en el mundo.")
o("004", "fr", 13,
  "Donnez une bénédiction au prochain Juif que vous croisez. Si vous ne trouvez rien à dire, souhaitez-lui "
  "simplement une longue vie en bonne santé ! Au Temple, les Kohanim levaient les mains et bénissaient toute "
  "la nation avec des paroles de paix et de protection que nous entendons encore à la synagogue. Vous n'avez "
  "pas besoin d'être Kohen pour souhaiter du bien — une bénédiction sincère d'un Juif ordinaire est un bel "
  "acte d'amour. Quand vous bénissez quelqu'un, vous devenez un canal de bien dans le monde.")
o("004", "ru", 13,
  "Дайте благословение следующему еврею, которого увидите. Если не знаете, что сказать — просто пожелайте "
  "ему долгой и здоровой жизни! В Храме коhenim поднимали руки и благословляли весь народ словами мира "
  "и защиты, которые мы до сих пор слышим в синагоге. Не нужно быть kohen, чтобы пожелать кому-то добра — "
  "искреннее благословение от обычного еврея — прекрасный акт любви. Благословляя другого, вы становитесь "
  "каналом добра в мире.")

o("004", "he", 17,
  'התנגדו באופן פעיל לעבודה זרה! 🔨 הרמב"ם מסביר בפרק ז\' שיש לנו מצווה חיובית להשמיד חפצי עבודה זרה '
  'וכל מה שנעשה בהם שימוש. הנה משהו עוצמתי: לא רק שאנחנו נמנעים מעבודה זרה — אנחנו פועלים באופן פעיל '
  'לחסל אותה! ידעתם? גם העץ והאבנים של אליל חייבים להישמד. משימת היום: קחו עמדה פעילה נגד '
  'השפעות שליליות בחייכם.')
o("004", "es", 17,
  "¡Oponte activamente a la idolatría! 🔨 El Rambam explica en el capítulo 7 que tenemos un mandamiento "
  "positivo de destruir objetos de culto idólatra y todo lo usado para ello. Algo poderoso: no solo evitamos "
  "la idolatría — ¡trabajamos activamente para eliminarla! ¿Lo sabías? Incluso la madera y las piedras de un "
  "ídolo deben destruirse. Misión de hoy: da un paso activo contra influencias negativas en tu vida.")
o("004", "fr", 17,
  "Opposez-vous activement à l'idolâtrie ! 🔨 Le Rambam explique au chapitre 7 que nous avons un commandement "
  "positif de détruire les objets de culte idolâtre et tout ce qui sert à cela. Voici quelque chose de puissant : "
  "nous n'évitons pas seulement l'idolâtrie — nous agissons activement pour l'éliminer ! Le saviez-vous ? "
  "Même le bois et les pierres d'une idole doivent être détruits. Mission d'aujourd'hui : prenez position "
  "contre les influences négatives dans votre vie.")
o("004", "ru", 17,
  "Активно противостойте идолопоклонству! 🔨 Рамбам объясняет в главе 7, что у нас есть положительная "
  "заповедь уничтожать предметы идолопоклонства и всё, что использовалось для него. Вот что-то мощное: "
  "мы не просто избегаем идолопоклонства — мы активно работаем над его устранением! Ты знал? "
  "Даже дерево и камни от идола должны быть уничтожены. Сегодняшняя миссия: активно противостоять "
  "негативным влияниям в вашей жизни.")

# Batch 003 — key Hebrew fixes
o("003", "he", 0,
  'לכו בדרכי ה\'! 👣 הרמב"ם מגלה בהלכות דעות: בדיוק כפי שה\' הוא רחום — היו רחום. בדיוק כפי שהוא חנון — היו חנון. בדיוק כפי שהוא קדוש — היו קדושים. הנה המפתח: מצאו את דרך האמצע בכל תכונותיכם — לא קשים מדי, לא רכים מדי. משימת היום: בחרו תכונה אלוקית אחת (חסד, קדושה או רחמים) והתמקדו באמת לפתח אותה!')
o("003", "he", 6,
  'גלו את האות המיסטית ה\' (ה)! ✡ האות העוצמתית הזו מייצגת את נוכחות ה\' בעולמנו! צורתה מלמדת משהu מדהים: יש לה פתח בתחתית — מראה שגם אם נופלים, תמיד אפשר לחזור לה\'. ידעתם? האות ה\' מופיעה פעמיים בשם הקדוש — מלמדת שה\' נוכח גם בעולם הרוחני וגם בגשמי. קחו רגע להבחין בנוכחות ה\' בחייכם היום.')
o("003", "he", 14,
  'חוו את כוח המזוזה! 🏠 הרמב"ם מסביר שבכל פעם שעוברים ליד מזוזה, היא מזכירה את נוכחות ה\' ומעוררת משינה רוחנית! למילה «מזוזה» גימטריה 65 — כמו «היכל», חדר או ארמון — ומראה שהמזוזה הופכת בית ל«ארמון» קטן למקום שכינה. בנוסף, טקסט המזוזה מכיל 713 אותיות — גימטריה של «תשובה».')

POST = [
    (r"\bDi-s\b", "D-s"), (r"\bD-os\b", "D-s"), (r"\bG-D\b", "D-s"),
    ("te partirán", "se te contagiarán"),
    ("¡Suéltame!", "¡Suelta rencores!"),
    ("Maestro el", "Domina el"),
    ("Mostrar más", "¡Muestra más"),
    ("Leurs bonnes qualités vont vous déprimer", "Leurs bonnes qualités finiront par vous inspirer"),
    ("свалятся на вас", "передадутся вам"),
    ("713 cartas", "713 letras"),
    ("משהu", "משהו"),
    ("קchu", "קחו"),
    ("קchu", "קחו"),
]


def post(s: str) -> str:
    for old, new in POST:
        if old.startswith("\\"):
            s = re.sub(old, new, s)
        else:
            s = s.replace(old, new)
    return s


def build(batch: str, lang: str) -> list[str]:
    return [post(O.get((batch, lang, i), s)) for i, s in enumerate(EXTRACTED[batch][lang])]


def main() -> None:
    for batch in ("003", "004"):
        for lang in ("he", "es", "fr", "ru"):
            strings = build(batch, lang)
            assert len(strings) == 25 and all(x.strip() for x in strings), f"{batch}/{lang}"
            path = HUMAN / f"local_{batch}_{lang}_only.json"
            path.write_text(json.dumps({lang: strings}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
            print(path.name)


if __name__ == "__main__":
    main()
