#!/usr/bin/env python3
"""Write human/he_alert_tone.json — quality Hebrew for Mitzvah Me alerts."""
from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "tools"))
HUMAN = ROOT / "data" / "translation-catalog" / "human"
OUT = HUMAN / "he_alert_tone.json"
SOURCES = ["local_003.json", "local_005.json", "glossary_polish.json", "mitzvah_alert_tone.json"]

from _he_alert_tone_batch29 import MANUAL_HE_BATCH29  # noqa: E402
from _he_alert_tone_batch50 import MANUAL_HE_BATCH50  # noqa: E402
from _he_alert_tone_batch70 import MANUAL_HE_BATCH70  # noqa: E402
from _he_alert_tone_batch100 import MANUAL_HE_BATCH100  # noqa: E402
HEBREW_FIXES = ROOT / "data" / "translation-catalog" / "shards" / "hebrew_fixes.json"

ALERT_PAT = re.compile(
    r"(Did you know|Today's mission|Today's challenge|Here's your mission|"
    r"Here's something|Your mission:|Take a moment|Here's the key|Here's your challenge|"
    r"Mission:)",
    re.I,
)
EMOJI_PAT = re.compile(r"[\U0001F300-\U0001FAFF\U00002600-\U000027BF]")
HYBRID = re.compile(r"[\u05d0-\u05ea][a-zA-Z]{2,}|[a-zA-Z]{2,}[\u05d0-\u05ea]")
LATIN_IN_HE = re.compile(
    r"[\u0590-\u05ff].{0,8}[A-Za-z]{3,}|[A-Za-z]{3,}.{0,8}[\u0590-\u05ff]"
)

MT_MARKERS = [
    "הידעת", "משימת היום", "המשימה של היום", "האתגר של היום", "הנה המשימה שלך",
    "הנה האתגר שלך", "הנה המפתח", "גלה את המכתב", "המאסטר", "D-s", "Hilchot", "Kochavim", " TWO ",
    "Hakhel", "Vav (Bee)", "feat.", "Everze", "t Kochavim", "הילוצ",
    "קאף (בד)", "דיילט", "Zayin", "Tet (", "צ'ומאש", "התנצר", "שם בדוי",
    "הילכואבאט", "הילוצ'ואט", "Hilcho", "Sé un", "Conéctate", "Descubre",
    "¡Celebra", "Conectar conPsalm", "t Kochavim", "עבודה זרהt",
    "אמונות על-על", "הומאים", "סימן \"עבודה\"",
]

ALLOWED_LATIN = {
    "Rambam", "Torah", "Talmud", "Shabbat", "Tehillim", "Shema", "Kiddush",
    "Havdalah", "Hallel", "Musaf", "Mincha", "Maariv", "Shacharit", "Ashrei",
    "Birkat", "Hamazon", "Kabbalists", "Chabad", "Sephard", "Ashkenaz",
    "Sefard", "gematria", "mitzvah", "mitzvot", "halacha", "mezuzah",
    "tefillin", "tzitzit", "kashrut", "Amen", "Wi", "Fi", "Hakhel",
    "Chumash", "Tanach", "Tefillin", "Kohen", "Kohanim", "Shechina",
    "minyan", "siddur", "segula", "Teshuvah", "Arizal", "Ramban",
    "Absalom", "Shaul", "Samuel", "Mordecai", "Esther", "Zohar",
    "Raphael", "Rachel", "Sarah", "Dina", "Odel", "Yosef", "HB", "MB", "AS", "DM",
}

