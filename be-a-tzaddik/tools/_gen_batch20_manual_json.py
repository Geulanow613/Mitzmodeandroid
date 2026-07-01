#!/usr/bin/env python3
"""Generate _ru_batch20_manual.json — fix hybrid Latin-in-Cyrillic transliterations."""
from __future__ import annotations

import json
from pathlib import Path

KEYS_PATH = Path(__file__).parent / "_batch20_keys.json"
OUT = Path(__file__).parent / "_ru_batch20_manual.json"

_LEVI = (
    "Леви — племя, служившее в Храме; сегодня левиим часто вызывают к Торе после коэним. "
    "Многие носят фамилию Леви или связанные традиции. У коэним и левиим есть особые галахот "
    "(биркат ха-коэним, тума). Если не уверены в статусе — спросите семью и рава."
)
_LEVI_DASH = (
    "леви — племя Храма; сегодня левиим читают Тору после коэним. "
    "Другие галахот: биркат ха-коэним, тума. Спросите семью и рава."
)
_MATANA = "матана аль менат ле-ха-хзир — дар с условием возврата"
_MATANA_LONG = (
    "матана аль менат ле-ха-хзир (מַתָּנָה עַל מְנָת לְהַחְזִיר) — «дар с условием возврата». "
    "Получатель владеет мгновенно, затем возвращает. В первый день Суккота — для лулава и этрога. "
    "Не для мехират хамец."
)
_MECHIRAT = (
    "мехират хамец — продажа хамеца нееврею через рава до Песаха. Оформите заранее; "
    "абсолютная продажа, не матана аль менат ле-ха-хзир. Проданный хамец — убрать. После Песаха — по договору рава."
)
_YAALEH = (
    "«Яале вьяво» — в Амиду и Биркат а-Мазон в Рош Ходеш, Йом Тов и Холь ха-Моэд.\n\n"
    "Если забыли в Шахарит/Минхе: в «Рецей» — вставьте; после «Рецей» — вернитесь к началу; "
    "после «Йихию ле-рацон» — повторите только эту Амиду.\n\n"
    "Маарив Рош Ходеш: после «Рецей» или всей Амиды — не повторяйте (Берахот 30б; О.Х. 422:1)."
)
_MAARIV_FORGOT = (
    "Если забыли на Маариве в Рош Ходеш:\n"
    "• Ещё в «Рецей» до Имени — вставьте «Яале вьяво» и продолжайте (О.Х. 422:1).\n"
    "• После «Рецей» или всей Амиды — не возвращайтесь и не повторяйте; месяц освящён днём (Берахот 30б)."
)
_MAARIV_FULL = (
    "В Амиде Маарива в Рош Ходеш добавьте «Яале вьяво» в «Рецей».\n\n"
    "Если забыли — см. правила выше (О.Х. 422:1; Берахот 30б).\n\n"
    "При еде с хлебом добавьте «Яале вьяво» и в Биркат а-Мазон."
)
_MAARIV_ADD = (
    "В Амиде Маарива в Рош Ходеш добавьте «Яале вьяво» в благословение «Рецей».\n\n"
    "Если забыли:\n"
    "• Ещё в «Рецей» до Имени — вставьте и продолжайте (О.Х. 422:1).\n"
    "• После «Рецей» или всей Амиды — не возвращайтесь и не повторяйте; месяц освящён днём (Берахот 30б).\n\n"
    "При еде с хлебом добавьте «Яале вьяво» и в Биркат а-Мазон."
)
_YAD = (
    "яд соледет бо — «когда рука отдёргивается» — порог бишула в Шаббат, ~43–45 °С. "
    "Разогреть сухую готовую еду иногда можно; жидкости строже. Блех держит тёплым без готовки."
)
_YAD_BARE = (
    "Яд соледет бо («когда рука отдёргивается») — порог бишула в Шаббат, ~43–45 °С. "
    "Разогреть сухую готовую еду иногда можно; жидкости строже. Блех держит тёплым без готовки."
)

