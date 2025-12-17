package com.pamn.ggmatch.app.architecture.control.matchmaking.commandsHandlers

import com.pamn.ggmatch.app.architecture.control.matchmaking.commands.UpsertMatchPreferencesCommand
import com.pamn.ggmatch.app.architecture.io.preferences.MatchPreferencesRepository
import com.pamn.ggmatch.app.architecture.model.preferences.MatchPreferencesProfile
import com.pamn.ggmatch.app.architecture.model.preferences.preferences.MatchPreferences
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider

class UpsertMatchPreferencesCommandHandler(
    private val matchPreferencesRepository: MatchPreferencesRepository,
    private val timeProvider: TimeProvider,
) {
    suspend fun handle(command: UpsertMatchPreferencesCommand): Result<Unit, AppError> {
        val existingResult = matchPreferencesRepository.get(command.userId)

        return when (existingResult) {
            is Result.Error -> existingResult
            is Result.Ok -> {
                val newMatchPreferences =
                    MatchPreferences(
                        roles = command.roles,
                        languages = command.languages,
                        schedules = command.schedules,
                        playstyles = command.playstyles,
                    )

                val existingProfile = existingResult.value

                val profileToPersist =
                    existingProfile?.also { currentProfile ->
                        currentProfile.update(newMatchPreferences, timeProvider)
                    }
                        ?: MatchPreferencesProfile.createNew(
                            userId = command.userId,
                            preferences = newMatchPreferences,
                            timeProvider = timeProvider,
                        )

                if (existingProfile == null) {
                    matchPreferencesRepository.add(profileToPersist)
                } else {
                    matchPreferencesRepository.update(profileToPersist)
                }
            }
        }
    }
}