# Hand-authored quality Hebrew for keys that need full rewrite (Israeli alert tone).
MANUAL_HE: dict[str, str] = {
    "Walk in G-d's ways! 👣 The Rambam reveals in Hilchot Deot: Just as G-d is merciful, you should be merciful. Just as He is gracious, you should be gracious. Just as He is holy, you should be holy. Here's the key: Find the middle path in all your traits - not too harsh, not too soft. Today's mission: Pick one divine attribute (kindness, holiness, or mercy) and really focus on becoming it!":
        "לכו בדרכי ה'! 👣 הרמב\"ם בהלכות דעות: כשם שה' רחום — היו רחומים; כשם שהוא חנון — היו חנונים; כשם שהוא קדוש — היו קדושים. הסוד: דרך האמצע בכל תכונה — לא קשים מדי ולא רכים מדי. משימה להיום: בחרו תכונה אלוקית אחת (חסד, קדושה או רחמים) והתמקדו בה באמת!",
    "Choose your friends wisely! 🤝 The Rambam teaches a crucial life lesson: A person is naturally influenced by their friends' behaviors and opinions. That's why it's a mitzvah to connect with the righteous and learn from their ways! Here's your challenge: Spend time today or chat with someone who inspires you to be better. Their good qualities will rub off on you!":
        "בחרו חברים בחוכמה! 🤝 הרמב\"ם מלמד: אדם מושפע מהחברים שלו — לכן מצווה להתחבר לצדיקים וללמוד מדרכיהם! האתגר: בלו היום או שוחחו עם מישהו שגורם לכם להשתפר. המידות הטובות שלו ידבקו גם בכם!",
    "Double your love! ❤️ The Rambam explains two beautiful mitzvot: loving fellow Jews and showing special love to converts. Why the extra love for converts? They chose to join our people and follow Torah - that's amazing! Here's something practical: Make an extra effort today to show love to another Jew through action - whether they were born Jewish or chose to join our people!":
        "הכפילו אהבה! ❤️ הרמב\"ם מסביר שתי מצוות יפות: אהבת ישראל ואהבה מיוחדת לגרים. למה עוד אהבה לגרים? הם בחרו להצטרף לעם וללכת בתורה — מדהים! טיפ מעשי: היום הראו אהבה ליהודי אחר במעשה — נולד כאן או הצטרף מרצון!",
    "Discover the letter Dalet (ד)! 🚪 The shape of this letter teaches us about humility - it's bent over, like someone who is humble. Its name means 'door' in Hebrew, reminding us that humility is the doorway to all spiritual growth. Did you know? The Dalet's numerical value is 4, connecting to the 4 directions - showing how a humble person is open to learning from everyone! Take a moment to practice humility today by learning something new from someone else.":
        "גלו את האות דלת (ד)! 🚪 צורתה כפופה — כמו אדם עניו. שמה «דלת» — הענווה היא הדלת לכל צמיחה רוחנית. ידעתם? גימטריה 4 — ארבע רוחות העולם; עניו פתוח ללמוד מכולם! קחו רגע היום לתרגל ענווה — למדו משהו חדש מאדם אחר.",
    "Guard your heart against hatred! ❤️ The Torah teaches us something profound: Even if someone wronged you, don't harbor hatred in your heart. Instead, speak to them about what's bothering you! Did you know? The word for hatred in Hebrew (שנאה) has the same letters as the word for sleep (שינה), teaching us that hatred makes us spiritually asleep. Here's your mission: If you're feeling negative towards someone, try to have an honest, respectful conversation with them or speak to a rabbi for guidance.":
        "שמרו על הלב מפני שנאה! ❤️ גם אם מישהו פגע בכם — אל תאחסנו שנאה בלב. דברו איתו על מה שמציק! ידעתם? «שנאה» ו«שינה» מאותן אותיות — שנאה מרדימה רוחנית. משימה: מרגישים שליליות כלפי מישהו? נסו שיחה כנה ומכובדת, או פנו לרב.",
    "Master the art of loving rebuke! 💝 The Torah gives us an amazing tool: If you see someone doing something wrong, don't just stay quiet or gossip about it - help them grow! Here's the key: Speak privately, gently, and make it clear you're coming from a place of love. Did you know? The word for rebuke (תוכחה) is related to the word for proof (הוכחה), teaching us to help others see proof of a better way! Today's challenge: If you need to give feedback, do it with kindness and wisdom.":
        "שלטו באמנות התוכחת מאהבה! 💝 רואים מישהו טועה? אל תשתקו ואל תרכלו — עזרו לו לגדול! הסוד: בפרטיות, בעדינות, ממקום של אהבה. ידעתם? «תוכחה» קשורה ל«הוכחה» — לעזור לראות דרך טובה יותר. אתגר היום: צריך לתת משוב? עשו זאת בחום ובחוכמה.",
    "Show extra care to those who need it most! 🤗 The Torah gives us a special mitzvah to be particularly gentle with widows and orphans. Why? Because they've experienced loss and might feel more vulnerable. Here's something powerful: The Talmud teaches that G-d Himself is their guardian! When you help them, you're partnering with G-d! Today's mission: Show extra kindness to someone who might be feeling alone or vulnerable.":
        "הראו טיפוח מיוחד למי שזקוק לו! 🤗 מצווה להיות עדינים במיוחד עם אלמנות ויתומים — כי חוו אובדן ועלולים להרגיש פגיעים. חזק: הגמרא מלמדת שה' עצמו שומר עליהם! כשעוזרים להם — שותפים עם ה'. משימה להיום: הראו חסד למי שמרגיש בודד או פגיע.",
    "Guard your tongue! 👄 The power of speech is incredible - it can build worlds or destroy them! Here's something amazing: The Hebrew word for speech (דיבור) has the same numerical value as the word for bee (דבורה). Just like a bee can make honey or sting, our words can be sweet or harmful! Today's challenge: Before speaking about others, ask yourself: Is it true? Is it kind? Is it necessary? Make your words as sweet as honey!":
        "שמרו על הלשון! 👄 כוח הדיבור מדהים — בונה עולמות או הורס. ידעתם? «דיבור» ו«דבורה» באותה גימטריה — מילים יכולות להיות דבש או עוקץ. אתגר היום: לפני שמדברים על אחרים — נכון? אדיב? הכרחי? תנו למילים להיות מתוקות כדבש!",
    "Rise above revenge! 🌅 The Torah teaches us something powerful: Don't take revenge, even when you have the chance! Here's a classic example: If someone refused to lend you something, and later they ask to borrow from you - don't say 'No, just like you did to me!' Instead, help them! Did you know? This mitzvah helps us become more like G-d, who gives good even to those who don't deserve it. Today's challenge: Do something nice for someone who wasn't nice to you.":
        "עלו מעל נקמה! 🌅 אל תנקמו גם כשיש הזדמנות. דוגמה קלאסית: מישהו סירב להלוות לכם — ואחר כך מבקש מכם? אל תגידו «לא, כמו שעשית לי!» — עזרו! ידעתם? המצווה הזו מקרבת אותנו לה', שנותן טוב גם ללא ראויים. אתגר היום: עשו טוב למי שלא היה נחמד אליכם.",
    "Let go of grudges! 🕊️ Here's an amazing mitzvah: Even if you can't take revenge, don't hold onto the hurt either! The Torah's example: When someone asks to borrow something, don't say 'Sure, I'm not like you who refused me!' - just lend with a full heart! Did you know? Holding a grudge is like drinking poison and expecting the other person to get sick. Free yourself by letting go! Today's mission: Replace a grudge with a blessing.":
        "שחררו טינות! 🕊️ גם בלי נקמה — אל תחזיקו כעס. דוגמה מהתורה: מבקשים לשאול? אל תאמרו «בטח, אני לא כמוך שסירבת לי» — הלוו בלב שלם! ידעתם? להחזיק טינה כמו לשתות רעל ולחכות שהשני יחלה. שחררו את עצמכם! משימה להיום: החליפו טינה בברכה.",
    "Embrace the gift of Torah study! 📚 The Rambam reveals something amazing: Learning Torah isn't just about gaining knowledge - it's about connecting to G-d's wisdom! Did you know? Every word of Torah you learn creates an angel that will accompany you forever! Here's your mission: Set aside even 5 minutes today to learn something new. Whether it's a verse, a law, or a story - every moment of learning is priceless!":
        "חבקו את מתנת לימוד התורה! 📚 הרמב\"ם: לימוד תורה הוא חיבור לחכמת ה' — לא רק ידע. ידעתם? כל מילה בלימוד יוצרת מלאך שליווה אתכם לנצח! משימה: הקדישו 5 דקות היום למשהו חדש — פסוק, הלכה או סיפור. כל רגע לימוד — יקר מפז.",
    "Honor Torah teachers! 👨‍🏫 The Rambam teaches us something powerful: A teacher of Torah is like a parent who gives us eternal life! Did you know? The respect due to a Torah teacher is so great that if both your father and your teacher are carrying heavy loads, you help your teacher first (unless your father is also a Torah scholar)! Here's today's mission: Show appreciation to someone who has taught you Torah - whether through a message, a call, or a small gift.":
        "כבדו מורי תורה! 👨‍🏫 הרמב\"ם: מורה תורה כמו הורה שנותן חיי נצח. ידעתם? אם אבא והמורה נושאים עומסים — עוזרים קודם למורה (אלא אם האב גם תלמיד חכם). משימה להיום: הודו למי שלימד אתכם תורה — הודעה, שיחה או מתנה קטנה.",
    "Discover the letter Vav (ו)! ✡ This amazing letter is shaped like a hook, connecting heaven and earth! Did you know? The letter Vav means 'and', teaching us that everything in creation is connected! Its numerical value is 6, representing the six days of creation. Here's something cool: When you add a Vav to the beginning of a Hebrew word, it transforms future tense to past - showing how G-d transcends time! Take a moment to notice the connections in your life.":
        "גלו את האות וו (ו)! ✡ צורת וו כמו וו — מחברת שמיים וארץ. ידעתם? «ו» פירושו «ו» — הכל בבריאה מחובר. גימטריה 6 — ששת ימי בראשית. מגניב: וו בתחילת מילה הופך עתיד לעבר — ה' מעל הזמן! שימו לב לקשרים בחייכם.",
    "Learn about the mitzvah of writing a Torah scroll! ✍️ Every Jewish man has the mitzvah to write (or according to some, participate in writing) a Torah scroll! Some also say you can fulfill this mitzvah nowadays by buying books like a Chumash, Tanach, and other holy books. Each letter in the Torah corresponds to a Jewish soul. Even one letter missing makes the whole Torah invalid - showing how important each Jew is!":
        "למדו על מצוות כתיבת ספר תורה! ✍️ לכל יהודי מצווה לכתוב (או להשתתף בכתיבת) ספר תורה. יש שאומרים שאפשר לקיים היום בקניית חומש, תנ\"ך וספרים קדושים. כל אות בתורה — נשמה יהודית. אות אחת חסרה — פוסלת את כל הספר. כך חשוב כל יהודי!",
    "Crown yourself with Torah! 👑 The Rambam teaches that a Jewish king must write TWO Torah scrolls - one for his treasury and one that never leaves his side! Here's the amazing lesson: No matter how powerful or busy you are, Torah should be your constant companion! Did you know? The king would read from his Torah scroll every day, teaching us to make daily Torah study a priority! Your mission: Set a fixed time for Torah study, even if it's just a few minutes.":
        "כתרו את עצמכם בתורה! 👑 הרמב\"ם: מלך יהודי כותב שני ספרי תורה — אחד לאוצר ואחד שלא יעזוב אותו. לא משנה כמה עסוקים — התורה צריכה להיות לצידכם. ידעתם? המלך קרא כל יום — גם אנחנו קובעים זמן קבוע, אפילו דקות ספורות.",
    "Gather for inspiration! 👥 Learn about the special mitzvah of Hakhel! Every seven years, the entire nation - men, women, and children - would gather to hear the king read from the Torah! Here's something amazing: Even babies were brought, and their parents received special reward for bringing them! The lesson? Sometimes just being present in a holy gathering can elevate your soul! Today's mission: Join a Torah class or gathering, even if online.":
        "התכנסו להשראה! 👥 מצוות הקהל — כל שבע שנים כל העם נאסף לשמוע את המלך קורא בתורה. מדהים: הביאו גם תינוקות — והורים זכו בשכר. לפעמים רק נוכחות בכינוס קדוש מרוממת את הנשמה. משימה להיום: הצטרפו לשיעור תורה או מפגש, גם אונליין.",
    "Discover the letter Zayin (ז)! ✡ This powerful letter looks like a crown with a sword pointing down! Did you know? Zayin means 'weapon' in Hebrew, teaching us that Torah is our spiritual weapon against negativity! Its numerical value is 7, connecting to Shabbat, the 7th day. Here's something amazing: The Zayin's shape teaches us to crown our physical actions with spiritual purpose! Take a moment today to elevate a physical activity with holy intention.":
        "גלו את האות זין (ז)! ✡ נראית כמו כתר עם חרב — זין פירושו «נשק», והתורה הנשק הרוחני שלנו נגד השליליות. גימטריה 7 — שבת, היום השביעי. צורתה מלמדת לכתר פעולות גשמיות בכוונה קדושה. היום — רוממו פעילות גשמית בכוונה.",
    "Learn about the prohibition of idol worship! 🛡️ The Rambam explains something fascinating: This isn't just about statues - it's about anything we put before G-d in our lives! Here's something amazing: When we resist the urge to worship money, fame, or other false gods, we testify that G-d is the only true power! Today's challenge: Identify something you might be making too important in your life, and take a step to put G-d first.":
        "למדו על איסור עבודה זרה! 🛡️ הרמב\"ם: לא רק פסלים — כל מה ששמים לפני ה'. כשמתנגדים לפולחן כסף, תהילה או אלים שקריים — מעידים שה' הכוח האמיתי היחיד. אתגר היום: זהו משהו שאולי חשוב מדי — וקחו צעד לשים את ה' ראשון.",
    "Guard your home from forbidden things! 🎨 The Rambam teaches in Hilchot Avodat Kochavim that we must be careful about certain types of images: Protruding human figures such as statues are forbidden, and we also can't make images of celestial bodies or angels at all! While some authorities extend these prohibitions to children's dolls, many are lenient. Today's mission: Be mindful of keeping your home free from problematic images!":
        "שמרו על הבית מדברים אסורים! 🎨 הרמב\"ם בהלכות עבודה זרה: זהירות בתמונות — פסלים בולטים אסורים, וגם גופים שמימיים או מלאכים. חלק מחמירים בבובות; רבים מקילים. משימה להיום: שימו לב שהבית נקי מתמונות בעייתיות.",
    "Discover the letter Tet (ט)! ✡ This amazing letter represents goodness - it's the first letter that appears in the Torah in the word 'Tov' (good)! Did you know? Its shape is curved inward, teaching us that true goodness is often hidden and modest. The numerical value is 9, representing the 9 months of pregnancy - a time when hidden goodness grows! Take a moment today to notice the hidden blessings in your life.":
        "גלו את האות ט (ט)! ✡ האות הראשונה בתורה במילה «טוב». צורתה כפופה פנימה — טוב אמיתי לעתים חבוי וצנוע. גימטריה 9 — תשעה חודשי הריון, זמן שבו הטוב הנסתר גדל. היום — שימו לב לברכות הנסתרות.",
    "Explore the power of Yud (י)! ✡ This tiniest letter holds incredible wisdom! Its small size teaches humility, yet it's the first letter of G-d's holy name! Did you know? The Yud represents a single point, teaching that even the smallest good deed can have infinite impact! Its numerical value is 10, connecting to the Ten Commandments.":
        "גלו את כוח האות יוד (י)! ✡ האות הקטנה ביותר — ענווה, וגם האות הראשונה בשם הקדוש. יוד = נקודה אחת: גם מעשה טוב קטן — השפעה אינסופית. גימטריה 10 — עשרת הדיברות.",
    "Uncover the mysteries of Kaf (כ)! 👑 In Jewish mysticism, the Kaf represents Keter (crown) - but not the crown of pride! Did you know? The bent shape of the Kaf (כ) represents bowing in submission, while its numerical value (20) represents the age when the pursuit of wisdom begins. The final Kaf (ך) teaches that true achievement comes through humility.":
        "גלו את סוד האות כף (כ)! 👑 בקבלה — כתר, לא כתר גאווה. צורת כף כפופה — כריעה והכנעה. גימטריה 20 — גיל תחילת מרדף החוכמה. כף סופית — הישג אמיתי דרך ענווה.",
    "Guard your unique identity! 👕 The Rambam teaches in Chapter 11 of Hilchot Avodat Kochavim that we shouldn't imitate non-Jewish customs in dress or behavior. Here's something fascinating: This isn't about superiority, but about maintaining our unique spiritual identity! Did you know? Even seemingly neutral customs might need to be avoided if they have idolatrous origins. Today's mission: Take pride in your Jewish distinctiveness.":
        "שמרו על הזהות הייחודית! 👕 הרמב\"ם בפרק י\"א בהלכות עבודה זרה: לא לחקות מנהגים לא יהודיים בלבוש והתנהגות. לא עליונות — על שמירת זהות רוחנית. ידעתם? גם מנהגים «נייטרליים» עשויים להימנע אם יש להם שורש עבודה זרה. משימה להיום: גאווה בייחודיות היהודית!",
    "Rise above superstition! 🎲 The Rambam explains in Chapter 10 that we shouldn't base decisions on 'lucky signs' or omens. Here's something powerful: Instead of relying on superstitions, we should trust in G-d! Did you know? Even if a sign 'works' multiple times, we still shouldn't rely on it. Today's challenge: Make decisions based on wisdom and faith, not superstition.":
        "עלו מעל אמונות תפלות! 🎲 הרמב\"ם בפרק י': לא לבסס החלטות על «סימנים» או מזל. במקום כישוף — ביטחון בה'. ידעתם? גם אם «סימן» עובד — לא סומכים עליו. אתגר היום: החלטות מחוכמה ואמונה, לא מאמונות תפלות.",
    "Be an Amen enthusiast! 🗣️ Did you know? Saying Amen to a blessing is confirming that what the first person said is true - the letters alef (א), mem (מ), nun (נ) are an acronym for G-d, King, who is faithful. Confirm that G-d is great and the sages say you steal the show! The Talmud says responding Amen is greater than making the blessing itself. Today's mission: Keep your ears open for blessings and be ready with your spiritual encore - don't miss an Amen! And remember, you're confirming that G-d is great and He does what He says He will do!":
        "תהיו חובבי אמן! 🗣️ ידעתם? אמן על ברכה — אישור שהדברים נכונים. א-מ-ן: אל מלך נאמן. הגמרא: עונים אמן גדול ממברך! משימה להיום: אוזניים פתוחות לברכות — אל תפספסו אמן! אתם מאשרים שה' גדול ונאמן.",
    "Celebrate the gift of joy! Take a moment to thank G-d for your ability to laugh and smile 😊. Whether it's from a good joke, time with friends, or a funny moment - laughter is G-d's special medicine for the soul. Did you know? The Talmud tells us that a person who brings joy to others is rewarded greatly! 🎭":
        "חגגו את מתנת השמחה! קחו רגע להודות לה' על היכולת לצחוק ולחייך 😊. בדיחה טובה, חברים או רגע מצחיק — צחוק הוא תרופת הנשמה. ידעתם? הגמרא: מי שמביא שמחה לאחרים — מתוגמל בגדול! 🎭",
    "Connect through prayer! 🙏 Did you know? Prayer is our spiritual Wi-Fi connection to G-d! Here's something beautiful: The blessings are prescribed by the sages, who knew how to open heavenly gates using specific words and phrases. By just saying the words in a siddur, our prayers descend to unfathomable heights! Today's mission: Choose one prayer to say with extra concentration and feeling.":
        "התחברו דרך תפילה! 🙏 ידעתם? התפילה היא חיבור ה-Wi-Fi הרוחני שלנו לה'! יפה: הברכות נקבעו על ידי החכמים שידעו לפתוח שערי שמיים במילים וביטויים מדויקים. רק באמירת המילים בסידור — התפילות עולות לגבהים בלתי נתפסים! משימה להיום: בחרו תפילה אחת לומר עם ריכוז ורגש מיוחדים.",
    "Prepare for the ultimate harvest parade! 🍇 Learn about Bikkurim, the first-fruit offering that was one of the most emotional and high-energy ceremonies in the entire Temple service. When a farmer saw the very first ripening fruits of the Seven Species—wheat, barley, grapes, figs, pomegranates, olives, and dates—he would tie a reed around them to mark them as holy. 🎀 The journey to Jerusalem was a nationwide block party! 🥳 Pilgrims traveled together in massive groups, and as they entered the city, the local artisans and leaders would stop their work to stand up and welcome them! 🏛️ The streets filled with music from the flutes of the Levi'im as the parade headed toward the Temple Mount. The coolest part is the declaration the farmer made while holding his basket. He would recount the history of the Jewish people from the Exodus until that very moment of standing on his own land. 📜 Rich farmers brought their fruits in baskets of gold and silver, while the poor used simple wicker, but":
        "התכוננו למצעד הקציר הגדול! 🍇 למדו על ביכורים — מנחת הביכורים, אחת הטקסים המרגשים ביותר בבית המקדש. כשחקלאי ראה את הפירות הראשונים של שבעת המינים — חיטה, שעורה, ענבים, תאנים, רימונים, זיתים ותמרים — קשר קנה סביבם לסימון קדושה. 🎀 המסע לירושלים היה חגיגה ארצית! 🥳 עולי רגל בקבוצות ענק, וכשנכנסו לעיר — אומנים ומנהיגים עמדו לקבלם! 🏛️ הרחובות התמלאו בחלילי הלויים בדרך להר הבית. החלק הכי מגניב: ההכרזה שהחקלאי אמר עם הסל — סיפור עם ישראל מיציאת מצרים ועד הרגע הזה על אדמתו. 📜 עשירים הביאו בסלי זהב וכסף, עניים בסלים פשוטים — אבל",
}


