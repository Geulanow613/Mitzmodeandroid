package com.beardytop.mitzmode.util

import com.beardytop.mitzmode.viewmodel.MitzModeViewModel

object RewardVideoAssets {
    private val MILESTONES = listOf(
        1, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1800,
    )

    fun nextMilestoneAfter(completedCount: Int): Int? =
        MILESTONES.firstOrNull { it > completedCount }

    fun videoIdForMilestone(milestoneCount: Int): Int = when (milestoneCount) {
        1 -> 1
        10 -> 2
        50 -> 3
        100 -> 4
        200 -> 5
        300 -> 6
        400 -> 7
        500 -> 8
        600 -> 9
        700 -> 10
        800 -> 11
        900 -> 12
        1000 -> 13
        1800 -> MitzModeViewModel.FINAL_REWARD_VIDEO_ID
        else -> 0
    }

    fun assetForVideoId(videoId: Int): String? = when {
        videoId <= 0 -> null
        videoId == MitzModeViewModel.FINAL_REWARD_VIDEO_ID -> "finalreward.mp4"
        else -> "mitzmodenew$videoId.mp4"
    }

    /** Asset for the next level-up clip the user will earn. */
    fun assetForNextMilestone(completedCount: Int): String? {
        val next = nextMilestoneAfter(completedCount) ?: return null
        return assetForVideoId(videoIdForMilestone(next))
    }
}
