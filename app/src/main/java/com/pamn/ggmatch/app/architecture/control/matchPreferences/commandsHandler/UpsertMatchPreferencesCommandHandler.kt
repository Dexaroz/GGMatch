package com.pamn.ggmatch.app.architecture.control.matchmaking.commandsHandlers

import com.pamn.ggmatch.app.architecture.control.matchmaking.commands.UpsertMatchPreferencesCommand
import com.pamn.ggmatch.app.architecture.io.preferences.MatchPreferencesRepository
import com.pamn.ggmatch.app.architecture.model.matchPreferences.MatchPreferences
import com.pamn.ggmatch.app.architecture.model.matchPreferences.preferences.Preferences
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
                val newPreferences =
                    Preferences(
                        roles = command.roles,
                        languages = command.languages,
                        schedules = command.schedules,
                        playstyles = command.playstyles,
                    )

                val existingProfile = existingResult.value

                val profileToPersist =
                    existingProfile?.also { currentProfile ->
                        currentProfile.update(newPreferences, timeProvider)
                    }
                        ?: MatchPreferences.createNew(
                            userId = command.userId,
                            preferences = newPreferences,
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