def has_foreign_garbage(text: str) -> bool:
    """Spanish / wrong-language values wrongly stored under he."""
    foreign = (
        "¿Lo sabías", "Sé un", "¡Celebra", "Conéctate", "Descubre", "Misión de hoy",
        "Lo sabías", "¿Sabías", "¡Aprecio", "Antes de comer", "Camina en",
    )
    return any(m in text for m in foreign)


def is_alert_key(k: str) -> bool:
    if not EMOJI_PAT.search(k):
        return False
    if ALERT_PAT.search(k):
        return True
    return "!" in k and len(k) > 80


def is_bad_he(text: str) -> bool:
    if not text:
        return True
    if HYBRID.search(text):
        return True
    if LATIN_IN_HE.search(text):
        for w in re.findall(r"[A-Za-z]{3,}", text):
            if w not in ALLOWED_LATIN:
                return True
    if any(m in text for m in MT_MARKERS):
        return True
    if has_foreign_garbage(text):
        return True
    # Mostly non-Hebrew alert body (Spanish shard leaked into he).
    he_chars = len(re.findall(r"[\u0590-\u05ff]", text))
    if he_chars < 20 and len(text) > 40:
        return True
    return False


def load_pool() -> dict[str, str]:
    pool: dict[str, str] = {}
    for path in sorted(HUMAN.glob("he_fix_*.json")):
        pool.update(json.loads(path.read_text(encoding="utf-8")).get("he", {}))
    for name in (
        "he_glue_fixes.json", "he_hybrid_purge.json", "educational_he_overrides.json",
        "local_005.json",
    ):
        p = HUMAN / name
        if p.is_file():
            pool.update(json.loads(p.read_text(encoding="utf-8")).get("he", {}))
    pool.update(json.loads(HEBREW_FIXES.read_text(encoding="utf-8")).get("he", {}))
    all_manual = {
        **MANUAL_HE,
        **MANUAL_HE_BATCH50,
        **MANUAL_HE_BATCH100,
        **MANUAL_HE_BATCH29,
        **MANUAL_HE_BATCH70,
    }
    pool.update(all_manual)
    return pool


