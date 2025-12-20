package com.pamn.ggmatch.app.architecture.control.profile.commandsHandlers

import com.pamn.ggmatch.app.architecture.control.profile.commands.UpsertUserProfileCommand
import com.pamn.ggmatch.app.architecture.io.profile.ProfileRepository
import com.pamn.ggmatch.app.architecture.model.profile.UserProfile
import com.pamn.ggmatch.app.architecture.model.profile.preferences.Preferences
import com.pamn.ggmatch.app.architecture.sharedKernel.control.CommandHandler
import com.pamn.ggmatch.app.architecture.sharedKernel.result.AppError
import com.pamn.ggmatch.app.architecture.sharedKernel.result.Result
import com.pamn.ggmatch.app.architecture.sharedKernel.time.TimeProvider

class UpsertUserProfileCommandHandler(
    private val profileRepository: ProfileRepository,
    private val timeProvider: TimeProvider,
) : CommandHandler<UpsertUserProfileCommand, Unit> {

    override suspend operator fun invoke(command: UpsertUserProfileCommand): Result<Unit, AppError> {
        val preferences =
            Preferences(
                favoriteRoles = command.favoriteRoles,
                languages = command.languages,
                playSchedule = command.playSchedule,
                playstyle = command.playstyle,
            )

        return when (val existing = profileRepository.get(command.userId)) {
            is Result.Error -> existing
            is Result.Ok -> {
                val profile =
                    existing.value
                        ?: UserProfile.createNew(
                            id = command.userId,
                            timeProvider = timeProvider,
                        )

                profile.changeUsername(command.username, timeProvider)
                profile.changeRiotAccount(command.riotAccount, timeProvider)
                profile.updatePreferences(preferences, timeProvider)

                profileRepository.addOrUpdate(profile)
            }
        }
    }
}
