#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Generate rest_e_batch3.py with translations for indices 96-143."""

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parent
KEYS = json.loads((ROOT / "_overlay_missing.json").read_text(encoding="utf-8"))[96:144]

def T(he, es, fr, ru):
    return (he, es, fr, ru)

DATA = {
    KEYS[0]: T(
        "Elokei Neshama modah la'Hashem baboker al hachzarat ha'neshamah, she'hayetah tehorah ve'tachzir ba'mavet. Hu ba'achar Birchot HaShachar b'rabim mi'ha'siddurim. Ha'tefillah melamedet she'kol yom hu halva'ah chadasha shel chayim — hasivu oto le'mitzvot, lo rak la'sidurim.",
        "Elokei Neshama agradece a D-s por la mañana por restaurar el alma, que era pura y será devuelta a la muerte. Sigue a Birchot HaShachar en muchos sidurim. La oración enseña que cada día es un nuevo préstamo de vida — úsalo para mitzvot, no solo recados.",
        "Elokei Neshama remercie D. le matin de restaurer l'âme, pure et rendue à la mort. Il suit Birchot HaShachar dans beaucoup de siddourim. La prière enseigne que chaque jour est un nouveau prêt de vie — utilisez-le pour les mitzvot, pas seulement les courses.",
        "Elokei Neshama blagodarit V-ga utrom za vozvrashchenie dushi, chistoy i vozvrashchaemoy pri smerti. Sleduet za Birchot HaShachar vo mnogikh siddurakh. Molitva uchит: kazhdyy den — novyy zayem zhizni; ispolzuyte dlya mitzvot, ne tolko del.",
    ),
    KEYS[1]: T(
        "Peninei Halakha — Peninei Halakha hu seder halacha b'Ivrit moderna shel haRav Eliezer Melamed (zamin gam be'Anglit ba'internet), me'urgan le'fi nusha — Shabbat, chagim, tefillah, mishpacha veYisrael. Barur, me'uyan ve'nifutz le'she'elot ma'asiyot.",
        "Peninei Halakha — Peninei Halakha es la serie moderna de halajá en hebreo del Rabí Eliezer Melamed (disponible en inglés en línea), organizada por tema — Shabat, fiestas, oración, familia e Israel. Es clara, con fuentes y muy usada para preguntas prácticas.",
        "Peninei Halakha — Peninei Halakha est la série moderne de halakha en hébreu du Rav Eliezer Melamed (disponible en anglais en ligne), organisée par sujet — Chabbat, fêtes, prière, famille et Israël. Claire, sourcée et largement utilisée pour les questions pratiques.",
        "Peninei Halakha — Peninei Halakha — sovremennaya ivritskaya seriya halakhi Rava Eliezera Melameda (dostupna na angliyskom onlayn), po temam — Shabbat, prazdniki, molitva, semya i Izrail. Yasnaya, s istochnikami, shiroko ispolzuetsya dlya prakticheskikh voprosov.",
    ),
    KEYS[2]: T(
        "Simanim hem ma'achalei siman leRosh Hashana — tapuach bidvash, rimon, rosh dag, tamarim — kol echad im kalut o tefillah le'shanah chadasha. Hem minhag, lo din miTorah, aval ahuvim le'lamed yeladim. Omru et tefillot yehi ratzon ha'ktzarot min ha'machzor.",
        "Simanim son alimentos simbólicos de Rosh Hashaná — manzana en miel, granada, cabeza de pescado, dátiles — cada uno con juego de palabras u oración para el año nuevo. Son minhag, no ley de Torá, pero queridos para enseñar a niños. Di las breves oraciones yehi ratzon del majzor.",
        "Simanim sont les aliments symboliques de Roch Hachana — pomme au miel, grenade, tête de poisson, dattes — chacun avec un jeu de mots ou prière pour la nouvelle année. Ce sont des minhag, pas une loi de Torah, mais chers pour enseigner aux enfants. Dites les courtes prières yehi ratzon du mahzor.",
        "Simanim — simvolicheskie rosh-hashana produkty — yabloko s medom, granat, rybinaya golova, finiki — kazhdyy s kalamburom ili molitvoy za novyy god. Eto minhag, ne zakon Tory, no lyubimy dlya obucheniya detey. Govorite kratkie yehi ratzon iz mahzora.",
    ),
    KEYS[3]: T(
        "Kitzur Shulchan Arukh hu kitzur halacha yom yomit — tefillah, Shabbat, kashrut, chagim u'mishpacha — she'katav haRav Shlomo Ganzfried. Sefer halacha rishon prakti le'matchilim ve'referencia mahera le'Yehudim vatikim.",
        "El Kitzur Shuljan Aruj es un resumen condensado de halajá diaria — oración, Shabat, kashrut, fiestas y vida familiar — escrito por el Rabí Shlomo Ganzfried. Es un primer libro práctico de halajá para principiantes y referencia rápida para judíos experimentados.",
        "Le Kitzour Choulhan Aroukh est un résumé condensé de halakha quotidienne — prière, Chabbat, cacherout, fêtes et vie familiale — écrit par le Rav Shlomo Ganzfried. C'est un premier livre pratique de halakha pour débutants et une référence rapide pour les Juifs expérimentés.",
        "Kitzur Shulchan Arukh — szhatoe izlozhenie povsednevnoy halakhi — molitva, Shabbat, kashrut, prazdniki i semya — Rava Shlomo Ganzfried. Prakticheskaya pervaya kniga halakhi dlya nachinayushchikh i bystraya spravka dlya opytnykh.",
    ),
    KEYS[4]: T(
        "Chutz la'aretz mashma michutz la'Eretz Yisrael. Ha'halacha shonah b'chlk tzadim — yom sheni shel Yom Tov, chukei chaklaut ve'chlk mi'tekstei tefillah. Yisraelim be'chutz la'aretz l'rov shomrim minhag ha'makom be'bikur; le'shihyah arukah sha'alu et ha'rav.",
        "Chutz la'aretz significa fuera de la Tierra de Israel. La halajá difiere en algunas áreas — segundo día de Yom Tov, ciertas leyes agrícolas y algunos textos de oración. Los israelíes en chutz la'aretz suelen seguir la práctica local al visitar; pregunta a tu rav para estancias largas.",
        "Houts la'arets signifie hors de la Terre d'Israël. La halakha diffère sur certains points — second jour de Yom Tov, lois agricoles et textes de prière. Les Israéliens à l'étranger suivent souvent la pratique locale en visite ; demandez à votre rav pour un long séjour.",
        "Chutz la'aretz oznachaet vne Eretz Yisrael. Halakha razlichayetsya — vtoroy den Yom Tov, nekotorye selskohozyaystvennye zakony i teksty molitv. Izrailety v galute chasto sleduyut mestnoy praktike v gostyakh; sprosite rava pri dolgikh stayakh.",
    ),
    KEYS[5]: T(
        "Hayom Yom (\"Hayom...\") hu luach Chabad shel limudim ktzarim — amirah, sippur o halacha le'chol yom ba'shanah. L'rov n'lmd ke'chelek miChitas yom yom achar Chumash, Tehillim veTanya. Ha'inyanim ktzarim u'ma'asiyim, le'luchot zmanim עמוסים.",
        "Hayom Yom (\"Hoy es…\") es un calendario Jabad de enseñanzas breves — un dicho, historia o ley para cada día del año. A menudo se estudia como parte del Chitas diario tras Jumash, Tehillim y Tanya. Las entradas son cortas y prácticas, para horarios ocupados.",
        "Hayom Yom (« Aujourd'hui… ») est un calendrier Habad d'enseignements brefs — une citation, histoire ou loi pour chaque jour de l'année. Souvent étudié dans le Chitas quotidien après 'Houmash, Tehilim et Tanya. Les entrées sont courtes et pratiques, pour emplois du temps chargés.",
        "Hayom Yom («Segodnya…») — khasidskiy kalendar kratkikh ucheniy na kazhdyy den goda. Chasto chast ezhednevnogo Chitas posle Humash, Tehillim i Tanya. Zapisi korotkie i praktichnye dlya zanyatykh.",
    ),
    KEYS[6]: T(
        "Tefilat Nedavah hi Amidah reshit mitnadvet k'she'ein tashlumin — im tenai ba'lev ve'chiddush katan ba'tefillah. Hi lo mechila chova ke'tashlumin aval derech lehitpalel achar hachmataot. Lo le'shimmush yom yomi kal.",
        "Tefilat Nedavah es una Amidá voluntaria cuando tashlumin es imposible — con estipulación mental y pequeña novedad en la oración. No reemplaza la obligación como tashlumin pero ofrece forma de orar tras múltiples omisiones. No para uso casual diario.",
        "Tefilat Nedavah est une Amidah volontaire quand le tachloumim est impossible — avec stipulation mentale et petite nouveauté dans la prière. Elle ne remplace pas l'obligation comme tachloumim mais permet de prier après plusieurs oublis. Pas pour un usage quotidien casual.",
        "Tefilat Nedavah — dobrovolnaya Amidah, kogda tashlumin nevozmozhen — s mentalnym usloviem i nebolshoy noviznoy. Ne zamenyaet obyazannost kak tashlumin, no pozvolyaet molitsya posle propuskov. Ne dlya povsednevnogo legkomyslennogo ispolzovaniya.",
    ),
    KEYS[7]: T(
        "chazeret — Chazeret hu ha'maror ha'sheni ba'ke'arah baSeder b'chlk minhagim — l'rov chazeret chasah — yachad im maror. Lo kol kehillah meshameshet chazeret nifrad; yesh ha'omrim rak chrain le'maror. L'fi haHaggadah u'minhag ha'mishpacha.",
        "chazeret — Chazeret es la segunda hierba amarga en el plato del Seder en algunas tradiciones — a menudo lechuga romana — junto al maror. No toda comunidad usa chazeret separado; algunos usan solo rábano picante para maror. Sigue tu Hagadá y costumbre familiar.",
        "chazeret — Chazeret est la deuxième herbe amère sur l'assiette du Seder dans certaines traditions — souvent une tige de laitue romaine — aux côtés du maror. Toutes les communautés n'utilisent pas un chazeret séparé ; certains n'utilisent que du raifort pour le maror. Suivez votre Hagada et coutume familiale.",
        "chazeret — Chazeret — vtoraya gorkaya trava na seder-tarelke v nekotorykh traditsiyakh — chasto salat romen — ryadom s maror. Ne vse obshchiny ispolzuyut otdelnyy chazeret; nekotorye tolko khren dlya maror. Sleduyte Hagade i semeynomu obychayu.",
    ),
    KEYS[8]: T(
        "העריכu את מתנת המנוחה! הודu לה' על היכולת לישון ולהתעורר רעננים 😴. דמיינu אם לא היינו יכולים לטעון את הסוללות כל לילה! במקום זאת, ה' נתן לנו כפתור reset יומי מדהים שעוזר לגוף ולנפש 🌅",
        "¡Aprecia el regalo del descanso! Agradece a D-s por poder dormir y despertar renovado 😴. ¡Imagina si no pudiéramos recargar baterías cada noche! En cambio, D-s nos dio este increíble botón de reinicio diario que ayuda a cuerpo y mente 🌅",
        "Appréciez le don du repos ! Remerciez D. de pouvoir dormir et vous réveiller reposé 😴. Imaginez si nous ne pouvions pas recharger nos batteries chaque nuit ! D. nous a donné ce bouton de reset quotidien qui aide corps et esprit 🌅",
        "Tsените dar otdykha! Blagodarite V-ga za son i svezhiy probuzhdenie 😴. Predstavte, esli by nelzya bylo podzaryaditsya kazhdyy vecher! V-mesto etogo V-s dal nam ежедневную knopku reset dlya tela i uma 🌅",
    ),
    KEYS[9]: T(
        "Kabbalah hi ha'mesoret ha'mistit ha'Yehudit ha'chokeret et yachasei ha'Hashem la'beriah, la'neshamah u'la'mitzvot. Sifrei Kabbalah k'mo haZohar veTanya. Kabbalah autentit n'lmda im yedi'at Torah u'moreh; hi lo nevu'ah o kishuf.",
        "Kabbalah es la tradición mística judía que explora cómo D-s se relaciona con la creación, el alma y las mitzvot. Textos incluyen el Zóhar y obras como Tanya. La Cábala auténtica se estudia con conocimiento de Torá y un maestro; no es adivinación ni magia.",
        "Kabbalah est la tradition mystique juive explorant comment D. se rapporte à la création, l'âme et les mitzvot. Textes incluent le Zohar et des œuvres comme Tanya. La Kabbale authentique s'étudie avec connaissance de Torah et un enseignant ; ce n'est ni divination ni magie.",
        "Kabbalah — evreyskaya mistическая traditsiya o tom, kak V-s otnositsya k tvoreniyu, dushe i mitzvot. Teksty: Zohar, Tanya. Podlinnaya Kabbala izuchaetsya s znaniyem Tory i uchitelem; eto ne gadaniye i ne magiya.",
    ),
    KEYS[10]: T(
        "Kosher mashma kasher le'fi halacha — bifrat ochel, aval gam sifrei kodesh u'ma'asim kasherim (l'mashal kesef kasher le'mitzvot). Kosher ein tov shel bri'ut; hu kashrut ruchanit. Be'safek al ma'achal, telefonu la'hechsher o sha'alu et ha'rav.",
        "Kosher significa apto según halajá — especialmente comida, pero también rollos válidos y conducta aceptable (p. ej. dinero kosher para mitzvot). Kosher no es etiqueta de salud; es aptitud espiritual. En duda sobre un producto, llama a la hechsher o consulta a tu rav.",
        "Kasher signifie apte selon la halakha — surtout la nourriture, mais aussi les rouleaux valides et la conduite acceptable (p. ex. argent casher pour mitzvot). Casher n'est pas un label santé ; c'est une aptitude spirituelle. En cas de doute, appelez la hekhsher ou consultez votre rav.",
        "Kosher oznachaet godnost po halakhe — osobenno eda, no takzhe svitki i priemlemoe povedenie (napr. koshernye dengi dlya mitzvot). Kosher — ne zdorovyy label; eto dukhovnaya godnost. Pri somnenii zvonite hekhsheru ili sprosite rava.",
    ),
    KEYS[11]: T(
        "Pirsumei nisa mashma le'farsem et ha'nes — hanachat menorat Chanukah b'makom she'ovrim ro'im et ha'nerot. Nes ha'shemen mufraס ba'rchov. B'batiim, rabim madlikim ba'chalon. Al tishtamshu b'nerot mitzvah le'kri'ah — hashav et ha'shamash.",
        "Pirsumei nisa significa publicar el milagro — colocar la menorá de Janucá donde los transeúntes vean las luces. El milagro del aceite se proclama a la calle. En casas, muchos encienden en la ventana. No uses las velas de mitzvá para leer — usa el shamash.",
        "Pirsoumei nissa signifie publiciser le miracle — placer la menorah de Hanoucca où les passants voient les lumières. Le miracle de l'huile est proclamé dans la rue. Chez soi, beaucoup allument à la fenêtre. N'utilisez pas les bougies de mitzvah pour lire — utilisez le shamash.",
        "Pirsumei nisa oznachaet oglašenie chuda — postavit hanukah-menoru tam, gde prokhozhie vidят svet. Chudo masla provozglasheno na ulitse. Doma mnogie zazhigayut u okna. Ne ispolzuyte svechi mitzvah dlya chteniya — ispolzuyte shamash.",
    ),
    KEYS[12]: T(
        "psak — Psak hu psak halacha — ha'teshuvah she'posek barur nosen le'mikre shelkha ha'amiti, lo l'vivuch te'oreti. BeYahadut yesh range legitimi bein rabbanim; tachlichem le'kabel psak ha'rav shelkhem be'emtza ve'lo le'chapes kulot.",
        "psak — Psak es una decisión halájica — la respuesta que un posek calificado da a tu caso real, no un debate teórico. El judaísmo tiene rangos legítimos entre autoridades; tu tarea es seguir el psak de tu rav consistentemente en lugar de buscar leniencias.",
        "psak — Psak est une décision halakhique — la réponse qu'un posek qualifié donne pour votre cas réel, pas un débat théorique. Le judaïsme a des écarts légitimes entre autorités ; votre rôle est de suivre le psak de votre rav de façon cohérente plutôt que de chercher des assouplissements.",
        "psak — Psak — halakhicheskoye reshenie — otvet kvalifitsirovannogo poseka na vash realnyy sluchay, a ne teoreticheskiy spor. V iudaizme est dopustimye razlichiya mezhdu autoritetami; vasha zadacha — posledovatelno sledovat psak vashego rava, a ne iskat poslableniya.",
    ),
    KEYS[13]: T(
        "Bitachon hu bitachon ba'Hashem — le'ha'amin she'Hu nosein mah she'at tzarich ve'she'kashim yecholim lihyot im tachlit afilu k'she'ein ro'im. Kshur le'emunah aval me'aged shalvat ba'dagah yom yomit. Musar veChasidut shneihem melamedim bitachon ma'asi.",
        "Bitajón es confianza en D-s — creer que Él provee lo que necesitas y que la dificultad puede tener propósito aunque no lo veas. Relacionado con emuná pero enfatiza calma en la preocupación diaria. Musar y Jasidut enseñan bitajón práctico.",
        "Bitachon est la confiance en D. — croire qu'Il pourvoit à vos besoins et que l'épreuve peut avoir un sens même invisible. Lié à l'emouna mais insiste sur le calme face aux soucis quotidiens. Moussar et Hassidout enseignent un bitachon pratique.",
        "Bitachon — doverie k V-gu — vera, chto On dayot nuzhnoe i chto trudnosti mogut imet smysl, dazhe esli ne vidno. Svyazano s emunah, no podcherkivaet spokoystvie v povsednevnykh zabotakh. Musar i Khasidut uchат prakticheskomu bitachon.",
    ),
    KEYS[14]: T(
        "Pesach mechazek et Yetziat Mitzrayim — matzah, maror, Seder ve'ein chametz. Chag ha'cherut veha'emunah. Ha'hachana domenet erev Pesach: bedikat, biur, hashgacha. Ha'tachlit ha'ruchanit — lihyot she'yatzata miMitzrayim, lo rak zikaron histori.",
        "Pesaj conmemora el Éxodo — matzá, maror, Seder y sin chametz. Es la festividad de libertad y fe. La preparación domina erev Pesaj: bedikat, biur, kashering. La meta espiritual es sentir que saliste de Egipto personalmente, no solo memoria histórica.",
        "Pessah commémore l'Exode — matza, maror, Seder et pas de 'hamets. C'est la fête de liberté et de foi. La préparation domine erev Pessah : bedikat, biour, cachérisation. Le but spirituel est de sentir que vous êtes sorti d'Égypte personnellement, pas seulement mémoire historique.",
        "Pesach otmechaet Iskhod — matzah, maror, Seder i bez chametz. Prazdnik svobody i very. Podgotovka dominiruet v erev Pesach: bedikat, biur, kashering. Dukhovnaya tsel — chuvstvovat, chto vy lichno vyshli iz Egipta, a ne tolko istoricheskaya pamyat.",
    ),
    KEYS[15]: T(
        "HaAmidah (\"tefillat amida\") hi lev kol tefillah — nikra gam Shmoneh Esrei (shem esre, achshav tsha-esre brachot k'she'nosfu achat). Ne'emra be'lachash u've'emtza, el Yerushalayim o Har haBayit (im beYerushalayim), raglayim tzuvu.",
        "La Amidá (oración \"de pie\") es el núcleo de cada servicio — también llamada Shmoneh Esrei (dieciocho, ahora diecinueve bendiciones). Se recita en silencio de pie, hacia Jerusalén o el Monte del Templo (si estás en Jerusalén), con los pies juntos.",
        "L'Amidah (prière « debout ») est le cœur de chaque office — aussi appelée Chemoné Esrei (dix-huit, maintenant dix-neuf bénédictions). Récitée en silence debout, face à Jérusalem ou au Mont du Temple (à Jérusalem), pieds joints.",
        "Amidah («stoyachaya» molitva) — yadro kazhdoy sluzhby — takzhe Shmoneh Esrei (vosemnadtsat, teper devyatnadtsat brakhot). Chitaetsya tiho stoya, litsom k Ierusalimu ili Khrámu (v Ierusalime), nogi vmeste.",
    ),
    KEYS[16]: T(
        "leining — Leining hu kriat haparsha min haSefer Torah im trop m'duyak. Ha'baal koreh mit'amen chodshim. Ha'tzibur omdim im Chumash. Shmi'at kol milah beShabbat baboker mekayemet mitzvat kriat haTorah ba'tzibur.",
        "leining — Leining es cantar la porción de Torá del rollo con trop preciso (cantilación). El baal kore entrena meses. Los feligreses siguen en un Jumash. Escuchar cada palabra el Shabat por la mañana cumple la mitzvá comunitaria de lectura pública de Torá.",
        "leining — Leining consiste à lire la portion de Torah du rouleau avec un trop précis (cantillation). Le baal koré s'entraîne des mois. Les fidèles suivent dans un 'Houmash. Entendre chaque mot le Chabbat matin accomplit la mitzvah communautaire de lecture publique de Torah.",
        "leining — Leining — chtenie parshi iz svitka s tochnym trop (kantillyatsiey). Baal koreh treniruetsya mesyatsami. Molящiesya sleduyut v Humash. Slushanie kazhdogo slova v Shabbat utrom vypolnyaet obshchinную mitzvah publichnogo chteniya Tory.",
    ),
    KEYS[17]: T(
        "Chazal — Chazal (חז\"ל) hu roshei teivot le\"chachameinu zikhronam livracha\" — rabbanim shel haMishnah, haTalmud veha'midrash she'he'eviru halacha u'mussar. K'she'ha'mekor omair \"Chazal omrim,\" hu ha'masoret ha'klalit ve'lo mechaber yachid.",
        "Chazal — Chazal (חז\"ל) es un acrónimo de \"nuestros Sabios de bendita memoria\" — los rabinos del Mishná, Talmud y midrash que transmitieron halajá y valores. Cuando una fuente dice \"Chazal enseñan,\" se refiere a esta tradición colectiva, no a un autor único.",
        "Chazal — Chazal (חז\"ל) est un acronyme pour « nos Sages que leur mémoire soit bénie » — les rabbins de la Michna, du Talmud et du midrash qui ont transmis halakha et valeurs. Quand une source dit « Chazal enseignent, » c'est cette tradition collective, pas un auteur unique.",
        "Chazal — Chazal (חז\"ל) — akronim «nashi Mudretsy da budet blagoslovena ikh pamyat» — ravviny Mishny, Talmuda i midrasha, peredavshie halakhu i tsennosti. Kogda istochnik govorit «Chazal uchат,» eto kollektivnaya traditsiya, a ne odin avtor.",
    ),
    KEYS[18]: T(
        "Yetzer hatov hu ha'yetzar le'tov — matzpun, ahavat mitzvot u'vosh mi'cheit. Hu gadol be'limud Torah u'vir'ot tzadikim. Chinuch Yehudi me'gadel yetzer hatov ba'yeladim derech hergel lifnei hasagah mle'a.",
        "Yetzer hatov es la inclinación hacia el bien — conciencia, amor por las mitzvot y vergüenza por el mal. Crece al aprender Torá y ver ejemplos justos. La educación judía cultiva yetzer hatov en niños mediante hábito antes de plena comprensión.",
        "Yetser hatov est l'inclination vers le bien — conscience, amour des mitzvot et honte du mal. Il grandit en étudiant la Torah et voyant des exemples justes. L'éducation juive cultive le yetser hatov chez les enfants par l'habitude avant la pleine compréhension.",
        "Yetzer hatov — sklonnost k dobру — sovest, lyubov k mitzvot i styd za grekh. Rastet pri izuchenii Tory i primere tsadikim. Evreyskoe vospitanie razvivает yetzer hatov u detey cherez privychku do polnogo ponimaniya.",
    ),
    KEYS[19]: T(
        "Zimun hu hachanat acherim le'bentching yachad k'she'shelosha o yoter anashim (l'fi minhag) achlu lechem ke'chavurah. Ha'menahel omair \"nevarech\" va'acheirim onim. Hu hofech hoda'ah pratit li'tehillah tziburit. Zimun shel nashim — minhagim nifradim, sha'alu et ha'rav.",
        "Zimun es invitar a otros a bentching juntos cuando tres o más hombres (según minhag) comieron pan en grupo. El líder dice \"bendigamos\" y otros responden. Convierte gracias privadas en alabanza comunitaria. Zimun de mujeres sigue costumbres separadas — pregunta a tu rav.",
        "Zimoun consiste à inviter d'autres au bentching quand trois hommes ou plus (selon minhag) ont mangé du pain ensemble. Le leader dit « bénissons » et les autres répondent. Il transforme la gratitude privée en louange communautaire. Zimoun des femmes — coutumes séparées, demandez à votre rav.",
        "Zimun — priglashenie drugikh k bentching, kogda tri ili bolee muzhchin (po minhag) eli khleb vmeste. Vozglavlyayushchiy govorit «davayte blagoslovim», drugie otvechayut. Prevращает lichnuyu blagodarnost v obshchinную hvalu. Zimun zhenshchin — otdelnye obychai, sprosite rava.",
    ),
    KEYS[20]: T(
        "bentcher — Bentcher (birkon) hu sefer katan im Birkat Hamazon ve'l'f'amim shirim leShabbat vetosafot lebentching. Le'hash'ir oto al ha'shulchan makhel et ha'bentching achar seudat lechem. Rabim dvei lashon; yesh im hanchayot zimun vetosafot lechagim.",
        "bentcher — Un bentcher (birkon) es un librito con Birkat Hamazon y a veces canciones de Shabat y adiciones de bentching. Tener uno en la mesa facilita el bentching tras comidas con pan. Muchos son bilingües; algunos incluyen instrucciones de zimun e inserciones festivas.",
        "bentcher — Un bentcher (birkon) est un petit livret avec Birkat HaMazon et parfois des chants de Chabbat et additions de bentching. En garder un à table facilite le bentching après les repas avec pain. Beaucoup sont bilingues ; certains incluent instructions de zimoun et insertions de fêtes.",
        "bentcher — Bentcher (birkon) — malenkaya knizhka s Birkat Hamazon i inogda shabbat-pesen i dobavleniyami k bentching. Derzhat na stole uproshchaet bentching posle khlebных трапез. Mnogie dvuyazychnye; nekotorye s instruktsiyami zimun i prazdnichnymi vstavkami.",
    ),
    KEYS[21]: T(
        "tzedakah — Tzedakah meturgemet le\"charity,\" aval ha'shoresh mashma tzedek — le'chalek et mah she'hafkid Hashem etkhem im ha'needyim. Ha'chachamim me'anyenim otah bein amudei ha'olam; afilu pruta she'nitanah be'lev shalem hi mitzvah gedolah.",
        "tzedaká — Tzedaká se traduce usualmente como \"caridad,\" pero la raíz significa justicia — compartir lo que D-s te confió con quienes lo necesitan. Los sabios la incluyen entre los pilares del mundo; incluso una moneda pequeña dada con corazón pleno es una gran mitzvá.",
        "tsedaka — Tsedaka se traduit souvent par « charité, » mais la racine signifie justice — partager ce que D. vous a confié avec les nécessiteux. Les sages la comptent parmi les piliers du monde ; même une petite pièce donnée de bon cœur est une grande mitzvah.",
        "tzedakah — Tzedakah obychno perevodyat kak «blagotvoritelnost,» no koren znachit spravedlivost — delitsya tem, chto V-s vam vveril, s nuzhdayushchimisya. Mudretsy schitayut ee odним iz stolpov mira; dazhe malaya moneta, dannaya ot vsego serdtsa — velikaya mitzvah.",
    ),
    KEYS[22]: T(
        "Aleinu hi tefillat siyum me'ir et malchut ha'Hashem ve'tikvateinu le'hakarat meluchato ba'olam kulo. Hi mesayemet Shacharit uMaariv yom yom u'mov'ah be'sof tefillot ha'Yamim Nora'im. Hakri'ah be\"ve'ankhnu kor'im\" hi rega shel hishtatfut ve'yirah.",
        "Aleinu es una oración de cierre que declara la soberanía de D-s y nuestra esperanza de reconocimiento universal de Su reinado. Cierra Shacharit y Maariv diarios y aparece al final de los servicios de las Yamim Noraim. La inclinación en \"nos inclinamos\" es momento de sumisión y reverencia.",
        "Aléinou est une prière de clôture déclarant la souveraineté de D. et notre espoir de reconnaissance universelle de Sa royauté. Elle clôt Shacharit et Maariv quotidiens et apparaît à la fin des offices des Yamim Noraïm. L'inclinaison à « nous inclinons le genou » est un moment de soumission et d'awe.",
        "Aleinu — zaklyuchitelnaya molitva o vladenii V-ga i nadezhde na vsemirnoye priznanie Ego tsarstva. Zavershaet Shacharit i Maariv i yavlyaetsya v Yamim Nora'im. Naklon pri «my sklonяem koleno» — moment smireniya i trepeta.",
    ),
    KEYS[23]: T(
        "הוסיפu מתיקות לShabbat! 🍪 אפu או הכינu ממתקים מיוחדים ליom הקodesh. הנה mitzvah hack מהנה: טעמו את היצירה לפני Shabbat כדי לוודא שהיא מושלמת — בדיקת 'איכות' זו היא mitzvah נוספת של הכנת Shabbat!",
        "¡Añade dulzura a Shabat! 🍪 Hornea o prepara golosinas especiales para el día santo. Truco de mitzvá: prueba tu creación antes de Shabat para asegurarte de que esté perfecta — ¡este 'control de calidad' es otra mitzvá de preparación de Shabat!",
        "Ajoutez de la douceur à Chabbat ! 🍪 Préparez des friandises spéciales pour le jour saint. Astuce mitzvah : goûtez votre création avant Chabbat pour vérifier qu'elle est parfaite — ce « contrôle qualité » est une mitzvah supplémentaire de préparation de Chabbat !",
        "Dobavte sladosti v Shabbat! 🍪 Ispекite ili prigotovte uгощения k svyatому dnyu. Mitzvah-hak: popробuyte pered Shabbat — eto «kontrol kachestva» i dopolnitelnaya mitzvah podgotovki Shabbat!",
    ),
    KEYS[24]: T(
        "Chiyuv mashma chayav — ha'mitzvah chala alekha ve'at tzarich le'kayem ota. Bar mitzvah yotzer chiyuv le'mitzvot shel ben; pturim acherim (machalah, sakanah) yecholim le'batel chiyuv ba'zman. Lada'at im chayav o patur — lachach zeh sha'alu rav.",
        "Chiyuv significa obligado — la mitzvá te aplica y se espera que la cumplas. Bar mitzvá crea chiyuv para las mitzvot del niño; otras exenciones (enfermedad, peligro) pueden quitar chiyuv temporalmente. Saber si eres chayav o patur es por qué preguntamos a un rav.",
        "Hiyouv signifie obligé — la mitzvah vous concerne et vous devez l'accomplir. Bar mitzvah crée un hiyouv pour les mitzvot du garçon ; d'autres exemptions (maladie, danger) peuvent retirer temporairement le hiyouv. Savoir si vous êtes hayav ou patour — c'est pourquoi on demande au rav.",
        "Chiyuv oznachaet obyazannost — mitzvah primenyaetsya k vam i vy dolzhny ee vypolnit. Bar-mitsva sozdaet chiyuv dlya mitzvot malchika; drugie osvobozhdeniya (bolezn, opasnost) mogut vremenno snyat chiyuv. Znat, chayav ili patur vy — zachem my sprashivaem rava.",
    ),
    KEYS[25]: T(
        "Rosh Hashana — Rosh Hashana hu Rosh Hashana u'Yom HaDin — shofar, seudot chagigiyot u'tefillah chamurah. Hu matchil et Aseret Yemei Teshuvah ad Yom Kippur. Minhagim: tapuach bidvash, pri chadash ve'tashlich. Melacha asura kmo Yom Tov.",
        "Rosh Hashaná — Rosh Hashaná es el Año Nuevo judío y Día del Juicio — shofar, comidas festivas y oración solemne. Comienza los Diez Días de Teshuvá hacia Yom Kipur. Costumbres: manzanas en miel, fruta nueva y tashlij. El trabajo está prohibido como Yom Tov.",
        "Roch Hachana — Roch Hachana est le Nouvel An juif et le Jour du Jugement — shofar, repas festifs et prière solennelle. Il commence les Dix Jours de Techouva menant à Yom Kippour. Coutumes : pommes au miel, fruit nouveau et tachlich. Le travail est interdit comme Yom Tov.",
        "Rosh Hashana — Rosh Hashana — evreyskiy Novyy god i Sudnyy den — shofar, prazdnichnye трапезы i torzhestvennaya molitva. Nachinaet Desyat dney teshuvy do Yom Kippur. Obychai: yabloki s medom, novyy plod i tashlich. Trud zapreshchen kak Yom Tov.",
    ),
    KEYS[26]: T(
        "simanim — Simanim hem ma'achalei siman leRosh Hashana — tapuach bidvash, rimon, rosh dag, tamarim — kol echad im kalut o tefillah le'shanah chadasha. Hem minhag, lo din miTorah, aval ahuvim le'lamed yeladim. Omru et tefillot yehi ratzon ha'ktzarot min ha'machzor.",
        "simanim — Simanim son alimentos simbólicos de Rosh Hashaná — manzana en miel, granada, cabeza de pescado, dátiles — cada uno con juego de palabras u oración para el año nuevo. Son minhag, no ley de Torá, pero queridos para enseñar a niños. Di las breves oraciones yehi ratzon del majzor.",
        "simanim — Simanim sont les aliments symboliques de Roch Hachana — pomme au miel, grenade, tête de poisson, dattes — chacun avec un jeu de mots ou prière pour la nouvelle année. Ce sont des minhag, pas une loi de Torah, mais chers pour enseigner aux enfants. Dites les courtes prières yehi ratzon du mahzor.",
        "simanim — Simanim — simvolicheskie rosh-hashana produkty — yabloko s medom, granat, rybinaya golova, finiki — kazhdyy s kalamburom ili molitvoy za novyy god. Eto minhag, ne zakon Tory, no lyubimy dlya obucheniya detey. Govorite kratkie yehi ratzon iz mahzora.",
    ),
    KEYS[27]: T(
        "Chassidut (Chasidut) melamedet avodat Hashem be'simcha, be'yosher u've'hakarat nitzotzot Elokim be'chol hachayim. Yisdah al yedei haBaal Shem Tov, hitpasah ba'adnot k'mo Chabad, Breslov veSatmar. Tanya hu sefer merkazi leChassidut le'limud yom yom.",
        "Jasidut (filosofía jasídica) enseña servir a D-s con alegría, sinceridad y conciencia de chispas divinas en toda la vida. Fundada por el Baal Shem Tov, se extendió por cortes como Jabad, Breslov y Satmar. Tanya es texto central de Jasidut para estudio diario.",
        "Hassidout (philosophie hassidique) enseigne à servir D. avec joie, sincérité et conscience des étincelles divines dans toute la vie. Fondée par le Baal Shem Tov, elle s'est répandue via des cours comme Habad, Breslov et Satmar. Tanya est un texte central de Hassidout pour l'étude quotidienne.",
        "Khasidut uchит sluzheniyu V-gu s radostyu, iskrennostyu i osозnaniem bozhestvennykh iskr vo vsей zhizni. Osnovana Baal Shem Tov, rasprostranilas cherez dvory: Chabad, Breslov, Satmar. Tanya — tsentralnyy tekst khasidut dlya ezhednevnogo izucheniya.",
    ),
    KEYS[28]: T(
        "Chesed hu chesed — la'avor me'eiver la'chova ha'meduyeket le'ezor acher. Gemilut chasadim koleh bikkur cholim, levayat ha'met ve'havla'at bli ribit. Ha'olam omed al Torah, avodah u'gemilut chasadim.",
        "Jesed es bondad amorosa — ir más allá del requisito estricto para ayudar a otro. Gemilut jasadim incluye visitar enfermos, enterrar muertos y préstamos sin interés. Se dice que el mundo se sostiene sobre Torá, avodá (servicio) y gemilut jasadim.",
        "Hessed est la bonté bienveillante — aller au-delà de l'obligation stricte pour aider autrui. Gemilout hassadim inclut visiter les malades, enterrer les morts et prêts sans intérêt. On dit que le monde repose sur Torah, avoda (service) et gemilout hassadim.",
        "Chesed — lyubyashchaya dobрota — pomoshch svyshe strogo obyazannosti. Gemilut chasadim vklyuchaet bikkur cholim, levaya ha-met i besprotsentnye zaymy. Govoryat, mir stoit na Tore, avodah i gemilut chasadim.",
    ),
    KEYS[29]: T(
        "למdu וגדלu! צפu בכל shiur Torah שמעניין אתכם 📚. בין אם על הפרשה השבועית, היסטוריה יהודית או halacha מעשית — כל דקת limud Torah מאירה את העולם! בנוסף, אתם לומדים בקצב שלכם ובנושאים שאתם אוהבים 🎓",
        "¡Aprende y crece! Mira cualquier clase de Torá que te interese 📚. Ya sea sobre la parashá semanal, historia judía o halajá práctica — ¡cada minuto de estudio de Torá ilumina el mundo! Además, aprendes a tu ritmo y eliges temas que amas 🎓",
        "Apprenez et grandissez ! Regardez tout cours de Torah qui vous intéresse 📚. Que ce soit la paracha hebdomadaire, l'histoire juive ou la halakha pratique — chaque minute d'étude de Torah illumine le monde ! De plus, vous apprenez à votre rythme et choisissez vos sujets 🎓",
        "Uchites i rastite! Smotrite lyuboy kurs Tory 📚. Parasha, evreyskaya istoriya ili prakticheskaya halakha — kazhdaya minuta limud Torah osveshchaet mir! Uchites v svoem tempе i vybiraete lyubimye temy 🎓",
    ),
    KEYS[30]: T(
        "Omru Half Hallel achar Amidah shel Shacharit beRosh Chodesh (minhag ahuv; lo chova miTorah — Peninei Halakha 05-01-12).\n\nLifnei Musaf:\nHafki'u tefillin lifnei Musaf — al tilbeshu tefillin bi'tefillat Musaf.\n\nTachanun batel beRosh Chodesh.",
        "Recita Medio Halel tras la Amidá de Shajarit en Rosh Jodesh (costumbre querida; no obligación de Torá — Peninei Halakha 05-01-12).\n\nAntes de Musaf:\nQuita tefilín antes de Musaf — no uses tefilín durante la Amidá de Musaf.\n\nTachanún se omite en Rosh Jodesh.",
        "Récitez Demi-Hallel après l'Amidah de Shacharit à Roch 'Hodesh (coutume chérie ; pas une obligation de Torah — Peninei Halakha 05-01-12).\n\nAvant Moussaf :\nRetirez les téfilines avant Moussaf — ne portez pas de téfilines pendant l'Amidah de Moussaf.\n\nTa'hanoun est omis à Roch 'Hodesh.",
        "Chitayte Polu-Hallel posle Amidah Shacharit v Rosh Chodesh (lyubimyy obychay; ne obyazannost Tory — Peninei Halakha 05-01-12).\n\nPered Musaf:\nSnimite tefillin pered Musaf — ne nadeваyte tefillin vo vremya Amidah Musaf.\n\nTachanun propuskayetsya v Rosh Chodesh.",
    ),
    KEYS[31]: T(
        "שלחu מסר מלב אל מי שלא מרגיש טוב 💌. בין אם מדובר בהצטננות או במשהו חמור יותר, המילים החמות שלכם יכולות לעודד! התalmud melamed שbikur cholim מוריד שישים מחלתו.",
        "¡Envía energía sanadora! Manda un mensaje sincero a alguien que no se siente bien 💌. Ya sea un resfriado o algo más serio, tus palabras cariñosas pueden levantar su ánimo. El Talmud enseña que visitar enfermos quita 1/60 de su enfermedad.",
        "Diffusez une énergie de guérison ! Envoyez un message sincère à quelqu'un qui ne se sent pas bien 💌. Qu'il s'agisse d'un rhume ou de quelque chose de plus grave, vos mots chaleureux peuvent remonter le moral ! Le Talmud enseigne que visiter les malades enlève 1/60 de leur maladie.",
        "Otpravte teploe soobshchenie komu-to, kto plokho sebya chuvstvuet 💌. Prostuda ili chto-to serьeznee — vashi slova podderzhat! Talmud uchит: bikkur cholim snimaet 1/60 bolezni.",
    ),
    KEYS[32]: T(
        "Borei nefashot hi bracha acharona ktzara al rabim mi'ma'achalim u'mashkim — pri (chutz me'tmarim, anavim, zayit, te'einim ve'rimonim she'hem Me'ein Shalosh), yarakot, mashkim (chutz m'yayin/mishmash anavim she'hem Me'ein Shalosh), basar, gevina ve'ch.",
        "Borei nefashot es la breve bendición posterior sobre muchos alimentos y bebidas — fruta (excepto dátiles, uvas, aceitunas, higos y granadas que usan Me'ein Shalosh), verduras, bebidas (excepto vino/jugo de uva que usan Me'ein Shalosh), carne, queso, etc.",
        "Borei nefashot est la brève bénédiction après de nombreux aliments et boissons — fruits (sauf dattes, raisins, olives, figues et grenades qui utilisent Me'ein Shalosh), légumes, boissons (sauf vin/jus de raisin qui utilisent Me'ein Shalosh), viande, fromage, etc.",
        "Borei nefashot — korotkoe poslebrachie na mnogie produkty i napitki — plody (krome finikov, vinograda, oliv, fig i granatov — Me'ein Shalosh), ovoshchi, napitki (krome vina/soka — Me'ein Shalosh), myaso, syr i t.d.",
    ),
    KEYS[33]: T(
        "Korbanot hem ha'korbanot baMikdash — korbanot behemah, ketoret u'nesachim — she'hikriv am Yisrael. Me'achar churban haMikdash ein korbanot; tefillah mechazkeret u'machlifet otam ad she'yibneh haMikdash.",
        "Korbanot son las ofrendas del Templo — sacrificios animales, incienso y libaciones — traídas por el pueblo judío en el Beit HaMikdash. Desde la destrucción del Templo no podemos ofrecerlas; la oración recuerda y sustituye esas ofrendas hasta que se reconstruya el Templo.",
        "Korbanot sont les offrandes du Temple — sacrifices d'animaux, encens et libations — apportées par le peuple juif au Beit HaMikdash. Depuis la destruction du Temple, nous ne pouvons pas les offrir ; la prière rappelle et remplace ces offrandes jusqu'à la reconstruction du Temple.",
        "Korbanot — khramovye prinosheniya — zhertvy, ktoret i nesechim — v Beit HaMikdash. Posle razrusheniya Khrama my ne mozhem ikh prinosit; molitva vspominaet i zamenyaet ikh do vosstanovleniya Khrama.",
    ),
    KEYS[34]: T(
        "תרגלu הכרת תודה! קחu רגע להודות לה' על משהו טוב בחייכם — גדול או קטן 🙌. אולי הבריאות, המשפחה, או אפילו הקפה של הבוקר! חazal melamedim שכשה' רואה שאנחנו שולחים תודה, הוא מקבל את שאר תפילותינו יחד איתה!",
        "¡Practica gratitud! Tómate un momento para agradecer a D-s por algo bueno en tu vida, grande o pequeño 🙌. ¡Tal vez tu salud, tu familia o incluso ese café matutino! Los rabinos enseñan que cuando D-s ve que enviamos gracias, recibe nuestras otras oraciones junto con ellas.",
        "Pratiquez la gratitude ! Prenez un moment pour remercier D. pour quelque chose de bon dans votre vie — grand ou petit 🙌. Votre santé, votre famille, ou même ce café du matin ! Les sages enseignent que quand D. voit nos remerciements, Il accueille nos autres prières avec eux !",
        "Praktikuyte blagodarnost! Poblagodarite V-ga za chto-to horoshee v zhizni — bolshoe ili maloe 🙌. Zdorove, semya ili utrenniy kofe! Mudretsy uchат: kogda V-s vidit nashu blagodarnost, On prinimaet i drugie nashi molitvy vmeste s ney!",
    ),
    KEYS[35]: T(
        "Ha'baal koreh kore et haTorah min haSefer im trop u'mivta n'chonim. Ha'hachana l'ochl chodshim. Ha'tzibur ba'im im Chumash. Ta'ut she'meshane et ha'pshat yechola le'drosh tikun — ha'oleh shome'a, ein koreh ba'gilyon.",
        "El baal kore canta la Torá del rollo con trop y pronunciación correctos. El entrenamiento toma meses. Los feligreses siguen en Jumash. Un error que cambia el significado puede requerir corrección — el oleh escucha, no lee en voz alta del rollo.",
        "Le baal koré lit la Torah du rouleau avec trop et prononciation corrects. L'entraînement prend des mois. Les fidèles suivent dans un 'Houmash. Une erreur changeant le sens peut exiger correction — l'oleh écoute, ne lit pas à voix haute du rouleau.",
        "Baal koreh chita Toru iz svitka s pravilnym trop i proiznosheniem. Podgotovka mesyatsami. Obshchina sleduet v Humash. Oshibka, menyayushchaya smysl, mozhet trebovat ispravleniya — oleh slushaet, ne chitaet vslukh so svitka.",
    ),
    KEYS[36]: T(
        "Yom HaZikaron (Yom HaZikaron) nofel be-4 Iyar beYisrael, miyad lifnei Yom Ha'atzmaut. Hu mechabbed chayalim naflim ve'kurbanot terror.\n\nHa'yom mit'aleh be'tkasei zikaron ve'sirenot. Rabim shomrim oto ke'yom chamur lifnei chag ha'atzma'ut.",
        "Yom HaZikaron (Día del Recuerdo) cae el 4 de Iyar en Israel, inmediatamente antes de Yom Ha'atzmaut. Honra a soldados caídos y víctimas del terror.\n\nEl día se marca con ceremonias conmemorativas y sirenas. Muchos lo observan como día solemne antes de la celebración de independencia.",
        "Yom HaZikaron (Jour du Souvenir) tombe le 4 Iyar en Israël, juste avant Yom Ha'atsmaout. Il honore les soldats tombés et les victimes du terrorisme.\n\nLa journée est marquée par des cérémonies commémoratives et des sirènes. Beaucoup l'observent comme jour solennel avant la fête de l'indépendance.",
        "Yom HaZikaron (Den Pamyati) — 4 Iyar v Izraile, neposredstvenno pered Yom Ha'atzmaut. Chestvuet pogibshikh soldat i zhertv terrora.\n\nDen otmechaetsya memorialnymi tseremoniyami i sirenami. Mnogie sobлюdayut ego kak torzhestvennyy den pered prazdnikom nezavisimosti.",
    ),
    KEYS[37]: T(
        "kosher — Kosher mashma kasher le'fi halacha — bifrat ochel, aval gam sifrei kodesh u'ma'asim kasherim (l'mashal kesef kasher le'mitzvot). Kosher ein tov shel bri'ut; hu kashrut ruchanit. Be'safek al ma'achal, telefonu la'hechsher o sha'alu et ha'rav.",
        "kosher — Kosher significa apto según halajá — especialmente comida, pero también rollos válidos y conducta aceptable (p. ej. dinero kosher para mitzvot). Kosher no es etiqueta de salud; es aptitud espiritual. En duda sobre un producto, llama a la hechsher o consulta a tu rav.",
        "kosher — Kasher signifie apte selon la halakha — surtout la nourriture, mais aussi les rouleaux valides et la conduite acceptable (p. ex. argent casher pour mitzvot). Casher n'est pas un label santé ; c'est une aptitude spirituelle. En cas de doute, appelez la hekhsher ou consultez votre rav.",
        "kosher — Kosher oznachaet godnost po halakhe — osobenno eda, no takzhe svitki i priemlemoe povedenie (napr. koshernye dengi dlya mitzvot). Kosher — ne zdorovyy label; eto dukhovnaya godnost. Pri somnenii zvonite hekhsheru ili sprosite rava.",
    ),
    KEYS[38]: T(
        "Amidah — HaAmidah (\"tefillat amida\") hi lev kol tefillah — nikra gam Shmoneh Esrei (shem esre, achshav tsha-esre brachot k'she'nosfu achat). Ne'emra be'lachash u've'emtza, el Yerushalayim o Har haBayit (im beYerushalayim), raglayim tzuvu.",
        "Amidá — La Amidá (oración \"de pie\") es el núcleo de cada servicio — también llamada Shmoneh Esrei (dieciocho, ahora diecinueve bendiciones). Se recita en silencio de pie, hacia Jerusalén o el Monte del Templo (si estás en Jerusalén), con los pies juntos.",
        "Amidah — L'Amidah (prière « debout ») est le cœur de chaque office — aussi appelée Chemoné Esrei (dix-huit, maintenant dix-neuf bénédictions). Récitée en silence debout, face à Jérusalem ou au Mont du Temple (à Jérusalem), pieds joints.",
        "Amidah — Amidah («stoyachaya» molitva) — yadro kazhdoy sluzhby — takzhe Shmoneh Esrei (vosemnadtsat, teper devyatnadtsat brakhot). Chitaetsya tiho stoya, litsom k Ierusalimu ili Khrámu (v Ierusalime), nogi vmeste.",
    ),
    KEYS[39]: T(
        "גלו את כוח הPeh (פ)! 👄 Peh mashma 'peh' u'melamed al koach ha'dibur. Tzurato koleh Bet (ב) bifnim (ha'makom ha'pani ba'ktav beSefer Torah yotzer bet), u'mazkir she'mah she'bifnim meshpia al mah she'omrim.",
        "¡Descubre el poder de Peh (פ)! 👄 Peh significa 'boca' y nos enseña sobre el poder del habla. Su forma incluye un Bet (ב) en su interior (el espacio vacío al escribir en un rollo de Torá forma un bet), recordándonos que lo interior afecta lo que decimos.",
        "Découvrez la puissance de Peh (פ) ! 👄 Peh signifie « bouche » et enseigne le pouvoir de la parole. Sa forme inclut un Bet (ב) à l'intérieur (l'espace vide dans un rouleau de Torah forme un bet), rappelant que l'intérieur influence ce que nous disons.",
        "Otkroyte silu Peh (פ)! 👄 Peh oznachaet «rot» i uchит o sile rechi. Ego forma vklyuchaet Bet (ב) vnutri (pustoe prostranstvo v svitke Tory obrazuet bet), napominaya: vnutrennee vliyaet na to, chto my govorim.",
    ),
    KEYS[40]: T(
        "Hayom Yom — Hayom Yom (\"Hayom...\") hu luach Chabad shel limudim ktzarim — amirah, sippur o halacha le'chol yom ba'shanah. L'rov n'lmd ke'chelek miChitas yom yom achar Chumash, Tehillim veTanya. Ha'inyanim ktzarim u'ma'asiyim, le'luchot zmanim עמוסים.",
        "Hayom Yom — Hayom Yom (\"Hoy es…\") es un calendario Jabad de enseñanzas breves — un dicho, historia o ley para cada día del año. A menudo se estudia como parte del Chitas diario tras Jumash, Tehillim y Tanya. Las entradas son cortas y prácticas, para horarios ocupados.",
        "Hayom Yom — Hayom Yom (« Aujourd'hui… ») est un calendrier Habad d'enseignements brefs — une citation, histoire ou loi pour chaque jour de l'année. Souvent étudié dans le Chitas quotidien après 'Houmash, Tehilim et Tanya. Les entrées sont courtes et pratiques, pour emplois du temps chargés.",
        "Hayom Yom — Hayom Yom («Segodnya…») — khasidskiy kalendar kratkikh ucheniy na kazhdyy den goda. Chasto chast ezhednevnogo Chitas posle Humash, Tehillim i Tanya. Zapisi korotkie i praktichnye dlya zanyatykh.",
    ),
    KEYS[41]: T(
        "Pesach — Pesach mechazek et Yetziat Mitzrayim — matzah, maror, Seder ve'ein chametz. Chag ha'cherut veha'emunah. Ha'hachana domenet erev Pesach: bedikat, biur, hashgacha. Ha'tachlit ha'ruchanit — lihyot she'yatzata miMitzrayim, lo rak zikaron histori.",
        "Pesaj — Pesaj conmemora el Éxodo — matzá, maror, Seder y sin chametz. Es la festividad de libertad y fe. La preparación domina erev Pesaj: bedikat, biur, kashering. La meta espiritual es sentir que saliste de Egipto personalmente, no solo memoria histórica.",
        "Pessah — Pessah commémore l'Exode — matza, maror, Seder et pas de 'hamets. C'est la fête de liberté et de foi. La préparation domine erev Pessah : bedikat, biour, cachérisation. Le but spirituel est de sentir que vous êtes sorti d'Égypte personnellement, pas seulement mémoire historique.",
        "Pesach — Pesach otmechaet Iskhod — matzah, maror, Seder i bez chametz. Prazdnik svobody i very. Podgotovka dominiruet v erev Pesach: bedikat, biur, kashering. Dukhovnaya tsel — chuvstvovat, chto vy lichno vyshli iz Egipta, a ne tolko istoricheskaya pamyat.",
    ),
    KEYS[42]: T(
        "Borei pri ha'etz hi ha'bracha al pri ha'etz — tapuchim, afarsekim, anavim (le'achilah k'pri) ve'domim. Omru lifnei ha'achilah. Pri min ha'adamah (bananot, tutim) — borei pri ha'adamah. Be'safek, sha'alu et ha'rav o hashav mivchan brachot.",
        "Borei pri ha'etz es la bendición sobre fruta que crece en árboles — manzanas, duraznos, uvas (para comer como fruta) y similares. Dila antes de comer. Fruta del suelo (bananas, fresas) usa borei pri ha'adamah. En duda, pregunta a tu rav o usa una guía de brachot.",
        "Borei pri ha'etz est la bénédiction sur les fruits qui poussent sur les arbres — pommes, pêches, raisins (à manger comme fruit) et similaires. Dites-la avant de manger. Fruits du sol (bananes, fraises) utilisent borei pri ha'adamah. En cas de doute, demandez à votre rav ou utilisez un guide de brachot.",
        "Borei pri ha'etz — brakha na plody derevev — yabloki, persiki, vinograd (kak plod) i podobnye. Govorite pered edoy. Plody s zemli (banany, klubnika) — borei pri ha'adamah. Pri somnenii sprosite rava ili ispolzuyte spravochnik brakhot.",
    ),
    KEYS[43]: T(
        "Kabbalah — Kabbalah hi ha'mesoret ha'mistit ha'Yehudit ha'chokeret et yachasei ha'Hashem la'beriah, la'neshamah u'la'mitzvot. Sifrei Kabbalah k'mo haZohar veTanya. Kabbalah autentit n'lmda im yedi'at Torah u'moreh; hi lo nevu'ah o kishuf.",
        "Kabbalah — Kabbalah es la tradición mística judía que explora cómo D-s se relaciona con la creación, el alma y las mitzvot. Textos incluyen el Zóhar y obras como Tanya. La Cábala auténtica se estudia con conocimiento de Torá y un maestro; no es adivinación ni magia.",
        "Kabbalah — Kabbalah est la tradition mystique juive explorant comment D. se rapporte à la création, l'âme et les mitzvot. Textes incluent le Zohar et des œuvres comme Tanya. La Kabbale authentique s'étudie avec connaissance de Torah et un enseignant ; ce n'est ni divination ni magie.",
        "Kabbalah — Kabbalah — evreyskaya mistическая traditsiya o tom, kak V-s otnositsya k tvoreniyu, dushe i mitzvot. Teksty: Zohar, Tanya. Podlinnaya Kabbala izuchaetsya s znaniyem Tory i uchitelem; eto ne gadaniye i ne magiya.",
    ),
    KEYS[44]: T(
        "Mayim achronim hu rechitzat k'tzotot ha'etzba'ot achar seudat lechem velifnei bentching — zikaron she'ochlim ke'avdei Hashem. Lo kol kehillah medgasheset hayom, aval rabim mi'ha'siddurim u'bentcherim kolvlu. Hu nifrad mi'netilat yadayim lifnei ha'seudah.",
        "Mayim achronim es lavar las yemas de los dedos tras una comida con pan y antes del bentching — recordatorio de que comemos como siervos ante D-s. No toda comunidad lo enfatiza hoy, pero muchos sidurim y bentchers lo incluyen. Es separado del netilat yadayim antes de la comida.",
        "Mayim a'haronim consiste à laver le bout des doigts après un repas avec pain et avant le bentching — rappel que nous mangeons comme serviteurs devant D. Toutes les communautés ne l'insistent pas aujourd'hui, mais beaucoup de siddourim et bentchers l'incluent. C'est distinct du netilat yadayim avant le repas.",
        "Mayim achronim — promyvanie konchikov paltsev posle khlebной трапезы i pered bentching — napominanie, chto my edim kak slugi pered V-gom. Ne vse obshchiny podcherkivayut segodnya, no mnogie siddury i bentchery vklyuchayut. Otdelno ot netilat yadayim pered edoy.",
    ),
    KEYS[45]: T(
        "Tekiah hu teki'ah yesharah ve'orech min ha'shofar. Sidrat Rosh Hashana meshalevet tekiah im shevarim (shvurim) ve'teruah (rordim) l'fi minhag. Me'ah teki'ot minhag Ashkenaz. Hit'asku ba'shul kodem kedei she'Rosh Hashana lo yihyeh ha'pa'am harishona she'atem shom'im shofar.",
        "Tekiah es un blast largo y recto de shofar. La secuencia de Rosh Hashaná combina tekiah con shevarim (quebrado) y teruah (tembloroso) según minhag. Cien blasts es costumbre ashkenazí. Practica en la shul antes para que Rosh Hashaná no sea tu primera vez escuchando shofar.",
        "Tekiah est un long son droit du shofar. La séquence de Roch Hachana combine tekiah avec shevarim (cassé) et teroua (tremblant) selon minhag. Cent sons est la coutume ashkénaze. Entraînez-vous à la shul avant pour que Roch Hachana ne soit pas votre première fois d'entendre le shofar.",
        "Tekiah — dlinnyy pryamoy zvuk shofara. Posledovatelnost Rosh Hashana sochetaet tekiah s shevarim (lomannym) i teruah (drozhashchim) po minhag. Sto zvukov — obychay ashkenazov. Potreniruytes v shul заранее, chtoby Rosh Hashana ne byl pervym razom slushaniya shofara.",
    ),
    KEYS[46]: T(
        "zimun — Zimun hu hachanat acherim le'bentching yachad k'she'shelosha o yoter anashim (l'fi minhag) achlu lechem ke'chavurah. Ha'menahel omair \"nevarech\" va'acheirim onim. Hu hofech hoda'ah pratit li'tehillah tziburit. Zimun shel nashim — minhagim nifradim, sha'alu et ha'rav.",
        "zimun — Zimun es invitar a otros a bentching juntos cuando tres o más hombres (según minhag) comieron pan en grupo. El líder dice \"bendigamos\" y otros responden. Convierte gracias privadas en alabanza comunitaria. Zimun de mujeres sigue costumbres separadas — pregunta a tu rav.",
        "zimun — Zimoun consiste à inviter d'autres au bentching quand trois hommes ou plus (selon minhag) ont mangé du pain ensemble. Le leader dit « bénissons » et les autres répondent. Il transforme la gratitude privée en louange communautaire. Zimoun des femmes — coutumes séparées, demandez à votre rav.",
        "zimun — Zimun — priglashenie drugikh k bentching, kogda tri ili bolee muzhchin (po minhag) eli khleb vmeste. Vozglavlyayushchiy govorit «davayte blagoslovim», drugie otvechayut. Prevращает lichnuyu blagodarnost v obshchinную hvalu. Zimun zhenshchin — otdelnye obychai, sprosite rava.",
    ),
    KEYS[47]: T(
        "Chumash (mi'chamesh) hu sefer mudbak shel chamesh chumshim shel Torah — Bereishit, Shemot, Vayikra, Bamidbar veDevarim. Rov Chumashim kolvlim perushim k'mo Rashi. Meshamshim beShnayim Mikra, le'hachazik ba'shul o limud ba'bayit.",
        "Un Jumash (de chamesh, \"cinco\") es un libro impreso de los cinco libros de la Torá — Bereshit, Shemot, Vayikrá, Bamidbar y Devarim. La mayoría incluyen comentarios como Rashi. Se usa para Shnayim Mikra, seguir en la shul o aprender en casa.",
        "Un 'Houmash (de 'hamesh, « cinq ») est un livre imprimé des cinq livres de la Torah — Berechit, Shemot, Vayikra, Bamidbar et Devarim. La plupart incluent des commentaires comme Rachi. On l'utilise pour Chenayim Mikra, suivre à la shul ou apprendre à la maison.",
        "Humash (ot chamesh, «pyat») — pechatnaya kniga pyati knig Tory — Bereishit, Shemot, Vayikra, Bamidbar i Devarim. Bolshinstvo vklyuchaet kommentarii, naprimer Rashi. Ispolzuetsya dlya Shnayim Mikra, sledovaniya v shul ili ucheba doma.",
    ),
}

assert len(DATA) == 48, len(DATA)

lines = [
    "# -*- coding: utf-8 -*-",
    "def T(he, es, fr, ru):",
    "    return (he, es, fr, ru)",
    "",
    "ENTRIES = {",
]
for key, val in DATA.items():
    he, es, fr, ru = val
    lines.append(f"    {json.dumps(key, ensure_ascii=False)}: T(")
    lines.append(f"        {json.dumps(he, ensure_ascii=False)},")
    lines.append(f"        {json.dumps(es, ensure_ascii=False)},")
    lines.append(f"        {json.dumps(fr, ensure_ascii=False)},")
    lines.append(f"        {json.dumps(ru, ensure_ascii=False)},")
    lines.append("    ),")
lines.append("}")
lines.append("")

(ROOT / "rest_e_batch3.py").write_text("\n".join(lines) + "\n", encoding="utf-8")
print(f"Wrote rest_e_batch3.py with {len(DATA)} entries")