def iter_alert_candidates() -> list[tuple[str, str]]:
    """(key, current_he) pairs from human shards + mitzvah_alert_tone."""
    seen: set[str] = set()
    items: list[tuple[str, str]] = []
    for src in SOURCES:
        p = HUMAN / src
        if not p.is_file():
            continue
        data = json.loads(p.read_text(encoding="utf-8"))
        for k, he in data.get("he", {}).items():
            if k in seen or not is_alert_key(k):
                continue
            seen.add(k)
            items.append((k, he))
    # Also scan shipped mitzvah_alert_tone for keys still needing rewrite.
    mt_path = HUMAN / "mitzvah_alert_tone.json"
    if mt_path.is_file():
        mt = json.loads(mt_path.read_text(encoding="utf-8")).get("he", {})
        for k, he in mt.items():
            if k in seen or not is_alert_key(k):
                continue
            if is_bad_he(he):
                seen.add(k)
                items.append((k, he))
    return items


def main() -> None:
    pool = load_pool()
    all_manual = {
        **MANUAL_HE,
        **MANUAL_HE_BATCH50,
        **MANUAL_HE_BATCH100,
        **MANUAL_HE_BATCH29,
        **MANUAL_HE_BATCH70,
    }
    out: dict[str, str] = {}

    mitz_path = HUMAN / "mitzvah_alert_tone.json"
    mitz_keys = set()
    if mitz_path.is_file():
        mitz_keys = set(json.loads(mitz_path.read_text(encoding="utf-8")).get("he", {}))

    # Hand-authored overrides always win for Mitzvah Me alert keys.
    for k, fixed in all_manual.items():
        if mitz_keys and k not in mitz_keys:
            continue
        if fixed and not is_bad_he(fixed):
            out[k] = fixed

    for k, he in iter_alert_candidates():
        if k in out:
            continue
        if not is_bad_he(he):
            continue
        fixed = pool.get(k)
        if fixed and not is_bad_he(fixed):
            out[k] = fixed
        elif k in pool and pool[k] != k:
            # Prefer hebrew_fixes / glue over raw mitzvah_alert_tone phrase swaps.
            candidate = pool[k]
            if not is_bad_he(candidate):
                out[k] = candidate

    OUT.write_text(
        json.dumps({"he": out}, ensure_ascii=False, indent=2) + "\n",
        encoding="utf-8",
    )
    print(f"Wrote {len(out)} alert tone fixes to {OUT.name}")


if __name__ == "__main__":
    main()
