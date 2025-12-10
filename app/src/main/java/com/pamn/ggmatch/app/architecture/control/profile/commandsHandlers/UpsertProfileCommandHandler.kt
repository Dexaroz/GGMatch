package com.pamn.ggmatch.app.architecture.control.profile.commandsHandlers

import com.pamn.ggmatch.app.architecture.control.profile.commands.UpsertUserProfileCommand
import com.pamn.ggmatch.app.architecture.io.profile.ProfileRepository
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Preferences
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider

class UpsertUserProfileCommandHandler(
    private val profileRepository: ProfileRepository,
    private val timeProvider: TimeProvider,
) {
    suspend fun handle(command: UpsertUserProfileCommand): Result<Unit, AppError> {
        val existingResult = profileRepository.get(command.userId)

        return when (existingResult) {
            is Result.Error -> existingResult
            is Result.Ok -> {
                val preferences =
                    Preferences(
                        favoriteRoles = command.favoriteRoles,
                        languages = command.languages,
                        playSchedule = command.playSchedule,
                        playstyle = command.playstyle,
                    )

                val existing = existingResult.value

                val profile =
                    existing?.also { current ->
                        current.changeRiotAccount(command.riotAccount, timeProvider)
                        current.updatePreferences(preferences, timeProvider)
                    }
                        ?: UserProfile.createNew(
                            id = command.userId,
                            riotAccount = command.riotAccount,
                            preferences = preferences,
                            timeProvider = timeProvider,
                        )

                if (existing == null) {
                    profileRepository.add(profile)
                } else {
                    profileRepository.update(profile)
                }
            }
        }
    }
}