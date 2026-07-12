package com.beardytop.beatzaddik.domain

/** English catalog keys for reward tiers. */
object MitzvahLevels {
    const val SECULAR = "Secular"
    const val BEGINNER = "Beginner"
    const val BAAL_TESHUVA = "Ba'al Teshuva"
    const val MASTER_CHOLENT_CHEF = "Master Cholent Chef"
    const val ASPIRING_KIDDUSH_MAKER = "Aspiring Kiddush Maker"
    const val ASSISTANT_GABBAI = "Assistant Gabbai"
    const val CANDY_AT_SHUL = "Guy who hands out candy at shul"
    const val WESTERN_WALL_REVELER = "Western Wall Reveler"
    const val SOFER = "Sofer"
    const val TZADDIK = "Tzaddik"
    const val LIVING_SEFER_TORAH = "Living Sefer Torah"
    const val ELIYAHU_HANAVI = "Eliyahu HaNavi"
    const val KING_DAVID = "King David"
    const val MOSHIACH = "Moshiach!!!"
    const val MITZ_MODE = "Mitz Mode!"

    const val FINAL_REWARD_THRESHOLD = 1800
    const val FINAL_REWARD_VIDEO_ID = 100

    fun forCount(count: Int): String = when {
        count <= 0 -> SECULAR
        count in 1..9 -> BEGINNER
        count in 10..49 -> BAAL_TESHUVA
        count in 50..99 -> MASTER_CHOLENT_CHEF
        count in 100..199 -> ASPIRING_KIDDUSH_MAKER
        count in 200..299 -> ASSISTANT_GABBAI
        count in 300..399 -> CANDY_AT_SHUL
        count in 400..499 -> WESTERN_WALL_REVELER
        count in 500..599 -> SOFER
        count in 600..699 -> TZADDIK
        count in 700..799 -> LIVING_SEFER_TORAH
        count in 800..899 -> ELIYAHU_HANAVI
        count in 900..999 -> KING_DAVID
        count in 1000..1799 -> MOSHIACH
        count >= FINAL_REWARD_THRESHOLD -> MITZ_MODE
        else -> BEGINNER
    }

    fun starCount(levelKey: String): Int = when (levelKey) {
        SECULAR, MITZ_MODE -> 0
        BEGINNER -> 1
        BAAL_TESHUVA -> 2
        MASTER_CHOLENT_CHEF -> 3
        ASPIRING_KIDDUSH_MAKER -> 4
        ASSISTANT_GABBAI -> 5
        CANDY_AT_SHUL -> 6
        WESTERN_WALL_REVELER -> 7
        SOFER -> 8
        TZADDIK -> 9
        LIVING_SEFER_TORAH -> 10
        ELIYAHU_HANAVI -> 11
        KING_DAVID -> 12
        MOSHIACH -> 13
        else -> 0
    }
}
