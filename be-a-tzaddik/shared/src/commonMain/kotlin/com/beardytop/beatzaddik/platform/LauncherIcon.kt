package com.beardytop.beatzaddik.platform

import com.beardytop.beatzaddik.domain.Gender

/** Swaps the home-screen launcher icon (male Tzaddik vs female Tzadeket). */
expect fun applyLauncherIcon(gender: Gender)
