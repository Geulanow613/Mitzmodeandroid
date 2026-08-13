#!/usr/bin/env python3
"""Write human/ru_explainer_polish.json — native RU for dynamic checklist explainers."""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "data" / "translation-catalog" / "human" / "ru_explainer_polish.json"

RU: dict[str, str] = {
    "Sefirat HaOmer links Pesach to Shavuot — counting each day from the Exodus toward receiving the Torah.\n\nToday in the Omer: $todaySummary (day $day of 49).\n\nTonight's count:\n• $tonight night — count $todaySummary after nightfall$timePart.\n$nextNightLine\n\nHow to count:\n• Stand and recite the blessing before counting if you are still saying it with a blessing (if you missed a day, ask your rabbi before continuing with a bracha).\n• Say: \"$speechPhrase\"\n• Count after nightfall (tzeit); complete before dawn. If you forgot at night, count the next day during the daytime without a bracha. If you do this before sunset, you can continue counting on subsequent nights with a bracha. You only lose the blessing permanently if you miss an entire 24-hour cycle (both night and the following day) — ask your rav.\n\n$nusachWhen": (
        "Сефират ха-Омер связывает Песах со Шавуот — счёт каждого дня от Исхода до принятия Торы.\n\n"
        "Сегодня в Омере: $todaySummary (день $day из 49).\n\n"
        "Счёт этой ночи:\n• Ночь $tonight — считайте $todaySummary после цейт$timePart.\n"
        "$nextNightLine\n\n"
        "Как считать:\n"
        "• Встаньте и произнесите браху перед счётом, если ещё говорите её с благословением "
        "(если пропустили день — спросите рава).\n"
        "• Скажите: «$speechPhrase»\n"
        "• Считайте после цейт; завершите до рассвета. Если забыли ночью — считайте днём без брахи. "
        "Если успели до заката, в следующие ночи можно снова с брахой. Браху теряют навсегда только "
        "при пропуске полного 24-часового цикла (ночь и следующий день) — спросите рава.\n\n"
        "$nusachWhen"
    ),
    "Chanukah night $day of 8 — lighting the menorah.\n\nWhen:\n• Light after tzeit (nightfall) — not before sunset. On Friday, light Chanukah candles before Shabbat candles (approx. 20–25 minutes before nightfall).\n• Friday candle size warning: Because Chanukah candles must burn for at least 30 minutes after nightfall (tzeit), standard small Chanukah candles cannot be used on Friday afternoon — they will burn out before nightfall and invalidate the mitzvah. Use larger candles (like standard Shabbat candles) or pour enough oil to burn for at least ~90 minutes from lighting (often longer if you light well before sunset) so they survive well past dark.\n• On Motzei Shabbat, light Chanukah before or after Havdalah per minhag.\n• Motzei Shabbat lighting: If your custom is to light Chanukah candles before formal Havdalah over wine, you must terminate Shabbat first — recite Atah Chonantanu in the Maariv Amidah, or say \"Baruch hamavdil bein kodesh l'chol\" aloud before striking a match. Melacha remains forbidden until Shabbat has ended.\n\nHow:\n• Pirsumei nisa — publicize the miracle; place menorah by a window or doorway where passersby can see.\n• Placement: Insert candles from right to left in the menorah (universal custom per Shulchan Arukh O.C. 676:5 — Ashkenaz, Sephard, and Chabad).\n• Lighting: Light from left to right, always starting with the newest candle (the leftmost one) and moving toward the right. Use the shamash to light each candle.\n• Oil or candles must burn at least 30 minutes after nightfall.\n• Brachot (first night all three; other nights two): lehadlik ner shel Chanukah, she'asa nissim; Shehecheyanu on first night.\n• Do not use the Chanukah lights for work — shamash is for utility light.\n\nPrayers & meals:\n• Insert Al HaNissim into every Amidah and into Birkat Hamazon (bentching) all day long.\n\n$brachotNote\n\nAfter lighting: sing HaNeiros halalu and Maoz Tzur (custom).": (
        "Ночь $day Хануки из 8 — зажигание меноры.\n\n"
        "Когда:\n"
        "• Зажигайте после цейт — не до заката. В пятницу зажигайте свечи Хануки до свечей Шаббата "
        "(примерно за 20–25 минут до наступления темноты).\n"
        "• В пятницу: свечи Хануки должны гореть минимум 30 минут после цейт — используйте большие свечи "
        "или достаточно масла (~90 минут от зажжения).\n"
        "• В моцаеи Шаббата зажигайте Хануку до или после Хавдалы по минхагу. Если до Хавдалы — "
        "сначала завершите Шаббат: «Ата хонантану» в Амиде Маарив или «Барух хамавдил» вслух.\n\n"
        "Как:\n"
        "• Пирсумей ниса — поставьте менору у окна или у двери.\n"
        "• Вставляйте свечи справа налево; зажигайте слева направо, начиная с новой (крайней слева). "
        "Используйте шамаш.\n"
        "• Брахот (в первую ночь три; в остальные две): ле-хадлик нер шель Ханука, ше-аса нисим; "
        "Шехехияну в первую ночь.\n\n"
        "Молитва и трапезы:\n• Вставляйте «Ал ха-Ниссим» в каждую Амиду и в Биркат а-Мазон весь день.\n\n"
        "$brachotNote\n\n"
        "После зажигания: «Ха-Неирот халалу» и «Маоз Цур» (минхаг)."
    ),
    "Kiddush Levana (Sanctification of the New Moon) — Birkat HaLevanah. Men are obligated in this time-bound positive mitzvah; women are exempt and the widespread custom is that women do not recite it at all (see Deracheha link below).\n\n$waitLine\n\nDeadline: The window ends at the moment of the full moon (roughly 14 days, 18 hours, and 22 minutes from the molad — about 14.75 days into the month). Saying it on the night of the 15th may already be too late depending on the month. This app uses the Hebrew calendar day as a rough guide only — always check Sof Zman Kiddush Levana for your location before the month ends.\n\nWhen:\n• After nightfall (tzeit), standing outdoors under the open sky.\n• Ideally on Motzei Shabbat while still in nice clothes — a widespread custom because you are already dressed up.\n• In Av, many wait until after Tisha B'Av; in Tishrei, many wait until after Yom Kippur — but say sooner on Motzei Shabbat if waiting risks cloudy nights.\n• Not on Shabbat or Yom Tov itself.\n\nHow:\n• The moon must be clearly visible — if clouds block it, wait for a clear night within the window.\n• Use your siddur; it praises G-d for creation — not worship of the moon.\n\nWindow closes at the full moon (~14.75 days from the molad). Check Sof Zman Kiddush Levana for your location.": (
        "Кидуш левана (Биркат ха-Левана). Мужчины обязаны; женщины освобождены, и распространённый минхаг — "
        "не произносить (см. Deracheha).\n\n$waitLine\n\n"
        "Срок: окно закрывается в полнолуние (~14,75 дней от молада).\n\n"
        "Когда: после цейт, на улице под открытым небом; желательно в моцаеи Шаббата; не в Шаббат и не в Йом Тов.\n\n"
        "Как: луна должна быть хорошо видна; используйте сидур."
    ),
    "Ashkenazi custom observes a longer, stricter mourning throughout the Three Weeks, intensifying during the Nine Days.\n\nFrom 17 Tammuz (general Three Weeks):\n• Haircuts & shaving: prohibited for the entire Three Weeks.\n• Music: instrumental music is not listened to throughout the period.\n• Weddings: not held.\n• Shehecheyanu: traditionally not recited on new clothes or new fruits; permitted on Shabbat.\n\nFrom 1 Av (Nine Days): restrictions intensify — see the Nine Days checklist item for meat, wine, laundry, bathing, and home practices.": (
        "Ашкеназский минхаг: продлённый траур на протяжении Трёх недель, усиливается в Девять дней.\n"
        "17 Тамуза: стрижка, музыка и свадьбы запрещены; Шехехияну избегают (разрешено в Шаббат).\n"
        "С 1 Ава: см. пункт о Девяти днях."
    ),
    "Sephardic and Edot HaMizrach communities, following Shulchan Arukh, generally take a more lenient approach than Ashkenazim during the early Three Weeks.\n\nFrom 17 Tammuz (general Three Weeks):\n• Haircuts & shaving: permitted during most of the Three Weeks; shaving is usually prohibited only during the week in which Tisha B'Av falls (shavuah she'chal bo).\n• Music: live or recorded music is avoided.\n• Weddings: some communities avoid weddings from 17 Tammuz; others are lenient and avoid them only from Rosh Chodesh Av — follow your kehilla.\n• Shehecheyanu: avoided on new items for the duration of the period.\n\nFrom Rosh Chodesh Av or the week of Tisha B'Av: additional restrictions apply — see the Nine Days checklist item. Some communities (e.g. Syrian, Mashadi) are stricter on meat and wine from Rosh Chodesh Av.": (
        "Сефарды и Эдот а-Мизрах: более мягко до недели 9 Ава.\n"
        "Стрижка разрешена большую часть периода; музыку избегают; свадьбы — по общине."
    ),
    "Chabad follows strict Ashkenazi mourning customs, with specific emphasis from the Lubavitcher Rebbe on spiritual growth during this period.\n\nFrom 17 Tammuz (general Three Weeks):\n• Haircuts, music & weddings: prohibited throughout the entire Three Weeks.\n• Shehecheyanu: avoided entirely, except on Shabbat or when required for a mitzvah (e.g. brit milah).\n• Torah & charity: increase Torah study — especially subjects about the Holy Temple's layout and construction — and give extra tzedakah during this time.\n\nFrom Rosh Chodesh Av (Nine Days): restrictions intensify — see the Nine Days checklist item for meat, wine, laundry, bathing, and home practices.": (
        "Хабад — строгий ашкеназский минхаг.\n"
        "Стрижка, музыка и свадьбы запрещены; больше Торы и цдаки; усиливается в Девять дней с Рош Ходеш Ав."
    ),
    "The Nine Days (from 1 Av until after Tisha B'Av) are the strictest part of summer mourning in Ashkenazi custom.\n\nFrom 1 Av:\n• Meat & wine: forbidden entirely, except on Shabbat or at a seudat mitzvah (e.g. brit milah, siyum — ask your rav).\n• Laundry: washing, ironing, and wearing freshly laundered outer clothing are prohibited.\n• Bathing: bathing or showering for pleasure is prohibited.\n• Home & shopping: buying new clothes or beginning major home improvements or repairs is forbidden.": (
        "Девять дней (с 1 Ава) — строгий ашкеназский траур: мясо, вино, стирка, купание для удовольствия, покупки.\n"
        "9 Ава: пост; кинот.\nПосле поста: ограничения до хацот 10 Ава."
    ),
    "The Nine Days and the week of Tisha B'Av (shavuah she'chal bo) are the strictest part of summer mourning for Sephardic communities following Shulchan Arukh — generally more lenient than Ashkenazim until the week of Tisha B'Av.\n\nFrom Rosh Chodesh Av (1 Av):\n• Haircuts: usually prohibited from Rosh Chodesh Av, or only during the actual week of Tisha B'Av — follow your rav.\n\nFrom the week in which Tisha B'Av falls (shavuah she'chal bo):\n• Meat & wine: prohibited from the start of that week (not necessarily the full Nine Days). Some communities (e.g. Syrian, Mashadi) are strict from Rosh Chodesh Av.\n• Laundry & bathing: restrictions on washing clothes and bathing for pleasure apply during the week of Tisha B'Av.": (
        "Девять дней и неделя 9 Ава — сефарды: строже в шавуа ше-халь бо.\n"
        "Мясо, вино, стирка и купание по минхаг; спросите рава."
    ),
    "The Nine Days (from Rosh Chodesh Av until after Tisha B'Av) follow strict Ashkenazi mourning in Chabad practice.\n\nFrom Rosh Chodesh Av:\n• Meat & wine: prohibited entirely, except on Shabbat or at a seudat mitzvah (e.g. brit milah, siyum).\n• Laundry, bathing & home: traditional Ashkenazi prohibitions against laundering, bathing for pleasure, home improvements, and buying new garments are observed.": (
        "Девять дней в Хабаде — мясо и вино запрещены с Рош Ходеш Ав; стирка и купание для удовольствия.\n"
        "После поста: по псаку Хабада до хацот 10 Ава."
    ),
    "After the fast (10 Av): Ashkenazi custom continues meat, wine, music, laundry, and bathing for pleasure until chatzos (halachic midday) on 10 Av.\n\nNine Days Havdalah: On Motzei Shabbat during the Nine Days, use wine or grape juice for Havdalah. Ashkenazi custom (Rema O.C. 551:10): ideally a child (ages 6–9) drinks the cup; if none is present, the one reciting Havdalah drinks it — the mitzvah of Havdalah overrides the custom of restraint.": (
        "После поста (10 Ава): ашкеназский обычай продлевает запрет на мясо, вино, музыку, стирку и купание "
        "для удовольствия до хацот (галахического полудня) 10 Ава.\n\n"
        "Хавдала в Девять дней: в моцаеи Шаббата используйте вино или сок; в идеале чашу выпивает ребёнок (6–9 лет)."
    ),
    "After the fast: many Sephardim follow similar post-fast restrictions until chatzos on 10 Av — confirm with your rav before resuming meat and wine.": (
        "После поста: многие сефарды соблюдают похожие ограничения до хацот 10 Ава — уточните у рава."
    ),
    "After the fast (10 Av): follow accepted Chabad psak on meat, wine, music, laundry, and bathing until chatzos on 10 Av — ask your rav if unsure.\n\nNine Days Havdalah: use wine or grape juice; Ashkenazi-style custom often gives the cup to a child (ages 6–9) when possible.": (
        "После поста (10 Ава): следуйте принятому псаку Хабада о мясе, вине, музыке, стирке и купании "
        "до хацот 10 Ава — спросите рава при сомнении.\n\n"
        "Хавдала в Девять дней: вино или сок; ашкеназский обычай часто даёт чашу ребёнку (6–9 лет)."
    ),
}


_ROMANCE_POLISH_LOGICAL = (
    "ilanot", "hachamah", "kiddush",
    "arba_men", "arba_fem",
    "mechirat", "mechirat_urgency", "mechirat_short",
    "bedikat", "bedikat_no_sched", "bedikat_short",
    "biur", "seder",
    "on_shabbat_body", "fri_shabbat_body", "fri_13",
    "lead_shabbat", "lead_friday",
    "lead_bed_thu_tomorrow", "lead_bed_thu_tonight",
    "lead_bed_fri_tomorrow", "lead_bed_fri_tonight",
)


def main() -> None:
    from _explainer_keys import resolve_keys
    from _explainer_romance_data import translations

    ru = dict(RU)
    keys = resolve_keys()
    tr = translations("ru")
    for logical in _ROMANCE_POLISH_LOGICAL:
        ru[keys[logical]] = tr[logical]

    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps({"ru": ru}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {len(ru)} RU explainer polish entries to {OUT.name}")


if __name__ == "__main__":
    main()