MANUAL: dict[str, str] = {
    "Connect through prayer! 🙏 Did you know? Prayer is our spiritual Wi-Fi connection to G-d! Here's something beautiful: The blessings are prescribed by the sages, who knew how to open heavenly gates using specific words and phrases. By just saying the words in a siddur, our prayers descend to unfathomable heights! Today's mission: Choose one prayer to say with extra concentration and feeling.": (
        "Соединись через молитву! 🙏 Тефила — наша духовная связь с В-гом, словно вай‑фай с Небом. "
        "Мудрецы задали благословения словами, открывающими небесные врата. Произнеси слова из сидура — "
        "и молитва взлетает на невообразимую высоту! Задание: выбери одну молитву с особой сосредоточенностью."
    ),
    "alot hashachar — Alot hashachar is halachic dawn — when the sky begins to lighten. Many morning laws start here, such as the beginning of public fast days. It is before sunrise (netz). It is not the normal time for the bracha on tallit and tefillin — see misheyakir. In great need, Igros Moshe (O.C. 4:6) allows putting them on after alot hashachar without a bracha and reciting the brachos after misheyakir — ask your rav.": (
        "алот ха-шахар — галахический рассвет, когда небо начинает светлеть; начало общественных постов; до нэц (восхода). "
        "Браха на талит и тфилин — после мишейакир; в нужде — Игрот Моше О.Х. 4:6, спросите рава."
    ),
    "Learn about Losh — kneading and forming a mass from separate ingredients! 🍞 Think dough: flour + water + mixing = one unified blob. In the Mishkan, Losh was a key agricultural step used when mixing water with the ground herb powders to make dyes, as well as kneading the flour to bake the Showbread. On Shabbat, this melacha prohibits combining a finely divided powder or small solid particles with a liquid to form a single, cohesive paste or thick mass. This restriction directly affects how people prepare everyday items like baby cereal, instant oatmeal, mustard, or even certain dense textures of egg salad. To avoid a Torah-level violation, the halacha requires using specific workarounds when a paste must be made, such as changing the normal order of ingredients (for instance, pouring the liquid into the bowl before adding the powder) and mixing the ingredients with an unusual motion (Shinui), like employing a crisscross slicing movement with a knife instead of a standard circular stir. This is why Shabbat challah is baked before the day of rest begins, since the creative act of unifying separate elements into a brand new entity belongs entirely to Friday, leaving us to enjoy the completed creation on Shabbat. Learn more!": (
        "Лош — замешивание и образование единой массы из отдельных ингредиентов! 🍞 В Мишкане — смешение красок и замес хлеба "
        "для ле-хем ха-паним. В Шаббат запрещено соединять порошок с жидкостью в однородную пасту. "
        "Для каши, горчицы и т.п. — меняйте порядок (сначала жидкость) и способ (шинуи). "
        "Хала пекут в пятницу — творческое объединение элементов остаётся для будней."
    ),
    'The Shulchan Aruch ("Set Table") is the classic 16th-century code of halacha (Jewish Law) by Rabbi Yosef Karo. Ashkenazim often study it with the Rema\'s glosses; Sephardim generally follow Rabbi Karo, but rabbis have debated about almost everything throughout the years.': (
        "Шулхан Арух («Покрытый стол») — классический кодекс галахи 16-го века раввина Йосефа Каро. "
        "Ашкеназы изучают с глоссами Рема; сефарды обычно следуют Каро."
    ),
    'Shulchan Aruch — The Shulchan Aruch ("Set Table") is the classic 16th-century code of halacha (Jewish Law) by Rabbi Yosef Karo. Ashkenazim often study it with the Rema\'s glosses; Sephardim generally follow Rabbi Karo, but rabbis have debated about almost everything throughout the years.': (
        "Шулхан Арух — классический кодекс галахи 16-го века раввина Йосефа Каро. Ашкеназы — с глоссами Рема; сефарды — обычно Каро."
    ),
    "When:\nIn the Northern Hemisphere, say it during Nissan when fruit trees blossom (Shulchan Arukh O.C. 226:1). In the Southern Hemisphere, say it when local fruit trees blossom in Elul–Tishrei. Set your city or GPS in Settings so the app shows this item in the correct season for you.": (
        "Когда:\nСеверное полушарие — Нисан при цветении (О.Х. 226:1). Южное — Элул–Тишрей. "
        "Укажите город или геолокацию в настройках."
    ),
    "This app is a learning companion, not a rabbi — it does not give halachic rulings.\n\nThis checklist does not contain all the mitzvot in the entire Torah. It covers standard daily mitzvot that observant Jews commonly practice — waking, prayer, blessings, meals, Torah study, Shabbat preparation, and similar foundations.\n\nWith your permission, the app uses GPS or a city you choose to calculate Jewish calendar times and when you can fulfill different mitzvot throughout the day (for example morning, afternoon, and evening prayer windows, candle lighting, and Shabbat-related times). Location is kept on your device only for zmanim and the calendar.\n\nIf you are new to Judaism, take it slow and do what you can. Build steady habits without overwhelm, and always ask an Orthodox rabbi you trust when something is unclear or when your situation needs personal guidance.": (
        "Это приложение — спутник обучения, не раввин; оно не выносит галахических решений.\n\n"
        "Список не охватывает все мицвот Торы — только ежедневные основы: пробуждение, молитва, благословения, еда, Тора, подготовка к Шаббату.\n\n"
        "С вашего разрешения приложение использует геолокацию или выбранный город для зманим и календаря; данные остаются на устройстве.\n\n"
        "Новичкам — не спешите; спрашивайте доверенного ортодоксального рава, когда нужна личная консультация."
    ),
    "Call someone who is lonely. Submitted by: Amy. Our sages warn never to separate yourself from the community — because a Jew needs connection to thrive. Visiting the sick removes one-sixtieth of their suffering, and loneliness can be its own kind of illness. A phone call, a message, or a visit tells someone they matter — and loving your fellow Jew is one of the foundations of everything we stand for.": (
        "Позвоните одинокому человеку. От Эми. Мудрецы предупреждают: не отделяйтесь от общины — еврею нужна связь. "
        "Посещение больного снимает одну шестидесятую страдания; одиночество — своя болезнь. "
        "Звонок или визит говорит человеку, что он важен."
    ),
    "Call someone who is lonely.\n\nSubmitted by: Amy": "Позвоните кому-то одинокому.\n\nОт Эми",
    "levi — Levi is the tribe serving in the Temple; today leviim are often called to the Torah after kohanim. Many carry the family name Levi or related traditions. Kohanim and leviim have other halachot (priestly blessing, tumah). If unsure of your status, ask family and your rav.": _LEVI_DASH,
    "Levi is the tribe serving in the Temple; today leviim are often called to the Torah after kohanim. Many carry the family name Levi or related traditions. Kohanim and leviim have other halachot (priestly blessing, tumah). If unsure of your status, ask family and your rav.": _LEVI,
    "kisui rosh": "кисуй рош",
    "simanim — Simanim are symbolic Rosh Hashana foods — apple in honey, pomegranate, fish head, dates — each with a pun or prayer for the new year. They are minhag, not Torah law, but beloved for teaching children. Say the short yehi ratzon prayers from the machzor.": (
        "симаним — символические продукты Рош ха-Шана: яблоко в мёде, гранат, рыбья голова, финики — с «йе-хи рацон». "
        "Минхаг, не закон Торы; любим для детей."
    ),
    "Nusach Ari is the prayer rite associated with Rabbi Isaac Luria (the Ari) and used by Chabad and some other communities. It blends Ashkenazi and Sephardi elements. Chabad siddurim such as Tehillat Hashem print this nusach. If you daven Nusach Ari, use that siddur consistently for festival inserts.": (
        "Нусах Ари — обряд рава Ицхака Лурии; у Хабада и других общин. Смешивает ашкеназ и сефард. "
        "Используйте сиддур «Техилат ха-Шем» последовательно для праздничных вставок."
    ),
    "Pray for world peace! - submitted by MM. The prophets describe the days of Mashiach as a time when nations will beat their swords into plowshares and war will be no more. Hillel taught us to be among the disciples of Aaron — a lover of peace and a pursuer of peace, who draws people closer to Torah through kindness. Our sages even teach that peace is so precious that G-d allows His holy Name to be erased in the bitter waters of the sotah ritual in order to restore peace between a husband and wife. When you pray for peace, you align your heart with one of the highest purposes of Torah.": (
        "Молитесь о мире! — от М.М. Пророки: мечи перекуют в плуги. Хиллель: будьте учениками Аарона — "
        "любите мир и гоняйтесь за миром."
    ),
    "Transform your worries into prayers! 🕊️ Rabbi Nachman of Breslov taught a revolutionary approach to anxiety: instead of letting worried thoughts spiral, *transform* them into heartfelt conversations with Hashem through Hitbodedut! Whatever is bothering you – big or small, trivial or overwhelming – speak to G-d about it in your own words, out loud if possible. This powerful practice takes negative, draining energy and redirects it toward spiritual growth, profound trust, and a deeper connection to the Divine. Try it right now: pick one worry that's on your mind and talk it through with Hashem. You'll be amazed at the clarity and peace it brings! Source: Likutei Moharan II, 25; Rabbi Nachman's Teachings.": (
        "Превратите тревоги в молитвы! 🕊️ Рабби Нахман: через хитбодедут говорите с Б-гом своими словами о том, что беспокоит. "
        "Источник: Ликутей Мохаран, ч. 2, 25."
    ),
    "Remember this powerful truth: There is absolutely NO despair in the world! 💪 Rabbi Nachman of Breslov's most famous declaration — 'Ein shum ye'ush ba'olam klal!' — is a game-changer. It means that no matter how far someone feels they've fallen, no matter how many mistakes they've made, return to Hashem is always possible. Every challenge has a solution, every misstep can be rectified, and spiritual renewal is always just a moment away. When discouragement strikes, repeat this phrase aloud in Hebrew or English and let it infuse you with strength and hope. Rabbi Nachman based this on deep Kabbalistic insights: despair presumes your situation is permanent, but that fundamentally misunderstands the fluid, dynamic reality of Divine mercy. Redemption can literally happen in the blink of an eye! Source: Likutei Moharan II, 78; Rabbi Nachman's Teachings.": (
        "Помните: в мире нет отчаяния! 💪 «Эйн шум еуш ба-олам клаль!» — рабби Нахман. "
        "Возвращение к Б-гу всегда возможно. Источник: Ликутей Мохаран, ч. 2, 78."
    ),
    "Pray for Moshiach!!!!!!! - submitted by EV. Belief in the coming of Mashiach is one of the thirteen foundations of Jewish faith — we anticipate a king from the house of David who will rebuild the Temple, gather the exiles, and fill the world with knowledge of G-d. Our sages teach that a Jew must live every day with the expectation of redemption. When you pray for Mashiach, you express hope for a world of peace, holiness, and the complete revelation of G-d's presence.": (
        "Молитесь о Машиахе! — от Е.В. Вера в Машиах — один из 13 столпов веры. "
        "Живите в ожидании геулы — мира, кедуши и полного раскрытия присутствия В-га."
    ),
    "Clean your bathroom extra spick and span in honor of Shabbat. - Submitted by AS. Our sages teach that preparing for Shabbat is itself a mitzvah — and that whoever toils on Friday will enjoy the rewards on Shabbat. A beautiful midrash describes how two angels accompany a person home from synagogue on Friday night: one good, one bad. If they find the home ready — candles lit, table set— the good angel blesses the home for the next Shabbat to be the same and the bad angel is forced to say 'amen'. Honoring Shabbat means making the whole home feel like a palace for the King.": (
        "Тщательно уберите ванную в честь Шаббата. — А.С. Подготовка к Шаббату — мицва. "
        "Мидраш: два ангела сопровождают домой из синагоги — если дом готов, добрый ангел благословляет, злой вынужден сказать «амен». "
        "Сделайте дом дворцом для Царя."
    ),
    "mechirat chametz — Mechirat chametz is selling chametz you want to keep to a non-Jew through your rabbi before Pesach so it is not yours during the holiday. Authorize the sale well before Erev Pesach — most rabbis stop accepting forms by the night before, even though ownership transfers on Erev Pesach morning. It must be an absolute sale, not a conditional gift (matana al menat lehachzir). Chametz sold should be stored away. When including year-round dishes, rabbis typically sell only the absorbed chametz flavor in the vessels — not the physical dishes themselves — to avoid requiring tevilat kelim after Pesach. Shortly after Pesach, ownership reverts per the contract your rabbi uses.": _MECHIRAT,
    "matana al menat lehachzir": _MATANA,
    "Yaknehaz": "якнэаз",
    "Minhag": "минхаг",
    "Learn about the special Kiyor (washbasin) of the Temple! 🚰 Did you know? The Kohanim had to wash their hands and feet at the same time by placing their right hand on their right foot and left hand on their left foot! This unique position reminded them to be fully dedicated to service - from head to toe! Plus, the water had to be fresh each day, teaching us about starting each day's divine service with renewal.": (
        "Кийор Храма! 🚰 Коэним мыли руки и ноги одновременно: правая рука на правой ноге, левая — на левой. "
        "Полная преданность служению. Вода каждый день свежая — обновление в служении."
    ),
    "Hafrashat challah is separating a small portion of dough when baking a large amount of bread or similar flour-based foods, then burning or discarding in many customs. It recalls the Temple-era gift to the kohen (when they were in a state of ritual purity and able to eat it).": (
        "Хафрашат хала — отделение куска теста при выпечке большого количества хлеба; сжигают или выбрасывают; "
        "напоминание о дарах коэним в Храме."
    ),
    "Birkat Kohanim": "биркат ха-коэним",
    "pareve": "парве",
    "Yaaleh V'Yavo is a special prayer paragraph inserted into the Amidah (the standing silent prayer) and into Birkat HaMazon (Grace After Meals) on Rosh Chodesh (the new month), Yom Tov (festivals), and Chol HaMoed (intermediate festival days).\n\nThe text asks G-d to remember us, our fathers, Jerusalem, the Davidic dynasty, and the entire Jewish people for good — for life and peace — on the day being observed.\n\nIf forgotten in Shacharit or Mincha (also Chol HaMoed / Yom Tov at any Amidah including Maariv):\n• Still in Retzei — insert Yaaleh V'yavo in its place and continue.\n• After concluding Retzei — return to the beginning of Retzei, insert Yaaleh V'yavo, and complete the remaining blessings.\n• After the final Yihiyu L'ratzon — repeat only that Amidah (Shemoneh Esrei), never the full service.\n\nRosh Chodesh Maariv only: if forgotten after finishing Retzei, or after the entire Amidah — do not repeat (Berachot 30b; Shulchan Arukh O.C. 422:1). If still in Retzei before God's name at the conclusion, insert it there.": _YAALEH,
    "Rosh Chodesh (ראש חודש) — the New Month — is a semi-holiday with extra prayer and customs.\n\nFestive meal (mitzvah):\n• It is a mitzvah to increase your meal on Rosh Chodesh — at minimum add an extra dish or special food in honor of the day (Shulchan Arukh O.C. 419:1).\n• Have the meal during the day. Poskim write this commemorates the feast the Sanhedrin held at Beit Ya'zek for witnesses who came to testify they saw the new moon (Mishnah Rosh Hashanah 2:5; Orchos Chaim and Kol Bo, cited on O.C. 419).\n• Money spent on Rosh Chodesh meals — like Shabbat and Yom Tov — is not deducted from the income allotted to you on Rosh Hashanah; if you spend more for these mitzvos, Heaven adds to your allotment (Pesikta de-Rav Kahana, cited in Tur O.C. 419 and Magen Avraham 419:1).\n\nDavening today (listed in your Morning, Afternoon, and Evening Prayer sections):\n• If you recite Shacharit, Mincha, or Maariv Amidah — add Yaaleh V'yavo in Retzei. Shacharit/Mincha: correct per timing (insert in Retzei, return to the beginning of Retzei if already concluded, or repeat only that Amidah if finished). Maariv on Rosh Chodesh only: do not repeat if forgotten after Retzei (Berachot 30b; SA O.C. 422:1)\n• If you say Birkat Hamazon when you eat bread — add Yaaleh V'yavo there too\n• Ashkenazi custom — most authorities obligate Shacharit and Mincha Amidah on these days\n• Sephardic custom — many women fulfill the daily obligation with one prayer; if you daven an extra Amidah and forget Yaaleh V'yavo, ask your rabbi about a voluntary (nedavah) stipulation\n• Half Hallel at Shacharit if you say Hallel (optional — follow your minhag; Full Hallel if Rosh Chodesh falls during Chanukah)\n• Tachanun is omitted all day\n\nOther customs:\n• Fasting and eulogies are generally not done on Rosh Chodesh\n• Widespread custom: married women refrain from certain melacha (needlework, laundry, etc.) as an extra mark of honor — ask your rav for details\n\nWhen Rosh Chodesh spans two days (30th of the previous month and 1st), observances apply to both days.": (
        "Рош Ходеш (ראש חודש) — полупраздник с дополнительными молитвами.\n\n"
        "Праздничная трапеза: добавьте блюдо в честь дня (О.Х. 419:1); днём.\n\n"
        "Молитва: «Яале вьяво» в «Рецей» и в Биркат а-Мазон; полу-Галель по минхагу; без Тахануна. "
        "Маарив: если забыли после «Рецей» — не повторяйте (Берахот 30б). Спросите рава о деталях."
    ),
    "Next time you have guests, keep in mind to escort them on their way as they leave, even for just a few steps. The Talmud (Sotah 46b) says the reward for escorting others has no measure!": (
        "Провожайте гостей несколько шагов при уходе — Талмуд (Сота 46б): награда без меры!"
    ),
    "If you recite the Maariv Amidah on Rosh Chodesh, add Yaaleh V'yavo in the blessing Retzei (Avodah).\n\nIf you forgot at Maariv on Rosh Chodesh:\n• Still in Retzei before God's name at the conclusion — insert Yaaleh V'yavo there and continue (Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10).\n• Once you finished Retzei, or after the entire Amidah — do not go back and do not repeat. Beit Din sanctified the new month by day, not at night (Berachot 30b; Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10). Continue davening.\n\nIf you say Birkat Hamazon when you eat bread tonight, add Yaaleh V'yavo there too.": _MAARIV_FULL,
    "If you forgot at Maariv on Rosh Chodesh:\n• Still in Retzei before God's name at the conclusion — insert Yaaleh V'yavo there and continue (Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10).\n• Once you finished Retzei, or after the entire Amidah — do not go back and do not repeat. Beit Din sanctified the new month by day, not at night (Berachot 30b; Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10). Continue davening.": _MAARIV_FORGOT,
    "Got some spare time? Visit someone who's sick. The Talmud (Nedarim 39b) says this removes 1/60th of their illness!": (
        "Есть время? Навестите больного — Талмуд (Недарим 39б): снимает 1/60 болезни!"
    ),
    "Add Yaaleh V'yavo in the Maariv Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).\n\nIf you forgot at Maariv on Rosh Chodesh:\n• Still in Retzei before God's name at the conclusion — insert Yaaleh V'yavo there and continue (Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10).\n• Once you finished Retzei, or after the entire Amidah — do not go back and do not repeat. Beit Din sanctified the new month by day, not at night (Berachot 30b; Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10). Continue davening.\n\nAlso add Yaaleh V'yavo in bentching if you eat bread tonight.": _MAARIV_ADD,
    "Serve Hashem with Simcha (Joy)! 😊 This isn't just a nice idea; Rabbi Nachman of Breslov (Likutei Moharan I, 24) teaches that performing mitzvot with joy elevates them to the highest spiritual levels, connecting them to the Keter (the 'crown' of creation)! He emphasized that joy is the key to spiritual breakthroughs and moving forward in life when you feel stuck. Think of it: a mitzvah done with a smile, song, or even a little dance is far more impactful! Your mission today: choose one mitzvah and perform it with extra happiness – whether it's giving tzedakah with a cheerful heart, singing during Torah study, or truly celebrating after prayer. Rabbi Nachman taught that joy isn't optional; it's how we access profound spiritual growth and closeness to Hashem! Source: Likutei Moharan I, 24; Rabbi Nachman's Teachings.": (
        "Служите В-гу с радостью! 😊 Рабби Нахман (Ликутей Мохаран, ч. 1, 24): мицвот с радостью возносятся к кетеру творения. "
        "Радость — ключ к прорыву, когда застряли. Задание: выполните одну мицву с особой радостью."
    ),
    'In a Jewish leap year, 14 Adar I is Purim Katan ("little Purim"). The full Purim mitzvot — Megillah, mishloach manot, matanot l\'evyonim, and the festive meal — apply only on regular Purim (14 Adar, or 15 Adar in walled cities).\n\nPurim Katan is marked by a slightly more festive tone; some communities add a special meal or omit Tachanun. It is not a substitute for Purim observance.': (
        "В високосный год 14 адара алеф — Пурим катан. Полные мицвот Пурима — только 14/15 адара. "
        "Чуть более праздничный тон; не заменяет Пурим."
    ),
    "Elevate your day: Express gratitude out loud to Hashem! 🙏 Rabbi Nachman of Breslov and countless sages teach that actively thanking Hashem for *everything* – even challenges – is a profound spiritual practice. It transforms harsh judgments into mercy and opens spiritual gates to blessings! Right now, try saying 'Thank You, Hashem!' for three *specific* things in your life. Be detailed: instead of a general 'thanks for food,' try 'Thank You, Hashem, for the delicious and nourishing breakfast I had this morning that gave me energy, and for all the amazing flavors infused in it!' Speaking your gratitude aloud creates a powerful spiritual channel and shifts your entire perspective. Source: Likutei Moharan I, 79; Rabbi Nachman's Teachings.": (
        "Выражайте благодарность вслух! 🙏 Рабби Нахман: благодарность за всё превращает строгость в милость. "
        "Скажите «Спасибо, Б-г!» за три конкретные вещи. Источник: Ликутей Мохаран, ч. 1, 79."
    ),
    "Discover the Tikkun HaKlali — Rabbi Nachman's powerful ten psalms! 📖 Rabbi Nachman of Breslov revealed a specific spiritual remedy known as the Tikkun HaKlali (General Rectification), consisting of ten chapters of Tehillim (Psalms 16, 32, 41, 42, 59, 77, 90, 105, 137, 150). He taught that this special collection has immense power to rectify the soul, particularly in cases of spiritual impurity or distress. What's truly remarkable? Rabbi Nachman stated that the revelation of this unique remedy was 'a very wonderful and awesome rectification' that had remained hidden since the time of Creation! You can recite these psalms anytime you need spiritual cleansing, strength, or a profound connection to Hashem. Source: Likutei Moharan I, 205; Rabbi Nachman's Teachings.": (
        "Откройте Тиккун ха-Клали — десять псалмов рабби Нахмана! 📖 Псалмы 16, 32, 41, 42, 59, 77, 90, 105, 137, 150. "
        "Сила исправления души. Источник: Ликутей Мохаран, ч. 1, 205."
    ),
    'yad soledet bo — Yad soledet bo ("when the hand recoils") is the heat threshold for bishul on Shabbat — roughly 110–113°F (43–45°C). Food already fully cooked may sometimes be reheated dry on Shabbat; liquids are stricter. This is why a blech keeps food warm without reaching cooking temperature.': _YAD,
    'Yad soledet bo ("when the hand recoils") is the heat threshold for bishul on Shabbat — roughly 110–113°F (43–45°C). Food already fully cooked may sometimes be reheated dry on Shabbat; liquids are stricter. This is why a blech keeps food warm without reaching cooking temperature.': _YAD_BARE,
}


def main() -> None:
    keys: list[str] = json.loads(KEYS_PATH.read_text(encoding="utf-8"))
    missing = [k for k in keys if k not in MANUAL]
    extra = [k for k in MANUAL if k not in keys]
    if missing:
        raise SystemExit(f"missing ({len(missing)}): {missing[0][:80]}...")
    if extra:
        raise SystemExit(f"extra ({len(extra)}): {extra[0][:80]}...")
    ordered = {k: MANUAL[k] for k in keys}
    OUT.write_text(json.dumps(ordered, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {len(ordered)} entries to {OUT.name}")


if __name__ == "__main__":
    main()
