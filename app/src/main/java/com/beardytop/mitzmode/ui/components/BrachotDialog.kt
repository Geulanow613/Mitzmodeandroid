package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.beardytop.mitzmode.ui.LocalTranslationViewModel
import com.beardytop.mitzmode.viewmodel.TranslationViewModel

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
    val translationViewModel: TranslationViewModel =
        LocalTranslationViewModel.current ?: hiltViewModel()
    val currentLanguage by translationViewModel.currentLanguage.collectAsState()
    /** Hebrew liturgy is already Hebrew — hide translation toggle only for Hebrew UI. */
    val showLiturgyTranslation = currentLanguage != "he"

    var fontScale by remember { mutableStateOf(1f) }
    var showEnglish by remember { mutableStateOf(false) }

    val transformableState = rememberTransformableState { zoomChange, _, _ ->
        fontScale = (fontScale * zoomChange).coerceIn(0.5f, 3f)
    }

    val scaledFontSize = (MaterialTheme.typography.bodyLarge.fontSize.value * fontScale).sp

    val brachot = listOf(
        Bracha(
            name = "Asher Yatzar",
            hebrew = buildAnnotatedString { append("בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם אֲשֶׁר יָצַר אֶת הָאָדָם בְּחָכְמָה וּבָרָא בוֹ נְקָבִים נְקָבִים חֲלוּלִים חֲלוּלִים גָּלוּי וְיָדוּעַ לִפְנֵי כִסֵּא כְבוֹדֶךָ שֶׁאִם יִפָּתֵחַ אֶחָד מֵהֶם אוֹ יִסָּתֵם אֶחָד מֵהֶם אִי אֶפְשַׁר לְהִתְקַיֵּם וְלַעֲמוֹד לְפָנֶיךָ אֲפִילוּ שָׁעָה אֶחָת בָּרוּךְ אַתָּה ה' רוֹפֵא כָל בָּשָׂר וּמַפְלִיא לַעֲשׂוֹת") },
            english = buildAnnotatedString { append("Blessed are You, Lord our God, King of the universe, who formed man with wisdom and created within him many openings and many hollow spaces. It is obvious and known before Your Throne of Glory that if even one of them would be opened, or if even one of them would be sealed, it would be impossible to survive and to stand before You even for one hour. Blessed are You, Lord, who heals all flesh and acts wondrously.") },
            description = "After using the bathroom — each time you finish. If you are ill (e.g. diarrhea) and may need to go again right away, wait until you are reasonably sure you have finished for that round before saying it."
        ),
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
            description = "Before eating: grain foods that are not bread — cake, cookies, crackers, pasta, cereal (from wheat, barley, spelt, oat, or rye)"
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
            description = "Before eating: tree fruits (not shivat ha-minim — use Ha'adama for ground produce)"
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
        Bracha(
            name = "Borei Nefashot",
            hebrew = buildAnnotatedString { append("בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם בּוֹרֵא נְפָשׁוֹת רַבּוֹת וְחֶסְרוֹנָן עַל כָּל מַה שֶּׁבָּרָאתָ לְהַחֲיוֹת בָּהֶם נֶפֶשׁ כָּל חָי בָּרוּךְ חֵי הָעוֹלָמִים") },
            english = buildAnnotatedString { append("Blessed are You, Lord our God, King of the universe, Who creates numerous living things with their deficiencies, for all that You have created with which to sustain the life of every being. Blessed is He who is the Life of the worlds") },
            description = "After-blessing for foods/drinks that don't require Birkat Hamazon or Bracha Me'ein Shalosh"
        ),
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
    )

    ParchmentDialog(
        onDismiss = onDismiss,
        title = "Blessings",
        confirmButton = { GoldButton(onClick = onDismiss, text = "Close") }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (showLiturgyTranslation) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TranslatableText(
                        text = "Show translation",
                        style = MaterialTheme.typography.labelLarge,
                        color = DialogTextPrimary
                    )
                    Switch(
                        checked = showEnglish,
                        onCheckedChange = { showEnglish = it }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 520.dp)
                    .transformable(transformableState)
            ) {
                item(key = "mein_shalosh") {
                    AlHaMichyaBlessingCard(
                        fontScale = fontScale,
                        showEnglish = if (showLiturgyTranslation) showEnglish else false,
                        onShowEnglishChange = if (showLiturgyTranslation) {
                            { showEnglish = it }
                        } else {
                            null
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = DialogGoldBorder.copy(alpha = 0.25f),
                        thickness = 0.8.dp
                    )
                }

                items(brachot, key = { it.name }) { bracha ->
                    BrachaListItem(
                        bracha = bracha,
                        showEnglish = showEnglish && showLiturgyTranslation,
                        scaledFontSize = scaledFontSize
                    )
                }
            }
        }
    }
}

@Composable
private fun BrachaListItem(
    bracha: Bracha,
    showEnglish: Boolean,
    scaledFontSize: androidx.compose.ui.unit.TextUnit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        TranslatableText(
            text = bracha.name,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = DialogGoldBorder
        )
        TranslatableText(
            text = bracha.description,
            style = MaterialTheme.typography.bodySmall,
            color = DialogTextMuted,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        if (showEnglish) {
            LiturgyTranslationText(
                text = bracha.english.text,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = scaledFontSize),
                color = DialogTextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            )
        } else {
            Text(
                text = bracha.hebrew,
                fontSize = scaledFontSize,
                color = DialogTextPrimary,
                textAlign = TextAlign.Center,
                style = TextStyle(textDirection = TextDirection.Rtl),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = DialogGoldBorder.copy(alpha = 0.25f),
            thickness = 0.8.dp
        )
    }
}
