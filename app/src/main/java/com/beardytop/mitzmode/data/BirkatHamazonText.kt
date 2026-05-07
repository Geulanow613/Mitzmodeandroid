package com.beardytop.mitzmode.data

enum class BirkatCategory {
    ZIMUN,
    MAIN_BLESSINGS,
    ADDITIONS
}

data class BirkatSection(
    val title: String,
    val hebrew: String,
    val english: String? = null,
    val category: BirkatCategory? = null
)

object BirkatHamazonText {
    val sections = listOf(
        BirkatSection(
            title = "שיר המעלות",
            hebrew = """על נהרות בבל שם ישבנו גם בכינו בזכרנו את ציון. על ערבים בתוכה תלינו כנרותינו. כי שם שאלונו שובינו דברי שיר ותוללינו שמחה שירו לנו משיר ציון. איך נשיר את שיר יהוה על אדמת נכר. אם אשכחך ירושלם תשכח ימיני. תדבק לשוני לחכי אם לא אזכרכי אם לא אעלה את ירושלם על ראש שמחתי. זכר יהוה לבני אדום את יום ירושלם האמרים ערו ערו עד היסוד בה. בת בבל השדודה אשרי שישלם לך את גמולך שגמלת לנו. אשרי שיאחז ונפץ את עלליך אל הסלע.""",
            english = "By the rivers of Babylon, there we sat and wept when we remembered Zion. Upon the willows in its midst we hung our harps. For there our captors asked us for words of song, and our tormentors [asked of us] mirth: 'Sing for us from Zion's song.' How shall we sing the Lord's song on alien soil? If I forget you, O Jerusalem, let my right hand forget its skill. Let my tongue cleave to my palate if I do not remember you, if I do not bring up Jerusalem at the beginning of my joy. Remember, O Lord, against the Edomites the day of Jerusalem, who said, 'Raze it, raze it to its very foundation!' O daughter of Babylon, who is destined to be plundered, praiseworthy is he who repays you your recompense for what you did to us. Praiseworthy is he who will take and dash your infants against the rock.",
            category = BirkatCategory.ADDITIONS
        ),
        BirkatSection(
            title = "שיר המעלות",
            hebrew = """שיר המעלות בשוב יהוה את שיבת ציון היינו כחלמים. אז ימלא שחוק פינו ולשוננו רנה אז יאמרו בגוים הגדיל יהוה לעשות עם אלה. הגדיל יהוה לעשות עמנו היינו שמחים. שובה יהוה את שביתנו כאפיקים בנגב. הזרעים בדמעה ברנה יקצרו. הלוך ילך ובכה נשא משך הזרע בא יבוא ברנה נשא אלמתיו""",
            english = "A song of ascents. When the Lord returned the returnees to Zion, we were like dreamers. Then our mouths will be filled with laughter and our tongues with songs of praise. Then they will say among the nations, 'The Lord has done great things for them.' The Lord has done great things for us; we were joyful. Return, O Lord, our captivity like rivulets in arid land. Those who sow with tears will reap with song. He who goes along weeping, carrying the valuable seeds, will come back with song, carrying his sheaves.",
            category = BirkatCategory.ADDITIONS
        ),
        BirkatSection(
            title = "זימון",
            hebrew = """רבותי, נברך!
המסבים עונים: יהי שם יי מברך מעתה ועד עולם.
המזמן אומר: ברשות מרנן ורבנן ורבותי, נברך (בעשרה אלהינו) שאכלנו משלו.
המסבים עונים: ברוך (אלהינו) שאכלנו משלו ובטובו חיינו.
המזמן חוזר ואומר: ברוך (אלהינו) שאכלנו משלו ובטובו חיינו.""",
            english = """My masters, let us bless!
Others respond: May the name of the Lord be blessed from now and forever.
Leader says: With the permission of our masters and teachers, let us bless (if 10: our God) He of whose food we have eaten.
Others respond: Blessed be (our God) He of whose food we have eaten and through whose goodness we live.
Leader repeats: Blessed be (our God) He of whose food we have eaten and through whose goodness we live.""",
            category = BirkatCategory.ZIMUN
        ),
        BirkatSection(
            title = "ברכת הזן",
            hebrew = """ברוך אתה יהוה אלהינו מלך העולם הזן את העולם כלו בטובו בחן בחסד וברחמים, הוא נתן לחם לכל-בשר כי לעולם חסדו ובטובו הגדול תמיד לא חסר לנו ואל יחסר לנו מזון (תמיד) לעולם ועד בעבור שמו הגדול כי הוא אל זן ומפרנס לכל ומטיב לכל ומכין מזון לכל-בריותיו אשר ברא ברוך אתה יי הזן את הכל.""",
            english = "Blessed are You, Lord our God, King of the universe, who nourishes the entire world with His goodness, with grace, with kindness and with mercy. He gives bread to all flesh, for His kindness endures forever. And through His great goodness, we have never lacked, and may we never lack nourishment, for all eternity, for the sake of His great name. For He is God who nourishes and sustains all, and does good to all, and prepares food for all His creatures which He created. Blessed are You, Lord, who nourishes all.",
            category = BirkatCategory.MAIN_BLESSINGS
        ),
        BirkatSection(
            title = "ברכת הארץ",
            hebrew = """נודה לך יי אלהינו על שהנחלת לאבותינו ארץ חמדה טובה ורחבה. ועל שהוצאתנו יי אלהינו מארץ מצרים ופדיתנו מבית עבדים. ועל בריתך שחתמת בבשרנו. ועל תורתך שלמדתנו. ועל חוקיך שהודעתנו. ועל חיים חן וחסד שחנתנו. ועל אכילת מזון שאתה זן ומפרנס אותנו תמיד בכל יום ובכל עת ובכל שעה.

ועל הכל יי אלהינו אנחנו מודים לך ומברכים אותך, יתברך שמך בפי כל חי תמיד לעולם ועד, ככתוב: ואכלת ושבעת, וברכת את יי אלהיך על הארץ הטובה אשר נתן לך. ברוך אתה יי, על הארץ ועל המזון.""",
            english = "We thank You, Lord our God, for having given as a heritage to our fathers a desirable, good and spacious land. And for having brought us out, Lord our God, from the land of Egypt and redeemed us from the house of bondage. And for Your covenant which You sealed in our flesh. And for Your Torah which You taught us. And for Your statutes which You made known to us. And for the life, grace and kindness which You graciously bestowed upon us. And for the eating of food with which You constantly nourish and sustain us every day, every moment and every hour.\n\nFor all this, Lord our God, we thank You and bless You. May Your name be blessed by the mouth of every living being, constantly and forever, as it is written: 'When you have eaten and are satisfied, you shall bless the Lord your God for the good land which He has given you.' Blessed are You, Lord, for the land and for the food.",
            category = BirkatCategory.MAIN_BLESSINGS
        ),
        BirkatSection(
            title = "על הניסים לחנוכה",
            hebrew = """בחנוכה ובפורים אומרים כאן על הניסים
על הנסים ועל הפרקן ועל הגבורות ועל התשועות ועל המלחמות שעשית לאבותינו בימים ההם בזמן הזה.

בימי מתתיהו בן יוחנן כהן גדול חשמונאי ובניו כשעמדה מלכות יון הרשעה על עמך ישראל להשכיחם מתורתך ולהעבירם מחקי רצונך ואתה ברחמיך הרבים עמדת להם בעת צרתם רבת את ריבם דנת את דינם נקמת את נקמתם מסרת גבורים ביד חלשים ורבים ביד מעטים וטמאים ביד טהורים ורשעים ביד צדיקים וזדים ביד עוסקי תורתך ולך עשית שם גדול וקדוש בעולמך ולעמך ישראל עשית תשועה גדולה ופרקן כהיום הזה ואחר כך באו בניך לדביר ביתך ופנו את היכלך וטהרו את-מקדשך והדליקו נרות בחצרות קדשך וקבעו שמונת ימי חנכה אלו להודות ולהלל לשמך הגדול.""",
            english = "For the miracles, and for the salvation, and for the mighty deeds, and for the victories, and for the battles which You performed for our forefathers in those days, at this time. In the days of Mattathias, the son of Johanan the High Priest, the Hasmonean and his sons, when the wicked Greek kingdom rose up against Your people Israel to make them forget Your Torah and compel them to stray from the statutes of Your will. But You in Your great mercy stood up for them in the time of their distress. You took up their grievance, judged their claim, and avenged their wrong. You delivered the strong into the hands of the weak, the many into the hands of the few, the impure into the hands of the pure, the wicked into the hands of the righteous, and the wanton into the hands of the diligent students of Your Torah. For Yourself You made a great and holy name in Your world, and for Your people Israel you worked a great victory and salvation as this very day. Thereafter, Your children came to the Holy of Holies of Your House, cleansed Your Temple, purified Your Sanctuary, kindled lights in Your holy courtyards, and instituted these eight days of Chanukah to give thanks and praise to Your great name.",
            category = BirkatCategory.ADDITIONS
        ),
        BirkatSection(
            title = "על הניסים לפורים",
            hebrew = """בימי מרדכי ואסתר בשושן הבירה כשעמד עליהם המן הרשע בקש להשמיד להרוג לאבד את-כל-היהודים מנער ועד זקן טף ונשים ביום אחד בשלשה עשר לחדש שנים עשר הוא חדש אדר וללם לבוז ואתה ברחמיך הרבים הפרת את עצתו וקלקלת את מחשבתו והשבות-לו גמולו בראשו ותלו אותו ואת בניו על העץ ועשית עמהם נסים ונפלאות ונודה לשמך הגדול סלה.""",
            english = "In the days of Mordechai and Esther, in Shushan the capital, when the wicked Haman rose up against them and sought to destroy, to slay, and to exterminate all the Jews, young and old, infants and women, on a single day, on the thirteenth day of the twelfth month, which is the month of Adar, and to take their spoils for plunder. But You, in Your abundant mercy, nullified his counsel and frustrated his intention and caused his design to return upon his own head, and they hanged him and his sons on the gallows. For all these miracles we thank Your great name.",
            category = BirkatCategory.ADDITIONS
        ),
        BirkatSection(
            title = "בונה ירושלים",
            hebrew = """רחם נא יי אלהינו על ישראל עמך, ועל ירושלים עירך, ועל ציון משכן כבודך, ועל מלכות בית דוד משיחך, ועל הבית הגדול והקדוש שנקרא שמך עליו. אלהינו, אבינו, רענו, זוננו, פרנסנו וכלכלנו והרויחנו, והרוח לנו יי אלהינו מהרה מכל צרותינו. ונא אל תצריכנו י אלהינו, לא לידי מתנת בשר ודם ולא לידי הלואתם, כי אם לידך המלאה הפתוחה הקדושה והרחבה, שלא נבוש ולא נכלם לעולם ועד.

ובנה ירושלים עיר הקדש במהרה בימינו. ברוך אתה יי, בונה ברחמיו ירושלים. אמן.""",
            english = "Have mercy, Lord our God, on Israel Your people, on Jerusalem Your city, on Zion the abode of Your glory, on the kingdom of the house of David Your anointed, and on the great and holy Temple that bears Your name. Our God, our Father, tend us, feed us, sustain us, support us, relieve us, and grant us speedy relief, Lord our God, from all our troubles. Please do not make us dependent, Lord our God, on the gifts or loans of human beings, but only on Your full, open, holy and generous hand, so that we may never be shamed or humiliated.\n\nAnd rebuild Jerusalem the holy city speedily in our days. Blessed are You, Lord, who in His mercy rebuilds Jerusalem. Amen.",
            category = BirkatCategory.MAIN_BLESSINGS
        ),
        BirkatSection(
            title = "יעלה ויבוא",
            hebrew = """אלהינו ואלהי אבותינו, יעלה ויבא יגיע יראה וירצה ישמע יפקד ויזכר זכרוננו ופקדוננו וזכרון אבותינו, וזכרון משיח בן דוד עבדך וזכרון ירושלים עיר קדשך וזכרון כל עמך בית ישראל לפניך לפלטה לטובה, לחן לחסד ולרחמים לחיים ולשלום ביום
בראש חודש: ראש החדש
בפסח: חג המצות
בסוכות: חג הסכות
זכרנו יהוה אלהינו בו לטובה, ופקדנו בו לברכה, והושיענו בו לחיים ובדבר ישועה ורחמים חוס וחננו, ורחם עלינו, והושיענו כי אליך עינינו, כי אל מלך חנון ורחום אתה.""",
            english = "Our God and God of our fathers, may there rise, come, reach, appear, be favored, be heard, be regarded, and be remembered before You our remembrance and consideration, and the remembrance of our fathers, and the remembrance of Messiah son of David Your servant, and the remembrance of Jerusalem Your holy city, and the remembrance of all Your people the house of Israel, for deliverance, for goodness, for grace, for kindness, for mercy, for life, and for peace on this day of:\n[On Rosh Chodesh: the New Moon]\n[On Passover: the Festival of Matzot]\n[On Sukkot: the Festival of Sukkot]\nRemember us, Lord our God, on this day for good; consider us on this day for blessing; save us on this day for life. With a word of salvation and mercy spare us and be gracious to us; have mercy on us and save us, for our eyes are turned to You, because You are God, the gracious and merciful King.",
            category = BirkatCategory.ADDITIONS
        ),
        BirkatSection(
            title = "הטוב והמטיב",
            hebrew = """ברוך אתה יי אלהינו, מלך העולם, האל אבינו, מלכנו, אדירנו, בוראנו, גאלנו, יוצרנו, קדושנו קדוש יעקב, רוענו רועה ישראל, המלך הטוב והמיטיב לכל, שבכל יום ויום הוא היטיב, הוא מיטיב, הוא ייטיב לנו, הוא גמלנו, הוא גומלנו, הוא יגמלנו לעד, לחן ולחסד ולרחמים ולרוח הצלה והצלחה, ברכה וישועה, נחמה פרנסה וכלכלה ורחמים וחיים ושלום, וכל טוב; ומכל טוב לעולם אל יחסרנו.""",
            english = "Blessed are You, Lord our God, King of the universe, God our Father, our King, our Mighty one, our Creator, our Redeemer, our Maker, our Holy One, the Holy One of Jacob, our Shepherd, the Shepherd of Israel, the King who is good and does good to all. For each and every day He has done good, He does good, and He will do good to us. He has bestowed, He bestows, and He will forever bestow upon us grace, kindness and mercy, relief, salvation and success, blessing and help, consolation, sustenance and nourishment, compassion, life, peace and all good; and may He never let us lack any good.",
            category = BirkatCategory.MAIN_BLESSINGS
        ),
        BirkatSection(
            title = "הרחמן",
            hebrew = """הרחמן הוא ימלוך עלינו לעולם ועד. הרחמן הוא יתברך בשמים ובארץ. הרחמן הוא ישתבח לדור דורים, ויתפאר בנו לעד ולנצח נצחים, ויתהדר בנו לעד ולעולמי עולמים.
הרחמן הוא יפרנסנו בכבוד. הרחמן הוא ישבור עלנו מעל צוארנו, והוא יוליכנו קוממיות לארצנו. הרחמן הוא ישלח לנו ברכה מרבה בבית הזה, ועל שלחן זה שאכלנו עליו. הרחמן הוא ישלח לנו את אליהו הנביא זכור לטוב, ויבשר לנו בשורות טובות ישועות ונחמות.

בבית אביו אומר: הרחמן הוא יברך את אבי מורי בעל הבית הזה, ואת אמי מורתי בעלת הבית הזה.
נשוי אומר: הרחמן הוא יברך אותי, (אם אביו ואמו בחיים: ואת אבי מורי, ואת אמי מורתי,) ואת אשתי, ואת זרעי, ואת כל אשר לי.
נשואה אומרת: הרחמן הוא יברך אותי, (אם איה ואמה בחיים: ואת אבי מורי, ואת אמי מורתי,) ואת בעלי, ואת זרעי, ואת כל אשר לי.
אורח אומר: הרחמן הוא יברך את בעל הבית הזה ואת בעלת הבית הזה, אותם ואת ביתם ואת זרעם ואת כל אשר להם.

בראש חודש: הרחמן הוא יחדש עלינו את החדש הזה לטובה ולברכה.
בסוכות: הרחמן הוא יקים לנו את סכת דוד הנופלת.

הרחמן הוא יזכנו לימות המשיח ולחיי העולם הבא. מגדיל ישועות מלכו, ועשה חסד למשיחו, לדוד ולזרעו עד עולם. עשה שלום במרומיו, הוא יעשה שלום עלינו ועל כל ישראל. ואמרו: אמן.""",
            english = "May the Merciful One reign over us forever and ever. May the Merciful One be blessed in heaven and on earth. May the Merciful One be praised for all generations, and be glorified through us forever and all eternity, and honored through us forever and ever. May the Merciful One grant us an honorable livelihood. May the Merciful One break the yoke from our neck and lead us upright to our land. May the Merciful One send abundant blessing into this house and upon this table at which we have eaten. May the Merciful One send us Elijah the Prophet - may he be remembered for good - who will bring us good tidings of salvation and comfort.\n\n[In father's house:] May the Merciful One bless my father, my teacher, the master of this house, and my mother, my teacher, the mistress of this house.\n[If married man:] May the Merciful One bless me (and my father and my mother) and my wife and my children and all that is mine.\n[If married woman:] May the Merciful One bless me (and my father and my mother) and my husband and my children and all that is mine.\n[If guest:] May the Merciful One bless the master and mistress of this house, them, and their household, and their children, and all that is theirs.\n\n[On Rosh Chodesh:] May the Merciful One renew for us this month for good and for blessing.\n[On Sukkot:] May the Merciful One restore for us the fallen sukkah of David.\n\nMay the Merciful One grant us the privilege of reaching the days of the Messiah and the life of the World to Come. He who makes great salvation for His king and shows kindness to His anointed one, to David and his descendants forever. He who makes peace in His heights, may He make peace for us and for all Israel. And say: Amen.",
            category = BirkatCategory.ADDITIONS
        ),
        BirkatSection(
            title = "פסוקים אחרונים",
            hebrew = """יראו את יי קדשיו, כי אין מחסור ליראיו. כפירים רשו ורעבו, ודרשי יי לא יחסרו כל טוב. הודו ליי כי טוב, כי לעולם חסדו. פותח את ידך, ומשביע לכל חי רצון. ברוך הגבר אשר יבטח ביי, והיה יי מבטחו. נער הייתי גם זקנתי, ולא ראיתי צדיק נעזב, וזרעו מבקש לחם. יי עז לעמו יתן, יי יברך את עמו בשלום.""",
            english = "Fear the Lord, you His holy ones, for those who fear Him suffer no want. Young lions may want and hunger, but those who seek the Lord shall not lack any good thing. Thank the Lord for He is good, His kindness endures forever. You open Your hand and satisfy the desire of every living thing. Blessed is the man who trusts in the Lord, and the Lord will be his security. I was young and now I am old, yet I have never seen a righteous man forsaken, nor his children begging for bread. The Lord will give strength to His people; The Lord will bless His people with peace.",
            category = BirkatCategory.ADDITIONS
        )
    )
} 
