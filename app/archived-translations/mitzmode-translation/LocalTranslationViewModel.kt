package com.beardytop.mitzmode.ui

import androidx.compose.runtime.compositionLocalOf
import com.beardytop.mitzmode.viewmodel.TranslationViewModel

/** Activity-scoped [TranslationViewModel]; prefer this over nested `hiltViewModel()` (menus/popups). */
val LocalTranslationViewModel = compositionLocalOf<TranslationViewModel?> { null }
