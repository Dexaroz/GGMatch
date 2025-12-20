package com.pamn.ggmatch.app.architecture.model.profile.riotAccount

import com.pamn.ggmatch.app.architecture.sharedKernel.domain.ValueObject

data class SoloqStats(
    val tier: String,
    val division: String?,
    val leaguePoints: Int,
    val wins: Int,
    val losses: Int,
    val hotStreak: Boolean,
    val inactive: Boolean,
) : ValueObject {
    val totalGames: Int get() = wins + losses
    val winRate: Double get() = if (totalGames == 0) 0.0 else wins.toDouble() / totalGames.toDouble()
}
