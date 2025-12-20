package com.pamn.ggmatch.app.architecture.control.profile.commandsHandlers

import com.pamn.ggmatch.app.architecture.control.profile.commands.VerifyRiotAccountCommand
import com.pamn.ggmatch.app.architecture.io.profile.ProfileRepository
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.profile.riotAccount.RiotAccount
import com.pamn.ggmatch.app.architecture.model.profile.riotAccount.SoloqStats
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider
import com.pamn.ggmatch.app.riotApi.RiotApiClient

class VerifyRiotAccountCommandHandler(
    private val riotApi: RiotApiClient,
    private val profileRepository: ProfileRepository,
    private val timeProvider: TimeProvider,
) : CommandHandler<VerifyRiotAccountCommand, Unit> {

    override suspend fun invoke(command: VerifyRiotAccountCommand): Result<Unit, AppError> {
        val puuid =
            when (val r = riotApi.getPuuidByRiotId(command.gameName, command.tagLine)) {
                is Result.Error -> return r
                is Result.Ok -> r.value.puuid
            }

        val entries =
            when (val r = riotApi.getLeagueEntriesByPuuid(puuid)) {
                is Result.Error -> return r
                is Result.Ok -> r.value
            }

        val soloq = entries.firstOrNull { it.queueType == "RANKED_SOLO_5x5" }

        val soloqStats =
            soloq?.let {
                SoloqStats(
                    tier = it.tier,
                    division = it.rank,
                    leaguePoints = it.leaguePoints,
                    wins = it.wins,
                    losses = it.losses,
                    hotStreak = it.hotStreak,
                    inactive = it.inactive,
                )
            }

        val profile =
            when (val get = profileRepository.get(command.userId)) {
                is Result.Error -> return get
                is Result.Ok -> get.value ?: UserProfile.createNew(id = command.userId, timeProvider = timeProvider)
            }

        val now = timeProvider.now()

        val verifiedRiotAccount =
            RiotAccount(
                gameName = command.gameName.trim(),
                tagLine = command.tagLine.trim(),
            ).markVerified(
                at = now,
                puuid = puuid,
                soloq = soloqStats,
            )

        profile.changeRiotAccount(verifiedRiotAccount, timeProvider)
        return profileRepository.addOrUpdate(profile)
    }
}
