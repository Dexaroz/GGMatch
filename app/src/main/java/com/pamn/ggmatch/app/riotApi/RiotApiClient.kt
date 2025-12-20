package com.pamn.ggmatch.app.riotApi

import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.riotApi.dto.LeagueEntryDto
import com.pamn.ggmatch.app.riotApi.dto.RiotAccountDto
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result

interface RiotApiClient {
    suspend fun getPuuidByRiotId(gameName: String, tagLine: String): Result<RiotAccountDto, AppError>
    suspend fun getLeagueEntriesByPuuid(puuid: String): Result<List<LeagueEntryDto>, AppError>
}