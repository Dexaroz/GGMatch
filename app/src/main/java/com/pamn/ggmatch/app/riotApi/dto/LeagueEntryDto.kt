package com.pamn.ggmatch.app.riotApi.dto

data class LeagueEntryDto(
    val queueType: String,
    val tier: String,
    val rank: String?,
    val leaguePoints: Int,
    val wins: Int,
    val losses: Int,
    val hotStreak: Boolean,
    val inactive: Boolean,
)
