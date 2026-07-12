import SwiftUI

struct MaleChecklistSection: View {
    @ObservedObject var viewModel: DailyMitzvotViewModel
    
    var body: some View {
        VStack(alignment: .leading, spacing: 15) {
            // Important Daily Mitzvot Section
            SectionHeader(title: "Important Daily Mitzvot")
            
            ChecklistItemWithInfo(
                text: "Ritual hand washing",
                explanation: "Upon waking, we perform ritual hand washing (netilat yadayim) to remove spiritual impurity from sleep:\n\n" +
                           "• Fill a cup with water\n" +
                           "• Pour first on right hand\n" +
                           "• Then on left hand\n" +
                           "• Continue alternating between hands until each hand is washed 3 times\n" +
                           "• Say the blessing 'Al Netilat Yadayim'\n\n" +
                           "Similar hand washing is required before prayer times throughout the day, " +
                           "though without a blessing. This washing helps prepare us to connect with G-d " +
                           "in a state of ritual purity.",
                link: ExternalLink(displayText: "Learn more about hand washing", url: "https://www.chabad.org/library/article_cdo/aid/7230155/jewish/What-You-Need-to-Know-About-Washing-Hands-Not-for-Bread.htm"),
                isChecked: viewModel.getItemState("Ritual hand washing"),
                onToggle: { isChecked in viewModel.toggleItem("Ritual hand washing") }
            )
            
            ChecklistItemWithInfo(
                text: "Wear a Kippah",
                explanation: "Jewish men wear a kippah (skullcap) as a sign of respect and acknowledgment that G-d is above us. " +
                           "While some authorities consider it a custom rather than a strict requirement, wearing a kippah is universally practiced by observant Jewish men and boys. " +
                           "It serves as a constant reminder of G-d's presence and our duty to live according to His will.",
                link: nil,
                isChecked: viewModel.getItemState("Wear Kippah"),
                onToggle: { isChecked in viewModel.toggleItem("Wear Kippah") }
            )
            
            ChecklistItemWithInfo(
                text: "Put on Tefillin",
                explanation: "Tefillin are sacred black leather boxes containing Torah verses, worn on the arm and head during morning prayers. " +
                           "They help us to direct our thoughts and actions to G-d's service, and connect us to Him. They can be worn later if not possible during morning prayers (but not at night).\n\n" +
                           "While tefillin are a significant investment, they are one of the most important items a Jewish man can own. " +
                           "It's crucial to purchase only certified kosher tefillin from a reliable source, as there are many non-kosher ones on the market. " +
                           "Consult your local Chabad/Orthodox rabbi for guidance on purchasing the right set and learning how to put them on properly.",
                link: nil,
                isChecked: viewModel.getItemState("Put on Tefillin"),
                onToggle: { isChecked in viewModel.toggleItem("Put on Tefillin") }
            )
            
            ChecklistItemWithInfo(
                text: "Wear Tzitzit",
                explanation: "Tzitzit are special fringes worn on a four-cornered garment. While technically only required when wearing such a garment, " +
                           "it's highly recommended to wear a tallit katan all day and tallit gadol during morning prayers for spiritual protection.",
                link: nil,
                isChecked: viewModel.getItemState("Wear Tzitzit"),
                onToggle: { isChecked in viewModel.toggleItem("Wear Tzitzit") }
            )
            
            ChecklistItemWithInfo(
                text: "Keep Kosher",
                explanation: "Keeping kosher involves following Jewish dietary laws:\n\n" +
                           "• Using only kosher-certified products\n" +
                           "• Separating meat and dairy completely\n" +
                           "• Using separate dishes, utensils, and appliances for meat and dairy\n" +
                           "• Waiting the required time between eating meat and dairy\n" +
                           "• Using only kosher wine and grape products\n" +
                           "• Special rules for Passover\n\n" +
                           "These laws elevate eating into a spiritual act and help maintain Jewish identity. " +
                           "Please consult with a rabbi for guidance on implementing kosher practices in your home.",
                link: nil,
                isChecked: viewModel.getItemState("Keep Kosher"),
                onToggle: { isChecked in viewModel.toggleItem("Keep Kosher") }
            )
            
            ChecklistItemWithInfo(
                text: "Have Mezuzot on your doorposts",
                explanation: "A mezuzah is a sacred scroll containing Torah verses that we affix to our doorposts:\n\n" +
                           "• Every Jewish home requires kosher mezuzot on all appropriate doorways\n" +
                           "• The mezuzah provides spiritual protection and blessings for the home and its inhabitants\n" +
                           "• It's crucial to use only certified kosher scrolls - many non-kosher ones exist in the market\n" +
                           "• Consult your local Chabad/Orthodox rabbi about which doorways need mezuzot and where to obtain kosher ones",
                link: nil,
                isChecked: viewModel.getItemState("Have Mezuzot"),
                onToggle: { isChecked in viewModel.toggleItem("Have Mezuzot") }
            )
            
            ChecklistItemWithInfo(
                text: "Immerse food vessels in mikveh",
                explanation: "Elevate your kitchen utensils through the beautiful mitzvah of tevilat keilim (vessel immersion):\n\n" +
                           "• New food vessels bought from non-Jewish sources need immersion in a mikveh or suitable body of water\n" +
                           "• This spiritually prepares them for use in kosher food preparation\n" +
                           "• Metal and glass items require immersion\n" +
                           "• Items like plastic, wood, or disposables don't need immersion\n\n" +
                           "This special mitzvah transforms ordinary eating into a holy act. Check with your local rabbi about specific items and the nearest mikveh location.",
                link: nil,
                isChecked: viewModel.getItemState("Immerse vessels"),
                onToggle: { isChecked in viewModel.toggleItem("Immerse vessels") }
            )
            
            ChecklistItemWithInfo(
                text: "100 Daily Blessings",
                explanation: "The goal is to say 100 blessings each day. On weekdays, this happens naturally through daily prayers and food blessings. On Shabbat and Festivals when prayers are shorter, make up the count with blessings on snacks and fragrances. On Yom Kippur, when we don't eat, there are other ways to make up the count.",
                link: ExternalLink(displayText: "Click here for more information", url: "https://aish.com/43-100-blessings-each-day/"),
                isChecked: viewModel.getItemState("100 Daily Blessings"),
                onToggle: { isChecked in viewModel.toggleItem("100 Daily Blessings") }
            )
            
            // Morning Prayer Section
            SectionHeader(title: "Morning Prayer (Shacharit)")
            
            ChecklistItemWithInfo(
                text: "Morning Blessings (Birchot HaShachar)",
                explanation: "These are blessings said at the start of the day thanking G-d for basic functions " +
                           "and needs. They help us begin each day with gratitude and awareness. Saying Korbanot is recommended, but at bare minimum some rabbis require saying the " +
                           "Parsha HaTamid, the Torah verses explaining the daily tamid offering.",
                link: nil,
                isChecked: viewModel.getItemState("Morning Blessings"),
                onToggle: { isChecked in viewModel.toggleItem("Morning Blessings") }
            )
            
            ChecklistItemWithInfo(
                text: "Torah Blessings + minimal Torah study",
                explanation: "Before studying Torah, we recite blessings acknowledging G-d as the giver of Torah. " +
                           "We then fulfill the daily obligation to study Torah, by reading the passages that follow in the siddur.",
                link: nil,
                isChecked: viewModel.getItemState("Torah Blessings"),
                onToggle: { isChecked in viewModel.toggleItem("Torah Blessings") }
            )
            
            ChecklistItemWithInfo(
                text: "Minimum Pesukei D'Zimra",
                explanation: "These are verses of praise from Psalms and other sources that prepare us for prayer. " +
                           "The minimum is Baruch She'amar, Ashrei, and Yishtabach.",
                link: nil,
                isChecked: viewModel.getItemState("Pesukei D'Zimra"),
                onToggle: { isChecked in viewModel.toggleItem("Pesukei D'Zimra") }
            )
            
            ChecklistItemWithInfo(
                text: "Morning Shema with its blessings",
                explanation: "The Shema is a central declaration of Jewish faith, recited with blessings before and after. " +
                           "It must be said within the first quarter of the day.",
                link: nil,
                isChecked: viewModel.getItemState("Morning Shema"),
                onToggle: { isChecked in viewModel.toggleItem("Morning Shema") }
            )
            
            ChecklistItemWithInfo(
                text: "Shemoneh Esrei",
                explanation: "Also called Amidah, this is the central prayer consisting of 19 blessings (18 originally). " +
                           "It's recited standing, facing Jerusalem, three times daily.\n\n" +
                           "On most weekdays, Tachanun (prayers of repentance) is recited immediately after. However, it's omitted on festive days and certain other occasions. " +
                           "Follow your siddur's instructions or the congregation if you're at synagogue.",
                link: nil,
                isChecked: viewModel.getItemState("Shemoneh Esrei"),
                onToggle: { isChecked in viewModel.toggleItem("Shemoneh Esrei") }
            )
            
            // Additional Prayers Section
            SectionHeader(title: "Additional Prayers")
            
            ChecklistItemWithInfo(
                text: "Musaf (only on Rosh Chodesh, Festivals, and Shabbat)",
                explanation: "The additional prayer service recited after Shacharit on special days, " +
                           "commemorating the additional Temple offerings that were brought on these occasions." +
                           " (Don't use your phone on Shabbat or festivals though!)",
                link: nil,
                isChecked: viewModel.getItemState("Musaf"),
                onToggle: { isChecked in viewModel.toggleItem("Musaf") }
            )
            
            // Afternoon Prayer Section
            SectionHeader(title: "Afternoon Prayer")
            
            ChecklistItemWithInfo(
                text: "Mincha - Shemoneh Esrei",
                explanation: "The afternoon prayer service, centered around the Shemoneh Esrei. " +
                           "It should be said after midday and before sunset. This prayer helps us " +
                           "pause during our busy day to reconnect with G-d.\n\n" +
                           "Like in the morning service, Tachanun is recited after Shemoneh Esrei on most weekdays, but omitted on festive days and certain occasions. " +
                           "Follow your siddur's instructions or the congregation if you're at synagogue.",
                link: nil,
                isChecked: viewModel.getItemState("Mincha"),
                onToggle: { isChecked in viewModel.toggleItem("Mincha") }
            )
            
            // Evening Requirements Section
            SectionHeader(title: "Evening Requirements")
            
            ChecklistItemWithInfo(
                text: "Evening Shema with its blessings",
                explanation: "The evening Shema must be recited after nightfall. Like the morning Shema, " +
                           "it's surrounded by appropriate blessings. This mitzvah can be fulfilled " +
                           "any time during the night.",
                link: nil,
                isChecked: viewModel.getItemState("Evening Shema"),
                onToggle: { isChecked in viewModel.toggleItem("Evening Shema") }
            )
            
            ChecklistItemWithInfo(
                text: "Maariv Shemoneh Esrei",
                explanation: "The evening Shemoneh Esrei prayer. While technically optional according to " +
                           "some opinions, it has become obligatory through universal Jewish custom.",
                link: nil,
                isChecked: viewModel.getItemState("Maariv"),
                onToggle: { isChecked in viewModel.toggleItem("Maariv") }
            )
            
            ChecklistItemWithInfo(
                text: "Torah Study",
                explanation: "It's a mitzvah to learn at least a little Torah each day and each night. " +
                           "While the more Torah study the better, even learning just a few laws or " +
                           "something inspiring fulfills this obligation. Try to set aside some time " +
                           "before bed to connect with G-d's wisdom through Torah study.",
                link: nil,
                isChecked: viewModel.getItemState("Torah Study"),
                onToggle: { isChecked in viewModel.toggleItem("Torah Study") }
            )
            
            ChecklistItemWithInfo(
                text: "Bedtime Shema (first paragraph - though recommended to say entire Shema for spiritual protection)",
                explanation: "Reciting Shema before sleep provides spiritual protection through the night. " +
                           "While only the first paragraph is strictly required, it's recommended to " +
                           "say the entire Shema and associated prayers for maximum protection.",
                link: nil,
                isChecked: viewModel.getItemState("Bedtime Shema"),
                onToggle: { isChecked in viewModel.toggleItem("Bedtime Shema") }
            )
            
            ChecklistItemWithInfo(
                text: "Hamapil blessing (according to many opinions)",
                explanation: "A beautiful blessing thanking G-d for the gift of sleep and asking for " +
                           "protection through the night. While some consider it optional, many authorities " +
                           "recommend saying it nightly. Check your siddur for the text.",
                link: nil,
                isChecked: viewModel.getItemState("Hamapil"),
                onToggle: { isChecked in viewModel.toggleItem("Hamapil") }
            )
            
            // Other Daily Requirements Section
            SectionHeader(title: "Other Daily Requirements")
            
            ChecklistItemWithInfo(
                text: "Blessings before food",
                explanation: "We acknowledge G-d as the source of our sustenance by reciting specific " +
                           "blessings before eating, with different blessings for different types of food.",
                link: nil,
                isChecked: viewModel.getItemState("Blessings before food"),
                onToggle: { isChecked in viewModel.toggleItem("Blessings before food") }
            )
            
            ChecklistItemWithInfo(
                text: "Weekly Parsha reading (twice in Hebrew, once in Targum)",
                explanation: "There is a mitzvah to read the weekly Torah portion (Parsha) twice in Hebrew and once in the Targum (Aramaic translation) each week, completing it before Shabbat (except during holidays when there is a separate holiday reading). This practice is called Shnayim Mikra v'Echad Targum. The goal is to become familiar with the Torah portion that will be read in synagogue on Shabbat.\n\n" +
                           "Some rabbis say that if you have no ability to read Hebrew, you can read the parsha twice and the translation of the targum once in the language you speak (Instead of Hebrew/Aramaic - but this is not a universally accepted leniency). Many people spread this out throughout the week, reading a small portion each day.",
                link: nil,
                isChecked: viewModel.getItemState("Weekly Parsha"),
                onToggle: { isChecked in viewModel.toggleItem("Weekly Parsha") }
            )
            
            ChecklistItemWithInfo(
                text: "Blessings after food",
                explanation: "After eating, we thank G-d for the food. After bread, we recite Birkat " +
                           "Hamazon (Grace After Meals). Other foods require shorter blessings of thanks.",
                link: nil,
                isChecked: viewModel.getItemState("Blessings after food"),
                onToggle: { isChecked in viewModel.toggleItem("Blessings after food") }
            )
            
            ChecklistItemWithInfo(
                text: "Asher Yatzar after using bathroom",
                explanation: "A blessing thanking G-d for our body's proper functioning. It reminds us " +
                           "that even the most basic bodily functions are miraculous and worthy of gratitude. " +
                           "Check your siddur for the proper text.",
                link: nil,
                isChecked: viewModel.getItemState("Asher Yatzar"),
                onToggle: { isChecked in viewModel.toggleItem("Asher Yatzar") }
            )
            
            // Recommended Resources Section
            SectionHeader(title: "Recommended Resources")
            
            Text("Note: To properly fulfill these mitzvot, you'll need a good siddur (prayer book). There are several options available:")
                .font(.body)
                .padding(.horizontal)
            
            Text("• Many free siddur apps are available on your phone's app store\n" +
                 "• ArtScroll offers nice printed siddurim in various formats")
                .font(.body)
                .padding(.horizontal)
                .padding(.leading, 16)
            
            AppLinkText(
                displayText: "Browse printed siddurim options",
                urlString: "https://www.artscroll.com/Categories/PBK.html",
                fontSize: 16
            )
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.horizontal)
            
            // Shabbat and Festivals Section
            SectionHeader(title: "Shabbat and Festivals")
            
            ChecklistItemWithInfo(
                text: "Prepare for and observe Shabbat and Festivals",
                explanation: "Shabbat and Jewish Festivals are sacred times that elevate us spiritually:\n\n" +
                           "• Light candles before sunset to welcome these holy days\n" +
                           "• Prepare food and our home in advance\n" +
                           "• Refrain from work activities (melacha) such as using electronics, driving, handling money etc.\n" +
                           "• Enjoy festive meals with family and community\n" +
                           "• Focus on prayer, Torah study, and spiritual growth\n\n" +
                           "Proper observance of these days can bring our souls unique feelings of holiness " +
                           "and closeness with G-d that are unattainable during regular days. These elevated " +
                           "spiritual states can be experienced through proper observance of the laws.",
                link: nil,
                isChecked: viewModel.getItemState("Shabbat and Festivals"),
                onToggle: { isChecked in viewModel.toggleItem("Shabbat and Festivals") }
            )
            
            // Special Mitzvot Notes
            SectionHeader(title: "Special Mitzvot Notes")
            
            Text("There are many additional mitzvot that apply at special times. Here are just a few examples:")
                .font(.body)
                .padding(.horizontal)
            
            Text("• Monthly: Kiddush HaLevana, Rosh Chodesh\n" +
                 "• Seasonal: Counting the Omer (between Pesach and Shavuot)\n" +
                 "• Festivals: Eating Matzah, Hearing Shofar, Lulav/Etrog, Hannukah, Purim")
                .font(.body)
                .padding(.horizontal)
            
            VStack(alignment: .leading, spacing: 8) {
                Text("To check which major upcoming Jewish events/holidays/mitzvot are happening currently, check out the Chabad.org calendar:")
                    .font(.body)
                    .foregroundColor(AppColor.textPrimary)
                AppLinkText(
                    displayText: "Chabad.org calendar",
                    urlString: "https://www.chabad.org/calendar/view/month.htm",
                    fontSize: 16
                )
                .frame(maxWidth: .infinity, alignment: .leading)
            }
            .padding(.horizontal)
            
            Text("This list covers basic requirements in terms of prayer, blessings, and lifestyle needed to fulfill daily Torah and rabbinic mitzvot. However, there are many other mitzvot in areas such as business, proper speech, relationships, and more that are beyond the scope of this list. Please consult a rabbi and reliable Jewish sources to learn about all the mitzvot relevant to your situation.")
                .font(.body)
                .padding(.horizontal)
                .padding(.bottom, 16)
            
            // Divider and Electronics Warning
            Divider()
                .padding(.vertical, 8)
                .background(Color(UIColor.systemGray4))
            
            Text("Important Note About Electronics")
                .font(.headline)
                .fontWeight(.bold)
                .padding(.top, 8)
                .padding(.bottom, 4)
            
            Text("Please refrain from using phones, computers, or any electronics during Shabbat and Festivals, as these are holy days of complete rest.")
                .font(.subheadline)
                .foregroundColor(.red)
                .padding(.bottom, 16)
        }
        .padding(.horizontal)
    }
} 