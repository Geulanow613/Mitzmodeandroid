package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.withStyle

data class Bracha(
    val name: String,
    val hebrew: AnnotatedString,
    val english: AnnotatedString,
    val description: String
)

@Composable
fun BrachotDialog(
    onDismiss: () -> Unit
) {
    var fontScale by remember { mutableStateOf(1f) }
    
    val transformableState = rememberTransformableState { zoomChange, _, _ ->
        fontScale = (fontScale * zoomChange).coerceIn(0.5f, 3f)
    }

    val scaledFontSize = (MaterialTheme.typography.bodyLarge.fontSize.value * fontScale).sp

    val brachot = listOf(
        // Body Functions Blessing
        Bracha(
            name = "Asher Yatzar",
            hebrew = buildAnnotatedString { append("בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם אֲשֶׁר יָצַר אֶת הָאָדָם בְּחָכְמָה וּבָרָא בוֹ נְקָבִים נְקָבִים חֲלוּלִים חֲלוּלִים גָּלוּי וְיָדוּעַ לִפְנֵי כִסֵּא כְבוֹדֶךָ שֶׁאִם יִפָּתֵחַ אֶחָד מֵהֶם אוֹ יִסָּתֵם אֶחָד מֵהֶם אִי אֶפְשַׁר לְהִתְקַיֵּם וְלַעֲמוֹד לְפָנֶיךָ אֲפִילוּ שָׁעָה אֶחָת בָּרוּךְ אַתָּה ה' רוֹפֵא כָל בָּשָׂר וּמַפְלִיא לַעֲשׂוֹת") },
            english = buildAnnotatedString { append("Blessed are You, Lord our God, King of the universe, who formed man with wisdom and created within him many openings and many hollow spaces. It is obvious and known before Your Throne of Glory that if even one of them would be opened, or if even one of them would be sealed, it would be impossible to survive and to stand before You even for one hour. Blessed are You, Lord, who heals all flesh and acts wondrously.") },
            description = "After using the bathroom"
        ),
        
        // Before Food Blessings
        Bracha(
            name = "Hamotzi",
            hebrew = buildAnnotatedString { append("בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם הַמּוֹצִיא לֶחֶם מִן הָאָרֶץ") },
            english = buildAnnotatedString { append("Blessed are You, Lord our God, King of the universe, who brings forth bread from the earth") },
            description = "For bread"
        ),
        Bracha(
            name = "Mezonot",
            hebrew = buildAnnotatedString { append("בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם בּוֹרֵא מִינֵי מְזוֹנוֹת") },
            english = buildAnnotatedString { append("Blessed are You, Lord our God, King of the universe, who creates various kinds of sustenance") },
            description = "After eating grain products (not bread), wine/grape juice, or fruits of the 7 species"
        ),
        Bracha(
            name = "Hagafen",
            hebrew = buildAnnotatedString { append("בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם בּוֹרֵא פְּרִי הַגָּפֶן") },
            english = buildAnnotatedString { append("Blessed are You, Lord our God, King of the universe, who creates the fruit of the vine") },
            description = "For wine and grape juice"
        ),
        Bracha(
            name = "Ha'etz",
            hebrew = buildAnnotatedString { append("בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם בּוֹרֵא פְּרִי הָעֵץ") },
            english = buildAnnotatedString { append("Blessed are You, Lord our God, King of the universe, who creates the fruit of the tree") },
            description = "For tree fruits"
        ),
        Bracha(
            name = "Ha'adama",
            hebrew = buildAnnotatedString { append("בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם בּוֹרֵא פְּרִי הָאֲדָמָה") },
            english = buildAnnotatedString { append("Blessed are You, Lord our God, King of the universe, who creates the fruit of the earth") },
            description = "For vegetables and ground fruits"
        ),
        Bracha(
            name = "Shehakol",
            hebrew = buildAnnotatedString { append("בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם שֶׁהַכֹּל נִהְיָה בִּדְבָרוֹ") },
            english = buildAnnotatedString { append("Blessed are You, Lord our God, King of the universe, by whose word all things came to be") },
            description = "For everything else (meat, dairy, drinks)"
        ),
        
        // After Food Blessing
        Bracha(
            name = "Borei Nefashot",
            hebrew = buildAnnotatedString { append("בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם בּוֹרֵא נְפָשׁוֹת רַבּוֹת וְחֶסְרוֹנָן עַל כָּל מַה שֶּׁבָּרָאתָ לְהַחֲיוֹת בָּהֶם נֶפֶשׁ כָּל חָי בָּרוּךְ חֵי הָעוֹלָמִים") },
            english = buildAnnotatedString { append("Blessed are You, Lord our God, King of the universe, Who creates numerous living things with their deficiencies, for all that You have created with which to sustain the life of every being. Blessed is He who is the Life of the worlds") },
            description = "After-blessing for foods/drinks that don't require Birkat Hamazon or the Three-faceted Blessing"
        ),
        
        // Natural Phenomena
        Bracha(
            name = "Shehecheyanu",
            hebrew = buildAnnotatedString { append("בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם שֶׁהֶחֱיָנוּ וְקִיְּמָנוּ וְהִגִּיעָנוּ לַזְּמַן הַזֶּה") },
            english = buildAnnotatedString { append("Blessed are You, Lord our God, King of the universe, who has granted us life, sustained us, and enabled us to reach this occasion") },
            description = "For new experiences, seasonal fruits, holidays"
        ),
        Bracha(
            name = "Oseh Ma'aseh Bereishit",
            hebrew = buildAnnotatedString { append("בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם עוֹשֶׂה מַעֲשֵׂה בְרֵאשִׁית") },
            english = buildAnnotatedString { append("Blessed are You, Lord our God, King of the universe, who does the work of creation") },
            description = "On lightning, shooting stars, earthquakes, strong winds"
        ),
        Bracha(
            name = "Birkat Ha'Ilanot",
            hebrew = buildAnnotatedString { append("בָּרוּךְ אַתָּה ה' אֱלֹקֵינוּ מֶלֶךְ הָעוֹלָם שֶׁלֹּא חִסַּר בְּעוֹלָמוֹ כְּלוּם וּבָרָא בוֹ בְּרִיּוֹת טוֹבוֹת וְאִילָנוֹת טוֹבוֹת לֵהָנוֹת בָּהֶם בְּנֵי אָדָם") },
            english = buildAnnotatedString { append("Blessed are You, Lord our God, King of the Universe, who has made nothing lacking in His universe, and created within it good creatures and good trees to give pleasure to mankind") },
            description = "Blessing on blossoming fruit trees during the month of Nissan"
        ),
        Bracha(
            name = "Three-faceted Blessing",
            hebrew = buildAnnotatedString { append("בָּרוּךְ אַתָּה ה' אֱלֹקֵינוּ מֶלֶךְ הָעוֹלָם עַל הַמִּחְיָה וְעַל הַכַּלְכָּלָה וְעַל הַגֶּפֶן וְעַל פְּרִי הַגֶּפֶן וְעַל הָעֵץ וְעַל פְּרִי הָעֵץ וְעַל תְּנוּבַת הַשָּׂדֶה וְעַל אֶרֶץ חֶמְדָּה טוֹבָה וּרְחָבָה שֶׁרָצִיתָ וְהִנְחַלְתָּ לַאֲבוֹתֵינוּ לֶאֱכוֹל מִפִּרְיָהּ וְלִשְׂבּוֹעַ מִטּוּבָהּ. רַחֵם ה' אֱלֹקֵינוּ עַל יִשְׂרָאֵל עַמֶּךָ וְעַל יְרוּשָׁלַיִם עִירֶךָ וְעַל צִיּוֹן מִשְׁכַּן כְּבוֹדֶךָ וְעַל מִזְבְּחֶךָ וְעַל הֵיכָלֶךָ וּבְנֵה יְרוּשָׁלַיִם עִיר הַקֹּדֶשׁ בִּמְהֵרָה בְיָמֵינוּ וְהַעֲלֵנוּ לְתוֹכָהּ וְשַׂמְּחֵנוּ בְּבִנְיָנָהּ וְנֹאכַל מִפִּרְיָהּ וְנִשְׂבַּע מִטּוּבָהּ וּנְבָרֶכְךָ עָלֶיהָ בִּקְדֻשָּׁה וּבְטָהֳרָה כִּי אַתָּה ה' טוֹב וּמֵטִיב לַכֹּל וְנוֹדֶה לְךָ עַל הָאָרֶץ וְעַל הַמִּחְיָה וְעַל הַכַּלְכָּלָה בָּרוּךְ אַתָּה ה' עַל הָאָרֶץ וְעַל הַמִּחְיָה") },
            english = buildAnnotatedString { append("Blessed are You, Lord our God, King of the universe, for the sustenance and the nourishment, and for the vine and the fruit of the vine, and for the tree and the fruit of the tree, for the produce of the field, and for the precious, good, and spacious land which You have graciously given as a heritage to our ancestors, to eat of its fruit and to be satiated with its goodness. Have mercy, Lord our God, on Israel Your people, on Jerusalem Your city, on Zion the abode of Your glory, on Your altar and on Your Temple. Rebuild Jerusalem, the holy city, speedily in our days, and bring us up to it and make us rejoice in it, and we will eat of its fruit and be satisfied with its goodness, and we will bless You in holiness and purity. For You, Lord, are good and do good to all, and we offer thanks to You for the land and for the sustenance and nourishment. Blessed are You, Lord, for the land and for the sustenance.") },
            description = "After eating grain products (not bread), wine/grape juice, or fruits of the 7 species"
        )
    )

    ParchmentDialog(
        onDismiss = onDismiss,
        title = "Blessings",
        confirmButton = { GoldButton(onClick = onDismiss, text = "Close") }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 540.dp)
                .transformable(transformableState)
        ) {
            items(brachot) { bracha ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    TranslatableText(
                        text = bracha.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = DialogGoldBorder
                    )
                    TranslatableText(
                        text = bracha.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = DialogTextMuted,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = bracha.hebrew,
                        fontSize = scaledFontSize,
                        color = DialogTextPrimary,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                    Text(
                        text = bracha.english,
                        fontSize = scaledFontSize,
                        color = DialogTextPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = DialogGoldBorder.copy(alpha = 0.25f),
                        thickness = 0.8.dp
                    )
                }
            }
        }
    }
}
