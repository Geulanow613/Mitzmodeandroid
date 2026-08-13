import SwiftUI

struct FemaleChecklistSection: View {
    @ObservedObject var viewModel: DailyMitzvotViewModel
    
    var body: some View {
        VStack(alignment: .leading, spacing: 15) {
            // Daily Prayer Section
            SectionHeader(title: "Daily Prayer")
            
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
                text: "At least one prayer daily (typically morning)",
                explanation: "While men are obligated in three daily prayers, women are required to pray " +
                           "at least once per day. Many women choose to pray Shacharit (morning prayers). " +
                           "Some are lenient and offer a simple prayer comprised of praise, thanks, and a request.",
                link: nil,
                isChecked: viewModel.getItemState("Daily Prayer"),
                onToggle: { isChecked in viewModel.toggleItem("Daily Prayer") }
            )
            
            // Important Lifestyle Mitzvot Section
            SectionHeader(title: "Important Lifestyle Mitzvot")
            
            ChecklistItemWithInfo(
                text: "Have Mezuzot on your doorposts",
                explanation: "A mezuzah is a sacred scroll containing Torah verses that we affix to our doorposts:\n\n" +
                           "• Every Jewish home requires kosher mezuzot on all appropriate doorways\n" +
                           "• The mezuzah provides spiritual protection and blessings for the home and its inhabitants\n" +
                           "• It's crucial to use only certified kosher scrolls - many non-kosher ones exist in the market\n" +
                           "• Consult your local Chabad/Orthodox rabbi about which doorways need mezuzot and where to obtain kosher ones",
                link: ExternalLink(displayText: "Learn more about mezuzot", url: "https://www.chabad.org/library/article_cdo/aid/278476/jewish/Mezuzah.htm"),
                isChecked: viewModel.getItemState("Have Mezuzot"),
                onToggle: { isChecked in viewModel.toggleItem("Have Mezuzot") }
            )
            
            ChecklistItemWithInfo(
                text: "Cover hair in public (if married)",
                explanation: Constants.HEAD_COVERING_EXPLANATIONS["female"] ?? "",
                link: nil,
                isChecked: viewModel.getItemState("Cover hair"),
                onToggle: { isChecked in viewModel.toggleItem("Cover hair") }
            )
            
            ChecklistItemWithInfo(
                text: "Modesty (Tznius)",
                explanation: "Modesty in Judaism encompasses both dress and behavior:\n\n" +
                           "• Wearing clothing that covers the body appropriately\n" +
                           "• Speaking and acting in a way that doesn't draw unwanted attention\n" +
                           "• Conducting ourselves in a way that reflects Jewish values\n\n" +
                           "The goal is to emphasize our inner spiritual beauty and dignity as Jewish women. " +
                           "Different communities have varying standards - consult with a rabbi for guidance " +
                           "on what's appropriate for you.",
                link: nil,
                isChecked: viewModel.getItemState("Modesty"),
                onToggle: { isChecked in viewModel.toggleItem("Modesty") }
            )
            
            ChecklistItemWithInfo(
                text: "Keep Kosher",
                explanation: "Keeping kosher involves following Jewish dietary laws:\n\n" +
                           "• Eating only kosher animals (e.g., no pork or shellfish)\n" +
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
                text: "Family Purity Laws (if married)",
                explanation: "Family purity laws (Taharat HaMishpacha) are central to Jewish married life:\n\n" +
                           "• Observing the laws of niddah (separation during menstruation)\n" +
                           "• Immersing in a mikvah at the appropriate time\n" +
                           "• Understanding the calendar calculations\n\n" +
                           "These laws are considered one of the three primary mitzvot for Jewish women. " +
                           "They bring holiness and blessing to marriage. Please learn these laws from a " +
                           "qualified instructor before marriage.",
                link: nil,
                isChecked: viewModel.getItemState("Family Purity"),
                onToggle: { isChecked in viewModel.toggleItem("Family Purity") }
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
                text: "Torah Study",
                explanation: "While women's Torah study obligations differ from men's, it is important and meritorious for women to learn about the mitzvot in which they are obligated, such as:\n\n" +
                           "• Laws of Shabbat and Festivals\n" +
                           "• Kosher dietary laws\n" +
                           "• Laws of separating challah from dough\n" +
                           "• Family purity laws (for married women)\n" +
                           "• Laws of modest dress and conduct\n" +
                           "• Laws of prayer and blessings\n\n" +
                           "Learning these laws helps women fulfill mitzvot properly. The study should be approached in a pleasant and inspiring manner.\n\n" +
                           "Many wonderful resources are available specifically for women's Torah study, and classes are often offered by local rebbetzins and women teachers.",
                link: nil,
                isChecked: viewModel.getItemState("Torah Study"),
                onToggle: { isChecked in viewModel.toggleItem("Torah Study") }
            )
            
            // Daily Blessings Section
            SectionHeader(title: "Daily Blessings")
            
            ChecklistItemWithInfo(
                text: "Blessings before food",
                explanation: "Before eating or drinking, we acknowledge G-d as the source of all sustenance. " +
                           "Different foods have specific blessings, helping us maintain constant awareness " +
                           "of G-d's providence.",
                link: nil,
                isChecked: viewModel.getItemState("Blessings before food"),
                onToggle: { isChecked in viewModel.toggleItem("Blessings before food") }
            )
            
            ChecklistItemWithInfo(
                text: "Blessings after food",
                explanation: "After eating, we express gratitude to G-d. After bread, we recite Birkat " +
                           "Hamazon (Grace After Meals). Other foods require shorter blessings, each " +
                           "thanking G-d for specific types of nourishment.",
                link: nil,
                isChecked: viewModel.getItemState("Blessings after food"),
                onToggle: { isChecked in viewModel.toggleItem("Blessings after food") }
            )
            
            ChecklistItemWithInfo(
                text: "Asher Yatzar after using bathroom",
                explanation: "This blessing acknowledges the wonder of our body's functioning and thanks " +
                           "G-d for maintaining our health. It reminds us that even basic bodily " +
                           "functions are miraculous gifts worthy of gratitude. Check your siddur for the proper text.",
                link: nil,
                isChecked: viewModel.getItemState("Asher Yatzar"),
                onToggle: { isChecked in viewModel.toggleItem("Asher Yatzar") }
            )
            
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
            
            Text("There are many additional mitzvot that may apply at special times. Here are just a few examples:")
                .font(.body)
                .padding(.horizontal)
            
            Text("• Monthly: Rosh Chodesh\n" +
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
        }
        .padding(.horizontal)
    }
} 